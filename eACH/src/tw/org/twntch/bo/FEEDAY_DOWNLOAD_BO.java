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

public class FEEDAY_DOWNLOAD_BO {
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private CodeUtils codeUtils;

	//條件
	public String getCondition(String date,String opbkId,String clearingphase){
		List<String> conditions = new ArrayList<String>();
		String condition = "";
				
		if(StrUtils.isNotEmpty(date)){
			conditions.add(" BIZDATE = '" + date + "' ");
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
		
		sql.append("SELECT A.BIZDATE,A.CLEARINGPHASE,A.BRBK_ID,A.TXN_ID,'A' STATUS,B.SENDCNT,(CASE WHEN B.SENDAMT < 0 THEN '-' ELSE '+' END) SENDAMTSIGN,ABS(B.SENDAMT) SENDAMT,C.OUTCNT,(CASE WHEN C.OUTAMT < 0 THEN '-' ELSE '+' END) OUTAMTSIGN,ABS(C.OUTAMT) OUTAMT,D.INCNT,(CASE WHEN D.INAMT < 0 THEN '-' ELSE '+' END) INAMTSIGN,ABS(D.INAMT) INAMT,E.WOCNT,(CASE WHEN E.WOAMT < 0 THEN '-' ELSE '+' END) WOAMTSIGN,ABS(E.WOAMT) WOAMT ");
		sql.append("FROM (SELECT ");
		sql.append("BIZDATE,CLEARINGPHASE,BRBK_ID,TXN_ID ");
		sql.append("FROM ");
		sql.append("RPCLEARFEETAB " + (StrUtils.isEmpty(condition)?"":"WHERE "+condition));
		sql.append("GROUP BY BIZDATE,CLEARINGPHASE,BRBK_ID,TXN_ID) A ");
		
		sql.append("LEFT JOIN (SELECT ");
		sql.append("BIZDATE,CLEARINGPHASE,BRBK_ID,TXN_ID,SUM(CNT) SENDCNT,SUM(AMT) SENDAMT ");
		sql.append("FROM ");
		sql.append("RPCLEARFEETAB WHERE OP_TYPE='S' " + (StrUtils.isEmpty(condition)?"":"AND "+condition));
		sql.append("GROUP BY BIZDATE,CLEARINGPHASE,BRBK_ID,TXN_ID) B ");
		sql.append("ON B.BIZDATE=A.BIZDATE AND B.CLEARINGPHASE=A.CLEARINGPHASE AND B.BRBK_ID=A.BRBK_ID AND B.TXN_ID=A.TXN_ID ");
		
		sql.append("LEFT JOIN (SELECT ");
		sql.append("BIZDATE,CLEARINGPHASE,BRBK_ID,TXN_ID,SUM(CNT) OUTCNT,SUM(AMT) OUTAMT ");
		sql.append("FROM ");
		sql.append("RPCLEARFEETAB WHERE OP_TYPE='O' " + (StrUtils.isEmpty(condition)?"":"AND "+condition));
		sql.append("GROUP BY BIZDATE,CLEARINGPHASE,BRBK_ID,TXN_ID) C ");
		sql.append("ON C.BIZDATE=A.BIZDATE AND C.CLEARINGPHASE=A.CLEARINGPHASE AND C.BRBK_ID=A.BRBK_ID AND C.TXN_ID=A.TXN_ID ");

		sql.append("LEFT JOIN (SELECT ");
		sql.append("BIZDATE,CLEARINGPHASE,BRBK_ID,TXN_ID,SUM(CNT) INCNT,SUM(AMT) INAMT ");
		sql.append("FROM ");
		sql.append("RPCLEARFEETAB WHERE OP_TYPE='I' " + (StrUtils.isEmpty(condition)?"":"AND "+condition));
		sql.append("GROUP BY BIZDATE,CLEARINGPHASE,BRBK_ID,TXN_ID) D ");
		sql.append("ON D.BIZDATE=A.BIZDATE AND D.CLEARINGPHASE=A.CLEARINGPHASE AND D.BRBK_ID=A.BRBK_ID AND D.TXN_ID=A.TXN_ID ");
		
		sql.append("LEFT JOIN (SELECT ");
		sql.append("BIZDATE,CLEARINGPHASE,BRBK_ID,TXN_ID,SUM(CNT) WOCNT,SUM(AMT) WOAMT ");
		sql.append("FROM ");
		sql.append("RPCLEARFEETAB WHERE OP_TYPE='W' " + (StrUtils.isEmpty(condition)?"":"AND "+condition));
		sql.append("GROUP BY BIZDATE,CLEARINGPHASE,BRBK_ID,TXN_ID) E ");
		sql.append("ON E.BIZDATE=A.BIZDATE AND E.CLEARINGPHASE=A.CLEARINGPHASE AND E.BRBK_ID=A.BRBK_ID AND E.TXN_ID=A.TXN_ID ");
		
		//20150529 過濾失敗的資料
		sql.append("Where not ( coalesce(B.SendCNT,0)=0 and coalesce(C.OutCNT,0)=0 and coalesce(D.InCNT,0)=0 and coalesce(E.WoCNT,0)=0 ) ");


		sql.append("UNION ALL SELECT A.BIZDATE,A.CLEARINGPHASE,A.BRBK_ID,A.TXN_ID,'E' STATUS,B.SENDCNT,(CASE WHEN B.SENDAMT < 0 THEN '-' ELSE '+' END) SENDAMTSIGN,ABS(B.SENDAMT) SENDAMT,C.OUTCNT,(CASE WHEN C.OUTAMT < 0 THEN '-' ELSE '+' END) OUTAMTSIGN,ABS(C.OUTAMT) OUTAMT,D.INCNT,(CASE WHEN D.INAMT < 0 THEN '-' ELSE '+' END) INAMTSIGN,ABS(D.INAMT) INAMT,E.WOCNT,(CASE WHEN E.WOAMT < 0 THEN '-' ELSE '+' END) WOAMTSIGN,ABS(E.WOAMT) WOAMT ");
		sql.append("FROM (SELECT ");
		sql.append("BIZDATE,CLEARINGPHASE,BRBK_ID,TXN_ID ");
		sql.append("FROM ");
		sql.append("RPCLEARFEETAB " + (StrUtils.isEmpty(condition)?"":"WHERE "+condition));
		sql.append("GROUP BY BIZDATE,CLEARINGPHASE,BRBK_ID,TXN_ID) A ");
		
		sql.append("LEFT JOIN (SELECT ");
		sql.append("BIZDATE,CLEARINGPHASE,BRBK_ID,TXN_ID,SUM(RVSCNT) SENDCNT,SUM(RVSAMT) SENDAMT ");
		sql.append("FROM ");
		sql.append("RPCLEARFEETAB WHERE OP_TYPE='S' " + (StrUtils.isEmpty(condition)?"":"AND "+condition));
		sql.append("GROUP BY BIZDATE,CLEARINGPHASE,BRBK_ID,TXN_ID) B ");
		sql.append("ON B.BIZDATE=A.BIZDATE AND B.CLEARINGPHASE=A.CLEARINGPHASE AND B.BRBK_ID=A.BRBK_ID AND B.TXN_ID=A.TXN_ID ");
		
		sql.append("LEFT JOIN (SELECT ");
		sql.append("BIZDATE,CLEARINGPHASE,BRBK_ID,TXN_ID,SUM(RVSCNT) OUTCNT,SUM(RVSAMT) OUTAMT ");
		sql.append("FROM ");
		sql.append("RPCLEARFEETAB WHERE OP_TYPE='O' " + (StrUtils.isEmpty(condition)?"":"AND "+condition));
		sql.append("GROUP BY BIZDATE,CLEARINGPHASE,BRBK_ID,TXN_ID) C ");
		sql.append("ON C.BIZDATE=A.BIZDATE AND C.CLEARINGPHASE=A.CLEARINGPHASE AND C.BRBK_ID=A.BRBK_ID AND C.TXN_ID=A.TXN_ID ");

		sql.append("LEFT JOIN (SELECT ");
		sql.append("BIZDATE,CLEARINGPHASE,BRBK_ID,TXN_ID,SUM(RVSCNT) INCNT,SUM(RVSAMT) INAMT ");
		sql.append("FROM ");
		sql.append("RPCLEARFEETAB WHERE OP_TYPE='I' " + (StrUtils.isEmpty(condition)?"":"AND "+condition));
		sql.append("GROUP BY BIZDATE,CLEARINGPHASE,BRBK_ID,TXN_ID) D ");
		sql.append("ON D.BIZDATE=A.BIZDATE AND D.CLEARINGPHASE=A.CLEARINGPHASE AND D.BRBK_ID=A.BRBK_ID AND D.TXN_ID=A.TXN_ID ");

		sql.append("LEFT JOIN (SELECT ");
		sql.append("BIZDATE,CLEARINGPHASE,BRBK_ID,TXN_ID,SUM(RVSCNT) WOCNT,SUM(RVSAMT) WOAMT ");
		sql.append("FROM ");
		sql.append("RPCLEARFEETAB WHERE OP_TYPE='W' " + (StrUtils.isEmpty(condition)?"":"AND "+condition));
		sql.append("GROUP BY BIZDATE,CLEARINGPHASE,BRBK_ID,TXN_ID) E ");
		sql.append("ON E.BIZDATE=A.BIZDATE AND E.CLEARINGPHASE=A.CLEARINGPHASE AND E.BRBK_ID=A.BRBK_ID AND E.TXN_ID=A.TXN_ID ");
		
		//20150529 過濾失敗的資料
		sql.append("Where not ( coalesce(B.SendCNT,0)=0 and coalesce(C.OutCNT,0)=0 and coalesce(D.InCNT,0)=0 and coalesce(E.WoCNT,0)=0 ) ");
		sql.append("ORDER BY BIZDATE,CLEARINGPHASE,BRBK_ID,TXN_ID,STATUS");
		
		SQL = sql.toString();
		logger.debug("SQL==>" + SQL);
		
		return SQL;
	}
	
	public String getSQL_NW(String condition){
		StringBuffer sql = new StringBuffer();
		String SQL = "";
		
		sql.append("SELECT A.BIZDATE,A.CLEARINGPHASE,A.BRBK_ID,A.TXN_ID,'A' STATUS,B.SENDCNT,(CASE WHEN B.SENDAMT < 0 THEN '-' ELSE '+' END) SENDAMTSIGN,ABS(B.SENDAMT) SENDAMT,C.OUTCNT,(CASE WHEN C.OUTAMT < 0 THEN '-' ELSE '+' END) OUTAMTSIGN,ABS(C.OUTAMT) OUTAMT,D.INCNT,(CASE WHEN D.INAMT < 0 THEN '-' ELSE '+' END) INAMTSIGN,ABS(D.INAMT) INAMT,E.WOCNT,(CASE WHEN E.WOAMT < 0 THEN '-' ELSE '+' END) WOAMTSIGN,ABS(E.WOAMT) WOAMT ");
		sql.append("FROM (SELECT ");
		sql.append("BIZDATE,CLEARINGPHASE,BRBK_ID,TXN_ID ");
		sql.append("FROM ");
		sql.append("RPCLEARFEETAB_NW " + (StrUtils.isEmpty(condition)?"":"WHERE "+condition));
		sql.append("GROUP BY BIZDATE,CLEARINGPHASE,BRBK_ID,TXN_ID) A ");
		
		sql.append("LEFT JOIN (SELECT ");
		sql.append("BIZDATE,CLEARINGPHASE,BRBK_ID,TXN_ID,SUM(CNT) SENDCNT,SUM(AMT) SENDAMT ");
		sql.append("FROM ");
		sql.append("RPCLEARFEETAB_NW WHERE OP_TYPE='S' " + (StrUtils.isEmpty(condition)?"":"AND "+condition));
		sql.append("GROUP BY BIZDATE,CLEARINGPHASE,BRBK_ID,TXN_ID) B ");
		sql.append("ON B.BIZDATE=A.BIZDATE AND B.CLEARINGPHASE=A.CLEARINGPHASE AND B.BRBK_ID=A.BRBK_ID AND B.TXN_ID=A.TXN_ID ");
		
		sql.append("LEFT JOIN (SELECT ");
		sql.append("BIZDATE,CLEARINGPHASE,BRBK_ID,TXN_ID,SUM(CNT) OUTCNT,SUM(AMT) OUTAMT ");
		sql.append("FROM ");
		sql.append("RPCLEARFEETAB_NW WHERE OP_TYPE='O' " + (StrUtils.isEmpty(condition)?"":"AND "+condition));
		sql.append("GROUP BY BIZDATE,CLEARINGPHASE,BRBK_ID,TXN_ID) C ");
		sql.append("ON C.BIZDATE=A.BIZDATE AND C.CLEARINGPHASE=A.CLEARINGPHASE AND C.BRBK_ID=A.BRBK_ID AND C.TXN_ID=A.TXN_ID ");

		sql.append("LEFT JOIN (SELECT ");
		sql.append("BIZDATE,CLEARINGPHASE,BRBK_ID,TXN_ID,SUM(CNT) INCNT,SUM(AMT) INAMT ");
		sql.append("FROM ");
		sql.append("RPCLEARFEETAB_NW WHERE OP_TYPE='I' " + (StrUtils.isEmpty(condition)?"":"AND "+condition));
		sql.append("GROUP BY BIZDATE,CLEARINGPHASE,BRBK_ID,TXN_ID) D ");
		sql.append("ON D.BIZDATE=A.BIZDATE AND D.CLEARINGPHASE=A.CLEARINGPHASE AND D.BRBK_ID=A.BRBK_ID AND D.TXN_ID=A.TXN_ID ");
		
		sql.append("LEFT JOIN (SELECT ");
		sql.append("BIZDATE,CLEARINGPHASE,BRBK_ID,TXN_ID,SUM(CNT) WOCNT,SUM(AMT) WOAMT ");
		sql.append("FROM ");
		sql.append("RPCLEARFEETAB_NW WHERE OP_TYPE='W' " + (StrUtils.isEmpty(condition)?"":"AND "+condition));
		sql.append("GROUP BY BIZDATE,CLEARINGPHASE,BRBK_ID,TXN_ID) E ");
		sql.append("ON E.BIZDATE=A.BIZDATE AND E.CLEARINGPHASE=A.CLEARINGPHASE AND E.BRBK_ID=A.BRBK_ID AND E.TXN_ID=A.TXN_ID ");
		
		//20150529 過濾失敗的資料
		sql.append("Where not ( coalesce(B.SendCNT,0)=0 and coalesce(C.OutCNT,0)=0 and coalesce(D.InCNT,0)=0 and coalesce(E.WoCNT,0)=0 ) ");


		sql.append("UNION ALL SELECT A.BIZDATE,A.CLEARINGPHASE,A.BRBK_ID,A.TXN_ID,'E' STATUS,B.SENDCNT,(CASE WHEN B.SENDAMT < 0 THEN '-' ELSE '+' END) SENDAMTSIGN,ABS(B.SENDAMT) SENDAMT,C.OUTCNT,(CASE WHEN C.OUTAMT < 0 THEN '-' ELSE '+' END) OUTAMTSIGN,ABS(C.OUTAMT) OUTAMT,D.INCNT,(CASE WHEN D.INAMT < 0 THEN '-' ELSE '+' END) INAMTSIGN,ABS(D.INAMT) INAMT,E.WOCNT,(CASE WHEN E.WOAMT < 0 THEN '-' ELSE '+' END) WOAMTSIGN,ABS(E.WOAMT) WOAMT ");
		sql.append("FROM (SELECT ");
		sql.append("BIZDATE,CLEARINGPHASE,BRBK_ID,TXN_ID ");
		sql.append("FROM ");
		sql.append("RPCLEARFEETAB_NW " + (StrUtils.isEmpty(condition)?"":"WHERE "+condition));
		sql.append("GROUP BY BIZDATE,CLEARINGPHASE,BRBK_ID,TXN_ID) A ");
		
		sql.append("LEFT JOIN (SELECT ");
		sql.append("BIZDATE,CLEARINGPHASE,BRBK_ID,TXN_ID,SUM(RVSCNT) SENDCNT,SUM(RVSAMT) SENDAMT ");
		sql.append("FROM ");
		sql.append("RPCLEARFEETAB_NW WHERE OP_TYPE='S' " + (StrUtils.isEmpty(condition)?"":"AND "+condition));
		sql.append("GROUP BY BIZDATE,CLEARINGPHASE,BRBK_ID,TXN_ID) B ");
		sql.append("ON B.BIZDATE=A.BIZDATE AND B.CLEARINGPHASE=A.CLEARINGPHASE AND B.BRBK_ID=A.BRBK_ID AND B.TXN_ID=A.TXN_ID ");
		
		sql.append("LEFT JOIN (SELECT ");
		sql.append("BIZDATE,CLEARINGPHASE,BRBK_ID,TXN_ID,SUM(RVSCNT) OUTCNT,SUM(RVSAMT) OUTAMT ");
		sql.append("FROM ");
		sql.append("RPCLEARFEETAB_NW WHERE OP_TYPE='O' " + (StrUtils.isEmpty(condition)?"":"AND "+condition));
		sql.append("GROUP BY BIZDATE,CLEARINGPHASE,BRBK_ID,TXN_ID) C ");
		sql.append("ON C.BIZDATE=A.BIZDATE AND C.CLEARINGPHASE=A.CLEARINGPHASE AND C.BRBK_ID=A.BRBK_ID AND C.TXN_ID=A.TXN_ID ");

		sql.append("LEFT JOIN (SELECT ");
		sql.append("BIZDATE,CLEARINGPHASE,BRBK_ID,TXN_ID,SUM(RVSCNT) INCNT,SUM(RVSAMT) INAMT ");
		sql.append("FROM ");
		sql.append("RPCLEARFEETAB_NW WHERE OP_TYPE='I' " + (StrUtils.isEmpty(condition)?"":"AND "+condition));
		sql.append("GROUP BY BIZDATE,CLEARINGPHASE,BRBK_ID,TXN_ID) D ");
		sql.append("ON D.BIZDATE=A.BIZDATE AND D.CLEARINGPHASE=A.CLEARINGPHASE AND D.BRBK_ID=A.BRBK_ID AND D.TXN_ID=A.TXN_ID ");

		sql.append("LEFT JOIN (SELECT ");
		sql.append("BIZDATE,CLEARINGPHASE,BRBK_ID,TXN_ID,SUM(RVSCNT) WOCNT,SUM(RVSAMT) WOAMT ");
		sql.append("FROM ");
		sql.append("RPCLEARFEETAB_NW WHERE OP_TYPE='W' " + (StrUtils.isEmpty(condition)?"":"AND "+condition));
		sql.append("GROUP BY BIZDATE,CLEARINGPHASE,BRBK_ID,TXN_ID) E ");
		sql.append("ON E.BIZDATE=A.BIZDATE AND E.CLEARINGPHASE=A.CLEARINGPHASE AND E.BRBK_ID=A.BRBK_ID AND E.TXN_ID=A.TXN_ID ");
		
		//20150529 過濾失敗的資料
		sql.append("Where not ( coalesce(B.SendCNT,0)=0 and coalesce(C.OutCNT,0)=0 and coalesce(D.InCNT,0)=0 and coalesce(E.WoCNT,0)=0 ) ");
		sql.append("ORDER BY BIZDATE,CLEARINGPHASE,BRBK_ID,TXN_ID,STATUS");
		
		SQL = sql.toString();
		logger.debug("SQL_NW==>" + SQL);
		
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
		columnLengthList.add(11);
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
		columnLengthList.add(11);
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
		columnLengthList.add(11);
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
		columnLengthList.add(11);
		columnTypeList.add("decimal(2)");
		
		columnMap.put("columnName",columnNameList);
		columnMap.put("columnLength",columnLengthList);
		columnMap.put("columnType",columnTypeList);
		
		return columnMap;
	}
	public String getFirstRow(String date){
		String firstRow = "";
		
		firstRow = "BOFEA23" + date;
		
		return firstRow;
	}
	public String getLastRow(String dataCount){
		String lastRow = "";
//		20150609 edit by hugo req by UAT-20150604-06
//		lastRow = "EOFEA24" + codeUtils.padText("number",6,dataCount);
		lastRow = "EOFEA23" + codeUtils.padText("number",6,dataCount);
		
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
			dataMap.put("message","查詢操作行="+opbkId+"的手續費日統計資料過程出現問題");
		}
		return dataMap;
	}
	
	public Map<String,Object> getTXT_NW(String date,String opbkId,String clearingphase){
		Map<String,Object> dataMap = new HashMap<String,Object>();
		
		byte[] byteTXT = null;
		
		List<Map<String, Object>> queryListMap = codeUtils.queryListMap(getSQL_NW(getCondition(date,opbkId,clearingphase)),null);
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
			dataMap.put("message","查詢操作行="+opbkId+"的手續費日統計資料過程出現問題");
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
