package tw.org.twntch.bo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;







import tw.org.twntch.bean.MONITOR_BIZ;
import tw.org.twntch.db.dao.hibernate.BANK_BRANCH_Dao;
import tw.org.twntch.db.dao.hibernate.EACHSYSSTATUSTAB_Dao;
import tw.org.twntch.db.dao.hibernate.ONBLOCKTAB_Dao;
import tw.org.twntch.po.EACHSYSSTATUSTAB;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.zDateHandler;

public class MONITOR_BIZ_BO {
	private ONBLOCKTAB_Dao onblocktab_Dao;
	private BANK_BRANCH_Dao bank_branch_Dao;
	private EACHSYSSTATUSTAB_BO eachsysstatustab_bo;
	private EACHSYSSTATUSTAB_Dao eachsysstatustab_Dao;
	
	public String getCondition_1(Map<String, String> param){
		String conditionStr_1 = "";
		List<String> conditions_1 = new ArrayList<String>();
		String userCompany = "";
		if(StrUtils.isNotEmpty(param.get("USER_COMPANY2").trim())){
			userCompany = param.get("USER_COMPANY2").trim();
			conditions_1.add(" (SENDERHEAD = '" + userCompany + "' OR OUTHEAD = '" + userCompany + "' OR INHEAD = '" + userCompany + "') ");
		}
		
		String clearingPhase = "";
		if(StrUtils.isNotEmpty(param.get("CLEARINGPHASE").trim())){
			clearingPhase = param.get("CLEARINGPHASE").trim();
			conditions_1.add(" CLEARINGPHASE = '" + clearingPhase + "' ");
		}
		
		String bizdate = "";
		if(StrUtils.isNotEmpty(param.get("BIZDATE").trim())){
			bizdate = param.get("BIZDATE").trim();
			if(bizdate.contains("-")){
				bizdate = DateTimeUtils.convertDate(DateTimeUtils.NOT_INTERCONVERSION, param.get("BIZDATE").trim(), "yyyy-MM-dd", "yyyyMMdd");
			}
		}else{
			bizdate = DateTimeUtils.convertDate(eachsysstatustab_bo.getBusinessDate(), "yyyyMMdd", "yyyyMMdd");
		}
		conditions_1.add(" BIZDATE = '" + bizdate + "' ");
		
		conditionStr_1 = combine(conditions_1);
		return conditionStr_1;
	}
	
	public String getCondition_2(Map<String, String> param){
		String conditionStr_2 = "";
		List<String> conditions_2 = new ArrayList<String>();
		
		String userCompany = "";
		if(StrUtils.isNotEmpty(param.get("USER_COMPANY2").trim())){
			userCompany = param.get("USER_COMPANY2").trim();
			conditions_2.add(" A.BANKID = '" + userCompany + "' ");
		}
		
		conditionStr_2 = combine(conditions_2);
		return conditionStr_2;
	}
	
	public String getWithTempSql(Map<String, String> param){
		StringBuffer withTemp = new StringBuffer();
		String conditionStr_1 = getCondition_1(param);
		withTemp.append("WITH TEMP AS ( ");
		withTemp.append("    SELECT SENDERACQUIRE, OUTACQUIRE, INACQUIRE, SENDERHEAD, OUTHEAD, INHEAD, RESULTSTATUS, TIMEOUTCODE, SENDERSTATUS, DT_REQ_1, ACCTCODE ");//ACCTCODE 20160728
		withTemp.append("    FROM VW_ONBLOCKTAB ");
		withTemp.append("    " + (StrUtils.isNotEmpty(conditionStr_1)? "WHERE " + conditionStr_1 + " with ur ": " with ur "));
		withTemp.append(") ");
		return withTemp.toString();
	}
	
