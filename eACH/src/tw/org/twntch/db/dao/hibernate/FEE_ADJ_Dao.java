package tw.org.twntch.db.dao.hibernate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;

import tw.org.twntch.aop.GenerieAop;
import tw.org.twntch.po.BRBK_FEE_ADJ;
import tw.org.twntch.po.BRBK_FEE_ADJ_PK;
import tw.org.twntch.po.EACH_FUNC_LIST;
import tw.org.twntch.po.EACH_USERLOG;
import tw.org.twntch.util.BeanUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.zDateHandler;

public class FEE_ADJ_Dao extends HibernateEntityDao<BRBK_FEE_ADJ, BRBK_FEE_ADJ_PK> {
	private EACH_USERLOG_Dao userLog_Dao;
	
	//刪除資料，包含更新生效日
	public boolean deleteData(BRBK_FEE_ADJ po){
		boolean result = true;
		Session session = getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.delete(po);
			result = updateACTIVE_DATE(session, po.getId().getYYYYMM());
			if(!result){
				session.beginTransaction().rollback();
			}else{
				session.beginTransaction().commit();
				result = true;
			}
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			session.beginTransaction().rollback();
		}
		return result;
	}	
	
	//新增、修改資料，包含更新生效日
	public boolean saveData(BRBK_FEE_ADJ po){
		boolean result = true;
		Session session = getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(po);
			result = updateACTIVE_DATE(session, po.getId().getYYYYMM());
			if(!result){
				session.beginTransaction().rollback();
			}else{
				session.beginTransaction().commit();
				result = true;
			}
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			session.beginTransaction().rollback();
		}
		return result;
	}
	
	public Page getByYyyymm(String yyyymm, int pageNo, int pageSize, String sidx, String sord){
		Page page = null;
		String sql = "FROM BRBK_FEE_ADJ WHERE YYYYMM = '" + yyyymm + "' ";
		if(StrUtils.isNotEmpty(sidx)){
			sql += "ORDER BY " + sidx + " " + sord;
		}
		String cntSQL = "FROM BRBK_FEE_ADJ WHERE YYYYMM = '" + yyyymm + "'";
		String[] cols = {"YYYYMM","BRBK_ID","SNO","BRBK_NAME","FEE","ACTIVE_DATE","CDATE","UDATE"};
		try{
			page = getData(pageNo, pageSize, sidx, sord, cntSQL, sql, cols, null);
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return page;
	}
	
	public Page getBySno(String sno, int pageNo, int pageSize, String sidx, String sord){
		Page page = null;
		String sql = "FROM BRBK_FEE_ADJ WHERE SNO = '" + sno + "' ";
		if(StrUtils.isNotEmpty(sidx)){
			sql += "ORDER BY " + sidx + " " + sord;
		}
		String cntSQL = "FROM BRBK_FEE_ADJ WHERE SNO = '" + sno + "'";
		String[] cols = {"YYYYMM","BRBK_ID","SNO","BRBK_NAME","FEE","ACTIVE_DATE","CDATE","UDATE"};
		try{
			page = getData(pageNo, pageSize, sidx, sord, cntSQL, sql, cols, null);
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return page;
	}
	
	public BRBK_FEE_ADJ getSNO(String yyyymm){
		List<BRBK_FEE_ADJ> list = null;
		BRBK_FEE_ADJ po = new BRBK_FEE_ADJ();
		String sql = "SELECT COALESCE((MAX(SNO) + 1),0) AS SNO FROM BRBK_FEE_ADJ WHERE YYYYMM = :yyyymm";
		try{
			SQLQuery query = getCurrentSession().createSQLQuery(sql);
			query.setParameter("yyyymm", yyyymm);
			query.addScalar("SNO");
			query.setResultTransformer(Transformers.aliasToBean(BRBK_FEE_ADJ.class));
			list = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
		}
		if(list != null){
			po = list.get(0);
		}
		return po;
	}
	
	public BigDecimal getCheckSum(String yyyymm){
		BigDecimal sum = null;
		List list = null;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT COALESCE(SUM(FEE),-1) AS NUM FROM BRBK_FEE_ADJ ");
		sql.append("WHERE YYYYMM = '" + yyyymm + "' ");
		sql.append("AND COALESCE(ACTIVE_DATE,'') = '' ");
		try{
			SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
			query.addScalar("NUM");
			query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			list = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
		}
		if(list != null && list.size() > 0){
			sum = (BigDecimal)((Map)list.get(0)).get("NUM");
			if(Float.valueOf(sum.toString()) == -1){
				sum = null;
			}
		}
		return sum;
	}
	
	public boolean publish(String yyyymm){
		boolean result = false;
		Session session = getSessionFactory().openSession();
		String cnt = "SELECT * FROM BRBK_FEE_ADJ WHERE YYYYMM = '" + yyyymm + "' AND COALESCE(ACTIVE_DATE,'') = '' ";
		String sql = "UPDATE BRBK_FEE_ADJ SET ACTIVE_DATE = '" + zDateHandler.getTWDate() + "' WHERE YYYYMM = '" + yyyymm + "' AND COALESCE(ACTIVE_DATE,'') = ''";
		try{
			session.beginTransaction();
			//計算欲發布的筆數
			SQLQuery query = session.createSQLQuery(cnt);
			query.addEntity(BRBK_FEE_ADJ.class);
			List<BRBK_FEE_ADJ> list = query.list();
			List<String> brbkNameList = new LinkedList<String>();
			if(list != null){
				for(int i = 0; i < list.size(); i++){
					brbkNameList.add(list.get(i).getBRBK_NAME());
				}
			}
			
			query = session.createSQLQuery(sql);
			query.executeUpdate();
			session.beginTransaction().commit();
			result = true;
			
			Map log = new HashMap();
			log.put("YYYYMM", yyyymm);
			log.put("BRBK_NAME", brbkNameList);
			writeLog("B", null, log, null, "發布成功");
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			session.beginTransaction().rollback();
		}
		return result;
	}
	
	/**
	 * 更新某年月下所有資料的生效日為未啟用(清空日期)
	 * @param yyyymm
	 * @return
	 */
	public boolean updateACTIVE_DATE(Session session, String yyyymm){
		boolean result = false;
		String sql = "UPDATE BRBK_FEE_ADJ SET ACTIVE_DATE = '' WHERE YYYYMM = '" + yyyymm + "'";
		try{
			SQLQuery query = session.createSQLQuery(sql);
			if(query.executeUpdate() >= 0){
				result = true;
			}
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return result;
	}
	
	public Page getData(int pageNo, int pageSize, String sidx, String sord, String countQuerySql, String sql, String[] cols, List<String> values){
		StringBuffer exeSQL = new StringBuffer();
		String col = "";
		for(int i = 0; i < cols.length; i++){
			col += cols[i];
			if(i < cols.length - 1){
				col += ",";
			}
		}
		exeSQL.append("SELECT " + col + " FROM ( ");
		exeSQL.append("    SELECT ROWNUMBER() OVER(");
		if(StrUtils.isNotEmpty(sidx)){
			exeSQL.append("ORDER BY " + sidx + " " + sord);
		}
		exeSQL.append("    ) AS ROWNUMBER, BRBK_FEE_ADJ.* ");
		exeSQL.append(sql);
		exeSQL.append(") WHERE ROWNUMBER >= " + (Page.getStartOfPage(pageNo, pageSize) + 1) + " AND ROWNUMBER <= " + (pageNo * pageSize) + " ");
		System.out.println("sql >> " + exeSQL);
		
		SQLQuery query =  getCurrentSession().createSQLQuery(exeSQL.toString());
		query.addEntity(BRBK_FEE_ADJ.class);
		//AutoAddScalar addscalar = new AutoAddScalar();
		//addscalar.addScalar(query, BRBK_FEE_ADJ.class, cols);
		//query.setResultTransformer(Transformers.aliasToBean(BRBK_FEE_ADJ.class));
		//實際查詢返回分頁對像
		List list = query.list();

		return new Page(Page.getStartOfPage(pageNo, pageSize), countData(countQuerySql), pageSize, list);
	}
	
	public int countData(String countQuerySql){
		StringBuffer cntSQL = new StringBuffer();
		cntSQL.append("SELECT COUNT(*) AS NUM ");
		cntSQL.append(countQuerySql);
		System.out.println("sql >> " + cntSQL);
		
		int count = 0;
		SQLQuery query =  getCurrentSession().createSQLQuery(cntSQL.toString());
		query.addScalar("NUM");
		List countList = query.list();
		if(countList != null && countList.size() > 0){
			count = (Integer)countList.get(0);
		}
		return count; 
	}
	
	public void writeLog(String op_type, BRBK_FEE_ADJ oldPo, BRBK_FEE_ADJ newPo, String adexcode){
		GenerieAop ga = new GenerieAop();
		EACH_FUNC_LIST func_po = ga.getUsed_Func(op_type, "");
		EACH_USERLOG log = ga.getEACH_USERLOG(op_type);
		if(func_po != null){
			log.setUP_FUNC_ID(func_po.getUP_FUNC_ID());
			log.setFUNC_ID(func_po.getFUNC_ID());
			log.setFUNC_TYPE(func_po.getFUNC_TYPE());
		}
		try{
			if(oldPo == null){
				log.setBFCHCON(null);
			}else{
				Map oldMap = BeanUtils.describe(oldPo);
				oldMap.putAll(BeanUtils.describe(oldPo.getId()));
				log.setBFCHCON(ga.restMapKey2CH(oldMap).toString());
			}
			if(newPo == null){
				log.setAFCHCON(null);
			}else{
				Map newMap = BeanUtils.describe(newPo);
				newMap.putAll(BeanUtils.describe(newPo.getId()));
				log.setAFCHCON(ga.restMapKey2CH(newMap).toString());
			}
			log.setADEXCODE(adexcode);
			userLog_Dao.aop_save(log);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void writeLog(String op_type, Map oldMap, Map newMap, Map pkMap, String adexcode){
		GenerieAop ga = new GenerieAop();
		EACH_FUNC_LIST func_po = ga.getUsed_Func(op_type, "");
		EACH_USERLOG log = ga.getEACH_USERLOG(op_type);
		if(func_po != null){
			log.setUP_FUNC_ID(func_po.getUP_FUNC_ID());
			log.setFUNC_ID(func_po.getFUNC_ID());
			log.setFUNC_TYPE(func_po.getFUNC_TYPE());
		}
		try{
			if(pkMap == null){
				pkMap = new HashMap();
			}
			if(oldMap == null){
				log.setBFCHCON(null);
			}else{
				oldMap.putAll(pkMap);
				log.setBFCHCON(ga.restMapKey2CH(oldMap).toString());
			}
			if(newMap == null){
				log.setAFCHCON(null);
			}else{
				newMap.putAll(pkMap);
				log.setAFCHCON(ga.restMapKey2CH(newMap).toString());
			}
			log.setADEXCODE(adexcode);
			userLog_Dao.aop_save(log);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public EACH_USERLOG_Dao getUserLog_Dao() {
		return userLog_Dao;
	}

	public void setUserLog_Dao(EACH_USERLOG_Dao userLog_Dao) {
		this.userLog_Dao = userLog_Dao;
	}
	
}
