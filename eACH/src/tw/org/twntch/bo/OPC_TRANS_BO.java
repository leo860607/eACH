package tw.org.twntch.bo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.struts.util.LabelValueBean;

import tw.org.twntch.db.dao.hibernate.BANK_GROUP_Dao;
import tw.org.twntch.db.dao.hibernate.EACH_TXN_CODE_Dao;
import tw.org.twntch.db.dao.hibernate.OPC_TRANS_Dao;
import tw.org.twntch.db.dao.hibernate.Page;
import tw.org.twntch.po.BANK_GROUP;
import tw.org.twntch.po.EACH_TXN_CODE;
import tw.org.twntch.po.ONBLOCKTAB;
import tw.org.twntch.po.OPCTRANSACTIONLOGTAB;
import tw.org.twntch.util.CodeUtils;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.StrUtils;

public class OPC_TRANS_BO {
	private OPC_TRANS_Dao opc_trans_Dao;
	private BANK_GROUP_Dao bank_group_Dao;
	private EACH_TXN_CODE_Dao each_txn_code_Dao;
	private EACH_USERLOG_BO userlog_bo;//寫操作軌跡記錄
	
	/**
	 * 取得操作行代號清單
	 * select distinct OPBK_ID where Currentdate between 啟用日期 and 停用日期
	 * @return
	 */
	public List<LabelValueBean> getBgbkIdList(){
		List<BANK_GROUP> list = bank_group_Dao.getBgbkIdList_2();
		List<LabelValueBean> beanList = new LinkedList<LabelValueBean>();
		LabelValueBean bean = null;
		for(BANK_GROUP po : list){
			bean = new LabelValueBean(po.getBGBK_ID() + " - " + po.getBGBK_NAME(), po.getBGBK_ID());
			beanList.add(bean);
		}
		//System.out.println("beanList>>"+beanList);
		return beanList;
	}
	
	/**
	 * 取得交易代號(PCODE-4碼)清單
	 * @return
	 */
	public List<LabelValueBean> getPcodeList(){
		List<EACH_TXN_CODE> list = each_txn_code_Dao.getAll();
		List<LabelValueBean> beanList = new LinkedList<LabelValueBean>();
		LabelValueBean bean = null;
		for(EACH_TXN_CODE po : list){
			bean = new LabelValueBean(po.getEACH_TXN_ID() + " - " + po.getEACH_TXN_NAME(), po.getEACH_TXN_ID());
			beanList.add(bean);
		}
		//System.out.println("beanList>>"+beanList);
		return beanList;
	}
	
	/**
	 * 取得OPC交易代號(PCODE-為1、5開頭)清單
	 * @return
	 */
	public List<LabelValueBean> getOpcTxnCodeList(){
		List<EACH_TXN_CODE> list = each_txn_code_Dao.getOpcTxnCode();
		List<LabelValueBean> beanList = new LinkedList<LabelValueBean>();
		LabelValueBean bean = null;
		for(EACH_TXN_CODE po : list){
			bean = new LabelValueBean(po.getEACH_TXN_ID() + " - " + po.getEACH_TXN_NAME(), po.getEACH_TXN_ID());
			beanList.add(bean);
		}
		//System.out.println("beanList>>"+beanList);
		return beanList;
	}
	
	public String getDataByStan(Map<String, String> params){
		String result = "";
		String paramName;
		String paramValue;
		Map rtnMap = new HashMap();
		String TXDATE = "";
		paramName = "TXDATE";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			TXDATE = paramValue;
		}
		
