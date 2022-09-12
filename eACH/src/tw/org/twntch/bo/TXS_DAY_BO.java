package tw.org.twntch.bo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.struts.util.LabelValueBean;

import tw.org.twntch.bean.TXS_DAY;
import tw.org.twntch.db.dao.hibernate.BANK_BRANCH_Dao;
import tw.org.twntch.db.dao.hibernate.BANK_GROUP_Dao;
import tw.org.twntch.db.dao.hibernate.EACH_TXN_CODE_Dao;
import tw.org.twntch.db.dao.hibernate.ONBLOCKTAB_Dao;
import tw.org.twntch.db.dao.hibernate.Page;
import tw.org.twntch.db.dao.hibernate.RPONBLOCKTAB_Dao;
import tw.org.twntch.po.BANK_BRANCH;
import tw.org.twntch.po.BANK_GROUP;
import tw.org.twntch.po.BANK_OPBK;
import tw.org.twntch.po.EACH_TXN_CODE;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.RptUtils;
import tw.org.twntch.util.StrUtils;

public class TXS_DAY_BO {
	private ONBLOCKTAB_Dao onblocktab_Dao;
	private BANK_GROUP_Dao bank_group_Dao;
	private EACH_TXN_CODE_Dao each_txn_code_Dao;
	private BANK_BRANCH_Dao bank_branch_Dao;
	private BANK_GROUP_BO bank_group_bo;
	private RPONBLOCKTAB_Dao rponblocktab_Dao;
	
