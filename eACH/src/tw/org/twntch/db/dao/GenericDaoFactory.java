package tw.org.twntch.db.dao;



/**
 * DAO factory 的介面
 * @author andy
 *
 */
public interface GenericDaoFactory 
{
	/**
	 * 取得預設 connection 之 DAO 物件
	 * @param daoName   DAO 名稱
	 * @return          指定的 DAO 物件或 null
	 */
	@SuppressWarnings("rawtypes")
	public GenericDao getDao(String daoName);
	
	/**
	 * 取得指定 connection 之 DAO 物件
	 * @param daoName     DAO 名稱
	 * @param unitId      單位別
	 * @param sessionId   session 區分
	 * @return            指定的 DAO 物件或 null
	 */
	@SuppressWarnings("rawtypes")
	public GenericDao getDao(String daoName, String unitId, String sessionId);
}

