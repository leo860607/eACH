package tw.org.twntch.db.dao.hibernate;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;

import tw.org.twntch.po.SYS_PARA;

public class SYS_PARA_Dao extends HibernateEntityDao<SYS_PARA, Serializable>{
	
	
	
	public List<SYS_PARA>  getDataByPK(String id){
		Query query =  getSession().createQuery(" FROM tw.org.twntch.po.SYS_PARA  WHERE SEQ_ID ="+id);
//		query.setMaxResults(1);
		List<SYS_PARA> list = query.list();
		System.out.println("SYS_PARA_Dao.list>>"+list);
		return list;
	}
	public List<SYS_PARA>  getTopOne(){
		Query query =  getSession().createQuery(" FROM tw.org.twntch.po.SYS_PARA ");
		query.setMaxResults(1);
		List<SYS_PARA> list = query.list();
		System.out.println("SYS_PARA_Dao.list>>"+list);
		return list;
	}
	
	public SYS_PARA getAPT_PEND_TIME(){
		SYS_PARA po = null;
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ");
		sql.append("COALESCE(TRIM(APT_PEND_START_TIME1), '') AS APT_PEND_START_TIME1, ");
		sql.append("COALESCE(TRIM(APT_PEND_END_TIME1), '') AS APT_PEND_END_TIME1, ");
		sql.append("COALESCE(TRIM(APT_PEND_START_TIME2), '') AS APT_PEND_START_TIME2, ");
		sql.append("COALESCE(TRIM(APT_PEND_END_TIME2), '') AS APT_PEND_END_TIME2, ");
		sql.append("COALESCE(TRIM(APT_PEND_START_TIME3), '') AS APT_PEND_START_TIME3, ");
		sql.append("COALESCE(TRIM(APT_PEND_END_TIME3), '') AS APT_PEND_END_TIME3, ");
		sql.append("COALESCE(TRIM(APT_PEND_START_TIME4), '') AS APT_PEND_START_TIME4, ");
		sql.append("COALESCE(TRIM(APT_PEND_END_TIME4), '') AS APT_PEND_END_TIME4, ");
		sql.append("COALESCE(TRIM(APT_PEND_START_TIME5), '') AS APT_PEND_START_TIME5, ");
		sql.append("COALESCE(TRIM(APT_PEND_END_TIME5), '') AS APT_PEND_END_TIME5 ");
		sql.append("FROM SYS_PARA ");
		
		try{
			SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
			query.setResultTransformer(Transformers.aliasToBean(SYS_PARA.class));
			List<SYS_PARA> list = query.list();
			if(list != null && list.size() > 0){
				po = list.get(0);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return po;
	}

}
