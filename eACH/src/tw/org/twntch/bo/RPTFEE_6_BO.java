package tw.org.twntch.bo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tw.org.twntch.db.dao.hibernate.BANK_BRANCH_Dao;
import tw.org.twntch.db.dao.hibernate.BANK_OPBK_Dao;
import tw.org.twntch.db.dao.hibernate.RPONBLOCKTAB_Dao;
import tw.org.twntch.form.Rptfee_6_Form;
import tw.org.twntch.po.BANK_BRANCH;
import tw.org.twntch.po.BANK_OPBK;
import tw.org.twntch.util.CodeUtils;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.RptUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.zDateHandler;

public class RPTFEE_6_BO {
	private RPONBLOCKTAB_Dao rponblocktab_Dao;
	private BANK_BRANCH_Dao bank_branch_Dao;
	private BANK_OPBK_Dao bank_opbk_Dao;
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
			if(StrUtils.isEmpty(opbkId)){
				params.put("V_OPT_BANK", "全部");
			}else{
				List<BANK_OPBK> bank_opbk = bank_opbk_Dao.getDataByBgbkId(opbkId, "");
				String opbk_name = "";
//				for (int i = 0; i < bank_opbk.size(); i++) {
//					opbk_name = bank_opbk.get(i).getOPBK_NAME();
//				}
				if((bank_opbk.size()-1)>0)opbk_name = bank_opbk.get(bank_opbk.size()-1).getOPBK_NAME();
				params.put("V_OPT_BANK", opbk_name);
			}
			
			
			BigDecimal adjFee = new BigDecimal("0");
			List adjFeeList = rponblocktab_Dao.getRptData(getAdjFeeSql(twYear, twMonth, opbkId, bgbkId, brbkId), new ArrayList());
			if(adjFeeList != null && adjFeeList.size() > 0){
				adjFee = (BigDecimal)((Map)adjFeeList.get(0)).get("FEE");
			}
			params.put("V_ADJ_FEE", adjFee);
			//System.out.println("params >> " + params);
			
			List datalist = rponblocktab_Dao.getRptData(getSQL(twYear, twMonth, opbkId, bgbkId, brbkId), new ArrayList());
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
			System.out.println("list >> "+ list);
			
