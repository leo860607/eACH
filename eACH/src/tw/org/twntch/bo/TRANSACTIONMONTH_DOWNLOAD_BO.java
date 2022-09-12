package tw.org.twntch.bo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import tw.org.twntch.util.CodeUtils;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.StrUtils;

public class TRANSACTIONMONTH_DOWNLOAD_BO {
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private CodeUtils codeUtils;

	//條件
	public String getCondition(String YYYYMM,String pcode,String opbkId){
		List<String> conditions = new ArrayList<String>();
		String condition = "";
		
		if(StrUtils.isNotEmpty(YYYYMM)){
			conditions.add(" YYYYMM = '" + YYYYMM + "' ");
		}
		if(StrUtils.isNotEmpty(pcode) && !pcode.equals("all")){
			conditions.add(" PCODE = '" + pcode + "' ");
		}
		if(StrUtils.isNotEmpty(opbkId) && !opbkId.equals("all")){
			conditions.add(" OPBK_ID = '" + opbkId + "' ");
		}
		
		for(int i = 0; i < conditions.size(); i++){
			if(i == 0){
				condition = "WHERE ";
			}
			condition += conditions.get(i);
			if(i < conditions.size() - 1){
				condition += " AND ";
			}
		}
		logger.debug("condition==>"+condition);
		
		return condition;
	}
	
	public String getSQL(String condition){
		StringBuffer sql = new StringBuffer();
		String SQL = "";
		
		sql.append("WITH TEMP AS (");
		sql.append("SELECT * FROM RPMONTHSUMTAB " + (StrUtils.isEmpty(condition)?"":condition));
		sql.append(")");
		
		sql.append("SELECT ");
		sql.append("A.YYYYMM,");
		sql.append("A.BRBK_ID,");
		sql.append("A.RESULTSTATUS,");
		sql.append("COALESCE((SELECT SUM(COALESCE(CNT,0)) FROM TEMP WHERE BRBK_ID = A.BRBK_ID AND RESULTSTATUS = A.RESULTSTATUS AND OP_TYPE = 'S'),0) AS FIRECOUNT,");
		sql.append("COALESCE((SELECT SUM(COALESCE(AMT,0)) FROM TEMP WHERE BRBK_ID = A.BRBK_ID AND RESULTSTATUS = A.RESULTSTATUS AND OP_TYPE = 'S'),0) AS FIREAMT,");
		sql.append("COALESCE((SELECT SUM(COALESCE(CNT,0)) FROM TEMP WHERE BRBK_ID = A.BRBK_ID AND RESULTSTATUS = A.RESULTSTATUS AND OP_TYPE = 'O'),0) AS DEBITCOUNT,");
		sql.append("COALESCE((SELECT SUM(COALESCE(AMT,0)) FROM TEMP WHERE BRBK_ID = A.BRBK_ID AND RESULTSTATUS = A.RESULTSTATUS AND OP_TYPE = 'O'),0) AS DEBITAMT,");
		sql.append("COALESCE((SELECT SUM(COALESCE(CNT,0)) FROM TEMP WHERE BRBK_ID = A.BRBK_ID AND RESULTSTATUS = A.RESULTSTATUS AND OP_TYPE = 'I'),0) AS SAVECOUNT,");
		sql.append("COALESCE((SELECT SUM(COALESCE(AMT,0)) FROM TEMP WHERE BRBK_ID = A.BRBK_ID AND RESULTSTATUS = A.RESULTSTATUS AND OP_TYPE = 'I'),0) AS SAVEAMT ");
		sql.append("FROM TEMP AS A ");
		sql.append("GROUP BY A.YYYYMM,A.BRBK_ID,A.RESULTSTATUS ");
		sql.append("ORDER BY A.BRBK_ID");
		
		SQL = sql.toString();
		logger.debug("SQL==>" + SQL);
		
		return SQL;
	}
	
	public Map<String,Object> getColumnMap(){
		Map<String,Object> columnMap = new HashMap<String,Object>();
		List<String> columnNameList = new ArrayList<String>();
		List<Integer> columnLengthList = new ArrayList<Integer>();
		List<String> columnTypeList = new ArrayList<String>();
		
		//營業年月(記帳年月)
		columnNameList.add("YYYYMM");
		columnLengthList.add(6);
		columnTypeList.add("string");
		//分行代號
		columnNameList.add("BRBK_ID");
		columnLengthList.add(7);
		columnTypeList.add("string");
		//處理結果
		columnNameList.add("RESULTSTATUS");
		columnLengthList.add(1);
		columnTypeList.add("string");
		//發動行筆數
		columnNameList.add("FIRECOUNT");
		columnLengthList.add(7);
		columnTypeList.add("number");
		//發動行金額
		columnNameList.add("FIREAMT");
		columnLengthList.add(15);
		columnTypeList.add("number");
		//扣款行筆數
		columnNameList.add("DEBITCOUNT");
		columnLengthList.add(7);
		columnTypeList.add("number");
		//扣款行金額
		columnNameList.add("DEBITAMT");
		columnLengthList.add(15);
		columnTypeList.add("number");
		//入帳行筆數
		columnNameList.add("SAVECOUNT");
		columnLengthList.add(7);
		columnTypeList.add("number");
		//入帳行金額
		columnNameList.add("SAVEAMT");
		columnLengthList.add(15);
		columnTypeList.add("number");
		
		columnMap.put("columnName",columnNameList);
		columnMap.put("columnLength",columnLengthList);
		columnMap.put("columnType",columnTypeList);
		
		return columnMap;
	}
	public String getFirstRow(String YYYYMM){
		String firstRow = "";
		
		firstRow = "BOFEA22" + YYYYMM;
		
		return firstRow;
	}
	public String getLastRow(String dataCount){
		String lastRow = "";
		
		lastRow = "EOFEA22" + codeUtils.padText("number",6,dataCount);
		
		return lastRow;
	}
	public Map<String,Object> getTXT(String YYYYMM,String pcode,String opbkId){
		Map<String,Object> dataMap = new HashMap<String,Object>();
		
		byte[] byteTXT = null;
		
		List<Map<String, Object>> queryListMap = codeUtils.queryListMap(getSQL(getCondition(YYYYMM,pcode,opbkId)),null);
		//TXT的Byte[]
		if(queryListMap.size() == 0){
			byteTXT = codeUtils.createTXTWithoutData(getFirstRow(YYYYMM),getLastRow("0"));
		}
		if(queryListMap.size() > 0){
			byteTXT = codeUtils.createTXT(queryListMap,getColumnMap(),getFirstRow(YYYYMM),getLastRow(String.valueOf(queryListMap.size())));
		}
		//正常
		if(byteTXT != null){
			dataMap.put("data",byteTXT);
		}
		//有問題
		else{
			dataMap.put("message","查詢操作行="+opbkId+"的交易月統計資料過程出現問題");
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
