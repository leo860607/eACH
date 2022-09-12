package tw.org.twntch.bo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.struts.util.LabelValueBean;
import org.hibernate.loader.custom.sql.SQLCustomQuery;
import org.springframework.jdbc.object.SqlCall;

import tw.org.twntch.bean.TXS_DAY;
import tw.org.twntch.db.dao.hibernate.BANK_BRANCH_Dao;
import tw.org.twntch.db.dao.hibernate.BANK_GROUP_Dao;
import tw.org.twntch.db.dao.hibernate.EACH_TXN_CODE_Dao;
import tw.org.twntch.db.dao.hibernate.ONBLOCKTAB_Dao;
import tw.org.twntch.db.dao.hibernate.Page;
import tw.org.twntch.db.dao.hibernate.RPONBLOCKTAB_Dao;
import tw.org.twntch.po.BANK_BRANCH;
import tw.org.twntch.po.BANK_GROUP;
import tw.org.twntch.po.BANK_OPBK;
import tw.org.twntch.po.EACH_TXN_CODE;
import tw.org.twntch.po.TXN_CODE;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.RptUtils;
import tw.org.twntch.util.StrUtils;

public class AGENT_FEE_SEARCH_BO {
	private TXNLOG_BO txnlog_bo ;
	private BANK_GROUP_BO bank_group_bo;
	private EACH_TXN_CODE_Dao each_txn_code_Dao;
	private ONBLOCKTAB_Dao onblocktab_Dao;
	private BANK_BRANCH_Dao bank_branch_Dao;
	private RPONBLOCKTAB_Dao rponblocktab_Dao;
	
	
	/**
	 * 取得交易代號(3碼)清單
	 * @return
	 */
	public List<LabelValueBean> getTxnIdList(){
		List<TXN_CODE> list = txnlog_bo.getAgent_fee_code_bo().getTxn_code_Dao().getAll_OrderByTxnId();
		List<LabelValueBean> beanList = new LinkedList<LabelValueBean>();
		LabelValueBean bean = null;
		for(TXN_CODE po : list){
			bean = new LabelValueBean(po.getTXN_ID() + " - " + po.getTXN_NAME(), po.getTXN_ID());
			beanList.add(bean);
		}
		System.out.println("beanList>>"+beanList);
		return beanList;
	}
	
	
	
	public String pageSearch(Map<String, String> params){
		int pageNo = StrUtils.isEmpty(params.get("page")) ?0:Integer.valueOf(params.get("page"));
		int pageSize = StrUtils.isEmpty(params.get("rows")) ?Integer.valueOf(Arguments.getStringArg("PAGE.SIZE")):Integer.valueOf(params.get("rows"));
		int startIndex = 0 , lastIndex = 0 ;
		String conditionKey = "" , sql="" , countQuerySql ="" ;
		List list = null;
		Page page = null;
		Map rtnMap = new HashMap();
		List<Map<String,Integer>> cntlist = null;
		try {
			String sord =StrUtils.isNotEmpty(params.get("sord"))? params.get("sord"):"";
			String sidx =StrUtils.isNotEmpty(params.get("sidx"))? params.get("sidx"):"";
			String orderSQL = StrUtils.isNotEmpty(sidx)? " ORDER BY "+sidx+" "+sord:"";
			conditionKey = "[\"SBIZDATE\",\"PCODE\",\"AGENT_COMPANY_ID\",\"SND_COMPANY_ID\",\"CLEARINGPHASE\",\"TG_RESULT\",\"ROWNUMBER\" , \"TXID\"]";
			startIndex = Page.getStartOfPage(pageNo, pageSize) + 1;
			lastIndex = pageNo * pageSize;
			System.out.println("startIndex>>"+startIndex+"lastIndex>>"+lastIndex);
			params.put("ROWNUMBER", String.valueOf(startIndex));
			params.put("LAST_ROWNUMBER", String.valueOf(lastIndex));
			Map map = getConditionData(params, conditionKey);
			sql = getSQL(map.get("sqlPath").toString(), map.get("sqlPath2").toString() ,orderSQL ) ;
			countQuerySql = getCntSQL(map.get("sqlPath").toString(), map.get("sqlPath2").toString());
			cntlist = onblocktab_Dao.DataSumList(countQuerySql , (Map<String,String>) map.get("values"));
			rtnMap.put("dataSumList", cntlist);
			page = onblocktab_Dao.getDataRetPage(startIndex, pageSize, cntlist.get(0).get("RECORDS") , sql, (Map<String,String>) map.get("values") );
			if(page == null){
				rtnMap.put("total", "0");
				rtnMap.put("page", "0");
				rtnMap.put("records", "0");
				rtnMap.put("rows", new ArrayList());
			}else{
				list = (List) page.getResult();
				//System.out.println("list>>"+list);
				rtnMap.put("total", page.getTotalPageCount());
				rtnMap.put("page", String.valueOf(page.getCurrentPageNo()));
				rtnMap.put("records", page.getTotalCount());
				rtnMap.put("rows", list);
			}
		} catch (Exception e) {
			e.printStackTrace();
			rtnMap.put("total", "0");
			rtnMap.put("page", "0");
			rtnMap.put("records", "0");
			rtnMap.put("rows", new ArrayList());
		}
		String json = JSONUtils.map2json(rtnMap);
		return json;
	}
	

