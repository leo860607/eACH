package tw.org.twntch.bo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import tw.org.twntch.db.dao.hibernate.EACHSYSSTATUSTAB_Dao;
import tw.org.twntch.po.EACHSYSSTATUSTAB;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.zDateHandler;

public class EACHSYSSTATUSTAB_BO {
	private EACHSYSSTATUSTAB_Dao eachsysstatustab_Dao;
	private WK_DATE_BO wk_date_bo;
	
	public String checkBizDate(Map<String, String> params){
		String activeDate = StrUtils.isNotEmpty(params.get("activeDate")) ?params.get("activeDate") : "";
		String bizDate = "";
		String json = "{}";
		Map<String,String> retmap = new HashMap<String,String>();
		if(StrUtils.isEmpty(activeDate)){
			retmap.put("result", "FALSE");
			retmap.put("msg", "activeDate 不可為空值");
			json = JSONUtils.map2json(retmap);
			return json;
		}
		bizDate = getBusinessDate();
		activeDate = DateTimeUtils.convertDate(DateTimeUtils.INTERCONVERSION, activeDate, "yyyyMMdd", "yyyyMMdd");
		if(checkBizDate(activeDate)){
			retmap.put("result", "TRUE");
			retmap.put("msg", "activeDate:"+activeDate+"小於"+bizDate+"");
		}else{
			retmap.put("result", "FALSE");
			retmap.put("msg", "activeDate:"+activeDate+"大於"+bizDate+"");
		}
		json = JSONUtils.map2json(retmap);
		
		return json;
	}
	
