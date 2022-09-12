package tw.org.twntch.bo;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import tw.org.twntch.db.dao.hibernate.ONCLEARINGTAB_Dao;
import tw.org.twntch.po.ONCLEARINGTAB;
import tw.org.twntch.po.ONCLEARINGTAB_PK;
import tw.org.twntch.util.BeanUtils;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.StrUtils;

public class ONCLEARING_ADJ_BO {
	private Logger logger = Logger.getLogger(ONCLEARING_ADJ_BO.class.getName());
	private ONCLEARINGTAB_Dao onclearingtab_Dao ;
	private EACH_USERLOG_BO userlog_bo ;
	private EACHSYSSTATUSTAB_BO eachsysstatustab_bo;
	
	public String exeUpdate (Map<String, String> param){
		boolean result = false ;
		String json = "{}";
		String sql = "";
		ONCLEARINGTAB po = null;
		ONCLEARINGTAB_PK id = null;
		String bizdate =  StrUtils.isNotEmpty(param.get("BIZDATE")) ? DateTimeUtils.convertDate(DateTimeUtils.INTERCONVERSION , param.get("BIZDATE"), "yyyyMMdd", "yyyyMMdd") :"";
		String clearingphase =  StrUtils.isNotEmpty(param.get("CLEARINGPHASE")) ?  param.get("CLEARINGPHASE") :"全部";
		String action = StrUtils.isNotEmpty(param.get("action")) ? param.get("action"):""; 
		Map<String, String> sqlParams = new HashMap<String , String>();
		Map<String, String> retmap = new HashMap<String , String>();
		Map<String, String> msgmap = new HashMap<String , String>();
		List<ONCLEARINGTAB> addlist = new LinkedList<ONCLEARINGTAB>();
		List<Map<String,String>> list = new LinkedList<Map<String,String>>();
		int deletecount = 0;
		try {
			param.put("BIZDATE", bizdate);
			sqlParams = getSqlParams(param);
			Map map = getCondition(sqlParams);
			sql = getExe_Update_Query_SQL(map.get("sql")+"");
//			取得BH的資料+CL的颱風天日期及清算階段
			list =onclearingtab_Dao.getData(sql, (Map<String, String>) map.get("vals"));
			if(list != null && list.size() != 0){
				for(Map tmp_map:list){
					po = new ONCLEARINGTAB();
					id = new ONCLEARINGTAB_PK();
					BeanUtils.populate(id, tmp_map);
					BeanUtils.populate(po, tmp_map);
//					20151120 add by hugo 針對onclearing 有可能無資料出現的問題
					if(StrUtils.isEmpty(po.getTYPHBIZDATE()) ){
						po.setTYPHBIZDATE(bizdate);
					}
					if(StrUtils.isEmpty(po.getTYPHCLEARINGPHASE()) ){
						po.setTYPHCLEARINGPHASE(clearingphase);
					}
					po.setId(id);
					addlist.add(po);
				}
			}
			map.clear();
			sqlParams.clear();
			sqlParams = getExeUpdateSqlParams(param);
			map = getExeUpdateQueryCondition(sqlParams);
			deletecount = onclearingtab_Dao.exeUpdate( map.get("sql")+"",(Map<String, String>)map.get("vals"), addlist);
			result =true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result =false;
			logger.debug("exeUpdate.Exception>>"+e);
		}finally{
			if(result){
//				寫log 條件 {"BIZDATE:20150101 , CLEARINGPHASE : '01'"}
				retmap.put("result", "TRUE");
				retmap.put("msg", "執行成功，連線結算檔已刪除"+deletecount+"筆資料及新增"+list.size()+"筆資料");
				userlog_bo.genericLog("X", "成功，資料已更新，條件:營業日期="+bizdate+",清算階段="+clearingphase, action, retmap, null);
			}else{
				retmap.put("result", "FALSE");
				retmap.put("msg", "失敗，系統異常...");
			}
			json = JSONUtils.map2json(retmap);
		}
		
		return json;
	}
	
