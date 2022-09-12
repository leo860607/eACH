package tw.org.twntch.db.dao.hibernate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;

import tw.org.twntch.po.AGENT_PROFILE;
import tw.org.twntch.po.BANK_GROUP;
import tw.org.twntch.util.StrUtils;

public class AGENT_PROFILE_Dao extends HibernateEntityDao<AGENT_PROFILE, String> {
	
	

	public List<AGENT_PROFILE> getAgentCom_Data(String com_id){
		List<AGENT_PROFILE> list = null;
		try{
			//取得前日期的民國年月日
			Calendar now = Calendar.getInstance();
			String nowDate = "0" + (now.get(Calendar.YEAR) - 1911) + new SimpleDateFormat("MMdd").format(now.getTime());
			String sql = " FROM tw.org.twntch.po.AGENT_PROFILE ";
			sql += " WHERE COMPANY_ID = :com_id AND ACTIVE_DATE <> '' AND '" + nowDate + "' BETWEEN ACTIVE_DATE AND ";
			sql += " (CASE STOP_DATE WHEN '' THEN '" + nowDate + "' ELSE STOP_DATE END)  ";
			Query query = getCurrentSession().createQuery(sql);
			query.setParameter("com_id", com_id);
			list = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return list;
	}
	
	
	public List<AGENT_PROFILE> getCompany_Id_List(String bizdate){
		List<AGENT_PROFILE> list = null;
		StringBuffer sql = new StringBuffer();
		try{
			System.out.println("bizdate>>"+bizdate);
			sql.append("SELECT * FROM AGENT_PROFILE WHERE  ");
			sql.append(bizdate+" BETWEEN  ");
			sql.append("  (CASE LENGTH(RTRIM(COALESCE(ACTIVE_DATE,''))) WHEN 0 THEN '99999999' ELSE ACTIVE_DATE END) ");
//			-1 是因STOP_DATE當天就算到期(停用)
			sql.append(" AND  (CASE LENGTH(RTRIM(COALESCE(STOP_DATE,''))) WHEN 0 THEN '99999999' ELSE STOP_DATE-1 END) ");
			sql.append(" ORDER BY COMPANY_ID ");
			SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
			query.addEntity(AGENT_PROFILE.class);
			list = query.list();
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	public List<AGENT_PROFILE> getCompany_Id_List(){
		List<AGENT_PROFILE> list = null;
		StringBuffer sql = new StringBuffer();
		try{
			list = this.getAll();
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	public List<AGENT_PROFILE> getData(String sql, List values){
		List<AGENT_PROFILE> list = null;
		
		try{
			SQLQuery query = getCurrentSession().createSQLQuery(sql);
			for(int i = 0; i < values.size(); i++){
				query.setString(i, (String) values.get(i));
			}
			query.addEntity(AGENT_PROFILE.class);
			list = query.list();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return list;
	}
}