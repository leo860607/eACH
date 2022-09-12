package tw.org.twntch.bo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.struts.util.LabelValueBean;

import com.google.gson.Gson;

import tw.org.twntch.bean.FEE_SEARCH;
import tw.org.twntch.bean.TXS_DAY;
import tw.org.twntch.db.dao.hibernate.BANK_BRANCH_Dao;
import tw.org.twntch.db.dao.hibernate.BANK_GROUP_Dao;
import tw.org.twntch.db.dao.hibernate.EACH_TXN_CODE_Dao;
import tw.org.twntch.db.dao.hibernate.ONBLOCKTAB_Dao;
import tw.org.twntch.db.dao.hibernate.Page;
import tw.org.twntch.db.dao.hibernate.RPONBLOCKTAB_Dao;
import tw.org.twntch.po.BANK_BRANCH;
import tw.org.twntch.po.BANK_GROUP;
import tw.org.twntch.po.EACH_TXN_CODE;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.RptUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.zDateHandler;

public class FEE_SEARCH_BO {
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
	
	/* 20141219 建立新的view來做查詢的方式，暫不使用
	public String pageSearch(Map<String, String> param){
		List<String> conditions = new ArrayList<String>();
		
		String startDate = "";
		if(StrUtils.isNotEmpty(param.get("START_DATE").trim())){
			startDate = param.get("START_DATE").trim();
			conditions.add(" A.TXDT >= '" + DateTimeUtils.convertDate(startDate, "yyyyMMdd", "yyyyMMdd") + "000000' ");
		}
				
		String endDate = "";
		if(StrUtils.isNotEmpty(param.get("END_DATE").trim())){
			endDate = param.get("END_DATE").trim();
			conditions.add(" A.TXDT <= '" + DateTimeUtils.convertDate(endDate, "yyyyMMdd", "yyyyMMdd") + "235959' ");
		}
		
		String pcode = "";
		if(StrUtils.isNotEmpty(param.get("PCODE").trim()) && !param.get("PCODE").trim().equals("all")){
			pcode = param.get("PCODE").trim();
			conditions.add(" A.PCODE = '" + pcode + "' ");
		}
		
		String opbkId = "";
		if(StrUtils.isNotEmpty(param.get("OPBK_ID").trim()) && !param.get("OPBK_ID").trim().equals("all")){
			opbkId = param.get("OPBK_ID").trim();
			conditions.add(" A.SENDERACQUIRE = '" + opbkId + "' ");
		}
		
		String brbkId = "";
		if(StrUtils.isNotEmpty(param.get("BRBK_ID").trim()) && !param.get("BRBK_ID").trim().equals("all")){
			brbkId = param.get("BRBK_ID").trim();
			conditions.add(" A.SENDERPAYBANKID = '" + param.get("BRBK_ID") + "' ");
		}
		
		String clearingPhase = "";
		if(StrUtils.isNotEmpty(param.get("CLEARINGPHASE").trim()) && !param.get("CLEARINGPHASE").trim().equals("all")){
			clearingPhase = param.get("CLEARINGPHASE").trim();
			conditions.add(" A.CLEARINGPHASE = '" + clearingPhase + "' ");
		}
		
		String pageNo = StrUtils.isEmpty(param.get("page")) ?"0":param.get("page");
		String pageSize = StrUtils.isEmpty(param.get("rows")) ?Arguments.getStringArg("PAGE.SIZE"):param.get("rows");
		
		Map rtnMap = new HashMap();
		
		List<FEE_SEARCH> list = null;
		Page page = null;
		try {
			list = new ArrayList<FEE_SEARCH>();
			String condition = "";
			for(int i = 0; i < conditions.size(); i++){
				condition += conditions.get(i);
				if(i < conditions.size() - 1){
					condition += " AND ";
				}
			}
			
			StringBuffer sql1 = new StringBuffer();
			StringBuffer sql2 = new StringBuffer();
			String cols[] = {"PCODE", "SENDERACQUIRE", "SENDERPAYBANKID", "FIRECOUNT", "FIREAMT", "DEBITCOUNT", "DEBITAMT", "SAVECOUNT", "SAVEAMT"};
			
			sql1.append("SELECT * ");
			sql1.append("FROM FEE_SEARCH_VIEW A ");
			sql1.append("WHERE (A.RESULTSTATUS = 'A' OR (A.RESULTSTATUS = 'P' AND A.RESULTCODE = '00') ) " + (StrUtils.isEmpty(condition)? "" : "AND " + condition));
			sql1.append("GROUP BY A.PCODE, A.SENDERACQUIRE, A.SENDERPAYBANKID ");
			
			sql2.append("SELECT "); 
			sql2.append("A.PCODE || '-' || ( SELECT COALESCE(EACH_TXN_NAME,'') FROM EACH_TXN_CODE WHERE EACH_TXN_ID=A.PCODE) PCODE, ");
			sql2.append("A.SENDERACQUIRE || '-' || (SELECT COALESCE(BGBK_NAME,'') FROM BANK_GROUP WHERE BGBK_ID=A.SENDERACQUIRE) SENDERACQUIRE, ");
			sql2.append("A.SENDERPAYBANKID || '-' || (SELECT COALESCE(BRBK_NAME,'') FROM BANK_BRANCH WHERE BRBK_ID=A.SENDERPAYBANKID) SENDERPAYBANKID, ");
			sql2.append("COUNT(*) FIRECOUNT, SUM(DECIMAL(A.SENDERFEE)) FIREAMT, ");
			sql2.append("(SELECT COUNT(*) FROM FEE_SEARCH_VIEW B WHERE B.PCODE=A.PCODE AND B.SENDERACQUIRE=A.SENDERACQUIRE AND B.SENDERPAYBANKID=A.SENDERPAYBANKID AND LENGTH(RC2) > 0) DEBITCOUNT, ");
			sql2.append("(SELECT SUM(DECIMAL(B.INFEE)) FROM FEE_SEARCH_VIEW B WHERE B.PCODE=A.PCODE AND B.SENDERACQUIRE=A.SENDERACQUIRE AND B.SENDERPAYBANKID=A.SENDERPAYBANKID AND LENGTH(RC2) > 0) DEBITAMT, ");
			sql2.append("(SELECT COUNT(*) FROM FEE_SEARCH_VIEW B WHERE B.PCODE=A.PCODE AND B.SENDERACQUIRE=A.SENDERACQUIRE AND B.SENDERPAYBANKID=A.SENDERPAYBANKID AND LENGTH(RC3) > 0) SAVECOUNT, ");
			sql2.append("(SELECT SUM(DECIMAL(B.OUTFEE)) FROM FEE_SEARCH_VIEW B WHERE B.PCODE=A.PCODE AND B.SENDERACQUIRE=A.SENDERACQUIRE AND B.SENDERPAYBANKID=A.SENDERPAYBANKID AND LENGTH(RC3) > 0) SAVEAMT ");
			sql2.append("FROM FEE_SEARCH_VIEW A ");
			sql2.append("WHERE (A.RESULTSTATUS = 'A' OR (A.RESULTSTATUS = 'P' AND A.RESULTCODE = '00') ) " + (StrUtils.isEmpty(condition)? "" : "AND " + condition));
			sql2.append("GROUP BY A.PCODE, A.SENDERACQUIRE, A.SENDERPAYBANKID ");
			System.out.println(sql2.toString());
			
			page = onblocktab_Dao.getData(Integer.valueOf(pageNo), Integer.valueOf(pageSize), sql1.toString(), sql2.toString(), cols, FEE_SEARCH.class);
			list = (List<FEE_SEARCH>) page.getResult();
			System.out.println("FEE_SEARCH.list>>"+list);
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
	
	public String pageSearch(Map<String, String> param){
		String clearingPhase = "";
		if(StrUtils.isNotEmpty(param.get("CLEARINGPHASE").trim()) && !param.get("CLEARINGPHASE").trim().equals("all")){
			clearingPhase = param.get("CLEARINGPHASE").trim();
		}
		
		String endDate = "";
		if(StrUtils.isNotEmpty(param.get("END_DATE").trim())){
			endDate = param.get("END_DATE").trim();
			endDate = DateTimeUtils.convertDate(endDate, "yyyyMMdd", "yyyyMMdd");
		}
		
		int pageNo = StrUtils.isEmpty(param.get("page")) ?0:Integer.valueOf(param.get("page"));
		int pageSize = StrUtils.isEmpty(param.get("rows")) ?Integer.valueOf(Arguments.getStringArg("PAGE.SIZE")):Integer.valueOf(param.get("rows"));
		
		Map rtnMap = new HashMap();
		
		List<FEE_SEARCH> list = null;
		Page page = null;
		try {
			list = new ArrayList<FEE_SEARCH>();
			List<String> conditionList = getConditionList(param);
			String condition_1 = "", condition_2 = "", condition_3 = "", condition_4 = "";
			condition_1 = conditionList.get(0);
			condition_2 = conditionList.get(1);
			condition_3 = conditionList.get(2);
			condition_4 = conditionList.get(3);
			
			StringBuffer withTempSql = new StringBuffer();
			withTempSql.append("WITH TEMP AS ( ");
			withTempSql.append("    SELECT PCODE, SENDERPAYBANKID, OUTPAYBANKID, INPAYBANKID, WOPAYBANKID, SENDERACQUIRE, OUTACQUIRE, INACQUIRE, WOACQUIRE, "); 
			withTempSql.append("    (CASE '" + clearingPhase + "' WHEN '' THEN NEWSENDERFEE ELSE SENDERFEE END) AS NEWSENDERFEE, ");
			withTempSql.append("    (CASE '" + clearingPhase + "' WHEN '' THEN NEWOUTFEE ELSE OUTFEE END) AS NEWOUTFEE, "); 
			withTempSql.append("    (CASE '" + clearingPhase + "' WHEN '' THEN NEWINFEE ELSE INFEE END) AS NEWINFEE, "); 
			withTempSql.append("    (CASE '" + clearingPhase + "' WHEN '' THEN NEWWOFEE ELSE WOFEE END) AS NEWWOFEE, "); 
			withTempSql.append("    (CASE WHEN (CASE '" + clearingPhase + "' WHEN '' THEN RESULTSTATUS ELSE GETFIRSTSTATUS(TXDATE, STAN, '" + endDate + "', '" + clearingPhase + "') END) <> 'R' THEN '1' ELSE (CASE ACCTCODE WHEN '0' THEN '0' ELSE '1' END) END) AS ACCTCODE ");
			withTempSql.append("    FROM VW_ONBLOCKTAB ");
			withTempSql.append("    WHERE NEWRESULT <> 'R' ");
			withTempSql.append("    AND (CASE '" + clearingPhase + "' WHEN '' THEN RESULTSTATUS ELSE GETFIRSTSTATUS(TXDATE, STAN, '" + endDate + "', '" + clearingPhase + "') END) <> 'R' ");
			withTempSql.append("    AND SENDERSTATUS <> '1' " + (StrUtils.isEmpty(condition_1)?"":"AND " + condition_1));
			withTempSql.append("    UNION ALL ");
			withTempSql.append("    SELECT PCODE, SENDERPAYBANKID, OUTPAYBANKID, INPAYBANKID, WOPAYBANKID, SENDERACQUIRE, OUTACQUIRE, INACQUIRE, WOACQUIRE, NEWSENDERFEE, NEWOUTFEE, NEWINFEE, NEWWOFEE, ACCTCODE ");
			withTempSql.append("    FROM VW_ONPENDING_EC ");
			withTempSql.append("	" + (StrUtils.isEmpty(condition_2)?"":"WHERE " + condition_2));
			withTempSql.append(") ");
			
			StringBuffer fromAndWhere = new StringBuffer();
			StringBuffer fromAndWhere_all = new StringBuffer();
			/*
			fromAndWhere.append("FROM ( ");
			fromAndWhere.append("    SELECT ROWNUMBER() OVER( ");
			fromAndWhere.append(parseSidx(param.get("sidx"), param.get("sord")));
			fromAndWhere.append("    ) AS ROWNUMBER, T.* FROM ( ");
			fromAndWhere.append("        SELECT * FROM (");
			fromAndWhere.append("            SELECT * FROM ( ");
			fromAndWhere.append("                SELECT SENDERPAYBANKID AS BANKID, PCODE, (CASE ACCTCODE WHEN '0' THEN '0' ELSE '1' END) AS ACCTCODE FROM TEMP GROUP BY SENDERPAYBANKID, PCODE, ACCTCODE ");
			fromAndWhere.append("                UNION     SELECT OUTPAYBANKID, PCODE, (CASE ACCTCODE WHEN '0' THEN '0' ELSE '1' END) AS ACCTCODE FROM TEMP GROUP BY OUTPAYBANKID, PCODE, ACCTCODE ");
			fromAndWhere.append("                UNION     SELECT INPAYBANKID, PCODE, (CASE ACCTCODE WHEN '0' THEN '0' ELSE '1' END) AS ACCTCODE FROM TEMP GROUP BY INPAYBANKID, PCODE, ACCTCODE ");
			fromAndWhere.append("            )  " + (StrUtils.isEmpty(condition_3)? "" : "WHERE " + condition_3)); 
			fromAndWhere.append("        ) AS X LEFT JOIN ( ");
//			fromAndWhere.append("            SELECT BR.BRBK_ID, BR.BRBK_NAME, BG.BGBK_ID, BG.BGBK_NAME, BG.OPBK_ID, OP.BGBK_NAME AS OPBK_NAME ");
			fromAndWhere.append("            SELECT BR.BRBK_ID, BR.BRBK_NAME, BG.BGBK_ID, OP.OPBK_ID ");
			fromAndWhere.append("            , (SELECT BG.BGBK_NAME FROM BANK_GROUP AS BG WHERE BG.BGBK_ID = OP.BGBK_ID) AS BGBK_NAME ");
			fromAndWhere.append("            , (SELECT BG.BGBK_NAME FROM BANK_GROUP AS BG WHERE BG.BGBK_ID = OP.OPBK_ID) AS OPBK_NAME ");
			fromAndWhere.append("            FROM BANK_BRANCH BR LEFT JOIN BANK_GROUP BG ON BR.BGBK_ID = BG.BGBK_ID ");
//			fromAndWhere.append("            LEFT JOIN BANK_GROUP OP ON BG.OPBK_ID = OP.BGBK_ID ");
			fromAndWhere.append("            LEFT JOIN (  SELECT DISTINCT BGBK_ID, OPBK_ID FROM BANK_OPBK ) OP ON BG.OPBK_ID = OP.BGBK_ID ");
			fromAndWhere.append("             " + (StrUtils.isEmpty(condition_4)? "" : "WHERE " + condition_4));
			fromAndWhere.append("        ) AS Y ON X.BANKID = Y.BRBK_ID ");
			fromAndWhere.append("         " + (StrUtils.isEmpty(condition_5)? "" : "WHERE " + condition_5));
			fromAndWhere.append("    ) AS T ");
			fromAndWhere_all.append(fromAndWhere);
			fromAndWhere.append(") AS A WHERE ROWNUMBER >= " + (Page.getStartOfPage(pageNo, pageSize) + 1) + " AND ROWNUMBER <= " + (pageNo * pageSize) + " ");
			fromAndWhere_all.append(") AS A ");
			*/
			
