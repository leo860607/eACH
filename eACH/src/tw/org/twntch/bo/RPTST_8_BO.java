package tw.org.twntch.bo;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.struts.util.LabelValueBean;

import tw.org.twntch.db.dao.hibernate.BANK_GROUP_Dao;
import tw.org.twntch.db.dao.hibernate.EACH_TXN_CODE_Dao;
import tw.org.twntch.db.dao.hibernate.RPONBLOCKTAB_Dao;
import tw.org.twntch.po.BANK_GROUP;
import tw.org.twntch.po.BANK_OPBK;
import tw.org.twntch.po.EACH_TXN_CODE;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.RptUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.zDateHandler;

public class RPTST_8_BO {
	private RPONBLOCKTAB_Dao rponblocktab_Dao;
	private BANK_GROUP_Dao bank_group_Dao;
	private EACH_TXN_CODE_Dao each_txn_code_Dao;
	private BANK_OPBK_BO bank_opbk_bo;
	
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
	
	public List<LabelValueBean> getBgbkIdList(){
		List<BANK_GROUP> list = bank_group_Dao.getBgbkIdList();
		List<LabelValueBean> beanList = new LinkedList<LabelValueBean>();
		LabelValueBean bean = null;
		for(BANK_GROUP po : list){
			bean = new LabelValueBean(po.getBGBK_ID() + " - " + po.getBGBK_NAME(), po.getBGBK_ID());
			beanList.add(bean);
		}
		System.out.println("beanList>>"+beanList);
		return beanList;
	}
	
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
	
	public Map<String, String> ex_export(String ext, String txdate, String pcode, String opbkId, String bgbkId, String clearingPhase, String serchStrs){
		return export(ext, txdate, pcode, opbkId, bgbkId, clearingPhase, serchStrs);
	}

