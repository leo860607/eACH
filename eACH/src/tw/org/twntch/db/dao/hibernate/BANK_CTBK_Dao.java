package tw.org.twntch.db.dao.hibernate;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;

import tw.org.twntch.po.BANK_CTBK;
import tw.org.twntch.po.BANK_CTBK;

public class BANK_CTBK_Dao extends HibernateEntityDao<BANK_CTBK, Serializable> {
	
	
	public List<BANK_CTBK> getBgbkList(String sqlPath , Map<String,String> param ,String s_bizdate, String e_bizdate){
		List<BANK_CTBK> list = null;
		StringBuilder sql = new StringBuilder();
		//--區間內及月份內的清算行所屬總行清單，含當前清算行所屬總行清單(START_DATE<=營業日的總行清單)
		sql.append(" WITH TMP AS ( ");
		sql.append(" SELECT  A.BGBK_ID , A.CTBK_ID ,A.START_DATE "); 
		sql.append(" FROM EACHUSER.BANK_CTBK A ");
		sql.append(" JOIN ( ");
		sql.append("       SELECT   max(START_DATE) START_DATE , BGBK_ID  "); 
		sql.append("       FROM EACHUSER.BANK_CTBK ");
		sql.append("       WHERE  START_DATE <= :s_bizdate ");
		sql.append("       GROUP BY BGBK_ID ");
		sql.append("       ORDER BY BGBK_ID ");
		sql.append(" ) AS B ");
		sql.append(" ON A.BGBK_ID = B.BGBK_ID AND A.START_DATE = B.START_DATE ");
		sql.append(" UNION ALL ");
		sql.append(" SELECT  A.BGBK_ID , A.CTBK_ID ,A.START_DATE  ");
		sql.append(" FROM EACHUSER.BANK_CTBK A ");
		sql.append(" JOIN ( ");
		sql.append("       SELECT   BGBK_ID , START_DATE  ");
		sql.append("       FROM EACHUSER.BANK_CTBK ");
		sql.append("       WHERE START_DATE BETWEEN :s_bizdate AND :e_bizdate ");
		sql.append("       GROUP BY BGBK_ID , START_DATE  ");
		sql.append("       ORDER BY BGBK_ID  ");
		sql.append(" ) AS B ");
		sql.append(" ON A.BGBK_ID = B.BGBK_ID AND A.START_DATE = B.START_DATE  ");
		sql.append(" ) ");
		sql.append(" SELECT BGBK_ID  ");
		sql.append(" ,COALESCE ((SELECT BG.BGBK_NAME FROM BANK_GROUP BG WHERE BG.BGBK_ID = TMP.BGBK_ID ) , '') AS BGBK_NAME ");
		sql.append(" ,MAX(START_DATE) AS START_DATE ");
		sql.append(" FROM TMP ");
		sql.append(sqlPath);
		sql.append(" GROUP BY BGBK_ID ");
		sql.append(" ORDER BY BGBK_ID ");

		try{
			SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
			query.setParameter("s_bizdate", s_bizdate);
			query.setParameter("e_bizdate", e_bizdate);
			for( String key :param.keySet()){
				query.setParameter(key, param.get(key));
			}
			query.setResultTransformer(Transformers.aliasToBean(BANK_CTBK.class));
			query.addScalar("BGBK_ID").addScalar("BGBK_NAME").addScalar("START_DATE");
			list = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
			logger.debug("getBgbkList.HibernateException>>"+e);
		}
		return list;
		
	}
	
	