	public String getSQL(String sqlpath ,String sqlpath2, String ordersql){
		StringBuffer sql = new StringBuffer();
		sql.append(" WITH TEMP AS ( ");
		sql.append(" SELECT COALESCE(AGENT_COMPANY_ID ,'')  AGENT_COMPANY_ID  , COALESCE(SND_COMPANY_ID ,'') SND_COMPANY_ID , COALESCE(RESPONSECODE , '' ) RESPONSECODE  ");
		sql.append(" , COALESCE(COMPANY_ABBR_NAME ,'') COMPANY_ABBR_NAME , COALESCE(SND_COMPANY_ABBR_NAME ,'' ) SND_COMPANY_ABBR_NAME ");
//		20151118 note by hugo 此SQL需注意欄位型態，COALESCE(TRANSACTIONAMOUNT ,'')  NEWTXAMT 在SQL工具雖然可以執行 ，但在hibernate 會出現No Dialect mapping for JDBC type: 1111的錯誤
//		故改成  CAST(COALESCE(TRANSACTIONAMOUNT ,'0') AS INT)   NEWTXAMT
//		sql.append(" , COALESCE(TG_RESULT ,'') TG_RESULT , COALESCE(TRANSACTIONAMOUNT ,'')  NEWTXAMT , COALESCE(CLEARINGPHASE ,'') CLEARINGPHASE , COALESCE(PCODE , '' ) PCODE ");
		sql.append(" , COALESCE(TG_RESULT ,'') TG_RESULT , CAST(COALESCE(TRANSACTIONAMOUNT ,'0') AS DECIMAL(13,0))   NEWTXAMT , COALESCE(CLEARINGPHASE ,'') CLEARINGPHASE , COALESCE(PCODE , '' ) PCODE ");
		sql.append(" ,COALESCE(AGENT_FEE,0.00) AS AGENT_FEE ,TXID , TXN_NAME ");
		sql.append(" FROM  VW_TXNLOG  WHERE TG_RESULT = 'A' ");
		sql.append(sqlpath);
		sql.append(" ) ");
		sql.append(" SELECT A.AGENT_COMPANY_ID  ,A.SND_COMPANY_ID  ,A.COMPANY_ABBR_NAME , A.SND_COMPANY_ABBR_NAME  ");
		sql.append(" ,TXID , A.TXN_NAME , A.FIRECOUNT , A.AGENT_FEE ,  A.SUM_AGENT_FEE ");
		sql.append(" FROM ( ");
		sql.append(" 		SELECT ROWNUMBER() OVER() AS ROWNUMBER, T.* FROM ( ");
		sql.append(" 			SELECT AGENT_COMPANY_ID AS AGENT_COMPANY_ID , SND_COMPANY_ID AS SND_COMPANY_ID ,TXID , TXN_NAME, TG_RESULT ,COMPANY_ABBR_NAME , SND_COMPANY_ABBR_NAME  ,AGENT_FEE ,SUM(AGENT_FEE) AS SUM_AGENT_FEE , COUNT(*) AS FIRECOUNT FROM TEMP GROUP BY AGENT_COMPANY_ID, SND_COMPANY_ID , TXID , TXN_NAME, TG_RESULT ,COMPANY_ABBR_NAME , SND_COMPANY_ABBR_NAME ,AGENT_FEE   ");
		sql.append(" 		 ) AS T   ");
		sql.append(" ) AS A WHERE 1=1 ");
		sql.append(sqlpath2);
		sql.append(ordersql);
		System.out.println("getSQL>>"+sql);
				 
		return sql.toString();
	}
	
