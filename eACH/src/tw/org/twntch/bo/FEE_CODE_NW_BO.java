package tw.org.twntch.bo;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.util.LabelValueBean;

import tw.org.twntch.db.dao.hibernate.EACH_USERLOG_Dao;
import tw.org.twntch.db.dao.hibernate.FEE_CODE_Dao;
import tw.org.twntch.db.dao.hibernate.FEE_CODE_NW_Dao;
import tw.org.twntch.db.dao.hibernate.SC_COMPANY_PROFILE_Dao;
import tw.org.twntch.db.dao.hibernate.TXN_CODE_Dao;
import tw.org.twntch.db.dao.hibernate.TXN_FEE_MAPPING_Dao;
import tw.org.twntch.po.FEE_CODE;
import tw.org.twntch.po.FEE_CODE_NW;
import tw.org.twntch.po.FEE_CODE_NWLVL;
import tw.org.twntch.po.FEE_CODE_NW_PK;
import tw.org.twntch.po.TXN_CODE;
import tw.org.twntch.util.CodeUtils;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.zDateHandler;

public class FEE_CODE_NW_BO {
	private SC_COMPANY_PROFILE_Dao sc_com_Dao;
	private EACH_USERLOG_Dao userLog_Dao;
	private BANK_GROUP_BO bank_group_bo;
	private EACH_USERLOG_BO userlog_bo;
	private FEE_CODE_NW_Dao fee_code_nw_Dao;
	private FEE_CODE_Dao fee_code_Dao;
	
	private EACHSYSSTATUSTAB_BO eachsysstatustab_bo;
	private TXN_CODE_Dao txn_code_Dao;
	private TXN_FEE_MAPPING_Dao txn_fee_mapping_Dao;
	

	public List<FEE_CODE_NW> searchBySenderHead(String fee_id, String fee_type) {
		List<FEE_CODE_NW> list = new ArrayList<FEE_CODE_NW>();
		if (StrUtils.isEmpty(fee_id) && StrUtils.isEmpty(fee_type)) {
		} else {
			StringBuffer sql = new StringBuffer();
			List<String> strList = new LinkedList<String>();
			Map<String, String> map = new HashMap<String, String>();
			map.put("FEE_ID", fee_id.trim());
			map.put("FEE_TYPE", fee_type.trim());			
			
			int i = 0;
			for (String key : map.keySet()) {
				if (StrUtils.isNotEmpty(map.get(key))) {
					if (i != 0) {
						sql.append(" AND ");
					}
					if (key.equals("FEE_ID")) {
						sql.append("FEE_ID= ?");
						strList.add(map.get(key));
					} else if (key.equals("FEE_TYPE")) {
						sql.append("FEE_TYPE= ?");
						strList.add(map.get(key));
					} else {
						sql.append(key + " = ?");
						strList.add(map.get(key));
					}
					i++;
				}
			}
//			System.out.println("SC_COMPANY_PROFILE.sql>>" + sql);
//			System.out.println("SC_COMPANY_PROFILE.param>>" + strList);
			list = fee_code_nw_Dao.getdata(sql.toString(), strList);
			// list=sc_com_Dao.find(sql.toString(), strList.toArray() );
			// list=sc_com_Dao.getDataByPK(sql.toString(), strList.toArray());
		}
		System.out.println("fee_code_nw_Dao.list>>" + list);
//		list = list == null ? list : list.size() == 0 ? list : list;
		if(list.size()!=0) {
			for(FEE_CODE_NW po : list ) {
				po.setIN_BANK_FEE(po.getIN_BANK_FEE()+"%");
				po.setOUT_BANK_FEE(po.getOUT_BANK_FEE()+"%");
				po.setSND_BANK_FEE(po.getSND_BANK_FEE() + "%");
				po.setWO_BANK_FEE(po.getWO_BANK_FEE()+"%");
				po.setTCH_FEE(po.getTCH_FEE()+"%");
				po.setHANDLECHARGE(po.getHANDLECHARGE() + "%");
			}
		}	
		
		return list;
	}

	public String search_fee_toJson(Map<String, String> param) {
		String fee_id = StrUtils.isNotEmpty(param.get("TXN_ID")) ? param.get("TXN_ID") : "";
		String fee_type = StrUtils.isNotEmpty(param.get("FEE_TYPE")) ? param.get("FEE_TYPE") : "";
		if (fee_id.equals("all")) {
			fee_id = "";
		}
		String jsonStr = JSONUtils
				.toJson(searchBySenderHead(fee_id, fee_type));
		System.out.println("json" + jsonStr);
		return jsonStr;
	}
		
