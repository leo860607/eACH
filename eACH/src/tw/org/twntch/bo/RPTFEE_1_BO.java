package tw.org.twntch.bo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tw.org.twntch.db.dao.hibernate.BANK_BRANCH_Dao;
import tw.org.twntch.db.dao.hibernate.RPONBLOCKTAB_Dao;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.RptUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.zDateHandler;

public class RPTFEE_1_BO {
	private RPONBLOCKTAB_Dao rponblocktab_Dao;
	private BANK_BRANCH_Dao bank_branch_Dao;
	
	public Map<String, String> ex_export(String ext, String startDate, String endDate, String clearingPhase, String opbkId, String bgbkId, String brbkId, String serchStrs){
		return export(ext, startDate, endDate, clearingPhase, opbkId, bgbkId, brbkId, serchStrs);
	}
	
	public Map<String, String> export(String ext, String startDate, String endDate, String clearingPhase, String opbkId, String bgbkId, String brbkId, String serchStrs){
		Map<String, String> rtnMap = null;
		
		try{
			rtnMap = new HashMap<String, String>();
			rtnMap.put("serchStrs", serchStrs);
			Map<String, Object> params = new HashMap<String, Object>();
			//「營業日區間」為必填
			params.put("V_TXDT",
					"營業日期：" + 
					DateTimeUtils.convertDate(DateTimeUtils.convertDate(startDate,"yyyyMMdd","yyyyMMdd"),"yyyyMMdd","yyy/MM/dd") + " ~ " + 
					DateTimeUtils.convertDate(DateTimeUtils.convertDate(endDate,"yyyyMMdd","yyyyMMdd"),"yyyyMMdd","yyy/MM/dd")
			);
			//清算階段
			params.put("V_CLEARINGPHASE", clearingPhase);
			params.put("V_PRINT_DATE", DateTimeUtils.convertDate(DateTimeUtils.INTERCONVERSION, zDateHandler.getTheDate(), "yyyy/MM/dd", "yyy/MM/dd"));
			//System.out.println("params >> " + params);
			//List list = rponblocktab_Dao.getFee_1_Detail_Data_ForRpt(conStr_1, conStr_2);
			List list = rponblocktab_Dao.getRptData(getSQL(startDate, endDate, clearingPhase, opbkId, bgbkId, brbkId), new ArrayList());
			//System.out.println("datalist >> "+ list);
			
			int pathType = Arguments.getStringArg("ISWINOPEN").equals("Y")?RptUtils.C_PATH :RptUtils.R_PATH;
			String outputFilePath = "";
			if(ext.equals("xls")){
				outputFilePath = RptUtils.export(RptUtils.COLLECTION, pathType, "ex_fee_1", "fee_1", params, list, 2);
			}else if(ext.equals("pdf")){
				outputFilePath = RptUtils.export(RptUtils.COLLECTION, pathType, "fee_1", "fee_1", params, list);
			}
			//String outputFilePath = "";
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
	
	public String getSQL(String startDate, String endDate, String clearingPhase, String opbkId, String bgbkId, String brbkId){
		List<String> conditions = new ArrayList<String>();
		List<String> conditions_2 = new ArrayList<String>();
		List<String> conditions_3 = new ArrayList<String>();
		String conStr = "", conStr_2 = "", conStr_3 = "";
		if(StrUtils.isNotEmpty(startDate)){
			conditions.add(" BIZDATE >= '" + DateTimeUtils.convertDate(startDate, "yyyyMMdd", "yyyyMMdd") + "' ");
			conditions_2.add(" BIZDATE >= '" + DateTimeUtils.convertDate(startDate, "yyyyMMdd", "yyyyMMdd") + "' ");
		}
		if(StrUtils.isNotEmpty(endDate)){
			conditions.add(" BIZDATE <= '" + DateTimeUtils.convertDate(endDate, "yyyyMMdd", "yyyyMMdd") + "' ");
			conditions_2.add(" BIZDATE <= '" + DateTimeUtils.convertDate(endDate, "yyyyMMdd", "yyyyMMdd") + "' ");
		}
		if(StrUtils.isNotEmpty(clearingPhase) && !clearingPhase.equalsIgnoreCase("all")){
			conditions.add(" CLEARINGPHASE = '" + clearingPhase + "' ");
			conditions_2.add(" CLEARINGPHASE = '" + clearingPhase + "' ");
		}
		if(StrUtils.isNotEmpty(opbkId)){
			//conditions.add(" (SELECT OPBK_ID FROM BANK_GROUP WHERE BGBK_ID = X.BGBK_ID) = '" + opbkId + "' ");
			conditions.add(" OPBK_ID = '" + opbkId + "' ");
			//conditions_2.add(" (SELECT OPBK_ID FROM BANK_GROUP WHERE BGBK_ID = Y.BGBK_ID) = '" + opbkId + "' ");
			conditions_2.add(" OPBK_ID = '" + opbkId + "' ");
		}
		if(StrUtils.isNotEmpty(bgbkId)){
			conditions.add(" BGBK_ID = '" + bgbkId + "' ");
			conditions_2.add(" BGBK_ID = '" + bgbkId + "' ");
			conditions_3.add(" A.BGBK_ID = '" + bgbkId + "' ");
		}
		if(StrUtils.isNotEmpty(brbkId)){
			conditions.add(" BRBK_ID = '" + brbkId + "' ");
			conditions_2.add(" BRBK_ID = '" + brbkId + "' ");
		}
		conStr = combine(conditions);
		conStr_2 = combine(conditions_2);
		conStr_3 = combine(conditions_3);
		
		StringBuffer sql = new StringBuffer();
		sql.append("WITH TEMP AS ( ");
		sql.append("    SELECT BIZDATE, CLEARINGPHASE, BGBK_ID ,'一般' AS ACCTCODE, OP_TYPE, CNT, AMT, RVSCNT, RVSAMT ");
		sql.append("    FROM EACHUSER.RPCLEARFEETAB_NW AS X ");
		sql.append("    " + (StrUtils.isNotEmpty(conStr)?"WHERE " + conStr : ""));
		sql.append("    UNION ALL ");
		sql.append("    SELECT BIZDATE, CLEARINGPHASE, BGBK_ID ,'沖正' AS ACCTCODE, OP_TYPE, CNT, AMT, RVSCNT, RVSAMT ");
		sql.append("    FROM EACHUSER.RPCLEARFEETAB_NW AS Y ");
		sql.append("    WHERE COALESCE(RVSCNT,0) <> 0 ");
		sql.append("    " + (StrUtils.isNotEmpty(conStr_2)?"AND " + conStr_2 : ""));
		sql.append(") ");

		sql.append("SELECT ");
		sql.append("BIZDATE, VARCHAR(A.BGBK_ID) AS BGBK_ID, BGBK_ID || '-' || (SELECT BGBK_NAME FROM BANK_GROUP BG WHERE A.BGBK_ID = BG.BGBK_ID) AS BANKID, ");
		sql.append("VARCHAR(CLEARINGPHASE) AS CLEARINGPHASE, ACCTCODE, ");
		sql.append("COALESCE((SELECT SUM(CASE ACCTCODE WHEN '一般' THEN COALESCE(CNT,0) ELSE COALESCE(RVSCNT,0) END) FROM TEMP WHERE BIZDATE = A.BIZDATE AND BGBK_ID = A.BGBK_ID AND CLEARINGPHASE = A.CLEARINGPHASE AND ACCTCODE = A.ACCTCODE AND OP_TYPE = 'S'),0) AS FIRECOUNT, ");
		sql.append("COALESCE((SELECT SUM(CASE ACCTCODE WHEN '一般' THEN COALESCE(AMT,0) ELSE COALESCE(RVSAMT,0) END) FROM TEMP WHERE BIZDATE = A.BIZDATE AND BGBK_ID = A.BGBK_ID AND CLEARINGPHASE = A.CLEARINGPHASE AND ACCTCODE = A.ACCTCODE AND OP_TYPE = 'S'),0) AS FIREAMT, ");
		sql.append("COALESCE((SELECT SUM(CASE ACCTCODE WHEN '一般' THEN COALESCE(CNT,0) ELSE COALESCE(RVSCNT,0) END) FROM TEMP WHERE BIZDATE = A.BIZDATE AND BGBK_ID = A.BGBK_ID AND CLEARINGPHASE = A.CLEARINGPHASE AND ACCTCODE = A.ACCTCODE AND OP_TYPE = 'O'),0) AS DEBITCOUNT, ");
		sql.append("COALESCE((SELECT SUM(CASE ACCTCODE WHEN '一般' THEN COALESCE(AMT,0) ELSE COALESCE(RVSAMT,0) END) FROM TEMP WHERE BIZDATE = A.BIZDATE AND BGBK_ID = A.BGBK_ID AND CLEARINGPHASE = A.CLEARINGPHASE AND ACCTCODE = A.ACCTCODE AND OP_TYPE = 'O'),0) AS DEBITAMT, ");
		sql.append("COALESCE((SELECT SUM(CASE ACCTCODE WHEN '一般' THEN COALESCE(CNT,0) ELSE COALESCE(RVSCNT,0) END) FROM TEMP WHERE BIZDATE = A.BIZDATE AND BGBK_ID = A.BGBK_ID AND CLEARINGPHASE = A.CLEARINGPHASE AND ACCTCODE = A.ACCTCODE AND OP_TYPE = 'I'),0) AS SAVECOUNT, ");
		sql.append("COALESCE((SELECT SUM(CASE ACCTCODE WHEN '一般' THEN COALESCE(AMT,0) ELSE COALESCE(RVSAMT,0) END) FROM TEMP WHERE BIZDATE = A.BIZDATE AND BGBK_ID = A.BGBK_ID AND CLEARINGPHASE = A.CLEARINGPHASE AND ACCTCODE = A.ACCTCODE AND OP_TYPE = 'I'),0) AS SAVEAMT, ");
		sql.append("COALESCE((SELECT SUM(CASE ACCTCODE WHEN '一般' THEN COALESCE(CNT,0) ELSE COALESCE(RVSCNT,0) END) FROM TEMP WHERE BIZDATE = A.BIZDATE AND BGBK_ID = A.BGBK_ID AND CLEARINGPHASE = A.CLEARINGPHASE AND ACCTCODE = A.ACCTCODE AND OP_TYPE = 'W'),0) AS CANCELCOUNT, ");
		sql.append("COALESCE((SELECT SUM(CASE ACCTCODE WHEN '一般' THEN COALESCE(AMT,0) ELSE COALESCE(RVSAMT,0) END) FROM TEMP WHERE BIZDATE = A.BIZDATE AND BGBK_ID = A.BGBK_ID AND CLEARINGPHASE = A.CLEARINGPHASE AND ACCTCODE = A.ACCTCODE AND OP_TYPE = 'W'),0) AS CANCELAMT ");
		sql.append("FROM ( ");
		sql.append("    SELECT BIZDATE, BGBK_ID, CLEARINGPHASE, ACCTCODE ");
		sql.append("    FROM TEMP "); 
		sql.append("    GROUP BY BIZDATE, BGBK_ID, CLEARINGPHASE, ACCTCODE ");
		sql.append(") AS A ");
		sql.append((StrUtils.isNotEmpty(conStr_3)?"WHERE " + conStr_3 : ""));
		sql.append("ORDER BY A.BGBK_ID, A.ACCTCODE, A.BIZDATE, A.CLEARINGPHASE ");
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
