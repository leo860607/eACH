package tw.org.twntch.db.dao.hibernate;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;

import tw.org.twntch.po.BANK_CTBK;
import tw.org.twntch.po.BANK_GROUP_BUSINESS;
import tw.org.twntch.po.BANK_OPBK;
import tw.org.twntch.po.ONCLEARINGTAB;

public class ONCLEARINGTAB_Dao extends HibernateEntityDao<ONCLEARINGTAB, Serializable> {

	
	public List<Map<String,String>> getData(String sql , Map<String,String> args){
		
		SQLQuery sqlquery =  getCurrentSession().createSQLQuery(sql);
		if(args!=null){
			for(String key :args.keySet()){
				sqlquery.setParameter(key, args.get(key));
			}
		}
		sqlquery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List<Map<String,String>> lsit = sqlquery.list();
		for(Map m :lsit){
			System.out.println("BIZDATE>>"+m.get("BIZDATE"));
		}
		return lsit;
		
	}
	
	
	public int exeUpdate(String sqlPath ,Map<String ,String> values ,List<ONCLEARINGTAB> addList){
		boolean result = false;
		StringBuffer sql = new StringBuffer();
		int delteCount = 0;
		Session session = getSessionFactory().openSession();
		session.beginTransaction();
		try {
//			sql.append("DELETE FROM ONCLEARINGTAB WHERE BIZDATE = :BIZDATE AND CLEARINGPHASE = :CLEARINGPHASE ");
			sql.append("DELETE FROM tw.org.twntch.po.ONCLEARINGTAB ");
			sql.append(sqlPath);
			Query query =   session.createQuery(sql.toString());
//			query.setParameter("bizdate" , bizdate);
//			query.setParameter("clearingphase", clearingphase);
			
			for(String key :values.keySet()){
				logger.debug("key>>"+key);
				logger.debug("val>>"+values.get(key));
				query.setParameter(key, values.get(key));
			}
			logger.debug("exeUpdate.delete SQL>>"+sql);
			delteCount = query.executeUpdate();
			
			for( ONCLEARINGTAB po :addList){
				//會時會出現無法INSERT因為有資料超過欄位長度限制 故放一個LOG 解決後移除 
				System.out.println("saveOrUpdate PO >>" + po.toString());
				session.saveOrUpdate(po);
			}
			session.beginTransaction().commit();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			session.beginTransaction().rollback();
			result = false;
			logger.debug("exeUpdate.Exception>>"+e);
			throw e;
		}
		return delteCount;
	}
	
}
