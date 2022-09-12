package tw.org.twntch.bo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tw.org.twntch.db.dao.hibernate.Page;
import tw.org.twntch.db.dao.hibernate.VW_ONBLOCKTAB_Dao;
import tw.org.twntch.po.VW_ONBLOCKTAB;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.StrUtils;

public class BATDATA_BO {
	private VW_ONBLOCKTAB_Dao vw_onblocktab_Dao;
	private EACH_USERLOG_BO userlog_bo;
	
	//檢視明細
	public Map searchByPk(String txdate, String stan){
		Map po = null;
		Map rtnMap = new HashMap();
		String condition = "";
		try {
			txdate = StrUtils.isEmpty(txdate)?"":txdate;
			stan = StrUtils.isEmpty(stan)?"":stan;
			
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT TRANSLATE('abcd-ef-gh', VARCHAR(A.TXDATE), 'abcdefgh') AS TXDATE,VARCHAR(A.STAN) AS STAN,VARCHAR_FORMAT(NEWTXDT, 'YYYY-MM-DD HH24:MI:SS') AS TXDT,A.PCODE || '-' || ETC.EACH_TXN_NAME AS PCODE_DESC, ");
			sql.append("A.SENDERBANK || '-' || (SELECT B.BRBK_NAME FROM BANK_BRANCH B WHERE A.SENDERBANK = B.BRBK_ID) AS SENDERBANK_NAME, ");
			sql.append("A.RECEIVERBANK || '-' || (SELECT B.BRBK_NAME FROM BANK_BRANCH B WHERE A.RECEIVERBANK = B.BRBK_ID) AS RECEIVERBANK_NAME, ");
			sql.append("(SELECT B.ERR_DESC FROM TXN_ERROR_CODE B WHERE A.CONRESULTCODE = B.ERROR_ID) AS CONRESULTCODE_DESC,A.ACCTCODE, ");
			sql.append("A.SENDERCLEARING || '-' || (SELECT B.BGBK_NAME FROM BANK_GROUP B WHERE A.SENDERCLEARING = B.BGBK_ID) AS SENDERCLEARING_NAME, ");
			sql.append("A.INCLEARING || '-' || (SELECT B.BGBK_NAME FROM BANK_GROUP B WHERE A.INCLEARING = B.BGBK_ID) AS INCLEARING_NAME, ");
			sql.append("A.OUTCLEARING || '-' || (SELECT B.BGBK_NAME FROM BANK_GROUP B WHERE A.OUTCLEARING = B.BGBK_ID) AS OUTCLEARING_NAME, ");
			sql.append("A.SENDERACQUIRE || '-' || (SELECT B.BGBK_NAME FROM BANK_GROUP B WHERE A.SENDERACQUIRE = B.BGBK_ID) AS SENDERACQUIRE_NAME, ");
			sql.append("A.INACQUIRE || '-' || (SELECT B.BGBK_NAME FROM BANK_GROUP B WHERE A.INACQUIRE = B.BGBK_ID) AS INACQUIRE_NAME, ");
			sql.append("A.OUTACQUIRE || '-' || (SELECT B.BGBK_NAME FROM BANK_GROUP B WHERE A.OUTACQUIRE = B.BGBK_ID) AS OUTACQUIRE_NAME, ");
			sql.append("A.SENDERHEAD || '-' || (SELECT B.BGBK_NAME FROM BANK_GROUP B WHERE A.SENDERHEAD = B.BGBK_ID) AS SENDERHEAD_NAME, ");
			sql.append("A.INHEAD || '-' || (SELECT B.BGBK_NAME FROM BANK_GROUP B WHERE A.INHEAD = B.BGBK_ID) AS INHEAD_NAME, ");
			sql.append("A.OUTHEAD || '-' || (SELECT B.BGBK_NAME FROM BANK_GROUP B WHERE A.OUTHEAD = B.BGBK_ID) AS OUTHEAD_NAME, ");
			sql.append("(CASE A.RESULTSTATUS WHEN 'R' THEN 0 WHEN 'A' THEN A.NEWSENDERFEE ELSE (CASE COALESCE(OP.RESULTCODE,'00') WHEN '00' THEN A.NEWSENDERFEE ELSE (CASE A.ACCTCODE WHEN '0' THEN A.NEWSENDERFEE ELSE 0 END) END) END) AS NEWSENDERFEE, ");
			sql.append("(CASE A.RESULTSTATUS WHEN 'R' THEN 0 WHEN 'A' THEN A.NEWINFEE ELSE (CASE COALESCE(OP.RESULTCODE,'00') WHEN '00' THEN A.NEWINFEE ELSE (CASE A.ACCTCODE WHEN '0' THEN A.NEWINFEE ELSE 0 END) END) END) AS NEWINFEE, ");
			sql.append("(CASE A.RESULTSTATUS WHEN 'R' THEN 0 WHEN 'A' THEN A.NEWOUTFEE ELSE (CASE COALESCE(OP.RESULTCODE,'00') WHEN '00' THEN A.NEWOUTFEE ELSE (CASE A.ACCTCODE WHEN '0' THEN A.NEWOUTFEE ELSE 0 END) END) END) AS NEWOUTFEE, ");
			sql.append("(CASE A.RESULTSTATUS WHEN 'R' THEN 0 WHEN 'A' THEN A.NEWEACHFEE ELSE (CASE COALESCE(OP.RESULTCODE,'00') WHEN '00' THEN A.NEWEACHFEE ELSE (CASE A.ACCTCODE WHEN '0' THEN A.NEWEACHFEE ELSE 0 END) END) END) AS NEWEACHFEE, ");
			//20220321新增
			sql.append("(CASE A.RESULTSTATUS WHEN 'R' THEN 0 WHEN 'A' THEN A.NEWEXTENDFEE ELSE (CASE COALESCE(OP.RESULTCODE,'00') WHEN '00' THEN A.NEWEXTENDFEE ELSE (CASE A.ACCTCODE WHEN '0' THEN A.NEWEXTENDFEE ELSE 0 END) END) END) AS NEWEXTENDFEE, ");
			sql.append(" A.EXTENDFEE AS EXTENDFEE , ");
			//20220321新增end
			sql.append("A.SENDERID,A.RECEIVERID,TRANSLATE('abcd-ef-gh',A.REFUNDDEADLINE,'abcdefgh') AS REFUNDDEADLINE, ");
			sql.append("A.TXID || '-' || TC.TXN_NAME AS TXN_NAME, A.NEWTXAMT AS NEWTXAMT, A.SENDERSTATUS, ");
			sql.append("(CASE A.RESULTSTATUS WHEN 'R' THEN 0 WHEN 'A' THEN A.NEWFEE ELSE (CASE COALESCE(OP.RESULTCODE,'00') WHEN '00' THEN A.NEWFEE ELSE (CASE A.ACCTCODE WHEN '0' THEN A.NEWFEE ELSE 0 END) END) END) AS NEWFEE, ");
			sql.append("A.SENDERBANKID || '-' || (SELECT B.BRBK_NAME FROM BANK_BRANCH B WHERE A.SENDERBANKID = B.BRBK_ID) AS SENDERBANKID_NAME, ");
			sql.append("A.INBANKID || '-' || (SELECT B.BRBK_NAME FROM BANK_BRANCH B WHERE A.INBANKID = B.BRBK_ID) AS INBANKID_NAME, ");
			sql.append("A.OUTBANKID || '-' || (SELECT B.BRBK_NAME FROM BANK_BRANCH B WHERE A.OUTBANKID = B.BRBK_ID) AS OUTBANKID_NAME, ");
			sql.append("TRANSLATE('abcd-ef-gh', A.BIZDATE, 'abcdefgh') AS BIZDATE,TRANSLATE('abcd-ef-gh ij:kl:mn', A.EACHDT, 'abcdefghijklmn') AS EACHDT,A.CLEARINGPHASE,A.INACCTNO,A.OUTACCTNO,A.INID,A.OUTID,A.ACCTBAL,A.AVAILBAL,A.CHECKTYPE,A.MERCHANTID,A.ORDERNO,A.TRMLID,A.TRMLCHECK,A.TRMLMCC,A.BANKRSPMSG,A.RRN, ");
			//20150319 by 李建利  「交易資料查詢」、「未完成交易資料查詢」的檢視明細的「交易結果」顯示最初交易結果即可
			//sql.append("(CASE A.NEWRESULT WHEN 'A' THEN '成功' WHEN 'R' THEN '失敗' ELSE COALESCE((CASE OP.RESULTCODE WHEN '01' THEN '失敗' ELSE '成功' END),(CASE A.SENDERSTATUS WHEN '1' THEN '處理中' ELSE '未完成' END)) END) AS RESP, ");
			sql.append("(CASE A.RESULTSTATUS WHEN 'A' THEN '成功' WHEN 'R' THEN '失敗' ELSE (CASE A.SENDERSTATUS WHEN '1' THEN '處理中' ELSE '未完成' END) END) AS RESP, ");
			sql.append("RC1 || '-' || (SELECT B.ERR_DESC FROM TXN_ERROR_CODE B WHERE A.RC1=B.ERROR_ID) ERR_DESC1, ");
			sql.append("RC2 || '-' || (SELECT B.ERR_DESC FROM TXN_ERROR_CODE B WHERE A.RC2=B.ERROR_ID) ERR_DESC2, ");
			sql.append("RC3 || '-' || (SELECT B.ERR_DESC FROM TXN_ERROR_CODE B WHERE A.RC3=B.ERROR_ID) ERR_DESC3, ");
			sql.append("RC4 || '-' || (SELECT B.ERR_DESC FROM TXN_ERROR_CODE B WHERE A.RC4=B.ERROR_ID) ERR_DESC4, ");
			sql.append("RC5 || '-' || (SELECT B.ERR_DESC FROM TXN_ERROR_CODE B WHERE A.RC5=B.ERROR_ID) ERR_DESC5, ");
			sql.append("RC6 || '-' || (SELECT B.ERR_DESC FROM TXN_ERROR_CODE B WHERE A.RC6=B.ERROR_ID) ERR_DESC6, TRANSLATE('abcd-ef-gh ij:kl:mn.opq',UPDATEDT,'abcdefghijklmnopq') AS UPDATEDT, ");
			sql.append("OBA.USERNO, A.CONMEMO, OBA.SENDERDATA, OBA.COMPANYID, A.MERCHANTID, A.ORDERNO, A.TRMLID, OBA.OTXAMT, COALESCE(TRANSLATE('abcd-ef-gh',OBA.OTXDATE,'abcdefgh'),'') AS OTXDATE, OBA.OTRMLID, OBA.OMERCHANTID, OBA.OORDERNO, OBA.PAN, OBA.OPAN, OBA.PSN, OBA.OPSN, ");
			sql.append("COALESCE(TRANSLATE('abcd-ef-gh',VARCHAR(OP.BIZDATE),'abcdefgh'),'') AS NEWBIZDATE, ");
			sql.append("COALESCE(OP.CLEARINGPHASE,'') AS NEWCLRPHASE, COALESCE(OP.RESULTCODE,'eACH') AS RESULTCODE ");
			sql.append("FROM VW_ONBLOCKTAB A LEFT JOIN EACH_TXN_CODE ETC ON A.PCODE = ETC.EACH_TXN_ID "); 
			sql.append("LEFT JOIN TXN_CODE TC ON A.TXID = TC.TXN_ID ");
			sql.append("LEFT JOIN ONBLOCKAPPENDTAB OBA ON A.TXDATE = OBA.TXDATE AND A.STAN = OBA.STAN ");
			sql.append("LEFT JOIN ONPENDINGTAB OP ON OP.OTXDATE = A.TXDATE AND OP.OSTAN = A.STAN ");
			condition += "WHERE ";
			if(!txdate.equals("")){
				condition += " A.TXDATE='"+txdate+"'";
				condition += " AND ";
			}
			if(!stan.equals("")){
				condition += " A.STAN='"+stan+"'";
			}
			sql.append(condition);
			po = vw_onblocktab_Dao.getDetail(sql.toString(), txdate, stan);
		}catch(Exception e){
			e.printStackTrace();
		}
					
		return po;
	}	
	
