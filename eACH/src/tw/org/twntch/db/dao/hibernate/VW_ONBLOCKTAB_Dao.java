package tw.org.twntch.db.dao.hibernate;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;

import tw.org.twntch.po.VW_ONBLOCKTAB;
import tw.org.twntch.util.AutoAddScalar;

public class VW_ONBLOCKTAB_Dao extends HibernateEntityDao<VW_ONBLOCKTAB, Serializable> {
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
	
	public int countData(String countQuerySql){
		int count = 0;
		SQLQuery query =  getCurrentSession().createSQLQuery(countQuerySql);
		query.addScalar("NUM");
		List countList = query.list();
		if(countList != null && countList.size() > 0){
			count = (Integer)countList.get(0);
		}
		return count; 
		/*
		SQLQuery query =  getCurrentSession().createSQLQuery(countQuerySql);
		List countList = query.list();
		return countList.size();
		*/
	}

	public Page getDataII(int pageNo, int pageSize, String countQuerySql, String sql, String[] cols,List<String> values){
		int totalCount = countDataII(countQuerySql , values);
		SQLQuery query =  getCurrentSession().createSQLQuery(sql);
		query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		int i = 0;
		for(String val :values ){
			query.setParameter(i, val);
			i++;
		}
		//實際查詢返回分頁對像
		int startIndex = Page.getStartOfPage(pageNo, pageSize);
		List list = query.setFirstResult(startIndex).setMaxResults(pageSize).list();
		return new Page(startIndex, totalCount, pageSize, list);
	}
	
	public int countDataII(String countQuerySql ,List<String> values){
		SQLQuery query =  getCurrentSession().createSQLQuery(countQuerySql);
		int i =0;
		for( String val :values){
			query.setParameter(i, val);
			i++;
		}
		List<Integer> countList = query.list();
		return countList.size();
	}
	
	public Page getDataIII(int pageNo, int pageSize, String countQuerySql, String sql, String[] cols, Class targetClass){
		int totalCount = countDataIII(countQuerySql);
		int startIndex = Page.getStartOfPage(pageNo, pageSize) + 1;
		
		SQLQuery query =  getCurrentSession().createSQLQuery(sql);
		AutoAddScalar addscalar = new AutoAddScalar();
		addscalar.addScalar(query, targetClass, cols);
		query.setResultTransformer(Transformers.aliasToBean(targetClass));
		//實際查詢返回分頁對像
		List list = query.list();

		return new Page(startIndex - 1, totalCount, pageSize, list);
	}
	
	public int countDataIII(String countQuerySql){
		int count = 0;
		SQLQuery query =  getCurrentSession().createSQLQuery(countQuerySql);
		query.addScalar("NUM");
		List countList = query.list();
		if(countList != null && countList.size() > 0){
			count = (Integer)countList.get(0);
		}
		return count; 
		/*
		SQLQuery query =  getCurrentSession().createSQLQuery(countQuerySql);
		List countList = query.list();
		return countList.size();
		*/
	}
	
	//檢視明細
	public Map getDetail(String condition, String txdate, String stan){
		List<Map> list = null;
		SQLQuery query = getCurrentSession().createSQLQuery(condition);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
//		query.addScalar("NEWSENDERFEE",Hibernate.BIG_DECIMAL);
//		query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		list = query.list();
		if(list != null && list.size() > 0){
			return list.get(0);
		}
		return null;
	}	
	
	//檢視明細
	public Map getDetail(String sql, Map<String,String> params ){
		List<Map> list = null;
		SQLQuery query = getCurrentSession().createSQLQuery(sql);
		for(String key :params.keySet()){
			query.setParameter(key, params.get(key));
		}
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		list = query.list();
		if(list != null && list.size() > 0){
			return list.get(0);
		}
		return null;
	}	
	
	//檢視明細
	public List<Map> getData(String sql, Map<String,String> params ){
		List<Map> list = null;
		SQLQuery query = getCurrentSession().createSQLQuery(sql);
		for(String key :params.keySet()){
			query.setParameter(key, params.get(key));
		}
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		list = query.list();
		return list;
	}
	
	public List<Map> getDataWithIn(String sql, Map<String,String> params ,String listName , List<String> listParam){
		List<Map> list = null;
		SQLQuery query = getCurrentSession().createSQLQuery(sql);
		for(String key :params.keySet()){
			query.setParameter(key, params.get(key));
		}
		query.setParameterList(listName,listParam);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		list = query.list();
		return list;
	}
	//計算加總用
	public List dataSum(String dataSumSQL,String[] dataSumCols,Class targetClass){
		SQLQuery query =  getCurrentSession().createSQLQuery(dataSumSQL);
		AutoAddScalar addscalar = new AutoAddScalar();
		addscalar.addScalar(query,targetClass,dataSumCols);
		query.setResultTransformer(Transformers.aliasToBean(targetClass));
		return query.list();
	}
	
	public List getCSVData(String sql,String[] Cols){
		SQLQuery query =  getCurrentSession().createSQLQuery(sql);
		AutoAddScalar addscalar = new AutoAddScalar();
		addscalar.addScalar(query,VW_ONBLOCKTAB.class,Cols);
		query.setResultTransformer(Transformers.aliasToBean(VW_ONBLOCKTAB.class));
		return  query.list();
	}
}
