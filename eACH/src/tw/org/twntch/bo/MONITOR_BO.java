package tw.org.twntch.bo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tw.org.twntch.bean.BANK_STATUS;
import tw.org.twntch.db.dao.hibernate.BANKAPSTATUSTAB_Dao;
import tw.org.twntch.db.dao.hibernate.BANKSYSSTATUSTAB_Dao;
import tw.org.twntch.db.dao.hibernate.EACHAPSTATUSTAB_Dao;
import tw.org.twntch.db.dao.hibernate.EACHSYSSTATUSTAB_Dao;
import tw.org.twntch.db.dao.hibernate.MONITOR_Dao;
import tw.org.twntch.po.EACHAPSTATUSTAB;
import tw.org.twntch.po.EACHSYSSTATUSTAB;
import tw.org.twntch.po.WK_DATE_CALENDAR;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.SpringAppCtxHelper;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.zDateHandler;
import tw.org.twntch.util.DateTimeUtils;

public class MONITOR_BO {
	private MONITOR_Dao monitor_Dao;
	private BANKAPSTATUSTAB_Dao bankapstatus_Dao;
	private BANKSYSSTATUSTAB_Dao banksysstatus_Dao;
	private EACHSYSSTATUSTAB_Dao eachsysstatustab_Dao;
	private EACHAPSTATUSTAB_Dao eachapstatustab_Dao;
	private EACH_USERLOG_BO userlog_bo;//寫操作軌跡記錄
	private ONBLOCKTAB_BO onblocktab_bo;
	
	public String getPendingData(Map<String, String> params){
		String json = "{}";
		String bizdate = "";
		String clearingphase = "";
		bizdate = StrUtils.isNotEmpty(params.get("BIZDATE"))?params.get("BIZDATE"):"";
		clearingphase = StrUtils.isNotEmpty(params.get("CLEARINGPHASE"))?params.get("CLEARINGPHASE"):"";
		List<Map> list = onblocktab_bo.groupOnpending(bizdate, clearingphase);
		if(list != null && list.size() !=0){
			json = JSONUtils.toJson(list);
		}
		return json;
	}
	
