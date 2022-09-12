package tw.org.twntch.bo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.struts.util.LabelValueBean;

import tw.org.twntch.db.dao.hibernate.BANK_GROUP_Dao;
import tw.org.twntch.db.dao.hibernate.MISSINGTRADERLOGTAB_Dao;
import tw.org.twntch.po.BANK_GROUP;
import tw.org.twntch.po.MISSINGTRADERLOGTAB;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.StrUtils;

public class UNKNOWN_MSG_BO {
	private MISSINGTRADERLOGTAB_Dao missingtraderlogtab_Dao;
	private BANK_GROUP_Dao bank_group_Dao;
	private EACH_USERLOG_BO userlog_bo;//寫操作軌跡記錄
	
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
	
	//以「STAN-交易追蹤序號」前三碼辨別是否為交換所發動(999)
	public String getDataByStan(Map<String, String> params){
		String result = "";
		String paramName;
		String paramValue;
		
		String SENDER_TYPE = "";
		paramName = "SENDER_TYPE";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			SENDER_TYPE = paramValue;
		}
		
		String SENDERBANK = "";
		paramName = "SENDERBANK";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			SENDERBANK = paramValue;
		}

		String RECEIVER_TYPE = "";
		paramName = "RECEIVER_TYPE";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			RECEIVER_TYPE = paramValue;
		}
		
		String RECEIVERBANK = "";
		paramName = "RECEIVERBANK";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			RECEIVERBANK = paramValue;
		}
		
		String OPBK_ID = "";
		paramName = "OPBK_ID";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			OPBK_ID = paramValue;
		}
		//操作軌跡記錄用
		String SERCHSTRS = "";
		paramName = "serchStrs";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue) ){
			SERCHSTRS = paramValue;
		}
		//將頁面上的查詢條件放進pkMap
		Map<String,Object> pkMap = new HashMap<String,Object>();
		pkMap.put("serchStrs",SERCHSTRS);
		//如果有錯誤要將訊息放進去
		Map<String,Object> msgMap = new HashMap<String,Object>();
		
		List<MISSINGTRADERLOGTAB> list = null;
		try{
			String condition = "";
			if(SENDER_TYPE.equalsIgnoreCase("TCH")){
				condition = " SENDERBANK LIKE '999%' ";
			}else if(SENDER_TYPE.equalsIgnoreCase("BANK")){
				if(StrUtils.isNotEmpty(SENDERBANK)){
					condition = " SENDERBANK LIKE '" + SENDERBANK + "%' ";
				}
			}
			
			if(RECEIVER_TYPE.equalsIgnoreCase("TCH")){
				condition = " RECEIVERBANK LIKE '999%' ";
			}else if(RECEIVER_TYPE.equalsIgnoreCase("BANK")){
				if(StrUtils.isNotEmpty(RECEIVERBANK)){
					condition = " RECEIVERBANK LIKE '" + RECEIVERBANK + "%' ";
				}
			}
			
			list = missingtraderlogtab_Dao.getByStan(condition);
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
		//操作軌跡記錄用
		String SERCHSTRS = "";
		paramName = "serchStrs";
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
		
		List<MISSINGTRADERLOGTAB> list = null;
		try{
			list = missingtraderlogtab_Dao.getByTxdate(TXDATE);
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
	
	public String getDataByStanAndDate(Map<String, String> params){
		String result = "";
		String paramName;
		String paramValue;
		
		String SENDER_TYPE = "";
		paramName = "SENDER_TYPE";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			SENDER_TYPE = paramValue;
		}
		
		String SENDERBANK = "";
		paramName = "SENDERBANK";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			SENDERBANK = paramValue;
		}

		String RECEIVER_TYPE = "";
		paramName = "RECEIVER_TYPE";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			RECEIVER_TYPE = paramValue;
		}
		
		String RECEIVERBANK = "";
		paramName = "RECEIVERBANK";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			RECEIVERBANK = paramValue;
		}
		
		String OPBK_ID = "";
		paramName = "OPBK_ID";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			OPBK_ID = paramValue;
		}

		String TXDATE = "";
		paramName = "TXDATE";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			TXDATE = paramValue;
		}
		//操作軌跡記錄用
		String SERCHSTRS = "";
		paramName = "serchStrs";
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
		//System.out.println(TXDATE);
		
		List<MISSINGTRADERLOGTAB> list = null;
		try{
			List<String> conditions = new ArrayList<String>();
			if(SENDER_TYPE.equalsIgnoreCase("TCH")){
				conditions.add(" SENDERBANK LIKE '999%' ");
			}else if(SENDER_TYPE.equalsIgnoreCase("BANK")){
				if(StrUtils.isNotEmpty(SENDERBANK)){
					conditions.add(" SENDERBANK LIKE '" + SENDERBANK + "%' ");
				}
			}
			
			if(RECEIVER_TYPE.equalsIgnoreCase("TCH")){
				conditions.add(" RECEIVERBANK LIKE '999%' ");
			}else if(RECEIVER_TYPE.equalsIgnoreCase("BANK")){
				if(StrUtils.isNotEmpty(RECEIVERBANK)){
					conditions.add(" RECEIVERBANK LIKE '" + RECEIVERBANK + "%' ");
				}
			}
			
			if(StrUtils.isEmpty(SENDER_TYPE) && StrUtils.isEmpty(RECEIVER_TYPE)){
				conditions.add(" (SENDERBANK LIKE '" + SENDERBANK + "%' OR RECEIVERBANK LIKE '" + RECEIVERBANK + "%') ");
			}
			
			if(StrUtils.isNotEmpty(OPBK_ID)){
				conditions.add(" SUBSTR('" + OPBK_ID + "', 1, 3) = SUBSTR(M.STAN, 1, 3) ");
			}
			
			String condition = combine(conditions);
			
			list = missingtraderlogtab_Dao.getByStanAndTxdate(condition, TXDATE);
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
	
	public String getDataAll(Map<String, String> params){
		String result = "";
		String paramName;
		String paramValue;
		
		//操作軌跡記錄用
		String SERCHSTRS = "";
		paramName = "serchStrs";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue) ){
			SERCHSTRS = paramValue;
		}
		//將頁面上的查詢條件放進pkMap
		Map<String,Object> pkMap = new HashMap<String,Object>();
		pkMap.put("serchStrs",SERCHSTRS);
		//如果有錯誤要將訊息放進去
		Map<String,Object> msgMap = new HashMap<String,Object>();
		
		List<MISSINGTRADERLOGTAB> list = null;
		try{
			list = missingtraderlogtab_Dao.getAllData();
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
	
	public String combine(List<String> conditions){
		String conStr = "";
		for(int i = 0 ; i < conditions.size(); i++){
			conStr += conditions.get(i);
			if(i < conditions.size() - 1){
				conStr += " AND ";
			}
		}
		return conStr;
	}

	public MISSINGTRADERLOGTAB_Dao getMissingtraderlogtab_Dao() {
		return missingtraderlogtab_Dao;
	}

	public void setMissingtraderlogtab_Dao(
			MISSINGTRADERLOGTAB_Dao missingtraderlogtab_Dao) {
		this.missingtraderlogtab_Dao = missingtraderlogtab_Dao;
	}

	public BANK_GROUP_Dao getBank_group_Dao() {
		return bank_group_Dao;
	}

	public void setBank_group_Dao(BANK_GROUP_Dao bank_group_Dao) {
		this.bank_group_Dao = bank_group_Dao;
	}

	public EACH_USERLOG_BO getUserlog_bo() {
		return userlog_bo;
	}

	public void setUserlog_bo(EACH_USERLOG_BO userlog_bo) {
		this.userlog_bo = userlog_bo;
	}	
}
