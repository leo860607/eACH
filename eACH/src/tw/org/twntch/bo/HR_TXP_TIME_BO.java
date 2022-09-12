package tw.org.twntch.bo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.struts.util.LabelValueBean;

import tw.org.twntch.bean.HR_TXP_TIME;
import tw.org.twntch.db.dao.hibernate.BANK_GROUP_Dao;
import tw.org.twntch.db.dao.hibernate.EACH_TXN_CODE_Dao;
import tw.org.twntch.db.dao.hibernate.ONBLOCKTAB_Dao;
import tw.org.twntch.db.dao.hibernate.Page;
import tw.org.twntch.db.dao.hibernate.RPONBLOCKTAB_Dao;
import tw.org.twntch.po.BANK_GROUP;
import tw.org.twntch.po.BANK_OPBK;
import tw.org.twntch.po.EACH_TXN_CODE;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.RptUtils;
import tw.org.twntch.util.StrUtils;

public class HR_TXP_TIME_BO {
	private ONBLOCKTAB_Dao onblocktab_Dao;
	private BANK_GROUP_Dao bank_group_Dao;
	private EACH_TXN_CODE_Dao each_txn_code_Dao;
	private BANK_OPBK_BO bank_opbk_bo;
	private RPONBLOCKTAB_Dao rponblocktab_Dao;
	
	/**
	 * 取得操作行代號清單
	 * select distinct OPBK_ID where Currentdate between 啟用日期 and 停用日期
	 * @return
	 */
	public List<LabelValueBean> getOpbkIdList(){
		List<BANK_OPBK> list = bank_opbk_bo.getAllOpbkList();
		List<LabelValueBean> beanList = new LinkedList<LabelValueBean>();
		LabelValueBean bean = null;
		for(BANK_OPBK po : list){
			bean = new LabelValueBean(po.getOPBK_ID() + " - " + po.getOPBK_NAME(), po.getOPBK_ID());
			beanList.add(bean);
		}
		return beanList;
//		List<BANK_GROUP> list = bank_group_Dao.getBgbkIdList_2();
//		List<LabelValueBean> beanList = new LinkedList<LabelValueBean>();
//		LabelValueBean bean = null;
//		for(BANK_GROUP po : list){
//			bean = new LabelValueBean(po.getBGBK_ID() + " - " + po.getBGBK_NAME(), po.getBGBK_ID());
//			beanList.add(bean);
//		}
//		System.out.println("beanList>>"+beanList);
//		return beanList;
	}
	
	/**
	 * 取得交易代號(PCODE-4碼)清單
	 * @return
	 */
	public List<LabelValueBean> getPcodeList(){
		List<EACH_TXN_CODE> list = each_txn_code_Dao.getTranCode();
		List<LabelValueBean> beanList = new LinkedList<LabelValueBean>();
		LabelValueBean bean = null;
		for(EACH_TXN_CODE po : list){
			bean = new LabelValueBean(po.getEACH_TXN_ID() + " - " + po.getEACH_TXN_NAME(), po.getEACH_TXN_ID());
			beanList.add(bean);
		}
		System.out.println("beanList>>"+beanList);
		return beanList;
	}
	
	public Map<String, String> qs_ex_export(String txdate, String pcode, String opbkId, String bgbkId, String clearingPhase, String serchStrs, String sortname, String sortorder){
		Map<String, String> rtnMap = null;
		
		try{
			rtnMap = new HashMap<String, String>();
			rtnMap.put("serchStrs", serchStrs);
			Map<String, Object> params = new HashMap<String, Object>();
			String sql = getSQL(txdate, pcode, opbkId, bgbkId, clearingPhase, serchStrs, sortname, sortorder);
			List list = rponblocktab_Dao.getRptData(sql, new ArrayList());
			
			int pathType = Arguments.getStringArg("ISWINOPEN").equals("Y")?RptUtils.C_PATH :RptUtils.R_PATH;
			String outputFilePath = RptUtils.export(RptUtils.COLLECTION,pathType, "hr_txp_time", "hr_txp_time", params, list, 2);
			//String outputFilePath = "";
			if(StrUtils.isNotEmpty(outputFilePath)){
				rtnMap.put("result", "TRUE");
				rtnMap.put("msg", outputFilePath);
			}else{
				rtnMap.put("result", "FALSE");
				rtnMap.put("msg", "產生失敗!");
			}
			System.out.println("EXPORT SQL >> " + sql);
		}catch(Exception e){
			e.printStackTrace();
			rtnMap.put("result", "FALSE");
			rtnMap.put("msg", "產生錯誤!");
		}
		
		return rtnMap;
	}
	
