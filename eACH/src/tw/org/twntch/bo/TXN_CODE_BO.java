package tw.org.twntch.bo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.struts.util.LabelValueBean;

import tw.org.twntch.db.dao.hibernate.AGENT_TXN_CODE_Dao;
import tw.org.twntch.db.dao.hibernate.FEE_CODE_Dao;
import tw.org.twntch.db.dao.hibernate.TXN_CODE_Dao;
import tw.org.twntch.db.dao.hibernate.TXN_FEE_MAPPING_Dao;
import tw.org.twntch.po.AGENT_TXN_CODE;
import tw.org.twntch.po.EACHSYSSTATUSTAB;
import tw.org.twntch.po.FEE_CODE;
import tw.org.twntch.po.FEE_CODE_PK;
import tw.org.twntch.po.TXN_CODE;
import tw.org.twntch.po.TXN_FEE_MAPPING;
import tw.org.twntch.po.TXN_FEE_MAPPING_PK;
import tw.org.twntch.util.BeanUtils;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.zDateHandler;

public class TXN_CODE_BO {
	private TXN_CODE_Dao txn_code_Dao ;
	private AGENT_TXN_CODE_Dao agent_txn_code_Dao ;
	private FEE_CODE_Dao fee_code_Dao;
	private TXN_FEE_MAPPING_Dao txn_fee_mapping_Dao;
	private EACH_USERLOG_BO userlog_bo;
	private WK_DATE_BO wk_date_bo;
	private EACHSYSSTATUSTAB_BO eachsysstatustab_bo;
	
