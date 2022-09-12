package tw.org.twntch.db.dao.hibernate;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;

import tw.org.twntch.po.BANK_CTBK;
import tw.org.twntch.po.BANK_OPBK;

public class BANK_OPBK_Dao extends HibernateEntityDao<BANK_OPBK, Serializable> {
	public List<BANK_OPBK> getAllOpbkList(){
		List<BANK_OPBK> list = null;
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT COALESCE(OP.OPBK_ID,'') AS OPBK_ID , COALESCE(BG.BGBK_NAME ,'') AS OPBK_NAME ");
		sql.append("FROM ( ");
		sql.append("    SELECT DISTINCT OPBK_ID ");
		sql.append("    FROM EACHUSER.BANK_OPBK ");
		sql.append(") AS OP JOIN ( ");
		sql.append("	SELECT BGBK_ID, BGBK_NAME ");
		sql.append("	FROM BANK_GROUP WHERE BGBK_ATTR <> '6' ");
		sql.append(") AS BG ON ");
		sql.append("OP.OPBK_ID = BG.BGBK_ID ");
		sql.append("ORDER BY OP.OPBK_ID ");
		
		try{
			SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
			query.setResultTransformer(Transformers.aliasToBean(BANK_OPBK.class));
			query.addScalar("OPBK_ID").addScalar("OPBK_NAME");
			list = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return list;
	}
	public List<BANK_OPBK> getAllOpbkListFromMaster(){
		List<BANK_OPBK> list = null;
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT COALESCE(OP.OPBK_ID,'') AS OPBK_ID, COALESCE(BG.BGBK_NAME ,'') AS OPBK_NAME ");
		sql.append("FROM ( ");
		sql.append("    SELECT DISTINCT OPBK_ID ");
		sql.append("    FROM EACHUSER.BANK_OPBK ");
		sql.append(") AS OP JOIN ( ");
		sql.append("	SELECT BGBK_ID, BGBK_NAME ");
		sql.append("	FROM MASTER_BANK_GROUP WHERE BGBK_ATTR <> '6' ");
		sql.append(") AS BG ON ");
		sql.append("OP.OPBK_ID = BG.BGBK_ID ");
		sql.append("ORDER BY OP.OPBK_ID ");
		
		try{
			SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
			query.setResultTransformer(Transformers.aliasToBean(BANK_OPBK.class));
			query.addScalar("OPBK_ID").addScalar("OPBK_NAME");
			list = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return list;
	}
	
	public List<BANK_OPBK> getBgbkList(String sqlPath , Map<String,String> param,String s_bizdate , String e_bizdate ){
		List<BANK_OPBK> list = null;
		StringBuffer sql = new StringBuffer();
//--區間內及月份內的操作行所屬總行清單，含當前操作行所屬總行清單(START_DATE<=營業日的總行清單)
		sql.append(" WITH TMP AS ( ");
		sql.append(" SELECT  A.BGBK_ID , A.OPBK_ID ,A.START_DATE "); 
		sql.append(" FROM EACHUSER.BANK_OPBK A ");
		sql.append(" JOIN ( ");
		sql.append("       SELECT   max(START_DATE) START_DATE , BGBK_ID  "); 
		sql.append("       FROM EACHUSER.BANK_OPBK ");
		sql.append("       WHERE  START_DATE <= :s_bizdate ");
		sql.append("       GROUP BY BGBK_ID ");
		sql.append("       ORDER BY BGBK_ID ");
		sql.append(" ) AS B ");
		sql.append(" ON A.BGBK_ID = B.BGBK_ID AND A.START_DATE = B.START_DATE ");
		sql.append(" UNION ALL ");
		sql.append(" SELECT  A.BGBK_ID , A.OPBK_ID ,A.START_DATE  ");
		sql.append(" FROM EACHUSER.BANK_OPBK A ");
		sql.append(" JOIN ( ");
		sql.append("       SELECT   BGBK_ID , START_DATE  ");
		sql.append("       FROM EACHUSER.BANK_OPBK ");
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
			query.setResultTransformer(Transformers.aliasToBean(BANK_OPBK.class));
			query.addScalar("BGBK_ID").addScalar("BGBK_NAME").addScalar("START_DATE");
			list = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
			logger.debug("getBgbkList.HibernateException>>"+e);
		}
		return list;
	}
	
	public List<BANK_OPBK> getCurBgbkList(String sqlPath , Map<String,String> param,String bizdate  ){
		List<BANK_OPBK> list = null;
		StringBuffer sql = new StringBuffer();
		//--依據操作行id及營業日期取得所屬的總行清單 
		sql.append(" SELECT  A.BGBK_ID , A.OPBK_ID ,A.START_DATE "); 
		sql.append(" ,COALESCE ((SELECT BG.BGBK_NAME FROM BANK_GROUP BG WHERE BG.BGBK_ID = A.BGBK_ID ) , '') AS BGBK_NAME ");
		sql.append(" ,COALESCE ((SELECT BG.BGBK_NAME FROM BANK_GROUP BG WHERE BG.BGBK_ID = A.OPBK_ID ) , '') AS OPBK_NAME ");
		sql.append(" FROM EACHUSER.BANK_OPBK A ");
		sql.append(" JOIN ( ");
		sql.append("    SELECT  max(START_DATE) START_DATE , BGBK_ID  ");
		sql.append("    FROM EACHUSER.BANK_OPBK ");
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
			query.setResultTransformer(Transformers.aliasToBean(BANK_OPBK.class));
			query.addScalar("BGBK_ID").addScalar("BGBK_NAME").addScalar("START_DATE").addScalar("OPBK_ID").addScalar("OPBK_NAME");
			list = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
			logger.debug("getBgbkList.HibernateException>>"+e);
		}
		return list;
	}
	
