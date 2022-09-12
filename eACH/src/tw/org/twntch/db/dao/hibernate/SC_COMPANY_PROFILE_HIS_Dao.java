package tw.org.twntch.db.dao.hibernate;

import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;

import tw.org.twntch.po.SC_COMPANY_PROFILE_HIS;
import tw.org.twntch.util.AutoAddScalar;

public class SC_COMPANY_PROFILE_HIS_Dao extends HibernateEntityDao<SC_COMPANY_PROFILE_HIS, Integer> {

	public List<SC_COMPANY_PROFILE_HIS> getData(String sql, List<String> params ,  String[] cols, Class targetClass) {
		List list = null;
		try {
			SQLQuery query = getCurrentSession().createSQLQuery(sql);
			for(int i = 0; i < params.size(); i++){
				query.setParameter(i, params.get(i));
			}
			AutoAddScalar addscalar = new AutoAddScalar();
			addscalar.addScalar(query, targetClass, cols);
			query.setResultTransformer(Transformers.aliasToBean(targetClass));
			list = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	
	public List<Map<String, String>> findByCondition(String company_id , String txn_id , String snd_brbk_id){
		List<Map<String, String>> list = null;
		String sql = "SELECT * FROM SC_COMPANY_PROFILE_HIS WHERE COMPANY_ID=:company_id AND TXN_ID=:txn_id AND SND_BRBK_ID=:snd_brbk_id";
		try{
			SQLQuery query = getCurrentSession().createSQLQuery(sql);
			query.setParameter("company_id", company_id);
			query.setParameter("txn_id", txn_id);
			query.setParameter("snd_brbk_id", snd_brbk_id);
			query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			list = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return list;	
	}
	public List<Map<String, String>> checkDoubleDate(String company_id , String txn_id , String snd_brbk_id , String fee_type_active_date){
		List<Map<String, String>> list = null;
		String sql = "SELECT * FROM SC_COMPANY_PROFILE_HIS WHERE COMPANY_ID=:company_id AND TXN_ID=:txn_id AND SND_BRBK_ID=:snd_brbk_id AND ACTIVE_DATE=:fee_type_active_date";
		try{
			SQLQuery query = getCurrentSession().createSQLQuery(sql);
			query.setParameter("company_id", company_id);
			query.setParameter("txn_id", txn_id);
			query.setParameter("snd_brbk_id", snd_brbk_id);
			query.setParameter("fee_type_active_date", fee_type_active_date);
			query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			list = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return list;	
	}
}
