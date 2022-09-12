package tw.org.twntch.bo;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.struts.util.LabelValueBean;

import tw.org.twntch.db.dao.hibernate.EACH_FUNC_LIST_Dao;
import tw.org.twntch.db.dao.hibernate.EACH_ROLE_LIST_Dao;
import tw.org.twntch.db.dao.hibernate.EACH_USERLOG_Dao;
import tw.org.twntch.db.dao.hibernate.EACH_USER_Dao;
import tw.org.twntch.po.EACH_FUNC_LIST;
import tw.org.twntch.po.EACH_ROLE_FUNC;
import tw.org.twntch.po.EACH_ROLE_FUNC_PK;
import tw.org.twntch.po.EACH_ROLE_LIST;
import tw.org.twntch.po.EACH_USER;
import tw.org.twntch.po.EACH_USERLOG;
import tw.org.twntch.util.BeanUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.SpringAppCtxHelper;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.zDateHandler;

public class EACH_ROLE_LIST_BO {
	
	private EACH_ROLE_LIST_Dao role_list_Dao;
	private EACH_FUNC_LIST_Dao func_list_Dao;
	private EACH_USER_Dao each_user_Dao ;
	private EACH_USERLOG_BO userlog_bo ;
	EACH_USERLOG userlog_po = null;
	private EACH_USERLOG_Dao userLog_Dao ;
	
	public String deleteCheck(Map<String,String> param){
		String json = "";
		String role_id = StrUtils.isEmpty(param.get("role_id")) ? "" :param.get("role_id");
		Map<String,String> map = new HashMap<String,String>();
		try {
			if(checkRoleUser(role_id)){
				map.put("result", "FALSE");
				map.put("msg", "此群組尚有使用者，不可刪除");
			}else{
				map.put("result", "TRUE");
			}
			json = JSONUtils.toJson(map);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("deleteCheck.Exception>>"+e);
		}
		System.out.println("deleteCheck.json>>"+json);
		return json;
	}
	
	/**
	 * 檢查該群組是否有user
	 * @return
	 */
	public boolean checkRoleUser(String roleid){
		boolean result = false ; 
		String hql = " FROM tw.org.twntch.po.EACH_USER WHERE ROLE_ID = ? ";
		List<EACH_USER> list = each_user_Dao.find(hql, roleid);
		if(list != null && list.size() !=0){
			result = true ;
		}
		return result;
	}
	
