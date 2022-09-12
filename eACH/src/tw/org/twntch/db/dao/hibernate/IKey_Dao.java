package tw.org.twntch.db.dao.hibernate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.support.JdbcDaoSupport;
public class IKey_Dao extends JdbcDaoSupport{
	/**
	 * 查詢方法，回傳值為List<Map>
	 * */
	public List<Map<String, Object>> list(String sql, Object[] paramObject){
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		if(paramObject!=null){
			resultList=getJdbcTemplate().queryForList(sql, paramObject);
		}else{
			resultList=getJdbcTemplate().queryForList(sql);
		}
		return resultList;
	}
}