	/**
	 * 輸入日期與營業日做比較
	 * @param activeDate 20150101 , 
	 * @param compareWay > , <  , = , >= , <=
	 * @return true or false
	 */
	public String checkBizDate2(Map<String, String> params){
		String activeDate = StrUtils.isNotEmpty(params.get("activeDate")) ?params.get("activeDate") : "";
		String compareWay = StrUtils.isNotEmpty(params.get("compareWay")) ?params.get("compareWay") : "";
		System.out.println("activeDate : "+activeDate);
		System.out.println("compareWay : "+compareWay);
		String bizDate = "";
		String json = "{}";
		Map<String,String> retmap = new HashMap<String,String>();
		if(StrUtils.isEmpty(activeDate)){
			retmap.put("result", "FALSE");
			retmap.put("msg", "activeDate 不可為空值");
			json = JSONUtils.map2json(retmap);
			return json;
		}
		bizDate = getBusinessDate();
//		activeDate = DateTimeUtils.convertDate(DateTimeUtils.INTERCONVERSION, activeDate, "yyyyMMdd", "yyyyMMdd");
		System.out.println("bizDate : "+ bizDate );
		System.out.println("activeDate convertDate : "+ activeDate );
		int result = zDateHandler.compareDiffDate( activeDate, bizDate , "yyyyMMdd");
		switch (compareWay) {
		case ">":
			if(result==1) {
				retmap.put("result", "TRUE");
				retmap.put("msg", "activeDate:"+activeDate+"大於"+bizDate+"");
			}else{
				retmap.put("result", "FALSE");
				retmap.put("msg", "activeDate:"+activeDate+"小於等於"+bizDate+"");
			}
			break;
		case "=":
			if(result==0) {
				retmap.put("result", "TRUE");
				retmap.put("msg", "activeDate:"+activeDate+"等於"+bizDate+"");
			}else{
				retmap.put("result", "FALSE");
				retmap.put("msg", "activeDate:"+activeDate+"不等於"+bizDate+"");
			}
			break;
		case "<":
			if(result==0) {
				retmap.put("result", "TRUE");
				retmap.put("msg", "activeDate:"+activeDate+"小於"+bizDate+"");
			}else{
				retmap.put("result", "FALSE");
				retmap.put("msg", "activeDate:"+activeDate+"大於等於"+bizDate+"");
			}
			break;
		case ">=":
			if(result==0||result==1) {
				retmap.put("result", "TRUE");
				retmap.put("msg", "activeDate:"+activeDate+"大於等於"+bizDate+"");
			}else{
				retmap.put("result", "FALSE");
				retmap.put("msg", "activeDate:"+activeDate+"小於"+bizDate+"");
			}
			break;
		case "<=":
			if(result==0||result==-1) {
				retmap.put("result", "TRUE");
				retmap.put("msg", "activeDate:"+activeDate+"小於等於"+bizDate+"");
			}else{
				retmap.put("result", "FALSE");
				retmap.put("msg", "activeDate:"+activeDate+"大於"+bizDate+"");
			}
			break;
		}
		json = JSONUtils.map2json(retmap);
		
		return json;
	}
	
	
	/**
	 * 輸入日期與前一營業日做比較
	 * @param activeDate 20150101 , 
	 * @param compareWay > , <  , = , >= , <=
	 * @return true or false
	 */
	public String checkBizDate3(Map<String, String> params){
		String activeDate = StrUtils.isNotEmpty(params.get("activeDate")) ?params.get("activeDate") : "";
		String compareWay = StrUtils.isNotEmpty(params.get("compareWay")) ?params.get("compareWay") : "";
		System.out.println("activeDate : "+activeDate);
		System.out.println("compareWay : "+compareWay);
		String bizDate = "";
		String json = "{}";
		Map<String,String> retmap = new HashMap<String,String>();
		if(StrUtils.isEmpty(activeDate)){
			retmap.put("result", "FALSE");
			retmap.put("msg", "activeDate 不可為空值");
			json = JSONUtils.map2json(retmap);
			return json;
		}
		bizDate = getThisBusinessDate();
//		activeDate = DateTimeUtils.convertDate(DateTimeUtils.INTERCONVERSION, activeDate, "yyyyMMdd", "yyyyMMdd");
		System.out.println("bizDate : "+ bizDate );
		System.out.println("activeDate convertDate : "+ activeDate );
		int result = zDateHandler.compareDiffDate( activeDate, bizDate , "yyyyMMdd");
		switch (compareWay) {
		case ">":
			if(result==1) {
				retmap.put("result", "TRUE");
				retmap.put("msg", "activeDate:"+activeDate+"大於"+bizDate+"");
			}else{
				retmap.put("result", "FALSE");
				retmap.put("msg", "activeDate:"+activeDate+"小於等於"+bizDate+"");
			}
			break;
		case "=":
			if(result==0) {
				retmap.put("result", "TRUE");
				retmap.put("msg", "activeDate:"+activeDate+"等於"+bizDate+"");
			}else{
				retmap.put("result", "FALSE");
				retmap.put("msg", "activeDate:"+activeDate+"不等於"+bizDate+"");
			}
			break;
		case "<":
			if(result==0) {
				retmap.put("result", "TRUE");
				retmap.put("msg", "activeDate:"+activeDate+"小於"+bizDate+"");
			}else{
				retmap.put("result", "FALSE");
				retmap.put("msg", "activeDate:"+activeDate+"大於等於"+bizDate+"");
			}
			break;
		case ">=":
			if(result==0||result==1) {
				retmap.put("result", "TRUE");
				retmap.put("msg", "activeDate:"+activeDate+"大於等於"+bizDate+"");
			}else{
				retmap.put("result", "FALSE");
				retmap.put("msg", "activeDate:"+activeDate+"小於"+bizDate+"");
			}
			break;
		case "<=":
			if(result==0||result==-1) {
				retmap.put("result", "TRUE");
				retmap.put("msg", "activeDate:"+activeDate+"小於等於"+bizDate+"");
			}else{
				retmap.put("result", "FALSE");
				retmap.put("msg", "activeDate:"+activeDate+"大於"+bizDate+"");
			}
			break;
		}
		json = JSONUtils.map2json(retmap);
		
		return json;
	}
	
	/**
	 * 檢查日期是否小於營業日
	 * @param activeDate 20150101
	 * @return 小於或等於>>true 大於 false 
	 */
	public boolean checkBizDate(String activeDate){
		List<EACHSYSSTATUSTAB> list = null;
		String businessDate = "";
		boolean result = false ; 
		int tmp  = -1;
		System.out.println("activeDate>>"+activeDate);
		try{
			list = wk_date_bo.getEachsysstatustab_Dao().getBusinessDate();
			if(list != null && list.size() > 0){
				businessDate = list.get(0).getBUSINESS_DATE();
				tmp = zDateHandler.compareDiffDate( activeDate, businessDate , "yyyyMMdd");
			}
			if(tmp <= 0){
				result = true ; 
			}
		}catch(Exception e){
			e.printStackTrace();
			result = false ; 
		}
		System.out.println("result>>"+result);
		return result;
		
	}
	
	
	
