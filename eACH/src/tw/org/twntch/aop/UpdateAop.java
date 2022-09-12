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
public class UpdateAop extends GenerieAop{
private EACH_USERLOG_Dao userLog_Dao ;
private EACH_FUNC_LIST_Dao func_list_Dao;
private FieldsMap fieldsmap;
	
	@Pointcut("execution(* tw.org.twntch.db.dao.hibernate.*.saveII(..)) and args(params)")
	public void p_dao_update(){
		
	}
	
//	20150901 edit by hugo req by 李建利 所有交易結果失敗，均不紀錄。
//	@Pointcut("execution(* tw.org.twntch.db.dao.hibernate.*.updateFail(..)) and args(params)")
//	public void p_updateFail(){
//		
//	}
	
	
	/**
	 * 修改
	 * @param <T>
	 * @param joinPoint
	 */
	@Around( "p_dao_update()")
	public <T> void around_saveII(ProceedingJoinPoint joinPoint){
		System.out.println("around_saveII is begin");
		EACH_FUNC_LIST func_list_po = null;
		EACH_USERLOG userlog_po = null;
		try {
			joinPoint.proceed();
			T o =   (T) Arrays.asList(joinPoint.getArgs()).get(0);
			Map newmap = BeanUtils.describe(o);
			System.out.println("tmp>>"+newmap);
			Map oldmap = (Map) Arrays.asList(joinPoint.getArgs()).get(1);
			Map pkmap = (Map) Arrays.asList(joinPoint.getArgs()).get(2);
			func_list_po = getUsed_Func("B" , "");
			userlog_po = getEACH_USERLOG("B");
			if(func_list_po != null ){
				userlog_po.setFUNC_ID(func_list_po.getFUNC_ID());
				userlog_po.setUP_FUNC_ID(func_list_po.getUP_FUNC_ID());
				userlog_po.setFUNC_TYPE(func_list_po.getFUNC_TYPE());
			}
			System.out.println("userlog_po>>"+userlog_po);
			System.out.println("oldmap>>"+oldmap);
			Map<String,Map> tmpmap = com_Dif_Val(newmap, oldmap);
			if(tmpmap.size()!=0){
				oldmap = tmpmap.get("oldmap");
				newmap = tmpmap.get("newmap");
			}
			userlog_po.setBFCHCON(restMapKey2CH(oldmap).toString());
			userlog_po.setAFCHCON(restMapKey2CH(newmap).toString());
			
			userlog_po.setADEXCODE("修改-儲存成功，PK="+restMapKey2CH(pkmap));
			userLog_Dao.aop_save(userlog_po);
			System.out.println(" after around_saveII() end " );
		} catch (Exception ee){
			System.out.println("AOP.around_saveII.Exception>>"+ee);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("around_saveII().excetion>>saveII().Throwable"+e);
			userlog_po.setADEXCODE("修改失敗，錯誤訊息:"+e.toString());
			userLog_Dao.aop_save(userlog_po);
		}
	}
	/**
	 * 修改
	 * @param <T>
	 * @param joinPoint
	 */
//	@AfterReturning( pointcut= "p_updateFail()" ,returning = "result"   )
	public <T> void ar_updateFail(JoinPoint joinPoint , Object result){
		System.out.println("ar_updateFail is begin");
		EACH_FUNC_LIST func_list_po = null;
		EACH_USERLOG userlog_po = null;
		try {
			T o =   (T) Arrays.asList(joinPoint.getArgs()).get(0);
			Map retmap =   (Map) result ;
			Map newmap = BeanUtils.describe(o);
			System.out.println("tmp>>"+newmap);
			Map oldmap = (Map) Arrays.asList(joinPoint.getArgs()).get(1);
			Map pkmap = (Map) Arrays.asList(joinPoint.getArgs()).get(2);
//			int type = (int)   Arrays.asList(joinPoint.getArgs()).get(3)  ;
//			int type =  Integer.valueOf( (String) Arrays.asList(joinPoint.getArgs()).get(4))   ;
			int type =   (Integer) Arrays.asList(joinPoint.getArgs()).get(4)  ;
			func_list_po = getUsed_Func("B" , "");
			userlog_po = getEACH_USERLOG("B");
			if(func_list_po != null ){
				userlog_po.setFUNC_ID(func_list_po.getFUNC_ID());
				userlog_po.setUP_FUNC_ID(func_list_po.getUP_FUNC_ID());
				userlog_po.setFUNC_TYPE(func_list_po.getFUNC_TYPE());
			}
			System.out.println("userlog_po>>"+userlog_po);
			System.out.println("oldmap>>"+oldmap);
			Map<String,Map> tmpmap = com_Dif_Val(newmap, oldmap);
			if(tmpmap.size()!=0){
				oldmap = tmpmap.get("oldmap");
				newmap = tmpmap.get("newmap");
			}
			userlog_po.setBFCHCON(restMapKey2CH(oldmap).toString());
			userlog_po.setAFCHCON(restMapKey2CH(newmap).toString());
			switch (type) {
			case 1:
				userlog_po.setADEXCODE( String.valueOf(retmap.get("msg")));
				break;

			default:
//				userlog_po.setADEXCODE("修改-儲存失敗，PK="+pkmap.toString());
				userlog_po.setADEXCODE("修改-儲存失敗，PK="+restMapKey2CH(pkmap).toString());
				break;
			}
			userLog_Dao.aop_save(userlog_po);
			System.out.println(" after ar_updateFail() end " );
		} catch (Exception ee){
			ee.printStackTrace();
			System.out.println("AOP.ar_updateFail.Exception>>"+ee);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("ar_updateFail().excetion>>saveII().Throwable"+e);
//			userlog_po.setADEXCODE("修改失敗，錯誤訊息:"+e.toString());
//			userLog_Dao.aop_save(userlog_po);
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
