package tw.org.twntch.aop;

import java.lang.reflect.InvocationTargetException;
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
public class OPC_Aop  extends GenerieAop{
private EACH_USERLOG_Dao userLog_Dao ;
private EACH_FUNC_LIST_Dao func_list_Dao;
private FieldsMap fieldsmap;
	
//	@Pointcut( "execution(* tw.org.twntch.socket.send(..)) and args(params)" )
	@Pointcut( "execution(* tw.org.twntch.bo.*.send(..)) and args(params)" )
	public void p_send( ){
	}
	
	@Pointcut( "execution(* tw.org.twntch.bo.*.resend(..)) and args(params)" )
	public void p_resend( ){
	}
//	@Pointcut( "execution(* tw.org.twntch.action.*.execute(..)) and args(params)" )
//	public void p_exportResult( ){
//	}
//	@Pointcut( "execution(* tw.org.twntch.bo.*.getDataByDate(..)) and args(params)" )
//	public void p_getDataByDate( ){
//	}
//	
//	@Pointcut( "execution(* tw.org.twntch.bo.*.getDataByStan(..)) and args(params)" )
//	public void p_getDataByStan( ){
//	}
	
//	@AfterReturning( pointcut= "p_exportResult()" ,returning = "result"   )
//	@After( "p_exportResult()"   )
//	public <T> void af_exportResult(JoinPoint joinPoint ){
//		System.out.println("af_exportResult is begin");
//		System.out.println("Agruments Passed=" + Arrays.toString(joinPoint.getArgs()));
//		EACH_FUNC_LIST func_list_po = null;
//		EACH_USERLOG userlog_po = null;
//		String serchStrs = "";
//		String action = "";
//		try {
//			System.out.println("af_exportResult>>"+joinPoint.getArgs());
////			Map retmap =   JSONUtils.json2map((String) result)  ;
////			System.out.println("retmap>>"+retmap);
//			System.out.println("action>>"+action);
//			func_list_po = getUsed_Func("E" , action);
//			userlog_po = getEACH_USERLOG("E");
//			if(func_list_po != null ){
//				userlog_po.setFUNC_ID(func_list_po.getFUNC_ID());
//				userlog_po.setUP_FUNC_ID(func_list_po.getUP_FUNC_ID());
//				userlog_po.setFUNC_TYPE(func_list_po.getFUNC_TYPE());
//			}
//			System.out.println("userlog_po>>"+userlog_po);
////			if(retmap.get("result").equals("TRUE")){
////				userlog_po.setADEXCODE( "報表列印 -成功 ");
////			}
////			userlog_po.setAFCHCON(""+retmap);
//			userLog_Dao.aop_save(userlog_po);
//			System.out.println(" af_exportResult end " );
//		} catch (Exception ee){
//			ee.printStackTrace();
//			System.out.println("AOP.af_exportResult.Exception>>"+ee);
//		} catch (Throwable e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			System.out.println("af_exportResult.excetion>>Throwable"+e);
//		}
//	}
	
	
	@AfterReturning( pointcut= "p_send()" ,returning = "result"   )
	public <T> void ar_send(JoinPoint joinPoint , Object result){
		System.out.println("ar_send is begin");
		EACH_FUNC_LIST func_list_po = null;
		EACH_USERLOG userlog_po = null;
		try {
			System.out.println("ar_send.getArgs>>"+joinPoint.getArgs());
			T o =   (T) Arrays.asList(joinPoint.getArgs()).get(0);
//			String retmap =   (String) result ;
			Map retmap =   JSONUtils.json2map((String) result)  ;
			System.out.println("retmap>>"+retmap);
			Map newmap = (Map) o;
			String action = (String) (newmap.get("action") == null ?"":newmap.get("action"));
			System.out.println("tmp>>"+newmap);
			System.out.println("action>>"+action);
			func_list_po = getUsed_Func("G" , action);
			userlog_po = getEACH_USERLOG("G");
			if(func_list_po != null ){
				userlog_po.setFUNC_ID(func_list_po.getFUNC_ID());
				userlog_po.setUP_FUNC_ID(func_list_po.getUP_FUNC_ID());
				userlog_po.setFUNC_TYPE(func_list_po.getFUNC_TYPE());
			}
			System.out.println("userlog_po>>"+userlog_po);
			String bgbkid = (String) newmap.get("BGBK_ID");
			String opbkid = (String) newmap.get("OPBK_ID");
			String bkid = "";
			if(StrUtils.isNotEmpty(bgbkid)){
				bkid = bgbkid;
			}else{
				bkid = opbkid;
			}
			
			if(retmap.get("result").equals("TRUE")){
				userlog_po.setADEXCODE( "送出成功，總行代號 = "+bkid);
				userlog_po.setAFCHCON("送出成功清單 : "+restMapKey2CH(retmap));
			}else{
				userlog_po.setADEXCODE( "送出失敗，總行代號 = "+bkid);
				userlog_po.setAFCHCON("送出失敗清單 : "+restMapKey2CH(retmap));
			}
			
			userLog_Dao.aop_save(userlog_po);
			System.out.println(" after ar_send() end " );
		} catch (Exception ee){
			ee.printStackTrace();
			System.out.println("AOP.ar_send.Exception>>"+ee);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("ar_send().excetion>>Throwable"+e);
		}
	}
	
