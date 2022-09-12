package tw.org.twntch.db.dao.hibernate;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;

import tw.org.twntch.po.BULLETIN;
import tw.org.twntch.po.BULLETIN_REC;

public class BULLETIN_Dao extends HibernateEntityDao<BULLETIN, Serializable>{
	
	
	
	public boolean saveData( BULLETIN bulletin , BULLETIN_REC bulletin_rec ){
		boolean result = false;
		StringBuffer sql = new StringBuffer();
//		sql.append(" UPDATE BULLETIN SET SEND_STATUS = 'N' WHERE SNO != :SNO");
		sql.append(" UPDATE tw.org.twntch.po.BULLETIN SET SEND_STATUS = 'N' ");
		Session session = getSessionFactory().openSession();
		session.beginTransaction();
		try {
			Query query = session.createQuery(sql.toString());
//		query.setParameter("BGBK_ID", bulletin.getSNO());
			int branchresult =  query.executeUpdate();
			logger.debug("更新SEND_STATUS='N'的筆數>>"+branchresult);
			session.save(bulletin);
			session.save(bulletin_rec);
			session.beginTransaction().commit();
			result = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			session.beginTransaction().rollback();
			logger.debug("saveData.Exception>>"+e);
		}
		return result;
	}
	public boolean updateData( BULLETIN bulletin , BULLETIN_REC bulletin_rec ){
		boolean result = false;
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE tw.org.twntch.po.BULLETIN SET SEND_STATUS = 'N' WHERE SNO != :SNO");
		Session session = getSessionFactory().openSession();
		session.beginTransaction();
		try {
			Query query = session.createQuery(sql.toString());
			query.setParameter("SNO", bulletin.getSNO());
			int branchresult =  query.executeUpdate();
			logger.debug("更新SEND_STATUS='N'的筆數>>"+branchresult);
			session.update(bulletin);
			session.save(bulletin_rec);
			session.beginTransaction().commit();
			result = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			session.beginTransaction().rollback();
			logger.debug("saveData.Exception>>"+e);
		}
		return result;
	}
	
	
	public List<BULLETIN> getDataById(Integer id ){
		List<BULLETIN> list = null ;
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT SNO,SEND_STATUS , CHCON ");
		sql.append(" ,( TO_TWDATEII( TO_CHAR(SAVE_DATE , 'YYYYMMDD') ) ||'　' ||TO_CHAR(SAVE_DATE , 'HH24:MI:SS') ) AS SAVE_DATE ");
		sql.append(" ,( TO_TWDATEII( TO_CHAR(SEND_DATE , 'YYYYMMDD') ) ||'　' ||TO_CHAR(SEND_DATE , 'HH24:MI:SS') ) AS SEND_DATE ");
//		sql.append(" ,( TRANS_DATE( TO_CHAR(SEND_DATE , 'YYYYMMDD') , 'T' ,'/' ) ||'　' ||TO_CHAR(SEND_DATE , 'HH24:MI:SS') ) AS SEND_DATE ");
		sql.append(" FROM EACHUSER.BULLETIN ");
		sql.append(" WHERE SNO = :id  ");
		try {
			SQLQuery sqlquery  = getCurrentSession().createSQLQuery(sql.toString());
			sqlquery.setParameter("id", id);
			sqlquery.addScalar("SNO" , Hibernate.INTEGER).addScalar("SEND_STATUS" , Hibernate.STRING).addScalar("CHCON" , Hibernate.STRING);
			sqlquery.addScalar("SAVE_DATE" , Hibernate.STRING).addScalar("SEND_DATE" , Hibernate.STRING);
			sqlquery.setResultTransformer( Transformers.aliasToBean(BULLETIN.class) );
			list = sqlquery.list();
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	public List<BULLETIN> getAll(){
		List<BULLETIN> list = null ;
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT SNO,SEND_STATUS , CHCON ");
		sql.append(" ,( TO_TWDATEII( TO_CHAR(SAVE_DATE , 'YYYYMMDD') ) ||'　' ||TO_CHAR(SAVE_DATE , 'HH24:MI:SS') ) AS SAVE_DATE ");
		sql.append(" ,( TO_TWDATEII( TO_CHAR(SEND_DATE , 'YYYYMMDD') ) ||'　' ||TO_CHAR(SEND_DATE , 'HH24:MI:SS') ) AS SEND_DATE ");
//		sql.append(" ,( TRANS_DATE( TO_CHAR(SEND_DATE , 'YYYYMMDD') , 'T' ,'/' ) ||'　' ||TO_CHAR(SEND_DATE , 'HH24:MI:SS') ) AS SEND_DATE ");
		sql.append(" FROM EACHUSER.BULLETIN ");
		try {
			SQLQuery sqlquery  = getCurrentSession().createSQLQuery(sql.toString());
			sqlquery.addScalar("SNO" , Hibernate.INTEGER).addScalar("SEND_STATUS" , Hibernate.STRING).addScalar("CHCON" , Hibernate.STRING);
			sqlquery.addScalar("SAVE_DATE" , Hibernate.STRING).addScalar("SEND_DATE" , Hibernate.STRING);
			sqlquery.setResultTransformer( Transformers.aliasToBean(BULLETIN.class) );
			list = sqlquery.list();
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	public List<BULLETIN> getDataByDate(String s_date ,String e_date ,  String orderSQL ){
		List<BULLETIN> list = null ;
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT SNO,SEND_STATUS , CHCON ");
		sql.append(" ,( TO_TWDATEII( TO_CHAR(SAVE_DATE , 'YYYYMMDD') ) ||'　' ||TO_CHAR(SAVE_DATE , 'HH24:MI:SS') ) AS SAVE_DATE ");
		sql.append(" ,( TO_TWDATEII( TO_CHAR(SEND_DATE , 'YYYYMMDD') ) ||'　' ||TO_CHAR(SEND_DATE , 'HH24:MI:SS') ) AS SEND_DATE ");
//		sql.append(" ,( TRANS_DATE( TO_CHAR(SEND_DATE , 'YYYYMMDD') , 'T' ,'/' ) ||'　' ||TO_CHAR(SEND_DATE , 'HH24:MI:SS') ) AS SEND_DATE ");
		sql.append(" FROM EACHUSER.BULLETIN ");
		sql.append(" WHERE SAVE_DATE BETWEEN :s_date AND :e_date ");
		sql.append(orderSQL);
		try {
			System.out.println("s_date>>"+s_date);
			System.out.println("e_date>>"+e_date);
			SQLQuery sqlquery  = getCurrentSession().createSQLQuery(sql.toString());
			sqlquery.setParameter("s_date", s_date);
			sqlquery.setParameter("e_date", e_date);
			sqlquery.addScalar("SNO" , Hibernate.INTEGER).addScalar("SEND_STATUS" , Hibernate.STRING).addScalar("CHCON" , Hibernate.STRING);
			sqlquery.addScalar("SAVE_DATE" , Hibernate.STRING).addScalar("SEND_DATE" , Hibernate.STRING);
			sqlquery.setResultTransformer( Transformers.aliasToBean(BULLETIN.class) );
			list = sqlquery.list();
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	

}
