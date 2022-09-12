package tw.org.twntch.db.dataaccess;

import java.sql.Connection;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.engine.SessionFactoryImplementor;
import org.springframework.orm.hibernate3.LocalDataSourceConnectionProvider;
import tw.org.twntch.util.SpringAppCtxHelper;
import tw.org.twntch.util.StrUtils;



/**
 * 使用 spring 取得 Hibernate 的 session</br>
 * 再經由 session 取得 connection</br>
 * 由 spring 來設定 transaction manager 套用至 session</br>
 * 經由此物件所取得的 connection 可以與 Hibernate DAO 一起進行 commit 或 rollback</br>
 * 相容新舊程式之寫法</br>
 * 重點是要讓 jdbc 所異動的資料也能讓  hibernate 可以取得，反之亦然</br>
 * 也可以使用 相同的 DataSource 來生成 JdbcTemplate並取得 jdbc connection</br>
 * @author andy
 *
 */
public class ConnectionManager
{
	public static final Logger log = Logger.getLogger(ConnectionManager.class.getName());
	
	public static Connection cn = null;
	
	public static synchronized Connection getConnection(String jndi)
	{
		log.info("enter getConnection");
		Connection lcn = null;
		SessionFactory sessionFactory = null;
		String sessionName = "";
//		String daoName = "";
		if (StrUtils.isEmpty(jndi))
		{
			log.debug("null jndi name");
			System.out.println("null jndi name");
			return lcn;
		}
		
		System.out.println("JNDI=" + jndi);
		log.debug("JNDI=" + jndi);
		
		try
		{
			if (jndi.equalsIgnoreCase("jdbc/ACH"))
			{
				sessionName = "sessionFactory_eACH";
//				daoName = "plDao";
			}
			if (StrUtils.isNotEmpty(sessionName))
			{
				log.debug("Session Name=" + sessionName);

				//也可以使用 相同的 DataSource 來生成 JdbcTemplate並取得 jdbc connection
				//重點是要讓 jdbc 所異動的資料也能讓  hibernate 可以取得，反之亦然
				sessionFactory = SpringAppCtxHelper.getBean(sessionName);
				LocalDataSourceConnectionProvider cp = (LocalDataSourceConnectionProvider) ((SessionFactoryImplementor)sessionFactory).getConnectionProvider();    
				lcn = cp.getConnection(); 
				
//				ConnectionProvider pr = SpringAppCtxHelper.getBean(daoName);
//				lcn = pr.getConnection();
//				HibernateEntityDao dao = (HibernateEntityDao) pr;
				
//使用此方法，取到的是已經 close 的 connection				
//              sessionFactory.getCurrentSession().doWork(new Work() 
//				{
//				    public void execute(Connection connection) throws SQLException 
//				    {
//				    	//cn 只是媒介，將 connection 傳出來，用完要清掉
//				    	cn = connection;
//				    }
//				});
//				
//				lcn = cn;
//				cn = null;
				
			}
		}
		catch(Exception e)
		{
			log.error(e.toString());	
			e.printStackTrace();
		}
		finally
		{
			log.info("leave getConnection");
		}
		
		return lcn;
	}

	
}

