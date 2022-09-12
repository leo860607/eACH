package tw.org.twntch.db.dao.hibernate;

import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;

import tw.org.twntch.po.SD_COMPANY_PROFILE;
import tw.org.twntch.po.SD_COMPANY_PROFILE_PK;
import tw.org.twntch.util.AutoAddScalar;
import tw.org.twntch.util.StrUtils;

public class SD_COMPANY_PROFILE_Dao extends HibernateEntityDao<SD_COMPANY_PROFILE, SD_COMPANY_PROFILE_PK>{
	
	public List<Map<String, String>> getSdBgbkIdList(){
		List<Map<String, String>> list = null;
		String sql = "SELECT DISTINCT COALESCE(BR.BGBK_ID, SD.SND_BRBK_ID) AS BGBK_ID, COALESCE(BG.BGBK_NAME,'') AS BGBK_NAME " +
		"FROM SD_COMPANY_PROFILE SD LEFT JOIN BANK_BRANCH BR ON SD.SND_BRBK_ID = BR.BRBK_ID " +
		"LEFT JOIN BANK_GROUP BG ON BR.BGBK_ID = BG.BGBK_ID ORDER BY BGBK_ID ";
		try{
			SQLQuery query = getCurrentSession().createSQLQuery(sql);
			query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			list = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return list;	
	}

	public List<SD_COMPANY_PROFILE> getData(String condition, List<String> params){
		List<SD_COMPANY_PROFILE> list = null;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT A.*, B.BRBK_NAME ");
		if(condition.equals("")){
			sql.append("FROM SD_COMPANY_PROFILE A LEFT JOIN BANK_BRANCH B ON A.SND_BRBK_ID = B.BRBK_ID ");
		}else{
			sql.append("FROM SD_COMPANY_PROFILE A LEFT JOIN BANK_BRANCH B ON A.SND_BRBK_ID = B.BRBK_ID ");
		}
		
		if(StrUtils.isNotEmpty(condition)){
			sql.append("WHERE " + condition);
		}
		sql.append(" ORDER BY COMPANY_ID ");
		
		System.out.println("EDIT get Data sql :"+sql.toString());
		try{
			SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
			for(int i = 0; i < params.size(); i++){
				query.setParameter(i, params.get(i));
			}
			String ary[] = {"COMPANY_ID","TXN_ID","SND_BRBK_ID","COMPANY_ABBR_NAME","COMPANY_NAME","CONTACT_INFO","START_DATE","DISPATCH_DATE","USER_NO","CASE_NO","STOP_DATE","CDATE","UDATE","BRBK_NAME","ACTIVE_DATE","FEE_TYPE","FEE_TYPE_ACTIVE_DATE"};
			AutoAddScalar autoScalar = new AutoAddScalar();
			autoScalar.addScalar(query, SD_COMPANY_PROFILE.class, ary);
			query.setResultTransformer(Transformers.aliasToBean(SD_COMPANY_PROFILE.class));
			list = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return list;
	}
	
	public List<SD_COMPANY_PROFILE> getData(String condition, List<String> params , String orderSQL){
		List<SD_COMPANY_PROFILE> list = null;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT A.*, B.BRBK_NAME ");
		if(condition.equals("")){
			sql.append("FROM SD_COMPANY_PROFILE A LEFT JOIN BANK_BRANCH B ON A.SND_BRBK_ID = B.BRBK_ID ");
		}else{
			sql.append("FROM SD_COMPANY_PROFILE A LEFT JOIN BANK_BRANCH B ON A.SND_BRBK_ID = B.BRBK_ID ");
		}
		
		if(StrUtils.isNotEmpty(condition)){
			sql.append("WHERE " + condition);
		}
		sql.append(orderSQL);
		try{
			SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
			for(int i = 0; i < params.size(); i++){
				query.setParameter(i, params.get(i));
			}
			String ary[] = {"COMPANY_ID","TXN_ID","SND_BRBK_ID","COMPANY_ABBR_NAME","COMPANY_NAME","CONTACT_INFO","START_DATE","DISPATCH_DATE","USER_NO","CASE_NO","STOP_DATE","CDATE","UDATE","BRBK_NAME","ACTIVE_DATE","FEE_TYPE","FEE_TYPE_ACTIVE_DATE"};
			AutoAddScalar autoScalar = new AutoAddScalar();
			autoScalar.addScalar(query, SD_COMPANY_PROFILE.class, ary);
			query.setResultTransformer(Transformers.aliasToBean(SD_COMPANY_PROFILE.class));
			list = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return list;
	}
	
	public List<SD_COMPANY_PROFILE> checkCOMPANY_ID(String companyId){
		List<SD_COMPANY_PROFILE> list = null;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT COMPANY_NAME,COMPANY_ABBR_NAME "); 
		sql.append("FROM SD_COMPANY_PROFILE WHERE COMPANY_ID='"+companyId+"' "); 
		sql.append("FETCH FIRST 1 ROWS ONLY"); 
		
		try{
		SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
		String cols = "COMPANY_NAME,COMPANY_ABBR_NAME";
		AutoAddScalar addscalar = new AutoAddScalar();
		addscalar.addScalar(query, SD_COMPANY_PROFILE.class, cols.split(","));
		query.setResultTransformer(Transformers.aliasToBean(SD_COMPANY_PROFILE.class));
		list = query.list();
		if(list.toString().equals("[]")){
			list = checkCOMPANY_ID_SC(companyId);
		}
		
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return list;
	}
	
	public List<SD_COMPANY_PROFILE> checkCOMPANY_ID_SC(String companyId){
		List<SD_COMPANY_PROFILE> list = null;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT COMPANY_NAME,COMPANY_ABBR_NAME "); 
		sql.append("FROM SC_COMPANY_PROFILE WHERE COMPANY_ID='"+companyId+"' "); 
		sql.append("FETCH FIRST 1 ROWS ONLY"); 
		
		try{
		SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
		String cols = "COMPANY_NAME,COMPANY_ABBR_NAME";
		AutoAddScalar addscalar = new AutoAddScalar();
		addscalar.addScalar(query, SD_COMPANY_PROFILE.class, cols.split(","));
		query.setResultTransformer(Transformers.aliasToBean(SD_COMPANY_PROFILE.class));
		list = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return list;
	}
	
	public String checkCOMPANY_NAME(String companyId){
		List list = null;
		String result="";
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT COUNT(DISTINCT COMPANY_NAME) "); 
		sql.append("FROM SD_COMPANY_PROFILE WHERE COMPANY_ID='"+companyId+"' "); 

		try{
		SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
		list = query.list();
		result = list.get(0).toString();
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return result;
	}
	
	public String checkAmount(String companyId){
		List list = null;
		String result="";
		String total="";
		int sc_count = checkAmount_SC(companyId);
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT COUNT(*) "); 
		sql.append("FROM SD_COMPANY_PROFILE WHERE COMPANY_ID='"+companyId+"' "); 

		try{
		SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
		list = query.list();
		result = list.get(0).toString();
		
		total = Integer.toString(Integer.parseInt(result) + sc_count);
		
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return total;
	}
	
	public int checkAmount_SC(String companyId){
		List list = null;
		String result="";
		int sc_count=0;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT COUNT(*) "); 
		sql.append("FROM SC_COMPANY_PROFILE WHERE COMPANY_ID='"+companyId+"' "); 

		try{
		SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
		list = query.list();
        result = list.get(0).toString();
		
		sc_count = Integer.parseInt(result);
		
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return sc_count;
	}
	
	public boolean updateNameById(String companyId,String name,String abbr_name){
		Session session = getSessionFactory().openSession();
		boolean result = false;
		boolean sc_result = false;
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE SD_COMPANY_PROFILE SET COMPANY_NAME='"+name+"' , COMPANY_ABBR_NAME='"+abbr_name+"' "); 
		sql.append("WHERE COMPANY_ID='"+companyId+"' "); 

		try{
		SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
		query.executeUpdate();
		session.beginTransaction().commit();
		result = true;
		sc_result = updateNameById_SC(companyId,name,abbr_name);
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return result;
	}

	public boolean updateNameById_SC(String companyId,String name,String abbr_name){
		Session session = getSessionFactory().openSession();
		boolean result = false;
		
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE SC_COMPANY_PROFILE SET COMPANY_NAME='"+name+"' , COMPANY_ABBR_NAME='"+abbr_name+"' "); 
		sql.append("WHERE COMPANY_ID='"+companyId+"' "); 

		try{
		SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
		query.executeUpdate();
		session.beginTransaction().commit();
		result = true;
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return result;
	}
	
	public String checkSDAmount(String companyId,String txn_id, String fee_type , String snd_brbk_id){
		List list = null;
		String result="";
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT COUNT(*) "); 
		sql.append("FROM SD_COMPANY_PROFILE WHERE COMPANY_ID='"+companyId+"' AND TXN_ID='"+ txn_id +"' AND FEE_TYPE !='"+fee_type+"' AND SND_BRBK_ID !='"+snd_brbk_id+"' "); 

		try{
		SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
		list = query.list();
		result = list.get(0).toString();
		
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return result;
	}
	
	public List<SD_COMPANY_PROFILE> updateSDList(String companyId,String txn_id, String fee_type ,String snd_brbk_id){
		List list = null;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * "); 
		sql.append("FROM SD_COMPANY_PROFILE WHERE COMPANY_ID='"+companyId+"' AND TXN_ID='"+ txn_id +"' AND FEE_TYPE !='"+fee_type+"' AND SND_BRBK_ID !='"+snd_brbk_id+"' "); 

		try{
		SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
		String ary[] = {"COMPANY_ID","TXN_ID","SND_BRBK_ID","COMPANY_ABBR_NAME","COMPANY_NAME","CONTACT_INFO","START_DATE","DISPATCH_DATE","USER_NO","CASE_NO","STOP_DATE","CDATE","UDATE","ACTIVE_DATE","FEE_TYPE","FEE_TYPE_ACTIVE_DATE"};
		AutoAddScalar autoScalar = new AutoAddScalar();
		autoScalar.addScalar(query, SD_COMPANY_PROFILE.class, ary);
		query.setResultTransformer(Transformers.aliasToBean(SD_COMPANY_PROFILE.class));
		list = query.list();
		
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return list;
	}
	
	public List<SD_COMPANY_PROFILE> getData(String sql, List<String> params ,  String[] cols, Class targetClass) {
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
	
	public List<Map<String, String>> getAll_ListMap(){
		List<Map<String, String>> list = null;
		String sql = "SELECT * FROM SD_COMPANY_PROFILE ORDER BY COMPANY_ID";
		try{
			SQLQuery query = getCurrentSession().createSQLQuery(sql);
			query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			list = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return list;	
	}
//	public List<SD_COMPANY_PROFILE> getDataByPK(String sql,Object...values){
//		Query query = getSession().createQuery(sql.toString());
//		for (int i = 0; i < values.length; i++) {
//			query.setParameter(i, values[i]);
//		}
//		List<SD_COMPANY_PROFILE> list = query.list();
//		list = list.size()==0?null :list;
//		return list;
//	}
}
