package tw.org.twntch.db.dao.hibernate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;

import tw.org.twntch.po.FEE_CODE;
import tw.org.twntch.po.FEE_CODE_PK;
import tw.org.twntch.util.AutoAddScalar;
import tw.org.twntch.util.CodeUtils;

public class FEE_CODE_Dao extends HibernateEntityDao<FEE_CODE, FEE_CODE_PK> {
	
	public List<Map<String, String>> getDistinctIdJoinTxnName_OrderByFeeId(){
		List<Map<String, String>> list = new ArrayList();
		String sql = "SELECT FC.FEE_ID, COALESCE(TC.TXN_NAME,'') AS TXN_NAME ";
		sql += "FROM (SELECT DISTINCT FEE_ID FROM EACHUSER.FEE_CODE) FC LEFT JOIN TXN_CODE TC ON FC.FEE_ID = TC.TXN_ID ORDER BY FC.FEE_ID";
		try{
			SQLQuery query = getCurrentSession().createSQLQuery(sql);
			query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			list = CodeUtils.escapeList(query.list());
		}catch(HibernateException e){
			e.printStackTrace();
		}
		list = list == null? null : list.size() == 0? null : list;
		return list;
	}
	
	public List<Map<String, String>> getDistinct_OrderByFeeId(){
		List<Map<String, String>> list = new ArrayList();
		String sql = "SELECT DISTINCT FEE_ID FROM FEE_CODE ORDER BY FEE_ID";
		try{
			SQLQuery query = getCurrentSession().createSQLQuery(sql);
			query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			list = CodeUtils.escapeList(query.list());
		}catch(HibernateException e){
			e.printStackTrace();
		}
		list = list == null? null : list.size() == 0? null : list;
		return list;
	}
	
	public List<FEE_CODE> getAll_OrderByFeeId(){
		List list = new ArrayList();
		String sql = "SELECT FC.*, COALESCE(TC.TXN_NAME,'') AS TXN_NAME ";
		sql += "FROM FEE_CODE FC LEFT JOIN TXN_CODE TC ON FC.FEE_ID = TC.TXN_ID ";
		sql += "ORDER BY FEE_ID";
		
		try{
			SQLQuery query = getCurrentSession().createSQLQuery(sql);
			String cols[] = {"FEE_ID","START_DATE","OUT_BANK_FEE","OUT_BANK_FEE_DISC","IN_BANK_FEE","IN_BANK_FEE_DISC","TCH_FEE","TCH_FEE_DISC","SND_BANK_FEE","SND_BANK_FEE_DISC","FEE_DESC","ACTIVE_DESC","CDATE"," UDATE"," TXN_NAME"};
			AutoAddScalar scalar = new AutoAddScalar();
			scalar.addScalar(query, FEE_CODE.class, cols);
			query.setResultTransformer(Transformers.aliasToBean(FEE_CODE.class));
			list = CodeUtils.escapeList(query.list());
		}catch(HibernateException e){
			e.printStackTrace();
		}
		list = list == null? null : list.size() == 0? null : list;
		return list;
	}
	public List<FEE_CODE> getAllData(String orderSQL){
		List list = new ArrayList();
		String sql = "SELECT FC.*, COALESCE(TC.TXN_NAME,'') AS TXN_NAME ";
		sql += "FROM FEE_CODE FC LEFT JOIN TXN_CODE TC ON FC.FEE_ID = TC.TXN_ID ";
		sql += orderSQL;
		
		try{
			SQLQuery query = getCurrentSession().createSQLQuery(sql);
			String cols[] = {"FEE_ID","START_DATE","OUT_BANK_FEE","OUT_BANK_FEE_DISC","IN_BANK_FEE","IN_BANK_FEE_DISC","WO_BANK_FEE","WO_BANK_FEE_DISC","TCH_FEE","TCH_FEE_DISC","SND_BANK_FEE","SND_BANK_FEE_DISC","FEE_DESC","ACTIVE_DESC","CDATE"," UDATE"," TXN_NAME" ,"HANDLECHARGE"};
			AutoAddScalar scalar = new AutoAddScalar();
			scalar.addScalar(query, FEE_CODE.class, cols);
			query.setResultTransformer(Transformers.aliasToBean(FEE_CODE.class));
			list = CodeUtils.escapeList(query.list());
		}catch(HibernateException e){
			e.printStackTrace();
		}
		list = list == null? null : list.size() == 0? null : list;
		return list;
	}
	