	public String getCntSQL(String sqlPath ,String sqlPath2  ){
		StringBuffer sql = new StringBuffer();
		sql.append(" WITH TEMP AS ( ");
		sql.append(" SELECT COALESCE(AGENT_COMPANY_ID ,'')  AGENT_COMPANY_ID  , COALESCE(SND_COMPANY_ID ,'') SND_COMPANY_ID , COALESCE(RESPONSECODE , '' ) RESPONSECODE  ");
		sql.append(" , COALESCE(COMPANY_ABBR_NAME ,'') COMPANY_ABBR_NAME , COALESCE(SND_COMPANY_ABBR_NAME ,'' ) SND_COMPANY_ABBR_NAME ");
		sql.append(" , COALESCE(TG_RESULT ,'') TG_RESULT , CAST(COALESCE(TRANSACTIONAMOUNT ,'0') AS DECIMAL(13,0))   NEWTXAMT , COALESCE(CLEARINGPHASE ,'') CLEARINGPHASE , COALESCE(PCODE , '' ) PCODE ");
		sql.append(" ,COALESCE(AGENT_FEE,0.00) AS AGENT_FEE ,TXID , TXN_NAME ");
		sql.append(" FROM  VW_TXNLOG  WHERE TG_RESULT = 'A' ");
		sql.append(sqlPath);
		sql.append(" ) ");
		sql.append(" SELECT  COUNT(*) AS RECORDS , SUM( FIRECOUNT) AS FIRECOUNT , SUM (AGENT_FEE)AS AGENT_FEE ");
		sql.append(" FROM (  ");
		sql.append(" 		SELECT  T.*  FROM ( ");
		sql.append(" 			 SELECT AGENT_COMPANY_ID AS AGENT_COMPANY_ID , SND_COMPANY_ID AS SND_COMPANY_ID ,TXID , TXN_NAME, TG_RESULT ,COMPANY_ABBR_NAME , SND_COMPANY_ABBR_NAME ,SUM(AGENT_FEE) AS AGENT_FEE , COUNT(*) AS FIRECOUNT FROM TEMP GROUP BY AGENT_COMPANY_ID, SND_COMPANY_ID , TXID , TXN_NAME, TG_RESULT ,COMPANY_ABBR_NAME , SND_COMPANY_ABBR_NAME   ");
		sql.append(" 		) AS T ");
		sql.append(" ) AS A ");
		
		System.out.println("getCntSQL.sql>>"+sql);
		return sql.toString();
	}
	
	
	public Map<String, String> qs_ex_export(Map<String,String> params){
		Map<String, String> rtnMap = null;
		String conditionKey = "" , sql=""  ;
		try{
			String sord =StrUtils.isNotEmpty(params.get("sortorder"))? params.get("sortorder"):"";
			String sidx =StrUtils.isNotEmpty(params.get("sortname"))? params.get("sortname"):"";
			String orderSQL = StrUtils.isNotEmpty(sidx)? " ORDER BY "+sidx+" "+sord:"";
			conditionKey = "[\"SBIZDATE\",\"TXID\",\"AGENT_COMPANY_ID\",\"SND_COMPANY_ID\",\"CLEARINGPHASE\"]";
			Map map = getConditionData(params, conditionKey);
			sql = getSQL(map.get("sqlPath").toString(), map.get("sqlPath2").toString() ,orderSQL ) ;
			rtnMap = new HashMap<String, String>();
			rtnMap.put("serchStrs", (String) params.get("serchStrs"));
			List list = rponblocktab_Dao.getRptData(sql, (Map<String,String>) map.get("values"));
			
			int pathType = Arguments.getStringArg("ISWINOPEN").equals("Y")?RptUtils.C_PATH :RptUtils.R_PATH;
			String outputFilePath = RptUtils.export(RptUtils.COLLECTION,pathType, "agent_fee_search", "agent_fee_search", new HashMap<String, Object>(), list, 2);
			//String outputFilePath = "";
			System.out.println("params>>"+params);
			System.out.println("map>>"+map);
			System.out.println("list>>"+list);
			System.out.println("sql>>"+sql);
			if(StrUtils.isNotEmpty(outputFilePath)){
				rtnMap.put("result", "TRUE");
				rtnMap.put("msg", outputFilePath);
			}else{
				rtnMap.put("result", "FALSE");
				rtnMap.put("msg", "產生失敗!");
			}
			System.out.println("EXPORT SQL >> " + sql);
		}catch(Exception e){
			e.printStackTrace();
			rtnMap.put("result", "FALSE");
			rtnMap.put("msg", "產生錯誤!");
		}
		
		return rtnMap;
	}
	
	
	
