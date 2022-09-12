package tw.org.twntch.aop;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import tw.org.twntch.bo.FieldsMap;
import tw.org.twntch.db.dao.hibernate.EACH_FUNC_LIST_Dao;
import tw.org.twntch.db.dao.hibernate.EACH_USERLOG_Dao;
import tw.org.twntch.po.EACH_FUNC_LIST;
import tw.org.twntch.po.EACH_USERLOG;
import tw.org.twntch.util.BeanUtils;
@Aspect
public class RemoveAop  extends GenerieAop{
private EACH_USERLOG_Dao userLog_Dao ;
private EACH_FUNC_LIST_Dao func_list_Dao;
private FieldsMap fieldsmap;
	
	@Pointcut("execution(* tw.org.twntch.db.dao.hibernate.*.removeII(..)) and args(params)")
	public void p_dao_remove(){}
//	20150901 edit by hugo req by 李建利 所有交易結果失敗，均不紀錄。
//	@Pointcut("execution(* tw.org.twntch.db.dao.hibernate.*.removeFail(..)) and args(params)")
//	public void p_removeFail(){
//		
//	}
	
	@Around( "p_dao_remove()")
	public <T> void around_dao_remove(ProceedingJoinPoint joinPoint){
		System.out.println("around_dao_remove is begin");
		EACH_FUNC_LIST func_list_po = null;
		EACH_USERLOG userlog_po = null;
		List<Object> paramlist = null;
		Map oldmap = null ;
		Map pkmap = null ;
		try {
			joinPoint.proceed();
			paramlist = Arrays.asList(joinPoint.getArgs());
			userlog_po = getEACH_USERLOG("D");
			if( paramlist != null && paramlist.size() > 1 ){
				T o =   (T) paramlist.get(0);
				oldmap = BeanUtils.describe(o);
				pkmap = (Map) paramlist.get(1);
			}
			func_list_po = getUsed_Func("D" , "");
			if(func_list_po != null ){
				userlog_po.setFUNC_ID(func_list_po.getFUNC_ID());
				userlog_po.setUP_FUNC_ID(func_list_po.getUP_FUNC_ID());
				userlog_po.setFUNC_TYPE(func_list_po.getFUNC_TYPE());
			}
			if(oldmap != null){
				userlog_po.setBFCHCON( String.valueOf(restMapKey2CH(oldmap)));
			}
			
			userlog_po.setADEXCODE("刪除成功，PK="+restMapKey2CH(pkmap));
			userLog_Dao.aop_save(userlog_po);
			System.out.println(" after around_dao_remove() end " );
		} catch (Exception ee){
			System.out.println("AOP.around_dao_remove.Exception>>"+ee);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("around_dao_remove().Throwable"+e);
//			userlog_po.setADEXCODE("刪除失敗，錯誤訊息:"+e.toString());
//			userLog_Dao.aop_save(userlog_po);
		}
	}
	
//	@AfterReturning( pointcut= "p_removeFail()" ,returning = "result"   )
	public <T> void ar_removeFail(JoinPoint joinPoint , Object result){
		System.out.println("ar_removeFail is begin");
		EACH_FUNC_LIST func_list_po = null;
		EACH_USERLOG userlog_po = null;
		try {
			T o =   (T) Arrays.asList(joinPoint.getArgs()).get(0);
			Map retmap =   (Map) result ;
			Map oldmap =  BeanUtils.describe(o);
			Map pkmap = (Map) Arrays.asList(joinPoint.getArgs()).get(1);
			int type =   (Integer) Arrays.asList(joinPoint.getArgs()).get(3)  ;
			func_list_po = getUsed_Func("D" , "");
			userlog_po = getEACH_USERLOG("D");
			if(func_list_po != null ){
				userlog_po.setFUNC_ID(func_list_po.getFUNC_ID());
				userlog_po.setUP_FUNC_ID(func_list_po.getUP_FUNC_ID());
				userlog_po.setFUNC_TYPE(func_list_po.getFUNC_TYPE());
			}
			System.out.println("userlog_po>>"+userlog_po);
			System.out.println("oldmap>>"+oldmap);
			userlog_po.setBFCHCON(restMapKey2CH(oldmap).toString());
			switch (type) {
			case 1:
				userlog_po.setADEXCODE( String.valueOf(retmap.get("msg")));
				break;

			default:
				userlog_po.setADEXCODE("刪除-儲存失敗，PK="+pkmap.toString());
				break;
			}
			userLog_Dao.aop_save(userlog_po);
			System.out.println(" after ar_removeFail() end " );
		} catch (Exception ee){
			ee.printStackTrace();
			System.out.println("AOP.ar_removeFail.Exception>>"+ee);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("ar_removeFail().excetion>>Throwable"+e);
//			userlog_po.setADEXCODE("刪除失敗，錯誤訊息:"+e.toString());
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
