package tw.org.twntch.bo;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts.util.LabelValueBean;

import tw.org.twntch.aop.GenerieAop;
import tw.org.twntch.db.dao.hibernate.EACH_FUNC_LIST_Dao;
import tw.org.twntch.db.dao.hibernate.EACH_USERLOG_Dao;
import tw.org.twntch.db.dao.hibernate.EACH_USER_Dao;
import tw.org.twntch.db.dao.hibernate.Page;
import tw.org.twntch.form.Login_Form;
import tw.org.twntch.po.EACH_FUNC_LIST;
import tw.org.twntch.po.EACH_USER;
import tw.org.twntch.po.EACH_USERLOG;
import tw.org.twntch.po.EACH_USERLOG_PK;
import tw.org.twntch.po.EACH_USER_PK;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.SpringAppCtxHelper;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.WebServletUtils;
import tw.org.twntch.util.zDateHandler;

public class EACH_USERLOG_BO extends GenerieAop {

	private EACH_USER_Dao each_user_Dao;
	private EACH_FUNC_LIST_Dao func_list_Dao;
	private EACH_USERLOG_Dao userLog_Dao;
	private FieldsMap fieldsmap;
	private Logger log = Logger.getLogger(EACH_USERLOG_BO.class.getName());
	/**
	 * op_type 
	 * @param op_type A:新增 B:修改 C:查詢 D:刪除 E:報表列印 F:檔案下載 G:送出 H:重送 I:登入 J:登出 K:檔案上傳
	 * @param oldmap 異動前的資料
	 * @param newmap 異動後的資料
	 * @param pkmap  如型態為C、E、F、k 查詢條件可以使用此參數，但一定要有 action:""
	 * @return
	 */
	public void writeLog(String op_type  , Map oldmap , Map newmap , Map pkmap){
		String adexcode = "";
		String str = "成功";
		String s_is_record = "";
		int key = 0;
		try {
			if(StrUtils.isEmpty(op_type)){
				log.debug(" writeLog op_type is null  do nothing");
				return;
			}
			Login_Form login_form = (Login_Form) WebServletUtils.getRequest().getSession().getAttribute("login_form");
			s_is_record = login_form.getUserData().getS_is_record();
//			op_type =查詢 且該功能設定查詢不紀錄
			if(op_type.equals("C") && !s_is_record.equals("Y")){
				log.debug(" op_type is C(search)  && s_is_record != Y so no log");
				return;
			}
			oldmap = each_user_Dao.mapremove(oldmap);
			newmap = each_user_Dao.mapremove(newmap);
			pkmap = each_user_Dao.mapremove(pkmap);
			Map<String,String> mapTypes = SpringAppCtxHelper.getBean("opt_type");
			log.debug(" writeLog.mapTypes>>"+mapTypes);
			adexcode = mapTypes.get(op_type);
			if(op_type.equals("A") || op_type.equals("K") || op_type.equals("0")){
				adexcode =  adexcode+str;
				addLog(op_type, adexcode, newmap, pkmap);
				return;
			}
			if(op_type.equals("B")){
				adexcode = adexcode+"-儲存"+str;
				updateLog(op_type, adexcode, oldmap, newmap, pkmap);
				return;
			}
			if(op_type.equals("C") || op_type.equals("E") || op_type.equals("F") || op_type.equals("L") || op_type.equals("U")){
				adexcode = adexcode+str;
				searchLog(op_type, adexcode, null, pkmap);
				return;
			}
			
			if(op_type.equals("D")){
				adexcode = adexcode+str;
				deleteLog(op_type, adexcode, oldmap, pkmap);
				return;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	/**
	 * 
	 * @param op_type
	 * @param msgMap
	 * @param oldmap
	 * @param newmap
	 * @param pkmap
	 */
	public void writeFailLog(String op_type,Map msgMap , Map oldmap , Map newmap , Map pkmap){
		String adexcode = "";
		String str = "失敗，";
		try {
			if(StrUtils.isEmpty(op_type)){
				log.debug(" writeLog op_type is null  do nothing");
				return;
			}
			oldmap = each_user_Dao.mapremove(oldmap);
			newmap = each_user_Dao.mapremove(newmap);
			pkmap = each_user_Dao.mapremove(pkmap);
			Map<String,String> mapTypes = SpringAppCtxHelper.getBean("opt_type");
			log.debug(" writeFailLog.mapTypes>>"+mapTypes);
			adexcode = mapTypes.get(op_type);
			if(op_type.equals("A")){
				adexcode =  str + msgMap.get("msg").toString();
			}
			if(op_type.equals("B")){
				adexcode =  str + msgMap.get("msg").toString();
			}
			if(op_type.equals("C") || op_type.equals("E") || op_type.equals("F") || op_type.equals("K") || op_type.equals("U")){
				adexcode = str + msgMap.get("msg").toString();
			}
			if(op_type.equals("D")){
				adexcode = str + msgMap.get("msg").toString();
			}
			
			geFailLog(op_type, adexcode, oldmap, newmap, pkmap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void geFailLog(String op_type ,String adexcode, Map oldmap , Map newmap , Map pkmap){
		String serchStrs = "{}";
		String action = "";
		Map tmpmap = new HashMap<String,String>();
		
		if(op_type.equals("C") || op_type.equals("E") || op_type.equals("F") || op_type.equals("K")){
			serchStrs = (String) (StrUtils.isEmpty(String.valueOf(pkmap.get("serchStrs")) ) ? "{}" :pkmap.get("serchStrs")) ;
			log.debug("geFailLog.serchStrs>>"+serchStrs);
			tmpmap =  JSONUtils.json2map(serchStrs) ;
			action = (String) (tmpmap.get("action") == null ? "" :tmpmap.get("action")) ;
		}
		EACH_USERLOG userlog_po = getUSERLOG(op_type ,action );
		if(op_type.equals("C") || op_type.equals("E") || op_type.equals("F")){
			userlog_po.setAFCHCON("查詢條件 : "+restMapKey2CH(tmpmap));
		}
		System.out.println("oldmap>>"+oldmap);
		System.out.println("restMapKey2CH>>"+restMapKey2CH(oldmap));
		userlog_po.setBFCHCON(restMapKey2CH(oldmap)+"");
		userlog_po.setAFCHCON(restMapKey2CH(newmap)+"");
		userlog_po.setADEXCODE(adexcode);
		userLog_Dao.aop_save(userlog_po);
	}
	
	
	public void searchLog(String op_type  , String adexcode, Map newmap , Map pkmap ){
		String serchStrs = "{}";
		String action = "";
		try {
//			serchStrs = (String) (StrUtils.isEmpty(String.valueOf(pkmap.get("serchStrs")) ) ? "{}" :pkmap.get("serchStrs")) ;
			serchStrs = (String) (StrUtils.isEmpty(pkmap.get("serchStrs")+"" ) ? "{}" :pkmap.get("serchStrs")) ;
			log.debug("searchLog.serchStrs>>"+serchStrs);
			serchStrs = serchStrs.replace("[", "").replace("]", "");
			serchStrs = URLDecoder.decode(serchStrs,"UTF-8");
			Map tmpmap =  JSONUtils.json2map(serchStrs) ;
			action = (String) (tmpmap.get("action") == null ? "" :tmpmap.get("action")) ;
			EACH_USERLOG userlog_po = getUSERLOG(op_type ,action );
			userlog_po.setAFCHCON("查詢條件 : "+restMapKey2CH(tmpmap));
			userlog_po.setADEXCODE(adexcode);
			userLog_Dao.aop_save(userlog_po);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.debug(" searchLog Exception>>"+e);
		}
	}
	
	public void genericLog(String op_type  , String adexcode, String action ,Map newmap , Map oldmap ){
		boolean iswritelog = true;
		String s_is_record = "";
		try {
			Login_Form login_form = (Login_Form) WebServletUtils.getRequest().getSession().getAttribute("login_form");
			s_is_record =  login_form.getUserData().getS_is_record();
			if(StrUtils.isNotEmpty(op_type) && op_type.equals("C") && !s_is_record.equals("Y")){
				iswritelog = false;
			}
			EACH_USERLOG userlog_po = getUSERLOG(op_type ,action );
			userlog_po.setBFCHCON(restMapKey2CH(oldmap)+"");
			userlog_po.setAFCHCON(restMapKey2CH(newmap)+"");
			userlog_po.setADEXCODE(adexcode);
			if(iswritelog){
				userLog_Dao.aop_save(userlog_po);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.debug(" searchLog Exception>>"+e);
		}
	}
	public void genericLog(String op_type  , String adexcode, String action ,String newdesc ,Map newmap , String olddesc ,Map oldmap ){
		boolean iswritelog = true;
		String s_is_record = "";
		try {
			Login_Form login_form = (Login_Form) WebServletUtils.getRequest().getSession().getAttribute("login_form");
			s_is_record =  login_form.getUserData().getS_is_record();
			if(StrUtils.isNotEmpty(op_type) && op_type.equals("C") && !s_is_record.equals("Y")){
				iswritelog = false;
			}
			EACH_USERLOG userlog_po = getUSERLOG(op_type ,action );
			userlog_po.setBFCHCON(olddesc+restMapKey2CH(oldmap));
			userlog_po.setAFCHCON(newdesc+restMapKey2CH(newmap)+"");
			userlog_po.setADEXCODE(adexcode);
			if(iswritelog){
				userLog_Dao.aop_save(userlog_po);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.debug(" searchLog Exception>>"+e);
		}
	}
	
	public void addLog(String op_type  , String adexcode, Map newmap , Map pkmap ){
		try {
			log.debug(" addLog.newmap >>"+newmap);
			log.debug(" addLog.op_type >>"+op_type);
			EACH_USERLOG userlog_po = getUSERLOG(op_type , "");
			if(newmap != null ){
				userlog_po.setAFCHCON(String.valueOf(restMapKey2CH(newmap)));
			}
			userlog_po.setADEXCODE(adexcode);
			userLog_Dao.aop_save(userlog_po);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.debug(" addLog.Exception>>"+e);
		}
	}
	
	
	public void updateLog(String op_type  , String adexcode, Map oldmap , Map newmap , Map pkmap ){
		EACH_USERLOG userlog_po = getUSERLOG(op_type , "");
		try {
			log.debug(" updateLog.newmap ="+newmap);
			log.debug(" updateLog.oldmap ="+oldmap);
			log.debug(" updateLog.pkmap ="+pkmap);
			Map<String,Map> tmpmap = com_Dif_Val(newmap, oldmap);
			log.debug(" updateLog.tmpmap ="+tmpmap);
			if(tmpmap.size()!=0){
				oldmap = tmpmap.get("oldmap");
				newmap = tmpmap.get("newmap");
			}
			userlog_po.setBFCHCON(restMapKey2CH(oldmap).toString());
			userlog_po.setAFCHCON(restMapKey2CH(newmap).toString());
			userlog_po.setADEXCODE(adexcode+"，PK="+restMapKey2CH(pkmap));
			userLog_Dao.aop_save(userlog_po);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.debug(" updateLog.Exception>>"+e);
		}
	}
	
	
	
	public void deleteLog(String op_type  , String adexcode, Map oldmap , Map pkmap ){
		try {
			EACH_USERLOG userlog_po = getUSERLOG(op_type , "");
			log.debug(" deleteLog.oldmap ="+oldmap);
			if(oldmap != null ){
				userlog_po.setBFCHCON(String.valueOf(restMapKey2CH(oldmap)));
			}
			userlog_po.setADEXCODE(adexcode+"，PK="+restMapKey2CH(pkmap));
			userLog_Dao.aop_save(userlog_po);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.debug(" deleteLog Exception>>"+e);
		}
	}
	
	
	
	
	
	
	
	
	//按照USER_COMPANY找出所有USER
	public List<LabelValueBean> getUserIdListByComId(String comId){
		List<EACH_USER> userIdList = null;
		List<LabelValueBean> list = null;
		try{
			if(StrUtils.isNotEmpty(comId)){
				userIdList = each_user_Dao.getDataByComId(comId);
			}else{
				userIdList = each_user_Dao.getAll();
			}
			System.out.println("userIdList"+userIdList);
			if(userIdList != null && userIdList.size() > 0){
				list = new ArrayList<LabelValueBean>();
				for(int i = 0; i < userIdList.size(); i++){
					list.add(new LabelValueBean(userIdList.get(i).getUSER_ID(), userIdList.get(i).getUSER_ID()));
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		list = list == null? null : list.size() == 0? null : list;
		System.out.println("list>>" + list);
		return list;
	}
	
	
	//找出所有USER_COMPANY清單
	public List<LabelValueBean> getUserCompanyList(){
		List<EACH_USER_PK> usercompanyList = null;
		List<LabelValueBean> list = null;
		try{
			usercompanyList = each_user_Dao.getUserCompanyList();
			if(usercompanyList != null && usercompanyList.size() > 0){
				list = new ArrayList<LabelValueBean>();
				for(int i = 0; i < usercompanyList.size(); i++){
					list.add(new LabelValueBean(usercompanyList.get(i).getUSER_COMPANY(), usercompanyList.get(i).getUSER_COMPANY()));
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		list = list == null? null : list.size() == 0? null : list;
		System.out.println("list>>" + list);
		return list;
	}	
	//根據user_type找出關聯的USER_COMPANY清單
	public List<LabelValueBean> getUserCompanyList(String user_type){
		List<EACH_USER_PK> usercompanyList = null;
		List<LabelValueBean> list = null;
		try{
			usercompanyList = each_user_Dao.getUserCompanyList(user_type);
			if(usercompanyList != null && usercompanyList.size() > 0){
				list = new ArrayList<LabelValueBean>();
				for(int i = 0; i < usercompanyList.size(); i++){
					list.add(new LabelValueBean(usercompanyList.get(i).getUSER_COMPANY(), usercompanyList.get(i).getUSER_COMPANY()));
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		list = list == null? null : list.size() == 0? null : list;
		System.out.println("list>>" + list);
		return list;
	}	
	
	//找出所有功能清單
	public List getFuncList(){
		List<EACH_FUNC_LIST> menuList = null;
		List<EACH_FUNC_LIST> subList = null;
		Map menuItem = null;
		Map subItem = null;
		List menuList_N = new ArrayList();
		List subList_N = new ArrayList();
		try {
			//找作業模組
			menuList = func_list_Dao.getFuncItemByType("1");
			if(menuList != null){
				menuList_N = new ArrayList();
				for(int i = 0; i < menuList.size(); i++){
					menuItem = new HashMap();
					menuItem.put("FUNC_NAME", menuList.get(i).getFUNC_NAME());
					menuItem.put("FUNC_URL", menuList.get(i).getFUNC_URL());
					menuItem.put("FUNC_TYPE", menuList.get(i).getFUNC_TYPE());
					menuItem.put("FUNC_ID", menuList.get(i).getFUNC_ID());
					subList = func_list_Dao.getNextSubItem(menuList.get(i).getFUNC_ID());
					if(subList != null){
						subList_N = new ArrayList();
						for(int j = 0; j < subList.size(); j++){
							subItem = new HashMap();
							subItem.put("FUNC_NAME", subList.get(j).getFUNC_NAME());
							subItem.put("FUNC_URL", subList.get(j).getFUNC_URL());
							subItem.put("FUNC_TYPE", subList.get(j).getFUNC_TYPE());
							subItem.put("FUNC_ID", subList.get(j).getFUNC_ID());
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
	//找出所有功能清單
	public List getFuncList(String user_type){
		List<EACH_FUNC_LIST> menuList = null;
		List<EACH_FUNC_LIST> subList = null;
		Map menuItem = null;
		Map subItem = null;
//		List menuList_N = new ArrayList();
		List menuList_N = new LinkedList();
		List subList_N = new ArrayList();
		try {
			//找作業模組
			StringBuffer pathSql = new StringBuffer();
			if(user_type.equals("A")){
				pathSql.append(" AND TCH_FUNC != 'N' ");
			}
			if(user_type.equals("B")){
				pathSql.append(" AND BANK_FUNC != 'N' ");
			}
			if(user_type.equals("C")){
				pathSql.append(" AND COMPANY_FUNC != 'N' ");
			}
			
			menuItem = getLoginItem(menuItem, subItem);
			menuList_N.add(menuItem);
			menuList = func_list_Dao.getFuncItemByTypes("1" ,pathSql.toString() );
			if(menuList != null){
//				menuList_N = new ArrayList();
				for(int i = 0; i < menuList.size(); i++){
					menuItem = new HashMap();
					menuItem.put("FUNC_NAME", menuList.get(i).getFUNC_NAME());
					menuItem.put("FUNC_URL", menuList.get(i).getFUNC_URL());
					menuItem.put("FUNC_TYPE", menuList.get(i).getFUNC_TYPE());
					menuItem.put("FUNC_ID", menuList.get(i).getFUNC_ID());
					subList = func_list_Dao.getNextSubItem(menuList.get(i).getFUNC_ID());
					subList = func_list_Dao.getNextSubItemII(menuList.get(i).getFUNC_ID() , user_type);
					if(subList != null){
						subList_N = new ArrayList();
						for(int j = 0; j < subList.size(); j++){
							subItem = new HashMap();
							subItem.put("FUNC_NAME", subList.get(j).getFUNC_NAME());
							subItem.put("FUNC_URL", subList.get(j).getFUNC_URL());
							subItem.put("FUNC_TYPE", subList.get(j).getFUNC_TYPE());
							subItem.put("FUNC_ID", subList.get(j).getFUNC_ID());
							subList_N.add(subItem);
						}
						menuItem.put("SUB_LIST", subList_N);
					}//if end
					menuList_N.add(menuItem);
				}//for end
//					menuList_N.add(0,menuItem);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("menu>>" + menuList_N);
		return menuList_N;
	}
	
	
	
	public Map getLoginItem(Map menuItem , Map subItem){
		List<Map> subList_N = new LinkedList<Map>();
		menuItem = new HashMap();
		subItem = new HashMap();
		menuItem.put("FUNC_NAME", "登入登出作業");
		menuItem.put("FUNC_URL", "");
		menuItem.put("FUNC_TYPE", "");
		menuItem.put("FUNC_ID", "eACH9999");
		
		subItem.put("FUNC_NAME", "登入作業");
		subItem.put("FUNC_URL", "");
		subItem.put("FUNC_TYPE", "");
		subItem.put("FUNC_ID", "eACH9999");
		subList_N.add(subItem);
		
		subItem = new HashMap();
		subItem.put("FUNC_NAME", "登出作業");
		subItem.put("FUNC_URL", "");
		subItem.put("FUNC_TYPE", "");
		subItem.put("FUNC_ID", "eACH9998");
		subList_N.add(subItem);
		
		menuItem.put("SUB_LIST", subList_N);
		
		return menuItem;
	}
	
	
	//根據群組類別找出所屬功能清單
	public List getFuncListByRole_Type(String role_type){
		List<Map> beanList = new LinkedList<Map>();
		List<Map> subList = new LinkedList<Map>();
		StringBuffer pathSql = new StringBuffer();
		try {
			role_type= StrUtils.isEmpty(role_type) ? "" :role_type;
			//票交所
			if(role_type.equals("A")){
//				pathSql.append(" WHERE (S.BANK_FUNC='Y' AND S.TCH_FUNC='Y') OR S.TCH_FUNC='Y' ");
				pathSql.append(" WHERE S.TCH_FUNC !='N' ");
			//銀行端
			}else if(role_type.equals("B")){
//				pathSql.append(" WHERE (S.BANK_FUNC='Y' AND S.TCH_FUNC='Y') OR S.BANK_FUNC='Y' ");
				pathSql.append(" WHERE S.BANK_FUNC !='N' ");
			}else if(role_type.equals("C")){
				pathSql.append(" WHERE S.COMPANY_FUNC !='N' ");
			}
			List<EACH_FUNC_LIST> list = func_list_Dao.getAllSubItem(pathSql.toString());
			String tmp = "";
			String tmpName = "";
			Map map = null;
			Map keymap = null;
			keymap = getLoginItem_userTypB(null, null);
			subList.add(keymap);
			int i =0;
			for(EACH_FUNC_LIST po :list){
				map = new HashMap<>();
				System.out.println("tmp"+tmp+",UP_FUNC_ID>>"+po.getUP_FUNC_ID());
				if(i==0){
					tmp = po.getUP_FUNC_ID();
					tmpName = po.getUP_FUNC_NAME();
				}
				
				if(!tmp.equals(po.getUP_FUNC_ID())){
					keymap = new HashMap<>();
					keymap.put(tmpName, beanList);
					subList.add(keymap);
					tmp = po.getUP_FUNC_ID();
					tmpName = po.getUP_FUNC_NAME();
					beanList = new LinkedList<Map>();
				}
				map.put("label", po.getFUNC_NAME());
				map.put("value", po.getFUNC_ID());
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
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("subList>>" + subList);
		return subList;
	}
	
	
	public Map getLoginItem_userTypB(Map menuItem , Map subItem){
		List<Map> subList_N = new LinkedList<Map>();
		menuItem = new HashMap();
		subItem = new HashMap();
		
		subItem.put("label", "登入作業");
		subItem.put("value", "eACH9999");
		subList_N.add(subItem);
		
		subItem = new HashMap();
		subItem.put("label", "登出作業");
		subItem.put("value", "eACH9998");
		subList_N.add(subItem);
		
		menuItem.put("登入登出作業", subList_N);
		
		return menuItem;
	}
	
	
	
	public List<EACH_USERLOG> search(String opt_date ,String user_company ,String sUser_id , String func_id,int pageNo , int pageSize){
		List<EACH_USERLOG> list = null;
		try {
			
			list = new ArrayList<EACH_USERLOG>();
			StringBuffer sql = new StringBuffer();
			sql.append(" FROM tw.org.twntch.po.EACH_USERLOG WHERE ") ;
			List<String> strList = new LinkedList<String>();
			Map<String,String> map = new HashMap<String,String>();
			map.put("TXTIME", DateTimeUtils.convertDate(opt_date.trim(), "yyyyMMdd", "yyyy-MM-dd"));
			map.put("USER_COMPANY", user_company.trim());
			map.put("USERID", sUser_id.trim());
			map.put("FUNC_ID", func_id.trim());
			int i =0;
			for(String key:map.keySet()){
				if(StrUtils.isNotEmpty(map.get(key))){
					if(i!=0){
						sql.append(" AND ");
					}
					if(key.equals("TXTIME")){
						sql.append(key + " >=  ?  AND "+key+" <= ?");
						strList.add(map.get(key)+" 00:00:00 ");
						strList.add(map.get(key)+" 23:59:59 ");
//						sql.append(key + " LIKE ?");
//						strList.add("%" + map.get(key) + "%");
					}else{
						sql.append(key+" = ?");
						strList.add(map.get(key));
					}
					i++;
				}
			}
			System.out.println("strList>>"+strList);
			System.out.println("sql.toString()==>"+sql.toString());
			list = (List<EACH_USERLOG>) userLog_Dao.pagedQuery(sql.toString(),pageNo,pageSize, strList.toArray()).getResult();
//			list = (List<EACH_USERLOG>) userLog_Dao.getAllDataByParm(pageNo,pageSize);
			System.out.println("EACH_USERLOG.list>>"+list);
			list = list!=null&& list.size() ==0 ?null:list;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	public String search_toJson(Map<String, String> param){
		String opt_date = StrUtils.isNotEmpty(param.get("TXTIME"))?param.get("TXTIME"):"";
		String user_company = StrUtils.isNotEmpty(param.get("USER_COMPANY")) && param.get("USER_COMPANY").trim().equals("all") ?"":param.get("USER_COMPANY");
		String sUser_id = StrUtils.isNotEmpty(param.get("USERID")) && param.get("USERID").trim().equals("all") ?"":param.get("USERID");
		String func_id = StrUtils.isNotEmpty(param.get("FUNC_ID")) && param.get("FUNC_ID").trim().equals("all") ?"":param.get("FUNC_ID");
//		String pageNo = StrUtils.isEmpty(param.get("pageNo")) ?"0":param.get("pageNo");
		String pageNo = StrUtils.isEmpty(param.get("page")) ?"0":param.get("page");
		System.out.println("pageNo"+pageNo);
//	 改從參數拿取
		String pageSize = StrUtils.isEmpty(param.get("pagesize")) ?Arguments.getStringArg("PAGE.SIZE"):param.get("pagesize");
		String json = JSONUtils.toJson(search(opt_date, user_company, sUser_id, func_id ,Integer.valueOf(pageNo)  ,Integer.valueOf(pageSize) )) ;
		return json;
	}
	
	//分頁版本
	public String pageSearch(Map<String, String> param){
		String opt_date = StrUtils.isNotEmpty(param.get("TXTIME_1"))?param.get("TXTIME_1"):"";
		String opt_date2 = StrUtils.isNotEmpty(param.get("TXTIME_2"))?param.get("TXTIME_2"):"";
		String user_company = StrUtils.isNotEmpty(param.get("USER_COMPANY")) && param.get("USER_COMPANY").trim().equals("all") ?"":param.get("USER_COMPANY");
		String sUser_id = StrUtils.isNotEmpty(param.get("USERID")) && param.get("USERID").trim().equals("all") ?"":param.get("USERID");
		String func_id = StrUtils.isNotEmpty(param.get("FUNC_ID")) && param.get("FUNC_ID").trim().equals("all") ?"":param.get("FUNC_ID");
		String user_type = StrUtils.isNotEmpty(param.get("USER_TYPE")) ?param.get("USER_TYPE"):"";
		//String pageNo = StrUtils.isEmpty(param.get("pageNo")) ?"0":param.get("pageNo");
		String pageNo = StrUtils.isEmpty(param.get("page")) ?"0":param.get("page");
		//改從參數拿取
		String pageSize = StrUtils.isEmpty(param.get("rows")) ?Arguments.getStringArg("PAGE.SIZE"):param.get("rows");
		//判斷群組類型
		String role_type = StrUtils.isNotEmpty(param.get("ROLE_TYPE"))?param.get("ROLE_TYPE"):"";
		
		Map rtnMap = new HashMap();
		List<EACH_USERLOG> list = null;
		Page page = null;
		try {
			list = new ArrayList<EACH_USERLOG>();
			StringBuffer orderbysql = new StringBuffer();
			List<String> strList = new LinkedList<String>();
			Map<String,String> map = new HashMap<String,String>();
			map.put("TXTIME_1", DateTimeUtils.convertDate(opt_date.trim(), "yyyyMMdd", "yyyy-MM-dd"));
			map.put("TXTIME_2", DateTimeUtils.convertDate(opt_date2.trim(), "yyyyMMdd", "yyyy-MM-dd"));
			map.put("USER_COMPANY", user_company.trim());
			map.put("USERID", sUser_id.trim());
			map.put("FUNC_ID", func_id.trim());
			map.put("USER_TYPE", user_type.trim());
			map.put("ROLE_TYPE", role_type.trim());
			System.out.println("user_type>>"+user_type);
			System.out.println("strList>>"+strList);
			System.out.println("role_type>>"+role_type);
//			if(StrUtils.isNotEmpty(param.get("sidx"))){
//				orderbysql.append(" ORDER BY "+param.get("sidx")+" "+param.get("sord"));
				orderbysql.append( getOrderBySql(param.get("sidx"), param.get("sord")));
//			}
			Map condition_Map =  getConditionData(map);
			String sql = getSql(condition_Map.get("sqlPath").toString(), orderbysql.toString(), user_type);
			String countSql = getCountSql(condition_Map.get("sqlPath").toString(), user_type);
			System.out.println("sql.toString()==>"+sql.toString());
			page = userLog_Dao.pagedSQLQuery(sql, countSql, Integer.valueOf(pageNo), Integer.valueOf(pageSize), (List<String>) condition_Map.get("values") , EACH_USERLOG.class );
//			page = userLog_Dao.pagedQuery(sql.toString(),Integer.valueOf(pageNo), Integer.valueOf(pageSize), strList.toArray());
			list = (List<EACH_USERLOG>) page.getResult();
//			list = (List<EACH_USERLOG>) userLog_Dao.getAllDataByParm(Integer.valueOf(pageNo),Integer.valueOf(pageSize));
			System.out.println("EACH_USERLOG.list>>"+list);
			list = list!=null&& list.size() ==0 ?null:list;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(page == null){
			rtnMap.put("total", "0");
			rtnMap.put("page", "0");
			rtnMap.put("records", "0");
			rtnMap.put("rows", new ArrayList());
		}else{
			rtnMap.put("total", page.getTotalPageCount());
			rtnMap.put("page", String.valueOf(page.getCurrentPageNo()));
			rtnMap.put("records", page.getTotalCount());
			rtnMap.put("rows", list);
		}
		
		String json = JSONUtils.map2json(rtnMap) ;
		return json;
	}
	
	public String getSql(String sqlPath ,String orderbySql, String user_type){
		String userType_sql = "";
		if(!user_type.equals("A")){
			userType_sql = " JOIN EACH_USER A ON E.USER_COMPANY = A.USER_COMPANY ";
		}
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT  E.SERNO ,E.USERID , E.USER_COMPANY ,E.USERIP,E.TXTIME"); 
//		sql.append(" ,E.UP_FUNC_ID ||'-'|| (SELECT FUNC_NAME FROM EACH_FUNC_LIST WHERE FUNC_ID = E.UP_FUNC_ID) AS UP_FUNC_ID "); 
//		sql.append(" ,E.FUNC_ID ||'-'||  (SELECT FUNC_NAME FROM EACH_FUNC_LIST WHERE FUNC_ID = E.FUNC_ID) AS FUNC_ID "); 
		sql.append(" ,E.UP_FUNC_ID ||'-'|| COALESCE ( (SELECT  FUNC_NAME  FROM EACH_FUNC_LIST WHERE FUNC_ID = E.UP_FUNC_ID) ,'') AS UP_FUNC_ID "); 
		sql.append(" ,E.FUNC_ID ||'-'||  COALESCE ( ( SELECT  FUNC_NAME  FROM EACH_FUNC_LIST WHERE FUNC_ID = E.FUNC_ID ) ,'')   AS FUNC_ID "); 
		sql.append(" ,E.FUNC_TYPE,E.OPITEM , E.BFCHCON,E.AFCHCON,E.ADEXCODE "); 
		sql.append(" FROM EACH_USERLOG E "+userType_sql+"WHERE "); 
		sql.append(sqlPath); 
		sql.append(orderbySql); 
		System.out.println("getSql.sql>>"+sql);
		return sql.toString();
	}
	public String getCountSql(String sqlPath, String user_type){
		String userType_sql = "";
		if(!user_type.equals("A")){
			userType_sql = " JOIN EACH_USER A ON E.USER_COMPANY = A.USER_COMPANY ";
		}
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT count(*) "); 
		sql.append(" FROM EACH_USERLOG E "+userType_sql+"WHERE "); 
		sql.append(sqlPath); 
		System.out.println("getSql.sql>>"+sql);
		return sql.toString();
	}
	
	public String getOrderBySql(String sidx ,String sord){
		StringBuffer orderbysql = new StringBuffer();
		System.out.println("sidx>>"+sidx);
		if(StrUtils.isEmpty(sidx)){
			orderbysql.append(" ORDER BY TXTIME DESC");
		}else{
			orderbysql.append(" ORDER BY "+sidx+" "+sord);
		}
		
		System.out.println("orderbysql>>"+orderbysql);
		return orderbysql.toString();
		
	}
	
	public Map getConditionData(Map<String,String> param ){
		StringBuffer sql = new StringBuffer();
		List values = new LinkedList();
		Map retmap = new LinkedHashMap();
		int i =0;
		for(String key:param.keySet()){
			if(StrUtils.isNotEmpty(param.get(key))){
				if(i!=0){
					sql.append(" AND ");
				}
				if(key.equals("TXTIME_1")){
					sql.append("E.TXTIME >=  ?");
					values.add(param.get(key)+" 00:00:00 ");
					
				}else if(key.equals("TXTIME_2")){
					sql.append("E.TXTIME <=  ?");
					values.add(param.get(key)+" 23:59:59 ");
				}else if(key.equals("USER_TYPE") && param.get(key).equals("B")){
//					要過濾掉票交的使用者紀錄
					sql.append("E.USER_COMPANY != '0188888' ");
					
				}else if(key.equals("USER_TYPE") && param.get(key).equals("A")){
					
					sql.append(" 1 = 1 ");
				}else if(key.equals("ROLE_TYPE")){
					if(! param.get("USER_TYPE").equals("A")){
						if(param.get("ROLE_TYPE").equals("B")){
							sql.append("A.USER_TYPE='B' AND A.USER_ID = E.USERID ");
						}else{
							sql.append("A.USER_TYPE='C' AND A.USER_ID = E.USERID ");
						}
					}
				}else{
					sql.append("E."+key+" = ?");
					values.add(param.get(key));
				}
				i++;
			}
		}
		retmap.put("sqlPath", sql.toString());
		retmap.put("values", values);
		return retmap;
	}
	//分頁版本
//	public String pageSearch(Map<String, String> param){
//		String opt_date = StrUtils.isNotEmpty(param.get("TXTIME"))?param.get("TXTIME"):"";
//		String user_company = StrUtils.isNotEmpty(param.get("USER_COMPANY")) && param.get("USER_COMPANY").trim().equals("all") ?"":param.get("USER_COMPANY");
//		String sUser_id = StrUtils.isNotEmpty(param.get("USERID")) && param.get("USERID").trim().equals("all") ?"":param.get("USERID");
//		String func_id = StrUtils.isNotEmpty(param.get("FUNC_ID")) && param.get("FUNC_ID").trim().equals("all") ?"":param.get("FUNC_ID");
//		//String pageNo = StrUtils.isEmpty(param.get("pageNo")) ?"0":param.get("pageNo");
//		String pageNo = StrUtils.isEmpty(param.get("page")) ?"0":param.get("page");
//		//改從參數拿取
//		String pageSize = StrUtils.isEmpty(param.get("rows")) ?Arguments.getStringArg("PAGE.SIZE"):param.get("rows");
//		
//		Map rtnMap = new HashMap();
//		List<EACH_USERLOG> list = null;
//		Page page = null;
//		try {
//			list = new ArrayList<EACH_USERLOG>();
//			StringBuffer sql = new StringBuffer();
//			sql.append(" FROM tw.org.twntch.po.EACH_USERLOG WHERE ") ;
//			List<String> strList = new LinkedList<String>();
//			Map<String,String> map = new HashMap<String,String>();
//			map.put("TXTIME", DateTimeUtils.convertDate(opt_date.trim(), "yyyyMMdd", "yyyy-MM-dd"));
//			map.put("USER_COMPANY", user_company.trim());
//			map.put("USERID", sUser_id.trim());
//			map.put("FUNC_ID", func_id.trim());
//			int i =0;
//			for(String key:map.keySet()){
//				if(StrUtils.isNotEmpty(map.get(key))){
//					if(i!=0){
//						sql.append(" AND ");
//					}
//					if(key.equals("TXTIME")){
//						sql.append(key + " >=  ?  AND "+key+" <= ?");
//						strList.add(map.get(key)+" 00:00:00 ");
//						strList.add(map.get(key)+" 23:59:59 ");
////						sql.append(key + " LIKE ?");
////						strList.add("%" + map.get(key) + "%");
//					}else{
//						sql.append(key+" = ?");
//						strList.add(map.get(key));
//					}
//					i++;
//				}
//			}
//			System.out.println("strList>>"+strList);
//			if(StrUtils.isNotEmpty(param.get("sidx"))){
//				sql.append(" ORDER BY "+param.get("sidx")+" "+param.get("sord"));
//			}
//			System.out.println("sql.toString()==>"+sql.toString());
//			page = userLog_Dao.pagedQuery(sql.toString(),Integer.valueOf(pageNo), Integer.valueOf(pageSize), strList.toArray());
//			list = (List<EACH_USERLOG>) page.getResult();
////			list = (List<EACH_USERLOG>) userLog_Dao.getAllDataByParm(Integer.valueOf(pageNo),Integer.valueOf(pageSize));
//			System.out.println("EACH_USERLOG.list>>"+list);
//			list = list!=null&& list.size() ==0 ?null:list;
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		if(page == null){
//			rtnMap.put("total", "0");
//			rtnMap.put("page", "0");
//			rtnMap.put("records", "0");
//			rtnMap.put("rows", new ArrayList());
//		}else{
//			rtnMap.put("total", page.getTotalPageCount());
//			rtnMap.put("page", String.valueOf(page.getCurrentPageNo()));
//			rtnMap.put("records", page.getTotalCount());
//			rtnMap.put("rows", list);
//		}
//		
//		String json = JSONUtils.map2json(rtnMap) ;
//		return json;
//	}
	
	public String getUserIdListByComId(Map<String, String> param){
		String com_id = StrUtils.isNotEmpty(param.get("com_id")) && param.get("com_id").trim().equals("all") ?"":param.get("com_id");
		List<EACH_USER> userIdList = null;
		List<LabelValueBean> list = null;
		try{
			if(StrUtils.isNotEmpty(com_id)){
				userIdList = each_user_Dao.getDataByComId(com_id);
			}else{
				if(com_id.equals("0188888")){
					userIdList = each_user_Dao.getAll();
				}else{
					userIdList = each_user_Dao.getAllNotEach();
				}
				
			}
			if(userIdList != null && userIdList.size() > 0){
				list = new ArrayList<LabelValueBean>();
				for(int i = 0; i < userIdList.size(); i++){
					list.add(new LabelValueBean(userIdList.get(i).getUSER_ID(), userIdList.get(i).getUSER_ID()));
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("list>>" + list);
		return JSONUtils.toJson(list);
	}
	
	public EACH_USERLOG getDetail(String ser_no , String user_id , String com_id ){
//		EACH_USERLOG_PK id = new EACH_USERLOG_PK( ser_no , com_id  ,  user_id);
//		EACH_USERLOG po = userLog_Dao.get(id);
		ser_no = StrUtils.isEmpty(ser_no)?"":ser_no.trim();
		user_id = StrUtils.isEmpty(user_id)?"":user_id.trim();
		com_id = StrUtils.isEmpty(com_id)?"":com_id.trim();
		List<EACH_USERLOG> list  = userLog_Dao.getDetail(ser_no, user_id, com_id);
		EACH_USERLOG po = null;
		System.out.println("list>>"+list);
		if(list != null && list.size() !=0 ){
			for(EACH_USERLOG logpo :list){
				System.out.println("logpo>>"+logpo);
				po = logpo != null ?logpo : new EACH_USERLOG()  ;
			}
		}
		System.out.println("po>>"+po);
		return po;
	}
	
	public String getFuncListBy_roleId(Map<String, String> param){
		String role_type = StrUtils.isNotEmpty(param.get("role_type")) ?param.get("role_type"):"";
		System.out.println("role_type ="+role_type);
		List funcList = getFuncListByRole_Type(role_type);
		
		return JSONUtils.toJson(funcList);
	}
	
	public void save(EACH_USERLOG po){
		userLog_Dao.aop_save(po);
	}
	
	
	
	
	public EACH_USER_Dao getEach_user_Dao() {
		return each_user_Dao;
	}

	public void setEach_user_Dao(EACH_USER_Dao each_user_Dao) {
		this.each_user_Dao = each_user_Dao;
	}

	public EACH_FUNC_LIST_Dao getFunc_list_Dao() {
		return func_list_Dao;
	}

	public void setFunc_list_Dao(EACH_FUNC_LIST_Dao func_list_Dao) {
		this.func_list_Dao = func_list_Dao;
	}

	public EACH_USERLOG_Dao getUserLog_Dao() {
		return userLog_Dao;
	}

	public void setUserLog_Dao(EACH_USERLOG_Dao userLog_Dao) {
		this.userLog_Dao = userLog_Dao;
	}

	public FieldsMap getFieldsmap() {
		return fieldsmap;
	}

	public void setFieldsmap(FieldsMap fieldsmap) {
		this.fieldsmap = fieldsmap;
	}
	
	
	
	
}
