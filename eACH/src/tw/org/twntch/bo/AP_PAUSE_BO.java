package tw.org.twntch.bo;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.struts.util.LabelValueBean;

import tw.org.twntch.db.dao.hibernate.AP_PAUSE_Dao;
import tw.org.twntch.db.dao.hibernate.BANK_GROUP_Dao;
import tw.org.twntch.db.dao.hibernate.EACHAPSTATUSTAB_Dao;
import tw.org.twntch.po.BANK_GROUP;
import tw.org.twntch.po.EACHAPSTATUSTAB;
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

public class AP_PAUSE_BO {
	private AP_PAUSE_Dao ap_pause_Dao;
	private BANK_GROUP_Dao bank_group_Dao;
	private SocketClient socketClient;
	private EACHAPSTATUSTAB_Dao eachapstatustab_Dao;
	private EACH_USERLOG_BO userlog_bo;//寫操作軌跡記錄
	
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
		//檢查交換所應用系統狀態，必須為暫停狀態才可執行通知
		if(getEachApStatus().equals("1")){
			rtnMap.put("result", "FALSE");
			rtnMap.put("msg", "應用系統未暫停，不可執行暫停通知!");
		}
		System.out.println("RTNMAP = " + rtnMap);
		return JSONUtils.map2json(rtnMap);
	}
	
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
		paramName = "BGBK_ID";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			BGBK_ID = paramValue;
		}
		
		Map rtnMap = null;
		
		try{
			/* 產生電文內容
			<header>
				<SystemHeader>eACH01</SystemHeader>
				<MsgType>0100</MsgType>
				<PrsCode>1110</PrsCode>
				<Stan>7385830</Stan>
				<InBank>0000000</InBank>
				<OutBank>9500000</OutBank>
				<DateTime>20141028173858</DateTime>
				<RspCode>0000</RspCode>
			</header>
			<body>
				<WebStep>02</WebStep>
				<ApId>2000</ApId>
			</body>
			*/
			Header msgHeader = new Header();
			msgHeader.setSystemHeader("eACH01");
			msgHeader.setMsgType("0100");
			msgHeader.setPrsCode("1110");
			msgHeader.setStan(ap_pause_Dao.getStan());
			msgHeader.setInBank(BGBK_ID);
			msgHeader.setOutBank("9990000");	//20150109 FANNY說 票交發動的電文，「OUTBANK」必須固定為「9990000」
			msgHeader.setDateTime(zDateHandler.getDateNum()+zDateHandler.getTheTime().replaceAll(":", ""));
			msgHeader.setRspCode("0000");
			Body msgBody = new Body();
			//20150120 HUANGPU Daivd說對方從1100改為2000
			msgBody.setApId("2000");
			msgBody.setWebStep("02");
			Message msg = new Message();
			msg.setHeader(msgHeader);
			msg.setBody(msgBody);
			String telegram = MessageConverter.marshalling(msg);
			rtnMap = socketClient.send(telegram);
			rtnMap.put("STAN", msgHeader.getStan());
			result = JSONUtils.map2json(rtnMap);
			
			//TODO 測試用，新增假資料
			//ap_pause_Dao.insertTestData(BGBK_ID, msgHeader.getStan());
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	public String multipleSend(Map<String, String> params ,String webNo){
		String result = "";
		String paramName;
		String paramValue;
		
		String BGBK_ID = "";
		paramName = "BGBK_ID";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			BGBK_ID = paramValue;
		}
		
		Map rtnMap = null;
		
		try{
			/* 產生電文內容
			<header>
				<SystemHeader>eACH01</SystemHeader>
				<MsgType>0100</MsgType>
				<PrsCode>1110</PrsCode>
				<Stan>7385830</Stan>
				<InBank>0000000</InBank>
				<OutBank>9500000</OutBank>
				<DateTime>20141028173858</DateTime>
				<RspCode>0000</RspCode>
			</header>
			<body>
				<WebStep>02</WebStep>
				<ApId>2000</ApId>
			</body>
			 */
			Header msgHeader = new Header();
			msgHeader.setSystemHeader("eACH01");
			msgHeader.setMsgType("0100");
			msgHeader.setPrsCode("1110");
			msgHeader.setStan(webNo);
			msgHeader.setInBank(BGBK_ID);
			msgHeader.setOutBank("9990000");	//20150109 FANNY說 票交發動的電文，「OUTBANK」必須固定為「9990000」
			msgHeader.setDateTime(zDateHandler.getDateNum()+zDateHandler.getTheTime().replaceAll(":", ""));
			msgHeader.setRspCode("0000");
			Body msgBody = new Body();
			//20150120 HUANGPU Daivd說對方從1100改為2000
			msgBody.setApId("2000");
			msgBody.setWebStep("02");
			Message msg = new Message();
			msg.setHeader(msgHeader);
			msg.setBody(msgBody);
			String telegram = MessageConverter.marshalling(msg);
			rtnMap = socketClient.send(telegram);
			rtnMap.put("STAN", msgHeader.getStan());
			result = JSONUtils.map2json(rtnMap);
			
			//TODO 測試用，新增假資料
			//ap_pause_Dao.insertTestData(BGBK_ID, msgHeader.getStan());
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
			list = ap_pause_Dao.getByStan(STAN);
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
		
		String BGBKID = "";
		paramName = "BGBKID";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue) && !paramValue.equalsIgnoreCase("0000000")){
			BGBKID = paramValue;
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
//			list = ap_pause_Dao.getByTxdate(TXDATE, BGBKID);
			list = ap_pause_Dao.getByTxdate(TXDATE, BGBKID ,RSPCODE);
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
		String BGBK_ID_LIST = "";
		paramName = "BGBK_ID_LIST";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			BGBK_ID_LIST = paramValue;
		}

		Map rtnMap = null;
		if(StrUtils.isNotEmpty(BGBK_ID_LIST)){
			String bgbkIdAry[] = BGBK_ID_LIST.split(",");
			webNo = ap_pause_Dao.getStan();
			Map map = new HashMap();
			rtnMap = new HashMap();
			for(int i = 0; i < bgbkIdAry.length; i++){
				map.put("BGBK_ID", bgbkIdAry[i]);
//				rtnMap.put(bgbkIdAry[i], JSONUtils.json2map(send(map)));
				rtnMap.put(bgbkIdAry[i], JSONUtils.json2map(multipleSend(map, webNo)));
			}
		}
		if(rtnMap != null){result = JSONUtils.map2json(rtnMap);}
		System.out.println("json>>" + result);
		return result;
	}
	
	public AP_PAUSE_Dao getAp_pause_Dao() {
		return ap_pause_Dao;
	}

	public void setAp_pause_Dao(AP_PAUSE_Dao ap_pause_Dao) {
		this.ap_pause_Dao = ap_pause_Dao;
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
	
}
