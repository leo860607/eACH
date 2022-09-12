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

public class FEEMONTH_DOWNLOAD_BO {
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private CodeUtils codeUtils;

	//條件
	public String getCondition(String YYYYMM,String opbkId){
		List<String> conditions = new ArrayList<String>();
		String condition = "";
			
		if(StrUtils.isNotEmpty(YYYYMM)){
			conditions.add(" YYYYMM = '" + YYYYMM + "' ");
		}
		if(StrUtils.isNotEmpty(opbkId) && !opbkId.equals("all")){
			conditions.add(" OPBK_ID = '" + opbkId + "' ");
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
		
		sql.append("SELECT A.YYYYMM,A.BRBK_ID,A.TXN_ID,'A' STATUS,B.SENDCNT,(CASE WHEN B.SENDAMT < 0 THEN '-' ELSE '+' END) SENDAMTSIGN,ABS(B.SENDAMT) SENDAMT,C.OUTCNT,(CASE WHEN C.OUTAMT < 0 THEN '-' ELSE '+' END) OUTAMTSIGN,ABS(C.OUTAMT) OUTAMT,D.INCNT,(CASE WHEN D.INAMT < 0 THEN '-' ELSE '+' END) INAMTSIGN,ABS(D.INAMT) INAMT,E.WOCNT,(CASE WHEN E.WOAMT < 0 THEN '-' ELSE '+' END) WOAMTSIGN,ABS(E.WOAMT) WOAMT ");
		sql.append("FROM (SELECT ");
		sql.append("YYYYMM,BRBK_ID,TXN_ID ");
		sql.append("FROM ");
		sql.append("RPCLEARFEETAB " + (StrUtils.isEmpty(condition)?"":"WHERE "+condition));
		sql.append("GROUP BY YYYYMM,BRBK_ID,TXN_ID) A ");
		
		sql.append("LEFT JOIN (SELECT ");
		sql.append("YYYYMM,BRBK_ID,TXN_ID,SUM(CNT) SENDCNT,SUM(AMT) SENDAMT ");
		sql.append("FROM ");
		sql.append("RPCLEARFEETAB WHERE OP_TYPE='S' " + (StrUtils.isEmpty(condition)?"":"AND "+condition));
		sql.append("GROUP BY YYYYMM,BRBK_ID,TXN_ID) B ");
		sql.append("ON B.YYYYMM=A.YYYYMM AND B.BRBK_ID=A.BRBK_ID AND B.TXN_ID=A.TXN_ID ");
		
		sql.append("LEFT JOIN (SELECT ");
		sql.append("YYYYMM,BRBK_ID,TXN_ID,SUM(CNT) OUTCNT,SUM(AMT) OUTAMT ");
		sql.append("FROM ");
		sql.append("RPCLEARFEETAB WHERE OP_TYPE='O' " + (StrUtils.isEmpty(condition)?"":"AND "+condition));
		sql.append("GROUP BY YYYYMM,BRBK_ID,TXN_ID) C ");
		sql.append("ON C.YYYYMM=A.YYYYMM AND C.BRBK_ID=A.BRBK_ID AND C.TXN_ID=A.TXN_ID ");

		sql.append("LEFT JOIN (SELECT ");
		sql.append("YYYYMM,BRBK_ID,TXN_ID,SUM(CNT) INCNT,SUM(AMT) INAMT ");
		sql.append("FROM ");
		sql.append("RPCLEARFEETAB WHERE OP_TYPE='I' " + (StrUtils.isEmpty(condition)?"":"AND "+condition));
		sql.append("GROUP BY YYYYMM,BRBK_ID,TXN_ID) D ");
		sql.append("ON D.YYYYMM=A.YYYYMM AND D.BRBK_ID=A.BRBK_ID AND D.TXN_ID=A.TXN_ID ");
		
		sql.append("LEFT JOIN (SELECT ");
		sql.append("YYYYMM,BRBK_ID,TXN_ID,SUM(CNT) WOCNT,SUM(AMT) WOAMT ");
		sql.append("FROM ");
		sql.append("RPCLEARFEETAB WHERE OP_TYPE='W' " + (StrUtils.isEmpty(condition)?"":"AND "+condition));
		sql.append("GROUP BY YYYYMM,BRBK_ID,TXN_ID) E ");
		sql.append("ON E.YYYYMM=A.YYYYMM AND E.BRBK_ID=A.BRBK_ID AND E.TXN_ID=A.TXN_ID ");
		
		//20150529 過濾失敗的資料
		sql.append("Where not ( coalesce(B.SendCNT,0)=0 and coalesce(C.OutCNT,0)=0 and coalesce(D.InCNT,0)=0 and coalesce(E.WoCNT,0)=0 ) ");

		sql.append("UNION ALL SELECT A.YYYYMM,A.BRBK_ID,A.TXN_ID,'E' STATUS,B.SENDCNT,(CASE WHEN B.SENDAMT < 0 THEN '-' ELSE '+' END) SENDAMTSIGN,ABS(B.SENDAMT) SENDAMT,C.OUTCNT,(CASE WHEN C.OUTAMT < 0 THEN '-' ELSE '+' END) OUTAMTSIGN,ABS(C.OUTAMT) OUTAMT,D.INCNT,(CASE WHEN D.INAMT < 0 THEN '-' ELSE '+' END) INAMTSIGN,ABS(D.INAMT) INAMT,E.WOCNT,(CASE WHEN E.WOAMT < 0 THEN '-' ELSE '+' END) WOAMTSIGN,ABS(E.WOAMT) WOAMT ");
		sql.append("FROM (SELECT ");
		sql.append("YYYYMM,BRBK_ID,TXN_ID ");
		sql.append("FROM ");
		sql.append("RPCLEARFEETAB " + (StrUtils.isEmpty(condition)?"":"WHERE "+condition));
		sql.append("GROUP BY YYYYMM,BRBK_ID,TXN_ID) A ");
		
		sql.append("LEFT JOIN (SELECT ");
		sql.append("YYYYMM,BRBK_ID,TXN_ID,SUM(RVSCNT) SENDCNT,SUM(RVSAMT) SENDAMT ");
		sql.append("FROM ");
		sql.append("RPCLEARFEETAB WHERE OP_TYPE='S' " + (StrUtils.isEmpty(condition)?"":"AND "+condition));
		sql.append("GROUP BY YYYYMM,BRBK_ID,TXN_ID) B ");
		sql.append("ON B.YYYYMM=A.YYYYMM AND B.BRBK_ID=A.BRBK_ID AND B.TXN_ID=A.TXN_ID ");
		
		sql.append("LEFT JOIN (SELECT ");
		sql.append("YYYYMM,BRBK_ID,TXN_ID,SUM(RVSCNT) OUTCNT,SUM(RVSAMT) OUTAMT ");
		sql.append("FROM ");
		sql.append("RPCLEARFEETAB WHERE OP_TYPE='O' " + (StrUtils.isEmpty(condition)?"":"AND "+condition));
		sql.append("GROUP BY YYYYMM,BRBK_ID,TXN_ID) C ");
		sql.append("ON C.YYYYMM=A.YYYYMM AND C.BRBK_ID=A.BRBK_ID AND C.TXN_ID=A.TXN_ID ");

		sql.append("LEFT JOIN (SELECT ");
		sql.append("YYYYMM,BRBK_ID,TXN_ID,SUM(RVSCNT) INCNT,SUM(RVSAMT) INAMT ");
		sql.append("FROM ");
		sql.append("RPCLEARFEETAB WHERE OP_TYPE='I' " + (StrUtils.isEmpty(condition)?"":"AND "+condition));
		sql.append("GROUP BY YYYYMM,BRBK_ID,TXN_ID) D ");
		sql.append("ON D.YYYYMM=A.YYYYMM AND D.BRBK_ID=A.BRBK_ID AND D.TXN_ID=A.TXN_ID ");
		
		sql.append("LEFT JOIN (SELECT ");
		sql.append("YYYYMM,BRBK_ID,TXN_ID,SUM(RVSCNT) WOCNT,SUM(RVSAMT) WOAMT ");
		sql.append("FROM ");
		sql.append("RPCLEARFEETAB WHERE OP_TYPE='W' " + (StrUtils.isEmpty(condition)?"":"AND "+condition));
		sql.append("GROUP BY YYYYMM,BRBK_ID,TXN_ID) E ");
		sql.append("ON E.YYYYMM=A.YYYYMM AND E.BRBK_ID=A.BRBK_ID AND E.TXN_ID=A.TXN_ID ");
		
		//20150529 過濾失敗的資料
		sql.append("Where not ( coalesce(B.SendCNT,0)=0 and coalesce(C.OutCNT,0)=0 and coalesce(D.InCNT,0)=0 and coalesce(E.WoCNT,0)=0 ) ");
		sql.append("ORDER BY YYYYMM,BRBK_ID,TXN_ID,STATUS");
		
		SQL = sql.toString();
		logger.debug("SQL==>" + SQL);
		
		return SQL;
	}
	
	public String getSQL_NW(String condition){
		StringBuffer sql = new StringBuffer();
		String SQL = "";
		
		sql.append("SELECT A.YYYYMM,A.BRBK_ID,A.TXN_ID,'A' STATUS,B.SENDCNT,(CASE WHEN B.SENDAMT < 0 THEN '-' ELSE '+' END) SENDAMTSIGN,ABS(B.SENDAMT) SENDAMT,C.OUTCNT,(CASE WHEN C.OUTAMT < 0 THEN '-' ELSE '+' END) OUTAMTSIGN,ABS(C.OUTAMT) OUTAMT,D.INCNT,(CASE WHEN D.INAMT < 0 THEN '-' ELSE '+' END) INAMTSIGN,ABS(D.INAMT) INAMT,E.WOCNT,(CASE WHEN E.WOAMT < 0 THEN '-' ELSE '+' END) WOAMTSIGN,ABS(E.WOAMT) WOAMT ");
		sql.append("FROM (SELECT ");
		sql.append("YYYYMM,BRBK_ID,TXN_ID ");
		sql.append("FROM ");
		sql.append("RPCLEARFEETAB_NW " + (StrUtils.isEmpty(condition)?"":"WHERE "+condition));
		sql.append("GROUP BY YYYYMM,BRBK_ID,TXN_ID) A ");
		
		sql.append("LEFT JOIN (SELECT ");
		sql.append("YYYYMM,BRBK_ID,TXN_ID,SUM(CNT) SENDCNT,SUM(AMT) SENDAMT ");
		sql.append("FROM ");
		sql.append("RPCLEARFEETAB_NW WHERE OP_TYPE='S' " + (StrUtils.isEmpty(condition)?"":"AND "+condition));
		sql.append("GROUP BY YYYYMM,BRBK_ID,TXN_ID) B ");
		sql.append("ON B.YYYYMM=A.YYYYMM AND B.BRBK_ID=A.BRBK_ID AND B.TXN_ID=A.TXN_ID ");
		
		sql.append("LEFT JOIN (SELECT ");
		sql.append("YYYYMM,BRBK_ID,TXN_ID,SUM(CNT) OUTCNT,SUM(AMT) OUTAMT ");
		sql.append("FROM ");
		sql.append("RPCLEARFEETAB_NW WHERE OP_TYPE='O' " + (StrUtils.isEmpty(condition)?"":"AND "+condition));
		sql.append("GROUP BY YYYYMM,BRBK_ID,TXN_ID) C ");
		sql.append("ON C.YYYYMM=A.YYYYMM AND C.BRBK_ID=A.BRBK_ID AND C.TXN_ID=A.TXN_ID ");

		sql.append("LEFT JOIN (SELECT ");
		sql.append("YYYYMM,BRBK_ID,TXN_ID,SUM(CNT) INCNT,SUM(AMT) INAMT ");
		sql.append("FROM ");
		sql.append("RPCLEARFEETAB_NW WHERE OP_TYPE='I' " + (StrUtils.isEmpty(condition)?"":"AND "+condition));
		sql.append("GROUP BY YYYYMM,BRBK_ID,TXN_ID) D ");
		sql.append("ON D.YYYYMM=A.YYYYMM AND D.BRBK_ID=A.BRBK_ID AND D.TXN_ID=A.TXN_ID ");
		
		sql.append("LEFT JOIN (SELECT ");
		sql.append("YYYYMM,BRBK_ID,TXN_ID,SUM(CNT) WOCNT,SUM(AMT) WOAMT ");
		sql.append("FROM ");
		sql.append("RPCLEARFEETAB_NW WHERE OP_TYPE='W' " + (StrUtils.isEmpty(condition)?"":"AND "+condition));
		sql.append("GROUP BY YYYYMM,BRBK_ID,TXN_ID) E ");
		sql.append("ON E.YYYYMM=A.YYYYMM AND E.BRBK_ID=A.BRBK_ID AND E.TXN_ID=A.TXN_ID ");
		
		//20150529 過濾失敗的資料
		sql.append("Where not ( coalesce(B.SendCNT,0)=0 and coalesce(C.OutCNT,0)=0 and coalesce(D.InCNT,0)=0 and coalesce(E.WoCNT,0)=0 ) ");

		sql.append("UNION ALL SELECT A.YYYYMM,A.BRBK_ID,A.TXN_ID,'E' STATUS,B.SENDCNT,(CASE WHEN B.SENDAMT < 0 THEN '-' ELSE '+' END) SENDAMTSIGN,ABS(B.SENDAMT) SENDAMT,C.OUTCNT,(CASE WHEN C.OUTAMT < 0 THEN '-' ELSE '+' END) OUTAMTSIGN,ABS(C.OUTAMT) OUTAMT,D.INCNT,(CASE WHEN D.INAMT < 0 THEN '-' ELSE '+' END) INAMTSIGN,ABS(D.INAMT) INAMT,E.WOCNT,(CASE WHEN E.WOAMT < 0 THEN '-' ELSE '+' END) WOAMTSIGN,ABS(E.WOAMT) WOAMT ");
		sql.append("FROM (SELECT ");
		sql.append("YYYYMM,BRBK_ID,TXN_ID ");
		sql.append("FROM ");
		sql.append("RPCLEARFEETAB_NW " + (StrUtils.isEmpty(condition)?"":"WHERE "+condition));
		sql.append("GROUP BY YYYYMM,BRBK_ID,TXN_ID) A ");
		
		sql.append("LEFT JOIN (SELECT ");
		sql.append("YYYYMM,BRBK_ID,TXN_ID,SUM(RVSCNT) SENDCNT,SUM(RVSAMT) SENDAMT ");
		sql.append("FROM ");
		sql.append("RPCLEARFEETAB_NW WHERE OP_TYPE='S' " + (StrUtils.isEmpty(condition)?"":"AND "+condition));
		sql.append("GROUP BY YYYYMM,BRBK_ID,TXN_ID) B ");
		sql.append("ON B.YYYYMM=A.YYYYMM AND B.BRBK_ID=A.BRBK_ID AND B.TXN_ID=A.TXN_ID ");
		
		sql.append("LEFT JOIN (SELECT ");
		sql.append("YYYYMM,BRBK_ID,TXN_ID,SUM(RVSCNT) OUTCNT,SUM(RVSAMT) OUTAMT ");
		sql.append("FROM ");
		sql.append("RPCLEARFEETAB_NW WHERE OP_TYPE='O' " + (StrUtils.isEmpty(condition)?"":"AND "+condition));
		sql.append("GROUP BY YYYYMM,BRBK_ID,TXN_ID) C ");
		sql.append("ON C.YYYYMM=A.YYYYMM AND C.BRBK_ID=A.BRBK_ID AND C.TXN_ID=A.TXN_ID ");

		sql.append("LEFT JOIN (SELECT ");
		sql.append("YYYYMM,BRBK_ID,TXN_ID,SUM(RVSCNT) INCNT,SUM(RVSAMT) INAMT ");
		sql.append("FROM ");
		sql.append("RPCLEARFEETAB_NW WHERE OP_TYPE='I' " + (StrUtils.isEmpty(condition)?"":"AND "+condition));
		sql.append("GROUP BY YYYYMM,BRBK_ID,TXN_ID) D ");
		sql.append("ON D.YYYYMM=A.YYYYMM AND D.BRBK_ID=A.BRBK_ID AND D.TXN_ID=A.TXN_ID ");
		
		sql.append("LEFT JOIN (SELECT ");
		sql.append("YYYYMM,BRBK_ID,TXN_ID,SUM(RVSCNT) WOCNT,SUM(RVSAMT) WOAMT ");
		sql.append("FROM ");
		sql.append("RPCLEARFEETAB_NW WHERE OP_TYPE='W' " + (StrUtils.isEmpty(condition)?"":"AND "+condition));
		sql.append("GROUP BY YYYYMM,BRBK_ID,TXN_ID) E ");
		sql.append("ON E.YYYYMM=A.YYYYMM AND E.BRBK_ID=A.BRBK_ID AND E.TXN_ID=A.TXN_ID ");
		
		//20150529 過濾失敗的資料
		sql.append("Where not ( coalesce(B.SendCNT,0)=0 and coalesce(C.OutCNT,0)=0 and coalesce(D.InCNT,0)=0 and coalesce(E.WoCNT,0)=0 ) ");
		sql.append("ORDER BY YYYYMM,BRBK_ID,TXN_ID,STATUS");
		
		SQL = sql.toString();
		logger.debug("SQL_NW==>" + SQL);
		
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
		//交易代號
		columnNameList.add("TXN_ID");
		columnLengthList.add(3);
		columnTypeList.add("string");
		//類別型態
		columnNameList.add("STATUS");
		columnLengthList.add(1);
		columnTypeList.add("string");
		//發動行筆數
		columnNameList.add("SENDCNT");
		columnLengthList.add(7);
		columnTypeList.add("number");
		//發動行金額正負號
		columnNameList.add("SENDAMTSIGN");
		columnLengthList.add(1);
		columnTypeList.add("string");
		//發動行金額
		columnNameList.add("SENDAMT");
		columnLengthList.add(12);
		columnTypeList.add("decimal(2)");
		//扣款行筆數
		columnNameList.add("OUTCNT");
		columnLengthList.add(7);
		columnTypeList.add("number");
		//扣款行金額正負號
		columnNameList.add("OUTAMTSIGN");
		columnLengthList.add(1);
		columnTypeList.add("string");
		//扣款行金額
		columnNameList.add("OUTAMT");
		columnLengthList.add(12);
		columnTypeList.add("decimal(2)");
		//入帳行筆數
		columnNameList.add("INCNT");
		columnLengthList.add(7);
		columnTypeList.add("number");
		//入帳行金額正負號
		columnNameList.add("INAMTSIGN");
		columnLengthList.add(1);
		columnTypeList.add("string");
		//入帳行金額
		columnNameList.add("INAMT");
		columnLengthList.add(12);
		columnTypeList.add("decimal(2)");
		//銷帳行筆數
		columnNameList.add("WOCNT");
		columnLengthList.add(7);
		columnTypeList.add("number");
		//銷帳行金額正負號
		columnNameList.add("WOAMTSIGN");
		columnLengthList.add(1);
		columnTypeList.add("string");
		//銷帳行金額
		columnNameList.add("WOAMT");
		columnLengthList.add(12);
		columnTypeList.add("decimal(2)");
		
		columnMap.put("columnName",columnNameList);
		columnMap.put("columnLength",columnLengthList);
		columnMap.put("columnType",columnTypeList);
		
		return columnMap;
	}
	public String getFirstRow(String YYYYMM){
		String firstRow = "";
		
		firstRow = "BOFEA24" + YYYYMM;
		
		return firstRow;
	}
	public String getLastRow(String dataCount){
		String lastRow = "";
		
		lastRow = "EOFEA24" + codeUtils.padText("number",6,dataCount);
		
		return lastRow;
	}
	public Map<String,Object> getTXT(String YYYYMM,String opbkId){
		Map<String,Object> dataMap = new HashMap<String,Object>();
		
		byte[] byteTXT = null;
		
		List<Map<String, Object>> queryListMap = codeUtils.queryListMap(getSQL(getCondition(YYYYMM,opbkId)),null);
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
			dataMap.put("message","查詢操作行="+opbkId+"的手續費月統計資料過程出現問題");
		}
		return dataMap;
	}
	
	public Map<String,Object> getTXT_NW(String YYYYMM,String opbkId){
		Map<String,Object> dataMap = new HashMap<String,Object>();
		
		byte[] byteTXT = null;
		
		List<Map<String, Object>> queryListMap = codeUtils.queryListMap(getSQL_NW(getCondition(YYYYMM,opbkId)),null);
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
			dataMap.put("message","查詢操作行="+opbkId+"的手續費月統計資料過程出現問題");
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
