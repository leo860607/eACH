package tw.org.twntch.bo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tw.org.twntch.db.dao.hibernate.BANK_GROUP_Dao;
import tw.org.twntch.db.dao.hibernate.EACHAPSTATUSTAB_Dao;
import tw.org.twntch.db.dao.hibernate.EACHSYSSTATUSTAB_Dao;
import tw.org.twntch.db.dao.hibernate.TCH_TURNON_Dao;
import tw.org.twntch.db.dao.hibernate.TURNOFF_Dao;
import tw.org.twntch.po.EACHAPSTATUSTAB;
import tw.org.twntch.po.EACHSYSSTATUSTAB;
import tw.org.twntch.po.OPCTRANSACTIONLOGTAB;
import tw.org.twntch.socket.Message;
import tw.org.twntch.socket.Message.Body;
import tw.org.twntch.socket.Message.Header;
import tw.org.twntch.socket.MessageConverter;
import tw.org.twntch.socket.SocketClient;
import tw.org.twntch.util.CodeUtils;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.PKCS7Util;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.zDateHandler;

public class TCH_TURNON_BO {
	private TCH_TURNON_Dao tch_turnon_Dao;
	private SocketClient socketClient;
	private EACHSYSSTATUSTAB_Dao eachsysstatustab_Dao;
	private EACHAPSTATUSTAB_Dao eachapstatustab_Dao;
	
	/**
	 * 取得交換所系統狀態
	 * @return
	 */
	public String getEachSysStatus(){
		String sysstatus = "";
		List<EACHSYSSTATUSTAB> list = eachsysstatustab_Dao.getEachSysStatus();
		if(list != null && list.size() > 0){
			sysstatus = list.get(0).getSYSSTATUS();
		}
		return sysstatus;
	}
	
	public String getEachApStatus(){
		String apstatus = "";
		List<EACHAPSTATUSTAB> list = eachapstatustab_Dao.getEachApStatus();
		if(list != null && list.size() > 0){
			apstatus = list.get(0).getAPSTATUS();
		}
		return apstatus;
	}
	
	public String preCheck(Map<String, String> params){
		Map rtnMap = new HashMap();
		String signvalue = "";
		if(StrUtils.isNotEmpty(params.get("signvalue"))){
			signvalue = params.get("signvalue").trim();
			PKCS7Util pkcs7Util = new PKCS7Util();
			//簽章驗證失敗
			try {
				if(!pkcs7Util.pkcs7Verify(signvalue)){
					rtnMap.put("result", "FALSE");
					rtnMap.put("msg", "簽章驗證失敗!");
				}
			} catch (Exception e) {
				e.printStackTrace();
				rtnMap.put("result", "FALSE");
				rtnMap.put("msg", "簽章驗證時發生錯誤!");
			}
		}else{
			//檢查交換所系統狀態，必須為關機狀態才可執行開機
			if(getEachSysStatus().equals("1")){
				rtnMap.put("result", "FALSE");
				rtnMap.put("msg", "系統已開機，不可重複執行開機作業!");
			}
		}
		return JSONUtils.map2json(rtnMap);
	}
	
	public String iKeyCheck(Map<String, String> params){
		Map rtnMap = new HashMap();
		//iKey驗證
		rtnMap = new CodeUtils().iKeyVerification(params.get("RAOName"));	
		System.out.println("RTNMAP = " + rtnMap);
		return JSONUtils.map2json(rtnMap);
	}
	
	/**
	 * 送出電文
	 * @return
	 */
	public String send(Map<String, String> params){
		String result = "";
		String paramName;
		String paramValue;
		
		String BGBK_ID = "";
		paramName = "BGBK_ID";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			BGBK_ID = paramValue;
		}
		