	public String search_toJson(Map<String, String> param){
		String json = "{}";
		String sql = "";
		Map<String, String> sqlParams = new HashMap<String , String>();
		String bizdate =  StrUtils.isNotEmpty(param.get("BIZDATE")) ? DateTimeUtils.convertDate(DateTimeUtils.INTERCONVERSION , param.get("BIZDATE"), "yyyyMMdd", "yyyyMMdd") :"";
		param.put("BIZDATE", bizdate);
		sqlParams = getSqlParams(param);
		Map map = getCondition(sqlParams);
		sql = getSQL(map.get("sql")+"");
		List list =onclearingtab_Dao.getData(sql, (Map<String, String>) map.get("vals"));
		if(list!=null && list.size()!=0){
			json = JSONUtils.toJson(list);
		}
		return json;
	}
	
	
	public String getSQL(String condition_sql ){
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT COALESCE (SUM(BH.RECVCNT+BH.PAYCNT+BH.RVSRECVCNT+BH.RVSPAYCNT) ,0) BHCNT ");
		sql.append(" , COALESCE( SUM(CL.RECVCNT+CL.PAYCNT+CL.RVSRECVCNT+CL.RVSPAYCNT)  , 0) CLCNT ");
		sql.append(" , COALESCE( SUM(BH.RECVMBFEECNT+BH.PAYMBFEECNT+BH.PAYEACHFEECNT+BH.RVSRECVMBFEECNT+BH.RVSRECVEACHFEECNT+BH.RVSPAYMBFEECNT)  , 0) BHFEECNT ");
		sql.append(" , COALESCE( SUM(CL.RECVMBFEECNT+CL.PAYMBFEECNT+CL.PAYEACHFEECNT+CL.RVSRECVMBFEECNT+CL.RVSRECVEACHFEECNT+CL.RVSPAYMBFEECNT)  , 0) CLFEECNT ");
		sql.append(" , (COALESCE (SUM(BH.RECVAMT+BH.RVSRECVAMT) , 0) - COALESCE( SUM(BH.PAYAMT+BH.RVSPAYAMT) ,0) ) BHDIFAMT ");
//		sql.append(" , (COALESCE (SUM(CL.RECVAMT+BH.RVSRECVAMT) , 0) - COALESCE( SUM(CL.PAYAMT+CL.RVSPAYAMT) , 0)) CLDIFAMT ");
		sql.append(" , (COALESCE (SUM(CL.RECVAMT+CL.RVSRECVAMT) , 0) - COALESCE( SUM(CL.PAYAMT+CL.RVSPAYAMT) , 0)) CLDIFAMT ");
		sql.append(" , (COALESCE (SUM(BH.RECVMBFEEAMT+BH.RVSRECVMBFEEAMT+BH.RVSRECVEACHFEEAMT) , 0) - COALESCE( SUM(BH.PAYMBFEEAMT+BH.RVSPAYMBFEEAMT+BH.PAYEACHFEEAMT) ,0) ) BHFEEDIFAMT ");
		sql.append(" , (COALESCE (SUM(CL.RECVMBFEEAMT+CL.RVSRECVMBFEEAMT+CL.RVSRECVEACHFEEAMT) , 0) - COALESCE( SUM(CL.PAYMBFEEAMT+CL.RVSPAYMBFEEAMT+CL.PAYEACHFEEAMT) , 0)) CLFEEDIFAMT ");
		sql.append(" , COALESCE (SUM( BH.BALANCEAMT),0) BALANCEAMT ");
		sql.append(" , COALESCE (SUM( BH.BALANCEFFAMT),0) BALANCEFFAMT ");
		sql.append(" ,( COALESCE (BH.BANKID,'') ||'-'|| COALESCE ( (SELECT BGBK_NAME FROM BANK_GROUP WHERE BGBK_ID = BH.BANKID) , '') ) BANKNAME ");
		sql.append(" , COALESCE (BH.BANKID,'') BANKID  , COALESCE (BH.BIZDATE,'') BIZDATE ,COALESCE (BH.CLEARINGPHASE,'') CLEARINGPHASE ");
		sql.append(" FROM EACHUSER.BHCLEARINGTAB BH ");
		sql.append(" FULL OUTER JOIN  ONCLEARINGTAB CL ON BH.BIZDATE =  COALESCE ( CL.TYPHBIZDATE, CL.BIZDATE) AND BH.CLEARINGPHASE = COALESCE ( CL.TYPHCLEARINGPHASE,CL.CLEARINGPHASE )  AND BH.BANKID = CL.BANKID AND BH.PCODE =CL.PCODE ");
		sql.append(condition_sql);
		sql.append(" GROUP BY BH.BANKID , BH.BIZDATE , BH.CLEARINGPHASE  ");
		sql.append(" ORDER BY BH.BANKID , BH.CLEARINGPHASE");
		logger.debug("getSQL>>"+sql);
		return sql.toString();
	}
	
