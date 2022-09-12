package tw.org.twntch.bo;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts.util.LabelValueBean;

import tw.org.twntch.db.dao.hibernate.EACH_FUNC_LIST_Dao;
import tw.org.twntch.po.EACH_FUNC_LIST;
import tw.org.twntch.util.BeanUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.zDateHandler;

public class EACH_FUNC_LIST_BO {
	private Logger logger = Logger.getLogger(EACH_FUNC_LIST_BO.class);
	private EACH_FUNC_LIST_Dao func_list_Dao ;
	private EACH_USERLOG_BO userlog_bo ;
	
	/* 20141209 HUANGPU 此方法原本是在LOGIN時取得功能清單用，但邏輯上應該是以EACH_ROLE_FUNC的設定為主，故停用此方法
	public List getMenu(){
		List<EACH_FUNC_LIST> menuList = null;
		List<EACH_FUNC_LIST> subList = null;
		Map menuItem = null;
		Map subItem = null;
		List menuList_N = new ArrayList();
		List subList_N = new ArrayList();
		try {
			//TODO 依群組找作業模組
			menuList = func_list_Dao.getFuncItemByType("1");
			if(menuList != null){
				menuList_N = new ArrayList();
				for(int i = 0; i < menuList.size(); i++){
					menuItem = new HashMap();
					menuItem.put("FUNC_NAME", menuList.get(i).getFUNC_NAME());
					menuItem.put("FUNC_URL", menuList.get(i).getFUNC_URL());
					menuItem.put("FUNC_TYPE", menuList.get(i).getFUNC_TYPE());
					subList = func_list_Dao.getNextSubItem(menuList.get(i).getFUNC_ID());
					if(subList != null){
						subList_N = new ArrayList();
						for(int j = 0; j < subList.size(); j++){
							subItem = new HashMap();
							subItem.put("FUNC_NAME", subList.get(j).getFUNC_NAME());
							subItem.put("FUNC_URL", subList.get(j).getFUNC_URL());
							subItem.put("FUNC_TYPE", subList.get(j).getFUNC_TYPE());
							subList_N.add(subItem);
						}
						menuItem.put("SUB_LIST", subList_N);
					}
					menuList_N.add(menuItem);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("menu>>" + menuList_N);
		return menuList_N;
	}
	*/
	
	/* 20141208 HUANGPU JSON版本暫不使用
	public String getMenu(Map<String, String> params){
		String json = "";
		List<EACH_FUNC_LIST> menuList = null;
		List menuList_N = new ArrayList();
		try {
			//找作業模組
			menuList = func_list_Dao.getFuncItemByType("1");
			if(menuList != null){
				menuList_N = new ArrayList();
				for(int i = 0; i < menuList.size(); i++){
					menuList_N.add(getMenuItem(menuList.get(i)));
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		json = JSONUtils.toJson(menuList_N);
		System.out.println("menuJson>>" + json);
		return json;
	}
	
	public Map getMenuItem(EACH_FUNC_LIST obj) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		Map menuItem = tw.org.twntch.util.BeanUtils.describe(obj);
		List<EACH_FUNC_LIST> subList = func_list_Dao.getNextSubItem(obj.getFUNC_ID());
		if(subList != null){
			List subList_M = new ArrayList();
			menuItem.put("SUB_LIST", subList_M);
			for(int i = 0; i < subList.size(); i++){
				((List)menuItem.get("SUB_LIST")).add(getMenuItem(subList.get(i)));
			}
		}
		return menuItem;
	}
	*/
	
