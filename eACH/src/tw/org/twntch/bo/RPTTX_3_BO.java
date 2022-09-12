package tw.org.twntch.bo;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.struts.util.LabelValueBean;

import tw.org.twntch.db.dao.hibernate.BANK_GROUP_Dao;
import tw.org.twntch.db.dao.hibernate.RPONBLOCKTAB_Dao;
import tw.org.twntch.po.BANK_GROUP;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.RptUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.zDateHandler;

public class RPTTX_3_BO {

	private RPONBLOCKTAB_Dao rponblocktab_Dao;
	private BANK_GROUP_Dao bank_group_Dao;
	public Map<String, String> export(String txdt, String opt_id,String bgbk_id , String clearingPhase ,String opt_bank ,String opt_type, String serchStrs){
		Map<String, String> rtnMap = null;
		String outputFilePath ="";
		try{
			rtnMap = new HashMap<String, String>();
			rtnMap.put("serchStrs", serchStrs);
			Map<String, Object> params = new HashMap<String, Object>();
//			顯示區塊
//			params.put("V_TXDT",DateTimeUtils.convertDate(DateTimeUtils.convertDate(txdt,"yyyyMMdd","yyyyMMdd"),"yyyyMMdd","民國 yyy 年 MM 月 dd 日"));
			params.put("V_TXDT",DateTimeUtils.convertDate(DateTimeUtils.convertDate(txdt,"yyyyMMdd","yyyyMMdd"),"yyyyMMdd","營業日期: yyy/MM/dd"));
			System.out.println("senderacquire>>"+opt_id);
			System.out.println("opt_id>>"+opt_id);
			if(StrUtils.isEmpty(opt_id)){
				params.put("V_OPT_BANK", "全部");
			}else{
				params.put("V_OPT_BANK", opt_id);
			}
			params.put("V_CLEARINGPHASE",clearingPhase);
			params.put("V_PRINT_DATE",zDateHandler.getODDate());
			params.put("V_PRINT_TIME",zDateHandler.getTheTime());
			params.put("V_OP_TYPE",getOpType_Name(Integer.valueOf(opt_type)));
			System.out.println("params >> " + params);
//			Map map = this.getConditionData(txdt, opt_id, bgbk_id, clearingPhase,Integer.valueOf(opt_type));
//			String sql = getSQL(map.get("sqlPath").toString(), getSPSql(Integer.valueOf(opt_type)), getOrderBySql(Integer.valueOf(opt_type)));
			Map map = this.getConditionDataForBat(txdt, opt_id, bgbk_id, clearingPhase,Integer.valueOf(opt_type));
			String sql = getSQLForBat(map.get("sqlPath").toString(),map.get("sqlPath2").toString(), getSPSql(Integer.valueOf(opt_type)), getOrderBySql(Integer.valueOf(opt_type)));
			List<Map> list = rponblocktab_Dao.getTx_Detail_Data_ForRpt(sql, (List<String>) map.get("values"));
			int pathType = Arguments.getStringArg("ISWINOPEN").equals("Y")?RptUtils.C_PATH :RptUtils.R_PATH;
			outputFilePath = RptUtils.export(RptUtils.COLLECTION,pathType, "tx_3", "tx_3", params, list);
//			System.out.println("tmp2>>"+tmp2);
			System.out.println("sql>>"+sql);
			System.out.println("map>>"+map);
			if(StrUtils.isNotEmpty(outputFilePath)){
				rtnMap.put("result", "TRUE");
				rtnMap.put("msg", outputFilePath);
			}else{
				rtnMap.put("result", "FALSE");
				rtnMap.put("msg", "產生失敗!");
			}
		}catch(Exception e){
			e.printStackTrace();
			rtnMap.put("result", "FALSE");
			rtnMap.put("msg", "產生錯誤!");
		}
		
		return rtnMap;
	}
	