	public String getSQL(String txdate, String pcode, String opbkId, String bgbkId, String clearingPhase, String serchStrs, String sidx, String sord){
		HashMap<String, String> param = new HashMap<String, String>();
		param.put("TXDATE", txdate);
		param.put("PCODE", pcode);
		param.put("OPBK_ID", opbkId);
		param.put("BGBK_ID", bgbkId);
		param.put("CLEARINGPHASE", clearingPhase);
		List<String> conditions = getConditionList(param);
		String condition = conditions.get(0);
		
		StringBuffer withTemp = new StringBuffer();
		withTemp.append("WITH TEMP AS ( ");
		withTemp.append("    SELECT ");
		withTemp.append("    DOUBLE(DATE_DIFF(ENDTIME, DT_REQ_1)) / CAST (1000000 AS BIGINT) AS TIME_1, ");
		withTemp.append("    DOUBLE(DATE_DIFF(DT_RSP_1, DT_REQ_2)) / CAST (1000000 AS BIGINT) AS TIME_2, ");
		withTemp.append("    (DOUBLE(DATE_DIFF(DT_REQ_2, DT_REQ_1)) + DOUBLE(DATE_DIFF(DT_RSP_2, DT_RSP_1))) / CAST (1000000 AS BIGINT) AS TIME_3, ");
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
		withTemp.append("        " + (StrUtils.isEmpty(condition)? "" : "AND " + condition) + " ");
		withTemp.append("    ) AS C ");
		withTemp.append(") ");
		
		StringBuffer sql = new StringBuffer();
		sql.append(withTemp);
		sql.append("    SELECT ROWNUMBER() OVER(");
		if(StrUtils.isNotEmpty(param.get("sidx"))){
			if("PRCTIME".equalsIgnoreCase(param.get("sidx"))){
				sql.append(" ORDER BY DECIMAL(COALESCE((SELECT SUM(TIME_1) / CAST(COUNT(*) AS BIGINT) FROM TEMP WHERE HOURLAP = A.HOURLAP AND RC2 <> '0601'),0),18,2) "+param.get("sord"));
			}
			else if("SAVETIME".equalsIgnoreCase(param.get("sidx"))){
				sql.append(" ORDER BY ( DECIMAL(COALESCE((SELECT SUM(TIME_2) / CAST(COUNT(*) AS BIGINT) FROM TEMP WHERE HOURLAP = A.HOURLAP AND TXN_TYPE = '2' AND RC2 <> '0601'),0),18,2) + ");
				//20160130 edit by hugo req by SRS_20160122
//				sql.append("DECIMAL(COALESCE((SELECT SUM((DOUBLE(DATE_DIFF(DT_RSP_3, DT_REQ_3)) / CAST (1000000 AS BIGINT))) / CAST(COUNT(*) AS BIGINT) FROM TEMP WHERE HOURLAP = A.HOURLAP AND TXN_TYPE = '4' AND DT_REQ_3 <> 0 AND RC2 <> '0601'),0),18,2) " + param.get("sord"));
				sql.append("DECIMAL(COALESCE((SELECT SUM((DOUBLE(DATE_DIFF(DT_RSP_3, DT_REQ_3)) / CAST (1000000 AS BIGINT))) / CAST(COUNT(*) AS BIGINT) FROM TEMP WHERE HOURLAP = A.HOURLAP AND TXN_TYPE IN ('4','6') AND DT_REQ_3 <> 0 AND RC2 <> '0601'),0),18,2) " + param.get("sord"));
			}
			else if("DEBITTIME".equalsIgnoreCase(param.get("sidx"))){
				//20160130 edit by hugo req by SRS_20160122
//				sql.append(" ORDER BY DECIMAL(COALESCE((SELECT SUM(TIME_2) / CAST(COUNT(*) AS BIGINT) FROM TEMP WHERE HOURLAP = A.HOURLAP AND TXN_TYPE IN ('1','3','4') AND RC2 <> '0601'),0),18,2) "+param.get("sord"));
				sql.append(" ORDER BY DECIMAL(COALESCE((SELECT SUM(TIME_2) / CAST(COUNT(*) AS BIGINT) FROM TEMP WHERE HOURLAP = A.HOURLAP AND TXN_TYPE IN ('1','3','4' ,'5','6') AND RC2 <> '0601'),0),18,2) "+param.get("sord"));
			}
			else if("ACHPRCTIME".equalsIgnoreCase(param.get("sidx"))){
				//20160130 edit by hugo req by SRS_20160122
//				sql.append(" ORDER BY DECIMAL(COALESCE((SELECT SUM(CASE TXN_TYPE WHEN '4' THEN (DATE_DIFF(DT_REQ_2,DT_REQ_1) + DATE_DIFF(DT_REQ_3,DT_RSP_1) + DATE_DIFF(DT_RSP_2,DT_RSP_3)) / CAST (1000000 AS BIGINT) ELSE TIME_3 END) / CAST(COUNT(*) AS BIGINT) FROM TEMP WHERE HOURLAP = A.HOURLAP AND RC2 <> '0601'),0),18,2) "+param.get("sord"));
				sql.append(" ORDER BY DECIMAL(COALESCE((SELECT SUM(CASE  WHEN TXN_TYPE IN('4','6') THEN (DATE_DIFF(DT_REQ_2,DT_REQ_1) + DATE_DIFF(DT_REQ_3,DT_RSP_1) + DATE_DIFF(DT_RSP_2,DT_RSP_3)) / CAST (1000000 AS BIGINT) ELSE TIME_3 END) / CAST(COUNT(*) AS BIGINT) FROM TEMP WHERE HOURLAP = A.HOURLAP AND RC2 <> '0601'),0),18,2) "+param.get("sord"));
			}
			else{
				sql.append(" ORDER BY "+param.get("sidx")+" "+param.get("sord"));
			}
		}else{
			sql.append(" ORDER BY A.HOURLAP ");
		}
		sql.append(") AS ROWNUMBER, A.*, ");
		sql.append("    COALESCE(B.TOTALCOUNT,0) AS TOTALCOUNT, COALESCE(B.OKCOUNT,0) AS OKCOUNT, ");
		sql.append("    COALESCE(B.FAILCOUNT,0) AS FAILCOUNT, COALESCE(B.PENDCOUNT,0) AS PENDCOUNT, ");
		sql.append("    DECIMAL(COALESCE((SELECT SUM(TIME_1) / CAST(COUNT(*) AS BIGINT) FROM TEMP WHERE HOURLAP = A.HOURLAP AND RC2 <> '0601'),0),18,2) PRCTIME, ");
		sql.append("    DECIMAL(COALESCE((SELECT SUM(TIME_2) / CAST(COUNT(*) AS BIGINT) FROM TEMP WHERE HOURLAP = A.HOURLAP AND TXN_TYPE = '2' AND RC2 <> '0601'),0),18,2) + ");
		//20160130 edit by hugo req by SRS_20160122
//		sql.append("    DECIMAL(COALESCE((SELECT SUM((DOUBLE(DATE_DIFF(DT_RSP_3, DT_REQ_3)) / CAST (1000000 AS BIGINT))) / CAST(COUNT(*) AS BIGINT) FROM TEMP WHERE HOURLAP = A.HOURLAP AND TXN_TYPE = '4' AND DT_REQ_3 <> 0 AND RC2 <> '0601'),0),18,2) SAVETIME, ");
//		sql.append("    DECIMAL(COALESCE((SELECT SUM(TIME_2) / CAST(COUNT(*) AS BIGINT) FROM TEMP WHERE HOURLAP = A.HOURLAP AND TXN_TYPE IN ('1','3','4') AND RC2 <> '0601'),0),18,2) DEBITTIME, ");
//		sql.append("    DECIMAL(COALESCE((SELECT SUM(CASE TXN_TYPE WHEN '4' THEN (DATE_DIFF(DT_REQ_2,DT_REQ_1) + DATE_DIFF(DT_REQ_3,DT_RSP_1) + DATE_DIFF(DT_RSP_2,DT_RSP_3)) / CAST (1000000 AS BIGINT) ELSE TIME_3 END) / CAST(COUNT(*) AS BIGINT) FROM TEMP WHERE HOURLAP = A.HOURLAP AND RC2 <> '0601'),0),18,2) ACHPRCTIME ");
		sql.append("    DECIMAL(COALESCE((SELECT SUM((DOUBLE(DATE_DIFF(DT_RSP_3, DT_REQ_3)) / CAST (1000000 AS BIGINT))) / CAST(COUNT(*) AS BIGINT) FROM TEMP WHERE HOURLAP = A.HOURLAP AND TXN_TYPE IN ('4','6') AND DT_REQ_3 <> 0 AND RC2 <> '0601'),0),18,2) SAVETIME, ");
		sql.append("    DECIMAL(COALESCE((SELECT SUM(TIME_2) / CAST(COUNT(*) AS BIGINT) FROM TEMP WHERE HOURLAP = A.HOURLAP AND TXN_TYPE IN ('1','3','4','5','6') AND RC2 <> '0601'),0),18,2) DEBITTIME, ");
		sql.append("    DECIMAL(COALESCE((SELECT SUM(CASE  WHEN TXN_TYPE IN('4','6') THEN (DATE_DIFF(DT_REQ_2,DT_REQ_1) + DATE_DIFF(DT_REQ_3,DT_RSP_1) + DATE_DIFF(DT_RSP_2,DT_RSP_3)) / CAST (1000000 AS BIGINT) ELSE TIME_3 END) / CAST(COUNT(*) AS BIGINT) FROM TEMP WHERE HOURLAP = A.HOURLAP AND RC2 <> '0601'),0),18,2) ACHPRCTIME ");
		sql.append("    FROM HOURITEM AS A LEFT JOIN ( ");
		sql.append("        SELECT ");
		sql.append("        HOURLAP, ");
		sql.append("        SUM(OKCOUNT + FAILCOUNT + PENDCOUNT) AS TOTALCOUNT, SUM(OKCOUNT) AS OKCOUNT, ");
		sql.append("        SUM(FAILCOUNT) AS FAILCOUNT, SUM(PENDCOUNT) AS PENDCOUNT ");
		sql.append("        FROM TEMP ");
		sql.append("        GROUP BY HOURLAP ");
		sql.append("    ) AS B ON A.HOURLAP = B.HOURLAP ");
		sql.append("    WHERE A.HOURLAP IS NOT NULL ");
		System.out.println("sql = " + sql.toString().toUpperCase());
		return sql.toString();
	}
	
