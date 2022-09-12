package tw.org.twntch.db.dao.hibernate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;

import tw.org.twntch.po.TXN_CODE;
import tw.org.twntch.po.TXN_FEE_MAPPING;

public class TXN_FEE_MAPPING_Dao extends HibernateEntityDao<TXN_FEE_MAPPING, Serializable> {
	public List<TXN_FEE_MAPPING> getByFeeId(String feeId){
		List<TXN_FEE_MAPPING> list = null;
		String sql = "FROM tw.org.twntch.po.TXN_FEE_MAPPING WHERE FEE_ID = :feeId";
		try{
			Query query = getCurrentSession().createQuery(sql);
			query.setParameter("feeId", feeId);
			list = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return list;
	}
	
	public List<TXN_FEE_MAPPING> getByTxnId(String txnId){
		List<TXN_FEE_MAPPING> list = null;
		String sql = "FROM tw.org.twntch.po.TXN_FEE_MAPPING WHERE TXN_ID = :txnId";
		try{
			Query query = getCurrentSession().createQuery(sql);
			query.setParameter("txnId", txnId);
			list = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return list;
	}
}
