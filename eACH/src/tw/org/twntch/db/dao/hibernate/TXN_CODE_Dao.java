package tw.org.twntch.db.dao.hibernate;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;

import tw.org.twntch.po.FEE_CODE;
import tw.org.twntch.po.TXN_CODE;
import tw.org.twntch.po.TXN_FEE_MAPPING;
import tw.org.twntch.util.AutoAddScalar;
import tw.org.twntch.util.zDateHandler;

public class TXN_CODE_Dao extends HibernateEntityDao<TXN_CODE, Serializable> {
	
	public List<TXN_CODE> getAllData(String orderSQL){
		List<TXN_CODE> list = null;
		String sql = "SELECT TC.*, COALESCE(TFM.FEE_ID,'無設定') AS FEE_ID, COALESCE(TFM.MAPPING_START_DATE,'無設定') AS START_DATE FROM EACHUSER.TXN_CODE TC LEFT JOIN ("
				+ "SELECT TXN_ID,FEE_ID,MAX(MAPPING_START_DATE) AS MAPPING_START_DATE "
				+ "FROM EACHUSER.TXN_FEE_MAPPING WHERE START_DATE <= '" + zDateHandler.getTWDate() + "' "
				
				+ "GROUP BY TXN_ID,FEE_ID "
				+ ") TFM ON TC.TXN_ID = TFM.TXN_ID  "
				+ orderSQL;
		try{
			SQLQuery query = getCurrentSession().createSQLQuery(sql);
			String cols[] = {"TXN_ID","TXN_NAME","TXN_TYPE","TXN_CHECK_TYPE","TXN_DESC","ACTIVE_DATE","MAX_TXN_AMT","BUSINESS_TYPE_ID","CDATE","UDATE","FEE_ID","START_DATE","AGENT_TXN_ID"};
			AutoAddScalar addScalar = new AutoAddScalar();
			addScalar.addScalar(query, TXN_CODE.class, cols);
			query.setResultTransformer(Transformers.aliasToBean(TXN_CODE.class));
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
	public List<TXN_CODE> getByPK(String id , String orderSQL){
		List<TXN_CODE> list = null;
		String sql = "FROM tw.org.twntch.po.TXN_CODE WHERE TXN_ID = :id "+ orderSQL;
		try{
			Query query = getCurrentSession().createQuery(sql);
			query.setParameter("id", id);
			list = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
		}
		
		/*
		String sql = "SELECT TC.*, COALESCE(TFM.FEE_ID,'無設定') AS FEE_ID, COALESCE(TFM.MAPPING_START_DATE,'無設定') AS START_DATE FROM EACHUSER.TXN_CODE TC LEFT JOIN ("
				+ "SELECT TXN_ID,FEE_ID,MAX(MAPPING_START_DATE) AS MAPPING_START_DATE "
				+ "FROM EACHUSER.TXN_FEE_MAPPING WHERE START_DATE <= '" + zDateHandler.getTWDate() + "' "
				+ "GROUP BY TXN_ID,FEE_ID "
				+ ") TFM ON TC.TXN_ID = TFM.TXN_ID WHERE TC.TXN_ID = :id";
				
		try{
			SQLQuery query = getCurrentSession().createSQLQuery(sql);
			query.setParameter("id", id);
			String cols[] = {"TXN_ID","TXN_NAME","TXN_TYPE","TXN_CHECK_TYPE","TXN_DESC","ACTIVE_DATE","MAX_TXN_AMT","BUSINESS_TYPE_ID","CDATE","UDATE","FEE_ID","START_DATE"};
			AutoAddScalar addScalar = new AutoAddScalar();
			addScalar.addScalar(query, TXN_CODE.class, cols);
			query.setResultTransformer(Transformers.aliasToBean(TXN_CODE.class));
			list = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
		}
		*/
		return list;
	}
	
	public List<TXN_CODE> getAll_OrderByTxnId(){
		List list = new ArrayList();
		String sql = "FROM tw.org.twntch.po.TXN_CODE ORDER BY TXN_ID";
		try{
			Query query = getCurrentSession().createQuery(sql);
			list = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 根據業務類別代號取得交易代號清單
	 * @param bs_id
	 * @return
	 */
	public List<TXN_CODE> getData_ByBS_Id(String bs_id){
		List list = new ArrayList();
		String sql = "FROM tw.org.twntch.po.TXN_CODE WHERE  BUSINESS_TYPE_ID = :bs_id AND TXN_ID <> '930' ORDER BY TXN_ID ";
		try{
			Query query = getCurrentSession().createQuery(sql);
			query.setParameter("bs_id", bs_id);
			list = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return list;
	}
	
	public List<TXN_CODE> getByIdAndActiveDate(String condition , String orderSQL){
		/*
		Query query = getSession().createQuery(" FROM tw.org.twntch.po.TXN_CODE WHERE " + condition);
		List<TXN_CODE> list = query.list();
		list = list == null? null : list.size() == 0 ? null : list;
		return list;
		*/
		List<TXN_CODE> list = null;
		String sql = "SELECT TC.*, COALESCE(TFM.FEE_ID,'無設定') AS FEE_ID, COALESCE(TFM.MAPPING_START_DATE,'無設定') AS START_DATE FROM EACHUSER.TXN_CODE TC LEFT JOIN ("
				+ "SELECT TXN_ID,FEE_ID,MAX(MAPPING_START_DATE) AS MAPPING_START_DATE "
				+ "FROM EACHUSER.TXN_FEE_MAPPING WHERE START_DATE <= '" + zDateHandler.getTWDate() + "' "
				+ "GROUP BY TXN_ID,FEE_ID "
				+ ") TFM ON TC.TXN_ID = TFM.TXN_ID WHERE " + condition
				+ orderSQL;
		try{
			SQLQuery query = getCurrentSession().createSQLQuery(sql);
			String cols[] = {"TXN_ID","TXN_NAME","TXN_TYPE","TXN_CHECK_TYPE","TXN_DESC","ACTIVE_DATE","MAX_TXN_AMT","BUSINESS_TYPE_ID","CDATE","UDATE","FEE_ID","START_DATE","AGENT_TXN_ID"};
			AutoAddScalar addScalar = new AutoAddScalar();
			addScalar.addScalar(query, TXN_CODE.class, cols);
			query.setResultTransformer(Transformers.aliasToBean(TXN_CODE.class));
			list = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return list;
	}
	
	public List<TXN_FEE_MAPPING> getFeeIdListByTxnId(String txnId){
		Query query = getSession().createQuery("FROM tw.org.twntch.po.TXN_FEE_MAPPING WHERE TXN_ID =:txnId");
		query.setParameter("txnId", txnId);
		List<TXN_FEE_MAPPING> list = query.list();
		list = list == null? null : list.size() == 0 ? null : list;
		return list;
	}
	
	public int deleteMappingByTxnId(String txnId){
		SQLQuery query = getSession().createSQLQuery("DELETE FROM TXN_FEE_MAPPING WHERE TXN_ID =:txnId");
		query.setParameter("txnId", txnId);
		return query.executeUpdate();
	}
	
	public boolean saveData(TXN_CODE txnCode, FEE_CODE feeCode){
		boolean result = false;
		Session session = getSessionFactory().openSession();
		session.beginTransaction();
		
		try {
			session.saveOrUpdate(feeCode);
			session.saveOrUpdate(txnCode);
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
	
	/**
	 * 20150120 HUANGPU 新增單筆
	 * @param po
	 * @param tfmList
	 * @return
	 */
	public boolean saveData(TXN_CODE po, TXN_FEE_MAPPING tfm){
		boolean result = false;
		Session session = getSessionFactory().openSession();
		session.beginTransaction();
		
		try {
			//Insert into TXN_FEE_MAPPING
			session.saveOrUpdate(tfm);
			//Insert into TXN_CODE
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
	
	public boolean updateData(TXN_CODE po, List<TXN_FEE_MAPPING> tfmList_toDelete, List<TXN_FEE_MAPPING> tfmList_toInsert){
		boolean result = false;
		Session session = getSessionFactory().openSession();
		session.beginTransaction();
		
		try {
			//Delete from TXN_FEE_MAPPING
			if(tfmList_toDelete != null){
				for(int i = 0; i < tfmList_toDelete.size(); i++){
					session.delete(tfmList_toDelete.get(i));
				}
			}
			//Insert into TXN_FEE_MAPPING
			for(int i = 0; i < tfmList_toInsert.size(); i++){
				session.merge(tfmList_toInsert.get(i));
			}
			//Insert into TXN_CODE
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
	
	public boolean updateData(TXN_CODE po, TXN_FEE_MAPPING txnFeeMapping){
		boolean result = false;
		Session session = getSessionFactory().openSession();
		session.beginTransaction();
		
		try {
			//Insert into TXN_FEE_MAPPING
			session.merge(txnFeeMapping);
			//Insert into TXN_CODE
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
	
	public boolean delData(TXN_CODE po){
		boolean result = false;
		Session session = getSessionFactory().openSession();
		session.beginTransaction();
		
		try {
			//Delete from TXN_CODE
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
	
	public List<TXN_CODE> getByBsTypeId(String bsType){
		List<TXN_CODE> list = null;
		String sql = "FROM tw.org.twntch.po.TXN_CODE WHERE BUSINESS_TYPE_ID = '" + bsType + "' ORDER BY TXN_ID";
		try{
			Query query = getCurrentSession().createQuery(sql);
			list = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return list;
	}
}
