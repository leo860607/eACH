package tw.org.twntch.bo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tw.org.twntch.db.dao.hibernate.RPONBLOCKTAB_Dao;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.RptUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.zDateHandler;

public class RPTST_7_BO {
	private RPONBLOCKTAB_Dao rponblocktab_Dao;
	private BANK_GROUP_BO bank_group_bo ;
	public Map<String, String> ex_export(String opbk_id,String bizdate ,  String clearingPhase , String serchStrs , int export_type){
		return export(opbk_id, bizdate, clearingPhase, serchStrs, export_type);
	}
	
	public Map<String, String> export(String opbk_id,String bizdate ,  String clearingPhase , String serchStrs , int export_type){
		Map<String, String> rtnMap = null;
		Map<String, String> serchparams = null;
		String outputFilePath = "" , conditionKey  ="" ,file_resource = "st_7";
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
			Map map = this.getConditionData(serchparams, conditionKey);
			String sql = getSQL(map.get("sqlPath").toString(),"", "");
			List list = rponblocktab_Dao.getRptData(sql, (Map<String,String>) map.get("values"));
			int pathType = Arguments.getStringArg("ISWINOPEN").equals("Y")?RptUtils.C_PATH :RptUtils.R_PATH;
			file_resource = export_type == 2?"ex_st_7":file_resource;
			outputFilePath  = RptUtils.export(RptUtils.COLLECTION,pathType, file_resource, file_resource, params, list , export_type);
			System.out.println("RPTST_7_BO.sql>>"+sql);
			System.out.println("RPTST_7_BO.map>>"+map);
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
	
	

	public Map getConditionData(Map<String, String> params ,String conditionKey) throws Exception{
		List<String> keyList = JSONUtils.toList(conditionKey);
		Map<String,Object> retMap = new HashMap<String,Object>();
		Map<String, String> values = new HashMap<String,String>();
		StringBuffer sql = new StringBuffer(); 
		String yyyymm = "" , eyyyymm = "";
		sql.append( " WHERE COALESCE(GARBAGEDATA,'')<>'*' ");
		try {
			for(String key : keyList){
				if(params.containsKey(key) && StrUtils.isNotEmpty(params.get(key)) && !params.get(key).equals("all")){
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
			retMap.put("values", values);
			System.out.println("retMap>>"+retMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e.toString()) ; 
		}
		return retMap;
	}

	public String getSQL(String sqlPath , String spSql , String orderbySql){
		StringBuffer sql = new StringBuffer();
		sql.append(" WITH TEMP AS(  ");
		sql.append(" SELECT  * FROM RPONBLOCKTAB ");
		sql.append(sqlPath);
//		 WHERE   SUBSTR (BIZDATE , 1,6) = '201601' AND   COALESCE(GARBAGEDATA,'')<>'*'
		sql.append(" ),TEMP2 AS ( ");
		sql.append(" SELECT SENDERACQUIRE ,COUNT(*) AS TOL_CNT FROM TEMP GROUP BY SENDERACQUIRE ");
		sql.append(" ) ");
		sql.append(" ,TEMP3 AS ( ");
		sql.append(" SELECT  SENDERACQUIRE ,COUNT(*) AS SUC_CNT FROM TEMP  WHERE RESULTSTATUS = 'A' OR    (RESULTSTATUS = 'P' AND SENDERSTATUS = '2 'AND  COALESCE( ACCTCODE,'0') = '0') ");
		sql.append(" GROUP BY SENDERACQUIRE ");
		sql.append(" ) ");
//		系統錯誤發動端
		sql.append(" ,ER_TEMP1 AS ( ");
		sql.append(" SELECT  SENDERACQUIRE ,COUNT(*) AS SYSERRCNT FROM TEMP    ");
		sql.append(" WHERE ");
		sql.append(" (RESULTSTATUS = 'R' AND ERRORTYPE = '1') OR    (RESULTSTATUS = 'P' AND SENDERSTATUS = '2 'AND  COALESCE( ACCTCODE,'0') = '1') ");
		sql.append(" GROUP BY SENDERACQUIRE ");
		sql.append(" ) ");
//		系統錯誤接收端	
		sql.append(" ,ER_TEMP2 AS ( ");
		sql.append(" SELECT  SENDERACQUIRE ,COUNT(*) AS SYSERRCNT_R FROM TEMP  WHERE RESULTSTATUS ='R' AND ERRORTYPE = '2'   GROUP BY SENDERACQUIRE ");
		sql.append(" ) ");
//		帳務錯誤	
		sql.append(" ,ER_TEMP3 AS ( ");
		sql.append(" SELECT  SENDERACQUIRE ,COUNT(*) AS BSERRCNT FROM TEMP  WHERE RESULTSTATUS ='R' AND ERRORTYPE = '3'   GROUP BY SENDERACQUIRE ");
		sql.append(" ) ");
//		其它錯誤	
		sql.append(" ,ER_TEMP4 AS ( ");
		sql.append(" SELECT  SENDERACQUIRE ,COUNT(*) AS OTHERRCNT FROM TEMP  WHERE (RESULTSTATUS ='R' AND ERRORTYPE = '4' )  OR   (RESULTSTATUS = 'P' AND SENDERSTATUS = '1') ");
		sql.append(" GROUP BY SENDERACQUIRE ");
		sql.append(" ) ");
//		總錯誤筆數
		sql.append(" ,ER_TEMP5 AS ( ");
		sql.append(" SELECT  SENDERACQUIRE ,COUNT(*) AS TOLERRCNT FROM TEMP  WHERE RESULTSTATUS = 'R'  OR   (RESULTSTATUS = 'P' AND SENDERSTATUS = '1') OR    (RESULTSTATUS = 'P' AND SENDERSTATUS = '2 'AND  COALESCE( ACCTCODE,'0') = '1')  GROUP BY SENDERACQUIRE ");
		sql.append(" ) ");
//		資料聯集並計算百分比
		sql.append(" ,TEMPN AS ( ");
		sql.append(" SELECT  COALESCE (T2.SENDERACQUIRE ,'') SENDERACQUIRE,COALESCE ( T2.TOL_CNT ,0) TOL_CNT ");
		sql.append(" , COALESCE (T3.SUC_CNT,0) SUC_CNT ,COALESCE (ET1.SYSERRCNT,0) SYSERRCNT ");
		sql.append(" , (CAST ( (COALESCE (ET1.SYSERRCNT,0) * 100.0 / COALESCE ( T2.TOL_CNT ,0))  AS DECIMAL(7,2)))  SYSERR_PCT ");
		sql.append(" , COALESCE (ET2.SYSERRCNT_R,0) SYSERRCNT_R ");
		sql.append(" , (CAST ( (COALESCE (ET2.SYSERRCNT_R,0) * 100.0 / COALESCE ( T2.TOL_CNT ,0))  AS DECIMAL(7,2)))  SYSERR_R_PCT ");
		sql.append(" , COALESCE (ET3.BSERRCNT,0) BSERRCNT ");
		sql.append(" , (CAST ( (COALESCE (ET3.BSERRCNT,0) * 100.0 / COALESCE ( T2.TOL_CNT ,0))  AS DECIMAL(7,2)))  BSERR_R_PCT ");
		sql.append(" , COALESCE (ET4.OTHERRCNT,0) OTHERRCNT ");
		sql.append(" , (CAST ( (COALESCE (ET4.OTHERRCNT,0) * 100.0 / COALESCE ( T2.TOL_CNT ,0))  AS DECIMAL(7,2)))  OTHERR_R_PCT ");
		sql.append(" , COALESCE (ET5.TOLERRCNT ,0) TOLERRCNT ");
		sql.append(" , (CAST ( (COALESCE (ET5.TOLERRCNT,0) * 100.0 / COALESCE ( T2.TOL_CNT ,0))  AS DECIMAL(7,2)))  TOLERR_R_PCT ");
		sql.append(" FROM TEMP2 T2 ");
		sql.append(" LEFT  JOIN TEMP3 T3 ON T2.SENDERACQUIRE = T3.SENDERACQUIRE ");
		sql.append(" LEFT  JOIN ER_TEMP1 ET1 ON T2.SENDERACQUIRE = ET1.SENDERACQUIRE ");
		sql.append(" LEFT  JOIN ER_TEMP2 ET2 ON T2.SENDERACQUIRE = ET2.SENDERACQUIRE ");
		sql.append(" LEFT  JOIN ER_TEMP3 ET3 ON T2.SENDERACQUIRE = ET3.SENDERACQUIRE ");
		sql.append(" LEFT  JOIN ER_TEMP4 ET4 ON T2.SENDERACQUIRE = ET4.SENDERACQUIRE ");
		sql.append(" LEFT  JOIN ER_TEMP5 ET5 ON T2.SENDERACQUIRE = ET5.SENDERACQUIRE ");
		sql.append(" ) ");
		sql.append(" SELECT COALESCE (EACHUSER.GETBKNAME(TN.SENDERACQUIRE),'') BGBK_NAME , TN.* FROM TEMPN TN ORDER BY SENDERACQUIRE ");
		System.out.println("getSQL>>"+sql);
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
	
	
	
	
}
