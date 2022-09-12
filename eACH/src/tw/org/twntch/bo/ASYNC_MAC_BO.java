package tw.org.twntch.bo;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.struts.util.LabelValueBean;

import tw.org.twntch.db.dao.hibernate.ASYNC_MAC_Dao;
import tw.org.twntch.db.dao.hibernate.BANK_GROUP_Dao;
import tw.org.twntch.db.dao.hibernate.CHG_MAC_Dao;
import tw.org.twntch.form.Login_Form;
import tw.org.twntch.po.BANK_GROUP;
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

public class ASYNC_MAC_BO {
	private ASYNC_MAC_Dao async_mac_Dao;
	private BANK_GROUP_Dao bank_group_Dao;
	private SocketClient socketClient;
	/**
	 * 取得操作行代號清單
	 * select distinct OPBK_ID where Currentdate between 啟用日期 and 停用日期
	 * @return
	 */
	public List<LabelValueBean> getOpbkIdList(){
		List<BANK_GROUP> list = bank_group_Dao.getBgbkIdList_2();
		List<LabelValueBean> beanList = new LinkedList<LabelValueBean>();
		LabelValueBean bean = null;
		for(BANK_GROUP po : list){
			bean = new LabelValueBean(po.getBGBK_ID() + " - " + po.getBGBK_NAME(), po.getBGBK_ID());
			beanList.add(bean);
		}
		//System.out.println("beanList>>"+beanList);
		return beanList;
	}
	
	/**
	 * 送出電文
	 * @return
	 */
	public String single_send(Map<String, String> params){
		String result = "";
		String paramName;
		String paramValue;
		
		String OPBK_ID = "";
		paramName = "OPBK_ID";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			OPBK_ID = paramValue;
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
		
		Map rtnMap = null;
		
		try{
			/* 產生電文內容
			<header>
				<SystemHeader>eACH01</SystemHeader>
				<MsgType>0100</MsgType>
				<PrsCode>1200</PrsCode>
				<Stan>7385830</Stan>
				<InBank>0000000</InBank>
				<OutBank>9500000</OutBank>
				<DateTime>20141028173858</DateTime>
				<RspCode>0000</RspCode>
			</header>
			<body>
				<KeyID>01</KeyID>
			</body>
			*/
			Header msgHeader = new Header();
			msgHeader.setSystemHeader("eACH01");
			msgHeader.setMsgType("0100");
			msgHeader.setPrsCode("1200");
			msgHeader.setStan(async_mac_Dao.getStan());
			msgHeader.setInBank(OPBK_ID);
			msgHeader.setOutBank("");
//			msgHeader.setOutBank("9990000");
			msgHeader.setDateTime(zDateHandler.getDateNum()+zDateHandler.getTheTime().replaceAll(":", ""));
			msgHeader.setRspCode("0000");
			Body msgBody = new Body();
			msgBody.setKeyID("01");
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
		return result;
	}
	
	public String multipleSend(Map<String, String> params ,String webNo){
		String result = "";
		String paramName;
		String paramValue;
		
		String OPBK_ID = "";
		paramName = "OPBK_ID";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			OPBK_ID = paramValue;
		}
		
		Map rtnMap = null;
		
		try{
			/* 產生電文內容
			<header>
				<SystemHeader>eACH01</SystemHeader>
				<MsgType>0100</MsgType>
				<PrsCode>1200</PrsCode>
				<Stan>7385830</Stan>
				<InBank>0000000</InBank>
				<OutBank>9500000</OutBank>
				<DateTime>20141028173858</DateTime>
				<RspCode>0000</RspCode>
			</header>
			<body>
				<KeyID>01</KeyID>
			</body>
			 */
			Header msgHeader = new Header();
			msgHeader.setSystemHeader("eACH01");
			msgHeader.setMsgType("0100");
			msgHeader.setPrsCode("1200");
			msgHeader.setStan(webNo);
			msgHeader.setInBank(OPBK_ID);
			msgHeader.setOutBank("");
			msgHeader.setDateTime(zDateHandler.getDateNum()+zDateHandler.getTheTime().replaceAll(":", ""));
			msgHeader.setRspCode("0000");
			Body msgBody = new Body();
			msgBody.setKeyID("01");
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
		String DATA_LIST = "";
		paramName = "DATA_LIST";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			DATA_LIST = paramValue;
		}

		Map rtnMap = null;
		//DATA_LIST = [BANKID_1,BANKID_2,....]
		if(StrUtils.isNotEmpty(DATA_LIST)){
			String dataAry[] = DATA_LIST.split(",");
			webNo = async_mac_Dao.getStan();
			Map map = new HashMap();
			rtnMap = new HashMap();
			for(int i = 0; i < dataAry.length; i++){
				map.put("OPBK_ID", dataAry[i]);
//				map = JSONUtils.json2map(send(map));
				map = JSONUtils.json2map(multipleSend(map, webNo));
				rtnMap.put(map.get("STAN"), map);
			}
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
			list = async_mac_Dao.getByStan(STAN);
			if(list != null){
				result = JSONUtils.toJson(list);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		//System.out.println("json>>"+result);
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
		
		String OPBKID = "";
		paramName = "OPBKID";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue) && !paramValue.equalsIgnoreCase("0000000")){
			OPBKID = paramValue;
		}
		
		//需民國年轉西元年
		TXDATE = DateTimeUtils.convertDate(TXDATE, "yyyyMMdd", "yyyyMMdd");
		//System.out.println(TXDATE);
		
		List<OPCTRANSACTIONLOGTAB> list = null;
		try{
			list = async_mac_Dao.getByTxdate(TXDATE, OPBKID);
			if(list != null){
				result = JSONUtils.toJson(list);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		//System.out.println("json>>"+result);
		return result;
	}

	public ASYNC_MAC_Dao getAsync_mac_Dao() {
		return async_mac_Dao;
	}

	public void setAsync_mac_Dao(ASYNC_MAC_Dao async_mac_Dao) {
		this.async_mac_Dao = async_mac_Dao;
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
}
