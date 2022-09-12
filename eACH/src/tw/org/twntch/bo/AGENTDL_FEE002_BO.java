package tw.org.twntch.bo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import tw.org.twntch.util.CodeUtils;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.StrUtils;

public class AGENTDL_FEE002_BO {
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private CodeUtils codeUtils;
	
	//條件
	public String getCondition(String yyyymm,String agent_company_id,String snd_company_id,String clearingphase){
		List<String> conditions = new ArrayList<String>();
		String condition = " WHERE TG_RESULT='A' ";
			
		if(StrUtils.isNotEmpty(yyyymm)){
//			conditions.add(" SUBSTR( START_DATE,1,6) = '" + yyyymm + "' ");
			yyyymm = DateTimeUtils.convertDate(yyyymm, "yyyyMM", "yyyyMM");
			conditions.add(" YYYYMM = '" + yyyymm + "' ");
		}
		
		if(StrUtils.isNotEmpty(agent_company_id) && !agent_company_id.equals("all")){
			conditions.add(" AGENT_COMPANY_ID = '" + agent_company_id + "' ");
		}
		if(StrUtils.isNotEmpty(snd_company_id) && !snd_company_id.equals("all")){
			conditions.add(" SND_COMPANY_ID = '" + snd_company_id + "' ");
		}
		if(StrUtils.isNotEmpty(clearingphase)){
			conditions.add(" CLEARINGPHASE = '" + clearingphase + "' ");
		}
		for(int i = 0; i < conditions.size(); i++){
			condition += " AND ";
			condition += conditions.get(i);
		}
		logger.debug("condition==>"+condition);
		
		return condition;
	}
	
	
	