	public Map<String,String> update(String id ,List<String> selected_MList ,List<String> selected_SList  ,Map formMap){
		boolean save_reault = false;
		boolean is_change = false;
		boolean is_change_priv = false;
		boolean is_change_func = false;
		Map<String, String> map = null;
		Map<String, String> oldmap = null;
		Map<String, String> newmap = null;
		Map<String, String> pkmap = new HashMap<String,String>();
		Map<String, String> msgMap = new HashMap<String,String>();
		EACH_ROLE_LIST po = null;
		EACH_ROLE_FUNC role_func = null;
		EACH_ROLE_FUNC_PK role_func_pk = null;
		List<EACH_ROLE_FUNC> role_funcs = new LinkedList<EACH_ROLE_FUNC>();
		
		Map<String,String> map_auths = new HashMap();
		Map<String,String> old_map_auths = new HashMap();
		Map<String,String> updated_map_auths= new HashMap();
		//紀錄舊有menu(checkbox)
		List<String> old_selected_MFuncs = new ArrayList();
		//紀錄舊有subItem(checkbox)
		List<String> old_selected_SFuncs = new ArrayList();
		
		
		List<String> menu_remove = new ArrayList();
		List<String> subItem_remove = new ArrayList();
		
		List<String> turn_false_M = new ArrayList();
		List<String> turn_true_M = new ArrayList();
		List<String> turn_false_S = new ArrayList();
		List<String> turn_true_S = new ArrayList();
		
		try {
			map = new HashMap<String, String>();
			pkmap.put("ROLE_ID", id);
			po = role_list_Dao.get(id);
			oldmap = BeanUtils.describe(po);
			if(po == null ){
				map.put("result", "FALSE");
				map.put("msg", "更新失敗，查無資料");
				map.put("target", "edit_p");
				msgMap.put("msg", "更新失敗，查無資料");
				userlog_bo.writeFailLog("", msgMap, oldmap, newmap, pkmap);
				return map;
			}
			po = new EACH_ROLE_LIST();
			BeanUtils.populate(po, formMap);
			po.setCDATE(zDateHandler.getTheDateII());
			//紀錄選項(radio)
			map_auths= JSONUtils.json2map(formMap.get("selected_SAuth").toString());
			
			//記錄舊的選項(radio)
			old_map_auths= JSONUtils.json2map(formMap.get("old_selected_SAuth").toString());
			
			//紀錄有更新的選項(radio)
			updated_map_auths= new HashMap();
			System.out.println("map_auths ="+formMap.get("selected_SAuth").toString());
			System.out.println("old_map_auths ="+formMap.get("old_selected_SAuth").toString());
			//比對新舊選項(radio)
			for (String key : map_auths.keySet()) {
				for (String key2 : old_map_auths.keySet()) {
					if(key.equals(key2)){
						if(!( map_auths.get(key) ).equals(old_map_auths.get(key2))){
							System.out.println("different key ="+key+", "+map_auths.get(key));
							updated_map_auths.put(key, map_auths.get(key));
							is_change_priv = true;
						}
					}
				}
	        }
			
			//記錄新舊menu、subItem
			String _selected_MFuncs = (String) formMap.get("old_selected_MFuncs");
			String _selected_SFuncs = (String) formMap.get("old_selected_SFuncs");
			System.out.println("old_selected_MFuncs ="+_selected_MFuncs);
			System.out.println("old_selected_SFuncs ="+_selected_SFuncs);
			
			_selected_MFuncs = _selected_MFuncs.replace("[", "").replace("]", "");
			_selected_SFuncs = _selected_SFuncs.replace("[", "").replace("]", "");
			
			String []array_MFuncs = _selected_MFuncs.split(", ");
			String []array_SFuncs = _selected_SFuncs.split(", ");
			
			for(String s1:array_MFuncs){
				old_selected_MFuncs.add(s1);
			}
			turn_false_M = old_selected_MFuncs;
			turn_true_M = new ArrayList(selected_MList);
			
			
			for(String s2:array_SFuncs){
				old_selected_SFuncs.add(s2);
			}	
			turn_false_S = old_selected_SFuncs;
			turn_true_S = new ArrayList(selected_SList);
			
			//比對出新舊menu
			for(String func_id : selected_MList){
				for(String old_func_id : old_selected_MFuncs){
					if(func_id.equals(old_func_id)){
						menu_remove.add(func_id);
					}
				}
			}
			
			//比對出新舊subItem
			for(String func_id : selected_SList){
				for(String old_func_id : old_selected_SFuncs){
					if(func_id.equals(old_func_id)){
						subItem_remove.add(func_id);
					}
				}
			}
			
			//移除menu重複的值(沒有更動的選項)
			if(menu_remove.size() > 0){
				for(String str : menu_remove){
					for(int i=0; i<turn_false_M.size(); i++){
						if(str.equals(turn_false_M.get(i))){
							turn_false_M.remove(i);
						}
					}
				}
				for(String str : menu_remove){
					for(int i=0; i<turn_true_M.size(); i++){
						if(str.equals(turn_true_M.get(i))){
							turn_true_M.remove(i);
						}
					}
				}
				
			}
			//移除subItem重複的值(沒有更動的選項)
			if(subItem_remove.size() > 0){
				for(String str : subItem_remove){
					for(int i=0; i<turn_false_S.size(); i++){
						if(str.equals(turn_false_S.get(i))){
							turn_false_S.remove(i);
						}
					}
				}
				if(turn_false_S.size()>0){
					
				}
				
				for(String str : subItem_remove){
					for(int i=0; i<turn_true_S.size(); i++){
						if(str.equals(turn_true_S.get(i))){
							turn_true_S.remove(i);
						}
					}
				}
				
			}
			
			//功能選項checkbox有異動
			if(turn_false_M.size()>0 || turn_true_M.size()>0 || turn_false_S.size()>0 || turn_true_S.size()>0){
				is_change_func = true;
			}
			
			
//			menu
			for(String func_id : selected_MList){
				role_func_pk = new EACH_ROLE_FUNC_PK(func_id, po.getROLE_ID());
				role_func = new EACH_ROLE_FUNC();
				role_func.setId(role_func_pk);
				role_func.setAUTH_TYPE("A");//menu 都是A
				role_func.setCDATE(zDateHandler.getTheDateII());
				role_funcs.add(role_func);
				System.out.println("selected_MList ="+func_id);
			}
//			subItem
			for(String func_id : selected_SList){
				role_func_pk = new EACH_ROLE_FUNC_PK(func_id, po.getROLE_ID());
				role_func = new EACH_ROLE_FUNC();
				role_func.setId(role_func_pk);
				role_func.setAUTH_TYPE(map_auths.get(func_id));
				role_func.setCDATE(zDateHandler.getTheDateII());
				role_funcs.add(role_func);
				System.out.println("selected_SList ="+func_id+": "+map_auths.get(func_id));
			}
			newmap = BeanUtils.describe(po); 
			
			//基本資料有異動(群組名稱、群組說明、是否使用Ikey)
			for(String old_str:oldmap.keySet()){
				for(String new_str:newmap.keySet()){
					if(old_str.equals("ROLE_NAME") && new_str.equals("ROLE_NAME")){
						if(!oldmap.get(old_str).equals(newmap.get(new_str))){
							is_change = true;
						}
					}
					if(old_str.equals("USE_IKEY") && new_str.equals("USE_IKEY")){
						if(!oldmap.get(old_str).equals(newmap.get(new_str))){
							is_change = true;
						}
					}
					if(old_str.equals("DESC") && new_str.equals("DESC")){
						if(!oldmap.get(old_str).equals(newmap.get(new_str))){
							is_change = true;
						}
					}
				}
			}
			
			if(role_list_Dao.saveData(po, role_funcs)){
				map.put("result", "TRUE");
				map.put("msg", "儲存成功");
				map.put("target", "search");
				save_reault = true;
			}else{
				map.put("result", "FALSE");
				map.put("msg", "儲存失敗");
				map.put("target", "add_p");
				msgMap.put("msg", "儲存失敗");
				userlog_bo.writeFailLog("", msgMap, oldmap, newmap, pkmap);
				save_reault = false;
			}
		}catch (Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();
			map.put("result", "ERROR");
			map.put("msg", "更新失敗，系統異常:"+e);
			map.put("target", "edit_p");
			msgMap.put("msg", "更新失敗，系統異常:");
			userlog_bo.writeFailLog("", msgMap, oldmap, newmap, pkmap);
			save_reault = false;
			return map;
		}finally{
			if(save_reault){
				//基本資料變更紀錄(群組名稱、群組說明、是否使用Ikey)
				if(is_change){
					updateLog(role_funcs, oldmap, newmap, pkmap);
				}
				
				//更新功能權限選項(radio)
				if(is_change_priv){
					updateLog_priv(role_funcs, old_map_auths, map_auths, pkmap, updated_map_auths);
				}
				
				//更新功能項目(checkbox)
				if(is_change_func){
					update_log_func(role_funcs , pkmap, turn_false_M, turn_true_M, turn_false_S, turn_true_S);
				}
			}
		}
		return map;
	}
	
