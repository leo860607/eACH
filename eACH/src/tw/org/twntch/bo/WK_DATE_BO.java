package tw.org.twntch.bo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import tw.org.twntch.db.dao.hibernate.EACHSYSSTATUSTAB_Dao;
import tw.org.twntch.db.dao.hibernate.WK_DATE_Dao;
import tw.org.twntch.po.EACHSYSSTATUSTAB;
import tw.org.twntch.po.WK_DATE_CALENDAR;
import tw.org.twntch.socket.Message;
import tw.org.twntch.socket.MessageConverter;
import tw.org.twntch.socket.SocketClient;
import tw.org.twntch.socket.Message.Body;
import tw.org.twntch.socket.Message.Header;
import tw.org.twntch.util.BeanUtils;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.zDateHandler;

public class WK_DATE_BO {
	private WK_DATE_Dao wk_date_Dao;
	private EACHSYSSTATUSTAB_Dao eachsysstatustab_Dao;
	private SocketClient socketClient;
	private EACH_USERLOG_BO userlog_bo;
	private Logger logger = Logger.getLogger(WK_DATE_BO.class.getName());
	
	/**
	 * 根據目前營業日期往後取下一個營業日期
	 * @param west_startDate
	 * @param dateType
	 * @return
	 */
	public String getNextBizdateByThisDate(String west_startDate , int dateType){
		String nextBizdate = "";
		try {
			nextBizdate = wk_date_Dao.gotoBusinessDate(west_startDate, 1);
			nextBizdate = DateTimeUtils.convertDate(dateType, nextBizdate, "yyyyMMdd", "yyyyMMdd");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return nextBizdate;
	}
	
	/**
	 * 直接查詢是否為交易日
	 * 
	 * @return
	 */
	public boolean isTxnDate(){
		boolean ret = false;
//		TODO 之後可能還要加入颱風天的判斷
		String today = zDateHandler.getTWDate();
		WK_DATE_CALENDAR po = wk_date_Dao.get(today);
		if(po!=null && StrUtils.isNotEmpty(po.getIS_TXN_DATE()) && po.getIS_TXN_DATE().equals("Y")){
			System.out.println("IS_TXN_DATE>>"+po.getIS_TXN_DATE());
			ret =true;
		}
		logger.debug("isTxnDate.ret>>"+ret);
		return ret;
	}
	/**
	 * 查詢輸入的日期是否是否為交易日
	 * @param date ex:01040101
	 * @return
	 */
	public boolean isTxnDate(String date){
		boolean ret = false;
		String today = date ;
		WK_DATE_CALENDAR po = wk_date_Dao.get(today);
		if(po!=null && StrUtils.isNotEmpty(po.getIS_TXN_DATE()) && po.getIS_TXN_DATE().equals("Y")){
			System.out.println("TXN_DATE>>"+po.getIS_TXN_DATE());
			ret =true;
		}
		return ret;
		
	}
	
	/**
	 * 輸入日期，取得該日期的第一個營業日
	 * @param date (西元年)("yyyy/MM/dd")
	 * @return (西元年)(01040101)
	 */
	public String getFirst_Bizdate_of_Month(String date){
		String first_bizdate = "";
		String first_date = zDateHandler.getFirst_Date_of_Date(date);
		String last_date = zDateHandler.getLast_Date_of_Date(date);
		System.out.println("getFirst_Bizdate_of_Month..first_date>>"+first_date);
		System.out.println("getFirst_Bizdate_of_Month..last_date>>"+last_date);
		logger.debug("getFirst_Bizdate_of_Month..first_date>>"+first_date);
		logger.debug("getFirst_Bizdate_of_Month..last_date>>"+last_date);
		List<Map> list =  wk_date_Dao.getFirst_Bizdate_of_Month(first_date, last_date);
		if(list != null && list.size() !=0){
			for(Map map:list){
				if(map.get("TXN_DATE") != null){
					first_bizdate = String.valueOf(map.get("TXN_DATE")) ;
				}
			}
		}
		return first_bizdate;
	}
	/**
	 * 輸入日期，取得該日期的最後一個營業日
	 * @param date
	 * @return
	 */
	public String getLast_Bizdate_of_Month(String date){
		String first_bizdate = "";
		String first_date = zDateHandler.getFirst_Date_of_Date(date);
		String last_date = zDateHandler.getLast_Date_of_Date(date);
		System.out.println("getFirst_Bizdate_of_Month..first_date>>"+first_date);
		System.out.println("getFirst_Bizdate_of_Month..last_date>>"+last_date);
		logger.debug("getFirst_Bizdate_of_Month..first_date>>"+first_date);
		logger.debug("getFirst_Bizdate_of_Month..last_date>>"+last_date);
		List<Map> list =  wk_date_Dao.getLast_Bizdate_of_Month(first_date, last_date);
		if(list != null && list.size() !=0){
			for(Map map:list){
				if(map.get("TXN_DATE") != null){
					first_bizdate = String.valueOf(map.get("TXN_DATE")) ;
				}
			}
		}
		return first_bizdate;
	}
	
	/**
	 * 
	 * @param date (西元年)
	 * @return
	 */
	public boolean isFirst_Bizdate_of_Month(String date){
//		first_bizdate :回傳格式 為西元年yyyymmdd
		String first_bizdate = getFirst_Bizdate_of_Month(date);
		if(StrUtils.isNotEmpty(first_bizdate)){
//			first_bizdate = DateTimeUtils.convertDate(first_bizdate, "yyyyMMdd", "yyyy/MM/dd");
			first_bizdate = DateTimeUtils.convertDate(2,first_bizdate, "yyyyMMdd", "yyyy/MM/dd");
			int equal = zDateHandler.compareDiffDate(first_bizdate, date);
			if(equal == 0){
				return true;
			}else{
				return false;
			}
		}else{
			return false ;
		}
		
	}
	
	
	
	public String search_toJson(Map<String, String> params){
		String result = "";
		String paramName;
		String paramValue;
		
		String TW_YEAR = "";
		paramName = "TW_YEAR";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			TW_YEAR = paramValue;
		}
		
		String TW_MONTH = "";
		paramName = "TW_MONTH";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			TW_MONTH = paramValue;
		}
		
