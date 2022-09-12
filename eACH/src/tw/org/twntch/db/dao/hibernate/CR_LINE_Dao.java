package tw.org.twntch.db.dao.hibernate;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;

import tw.org.twntch.po.CR_LINE;
import tw.org.twntch.po.CR_LINE_LOG;
import tw.org.twntch.util.AutoAddScalar;

public class CR_LINE_Dao extends HibernateEntityDao<CR_LINE, Serializable> {

	
	
	public List<CR_LINE> findDataByIdList(List<String> bankList){
		String sql = " FROM tw.org.twntch.po.CR_LINE WHERE BANK_ID IN(:bankList) ORDER BY BANK_ID";
		Query query = getCurrentSession().createQuery(sql);
		query.setParameterList("bankList", bankList);
		List<CR_LINE> list = query.list();
		return list;
	}
	
	
	public List<Map<String, String>> getExistingBgbkIdList(){
		List<Map<String, String>> list = null;
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT DISTINCT BANK_ID AS BGBK_ID, BGBK_NAME FROM CR_LINE LEFT JOIN BANK_GROUP ON BANK_ID = BGBK_ID ORDER BY BANK_ID ");
		try{
			SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
			query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			query.addScalar("BGBK_ID");
			query.addScalar("BGBK_NAME");
			list = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return list;
	}
	
	public List<CR_LINE> getDataById(String id ){
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT BG.BGBK_NAME AS BANK_NAME , CR.* FROM CR_LINE CR  ");
		sql.append(" LEFT JOIN BANK_GROUP BG ON CR.BANK_ID = BG.BGBK_ID  ");
		sql.append(" WHERE CR.BANK_ID=:id ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setParameter("id", id);
			
		//String cols = " BANK_NAME,BANK_ID,USER_ID,BASIC_CR_LINE,REST_CR_LINE,CDATE,UDATE " ;
		String cols = " BANK_NAME,BANK_ID,BASIC_CR_LINE,REST_CR_LINE,CDATE,UDATE " ;
		String[] objs =  cols.split(",");
		AutoAddScalar addscalar = new AutoAddScalar();
		addscalar.addScalar(query, CR_LINE.class, objs);
		query.setResultTransformer(Transformers.aliasToBean(CR_LINE.class));
		List<CR_LINE> list = query.list();
		return list;
	}
	
	public List<CR_LINE> getAllData(String orderSQL){
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT BG.BGBK_NAME AS BANK_NAME , CR.* FROM CR_LINE CR  ");
		sql.append(" LEFT JOIN BANK_GROUP BG ON CR.BANK_ID = BG.BGBK_ID  ");
		sql.append(orderSQL);
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		String cols = " BANK_NAME,BANK_ID,BASIC_CR_LINE,REST_CR_LINE,CDATE,UDATE " ;
		String[] objs =  cols.split(",");
		AutoAddScalar addscalar = new AutoAddScalar();
		addscalar.addScalar(query, CR_LINE.class, objs);
		query.setResultTransformer(Transformers.aliasToBean(CR_LINE.class));
		List<CR_LINE> list = query.list();
		return list;
	}
	public List<Map> getAllDataRetMap(String orderSQL){
		StringBuffer sql = new StringBuffer();
		sql.append(" WITH TEMP AS (  ");
		sql.append(" SELECT *  FROM CR_LINE CR ");
		sql.append(" ) ");
		sql.append(" ,TEMP2 AS ( ");
		sql.append(" SELECT BANKID ,(COALESCE (SUM(RECVAMT+RVSRECVAMT) , 0) - COALESCE( SUM(PAYAMT+RVSPAYAMT) , 0)) CLDIFAMT ");
		sql.append(" FROM ONCLEARINGTAB ");
		sql.append(" WHERE BIZDATE = (SELECT (CASE  DATEMODE WHEN '0' THEN THISDATE ELSE NEXTDATE END ) AS BIZDATE FROM EACHSYSSTATUSTAB ) ");
		sql.append(" AND CLEARINGPHASE = (SELECT CLEARINGPHASE FROM EACHSYSSTATUSTAB ) ");
		sql.append(" GROUP BY BANKID ");
		sql.append(" ) ");
		sql.append(" SELECT  (CASE WHEN  (T.BASIC_CR_LINE - COALESCE(T2.CLDIFAMT,0)) <> T.REST_CR_LINE THEN 'Y' ELSE 'N' END) IS_DIF  ");
//		sql.append(" ,(T.BASIC_CR_LINE - COALESCE(T2.CLDIFAMT,0)) AS CNT_OVER_CR , COALESCE ((SELECT BGBK_NAME FROM BANK_GROUP BG WHERE BG.BGBK_ID = T.BANK_ID ) ,'') BANK_NAME  ,T.*  ");
		//20160429 UAT-20160427-01
		sql.append(" ,(T.BASIC_CR_LINE + COALESCE(T2.CLDIFAMT,0)) AS CNT_OVER_CR , COALESCE ((SELECT BGBK_NAME FROM BANK_GROUP BG WHERE BG.BGBK_ID = T.BANK_ID ) ,'') BANK_NAME  ,T.*  ");
		sql.append(" FROM TEMP T ");
		sql.append(" LEFT JOIN TEMP2 T2 ON T.BANK_ID = T2.BANKID ");
//		sql.append(orderSQL);
		sql.append(" ORDER BY IS_DIF DESC , T.BANK_ID ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List<Map> list = query.list();
		return list;
	}
	
	
	public List<Map> getDataByIdRetMap(String id ){
		StringBuffer sql = new StringBuffer();
		sql.append(" WITH TEMP AS (  ");
		sql.append(" SELECT *  FROM CR_LINE CR ");
		sql.append(" ) ");
		sql.append(" ,TEMP2 AS ( ");
		sql.append(" SELECT BANKID ,(COALESCE (SUM(RECVAMT+RVSRECVAMT) , 0) - COALESCE( SUM(PAYAMT+RVSPAYAMT) , 0)) CLDIFAMT ");
		sql.append(" FROM ONCLEARINGTAB ");
		sql.append(" WHERE BIZDATE = (SELECT (CASE  DATEMODE WHEN '0' THEN THISDATE ELSE NEXTDATE END ) AS BIZDATE FROM EACHSYSSTATUSTAB ) ");
		sql.append(" AND CLEARINGPHASE = (SELECT CLEARINGPHASE FROM EACHSYSSTATUSTAB ) ");
		sql.append(" GROUP BY BANKID ");
		sql.append(" ) ");
		sql.append(" SELECT  (CASE WHEN  (T.BASIC_CR_LINE - COALESCE(T2.CLDIFAMT,0)) <> T.REST_CR_LINE THEN 'Y' ELSE 'N' END) IS_DIF   ");
//		sql.append(" , (T.BASIC_CR_LINE - COALESCE(T2.CLDIFAMT,0)) AS CNT_OVER_CR , COALESCE ((SELECT BGBK_NAME FROM BANK_GROUP BG WHERE BG.BGBK_ID = T.BANK_ID ) ,'') BANK_NAME  ,T.*  ");
		//20160429 UAT-20160427-01
		sql.append(" ,(T.BASIC_CR_LINE + COALESCE(T2.CLDIFAMT,0)) AS CNT_OVER_CR , COALESCE ((SELECT BGBK_NAME FROM BANK_GROUP BG WHERE BG.BGBK_ID = T.BANK_ID ) ,'') BANK_NAME  ,T.*  ");
		sql.append(" FROM TEMP T ");
		sql.append(" LEFT JOIN TEMP2 T2 ON T.BANK_ID = T2.BANKID ");
		sql.append(" WHERE T.BANK_ID=:id ");
		sql.append(" ORDER BY IS_DIF DESC , T.BANK_ID ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setParameter("id", id);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List<Map> list = query.list();
		return list;
	}
	
	//儲存CR_LINE 及CR_LINE_LOG
	public boolean saveData(CR_LINE cr_line ,CR_LINE_LOG log) {
		boolean result = false;
		Session session = getSessionFactory().openSession();
		session.beginTransaction();
		
		try {
			session.saveOrUpdate(cr_line);
			session.saveOrUpdate(log);
			session.beginTransaction().commit();
			result = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			session.beginTransaction().rollback();
			result = false;
		}
		return result;
	}
}
