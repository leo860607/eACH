package tw.org.twntch.db.dao.hibernate;

import java.io.Serializable;
import java.util.List;
import org.hibernate.SQLQuery;

public class EACH_BATCH_Dao<T> extends HibernateEntityDao<T, Serializable> {
	
	public int do_BAT_RPONBLOCKTAB(){
		SQLQuery sqlquery =  getSession().createSQLQuery("call EACHUSER.BAT_RPONBLOCKTAB()");
		int result = sqlquery.executeUpdate();
		System.out.println("result>>"+result);
		return result;
	}
	public int do_BAT_RPONPENDINGTAB(){
		SQLQuery sqlquery =  getSession().createSQLQuery("call EACHUSER.BAT_RPONPENDINGTAB()");
		int result = sqlquery.executeUpdate();
		System.out.println("result>>"+result);
		return result;
	}
	public int do_BAT_RPONCLEARINGTAB(){
		SQLQuery sqlquery =  getSession().createSQLQuery("call EACHUSER.BAT_RPONCLEARINGTAB()");
		int result = sqlquery.executeUpdate();
		System.out.println("result>>"+result);
		return result;
	}
	public int do_BAT_RPCLEARFEETAB(){
		SQLQuery sqlquery =  getSession().createSQLQuery("call EACHUSER.BAT_RPCLEARFEETAB()");
		int result = sqlquery.executeUpdate();
		System.out.println("result>>"+result);
		return result;
	}
	public int do_BAT_RPDAILYSUM(){
		SQLQuery sqlquery =  getSession().createSQLQuery("call EACHUSER.BAT_RPDAILYSUM()");
		int result = sqlquery.executeUpdate();
		System.out.println("result>>"+result);
		return result;
	}
	public int do_BAT_RPMONTHSUM(){
		SQLQuery sqlquery =  getSession().createSQLQuery("call EACHUSER.BAT_RPMONTHSUM()");
		int result = sqlquery.executeUpdate();
		System.out.println("result>>"+result);
		return result;
	}
		
	public int do_BAT_RPTXNLOG(){
		SQLQuery sqlquery =  getSession().createSQLQuery("call EACHUSER.BAT_RPTXNLOG()");
		int result = sqlquery.executeUpdate();
		System.out.println("result>>"+result);
		return result;
	}
	public int do_BAT_RPDAILYTXNLOG(){
		SQLQuery sqlquery =  getSession().createSQLQuery("call EACHUSER.BAT_RPDAILYTXNLOG()");
		int result = sqlquery.executeUpdate();
		System.out.println("result>>"+result);
		return result;
	}

}