	public Map<String, String> export(String ext, String txdate, String pcode, String opbkId, String bgbkId, String clearingPhase, String serchStrs){
		Map<String, String> rtnMap = null;
		
		try{
			rtnMap = new HashMap<String, String>();
			rtnMap.put("serchStrs", serchStrs);
			Map<String, Object> params = new HashMap<String, Object>();
			//「營業月份區間」為必填
			params.put("V_TXDT", DateTimeUtils.convertDate(DateTimeUtils.NOT_INTERCONVERSION, txdate, "yyyyMMdd", "yyy/MM/dd"));
			params.put("V_PCODE", pcode.equals("all")?"全部":pcode + "-" + each_txn_code_Dao.getByPK(pcode).get(0).getEACH_TXN_NAME());
			params.put("V_CLEARINGPHASE", clearingPhase.equals("all")?"全部":clearingPhase);
			params.put("V_OPT_BANK", opbkId.equals("all")?"全部":opbkId);
			params.put("V_BG_BANK", bgbkId.equals("all")?"全部":bgbkId);
			params.put("V_PRINT_DATE", DateTimeUtils.convertDate(DateTimeUtils.INTERCONVERSION, zDateHandler.getTheDate(), "yyyy/MM/dd", "yyy/MM/dd"));
			//System.out.println("params >> " + params);
			
			Map queryParam = this.getConditionData(txdate, pcode, opbkId, bgbkId, clearingPhase);
			//System.out.println("queryParam >> " + queryParam);
			
			//List list = rponblocktab_Dao.getSt_1_Detail_Data_ForRpt(queryParam.get("sqlPath").toString(), (List<String>) queryParam.get("values"));
			List list = rponblocktab_Dao.getRptData(getSQL((String)queryParam.get("sqlPath")), (List<String>)queryParam.get("values"));
			int pathType = Arguments.getStringArg("ISWINOPEN").equals("Y")?RptUtils.C_PATH :RptUtils.R_PATH;
			String outputFilePath = "";
			if(ext.equals("xls")){
				outputFilePath = RptUtils.export(RptUtils.COLLECTION,pathType, "ex_st_8", "st_8", params, list, 2);
			}else if(ext.equals("pdf")){
				outputFilePath = RptUtils.export(RptUtils.COLLECTION,pathType, "st_8", "st_8", params, list);
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
	
	public Map getConditionData(String txdate, String pcode, String opbkId, String bgbkId, String clearingPhase) throws Exception{
		StringBuffer sql = new StringBuffer();
		Map<String, String> params = new LinkedHashMap<String, String> ();
		Map retmap = new LinkedHashMap();
		List<String> values = new LinkedList<String>(); 
		try {
			//20151022 HUANGPU 改用交易日期查較合邏輯
			//params.put("A.BIZDATE", txdate);
			params.put("A.TXDATE", txdate);
			params.put("A.PCODE", pcode);
			params.put("OPBK_ID", opbkId);
			params.put("BGBK_ID", bgbkId);
			params.put("A.CLEARINGPHASE", clearingPhase);
			
			int i = 0;
			
			for(String key : params.keySet()){
				if(StrUtils.isNotEmpty(params.get(key)) && !params.get(key).equalsIgnoreCase("all")){
					
					if(key.equals("A.TXDATE")){
						sql.append( key + " = ? ");
						values.add(DateTimeUtils.convertDate(txdate, "yyyyMMdd", "yyyyMMdd"));
					}
					else if(key.equals("OPBK_ID")){
						sql.append(" AND (A.SENDERACQUIRE = ? OR A.OUTACQUIRE = ? OR A.INACQUIRE = ?) ");
						values.add(opbkId);
						values.add(opbkId);
						values.add(opbkId);
					}
					else if(key.equals("BGBK_ID")){
						sql.append(" AND (A.SENDERHEAD = ? OR A.OUTHEAD = ? OR A.INHEAD = ?) ");
						values.add(bgbkId);
						values.add(bgbkId);
						values.add(bgbkId);
					}
					else{
						sql.append(" AND ");
						sql.append( key + " = ? ");
						values.add(params.get(key));
					}
					i++;
				}
			}
			
			retmap.put("sqlPath", sql.toString());
			retmap.put("values", values);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e.toString()) ; 
		}
		return retmap;
	}
	
	public String getSQL(String condition){
		StringBuffer sql = new StringBuffer();
		
		sql.append("WITH TEMP AS ( ");
		sql.append("    SELECT ");
		sql.append("    DOUBLE(DATE_DIFF(ENDTIME, DT_REQ_1)) / CAST (1000000 AS BIGINT) AS TIME_1, ");
		sql.append("    DOUBLE(DATE_DIFF(DT_RSP_1, DT_REQ_2)) / CAST (1000000 AS BIGINT) AS TIME_2, ");
		sql.append("    (DOUBLE(DATE_DIFF(DT_REQ_2, DT_REQ_1)) + DOUBLE(DATE_DIFF(DT_RSP_2, DT_RSP_1))) / CAST (1000000 AS BIGINT) AS TIME_3, ");
		sql.append("    C.* ");
		sql.append("    FROM ( ");
		sql.append("        SELECT ");
		sql.append("        (CASE WHEN RC2 = '3001' THEN 1 ELSE 0 END) OKCOUNT, ");
		sql.append("        (CASE WHEN RC2 NOT IN ('3001','0601') THEN 1 ELSE 0 END) FAILCOUNT, ");
		sql.append("        (CASE WHEN RC2 = '0601' THEN 1 ELSE 0 END) PENDCOUNT, ");
		sql.append("        INTEGER(SUBSTR(A.TXDT,9,2)) AS HOURLAP, A.RESULTSTATUS, ");
		sql.append("        A.PCODE, SUBSTR(COALESCE(A.PCODE,'0000'),4,1) AS TXN_TYPE, A.RC2, A.SENDERSTATUS, ");
		sql.append("        COALESCE(A.DT_RSP_2, COALESCE(A.DT_RSP_1, COALESCE(A.DT_REQ_2, COALESCE(A.DT_REQ_1, 0)))) AS ENDTIME, ");
		sql.append("        (CASE WHEN LENGTH(COALESCE(A.DT_REQ_1,''))=0 THEN '0' ELSE A.DT_REQ_1 END ) DT_REQ_1, ");
		sql.append("        (CASE WHEN LENGTH(COALESCE(A.DT_REQ_2,''))=0 THEN '0' ELSE A.DT_REQ_2 END ) DT_REQ_2, ");
		sql.append("        (CASE WHEN LENGTH(COALESCE(A.DT_REQ_3,''))=0 THEN '0' ELSE A.DT_REQ_3 END ) DT_REQ_3, ");
		sql.append("        (CASE WHEN LENGTH(COALESCE(A.DT_RSP_1,''))=0 THEN '0' ELSE A.DT_RSP_1 END ) DT_RSP_1, ");
		sql.append("        (CASE WHEN LENGTH(COALESCE(A.DT_RSP_2,''))=0 THEN '0' ELSE A.DT_RSP_2 END ) DT_RSP_2, ");
		sql.append("        (CASE WHEN LENGTH(COALESCE(A.DT_RSP_3,''))=0 THEN '0' ELSE A.DT_RSP_3 END ) DT_RSP_3, ");
		sql.append("        A.CLEARINGPHASE, A.SENDERACQUIRE, A.OUTACQUIRE, A.INACQUIRE, A.SENDERHEAD, A.OUTHEAD, A.INHEAD ");
		sql.append("        FROM ONBLOCKTAB AS A ");
		sql.append("        WHERE ( A.RESULTSTATUS IN ('A', 'R') OR (A.RESULTSTATUS = 'P' AND A.SENDERSTATUS <> '1') ) AND COALESCE(DT_REQ_2,'') <> '' ");
		sql.append("        " + (StrUtils.isEmpty(condition)? "" : "AND " + condition) + " ");
		sql.append("    ) AS C ");
		sql.append("), TEMP_ AS ( ");
		sql.append("	SELECT HOURLAP, ");
		sql.append("	SUM(OKCOUNT + FAILCOUNT + PENDCOUNT) AS TOTALCOUNT, SUM(OKCOUNT) AS OKCOUNT, ");
		sql.append("	SUM(FAILCOUNT) AS FAILCOUNT, SUM(PENDCOUNT) AS PENDCOUNT ");
		sql.append("	FROM TEMP ");
		sql.append("	GROUP BY HOURLAP ");
		sql.append(") ");
		
		sql.append("SELECT A.*, ");
		sql.append("COALESCE((SELECT SUM(CASE WHEN (OKCOUNT + FAILCOUNT) > 0 THEN 1 ELSE 0 END) FROM TEMP_), 0) AS PERIOD_COUNT, ");
		sql.append("COALESCE(B.TOTALCOUNT,0) AS TOTALCOUNT, COALESCE(B.OKCOUNT,0) AS OKCOUNT, ");
		sql.append("COALESCE(B.FAILCOUNT,0) AS FAILCOUNT, COALESCE(B.PENDCOUNT,0) AS PENDCOUNT, ");
		sql.append("DECIMAL(COALESCE((SELECT SUM(TIME_1) / CAST(COUNT(*) AS BIGINT) FROM TEMP WHERE HOURLAP = A.HOURLAP AND RC2 <> '0601'),0),18,2) PRCTIME, ");
		sql.append("DECIMAL(COALESCE((SELECT SUM(TIME_2) / CAST(COUNT(*) AS BIGINT) FROM TEMP WHERE HOURLAP = A.HOURLAP AND TXN_TYPE = '2' AND RC2 <> '0601'),0),18,2) + ");
		//20160130 edit by hugo req by SRS_20160122
//		sql.append("DECIMAL(COALESCE((SELECT SUM((DOUBLE(DATE_DIFF(DT_RSP_3, DT_REQ_3)) / CAST (1000000 AS BIGINT))) / CAST(COUNT(*) AS BIGINT) FROM TEMP WHERE HOURLAP = A.HOURLAP AND TXN_TYPE = '4' AND DT_REQ_3 <> 0 AND RC2 <> '0601'),0),18,2) SAVETIME, ");
//		sql.append("DECIMAL(COALESCE((SELECT SUM(TIME_2) / CAST(COUNT(*) AS BIGINT) FROM TEMP WHERE HOURLAP = A.HOURLAP AND TXN_TYPE IN ('1','3','4') AND RC2 <> '0601'),0),18,2) DEBITTIME, ");
//		sql.append("DECIMAL(COALESCE((SELECT SUM(CASE TXN_TYPE WHEN '4' THEN (DATE_DIFF(DT_REQ_2,DT_REQ_1) + DATE_DIFF(DT_REQ_3,DT_RSP_1) + DATE_DIFF(DT_RSP_2,DT_RSP_3)) / CAST (1000000 AS BIGINT) ELSE TIME_3 END) / CAST(COUNT(*) AS BIGINT) FROM TEMP WHERE HOURLAP = A.HOURLAP AND RC2 <> '0601'),0),18,2) ACHPRCTIME ");
		sql.append("DECIMAL(COALESCE((SELECT SUM((DOUBLE(DATE_DIFF(DT_RSP_3, DT_REQ_3)) / CAST (1000000 AS BIGINT))) / CAST(COUNT(*) AS BIGINT) FROM TEMP WHERE HOURLAP = A.HOURLAP AND TXN_TYPE IN ('4','6') AND DT_REQ_3 <> 0 AND RC2 <> '0601'),0),18,2) SAVETIME, ");
		sql.append("DECIMAL(COALESCE((SELECT SUM(TIME_2) / CAST(COUNT(*) AS BIGINT) FROM TEMP WHERE HOURLAP = A.HOURLAP AND TXN_TYPE IN ('1','3','4','5','6') AND RC2 <> '0601'),0),18,2) DEBITTIME, ");
		sql.append("DECIMAL(COALESCE((SELECT SUM(CASE  WHEN TXN_TYPE IN ('4','6') THEN (DATE_DIFF(DT_REQ_2,DT_REQ_1) + DATE_DIFF(DT_REQ_3,DT_RSP_1) + DATE_DIFF(DT_RSP_2,DT_RSP_3)) / CAST (1000000 AS BIGINT) ELSE TIME_3 END) / CAST(COUNT(*) AS BIGINT) FROM TEMP WHERE HOURLAP = A.HOURLAP AND RC2 <> '0601'),0),18,2) ACHPRCTIME ");
		sql.append("FROM HOURITEM AS A LEFT JOIN TEMP_ AS B ON A.HOURLAP = B.HOURLAP ");
		sql.append("WHERE A.HOURLAP IS NOT NULL ");
		sql.append("ORDER BY A.HOURLAP ");
		/*
		sql.append("with temp1 as ( SELECT * FROM EACHUSER.RPMONTHSUMTAB WHERE " + (StrUtils.isEmpty(condition)?"":condition));
		sql.append(") ");
		sql.append("    ( ");
		sql.append("    SELECT A.BGBK_ID, B.BGBK_NAME, A.CNT,  DECIMAL(( 1.0 * A.CNT / (SELECT SUM(CNT) FROM temp1) ),10,5)   AS  CNT_2, A.AMT,  DECIMAL(( 1.0 * A.AMT / (SELECT SUM(AMT) FROM temp1) ),10,5)   AS  AMT_2 ");
		sql.append("        From (  ");
		sql.append("              SELECT VARCHAR(BGBK_ID) BGBK_ID, sum(CNT) CNT, sum(AMT) AMT FROM temp1 Group by BGBK_ID ");
		sql.append("            ) A  ");
		sql.append("LEFT JOIN ( ");
		sql.append("    SELECT DISTINCT  BGBK_ID, BGBK_NAME FROM temp1 WHERE BGBK_NAME IS NOT NULL ");
		sql.append("            ) B ON B.BGBK_ID=A.BGBK_ID ");
		sql.append("WHERE  ( 100.0 * A.CNT / (SELECT SUM(CNT) FROM temp1) ) > 1 ");
		sql.append("    ) ");
		sql.append("UNION ");
		sql.append("    ( ");
		sql.append("    SELECT '其他'  AS BGBK_ID, '其他'  AS  BGBK_NAME, SUM(A.CNT) AS CNT,SUM(  DECIMAL(( 1.0 * A.CNT / (SELECT SUM(CNT) FROM temp1) ),10,5)  )  AS  CNT_2,SUM( A.AMT)  AS  AMT,SUM(  DECIMAL(( 1.0 * A.AMT / (SELECT SUM(AMT) FROM temp1) ),10,5) )  AS  AMT_2 ");
		sql.append("        From (  ");
		sql.append("              SELECT VARCHAR(BGBK_ID) BGBK_ID, sum(CNT) CNT, sum(AMT) AMT FROM temp1 Group by BGBK_ID ");
		sql.append("            ) A  ");
		sql.append("WHERE  ( 100.0 * A.CNT / (SELECT SUM(CNT) FROM temp1) ) < 1 ");
		sql.append("    ) ");
		sql.append("Order by BGBK_ID ");
		*/
		//System.out.println("SQL >> " + sql.toString());
		return sql.toString();
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
	
}
