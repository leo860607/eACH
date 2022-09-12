package tw.org.twntch.db.dao.hibernate;

import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;

import tw.org.twntch.po.WO_COMPANY_PROFILE;
import tw.org.twntch.po.WO_COMPANY_PROFILE;
import tw.org.twntch.po.WO_COMPANY_PROFILE_PK;
import tw.org.twntch.util.AutoAddScalar;

public class WO_COMPANY_PROFILE_Dao extends HibernateEntityDao<WO_COMPANY_PROFILE, WO_COMPANY_PROFILE_PK> {
	
	

	public WO_COMPANY_PROFILE getWO_CompanyDataByCompanyId(String companyId){
		WO_COMPANY_PROFILE po = null;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM ( ");
		sql.append("	SELECT WO_COMPANY_ID COMPANY_ID , WO_COMPANY_ABBR_NAME COMPANY_ABBR_NAME, WO_COMPANY_NAME COMPANY_NAME FROM WO_COMPANY_PROFILE WHERE WO_COMPANY_ID = ? ");
//		sql.append("	UNION ");
//		sql.append("	SELECT COMPANY_ID, COMPANY_ABBR_NAME, COMPANY_NAME FROM SD_COMPANY_PROFILE WHERE COMPANY_ID = ? ");
		sql.append("	FETCH FIRST 1 ROWS ONLY ");
		sql.append(") ");
		
		try {
			SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
			query.setParameter(0, companyId);
//			query.setParameter(1, companyId);
			String[] cols = {"COMPANY_ID", "COMPANY_ABBR_NAME", "COMPANY_NAME"};
			AutoAddScalar addScalar = new AutoAddScalar();
			addScalar.addScalar(query, WO_COMPANY_PROFILE.class, cols);
			query.setResultTransformer(Transformers.aliasToBean(WO_COMPANY_PROFILE.class));
			List<WO_COMPANY_PROFILE> list = query.list();
			if(list != null && list.size() > 0){
				po = list.get(0);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.debug("getCompanyDataByCompanyId.Exception>>"+e);
		}
		return po;
	}
	public WO_COMPANY_PROFILE getCompanyDataByCompanyId(String companyId){
		WO_COMPANY_PROFILE po = null;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM ( ");
		sql.append("	SELECT COMPANY_ID, COMPANY_ABBR_NAME, COMPANY_NAME FROM SC_COMPANY_PROFILE WHERE COMPANY_ID = ? ");
		sql.append("	UNION ");
		sql.append("	SELECT COMPANY_ID, COMPANY_ABBR_NAME, COMPANY_NAME FROM SD_COMPANY_PROFILE WHERE COMPANY_ID = ? ");
		sql.append("	FETCH FIRST 1 ROWS ONLY ");
		sql.append(") ");
		
		try {
			SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
			query.setParameter(0, companyId);
			query.setParameter(1, companyId);
			String[] cols = {"COMPANY_ID", "COMPANY_ABBR_NAME", "COMPANY_NAME"};
			AutoAddScalar addScalar = new AutoAddScalar();
			addScalar.addScalar(query, WO_COMPANY_PROFILE.class, cols);
			query.setResultTransformer(Transformers.aliasToBean(WO_COMPANY_PROFILE.class));
			List<WO_COMPANY_PROFILE> list = query.list();
			if(list != null && list.size() > 0){
				po = list.get(0);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.debug("getCompanyDataByCompanyId.Exception>>"+e);
		}
		return po;
	}
	
	
	public List<WO_COMPANY_PROFILE> getData(String sql ,  List<String> values){
		List<WO_COMPANY_PROFILE> list = null;
		try{
			SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
			int i = 0;
			for(String val :values){
				query.setParameter(i, val);
				i++;
			}
			query.setResultTransformer(Transformers.aliasToBean(WO_COMPANY_PROFILE.class));
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