	/**
	 * 依照DATEMODE取得目前營業日
	 * @return
	 */
	public String getBusinessDate(){
		List<EACHSYSSTATUSTAB> list = null;
		String businessDate = "";
		try{
			list = eachsysstatustab_Dao.getBusinessDate();
			if(list != null && list.size() > 0){
				businessDate = list.get(0).getBUSINESS_DATE();
				businessDate = DateTimeUtils.convertDate(businessDate, "yyyyMMdd", "yyyyMMdd");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return businessDate;
	}

	/**
	 * 依照DATEMODE取得目前營業日
	 * @return
	 */
	public String getBusinessDateII(){
		List<EACHSYSSTATUSTAB> list = null;
		String businessDate = "";
		try{
			list = eachsysstatustab_Dao.getBusinessDate();
			if(list != null && list.size() > 0){
				businessDate = list.get(0).getBUSINESS_DATE();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return businessDate;
	}
	/**
	 * 取得報表所需的預設營業日期
	 * @return
	 */
	public String getRptBusinessDate(){
		List<EACHSYSSTATUSTAB> list = null;
		String businessDate = "";
		boolean isTxnDate = false;
		try{
			isTxnDate = wk_date_bo.isTxnDate();
			list = eachsysstatustab_Dao.getRptBusinessDate(isTxnDate);
			if(list != null && list.size() > 0){
				businessDate = list.get(0).getBUSINESS_DATE();
				businessDate = DateTimeUtils.convertDate(businessDate, "yyyyMMdd", "yyyyMMdd");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return businessDate;
	}
	
	/**
	 * 執意取得次營業日
	 * @param params
	 * @return
	 */
	public String getNextBusinessDate(){
		List<EACHSYSSTATUSTAB> list = null;
		String businessDate = "";
		try{
			list = eachsysstatustab_Dao.getNextBusinessDate();
			if(list != null && list.size() > 0){
				businessDate = list.get(0).getNEXTDATE();
				businessDate = DateTimeUtils.convertDate(businessDate, "yyyyMMdd", "yyyyMMdd");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return businessDate;
	}
	
	/**
	 * 執意取得THISDATE
	 * @param params
	 * @return
	 */
	public String getThisBusinessDate(){
		List<EACHSYSSTATUSTAB> list = null;
		String businessDate = "";
		try{
			list = eachsysstatustab_Dao.getThisBusinessDate();
			if(list != null && list.size() > 0){
				businessDate = list.get(0).getTHISDATE();
				businessDate = DateTimeUtils.convertDate(businessDate, "yyyyMMdd", "yyyyMMdd");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return businessDate;
	}
	
	/**
	 * 執意取得現在營業日(THIS_DATE)
	 * @param params
	 * @return
	 */
	public String getThisBusinessDate(int mode){
		List<EACHSYSSTATUSTAB> list = null;
		String businessDate = "";
		try{
			list = eachsysstatustab_Dao.getNextBusinessDate();
			if(list != null && list.size() > 0){
				businessDate = list.get(0).getTHISDATE();
				businessDate = DateTimeUtils.convertDate(mode, businessDate, "yyyyMMdd", "yyyyMMdd");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return businessDate;
	}
	
	/**
	 * 執意取得目前清算階段
	 * @param params
	 * @return
	 */
	public String getClearingphase(){
		List<EACHSYSSTATUSTAB> list = null;
		String clrPhase = "";
		try{
			list = eachsysstatustab_Dao.getNextBusinessDate();
			if(list != null && list.size() > 0){
				clrPhase = list.get(0).getCLEARINGPHASE();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return clrPhase;
	}
	
	public Map getRowData(){
		List<EACHSYSSTATUSTAB> list = null;
		Map data = new HashMap();
		try{
			list = eachsysstatustab_Dao.getNextBusinessDate();
			if(list != null && list.size() > 0){
				data = BeanUtils.describe(list.get(0));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return data;
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
	
	
}