	/**
	 * 取得操作行代號清單
	 * @return
	 */
	public List<LabelValueBean> getOpbkIdList(){
		return bank_group_bo.getOpbkList();
		
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
	
	/**
	 * 取得總行代號清單
	 * @return
	 */
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
	
	/**
	 * 取得分行代號清單
	 * @return
	 */
	public List<LabelValueBean> getBrbkIdList(String bgbkId){
		List<BANK_BRANCH> list = null;
		if(StrUtils.isEmpty(bgbkId)){
			list = bank_branch_Dao.getAll();
		}else{
			list = bank_branch_Dao.getDataByBgBkId(bgbkId);
		}
		List<LabelValueBean> beanList = new LinkedList<LabelValueBean>();
		LabelValueBean bean = null;
		for(BANK_BRANCH po : list){
			bean = new LabelValueBean(po.getId().getBRBK_ID() + " - " + po.getBRBK_NAME(), po.getId().getBRBK_ID());
			beanList.add(bean);
		}
		System.out.println("beanList>>"+beanList);
		return beanList;
	}
	
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
	
	public String pageSearch(Map<String, String> param){
		int pageNo = StrUtils.isEmpty(param.get("page")) ?0:Integer.valueOf(param.get("page"));
		int pageSize = StrUtils.isEmpty(param.get("rows")) ?Integer.valueOf(Arguments.getStringArg("PAGE.SIZE")):Integer.valueOf(param.get("rows"));
		
		Map rtnMap = new HashMap();
		
		List<TXS_DAY> list = null;
		Page page = null;
		try {
			list = new ArrayList<TXS_DAY>();
			String condition_1 = "", condition_2 = "";
			List<String> conditionList = getConditionList(param);
			condition_1 = conditionList.get(0);
			condition_2 = conditionList.get(1);
			
			StringBuffer withTempSql = new StringBuffer();
			//20150312 by 李建利 失敗交易仍要顯示最初交易金額
			withTempSql.append("WITH TEMP AS ( ");
			withTempSql.append("    SELECT TXDATE, STAN, NEWTXAMT, SENDERACQUIRE, OUTACQUIRE, INACQUIRE, ");
			withTempSql.append("    BIZDATE, CLEARINGPHASE, PCODE, RESULTSTATUS AS NEWRESULT, ACCTCODE AS ACCTCODE, ");
			withTempSql.append("    COALESCE(SENDERHEAD,'') AS SENDERHEAD,COALESCE(OUTHEAD,'') AS OUTHEAD,COALESCE(INHEAD,'') AS INHEAD ");
			withTempSql.append("    FROM VW_ONBLOCKTAB ");
			//20150507 HUANGPU 過濾掉垃圾資料
			withTempSql.append("    WHERE SENDERSTATUS <> '1' AND COALESCE(GARBAGEDATA,'') <> '*' ");
			withTempSql.append("    " + (StrUtils.isEmpty(condition_1)? "" : "AND " + condition_1));
			withTempSql.append("    WITH UR ) ");
			
			StringBuffer fromAndWhere = new StringBuffer();
			StringBuffer fromAndWhere_all = new StringBuffer();
			fromAndWhere.append("FROM ( ");
			fromAndWhere.append("    SELECT ROWNUMBER() OVER( ");
			fromAndWhere.append(parseSidx(param.get("sidx"), param.get("sord")));
			fromAndWhere.append("    ) AS ROWNUMBER, T.* FROM ( ");
			fromAndWhere.append("		 SELECT SENDERACQUIRE AS OPBKID, SENDERHEAD AS BANKID, PCODE, NEWRESULT FROM TEMP GROUP BY SENDERACQUIRE, SENDERHEAD, PCODE, NEWRESULT ");
			fromAndWhere.append("		 UNION ");
			fromAndWhere.append("		 SELECT OUTACQUIRE, OUTHEAD, PCODE, NEWRESULT FROM TEMP GROUP BY OUTACQUIRE, OUTHEAD, PCODE, NEWRESULT ");
			fromAndWhere.append("		 UNION ");
			fromAndWhere.append("		 SELECT INACQUIRE, INHEAD, PCODE, NEWRESULT FROM TEMP GROUP BY INACQUIRE, INHEAD, PCODE, NEWRESULT ");
			fromAndWhere.append("    ) AS T " + (StrUtils.isEmpty(condition_2)? "" : "WHERE " + condition_2));
			fromAndWhere.append(") AS A ");
			fromAndWhere_all.append(fromAndWhere);			
			fromAndWhere.append("WHERE ROWNUMBER >= " + (Page.getStartOfPage(pageNo, pageSize) + 1) + " AND ROWNUMBER <= " + (pageNo * pageSize) + " ");
			
			StringBuffer selectStr = new StringBuffer();
			selectStr.append("(SELECT COUNT(*) FROM TEMP AS B WHERE B.SENDERHEAD = A.BANKID AND B.PCODE = A.PCODE AND B.NEWRESULT = A.NEWRESULT) AS FIRECOUNT, ");
			selectStr.append("COALESCE((SELECT SUM(COALESCE(NEWTXAMT,0)) FROM TEMP AS B WHERE B.SENDERHEAD = A.BANKID AND B.PCODE = A.PCODE AND B.NEWRESULT = A.NEWRESULT),0) AS FIREAMT, ");
			selectStr.append("(SELECT COUNT(*) FROM TEMP AS B WHERE B.OUTHEAD = A.BANKID AND B.PCODE = A.PCODE AND B.NEWRESULT = A.NEWRESULT) AS DEBITCOUNT, ");
			selectStr.append("COALESCE((SELECT SUM(COALESCE(NEWTXAMT,0)) FROM TEMP AS B WHERE B.OUTHEAD = A.BANKID AND B.PCODE = A.PCODE AND B.NEWRESULT = A.NEWRESULT),0) AS DEBITAMT, ");
			selectStr.append("(SELECT COUNT(*) FROM TEMP AS B WHERE B.INHEAD = A.BANKID AND B.PCODE = A.PCODE AND B.NEWRESULT = A.NEWRESULT) AS SAVECOUNT, ");
			selectStr.append("COALESCE((SELECT SUM(COALESCE(NEWTXAMT,0)) FROM TEMP AS B WHERE B.INHEAD = A.BANKID AND B.PCODE = A.PCODE AND B.NEWRESULT = A.NEWRESULT),0) AS SAVEAMT ");
			
			//先算出總計的欄位值，放到rtnMap裡面，到頁面再放到下面的總計Grid裡面
			String dataSumCols[] = {"FIRECOUNT", "FIREAMT", "DEBITCOUNT", "DEBITAMT", "SAVECOUNT", "SAVEAMT"};
			StringBuffer dataSumSQL = new StringBuffer();
			dataSumSQL.append(withTempSql);
			dataSumSQL.append("SELECT SUM(FIRECOUNT) AS FIRECOUNT,SUM(FIREAMT) AS FIREAMT,SUM(DEBITCOUNT) AS DEBITCOUNT,SUM(DEBITAMT) AS DEBITAMT,SUM(SAVECOUNT) AS SAVECOUNT,SUM(SAVEAMT) AS SAVEAMT ");
			dataSumSQL.append("FROM ( ");
			dataSumSQL.append("    SELECT ");
			dataSumSQL.append("    " + selectStr);
			dataSumSQL.append("    " + fromAndWhere_all);
			dataSumSQL.append(")");
			//System.out.println("dataSumSQL = " + dataSumSQL.toString().toUpperCase());
			
			list = onblocktab_Dao.dataSum(dataSumSQL.toString(),dataSumCols,TXS_DAY.class);
			rtnMap.put("dataSumList", list);
			/* FOR TEST
			for(TXS_DAY po : list){
				System.out.println(String.format("
				SUM(X.FIRECOUNT)=%s,
				SUM(X.FIREAMT)=%s,
				SUM(X.DEBITCOUNT)=%s,
				SUM(X.DEBITAMT)=%s,
				SUM(X.SAVECOUNT)=%s,
				SUM(X.SAVEAMT)=%s",
				po.getFIRECOUNT(),
				po.getFIREAMT(),
				po.getDEBITCOUNT(),
				po.getDEBITAMT(),
				po.getSAVECOUNT(),
				po.getSAVEAMT())
				);
			}
			*/
			
			String cols[] = {"BANKHEAD", "PCODE", "RESULTSTATUS", "FIRECOUNT", "FIREAMT", "DEBITCOUNT", "DEBITAMT", "SAVECOUNT", "SAVEAMT"};
			StringBuffer countQuery = new StringBuffer();
			countQuery.append(withTempSql);
			countQuery.append("SELECT COUNT(*) AS NUM ");
			countQuery.append(fromAndWhere_all);
			//System.out.println("countQuery = " + countQuery.toString().toUpperCase());
			
			StringBuffer sql = new StringBuffer();
			sql.append(withTempSql);
			sql.append("SELECT ");
			sql.append("A.BANKID || '-' || COALESCE((SELECT BGBK_NAME FROM BANK_GROUP WHERE A.BANKID = BGBK_ID),'') AS BANKHEAD, ");
			sql.append("A.PCODE, (CASE A.NEWRESULT WHEN 'A' THEN '成功' WHEN 'R' THEN '失敗' ELSE '未完成' END) AS RESULTSTATUS, ");
			sql.append(selectStr);
			sql.append(fromAndWhere);
			if(StrUtils.isNotEmpty(param.get("sidx"))){
				if(param.get("sidx").contains("ASC") || param.get("sidx").contains("DESC") ||
				   param.get("sidx").contains("asc") || param.get("sidx").contains("desc")){
					sql.append(" ORDER BY "+param.get("sidx"));
				}else{
					sql.append(" ORDER BY "+param.get("sidx")+" "+param.get("sord"));
				}
			}
			System.out.println("sql==>" + sql.toString().toUpperCase());
			
			page = onblocktab_Dao.getDataIIII(pageNo, pageSize, countQuery.toString().toUpperCase(), sql.toString().toUpperCase(), cols, TXS_DAY.class);
			list = (List<TXS_DAY>) page.getResult();
			//System.out.println("list>>"+list);
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
		
		String json = JSONUtils.map2json(rtnMap);
		return json;
	}
	
	public Map<String, String> qs_ex_export(String txDate, String endDate, String pcode, String senderacquire, String bgbkId, String clearingPhase, String resultStatus, String serchStrs, String sortname, String sortorder){
		Map<String, String> rtnMap = null;
		
		try{
			rtnMap = new HashMap<String, String>();
			rtnMap.put("serchStrs", serchStrs);
			Map<String, Object> params = new HashMap<String, Object>();
			String sql = getSQL(txDate, endDate, pcode, senderacquire, bgbkId, clearingPhase, resultStatus, sortname, sortorder);
			List list = rponblocktab_Dao.getRptData(sql, new ArrayList());
			
			int pathType = Arguments.getStringArg("ISWINOPEN").equals("Y")?RptUtils.C_PATH :RptUtils.R_PATH;
			String outputFilePath = RptUtils.export(RptUtils.COLLECTION,pathType, "txs_day", "txs_day", params, list, 2);
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
	
	public String getSQL(String txDate, String endDate, String pcode, String senderacquire, String bgbkId, String clearingPhase, String resultStatus, String sidx, String sord){
		HashMap<String, String> param = new HashMap<String, String>();
		param.put("TXDATE", txDate);
		param.put("END_DATE", endDate);
		param.put("PCODE", pcode);
		param.put("SENDERACQUIRE", senderacquire);
		param.put("BGBK_ID", bgbkId);
		param.put("CLEARINGPHASE", clearingPhase);
		param.put("RESULTSTATUS", resultStatus);
		List<String> conditionList = getConditionList(param);
		String condition_1 = conditionList.get(0);
		String condition_2 = conditionList.get(1);
		
		StringBuffer withTempSql = new StringBuffer();
		//20150312 by 李建利 失敗交易仍要顯示最初交易金額
		withTempSql.append("WITH TEMP AS ( ");
		withTempSql.append("    SELECT TXDATE, STAN, NEWTXAMT, SENDERACQUIRE, OUTACQUIRE, INACQUIRE, ");
		withTempSql.append("    BIZDATE, CLEARINGPHASE, PCODE, RESULTSTATUS AS NEWRESULT, ACCTCODE AS ACCTCODE, ");
		withTempSql.append("    COALESCE(SENDERHEAD,'') AS SENDERHEAD,COALESCE(OUTHEAD,'') AS OUTHEAD,COALESCE(INHEAD,'') AS INHEAD ");
		withTempSql.append("    FROM VW_ONBLOCKTAB ");
		//20150507 HUANGPU 過濾掉垃圾資料
		withTempSql.append("    WHERE SENDERSTATUS <> '1' AND COALESCE(GARBAGEDATA,'') <> '*' ");
		withTempSql.append("    " + (StrUtils.isEmpty(condition_1)? "" : "AND " + condition_1));
		withTempSql.append(") ");
		
		StringBuffer fromAndWhere = new StringBuffer();
		fromAndWhere.append("FROM ( ");
		fromAndWhere.append("    SELECT ROWNUMBER() OVER( ");
		fromAndWhere.append(parseSidx(param.get("sidx"), param.get("sord")));
		fromAndWhere.append("    ) AS ROWNUMBER, T.* FROM ( ");
		fromAndWhere.append("		 SELECT SENDERHEAD AS BANKID, PCODE, NEWRESULT FROM TEMP GROUP BY SENDERHEAD, PCODE, NEWRESULT ");
		fromAndWhere.append("        UNION ");
		fromAndWhere.append("        SELECT OUTHEAD, PCODE, NEWRESULT FROM TEMP GROUP BY OUTHEAD, PCODE, NEWRESULT ");
		fromAndWhere.append("        UNION ");
		fromAndWhere.append("        SELECT INHEAD, PCODE, NEWRESULT FROM TEMP GROUP BY INHEAD, PCODE, NEWRESULT ");
		fromAndWhere.append("    ) AS T " + (StrUtils.isEmpty(condition_2)? "" : "WHERE " + condition_2));
		fromAndWhere.append(") AS A ");
		
		StringBuffer selectStr = new StringBuffer();
		selectStr.append("(SELECT COUNT(*) FROM TEMP AS B WHERE B.SENDERHEAD = A.BANKID AND B.PCODE = A.PCODE AND B.NEWRESULT = A.NEWRESULT) AS FIRECOUNT, ");
		selectStr.append("DECIMAL(COALESCE((SELECT SUM(COALESCE(NEWTXAMT,0)) FROM TEMP AS B WHERE B.SENDERHEAD = A.BANKID AND B.PCODE = A.PCODE AND B.NEWRESULT = A.NEWRESULT),0),20,0) AS FIREAMT, ");
		selectStr.append("(SELECT COUNT(*) FROM TEMP AS B WHERE B.OUTHEAD = A.BANKID AND B.PCODE = A.PCODE AND B.NEWRESULT = A.NEWRESULT) AS DEBITCOUNT, ");
		selectStr.append("DECIMAL(COALESCE((SELECT SUM(COALESCE(NEWTXAMT,0)) FROM TEMP AS B WHERE B.OUTHEAD = A.BANKID AND B.PCODE = A.PCODE AND B.NEWRESULT = A.NEWRESULT),0),20,0) AS DEBITAMT, ");
		selectStr.append("(SELECT COUNT(*) FROM TEMP AS B WHERE B.INHEAD = A.BANKID AND B.PCODE = A.PCODE AND B.NEWRESULT = A.NEWRESULT) AS SAVECOUNT, ");
		selectStr.append("DECIMAL(COALESCE((SELECT SUM(COALESCE(NEWTXAMT,0)) FROM TEMP AS B WHERE B.INHEAD = A.BANKID AND B.PCODE = A.PCODE AND B.NEWRESULT = A.NEWRESULT),0),20,0) AS SAVEAMT ");
		
		StringBuffer sql = new StringBuffer();
		sql.append(withTempSql);
		sql.append("SELECT ");
		sql.append("A.BANKID || '-' || COALESCE((SELECT BGBK_NAME FROM BANK_GROUP WHERE A.BANKID = BGBK_ID),'') AS BANKHEAD, ");
		sql.append("VARCHAR(A.PCODE) AS PCODE, (CASE A.NEWRESULT WHEN 'A' THEN '成功' WHEN 'R' THEN '失敗' ELSE '未完成' END) AS RESULTSTATUS, ");
		sql.append(selectStr);
		sql.append(fromAndWhere);
		if(StrUtils.isNotEmpty(sidx)){
			if(sidx.contains("ASC") || sidx.contains("DESC") ||
					sidx.contains("asc") || sidx.contains("desc")){
				sql.append(" ORDER BY "+sidx);
			}else{
				sql.append(" ORDER BY "+sidx+" "+sord);
			}
		}
		return sql.toString();
	}
	
	public String getOrderCondition(String sidx){
		if(sidx.startsWith("BANKHEAD")){
			return "BANKID";
		}else if(sidx.startsWith("RESULTSTATUS")){
			return "NEWRESULT";
		}else if(sidx.startsWith("PCODE")){
			return "PCODE";
		}else if(sidx.startsWith("FIRECOUNT")){
			return "(SELECT COUNT(*) FROM TEMP AS B WHERE B.SENDERHEAD = T.BANKID AND B.PCODE = T.PCODE AND B.NEWRESULT = T.NEWRESULT)";
		}else if(sidx.startsWith("FIREAMT")){
			return "COALESCE((SELECT SUM(COALESCE(NEWTXAMT,0)) FROM TEMP AS B WHERE B.SENDERHEAD = T.BANKID AND B.PCODE = T.PCODE AND B.NEWRESULT = T.NEWRESULT),0)";
		}else if(sidx.startsWith("DEBITCOUNT")){
			return "(SELECT COUNT(*) FROM TEMP AS B WHERE B.OUTHEAD = T.BANKID AND B.PCODE = T.PCODE AND B.NEWRESULT = T.NEWRESULT)";
		}else if(sidx.startsWith("DEBITAMT")){
			return "COALESCE((SELECT SUM(COALESCE(NEWTXAMT,0)) FROM TEMP AS B WHERE B.OUTHEAD = T.BANKID AND B.PCODE = T.PCODE AND B.NEWRESULT = T.NEWRESULT),0)";
		}else if(sidx.startsWith("SAVECOUNT")){
			return "(SELECT COUNT(*) FROM TEMP AS B WHERE B.INHEAD = T.BANKID AND B.PCODE = T.PCODE AND B.NEWRESULT = T.NEWRESULT)";
		}else if(sidx.startsWith("SAVEAMT")){
			return "COALESCE((SELECT SUM(COALESCE(NEWTXAMT,0)) FROM TEMP AS B WHERE B.INHEAD = T.BANKID AND B.PCODE = T.PCODE AND B.NEWRESULT = T.NEWRESULT),0)";
		}else{
			return sidx;
		}
	}
	
	public List<String> getConditionList(Map<String, String> param){
		List<String> conditionList = new ArrayList<String>();
		
		List<String> conditions_1 = new ArrayList<String>();
		List<String> conditions_2 = new ArrayList<String>();
		//營業日(起)
		String bizDate = "";
		if(StrUtils.isNotEmpty(param.get("TXDATE").trim())){
			bizDate = param.get("TXDATE").trim();
			conditions_1.add(" BIZDATE >= '" + DateTimeUtils.convertDate(bizDate, "yyyyMMdd", "yyyyMMdd") + "' ");
		}
		//營業日(迄)
		String endDate = "";
		if(StrUtils.isNotEmpty(param.get("END_DATE").trim())){
			endDate = param.get("END_DATE").trim();
			conditions_1.add(" BIZDATE <= '" + DateTimeUtils.convertDate(endDate, "yyyyMMdd", "yyyyMMdd") + "' ");
		}		
		//交易代號
		String pcode = "";
		if(StrUtils.isNotEmpty(param.get("PCODE").trim()) && !param.get("PCODE").trim().equals("all")){
			pcode = param.get("PCODE").trim();
			conditions_1.add(" PCODE = '" + pcode + "' ");
		}
		//操作行
		String opbkId = "";
		if(StrUtils.isNotEmpty(param.get("SENDERACQUIRE").trim()) && !param.get("SENDERACQUIRE").trim().equals("all")){
			opbkId = param.get("SENDERACQUIRE").trim();
			conditions_1.add(" (SENDERACQUIRE = '" + opbkId + "' OR OUTACQUIRE = '" + opbkId + "' OR INACQUIRE = '" + opbkId + "') ");
			//conditions_2.add(" (SELECT OPBK_ID FROM BANK_GROUP WHERE BGBK_ID = BANKID) = '" + opbkId + "' ");
			//conditions_2.add(" (SELECT OPBK_ID FROM BANK_OPBK WHERE BGBK_ID = BANKID AND OPBK_ID = '" + opbkId + "') = '" + opbkId + "' ");
			conditions_2.add(" T.OPBKID = '" + opbkId + "' ");
		}
		//總行代號
		String bgbkId = "";
		if(StrUtils.isNotEmpty(param.get("BGBK_ID").trim()) && !param.get("BGBK_ID").trim().equals("all")){
			bgbkId = param.get("BGBK_ID").trim();
			conditions_1.add(" (SENDERHEAD = '" + bgbkId + "' OR OUTHEAD = '" + bgbkId + "' OR INHEAD = '" + bgbkId + "') ");
			conditions_2.add(" T.BANKID = '" + bgbkId + "' ");
		}
		//清算階段
		String clearingPhase = "";
		if(StrUtils.isNotEmpty(param.get("CLEARINGPHASE").trim()) && !param.get("CLEARINGPHASE").trim().equals("all")){
			clearingPhase = param.get("CLEARINGPHASE").trim();
			conditions_1.add(" CLEARINGPHASE = '" + clearingPhase + "' ");
		}
		//交易結果
		String resultStatus = "";
		if(StrUtils.isNotEmpty(param.get("RESULTSTATUS").trim()) && !param.get("RESULTSTATUS").trim().equals("all")){
			resultStatus = param.get("RESULTSTATUS").trim();
			conditions_1.add(" RESULTSTATUS = '" + resultStatus + "' ");
		}
		conditionList.add( combine(conditions_1) );
		conditionList.add( combine(conditions_2) );
		return conditionList;
	}
	
	public String parseSidx(String sidx, String sord){
		StringBuffer orderbyStr = new StringBuffer("");
		if(StrUtils.isNotEmpty(sidx)){
			orderbyStr.append("ORDER BY ");
			String strAry[] = sidx.split(",");
			if(strAry.length == 1){
				orderbyStr.append(getOrderCondition(strAry[0].trim()) + " ");
				if(StrUtils.isNotEmpty(sord)){
					orderbyStr.append(sord);
				}
			}else{
				for(int i = 0; i < strAry.length; i++){
					strAry[i] = strAry[i].trim();
					orderbyStr.append(getOrderCondition(strAry[i]));
					if(strAry[i].contains(" ")){
						orderbyStr.append(" " + strAry[i].split(" ")[1]);
					}
					if(i < strAry.length - 1){
						orderbyStr.append(", ");
					}
				}
			}
		}
		return orderbyStr.toString();
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
	public BANK_BRANCH_Dao getBank_branch_Dao() {
		return bank_branch_Dao;
	}
	public void setBank_branch_Dao(BANK_BRANCH_Dao bank_branch_Dao) {
		this.bank_branch_Dao = bank_branch_Dao;
	}
	public BANK_GROUP_BO getBank_group_bo() {
		return bank_group_bo;
	}
	public void setBank_group_bo(BANK_GROUP_BO bank_group_bo) {
		this.bank_group_bo = bank_group_bo;
	}
	public RPONBLOCKTAB_Dao getRponblocktab_Dao() {
		return rponblocktab_Dao;
	}

	public void setRponblocktab_Dao(RPONBLOCKTAB_Dao rponblocktab_Dao) {
		this.rponblocktab_Dao = rponblocktab_Dao;
	}
}
