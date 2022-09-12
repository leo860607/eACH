package tw.org.twntch.bo;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.struts.util.LabelValueBean;

import tw.org.twntch.db.dao.hibernate.BANK_GROUP_Dao;
import tw.org.twntch.db.dao.hibernate.CHG_MAC_Dao;
import tw.org.twntch.po.BANK_GROUP;
import tw.org.twntch.po.EACH_USERLOG;
import tw.org.twntch.po.OPCTRANSACTIONLOGTAB;
import tw.org.twntch.socket.Message;
import tw.org.twntch.socket.Message.Body;
import tw.org.twntch.socket.Message.Header;
import tw.org.twntch.socket.MessageConverter;
import tw.org.twntch.socket.SocketClient;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.zDateHandler;

public class CHG_MAC_BO {
	private ASYNC_MAC_BO async_mac_bo;
	private CHG_MAC_Dao chg_mac_Dao;
	private BANK_GROUP_Dao bank_group_Dao;
	private SocketClient socketClient;
	private EACH_USERLOG_BO userlog_bo;
	/**
	 * 取得操作行代號清單
	 * select distinct OPBK_ID where Currentdate between 啟用日期 and 停用日期
	 * @return
	 */
	public List<LabelValueBean> getBgbkIdList(){
		List<BANK_GROUP> list = bank_group_Dao.getBgbkIdList_2();
		List<LabelValueBean> beanList = new LinkedList<LabelValueBean>();
		LabelValueBean bean = null;
		for(BANK_GROUP po : list){
			bean = new LabelValueBean(po.getBGBK_ID() + " - " + po.getBGBK_NAME(), po.getBGBK_ID());
			beanList.add(bean);
		}
		System.out.println("beanList>>"+beanList);
		return beanList;
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
//		paramName = "BGBK_ID";
		paramName = "OPBK_ID";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			BGBK_ID = paramValue;
		}
		
