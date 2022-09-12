package tw.org.twntch.bo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts.util.LabelValueBean;

import tw.org.twntch.aop.GenerieAop;
import tw.org.twntch.db.dao.hibernate.BANK_GROUP_BUSINESS_Dao;
import tw.org.twntch.db.dao.hibernate.BUSINESS_TYPE_Dao;
import tw.org.twntch.db.dao.hibernate.EACH_TXN_CODE_Dao;
import tw.org.twntch.db.dao.hibernate.EACH_USERLOG_Dao;
import tw.org.twntch.db.dao.hibernate.TXN_CODE_Dao;
import tw.org.twntch.po.BANK_GROUP;
import tw.org.twntch.po.BANK_GROUP_BUSINESS;
import tw.org.twntch.po.BANK_GROUP_BUSINESS_PK;
import tw.org.twntch.po.BUSINESS_TYPE;
import tw.org.twntch.po.EACH_TXN_CODE;
import tw.org.twntch.po.EACH_USERLOG;
import tw.org.twntch.po.TXN_CODE;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.SpringAppCtxHelper;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.zDateHandler;

public class BUSINESS_TYPE_BO  extends GenerieAop{
	Logger logger = Logger.getLogger(getClass()); 
	private BUSINESS_TYPE_Dao business_type_Dao ;
	private BANK_GROUP_BUSINESS_Dao bank_group_business_Dao;
	private TXN_CODE_Dao txn_code_Dao;
	private EACH_TXN_CODE_Dao each_txn_code_Dao;
	private EACH_USERLOG_Dao userLog_Dao ;
	