	/**
	 * 檢驗該功能取消打勾的的角色是否有被使用
	 * 有TRUE 無:FALSE
	 * @param params
	 * @return
	 */
	public Map<String,String> validate(Map<String,String> params){
		Map<String , String> retMap = new HashMap<String , String>();
		List<String> inVals = new ArrayList<String>();
		StringBuffer msg = new StringBuffer();
		System.out.println("params>>"+params);
		try {
			if(params!= null){
				for(String key :params.keySet()){
					if((key.equals("TCH_FUNC") || key.equals("BANK_FUNC") || key.equals("COMPANY_FUNC")) ){
						if(StrUtils.isEmpty(params.get(key))){
							if(key.equals("TCH_FUNC")){
								inVals.add("A");
							}
							if(key.equals("BANK_FUNC")){
								inVals.add("B");
							}
							if(key.equals("COMPANY_FUNC")){
								inVals.add("C");
							}
						}
					}
				}//fro end
			}
			System.out.println("inVals>>"+inVals);
			List<Map> list = func_list_Dao.funcIsUsed(params.get("FUNC_ID"), inVals);
			System.out.println("list>>"+list);
			if(list == null || list.size() == 0 ){
				retMap.put("result", "TRUE");
			}else{
				msg.append("功能所屬單位，無法修改，此功能已被下列群組使用:");
				for(Map map :list){
					msg.append("\\n");
					msg.append(map.get("ROLE_ID"));
					msg.append("\\n");//避免Unterminated string literal
				}
				retMap.put("result", "FALSE");
				retMap.put("msg", msg.toString());
				retMap.put("target", "edit_p");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.debug("validate.Exception>>"+e);
		}
		System.out.println("retMap>>"+retMap);
		return retMap;
	}
	
	
	
	//帶出全部作業模組清單
	public List<LabelValueBean> getFuncIdList(){
		List<EACH_FUNC_LIST> list = func_list_Dao.getFuncItemByType("1");
		List<LabelValueBean> beanList = new LinkedList<LabelValueBean>();
		LabelValueBean bean = null;
		for(EACH_FUNC_LIST po : list){
			bean = new LabelValueBean(po.getFUNC_ID() + " - " + po.getFUNC_NAME(), po.getFUNC_ID());
			beanList.add(bean);
		}
		System.out.println("beanList>>"+beanList);
		return beanList;
	}
	
	//依照功能所屬單位找出「作業模組」清單，若未帶入功能所屬單位，則帶出全部
	public List<LabelValueBean> getFuncIdList(Map<String, String> funcOwner){
		List<EACH_FUNC_LIST> list = func_list_Dao.getFuncItemByType("1");
		List<LabelValueBean> beanList = new LinkedList<LabelValueBean>();
		LabelValueBean bean = null;
		
		String TCH_FUNC = funcOwner.get("TCH_FUNC"), 
			   BANK_FUNC = funcOwner.get("BANK_FUNC")
			   //,COMPANY_FUNC = funcOwner.get("COMPANY_FUNC")
			   ;
		for(EACH_FUNC_LIST po : list){
			bean = new LabelValueBean(po.getFUNC_ID() + " - " + po.getFUNC_NAME(), po.getFUNC_ID());
			if(StrUtils.isEmpty(TCH_FUNC) && StrUtils.isEmpty(BANK_FUNC)){
				beanList.add(bean);
			}else 
			//依照功能所屬單位判斷是否撈出
			if( po.getTCH_FUNC().equals(TCH_FUNC) || po.getBANK_FUNC().equals(BANK_FUNC)){
				beanList.add(bean);
			}
		}
		System.out.println("beanList>>"+beanList);
		return beanList;
	}
	
	//依照功能所屬單位找出「作業模組」清單，若未帶入功能所屬單位，則帶出全部(JSON版本)
	public String getFuncIdList_toJson(Map<String, String> params){
		String result = "";
		String paramName;
		String paramValue;
		
		String TCH_FUNC = "";
		paramName = "TCH_FUNC";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			TCH_FUNC = paramValue;
		}
		
		String BANK_FUNC = "";
		paramName = "BANK_FUNC";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			BANK_FUNC = paramValue;
		}
		