	public String getFromAndWhere(Map<String, String> param){
		StringBuffer fromAndWhere = new StringBuffer();
		String conditionStr_2 = getCondition_2(param);
		fromAndWhere.append("FROM ( ");
		fromAndWhere.append("    SELECT BG.OPBK_ID AS BANKID, OP.BGBK_NAME AS BANKNAME ");
		fromAndWhere.append("    FROM BANK_GROUP BG JOIN BANK_GROUP OP ON BG.OPBK_ID = OP.BGBK_ID ");
		fromAndWhere.append("    WHERE  RTRIM(COALESCE(OP.ACTIVE_DATE,'')) <> '' ");
		//fromAndWhere.append("    AND '" + zDateHandler.getTWDate() + "' BETWEEN OP.ACTIVE_DATE AND (CASE RTRIM(COALESCE(OP.STOP_DATE,'')) WHEN '' THEN '20991231' ELSE OP.STOP_DATE END) ");
		fromAndWhere.append("    AND '" + DateTimeUtils.convertDate(param.get("BIZDATE"), "yyyy-MM-dd", "yyyyMMdd") + "' BETWEEN OP.ACTIVE_DATE AND (CASE RTRIM(COALESCE(OP.STOP_DATE,'')) WHEN '' THEN '01991231' ELSE OP.STOP_DATE END) ");
		fromAndWhere.append("    AND BG.BGBK_ATTR <> '6' ");
		fromAndWhere.append("    GROUP BY BG.OPBK_ID, OP.BGBK_NAME ORDER BY BG.OPBK_ID ");
		fromAndWhere.append(") AS A ");
		fromAndWhere.append((StrUtils.isNotEmpty(conditionStr_2)? "WHERE " + conditionStr_2 : ""));
		return fromAndWhere.toString();
	}
	
	public String getCountQuery(Map<String, String> param){
		StringBuffer countQuery = new StringBuffer();
		countQuery.append(getWithTempSql(param));
		countQuery.append("SELECT COUNT(*) AS NUM ");
		countQuery.append(getFromAndWhere(param));
		System.out.println("countQuery==>"+countQuery.toString().toUpperCase());
		return countQuery.toString();
	}
	
	public String getSql(Map<String, String> param){
		StringBuffer sql = new StringBuffer();
		sql.append(	getWithTempSql(param));
		sql.append("SELECT ");
		sql.append("A.BANKID || '-' || COALESCE(A.BANKNAME,'') AS BANKID, ");
		sql.append("COALESCE((SELECT (CASE WHEN MBSYSSTATUS='1' THEN '開機' WHEN MBSYSSTATUS='2' THEN '押碼基碼同步' WHEN MBSYSSTATUS='3' THEN '訊息通知' WHEN MBSYSSTATUS='9' THEN '關機' ELSE 'NA' END) FROM BANKSYSSTATUSTAB WHERE BGBK_ID=A.BANKID with ur ), '') MemBankSsySts, ");//--參加單位系統狀態
		sql.append("COALESCE((SELECT (CASE WHEN MBAPSTATUS='1' THEN '簽到' WHEN MBAPSTATUS='2' THEN '暫時簽退' WHEN MBAPSTATUS='9' THEN '簽退'  ELSE 'NA' END ) FROM BANKAPSTATUSTAB WHERE BGBK_ID=A.BANKID AND APID='2000'  with ur ), '') MemBankAppSts, ");//--參加單位應用系統狀態
		sql.append("(SELECT COUNT(*) FROM TEMP WHERE SENDERACQUIRE = A.BANKID AND RESULTSTATUS = 'A') FIRE_SUCCESS_COUNT, ");
		sql.append("(SELECT COUNT(*) FROM TEMP WHERE SENDERACQUIRE = A.BANKID AND RESULTSTATUS = 'R') FIRE_FAIL_COUNT, ");
		sql.append("(SELECT COUNT(*) FROM TEMP WHERE OUTACQUIRE = A.BANKID AND RESULTSTATUS = 'A') OUT_SUCCESS_COUNT, ");
		sql.append("(SELECT COUNT(*) FROM TEMP WHERE OUTACQUIRE = A.BANKID AND RESULTSTATUS = 'R') OUT_FAIL_COUNT, ");
		sql.append("(SELECT COUNT(*) FROM TEMP WHERE INACQUIRE = A.BANKID AND RESULTSTATUS = 'A') IN_SUCCESS_COUNT, ");
		sql.append("(SELECT COUNT(*) FROM TEMP WHERE INACQUIRE = A.BANKID AND RESULTSTATUS = 'R') IN_FAIL_COUNT, ");
		sql.append("(SELECT COUNT(*) FROM TEMP WHERE (SENDERACQUIRE = A.BANKID OR OUTACQUIRE = A.BANKID OR INACQUIRE = A.BANKID) AND LENGTH(TIMEOUTCODE) > 0) TIMEOUTCOUNT, ");//--逾時交易筆數
		sql.append("(SELECT COUNT(*) FROM TEMP WHERE (SENDERACQUIRE = A.BANKID OR OUTACQUIRE = A.BANKID OR INACQUIRE = A.BANKID) AND COALESCE(RESULTSTATUS,'P') = 'P') PENDTCOUNT_1, ");//--未完成筆數
		sql.append("(SELECT COUNT(*) FROM TEMP WHERE (SENDERACQUIRE = A.BANKID OR OUTACQUIRE = A.BANKID OR INACQUIRE = A.BANKID) AND RESULTSTATUS='P' AND SENDERSTATUS <> '1' AND ACCTCODE='0') ACCTCOUNT, ");//--沖正交易筆數 20160728
		sql.append("(SELECT COUNT(*) FROM TEMP WHERE (SENDERACQUIRE = A.BANKID OR OUTACQUIRE = A.BANKID OR INACQUIRE = A.BANKID) AND RESULTSTATUS='P' AND SENDERSTATUS = '1') PENDTCOUNT_2, ");//--待處理筆數
		sql.append("(SELECT COUNT(*) FROM TEMP WHERE (SENDERACQUIRE = A.BANKID OR OUTACQUIRE = A.BANKID OR INACQUIRE = A.BANKID) AND COALESCE(DT_REQ_1,'') <> '' AND LENGTH(COALESCE(TIMEOUTCODE,'')) = 0) TOTALCOUNT, ");//--總筆數
		sql.append("COALESCE((SELECT REST_CR_LINE FROM CR_LINE WHERE BANK_ID=A.BANKID  with ur ), 0) REST_CR_LINE ");//--剩餘額度
		sql.append(getFromAndWhere(param));
		if(StrUtils.isNotEmpty(param.get("sidx"))){
			sql.append(" ORDER BY "+param.get("sidx")+" "+param.get("sord"));
		}
		System.out.println("sql==>"+sql.toString().toUpperCase());
		return sql.toString();
	}
	
