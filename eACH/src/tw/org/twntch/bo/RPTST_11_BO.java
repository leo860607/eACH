package tw.org.twntch.bo;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tw.org.twntch.db.dao.hibernate.BANK_OPBK_Dao;
import tw.org.twntch.db.dao.hibernate.RPONBLOCKTAB_Dao;
import tw.org.twntch.po.BANK_OPBK;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.RptUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.zDateHandler;

public class RPTST_11_BO {
	private RPONBLOCKTAB_Dao rponblocktab_Dao;
	private BANK_GROUP_BO bank_group_bo ;
	private BANK_OPBK_Dao bank_opbk_Dao;
	
	public Map<String, String> ex_export(String opbk_id,String bizdate ,  String clearingPhase , String serchStrs , int export_type){
		return export(opbk_id, bizdate, clearingPhase, serchStrs, export_type);
	}
	
	public Map<String, String> export(String opbk_id,String bizdate ,  String clearingPhase , String serchStrs , int export_type){
		Map<String, String> rtnMap = null;
		Map<String, String> serchparams = null;
		String outputFilePath = "" , conditionKey  ="" ,file_resource = "st_11";
		BigDecimal v_tolcnt = null , v_txamt = null;
		try{
			conditionKey = "[\"TW_YEAR\" , \"START_YEAR\" ,\"CLEARINGPHASE\" ,\"RESULT_TYPE\" , \"OPBK_ID\"]";
			rtnMap = new HashMap<String, String>();
			rtnMap.put("serchStrs", serchStrs);
			Map<String, Object> params = new HashMap<String, Object>();
//			SQL查詢條件 區塊
			if(StrUtils.isNotEmpty(serchStrs)){
				serchparams = JSONUtils.json2map(serchStrs);
			}
			params.putAll(serchparams);
//			報表顯示區塊
			params.put("V_TXDT",bizdate);
			params.put("V_OPBK_ID",StrUtils.isEmpty(serchparams.get("OPBK_ID"))?"全部": serchparams.get("OPBK_ID"));
			params.put("V_CLEARINGPHASE", StrUtils.isEmpty(clearingPhase)?"全部": clearingPhase );
			params.put("V_PRINT_DATE",DateTimeUtils.convertDate(DateTimeUtils.INTERCONVERSION, zDateHandler.getTheDate(), "yyyy/MM/dd", "yyy/MM/dd"));
			params.put("V_PRINT_TIME",zDateHandler.getTheTime());
//			params.put("V_RESULT_TYPE",StrUtils.isEmpty(serchparams.get("RESULT_TYPE"))?"全部": serchparams.get("RESULT_TYPE"));
			params.put("V_RESULT_TYPE", getResultType2CH (serchparams.get("RESULT_TYPE")));
			Map map = this.getConditionData(serchparams, conditionKey);
//			TODO 還要查詢交易總筆數、總金額
			String cntsql = getCNTSQL(map.get("sqlPath").toString(),map.get("sqlPath2").toString(), "");
			List<Map> cntlist = rponblocktab_Dao.getRptData(cntsql, (Map<String,String>) map.get("values"));
			if(cntlist!=null && cntlist.size()!=0){
				v_tolcnt =  new BigDecimal( (Integer) cntlist.get(0).get("TOLCNT")) ;
				v_txamt =  new BigDecimal(cntlist.get(0).get("TXAMT").toString()) ;
			}
			
			params.put("V_TOLCNT",v_tolcnt);
			params.put("V_TXAMT",v_txamt);
			if(StrUtils.isEmpty(opbk_id)){
				params.put("V_OPT_BANK", "全部");
			}else{
				List<BANK_OPBK> bank_opbk = bank_opbk_Dao.getDataByBgbkId(opbk_id, "");
				String opbk_name = "";
				for (int i = 0; i < bank_opbk.size(); i++) {
					opbk_name = bank_opbk.get(i).getOPBK_NAME();
				}
				params.put("V_OPT_BANK", opbk_name);
			}
			
			String sql = getSQL(map.get("sqlPath").toString(),map.get("sqlPath2").toString(), "");
			List list = rponblocktab_Dao.getRptData(sql, (Map<String,String>) map.get("values"));
			int pathType = Arguments.getStringArg("ISWINOPEN").equals("Y")?RptUtils.C_PATH :RptUtils.R_PATH;
			file_resource = export_type == 2?"ex_st_11":file_resource;
			outputFilePath  = RptUtils.export(RptUtils.COLLECTION,pathType, file_resource, file_resource, params, list , export_type);
			System.out.println("RPTST_11_BO.sql>>"+sql);
			System.out.println("RPTST_11_BO.map>>"+map);
			System.out.println("params >> " + params);
			System.out.println("params >> " + serchparams);
			if(StrUtils.isNotEmpty(outputFilePath)){
				rtnMap.put("result", "TRUE");
				rtnMap.put("msg", outputFilePath);
			}else{
				rtnMap.put("result", "FALSE");
				rtnMap.put("msg", "產生失敗!");
			}
		}catch(Exception e){
			e.printStackTrace();
			rtnMap.put("result", "FALSE");
			rtnMap.put("msg", "產生錯誤!");
		}
		
		return rtnMap;
	}
	
