package tw.org.twntch.db.dao.hibernate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;

import tw.org.twntch.po.AGENT_CR_LINE;
import tw.org.twntch.po.AGENT_SEND_PROFILE;
import tw.org.twntch.po.AGENT_SEND_PROFILE_PK;

public class AGENT_SEND_PROFILE_Dao extends HibernateEntityDao<AGENT_SEND_PROFILE, AGENT_SEND_PROFILE_PK>{

	
	/**
	 * 
	 * @param txn_id
	 * @param company_id
	 * @param snd_company_id
	 * @return
	 */
	public Map<String , Integer> getTxnLogDataById(String txn_id , String company_id , String snd_company_id){
		List<Map<String , Integer>> list = null;
		StringBuffer sql = new StringBuffer();
		Map<String , Integer> retMap = new HashMap<String, Integer>();
		retMap.put("NUM", 0);
		System.out.println();
		try {
			
			sql.append(" WITH TEMP AS ( ");
			sql.append(" SELECT PCODE , INNATIONALID , OUTNATIONALID , COALESCE( ISSUERID ,'') AS COMPANY_ID , COALESCE(TXID,'' )TXN_ID ");
			sql.append(" ,(CASE WHEN COALESCE(PCODE ,'') IN ('2505', '2506', '2705', '2706') THEN    COALESCE( OUTNATIONALID  ,'' )ELSE COALESCE(INNATIONALID,'')  END ) AS SND_COMPANY_ID ");
			sql.append(" FROM  EACHUSER.TXNLOG");
			sql.append(" ) ");
			sql.append(" SELECT COUNT(*) AS NUM  FROM TEMP ");
			sql.append(" WHERE COMPANY_ID=:company_id  AND SND_COMPANY_ID = :snd_company_id AND TXN_ID = :txn_id");
			SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
			query.setParameter("txn_id", txn_id);
			query.setParameter("company_id", company_id);
			query.setParameter("snd_company_id", snd_company_id);
			query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			list = query.list();
			if(list!=null && list.size() !=0){
				retMap = list.get(0);
			}
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retMap;
	}
	
	
	public Map<String , Integer> getDataByCom_Id_Txn_Id(String txn_id , String company_id){
		List<Map<String , Integer>> list = null;
		Map<String , Integer> retMap = new HashMap<String, Integer>();
		retMap.put("NUM", 0);
		try {
			String sql = " SELECT COUNT(*) AS NUM FROM AGENT_SEND_PROFILE WHERE TXN_ID = :txn_id AND COMPANY_ID = :company_id ";
			SQLQuery query = getCurrentSession().createSQLQuery(sql);
			query.setParameter("txn_id", txn_id);
			query.setParameter("company_id", company_id);
			query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			list = query.list();
			if(list!=null && list.size() !=0){
				retMap = list.get(0);
			}
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retMap;
	}

	public List<AGENT_SEND_PROFILE> getData(String sql ,  List<String> values){
		List<AGENT_SEND_PROFILE> list = null;
		try{
			SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
			int i = 0;
			for(String val :values){
				query.setParameter(i, val);
				i++;
			}
			query.setResultTransformer(Transformers.aliasToBean(AGENT_SEND_PROFILE.class));
			list = query.list();
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	public List<Map> getDataRTMap(String sql ,  List<String> values){
		List<Map> list = null;
		try{
			SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
			int i = 0;
			for(String val :values){
				query.setParameter(i, val);
				i++;
			}
			query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			list = query.list();
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
}
