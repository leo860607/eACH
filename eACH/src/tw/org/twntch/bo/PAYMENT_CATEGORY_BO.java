package tw.org.twntch.bo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.struts.util.LabelValueBean;

import tw.org.twntch.db.dao.hibernate.PAYMENT_CATEGORY_DAO;
import tw.org.twntch.po.BILL_TYPE;
import tw.org.twntch.util.BeanUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.zDateHandler;

public class PAYMENT_CATEGORY_BO {
	
	private PAYMENT_CATEGORY_DAO payment_category_dao;

	public String getByBsTypeId(Map<String, String> params){
		String result = "";
		String bsTypeId = "";
		if(StrUtils.isNotEmpty(params.get("bsTypeId"))){
			bsTypeId = params.get("bsTypeId").trim(); 		
		}
		
		List<BILL_TYPE> list = null;
		try{
			list = payment_category_dao.getByBsTypeId(bsTypeId);
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
		
		List<BILL_TYPE> list = null;
		List<Map<String, String>> rtnList = new ArrayList<Map<String, String>>();
		try{
			if(StrUtils.isEmpty(bsTypeId)){
				
			}else{
				list = payment_category_dao.getByBsTypeId(bsTypeId);
			}
			
			Map<String, String> m = null;
			for(BILL_TYPE po : list){
				m = new HashMap<String, String>();
				m.put("name", po.getBILL_TYPE_ID() + "-" + po.getBILL_TYPE_NAME());
				m.put("value", po.getBILL_TYPE_ID());
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
		
		List<BILL_TYPE> list = null;
		try{
			list = payment_category_dao.getByBsTypeId(bsTypeId);
		}catch(Exception e){
			e.printStackTrace();
		}
		if(list != null){
			result = JSONUtils.toJson(list);
		}
		return result;
	}
	
	public String search_toJson2(Map<String, String> param){
		String BILL_TYPE_ID =StrUtils.isNotEmpty(param.get("BILL_TYPE_ID"))? param.get("BILL_TYPE_ID"):"";
		BILL_TYPE_ID = BILL_TYPE_ID.equals("all")?"":BILL_TYPE_ID;
		String sord =StrUtils.isNotEmpty(param.get("sord"))? param.get("sord"):"";
		String sidx =StrUtils.isNotEmpty(param.get("sidx"))? param.get("sidx"):"";
		String orderSQL = StrUtils.isNotEmpty(sidx)? " ORDER BY "+sidx+" "+sord:"";
		return JSONUtils.toJson(search(BILL_TYPE_ID , orderSQL));
	}
	
	public Map<String,String> update(String id  ,Map formMap){
		Map<String, String> map = null;
		Map<String, String> pkmap = new HashMap<String, String>();
		Map<String, String> oldmap = new HashMap<String, String>();
		
		try {
			map = new HashMap<String, String>();
			BILL_TYPE po = payment_category_dao.get(id);
			pkmap.put("BILL_TYPE_ID", id);
			if(po == null ){
//				map.put("result", "FALSE");
//				map.put("msg", "更新失敗，查無資料");
//				map.put("target", "edit_p");
				payment_category_dao.updateFail(null, null, pkmap, "儲存失敗，查無資料", 1);
				return map;
			}
//			po = new BILL_TYPE();
			oldmap = BeanUtils.describe(po);
			BeanUtils.populate(po, formMap);
			payment_category_dao.save(po);
//			payment_category_dao.saveII(po, oldmap, pkmap);
			map.put("result", "TRUE");
			map.put("msg", "儲存成功");
			map.put("target", "search");
		}catch (Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();
//			map.put("result", "ERROR");
//			map.put("msg", "儲存失敗，系統異常");
//			map.put("target", "edit_p");
			map = payment_category_dao.updateFail(null, null, pkmap, "儲存失敗，系統異常", 1);
			return map;
		}
		return map;
	}

public Map<String,String> delete(String id ){
	Map<String, String> map = null;
	BILL_TYPE po = null;
	Map<String, String> pkmap = new HashMap<String, String>();
	try {
		map = new HashMap<String, String>();
		po = payment_category_dao.get(id);
		pkmap.put("BILL_TYPE_ID", id);
		if(po == null ){
//			map.put("result", "FALSE");
//			map.put("msg", "刪除失敗，查無資料");
//			map.put("target", "edit_p");
			map = payment_category_dao.removeFail(po, pkmap, "刪除失敗，查無資料", 1);
			return map;
		}
		
		payment_category_dao.remove(po);
//		payment_category_dao.removeII(po, pkmap);
		map.put("result", "TRUE");
		map.put("msg", "刪除成功");
		map.put("target", "search");
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		map.put("result", "ERROR");
		map.put("msg", "刪除失敗，系統異常");
		map.put("target", "edit_p");
		map = payment_category_dao.removeFail(po, pkmap, "刪除失敗，查無資料", 1);
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
		BILL_TYPE po = null;
		Map<String, String> pkmap = new HashMap<String, String>();
		try {
			map = new HashMap<String, String>();
			po = payment_category_dao.get(id);
			pkmap.put("BILL_TYPE_ID", id);
			if(po != null ){
//				map.put("result", "FALSE");
//				map.put("msg", "儲存失敗，資料重複");
//				map.put("target", "add_p");
				map = payment_category_dao.saveFail(po, pkmap, "儲存失敗，資料重複", 1);
				return map;
			}
			po = new BILL_TYPE();
			BeanUtils.populate(po, formMap);
//			payment_category_dao.save(po);
			payment_category_dao.save(po , pkmap);
			map.put("result", "TRUE");
			map.put("msg", "儲存成功");
			map.put("target", "search");
		}catch (Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();
//			map.put("result", "ERROR");
//			map.put("msg", "儲存失敗，系統異常");
//			map.put("target", "add_p");
			map = payment_category_dao.saveFail(po, pkmap, "儲存失敗，系統異常", 1);
			return map;
		}
		return map;
	}
	
	public List<BILL_TYPE> search(String id){
		
		List<BILL_TYPE> list = null ;
		try {
			if(StrUtils.isEmpty(id)){
				list = payment_category_dao.getAll_OrderByEachTxnId();
			}else{
				list= payment_category_dao.getByPK(id.trim());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("list>>"+list);
		list = list.size() == 0 ? null : list;
		return list;
	}
	public List<BILL_TYPE> search(String id , String orderSQL){
		
		List<BILL_TYPE> list = null ;
		try {
			if(StrUtils.isEmpty(id)){
				list = payment_category_dao.getAllData(orderSQL);
			}else{
				list= payment_category_dao.getByPK(id.trim());
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
		List<BILL_TYPE> list = payment_category_dao.getAll_OrderByEachTxnId();
		List<LabelValueBean> beanList = new LinkedList<LabelValueBean>();
		LabelValueBean bean = null;
		for(BILL_TYPE po : list){
			bean = new LabelValueBean(po.getBILL_TYPE_ID() + " - " + po.getBILL_TYPE_NAME(), po.getBILL_TYPE_ID());
			beanList.add(bean);
		}
		System.out.println("beanList>>"+beanList);
		return beanList;
	}

	public PAYMENT_CATEGORY_DAO getPayment_category_dao() {
		return payment_category_dao;
	}

	public void setPayment_category_dao(PAYMENT_CATEGORY_DAO payment_category_dao) {
		this.payment_category_dao = payment_category_dao;
	}
	
	
	
}