	public Map<String,Object> getConditionData(Map<String, String> params ,String conditionKey){
		Map<String,Object> retMap = new HashMap<String,Object>();
		
		List<String> keyList = JSONUtils.toList(conditionKey);
		Map<String, String> values = new HashMap<String,String>();
		StringBuffer sql = new StringBuffer();
		StringBuffer sql2 = new StringBuffer();
		try {
			for(String key : keyList){
				if(params.containsKey(key) && StrUtils.isNotEmpty(params.get(key)) && !params.get(key).equals("all")){
					if(key.equals("ROWNUMBER")){
						sql2.append(" AND ");
						sql2.append(key+" >= :"+key+" AND "+key+" <= :LAST_ROWNUMBER");
						values.put(key, params.get(key));
						values.put("LAST_ROWNUMBER", params.get("LAST_ROWNUMBER"));
						continue;
					}
					if(key.equals("SBIZDATE")){
						sql.append( " AND ");
						sql.append(" BIZDATE>= :"+key+" AND BIZDATE <= :EBIZDATE");
						values.put(key,DateTimeUtils.convertDate(params.get(key), "yyyymmdd", "yyyymmdd"));
						values.put("EBIZDATE",DateTimeUtils.convertDate(params.get("EBIZDATE"), "yyyymmdd", "yyyymmdd"));
						continue;
					}
					sql.append( " AND ");
					sql.append( key+" = :"+key);
					values.put(key,params.get(key));
				}
			}
			retMap.put("sqlPath", sql.toString());
			retMap.put("sqlPath2", sql2.toString());
			retMap.put("values", values);
			System.out.println("retMap>>"+retMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retMap;
	}

	public TXNLOG_BO getTxnlog_bo() {
		return txnlog_bo;
	}

	public void setTxnlog_bo(TXNLOG_BO txnlog_bo) {
		this.txnlog_bo = txnlog_bo;
	}

	public BANK_GROUP_BO getBank_group_bo() {
		return bank_group_bo;
	}

	public void setBank_group_bo(BANK_GROUP_BO bank_group_bo) {
		this.bank_group_bo = bank_group_bo;
	}

	public EACH_TXN_CODE_Dao getEach_txn_code_Dao() {
		return each_txn_code_Dao;
	}

	public void setEach_txn_code_Dao(EACH_TXN_CODE_Dao each_txn_code_Dao) {
		this.each_txn_code_Dao = each_txn_code_Dao;
	}

	public ONBLOCKTAB_Dao getOnblocktab_Dao() {
		return onblocktab_Dao;
	}

	public void setOnblocktab_Dao(ONBLOCKTAB_Dao onblocktab_Dao) {
		this.onblocktab_Dao = onblocktab_Dao;
	}

	public BANK_BRANCH_Dao getBank_branch_Dao() {
		return bank_branch_Dao;
	}

	public void setBank_branch_Dao(BANK_BRANCH_Dao bank_branch_Dao) {
		this.bank_branch_Dao = bank_branch_Dao;
	}

	public RPONBLOCKTAB_Dao getRponblocktab_Dao() {
		return rponblocktab_Dao;
	}

	public void setRponblocktab_Dao(RPONBLOCKTAB_Dao rponblocktab_Dao) {
		this.rponblocktab_Dao = rponblocktab_Dao;
	}
	
	
	
}
