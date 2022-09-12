package tw.org.twntch.db.dao.hibernate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;

import tw.org.twntch.po.TXN_ERROR_CODE;

public class TXN_ERROR_CODE_Dao extends HibernateEntityDao<TXN_ERROR_CODE, Serializable>{
	
	
	/**
	 * 非原本父類別的get(Serializable id)，
	 * 原因:使用父類別的get(id)，轉json字串時會跳出錯誤，暫時用此解法
	 * [net.sf.json.JSONException: java.lang.NoSuchMethodException: Property 'delegate' has no getter method] with root cause
	 * @param id
	 * @return
	 */
	public List<TXN_ERROR_CODE> getByPK(String id , String orderSQL){
		Query query = getSession().createQuery(" FROM tw.org.twntch.po.TXN_ERROR_CODE WHERE ERROR_ID = :id "+orderSQL );
		query.setParameter("id", id);
		List<TXN_ERROR_CODE> list = query.list();
		return list;
	}
	
	
	public List<TXN_ERROR_CODE> getAll_OrderByTxnId(){
		List list = new ArrayList();
		String sql = " FROM tw.org.twntch.po.TXN_ERROR_CODE ORDER BY ERROR_ID " ;
		try{
			Query query = getCurrentSession().createQuery(sql);
			list = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return list;
	}
	
	public List<TXN_ERROR_CODE> getAll_OrderByTxnId4Bank(){
		List list = new ArrayList();
		String sql = " FROM tw.org.twntch.po.TXN_ERROR_CODE WHERE ERROR_ID NOT LIKE '42%' ORDER BY ERROR_ID " ;
		try{
			Query query = getCurrentSession().createQuery(sql);
			list = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return list;
	}
	public List<TXN_ERROR_CODE> getAllData(String orderSQL){
		List list = new ArrayList();
		String sql = "FROM tw.org.twntch.po.TXN_ERROR_CODE "+orderSQL ;
		try{
			Query query = getCurrentSession().createQuery(sql);
			list = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 銀行端要過濾掉42xx
	 * @param orderSQL
	 * @return
	 */
	public List<TXN_ERROR_CODE> getAllData4Bank(String orderSQL){
		List list = new ArrayList();
		String sql = "FROM tw.org.twntch.po.TXN_ERROR_CODE WHERE ERROR_ID NOT LIKE '42%'  "+orderSQL ;
		try{
			Query query = getCurrentSession().createQuery(sql);
			list = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return list;
	}

}
