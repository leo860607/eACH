package tw.org.twntch.db.dao.hibernate;


import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;

import tw.org.twntch.po.EACH_FUNC_LIST;
import tw.org.twntch.po.EACH_ROLE_FUNC;
import tw.org.twntch.po.EACH_ROLE_FUNC_PK;

public class EACH_ROLE_FUNC_Dao extends HibernateEntityDao<EACH_ROLE_FUNC, EACH_ROLE_FUNC_PK> {
	
	//依群組找出可用的FUNC_ID清單(作業模組)
	public List<EACH_FUNC_LIST> getFuncListByRoleId(String roleId, String userType){
		List<EACH_FUNC_LIST> list = null;
		String sql = "SELECT FL.FUNC_ID, FL.FUNC_NAME , value(FL.FUNC_NAME_BK , '') FUNC_NAME_BK , FL.FUNC_URL, FL.FUNC_TYPE, FL.PROXY_FUNC ";
		sql += "FROM EACH_ROLE_FUNC RF JOIN EACH_FUNC_LIST FL ON RF.FUNC_ID = FL.FUNC_ID ";
		sql += "WHERE RF.ROLE_ID = :roleId AND FL.IS_USED = 'Y' AND FL.FUNC_TYPE = '1' AND ";
		//20141210 HUANGPU David說，應依照使用者類型挑選功能
		//票交所
		if(userType.equals("A")){
			sql += "FL.TCH_FUNC = 'Y'";
		//銀行端
		}else if(userType.equals("B")){
			sql += "FL.BANK_FUNC = 'Y'";
		//發動者
		}else if(userType.equals("C")){
			sql += "FL.COMPANY_FUNC = 'Y'";
		}
		sql += "ORDER BY FUNC_ID";
		System.out.println(sql);
		try{
			SQLQuery query = getCurrentSession().createSQLQuery(sql);
			query.setParameter("roleId", roleId);
			query.addScalar("FUNC_ID", Hibernate.STRING)
			.addScalar("FUNC_NAME", Hibernate.STRING)
			.addScalar("FUNC_URL", Hibernate.STRING)
			.addScalar("FUNC_TYPE", Hibernate.STRING)
			.addScalar("PROXY_FUNC", Hibernate.STRING)
			.addScalar("FUNC_NAME_BK", Hibernate.STRING)
			.setResultTransformer(Transformers.aliasToBean(EACH_FUNC_LIST.class));
			list = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return list;
	}
}
