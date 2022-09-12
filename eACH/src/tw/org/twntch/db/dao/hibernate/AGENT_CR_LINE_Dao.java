package tw.org.twntch.db.dao.hibernate;



import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;

import tw.org.twntch.po.AGENT_CR_LINE;
import tw.org.twntch.po.AGENT_FEE_CODE;

public class AGENT_CR_LINE_Dao extends HibernateEntityDao<AGENT_CR_LINE, Serializable> {
	

	public List<AGENT_CR_LINE> getData(String sql ,  List<String> values){
		List<AGENT_CR_LINE> list = null;
		try{
			SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
			int i = 0;
			for(String val :values){
				query.setParameter(i, val);
				i++;
			}
			query.setResultTransformer(Transformers.aliasToBean(AGENT_CR_LINE.class));
			list = query.list();
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	public List<Map> getDataRetMap(String sql ,  List<String> values){
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
	public List<AGENT_CR_LINE> getDataDemo( List<String> values){
		String hql = " FROM tw.org.twntch.po.AGENT_CR_LINE WHERE SND_COMPANY_ID = ? ";
		List<AGENT_CR_LINE> list = null;
		try{
			Query query = getCurrentSession().createQuery(hql.toString());
			int i = 0;
			for(String val :values){
				query.setParameter(i, val);
				i++;
			}
			list = query.list();
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}

}