	public Map search(Map<String, String> params){
		Map rtnMap = new HashMap();
		
		int pageNo = StrUtils.isEmpty(params.get("page"))?0:Integer.valueOf(params.get("page"));
		int pageSize = StrUtils.isEmpty(params.get("pagesize"))?Integer.valueOf(Arguments.getStringArg("PAGE.SIZE")):Integer.valueOf(params.get("pagesize"));
		String isSearch = StrUtils.isEmpty(params.get("isSearch")) ?"Y":params.get("isSearch");
		
		try{
			String condition = getConditionList(params).get(0);
			
			StringBuffer fromAndWhere_core = new StringBuffer();
			fromAndWhere_core.append("SELECT ROWNUMBER() OVER( ");
			if(StrUtils.isNotEmpty(params.get("sidx"))){
				if("TXDT".equalsIgnoreCase(params.get("sidx"))){
					fromAndWhere_core.append("ORDER BY NEWTXDT " + params.get("sord"));
				}
				else if("TXAMT".equalsIgnoreCase(params.get("sidx"))){
					fromAndWhere_core.append("ORDER BY NEWTXAMT " + params.get("sord"));
				}
				else if("RESP".equalsIgnoreCase(params.get("sidx"))){
					fromAndWhere_core.append("ORDER BY RESULTSTATUS " + params.get("sord"));
				}
				else{
					fromAndWhere_core.append("ORDER BY " + params.get("sidx") + " "+params.get("sord"));
				}
			}
			fromAndWhere_core.append(") AS ROWNUMBER, NEWTXDT, PCODE, SENDERBANKID, OUTBANKID, INBANKID, NEWTXAMT, OUTACCTNO, INACCTNO, STAN, TXDATE, NEWRESULT, UPDATEDT, ");
			fromAndWhere_core.append("(CASE WHEN RESULTSTATUS = 'P' AND NEWRESULT = 'R' THEN '0' ELSE '1' END) AS ACCTCODE, BIZDATE, CLEARINGPHASE, TXID, ");
			fromAndWhere_core.append("SENDERID, RECEIVERID, SENDERACQUIRE, OUTACQUIRE, INACQUIRE, SENDERHEAD, OUTHEAD, INHEAD, RESULTSTATUS, SENDERSTATUS, FLBATCHSEQ ");
			fromAndWhere_core.append("FROM VW_ONBLOCKTAB A ");
			fromAndWhere_core.append((StrUtils.isEmpty(condition))?"" : "WHERE " + condition);
			
			StringBuffer fromAndWhere = new StringBuffer();
			fromAndWhere.append("FROM (" + fromAndWhere_core + ") AS C WHERE ROWNUMBER >= " + (Page.getStartOfPage(pageNo, pageSize) + 1) + " AND ROWNUMBER <= " + pageNo * pageSize + " ");
			StringBuffer fromAndWhere_all = new StringBuffer();
			fromAndWhere_all.append("FROM (" + fromAndWhere_core + ") AS C ");
			
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT VARCHAR_FORMAT(C.NEWTXDT, 'YYYY-MM-DD HH24:MI:SS') AS TXDT, FLBATCHSEQ, "); 
			sql.append("C.PCODE || '-' || COALESCE((SELECT EACH_TXN_NAME FROM EACH_TXN_CODE WHERE EACH_TXN_ID = C.PCODE),'') AS PCODE, "); 
			sql.append("C.SENDERBANKID || '-' || (SELECT COALESCE(BRBK_NAME,'') FROM BANK_BRANCH WHERE BRBK_ID = C.SENDERBANKID) AS SENDERBANKID, "); 
			sql.append("C.OUTBANKID || '-' || (SELECT COALESCE(BRBK_NAME,'') FROM BANK_BRANCH WHERE BRBK_ID = C.OUTBANKID) AS OUTBANKID, "); 
			sql.append("C.INBANKID || '-' || (SELECT COALESCE(BRBK_NAME,'') FROM BANK_BRANCH WHERE BRBK_ID = C.INBANKID) AS INBANKID, ");
			sql.append("C.NEWTXAMT AS TXAMT, C.OUTACCTNO, C.INACCTNO, C.STAN, C.TXDATE, UPDATEDT, BIZDATE, CLEARINGPHASE, ");
			sql.append("C.SENDERID || '-' || COALESCE(GETCOMPANY_ABBR(C.SENDERID), '') AS SENDERID, ");
			sql.append("C.TXID || '-' || COALESCE((SELECT TXN_NAME FROM TXN_CODE WHERE TXN_ID = C.TXID),'') AS TXID, ");
			sql.append("(CASE C.RESULTSTATUS WHEN 'A' THEN '成功' WHEN 'R' THEN '失敗' ELSE (CASE SENDERSTATUS WHEN '1' THEN '處理中' ELSE '未完成' END) END) AS RESP ");
			sql.append(fromAndWhere);
			if(StrUtils.isNotEmpty(params.get("sidx"))){
				if(!"ACCTCODE".equalsIgnoreCase(params.get("sidx"))){
					if("SENDERID".equalsIgnoreCase(params.get("sidx"))){
						sql.append("ORDER BY C." + params.get("sidx"));
					}else{
						sql.append("ORDER BY " + params.get("sidx"));
					}
					if(params.get("sidx").contains("ASC") || params.get("sidx").contains("DESC") ||
					   params.get("sidx").contains("asc") || params.get("sidx").contains("desc")){
					}else{
						sql.append(" " + params.get("sord"));
					}
				}
			}
			//System.out.println("### SQL >> " + sql);
			
			//先算出總計的欄位值，放到rtnMap裡面，到頁面再放到下面的總計Grid裡面
			StringBuffer dataSumSQL = new StringBuffer();
			dataSumSQL.append("SELECT SUM( ");
			dataSumSQL.append("		CASE RESULTSTATUS WHEN 'R' THEN NEWTXAMT ELSE NEWTXAMT END ");
			dataSumSQL.append(") AS TXAMT ");
			dataSumSQL.append(fromAndWhere_all);
			//System.out.println("### DATASUM >> " + dataSumSQL);
			String dataSumCols[] = {"TXAMT"};
			List<VW_ONBLOCKTAB> list = vw_onblocktab_Dao.dataSum(dataSumSQL.toString(),dataSumCols,VW_ONBLOCKTAB.class);
			rtnMap.put("dataSumList", list);
			
			//總筆數
			StringBuffer countQuery = new StringBuffer();
			countQuery.append("SELECT COUNT(*) AS NUM ");
			countQuery.append(fromAndWhere_all);
			//System.out.println("### COUNTSQL >> " + countQuery.toString());
			
			String cols[] = {"TXDT", "FLBATCHSEQ", "PCODE", "SENDERBANKID", "OUTBANKID", "INBANKID", "OUTACCTNO", "INACCTNO","TXDATE","STAN", "TXAMT", "RESP", "SENDERID", "TXID"};
			Page page = vw_onblocktab_Dao.getDataIII(Integer.valueOf(pageNo), Integer.valueOf(pageSize), countQuery.toString(), sql.toString(), cols, VW_ONBLOCKTAB.class);
			list = (List<VW_ONBLOCKTAB>) page.getResult();
			
			list = (list == null || list.size() == 0)? null : list;
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
			
			//必須是按下UI的查詢才紀錄
			if(isSearch.equals("Y")){
				userlog_bo.writeLog("C", null, null, params);
			}
		}catch(Exception e){
			e.printStackTrace();
			rtnMap.put("total", 1);
			rtnMap.put("page", 1);
			rtnMap.put("records", 0);
			rtnMap.put("rows", new ArrayList<>());
			rtnMap.put("msg", "查詢失敗");
			userlog_bo.writeFailLog("C", rtnMap, null, null, params);
		}
		
		return rtnMap;
	}
	
