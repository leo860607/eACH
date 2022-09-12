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

public class RPTTX_5_BO {
	private AGENT_SEND_PROFILE_BO agent_send_profile_bo;
	private RPONBLOCKTAB_Dao rponblocktab_Dao;
	public Map<String, String> export(String agent_company_id, String snd_company_id,String bizdate ,  String clearingPhase , String serchStrs){
		Map<String, String> rtnMap = null;
		Map<String, String> serchparams = null;
		String outputFilePath = "" , conditionKey  ="";
		try{
			conditionKey = "[\"BIZDATE\",\"TXID\",\"AGENT_COMPANY_ID\",\"SND_COMPANY_ID\",\"CLEARINGPHASE\"]";
			rtnMap = new HashMap<String, String>();
			rtnMap.put("serchStrs", serchStrs);
			Map<String, Object> params = new HashMap<String, Object>();
//			SQL查詢條件 區塊
			if(StrUtils.isNotEmpty(serchStrs)){
				serchparams = JSONUtils.json2map(serchStrs);
			}
			params.putAll(serchparams);
//			顯示區塊
			params.put("V_TXDT",DateTimeUtils.convertDate(DateTimeUtils.convertDate(bizdate,"yyyyMMdd","yyyyMMdd"),"yyyyMMdd","營業日期: yyy/MM/dd"));
			params.put("V_CLEARINGPHASE",clearingPhase);
			params.put("V_AGENT_COMPANY_ID",agent_company_id); //20161027
			params.put("V_PRINT_DATE",DateTimeUtils.convertDate(DateTimeUtils.INTERCONVERSION, zDateHandler.getTheDate(), "yyyy/MM/dd", "yyy/MM/dd"));
			params.put("V_PRINT_TIME",zDateHandler.getTheTime());
			Map map = this.getConditionData(serchparams, conditionKey);
			String sql = getSQL(map.get("sqlPath").toString(),"", "");
			List list = rponblocktab_Dao.getRptData(sql, (Map<String,String>) map.get("values"));
			int pathType = Arguments.getStringArg("ISWINOPEN").equals("Y")?RptUtils.C_PATH :RptUtils.R_PATH;
			outputFilePath = outputFilePath = RptUtils.export(RptUtils.COLLECTION,pathType, "tx_5", "tx_5", params, list);
			System.out.println("RPTTX_5_BO.sql>>"+sql);
			System.out.println("RPTTX_5_BO.map>>"+map);
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
//						values.put("EBIZDATE",DateTimeUtils.convertDate(params.get("EBIZDATE"), "yyyymmdd", "yyyymmdd"));
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
		sql.append(" SELECT AGENT_COMPANY_ID, AGENT_COMPANY_ABBR_NAME , SND_COMPANY_ID,SND_COMPANY_ABBR_NAME , OUTACCOUNTNUM "
				+ ", INACCOUNTNUM ,TXID ,TXN_NAME , PCODE , PCODE_NAME, PROCESSCODE ,RESPONSECODE,SEQ,STAN  "
				+ ",CAST(TRANSACTIONAMOUNT AS BIGINT) AS TRANSACTIONAMOUNT,OUTBANKID, INBANKID,CLEARINGPHASE,OUTBANK_NAME,INBANK_NAME"
				+ ",TRANSLATE('abcd-ef-gh', TRANSACTIONDATETIME, 'abcdefghopqrst') AS TXDATE,TRANSLATE('op:qr:st', TRANSACTIONDATETIME, 'abcdefghopqrst') AS TXTIME ");
		sql.append(" ,(CASE WHEN TG_RESULT='A' THEN '成功' WHEN TG_RESULT = 'P' THEN '未完成'  ELSE '失敗' END) TG_RESULT");
		sql.append(spSql);
//	發動者端	 ,(CASE WHEN TG_RESULT = 'P' OR TG_RESULT='A' THEN '成功' ELSE '失敗' END) TG_RESULT
//	票交端	 ,(CASE WHEN TG_RESULT='A' THEN '成功' WHEN TG_RESULT = 'P' THEN '未完成'  ELSE '失敗' END) TG_RESULT
		sql.append(" FROM VW_RP_TXNLOG ");
		sql.append(sqlPath);
		sql.append(" ORDER BY TXDATE , SEQ,CLEARINGPHASE ");
		sql.append(orderbySql);
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
