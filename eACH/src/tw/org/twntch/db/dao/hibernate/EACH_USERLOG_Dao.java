package tw.org.twntch.db.dao.hibernate;

import java.util.LinkedList;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;

import tw.org.twntch.po.EACH_USERLOG;
import tw.org.twntch.po.EACH_USERLOG_PK;

public class EACH_USERLOG_Dao extends HibernateEntityDao<EACH_USERLOG, EACH_USERLOG_PK>{

	
	
	public List<EACH_USERLOG> getDetail(String serno , String user_id , String com_id){
		StringBuffer sql = new StringBuffer();
//		sql.append(" SELECT  E.SERNO ,E.USERID , E.USER_COMPANY ,E.USERIP,E.TXTIME");
		sql.append(" SELECT  E.SERNO ,E.USERID ,E.USERIP,E.TXTIME");
		sql.append(" ,E.UP_FUNC_ID ||'-'|| (SELECT FUNC_NAME FROM EACH_FUNC_LIST WHERE FUNC_ID = E.UP_FUNC_ID) AS UP_FUNC_ID "); 
		sql.append(" ,E.FUNC_ID ||'-'||  (SELECT FUNC_NAME FROM EACH_FUNC_LIST WHERE FUNC_ID = E.FUNC_ID) AS FUNC_ID "); 
//		sql.append(" ,coalesce(E.USER_COMPANY ||'-'||  (SELECT BRBK_NAME FROM BANK_BRANCH WHERE BRBK_ID = E.USER_COMPANY),'') AS USER_COMPANY "); 
		sql.append(" ,coalesce(E.USER_COMPANY ||'-'||  (SELECT BGBK_NAME FROM BANK_GROUP WHERE BGBK_ID = E.USER_COMPANY),'') AS USER_COMPANY "); 
		sql.append(" ,E.FUNC_TYPE,E.OPITEM , E.BFCHCON,E.AFCHCON,E.ADEXCODE "); 
		sql.append(" FROM EACH_USERLOG E ");
		sql.append(" WHERE E.SERNO = :serno AND E.USERID = :user_id AND E.USER_COMPANY = :com_id ");
//		sql.append(" WHERE E.SERNO = '"+serno+"' AND E.USERID = '"+user_id+"' AND E.USER_COMPANY = '"+com_id+"' ");
		System.out.println("sql>>>>"+sql);
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setParameter("serno", serno);
		query.setParameter("user_id", user_id);
		query.setParameter("com_id", com_id);
		query.addEntity(EACH_USERLOG.class);
		
		List<EACH_USERLOG> list = query.list();
		return list;
		
	}
	
	
	
	/**
	 * 分頁測試用
	 * @return
	 */
	public List<EACH_USERLOG> getAllDataByParm(int pageNo , int pageSize){
		StringBuffer sql = new StringBuffer();
//		sql.append(" SELECT USERIP , TXTIME FROM  EACH_USERLOG ");
//		sql.append(" SELECT E.* FROM EACH_USERLOG E "); //不可單純加 * 要給別名
		sql.append(" SELECT  E.SERNO ,E.USERID , E.USER_COMPANY ,E.USERIP,E.TXTIME"); 
		sql.append(" ,E.UP_FUNC_ID ||'-'|| (SELECT FUNC_NAME FROM EACH_FUNC_LIST WHERE FUNC_ID = E.UP_FUNC_ID) AS UP_FUNC_ID "); 
		sql.append(" ,E.FUNC_ID ||'-'||  (SELECT FUNC_NAME FROM EACH_FUNC_LIST WHERE FUNC_ID = E.FUNC_ID) AS FUNC_ID "); 
		sql.append(" ,E.FUNC_TYPE,E.OPITEM , E.BFCHCON,E.AFCHCON,E.ADEXCODE "); 
		sql.append(" FROM EACH_USERLOG E "); 
//		String countQueryString = " select count (*) " + removeSelect(removeOrders(sql.toString())); ;
		String countQueryString = " select count (*) FROM EACH_USERLOG " ;
		SQLQuery query =  getSession().createSQLQuery(countQueryString);
		List<Integer> countList = query.list();
		int totalCount =  countList.get(0);
		if (totalCount < 1) return new LinkedList<EACH_USERLOG>();
		query =  getSession().createSQLQuery(sql.toString());
//		query.addScalar("USERIP", Hibernate.STRING);
//		query.addScalar("TXTIME", Hibernate.STRING);
//		query.setResultTransformer(Transformers.aliasToBean(EACH_USERLOG.class));
		query.addEntity(EACH_USERLOG.class);
		//實際查詢返回分頁對像
		int startIndex = Page.getStartOfPage(pageNo, pageSize);
		System.out.println("startIndex>>"+startIndex);
		List<EACH_USERLOG> list = query.setFirstResult(startIndex).setMaxResults(10).list();

//		new Page(startIndex, totalCount, pageSize, list);
		return list;
	}
	
}