	public List<String> getConditionList(Map<String, String> params){
		List<String> conditionList = new ArrayList<String>();
		List<String> conditions_1 = new ArrayList<String>();
		
		//過濾掉非整批的資料
		conditions_1.add("COALESCE(FLBATCHSEQ, '') <> ''");
		
		if(StrUtils.isNotEmpty(params.get("TXTIME1"))){
			conditions_1.add("BIZDATE >= '" + DateTimeUtils.convertDate(params.get("TXTIME1"), "yyyyMMdd", "yyyyMMdd") + "'");
		}
		
		if(StrUtils.isNotEmpty(params.get("TXTIME2"))){
			conditions_1.add("BIZDATE <= '" + DateTimeUtils.convertDate(params.get("TXTIME2"), "yyyyMMdd", "yyyyMMdd") + "'");
		}
		
		if(StrUtils.isNotEmpty(params.get("FLBATCHSEQ"))){
			conditions_1.add("FLBATCHSEQ = '" + params.get("FLBATCHSEQ").trim() + "'");
		}
		
		if(StrUtils.isNotEmpty(params.get("STAN"))){
			conditions_1.add("STAN = '" + params.get("STAN").trim() + "'");
		}
		
		if(StrUtils.isNotEmpty(params.get("OPBK_ID")) && !params.get("OPBK_ID").equals("all")){
			conditions_1.add("SENDERACQUIRE = '" + params.get("OPBK_ID") + "'");
		}
		
		if(StrUtils.isNotEmpty(params.get("BGBK_ID")) && !params.get("BGBK_ID").equals("all")){
			conditions_1.add("SENDERHEAD = '" + params.get("BGBK_ID") + "'");
		}
		
		if(StrUtils.isNotEmpty(params.get("BRBK_ID")) && !params.get("BRBK_ID").equals("all")){
			conditions_1.add("SENDERBANKID = '" + params.get("BRBK_ID") + "'");
		}
		
		if(StrUtils.isNotEmpty(params.get("CARDNUM_ID"))){
			String cdNumRao = params.get("CDNUMRAO");
			if(cdNumRao.equals("SENDID")){
				conditions_1.add("SENDERID = '" + params.get("CARDNUM_ID").trim() + "'");
			}else if(cdNumRao.equals("RECVID")){
				conditions_1.add("RECEIVERID = '" + params.get("CARDNUM_ID").trim() + "'");
			}
		}
		
		if(StrUtils.isNotEmpty(params.get("ACCTNO"))){
			String opAction1 = params.get("opAction1");
			if(opAction1.equals("IN")){
				conditions_1.add("INACCTNO = '" + params.get("ACCTNO").trim() + "'");
			}else if(opAction1.equals("OUT")){
				conditions_1.add("OUTACCTNO = '" + params.get("ACCTNO").trim() + "'");
			}
		}
		
		if(StrUtils.isNotEmpty(params.get("CLEARINGPHASE"))){
			conditions_1.add("CLEARINGPHASE = '" + params.get("CLEARINGPHASE") + "'");
		}
		
		if(StrUtils.isNotEmpty(params.get("BUSINESS_TYPE_ID")) && !params.get("BUSINESS_TYPE_ID").equals("all")){
			conditions_1.add("SUBSTR(PCODE,1,2) LIKE SUBSTR('" + params.get("BUSINESS_TYPE_ID") + "',1,2)");
		}
		
		if(StrUtils.isNotEmpty(params.get("TXAMT"))){
			conditions_1.add("NEWTXAMT = ISNUMERIC('" + params.get("TXAMT").trim() + "')");
		}
		
		//(U=處理中需另外轉換條件)
		if(StrUtils.isNotEmpty(params.get("RESULTSTATUS"))){
			if(params.get("RESULTSTATUS").equals("U")){
				conditions_1.add("(RESULTSTATUS = 'P' AND SENDERSTATUS = '1')");
			}else if(params.get("RESULTSTATUS").equals("AP")){
				conditions_1.add("( (RESULTSTATUS IN ('A', 'P')) AND SENDERSTATUS <> '1')");
			}else{
				conditions_1.add("(RESULTSTATUS = '" + params.get("RESULTSTATUS") + "' AND SENDERSTATUS <> '1')");
			}
		}
		
		//不包含異常資料才需下條件過濾
		if(params.get("GARBAGEDATA") == null){
			conditions_1.add("COALESCE(GARBAGEDATA,'') <> '*'");
		}
		
		conditionList.add( combine(conditions_1) );
		return conditionList;
	}
	
	public String search_toJson(Map<String, String> param){
		return JSONUtils.map2json(search(param));
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

	public VW_ONBLOCKTAB_Dao getVw_onblocktab_Dao() {
		return vw_onblocktab_Dao;
	}

	public void setVw_onblocktab_Dao(VW_ONBLOCKTAB_Dao vw_onblocktab_Dao) {
		this.vw_onblocktab_Dao = vw_onblocktab_Dao;
	}

	public EACH_USERLOG_BO getUserlog_bo() {
		return userlog_bo;
	}

	public void setUserlog_bo(EACH_USERLOG_BO userlog_bo) {
		this.userlog_bo = userlog_bo;
	}
	
}