		String IDFIELD = "";
		paramName = "IDFIELD";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			IDFIELD = paramValue;
		}
		
		String CHG_PCODE = "";
		paramName = "CHG_PCODE";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			CHG_PCODE = paramValue;
		}
		
		
		if(CHG_PCODE.equals("1200")){
//			params.put("OPBK_ID", BGBK_ID);
//			result = async_mac_bo.send(params);
			result = async_mac_bo.single_send(params);
		}else{
			Map rtnMap = null;
			
			try{
				/* 產生電文內容
				<header>
					<SystemHeader>eACH01</SystemHeader>
					<MsgType>0100</MsgType>
					<PrsCode>1210</PrsCode>
					<Stan>7385830</Stan>
					<InBank>0000000</InBank>
					<OutBank>9500000</OutBank>
					<DateTime>20141028173858</DateTime>
					<RspCode>0000</RspCode>
				</header>
				<body>
					<KeyID>02</KeyID>
				</body>

				*/
				Header msgHeader = new Header();
				msgHeader.setSystemHeader("eACH01");
				msgHeader.setMsgType("0100");
				msgHeader.setPrsCode("1210");
				msgHeader.setStan(chg_mac_Dao.getStan());
				msgHeader.setInBank(BGBK_ID);
				msgHeader.setOutBank("");
//				msgHeader.setOutBank("9990000");
				msgHeader.setDateTime(zDateHandler.getDateNum()+zDateHandler.getTheTime().replaceAll(":", ""));
				msgHeader.setRspCode("0000");
				Body msgBody = new Body();
				msgBody.setKeyID(IDFIELD);
				Message msg = new Message();
				msg.setHeader(msgHeader);
				msg.setBody(msgBody);
				String telegram = MessageConverter.marshalling(msg);
				rtnMap = socketClient.send(telegram);
				rtnMap.put("STAN", msgHeader.getStan());
				rtnMap.put("CHG_PCODE", CHG_PCODE);
				rtnMap.put("IDFIELD", IDFIELD);
				result = JSONUtils.map2json(rtnMap);
				
				//TODO 測試用，新增假資料
				//chg_mac_Dao.insertTestData(BGBK_ID, msgHeader.getStan(), IDFIELD);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		return result;
	}
	
	
	public String multipleSend(Map<String, String> params ,String webNo){
		String result = "";
		String paramName;
		String paramValue;
		
		String BGBK_ID = "";
//		paramName = "BGBK_ID";
		paramName = "OPBK_ID";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			BGBK_ID = paramValue;
		}
		
		String IDFIELD = "";
		paramName = "IDFIELD";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			IDFIELD = paramValue;
		}
		String CHG_PCODE = "";
		paramName = "CHG_PCODE";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			CHG_PCODE = paramValue;
		}
		
		if(CHG_PCODE.equals("1200")){
			params.put("OPBK_ID", BGBK_ID);
//			20150616 edit by hugo 
//			result = async_mac_bo.resend(params);
			result = async_mac_bo.multipleSend(params ,webNo);
		}else{
			Map rtnMap = null;
			
			try{
				/* 產生電文內容
				<header>
					<SystemHeader>eACH01</SystemHeader>
					<MsgType>0100</MsgType>
					<PrsCode>1210</PrsCode>
					<Stan>7385830</Stan>
					<InBank>0000000</InBank>
					<OutBank>9500000</OutBank>
					<DateTime>20141028173858</DateTime>
					<RspCode>0000</RspCode>
				</header>
				<body>
					<KeyID>02</KeyID>
				</body>

				 */
				Header msgHeader = new Header();
				msgHeader.setSystemHeader("eACH01");
				msgHeader.setMsgType("0100");
				msgHeader.setPrsCode("1210");
				msgHeader.setStan(webNo);
				msgHeader.setInBank(BGBK_ID);
				msgHeader.setOutBank("");
				msgHeader.setDateTime(zDateHandler.getDateNum()+zDateHandler.getTheTime().replaceAll(":", ""));
				msgHeader.setRspCode("0000");
				Body msgBody = new Body();
				msgBody.setKeyID(IDFIELD);
				Message msg = new Message();
				msg.setHeader(msgHeader);
				msg.setBody(msgBody);
				String telegram = MessageConverter.marshalling(msg);
				rtnMap = socketClient.send(telegram);
				rtnMap.put("STAN", msgHeader.getStan());
				result = JSONUtils.map2json(rtnMap);
				
				//TODO 測試用，新增假資料
				//chg_mac_Dao.insertTestData(BGBK_ID, msgHeader.getStan(), IDFIELD);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		return result;
	}
	
	/**
	 * 重送電文
	 * @param params
	 * @return
	 */
	public String resend(Map<String, String> params){
		String result = "";
		String paramName ;
		String paramValue;
		String webNo = "";
		String DATA_LIST = "";
		String OPBK_ID_STR= "";
		paramName = "DATA_LIST";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			DATA_LIST = paramValue;
		}

		Map rtnMap = null;
		//DATA_LIST = [BANKID_1@IDFIELD_1,BANKID_2@IDFIELD_2,....]
		if(StrUtils.isNotEmpty(DATA_LIST)){
			String dataAry[] = DATA_LIST.split(",");
			webNo = chg_mac_Dao.getStan();
			Map map = new HashMap();
			rtnMap = new HashMap();
			for(int i = 0; i < dataAry.length; i++){
//				map.put("BGBK_ID", dataAry[i].split("@")[0]);
				map.put("OPBK_ID", dataAry[i].split("@")[0]);
				map.put("CHG_PCODE", dataAry[i].split("@")[1]);
				map.put("IDFIELD", dataAry[i].split("@")[2]);
//				map = JSONUtils.json2map(send(map));
				map = JSONUtils.json2map(multipleSend(map, webNo));
				rtnMap.put(map.get("STAN"), map);
				
				OPBK_ID_STR+= dataAry[i].split("@")[0]+",";
			}
			rtnMap.put("opc_type", "1");
			rtnMap.put("RESEND_OPBK_ID", OPBK_ID_STR);
			rtnMap.put("result", map.get("result"));
			rtnMap.put("AOP_SATN", map.get("STAN"));
			
		}
		if(rtnMap != null){result = JSONUtils.map2json(rtnMap);}
		System.out.println("json>>" + result);
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
			list = chg_mac_Dao.getByStan(STAN);
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
		
		Map<String,String> pkmap = new HashMap<String,String>();
		Map<String,String> tmpmap = new HashMap<String,String>();
		Map<String,String> msgmap = new HashMap<String,String>();
		String TXDATE = "";
		paramName = "TXDATE";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			TXDATE = paramValue;
		}
		
		String BGBKID = "";