	public String pageSearch(Map<String, String> param){
		List<String> conditions = getConditionList(param);
		
		String pageNo = StrUtils.isEmpty(param.get("page")) ?"0":param.get("page");
		String pageSize = StrUtils.isEmpty(param.get("rows")) ?Arguments.getStringArg("PAGE.SIZE"):param.get("rows");
		
		Map rtnMap = new HashMap();
		
		List<HR_TXP_TIME> list = null;
		Page page = null;
		try {
			list = new ArrayList<HR_TXP_TIME>();
			String condition = conditions.get(0);
			
			StringBuffer withTemp = new StringBuffer();
			/*
			withTemp.append("WITH TEMP AS ( ");
			withTemp.append("	SELECT INTEGER(SUBSTR(A.TXDT,9,2)) HOURLAP, ");
			withTemp.append("	A.TXDT, A.NEWRESULT AS RESULTSTATUS, ");
			withTemp.append("	(case when length(COALESCE(a.DT_Req_1,''))=0 then '0' else a.DT_Req_1 end ) DT_Req_1, ");
			withTemp.append("	(case when length(COALESCE(a.DT_Req_2,''))=0 then '0' else a.DT_Req_2 end ) DT_Req_2,  ");
			withTemp.append("	(case when length(COALESCE(a.DT_Rsp_1,''))=0 then '0' else a.DT_Rsp_1 end ) DT_Rsp_1, ");
			withTemp.append("	(case when length(COALESCE(a.DT_Rsp_2,''))=0 then '0' else a.DT_Rsp_2 end ) DT_Rsp_2, ");
			withTemp.append("	(case when length(COALESCE(a.DT_Con_1,''))=0 then '0' else a.DT_Con_1 end ) DT_Con_1, ");
			withTemp.append("	(case when length(COALESCE(a.DT_Con_2,''))=0 then '0' else a.DT_Con_2 end ) DT_Con_2, ");
//			withTemp.append("	(case when length(a.DT_Req_1)=0 then '0' else a.DT_Req_1 end ) DT_Req_1, ");
//			withTemp.append("	(case when length(a.DT_Req_2)=0  then '0' else a.DT_Req_2 end ) DT_Req_2, ");
//			withTemp.append("	(case when length(a.DT_Rsp_1)=0  then '0' else a.DT_Rsp_1 end ) DT_Rsp_1, ");
//			withTemp.append("	(case when length(a.DT_Rsp_2)=0  then '0' else a.DT_Rsp_2 end ) DT_Rsp_2, ");
//			withTemp.append("	(case when length(a.DT_Con_1)=0  then '0' else a.DT_Con_1 end ) DT_Con_1, ");
//			withTemp.append("	(case when length(a.DT_Con_2)=0  then '0' else a.DT_Con_2 end ) DT_Con_2, ");
			withTemp.append("	(case when length(COALESCE(a.DT_Con_2,''))=0 then ( ");
			withTemp.append("	    case when length(COALESCE(a.DT_Con_1,''))=0 then ( ");
			withTemp.append("	    case when length(COALESCE(a.DT_Rsp_2,''))=0 then ( ");
			withTemp.append("	    case when length(COALESCE(a.DT_Rsp_1,''))=0 then ( ");
			withTemp.append("	    case when length(COALESCE(a.DT_Req_2,''))=0 then a.DT_Req_1 else a.DT_Req_2 end) ");
			withTemp.append("	    else a.DT_Rsp_1 end ) else a.DT_Rsp_2 end ) else a.DT_Con_1 end) else a.DT_Con_2 end ");
			withTemp.append("	) AS EndTime ");
			withTemp.append("	FROM VW_ONBLOCKTAB A ");
			withTemp.append("	" + (StrUtils.isEmpty(condition)? "" : "WHERE " + condition));
			withTemp.append(") ");
			*/
			withTemp.append("WITH TEMP AS ( ");
			withTemp.append("    SELECT ");
			withTemp.append("    DOUBLE(DATE_DIFF(ENDTIME, DT_REQ_1)) / CAST (1000000 AS BIGINT) AS TIME_1, ");
			withTemp.append("    DOUBLE(DATE_DIFF(DT_RSP_1, DT_REQ_2)) / CAST (1000000 AS BIGINT) AS TIME_2, ");
			withTemp.append("    (DOUBLE(DATE_DIFF(DT_REQ_2, DT_REQ_1)) + DOUBLE(DATE_DIFF(DT_RSP_2, DT_RSP_1))) / CAST (1000000 AS BIGINT) AS TIME_3, ");
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
			withTemp.append("        " + (StrUtils.isEmpty(condition)? "" : "AND " + condition) + " ");
			withTemp.append("    ) AS C ");
			withTemp.append(") ");
			
			//先算出總計的欄位值，放到rtnMap裡面，到頁面再放到下面的總計Grid裡面
			String dataSumCols[] = {"TOTALCOUNT", "OKCOUNT", "FAILCOUNT", "PENDCOUNT"};
			StringBuffer dataSumSQL = new StringBuffer();
			dataSumSQL.append(withTemp);
			/*
			dataSumSQL.append("SELECT SUM(X.TOTALCOUNT) TOTALCOUNT,SUM(X.OKCOUNT) OKCOUNT,SUM(X.FAILCOUNT) FAILCOUNT,SUM(X.PENDCOUNT) PENDCOUNT FROM( ");
			dataSumSQL.append("	SELECT ");
			dataSumSQL.append(" (SELECT COUNT(*) FROM TEMP WHERE HOURLAP=A.HOURLAP) TOTALCOUNT, ");
			dataSumSQL.append(" (SELECT COUNT(*) FROM TEMP WHERE HOURLAP=A.HOURLAP AND RESULTSTATUS='A' ) OKCOUNT, ");
			dataSumSQL.append(" (SELECT COUNT(*) FROM TEMP WHERE HOURLAP=A.HOURLAP AND RESULTSTATUS='R' ) FAILCOUNT, ");
			dataSumSQL.append(" (SELECT COUNT(*) FROM TEMP WHERE HOURLAP=A.HOURLAP AND RESULTSTATUS='P' ) PENDCOUNT ");
			dataSumSQL.append("	FROM HOURITEM AS A ");
			dataSumSQL.append(") AS X(TOTALCOUNT,OKCOUNT,FAILCOUNT,PENDCOUNT)");
			*/
			dataSumSQL.append("SELECT ");
			dataSumSQL.append("SUM(COALESCE(B.TOTALCOUNT,0)) AS TOTALCOUNT, SUM(COALESCE(B.OKCOUNT,0)) AS OKCOUNT, ");
			dataSumSQL.append("SUM(COALESCE(B.FAILCOUNT,0)) AS FAILCOUNT, SUM(COALESCE(B.PENDCOUNT,0)) AS PENDCOUNT ");
			dataSumSQL.append("FROM HOURITEM AS A LEFT JOIN ( ");
			dataSumSQL.append("    SELECT ");
			dataSumSQL.append("    HOURLAP, ");
			dataSumSQL.append("    SUM(OKCOUNT + FAILCOUNT + PENDCOUNT) AS TOTALCOUNT, SUM(OKCOUNT) AS OKCOUNT, ");
			dataSumSQL.append("    SUM(FAILCOUNT) AS FAILCOUNT, SUM(PENDCOUNT) AS PENDCOUNT ");
			dataSumSQL.append("    FROM TEMP ");
			dataSumSQL.append("    GROUP BY HOURLAP ");
			dataSumSQL.append(") AS B ON A.HOURLAP = B.HOURLAP ");
			dataSumSQL.append("WHERE A.HOURLAP IS NOT NULL ");
			System.out.println("dataSumSQL = " + dataSumSQL.toString().toUpperCase());
			
			list = onblocktab_Dao.dataSum(dataSumSQL.toString(),dataSumCols,HR_TXP_TIME.class);
			rtnMap.put("dataSumList", list);
			/*
			for(HR_TXP_TIME po:list){
				System.out.println(String.format("SUM(X.TOTALCOUNT)=%s,SUM(X.OKCOUNT)=%s,SUM(X.FAILCOUNT)=%s,SUM(X.PENDCOUNT)=%s",
					po.getTOTALCOUNT(),po.getOKCOUNT(),po.getFAILCOUNT(),po.getPENDCOUNT()));
			}
			*/
			
			StringBuffer countQuery = new StringBuffer();
			StringBuffer sql = new StringBuffer();
			String cols[] = {"HOURLAP","HOURLAPNAME","TOTALCOUNT","OKCOUNT","FAILCOUNT","PENDCOUNT","PRCTIME","DEBITTIME","SAVETIME","ACHPRCTIME"};
			
			sql.append(withTemp);
			sql.append("SELECT * FROM ( ");
			sql.append("    SELECT ROWNUMBER() OVER(");
			if(StrUtils.isNotEmpty(param.get("sidx"))){
				if("PRCTIME".equalsIgnoreCase(param.get("sidx"))){
					sql.append(" ORDER BY DECIMAL(COALESCE((SELECT SUM(TIME_1) / CAST(COUNT(*) AS BIGINT) FROM TEMP WHERE HOURLAP = A.HOURLAP AND RC2 <> '0601'),0),18,2) "+param.get("sord"));
				}
				else if("SAVETIME".equalsIgnoreCase(param.get("sidx"))){
					sql.append(" ORDER BY ( DECIMAL(COALESCE((SELECT SUM(TIME_2) / CAST(COUNT(*) AS BIGINT) FROM TEMP WHERE HOURLAP = A.HOURLAP AND TXN_TYPE = '2' AND RC2 <> '0601'),0),18,2) + ");
					sql.append("DECIMAL(COALESCE((SELECT SUM((DOUBLE(DATE_DIFF(DT_RSP_3, DT_REQ_3)) / CAST (1000000 AS BIGINT))) / CAST(COUNT(*) AS BIGINT) FROM TEMP WHERE HOURLAP = A.HOURLAP AND TXN_TYPE = '4' AND DT_REQ_3 <> 0 AND RC2 <> '0601'),0),18,2) " + param.get("sord"));
				}
				else if("DEBITTIME".equalsIgnoreCase(param.get("sidx"))){
					//20160130 edit by hugo req by SRS_20160122
//					sql.append(" ORDER BY DECIMAL(COALESCE((SELECT SUM(TIME_2) / CAST(COUNT(*) AS BIGINT) FROM TEMP WHERE HOURLAP = A.HOURLAP AND TXN_TYPE IN ('1','3','4') AND RC2 <> '0601'),0),18,2) "+param.get("sord"));
					sql.append(" ORDER BY DECIMAL(COALESCE((SELECT SUM(TIME_2) / CAST(COUNT(*) AS BIGINT) FROM TEMP WHERE HOURLAP = A.HOURLAP AND TXN_TYPE IN ('1','3','4','5','6' ) AND RC2 <> '0601'),0),18,2) "+param.get("sord"));
				}
				else if("ACHPRCTIME".equalsIgnoreCase(param.get("sidx"))){
					//20160130 edit by hugo req by SRS_20160122
//					sql.append(" ORDER BY DECIMAL(COALESCE((SELECT SUM(CASE TXN_TYPE WHEN '4' THEN (DATE_DIFF(DT_REQ_2,DT_REQ_1) + DATE_DIFF(DT_REQ_3,DT_RSP_1) + DATE_DIFF(DT_RSP_2,DT_RSP_3)) / CAST (1000000 AS BIGINT) ELSE TIME_3 END) / CAST(COUNT(*) AS BIGINT) FROM TEMP WHERE HOURLAP = A.HOURLAP AND RC2 <> '0601'),0),18,2) "+param.get("sord"));
					sql.append(" ORDER BY DECIMAL(COALESCE((SELECT SUM(CASE  WHEN TXN_TYPE IN ('4','6') THEN (DATE_DIFF(DT_REQ_2,DT_REQ_1) + DATE_DIFF(DT_REQ_3,DT_RSP_1) + DATE_DIFF(DT_RSP_2,DT_RSP_3)) / CAST (1000000 AS BIGINT) ELSE TIME_3 END) / CAST(COUNT(*) AS BIGINT) FROM TEMP WHERE HOURLAP = A.HOURLAP AND RC2 <> '0601'),0),18,2) "+param.get("sord"));
				}
				else{
					sql.append(" ORDER BY "+param.get("sidx")+" "+param.get("sord"));
				}
			}else{
				sql.append(" ORDER BY A.HOURLAP ");
			}
			sql.append(") AS ROWNUMBER, A.*, ");
			sql.append("    COALESCE(B.TOTALCOUNT,0) AS TOTALCOUNT, COALESCE(B.OKCOUNT,0) AS OKCOUNT, ");
			sql.append("    COALESCE(B.FAILCOUNT,0) AS FAILCOUNT, COALESCE(B.PENDCOUNT,0) AS PENDCOUNT, ");
			sql.append("    DECIMAL(COALESCE((SELECT SUM(TIME_1) / CAST(COUNT(*) AS BIGINT) FROM TEMP WHERE HOURLAP = A.HOURLAP AND RC2 <> '0601'),0),18,2) PRCTIME, ");
			sql.append("    DECIMAL(COALESCE((SELECT SUM(TIME_2) / CAST(COUNT(*) AS BIGINT) FROM TEMP WHERE HOURLAP = A.HOURLAP AND TXN_TYPE = '2' AND RC2 <> '0601'),0),18,2) + ");
			//20160130 edit by hugo req by SRS_20160122
//			sql.append("    DECIMAL(COALESCE((SELECT SUM((DOUBLE(DATE_DIFF(DT_RSP_3, DT_REQ_3)) / CAST (1000000 AS BIGINT))) / CAST(COUNT(*) AS BIGINT) FROM TEMP WHERE HOURLAP = A.HOURLAP AND TXN_TYPE = '4' AND DT_REQ_3 <> 0 AND RC2 <> '0601'),0),18,2) SAVETIME, ");
//			sql.append("    DECIMAL(COALESCE((SELECT SUM(TIME_2) / CAST(COUNT(*) AS BIGINT) FROM TEMP WHERE HOURLAP = A.HOURLAP AND TXN_TYPE IN ('1','3','4') AND RC2 <> '0601'),0),18,2) DEBITTIME, ");
			sql.append("    DECIMAL(COALESCE((SELECT SUM((DOUBLE(DATE_DIFF(DT_RSP_3, DT_REQ_3)) / CAST (1000000 AS BIGINT))) / CAST(COUNT(*) AS BIGINT) FROM TEMP WHERE HOURLAP = A.HOURLAP AND TXN_TYPE IN ('4','6') AND DT_REQ_3 <> 0 AND RC2 <> '0601'),0),18,2) SAVETIME, ");
			sql.append("    DECIMAL(COALESCE((SELECT SUM(TIME_2) / CAST(COUNT(*) AS BIGINT) FROM TEMP WHERE HOURLAP = A.HOURLAP AND TXN_TYPE IN ('1','3','4' ,'5','6') AND RC2 <> '0601'),0),18,2) DEBITTIME, ");
//			sql.append("    DECIMAL(COALESCE((SELECT SUM(CASE TXN_TYPE WHEN '4' THEN (DATE_DIFF(DT_REQ_2,DT_REQ_1) + DATE_DIFF(DT_REQ_3,DT_RSP_1) + DATE_DIFF(DT_RSP_2,DT_RSP_3)) / CAST (1000000 AS BIGINT) ELSE TIME_3 END) / CAST(COUNT(*) AS BIGINT) FROM TEMP WHERE HOURLAP = A.HOURLAP AND RC2 <> '0601'),0),18,2) ACHPRCTIME ");
			sql.append("    DECIMAL(COALESCE((SELECT SUM(CASE  WHEN TXN_TYPE IN ('4','6') THEN (DATE_DIFF(DT_REQ_2,DT_REQ_1) + DATE_DIFF(DT_REQ_3,DT_RSP_1) + DATE_DIFF(DT_RSP_2,DT_RSP_3)) / CAST (1000000 AS BIGINT) ELSE TIME_3 END) / CAST(COUNT(*) AS BIGINT) FROM TEMP WHERE HOURLAP = A.HOURLAP AND RC2 <> '0601'),0),18,2) ACHPRCTIME ");
			sql.append("    FROM HOURITEM AS A LEFT JOIN ( ");
			sql.append("        SELECT ");
			sql.append("        HOURLAP, ");
			sql.append("        SUM(OKCOUNT + FAILCOUNT + PENDCOUNT) AS TOTALCOUNT, SUM(OKCOUNT) AS OKCOUNT, ");
			sql.append("        SUM(FAILCOUNT) AS FAILCOUNT, SUM(PENDCOUNT) AS PENDCOUNT ");
			sql.append("        FROM TEMP ");
			sql.append("        GROUP BY HOURLAP ");
			sql.append("    ) AS B ON A.HOURLAP = B.HOURLAP ");
			sql.append("    WHERE A.HOURLAP IS NOT NULL ");
			
			/*
			sql.append(withTemp);
			sql.append("SELECT * FROM ( SELECT ROWNUMBER() OVER( ");
			if(StrUtils.isNotEmpty(param.get("sidx"))){
				if("TOTALCOUNT".equalsIgnoreCase(param.get("sidx"))){
					sql.append(" ORDER BY (select count(*) from temp Where HOURLAP=a.HOURLAP) "+param.get("sord"));
				}
				else if("OKCOUNT".equalsIgnoreCase(param.get("sidx"))){
					sql.append(" ORDER BY (select count(*) from temp where HOURLAP=a.HOURLAP and resultstatus='A' ) "+param.get("sord"));
				}
				else if("FAILCOUNT".equalsIgnoreCase(param.get("sidx"))){
					sql.append(" ORDER BY (select count(*) from temp where HOURLAP=a.HOURLAP and resultstatus='R' ) "+param.get("sord"));
				}
				else if("PENDCOUNT".equalsIgnoreCase(param.get("sidx"))){
					sql.append(" ORDER BY (select count(*) from temp where HOURLAP=a.HOURLAP and resultstatus='P' ) "+param.get("sord"));
				}
				else if("PRCTIME".equalsIgnoreCase(param.get("sidx"))){
//					sql.append(" ORDER BY (select SUM(Double(Date_Diff(EndTime,DT_Req_1))) / (1000000*count(*)) from temp where HOURLAP=a.HOURLAP and resultstatus ='A' ) "+param.get("sord"));
					sql.append(" ORDER BY (select SUM(Double(Date_Diff(EndTime,DT_Req_1))) / ( cast (1000000  AS BIGINT) * cast ( count(*) AS BIGINT)) from temp where HOURLAP=a.HOURLAP and resultstatus ='A' ) "+param.get("sord"));
				}
				else if("SAVETIME".equalsIgnoreCase(param.get("sidx"))){
//					sql.append(" ORDER BY (select SUM(Double(Date_Diff(DT_Con_1,DT_Rsp_2))) / (1000000*count(*)) from temp where HOURLAP=a.HOURLAP and resultstatus ='A' ) "+param.get("sord"));
					sql.append(" ORDER BY (select SUM(Double(Date_Diff(DT_Con_1,DT_Rsp_2))) / ( cast (1000000  AS BIGINT) * cast ( count(*) AS BIGINT)) from temp where HOURLAP=a.HOURLAP and resultstatus ='A' ) "+param.get("sord"));
				}
				else if("DEBITTIME".equalsIgnoreCase(param.get("sidx"))){
//					sql.append(" ORDER BY (select SUM(Double(Date_Diff(DT_Rsp_1,DT_Req_2))) / (1000000*count(*)) from temp where HOURLAP=a.HOURLAP and resultstatus ='A' ) "+param.get("sord"));
					sql.append(" ORDER BY (select SUM(Double(Date_Diff(DT_Rsp_1,DT_Req_2))) / ( cast (1000000  AS BIGINT) * cast ( count(*) AS BIGINT)) from temp where HOURLAP=a.HOURLAP and resultstatus ='A' ) "+param.get("sord"));
				}
				else if("ACHPRCTIME".equalsIgnoreCase(param.get("sidx"))){
//					sql.append(" ORDER BY (select (SUM(Double(Date_Diff(DT_Req_2,DT_Req_1)))+SUM(Double(Date_Diff(DT_Rsp_2,DT_Rsp_1)))+SUM(Double(Date_Diff(DT_Con_2,DT_Con_1)))) / (1000000*Count(*)) from temp where HOURLAP=a.HOURLAP  and resultstatus ='A') "+param.get("sord"));
					sql.append(" ORDER BY (select (SUM(Double(Date_Diff(DT_Req_2,DT_Req_1)))+SUM(Double(Date_Diff(DT_Rsp_2,DT_Rsp_1)))+SUM(Double(Date_Diff(DT_Con_2,DT_Con_1)))) / ( cast (1000000  AS BIGINT) * cast ( count(*) AS BIGINT)) from temp where HOURLAP=a.HOURLAP  and resultstatus ='A') "+param.get("sord"));
				}
				else{
					sql.append(" ORDER BY "+param.get("sidx")+" "+param.get("sord"));
				}
			}
			sql.append(") AS ROWNUMBER, A.HOURLAP,A.HOURLAPNAME,(select count(*) from temp Where HOURLAP=a.HOURLAP) TotalCount ");
			sql.append(",(select count(*) from temp where HOURLAP=a.HOURLAP and resultstatus='A' ) OKCOUNT ");
			sql.append(",(select count(*) from temp where HOURLAP=a.HOURLAP and resultstatus='R' ) FailCOUNT ");
			sql.append(",(select count(*) from temp where HOURLAP=a.HOURLAP and resultstatus='P' ) PendCOUNT ");
			sql.append(",(select SUM(Double(Date_Diff(EndTime,DT_Req_1))) / ( cast (1000000  AS BIGINT) * cast ( count(*) AS BIGINT)) from temp where HOURLAP=a.HOURLAP and resultstatus ='A' ) PrcTime ");
			sql.append(",(select SUM(Double(Date_Diff(DT_Rsp_1,DT_Req_2))) / ( cast (1000000  AS BIGINT) * cast ( count(*) AS BIGINT)) from temp where HOURLAP=a.HOURLAP and resultstatus ='A' ) DebitTime ");
			sql.append(",(select SUM(Double(Date_Diff(DT_Con_1,DT_Rsp_2))) / ( cast (1000000  AS BIGINT) * cast ( count(*) AS BIGINT)) from temp where HOURLAP=a.HOURLAP and resultstatus ='A' ) SaveTime ");
			sql.append(",(select (SUM(Double(Date_Diff(DT_Req_2,DT_Req_1)))+SUM(Double(Date_Diff(DT_Rsp_2,DT_Rsp_1)))+SUM(Double(Date_Diff(DT_Con_2,DT_Con_1)))) / ( cast (1000000  AS BIGINT) * cast ( count(*) AS BIGINT)) from temp where HOURLAP=a.HOURLAP  and resultstatus ='A') ACHPrcTime ");
//			sql.append(",(select SUM(Double(Date_Diff(EndTime,DT_Req_1))) / (1000000*count(*)) from temp where HOURLAP=a.HOURLAP and resultstatus ='A' ) PrcTime ");
//			sql.append(",(select SUM(Double(Date_Diff(DT_Rsp_1,DT_Req_2))) / (1000000*count(*)) from temp where HOURLAP=a.HOURLAP and resultstatus ='A' ) DebitTime ");
//			sql.append(",(select SUM(Double(Date_Diff(DT_Con_1,DT_Rsp_2))) / (1000000*count(*)) from temp where HOURLAP=a.HOURLAP and resultstatus ='A' ) SaveTime ");
//			sql.append(",(select (SUM(Double(Date_Diff(DT_Req_2,DT_Req_1)))+SUM(Double(Date_Diff(DT_Rsp_2,DT_Rsp_1)))+SUM(Double(Date_Diff(DT_Con_2,DT_Con_1)))) / (1000000*Count(*)) from temp where HOURLAP=a.HOURLAP  and resultstatus ='A') ACHPrcTime ");
			sql.append("FROM HOURITEM AS A ");
			*/
			System.out.println("sql = " + sql.toString().toUpperCase());
			
			countQuery.append(withTemp);
			countQuery.append("SELECT A.* FROM HOURITEM AS A ");
			countQuery.append("WHERE A.HOURLAP IS NOT NULL ");
			/*
			countQuery.append("SELECT ");
			countQuery.append("A.HOURLAP,A.HOURLAPNAME,(select count(*) from temp Where HOURLAP=a.HOURLAP) TotalCount ");
			countQuery.append(",(select count(*) from temp where HOURLAP=a.HOURLAP and resultstatus='A' ) OKCOUNT ");
			countQuery.append(",(select count(*) from temp where HOURLAP=a.HOURLAP and resultstatus='R' ) FailCOUNT ");
			countQuery.append(",(select count(*) from temp where HOURLAP=a.HOURLAP and resultstatus='P' ) PendCOUNT ");
			countQuery.append(",(select SUM(Double(Date_Diff(EndTime,DT_Req_1))) / ( cast (1000000  AS BIGINT) * cast ( count(*) AS BIGINT)) from temp where HOURLAP=a.HOURLAP and resultstatus ='A' ) PrcTime ");
			countQuery.append(",(select SUM(Double(Date_Diff(DT_Rsp_1,DT_Req_2))) / ( cast (1000000  AS BIGINT) * cast ( count(*) AS BIGINT)) from temp where HOURLAP=a.HOURLAP and resultstatus ='A' ) DebitTime ");
			countQuery.append(",(select SUM(Double(Date_Diff(DT_Con_1,DT_Rsp_2))) / ( cast (1000000  AS BIGINT) * cast ( count(*) AS BIGINT)) from temp where HOURLAP=a.HOURLAP and resultstatus ='A' ) SaveTime ");
			countQuery.append(",(select (SUM(Double(Date_Diff(DT_Req_2,DT_Req_1)))+SUM(Double(Date_Diff(DT_Rsp_2,DT_Rsp_1)))+SUM(Double(Date_Diff(DT_Con_2,DT_Con_1)))) / ( cast (1000000  AS BIGINT) * cast ( count(*) AS BIGINT)) from temp where HOURLAP=a.HOURLAP  and resultstatus ='A') ACHPrcTime ");
//			countQuery.append(",(select SUM(Double(Date_Diff(EndTime,DT_Req_1))) / (1000000*count(*)) from temp where HOURLAP=a.HOURLAP and resultstatus ='A' ) PrcTime ");
//			countQuery.append(",(select SUM(Double(Date_Diff(DT_Rsp_1,DT_Req_2))) / (1000000*count(*)) from temp where HOURLAP=a.HOURLAP and resultstatus ='A' ) DebitTime ");
//			countQuery.append(",(select SUM(Double(Date_Diff(DT_Con_1,DT_Rsp_2))) / (1000000*count(*)) from temp where HOURLAP=a.HOURLAP and resultstatus ='A' ) SaveTime ");
//			countQuery.append(",(select (SUM(Double(Date_Diff(DT_Req_2,DT_Req_1)))+SUM(Double(Date_Diff(DT_Rsp_2,DT_Rsp_1)))+SUM(Double(Date_Diff(DT_Con_2,DT_Con_1)))) / (1000000*Count(*)) from temp where HOURLAP=a.HOURLAP  and resultstatus ='A') ACHPrcTime ");
			countQuery.append("FROM HOURITEM AS A ");
			*/
			System.out.println("countQuery = " + countQuery.toString().toUpperCase());
			
			page = onblocktab_Dao.getData(Integer.valueOf(pageNo), Integer.valueOf(pageSize), countQuery.toString(), sql.toString(), cols, HR_TXP_TIME.class);
			list = (List<HR_TXP_TIME>) page.getResult();
			System.out.println("list>>"+list);
			list = list!=null&& list.size() ==0 ?null:list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(page == null){
			rtnMap.put("total", "0");
			rtnMap.put("page", "0");
			rtnMap.put("records", "0");
			rtnMap.put("rows", new ArrayList());
		}else{
			rtnMap.put("total", page.getTotalPageCount());
			rtnMap.put("page", String.valueOf(page.getCurrentPageNo()));
			rtnMap.put("records", page.getTotalCount());
			rtnMap.put("rows", list);
		}
		
		String json = JSONUtils.map2json(rtnMap) ;
		return json;
	}
	
	public List<String> getConditionList(Map<String, String> param){
		List<String> conditionList = new ArrayList<String>();
		
		List<String> conditions = new ArrayList<String>();
		
		String txDate = "";
		if(StrUtils.isNotEmpty(param.get("TXDATE").trim())){
			txDate = param.get("TXDATE").trim();
			//20151022 HUANGPU 改用交易日期查詢較合邏輯
			//conditions.add(" A.BIZDATE = '" + DateTimeUtils.convertDate(txDate, "yyyyMMdd", "yyyyMMdd") + "' ");
			conditions.add(" A.TXDATE = '" + DateTimeUtils.convertDate(txDate, "yyyyMMdd", "yyyyMMdd") + "' ");
		}
		
		String pcode = "";
		if(StrUtils.isNotEmpty(param.get("PCODE").trim()) && !param.get("PCODE").trim().equals("all")){
			pcode = param.get("PCODE").trim();
			conditions.add(" A.PCODE = '" + pcode + "' ");
		}
		
		String opbkId = "";
		if(StrUtils.isNotEmpty(param.get("OPBK_ID").trim()) && !param.get("OPBK_ID").trim().equals("all")){
			opbkId = param.get("OPBK_ID").trim();
			conditions.add(" (A.SENDERACQUIRE = '" + opbkId + "' OR A.OUTACQUIRE = '" + opbkId + "' OR A.INACQUIRE = '" + opbkId + "') ");
		}
		
		String bgbkId = "";
		if(StrUtils.isNotEmpty(param.get("BGBK_ID").trim()) && !param.get("BGBK_ID").trim().equals("all")){
			bgbkId = param.get("BGBK_ID").trim();
			conditions.add(" (A.SENDERHEAD = '" + bgbkId + "' OR A.OUTHEAD = '" + bgbkId + "' OR A.INHEAD = '" + bgbkId + "') ");
		}
		
		String clearingPhase = "";
		if(StrUtils.isNotEmpty(param.get("CLEARINGPHASE").trim()) && !param.get("CLEARINGPHASE").trim().equals("all")){
			clearingPhase = param.get("CLEARINGPHASE").trim();
			conditions.add(" A.CLEARINGPHASE = '" + clearingPhase + "' ");
		}
		
		conditionList.add( combine(conditions) );
		return conditionList;
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
	
	public ONBLOCKTAB_Dao getOnblocktab_Dao() {
		return onblocktab_Dao;
	}
	public void setOnblocktab_Dao(ONBLOCKTAB_Dao onblocktab_Dao) {
		this.onblocktab_Dao = onblocktab_Dao;
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

	public RPONBLOCKTAB_Dao getRponblocktab_Dao() {
		return rponblocktab_Dao;
	}

	public void setRponblocktab_Dao(RPONBLOCKTAB_Dao rponblocktab_Dao) {
		this.rponblocktab_Dao = rponblocktab_Dao;
	}
	
}
