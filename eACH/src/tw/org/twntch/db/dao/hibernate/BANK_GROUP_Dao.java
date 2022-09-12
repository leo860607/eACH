package tw.org.twntch.db.dao.hibernate;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;

import tw.org.twntch.po.BANKAPSTATUSTAB;
import tw.org.twntch.po.BANKSYSSTATUSTAB;
import tw.org.twntch.po.BANK_CTBK;
import tw.org.twntch.po.BANK_GROUP;
import tw.org.twntch.po.BANK_GROUP_BUSINESS;
import tw.org.twntch.po.BANK_OPBK;
import tw.org.twntch.po.CR_LINE;
import tw.org.twntch.util.AutoAddScalar;
import tw.org.twntch.util.zDateHandler;

public class BANK_GROUP_Dao extends HibernateEntityDao<BANK_GROUP, Serializable>{
	
	public List<BANK_GROUP> getBgbkByComId(String comId){
		StringBuffer sql = new  StringBuffer();
		sql.append(" SELECT * FROM BANK_GROUP WHERE BGBK_ID =:comId ");
		sql.append(" AND  "+zDateHandler.getTWDate()+" BETWEEN ");
		sql.append("  (CASE LENGTH(RTRIM(COALESCE(ACTIVE_DATE,''))) WHEN 0 THEN '99999999' ELSE ACTIVE_DATE END) ");
//		-1 是因STOP_DATE當天就算到期(停用)
		sql.append(" AND  (CASE LENGTH(RTRIM(COALESCE(STOP_DATE,''))) WHEN 0 THEN '99999999' ELSE STOP_DATE-1 END) ");
		SQLQuery query =  getCurrentSession().createSQLQuery(sql.toString());
		query.setParameter("comId", comId);
		query.addEntity(BANK_GROUP.class);
		List list = query.list();
		return list;
	}
	