	public String pageSearch(Map<String, String> param){
		Map rtnMap = new HashMap();
		List<MONITOR_BIZ> list = null;
		try {
			list = new ArrayList<MONITOR_BIZ>();
			String cols[] = {"BANKID","MEMBANKSSYSTS","MEMBANKAPPSTS","TIMEOUTCOUNT","PENDTCOUNT_1","FIRE_SUCCESS_COUNT","FIRE_FAIL_COUNT","OUT_SUCCESS_COUNT","OUT_FAIL_COUNT","IN_SUCCESS_COUNT","IN_FAIL_COUNT","TOTALCOUNT","PENDTCOUNT_2","REST_CR_LINE","ACCTCOUNT"};
			String sql = getSql(param);
			list = onblocktab_Dao.search(sql, cols, MONITOR_BIZ.class);
			System.out.println("MONITOR_BIZ.list>>"+list);
			list = list!=null&& list.size() ==0 ?new ArrayList():list;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		rtnMap.put("total", 0);
		rtnMap.put("page", 0);
		rtnMap.put("records", list.size());
		rtnMap.put("rows", list);
		
		String json = JSONUtils.map2json(rtnMap) ;
		//System.out.println("json->" +json);
		return json;
	}
	
	public String pageSearch2(Map<String, String> param){
		Map rtnMap = new HashMap();
		List<MONITOR_BIZ> list = null;
		try {
			list = new ArrayList<MONITOR_BIZ>();
			String cols[] = {"BANKID","MEMBANKSSYSTS","MEMBANKAPPSTS","TIMEOUTCOUNT","PENDTCOUNT_1","FIRE_SUCCESS_COUNT","FIRE_FAIL_COUNT","OUT_SUCCESS_COUNT","OUT_FAIL_COUNT","IN_SUCCESS_COUNT","IN_FAIL_COUNT","TOTALCOUNT","PENDTCOUNT_2","REST_CR_LINE","ACCTCOUNT"};
			String sql = getSql(param);
			list = onblocktab_Dao.search(sql, cols, MONITOR_BIZ.class);
			System.out.println("MONITOR_BIZ.list>>"+list);
			list = list!=null&& list.size() ==0 ?new ArrayList():list;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		rtnMap.put("total", 0);
		rtnMap.put("page", 0);
		rtnMap.put("records", list.size());
		rtnMap.put("rows", list);
		
		String json = JSONUtils.map2json(rtnMap) ;
		//System.out.println("json->" +json);
		return json;
	}
	/*
	public String pageSearch(Map<String, String> param){
		String pageNo = StrUtils.isEmpty(param.get("page")) ?"0":param.get("page");
		String pageSize = StrUtils.isEmpty(param.get("rows")) ?Arguments.getStringArg("PAGE.SIZE"):param.get("rows");
		
		Map rtnMap = new HashMap();
		
		List<MONITOR_BIZ> list = null;
		Page page = null;
		try {
			list = new ArrayList<MONITOR_BIZ>();
			String cols[] = {"BANKID","MEMBANKSSYSTS","MEMBANKAPPSTS","TIMEOUTCOUNT","PENDTCOUNT_1","FIRE_SUCCESS_COUNT","FIRE_FAIL_COUNT","OUT_SUCCESS_COUNT","OUT_FAIL_COUNT","IN_SUCCESS_COUNT","IN_FAIL_COUNT","TOTALCOUNT","PENDTCOUNT_2","REST_CR_LINE"};
			String countQuery = getCountQuery(param);
			String sql = getSql(param);
			
			page = onblocktab_Dao.getDataIIII(Integer.valueOf(pageNo), Integer.valueOf(pageSize), countQuery, sql, cols, MONITOR_BIZ.class);
			list = (List<MONITOR_BIZ>) page.getResult();
			//System.out.println("MONITOR_BIZ.list>>"+list);
			list = list!=null&& list.size() ==0 ?null:list;
		} catch (Exception e) {
			// TODO Auto-generated catch block
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
	*/
	
	/**
	 * 依照DATEMODE取得目前營業日、清算階段及前一清算階段、營業日(for AJAX)
	 * @return
	 */
	public String getBusinessDateAndClrphase(Map<String, String> params){
		Map<String, String> data = new HashMap<String, String>();
		List<EACHSYSSTATUSTAB> list = null;
		String businessDate = "", clrphase = "";
		String pre_businessDate = "", pre_clrphase = "";
		
		try{
			list = eachsysstatustab_Dao.getBusinessDate();
			if(list != null && list.size() > 0){
				businessDate = list.get(0).getBUSINESS_DATE();
				clrphase = list.get(0).getCLEARINGPHASE();
			}
			Map dateData = eachsysstatustab_bo.getRowData();
			//前一階段的營業日
			pre_businessDate = (String) dateData.get("THISDATE");
			//前一階段的清算階段
			pre_clrphase = (clrphase.equals("01")? "02" : "01");
		}catch(Exception e){
			e.printStackTrace();
		}
		data.put("bizdate", DateTimeUtils.convertDate(DateTimeUtils.NOT_INTERCONVERSION, businessDate, "yyyyMMdd", "yyyy-MM-dd"));
		data.put("clearingphase", clrphase);
		data.put("pre_bizdate", DateTimeUtils.convertDate(DateTimeUtils.NOT_INTERCONVERSION, pre_businessDate, "yyyyMMdd", "yyyy-MM-dd"));
		data.put("pre_clearingphase", pre_clrphase);
		return JSONUtils.map2json(data);
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

	public BANK_BRANCH_Dao getBank_branch_Dao() {
		return bank_branch_Dao;
	}

	public void setBank_branch_Dao(BANK_BRANCH_Dao bank_branch_Dao) {
		this.bank_branch_Dao = bank_branch_Dao;
	}

	public EACHSYSSTATUSTAB_BO getEachsysstatustab_bo() {
		return eachsysstatustab_bo;
	}

	public void setEachsysstatustab_bo(EACHSYSSTATUSTAB_BO eachsysstatustab_bo) {
		this.eachsysstatustab_bo = eachsysstatustab_bo;
	}

	public EACHSYSSTATUSTAB_Dao getEachsysstatustab_Dao() {
		return eachsysstatustab_Dao;
	}

	public void setEachsysstatustab_Dao(EACHSYSSTATUSTAB_Dao eachsysstatustab_Dao) {
		this.eachsysstatustab_Dao = eachsysstatustab_Dao;
	}
	
}