	/**
	 * 
	 * @param param
	 * @return 
	 * "sql"
	 * "vals"
	 */
	public Map getCondition(Map<String,String> param){
		StringBuffer sql = new StringBuffer();
		Map retmap = new HashMap<String, Object>();
		int i = 0 ;
		for(String key :param.keySet()){
			if(i!=0){sql.append(" AND ");}
			if(StrUtils.isNotEmpty(param.get(key))){
				if(i==0){sql.append(" WHERE ");}
				sql.append( "BH."+key+" = :"+key);
				i++;
			}
		}
		retmap.put("sql", sql.toString());
		retmap.put("vals" , param);
		logger.debug("getCondition.retmap>>"+retmap);
		return retmap;
	}
	
	/**
	 * 
	 * @param param
	 * @return 
	 * "sql"
	 * "vals"
	 */
	public Map getExeUpdateQueryCondition(Map<String,String> param){
		StringBuffer sql = new StringBuffer();
		Map retmap = new HashMap<String, Object>();
		int i = 0 ;
		for(String key :param.keySet()){
			if(i!=0){sql.append(" AND ");}
			if(StrUtils.isNotEmpty(param.get(key))){
				if(i==0){sql.append(" WHERE ");}
				sql.append( key+" = :"+key);
				i++;
			}
		}
		retmap.put("sql", sql.toString());
		retmap.put("vals" , param);
		logger.debug("getExeUpdateQueryCondition.retmap>>"+retmap);
		return retmap;
	}
	
	
	public Map getSqlParams(Map<String ,String> param){
		Map retmap = new HashMap<String, Object>();
		for(String key :param.keySet()){
			if(key.equals("BIZDATE") || key.equals("CLEARINGPHASE") || key.equals("BANKID") || key.equals("TYPHBIZDATE") || key.equals("TYPHCLEARINGPHASE") ){
				if(StrUtils.isNotEmpty(param.get(key))){
					retmap.put(key, param.get(key));
				}
			}
		}
		logger.debug("getSqlParams.retmap>>"+retmap);
		return retmap;
	}
	public Map getExeUpdateSqlParams(Map<String ,String> param){
		Map retmap = new HashMap<String, Object>();
		for(String key :param.keySet()){
			if(key.equals("BIZDATE") || key.equals("CLEARINGPHASE") ){
				if(StrUtils.isNotEmpty(param.get(key))){
					retmap.put("TYPH"+key, param.get(key));
				}
			}
		}
		logger.debug("getSqlParams.retmap>>"+retmap);
		return retmap;
	}
	
	
	public Map<String ,String> getOne(String bizdate , String clearingphase ,String bankid){
		String sql = "";
		Map<String, String> retmap = new HashMap<String , String>();
		Map<String, String> sqlParams = new HashMap<String , String>();
		try {
			sqlParams.put("BIZDATE", bizdate);
			sqlParams.put("CLEARINGPHASE", clearingphase);
			sqlParams.put("BANKID", bankid);
			Map map = getCondition(sqlParams);
			sql = getOne_SQL(map.get("sql")+"");
			List<Map<String,String>> list_BH =onclearingtab_Dao.getData(sql, (Map<String, String>) map.get("vals"));
			if(list_BH !=null && list_BH.size() != 0 ){
				retmap=  list_BH.get(0);
			}
			System.out.println("CL_RECVCNT>>"+retmap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retmap;
		
	}
	
	public String getOne_SQL(String condition_sql ){
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT  ");
		sql.append(" SUM (COALESCE (BH.RECVCNT ,0)) BH_RECVCNT , SUM (COALESCE (BH.PAYCNT ,0)) BH_PAYCNT , SUM (COALESCE (BH.RECVAMT ,0)) BH_RECVAMT  , SUM (COALESCE (BH.PAYAMT ,0)) BH_PAYAMT ");
		sql.append(" , SUM (COALESCE (BH.RVSRECVCNT ,0)) BH_RVSRECVCNT, SUM (COALESCE (BH.RVSPAYCNT ,0)) BH_RVSPAYCNT, SUM (COALESCE (BH.RVSRECVAMT ,0)) BH_RVSRECVAMT, SUM (COALESCE (BH.RVSPAYAMT ,0)) BH_RVSPAYAMT ");
		sql.append(" , SUM (COALESCE (CL.RECVCNT ,0)) CL_RECVCNT , SUM (COALESCE (CL.PAYCNT ,0)) CL_PAYCNT , SUM (COALESCE (CL.RECVAMT ,0)) CL_RECVAMT  , SUM (COALESCE (CL.PAYAMT ,0)) CL_PAYAMT ");
		sql.append(" , SUM (COALESCE (CL.RVSRECVCNT ,0)) CL_RVSRECVCNT, SUM ( COALESCE (CL.RVSPAYCNT ,0)) CL_RVSPAYCNT, SUM (COALESCE (CL.RVSRECVAMT ,0)) CL_RVSRECVAMT, SUM (COALESCE (CL.RVSPAYAMT ,0)) CL_RVSPAYAMT ");
		
		sql.append(" , SUM (COALESCE (BH.RECVMBFEECNT ,0))  BH_RECVMBFEECNT, SUM (COALESCE (BH.RECVMBFEEAMT ,0))  BH_RECVMBFEEAMT, SUM (COALESCE (BH.PAYMBFEECNT ,0)) BH_PAYMBFEECNT, SUM (COALESCE (BH.PAYMBFEEAMT ,0)) BH_PAYMBFEEAMT, SUM (COALESCE (BH.PAYEACHFEECNT ,0)) BH_PAYEACHFEECNT, SUM (COALESCE (BH.PAYEACHFEEAMT ,0)) BH_PAYEACHFEEAMT ");
		sql.append(" , SUM (COALESCE (BH.RVSRECVMBFEECNT ,0)) BH_RVSRECVMBFEECNT, SUM (COALESCE (BH.RVSRECVMBFEEAMT ,0)) BH_RVSRECVMBFEEAMT, SUM (COALESCE (BH.RVSRECVEACHFEECNT ,0)) BH_RVSRECVEACHFEECNT, SUM (COALESCE (BH.RVSRECVEACHFEEAMT ,0)) BH_RVSRECVEACHFEEAMT, SUM (COALESCE (BH.RVSPAYMBFEECNT ,0)) BH_RVSPAYMBFEECNT, SUM (COALESCE (BH.RVSPAYMBFEEAMT ,0)) BH_RVSPAYMBFEEAMT  ");
		sql.append(" , SUM (COALESCE (CL.RECVMBFEECNT ,0))  CL_RECVMBFEECNT, SUM (COALESCE (CL.RECVMBFEEAMT ,0))  CL_RECVMBFEEAMT, SUM (COALESCE (CL.PAYMBFEECNT ,0)) CL_PAYMBFEECNT, SUM (COALESCE (CL.PAYMBFEEAMT ,0)) CL_PAYMBFEEAMT, SUM (COALESCE (CL.PAYEACHFEECNT ,0)) CL_PAYEACHFEECNT, SUM (COALESCE (CL.PAYEACHFEEAMT ,0)) CL_PAYEACHFEEAMT  ");
		sql.append(" , SUM (COALESCE (CL.RVSRECVMBFEECNT ,0)) CL_RVSRECVMBFEECNT, SUM (COALESCE (CL.RVSRECVMBFEEAMT ,0)) CL_RVSRECVMBFEEAMT, SUM (COALESCE (CL.RVSRECVEACHFEECNT ,0)) CL_RVSRECVEACHFEECNT, SUM (COALESCE (CL.RVSRECVEACHFEEAMT ,0)) CL_RVSRECVEACHFEEAMT, SUM (COALESCE (CL.RVSPAYMBFEECNT ,0)) CL_RVSPAYMBFEECNT, SUM (COALESCE (CL.RVSPAYMBFEEAMT ,0)) CL_RVSPAYMBFEEAMT  ");
		sql.append(" , ( COALESCE (BH.BANKID,'') ||'-'|| COALESCE ( (SELECT BGBK_NAME FROM BANK_GROUP WHERE BGBK_ID = BH.BANKID) , '') ) BANKNAME ");
		sql.append(" , COALESCE (BH.BANKID,'') BANKID  , COALESCE (BH.BIZDATE,'') BIZDATE ,COALESCE (BH.CLEARINGPHASE,'') CLEARINGPHASE  ");
		sql.append(" FROM EACHUSER.BHCLEARINGTAB BH ");
		sql.append(" FULL OUTER JOIN  ONCLEARINGTAB CL ON BH.BIZDATE =  COALESCE ( CL.TYPHBIZDATE, CL.BIZDATE) AND BH.CLEARINGPHASE = COALESCE ( CL.TYPHCLEARINGPHASE,CL.CLEARINGPHASE )  AND BH.BANKID = CL.BANKID  AND BH.PCODE =CL.PCODE  ");
		sql.append(condition_sql);
		sql.append(" GROUP BY BH.BANKID , BH.BIZDATE , BH.CLEARINGPHASE  ");
		sql.append(" ORDER BY BH.BANKID ");
		logger.debug("getOne_SQL>>"+sql);
		return sql.toString();
	}
	public String getExe_Update_Query_SQL(String condition_sql ){
		StringBuffer sql = new StringBuffer();
//
//INSERT INTO ONCLEARINGTAB ( BIZDATE,CLEARINGPHASE,BANKID,PCODE,RECVCNT,RECVAMT,PAYCNT,PAYAMT,RECVMBFEECNT,RECVMBFEEAMT,PAYMBFEECNT,PAYMBFEEAMT
//,PAYEACHFEECNT,PAYEACHFEEAMT,RVSRECVCNT,RVSRECVAMT,RVSPAYCNT,RVSPAYAMT,RVSRECVMBFEECNT,RVSRECVMBFEEAMT,RVSRECVEACHFEECNT,RVSRECVEACHFEEAMT
//,RVSPAYMBFEECNT,RVSPAYMBFEEAMT,TYPHBIZDATE,TYPHCLEARINGPHASE  )
// SELECT 
//  COALESCE (BH.BIZDATE,'') BIZDATE ,COALESCE (BH.CLEARINGPHASE,'') CLEARINGPHASE , COALESCE (BH.BANKID,'') BANKID , COALESCE (BH.PCODE,'') PCODE
// ,COALESCE (BH.RECVCNT ,0) RECVCNT , COALESCE (BH.RECVAMT ,0) RECVAMT , COALESCE (BH.PAYCNT ,0) PAYCNT   , COALESCE (BH.PAYAMT ,0) PAYAMT
//, COALESCE (BH.RECVMBFEECNT ,0) RECVMBFEECNT , COALESCE (BH.RECVMBFEEAMT ,0) RECVMBFEEAMT , COALESCE (BH.PAYMBFEEAMT ,0) PAYMBFEEAMT , COALESCE (BH.PAYMBFEEAMT ,0) PAYMBFEEAMT
//  
//, COALESCE (BH.PAYEACHFEECNT ,0)  PAYEACHFEECNT, COALESCE (BH.PAYEACHFEEAMT ,0)  PAYEACHFEEAMT, COALESCE (BH.RVSRECVCNT ,0)  RVSRECVCNT , COALESCE (BH.RVSRECVAMT ,0) RVSRECVAMT
//,  COALESCE (BH.RVSPAYCNT ,0) RVSPAYCNT, COALESCE (BH.RVSPAYAMT ,0) RVSPAYAMT, COALESCE (BH.RVSRECVMBFEECNT ,0) RVSRECVMBFEECNT, COALESCE (BH.RVSRECVMBFEEAMT ,0) RVSRECVMBFEEAMT
//, COALESCE (BH.RVSRECVEACHFEECNT ,0) RVSRECVEACHFEECNT ,COALESCE (BH.RVSRECVEACHFEEAMT ,0)  RVSRECVEACHFEEAMT
//, COALESCE (BH.RVSPAYMBFEECNT ,0) RVSPAYMBFEECNT, COALESCE (BH.RVSPAYMBFEEAMT ,0) RVSPAYMBFEEAMT, COALESCE (CL.TYPHBIZDATE ,'') TYPHBIZDATE,  COALESCE (CL.TYPHCLEARINGPHASE ,'') TYPHCLEARINGPHASE
//
// FROM EACHUSER.BHCLEARINGTAB BH 
// FULL OUTER JOIN  ONCLEARINGTAB CL ON BH.BIZDATE =  COALESCE ( CL.TYPHBIZDATE, CL.BIZDATE) AND BH.CLEARINGPHASE = COALESCE ( CL.TYPHCLEARINGPHASE,CL.CLEARINGPHASE ) AND BH.BANKID = CL.BANKID 
// WHERE BH.BIZDATE='20150703' AND BH.CLEARINGPHASE='02' -- AND BH.BANKID='4620000'
// ORDER BY BH.BANKID 
		sql.append(" SELECT  ");
		sql.append(" COALESCE (BH.BIZDATE,'') BIZDATE ,COALESCE (BH.CLEARINGPHASE,'') CLEARINGPHASE , COALESCE (BH.BANKID,'') BANKID , COALESCE (BH.PCODE,'') PCODE  ");
		sql.append(" ,COALESCE (BH.RECVCNT ,0) RECVCNT , COALESCE (BH.RECVAMT ,0) RECVAMT , COALESCE (BH.PAYCNT ,0) PAYCNT   , COALESCE (BH.PAYAMT ,0) PAYAMT  ");
		sql.append(" ,COALESCE (BH.RECVMBFEECNT ,0) RECVMBFEECNT , COALESCE (BH.RECVMBFEEAMT ,0) RECVMBFEEAMT , COALESCE (BH.PAYMBFEEAMT ,0) PAYMBFEEAMT , COALESCE (BH.PAYMBFEEAMT ,0) PAYMBFEEAMT  ");
		sql.append(" , COALESCE (BH.PAYEACHFEECNT ,0)  PAYEACHFEECNT, COALESCE (BH.PAYEACHFEEAMT ,0)  PAYEACHFEEAMT, COALESCE (BH.RVSRECVCNT ,0)  RVSRECVCNT , COALESCE (BH.RVSRECVAMT ,0) RVSRECVAMT  ");
		sql.append(" , COALESCE (BH.RVSPAYCNT ,0) RVSPAYCNT, COALESCE (BH.RVSPAYAMT ,0) RVSPAYAMT, COALESCE (BH.RVSRECVMBFEECNT ,0) RVSRECVMBFEECNT, COALESCE (BH.RVSRECVMBFEEAMT ,0) RVSRECVMBFEEAMT  ");
		sql.append(" , COALESCE (BH.RVSRECVEACHFEECNT ,0) RVSRECVEACHFEECNT ,COALESCE (BH.RVSRECVEACHFEEAMT ,0)  RVSRECVEACHFEEAMT  ");
		sql.append(" , COALESCE (BH.RVSPAYMBFEECNT ,0) RVSPAYMBFEECNT, COALESCE (BH.RVSPAYMBFEEAMT ,0) RVSPAYMBFEEAMT, COALESCE (CL.TYPHBIZDATE ,'') TYPHBIZDATE,  COALESCE (CL.TYPHCLEARINGPHASE ,'') TYPHCLEARINGPHASE  ");
		sql.append(" , COALESCE (BH.PAYMBFEECNT ,0) PAYMBFEECNT  ");
		sql.append(" FROM EACHUSER.BHCLEARINGTAB BH  ");
		sql.append(" FULL OUTER JOIN  ONCLEARINGTAB CL ON BH.BIZDATE =  COALESCE ( CL.TYPHBIZDATE, CL.BIZDATE) AND BH.CLEARINGPHASE = COALESCE ( CL.TYPHCLEARINGPHASE,CL.CLEARINGPHASE ) AND BH.BANKID = CL.BANKID  AND BH.PCODE =CL.PCODE  ");
		sql.append(condition_sql);
		sql.append(" ORDER BY BH.BANKID , BH.CLEARINGPHASE");
		logger.debug("getExe_Update_Query_SQL>>"+sql);
		return sql.toString();
	}
	
	
	
	public String getPreBizdate(){
		return eachsysstatustab_bo.getRptBusinessDate();
	}
	
	public ONCLEARINGTAB_Dao getOnclearingtab_Dao() {
		return onclearingtab_Dao;
	}
	public void setOnclearingtab_Dao(ONCLEARINGTAB_Dao onclearingtab_Dao) {
		this.onclearingtab_Dao = onclearingtab_Dao;
	}
	public EACH_USERLOG_BO getUserlog_bo() {
		return userlog_bo;
	}
	public void setUserlog_bo(EACH_USERLOG_BO userlog_bo) {
		this.userlog_bo = userlog_bo;
	}

	public EACHSYSSTATUSTAB_BO getEachsysstatustab_bo() {
		return eachsysstatustab_bo;
	}

	public void setEachsysstatustab_bo(EACHSYSSTATUSTAB_BO eachsysstatustab_bo) {
		this.eachsysstatustab_bo = eachsysstatustab_bo;
	}
	
	
	

}