		result = JSONUtils.toJson(rePack(search(TW_YEAR, TW_MONTH)));
		System.out.println("json>>" + result);
		return result;
	}
	
	public Map<String,String>  validateYear_Month(String twYear, String twMonth){
		List<WK_DATE_CALENDAR> lists = search(twYear,twMonth );
		Map<String,String>  rtnMap = new HashMap<String,String>();
		rtnMap.put("result", "TRUE");
		rtnMap.put("msg", "");
		if(lists == null || lists.size() == 0){
			rtnMap.put("result", "FALSE");
			rtnMap.put("msg", "輸入年月尚未建立營業日檔，參數值:"+twYear+twMonth);
		}
		return rtnMap;
	}
	
	public List<WK_DATE_CALENDAR> search(String twYear, String twMonth){
		List<WK_DATE_CALENDAR> list = null ;
		try {
			if(StrUtils.isNotEmpty(twYear)){
				if(StrUtils.isNotEmpty(twMonth)){
					list = wk_date_Dao.getByTwYearMonth(twYear, twMonth);
				}else{
					list = wk_date_Dao.getByTwYear(twYear);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("list>>"+list);
		list = list == null? null : list.size() == 0? null : list;
		return list;
	}
	
	public WK_DATE_CALENDAR search(String txnDate){
		WK_DATE_CALENDAR po = null;
		try {
			if(StrUtils.isNotEmpty(txnDate)){
				po = wk_date_Dao.get(txnDate);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return po;
	}
	
	public Map<String, String> update(WK_DATE_CALENDAR po){
		Map rtnMap = new HashMap();
		Map pkmap = new HashMap();
		Map oldmap = new HashMap();
		try{
			WK_DATE_CALENDAR tmp = wk_date_Dao.get(po.getTXN_DATE());
			
			pkmap.put("TXN_DATE", po.getTXN_DATE());
			if(tmp == null){
				rtnMap.put("result", "FALSE");
				rtnMap.put("msg", "該資料不存在，儲存失敗");
				return rtnMap;
			}
			oldmap = BeanUtils.describe(tmp);
			List<EACHSYSSTATUSTAB> eachSys = eachsysstatustab_Dao.getBusinessDate();
			if(eachSys == null || eachSys.size() <= 0){
				rtnMap.put("result", "FALSE");
				rtnMap.put("msg", "系統資料錯誤");
				return rtnMap;
			}
			//不可超過第一清算階段才修改當天的狀態
			String west_tDate = DateTimeUtils.convertDate(po.getTXN_DATE(), "yyyyMMdd", "yyyyMMdd");
			EACHSYSSTATUSTAB sysStatus = eachSys.get(0);
			if(west_tDate.equals(sysStatus.getBUSINESS_DATE()) && sysStatus.getCLEARINGPHASE().equals("02")){
				rtnMap.put("result", "FALSE");
				rtnMap.put("msg", "已無法修改當日資料");
				return rtnMap;
			}
			//先送電文再修改資料狀態
			//僅有在前一天修改才送電文，當天不送
			if(isPreBizdate(sysStatus.getBUSINESS_DATE(), west_tDate)){
				if(send(po).get("result").equals("FALSE")){
					rtnMap.put("result", "FALSE");
					rtnMap.put("msg", "資料同步請求失敗");
					return rtnMap;
				}else{
					//等待普鴻修改營業日日期(EACHSYSSTATUSTAB)
					boolean isDone = checkBusinessDate(sysStatus.getBUSINESS_DATE(), west_tDate, po.getIS_TXN_DATE());
					if(!isDone){
						rtnMap.put("result", "FALSE");
						rtnMap.put("msg", "資料同步失敗");
//						rtnMap = wk_date_Dao.updateFail_PK(tmp, pkmap, "資料同步失敗");
						return rtnMap;
					}else{
//						wk_date_Dao.save(po);
						wk_date_Dao.saveII(po, oldmap, pkmap);
						rtnMap.put("result", "TRUE");
						rtnMap.put("msg", "儲存成功");
					}
				}
			}else{
//				wk_date_Dao.save(po);
				wk_date_Dao.saveII(po, oldmap, pkmap);
				rtnMap.put("result", "TRUE");
				rtnMap.put("msg", "儲存成功");
			}
		}catch(Exception e){
			e.printStackTrace();
			rtnMap.put("result", "FALSE");
			rtnMap.put("msg", "儲存失敗:"+e.toString());
		}
		return rtnMap;
	}
	
	/**
	 * 重新打包回傳的List<WK_DATE_CALENDAR>
	 * 格式為：{"010301":[~ 1月各日期資料 ~],"010301":[~ 2月各日期資料 ~],...}
	 * @return
	 */
	public Map<String, List<WK_DATE_CALENDAR>> rePack(List<WK_DATE_CALENDAR> dateList){
		Map<String, List<WK_DATE_CALENDAR>> resultMap = new LinkedHashMap<String, List<WK_DATE_CALENDAR>>();
		List<WK_DATE_CALENDAR> newDateList = null;
		
		String strYM = "";
		for(int i = 0; i < dateList.size(); i++){
			strYM = dateList.get(i).getTXN_DATE().substring(0,6);
			if(!resultMap.containsKey(strYM)){
				newDateList = new ArrayList<WK_DATE_CALENDAR>();
				newDateList.add(dateList.get(i));
				resultMap.put(strYM, newDateList);
			}else{
				resultMap.get(strYM).add(dateList.get(i));
			}
		}
		
		return resultMap;
	}
	
	public Map<String, String> createWholeYearData(String twYear){
		Map<String, String> rtnMap = null;
		Map<String, String> pkmap = new HashMap<>();
		Map<String, String> logmap = new HashMap<>();
		List<WK_DATE_CALENDAR> list = null;
		try{
			pkmap.put("TXN_DATE", twYear);
			rtnMap = new HashMap<String, String>();
			if(twYear.length() != 4 || !twYear.startsWith("0")){
				rtnMap.put("result", "FALSE");
				rtnMap.put("msg", "年份錯誤，請輸入民國年!");
				return rtnMap;
			}
			list = search(twYear, "");
			if(list != null && list.size() != 0){
				rtnMap.put("result", "FALSE");
				rtnMap.put("msg", "該年份已有資料，不可重複產生!");
				wk_date_Dao.saveFail_PK(null, pkmap, "年份錯誤，請輸入民國年!");
				return rtnMap;
			}
			if(wk_date_Dao.createWholeYearData(twYear)){
				rtnMap.put("result", "TRUE");
				rtnMap.put("msg", "民國" + twYear + "資料已產生!");
				userlog_bo.addLog("A", "成功，民國" + twYear + "資料已產生!", pkmap, pkmap);
			}else{
				rtnMap.put("result", "FALSE");
				rtnMap.put("msg", "資料產生失敗!");
				logmap.putAll(rtnMap);
				userlog_bo.writeFailLog("A", logmap, null, null, pkmap);
			}
		}catch(Exception e){
			e.printStackTrace();
			rtnMap.put("result", "FALSE");
			rtnMap.put("msg", "資料產生時發生錯誤!");
			logmap.putAll(rtnMap);
			userlog_bo.writeFailLog("A", logmap, null, null, pkmap);
		}
		return rtnMap;
	}
	
	public Map<String, String> send(WK_DATE_CALENDAR po){
		Map<String, String> rtnMap = null;
		try{
			/* 產生電文內容
			<header> 
		        <SystemHeader>eACH01</SystemHeader> 
		        <MsgType>0100</MsgType> 
		        <PrsCode>9101</PrsCode> 
		        <Stan>XXXXXXX</Stan> 
		        <InBank>0000000</InBank> 
		        <OutBank>9990000</OutBank> 
		        <DateTime>YYYYMMDDHHMMSS</DateTime> 
		        <RspCode>0000</RspCode> 
		    </header> 
		    <body> 
		         <BizDate></BizDate> 
		         <IsBizDate></IsBizDate> 
		    </body> 
			*/
			Header msgHeader = new Header();
			msgHeader.setSystemHeader("eACH01");
			msgHeader.setMsgType("0100");
			msgHeader.setPrsCode("9101");
			msgHeader.setStan(wk_date_Dao.getStan());
			msgHeader.setInBank("0000000");
			msgHeader.setOutBank("9990000");	//20150109 FANNY說 票交發動的電文，「OUTBANK」必須固定為「9990000」
			msgHeader.setDateTime(zDateHandler.getDateNum()+zDateHandler.getTheTime().replaceAll(":", ""));
			msgHeader.setRspCode("0000");
			Message msg = new Message();
			msg.setHeader(msgHeader);
			Body body = new Body();
			body.setBizDate(DateTimeUtils.convertDate(po.getTXN_DATE(), "yyyyMMdd", "yyyyMMdd"));
			body.setIsBizDate(po.getIS_TXN_DATE());
			msg.setBody(body);
			String telegram = MessageConverter.marshalling(msg);
			rtnMap = socketClient.send(telegram);
			rtnMap.put("STAN", msgHeader.getStan());
		}catch(Exception e){
			e.printStackTrace();
		}
		return rtnMap;
	}
	
	//傳入修改前的營業日，欲修改的日期, 是否為營業日
	public boolean checkBusinessDate(String bDate, String tDate, String isTxnDate){
		//若要將tDate改為非營業日，則檢查傳入bDate的下下個營業日是否等於EACHSYSSTATUSTAB的NEXTDATE
		//若要將tDate改為營業日，則檢查tDate是否等於EACHSYSSTATUSTAB的NEXTDATE
		String compareDate = "";
		if(isTxnDate.equalsIgnoreCase("Y")){
			compareDate = tDate;
		}else if(isTxnDate.equalsIgnoreCase("N")){
			compareDate = wk_date_Dao.gotoBusinessDate(bDate, 2);
		}
		
		int timeoutCount = 5;	//五次
		int sleepInterval = 2 * 1000;	//每次休息2秒
		boolean isChanged = false;
		
		List<EACHSYSSTATUSTAB> list = null;
		while(timeoutCount != 0 && !isChanged){
			list = eachsysstatustab_Dao.getNextBusinessDate();
			if(list != null && list.size() > 0){
				isChanged = compareDate.equals(list.get(0).getNEXTDATE());
				System.out.println("@@@ (" + timeoutCount + ") " + bDate + " is changed to " + compareDate + "? " + isChanged + "( " + list.get(0).getNEXTDATE() + " )");
			}
			
			try {
				if(!isChanged){
					Thread.sleep(sleepInterval);
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			timeoutCount--;
		}
		return isChanged;
	}
	
	//nDate=現在營業日,tDate=欲修改的營業日
	public boolean isPreBizdate(String nDate, String tDate){
		String bDate = wk_date_Dao.gotoBusinessDate(tDate, -1);
		if(nDate.equals(bDate)){
			System.out.println("@@@ " + nDate + " is previous bizdate( " + bDate + " ) of " + tDate);
			return true;
		}else{
			System.out.println("@@@ " + nDate + " is not previous bizdate( " + bDate + " ) of " + tDate);
			return false;
		}
	}

	public WK_DATE_Dao getWk_date_Dao() {
		return wk_date_Dao;
	}

	public void setWk_date_Dao(WK_DATE_Dao wk_date_Dao) {
		this.wk_date_Dao = wk_date_Dao;
	}
	public EACHSYSSTATUSTAB_Dao getEachsysstatustab_Dao() {
		return eachsysstatustab_Dao;
	}
	public void setEachsysstatustab_Dao(EACHSYSSTATUSTAB_Dao eachsysstatustab_Dao) {
		this.eachsysstatustab_Dao = eachsysstatustab_Dao;
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