	public Map<String,String> update(Map formMap, String selectedFeeAry[]){
		Map<String, String> map = null;
		Map<String, String> txn_pkmap = new HashMap<String, String>();
//		Map<String, String> fee_pkmap = new HashMap<String, String>();
		Map<String, String> txn_oldmap = new HashMap<String, String>();
//		Map<String, String> fee_newmap = new HashMap<String, String>();
		String activeDate ="";
		TXN_CODE po = null;
		try {
			po = new TXN_CODE();
			BeanUtils.populate(po, formMap);
			po.setUDATE(zDateHandler.getTheDateII());
			
			map = new HashMap<String, String>();
			TXN_CODE tmp = txn_code_Dao.get(po.getTXN_ID());
//			fee_pkmap = BeanUtils.describe(feeCodePk);
			txn_pkmap.put("TXN_ID", po.getTXN_ID());
			if(tmp == null ){
//				map.put("result", "FALSE");
//				map.put("msg", "儲存失敗，查無資料");
//				map.put("target", "edit_p");
				map = txn_code_Dao.updateFail(po, null, txn_pkmap, "儲存失敗，查無資料", 1);
				return map;
			}
			txn_oldmap = BeanUtils.describe(tmp);
			
			//找出要新增的清單
			/*
			List<TXN_FEE_MAPPING> tfmList_toInsert = new ArrayList<TXN_FEE_MAPPING>();
			TXN_FEE_MAPPING tfm = null;
			if(selectedFeeAry != null){
				for(int i = 0; i < selectedFeeAry.length; i++){
					tfm = new TXN_FEE_MAPPING();
					tfm.setId(new TXN_FEE_MAPPING_PK(
							po.getTXN_ID(),
							selectedFeeAry[i].split("-")[0],
							selectedFeeAry[i].split("-")[1]));
					tfmList_toInsert.add(tfm);
				}
			}
			*/
			
			//找出要刪除的清單
			//20150121 HUANGPU 不須刪除MAPPING，用來記錄設定歷程
			//List<TXN_FEE_MAPPING> tfmList_toDelete = txn_code_Dao.getFeeIdListByTxnId(po.getTXN_ID());
			List<TXN_FEE_MAPPING> tfmList_toDelete = null;
			
			//依照MAPPING_START_DATE，找出最接近小於等於該日期的手續費代號
			/*
			FEE_CODE feeCode = fee_code_Dao.getLastestByFeeIdAndMappingStartDate((String)formMap.get("feeId"), (String)formMap.get("MAPPING_START_DATE"));
			if(feeCode == null){
				map.put("result", "FALSE");
				map.put("msg", "儲存失敗，無符合該啟用日期的手續費代號");
				map.put("target", "edit_p");
				return map;
			}
			*/
			
			//檢查是否有重複的MAPPING
			/*
			TXN_FEE_MAPPING_PK txnFeeMappingPk = new TXN_FEE_MAPPING_PK(po.getTXN_ID(), feeCode.getId().getFEE_ID(), feeCode.getId().getSTART_DATE());
			tfm = txn_fee_mapping_Dao.get(txnFeeMappingPk);
			if(tfm != null){
				map.put("result", "FALSE");
				map.put("msg", "儲存失敗，已存在的手續費設定");
				map.put("target", "edit_p");
				return map;
			}
			*/
			
			/*
			TXN_FEE_MAPPING txnFeeMapping = new TXN_FEE_MAPPING(txnFeeMappingPk);
			txnFeeMapping.setMAPPING_START_DATE((String)formMap.get("MAPPING_START_DATE"));
			txnFeeMapping.setCDATE(zDateHandler.getTheDateII());
			*/
			
			//txn_code_Dao.updateData(po, tfmList_toDelete, tfmList_toInsert);
			//txn_code_Dao.updateData(po, txnFeeMapping);
//			txn_code_Dao.save(po);
			
			txn_code_Dao.saveII(po, txn_oldmap, txn_pkmap);
//			userlog_bo.writeLog("B", txn_oldmap, newmap, txn_pkmap);
			map.put("result", "TRUE");
			map.put("msg", "儲存成功");
			map.put("target", "search");
		}catch (Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();
//			map.put("result", "ERROR");
//			map.put("msg", "儲存失敗，系統異常:"+e);
//			map.put("target", "edit_p");
			map = txn_code_Dao.updateFail(po, null, txn_pkmap, "儲存失敗，系統異常", 2);
			return map;
		}
		return map;
	}

public Map<String,String> delete(String id){
	Map<String, String> map = null;
	TXN_CODE po = null;
	Map<String, String> pkmap = new HashMap<String, String>();
	Map<String, String> oldmap = new HashMap<String, String>();
	String tmp_msg = "";
	boolean result = false;
	try {
		map = new HashMap<String, String>();
		po = txn_code_Dao.get(id);
		pkmap.put("TXN_ID", id);
		if(po == null ){
//			map.put("result", "FALSE");
//			map.put("msg", "刪除失敗，查無資料");
//			map.put("target", "edit_p");
			map = txn_code_Dao.removeFail(po, pkmap, "刪除失敗，查無資料", 1);
			return map;
		}
		oldmap = BeanUtils.describe(po);
		//找出要刪除的資料
		List<FEE_CODE> feeCodeToDelete = fee_code_Dao.getByFeeId(po.getTXN_ID());
		//List<TXN_FEE_MAPPING> tfmList_toDelete = txn_code_Dao.getFeeIdListByTxnId(po.getTXN_ID());
		feeCodeToDelete = feeCodeToDelete ==null? new LinkedList<FEE_CODE>() :feeCodeToDelete;
		txn_code_Dao.delData(po);
		tmp_msg = "刪除成功，手續費代號( " + id + " )尚有 " + feeCodeToDelete.size() + " 筆手續費設定紀錄未維護";
		map.put("result", "TRUE");
		map.put("msg", tmp_msg);
		map.put("target", "search");
		result = true;
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
//		map.put("result", "ERROR");
//		map.put("msg", "刪除失敗，系統異常:"+e);
//		map.put("target", "edit_p");
		map = txn_code_Dao.removeFail(po, pkmap, "刪除失敗，系統異常", 2);
		result = false;
		return map;
	}finally{
		oldmap.remove("START_DATE");
		oldmap.remove("FEE_ID");
		deleteLog(pkmap, oldmap, tmp_msg);
	}
	return map;
}
	

public void deleteLog(Map pkmap ,Map oldmap ,String tmp_msg){
	try {
		userlog_bo.deleteLog("D", tmp_msg, oldmap, pkmap);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}  

	/**
	 * 新增交易代碼、手續費
	 * @param bgid
	 * @param brid
	 * @param formMap
	 * @return
	 */
	public Map<String,String> save(Map formMap){
		Map<String, String> map = null;
		Map<String, String> txn_pkmap = new HashMap<String, String>();
		Map<String, String> fee_pkmap = new HashMap<String, String>();
		Map<String, String> txn_newmap = new HashMap<String, String>();
		Map<String, String> fee_newmap = new HashMap<String, String>();
		String activeDate = "";
		boolean result =false;
		try {
			System.out.println("formMap = " + formMap);
			TXN_CODE txnCode = new TXN_CODE();
			FEE_CODE feeCode = new FEE_CODE();
			BeanUtils.populate(txnCode, formMap);
			BeanUtils.populate(feeCode, formMap);
			FEE_CODE_PK feeCodePk = new FEE_CODE_PK((String)formMap.get("FEE_ID"),(String)formMap.get("START_DATE"));
			fee_pkmap = BeanUtils.describe(feeCodePk);
			feeCode.setId(feeCodePk);
			txnCode.setCDATE(zDateHandler.getTheDateII());
			feeCode.setCDATE(zDateHandler.getTheDateII());
			
			//檢查是否有重複的交易代號
			map = new HashMap<String, String>();
			TXN_CODE tmp_txnCode = txn_code_Dao.get(txnCode.getTXN_ID());
			txn_pkmap.put("TXN_ID", txnCode.getTXN_ID());
			if(tmp_txnCode != null){
//				map.put("result", "FALSE");
//				map.put("msg", "儲存失敗，交易代號重複");
//				map.put("target", "add_p");
				map = txn_code_Dao.saveFail(tmp_txnCode, txn_pkmap, "儲存失敗，交易代號重複", 1);
				return map;
			}
//			檢查是否有重複的代理業者類別
//			List<TXN_CODE> list = txn_code_Dao.find(" FROM tw.org.twntch.po.TXN_CODE WHERE AGENT_TXN_ID = ? ", txnCode.getAGENT_TXN_ID());
//			if(list.size() != 0  ){
//				map = txn_code_Dao.saveFail(tmp_txnCode, txn_pkmap, "儲存失敗，代理業者交易類別重複", 1);
//				return map;
//			}
			
			//檢查是否有重複的手續費代號及日期
			FEE_CODE tmp_feeCode = fee_code_Dao.get(feeCodePk);
			if(tmp_feeCode != null){
//				map.put("result", "FALSE");
//				map.put("msg", "儲存失敗，手續費啟用日期重複");
//				map.put("target", "add_p");
				map = txn_code_Dao.saveFail(tmp_feeCode, fee_pkmap, "儲存失敗，手續費啟用日期重複", 1);
				return map;
			}
			
			//UAT-20150129-04 新增代收代號（501~999），請檢核「交易類別」僅得選SD
			int txnId = Integer.valueOf(txnCode.getTXN_ID());
			if((txnId > 500 && txnId < 1000) && !txnCode.getTXN_TYPE().equalsIgnoreCase("SD")){
//				map.put("result", "FALSE");
//				map.put("msg", "儲存失敗，交易類別錯誤，代收代號（501~999）僅可為 SD-扣款");
//				map.put("target", "add_p");
				map = txn_code_Dao.saveFail(tmp_txnCode, txn_pkmap, "儲存失敗，交易類別錯誤，代收代號（501~999）僅可為 SD-扣款", 2);
				return map;
			}
			//UAT-20150129-04 新增代付代號（101~499），請檢核「交易類別」僅得選SC
			if((txnId > 0 && txnId < 500) && !txnCode.getTXN_TYPE().equalsIgnoreCase("SC")){
//				map.put("result", "FALSE");
//				map.put("msg", "儲存失敗，交易類別錯誤，代付代號（101~499）僅可為 SC-入帳");
//				map.put("target", "add_p");
				map = txn_code_Dao.saveFail(tmp_feeCode, fee_pkmap, "儲存失敗，交易類別錯誤，代付代號（101~499）僅可為 SC-入帳", 2);
				return map;
			}
//			20150622 add by hugo req by UAT-2015011-01
			eachsysstatustab_bo.getBusinessDate();
			activeDate = txnCode.getACTIVE_DATE();
			activeDate = DateTimeUtils.convertDate(DateTimeUtils.INTERCONVERSION,activeDate, "yyyyMMdd", "yyyyMMdd");
			if(eachsysstatustab_bo.checkBizDate(activeDate)){
				map.put("result", "FALSE");
				map.put("msg", "交易代號的啟用日期不可小於或等於營業日:"+eachsysstatustab_bo.getBusinessDate());
				map.put("target", "add_p");
				return map;
			}
			activeDate = feeCode.getSTART_DATE();
			activeDate = DateTimeUtils.convertDate(DateTimeUtils.INTERCONVERSION,activeDate, "yyyyMMdd", "yyyyMMdd");
			if(eachsysstatustab_bo.checkBizDate(activeDate)){
				map.put("result", "FALSE");
				map.put("msg", "手續費代號的啟用日期不可小於或等於營業日:"+eachsysstatustab_bo.getBusinessDate());
				map.put("target", "add_p");
				return map;
			}
			
			txn_code_Dao.saveData(txnCode, feeCode);
			
			map.put("result", "TRUE");
			map.put("msg", "儲存成功");
			map.put("target", "search");
			result =true;
			txn_newmap = BeanUtils.describe(txnCode);
			fee_newmap = BeanUtils.describe(feeCode);
			fee_newmap.putAll(fee_pkmap);
		}catch (Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();
			map.put("result", "ERROR");
			map.put("msg", "儲存失敗，系統異常");
			map.put("target", "add_p");
			map = txn_code_Dao.saveFail(null, txn_pkmap, "儲存失敗，系統異常", 2);
			return map;
		}finally{
			if(result){
				addLog(txn_pkmap, txn_newmap, fee_pkmap, fee_newmap);
			}
		}
		return map;
	}
	
	public void addLog(Map txn_pkmap ,Map txn_newmap , Map fee_pkmap , Map fee_newmap){
		try {
			System.out.println("ACTIVE_DATE>>"+txn_newmap.get("ACTIVE_DATE"));
			userlog_bo.writeLog("A", null, txn_newmap, txn_pkmap);
			userlog_bo.writeLog("A", null, fee_newmap, fee_pkmap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 檢查啟用日期是否等於或大於營業日
	 * @param activeDate 20150101
	 * @return
	 */
	public boolean checkBizDate(String activeDate){
		List<EACHSYSSTATUSTAB> list = null;
		String businessDate = "";
		boolean result = false ; 
		int tmp  = -1;
		try{
			list = wk_date_bo.getEachsysstatustab_Dao().getBusinessDate();
			if(list != null && list.size() > 0){
				businessDate = list.get(0).getBUSINESS_DATE();
				tmp = zDateHandler.compareDiffDate(activeDate, businessDate);
			}
			if(tmp > 0){
				result = true ; 
			}
		}catch(Exception e){
			e.printStackTrace();
			result = false ; 
		}
		return result;
		
	}
	
	public List<TXN_CODE> search(String id){
		
		List<TXN_CODE> list = null ;
		try {
			if(StrUtils.isEmpty(id)){
				list = txn_code_Dao.getAll_OrderByTxnId();
			}else{
				list= txn_code_Dao.getByPK(id.trim() , "");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("list>>"+list);
		list = list == null? null : list.size() == 0 ? null : list;
		return list;
	}
	
	public List<TXN_CODE> searchByIdAndActiveDate(String id, String ad , String orderSQL){
		List<TXN_CODE> list = null ;
		String condition = "";
		try {
			if(id.equalsIgnoreCase("none") || StrUtils.isEmpty(id)){
				condition += "ACTIVE_DATE = '" + ad + "'";
			}else{
				condition += "TC.TXN_ID = '" + id + "' AND ACTIVE_DATE = '" + ad + "'";
			}
			list= txn_code_Dao.getByIdAndActiveDate(condition , orderSQL);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("list>>"+list);
		list = list == null? null : list.size() == 0 ? null : list;
		return list;
	}
	
	public List<LabelValueBean> getIdList(){
		List<TXN_CODE> list = txn_code_Dao.getAll_OrderByTxnId();
		List<LabelValueBean> beanList = new LinkedList<LabelValueBean>();
		LabelValueBean bean = null;
		for(TXN_CODE po : list){
			bean = new LabelValueBean(po.getTXN_ID() + " - " + po.getTXN_NAME(), po.getTXN_ID());
			beanList.add(bean);
		}
		System.out.println("beanList>>"+beanList);
		return beanList;
	}
	
	/**
	 * 代理業者交易類別清單
	 * @return
	 */
	public List<LabelValueBean> getAgentTxnIdList(){
		List<AGENT_TXN_CODE> list = agent_txn_code_Dao.find(" FROM tw.org.twntch.po.AGENT_TXN_CODE ");
		List<LabelValueBean> beanList = new LinkedList<LabelValueBean>();
		LabelValueBean bean = null;
		if(list !=null && list.size() !=0 ){
			for(AGENT_TXN_CODE po : list){
				bean = new LabelValueBean(po.getAGENT_TXN_ID() + " - " + po.getAGENT_TXN_NAME(), po.getAGENT_TXN_ID());
				beanList.add(bean);
			}
		}else{
			bean = new LabelValueBean("尚未建立任何資料","");
			beanList.add(bean);
		}
		
		System.out.println("beanList>>"+beanList);
		return beanList;
	}
	
	/**
	 * 根據業務類別代號查詢代理業者交易代號
	 * @return
	 */
	public String getAgentTxnIdListByBSid(Map<String, String> params){
		String business_type_id = params.get("BUSINESS_TYPE_ID");
		Map retmap = new HashMap<>();
		String json = "{}";
		
		try {
			if(StrUtils.isNotEmpty(business_type_id)){
				List<AGENT_TXN_CODE> list = agent_txn_code_Dao.find(" FROM tw.org.twntch.po.AGENT_TXN_CODE WHERE BUSINESS_TYPE_ID = ? " , business_type_id);
				List<LabelValueBean> beanList = new LinkedList<LabelValueBean>();
				LabelValueBean bean = null;
				if(list !=null && list.size() !=0 ){
					for(AGENT_TXN_CODE po : list){
						bean = new LabelValueBean(po.getAGENT_TXN_ID() + " - " + po.getAGENT_TXN_NAME(), po.getAGENT_TXN_ID());
						beanList.add(bean);
					}
					retmap.put("result", "TRUE");
					retmap.put("msg", beanList);
				}else{
					retmap.put("result", "FALSE");
					retmap.put("msg", "查無資料");
				}
				System.out.println("beanList>>"+beanList);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			retmap.put("result", "FALSE");
			retmap.put("msg", e.toString());
		}
		json = JSONUtils.toJson(retmap);
		
		System.out.println("json>>"+json);
		return json;
	}
	
	public List<LabelValueBean> getIdListByTxnType(String txnType){
		List<LabelValueBean> beanList = new LinkedList<LabelValueBean>();
		if(StrUtils.isNotEmpty(txnType)){
			List<TXN_CODE> list = txn_code_Dao.getAll_OrderByTxnId();
			LabelValueBean bean = null;
			for(TXN_CODE po : list){
				if(po.getTXN_TYPE().equalsIgnoreCase(txnType)){
					bean = new LabelValueBean(po.getTXN_ID() + " - " + po.getTXN_NAME(), po.getTXN_ID());
					beanList.add(bean);
				}
			}
			System.out.println("beanList>>"+beanList);
		}
		return beanList;
	}
	
	//授權交易代號繳費類別只需要930，不需要931~990
	public List<LabelValueBean> getIdListByTxnTypeII(String txnType){
		List<LabelValueBean> beanList = new LinkedList<LabelValueBean>();
		if(StrUtils.isNotEmpty(txnType)){
			List<TXN_CODE> list = txn_code_Dao.getAll_OrderByTxnId();
			LabelValueBean bean = null;
			for(TXN_CODE po : list){
				if(po.getTXN_TYPE().equalsIgnoreCase(txnType)){
					//過濾931~990
					if(Integer.parseInt(po.getTXN_ID()) <=930 || Integer.parseInt(po.getTXN_ID()) >990){
						bean = new LabelValueBean(po.getTXN_ID() + " - " + po.getTXN_NAME(), po.getTXN_ID());
						beanList.add(bean);
					}
				}
			}
			System.out.println("beanList>>"+beanList);
		}
		return beanList;
	}
	
//	根據業務類別代號取得交易代號清單
	public List<LabelValueBean> getIdListByBS_Id(String bs_id){
		List<LabelValueBean> beanList = new LinkedList<LabelValueBean>();
		if(StrUtils.isNotEmpty(bs_id)){
			List<TXN_CODE> list = txn_code_Dao.getData_ByBS_Id(bs_id);
			LabelValueBean bean = null;
			for(TXN_CODE po : list){
				bean = new LabelValueBean(po.getTXN_ID() + " - " + po.getTXN_NAME(), po.getTXN_ID());
				beanList.add(bean);
			}
		}
		System.out.println("beanList>>"+beanList);
		return beanList;
	}
	
	public String getFeeCodeByTxnId_toJson(Map<String, String> params){
		String paramName;
		String paramValue;
		String jason = "{}";
		Map newmap = new HashMap<>();
		String txnId = "";
		paramName = "txnId";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			txnId = paramValue;
		}
		String action = "";
		paramName = "action";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			action = paramValue;
		}
		
		
		/*
		List<TXN_FEE_MAPPING> feeIdList = null;
		List<FEE_CODE> tmp = null;
		*/
		List<FEE_CODE> feeCodelist = null;
		try{
			feeCodelist = fee_code_Dao.getByFeeId(txnId);
			newmap.put("TXN_ID",txnId);
			userlog_bo.genericLog("V", "查詢成功", action, "查詢條件=" ,  newmap , "" , null);
			/*
			feeIdList = txn_code_Dao.getFeeIdListByTxnId(txnId);
			System.out.println(txnId+"-共"+feeIdList.size()+"筆手續費");
			if(feeIdList != null){
				feeCodelist = new ArrayList();
				for(int i = 0; i < feeIdList.size(); i++){
					tmp = fee_code_Dao.getByPK(
							feeIdList.get(i).getId().getFEE_ID(), feeIdList.get(i).getId().getSTART_DATE()
					);
					if(tmp != null){
						for(FEE_CODE po : tmp){
							feeCodelist.add(po);
						}
					}
				}
			}
			*/
		}catch(Exception e){
			e.printStackTrace();
		}
		
		System.out.println("list>>" + feeCodelist);
		
		if(feeCodelist != null && feeCodelist.size() > 0){
			jason = JSONUtils.toJson(feeCodelist);
			System.out.println("json>>" + jason);
			return jason;
		}
		
		return jason;
	}
	
	public String getMappingByTxnId_toJson(Map<String, String> params){
		String paramName;
		String paramValue;
		
		String txnId = "";
		paramName = "txnId";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			txnId = paramValue;
		}
		
		List<TXN_FEE_MAPPING> feeIdList = null;
		try{
			feeIdList = txn_fee_mapping_Dao.getByTxnId(txnId);
			System.out.println(txnId+"-共"+feeIdList.size()+"筆手續費設定");
		}catch(Exception e){
			e.printStackTrace();
		}
		
		System.out.println("list>>" + feeIdList);
		
		if(feeIdList != null && feeIdList.size() > 0){
			String result = JSONUtils.toJson(feeIdList);
			System.out.println("json>>" + result);
			return result;
		}
		
		return null;
	}
	
	public String search_toJson(Map<String, String> params){
		String paramName;
		String paramValue;
		
		String txnId = "";
		paramName = "TXN_ID";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			txnId = paramValue;
		}
		
		String activeDate = "";
		paramName = "ACTIVE_DATE";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			activeDate = paramValue;
		}
		
		String sord =StrUtils.isNotEmpty(params.get("sord"))? params.get("sord"):"";
		String sidx =StrUtils.isNotEmpty(params.get("sidx"))? params.get("sidx"):"";
		String orderSQL = StrUtils.isNotEmpty(sidx)? " ORDER BY TC."+sidx+" "+sord:"";
		
		List<TXN_CODE> txnCodeList = null;
		try{
			if(StrUtils.isNotEmpty(activeDate)){
				System.out.println("WITH DATE");
				txnCodeList = searchByIdAndActiveDate(txnId, activeDate , orderSQL);
			}else{
				System.out.println("NO DATE");
				if(StrUtils.isEmpty(txnId)){
					txnCodeList = txn_code_Dao.getAllData(orderSQL);
				}else{
					orderSQL = StrUtils.isNotEmpty(sidx)? " ORDER BY "+sidx+" "+sord:"";
					txnCodeList = txn_code_Dao.getByPK(txnId , orderSQL);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		System.out.println("list>>" + txnCodeList);
		
		if(txnCodeList != null && txnCodeList.size() > 0){
			String result = JSONUtils.toJson(txnCodeList);
			System.out.println("json>>" + result);
			return result;
		}
		
		return null;
	}
	
	public TXN_FEE_MAPPING getMappingByPk(String txnId, String feeId, String startDate){
		TXN_FEE_MAPPING tfm = null;
		try{
			tfm = txn_fee_mapping_Dao.get(new TXN_FEE_MAPPING_PK(txnId, feeId, startDate));
		}catch(Exception e){
			e.printStackTrace();
		}
		return tfm;
	}
	
	public TXN_CODE_Dao getTxn_code_Dao() {
		return txn_code_Dao;
	}

	public void setTxn_code_Dao(TXN_CODE_Dao txn_code_Dao) {
		this.txn_code_Dao = txn_code_Dao;
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

	public EACH_USERLOG_BO getUserlog_bo() {
		return userlog_bo;
	}

	public void setUserlog_bo(EACH_USERLOG_BO userlog_bo) {
		this.userlog_bo = userlog_bo;
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

	public AGENT_TXN_CODE_Dao getAgent_txn_code_Dao() {
		return agent_txn_code_Dao;
	}

	public void setAgent_txn_code_Dao(AGENT_TXN_CODE_Dao agent_txn_code_Dao) {
		this.agent_txn_code_Dao = agent_txn_code_Dao;
	}
	
	
}
