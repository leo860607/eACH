package tw.org.twntch.bo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tw.org.twntch.db.dao.hibernate.BANK_BRANCH_Dao;
import tw.org.twntch.db.dao.hibernate.RPONBLOCKTAB_Dao;
import tw.org.twntch.po.BANK_BRANCH;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.RptUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.zDateHandler;

public class RPTFEE_2_BO {
	private RPONBLOCKTAB_Dao rponblocktab_Dao;
	private BANK_BRANCH_Dao bank_branch_Dao;
	
	private class IndexKey {
		int index;
		String bgbkId;
		String acctCode;
		BigDecimal adjFee;
		public IndexKey() {
			// TODO Auto-generated constructor stub
		}
		public IndexKey(int index, String bgbkId, String acctCode, BigDecimal adjFee) {
			super();
			this.index = index;
			this.bgbkId = bgbkId;
			if(acctCode.equals("一般")){this.acctCode = "1";}else if(acctCode.equals("沖正")){this.acctCode = "0";}else{this.acctCode = acctCode;}
			this.adjFee = adjFee;
		}
		public int getIndex() {
			return index;
		}
		public void setIndex(int index) {
			this.index = index;
		}
		public String getBgbkId() {
			return bgbkId;
		}
		public void setBgbkId(String bgbkId) {
			this.bgbkId = bgbkId;
		}
		public String getAcctCode() {
			return acctCode;
		}
		public void setAcctCode(String acctCode) {
			this.acctCode = acctCode;
		}
		public BigDecimal getAdjFee() {
			return adjFee;
		}
		public void setAdjFee(BigDecimal adjFee) {
			this.adjFee = adjFee;
		}
	}
	
	/**
	 * 取得分行代號清單(AJAX)
	 * @return
	 */
	public String getBank_branch_List(Map<String, String> params){
		String bgbkId = StrUtils.isEmpty(params.get("bgbkId"))?"":params.get("bgbkId");
		List<Map> listRet = new ArrayList<Map>();
		Map<String,String> m = new HashMap<String,String>();
		List<BANK_BRANCH> list = null;
		String json= "";
		try {
			if(StrUtils.isEmpty(bgbkId)){
				list = bank_branch_Dao.getAll();
			}else{
				list = bank_branch_Dao.getDataByBgBkId(bgbkId);
			}
			if(list!=null){
				for(BANK_BRANCH po :list){
					m = new HashMap<String,String>();
					m.put("label", po.getId().getBRBK_ID()+" - "+po.getBRBK_NAME());
					m.put("value", po.getId().getBRBK_ID());
					listRet.add(m);
				}
			}
			json = JSONUtils.toJson(listRet);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("getBank_branch_List.Exception>>"+e);
		}
		return json;
	}
	
	public Map<String, String> ex_export(String ext, String twYear, String twMonth, String opbkId, String bgbkId, String brbkId, String serchStrs){
		return export(ext, twYear, twMonth, opbkId, bgbkId, brbkId, serchStrs);
	}
	
