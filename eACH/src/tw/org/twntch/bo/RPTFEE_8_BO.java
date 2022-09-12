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

public class RPTFEE_8_BO {
	private AGENT_SEND_PROFILE_BO agent_send_profile_bo;
	private RPONBLOCKTAB_Dao rponblocktab_Dao;
	
	public Map<String, String> ex_export(String agent_company_id, String snd_company_id,String yyyymm ,  String clearingPhase , String serchStrs , int export_type ,String user_type){
		return export(agent_company_id, snd_company_id, yyyymm, clearingPhase, serchStrs, export_type , user_type);
	}
	public Map<String, String> export(String agent_company_id, String snd_company_id,String bizdate ,  String clearingPhase , String serchStrs , int export_type ,String user_type){
		Map<String, String> rtnMap = null;
		Map<String, String> serchparams = null;
		String outputFilePath = "" , conditionKey  ="" ,  ebizdate = "" , txid = "" ,file_resource = "fee_8";
		try{
			conditionKey = "[\"SBIZDATE\",\"TXID\",\"AGENT_COMPANY_ID\",\"SND_COMPANY_ID\",\"CLEARINGPHASE\"]";
			rtnMap = new HashMap<String, String>();
			rtnMap.put("serchStrs", serchStrs);
			Map<String, Object> params = new HashMap<String, Object>();
//			SQL查詢條件 區塊
			if(StrUtils.isNotEmpty(serchStrs)){
				serchparams = JSONUtils.json2map(serchStrs);
			}
			bizdate = serchparams.get("SBIZDATE");
			if(user_type.equals("A")){
				ebizdate =  serchparams.get("EBIZDATE") ;
			}else{
				serchparams.put("EBIZDATE", bizdate);
			}
			System.out.println("bizdate>>"+bizdate+",ebizdate>>"+ebizdate);
			txid =  StrUtils.isEmpty(serchparams.get("TXID"))?"全部" :serchparams.get("TXID");
			params.putAll(serchparams);
//			顯示區塊
			if(user_type.equals("A")){
				params.put("V_TXDT",DateTimeUtils.convertDate(DateTimeUtils.convertDate(bizdate,"yyyyMMdd","yyyyMMdd"),"yyyyMMdd","yyy/MM/dd")+"~"+DateTimeUtils.convertDate(DateTimeUtils.convertDate(ebizdate,"yyyyMMdd","yyyyMMdd"),"yyyyMMdd","yyy/MM/dd"));
			}else{
				params.put("V_TXDT",DateTimeUtils.convertDate(DateTimeUtils.convertDate(bizdate,"yyyyMMdd","yyyyMMdd"),"yyyyMMdd","yyy/MM/dd"));
				
			}
			params.put("V_CLEARINGPHASE", StrUtils.isEmpty(clearingPhase)?"全部": clearingPhase );
			params.put("V_AGENT_COMPANY_ID",StrUtils.isEmpty(serchparams.get("AGENT_COMPANY_ID"))?"全部": serchparams.get("AGENT_COMPANY_ID"));
			params.put("V_SND_COMPANY_ID",StrUtils.isEmpty(serchparams.get("SND_COMPANY_ID"))?"全部": serchparams.get("SND_COMPANY_ID"));
			params.put("V_TXID",txid);
			params.put("V_PRINT_DATE",DateTimeUtils.convertDate(DateTimeUtils.INTERCONVERSION, zDateHandler.getTheDate(), "yyyy/MM/dd", "yyy/MM/dd"));
			params.put("V_PRINT_TIME",zDateHandler.getTheTime());
			params.put("V_USER_TYPE",user_type);
			System.out.println("params>>"+params);
			Map map = this.getConditionData(serchparams, conditionKey);
//			避免被AOP攔截並記錄此參數
			serchparams.remove("EBIZDATE");
			String sql = getSQL(map.get("sqlPath").toString(),"", "");
			List list = rponblocktab_Dao.getRptData(sql, (Map<String,String>) map.get("values"));
			int pathType = Arguments.getStringArg("ISWINOPEN").equals("Y")?RptUtils.C_PATH :RptUtils.R_PATH;
			file_resource = export_type == 2?"ex_fee_8":file_resource;
//			outputFilePath = outputFilePath = RptUtils.export(RptUtils.COLLECTION,pathType, file_resource, file_resource, params, list);
			outputFilePath  = RptUtils.export(RptUtils.COLLECTION,pathType, file_resource, file_resource, params, list , export_type);
			System.out.println("RPTTX_8_BO.sql>>"+sql);
			System.out.println("RPTTX_8_BO.map>>"+map);
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
		sql.append( " WHERE TG_RESULT = 'A' ");
		try {
			for(String key : keyList){
				if(params.containsKey(key) && StrUtils.isNotEmpty(params.get(key)) && !params.get(key).equals("all")){
					if(key.equals("SBIZDATE")){
						sql.append( " AND ");
						sql.append(" BIZDATE >= :"+key+" AND BIZDATE<= :EBIZDATE");
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
		sql.append(" WITH TEMP AS( SELECT * FROM RP_DAILY_TXNLOG  ");
		sql.append(sqlPath);
		sql.append(" ) ");
		sql.append(" ,TEMP2 AS ( SELECT AGENT_COMPANY_ID  ,SND_COMPANY_ID  ,TXID,AGENT_FEE, SUM(CNT) CNT ,SUM( SUM_AGENT_FEE) SUM_AGENT_FEE");
		sql.append(" FROM TEMP GROUP BY AGENT_COMPANY_ID  ,SND_COMPANY_ID  ,TXID,AGENT_FEE ");
		sql.append(" ORDER BY AGENT_COMPANY_ID  ,SND_COMPANY_ID  ,TXID,AGENT_FEE ");
		sql.append(" ) ");
		sql.append(" SELECT T2.* ,COALESCE ((SELECT TXN_NAME FROM TXN_CODE TC WHERE TC.TXN_ID = TXID ),'') AS TXN_NAME ");
		sql.append(" ,(SELECT AGENT_COMPANY_ABBR_NAME FROM TEMP T WHERE T.AGENT_COMPANY_ID  = AGENT_COMPANY_ID FETCH FIRST 1 ROWS ONLY)  AGENT_COMPANY_ABBR_NAME ");
		sql.append(" ,(SELECT SND_COMPANY_ABBR_NAME FROM TEMP T WHERE T.SND_COMPANY_ID  = SND_COMPANY_ID FETCH FIRST 1 ROWS ONLY)  SND_COMPANY_ABBR_NAME ");
		sql.append(" FROM TEMP2 T2 ");
		sql.append(" ORDER BY AGENT_COMPANY_ID,SND_COMPANY_ID,TXID,AGENT_FEE ");
		System.out.println("getSQL>>"+sql);
		return sql.toString();
	}
//	
//	public String getSQL(String sqlPath , String spSql , String orderbySql){
//		StringBuffer sql = new StringBuffer();
//		sql.append(" SELECT AGENT_COMPANY_ID , AGENT_COMPANY_ABBR_NAME ,SND_COMPANY_ID ,SND_COMPANY_ABBR_NAME ,TXID,CLEARINGPHASE ") ;
//		sql.append(" ,(SELECT TXN_NAME FROM TXN_CODE TC WHERE TC.TXN_ID = TXID ),AGENT_FEE, CNT , SUM_AGENT_FEE ") ;
//		sql.append(spSql);
//		sql.append("  FROM RP_DAILY_TXNLOG ");
//		sql.append(sqlPath);
//		sql.append(" ORDER BY AGENT_COMPANY_ID , SND_COMPANY_ID,CLEARINGPHASE ");
//		sql.append(orderbySql);
//		System.out.println("getSQL>>"+sql);
//		return sql.toString();
//	}
//	
	
	
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