	public String getResultType2CH(String result_type){
		String ret ="";
		
//		if(StrUtils.isEmpty(result_type)){
//			return ret;
//		}
		
		if(result_type.equals("")){
			ret = "全部";
		}
		if(result_type.equals("A")){
			ret = "成功";
		}
		if(result_type.equals("ER1")){
			ret = "系統錯誤";
		}
		if(result_type.equals("ER3")){
			ret = "帳務錯誤";
		}
		if(result_type.equals("ER4")){
			ret = "其它錯誤";
		}
		
		return ret;
				
	}

	public Map getConditionData(Map<String, String> params ,String conditionKey) throws Exception{
		List<String> keyList = JSONUtils.toList(conditionKey);
		Map<String,Object> retMap = new HashMap<String,Object>();
		Map<String, String> values = new HashMap<String,String>();
		StringBuffer sql = new StringBuffer(); 
		StringBuffer sql2 = new StringBuffer(); 
		String yyyymm = "" , eyyyymm = "";
		sql.append( " WHERE COALESCE(GARBAGEDATA,'')<>'*' ");
		sql2.append( " WHERE 1=1 ");
//		COALESCE(GARBAGEDATA,'')<>'*'
		try {
			for(String key : keyList){
				if(params.containsKey(key) && StrUtils.isNotEmpty(params.get(key)) && !params.get(key).equals("all")){
					
					if(key.equals("RESULT_TYPE")){
						sql2.append( " AND ");
						sql2.append(key+" = :"+key);
						values.put(key,params.get(key));
						continue;
					}
					if(key.equals("TW_YEAR")){
						yyyymm = DateTimeUtils.convertDate(params.get(key), "yyyyMM", "yyyyMM");
						sql.append( " AND ");
						sql.append(" SUBSTR (BIZDATE , 1,6) = :yyyymm");
						values.put("yyyymm",yyyymm);
						continue;
					}
					if(key.equals("START_YEAR")){
						yyyymm = DateTimeUtils.convertDate(params.get("START_YEAR")+params.get("START_MONTH"), "yyyyMM", "yyyyMM");
						eyyyymm =  DateTimeUtils.convertDate(params.get("END_YEAR")+params.get("END_MONTH"), "yyyyMM", "yyyyMM");
						sql.append( " AND ");
						sql.append("SUBSTR (BIZDATE , 1,6) >= :syyyymm AND SUBSTR (BIZDATE , 1,6) <= :eyyyymm" );
						values.put("syyyymm",yyyymm);
						values.put("eyyyymm",eyyyymm);
						continue;
					}
					if(key.equals("OPBK_ID")){
						sql.append( " AND ");
						sql.append(" SENDERACQUIRE = :"+key );
						values.put(key,params.get(key));
						continue;
					}
					sql.append( " AND ");
					sql.append( key+" = :"+key);
					values.put(key,params.get(key));
				}
			}
			
			retMap.put("sqlPath", sql.toString());
			retMap.put("sqlPath2", sql2.toString());
			retMap.put("values", values);
			System.out.println("retMap>>"+retMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e.toString()) ; 
		}
		return retMap;
	}