			int pathType = Arguments.getStringArg("ISWINOPEN").equals("Y")?RptUtils.C_PATH :RptUtils.R_PATH;
			String outputFilePath = "";
			if(ext.equals("xls")){
				outputFilePath = RptUtils.export(RptUtils.COLLECTION,pathType, "ex_fee_6", "fee_6", params, list, 2);
			}else if(ext.equals("pdf")){
				outputFilePath = RptUtils.export(RptUtils.COLLECTION,pathType, "fee_6", "fee_6", params, list);
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
		
		List<String> conditions = new ArrayList<String>();
		if(StrUtils.isNotEmpty(twYear) && StrUtils.isNotEmpty(twMonth)){
			conditions.add(" YYYYMM = '" + DateTimeUtils.convertDate(DateTimeUtils.INTERCONVERSION, (twYear + twMonth), "yyyyMM", "yyyyMM") + "' ");
		}
		if(StrUtils.isNotEmpty(opbkId)){
			conditions.add(" OPBK_ID = '" + opbkId + "' ");
		}
		if(StrUtils.isNotEmpty(bgbkId)){
			conditions.add(" BGBK_ID = '" + bgbkId + "' ");
		}
		if(StrUtils.isNotEmpty(brbkId)){
			conditions.add(" BRBK_ID = '" + brbkId + "' ");
		}
		String where = combine(conditions);
		
		sql.append("WITH TEMP AS ( ");
		sql.append("	SELECT '1' AS ACCTCODE, BIZDATE, CLEARINGPHASE, BRBK_ID, BGBK_ID, TXN_ID, SENDERID, BUSINESS_TYPE_ID, OP_TYPE, BRBK_NAME, BGBK_NAME, OPBK_ID, OPBK_NAME, TXN_NAME, COMPANY_ABBR_NAME, BUSINESS_TYPE_NAME, ");
						
//		sql.append("	('0'||VARCHAR(CAST(YYYYMM AS INT) - 191100)) AS YYYYMM, CNT, AMT, 0 AS RVSCNT, 0 AS RVSAMT,GET_FEE_TYPE(replace(SENDERID, CHR(13) || CHR(10), ''),TXN_ID,BRBK_ID) AS FEE_TYPE   ");
		sql.append("	('0'||VARCHAR(CAST(YYYYMM AS INT) - 191100)) AS YYYYMM, CNT, AMT, 0 AS RVSCNT, 0 AS RVSAMT ,FEE_TYPE  ");
//		sql.append("	FROM RPCLEARFEETAB ");
		sql.append("	FROM RPCLEARFEETAB_NW ");
		sql.append("	WHERE COALESCE(CNT,0) <> 0 ");
		sql.append("	 " + (StrUtils.isEmpty(where)?"":"AND " + where));
		sql.append("	UNION ALL ");
		sql.append("	SELECT '0' AS ACCTCODE, BIZDATE, CLEARINGPHASE, BRBK_ID, BGBK_ID, TXN_ID, SENDERID, BUSINESS_TYPE_ID, OP_TYPE, BRBK_NAME, BGBK_NAME, OPBK_ID, OPBK_NAME, TXN_NAME, COMPANY_ABBR_NAME, BUSINESS_TYPE_NAME, ");
//		sql.append("	('0'||VARCHAR(CAST(YYYYMM AS INT) - 191100)) AS YYYYMM, 0 AS CNT, 0 AS AMT, RVSCNT, RVSAMT, GET_FEE_TYPE(replace(SENDERID, CHR(13) || CHR(10), ''),TXN_ID,BRBK_ID) AS FEE_TYPE  ");
		sql.append("	('0'||VARCHAR(CAST(YYYYMM AS INT) - 191100)) AS YYYYMM, 0 AS CNT, 0 AS AMT, RVSCNT, RVSAMT, FEE_TYPE ");
//		sql.append("	FROM RPCLEARFEETAB ");
		sql.append("	FROM RPCLEARFEETAB_NW ");
		sql.append("	WHERE COALESCE(RVSCNT,0) <> 0 ");
		sql.append("	 " + (StrUtils.isEmpty(where)?"":"AND " + where));
		sql.append(") ");

//		sql.append("SELECT FEE_TYPE,YYYYMM, ");
		sql.append("SELECT FEE_TYPE,YYYYMM, ");
		
		sql.append("(CASE A.ACCTCODE WHEN '0' THEN '沖正' ELSE '一般' END) AS ACCTCODE, ");
		sql.append("A.TXN_ID || COALESCE((SELECT '-' || TXN_NAME FROM TXN_CODE WHERE TXN_ID = A.TXN_ID),'') AS TXN_ID, ");
		sql.append("RTRIM(A.SENDERID) || COALESCE((SELECT '-' || COMPANY_ABBR_NAME FROM (SELECT COMPANY_ID, TXN_ID, SND_BRBK_ID, COMPANY_ABBR_NAME FROM SD_COMPANY_PROFILE UNION SELECT COMPANY_ID, TXN_ID, SND_BRBK_ID, COMPANY_ABBR_NAME FROM SC_COMPANY_PROFILE) WHERE COMPANY_ID = A.SENDERID AND (TXN_ID = A.TXN_ID OR TXN_ID = '930') FETCH FIRST 1 ROWS ONLY),'') AS SENDERID ");
		sql.append(",COALESCE((SELECT SUM((CASE A.ACCTCODE WHEN '1' THEN COALESCE(CNT,0) ELSE COALESCE(RVSCNT,0) END)) FROM TEMP WHERE YYYYMM = A.YYYYMM AND ACCTCODE = A.ACCTCODE AND TXN_ID = A.TXN_ID AND COALESCE(SENDERID,'') = A.SENDERID AND OP_TYPE = 'S' AND FEE_TYPE=A.FEE_TYPE),0) AS FIRECOUNT ");
		sql.append(",COALESCE((SELECT SUM((CASE A.ACCTCODE WHEN '1' THEN COALESCE(AMT,0) ELSE COALESCE(RVSAMT,0) END)) FROM TEMP WHERE YYYYMM = A.YYYYMM AND ACCTCODE = A.ACCTCODE AND TXN_ID = A.TXN_ID AND COALESCE(SENDERID,'') = A.SENDERID AND OP_TYPE = 'S' AND FEE_TYPE=A.FEE_TYPE),0) AS FIREAMT ");
		sql.append(",COALESCE((SELECT SUM((CASE A.ACCTCODE WHEN '1' THEN COALESCE(CNT,0) ELSE COALESCE(RVSCNT,0) END)) FROM TEMP WHERE YYYYMM = A.YYYYMM AND ACCTCODE = A.ACCTCODE AND TXN_ID = A.TXN_ID AND COALESCE(SENDERID,'') = A.SENDERID AND OP_TYPE = 'O' AND FEE_TYPE=A.FEE_TYPE),0) AS DEBITCOUNT ");
		sql.append(",COALESCE((SELECT SUM((CASE A.ACCTCODE WHEN '1' THEN COALESCE(AMT,0) ELSE COALESCE(RVSAMT,0) END)) FROM TEMP WHERE YYYYMM = A.YYYYMM AND ACCTCODE = A.ACCTCODE AND TXN_ID = A.TXN_ID AND COALESCE(SENDERID,'') = A.SENDERID AND OP_TYPE = 'O' AND FEE_TYPE=A.FEE_TYPE),0) AS DEBITAMT ");
		sql.append(",COALESCE((SELECT SUM((CASE A.ACCTCODE WHEN '1' THEN COALESCE(CNT,0) ELSE COALESCE(RVSCNT,0) END)) FROM TEMP WHERE YYYYMM = A.YYYYMM AND ACCTCODE = A.ACCTCODE AND TXN_ID = A.TXN_ID AND COALESCE(SENDERID,'') = A.SENDERID AND OP_TYPE = 'I' AND FEE_TYPE=A.FEE_TYPE),0) AS SAVECOUNT ");
		sql.append(",COALESCE((SELECT SUM((CASE A.ACCTCODE WHEN '1' THEN COALESCE(AMT,0) ELSE COALESCE(RVSAMT,0) END)) FROM TEMP WHERE YYYYMM = A.YYYYMM AND ACCTCODE = A.ACCTCODE AND TXN_ID = A.TXN_ID AND COALESCE(SENDERID,'') = A.SENDERID AND OP_TYPE = 'I' AND FEE_TYPE=A.FEE_TYPE),0) AS SAVEAMT ");
		sql.append(",COALESCE((SELECT SUM((CASE A.ACCTCODE WHEN '1' THEN COALESCE(CNT,0) ELSE COALESCE(RVSCNT,0) END)) FROM TEMP WHERE YYYYMM = A.YYYYMM AND ACCTCODE = A.ACCTCODE AND TXN_ID = A.TXN_ID AND COALESCE(SENDERID,'') = A.SENDERID AND OP_TYPE = 'W' AND FEE_TYPE=A.FEE_TYPE),0) AS CANCELCOUNT ");
		sql.append(",COALESCE((SELECT SUM((CASE A.ACCTCODE WHEN '1' THEN COALESCE(AMT,0) ELSE COALESCE(RVSAMT,0) END)) FROM TEMP WHERE YYYYMM = A.YYYYMM AND ACCTCODE = A.ACCTCODE AND TXN_ID = A.TXN_ID AND COALESCE(SENDERID,'') = A.SENDERID AND OP_TYPE = 'W' AND FEE_TYPE=A.FEE_TYPE),0) AS CANCELAMT ");
		sql.append("FROM ( ");
//		sql.append("    SELECT YYYYMM, ACCTCODE, TXN_ID, COALESCE(SENDERID,'') AS SENDERID,  FEE_TYPE FROM TEMP GROUP BY YYYYMM, ACCTCODE, TXN_ID, SENDERID ,FEE_TYPE  ");
		sql.append("    SELECT YYYYMM, ACCTCODE, TXN_ID, COALESCE(SENDERID,'') AS SENDERID , FEE_TYPE FROM TEMP GROUP BY YYYYMM, ACCTCODE, TXN_ID, SENDERID ,FEE_TYPE ");
		sql.append(") AS A ");
//		sql.append("ORDER BY A.ACCTCODE DESC, A.TXN_ID, A.SENDERID,FEE_TYPE ");
		sql.append("ORDER BY A.ACCTCODE DESC, A.TXN_ID, A.SENDERID , FEE_TYPE ");
		System.out.println("SQL >> " + sql.toString());
		return sql.toString();
	}
	