			fromAndWhere.append("FROM ( ");
			fromAndWhere.append("    SELECT ROWNUMBER() OVER("+parseSidx(param.get("sidx"), param.get("sord"))+") AS ROWNUMBER, T.* FROM ( ");
			fromAndWhere.append("        SELECT Y.*, COALESCE((SELECT BRBK_NAME FROM BANK_BRANCH WHERE BRBK_ID = Y.BANKID),'') AS BRBK_NAME, ");
			fromAndWhere.append("		 COALESCE((SELECT BGBK_NAME FROM BANK_GROUP WHERE BGBK_ID = Y.OPBK_ID),'') AS OPBK_NAME ");
			fromAndWhere.append("        FROM ( ");
			fromAndWhere.append("            SELECT X.*, GETBKHEADID(X.BANKID) AS BGBK_ID ");
			fromAndWhere.append("            FROM ( ");
			fromAndWhere.append("                SELECT SENDERACQUIRE AS OPBK_ID, SENDERPAYBANKID AS BANKID, PCODE, (CASE ACCTCODE WHEN '0' THEN '0' ELSE '1' END) AS ACCTCODE FROM TEMP GROUP BY SENDERACQUIRE, SENDERPAYBANKID, PCODE, ACCTCODE ");
			fromAndWhere.append("                UNION ");
			fromAndWhere.append("                SELECT OUTACQUIRE, OUTPAYBANKID, PCODE, (CASE ACCTCODE WHEN '0' THEN '0' ELSE '1' END) AS ACCTCODE FROM TEMP GROUP BY OUTACQUIRE, OUTPAYBANKID, PCODE, ACCTCODE ");
			fromAndWhere.append("                UNION ");
			fromAndWhere.append("                SELECT INACQUIRE, INPAYBANKID, PCODE, (CASE ACCTCODE WHEN '0' THEN '0' ELSE '1' END) AS ACCTCODE FROM TEMP GROUP BY INACQUIRE, INPAYBANKID, PCODE, ACCTCODE ");
			fromAndWhere.append("                UNION ");
			fromAndWhere.append("                SELECT WOACQUIRE, WOPAYBANKID, PCODE, (CASE ACCTCODE WHEN '0' THEN '0' ELSE '1' END) AS ACCTCODE FROM TEMP WHERE PCODE LIKE '27%' GROUP BY WOACQUIRE, WOPAYBANKID, PCODE, ACCTCODE ");
			fromAndWhere.append("            ) AS X ");
			fromAndWhere.append("            " + (StrUtils.isEmpty(condition_3)? "" : "WHERE " + condition_3));
			fromAndWhere.append("        ) AS Y ");
			fromAndWhere.append("        " + (StrUtils.isEmpty(condition_4)? "" : "WHERE " + condition_4));
			fromAndWhere.append("    ) AS T ");
			fromAndWhere.append(") AS A ");
			
			fromAndWhere_all.append(fromAndWhere);
			fromAndWhere.append("WHERE ROWNUMBER >= " + (Page.getStartOfPage(pageNo, pageSize) + 1) + " AND ROWNUMBER <= " + (pageNo * pageSize) + " ");
			
