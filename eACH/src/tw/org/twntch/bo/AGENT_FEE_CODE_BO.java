package tw.org.twntch.bo;

import tw.org.twntch.db.dao.hibernate.AGENT_FEE_CODE_Dao;
import tw.org.twntch.db.dao.hibernate.FEE_CODE_Dao;
import tw.org.twntch.db.dao.hibernate.TXN_CODE_Dao;
import tw.org.twntch.db.dao.hibernate.TXN_FEE_MAPPING_Dao;
import tw.org.twntch.po.AGENT_FEE_CODE;
import tw.org.twntch.po.AGENT_FEE_CODE_PK;
import tw.org.twntch.po.FEE_CODE;
import tw.org.twntch.po.FEE_CODE_PK;
import tw.org.twntch.po.TXN_CODE;
import tw.org.twntch.po.TXN_FEE_MAPPING;
import tw.org.twntch.util.BeanUtils;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.zDateHandler;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.struts.util.LabelValueBean;

public class AGENT_FEE_CODE_BO {
	private AGENT_FEE_CODE_Dao agent_fee_code_Dao;
	
	private EACHSYSSTATUSTAB_BO eachsysstatustab_bo;
	private FEE_CODE_Dao fee_code_Dao;
	private TXN_CODE_Dao txn_code_Dao;
	private TXN_FEE_MAPPING_Dao txn_fee_mapping_Dao;
	private EACH_USERLOG_BO userlog_bo;
	private AGENT_SEND_PROFILE_BO agent_send_profile_bo ; 
	
	public List<LabelValueBean> getFeeIdList(){
		List<AGENT_FEE_CODE> list = agent_fee_code_Dao.getFeeIdList();
		List<LabelValueBean> beanList = new LinkedList<LabelValueBean>();
		LabelValueBean bean = null;
		if(list != null){
			for(AGENT_FEE_CODE po : list){
				bean = new LabelValueBean(po.getFEE_ID() + "-" + po.getFEE_NAME(), po.getFEE_ID());
				beanList.add(bean);
			}
		}
		return beanList;
	}
	