	public String getAdjFeeSql(String twYear, String twMonth, String opbkId, String bgbkId, String brbkId){
		StringBuilder sql = new StringBuilder();
		List<String> conditions = new ArrayList<String>();
		
		if(StrUtils.isNotEmpty(twYear) && StrUtils.isNotEmpty(twMonth)){
			conditions.add(" D.YYYYMM = '" + (twYear + twMonth) + "' ");
		}
		if(StrUtils.isNotEmpty(opbkId)){
			conditions.add(" BG.OPBK_ID = '" + opbkId + "' ");
		}
		if(StrUtils.isNotEmpty(bgbkId)){
			conditions.add(" D.BGBK_ID = '" + bgbkId + "' ");
		}
		if(StrUtils.isNotEmpty(brbkId)){
			conditions.add(" D.BRBK_ID = '" + brbkId + "' ");
		}
		String where = combine(conditions);
		
		sql.append("SELECT COALESCE(SUM(D.FEE),0) AS FEE ");
		sql.append("FROM ( ");
		sql.append("    SELECT BFA.YYYYMM, BFA.BRBK_ID, SUM(FEE) AS FEE, ");
		sql.append("    COALESCE(BR.BGBK_ID,(CASE BFA.BRBK_ID WHEN '0188888' THEN '0188888' ELSE '' END)) AS BGBK_ID ");
		sql.append("    FROM BRBK_FEE_ADJ AS BFA LEFT JOIN BANK_BRANCH AS BR ON BFA.BRBK_ID = BR.BRBK_ID ");
		sql.append("    WHERE COALESCE(BFA.ACTIVE_DATE,'') <> '' ");
		sql.append("    GROUP BY BFA.YYYYMM, BR.BGBK_ID, BFA.BRBK_ID ");
		sql.append(") AS D LEFT JOIN BANK_GROUP AS BG ON D.BGBK_ID = BG.BGBK_ID ");
		sql.append((StrUtils.isNotEmpty(where)?"WHERE " + where:""));
		System.out.println("ADJ_FEE SQL >> " + sql );
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

	public BANK_OPBK_Dao getBank_opbk_Dao() {
		return bank_opbk_Dao;
	}

	public void setBank_opbk_Dao(BANK_OPBK_Dao bank_opbk_Dao) {
		this.bank_opbk_Dao = bank_opbk_Dao;
	}
	
	
}
