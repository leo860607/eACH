package tw.org.twntch.bo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.struts.util.LabelValueBean;

import tw.org.twntch.db.dao.hibernate.BANK_BRANCH_Dao;
import tw.org.twntch.db.dao.hibernate.BANK_GROUP_Dao;
import tw.org.twntch.db.dao.hibernate.EACH_TXN_CODE_Dao;
import tw.org.twntch.db.dao.hibernate.ONBLOCKTAB_Dao;
import tw.org.twntch.db.dao.hibernate.Page;
import tw.org.twntch.db.dao.hibernate.RPONBLOCKTAB_Dao;
import tw.org.twntch.po.BANK_GROUP;
import tw.org.twntch.po.EACH_TXN_CODE;
import tw.org.twntch.po.ONCLEARINGTAB;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.RptUtils;
import tw.org.twntch.util.StrUtils;

public class CLEAR_DATA_BO {
	private ONBLOCKTAB_Dao onblocktab_Dao;
	private BANK_GROUP_Dao bank_group_Dao;
	private EACH_TXN_CODE_Dao each_txn_code_Dao;
	private BANK_BRANCH_Dao bank_branch_Dao;
	private RPONBLOCKTAB_Dao rponblocktab_Dao;
	
	/**
	 * 取得操作行代號清單
	 * select distinct OPBK_ID where Currentdate between 啟用日期 and 停用日期
	 * @return
	 */
	public List<LabelValueBean> getOpbkIdList(){
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
	
	public String pageSearch(Map<String, String> param){
		int pageNo = StrUtils.isEmpty(param.get("page")) ?0:Integer.valueOf(param.get("page"));
		int pageSize = StrUtils.isEmpty(param.get("rows")) ?Integer.valueOf(Arguments.getStringArg("PAGE.SIZE")):Integer.valueOf(param.get("rows"));
		
		Map rtnMap = new HashMap();
		
		List<ONCLEARINGTAB> list = null;
		Page page = null;
		try {
			list = new ArrayList<ONCLEARINGTAB>();
			String condition_1 = "";
			
			List<String> conditionList = getConditionList(param);
			condition_1 = conditionList.get(0);
			
			StringBuffer fromAndWhere = new StringBuffer();
			StringBuffer fromAndWhere_all = new StringBuffer();
			fromAndWhere.append("FROM ( ");
//			fromAndWhere.append("	   SELECT ROWNUMBER() OVER(" + parseSidx(param.get("sidx"), param.get("sord")) + ") AS ROWNUMBER, T.* ");
			fromAndWhere.append("	   SELECT /* ROWNUMBER() OVER(" + parseSidx(param.get("sidx"), param.get("sord")) + ") AS ROWNUMBER, */ T.* ");
			fromAndWhere.append("	   FROM ONCLEARINGTAB AS T ");
			fromAndWhere.append((StrUtils.isEmpty(condition_1)? "" : "WHERE " + condition_1));
			fromAndWhere.append(") AS A ");
			fromAndWhere_all.append(fromAndWhere);
//			fromAndWhere.append("  WHERE ROWNUMBER >= " + (Page.getStartOfPage(pageNo, pageSize) + 1) + " AND ROWNUMBER <= " + (pageNo * pageSize) + " ");
			fromAndWhere.append(" /* WHERE ROWNUMBER >= " + (Page.getStartOfPage(pageNo, pageSize) + 1) + " AND ROWNUMBER <= " + (pageNo * pageSize) + " */ ");
			System.out.println("fromAndWhere >> " + fromAndWhere);
			/*
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
			*/
//			rtnMap.put("dataSumList", list);
			
			String cols[] = {"BIZDATE", "Format_BIZDATE", "CLEARINGPHASE", "Format_CLEARINGPHASE", "BANKID", "BANKIDANDNAME", "PCODE", "PCODEANDNAME","TOTAL_RECV_PAY_CNT","TOTAL_RECV_PAY_AMT","TOTAL_RECV_PAY_FEE_CNT","TOTAL_RECV_PAY_FEE_AMT" ,"ALL_TOTAL_RECV_PAY_CNT" , "ALL_TOTAL_RECV_PAY_AMT" , "ALL_TOTAL_RECV_PAY_FEE_CNT" , "ALL_TOTAL_RECV_PAY_FEE_AMT" };
			StringBuffer countQuery = new StringBuffer();
			countQuery.append("SELECT COUNT(*) AS NUM ");
			countQuery.append(fromAndWhere_all);
			//System.out.println("countQuery = " + countQuery.toString().toUpperCase());
			
			StringBuffer sql = new StringBuffer();
			
			sql.append("WITH TEMP AS ( ");
//			sql.append("SELECT VARCHAR(BIZDATE) AS BIZDATE, TRANSLATE('abcd-ef-gh',VARCHAR(BIZDATE),'abcdefgh') AS format_BIZDATE, ");
			sql.append("SELECT VARCHAR(BIZDATE) AS BIZDATE, TRANSLATE('abcd-ef-gh',VARCHAR(TYPHBIZDATE),'abcdefgh') AS format_BIZDATE, ");
			sql.append("VARCHAR(CLEARINGPHASE) AS CLEARINGPHASE, ");
			sql.append("VARCHAR(TYPHCLEARINGPHASE) AS format_CLEARINGPHASE, ");
			sql.append("VARCHAR(BANKID) AS BANKID, BANKID || '-' || (SELECT BGBK_NAME FROM BANK_GROUP WHERE BGBK_ID = BANKID) AS BANKIDANDNAME, ");
			sql.append("VARCHAR(PCODE) AS PCODE, VARCHAR(PCODE) || '-' || (SELECT EACH_TXN_NAME FROM EACH_TXN_CODE WHERE EACH_TXN_ID = PCODE) AS PCODEANDNAME, ");
			sql.append("(SELECT SUM(RECVCNT)+SUM(PAYCNT)+SUM(RVSRECVCNT)+SUM(RVSPAYCNT) FROM ONCLEARINGTAB AS B WHERE B.BIZDATE = A.BIZDATE AND B.CLEARINGPHASE = A.CLEARINGPHASE AND B.BANKID = A.BANKID AND B.PCODE = A.PCODE) AS TOTAL_RECV_PAY_CNT, ");
//			20150429 edit by hugo req by 李建利 [應收+應收(沖正)]-[應付+應付[沖正]] 手續費亦同 ,手續費筆數，票交筆數不列入計算避免資料重覆
//			sql.append("(SELECT SUM(RECVAMT)+SUM(PAYAMT)+SUM(RVSRECVAMT)+SUM(RVSPAYAMT) FROM ONCLEARINGTAB AS B WHERE B.BIZDATE = A.BIZDATE AND B.CLEARINGPHASE = A.CLEARINGPHASE AND B.BANKID = A.BANKID AND B.PCODE = A.PCODE) AS TOTAL_RECV_PAY_AMT, ");
			sql.append("(SELECT (SUM(RECVAMT)+SUM(RVSRECVAMT) )-(SUM(PAYAMT) + SUM(RVSPAYAMT)) FROM ONCLEARINGTAB AS B WHERE B.BIZDATE = A.BIZDATE AND B.CLEARINGPHASE = A.CLEARINGPHASE AND B.BANKID = A.BANKID AND B.PCODE = A.PCODE) AS TOTAL_RECV_PAY_AMT, ");
//			sql.append("(SELECT SUM(RECVMBFEECNT)+SUM(PAYMBFEECNT)+SUM(PAYEACHFEECNT)+SUM(RVSRECVMBFEECNT)+SUM(RVSPAYMBFEECNT)+SUM(RVSRECVEACHFEECNT) FROM ONCLEARINGTAB AS B WHERE B.BIZDATE = A.BIZDATE AND B.CLEARINGPHASE = A.CLEARINGPHASE AND B.BANKID = A.BANKID AND B.PCODE = A.PCODE) AS TOTAL_RECV_PAY_FEE_CNT, ");
//			sql.append("(SELECT SUM(RECVMBFEEAMT)+SUM(PAYMBFEEAMT)+SUM(PAYEACHFEEAMT)+SUM(RVSRECVMBFEEAMT)+SUM(RVSPAYMBFEEAMT)+SUM(RVSRECVEACHFEEAMT) FROM ONCLEARINGTAB AS B WHERE B.BIZDATE = A.BIZDATE AND B.CLEARINGPHASE = A.CLEARINGPHASE AND B.BANKID = A.BANKID AND B.PCODE = A.PCODE) AS TOTAL_RECV_PAY_FEE_AMT ");
			sql.append("(SELECT SUM(RECVMBFEECNT)+SUM(PAYMBFEECNT)+SUM(RVSRECVMBFEECNT)+SUM(RVSPAYMBFEECNT) FROM ONCLEARINGTAB AS B WHERE B.BIZDATE = A.BIZDATE AND B.CLEARINGPHASE = A.CLEARINGPHASE AND B.BANKID = A.BANKID AND B.PCODE = A.PCODE) AS TOTAL_RECV_PAY_FEE_CNT, ");
			sql.append("(SELECT( SUM(RECVMBFEEAMT)+SUM(RVSRECVMBFEEAMT)+SUM(RVSRECVEACHFEEAMT))-(SUM(PAYMBFEEAMT)+SUM(PAYEACHFEEAMT)+SUM(RVSPAYMBFEEAMT)) FROM ONCLEARINGTAB AS B WHERE B.BIZDATE = A.BIZDATE AND B.CLEARINGPHASE = A.CLEARINGPHASE AND B.BANKID = A.BANKID AND B.PCODE = A.PCODE) AS TOTAL_RECV_PAY_FEE_AMT ");
			sql.append(fromAndWhere);
			if(StrUtils.isNotEmpty(param.get("sidx"))){
				if(param.get("sidx").contains("ASC") || param.get("sidx").contains("DESC") ||
				   param.get("sidx").contains("asc") || param.get("sidx").contains("desc")){
					sql.append(" ORDER BY "+param.get("sidx"));
				}else{
					sql.append(" ORDER BY "+param.get("sidx")+" "+param.get("sord"));
				}
			}
			
			sql.append(" )");
			sql.append(" SELECT A.* ");
			sql.append(" ,(SELECT SUM(TOTAL_RECV_PAY_CNT) FROM TEMP) AS ALL_TOTAL_RECV_PAY_CNT ");
			sql.append(" ,(SELECT SUM(TOTAL_RECV_PAY_AMT) FROM TEMP) AS ALL_TOTAL_RECV_PAY_AMT ");
			sql.append(" ,(SELECT SUM(TOTAL_RECV_PAY_FEE_CNT) FROM TEMP) AS ALL_TOTAL_RECV_PAY_FEE_CNT ");
			sql.append(" ,(SELECT SUM(TOTAL_RECV_PAY_FEE_AMT) FROM TEMP) AS ALL_TOTAL_RECV_PAY_FEE_AMT ");
			sql.append("  FROM TEMP A ");
			
			System.out.println("totalcountsql==>" + sql.toString().toUpperCase());
			page = onblocktab_Dao.getDataIIII(pageNo, pageSize, countQuery.toString().toUpperCase(), sql.toString().toUpperCase(), cols, ONCLEARINGTAB.class);
			list = (List<ONCLEARINGTAB>) page.getResult();
			rtnMap.put("dataSumList", list);
			
			String tmpString = "";
			tmpString = sql.toString().replace("/*", " ");
			tmpString = tmpString.replace("*/", " ");
			System.out.println("tmpString==>" + tmpString.toString().toUpperCase());
			page = onblocktab_Dao.getDataIIII(pageNo, pageSize, countQuery.toString().toUpperCase(), tmpString, cols, ONCLEARINGTAB.class);
			list = (List<ONCLEARINGTAB>) page.getResult();
			//System.out.println("list>>"+list);
			list = list!=null&& list.size() ==0 ?new ArrayList<ONCLEARINGTAB>():list;
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
		System.out.println("JSON >> " + json);
		return json;
	}
	
	public Map<String, String> qs_ex_export(String startDate, String endDate, String pcode, String opbkId, String ctbkId, String bgbkId, String clearingPhase, String serchStrs, String sortname, String sortorder){
		Map<String, String> rtnMap = null;
		
		try{
			rtnMap = new HashMap<String, String>();
			rtnMap.put("serchStrs", serchStrs);
			Map<String, Object> params = new HashMap<String, Object>();
			String sql = getSQL(startDate, endDate, pcode, opbkId, ctbkId, bgbkId, clearingPhase, serchStrs, sortname, sortorder);
			List list = rponblocktab_Dao.getRptData(sql, new ArrayList());
			
			int pathType = Arguments.getStringArg("ISWINOPEN").equals("Y")?RptUtils.C_PATH :RptUtils.R_PATH;
			String outputFilePath = RptUtils.export(RptUtils.COLLECTION,pathType, "clear_data", "clear_data", params, list, 2);
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
	
	public String getSQL(String startDate, String endDate, String pcode, String opbkId, String ctbkId, String bgbkId, String clearingPhase, String serchStrs, String sidx, String sord){
		Map<String, String> params = new HashMap<String, String>();
		params.put("START_DATE", startDate);
		params.put("END_DATE", endDate);
		params.put("PCODE", pcode);
		params.put("OPBK_ID", opbkId);
		params.put("CTBK_ID", ctbkId);
		params.put("BGBK_ID", bgbkId);
		params.put("CLEARINGPHASE", clearingPhase);
		List<String> conditionList = getConditionList(params);
		String condition_1 = conditionList.get(0);
		
		StringBuffer fromAndWhere = new StringBuffer();
		fromAndWhere.append("FROM ( ");
		fromAndWhere.append("	   SELECT /* ROWNUMBER() OVER(" + parseSidx(sidx, sord) + ") AS ROWNUMBER, */ T.* ");
		fromAndWhere.append("	   FROM ONCLEARINGTAB AS T ");
		fromAndWhere.append((StrUtils.isEmpty(condition_1)? "" : "WHERE " + condition_1));
		fromAndWhere.append(") AS A ");
		
		StringBuffer sql = new StringBuffer();
		sql.append("WITH TEMP AS ( ");
//		sql.append("SELECT VARCHAR(BIZDATE) AS BIZDATE, TRANSLATE('abcd-ef-gh',VARCHAR(BIZDATE),'abcdefgh') AS format_BIZDATE, ");
		sql.append("SELECT VARCHAR(TYPHBIZDATE) AS BIZDATE, TRANSLATE('abcd-ef-gh',VARCHAR(TYPHBIZDATE),'abcdefgh') AS format_BIZDATE, ");
//		sql.append("VARCHAR(CLEARINGPHASE) AS CLEARINGPHASE, ");
		sql.append("VARCHAR(TYPHCLEARINGPHASE) AS CLEARINGPHASE, ");
		sql.append("VARCHAR(BANKID) AS BANKID, BANKID || '-' || (SELECT BGBK_NAME FROM BANK_GROUP WHERE BGBK_ID = BANKID) AS BANKIDANDNAME, ");
		sql.append("VARCHAR(PCODE) AS PCODE, VARCHAR(PCODE) || '-' || (SELECT EACH_TXN_NAME FROM EACH_TXN_CODE WHERE EACH_TXN_ID = PCODE) AS PCODEANDNAME, ");
		sql.append("(SELECT SUM(RECVCNT)+SUM(PAYCNT)+SUM(RVSRECVCNT)+SUM(RVSPAYCNT) FROM ONCLEARINGTAB AS B WHERE B.BIZDATE = A.BIZDATE AND B.CLEARINGPHASE = A.CLEARINGPHASE AND B.BANKID = A.BANKID AND B.PCODE = A.PCODE) AS TOTAL_RECV_PAY_CNT, ");
//		20150429 edit by hugo req by 李建利 [應收+應收(沖正)]-[應付+應付[沖正]] 手續費亦同 ,手續費筆數，票交筆數不列入計算避免資料重覆
//		sql.append("(SELECT SUM(RECVAMT)+SUM(PAYAMT)+SUM(RVSRECVAMT)+SUM(RVSPAYAMT) FROM ONCLEARINGTAB AS B WHERE B.BIZDATE = A.BIZDATE AND B.CLEARINGPHASE = A.CLEARINGPHASE AND B.BANKID = A.BANKID AND B.PCODE = A.PCODE) AS TOTAL_RECV_PAY_AMT, ");
		sql.append("DECIMAL((SELECT (SUM(RECVAMT)+SUM(RVSRECVAMT) )-(SUM(PAYAMT) + SUM(RVSPAYAMT)) FROM ONCLEARINGTAB AS B WHERE B.BIZDATE = A.BIZDATE AND B.CLEARINGPHASE = A.CLEARINGPHASE AND B.BANKID = A.BANKID AND B.PCODE = A.PCODE),15,2) AS TOTAL_RECV_PAY_AMT, ");
//		sql.append("(SELECT SUM(RECVMBFEECNT)+SUM(PAYMBFEECNT)+SUM(PAYEACHFEECNT)+SUM(RVSRECVMBFEECNT)+SUM(RVSPAYMBFEECNT)+SUM(RVSRECVEACHFEECNT) FROM ONCLEARINGTAB AS B WHERE B.BIZDATE = A.BIZDATE AND B.CLEARINGPHASE = A.CLEARINGPHASE AND B.BANKID = A.BANKID AND B.PCODE = A.PCODE) AS TOTAL_RECV_PAY_FEE_CNT, ");
//		sql.append("(SELECT SUM(RECVMBFEEAMT)+SUM(PAYMBFEEAMT)+SUM(PAYEACHFEEAMT)+SUM(RVSRECVMBFEEAMT)+SUM(RVSPAYMBFEEAMT)+SUM(RVSRECVEACHFEEAMT) FROM ONCLEARINGTAB AS B WHERE B.BIZDATE = A.BIZDATE AND B.CLEARINGPHASE = A.CLEARINGPHASE AND B.BANKID = A.BANKID AND B.PCODE = A.PCODE) AS TOTAL_RECV_PAY_FEE_AMT ");
		sql.append("(SELECT SUM(RECVMBFEECNT)+SUM(PAYMBFEECNT)+SUM(RVSRECVMBFEECNT)+SUM(RVSPAYMBFEECNT) FROM ONCLEARINGTAB AS B WHERE B.BIZDATE = A.BIZDATE AND B.CLEARINGPHASE = A.CLEARINGPHASE AND B.BANKID = A.BANKID AND B.PCODE = A.PCODE) AS TOTAL_RECV_PAY_FEE_CNT, ");
		sql.append("DECIMAL((SELECT( SUM(RECVMBFEEAMT)+SUM(RVSRECVMBFEEAMT)+SUM(RVSRECVEACHFEEAMT))-(SUM(PAYMBFEEAMT)+SUM(PAYEACHFEEAMT)+SUM(RVSPAYMBFEEAMT)) FROM ONCLEARINGTAB AS B WHERE B.BIZDATE = A.BIZDATE AND B.CLEARINGPHASE = A.CLEARINGPHASE AND B.BANKID = A.BANKID AND B.PCODE = A.PCODE),15,2) AS TOTAL_RECV_PAY_FEE_AMT ");
		sql.append(fromAndWhere);
		if(StrUtils.isNotEmpty(sidx)){
			if(sidx.contains("ASC") || sidx.contains("DESC") ||
			   sidx.contains("asc") || sidx.contains("desc")){
				sql.append(" ORDER BY "+sidx);
			}else{
				sql.append(" ORDER BY "+sidx+" "+sord);
			}
		}
		
		sql.append(" )");
		sql.append(" SELECT A.* ");
		sql.append(" ,(SELECT SUM(TOTAL_RECV_PAY_CNT) FROM TEMP) AS ALL_TOTAL_RECV_PAY_CNT ");
		sql.append(" ,(SELECT SUM(TOTAL_RECV_PAY_AMT) FROM TEMP) AS ALL_TOTAL_RECV_PAY_AMT ");
		sql.append(" ,(SELECT SUM(TOTAL_RECV_PAY_FEE_CNT) FROM TEMP) AS ALL_TOTAL_RECV_PAY_FEE_CNT ");
		sql.append(" ,(SELECT SUM(TOTAL_RECV_PAY_FEE_AMT) FROM TEMP) AS ALL_TOTAL_RECV_PAY_FEE_AMT ");
		sql.append("  FROM TEMP A ");
		
		String tmpString = "";
		tmpString = sql.toString().replace("/*", " ");
		tmpString = tmpString.replace("*/", " ");
		
		return tmpString;
	}
	
	public Map getDetail(String bizdate, String clearingphase, String bankid, String pcode){
		List<String> conditions_1 = new ArrayList<String>();
		//營業日
		if(StrUtils.isNotEmpty(bizdate)){
//			20150605 edit by hugo 因應颱風天且此表格TYPHBIZDATE為PK不可修改，所以改抓TYPHBIZDATE
			conditions_1.add(" BIZDATE = '" + bizdate + "' ");
//			conditions_1.add(" TYPHBIZDATE = '" + bizdate + "' ");
		}
		//交易類別
		if(StrUtils.isNotEmpty(pcode) && !pcode.equals("all")){
			conditions_1.add(" PCODE = '" + pcode + "' ");
		}
		//總行代號
		if(StrUtils.isNotEmpty(bankid) && !bankid.equals("all")){
			conditions_1.add(" BANKID = '" + bankid + "' ");
		}
		//清算階段
		if(StrUtils.isNotEmpty(clearingphase) && !clearingphase.equals("all")){
//			20150605 edit by hugo 因應颱風天且此表格TYPHBIZDATE為PK不可修改，所以改抓TYPHBIZDATE
			conditions_1.add(" CLEARINGPHASE = '" + clearingphase + "' ");
//			conditions_1.add(" TYPHCLEARINGPHASE = '" + clearingphase + "' ");
		}		
		String condition = combine(conditions_1);
//		20150605 edit by hugo 因應颱風天且此表格TYPHBIZDATE 、TYPHCLEARINGPHASE   PK不可修改，所以改抓TYPHBIZDATE 、TYPHCLEARINGPHASE
//		String cols = "TRANSLATE('abcd-ef-gh', VARCHAR(BIZDATE), 'abcdefgh') AS BIZDATE,VARCHAR(CLEARINGPHASE) AS CLEARINGPHASE,VARCHAR(BANKID) || '-' || (SELECT BGBK_NAME FROM BANK_GROUP WHERE BGBK_ID = BANKID) AS BANKID,VARCHAR(PCODE) || '-' || (SELECT EACH_TXN_NAME FROM EACH_TXN_CODE WHERE EACH_TXN_ID = PCODE) AS PCODE,";
		String cols = "TRANSLATE('abcd-ef-gh', VARCHAR(TYPHBIZDATE), 'abcdefgh') AS BIZDATE,VARCHAR(TYPHCLEARINGPHASE) AS CLEARINGPHASE,VARCHAR(BANKID) || '-' || (SELECT BGBK_NAME FROM BANK_GROUP WHERE BGBK_ID = BANKID) AS BANKID,VARCHAR(PCODE) || '-' || (SELECT EACH_TXN_NAME FROM EACH_TXN_CODE WHERE EACH_TXN_ID = PCODE) AS PCODE,";
		cols += "RECVCNT,RECVAMT,PAYCNT,PAYAMT,RECVMBFEECNT,RECVMBFEEAMT,PAYMBFEECNT,PAYMBFEEAMT,PAYEACHFEECNT,PAYEACHFEEAMT,RVSRECVCNT,RVSRECVAMT,RVSPAYCNT,RVSPAYAMT,RVSRECVMBFEECNT,RVSRECVMBFEEAMT,RVSRECVEACHFEECNT,RVSRECVEACHFEEAMT,RVSPAYMBFEECNT,RVSPAYMBFEEAMT";
		
		return onblocktab_Dao.getDetail("SELECT " + cols + " FROM ONCLEARINGTAB " + (StrUtils.isEmpty(condition)?"" : "WHERE " + condition), "", "");
	}
	
	public String getOrderCondition(String sidx){
		if(sidx.startsWith("BANKIDANDNAME")){
			return "BANKID";
		}else{
			return sidx;
		}
	}
	
	public List<String> getConditionList(Map<String, String> param){
		List<String> conditionList = new ArrayList<String>();
		
		List<String> conditions_1 = new ArrayList<String>();
		//營業日
		String startDate = "";
		if(StrUtils.isNotEmpty(param.get("START_DATE").trim())){
		startDate = param.get("START_DATE").trim();
//		20150605 edit by hugo 因應颱風天且此表格BIZDATE為PK不可修改，所以改抓TYPHBIZDATE
//		conditions_1.add(" BIZDATE = '" + DateTimeUtils.convertDate(bizDate, "yyyyMMdd", "yyyyMMdd") + "' ");
		conditions_1.add(" TYPHBIZDATE >= '" + DateTimeUtils.convertDate(startDate, "yyyyMMdd", "yyyyMMdd") + "' ");
		}
		
		//營業日
		String endDate = "";
		if(StrUtils.isNotEmpty(param.get("END_DATE").trim())){
		endDate = param.get("END_DATE").trim();
//		20150605 edit by hugo 因應颱風天且此表格BIZDATE為PK不可修改，所以改抓TYPHBIZDATE
//		conditions_1.add(" BIZDATE = '" + DateTimeUtils.convertDate(bizDate, "yyyyMMdd", "yyyyMMdd") + "' ");
		conditions_1.add(" TYPHBIZDATE <= '" + DateTimeUtils.convertDate(endDate, "yyyyMMdd", "yyyyMMdd") + "' ");
		}				
				
		//交易類別
		String pcode = "";
		if(StrUtils.isNotEmpty(param.get("PCODE").trim()) && !param.get("PCODE").trim().equals("all")){
			pcode = param.get("PCODE").trim();
			conditions_1.add(" PCODE = '" + pcode + "' ");
		}
		//操作行代號
		String opbkId = "";
		if(StrUtils.isNotEmpty(param.get("OPBK_ID")) && !param.get("OPBK_ID").trim().equals("all")){
			opbkId = param.get("OPBK_ID").trim();
			//conditions_1.add(" (SELECT OPBK_ID FROM BANK_GROUP WHERE BGBK_ID = T.BANKID) = '" + opbkId + "' ");
			//conditions_1.add(" (SELECT OPBK_ID FROM BANK_OPBK WHERE BGBK_ID = T.BANKID AND OPBK_ID = '" + opbkId + "') = '" + opbkId + "' ");
			conditions_1.add(" GET_CUR_OPBKID(T.BANKID, TRANS_DATE(T.BIZDATE,'T',''), 0) = '" + opbkId + "' ");
		}
		//清算行代號
		String ctbkId = "";
		if(StrUtils.isNotEmpty(param.get("CTBK_ID")) && !param.get("CTBK_ID").trim().equals("all")){
			ctbkId = param.get("CTBK_ID").trim();
			//conditions_1.add(" (SELECT CTBK_ID FROM BANK_GROUP WHERE BGBK_ID = T.BANKID) = '" + ctbkId + "' ");
			conditions_1.add(" GET_CUR_CTBKID(T.BANKID, TRANS_DATE(T.BIZDATE,'T',''), 0) = '" + ctbkId + "' ");
		}
		//總行代號
		String bgbkId = "";
		if(StrUtils.isNotEmpty(param.get("BGBK_ID").trim()) && !param.get("BGBK_ID").trim().equals("all")){
			bgbkId = param.get("BGBK_ID").trim();
			conditions_1.add(" BANKID = '" + bgbkId + "' ");
		}
		//清算階段
		String clearingPhase = "";
		if(StrUtils.isNotEmpty(param.get("CLEARINGPHASE").trim()) && !param.get("CLEARINGPHASE").trim().equals("all")){
			clearingPhase = param.get("CLEARINGPHASE").trim();
//			20150605 edit by hugo 因應颱風天且此表格CLEARINGPHASE為PK不可修改，所以改抓TYPHCLEARINGPHASE
//			conditions_1.add(" CLEARINGPHASE = '" + clearingPhase + "' ");
			conditions_1.add(" TYPHCLEARINGPHASE = '" + clearingPhase + "' ");
		}
		
		conditionList.add( combine(conditions_1) );
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
	public RPONBLOCKTAB_Dao getRponblocktab_Dao() {
		return rponblocktab_Dao;
	}
	public void setRponblocktab_Dao(RPONBLOCKTAB_Dao rponblocktab_Dao) {
		this.rponblocktab_Dao = rponblocktab_Dao;
	}
}