	public List<LabelValueBean> getIdList(){
//		List<Map<String, String>> list = fee_code_Dao.getDistinct_OrderByFeeId();
		List<TXN_CODE> list = txn_code_Dao.getAll_OrderByTxnId();
		List<LabelValueBean> beanList = new LinkedList<LabelValueBean>();
		LabelValueBean bean = null;
		if(list != null){
			for(TXN_CODE po : list){
//				bean = new LabelValueBean(po.getTXN_ID(), po.getTXN_ID()+"-"+po.getTXN_NAME());
				bean = new LabelValueBean(po.getTXN_ID()+"-"+po.getTXN_NAME() ,  po.getTXN_ID());
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
	
	public List<AGENT_FEE_CODE> search(String feeId, String startDate , String company_id){
		List<AGENT_FEE_CODE> list = null;
		System.out.println("feeId?>"+feeId+",startDate>>"+startDate+",company_id>>"+company_id);
		try{
			list = agent_fee_code_Dao.find(" FROM tw.org.twntch.po.AGENT_FEE_CODE WHERE FEE_ID = ? AND START_DATE = ? AND COMPANY_ID = ? ",  feeId, startDate , company_id);
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	
	public List<AGENT_FEE_CODE> search(String sqlPath, String sqlPath2,String orderSQL  , List<String> values){
		List<AGENT_FEE_CODE> list = null;
		StringBuffer sql = new StringBuffer();
		try{
			sql.append(" WITH TEMP AS ( ");
			sql.append(" SELECT FEE_ID ,COALESCE (COMPANY_ID , '') AS COMPANY_ID , COALESCE (COMPANY_ID , '') ||'-'|| GETCOMPANY_ABBR(COMPANY_ID) AS COMPANY_NAME, START_DATE ,FEE , FEE_DESC , ACTIVE_DESC");
			sql.append(" FROM AGENT_FEE_CODE ");
			sql.append(sqlPath);
			sql.append(" ) ");
			sql.append(" SELECT * FROM TEMP ");
			sql.append(sqlPath2);
			sql.append(orderSQL);
			System.out.println("sql>>"+sql);
			list = agent_fee_code_Dao.getData(sql.toString(), values);
		}catch(Exception e){
			e.printStackTrace();
		}
//		list = list == null? null : list.size() == 0? null : list;
		System.out.println("list>>"+list);
		return list;
	}
	
//	判斷該業者是否已啟用
	public boolean checkIsEx(String company_id, String txn_id){
		List<AGENT_FEE_CODE> list = null;
		List<String> values = new LinkedList<String>();
		StringBuffer sql = new StringBuffer();
		String bizdate = "";
		boolean result = false;
		try{
			bizdate = eachsysstatustab_bo.getBusinessDate();
			sql.append(" SELECT START_DATE ");
			sql.append(" FROM AGENT_FEE_CODE ");
			sql.append(" WHERE "+bizdate+" > ");
			sql.append("  (CASE LENGTH(RTRIM(COALESCE(START_DATE,''))) WHEN 0 THEN '99999999' ELSE START_DATE END) ");
			sql.append(" AND COMPANY_ID = ? AND FEE_ID = ? ");
			System.out.println("sql>>"+sql);
			values.add(company_id);
			values.add(txn_id);
			list = agent_fee_code_Dao.getData(sql.toString(), values);
			System.out.println("list>>"+list);
			if(list != null && list.size() !=0){
				result = true;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
//	判斷該業者手續費檔是否建立
	public boolean checkIsExII(String company_id, String txn_id){
		List<AGENT_FEE_CODE> list = null;
		List<String> values = new LinkedList<String>();
		StringBuffer sql = new StringBuffer();
		boolean result = false;
		try{
			sql.append(" SELECT START_DATE ");
			sql.append(" FROM AGENT_FEE_CODE ");
			sql.append(" WHERE COMPANY_ID = ? AND FEE_ID = ? ");
			System.out.println("sql>>"+sql);
			values.add(company_id);
			values.add(txn_id);
			list = agent_fee_code_Dao.getData(sql.toString(), values);
			System.out.println("list>>"+list);
			if(list != null && list.size() !=0){
				result = true;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	
	public String search_toJson(Map<String, String> params){
		String json = "{}";
		try {
			String conditionKey = "[\"FEE_ID\",\"START_DATE\",\"COMPANY_ID\",\"COMPANY_NAME\"]";
			Map map = getConditionData(params, conditionKey);
			String sord =StrUtils.isNotEmpty(params.get("sord"))? params.get("sord"):"";
			String sidx =StrUtils.isNotEmpty(params.get("sidx"))? params.get("sidx"):"";
			String orderSQL = StrUtils.isNotEmpty(sidx)? " ORDER BY "+sidx+" "+sord:"";
			List<AGENT_FEE_CODE> list = null;
			
			list = search(map.get("sqlPath").toString(),map.get("sqlPath2").toString(), orderSQL , (List<String>) map.get("values"));
			if(list != null && list.size() > 0){
				json = JSONUtils.toJson(list);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("json>>" + json);
		return json;
	}
	
	public Map getConditionData(Map<String,String> params , String conditionKey){
		List<String> keyList = JSONUtils.toList(conditionKey);
		List<String> values = new LinkedList<String>();
		Map retMap = new HashMap();
		StringBuffer sql = new StringBuffer();
		StringBuffer sql2 = new StringBuffer();
		int i = 0 , j=0;
		try {
			for(String key :params.keySet()){
				if(keyList.contains(key)){
					if(StrUtils.isNotEmpty(params.get(key))){
						
						if(key.equals("COMPANY_NAME")){
							if(j==0){sql2.append(" WHERE ");}
							if(j!=0){sql2.append(" AND ");}
							sql2.append(key+" LIKE ?");
						values.add("%" + params.get(key) + "%");
//							values.add("%" + java.net.URLDecoder.decode(params.get(key), "UTF-8") + "%");
							j++;
						}else{
							if(i==0){sql.append(" WHERE ");}
							if(i!=0){sql.append(" AND ");}
							sql.append( key+" = ? ");
							values.add(params.get(key));
							
							i++;
						}
					}
				}
			}
			retMap.put("sqlPath", sql.toString());
			retMap.put("sqlPath2", sql2.toString());
			retMap.put("values", values);
			System.out.println("retMap>>"+retMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retMap;
	}
	
	
	public Map<String, String> save(AGENT_FEE_CODE po){
		Map<String, String> map = null;
		Map<String, String> pkmap = new HashMap<String, String>();
		try {
			map = new HashMap<String, String>();
			AGENT_FEE_CODE tmp = agent_fee_code_Dao.get(po.getId());
			pkmap = BeanUtils.describe(po.getId());
			if(tmp != null){
				map = fee_code_Dao.saveFail(null, pkmap, "儲存失敗，資料重複", 1);
				return map;
			}
			// 啟用日期控管為操作時間所屬營業日的下一個營業日(含)以後
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
				map.put("msg", "啟用日期不可小於或等於營業日:"+eachsysstatustab_bo.getBusinessDate());
				map.put("target", "add_p");
				return map;
			}
			
			
			
			po.setCDATE(zDateHandler.getTheDateII());
//			fee_code_Dao.save(po);
			agent_fee_code_Dao.save(po , pkmap);
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
	
	public Map<String,String> delete(String feeId,String company_id, String startDate  ){
		Map<String, String> map = null;
		Map<String, String> pkmap = new HashMap<String, String>();
		Map<String, String> oldmap = new HashMap<String, String>();
		String activeDate = "";
		try {
			map = new HashMap<String, String>();
			AGENT_FEE_CODE po = agent_fee_code_Dao.get(new AGENT_FEE_CODE_PK(feeId, company_id, startDate ));
			pkmap = BeanUtils.describe(new AGENT_FEE_CODE_PK(feeId , company_id, startDate));
			if(po == null ){
				map.put("result", "FALSE");
				map.put("msg", "刪除失敗，查無資料");
				map.put("target", "edit_p");
//				map = fee_code_Dao.removeFail(null, pkmap, "刪除失敗，查無資料", 1);
				return map;
			}
			
			activeDate= DateTimeUtils.convertDate(DateTimeUtils.INTERCONVERSION, startDate, "yyyyMMdd", "yyyyMMdd");
//			已到啟用日期，代理業者vs發動者關聯檔(AGENT_SEND_PROFILE)未建立時允許刪除
			//657 UAT-20160504-01 註解啟用日判斷，改為只要 "代理業者關聯維護 " 有建立就不可刪除
//			if(eachsysstatustab_bo.checkBizDate(activeDate) ){
				Map<String , Integer> cntmap = agent_send_profile_bo.getAgent_send_profile_Dao().getDataByCom_Id_Txn_Id(feeId,company_id);
				if(cntmap.get("NUM") > 0 ){
					map.put("result", "FALSE");
//					map.put("msg", "刪除失敗，此筆資料已啟用，且代理業者vs發動者關聯檔已建立");
					map.put("msg", "刪除失敗，代理業者vs發動者關聯檔已建立");
					map.put("target", "edit_p");
					return map;
				}
//			}
			agent_fee_code_Dao.removeII(po, pkmap);
			
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
	
	public Map<String,String> update(AGENT_FEE_CODE po){
		Map<String, String> map = null;
		Map<String, String> pkmap = new HashMap<String, String>();
		Map<String, String> oldmap = new HashMap<String, String>();
		try {
			map = new HashMap<String, String>();
			AGENT_FEE_CODE tmp = agent_fee_code_Dao.get(po.getId());
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
	
	public AGENT_FEE_CODE_Dao getAgent_fee_code_Dao() {
		return agent_fee_code_Dao;
	}

	public void setAgent_fee_code_Dao(AGENT_FEE_CODE_Dao agent_fee_code_Dao) {
		this.agent_fee_code_Dao = agent_fee_code_Dao;
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

	public AGENT_SEND_PROFILE_BO getAgent_send_profile_bo() {
		return agent_send_profile_bo;
	}

	public void setAgent_send_profile_bo(AGENT_SEND_PROFILE_BO agent_send_profile_bo) {
		this.agent_send_profile_bo = agent_send_profile_bo;
	}
	
	
}
