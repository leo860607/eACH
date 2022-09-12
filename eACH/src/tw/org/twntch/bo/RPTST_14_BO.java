package tw.org.twntch.bo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.struts.util.LabelValueBean;

import tw.org.twntch.db.dao.hibernate.BANK_GROUP_Dao;
import tw.org.twntch.db.dao.hibernate.RPONBLOCKTAB_Dao;
import tw.org.twntch.po.BANK_GROUP;
import tw.org.twntch.po.BANK_OPBK;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.RptUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.zDateHandler;

public class RPTST_14_BO {
	private RPONBLOCKTAB_Dao rponblocktab_Dao;
	private BANK_GROUP_Dao bank_group_Dao;
	private BANK_OPBK_BO bank_opbk_bo;
	
	public List<LabelValueBean> getOpbkIdList(){
		List<BANK_OPBK> list = bank_opbk_bo.getAllOpbkList();
		List<LabelValueBean> beanList = new LinkedList<LabelValueBean>();
		LabelValueBean bean = null;
		for(BANK_OPBK po : list){
			bean = new LabelValueBean(po.getOPBK_ID() + " - " + po.getOPBK_NAME(), po.getOPBK_ID());
			beanList.add(bean);
		}
		return beanList;
	}
	
	public Map<String, String> export(String ext, String twYear, String startTwMonth, String endTwMonth, String opbkId, String serchStrs){
		Map<String, String> rtnMap = null;
		String outputFilePath = "";
		try{
			rtnMap = new HashMap<String, String>();
			rtnMap.put("serchStrs", serchStrs);
			Map<String, Object> params = new HashMap<String, Object>();
			//顯示區塊
			if(startTwMonth.equals(endTwMonth)){
				params.put("V_TXDT", "上傳營業月份：" + DateTimeUtils.convertDate(DateTimeUtils.NOT_INTERCONVERSION, (twYear + startTwMonth), "yyyymm", "yyy/mm"));
			}else{
				params.put("V_TXDT", "上傳營業月份區間：" + DateTimeUtils.convertDate(DateTimeUtils.NOT_INTERCONVERSION, (twYear + startTwMonth), "yyyymm", "yyy/mm") + "~" + DateTimeUtils.convertDate(DateTimeUtils.NOT_INTERCONVERSION, (twYear + endTwMonth), "yyyymm", "yyy/mm"));
			}
			params.put("V_PRINT_DATE",DateTimeUtils.convertDate(DateTimeUtils.INTERCONVERSION, zDateHandler.getTheDate(), "yyyy/MM/dd", "yyy/MM/dd"));
			params.put("V_PRINT_TIME",zDateHandler.getTheTime());
			//System.out.println("params >> " + params);
			
			String sql = getSQL(twYear, startTwMonth, endTwMonth, opbkId);
			List list = rponblocktab_Dao.getRptData(sql, new ArrayList<String>());
			int pathType = Arguments.getStringArg("ISWINOPEN").equals("Y")?RptUtils.C_PATH :RptUtils.R_PATH;
			outputFilePath = RptUtils.export(RptUtils.COLLECTION,pathType, "st_14", "st_14", params, list);
			
			System.out.println("RPTST_14_BO.sql >> " + sql);
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
	
	public String getSQL(String twYear, String startTwMonth, String endTwMonth, String opbkId){
		SimpleDateFormat sdf_1 = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat sdf_2 = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		
		String startDate_1 = "";
		String startDate_2 = "";
		String endDate_1 = "";
		String endDate_2 = "";
		
		try{
			if(StrUtils.isEmpty(twYear) || StrUtils.isEmpty(startTwMonth)){
				startDate_1 = DateTimeUtils.convertDate(zDateHandler.getTWDate().substring(0, 6) + "01", "yyyyMMdd", "yyyyMMdd");
				startDate_2 = DateTimeUtils.convertDate(zDateHandler.getTWDate().substring(0, 6) + "01", "yyyyMMdd", "yyyy-MM-dd");
			}else{
				startDate_1 = DateTimeUtils.convertDate((twYear + startTwMonth + "01"), "yyyyMMdd", "yyyyMMdd");
				startDate_2 = DateTimeUtils.convertDate((twYear + startTwMonth + "01"), "yyyyMMdd", "yyyy-MM-dd");
			}
			
			if(StrUtils.isEmpty(twYear) || StrUtils.isEmpty(endTwMonth)){
				cal.setTime(sdf_1.parse(startDate_1));
				cal.add(Calendar.MONTH, 1);
			}else{
				cal.setTime(sdf_1.parse(DateTimeUtils.convertDate((twYear + endTwMonth + "01"), "yyyyMMdd", "yyyyMMdd")));
				cal.add(Calendar.MONTH, 1);
			}
			endDate_1 = sdf_1.format(cal.getTime());
			endDate_2 = sdf_2.format(cal.getTime());
		}catch(Exception e){
			e.printStackTrace();
		}
		
		StringBuffer sql = new StringBuffer();
		sql.append("WITH CTE_MONTHS (TMS, TMS_STR) AS ( ");
		sql.append("    SELECT ");
		sql.append("    TIMESTAMP(DATE('" + startDate_2 + "'), TIME('00:00:00')) AS TMS, ");
		sql.append("    VARCHAR_FORMAT(DATE('" + startDate_2 + "'), 'YYYYMMDD') TMS_STR ");
		sql.append("    FROM SYSIBM.SYSDUMMY1 ");
		sql.append("    UNION ALL ");
		sql.append("    SELECT TMS + 1 DAY, VARCHAR_FORMAT(TMS + 1 DAY, 'YYYYMMDD') ");
		sql.append("    FROM CTE_MONTHS WHERE TMS + 1 DAY < DATE('" + endDate_2 + "') ");
		sql.append("), SRC AS ( ");
		sql.append("    SELECT SENDERACQUIRE AS ACQUIREID, FLBIZDATE ");
		sql.append("    , (CASE NEWRESULT WHEN 'A' THEN 1 ELSE 0 END) AS AC ");
		sql.append("    , (CASE NEWRESULT WHEN 'R' THEN 1 ELSE 0 END) AS RC ");
		sql.append("    , (CASE NEWRESULT WHEN 'A' THEN TXAMT ELSE 0 END) AS AM ");
		sql.append("    , (CASE NEWRESULT WHEN 'R' THEN TXAMT ELSE 0 END) AS RM ");
		sql.append("    FROM RPONBLOCKTAB ");
		sql.append("    WHERE FLBIZDATE >= '" + startDate_1 + "' AND FLBIZDATE < '" + endDate_1 + "' ");
		sql.append("    " + (StrUtils.isEmpty(opbkId)?"":opbkId.equalsIgnoreCase("all") ? "" : "AND SENDERACQUIRE = '" + opbkId + "' "));
		sql.append("), TEMP AS ( ");
		sql.append("    SELECT A.ACQUIREID, A.BIZDATE ");
		sql.append("    , (SELECT COUNT(DISTINCT BATCHSEQ) FROM FLCONTROLTAB WHERE BIZDATE = A.BIZDATE AND ACQUIREID = A.ACQUIREID) AS FILEUPLOADCOUNT ");
		sql.append("    , (SELECT SUM(CASE COALESCE(FILEREJECT,'') WHEN 'R' THEN 1 ELSE 0 END) FROM FLCONTROLTAB WHERE BIZDATE = A.BIZDATE AND ACQUIREID = A.ACQUIREID) AS FILEREJECTCOUNT ");
		sql.append("    , SUM(COALESCE(A.TOTALCOUNT,0)) AS TOTALCOUNT ");
		sql.append("    , SUM(COALESCE(A.TOTALAMT,0)) AS TOTALAMT ");
		sql.append("    , SUM(COALESCE(A.REJECTCOUNT,0)) AS REJECTCOUNT ");
		sql.append("    , SUM(COALESCE(A.REJECTAMT,0)) AS REJECTAMT ");
		sql.append("    , SUM(COALESCE(A.ACCEPTCOUNT,0)) AS ACCEPTCOUNT ");
		sql.append("    , SUM(COALESCE(A.ACCEPTAMT,0)) AS ACCEPTAMT ");
		sql.append("    , (SELECT SUM(AC) FROM SRC WHERE ACQUIREID = A.ACQUIREID AND FLBIZDATE = A.BIZDATE) AS COUNT_A ");
		sql.append("    , (SELECT SUM(AM) FROM SRC WHERE ACQUIREID = A.ACQUIREID AND FLBIZDATE = A.BIZDATE) AS AMT_A ");
		sql.append("    , (SELECT SUM(RC) FROM SRC WHERE ACQUIREID = A.ACQUIREID AND FLBIZDATE = A.BIZDATE) AS COUNT_R ");
		sql.append("    , (SELECT SUM(RM) FROM SRC WHERE ACQUIREID = A.ACQUIREID AND FLBIZDATE = A.BIZDATE) AS AMT_R ");
		sql.append("    FROM FLCONTROLTAB AS A ");
		sql.append("    WHERE A.ACQUIREID IS NOT NULL ");
		sql.append("    AND A.BIZDATE >= '" + startDate_1 + "' AND A.BIZDATE < '" + endDate_1 + "' ");
		sql.append("    " + (StrUtils.isEmpty(opbkId)?"":opbkId.equalsIgnoreCase("all") ? "" : "AND A.ACQUIREID = '" + opbkId + "' "));
		sql.append("    GROUP BY A.ACQUIREID, A.BIZDATE ");
		sql.append("), TEMP2 AS ( ");
		sql.append("    SELECT * FROM ( ");
		sql.append("        SELECT ACQUIREID FROM TEMP GROUP BY ACQUIREID ");
		sql.append("    ) CROSS JOIN CTE_MONTHS ");
		sql.append("    ORDER BY ACQUIREID ");
		sql.append(") ");

		sql.append("SELECT ");
		sql.append("COALESCE(A.ACQUIREID,'') AS ACQUIREID ");
		sql.append(", COALESCE((SELECT BGBK_NAME FROM BANK_GROUP WHERE BGBK_ID = A.ACQUIREID),'') AS ACQUIRE_NAME ");
		sql.append(", SUBSTR(TRANS_DATE(A.TMS_STR,'T','/'),2) AS BIZDATE ");
		sql.append(", COALESCE(B.FILEUPLOADCOUNT,0) AS FILEUPLOADCOUNT ");
		sql.append(", COALESCE(B.FILEREJECTCOUNT,0) AS FILEREJECTCOUNT ");
		sql.append(", COALESCE(B.TOTALCOUNT,0) AS TOTALCOUNT ");
		sql.append(", COALESCE(B.TOTALAMT,0) AS TOTALAMT ");
		sql.append(", COALESCE(B.REJECTCOUNT,0) AS REJECTCOUNT ");
		sql.append(", COALESCE(B.REJECTAMT,0) AS REJECTAMT ");
		sql.append(", COALESCE(B.ACCEPTCOUNT,0) AS ACCEPTCOUNT ");
		sql.append(", COALESCE(B.ACCEPTAMT,0) AS ACCEPTAMT ");
		sql.append(", COALESCE(B.COUNT_A,0) AS COUNT_A ");
		sql.append(", COALESCE(B.AMT_A,0) AS AMT_A ");
		sql.append(", COALESCE(B.COUNT_R,0) AS COUNT_R ");
		sql.append(", COALESCE(B.AMT_R,0) AS AMT_R ");
		sql.append("FROM TEMP2 AS A LEFT JOIN TEMP AS B ON A.ACQUIREID = B.ACQUIREID AND A.TMS_STR = B.BIZDATE ");
		sql.append("ORDER BY A.ACQUIREID, A.TMS_STR ");
		
		//System.out.println("getSQL >> " + sql);
		return sql.toString();
	}
	
	public List<LabelValueBean> getBgbkIdList(){
		List<BANK_GROUP> list = bank_group_Dao.getBgbkIdList_2();
		List<LabelValueBean> beanList = new LinkedList<LabelValueBean>();
		LabelValueBean bean = null;
		for(BANK_GROUP po : list){
			bean = new LabelValueBean(po.getBGBK_ID() + " - " + po.getBGBK_NAME(), po.getBGBK_ID());
			beanList.add(bean);
		}
		System.out.println("beanList>>"+beanList);
		return beanList;
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

	public BANK_OPBK_BO getBank_opbk_bo() {
		return bank_opbk_bo;
	}

	public void setBank_opbk_bo(BANK_OPBK_BO bank_opbk_bo) {
		this.bank_opbk_bo = bank_opbk_bo;
	}

}