	public Map<String, String> export(String ext, String twYear, String twMonth, String opbkId, String bgbkId, String brbkId, String serchStrs){
		Map<String, String> rtnMap = null;
		
		try{
			rtnMap = new HashMap<String, String>();
			rtnMap.put("serchStrs", serchStrs);
			Map<String, Object> params = new HashMap<String, Object>();
			//「民國年」為必填
			params.put("V_TXDT", Integer.valueOf(twYear) + " 年 " + twMonth + " 月");
			params.put("V_PRINT_DATE", DateTimeUtils.convertDate(DateTimeUtils.INTERCONVERSION, zDateHandler.getTheDate(), "yyyy/MM/dd", "yyy/MM/dd"));
			
			BigDecimal tchAdjFee = new BigDecimal("0");
			List tchAdjFeeList = rponblocktab_Dao.getRptData(getTchAdjFeeSql(twYear, twMonth), new ArrayList());
			if(tchAdjFeeList != null && tchAdjFeeList.size() > 0){
				tchAdjFee = (BigDecimal)((Map)tchAdjFeeList.get(0)).get("FEE");
			}
			params.put("V_TCH_ADJ_FEE", tchAdjFee);
			//System.out.println("params >> " + params);
			
			List list = rponblocktab_Dao.getRptData(getSQL(twYear, twMonth, opbkId, bgbkId, brbkId), new ArrayList());
			//System.out.println("datalist >> "+ list);
			
			//調整調帳資料(若有某銀行有一般及沖正資料，則調帳金額僅需放置於一般即可)
			list = dataTunning(list);
			
			int pathType = Arguments.getStringArg("ISWINOPEN").equals("Y")?RptUtils.C_PATH :RptUtils.R_PATH;
			String outputFilePath = "";
			if(ext.equals("xls")){
				outputFilePath = RptUtils.export(RptUtils.COLLECTION,pathType, "ex_fee_2", "fee_2", params, list, 2);
			}else if(ext.equals("pdf")){
				outputFilePath = RptUtils.export(RptUtils.COLLECTION,pathType, "fee_2", "fee_2", params, list);
			}
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
	
	public String getSQL(String twYear, String twMonth, String opbkId, String bgbkId, String brbkId){
		StringBuffer sql = new StringBuffer();
		StringBuffer bfaSql = new StringBuffer();
		
		List<String> conditions = new ArrayList<String>();
		List<String> bfaConditions = new ArrayList<String>();
		if(StrUtils.isNotEmpty(twYear) && StrUtils.isNotEmpty(twMonth)){
			conditions.add(" YYYYMM = '" + DateTimeUtils.convertDate(DateTimeUtils.INTERCONVERSION, (twYear + twMonth), "yyyyMM", "yyyyMM") + "' ");
			bfaConditions.add(" BFA.YYYYMM = '" + (twYear + twMonth) + "' ");
		}
		if(StrUtils.isNotEmpty(opbkId)){
			conditions.add(" OPBK_ID = '" + opbkId + "' ");
			//bfaConditions.add(" BG.OPBK_ID = '" + opbkId + "' ");
			
			bfaConditions.add(" GET_CUR_OPBKID(BG.BGBK_ID, BFA.ACTIVE_DATE, 0) = '" + opbkId + "' ");
		}
		if(StrUtils.isNotEmpty(bgbkId)){
			conditions.add(" BGBK_ID = '" + bgbkId + "' ");
			bfaConditions.add(" BG.BGBK_ID = '" + bgbkId + "' ");
		}
		if(StrUtils.isNotEmpty(brbkId)){
			conditions.add(" BRBK_ID = '" + brbkId + "' ");
			bfaConditions.add(" BFA.BRBK_ID = '" + brbkId + "' ");
		}
		String where = combine(conditions);
		String bfaWhere = combine(bfaConditions);
		
		bfaSql.append("SELECT ");
		bfaSql.append("BFA.YYYYMM, BG.BGBK_ID, SUM(FEE) AS FEE ");
		bfaSql.append("FROM BRBK_FEE_ADJ AS BFA ");
		bfaSql.append("LEFT JOIN BANK_BRANCH AS BR ON BFA.BRBK_ID = BR.BRBK_ID ");
		bfaSql.append("LEFT JOIN BANK_GROUP AS BG ON BR.BGBK_ID = BG.BGBK_ID ");
		//bfaSql.append("LEFT JOIN BANK_GROUP AS OP ON BG.OPBK_ID = OP.BGBK_ID ");
		bfaSql.append("WHERE COALESCE(BFA.ACTIVE_DATE,'') <> '' AND BFA.BRBK_ID <> '0188888' ");
		bfaSql.append( (StrUtils.isEmpty(bfaWhere)?"":"AND " + bfaWhere) );
		bfaSql.append("GROUP BY BFA.YYYYMM, BG.BGBK_ID ");
		bfaSql.append("HAVING SUM(FEE) <> 0 ");
		System.out.println("BFA.SQL >> " + bfaSql);
		
		sql.append("WITH TEMP AS ( ");
		sql.append("	SELECT '1' AS ACCTCODE, BIZDATE, CLEARINGPHASE, BRBK_ID, BGBK_ID, TXN_ID, SENDERID, BUSINESS_TYPE_ID, OP_TYPE, BRBK_NAME, BGBK_NAME, OPBK_ID, OPBK_NAME, TXN_NAME, COMPANY_ABBR_NAME, BUSINESS_TYPE_NAME, ");
		sql.append("	('0'||VARCHAR(CAST(YYYYMM AS INT) - 191100)) AS YYYYMM, CNT, AMT, 0 AS RVSCNT, 0 AS RVSAMT      ");
		sql.append("	FROM RPCLEARFEETAB_NW ");
		sql.append("	WHERE COALESCE(CNT,0) <> 0 ");
		sql.append("	 " + (StrUtils.isEmpty(where)?"":"AND " + where));
		sql.append("	UNION ALL ");
		sql.append("	SELECT '0' AS ACCTCODE, BIZDATE, CLEARINGPHASE, BRBK_ID, BGBK_ID, TXN_ID, SENDERID, BUSINESS_TYPE_ID, OP_TYPE, BRBK_NAME, BGBK_NAME, OPBK_ID, OPBK_NAME, TXN_NAME, COMPANY_ABBR_NAME, BUSINESS_TYPE_NAME, ");
		sql.append("	('0'||VARCHAR(CAST(YYYYMM AS INT) - 191100)) AS YYYYMM, 0 AS CNT, 0 AS AMT, RVSCNT, RVSAMT ");
		sql.append("	FROM RPCLEARFEETAB_NW ");
		sql.append("	WHERE COALESCE(RVSCNT,0) <> 0 ");
		sql.append("	 " + (StrUtils.isEmpty(where)?"":"AND " + where));
		sql.append(") ");

		sql.append("SELECT "); 
		sql.append("COALESCE(A.YYYYMM, B.YYYYMM) AS YYYYMM, ");
		sql.append("COALESCE(A.BGBK_ID, B.BGBK_ID) AS BGBK_ID, ");
		sql.append("(SELECT VARCHAR(BGBK_NAME) FROM BANK_GROUP WHERE BGBK_ID = COALESCE(A.BGBK_ID, B.BGBK_ID)) AS BGBK_NAME, ");
		sql.append("(CASE A.ACCTCODE WHEN '0' THEN '沖正' ELSE '一般' END) AS ACCTCODE ");
		sql.append(",COALESCE((SELECT SUM((CASE A.ACCTCODE WHEN '1' THEN COALESCE(CNT,0) ELSE COALESCE(RVSCNT,0) END)) FROM TEMP WHERE YYYYMM = A.YYYYMM AND BGBK_ID = A.BGBK_ID AND ACCTCODE = A.ACCTCODE AND OP_TYPE = 'S'),0) AS FIRECOUNT "); 
		sql.append(",COALESCE((SELECT SUM((CASE A.ACCTCODE WHEN '1' THEN COALESCE(AMT,0) ELSE COALESCE(RVSAMT,0) END)) FROM TEMP WHERE YYYYMM = A.YYYYMM AND BGBK_ID = A.BGBK_ID AND ACCTCODE = A.ACCTCODE AND OP_TYPE = 'S'),0) AS FIREAMT "); 
		sql.append(",COALESCE((SELECT SUM((CASE A.ACCTCODE WHEN '1' THEN COALESCE(CNT,0) ELSE COALESCE(RVSCNT,0) END)) FROM TEMP WHERE YYYYMM = A.YYYYMM AND BGBK_ID = A.BGBK_ID AND ACCTCODE = A.ACCTCODE AND OP_TYPE = 'O'),0) AS DEBITCOUNT "); 
		sql.append(",COALESCE((SELECT SUM((CASE A.ACCTCODE WHEN '1' THEN COALESCE(AMT,0) ELSE COALESCE(RVSAMT,0) END)) FROM TEMP WHERE YYYYMM = A.YYYYMM AND BGBK_ID = A.BGBK_ID AND ACCTCODE = A.ACCTCODE AND OP_TYPE = 'O'),0) AS DEBITAMT "); 
		sql.append(",COALESCE((SELECT SUM((CASE A.ACCTCODE WHEN '1' THEN COALESCE(CNT,0) ELSE COALESCE(RVSCNT,0) END)) FROM TEMP WHERE YYYYMM = A.YYYYMM AND BGBK_ID = A.BGBK_ID AND ACCTCODE = A.ACCTCODE AND OP_TYPE = 'I'),0) AS SAVECOUNT "); 
		sql.append(",COALESCE((SELECT SUM((CASE A.ACCTCODE WHEN '1' THEN COALESCE(AMT,0) ELSE COALESCE(RVSAMT,0) END)) FROM TEMP WHERE YYYYMM = A.YYYYMM AND BGBK_ID = A.BGBK_ID AND ACCTCODE = A.ACCTCODE AND OP_TYPE = 'I'),0) AS SAVEAMT ");
		sql.append(",COALESCE((SELECT SUM((CASE A.ACCTCODE WHEN '1' THEN COALESCE(CNT,0) ELSE COALESCE(RVSCNT,0) END)) FROM TEMP WHERE YYYYMM = A.YYYYMM AND BGBK_ID = A.BGBK_ID AND ACCTCODE = A.ACCTCODE AND OP_TYPE = 'W'),0) AS CANCELCOUNT "); 
		sql.append(",COALESCE((SELECT SUM((CASE A.ACCTCODE WHEN '1' THEN COALESCE(AMT,0) ELSE COALESCE(RVSAMT,0) END)) FROM TEMP WHERE YYYYMM = A.YYYYMM AND BGBK_ID = A.BGBK_ID AND ACCTCODE = A.ACCTCODE AND OP_TYPE = 'W'),0) AS CANCELAMT ");
		sql.append(",COALESCE(B.FEE,0) AS ADJ_FEE ");
		sql.append("FROM ( ");
		sql.append("	SELECT YYYYMM, BGBK_ID, ACCTCODE FROM TEMP GROUP BY YYYYMM, BGBK_ID, ACCTCODE ");
		sql.append(") AS A FULL JOIN ( ");
		sql.append(bfaSql);
		sql.append(") AS B ON A.YYYYMM = B.YYYYMM AND A.BGBK_ID = B.BGBK_ID ");
		sql.append("ORDER BY ACCTCODE, BGBK_ID ");
		System.out.println("SQL >> " + sql.toString());
		return sql.toString();
	}
	
	public String getTchAdjFeeSql(String twYear, String twMonth){
		StringBuilder sql = new StringBuilder();
		List<String> conditions = new ArrayList<String>();
		
		if(StrUtils.isNotEmpty(twYear) && StrUtils.isNotEmpty(twMonth)){
			conditions.add(" YYYYMM = '" + (twYear + twMonth) + "' ");
		}
		String where = combine(conditions);
		
		sql.append("SELECT FEE FROM BRBK_FEE_ADJ WHERE BRBK_ID = '0188888' " + (StrUtils.isNotEmpty(where)?"AND " + where:""));
		
		System.out.println("TCH_ADJ_FEE.SQL >> " + sql);
		
		return sql.toString();
	}
	
	//調整調帳資料(若有某銀行有一般及沖正資料，則調帳金額僅需放置於一般即可)
	public List dataTunning(List<Map> odataList){
		List<Map> newList = new ArrayList<Map>();
		Map<String, IndexKey> indexTable = new HashMap<String, IndexKey>();
		String key = "";
		Map data = null;
		for(int i = 0; i < odataList.size(); i++){
			data = odataList.get(i);
			key = genIndexTableKey(data.get("BGBK_ID"), data.get("ACCTCODE"));
			IndexKey ik = isKeyExists(key, indexTable);
			if(ik != null){
				if(ik.getAcctCode().equals("0")){
					Map oData = newList.get(ik.getIndex());
					oData.put("ADJ_FEE", 0);
					newList.set(ik.getIndex(), oData);
				}else if(ik.getAcctCode().equals("1")){
					data.put("ADJ_FEE", 0);
				}
				newList.add(data);
			}else{
				newList.add(data);
				indexTable.put(key, new IndexKey(i, (String)data.get("BGBK_ID"), (String)data.get("ACCTCODE"), (BigDecimal)data.get("ADJ_FEE")));
			}
		}
		return newList;
		
	}
	
	public String genIndexTableKey(Object bgbkId, Object acctCode){
		return bgbkId+"_"+(acctCode.equals("一般")?"N":(acctCode.equals("沖正")?"R":"E"));
	}
	
	public IndexKey isKeyExists(String key, Map<String, IndexKey> indexTable){
		IndexKey indexKey = null;
		String bgbkId = key.split("_")[0];
		
		String existKey = genIndexTableKey(bgbkId, "一般");
		if(indexTable.containsKey(existKey)){
			indexKey = indexTable.get(existKey);
		}
		
		if(indexKey == null){
			existKey = genIndexTableKey(bgbkId, "沖正");
			if(indexTable.containsKey(existKey)){
				indexKey = indexTable.get(existKey);
			}
		}
		return indexKey;
	}
	
	public String combine(List<String> conditions){
		String conStr = "";
		for(int i = 0 ; i < conditions.size(); i++){
			conStr += conditions.get(i);
			if(i < conditions.size() - 1){
				conStr += " AND ";
			}
		}
		return conStr;
	}
	
	public RPONBLOCKTAB_Dao getRponblocktab_Dao() {
		return rponblocktab_Dao;
	}

	public void setRponblocktab_Dao(RPONBLOCKTAB_Dao rponblocktab_Dao) {
		this.rponblocktab_Dao = rponblocktab_Dao;
	}

	public BANK_BRANCH_Dao getBank_branch_Dao() {
		return bank_branch_Dao;
	}

	public void setBank_branch_Dao(BANK_BRANCH_Dao bank_branch_Dao) {
		this.bank_branch_Dao = bank_branch_Dao;
	}
}
