package tw.org.twntch.bo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.struts.util.LabelValueBean;

import tw.org.twntch.db.dao.hibernate.EACH_TXN_CODE_Dao;
import tw.org.twntch.po.EACH_TXN_CODE;
import tw.org.twntch.util.BeanUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.zDateHandler;

public class EACH_TXN_CODE_BO {
	private EACH_TXN_CODE_Dao each_txn_code_Dao;
	private EACH_USERLOG_BO userlog_bo;//寫操作軌跡記錄
	
	public String getByBsTypeId(Map<String, String> params){
		String result = "";
		String bsTypeId = "";
		if(StrUtils.isNotEmpty(params.get("bsTypeId"))){
			bsTypeId = params.get("bsTypeId").trim(); 		
		}
		
		List<EACH_TXN_CODE> list = null;
		try{
			list = each_txn_code_Dao.getByBsTypeId(bsTypeId);
		}catch(Exception e){
			e.printStackTrace();
		}
		if(list != null){
			result = JSONUtils.toJson(list);
		}
		return result;
	}
	
	/**
	 * For AJAX dropdownlist
	 * @param params
	 * @return
	 */
	public String getByBsTypeId_2(Map<String, String> params){
		String result = "";
		String bsTypeId = "";
		if(StrUtils.isNotEmpty(params.get("bsTypeId"))){
			bsTypeId = params.get("bsTypeId").trim(); 		
		}
		
		List<EACH_TXN_CODE> list = null;
		List<Map<String, String>> rtnList = new ArrayList<Map<String, String>>();
		try{
			if(StrUtils.isEmpty(bsTypeId)){
				list = each_txn_code_Dao.getTranCode();
			}else{
				list = each_txn_code_Dao.getByBsTypeId(bsTypeId);
			}
			
			Map<String, String> m = null;
			for(EACH_TXN_CODE po : list){
				m = new HashMap<String, String>();
				m.put("name", po.getEACH_TXN_ID() + "-" + po.getEACH_TXN_NAME());
				m.put("value", po.getEACH_TXN_ID());
				rtnList.add(m);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		if(list != null){
			result = JSONUtils.toJson(rtnList);
		}
		return result;
	}

	public String search_toJson(Map<String, String> params){
		String result = "";
		String bsTypeId = "";
		if(StrUtils.isNotEmpty(params.get("bsTypeId"))){
			bsTypeId = params.get("bsTypeId").trim(); 		
		}
		
		List<EACH_TXN_CODE> list = null;
		try{
			list = each_txn_code_Dao.getByBsTypeId(bsTypeId);
		}catch(Exception e){
			e.printStackTrace();
		}
		if(list != null){
			result = JSONUtils.toJson(list);
		}
		return result;
	}
	
	public String search_toJson2(Map<String, String> param){
		String each_txn_id =StrUtils.isNotEmpty(param.get("EACH_TXN_ID"))? param.get("EACH_TXN_ID"):"";
		each_txn_id = each_txn_id.equals("all")?"":each_txn_id;
		String sord =StrUtils.isNotEmpty(param.get("sord"))? param.get("sord"):"";
		String sidx =StrUtils.isNotEmpty(param.get("sidx"))? param.get("sidx"):"";
		String orderSQL = StrUtils.isNotEmpty(sidx)? " ORDER BY "+sidx+" "+sord:"";
		userlog_bo.writeLog("C",null,null,param);
		return JSONUtils.toJson(search(each_txn_id , orderSQL));
	}
	
	public Map<String,String> update(String id  ,Map formMap){
		Map<String, String> map = null;
		Map<String, String> pkmap = new HashMap<String, String>();
		Map<String, String> oldmap = new HashMap<String, String>();
		
		try {
			map = new HashMap<String, String>();
			EACH_TXN_CODE po = each_txn_code_Dao.get(id);
			pkmap.put("EACH_TXN_ID", id);
			if(po == null ){
//				map.put("result", "FALSE");
//				map.put("msg", "更新失敗，查無資料");
//				map.put("target", "edit_p");
				each_txn_code_Dao.updateFail(null, null, pkmap, "儲存失敗，查無資料", 1);
				return map;
			}
//			po = new EACH_TXN_CODE();
			oldmap = BeanUtils.describe(po);
			BeanUtils.populate(po, formMap);
			po.setUDATE(zDateHandler.getTheDateII());
//			each_txn_code_Dao.save(po);
			each_txn_code_Dao.saveII(po, oldmap, pkmap);
			map.put("result", "TRUE");
			map.put("msg", "儲存成功");
			map.put("target", "search");
		}catch (Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();
//			map.put("result", "ERROR");
//			map.put("msg", "儲存失敗，系統異常");
//			map.put("target", "edit_p");
			map = each_txn_code_Dao.updateFail(null, null, pkmap, "儲存失敗，系統異常", 1);
			return map;
		}
		return map;
	}

public Map<String,String> delete(String id ){
	Map<String, String> map = null;
	EACH_TXN_CODE po = null;
	Map<String, String> pkmap = new HashMap<String, String>();
	try {
		map = new HashMap<String, String>();
		po = each_txn_code_Dao.get(id);
		pkmap.put("EACH_TXN_ID", id);
		if(po == null ){
//			map.put("result", "FALSE");
//			map.put("msg", "刪除失敗，查無資料");
//			map.put("target", "edit_p");
			map = each_txn_code_Dao.removeFail(po, pkmap, "刪除失敗，查無資料", 1);
			return map;
		}
		
//		each_txn_code_Dao.remove(po);
		each_txn_code_Dao.removeII(po, pkmap);
		map.put("result", "TRUE");
		map.put("msg", "刪除成功");
		map.put("target", "search");
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		map.put("result", "ERROR");
		map.put("msg", "刪除失敗，系統異常");
		map.put("target", "edit_p");
		map = each_txn_code_Dao.removeFail(po, pkmap, "刪除失敗，查無資料", 1);
		return map;
	}
	return map;
}
	
	/**
	 * 新增交易類別
	 * @param bgid
	 * @param brid
	 * @param formMap
	 * @return
	 */
	public Map<String,String> save(String id ,Map formMap){
		Map<String, String> map = null;
		EACH_TXN_CODE po = null;
		Map<String, String> pkmap = new HashMap<String, String>();
		try {
			map = new HashMap<String, String>();
			po = each_txn_code_Dao.get(id);
			pkmap.put("EACH_TXN_ID", id);
			if(po != null ){
//				map.put("result", "FALSE");
//				map.put("msg", "儲存失敗，資料重複");
//				map.put("target", "add_p");
				map = each_txn_code_Dao.saveFail(po, pkmap, "儲存失敗，資料重複", 1);
				return map;
			}
			po = new EACH_TXN_CODE();
			BeanUtils.populate(po, formMap);
			po.setCDATE(zDateHandler.getTheDateII());
//			each_txn_code_Dao.save(po);
			each_txn_code_Dao.save(po , pkmap);
			map.put("result", "TRUE");
			map.put("msg", "儲存成功");
			map.put("target", "search");
		}catch (Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();
//			map.put("result", "ERROR");
//			map.put("msg", "儲存失敗，系統異常");
//			map.put("target", "add_p");
			map = each_txn_code_Dao.saveFail(po, pkmap, "儲存失敗，系統異常", 1);
			return map;
		}
		return map;
	}
	
	public List<EACH_TXN_CODE> search(String id){
		
		List<EACH_TXN_CODE> list = null ;
		try {
			if(StrUtils.isEmpty(id)){
				list = each_txn_code_Dao.getAll_OrderByEachTxnId();
			}else{
				list= each_txn_code_Dao.getByPK(id.trim());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("list>>"+list);
		list = list.size() == 0 ? null : list;
		return list;
	}
	public List<EACH_TXN_CODE> search(String id , String orderSQL){
		
		List<EACH_TXN_CODE> list = null ;
		try {
			if(StrUtils.isEmpty(id)){
				list = each_txn_code_Dao.getAllData(orderSQL);
			}else{
				list= each_txn_code_Dao.getByPK(id.trim());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("list>>"+list);
		list = list.size() == 0 ? null : list;
		return list;
	}
	
	
	public List<LabelValueBean> getIdList(){
		List<EACH_TXN_CODE> list = each_txn_code_Dao.getAll_OrderByEachTxnId();
		List<LabelValueBean> beanList = new LinkedList<LabelValueBean>();
		LabelValueBean bean = null;
		for(EACH_TXN_CODE po : list){
			bean = new LabelValueBean(po.getEACH_TXN_ID() + " - " + po.getEACH_TXN_NAME(), po.getEACH_TXN_ID());
			beanList.add(bean);
		}
		System.out.println("beanList>>"+beanList);
		return beanList;
	}
	
	public EACH_TXN_CODE_Dao getEach_txn_code_Dao() {
		return each_txn_code_Dao;
	}

	public void setEach_txn_code_Dao(EACH_TXN_CODE_Dao each_txn_code_Dao) {
		this.each_txn_code_Dao = each_txn_code_Dao;
	}

	public EACH_USERLOG_BO getUserlog_bo() {
		return userlog_bo;
	}
	
	public void setUserlog_bo(EACH_USERLOG_BO userlog_bo) {
		this.userlog_bo = userlog_bo;
	}	
	
}
