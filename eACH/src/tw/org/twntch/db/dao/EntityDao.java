package tw.org.twntch.db.dao;


import java.io.Serializable;
import java.util.List;

/**
 * 針對單個Entity對象的操作定義.
 * 不依賴於具體ORM實現方案.
 *
 * @author calvin
 */
public interface EntityDao<T, PK extends Serializable> {

	public T get(PK id);

	public List<T> getAll();

//	public void save(T o);
//	
//	public void saveObject(T o);
//
//	public void update(T o);
	
//	public void remove(T o);

	public void removeById(PK id);

	public String getIdName(Class entityClass);
}