		Map rtnMap = new HashMap();
		try{
			/* 產生電文內容
			<header> 
		        <SystemHeader>eACH01</SystemHeader> 
		        <MsgType>0100</MsgType> 
		        <PrsCode>1100</PrsCode> 
		        <Stan>XXXXXXX</Stan> 
		        <InBank>0000000</InBank> 
		        <OutBank>9990000</OutBank> 
		        <DateTime>YYYYMMDDHHMMSS</DateTime> 
		        <RspCode>0000</RspCode> 
		    </header> 
		    <body> 
		    	<WebStep>01</WebStep> 
		    </body> 
			*/
			Header msgHeader = new Header();
			msgHeader.setSystemHeader("eACH01");
			msgHeader.setMsgType("0100");
			msgHeader.setPrsCode("1100");
			msgHeader.setStan(tch_turnon_Dao.getStan());
			msgHeader.setInBank(BGBK_ID);
			msgHeader.setOutBank("9990000");	//20150109 FANNY說 票交發動的電文，「OUTBANK」必須固定為「9990000」
			msgHeader.setDateTime(zDateHandler.getDateNum()+zDateHandler.getTheTime().replaceAll(":", ""));
			msgHeader.setRspCode("0000");
			Message msg = new Message();
			msg.setHeader(msgHeader);
			Body body = new Body();
			body.setWebStep("01");
			msg.setBody(body);
			String telegram = MessageConverter.marshalling(msg);
			
			//TODO 測試時註解
			rtnMap = socketClient.send(telegram);
//			rtnMap.put("result", "TRUE");
//			rtnMap.put("msg", "測試成功 => \nSTAN = " + msgHeader.getStan() + "\n" + telegram);
			
			rtnMap.put("STAN", msgHeader.getStan());
			
			//TODO 測試用，新增假資料
			//turnoff_Dao.insertTestData(BGBK_ID, msgHeader.getStan());
		}catch(Exception e){
			e.printStackTrace();
		}
		result = JSONUtils.map2json(rtnMap);
		return result;
	}
	
	//以「STAN-交易追蹤序號」查詢日誌檔
	public String getDataByStan(Map<String, String> params){
		String result = "";
		String paramName;
		String paramValue;
		
		String STAN = "";
		paramName = "STAN";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			STAN = paramValue;
		}
		
		List<OPCTRANSACTIONLOGTAB> list = null;
		try{
			list = tch_turnon_Dao.getByStan(STAN);
			if(list != null){
				result = JSONUtils.toJson(list);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("json>>"+result);
		return result;
	}
	
	//以「TXDATE-交易日期」查詢日誌檔
	public String getDataByDate(Map<String, String> params){
		String result = "";
		String paramName;
		String paramValue;
		
		String TXDATE = "";
		paramName = "TXDATE";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			TXDATE = paramValue;
		}
		
		//需民國年轉西元年
		TXDATE = DateTimeUtils.convertDate(TXDATE, "yyyyMMdd", "yyyyMMdd");
		System.out.println(TXDATE);
		
		List<OPCTRANSACTIONLOGTAB> list = null;
		try{
			list = tch_turnon_Dao.getByTxdate(TXDATE);
			if(list != null){
				result = JSONUtils.toJson(list);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("json>>"+result);
		return result;
	}

	public TCH_TURNON_Dao getTch_turnon_Dao() {
		return tch_turnon_Dao;
	}

	public void setTch_turnon_Dao(TCH_TURNON_Dao tch_turnon_Dao) {
		this.tch_turnon_Dao = tch_turnon_Dao;
	}

	public SocketClient getSocketClient() {
		return socketClient;
	}

	public void setSocketClient(SocketClient socketClient) {
		this.socketClient = socketClient;
	}

	public EACHSYSSTATUSTAB_Dao getEachsysstatustab_Dao() {
		return eachsysstatustab_Dao;
	}

	public void setEachsysstatustab_Dao(EACHSYSSTATUSTAB_Dao eachsysstatustab_Dao) {
		this.eachsysstatustab_Dao = eachsysstatustab_Dao;
	}

	public EACHAPSTATUSTAB_Dao getEachapstatustab_Dao() {
		return eachapstatustab_Dao;
	}

	public void setEachapstatustab_Dao(EACHAPSTATUSTAB_Dao eachapstatustab_Dao) {
		this.eachapstatustab_Dao = eachapstatustab_Dao;
	}	

}
