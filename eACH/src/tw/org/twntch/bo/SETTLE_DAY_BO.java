package tw.org.twntch.bo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.struts.util.LabelValueBean;

import tw.org.twntch.db.dao.hibernate.BANK_GROUP_Dao;
import tw.org.twntch.db.dao.hibernate.EACH_TXN_CODE_Dao;
import tw.org.twntch.db.dao.hibernate.Page;
import tw.org.twntch.db.dao.hibernate.RPONBLOCKTAB_Dao;
import tw.org.twntch.db.dao.hibernate.SETTLE_DAY_Dao;
import tw.org.twntch.db.dao.hibernate.VW_ONBLOCKTAB_Dao;
import tw.org.twntch.po.BANK_OPBK;
import tw.org.twntch.po.EACH_TXN_CODE;
import tw.org.twntch.po.ONCLEARINGTAB;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.RptUtils;
import tw.org.twntch.util.StrUtils;

public class SETTLE_DAY_BO {
	private EACH_TXN_CODE_Dao each_txn_code_Dao;
	private BANK_GROUP_Dao bank_group_Dao;
	private SETTLE_DAY_Dao settle_day_Dao;
	private VW_ONBLOCKTAB_Dao vw_onblocktab_Dao;
	private BANK_OPBK_BO bank_opbk_bo;
	private RPONBLOCKTAB_Dao rponblocktab_Dao;
	