	@AfterReturning( pointcut= "p_resend()" ,returning = "result"   )
	public <T> void ar_resend(JoinPoint joinPoint , Object result){
		System.out.println("ar_resend is begin");
		EACH_FUNC_LIST func_list_po = null;
		EACH_USERLOG userlog_po = null;
		StringBuffer failstr = null;
		StringBuffer sucstr = null;
		boolean results = true;
		int key = 0;
		try {
			System.out.println("ar_send.getArgs>>"+joinPoint.getArgs());
			T o =   (T) Arrays.asList(joinPoint.getArgs()).get(0);
//			String retmap =   (String) result ;
			Map<String , String> retmap =   JSONUtils.json2map((String) result)  ;
			System.out.println("retmap>>"+retmap);
			Map newmap = (Map) o;
			String action = (String) (newmap.get("action") == null ?"":newmap.get("action"));
			System.out.println("tmp>>"+newmap);
			System.out.println("action>>"+action);
			func_list_po = getUsed_Func("H" , action);
			userlog_po = getEACH_USERLOG("H");
			if(func_list_po != null ){
				userlog_po.setFUNC_ID(func_list_po.getFUNC_ID());
				userlog_po.setUP_FUNC_ID(func_list_po.getUP_FUNC_ID());
				userlog_po.setFUNC_TYPE(func_list_po.getFUNC_TYPE());
			}
			System.out.println("ar_resend>>"+userlog_po);
			if(retmap.get("opc_type") !=null){
				key = Integer.valueOf(retmap.get("opc_type"));
			}
			
			switch (key) {
			case 0:
				userlog_po = doType_0(retmap, userlog_po);
				break;
			case 1:
				userlog_po = doType_1(retmap, userlog_po);
				break;

			default:
				userlog_po = doType_0(retmap, userlog_po);
				break;
			}
			
			userLog_Dao.aop_save(userlog_po);
			System.out.println(" after ar_resend() end " );
		} catch (Exception ee){
			ee.printStackTrace();
			System.out.println("AOP.ar_resend.Exception>>"+ee);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("ar_resend().excetion>>Throwable"+e);
		}
	}
	
	public EACH_USERLOG doType_0(Map<String,String> retmap ,EACH_USERLOG userlog_po){
		StringBuffer failstr = null;
		StringBuffer sucstr = null;
		boolean results = true;
		EACH_USERLOG po = null;
		failstr = new StringBuffer();
		sucstr = new StringBuffer();
		failstr.append("送出失敗清單 : ");
		sucstr.append("送出成功清單 : ");
		
		for(String key :retmap.keySet()){
//			String json =  retmap.get(key);
//			Map<String,String> tmpmap = JSONUtils.json2map(json);
//			System.out.println("retmap.get(key)>>"+retmap.get(key));
			Map tmpmap = JSONUtils.json2map(JSONUtils.toJson(retmap.get(key)));
			if(!tmpmap.get("result").equals("TRUE")){
				results = false;
				failstr.append("{總行代號="+key+" , 交易序號 ="+tmpmap.get("STAN")+"},");
			}else{
				sucstr.append("{總行代號="+key+" , 交易序號 ="+tmpmap.get("STAN")+"},");
			}
		}
		if(results){
			userlog_po.setADEXCODE( "重送成功");
			userlog_po.setAFCHCON(sucstr.toString());
		}else{
			userlog_po.setADEXCODE( "重送失敗");
			userlog_po.setAFCHCON(failstr.toString());
		}
		return userlog_po;
	}
	public EACH_USERLOG doType_1(Map<String,String> retmap ,EACH_USERLOG userlog_po){
		StringBuffer failstr = null;
		StringBuffer sucstr = null;
		boolean results = true;
		EACH_USERLOG po = null;
		failstr = new StringBuffer();
		sucstr = new StringBuffer();
		failstr.append("送出失敗清單 : ");
		sucstr.append("送出成功清單 : ");
		
//		for(String key :retmap.keySet()){
//			String json =  retmap.get(key);
//			Map<String,String> tmpmap = JSONUtils.json2map(json);
//			System.out.println("retmap.get(key)>>"+retmap.get(key));
//			Map tmpmap = JSONUtils.json2map(JSONUtils.toJson(retmap.get(key)));
			if(!retmap.get("result").equals("TRUE")){
				results = false;
//			20150616 note by hugo 這邊的STAN最後都塞到WEBTRACENO 所以應該叫...管理網站追蹤序號??? (0.0)a
				failstr.append("{ 交易序號 ="+retmap.get("AOP_SATN")+ " , 重送操作行清單="+retmap.get("RESEND_OPBK_ID")+"},");
			}else{
				sucstr.append("{  交易序號 ="+retmap.get("AOP_SATN")+ " , 重送操作行清單="+retmap.get("RESEND_OPBK_ID")+"},");
			}
//		}
		if(results){
			userlog_po.setADEXCODE( "重送成功");
			userlog_po.setAFCHCON(sucstr.toString());
		}else{
			userlog_po.setADEXCODE( "重送失敗");
			userlog_po.setAFCHCON(failstr.toString());
		}
		return userlog_po;
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
