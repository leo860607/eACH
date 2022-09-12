package tw.org.twntch.db.dao.hibernate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;

import tw.org.twntch.po.BANK_GROUP;
import tw.org.twntch.po.BANK_GROUP_BUSINESS;
import tw.org.twntch.po.BUSINESS_TYPE;
import tw.org.twntch.util.zDateHandler;

public class BANK_GROUP_BUSINESS_Dao extends HibernateEntityDao<BANK_GROUP_BUSINESS, Serializable>{
	
	public List<BANK_GROUP_BUSINESS> getByBgbkId(String bgbkId){
		List list = new ArrayList();
		Query query = getCurrentSession().createQuery("FROM tw.org.twntch.po.BANK_GROUP_BUSINESS WHERE BGBK_ID =:bgbkId ORDER BY BUSINESS_TYPE_ID");
		query.setParameter("bgbkId", bgbkId);
		list = query.list();
		return list;
	}
	
	public List<BANK_GROUP_BUSINESS> getByBsTypeId(String bsTypeId){
		List list = new ArrayList();
		Query query = getCurrentSession().createQuery("FROM tw.org.twntch.po.BANK_GROUP_BUSINESS WHERE BUSINESS_TYPE_ID =:bsTypeId ORDER BY BGBK_ID");
		query.setParameter("bsTypeId", bsTypeId);
		list = query.list();
		return list;
	}
	
	public List<BUSINESS_TYPE> getUnselectedBsTypeList(String bgbkId){
		List list = new ArrayList();
		SQLQuery query = getCurrentSession().createSQLQuery("SELECT * FROM BUSINESS_TYPE WHERE BUSINESS_TYPE_ID NOT IN (SELECT BUSINESS_TYPE_ID FROM BANK_GROUP_BUSINESS WHERE BGBK_ID =:bgbkId) ORDER BY BUSINESS_TYPE_ID ");
		query.setParameter("bgbkId", bgbkId);
		query.addEntity(tw.org.twntch.po.BUSINESS_TYPE.class);
		list = query.list();
		return list;
	}
	
	/**
	 * 找出未承辦某業務類別的所有總行清單
	 * PU EDIT - 不可列入已停用的總行
	 * @param bsType
	 * @return
	 */
	public List<BANK_GROUP> getUnselectedBgbkIdList(String bsType){
		List<BANK_GROUP> list = new ArrayList();
		String sql = "SELECT * FROM BANK_GROUP WHERE BGBK_ID NOT IN (";
		sql += "SELECT DISTINCT BGBK_ID  FROM EACHUSER.BANK_GROUP_BUSINESS  WHERE BUSINESS_TYPE_ID = '" + bsType + "' ";
		sql += ") AND (STOP_DATE = '' OR STOP_DATE > '" + zDateHandler.getTWDate() + "') ORDER BY BGBK_ID";
		try{
			SQLQuery query = getCurrentSession().createSQLQuery(sql);
			query.addEntity(BANK_GROUP.class);
			list = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 找出有承辦某業務類別的所有總行清單
	 * @param bsType
	 * @return
	 */
	public List<BANK_GROUP> getSelectedBgbkIdList(String bsType){
		List<BANK_GROUP> list = new ArrayList();
		String sql = "SELECT * FROM BANK_GROUP WHERE BGBK_ID IN (";
		sql += "SELECT DISTINCT BGBK_ID  FROM EACHUSER.BANK_GROUP_BUSINESS  WHERE BUSINESS_TYPE_ID = '" + bsType + "' ";
		sql += ")ORDER BY BGBK_ID";
		try{
			SQLQuery query = getCurrentSession().createSQLQuery(sql);
			query.addEntity(BANK_GROUP.class);
			list = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return list;
	}
}