	public List<LabelValueBean> getIdList_toJson(){
		List<FEE_CODE_NW> list = fee_code_nw_Dao.getAll_OrderByFeeId();
		List<LabelValueBean> beanList = new LinkedList<LabelValueBean>();
		LabelValueBean bean = null;
		if(list != null){
			for(FEE_CODE_NW po : list){
				try {
					Map pkMap = tw.org.twntch.util.BeanUtils.describe(po.getId());
					Map map = tw.org.twntch.util.BeanUtils.describe(po);
					map.put("id", JSONUtils.map2json(pkMap));
					bean = new LabelValueBean(JSONUtils.map2json(map), po.getFEE_ID() + "-" + po.getSTART_DATE());
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
		List<Map<String, String>> list = fee_code_nw_Dao.getDistinct_OrderByFeeId();
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
		List<Map<String, String>> list = fee_code_nw_Dao.getDistinctIdJoinTxnName_OrderByFeeId();
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
	
	
	public List<LabelValueBean> getUnselectedFeeList(String txnId){
		List<FEE_CODE_NW> list = fee_code_nw_Dao.getUnselectedFeeList(txnId);
		List<LabelValueBean> beanList = new LinkedList<LabelValueBean>();
		LabelValueBean bean = null;
		if(list != null){
			for(FEE_CODE_NW po : list){
				try {
					Map pkMap = tw.org.twntch.util.BeanUtils.describe(po.getId());
					Map map = tw.org.twntch.util.BeanUtils.describe(po);
					map.put("id", JSONUtils.map2json(pkMap));
					bean = new LabelValueBean(JSONUtils.map2json(map), po.getFEE_ID() + "-" + po.getSTART_DATE());
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
		List<FEE_CODE_NW> list = fee_code_nw_Dao.getSelectedFeeList(txnId);
		List<LabelValueBean> beanList = new LinkedList<LabelValueBean>();
		LabelValueBean bean = null;
		if(list != null){
			for(FEE_CODE_NW po : list){
				try {
					Map pkMap = tw.org.twntch.util.BeanUtils.describe(po.getId());
					Map map = tw.org.twntch.util.BeanUtils.describe(po);
					map.put("id", JSONUtils.map2json(pkMap));
					bean = new LabelValueBean(JSONUtils.map2json(map), po.getFEE_ID() + "-" + po.getSTART_DATE());
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
	
	public List<FEE_CODE_NW> search(String feeId, String startDate){
		List<FEE_CODE_NW> list = null;
		try{
			if(StrUtils.isEmpty(startDate)){
				if(feeId.equalsIgnoreCase("all")){
					list = fee_code_nw_Dao.getAll_OrderByFeeId();
				}else{
					list = fee_code_nw_Dao.getByFeeId(feeId);
				}
			}else{
				if(StrUtils.isEmpty(feeId) || feeId.equalsIgnoreCase("all")){
					list = fee_code_nw_Dao.getByStartDate(startDate);
				}else{
					list = fee_code_nw_Dao.getByPK(feeId, startDate);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		list = list == null? null : list.size() == 0? null : list;
		return list;
	}
	public List<FEE_CODE_NW> search(String feeId, String startDate , String orderSQL){
		List<FEE_CODE_NW> list = null;
		try{
			if(StrUtils.isEmpty(startDate)){
				if(feeId.equalsIgnoreCase("all")){
					list = fee_code_nw_Dao.getAllData(orderSQL);
				}else{
					list = fee_code_nw_Dao.getByFeeId(feeId);
				}
			}else{
				if(StrUtils.isEmpty(feeId) || feeId.equalsIgnoreCase("all")){
					list = fee_code_nw_Dao.getByStartDate(startDate);
				}else{
					list = fee_code_nw_Dao.getByPK(feeId, startDate);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		list = list == null? null : list.size() == 0? null : list;
		return list;
	}
	
	public List<FEE_CODE_NW> searchII(String feeId, String startDate , String feeType, String orderSQL){
		List<FEE_CODE_NW> list_nw = null;
		List<FEE_CODE> list = null;
		List<FEE_CODE_NW> rs_o = new ArrayList<FEE_CODE_NW>();
		List<FEE_CODE_NW> rs = new ArrayList<FEE_CODE_NW>();
		try{
			if(StrUtils.isEmpty(startDate)){
				if(feeId.equalsIgnoreCase("all")){
					list_nw = fee_code_nw_Dao.getAllData(orderSQL);
					list = fee_code_Dao.getAllData(orderSQL);
				}else{
					list_nw = fee_code_nw_Dao.getByFeeIdII(feeId);
					list = fee_code_Dao.getByFeeId(feeId);
				}
			}else{
				if(StrUtils.isEmpty(feeId) || feeId.equalsIgnoreCase("all")){
					list_nw = fee_code_nw_Dao.getByStartDateII(startDate);
					list = fee_code_Dao.getByStartDate(startDate);
				}else{
					list_nw = fee_code_nw_Dao.getByPKII(feeId, startDate);
					list = fee_code_Dao.getByPK(feeId, startDate);
				}
			}
			if(list_nw !=null && list_nw.size()>0) {
				rs_o.addAll(list_nw);
			}
			
			if(list !=null && list.size()>0) {
				for(FEE_CODE each_old:list) {
					FEE_CODE_NW newdata = CodeUtils.objectCovert(FEE_CODE_NW.class, each_old);
					newdata= convertdata(newdata);
					rs_o.add(newdata);
				}
			}
			
			
			//搜尋條件有帶 Fee type 
			for(FEE_CODE_NW data:rs_o) {
				if(StrUtils.isNotEmpty(feeType)) {
					if(feeType.equals(data.getFEE_TYPE())) {
						rs.add(data);
					}
				}else {
					rs.add(data);
				}
			}
			
			if(rs !=null && rs.size()>0) {
				Collections.sort(rs,new Comparator<FEE_CODE_NW>(){  
			        public int compare(FEE_CODE_NW arg0, FEE_CODE_NW arg1) {
			            return arg0.getFEE_ID().compareTo(arg1.getFEE_ID());  
			        }  
			    });
			}

		}catch(Exception e){
			e.printStackTrace();
		}
//		list = list == null? null : list.size() == 0? null : list;
		return rs;
	}
	
//	public String search_toJson(Map<String, String> params){
//		String jsonStr = null;
//		String paramName;
//		String paramValue;
//		
//		String feeId = "";
//		paramName = "feeId";
//		paramValue = params.get(paramName);
//		if (StrUtils.isNotEmpty(paramValue)){
//			feeId = paramValue;
//		}
//		
//		String startDate = "";
//		paramName = "startDate";
//		paramValue = params.get(paramName);
//		if (StrUtils.isNotEmpty(paramValue)){
//			startDate = paramValue;
//		}
//		String sord =StrUtils.isNotEmpty(params.get("sord"))? params.get("sord"):"";
//		String sidx =StrUtils.isNotEmpty(params.get("sidx"))? params.get("sidx"):"";
//		String orderSQL = StrUtils.isNotEmpty(sidx)? " ORDER BY "+sidx+" "+sord:"";
//		
//		List list = null;
//		list = search(feeId, startDate , orderSQL);
//		
//		if(list != null && list.size() > 0){
//			jsonStr = JSONUtils.toJson(list);
//		}
//		System.out.println("json>>" + jsonStr);
//		return jsonStr;
//	}
	
	
	public String search_toJsonII(Map<String, String> params){
		System.out.println("search_toJsonII START");
		String jsonStr = "{}";
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
		
		String feeType = "";
		paramName = "feeType";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			feeType = paramValue;
		}
		
		System.out.println("feeId>>" + feeId);
		System.out.println("startDate>>" + startDate);
		System.out.println("feeType>>" + feeType);
		
		String sord =StrUtils.isNotEmpty(params.get("sord"))? params.get("sord"):"";
		String sidx =StrUtils.isNotEmpty(params.get("sidx"))? params.get("sidx"):"";
		String orderSQL = StrUtils.isNotEmpty(sidx)? " ORDER BY "+sidx+" "+sord:"";
		System.out.println("search_toJsonII orderSQL >> "+orderSQL);
		
		List list = null;
		list = searchII(feeId, startDate , feeType  ,orderSQL);
		
		if(list != null && list.size() > 0){
			jsonStr = JSONUtils.toJson(list);
		}
		System.out.println("json>>" + jsonStr);
		return jsonStr;
	}
	
//	public String search_toJson_LVL(Map<String, String> params){
//		String jsonStr = null;
//		String paramName;
//		String paramValue;
//		
//		String feeUid = "";
//		paramName = "feeUId";
//		paramValue = params.get(paramName);
//		if (StrUtils.isNotEmpty(paramValue)){
//			feeUid = paramValue;
//		}
//		System.out.println("search_toJson_LVL >> "+ feeUid);
//		List<FEE_CODE_NW> list = null;
//		list = search_LVL(feeUid);
//		for(FEE_CODE_NW data:list) {
//			if("1".equals(data.getFEE_LVL_TYPE())){
//				data.setFEE_LVL_TYPE("固定");
//			}else if ("2".equals(data.getFEE_LVL_TYPE())) {
//				data.setFEE_LVL_TYPE("百分比");
//			}
//		}
//		
//		if(list != null && list.size() > 0){
//			jsonStr = JSONUtils.toJson(list);
//		}
//		System.out.println("json>>" + jsonStr);
//		return jsonStr;
//	}
	
	
	public Map<String, String> save(FEE_CODE_NW po){
		Map<String, String> map = null;
		Map<String, String> pkmap = new HashMap<String, String>();
		try {
			map = new HashMap<String, String>();
			
			pkmap.put("FEE_ID", po.getFEE_ID());
			pkmap.put("START_DATE", po.getSTART_DATE());
			pkmap.put("FEE_TYPE", po.getFEE_TYPE());
			System.out.println("po.getFEE_ID()>>" + po.getFEE_ID());
			System.out.println("po.getSTART_DATE()>>" + po.getSTART_DATE());
			System.out.println("po.getFEE_TYPE()>>" + po.getFEE_TYPE());
			
			boolean result = fee_code_nw_Dao.checkDoubleByUK( po.getFEE_ID() , po.getSTART_DATE() , po.getFEE_TYPE());
			System.out.println("checkDoubleByUK >>" +result);
			//true有重複的FEE_ID,START_DATE,FEE_TYPE擋掉
			if(result){
				switch (po.getFEE_TYPE()) {
				case "A":
					pkmap.put("FEE_TYPE", "固定");
					break;
				case "B":
					pkmap.put("FEE_TYPE", "外加");
					break;
				case "C":
					pkmap.put("FEE_TYPE", "百分比");
					break;
				case "D":
					pkmap.put("FEE_TYPE", "級距");
					break;
				}
				map = fee_code_nw_Dao.saveFail(null, pkmap, "儲存失敗，資料重複", 1);
				return map;
			}

//			20150623 eidt by hugo req by UAT-2015011-01
			//UAT-20150507-04 啟用日期控管為操作時間所屬營業日的下一個營業日(含)以後
//			String nextDate = eachsysstatustab_bo.getNextBusinessDate();
//			String west_startDate = po.getId().getSTART_DATE();
//			if(Integer.valueOf(west_startDate) < Integer.valueOf(nextDate)){
//				map = fee_code_nw_Dao.saveFail(null, pkmap, "儲存失敗，啟用日期不可小於 " + nextDate, 2);
//				return map;
//			}
			
			eachsysstatustab_bo.getBusinessDate();
			String west_startDate = po.getSTART_DATE();
			west_startDate = DateTimeUtils.convertDate(DateTimeUtils.INTERCONVERSION,west_startDate, "yyyyMMdd", "yyyyMMdd");
			if(eachsysstatustab_bo.checkBizDate(west_startDate)){
				map.put("result", "FALSE");
				map.put("msg", "手續費代號的啟用日期不可小於或等於營業日:"+eachsysstatustab_bo.getBusinessDate());
				map.put("target", "add_p");
				return map;
			}
			
			po.setCDATE(zDateHandler.getTheDateII());
//			fee_code_nw_Dao.save(po);
			fee_code_nw_Dao.save(po , pkmap);
			map.put("result", "TRUE");
			map.put("msg", "儲存成功");
			map.put("target", "search");
		}catch (Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();
//			map.put("result", "ERROR");
//			map.put("msg", "儲存失敗，系統異常");
//			map.put("target", "add_p");
			map = fee_code_nw_Dao.saveFail(null, pkmap, "儲存失敗，系統異常" , 2);
			return map;
		}
		return map;
	}
	
	
//	public Map<String, String> saveLVL(Map<String, String> params){
//		FEE_CODE_NW po= null;
//		try {
//			BeanUtils.populate(po,params);
//			
//			
//		} catch (IllegalAccessException e) {
//			e.printStackTrace();
//		} catch (InvocationTargetException e) {
//			e.printStackTrace();
//		}
//		return params;
//		
//	}
	
	public Map<String, String> saveLVLafter1(FEE_CODE_NWLVL polvl ){
		Map<String, String> map = null;
		Map<String, String> pkmap = new HashMap<String, String>();
		try {
			map = new HashMap<String, String>();
			
			pkmap.put("FEE_UID", polvl.getFEE_ID());
			pkmap.put("FEE_DTNO", polvl.getFEE_DTNO());
			
			boolean result = fee_code_nw_Dao.checkLVLDoubleByUK( polvl.getFEE_UID() , polvl.getFEE_DTNO());
			System.out.println("checkLVLDoubleByUK >>" +result);
			//true有重複的FEE_UID,FEE_DTNO,擋掉
			if(result){
				map = fee_code_nw_Dao.saveFail(null, pkmap, "儲存失敗，資料重複", 1);
				return map;
			}

			polvl.setCDATE(zDateHandler.getTheDateII());
//			fee_code_nw_Dao.save(po);
			fee_code_nw_Dao.save(polvl , pkmap);
			map.put("result", "TRUE");
			map.put("msg", "儲存成功");
			map.put("target", "search");
		}catch (Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();
//			map.put("result", "ERROR");
//			map.put("msg", "儲存失敗，系統異常");
//			map.put("target", "add_p");
			map = fee_code_nw_Dao.saveFail(null, pkmap, "儲存失敗，系統異常" , 2);
			return map;
		}
		return map;
	}
	
	public Map<String,String> delete(String feeUId, String feeId , String startDate){
		Map<String, String> map = null;
		Map<String, String> pkmap = new HashMap<String, String>();
		Map<String, String> oldmap = new HashMap<String, String>();
		try {
			map = new HashMap<String, String>();
			FEE_CODE_NW po = fee_code_nw_Dao.get(new FEE_CODE_NW_PK(feeUId));
//			pkmap = BeanUtils.describe(new FEE_CODE_NW_PK(feeUId));
			//202004 BEN更新 將上行註解 因為PK改成UID , 改用塞值紀錄UK
			pkmap.put("FEE_ID",po.getFEE_ID());
			pkmap.put("START_DATE", po.getSTART_DATE());
			if(po == null ){
				map.put("result", "FALSE");
				map.put("msg", "刪除失敗，查無資料");
				map.put("target", "edit_p");
//				map = fee_code_nw_Dao.removeFail(null, pkmap, "刪除失敗，查無資料", 1);
				return map;
			}
			
			//若刪除今日(含)之前啟用的手續費，且僅剩一筆，則不可刪除  
			//20150206 HUANGPU 改成啟用日期小於營業日期，且僅剩一筆則不可刪除
			//20150206 HUANGPU 但若此手續費之交易代號已被刪除，則允許刪除，否則垃圾資料太多
			List<FEE_CODE_NW> feeCodeList = fee_code_nw_Dao.find("FROM tw.org.twntch.po.FEE_CODE_NW WHERE FEE_ID = '" + feeId + "' AND START_DATE <= '" + zDateHandler.getTWDate() + "'");
			List<TXN_CODE> txnCodeList = txn_code_Dao.getByPK(feeId , "");
			if(Integer.valueOf(startDate) <= Integer.valueOf(eachsysstatustab_bo.getBusinessDate()) 
				&& (feeCodeList == null || (feeCodeList != null && feeCodeList.size() <= 1))
				&& txnCodeList != null && txnCodeList.size() > 0){
				map = fee_code_nw_Dao.removeFail(null, pkmap, "刪除失敗，該手續費小於營業日期，且可能已使用，不可刪除", 2);
				return map;
			}
			
			fee_code_nw_Dao.remove(po);
			
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
//			map = fee_code_nw_Dao.removeFail(null, pkmap, "刪除失敗，系統異常", 2);
			return map;
		}
		return map;
	}
	
	public Map<String,String> update(FEE_CODE_NW po){
		Map<String, String> map = null;
		Map<String, String> pkmap = new HashMap<String, String>();
		Map<String, String> oldmap = new HashMap<String, String>();
		try {
			map = new HashMap<String, String>();
			FEE_CODE_NW tmp = fee_code_nw_Dao.get(po.getId());
//			pkmap = BeanUtils.describe(po.getId());
			//202004 BEN更新 將上行註解 因為PK改成UID , 改用塞值紀錄UK
			pkmap.put("FEE_ID",po.getFEE_ID());
			pkmap.put("START_DATE", po.getSTART_DATE());
			if(tmp == null ){
				map.put("result", "FALSE");
				map.put("msg", "儲存失敗，查無資料");
				map.put("target", "edit_p");
				return map;
			}
			
			oldmap = BeanUtils.describe(tmp);
			po.setCDATE(tmp.getCDATE());
			po.setUDATE(zDateHandler.getTheDateII());
			fee_code_nw_Dao.saveII(po, oldmap, pkmap);
			map.put("result", "TRUE");
			map.put("msg", "儲存成功");
			map.put("target", "search");
		}catch (Exception e){
			e.printStackTrace();
			map.put("result", "ERROR");
			map.put("msg", "儲存失敗，系統異常");
			map.put("target", "edit_p");
//			map = fee_code_nw_Dao.updateFail(po, oldmap, pkmap, "儲存失敗，系統異常", 2);
			return map;
		}
		return map;
	}
	
//	public List<FEE_CODE_NW> search_LVL(String feeUid){
//		List<FEE_CODE_NW> list = null;
//		try{
//			list = fee_code_nw_Dao.getByNewPK(feeUid,"D",null);
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		list = list == null? null : list.size() == 0? null : list;
//		return list;
//	}
	
	
	public FEE_CODE_NW findByNPK(String feeUId){
		FEE_CODE_NW po = null;
		List<FEE_CODE_NW> list = null;
		try{
			po = fee_code_nw_Dao.findByNPK(feeUId);
		}catch(Exception e){
			e.printStackTrace();
		}
		return po;
	}
	
	public List<FEE_CODE_NW> getFeeTypeList(String txnid){
		List<FEE_CODE_NW> list = null;
		String sqlPath = ""; 
		Map<String,String> param = new HashMap<String, String>();
		if(StrUtils.isNotEmpty(txnid)){
			sqlPath = " WHERE FEE_ID = :txnid ";
			param.put("txnid", txnid);
		}
		try{
			list = fee_code_nw_Dao.getFeeTypeList(sqlPath , param );
		}catch(Exception e){
			e.printStackTrace();
		}
		if(list != null && list.size() > 0){
			return list;
		}else {
			return new ArrayList<>();
		}
		
	}
	
	public List<FEE_CODE_NW> checkActive(String txnid,String fee_type){
		List<FEE_CODE_NW> list = null;
		String sqlPath = ""; 
		String sqlPath2 = ""; 
		Map<String,String> param = new HashMap<String, String>();
		
		if(StrUtils.isNotEmpty(txnid)){
			sqlPath = " WHERE FEE_ID = :txnid ";
			param.put("txnid", txnid);
		}
		if(StrUtils.isNotEmpty(fee_type)){
			sqlPath2 = " WHERE FEE_TYPE = :fee_type ";
			param.put("fee_type", fee_type);
		}
		
		try{
			list = fee_code_nw_Dao.checkActive(sqlPath ,sqlPath2, param );
		}catch(Exception e){
			e.printStackTrace();
		}
		if(list != null && list.size() > 0){
			return list;
		}else {
			return new ArrayList<>();
		}
		
	}
	
	public FEE_CODE_NW convertdata(FEE_CODE_NW po) {
//		if(Double.parseDouble(po.getHANDLECHARGE())>0) {
//			po.setFEE_TYPE("B");
//			po.setFEE_TYPE_CHT("B.外加(舊)");
//		}else {
//			po.setFEE_TYPE("A");
//			po.setFEE_TYPE_CHT("A.固定(舊)");
//		}
		if(Double.parseDouble(po.getHANDLECHARGE()) != 0) {
			po.setFEE_TYPE("B");
			po.setFEE_TYPE_CHT("外加");
		}else {
			po.setFEE_TYPE("A");
			po.setFEE_TYPE_CHT("固定");
		}
		
		po.setFEE_LVL_TYPE_CHT("-");
		po.setFEE_DTNO("-");
		po.setFEE_LVL_BEG_AMT("-");
		po.setFEE_LVL_END_AMT("-");
		po.setOUT_BANK_FEE(
				StrUtils.isEmpty(po.getOUT_BANK_FEE()) ? "0.00" : po.getOUT_BANK_FEE());
		po.setOUT_BANK_FEE_DISC(StrUtils.isEmpty(po.getOUT_BANK_FEE_DISC()) ? "0.00"
				: po.getOUT_BANK_FEE_DISC());
		po.setIN_BANK_FEE(
				StrUtils.isEmpty(po.getIN_BANK_FEE()) ? "0.00" : po.getIN_BANK_FEE());
		po.setIN_BANK_FEE_DISC(StrUtils.isEmpty(po.getIN_BANK_FEE_DISC()) ? "0.00"
				: po.getIN_BANK_FEE_DISC());
		po.setTCH_FEE(StrUtils.isEmpty(po.getTCH_FEE()) ? "0.00" : po.getTCH_FEE());
		po.setTCH_FEE_DISC(
				StrUtils.isEmpty(po.getTCH_FEE_DISC()) ? "0.00" : po.getTCH_FEE_DISC());
		po.setSND_BANK_FEE(
				StrUtils.isEmpty(po.getSND_BANK_FEE()) ? "0.00" : po.getSND_BANK_FEE());
		po.setSND_BANK_FEE_DISC(StrUtils.isEmpty(po.getSND_BANK_FEE_DISC()) ? "0.00"
				: po.getSND_BANK_FEE_DISC());
		po.setWO_BANK_FEE(
				StrUtils.isEmpty(po.getWO_BANK_FEE()) ? "0.00" : po.getWO_BANK_FEE());
		po.setWO_BANK_FEE_DISC(StrUtils.isEmpty(po.getWO_BANK_FEE_DISC()) ? "0.00"
				: po.getWO_BANK_FEE_DISC());
		po.setHANDLECHARGE(
				StrUtils.isEmpty(po.getHANDLECHARGE()) ? "0.00" : po.getHANDLECHARGE());
		return po;
	}
	

	public SC_COMPANY_PROFILE_Dao getSc_com_Dao() {
		return sc_com_Dao;
	}

	public void setFee_code_nw_Dao(FEE_CODE_NW_Dao fee_code_nw_Dao) {
		this.fee_code_nw_Dao = fee_code_nw_Dao;
	}

	public FEE_CODE_NW_Dao getFee_code_nw_Dao() {
		return fee_code_nw_Dao;
	}

	public void setSc_com_Dao(SC_COMPANY_PROFILE_Dao sc_com_Dao) {
		this.sc_com_Dao = sc_com_Dao;
	}

	
	public EACH_USERLOG_Dao getUserLog_Dao() {
		return userLog_Dao;
	}

	public void setUserLog_Dao(EACH_USERLOG_Dao userLog_Dao) {
		this.userLog_Dao = userLog_Dao;
	}

	public BANK_GROUP_BO getBank_group_bo() {
		return bank_group_bo;
	}

	public void setBank_group_bo(BANK_GROUP_BO bank_group_bo) {
		this.bank_group_bo = bank_group_bo;
	}

	public EACH_USERLOG_BO getUserlog_bo() {
		return userlog_bo;
	}

	public void setUserlog_bo(EACH_USERLOG_BO userlog_bo) {
		this.userlog_bo = userlog_bo;
	}

	public EACHSYSSTATUSTAB_BO getEachsysstatustab_bo() {
		return eachsysstatustab_bo;
	}

	public void setEachsysstatustab_bo(EACHSYSSTATUSTAB_BO eachsysstatustab_bo) {
		this.eachsysstatustab_bo = eachsysstatustab_bo;
	}

	public TXN_CODE_Dao getTxn_code_Dao() {
		return txn_code_Dao;
	}

	public void setTxn_code_Dao(TXN_CODE_Dao txn_code_Dao) {
		this.txn_code_Dao = txn_code_Dao;
	}

	public TXN_FEE_MAPPING_Dao getTxn_fee_mapping_Dao() {
		return txn_fee_mapping_Dao;
	}

	public void setTxn_fee_mapping_Dao(TXN_FEE_MAPPING_Dao txn_fee_mapping_Dao) {
		this.txn_fee_mapping_Dao = txn_fee_mapping_Dao;
	}

	public FEE_CODE_Dao getFee_code_Dao() {
		return fee_code_Dao;
	}

	public void setFee_code_Dao(FEE_CODE_Dao fee_code_Dao) {
		this.fee_code_Dao = fee_code_Dao;
	}
	
}