	public String getSQL(String condition){
		StringBuffer sql = new StringBuffer();
		sql.append(" WITH TEMP AS ( ");
		sql.append(" SELECT *  FROM RP_DAILY_TXNLOG ");
		sql.append(condition);
		sql.append(" ) ");
		sql.append(" , TEMP2 AS( ");
//		20150310 edit by hugo req by UAT-2016310-06
//		sql.append(" SELECT YYYYMM,AGENT_COMPANY_ID  ,AGENT_FEE ,SUM( SUM_AGENT_FEE) SUM_AGENT_FEE ,  '507' AS  FEENO ");
//		sql.append(" FROM TEMP GROUP BY YYYYMM,AGENT_COMPANY_ID  ,AGENT_FEE ");
		sql.append(" SELECT YYYYMM,AGENT_COMPANY_ID  ,SUM( SUM_AGENT_FEE) SUM_AGENT_FEE ,  '507' AS  FEENO ");
		sql.append(" FROM TEMP GROUP BY YYYYMM,AGENT_COMPANY_ID   ");
		sql.append(" ) ");
		sql.append(" SELECT T2.*  ,COALESCE( (SELECT COMPANY_NO FROM AGENT_PROFILE AP WHERE AP.COMPANY_ID = T2.AGENT_COMPANY_ID) , '' ) AS COMPANY_NO ");
//		20150310 edit by hugo req by UAT-2016310-06 
//		sql.append(" ,(CASE WHEN SUM_AGENT_FEE< 0 THEN '-'ELSE'+' END  ) FEE_SIGN  , '' AS RETCOL ");
		sql.append(" ,(CASE WHEN SUM_AGENT_FEE > 0 THEN '-'ELSE'+' END  ) FEE_SIGN  , '' AS RETCOL ");
		sql.append(" FROM TEMP2 T2 ");
//		20150310 add by hugo req by UAT-20160108-01
		sql.append(" WHERE SUM_AGENT_FEE <> 0  ");
		sql.append(" ORDER BY YYYYMM , COMPANY_NO ");

		logger.debug("SQL==>" + sql.toString());
		return sql.toString();
	}
//	public String getSQL(String condition){
//		StringBuffer sql = new StringBuffer();
//		sql.append(" SELECT SUBSTR( START_DATE,1,6) YYYYMM ,  COALESCE( (SELECT COMPANY_NO FROM AGENT_PROFILE AP WHERE AP.COMPANY_ID = COMPANY_ID FETCH FIRST 1 ROW ONLY),'') COMPANY_NO ,FEE_ID  ,(CASE WHEN FEE < 0 THEN '-' ELSE '+' END) FEE_SIGN,CAST (FEE AS INTEGER) FEE ,'' RETCOL ");
//		sql.append(" FROM AGENT_FEE_CODE ");
//		sql.append(condition);
//		logger.debug("SQL==>" + sql.toString());
//		return sql.toString();
//	}
	
	
	public Map<String,Object> getColumnMap(String key , Map<String,String> valueMap){
		Map<String,Object> columnMap = new HashMap<String,Object>();
		List<String> columnNameList = new ArrayList<String>();
		List<Integer> columnLengthList = new ArrayList<Integer>();
		List<String> columnTypeList = new ArrayList<String>();
		String length_key_str = "";
		String type_key_str = "";
		try {
			if(valueMap == null){
				return null;
			}
			length_key_str = valueMap.get(key+"_L") ;
			type_key_str = valueMap.get(key+"_T");
			if(StrUtils.isEmpty(length_key_str) || StrUtils.isEmpty(type_key_str) ){
				return null;
			}
			Map<String,Integer> lengthMap = JSONUtils.json2map(length_key_str);
			Map<String,String> typeMap = JSONUtils.json2map(type_key_str);
			int i = 0;
			System.out.println("lengthMap>>"+lengthMap);
			for(String key_tmp :lengthMap.keySet()){
				System.out.println("key_tmp"+key_tmp);
				columnNameList.add(key_tmp);
				columnLengthList.add(lengthMap.get(key_tmp));
				columnTypeList.add(typeMap.get(key_tmp));
				i=i+ lengthMap.get(key_tmp);
			}
			logger.debug("總長度>>"+i);
			columnMap.put("columnName",columnNameList);
			columnMap.put("columnLength",columnLengthList);
			columnMap.put("columnType",columnTypeList);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			columnMap = null;
		}
		
		return columnMap;
	}
	public String getFirstRow(String date ,String key , Map <String,String> valueMap){
		String firstRow = "";
		if(valueMap != null){
			String head = valueMap.get("head");	
			String dataid = valueMap.get(key+"_N");	
			firstRow = head+dataid+ date;
		}
		
		return firstRow;
	}
	public String getLastRow(String dataCount,String key , Map <String,String> valueMap){
		String lastRow = "";
		if(valueMap != null){
			String end = valueMap.get("end");	
			String dataid = valueMap.get(key+"_N");	
			lastRow = end+dataid+ codeUtils.padText("number",6,dataCount);;
		}
//		lastRow = "EOFEA21" + codeUtils.padText("number",6,dataCount);
		
		return lastRow;
	}
	public Map<String,Object> getTXT(String date,String agent_company_id , String snd_company_id,String clearingphase){
		Map<String,Object> dataMap = new HashMap<String,Object>();
		String key = "agent_fee002";
		Map<String,String> valueMap = codeUtils.getPropertyValue("Configuration.properties",key+"_N",key+"_L",key+"_T","head","end");
		//無法取得properties
		if(valueMap == null){
			logger.debug("無法取得Configuration.properties");
			return null;
		}
		byte[] byteTXT = null;
		
		List<Map<String, Object>> queryListMap = codeUtils.queryListMap(getSQL(getCondition(date,agent_company_id,snd_company_id,clearingphase)),null);
		//TXT的Byte[]
		if(queryListMap.size() == 0){
			byteTXT = codeUtils.createTXTWithoutData(getFirstRow(date,key,valueMap),getLastRow("0",key,valueMap));
		}
		if(queryListMap.size() > 0){
			byteTXT = codeUtils.createTXT(queryListMap,getColumnMap(key , valueMap),getFirstRow(date,key,valueMap),getLastRow(String.valueOf(queryListMap.size()),key,valueMap));
		}
		//正常
		if(byteTXT != null){
			dataMap.put("data",byteTXT);
		}
		//有問題
		else{
			dataMap.put("message","查詢代理業者="+agent_company_id+"的手續費日統計資料檔過程出現問題");
		}
		return dataMap;
	}
	
	public CodeUtils getCodeUtils() {
		return codeUtils;
	}
	public void setCodeUtils(CodeUtils codeUtils) {
		this.codeUtils = codeUtils;
	}
}
