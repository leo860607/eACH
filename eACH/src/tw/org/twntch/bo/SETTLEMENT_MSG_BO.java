package tw.org.twntch.bo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.struts.util.LabelValueBean;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;

import tw.org.twntch.db.dao.hibernate.BANK_GROUP_Dao;
import tw.org.twntch.db.dao.hibernate.EACH_TXN_CODE_Dao;
import tw.org.twntch.db.dao.hibernate.OPC_TRANS_Dao;
import tw.org.twntch.db.dao.hibernate.Page;
import tw.org.twntch.po.BANK_GROUP;
import tw.org.twntch.po.EACH_TXN_CODE;
import tw.org.twntch.po.ONBLOCKTAB;
import tw.org.twntch.po.OPCTRANSACTIONLOGTAB;
import tw.org.twntch.util.AutoAddScalar;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.StrUtils;

public class SETTLEMENT_MSG_BO {
	private OPC_TRANS_Dao opc_trans_Dao;
	private BANK_GROUP_Dao bank_group_Dao;
	private EACH_TXN_CODE_Dao each_txn_code_Dao;
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
		List<EACH_TXN_CODE> list = each_txn_code_Dao.getOpcTxnCode_Settlement();
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
		
		String pageNo = StrUtils.isEmpty(params.get("page")) ?"0":params.get("page");
		String pageSize = StrUtils.isEmpty(params.get("rows")) ?Arguments.getStringArg("PAGE.SIZE"):params.get("rows");
		
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
			if(StrUtils.isEmpty(PCODE) || PCODE.equals("all")){
				conditions_1.add(" PCODE LIKE '5%' ");
				conditions_2.add(" PCODE LIKE '5%' ");
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
			countQuery.append("    SENDERBANK || '-' || COALESCE((SELECT BGBK_NAME FROM BANK_GROUP WHERE BGBK_ID = SENDERBANK),'') || '->' || RECEIVERBANK || '-' || COALESCE((SELECT BGBK_NAME FROM BANK_GROUP WHERE BGBK_ID = RECEIVERBANK),'') AS BANKID ");
			countQuery.append("    FROM PENDINGLOGTAB ");
			countQuery.append("    " + (StrUtils.isNotEmpty(condition_2)?"WHERE " + condition_2 : "") + " ");
			countQuery.append("    UNION ALL ");
			countQuery.append("    SELECT TXDATE, TRANSLATE('abcd-ef-gh', TXDATE, 'abcdefgh'), TRANSLATE('ij:kl:mn', TXDT, 'abcdefghijklmn'), STAN, ");
			countQuery.append("    PCODE, PCODE || '-' || COALESCE((SELECT EACH_TXN_NAME FROM EACH_TXN_CODE WHERE EACH_TXN_ID = PCODE),'') AS PNAME, ");
			countQuery.append("    SENDERBANK || '-' || COALESCE((SELECT BGBK_NAME FROM BANK_GROUP WHERE BGBK_ID = SENDERBANK),'') || '->' || RECEIVERBANK || '-' || COALESCE((SELECT BGBK_NAME FROM BANK_GROUP WHERE BGBK_ID = RECEIVERBANK),'') AS BANKID ");
			countQuery.append("    FROM SETTLEMENTLOGTAB ");
			countQuery.append("    " + (StrUtils.isNotEmpty(condition_2)?"WHERE " + condition_2 : "") + " ");
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
			    + "COALESCE( (SELECT ERR_DESC FROM TXN_ERROR_CODE WHERE RSPCODE = ERROR_ID), '未定義') AS RSP_ERR_DESC "
			    + "FROM OPCTRANSACTIONLOGTAB "
			    + (StrUtils.isNotEmpty(condition_1)?"WHERE " + condition_1 : "") + " "
			    + "UNION ALL "
			    + "SELECT TXDATE, TRANSLATE('abcd-ef-gh', TXDATE, 'abcdefgh'), TRANSLATE('ij:kl:mn', TXDT, 'abcdefghijklmn'), STAN, "
			    + "PCODE, PCODE || '-' || COALESCE((SELECT EACH_TXN_NAME FROM EACH_TXN_CODE WHERE EACH_TXN_ID = PCODE),'') AS PNAME, "
			    + "SENDERBANK || '-' || COALESCE((SELECT BGBK_NAME FROM BANK_GROUP WHERE BGBK_ID = SENDERBANK),'') || '->' || RECEIVERBANK || '-' || COALESCE((SELECT BGBK_NAME FROM BANK_GROUP WHERE BGBK_ID = RECEIVERBANK),'') AS BANKID, "
			    + "COALESCE( (SELECT ERR_DESC FROM TXN_ERROR_CODE WHERE RSPCODE = ERROR_ID), ''), "
			    + "'' "
			    + "FROM PENDINGLOGTAB "
			    + (StrUtils.isNotEmpty(condition_2)?"WHERE " + condition_2 : "") + " "
			    + "UNION ALL "
			    + "SELECT TXDATE, TRANSLATE('abcd-ef-gh', TXDATE, 'abcdefgh'), TRANSLATE('ij:kl:mn', TXDT, 'abcdefghijklmn'), STAN, "
			    + "PCODE, PCODE || '-' || COALESCE((SELECT EACH_TXN_NAME FROM EACH_TXN_CODE WHERE EACH_TXN_ID = PCODE),'') AS PNAME, "
			    + "SENDERBANK || '-' || COALESCE((SELECT BGBK_NAME FROM BANK_GROUP WHERE BGBK_ID = SENDERBANK),'') || '->' || RECEIVERBANK || '-' || COALESCE((SELECT BGBK_NAME FROM BANK_GROUP WHERE BGBK_ID = RECEIVERBANK),'') AS BANKID, "
			    + "COALESCE( (SELECT ERR_DESC FROM TXN_ERROR_CODE WHERE RSPCODE = ERROR_ID), ''), "
			    + "'' "
			    + "FROM SETTLEMENTLOGTAB "
			    + (StrUtils.isNotEmpty(condition_2)?"WHERE " + condition_2 : "") + " "
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
			
			String cols = "PK_STAN,PK_TXDATE,TXDATE,TXTIME,PCODE,BANKID,PNAME,FEP_ERR_DESC,RSP_ERR_DESC";
			
			page = opc_trans_Dao.getData(Integer.valueOf(pageNo), Integer.valueOf(pageSize), countQuery.toString(), sql, cols.split(","), OPCTRANSACTIONLOGTAB.class);
			list = (List<ONBLOCKTAB>)page.getResult();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		Map rtnMap = new HashMap();
		if(page == null){
			rtnMap.put("total", "1");
			rtnMap.put("page", "0");
			rtnMap.put("records", "0");
			rtnMap.put("rows", new ArrayList());
		}else{
//			rtnMap.put("total", page.getTotalPageCount());
//			long tmpPage = page.getTotalPageCount() == 0? 1:page.getTotalPageCount();
//			rtnMap.put("total", page.getTotalPageCount() );
			rtnMap.put("total", page.getTotalPageCount() );
			rtnMap.put("page", String.valueOf(page.getCurrentPageNo()));
			rtnMap.put("records", page.getTotalCount());
			rtnMap.put("rows", list);
		}
		
		result = JSONUtils.map2json(rtnMap);
		System.out.println("result>>"+result);
		return result;
	}
	
