package tw.org.twntch.db.dao.hibernate;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;

import tw.org.twntch.po.AGENT_FEE_CODE;
import tw.org.twntch.po.AGENT_FEE_CODE_PK;

public class AGENT_FEE_CODE_Dao extends HibernateEntityDao<AGENT_FEE_CODE, AGENT_FEE_CODE_PK> {
	
	public List<AGENT_FEE_CODE> getFeeIdList(){
		List<AGENT_FEE_CODE> list = null;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT DISTINCT FEE_ID, (SELECT TXN_NAME FROM TXN_CODE WHERE TXN_ID = FEE_ID) AS FEE_NAME ");
		sql.append("FROM AGENT_FEE_CODE ");
		sql.append("ORDER BY FEE_ID ");
		try{
			SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
			query.setResultTransformer(Transformers.aliasToBean(AGENT_FEE_CODE.class));
			query.addScalar("FEE_ID", Hibernate.STRING);
			query.addScalar("FEE_NAME", Hibernate.STRING);
			list = query.list();
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	
	public List<AGENT_FEE_CODE> getData(String sql ,  List<String> values){
		List<AGENT_FEE_CODE> list = null;
		try{
			SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
			int i = 0;
			for(String val :values){
				query.setParameter(i, val);
				i++;
			}
			query.setResultTransformer(Transformers.aliasToBean(AGENT_FEE_CODE.class));
//			query.addScalar("FEE_ID", Hibernate.STRING);
//			query.addScalar("FEE_NAME", Hibernate.STRING);
			list = query.list();
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
}