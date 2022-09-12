package tw.org.twntch.aop;

import java.lang.reflect.InvocationTargetException;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import com.google.gson.Gson;

import tw.org.twntch.bo.Arguments;
import tw.org.twntch.bo.FieldsMap;
import tw.org.twntch.db.dao.hibernate.EACH_FUNC_LIST_Dao;
import tw.org.twntch.db.dao.hibernate.EACH_USERLOG_Dao;
import tw.org.twntch.form.Each_User_Form;
import tw.org.twntch.form.Login_Form;
import tw.org.twntch.po.EACH_FUNC_LIST;
import tw.org.twntch.po.EACH_USERLOG;
import tw.org.twntch.po.EACH_USERLOG_PK;
import tw.org.twntch.util.BeanUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.SpringAppCtxHelper;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.WebServletUtils;
import tw.org.twntch.util.zDateHandler;
@Aspect
public class SearchAop  extends GenerieAop{
private EACH_USERLOG_Dao userLog_Dao ;
private EACH_FUNC_LIST_Dao func_list_Dao;
private FieldsMap fieldsmap;
	
//	@Pointcut("execution(* tw.org.twntch.bo.*.search_toJson(..))")
//	public void p_searchtojson(){
//		System.out.println("查詢");
//	}
	@Pointcut( "execution(* tw.org.twntch.bo.*.search_toJson(..)) and args(params)" )
//	@Pointcut( "execution(* tw.org.twntch.bo.*.*_search_toJson(..)) " )
	public void p_searchtojson( ){
	}
	@Pointcut( "execution(* tw.org.twntch.bo.*.pageSearch(..)) and args(params)" )
	public void p_pageSearch( ){
	}
	
	
	
	

	
//	@After(argNames="p_searchtojson()", value = "val")
	@After("p_searchtojson()")
	public void af_searchtojson(JoinPoint joinPoint){
		System.out.println("af_searchtojson is begin");
		System.out.println("Agruments Passed=" + Arrays.toString(joinPoint.getArgs()));
		EACH_FUNC_LIST func_list_po = null;
		EACH_USERLOG userlog_po = null;
		String serchStrs = "";
		String action = "";
		Gson gs = null;
		Map tmpmap = null;
		List<Map> tmpList = null ;
		try {
			System.out.println("Agruments Passed=" + Arrays.toString(joinPoint.getArgs()));
			Map<String , String> searchMap = (Map) Arrays.asList(joinPoint.getArgs()).get(0);
			System.out.println("searchMap=" + searchMap);
			serchStrs = StrUtils.isEmpty(searchMap.get("serchStrs")) ? "" :searchMap.get("serchStrs") ;
			serchStrs = URLDecoder.decode(serchStrs,"UTF-8");
			gs = new Gson();
			System.out.println("serchStrs>>"+serchStrs);
//			Map tmpmap =  JSONUtils.json2map(serchStrs) ;
			if(serchStrs.indexOf("[")>=0){
				tmpList = gs.fromJson(serchStrs, List.class);
				tmpmap = tmpList.get(0);
			}else{
				tmpmap =  gs.fromJson(serchStrs, Map.class) ;
			}
				
			String s_is_record ="";
			String isSearch = StrUtils.isEmpty(searchMap.get("isSearch"))?"Y": searchMap.get("isSearch").toString();
			Login_Form login_form = (Login_Form) WebServletUtils.getRequest().getSession().getAttribute("login_form");
			s_is_record = login_form.getUserData().getS_is_record();
			System.out.println("isSearch ="+isSearch+" ,s_is_record ="+s_is_record);
			if(StrUtils.isNotEmpty(isSearch) && StrUtils.isNotEmpty(s_is_record) && isSearch.equals("Y") && s_is_record.equals("Y")){
				
				action = (String) (tmpmap.get("action") == null ? "" :tmpmap.get("action")) ;
//			action = StrUtils.isEmpty(searchMap.get("action")) ? "" :searchMap.get("action") ;
				System.out.println("serchStrs>>"+serchStrs);
				System.out.println("actioin>>"+action);
				func_list_po = getUsed_Func("C" , action);
				userlog_po = getEACH_USERLOG("C");
				if(func_list_po!=null){
					userlog_po.setAFCHCON( serchStrs);
					userlog_po.setFUNC_ID(func_list_po.getFUNC_ID());
					userlog_po.setUP_FUNC_ID(func_list_po.getUP_FUNC_ID());
					userlog_po.setFUNC_TYPE(func_list_po.getFUNC_TYPE());
				}
				userlog_po.setADEXCODE("查詢成功");
				userlog_po.setAFCHCON("查詢條件 : "+restMapKey2CH(tmpmap));
				userLog_Dao.aop_save(userlog_po);
				System.out.println(" after around_searchtojson() end " );
			}
			
			
		} catch (Exception ee){
			System.out.println("AOP.around_searchtojson.Exception>>"+ee);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("around_searchtojson().Throwable"+e);
			userlog_po.setADEXCODE("查詢失敗，錯誤訊息:"+e.toString());
			userlog_po.setAFCHCON("查詢條件 : "+serchStrs);
			userLog_Dao.aop_save(userlog_po);
		}
	}
	@After("p_pageSearch()")
	public void af_pageSearch(JoinPoint joinPoint){
		System.out.println("af_pageSearch is begin");
		System.out.println("Agruments Passed=" + Arrays.toString(joinPoint.getArgs()));
		EACH_FUNC_LIST func_list_po = null;
		EACH_USERLOG userlog_po = null;
		String serchStrs = "";
		String action = "";
		try {
			System.out.println("Agruments Passed=" + Arrays.toString(joinPoint.getArgs()));
			Map<String , String> searchMap = (Map) Arrays.asList(joinPoint.getArgs()).get(0);
			System.out.println("searchMap=" + searchMap);
			serchStrs = StrUtils.isEmpty(searchMap.get("serchStrs")) ? "" :searchMap.get("serchStrs") ;
			Map tmpmap =  JSONUtils.json2map(serchStrs) ;
			String s_is_record ="";
			String isSearch = StrUtils.isEmpty(searchMap.get("isSearch"))?"Y": searchMap.get("isSearch").toString();
			Login_Form login_form = (Login_Form) WebServletUtils.getRequest().getSession().getAttribute("login_form");
			s_is_record = login_form.getUserData().getS_is_record();
			System.out.println("s_is_record>>"+s_is_record);
			System.out.println("isSearch>>"+isSearch);
			System.out.println("is_record>>"+(StrUtils.isNotEmpty(isSearch) && StrUtils.isNotEmpty(s_is_record) && (isSearch.equals("Y") && s_is_record.equals("Y") )));
			if(StrUtils.isNotEmpty(isSearch) && StrUtils.isNotEmpty(s_is_record) && (isSearch.equals("Y") && s_is_record.equals("Y") ) ){
				action = (String) (tmpmap.get("action") == null ? "" :tmpmap.get("action")) ;
				System.out.println("serchStrs>>"+serchStrs);
				System.out.println("actioin>>"+action);
				func_list_po = getUsed_Func("C" , action);
				userlog_po = getEACH_USERLOG("C");
				if(func_list_po!=null){
					userlog_po.setFUNC_ID(func_list_po.getFUNC_ID());
					userlog_po.setUP_FUNC_ID(func_list_po.getUP_FUNC_ID());
					userlog_po.setFUNC_TYPE(func_list_po.getFUNC_TYPE());
				}
				userlog_po.setADEXCODE("查詢成功");
				userlog_po.setAFCHCON("查詢條件 : "+restMapKey2CH(tmpmap));
				userLog_Dao.aop_save(userlog_po);
				System.out.println(" after af_pageSearch() end " );
			}
		} catch (Exception ee){
			System.out.println("AOP.af_pageSearch.Exception>>"+ee);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("af_pageSearch().Throwable"+e);
			userlog_po.setADEXCODE("查詢失敗，錯誤訊息:"+e.toString());
			userlog_po.setAFCHCON("查詢條件 : "+serchStrs);
			userLog_Dao.aop_save(userlog_po);
		}
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
