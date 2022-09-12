package tw.org.twntch.db.dao;


import java.io.Serializable;
import java.util.List;

public interface GenericDao  <T, PK extends Serializable>
{
	/** Persist the newInstance object into database */
	T create(T newInstance);
	
    /** Retrieve an object that was previously persisted to the database using
	 * the indicated id as primary key
	 */
	T read(PK id);
	
	/** Save changes made to a persistent object. */
	void update(T transientObject);
	
	/** Remove an object from persistent storage in the database */
	void delete(T persistentObject);
	
    /**
     * Find an entity by its primary key
     *
     * @param id the primary key
     * @return the entity
     */
    T findById(final PK id);

    /**
     * Load all entities
     *
     * @return the list of entities
     */
    List<T> findAll();
	
}


