package tw.org.twntch.bo;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.struts.util.LabelValueBean;

import tw.org.twntch.db.dao.hibernate.FEE_CODE_Dao;
import tw.org.twntch.db.dao.hibernate.TXN_CODE_Dao;
import tw.org.twntch.db.dao.hibernate.TXN_FEE_MAPPING_Dao;
import tw.org.twntch.po.FEE_CODE;
import tw.org.twntch.po.FEE_CODE_PK;
import tw.org.twntch.po.TXN_CODE;
import tw.org.twntch.util.BeanUtils;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.zDateHandler;

public class FEE_CODE_BO {
	private EACHSYSSTATUSTAB_BO eachsysstatustab_bo;
	private FEE_CODE_Dao fee_code_Dao;
	private TXN_CODE_Dao txn_code_Dao;
	private TXN_FEE_MAPPING_Dao txn_fee_mapping_Dao;
	private EACH_USERLOG_BO userlog_bo;
	
	public List<LabelValueBean> getIdList_toJson(){
		List<FEE_CODE> list = fee_code_Dao.getAll_OrderByFeeId();
		List<LabelValueBean> beanList = new LinkedList<LabelValueBean>();
		LabelValueBean bean = null;
		if(list != null){
			for(FEE_CODE po : list){
				try {
					Map pkMap = tw.org.twntch.util.BeanUtils.describe(po.getId());
					Map map = tw.org.twntch.util.BeanUtils.describe(po);
					map.put("id", JSONUtils.map2json(pkMap));
					bean = new LabelValueBean(JSONUtils.map2json(map), po.getId().getFEE_ID() + "-" + po.getId().getSTART_DATE());
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				beanList.add(bean);
			}
		}
		System.out.println("beanList>>"+beanList);
		return beanList;
	}
	
	public List<LabelValueBean> getIdList(){
		List<Map<String, String>> list = fee_code_Dao.getDistinct_OrderByFeeId();
		List<LabelValueBean> beanList = new LinkedList<LabelValueBean>();
		LabelValueBean bean = null;
		if(list != null){
			for(Map<String, String> po : list){
				bean = new LabelValueBean(po.get("FEE_ID"), po.get("FEE_ID"));
				beanList.add(bean);
			}
		}
		System.out.println("beanList>>"+beanList);
		return beanList;
	}
	
	/**
	 * 用手續費代號JOIN TXN_CODE找到對應的交易項目名稱
	 * @return
	 */
	public List<LabelValueBean> getIdListJoinTxnName(){
		List<Map<String, String>> list = fee_code_Dao.getDistinctIdJoinTxnName_OrderByFeeId();
		List<LabelValueBean> beanList = new LinkedList<LabelValueBean>();
		LabelValueBean bean = null;
		if(list != null){
			String label = "";
			for(Map<String, String> po : list){
				label = StrUtils.isEmpty((String)po.get("TXN_NAME"))?po.get("FEE_ID") : po.get("FEE_ID") + " - " + po.get("TXN_NAME");
				bean = new LabelValueBean(label, po.get("FEE_ID"));
				beanList.add(bean);
			}
		}
		System.out.println("beanList>>"+beanList);
		return beanList;
	}
	
	/*
	public List<LabelValueBean> getIdList(){
		List<FEE_CODE> list = fee_code_Dao.getAll_OrderByFeeId();
		List<LabelValueBean> beanList = new LinkedList<LabelValueBean>();
		LabelValueBean bean = null;
		if(list != null){
			for(FEE_CODE po : list){
				bean = new LabelValueBean(po.getId().getFEE_ID(), po.getId().getFEE_ID());
				beanList.add(bean);
			}
		}
		System.out.println("beanList>>"+beanList);
		return beanList;
	}
	*/
	
	public List<LabelValueBean> getUnselectedFeeList(String txnId){
		List<FEE_CODE> list = fee_code_Dao.getUnselectedFeeList(txnId);
		List<LabelValueBean> beanList = new LinkedList<LabelValueBean>();
		LabelValueBean bean = null;
		if(list != null){
			for(FEE_CODE po : list){
				try {
					Map pkMap = tw.org.twntch.util.BeanUtils.describe(po.getId());
					Map map = tw.org.twntch.util.BeanUtils.describe(po);
					map.put("id", JSONUtils.map2json(pkMap));
					bean = new LabelValueBean(JSONUtils.map2json(map), po.getId().getFEE_ID() + "-" + po.getId().getSTART_DATE());
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				beanList.add(bean);
			}
		}
		System.out.println("beanList>>"+beanList);
		return beanList;
	}
	
	public List<LabelValueBean> getSelectedFeeList(String txnId){
		List<FEE_CODE> list = fee_code_Dao.getSelectedFeeList(txnId);
		List<LabelValueBean> beanList = new LinkedList<LabelValueBean>();
		LabelValueBean bean = null;
		if(list != null){
			for(FEE_CODE po : list){
				try {
					Map pkMap = tw.org.twntch.util.BeanUtils.describe(po.getId());
					Map map = tw.org.twntch.util.BeanUtils.describe(po);
					map.put("id", JSONUtils.map2json(pkMap));
					bean = new LabelValueBean(JSONUtils.map2json(map), po.getId().getFEE_ID() + "-" + po.getId().getSTART_DATE());
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				beanList.add(bean);
			}
		}
		System.out.println("beanList>>"+beanList);
		return beanList;
	}
	
	public List<FEE_CODE> search(String feeId, String startDate){
		List<FEE_CODE> list = null;
		try{
			if(StrUtils.isEmpty(startDate)){
				if(feeId.equalsIgnoreCase("all")){
					list = fee_code_Dao.getAll_OrderByFeeId();
				}else{
					list = fee_code_Dao.getByFeeId(feeId);
				}
			}else{
				if(StrUtils.isEmpty(feeId) || feeId.equalsIgnoreCase("all")){
					list = fee_code_Dao.getByStartDate(startDate);
				}else{
					list = fee_code_Dao.getByPK(feeId, startDate);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		list = list == null? null : list.size() == 0? null : list;
		return list;
	}
	public List<FEE_CODE> search(String feeId, String startDate , String orderSQL){
		List<FEE_CODE> list = null;
		try{
			if(StrUtils.isEmpty(startDate)){
				if(feeId.equalsIgnoreCase("all")){
					list = fee_code_Dao.getAllData(orderSQL);
				}else{
					list = fee_code_Dao.getByFeeId(feeId);
				}
			}else{
				if(StrUtils.isEmpty(feeId) || feeId.equalsIgnoreCase("all")){
					list = fee_code_Dao.getByStartDate(startDate);
				}else{
					list = fee_code_Dao.getByPK(feeId, startDate);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		list = list == null? null : list.size() == 0? null : list;
		return list;
	}
	
	public String search_toJson(Map<String, String> params){
		String jsonStr = null;
		String paramName;
		String paramValue;
		
		String feeId = "";
		paramName = "feeId";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			feeId = paramValue;
		}
		
		String startDate = "";
		paramName = "startDate";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			startDate = paramValue;
		}
		String sord =StrUtils.isNotEmpty(params.get("sord"))? params.get("sord"):"";
		String sidx =StrUtils.isNotEmpty(params.get("sidx"))? params.get("sidx"):"";
		String orderSQL = StrUtils.isNotEmpty(sidx)? " ORDER BY "+sidx+" "+sord:"";
		
		List list = null;
		list = search(feeId, startDate , orderSQL);
		
		if(list != null && list.size() > 0){
			jsonStr = JSONUtils.toJson(list);
		}
		System.out.println("json>>" + jsonStr);
		return jsonStr;
	}
	
	public Map<String, String> save(FEE_CODE po){
		Map<String, String> map = null;
		Map<String, String> pkmap = new HashMap<String, String>();
		try {
			map = new HashMap<String, String>();
			FEE_CODE tmp = fee_code_Dao.get(po.getId());
			pkmap = BeanUtils.describe(po.getId());
			if(tmp != null){
//				map.put("result", "FALSE");
//				map.put("msg", "儲存失敗，id重複");
//				map.put("target", "add_p");
				map = fee_code_Dao.saveFail(null, pkmap, "儲存失敗，資料重複", 1);
				return map;
			}
//			20150623 eidt by hugo req by UAT-2015011-01
			//UAT-20150507-04 啟用日期控管為操作時間所屬營業日的下一個營業日(含)以後
//			String nextDate = eachsysstatustab_bo.getNextBusinessDate();
//			String west_startDate = po.getId().getSTART_DATE();
//			if(Integer.valueOf(west_startDate) < Integer.valueOf(nextDate)){
//				map = fee_code_Dao.saveFail(null, pkmap, "儲存失敗，啟用日期不可小於 " + nextDate, 2);
//				return map;
//			}
			
			eachsysstatustab_bo.getBusinessDate();
			String west_startDate = po.getId().getSTART_DATE();
			west_startDate = DateTimeUtils.convertDate(DateTimeUtils.INTERCONVERSION,west_startDate, "yyyyMMdd", "yyyyMMdd");
			if(eachsysstatustab_bo.checkBizDate(west_startDate)){
				map.put("result", "FALSE");
				map.put("msg", "手續費代號的啟用日期不可小於或等於營業日:"+eachsysstatustab_bo.getBusinessDate());
				map.put("target", "add_p");
				return map;
			}
			
			po.setCDATE(zDateHandler.getTheDateII());
//			fee_code_Dao.save(po);
			fee_code_Dao.save(po , pkmap);
			map.put("result", "TRUE");
			map.put("msg", "儲存成功");
			map.put("target", "search");
		}catch (Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();
//			map.put("result", "ERROR");
//			map.put("msg", "儲存失敗，系統異常");
//			map.put("target", "add_p");
			map = fee_code_Dao.saveFail(null, pkmap, "儲存失敗，系統異常" , 2);
			return map;
		}
		return map;
	}
	
	public Map<String,String> delete(String feeId, String startDate){
		Map<String, String> map = null;
		Map<String, String> pkmap = new HashMap<String, String>();
		Map<String, String> oldmap = new HashMap<String, String>();
		try {
			map = new HashMap<String, String>();
			FEE_CODE po = fee_code_Dao.get(new FEE_CODE_PK(feeId, startDate));
			pkmap = BeanUtils.describe(new FEE_CODE_PK(feeId, startDate));
			if(po == null ){
				map.put("result", "FALSE");
				map.put("msg", "刪除失敗，查無資料");
				map.put("target", "edit_p");
//				map = fee_code_Dao.removeFail(null, pkmap, "刪除失敗，查無資料", 1);
				return map;
			}
			//檢核是否有交易代號使用此手續費 edit by hugo 此邏輯建利說可以移除
//			List<TXN_FEE_MAPPING> list = txn_fee_mapping_Dao.getByFeeId(feeId);
//			if( (list != null && list.size() > 0)){
//				map.put("result", "FALSE");
//				map.put("msg", "刪除失敗，此手續費已被使用");
//				map.put("target", "edit_p");
//				return map;
//			}
			//若刪除今日(含)之前啟用的手續費，且僅剩一筆，則不可刪除  
			//20150206 HUANGPU 改成啟用日期小於營業日期，且僅剩一筆則不可刪除
			//20150206 HUANGPU 但若此手續費之交易代號已被刪除，則允許刪除，否則垃圾資料太多
			List<FEE_CODE> feeCodeList = fee_code_Dao.find("FROM tw.org.twntch.po.FEE_CODE WHERE FEE_ID = '" + feeId + "' AND START_DATE <= '" + zDateHandler.getTWDate() + "'");
			List<TXN_CODE> txnCodeList = txn_code_Dao.getByPK(feeId , "");
			if(Integer.valueOf(startDate) <= Integer.valueOf(eachsysstatustab_bo.getBusinessDate()) 
				&& (feeCodeList == null || (feeCodeList != null && feeCodeList.size() <= 1))
				&& txnCodeList != null && txnCodeList.size() > 0){
//				map.put("result", "FALSE");
////				map.put("msg", "刪除失敗，此手續費已被使用");
//				map.put("msg", "刪除失敗，該手續費小於營業日期，且可能已使用，不可刪除");
//				map.put("target", "edit_p");
				map = fee_code_Dao.removeFail(null, pkmap, "刪除失敗，該手續費小於營業日期，且可能已使用，不可刪除", 2);
				return map;
			}
			
//			fee_code_Dao.remove(po);
//			fee_code_Dao.removeII(po, pkmap);
			fee_code_Dao.remove(po);
			
			map.put("result", "TRUE");
			map.put("msg", "刪除成功");
			map.put("target", "search");
			oldmap = BeanUtils.describe(po);
			oldmap.remove("TXN_NAME");
			oldmap.putAll(BeanUtils.describe(po.getId()));
			userlog_bo.writeLog("D", oldmap, null, pkmap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			map.put("result", "ERROR");
			map.put("msg", "刪除失敗，系統異常");
			map.put("target", "edit_p");
//			map = fee_code_Dao.removeFail(null, pkmap, "刪除失敗，系統異常", 2);
			return map;
		}
		return map;
	}
	
	public Map<String,String> update(FEE_CODE po){
		Map<String, String> map = null;
		Map<String, String> pkmap = new HashMap<String, String>();
		Map<String, String> oldmap = new HashMap<String, String>();
		try {
			map = new HashMap<String, String>();
			FEE_CODE tmp = fee_code_Dao.get(po.getId());
			pkmap = BeanUtils.describe(po.getId());
//			if(po == null ){
			if(tmp == null ){
				map.put("result", "FALSE");
				map.put("msg", "儲存失敗，查無資料");
				map.put("target", "edit_p");
//				map = fee_code_Dao.updateFail(po, oldmap, pkmap, "儲存失敗，查無資料", 1);
				return map;
			}
			oldmap = BeanUtils.describe(tmp);
//			oldmap.putAll(pkmap);
			po.setCDATE(tmp.getCDATE());
			po.setUDATE(zDateHandler.getTheDateII());
//			fee_code_Dao.save(po);
			fee_code_Dao.saveII(po, oldmap, pkmap);
			map.put("result", "TRUE");
			map.put("msg", "儲存成功");
			map.put("target", "search");
		}catch (Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();
			map.put("result", "ERROR");
			map.put("msg", "儲存失敗，系統異常");
			map.put("target", "edit_p");
//			map = fee_code_Dao.updateFail(po, oldmap, pkmap, "儲存失敗，系統異常", 2);
			return map;
		}
		return map;
	}
	
	//判斷此 FEE_ID 在舊版手續費內生效的那筆是屬於固定還是外加  A固定B外加
	public String checkFeeCodeType(String fee_id) {
		String result = fee_code_Dao.checkFeeCodeType(fee_id);
		return result;
	}
	
	public String checkFeeCodeType2(String fee_id) {
		String result = fee_code_Dao.checkFeeCodeType2(fee_id);
		return result;
	}
	
	public EACHSYSSTATUSTAB_BO getEachsysstatustab_bo() {
		return eachsysstatustab_bo;
	}
	public void setEachsysstatustab_bo(EACHSYSSTATUSTAB_BO eachsysstatustab_bo) {
		this.eachsysstatustab_bo = eachsysstatustab_bo;
	}
	public FEE_CODE_Dao getFee_code_Dao() {
		return fee_code_Dao;
	}
	public void setFee_code_Dao(FEE_CODE_Dao fee_code_Dao) {
		this.fee_code_Dao = fee_code_Dao;
	}
	public TXN_FEE_MAPPING_Dao getTxn_fee_mapping_Dao() {
		return txn_fee_mapping_Dao;
	}
	public void setTxn_fee_mapping_Dao(TXN_FEE_MAPPING_Dao txn_fee_mapping_Dao) {
		this.txn_fee_mapping_Dao = txn_fee_mapping_Dao;
	}
	public TXN_CODE_Dao getTxn_code_Dao() {
		return txn_code_Dao;
	}
	public void setTxn_code_Dao(TXN_CODE_Dao txn_code_Dao) {
		this.txn_code_Dao = txn_code_Dao;
	}

	public EACH_USERLOG_BO getUserlog_bo() {
		return userlog_bo;
	}

	public void setUserlog_bo(EACH_USERLOG_BO userlog_bo) {
		this.userlog_bo = userlog_bo;
	}
	
	
}
