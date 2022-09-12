package tw.org.twntch.aop;

import java.util.Map;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import tw.org.twntch.bo.FieldsMap;
import tw.org.twntch.db.dao.hibernate.EACH_FUNC_LIST_Dao;
import tw.org.twntch.db.dao.hibernate.EACH_USERLOG_Dao;
import tw.org.twntch.po.EACH_FUNC_LIST;
import tw.org.twntch.po.EACH_USERLOG;
import tw.org.twntch.util.JSONUtils;

@Aspect
public class Qs_Ex_Export_Aop extends GenerieAop {
	private EACH_USERLOG_Dao userLog_Dao ;
	private EACH_FUNC_LIST_Dao func_list_Dao;
	private FieldsMap fieldsmap;
	
	@Pointcut( "execution(* tw.org.twntch.bo.*.qs_ex_export(..)) and args(params)" )
	public void p_qs_ex_exportResult( ){
	}

	@AfterReturning(pointcut= "p_qs_ex_exportResult()" ,returning = "result")
	public <T> void af_qs_ex_exportResult(JoinPoint joinPoint, Object result){
		EACH_FUNC_LIST func_list_po = null;
		EACH_USERLOG userlog_po = null;
		try {
			Map retmap = (Map) result;
			func_list_po = getUsed_Func("2", "");
			userlog_po = getEACH_USERLOG("2");
			if(func_list_po != null ){
				userlog_po.setFUNC_ID(func_list_po.getFUNC_ID());
				userlog_po.setUP_FUNC_ID(func_list_po.getUP_FUNC_ID());
				userlog_po.setFUNC_TYPE(func_list_po.getFUNC_TYPE());
			}
			System.out.println("userlog_po>>"+userlog_po);
			if(retmap.get("result").equals("TRUE")){
				userlog_po.setADEXCODE( "成功 ");
			}else{
				userlog_po.setADEXCODE( "失敗 ");
			}
			Map serchmap = JSONUtils.json2map(String.valueOf(retmap.get("serchStrs")));
			userlog_po.setAFCHCON(""+restMapKey2CH(serchmap));
			userLog_Dao.aop_save(userlog_po);
			System.out.println(" af_exportResult end " );
		} catch (Exception ee){
			ee.printStackTrace();
			System.out.println("AOP.af_exportResult.Exception>>"+ee);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("af_exportResult.excetion>>Throwable"+e);
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