	/**
	 * 結算通知電文查詢
	 * @param params
	 * @return
	 */
	public String pageSearch(Map<String, String> params){
		String result = "";
		String paramName;
		String paramValue;
		
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
		if (StrUtils.isNotEmpty(paramValue) ){
			RSPCODE = paramValue;
		}
		String  sidx = "";
		paramName = "sidx";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue) ){
			sidx = paramValue;
		}
		String  sord = "";
		paramName = "sord";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue) ){
			sord = paramValue;
		}
//		sidx=<TXTIME>
//		sord=<desc>
		String pageNo = StrUtils.isEmpty(params.get("page")) ?"0":params.get("page");
		String pageSize = StrUtils.isEmpty(params.get("rows")) ?Arguments.getStringArg("PAGE.SIZE"):params.get("rows");
		
		List list = null;
		Page page = null;
		List<String> conditions_2 = new ArrayList<String>();
		String  condition_2 = "";
		String orderBy = "";
		try{
			orderBy = StrUtils.isNotEmpty(sidx) ? "ORDER BY "+sidx: "";
			orderBy = StrUtils.isNotEmpty(sidx) && StrUtils.isNotEmpty(sord) ? orderBy+" "+sord: "";
			//TXDATE
			if(StrUtils.isNotEmpty(TXDATE)){
				//需民國年轉西元年
				TXDATE = DateTimeUtils.convertDate(TXDATE, "yyyyMMdd", "yyyyMMdd");
				conditions_2.add(" TXDATE = '" + TXDATE + "' ");
			}
			//TXTIME
			if(StrUtils.isNotEmpty(TXTIME_START)){
				if(StrUtils.isNotEmpty(TXTIME_END)){
					conditions_2.add(" TRANSLATE('ijklmn', TXDT, 'abcdefghijklmn') BETWEEN '" + TXTIME_START.replaceAll(":", "") + "' AND '" + TXTIME_END.replaceAll(":", "") + "' ");
				}else{
					conditions_2.add(" TRANSLATE('ijklmn', TXDT, 'abcdefghijklmn') >= '" + TXTIME_START.replaceAll(":", "") + "' ");
				}
			}else{
				if(StrUtils.isNotEmpty(TXTIME_END)){
					conditions_2.add(" TRANSLATE('ijklmn', TXDT, 'abcdefghijklmn') <= '" + TXTIME_END.replaceAll(":", "") + "' ");
				}
			}
			//SENDER_TYPE
			if(SENDER_TYPE.equalsIgnoreCase("TCH")){
				conditions_2.add(" SENDERBANK LIKE '999%' ");
			}else if(SENDER_TYPE.equalsIgnoreCase("BANK")){
				if(StrUtils.isNotEmpty(SENDERBANK)){
					conditions_2.add(" SENDERBANK LIKE '" + SENDERBANK + "%' ");
				}
			}
			//RECEIVER_TYPE
			if(RECEIVER_TYPE.equalsIgnoreCase("TCH")){
				conditions_2.add(" RECEIVERBANK LIKE '999%' ");
			}else if(RECEIVER_TYPE.equalsIgnoreCase("BANK")){
				if(StrUtils.isNotEmpty(RECEIVERBANK)){
					conditions_2.add(" RECEIVERBANK LIKE '" + RECEIVERBANK + "%' ");
				}
			}
			//SENDER_TYPE & RECEIVER_TYPE
			if(StrUtils.isEmpty(SENDER_TYPE) && StrUtils.isEmpty(RECEIVER_TYPE)){
				if(StrUtils.isNotEmpty(SENDERBANK) && StrUtils.isNotEmpty(RECEIVERBANK)){
					conditions_2.add(" (SENDERBANK LIKE '" + SENDERBANK + "%' OR RECEIVERBANK LIKE '" + RECEIVERBANK + "%') ");
				}
			}
			//BANKID
			if(StrUtils.isNotEmpty(OPBK_ID) && !OPBK_ID.equals("0000000")){
//				conditions_2.add(" ( (SELECT OPBK_ID FROM BANK_OPBK WHERE BGBK_ID = SENDERBANK AND OPBK_ID = '" + OPBK_ID + "') = '" + OPBK_ID + "' OR (SELECT OPBK_ID FROM BANK_OPBK WHERE BGBK_ID = RECEIVERBANK AND OPBK_ID = '" + OPBK_ID + "') = '" + OPBK_ID + "' ) ");
				conditions_2.add(" ( (SELECT OPBK_ID FROM BANK_GROUP WHERE BGBK_ID = SENDERBANK) = '" + OPBK_ID + "' OR (SELECT OPBK_ID FROM BANK_GROUP WHERE BGBK_ID = RECEIVERBANK) = '" + OPBK_ID + "' ) ");
			}
			//PCODE
			if(StrUtils.isNotEmpty(PCODE) && !PCODE.equals("all")){
				conditions_2.add(" PCODE = '" + PCODE + "' ");
			}
			if(StrUtils.isEmpty(PCODE) || PCODE.equals("all")){
				conditions_2.add(" PCODE LIKE '5%' ");
			}
			
			if(StrUtils.isNotEmpty(RSPCODE) && RSPCODE.equals("S")){
//				conditions_2.add(" RSPCODE = '0001' ");
				if(StrUtils.isEmpty(PCODE) || PCODE.equals("all")){
					conditions_2.add(" ((PCODE = '5200' AND COALESCE(RSPCODE , '') = '5101')  OR  (PCODE != '5200' AND COALESCE(RSPCODE , '') = '0001') ) ");
				}else if(StrUtils.isNotEmpty(PCODE) && PCODE.equals("5200")){
					conditions_2.add(" COALESCE(RSPCODE , '') = '5101' ");
				}else{
					conditions_2.add(" COALESCE(RSPCODE , '') = '0001' ");
				}
				//				AND ((PCODE = '5200' AND RSPCODE = '5101')  OR  (PCODE != '5200' AND RSPCODE = '0001') )
			}
			if(StrUtils.isNotEmpty(RSPCODE) && RSPCODE.equals("F")){
//				conditions_2.add(" RSPCODE != '0001' ");
//				conditions_2.add(" COALESCE(RSPCODE , '') != '0001' ");
				if(StrUtils.isEmpty(PCODE) || PCODE.equals("all")){
					conditions_2.add(" ((PCODE = '5200' AND COALESCE(RSPCODE , '') != '5101')  OR  (PCODE != '5200' AND COALESCE(RSPCODE , '') != '0001') ) ");
				}else if(StrUtils.isNotEmpty(PCODE) && PCODE.equals("5200")){
					conditions_2.add(" COALESCE(RSPCODE , '') != '5101' ");
				}else{
					conditions_2.add(" COALESCE(RSPCODE , '') != '0001' ");
				}
//				AND ((PCODE = '5200' AND RSPCODE != '5101')  OR  (PCODE != '5200' AND RSPCODE != '0001') )
			}
			condition_2 = combine(conditions_2);
			StringBuffer sql = new StringBuffer();
//			sql.append(" WITH TEMP AS ( ");
//			sql.append(" SELECT TXDATE , TXDT , STAN AS PK_STAN  ,RSPRESULTCODE,RSPCODE , PCODE ");
			sql.append(" SELECT TXDATE AS PK_TXDATE  , TXDT AS TXTIME  , STAN AS PK_STAN  ,RSPRESULTCODE AS FEP_ERR_DESC ,RSPCODE AS RSP_ERR_DESC , PCODE AS PNAME");
			sql.append(" ,TRANSLATE('abcd-ef-gh', TXDT, 'abcdefgh') AS TXDATE ");
			sql.append(" ,TRANSLATE('ij:kl:mn', TXDT, 'abcdefghijklmn') AS TXDT ");
//			sql.append(" ,TRANSLATE('abcd-ef-gh', TXDATE, 'abcdefgh') AS PK_TXDATE ");
//			sql.append(" ,TRANSLATE('ij:kl:mn', TXDT, 'abcdefghijklmn') AS TXTIME ");
			sql.append(" ,COALESCE( PCODE || '-' || (SELECT EACH_TXN_NAME FROM EACH_TXN_CODE WHERE EACH_TXN_ID = PCODE),'') AS PCODE ");
//			sql.append(" ,COALESCE( PCODE || '-' || (SELECT EACH_TXN_NAME FROM EACH_TXN_CODE WHERE EACH_TXN_ID = PCODE),'') AS PNAME ");
			sql.append(" ,COALESCE( BANKID ||'-'|| (SELECT BGBK_NAME FROM BANK_GROUP WHERE BGBK_ID = BANKID),'') AS BANKID ");
			sql.append(" ,COALESCE( (SELECT ERR_DESC FROM TXN_ERROR_CODE WHERE RSPCODE = ERROR_ID), '') AS RSPCODE ");
			sql.append(" ,COALESCE( (SELECT ERR_DESC FROM TXN_ERROR_CODE WHERE (CASE RSPRESULTCODE WHEN '0000' THEN '0001' ELSE RSPRESULTCODE END) = ERROR_ID), '') AS RSPRESULTCODE ");
//			sql.append(" ,COALESCE( (SELECT ERR_DESC FROM TXN_ERROR_CODE WHERE RSPCODE = ERROR_ID), '') AS RSP_ERR_DESC ");
//			sql.append(" ,COALESCE( (SELECT ERR_DESC FROM TXN_ERROR_CODE WHERE (CASE RSPRESULTCODE WHEN '0000' THEN '0001' ELSE RSPRESULTCODE END) = ERROR_ID), '') AS FEP_ERR_DESC ");
			sql.append(" ,CASE SUBSTR ( COALESCE(PCODE ,'') , 1 ,2)  WHEN  '51' ");
			sql.append(" THEN ");
			sql.append(" COALESCE(SENDERBANK  ||'-'|| ( SELECT BGBK_NAME FROM BANK_GROUP WHERE BGBK_ID = SENDERBANK  ) , '') ");
			sql.append(" ELSE  ");
			sql.append(" COALESCE(RECEIVERBANK ||'-'|| ( SELECT BGBK_NAME FROM BANK_GROUP WHERE BGBK_ID = RECEIVERBANK ),'') "); 
			sql.append(" END AS  BANK ");
			sql.append(" FROM EACHUSER.SETTLEMENTLOGTAB  ");
			sql.append((StrUtils.isNotEmpty(condition_2)?"WHERE " + condition_2 : ""));
//			sql.append(" ) ");
//			sql.append(" SELECT * FROM TEMP ");
			sql.append(orderBy);
//			String cols = "PK_STAN,PK_TXDATE,TXDATE,TXTIME,PCODE,BANKID,PNAME,FEP_ERR_DESC,RSP_ERR_DESC,BANK";
//			String cols = "PK_STAN , PK_TXDATE ,TXTIME ,TXDATE,TXDT,PCODE,BANKID,PNAME,FEP_ERR_DESC,RSP_ERR_DESC,BANK";
			String cols = "PK_STAN , PK_TXDATE ,TXTIME ,TXDATE,TXDT,PCODE,BANKID,PNAME,FEP_ERR_DESC,RSP_ERR_DESC, RSPRESULTCODE , RSPCODE,BANK";
			System.out.println("SQL >> " + sql);
			String countQuery  = "SELECT COUNT(*) AS NUM FROM EACHUSER.SETTLEMENTLOGTAB "+(StrUtils.isNotEmpty(condition_2)?"WHERE " + condition_2 : "");
//			page = opc_trans_Dao.getData(Integer.valueOf(pageNo), Integer.valueOf(pageSize), countQuery.toString(), sql, cols.split(","), OPCTRANSACTIONLOGTAB.class);
			page = opc_trans_Dao.getSettleData(Integer.valueOf(pageNo), Integer.valueOf(pageSize), countQuery.toString(), sql.toString(), cols.split(","), OPCTRANSACTIONLOGTAB.class);
			list = (List<ONBLOCKTAB>)page.getResult();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		Map rtnMap = new HashMap();
		if(page == null){
			rtnMap.put("total", "1");
			rtnMap.put("page", "0");
			rtnMap.put("records", "0");
			rtnMap.put("rows", new ArrayList());
		}else{
//			rtnMap.put("total", page.getTotalPageCount());
//			long tmpPage = page.getTotalPageCount() == 0? 1:page.getTotalPageCount();
//			rtnMap.put("total", page.getTotalPageCount() );
			rtnMap.put("total", page.getTotalPageCount() );
			rtnMap.put("page", String.valueOf(page.getCurrentPageNo()));
			rtnMap.put("records", page.getTotalCount());
			rtnMap.put("rows", list);
		}
		
		result = JSONUtils.map2json(rtnMap);
		System.out.println("result>>"+result);
		return result;
	}
	
	public List getDetail(Map<String, String> params){
		String result = "";
		String paramName;
		String paramValue;
		
		String TXDATE = "";
		paramName = "TXDATE";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			TXDATE = paramValue;
		}
		
		String STAN = "";
		paramName = "STAN";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			STAN = paramValue;
		}
		
		String PCODE = "";
		paramName = "PCODE";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			PCODE = paramValue.trim();
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
				+ "FEPPROCESSRESULT || '-' || COALESCE((SELECT ERR_DESC FROM TXN_ERROR_CODE WHERE (CASE FEPPROCESSRESULT WHEN '0000' THEN '0001' ELSE FEPPROCESSRESULT END) = ERROR_ID),'未定義') AS FEPPROCESSRESULT, "
				+ "CONCODE, CONTIME, FEPTRACENO, IDFIELD, DATAFIELD, INQSTATUS, WEBTRACENO, "
				+ "CASE BANKID WHEN '0000000' THEN '全部' ELSE BANKID || '-' || (SELECT BGBK_NAME FROM BANK_GROUP WHERE BGBK_ID = COALESCE(BANKID,'')) END AS BANKID "
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
		}else if(pageType.equals("C")){
			sql = "SELECT VARCHAR(STAN) AS STAN, TRANS_DATE(EACHUSER.TRANS_DATE(COALESCE(VARCHAR(TXDATE),''), 'T', ''), 'W', '-') AS TXDATE, "
				+ "VARCHAR(PCODE) || '-' || COALESCE((SELECT EACH_TXN_NAME FROM EACH_TXN_CODE WHERE EACH_TXN_ID = PCODE ),'未定義') AS PCODE, "
				+ "CASE COALESCE(SENDERBANK,'') WHEN '0000000' THEN '全部' WHEN '' THEN SENDERBANK ELSE SENDERBANK || '-' || COALESCE((SELECT BGBK_NAME FROM BANK_GROUP WHERE BGBK_ID = SENDERBANK),'') END AS SENDERBANK, "
				+ "CASE COALESCE(RECEIVERBANK,'') WHEN '0000000' THEN '全部' WHEN '' THEN VARCHAR(RECEIVERBANK) ELSE VARCHAR(RECEIVERBANK) || '-' || COALESCE((SELECT BGBK_NAME FROM BANK_GROUP WHERE BGBK_ID = RECEIVERBANK),'') END AS RECEIVERBANK, TRANSLATE('abcd-ef-gh ij:kl:mn', VARCHAR(TXDT), 'abcdefghijklmn') AS TXDT, "
				+ "VARCHAR(STATUS) AS STATUS, (CASE PENDING WHEN '0' THEN '0-處理中' WHEN '9' THEN '9-完成' ELSE VARCHAR(PENDING) END) AS PENDING, VARCHAR(TIMEOUTCODE) AS TIMEOUTCODE, RSPRESULTCODE, "
				+ "BANKID, TRANSLATE('abcd-ef-gh', BIZDATE, 'abcdefgh') AS BIZDATE, CLEARINGPHASE, BIZFLAG, BIZGROUP || '-' || (SELECT BUSINESS_TYPE_NAME FROM BUSINESS_TYPE WHERE BUSINESS_TYPE_ID = BIZGROUP) AS BIZGROUP, "
				+ "RSPCODE || '-' || COALESCE( (SELECT ERR_DESC FROM TXN_ERROR_CODE WHERE RSPCODE = ERROR_ID), '') AS RSPCODE, DTREQ, DTRSP " 
				
				+ " ,COALESCE(ACHPAYAMT , '' ) AS ACHPAYAMT ,COALESCE(ACHPAYCNT , '' ) AS ACHPAYCNT ,COALESCE(ACHRECVAMT , '' ) AS ACHRECVAMT ,COALESCE(ACHRECVCNT , '' ) AS ACHRECVCNT " 
				+ " ,COALESCE(ACHRVSPAYAMT , '' ) AS ACHRVSPAYAMT , COALESCE(ACHRVSPAYCNT , '' ) AS ACHRVSPAYCNT , COALESCE(ACHRVSRECVAMT , '' ) AS ACHRVSRECVAMT  , COALESCE(ACHRVSRECVCNT , '' ) AS ACHRVSRECVCNT  " 
				+ " ,COALESCE(CCACHPAYAMT , '' ) AS CCACHPAYAMT , COALESCE(CCACHPAYCNT , '' ) AS CCACHPAYCNT , COALESCE(CCACHRECVAMT , '' ) AS CCACHRECVAMT , COALESCE(CCACHRECVCNT , '' ) AS CCACHRECVCNT " 
				+ " ,COALESCE(CCACHRVSPAYAMT , '' ) AS CCACHRVSPAYAMT , COALESCE(CCACHRVSPAYCNT , '' ) AS CCACHRVSPAYCNT , COALESCE(CCACHRVSRECVAMT , '' ) AS CCACHRVSRECVAMT , COALESCE(CCACHRVSRECVCNT , '' ) AS CCACHRVSRECVCNT  " 
				+ " ,COALESCE(NETAMT , '' ) AS NETAMT , COALESCE(NETFEEAMT , '' ) AS NETFEEAMT , COALESCE(PAYAMT , '' ) AS PAYAMT " 
				+ " ,COALESCE(PAYCNT , '' ) AS PAYCNT , COALESCE(PAYFEEAMT , '' ) AS PAYFEEAMT , COALESCE(RECVAMT , '' ) AS RECVAMT " 
				+ " ,COALESCE(RECVCNT , '' ) AS RECVCNT , COALESCE(RECVFEEAMT , '' ) AS RECVFEEAMT , COALESCE(RVSPAYFEEAMT , '' ) AS RVSPAYFEEAMT " 
				+ " ,COALESCE(RVSRECVFEEAMT , '' ) AS RVSRECVFEEAMT , COALESCE(TOTALCNT , '' )  AS TOTALCNT  " 
				
				+ "FROM SETTLEMENTLOGTAB WHERE TXDATE = '" + TXDATE + "' AND STAN = '" + STAN + "' ";
		}
		list = opc_trans_Dao.getData(sql);
		//System.out.println(JSONUtils.toJson(list));
		System.out.println("list>>"+list);
		return list;
	}
	
	//決定檢視明細的種類為 A(OPCTRANSACTIONLOGTAB);B(PENDINGLOGTAB);C(SETTLEMENTLOGTAB)
	public String getPageType(String pcode){
		String type = "A";
		if(StrUtils.isNotEmpty(pcode)){
			int iPcode = Integer.valueOf(pcode);
			if(iPcode >= 5000){
				type = "C";
			}else if(iPcode >= 1400 && iPcode != 1405){
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

}
