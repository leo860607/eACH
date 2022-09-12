package tw.org.twntch.bo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts.util.LabelValueBean;

import tw.org.twntch.action.GenericAction;
import tw.org.twntch.db.dao.hibernate.BANK_GROUP_Dao;
import tw.org.twntch.db.dao.hibernate.ONBLOCKTAB_Dao;
import tw.org.twntch.db.dao.hibernate.Page;
import tw.org.twntch.db.dao.hibernate.RPONBLOCKTAB_Dao;
import tw.org.twntch.po.BANK_GROUP;
import tw.org.twntch.util.CodeUtils;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.RptUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.zDateHandler;

public class PROXY_CL_BO extends GenericAction{
	private ONBLOCKTAB_Dao onblocktab_Dao;
	private BANK_GROUP_Dao bank_group_Dao;
	private BANK_GROUP_BO bank_group_bo;
	private RPONBLOCKTAB_Dao rponblocktab_Dao;
	private CodeUtils codeUtils;
	private EACH_USERLOG_BO userlog_bo;
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	public String pageSearch(Map<String, String> param){
		int pageNo = StrUtils.isEmpty(param.get("page"))? 0 : Integer.valueOf(param.get("page"));
		int pageSize = StrUtils.isEmpty(param.get("rows"))? Integer.valueOf(Arguments.getStringArg("PAGE.SIZE")) : Integer.valueOf(param.get("rows"));
		String countQuerySql = "" ,sql = "" , result = "";
		String[] cols = "".split(",");
		List list = null;
		Map rtnMap = null;
		Map conditionMap = null;
		Page page = null;
		try {
			String conditionColsKey = "BIZDATE,CLEARINGPHASE,SENDERACQUIRE";
//			conditionMap = getPathSQL(param, Arrays.asList(conditionColsKey.split(",")));
			//conditionMap = getConditionData(param, Arrays.asList(conditionColsKey.split(",")));
			conditionMap = getConditionData2(param, Arrays.asList(conditionColsKey.split(",")));
			//System.out.println("123>>"+conditionMap.get("sqlPath").toString());
			//countQuerySql = getCountSQL(conditionMap.get("sqlPath").toString());
			countQuerySql = getCountSQL2((String)conditionMap.get("sql1"), (String)conditionMap.get("sql2"));
			//sql = getSQL(conditionMap.get("sqlPath").toString() , false, param);
			sql = getSQL2((String)conditionMap.get("sql1"), (String)conditionMap.get("sql2"), param);
			
			//先算出總計的欄位值，放到rtnMap裡面，到頁面再放到下面的總計Grid裡面
			String dataSumCols[] = {"RECVCNT","RECVAMT","RVSRECVCNT","RVSRECVAMT","IN_TOL","PAYCNT","PAYAMT","RVSPAYCNT","RVSPAYAMT","OUT_TOL","DIF_TOL"};
			StringBuffer dataSumSQL = new StringBuffer();
			dataSumSQL.append("SELECT SUM(RECVCNT) AS RECVCNT,SUM(RECVAMT) AS RECVAMT,SUM(RVSRECVCNT) AS RVSRECVCNT,SUM(RVSRECVAMT) AS RVSRECVAMT,SUM(IN_TOL) AS IN_TOL,SUM(PAYCNT) AS PAYCNT,SUM(PAYAMT) AS PAYAMT,SUM(RVSPAYCNT) AS RVSPAYCNT,SUM(RVSPAYAMT) AS RVSPAYAMT,SUM(OUT_TOL) AS OUT_TOL,SUM(DIF_TOL) AS DIF_TOL ");
			dataSumSQL.append("FROM (" + sql + ")");
			List dataSumList = onblocktab_Dao.dataSumII(dataSumSQL.toString(),dataSumCols,(List)conditionMap.get("values"));
			rtnMap = new HashMap();
			rtnMap.put("dataSumList",dataSumList);
			
//			page = onblocktab_Dao.getData(pageNo, pageSize, countQuerySql, sql, cols, FAIL_TRANS.class);
//			TODO SQL 分頁SQL語法會出現錯誤 待處理
			page = onblocktab_Dao.getDataII(pageNo, pageSize, countQuerySql, sql, cols, (List<String>) conditionMap.get("values"));
			list = (List) page.getResult();
			rtnMap.put("total", page.getTotalPageCount());
			rtnMap.put("page", String.valueOf(page.getCurrentPageNo()));
			rtnMap.put("records", page.getTotalCount());
			rtnMap.put("rows", list);
			//20151005 操作紀錄 清算階段=all
			if(param.get("CLEARINGPHASE").equals("")){
				String str= param.get("serchStrs").replace("\"CLEARINGPHASE\":\"\"", "\"CLEARINGPHASE\":\"all\"");
				param.put("serchStrs", str);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rtnMap.put("total", "0");
			rtnMap.put("page", "0");
			rtnMap.put("records", "0");
			rtnMap.put("rows", new ArrayList());
		}
		result = JSONUtils.map2json(rtnMap) ;
		logger.debug("FAIL_TRANS_BO>>"+result);
		return result;
	}


	public Map getConditionData(Map<String, String> param , List<String> list) throws Exception{
		StringBuffer sql = new StringBuffer();
		List<String> conditions = new LinkedList<String>();
		List values = new LinkedList();
		Map retmap = new HashMap();
		try{
			for(String key : list){
				if(StrUtils.isNotEmpty(param.get(key))){
					if(key.equals("BIZDATE")){
						conditions.add(" A.BIZDATE = ? ");
						values.add(DateTimeUtils.convertDate(param.get(key), "yyyymmdd", "yyyymmdd"));
					}else if(key.equals("SENDERACQUIRE")){
						conditions.add(" B.CTBK_ID = ? ");
						values.add(param.get(key));
					}else{
						conditions.add(" A."+key+" = ? ");
						values.add(param.get(key));
					}
				}
			}
			for(int i = 0 ; i < conditions.size(); i++){
				if(i == 0){
					sql.append(" WHERE ");
				}
				sql.append(conditions.get(i));
				if(i < conditions.size() - 1){
					sql.append(" AND ");
				}
			}
			sql.append(" ");
			retmap.put("sqlPath", sql.toString());
			retmap.put("values", values);
			logger.debug("sql>>"+sql.toString());
			logger.debug("getConditionDataSQL_Path>>"+retmap);	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e.toString()) ; 
		}
		return retmap;
	}
	
	public Map getConditionData2(Map<String, String> param , List<String> list) throws Exception{
		Map retmap = new HashMap();
		
		List<String> con1 = new LinkedList<String>();
		List<String> con2 = new LinkedList<String>();
		List values = new LinkedList();
		
		String sql1 = "", sql2 = "";
		try{
			for(String key : list){
				if(StrUtils.isNotEmpty(param.get(key))){
					if(key.equals("BIZDATE")){
						con1.add(" T.BIZDATE = ? ");
						values.add(DateTimeUtils.convertDate(param.get(key), "yyyymmdd", "yyyymmdd"));
					}else if(key.equals("SENDERACQUIRE")){
						con2.add(" A.CTBK_ID = ? ");
						values.add(param.get(key));
					}else{
						con1.add(" T."+key+" = ? ");
						values.add(param.get(key));
					}
				}
			}
			sql1 = combine(con1);
			sql2 = combine(con2);
			retmap.put("sql1", sql1);
			retmap.put("sql2", sql2);
			retmap.put("values", values);
			logger.debug("getConditionDataSQL_Path>>"+retmap);	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e.toString()) ; 
		}
		return retmap;
	}
	
	