	public List<FEE_CODE> getByPK(String feeId, String startDate){
		List<FEE_CODE> list = new ArrayList();
		String sql = "SELECT FC.*, COALESCE(TC.TXN_NAME, '') AS TXN_NAME FROM FEE_CODE FC LEFT JOIN TXN_CODE TC ON FC.FEE_ID = TC.TXN_ID WHERE FC.FEE_ID = '" + feeId + "' AND FC.START_DATE = '" + startDate + "' ORDER BY START_DATE";
		try{
			SQLQuery query = getCurrentSession().createSQLQuery(sql);
			String cols[] = {"FEE_ID","START_DATE","OUT_BANK_FEE","OUT_BANK_FEE_DISC","IN_BANK_FEE","IN_BANK_FEE_DISC","WO_BANK_FEE","WO_BANK_FEE_DISC","TCH_FEE","TCH_FEE_DISC","SND_BANK_FEE","SND_BANK_FEE_DISC","WO_BANK_FEE","WO_BANK_FEE_DISC","FEE_DESC","ACTIVE_DESC","CDATE"," UDATE"," TXN_NAME","HANDLECHARGE"};
			AutoAddScalar scalar = new AutoAddScalar();
			scalar.addScalar(query, FEE_CODE.class, cols);
			query.setResultTransformer(Transformers.aliasToBean(FEE_CODE.class));
			list = CodeUtils.escapeList(query.list());
		}catch(HibernateException e){
			e.printStackTrace();
		}
		list = list == null? null : list.size() == 0? null : list;
		return list;
	}
	
	public List<FEE_CODE> getByFeeId(String feeId){
		feeId=CodeUtils.escape(feeId);
		List<FEE_CODE> list = new ArrayList();
		String sql = "SELECT FC.*, COALESCE(TC.TXN_NAME, '') AS TXN_NAME FROM FEE_CODE FC LEFT JOIN TXN_CODE TC ON FC.FEE_ID = TC.TXN_ID WHERE FC.FEE_ID = :feeId ORDER BY START_DATE";
		try{
			SQLQuery query = getCurrentSession().createSQLQuery(sql);
			query.setParameter("feeId", feeId);
			String cols[] = {"FEE_ID","START_DATE","OUT_BANK_FEE","OUT_BANK_FEE_DISC","IN_BANK_FEE","IN_BANK_FEE_DISC","WO_BANK_FEE","WO_BANK_FEE_DISC","TCH_FEE","TCH_FEE_DISC","SND_BANK_FEE","SND_BANK_FEE_DISC","WO_BANK_FEE","WO_BANK_FEE_DISC","FEE_DESC","ACTIVE_DESC","CDATE"," UDATE"," TXN_NAME","HANDLECHARGE"};
			AutoAddScalar scalar = new AutoAddScalar();
			scalar.addScalar(query, FEE_CODE.class, cols);
			query.setResultTransformer(Transformers.aliasToBean(FEE_CODE.class));
			list = CodeUtils.escapeList(query.list());
		}catch(HibernateException e){
			e.printStackTrace();
		}
		list = list == null? null : list.size() == 0? null : list;
		return list;
	}
	
	public List<FEE_CODE> getByStartDate(String startDate){
		List<FEE_CODE> list = new ArrayList();
		String sql = "SELECT FC.*, COALESCE(TC.TXN_NAME, '') AS TXN_NAME FROM FEE_CODE FC LEFT JOIN TXN_CODE TC ON FC.FEE_ID = TC.TXN_ID WHERE FC.START_DATE = '" + startDate + "' ORDER BY FC.FEE_ID";
		try{
			SQLQuery query = getCurrentSession().createSQLQuery(sql);
			String cols[] = {"FEE_ID","START_DATE","OUT_BANK_FEE","OUT_BANK_FEE_DISC","IN_BANK_FEE","IN_BANK_FEE_DISC","WO_BANK_FEE","WO_BANK_FEE_DISC","TCH_FEE","TCH_FEE_DISC","SND_BANK_FEE","SND_BANK_FEE_DISC","FEE_DESC","ACTIVE_DESC","CDATE"," UDATE"," TXN_NAME","HANDLECHARGE"};
			AutoAddScalar scalar = new AutoAddScalar();
			scalar.addScalar(query, FEE_CODE.class, cols);
			query.setResultTransformer(Transformers.aliasToBean(FEE_CODE.class));
			list = CodeUtils.escapeList(query.list());
		}catch(HibernateException e){
			e.printStackTrace();
		}
		list = list == null? null : list.size() == 0? null : list;
		return list;
	}
	