	public String getOpType_Name(int opt_type){
		String ret = "";
		switch (opt_type) {
		case 1:
			ret = "依扣款行";
			break;
		case 2:
			ret = "依入帳行";
			break;
		default:
			ret = "依發動行";
			break;
		}
		return ret ;
	}
	
	
	public String getSQLForBat(String sqlPath ,String sqlPath2  , String spSql , String orderbySql){
		StringBuffer sql = new StringBuffer();
		sql.append(" WITH TEMP AS  ");
		sql.append(" ( ");
		sql.append("SELECT ");
		sql.append("COALESCE(A.RESULTCODE, '') CONRESULTCODE, COALESCE(A.CLEARINGPHASE, '') CLEARINGPHASE, "); 
		sql.append("SUBSTR(TRANS_DATE(A.OTXDATE, 'T', '/'),2,9) TXDATE , VARCHAR(SUBSTR(TRANS_DATE(A.BIZDATE, 'T', '/'),2,9)) BIZDATE, ");
		sql.append("(CASE WHEN COALESCE(VARCHAR(B.TXDT),'') = '' THEN '' ELSE TO_TWDATETIMEII(VARCHAR_FORMAT(B.TXDT,'YYYYMMDDHH24MISS')) END) TXDT, ");
		sql.append("C.BUSINESS_TYPE_ID || '-' || COALESCE((SELECT BUSINESS_TYPE_NAME FROM BUSINESS_TYPE WHERE BUSINESS_TYPE_ID = C.BUSINESS_TYPE_ID),'') BIZTYPE, ");
		sql.append("A.PCODE || '-' || COALESCE(C.EACH_TXN_NAME,'') AS PCODE, VARCHAR(A.OSTAN) AS OSTAN, ");
		sql.append("COALESCE(A.SENDERBANKID,'') SENDERBANKID, COALESCE((SELECT BRBK_NAME FROM BANK_BRANCH WHERE BRBK_ID = A.SENDERBANKID),'') SENDERBANKID_NAME, ");
		sql.append("COALESCE(A.OUTBANKID,'') AS OUTBANKID, COALESCE((SELECT BRBK_NAME FROM BANK_BRANCH WHERE BRBK_ID = A.OUTBANKID),'') OUTBANKID_NAME, ");
		sql.append("COALESCE(A.INBANKID,'') AS INBANKID, COALESCE((SELECT BRBK_NAME FROM BANK_BRANCH WHERE BRBK_ID = A.INBANKID),'') INBANKID_NAME, ");
		sql.append("COALESCE(A.OUTACCT, B.OUTACCTNO) AS OUTACCTNO, COALESCE(A.INACCT, B.INACCTNO) AS INACCTNO, DECIMAL(A.TXAMT) AS TXAMT, ");
		sql.append("CASE A.RESULTCODE WHEN '01' THEN '失敗' ELSE '成功' END RESULTCODE ");
		sql.append(spSql);
		sql.append("FROM RPONPENDINGTAB AS A LEFT JOIN RPONBLOCKTAB AS B on A.OSTAN = B.STAN AND A.OTXDATE = B.TXDATE ");
		sql.append("LEFT JOIN EACH_TXN_CODE AS C ON C.EACH_TXN_ID = A.PCODE ");
		sql.append(sqlPath);
		sql.append(" UNION ALL ");
		sql.append("SELECT ");
		sql.append("COALESCE(A.RESULTCODE, '') CONRESULTCODE, COALESCE(A.CLEARINGPHASE, '') CLEARINGPHASE, "); 
		sql.append("SUBSTR(TRANS_DATE(A.OTXDATE, 'T', '/'),2,9) TXDATE , VARCHAR(SUBSTR(TRANS_DATE(A.BIZDATE, 'T', '/'),2,9)) BIZDATE, ");
		sql.append("(CASE WHEN COALESCE(VARCHAR(B.TXDT),'') = '' THEN '' ELSE TO_TWDATETIMEII(VARCHAR_FORMAT(B.TXDT,'YYYYMMDDHH24MISS')) END) TXDT, ");
		sql.append("C.BUSINESS_TYPE_ID || '-' || COALESCE((SELECT BUSINESS_TYPE_NAME FROM BUSINESS_TYPE WHERE BUSINESS_TYPE_ID = C.BUSINESS_TYPE_ID),'') BIZTYPE, ");
		sql.append("A.PCODE || '-' || COALESCE(C.EACH_TXN_NAME,'') AS PCODE, VARCHAR(A.OSTAN) AS OSTAN, ");
		sql.append("COALESCE(A.SENDERBANKID,'') SENDERBANKID, COALESCE((SELECT BRBK_NAME FROM BANK_BRANCH WHERE BRBK_ID = A.SENDERBANKID),'') SENDERBANKID_NAME, ");
		sql.append("COALESCE(A.OUTBANKID,'') AS OUTBANKID, COALESCE((SELECT BRBK_NAME FROM BANK_BRANCH WHERE BRBK_ID = A.OUTBANKID),'') OUTBANKID_NAME, ");
		sql.append("COALESCE(A.INBANKID,'') AS INBANKID, COALESCE((SELECT BRBK_NAME FROM BANK_BRANCH WHERE BRBK_ID = A.INBANKID),'') INBANKID_NAME, ");
		sql.append("COALESCE(A.OUTACCT, B.OUTACCTNO) AS OUTACCTNO, COALESCE(A.INACCT, B.INACCTNO) AS INACCTNO, DECIMAL(A.TXAMT) AS TXAMT, ");
		sql.append("CASE A.RESULTCODE WHEN '01' THEN '失敗' ELSE '成功' END RESULTCODE ");
		sql.append(spSql);
		sql.append("FROM RPONPENDINGTAB AS A LEFT JOIN RPONBLOCKTAB AS B on A.OSTAN = B.STAN AND A.OTXDATE = B.TXDATE ");
		sql.append("LEFT JOIN EACH_TXN_CODE AS C ON C.EACH_TXN_ID = A.PCODE ");
		sql.append(sqlPath2);
		sql.append(" ) ");
		sql.append(" SELECT * FROM TEMP a ");
		sql.append(orderbySql);
		
		System.out.println("getSQLForBat.sql>>>>"+sql);
		return sql.toString();
	}
	
