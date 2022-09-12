package tw.org.twntch.db.dao;



/**
 * GenericDaoFactory 的實作類別
 * 用來做為其他 DAO factory 的父類別
 * 實際以 spring getBean 來取得 DAO，簡化 DaoFactory，並得以使用 string 來統一存取 DAO
 * @author andy
 *
 */
public class GenericDaoFactoryImpl implements GenericDaoFactory 
{
    //TODO 未完成
	@SuppressWarnings("rawtypes")
	//@Override
	public GenericDao getDao(String daoName) 
	{
		return null;
	}

	@SuppressWarnings("rawtypes")
	//@Override
	public GenericDao getDao(String daoName, String unitId, String sessionId) 
	{
		return null;
	}

}