		String TXTIME_START = "";
		paramName = "TXTIME_START";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			TXTIME_START = paramValue;
		}
		
		String TXTIME_END = "";
		paramName = "TXTIME_END";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			TXTIME_END = paramValue;
		}
		
		String SENDER_TYPE = "";
		paramName = "SENDER_TYPE";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			SENDER_TYPE = paramValue;
		}
		
		String SENDERBANK = "";
		paramName = "SENDERBANK";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			SENDERBANK = paramValue;
		}

		String RECEIVER_TYPE = "";
		paramName = "RECEIVER_TYPE";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			RECEIVER_TYPE = paramValue;
		}
		
		String RECEIVERBANK = "";
		paramName = "RECEIVERBANK";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			RECEIVERBANK = paramValue;
		}
		
		String OPBK_ID = "";
		paramName = "OPBK_ID";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			OPBK_ID = paramValue;
		}
		
		String PCODE = "";
		paramName = "PCODE";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			PCODE = paramValue;
		}
		
		String RSPCODE = "";
		paramName = "RSPCODE";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			RSPCODE = paramValue;
		}
		
		//操作軌跡記錄用
		String SERCHSTRS = "";
		paramName = "serchStrs";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue) ){
			SERCHSTRS = paramValue;
		}
		System.out.println("SERCHSTRS="+SERCHSTRS);
		//將頁面上的查詢條件放進pkMap
		Map<String,Object> pkMap = new HashMap<String,Object>();
		pkMap.put("serchStrs",SERCHSTRS);
		//如果有錯誤要將訊息放進去
		Map<String,Object> msgMap = new HashMap<String,Object>();
		
		String pageNo = StrUtils.isEmpty(params.get("page")) ?"0":params.get("page");
		String pageSize = StrUtils.isEmpty(params.get("rows")) ?Arguments.getStringArg("PAGE.SIZE"):params.get("rows");
		String isSearch = StrUtils.isEmpty(params.get("isSearch")) ?"Y":params.get("isSearch");
		
		List list = null;
		Page page = null;
		List<String> conditions_1 = new ArrayList<String>();
		List<String> conditions_2 = new ArrayList<String>();
		String condition_1 = "", condition_2 = "";
		try{
			//TXDATE
			if(StrUtils.isNotEmpty(TXDATE)){
				//需民國年轉西元年
				TXDATE = DateTimeUtils.convertDate(TXDATE, "yyyyMMdd", "yyyyMMdd");
				conditions_1.add(" TXDATE = '" + TXDATE + "' ");
				conditions_2.add(" TXDATE = '" + TXDATE + "' ");
			}
			//TXTIME
			if(StrUtils.isNotEmpty(TXTIME_START)){
				if(StrUtils.isNotEmpty(TXTIME_END)){
					conditions_1.add(" TXTIME BETWEEN '" + TXTIME_START.replaceAll(":", "") + "' AND '" + TXTIME_END.replaceAll(":", "") + "' ");
					conditions_2.add(" TRANSLATE('ijklmn', TXDT, 'abcdefghijklmn') BETWEEN '" + TXTIME_START.replaceAll(":", "") + "' AND '" + TXTIME_END.replaceAll(":", "") + "' ");
				}else{
					conditions_1.add(" TXTIME >= '" + TXTIME_START.replaceAll(":", "") + "' ");
					conditions_2.add(" TRANSLATE('ijklmn', TXDT, 'abcdefghijklmn') >= '" + TXTIME_START.replaceAll(":", "") + "' ");
				}
			}else{
				if(StrUtils.isNotEmpty(TXTIME_END)){
					conditions_1.add(" TXTIME <= '" + TXTIME_END.replaceAll(":", "") + "' ");
					conditions_2.add(" TRANSLATE('ijklmn', TXDT, 'abcdefghijklmn') <= '" + TXTIME_END.replaceAll(":", "") + "' ");
				}
			}
			//SENDER_TYPE
			if(SENDER_TYPE.equalsIgnoreCase("TCH")){
				conditions_1.add(" STAN LIKE '999%' ");
				conditions_2.add(" SENDERBANK LIKE '999%' ");
			}else if(SENDER_TYPE.equalsIgnoreCase("BANK")){
				if(StrUtils.isNotEmpty(SENDERBANK)){
					conditions_1.add(" SUBSTR(STAN,1,3) = SUBSTR('" + SENDERBANK + "',1,3) ");
					conditions_2.add(" SENDERBANK LIKE '" + SENDERBANK + "%' ");
				}
			}
			//RECEIVER_TYPE
			if(RECEIVER_TYPE.equalsIgnoreCase("TCH")){
				conditions_1.add(" STAN NOT LIKE '999%' ");
				conditions_2.add(" RECEIVERBANK LIKE '999%' ");
			}else if(RECEIVER_TYPE.equalsIgnoreCase("BANK")){
				if(StrUtils.isNotEmpty(RECEIVERBANK)){
					conditions_1.add(" BANKID = '" + RECEIVERBANK + "' ");
					conditions_2.add(" RECEIVERBANK LIKE '" + RECEIVERBANK + "%' ");
				}
			}
			//SENDER_TYPE & RECEIVER_TYPE
			if(StrUtils.isEmpty(SENDER_TYPE) && StrUtils.isEmpty(RECEIVER_TYPE)){
				if(StrUtils.isNotEmpty(SENDERBANK) && StrUtils.isNotEmpty(RECEIVERBANK)){
					conditions_1.add("");
					conditions_2.add(" (SENDERBANK LIKE '" + SENDERBANK + "%' OR RECEIVERBANK LIKE '" + RECEIVERBANK + "%') ");
				}
			}
			//BANKID
			if(StrUtils.isNotEmpty(OPBK_ID) && !OPBK_ID.equals("0000000")){
				conditions_1.add(" (SELECT OPBK_ID FROM BANK_GROUP WHERE BGBK_ID = BANKID) = '" + OPBK_ID + "' ");
				conditions_2.add(" ( (SELECT OPBK_ID FROM BANK_GROUP WHERE BGBK_ID = SENDERBANK) = '" + OPBK_ID + "' OR (SELECT OPBK_ID FROM BANK_GROUP WHERE BGBK_ID = RECEIVERBANK) = '" + OPBK_ID + "' ) ");
			}
			//PCODE
			if(StrUtils.isNotEmpty(PCODE) && !PCODE.equals("all")){
				conditions_1.add(" PCODE = '" + PCODE + "' ");
				conditions_2.add(" PCODE = '" + PCODE + "' ");
			}
			//RSPCODE
			if(StrUtils.isNotEmpty(RSPCODE) && !RSPCODE.equals("all") && !RSPCODE.equals("fail")){
				conditions_1.add(" COALESCE(RSPCODE,'') = '" + RSPCODE + "' ");
				conditions_2.add(" COALESCE(RSPCODE,'') = '" + RSPCODE + "' ");
			}else if(RSPCODE.equals("fail")){
				conditions_1.add(" COALESCE(RSPCODE,'') <> '0001' ");
				conditions_2.add(" COALESCE(RSPCODE,'') <> '0001' ");
			}
			
			condition_1 = combine(conditions_1);
			condition_2 = combine(conditions_2);
			
			StringBuffer countQuery = new StringBuffer();
			countQuery.append("SELECT COUNT(*) AS NUM FROM (");
			countQuery.append("    SELECT TXDATE, TRANSLATE('abcd-ef-gh', TXDATE, 'abcdefgh') AS PK_TXDATE, TRANSLATE('ab:cd:ef', TXTIME, 'abcdef') AS TXTIME, STAN AS PK_STAN, ");
			countQuery.append("    PCODE, PCODE || '-' || COALESCE((SELECT EACH_TXN_NAME FROM EACH_TXN_CODE WHERE EACH_TXN_ID = PCODE),'') AS PNAME, ");
			countQuery.append("    BANKID || '-' || COALESCE((SELECT BGBK_NAME FROM BANK_GROUP WHERE BGBK_ID = BANKID),'') AS BANKID ");
			countQuery.append("    FROM OPCTRANSACTIONLOGTAB ");
			countQuery.append("    " + (StrUtils.isNotEmpty(condition_1)?"WHERE " + condition_1 : "") + " ");
			countQuery.append("    UNION ALL ");
			countQuery.append("    SELECT TXDATE, TRANSLATE('abcd-ef-gh', TXDATE, 'abcdefgh'), TRANSLATE('ij:kl:mn', TXDT, 'abcdefghijklmn'), STAN, ");
			countQuery.append("    PCODE, PCODE || '-' || COALESCE((SELECT EACH_TXN_NAME FROM EACH_TXN_CODE WHERE EACH_TXN_ID = PCODE),'') AS PNAME, ");
			countQuery.append("    SENDERBANK || '-' || COALESCE((SELECT BGBK_NAME FROM BANK_GROUP WHERE BGBK_ID = SENDERBANK),'') || '->' || RECEIVERBANK || '-' || COALESCE((SELECT BGBK_NAME FROM BANK_GROUP WHERE BGBK_ID = RECEIVERBANK),'') AS BANKID ");
			countQuery.append("    FROM PENDINGLOGTAB ");
			countQuery.append("    " + (StrUtils.isNotEmpty(condition_2)?"WHERE " + condition_2 : "") + " ");
//			countQuery.append("    UNION ALL ");
//			countQuery.append("    SELECT TXDATE, TRANSLATE('abcd-ef-gh', TXDATE, 'abcdefgh'), TRANSLATE('ij:kl:mn', TXDT, 'abcdefghijklmn'), STAN, ");
//			countQuery.append("    PCODE, PCODE || '-' || COALESCE((SELECT EACH_TXN_NAME FROM EACH_TXN_CODE WHERE EACH_TXN_ID = PCODE),'') AS PNAME, ");
//			countQuery.append("    SENDERBANK || '-' || COALESCE((SELECT BGBK_NAME FROM BANK_GROUP WHERE BGBK_ID = SENDERBANK),'') || '->' || RECEIVERBANK || '-' || COALESCE((SELECT BGBK_NAME FROM BANK_GROUP WHERE BGBK_ID = RECEIVERBANK),'') AS BANKID ");
//			countQuery.append("    FROM SETTLEMENTLOGTAB ");
//			countQuery.append("    " + (StrUtils.isNotEmpty(condition_2)?"WHERE " + condition_2 : "") + " ");
			countQuery.append(")");
			
			String sql = "SELECT * FROM ( SELECT ROWNUMBER() OVER( ";
			if(StrUtils.isNotEmpty(params.get("sidx"))){
				sql += " ORDER BY " + params.get("sidx") + " " + params.get("sord");
			}
			sql += ") AS ROWNUMBER, TEMP.* FROM ("
			    + "SELECT TXDATE, TRANSLATE('abcd-ef-gh', TXDATE, 'abcdefgh') AS PK_TXDATE, TRANSLATE('ab:cd:ef', TXTIME, 'abcdef') AS TXTIME, STAN AS PK_STAN, "
			    + "PCODE, PCODE || '-' || COALESCE((SELECT EACH_TXN_NAME FROM EACH_TXN_CODE WHERE EACH_TXN_ID = PCODE),'') AS PNAME, "
			    + "CASE BANKID WHEN '0000000' THEN '全部' ELSE BANKID || '-' || COALESCE((SELECT BGBK_NAME FROM BANK_GROUP WHERE BGBK_ID = BANKID),'') END AS BANKID, "
			    + "COALESCE( (SELECT ERR_DESC FROM TXN_ERROR_CODE WHERE (CASE FEPPROCESSRESULT WHEN '0000' THEN '0001' ELSE FEPPROCESSRESULT END) = ERROR_ID), '未定義') AS FEP_ERR_DESC, "
			    //20150520 BY FANNY 若FEPPROCESSRESULT非0000或0001，則不須顯示RSPCODE
			    + "(CASE WHEN FEPPROCESSRESULT NOT IN ('0000','0001') THEN '' ELSE COALESCE( (SELECT ERR_DESC FROM TXN_ERROR_CODE WHERE RSPCODE = ERROR_ID), '未定義') END) AS RSP_ERR_DESC "
//			    20150714 edit by hugo req by UAT-20150603-04
			    + ", ( CASE   WHEN  CONCODE IS NULL   THEN '' ELSE  CONCODE ||'-'|| COALESCE( (SELECT ERR_DESC FROM TXN_ERROR_CODE WHERE CONCODE = ERROR_ID), '未定義') END )  AS  CONCODE "
			    + "FROM OPCTRANSACTIONLOGTAB "
			    + (StrUtils.isNotEmpty(condition_1)?"WHERE " + condition_1 : "") + " "
			    + "UNION ALL "
			    + "SELECT TXDATE, TRANSLATE('abcd-ef-gh', TXDATE, 'abcdefgh'), TRANSLATE('ij:kl:mn', TXDT, 'abcdefghijklmn'), STAN, "
			    + "PCODE, PCODE || '-' || COALESCE((SELECT EACH_TXN_NAME FROM EACH_TXN_CODE WHERE EACH_TXN_ID = PCODE),'') AS PNAME, "
			    + "SENDERBANK || '-' || COALESCE((SELECT BGBK_NAME FROM BANK_GROUP WHERE BGBK_ID = SENDERBANK),'') || '->' || RECEIVERBANK || '-' || COALESCE((SELECT BGBK_NAME FROM BANK_GROUP WHERE BGBK_ID = RECEIVERBANK),'') AS BANKID, "
			    + "'', "
			    + "COALESCE( (SELECT ERR_DESC FROM TXN_ERROR_CODE WHERE RSPCODE = ERROR_ID), '') "
//			    20150714 edit by hugo req by UAT-20150603-04
			    + ",'' AS  CONCODE "
			    + "FROM PENDINGLOGTAB "
			    + (StrUtils.isNotEmpty(condition_2)?"WHERE " + condition_2 : "") + " "
//			    + "UNION ALL "
//			    + "SELECT TXDATE, TRANSLATE('abcd-ef-gh', TXDATE, 'abcdefgh'), TRANSLATE('ij:kl:mn', TXDT, 'abcdefghijklmn'), STAN, "
//			    + "PCODE, PCODE || '-' || COALESCE((SELECT EACH_TXN_NAME FROM EACH_TXN_CODE WHERE EACH_TXN_ID = PCODE),'') AS PNAME, "
//			    + "SENDERBANK || '-' || COALESCE((SELECT BGBK_NAME FROM BANK_GROUP WHERE BGBK_ID = SENDERBANK),'') || '->' || RECEIVERBANK || '-' || COALESCE((SELECT BGBK_NAME FROM BANK_GROUP WHERE BGBK_ID = RECEIVERBANK),'') AS BANKID, "
//			    + "'', "
//			    + "COALESCE( (SELECT ERR_DESC FROM TXN_ERROR_CODE WHERE RSPCODE = ERROR_ID), '') "
//			    + ",'' AS  CONCODE "
//			    + "FROM SETTLEMENTLOGTAB "
//			    + (StrUtils.isNotEmpty(condition_2)?"WHERE " + condition_2 : "") + " "
			+ ") AS TEMP ";
			
			/*
			String sql = "SELECT STAN AS PK_STAN,TXDATE AS PK_TXDATE,TXTIME,PCODE,RSPCODE,FEPPROCESSRESULT,CONCODE,CONTIME,FEPTRACENO,IDFIELD,DATAFIELD,INQSTATUS,WEBTRACENO,BANKID, ";
			sql += "(CASE BANKID WHEN '0000000' THEN '全部' ELSE BG.BGBK_NAME END) AS BANKNAME, COALESCE(( ";
			sql += "    SELECT ERR_DESC ";
			sql += "    FROM TXN_ERROR_CODE ";
			sql += "    WHERE OPC.FEPPROCESSRESULT = ERROR_ID ";
			sql += "),'未定義') AS FEP_ERR_DESC, COALESCE(( ";
			sql += "    SELECT ERR_DESC ";
			sql += "    FROM TXN_ERROR_CODE ";
			sql += "    WHERE OPC.RSPCODE = ERROR_ID ";
			sql += "),'未定義') AS RSP_ERR_DESC, COALESCE(( ";
			sql += "	SELECT EACH_TXN_NAME ";
			sql += "	FROM EACH_TXN_CODE ";
			sql += "	WHERE EACH_TXN_ID = PCODE ";
			sql += "),'未定義') AS PNAME ";
			sql += "FROM OPCTRANSACTIONLOGTAB OPC LEFT JOIN BANK_GROUP BG ON OPC.BANKID = BG.BGBK_ID ";
			sql += "WHERE ";
			for(int i = 0; i < conditions.size(); i++){
				sql += conditions.get(i);
				if(i < conditions.size() - 1){
					sql += " AND ";
				}
			}
			sql += " ORDER BY TXDATE, TXTIME";
			String cols = "PK_STAN,PK_TXDATE,TXTIME,PCODE,RSPCODE,FEPPROCESSRESULT,CONCODE,CONTIME,FEPTRACENO,IDFIELD,DATAFIELD,INQSTATUS,WEBTRACENO,BANKID,BANKNAME,FEP_ERR_DESC,RSP_ERR_DESC,PNAME";
			*/
			System.out.println("SQL >> " + sql);
			
			String cols = "PK_STAN,PK_TXDATE,TXDATE,TXTIME,PCODE,BANKID,PNAME,FEP_ERR_DESC,RSP_ERR_DESC ,CONCODE";
			
			page = opc_trans_Dao.getData(Integer.valueOf(pageNo), Integer.valueOf(pageSize), countQuery.toString(), sql, cols.split(","), OPCTRANSACTIONLOGTAB.class);
			
			list = (List<ONBLOCKTAB>)page.getResult();
			
			//寫操作軌跡記錄(成功) 必須是按下UI的查詢才紀錄
			if(isSearch.equals("Y")){
				userlog_bo.writeLog("C",null,null,pkMap);
			}
		}catch(Exception e){
			e.printStackTrace();
			
			//寫操作軌跡記錄(失敗)
//			msgMap.put("msg",e.getMessage());
//			userlog_bo.writeFailLog("F",msgMap,null,null,pkMap);
			rtnMap.put("total", "0");
			rtnMap.put("page", "0");
			rtnMap.put("records", "0");
			rtnMap.put("rows", new ArrayList());
			rtnMap.put("result", "FALSE");
			rtnMap.put("msg", "查詢失敗，系統異常");
			rtnMap.put("errmsg", e.toString());
			result = JSONUtils.map2json(rtnMap);
			return result;
		}
		
		
		if(page == null){
			rtnMap.put("total", "0");
			rtnMap.put("page", "0");
			rtnMap.put("records", "0");
			rtnMap.put("rows", new ArrayList());
			rtnMap.put("result", "TRUE");
		}else{
			rtnMap.put("total", page.getTotalPageCount());
			rtnMap.put("page", String.valueOf(page.getCurrentPageNo()));
			rtnMap.put("records", page.getTotalCount());
			rtnMap.put("rows", list);
			rtnMap.put("result", "TRUE");
		}
		
		result = JSONUtils.map2json(rtnMap);
		//System.out.println(result);
		return result;
	}
	
	public List getDetail(Map<String, String> params){
		String result = "";
		String paramName;
		String paramValue;
		
		params = CodeUtils.escapeStringMap(params);
		
		String TXDATE = "";
		paramName = "TXDATE";
		paramValue =CodeUtils.escape(params.get(paramName));
		if (StrUtils.isNotEmpty(paramValue)){
			TXDATE = paramValue;
		}
		
		String STAN = "";
		paramName = "STAN";
		paramValue =CodeUtils.escape(params.get(paramName));
		if (StrUtils.isNotEmpty(paramValue)){
			STAN = paramValue;
		}
		
		String PCODE = "";
		paramName = "PCODE";
		paramValue = CodeUtils.escape(params.get(paramName));
		if (StrUtils.isNotEmpty(paramValue)){
			PCODE = paramValue.equals("all") ? "": paramValue.trim();
		}
		
		System.out.println("TXDATE = " + TXDATE);
		System.out.println("STAN = " + STAN);
		System.out.println("PCODE = " + PCODE);
		
		//依據PCODE，查詢不同TABLE
		List list = null;
		String sql = "";
		String pageType = getPageType(PCODE);
		if(pageType.equals("A")){
			sql = "SELECT STAN, TRANS_DATE(EACHUSER.TRANS_DATE(COALESCE(TXDATE,''), 'T', ''), 'W', '-') AS TXDATE, "
				+ "CASE COALESCE(TXTIME,'') WHEN '' THEN '' ELSE SUBSTR(TXTIME,1,2)||':'||SUBSTR(TXTIME,3,2)||':'||SUBSTR(TXTIME,5,2) END AS TXTIME, "
				+ "PCODE || '-' || COALESCE((SELECT EACH_TXN_NAME FROM EACH_TXN_CODE WHERE EACH_TXN_ID = PCODE ),'未定義') AS PCODE, "
				+ "RSPCODE || '-' || COALESCE((SELECT ERR_DESC FROM TXN_ERROR_CODE WHERE RSPCODE = ERROR_ID ),'未定義') AS RSPCODE, "
				+ "FEPPROCESSRESULT || '-' || COALESCE((SELECT ERR_DESC FROM TXN_ERROR_CODE WHERE (CASE FEPPROCESSRESULT WHEN '0000' THEN '0001' ELSE FEPPROCESSRESULT END) = ERROR_ID),'未定義') AS FEPPROCESSRESULT "
//			    20150714 edit by hugo req by UAT-20150603-04
//				+ "CONCODE, CONTIME, FEPTRACENO, IDFIELD, DATAFIELD, INQSTATUS, WEBTRACENO "
				+ ", CONTIME, FEPTRACENO, IDFIELD, DATAFIELD, INQSTATUS, WEBTRACENO "
				+ ", ( CASE   WHEN  CONCODE IS NULL   THEN '' ELSE  CONCODE ||'-'|| COALESCE( (SELECT ERR_DESC FROM TXN_ERROR_CODE WHERE CONCODE = ERROR_ID), '未定義') END ) AS CONCODE "
				+ " , CASE BANKID WHEN '0000000' THEN '全部' ELSE BANKID || '-' || (SELECT BGBK_NAME FROM BANK_GROUP WHERE BGBK_ID = COALESCE(BANKID,'')) END AS BANKID "
			    + "FROM OPCTRANSACTIONLOGTAB WHERE TXDATE = '" + TXDATE + "' AND STAN = '" + STAN + "' ";
		}else if(pageType.equals("B")){
			sql = "SELECT VARCHAR(STAN) AS STAN, TRANS_DATE(EACHUSER.TRANS_DATE(COALESCE(VARCHAR(TXDATE),''), 'T', ''), 'W', '-') AS TXDATE, "
				+ "VARCHAR(PCODE) || '-' || COALESCE((SELECT EACH_TXN_NAME FROM EACH_TXN_CODE WHERE EACH_TXN_ID = PCODE ),'未定義') AS PCODE, "
				+ "CASE COALESCE(SENDERBANK,'') WHEN '0000000' THEN '全部' WHEN '' THEN SENDERBANK ELSE SENDERBANK || '-' || COALESCE((SELECT BGBK_NAME FROM BANK_GROUP WHERE BGBK_ID = SENDERBANK),'') END AS SENDERBANK, "
				+ "CASE COALESCE(RECEIVERBANK,'') WHEN '0000000' THEN '全部' WHEN '' THEN VARCHAR(RECEIVERBANK) ELSE VARCHAR(RECEIVERBANK) || '-' || COALESCE((SELECT BGBK_NAME FROM BANK_GROUP WHERE BGBK_ID = RECEIVERBANK),'') END AS RECEIVERBANK, TRANSLATE('abcd-ef-gh ij:kl:mn', VARCHAR(TXDT), 'abcdefghijklmn') AS TXDT, "
				+ "VARCHAR(STATUS) AS STATUS, (CASE PENDING WHEN '0' THEN '0-處理中' WHEN '9' THEN '9-完成' ELSE VARCHAR(PENDING) END) AS PENDING, VARCHAR(TIMEOUTCODE) AS TIMEOUTCODE, RSPRESULTCODE, "
				+ "TRANSLATE('abcd-ef-gh', OTXDATE, 'abcdefgh') AS OTXDATE, OSTAN, ISNUMERIC(TXAMT) AS TXAMT, TRMLID, "
				+ "SENDERBANKID || '-' || (SELECT BRBK_NAME FROM BANK_BRANCH WHERE BRBK_ID = SENDERBANKID) AS SENDERBANKID, "
				+ "OUTBANKID || '-' || (SELECT BRBK_NAME FROM BANK_BRANCH WHERE BRBK_ID = OUTBANKID) AS OUTBANKID, "
				+ "INBANKID || '-' || (SELECT BRBK_NAME FROM BANK_BRANCH WHERE BRBK_ID = INBANKID) AS INBANKID, OUTACCTNO, INACCTNO, RESULTCODE, "
				+ "RSPCODE || '-' || COALESCE( (SELECT ERR_DESC FROM TXN_ERROR_CODE WHERE RSPCODE = ERROR_ID), '') AS RSPCODE, DTREQ, DTRSP "
				+ "FROM PENDINGLOGTAB WHERE TXDATE = '" + TXDATE + "' AND STAN = '" + STAN + "' ";
		}
//		else if(pageType.equals("C")){
//			sql = "SELECT VARCHAR(STAN) AS STAN, TRANS_DATE(EACHUSER.TRANS_DATE(COALESCE(VARCHAR(TXDATE),''), 'T', ''), 'W', '-') AS TXDATE, "
//				+ "VARCHAR(PCODE) || '-' || COALESCE((SELECT EACH_TXN_NAME FROM EACH_TXN_CODE WHERE EACH_TXN_ID = PCODE ),'未定義') AS PCODE, "
//				+ "CASE COALESCE(SENDERBANK,'') WHEN '0000000' THEN '全部' WHEN '' THEN SENDERBANK ELSE SENDERBANK || '-' || COALESCE((SELECT BGBK_NAME FROM BANK_GROUP WHERE BGBK_ID = SENDERBANK),'') END AS SENDERBANK, "
//				+ "CASE COALESCE(RECEIVERBANK,'') WHEN '0000000' THEN '全部' WHEN '' THEN VARCHAR(RECEIVERBANK) ELSE VARCHAR(RECEIVERBANK) || '-' || COALESCE((SELECT BGBK_NAME FROM BANK_GROUP WHERE BGBK_ID = RECEIVERBANK),'') END AS RECEIVERBANK, TRANSLATE('abcd-ef-gh ij:kl:mn', VARCHAR(TXDT), 'abcdefghijklmn') AS TXDT, "
//				+ "VARCHAR(STATUS) AS STATUS, (CASE PENDING WHEN '0' THEN '0-處理中' WHEN '9' THEN '9-完成' ELSE VARCHAR(PENDING) END) AS PENDING, VARCHAR(TIMEOUTCODE) AS TIMEOUTCODE, RSPRESULTCODE, "
//				+ "BANKID, TRANSLATE('abcd-ef-gh', BIZDATE, 'abcdefgh') AS BIZDATE, CLEARINGPHASE, BIZFLAG, BIZGROUP || '-' || (SELECT BUSINESS_TYPE_NAME FROM BUSINESS_TYPE WHERE BUSINESS_TYPE_ID = BIZGROUP) AS BIZGROUP, "
//				+ "RSPCODE || '-' || COALESCE( (SELECT ERR_DESC FROM TXN_ERROR_CODE WHERE RSPCODE = ERROR_ID), '') AS RSPCODE, DTREQ, DTRSP " 
//				+ "FROM SETTLEMENTLOGTAB WHERE TXDATE = '" + TXDATE + "' AND STAN = '" + STAN + "' ";
//		}
		list = opc_trans_Dao.getData(sql);
		//System.out.println(JSONUtils.toJson(list));
		
		return list;
	}
	
	//決定檢視明細的種類為 A(OPCTRANSACTIONLOGTAB);B(PENDINGLOGTAB);C(SETTLEMENTLOGTAB)
	public String getPageType(String pcode){
		String type = "A";
		if(StrUtils.isNotEmpty(pcode)){
			int iPcode = Integer.valueOf(pcode);
//			if(iPcode >= 5000){
//				type = "C";
//			}else 
			if(iPcode >= 1400 && iPcode != 1405){
				type = "B";
			}
		}
		return type;
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
	
	public String opcSearch(Map<String, String> param) {
		Map rtnMap = new HashMap();
		List<OPCTRANSACTIONLOGTAB> list = null;
		try {
			list = new ArrayList<OPCTRANSACTIONLOGTAB>();
			String cols[] = { "BANKID", "ERROR1200", "ERROR1210", "ERROR1310" };
			String sql = getSql(param);
			list = opc_trans_Dao.search(sql, cols, OPCTRANSACTIONLOGTAB.class);
			System.out.println("OPCTRANSACTIONLOG.list>>" + list);
			list = list != null && list.size() == 0 ? new ArrayList() : list;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		rtnMap.put("records", list.size());
		rtnMap.put("rows", list);

		String json = JSONUtils.map2json(rtnMap);
		// System.out.println("json->" +json);
		return json;
	}
	
	/**
	 * 取得目前系統日及前一系統日(for AJAX)
	 * @return
	 */
	public String getTxdate(Map<String, String> params){
		Map<String, String> data = new HashMap<String, String>();
		Calendar cal = Calendar.getInstance();
		Date today = cal.getTime();
		data.put("txdate", DateTimeUtils.getDateShort2(today));
		cal.add(Calendar.DAY_OF_YEAR, -1);
		Date yesterday = cal.getTime();
		data.put("txdate_pre",DateTimeUtils.getDateShort2(yesterday));
		
		return JSONUtils.map2json(data);
	}
	
	public String getSql(Map<String, String> param){
		StringBuffer sql = new StringBuffer();
		sql.append(" WITH " );
		sql.append(getWithTemp1200(param) +",");
		sql.append(getWithTemp1210(param) +",");
		sql.append(getWithTemp1310(param));
		sql.append(" SELECT ");
		sql.append(" COALESCE(COALESCE(E1200.BANKID,E1210.BANKID),E1310.BANKID) AS BANKID,  ");
		sql.append(" COALESCE(E1200.ERROR1200,'0')  ERROR1200 ,");
		sql.append(" COALESCE(E1210.ERROR1210,'0') ERROR1210, ");
		sql.append(" COALESCE(E1310.ERROR1310,'0') ERROR1310 ");
		sql.append(" FROM TEMP1200  E1200");
		sql.append(" FULL OUTER JOIN TEMP1210 E1210 ON E1210.BANKID=E1200.BANKID ");
		sql.append(" FULL OUTER JOIN TEMP1310 E1310 ON E1310.BANKID=E1210.BANKID ");
		sql.append("ORDER BY BANKID ASC");
//		if(StrUtils.isNotEmpty(param.get("sidx"))){
//			sql.append(" ORDER BY "+param.get("sidx")+" "+param.get("sord"));
//		}
		System.out.println("sql==>"+sql.toString().toUpperCase());
		return sql.toString();
	}
	
	public String getWithTemp1200(Map<String, String> param){
		StringBuffer withTemp = new StringBuffer();
		String conditionStr_1200 = getCondition_1200(param);
		withTemp.append(" TEMP1200 AS( ");
		withTemp.append(" select A.BANKID , COUNT(*) ERROR1200 from( ");
		withTemp.append(" SELECT TEMP.*  ");
		withTemp.append(" FROM ( ");
		withTemp.append(" SELECT TXDATE,  ");
		withTemp.append(" TRANSLATE('abcd-ef-gh', TXDATE, 'abcdefgh') AS PK_TXDATE, ");
		withTemp.append(" TRANSLATE('ab:cd:ef', TXTIME, 'abcdef') AS TXTIME,  ");
		withTemp.append(" STAN AS PK_STAN, ");
		withTemp.append(" PCODE,  ");
		withTemp.append(" PCODE || '-' || COALESCE((SELECT EACH_TXN_NAME FROM EACH_TXN_CODE WHERE EACH_TXN_ID = PCODE),'') AS PNAME, ");
		withTemp.append(" CASE BANKID WHEN '0000000' THEN '??' ELSE BANKID || '-' || COALESCE((SELECT BGBK_NAME FROM BANK_GROUP WHERE BGBK_ID = BANKID),'') END AS BANKID,  ");
		withTemp.append(" COALESCE( (SELECT ERR_DESC FROM TXN_ERROR_CODE WHERE (CASE FEPPROCESSRESULT WHEN '0000' THEN '0001' ELSE FEPPROCESSRESULT END) = ERROR_ID), '???') AS FEP_ERR_DESC, ");
		withTemp.append(" (CASE WHEN FEPPROCESSRESULT NOT IN ('0000','0001') THEN '' ELSE COALESCE( (SELECT ERR_DESC FROM TXN_ERROR_CODE WHERE RSPCODE = ERROR_ID), '???') END) AS RSP_ERR_DESC , ");
		withTemp.append(" ( CASE   WHEN  CONCODE IS NULL   THEN '' ELSE  CONCODE ||'-'|| COALESCE( (SELECT ERR_DESC FROM TXN_ERROR_CODE WHERE CONCODE = ERROR_ID), '???') END )  AS  CONCODE  ");
		withTemp.append(" FROM OPCTRANSACTIONLOGTAB  ");
		withTemp.append(" WHERE  "+   (StrUtils.isNotEmpty(conditionStr_1200)? conditionStr_1200 : "")
				+ "AND  COALESCE(RSPCODE,'') <> '0001'  ");
		withTemp.append(" UNION ALL  ");
		withTemp.append(" SELECT TXDATE,  ");
		withTemp.append(" TRANSLATE('abcd-ef-gh', TXDATE, 'abcdefgh'),  ");
		withTemp.append(" TRANSLATE('ij:kl:mn', TXDT, 'abcdefghijklmn'), ");
		withTemp.append(" STAN, ");
		withTemp.append(" PCODE, ");
		withTemp.append(" PCODE || '-' || COALESCE((SELECT EACH_TXN_NAME FROM EACH_TXN_CODE WHERE EACH_TXN_ID = PCODE),'') AS PNAME, ");
		withTemp.append(" SENDERBANK || '-' || COALESCE((SELECT BGBK_NAME FROM BANK_GROUP WHERE BGBK_ID = SENDERBANK),'') || '->' || RECEIVERBANK || '-' || COALESCE((SELECT BGBK_NAME FROM BANK_GROUP WHERE BGBK_ID = RECEIVERBANK),'') AS BANKID, ");
		withTemp.append(" '', ");
		withTemp.append(" COALESCE( (SELECT ERR_DESC FROM TXN_ERROR_CODE WHERE RSPCODE = ERROR_ID), '') , ");
		withTemp.append(" '' AS  CONCODE  ");
		withTemp.append(" FROM PENDINGLOGTAB  ");
		withTemp.append("  "+(StrUtils.isNotEmpty(conditionStr_1200)? "WHERE " + conditionStr_1200 : ""));
		withTemp.append(" AND  COALESCE(RSPCODE,'') <> '0001'  ) AS TEMP  ");
		withTemp.append(" ) AS A ");
		withTemp.append(" GROUP by BANKID ");
		withTemp.append(" order by BANKID ");
		withTemp.append(") ");
		return withTemp.toString();
	}
	
	public String getWithTemp1210(Map<String, String> param){
		StringBuffer withTemp = new StringBuffer();
		String conditionStr_1210 = getCondition_1210(param);
		withTemp.append(" TEMP1210 AS( ");
		withTemp.append(" select A.BANKID , COUNT(*) ERROR1210 from( ");
		withTemp.append(" SELECT TEMP.*  ");
		withTemp.append(" FROM ( ");
		withTemp.append(" SELECT TXDATE,  ");
		withTemp.append(" TRANSLATE('abcd-ef-gh', TXDATE, 'abcdefgh') AS PK_TXDATE, ");
		withTemp.append(" TRANSLATE('ab:cd:ef', TXTIME, 'abcdef') AS TXTIME,  ");
		withTemp.append(" STAN AS PK_STAN, ");
		withTemp.append(" PCODE,  ");
		withTemp.append(" PCODE || '-' || COALESCE((SELECT EACH_TXN_NAME FROM EACH_TXN_CODE WHERE EACH_TXN_ID = PCODE),'') AS PNAME, ");
		withTemp.append(" CASE BANKID WHEN '0000000' THEN '??' ELSE BANKID || '-' || COALESCE((SELECT BGBK_NAME FROM BANK_GROUP WHERE BGBK_ID = BANKID),'') END AS BANKID,  ");
		withTemp.append(" COALESCE( (SELECT ERR_DESC FROM TXN_ERROR_CODE WHERE (CASE FEPPROCESSRESULT WHEN '0000' THEN '0001' ELSE FEPPROCESSRESULT END) = ERROR_ID), '???') AS FEP_ERR_DESC, ");
		withTemp.append(" (CASE WHEN FEPPROCESSRESULT NOT IN ('0000','0001') THEN '' ELSE COALESCE( (SELECT ERR_DESC FROM TXN_ERROR_CODE WHERE RSPCODE = ERROR_ID), '???') END) AS RSP_ERR_DESC , ");
		withTemp.append(" ( CASE   WHEN  CONCODE IS NULL   THEN '' ELSE  CONCODE ||'-'|| COALESCE( (SELECT ERR_DESC FROM TXN_ERROR_CODE WHERE CONCODE = ERROR_ID), '???') END )  AS  CONCODE  ");
		withTemp.append(" FROM OPCTRANSACTIONLOGTAB  ");
		withTemp.append(" WHERE  "+   (StrUtils.isNotEmpty(conditionStr_1210)? conditionStr_1210 : "")
				+ "AND  COALESCE(RSPCODE,'') <> '0001'  ");
		withTemp.append(" UNION ALL  ");
		withTemp.append(" SELECT TXDATE,  ");
		withTemp.append(" TRANSLATE('abcd-ef-gh', TXDATE, 'abcdefgh'),  ");
		withTemp.append(" TRANSLATE('ij:kl:mn', TXDT, 'abcdefghijklmn'), ");
		withTemp.append(" STAN, ");
		withTemp.append(" PCODE, ");
		withTemp.append(" PCODE || '-' || COALESCE((SELECT EACH_TXN_NAME FROM EACH_TXN_CODE WHERE EACH_TXN_ID = PCODE),'') AS PNAME, ");
		withTemp.append(" SENDERBANK || '-' || COALESCE((SELECT BGBK_NAME FROM BANK_GROUP WHERE BGBK_ID = SENDERBANK),'') || '->' || RECEIVERBANK || '-' || COALESCE((SELECT BGBK_NAME FROM BANK_GROUP WHERE BGBK_ID = RECEIVERBANK),'') AS BANKID, ");
		withTemp.append(" '', ");
		withTemp.append(" COALESCE( (SELECT ERR_DESC FROM TXN_ERROR_CODE WHERE RSPCODE = ERROR_ID), '') , ");
		withTemp.append(" '' AS  CONCODE  ");
		withTemp.append(" FROM PENDINGLOGTAB  ");
		withTemp.append("  "+(StrUtils.isNotEmpty(conditionStr_1210)? "WHERE " + conditionStr_1210 : ""));
		withTemp.append(" AND  COALESCE(RSPCODE,'') <> '0001'  ) AS TEMP  ");
		withTemp.append(" ) AS A ");
		withTemp.append(" GROUP by BANKID ");
		withTemp.append(" order by BANKID ");
		withTemp.append(") ");
		return withTemp.toString();
	}
	
	public String getWithTemp1310(Map<String, String> param){
		StringBuffer withTemp = new StringBuffer();
		String conditionStr_1310 = getCondition_1310(param);
		withTemp.append(" TEMP1310 AS( ");
		withTemp.append(" select A.BANKID , COUNT(*) ERROR1310 from( ");
		withTemp.append(" SELECT TEMP.*  ");
		withTemp.append(" FROM ( ");
		withTemp.append(" SELECT TXDATE,  ");
		withTemp.append(" TRANSLATE('abcd-ef-gh', TXDATE, 'abcdefgh') AS PK_TXDATE, ");
		withTemp.append(" TRANSLATE('ab:cd:ef', TXTIME, 'abcdef') AS TXTIME,  ");
		withTemp.append(" STAN AS PK_STAN, ");
		withTemp.append(" PCODE,  ");
		withTemp.append(" PCODE || '-' || COALESCE((SELECT EACH_TXN_NAME FROM EACH_TXN_CODE WHERE EACH_TXN_ID = PCODE),'') AS PNAME, ");
		withTemp.append(" CASE BANKID WHEN '0000000' THEN '??' ELSE BANKID || '-' || COALESCE((SELECT BGBK_NAME FROM BANK_GROUP WHERE BGBK_ID = BANKID),'') END AS BANKID,  ");
		withTemp.append(" COALESCE( (SELECT ERR_DESC FROM TXN_ERROR_CODE WHERE (CASE FEPPROCESSRESULT WHEN '0000' THEN '0001' ELSE FEPPROCESSRESULT END) = ERROR_ID), '???') AS FEP_ERR_DESC, ");
		withTemp.append(" (CASE WHEN FEPPROCESSRESULT NOT IN ('0000','0001') THEN '' ELSE COALESCE( (SELECT ERR_DESC FROM TXN_ERROR_CODE WHERE RSPCODE = ERROR_ID), '???') END) AS RSP_ERR_DESC , ");
		withTemp.append(" ( CASE   WHEN  CONCODE IS NULL   THEN '' ELSE  CONCODE ||'-'|| COALESCE( (SELECT ERR_DESC FROM TXN_ERROR_CODE WHERE CONCODE = ERROR_ID), '???') END )  AS  CONCODE  ");
		withTemp.append(" FROM OPCTRANSACTIONLOGTAB  ");
		withTemp.append(" WHERE  "+   (StrUtils.isNotEmpty(conditionStr_1310)? conditionStr_1310 : "")
				+ "AND  COALESCE(RSPCODE,'') <> '0001'  ");
		withTemp.append(" UNION ALL  ");
		withTemp.append(" SELECT TXDATE,  ");
		withTemp.append(" TRANSLATE('abcd-ef-gh', TXDATE, 'abcdefgh'),  ");
		withTemp.append(" TRANSLATE('ij:kl:mn', TXDT, 'abcdefghijklmn'), ");
		withTemp.append(" STAN, ");
		withTemp.append(" PCODE, ");
		withTemp.append(" PCODE || '-' || COALESCE((SELECT EACH_TXN_NAME FROM EACH_TXN_CODE WHERE EACH_TXN_ID = PCODE),'') AS PNAME, ");
		withTemp.append(" SENDERBANK || '-' || COALESCE((SELECT BGBK_NAME FROM BANK_GROUP WHERE BGBK_ID = SENDERBANK),'') || '->' || RECEIVERBANK || '-' || COALESCE((SELECT BGBK_NAME FROM BANK_GROUP WHERE BGBK_ID = RECEIVERBANK),'') AS BANKID, ");
		withTemp.append(" '', ");
		withTemp.append(" COALESCE( (SELECT ERR_DESC FROM TXN_ERROR_CODE WHERE RSPCODE = ERROR_ID), '') , ");
		withTemp.append(" '' AS  CONCODE  ");
		withTemp.append(" FROM PENDINGLOGTAB  ");
		withTemp.append("  "+(StrUtils.isNotEmpty(conditionStr_1310)? "WHERE " + conditionStr_1310 : ""));
		withTemp.append(" AND  COALESCE(RSPCODE,'') <> '0001'  ) AS TEMP  ");
		withTemp.append(" ) AS A ");
		withTemp.append(" GROUP by BANKID ");
		withTemp.append(" order by BANKID ");
		withTemp.append(") ");
		return withTemp.toString();
	}
	
	public String getCondition_1200(Map<String, String> param){
		String conditionStr_1200 = "";
		List<String> conditions_1200 = new ArrayList<String>();
		
		String txdate = param.get("OPCTXDATE").replaceAll("-", "");
		conditions_1200.add(" TXDATE = '" + txdate + "' ");
		conditions_1200.add(" PCODE = '1200' ");
		conditionStr_1200 = combine(conditions_1200);
		return conditionStr_1200;
	}
	
	public String getCondition_1210(Map<String, String> param){
		String conditionStr_1210 = "";
		List<String> conditions_1210 = new ArrayList<String>();
		
		String txdate = param.get("OPCTXDATE").replaceAll("-", "");
		conditions_1210.add(" TXDATE = '" + txdate + "' ");
		conditions_1210.add(" PCODE = '1210' ");
		conditionStr_1210 = combine(conditions_1210);
		return conditionStr_1210;
	}
	
	public String getCondition_1310(Map<String, String> param){
		String conditionStr_1310 = "";
		List<String> conditions_1310 = new ArrayList<String>();
		
		String txdate = param.get("OPCTXDATE").replaceAll("-", "");
		conditions_1310.add(" TXDATE = '" + txdate + "' ");
		conditions_1310.add(" PCODE = '1310' ");
		conditionStr_1310 = combine(conditions_1310);
		return conditionStr_1310;
	}
	

	public OPC_TRANS_Dao getOpc_trans_Dao() {
		return opc_trans_Dao;
	}

	public void setOpc_trans_Dao(OPC_TRANS_Dao opc_trans_Dao) {
		this.opc_trans_Dao = opc_trans_Dao;
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
	public EACH_USERLOG_BO getUserlog_bo() {
		return userlog_bo;
	}
	public void setUserlog_bo(EACH_USERLOG_BO userlog_bo) {
		this.userlog_bo = userlog_bo;
	}	
}