	public String getSQL(String sqlPath , String spSql , String orderbySql){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ");
		sql.append("COALESCE(A.RESULTCODE, '') CONRESULTCODE, COALESCE(A.CLEARINGPHASE, '') CLEARINGPHASE, "); 
		sql.append("SUBSTR(TRANS_DATE(A.OTXDATE, 'T', '/'),2,9) TXDATE , VARCHAR(SUBSTR(TRANS_DATE(A.BIZDATE, 'T', '/'),2,9)) BIZDATE, ");
		sql.append("(CASE WHEN COALESCE(VARCHAR(B.TXDT),'') = '' THEN '' ELSE TO_TWDATETIMEII(VARCHAR_FORMAT(B.TXDT,'YYYYMMDDHH24MISS')) END) TXDT, ");
		sql.append("C.BUSINESS_TYPE_ID || '-' || COALESCE((SELECT BUSINESS_TYPE_NAME FROM BUSINESS_TYPE WHERE BUSINESS_TYPE_ID = C.BUSINESS_TYPE_ID),'') BIZTYPE, ");
		sql.append("A.PCODE || '-' || COALESCE(C.EACH_TXN_NAME,'') AS PCODE, VARCHAR(A.OSTAN) AS OSTAN, ");
		sql.append("COALESCE(A.SENDERBANKID,'') SENDERBANKID, COALESCE((SELECT BRBK_NAME FROM BANK_BRANCH WHERE BRBK_ID = A.SENDERBANKID),'') SENDERBANKID_NAME, ");
		sql.append("COALESCE(A.OUTBANKID,'') AS OUTBANKID, COALESCE((SELECT BRBK_NAME FROM BANK_BRANCH WHERE BRBK_ID = A.OUTBANKID),'') OUTBANKID_NAME, ");
		sql.append("COALESCE(A.INBANKID,'') AS INBANKID, COALESCE((SELECT BRBK_NAME FROM BANK_BRANCH WHERE BRBK_ID = A.INBANKID),'') INBANKID_NAME, ");
		sql.append("COALESCE(A.OUTACCT, B.OUTACCTNO) AS OUTACCTNO, COALESCE(A.INACCT, B.INACCTNO) AS INACCTNO, DECIMAL(A.TXAMT) AS TXAMT, ");
		sql.append("CASE A.RESULTCODE WHEN '01' THEN '失敗' ELSE '成功' END RESULTCODE ");
		sql.append(spSql);
		sql.append("FROM RPONPENDINGTAB AS A LEFT JOIN RPONBLOCKTAB AS B on A.OSTAN = B.STAN AND A.OTXDATE = B.TXDATE ");
		sql.append("LEFT JOIN EACH_TXN_CODE AS C ON C.EACH_TXN_ID = A.PCODE ");
		sql.append(sqlPath);
		sql.append(orderbySql);
		
		/*
//		sql.append(" SELECT REPLACE( coalesce( a.RESP ,'') ,'-' , '\n') CONRESULTCODE , coalesce(a.CLEARINGPHASE ,'')  CLEARINGPHASE ");
		sql.append(" SELECT coalesce( a.RESULTCODE ,'') CONRESULTCODE , coalesce(a.CLEARINGPHASE ,'')  CLEARINGPHASE ");
		sql.append(" , RIGHT( RTRIM(CHAR((YEAR(DATE(SUBSTR(b.TXDATE,1,4) || '-' || SUBSTR(b.TXDATE,5,2) || '-' || SUBSTR(b.TXDATE,7,2)) ) - 1911))), 4) || '/' || REPLACE(RIGHT(VARCHAR(DATE(SUBSTR(b.TXDATE,1,4) || '-' || SUBSTR(b.TXDATE,5,2) || '-' || SUBSTR(b.TXDATE,7,2))),5), '-', '/') TXDATE ");
		sql.append(" , RIGHT( RTRIM(CHAR((YEAR(DATE(SUBSTR(b.BIZDATE,1,4) || '-' || SUBSTR(b.BIZDATE,5,2) || '-' || SUBSTR(b.BIZDATE,7,2)) ) - 1911))), 4) || '/' || REPLACE(RIGHT(VARCHAR(DATE(SUBSTR(b.BIZDATE,1,4) || '-' || SUBSTR(b.BIZDATE,5,2) || '-' || SUBSTR(b.BIZDATE,7,2))),5), '-', '/') BIZDATE ");
//		sql.append(" ,(CASE WHEN coalesce(b.TXDATE,'') ='' THEN '' ELSE  SUBSTR(REPLACE(VARCHAR_FORMAT( TIMESTAMP('0'|| CAST((CAST( SUBSTR (b.TXDATE,1,4) AS INTEGER ) -1911)  AS CHAR(3) ) || SUBSTR (b.TXDATE,5,4) ||'000000'),'YYYY-MM-DD HH24:MI:SS'),'-','/'),1,10)   END ) TXDATE ");
//		sql.append(" ,(CASE WHEN coalesce(b.BIZDATE,'') ='' THEN '' ELSE  SUBSTR(REPLACE(VARCHAR_FORMAT( TIMESTAMP('0'|| CAST((CAST( SUBSTR (b.BIZDATE,1,4) AS INTEGER ) -1911)  AS CHAR(3) ) || SUBSTR (b.BIZDATE,5,4) ||'000000'),'YYYY-MM-DD HH24:MI:SS'),'-','/'),1,10)   END ) BIZDATE ");
//		sql.append(" , TO_TWDATEII(b.TXDATE)  TXDATE ");
//		sql.append(" , TO_TWDATEII(a.BIZDATE) BIZDATE ");
		sql.append(" , (CASE WHEN coalesce(VARCHAR(b.txDT),'') ='' THEN '' ELSE (CAST (YEAR(b.txDT)-1911 AS CHAR(3)) ||''|| SUBSTR( REPLACE(CAST( DATE(b.txdt) AS CHAR(10)),'-','/'),5,6)  ||' '|| SUBSTR(REPLACE(CAST( TIME(b.TXDT) AS CHAR(8)) ,'.',':') ,1,8))  END ) TXDT  ");
		sql.append(" ,c.business_type_id || coalesce((select (business_type_name) from BUSINESS_TYPE where business_type_id=c.business_type_id),'') biztype");
		sql.append(" ,a.pcode ||'\n'|| coalesce(c.each_txn_name,'')  pcode , b.STAN  ");
		sql.append(" ,coalesce(a.SENDERBANKID ,'') SENDERBANKID , coalesce((select brbk_name from bank_branch where brbk_id=a.SENDERBANKID),'') SENDERBANKID_NAME ");
		sql.append(" ,coalesce(a.outBankId,'')  outBankId , coalesce((select brbk_name from bank_branch where brbk_id=a.outBankId),'') outBankId_NAME  ");
		sql.append(" ,coalesce(a.inBankId ,'')  inBankId , coalesce((select brbk_name from bank_branch where brbk_id=a.inBankId),'') inBankId_NAME ");
//		sql.append(" ,OUTACCTNO ,INACCTNO, a.txAmt , case a.RESULTCODE when  '01' then '失敗' else '成功' end  RESULTCODE ");
//		sql.append(" ,OUTACCTNO ,INACCTNO, STRIP(a.txAmt ,L ,'0') ||'' as txamt , case a.RESULTCODE when  '01' then '失敗' else '成功' end  RESULTCODE ");
		sql.append(" ,OUTACCTNO ,INACCTNO, decimal (a.txAmt ) as txamt , case a.RESULTCODE when  '01' then '失敗' else '成功' end  RESULTCODE ");
		sql.append(spSql);
		sql.append(" FROM RPONPENDINGTAB a");
		sql.append("  LEFT JOIN RPONBLOCKTAB b on a.OSTAN = b.stan AND a.OBIZDATE = b.BIZDATE ");
		sql.append("  left join EACH_TXN_CODE c on c.each_txn_id = a.pcode ");
		sql.append(sqlPath);
		sql.append(orderbySql);
		 */
		System.out.println("sql>>>>"+sql);
		return sql.toString();
	}
	
	
	public String getOrderBySql(int opt_type){
		StringBuffer sql = new StringBuffer();
		sql.append(" ORDER BY A.CLEARINGPHASE ");
		sql.append(" ,BANKID ");
//		switch (opt_type) {
//		case 0:
////			sql.append(" , a.SENDERHEAD ");
//			sql.append(" , BANKID ");
//			break;
//		case 1:
////			sql.append(" , a.OUTHEAD ");
//			sql.append(" , BANKID ");
//			break;
//		case 2:
////			sql.append(" , a.INHEAD ");
//			sql.append(" , BANKID ");
//			break;
//		default:
////			sql.append(" , a.SENDERHEAD ");
//			sql.append(" , BANKID ");
//			break;
//		}
//		sql.append(" , A.PCODE, B.TXDT, A.OSTAN ");
		sql.append(" , A.PCODE, A.TXDT, A.OSTAN ");
		System.out.println("OrderBySql>>"+sql);
		return sql.toString();
	}
	
	
	public Map getConditionDataForBat(String txdt, String opt_id ,String bgbk_id , String clearingPhase ,int opt_type) throws Exception{
		StringBuffer sql = new StringBuffer();
		StringBuffer sql2 = new StringBuffer();
		boolean hasOpt_Id = StrUtils.isNotEmpty(opt_id) ? true : false;
		Map<String, String> params = new LinkedHashMap<String, String> ();
		Map<String, String> params2 = new LinkedHashMap<String, String> ();
		Map retmap = new LinkedHashMap();
		List<String> values = new LinkedList<String>(); 
		try {
//			params.put("a.RESULTSTATUS", "P");
//			params.put("a.newresult", "P");
//			params.put("a.SENDERACQUIRE", senderacquire);
			params.put("a.CLEARINGPHASE", clearingPhase);
			params.put("a.BIZDATE", txdt);
			params2.putAll(params);
			switch (opt_type) {
			case 0:
//				params.put("a.SENDERACQUIRE", opt_id);
//				params.put("a.SENDERHEAD", bgbk_id);
				
				params.put("a.FLBIZDATE", "''");
				params.put("a.SENDERACQUIRE", opt_id);
				params.put("a.SENDERHEAD", bgbk_id);
				
				params2.put("a.FLBIZDATE", "''");
				params2.put("a.SENDERACQUIRE", opt_id);
				params2.put("a.SENDERHEAD", bgbk_id);
				
				break;
			case 1:
//				params.put("a.OUTACQUIRE", opt_id);
//				params.put("a.OUTHEAD", bgbk_id);
				params.put("a.FLBIZDATE", "''");
				params.put("a.OUTACQUIRE", opt_id);
				params.put("a.OUTHEAD", bgbk_id);
				if(hasOpt_Id){
					params2.putAll(params);
				}
				params2.put("a.FLBIZDATE", "''");
				params2.put(" substr(COALESCE(a.PCODE,''),4)  ", "1");
				break;
			case 2:
//				params.put("a.INACQUIRE", opt_id);
//				params.put("a.INHEAD", bgbk_id);
				params.put("a.FLBIZDATE", "''");
				params.put("a.INACQUIRE", opt_id);
				params.put("a.INHEAD", bgbk_id);
				if(hasOpt_Id){
					params2.putAll(params);
				}
				params2.put("a.FLBIZDATE", "''");
				params2.put(" substr(COALESCE(a.PCODE,''),4)  ", "2");
				break;
			default:
//				params.put("a.SENDERACQUIRE", opt_id);
//				params.put("a.SENDERHEAD", bgbk_id);
				params.put("a.FLBIZDATE", "''");
				params.put("a.SENDERACQUIRE", opt_id);
				params.put("a.SENDERHEAD", bgbk_id);
				
				params2.put("a.FLBIZDATE", "''");
				params2.put("a.SENDERACQUIRE", opt_id);
				params2.put("a.SENDERHEAD", bgbk_id);
				break;
			}
			int i = 0;
			for(String key :params.keySet()){
				if(StrUtils.isNotEmpty(params.get(key))){
					if(i==0){sql.append(" WHERE ");}
					if(i!=0){sql.append(" AND ");}
					if(key.equals("a.FLBIZDATE")){
						sql.append( "coalesce( "+key+" , '')  = '' " );
					}else if(key.equals("a.BIZDATE")){
						sql.append( key+" = ? ");
						values.add(DateTimeUtils.convertDate(params.get(key), "yyyymmdd", "yyyymmdd"));
					}
					else if(key.equals("a.newresult")){
						sql.append( key+" != ? ");
						values.add(params.get(key));
					}else{
						sql.append( key+" = ? ");
						values.add(params.get(key));
					}
					i++;
				}
			}
			i = 0;
			for(String key :params2.keySet()){
				if(StrUtils.isNotEmpty(params2.get(key))){
					if(i==0){sql2.append(" WHERE ");}
					if(i!=0){sql2.append(" AND ");}
					if(key.equals("a.FLBIZDATE")){
						sql2.append( "coalesce( "+key+" , '')  <> '' " );
					}else if(key.equals("a.BIZDATE")){
						sql2.append( key+" = ? ");
						values.add(DateTimeUtils.convertDate(params2.get(key), "yyyymmdd", "yyyymmdd"));
					}
					else if(key.equals("a.SENDERACQUIRE")){
						sql2.append("("+ key+" = ? ");
						sql2.append( " AND substr(COALESCE(a.PCODE,''),4) NOT IN ( '1' ,'2' ,'3' , '4')) " );
						values.add(params2.get(key));
					}else if(key.equals("a.newresult")){
						sql2.append( key+" != ? ");
						values.add(params2.get(key));
					}
					else{
						sql2.append( key+" = ? ");
						values.add(params2.get(key));
					}
					i++;
				}
			}
			sql.append(" ");
			sql2.append(" ");
			retmap.put("sqlPath", sql.toString());
			retmap.put("sqlPath2", sql2.toString());
			retmap.put("values", values);
			System.out.println("getConditionDataSQL_Path>>"+retmap);
			System.out.println("getConditionDataSQL_Path>>"+retmap);
			System.out.println("getConditionDataSQL_Path>>"+retmap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e.toString()) ; 
		}
		return retmap;
	}
	public Map getConditionData(String txdt, String opt_id ,String bgbk_id , String clearingPhase ,int opt_type) throws Exception{
		StringBuffer sql = new StringBuffer();
		Map<String, String> params = new LinkedHashMap<String, String> ();
		Map retmap = new LinkedHashMap();
		List<String> values = new LinkedList<String>(); 
		try {
//			params.put("a.RESULTSTATUS", "P");
//			params.put("a.newresult", "P");
//			params.put("a.SENDERACQUIRE", senderacquire);
			params.put("a.CLEARINGPHASE", clearingPhase);
			params.put("a.BIZDATE", txdt);
			
			switch (opt_type) {
			case 0:
				params.put("a.SENDERACQUIRE", opt_id);
				params.put("a.SENDERHEAD", bgbk_id);
				break;
			case 1:
				params.put("a.OUTACQUIRE", opt_id);
				params.put("a.OUTHEAD", bgbk_id);
				break;
			case 2:
				params.put("a.INACQUIRE", opt_id);
				params.put("a.INHEAD", bgbk_id);
				break;
			default:
				params.put("a.SENDERACQUIRE", opt_id);
				params.put("a.SENDERHEAD", bgbk_id);
				break;
			}
			int i = 0;
			for(String key :params.keySet()){
				if(StrUtils.isNotEmpty(params.get(key))){
					if(i==0){sql.append(" WHERE ");}
					if(i!=0){sql.append(" AND ");}
					if(key.equals("a.BIZDATE")){
						sql.append( key+" = ? ");
						values.add(DateTimeUtils.convertDate(params.get(key), "yyyymmdd", "yyyymmdd"));
					}
					else if(key.equals("a.newresult")){
						sql.append( key+" != ? ");
						values.add(params.get(key));
					}
					else{
						sql.append( key+" = ? ");
						values.add(params.get(key));
					}
					i++;
				}
			}
			sql.append(" ");
			retmap.put("sqlPath", sql.toString());
			retmap.put("values", values);
			System.out.println("getConditionDataSQL_Path>>"+retmap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e.toString()) ; 
		}
		return retmap;
	}
	
	
	public List<LabelValueBean> getBgbkIdList(){
		List<BANK_GROUP> list = bank_group_Dao.getBgbkIdList_2();
		List<LabelValueBean> beanList = new LinkedList<LabelValueBean>();
		LabelValueBean bean = null;
		for(BANK_GROUP po : list){
			bean = new LabelValueBean(po.getBGBK_ID() + " - " + po.getBGBK_NAME(), po.getBGBK_ID());
			beanList.add(bean);
		}
		System.out.println("beanList>>"+beanList);
		return beanList;
	}
	
