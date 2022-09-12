package tw.org.twntch.db.dao.hibernate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.Id;
import javax.persistence.Transient;

import org.apache.commons.beanutils.DynaBean;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.dao.DataAccessResourceFailureException;

import tw.org.twntch.po.RETURN_DAY;
import tw.org.twntch.util.AutoAddScalar;

public class TXN_RETURNDAY_Dao extends HibernateEntityDao<RETURN_DAY, Serializable>{
	
	public List<RETURN_DAY> getAllData(){
		List<RETURN_DAY> list = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" FROM tw.org.twntch.po.RETURN_DAY ORDER BY TXN_ID, ACTIVE_DATE");
		try{
			Query query = getCurrentSession().createQuery(sql.toString());
			list = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return list;
	}
	public List<RETURN_DAY> getAllData(String orderSQL){
		List<RETURN_DAY> list = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" FROM tw.org.twntch.po.RETURN_DAY "+orderSQL);
		try{
			Query query = getCurrentSession().createQuery(sql.toString());
			list = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return list;
	}
	
	public List getHistory(){
		/*
		SELECT
		TC.TXN_ID, TC.TXN_NAME, RD.RETURN_DAY, RD.ACTIVE_DATE, RD.CDATE,
		(CASE LENGTH(VALUE(RD.ACTIVE_DATE, '')) WHEN 0 THEN '未啟用' ELSE '已啟用' END) AS IS_ACTIVE
		FROM TXN_CODE AS TC LEFT JOIN RETURN_DAY AS RD ON TC.TXN_ID = RD.TXN_ID
		ORDER BY TC.TXN_ID
		*/
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TC.TXN_ID, TC.TXN_NAME, RD.RETURN_DAY, RD.ACTIVE_DATE, RD.CDATE,");
		sql.append("(CASE LENGTH(VALUE(RD.ACTIVE_DATE, '')) WHEN 0 THEN 0 ELSE 1 END) AS IS_ACTIVE ");
		sql.append("FROM TXN_CODE AS TC LEFT JOIN RETURN_DAY AS RD ON TC.TXN_ID = RD.TXN_ID ");
		sql.append("ORDER BY TC.TXN_ID");
		
		List list = null;
		try{
			SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
			String cols = "TXN_ID, TXN_NAME, RETURN_DAY, ACTIVE_DATE, CDATE";
			String[] objs =  cols.split(",");
			AutoAddScalar addscalar = new AutoAddScalar();
			addscalar.addScalar(query, RETURN_DAY.class, objs);
			query.setResultTransformer(Transformers.aliasToBean(RETURN_DAY.class));
			query.addScalar("IS_ACTIVE", Hibernate.STRING);
			list = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return list;
	}
	
	public List<RETURN_DAY> getByTxnId(String txnId){
		List<RETURN_DAY> list = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" FROM tw.org.twntch.po.RETURN_DAY WHERE TXN_ID = :txnId ORDER BY ACTIVE_DATE");
		try{
			Query query = getCurrentSession().createQuery(sql.toString());
			query.setParameter("txnId", txnId);
			list = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return list;
	}
	
	public List<RETURN_DAY> getByTxnId(String txnId , String orderSQL){
		List<RETURN_DAY> list = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" FROM tw.org.twntch.po.RETURN_DAY WHERE TXN_ID = :txnId "+orderSQL);
		try{
			Query query = getCurrentSession().createQuery(sql.toString());
			query.setParameter("txnId", txnId);
			list = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return list;
	}
	
	public List<Map<String, String>> getTxnIdList(){
		List<Map<String, String>> list = null;
		String sql = "SELECT DISTINCT RD.TXN_ID, TC.TXN_NAME FROM RETURN_DAY RD JOIN TXN_CODE TC ON RD.TXN_ID = TC.TXN_ID";
		try{
			SQLQuery query = getCurrentSession().createSQLQuery(sql);
			query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			list = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return list;
	}
}