	public String getSQL(String sqlPath , String sqlPath2 , String orderbySql){
		StringBuffer sql = new StringBuffer();
		sql.append(" WITH TEMP AS(  ");
		sql.append(" SELECT  (CASE WHEN  RESULTSTATUS = 'A' OR    (RESULTSTATUS = 'P' AND SENDERSTATUS = '2 'AND  COALESCE( ACCTCODE,'0') = '0') THEN 'A' ");
//		ERRORTYPE 1、2 都歸類為系統錯誤
		sql.append(" WHEN  (RESULTSTATUS = 'R' AND ERRORTYPE = '1')   OR    (RESULTSTATUS = 'P' AND SENDERSTATUS = '2 'AND  COALESCE( ACCTCODE,'0') = '1') THEN 'ER1' ");
		sql.append(" WHEN  RESULTSTATUS ='R' AND ERRORTYPE = '2'  THEN 'ER1' ");
		sql.append(" WHEN  RESULTSTATUS ='R' AND ERRORTYPE = '3'  THEN 'ER3' ");
		sql.append(" WHEN  (RESULTSTATUS ='R' AND ERRORTYPE = '4' )  OR   (RESULTSTATUS = 'P' AND SENDERSTATUS = '1') THEN 'ER4' ");
		sql.append(" ELSE  'X'  END ) AS RESULT_TYPE ");
		sql.append(" ,COALESCE( CASE WHEN (RESULTSTATUS = 'P' AND SENDERSTATUS = '2 'AND  COALESCE( ACCTCODE,'0') = '1') THEN '3699' ELSE  ROB.RC2 END,'') FRC ");
		sql.append(" , ROB.*  ");
		sql.append(" FROM RPONBLOCKTAB  ROB  ");
		sql.append(sqlPath);
		sql.append(" ),TEMP2 AS ( ");
		sql.append(" SELECT FRC ,COUNT(*) AS ERR_CNT  , SUM(TXAMT) AS TXAMT , RESULT_TYPE  FROM TEMP  ");
		sql.append(sqlPath2);
		sql.append(" GROUP BY FRC,RESULT_TYPE ");
		sql.append(" ) ");
		sql.append(" SELECT (CASE WHEN T2.FRC = '3699'  THEN '未完成交易回覆失敗'  ELSE COALESCE((SELECT  ERR_DESC  FROM TXN_ERROR_CODE WHERE T2.FRC = ERROR_ID),'') END ) AS FRC_NAME  ,T2.*    ");
		sql.append(" FROM TEMP2 T2 ORDER BY RESULT_TYPE , FRC ");
		System.out.println("getSQL>>"+sql);
		return sql.toString();
	}
	
	public String getCNTSQL(String sqlPath , String sqlPath2 , String orderbySql){
		StringBuffer sql = new StringBuffer();
		sql.append(" WITH TEMP AS(  ");
		sql.append(" SELECT  (CASE WHEN  RESULTSTATUS = 'A' OR    (RESULTSTATUS = 'P' AND SENDERSTATUS = '2 'AND  COALESCE( ACCTCODE,'0') = '0') THEN 'A' ");
//		ERRORTYPE 1、2 都歸類為系統錯誤
		sql.append(" WHEN  (RESULTSTATUS = 'R' AND ERRORTYPE = '1')   OR    (RESULTSTATUS = 'P' AND SENDERSTATUS = '2 'AND  COALESCE( ACCTCODE,'0') = '1') THEN 'ER1' ");
		sql.append(" WHEN  RESULTSTATUS ='R' AND ERRORTYPE = '2'  THEN 'ER1' ");
		sql.append(" WHEN  RESULTSTATUS ='R' AND ERRORTYPE = '3'  THEN 'ER3' ");
		sql.append(" WHEN  (RESULTSTATUS ='R' AND ERRORTYPE = '4' )  OR   (RESULTSTATUS = 'P' AND SENDERSTATUS = '1') THEN 'ER4' ");
		sql.append(" ELSE  'X'  END ) AS RESULT_TYPE , ROB.*  ");
		sql.append(" FROM RPONBLOCKTAB  ROB  ");
		sql.append(sqlPath);
		sql.append(" ),TEMP2 AS ( ");
		sql.append(" SELECT PCODE ,COUNT(*) AS ERR_CNT  , SUM(TXAMT) AS TXAMT , RESULT_TYPE  FROM TEMP  ");
		sql.append(sqlPath2);
		sql.append(" GROUP BY PCODE,RESULT_TYPE ");
		sql.append(" ) ");
		sql.append(" SELECT COUNT(*)  AS TOLCNT , COALESCE(SUM(TXAMT),0) TXAMT FROM TEMP  ");
		System.out.println("getSQL"+sql);
		return sql.toString();
	}


	public RPONBLOCKTAB_Dao getRponblocktab_Dao() {
		return rponblocktab_Dao;
	}


	public void setRponblocktab_Dao(RPONBLOCKTAB_Dao rponblocktab_Dao) {
		this.rponblocktab_Dao = rponblocktab_Dao;
	}

	public BANK_GROUP_BO getBank_group_bo() {
		return bank_group_bo;
	}

	public void setBank_group_bo(BANK_GROUP_BO bank_group_bo) {
		this.bank_group_bo = bank_group_bo;
	}

	public BANK_OPBK_Dao getBank_opbk_Dao() {
		return bank_opbk_Dao;
	}

	public void setBank_opbk_Dao(BANK_OPBK_Dao bank_opbk_Dao) {
		this.bank_opbk_Dao = bank_opbk_Dao;
	}
	
}