	/**
	 * 取得交易類別(PCODE-4碼)清單
	 * @return
	 */
	public List<LabelValueBean> getPcodeList(){
		List<EACH_TXN_CODE> list = each_txn_code_Dao.getTranCode();
		List<LabelValueBean> beanList = new LinkedList<LabelValueBean>();
		LabelValueBean bean = null;
		for(EACH_TXN_CODE po : list){
			bean = new LabelValueBean(po.getEACH_TXN_ID() + " - " + po.getEACH_TXN_NAME(), po.getEACH_TXN_ID());
			System.out.println("po.getEACH_TXN_ID()="+po.getEACH_TXN_ID()+",po.getEACH_TXN_NAME()="+po.getEACH_TXN_NAME());
			beanList.add(bean);
		}
		return beanList;
	}
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
//			System.out.println("po.getBGBK_ID()="+po.getBGBK_ID()+",po.getBGBK_NAME()="+po.getBGBK_NAME());
//			beanList.add(bean);
//		}
//		return beanList;
	}
	
	public Map<String, String> qs_ex_export(String date, String pcode, String opbkId, String bgbkId, String clearingPhase, String serchStrs, String sortname, String sortorder){
		Map<String, String> rtnMap = null;
		
		try{
			rtnMap = new HashMap<String, String>();
			rtnMap.put("serchStrs", serchStrs);
			Map<String, Object> params = new HashMap<String, Object>();
			String sql = getSQL(date, pcode, opbkId, bgbkId, clearingPhase, serchStrs, sortname, sortorder);
			List list = rponblocktab_Dao.getRptData(sql, new ArrayList());
			
			int pathType = Arguments.getStringArg("ISWINOPEN").equals("Y")?RptUtils.C_PATH :RptUtils.R_PATH;
			String outputFilePath = RptUtils.export(RptUtils.COLLECTION,pathType, "settle_day", "settle_day", params, list, 2);
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
	
	//JQGRID分頁查詢
	public String pageSearch(Map<String, String> param){
		List<ONCLEARINGTAB> list= null;
		
		int pageNo = StrUtils.isEmpty(param.get("page")) ?0:Integer.valueOf(param.get("page"));
		int pageSize = StrUtils.isEmpty(param.get("rows")) ?Integer.valueOf(Arguments.getStringArg("PAGE.SIZE")):Integer.valueOf(param.get("rows"));
		Map<String,Object> rtnMap = new HashMap<String,Object>();
		
		List<String> conditionList = getConditionList(param);
		String condition_1 = "",condition_2 = "",condition_3 = "";
		condition_1 = conditionList.get(0);
		condition_2 = conditionList.get(1);
		condition_3 = conditionList.get(2);
		
		StringBuffer selectStr = new StringBuffer();
		selectStr.append("SELECT ");
		selectStr.append("A.BANKID || '-' || (SELECT BGBK_NAME FROM BANK_GROUP WHERE BGBK_ID = A.BANKID) AS BANKIDANDNAME, ");
		selectStr.append("A.PCODE AS PCODEANDNAME, CLEARINGPHASE, ");
		selectStr.append("(SELECT COUNT(*) FROM TEMP WHERE COALESCE(ACCTCODE,'') <> '0' AND INHEAD = A.BANKID AND PCODE = A.PCODE AND CLEARINGPHASE = A.CLEARINGPHASE) AS RECVCNT, ");
		selectStr.append("COALESCE((SELECT SUM(NEWTXAMT) FROM TEMP WHERE COALESCE(ACCTCODE,'') <> '0' AND INHEAD = A.BANKID AND PCODE = A.PCODE AND CLEARINGPHASE = A.CLEARINGPHASE),0) AS RECVAMT, ");
		selectStr.append("(SELECT COUNT(*) FROM TEMP WHERE COALESCE(ACCTCODE,'') <> '0' AND OUTHEAD = A.BANKID AND PCODE = A.PCODE AND CLEARINGPHASE = A.CLEARINGPHASE) AS PAYCNT, ");
		selectStr.append("COALESCE((SELECT SUM(NEWTXAMT) FROM TEMP WHERE COALESCE(ACCTCODE,'') <> '0' AND OUTHEAD = A.BANKID AND PCODE = A.PCODE AND CLEARINGPHASE = A.CLEARINGPHASE),0) AS PAYAMT, ");
		selectStr.append("(SELECT COUNT(*) FROM TEMP WHERE COALESCE(ACCTCODE,'') = '0' AND OUTHEAD = A.BANKID AND PCODE = A.PCODE AND CLEARINGPHASE = A.CLEARINGPHASE) AS RVSRECVCNT, "); 
		selectStr.append("COALESCE((SELECT ABS(SUM(NEWTXAMT)) FROM TEMP WHERE COALESCE(ACCTCODE,'') = '0' AND OUTHEAD = A.BANKID AND PCODE = A.PCODE AND CLEARINGPHASE = A.CLEARINGPHASE),0) AS RVSRECVAMT, ");
		selectStr.append("(SELECT COUNT(*) FROM TEMP WHERE COALESCE(ACCTCODE,'') = '0' AND INHEAD = A.BANKID AND PCODE = A.PCODE AND CLEARINGPHASE = A.CLEARINGPHASE) AS RVSPAYCNT, "); 
		selectStr.append("COALESCE((SELECT ABS(SUM(NEWTXAMT)) FROM TEMP WHERE COALESCE(ACCTCODE,'') = '0' AND INHEAD = A.BANKID AND PCODE = A.PCODE AND CLEARINGPHASE = A.CLEARINGPHASE),0) AS RVSPAYAMT ");
			
		StringBuffer withTemp = new StringBuffer();
		withTemp.append("WITH TEMP AS ( ");
		withTemp.append("    SELECT SENDERHEAD, OUTHEAD, INHEAD, SENDERBANKID, OUTBANKID, INBANKID, '1' AS ACCTCODE, PCODE, CLEARINGPHASE, NEWTXAMT FROM VW_ONBLOCKTAB ");
		withTemp.append("    WHERE (NEWRESULT = 'A' OR (NEWRESULT = 'P' AND SENDERSTATUS <> '1')) ");
		withTemp.append("    " + (StrUtils.isEmpty(condition_1)? "" : "AND " + condition_1));
		withTemp.append("    UNION ALL ");
		withTemp.append("    SELECT SENDERHEAD, OUTHEAD, INHEAD, SENDERBANKID, OUTBANKID, INBANKID, ACCTCODE, PCODE, NEWCLRPHASE, NEWTXAMT FROM VW_ONPENDING_EC ");
		withTemp.append("    " + (StrUtils.isEmpty(condition_2)? "" : "WHERE " + condition_2));
		withTemp.append(") ");
		
		StringBuffer fromAndWhere = new StringBuffer();
		StringBuffer fromAndWhere_all = new StringBuffer();
		fromAndWhere.append("FROM ( ");
		fromAndWhere.append("    SELECT * FROM ( ");
		fromAndWhere.append("        SELECT ROWNUMBER() OVER(" + parseSidx(param.get("sidx"), param.get("sord")) + ") AS ROWNUMBER , T.* FROM (");
		fromAndWhere.append("           SELECT  GET_CUR_OPBKID(SENDERHEAD,'"+param.get("DATE").trim()+"',0) AS OPBKID, SENDERHEAD AS BANKID, CLEARINGPHASE, PCODE FROM TEMP GROUP BY SENDERHEAD, CLEARINGPHASE, PCODE ");
		fromAndWhere.append("           UNION ");
		fromAndWhere.append("           SELECT  GET_CUR_OPBKID(OUTHEAD,'"+param.get("DATE").trim()+"',0) AS OPBKID, OUTHEAD, CLEARINGPHASE, PCODE FROM TEMP GROUP BY OUTHEAD, CLEARINGPHASE, PCODE ");
		fromAndWhere.append("           UNION ");
		fromAndWhere.append("           SELECT  GET_CUR_OPBKID(INHEAD,'"+param.get("DATE").trim()+"',0) AS OPBKID,  INHEAD, CLEARINGPHASE, PCODE FROM TEMP GROUP BY INHEAD, CLEARINGPHASE, PCODE ");
//		fromAndWhere.append("        SELECT ROWNUMBER() OVER(" + parseSidx(param.get("sidx"), param.get("sord")) + ") AS ROWNUMBER, GET_CUR_OPBKID(T.BANKID,'"+param.get("DATE").trim()+"',0) AS OPBKID, T.* FROM (");
//		fromAndWhere.append("           SELECT SENDERHEAD AS BANKID, CLEARINGPHASE, PCODE FROM TEMP GROUP BY SENDERHEAD, CLEARINGPHASE, PCODE ");
//		fromAndWhere.append("           UNION ");
//		fromAndWhere.append("           SELECT OUTHEAD, CLEARINGPHASE, PCODE FROM TEMP GROUP BY OUTHEAD, CLEARINGPHASE, PCODE ");
//		fromAndWhere.append("           UNION ");
//		fromAndWhere.append("           SELECT INHEAD, CLEARINGPHASE, PCODE FROM TEMP GROUP BY INHEAD, CLEARINGPHASE, PCODE ");
		fromAndWhere.append("        ) AS T ");
		fromAndWhere.append((StrUtils.isEmpty(condition_3)? "" : "WHERE " + condition_3));
		fromAndWhere_all.append(fromAndWhere);
		fromAndWhere.append("    ) WHERE ROWNUMBER >= " + (Page.getStartOfPage(pageNo, pageSize) + 1) + " AND ROWNUMBER <= " + (pageNo * pageSize) + " ");
		fromAndWhere_all.append(" ) ");
		fromAndWhere.append(") AS A ");
		fromAndWhere_all.append(") AS A ");
//		fromAndWhere.append((StrUtils.isEmpty(condition_3)? "" : "WHERE " + condition_3));
//		fromAndWhere_all.append((StrUtils.isEmpty(condition_3)? "" : "WHERE " + condition_3));
		
		//總計的SQL
		String dataSumSQL = withTemp.toString() + "SELECT SUM(DECIMAL(RECVCNT)) RECVCNT, SUM(DECIMAL(RECVAMT)) RECVAMT, SUM(DECIMAL(PAYCNT)) PAYCNT, SUM(DECIMAL(PAYAMT)) PAYAMT, SUM(DECIMAL(RVSRECVCNT)) RVSRECVCNT, SUM(DECIMAL(RVSRECVAMT)) RVSRECVAMT, SUM(DECIMAL(RVSPAYCNT)) RVSPAYCNT, SUM(DECIMAL(RVSPAYAMT)) RVSPAYAMT ";
		dataSumSQL += "FROM (" + selectStr.toString() + fromAndWhere_all.toString() + ")";
		String dataSumCols[] = {"RECVCNT", "RECVAMT", "PAYCNT", "PAYAMT", "RVSRECVCNT", "RVSRECVAMT", "RVSPAYCNT", "RVSPAYAMT"};
		System.out.println("dataSumSQL="+dataSumSQL);
		list = vw_onblocktab_Dao.dataSum(dataSumSQL.toString(),dataSumCols, ONCLEARINGTAB.class);
		rtnMap.put("dataSumList", list);
		
		//分頁的SQL
		StringBuffer countQuery = new StringBuffer();
		countQuery.append(withTemp);
		countQuery.append("SELECT COUNT(*) AS NUM ");
		countQuery.append(fromAndWhere_all);
		System.out.println("countQuery="+countQuery);
		
		//查詢的SQL
		StringBuffer sql = new StringBuffer();
		sql.append(withTemp);
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
		String cols[] = {"BANKIDANDNAME", "PCODEANDNAME", "CLEARINGPHASE", "RECVCNT", "RECVAMT", "PAYCNT", "PAYAMT", "RVSRECVCNT", "RVSRECVAMT", "RVSPAYCNT", "RVSPAYAMT"};
		//rtnMap = settle_day_Dao.queryData(Integer.valueOf(pageNo), Integer.valueOf(pageSize), dataSumSQL, sql, sql2, sql3, dataSumCols, cols, ONCLEARINGTAB.class);
		Page page = vw_onblocktab_Dao.getDataIII(pageNo, pageSize, countQuery.toString(), sql.toString(), cols, ONCLEARINGTAB.class);
		list = (List<ONCLEARINGTAB>) page.getResult();
		list = list!=null&& list.size() ==0 ?null:list;
		
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
		System.out.println("json===>"+json+"<===");
		return json;
	}
	
	public String getSQL(String date, String pcode, String opbkId, String bgbkId, String clearingPhase, String serchStrs, String sidx, String sord){
		HashMap<String, String> param = new HashMap<String, String>();
		param.put("DATE", date);
		param.put("PCODE", pcode);
		param.put("OPBK_ID", opbkId);
		param.put("BGBK_ID", bgbkId);
		param.put("CLEARINGPHASE", clearingPhase);
		List<String> conditionList = getConditionList(param);
		String condition_1 = "",condition_2 = "",condition_3 = "";
		condition_1 = conditionList.get(0);
		condition_2 = conditionList.get(1);
		condition_3 = conditionList.get(2);
		
		StringBuffer selectStr = new StringBuffer();
		selectStr.append("SELECT ");
		selectStr.append("A.BANKID || '-' || (SELECT BGBK_NAME FROM BANK_GROUP WHERE BGBK_ID = A.BANKID) AS BANKIDANDNAME, ");
		selectStr.append("VARCHAR(A.PCODE) AS PCODEANDNAME, CLEARINGPHASE, ");
		selectStr.append("(SELECT COUNT(*) FROM TEMP WHERE COALESCE(ACCTCODE,'') <> '0' AND INHEAD = A.BANKID AND PCODE = A.PCODE AND CLEARINGPHASE = A.CLEARINGPHASE) AS RECVCNT, ");
		selectStr.append(" DECIMAL ( COALESCE((SELECT SUM(NEWTXAMT) FROM TEMP WHERE COALESCE(ACCTCODE,'') <> '0' AND INHEAD = A.BANKID AND PCODE = A.PCODE AND CLEARINGPHASE = A.CLEARINGPHASE),0) ,20,0) AS RECVAMT, ");
		selectStr.append("(SELECT COUNT(*) FROM TEMP WHERE COALESCE(ACCTCODE,'') <> '0' AND OUTHEAD = A.BANKID AND PCODE = A.PCODE AND CLEARINGPHASE = A.CLEARINGPHASE) AS PAYCNT, ");
		
		selectStr.append(" DECIMAL ( COALESCE((SELECT SUM(NEWTXAMT) FROM TEMP WHERE COALESCE(ACCTCODE,'') <> '0' AND OUTHEAD = A.BANKID AND PCODE = A.PCODE AND CLEARINGPHASE = A.CLEARINGPHASE),0) ,20,0)  AS PAYAMT, ");
		selectStr.append("(SELECT COUNT(*) FROM TEMP WHERE COALESCE(ACCTCODE,'') = '0' AND OUTHEAD = A.BANKID AND PCODE = A.PCODE AND CLEARINGPHASE = A.CLEARINGPHASE) AS RVSRECVCNT, "); 
		selectStr.append(" DECIMAL ( COALESCE((SELECT ABS(SUM(NEWTXAMT)) FROM TEMP WHERE COALESCE(ACCTCODE,'') = '0' AND OUTHEAD = A.BANKID AND PCODE = A.PCODE AND CLEARINGPHASE = A.CLEARINGPHASE),0) ,20,0) AS RVSRECVAMT, ");
		selectStr.append("(SELECT COUNT(*) FROM TEMP WHERE COALESCE(ACCTCODE,'') = '0' AND INHEAD = A.BANKID AND PCODE = A.PCODE AND CLEARINGPHASE = A.CLEARINGPHASE) AS RVSPAYCNT , "); 
		selectStr.append("  DECIMAL ( COALESCE((SELECT ABS(SUM(NEWTXAMT)) FROM TEMP WHERE COALESCE(ACCTCODE,'') = '0' AND INHEAD = A.BANKID AND PCODE = A.PCODE AND CLEARINGPHASE = A.CLEARINGPHASE),0) ,20,0)  AS RVSPAYAMT ");
			
		StringBuffer withTemp = new StringBuffer();
		withTemp.append("WITH TEMP AS ( ");
		withTemp.append("    SELECT SENDERHEAD, OUTHEAD, INHEAD, SENDERBANKID, OUTBANKID, INBANKID, '1' AS ACCTCODE, PCODE, CLEARINGPHASE, NEWTXAMT FROM VW_ONBLOCKTAB ");
		withTemp.append("    WHERE (NEWRESULT = 'A' OR (NEWRESULT = 'P' AND SENDERSTATUS <> '1')) ");
		withTemp.append("    " + (StrUtils.isEmpty(condition_1)? "" : "AND " + condition_1));
		withTemp.append("    UNION ALL ");
		withTemp.append("    SELECT SENDERHEAD, OUTHEAD, INHEAD, SENDERBANKID, OUTBANKID, INBANKID, ACCTCODE, PCODE, NEWCLRPHASE, NEWTXAMT FROM VW_ONPENDING_EC ");
		withTemp.append("    " + (StrUtils.isEmpty(condition_2)? "" : "WHERE " + condition_2));
		withTemp.append(") ");
		
		StringBuffer fromAndWhere_all = new StringBuffer();
		fromAndWhere_all.append("FROM ( ");
		fromAndWhere_all.append("    SELECT * FROM ( ");
		fromAndWhere_all.append("        SELECT ROWNUMBER() OVER(" + parseSidx(param.get("sidx"), param.get("sord")) + ") AS ROWNUMBER,  T.* FROM (");
		fromAndWhere_all.append("SELECT  GET_CUR_OPBKID(SENDERHEAD,'"+param.get("DATE").trim()+"',0) AS OPBKID, SENDERHEAD AS BANKID, CLEARINGPHASE, PCODE FROM TEMP GROUP BY SENDERHEAD, CLEARINGPHASE, PCODE ");
		fromAndWhere_all.append("           UNION ");
		fromAndWhere_all.append("           SELECT  GET_CUR_OPBKID(OUTHEAD,'"+param.get("DATE").trim()+"',0) AS OPBKID, OUTHEAD, CLEARINGPHASE, PCODE FROM TEMP GROUP BY OUTHEAD, CLEARINGPHASE, PCODE ");
		fromAndWhere_all.append("           UNION ");
		fromAndWhere_all.append("           SELECT  GET_CUR_OPBKID(INHEAD,'"+param.get("DATE").trim()+"',0) AS OPBKID,  INHEAD, CLEARINGPHASE, PCODE FROM TEMP GROUP BY INHEAD, CLEARINGPHASE, PCODE ");
//		fromAndWhere_all.append("        SELECT ROWNUMBER() OVER(" + parseSidx(param.get("sidx"), param.get("sord")) + ") AS ROWNUMBER, GET_CUR_OPBKID(T.BANKID,'"+param.get("DATE").trim()+"',0) AS OPBKID, T.* FROM (");
//		fromAndWhere_all.append("           SELECT SENDERHEAD AS BANKID, CLEARINGPHASE, PCODE FROM TEMP GROUP BY SENDERHEAD, CLEARINGPHASE, PCODE ");
//		fromAndWhere_all.append("           UNION ");
//		fromAndWhere_all.append("           SELECT OUTHEAD, CLEARINGPHASE, PCODE FROM TEMP GROUP BY OUTHEAD, CLEARINGPHASE, PCODE ");
//		fromAndWhere_all.append("           UNION ");
//		fromAndWhere_all.append("           SELECT INHEAD, CLEARINGPHASE, PCODE FROM TEMP GROUP BY INHEAD, CLEARINGPHASE, PCODE ");
		fromAndWhere_all.append("        ) AS T ");
		fromAndWhere_all.append((StrUtils.isEmpty(condition_3)? "" : "WHERE " + condition_3));
		fromAndWhere_all.append("    ) ");
		fromAndWhere_all.append(") AS A ");
//		fromAndWhere_all.append((StrUtils.isEmpty(condition_3)? "" : "WHERE " + condition_3));
		
		//查詢的SQL
		StringBuffer sql = new StringBuffer();
		sql.append(withTemp);
		sql.append(selectStr);
		sql.append(fromAndWhere_all);
		if(StrUtils.isNotEmpty(sidx)){
			if(sidx.contains("ASC") || sidx.contains("DESC") ||
			   sidx.contains("asc") || sidx.contains("desc")){
				sql.append(" ORDER BY "+sidx);
			}else{
				sql.append(" ORDER BY "+sidx+" "+sord);
			}
		}
		System.out.println("sql>>"+sql);
		return sql.toString();
	}
	
	public List<String> getConditionList(Map<String, String> param){
		List<String> conditionList = new ArrayList<String>();
		
		List<String> conditions_1 = new ArrayList<String>();
		List<String> conditions_2 = new ArrayList<String>();
		List<String> conditions_3 = new ArrayList<String>();
		
		String date = "";
		//日期
		if(StrUtils.isNotEmpty(param.get("DATE").trim())){
			date = param.get("DATE").trim();
			conditions_1.add(" BIZDATE = '" + DateTimeUtils.convertDate(date, "yyyyMMdd", "yyyyMMdd") + "' ");
			conditions_2.add(" NEWBIZDATE = '" + DateTimeUtils.convertDate(date, "yyyyMMdd", "yyyyMMdd") + "' ");
		}
				
		String pcode = "";
		//交易類別
		if(StrUtils.isNotEmpty(param.get("PCODE").trim()) && !param.get("PCODE").trim().equals("all")){
			pcode = param.get("PCODE").trim();
			conditions_1.add(" PCODE = '" + pcode + "' ");
			conditions_2.add(" PCODE = '" + pcode + "' ");
			conditions_3.add(" PCODE = '" + pcode + "' ");
		}
		
		String opbkId = "";
		//操作行
		if(StrUtils.isNotEmpty(param.get("OPBK_ID").trim()) && !param.get("OPBK_ID").trim().equals("all")){
			opbkId = param.get("OPBK_ID").trim();
			conditions_1.add(" (SENDERACQUIRE = '" + opbkId + "' OR OUTACQUIRE = '" + opbkId + "' OR INACQUIRE = '" + opbkId + "') ");
			conditions_2.add(" (SENDERACQUIRE = '" + opbkId + "' OR OUTACQUIRE = '" + opbkId + "' OR INACQUIRE = '" + opbkId + "') ");
//			conditions_3.add(" A.OPBKID = '" + opbkId + "' ");
			conditions_3.add(" T.OPBKID = '" + opbkId + "' ");
		}
		
		String bgbkId = "";
		//總行代號
		if(StrUtils.isNotEmpty(param.get("BGBK_ID").trim()) && !param.get("BGBK_ID").trim().equals("all")){
			bgbkId = param.get("BGBK_ID").trim();
			conditions_1.add(" (SENDERHEAD = '" + bgbkId + "' OR OUTHEAD = '" + bgbkId + "' OR INHEAD = '" + bgbkId + "') ");
			conditions_2.add(" (SENDERHEAD = '" + bgbkId + "' OR OUTHEAD = '" + bgbkId + "' OR INHEAD = '" + bgbkId + "') ");
//			conditions_3.add(" A.BANKID = '" + bgbkId + "' ");
			conditions_3.add(" T.BANKID = '" + bgbkId + "' ");
		}
		
		String clearingPhase = "";
		//清算階段
		if(StrUtils.isNotEmpty(param.get("CLEARINGPHASE").trim()) && !param.get("CLEARINGPHASE").trim().equals("all")){
			clearingPhase = param.get("CLEARINGPHASE").trim();
			conditions_1.add(" CLEARINGPHASE = '" + clearingPhase + "' ");
			conditions_2.add(" NEWCLRPHASE = '" + clearingPhase + "' ");
			conditions_3.add(" CLEARINGPHASE = '" + clearingPhase + "' ");
		}
		
		conditionList.add( combine(conditions_1) );
		conditionList.add( combine(conditions_2) );
		conditionList.add( combine(conditions_3) );
		return conditionList;
	}
	
	public String getOrderCondition(String sidx){
		if(sidx.startsWith("BANKIDANDNAME")){
			return "BANKID";
		}else if(sidx.startsWith("CLEARINGPHASE")){
			return "CLEARINGPHASE";
		}else if(sidx.startsWith("PCODEANDNAME")){
			return "PCODE";
		}else if(sidx.startsWith("RECVCNT")){
			return "(SELECT COUNT(*) FROM TEMP WHERE COALESCE(ACCTCODE,'') <> '0' AND INBANKID = T.BANKID AND PCODE = T.PCODE)";
		}else if(sidx.startsWith("RECVAMT")){
			return "COALESCE((SELECT SUM(NEWTXAMT) FROM TEMP WHERE COALESCE(ACCTCODE,'') <> '0' AND INBANKID = T.BANKID AND PCODE = T.PCODE),0)";
		}else if(sidx.startsWith("PAYCNT")){
			return "(SELECT COUNT(*) FROM TEMP WHERE COALESCE(ACCTCODE,'') <> '0' AND OUTBANKID = T.BANKID AND PCODE = T.PCODE)";
		}else if(sidx.startsWith("PAYAMT")){
			return "COALESCE((SELECT SUM(NEWTXAMT) FROM TEMP WHERE COALESCE(ACCTCODE,'') <> '0' AND OUTBANKID = T.BANKID AND PCODE = T.PCODE),0)";
		}else if(sidx.startsWith("RVSRECVCNT")){
			return "(SELECT COUNT(*) FROM TEMP WHERE COALESCE(ACCTCODE,'') = '0' AND OUTBANKID = T.BANKID AND PCODE = T.PCODE)";
		}else if(sidx.startsWith("RVSRECVAMT")){
			return "COALESCE((SELECT ABS(SUM(NEWTXAMT)) FROM TEMP WHERE COALESCE(ACCTCODE,'') = '0' AND OUTBANKID = T.BANKID AND PCODE = T.PCODE),0)";
		}else if(sidx.startsWith("RVSPAYCNT")){
			return "(SELECT COUNT(*) FROM TEMP WHERE COALESCE(ACCTCODE,'') = '0' AND INBANKID = T.BANKID AND PCODE = T.PCODE)";
		}else if(sidx.startsWith("RVSPAYAMT")){
			return "COALESCE((SELECT ABS(SUM(NEWTXAMT)) FROM TEMP WHERE COALESCE(ACCTCODE,'') = '0' AND INBANKID = T.BANKID AND PCODE = T.PCODE),0)";
		}else{
			return sidx;
		}
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
	
	public EACH_TXN_CODE_Dao getEach_txn_code_Dao() {
		return each_txn_code_Dao;
	}
	public void setEach_txn_code_Dao(EACH_TXN_CODE_Dao each_txn_code_Dao) {
		this.each_txn_code_Dao = each_txn_code_Dao;
	}
	public BANK_GROUP_Dao getBank_group_Dao() {
		return bank_group_Dao;
	}
	public void setBank_group_Dao(BANK_GROUP_Dao bank_group_Dao) {
		this.bank_group_Dao = bank_group_Dao;
	}
	public SETTLE_DAY_Dao getSettle_day_Dao() {
		return settle_day_Dao;
	}
	public void setSettle_day_Dao(SETTLE_DAY_Dao settle_day_Dao) {
		this.settle_day_Dao = settle_day_Dao;
	}
	public VW_ONBLOCKTAB_Dao getVw_onblocktab_Dao() {
		return vw_onblocktab_Dao;
	}
	public void setVw_onblocktab_Dao(VW_ONBLOCKTAB_Dao vw_onblocktab_Dao) {
		this.vw_onblocktab_Dao = vw_onblocktab_Dao;
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