	public List<FEE_CODE> getUnselectedFeeList(String txnId){
		List<FEE_CODE> list = null;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM FEE_CODE FC WHERE FEE_ID NOT IN (SELECT FEE_ID FROM TXN_FEE_MAPPING WHERE TXN_ID = :txnId)");
		SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
		query.setParameter("txnId", txnId);
		query.addEntity(FEE_CODE.class);
		try{
			list = CodeUtils.escapeList(query.list());
		}catch(Exception e){
			e.printStackTrace();
		}
		list =  list == null? null : list.size() == 0? null : list;
		return list;
	}
	
	public List<FEE_CODE> getSelectedFeeList(String txnId){
		List<FEE_CODE> list = null;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM FEE_CODE FC WHERE FEE_ID IN (SELECT FEE_ID FROM TXN_FEE_MAPPING WHERE TXN_ID = :txnId)");
		SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
		query.setParameter("txnId", txnId);
		query.addEntity(FEE_CODE.class);
		try{
			list = CodeUtils.escapeList(query.list());
		}catch(Exception e){
			e.printStackTrace();
		}
		list =  list == null? null : list.size() == 0? null : list;
		return list;
	}
	
	public FEE_CODE getLastestByFeeIdAndMappingStartDate(String feeId, String mappingStartDate){
		FEE_CODE po = null;
		String sql = "SELECT * FROM FEE_CODE WHERE FEE_ID = :feeId AND START_DATE = ( ";
		sql += "SELECT MAX(START_DATE) FROM FEE_CODE WHERE FEE_ID = :feeId AND START_DATE <= :mappingStartDate)";
		
		SQLQuery query = getCurrentSession().createSQLQuery(sql);
		query.setParameter("feeId", feeId);
		query.setParameter("mappingStartDate", mappingStartDate);
		query.addEntity(FEE_CODE.class);
		List<FEE_CODE> list = CodeUtils.escapeList(query.list());
		list = list == null? null : list.size()==0?null :list;
		if(list != null){
			po = list.get(0);
		}
		return po;
	}
	
	public String checkFeeCodeType(String fee_id) {
		List<FEE_CODE> list = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT ");
		sql.append("  (CASE WHEN HANDLECHARGE = 0  THEN 'A' WHEN HANDLECHARGE>0 THEN 'B'  END) AS FEE_TYPE ,START_DATE ");
		sql.append(" FROM FEE_CODE WHERE ");
		sql.append(" FEE_ID =:fee_id ");
		sql.append(" ORDER BY START_DATE DESC fetch first 1 rows only ");
		SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
		query.setParameter("fee_id", fee_id);
		query.setResultTransformer(Transformers.aliasToBean(FEE_CODE.class));
		try{
			list = CodeUtils.escapeList(query.list());
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return (list==null||list.size()==0)?"":list.get(0).getFEE_TYPE();
	}
	
	public String checkFeeCodeType2(String fee_id) {
		List<FEE_CODE> list = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT ");
		sql.append("  (CASE WHEN HANDLECHARGE = 0  THEN 'A' WHEN HANDLECHARGE>0 THEN 'B'  END) AS FEE_TYPE ,START_DATE ");
		sql.append(" FROM FEE_CODE WHERE ");
		sql.append(" FEE_ID =:fee_id ");
		sql.append(" AND TRANS_DATE(START_DATE,'W','') < (SELECT (CASE DATEMODE WHEN '0' THEN THISDATE WHEN '1' THEN NEXTDATE ELSE THISDATE END) AS BUSINESS_DATE  FROM EACHSYSSTATUSTAB) ");
		sql.append(" ORDER BY START_DATE DESC fetch first 1 rows only ");
		SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
		query.setParameter("fee_id", fee_id);
		query.setResultTransformer(Transformers.aliasToBean(FEE_CODE.class));
		try{
			list = CodeUtils.escapeList(query.list());
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return (list==null||list.size()==0)?"":list.get(0).getFEE_TYPE();
	}
	
}