	public List<BANK_OPBK> getHisBgbkList(String opbk_id ,String bizdate  ){
		List<BANK_OPBK> list = null;
		
		
//----取得日期區間曾經的總行清單
		/*
SELECT  A.BGBK_ID , A.OPBK_ID ,A.START_DATE  
,COALESCE ((SELECT BG.BGBK_NAME FROM BANK_GROUP BG WHERE BG.BGBK_ID = A.BGBK_ID ) , '') AS BGBK_NAME
,COALESCE ((SELECT BG.BGBK_NAME FROM BANK_GROUP BG WHERE BG.BGBK_ID = A.OPBK_ID ) , '') AS OPBK_NAME
FROM EACHUSER.BANK_OPBK A
JOIN (
   SELECT   BGBK_ID , START_DATE --, max(START_DATE) , max (BGBK_ID)
    FROM EACHUSER.BANK_OPBK 
    WHERE  START_DATE BETWEEN '01040616' AND '01040816' 
    GROUP BY BGBK_ID , START_DATE -- , START_DATE ,BGBK_ID ,
    ORDER BY BGBK_ID  
) AS B
ON A.BGBK_ID = B.BGBK_ID AND A.START_DATE = B.START_DATE 
--WHERE A.OPBK_ID = '8160000'
		 */		
		return list;
	}
	
	public List<BANK_OPBK> getDataByBgbkId(String bgbk_id ,String sqlPath){
		List<BANK_OPBK> list = null;
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT A.OPBK_ID, A.BGBK_ID,A.START_DATE , A.OP_NOTE ");
		sql.append(" ,COALESCE( (SELECT B.BGBK_NAME FROM BANK_GROUP B WHERE B.BGBK_ID = A.OPBK_ID ),'') AS OPBK_NAME   ");
		sql.append(" ,COALESCE( (SELECT B.BGBK_NAME FROM BANK_GROUP B WHERE B.BGBK_ID = A.BGBK_ID ),'') AS BGBK_NAME   ");
		sql.append(" FROM EACHUSER.BANK_OPBK A ");
		sql.append(" WHERE A.BGBK_ID = :bgbk_id ");
//		sql.append(" ORDER BY  A.START_DATE DESC ");
		sql.append(sqlPath);
		try{
			SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
			query.setParameter("bgbk_id", bgbk_id);
			query.setResultTransformer(Transformers.aliasToBean(BANK_OPBK.class));
			query.addScalar("OPBK_ID").addScalar("OPBK_NAME").addScalar("START_DATE").addScalar("BGBK_ID").addScalar("BGBK_NAME").addScalar("OP_NOTE");
			list = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
		}
		
		return list;
	}
	
	
//	SELECT  *  
//	FROM EACHUSER.BANK_OPBK  WHERE OPBK_ID = '0040000' AND START_DATE <= '01040813' 
//	ORDER BY START_DATE DESC
//	FETCH FIRST 1 ROWS ONLY
//	TODO 未完成
	public List<BANK_OPBK> getCur_Bgbk(String opbk_id ,String bizdate){
		List<BANK_OPBK> list = null;
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT A.OPBK_ID, A.BGBK_ID,A.START_DATE  ");
		try{
			SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
//			query.setParameter("bgbk_id", bgbk_id);
			query.setResultTransformer(Transformers.aliasToBean(BANK_OPBK.class));
			query.addScalar("OPBK_ID").addScalar("OPBK_NAME").addScalar("START_DATE").addScalar("BGBK_ID").addScalar("BGBK_NAME");
			list = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
		}
		
		return list;
	}
}