    //紀錄基本資料變更紀錄(群組名稱、群組說明、是否使用Ikey)
	public void updateLog(List<EACH_ROLE_FUNC> role_funcs ,Map<String,String> oldmap,  Map newmap , Map pkmap){
		userlog_bo.writeLog("B", oldmap, newmap, pkmap);
	}
	
	//紀錄權限radio變更紀錄(每一筆異動分別記錄)
	public void updateLog_priv(List<EACH_ROLE_FUNC> role_funcs ,Map<String,String> oldmap,  Map newmap , Map pkmap, Map<String,String> updated_map_auths){
		try {
//			userlog_bo.writeLog("B", oldmap, newmap, pkmap);
			//有更新的項目
			for(String key : updated_map_auths.keySet()){
				//舊的選項
				for(String key2 : oldmap.keySet()){
					if(key.equals(key2)){
						String priv = oldmap.get(key2);if(priv.equals("R")){priv="查詢";}else{priv="異動";}
						String new_priv = updated_map_auths.get(key);if(new_priv.equals("R")){new_priv="查詢";}else{new_priv="異動";}
						
						writeLog(key, pkmap, priv, new_priv);
					}
				}
			}
			

//			newmap.clear();
//			for(EACH_ROLE_FUNC tmp :role_funcs){
//				newmap.putAll(BeanUtils.describe(tmp.getId())); 
//				newmap.putAll(BeanUtils.describe(tmp)); 
//				userlog_bo.writeLog("A", oldmap, newmap, pkmap);
//			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//更新log(功能項目checkbox)(每一筆異動分別記錄)
	public void update_log_func(List<EACH_ROLE_FUNC> role_funcs , Map pkmap, List<String> turn_false_M, List<String> turn_true_M, List<String> turn_false_S, List<String> turn_true_S){
		try {
			if(turn_false_M.size()>0){
				for(String str:turn_false_M){
					writeLog(str, pkmap, "開啟", "關閉");
				}
			}
			
			if(turn_true_M.size()>0){
				for(String str:turn_true_M){
					writeLog(str, pkmap, "關閉", "開啟");
				}
			}
			
			if(turn_false_S.size()>0){
				for(String str:turn_false_S){
					writeLog(str, pkmap, "開啟", "關閉");
				}
			}
			
			if(turn_true_S.size()>0){
				for(String str:turn_true_S){
					writeLog(str, pkmap, "關閉", "開啟");
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void writeLog(String funcId, Map pkmap, String old_priv, String new_priv){
		//功能項目名稱
		String func_name = func_list_Dao.getData(funcId).get(0).getFUNC_NAME();				
		userlog_po = userlog_bo.getUSERLOG("B","");
		userlog_po.setADEXCODE("修改-儲存成功，PK={群組代號="+pkmap.get("ROLE_ID")+"}");
		userlog_po.setBFCHCON("{功能名稱="+func_name+", 功能權限="+old_priv+"}");
    	userlog_po.setAFCHCON("{功能名稱="+func_name+", 功能權限="+new_priv+"}");
        userLog_Dao = SpringAppCtxHelper.getBean("userLog_Dao");
    	
    	userLog_Dao.aop_save(userlog_po);
	}
	
	public Map<String,String> delete(String id ){
		boolean result = false;
		EACH_ROLE_LIST po = null;
		Map<String, String> map = null;
		Map<String, String> pkmap = new HashMap<String, String>();
		Map<String, String> oldmap = new HashMap<String, String>();
		try {
			map = new HashMap<String, String>();
			po = role_list_Dao.get(id);
			oldmap = BeanUtils.describe(po);
			if(po == null ){
//				map.put("result", "FALSE");
//				map.put("msg", "刪除失敗，查無資料");
//				map.put("target", "edit_p");
				map = role_list_Dao.removeFail_PK(po, pkmap, "刪除失敗，查無資料");
				return map;
			}
			if(role_list_Dao.deleteRole_FuncByRole_Id(id)){
				map.put("result", "TRUE");
				map.put("msg", "刪除成功");
				map.put("target", "search");
				result = true;
			}else{
//				map.put("result", "FALSE");
//				map.put("msg", "刪除失敗");
//				map.put("target", "edit_p");
				map = role_list_Dao.removeFail_GE(po, pkmap, "刪除失敗");
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
//			map.put("result", "ERROR");
//			map.put("msg", "刪除失敗，系統異常:"+e);
//			map.put("target", "edit_p");
			map = role_list_Dao.removeFail_GE(po, pkmap, "刪除失敗，系統異常:"+e);
			result = false;
			return map;
		}finally{
			userlog_bo.writeLog("D", oldmap, null, pkmap);
		}
		return map;
	}
	
	
	public Map<String,String> save(String id ,List<String> selected_MList ,List<String> selected_SList  ,Map formMap){
		boolean result = false;
		Map<String, String> pkmap = new HashMap<String, String>();
		Map<String, String> newmap = new HashMap<String, String>();
		Map<String, String> map = null;
		EACH_ROLE_LIST po = null;
		EACH_ROLE_FUNC role_func = null;
		EACH_ROLE_FUNC_PK role_func_pk = null;
		List<EACH_ROLE_FUNC> role_funcs = new LinkedList<EACH_ROLE_FUNC>();
		try {
			map = new HashMap<String, String>();
			po  = role_list_Dao.get(id);
			pkmap = BeanUtils.describe(po);
			if(po != null){
				map.put("result", "FALSE");
				map.put("msg", "儲存失敗，已有資料");
				map.put("target", "add_p");
				role_list_Dao.saveFail_PK(po, pkmap, "儲存失敗，已有資料");
				return map;
			}
			po = new EACH_ROLE_LIST();
			BeanUtils.populate(po, formMap);
			po.setCDATE(zDateHandler.getTheDateII());
			newmap = BeanUtils.describe(po);
			Map<String,String> map_auths= JSONUtils.json2map(formMap.get("selected_SAuth").toString());
//			menu
			for(String func_id : selected_MList){
				role_func_pk = new EACH_ROLE_FUNC_PK(func_id, po.getROLE_ID());
				role_func = new EACH_ROLE_FUNC();
				role_func.setId(role_func_pk);
				role_func.setAUTH_TYPE("A");//menu 都是A
				role_func.setCDATE(zDateHandler.getTheDateII());
				role_funcs.add(role_func);
			}
//			subItem
			for(String func_id : selected_SList){
				role_func_pk = new EACH_ROLE_FUNC_PK(func_id, po.getROLE_ID());
				role_func = new EACH_ROLE_FUNC();
				role_func.setId(role_func_pk);
				role_func.setAUTH_TYPE(map_auths.get(func_id));
				role_func.setCDATE(zDateHandler.getTheDateII());
				role_funcs.add(role_func);
			}
			
			if(role_list_Dao.saveData(po, role_funcs)){
				map.put("result", "TRUE");
				map.put("msg", "儲存成功");
				map.put("target", "search");
				result = true;
			}else{
//				map.put("result", "FALSE");
//				map.put("msg", "儲存失敗");
//				map.put("target", "add_p");
				result = false;
				map = role_list_Dao.saveFail_GE(po, "儲存失敗");
			}
		}catch (Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();
//			map.put("result", "ERROR");
//			map.put("msg", "儲存失敗，系統異常:"+e);
//			map.put("target", "add_p");
			map = role_list_Dao.saveFail_GE(po, "儲存失敗，系統異常");
			return map;
		}finally{
			if(result){
				addLog(role_funcs, null, newmap, pkmap);
			}
		}
		
		System.out.println("save.map"+map);
		return map;
	}
	
	public void addLog(List<EACH_ROLE_FUNC> role_funcs ,Map oldmap ,  Map newmap , Map pkmap){
		try {
			userlog_bo.writeLog("A", oldmap, newmap, pkmap);
			newmap.clear();
			for(EACH_ROLE_FUNC tmp :role_funcs){
				newmap.putAll(BeanUtils.describe(tmp.getId())); 
				newmap.putAll(BeanUtils.describe(tmp)); 
				
				String func_id = (String) newmap.get("FUNC_ID");
				//功能項目名稱
				String func_name = func_list_Dao.getData(func_id).get(0).getFUNC_NAME();
				newmap.put("FUNC_NAME", func_name);
				
				userlog_bo.writeLog("A", oldmap, newmap, pkmap);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public String search_toJson(Map<String, String> param){
		String id =StrUtils.isNotEmpty(param.get("ROLE_ID"))? param.get("ROLE_ID"):"";
//		String role_Type =StrUtils.isNotEmpty(param.get("ROLE_TYPE"))? param.get("ROLE_TYPE"):"";
		String role_Type =StrUtils.isNotEmpty(param.get("ROLE_TYPE"))? param.get("ROLE_TYPE"):"";
		if(StrUtils.isEmpty(role_Type)){
			return "{}";
		}
		String sord =StrUtils.isNotEmpty(param.get("sord"))? param.get("sord"):"";
		String sidx =StrUtils.isNotEmpty(param.get("sidx"))? param.get("sidx"):"";
		String orderSQL = StrUtils.isNotEmpty(sidx)? " ORDER BY "+sidx+" "+sord:"";
		return JSONUtils.toJson(search(id , role_Type , orderSQL));
	}
	public String search_toJson4Bank(Map<String, String> param){
		String json = "";
		String id =StrUtils.isNotEmpty(param.get("ROLE_ID"))? param.get("ROLE_ID"):"";
		String role_Type ="B";
		String sord =StrUtils.isNotEmpty(param.get("sord"))? param.get("sord"):"";
		String sidx =StrUtils.isNotEmpty(param.get("sidx"))? param.get("sidx"):"";
		String orderSQL = StrUtils.isNotEmpty(sidx)? " ORDER BY "+sidx+" "+sord:"";
		List<EACH_ROLE_LIST> list = null;
		try {
			list = search(id , role_Type , orderSQL);
			Map<String,String> map = new HashMap<String,String>();
			userlog_bo.writeLog("C", null, null, param);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
//			userlog_bo.geFailLog("C", "失敗", null, null, param);
		}
		
		json = JSONUtils.toJson(list);
		return  json;
	}
	
	/**
	 * func_type =1 (Menu)  
	 * func_type =2 (SubItem)  
	 * @param role_id
	 * @param func_type
	 * @return
	 */
	public String[] getSelectedItem(String role_id ,String func_type){
		String[] ary = null;
		List<EACH_FUNC_LIST> list = func_list_Dao.getSelectedItem(role_id, func_type);
		ary = new String[list.size()];
		for(EACH_FUNC_LIST po :list){
			ary[list.indexOf(po)] = po.getFUNC_ID();
		}
		for(String ss :Arrays.asList(ary) ){
			System.out.println("1ary.ss>>"+ss);
		}
		System.out.println("ary>>"+ary);
		System.out.println("arylength>>"+ary.length);
		return ary;
	}
	/**
	 * func_type =1 (Menu)  
	 * func_type =2 (SubItem)  
	 * @param role_id
	 * @param func_type
	 * @return
	 */
	public String[] getSelectedSAuth(String role_id ,String func_type){
		String[] ary = null;
		List<EACH_FUNC_LIST> list = func_list_Dao.getSelectedItem(role_id, func_type);
		ary = new String[list.size()];
		String key = "";
		String mapStr = "";
		for(EACH_FUNC_LIST po :list){
			key = po.getUP_FUNC_ID()+"_"+po.getFUNC_ID();
			mapStr="{\""+key+"\":\""+po.getAUTH_TYPE()+"\"}";
			ary[list.indexOf(po)] = mapStr;
		}
		for(String ss :Arrays.asList(ary) ){
			System.out.println("2ary.ss>>"+ss);
		}
		System.out.println("ary>>"+ary);
		System.out.println("arylength>>"+ary.length);
		return ary;
	}
	
	public String getSelectedSAuth2(String role_id ,String func_type){
		List<EACH_FUNC_LIST> list = func_list_Dao.getSelectedItem(role_id, func_type);
		String key = "";
		String mapStr = "";
		for(EACH_FUNC_LIST po :list){
			key = po.getFUNC_ID();
			if( !(list.get(list.size()-1).toString()).equals(po.toString()) ){
				mapStr+="\""+key+"\":\""+po.getAUTH_TYPE()+"\""+",";
			}else{
				mapStr+="\""+key+"\":\""+po.getAUTH_TYPE()+"\"";
			}
			
		}
		mapStr ="{"+mapStr+"}";
		System.out.println("mapStr>>"+mapStr);
		return mapStr;
	}
	
	public List<EACH_ROLE_LIST> search(String id , String role_Type){
		
		List<EACH_ROLE_LIST> list = null ;
		try {
			if(StrUtils.isEmpty(id) && StrUtils.isEmpty(role_Type)){
				list = role_list_Dao.getAll();
			}else{
				StringBuffer sql = new StringBuffer();
				sql.append(" FROM tw.org.twntch.po.EACH_ROLE_LIST WHERE ");
				Map<String,String> map = new HashMap<String,String>();
				List<String> strList = new LinkedList<String>();
				map.put("ROLE_ID", id.trim());
				map.put("ROLE_TYPE", role_Type.trim());
				int i =0;
				for(String key:map.keySet()){
					if(StrUtils.isNotEmpty(map.get(key))){
						if(i!=0){
							sql.append(" AND ");
						}
						sql.append(key+" = ?");
						if(map.get("ROLE_TYPE").equals("B")){
							sql.append(" OR ROLE_TYPE = 'C' ");
						}
						strList.add(map.get(key));
						i++;
					}
				}//for end
				list = role_list_Dao.find(sql.toString(), strList.toArray());
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("list>>"+list);
		list = list.size() == 0 ? null : list;
		return list;
	}
	public List<EACH_ROLE_LIST> search(String id , String role_Type , String orderSQL){
		
		List<EACH_ROLE_LIST> list = null ;
		try {
			if(StrUtils.isEmpty(id) && StrUtils.isEmpty(role_Type)){
				list = role_list_Dao.getAll();
			}else{
				StringBuffer sql = new StringBuffer();
				sql.append(" FROM tw.org.twntch.po.EACH_ROLE_LIST WHERE ");
				Map<String,String> map = new HashMap<String,String>();
				List<String> strList = new LinkedList<String>();
				map.put("ROLE_ID", id.trim());
				map.put("ROLE_TYPE", role_Type.trim());
				int i =0;
				for(String key:map.keySet()){
					if(StrUtils.isNotEmpty(map.get(key))){
						if(i!=0){
							sql.append(" AND ");
						}
						sql.append(key+" = ?");
						if(map.get("ROLE_TYPE").equals("B")){
							sql.append(" OR ROLE_TYPE = 'C' ");
						}
						strList.add(map.get(key));
						i++;
					}
				}//for end
				sql.append(orderSQL);
				System.out.println("SQL>>"+sql);
				list = role_list_Dao.find(sql.toString(), strList.toArray());
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("list>>"+list);
		list = list.size() == 0 ? null : list;
		return list;
	}
	
	public List<Map> getMenuFuncList(String role_type){
//		List<EACH_FUNC_LIST> list = func_list_Dao.getFuncItemByType("1");
		StringBuffer pathSql = new StringBuffer();
		role_type= StrUtils.isEmpty(role_type) ? "" :role_type;
		//票交所
		if(role_type.equals("A")){
			pathSql.append(" AND TCH_FUNC = 'Y'");
		//銀行端
		}else if(role_type.equals("B")){
			pathSql.append(" AND BANK_FUNC = 'Y'");
		//發動者
		}else if(role_type.equals("C")){
			pathSql.append(" AND COMPANY_FUNC = 'Y'");
		}
		List<EACH_FUNC_LIST> list = func_list_Dao.getFuncItemByTypes("1" ,pathSql.toString() );
		List<Map> beanList = new LinkedList<Map>();
		Map map = null;
		for(EACH_FUNC_LIST po :list){
			map = new HashMap<>();
			map.put("label",po.getFUNC_NAME());
			map.put("value",po.getFUNC_ID());
			map.put("is_used",po.getIS_USED());
			beanList.add(map);
		}
		return beanList;
	}
//	public List<LabelValueBean> getMenuFuncList(){
//		List<EACH_FUNC_LIST> list = func_list_Dao.getFuncItemByType("1");
//		List<LabelValueBean> beanList = new LinkedList<LabelValueBean>();
//		LabelValueBean bean = null;
//		for(EACH_FUNC_LIST po :list){
//			bean = new LabelValueBean(po.getFUNC_NAME(), po.getFUNC_ID());
//			beanList.add(bean);
//		}
//		return beanList;
//	}
	public List<Map> getSubItemFuncList(String role_type){
//		List<EACH_FUNC_LIST> list = func_list_Dao.getAllSubItem();
		StringBuffer pathSql = new StringBuffer();
		role_type= StrUtils.isEmpty(role_type) ? "" :role_type;
		//票交所
		if(role_type.equals("A")){
			pathSql.append(" WHERE S.TCH_FUNC = 'Y'");
		//銀行端
		}else if(role_type.equals("B")){
			pathSql.append(" WHERE S.BANK_FUNC = 'Y'");
		//發動者
		}else if(role_type.equals("C")){
			pathSql.append(" WHERE S.COMPANY_FUNC = 'Y'");
		}
		List<EACH_FUNC_LIST> list = func_list_Dao.getAllSubItem(pathSql.toString());
		List<Map> beanList = new LinkedList<Map>();
		Map map = null;
		for(EACH_FUNC_LIST po :list){
			map = new HashMap<>();
			map.put("label", po.getFUNC_NAME());
			map.put("value", po.getFUNC_ID());
			map.put("htmlId", po.getUP_FUNC_ID()+"_"+po.getFUNC_ID());
			map.put("is_used",po.getIS_USED());
			beanList.add(map);
		}
		return beanList;
	}
	public List<Map> getSubItemFuncListII(String role_type){
//		List<EACH_FUNC_LIST> list = func_list_Dao.getAllSubItem();
		StringBuffer pathSql = new StringBuffer();
		role_type= StrUtils.isEmpty(role_type) ? "" :role_type;
		//票交所
		if(role_type.equals("A")){
			pathSql.append(" WHERE S.TCH_FUNC = 'Y'");
//			pathSql.append(" WHERE M.TCH_FUNC = 'Y'");
		//銀行端
		}else if(role_type.equals("B")){
			pathSql.append(" WHERE S.BANK_FUNC = 'Y'");
//			pathSql.append(" WHERE M.BANK_FUNC = 'Y'");
		//發動者
		}else if(role_type.equals("C")){
			pathSql.append(" WHERE S.COMPANY_FUNC = 'Y'");
//			pathSql.append(" WHERE M.COMPANY_FUNC = 'Y'");
		}
		pathSql.append(" AND M.FUNC_TYPE='1' ");
		List<EACH_FUNC_LIST> list = func_list_Dao.getAllSubItem(pathSql.toString());
		List<Map> beanList = new LinkedList<Map>();
		List<Map> subList = new LinkedList<Map>();
		String tmp = "";
		String tmpName = "";
		Map map = null;
		Map keymap = null;
		int i =0;
		for(EACH_FUNC_LIST po :list){
			map = new HashMap<>();
			System.out.println("tmp"+tmp+",UP_FUNC_ID>>"+po.getUP_FUNC_ID());
			if(i==0){
				tmp = po.getUP_FUNC_ID();
				tmpName = po.getUP_FUNC_NAME();
			}
			
			if(!tmp.equals(po.getUP_FUNC_ID())){
				System.out.println("hi");
				System.out.println(beanList);
				keymap = new HashMap<>();
				keymap.put(tmpName, beanList);
				subList.add(keymap);
				tmp = po.getUP_FUNC_ID();
				tmpName = po.getUP_FUNC_NAME();
				beanList = new LinkedList<Map>();
			}
			
			
			map.put("label", po.getFUNC_NAME());
			map.put("value", po.getFUNC_ID());
			map.put("htmlId", po.getHtmlId());
			map.put("ulName", po.getUP_FUNC_ID());
			map.put("is_used",po.getIS_USED());
			beanList.add(map);
			
			if(i==list.size()-1){
				keymap = new HashMap<>();
				keymap.put(tmpName, beanList);
				subList.add(keymap);
			}
			i++;	
			
		}
		System.out.println("subList>>"+subList);
		return subList;
	}
	
	public List<Map> getSubItemFuncListIII(String role_type){
		StringBuffer pathSql = new StringBuffer();
		role_type= StrUtils.isEmpty(role_type) ? "" :role_type;
		//票交所
		if(role_type.equals("A")){
//			pathSql.append(" WHERE S.TCH_FUNC = 'Y'");
			pathSql.append(" WHERE S.TCH_FUNC != 'N'");
			//銀行端
		}else if(role_type.equals("B")){
//			pathSql.append(" WHERE S.BANK_FUNC = 'Y'");
			pathSql.append(" WHERE S.BANK_FUNC != 'N'");
			//發動者
		}else if(role_type.equals("C")){
//			pathSql.append(" WHERE S.COMPANY_FUNC = 'Y'");
			pathSql.append(" WHERE S.COMPANY_FUNC != 'N'");
		}
		pathSql.append(" AND M.FUNC_TYPE='1' ");
		List<EACH_FUNC_LIST> list = func_list_Dao.getAllSubItem(pathSql.toString());
		List<Map> beanList = new LinkedList<Map>();
		List<Map> subList = new LinkedList<Map>();
		String tmp = "";
		String tmpName = "";
		Map map = null;
		Map keymap = null;
		int i =0;
		for(EACH_FUNC_LIST po :list){
			map = new HashMap<>();
			System.out.println("tmp"+tmp+",UP_FUNC_ID>>"+po.getUP_FUNC_ID());
			if(i==0){
				tmp = po.getUP_FUNC_ID();
				tmpName = po.getUP_FUNC_NAME();
			}
			
			if(!tmp.equals(po.getUP_FUNC_ID())){
				System.out.println("hi");
				System.out.println(beanList);
				keymap = new HashMap<>();
//				keymap.put(tmpName, beanList);
				keymap.put("subItem", beanList);
				keymap.put("uplabel", tmpName);
				keymap.put("upvalue", tmp);
				subList.add(keymap);
				tmp = po.getUP_FUNC_ID();
				tmpName = po.getUP_FUNC_NAME();
				beanList = new LinkedList<Map>();
			}
			
			
			map.put("label", po.getFUNC_NAME());
			map.put("value", po.getFUNC_ID());
			map.put("htmlId", po.getHtmlId());
			map.put("ulName", po.getUP_FUNC_ID());
			map.put("is_used",po.getIS_USED());
			beanList.add(map);
			
			if(i==list.size()-1){
				keymap = new HashMap<>();
//				keymap.put(tmpName, beanList);
				keymap.put("subItem", beanList);
				keymap.put("uplabel", tmpName);
				keymap.put("upvalue", tmp);
				subList.add(keymap);
			}
			i++;	
			
		}
		System.out.println("subList>>"+subList);
		return subList;
	}
	
	public List<Map> getSubItemFuncListIV(String role_type, String USE_IKEY){
		StringBuffer pathSql = new StringBuffer();
		role_type= StrUtils.isEmpty(role_type) ? "" :role_type;
		//票交所
		if(role_type.equals("A")){
//			pathSql.append(" WHERE S.TCH_FUNC = 'Y'");
			pathSql.append(" WHERE S.TCH_FUNC != 'N'");
			//銀行端
		}else if(role_type.equals("B")){
//			pathSql.append(" WHERE S.BANK_FUNC = 'Y'");
			pathSql.append(" WHERE S.BANK_FUNC != 'N'");
			//發動者
		}else if(role_type.equals("C")){
//			pathSql.append(" WHERE S.COMPANY_FUNC = 'Y'");
			pathSql.append(" WHERE S.COMPANY_FUNC != 'N'");
		}
		pathSql.append(" AND M.FUNC_TYPE='1' ");
//		pathSql.append(" AND S.USE_IKEY='"+USE_IKEY+"' ");
		//20161027
		if(USE_IKEY.equals("N")){
			pathSql.append(" AND S.USE_IKEY='"+USE_IKEY+"' ");
		}
		
		List<EACH_FUNC_LIST> list = func_list_Dao.getAllSubItem(pathSql.toString());
		List<Map> beanList = new LinkedList<Map>();
		List<Map> subList = new LinkedList<Map>();
		String tmp = "";
		String tmpName = "";
		Map map = null;
		Map keymap = null;
		int i =0;
		for(EACH_FUNC_LIST po :list){
			map = new HashMap<>();
			System.out.println("tmp"+tmp+",UP_FUNC_ID>>"+po.getUP_FUNC_ID());
			if(i==0){
				tmp = po.getUP_FUNC_ID();
				tmpName = po.getUP_FUNC_NAME();
			}
			
			if(!tmp.equals(po.getUP_FUNC_ID())){
				System.out.println("hi");
				System.out.println(beanList);
				keymap = new HashMap<>();
//				keymap.put(tmpName, beanList);
				keymap.put("subItem", beanList);
				keymap.put("uplabel", tmpName);
				keymap.put("upvalue", tmp);
				subList.add(keymap);
				tmp = po.getUP_FUNC_ID();
				tmpName = po.getUP_FUNC_NAME();
				beanList = new LinkedList<Map>();
			}
			
			
			map.put("label", po.getFUNC_NAME());
			map.put("value", po.getFUNC_ID());
			map.put("htmlId", po.getHtmlId());
			map.put("ulName", po.getUP_FUNC_ID());
			map.put("is_used",po.getIS_USED());
			beanList.add(map);
			
			if(i==list.size()-1){
				keymap = new HashMap<>();
//				keymap.put(tmpName, beanList);
				keymap.put("subItem", beanList);
				keymap.put("uplabel", tmpName);
				keymap.put("upvalue", tmp);
				subList.add(keymap);
			}
			i++;	
			
		}
		System.out.println("subList>>"+subList);
		return subList;
	}
	
	public String getSubItemFuncList2Json(Map<String, String> param){
		StringBuffer pathSql = new StringBuffer();
		String json = "";
		String role_type = StrUtils.isEmpty(param.get("role_type")) ? "" :param.get("role_type");
		//票交所
		if(role_type.equals("A")){
			pathSql.append(" WHERE S.TCH_FUNC != 'N'");
			//銀行端
		}else if(role_type.equals("B")){
			pathSql.append(" WHERE S.BANK_FUNC != 'N'");
			//發動者
		}else if(role_type.equals("C")){
			pathSql.append(" WHERE S.COMPANY_FUNC != 'N'");
		}
		pathSql.append(" AND M.FUNC_TYPE='1' ");
		List<EACH_FUNC_LIST> list = func_list_Dao.getAllSubItem(pathSql.toString());
		List<Map> beanList = new LinkedList<Map>();
		List<Map> subList = new LinkedList<Map>();
		String tmp = "";
		String tmpName = "";
		Map map = null;
		Map keymap = null;
		int i =0;
		for(EACH_FUNC_LIST po :list){
			map = new HashMap<>();
			System.out.println("tmp"+tmp+",UP_FUNC_ID>>"+po.getUP_FUNC_ID());
			if(i==0){
				tmp = po.getUP_FUNC_ID();
				tmpName = po.getUP_FUNC_NAME();
			}
			
			if(!tmp.equals(po.getUP_FUNC_ID())){
				System.out.println("hi");
				System.out.println(beanList);
				keymap = new HashMap<>();
//				keymap.put(tmpName, beanList);
				keymap.put("subItem", beanList);
				keymap.put("uplabel", tmpName);
				keymap.put("upvalue", tmp);
				subList.add(keymap);
				tmp = po.getUP_FUNC_ID();
				tmpName = po.getUP_FUNC_NAME();
				beanList = new LinkedList<Map>();
			}
			
			
			map.put("label", po.getFUNC_NAME());
			map.put("value", po.getFUNC_ID());
			map.put("htmlId", po.getHtmlId());
			map.put("ulName", po.getUP_FUNC_ID());
			map.put("is_used",po.getIS_USED());
			beanList.add(map);
			
			if(i==list.size()-1){
				keymap = new HashMap<>();
//				keymap.put(tmpName, beanList);
				keymap.put("subItem", beanList);
				keymap.put("uplabel", tmpName);
				keymap.put("upvalue", tmp);
				subList.add(keymap);
			}
			i++;	
			
		}
		System.out.println("subList>>"+subList);
		json = JSONUtils.toJson(subList);
		return json;
	}
	
	public EACH_ROLE_LIST_Dao getRole_list_Dao() {
		return role_list_Dao;
	}

	public void setRole_list_Dao(EACH_ROLE_LIST_Dao role_list_Dao) {
		this.role_list_Dao = role_list_Dao;
	}

	public EACH_FUNC_LIST_Dao getFunc_list_Dao() {
		return func_list_Dao;
	}

	public void setFunc_list_Dao(EACH_FUNC_LIST_Dao func_list_Dao) {
		this.func_list_Dao = func_list_Dao;
	}

	public EACH_USER_Dao getEach_user_Dao() {
		return each_user_Dao;
	}

	public void setEach_user_Dao(EACH_USER_Dao each_user_Dao) {
		this.each_user_Dao = each_user_Dao;
	}

	public EACH_USERLOG_BO getUserlog_bo() {
		return userlog_bo;
	}

	public void setUserlog_bo(EACH_USERLOG_BO userlog_bo) {
		this.userlog_bo = userlog_bo;
	}
	
	
	
}