//	public Map getPathSQL(Map<String, String> param , List<String> list){
//		StringBuffer sql = new StringBuffer();
//		List values = new LinkedList();
//		Map map = new HashMap();
//		for(String key :list){
//			param.get(key);
//			if(StrUtils.isNotEmpty(param.get(key))){
//				if(list.indexOf(key) == 0){ sql.append(" WHERE "); }
//				if(list.indexOf(key) != 0){ sql.append(" AND "); }
//				if(key.equals("BIZDATE")){
//					sql.append("a."+key +" = ? ");
//					values.add(DateTimeUtils.convertDate(param.get(key), "yyyymmdd", "yyyymmdd"));
//				}else{
//					sql.append("a."+key+" = ?");
//					values.add(param.get(key));
//				}
//			}
//		}
//		sql.append(" ");
//		map.put("sqlPath", sql.toString());
//		map.put("values", values);
//		System.out.println("getPathSQL.map>>"+map);
//		return map;
//	}
	
	public String getSQL(String sqlPath ,boolean isPageSQL, Map<String, String> params){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT "); 
		sql.append("VARCHAR(B.CTBK_ID) AS CTBK_ID, ");
		sql.append("VARCHAR(A.BGBK_ID) AS BGBK_ID, ");
		sql.append("COALESCE((SELECT BGBK_NAME FROM BANK_GROUP WHERE BGBK_ID = A.BGBK_ID),'') AS BGBK_NAME, ");
		sql.append("VARCHAR(A.CLEARINGPHASE) AS CLEARINGPHASE, ");
//	    20150430 edit by hugo req by 李建利 此張報表所有應付金額都要正向表示 差額才有負號
//		sql.append("SUM(A.RECVCNT) RECVCNT, SUM(A.RECVAMT) RECVAMT, SUM(A.RVSRECVCNT) RVSRECVCNT, SUM(A.RVSRECVAMT) RVSRECVAMT, (SUM(A.RECVAMT+A.RVSRECVAMT)) AS IN_TOL, ");
//		sql.append("SUM(A.PAYCNT) PAYCNT, SUM(A.PAYAMT) PAYAMT,SUM(A.RVSPAYCNT) RVSPAYCNT,SUM(A.RVSPAYAMT) RVSPAYAMT, (SUM(A.PAYAMT+A.RVSPAYAMT)) AS OUT_TOL, ");
		sql.append("SUM(A.RECVCNT) RECVCNT, SUM(A.RECVAMT) RECVAMT, SUM(A.RVSRECVCNT) RVSRECVCNT, SUM(A.RVSRECVAMT) RVSRECVAMT, (SUM(A.RECVAMT+A.RVSRECVAMT)) AS IN_TOL, ");
		sql.append("SUM(A.PAYCNT) PAYCNT, abs(SUM(A.PAYAMT)) PAYAMT,SUM(A.RVSPAYCNT) RVSPAYCNT, abs(SUM(A.RVSPAYAMT)) RVSPAYAMT, abs((SUM(A.PAYAMT+A.RVSPAYAMT))) AS OUT_TOL, ");
		sql.append("SUM((A.RECVAMT+A.RVSRECVAMT)+(A.PAYAMT+A.RVSPAYAMT)) DIF_TOL ");
		sql.append("FROM RPONCLEARINGTAB A LEFT JOIN (SELECT BGBK_ID, CTBK_ID FROM BANK_GROUP WHERE BGBK_ID <> CTBK_ID AND CTBK_ID <> '') B ON A.BGBK_ID = B.BGBK_ID ");
		sql.append(sqlPath);
		sql.append("GROUP BY B.CTBK_ID, A.BGBK_ID, A.CLEARINGPHASE ");
		sql.append(getOrderStmnt(params.get("sidx"), params.get("sord")));
		/*
		sql.append(" SELECT coalesce( b.CTBK_ID ,'')  CTBK_ID , coalesce((SELECT BGBK_NAME  FROM BANK_GROUP  WHERE BGBK_ID = b.CTBK_ID )  ,'') CTBGBK_NAME ");
		sql.append(" , coalesce((SELECT CTBK_ACCT  FROM BANK_GROUP  WHERE BGBK_ID = b.CTBK_ID )  ,'') CTBK_ACCT ,VARCHAR(a.CLEARINGPHASE) CLEARINGPHASE   ");
		sql.append(", VARCHAR(a.BGBK_ID) BGBK_ID , coalesce(b.bgbk_name,'')  BGBK_NAME ");
		sql.append(" ,SUM(a.RECVCNT) RECVCNT , SUM(a.RECVAMT) RECVAMT ,SUM(a.PAYCNT) PAYCNT , SUM(a.PAYAMT ) PAYAMT ,SUM(a.RVSRECVCNT) RVSRECVCNT , SUM(a.RVSRECVAMT) RVSRECVAMT ,SUM(a.RVSPAYCNT) RVSPAYCNT ,SUM(a.RVSPAYAMT) RVSPAYAMT ");
		sql.append(" ,SUM(a.RECVAMT+a.RVSRECVAMT) in_tol, SUM(a.PAYAMT+a.RVSPAYAMT) out_tol , SUM((a.RECVAMT+a.RVSRECVAMT)+(a.PAYAMT+a.RVSPAYAMT)) dif_tol  ");
		sql.append(" FROM  RPONCLEARINGTAB a");
		sql.append(" LEFT JOIN BANK_GROUP b ON a.BGBK_ID = b.BGBK_ID ");
		sql.append(sqlPath);
		sql.append(" GROUP BY b.CTBK_ID ,a.BGBK_ID ,CLEARINGPHASE ,BGBK_NAME ");
		sql.append(" ORDER BY CLEARINGPHASE ,  b.CTBK_ID  ,a.BGBK_ID");
		System.out.println("getSQL.sql>>"+sql);
		System.out.println("getSQL.isPage>>"+isPageSQL);
		*/
		return sql.toString();
	}
	
	public String getSQL2(String con1, String con2, Map<String, String> params){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ");
		sql.append("VARCHAR(A.CTBK_ID) AS CTBK_ID, ");
//		sql.append("VARCHAR(A.BGBK_ID) AS BGBK_ID, ");
		sql.append("VARCHAR(A.BANKID) AS BGBK_ID, ");
//		sql.append("COALESCE((SELECT BGBK_NAME FROM BANK_GROUP WHERE BGBK_ID = A.BGBK_ID),'') AS BGBK_NAME, ");
		sql.append("COALESCE((SELECT BGBK_NAME FROM BANK_GROUP WHERE BGBK_ID = A.BANKID),'') AS BGBK_NAME, ");
		sql.append("VARCHAR(A.CLEARINGPHASE) AS CLEARINGPHASE, ");
		sql.append("SUM(A.RECVCNT) RECVCNT, SUM(A.RECVAMT) RECVAMT, SUM(A.RVSRECVCNT) RVSRECVCNT, SUM(A.RVSRECVAMT) RVSRECVAMT, (SUM(A.RECVAMT+A.RVSRECVAMT)) AS IN_TOL, ");
		sql.append("SUM(A.PAYCNT) PAYCNT, abs(SUM(A.PAYAMT)) PAYAMT,SUM(A.RVSPAYCNT) RVSPAYCNT, abs(SUM(A.RVSPAYAMT)) RVSPAYAMT, abs((SUM(A.PAYAMT+A.RVSPAYAMT))) AS OUT_TOL, ");
		sql.append("SUM((A.RECVAMT+A.RVSRECVAMT)-(A.PAYAMT+A.RVSPAYAMT)) DIF_TOL ");
		sql.append("FROM ( ");
//		sql.append("    SELECT T.*, COALESCE(GET_CUR_CTBKID(T.BGBK_ID, TRANS_DATE(T.BIZDATE,'T',''), 0), '') AS CTBK_ID ");
		sql.append("    SELECT T.*, COALESCE(GET_CUR_CTBKID(T.BANKID, TRANS_DATE(T.BIZDATE,'T',''), 0), '') AS CTBK_ID ");
//		sql.append("    FROM EACHUSER.RPONCLEARINGTAB AS T ");
		sql.append("    FROM EACHUSER.ONCLEARINGTAB AS T ");
		sql.append("    " + (StrUtils.isNotEmpty(con1)?"WHERE " + con1 : ""));
		sql.append(") A  ");
//		sql.append("WHERE A.CTBK_ID <> '' AND A.BGBK_ID <> A.CTBK_ID " + (StrUtils.isNotEmpty(con2)?"AND " + con2 : ""));
//		sql.append("GROUP BY A.CTBK_ID, A.BGBK_ID, A.CLEARINGPHASE ");
		sql.append("WHERE A.CTBK_ID <> '' AND A.BANKID <> A.CTBK_ID " + (StrUtils.isNotEmpty(con2)?"AND " + con2 : ""));
		sql.append("GROUP BY A.CTBK_ID, A.BANKID, A.CLEARINGPHASE ");
		sql.append(getOrderStmnt(params.get("sidx"), params.get("sord")));
		logger.debug("getSQL2 >> " + sql.toString());
		return sql.toString();
	}
	
	public String getOrderStmnt(String sidx, String sord){
		StringBuffer sql = new StringBuffer();
		if(StrUtils.isEmpty(sidx)){
//			sql.append("ORDER BY A.CTBK_ID, A.BGBK_ID, A.CLEARINGPHASE ");
			sql.append("ORDER BY A.CTBK_ID, A.BANKID, A.CLEARINGPHASE ");
		}else{
			if("BGBK_NAME".equals(sidx)){
//				sql.append("ORDER BY COALESCE((SELECT BGBK_NAME FROM BANK_GROUP WHERE BGBK_ID = A.BGBK_ID),'') " + sord);
				sql.append("ORDER BY COALESCE((SELECT BGBK_NAME FROM BANK_GROUP WHERE BGBK_ID = A.BANKID),'') " + sord);
			}else if("RECVCNT".equals(sidx)){
				sql.append("ORDER BY SUM(A.RECVCNT) " + sord);
			}else if("RECVAMT".equals(sidx)){
				sql.append("ORDER BY SUM(A.RECVAMT) " + sord);
			}else if("RVSRECVCNT".equals(sidx)){
				sql.append("ORDER BY SUM(A.RVSRECVCNT) " + sord);
			}else if("RVSRECVAMT".equals(sidx)){
				sql.append("ORDER BY SUM(A.RVSRECVAMT) " + sord);
			}else if("IN_TOL".equals(sidx)){
				sql.append("ORDER BY (SUM(A.RECVAMT+A.RVSRECVAMT)) " + sord);
			}else if("PAYCNT".equals(sidx)){
				sql.append("ORDER BY SUM(A.PAYCNT) " + sord);
			}else if("PAYAMT".equals(sidx)){
				sql.append("ORDER BY SUM(A.PAYAMT) " + sord);
			}else if("RVSPAYCNT".equals(sidx)){
				sql.append("ORDER BY SUM(A.RVSPAYCNT) " + sord);
			}else if("RVSPAYAMT".equals(sidx)){
				sql.append("ORDER BY SUM(A.RVSPAYAMT) " + sord);
			}else if("OUT_TOL".equals(sidx)){
				sql.append("ORDER BY (SUM(A.PAYAMT+A.RVSPAYAMT)) " + sord);
			}else if("DIF_TOL".equals(sidx)){
				sql.append("ORDER BY SUM((A.RECVAMT+A.RVSRECVAMT)+(A.PAYAMT+A.RVSPAYAMT)) " + sord);
			}else{
				sql.append("ORDER BY " + sidx + " " + sord);
			}
		}
		return sql.toString();
	}
	
	public String getCountSQL(String sqlPath ){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT B.CTBK_ID, A.BGBK_ID, A.CLEARINGPHASE FROM RPONCLEARINGTAB A LEFT JOIN (SELECT BGBK_ID, CTBK_ID FROM BANK_GROUP WHERE BGBK_ID <> CTBK_ID AND CTBK_ID <> '') B ON A.BGBK_ID = B.BGBK_ID ");
		sql.append(sqlPath);
		sql.append("GROUP BY B.CTBK_ID, A.BGBK_ID, A.CLEARINGPHASE ");
		sql.append("ORDER BY B.CTBK_ID, A.BGBK_ID, A.CLEARINGPHASE ");
		/*
		sql.append(" SELECT COUNT(*)  ");
		sql.append(" FROM  RPONCLEARINGTAB a");
		sql.append(" LEFT JOIN BANK_GROUP b ON a.BGBK_ID = b.BGBK_ID ");
		sql.append(sqlPath);
		sql.append(" GROUP BY b.CTBK_ID ,a.BGBK_ID ,CLEARINGPHASE ,b.BGBK_NAME ");
		sql.append(" ORDER BY CLEARINGPHASE ,  b.CTBK_ID ");
		*/
		logger.debug("getCountSQL.sql>>"+sql);
		return sql.toString();
	}
	
	public String getCountSQL2(String con1, String con2){
		StringBuffer sql = new StringBuffer();
//		sql.append("SELECT A.CTBK_ID, A.BGBK_ID, A.CLEARINGPHASE ");
		sql.append("SELECT A.CTBK_ID, A.BANKID, A.CLEARINGPHASE ");
		sql.append("FROM ( ");
		sql.append("	SELECT T.*, COALESCE(GET_CUR_CTBKID(T.BANKID, TRANS_DATE(T.BIZDATE,'T',''), 0), '') AS CTBK_ID ");
//		sql.append("	FROM EACHUSER.RPONCLEARINGTAB AS T ");
		sql.append("	FROM EACHUSER.ONCLEARINGTAB AS T ");
		sql.append("	" + (StrUtils.isNotEmpty(con1)?"WHERE " + con1 : ""));
		sql.append(") A ");
//		sql.append("WHERE A.CTBK_ID <> '' AND A.BGBK_ID <> A.CTBK_ID " + (StrUtils.isNotEmpty(con2)?"AND " + con2 : ""));
//		sql.append("GROUP BY A.CTBK_ID, A.BGBK_ID, A.CLEARINGPHASE ");
		sql.append("WHERE A.CTBK_ID <> '' AND A.BANKID <> A.CTBK_ID " + (StrUtils.isNotEmpty(con2)?"AND " + con2 : ""));
		sql.append("GROUP BY A.CTBK_ID, A.BANKID, A.CLEARINGPHASE ");
		logger.debug("getCountSQL2 >> "+sql);
		return sql.toString();
	}
	
	/**
	 * 舊版 先註解
	 * @param sqlPath
	 * @param isPageSQL
	 * @return
	 */