	public Map<String,String> save(String id ,String name, String[] selectedBankArray){
		Map<String, String> map = null;
		EACH_USERLOG userlog_po = null;
		try {
			BUSINESS_TYPE po = business_type_Dao.get(id);
			map = new HashMap<String, String>();
			if(po != null ){
				map.put("result", "FALSE");
				map.put("msg", "儲存失敗，業務類別代號重複");
				map.put("target", "business_type_add");
				return map;
			}
			
			po = new BUSINESS_TYPE();
			po.setBUSINESS_TYPE_ID(id);
			po.setBUSINESS_TYPE_NAME(name);
			po.setCDATE(zDateHandler.getTheDateII());
			
			List<BANK_GROUP_BUSINESS> bgbList = new ArrayList<BANK_GROUP_BUSINESS>();
			if(selectedBankArray != null && selectedBankArray.length > 0){
				BANK_GROUP_BUSINESS bgb = null;
				for(int i = 0; i < selectedBankArray.length; i++){
					bgb = new BANK_GROUP_BUSINESS();
					bgb.setId(new BANK_GROUP_BUSINESS_PK(selectedBankArray[i], id));
					bgb.setCDATE(zDateHandler.getTheDateII());
					bgbList.add(bgb);
				}
			}
			
			if(business_type_Dao.saveData(po, bgbList)){
				map.put("result", "TRUE");
				map.put("msg", "儲存成功");
				map.put("target", "business_type");
				
				userlog_po = getUSERLOG("A","");
				userlog_po.setADEXCODE("新增成功，PK={業務類別代號="+po.getBUSINESS_TYPE_ID()+"}");
				
				String insertText = "";
				String result="";
				for(int i=0; i<bgbList.size(); i++){
					if(i != bgbList.size()-1){
						insertText += bgbList.get(i).getId().getBGBK_ID()+",";
					}else{
						insertText += bgbList.get(i).getId().getBGBK_ID();
					}
				}
				result = "{業務類別代號="+po.getBUSINESS_TYPE_ID()+",業務類別名稱="+po.getBUSINESS_TYPE_NAME()+",建立日期="+po.getCDATE()+"}";
				userlog_po.setAFCHCON(result);
				
				userLog_Dao = SpringAppCtxHelper.getBean("userLog_Dao");
				userLog_Dao.aop_save(userlog_po);
				
				if(!insertText.equals("")){
					String AFCHCON = "操作行代號={"+insertText+"}";
					
					update_bankChange(po.getBUSINESS_TYPE_ID(), AFCHCON, "", "A");
				}
			}else{
				map.put("result", "ERROR");
				map.put("msg", "儲存失敗，資料異常");
				map.put("target", "business_type_add");
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			map.put("result", "ERROR");
			map.put("msg", "儲存失敗，系統異常");
			map.put("target", "business_type_add");
			return map;
		}
		return map;
	}
	public Map<String,String> update(String id ,String name,String[] selectedBankArray){
		Map<String, String> map = null;
		EACH_USERLOG userlog_po = null;
		String oldName = "";
		String oldUdate = "";
		try {
			BUSINESS_TYPE po = business_type_Dao.get(id);
			oldName = po.getBUSINESS_TYPE_NAME();
			oldUdate = po.getUDATE();
			map = new HashMap<String, String>();
			if(po == null ){
				map.put("result", "FALSE");
				map.put("msg", "儲存失敗，查無資料");
				map.put("target", "business_type_edit");
				return map;
			}
			po.setBUSINESS_TYPE_NAME(name);
			po.setUDATE(zDateHandler.getTheDateII());
			
			List<BANK_GROUP_BUSINESS> bgbList_toInsert = new ArrayList<BANK_GROUP_BUSINESS>();
			if(selectedBankArray != null && selectedBankArray.length > 0){
				BANK_GROUP_BUSINESS bgb = null;
				for(int i = 0; i < selectedBankArray.length; i++){
					bgb = new BANK_GROUP_BUSINESS();
					bgb.setId(new BANK_GROUP_BUSINESS_PK(selectedBankArray[i], id));
					bgb.setCDATE(zDateHandler.getTheDateII());
					bgbList_toInsert.add(bgb);
				}
			}
			
			List<BANK_GROUP_BUSINESS> bgbList_toDelete = bank_group_business_Dao.getByBsTypeId(id);
			
			if(business_type_Dao.updateData(po, bgbList_toInsert, bgbList_toDelete)){
				map.put("result", "TRUE");
				map.put("msg", "儲存成功");
				map.put("target", "business_type");
				
				userlog_po = getUSERLOG("B","");
				userlog_po.setADEXCODE("修改-儲存成功，PK={業務類別代號="+po.getBUSINESS_TYPE_ID()+"}");
				
				String insertText = "";
				String result1 = "";
				for(int i=0;i<bgbList_toInsert.size();i++){
					
					if(i != (bgbList_toInsert.size())-1){
						insertText += bgbList_toInsert.get(i).getId().getBGBK_ID()+",";
					}else{
						insertText += bgbList_toInsert.get(i).getId().getBGBK_ID();
					}
					
				}
				
				result1 = "業務類別名稱="+po.getBUSINESS_TYPE_NAME();
				
				
				String deleteText = "";
				String result2 = "";
				for(int i=0;i<bgbList_toDelete.size();i++){
					
					if(i != (bgbList_toDelete.size())-1){
						deleteText += bgbList_toDelete.get(i).getId().getBGBK_ID()+",";
					}else{
						deleteText += bgbList_toDelete.get(i).getId().getBGBK_ID();
					}
					
				}
				
				result2 = "業務類別名稱="+oldName;
				
				if(!result1.equals(result2)){
					userlog_po.setAFCHCON("{"+result1+",異動日期="+po.getUDATE()+"}");
					userlog_po.setBFCHCON("{"+result2+",異動日期="+oldUdate+"}");
				}else{
					userlog_po.setAFCHCON("{異動日期="+po.getUDATE()+"}");
					userlog_po.setBFCHCON("{異動日期="+oldUdate+"}");
				}
				
				
				userLog_Dao = SpringAppCtxHelper.getBean("userLog_Dao");
				userLog_Dao.aop_save(userlog_po);
				
				if(!insertText.equals(deleteText)){
					String AFCHCON = "操作行代號={"+insertText+"}";
					String BFCHCON = "操作行代號={"+deleteText+"}";
					
					update_bankChange(po.getBUSINESS_TYPE_ID(), AFCHCON, BFCHCON, "B");
				}
				
			}else{
				map.put("result", "ERROR");
				map.put("msg", "儲存失敗，資料異常");
				map.put("target", "business_type_edit");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("update.Exception>>"+e);
			map.put("result", "ERROR");
			map.put("msg", "儲存失敗，系統異常");
			map.put("target", "business_type_edit");
			return map;
		}
		return map;
	}
	
	public void update_bankChange(String id, String AFCHCON, String BFCHCON, String type){
		EACH_USERLOG userlog_po = null;
		
		if(type.equals("B")){
			userlog_po = getUSERLOG("B","");
			userlog_po.setADEXCODE("修改-儲存成功，PK={業務類別代號="+id+"}");
		}else{
			userlog_po = getUSERLOG("A","");
			userlog_po.setADEXCODE("新增成功，PK={業務類別代號="+id+"}");
		}
		
		userlog_po.setAFCHCON(AFCHCON);
		userlog_po.setBFCHCON(BFCHCON);
		
		userLog_Dao = SpringAppCtxHelper.getBean("userLog_Dao");
		userLog_Dao.aop_save(userlog_po);
	}
	
	public Map<String,String> delete(String id ){
		Map<String, String> map = null;
		EACH_USERLOG userlog_po = null;
		try {
			BUSINESS_TYPE po = business_type_Dao.get(id);
			map = new HashMap<String, String>();
			if(po == null ){
				map.put("result", "FALSE");
				map.put("msg", "刪除失敗，查無資料");
				map.put("target", "business_type_edit");
				return map;
			}
			
			//檢查是否有總行使用該業務類別
			List<BANK_GROUP_BUSINESS> bsTypeList = bank_group_business_Dao.getByBsTypeId(id);
			if(bsTypeList != null && bsTypeList.size() != 0){
				map.put("result", "FALSE");
				map.put("msg", "刪除失敗，該業務類別已被總行使用");
				map.put("target", "business_type_edit");
				return map;
			}
			
			//檢查是否有交易代號(TXN_CODE)歸屬該業務類別下
			List<TXN_CODE> txnCodeList = txn_code_Dao.getByBsTypeId(id);
			if(txnCodeList != null && txnCodeList.size() != 0){
				map.put("result", "FALSE");
				map.put("msg", "刪除失敗，該業務類別已被交易代號(3碼)使用");
				map.put("target", "business_type_edit");
				return map;
			}
			
			//檢查是否有交易代號(EACH_TXN_CODE)歸屬該業務類別下
			List<EACH_TXN_CODE> eachTxnCodeList = each_txn_code_Dao.getByBsTypeId(id);
			if(eachTxnCodeList != null && eachTxnCodeList.size() != 0){
				map.put("result", "FALSE");
				map.put("msg", "刪除失敗，該業務類別已被交易代號(4碼)使用");
				map.put("target", "business_type_edit");
				return map;
			}
			
			business_type_Dao.remove(po);
			map.put("result", "TRUE");
			map.put("msg", "刪除成功");
			map.put("target", "business_type");
			
			userlog_po = getUSERLOG("D","");
			userlog_po.setADEXCODE("刪除成功，PK={業務類別代號="+po.getBUSINESS_TYPE_ID()+"}");
			
			String deleteText = "{業務類別代號="+po.getBUSINESS_TYPE_ID()+",業務類別名稱="+po.getBUSINESS_TYPE_NAME()+",建立日期="+po.getCDATE()+",異動日期="+po.getUDATE()+"}";
			userlog_po.setBFCHCON(deleteText);
			
			userLog_Dao = SpringAppCtxHelper.getBean("userLog_Dao");
			userLog_Dao.aop_save(userlog_po);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			map.put("result", "ERROR");
			map.put("msg", "刪除失敗，系統異常:"+e);
			map.put("target", "business_type_edit");
			return map;
		}
		return map;
	}
	
	public List<BUSINESS_TYPE> search(String id , String name){
		List<BUSINESS_TYPE> list = null ;
		EACH_USERLOG userlog_po = null;
		
		if(StrUtils.isEmpty(id) ){
			//list = business_type_Dao.getAll();
			list = business_type_Dao.find("FROM tw.org.twntch.po.BUSINESS_TYPE ORDER BY BUSINESS_TYPE_ID");
		}else{
			list = new ArrayList<BUSINESS_TYPE>();
			BUSINESS_TYPE po = business_type_Dao.get(id);
			if(po!=null){
				list.add(po);
			}
		}
		System.out.println("list>>"+list);
		list = list.size() ==0 ?null:list;
		return list;
	}
	
	public List<BUSINESS_TYPE> search2(String id , String name , String orderSQL){
		List<BUSINESS_TYPE> list = null ;
		try {
			if(StrUtils.isEmpty(id) ){
				//list = business_type_Dao.getAll();
				list = business_type_Dao.find(" FROM tw.org.twntch.po.BUSINESS_TYPE "+orderSQL);
			}else{
				list = new ArrayList<BUSINESS_TYPE>();
//			20150923 edit by hugo  因使用business_type_Dao.get(id) 後續JSONUtils.toJson 會發生錯誤>> net.sf.json.JSONObject  - Property 'transactionTimeout' has no read method. SKIPPED
				list = business_type_Dao.find(" FROM tw.org.twntch.po.BUSINESS_TYPE WHERE BUSINESS_TYPE_ID = ? "+orderSQL , id);
//			BUSINESS_TYPE po = business_type_Dao.get(id);
//			if(po!=null){
//				list.add(po);
//			}
			}
			
			list = list== null ? new ArrayList<BUSINESS_TYPE>():list;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.debug("search2.Exception>>"+e);
		}
		return list;
	}
	
	public String search_toJson(Map<String, String> param){
		String json = "{}";
		String business_type_id =StrUtils.isNotEmpty(param.get("BUSINESS_TYPE_ID"))? param.get("BUSINESS_TYPE_ID"):"";
		String sord =StrUtils.isNotEmpty(param.get("sord"))? param.get("sord"):"";
		String sidx =StrUtils.isNotEmpty(param.get("sidx"))? param.get("sidx"):"";
		String orderSQL = StrUtils.isNotEmpty(sidx)? " ORDER BY "+sidx+" "+sord:"";
		
		System.out.println("business_type_id>>"+business_type_id);
		json = JSONUtils.toJson(search2(business_type_id, "" , orderSQL ));
		System.out.println("json>>"+json);
		return json;
	}
	
	
	public List<LabelValueBean> getBsTypeIdList(){
		//List<BUSINESS_TYPE> list = business_type_Dao.getAll();
		List<BUSINESS_TYPE> list = business_type_Dao.find("FROM tw.org.twntch.po.BUSINESS_TYPE ORDER BY BUSINESS_TYPE_ID");
		List<LabelValueBean> beanList = new LinkedList<LabelValueBean>();
		LabelValueBean bean = null;
		new LabelValueBean("===請選擇===", "");
//		beanList.add(bean);
		for(BUSINESS_TYPE po :list){
			po.getBUSINESS_TYPE_ID();
			bean = new LabelValueBean(po.getBUSINESS_TYPE_ID()+" - "+po.getBUSINESS_TYPE_NAME(), po.getBUSINESS_TYPE_ID());
			beanList.add(bean);
		}
		System.out.println("beanList>>"+beanList);
		return beanList;
		
	}
	
	//GET BSTYPELIST FROM AJAX
	public String getBsTypeIdList(Map<String, String> params){
		List<BUSINESS_TYPE> list = business_type_Dao.find("FROM tw.org.twntch.po.BUSINESS_TYPE ORDER BY BUSINESS_TYPE_ID");
		List<Map<String, String>> beanList = new LinkedList<Map<String, String>>();
		Map<String, String> m = null;
		for(BUSINESS_TYPE po : list){
			m = new HashMap<String, String>();
			m.put("name", po.getBUSINESS_TYPE_ID() + "-" + po.getBUSINESS_TYPE_NAME());
			m.put("value", po.getBUSINESS_TYPE_ID());
			beanList.add(m);
		}
		System.out.println("beanList>>"+beanList);
		return JSONUtils.toJson(beanList);
	}
	
	/**
	 * 從[{label:"XXX", value:"ooo"}]轉成下拉選單選項
	 * @param json_Options
	 * @return
	 */
	public List<LabelValueBean> getDropdownFromJson(String json_Options){
		List<Map> list = JSONUtils.toList(json_Options);
		List<LabelValueBean> beanList = new LinkedList<LabelValueBean>();
		LabelValueBean bean = null;
		for(Map option : list){
			bean = new LabelValueBean((String)option.get("label"), (String)option.get("value"));
			beanList.add(bean);
		}
		System.out.println("beanList>>"+beanList);
		return beanList;
		
	}
	
	/**
	 * 找出未承辦某業務類別的所有總行清單
	 * @param bsType
	 * @return
	 */
	public List<LabelValueBean> getUnselectedBgbkIdList(String bsType){
		List<BANK_GROUP> list = bank_group_business_Dao.getUnselectedBgbkIdList(bsType);
		List<LabelValueBean> beanList = new LinkedList<LabelValueBean>();
		LabelValueBean bean = null;
		for(BANK_GROUP po : list){
			bean = new LabelValueBean(po.getBGBK_ID() + " - " + po.getBGBK_NAME(), po.getBGBK_ID());
			beanList.add(bean);
		}
		System.out.println("beanList>>"+beanList);
		return beanList;
		
	}
	
	/**
	 * 找出有承辦某業務類別的所有總行清單
	 * @param bsType
	 * @return
	 */
	public List<LabelValueBean> getSelectedBgbkIdList(String bsType){
		List<BANK_GROUP> list = bank_group_business_Dao.getSelectedBgbkIdList(bsType);
		List<LabelValueBean> beanList = new LinkedList<LabelValueBean>();
		LabelValueBean bean = null;
		for(BANK_GROUP po : list){
			bean = new LabelValueBean(po.getBGBK_ID() + " - " + po.getBGBK_NAME(), po.getBGBK_ID());
			beanList.add(bean);
		}
		System.out.println("beanList>>"+beanList);
		return beanList;
		
	}
	
	public BUSINESS_TYPE_Dao getBusiness_type_Dao() {
		return business_type_Dao;
	}
	public void setBusiness_type_Dao(BUSINESS_TYPE_Dao business_type_Dao) {
		this.business_type_Dao = business_type_Dao;
	}
	public BANK_GROUP_BUSINESS_Dao getBank_group_business_Dao() {
		return bank_group_business_Dao;
	}
	public void setBank_group_business_Dao(
			BANK_GROUP_BUSINESS_Dao bank_group_business_Dao) {
		this.bank_group_business_Dao = bank_group_business_Dao;
	}
	public TXN_CODE_Dao getTxn_code_Dao() {
		return txn_code_Dao;
	}
	public void setTxn_code_Dao(TXN_CODE_Dao txn_code_Dao) {
		this.txn_code_Dao = txn_code_Dao;
	}
	public EACH_TXN_CODE_Dao getEach_txn_code_Dao() {
		return each_txn_code_Dao;
	}
	public void setEach_txn_code_Dao(EACH_TXN_CODE_Dao each_txn_code_Dao) {
		this.each_txn_code_Dao = each_txn_code_Dao;
	}
	
}
