package tw.org.twntch.bo;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.apache.struts.util.LabelValueBean;
import org.apache.log4j.Logger;

import tw.org.twntch.db.dao.hibernate.AP_PAUSE_Dao;
import tw.org.twntch.db.dao.hibernate.BANK_GROUP_Dao;
import tw.org.twntch.db.dao.hibernate.EACHSYSSTATUSTAB_Dao;
import tw.org.twntch.db.dao.hibernate.EACH_BATCH_NOTIFY_Dao;
import tw.org.twntch.db.dao.hibernate.EACH_BATCH_STATUS_Dao;
import tw.org.twntch.db.dao.hibernate.MSG_NOTICE_Dao;
import tw.org.twntch.po.BANK_GROUP;
import tw.org.twntch.po.BULLETIN;
import tw.org.twntch.po.EACHSYSSTATUSTAB;
import tw.org.twntch.po.EACH_BATCH_NOTIFY;
import tw.org.twntch.po.EACH_BATCH_NOTIFY_PK;
import tw.org.twntch.po.EACH_BATCH_STATUS;
import tw.org.twntch.po.OPCTRANSACTIONLOGTAB;
import tw.org.twntch.po.OPCTRANSACTIONLOGTAB_PK;
import tw.org.twntch.socket.Message;
import tw.org.twntch.socket.Message.Body;
import tw.org.twntch.socket.Message.Header;
import tw.org.twntch.socket.MessageConverter;
import tw.org.twntch.socket.SocketClient;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.zDateHandler;

public class MSG_NOTICE_BO {
	private MSG_NOTICE_Dao msg_notice_Dao;
	private BANK_GROUP_Dao bank_group_Dao;
	private SocketClient socketClient;
	private EACH_USERLOG_BO userlog_bo;//寫操作軌跡記錄
	private EACH_BATCH_STATUS_Dao batch_status_Dao;
	private EACH_BATCH_NOTIFY_Dao batch_notify_Dao;
	private BULLETIN_BO bulletin_bo;
	private AP_PAUSE_Dao ap_pause_Dao;
	private EACHSYSSTATUSTAB_Dao eachsysstatustab_Dao;
	private WK_DATE_BO wk_date_bo;
	private EACHSYSSTATUSTAB_BO eachsysstatustab_bo; 
	private Logger logger = Logger.getLogger(getClass());
	
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
	 * 重送電文
	 * @param params
	 * @return
	 */
	public String resend(Map<String, String> params){
		String result = "";
		String paramName;
		String paramValue;
		String webNo = "";
		String ID_LIST = "";
		paramName = "ID_LIST";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			ID_LIST = paramValue;
		}

