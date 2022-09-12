package tw.org.twntch.bo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tw.org.twntch.db.dao.hibernate.AGENT_TURNOFF_Dao;
import tw.org.twntch.db.dao.hibernate.EACHAPSTATUSTAB_Dao;
import tw.org.twntch.db.dao.hibernate.EACHSYSSTATUSTAB_Dao;
import tw.org.twntch.po.EACHAPSTATUSTAB;
import tw.org.twntch.po.EACHSYSSTATUSTAB;
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

public class AGENT_TURNOFF_BO {
	private AGENT_TURNOFF_Dao agent_turnoff_Dao;
	private AGENT_SEND_PROFILE_BO agent_send_profile_bo;
	private SocketClient socketClient;
	private EACHSYSSTATUSTAB_Dao eachsysstatustab_Dao;
	private EACHAPSTATUSTAB_Dao eachapstatustab_Dao;
	private EACH_USERLOG_BO userlog_bo;//寫操作軌跡記錄
	
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
//		20150416 edit by hugo req by 李建利   開機狀態可執行關機通知，但系統為關機狀態不可執行關機通知
//		req by David  應用系統必須是簽退(暫停)，才可發佈關機通知
//		最後討論結果 只檢核關機狀態下不可執行關機通知   FANNY 補充 系統關機後 關機通知仍可收發 故不檢核
		
// 		if(!getEachApStatus().equals("9")){
//			rtnMap.put("result", "FALSE");
//			rtnMap.put("msg", "應用系統非暫停(簽退)狀態，不可執行關機通知! 請先執行交換所應用系統暫停作業");
//		}
// 		if(getEachSysStatus().equals("9")){
// 			rtnMap.put("result", "FALSE");
// 			rtnMap.put("msg", "系統為關機狀態，不可執行關機通知!");
// 		}
		
		
		//檢查交換所系統狀態，必須為關機狀態才可執行通知
//		if(getEachSysStatus().equals("1")){
//			rtnMap.put("result", "FALSE");
//			rtnMap.put("msg", "系統為開機狀態，不可執行關機通知!");
//		}
		return JSONUtils.map2json(rtnMap);
	}
	
	/**
	 * 重送電文
	 * @param params
	 * @return
	 */
	public String resend(Map<String, String> params){
		String result = "";
		String paramName;
		String paramValue;
		String webNo = "";
		String ABBR_ID_LIST = "";
		paramName = "ABBR_ID_LIST";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			ABBR_ID_LIST = paramValue;
		}

		Map rtnMap = null;
		if(StrUtils.isNotEmpty(ABBR_ID_LIST)){
			webNo = agent_turnoff_Dao.getStan();
			String abbrIdAry[] = ABBR_ID_LIST.split(",");
			Map map = new HashMap();
			rtnMap = new HashMap();
			for(int i = 0; i < abbrIdAry.length; i++){
				map.put("ABBR_ID", abbrIdAry[i]);
//				rtnMap.put(bgbkIdAry[i], JSONUtils.json2map(send(map)));
				rtnMap.put(abbrIdAry[i], JSONUtils.json2map(multipleSend(map, webNo)));
			}
		}
		if(rtnMap != null){result = JSONUtils.map2json(rtnMap);}
		System.out.println("json>>" + result);
		return result;
	}
	
	/**
	 * 送出電文
	 * @return
	 */
	public String send(Map<String, String> params){
		String result = "";
		String paramName;
		String paramValue;
		
		String ABBR_ID = "";
		paramName = "ABBR_ID";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			ABBR_ID = paramValue;
		}
		
		Map rtnMap = null;
		
		try{
			/* 產生電文內容
			<header> 
	        	<SystemHeader>eACH01</SystemHeader> 
		        <MsgType>0100</MsgType> 
		        <PrsCode>9104</PrsCode> 
		        <Stan>XXXXXXX</Stan> 
		        <InBank>0000000</InBank> 
		        <OutBank>9990000</OutBank> 
		        <DateTime>YYYYMMDDHHMMSS</DateTime> 
		        <RspCode>0000</RspCode> 
    		</header> 
		    <body> 
	            <Type></Type> 
	            <CompanyId></CompanyId> 
	            <Msg></Msg> 
		    </body> 
			*/
			Header msgHeader = new Header();
			msgHeader.setSystemHeader("eACH01");
			msgHeader.setMsgType("0100");
			msgHeader.setPrsCode("9104");
			msgHeader.setStan(agent_turnoff_Dao.getStan());
			msgHeader.setInBank("0000000");
			msgHeader.setOutBank("9990000");	//20150109 FANNY說 票交發動的電文，「OUTBANK」必須固定為「9990000」
			msgHeader.setDateTime(zDateHandler.getDateNum()+zDateHandler.getTheTime().replaceAll(":", ""));
			msgHeader.setRspCode("0000");
			Message msg = new Message();
			msg.setHeader(msgHeader);
			Body body = new Body();
			body.setType("1009");
			body.setCompanyId(ABBR_ID);
			body.setMsg("");
			msg.setBody(body);
			String telegram = MessageConverter.marshalling(msg);
			rtnMap = socketClient.send(telegram);
			rtnMap.put("STAN", msgHeader.getStan());
			result = JSONUtils.map2json(rtnMap);
			System.out.println();
			//TODO 測試用，新增假資料
			//turnoff_Dao.insertTestData(ABBR_ID, msgHeader.getStan());
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * 送出電文
	 * @return
	 */
	public String multipleSend (Map<String, String> params ,String webNo){
		String result = "";
		String paramName;
		String paramValue;
		
		String ABBR_ID = "";
		paramName = "ABBR_ID";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			ABBR_ID = paramValue;
		}
		
		Map rtnMap = null;
		
		try{
			/* 產生電文內容
			<header> 
	        	<SystemHeader>eACH01</SystemHeader> 
		        <MsgType>0100</MsgType> 
		        <PrsCode>9104</PrsCode> 
		        <Stan>XXXXXXX</Stan> 
		        <InBank>0000000</InBank> 
		        <OutBank>9990000</OutBank> 
		        <DateTime>YYYYMMDDHHMMSS</DateTime> 
		        <RspCode>0000</RspCode> 
    		</header> 
		    <body> 
	            <Type></Type> 
	            <CompanyId></CompanyId> 
	            <Msg></Msg> 
		    </body> 
			*/
			Header msgHeader = new Header();
			msgHeader.setSystemHeader("eACH01");
			msgHeader.setMsgType("0100");
			msgHeader.setPrsCode("9104");
			msgHeader.setStan(webNo);
			msgHeader.setInBank("0000000");
			msgHeader.setOutBank("9990000");	//20150109 FANNY說 票交發動的電文，「OUTBANK」必須固定為「9990000」
			msgHeader.setDateTime(zDateHandler.getDateNum()+zDateHandler.getTheTime().replaceAll(":", ""));
			msgHeader.setRspCode("0000");
			Message msg = new Message();
			msg.setHeader(msgHeader);
			Body body = new Body();
			body.setType("1009");
			body.setCompanyId(ABBR_ID);
			body.setMsg("");
			msg.setBody(body);
			String telegram = MessageConverter.marshalling(msg);
			rtnMap = socketClient.send(telegram);
			rtnMap.put("STAN", msgHeader.getStan());
			result = JSONUtils.map2json(rtnMap);
			System.out.println();
			//TODO 測試用，新增假資料
			//turnoff_Dao.insertTestData(ABBR_ID, msgHeader.getStan());
		}catch(Exception e){
			e.printStackTrace();
		}
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
			list = agent_turnoff_Dao.getByStan(STAN);
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
		
		String ABBRID = "";
		paramName = "ABBRID";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue) && !paramValue.equals("0000000")){
			ABBRID = paramValue;
		}
		String RSPCODE = "";
		paramName = "RSPCODE";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue) ){
			RSPCODE = paramValue;
		}
		
		//操作軌跡記錄用
		String SERCHSTRS = "";
		paramName = "SERCHSTRS";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue) ){
			SERCHSTRS = paramValue;
		}
		//將頁面上的查詢條件放進pkMap
		Map<String,Object> pkMap = new HashMap<String,Object>();
		pkMap.put("serchStrs",SERCHSTRS);
		//如果有錯誤要將訊息放進去
		Map<String,Object> msgMap = new HashMap<String,Object>();
		
		//需民國年轉西元年
		TXDATE = DateTimeUtils.convertDate(TXDATE, "yyyyMMdd", "yyyyMMdd");
		System.out.println(TXDATE);
		
		List<OPCTRANSACTIONLOGTAB> list = null;
		try{
			list = agent_turnoff_Dao.getByTxdate(TXDATE, ABBRID , RSPCODE);
			//查詢成功
			if(list != null){
				result = JSONUtils.toJson(list);
				//寫操作軌跡記錄(成功)
				userlog_bo.writeLog("C",null,null,pkMap);
			}
			//失敗
			else{
				//寫操作軌跡記錄(失敗)
				msgMap.put("msg","查詢失敗");
				userlog_bo.writeFailLog("C",msgMap,null,null,pkMap);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("json>>"+result);
		return result;
	}	

	public AGENT_TURNOFF_Dao getAgent_turnoff_Dao() {
		return agent_turnoff_Dao;
	}

	public void setAgent_turnoff_Dao(AGENT_TURNOFF_Dao agent_turnoff_Dao) {
		this.agent_turnoff_Dao = agent_turnoff_Dao;
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

	public EACH_USERLOG_BO getUserlog_bo() {
		return userlog_bo;
	}

	public void setUserlog_bo(EACH_USERLOG_BO userlog_bo) {
		this.userlog_bo = userlog_bo;
	}

	public AGENT_SEND_PROFILE_BO getAgent_send_profile_bo() {
		return agent_send_profile_bo;
	}

	public void setAgent_send_profile_bo(AGENT_SEND_PROFILE_BO agent_send_profile_bo) {
		this.agent_send_profile_bo = agent_send_profile_bo;
	}

}
