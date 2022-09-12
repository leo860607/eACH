package tw.org.twntch.db.dao.hibernate;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;

import tw.org.twntch.po.OPCTRANSACTIONLOGTAB;
import tw.org.twntch.util.AutoAddScalar;

public class OPC_TRANS_Dao extends HibernateEntityDao<OPCTRANSACTIONLOGTAB, Serializable> {
	
	public List<Map<String,Integer>> DataSumList(String countQuerySql , Map<String,String> values){
		SQLQuery query =  getCurrentSession().createSQLQuery(countQuerySql);
		for(String key :values.keySet()){
			if(key.equals("ROWNUMBER") || key.equals("LAST_ROWNUMBER")  ){
				continue;
			}
			query.setParameter(key, values.get(key));
		}
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List<Map<String,Integer>> countList = query.list();
		return countList;
	}
	
	
	public Page getDataRetPage( int startIndex ,int pageSize, int totalCount,  String sql ,  Map<String,String> values){
		List<Map> list = null;
		List<Map<String,Integer>> cntlist = null;
		try{
			SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
			for(String key :values.keySet()){
				query.setParameter(key, values.get(key));
			}
			query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			list = query.list();
		}catch(Exception e){
			e.printStackTrace();
		}
		return new Page(startIndex - 1, totalCount, pageSize, list);
	}
	
	
	public Page getData(int pageNo, int pageSize, String countQuerySql, String sql, String[] cols, Class targetClass){
		int totalCount = countData(countQuerySql);
		int startIndex = Page.getStartOfPage(pageNo, pageSize) + 1;
		int lastIndex = pageNo * pageSize;
		sql += " ) AS TEMP_ WHERE ROWNUMBER >= "+startIndex +" AND ROWNUMBER <= "+lastIndex;
		
		SQLQuery query =  getCurrentSession().createSQLQuery(sql);
		AutoAddScalar addscalar = new AutoAddScalar();
		addscalar.addScalar(query, targetClass, cols);
		query.setResultTransformer(Transformers.aliasToBean(targetClass));
		//實際查詢返回分頁對像
		List list = query.list();

		return new Page(startIndex - 1, totalCount, pageSize, list);
	}
	public Page getSettleData(int pageNo, int pageSize, String countQuerySql, String sql, String[] cols, Class targetClass){
		int totalCount = countData(countQuerySql);
//		int startIndex = Page.getStartOfPage(pageNo, pageSize) + 1;
		int startIndex = Page.getStartOfPage(pageNo, pageSize) ;
		SQLQuery query =  getCurrentSession().createSQLQuery(sql);
		AutoAddScalar addscalar = new AutoAddScalar();
		addscalar.addScalar(query, targetClass, cols);
		query.setResultTransformer(Transformers.aliasToBean(targetClass));
		//實際查詢返回分頁對像
		List list = query.setFirstResult(startIndex).setMaxResults(pageSize).list();
//		List list = query.list();
		
		return new Page(startIndex, totalCount, pageSize, list);
	}
	
	public int countData(String countQuerySql){
		int result = 0;
		SQLQuery query =  getCurrentSession().createSQLQuery(countQuerySql);
		query.addScalar("NUM");
		List countList = query.list();
		if(countList != null && countList.size() > 0){
			result = (Integer)countList.get(0);
		}
		return result;
	}
	
	/*
	public Page getData(int pageNo, int pageSize, String countQuerySql, String sql, String[] cols, Class targetClass){
		SQLQuery query =  getCurrentSession().createSQLQuery(sql);
		AutoAddScalar addscalar = new AutoAddScalar();
		addscalar.addScalar(query, targetClass, cols);
		query.setResultTransformer(Transformers.aliasToBean(targetClass));
		int totalCount = query.list().size();
		//實際查詢返回分頁對像
		int startIndex = Page.getStartOfPage(pageNo, pageSize);
		List list = query.setFirstResult(startIndex).setMaxResults(pageSize).list();

		return new Page(startIndex, totalCount, pageSize, list);
	}
	*/
	
	public List<OPCTRANSACTIONLOGTAB> getData(String sql, String cols){
		List<OPCTRANSACTIONLOGTAB> list = null;
		
		try{
			System.out.println("SQL >> " + sql);
			
			SQLQuery query = getCurrentSession().createSQLQuery(sql);
			
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
	
	public List search(String sql, String[] cols, Class targetClass) {
		List list = null;
		try {
			SQLQuery query = getCurrentSession().createSQLQuery(sql);
			AutoAddScalar addscalar = new AutoAddScalar();
			addscalar.addScalar(query, targetClass, cols);
			query.setResultTransformer(Transformers.aliasToBean(targetClass));
			list = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public List getData(String sql){
		List list = null;
		
		try{
			System.out.println("SQL >> " + sql);
			
			SQLQuery query = getCurrentSession().createSQLQuery(sql);
			
			query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			list = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return list;
	}
}
