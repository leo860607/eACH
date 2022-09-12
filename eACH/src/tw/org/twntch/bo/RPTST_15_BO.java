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

public class RPTST_15_BO {
	private RPONBLOCKTAB_Dao rponblocktab_Dao;
	private BANK_GROUP_Dao bank_group_Dao;
	private EACH_TXN_CODE_Dao each_txn_code_Dao;
	private BANK_OPBK_BO bank_opbk_bo;
	private BUSINESS_TYPE_Dao business_type_Dao;
	private SYS_PARA_Dao sys_para_Dao;
	
	public List<LabelValueBean> getOpbkIdList(){
//		List<BANK_GROUP> list = bank_group_Dao.getBgbkIdList_2();
//		List<LabelValueBean> beanList = new LinkedList<LabelValueBean>();
//		LabelValueBean bean = null;
//		for(BANK_GROUP po : list){
//			bean = new LabelValueBean(po.getBGBK_ID() + " - " + po.getBGBK_NAME(), po.getBGBK_ID());
//			beanList.add(bean);
//		}
//		System.out.println("beanList>>"+beanList);
//		return beanList;
		List<BANK_OPBK> list = bank_opbk_bo.getAllOpbkList();
		List<LabelValueBean> beanList = new LinkedList<LabelValueBean>();
		LabelValueBean bean = null;
		for(BANK_OPBK po : list){
			bean = new LabelValueBean(po.getOPBK_ID() + " - " + po.getOPBK_NAME(), po.getOPBK_ID());
			beanList.add(bean);
		}
		return beanList;
	}
	
	public Map<String, String> ex_export(String ext, String txdate, String bstype, String opbkId, String serchStrs){
		return export(ext, txdate, bstype, opbkId, serchStrs);
	}

