package tw.org.twntch.db.dao.hibernate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;

import tw.org.twntch.po.BANK_GROUP;
import tw.org.twntch.po.EACH_FILEUPLOAD;
import tw.org.twntch.util.AutoAddScalar;

public class EACH_FILEUPLOAD_Dao extends HibernateEntityDao<EACH_FILEUPLOAD,Serializable>{
	
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
	//執行新增或修改的動作
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
	//查詢本國銀行，地區銀行和外商銀行的總行清單
	public List<BANK_GROUP> getBanksList(){
		List list = new ArrayList();
//		20160130依據20160122SRS 修改 濾掉票交所
//		Query query = getCurrentSession().createQuery("FROM tw.org.twntch.po.BANK_GROUP BANK_GROUP WHERE BGBK_ATTR IN ('0','1','4','6') ORDER BY BGBK_ID");
		Query query = getCurrentSession().createQuery("FROM tw.org.twntch.po.BANK_GROUP BANK_GROUP WHERE BGBK_ATTR IN ('0','1','4') ORDER BY BGBK_ID");
		list = query.list();
		return list;
	}
	
	public List<BANK_GROUP> getBanksList(String bank_id){
		List list = new ArrayList();
		Query query = getCurrentSession().createQuery("FROM tw.org.twntch.po.BANK_GROUP BANK_GROUP WHERE BGBK_ATTR IN ('0','1','4','6') AND BGBK_ID != ? ORDER BY BGBK_ID");
		query.setParameter(0, bank_id);
		list = query.list();
		return list;
	}
}
