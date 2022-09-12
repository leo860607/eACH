package tw.org.twntch.db.dao.hibernate;

import java.io.Serializable;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;

import tw.org.twntch.po.EACHAPSTATUSTAB;

public class EACHAPSTATUSTAB_Dao extends HibernateEntityDao<EACHAPSTATUSTAB, Serializable> {
	public List<EACHAPSTATUSTAB> getEachApStatus(){
		List<EACHAPSTATUSTAB> list = null;
		String sql = "FROM tw.org.twntch.po.EACHAPSTATUSTAB";
		try{
			Query query = getCurrentSession().createQuery(sql);
			list = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return list;
	}
}
