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

public class AGENTDL_TX001_BO {
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private CodeUtils codeUtils;
	
	//條件
	public String getCondition(String date,String agent_company_id,String snd_company_id,String clearingphase){
		List<String> conditions = new ArrayList<String>();
		String condition = " WHERE 1=1 ";
			
		if(StrUtils.isNotEmpty(date)){
			conditions.add(" TG.BIZDATE = '" + date + "' ");
		}
		if(StrUtils.isNotEmpty(agent_company_id) && !agent_company_id.equals("all")){
			conditions.add(" TG.AGENT_COMPANY_ID = '" + agent_company_id + "' ");
		}
		if(StrUtils.isNotEmpty(snd_company_id) && !snd_company_id.equals("all")){
			conditions.add(" TG.SND_COMPANY_ID = '" + snd_company_id + "' ");
		}
		if(StrUtils.isNotEmpty(clearingphase)){
			conditions.add(" TG.CLEARINGPHASE = '" + clearingphase + "' ");
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
		sql.append("WITH TEMP AS (");
		sql.append(" SELECT COALESCE(TG.TRANSACTIONDATETIME,'') AS TRANSACTIONDATETIME ");
		sql.append(" , COALESCE(TG.SEQ , '') AS  SEQ    ,  COALESCE(TG.STAN , '') AS  STAN ");
		sql.append(" , COALESCE(TG.PCODE , '') AS  PCODE   , COALESCE(TG.TXID,'') AS TXID ");
		sql.append(" , COALESCE(TG.TRANSACTIONAMOUNT,'0') AS TRANSACTIONAMOUNT ");
		sql.append(" , TG.AGENT_COMPANY_ID ,  COALESCE(TG.RECEIVERID ,'') RECEIVERID  ");
		sql.append(" , COALESCE(TG.INBANKID,'') AS INBANKID , COALESCE(TG.OUTBANKID,'') AS OUTBANKID ");
		sql.append(" , COALESCE(TG.INNATIONALID,'') INNATIONALID , COALESCE(TG.OUTNATIONALID,'') OUTNATIONALID ");
		sql.append(" , COALESCE(TG.INACCOUNTNUM ,'') AS INACCOUNTNUM  , COALESCE(TG.OUTACCOUNTNUM ,'') AS OUTACCOUNTNUM ");
		sql.append(" , COALESCE(TG.USERNUM , '') USERNUM, COALESCE(TG.NOTE,'')	NOTE  ");
		sql.append(" , COALESCE(TG.ISSUERREMARK,'') ISSUERREMARK , COALESCE (TG.HANDLECHARGE,'') HANDLECHARGE ");
		sql.append(" ,( CASE WHEN COALESCE(TG.TG_RESULT ,'') = 'A'  THEN 'A'  ELSE  'R' END) TG_RESULT ");
		sql.append(" ,COALESCE(TG.BIZDATE , '' ) AS  BIZDATE , COALESCE(TG.CLEARINGPHASE  , '') AS  CLEARINGPHASE ");
		sql.append(" FROM VW_RP_TXNLOG TG  ");
		sql.append(" LEFT JOIN ONBLOCKTAB OB ON TG.BIZDATE = OB.BIZDATE AND TG.STAN = OB.STAN ");
		sql.append(condition);
		sql.append(" ) ");
		sql.append(" SELECT * FROM TEMP  ");
		logger.debug("SQL==>" + sql.toString());
		return sql.toString();
	}
	
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
			for(String key_tmp :lengthMap.keySet()){
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
	/**
	 * 
	 * @param date
	 * @param agent_company_id
	 * @param snd_company_id
	 * @param clearingphase
	 * @param isWeb 主要用來判斷是否為UI驅動 ，以利後續操作記錄判別用 
	 * @return
	 */
	public Map<String,Object> getTXT(String date,String agent_company_id , String snd_company_id,String clearingphase,boolean isWeb){
		Map<String,Object> dataMap = new HashMap<String,Object>();
		String key = "agent_tx001";
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
			dataMap.put("message","查詢代理業者="+agent_company_id+"的交易資料檔過程出現問題");
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
