package tw.org.twntch.db.dao.hibernate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;

import tw.org.twntch.po.ONCLEARINGTAB;
import tw.org.twntch.po.ONCLEARINGTAB_PK;
import tw.org.twntch.util.AutoAddScalar;
import tw.org.twntch.util.StrUtils;


public class SETTLE_DAY_Dao extends HibernateEntityDao<ONCLEARINGTAB,ONCLEARINGTAB_PK> {
	public Map<String,Object> queryData(int pageNo, int pageSize, String dataSumSQL, String sql, String sql2, String sql3, String[] dataSumCols, String[] cols, Class targetClass){
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<ONCLEARINGTAB> list = new ArrayList<ONCLEARINGTAB>();
		//先算出總計的欄位值
		list = query(dataSumSQL,dataSumCols,targetClass);
		
		for(ONCLEARINGTAB po:list){
			System.out.println(String.format("SUM(DECIMAL(A.RECVCNT))=%s, SUM(DECIMAL(A.RECVAMT))=%s, SUM(DECIMAL(A.PAYCNT))=%s, SUM(DECIMAL(A.PAYAMT))=%s, SUM(DECIMAL(A.RVSRECVCNT))=%s, SUM(DECIMAL(A.RVSRECVAMT))=%s, SUM(DECIMAL(A.RVSPAYCNT))=%s, SUM(DECIMAL(A.RVSPAYAMT))=%s",
				po.getRECVCNT(),po.getRECVAMT(),po.getPAYCNT(),po.getPAYAMT(),po.getRVSRECVCNT(),po.getRVSRECVAMT(),po.getRVSPAYCNT(),po.getRVSPAYAMT()));
		}
		dataMap.put("dataSumList", list);
		
		//計算分頁用
		int startIndex;
		int lastIndex = pageNo * pageSize;
		
		list = query(sql+sql3,cols,targetClass);
		System.out.println("list="+list);
		int totalCount = list.size();
		Page page;
		//如果沒有排序的話
		if(StrUtils.isEmpty(sql2)){
			List<ONCLEARINGTAB> querylist = new ArrayList<ONCLEARINGTAB>();
			startIndex = Page.getStartOfPage(pageNo, pageSize);
			for(int x=startIndex;x<lastIndex;x++){
				if(x<totalCount){
					querylist.add(list.get(x));
				}
			}
			page = new Page(startIndex, totalCount, pageSize, querylist);
		}
		//如果有排序的話
		else{
			startIndex = Page.getStartOfPage(pageNo, pageSize) + 1;
			sql3 += " ) AS TEMP_ WHERE ROWNUMBER >= "+startIndex +" AND ROWNUMBER <= "+lastIndex;
			list = query(sql+sql2+sql3,cols,targetClass);
			page = new Page(startIndex, totalCount, pageSize, list);
		}
		
		if(page == null){
			dataMap.put("total", "0");
			dataMap.put("page", "0");
			dataMap.put("records", "0");
			dataMap.put("rows", new ArrayList());
		}
		else{
			dataMap.put("total", page.getTotalPageCount());
			dataMap.put("page", String.valueOf(page.getCurrentPageNo()));
			dataMap.put("records", page.getTotalCount());
			dataMap.put("rows", page.getResult());
		}
		return dataMap;
	}
	
	public List<ONCLEARINGTAB> query(String SQL,String[] cols,Class targetClass){
		SQLQuery query =  getCurrentSession().createSQLQuery(SQL);
		AutoAddScalar addscalar = new AutoAddScalar();
		addscalar.addScalar(query,targetClass,cols);
		query.setResultTransformer(Transformers.aliasToBean(targetClass));
		return query.list();
	}
}
