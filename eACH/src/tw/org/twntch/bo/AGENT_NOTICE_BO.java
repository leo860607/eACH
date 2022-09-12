package tw.org.twntch.bo;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.struts.util.LabelValueBean;

import tw.org.twntch.db.dao.hibernate.AGENT_NOTICE_Dao;
import tw.org.twntch.db.dao.hibernate.BANK_GROUP_Dao;
import tw.org.twntch.db.dao.hibernate.MSG_NOTICE_Dao;
import tw.org.twntch.po.BANK_GROUP;
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

public class AGENT_NOTICE_BO {
	private AGENT_NOTICE_Dao agent_notice_Dao;
	private AGENT_SEND_PROFILE_BO agent_send_profile_bo;
	private SocketClient socketClient;
	private EACH_USERLOG_BO userlog_bo;//寫操作軌跡記錄
	
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
			webNo = agent_notice_Dao.getStan();
			//用STAN找出操作行及訊息內容重送
			for(int i = 0; i < idAry.length; i++){
				map = new HashMap();
				pk = new OPCTRANSACTIONLOGTAB_PK(idAry[i].split("@")[0],idAry[i].split("@")[1]);
				po = agent_notice_Dao.get(pk);
				if(po != null){
					map.put("ABBR_ID", po.getBANKID());
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
		
		String ABBR_ID = "";
		paramName = "ABBR_ID";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			ABBR_ID = paramValue;
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
			msgHeader.setStan(agent_notice_Dao.getStan());
			msgHeader.setInBank(ABBR_ID);
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
			//msg_notice_Dao.insertTestData(ABBR_ID, msgHeader.getStan(), MESSAGE);
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
		
		String ABBR_ID = "";
		paramName = "ABBR_ID";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			ABBR_ID = paramValue;
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
			msgHeader.setInBank(ABBR_ID);
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
			//msg_notice_Dao.insertTestData(ABBR_ID, msgHeader.getStan(), MESSAGE);
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
			list = agent_notice_Dao.getByStan(STAN);
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
		if (StrUtils.isNotEmpty(paramValue) && !paramValue.equalsIgnoreCase("0000000")){
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
			list = agent_notice_Dao.getByTxdate(TXDATE, ABBRID , RSPCODE);
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
	
	public AGENT_NOTICE_Dao getAgent_notice_Dao() {
		return agent_notice_Dao;
	}

	public void setAgent_notice_Dao(AGENT_NOTICE_Dao agent_notice_Dao) {
		this.agent_notice_Dao = agent_notice_Dao;
	}

	public AGENT_SEND_PROFILE_BO getAgent_send_profile_bo() {
		return agent_send_profile_bo;
	}

	public void setAgent_send_profile_bo(AGENT_SEND_PROFILE_BO agent_send_profile_bo) {
		this.agent_send_profile_bo = agent_send_profile_bo;
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
