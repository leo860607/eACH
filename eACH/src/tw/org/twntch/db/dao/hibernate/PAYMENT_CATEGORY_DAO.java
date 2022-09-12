package tw.org.twntch.db.dao.hibernate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;

import tw.org.twntch.po.BILL_TYPE;

public class PAYMENT_CATEGORY_DAO extends HibernateEntityDao<BILL_TYPE, Serializable>{

	public List<BILL_TYPE> getByBsTypeId(String billId){
		List<BILL_TYPE> list = null;
		String sql = "FROM tw.org.twntch.po.BILL_TYPE WHERE BILL_TYPE_ID = '" + billId + "' ORDER BY BILL_TYPE_ID";
		try{
			Query query = getCurrentSession().createQuery(sql);
			list = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return list;
	}
	
	public List<BILL_TYPE> getByPK(String id){
		Query query = getSession().createQuery(" FROM tw.org.twntch.po.BILL_TYPE WHERE BILL_TYPE_ID = :id" );
		query.setParameter("id", id);
		List<BILL_TYPE> list = query.list();
		return list;
	}
	
	public List<BILL_TYPE> getAll_OrderByEachTxnId(){
		List list = new ArrayList();
		String sql = "FROM tw.org.twntch.po.BILL_TYPE ORDER BY BILL_TYPE_ID";
		try{
			Query query = getCurrentSession().createQuery(sql);
			list = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return list;
	}
	public List<BILL_TYPE> getAllData(String orderSQL){
		List list = new ArrayList();
		String sql = "FROM tw.org.twntch.po.BILL_TYPE "+orderSQL;
		try{
			Query query = getCurrentSession().createQuery(sql);
			list = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return list;
	}

}
