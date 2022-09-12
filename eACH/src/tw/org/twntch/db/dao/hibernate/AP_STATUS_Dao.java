package tw.org.twntch.db.dao.hibernate;

import java.io.Serializable;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;

import tw.org.twntch.bean.BANK_STATUS;
import tw.org.twntch.po.BANKAPSTATUSTAB;
import tw.org.twntch.po.BANKSYSSTATUSTAB;
import tw.org.twntch.util.AutoAddScalar;
import tw.org.twntch.util.zDateHandler;

public class AP_STATUS_Dao extends HibernateEntityDao<BANK_STATUS, Serializable> {
	public List<BANK_STATUS> getData(){
		List<BANK_STATUS> list = null;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT AP.BGBK_ID, AP.APID, AP.MBAPSTATUS, SYS.MBSYSSTATUS, ");
		sql.append("CASE B.IS_ACTIVE WHEN 'N' THEN '停用' ELSE (");
		sql.append("	CASE AP.MBAPSTATUS ");
		sql.append("	WHEN '1' THEN '簽到' ");
		sql.append("	WHEN '2' THEN '暫時簽退' ");
		sql.append("	WHEN '9' THEN '簽退' END ");
		sql.append(") END AS MBAPSTATUSNAME, ");
		sql.append("CASE B.IS_ACTIVE WHEN 'N' THEN '停用' ELSE ( ");
		sql.append("	CASE SYS.MBSYSSTATUS ");
		sql.append("	WHEN '1' THEN '開機' ");
		sql.append("	WHEN '2' THEN '押碼基碼同步' ");
		sql.append("	WHEN '3' THEN '訊息通知' ");
		sql.append("	WHEN '9' THEN '關機' END ");
		sql.append(") END AS MBSYSSTATUSNAME ");
		sql.append("FROM BANKAPSTATUSTAB AP JOIN BANKSYSSTATUSTAB SYS ON AP.BGBK_ID = SYS.BGBK_ID ");
		sql.append("LEFT JOIN ( ");
		sql.append("	SELECT BGBK_ID, ");
//		2015 edit by hugo 日期不可寫死 否則啟用日期大於01040224 顯示訊息會異常 改用帶入系統日
//		sql.append("	(CASE WHEN '01040224' BETWEEN ");
//		sql.append("	(CASE WHEN VARCHAR_FORMAT(CURRENT TIMESTAMP, 'YYYYMMDD') BETWEEN ");
		sql.append("	(CASE WHEN '"+zDateHandler.getTWDate()+"' BETWEEN ");
		sql.append("	(CASE LENGTH(RTRIM(COALESCE(ACTIVE_DATE,''))) WHEN 0 THEN '99999999' ELSE ACTIVE_DATE END) AND "); 
		sql.append("	(CASE LENGTH(RTRIM(COALESCE(STOP_DATE,''))) WHEN 0 THEN '99999999' ELSE STOP_DATE END) "); 
		sql.append("	THEN 'Y' ELSE 'N' END) AS IS_ACTIVE ");
		sql.append("	FROM EACHUSER.BANK_GROUP ");
		sql.append(") AS B ON AP.BGBK_ID = B.BGBK_ID ");
		sql.append("ORDER BY AP.BGBK_ID ");
		
		try{
			SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
			String cols = "BGBK_ID,APID,MBAPSTATUS,MBSYSSTATUS,MBAPSTATUSNAME,MBSYSSTATUSNAME";
			AutoAddScalar addscalar = new AutoAddScalar();
			addscalar.addScalar(query, BANK_STATUS.class, cols.split(","));
			query.setResultTransformer(Transformers.aliasToBean(BANK_STATUS.class));
			list = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return list;
	}
	public List<BANK_STATUS> getData(String orderSQL){
		List<BANK_STATUS> list = null;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT AP.BGBK_ID, AP.APID, AP.MBAPSTATUS, SYS.MBSYSSTATUS, ");
		sql.append("CASE B.IS_ACTIVE WHEN 'N' THEN '停用' ELSE (");
		sql.append("	CASE AP.MBAPSTATUS ");
		sql.append("	WHEN '1' THEN '簽到' ");
		sql.append("	WHEN '2' THEN '暫時簽退' ");
		sql.append("	WHEN '9' THEN '簽退' END ");
		sql.append(") END AS MBAPSTATUSNAME, ");
		sql.append("CASE B.IS_ACTIVE WHEN 'N' THEN '停用' ELSE ( ");
		sql.append("	CASE SYS.MBSYSSTATUS ");
		sql.append("	WHEN '1' THEN '開機' ");
		sql.append("	WHEN '2' THEN '押碼基碼同步' ");
		sql.append("	WHEN '3' THEN '訊息通知' ");
		sql.append("	WHEN '9' THEN '關機' END ");
		sql.append(") END AS MBSYSSTATUSNAME ");
		sql.append("FROM BANKAPSTATUSTAB AP JOIN BANKSYSSTATUSTAB SYS ON AP.BGBK_ID = SYS.BGBK_ID ");
		sql.append("LEFT JOIN ( ");
//		20160118  edit by hugo  req by UAT-20160114-02
//		sql.append("	SELECT BGBK_ID, ");
		sql.append("	SELECT BGBK_ID ,IS_EACH");
//		2015 edit by hugo 日期不可寫死 否則啟用日期大於01040224 顯示訊息會異常 改用帶入系統日
//		sql.append("	(CASE WHEN '01040224' BETWEEN ");
//		sql.append("	(CASE WHEN VARCHAR_FORMAT(CURRENT TIMESTAMP, 'YYYYMMDD') BETWEEN ");
		sql.append("   ,(CASE WHEN '"+zDateHandler.getTWDate()+"' BETWEEN ");
		sql.append("	(CASE LENGTH(RTRIM(COALESCE(ACTIVE_DATE,''))) WHEN 0 THEN '99999999' ELSE ACTIVE_DATE END) AND "); 
		sql.append("	(CASE LENGTH(RTRIM(COALESCE(STOP_DATE,''))) WHEN 0 THEN '99999999' ELSE STOP_DATE END) "); 
		sql.append("	THEN 'Y' ELSE 'N' END) AS IS_ACTIVE ");
		sql.append("	FROM EACHUSER.BANK_GROUP ");
		sql.append(") AS B ON AP.BGBK_ID = B.BGBK_ID ");
//		20160118  add by hugo  req by UAT-20160114-02
		sql.append(" WHERE COALESCE(B.IS_EACH , '') <>'' ");
		sql.append(orderSQL);
		System.out.println("getData.sql>>"+sql);
		try{
			SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
			String cols = "BGBK_ID,APID,MBAPSTATUS,MBSYSSTATUS,MBAPSTATUSNAME,MBSYSSTATUSNAME";
			AutoAddScalar addscalar = new AutoAddScalar();
			addscalar.addScalar(query, BANK_STATUS.class, cols.split(","));
			query.setResultTransformer(Transformers.aliasToBean(BANK_STATUS.class));
			list = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return list;
	}
	
	public List<BANK_STATUS> getData(String bgbkId, String apId){
		List<BANK_STATUS> list = null;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT AP.BGBK_ID, AP.APID, AP.MBAPSTATUS, SYS.MBSYSSTATUS, ");
		sql.append("CASE B.IS_ACTIVE WHEN 'N' THEN '停用' ELSE (");
		sql.append("	CASE AP.MBAPSTATUS ");
		sql.append("	WHEN '1' THEN '簽到' ");
		sql.append("	WHEN '2' THEN '暫時簽退' ");
		sql.append("	WHEN '9' THEN '簽退' END ");
		sql.append(") END AS MBAPSTATUSNAME, ");
		sql.append("CASE B.IS_ACTIVE WHEN 'N' THEN '停用' ELSE ( ");
		sql.append("	CASE SYS.MBSYSSTATUS ");
		sql.append("	WHEN '1' THEN '開機' ");
		sql.append("	WHEN '2' THEN '押碼基碼同步' ");
		sql.append("	WHEN '3' THEN '訊息通知' ");
		sql.append("	WHEN '9' THEN '關機' END ");
		sql.append(") END AS MBSYSSTATUSNAME ");
		sql.append("FROM BANKAPSTATUSTAB AP JOIN BANKSYSSTATUSTAB SYS ON AP.BGBK_ID = SYS.BGBK_ID ");
		sql.append("LEFT JOIN ( ");
		sql.append("	SELECT BGBK_ID, ");
//		2015 edit by hugo 日期不可寫死 否則啟用日期大於01040224 顯示訊息會異常 改用系統日
//		sql.append("	(CASE WHEN '01040224' BETWEEN ");
//		sql.append("	(CASE WHEN VARCHAR_FORMAT(CURRENT TIMESTAMP, 'YYYYMMDD') BETWEEN ");
		sql.append("	(CASE WHEN '"+zDateHandler.getTWDate()+"' BETWEEN ");
		sql.append("	(CASE LENGTH(RTRIM(COALESCE(ACTIVE_DATE,''))) WHEN 0 THEN '99999999' ELSE ACTIVE_DATE END) AND "); 
		sql.append("	(CASE LENGTH(RTRIM(COALESCE(STOP_DATE,''))) WHEN 0 THEN '99999999' ELSE STOP_DATE END) "); 
		sql.append("	THEN 'Y' ELSE 'N' END) AS IS_ACTIVE ");
		sql.append("	FROM EACHUSER.BANK_GROUP ");
		sql.append(") AS B ON AP.BGBK_ID = B.BGBK_ID ");
		sql.append("WHERE AP.BGBK_ID = :bgbkId AND AP.APID = :apId");

		
		try{
			SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
			String cols = "BGBK_ID,APID,MBAPSTATUS,MBSYSSTATUS,MBAPSTATUSNAME,MBSYSSTATUSNAME";
			AutoAddScalar addscalar = new AutoAddScalar();
			addscalar.addScalar(query, BANK_STATUS.class, cols.split(","));
			query.setResultTransformer(Transformers.aliasToBean(BANK_STATUS.class));
			query.setParameter("bgbkId", bgbkId);
			query.setParameter("apId", apId);
			list = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return list;
	}
	
	public boolean saveData(BANKAPSTATUSTAB ap, BANKSYSSTATUSTAB sys){
		boolean result = false;
		if(ap != null && sys != null){
			Session session = getSessionFactory().openSession();
			session.beginTransaction();
			
			try {
				session.saveOrUpdate(ap);
				session.saveOrUpdate(sys);
				session.beginTransaction().commit();
				result = true;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				session.beginTransaction().rollback();
				result = false;
			}
			return result;
		}
		return result;
	}
}