	public List<BANK_GROUP> getProxyClean_BankList(){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT CT.CTBK_ID, BG.BGBK_NAME AS CTBK_NAME FROM ( ");
		sql.append("    SELECT DISTINCT CTBK_ID ");
		sql.append("    FROM BANK_CTBK ");
		sql.append("    WHERE BGBK_ID <> CTBK_ID ");
		sql.append(") AS CT JOIN BANK_GROUP AS BG ON ");
		sql.append("CT.CTBK_ID = BG.BGBK_ID ");
		sql.append("ORDER BY CT.CTBK_ID ");
		/*
		sql.append(" SELECT DISTINCT B.BRBK_NAME CTBK_NAME, A.CTBK_ID FROM BANK_GROUP A  ");
		sql.append(" JOIN BANK_BRANCH B ON A.CTBK_ID=B.BRBK_ID ");
		sql.append(" WHERE A.CTBK_ID != A.BGBK_ID ORDER BY A.CTBK_ID ");
		*/
		SQLQuery query =  getCurrentSession().createSQLQuery(sql.toString());
		query.addScalar("CTBK_NAME" , Hibernate.STRING);
		query.addScalar("CTBK_ID" , Hibernate.STRING);
		query.setResultTransformer( Transformers.aliasToBean(BANK_GROUP.class) );
		List<BANK_GROUP> list = query.list();
		return list;
	}
	
	public List<BANK_GROUP> getBgbkIdList(){
		List list = new ArrayList();
		//20150120 建利說，共用中心不會做交易，故總行清單中應排除共用中心選項
		Query query = getCurrentSession().createQuery("FROM tw.org.twntch.po.BANK_GROUP BANK_GROUP WHERE BGBK_ATTR <> '5' ORDER BY BGBK_ID");
		list = query.list();
		return list;
	}
	
	/**
	 * 非票交、共用中心的總行清單
	 * @return
	 */
	public List<BANK_GROUP> getBgbkIdList_Not_5_And_6(){
		List list = new ArrayList();
		Query query = getCurrentSession().createQuery("FROM tw.org.twntch.po.BANK_GROUP BANK_GROUP WHERE BGBK_ATTR <> '6' AND BGBK_ATTR <> '5' ORDER BY BGBK_ID");
		list = query.list();
		return list;
	}
	/**
	 * 非票交、共用中心及屬於EACH的總行清單
	 * @return
	 */
	public List<BANK_GROUP> getBgbkIdList_Not_5_And_6_And_IS_EACH(){
		List list = new ArrayList();
		Query query = getCurrentSession().createQuery("FROM tw.org.twntch.po.BANK_GROUP BANK_GROUP WHERE BGBK_ATTR <> '6' AND BGBK_ATTR <> '5' AND IS_EACH = 'Y' ORDER BY BGBK_ID");
		list = query.list();
		return list;
	}
	
	/**
	 * 非票交的總行清單
	 * @return
	 */
	public List<BANK_GROUP> getBgbkIdListwithoutACH(){
		List list = new ArrayList();
		Query query = getCurrentSession().createQuery("FROM tw.org.twntch.po.BANK_GROUP BANK_GROUP WHERE BGBK_ATTR != '6' ORDER BY BGBK_ID");
		list = query.list();
		return list;
	}
	/**
	 * 屬票交的總行清單
	 * @return
	 */
	public List<BANK_GROUP> getBgbkIdList_ACH(){
		List list = new ArrayList();
		Query query = getCurrentSession().createQuery("FROM tw.org.twntch.po.BANK_GROUP BANK_GROUP WHERE BGBK_ATTR = '6' ORDER BY BGBK_ID");
		list = query.list();
		return list;
	}
	
	/* 
	 * 20150625 edit by hugo req by UAT-2015018-02  只要有啟用日期就要出現
	 * 20141210 BY David
	 * 操作行之資料應判斷目前日期是否落於銀行總行檔之啟用日期與停用日期之間
	 * 然後distinct 操作行代號與帶出操作行名稱(OPBK_ID、OPBK_NAME)、提供操作行選取.
	 * edit: 應過濾票交分所代號
	 */
	public List<BANK_GROUP> getBgbkIdList_2(){
		List<BANK_GROUP> list = null;
		try{
			//取得前日期的民國年月日
			Calendar now = Calendar.getInstance();
			String sql = "SELECT A.OPBK_ID AS BGBK_ID, B.BGBK_NAME ";
			sql += "FROM BANK_GROUP A JOIN BANK_GROUP B ON A.OPBK_ID = B.BGBK_ID ";
			sql += "WHERE  B.ACTIVE_DATE <> '' AND B.BGBK_ATTR <>'6' ";
			sql += "GROUP BY A.OPBK_ID, B.BGBK_NAME ORDER BY A.OPBK_ID";
			/*
			String sql = "SELECT BG.BGBK_ID, BG.BGBK_NAME FROM BANK_GROUP BG ";
			sql += "WHERE '" + nowDate + "' BETWEEN BG.ACTIVE_DATE AND BG.STOP_DATE ";
			sql += "GROUP BY BG.BGBK_ID, BG.BGBK_NAME ORDER BY BG.BGBK_ID";
			*/
			SQLQuery query = getCurrentSession().createSQLQuery(sql);
			query.addScalar("BGBK_ID", Hibernate.STRING)
			.addScalar("BGBK_NAME", Hibernate.STRING)
			.setResultTransformer(Transformers.aliasToBean(BANK_GROUP.class));
			list = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return list;
	}
	/* 20150624 note by hugo 此為原getBgbkIdList_2的備份
	 * 20141210 BY David 
	 * 操作行之資料應判斷目前日期是否落於銀行總行檔之啟用日期與停用日期之間
	 * 然後distinct 操作行代號與帶出操作行名稱(OPBK_ID、OPBK_NAME)、提供操作行選取.
	 * edit: 應過濾票交分所代號
	 */
	public List<BANK_GROUP> getBgbkIdList_2_BAK(){
		List<BANK_GROUP> list = null;
		try{
			//取得前日期的民國年月日
			Calendar now = Calendar.getInstance();
			String nowDate = "0" + (now.get(Calendar.YEAR) - 1911) + new SimpleDateFormat("MMdd").format(now.getTime());
			String sql = "SELECT A.OPBK_ID AS BGBK_ID, B.BGBK_NAME ";
			sql += "FROM BANK_GROUP A JOIN BANK_GROUP B ON A.OPBK_ID = B.BGBK_ID ";
			sql += "WHERE  B.ACTIVE_DATE <> '' AND '" + nowDate + "' BETWEEN B.ACTIVE_DATE AND ";
			sql += "(CASE B.STOP_DATE WHEN '' THEN '" + nowDate + "' ELSE B.STOP_DATE END) AND A.BGBK_ATTR <> '6' ";
			sql += "GROUP BY A.OPBK_ID, B.BGBK_NAME ORDER BY A.OPBK_ID";
			/*
			String sql = "SELECT BG.BGBK_ID, BG.BGBK_NAME FROM BANK_GROUP BG ";
			sql += "WHERE '" + nowDate + "' BETWEEN BG.ACTIVE_DATE AND BG.STOP_DATE ";
			sql += "GROUP BY BG.BGBK_ID, BG.BGBK_NAME ORDER BY BG.BGBK_ID";
			 */
			SQLQuery query = getCurrentSession().createSQLQuery(sql);
			query.addScalar("BGBK_ID", Hibernate.STRING)
			.addScalar("BGBK_NAME", Hibernate.STRING)
			.setResultTransformer(Transformers.aliasToBean(BANK_GROUP.class));
			list = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return list;
	}
	
	public List<BANK_GROUP> getBgbkId_Data(String BgbkId){
		List<BANK_GROUP> list = null;
		try{
			//取得前日期的民國年月日
			Calendar now = Calendar.getInstance();
			String nowDate = "0" + (now.get(Calendar.YEAR) - 1911) + new SimpleDateFormat("MMdd").format(now.getTime());
			String sql = " FROM tw.org.twntch.po.BANK_GROUP ";
			sql += " WHERE BGBK_ID = :BgbkId AND ACTIVE_DATE <> '' AND '" + nowDate + "' BETWEEN ACTIVE_DATE AND ";
			sql += " (CASE STOP_DATE WHEN '' THEN '" + nowDate + "' ELSE STOP_DATE END) AND BGBK_ATTR <> '6' ";
			Query query = getCurrentSession().createQuery(sql);
			query.setParameter("BgbkId", BgbkId);
			list = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return list;
	}
	
	public List<BANK_GROUP> getDataByBgbkId(String bgbkId){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ");
//		sql.append("BGBK_ID,BGBK_NAME,CTBK_ACCT,TCH_ID,OPBK_ID,CTBK_ID,ACTIVE_DATE,STOP_DATE,SND_FEE_BRBK_ID,OUT_FEE_BRBK_ID,IN_FEE_BRBK_ID,EDDA_FLAG,CDATE,UDATE, ");
		sql.append("BGBK_ID,BGBK_NAME,CTBK_ACCT,TCH_ID,ACTIVE_DATE,STOP_DATE,SND_FEE_BRBK_ID,OUT_FEE_BRBK_ID,IN_FEE_BRBK_ID,EDDA_FLAG,CDATE,UDATE,IS_EACH ");
		sql.append(" , (CASE BGBK_ATTR WHEN '0' THEN '本國銀行' WHEN '1' THEN '地區企銀' WHEN '2' THEN '合作社' WHEN '3' THEN '農漁會' WHEN '4' THEN '外商銀行' WHEN '5' THEN '共用中心' WHEN '6' THEN '票交分所' ELSE '未定義屬性' END) AS BGBK_ATTR, ");
		sql.append("COALESCE( ");
		sql.append("    (SELECT B.BGBK_NAME FROM BANK_GROUP B WHERE B.BGBK_ID = EACHUSER.GET_CUR_OPBKID (A.BGBK_ID , '' , 0 ) ), '' ");
		sql.append(") AS OPBK_NAME, ");
		sql.append("COALESCE( ");
		sql.append("    (SELECT B.BGBK_NAME FROM BANK_GROUP B WHERE B.BGBK_ID = EACHUSER.GET_CUR_CTBKID (A.BGBK_ID , '' , 0 ) ), '' ");
		sql.append(") AS CTBK_NAME ");
//		20150810 add by hugo 因應變更操作行、清算行
		sql.append(",  COALESCE( EACHUSER.GET_CUR_OPBKID (BGBK_ID , '' , 0 ) , '') AS OPBK_ID ");
		sql.append(",  COALESCE( EACHUSER.GET_CUR_CTBKID (BGBK_ID , '' , 0 ) , '') AS CTBK_ID ");
		sql.append(",  COALESCE( EACHUSER.GET_CUR_OPBKID (BGBK_ID , '',1 ) , '') AS OP_START_DATE  ");
		sql.append(", COALESCE( EACHUSER.GET_CUR_CTBKID (BGBK_ID , '' ,1) , '') AS CT_START_DATE ");
		sql.append(",  COALESCE( EACHUSER.GET_CUR_OPBKID (BGBK_ID , '',2 ) , '') AS OP_NOTE  ");
		sql.append(", COALESCE( EACHUSER.GET_CUR_CTBKID (BGBK_ID , '' ,2) , '') AS CT_NOTE ");
		sql.append(", COALESCE( HR_UPLOAD_MAX_FILE , 0) AS HR_UPLOAD_MAX_FILE  ");
		
		
		sql.append("FROM BANK_GROUP A ");
		sql.append("WHERE A.BGBK_ID = :bgbkId ");
		/*
		sql.append(" SELECT value((SELECT BANK_BRANCH.BRBK_NAME FROM BANK_BRANCH WHERE BANK_BRANCH.BRBK_ID = BANK_GROUP.OPBK_ID   ),'無此分行') AS OPBK_NAME ");
		sql.append(" , value((SELECT BANK_BRANCH.BRBK_NAME FROM BANK_BRANCH WHERE BANK_BRANCH.BRBK_ID = BANK_GROUP.CTBK_ID   ) ,'無此分行')AS CTBK_NAME ");
		sql.append(" ,BGBK_ID,BGBK_NAME,CTBK_ACCT,TCH_ID,OPBK_ID,CTBK_ID,ACTIVE_DATE,STOP_DATE,SND_FEE_BRBK_ID,OUT_FEE_BRBK_ID,IN_FEE_BRBK_ID,EDDA_FLAG,CDATE,UDATE") ;
		sql.append(" ,(CASE WHEN (BGBK_ATTR = '0') THEN '本國銀行'  WHEN (BGBK_ATTR = '1') THEN '地區企銀'  WHEN (BGBK_ATTR = '2') THEN '合作社'  WHEN (BGBK_ATTR = '3') THEN '農漁會'" );
		sql.append(" WHEN (BGBK_ATTR = '4') THEN '外商銀行' WHEN (BGBK_ATTR = '5') THEN '共用中心'  WHEN (BGBK_ATTR = '6') THEN '票交分所' ELSE '未定義屬性' END) AS BGBK_ATTR ");
		sql.append(" FROM BANK_GROUP ");
		sql.append(" WHERE BANK_GROUP.BGBK_ID = :bgbkId");
		*/
		System.out.println("getDataByBgbkId.sql>>"+sql);
		List<BANK_GROUP> list = null;
		try {
			SQLQuery query = getSession().createSQLQuery(sql.toString());
			query.setParameter("bgbkId", bgbkId);
			String cols = " OPBK_NAME, CTBK_NAME ,BGBK_ID,BGBK_NAME,BGBK_ATTR,CTBK_ACCT,TCH_ID,OPBK_ID,CTBK_ID,ACTIVE_DATE,STOP_DATE,SND_FEE_BRBK_ID,OUT_FEE_BRBK_ID,IN_FEE_BRBK_ID,EDDA_FLAG,CDATE,UDATE , OP_START_DATE , CT_START_DATE , HR_UPLOAD_MAX_FILE , OP_NOTE , CT_NOTE , IS_EACH " ;
			String[] objs =  cols.split(",");
			AutoAddScalar addscalar = new AutoAddScalar();
			addscalar.addScalar(query, BANK_GROUP.class, objs);
			query.setResultTransformer(Transformers.aliasToBean(BANK_GROUP.class));
			list = query.list();
			list = list == null? null:list.size() == 0? null:list;
		 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	public List<BANK_GROUP> getDataFromMasterByBgbkId(String bgbkId){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ");
//		sql.append("BGBK_ID,BGBK_NAME,CTBK_ACCT,TCH_ID,OPBK_ID,CTBK_ID,ACTIVE_DATE,STOP_DATE,SND_FEE_BRBK_ID,OUT_FEE_BRBK_ID,IN_FEE_BRBK_ID,EDDA_FLAG,CDATE,UDATE, ");
		sql.append("BGBK_ID,BGBK_NAME,CTBK_ACCT,TCH_ID,ACTIVE_DATE,STOP_DATE,SND_FEE_BRBK_ID,OUT_FEE_BRBK_ID,IN_FEE_BRBK_ID,WO_FEE_BRBK_ID,EDDA_FLAG,CDATE,UDATE,IS_EACH ,FILE_MAX_CNT");
		sql.append(" , (CASE BGBK_ATTR WHEN '0' THEN '本國銀行' WHEN '1' THEN '地區企銀' WHEN '2' THEN '合作社' WHEN '3' THEN '農漁會' WHEN '4' THEN '外商銀行' WHEN '5' THEN '共用中心' WHEN '6' THEN '票交分所' ELSE '未定義屬性' END) AS BGBK_ATTR, ");
		sql.append("COALESCE( ");
		sql.append("    (SELECT B.BGBK_NAME FROM MASTER_BANK_GROUP B WHERE B.BGBK_ID = EACHUSER.GET_CUR_OPBKID (A.BGBK_ID , '' , 0 ) ), '' ");
		sql.append(") AS OPBK_NAME, ");
		sql.append("COALESCE( ");
		sql.append("    (SELECT B.BGBK_NAME FROM MASTER_BANK_GROUP B WHERE B.BGBK_ID = EACHUSER.GET_CUR_CTBKID (A.BGBK_ID , '' , 0 ) ), '' ");
		sql.append(") AS CTBK_NAME ");
//		20150810 add by hugo 因應變更操作行、清算行
		sql.append(",  COALESCE( EACHUSER.GET_CUR_OPBKID (BGBK_ID , '' , 0 ) , '') AS OPBK_ID ");
		sql.append(",  COALESCE( EACHUSER.GET_CUR_CTBKID (BGBK_ID , '' , 0 ) , '') AS CTBK_ID ");
		sql.append(",  COALESCE( EACHUSER.GET_CUR_OPBKID (BGBK_ID , '',1 ) , '') AS OP_START_DATE  ");
		sql.append(", COALESCE( EACHUSER.GET_CUR_CTBKID (BGBK_ID , '' ,1) , '') AS CT_START_DATE ");
		sql.append(",  COALESCE( EACHUSER.GET_CUR_OPBKID (BGBK_ID , '',2 ) , '') AS OP_NOTE  ");
		sql.append(", COALESCE( EACHUSER.GET_CUR_CTBKID (BGBK_ID , '' ,2) , '') AS CT_NOTE ");
		sql.append(", COALESCE( HR_UPLOAD_MAX_FILE , 0) AS HR_UPLOAD_MAX_FILE  ");
		
		
		sql.append("FROM MASTER_BANK_GROUP A ");
		sql.append("WHERE A.BGBK_ID = :bgbkId ");
		/*
		sql.append(" SELECT value((SELECT BANK_BRANCH.BRBK_NAME FROM BANK_BRANCH WHERE BANK_BRANCH.BRBK_ID = BANK_GROUP.OPBK_ID   ),'無此分行') AS OPBK_NAME ");
		sql.append(" , value((SELECT BANK_BRANCH.BRBK_NAME FROM BANK_BRANCH WHERE BANK_BRANCH.BRBK_ID = BANK_GROUP.CTBK_ID   ) ,'無此分行')AS CTBK_NAME ");
		sql.append(" ,BGBK_ID,BGBK_NAME,CTBK_ACCT,TCH_ID,OPBK_ID,CTBK_ID,ACTIVE_DATE,STOP_DATE,SND_FEE_BRBK_ID,OUT_FEE_BRBK_ID,IN_FEE_BRBK_ID,EDDA_FLAG,CDATE,UDATE") ;
		sql.append(" ,(CASE WHEN (BGBK_ATTR = '0') THEN '本國銀行'  WHEN (BGBK_ATTR = '1') THEN '地區企銀'  WHEN (BGBK_ATTR = '2') THEN '合作社'  WHEN (BGBK_ATTR = '3') THEN '農漁會'" );
		sql.append(" WHEN (BGBK_ATTR = '4') THEN '外商銀行' WHEN (BGBK_ATTR = '5') THEN '共用中心'  WHEN (BGBK_ATTR = '6') THEN '票交分所' ELSE '未定義屬性' END) AS BGBK_ATTR ");
		sql.append(" FROM BANK_GROUP ");
		sql.append(" WHERE BANK_GROUP.BGBK_ID = :bgbkId");
		 */
		System.out.println("getDataByBgbkId.sql>>"+sql);
		List<BANK_GROUP> list = null;
		try {
			SQLQuery query = getSession().createSQLQuery(sql.toString());
			query.setParameter("bgbkId", bgbkId);
			String cols = " OPBK_NAME, CTBK_NAME ,BGBK_ID,BGBK_NAME,BGBK_ATTR,CTBK_ACCT,TCH_ID,OPBK_ID,CTBK_ID,ACTIVE_DATE,STOP_DATE,SND_FEE_BRBK_ID,OUT_FEE_BRBK_ID,IN_FEE_BRBK_ID,WO_FEE_BRBK_ID,EDDA_FLAG,CDATE,UDATE , OP_START_DATE , CT_START_DATE , HR_UPLOAD_MAX_FILE , OP_NOTE , CT_NOTE , IS_EACH,FILE_MAX_CNT " ;
			String[] objs =  cols.split(",");
			AutoAddScalar addscalar = new AutoAddScalar();
			addscalar.addScalar(query, BANK_GROUP.class, objs);
			query.setResultTransformer(Transformers.aliasToBean(BANK_GROUP.class));
			list = query.list();
			list = list == null? null:list.size() == 0? null:list;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 查詢 BANK_GROUP(VIEW)
	 * @return
	 */
	public List<BANK_GROUP> getAllData(){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ");
//		sql.append("BGBK_ID,BGBK_NAME,CTBK_ACCT,TCH_ID,OPBK_ID,CTBK_ID,ACTIVE_DATE,STOP_DATE,SND_FEE_BRBK_ID,OUT_FEE_BRBK_ID,IN_FEE_BRBK_ID,EDDA_FLAG,CDATE,UDATE, ");
		sql.append("BGBK_ID,BGBK_NAME,CTBK_ACCT,TCH_ID,ACTIVE_DATE,STOP_DATE,SND_FEE_BRBK_ID,OUT_FEE_BRBK_ID,IN_FEE_BRBK_ID,EDDA_FLAG,CDATE,UDATE, ");
		sql.append("(CASE BGBK_ATTR WHEN '0' THEN '本國銀行' WHEN '1' THEN '地區企銀' WHEN '2' THEN '合作社' WHEN '3' THEN '農漁會' WHEN '4' THEN '外商銀行' WHEN '5' THEN '共用中心' WHEN '6' THEN '票交分所' ELSE '未定義屬性' END) AS BGBK_ATTR, ");
		sql.append("COALESCE( ");
		sql.append("    (SELECT B.BGBK_NAME FROM BANK_GROUP B WHERE B.BGBK_ID =  EACHUSER.GET_CUR_OPBKID (A.BGBK_ID , '' , 0 )), '' ");
		sql.append(") AS OPBK_NAME, ");
		sql.append("COALESCE( ");
		sql.append("    (SELECT B.BGBK_NAME FROM BANK_GROUP B WHERE B.BGBK_ID =  EACHUSER.GET_CUR_CTBKID (A.BGBK_ID , '' , 0)), '' ");
		sql.append(") AS CTBK_NAME ");
//		20150810 add by hugo 因應變更操作行、清算行
		sql.append(",  COALESCE( EACHUSER.GET_CUR_OPBKID (BGBK_ID , ''  , 0) , '') AS OPBK_ID ");
		sql.append(",  COALESCE( EACHUSER.GET_CUR_CTBKID (BGBK_ID , ''  , 0) , '') AS CTBK_ID ");
		sql.append(",  COALESCE( EACHUSER.GET_CUR_OPBKID (BGBK_ID , '' ,1 ) , '') AS OP_START_DATE ");
		sql.append(",  COALESCE( EACHUSER.GET_CUR_CTBKID (BGBK_ID , '' ,1 ) , '') AS CT_START_DATE ");
		sql.append("FROM BANK_GROUP A ");
		sql.append("ORDER BY A.BGBK_ID ");
		/*
		sql.append(" SELECT value((SELECT BANK_BRANCH.BRBK_NAME FROM BANK_BRANCH WHERE BANK_BRANCH.BRBK_ID = BANK_GROUP.OPBK_ID  ),'無此分行') AS OPBK_NAME ");
		sql.append(" , value((SELECT BANK_BRANCH.BRBK_NAME FROM BANK_BRANCH WHERE BANK_BRANCH.BRBK_ID = BANK_GROUP.CTBK_ID   ) ,'無此分行')AS CTBK_NAME ");
		sql.append(" ,BGBK_ID,BGBK_NAME,CTBK_ACCT,TCH_ID,OPBK_ID,CTBK_ID,ACTIVE_DATE,STOP_DATE,SND_FEE_BRBK_ID,OUT_FEE_BRBK_ID,IN_FEE_BRBK_ID,EDDA_FLAG,CDATE,UDATE") ;
		sql.append(" ,(CASE WHEN (BGBK_ATTR = '0') THEN '本國銀行'  WHEN (BGBK_ATTR = '1') THEN '地區企銀'  WHEN (BGBK_ATTR = '2') THEN '合作社'  WHEN (BGBK_ATTR = '3') THEN '農漁會'" );
		sql.append(" WHEN (BGBK_ATTR = '4') THEN '外商銀行' WHEN (BGBK_ATTR = '5') THEN '共用中心'  WHEN (BGBK_ATTR = '6') THEN '票交分所' ELSE '未定義屬性' END) AS BGBK_ATTR ");
		sql.append(" FROM BANK_GROUP ORDER BY BANK_GROUP.BGBK_ID ");
		*/
		//System.out.println(sql.toString());
		List<BANK_GROUP> list = null;
		try {
			SQLQuery query = getSession().createSQLQuery(sql.toString());
			String cols = " OPBK_NAME, CTBK_NAME ,BGBK_ID,BGBK_NAME,BGBK_ATTR,CTBK_ACCT,TCH_ID,OPBK_ID,CTBK_ID,ACTIVE_DATE,STOP_DATE,SND_FEE_BRBK_ID,OUT_FEE_BRBK_ID,IN_FEE_BRBK_ID,EDDA_FLAG,CDATE,UDATE,OP_START_DATE,CT_START_DATE" ;
			String[] objs =  cols.split(",");
			AutoAddScalar addscalar = new AutoAddScalar();
			addscalar.addScalar(query, BANK_GROUP.class, objs);
			query.setResultTransformer(Transformers.aliasToBean(BANK_GROUP.class));
			list = query.list();
			list = list.size()==0 ? null :list;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 查詢 MASTER_BANK_GROUP
	 * @return
	 */
	public List<BANK_GROUP> getAllDataFromMaster(){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ");
//		sql.append("BGBK_ID,BGBK_NAME,CTBK_ACCT,TCH_ID,OPBK_ID,CTBK_ID,ACTIVE_DATE,STOP_DATE,SND_FEE_BRBK_ID,OUT_FEE_BRBK_ID,IN_FEE_BRBK_ID,EDDA_FLAG,CDATE,UDATE, ");
		sql.append("BGBK_ID,BGBK_NAME,CTBK_ACCT,TCH_ID,ACTIVE_DATE,STOP_DATE,SND_FEE_BRBK_ID,OUT_FEE_BRBK_ID,IN_FEE_BRBK_ID,WO_FEE_BRBK_ID,EDDA_FLAG,CDATE,UDATE,FILE_MAX_CNT, ");
		sql.append("(CASE BGBK_ATTR WHEN '0' THEN '本國銀行' WHEN '1' THEN '地區企銀' WHEN '2' THEN '合作社' WHEN '3' THEN '農漁會' WHEN '4' THEN '外商銀行' WHEN '5' THEN '共用中心' WHEN '6' THEN '票交分所' ELSE '未定義屬性' END) AS BGBK_ATTR, ");
		sql.append("COALESCE( ");
		sql.append("    (SELECT B.BGBK_NAME FROM MASTER_BANK_GROUP B WHERE B.BGBK_ID =  EACHUSER.GET_CUR_OPBKID (A.BGBK_ID , '' , 0 )), '' ");
		sql.append(") AS OPBK_NAME, ");
		sql.append("COALESCE( ");
		sql.append("    (SELECT B.BGBK_NAME FROM MASTER_BANK_GROUP B WHERE B.BGBK_ID =  EACHUSER.GET_CUR_CTBKID (A.BGBK_ID , '' , 0)), '' ");
		sql.append(") AS CTBK_NAME ");
//		20150810 add by hugo 因應變更操作行、清算行
		sql.append(",  COALESCE( EACHUSER.GET_CUR_OPBKID (BGBK_ID , ''  , 0) , '') AS OPBK_ID ");
		sql.append(",  COALESCE( EACHUSER.GET_CUR_CTBKID (BGBK_ID , ''  , 0) , '') AS CTBK_ID ");
		sql.append(",  COALESCE( EACHUSER.GET_CUR_OPBKID (BGBK_ID , '' ,1 ) , '') AS OP_START_DATE ");
		sql.append(",  COALESCE( EACHUSER.GET_CUR_CTBKID (BGBK_ID , '' ,1 ) , '') AS CT_START_DATE ");
		sql.append("FROM MASTER_BANK_GROUP A ");
		sql.append("ORDER BY A.BGBK_ID ");
		//System.out.println(sql.toString());
		List<BANK_GROUP> list = null;
		try {
			SQLQuery query = getSession().createSQLQuery(sql.toString());
			String cols = " OPBK_NAME, CTBK_NAME ,BGBK_ID,BGBK_NAME,BGBK_ATTR,CTBK_ACCT,TCH_ID,OPBK_ID,CTBK_ID,ACTIVE_DATE,STOP_DATE,SND_FEE_BRBK_ID,OUT_FEE_BRBK_ID,IN_FEE_BRBK_ID,WO_FEE_BRBK_ID,EDDA_FLAG,CDATE,UDATE,OP_START_DATE,CT_START_DATE,FILE_MAX_CNT" ;
			String[] objs =  cols.split(",");
			AutoAddScalar addscalar = new AutoAddScalar();
			addscalar.addScalar(query, BANK_GROUP.class, objs);
			query.setResultTransformer(Transformers.aliasToBean(BANK_GROUP.class));
			list = query.list();
			list = list.size()==0 ? null :list;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	public boolean saveData(BANK_GROUP bg, List<BANK_GROUP_BUSINESS> bgbList_toDelete, List<BANK_GROUP_BUSINESS> bgbList_toInsert, List<BANK_OPBK> opbkList , BANK_OPBK opbk,List<BANK_CTBK> ctbkList ,  BANK_CTBK ctbk , BANKSYSSTATUSTAB sysPO ,BANKAPSTATUSTAB apPO ,CR_LINE cr_line ){
		boolean result = false;
		Session session = getSessionFactory().openSession();
		session.beginTransaction();
		String hql = " UPDATE tw.org.twntch.po.BANK_BRANCH BR SET BR.STOP_DATE = :STOP_DATE WHERE BR.id.BGBK_ID =:BGBK_ID AND BR.SYNCSPDATE = 'Y' ";
		try {
			//Delete from BANK_GROUP_BUSINESS
			for(int i = 0; i < bgbList_toDelete.size(); i++){
				session.delete(bgbList_toDelete.get(i));
			}
			//Insert to BANK_GROUP_BUSINESS
			for(int i = 0; i < bgbList_toInsert.size(); i++){
				bgbList_toInsert.get(i).setCDATE(zDateHandler.getTheDateII());
				//session.saveOrUpdate(bgbList_toInsert.get(i));
				session.merge(bgbList_toInsert.get(i));
			}
			
//			20150129 add by hugo 要順便更新分行的停用日期(有設定與總行同步的分行)
			Query query = session.createQuery(hql);
			query.setParameter("BGBK_ID", bg.getBGBK_ID());
			query.setParameter("STOP_DATE", bg.getSTOP_DATE());
			int branchresult =  query.executeUpdate();
			logger.debug("更新分行停用日期筆數>>"+branchresult);
			
			if(opbkList !=null){
				for(BANK_OPBK po: opbkList){
					session.delete(po);
				}
			}
			if(ctbkList !=null){
				for(BANK_CTBK po: ctbkList){
					session.delete(po);
				}
			}
			
			//Update BANK_GROUP
			session.saveOrUpdate(bg);
			//UPDATE BANK_OPBK
			if(opbk != null){
				session.saveOrUpdate(opbk);
			}
			//UPDATE BANK_CTBK
			if(ctbk != null){
				session.saveOrUpdate(ctbk);
			}
			
//			20150618 add by hugo
			if(sysPO != null){
				session.saveOrUpdate(sysPO);
			}
			if(apPO != null){
				session.saveOrUpdate(apPO);
			}
			if(cr_line != null){
				session.saveOrUpdate(cr_line);
			}
			session.beginTransaction().commit();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			session.beginTransaction().rollback();
			result = false;
		}
		return result;
	}
//	public boolean saveData(BANK_GROUP bg, List<BANK_GROUP_BUSINESS> bgbList_toDelete, List<BANK_GROUP_BUSINESS> bgbList_toInsert, List<BANK_OPBK> opbkList, BANK_OPBK opbk, List<BANK_CTBK> ctbkList, BANK_CTBK ctbk){
//		boolean result = false;
//		Session session = getSessionFactory().openSession();
//		session.beginTransaction();
//		String hql = " UPDATE tw.org.twntch.po.BANK_BRANCH BR SET BR.STOP_DATE = :STOP_DATE WHERE BR.id.BGBK_ID =:BGBK_ID AND BR.SYNCSPDATE = 'Y' ";
//		try {
//			//Delete from BANK_GROUP_BUSINESS
//			for(int i = 0; i < bgbList_toDelete.size(); i++){
//				session.delete(bgbList_toDelete.get(i));
//			}
//			//Insert to BANK_GROUP_BUSINESS
//			for(int i = 0; i < bgbList_toInsert.size(); i++){
//				bgbList_toInsert.get(i).setCDATE(zDateHandler.getTheDateII());
//				//session.saveOrUpdate(bgbList_toInsert.get(i));
//				session.merge(bgbList_toInsert.get(i));
//			}
//			//update BANK_OPBK
//			if(opbkList != null){
//				for(BANK_OPBK op : opbkList){
//					session.merge(op);
//				}
//			}
//			//update BANK_CTBK
//			if(ctbkList != null){
//				for(BANK_CTBK ct : ctbkList){
//					session.merge(ct);
//				}
//			}
//			
////			20150129 add by hugo 要順便更新分行的停用日期(有設定與總行同步的分行)
//			Query query = session.createQuery(hql);
//			query.setParameter("BGBK_ID", bg.getBGBK_ID());
//			query.setParameter("STOP_DATE", bg.getSTOP_DATE());
//			int branchresult =  query.executeUpdate();
//			System.out.println("更新分行停用日期筆數>>"+branchresult);
//			//Update BANK_GROUP
//			session.saveOrUpdate(bg);
//			//UPDATE BANK_OPBK
//			if(opbk != null){
//				session.saveOrUpdate(opbk);
//			}
//			//UPDATE BANK_CTBK
//			if(ctbk != null){
//				session.saveOrUpdate(ctbk);
//			}
//			session.beginTransaction().commit();
//			result = true;
//		} catch (Exception e) {
//			e.printStackTrace();
//			session.beginTransaction().rollback();
//			result = false;
//		}
//		return result;
//	}
	
	
	
	
	public boolean add_saveData(BANK_GROUP bg, CR_LINE cr_line , BANKSYSSTATUSTAB sysPO , BANKAPSTATUSTAB apPO, List<BANK_GROUP_BUSINESS> bgbList, BANK_OPBK bankOpbk, BANK_CTBK bankCtbk){
		boolean result = false;
		Session session = getSessionFactory().openSession();
		session.beginTransaction();
		try {
			session.saveOrUpdate(bg);
			if(cr_line != null){
				session.saveOrUpdate(cr_line);
			}
			if(sysPO != null){
				session.saveOrUpdate(sysPO);
			}
			if(apPO != null){
				session.saveOrUpdate(apPO);
			}
			if(bankOpbk != null){
				session.saveOrUpdate(bankOpbk);
			}
			if(bankCtbk != null){
				session.saveOrUpdate(bankCtbk);
			}
			
			if(bgbList!=null){
				for( BANK_GROUP_BUSINESS po :bgbList){
//					故障測試
//					po = null;
					session.saveOrUpdate(po);
				}
			}
			session.beginTransaction().commit();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			session.beginTransaction().rollback();
			result = false;
		}
		return result;
	}
	
	public boolean delData(BANK_GROUP bg, List<BANK_GROUP_BUSINESS> bgbList_toDelete, List<BANK_OPBK> opbkList, List<BANK_CTBK> ctbkList){
		boolean result = false;
		Session session = getSessionFactory().openSession();
		session.beginTransaction();
		
		try {
			//Delete from BANK_GROUP_BUSINESS
			for(int i = 0; i < bgbList_toDelete.size(); i++){
				session.delete(bgbList_toDelete.get(i));
			}
			//Delete BANK_GROUP
			session.delete(bg);
			//Delete BANK_OPBK
			if(opbkList != null){
				for(BANK_OPBK op : opbkList){
					session.delete(op);
				}
			}
			//Delete BANK_CTBK
			if(ctbkList != null){
				for(BANK_CTBK ct : ctbkList){
					session.delete(ct);
				}
			}
			session.beginTransaction().commit();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			session.beginTransaction().rollback();
			result = false;
		}
		return result;
	}
	
	/*
	 * 20150625 edit by hugo req by UAT-2015018-02
	 * 清算行之資料應判斷目前日期是否落於銀行總行檔之啟用日期與停用日期之間
	 * 然後distinct 清算行代號與帶出操作行名稱(CTBK_ID、CTBK_NAME)、提供清算行選取.
	 * FOR "TURNON_BO"
	 */
	public List<BANK_GROUP> getctbkIdList(){
		List<BANK_GROUP> list = null;
		try{
			//取得前日期的民國年月日
			String sql = "SELECT A.CTBK_ID AS BGBK_ID, B.BGBK_NAME ";
			sql += "FROM BANK_GROUP A JOIN BANK_GROUP B ON A.CTBK_ID = B.BGBK_ID ";
			sql += "WHERE B.ACTIVE_DATE <> '' ";
			sql += "GROUP BY A.CTBK_ID, B.BGBK_NAME ORDER BY A.CTBK_ID";
			SQLQuery query = getCurrentSession().createSQLQuery(sql);
			query.addScalar("BGBK_ID", Hibernate.STRING)
			.addScalar("BGBK_NAME", Hibernate.STRING)
			.setResultTransformer(Transformers.aliasToBean(BANK_GROUP.class));
			list = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return list;
	}
	/*
	 * 20150625 note by hugo 從原getctbkIdList改名而來 備份用
	 * 清算行之資料應判斷目前日期是否落於銀行總行檔之啟用日期與停用日期之間
	 * 然後distinct 清算行代號與帶出操作行名稱(CTBK_ID、CTBK_NAME)、提供清算行選取.
	 * FOR "TURNON_BO"
	 */
	public List<BANK_GROUP> getctbkIdList_BAK(){
		List<BANK_GROUP> list = null;
		try{
			//取得前日期的民國年月日
			String nowDate = zDateHandler.getTWDate();
			String sql = "SELECT A.CTBK_ID AS BGBK_ID, B.BGBK_NAME ";
			sql += "FROM BANK_GROUP A JOIN BANK_GROUP B ON A.CTBK_ID = B.BGBK_ID ";
			sql += "WHERE B.ACTIVE_DATE <> '' AND '" + nowDate + "' BETWEEN B.ACTIVE_DATE AND ";
			sql += "CASE B.STOP_DATE WHEN '' THEN '" + nowDate + "' ELSE B.STOP_DATE END ";
			sql += "GROUP BY A.CTBK_ID, B.BGBK_NAME ORDER BY A.CTBK_ID";
			SQLQuery query = getCurrentSession().createSQLQuery(sql);
			query.addScalar("BGBK_ID", Hibernate.STRING)
			.addScalar("BGBK_NAME", Hibernate.STRING)
			.setResultTransformer(Transformers.aliasToBean(BANK_GROUP.class));
			list = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return list;
	}
	
	
	
	/*
	 * 若網頁功能為提供查詢歷史資料，則清算行清單不應過濾已停用的資料，則會查不到歷史資料
	 */
	public List<BANK_GROUP> getctbkIdList_all(){
		List<BANK_GROUP> list = null;
		try{
			//取得前日期的民國年月日
			String sql = "SELECT A.CTBK_ID AS BGBK_ID, B.BGBK_NAME ";
			sql += "FROM BANK_GROUP A JOIN BANK_GROUP B ON A.CTBK_ID = B.BGBK_ID ";
			sql += "GROUP BY A.CTBK_ID, B.BGBK_NAME ORDER BY A.CTBK_ID";
			SQLQuery query = getCurrentSession().createSQLQuery(sql);
			query.addScalar("BGBK_ID", Hibernate.STRING)
			.addScalar("BGBK_NAME", Hibernate.STRING)
			.setResultTransformer(Transformers.aliasToBean(BANK_GROUP.class));
			list = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return list;
	}
	
	public List<BANK_GROUP> getByOpbkidAndCtbkid(List<String> cons){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ");
//		sql.append("BGBK_ID,BGBK_NAME,CTBK_ACCT,TCH_ID,OPBK_ID,CTBK_ID,ACTIVE_DATE,STOP_DATE,SND_FEE_BRBK_ID,OUT_FEE_BRBK_ID,IN_FEE_BRBK_ID,EDDA_FLAG,CDATE,UDATE, ");
		sql.append("BGBK_ID,BGBK_NAME,CTBK_ACCT,TCH_ID,ACTIVE_DATE,STOP_DATE,SND_FEE_BRBK_ID,OUT_FEE_BRBK_ID,IN_FEE_BRBK_ID,EDDA_FLAG,CDATE,UDATE, ");
		sql.append("(CASE BGBK_ATTR WHEN '0' THEN '本國銀行' WHEN '1' THEN '地區企銀' WHEN '2' THEN '合作社' WHEN '3' THEN '農漁會' WHEN '4' THEN '外商銀行' WHEN '5' THEN '共用中心' WHEN '6' THEN '票交分所' ELSE '未定義屬性' END) AS BGBK_ATTR, ");
		sql.append("COALESCE( ");
		sql.append("    (SELECT B.BGBK_NAME FROM BANK_GROUP B WHERE B.BGBK_ID =  EACHUSER.GET_CUR_OPBKID (A.BGBK_ID , '' , 0)), '' ");
		sql.append(") AS OPBK_NAME, ");
		sql.append("COALESCE( ");
		sql.append("    (SELECT B.BGBK_NAME FROM BANK_GROUP B WHERE B.BGBK_ID =  EACHUSER.GET_CUR_CTBKID (A.BGBK_ID , '' , 0 )), '' ");
		sql.append(") AS CTBK_NAME ");
//		20150810 add by hugo 因應變更操作行、清算行
		sql.append(",  COALESCE( EACHUSER.GET_CUR_OPBKID (BGBK_ID , '' , 0 ) , '') AS OPBK_ID ");
		sql.append(",  COALESCE( EACHUSER.GET_CUR_CTBKID (BGBK_ID , '' , 0 ) , '') AS CTBK_ID ");
		sql.append(",  COALESCE( EACHUSER.GET_CUR_OPBKID (BGBK_ID , '' , 1 ) , '') AS OP_START_DATE ");
		sql.append(",  COALESCE( EACHUSER.GET_CUR_CTBKID (BGBK_ID , '' , 1 ) , '') AS CT_START_DATE ");
		sql.append("FROM BANK_GROUP A ");
		if(cons.size() > 0){
			sql.append("WHERE ");
			for(int i = 0; i < cons.size(); i++){
				sql.append(cons.get(i));
				if(i < cons.size() - 1){
					sql.append(" AND ");
				}
			}
		}
		sql.append("ORDER BY A.BGBK_ID ");
		System.out.println(sql.toString());
		List<BANK_GROUP> list = null;
		try {
			SQLQuery query = getSession().createSQLQuery(sql.toString());
			String cols = " OPBK_NAME, CTBK_NAME ,BGBK_ID,BGBK_NAME,BGBK_ATTR,CTBK_ACCT,TCH_ID,OPBK_ID,CTBK_ID,ACTIVE_DATE,STOP_DATE,SND_FEE_BRBK_ID,OUT_FEE_BRBK_ID,IN_FEE_BRBK_ID,EDDA_FLAG,CDATE,UDATE , OP_START_DATE , CT_START_DATE" ;
			String[] objs =  cols.split(",");
			AutoAddScalar addscalar = new AutoAddScalar();
			addscalar.addScalar(query, BANK_GROUP.class, objs);
			query.setResultTransformer(Transformers.aliasToBean(BANK_GROUP.class));
			list = query.list();
			list = list.size()==0 ? null :list;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	
	/**
	 * 查詢MASTER_BANK_GROUP
	 * @param cons
	 * @return
	 */
	public List<BANK_GROUP> getByOpbkidAndCtbkidFromMaster(List<String> cons , String orderSQL){
		StringBuffer sql = new StringBuffer();
		sql.append("WITH TEMP AS( ");
		sql.append("SELECT ");
//		sql.append("BGBK_ID,BGBK_NAME,CTBK_ACCT,TCH_ID,OPBK_ID,CTBK_ID,ACTIVE_DATE,STOP_DATE,SND_FEE_BRBK_ID,OUT_FEE_BRBK_ID,IN_FEE_BRBK_ID,EDDA_FLAG,CDATE,UDATE, ");
		sql.append("BGBK_ID,BGBK_NAME,CTBK_ACCT,TCH_ID,ACTIVE_DATE,STOP_DATE,SND_FEE_BRBK_ID,OUT_FEE_BRBK_ID,IN_FEE_BRBK_ID,WO_FEE_BRBK_ID,EDDA_FLAG,CDATE,UDATE, ");
		sql.append("(CASE BGBK_ATTR WHEN '0' THEN '本國銀行' WHEN '1' THEN '地區企銀' WHEN '2' THEN '合作社' WHEN '3' THEN '農漁會' WHEN '4' THEN '外商銀行' WHEN '5' THEN '共用中心' WHEN '6' THEN '票交分所' ELSE '未定義屬性' END) AS BGBK_ATTR, ");
		sql.append("COALESCE( ");
		sql.append("    (SELECT B.BGBK_NAME FROM MASTER_BANK_GROUP B WHERE B.BGBK_ID =  EACHUSER.GET_CUR_OPBKID (A.BGBK_ID , '' , 0)), '' ");
		sql.append(") AS OPBK_NAME, ");
		sql.append("COALESCE( ");
		sql.append("    (SELECT B.BGBK_NAME FROM MASTER_BANK_GROUP B WHERE B.BGBK_ID =  EACHUSER.GET_CUR_CTBKID (A.BGBK_ID , '' , 0 )), '' ");
		sql.append(") AS CTBK_NAME ");
//		20150810 add by hugo 因應變更操作行、清算行
		sql.append(",  COALESCE( EACHUSER.GET_CUR_OPBKID (BGBK_ID , '' , 0 ) , '') AS OPBK_ID ");
		sql.append(",  COALESCE( EACHUSER.GET_CUR_CTBKID (BGBK_ID , '' , 0 ) , '') AS CTBK_ID ");
		sql.append(",  COALESCE( EACHUSER.GET_CUR_OPBKID (BGBK_ID , '' , 1 ) , '') AS OP_START_DATE ");
		sql.append(",  COALESCE( EACHUSER.GET_CUR_CTBKID (BGBK_ID , '' , 1 ) , '') AS CT_START_DATE ");
		sql.append(",  (CASE IS_EACH WHEN 'Y' THEN '是' ELSE '' END) AS IS_EACH  ");
		sql.append("FROM MASTER_BANK_GROUP A ");
		sql.append(" ) ");
		sql.append(" SELECT * FROM TEMP ");
		
		if(cons.size() > 0){
			sql.append("WHERE ");
			for(int i = 0; i < cons.size(); i++){
				sql.append("TEMP."+cons.get(i));
				if(i < cons.size() - 1){
					sql.append(" AND ");
				}
			}
		}
//		sql.append("ORDER BY TEMP.BGBK_ID ");
		sql.append(orderSQL);
		System.out.println(sql.toString());
		List<BANK_GROUP> list = null;
		try {
			SQLQuery query = getSession().createSQLQuery(sql.toString());
			String cols = " OPBK_NAME, CTBK_NAME ,BGBK_ID,BGBK_NAME,BGBK_ATTR,CTBK_ACCT,TCH_ID,OPBK_ID,CTBK_ID,ACTIVE_DATE,STOP_DATE,SND_FEE_BRBK_ID,OUT_FEE_BRBK_ID,IN_FEE_BRBK_ID,WO_FEE_BRBK_ID,EDDA_FLAG,CDATE,UDATE , OP_START_DATE , CT_START_DATE , IS_EACH" ;
			String[] objs =  cols.split(",");
			AutoAddScalar addscalar = new AutoAddScalar();
			addscalar.addScalar(query, BANK_GROUP.class, objs);
			query.setResultTransformer(Transformers.aliasToBean(BANK_GROUP.class));
			list = query.list();
			list = list.size()==0 ? null :list;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	public List<BANK_GROUP> getByOpbkid(String opbkId){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ");
		sql.append("BGBK_ID,BGBK_NAME,CTBK_ACCT,TCH_ID,OPBK_ID,CTBK_ID,ACTIVE_DATE,STOP_DATE,SND_FEE_BRBK_ID,OUT_FEE_BRBK_ID,IN_FEE_BRBK_ID,EDDA_FLAG,CDATE,UDATE, ");
		sql.append("(CASE BGBK_ATTR WHEN '0' THEN '本國銀行' WHEN '1' THEN '地區企銀' WHEN '2' THEN '合作社' WHEN '3' THEN '農漁會' WHEN '4' THEN '外商銀行' WHEN '5' THEN '共用中心' WHEN '6' THEN '票交分所' ELSE '未定義屬性' END) AS BGBK_ATTR, ");
		sql.append("COALESCE( ");
		sql.append("    (SELECT B.BGBK_NAME FROM BANK_GROUP B WHERE B.BGBK_ID = A.OPBK_ID), ");
		sql.append("    COALESCE((SELECT B.BRBK_NAME FROM BANK_BRANCH B WHERE B.BRBK_ID = A.OPBK_ID), '') ");
		sql.append(") AS OPBK_NAME, ");
		sql.append("COALESCE( ");
		sql.append("    (SELECT B.BGBK_NAME FROM BANK_GROUP B WHERE B.BGBK_ID = A.CTBK_ID), ");
		sql.append("    COALESCE((SELECT B.BRBK_NAME FROM BANK_BRANCH B WHERE B.BRBK_ID = A.CTBK_ID), '') ");
		sql.append(") AS CTBK_NAME ");
		sql.append("FROM BANK_GROUP A ");
		sql.append(" WHERE ");
		sql.append(" OPBK_ID = '" + opbkId + "' ");
		sql.append("ORDER BY A.BGBK_ID ");
		System.out.println(sql.toString());
		List<BANK_GROUP> list = null;
		try {
			SQLQuery query = getSession().createSQLQuery(sql.toString());
			String cols = " OPBK_NAME, CTBK_NAME ,BGBK_ID,BGBK_NAME,BGBK_ATTR,CTBK_ACCT,TCH_ID,OPBK_ID,CTBK_ID,ACTIVE_DATE,STOP_DATE,SND_FEE_BRBK_ID,OUT_FEE_BRBK_ID,IN_FEE_BRBK_ID,EDDA_FLAG,CDATE,UDATE" ;
			String[] objs =  cols.split(",");
			AutoAddScalar addscalar = new AutoAddScalar();
			addscalar.addScalar(query, BANK_GROUP.class, objs);
			query.setResultTransformer(Transformers.aliasToBean(BANK_GROUP.class));
			list = query.list();
			list = list.size()==0 ? null :list;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	public List<BANK_GROUP> getTchIdList(){
		List<BANK_GROUP> list = null;
		String sql = "SELECT DISTINCT TCH_ID FROM EACHUSER.BANK_GROUP WHERE LENGTH(RTRIM(COALESCE(TCH_ID, ''))) <> 0";
		try{
			SQLQuery query = getCurrentSession().createSQLQuery(sql);
			query.addScalar("TCH_ID");
			query.setResultTransformer(Transformers.aliasToBean(BANK_GROUP.class));
			list = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return list;
	}
	
	public void creatWK(){
		int monthCount[] = {31,28,31,30,31,30,31,31,30,31,30,31};
		int initWD = 3;
		String isTxnDate = "";
		String sql = "";
		for(int i = 1; i <= 12; i++){
			for(int j = 1; j <= monthCount[i-1]; j++){
				if(initWD == 6 || initWD == 7){
					isTxnDate = "N";
				}else{
					isTxnDate= "Y";
				}
				sql = "INSERT INTO WK_DATE_CALENDAR VALUES ('0103" + padding(i) + padding(j) + "'," +initWD +",'"+isTxnDate+"',null,null)";
				System.out.println(sql);
				
				if(initWD == 7){
					initWD = 1;
				}else{
					initWD++;
				}
			}
		}
	}
	
	public String padding(int input){
		String pad = "";
		int count = 2 - String.valueOf(input).length();
		for(int i = 0; i < count; i++){
			pad += "0";
		}
		return pad + input;
	}
	
	public List<BANK_GROUP> getfeeBranch(String bgbkId){
		StringBuffer sql = new StringBuffer();
		
		sql.append("SELECT SND_FEE_BRBK_ID,OUT_FEE_BRBK_ID,IN_FEE_BRBK_ID FROM BANK_GROUP ");
		sql.append("WHERE BGBK_ID = :bgbkId");
		List<BANK_GROUP> list = null;
		try{
			SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
			query.setParameter("bgbkId", bgbkId);
			query.addScalar("SND_FEE_BRBK_ID")
			.addScalar("OUT_FEE_BRBK_ID")
			.addScalar("IN_FEE_BRBK_ID");
			query.setResultTransformer(Transformers.aliasToBean(BANK_GROUP.class));
			list = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return list;
	}
}
