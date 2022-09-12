package tw.org.twntch.bo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import tw.org.twntch.bean.FAIL_TRANS;
import tw.org.twntch.db.dao.hibernate.ONBLOCKTAB_Dao;
import tw.org.twntch.db.dao.hibernate.Page;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.StrUtils;

public class FAIL_TRANS_BO {
	private ONBLOCKTAB_Dao onblocktab_Dao;

	public String pageSearch(Map<String, String> param){
		int pageNo = StrUtils.isEmpty(param.get("page"))? 0 : Integer.valueOf(param.get("page"));
		int pageSize = StrUtils.isEmpty(param.get("rows"))? Integer.valueOf(Arguments.getStringArg("PAGE.SIZE")) : Integer.valueOf(param.get("rows"));
		StringBuffer countQuerySql = new StringBuffer();
		StringBuffer sql = new StringBuffer();
		String result = "";
		List list = null;
		Map rtnMap = null;
		List<String> conditions = new ArrayList<String>();
		String condition = "";
		Page page = null;
		try {
			
			String sdate = "";
			//日期起
			if(StrUtils.isNotEmpty(param.get("TXDT").trim())){
				sdate = param.get("TXDT").trim();
				System.out.println("sdate="+sdate);
				conditions.add(" a.BIZDATE >= '" + DateTimeUtils.convertDate(sdate, "yyyymmdd", "yyyymmdd")+"000000" + "' ");
			}
			String edate = "";
			//日期迄
			if(StrUtils.isNotEmpty(param.get("ETXDT").trim())){
				edate = param.get("ETXDT").trim();
				System.out.println("edate="+edate);
				conditions.add(" a.BIZDATE <= '" + DateTimeUtils.convertDate(edate, "yyyymmdd", "yyyymmdd")+"235959" + "' ");
			}		
			String senderacquire = "";
			//操作行代號
			if(StrUtils.isNotEmpty(param.get("OPBK_ID").trim())){
				senderacquire = param.get("OPBK_ID").trim();
				System.out.println("SENDERACQUIRE="+senderacquire);
				conditions.add(" a.SENDERACQUIRE = '" + senderacquire + "' ");
			}
			String bgbkId = "";
			//操作行代號
			if(StrUtils.isNotEmpty(param.get("BGBK_ID").trim())){
				bgbkId = param.get("BGBK_ID").trim();
				conditions.add(" a.SENDERHEAD = '" + bgbkId + "' ");
			}
			
			for(int i = 0; i < conditions.size(); i++){
				condition += conditions.get(i);
				if(i < conditions.size() - 1){
					condition += " AND ";
				}
			}
			System.out.println("condition="+condition);
			
			countQuerySql.append("WITH TEMP AS (SELECT a.txDate, a.Stan ,coalesce(a.senderAcquire, a.senderBankId) senderAcquire, ");
			countQuerySql.append("a.inAcquire, (case when a.senderAcquire=a.inAcquire then 'S' else 'O' end) TransType, ");
			countQuerySql.append(" (case when a.resultstatus='P' then (case when b.RESULTCODE='00' then 'A' when b.RESULTCODE='01' then 'R' else 'P' end) else  a.resultstatus end )  resultstatus, ");
			countQuerySql.append("a.CONRESULTCODE, ");
			countQuerySql.append("c.ERR_DESC TXN_ERR_DESC, ");
			countQuerySql.append("d.ERR_DESC GL_ERR_DESC ");
			countQuerySql.append("FROM ONBLOCKTAB  A  left join ONPENDINGTAB b on b.OTXDate=a.txDATE and b.OSTAN=a.STAN ");
			countQuerySql.append("left join TXN_ERROR_CODE c on c.ERROR_ID=a.CONRESULTCODE  left join GL_ERROR_CODE d on d.ERROR_ID=a.CONRESULTCODE ");
			countQuerySql.append("where "+condition);
			countQuerySql.append(" ) ");
			countQuerySql.append(" SELECT ");
			countQuerySql.append(" a.senderAcquire || '-' || (select coalesce(bgbk_name,'') from bank_group where bgbk_id=a.senderAcquire) senderAcquire ");
			countQuerySql.append(",count(*)  TotalCount ");
			countQuerySql.append(",(SELECT COUNT(*) FROM TEMP B WHERE B.senderAcquire = A.senderAcquire and resultstatus='A' ) SuccessCount ");
			countQuerySql.append(",(SELECT COUNT(*) FROM TEMP B WHERE B.senderAcquire = A.senderAcquire and resultstatus !='A' and TransType='S' and  (GL_ERR_DESC is not null  and  TXN_ERR_DESC is null) ) SysErrSelf ");
			countQuerySql.append(",(SELECT COUNT(*) FROM TEMP B WHERE B.senderAcquire = A.senderAcquire and resultstatus !='A' and TransType='O' and  (GL_ERR_DESC is not null  and  TXN_ERR_DESC is null) ) SysErrOut ");
			countQuerySql.append(",(SELECT COUNT(*) FROM TEMP B WHERE B.senderAcquire = A.senderAcquire and resultstatus !='A' and TransType='S' and  (GL_ERR_DESC is  null  and  TXN_ERR_DESC is not null) ) TXNErrSelf ");
			countQuerySql.append(",(SELECT COUNT(*) FROM TEMP B WHERE B.senderAcquire = A.senderAcquire and resultstatus !='A' and TransType='O' and  (GL_ERR_DESC is  null  and  TXN_ERR_DESC is not null) ) TXNErrOut ");
			countQuerySql.append(",(SELECT COUNT(*) FROM TEMP B WHERE B.senderAcquire = A.senderAcquire and resultstatus !='A' and TransType='S' and  (GL_ERR_DESC is  null  and  TXN_ERR_DESC is null) ) OthErrSelf ");
			countQuerySql.append(",(SELECT COUNT(*) FROM TEMP B WHERE B.senderAcquire = A.senderAcquire and resultstatus !='A' and TransType='O' and  (GL_ERR_DESC is  null  and  TXN_ERR_DESC is null) ) OthErrOut ");
			countQuerySql.append(",(SELECT COUNT(*) FROM TEMP B WHERE B.senderAcquire = A.senderAcquire and resultstatus !='A' ) FailCount ");
			countQuerySql.append("FROM TEMP a ");
			countQuerySql.append("group by a.senderAcquire");
			
			sql.append("WITH TEMP AS (SELECT a.txDate, a.Stan ,coalesce(a.senderAcquire, a.senderBankId) senderAcquire, ");
			sql.append("a.inAcquire, (case when a.senderAcquire=a.inAcquire then 'S' else 'O' end) TransType, ");
			sql.append(" (case when a.resultstatus='P' then (case when b.RESULTCODE='00' then 'A' when b.RESULTCODE='01' then 'R' else 'P' end) else  a.resultstatus end )  resultstatus, ");
			sql.append("a.CONRESULTCODE, ");
			sql.append("c.ERR_DESC TXN_ERR_DESC, ");
			sql.append("d.ERR_DESC GL_ERR_DESC ");
			sql.append("FROM ONBLOCKTAB  A  left join ONPENDINGTAB b on b.OTXDate=a.txDATE and b.OSTAN=a.STAN ");
			sql.append("left join TXN_ERROR_CODE c on c.ERROR_ID=a.CONRESULTCODE  left join GL_ERROR_CODE d on d.ERROR_ID=a.CONRESULTCODE ");
			sql.append("where "+condition);
			sql.append(" ) ");
			sql.append("SELECT * FROM ( SELECT ROWNUMBER() OVER(");
			if(StrUtils.isNotEmpty(param.get("sidx"))){
				if("TOTALCOUNT".equalsIgnoreCase(param.get("sidx"))){
					sql.append(" ORDER BY count(*) "+param.get("sord"));
				}
				else if("SUCCESSCOUNT".equalsIgnoreCase(param.get("sidx"))){
					sql.append(" ORDER BY (SELECT COUNT(*) FROM TEMP B WHERE B.senderAcquire = A.senderAcquire and resultstatus='A' ) "+param.get("sord"));
				}
				else if("SYSERRSELF".equalsIgnoreCase(param.get("sidx"))){
					sql.append(" ORDER BY (SELECT COUNT(*) FROM TEMP B WHERE B.senderAcquire = A.senderAcquire and resultstatus !='A' and TransType='S' and  (GL_ERR_DESC is not null  and  TXN_ERR_DESC is null) ) "+param.get("sord"));
				}
				else if("SYSERROUT".equalsIgnoreCase(param.get("sidx"))){
					sql.append(" ORDER BY (SELECT COUNT(*) FROM TEMP B WHERE B.senderAcquire = A.senderAcquire and resultstatus !='A' and TransType='O' and  (GL_ERR_DESC is not null  and  TXN_ERR_DESC is null) ) "+param.get("sord"));
				}
				else if("TXNERRSELF".equalsIgnoreCase(param.get("sidx"))){
					sql.append(" ORDER BY (SELECT COUNT(*) FROM TEMP B WHERE B.senderAcquire = A.senderAcquire and resultstatus !='A' and TransType='S' and  (GL_ERR_DESC is  null  and  TXN_ERR_DESC is not null) ) "+param.get("sord"));
				}
				else if("TXNERROUT".equalsIgnoreCase(param.get("sidx"))){
					sql.append(" ORDER BY (SELECT COUNT(*) FROM TEMP B WHERE B.senderAcquire = A.senderAcquire and resultstatus !='A' and TransType='O' and  (GL_ERR_DESC is  null  and  TXN_ERR_DESC is not null) ) "+param.get("sord"));
				}
				else if("OTHERRSELF".equalsIgnoreCase(param.get("sidx"))){
					sql.append(" ORDER BY (SELECT COUNT(*) FROM TEMP B WHERE B.senderAcquire = A.senderAcquire and resultstatus !='A' and TransType='S' and  (GL_ERR_DESC is  null  and  TXN_ERR_DESC is null) ) "+param.get("sord"));
				}
				else if("OTHERROUT".equalsIgnoreCase(param.get("sidx"))){
					sql.append(" ORDER BY (SELECT COUNT(*) FROM TEMP B WHERE B.senderAcquire = A.senderAcquire and resultstatus !='A' and TransType='O' and  (GL_ERR_DESC is  null  and  TXN_ERR_DESC is null) ) "+param.get("sord"));
				}
				else if("FAILCOUNT".equalsIgnoreCase(param.get("sidx"))){
					sql.append(" ORDER BY (SELECT COUNT(*) FROM TEMP B WHERE B.senderAcquire = A.senderAcquire and resultstatus !='A' ) "+param.get("sord"));
				}
				else if("CALCULATE".equalsIgnoreCase(param.get("sidx"))){
					sql.append(" ORDER BY DOUBLE((SELECT COUNT(*) FROM TEMP B WHERE B.senderAcquire = A.senderAcquire and resultstatus !='A' ))/DOUBLE(count(*)) "+param.get("sord"));
				}
				else{
					sql.append(" ORDER BY "+param.get("sidx")+" "+param.get("sord"));
				}
			}
			sql.append(") AS ROWNUMBER,  a.senderAcquire || '-' || (select coalesce(bgbk_name,'') from bank_group where bgbk_id=a.senderAcquire) senderAcquire ");
			sql.append(",count(*)  TotalCount ");
			sql.append(",(SELECT COUNT(*) FROM TEMP B WHERE B.senderAcquire = A.senderAcquire and resultstatus='A' ) SuccessCount ");
			sql.append(",(SELECT COUNT(*) FROM TEMP B WHERE B.senderAcquire = A.senderAcquire and resultstatus !='A' and TransType='S' and  (GL_ERR_DESC is not null  and  TXN_ERR_DESC is null) ) SysErrSelf ");
			sql.append(",(SELECT COUNT(*) FROM TEMP B WHERE B.senderAcquire = A.senderAcquire and resultstatus !='A' and TransType='O' and  (GL_ERR_DESC is not null  and  TXN_ERR_DESC is null) ) SysErrOut ");
			sql.append(",(SELECT COUNT(*) FROM TEMP B WHERE B.senderAcquire = A.senderAcquire and resultstatus !='A' and TransType='S' and  (GL_ERR_DESC is  null  and  TXN_ERR_DESC is not null) ) TXNErrSelf ");
			sql.append(",(SELECT COUNT(*) FROM TEMP B WHERE B.senderAcquire = A.senderAcquire and resultstatus !='A' and TransType='O' and  (GL_ERR_DESC is  null  and  TXN_ERR_DESC is not null) ) TXNErrOut ");
			sql.append(",(SELECT COUNT(*) FROM TEMP B WHERE B.senderAcquire = A.senderAcquire and resultstatus !='A' and TransType='S' and  (GL_ERR_DESC is  null  and  TXN_ERR_DESC is null) ) OthErrSelf ");
			sql.append(",(SELECT COUNT(*) FROM TEMP B WHERE B.senderAcquire = A.senderAcquire and resultstatus !='A' and TransType='O' and  (GL_ERR_DESC is  null  and  TXN_ERR_DESC is null) ) OthErrOut ");
			sql.append(",(SELECT COUNT(*) FROM TEMP B WHERE B.senderAcquire = A.senderAcquire and resultstatus !='A' ) FailCount ");
			sql.append("FROM TEMP a ");
			sql.append("group by a.senderAcquire");
			
			
			System.out.println("countQuerySql==>"+countQuerySql.toString());
			System.out.println("sql==>"+sql.toString());
			String cols[] = {"SENDERACQUIRE", "TOTALCOUNT", "SUCCESSCOUNT", "SYSERRSELF", "SYSERROUT", "TXNERRSELF", "TXNERROUT", "OTHERRSELF", "OTHERROUT", "FAILCOUNT"};
			page = onblocktab_Dao.getData(pageNo, pageSize, countQuerySql.toString(), sql.toString(), cols, FAIL_TRANS.class);
			list = (List) page.getResult();
			rtnMap = new HashMap();
			rtnMap.put("total", page.getTotalPageCount());
			rtnMap.put("page", String.valueOf(page.getCurrentPageNo()));
			rtnMap.put("records", page.getTotalCount());
			rtnMap.put("rows", list);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rtnMap.put("total", "0");
			rtnMap.put("page", "0");
			rtnMap.put("records", "0");
			rtnMap.put("rows", new ArrayList());
		}
		result = JSONUtils.map2json(rtnMap) ;
		System.out.println("FAIL_TRANS_BO>>"+result);
		return result;
	}

