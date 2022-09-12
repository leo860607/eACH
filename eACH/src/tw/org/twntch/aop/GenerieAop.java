package tw.org.twntch.aop;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.aspectj.lang.annotation.Aspect;

import tw.org.twntch.bo.FieldsMap;
import tw.org.twntch.db.dao.hibernate.EACH_FUNC_LIST_Dao;
import tw.org.twntch.db.dao.hibernate.EACH_USERLOG_Dao;
import tw.org.twntch.form.Login_Form;
import tw.org.twntch.po.EACH_FUNC_LIST;
import tw.org.twntch.po.EACH_USERLOG;
import tw.org.twntch.po.EACH_USERLOG_PK;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.SpringAppCtxHelper;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.WebServletUtils;
import tw.org.twntch.util.zDateHandler;

public class GenerieAop {
Logger logger = Logger.getLogger(getClass()); 
private EACH_USERLOG_Dao userLog_Dao ;
private EACH_FUNC_LIST_Dao func_list_Dao;
private FieldsMap fieldsmap;
	



public Map com_Dif_Val(Map<String,String> newmap , Map<String,String> oldmap){
	System.out.println(" super.compareDif_Val is start");
	newmap = newmap != null ?newmap :new HashMap<String,String>();
	oldmap = oldmap != null ?oldmap :new HashMap<String,String>();
	String oldvalue = "";
	String newvalue = "";
	Map<String ,Map> map = new HashMap<String, Map>();
	Map<String ,String> oldtmp = new HashMap<String, String>();
	Map<String ,String> newtmp = new HashMap<String, String>();
	for(String key : newmap.keySet()){
//		20150627 edit by hugo 用containsValue 不適合
//		if( ! oldmap.containsValue(newmap.get(key))){
		oldvalue = StrUtils.isNotEmpty(oldmap.get(key)) ? oldmap.get(key):"";
		newvalue = StrUtils.isNotEmpty(newmap.get(key)) ? newmap.get(key):"";
			if( ! oldvalue.equals(newvalue)){
//			oldtmp = new HashMap<String, String>();
//			newtmp = new HashMap<String, String>();
			oldtmp.put(key, oldmap.get(key));
			newtmp.put(key, newmap.get(key));
//			map.put(fieldMap.get(key).toString(), newmap.get(key));
//			map.put("oldmap", JSONUtils.json2map("{"+key+"="+oldmap.get(key)+"}") );
//			map.put("newmap", JSONUtils.json2map("{"+key+"="+newmap.get(key)+"}") );
		}
	}
	map.put("oldmap", oldtmp );
	map.put("newmap", newtmp );
	System.out.println("compareDif_Val.map>>"+map);
	return map;
}
	
	/**
	 * 把map中的key(field name)轉成對應的中文名稱
	 * @param newmap
	 * @return
	 */
	public Map restMapKey2CH(Map<String,String> newmap){
		System.out.println(" super.restMapKey2CH is start");
		newmap = newmap != null ?newmap :new HashMap<String,String>();
		fieldsmap = (FieldsMap) (fieldsmap== null ?SpringAppCtxHelper.getBean("fieldsmap"):fieldsmap);
		Map<String, Object> fieldMap = fieldsmap.getArgs();
		Map map = new HashMap<String, String>();
		for(String key : newmap.keySet()){
			if(fieldMap.containsKey(key)){
				map.put( String.valueOf(fieldMap.get(key)), String.valueOf(newmap.get(key)));
//				newmap.put(fieldMap.get(key).toString(), newmap.get(key));
//				newmap.remove(key); 會出錯
			}
		}
		return map;
	}
	