	public String getSPSql(int opt_type){
		StringBuffer sql = new StringBuffer();
		switch (opt_type) {
		case 0:
			sql.append(" ,COALESCE(A.SENDERHEAD,'') BANKID, COALESCE((SELECT BRBK_NAME FROM BANK_BRANCH WHERE BRBK_ID = A.SENDERHEAD),'') BANK_NAME ");
			break;
		case 1:
			sql.append(" ,COALESCE(A.OUTHEAD,'') BANKID, coalesce((SELECT BRBK_NAME FROM BANK_BRANCH WHERE BRBK_ID = A.OUTHEAD),'') BANK_NAME ");
			break;
		case 2:
			sql.append(" ,COALESCE(A.INHEAD,'') BANKID, coalesce((SELECT BRBK_NAME FROM BANK_BRANCH WHERE BRBK_ID = A.INHEAD),'') BANK_NAME ");
			break;
		default:
			sql.append(" ,COALESCE(A.SENDERHEAD,'') BANKID, coalesce((SELECT BRBK_NAME FROM BANK_BRANCH WHERE BRBK_ID = A.SENDERHEAD),'') BANK_NAME ");
			break;
		}
		System.out.println("SPSql>>"+sql);
		return sql.toString();
	}
	public RPONBLOCKTAB_Dao getRponblocktab_Dao() {
		return rponblocktab_Dao;
	}

	public void setRponblocktab_Dao(RPONBLOCKTAB_Dao rponblocktab_Dao) {
		this.rponblocktab_Dao = rponblocktab_Dao;
	}

	public BANK_GROUP_Dao getBank_group_Dao() {
		return bank_group_Dao;
	}

	public void setBank_group_Dao(BANK_GROUP_Dao bank_group_Dao) {
		this.bank_group_Dao = bank_group_Dao;
	}
	
	
	
}
