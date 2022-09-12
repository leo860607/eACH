package tw.org.twntch.bo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import tw.org.twntch.bean.TX_ERR;
import tw.org.twntch.db.dao.hibernate.ONBLOCKTAB_Dao;
import tw.org.twntch.db.dao.hibernate.Page;
import tw.org.twntch.db.dao.hibernate.VW_ONBLOCKTAB_Dao;
import tw.org.twntch.po.VW_ONBLOCKTAB;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.StrUtils;

public class TX_ERR_BO {
	private ONBLOCKTAB_Dao onblocktab_Dao;
	private VW_ONBLOCKTAB_Dao vw_onblocktab_Dao;
	
	public String pageSearch(Map<String, String> param){
		String pageNo = StrUtils.isEmpty(param.get("page")) ?"0":param.get("page");
		String pageSize = StrUtils.isEmpty(param.get("rows")) ?Arguments.getStringArg("PAGE.SIZE"):param.get("rows");
		
		Map rtnMap = new HashMap();
		
		List<TX_ERR> list = null;
		Page page = null;
		try {
			list = new ArrayList<TX_ERR>();
			String condition = getConditionList(param).get(0);
			
			StringBuffer withTemp = new StringBuffer();
			withTemp.append("WITH TEMP AS ( ");
			withTemp.append("    SELECT ");
			withTemp.append("    (CASE COALESCE(A.RC2,'') WHEN '' THEN 1 ELSE 0 END) AS TYPE1_FLAG ");
			withTemp.append("    , (CASE COALESCE(A.RC1,'') WHEN '' THEN 1 ELSE 0 END) AS TYPE2_FLAG ");
			withTemp.append("    , (CASE COALESCE(A.RC3,'') WHEN '' THEN 1 ELSE 0 END) AS TYPE3_FLAG ");
			withTemp.append("    , (CASE WHEN SENDERSTATUS = 2 AND RESULTSTATUS = 'P' THEN 1 ELSE 0 END) AS TYPE4_FLAG ");
			withTemp.append("    , (CASE COALESCE(A.RC4,'') WHEN '' THEN 1 ELSE 0 END) AS TYPE5_FLAG ");
			withTemp.append("    , COALESCE(CONRESULTCODE,'') AS CONRESULTCODE, VARCHAR_FORMAT(A.NEWTXDT, 'YYYY/MM/DD HH24:MI:SS') AS TXDT, A.STAN, A.NEWTXAMT AS TXAMT ");
			withTemp.append("    , COALESCE(FLBATCHSEQ,'') AS FLBATCHSEQ, TXDATE ");
			withTemp.append("    , (CASE WHEN (TIMESTAMP_FORMAT(A.DT_REQ_2, 'YYYYMMDDHH24MISSFF', 6) + 3 MINUTE) < CURRENT_TIMESTAMP THEN 1 ELSE 0 END) AS IS_OVER_3_MINS ");
			withTemp.append("    , COALESCE(EACHUSER.GETBKNAME(A.SENDERBANKID),'') AS SENDERBANKID ");
			withTemp.append("    , (COALESCE(EACHUSER.GETBKNAME(A.OUTBANKID),'') || '<br/>' || A.OUTACCTNO) AS OUTBANKID ");
			withTemp.append("    , (COALESCE(EACHUSER.GETBKNAME(A.INBANKID),'') || '<br/>' || A.INACCTNO) AS INBANKID ");
			withTemp.append("    , (A.SENDERID || '<br/>' || EACHUSER.GETCOMPANY_ABBR(A.SENDERID)) AS SENDERID ");
			withTemp.append("    , (A.PCODE || COALESCE((SELECT '-' || EACH_TXN_NAME FROM EACH_TXN_CODE WHERE EACH_TXN_ID = A.PCODE), '') || '<br/>' || A.TXID || COALESCE((SELECT '-' || TXN_NAME FROM TXN_CODE WHERE TXN_ID = A.TXID), '')) AS PCODE ");
			withTemp.append("    FROM VW_ONBLOCKTAB AS A ");
			withTemp.append("    WHERE COALESCE(TRIM(A.DT_REQ_2),'') <> '' ");
			withTemp.append("    " + (StrUtils.isNotEmpty(condition)?"AND " + condition : ""));
			withTemp.append("), TEMP_2 AS ( ");
			withTemp.append("    SELECT ( ");
			withTemp.append("        CASE WHEN IS_OVER_3_MINS = 1 AND TYPE1_FLAG = 1 THEN '01' ");
			withTemp.append("        WHEN IS_OVER_3_MINS = 1 AND TYPE2_FLAG = 1 THEN '02' ");
			withTemp.append("        WHEN CONRESULTCODE <> '' AND TYPE3_FLAG = 1 THEN '03' ");
			withTemp.append("        WHEN FLBATCHSEQ <> '' AND TYPE4_FLAG = 1 THEN '04' ");
			withTemp.append("        WHEN TYPE3_FLAG = 0 AND TYPE5_FLAG = 1 THEN '05' ");
			withTemp.append("        ELSE NULL END ");
			withTemp.append("    ) AS ERR_TYPE, ");
			withTemp.append("    TXDATE, TXDT, STAN, SENDERBANKID, OUTBANKID, INBANKID, SENDERID, PCODE, TXAMT ");
			withTemp.append("    FROM TEMP ");
			withTemp.append("    WHERE (TYPE1_FLAG + TYPE2_FLAG + TYPE3_FLAG + TYPE4_FLAG + TYPE5_FLAG) <> 0 ");
			withTemp.append(") ");
			
			//總計與總數的語法
			StringBuffer countAndSumQuery = new StringBuffer();
			countAndSumQuery.append(withTemp.toString());
			countAndSumQuery.append("SELECT COUNT(*) AS NUM, SUM(TXAMT) AS TXAMT ");
			countAndSumQuery.append("FROM TEMP_2 ");
			countAndSumQuery.append("WHERE ERR_TYPE IS NOT NULL ");
			String countAndSumCols[] = {"NUM", "TXAMT"};
			List<VW_ONBLOCKTAB> countAndSumList = vw_onblocktab_Dao.dataSum(countAndSumQuery.toString(), countAndSumCols, VW_ONBLOCKTAB.class);
			rtnMap.put("dataSumList", countAndSumList);
			
			StringBuffer sql = new StringBuffer();
			sql.append(withTemp.toString());
			sql.append("SELECT * FROM ( ");
			sql.append("    SELECT ROWNUMBER() OVER(" );
			if(StrUtils.isNotEmpty(param.get("sidx"))){
				sql.append("ORDER BY " + param.get("sidx") + " " + param.get("sord"));
			}
			sql.append(") AS ROWNUMBER, C.* ");
			sql.append("    FROM TEMP_2 AS C ");
			sql.append("    WHERE ERR_TYPE IS NOT NULL ");
			//System.out.println("### SQL >> " + sql);
			
			//假的countQuery
			String dummyQuery = "SELECT 1 AS NUM FROM SYSIBM.SYSDUMMY1";
			
			String cols[] = {"ERR_TYPE","TXDATE", "TXDT","STAN","SENDERBANKID","OUTBANKID","INBANKID","SENDERID","PCODE","TXAMT"};
			page = onblocktab_Dao.getDataIII(Integer.valueOf(pageNo), Integer.valueOf(pageSize), dummyQuery, sql.toString(), cols, TX_ERR.class);
			if(countAndSumList != null){
				page.setTotalCount(Long.parseLong(countAndSumList.get(0).getNUM()));
			}
			list = (List<TX_ERR>) page.getResult();
			list = list!=null&& list.size() == 0 ?null:list;
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
	
	public List<String> getConditionList(Map<String, String> param){
		List<String> conditionList = new ArrayList<String>();
		
		List<String> conditions = new ArrayList<String>();
		
		String bizdate = "";
		if(StrUtils.isNotEmpty(param.get("BIZDATE").trim())){
			bizdate = DateTimeUtils.convertDate(param.get("BIZDATE").trim(), "yyyyMMdd", "yyyyMMdd");
			conditions.add(" A.BIZDATE = '" + bizdate + "' ");
		}
		
		String clearingPhase = "";
		if(StrUtils.isNotEmpty(param.get("CLEARINGPHASE").trim()) && !param.get("CLEARINGPHASE").trim().equals("all")){
			clearingPhase = param.get("CLEARINGPHASE").trim();
			conditions.add(" A.CLEARINGPHASE = '" + clearingPhase + "' ");
		}
		
		conditionList.add( combine(conditions) );
		return conditionList;
	}
	
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
			//20200824新增start
			sql.append("(CASE A.RESULTSTATUS WHEN 'R' THEN 0 WHEN 'A' THEN A.SENDERFEE_NW ELSE (CASE COALESCE(OP.RESULTCODE,'00') WHEN '00' THEN A.SENDERFEE_NW ELSE (CASE A.ACCTCODE WHEN '0' THEN A.SENDERFEE_NW ELSE 0 END) END) END) AS NEWSENDERFEE_NW, ");
			sql.append("(CASE A.RESULTSTATUS WHEN 'R' THEN 0 WHEN 'A' THEN A.INFEE_NW ELSE (CASE COALESCE(OP.RESULTCODE,'00') WHEN '00' THEN A.INFEE_NW ELSE (CASE A.ACCTCODE WHEN '0' THEN A.INFEE_NW ELSE 0 END) END) END) AS NEWINFEE_NW, ");
			sql.append("(CASE A.RESULTSTATUS WHEN 'R' THEN 0 WHEN 'A' THEN A.OUTFEE_NW ELSE (CASE COALESCE(OP.RESULTCODE,'00') WHEN '00' THEN A.OUTFEE_NW ELSE (CASE A.ACCTCODE WHEN '0' THEN A.OUTFEE_NW ELSE 0 END) END) END) AS NEWOUTFEE_NW, ");
			sql.append("(CASE A.RESULTSTATUS WHEN 'R' THEN 0 WHEN 'A' THEN A.WOFEE_NW ELSE (CASE COALESCE(OP.RESULTCODE,'00') WHEN '00' THEN A.WOFEE_NW ELSE (CASE A.ACCTCODE WHEN '0' THEN A.WOFEE_NW ELSE 0 END) END) END) AS NEWWOFEE_NW, ");
			sql.append("(CASE A.RESULTSTATUS WHEN 'R' THEN 0 WHEN 'A' THEN A.EACHFEE_NW ELSE (CASE COALESCE(OP.RESULTCODE,'00') WHEN '00' THEN A.EACHFEE_NW ELSE (CASE A.ACCTCODE WHEN '0' THEN A.EACHFEE_NW ELSE 0 END) END) END) AS NEWEACHFEE_NW, ");
			sql.append(" (CASE CAST(A.FEE_TYPE AS VARCHAR(1)) WHEN ' ' THEN '' ELSE CAST(A.FEE_TYPE AS VARCHAR(1)) END) AS FEE_TYPE ,");
			sql.append("(CASE A.RESULTSTATUS WHEN 'R' THEN 0 WHEN 'A' THEN A.HANDLECHARGE_NW ELSE (CASE COALESCE(OP.RESULTCODE,'00') WHEN '00' THEN A.HANDLECHARGE_NW ELSE (CASE A.ACCTCODE WHEN '0' THEN A.HANDLECHARGE_NW ELSE 0 END) END) END) AS NEWFEE_NW, ");
			//20200824新增end
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
	public VW_ONBLOCKTAB_Dao getVw_onblocktab_Dao() {
		return vw_onblocktab_Dao;
	}
	public void setVw_onblocktab_Dao(VW_ONBLOCKTAB_Dao vw_onblocktab_Dao) {
		this.vw_onblocktab_Dao = vw_onblocktab_Dao;
	}
}
