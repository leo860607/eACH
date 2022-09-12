package tw.org.twntch.db.dao.hibernate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;

import tw.org.twntch.po.EACH_TXN_CODE;

public class EACH_TXN_CODE_Dao extends HibernateEntityDao<EACH_TXN_CODE, Serializable> {
	public List<EACH_TXN_CODE> getTranCode(){
		List<EACH_TXN_CODE> list = null;
		String sql = "FROM tw.org.twntch.po.EACH_TXN_CODE WHERE (EACH_TXN_ID LIKE '2%' OR EACH_TXN_ID LIKE '4%' OR EACH_TXN_ID LIKE '6%') ORDER BY EACH_TXN_ID";
		try{
			Query query = getCurrentSession().createQuery(sql);
			list = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return list;
	}
	
	public List<EACH_TXN_CODE> getOpcTxnCode(){
		List<EACH_TXN_CODE> list = null;
//		20150425 edit by hugo 過濾掉5XXX的
//		String sql = "FROM tw.org.twntch.po.EACH_TXN_CODE WHERE (EACH_TXN_ID LIKE '1%' OR EACH_TXN_ID LIKE '5%') ORDER BY EACH_TXN_ID";
		String sql = "FROM tw.org.twntch.po.EACH_TXN_CODE WHERE (EACH_TXN_ID LIKE '1%' ) ORDER BY EACH_TXN_ID";
		try{
			Query query = getCurrentSession().createQuery(sql);
			list = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return list;
	}
	public List<EACH_TXN_CODE> getOpcTxnCode_Settlement(){
		List<EACH_TXN_CODE> list = null;
		String sql = "FROM tw.org.twntch.po.EACH_TXN_CODE WHERE (EACH_TXN_ID LIKE '5%' ) ORDER BY EACH_TXN_ID";
		try{
			Query query = getCurrentSession().createQuery(sql);
			list = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return list;
	}
	
	public List<EACH_TXN_CODE> getByBsTypeId(String bsType){
		List<EACH_TXN_CODE> list = null;
		String sql = "FROM tw.org.twntch.po.EACH_TXN_CODE WHERE BUSINESS_TYPE_ID = '" + bsType + "' ORDER BY EACH_TXN_ID";
		try{
			Query query = getCurrentSession().createQuery(sql);
			list = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 非原本父類別的get(Serializable id)，
	 * 原因:使用父類別的get(id)，轉json字串時會跳出錯誤，暫時用此解法
	 * [net.sf.json.JSONException: java.lang.NoSuchMethodException: Property 'delegate' has no getter method] with root cause
	 * @param id
	 * @return
	 */
	public List<EACH_TXN_CODE> getByPK(String id){
		Query query = getSession().createQuery(" FROM tw.org.twntch.po.EACH_TXN_CODE WHERE EACH_TXN_ID = :id" );
		query.setParameter("id", id);
		List<EACH_TXN_CODE> list = query.list();
		return list;
	}
	
	public List<EACH_TXN_CODE> getAll_OrderByEachTxnId(){
		List list = new ArrayList();
		String sql = "FROM tw.org.twntch.po.EACH_TXN_CODE ORDER BY EACH_TXN_ID";
		try{
			Query query = getCurrentSession().createQuery(sql);
			list = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return list;
	}
	public List<EACH_TXN_CODE> getAllData(String orderSQL){
		List list = new ArrayList();
		String sql = "FROM tw.org.twntch.po.EACH_TXN_CODE "+orderSQL;
		try{
			Query query = getCurrentSession().createQuery(sql);
			list = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return list;
	}
}