	public Map<String, String> export(String ext, String txdate, String bstype, String opbkId, String serchStrs){
		Map<String, String> rtnMap = null;
		
		String westDate = StrUtils.isEmpty(txdate)?"":DateTimeUtils.convertDate(txdate, "yyyyMMdd", "yyyyMMdd");
		bstype = StrUtils.isEmpty(bstype)?"":bstype.equalsIgnoreCase("all")?"":bstype;
		opbkId = StrUtils.isEmpty(opbkId)?"":opbkId.equalsIgnoreCase("all")?"":opbkId;
		
		try{
			rtnMap = new HashMap<String, String>();
			rtnMap.put("serchStrs", serchStrs);
			String withTempSQL = getEvdayTimeTolSQL(westDate, bstype, opbkId);
			//System.out.println("WITH TEMP SQL >> " + withTempSQL);
			
			Map<String, Object> params = new HashMap<String, Object>();
			//「營業月份區間」為必填
			params.put("V_TXDT", DateTimeUtils.convertDate(DateTimeUtils.NOT_INTERCONVERSION, txdate, "yyyyMMdd", "yyy/MM/dd"));
			params.put("V_BSTYPE", StrUtils.isEmpty(bstype)?"全部":bstype + "-" + business_type_Dao.get(bstype).getBUSINESS_TYPE_NAME());
			params.put("V_OPT_BANK", StrUtils.isEmpty(opbkId)?"全部":opbkId);
			params.put("V_PRINT_DATE", DateTimeUtils.convertDate(DateTimeUtils.INTERCONVERSION, zDateHandler.getTheDate(), "yyyy/MM/dd", "yyy/MM/dd"));
			
			SYS_PARA sp = sys_para_Dao.getTopOne().get(0);
			params.put("V_TXN_STD_PROC_TIME", sp.getTXN_STD_PROC_TIME());
			params.put("V_BANK_SC_STD_PROC_TIME", sp.getBANK_SC_STD_PROC_TIME());
			params.put("V_BANK_SD_STD_PROC_TIME", sp.getBANK_SD_STD_PROC_TIME());
			params.put("V_TCH_STD_ECHO_TIME", sp.getTCH_STD_ECHO_TIME());
			
			//總計不在report裡做，先算好在丟進去
			StringBuffer dataSumSQL = new StringBuffer();
			dataSumSQL.append(withTempSQL);
			dataSumSQL.append("SELECT ");
			dataSumSQL.append("TOTALCOUNT, PENDCOUNT, ");
			dataSumSQL.append("PRCCOUNT, (CASE WHEN PRCCOUNT > 0 THEN DECIMAL((PRCTIME / PRCCOUNT), 18, 2) ELSE 0 END) AS PRCTIME, ");
			dataSumSQL.append("SAVECOUNT, (CASE WHEN SAVECOUNT > 0 THEN DECIMAL((SAVETIME / SAVECOUNT), 18, 2) ELSE 0 END) AS SAVETIME, ");
			dataSumSQL.append("DEBITCOUNT, (CASE WHEN DEBITCOUNT > 0 THEN DECIMAL((DEBITTIME / DEBITCOUNT), 18, 2) ELSE 0 END) AS DEBITTIME, ");
			dataSumSQL.append("ACHPRCCOUNT, (CASE WHEN ACHPRCCOUNT > 0 THEN DECIMAL((ACHPRCTIME / ACHPRCCOUNT), 18, 2) ELSE 0 END) AS ACHPRCTIME ");
			dataSumSQL.append("FROM TEMP____ ");
			List list = rponblocktab_Dao.getRptData(dataSumSQL.toString(), new ArrayList<String>());
			Map sumData = list==null?new HashMap():(Map)list.get(0);
			params.put("SUM_TOTALCOUNT", sumData.get("TOTALCOUNT"));
			params.put("SUM_PENDCOUNT", sumData.get("PENDCOUNT"));
			params.put("SUM_PRCCOUNT", sumData.get("PRCCOUNT"));
			params.put("SUM_PRCTIME", sumData.get("PRCTIME"));
			params.put("SUM_SAVECOUNT", sumData.get("SAVECOUNT"));
			params.put("SUM_SAVETIME", sumData.get("SAVETIME"));
			params.put("SUM_DEBITCOUNT", sumData.get("DEBITCOUNT"));
			params.put("SUM_DEBITTIME", sumData.get("DEBITTIME"));
			params.put("SUM_ACHPRCCOUNT", sumData.get("ACHPRCCOUNT"));
			params.put("SUM_ACHPRCTIME", sumData.get("ACHPRCTIME"));
			//System.out.println("params >> " + params);
			
			//Map queryParam = this.getConditionData(txdate, bstype, opbkId);
			//System.out.println("queryParam >> " + queryParam);
			
			StringBuffer query = new StringBuffer();
			query.append(withTempSQL + " ");
			query.append("SELECT RSS.* FROM ( ");
			query.append(" 	SELECT ROWNUMBER() OVER (ORDER BY BANKID) AS ROWNUMBER, "); 
			query.append("    BANKID, BANKIDANDNAME, TOTALCOUNT, PENDCOUNT, ");
			query.append("    PRCCOUNT, (CASE WHEN PRCCOUNT > 0 THEN DECIMAL((PRCTIME / PRCCOUNT), 18, 2) ELSE 0 END) AS PRCTIME, ");
			query.append("    SAVECOUNT, (CASE WHEN SAVECOUNT > 0 THEN DECIMAL((SAVETIME / SAVECOUNT), 18, 2) ELSE 0 END) AS SAVETIME, ");
			query.append("    DEBITCOUNT, (CASE WHEN DEBITCOUNT > 0 THEN DECIMAL((DEBITTIME / DEBITCOUNT), 18, 2) ELSE 0 END) AS DEBITTIME, ");
			query.append("    ACHPRCCOUNT, (CASE WHEN ACHPRCCOUNT > 0 THEN DECIMAL((ACHPRCTIME / ACHPRCCOUNT), 18, 2) ELSE 0 END) AS ACHPRCTIME ");
			query.append("    FROM TEMP___ AS RS "); 
			query.append(") AS RSS ");
			
			//List list = rponblocktab_Dao.getSt_1_Detail_Data_ForRpt(queryParam.get("sqlPath").toString(), (List<String>) queryParam.get("values"));
			list = rponblocktab_Dao.getRptData(query.toString(), new ArrayList<String>());
			int pathType = Arguments.getStringArg("ISWINOPEN").equals("Y")?RptUtils.C_PATH :RptUtils.R_PATH;
			String outputFilePath = "";
			if(ext.equals("xls")){
				outputFilePath = RptUtils.export(RptUtils.COLLECTION,pathType, "ex_st_15", "st_15", params, list, 2);
			}else if(ext.equals("pdf")){
				outputFilePath = RptUtils.export(RptUtils.COLLECTION,pathType, "st_15", "st_15", params, list);
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
	
	public List<String> getConditionList(String txtime, String bstype, String opbkId){
		List<String> conditions = new ArrayList<String>();
		
		List<String> conditions_1 = new ArrayList<String>();
		List<String> conditions_2 = new ArrayList<String>();
		
		if(StrUtils.isNotEmpty(txtime)){
			//20151022 HUANGPU 改用交易日期查較合邏輯
			//conditions_1.add(" A.BIZDATE= '"+txtime+"' ");
			conditions_1.add(" A.TXDATE= '"+txtime+"' ");
		}
		if(StrUtils.isNotEmpty(opbkId)){//XX行所屬操作行
			conditions_1.add(" (A.SENDERACQUIRE= '"+opbkId+"' OR A.OUTACQUIRE= '"+opbkId+"' OR A.INACQUIRE= '"+opbkId+"') ");
			conditions_2.add(" BANKID = '" + opbkId + "' ");
		}
		if(StrUtils.isNotEmpty(bstype)){//業務類別
			conditions_1.add(" A.PCODE LIKE '" + bstype.substring(0,2) + "%' ");
		}
		
		conditions.add( combine(conditions_1) );
		conditions.add( combine(conditions_2) );
		
		return conditions;
	}
	
	public String getEvdayTimeTolSQL(String txtime, String bstype, String opbkId){
		List<String> conditions = getConditionList(txtime, bstype, opbkId);
		String condition_1 = conditions.get(0);
		String condition_2 = conditions.get(1);
		
		StringBuffer withTemp = new StringBuffer();
		withTemp.append("WITH TEMP AS ( ");
		withTemp.append("    SELECT ");
		withTemp.append("    DOUBLE(DATE_DIFF(ENDTIME, DT_REQ_1)) / CAST (1000000 AS BIGINT) AS TIME_1, ");
		withTemp.append("    DOUBLE(DATE_DIFF(DT_RSP_1, DT_REQ_2)) / CAST (1000000 AS BIGINT) AS TIME_2, ");
		withTemp.append("    (DOUBLE(DATE_DIFF(DT_REQ_2, DT_REQ_1)) + DOUBLE(DATE_DIFF(DT_RSP_2, DT_RSP_1))) / CAST (1000000 AS BIGINT) AS TIME_3, ");
		//20160130 edit by hugo req by SRS_20160122
//		withTemp.append("    (CASE C.TXN_TYPE WHEN '4' THEN (DOUBLE(DATE_DIFF(DT_RSP_3, DT_REQ_3)) / CAST(1000000 AS BIGINT)) ELSE 0 END) AS TIME_4, ");
//		withTemp.append("    (CASE C.TXN_TYPE WHEN '4' THEN ((DOUBLE(DATE_DIFF(DT_REQ_2,DT_REQ_1)) + DOUBLE(DATE_DIFF(DT_REQ_3,DT_RSP_1)) + DOUBLE(DATE_DIFF(DT_RSP_2,DT_RSP_3))) / CAST(1000000 AS BIGINT)) ELSE 0 END) AS TIME_5, ");
		withTemp.append("    (CASE  WHEN C.TXN_TYPE IN ('4','6') THEN (DOUBLE(DATE_DIFF(DT_RSP_3, DT_REQ_3)) / CAST(1000000 AS BIGINT)) ELSE 0 END) AS TIME_4, ");
		withTemp.append("    (CASE  WHEN C.TXN_TYPE IN ('4','6') THEN ((DOUBLE(DATE_DIFF(DT_REQ_2,DT_REQ_1)) + DOUBLE(DATE_DIFF(DT_REQ_3,DT_RSP_1)) + DOUBLE(DATE_DIFF(DT_RSP_2,DT_RSP_3))) / CAST(1000000 AS BIGINT)) ELSE 0 END) AS TIME_5, ");
		withTemp.append("    C.* ");
		withTemp.append("    FROM ( ");
		withTemp.append("        SELECT ");
		withTemp.append("        (CASE WHEN RC2 = '3001' THEN 1 ELSE 0 END) OKCOUNT, ");
		withTemp.append("        (CASE WHEN RC2 NOT IN ('3001','0601') THEN 1 ELSE 0 END) FAILCOUNT, ");
		withTemp.append("        (CASE WHEN RC2 = '0601' THEN 1 ELSE 0 END) PENDCOUNT, ");
		withTemp.append("        INTEGER(SUBSTR(A.TXDT,9,2)) AS HOURLAP, A.RESULTSTATUS, ");
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
		withTemp.append("		 " + (StrUtils.isEmpty(condition_1)? "" : "AND " + condition_1));
		withTemp.append("    ) AS C ");
		withTemp.append("), TEMP_ AS ( ");
		withTemp.append("    SELECT SENDERACQUIRE AS BANKID FROM TEMP GROUP BY SENDERACQUIRE ");
		withTemp.append("    UNION ");
		withTemp.append("    SELECT OUTACQUIRE FROM TEMP GROUP BY OUTACQUIRE ");
		withTemp.append("    UNION ");
		withTemp.append("    SELECT INACQUIRE FROM TEMP GROUP BY INACQUIRE ");
		withTemp.append("), TEMP__ AS ( ");
		withTemp.append("    SELECT ");
		withTemp.append("    (CASE WHEN RC2 <> '0601' AND TIME_1 > TXN_STD_PROC_TIME THEN TIME_1 ELSE 0 END) PRC_FLAG, ");
		//20160130 edit by hugo req by SRS_20160122
//		withTemp.append("    (CASE TXN_TYPE ");
//		withTemp.append("    WHEN '2' THEN (CASE WHEN RC2 <> '0601' AND TIME_2 > BANK_SC_STD_PROC_TIME THEN TIME_2 ELSE 0 END) ");
//		withTemp.append("    WHEN '4' THEN (CASE WHEN RC2 <> '0601' AND DT_REQ_3 <> 0 AND TIME_4 > BANK_SC_STD_PROC_TIME THEN TIME_4 ELSE 0 END) ");
		withTemp.append("    (CASE  ");
		withTemp.append("    WHEN TXN_TYPE= '2' THEN (CASE WHEN RC2 <> '0601' AND TIME_2 > BANK_SC_STD_PROC_TIME THEN TIME_2 ELSE 0 END) ");
		withTemp.append("    WHEN TXN_TYPE IN ('4','6') THEN (CASE WHEN RC2 <> '0601' AND DT_REQ_3 <> 0 AND TIME_4 > BANK_SC_STD_PROC_TIME THEN TIME_4 ELSE 0 END) ");
		withTemp.append("    ELSE 0 END) SAVE_FLAG, ");
		//20160130 edit by hugo req by SRS_20160122
//		withTemp.append("    (CASE WHEN TXN_TYPE IN ('1','3','4') THEN ( ");
		withTemp.append("    (CASE WHEN TXN_TYPE IN ('1','3','4','5','6') THEN ( ");
		withTemp.append("        CASE WHEN RC2 <> '0601' AND TIME_2 > BANK_SD_STD_PROC_TIME THEN TIME_2 ELSE 0 END ");
		withTemp.append("    ) ELSE 0 END) AS DEBIT_FLAG, ");
		//20160130 edit by hugo req by SRS_20160122
//		withTemp.append("    (CASE TXN_TYPE ");
//		withTemp.append("    WHEN '4' THEN (CASE WHEN RC2 <> '0601' AND TIME_5 > TCH_STD_ECHO_TIME THEN TIME_5 ELSE 0 END) ");
		withTemp.append("    (CASE  ");
		withTemp.append("    WHEN TXN_TYPE IN ('4','6') THEN (CASE WHEN RC2 <> '0601' AND TIME_5 > TCH_STD_ECHO_TIME THEN TIME_5 ELSE 0 END) ");
		withTemp.append("    ELSE (CASE WHEN RC2 <> '0601' AND TIME_3 > TCH_STD_ECHO_TIME THEN TIME_3 ELSE 0 END) ");
		withTemp.append("    END) AS ACHPRC_FLAG, TEMP.* ");
		withTemp.append("    FROM TEMP, SYS_PARA ");
		withTemp.append("), TEMP___ AS ( ");
		withTemp.append("    SELECT ");
		withTemp.append("    B.BANKID, COALESCE(EACHUSER.GETBKNAME(B.BANKID), '') AS BANKIDANDNAME, ");
		withTemp.append("    (SELECT SUM(OKCOUNT + FAILCOUNT + PENDCOUNT) FROM TEMP AS A WHERE (A.SENDERACQUIRE = B.BANKID OR A.OUTACQUIRE = B.BANKID OR A.INACQUIRE = B.BANKID)) AS TOTALCOUNT, ");
		withTemp.append("    (SELECT SUM(PENDCOUNT) FROM TEMP AS A WHERE (A.SENDERACQUIRE = B.BANKID OR A.OUTACQUIRE = B.BANKID OR A.INACQUIRE = B.BANKID)) AS PENDCOUNT, ");
		withTemp.append("    DECIMAL(COALESCE((SELECT SUM(PRC_FLAG) FROM TEMP__ AS A WHERE (A.SENDERACQUIRE = B.BANKID OR A.OUTACQUIRE = B.BANKID OR A.INACQUIRE = B.BANKID) AND PRC_FLAG <> 0),0),18,2) PRCTIME, ");
		withTemp.append("    DECIMAL(COALESCE((SELECT SUM(SAVE_FLAG) FROM TEMP__ AS A WHERE (A.SENDERACQUIRE = B.BANKID OR A.OUTACQUIRE = B.BANKID OR A.INACQUIRE = B.BANKID) AND SAVE_FLAG <> 0),0),18,2) SAVETIME, ");
		withTemp.append("    DECIMAL(COALESCE((SELECT SUM(DEBIT_FLAG) FROM TEMP__ AS A WHERE (A.SENDERACQUIRE = B.BANKID OR A.OUTACQUIRE = B.BANKID OR A.INACQUIRE = B.BANKID) AND DEBIT_FLAG <> 0),0),18,2) DEBITTIME, ");
		withTemp.append("    DECIMAL(COALESCE((SELECT SUM(ACHPRC_FLAG) FROM TEMP__ AS A WHERE (A.SENDERACQUIRE = B.BANKID OR A.OUTACQUIRE = B.BANKID OR A.INACQUIRE = B.BANKID) AND ACHPRC_FLAG <> 0),0),18,2) ACHPRCTIME, ");
		withTemp.append("    (SELECT SUM(CASE WHEN PRC_FLAG <> 0 THEN 1 ELSE 0 END) FROM TEMP__ AS A WHERE (A.SENDERACQUIRE = B.BANKID OR A.OUTACQUIRE = B.BANKID OR A.INACQUIRE = B.BANKID)) AS PRCCOUNT, ");
		withTemp.append("    (SELECT SUM(CASE WHEN SAVE_FLAG <> 0 THEN 1 ELSE 0 END) FROM TEMP__ AS A WHERE (A.SENDERACQUIRE = B.BANKID OR A.OUTACQUIRE = B.BANKID OR A.INACQUIRE = B.BANKID)) AS SAVECOUNT, ");
		withTemp.append("    (SELECT SUM(CASE WHEN DEBIT_FLAG <> 0 THEN 1 ELSE 0 END) FROM TEMP__ AS A WHERE (A.SENDERACQUIRE = B.BANKID OR A.OUTACQUIRE = B.BANKID OR A.INACQUIRE = B.BANKID)) AS DEBITCOUNT, ");
		withTemp.append("    (SELECT SUM(CASE WHEN ACHPRC_FLAG <> 0 THEN 1 ELSE 0 END) FROM TEMP__ AS A WHERE (A.SENDERACQUIRE = B.BANKID OR A.OUTACQUIRE = B.BANKID OR A.INACQUIRE = B.BANKID)) AS ACHPRCCOUNT ");
		withTemp.append("    FROM TEMP_ AS B ");
		withTemp.append("     " + (StrUtils.isEmpty(condition_2)? "" : "WHERE " + condition_2));
		withTemp.append("), TEMP____ AS ( ");
		withTemp.append("    SELECT ");
		withTemp.append("    SUM(TOTALCOUNT) AS TOTALCOUNT, SUM(PENDCOUNT) AS PENDCOUNT, ");
		withTemp.append("    SUM(PRCCOUNT) AS PRCCOUNT, SUM(SAVECOUNT) AS SAVECOUNT, ");
		withTemp.append("    SUM(DEBITCOUNT) AS DEBITCOUNT, SUM(ACHPRCCOUNT) AS ACHPRCCOUNT, ");
		withTemp.append("    SUM(PRCTIME) AS PRCTIME, SUM(SAVETIME) AS SAVETIME, ");
		withTemp.append("    SUM(DEBITTIME) AS DEBITTIME, SUM(ACHPRCTIME) AS ACHPRCTIME ");
		withTemp.append("    FROM TEMP___ ");
		withTemp.append(")");
		
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

	public EACH_TXN_CODE_Dao getEach_txn_code_Dao() {
		return each_txn_code_Dao;
	}

	public void setEach_txn_code_Dao(EACH_TXN_CODE_Dao each_txn_code_Dao) {
		this.each_txn_code_Dao = each_txn_code_Dao;
	}

	public BANK_OPBK_BO getBank_opbk_bo() {
		return bank_opbk_bo;
	}

	public void setBank_opbk_bo(BANK_OPBK_BO bank_opbk_bo) {
		this.bank_opbk_bo = bank_opbk_bo;
	}

	public BUSINESS_TYPE_Dao getBusiness_type_Dao() {
		return business_type_Dao;
	}

	public void setBusiness_type_Dao(BUSINESS_TYPE_Dao business_type_Dao) {
		this.business_type_Dao = business_type_Dao;
	}

	public SYS_PARA_Dao getSys_para_Dao() {
		return sys_para_Dao;
	}

	public void setSys_para_Dao(SYS_PARA_Dao sys_para_Dao) {
		this.sys_para_Dao = sys_para_Dao;
	}
	
}