//	public String getSQL(String sqlPath ,boolean isPageSQL){
//		StringBuffer sql = new StringBuffer();
//		sql.append(" WITH TEMP AS ( SELECT  a.CLEARINGPHASE  , (case when a.ACCTCODE='0' then '0' else '1' end) ACCTCODE ");
//		sql.append("  , a.INCLEARING INCLEARING  ,a.OUTCLEARING OUTCLEARING ,a.TXAMT ");
//		sql.append("  FROM RPONBLOCKTAB  A  ");
//		sql.append(sqlPath);
//		sql = isPageSQL ? sql.append(" ) as t) ") : sql.append(" ) ");
//		sql = isPageSQL ? sql.append(" select * from ( select rownumber() over() as rownumber_, ") : sql.append(" SELECT ");
////		sql.append(" SELECT ");
//		sql.append(" a.bgbk_Id  bgbk_Id , coalesce((select bgbk_name from bank_group where bgbk_id=a.bgbk_Id) ,'') bgbk_name ");
//		sql.append(" , (case when a.CLEARINGPHASE='01' then '第一階段' else '第二階段' end)  CLEARINGPHASE ");
////		--入帳筆數
//		sql.append(" ,(SELECT count(INCLEARING)  from TEMP where  INCLEARING=a.bgbk_Id and CLEARINGPHASE=a.CLEARINGPHASE ) InCount ");
//		sql.append("  ,(SELECT Sum(TXAMT)  from TEMP where  INCLEARING=a.bgbk_Id and CLEARINGPHASE=a.CLEARINGPHASE ) INAmt ");
////		扣款筆數
//		sql.append("  ,(SELECT count(OUTCLEARING)  from TEMP where  OUTCLEARING=a.bgbk_Id and CLEARINGPHASE=a.CLEARINGPHASE  ) OutCount ");
//		sql.append("  ,(SELECT Sum(TXAMT)  from TEMP where  OUTCLEARING=a.bgbk_Id and CLEARINGPHASE=a.CLEARINGPHASE ) OutAmt ");
//		sql.append("  FROM (select distinct INCLEARING bgbk_Id , CLEARINGPHASE from Temp )a");
////		sql.append("  FROM (select distinct INCLEARING bgbk_Id , CLEARINGPHASE from Temp ");
////		sql.append("  union select distinct OUTCLEARING bgbk_Id , CLEARINGPHASE from Temp)  a ");
//		System.out.println("getSQL.sql>>"+sql);
//		System.out.println("getSQL.isPage>>"+isPageSQL);
//		return sql.toString();
//	}
//	
	/**
	 * 取得代理清算行清單
	 * @return
	 */
	public List getProxyClean_BankList(){
		List<LabelValueBean> beanList = new LinkedList<LabelValueBean>();
		beanList = bank_group_bo.getAll_Proxy_CtbkList();
		logger.debug("beanList>>"+beanList);
		return beanList;
//		List<BANK_GROUP> list = bank_group_Dao.getProxyClean_BankList();
//		List<LabelValueBean> beanList = new LinkedList<LabelValueBean>();
//		LabelValueBean bean = null;
//		for(BANK_GROUP po : list){
//			bean = new LabelValueBean(po.getCTBK_ID() + " - " + po.getCTBK_NAME(), po.getCTBK_ID());
//			beanList.add(bean);
//		}
//		logger.debug("beanList>>"+beanList);
//		return beanList;
	}
	
	
	
	
	
	public Map<String, String> export(String bzdate, String clearingPhase,String CTBK_ID ,String serchStrs){
		Map<String, String> rtnMap = null;
		String outputFilePath ="";
		try{
			rtnMap = new HashMap<String, String>();
			rtnMap.put("serchStrs", serchStrs);//e
			Map<String, Object> params = new HashMap<String, Object>();
//			顯示區塊
			params.put("V_TXDT",DateTimeUtils.convertDate(DateTimeUtils.convertDate(bzdate,"yyyyMMdd","yyyyMMdd"),"yyyyMMdd","營業日期: yyy/MM/dd"));
			params.put("V_CLEARINGPHASE",clearingPhase);
			params.put("V_PRINT_DATE",zDateHandler.getODDate());
			params.put("V_PRINT_TIME",zDateHandler.getTheTime());
			logger.debug("params >> " + params);
			//Map map = this.getrRPTConditionData(bzdate, clearingPhase, CTBK_ID);
			Map map = this.getrRPTConditionData2(bzdate, clearingPhase, CTBK_ID);
//			List<Map> list = rponblocktab_Dao.getCl_3_Detail_Data_ForRpt(map.get("sqlPath").toString());
			
			//String sql = getRPTSQL(map.get("sqlPath").toString());
			String sql = getRPTSQL2(map.get("sql1").toString(), map.get("sql2").toString());
			
			List<Map> list = rponblocktab_Dao.getRptData(sql, (List<String>) map.get("values"));
			int pathType = Arguments.getStringArg("ISWINOPEN").equals("Y")?RptUtils.C_PATH :RptUtils.R_PATH;
			outputFilePath = RptUtils.export(RptUtils.COLLECTION,pathType, "cl_4", "cl_4", params, list);
			//outputFilePath = "";
			if(StrUtils.isNotEmpty(outputFilePath)){
				rtnMap.put("result", "TRUE");
				rtnMap.put("msg", outputFilePath);
			}else{
				rtnMap.put("result", "FALSE");
				rtnMap.put("msg", "產生失敗!");
			}
			
			//20151005 操作紀錄 清算階段=all
			if(clearingPhase.equals("")){
				String str= serchStrs.replace("\"CLEARINGPHASE\":\"\"", "\"CLEARINGPHASE\":\"all\"");
				rtnMap.put("serchStrs", str);
			}
		}catch(Exception e){
			e.printStackTrace();
			rtnMap.put("result", "FALSE");
			rtnMap.put("msg", "產生錯誤!");
		}
		
		return rtnMap;
	}
	
	public Map<String,Object> exportTXT(String bzdate, String clearingPhase ,String CTBK_ID,String serchStrs){
		Map<String,Object> rtnMap = null;
		Map<String,Object> pkMap = null;
		//清算日期
		String bizDate = "";
		//總差額註記
		String TOTAL_DIF_MARK = "0";
		//總差額(右靠左補零)
		String ABS_TOTAL_DIF_AMOUNT = "0";
		//空白(49)
		String blank = "                                                 ";
//		20150519 edit by hugo req by UAT-201505011-01 不需要首錄
		//TXT的控制首錄
//		String firstRow = "BOFclbkbal"+bizDate;
		String firstRow = null;
		//TXT的控制尾錄
		String lastRow = "";
		//產生TXT的BYTE[]
		byte[] byteTXT = null;
//		是否有一般查詢資料
		boolean hasData = false;
		Map<String,Object> columnMap = new HashMap<String,Object>();
		try{
			rtnMap = new HashMap<String,Object>();
			pkMap = new HashMap<String,Object>();
			rtnMap.put("serchStrs", serchStrs);//e
			pkMap.put("serchStrs", serchStrs);//e
			//組查詢條件
			List<String> con1 = new ArrayList<String>();
			List<String> con2 = new ArrayList<String>();
			if(StrUtils.isNotEmpty(bzdate)){
				con1.add("T.BIZDATE = '" + DateTimeUtils.convertDate(bzdate,"yyyyMMdd","yyyyMMdd") + "'");
			}
			if(StrUtils.isNotEmpty(clearingPhase)){
				con1.add("T.CLEARINGPHASE = '" + clearingPhase + "'");
			}
			if(StrUtils.isNotEmpty(CTBK_ID)){
				con2.add("A.CTBK_ID = '" + CTBK_ID + "'");
			}
			String sql1 = "", sql2 = "";
			sql1 = combine(con1);
			sql2 = combine(con2);
			//取得sql
			//String sql = getTXTSQL(condition);
			String sql = getTXTSQL2(sql1, sql2);
			//查詢資料
			List<Map<String,Object>> list = codeUtils.queryListMap(sql,null);
			hasData = list != null && list.size() > 0 ? true:false;
			columnMap = getColumnMap();
			//計算尾錄的總差額和總註記的SQL語法
			//String sumSQL = getSumSQL(condition);
			String sumSQL = getSumSQL2(sql1, sql2);
			logger.debug("sumSQL="+sumSQL);
			//查詢正常而且有資料
			//查詢尾錄的總差額和總註記
			List<Map<String,Object>> queryListMap = codeUtils.queryListMap(sumSQL,null);
			Map<String,Object> map = queryListMap != null && queryListMap.size() > 0 ? queryListMap.get(0) :new HashMap<String,Object>();
			//查詢尾錄的總差額和總註記正常而且有資料
			//清算日期
			bizDate =  hasData ?(String)list.get(0).get("BIZDATE") : bzdate;
			//總差額註記
			TOTAL_DIF_MARK =  map.get("TOTAL_DIF_MARK") !=null ? (String)map.get("TOTAL_DIF_MARK") :TOTAL_DIF_MARK;
			//總差額(右靠左補零)
			ABS_TOTAL_DIF_AMOUNT = map.get("ABS_TOTAL_DIF_AMOUNT") !=null ? String.valueOf((long)queryListMap.get(0).get("ABS_TOTAL_DIF_AMOUNT")) :ABS_TOTAL_DIF_AMOUNT;
			ABS_TOTAL_DIF_AMOUNT = codeUtils.padText("number",14,ABS_TOTAL_DIF_AMOUNT) ;
			//空白(49)
			blank = "                                                 ";
			logger.debug("bizDate="+bizDate);
			logger.debug("TOTAL_DIF_MARK="+TOTAL_DIF_MARK);
			logger.debug("ABS_TOTAL_DIF_AMOUNT="+ABS_TOTAL_DIF_AMOUNT);
			//TXT的控制尾錄
			lastRow = "EOF"+bizDate+TOTAL_DIF_MARK+ABS_TOTAL_DIF_AMOUNT+blank;
			//產生TXT的BYTE[]
			byteTXT = codeUtils.createTXT((List)list,columnMap,firstRow,lastRow);
			//正常
			if(byteTXT != null){
				rtnMap.put("result","TRUE");
				rtnMap.put("msg",byteTXT);
			}
			//因為list不為null且有資料才會進來，所以一定是產生TXT檔過程出現問題，byteTXT才會為null
			else{
				rtnMap.put("result", "FALSE");
				rtnMap.put("msg","產生TXT檔過程出現問題!");
			}
			userlog_bo.writeLog("E", null, null, pkMap);
		}catch(Exception e){
			e.printStackTrace();
			rtnMap.put("result", "FALSE");
			rtnMap.put("msg", "產生錯誤!");
			userlog_bo.writeFailLog("E", null, null, null, pkMap);
		}
		logger.debug("rtnMap>>"+rtnMap);
		return rtnMap;
	}
	
	
	public Map<String,Object> getColumnMap(){
		Map<String,Object> columnMap = new HashMap<String,Object>();
		List<String> columnNameList = new ArrayList<String>();
		List<Integer> columnLengthList = new ArrayList<Integer>();
		List<String> columnTypeList = new ArrayList<String>();
		
		columnNameList.add("BIZDATE");
		columnLengthList.add(8);
		columnTypeList.add("string");
		columnNameList.add("CLEARINGPHASE");
		columnLengthList.add(1);
		columnTypeList.add("string");
		columnNameList.add("CLTYPE");
		columnLengthList.add(1);
		columnTypeList.add("string");
		columnNameList.add("BGBK_ID");
		columnLengthList.add(7);
		columnTypeList.add("string");
		columnNameList.add("TCH_ID");
		columnLengthList.add(2);
		columnTypeList.add("string");
		columnNameList.add("BGBK_ATTR");
		columnLengthList.add(1);
		columnTypeList.add("string");
		columnNameList.add("OUTCNT");
		columnLengthList.add(6);
		columnTypeList.add("number");
		columnNameList.add("OUT_TOL");
		columnLengthList.add(14);
		columnTypeList.add("number");
		columnNameList.add("INCNT");
		columnLengthList.add(6);
		columnTypeList.add("number");
		columnNameList.add("IN_TOL");
		columnLengthList.add(14);
		columnTypeList.add("number");
		columnNameList.add("DIF_MARK");
		columnLengthList.add(1);
		columnTypeList.add("string");
		columnNameList.add("DIF_TOL");
		columnLengthList.add(14);
		columnTypeList.add("number");
		columnMap.put("columnName",columnNameList);
		columnMap.put("columnLength",columnLengthList);
		columnMap.put("columnType",columnTypeList);
		return columnMap;
	}
	
	public String getSumSQL(String condition){
		String sumSQL = "SELECT ";
		sumSQL += " case when SUM(dif_tol) < 0 THEN '-' ELSE '+' END AS TOTAL_DIF_MARK,";
		sumSQL += " ABS(SUM(dif_tol)) AS ABS_TOTAL_DIF_AMOUNT";
		sumSQL += " FROM (";
		sumSQL += " SELECT ";
		sumSQL += " SUM((a.RECVAMT+a.RVSRECVAMT)+(a.PAYAMT+a.RVSPAYAMT)) dif_tol";
		sumSQL += " FROM ";
		sumSQL += " EACHUSER.RPONCLEARINGTAB a ";
		sumSQL += " RIGHT JOIN BANK_GROUP b ON a.BGBK_ID = b.BGBK_ID and b.BGBK_ID != b.CTBK_ID ";
		sumSQL += StrUtils.isEmpty(condition)?"":condition;
		sumSQL += " GROUP BY b.CTBK_ID , a.BGBK_ID  ,CLEARINGPHASE  ,b.TCH_ID,b.BGBK_ATTR ,a.BIZDATE) T";
		return sumSQL ;
	}
	
	public String getSumSQL2(String con1, String con2){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ");
		sql.append("CASE WHEN SUM(DIF_TOL) < 0 THEN '-' ELSE '+' END AS TOTAL_DIF_MARK, ");
		sql.append("ABS(SUM(DIF_TOL)) AS ABS_TOTAL_DIF_AMOUNT ");
		sql.append("FROM ( ");
		sql.append("	SELECT ");
		sql.append("	SUM((A.RECVAMT+A.RVSRECVAMT)+(A.PAYAMT+A.RVSPAYAMT)) DIF_TOL ");
		sql.append("	FROM ( ");
		sql.append("        SELECT T.*, GET_CUR_CTBKID(BGBK_ID, TRANS_DATE(BIZDATE,'T',''), 0) AS CTBK_ID ");
		sql.append("        FROM EACHUSER.RPONCLEARINGTAB AS T ");
		sql.append("        " + (StrUtils.isNotEmpty(con1)?"WHERE " + con1 : ""));
		sql.append("    ) A RIGHT JOIN MASTER_BANK_GROUP B ON A.BGBK_ID = B.BGBK_ID ");
		sql.append("    WHERE A.BGBK_ID <> A.CTBK_ID " + (StrUtils.isNotEmpty(con2)?"AND " + con2 : ""));
		sql.append("	GROUP BY A.CTBK_ID, A.BGBK_ID, A.CLEARINGPHASE, B.TCH_ID, B.BGBK_ATTR, A.BIZDATE ");
		sql.append(") T ");
		return sql.toString();
	}
	
