package tw.org.twntch.db.dao.hibernate;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.impl.CriteriaImpl;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import tw.org.twntch.bo.FieldsMap;
import tw.org.twntch.util.BeanUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.SpringAppCtxHelper;




/**
 * Hibernate Dao的泛型基類.
 * <p/>
 * 繼承於Spring的HibernateDaoSupport,提供分頁函數和若干便捷查詢方法，並對返回值作了泛型類型轉換.
 *
 * @author calvin
 * @author tin
 * @see HibernateDaoSupport
 * @see HibernateEntityDao
 */

@SuppressWarnings("unchecked")
public class HibernateGenericDao<T, PK extends Serializable> extends HibernateDaoSupport {
	private Logger logger = Logger.getLogger(getClass());
	
	private SessionFactory sessionFactory;
	
	private HibernateTemplate hibernateTemplate;
	
	/*
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	*/
	
//	public HibernateTemplate getHibernateTemplate() {
//		return hibernateTemplate;
//	}
/*
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
*/	
//
	public Session getCurrentSession() {
			return getSession();
	}
//
//	public Session getSession() {
//		return getHibernateTemplate().getSessionFactory().getCurrentSession();
//	}
	
	/**
	 * 根據ID獲取對像
	 */
	public T get(Class<T> entityClass, final PK id) {
		return (T) getCurrentSession().load(entityClass, id);
	}

