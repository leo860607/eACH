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

public class CLEARDAY_DOWNLOAD_BO {
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private CodeUtils codeUtils;

	//條件
	public String getCondition(String date,String opbkId,String clearingphase){
		List<String> conditions = new ArrayList<String>();
		String condition = "";
				
		if(StrUtils.isNotEmpty(date)){
			conditions.add(" A.BIZDATE = '" + date + "' ");
		}
		if(StrUtils.isNotEmpty(opbkId) && !opbkId.equals("all")){
			//conditions.add(" B.OPBK_ID = '" + opbkId + "' ");
			conditions.add(" GET_CUR_OPBKID(A.BANKID, '" + DateTimeUtils.convertDate(date, "yyyyMMdd", "yyyyMMdd") + "',0) = '" + opbkId + "' ");
		}
		if(StrUtils.isNotEmpty(opbkId)){
			conditions.add(" CLEARINGPHASE = '" + clearingphase + "' ");
		}
		for(int i = 0; i < conditions.size(); i++){
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
		
		sql.append("SELECT A.BIZDATE,A.CLEARINGPHASE,A.BANKID,SUM(A.RECVCNT) RECVCNT,ABS(SUM(A.RECVAMT)) RECVAMT,SUM(A.PAYCNT) PAYCNT,ABS(SUM(A.PAYAMT)) PAYAMT,SUM(A.RVSRECVCNT) RVSRECVCNT,ABS(SUM(A.RVSRECVAMT)) RVSRECVAMT,SUM(A.RVSPAYCNT) RVSPAYCNT,ABS(SUM(A.RVSPAYAMT)) RVSPAYAMT ");
		sql.append("FROM ");
		sql.append("ONCLEARINGTAB A ");
		//sql.append("LEFT JOIN BANK_GROUP B ");
		//sql.append("ON A.BANKID=B.BGBK_ID ");
		sql.append((StrUtils.isEmpty(condition)?"":"WHERE "+condition));
		sql.append(" GROUP BY A.BIZDATE,A.CLEARINGPHASE,A.BANKID ");
		sql.append("ORDER BY A.BIZDATE,A.CLEARINGPHASE,A.BANKID");
		
		SQL = sql.toString();
		logger.debug("SQL==>" + SQL);
		
		return SQL;
	}
	
	public Map<String,Object> getColumnMap(){
		Map<String,Object> columnMap = new HashMap<String,Object>();
		List<String> columnNameList = new ArrayList<String>();
		List<Integer> columnLengthList = new ArrayList<Integer>();
		List<String> columnTypeList = new ArrayList<String>();
		
		//營業日(記帳日)
		columnNameList.add("BIZDATE");
		columnLengthList.add(8);
		columnTypeList.add("string");
		//清算階段
		columnNameList.add("CLEARINGPHASE");
		columnLengthList.add(2);
		columnTypeList.add("string");
		//總行代號
		columnNameList.add("BANKID");
		columnLengthList.add(7);
		columnTypeList.add("string");
		//應收筆數
		columnNameList.add("RECVCNT");
		columnLengthList.add(7);
		columnTypeList.add("number");
		//應收金額
		columnNameList.add("RECVAMT");
		columnLengthList.add(14);
		columnTypeList.add("decimal(0)");
		//沖正-應收筆數
		columnNameList.add("RVSRECVCNT");
		columnLengthList.add(7);
		columnTypeList.add("number");
		//沖正-應收金額
		columnNameList.add("RVSRECVAMT");
		columnLengthList.add(14);
		columnTypeList.add("decimal(0)");
		//應付筆數
		columnNameList.add("PAYCNT");
		columnLengthList.add(7);
		columnTypeList.add("number");
		//應付金額
		columnNameList.add("PAYAMT");
		columnLengthList.add(14);
		columnTypeList.add("decimal(0)");
		//沖正-應付筆數
		columnNameList.add("RVSPAYCNT");
		columnLengthList.add(7);
		columnTypeList.add("number");
		//沖正-應付金額
		columnNameList.add("RVSPAYAMT");
		columnLengthList.add(14);
		columnTypeList.add("decimal(0)");
		
		columnMap.put("columnName",columnNameList);
		columnMap.put("columnLength",columnLengthList);
		columnMap.put("columnType",columnTypeList);
		
		return columnMap;
	}
	public String getFirstRow(String date){
		String firstRow = "";
		
		firstRow = "BOFEA25" + date;
		
		return firstRow;
	}
	public String getLastRow(String dataCount){
		String lastRow = "";
		
		lastRow = "EOFEA25" + codeUtils.padText("number",6,dataCount);
		
		return lastRow;
	}
	public Map<String,Object> getTXT(String date,String opbkId,String clearingphase){
		Map<String,Object> dataMap = new HashMap<String,Object>();
		
		byte[] byteTXT = null;
		
		List<Map<String, Object>> queryListMap = codeUtils.queryListMap(getSQL(getCondition(date,opbkId,clearingphase)),null);
		//TXT的Byte[]
		if(queryListMap.size() == 0){
			byteTXT = codeUtils.createTXTWithoutData(getFirstRow(date),getLastRow("0"));
		}
		if(queryListMap.size() > 0){
			byteTXT = codeUtils.createTXT(queryListMap,getColumnMap(),getFirstRow(date),getLastRow(String.valueOf(queryListMap.size())));
		}
		//正常
		if(byteTXT != null){
			dataMap.put("data",byteTXT);
		}
		//有問題
		else{
			dataMap.put("message","查詢操作行="+opbkId+"的結算日統計資料過程出現問題");
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