			StringBuffer select_fds = new StringBuffer();
			select_fds.append("(SELECT COUNT(*) FROM TEMP WHERE SENDERPAYBANKID = A.BANKID AND PCODE = A.PCODE AND ACCTCODE = A.ACCTCODE) AS FIRECOUNT, ");
			select_fds.append("COALESCE((SELECT SUM(NEWSENDERFEE) FROM TEMP WHERE SENDERPAYBANKID = A.BANKID AND PCODE = A.PCODE AND ACCTCODE = A.ACCTCODE),0) AS FIREAMT, ");
			select_fds.append("(SELECT COUNT(*) FROM TEMP WHERE OUTPAYBANKID = A.BANKID AND PCODE = A.PCODE AND ACCTCODE = A.ACCTCODE) AS DEBITCOUNT, ");
			select_fds.append("COALESCE((SELECT SUM(NEWOUTFEE) FROM TEMP WHERE OUTPAYBANKID = A.BANKID AND PCODE = A.PCODE AND ACCTCODE = A.ACCTCODE),0) AS DEBITAMT, ");
			select_fds.append("(SELECT COUNT(*) FROM TEMP WHERE INPAYBANKID = A.BANKID AND PCODE = A.PCODE AND ACCTCODE = A.ACCTCODE) AS SAVECOUNT, ");
			select_fds.append("COALESCE((SELECT SUM(NEWINFEE) FROM TEMP WHERE INPAYBANKID = A.BANKID AND PCODE = A.PCODE AND ACCTCODE = A.ACCTCODE),0) AS SAVEAMT, ");
			select_fds.append("(SELECT COUNT(*) FROM TEMP WHERE WOPAYBANKID = A.BANKID AND PCODE = A.PCODE AND ACCTCODE = A.ACCTCODE) AS CANCELCOUNT, ");
			select_fds.append("COALESCE((SELECT SUM(NEWWOFEE) FROM TEMP WHERE WOPAYBANKID = A.BANKID AND PCODE = A.PCODE AND ACCTCODE = A.ACCTCODE),0) AS CANCELAMT ");
			
			//先算出總計的欄位值，放到rtnMap裡面，到頁面再放到下面的總計Grid裡面
			StringBuffer dataSumSQL = new StringBuffer();
			dataSumSQL.append(withTempSql);
			dataSumSQL.append("SELECT SUM(X.FIRECOUNT) FIRECOUNT,SUM(X.FIREAMT) FIREAMT,SUM(X.DEBITCOUNT) DEBITCOUNT,SUM(X.DEBITAMT) DEBITAMT,SUM(X.SAVECOUNT) SAVECOUNT,SUM(X.SAVEAMT) SAVEAMT,SUM(X.CANCELCOUNT) CANCELCOUNT,SUM(X.CANCELAMT) CANCELAMT  FROM ( ");
			dataSumSQL.append("SELECT ");
			dataSumSQL.append(select_fds);
			dataSumSQL.append(fromAndWhere_all);
			dataSumSQL.append(") AS X(FIRECOUNT,FIREAMT,DEBITCOUNT,DEBITAMT,SAVECOUNT,SAVEAMT,CANCELCOUNT,CANCELAMT)");
			System.out.println("dataSumSQL="+dataSumSQL);
			
			String dataSumCols[] = {"FIRECOUNT", "FIREAMT", "DEBITCOUNT", "DEBITAMT","SAVECOUNT","SAVEAMT","CANCELCOUNT","CANCELAMT"};
			list = onblocktab_Dao.dataSum(dataSumSQL.toString(),dataSumCols,FEE_SEARCH.class);
			
			for(FEE_SEARCH po:list){
				System.out.println(String.format("SUM(X.FIRECOUNT)=%s,SUM(X.FIREAMT)=%s,SUM(X.DEBITCOUNT)=%s,SUM(X.DEBITAMT)=%s,SUM(X.SAVECOUNT)=%s,SUM(X.SAVEAMT)=%s,SUM(X.CANCELCOUNT)=%s,SUM(X.CANCELAMT)=%s",
					po.getFIRECOUNT(),po.getFIREAMT(),po.getDEBITCOUNT(),po.getDEBITAMT(),po.getSAVECOUNT(),po.getSAVEAMT(),po.getCANCELCOUNT(),po.getCANCELAMT()));
			}
			rtnMap.put("dataSumList", list);
			
			StringBuffer countQuery = new StringBuffer();
			StringBuffer sql = new StringBuffer();
			//String cols[] = {"BGBK_ID_NAME", "BANKID", "PCODE", "ACCTCODE", "FIRECOUNT", "FIREAMT", "DEBITCOUNT", "DEBITAMT", "SAVECOUNT", "SAVEAMT", "BGBK_ID", "BGBK_NAME", "OPBK_ID", "OPBK_NAME"};
			String cols[] = {"BGBK_ID_NAME", "BANKID", "PCODE", "ACCTCODE", "FIRECOUNT", "FIREAMT", "DEBITCOUNT", "DEBITAMT", "SAVECOUNT", "SAVEAMT", "CANCELCOUNT", "CANCELAMT", "BGBK_ID", "OPBK_ID", "OPBK_NAME"};
			countQuery.append(withTempSql);
			countQuery.append("SELECT COUNT(*) AS NUM ");
			countQuery.append(fromAndWhere_all);
				
			sql.append(withTempSql);
			sql.append("SELECT ");
			sql.append("GETBKNAME(A.BGBK_ID) AS BGBK_ID_NAME, (A.BANKID || '-' || A.BRBK_NAME) AS BANKID, A.PCODE, "); 
			sql.append("(CASE A.ACCTCODE WHEN '0' THEN '沖正' ELSE '一般' END) AS ACCTCODE, "); 
			sql.append(select_fds + ", ");
			//sql.append("A.BGBK_ID, A.BGBK_NAME, A.OPBK_ID, A.OPBK_NAME ");
			sql.append("A.BGBK_ID, A.OPBK_ID, A.OPBK_NAME ");
			sql.append(fromAndWhere);
			if(StrUtils.isNotEmpty(param.get("sidx"))){
				if(param.get("sidx").contains("ASC") || param.get("sidx").contains("DESC") ||
				   param.get("sidx").contains("asc") || param.get("sidx").contains("desc")){
					sql.append(" ORDER BY "+param.get("sidx"));
				}else{
					sql.append(" ORDER BY "+param.get("sidx")+" "+param.get("sord"));
				}
			}
			
			System.out.println("countQuery="+countQuery.toString().toUpperCase());
			System.out.println("sql="+sql.toString().toUpperCase());
			
