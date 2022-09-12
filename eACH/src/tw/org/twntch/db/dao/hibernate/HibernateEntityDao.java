package tw.org.twntch.db.dao.hibernate;


import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria; 
import org.hibernate.Hibernate;
import org.hibernate.ObjectNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import tw.org.twntch.db.dao.EntityDao;
import tw.org.twntch.util.GenericsUtils;



/**
 * 負責為單個Entity對像提供CRUD操作的Hibernate DAO基類.
 * <p/>
 * 子類只要在類定義時指定所管理Entity的Class, 即擁有對單個Entity對象的CRUD操作.
 * eg.
 * <pre>
 * public class UserManager extends HibernateEntityDao<User>{
 * }
 * </pre>
 *
 * @author calvin
 * @see HibernateGenericDao
 */

@SuppressWarnings("unchecked")
public class HibernateEntityDao<T, PK extends Serializable> extends HibernateGenericDao implements EntityDao<T, PK> {
	Logger logger = Logger.getLogger(getClass()); 
	/**
	 * DAO所管理的Entity類型.
	 */
	protected Class<T> entityClass;

	/**
	 * 取得entityClass.
	 * JDK1.4不支持泛型的子類可以拋開Class<T> entityClass,重載此函數達到相同效果。
	 */
	protected Class<T> getEntityClass() {
		return entityClass;
	}

	/**
	 * 在構造函數中將泛型T.class賦給entityClass
	 */
	public HibernateEntityDao() {
		entityClass = GenericsUtils.getSuperClassGenricType(getClass());
	}

	/**
	 * 根據ID獲取對像
	 */
	public T get(PK id) {
		try {
			T o = (T)get(getEntityClass(), id);
			Hibernate.initialize(o);
			return o;
		}
		catch(ObjectNotFoundException ee) {
			// 20130814 andy 改為 傳回 null ，不拋例外
			//throw ee;
			return null;
		}
		catch(Exception e){
			logger.error("HibernateEntityDao.get(PK id) Exception : " + e.getMessage());			
			logger.error("Load " + getEntityClass() + ", ID: " + id + " Error !");
		}
		return null;
	}

	/**
	 * 獲取全部對像
	 */
	public List<T> getAll() {
		return getAll(getEntityClass());
	}

	/**
	 * 根據ID移除對像.
	 */
	public void removeById(PK id) {
		removeById(getEntityClass(), id);
	}

	/**
	 * 根據屬性名和屬性值查詢對像.
	 *
	 * @return 符合條件的對象列表
	 */
	public List<T> findBy(String name, Object value) {
		return findBy(getEntityClass(), name, value);
	}

	/**
	 * 根據屬性名和屬性值查詢單個對象.
	 *
	 * @return 符合條件的唯一對像
	 */
	public T findUniqueBy(String name, Object value) {
		return (T)findUniqueBy(getEntityClass(), name, value);
	}

	/**
	 * 根據屬性名和屬性值以Like AnyWhere方式查詢對像.
	 */
	public List<T> findByLike(String name, String value) {
		return findByLike(getEntityClass(), name, value);
	}

	/**
	 * 取得Entity的Criteria.
	 */
	protected Criteria getEntityCriteria() {
		return getCriteria(getEntityClass());
	}

	/**
	 * 判斷對像某些屬性的值在數據庫中不存在重複
	 *
	 * @param names 在POJO裡不能重複的屬性列表,以逗號分割
	 *              如"name,loginid,password"
	 */
	public boolean isNotUnique(Object entity, String names) {
		return isNotUnique(getEntityClass(), entity, names);
	}
	
}

