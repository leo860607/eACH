package tw.org.twntch.bo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.struts.util.LabelValueBean;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;

import tw.org.twntch.db.dao.hibernate.BANK_GROUP_Dao;
import tw.org.twntch.db.dao.hibernate.EACH_TXN_CODE_Dao;
import tw.org.twntch.db.dao.hibernate.OPC_TRANS_Dao;
import tw.org.twntch.db.dao.hibernate.Page;
import tw.org.twntch.po.BANK_GROUP;
import tw.org.twntch.po.EACH_TXN_CODE;
import tw.org.twntch.po.ONBLOCKTAB;
import tw.org.twntch.po.OPCTRANSACTIONLOGTAB;
import tw.org.twntch.util.AutoAddScalar;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.StrUtils;

public class AGENT_OPC_TRANS_BO {
	private OPC_TRANS_Dao opc_trans_Dao;
	private EACH_USERLOG_BO userlog_bo;//寫操作軌跡記錄
	
	
	public Map<String,Object> getConditionData(Map<String, String> params ,String conditionKey) throws Exception{
		List<String> keyList = JSONUtils.toList(conditionKey);
		Map<String,Object> retMap = new HashMap<String,Object>();
		Map<String, String> values = new HashMap<String,String>();
		StringBuffer sql = new StringBuffer(); 
		StringBuffer sql2 = new StringBuffer(); 
		try {
			sql.append( " WHERE 1=1 ");
			sql2.append( " WHERE 1=1 ");
			for(String key : keyList){
				if(params.containsKey(key) && StrUtils.isNotEmpty(params.get(key)) && !params.get(key).equals("all")){
					
					if(key.equals("ROWNUMBER")){
						sql2.append(" AND ");
						sql2.append(key+" >= :"+key+" AND "+key+" <= :LAST_ROWNUMBER");
						values.put(key, params.get(key));
						values.put("LAST_ROWNUMBER", params.get("LAST_ROWNUMBER"));
						continue;
					}
					if(key.equals("TXDATE")){
						sql.append( " AND ");
						sql.append(key+" = :"+key);
						values.put(key,DateTimeUtils.convertDate(params.get(key), "yyyymmdd", "yyyymmdd"));
						continue;
					}
					if(key.equals("TXTIME_START") ){
						sql.append( " AND ");
						sql.append(" TXTIME >= :TXTIME_START AND TXTIME <= :TXTIME_END");
						values.put(key,params.get(key));
						values.put("TXTIME_END",params.get("TXTIME_END"));
						continue;
					}
					if(key.equals("SENDER_TYPE") && params.get(key).equals("ACH")){
						sql.append( " AND ");
						sql.append( " ISSUERID = '9990000' ");
						continue;
					}
					if(key.equals("RECEIVER_TYPE") && params.get(key).equals("ACH")){
						sql.append( " AND ");
						sql.append( " RECEIVERID = '9990000' ");
						continue;
					}
					if(key.equals("SENDER_TYPE") || key.equals("RECEIVER_TYPE")){
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
	public String getCntSQL(String sqlPath ,String sqlPath2  ){
		
		StringBuffer sql = new StringBuffer();
		sql.append(" WITH TEMP AS( ");
		sql.append(" SELECT TXDATE ,TXTIME , ISSUERID ,COALESCE(STAN,'') STAN,AGENT_COMPANY_ID ");
		sql.append(" , COALESCE(PROCESSCODE,'') PROCESSCODE  ,COALESCE(RESPONSECODE ,'') AS RESPONSECODE , RESPONSECODE_NAME ,COALESCE( RSPCODE ,'') AS RSPCODE, RSPCODE_NAME ");
		sql.append(" FROM VW_OPC_TXNLOG  ");
		sql.append(sqlPath);
		sql.append(" ) ");
		sql.append(" SELECT COUNT(*) AS RECORDS FROM ( ");
		sql.append(" 	SELECT ROWNUMBER() OVER() AS ROWNUMBER, T.* FROM (  ");
		sql.append(" 		SELECT * FROM TEMP ) AS T ");
		sql.append(" 	) AS A ");
		sql.append(sqlPath2);
		System.out.println("getCntSQL.sql>>"+sql);
		return sql.toString();
	}
	
	public String getSQL(String sqlPath , String sqlPath2 ,String ordersql){
		StringBuffer sql = new StringBuffer();
		sql.append(" WITH TEMP AS( ");
		sql.append(" SELECT TXDATE ,TXTIME,SEQ , ISSUERID ,COALESCE(STAN,'') STAN,AGENT_COMPANY_ID ,COMPANY_ABBR_NAME");
		sql.append(" , COALESCE(PROCESSCODE,'') PROCESSCODE  ,COALESCE(RESPONSECODE ,'') AS RESPONSECODE , RESPONSECODE_NAME ,COALESCE( RSPCODE ,'') AS RSPCODE, RSPCODE_NAME ");
		sql.append(" FROM VW_OPC_TXNLOG  ");
		sql.append(sqlPath);
		sql.append(ordersql);
		sql.append(" ) ");
		sql.append(" SELECT * FROM ( ");
		sql.append(" 	SELECT ROWNUMBER() OVER() AS ROWNUMBER, T.* FROM (  ");
		sql.append(" 		SELECT * FROM TEMP ) AS T ");
		sql.append(" 	) AS A ");
		sql.append(sqlPath2);
//		 AGENT_COMPANY_ID='11111111'   AND PROCESSCODE = '2000' 
//		AND TXTIME >='16:40:00' AND  TXTIME <='17:00:00' --AND TXDATE = '20151201'
//		ORDER BY  TXDATE DESC , TXTIME DESC,STAN,AGENT_COMPANY_ID
		
		
		
		
//		SELECT 
//		TRANSLATE('abcdefgh ij:kl:mn', TRANSACTIONDATETIME, 'abcdefghijklmn') TRANSACTIONDATETIME,STAN , ISSUERID
//		--要依照查詢調件切換
//		--發動端非ACH
//		,COALESCE( ISSUERID ,'') AS AGENT_COMPANY_ID , COALESCE((SELECT COMPANY_ABBR_NAME FROM AGENT_PROFILE AP  WHERE AP.COMPANY_ID = ISSUERID FETCH FIRST 1 ROW ONLY ),'') AS COMPANY_ABBR_NAME
//		--發動端為ACH
//		,COALESCE( RECEIVERID ,'') AS AGENT_COMPANY_ID , COALESCE((SELECT COMPANY_ABBR_NAME FROM AGENT_PROFILE AP  WHERE AP.COMPANY_ID = RECEIVERID FETCH FIRST 1 ROW ONLY),'') AS COMPANY_ABBR_NAME
//		, COALESCE(PROCESSCODE,'') PROCESSCODE  ,COALESCE(RESPONSECODE ,'') AS RESPONSECODE , COALESCE((SELECT ERR_DESC FROM TXN_ERROR_CODE TEC WHERE TEC.ERROR_ID = RESPONSECODE),'') AS RESPONSECODE_NAME ,COALESCE( RSPCODE ,'') AS RSPCODE, COALESCE((SELECT ERR_DESC FROM TXN_ERROR_CODE TEC WHERE TEC.ERROR_ID = RSPCODE),'') AS RSPCODE_NAME
//		FROM VW_OPC_TXNLOG WHERE ISSUERID='11111111' AND RECEIVERID = '9990000' AND TXDATE = '20151201' AND PROCESSCODE = '2000' 
//		AND TRANSLATE('ijklmn', TRANSACTIONDATETIME, 'abcdefghijklmn') >= '144000' AND TRANSLATE('ijklmn', TRANSACTIONDATETIME, 'abcdefghijklmn') <= '214000'
		System.out.println("sql>>"+sql);
		return sql.toString();
	}
	
	public String getDataByStan(Map<String, String> params){
		String conditionKey = "" ,countQuerySql="",sql = "" , result="";
		int pageNo = StrUtils.isEmpty(params.get("page")) ?0:Integer.valueOf(params.get("page"));
		int pageSize = StrUtils.isEmpty(params.get("rows")) ?Integer.valueOf(Arguments.getStringArg("PAGE.SIZE")):Integer.valueOf(params.get("rows"));
		Integer startIndex = 0,lastIndex=0 ,totalCount=0;
		conditionKey = "[\"TXDATE\" , \"TXTIME_START\" ,\"AGENT_COMPANY_ID\",\"PROCESSCODE\",\"RESPONSECODE\",\"SENDER_TYPE\",\"RECEIVER_TYPE\",\"ROWNUMBER\"]";
		String isSearch = StrUtils.isEmpty(params.get("isSearch")) ?"Y":params.get("isSearch");
		Map rtnMap = new HashMap();
		Map pkMap = null ;
		List list = null;
		List<Map<String,Integer>> cntlist = null;
		Page page = null;
		try{
			String sord =StrUtils.isNotEmpty(params.get("sord"))? params.get("sord"):"";
			String sidx =StrUtils.isNotEmpty(params.get("sidx"))? params.get("sidx"):"";
			String orderSQL = StrUtils.isNotEmpty(sidx)? " ORDER BY "+sidx+" "+sord:"";
			pkMap = StrUtils.isEmpty(params.get("seachStr"))?new HashMap(): JSONUtils.json2map(params.get("seachStr"));
			startIndex = Page.getStartOfPage(pageNo, pageSize) + 1;
			lastIndex = pageNo  * pageSize;
			System.out.println("startIndex>>"+startIndex+"lastIndex>>"+lastIndex);
			params.put("ROWNUMBER", startIndex.toString());
			params.put("LAST_ROWNUMBER", lastIndex.toString());
			Map<String,Object> map = getConditionData(params, conditionKey);
			countQuerySql = getCntSQL(map.get("sqlPath").toString(), "");
			sql = getSQL(map.get("sqlPath").toString(), map.get("sqlPath2").toString(), orderSQL);
			cntlist = opc_trans_Dao.DataSumList(countQuerySql, (Map)map.get("values"));
			totalCount = cntlist.get(0).get("RECORDS");
			page = opc_trans_Dao.getDataRetPage(startIndex, pageSize, totalCount, sql, (Map)map.get("values"));
		//寫操作軌跡記錄(成功) 必須是按下UI的查詢才紀錄
			if(isSearch.equals("Y")){
				userlog_bo.writeLog("C",null,null,pkMap);
			}
		}catch(Exception e){
			e.printStackTrace();
			rtnMap.put("total", "0");
			rtnMap.put("page", "0");
			rtnMap.put("records", "0");
			rtnMap.put("rows", new ArrayList());
			rtnMap.put("result", "FALSE");
			rtnMap.put("msg", "查詢失敗，系統異常");
			rtnMap.put("errmsg", e.toString());
			result = JSONUtils.map2json(rtnMap);
			return result;
		}
		
		
		if(page == null){
			rtnMap.put("total", "0");
			rtnMap.put("page", "0");
			rtnMap.put("records", "0");
			rtnMap.put("rows", new ArrayList());
			rtnMap.put("result", "TRUE");
		}else{
			list = (List) page.getResult();
			rtnMap.put("total", page.getTotalPageCount());
			rtnMap.put("page", String.valueOf(page.getCurrentPageNo()));
			rtnMap.put("records", page.getTotalCount());
			rtnMap.put("rows", list);
			rtnMap.put("result", "TRUE");
		}
		
		result = JSONUtils.map2json(rtnMap);
		System.out.println(result);
		return result;
	}
	
	
	
	
	
	
	
	public List getDetail(Map<String, String> params){
		String result = "";
		String paramName;
		String paramValue;
		
		String TXDATE = "";
		paramName = "TXDATE";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			TXDATE = paramValue;
		}
		
		String SEQ = "";
		paramName = "SEQ";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			SEQ = paramValue;
		}
		System.out.println("TXDATE = " + TXDATE);
		System.out.println("SEQ = " + SEQ);
		
		//依據PCODE，查詢不同TABLE
		List list = null;
		String sql = "SELECT * FROM VW_OPC_TXNLOG  "
				+ "WHERE TXDATE = '"+TXDATE+"' AND SEQ = '"+SEQ+"'";
		list = opc_trans_Dao.getData(sql);
		//System.out.println(JSONUtils.toJson(list));
		
		return list;
	}
	
	//決定檢視明細的種類為 A(OPCTRANSACTIONLOGTAB);B(PENDINGLOGTAB);C(SETTLEMENTLOGTAB)
	public String getPageType(String pcode){
		String type = "A";
		if(StrUtils.isNotEmpty(pcode)){
			int iPcode = Integer.valueOf(pcode);
			if(iPcode >= 5000){
				type = "C";
			}else if(iPcode >= 1400 && iPcode != 1405){
				type = "B";
			}
		}
		return type;
	}
	
	public String combine(List<String> conditions){
		String conStr = "";
		for(int i = 0 ; i < conditions.size(); i++){
			conStr += conditions.get(i);
			if(i < conditions.size() - 1){
				conStr += " AND ";
			}
		}
		return conStr;
	}

	public OPC_TRANS_Dao getOpc_trans_Dao() {
		return opc_trans_Dao;
	}

	public void setOpc_trans_Dao(OPC_TRANS_Dao opc_trans_Dao) {
		this.opc_trans_Dao = opc_trans_Dao;
	}

	public EACH_USERLOG_BO getUserlog_bo() {
		return userlog_bo;
	}
	public void setUserlog_bo(EACH_USERLOG_BO userlog_bo) {
		this.userlog_bo = userlog_bo;
	}	
}
