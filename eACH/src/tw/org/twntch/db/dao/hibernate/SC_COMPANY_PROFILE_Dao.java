package tw.org.twntch.db.dao.hibernate;

import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;

import tw.org.twntch.po.SC_COMPANY_PROFILE;
import tw.org.twntch.po.SC_COMPANY_PROFILE_HIS;
import tw.org.twntch.po.SC_COMPANY_PROFILE_PK;
import tw.org.twntch.util.AutoAddScalar;
import tw.org.twntch.util.StrUtils;

public class SC_COMPANY_PROFILE_Dao extends HibernateEntityDao<SC_COMPANY_PROFILE, SC_COMPANY_PROFILE_PK> {

	/**
	 * 實驗用，未完成
	 * 
	 * @param <T>
	 * @param <T>
	 * @param companyId
	 * @param cls
	 * @return
	 */
	public <T> T getCompanyDataByCompanyId(String companyId, T po) {
		// Class<T> po = null;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM ( ");
		sql.append(
				"	SELECT COMPANY_ID, COMPANY_ABBR_NAME, COMPANY_NAME FROM SC_COMPANY_PROFILE WHERE COMPANY_ID = ? ");
		sql.append("	UNION ");
		sql.append(
				"	SELECT COMPANY_ID, COMPANY_ABBR_NAME, COMPANY_NAME FROM SD_COMPANY_PROFILE WHERE COMPANY_ID = ? ");
		sql.append("	FETCH FIRST 1 ROWS ONLY ");
		sql.append(") ");

		try {
			SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
			query.setParameter(0, companyId);
			query.setParameter(1, companyId);
			String[] cols = { "COMPANY_ID", "COMPANY_ABBR_NAME", "COMPANY_NAME" };
			// AutoAddScalar addScalar = new AutoAddScalar();
			// addScalar.addScalar(query, po, cols);
			query.setResultTransformer(Transformers.aliasToBean(po.getClass()));
			List<T> list = query.list();
			if (list != null && list.size() > 0) {
				po = list.get(0);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.debug("getCompanyDataByCompanyId.Exception>>" + e);
		}
		return po;
	}

	// 收費業者關聯檔只抓繳費類的(交易代號=930)
	public <T> T getCompanyDataByCompanyIdOnly930(String companyId, T po) {
		// Class<T> po = null;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM ( ");
		sql.append(
				"	SELECT COMPANY_ID, COMPANY_ABBR_NAME, COMPANY_NAME FROM SC_COMPANY_PROFILE WHERE COMPANY_ID = ? AND TXN_ID = '930' ");
		sql.append("	UNION ");
		sql.append(
				"	SELECT COMPANY_ID, COMPANY_ABBR_NAME, COMPANY_NAME FROM SD_COMPANY_PROFILE WHERE COMPANY_ID = ? AND TXN_ID = '930' ");
		sql.append("	FETCH FIRST 1 ROWS ONLY ");
		sql.append(") ");

		try {
			SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
			query.setParameter(0, companyId);
			query.setParameter(1, companyId);
			String[] cols = { "COMPANY_ID", "COMPANY_ABBR_NAME", "COMPANY_NAME" };
			// AutoAddScalar addScalar = new AutoAddScalar();
			// addScalar.addScalar(query, po, cols);
			query.setResultTransformer(Transformers.aliasToBean(po.getClass()));
			List<T> list = query.list();
			if (list != null && list.size() > 0) {
				po = list.get(0);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.debug("getCompanyDataByCompanyId.Exception>>" + e);
		}
		return po;
	}

	/**
	 * 批次自動更新用
	 * 
	 * @param list
	 * @return
	 */
	public boolean batch_SaveData(List<SC_COMPANY_PROFILE> list) {
		boolean res = false;

		Session session = getSessionFactory().openSession();
		session.beginTransaction();
		SC_COMPANY_PROFILE_HIS his = null ;
		try {
			
			
			for (SC_COMPANY_PROFILE po : list) {
				his = new SC_COMPANY_PROFILE_HIS();
				his.setCOMPANY_ID(po.getId().getCOMPANY_ID());
				his.setSND_BRBK_ID(po.getId().getSND_BRBK_ID());
				his.setTXN_ID(po.getId().getTXN_ID());
				his.setFEE_TYPE(po.getFEE_TYPE());
				his.setACTIVE_DATE(po.getFEE_TYPE_ACTIVE_DATE());
				session.saveOrUpdate(po);
				session.saveOrUpdate(his);
			}
			session.beginTransaction().commit();
			res = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.debug("batch_SaveData.Exception>>" + e);
			session.beginTransaction().rollback();
			throw e;
		}

		return res;
	}

	/**
	 * 從交易檔取得新的代付業者
	 * 
	 * @param sbizdate
	 * @param ebizdate
	 * @return
	 */
	public List<SC_COMPANY_PROFILE_PK> getNewSC_Data(String sbizdate, String ebizdate) {
		List<SC_COMPANY_PROFILE_PK> list = null;
		// List<Map<String,String>> tmplist = null;
		// SC_COMPANY_PROFILE_PK id = null;
		StringBuffer sql = new StringBuffer();
		sql.append(
				" SELECT COALESCE(TMP.SENDERID,'') AS COMPANY_ID ,COALESCE(TMP.TXID ,'') AS TXN_ID , COALESCE(TMP.SENDERBANKID ,'') AS SND_BRBK_ID  FROM ");
		sql.append(" ( ");
		sql.append(
				" SELECT DISTINCT OB.SENDERID ,OB.TXID , OB.SENDERBANKID ,SC.COMPANY_ID ,(SELECT TX.TXN_TYPE FROM EACHUSER.TXN_CODE TX WHERE TX.TXN_ID = OB.TXID  ) AS  TXN_TYPE ");
		sql.append(" FROM EACHUSER.ONBLOCKTAB  OB ");
		sql.append(
				" LEFT JOIN EACHUSER.ONPENDINGTAB OP ON OP.OBIZDATE = OB.BIZDATE AND OP.OSTAN = OB.STAN AND  COALESCE(OP.BIZDATE, '') <> '' AND COALESCE(OP.RESULTCODE ,'00') = '00' ");
		sql.append(
				" LEFT JOIN EACHUSER.SC_COMPANY_PROFILE SC ON SC.COMPANY_ID = OB.SENDERID  AND SC.TXN_ID = OB.TXID AND SC.SND_BRBK_ID = OB.SENDERBANKID  ");
		sql.append(
				" WHERE COALESCE(OB.GARBAGEDATA,'') <> '*' AND OB.RESULTSTATUS = 'A' AND OB.BIZDATE BETWEEN :sbizdate AND :ebizdate  ");
		sql.append(" ) AS TMP ");
		sql.append(
				" WHERE COALESCE(TMP.COMPANY_ID , '') = '' AND  COALESCE(TMP.SENDERID , '') <>'' AND  COALESCE(TMP.TXID , '') <>'' AND  COALESCE(TMP.SENDERBANKID , '') <>'' AND COALESCE(TMP.TXN_TYPE , '') = 'SC'  ");

		try {
			logger.debug("sbizdate>>" + sbizdate + ",ebizdate>>" + ebizdate);
			SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
			query.setParameter("sbizdate", sbizdate);
			query.setParameter("ebizdate", ebizdate);
			query.setResultTransformer(Transformers.aliasToBean(SC_COMPANY_PROFILE_PK.class));
			// query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			list = query.list();
			logger.debug("list>>>" + list);
			// tmplist = query.list();

			// if(tmplist!= null && tmplist.size() !=0 ){
			// for(Map map :tmplist){
			// id = new SC_COMPANY_PROFILE_PK();
			// BeanUtils.populate(id, map);
			// list.add(id);
			// }
			// }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.debug("getNewSC_Data.Exception>>" + e);
		}
		return list;

	}

	// public SC_COMPANY_PROFILE getDataByCompany_Id(String com_id ){
	// SC_COMPANY_PROFILE po = null ;
	// List<SC_COMPANY_PROFILE> list = null;
	//
	// String hql = " FROM tw.org.twntch.po.SC_COMPANY_PROFILE SC WHERE
	// SC.id.COMPANY_ID = :com_id "
	// + " ORDER BY SC.id.COMPANY_ID FETCH FIRST 1 ROWS ONLY";
	//
	// try {
	// Query query = getCurrentSession().createQuery(hql);
	// query.setParameter("com_id", com_id);
	// list = query.list();
	// if(list != null && list.size() == 0){
	// for( SC_COMPANY_PROFILE tmp_po :list){
	// po = tmp_po;
	// }
	// }
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// logger.debug("getDataByCompany_Id.Exception>>"+e);
	// throw e;
	// }
	// return po;
	//
	// }
	public SC_COMPANY_PROFILE getDataByCompany_Id(String com_id, String txn_id, String snd_brbk_id) {
		SC_COMPANY_PROFILE po = null;
		SC_COMPANY_PROFILE_PK id = null;
		List<SC_COMPANY_PROFILE> list = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT * FROM ");
		sql.append(" ( ");
		sql.append(" SELECT COALESCE(SC.COMPANY_ID , SD.COMPANY_ID , '' ) COMPANY_ID ");
		sql.append(" ,COALESCE( SC.COMPANY_ABBR_NAME , SD.COMPANY_ABBR_NAME , '') COMPANY_ABBR_NAME ");
		sql.append(" ,COALESCE( SC.COMPANY_NAME , SD.COMPANY_NAME , '') COMPANY_NAME ");
		sql.append(" ,SC.TXN_ID AS TXN_ID , SC.SND_BRBK_ID AS SND_BRBK_ID ");
		// 取消原因setResultTransformer CDATE 欄位會出錯 資料面也無使用到故註解
		// sql.append(" , SC.IPO_COMPANY_ID,SC.CDATE,SC.UDATE,SC.SYS_CDATE,SC.IS_SHORT
		// ");
		sql.append(" FROM SC_COMPANY_PROFILE SC ");
		sql.append(" FULL JOIN  SD_COMPANY_PROFILE SD ON SD.COMPANY_ID = SC.COMPANY_ID ");
		sql.append(" ) AS TMP ");
		sql.append(" WHERE TMP.COMPANY_ID = :com_id ");
		sql.append(" ORDER BY TMP.COMPANY_ID  FETCH FIRST 1 ROWS ONLY ");

		try {
			SQLQuery sqlquery = getCurrentSession().createSQLQuery(sql.toString());
			sqlquery.setParameter("com_id", com_id);
			sqlquery.setResultTransformer(Transformers.aliasToBean(SC_COMPANY_PROFILE.class));
			list = sqlquery.list();
			logger.debug("sqlquery.list()>>" + list);
			if (list != null && list.size() != 0) {
				for (SC_COMPANY_PROFILE tmp_po : list) {
					logger.debug("COMPANY_ID()" + tmp_po.getCOMPANY_ID());
					logger.debug("TXN_ID()>>" + tmp_po.getTXN_ID());
					logger.debug("SND_BRBK_ID()" + tmp_po.getSND_BRBK_ID());
					if (StrUtils.isEmpty(tmp_po.getCOMPANY_ID()) || StrUtils.isEmpty(tmp_po.getTXN_ID())
							|| StrUtils.isEmpty(tmp_po.getSND_BRBK_ID())) {
						// 如果上述其中一個成立 理論上sc不用新增任何資料
						po = null;
					} else {
						id = new SC_COMPANY_PROFILE_PK(com_id, txn_id, snd_brbk_id);
						po = new SC_COMPANY_PROFILE();
						// BeanUtils.copyProperties(po, tmp_po);
						po.setId(id);
						po.setCOMPANY_NAME(tmp_po.getCOMPANY_NAME());
						po.setCOMPANY_ABBR_NAME(tmp_po.getCOMPANY_ABBR_NAME());
					}
				}
			} else {// 如果代收與代付都沒有
				id = new SC_COMPANY_PROFILE_PK(com_id, txn_id, snd_brbk_id);
				po = new SC_COMPANY_PROFILE();
				po.setId(id);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.debug("getDataByCompany_Id.Exception>>" + e);
			// throw e;
		}
		logger.debug("po>>>" + po);
		return po;

	}

	public List<Map<String, String>> getScBgbkIdList() {
		List<Map<String, String>> list = null;
		String sql = "SELECT DISTINCT COALESCE(BR.BGBK_ID, SC.SND_BRBK_ID) AS BGBK_ID, COALESCE(BG.BGBK_NAME,'') AS BGBK_NAME "
				+ "FROM SC_COMPANY_PROFILE SC LEFT JOIN BANK_BRANCH BR ON SC.SND_BRBK_ID = BR.BRBK_ID "
				+ "LEFT JOIN BANK_GROUP BG ON BR.BGBK_ID = BG.BGBK_ID ORDER BY BGBK_ID ";
		try {
			SQLQuery query = getCurrentSession().createSQLQuery(sql);
			query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			list = query.list();
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<SC_COMPANY_PROFILE> getData(String condition, List<String> params) {
		List<SC_COMPANY_PROFILE> list = null;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT A.*, B.BRBK_NAME ");
		if (condition.equals("")) {
			sql.append("FROM SC_COMPANY_PROFILE A LEFT JOIN BANK_BRANCH B ON A.SND_BRBK_ID = B.BRBK_ID ");
		} else {
			sql.append("FROM SC_COMPANY_PROFILE A LEFT JOIN BANK_BRANCH B ON A.SND_BRBK_ID = B.BRBK_ID ");
		}

		if (StrUtils.isNotEmpty(condition)) {
			sql.append("WHERE " + condition);
		}
		sql.append(" ORDER BY COMPANY_ID ");
		try {
			SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
			for (int i = 0; i < params.size(); i++) {
				query.setParameter(i, params.get(i));
			}
			String ary[] = { "COMPANY_ID", "TXN_ID", "SND_BRBK_ID", "COMPANY_ABBR_NAME", "COMPANY_NAME",
					"IPO_COMPANY_ID", "PROFIT_ISSUE_DATE", "CDATE", "UDATE", "BRBK_NAME", "SYS_CDATE", "IS_SHORT",
					"FEE_TYPE","FEE_TYPE_ACTIVE_DATE" };
			AutoAddScalar autoScalar = new AutoAddScalar();
			autoScalar.addScalar(query, SC_COMPANY_PROFILE.class, ary);
			query.setResultTransformer(Transformers.aliasToBean(SC_COMPANY_PROFILE.class));
			list = query.list();
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public List<SC_COMPANY_PROFILE> getData(String sql, List<String> params ,  String[] cols, Class targetClass) {
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

//	public List<FEE_CODE_NW> getdata(String condition, List<String> params) {
//		List<FEE_CODE_NW> list = null;
//		StringBuffer sql = new StringBuffer();
//		sql.append("SELECT * ");
//		if (condition.equals("")) {
//			sql.append("FROM FEE_CODE_NW  ");
//		} else {
//			sql.append("FROM FEE_CODE_NW ");
//		}
//
//		if (StrUtils.isNotEmpty(condition)) {
//			sql.append("WHERE " + condition);
//		}
//		sql.append(" ORDER BY START_DATE ");
//		try {
//			SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
//			for (int i = 0; i < params.size(); i++) {
//				query.setParameter(i, params.get(i));
//			}
//			String ary[] = { "FEE_TYPE", "FEE_ID" };
//			AutoAddScalar autoScalar = new AutoAddScalar();
//			autoScalar.addScalar(query, FEE_CODE_NW.class, ary);
//			query.setResultTransformer(Transformers.aliasToBean(FEE_CODE_NW.class));
//			list = query.list();
//		} catch (HibernateException e) {
//			e.printStackTrace();
//		}
//		return list;
//	}

	public List<SC_COMPANY_PROFILE> getData(String condition, List<String> params, String orderSQL) {
		List<SC_COMPANY_PROFILE> list = null;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT A.*, B.BRBK_NAME ");
		if (condition.equals("")) {
			sql.append("FROM SC_COMPANY_PROFILE A LEFT JOIN BANK_BRANCH B ON A.SND_BRBK_ID = B.BRBK_ID ");
		} else {
			sql.append("FROM SC_COMPANY_PROFILE A LEFT JOIN BANK_BRANCH B ON A.SND_BRBK_ID = B.BRBK_ID ");
		}

		if (StrUtils.isNotEmpty(condition)) {
			sql.append("WHERE " + condition);
		}
		sql.append(orderSQL);
		try {
			SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
			for (int i = 0; i < params.size(); i++) {
				query.setParameter(i, params.get(i));
			}
			String ary[] = { "COMPANY_ID", "TXN_ID", "SND_BRBK_ID", "COMPANY_ABBR_NAME", "COMPANY_NAME",
					"IPO_COMPANY_ID", "PROFIT_ISSUE_DATE", "CDATE", "UDATE", "BRBK_NAME", "SYS_CDATE", "IS_SHORT","FEE_TYPE","FEE_TYPE_ACTIVE_DATE"
					 };
			AutoAddScalar autoScalar = new AutoAddScalar();
			autoScalar.addScalar(query,SC_COMPANY_PROFILE.class, ary);
			query.setResultTransformer(Transformers.aliasToBean(SC_COMPANY_PROFILE.class));
			list = query.list();
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<SC_COMPANY_PROFILE> checkCOMPANY_ID(String companyId) {
		List<SC_COMPANY_PROFILE> list = null;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT COMPANY_NAME,COMPANY_ABBR_NAME ");
		sql.append("FROM SC_COMPANY_PROFILE WHERE COMPANY_ID='" + companyId + "' ");
		sql.append("FETCH FIRST 1 ROWS ONLY");

		try {
			SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
			String cols = "COMPANY_NAME,COMPANY_ABBR_NAME";
			AutoAddScalar addscalar = new AutoAddScalar();
			addscalar.addScalar(query, SC_COMPANY_PROFILE.class, cols.split(","));
			query.setResultTransformer(Transformers.aliasToBean(SC_COMPANY_PROFILE.class));
			list = query.list();
			if (list.toString().equals("[]")) {
				list = checkCOMPANY_ID_SD(companyId);
			}

		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<SC_COMPANY_PROFILE> checkCOMPANY_ID_SD(String companyId) {
		List<SC_COMPANY_PROFILE> list = null;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT COMPANY_NAME,COMPANY_ABBR_NAME ");
		sql.append("FROM SD_COMPANY_PROFILE WHERE COMPANY_ID=?");
		sql.append("FETCH FIRST 1 ROWS ONLY");

		try {
			SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
			query.setParameter(0,companyId);
			String cols = "COMPANY_NAME,COMPANY_ABBR_NAME";
			AutoAddScalar addscalar = new AutoAddScalar();
			addscalar.addScalar(query, SC_COMPANY_PROFILE.class, cols.split(","));
			query.setResultTransformer(Transformers.aliasToBean(SC_COMPANY_PROFILE.class));
			list = query.list();
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return list;
	}

	public String checkCOMPANY_NAME(String companyId) {
		List list = null;
		String result = "";
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT COUNT(DISTINCT COMPANY_NAME) ");
		sql.append("FROM SC_COMPANY_PROFILE WHERE COMPANY_ID='" + companyId + "' ");

		try {
			SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
			list = query.list();
			result = list.get(0).toString();
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return result;
	}

	public String checkAmount(String companyId) {
		List list = null;
		String result = "";
		String total = "";
		int sd_count = checkAmount_SD(companyId);
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT COUNT(*) ");
		sql.append("FROM SC_COMPANY_PROFILE WHERE COMPANY_ID='" + companyId + "' ");

		try {
			SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
			list = query.list();
			result = list.get(0).toString();

			total = Integer.toString(Integer.parseInt(result) + sd_count);

		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return total;
	}

	public int checkAmount_SD(String companyId) {
		List list = null;
		String result = "";
		int sd_count = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT COUNT(*) ");
		sql.append("FROM SD_COMPANY_PROFILE WHERE COMPANY_ID='" + companyId + "' ");

		try {
			SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
			list = query.list();
			result = list.get(0).toString();

			sd_count = Integer.parseInt(result);

		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return sd_count;
	}

	public boolean updateNameById(String companyId, String name, String abbr_name) {
		Session session = getSessionFactory().openSession();
		boolean result = false;
		boolean sd_result = false;
		StringBuffer sql = new StringBuffer();
		sql.append(
				"UPDATE SC_COMPANY_PROFILE SET COMPANY_NAME='" + name + "' , COMPANY_ABBR_NAME='" + abbr_name + "' ");
		sql.append("WHERE COMPANY_ID='" + companyId + "' ");

		try {
			SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
			query.executeUpdate();
			session.beginTransaction().commit();
			result = true;
		} catch (HibernateException e) {
			e.printStackTrace();
		}

		sd_result = updateNameById_SD(companyId, name, abbr_name);

		if (result == true || sd_result == true) {
			result = true;
		}

		return result;
	}

	public boolean updateNameById_SD(String companyId, String name, String abbr_name) {
		Session session = getSessionFactory().openSession();
		boolean result = false;
		StringBuffer sql = new StringBuffer();
		sql.append(
				"UPDATE SD_COMPANY_PROFILE SET COMPANY_NAME='" + name + "' , COMPANY_ABBR_NAME='" + abbr_name + "' ");
		sql.append("WHERE COMPANY_ID='" + companyId + "' ");

		try {
			SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
			query.executeUpdate();
			session.beginTransaction().commit();
			result = true;
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return result;
	}

	public List<SC_COMPANY_PROFILE> searchONBLOCKTAB(String TXTIME1, String TXTIME2) {
		String BIZDATE = "WHERE BIZDATE >= '" + TXTIME1 + "'";
		if (!TXTIME2.equals("")) {
			BIZDATE = BIZDATE + " AND BIZDATE <= '" + TXTIME2 + "'";
		}
		BIZDATE = BIZDATE + " AND SENDERID<>'' ";

		List<SC_COMPANY_PROFILE> list = null;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT DISTINCT A.SENDERID AS COMPANY_ID,A.SENDERBANKID AS SND_BRBK_ID,A.TXID AS TXN_ID ");
		sql.append("  FROM (SELECT DISTINCT SENDERID,SENDERBANKID,TXID ");
		sql.append("          FROM ONBLOCKTAB " + BIZDATE
				+ " AND RESULTSTATUS='A' AND GARBAGEDATA IS NULL OR GARBAGEDATA='') A ");
		sql.append("LEFT JOIN ");
		sql.append("    (SELECT DISTINCT COMPANY_ID,SND_BRBK_ID,TXN_ID FROM SC_COMPANY_PROFILE) B ");
		sql.append("ON A.SENDERID = B.COMPANY_ID AND A.SENDERBANKID = B.SND_BRBK_ID AND A.TXID = B.TXN_ID ");
		sql.append("LEFT JOIN ");
		sql.append("    (SELECT DISTINCT SENDERID,SENDERBANKID,TXID FROM ONPENDINGTAB " + BIZDATE
				+ "AND RESULTCODE <> '01') C ");
		sql.append("ON A.SENDERID = C.SENDERID AND A.SENDERBANKID = C.SENDERBANKID AND A.TXID = C.TXID ");
		sql.append("JOIN ");
		sql.append("    (SELECT TXN_ID FROM TXN_CODE WHERE TXN_TYPE='SC') D ");
		sql.append("ON A.TXID = D.TXN_ID ");
		sql.append("WHERE B.COMPANY_ID IS NULL AND B.SND_BRBK_ID IS NULL AND B.TXN_ID IS NULL ");

		try {
			SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
			String cols = "COMPANY_ID,SND_BRBK_ID,TXN_ID";
			AutoAddScalar addscalar = new AutoAddScalar();
			addscalar.addScalar(query, SC_COMPANY_PROFILE.class, cols.split(","));
			query.setResultTransformer(Transformers.aliasToBean(SC_COMPANY_PROFILE.class));
			list = query.list();

		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return list;
	}
	public String checkSCAmount(String companyId,String txn_id, String fee_type , String snd_brbk_id){
		List list = null;
		String result="";
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT COUNT(*) "); 
		sql.append("FROM SC_COMPANY_PROFILE WHERE COMPANY_ID='"+companyId+"' AND TXN_ID='"+ txn_id +"' AND FEE_TYPE !='"+fee_type+"' AND SND_BRBK_ID !='"+snd_brbk_id+"' "); 

		try{
		SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
		list = query.list();
		result = list.get(0).toString();
		
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return result;
	}
	
	public List<SC_COMPANY_PROFILE> updateSCList(String companyId,String txn_id, String fee_type ,String snd_brbk_id){
		List list = null;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * "); 
		sql.append("FROM SC_COMPANY_PROFILE WHERE COMPANY_ID='"+companyId+"' AND TXN_ID='"+ txn_id +"' AND FEE_TYPE !='"+fee_type+"' AND SND_BRBK_ID !='"+snd_brbk_id+"' "); 

		try{
		SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
		String ary[] = {"COMPANY_ID","TXN_ID","SND_BRBK_ID","COMPANY_ABBR_NAME","COMPANY_NAME","IPO_COMPANY_ID","PROFIT_ISSUE_DATE","SYS_CDATE","CDATE","UDATE","IS_SHORT","FEE_TYPE","FEE_TYPE_ACTIVE_DATE"};
		AutoAddScalar autoScalar = new AutoAddScalar();
		autoScalar.addScalar(query, SC_COMPANY_PROFILE.class, ary);
		query.setResultTransformer(Transformers.aliasToBean(SC_COMPANY_PROFILE.class));
		list = query.list();
		
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return list;
	}
	
	public List<Map<String, String>> checkDoubleDate(String company_id , String txn_id , String snd_brbk_id , String fee_type_active_date){
		List<Map<String, String>> list = null;
		String sql = "SELECT * FROM SC_COMPANY_PROFILE_HIS WHERE COMPANY_ID=:company_id AND TXN_ID=:txn_id AND SND_BRBK_ID=:snd_brbk_id AND ACTIVE_DATE=:fee_type_active_date";
		try{
			SQLQuery query = getCurrentSession().createSQLQuery(sql);
			query.setParameter("company_id", company_id);
			query.setParameter("txn_id", txn_id);
			query.setParameter("snd_brbk_id", snd_brbk_id);
			query.setParameter("fee_type_active_date", fee_type_active_date);
			query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			list = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return list;	
	}
}