			page = onblocktab_Dao.getDataIIII(pageNo, pageSize, countQuery.toString(), sql.toString(), cols, FEE_SEARCH.class);
			list = (List<FEE_SEARCH>) page.getResult();
			System.out.println("FEE_SEARCH.list>>"+list);
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
		//為了讓金額有浮點數兩位，所以用gson轉json字串(原先用JSONUtils.map2json，小數點會被吃掉)
		String json = new Gson().toJson(rtnMap) ;
		System.out.println("json=" + json);
		return json;
	}
	
	
	
	public String pageSearch_aftb(Map<String, String> param){
		String clearingPhase = "";
		if(StrUtils.isNotEmpty(param.get("CLEARINGPHASE").trim()) && !param.get("CLEARINGPHASE").trim().equals("all")){
			clearingPhase = param.get("CLEARINGPHASE").trim();
		}
		
		String endDate = "";
		if(StrUtils.isNotEmpty(param.get("END_DATE").trim())){
			endDate = param.get("END_DATE").trim();
			endDate = DateTimeUtils.convertDate(endDate, "yyyyMMdd", "yyyyMMdd");
		}
		
		int pageNo = StrUtils.isEmpty(param.get("page")) ?0:Integer.valueOf(param.get("page"));
		int pageSize = StrUtils.isEmpty(param.get("rows")) ?Integer.valueOf(Arguments.getStringArg("PAGE.SIZE")):Integer.valueOf(param.get("rows"));
		
		Map rtnMap = new HashMap();
		
		List<FEE_SEARCH> list = null;
		Page page = null;
		try {
			list = new ArrayList<FEE_SEARCH>();
			List<String> conditionList = getConditionList(param);
			String condition_1 = "", condition_2 = "", condition_3 = "", condition_4 = "";
			condition_1 = conditionList.get(0);
			condition_2 = conditionList.get(1);
			condition_3 = conditionList.get(2);
			condition_4 = conditionList.get(3);
			
			StringBuffer withTempSql = new StringBuffer();
			withTempSql.append("WITH TEMP AS ( ");
			withTempSql.append("    SELECT PCODE, SENDERPAYBANKID, OUTPAYBANKID, INPAYBANKID, WOPAYBANKID, SENDERACQUIRE, OUTACQUIRE, INACQUIRE, WOACQUIRE, "); 
//			withTempSql.append("    SENDERFEE_NW  AS NEWSENDERFEE, ");
//			withTempSql.append("    OUTFEE_NW AS NEWOUTFEE, "); 
//			withTempSql.append("    INFEE_NW AS NEWINFEE, "); 
//			withTempSql.append("    WOFEE_NW AS NEWWOFEE, ");
			withTempSql.append("    (CASE '" + clearingPhase + "' WHEN '' THEN NEWSENDERFEE ELSE SENDERFEE_NW END) AS NEWSENDERFEE, ");
			withTempSql.append("    (CASE '" + clearingPhase + "' WHEN '' THEN NEWOUTFEE ELSE OUTFEE_NW END) AS NEWOUTFEE, "); 
			withTempSql.append("    (CASE '" + clearingPhase + "' WHEN '' THEN NEWINFEE ELSE INFEE_NW END) AS NEWINFEE, "); 
			withTempSql.append("    (CASE '" + clearingPhase + "' WHEN '' THEN NEWWOFEE ELSE WOFEE_NW END) AS NEWWOFEE, "); 
			withTempSql.append("    (CASE WHEN (CASE '" + clearingPhase + "' WHEN '' THEN RESULTSTATUS ELSE GETFIRSTSTATUS(TXDATE, STAN, '" + endDate + "', '" + clearingPhase + "') END) <> 'R' THEN '1' ELSE (CASE ACCTCODE WHEN '0' THEN '0' ELSE '1' END) END) AS ACCTCODE ");
			withTempSql.append("    FROM VW_ONBLOCKTAB_NW ");
			withTempSql.append("    WHERE NEWRESULT <> 'R' ");
			withTempSql.append("    AND (CASE '" + clearingPhase + "' WHEN '' THEN RESULTSTATUS ELSE GETFIRSTSTATUS(TXDATE, STAN, '" + endDate + "', '" + clearingPhase + "') END) <> 'R' ");
			withTempSql.append("    AND SENDERSTATUS <> '1' " + (StrUtils.isEmpty(condition_1)?"":"AND " + condition_1));
			withTempSql.append("    UNION ALL ");
			withTempSql.append("    SELECT PCODE, SENDERPAYBANKID, OUTPAYBANKID, INPAYBANKID, WOPAYBANKID, SENDERACQUIRE, OUTACQUIRE, INACQUIRE, WOACQUIRE, SENDERFEE_NW * (-1), OUTFEE_NW * (-1) , INFEE_NW * (-1) , WOFEE_NW * (-1), ACCTCODE ");
			withTempSql.append("    FROM VW_ONPENDING_EC_NW ");
			withTempSql.append("	" + (StrUtils.isEmpty(condition_2)?"":"WHERE " + condition_2));
			withTempSql.append(") ");
			
			StringBuffer fromAndWhere = new StringBuffer();
			StringBuffer fromAndWhere_all = new StringBuffer();
			/*
			fromAndWhere.append("FROM ( ");
			fromAndWhere.append("    SELECT ROWNUMBER() OVER( ");
			fromAndWhere.append(parseSidx(param.get("sidx"), param.get("sord")));
			fromAndWhere.append("    ) AS ROWNUMBER, T.* FROM ( ");
			fromAndWhere.append("        SELECT * FROM (");
			fromAndWhere.append("            SELECT * FROM ( ");
			fromAndWhere.append("                SELECT SENDERPAYBANKID AS BANKID, PCODE, (CASE ACCTCODE WHEN '0' THEN '0' ELSE '1' END) AS ACCTCODE FROM TEMP GROUP BY SENDERPAYBANKID, PCODE, ACCTCODE ");
			fromAndWhere.append("                UNION     SELECT OUTPAYBANKID, PCODE, (CASE ACCTCODE WHEN '0' THEN '0' ELSE '1' END) AS ACCTCODE FROM TEMP GROUP BY OUTPAYBANKID, PCODE, ACCTCODE ");
			fromAndWhere.append("                UNION     SELECT INPAYBANKID, PCODE, (CASE ACCTCODE WHEN '0' THEN '0' ELSE '1' END) AS ACCTCODE FROM TEMP GROUP BY INPAYBANKID, PCODE, ACCTCODE ");
			fromAndWhere.append("            )  " + (StrUtils.isEmpty(condition_3)? "" : "WHERE " + condition_3)); 
			fromAndWhere.append("        ) AS X LEFT JOIN ( ");
//			fromAndWhere.append("            SELECT BR.BRBK_ID, BR.BRBK_NAME, BG.BGBK_ID, BG.BGBK_NAME, BG.OPBK_ID, OP.BGBK_NAME AS OPBK_NAME ");
			fromAndWhere.append("            SELECT BR.BRBK_ID, BR.BRBK_NAME, BG.BGBK_ID, OP.OPBK_ID ");
			fromAndWhere.append("            , (SELECT BG.BGBK_NAME FROM BANK_GROUP AS BG WHERE BG.BGBK_ID = OP.BGBK_ID) AS BGBK_NAME ");
			fromAndWhere.append("            , (SELECT BG.BGBK_NAME FROM BANK_GROUP AS BG WHERE BG.BGBK_ID = OP.OPBK_ID) AS OPBK_NAME ");
			fromAndWhere.append("            FROM BANK_BRANCH BR LEFT JOIN BANK_GROUP BG ON BR.BGBK_ID = BG.BGBK_ID ");
//			fromAndWhere.append("            LEFT JOIN BANK_GROUP OP ON BG.OPBK_ID = OP.BGBK_ID ");
			fromAndWhere.append("            LEFT JOIN (  SELECT DISTINCT BGBK_ID, OPBK_ID FROM BANK_OPBK ) OP ON BG.OPBK_ID = OP.BGBK_ID ");
			fromAndWhere.append("             " + (StrUtils.isEmpty(condition_4)? "" : "WHERE " + condition_4));
			fromAndWhere.append("        ) AS Y ON X.BANKID = Y.BRBK_ID ");
			fromAndWhere.append("         " + (StrUtils.isEmpty(condition_5)? "" : "WHERE " + condition_5));
			fromAndWhere.append("    ) AS T ");
			fromAndWhere_all.append(fromAndWhere);
			fromAndWhere.append(") AS A WHERE ROWNUMBER >= " + (Page.getStartOfPage(pageNo, pageSize) + 1) + " AND ROWNUMBER <= " + (pageNo * pageSize) + " ");
			fromAndWhere_all.append(") AS A ");
			*/
			
			fromAndWhere.append("FROM ( ");
			fromAndWhere.append("    SELECT ROWNUMBER() OVER("+parseSidx(param.get("sidx"), param.get("sord"))+") AS ROWNUMBER, T.* FROM ( ");
			fromAndWhere.append("        SELECT Y.*, COALESCE((SELECT BRBK_NAME FROM BANK_BRANCH WHERE BRBK_ID = Y.BANKID),'') AS BRBK_NAME, ");
			fromAndWhere.append("		 COALESCE((SELECT BGBK_NAME FROM BANK_GROUP WHERE BGBK_ID = Y.OPBK_ID),'') AS OPBK_NAME ");
			fromAndWhere.append("        FROM ( ");
			fromAndWhere.append("            SELECT X.*, GETBKHEADID(X.BANKID) AS BGBK_ID ");
			fromAndWhere.append("            FROM ( ");
			fromAndWhere.append("                SELECT SENDERACQUIRE AS OPBK_ID, SENDERPAYBANKID AS BANKID, PCODE, (CASE ACCTCODE WHEN '0' THEN '0' ELSE '1' END) AS ACCTCODE FROM TEMP GROUP BY SENDERACQUIRE, SENDERPAYBANKID, PCODE, ACCTCODE ");
			fromAndWhere.append("                UNION ");
			fromAndWhere.append("                SELECT OUTACQUIRE, OUTPAYBANKID, PCODE, (CASE ACCTCODE WHEN '0' THEN '0' ELSE '1' END) AS ACCTCODE FROM TEMP GROUP BY OUTACQUIRE, OUTPAYBANKID, PCODE, ACCTCODE ");
			fromAndWhere.append("                UNION ");
			fromAndWhere.append("                SELECT INACQUIRE, INPAYBANKID, PCODE, (CASE ACCTCODE WHEN '0' THEN '0' ELSE '1' END) AS ACCTCODE FROM TEMP GROUP BY INACQUIRE, INPAYBANKID, PCODE, ACCTCODE ");
			fromAndWhere.append("                UNION ");
			fromAndWhere.append("                SELECT WOACQUIRE, WOPAYBANKID, PCODE, (CASE ACCTCODE WHEN '0' THEN '0' ELSE '1' END) AS ACCTCODE FROM TEMP WHERE PCODE LIKE '27%' GROUP BY WOACQUIRE, WOPAYBANKID, PCODE, ACCTCODE ");
			fromAndWhere.append("            ) AS X ");
			fromAndWhere.append("            " + (StrUtils.isEmpty(condition_3)? "" : "WHERE " + condition_3));
			fromAndWhere.append("        ) AS Y ");
			fromAndWhere.append("        " + (StrUtils.isEmpty(condition_4)? "" : "WHERE " + condition_4));
			fromAndWhere.append("    ) AS T ");
			fromAndWhere.append(") AS A ");
			
			fromAndWhere_all.append(fromAndWhere);
			fromAndWhere.append("WHERE ROWNUMBER >= " + (Page.getStartOfPage(pageNo, pageSize) + 1) + " AND ROWNUMBER <= " + (pageNo * pageSize) + " ");
			
			StringBuffer select_fds = new StringBuffer();
			select_fds.append("(SELECT COUNT(*) FROM TEMP WHERE SENDERPAYBANKID = A.BANKID AND PCODE = A.PCODE AND ACCTCODE = A.ACCTCODE) AS FIRECOUNT, ");
			select_fds.append("COALESCE((SELECT SUM(NEWSENDERFEE) FROM TEMP WHERE SENDERPAYBANKID = A.BANKID AND PCODE = A.PCODE AND ACCTCODE = A.ACCTCODE),0) AS FIREAMT, ");
			select_fds.append("(SELECT COUNT(*) FROM TEMP WHERE OUTPAYBANKID = A.BANKID AND PCODE = A.PCODE AND ACCTCODE = A.ACCTCODE) AS DEBITCOUNT, ");
			select_fds.append("COALESCE((SELECT SUM(NEWOUTFEE) FROM TEMP WHERE OUTPAYBANKID = A.BANKID AND PCODE = A.PCODE AND ACCTCODE = A.ACCTCODE),0) AS DEBITAMT, ");
			select_fds.append("(SELECT COUNT(*) FROM TEMP WHERE INPAYBANKID = A.BANKID AND PCODE = A.PCODE AND ACCTCODE = A.ACCTCODE) AS SAVECOUNT, ");
			select_fds.append("COALESCE((SELECT SUM(NEWINFEE) FROM TEMP WHERE INPAYBANKID = A.BANKID AND PCODE = A.PCODE AND ACCTCODE = A.ACCTCODE),0) AS SAVEAMT, ");
			select_fds.append("(SELECT COUNT(*) FROM TEMP WHERE WOPAYBANKID = A.BANKID AND PCODE = A.PCODE AND ACCTCODE = A.ACCTCODE) AS CANCELCOUNT, ");
			select_fds.append("COALESCE((SELECT SUM(NEWWOFEE) FROM TEMP WHERE WOPAYBANKID = A.BANKID AND PCODE = A.PCODE AND ACCTCODE = A.ACCTCODE),0) AS CANCELAMT ");
			
			//先算出總計的欄位值，放到rtnMap裡面，到頁面再放到下面的總計Grid裡面
			StringBuffer dataSumSQL = new StringBuffer();
			dataSumSQL.append(withTempSql);
			dataSumSQL.append("SELECT SUM(X.FIRECOUNT) FIRECOUNT,SUM(X.FIREAMT) FIREAMT,SUM(X.DEBITCOUNT) DEBITCOUNT,SUM(X.DEBITAMT) DEBITAMT,SUM(X.SAVECOUNT) SAVECOUNT,SUM(X.SAVEAMT) SAVEAMT,SUM(X.CANCELCOUNT) CANCELCOUNT,SUM(X.CANCELAMT) CANCELAMT  FROM ( ");
			dataSumSQL.append("SELECT ");
			dataSumSQL.append(select_fds);
			dataSumSQL.append(fromAndWhere_all);
			dataSumSQL.append(") AS X(FIRECOUNT,FIREAMT,DEBITCOUNT,DEBITAMT,SAVECOUNT,SAVEAMT,CANCELCOUNT,CANCELAMT)");
			System.out.println("dataSumSQL="+dataSumSQL);
			
			String dataSumCols[] = {"FIRECOUNT", "FIREAMT", "DEBITCOUNT", "DEBITAMT","SAVECOUNT","SAVEAMT","CANCELCOUNT","CANCELAMT"};
			list = onblocktab_Dao.dataSum(dataSumSQL.toString(),dataSumCols,FEE_SEARCH.class);
			
			for(FEE_SEARCH po:list){
				System.out.println(String.format("SUM(X.FIRECOUNT)=%s,SUM(X.FIREAMT)=%s,SUM(X.DEBITCOUNT)=%s,SUM(X.DEBITAMT)=%s,SUM(X.SAVECOUNT)=%s,SUM(X.SAVEAMT)=%s,SUM(X.CANCELCOUNT)=%s,SUM(X.CANCELAMT)=%s",
					po.getFIRECOUNT(),po.getFIREAMT(),po.getDEBITCOUNT(),po.getDEBITAMT(),po.getSAVECOUNT(),po.getSAVEAMT(),po.getCANCELCOUNT(),po.getCANCELAMT()));
			}
			rtnMap.put("dataSumList", list);
			
			StringBuffer countQuery = new StringBuffer();
			StringBuffer sql = new StringBuffer();
			//String cols[] = {"BGBK_ID_NAME", "BANKID", "PCODE", "ACCTCODE", "FIRECOUNT", "FIREAMT", "DEBITCOUNT", "DEBITAMT", "SAVECOUNT", "SAVEAMT", "BGBK_ID", "BGBK_NAME", "OPBK_ID", "OPBK_NAME"};
			String cols[] = {"BGBK_ID_NAME", "BANKID", "PCODE", "ACCTCODE", "FIRECOUNT", "FIREAMT", "DEBITCOUNT", "DEBITAMT", "SAVECOUNT", "SAVEAMT", "CANCELCOUNT", "CANCELAMT", "BGBK_ID", "OPBK_ID", "OPBK_NAME"};
			countQuery.append(withTempSql);
			countQuery.append("SELECT COUNT(*) AS NUM ");
			countQuery.append(fromAndWhere_all);
				
			sql.append(withTempSql);
			sql.append("SELECT ");
			sql.append("GETBKNAME(A.BGBK_ID) AS BGBK_ID_NAME, (A.BANKID || '-' || A.BRBK_NAME) AS BANKID, A.PCODE, "); 
			sql.append("(CASE A.ACCTCODE WHEN '0' THEN '沖正' ELSE '一般' END) AS ACCTCODE, "); 
			sql.append(select_fds + ", ");
			//sql.append("A.BGBK_ID, A.BGBK_NAME, A.OPBK_ID, A.OPBK_NAME ");
			sql.append("A.BGBK_ID, A.OPBK_ID, A.OPBK_NAME ");
			sql.append(fromAndWhere);
			if(StrUtils.isNotEmpty(param.get("sidx"))){
				if(param.get("sidx").contains("ASC") || param.get("sidx").contains("DESC") ||
				   param.get("sidx").contains("asc") || param.get("sidx").contains("desc")){
					sql.append(" ORDER BY "+param.get("sidx"));
				}else{
					sql.append(" ORDER BY "+param.get("sidx")+" "+param.get("sord"));
				}
			}
			
			System.out.println("countQuery="+countQuery.toString().toUpperCase());
			System.out.println("sql="+sql.toString().toUpperCase());
			
			page = onblocktab_Dao.getDataIIII(pageNo, pageSize, countQuery.toString(), sql.toString(), cols, FEE_SEARCH.class);
			list = (List<FEE_SEARCH>) page.getResult();
			System.out.println("FEE_SEARCH.list>>"+list);
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
		//為了讓金額有浮點數兩位，所以用gson轉json字串(原先用JSONUtils.map2json，小數點會被吃掉)
		String json = new Gson().toJson(rtnMap) ;
		System.out.println("json=" + json);
		return json;
	}
	
	public List<String> getConditionList(Map<String, String> param){
		List<String> conditionList = new ArrayList<String>();
		
		List<String> conditions_1 = new ArrayList<String>();
		List<String> conditions_2 = new ArrayList<String>();
		List<String> conditions_3 = new ArrayList<String>();
		List<String> conditions_4 = new ArrayList<String>();
		
		String pcode = "";
		if(StrUtils.isNotEmpty(param.get("PCODE").trim()) && !param.get("PCODE").trim().equals("all")){
			pcode = param.get("PCODE").trim();
			conditions_1.add(" PCODE = '" + pcode + "' ");
			conditions_2.add(" PCODE = '" + pcode + "' ");
			conditions_3.add(" PCODE = '" + pcode + "' ");
		}
		
		String opbkId = "";
		if(StrUtils.isNotEmpty(param.get("OPBK_ID").trim()) && !param.get("OPBK_ID").trim().equals("all")){
			opbkId = param.get("OPBK_ID").trim();
			conditions_1.add(" (SENDERACQUIRE = '" + opbkId + "' OR OUTACQUIRE = '" + opbkId + "' OR INACQUIRE = '" + opbkId + "' OR WOACQUIRE = '" + opbkId + "') ");
			conditions_2.add(" (SENDERACQUIRE = '" + opbkId + "' OR OUTACQUIRE = '" + opbkId + "' OR INACQUIRE = '" + opbkId + "' OR WOACQUIRE = '" + opbkId + "') ");
			conditions_3.add(" OPBK_ID = '" + opbkId + "' ");
		}
		
		String bgbkId = "";
		if(!param.get("BGBK_ID").trim().equals("all")){
			bgbkId = param.get("BGBK_ID").trim();
			conditions_1.add(" (SENDERHEAD = '" + bgbkId + "' OR OUTHEAD = '" + bgbkId + "' OR INHEAD = '" + bgbkId + "' OR WOHEAD = '" + bgbkId + "') ");
			conditions_2.add(" (SENDERHEAD = '" + bgbkId + "' OR OUTHEAD = '" + bgbkId + "' OR INHEAD = '" + bgbkId + "' OR WOHEAD = '" + bgbkId + "') ");
			conditions_4.add(" Y.BGBK_ID = '" + bgbkId + "' ");
		}
		
		String brbkId = "";
		if(StrUtils.isNotEmpty(param.get("BRBK_ID").trim()) && !param.get("BRBK_ID").trim().equals("all")){
			brbkId = param.get("BRBK_ID").trim();
			conditions_1.add(" (SENDERPAYBANKID = '" + brbkId + "' OR OUTPAYBANKID = '" + brbkId + "' OR INPAYBANKID = '" + brbkId  + "' OR WOPAYBANKID = '" + brbkId + "') ");
			conditions_2.add(" (SENDERPAYBANKID = '" + brbkId + "' OR OUTPAYBANKID = '" + brbkId + "' OR INPAYBANKID = '" + brbkId  + "' OR WOPAYBANKID = '" + brbkId + "') ");
			conditions_3.add(" BANKID = '" + brbkId + "' ");
		}
		
		String clearingPhase = "";
		if(StrUtils.isNotEmpty(param.get("CLEARINGPHASE").trim()) && !param.get("CLEARINGPHASE").trim().equals("all")){
			clearingPhase = param.get("CLEARINGPHASE").trim();
			conditions_1.add(" CLEARINGPHASE = '" + clearingPhase + "' ");
			conditions_2.add(" NEWCLRPHASE = '" + clearingPhase + "' ");
		}
		
		String startDate = "";
		if(StrUtils.isNotEmpty(param.get("START_DATE").trim())){
			startDate = param.get("START_DATE").trim();
			conditions_1.add(" BIZDATE >= '" + DateTimeUtils.convertDate(startDate, "yyyyMMdd", "yyyyMMdd") + "' ");
			conditions_2.add(" NEWBIZDATE >= '" + DateTimeUtils.convertDate(startDate, "yyyyMMdd", "yyyyMMdd") + "' ");
		}
				
		String endDate = "";
		if(StrUtils.isNotEmpty(param.get("END_DATE").trim())){
			endDate = param.get("END_DATE").trim();
			endDate = DateTimeUtils.convertDate(endDate, "yyyyMMdd", "yyyyMMdd");
			conditions_1.add(" BIZDATE <= '" + endDate + "' ");
			conditions_2.add(" NEWBIZDATE <= '" + endDate + "' ");
		}
		
		conditionList.add( combine(conditions_1) );
		conditionList.add( combine(conditions_2) );
		conditionList.add( combine(conditions_3) );
		conditionList.add( combine(conditions_4) );
		return conditionList;
	}
	
	public String getOrderCondition(String sidx){
		if(sidx.startsWith("BGBK_ID_NAME")){
			return "BGBK_ID";
		}else if(sidx.startsWith("BANKID")){
			return "BANKID";
		}else if(sidx.startsWith("PCODE")){
			return "PCODE";
		}else if(sidx.startsWith("ACCTCODE")){
			return "ACCTCODE";
		}else if(sidx.startsWith("FIRECOUNT")){
			return "(SELECT COUNT(*) FROM TEMP WHERE SENDERPAYBANKID = T.BANKID AND PCODE = T.PCODE AND ACCTCODE = T.ACCTCODE)";
		}else if(sidx.startsWith("FIREAMT")){
			return "COALESCE((SELECT SUM(NEWSENDERFEE) FROM TEMP WHERE SENDERPAYBANKID = T.BANKID AND PCODE = T.PCODE AND ACCTCODE = T.ACCTCODE),0)";
		}else if(sidx.startsWith("DEBITCOUNT")){
			return "(SELECT COUNT(*) FROM TEMP WHERE OUTPAYBANKID = T.BANKID AND PCODE = T.PCODE AND ACCTCODE = T.ACCTCODE)";
		}else if(sidx.startsWith("DEBITAMT")){
			return "COALESCE((SELECT SUM(NEWOUTFEE) FROM TEMP WHERE OUTPAYBANKID = T.BANKID AND PCODE = T.PCODE AND ACCTCODE = T.ACCTCODE),0)";
		}else if(sidx.startsWith("SAVECOUNT")){
			return "(SELECT COUNT(*) FROM TEMP WHERE INPAYBANKID = T.BANKID AND PCODE = T.PCODE AND ACCTCODE = T.ACCTCODE)";
		}else if(sidx.startsWith("SAVEAMT")){
			return "COALESCE((SELECT SUM(NEWINFEE) FROM TEMP WHERE INPAYBANKID = T.BANKID AND PCODE = T.PCODE AND ACCTCODE = T.ACCTCODE),0)";
		}else if(sidx.startsWith("CANCELCOUNT")){
			return "(SELECT COUNT(*) FROM TEMP WHERE WOPAYBANKID = T.BANKID AND PCODE = T.PCODE AND ACCTCODE = T.ACCTCODE)";
		}else if(sidx.startsWith("CANCELAMT")){
			return "COALESCE((SELECT SUM(NEWWOFEE) FROM TEMP WHERE WOPAYBANKID = T.BANKID AND PCODE = T.PCODE AND ACCTCODE = T.ACCTCODE),0)";
		}else{
			return sidx;
		}
	}
	
	public String getSQL(String startDate, String endDate, String pcode, String clearingPhase, String opbkId, String bgbkId, String brbkId, String sidx, String sord){
		Map<String, String> params = new HashMap<String, String>();
		String tmpClearingPhase = "";
		params.put("START_DATE", startDate);
		params.put("END_DATE", endDate);
		params.put("PCODE", pcode);
		params.put("CLEARINGPHASE", clearingPhase);
		params.put("OPBK_ID", opbkId);
		params.put("BGBK_ID", bgbkId);
		params.put("BRBK_ID", brbkId);
		List<String> conditions = getConditionList(params);
		String condition_1 = "", condition_2 = "", condition_3 = "", condition_4 = "";
		condition_1 = conditions.get(0);
		condition_2 = conditions.get(1);
		condition_3 = conditions.get(2);
		condition_4 = conditions.get(3);
		if(StrUtils.isNotEmpty(clearingPhase) && !clearingPhase.equals("all")){
			tmpClearingPhase = clearingPhase;
		}
		StringBuffer sql = new StringBuffer();
		
		sql.append("WITH TEMP AS ( ");
		sql.append("    SELECT PCODE, SENDERPAYBANKID, OUTPAYBANKID, INPAYBANKID, WOPAYBANKID, SENDERACQUIRE, OUTACQUIRE, INACQUIRE, WOACQUIRE, "); 
//		sql.append("    (CASE '" + clearingPhase + "' WHEN '' THEN NEWSENDERFEE ELSE SENDERFEE END) AS NEWSENDERFEE, ");
//		sql.append("    (CASE '" + clearingPhase + "' WHEN '' THEN NEWOUTFEE ELSE OUTFEE END) AS NEWOUTFEE, "); 
//		sql.append("    (CASE '" + clearingPhase + "' WHEN '' THEN NEWINFEE ELSE INFEE END) AS NEWINFEE, "); 
//		sql.append("    (CASE WHEN (CASE '" + clearingPhase + "' WHEN '' THEN RESULTSTATUS ELSE GETFIRSTSTATUS(TXDATE, STAN, '" + endDate + "', '" + clearingPhase + "') END) <> 'R' THEN '1' ELSE (CASE ACCTCODE WHEN '0' THEN '0' ELSE '1' END) END) AS ACCTCODE ");
		sql.append("    (CASE '" + tmpClearingPhase + "' WHEN '' THEN NEWSENDERFEE ELSE SENDERFEE END) AS NEWSENDERFEE, ");
		sql.append("    (CASE '" + tmpClearingPhase + "' WHEN '' THEN NEWOUTFEE ELSE OUTFEE END) AS NEWOUTFEE, "); 
		sql.append("    (CASE '" + tmpClearingPhase + "' WHEN '' THEN NEWINFEE ELSE INFEE END) AS NEWINFEE, "); 
		sql.append("    (CASE '" + tmpClearingPhase + "' WHEN '' THEN NEWWOFEE ELSE WOFEE END) AS NEWWOFEE, "); 
		sql.append("    (CASE WHEN (CASE '" + tmpClearingPhase + "' WHEN '' THEN RESULTSTATUS ELSE GETFIRSTSTATUS(TXDATE, STAN, '" + endDate + "', '" + tmpClearingPhase + "') END) <> 'R' THEN '1' ELSE (CASE ACCTCODE WHEN '0' THEN '0' ELSE '1' END) END) AS ACCTCODE ");
		sql.append("    FROM VW_ONBLOCKTAB ");
		sql.append("    WHERE NEWRESULT <> 'R' ");
//		sql.append("    AND (CASE '" + clearingPhase + "' WHEN '' THEN RESULTSTATUS ELSE GETFIRSTSTATUS(TXDATE, STAN, '" + endDate + "', '" + clearingPhase + "') END) <> 'R' ");
		sql.append("    AND (CASE '" + tmpClearingPhase + "' WHEN '' THEN RESULTSTATUS ELSE GETFIRSTSTATUS(TXDATE, STAN, '" + endDate + "', '" + tmpClearingPhase + "') END) <> 'R' ");
		sql.append("    AND SENDERSTATUS <> '1' " + (StrUtils.isEmpty(condition_1)?"":"AND " + condition_1));
		sql.append("    UNION ALL ");
		sql.append("    SELECT PCODE, SENDERPAYBANKID, OUTPAYBANKID, INPAYBANKID, WOPAYBANKID, SENDERACQUIRE, OUTACQUIRE, INACQUIRE, WOACQUIRE, NEWSENDERFEE, NEWOUTFEE, NEWINFEE, NEWWOFEE, ACCTCODE ");
		sql.append("    FROM VW_ONPENDING_EC ");
		sql.append("	" + (StrUtils.isEmpty(condition_2)?"":"WHERE " + condition_2));
		sql.append(") ");
		
		sql.append("SELECT ");
		sql.append("(SELECT GETBKNAME(A.BGBK_ID) FROM SYSIBM.SYSDUMMY1) AS BGBK_ID_NAME, (A.BANKID || '-' || A.BRBK_NAME) AS BANKID, VARCHAR(A.PCODE) AS PCODE, "); 
		sql.append("(CASE A.ACCTCODE WHEN '0' THEN '沖正' ELSE '一般' END) AS ACCTCODE, ");
		sql.append("(SELECT COUNT(*) FROM TEMP WHERE SENDERPAYBANKID = A.BANKID AND PCODE = A.PCODE AND ACCTCODE = A.ACCTCODE) AS FIRECOUNT, ");
		sql.append("COALESCE((SELECT SUM(NEWSENDERFEE) FROM TEMP WHERE SENDERPAYBANKID = A.BANKID AND PCODE = A.PCODE AND ACCTCODE = A.ACCTCODE),0) AS FIREAMT, ");
		sql.append("(SELECT COUNT(*) FROM TEMP WHERE OUTPAYBANKID = A.BANKID AND PCODE = A.PCODE AND ACCTCODE = A.ACCTCODE) AS DEBITCOUNT, ");
		sql.append("COALESCE((SELECT SUM(NEWOUTFEE) FROM TEMP WHERE OUTPAYBANKID = A.BANKID AND PCODE = A.PCODE AND ACCTCODE = A.ACCTCODE),0) AS DEBITAMT, ");
		sql.append("(SELECT COUNT(*) FROM TEMP WHERE INPAYBANKID = A.BANKID AND PCODE = A.PCODE AND ACCTCODE = A.ACCTCODE) AS SAVECOUNT, ");
		sql.append("COALESCE((SELECT SUM(NEWINFEE) FROM TEMP WHERE INPAYBANKID = A.BANKID AND PCODE = A.PCODE AND ACCTCODE = A.ACCTCODE),0) AS SAVEAMT, ");
		sql.append("(SELECT COUNT(*) FROM TEMP WHERE WOPAYBANKID = A.BANKID AND PCODE = A.PCODE AND ACCTCODE = A.ACCTCODE) AS CANCELCOUNT, ");
		sql.append("COALESCE((SELECT SUM(NEWWOFEE) FROM TEMP WHERE WOPAYBANKID = A.BANKID AND PCODE = A.PCODE AND ACCTCODE = A.ACCTCODE),0) AS CANCELAMT, ");
		sql.append("A.BGBK_ID, A.OPBK_ID, A.OPBK_NAME ");
		
		sql.append("FROM ( ");
		sql.append("    SELECT ROWNUMBER() OVER(" + parseSidx(sidx, sord) + ") AS ROWNUMBER, T.* FROM ( ");
		sql.append("        SELECT Y.*, COALESCE((SELECT BRBK_NAME FROM BANK_BRANCH WHERE BRBK_ID = Y.BANKID),'') AS BRBK_NAME, ");
		sql.append("		COALESCE((SELECT BGBK_NAME FROM BANK_GROUP WHERE BGBK_ID = Y.OPBK_ID),'') AS OPBK_NAME ");
		sql.append("        FROM ( ");
		sql.append("            SELECT X.*, (SELECT GETBKHEADID(X.BANKID) FROM SYSIBM.SYSDUMMY1) AS BGBK_ID ");
		sql.append("            FROM ( ");
		sql.append("                SELECT SENDERACQUIRE AS OPBK_ID, SENDERPAYBANKID AS BANKID, PCODE, (CASE ACCTCODE WHEN '0' THEN '0' ELSE '1' END) AS ACCTCODE FROM TEMP GROUP BY SENDERACQUIRE, SENDERPAYBANKID, PCODE, ACCTCODE ");
		sql.append("                UNION ");
		sql.append("                SELECT OUTACQUIRE, OUTPAYBANKID, PCODE, (CASE ACCTCODE WHEN '0' THEN '0' ELSE '1' END) AS ACCTCODE FROM TEMP GROUP BY OUTACQUIRE, OUTPAYBANKID, PCODE, ACCTCODE ");
		sql.append("                UNION ");
		sql.append("                SELECT INACQUIRE, INPAYBANKID, PCODE, (CASE ACCTCODE WHEN '0' THEN '0' ELSE '1' END) AS ACCTCODE FROM TEMP GROUP BY INACQUIRE, INPAYBANKID, PCODE, ACCTCODE ");
		sql.append("                UNION ");
		sql.append("                SELECT WOACQUIRE, WOPAYBANKID, PCODE, (CASE ACCTCODE WHEN '0' THEN '0' ELSE '1' END) AS ACCTCODE FROM TEMP WHERE PCODE LIKE '27%' GROUP BY WOACQUIRE, WOPAYBANKID, PCODE, ACCTCODE ");
		sql.append("            ) AS X ");
		sql.append("            " + (StrUtils.isEmpty(condition_3)? "" : "WHERE " + condition_3));
		sql.append("        ) AS Y ");
		sql.append("        " + (StrUtils.isEmpty(condition_4)? "" : "WHERE " + condition_4));
		sql.append("    ) AS T ");
		sql.append(") AS A ");
		
		if(StrUtils.isNotEmpty(sidx)){
			if(sidx.contains("ASC") || sidx.contains("DESC") ||
			   sidx.contains("asc") || sidx.contains("desc")){
				sql.append(" ORDER BY " + sidx);
			}else{
				sql.append(" ORDER BY " + sidx + " " + sord);
			}
		}
		return sql.toString();
	}
	
	public String getSQL_aftb(String startDate, String endDate, String pcode, String clearingPhase, String opbkId, String bgbkId, String brbkId, String sidx, String sord){
		Map<String, String> params = new HashMap<String, String>();
		String tmpClearingPhase = "";
		params.put("START_DATE", startDate);
		params.put("END_DATE", endDate);
		params.put("PCODE", pcode);
		params.put("CLEARINGPHASE", clearingPhase);
		params.put("OPBK_ID", opbkId);
		params.put("BGBK_ID", bgbkId);
		params.put("BRBK_ID", brbkId);
		List<String> conditions = getConditionList(params);
		String condition_1 = "", condition_2 = "", condition_3 = "", condition_4 = "";
		condition_1 = conditions.get(0);
		condition_2 = conditions.get(1);
		condition_3 = conditions.get(2);
		condition_4 = conditions.get(3);
		if(StrUtils.isNotEmpty(clearingPhase) && !clearingPhase.equals("all")){
			tmpClearingPhase = clearingPhase;
		}
		StringBuffer sql = new StringBuffer();
		
		sql.append("WITH TEMP AS ( ");
		sql.append("    SELECT PCODE, SENDERPAYBANKID, OUTPAYBANKID, INPAYBANKID, WOPAYBANKID, SENDERACQUIRE, OUTACQUIRE, INACQUIRE, WOACQUIRE, "); 
//		sql.append("    (CASE '" + clearingPhase + "' WHEN '' THEN NEWSENDERFEE ELSE SENDERFEE END) AS NEWSENDERFEE, ");
//		sql.append("    (CASE '" + clearingPhase + "' WHEN '' THEN NEWOUTFEE ELSE OUTFEE END) AS NEWOUTFEE, "); 
//		sql.append("    (CASE '" + clearingPhase + "' WHEN '' THEN NEWINFEE ELSE INFEE END) AS NEWINFEE, "); 
//		sql.append("    (CASE WHEN (CASE '" + clearingPhase + "' WHEN '' THEN RESULTSTATUS ELSE GETFIRSTSTATUS(TXDATE, STAN, '" + endDate + "', '" + clearingPhase + "') END) <> 'R' THEN '1' ELSE (CASE ACCTCODE WHEN '0' THEN '0' ELSE '1' END) END) AS ACCTCODE ");
		sql.append("    (CASE '" + tmpClearingPhase + "' WHEN '' THEN NEWSENDERFEE ELSE SENDERFEE_NW END) AS NEWSENDERFEE, ");
		sql.append("    (CASE '" + tmpClearingPhase + "' WHEN '' THEN NEWOUTFEE ELSE OUTFEE_NW END) AS NEWOUTFEE, "); 
		sql.append("    (CASE '" + tmpClearingPhase + "' WHEN '' THEN NEWINFEE ELSE INFEE_NW END) AS NEWINFEE, "); 
		sql.append("    (CASE '" + tmpClearingPhase + "' WHEN '' THEN NEWWOFEE ELSE WOFEE_NW END) AS NEWWOFEE, "); 
		sql.append("    (CASE WHEN (CASE '" + tmpClearingPhase + "' WHEN '' THEN RESULTSTATUS ELSE GETFIRSTSTATUS(TXDATE, STAN, '" + endDate + "', '" + tmpClearingPhase + "') END) <> 'R' THEN '1' ELSE (CASE ACCTCODE WHEN '0' THEN '0' ELSE '1' END) END) AS ACCTCODE ");
		sql.append("    FROM VW_ONBLOCKTAB_NW ");
		sql.append("    WHERE NEWRESULT <> 'R' ");
//		sql.append("    AND (CASE '" + clearingPhase + "' WHEN '' THEN RESULTSTATUS ELSE GETFIRSTSTATUS(TXDATE, STAN, '" + endDate + "', '" + clearingPhase + "') END) <> 'R' ");
		sql.append("    AND (CASE '" + tmpClearingPhase + "' WHEN '' THEN RESULTSTATUS ELSE GETFIRSTSTATUS(TXDATE, STAN, '" + endDate + "', '" + tmpClearingPhase + "') END) <> 'R' ");
		sql.append("    AND SENDERSTATUS <> '1' " + (StrUtils.isEmpty(condition_1)?"":"AND " + condition_1));
		sql.append("    UNION ALL ");
		sql.append("    SELECT PCODE, SENDERPAYBANKID, OUTPAYBANKID, INPAYBANKID, WOPAYBANKID, SENDERACQUIRE, OUTACQUIRE, INACQUIRE, WOACQUIRE, NEWSENDERFEE, NEWOUTFEE, NEWINFEE, NEWWOFEE, ACCTCODE ");
		sql.append("    FROM VW_ONPENDING_EC_NW ");
		sql.append("	" + (StrUtils.isEmpty(condition_2)?"":"WHERE " + condition_2));
		sql.append(") ");
		
		sql.append("SELECT ");
		sql.append("(SELECT GETBKNAME(A.BGBK_ID) FROM SYSIBM.SYSDUMMY1) AS BGBK_ID_NAME, (A.BANKID || '-' || A.BRBK_NAME) AS BANKID, VARCHAR(A.PCODE) AS PCODE, "); 
		sql.append("(CASE A.ACCTCODE WHEN '0' THEN '沖正' ELSE '一般' END) AS ACCTCODE, ");
		sql.append("(SELECT COUNT(*) FROM TEMP WHERE SENDERPAYBANKID = A.BANKID AND PCODE = A.PCODE AND ACCTCODE = A.ACCTCODE) AS FIRECOUNT, ");
		sql.append("COALESCE((SELECT SUM(NEWSENDERFEE) FROM TEMP WHERE SENDERPAYBANKID = A.BANKID AND PCODE = A.PCODE AND ACCTCODE = A.ACCTCODE),0) AS FIREAMT, ");
		sql.append("(SELECT COUNT(*) FROM TEMP WHERE OUTPAYBANKID = A.BANKID AND PCODE = A.PCODE AND ACCTCODE = A.ACCTCODE) AS DEBITCOUNT, ");
		sql.append("COALESCE((SELECT SUM(NEWOUTFEE) FROM TEMP WHERE OUTPAYBANKID = A.BANKID AND PCODE = A.PCODE AND ACCTCODE = A.ACCTCODE),0) AS DEBITAMT, ");
		sql.append("(SELECT COUNT(*) FROM TEMP WHERE INPAYBANKID = A.BANKID AND PCODE = A.PCODE AND ACCTCODE = A.ACCTCODE) AS SAVECOUNT, ");
		sql.append("COALESCE((SELECT SUM(NEWINFEE) FROM TEMP WHERE INPAYBANKID = A.BANKID AND PCODE = A.PCODE AND ACCTCODE = A.ACCTCODE),0) AS SAVEAMT, ");
		sql.append("(SELECT COUNT(*) FROM TEMP WHERE WOPAYBANKID = A.BANKID AND PCODE = A.PCODE AND ACCTCODE = A.ACCTCODE) AS CANCELCOUNT, ");
		sql.append("COALESCE((SELECT SUM(NEWWOFEE) FROM TEMP WHERE WOPAYBANKID = A.BANKID AND PCODE = A.PCODE AND ACCTCODE = A.ACCTCODE),0) AS CANCELAMT, ");
		sql.append("A.BGBK_ID, A.OPBK_ID, A.OPBK_NAME ");
		
		sql.append("FROM ( ");
		sql.append("    SELECT ROWNUMBER() OVER(" + parseSidx(sidx, sord) + ") AS ROWNUMBER, T.* FROM ( ");
		sql.append("        SELECT Y.*, COALESCE((SELECT BRBK_NAME FROM BANK_BRANCH WHERE BRBK_ID = Y.BANKID),'') AS BRBK_NAME, ");
		sql.append("		COALESCE((SELECT BGBK_NAME FROM BANK_GROUP WHERE BGBK_ID = Y.OPBK_ID),'') AS OPBK_NAME ");
		sql.append("        FROM ( ");
		sql.append("            SELECT X.*, (SELECT GETBKHEADID(X.BANKID) FROM SYSIBM.SYSDUMMY1) AS BGBK_ID ");
		sql.append("            FROM ( ");
		sql.append("                SELECT SENDERACQUIRE AS OPBK_ID, SENDERPAYBANKID AS BANKID, PCODE, (CASE ACCTCODE WHEN '0' THEN '0' ELSE '1' END) AS ACCTCODE FROM TEMP GROUP BY SENDERACQUIRE, SENDERPAYBANKID, PCODE, ACCTCODE ");
		sql.append("                UNION ");
		sql.append("                SELECT OUTACQUIRE, OUTPAYBANKID, PCODE, (CASE ACCTCODE WHEN '0' THEN '0' ELSE '1' END) AS ACCTCODE FROM TEMP GROUP BY OUTACQUIRE, OUTPAYBANKID, PCODE, ACCTCODE ");
		sql.append("                UNION ");
		sql.append("                SELECT INACQUIRE, INPAYBANKID, PCODE, (CASE ACCTCODE WHEN '0' THEN '0' ELSE '1' END) AS ACCTCODE FROM TEMP GROUP BY INACQUIRE, INPAYBANKID, PCODE, ACCTCODE ");
		sql.append("                UNION ");
		sql.append("                SELECT WOACQUIRE, WOPAYBANKID, PCODE, (CASE ACCTCODE WHEN '0' THEN '0' ELSE '1' END) AS ACCTCODE FROM TEMP WHERE PCODE LIKE '27%' GROUP BY WOACQUIRE, WOPAYBANKID, PCODE, ACCTCODE ");
		sql.append("            ) AS X ");
		sql.append("            " + (StrUtils.isEmpty(condition_3)? "" : "WHERE " + condition_3));
		sql.append("        ) AS Y ");
		sql.append("        " + (StrUtils.isEmpty(condition_4)? "" : "WHERE " + condition_4));
		sql.append("    ) AS T ");
		sql.append(") AS A ");
		
		if(StrUtils.isNotEmpty(sidx)){
			if(sidx.contains("ASC") || sidx.contains("DESC") ||
			   sidx.contains("asc") || sidx.contains("desc")){
				sql.append(" ORDER BY " + sidx);
			}else{
				sql.append(" ORDER BY " + sidx + " " + sord);
			}
		}
		return sql.toString();
	}
	
	public Map<String, String> qs_ex_export(String startDate, String endDate, String pcode, String clearingPhase, String opbkId, String bgbkId, String brbkId, String serchStrs, String sortname, String sortorder){
		Map<String, String> rtnMap = null;
		
		try{
			rtnMap = new HashMap<String, String>();
			rtnMap.put("serchStrs", serchStrs);
			Map<String, Object> params = new HashMap<String, Object>();
			String sql = getSQL(startDate, endDate, pcode, clearingPhase, opbkId, bgbkId, brbkId, sortname, sortorder);
			List list = rponblocktab_Dao.getRptData(sql, new ArrayList());
			
			int pathType = Arguments.getStringArg("ISWINOPEN").equals("Y")?RptUtils.C_PATH :RptUtils.R_PATH;
			String outputFilePath = RptUtils.export(RptUtils.COLLECTION,pathType, "fee_search", "fee_search", params, list, 2);
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
	
	public Map<String, String> qs_ex_export_aftb(String startDate, String endDate, String pcode, String clearingPhase, String opbkId, String bgbkId, String brbkId, String serchStrs, String sortname, String sortorder){
		Map<String, String> rtnMap = null;
		
		try{
			rtnMap = new HashMap<String, String>();
			rtnMap.put("serchStrs", serchStrs);
			Map<String, Object> params = new HashMap<String, Object>();
			String sql = getSQL_aftb(startDate, endDate, pcode, clearingPhase, opbkId, bgbkId, brbkId, sortname, sortorder);
			List list = rponblocktab_Dao.getRptData(sql, new ArrayList());
			
			int pathType = Arguments.getStringArg("ISWINOPEN").equals("Y")?RptUtils.C_PATH :RptUtils.R_PATH;
			String outputFilePath = RptUtils.export(RptUtils.COLLECTION,pathType, "fee_search", "fee_search", params, list, 2);
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
