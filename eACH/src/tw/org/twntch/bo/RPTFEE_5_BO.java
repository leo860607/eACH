package tw.org.twntch.bo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tw.org.twntch.db.dao.hibernate.BANK_BRANCH_Dao;
import tw.org.twntch.db.dao.hibernate.BANK_GROUP_Dao;
import tw.org.twntch.db.dao.hibernate.RPONBLOCKTAB_Dao;
import tw.org.twntch.po.BANK_BRANCH;
import tw.org.twntch.po.BANK_GROUP;
import tw.org.twntch.util.CodeUtils;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.RptUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.zDateHandler;

public class RPTFEE_5_BO {
	private RPONBLOCKTAB_Dao rponblocktab_Dao;
	private BANK_GROUP_Dao bank_group_Dao;
	
	public Map<String, String> ex_export(String ext, String bizDate, String clearingPhase, String opbkId, String bgbkId, String brbkId, String serchStrs){
		return export(ext, bizDate, clearingPhase, opbkId, bgbkId, brbkId, serchStrs);
	}
	public Map<String, String> csv_export(String ext, String bizDate, String clearingPhase, String opbkId, String bgbkId, String brbkId, String serchStrs){
		return export(ext, bizDate, clearingPhase, opbkId, bgbkId, brbkId, serchStrs);
	}	
	public Map<String, String> export(String ext, String bizDate, String clearingPhase, String opbkId, String bgbkId, String brbkId, String serchStrs){
		Map<String, String> rtnMap = null;
		
		try{
			rtnMap = new HashMap<String, String>();
			rtnMap.put("serchStrs", serchStrs);
			Map<String, Object> params = new HashMap<String, Object>();
			//「民國年」為必填
			params.put("V_TXDT", "營業日期：" + DateTimeUtils.convertDate(DateTimeUtils.NOT_INTERCONVERSION, bizDate, "yyyyMMdd", "yyy/MM/dd"));
			
			if( (StrUtils.isNotEmpty(opbkId) && opbkId.equalsIgnoreCase("all")) || 
				(StrUtils.isNotEmpty(bgbkId) && bgbkId.equalsIgnoreCase("all"))	){
				params.put("V_BANK_ID", "全部");
			}else{
				BANK_GROUP po = null;
				if(StrUtils.isNotEmpty(opbkId) && !opbkId.equalsIgnoreCase("all")){
					po = bank_group_Dao.get(opbkId);
				}else if(StrUtils.isNotEmpty(bgbkId) && !bgbkId.equalsIgnoreCase("all")){
					po = bank_group_Dao.get(bgbkId);
				}
				params.put("V_BANK_ID", po.getBGBK_NAME());
			}
			params.put("V_PRINT_DATE", DateTimeUtils.convertDate(DateTimeUtils.INTERCONVERSION, zDateHandler.getTheDate(), "yyyy/MM/dd", "yyy/MM/dd"));
			System.out.println("params >> " + params);
			
			//List list = rponblocktab_Dao.getFee_5_Detail_Data_ForRpt(conStr_1, conStr_2);
			List datalist = rponblocktab_Dao.getRptData(getSQL(bizDate, clearingPhase, opbkId, bgbkId, brbkId), new ArrayList());
			List list = new ArrayList();
			for(Object each:datalist) {
				Map eachMap = CodeUtils.objectCovert(Map.class, each);
				switch (null==(String)eachMap.get("FEE_TYPE")?"":(String)eachMap.get("FEE_TYPE")){
				case "":
					eachMap.put("FEE_TYPE", "");
					break;
				case "A":
					eachMap.put("FEE_TYPE", "固定");
					break;
				case "B":
					eachMap.put("FEE_TYPE", "外加");
					break;
				case "C":
					eachMap.put("FEE_TYPE", "百分比");
					break;
				case "D":
					eachMap.put("FEE_TYPE", "級距");
					break;
				}
				list.add(eachMap);
			}
			System.out.println("datalist >> "+ list);
			int pathType = Arguments.getStringArg("ISWINOPEN").equals("Y")?RptUtils.C_PATH :RptUtils.R_PATH;
			String outputFilePath = "";
			if(ext.equals("xls")){
				outputFilePath = RptUtils.export(RptUtils.COLLECTION, pathType, "ex_fee_5", "fee_5", params, list, 2);
			}else if(ext.equals("pdf")){
				outputFilePath = RptUtils.export(RptUtils.COLLECTION, pathType, "fee_5", "fee_5", params, list);
			}
			else if(ext.equals("csv")){
				outputFilePath = RptUtils.export(RptUtils.COLLECTION, pathType, "csv_fee_5", "fee_5", params, list,4);
			}
			System.out.println("rptsql>>"+getSQL(bizDate, clearingPhase, opbkId, bgbkId, brbkId));
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
	
	public String getSQL(String bizDate, String clearingPhase, String opbkId, String bgbkId, String brbkId){
		List<String> conditions_1 = new ArrayList<String>();
		List<String> conditions_2 = new ArrayList<String>();
		String conStr_1 = "", conStr_2 = "";
		
		if(StrUtils.isNotEmpty(bizDate)){
			conditions_1.add(" BIZDATE = '" + DateTimeUtils.convertDate(bizDate, "yyyyMMdd", "yyyyMMdd") + "' ");
			conditions_2.add(" BIZDATE = '" + DateTimeUtils.convertDate(bizDate, "yyyyMMdd", "yyyyMMdd") + "' ");
		}else{
			bizDate = zDateHandler.getTWDate();
		}
		bizDate = DateTimeUtils.convertDate(zDateHandler.getTWDate(), "yyyyMMdd", "yyyyMMdd");
		
		if(StrUtils.isNotEmpty(clearingPhase) && !clearingPhase.equalsIgnoreCase("all")){
			conditions_1.add(" CLEARINGPHASE = '" + clearingPhase + "' ");
			conditions_2.add(" CLEARINGPHASE = '" + clearingPhase + "' ");
		}
		if(StrUtils.isNotEmpty(opbkId) && !opbkId.equalsIgnoreCase("all")){
			/*
			conditions_1.add(" (SELECT OPBK_ID FROM BANK_GROUP WHERE BGBK_ID = X.BGBK_ID) = '" + opbkId + "' ");
			conditions_2.add(" (SELECT OPBK_ID FROM BANK_GROUP WHERE BGBK_ID = Y.BGBK_ID) = '" + opbkId + "' ");
			*/
			conditions_1.add(" OPBK_ID = '" + opbkId + "' ");
			conditions_2.add(" OPBK_ID = '" + opbkId + "' ");
		}
		if(StrUtils.isNotEmpty(bgbkId) && !bgbkId.equalsIgnoreCase("all")){
			conditions_1.add(" BGBK_ID = '" + bgbkId + "' ");
			conditions_2.add(" BGBK_ID = '" + bgbkId + "' ");
		}
		if(StrUtils.isNotEmpty(brbkId) && !brbkId.equalsIgnoreCase("all")){
			conditions_1.add(" BRBK_ID = '" + brbkId + "' ");
			conditions_2.add(" BRBK_ID = '" + brbkId + "' ");
		}
		
		conStr_1 = combine(conditions_1);
		conStr_2 = combine(conditions_2);
		
		StringBuffer withTempSql = new StringBuffer();
		withTempSql.append("WITH TEMP AS ( ");
//		20160307 edit by hugo  因應部分交易  SENDERID有可能是null 故調整
//		withTempSql.append("    SELECT '一般' AS ACCTCODE, OP_TYPE, BIZDATE, CLEARINGPHASE, BGBK_ID, BRBK_ID, TXN_ID, SENDERID, CNT, AMT, 0 AS RVSCNT, 0 AS RVSAMT ");
		withTempSql.append("    SELECT '一般' AS ACCTCODE, OP_TYPE, BIZDATE, CLEARINGPHASE, BGBK_ID, BRBK_ID, TXN_ID, COALESCE( SENDERID ,'') SENDERID, CNT, AMT, 0 AS RVSCNT, 0 AS RVSAMT , FEE_TYPE ");
		withTempSql.append("    FROM RPCLEARFEETAB_NW AS X ");
		withTempSql.append("    WHERE COALESCE(CNT,0) <> 0 ");
		withTempSql.append("     " + (StrUtils.isEmpty(conStr_1)? "" : "AND " + conStr_1));
		withTempSql.append("    UNION ALL ");
//		20160307 edit by hugo  因應部分交易  SENDERID有可能是null 故調整
//		withTempSql.append("    SELECT '沖正' AS ACCTCODE, OP_TYPE, BIZDATE, CLEARINGPHASE, BGBK_ID, BRBK_ID, TXN_ID, SENDERID, 0 AS CNT, 0 AS AMT, RVSCNT, RVSAMT ");
		withTempSql.append("    SELECT '沖正' AS ACCTCODE, OP_TYPE, BIZDATE, CLEARINGPHASE, BGBK_ID, BRBK_ID, TXN_ID, COALESCE( SENDERID ,'') SENDERID, 0 AS CNT, 0 AS AMT, RVSCNT, RVSAMT , FEE_TYPE ");
		withTempSql.append("    FROM RPCLEARFEETAB_NW AS Y ");
		withTempSql.append("    WHERE COALESCE(RVSCNT,0) <> 0 ");
		withTempSql.append("     " + (StrUtils.isEmpty(conStr_2)? "" : "AND " + conStr_2));
		withTempSql.append(") ");

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ");
		sql.append("A.FEE_TYPE AS FEE_TYPE, ");
	
		sql.append("COALESCE(VARCHAR(BGBK_ID),'') AS BGBK_ID, (SELECT BGBK_NAME FROM BANK_GROUP WHERE BGBK_ID = A.BGBK_ID) AS BGBK_NAME, ");
		sql.append("ACCTCODE, TXN_ID || '-' || (SELECT TXN_NAME FROM TXN_CODE WHERE TXN_ID = A.TXN_ID) AS TXN_ID, VARCHAR(SENDERID) AS SENDERID, ");
		sql.append("COALESCE((SELECT SUM(CASE ACCTCODE WHEN '一般' THEN COALESCE(CNT,0) ELSE COALESCE(RVSCNT,0) END) FROM TEMP WHERE BGBK_ID = A.BGBK_ID AND ACCTCODE = A.ACCTCODE AND TXN_ID = A.TXN_ID AND SENDERID = A.SENDERID AND OP_TYPE = 'S' AND FEE_TYPE=A.FEE_TYPE ),0) AS FIRECOUNT, ");
//		sql.append("COALESCE((SELECT (CASE ACCTCODE WHEN '一般' THEN SND_BANK_FEE_DISC ELSE -SND_BANK_FEE_DISC END) FROM FEE_CODE WHERE FEE_ID = A.TXN_ID AND START_DATE = (SELECT MAX(START_DATE) FROM FEE_CODE WHERE FEE_ID = A.TXN_ID AND START_DATE <= TRANS_DATE('" + bizDate + "','T','')) FETCH FIRST 1 ROWS ONLY),0) AS SND_BANK_FEE_DISC, ");
//		sql.append("COALESCE((SELECT (CASE ACCTCODE WHEN '一般' THEN SND_BANK_FEE_DISC ELSE -SND_BANK_FEE_DISC END) FROM VW_TXID_ACTIVED_FEENW_FEEOLD WHERE FEE_ID = A.TXN_ID  AND FEE_TYPE=A.FEE_TYPE  order by START_DATE desc FETCH FIRST 1 ROWS ONLY) ,0) AS SND_BANK_FEE_DISC ,");
		sql.append("COALESCE((SELECT SUM(CASE ACCTCODE WHEN '一般' THEN COALESCE(AMT,0) ELSE COALESCE(RVSAMT,0) END) FROM TEMP WHERE BGBK_ID = A.BGBK_ID AND ACCTCODE = A.ACCTCODE AND TXN_ID = A.TXN_ID AND SENDERID = A.SENDERID AND OP_TYPE = 'S' AND FEE_TYPE=A.FEE_TYPE ),0) AS FIREAMT, ");
		sql.append("COALESCE((SELECT SUM(CASE ACCTCODE WHEN '一般' THEN COALESCE(CNT,0) ELSE COALESCE(RVSCNT,0) END) FROM TEMP WHERE BGBK_ID = A.BGBK_ID AND ACCTCODE = A.ACCTCODE AND TXN_ID = A.TXN_ID AND SENDERID = A.SENDERID AND OP_TYPE = 'O' AND FEE_TYPE=A.FEE_TYPE ),0) AS DEBITCOUNT, ");
//		sql.append("COALESCE((SELECT (CASE ACCTCODE WHEN '一般' THEN OUT_BANK_FEE_DISC ELSE -OUT_BANK_FEE_DISC END) FROM FEE_CODE WHERE FEE_ID = A.TXN_ID AND START_DATE = (SELECT MAX(START_DATE) FROM FEE_CODE WHERE FEE_ID = A.TXN_ID AND START_DATE <= TRANS_DATE('" + bizDate + "','T','')) FETCH FIRST 1 ROWS ONLY),0) AS OUT_BANK_FEE_DISC, ");
//		sql.append("COALESCE((SELECT (CASE ACCTCODE WHEN '一般' THEN OUT_BANK_FEE_DISC ELSE -OUT_BANK_FEE_DISC END) FROM VW_TXID_ACTIVED_FEENW_FEEOLD WHERE FEE_ID = A.TXN_ID  AND FEE_TYPE=A.FEE_TYPE order by START_DATE desc FETCH FIRST 1 ROWS ONLY), 0) AS OUT_BANK_FEE_DISC, ");
		sql.append("COALESCE((SELECT SUM(CASE ACCTCODE WHEN '一般' THEN COALESCE(AMT,0) ELSE COALESCE(RVSAMT,0) END) FROM TEMP WHERE BGBK_ID = A.BGBK_ID AND ACCTCODE = A.ACCTCODE AND TXN_ID = A.TXN_ID AND SENDERID = A.SENDERID AND OP_TYPE = 'O' AND FEE_TYPE=A.FEE_TYPE ),0) AS DEBITAMT, ");
		sql.append("COALESCE((SELECT SUM(CASE ACCTCODE WHEN '一般' THEN COALESCE(CNT,0) ELSE COALESCE(RVSCNT,0) END) FROM TEMP WHERE BGBK_ID = A.BGBK_ID AND ACCTCODE = A.ACCTCODE AND TXN_ID = A.TXN_ID AND SENDERID = A.SENDERID AND OP_TYPE = 'I' AND FEE_TYPE=A.FEE_TYPE ),0) AS SAVECOUNT, ");
//		sql.append("COALESCE((SELECT (CASE ACCTCODE WHEN '一般' THEN IN_BANK_FEE_DISC ELSE -IN_BANK_FEE_DISC END) FROM FEE_CODE WHERE FEE_ID = A.TXN_ID AND START_DATE = (SELECT MAX(START_DATE) FROM FEE_CODE WHERE FEE_ID = A.TXN_ID AND START_DATE <= TRANS_DATE('" + bizDate + "','T','')) FETCH FIRST 1 ROWS ONLY),0) AS IN_BANK_FEE_DISC, ");
//		sql.append("COALESCE((SELECT (CASE ACCTCODE WHEN '一般' THEN IN_BANK_FEE_DISC ELSE -IN_BANK_FEE_DISC END) FROM VW_TXID_ACTIVED_FEENW_FEEOLD WHERE FEE_ID = A.TXN_ID  AND FEE_TYPE=A.FEE_TYPE order by START_DATE desc FETCH FIRST 1 ROWS ONLY), 0) AS IN_BANK_FEE_DISC, ");
		sql.append("COALESCE((SELECT SUM(CASE ACCTCODE WHEN '一般' THEN COALESCE(AMT,0) ELSE COALESCE(RVSAMT,0) END) FROM TEMP WHERE BGBK_ID = A.BGBK_ID AND ACCTCODE = A.ACCTCODE AND TXN_ID = A.TXN_ID AND SENDERID = A.SENDERID AND OP_TYPE = 'I' AND FEE_TYPE=A.FEE_TYPE ),0) AS SAVEAMT, ");
		sql.append("COALESCE((SELECT SUM(CASE ACCTCODE WHEN '一般' THEN COALESCE(CNT,0) ELSE COALESCE(RVSCNT,0) END) FROM TEMP WHERE BGBK_ID = A.BGBK_ID AND ACCTCODE = A.ACCTCODE AND TXN_ID = A.TXN_ID AND SENDERID = A.SENDERID AND OP_TYPE = 'W' AND FEE_TYPE=A.FEE_TYPE ),0) AS CANCELCOUNT, ");
//		sql.append("COALESCE((SELECT (CASE ACCTCODE WHEN '一般' THEN WO_BANK_FEE_DISC ELSE -WO_BANK_FEE_DISC END) FROM FEE_CODE WHERE FEE_ID = A.TXN_ID AND START_DATE = (SELECT MAX(START_DATE) FROM FEE_CODE WHERE FEE_ID = A.TXN_ID AND START_DATE <= TRANS_DATE('" + bizDate + "','T','')) FETCH FIRST 1 ROWS ONLY),0) AS WO_BANK_FEE_DISC, ");
//		sql.append("COALESCE((SELECT (CASE ACCTCODE WHEN '一般' THEN WO_BANK_FEE_DISC ELSE -WO_BANK_FEE_DISC END) FROM VW_TXID_ACTIVED_FEENW_FEEOLD WHERE FEE_ID = A.TXN_ID  AND FEE_TYPE=A.FEE_TYPE order by START_DATE desc FETCH FIRST 1 ROWS ONLY), 0) AS WO_BANK_FEE_DISC, ");
		sql.append("COALESCE((SELECT SUM(CASE ACCTCODE WHEN '一般' THEN COALESCE(AMT,0) ELSE COALESCE(RVSAMT,0) END) FROM TEMP WHERE BGBK_ID = A.BGBK_ID AND ACCTCODE = A.ACCTCODE AND TXN_ID = A.TXN_ID AND SENDERID = A.SENDERID AND OP_TYPE = 'W' AND FEE_TYPE=A.FEE_TYPE),0) AS CANCELAMT ");
		sql.append("FROM ( ");
//		sql.append("    SELECT BGBK_ID, ACCTCODE, TXN_ID, SENDERID, FEE_TYPE  FROM TEMP GROUP BY BGBK_ID, ACCTCODE, TXN_ID, SENDERID, FEE_TYPE ");
		sql.append("    SELECT BGBK_ID, ACCTCODE, TXN_ID, SENDERID , FEE_TYPE FROM TEMP GROUP BY BGBK_ID, ACCTCODE, TXN_ID, SENDERID , FEE_TYPE ");
		sql.append(") AS A ");
		sql.append("ORDER BY BGBK_ID, ACCTCODE, TXN_ID ");
		System.out.println("RPTFEE_5 SQL >> " +withTempSql.toString()+ sql.toString());
		return withTempSql.toString() + sql.toString();
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

	public BANK_GROUP_Dao getBank_group_Dao() {
		return bank_group_Dao;
	}

	public void setBank_group_Dao(BANK_GROUP_Dao bank_group_Dao) {
		this.bank_group_Dao = bank_group_Dao;
	}

}