//		paramName = "BGBKID";
		paramName = "OPBK_ID";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue) && !paramValue.equalsIgnoreCase("0000000")){
			BGBKID = paramValue;
		}
		
		String IDFIELD = "";
		paramName = "IDFIELD";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			IDFIELD = paramValue;
		}
		
		String RSPCODE = "";
		paramName = "RSPCODE";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue) ){
			RSPCODE = paramValue;
		}
		String CHG_PCODE = "";
		paramName = "CHG_PCODE";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue) ){
			CHG_PCODE = paramValue;
		}
		
		String serchStrs = "{}";
		paramName = "serchStrs";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue) ){
			serchStrs = paramValue;
		}
		
		tmpmap = JSONUtils.json2map(serchStrs);
		if(CHG_PCODE.equals("1200")){
			tmpmap.put("CHG_PCODE", "押碼基碼同步");
		}else{
			tmpmap.put("CHG_PCODE", "變更押碼基碼");
		}
		serchStrs = JSONUtils.map2json(tmpmap);
		pkmap.put("serchStrs",serchStrs);
		System.out.println("pkmap>>"+pkmap);
		
		//需民國年轉西元年
		TXDATE = DateTimeUtils.convertDate(TXDATE, "yyyyMMdd", "yyyyMMdd");
		//System.out.println(TXDATE);
		
		List<OPCTRANSACTIONLOGTAB> list = null;
		try{
			list = chg_mac_Dao.getByTxdate(TXDATE, CHG_PCODE, IDFIELD, BGBKID , RSPCODE);
			if(list != null){
				result = JSONUtils.toJson(list);
			}
			userlog_bo.writeLog("C", null, null, pkmap);
		}catch(Exception e){
			e.printStackTrace();
			msgmap.put("msg", "查詢失敗，系統異常");
			userlog_bo.writeFailLog("C", msgmap, null, null, pkmap);
		}
		System.out.println("json>>"+result);
		return result;
	}
	
	public ASYNC_MAC_BO getAsync_mac_bo() {
		return async_mac_bo;
	}

	public void setAsync_mac_bo(ASYNC_MAC_BO async_mac_bo) {
		this.async_mac_bo = async_mac_bo;
	}

	public CHG_MAC_Dao getChg_mac_Dao() {
		return chg_mac_Dao;
	}

	public void setChg_mac_Dao(CHG_MAC_Dao chg_mac_Dao) {
		this.chg_mac_Dao = chg_mac_Dao;
	}

	public BANK_GROUP_Dao getBank_group_Dao() {
		return bank_group_Dao;
	}

	public void setBank_group_Dao(BANK_GROUP_Dao bank_group_Dao) {
		this.bank_group_Dao = bank_group_Dao;
	}

	public SocketClient getSocketClient() {
		return socketClient;
	}

	public void setSocketClient(SocketClient socketClient) {
		this.socketClient = socketClient;
	}

	public EACH_USERLOG_BO getUserlog_bo() {
		return userlog_bo;
	}

	public void setUserlog_bo(EACH_USERLOG_BO userlog_bo) {
		this.userlog_bo = userlog_bo;
	}	
	
	
}
