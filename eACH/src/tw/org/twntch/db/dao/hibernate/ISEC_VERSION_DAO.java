package tw.org.twntch.db.dao.hibernate;

import java.io.Serializable;
import java.util.List;

import org.apache.poi.ss.formula.functions.T;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;

import tw.org.twntch.po.SYS_PARA;

public class ISEC_VERSION_DAO extends HibernateEntityDao<SYS_PARA, Serializable> {

	public SYS_PARA getISEC(){
		System.out.println("Start ISEC_DAO");
		StringBuffer sql =new StringBuffer();
		sql.append("SELECT * FROM SYS_PARA ");		
		SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
		System.out.println("query>>"+query);
//		query.setResultTransformer(Transformers.aliasToBean(SYS_PARA.class));
		List<SYS_PARA> list = query.list();
		System.out.println("ISEC_VERSION_DAO.list>>"+list);
		
		SYS_PARA po = null;
		
		if(list != null && list.size() > 0){
			po = list.get(0);
			System.out.println("DAO_PO>>"+po);
		}
		return po;		
	}

	public void aop_save(T o ) {
		getHibernateTemplate().saveOrUpdate(o);
	}
	
	public List<SYS_PARA> getversion() {
		System.out.println("isec_ver_init...");
		StringBuffer sql =new StringBuffer();
		sql.append("SELECT ISEC_VERSION FROM SYS_PARA ");		
		SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
		System.out.println("query>>"+query);
//		query.setResultTransformer(Transformers.aliasToBean(SYS_PARA.class));
		List<SYS_PARA> list = query.list();
		return list;		
	}	
	
}
