package tw.org.twntch.db.dao.hibernate;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;

import tw.org.twntch.po.BULLETIN;
import tw.org.twntch.po.BULLETIN_REC;

public class BULLETIN_REC_Dao extends HibernateEntityDao<BULLETIN_REC, Serializable>{
	
	

	public List<BULLETIN_REC> getDataByDate(String s_date ,String e_date ,String orderSQL){
		List<BULLETIN_REC> list = null ;
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT SNO,USERIP,USERID , CHCON ");
		sql.append(" ,( TO_TWDATEII( TO_CHAR(SEND_DATE , 'YYYYMMDD') ) ||'　' ||TO_CHAR(SEND_DATE , 'HH24:MI:SS') ) AS SEND_DATE ");
//		sql.append(" ,( TRANS_DATE( TO_CHAR(SEND_DATE , 'YYYYMMDD') , 'T' ,'/' ) ||'　' ||TO_CHAR(SEND_DATE , 'HH24:MI:SS') ) AS SEND_DATE ");
		sql.append(" FROM EACHUSER.BULLETIN_REC ");
		sql.append(" WHERE SEND_DATE BETWEEN :s_date AND :e_date ");
		sql.append(orderSQL);
		try {
			System.out.println("s_date>>"+s_date);
			System.out.println("e_date>>"+e_date);
			SQLQuery sqlquery  = getCurrentSession().createSQLQuery(sql.toString());
			sqlquery.setParameter("s_date", s_date);
			sqlquery.setParameter("e_date", e_date);
			sqlquery.addScalar("SNO" , Hibernate.INTEGER).addScalar("USERIP" , Hibernate.STRING).addScalar("USERID" , Hibernate.STRING);
			sqlquery.addScalar("SEND_DATE" , Hibernate.STRING).addScalar("CHCON" , Hibernate.STRING);
			sqlquery.setResultTransformer( Transformers.aliasToBean(BULLETIN_REC.class) );
			list = sqlquery.list();
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

}