	public String isOnPending(Map<String, String> params){
		String json = "{}";
		String bizdate = "";
		String clearingphase = "";
		Map<String, String> rtnMap = null;
		try {
			List<EACHSYSSTATUSTAB> list = eachsysstatustab_Dao.getBusinessDate();
			rtnMap = new HashMap<String, String>();
			if(list != null){
				for(EACHSYSSTATUSTAB po : list){
					bizdate = po.getBUSINESS_DATE();
					clearingphase = po.getCLEARINGPHASE();
				}
			}
//			TODO 測試用 
//			bizdate = "20151022";
//			clearingphase= clearingphase.equals("01")?"02":"01";
			rtnMap.put("bizdate", bizdate);
			rtnMap.put("clearingphase", clearingphase);
			Map<String, Integer> map = onblocktab_bo.cntOnpending(bizdate, clearingphase);
			if(map!=null && map.get("NUM") !=null && map.get("NUM") >0 ){
				rtnMap.put("result", "TRUE");
				rtnMap.put("msg", "「整批處理交易結果發生未完成結果，eACH可能有系統問題」");
			}else{
				rtnMap.put("result", "FALSE");
				rtnMap.put("msg", "目前整批尚未出現未完成交易");
			}
			json = JSONUtils.map2json(rtnMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rtnMap.put("result", "TRUE");
			rtnMap.put("msg", "Ajax:查詢整批未完成資料時出現異常:"+e);
			json = JSONUtils.map2json(rtnMap);
			return json;
		}
		
		return json;
	}
	
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
	
	/**
	 * 取得交換所應用系統狀態
	 * @return
	 */
	public String getEachApStatus(){
		String apstatus = "";
		List<EACHAPSTATUSTAB> list = eachapstatustab_Dao.getEachApStatus();
		if(list != null && list.size() > 0){
			apstatus = list.get(0).getAPSTATUS();
		}
		return apstatus;
	}
	
	public String getEachStatus(Map<String, String> params){
		String result = "";
		Map<String, String> rtnMap = null;
		try{
			rtnMap = new HashMap<String, String>();
			String str = "";
			str = getEachApStatus();
			switch(str){
				case "1":
					rtnMap.put("EACHAPSTATUS", str + "-啟動");
					break;
				case "9":
					rtnMap.put("EACHAPSTATUS", str + "-暫停");
					break;
				default:
					rtnMap.put("EACHAPSTATUS", str + "-未定義");
					break;
			}
			str = getEachSysStatus();
			switch(str){
				case "1":
					rtnMap.put("EACHSYSSTATUS", str + "-開機");
					break;
				case "9":
					rtnMap.put("EACHSYSSTATUS", str + "-關機");
					break;
				default:
					rtnMap.put("EACHSYSSTATUS", str + "-未定義");
					break;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		if(rtnMap != null){
			result = JSONUtils.map2json(rtnMap);
		}
		return result;
	}
	
	public String getData(Map<String, String> params){
		String result = "";
		List list = null;
		try{
			list = monitor_Dao.getData();
		}catch(Exception e){
			e.printStackTrace();
		}
		list = list == null? null : list.size() == 0? null : list;
		if(list != null){
			result = JSONUtils.toJson(list);
		}
		return result;
	}
	
	public List<BANK_STATUS> getData(String bgbkId, String apId){
		List<BANK_STATUS> list = null;
		try{
			list = monitor_Dao.getData(bgbkId, apId);
		}catch(Exception e){
			e.printStackTrace();
		}
		list = list == null? null : list.size() == 0? null : list;
		return list;
	}
	
	public String getSYSSTATUSTAB(Map<String, String> param){
		String result = "";
		List <EACHSYSSTATUSTAB> list = null;
		try{
		    list = eachsysstatustab_Dao.getSYSSTATUSTAB();
		}catch(Exception e){
			e.printStackTrace();
		}
		list = list == null? null : list.size() == 0? null : list;
		if(list != null){
			result = JSONUtils.toJson(list);
		}
		return result;
	}
	
	public String getStatusPanel(Map<String, String> params){
		//操作軌跡記錄用
		String SERCHSTRS = "";
		String paramName = "serchStrs";
		String paramValue = params.get(paramName);
		String path = "";
		
		if (StrUtils.isNotEmpty(paramValue) ){
			SERCHSTRS = paramValue;
		}
		
		if (StrUtils.isNotEmpty(params.get("PATH")) ){
			path = params.get("PATH");
		}
		
		//將頁面上的查詢條件放進pkMap
		Map<String,Object> pkMap = new HashMap<String,Object>();
		pkMap.put("serchStrs",SERCHSTRS);
		//如果有錯誤要將訊息放進去
		Map<String,Object> msgMap = new HashMap<String,Object>();
		
		String result = "";
		List list = null;
		try{
			list = monitor_Dao.getStatusPanel();
		}catch(Exception e){
			e.printStackTrace();
		}
		list = list == null? null : list.size() == 0? null : list;
		
	
		//查詢成功
		if(list != null){
			result = JSONUtils.toJson(list);
			//寫操作軌跡記錄(成功)
			if(path.equals("init")){
			    userlog_bo.writeLog("C",null,null,pkMap);	
			}
		}
		//失敗
		else{
			//寫操作軌跡記錄(失敗)
			msgMap.put("msg","查詢失敗");
			userlog_bo.writeFailLog("C",msgMap,null,null,pkMap);
		}
	
		return result;
	}
	
	public String getPanelDetail(Map<String, String> params){
		String result = "";
		List list = null;
		try{
			list = monitor_Dao.getPanelDetail(params.get("BGBK_ID"), params.get("CHANNEL_ID"));
		}catch(Exception e){
			e.printStackTrace();
		}
		list = list == null? null : list.size() == 0? null : list;
		if(list != null){
			result = JSONUtils.toJson(list);
		}
		return result;
	}
	
	//檢核系統狀態檔資料是否有誤
	public String checksysStatus(Map<String, String> param){
		String res = "";
		
		//取得當前日期時間(民國)
		String today = zDateHandler.getTWDate();
		
		WK_DATE_BO wk_date_bo = SpringAppCtxHelper.getBean("wk_date_bo");
		
		//目前時間
		String nowTime = zDateHandler.getTheTime();
		
		String time_1200 = "12:00:00";
		String time_0000 = "00:00:00";
		String time_1530 = "15:30:00";
		
		//判斷落在營業日區間(range1:0000~1200 、 range2:1200~1530 、 range3:>1530)
		boolean range1 = zDateHandler.isInTimeRange(nowTime, time_0000, time_1200);
		boolean range2 = zDateHandler.isInTimeRange(nowTime, time_1200, time_1530);
		int range3 = zDateHandler.compareDiffTime(nowTime, time_1530);
		
		List<EACHSYSSTATUSTAB> list = eachsysstatustab_Dao.getSYSSTATUSTAB();
		List<WK_DATE_CALENDAR> list2 = monitor_Dao.getNextTxnDateList(today);
		
		//取得EACHSYSSTATUSTAB DATEMODE
		String dateMode = list.get(0).getDATEMODE();
		System.out.println("dateMode ="+dateMode);
		
		//取得EACHSYSSTATUSTAB CLEARINGPHASE
		String clearingPhase = list.get(0).getCLEARINGPHASE();
		System.out.println("clearingPhase ="+clearingPhase);
		
		//取得EACHSYSSTATUSTAB 本營業日(THISDATE)
		String thisDate = list.get(0).getTHISDATE();
		System.out.println("thisDate ="+thisDate);
		
		//取得EACHSYSSTATUSTAB 次營業日(NEXTDATE)
		String sysNextDate = list.get(0).getNEXTDATE();
		System.out.println("sysNextDate ="+sysNextDate);
		
		//西元年轉民國年，以跟 WK_DATE_CALENDAR 比較
		String thisDate_trans = DateTimeUtils.convertDate(thisDate,"yyyyMMdd","yyyyMMdd");
		System.out.println("thisDate_trans ="+thisDate_trans);
		
		String sysNextDate_trans = DateTimeUtils.convertDate(sysNextDate,"yyyyMMdd","yyyyMMdd");
		System.out.println("sysNextDate_trans ="+sysNextDate_trans);
		
		//從WK_DATE_CALENDAR 取得正確次營業日
		String nextDate = list2.get(0).getTXN_DATE();
		System.out.println("nextDate ="+nextDate);
		
		//假日時從WK_DATE_CALENDAR 取得正確次營業日(假日時次營業日等於次次營業日)
		String holiday_nextDate = list2.get(1).getTXN_DATE();
		System.out.println("holiday_nextDate ="+holiday_nextDate);
		
		//檢查當天是否為營業日
		boolean txnDate = wk_date_bo.isTxnDate(); 
		if(txnDate == true){
			//檢查時間落在哪個區間再比對Datemode、清算階段
			if(range1 == true){
				if(!dateMode.equals("0") || !clearingPhase.equals("01")){
					res="EACH系統狀態檔資料有誤";
				}
			}else if(range2 == true){
				if(!dateMode.equals("0") || !clearingPhase.equals("02")){
					res="EACH系統狀態檔資料有誤";
				}
			}else if(range3 == 1 || range3 == 0){
				if(!dateMode.equals("1") || !clearingPhase.equals("01")){
					res="EACH系統狀態檔資料有誤";
				}
				//檢查次營業日是否正確
			}else if(!sysNextDate_trans.equals(nextDate)){
				res="EACH系統狀態檔資料有誤(次營業日有誤)";
			}
			
			//非營業日
		}else{
			//非營業日檢查本營業日(假日等於次營業日)
			if(!thisDate_trans.equals(nextDate)){
//				res="EACH系統狀態檔資料有誤或該日為非營業日(颱風天或本營業日有誤)";//20160812 加上'颱風天或'
				res="EACH系統狀態檔資料有誤。若本日因天然災害(颱風天)，可忽略本則訊息。";//20160930
			}
			//非營業日檢查次營業日(假日等於次次營業日)
			else if(!sysNextDate_trans.equals(holiday_nextDate)){
				res="EACH系統狀態檔資料有誤或該日為非營業日(次營業日有誤)";
			}
			//檢查Datemode、清算階段
			else if(!dateMode.equals("0") || !clearingPhase.equals("01")){
				res="EACH系統狀態檔資料有誤或該日為非營業日";
			}
		}
		
		return res;
	}
	public MONITOR_Dao getMonitor_Dao() {
		return monitor_Dao;
	}

	public void setMonitor_Dao(MONITOR_Dao monitor_Dao) {
		this.monitor_Dao = monitor_Dao;
	}

	public BANKAPSTATUSTAB_Dao getBankapstatus_Dao() {
		return bankapstatus_Dao;
	}

	public void setBankapstatus_Dao(BANKAPSTATUSTAB_Dao bankapstatus_Dao) {
		this.bankapstatus_Dao = bankapstatus_Dao;
	}

	public BANKSYSSTATUSTAB_Dao getBanksysstatus_Dao() {
		return banksysstatus_Dao;
	}

	public void setBanksysstatus_Dao(BANKSYSSTATUSTAB_Dao banksysstatus_Dao) {
		this.banksysstatus_Dao = banksysstatus_Dao;
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

	public ONBLOCKTAB_BO getOnblocktab_bo() {
		return onblocktab_bo;
	}

	public void setOnblocktab_bo(ONBLOCKTAB_BO onblocktab_bo) {
		this.onblocktab_bo = onblocktab_bo;
	}

}
