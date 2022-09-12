package tw.org.twntch.bo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.struts.util.LabelValueBean;

import tw.org.twntch.db.dao.hibernate.BANK_GROUP_Dao;
import tw.org.twntch.db.dao.hibernate.BUSINESS_TYPE_Dao;
import tw.org.twntch.db.dao.hibernate.EACH_TXN_CODE_Dao;
import tw.org.twntch.db.dao.hibernate.RPONBLOCKTAB_Dao;
import tw.org.twntch.db.dao.hibernate.SYS_PARA_Dao;
import tw.org.twntch.po.BANK_GROUP;
import tw.org.twntch.po.BANK_OPBK;
import tw.org.twntch.po.BUSINESS_TYPE;
import tw.org.twntch.po.EACH_TXN_CODE;
import tw.org.twntch.po.SYS_PARA;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.RptUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.zDateHandler;

public class RPTST_10_BO {
	private BUSINESS_TYPE_Dao business_type_Dao;
	private RPONBLOCKTAB_Dao rponblocktab_Dao;
	
	public Map<String, String> ex_export(String ext, String twYear, String twMonth, String bstype, String serchStrs){
		return export(ext, twYear, twMonth, bstype, serchStrs);
	}

	public Map<String, String> export(String ext, String twYear, String twMonth, String bstype, String serchStrs){
		Map<String, String> rtnMap = null;
		
		String twTempDate = twYear + twMonth + "01";
		String westTempDate = StrUtils.isEmpty(twTempDate)?"":DateTimeUtils.convertDate(twTempDate, "yyyyMMdd", "yyyyMMdd");
		String westYYYYMM = westTempDate.substring(0, 6);
		bstype = StrUtils.isEmpty(bstype)?"":bstype.equalsIgnoreCase("all")?"":bstype;
		
		try{
			rtnMap = new HashMap<String, String>();
			rtnMap.put("serchStrs", serchStrs);
			String withTempSQL = getSQL(westYYYYMM, bstype);
			System.out.println("WITH TEMP SQL >> " + withTempSQL);
			
			Map<String, Object> params = new HashMap<String, Object>();
			//「營業月份區間」為必填
			params.put("V_TXDT", Integer.valueOf(twYear) + "/" + twMonth);
			params.put("V_BSTYPE", StrUtils.isEmpty(bstype)?"全部":bstype + "-" + business_type_Dao.get(bstype).getBUSINESS_TYPE_NAME());
			params.put("V_PRINT_DATE", DateTimeUtils.convertDate(DateTimeUtils.INTERCONVERSION, zDateHandler.getTheDate(), "yyyy/MM/dd", "yyy/MM/dd"));
			
			StringBuffer query = new StringBuffer();
			query.append(withTempSQL + " ");
			query.append("SELECT * FROM TEMP___ ");
			query.append("ORDER BY PRCTIME ASC, BANKID ASC ");
			
			//List list = rponblocktab_Dao.getSt_1_Detail_Data_ForRpt(queryParam.get("sqlPath").toString(), (List<String>) queryParam.get("values"));
			List list = rponblocktab_Dao.getRptData(query.toString(), new ArrayList<String>());
			int pathType = Arguments.getStringArg("ISWINOPEN").equals("Y")?RptUtils.C_PATH :RptUtils.R_PATH;
			String outputFilePath = "";
			if(ext.equals("xls")){
				outputFilePath = RptUtils.export(RptUtils.COLLECTION,pathType, "ex_st_10", "st_10", params, list, 2);
			}else if(ext.equals("pdf")){
				outputFilePath = RptUtils.export(RptUtils.COLLECTION,pathType, "st_10", "st_10", params, list);
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
	
	public List<String> getConditionList(String westYYYYMM, String bstype){
		List<String> conditions = new ArrayList<String>();
		
		List<String> conditions_1 = new ArrayList<String>();
		
		if(StrUtils.isNotEmpty(westYYYYMM)){//營業日
			conditions_1.add(" A.BIZDATE LIKE '"+westYYYYMM+"%' ");
		}
		if(StrUtils.isNotEmpty(bstype)){//業務類別
			conditions_1.add(" A.PCODE LIKE '" + bstype.substring(0,2) + "%' ");
		}
		
		conditions.add( combine(conditions_1) );
		
		return conditions;
	}
	
	public String getSQL(String westYYYYMM, String bstype){
		List<String> conditions = getConditionList(westYYYYMM, bstype);
		String condition_1 = conditions.get(0);
		
		StringBuffer withTemp = new StringBuffer();
		withTemp.append("WITH TEMP AS ( ");
		withTemp.append("    SELECT ");
		withTemp.append("    (CASE RC2 WHEN '0601' THEN 30 ELSE DOUBLE(DATE_DIFF(ENDTIME, DT_REQ_1)) / CAST (1000000 AS BIGINT) END) AS PRCTIME, ");
		withTemp.append("    C.* ");
		withTemp.append("    FROM ( ");
		withTemp.append("        SELECT ");
		withTemp.append("        (CASE WHEN RC2 = '3001' THEN 1 ELSE 0 END) OKCOUNT, ");
		withTemp.append("        (CASE WHEN RC2 NOT IN ('3001','0601') THEN 1 ELSE 0 END) FAILCOUNT, ");
		withTemp.append("        (CASE WHEN RC2 = '0601' THEN 1 ELSE 0 END) PENDCOUNT, A.RESULTSTATUS, ");
		withTemp.append("        A.PCODE, SUBSTR(COALESCE(A.PCODE,'0000'),4,1) AS TXN_TYPE, A.RC2, A.SENDERSTATUS, ");
		withTemp.append("        COALESCE(A.DT_RSP_2, COALESCE(A.DT_RSP_1, COALESCE(A.DT_REQ_2, COALESCE(A.DT_REQ_1, 0)))) AS ENDTIME, ");
		withTemp.append("        (CASE WHEN LENGTH(COALESCE(A.DT_REQ_1,''))=0 THEN '0' ELSE A.DT_REQ_1 END ) DT_REQ_1, ");
		withTemp.append("        (CASE WHEN LENGTH(COALESCE(A.DT_REQ_2,''))=0 THEN '0' ELSE A.DT_REQ_2 END ) DT_REQ_2, ");
		withTemp.append("        (CASE WHEN LENGTH(COALESCE(A.DT_REQ_3,''))=0 THEN '0' ELSE A.DT_REQ_3 END ) DT_REQ_3, ");
		withTemp.append("        (CASE WHEN LENGTH(COALESCE(A.DT_RSP_1,''))=0 THEN '0' ELSE A.DT_RSP_1 END ) DT_RSP_1, ");
		withTemp.append("        (CASE WHEN LENGTH(COALESCE(A.DT_RSP_2,''))=0 THEN '0' ELSE A.DT_RSP_2 END ) DT_RSP_2, ");
		withTemp.append("        (CASE WHEN LENGTH(COALESCE(A.DT_RSP_3,''))=0 THEN '0' ELSE A.DT_RSP_3 END ) DT_RSP_3, ");
		withTemp.append("        A.CLEARINGPHASE, A.SENDERACQUIRE, A.OUTACQUIRE, A.INACQUIRE, A.SENDERHEAD, A.OUTHEAD, A.INHEAD ");
		withTemp.append("        FROM ONBLOCKTAB AS A ");
		withTemp.append("        WHERE ( A.RESULTSTATUS IN ('A', 'R') OR (A.RESULTSTATUS = 'P' AND A.SENDERSTATUS <> '1') ) AND COALESCE(DT_REQ_2,'') <> '' ");
		withTemp.append("        " + (StrUtils.isEmpty(condition_1)? "" : "AND " + condition_1));
		withTemp.append("    ) AS C ");
		withTemp.append("), TEMP_ AS ( ");
		withTemp.append("    SELECT SENDERACQUIRE AS BANKID FROM TEMP GROUP BY SENDERACQUIRE ");
		withTemp.append("    UNION ");
		withTemp.append("    SELECT OUTACQUIRE FROM TEMP GROUP BY OUTACQUIRE ");
		withTemp.append("    UNION ");
		withTemp.append("    SELECT INACQUIRE FROM TEMP GROUP BY INACQUIRE ");
		withTemp.append("), TEMP__ AS ( ");
		withTemp.append("    SELECT ");
		withTemp.append("    B.BANKID, COALESCE(EACHUSER.GETBKNAME(B.BANKID), '') AS BANKIDANDNAME, ");
		withTemp.append("    (SELECT SUM(OKCOUNT + FAILCOUNT + PENDCOUNT) FROM TEMP AS A WHERE (A.SENDERACQUIRE = B.BANKID OR A.OUTACQUIRE = B.BANKID OR A.INACQUIRE = B.BANKID)) AS TOTALCOUNT ");
		withTemp.append("    ,DECIMAL(COALESCE((SELECT SUM(PRCTIME) FROM TEMP AS A WHERE (A.SENDERACQUIRE = B.BANKID OR A.OUTACQUIRE = B.BANKID OR A.INACQUIRE = B.BANKID)),0),18,2) PRCTIME ");
		withTemp.append("    FROM TEMP_ AS B ");
		withTemp.append("), TEMP___ AS ( ");
		withTemp.append("    SELECT BANKID, BANKIDANDNAME, TOTALCOUNT, ROUND((PRCTIME / TOTALCOUNT), 2) AS PRCTIME ");
		withTemp.append("    FROM TEMP__ ");
		withTemp.append(") ");
		
		//System.out.println("### SQL >> " + withTemp.toString());
		return withTemp.toString();
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
	
	public BUSINESS_TYPE_Dao getBusiness_type_Dao() {
		return business_type_Dao;
	}

	public void setBusiness_type_Dao(BUSINESS_TYPE_Dao business_type_Dao) {
		this.business_type_Dao = business_type_Dao;
	}
	
	public RPONBLOCKTAB_Dao getRponblocktab_Dao() {
		return rponblocktab_Dao;
	}

	public void setRponblocktab_Dao(RPONBLOCKTAB_Dao rponblocktab_Dao) {
		this.rponblocktab_Dao = rponblocktab_Dao;
	}
	
}
