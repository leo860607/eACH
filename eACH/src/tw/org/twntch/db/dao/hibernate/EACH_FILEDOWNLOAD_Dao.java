package tw.org.twntch.db.dao.hibernate;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;

import tw.org.twntch.po.EACH_FILEDOWNLOAD;
import tw.org.twntch.util.AutoAddScalar;

public class EACH_FILEDOWNLOAD_Dao extends HibernateEntityDao<EACH_FILEDOWNLOAD,Serializable>{
	
	public Page getData(int pageNo, int pageSize, String countQuerySql, String sql, String[] cols, Class targetClass){
		int totalCount = countData(countQuerySql);
		SQLQuery query =  getCurrentSession().createSQLQuery(sql);
		AutoAddScalar addscalar = new AutoAddScalar();
		addscalar.addScalar(query, targetClass, cols);
		query.setResultTransformer(Transformers.aliasToBean(targetClass));
		//實際查詢返回分頁對像
		int startIndex = Page.getStartOfPage(pageNo, pageSize);
		List list = query.setFirstResult(startIndex).setMaxResults(pageSize).list();

		return new Page(startIndex, totalCount, pageSize, list);
	}
	//算筆數
	public int countData(String countQuerySql){
		SQLQuery query =  getCurrentSession().createSQLQuery(countQuerySql);
		List<Integer> countList = query.list();
		return countList.get(0).intValue();
	}
	//執行更新的動作
    public void saveOrUpdate(String sql, Object...paramObjectArray){
		Session session = getCurrentSession();
		Query query=session.createSQLQuery(sql);
		//sql參數處理
		if(paramObjectArray!=null){
			for (int i = 0; i < paramObjectArray.length; i++) {
				query.setParameter(i, paramObjectArray[i]);
			}
		}
		query.executeUpdate();
    }
    
    public List<EACH_FILEDOWNLOAD> query(String ID_NO,int FILE_NO,String sql,String[] cols,Class targetClass) {
		SQLQuery query =  getCurrentSession().createSQLQuery(sql);
		AutoAddScalar addscalar = new AutoAddScalar();
		addscalar.addScalar(query, targetClass, cols);
		//塞參數
		query.setParameter(0, ID_NO);
		query.setParameter(1, FILE_NO);
		query.setResultTransformer(Transformers.aliasToBean(targetClass));
		return query.list();
	}
}
