package tw.org.twntch.db.dao.hibernate;

import java.io.Serializable;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;

import tw.org.twntch.po.MISSINGTRADERLOGTAB;
import tw.org.twntch.po.OPCTRANSACTIONLOGTAB;
import tw.org.twntch.util.AutoAddScalar;

public class MISSINGTRADERLOGTAB_Dao extends HibernateEntityDao<MISSINGTRADERLOGTAB, Serializable> {
	
	public List<MISSINGTRADERLOGTAB> getByStan(String condition){
		List<MISSINGTRADERLOGTAB> list = null;
		String sql = "SELECT "; 
		sql += "TXDATE AS PK_TXDATE, TXTIME, PCODE, BASICDATA, ";
		sql += "COALESCE((CASE WHEN SENDERBANK LIKE '999%' THEN '交換所' ELSE (SELECT BGBK_NAME FROM BANK_GROUP WHERE BGBK_ID = SENDERBANK) END),'') AS SENDERBANK, "; 
		sql += "COALESCE((CASE WHEN RECEIVERBANK LIKE '999%' THEN '交換所' ELSE (SELECT BGBK_NAME FROM BANK_GROUP WHERE BGBK_ID = RECEIVERBANK) END),'') AS RECEIVERBANK, ";
		/*
		sql += "    (CASE SUBSTR(STAN, 1, 3) WHEN '999' THEN '交換所' ELSE ( ";
		sql += "        SELECT B.BGBK_NAME ";
		sql += "        FROM ( ";
		sql += "            SELECT DISTINCT OPBK_ID ";
		sql += "            FROM BANK_GROUP ";
		sql += "        ) A JOIN BANK_GROUP B ON A.OPBK_ID = B.BGBK_ID ";
		sql += "        WHERE SUBSTR(A.OPBK_ID, 1, 3) = SUBSTR(STAN, 1, 3) ";
		sql += "        FETCH FIRST 1 ROW ONLY ";
		sql += "    ) END) AS PK_STAN, ";
		*/
		sql += "COALESCE(E.EACH_TXN_NAME, '') AS PNAME ";
		sql += "FROM MISSINGTRADERLOGTAB M LEFT JOIN EACH_TXN_CODE E ON M.PCODE = E.EACH_TXN_ID ";
		sql += "WHERE " + condition + " ORDER BY TXDATE, TXTIME";
		System.out.println(sql);
		try{
			SQLQuery query = getCurrentSession().createSQLQuery(sql);
			
			String cols = "PK_TXDATE,TXTIME,PCODE,PNAME,BASICDATA,SENDERBANK,RECEIVERBANK";
			String[] objs =  cols.split(",");
			AutoAddScalar addscalar = new AutoAddScalar();
			addscalar.addScalar(query, MISSINGTRADERLOGTAB.class, objs);
			query.setResultTransformer(Transformers.aliasToBean(MISSINGTRADERLOGTAB.class));
			list = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return list;
	}
	
	public List<MISSINGTRADERLOGTAB> getByTxdate(String txDate){
		List<MISSINGTRADERLOGTAB> list = null;
		String sql = "SELECT "; 
		sql += "TXDATE AS PK_TXDATE, TXTIME, PCODE, BASICDATA, ";
		sql += "COALESCE((CASE WHEN SENDERBANK LIKE '999%' THEN '交換所' ELSE (SELECT BGBK_NAME FROM BANK_GROUP WHERE BGBK_ID = SENDERBANK) END),'') AS SENDERBANK, "; 
		sql += "COALESCE((CASE WHEN RECEIVERBANK LIKE '999%' THEN '交換所' ELSE (SELECT BGBK_NAME FROM BANK_GROUP WHERE BGBK_ID = RECEIVERBANK) END),'') AS RECEIVERBANK, ";
		/*
		sql += "    (CASE SUBSTR(STAN, 1, 3) WHEN '999' THEN '交換所' ELSE ( ";
		sql += "        SELECT B.BGBK_NAME ";
		sql += "        FROM ( ";
		sql += "            SELECT DISTINCT OPBK_ID ";
		sql += "            FROM BANK_GROUP ";
		sql += "        ) A JOIN BANK_GROUP B ON A.OPBK_ID = B.BGBK_ID ";
		sql += "        WHERE SUBSTR(A.OPBK_ID, 1, 3) = SUBSTR(STAN, 1, 3) ";
		sql += "        FETCH FIRST 1 ROW ONLY ";
		sql += "    ) END) AS PK_STAN ";
		*/
		sql += "COALESCE(E.EACH_TXN_NAME, '') AS PNAME ";
		sql += "FROM MISSINGTRADERLOGTAB M LEFT JOIN EACH_TXN_CODE E ON M.PCODE = E.EACH_TXN_ID ";
		sql += "WHERE TXDATE = :txDate ORDER BY TXDATE, TXTIME";
		System.out.println(sql);
		try{
			SQLQuery query = getCurrentSession().createSQLQuery(sql);
			query.setParameter("txDate", txDate);
			
			String cols = "PK_TXDATE,TXTIME,PCODE,PNAME,BASICDATA,SENDERBANK,RECEIVERBANK";
			String[] objs =  cols.split(",");
			AutoAddScalar addscalar = new AutoAddScalar();
			addscalar.addScalar(query, MISSINGTRADERLOGTAB.class, objs);
			query.setResultTransformer(Transformers.aliasToBean(MISSINGTRADERLOGTAB.class));
			list = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return list;
	}
	
	public List<MISSINGTRADERLOGTAB> getByStanAndTxdate(String condition, String txDate){
		List<MISSINGTRADERLOGTAB> list = null;
		String sql = "SELECT "; 
		sql += "TXDATE AS PK_TXDATE, TXTIME, PCODE, BASICDATA, ";
		sql += "COALESCE((CASE WHEN SENDERBANK LIKE '999%' THEN '交換所' ELSE (SELECT BGBK_NAME FROM BANK_GROUP WHERE BGBK_ID = SENDERBANK) END),'') AS SENDERBANK, "; 
		sql += "COALESCE((CASE WHEN RECEIVERBANK LIKE '999%' THEN '交換所' ELSE (SELECT BGBK_NAME FROM BANK_GROUP WHERE BGBK_ID = RECEIVERBANK) END),'') AS RECEIVERBANK, ";
		/*
		sql += "    (CASE SUBSTR(STAN, 1, 3) WHEN '999' THEN '交換所' ELSE ( ";
		sql += "        SELECT B.BGBK_NAME ";
		sql += "        FROM ( ";
		sql += "            SELECT DISTINCT OPBK_ID ";
		sql += "            FROM BANK_GROUP ";
		sql += "        ) A JOIN BANK_GROUP B ON A.OPBK_ID = B.BGBK_ID ";
		sql += "        WHERE SUBSTR(A.OPBK_ID, 1, 3) = SUBSTR(STAN, 1, 3) ";
		sql += "        FETCH FIRST 1 ROW ONLY ";
		sql += "    ) END) AS PK_STAN, ";
		*/
		sql += "COALESCE(E.EACH_TXN_NAME, '') AS PNAME ";
		sql += "FROM MISSINGTRADERLOGTAB M LEFT JOIN EACH_TXN_CODE E ON M.PCODE = E.EACH_TXN_ID ";
		sql += "WHERE " + condition + " AND TXDATE = :txDate ORDER BY TXDATE, TXTIME";
		System.out.println(sql);
		try{
			SQLQuery query = getCurrentSession().createSQLQuery(sql);
			query.setParameter("txDate", txDate);
			
			String cols = "PK_TXDATE,TXTIME,PCODE,PNAME,BASICDATA,SENDERBANK,RECEIVERBANK";
			String[] objs =  cols.split(",");
			AutoAddScalar addscalar = new AutoAddScalar();
			addscalar.addScalar(query, MISSINGTRADERLOGTAB.class, objs);
			query.setResultTransformer(Transformers.aliasToBean(MISSINGTRADERLOGTAB.class));
			list = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return list;
	}
	
	public List<MISSINGTRADERLOGTAB> getAllData(){
		List<MISSINGTRADERLOGTAB> list = null;
		String sql = "SELECT "; 
		sql += "TXDATE AS PK_TXDATE, TXTIME, PCODE, BASICDATA, ";
		sql += "COALESCE((CASE WHEN SENDERBANK LIKE '999%' THEN '交換所' ELSE (SELECT BGBK_NAME FROM BANK_GROUP WHERE BGBK_ID = SENDERBANK) END),'') AS SENDERBANK, "; 
		sql += "COALESCE((CASE WHEN RECEIVERBANK LIKE '999%' THEN '交換所' ELSE (SELECT BGBK_NAME FROM BANK_GROUP WHERE BGBK_ID = RECEIVERBANK) END),'') AS RECEIVERBANK, ";
		/*
		sql += "    (CASE SUBSTR(STAN, 1, 3) WHEN '999' THEN '交換所' ELSE ( ";
		sql += "        SELECT B.BGBK_NAME ";
		sql += "        FROM ( ";
		sql += "            SELECT DISTINCT OPBK_ID ";
		sql += "            FROM BANK_GROUP ";
		sql += "        ) A JOIN BANK_GROUP B ON A.OPBK_ID = B.BGBK_ID ";
		sql += "        WHERE SUBSTR(A.OPBK_ID, 1, 3) = SUBSTR(STAN, 1, 3) ";
		sql += "        FETCH FIRST 1 ROW ONLY ";
		sql += "    ) END) AS PK_STAN, ";
		*/
		sql += "COALESCE(E.EACH_TXN_NAME, '') AS PNAME ";
		sql += "FROM MISSINGTRADERLOGTAB M LEFT JOIN EACH_TXN_CODE E ON M.PCODE = E.EACH_TXN_ID ";
		sql += "ORDER BY TXDATE, TXTIME";
		System.out.println(sql);
		try{
			SQLQuery query = getCurrentSession().createSQLQuery(sql);
			
			String cols = "PK_TXDATE,TXTIME,PCODE,PNAME,BASICDATA,SENDERBANK,RECEIVERBANK";
			String[] objs =  cols.split(",");
			AutoAddScalar addscalar = new AutoAddScalar();
			addscalar.addScalar(query, MISSINGTRADERLOGTAB.class, objs);
			query.setResultTransformer(Transformers.aliasToBean(MISSINGTRADERLOGTAB.class));
			list = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return list;
	}
}