		List<EACH_FUNC_LIST> list = func_list_Dao.getFuncItemByType("1");
		List<Map<String, String>> beanList = new ArrayList<Map<String, String>>();
		Map<String, String> bean = null;
		for(EACH_FUNC_LIST po : list){
			bean = new HashMap<String, String>();
			bean.put("label", po.getFUNC_ID() + " - " + po.getFUNC_NAME());
			bean.put("value", po.getFUNC_ID());
			if(StrUtils.isEmpty(TCH_FUNC) && StrUtils.isEmpty(BANK_FUNC)){
				beanList.add(bean);
			}else 
			//依照功能所屬單位判斷是否撈出
			if( po.getTCH_FUNC().equals(TCH_FUNC) || po.getBANK_FUNC().equals(BANK_FUNC)){
				beanList.add(bean);
			}
		}
		result = JSONUtils.toJson(beanList);
		System.out.println("json>>"+result);
		return result;
	}	
	
	/**
	 * 新增
	 * @param formMap
	 * @return
	 */
	public Map<String, String> save(Map<String,String> formMap){
		Map<String, String> map = null;
		EACH_FUNC_LIST po = null ;
		Map<String, String> pkmap = new HashMap<String, String>();
		try {
			map = new HashMap<String, String>();
			po = new EACH_FUNC_LIST();
			BeanUtils.populate(po, formMap);
			po.setTCH_FUNC(formMap.get("TCH_FUNC") == null? "N" : (String)formMap.get("TCH_FUNC"));
			po.setBANK_FUNC(formMap.get("BANK_FUNC") == null? "N" : (String)formMap.get("BANK_FUNC"));
			po.setCOMPANY_FUNC(formMap.get("COMPANY_FUNC") == null? "N" : (String)formMap.get("COMPANY_FUNC"));
			po.setIS_USED(formMap.get("IS_USED") == null? "N" : (String)formMap.get("IS_USED"));
			po.setUSE_IKEY(formMap.get("USE_IKEY") == null? "N" : (String)formMap.get("USE_IKEY"));
			po.setPROXY_FUNC(formMap.get("PROXY_FUNC") == null? "N" : (String)formMap.get("PROXY_FUNC"));
			
			pkmap.put("FUNC_ID", formMap.get("FUNC_ID"));
			
			if(func_list_Dao.get(po.getFUNC_ID()) != null){
//				map.put("result", "FALSE");
//				map.put("msg", "儲存失敗，已有資料");
//				map.put("target", "add_p");
				map = func_list_Dao.saveFail_PK(po, pkmap, "儲存失敗，已有資料");
				return map;
			}
			po.setCDATE(zDateHandler.getTheDateII());
//			20150612 edit by hugo 新增不適用此API
//			if(func_list_Dao.saveData(po, null)){
			func_list_Dao.save(po);
			map.put("result", "TRUE");
			map.put("msg", "儲存成功");
			map.put("target", "search");
//			}else{
//				map.put("result", "FALSE");
//				map.put("msg", "儲存失敗");
//				map.put("target", "add_p");
//			}
		}catch (Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();
			map.put("result", "ERROR");
			map.put("msg", "儲存失敗，系統異常:"+e);
			map.put("target", "add_p");
			map = func_list_Dao.saveFail_GE(po, "儲存失敗，系統異常");
			return map;
		}
		System.out.println("save.map"+map);
		return map;
	}
	
	public String search_toJson(Map<String, String> params){
		String result = "";
		String paramName;
		String paramValue;
		
		String FUNC_ID = "";
		paramName = "FUNC_ID";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			FUNC_ID = paramValue;
		}
		String sord =StrUtils.isNotEmpty(params.get("sord"))? params.get("sord"):"";
		String sidx =StrUtils.isNotEmpty(params.get("sidx"))? params.get("sidx"):"";
		String orderSQL = StrUtils.isNotEmpty(sidx)? " ORDER BY "+sidx+" "+sord:"";
		result = JSONUtils.toJson(search(FUNC_ID , orderSQL));
		System.out.println("json>>" + result);
		return result;
	}
	
	public List<EACH_FUNC_LIST> search(String funcId){
		List<EACH_FUNC_LIST> list = null;
		try{
			if(StrUtils.isNotEmpty(funcId)){
				list = func_list_Dao.getData(funcId);
			}else{
				list = func_list_Dao.getAllData();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("list>>"+list);
		list = list == null? null : list.size() == 0? null : list;
		return list;
	}
	public List<EACH_FUNC_LIST> search(String funcId ,String orderSQL){
		List<EACH_FUNC_LIST> list = null;
		try{
			if(StrUtils.isNotEmpty(funcId)){
				list = func_list_Dao.getData(funcId , orderSQL);
			}else{
				list = func_list_Dao.getAllData(orderSQL);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("list>>"+list);
		list = list == null? null : list.size() == 0? null : list;
		return list;
	}
	
	public Map<String, String> update(Map<String,String> formMap){
		boolean result = false;
		EACH_FUNC_LIST po = null;
		Map<String, String> map = null;
		Map<String, String> newmap = new HashMap<String, String>();
		Map<String, String> oldmap = new HashMap<String, String>();
		Map<String, String> pkmap = new HashMap<String, String>();
		try {
			map = new HashMap<String, String>();
			po = new EACH_FUNC_LIST();
			pkmap.put("FUNC_ID", formMap.get("FUNC_ID"));
			po = func_list_Dao.get(formMap.get("FUNC_ID"));
			if(po == null){
//				map.put("result", "FALSE");
//				map.put("msg", "儲存失敗，資料不存在");
//				map.put("target", "edit_p");
				map = func_list_Dao.updateFail_PK(null, pkmap, "儲存失敗，資料不存在");
				return map;
			}
			oldmap = BeanUtils.describe(po);
			BeanUtils.populate(po, formMap);
			po.setTCH_FUNC(formMap.get("TCH_FUNC") == null? "N" : (String)formMap.get("TCH_FUNC"));
			po.setBANK_FUNC(formMap.get("BANK_FUNC") == null? "N" : (String)formMap.get("BANK_FUNC"));
			po.setCOMPANY_FUNC(formMap.get("COMPANY_FUNC") == null? "N" : (String)formMap.get("COMPANY_FUNC"));
			po.setIS_USED(formMap.get("IS_USED") == null? "N" : (String)formMap.get("IS_USED"));
			po.setUSE_IKEY(formMap.get("USE_IKEY") == null? "N" : (String)formMap.get("USE_IKEY"));
			po.setUDATE(zDateHandler.getTheDateII());
			
			//若為作業模組，則需檢查是否啟用，若停用，則需停用所有子項目
			List<EACH_FUNC_LIST> subList = null;
			if(po.getFUNC_TYPE().equals("1") && po.getIS_USED().equals("N")){
				subList = func_list_Dao.getNextSubItem(po.getFUNC_ID());
			}
			newmap = BeanUtils.describe(po);
			if(func_list_Dao.saveData(po, subList)){
				map.put("result", "TRUE");
				map.put("msg", "儲存成功");
				map.put("target", "search");
				result = true;
			}else{
				map.put("result", "FALSE");
				map.put("msg", "儲存失敗");
				map.put("target", "edit_p");
				result = false;
			}
		}catch (Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();
			map.put("result", "ERROR");
			map.put("msg", "儲存失敗，系統異常:"+e);
			map.put("target", "edit_p");
			func_list_Dao.updateFail_GE(po, oldmap, pkmap, "儲存失敗，系統異常");
			return map;
		}finally{
			if(result){
				userlog_bo.updateLog("B", "儲存成功", oldmap, newmap, pkmap);
			}
		}
		System.out.println("save.map"+map);
		return map;
	}
	
	public Map<String, String> delete(String funcId){
		boolean result = false;
		Map rtnMap = null;
		Map pkmap = new HashMap<>();
		Map oldmap = new HashMap<>();
		EACH_FUNC_LIST po = null;
		List<EACH_FUNC_LIST> subList = null;
		try{
			rtnMap = new HashMap();
			pkmap.put("FUNC_ID", funcId);
			po = func_list_Dao.get(funcId);
			if(po == null){
				rtnMap.put("result", "FALSE");
				rtnMap.put("msg", "刪除失敗，無此資料");
				func_list_Dao.removeFail_PK(po, pkmap, "刪除失敗，無此資料");
				return rtnMap;
			}
			
			//若刪除作業模組，則需清空其所有子項目的「上層所屬作業 UP_FUNC_ID」欄位
			if(po.getFUNC_TYPE().equals("1")){
				subList = func_list_Dao.getNextSubItem(funcId);
			}
			oldmap = BeanUtils.describe(po);
			func_list_Dao.delData(po, subList);
			rtnMap.put("result", "TRUE");
			rtnMap.put("msg", "刪除成功");
			result = true;
		}catch(Exception e){
			e.printStackTrace();
			rtnMap.put("result", "FALSE");
			rtnMap.put("msg", "刪除失敗:"+e.toString());
			func_list_Dao.removeFail_GE(po, pkmap, "刪除失敗");
		}finally{
			if(result){
				userlog_bo.deleteLog("D", "刪除成功", oldmap, pkmap);
				if(subList!=null){
					deleteLog(subList);
				}
			}
		}
		return rtnMap;
	}

	public void deleteLog(List<EACH_FUNC_LIST> subList ){
		Map pkmap = new HashMap<>();
		try {
			for(EACH_FUNC_LIST po :subList){
				pkmap = new HashMap<>();
				Map oldmap = BeanUtils.describe(po);
				pkmap.put("FUNC_ID", po.getFUNC_ID());
				userlog_bo.deleteLog("D", "刪除成功", oldmap, pkmap);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public EACH_FUNC_LIST_Dao getFunc_list_Dao() {
		return func_list_Dao;
	}

	public void setFunc_list_Dao(EACH_FUNC_LIST_Dao func_list_Dao) {
		this.func_list_Dao = func_list_Dao;
	}

	public EACH_USERLOG_BO getUserlog_bo() {
		return userlog_bo;
	}

	public void setUserlog_bo(EACH_USERLOG_BO userlog_bo) {
		this.userlog_bo = userlog_bo;
	}
	
	
	
}