	public Map getPathSQL(Map<String, String> param , List<String> list){
		StringBuffer sql = new StringBuffer();
		List values = new LinkedList();
		Map map = new HashMap();
		for(String key :list){
			param.get(key);
			if(StrUtils.isNotEmpty(param.get(key))){
				if(list.indexOf(key) == 0){ sql.append(" WHERE "); }
				if(list.indexOf(key) != 0){ sql.append(" AND "); }
				if(key.equals("TXDT")){
					sql.append("a."+key +" BETWEEN ? ");
					values.add(DateTimeUtils.convertDate(param.get(key), "yyyymmdd", "yyyymmdd")+"000000");
				}else if(key.equals("ETXDT")){
					sql.append(" ? ");
					values.add(DateTimeUtils.convertDate(param.get(key), "yyyymmdd", "yyyymmdd")+"235959");
				}else{
					sql.append("a."+key+" = ?");
					values.add(param.get(key));
				}
			}
		}
		sql.append(" ");
		map.put("sqlPath", sql.toString());
		map.put("values", values);
		System.out.println("getPathSQL.map>>"+map);
		return map;
	}
	
	public ONBLOCKTAB_Dao getOnblocktab_Dao() {
		return onblocktab_Dao;
	}

	public void setOnblocktab_Dao(ONBLOCKTAB_Dao onblocktab_Dao) {
		this.onblocktab_Dao = onblocktab_Dao;
	}

	
	
	
}
