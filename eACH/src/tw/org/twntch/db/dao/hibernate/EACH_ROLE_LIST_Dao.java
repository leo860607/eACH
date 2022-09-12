package tw.org.twntch.db.dao.hibernate;

import java.io.Serializable;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import tw.org.twntch.po.EACH_ROLE_FUNC;
import tw.org.twntch.po.EACH_ROLE_LIST;

public class EACH_ROLE_LIST_Dao extends HibernateEntityDao<EACH_ROLE_LIST, Serializable> {

	public List<EACH_ROLE_LIST> getDataById(String role_id){
		List<EACH_ROLE_LIST> list = find(" FROM tw.org.twntch.po.EACH_ROLE_LIST WHERE ROLE_ID = ?",role_id);
		return list;
	}
	public List<EACH_ROLE_LIST> getDataByType(String role_type){
		List<EACH_ROLE_LIST> list = find(" FROM tw.org.twntch.po.EACH_ROLE_LIST WHERE ROLE_TYPE = ?",role_type);
		return list;
	}
	
	public boolean deleteRole_FuncByRole_Id(String role_id){
		boolean result = false ;
		Session session = 	getSessionFactory().openSession();
		StringBuffer sql = new StringBuffer();
		try {
			sql.append(" DELETE FROM tw.org.twntch.po.EACH_ROLE_FUNC WHERE ROLE_ID = :role_id" );
			session.beginTransaction();
			session.createQuery(sql.toString()).setParameter("role_id", role_id).executeUpdate();
			sql.delete(0, sql.length());
			sql.append(" DELETE FROM tw.org.twntch.po.EACH_ROLE_LIST WHERE ROLE_ID = :role_id" );
			session.createQuery(sql.toString()).setParameter("role_id", role_id).executeUpdate();
			session.beginTransaction().commit();
			result=true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result=false;
			session.beginTransaction().rollback();;
		}
		return result;
	}
	public boolean saveData(EACH_ROLE_LIST role_list ,List<EACH_ROLE_FUNC> role_funcs ){
		boolean result = false ;
		Session session = 	getSessionFactory().openSession();
		StringBuffer sql = new StringBuffer();
		int count = 0;
		try {
			sql.append(" DELETE FROM tw.org.twntch.po.EACH_ROLE_FUNC WHERE ROLE_ID = '"+role_list.getROLE_ID()+"'" );
			session.beginTransaction();
			count = session.createQuery(sql.toString()).executeUpdate();
//			session.delete(" DELETE FROM tw.org.twntch.po.EACH_ROLE_FUNC WHERE ROLE_ID = ? " ,role_list );
			System.out.println("count>>"+count);
			session.saveOrUpdate(role_list);
			for(EACH_ROLE_FUNC tmp_po:role_funcs){
				session.saveOrUpdate(tmp_po);
			}
			session.beginTransaction().commit();
			result=true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result=false;
			session.beginTransaction().rollback();;
		}
		return result;
	}
}
