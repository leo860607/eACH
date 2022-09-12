package tw.org.twntch.db.dao.hibernate;

import java.io.Serializable;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;

import tw.org.twntch.po.OPCTRANSACTIONLOGTAB;
import tw.org.twntch.util.AutoAddScalar;

public class UNKNOWN_MSG_Dao extends HibernateEntityDao<OPCTRANSACTIONLOGTAB, Serializable> {
	
	public List<OPCTRANSACTIONLOGTAB> getByStan(String condition){
		List<OPCTRANSACTIONLOGTAB> list = null;
		String sql = "SELECT STAN AS PK_STAN,TXDATE AS PK_TXDATE,TXTIME,PCODE,RSPCODE,FEPPROCESSRESULT,CONCODE,CONTIME,FEPTRACENO,IDFIELD,DATAFIELD,INQSTATUS,WEBTRACENO,BANKID, ";
		sql += "BG.BGBK_NAME AS BANKNAME, COALESCE(( ";
		sql += "    SELECT ERR_DESC ";
		sql += "    FROM TXN_ERROR_CODE ";
		sql += "    WHERE OPC.FEPPROCESSRESULT = ERROR_ID ";
		sql += "),'未定義') AS FEP_ERR_DESC, COALESCE(( ";
		sql += "    SELECT ERR_DESC ";
		sql += "    FROM TXN_ERROR_CODE ";
		sql += "    WHERE OPC.RSPCODE = ERROR_ID ";
		sql += "),'未定義') AS RSP_ERR_DESC ";
		sql += "FROM OPCTRANSACTIONLOGTAB OPC JOIN BANK_GROUP BG ON OPC.BANKID = BG.BGBK_ID ";
		sql += "WHERE " + condition + " AND RSPCODE LIKE '18%' ORDER BY STAN";
		try{
			SQLQuery query = getCurrentSession().createSQLQuery(sql);
			
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
	
	public List<OPCTRANSACTIONLOGTAB> getByTxdate(String txDate){
		List<OPCTRANSACTIONLOGTAB> list = null;
		String sql = "SELECT STAN AS PK_STAN,TXDATE AS PK_TXDATE,TXTIME,PCODE,RSPCODE,FEPPROCESSRESULT,CONCODE,CONTIME,FEPTRACENO,IDFIELD,DATAFIELD,INQSTATUS,WEBTRACENO,BANKID, ";
		sql += "BG.BGBK_NAME AS BANKNAME, COALESCE(( ";
		sql += "    SELECT ERR_DESC ";
		sql += "    FROM TXN_ERROR_CODE ";
		sql += "    WHERE OPC.FEPPROCESSRESULT = ERROR_ID ";
		sql += "),'未定義') AS FEP_ERR_DESC, COALESCE(( ";
		sql += "    SELECT ERR_DESC ";
		sql += "    FROM TXN_ERROR_CODE ";
		sql += "    WHERE OPC.RSPCODE = ERROR_ID ";
		sql += "),'未定義') AS RSP_ERR_DESC ";
		sql += "FROM OPCTRANSACTIONLOGTAB OPC JOIN BANK_GROUP BG ON OPC.BANKID = BG.BGBK_ID ";
		sql += "WHERE TXDATE = :txDate AND RSPCODE LIKE '18%' ORDER BY TXDATE";
		try{
			SQLQuery query = getCurrentSession().createSQLQuery(sql);
			query.setParameter("txDate", txDate);
			
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
	
	public List<OPCTRANSACTIONLOGTAB> getAllData(){
		List<OPCTRANSACTIONLOGTAB> list = null;
		String sql = "SELECT STAN AS PK_STAN,TXDATE AS PK_TXDATE,TXTIME,PCODE,RSPCODE,FEPPROCESSRESULT,CONCODE,CONTIME,FEPTRACENO,IDFIELD,DATAFIELD,INQSTATUS,WEBTRACENO,BANKID, ";
		sql += "BG.BGBK_NAME AS BANKNAME, COALESCE(( ";
		sql += "    SELECT ERR_DESC ";
		sql += "    FROM TXN_ERROR_CODE ";
		sql += "    WHERE OPC.FEPPROCESSRESULT = ERROR_ID ";
		sql += "),'未定義') AS FEP_ERR_DESC, COALESCE(( ";
		sql += "    SELECT ERR_DESC ";
		sql += "    FROM TXN_ERROR_CODE ";
		sql += "    WHERE OPC.RSPCODE = ERROR_ID ";
		sql += "),'未定義') AS RSP_ERR_DESC ";
		sql += "FROM OPCTRANSACTIONLOGTAB OPC JOIN BANK_GROUP BG ON OPC.BANKID = BG.BGBK_ID ";
		sql += "WHERE RSPCODE LIKE '18%' ORDER BY TXDATE";
		try{
			SQLQuery query = getCurrentSession().createSQLQuery(sql);
			
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
	
	public List<OPCTRANSACTIONLOGTAB> getByStanAndTxdate(String condition, String txDate){
		List<OPCTRANSACTIONLOGTAB> list = null;
		String sql = "SELECT STAN AS PK_STAN,TXDATE AS PK_TXDATE,TXTIME,PCODE,RSPCODE,FEPPROCESSRESULT,CONCODE,CONTIME,FEPTRACENO,IDFIELD,DATAFIELD,INQSTATUS,WEBTRACENO,BANKID, ";
		sql += "BG.BGBK_NAME AS BANKNAME, COALESCE(( ";
		sql += "    SELECT ERR_DESC ";
		sql += "    FROM TXN_ERROR_CODE ";
		sql += "    WHERE OPC.FEPPROCESSRESULT = ERROR_ID ";
		sql += "),'未定義') AS FEP_ERR_DESC, COALESCE(( ";
		sql += "    SELECT ERR_DESC ";
		sql += "    FROM TXN_ERROR_CODE ";
		sql += "    WHERE OPC.RSPCODE = ERROR_ID ";
		sql += "),'未定義') AS RSP_ERR_DESC ";
		sql += "FROM OPCTRANSACTIONLOGTAB OPC JOIN BANK_GROUP BG ON OPC.BANKID = BG.BGBK_ID ";
		sql += "WHERE TXDATE = :txDate AND " + condition + " AND RSPCODE LIKE '18%' ORDER BY TXDATE";
		try{
			SQLQuery query = getCurrentSession().createSQLQuery(sql);
			query.setParameter("txDate", txDate);
			
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
}
