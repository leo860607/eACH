package tw.org.twntch.db.dao.hibernate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;

import tw.org.twntch.po.GL_ERROR_CODE;

public class GL_ERROR_CODE_Dao extends HibernateEntityDao<GL_ERROR_CODE, Serializable> {
	
	

	/**
	 * 非原本父類別的get(Serializable id)，
	 * 原因:使用父類別的get(id)，轉json字串時會跳出錯誤，暫時用此解法
	 * [net.sf.json.JSONException: java.lang.NoSuchMethodException: Property 'delegate' has no getter method] with root cause
	 * @param id
	 * @return
	 */
	public List<GL_ERROR_CODE> getByPK(String id){
		Query query = getSession().createQuery(" FROM tw.org.twntch.po.GL_ERROR_CODE WHERE ERROR_ID = :id" );
		query.setParameter("id", id);
		List<GL_ERROR_CODE> list = query.list();
		return list;
	}
	
	public List<GL_ERROR_CODE> getAll_OrderByTxnId(){
		List list = new ArrayList();
		String sql = "FROM tw.org.twntch.po.GL_ERROR_CODE ORDER BY ERROR_ID";
		try{
			Query query = getCurrentSession().createQuery(sql);
			list = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return list;
	}

}
