package tw.org.twntch.db.dao.hibernate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;

import tw.org.twntch.po.AGENT_SEND_PROFILE;
import tw.org.twntch.po.CHG_SC_PROFILE;
import tw.org.twntch.po.PI_COMPANY_PROFILE;
import tw.org.twntch.po.PI_COMPANY_PROFILE_PK;
import tw.org.twntch.po.PI_SND_PROFILE;
import tw.org.twntch.po.PI_SND_PROFILE_PK;
import tw.org.twntch.po.SC_COMPANY_PROFILE;
import tw.org.twntch.po.WO_COMPANY_PROFILE;
import tw.org.twntch.util.AutoAddScalar;
import tw.org.twntch.util.StrUtils;

public class PI_SND_PROFILE_Dao extends HibernateEntityDao<PI_SND_PROFILE, PI_SND_PROFILE_PK> {
	
	
	public List<PI_SND_PROFILE> getData(String sql ,  List<String> values){
		List<PI_SND_PROFILE> list = null;
		try{
			SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
			int i = 0;
			for(String val :values){
				query.setParameter(i, val);
				i++;
			}
			query.setResultTransformer(Transformers.aliasToBean(PI_SND_PROFILE.class));
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
			if(values !=null){
				for(String val :values){
					query.setParameter(i, val);
					i++;
				}
			}
			query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			list = query.list();
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
}