//	原本kuso寫的 先暫時不用 因req改 無資料仍要產生檔案 但要有首尾錄
	public Map<String,Object> exportTXT_bykuso(String bzdate, String clearingPhase ,String CTBK_ID,String serchStrs){
		Map<String,Object> rtnMap = null;
		try{
			rtnMap = new HashMap<String,Object>();
			rtnMap.put("serchStrs", serchStrs);//e
			//組查詢條件
			List<String> conditions = new ArrayList<String>();
			if(StrUtils.isNotEmpty(bzdate)){
				conditions.add(" a.BIZDATE = '" + DateTimeUtils.convertDate(bzdate,"yyyyMMdd","yyyyMMdd") + "' ");
			}
			if(StrUtils.isNotEmpty(clearingPhase)){
				conditions.add(" a.CLEARINGPHASE = '" + clearingPhase + "' ");
			}
			if(StrUtils.isNotEmpty(CTBK_ID)){
				conditions.add(" b.CTBK_ID = '" + CTBK_ID + "' ");
			}
			String condition = "";
			for(int i = 0; i < conditions.size(); i++){
				if(i == 0){
					condition += " WHERE ";
				}
				condition += conditions.get(i);
				if(i < conditions.size() - 1){
					condition += " AND ";
				}
			}
			//取得sql
			String sql = getTXTSQL(condition);
			//查詢資料
			List<Map<String,Object>> list = codeUtils.queryListMap(sql,null);
			
			//查詢正常而且有資料
			if(list != null && list.size() > 0){
				Map<String,Object> columnMap = new HashMap<String,Object>();
				List<String> columnNameList = new ArrayList<String>();
				List<Integer> columnLengthList = new ArrayList<Integer>();
				List<String> columnTypeList = new ArrayList<String>();
				
				columnNameList.add("BIZDATE");
				columnLengthList.add(8);
				columnTypeList.add("string");
				columnNameList.add("CLEARINGPHASE");
				columnLengthList.add(1);
				columnTypeList.add("string");
				columnNameList.add("CLTYPE");
				columnLengthList.add(1);
				columnTypeList.add("string");
				columnNameList.add("BGBK_ID");
				columnLengthList.add(7);
				columnTypeList.add("string");
				columnNameList.add("TCH_ID");
				columnLengthList.add(2);
				columnTypeList.add("string");
				columnNameList.add("BGBK_ATTR");
				columnLengthList.add(1);
				columnTypeList.add("string");
				columnNameList.add("OUTCNT");
				columnLengthList.add(6);
				columnTypeList.add("number");
				columnNameList.add("OUT_TOL");
				columnLengthList.add(14);
				columnTypeList.add("number");
				columnNameList.add("INCNT");
				columnLengthList.add(6);
				columnTypeList.add("number");
				columnNameList.add("IN_TOL");
				columnLengthList.add(14);
				columnTypeList.add("number");
				columnNameList.add("DIF_MARK");
				columnLengthList.add(1);
				columnTypeList.add("string");
				columnNameList.add("DIF_TOL");
				columnLengthList.add(14);
				columnTypeList.add("number");
				columnMap.put("columnName",columnNameList);
				columnMap.put("columnLength",columnLengthList);
				columnMap.put("columnType",columnTypeList);
				
				//計算尾錄的總差額和總註記的SQL語法
				String sumSQL = "SELECT ";
				sumSQL += " case when SUM(dif_tol) < 0 THEN '-' ELSE '+' END AS TOTAL_DIF_MARK,";
				sumSQL += " ABS(SUM(dif_tol)) AS ABS_TOTAL_DIF_AMOUNT";
				sumSQL += " FROM (";
				sumSQL += " SELECT ";
				sumSQL += " SUM((a.RECVAMT+a.RVSRECVAMT)+(a.PAYAMT+a.RVSPAYAMT)) dif_tol";
				sumSQL += " FROM ";
				sumSQL += " EACHUSER.RPONCLEARINGTAB a ";
				sumSQL += " RIGHT JOIN BANK_GROUP b ON a.BGBK_ID = b.BGBK_ID and b.BGBK_ID != b.CTBK_ID ";
				sumSQL += StrUtils.isEmpty(condition)?"":condition;
				sumSQL += " GROUP BY b.CTBK_ID , a.BGBK_ID  ,CLEARINGPHASE  ,b.TCH_ID,b.BGBK_ATTR ,a.BIZDATE) T";
				
				logger.debug("sumSQL="+sumSQL);
				//查詢尾錄的總差額和總註記
				List<Map<String,Object>> queryListMap = codeUtils.queryListMap(sumSQL,null);
				
				//查詢尾錄的總差額和總註記正常而且有資料
				if(queryListMap != null && queryListMap.size() > 0){
					//清算日期
					String bizDate = (String)list.get(0).get("BIZDATE");
					//總差額註記
					String TOTAL_DIF_MARK = (String)queryListMap.get(0).get("TOTAL_DIF_MARK");
					//總差額(右靠左補零)
					String ABS_TOTAL_DIF_AMOUNT = codeUtils.padText("number",14,String.valueOf((long)queryListMap.get(0).get("ABS_TOTAL_DIF_AMOUNT")));
					//空白(49)
					String blank = "                                                 ";
					logger.debug("bizDate="+bizDate);
					logger.debug("TOTAL_DIF_MARK="+TOTAL_DIF_MARK);
					logger.debug("ABS_TOTAL_DIF_AMOUNT="+ABS_TOTAL_DIF_AMOUNT);
//					20150519 edit by hugo req by UAT-201505011-01 不需要首錄
					//TXT的控制首錄
//					String firstRow = "BOFclbkbal"+bizDate;
					String firstRow = null;
					//TXT的控制尾錄
					String lastRow = "EOF"+bizDate+TOTAL_DIF_MARK+ABS_TOTAL_DIF_AMOUNT+blank;
					//產生TXT的BYTE[]
					byte[] byteTXT = codeUtils.createTXT((List)list,columnMap,firstRow,lastRow);
					//正常
					if(byteTXT != null){
						rtnMap.put("result","TRUE");
						rtnMap.put("msg",byteTXT);
					}
					//因為list不為null且有資料才會進來，所以一定是產生TXT檔過程出現問題，byteTXT才會為null
					else{
						rtnMap.put("result", "FALSE");
						rtnMap.put("msg","產生TXT檔過程出現問題!");
					}
				}
				//查詢尾錄的總差額和總註記有問題或查無資料
				else{
					rtnMap.put("result", "FALSE");
					rtnMap.put("msg","查無資料或查詢過程出現問題!");
				}
			}
			//有問題或查無資料
			else{
				rtnMap.put("result", "FALSE");
				rtnMap.put("msg","查無資料或查詢過程出現問題!");
			}
		}catch(Exception e){
			e.printStackTrace();
			rtnMap.put("result", "FALSE");
			rtnMap.put("msg", "產生錯誤!");
		}
		
		return rtnMap;
	}
	
	public String getRPTSQL(String sqlPath){
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT coalesce( b.CTBK_ID ,'')  CTBK_ID , coalesce((SELECT BGBK_NAME  FROM BANK_GROUP  WHERE BGBK_ID = b.CTBK_ID )  ,'') CTBGBK_NAME "); 
	    sql.append(" , coalesce((SELECT CTBK_ACCT  FROM BANK_GROUP  WHERE BGBK_ID = b.CTBK_ID )  ,'') CTBK_ACCT ,VARCHAR(a.CLEARINGPHASE) CLEARINGPHASE ");
	    sql.append(" , VARCHAR(a.BGBK_ID) BGBK_ID , (select coalesce(bgbk_name,'') from bank_group where bgbk_id=a.BGBK_ID) BGBK_NAME ");
//	    sql.append(" ,SUM(a.RECVCNT) RECVCNT , SUM(a.RECVAMT) RECVAMT ,SUM(a.PAYCNT) PAYCNT , SUM(a.PAYAMT ) PAYAMT ,SUM(a.RVSRECVCNT) RVSRECVCNT , SUM(a.RVSRECVAMT) RVSRECVAMT ,SUM(a.RVSPAYCNT) RVSPAYCNT ,SUM(a.RVSPAYAMT) RVSPAYAMT ");
//	    sql.append(" ,SUM(a.RECVAMT+a.RVSRECVAMT) in_tol, SUM(a.PAYAMT+a.RVSPAYAMT) out_tol,SUM((a.RECVAMT+a.RVSRECVAMT)-(a.PAYAMT+a.RVSPAYAMT)) dif_tol ");
	    sql.append(" ,SUM(a.RECVCNT) RECVCNT , SUM(a.RECVAMT) RECVAMT ,SUM(a.PAYCNT) PAYCNT , abs(SUM(a.PAYAMT )) PAYAMT ,SUM(a.RVSRECVCNT) RVSRECVCNT , SUM(a.RVSRECVAMT) RVSRECVAMT ,SUM(a.RVSPAYCNT) RVSPAYCNT ,abs(SUM(a.RVSPAYAMT)) RVSPAYAMT ");
	    sql.append(" ,SUM(a.RECVAMT+a.RVSRECVAMT) in_tol, abs(SUM(a.PAYAMT+a.RVSPAYAMT)) out_tol,SUM((a.RECVAMT+a.RVSRECVAMT)+(a.PAYAMT+a.RVSPAYAMT)) dif_tol ");
	    sql.append(" FROM EACHUSER.RPONCLEARINGTAB a ");
	    sql.append(" RIGHT JOIN BANK_GROUP b ON a.BGBK_ID = b.BGBK_ID and b.BGBK_ID != b.CTBK_ID ");
	    sql.append(sqlPath);
	    sql.append(" GROUP BY b.CTBK_ID , a.BGBK_ID  ,CLEARINGPHASE ");
	    sql.append(" ORDER BY CLEARINGPHASE ,  b.CTBK_ID , a.BGBK_ID ");
	    logger.debug("getSQL>>"+sql);
		return sql.toString();
	}
	
	public String getRPTSQL2(String con1, String con2){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ");
		sql.append("COALESCE(A.CTBK_ID, '') AS CTBK_ID, ");
		sql.append("COALESCE((SELECT BGBK_NAME FROM BANK_GROUP WHERE BGBK_ID = A.CTBK_ID), '') AS CTBGBK_NAME, ");
		sql.append("COALESCE((SELECT CTBK_ACCT FROM BANK_GROUP WHERE BGBK_ID = A.CTBK_ID), '') AS CTBK_ACCT, ");
		sql.append("VARCHAR(A.CLEARINGPHASE) AS CLEARINGPHASE, ");
		sql.append("VARCHAR(A.BGBK_ID) AS BGBK_ID, ");
		sql.append("(SELECT COALESCE(BGBK_NAME, '') FROM BANK_GROUP WHERE BGBK_ID = A.BGBK_ID) AS BGBK_NAME, ");
		sql.append("SUM(A.RECVCNT) RECVCNT, SUM(A.RECVAMT) RECVAMT, SUM(A.PAYCNT) PAYCNT, ABS(SUM(A.PAYAMT)) PAYAMT, SUM(A.RVSRECVCNT) RVSRECVCNT, ");
		sql.append("SUM(A.RVSRECVAMT) RVSRECVAMT, SUM(A.RVSPAYCNT) RVSPAYCNT, ABS(SUM(A.RVSPAYAMT)) RVSPAYAMT, SUM(A.RECVAMT+A.RVSRECVAMT) IN_TOL, ");
		sql.append("ABS(SUM(A.PAYAMT+A.RVSPAYAMT)) OUT_TOL, SUM((A.RECVAMT+A.RVSRECVAMT)+(A.PAYAMT+A.RVSPAYAMT)) DIF_TOL ");
		sql.append("FROM ( ");
		sql.append("    SELECT T.*, (GET_CUR_CTBKID(BGBK_ID, TRANS_DATE(BIZDATE, 'T', ''), 0)) AS CTBK_ID ");
		sql.append("    FROM EACHUSER.RPONCLEARINGTAB AS T ");
		sql.append("    " + (StrUtils.isNotEmpty(con1)?"WHERE " + con1 : ""));
		sql.append(") AS A ");
		sql.append("WHERE A.BGBK_ID <> A.CTBK_ID ");
		sql.append((StrUtils.isNotEmpty(con2)?"AND " + con2 : ""));
		sql.append("GROUP BY A.CTBK_ID, A.BGBK_ID, CLEARINGPHASE ");
		sql.append("ORDER BY A.CLEARINGPHASE, A.CTBK_ID, A.BGBK_ID ");
		logger.debug("getRPTSQL2 >> " + sql.toString());
		return sql.toString();
	}
	
	public Map getrRPTConditionData(String txdt, String clearingPhase,String CTBK_ID) throws Exception{
		StringBuffer sql = new StringBuffer();
		Map<String, String> params = new LinkedHashMap<String, String> ();
		Map retmap = new LinkedHashMap();
		List<String> values = new LinkedList<String>(); 
		try {
			params.put("a.CLEARINGPHASE", clearingPhase);
			params.put("a.BIZDATE", txdt);
			params.put("b.CTBK_ID", CTBK_ID);
			int i = 0;
			for(String key :params.keySet()){
				if(StrUtils.isNotEmpty(params.get(key))){
					if(i==0){sql.append(" WHERE ");}
					if(i!=0){sql.append(" AND ");}
					if(key.equals("a.BIZDATE")){
						sql.append( key+" = ? ");
						values.add(DateTimeUtils.convertDate(params.get(key), "yyyymmdd", "yyyymmdd"));
					}else{
//						sql.append( key+" = '"+params.get(key)+"'");
						sql.append( key+" = ?");
						values.add(params.get(key));
					}
					i++;
				}
			}
			sql.append(" ");
			retmap.put("sqlPath", sql.toString());
			retmap.put("values", values);
			logger.debug("sql>>"+sql.toString());
			logger.debug("getConditionDataSQL_Path>>"+retmap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e.toString()) ; 
		}
		return retmap;
	}
	
	public Map getrRPTConditionData2(String txdt, String clearingPhase, String CTBK_ID) throws Exception{
		Map retmap = new LinkedHashMap();
		
		List<String> con1 = new ArrayList<String>();
		List<String> con2 = new ArrayList<String>();
		List<String> values = new LinkedList<String>();
		
		if(StrUtils.isNotEmpty(txdt)){
			con1.add( "T.BIZDATE = ? ");
			values.add(DateTimeUtils.convertDate(txdt, "yyyymmdd", "yyyymmdd"));
		}
		
		if(StrUtils.isNotEmpty(clearingPhase)){
			con1.add( "T.CLEARINGPHASE = ? ");
			values.add(clearingPhase);
		}

		if(StrUtils.isNotEmpty(CTBK_ID)){
			con2.add( "A.CTBK_ID = ? ");
			values.add(CTBK_ID);
		}
		
		String sql1 = combine(con1);
		String sql2 = combine(con2);
		
		retmap.put("sql1", sql1);
		retmap.put("sql2", sql2);
		retmap.put("values", values);
		return retmap;
	}
	
	public String getTXTSQL(String sqlPath){
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT T.BIZDATE ,STRIP(T.CLEARINGPHASE,L,'0' ) CLEARINGPHASE "); 
		sql.append(" , case when T.CLEARINGPHASE = '01' then 'F' else 'G' end CLTYPE "); 
		sql.append(" , T.BGBK_ID,T.TCH_ID ,T.BGBK_ATTR "); 
		sql.append(" ,LPAD( cast( T.OUTCNT as VARCHAR(6) )  , 6, '0')  AS OUTCNT "); 
		sql.append(" ,LPAD( cast( ABS(T.out_tol)as VARCHAR(14) )  , 14, '0')  AS out_tol "); 
		sql.append(" ,LPAD( cast( T.INCNT as VARCHAR(6) )  , 6, '0')  AS INCNT "); 
		sql.append(" ,LPAD( cast( ABS(T.in_tol)as VARCHAR(14) )  , 14, '0')  AS in_tol "); 
		sql.append(" ,LPAD( cast( ABS(T.dif_tol)as VARCHAR(14) )  , 14, '0')  AS dif_tol "); 
		sql.append(" , case when T.dif_tol < 0 THEN '-' ELSE '+' END DIF_MARK "); 
		sql.append(" FROM ( "); 
	    sql.append("  SELECT  coalesce( b.CTBK_ID ,'')  CTBK_ID  ");
	    sql.append("  ,'0'|| RIGHT( RTRIM(CHAR((YEAR(DATE(SUBSTR(a.BIZDATE,1,4) || '-' || SUBSTR(a.BIZDATE,5,2) || '-' || SUBSTR(a.BIZDATE,7,2)) ) - 1911))), 3) || '' || REPLACE(RIGHT(VARCHAR(DATE(SUBSTR(a.BIZDATE,1,4) || '-' || SUBSTR(a.BIZDATE,5,2) || '-' || SUBSTR(a.BIZDATE,7,2))),5), '-', '') BIZDATE ");
	    sql.append("    ,VARCHAR(a.CLEARINGPHASE) CLEARINGPHASE  , VARCHAR(a.BGBK_ID) BGBK_ID ,b.TCH_ID ,b.BGBK_ATTR ");
	    sql.append("    ,SUM(a.RECVCNT + a.RVSRECVCNT)  INCNT ,SUM(a.PAYCNT + a.RVSPAYCNT) OUTCNT ");
	    sql.append("    ,SUM(a.RECVAMT+a.RVSRECVAMT) in_tol, SUM(a.PAYAMT+a.RVSPAYAMT) out_tol,SUM((a.RECVAMT+a.RVSRECVAMT)+(a.PAYAMT+a.RVSPAYAMT)) dif_tol ");
	    sql.append(" 	FROM EACHUSER.RPONCLEARINGTAB a ");
	    sql.append(" 	RIGHT JOIN BANK_GROUP b ON a.BGBK_ID = b.BGBK_ID and b.BGBK_ID != b.CTBK_ID ");
	    sql.append(sqlPath);
	    sql.append(" 	GROUP BY b.CTBK_ID , a.BGBK_ID  ,CLEARINGPHASE  ,b.TCH_ID,b.BGBK_ATTR ,a.BIZDATE");
	    sql.append(" 	ORDER BY CLEARINGPHASE ,  b.CTBK_ID , a.BGBK_ID ");
	    sql.append(" ) T ");
	    logger.debug("getSQL>>"+sql);
		return sql.toString();
	}
	
	public String getTXTSQL2(String con1, String con2){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ");
		sql.append("T.BIZDATE, ");
		sql.append("STRIP(T.CLEARINGPHASE,L,'0') CLEARINGPHASE, ");
		sql.append("CASE WHEN T.CLEARINGPHASE = '01' THEN 'F' ELSE 'G' END CLTYPE, ");
		sql.append("T.BGBK_ID,T.TCH_ID, T.BGBK_ATTR, LPAD( CAST( T.OUTCNT AS VARCHAR(6)),  6, '0') AS OUTCNT, ");
		sql.append("LPAD( CAST( ABS(T.OUT_TOL)AS VARCHAR(14)), 14, '0') AS OUT_TOL, ");
		sql.append("LPAD( CAST( T.INCNT AS VARCHAR(6)), 6, '0') AS INCNT, ");
		sql.append("LPAD( CAST( ABS(T.IN_TOL)AS VARCHAR(14)), 14, '0') AS IN_TOL, ");
		sql.append("LPAD( CAST( ABS(T.DIF_TOL)AS VARCHAR(14)), 14, '0') AS DIF_TOL, ");
		sql.append("CASE WHEN T.DIF_TOL < 0 THEN '-' ELSE '+' END DIF_MARK ");
		sql.append("FROM ( ");
		sql.append("    SELECT COALESCE(A.CTBK_ID, '') CTBK_ID, ");
		sql.append("    '0'|| RIGHT(RTRIM(CHAR((YEAR(DATE(SUBSTR(A.BIZDATE,1,4) || '-' || SUBSTR(A.BIZDATE,5,2) || '-' || SUBSTR(A.BIZDATE,7,2))) - 1911))), 3) || '' || REPLACE(RIGHT(VARCHAR(DATE(SUBSTR(A.BIZDATE,1,4) || '-' || SUBSTR(A.BIZDATE,5,2) || '-' || SUBSTR(A.BIZDATE,7,2))),5), '-', '') BIZDATE, ");
		sql.append("    VARCHAR(A.CLEARINGPHASE) CLEARINGPHASE, ");
		sql.append("    VARCHAR(A.BGBK_ID) BGBK_ID, B.TCH_ID, B.BGBK_ATTR, ");
		sql.append("    SUM(A.RECVCNT + A.RVSRECVCNT) INCNT, SUM(A.PAYCNT + A.RVSPAYCNT) OUTCNT, ");
		sql.append("    SUM(A.RECVAMT+A.RVSRECVAMT) IN_TOL, SUM(A.PAYAMT+A.RVSPAYAMT) OUT_TOL,SUM((A.RECVAMT+A.RVSRECVAMT)+(A.PAYAMT+A.RVSPAYAMT)) DIF_TOL ");
		sql.append("    FROM ( ");
		sql.append("        SELECT T.*, GET_CUR_CTBKID(BGBK_ID, TRANS_DATE(BIZDATE, 'T', ''), 0) AS CTBK_ID ");
		sql.append("        FROM EACHUSER.RPONCLEARINGTAB AS T ");
		sql.append("        " + (StrUtils.isNotEmpty(con1)?"WHERE " + con1 : ""));
		sql.append("    ) A RIGHT JOIN MASTER_BANK_GROUP AS B ON A.BGBK_ID = B.BGBK_ID ");
		sql.append("    WHERE A.BGBK_ID <> A.CTBK_ID " + (StrUtils.isNotEmpty(con2)?"AND " + con2 : ""));
		sql.append("    GROUP BY A.CTBK_ID, A.BGBK_ID, CLEARINGPHASE, B.TCH_ID, B.BGBK_ATTR, A.BIZDATE ");
		sql.append("    ORDER BY A.CLEARINGPHASE, A.CTBK_ID, A.BGBK_ID ");
		sql.append(") T ");
		logger.debug("getTXTSQL2 >> " + sql);
		return sql.toString();
	}
	
	public String checkDate(Map<String,String> param){
		String resp="TRUE";
		Map retmap = super.checkRPT_BizDate(param.get("BIZDATE"), param.get("CLEARINGPHASE"));
		if(retmap.get("result").equals("FALSE") ){
			resp ="FALSE";
		}
		return resp;
	}
	
	public ONBLOCKTAB_Dao getOnblocktab_Dao() {
		return onblocktab_Dao;
	}
	public void setOnblocktab_Dao(ONBLOCKTAB_Dao onblocktab_Dao) {
		this.onblocktab_Dao = onblocktab_Dao;
	}
	public BANK_GROUP_Dao getBank_group_Dao() {
		return bank_group_Dao;
	}
	public void setBank_group_Dao(BANK_GROUP_Dao bank_group_Dao) {
		this.bank_group_Dao = bank_group_Dao;
	}
	public RPONBLOCKTAB_Dao getRponblocktab_Dao() {
		return rponblocktab_Dao;
	}
	public void setRponblocktab_Dao(RPONBLOCKTAB_Dao rponblocktab_Dao) {
		this.rponblocktab_Dao = rponblocktab_Dao;
	}
	public CodeUtils getCodeUtils() {
		return codeUtils;
	}
	public void setCodeUtils(CodeUtils codeUtils) {
		this.codeUtils = codeUtils;
	}


	public EACH_USERLOG_BO getUserlog_bo() {
		return userlog_bo;
	}


	public void setUserlog_bo(EACH_USERLOG_BO userlog_bo) {
		this.userlog_bo = userlog_bo;
	}


	public BANK_GROUP_BO getBank_group_bo() {
		return bank_group_bo;
	}


	public void setBank_group_bo(BANK_GROUP_BO bank_group_bo) {
		this.bank_group_bo = bank_group_bo;
	}
	
	private String combine(List<String> conditions){
		String sql = "";
		for(int i = 0; i < conditions.size(); i++){
			sql += conditions.get(i);
			if(i < conditions.size() - 1){
				sql += " AND ";
			}
		}
		return sql;
	}
}
