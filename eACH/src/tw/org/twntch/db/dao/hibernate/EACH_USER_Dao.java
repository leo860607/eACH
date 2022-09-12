package tw.org.twntch.db.dao.hibernate;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;

import tw.org.twntch.po.EACH_USER;
import tw.org.twntch.po.EACH_USER_PK;
import tw.org.twntch.util.AutoAddScalar;
import tw.org.twntch.util.zDateHandler;

public class EACH_USER_Dao extends HibernateEntityDao<EACH_USER, Serializable> {
	
	

	public List<EACH_USER> getAllData(String orderSQL){
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT A.USER_COMPANY ||'-'|| B.BGBK_NAME COM_NAME , R.ROLE_ID||'-'||R.ROLE_NAME ROLE_ID ,A.* FROM EACH_USER A ");
		sql.append(" JOIN EACH_ROLE_LIST R ON A.ROLE_ID = R.ROLE_ID  ");
		sql.append(" JOIN BANK_GROUP B  ON A.USER_COMPANY = B.BGBK_ID ");
		sql.append(orderSQL);
		SQLQuery query =  getSession().createSQLQuery(sql.toString());
		AutoAddScalar addScalr = new AutoAddScalar();
		String cols = "COM_NAME,USER_COMPANY,USER_ID,USER_TYPE,USER_STATUS,USER_DESC,USE_IKEY,ROLE_ID,NOLOGIN_EXPIRE_DAY,IP,IDLE_TIMEOUT,LAST_LOGIN_DATE,LAST_LOGIN_IP,CDATE,UDATE ";
		addScalr.addScalar(query, EACH_USER.class, cols.split(","));
//		query.addEntity(EACH_USER.class);
		query.setResultTransformer(Transformers.aliasToBean(EACH_USER.class));
//		query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		List<EACH_USER> list = query.list();
		return list;
	}
	public List<EACH_USER> getData(String sql ,List<String> values ){
		SQLQuery query =  getSession().createSQLQuery(sql.toString());
		int i =0;
		for(String value :values){
			query.setParameter(i, value);
			i++;
		}
		AutoAddScalar addScalr = new AutoAddScalar();
		String cols = "COM_NAME,USER_COMPANY,USER_ID,USER_TYPE,USER_STATUS,USER_DESC,USE_IKEY,ROLE_ID,NOLOGIN_EXPIRE_DAY,IP,IDLE_TIMEOUT,LAST_LOGIN_DATE,LAST_LOGIN_IP,CDATE,UDATE ";
		addScalr.addScalar(query, EACH_USER.class, cols.split(","));
		query.setResultTransformer(Transformers.aliasToBean(EACH_USER.class));
		List list = query.list();
		return list;
	}
	
	
	public List<EACH_USER_PK> getUserCompanyList(){
		List<EACH_USER_PK> list = null;
		String sql = "SELECT DISTINCT USER_COMPANY AS USER_COMPANY FROM EACH_USER";
		try{
			SQLQuery query = getCurrentSession().createSQLQuery(sql);
			query.addScalar("USER_COMPANY", Hibernate.STRING);
			query.setResultTransformer(Transformers.aliasToBean(EACH_USER_PK.class));
			list = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return list;
	}
	public List<EACH_USER_PK> getUserCompanyList(String user_type){
		List<EACH_USER_PK> list = null;
		String sql = "SELECT DISTINCT USER_COMPANY AS USER_COMPANY FROM EACH_USER WHERE USER_TYPE =:user_type";
		try{
			SQLQuery query = getCurrentSession().createSQLQuery(sql);
			query.setParameter("user_type", user_type);
			query.addScalar("USER_COMPANY", Hibernate.STRING);
			query.setResultTransformer(Transformers.aliasToBean(EACH_USER_PK.class));
			list = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return list;
	}
	
	public EACH_USER getUserData(String userId){
		StringBuffer sql = new  StringBuffer();
		sql.append(" SELECT * FROM EACH_USER WHERE USER_ID =:userId ");
		sql.append(" AND USER_STATUS = 'Y' ");
		SQLQuery query =  getCurrentSession().createSQLQuery(sql.toString());
		query.setParameter("userId", userId);
		query.addEntity(EACH_USER.class);
		EACH_USER po = null;
		List<EACH_USER> list = query.list();
		if(list != null && list.size() !=0){
			po = list.get(0);
		}
		return po;
	}
	public List<EACH_USER> getDataByUserId(String userId){
		List<EACH_USER> list = this.find(" FROM tw.org.twntch.po.EACH_USER WHERE USER_ID=? ", userId);
		return list;
	}
	public List<EACH_USER> getDataByComId(String user_comId){
		List<EACH_USER> list = this.find(" FROM tw.org.twntch.po.EACH_USER WHERE USER_COMPANY=? ", user_comId);
		return list;
	}
	public List<EACH_USER> getDataByPK(String userId ,String user_comId){
		List<EACH_USER> list = this.find(" FROM tw.org.twntch.po.EACH_USER WHERE USER_ID =? AND USER_COMPANY = ?", userId ,user_comId);
		return list;
	}
	
	public List<EACH_USER> getAllNotEach(){
		List<EACH_USER> list = null;
		String sql = "SELECT * FROM EACH_USER WHERE USER_COMPANY !='0188888' ";
		try{
			SQLQuery query = getCurrentSession().createSQLQuery(sql);
			query.addEntity(EACH_USER.class);
			list = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return list;
	}
}