	public List<BANK_CTBK> getCurBgbkList(String sqlPath , Map<String,String> param,String bizdate  ){
		List<BANK_CTBK> list = null;
		StringBuffer sql = new StringBuffer();
		//--依據操作行id及營業日期取得所屬的總行清單 
		sql.append(" SELECT  A.BGBK_ID , A.CTBK_ID ,A.START_DATE "); 
		sql.append(" ,COALESCE ((SELECT BG.BGBK_NAME FROM BANK_GROUP BG WHERE BG.BGBK_ID = A.BGBK_ID ) , '') AS BGBK_NAME ");
		sql.append(" ,COALESCE ((SELECT BG.BGBK_NAME FROM BANK_GROUP BG WHERE BG.BGBK_ID = A.CTBK_ID ) , '') AS CTBK_NAME ");
		sql.append(" FROM EACHUSER.BANK_CTBK A ");
		sql.append(" JOIN ( ");
		sql.append("    SELECT  max(START_DATE) START_DATE , BGBK_ID  ");
		sql.append("    FROM EACHUSER.BANK_CTBK ");
		sql.append("    WHERE START_DATE <= :bizdate ");
		sql.append("    GROUP BY BGBK_ID ");
		sql.append("    ORDER BY BGBK_ID ");
		sql.append(" ) AS B ");
		sql.append(" ON A.BGBK_ID = B.BGBK_ID AND A.START_DATE = B.START_DATE ");
		sql.append(sqlPath);
		try{
			SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
			query.setParameter("bizdate", bizdate);
			for( String key :param.keySet()){
				query.setParameter(key, param.get(key));
			}
			query.setResultTransformer(Transformers.aliasToBean(BANK_CTBK.class));
			query.addScalar("BGBK_ID").addScalar("BGBK_NAME").addScalar("START_DATE").addScalar("CTBK_ID").addScalar("CTBK_NAME");
			list = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
			logger.debug("getBgbkList.HibernateException>>"+e);
		}
		return list;
	}
	
	
	
	
	public List<BANK_CTBK> getAllCtbkList(){
		List<BANK_CTBK> list = null;
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT COALESCE(CT.CTBK_ID,'') AS CTBK_ID , COALESCE(BG.BGBK_NAME ,'') AS CTBK_NAME ");
		sql.append("FROM ( ");
		sql.append("    SELECT DISTINCT CTBK_ID ");
		sql.append("    FROM EACHUSER.BANK_CTBK ");
//		sql.append(") AS CT LEFT JOIN BANK_GROUP AS BG ON ");
		sql.append(") AS CT JOIN BANK_GROUP AS BG ON ");
		sql.append("CT.CTBK_ID = BG.BGBK_ID ");
		sql.append("ORDER BY CT.CTBK_ID ");
		
		try{
			SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
			query.setResultTransformer(Transformers.aliasToBean(BANK_CTBK.class));
			query.addScalar("CTBK_ID").addScalar("CTBK_NAME");
			list = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return list;
	}
	
	public List<BANK_CTBK> getAllCtbkListFromMaster(){
		List<BANK_CTBK> list = null;
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT COALESCE(CT.CTBK_ID,'') AS CTBK_ID , COALESCE(BG.BGBK_NAME ,'') AS CTBK_NAME ");
		sql.append("FROM ( ");
		sql.append("    SELECT DISTINCT CTBK_ID ");
		sql.append("    FROM EACHUSER.BANK_CTBK ");
//		sql.append(") AS CT LEFT JOIN MASTER_BANK_GROUP AS BG ON ");
		sql.append(") AS CT JOIN MASTER_BANK_GROUP AS BG ON ");
		sql.append("CT.CTBK_ID = BG.BGBK_ID ");
		sql.append("ORDER BY CT.CTBK_ID ");
		
		try{
			SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
			query.setResultTransformer(Transformers.aliasToBean(BANK_CTBK.class));
			query.addScalar("CTBK_ID").addScalar("CTBK_NAME");
			list = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return list;
	}
	/**
	 * 取得所有代理清算行
	 * @return
	 */
	public List<BANK_CTBK> getAll_Proxy_CtbkList(){
		List<BANK_CTBK> list = null;
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT  DISTINCT  A.CTBK_ID ");
		sql.append(" , COALESCE((SELECT BG.BGBK_NAME FROM BANK_GROUP BG WHERE BG.BGBK_ID = A.CTBK_ID ),'') AS CTBK_NAME ");
		sql.append(" FROM EACHUSER.BANK_CTBK A ");
		sql.append(" JOIN EACHUSER.BANK_GROUP AS B  ON A.CTBK_ID = B.BGBK_ID ");
		sql.append(" WHERE A.CTBK_ID != A.BGBK_ID  ");
		sql.append(" ORDER BY A.CTBK_ID ");
		try{
			SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
			query.setResultTransformer(Transformers.aliasToBean(BANK_CTBK.class));
			query.addScalar("CTBK_ID").addScalar("CTBK_NAME");
			list = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 根據日期取得所有代理清算行清單，如為單一日期，s_bizdate 必須等於e_bizdate
	 * @param s_bizdate
	 * @param e_bizdate
	 * @return
	 */
	public List<BANK_CTBK> getAll_Proxy_CtbkList(String s_bizdate , String e_bizdate){
		List<BANK_CTBK> list = null;
		StringBuilder sql = new StringBuilder();
		
		sql.append(" WITH TMP AS ( ");
		sql.append(" SELECT  A.BGBK_ID , A.CTBK_ID ,A.START_DATE "); 
		sql.append(" FROM EACHUSER.BANK_CTBK A ");
		sql.append(" JOIN ( ");
		sql.append("       SELECT   max(START_DATE) START_DATE , BGBK_ID  "); 
		sql.append("       FROM EACHUSER.BANK_CTBK ");
		sql.append("       WHERE  START_DATE <= :s_bizdate ");
		sql.append("       GROUP BY BGBK_ID ");
		sql.append("       ORDER BY BGBK_ID ");
		sql.append(" ) AS B ");
		sql.append(" ON A.BGBK_ID = B.BGBK_ID AND A.START_DATE = B.START_DATE AND  A.CTBK_ID != B.BGBK_ID ");
		sql.append(" UNION ALL ");
		sql.append(" SELECT  A.BGBK_ID , A.CTBK_ID ,A.START_DATE  ");
		sql.append(" FROM EACHUSER.BANK_CTBK A ");
		sql.append(" JOIN ( ");
		sql.append("       SELECT   BGBK_ID , START_DATE  ");
		sql.append("       FROM EACHUSER.BANK_CTBK ");
		sql.append("       WHERE START_DATE BETWEEN :s_bizdate AND :e_bizdate ");
		sql.append("       GROUP BY BGBK_ID , START_DATE  ");
		sql.append("       ORDER BY BGBK_ID  ");
		sql.append(" ) AS B ");
		sql.append(" ON A.BGBK_ID = B.BGBK_ID AND A.START_DATE = B.START_DATE AND  A.CTBK_ID != B.BGBK_ID ");
		sql.append(" ) ");
		sql.append(" SELECT BGBK_ID  ");
		sql.append(" ,COALESCE ((SELECT BG.BGBK_NAME FROM BANK_GROUP BG WHERE BG.BGBK_ID = TMP.BGBK_ID ) , '') AS BGBK_NAME ");
		sql.append(" ,MAX(START_DATE) AS START_DATE ");
		sql.append(" FROM TMP ");
		sql.append(" GROUP BY BGBK_ID ");
		sql.append(" ORDER BY BGBK_ID ");
		try{
			SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
			query.setResultTransformer(Transformers.aliasToBean(BANK_CTBK.class));
			query.addScalar("CTBK_ID").addScalar("CTBK_NAME").addScalar("START_DATE");;
			list = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return list;
	}
	
	public List<BANK_CTBK> getDataByBgbkId(String bgbk_id ,String sqlPath){
		List<BANK_CTBK> list = null;
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT A.CTBK_ID, A.BGBK_ID,A.START_DATE , A.CT_NOTE  ");
		sql.append(" ,COALESCE( (SELECT B.BGBK_NAME FROM BANK_GROUP B WHERE B.BGBK_ID = A.CTBK_ID ),'') AS CTBK_NAME   ");
		sql.append(" ,COALESCE( (SELECT B.BGBK_NAME FROM BANK_GROUP B WHERE B.BGBK_ID = A.BGBK_ID ),'') AS BGBK_NAME   ");
		sql.append(" FROM EACHUSER.BANK_CTBK A ");
		sql.append(" WHERE A.BGBK_ID = :bgbk_id ");
//		sql.append(" ORDER BY  A.START_DATE DESC ");
		sql.append(sqlPath);
		try{
			SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
			query.setParameter("bgbk_id", bgbk_id);
			query.setResultTransformer(Transformers.aliasToBean(BANK_CTBK.class));
			query.addScalar("CTBK_ID").addScalar("CTBK_NAME").addScalar("START_DATE").addScalar("BGBK_ID").addScalar("BGBK_NAME").addScalar("CT_NOTE");
			list = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
		}
		
		return list;
	}
}