		Map rtnMap = null;
		if(StrUtils.isNotEmpty(ID_LIST)){
			String idAry[] = ID_LIST.split(",");
			Map map = new HashMap();
			rtnMap = new HashMap();
			OPCTRANSACTIONLOGTAB po = null;
			OPCTRANSACTIONLOGTAB_PK pk = null;
			webNo = msg_notice_Dao.getStan();
			//用STAN找出操作行及訊息內容重送
			for(int i = 0; i < idAry.length; i++){
				map = new HashMap();
				pk = new OPCTRANSACTIONLOGTAB_PK(idAry[i].split("@")[0],idAry[i].split("@")[1]);
				po = msg_notice_Dao.get(pk);
				if(po != null){
					map.put("BGBK_ID", po.getBANKID());
					map.put("MESSAGE", po.getDATAFIELD());
//					20150421 add by hugo
					map.put("NOTICEID", po.getIDFIELD());
//					20150421 edit by hugo 這邊應該跟其他通知功能一樣 放BANKID即可WEBTRACENO要抓最新的
//					rtnMap.put(po.getWEBTRACENO(), JSONUtils.json2map(send(map)));
					rtnMap.put(po.getBANKID(), JSONUtils.json2map(multipleSend(map, webNo)));
				}else{
					map.put("result", "FALSE");
					map.put("msg", "找不到資料");
					rtnMap.put(pk.getSTAN(), map);
				}
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
		
		String BGBK_ID = "";
		paramName = "BGBK_ID";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			BGBK_ID = paramValue;
		}
		
		String MESSAGE = "";
		paramName = "MESSAGE";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			MESSAGE = paramValue;
		}
		String NoticeId = "";
		paramName = "NOTICEID";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			NoticeId = paramValue;
		}
		
		Map rtnMap = null;
		
		try{
			/* 產生電文內容
			<header>
				<SystemHeader>eACH01</SystemHeader>
				<MsgType>0100</MsgType>
				<PrsCode>1300</PrsCode>
				<Stan>7385830</Stan>
				<InBank>0000000</InBank>
				<OutBank>9500000</OutBank>
				<DateTime>20141028173858</DateTime>
				<RspCode>0000</RspCode>
			</header>
			<body>
				<NoticeId>1000</NoticeId>
				<NoticeData>input notice data here</NoticeData>
			</body>
			*/
			Header msgHeader = new Header();
			msgHeader.setSystemHeader("eACH01");
			msgHeader.setMsgType("0100");
			msgHeader.setPrsCode("1300");
			msgHeader.setStan(msg_notice_Dao.getStan());
			msgHeader.setInBank(BGBK_ID);
			msgHeader.setOutBank("9990000");	//20150109 FANNY說 票交發動的電文，「OUTBANK」必須固定為「9990000」
			msgHeader.setDateTime(zDateHandler.getDateNum()+zDateHandler.getTheTime().replaceAll(":", ""));
			msgHeader.setRspCode("0000");
			Body msgBody = new Body();
//			20150414 edit by hugo req by 票交李建利  UAT-20150411-07
//			msgBody.setNoticeId("1000");
			System.out.println("NoticeId>>"+NoticeId);
			msgBody.setNoticeId(NoticeId); 
			msgBody.setNoticeData(MESSAGE);
			Message msg = new Message();
			msg.setHeader(msgHeader);
			msg.setBody(msgBody);
			String telegram = MessageConverter.marshalling(msg);
			rtnMap = socketClient.send(telegram);
			rtnMap.put("STAN", msgHeader.getStan());
			rtnMap.put("NOTICEID", NoticeId);
			String message = java.net.URLDecoder.decode(MESSAGE, "UTF-8");
			rtnMap.put("MESSAGE", message);
			result = JSONUtils.map2json(rtnMap);
			
			//TODO 測試用，新增假資料
			//msg_notice_Dao.insertTestData(BGBK_ID, msgHeader.getStan(), MESSAGE);
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * 針對重送有多筆電文
	 * @return
	 */
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
		
		String MESSAGE = "";
		paramName = "MESSAGE";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			MESSAGE = paramValue;
			//20161102 中文轉換
			try {
				MESSAGE = java.net.URLEncoder.encode(MESSAGE,"UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		String NoticeId = "";
		paramName = "NOTICEID";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			NoticeId = paramValue;
		}
		
		Map rtnMap = null;
		
		try{
			/* 產生電文內容
			<header>
				<SystemHeader>eACH01</SystemHeader>
				<MsgType>0100</MsgType>
				<PrsCode>1300</PrsCode>
				<Stan>7385830</Stan>
				<InBank>0000000</InBank>
				<OutBank>9500000</OutBank>
				<DateTime>20141028173858</DateTime>
				<RspCode>0000</RspCode>
			</header>
			<body>
				<NoticeId>1000</NoticeId>
				<NoticeData>input notice data here</NoticeData>
			</body>
			 */
			Header msgHeader = new Header();
			msgHeader.setSystemHeader("eACH01");
			msgHeader.setMsgType("0100");
			msgHeader.setPrsCode("1300");
			msgHeader.setStan(webNo);
			msgHeader.setInBank(BGBK_ID);
			msgHeader.setOutBank("9990000");	//20150109 FANNY說 票交發動的電文，「OUTBANK」必須固定為「9990000」
			msgHeader.setDateTime(zDateHandler.getDateNum()+zDateHandler.getTheTime().replaceAll(":", ""));
			msgHeader.setRspCode("0000");
			Body msgBody = new Body();
//			20150414 edit by hugo req by 票交李建利  UAT-20150411-07
//			msgBody.setNoticeId("1000");
			System.out.println("NoticeId>>"+NoticeId);
			msgBody.setNoticeId(NoticeId); 
			msgBody.setNoticeData(MESSAGE);
			Message msg = new Message();
			msg.setHeader(msgHeader);
			msg.setBody(msgBody);
			String telegram = MessageConverter.marshalling(msg);
			rtnMap = socketClient.send(telegram);
			rtnMap.put("STAN", msgHeader.getStan());
			result = JSONUtils.map2json(rtnMap);
			
			//TODO 測試用，新增假資料
			//msg_notice_Dao.insertTestData(BGBK_ID, msgHeader.getStan(), MESSAGE);
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
			list = msg_notice_Dao.getByStan(STAN);
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
			list = msg_notice_Dao.getByTxdate(TXDATE, BGBKID , RSPCODE);
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
	 * 依照DATEMODE取得目前營業日、清算階段及前一清算階段、營業日(for AJAX)
	 * @return
	 */
	public String getBusinessDateAndClrphase(Map<String, String> params){
		Map<String, String> data = new HashMap<String, String>();
		List<EACHSYSSTATUSTAB> list = null;
		String businessDate = "", clrphase = "";
		String pre_businessDate = "", pre_clrphase = "";
		
		try{
			list = eachsysstatustab_Dao.getBusinessDate();
			if(list != null && list.size() > 0){
				businessDate = list.get(0).getBUSINESS_DATE();
				clrphase = list.get(0).getCLEARINGPHASE();
			}
			Map dateData = eachsysstatustab_bo.getRowData();
			//前一階段的營業日
	//		pre_businessDate = (String) dateData.get("THISDATE");
	//		時間為中午12前抓PREVDATE 12:00 後抓THISDATE
			if(zDateHandler.isInTimeRange(zDateHandler.getTheTime(), "00:00:00", "12:00:00")){
//			假日測試用
//			if(zDateHandler.isInTimeRange(zDateHandler.getTheTime(), "00:00:00", "15:10:00")){
				businessDate = (String) dateData.get("PREVDATE");
				pre_businessDate = (String) dateData.get("PREVDATE");
				System.out.println("businessDate>>"+businessDate);
				System.out.println("pre_businessDate>>"+pre_businessDate);
				businessDate = DateTimeUtils.convertDate(1, businessDate, "yyyyMMdd", "yyyyMMdd"); 
				pre_businessDate = DateTimeUtils.convertDate(1, pre_businessDate, "yyyyMMdd", "yyyyMMdd"); 
				pre_clrphase = (clrphase.equals("01")? "02" : "01");
			}
//			else if(zDateHandler.isInTimeRange(zDateHandler.getTheTime(), "12:00:00", "15:30:00")){
//				businessDate = (String) dateData.get("THISDATE");
//				pre_businessDate = (String) dateData.get("PREVDATE");
//				System.out.println("time in 12:00:00~15:30:00 >>");
//				System.out.println("businessDate>>"+businessDate);
//				System.out.println("pre_businessDate>>"+pre_businessDate);
//				businessDate = DateTimeUtils.convertDate(1, businessDate, "yyyyMMdd", "yyyyMMdd"); 
//				pre_businessDate = DateTimeUtils.convertDate(1, pre_businessDate, "yyyyMMdd", "yyyyMMdd"); 
//				pre_clrphase = (clrphase.equals("01")? "02" : "01");
//			}
			else{
//				businessDate = zDateHandler.getTWDate();
//				pre_businessDate = zDateHandler.getTWDate();
//				如果是營業日
				if(wk_date_bo.isTxnDate()){
					businessDate = (String) dateData.get("THISDATE");
					pre_businessDate = (String) dateData.get("THISDATE");
				}else{//非營業日
					businessDate = (String) dateData.get("PREVDATE");
					pre_businessDate = (String) dateData.get("PREVDATE");
				}
			
				businessDate = DateTimeUtils.convertDate(1, businessDate, "yyyyMMdd", "yyyyMMdd"); 
				pre_businessDate = DateTimeUtils.convertDate(1, pre_businessDate, "yyyyMMdd", "yyyyMMdd"); 
				//前一階段的清算階段
				pre_clrphase = (clrphase.equals("01")? "02" : "01");
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		data.put("bizdate", DateTimeUtils.convertDate(DateTimeUtils.NOT_INTERCONVERSION, businessDate, "yyyyMMdd", "yyyy-MM-dd"));
		data.put("clearingphase", clrphase);
		data.put("pre_bizdate", DateTimeUtils.convertDate(DateTimeUtils.NOT_INTERCONVERSION, pre_businessDate, "yyyyMMdd", "yyyy-MM-dd"));
		data.put("pre_clearingphase", pre_clrphase);
		data.put("sysdate", zDateHandler.getODDate());
		
		return JSONUtils.map2json(data);
	}
	
//	發佈結算通知
	public String doCLNotifySole(Map<String, String> param){
		String json = "";
		String bizdate =StrUtils.isNotEmpty(param.get("BIZDATE"))? param.get("BIZDATE"):"";
		String clearingphase =StrUtils.isNotEmpty(param.get("CLEARINGPHASE"))? param.get("CLEARINGPHASE"):"";
		String batch_proc_seq =StrUtils.isNotEmpty(param.get("BATCH_PROC_SEQ"))? param.get("BATCH_PROC_SEQ"):"";
		String bgbkId =StrUtils.isNotEmpty(param.get("BGBK_ID"))? param.get("BGBK_ID"):"";
		String action =StrUtils.isNotEmpty(param.get("action"))? param.get("action"):"";
		Map<String,String> retmap = new HashMap<String,String>();
		Map<String,String> logmap = new HashMap<String,String>();
		retmap.put("result", "FALSE");
		List<EACH_BATCH_STATUS> list = null ;
		try {
			bizdate = DateTimeUtils.convertDate(1, bizdate, "yyyy-MM-dd", "yyyyMMdd");
			list = batch_status_Dao.checkStatus_By_Seq(bizdate, clearingphase , Integer.valueOf(batch_proc_seq) );
			list = list ==null? new LinkedList<EACH_BATCH_STATUS>():list;
			if(list.size() ==0){
				retmap.put("msg", "無法執行，該項批次作業未完成");
			}else{
				EACH_BATCH_NOTIFY_PK id = new EACH_BATCH_NOTIFY_PK(bizdate, clearingphase);
				EACH_BATCH_NOTIFY po = batch_notify_Dao.get(id);
				if(po == null){
					po = new EACH_BATCH_NOTIFY();
					po.setId(id);
				}
//				最後由普鴻回寫Y或N
				po.setCLEAR_NOTIFY("P");
//				batch_notify_Dao.save(po);
				batch_notify_Dao.aop_save(po);
//				發送電文通知給GATEWAY，再轉發給各家銀行。
				retmap =  sendSole(bizdate, clearingphase, bgbkId);
				if(!retmap.get("result").equals("TRUE")){
					json = JSONUtils.map2json(retmap);
					return json;
				}
				retmap.put("result", "TRUE");
				retmap.put("msg", "結算通知執行中");
				logmap.put("BIZDATE", bizdate);
				logmap.put("CLEARINGPHASE", clearingphase);
				logmap.put("action", action);
//				userlog_bo.writeLog("P", null, logmap, null);
				userlog_bo.genericLog("P", "成功" ,action , logmap, null);
//				只要送出通知給getway無論結果均發佈通告，格式:yyy-mm-dd 第x清算階段結算通知已發佈
				BULLETIN bulletin = new BULLETIN();
				bizdate = DateTimeUtils.convertDate(1, bizdate, "yyyyMMdd", "yyy-MM-dd");
				bulletin.setCHCON(bizdate+ " ， 第"+clearingphase+"清算階段結算通知已發佈 ");
				bulletin.setSAVE_DATE(zDateHandler.getTimestamp());
				bulletin.setSEND_DATE(zDateHandler.getTimestamp());
				bulletin.setSEND_STATUS("Y");
				bulletin_bo.saveNsend_BAT(bulletin,action);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.debug("doCLNotify.Exception>>"+e);
			retmap.put("msg", "發出通知失敗，系統異常");
			logmap.clear();
			logmap.put("BIZDATE", bizdate);
			logmap.put("CLEARINGPHASE", clearingphase);
			logmap.put("action", action);
			userlog_bo.genericLog("P", "失敗，發出通知失敗，系統異常:"+e, action, logmap, null);
		}
		
		json = JSONUtils.map2json(retmap);
		
		return json;
	}
	
	public Map<String,String> sendSole(String bizdate ,String clearingphase,String bgbkId){
		boolean res = false ;
		String telegram = "";
		Map<String ,String> retmap = new HashMap<String ,String>();
//		<?xml version="1.0" encoding="UTF-8" standalone="yes"?> 
//		<msg> 
//		    <header> 
//		        <SystemHeader>eACH01</SystemHeader> 
//		        <MsgType>0100</MsgType> 
//		        <PrsCode>52XX</PrsCode> 
//		        <Stan>XXXXXXX</Stan> 
//		        <InBank>XXXXXXX</InBank> 
//		        <OutBank>9990000</OutBank> 
//		        <DateTime>YYYYMMDDHHMMSS</DateTime> 
//		        <RspCode>0000</RspCode> 
//		    </header> 
//		</msg>
		try {
		Header msgHeader = new Header();
		msgHeader.setSystemHeader("eACH01");
		msgHeader.setMsgType("0100");
		msgHeader.setPrsCode("52XX");//52XX 
		msgHeader.setStan(ap_pause_Dao.getStan());
		msgHeader.setInBank(bgbkId);
		msgHeader.setOutBank("9990000");	//20150109 FANNY說 票交發動的電文，「OUTBANK」必須固定為「9990000」
		msgHeader.setDateTime(zDateHandler.getDateNum()+zDateHandler.getTheTime().replaceAll(":", ""));
		msgHeader.setRspCode("0000");
		System.out.println("msgHeader.getStan>>"+msgHeader.getStan());
		Body msgBody = new Body();
		System.out.println("bizdate>>"+bizdate+",clearingphase>>"+clearingphase);
		msgBody.setBizDate(bizdate);
		msgBody.setClearingPhase(clearingphase);
		Message msg = new Message();
		msg.setHeader(msgHeader);
		msg.setBody(msgBody);
		telegram = MessageConverter.marshalling(msg);
		retmap = socketClient.send(telegram);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			retmap.put("result", "FALSE");
			retmap.put("msg", "發送通知失敗，系統異常");
		}
		
		return retmap;
	}
	
	
	public MSG_NOTICE_Dao getMsg_notice_Dao() {
		return msg_notice_Dao;
	}

	public void setMsg_notice_Dao(MSG_NOTICE_Dao msg_notice_Dao) {
		this.msg_notice_Dao = msg_notice_Dao;
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

	public EACH_BATCH_STATUS_Dao getBatch_status_Dao() {
		return batch_status_Dao;
	}

	public void setBatch_status_Dao(EACH_BATCH_STATUS_Dao batch_status_Dao) {
		this.batch_status_Dao = batch_status_Dao;
	}

	public EACH_BATCH_NOTIFY_Dao getBatch_notify_Dao() {
		return batch_notify_Dao;
	}

	public void setBatch_notify_Dao(EACH_BATCH_NOTIFY_Dao batch_notify_Dao) {
		this.batch_notify_Dao = batch_notify_Dao;
	}

	public BULLETIN_BO getBulletin_bo() {
		return bulletin_bo;
	}

	public void setBulletin_bo(BULLETIN_BO bulletin_bo) {
		this.bulletin_bo = bulletin_bo;
	}

	public AP_PAUSE_Dao getAp_pause_Dao() {
		return ap_pause_Dao;
	}

	public void setAp_pause_Dao(AP_PAUSE_Dao ap_pause_Dao) {
		this.ap_pause_Dao = ap_pause_Dao;
	}

	public EACHSYSSTATUSTAB_Dao getEachsysstatustab_Dao() {
		return eachsysstatustab_Dao;
	}

	public void setEachsysstatustab_Dao(EACHSYSSTATUSTAB_Dao eachsysstatustab_Dao) {
		this.eachsysstatustab_Dao = eachsysstatustab_Dao;
	}

	public WK_DATE_BO getWk_date_bo() {
		return wk_date_bo;
	}

	public void setWk_date_bo(WK_DATE_BO wk_date_bo) {
		this.wk_date_bo = wk_date_bo;
	}

	public EACHSYSSTATUSTAB_BO getEachsysstatustab_bo() {
		return eachsysstatustab_bo;
	}

	public void setEachsysstatustab_bo(EACHSYSSTATUSTAB_BO eachsysstatustab_bo) {
		this.eachsysstatustab_bo = eachsysstatustab_bo;
	}
	
}