	/**
	 * 
	 * @param op_type A:新增 B:修改 C:查詢 D:刪除 E:報表列印 F:檔案下載
	 * @return
	 */
	public EACH_USERLOG getEACH_USERLOG(String op_type){
		UUID uuid = UUID.randomUUID();
		System.out.println("uuid"+uuid);
		Login_Form form = (Login_Form) WebServletUtils.getRequest().getSession().getAttribute("login_form");
		System.out.println("form>>"+form);
		EACH_USERLOG po = null;
		EACH_USERLOG_PK id  = null;
		try {
			if(form != null){
				System.out.println("formgetUserData>>"+form.getUserData());
				System.out.println("formgetUSER_COMPANY>>"+form.getUserData().getUSER_COMPANY());
				System.out.println("getUSER_ID>>"+form.getUserData().getUSER_ID());
			}else{
				return null;
			}
			id = new EACH_USERLOG_PK(uuid.toString(), form.getUserData().getUSER_COMPANY(), form.getUserData().getUSER_ID());
			po = new EACH_USERLOG();
//		20150514 edit by hugo 因應load balance 取不到用戶端IP
//		req.getHeader("referer");
			String userIP = WebServletUtils.getRequest().getHeader("HTTP_X_FORWARDED_FOR");
			System.out.println("HTTP_X_FORWARDED_FOR.userIP>>"+userIP);
			userIP = StrUtils.isEmpty(userIP)?WebServletUtils.getRequest().getHeader("X-Forwarded-For"):userIP;
			System.out.println("X-Forwarded-For.userIP>>"+userIP);
			userIP = StrUtils.isEmpty(userIP)?WebServletUtils.getRequest().getHeader("Remote_Addr"):userIP;
			System.out.println("getHeader(Remote_Addr).userIP>>"+userIP);
			userIP = StrUtils.isEmpty(userIP)?WebServletUtils.getRequest().getRemoteAddr():userIP;
			System.out.println("getRequest().getRemoteAddr().userIP>>"+userIP);
			po.setUSERIP(userIP);
			po.setTXTIME(zDateHandler.getTheDateII()+" "+zDateHandler.getTheTime_MS());
			po.setOPITEM(op_type);
			po.setId(id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			po = null;
		}
		return po;
	}
	/**
	 * 
	 * @param op_type A:新增 B:修改 C:查詢 D:刪除 E:報表列印 F:檔案下載
	 * @return
	 */
	public EACH_USERLOG getEACH_USERLOG(String op_type , String opbk_id  ){
		UUID uuid = UUID.randomUUID();
		System.out.println("uuid"+uuid);
		Login_Form form = (Login_Form) WebServletUtils.getRequest().getSession().getAttribute("login_form");
		System.out.println("form>>"+form);
		EACH_USERLOG po = null;
		EACH_USERLOG_PK id  = null;
		try {
			if(form != null && form.getUserData() !=null){
				System.out.println("formgetUserData>>"+form.getUserData());
				System.out.println("formgetUSER_COMPANY>>"+form.getUserData().getUSER_COMPANY());
				System.out.println("getUSER_ID>>"+form.getUserData().getUSER_ID());
				id = new EACH_USERLOG_PK(uuid.toString(), form.getUserData().getUSER_COMPANY(), form.getUserData().getUSER_ID());
			}else{
//				如果是未登入狀態下的http下載 USER_ID無法抓取   confirm by 票交 李建利&江怡宏
				id = new EACH_USERLOG_PK(uuid.toString(), opbk_id , "" );
			}
			po = new EACH_USERLOG();
//		20150514 edit by hugo 因應load balance 取不到用戶端IP
//		req.getHeader("referer");
			String userIP = WebServletUtils.getRequest().getHeader("HTTP_X_FORWARDED_FOR");
			System.out.println("HTTP_X_FORWARDED_FOR.userIP>>"+userIP);
			userIP = StrUtils.isEmpty(userIP)?WebServletUtils.getRequest().getHeader("X-Forwarded-For"):userIP;
			System.out.println("X-Forwarded-For.userIP>>"+userIP);
			userIP = StrUtils.isEmpty(userIP)?WebServletUtils.getRequest().getHeader("Remote_Addr"):userIP;
			System.out.println("getHeader(Remote_Addr).userIP>>"+userIP);
			userIP = StrUtils.isEmpty(userIP)?WebServletUtils.getRequest().getRemoteAddr():userIP;
			System.out.println("getRequest().getRemoteAddr().userIP>>"+userIP);
			po.setUSERIP(userIP);
			po.setTXTIME(zDateHandler.getTheDateII()+" "+zDateHandler.getTheTime_MS());
			po.setOPITEM(op_type);
			po.setId(id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.debug("getEACH_USERLOG.Exception>>"+e);
			po = null;
		}
		return po;
	}
	public EACH_USERLOG getUSERLOG(String op_type , String uri){
		EACH_USERLOG userlog_po = null;
		EACH_FUNC_LIST func_list_po = null;
		func_list_po = getUsed_Func(op_type , uri);
		userlog_po = getEACH_USERLOG(op_type);
//		System.out.println("func_list_po>>"+func_list_po+", userlog_po>>"+userlog_po);
		if(func_list_po != null ){
			userlog_po.setFUNC_ID(func_list_po.getFUNC_ID());
			userlog_po.setUP_FUNC_ID(func_list_po.getUP_FUNC_ID());
			userlog_po.setFUNC_TYPE(func_list_po.getFUNC_TYPE());
		}
		return userlog_po;
	}
	
	public EACH_FUNC_LIST getUsed_Func(String op_type , String url){
		EACH_FUNC_LIST po = null;
		String funcUrl= WebServletUtils.getRequest().getRequestURI().replace(WebServletUtils.getRequest().getContextPath()+"/", "");
//		funcUrl = op_type.equals("C")? url.replace(WebServletUtils.getRequest().getContextPath()+"/", "") : funcUrl;
		funcUrl = StrUtils.isNotEmpty(url) ? url.replace(WebServletUtils.getRequest().getContextPath()+"/", "") : funcUrl;
		if(funcUrl.equals("each_user.do")){
			String tmp = WebServletUtils.getRequest().getParameter("USER_TYPE");
			funcUrl= funcUrl+"?USER_TYPE="+tmp+"&";
		}
//		role_list.do?ROLE_TYPE=A&
		if(funcUrl.equals("role_list.do")){
			String tmp = WebServletUtils.getRequest().getParameter("ROLE_TYPE");
			funcUrl= funcUrl+"?ROLE_TYPE="+tmp+"&";
		}
		System.out.println("getUsed_Func.funcUrl>>"+funcUrl);
		System.out.println("func_list_Dao>>"+func_list_Dao);
		func_list_Dao = (EACH_FUNC_LIST_Dao) (func_list_Dao !=null ?func_list_Dao : SpringAppCtxHelper.getBean("func_list_Dao"));
//		List<EACH_FUNC_LIST> list = func_list_Dao.find(" FROM tw.org.twntch.po.EACH_FUNC_LIST WHERE FUNC_URL LIKE ?  ", "%"+funcUrl+"%");
		List<EACH_FUNC_LIST> list = func_list_Dao.find(" FROM tw.org.twntch.po.EACH_FUNC_LIST WHERE FUNC_URL = ?  ", funcUrl);
		System.out.println("list>>"+list);
		if(list != null && list.size() !=0){
			for(EACH_FUNC_LIST tmppo : list){
				po = tmppo;
			}
		}
		return po;
	}
	
	

	public Map mapremove(Map map){
		if(map !=null){
			map.remove("hibernateLazyInitializer");
			map.remove("class");
			map.remove("handler");
			map.remove("multipartRequestHandler");
			map.remove("pagesize");
			map.remove("result");
			map.remove("servletWrapper");
			map.remove("scaseary");
			map.remove("target");
			map.remove("msg");
			map.remove("ac_key");
		}
		
		return map;
	}
	/**
	 * 
	 * @param map
	 * @param values
	 * @return
	 */
	public Map mapremoveII(Map map , Object... values){
		if(map !=null){
			map.remove("hibernateLazyInitializer");
			map.remove("class");
			map.remove("handler");
			map.remove("multipartRequestHandler");
			map.remove("pagesize");
			map.remove("result");
			map.remove("servletWrapper");
			map.remove("scaseary");
			map.remove("target");
			map.remove("msg");
			map.remove("ac_key");
			
			for(int i =0 ; i < values.length ; i++){
				map.remove(values[i]);
			}
		}
		return map;
	}
			
	
	public EACH_USERLOG_Dao getUserLog_Dao() {
		return userLog_Dao;
	}
	public void setUserLog_Dao(EACH_USERLOG_Dao userLog_Dao) {
		this.userLog_Dao = userLog_Dao;
	}

	public EACH_FUNC_LIST_Dao getFunc_list_Dao() {
		return func_list_Dao;
	}

	public void setFunc_list_Dao(EACH_FUNC_LIST_Dao func_list_Dao) {
		this.func_list_Dao = func_list_Dao;
	}

	public FieldsMap getFieldsmap() {
		return fieldsmap;
	}

	public void setFieldsmap(FieldsMap fieldsmap) {
		this.fieldsmap = fieldsmap;
	}
	
	
	
	
}
