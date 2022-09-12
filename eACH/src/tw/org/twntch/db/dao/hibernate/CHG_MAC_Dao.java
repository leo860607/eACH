package tw.org.twntch.db.dao.hibernate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;

import tw.org.twntch.po.OPCTRANSACTIONLOGTAB;
import tw.org.twntch.po.OPCTRANSACTIONLOGTAB_PK;
import tw.org.twntch.util.AutoAddScalar;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.zDateHandler;

public class CHG_MAC_Dao extends HibernateEntityDao<OPCTRANSACTIONLOGTAB, Serializable> {
	
	//傳入上行電文中的STAN欄位，查詢表格中的WEBTRACENO欄位
	public List<OPCTRANSACTIONLOGTAB> getByStan(String webTraceNo){
		List<OPCTRANSACTIONLOGTAB> list = null;
		String sql = "SELECT STAN AS PK_STAN,TXDATE AS PK_TXDATE,TXTIME,PCODE,RSPCODE,FEPPROCESSRESULT,CONCODE,CONTIME,FEPTRACENO,"
				   + "(CASE PCODE WHEN '1200' THEN '01' ELSE IDFIELD END) AS IDFIELD,DATAFIELD,INQSTATUS,WEBTRACENO,BANKID, ";
		sql += "COALESCE(BG.BGBK_NAME, (CASE BANKID WHEN '0000000' THEN '全部' ELSE '' END)) AS BANKNAME, COALESCE(( ";
		sql += "    SELECT ERR_DESC ";
		sql += "    FROM TXN_ERROR_CODE ";
		//20150121 Fanny說，FEPPROCESSRESULT回應代碼若為0000表示訊息被認可，與TXN_ERROR_CODE設定不同，故要另外處理
		sql += "    WHERE (CASE OPC.FEPPROCESSRESULT WHEN '0000' THEN '0001' ELSE OPC.FEPPROCESSRESULT END) = ERROR_ID ";
		sql += "),'未定義') AS FEP_ERR_DESC, COALESCE(( ";
		sql += "    SELECT ERR_DESC ";
		sql += "    FROM TXN_ERROR_CODE ";
		sql += "    WHERE OPC.RSPCODE = ERROR_ID ";
		sql += "),'未定義') AS RSP_ERR_DESC ";
		sql += "FROM OPCTRANSACTIONLOGTAB OPC JOIN BANK_GROUP BG ON OPC.BANKID = BG.BGBK_ID ";
		sql += "WHERE TXDATE = :txDate AND WEBTRACENO = :webTraceNo AND STAN LIKE '999%'";
		try{
			SQLQuery query = getCurrentSession().createSQLQuery(sql);
			query.setParameter("txDate", zDateHandler.getDateNum());
			query.setParameter("webTraceNo", webTraceNo);
			
			String cols = "PK_STAN,PK_TXDATE,TXTIME,PCODE,RSPCODE,FEPPROCESSRESULT,CONCODE,CONTIME,FEPTRACENO,IDFIELD,DATAFIELD,INQSTATUS,WEBTRACENO,BANKID,BANKNAME,FEP_ERR_DESC,RSP_ERR_DESC";
			String[] objs =  cols.split(",");
			AutoAddScalar addscalar = new AutoAddScalar();
			addscalar.addScalar(query, OPCTRANSACTIONLOGTAB.class, objs);
			query.setResultTransformer(Transformers.aliasToBean(OPCTRANSACTIONLOGTAB.class));
			list = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return list;
	}
	
	public List<OPCTRANSACTIONLOGTAB> getByTxdate(String txDate, String pcode,String idField, String bgbkId , String rspcode){
		List<OPCTRANSACTIONLOGTAB> list = null;
		
		List<String> conditions = new ArrayList<String>();
		conditions.add(" STAN LIKE '999%' ");
		if(StrUtils.isNotEmpty(txDate)){
			conditions.add(" TXDATE = '" + txDate + "' ");
		}
		if(StrUtils.isNotEmpty(bgbkId)){
			conditions.add(" BANKID = '" + bgbkId + "' ");
		}
//		20150423 add by hugo 
		if(StrUtils.isNotEmpty(idField)){
			conditions.add(" IDFIELD = '"+idField+"' ");
		}
		if(StrUtils.isNotEmpty(pcode)){
			conditions.add(" PCODE = '"+pcode+"' ");
		}
		if(StrUtils.isNotEmpty(rspcode)){
			if(rspcode.equals("S")){
				conditions.add(" RSPCODE = '0001' ");
			}else{
				conditions.add(" RSPCODE != '0001' ");
			}
		}
		
		String where = "";
		for(int i = 0; i < conditions.size(); i++){
			where += conditions.get(i);
			if(i < conditions.size() - 1){
				where += " AND ";
			}
		}
		
		String sql = "SELECT STAN AS PK_STAN,TXDATE AS PK_TXDATE,TXTIME,PCODE , COALESCE(PCODE,'') CHG_PCODE ,RSPCODE,FEPPROCESSRESULT,CONCODE,CONTIME,FEPTRACENO,"
				   + "(CASE PCODE WHEN '1200' THEN '01' ELSE IDFIELD END) AS IDFIELD,DATAFIELD,INQSTATUS,WEBTRACENO,BANKID, ";
		sql += "COALESCE(BG.BGBK_NAME, (CASE BANKID WHEN '0000000' THEN '全部' ELSE '' END)) AS BANKNAME, COALESCE(( ";
		sql += "    SELECT ERR_DESC ";
		sql += "    FROM TXN_ERROR_CODE ";
		//20150121 Fanny說，FEPPROCESSRESULT回應代碼若為0000表示訊息被認可，與TXN_ERROR_CODE設定不同，故要另外處理
		sql += "    WHERE (CASE OPC.FEPPROCESSRESULT WHEN '0000' THEN '0001' ELSE OPC.FEPPROCESSRESULT END) = ERROR_ID ";
		sql += "),'未定義') AS FEP_ERR_DESC, COALESCE(( ";
		sql += "    SELECT ERR_DESC ";
		sql += "    FROM TXN_ERROR_CODE ";
		sql += "    WHERE OPC.RSPCODE = ERROR_ID ";
		sql += "),'未定義') AS RSP_ERR_DESC ";
		sql += "FROM OPCTRANSACTIONLOGTAB OPC LEFT JOIN BANK_GROUP BG ON OPC.BANKID = BG.BGBK_ID ";
		sql += StrUtils.isEmpty(where)?"":"WHERE " + where;
		sql += "ORDER BY STAN";
		try{
			SQLQuery query = getCurrentSession().createSQLQuery(sql);			
			String cols = "PK_STAN,PK_TXDATE,TXTIME,PCODE,RSPCODE,FEPPROCESSRESULT,CONCODE,CONTIME,FEPTRACENO,IDFIELD,DATAFIELD,INQSTATUS,WEBTRACENO,BANKID,BANKNAME,FEP_ERR_DESC,RSP_ERR_DESC , CHG_PCODE";
			String[] objs =  cols.split(",");
			AutoAddScalar addscalar = new AutoAddScalar();
			addscalar.addScalar(query, OPCTRANSACTIONLOGTAB.class, objs);
			query.setResultTransformer(Transformers.aliasToBean(OPCTRANSACTIONLOGTAB.class));
			list = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return list;
	}	
	
	//取得最新STAN並加1(當日不重複)
	public String getStan(){
		String nTraceNo = null;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT CASE MAX(COALESCE(WEBTRACENO,'')) WHEN '' THEN '0000000' ELSE ( ");
		sql.append("	COALESCE(RIGHT(RTRIM(REPEAT('0', 7) || CAST(CAST(MAX(WEBTRACENO) AS INT) + 1 AS CHAR(20))), 7), '0000000') "); 
		sql.append(") END AS N_TRACENO ");
		sql.append("FROM OPCTRANSACTIONLOGTAB ");
		sql.append("WHERE TXDATE = '" + zDateHandler.getDateNum() + "' ");
		try{
			SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
			query.addScalar("N_TRACENO", Hibernate.STRING);
			query.setResultTransformer(Transformers.aliasToBean(OPCTRANSACTIONLOGTAB.class));
			List<OPCTRANSACTIONLOGTAB> list = query.list();
			if(list != null && list.size() > 0){
				nTraceNo = list.get(0).getN_TRACENO();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return nTraceNo;
	}
	
	public void insertTestData(String bankId, String webTraceNo, String idField){
		OPCTRANSACTIONLOGTAB opc = new OPCTRANSACTIONLOGTAB();
		OPCTRANSACTIONLOGTAB_PK id = new OPCTRANSACTIONLOGTAB_PK();
		String stan = UUID.randomUUID().toString();
		id.setSTAN("999" + stan.substring(0,7));
		id.setTXDATE(zDateHandler.getDateNum());
		opc.setId(id);
		opc.setTXTIME(zDateHandler.getTheTime().replaceAll(":", ""));
		opc.setPCODE("1210");
		opc.setRSPCODE("0000");
		opc.setFEPPROCESSRESULT("0000");
		opc.setCONCODE(null);
		opc.setCONTIME(null);
		opc.setFEPTRACENO(opc.getId().getSTAN().substring(3,10));
		opc.setIDFIELD(idField);
		opc.setDATAFIELD(null);
		opc.setINQSTATUS(null);
		opc.setWEBTRACENO(webTraceNo);
		opc.setBANKID(bankId);
		save(opc);
	}
}
