package tw.org.twntch.bo;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.struts.util.LabelValueBean;

import tw.org.twntch.db.dao.hibernate.TXN_ERROR_CODE_Dao;
import tw.org.twntch.form.Login_Form;
import tw.org.twntch.po.TXN_ERROR_CODE;
import tw.org.twntch.util.BeanUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.WebServletUtils;
import tw.org.twntch.util.zDateHandler;

public class TXN_ERROR_CODE_BO {

	private TXN_ERROR_CODE_Dao txn_err_code_Dao;


	public Map<String,String> update(String id  ,Map formMap){
		Map<String, String> map = null;
		Map<String, String> pkmap = new HashMap<String, String>();
		Map<String, String> oldmap = new HashMap<String, String>();
		
		try {
			map = new HashMap<String, String>();
			TXN_ERROR_CODE po = txn_err_code_Dao.get(id);
			pkmap.put("ERROR_ID", id);
			if(po == null ){
//				map.put("result", "FALSE");
//				map.put("msg", "更新失敗，查無資料");
//				map.put("target", "edit_p");
				txn_err_code_Dao.updateFail(null, null, pkmap, "儲存失敗，查無資料", 1);
				return map;
			}
//			po = new TXN_ERROR_CODE();
			oldmap = BeanUtils.describe(po);
			BeanUtils.populate(po, formMap);
			po.setUDATE(zDateHandler.getTheDateII());
//			txn_err_code_Dao.save(po);
			txn_err_code_Dao.saveII(po, oldmap, pkmap);
			map.put("result", "TRUE");
			map.put("msg", "儲存成功");
			map.put("target", "search");
		}catch (Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();
//			map.put("result", "ERROR");
//			map.put("msg", "儲存失敗，系統異常");
//			map.put("target", "edit_p");
			map = txn_err_code_Dao.updateFail(null, null, pkmap, "儲存失敗，系統異常", 1);
			return map;
		}
		return map;
	}

public Map<String,String> delete(String id ){
	Map<String, String> map = null;
	TXN_ERROR_CODE po = null;
	Map<String, String> pkmap = new HashMap<String, String>();
	try {
		map = new HashMap<String, String>();
		po = txn_err_code_Dao.get(id);
		pkmap.put("ERROR_ID", id);
		if(po == null ){
//			map.put("result", "FALSE");
//			map.put("msg", "刪除失敗，查無資料");
//			map.put("target", "edit_p");
			map = txn_err_code_Dao.removeFail(po, pkmap, "刪除失敗，查無資料", 1);
			return map;
		}
		
//		txn_err_code_Dao.remove(po);
		txn_err_code_Dao.removeII(po, pkmap);
		map.put("result", "TRUE");
		map.put("msg", "刪除成功");
		map.put("target", "search");
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		map.put("result", "ERROR");
		map.put("msg", "刪除失敗，系統異常");
		map.put("target", "edit_p");
		map = txn_err_code_Dao.removeFail(po, pkmap, "刪除失敗，查無資料", 1);
		return map;
	}
	return map;
}
	
	/**
	 * 新增交易代碼
	 * @param bgid
	 * @param brid
	 * @param formMap
	 * @return
	 */
	public Map<String,String> save(String id ,Map formMap){
		Map<String, String> map = null;
		TXN_ERROR_CODE po = null;
		Map<String, String> pkmap = new HashMap<String, String>();
		try {
			map = new HashMap<String, String>();
			po = txn_err_code_Dao.get(id);
			pkmap.put("ERROR_ID", id);
			if(po != null ){
//				map.put("result", "FALSE");
//				map.put("msg", "儲存失敗，資料重複");
//				map.put("target", "add_p");
				map = txn_err_code_Dao.saveFail(po, pkmap, "儲存失敗，資料重複", 1);
				return map;
			}
			po = new TXN_ERROR_CODE();
			BeanUtils.populate(po, formMap);
			po.setCDATE(zDateHandler.getTheDateII());
//			txn_err_code_Dao.save(po);
			txn_err_code_Dao.save(po , pkmap);
			map.put("result", "TRUE");
			map.put("msg", "儲存成功");
			map.put("target", "search");
		}catch (Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();
//			map.put("result", "ERROR");
//			map.put("msg", "儲存失敗，系統異常");
//			map.put("target", "add_p");
			map = txn_err_code_Dao.saveFail(po, pkmap, "儲存失敗，系統異常", 1);
			return map;
		}
		return map;
	}
	
	public List<TXN_ERROR_CODE> search(String id , String orderSQL){
		Login_Form login_form = (Login_Form) WebServletUtils.getRequest().getSession().getAttribute("login_form");
		List<TXN_ERROR_CODE> list = null ;
		try {
			if(StrUtils.isEmpty(id)){
				if(login_form.getUserData().getUSER_TYPE().equals("B")){
					list = txn_err_code_Dao.getAllData4Bank(orderSQL);
				}else{
					list = txn_err_code_Dao.getAllData(orderSQL);
				}
			}else{
				list= txn_err_code_Dao.getByPK(id.trim() , orderSQL);
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
		List<TXN_ERROR_CODE> list = txn_err_code_Dao.getAll_OrderByTxnId();
		List<LabelValueBean> beanList = new LinkedList<LabelValueBean>();
		LabelValueBean bean = null;
		for(TXN_ERROR_CODE po : list){
			bean = new LabelValueBean(po.getERROR_ID() + " - " + po.getERR_DESC(), po.getERROR_ID());
			beanList.add(bean);
		}
		System.out.println("beanList>>"+beanList);
		return beanList;
	}
	
	public List<LabelValueBean> getIdList4Bank(){
		List<TXN_ERROR_CODE> list = txn_err_code_Dao.getAll_OrderByTxnId4Bank();
		List<LabelValueBean> beanList = new LinkedList<LabelValueBean>();
		LabelValueBean bean = null;
		for(TXN_ERROR_CODE po : list){
			bean = new LabelValueBean(po.getERROR_ID() + " - " + po.getERR_DESC(), po.getERROR_ID());
			beanList.add(bean);
		}
		System.out.println("beanList>>"+beanList);
		return beanList;
	}
	
	public String search_toJson(Map<String, String> param){
		String error_id =StrUtils.isNotEmpty(param.get("ERROR_ID"))? param.get("ERROR_ID"):"";
		error_id = error_id.equals("all")?"":error_id;
		String sord =StrUtils.isNotEmpty(param.get("sord"))? param.get("sord"):"";
		String sidx =StrUtils.isNotEmpty(param.get("sidx"))? param.get("sidx"):"";
		String orderSQL = StrUtils.isNotEmpty(sidx)? " ORDER BY "+sidx+" "+sord:"";
		return JSONUtils.toJson(search(error_id , orderSQL));
	}
	
	public TXN_ERROR_CODE_Dao getTxn_err_code_Dao() {
		return txn_err_code_Dao;
	}

	public void setTxn_err_code_Dao(TXN_ERROR_CODE_Dao txn_err_code_Dao) {
		this.txn_err_code_Dao = txn_err_code_Dao;
	}

	
	
}
