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

public class TRANSACTIONDAY_DOWNLOAD_BO {
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private CodeUtils codeUtils;
	
	//條件
	public String getCondition(String date,String pcode,String opbkId,String clearingphase){
		List<String> conditions = new ArrayList<String>();
		String condition = "";
			
		if(StrUtils.isNotEmpty(date)){
			conditions.add(" BIZDATE = '" + date + "' ");
		}
		if(StrUtils.isNotEmpty(pcode) && !pcode.equals("all")){
			conditions.add(" PCODE = '" + pcode + "' ");
		}
		if(StrUtils.isNotEmpty(opbkId) && !opbkId.equals("all")){
			conditions.add(" OPBK_ID = '" + opbkId + "' ");
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
		
		sql.append("SELECT ");
		sql.append("A.BIZDATE,");
		sql.append("A.CLEARINGPHASE,");
		sql.append("A.BRBK_ID,");
		sql.append("A.PCODE,");
		sql.append("A.RESULTSTATUS,");
		sql.append("B.SENDCNT AS FIRECOUNT,");
		sql.append("B.SENDAMT AS FIREAMT,");
		sql.append("C.OUTCNT AS DEBITCOUNT,");
		sql.append("C.OUTAMT AS DEBITAMT,");
		sql.append("D.INCNT AS SAVECOUNT,");
		sql.append("D.INAMT AS SAVEAMT ");
		sql.append("FROM (SELECT ");
		sql.append("BIZDATE,CLEARINGPHASE,BRBK_ID,PCODE,RESULTSTATUS ");
		sql.append("FROM RPDAILYSUMTAB" + (StrUtils.isEmpty(condition)?"":" WHERE "+condition));
		
		sql.append(" GROUP BY BIZDATE,CLEARINGPHASE,BRBK_ID,PCODE,RESULTSTATUS) A ");
		
		sql.append("LEFT JOIN (SELECT ");
		sql.append("BIZDATE,CLEARINGPHASE,BRBK_ID,PCODE,RESULTSTATUS,SUM(CNT) SENDCNT,SUM(AMT) SENDAMT ");
		sql.append("FROM RPDAILYSUMTAB WHERE OP_TYPE='S'" + (StrUtils.isEmpty(condition)?"":" AND "+condition));
		sql.append(" GROUP BY BIZDATE,CLEARINGPHASE,BRBK_ID,PCODE,RESULTSTATUS) B ");
		sql.append("ON B.BIZDATE=A.BIZDATE AND B.CLEARINGPHASE=A.CLEARINGPHASE AND B.BRBK_ID=A.BRBK_ID AND B.PCODE=A.PCODE AND B.RESULTSTATUS=A.RESULTSTATUS ");
		
		sql.append("LEFT JOIN (SELECT ");
		sql.append("BIZDATE,CLEARINGPHASE,BRBK_ID,PCODE,RESULTSTATUS,SUM(CNT) OUTCNT,SUM(AMT) OUTAMT ");
		sql.append("FROM RPDAILYSUMTAB WHERE OP_TYPE='O'" + (StrUtils.isEmpty(condition)?"":" AND "+condition));
		sql.append(" GROUP BY BIZDATE,CLEARINGPHASE,BRBK_ID,PCODE,RESULTSTATUS) C ");
		sql.append("ON C.BIZDATE=A.BIZDATE AND C.CLEARINGPHASE=A.CLEARINGPHASE AND C.BRBK_ID=A.BRBK_ID AND C.PCODE=A.PCODE AND C.RESULTSTATUS=A.RESULTSTATUS ");
		
		sql.append("LEFT JOIN (SELECT ");
		sql.append("BIZDATE,CLEARINGPHASE,BRBK_ID,PCODE,RESULTSTATUS,SUM(CNT) INCNT,SUM(AMT) INAMT ");
		sql.append("FROM RPDAILYSUMTAB WHERE OP_TYPE='I'" + (StrUtils.isEmpty(condition)?"":" AND "+condition));
		sql.append(" GROUP BY BIZDATE,CLEARINGPHASE,BRBK_ID,PCODE,RESULTSTATUS) D ");
		sql.append("ON D.BIZDATE=A.BIZDATE AND D.CLEARINGPHASE=A.CLEARINGPHASE AND D.BRBK_ID=A.BRBK_ID AND D.PCODE=A.PCODE AND D.RESULTSTATUS=A.RESULTSTATUS ");
		sql.append("ORDER BY BIZDATE,CLEARINGPHASE,BRBK_ID,PCODE,RESULTSTATUS");
		
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
		//分行代號
		columnNameList.add("BRBK_ID");
		columnLengthList.add(7);
		columnTypeList.add("string");
		//交易代號
		columnNameList.add("PCODE");
		columnLengthList.add(4);
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
		columnLengthList.add(14);
		columnTypeList.add("number");
		//扣款行筆數
		columnNameList.add("DEBITCOUNT");
		columnLengthList.add(7);
		columnTypeList.add("number");
		//扣款行金額
		columnNameList.add("DEBITAMT");
		columnLengthList.add(14);
		columnTypeList.add("number");
		//入帳行筆數
		columnNameList.add("SAVECOUNT");
		columnLengthList.add(7);
		columnTypeList.add("number");
		//入帳行金額
		columnNameList.add("SAVEAMT");
		columnLengthList.add(14);
		columnTypeList.add("number");
		
		columnMap.put("columnName",columnNameList);
		columnMap.put("columnLength",columnLengthList);
		columnMap.put("columnType",columnTypeList);
		
		return columnMap;
	}
	public String getFirstRow(String date){
		String firstRow = "";
		
		firstRow = "BOFEA21" + date;
		
		return firstRow;
	}
	public String getLastRow(String dataCount){
		String lastRow = "";
		
		lastRow = "EOFEA21" + codeUtils.padText("number",6,dataCount);
		
		return lastRow;
	}
	public Map<String,Object> getTXT(String date,String pcode,String opbkId,String clearingphase){
		Map<String,Object> dataMap = new HashMap<String,Object>();
		
		byte[] byteTXT = null;
		
		List<Map<String, Object>> queryListMap = codeUtils.queryListMap(getSQL(getCondition(date,pcode,opbkId,clearingphase)),null);
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
			dataMap.put("message","查詢操作行="+opbkId+"的交易日統計資料過程出現問題");
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
