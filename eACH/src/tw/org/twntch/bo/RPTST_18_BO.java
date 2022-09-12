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

public class RPTST_18_BO {
	private AGENT_SEND_PROFILE_BO agent_send_profile_bo;
	private RPONBLOCKTAB_Dao rponblocktab_Dao;
	
	public Map<String, String> ex_export(String agent_company_id, String snd_company_id,String bizdate ,  String clearingPhase , String serchStrs , int export_type){
		return export(agent_company_id, snd_company_id, bizdate, clearingPhase, serchStrs, export_type);
	}
	
	public Map<String, String> export(String agent_company_id, String snd_company_id,String bizdate ,  String clearingPhase , String serchStrs , int export_type){
		Map<String, String> rtnMap = null;
		Map<String, String> serchparams = null;
		String outputFilePath = "" , conditionKey  ="" ,file_resource = "st_18";
		try{
			conditionKey = "[\"BIZDATE\" , \"SBIZDATE\" ,\"TXID\",\"AGENT_COMPANY_ID\",\"SND_COMPANY_ID\",\"CLEARINGPHASE\" ,\"TG_RESULT\"]";
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
			params.put("V_CLEARINGPHASE", StrUtils.isEmpty(clearingPhase)?"全部": clearingPhase );
			params.put("V_PRINT_DATE",DateTimeUtils.convertDate(DateTimeUtils.INTERCONVERSION, zDateHandler.getTheDate(), "yyyy/MM/dd", "yyy/MM/dd"));
			params.put("V_PRINT_TIME",zDateHandler.getTheTime());
			params.put("V_AGENT_COMPANY_ID",StrUtils.isEmpty(serchparams.get("AGENT_COMPANY_ID"))?"全部": serchparams.get("AGENT_COMPANY_ID"));
			params.put("V_SND_COMPANY_ID",StrUtils.isEmpty(serchparams.get("SND_COMPANY_ID"))?"全部": serchparams.get("SND_COMPANY_ID"));
			params.put("V_TG_RESULT",serchparams.get("TG_RESULT"));
			params.put("V_TXID",serchparams.get("TXID"));
			Map map = this.getConditionData(serchparams, conditionKey);
			String sql = getSQL(map.get("sqlPath").toString(),"", "");
			List list = rponblocktab_Dao.getRptData(sql, (Map<String,String>) map.get("values"));
			int pathType = Arguments.getStringArg("ISWINOPEN").equals("Y")?RptUtils.C_PATH :RptUtils.R_PATH;
			file_resource = export_type == 2?"ex_st_18":file_resource;
			outputFilePath  = RptUtils.export(RptUtils.COLLECTION,pathType, file_resource, file_resource, params, list , export_type);
			System.out.println("RPTST_18_BO.sql>>"+sql);
			System.out.println("RPTST_18_BO.map>>"+map);
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
		sql.append( " WHERE 1=1 ");
		try {
			for(String key : keyList){
				if(params.containsKey(key) && StrUtils.isNotEmpty(params.get(key)) && !params.get(key).equals("all")){
					if(key.equals("BIZDATE")){
						sql.append( " AND ");
						sql.append(key+" = :"+key);
						values.put(key,DateTimeUtils.convertDate(params.get(key), "yyyyMMdd", "yyyyMMdd"));
						continue;
					}
					if(key.equals("SBIZDATE")){
						sql.append( " AND ");
						sql.append("BIZDATE >= :"+key+" AND BIZDATE<= :EBIZDATE" );
						values.put(key,DateTimeUtils.convertDate(params.get(key), "yyyyMMdd", "yyyyMMdd"));
						values.put("EBIZDATE",DateTimeUtils.convertDate(params.get("EBIZDATE"), "yyyymmdd", "yyyymmdd"));
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
		sql.append(" SELECT AGENT_COMPANY_ID,AGENT_COMPANY_ABBR_NAME,SND_COMPANY_ID, SND_COMPANY_ABBR_NAME,CNT,BIZDATE,CLEARINGPHASE,AGENT_FEE,SUM_AGENT_FEE,AMT ");
		sql.append(" ,TXID,COALESCE ((SELECT TXN_NAME FROM TXN_CODE TC WHERE TC.TXN_ID = TXID),'') AS TXN_NAME ");
		sql.append(" ,PCODE,(SELECT EACH_TXN_NAME FROM EACH_TXN_CODE ETC WHERE ETC.EACH_TXN_ID = PCODE) AS PCODE_NAME ");
		sql.append(" ,(CASE WHEN TG_RESULT = 'A' THEN '成功' ELSE '失敗' END) AS TG_RESULT_CH ,TG_RESULT ");
		sql.append(" FROM RP_DAILY_TXNLOG  ");
		sql.append(sqlPath);
		sql.append(" ORDER BY AGENT_COMPANY_ID,SND_COMPANY_ID,TXID ");
		sql.append(" ) ");
		sql.append(" , TEMP2 AS( ");
		sql.append(" SELECT AGENT_COMPANY_ID,SND_COMPANY_ID,BIZDATE,CLEARINGPHASE,TXID,SUM(AMT) AS AMT,SUM(CNT) AS CNT ");
		sql.append(" ,(CASE WHEN TG_RESULT = 'A' THEN '成功' ELSE '失敗' END) AS TG_RESULT ");
		sql.append(" FROM TEMP WHERE TG_RESULT = 'A' ");
		sql.append(" GROUP BY  AGENT_COMPANY_ID,SND_COMPANY_ID,BIZDATE,CLEARINGPHASE,TXID,TG_RESULT ");
		sql.append(" ) ");
		sql.append(" , TEMP3 AS( ");
		sql.append(" SELECT AGENT_COMPANY_ID,SND_COMPANY_ID,BIZDATE,CLEARINGPHASE,TXID,SUM(AMT) AS AMT,SUM(CNT) AS CNT ");
		sql.append(" ,(CASE WHEN TG_RESULT = 'A' THEN '成功' ELSE '失敗' END) AS TG_RESULT ");
		sql.append(" FROM TEMP WHERE TG_RESULT = 'R' ");
		sql.append(" GROUP BY  AGENT_COMPANY_ID,SND_COMPANY_ID,BIZDATE,CLEARINGPHASE,TXID,TG_RESULT ");
		sql.append(" ) ");
		sql.append(" , TEMP4 AS ( ");
		sql.append(" SELECT COALESCE( T3.AGENT_COMPANY_ID ,T2.AGENT_COMPANY_ID ) AGENT_COMPANY_ID , COALESCE( T3.SND_COMPANY_ID ,T2.SND_COMPANY_ID ) SND_COMPANY_ID,COALESCE( T3.TXID ,T2.TXID ) TXID ");
		sql.append(" ,COALESCE(T2.TG_RESULT , '成功') AS SUC_RESULT , COALESCE( T2.CNT ,0 ) SUC_CNT ,COALESCE( T2.AMT ,0 ) SUC_AMT ");
		sql.append(" ,COALESCE(T3.TG_RESULT , '失敗') AS FAL_RESULT , COALESCE( T3.CNT ,0 ) FAL_CNT ,COALESCE( T3.AMT ,0 ) FAL_AMT ");
		sql.append(" ,(COALESCE( T3.CNT ,0 )+COALESCE( T2.CNT ,0 )) ROW_CNT,(COALESCE( T3.AMT ,0 )+COALESCE( T2.AMT ,0 )) ROW_AMT ");
		sql.append(" FROM  TEMP3 T3 ");
		sql.append(" FULL JOIN TEMP2 T2 ON T3.BIZDATE = T2.BIZDATE AND T3.CLEARINGPHASE = T2.CLEARINGPHASE AND T2.TXID = T3.TXID ");
		sql.append(" AND T2.AGENT_COMPANY_ID = T3.AGENT_COMPANY_ID AND T2.SND_COMPANY_ID = T3.SND_COMPANY_ID ");
		sql.append(" ) ");
		sql.append(" SELECT COALESCE ((SELECT TXN_NAME FROM TXN_CODE TC WHERE TC.TXN_ID = TXID),'') AS TXN_NAME ");
		sql.append(" ,(SELECT AGENT_COMPANY_ABBR_NAME FROM TEMP T WHERE T.AGENT_COMPANY_ID  = AGENT_COMPANY_ID FETCH FIRST 1 ROWS ONLY)  AGENT_COMPANY_ABBR_NAME ");
		sql.append(" ,(SELECT SND_COMPANY_ABBR_NAME FROM TEMP T WHERE T.SND_COMPANY_ID  = SND_COMPANY_ID FETCH FIRST 1 ROWS ONLY)  SND_COMPANY_ABBR_NAME ");
		sql.append(" ,T4.*  ");
		sql.append(" FROM TEMP4 T4	  ");
		sql.append(" ORDER BY AGENT_COMPANY_ID,TXID,SND_COMPANY_ID	  ");
		System.out.println("getSQL>>"+sql);
				
		return sql.toString();
	}
	
	
	
	public AGENT_SEND_PROFILE_BO getAgent_send_profile_bo() {
		return agent_send_profile_bo;
	}

	public void setAgent_send_profile_bo(AGENT_SEND_PROFILE_BO agent_send_profile_bo) {
		this.agent_send_profile_bo = agent_send_profile_bo;
	}


	public RPONBLOCKTAB_Dao getRponblocktab_Dao() {
		return rponblocktab_Dao;
	}


	public void setRponblocktab_Dao(RPONBLOCKTAB_Dao rponblocktab_Dao) {
		this.rponblocktab_Dao = rponblocktab_Dao;
	}
	
	
	
	
}
