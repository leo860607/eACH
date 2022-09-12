package tw.org.twntch.db.dao.hibernate;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;

import tw.org.twntch.po.BANK_GROUP_BUSINESS;
import tw.org.twntch.po.BUSINESS_TYPE;

public class BUSINESS_TYPE_Dao extends HibernateEntityDao<BUSINESS_TYPE, Serializable> {

	public List<BUSINESS_TYPE> getDataByBsName(String name){
		StringBuffer sql = new StringBuffer();
		sql.append(" FROM tw.org.twntch.po.BUSINESS_TYPE WHERE BUSINESS_TYPE_NAME =:name ");
		Query query = getSession().createQuery(sql.toString());
		query.setParameter("name", name);
		List<BUSINESS_TYPE> list = query.list();
		list = list.size()==0?null :list;
		return list;
	}
	
	public boolean saveData(BUSINESS_TYPE po, List<BANK_GROUP_BUSINESS> bgbList){
		boolean result = false;
		Session session = getSessionFactory().openSession();
		session.beginTransaction();
		
		try {
			session.saveOrUpdate(po);
			
			for(int i = 0; i < bgbList.size(); i++){
				session.saveOrUpdate(bgbList.get(i));
			}
			
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
	
	public boolean updateData(BUSINESS_TYPE po, List<BANK_GROUP_BUSINESS> insList, List<BANK_GROUP_BUSINESS> delList){
		boolean result = false;
		Session session = getSessionFactory().openSession();
		session.beginTransaction();
		
		try {
			session.saveOrUpdate(po);
			
			for(int i = 0; i < delList.size(); i++){
				session.delete(delList.get(i));
			}
			
			for(int i = 0; i < insList.size(); i++){
				session.merge(insList.get(i));
			}
			
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
