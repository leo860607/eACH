package tw.org.twntch.db.dao.hibernate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;

import tw.org.twntch.po.EACH_FUNC_LIST;

public class EACH_FUNC_LIST_Dao extends HibernateEntityDao<EACH_FUNC_LIST, Serializable> {

	/**
	 * 根據func_id 及 func_type 來查詢是否有被使用
	 * @param func_id
	 * @param inVals
	 * @return
	 */
	public List<Map> funcIsUsed(String func_id , List<String> inVals){
		List<Map> list = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT B.ROLE_ID,B.ROLE_TYPE FROM  EACHUSER.EACH_ROLE_FUNC A " );
		sql.append(" LEFT JOIN EACHUSER.EACH_ROLE_LIST B " );
		sql.append(" ON A.ROLE_ID = B.ROLE_ID " );
		sql.append(" WHERE A.FUNC_ID = :func_id AND B.ROLE_TYPE IN (:inVals) " );
		try{
			SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
			query.setParameter("func_id", func_id);
			query.setParameterList("inVals", inVals);
			query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			list = query.list();
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	
	
	
	/**
	 * 根據使用者目前操作的url 及所屬群組 找出相應的功能權限
	 * @param func_url
	 * @param role_id
	 * @return
	 */
	public List<Map> getFuncAuth(String func_url , String role_id){
		
		List<Map> list = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT b.AUTH_TYPE ,A.FUNC_ID ,A.FUNC_NAME , value(A.IS_RECORD,'')  IS_RECORD , value(A.FUNC_NAME_BK,'') FUNC_NAME_BK  ,(SELECT FUNC_NAME FROM EACH_FUNC_LIST WHERE FUNC_ID = A.UP_FUNC_ID ) UP_FUCNAME " );
		sql.append(" ,(SELECT value(FUNC_NAME_BK,'') FUNC_NAME_BK  FROM EACH_FUNC_LIST WHERE FUNC_ID = A.UP_FUNC_ID ) UP_FUCNAME_BK " );
		sql.append(" FROM EACHUSER.EACH_FUNC_LIST A  " );
		sql.append(" LEFT JOIN  EACH_ROLE_FUNC B ON A.FUNC_ID = B.FUNC_ID  " );
		sql.append(" WHERE A.FUNC_TYPE ='2' AND A.FUNC_URL = :func_url AND B.ROLE_ID = :role_id  " );
		try{
			SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
			query.setParameter("func_url", func_url);
			query.setParameter("role_id", role_id);
			query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			list = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
		}
		
		return list;
	}
	
	/**
	 * 根據使用者目前操作的func_id 及所屬群組 找出相應的功能權限
	 * @param func_url
	 * @param role_id
	 * @return
	 */
	public List<Map> getFuncAuthByFuncId(String fcid , String role_id){
		
		List<Map> list = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT b.AUTH_TYPE ,A.FUNC_ID ,A.FUNC_NAME ,value(A.IS_RECORD,'')  IS_RECORD  , value(A.FUNC_NAME_BK,'') FUNC_NAME_BK ,(SELECT FUNC_NAME FROM EACH_FUNC_LIST WHERE FUNC_ID = A.UP_FUNC_ID ) UP_FUCNAME " );
		sql.append("  ,(SELECT value(FUNC_NAME_BK,'') FUNC_NAME_BK FROM EACH_FUNC_LIST WHERE FUNC_ID = A.UP_FUNC_ID ) UP_FUCNAME_BK " );
		sql.append(" FROM EACHUSER.EACH_FUNC_LIST A  " );
		sql.append(" LEFT JOIN  EACH_ROLE_FUNC B ON A.FUNC_ID = B.FUNC_ID  " );
		sql.append(" WHERE A.FUNC_TYPE ='2' AND A.FUNC_ID = :fcid AND B.ROLE_ID = :role_id  " );
		try{
			SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
			query.setParameter("fcid", fcid);
			query.setParameter("role_id", role_id);
			query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			list = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
		}
		
		return list;
	}
	
	public List<EACH_FUNC_LIST> getNextSubItem(String funcId){
		List<EACH_FUNC_LIST> list = find(" FROM tw.org.twntch.po.EACH_FUNC_LIST WHERE FUNC_TYPE = '2'  AND UP_FUNC_ID = ? ORDER BY FUNC_ID", funcId);
		return list;
	}
	public List<EACH_FUNC_LIST> getNextSubItemII(String funcId , String user_type){
		StringBuffer hql = new StringBuffer();
		hql.append(" FROM tw.org.twntch.po.EACH_FUNC_LIST WHERE FUNC_TYPE = '2'  AND UP_FUNC_ID = ?  ");
		if(user_type.equals("A")){
			hql.append(" AND TCH_FUNC != 'N' ");
		}
		if(user_type.equals("B")){
			hql.append(" AND BANK_FUNC != 'N' ");
		}
		if(user_type.equals("C")){
			hql.append(" AND COMPANY_FUNC != 'N' ");
		}
		hql.append(" ORDER BY FUNC_ID  ");
		List<EACH_FUNC_LIST> list = find(hql.toString(), funcId);
		
		return list;
	}
	
	public List<EACH_FUNC_LIST> getNextActiveSubItem(String funcId){
		List<EACH_FUNC_LIST> list = find(" FROM tw.org.twntch.po.EACH_FUNC_LIST WHERE FUNC_TYPE = '2'  AND UP_FUNC_ID = ? AND IS_USED = 'Y' ORDER BY FUNC_ID", funcId);
		return list;
	}
	
	public List<EACH_FUNC_LIST> getNextActiveSubItemByType(String funcId, String userType, String roleId){
		List<EACH_FUNC_LIST> list = null;
		String sql = "SELECT FL.* FROM EACH_ROLE_FUNC RF JOIN EACH_FUNC_LIST FL ON RF.FUNC_ID = FL.FUNC_ID ";
		sql += "WHERE RF.ROLE_ID = :roleId AND FL.FUNC_TYPE = '2' AND IS_USED = 'Y' AND UP_FUNC_ID = :upFuncId AND ";
		//20141210 HUANGPU David說，應依照使用者類型挑選功能
		//票交所
		if(userType.equals("A")){
			sql += "FL.TCH_FUNC = 'Y'";
		//銀行端
		}else if(userType.equals("B")){
			sql += "FL.BANK_FUNC = 'Y'";
		//發動者
		}else if(userType.equals("C")){
			sql += "FL.COMPANY_FUNC = 'Y'";
		}
		sql += " ORDER BY FL.FUNC_ID";
		try{
			SQLQuery query = getCurrentSession().createSQLQuery(sql);
			query.setParameter("roleId", roleId);
			query.setParameter("upFuncId", funcId);
			query.addEntity(EACH_FUNC_LIST.class);
			//query.setResultTransformer(Transformers.aliasToBean(EACH_FUNC_LIST.class));
			list = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return list;
	}
	
	public List<EACH_FUNC_LIST> getFuncItemByType(String func_type){
		List<EACH_FUNC_LIST> list = find(" FROM tw.org.twntch.po.EACH_FUNC_LIST WHERE  FUNC_TYPE=?  ORDER BY FUNC_ID",  func_type);
		return list;
	}
	public List<EACH_FUNC_LIST> getFuncItemByTypes(String func_type ,String path_sql){
		List<EACH_FUNC_LIST> list = find(" FROM tw.org.twntch.po.EACH_FUNC_LIST WHERE  FUNC_TYPE=? "+path_sql+"  ORDER BY FUNC_ID",  func_type);
		return list;
	}
	
	public List<EACH_FUNC_LIST> getAllSubItem(String path_sql){
		StringBuffer sql = new StringBuffer();
//		sql.append(" SELECT M.FUNC_NAME AS UP_FUNC_NAME,S.UP_FUNC_ID||'_'||S.FUNC_ID AS htmlId,S.FUNC_ID ,S.FUNC_NAME ,S.UP_FUNC_ID ,S.IS_USED FROM EACH_FUNC_LIST S  ") ;
		sql.append(" SELECT value(M.FUNC_NAME,'') AS UP_FUNC_NAME,value(S.UP_FUNC_ID||'_'||S.FUNC_ID ,'')AS htmlId,value(S.FUNC_ID,'') FUNC_ID ,value(M.FUNC_ID,'' ) UP_FUNC_ID ,value(S.FUNC_NAME,'') FUNC_NAME , value(S.IS_USED,'N') IS_USED FROM EACH_FUNC_LIST S  ") ;
//		sql.append(" JOIN EACH_FUNC_LIST  M ON S.UP_FUNC_ID=M.FUNC_ID  ") ;
		sql.append(" RIGHT JOIN EACH_FUNC_LIST  M ON S.UP_FUNC_ID=M.FUNC_ID  ") ;
		sql.append( path_sql ) ;
//		所方最後決定用FUNC_ID 來排序
//		sql.append(" ORDER BY  CAST (M.FUNC_SEQ AS INT),CAST ( S.FUNC_SEQ AS INT )  WITH UR  ") ;
		sql.append(" ORDER BY  M.FUNC_ID  WITH UR  ") ;
		System.out.println("getAllSubItem.sql>>"+sql);
		SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
		query.addScalar("htmlId" , Hibernate.STRING);
		query.addScalar("UP_FUNC_NAME" , Hibernate.STRING);
		query.addScalar("FUNC_ID" , Hibernate.STRING);
		query.addScalar("FUNC_NAME" , Hibernate.STRING);
		query.addScalar("UP_FUNC_ID" , Hibernate.STRING);
		query.addScalar("IS_USED" , Hibernate.STRING);
		query.setResultTransformer(Transformers.aliasToBean(EACH_FUNC_LIST.class));
//		query.addEntity(EACH_FUNC_LIST.class);
		List<EACH_FUNC_LIST> list = query.list();
		return list;
	}
	
	public List<EACH_FUNC_LIST> getSelectedItem(String role_id ,String func_type){
		StringBuffer sql = new StringBuffer();
//		sql.append(" SELECT FL.* FROM EACH_ROLE_FUNC RF  ");
		sql.append(" SELECT RF.AUTH_TYPE, FL.FUNC_ID ,FL.UP_FUNC_ID ,FL.IS_USED FROM EACH_ROLE_FUNC RF  ");
		sql.append(" JOIN EACH_FUNC_LIST FL ON RF.FUNC_ID = FL.FUNC_ID    ");
		sql.append(" WHERE FL.FUNC_TYPE = :func_type AND RF.ROLE_ID=:role_id  ");
//		sql.append(" AND FL.BANK_FUNC = 'Y' ");
//		sql.append(" WHERE FL.FUNC_TYPE = :func_type AND RF.ROLE_ID=:role_id AND FL.IS_USED='Y' ");
		sql.append(" ORDER BY FL.FUNC_ID WITH UR ");
		SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
		query.setParameter("role_id", role_id);
		query.setParameter("func_type", func_type);
//		query.addEntity(EACH_FUNC_LIST.class);
		query.addScalar("FUNC_ID" , Hibernate.STRING);
		query.addScalar("UP_FUNC_ID" , Hibernate.STRING);
		query.addScalar("AUTH_TYPE" , Hibernate.STRING);
		query.addScalar("IS_USED" , Hibernate.STRING);
		query.setResultTransformer(Transformers.aliasToBean(EACH_FUNC_LIST.class));
		List<EACH_FUNC_LIST> list = query.list();
		return list;
	}
	
	
	public boolean saveData(EACH_FUNC_LIST po, List<EACH_FUNC_LIST> subList) {
		boolean result = false;
		Session session = getSessionFactory().openSession();
		session.beginTransaction();
		
		try {
			//若為作業模組，則需檢查是否啟用，若停用，則需停用所有子項目
			if(subList != null && subList.size() > 0){
				for(int i = 0; i < subList.size(); i++){
					subList.get(i).setIS_USED("N");
					subList.get(i).setSTART_DATE("");
					session.merge(subList.get(i));
				}
			}
			
			session.saveOrUpdate(po);
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
	
	public List<EACH_FUNC_LIST> getData(String funcId) {
		Session session = getSessionFactory().openSession();
		session.beginTransaction();
		List<EACH_FUNC_LIST> list = new ArrayList<EACH_FUNC_LIST>();
		try {
//			Query query = session.createQuery("FROM tw.org.twntch.po.EACH_FUNC_LIST WHERE FUNC_ID = :funcId ORDER BY FUNC_ID");
			Query query = session.createQuery("FROM tw.org.twntch.po.EACH_FUNC_LIST WHERE FUNC_ID LIKE :funcId ORDER BY FUNC_ID");
//			query.setParameter("funcId", funcId);
			query.setParameter("funcId", funcId+"%");
			list = query.list();
			session.beginTransaction().commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			session.beginTransaction().rollback();
		}
		return list;
	}
	public List<EACH_FUNC_LIST> getData(String funcId , String orderSQL) {
		Session session = getSessionFactory().openSession();
		session.beginTransaction();
		List<EACH_FUNC_LIST> list = new ArrayList<EACH_FUNC_LIST>();
		try {
//			Query query = session.createQuery("FROM tw.org.twntch.po.EACH_FUNC_LIST WHERE FUNC_ID = :funcId ORDER BY FUNC_ID");
			Query query = session.createQuery("FROM tw.org.twntch.po.EACH_FUNC_LIST WHERE FUNC_ID LIKE :funcId "+orderSQL);
//			query.setParameter("funcId", funcId);
			query.setParameter("funcId", funcId+"%");
			list = query.list();
			session.beginTransaction().commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			session.beginTransaction().rollback();
		}
		return list;
	}
	
	public List<EACH_FUNC_LIST> getAllData() {
		Session session = getSessionFactory().openSession();
		session.beginTransaction();
		List<EACH_FUNC_LIST> list = new ArrayList<EACH_FUNC_LIST>();
		try {
			Query query = session.createQuery("FROM tw.org.twntch.po.EACH_FUNC_LIST ORDER BY FUNC_ID");
			list = query.list();
			session.beginTransaction().commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			session.beginTransaction().rollback();
		}
		return list;
	}
	public List<EACH_FUNC_LIST> getAllData(String orderSQL) {
		Session session = getSessionFactory().openSession();
		session.beginTransaction();
		List<EACH_FUNC_LIST> list = new ArrayList<EACH_FUNC_LIST>();
		try {
			Query query = session.createQuery("FROM tw.org.twntch.po.EACH_FUNC_LIST "+orderSQL);
			list = query.list();
			session.beginTransaction().commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			session.beginTransaction().rollback();
		}
		return list;
	}
	
	public boolean delData(EACH_FUNC_LIST po, List<EACH_FUNC_LIST> subList){
		boolean result = false;
		Session session = getSessionFactory().openSession();
		session.beginTransaction();
		
		try {
			//若刪除作業模組，則需清空其所有子項目的「上層所屬作業 UP_FUNC_ID」欄位
			if(subList != null && subList.size() > 0){
				for(int i = 0; i < subList.size(); i++){
					subList.get(i).setUP_FUNC_ID("");
					session.merge(subList.get(i));
				}
			}
			
			session.delete(po);
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
