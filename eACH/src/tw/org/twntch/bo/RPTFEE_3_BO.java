
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

public class RPTFEE_3_BO {
	private RPONBLOCKTAB_Dao rponblocktab_Dao;
	private BANK_BRANCH_Dao bank_branch_Dao;
	
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
	
	public Map<String, String> ex_export(String ext, String twYear, String twMonth, String opbkId, String bgbkId, String serchStrs){
		return export(ext, twYear, twMonth, opbkId, bgbkId, serchStrs);
	}
	
	public Map<String, String> export(String ext, String twYear, String twMonth, String opbkId, String bgbkId, String serchStrs){
		Map<String, String> rtnMap = null;
		
		try{
			rtnMap = new HashMap<String, String>();
			rtnMap.put("serchStrs", serchStrs);
			Map<String, Object> params = new HashMap<String, Object>();
			//「民國年」為必填
			params.put("V_TXDT", Integer.valueOf(twYear) + " 年 " + twMonth + " 月");
			params.put("V_PRINT_DATE", DateTimeUtils.convertDate(DateTimeUtils.INTERCONVERSION, zDateHandler.getTheDate(), "yyyy/MM/dd", "yyy/MM/dd"));
			//System.out.println("params >> " + params);
			
			//交換所調帳金額
			BigDecimal tchAdjFee = new BigDecimal("0");
			List tchAdjFeeList = rponblocktab_Dao.getRptData(getTchAdjFeeSql(twYear, twMonth), new ArrayList());
			if(tchAdjFeeList != null && tchAdjFeeList.size() > 0){
				tchAdjFee = (BigDecimal)((Map)tchAdjFeeList.get(0)).get("FEE");
			}
			params.put("V_TCH_ADJ_FEE", tchAdjFee);
			
			List list = rponblocktab_Dao.getRptData(getSQL(twYear, twMonth, opbkId, bgbkId), new ArrayList());
			//System.out.println("datalist >> "+ list);
			
			int pathType = Arguments.getStringArg("ISWINOPEN").equals("Y")?RptUtils.C_PATH :RptUtils.R_PATH;
			String outputFilePath = "";
			if(ext.equals("xls")){
				outputFilePath = RptUtils.export(RptUtils.COLLECTION,pathType, "ex_fee_3", "fee_3", params, list, 2);
			}else if(ext.equals("pdf")){
				outputFilePath = RptUtils.export(RptUtils.COLLECTION,pathType, "fee_3", "fee_3", params, list);
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
	
	public String getSQL(String twYear, String twMonth, String opbkId, String bgbkId){
		StringBuffer sql = new StringBuffer();
		
		List<String> conditions = new ArrayList<String>();
		List<String> bfaConditions = new ArrayList<String>();
		if(StrUtils.isNotEmpty(twYear) && StrUtils.isNotEmpty(twMonth)){
			conditions.add(" A.YYYYMM = '" + DateTimeUtils.convertDate(DateTimeUtils.INTERCONVERSION, (twYear + twMonth), "yyyyMM", "yyyyMM") + "' ");
			bfaConditions.add(" T.YYYYMM = '" + (twYear + twMonth) + "' ");
		}
		if(StrUtils.isNotEmpty(opbkId)){
			conditions.add(" A.OPBK_ID = '" + opbkId + "' ");
			//bfaConditions_2.add(" BG.OPBK_ID = '" + opbkId + "' ");
			bfaConditions.add(" T.OPBK_ID = '" + opbkId + "' ");
		}
		if(StrUtils.isNotEmpty(bgbkId)){
			conditions.add(" A.BGBK_ID = '" + bgbkId + "' ");
			bfaConditions.add(" T.BGBK_ID = '" + bgbkId + "' ");
		}
		String where = combine(conditions);
		String bfaWhere = combine(bfaConditions);
		
		/*
		sql.append("WITH TEMP AS ( ");
		sql.append("    SELECT D.YYYYMM, D.BGBK_ID, BG.OPBK_ID, D.FEE ");
		sql.append("    FROM ( ");
		sql.append("        SELECT T.YYYYMM, T.BGBK_ID, SUM(T.FEE) AS FEE FROM ( ");
		sql.append("            SELECT BFA.YYYYMM, BFA.BRBK_ID, FEE, ");
		sql.append("            COALESCE(BR.BGBK_ID,(CASE BFA.BRBK_ID WHEN '0188888' THEN '0188888' ELSE '' END)) AS BGBK_ID ");
		sql.append("            FROM BRBK_FEE_ADJ AS BFA LEFT JOIN BANK_BRANCH AS BR ON BFA.BRBK_ID = BR.BRBK_ID ");
		sql.append("			WHERE COALESCE(BFA.ACTIVE_DATE,'') <> '' AND BFA.BRBK_ID <> '0188888' ");
		sql.append("			" + (StrUtils.isNotEmpty(bfaWhere_1)?"AND " + bfaWhere_1:""));
		sql.append("        ) AS T GROUP BY T.YYYYMM, T.BGBK_ID ");
		sql.append("    ) AS D LEFT JOIN BANK_GROUP AS BG ON D.BGBK_ID = BG.BGBK_ID ");
		sql.append("	" + (StrUtils.isNotEmpty(bfaWhere_2)?"WHERE " + bfaWhere_2:""));
		sql.append(") ");
		*/
		sql.append("WITH TEMP AS ( ");
		sql.append("	SELECT T.YYYYMM, T.OPBK_ID, T.BGBK_ID, SUM(T.FEE) AS FEE FROM ( ");
		sql.append("	    SELECT BFA.YYYYMM, BFA.BRBK_ID, FEE, ");
		sql.append("	    GETBKHEADID(BFA.BRBK_ID) AS BGBK_ID, ");
		sql.append("	    GET_CUR_OPBKID(GETBKHEADID(BFA.BRBK_ID), BFA.ACTIVE_DATE, 0) AS OPBK_ID ");
		sql.append("	    FROM BRBK_FEE_ADJ AS BFA ");
		sql.append("	    WHERE COALESCE(BFA.ACTIVE_DATE,'') <> '' AND BFA.BRBK_ID <> '0188888' ");
		sql.append("	) AS T ");
		sql.append("    " + (StrUtils.isNotEmpty(bfaWhere)?"WHERE " + bfaWhere:""));
		sql.append("    GROUP BY T.YYYYMM, T.OPBK_ID, T.BGBK_ID ");
		sql.append(") ");

		sql.append("SELECT ");
		sql.append("COALESCE(B.YYYYMM, VARCHAR(INT(C.YYYYMM) + 191100)) AS YYYYMM, ");
		sql.append("COALESCE(B.BGBK_ID, C.BGBK_ID) AS BGBK_ID, ");
		sql.append("COALESCE(BGBK_NAME, (SELECT BGBK_NAME FROM BANK_GROUP WHERE BGBK_ID = C.BGBK_ID)) AS BGBK_NAME, ");
		sql.append("COALESCE(FIRECOUNT,0) AS FIRECOUNT, COALESCE(FIREAMT,0) AS FIREAMT, ");
		sql.append("COALESCE(DEBITCOUNT,0) AS DEBITCOUNT, COALESCE(DEBITAMT,0) AS DEBITAMT, ");
		sql.append("COALESCE(SAVECOUNT,0) AS SAVECOUNT, COALESCE(SAVEAMT,0) AS SAVEAMT, ");
		sql.append("COALESCE(CANCELCOUNT,0) AS CANCELCOUNT, COALESCE(CANCELAMT,0) AS CANCELAMT, ");
		sql.append("COALESCE(B.ADJ_FEE, COALESCE(C.FEE, 0)) AS ADJ_FEE ");
		sql.append("FROM ( ");
		sql.append("    SELECT ");
		sql.append("    A.YYYYMM, VARCHAR(A.BGBK_ID) AS BGBK_ID, (SELECT BGBK_NAME FROM BANK_GROUP WHERE BGBK_ID = A.BGBK_ID) AS BGBK_NAME ");
		sql.append("    ,(SELECT SUM(CNT+RVSCNT) FROM RPCLEARFEETAB_NW WHERE YYYYMM = A.YYYYMM AND BGBK_ID = A.BGBK_ID AND OP_TYPE = 'S') AS FIRECOUNT ");
		sql.append("    ,(SELECT SUM(AMT+RVSAMT) FROM RPCLEARFEETAB_NW WHERE YYYYMM = A.YYYYMM AND BGBK_ID = A.BGBK_ID AND OP_TYPE = 'S') AS FIREAMT ");
		sql.append("    ,(SELECT SUM(CNT+RVSCNT) FROM RPCLEARFEETAB_NW WHERE YYYYMM = A.YYYYMM AND BGBK_ID = A.BGBK_ID AND OP_TYPE = 'O') AS DEBITCOUNT ");
		sql.append("    ,(SELECT SUM(AMT+RVSAMT) FROM RPCLEARFEETAB_NW WHERE YYYYMM = A.YYYYMM AND BGBK_ID = A.BGBK_ID AND OP_TYPE = 'O') AS DEBITAMT ");
		sql.append("    ,(SELECT SUM(CNT+RVSCNT) FROM RPCLEARFEETAB_NW WHERE YYYYMM = A.YYYYMM AND BGBK_ID = A.BGBK_ID AND OP_TYPE = 'I') AS SAVECOUNT ");
		sql.append("    ,(SELECT SUM(AMT+RVSAMT) FROM RPCLEARFEETAB_NW WHERE YYYYMM = A.YYYYMM AND BGBK_ID = A.BGBK_ID AND OP_TYPE = 'I') AS SAVEAMT ");
		sql.append("    ,(SELECT SUM(CNT+RVSCNT) FROM RPCLEARFEETAB_NW WHERE YYYYMM = A.YYYYMM AND BGBK_ID = A.BGBK_ID AND OP_TYPE = 'W') AS CANCELCOUNT ");
		sql.append("    ,(SELECT SUM(AMT+RVSAMT) FROM RPCLEARFEETAB_NW WHERE YYYYMM = A.YYYYMM AND BGBK_ID = A.BGBK_ID AND OP_TYPE = 'W') AS CANCELAMT ");
		sql.append("    ,(SELECT FEE FROM TEMP WHERE VARCHAR(INT(YYYYMM) + 191100) = A.YYYYMM AND BGBK_ID = A.BGBK_ID) AS ADJ_FEE ");
		sql.append("    FROM RPCLEARFEETAB_NW AS A ");
		sql.append("	" + (StrUtils.isNotEmpty(where)?"WHERE " + where : ""));
		sql.append("    GROUP BY A.YYYYMM, A.BGBK_ID ");
		sql.append("    ORDER BY A.BGBK_ID ");
		sql.append(") B FULL OUTER JOIN TEMP AS C ON B.YYYYMM = VARCHAR(INT(C.YYYYMM) + 191100) AND B.BGBK_ID = C.BGBK_ID ");
		sql.append("ORDER BY BGBK_ID ");
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
