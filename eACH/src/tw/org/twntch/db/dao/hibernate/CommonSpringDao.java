package tw.org.twntch.db.dao.hibernate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.support.JdbcDaoSupport;
//由於用hibernate查select sum(COUNT) FIRECOUNT from (SELECT count(*) as COUNT FROM TEMP a Where a.resultstatus = 'A' group by a.SENDERACQUIRE )會出現無法辨視COUNT的錯誤，因此用Spring查詢
//由於用hibernate查blob欄位會出現lob已關閉的sqlexception，因此用Spring查詢
public class CommonSpringDao extends JdbcDaoSupport{
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