	/**
	 * 保存對像
	 */
	public void save(T o) {
		//logger.debug("Save Object: ");
		//logger.debug(JSONUtils.toJson(o));
		try {
			getHibernateTemplate().saveOrUpdate(o);
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 保存對像
	 */
	public void save(T o, Map pkmap) {
		//logger.debug("Save Object: ");
		//logger.debug(JSONUtils.toJson(o));
		getHibernateTemplate().saveOrUpdate(o);
	}
	
	
//	因aop 而生的api
	
	public Map<String,String> saveFail(T o,Map<String , String> pkmap ,String failmsg , Integer type){
		Map<String,String> msgmap = new HashMap<String,String>();
		type = type != null ? type : 0;
		switch (type) {
		case 1:
			msgmap = saveFail_PK(o ,pkmap, failmsg);
			break;
		case 2:
			msgmap = saveFail_GE(o,failmsg);
			break;
		default:
			msgmap = saveFail_GE(o,failmsg);
			break;
		}
		return msgmap;
		
	}
	public Map<String,String> saveFail_PK(T o ,Map<String , String> pkmap ,String failmsg){
		
		pkmap = pkmap != null ? pkmap : new HashMap<String , String>() ;
		Map<String,String> msgmap = new HashMap<String,String>();
		Map<String,String> chmap = new HashMap<String,String>();
		FieldsMap fieldsmap =  SpringAppCtxHelper.getBean("fieldsmap");
		Map fielmap = fieldsmap.getArgs();
		for( String key :pkmap.keySet()){
			if(fielmap.containsKey(key) ){
				chmap.put(fielmap.get(key).toString(), pkmap.get(key));
			} 
		}
		msgmap.put("result", "FALSE");
//		msgmap.put("msg", failmsg+"PK="+JSONUtils.map2json(chmap));
		msgmap.put("msg", failmsg+" "+chmap.toString());
		msgmap.put("target", "add_p");
		return msgmap;
	}
	/**
	 * 一般錯誤
	 * @param failmsg
	 * @return
	 */
	public Map<String,String> saveFail_GE(T o  ,String failmsg){
		Map<String,String> msgmap = new HashMap<String,String>();
		msgmap.put("result", "FALSE");
		msgmap.put("msg", failmsg);
		msgmap.put("target", "add_p");
		return msgmap;
	}
	
	public Map<String,String> updateFail(T o, Map<String , String> oldmap ,Map<String , String> pkmap ,String failmsg , Integer type){
		Map<String,String> msgmap = new HashMap<String,String>();
		type = type != null ? type : 0;
		switch (type) {
		case 1:
			msgmap = updateFail_PK(o ,pkmap, failmsg);
			break;
		case 2:
			msgmap = updateFail_GE(o, oldmap ,pkmap, failmsg);
			break;
		default:
			msgmap = updateFail_GE(o,  oldmap ,pkmap ,failmsg);
			break;
		}
		return msgmap;
		
	}
	
	public Map<String,String> updateFail_PK(T o ,Map<String , String> pkmap ,String failmsg){
		pkmap = pkmap != null ? pkmap : new HashMap<String , String>() ;
		Map<String,String> msgmap = new HashMap<String,String>();
		Map<String,String> chmap = new HashMap<String,String>();
		FieldsMap fieldsmap =  SpringAppCtxHelper.getBean("fieldsmap");
		Map fielmap = fieldsmap.getArgs();
		for( String key :pkmap.keySet()){
			if(fielmap.containsKey(key) ){
				chmap.put(fielmap.get(key).toString(), pkmap.get(key));
			} 
		}
		msgmap.put("result", "FALSE");
		msgmap.put("msg", failmsg+"PK="+chmap.toString());
		msgmap.put("target", "edit_p");
		return msgmap;
	}
	/**
	 * 一般錯誤
	 * @param failmsg
	 * @return
	 */
	public Map<String,String> updateFail_GE(T o   , Map oldmap , Map pkmap ,String failmsg){
		Map<String,String> msgmap = new HashMap<String,String>();
//		map.put("result", "FALSE");
//		map.put("msg", "儲存失敗，查無資料");
//		map.put("target", "edit_p");
		msgmap.put("result", "FALSE");
		msgmap.put("msg", failmsg);
		msgmap.put("target", "edit_p");
		return msgmap;
	}
	
	
	
	public Map<String,String> removeFail(T o ,Map<String , String> pkmap ,String failmsg ,  Integer type){
		Map<String,String> msgmap = new HashMap<String,String>();
		type = type != null ? type : 0;
		switch (type) {
		case 1:
			msgmap = removeFail_PK(o ,pkmap, failmsg);
			break;
		case 2:
			msgmap = removeFail_GE(o ,pkmap, failmsg);
			break;
		default:
			msgmap = removeFail_GE(o ,pkmap ,failmsg);
			break;
		}
		return msgmap;
	}
	public Map<String,String> removeFail_PK(T o ,Map<String , String> pkmap ,String failmsg){
		pkmap = pkmap != null ? pkmap : new HashMap<String , String>() ;
		Map<String,String> msgmap = new HashMap<String,String>();
		Map<String,String> chmap = new HashMap<String,String>();
		FieldsMap fieldsmap =  SpringAppCtxHelper.getBean("fieldsmap");
		Map fielmap = fieldsmap.getArgs();
		for( String key :pkmap.keySet()){
			if(fielmap.containsKey(key) ){
				chmap.put(fielmap.get(key).toString(), pkmap.get(key));
			} 
		}
		msgmap.put("result", "FALSE");
		msgmap.put("msg", failmsg+"PK="+chmap.toString());
		msgmap.put("target", "edit_p");
		return msgmap;
	}
	public Map<String,String> removeFail_GE(T o ,Map<String , String> pkmap ,String failmsg){
		pkmap = pkmap != null ? pkmap : new HashMap<String , String>() ;
		Map<String,String> msgmap = new HashMap<String,String>();
		Map<String,String> chmap = new HashMap<String,String>();
		FieldsMap fieldsmap =  SpringAppCtxHelper.getBean("fieldsmap");
		Map fielmap = fieldsmap.getArgs();
		for( String key :pkmap.keySet()){
			if(fielmap.containsKey(key) ){
				chmap.put(fielmap.get(key).toString(), pkmap.get(key));
			} 
		}
		msgmap.put("result", "FALSE");
		msgmap.put("msg", failmsg+"PK="+chmap.toString());
		msgmap.put("target", "edit_p");
		return msgmap;
	}
	
	/**
	 * add by hugo 
	 * eACH專案使用 、使用者操作紀錄  ，pkmap為紀錄pk用、  oldMap為更新前的內容 。
	 * 供spring aop 紀錄 newMap及oldMap
	 * 保存對像
	 */
	public void saveII(T o , Map oldMap ,Map pkmap) {
		getHibernateTemplate().saveOrUpdate(o);
	}
	
	/**
	 * 刪除對像
	 */
	public void removeII(T o ,Map pkmap) {
		getHibernateTemplate().delete(o);
	}
	
	/**
	 * 避免被Aop攔截
	 * @param o
	 */
	public void aop_save(T o ) {
		getHibernateTemplate().saveOrUpdate(o);
	}
	
	
	public Map mapremove(Map map){
		if(map !=null){
			map.remove("hibernateLazyInitializer");
			map.remove("class");
			map.remove("handler");
			map.remove("multipartRequestHandler");
			map.remove("pagesize");
			map.remove("result");
			map.remove("servletWrapper");
			map.remove("scaseary");
			map.remove("target");
			map.remove("msg");
			map.remove("ac_key");
		}
		
		return map;
	}
	/**
	 * 
	 * @param map
	 * @param values
	 * @return
	 */
	public Map mapremoveII(Map map , Object... values){
		if(map !=null){
			map.remove("hibernateLazyInitializer");
			map.remove("class");
			map.remove("handler");
			map.remove("multipartRequestHandler");
			map.remove("pagesize");
			map.remove("result");
			map.remove("servletWrapper");
			map.remove("scaseary");
			map.remove("target");
			map.remove("msg");
			map.remove("ac_key");
			
			for(int i =0 ; i < values.length ; i++){
				map.remove(values[i]);
			}
		}
		return map;
	}
	
//	因aop 而生的api end
	
	
	
	
	
	/**
	 * 保存對像
	 */
//	public void saveObject(T o) {
//		getHibernateTemplate().save(o);
//	}

	/**
	 * 保存對像
	 */
	public void update(T o) {
		getHibernateTemplate().update(o);
	}

	/**
	 * 刪除對像
	 */
	public void remove(T o) {
		getHibernateTemplate().delete(o);
	}

	/**
	 * 根據ID刪除對像
	 */
	public void removeById(Class<T> entityClass, final PK id) {
		remove(get(entityClass, id));
	}

	/**
	 * 創建Query對像.
	 * 對於需要first,max,fetchsize,cache,cacheRegion等諸多設置的函數,可以返回Query後自行設置.
	 * 留意可以連續設置,如 dao.getQuery(hql).setMaxResult(100).setCacheable(true).list();
	 *
	 * @param values 可變參數
	 *               用戶可以如下四種方式使用
	 *               dao.getQuery(hql)
	 *               dao.getQuery(hql,arg0);
	 *               dao.getQuery(hql,arg0,arg1);
	 *               dao.getQuery(hql,new Object[arg0,arg1,arg2])
	 */
	public Query getQuery(String hql, Object... values) {
		Assert.hasText(hql);
		Query query = getCurrentSession().createQuery(hql);
		String hqll = hql;
		int vlen = values.length;
		for (int i = 0; i < values.length; i++) {
			query.setParameter(i, values[i]);
		}
		return query;
	}

	public List<T> getAll(Class<T> entityClass) {
		Criteria criteria = getCurrentSession().createCriteria(entityClass);
			
		return criteria.list();
	}
	
	/**
	 * 創建Criteria對像
	 *
	 * @param criterion 可變條件列表,Restrictions生成的條件
	 */
	public Criteria getCriteria(Class<T> entityClass, Criterion... criterion) {
		Criteria criteria = getCurrentSession().createCriteria(entityClass);
		for (Criterion c : criterion) {
			criteria.add(c);
		}
		return criteria;
	}


	/**
	 * hql查詢.
	 *
	 * @param values 可變參數
	 *               用戶可以如下四種方式使用
	 *               dao.find(hql)
	 *               dao.find(hql,arg0);
	 *               dao.find(hql,arg0,arg1);
	 *               dao.find(hql,new Object[arg0,arg1,arg2])
	 */
	public List find(String hql, Object... values) {
		Assert.hasText(hql);
		Query query = getQuery(hql, values);
		return query.list();
	}

	/**
	 * 根據屬性名和屬性值查詢對像.
	 *
	 * @return 符合條件的對象列表
	 */
	public List<T> findBy(Class<T> entityClass, String name, Object value) {
		Assert.hasText(name);
		return getCriteria(entityClass, Restrictions.eq(name, value)).list();
	}

	/**
	 * 根據屬性名和屬性值查詢唯一對像.
	 *
	 * @return 符合條件的唯一對像
	 */
	public T findUniqueBy(Class<T> entityClass, String name, Object value) {
		Assert.hasText(name);
		return (T) getCriteria(entityClass, Restrictions.eq(name, value)).uniqueResult();
	}

	/**
	 * 根據屬性名和屬性值以Like AnyWhere方式查詢對像.
	 */
	public List<T> findByLike(Class<T> entityClass, String name, String value) {
		Assert.hasText(name);
		return getCriteria(entityClass, Restrictions.like(name, value, MatchMode.ANYWHERE)).list();
	}

	/**
	 * 判斷對像某些屬性的值在數據庫中不存在重複
	 *
	 * @param names 在POJO裡不能重複的屬性列表,以逗號分割
	 *              如"name,loginid,password"
	 */
	public boolean isNotUnique(Class<T> entityClass, Object entity, String names) {
		Assert.hasText(names);
		Criteria criteria = getCriteria(entityClass).setProjection(Projections.rowCount());
		String[] nameList = names.split(",");
		try {
			//循環加入
			for (String name : nameList) {
				criteria.add(Restrictions.eq(name, PropertyUtils.getProperty(entity, name)));
			}

			//以下代碼為了如果是update的情況,排除entity自身.

			String idName = getIdName(entityClass);
			//通過反射取得entity的主鍵值
			Object id = PropertyUtils.getProperty(entity, idName);
			//如果id!=null,說明對像已存在,該操作為update,加入排除自身的判斷
			if (id != null)
				criteria.add(Restrictions.not(Restrictions.eq(idName, id)));

		} catch (Exception e) {
			ReflectionUtils.handleReflectionException(e);
		}
		return (Integer) criteria.uniqueResult() > 0;
	}

	public String getIdName(Class entityClass) {
		Assert.notNull(entityClass);
		String idName = getSessionFactory().getClassMetadata(entityClass).getIdentifierPropertyName();
		Assert.hasText(idName, entityClass.getSimpleName() + "has no id column define");
		return idName;
	}

	/**
	 * 分頁查詢函數，使用Criteria.
	 *
	 * @param pageNo 頁號,從1開始.
	 */
	public Page pagedQuery(Class entityClass, int pageNo, int pageSize, Criterion... criterion) {
		Criteria criteria = getCriteria(entityClass, criterion);
		return pagedQuery(criteria, pageNo, pageSize);
	}

	/**
	 * 分頁查詢函數，使用Criteria.
	 *
	 * @param pageNo 頁號,從1開始.
	 */
	public Page pagedQuery(Criteria criteria, int pageNo, int pageSize) {
		Assert.isTrue(pageNo >= 0, "pageNo should start from 0");
		CriteriaImpl impl = (CriteriaImpl) criteria;

		//先把Projection和OrderBy條件取出來,清空兩者來執行Count操作
		Projection projection = impl.getProjection();
		List<CriteriaImpl.OrderEntry> orderEntries;
		try {
			orderEntries = (List) BeanUtils.getDeclaredProperty(impl, "orderEntries");
			BeanUtils.setDeclaredProperty(impl, "orderEntries", new ArrayList());
		}
		catch (Exception e) {
			throw new InternalError(" Runtime Exception impossibility throw ");
		}

		//執行查詢
		long totalCount = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();

		//將之前的Projection和OrderBy條件重新設回去
		criteria.setProjection(projection);
		if (projection == null) {
			criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		}
		try {
			BeanUtils.setDeclaredProperty(impl, "orderEntries", orderEntries);
		} catch (Exception e) {
			throw new InternalError(" Runtime Exception impossibility throw ");
		}

		//返回分頁對像
		if (totalCount < 1) return new Page();

		int startIndex = Page.getStartOfPage(pageNo, pageSize);
		List list = criteria.setFirstResult(startIndex).setMaxResults(pageSize).list();

		return new Page(startIndex, totalCount, pageSize, list);
	}

	/**
	 * 分頁查詢函數，使用hql.
	 *
	 * @param pageNo 頁號,從0開始.
	 */
	public Page pagedQuery(String hql, int pageNo, int pageSize, Object... values) {
		Assert.hasText(hql);
		//Count查詢
		String countQueryString = " select count (*) " + removeSelect(removeOrders(hql));
		List countlist = getQuery(countQueryString, values).list();
		long totalCount = (Long) countlist.get(0);
		if (totalCount < 1) return new Page();
		//實際查詢返回分頁對像
		int startIndex = Page.getStartOfPage(pageNo, pageSize);
		Query query = getQuery(hql, values);
		List list = query.setFirstResult(startIndex).setMaxResults(pageSize).list();

		return new Page(startIndex, totalCount, pageSize, list);
	}
	
	
	/**
	 * 20150310 add by hugo
	 * 分頁查詢函數，使用一般sql ，但不支援太複雜的sql，也不支援addscalar
	 *
	 * @param sql
	 * @param countSql
	 * @param pageNo 頁號,從0開始.
	 * @param pageSize
	 * @param values
	 * @param o
	 * @return
	 */
	public Page pagedSQLQuery(String sql, String countSql,int pageNo, int pageSize, List<String> values , Class<T> o) {
		Assert.hasText(sql);
		//Count查詢
		String countQueryString = countSql;
//		List countlist = getQuery(countQueryString, values).list();
		SQLQuery sqlQuery = getSession().createSQLQuery(countQueryString);
		for (int i = 0; i < values.size(); i++) {
			sqlQuery.setParameter(i, values.get(i));
		}
		List countlist = sqlQuery.list();
		long totalCount = new BigDecimal(countlist.get(0).toString()).longValue() ;
		if (totalCount < 1) return new Page();
		//實際查詢返回分頁對像
		int startIndex = Page.getStartOfPage(pageNo, pageSize);
		System.out.println("totalCount>>"+totalCount);
		System.out.println("startIndex>>"+startIndex+" , pageSize>>"+pageSize);
		System.out.println("values>>"+values);
//		Query query = getQuery(hql, values);
		sqlQuery = getSession().createSQLQuery(sql);
		for (int i = 0; i < values.size(); i++) {
			sqlQuery.setParameter(i, values.get(i));
		}
		System.out.println("o>>"+o);
		sqlQuery.addEntity(o);
		List list = sqlQuery.setFirstResult(startIndex).setMaxResults(pageSize).list();
		
		return new Page(startIndex, totalCount, pageSize, list);
	}


	/**
	 * 去除hql的select 子句，未考慮union的情況,，用於pagedQuery.
	 */
//	private static String removeSelect(String hql) {
//		Assert.hasText(hql);
//		int beginPos = hql.toLowerCase().indexOf("from");
//		Assert.isTrue(beginPos != -1, " hql : " + hql + " must has a keyword 'from'");
//		return hql.substring(beginPos);
//	}
	/**
	 * 去除hql的select 子句，未考慮union的情況,，用於pagedQuery.
	 */
	public static String removeSelect(String hql) {
		Assert.hasText(hql);
		int beginPos = hql.toLowerCase().indexOf("from");
		Assert.isTrue(beginPos != -1, " hql : " + hql + " must has a keyword 'from'");
		return hql.substring(beginPos);
	}

	/**
	 * 去除hql的orderby 子句，用於pagedQuery.
	 */
//	private static String removeOrders(String hql) {
//		Assert.hasText(hql);
//		Pattern p = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*", Pattern.CASE_INSENSITIVE);
//		Matcher m = p.matcher(hql);
//		StringBuffer sb = new StringBuffer();
//		while (m.find()) {
//			m.appendReplacement(sb, "");
//		}
//		m.appendTail(sb);
//		return sb.toString();
//	}
	public static String removeOrders(String hql) {
		Assert.hasText(hql);
		Pattern p = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(hql);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, "");
		}
		m.appendTail(sb);
		return sb.toString();
	}
}
