package tw.org.twntch.db.dao.hibernate;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.impl.SessionImpl;
import org.hibernate.transform.Transformers;

import tw.org.twntch.po.OPCTRANSACTIONLOGTAB;
import tw.org.twntch.po.WK_DATE_CALENDAR;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.zDateHandler;

public class WK_DATE_Dao extends HibernateEntityDao<WK_DATE_CALENDAR, Serializable> {
	
	/**
	 * 
	 * @param first_date(西元年)
	 * @param last_date(西元年)
	 * @return
	 */
	public List<Map> getFirst_Bizdate_of_Month(String first_date , String last_date ){
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT txn_date FROM ( ");
		sql.append(" SELECT EACHUSER.TRANS_DATE(txn_date,'W','') txn_date  ,row_number()  OVER (ORDER BY txn_date asc) row_num ");
		sql.append(" FROM	EACHUSER.WK_DATE_CALENDAR ");
		sql.append(" where (txn_date between EACHUSER.TRANS_DATE("+first_date+",'T','') and  EACHUSER.TRANS_DATE("+last_date+",'T','') ) and IS_TXN_DATE='Y' ) ");
		sql.append(" where row_num =1 ");

		SQLQuery  sqlquery = getCurrentSession().createSQLQuery(sql.toString());
		sqlquery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List<Map> list = sqlquery.list();
		return list ;
	}
	/**
	 * 
	 * @param first_date(西元年)
	 * @param last_date(西元年)
	 * @return
	 */
	public List<Map> getLast_Bizdate_of_Month(String first_date , String last_date ){
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT txn_date FROM ( ");
		sql.append(" SELECT EACHUSER.TRANS_DATE(txn_date,'W','') txn_date  ,row_number()  OVER (ORDER BY txn_date desc) row_num ");
		sql.append(" FROM	EACHUSER.WK_DATE_CALENDAR ");
		sql.append(" where (txn_date between EACHUSER.TRANS_DATE("+first_date+",'T','') and  EACHUSER.TRANS_DATE("+last_date+",'T','') ) and IS_TXN_DATE='Y' ) ");
		sql.append(" where row_num =1 ");
		
		SQLQuery  sqlquery = getCurrentSession().createSQLQuery(sql.toString());
		sqlquery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List<Map> list = sqlquery.list();
		return list ;
	}
	
	
	
	
	public List<WK_DATE_CALENDAR> getByTwYear(String twYear){
		List<WK_DATE_CALENDAR> list = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" FROM tw.org.twntch.po.WK_DATE_CALENDAR WITHUR WHERE TXN_DATE LIKE :twYear ORDER BY TXN_DATE");
		try{
			Query query = getCurrentSession().createQuery(sql.toString());
			query.setParameter("twYear", twYear + "%");
			list = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return list;
	}
	
	public List<WK_DATE_CALENDAR> getByTwYearMonth(String twYear, String twMonth){
		List<WK_DATE_CALENDAR> list = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" FROM tw.org.twntch.po.WK_DATE_CALENDAR WITHUR WHERE TXN_DATE LIKE :twYearMonth ORDER BY TXN_DATE");
		try{
			Query query = getCurrentSession().createQuery(sql.toString());
			query.setParameter("twYearMonth", twYear + twMonth + "%");
			list = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return list;
	}
	
	public List<WK_DATE_CALENDAR> getByStartAndEndDate(String startDate, String endDate){
		List<WK_DATE_CALENDAR> list = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" FROM tw.org.twntch.po.WK_DATE_CALENDAR WITHUR WHERE TXN_DATE BETWEEN :startDate AND :endDate ORDER BY TXN_DATE");
		try{
			Query query = getCurrentSession().createQuery(sql.toString());
			query.setParameter("startDate", startDate);
			query.setParameter("endDate", endDate);
			list = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return list;
	}
	
	public boolean createWholeYearData(String twYear) throws SQLException{
		boolean result = false;
		Session session = getSessionFactory().openSession();
		session.beginTransaction();
		String sql = "CALL MAKEWK_ALLDATE(" + Integer.valueOf(twYear) + ")";
		try {
			CallableStatement query = session.connection().prepareCall(sql);
			query.execute();
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
	
	//從某一時間起(含)往前或往後取N個營業日
	public String gotoBusinessDate(String west_startDate, int offset){
		String tDate = west_startDate;
		
		StringBuilder sql = new StringBuilder();
		String tw_startDate = DateTimeUtils.convertDate(west_startDate, "yyyyMMdd", "yyyyMMdd");
		sql.append("SELECT * FROM ( ");
		if(offset < 0){
			sql.append("    SELECT ROW_NUMBER() OVER(ORDER BY TXN_DATE DESC) AS NUM, WK.TXN_DATE ");
			sql.append("    FROM WK_DATE_CALENDAR AS WK ");
			sql.append("    WHERE TXN_DATE < '"+ tw_startDate + "' ");
		}else{
			sql.append("    SELECT ROW_NUMBER() OVER(ORDER BY TXN_DATE ASC) AS NUM, WK.TXN_DATE ");
			sql.append("    FROM WK_DATE_CALENDAR AS WK ");
			sql.append("    WHERE TXN_DATE >= '" + tw_startDate + "' ");
			offset++;
		}
		sql.append("    AND IS_TXN_DATE = 'Y' ");
		sql.append(") AS TEMP ");
		sql.append("WHERE NUM = " + Math.abs(offset));
		System.out.println("GOTO SQL >> " + sql);
		
		List<Map> wkList = null;
		try{
			SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
			query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			wkList = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
		}
		
		if(wkList != null && wkList.size() > 0){
			tDate = (String)wkList.get(0).get("TXN_DATE");
			tDate = DateTimeUtils.convertDate(tDate, "yyyyMMdd", "yyyyMMdd");
		}
		return tDate;
	}
	
	//取得最新STAN並加1(當日不重複)
	public String getStan(){
		String nTraceNo = null;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT CASE MAX(COALESCE(WEBTRACENO,'')) WHEN '' THEN '0000000' ELSE ( ");
		sql.append("	COALESCE(RIGHT(RTRIM(REPEAT('0', 7) || CAST(CAST(MAX(WEBTRACENO) AS INT) + 1 AS CHAR(20))), 7), '0000000') "); 
		sql.append(") END AS N_TRACENO ");
		sql.append("FROM OPCTRANSACTIONLOGTAB ");
		sql.append("WHERE TXDATE = '" + zDateHandler.getDateNum() + "' ");
		try{
			SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
			query.addScalar("N_TRACENO", Hibernate.STRING);
			query.setResultTransformer(Transformers.aliasToBean(OPCTRANSACTIONLOGTAB.class));
			List<OPCTRANSACTIONLOGTAB> list = query.list();
			if(list != null && list.size() > 0){
				nTraceNo = list.get(0).getN_TRACENO();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return nTraceNo;
	}

	//將onblocktab, onpendingtab, onclearing在typhDate的資料營業日改為nextBizdate
	public String updateTyphData(String typhDate, String nextBizdate){
		//測試用
		boolean isTest = false;
		
		String rtnStr = "";
		String sql = "{CALL TYPH_UPD_BIZDATE(?,?,?)}";
		
		try{
			CallableStatement callableStatement = ((SessionImpl)getCurrentSession()).connection().prepareCall(sql);
			callableStatement.registerOutParameter(1, java.sql.Types.VARCHAR);
			callableStatement.setString(2, typhDate);
			callableStatement.setString(3, nextBizdate);
			log("EXEC : {CALL TYPH_UPD_BIZDATE(?,'"+typhDate+"','"+nextBizdate+"')} = ");
			//測試時不執行，避免影響資料
			if(!isTest){
				callableStatement.executeUpdate();
				rtnStr = callableStatement.getString(1);
			}else{
				rtnStr = "0,1,2,3";
			}
			log(rtnStr);
		}catch(HibernateException e){
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rtnStr;
	}
	
	public void log(String msg){
		System.out.println("### " + msg);
		logger.debug("### " + msg);
		
	}
}
