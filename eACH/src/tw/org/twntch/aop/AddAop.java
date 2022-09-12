package tw.org.twntch.aop;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
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
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.SpringAppCtxHelper;
@Aspect
public class AddAop extends GenerieAop{
private EACH_USERLOG_Dao userLog_Dao ;
private EACH_FUNC_LIST_Dao func_list_Dao;
private FieldsMap fieldsmap;
	
	
	@Pointcut("execution(* tw.org.twntch.db.dao.hibernate.*.save(..)) and args(params)")
	public void p_save(){}
//	@Pointcut("execution(* tw.org.twntch.db.dao.hibernate.*.saveFail_PK(..)) and args(params)")
//	20150901 edit by hugo req by 李建利 所有交易結果失敗，均不紀錄。
//	@Pointcut("execution(* tw.org.twntch.db.dao.hibernate.*.saveFail(..)) and args(params)")
//	public void p_saveFail(){}
	
	
	
	
	
	/**
	 * 新增成功時寫log
	 * @param <T>
	 * @param joinPoint
	 */
	@Around( "p_save()")
	public <T> void around_save(ProceedingJoinPoint joinPoint){
		System.out.println("around_save is begin");
		EACH_FUNC_LIST func_list_po = null;
		EACH_USERLOG userlog_po = null;
		List argList = null;
		Map<String, String> map = null ;
		try {
			joinPoint.proceed();
			argList = Arrays.asList(joinPoint.getArgs());
			T o =   (T) argList.get(0);
			Map<String, String> newmap = BeanUtils.describe(o);
//			fieldsmap = (FieldsMap) (fieldsmap== null ?SpringAppCtxHelper.getBean("fieldsmap"):fieldsmap);
//			Map<String, Object> fieldMap = fieldsmap.getArgs();
//			System.out.println("fieldMap>>"+fieldMap);
			func_list_po = getUsed_Func("A" , "");
			userlog_po = getEACH_USERLOG("A");
//			System.out.println("func_list_po>>"+func_list_po+", userlog_po>>"+userlog_po);
			if(func_list_po != null ){
				userlog_po.setFUNC_ID(func_list_po.getFUNC_ID());
				userlog_po.setUP_FUNC_ID(func_list_po.getUP_FUNC_ID());
				userlog_po.setFUNC_TYPE(func_list_po.getFUNC_TYPE());
			}
			if(newmap != null ){
				map = new HashMap<String, String>();
//				for(String key : newmap.keySet()){
//					if(fieldMap.containsKey(key)){
//						map.put(fieldMap.get(key).toString(), newmap.get(key));
////						newmap.put(fieldMap.get(key).toString(), newmap.get(key));
////						newmap.remove(key); 會出錯
//					}
//				}
//				System.out.println("newmapII"+map);
//				userlog_po.setAFCHCON(JSONUtils.map2json(map));
				userlog_po.setAFCHCON(String.valueOf(restMapKey2CH(newmap)));
			}
			if(argList.size() == 1){
				userlog_po.setADEXCODE("新增成功");
			}else if(argList.size() == 2){
				Map<String, String> pkmap = (Map) argList.get(1);
				userlog_po.setADEXCODE("新增成功，PK=" + restMapKey2CH(pkmap));
			}
			userLog_Dao.aop_save(userlog_po);
			System.out.println(" after around_save() end " );
		} catch (Exception ee){
			System.out.println("AOP.around_save.Exception>>"+ee);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(e.equals( Exception.class)){
				System.out.println("新增失敗，系統異常"+e);
			}
			if(e.equals( SQLException.class)){
				System.out.println("新增失敗，資料庫語法異常"+e);
			}
			
			System.out.println("around_save().excetion>>around_save.Throwable"+e);
			userlog_po.setADEXCODE("新增失敗，錯誤訊息:"+e.toString());
			userLog_Dao.aop_save(userlog_po);
		}
	}
	/**
	 * 新增失敗時寫log
	 * @param joinPoint
	 * @param result
	 */
//	@AfterReturning( pointcut= "p_saveFail()" ,returning = "result"   )
	public <T> void ar_saveFail(JoinPoint joinPoint , Object result ){
		System.out.println("saveFail");
//		System.out.println("result>>"+result);
//		System.out.println("joinPoint>>"+joinPoint);
//		System.out.println("Agruments Passed=" + Arrays.toString(joinPoint.getArgs()));
		EACH_FUNC_LIST func_list_po = null;
		EACH_USERLOG userlog_po =  new EACH_USERLOG();
		List argList = null;
		Map map = null;
		try {
			argList = Arrays.asList(joinPoint.getArgs());
			T o =   (T) argList.get(0);
			Map<String, String> newmap = BeanUtils.describe(o);
			Map retmap =   (Map) result ; 
			fieldsmap = (FieldsMap) (fieldsmap== null ?SpringAppCtxHelper.getBean("fieldsmap"):fieldsmap);
			Map<String, Object> fieldMap = fieldsmap.getArgs();
//			System.out.println("fieldMap>>"+fieldMap);
			func_list_po = getUsed_Func("A" , "");
			userlog_po = getEACH_USERLOG("A");
//			System.out.println("func_list_po>>"+func_list_po+", userlog_po>>"+userlog_po);
			if(func_list_po != null ){
				userlog_po.setFUNC_ID(func_list_po.getFUNC_ID());
				userlog_po.setUP_FUNC_ID(func_list_po.getUP_FUNC_ID());
				userlog_po.setFUNC_TYPE(func_list_po.getFUNC_TYPE());
			}
			if(newmap != null ){
				map = new HashMap<String, String>();
				for(String key : newmap.keySet()){
					if(fieldMap.containsKey(key)){
						map.put(String.valueOf(fieldMap.get(key)) , String.valueOf(newmap.get(key)));
//						newmap.put(fieldMap.get(key).toString(), newmap.get(key));
//						newmap.remove(key); 會出錯
					}
				}
//				System.out.println("newmapII"+map);
				userlog_po.setAFCHCON(JSONUtils.map2json(map));
			}
			userlog_po.setADEXCODE(retmap.get("msg").toString());
			userLog_Dao.aop_save(userlog_po);
			System.out.println("AOP.ar_saveFail end");
		} catch (Exception ee){
			System.out.println("AOP.ar_saveFail.Exception>>"+ee);
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
