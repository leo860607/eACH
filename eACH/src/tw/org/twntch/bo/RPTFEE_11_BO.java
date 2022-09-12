package tw.org.twntch.bo;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.apache.struts.util.LabelValueBean;

import tw.org.twntch.db.dao.hibernate.BANK_GROUP_Dao;
import tw.org.twntch.db.dao.hibernate.RPONBLOCKTAB_Dao;
import tw.org.twntch.po.BANK_GROUP;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.RptUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.zDateHandler;

public class RPTFEE_11_BO {

	private RPONBLOCKTAB_Dao rponblocktab_Dao;
	private BANK_GROUP_Dao bank_group_Dao;
	private Logger log = Logger.getLogger(this.getClass().getName());
	
	public Map<String, String> export(String txdt, String opt_id,String bgbk_id ,  String clearingPhase ,  String opt_bank , String senderid ,String txnid ,String txtype ,String fee_type ,String serchStrs ){
		Map<String, String> rtnMap = null;
		String outputFilePath = "";
		String filename = "fee_11";
		try{
			rtnMap = new HashMap<String, String>();
			rtnMap.put("serchStrs", serchStrs);
			Map<String, Object> params = new HashMap<String, Object>();
//			SQL查詢條件 區塊
			params.put("TXDT","'%"+ DateTimeUtils.convertDate(txdt, "yyyymmdd", "yyyymmdd")+"%' ");
			params.put("opt_id", opt_id);
			params.put("CLEARINGPHASE", clearingPhase);
//			顯示區塊
			params.put("V_TXDT",DateTimeUtils.convertDate(DateTimeUtils.convertDate(txdt,"yyyyMMdd","yyyyMMdd"),"yyyyMMdd","營業日期: yyy/MM/dd"));
			log.debug("opt_id>>"+opt_id);
			log.debug("opt_bank>>"+opt_bank);
			if(StrUtils.isEmpty(opt_id)){
				params.put("V_OPT_BANK", "全部");
			}else{
				params.put("V_OPT_BANK", opt_bank);
			}
			params.put("V_CLEARINGPHASE",clearingPhase);
			params.put("V_PRINT_DATE",DateTimeUtils.convertDate(DateTimeUtils.INTERCONVERSION, zDateHandler.getTheDate(), "yyyy/MM/dd", "yyy/MM/dd"));
			params.put("V_PRINT_TIME",zDateHandler.getTheTime());
//			params.put("V_OP_TYPE",getOpType_Name(Integer.valueOf(opt_type)));
			log.debug("params >> " + params);
			Map map = null;
			String sql = "";
			map = this.getConditionData(txdt, opt_id , bgbk_id, clearingPhase ,senderid , txnid , txtype ,fee_type);
			sql = getSQL(map , getOrderBySql());
			List list = rponblocktab_Dao.getRptData(sql, (List<String>) map.get("values"));
			int pathType = Arguments.getStringArg("ISWINOPEN").equals("Y")?RptUtils.C_PATH :RptUtils.R_PATH;
			outputFilePath = RptUtils.export(RptUtils.COLLECTION,pathType, filename, filename, params, list);
			log.debug("RPTFEE_11_BO.sql>>"+sql);
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
	
	
	public String getspSqlII(int opt_type){
		String sql = "";
		switch (opt_type) {
		case 1:
//			依扣款行
			sql = ",(case   WHEN substr(COALESCE(PCODE,''),4)   IN ( '1','2','3','4','5','6') AND COALESCE(RC1,'') = ''   AND COALESCE(RC2,'') <> '0601' AND a.newresult='R' THEN 'R1'    when a.newresult='R' then '失敗' when a.newresult='P' and a.senderstatus != '1' then '未完成' when a.newresult='P' and a.senderstatus = '1' then '失敗' else '成功' end) Resp  ";
			break;
		case 2:
//			依入帳行
			sql = ",(case   WHEN substr(COALESCE(PCODE,''),4)   IN ( '4','6') AND COALESCE(RC5,'') = ''   AND COALESCE(RC2,'') <> '0601' AND COALESCE(RC1,'') ='3001' AND a.newresult='R'  THEN 'R1'    when a.newresult='R' then '失敗' when a.newresult='P' and a.senderstatus != '1' then '未完成' when a.newresult='P' and a.senderstatus = '1' then '失敗' else '成功' end) Resp  ";
			break;
		case 3:
//			依銷帳行
			sql = ",(case   WHEN substr(COALESCE(PCODE,''),4)   IN ( '4','6') AND COALESCE(RC5,'') = ''   AND COALESCE(RC2,'') <> '0601' AND COALESCE(RC1,'') ='3001' AND a.newresult='R'  THEN 'R1'    when a.newresult='R' then '失敗' when a.newresult='P' and a.senderstatus != '1' then '未完成' when a.newresult='P' and a.senderstatus = '1' then '失敗' else '成功' end) Resp  ";
			break;
		default:
//			依發動行 無失敗未送達
			sql = ",(case  when a.newresult='R' then '失敗' when a.newresult='P' and a.senderstatus != '1' then '未完成' when a.newresult='P' and a.senderstatus = '1' then '失敗' else '成功' end) Resp";
			break;
		}
		
		return sql;
		
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
		case 3:
			ret = "依銷帳行";
			break;
		default:
			ret = "依發動行";
			break;
		}
		return ret ;
	}
	public String getOpType(int opt_type){
		String ret = "";
		switch (opt_type) {
		case 1:
//			ret = "O";
			ret = "B";
			break;
		case 2:
			ret = "I";
			break;
		case 3:
			ret = "W";
			break;
		default:
			ret = "S";
			break;
		}
		return ret ;
	}
	
	
	public String getOrderBySql(){
		StringBuffer sql = new StringBuffer();
		sql.append(" order by  a.CLEARINGPHASE , a.ACQUIRE , a.BANK_ID ,a.ROLETYPE  , a.SENDERID ,a.TXID , a.TXTYPE");
//		sql.append(" order by  a.CLEARINGPHASE , a.SENDERACQUIRE ,a.ROLETYPE  , a.SENDERID ,a.TXID , a.TXTYPE");
//		sql.append(" , BANKID ");
//		sql.append(" , a.PCODE ,a.TXDT,a.STAN ");
		log.debug("OrderBySql>>"+sql);
		return sql.toString();
	}
	public String getSPSql(int opt_type){
		StringBuffer sql = new StringBuffer();
		switch (opt_type) {
		case 0:
			sql.append(" ,coalesce(a.SENDERHEAD,'') BANKID , coalesce((select brbk_name from bank_branch where brbk_id=a.SENDERHEAD),'') BANK_NAME ");
//			sql.append(" ,(case when a.newresult !='R' then  coalesce(a.senderfee,0) else 0 end) fee ");
//			20150527 edit by hugo 因SP使用的ISNUMERICII 所回傳的 senderfee、outfee、infee 只有判斷A(成功)或P但ONPENDING.RESULTCODE !='01' 才會有手續費
//			但a.newresult='P' and a.senderstatus = '1' 的交易不會出現在ONPENDING中 ，會被視為"00"，會出現手續費 故自行加入判斷
//			sql.append(" ,coalesce(a.senderfee,0)  fee ");
			sql.append(" , (case when a.newresult ='A' OR (a.newresult='P' and a.senderstatus != '1')  then  coalesce(a.senderfee,0) else 0 end) fee");
			break;
		case 1:
			sql.append(" ,coalesce(a.OUTHEAD,'') BANKID , coalesce((select brbk_name from bank_branch where brbk_id=a.OUTHEAD),'') BANK_NAME ");
//			sql.append(" ,(case when a.newresult !='R' then  coalesce(a.outfee,0) else 0 end) fee ");
//			20150527 edit by hugo 因SP使用的ISNUMERICII 所回傳的 senderfee、outfee、infee 只有判斷A(成功)或P但ONPENDING.RESULTCODE !='01' 才會有手續費
//			但a.newresult='P' and a.senderstatus = '1' 的交易不會出現在ONPENDING中 ，會被視為"00"，會出現手續費 故自行加入判斷
//			sql.append(" ,coalesce(a.outfee,0)  fee ");
			sql.append(" , (case when a.newresult ='A' OR (a.newresult='P' and a.senderstatus != '1')  then  coalesce(a.outfee,0) else 0 end) fee");
			break;
		case 2:
			sql.append(" ,coalesce(a.INHEAD,'') BANKID , coalesce((select brbk_name from bank_branch where brbk_id=a.INHEAD),'') BANK_NAME ");
//			sql.append(" ,(case when a.newresult !='R' then  coalesce(a.infee,0) else 0 end) fee ");
//			20150527 edit by hugo 因SP使用的ISNUMERICII 所回傳的 senderfee、outfee、infee 只有判斷A(成功)或P但ONPENDING.RESULTCODE !='01' 才會有手續費
//			但a.newresult='P' and a.senderstatus = '1' 的交易不會出現在ONPENDING中 ，會被視為"00"，會出現手續費 故自行加入判斷
//			sql.append(" ,coalesce(a.infee,0)  fee ");
			sql.append(" , (case when a.newresult ='A' OR (a.newresult='P' and a.senderstatus != '1')  then  coalesce(a.infee,0) else 0 end) fee");
			break;
		case 3:
			sql.append(" ,coalesce(a.WOHEAD,'') BANKID , coalesce((select brbk_name from bank_branch where brbk_id=a.WOHEAD),'') BANK_NAME ");
//			sql.append(" ,(case when a.newresult !='R' then  coalesce(a.infee,0) else 0 end) fee ");
//			20150527 edit by hugo 因SP使用的ISNUMERICII 所回傳的 senderfee、outfee、infee 只有判斷A(成功)或P但ONPENDING.RESULTCODE !='01' 才會有手續費
//			但a.newresult='P' and a.senderstatus = '1' 的交易不會出現在ONPENDING中 ，會被視為"00"，會出現手續費 故自行加入判斷
//			sql.append(" ,coalesce(a.infee,0)  fee ");
			sql.append(" , (case when a.newresult ='A' OR (a.newresult='P' and a.senderstatus != '1')  then  coalesce(a.wofee,0) else 0 end) fee");
			break;
		default:
			sql.append(" ,coalesce(a.SENDERHEAD,'') BANKID , coalesce((select brbk_name from bank_branch where brbk_id=a.SENDERHEAD),'') BANK_NAME ");
//			sql.append(" ,(case when a.newresult !='R' then  coalesce(a.senderfee,0) else 0 end) fee ");
			sql.append(" ,coalesce(a.senderfee,0)  fee ");
			break;
		}
		log.debug("SPSql>>"+sql);
		return sql.toString();
	}
	
	public Map getConditionData(String txdt, String opt_id,String bgbk_id , String clearingPhase ,String senderid ,String txnid ,String txtype ,String fee_type) throws Exception{
		StringBuffer sql = new StringBuffer();
		StringBuffer sql2 = new StringBuffer();
		StringBuffer sql3 = new StringBuffer();
		StringBuffer sql4 = new StringBuffer();
		StringBuffer sql5 = new StringBuffer();
		StringBuffer sql6 = new StringBuffer();
		StringBuffer sql7 = new StringBuffer();
		StringBuffer sql8 = new StringBuffer();
		StringBuffer sql9 = new StringBuffer(); //最外層的ConditionSql
		boolean hasOpt_Id = StrUtils.isNotEmpty(opt_id) ? true : false;
		Map<String, String> params = new LinkedHashMap<String, String> ();
		Map<String, String> params2 = new LinkedHashMap<String, String> ();
		Map<String, String> params3 = new LinkedHashMap<String, String> ();
		Map<String, String> params4 = new LinkedHashMap<String, String> ();
		Map<String, String> params5 = new LinkedHashMap<String, String> ();
		Map<String, String> params6 = new LinkedHashMap<String, String> ();
		Map<String, String> params7 = new LinkedHashMap<String, String> ();
		Map<String, String> params8 = new LinkedHashMap<String, String> ();
		Map<String, String> params9 = new LinkedHashMap<String, String> ();//最外層的Condition
		Map retmap = new LinkedHashMap();
		List<String> values = new LinkedList<String>(); 
		try {
//			AND a.newresult IN ('A' ,'P')    AND  a.senderstatus != '1'
//			發動行
			params.put("a.CLEARINGPHASE", clearingPhase);
			params.put("a.BIZDATE", txdt);
			params.put("SENDERACQUIRE", opt_id);
			params.put("SENDERID", senderid);
			params.put("TXID", txnid);
			params.put("FEE_TYPE", fee_type);
			params.put("a.newresult", "AP");
			
			params2.put("p.CLEARINGPHASE", clearingPhase);
			params2.put("p.BIZDATE", txdt);
			params2.put("SENDERACQUIRE", opt_id);
			params2.put("SENDERID", senderid);
			params2.put("TXID", txnid);
			params2.put("FEE_TYPE", fee_type);
//			扣賬行
			params3.put("a.CLEARINGPHASE", clearingPhase);
			params3.put("a.BIZDATE", txdt);
			params3.put("OUTACQUIRE", opt_id);
			params3.put("SENDERID", senderid);
			params3.put("TXID", txnid);
			params3.put("FEE_TYPE", fee_type);
			params3.put("a.newresult", "AP");
			
			params4.put("p.CLEARINGPHASE", clearingPhase);
			params4.put("p.BIZDATE", txdt);
			params4.put("OUTACQUIRE", opt_id);
			params4.put("SENDERID", senderid);
			params4.put("TXID", txnid);
			params4.put("FEE_TYPE", fee_type);
			
//			入賬行
			params5.put("a.CLEARINGPHASE", clearingPhase);
			params5.put("a.BIZDATE", txdt);
			params5.put("INACQUIRE", opt_id);
			params5.put("SENDERID", senderid);
			params5.put("TXID", txnid);
			params5.put("FEE_TYPE", fee_type);
			params5.put("a.newresult", "AP");
			
			params6.put("p.CLEARINGPHASE", clearingPhase);
			params6.put("p.BIZDATE", txdt);
			params6.put("INACQUIRE", opt_id);
			params6.put("SENDERID", senderid);
			params6.put("TXID", txnid);
			params6.put("FEE_TYPE", fee_type);
			
//			銷賬行
			params7.put("a.CLEARINGPHASE", clearingPhase);
			params7.put("a.BIZDATE", txdt);
			params7.put("WOACQUIRE", opt_id);
			params7.put("SENDERID", senderid);
			params7.put("TXID", txnid);
			params7.put("FEE_TYPE", fee_type);
			params7.put("a.newresult", "AP");
			
			params8.put("p.CLEARINGPHASE", clearingPhase);
			params8.put("p.BIZDATE", txdt);
			params8.put("WOACQUIRE", opt_id);
			params8.put("SENDERID", senderid);
			params8.put("TXID", txnid);
			params8.put("FEE_TYPE", fee_type);
			
//			最外層
			params9.put("a.TXTYPE", txtype);
			
			int i = 0;
			for(String key :params.keySet()){
				
				if(StrUtils.isNotEmpty(params.get(key))){
					if(i==0){sql.append(" WHERE ");}
					if(i!=0){sql.append(" AND ");}
					if(key.equals("a.FLBIZDATE")){
						sql.append( "coalesce( "+key+" , '')  = '' " );
//						params.put("a.newresult", newresult);
					}else if(key.equals("a.newresult") ){
						if("P".equals(params.get(key))){
							sql.append( " a.newresult IN ('P')    AND  a.senderstatus != '1' ");
						}else{
							sql.append( " a.newresult IN ('A' ,'P')    AND  a.senderstatus != '1' ");
						}
						
					}else if(key.equals("a.BIZDATE")){
						sql.append( key+" = ? ");
						values.add(DateTimeUtils.convertDate(params.get(key), "yyyymmdd", "yyyymmdd"));
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
					if(key.equals("p.FLBIZDATE")){
						sql2.append( "coalesce( "+key+" , '')  <> '' " );
					}else if(key.equals("p.BIZDATE")){
						sql2.append( key+" = ? ");
						values.add(DateTimeUtils.convertDate(params2.get(key), "yyyymmdd", "yyyymmdd"));
					}else{
						sql2.append( key+" = ? ");
						values.add(params2.get(key));
					}
					i++;
				}
			}
			i = 0;
			for(String key :params3.keySet()){
				if(StrUtils.isNotEmpty(params3.get(key))){
					if(i==0){sql3.append(" WHERE ");}
					if(i!=0){sql3.append(" AND ");}
					if(key.equals("a.FLBIZDATE")){
						sql3.append( "coalesce( "+key+" , '')  <> '' " );
					}else if(key.equals("a.newresult") ){
						if("P".equals(params3.get(key))){
							sql3.append( " a.newresult IN ('P')    AND  a.senderstatus != '1' ");
						}else{
							sql3.append( " a.newresult IN ('A' ,'P')    AND  a.senderstatus != '1' ");
						}	
					}else if(key.equals("a.BIZDATE")){
						sql3.append( key+" = ? ");
						values.add(DateTimeUtils.convertDate(params3.get(key), "yyyymmdd", "yyyymmdd"));
					}else{
						sql3.append( key+" = ? ");
						values.add(params3.get(key));
					}
					i++;
				}
			}
			i = 0;
			for(String key :params4.keySet()){
				if(StrUtils.isNotEmpty(params4.get(key))){
					if(i==0){sql4.append(" WHERE ");}
					if(i!=0){sql4.append(" AND ");}
					if(key.equals("p.FLBIZDATE")){
						sql4.append( "coalesce( "+key+" , '')  <> '' " );
					}else if(key.equals("p.BIZDATE")){
						sql4.append( key+" = ? ");
						values.add(DateTimeUtils.convertDate(params4.get(key), "yyyymmdd", "yyyymmdd"));
					}else{
						sql4.append( key+" = ? ");
						values.add(params4.get(key));
					}
					i++;
				}
			}
			i = 0;
			for(String key :params5.keySet()){
				if(StrUtils.isNotEmpty(params5.get(key))){
					if(i==0){sql5.append(" WHERE ");}
					if(i!=0){sql5.append(" AND ");}
					if(key.equals("a.FLBIZDATE")){
						sql5.append( "coalesce( "+key+" , '')  <> '' " );
					}else if(key.equals("a.newresult") ){
						if("P".equals(params5.get(key))){
							sql5.append( " a.newresult IN ('P')    AND  a.senderstatus != '1' ");
						}else{
							sql5.append( " a.newresult IN ('A' ,'P')    AND  a.senderstatus != '1' ");
						}
					}else if(key.equals("a.BIZDATE")){
						sql5.append( key+" = ? ");
						values.add(DateTimeUtils.convertDate(params5.get(key), "yyyymmdd", "yyyymmdd"));
					}else{
						sql5.append( key+" = ? ");
						values.add(params5.get(key));
					}
					i++;
				}
			}
			i = 0;
			for(String key :params6.keySet()){
				if(StrUtils.isNotEmpty(params6.get(key))){
					if(i==0){sql6.append(" WHERE ");}
					if(i!=0){sql6.append(" AND ");}
					if(key.equals("p.FLBIZDATE")){
						sql6.append( "coalesce( "+key+" , '')  <> '' " );
					}else if(key.equals("p.BIZDATE")){
						sql6.append( key+" = ? ");
						values.add(DateTimeUtils.convertDate(params6.get(key), "yyyymmdd", "yyyymmdd"));
					}else{
						sql6.append( key+" = ? ");
						values.add(params6.get(key));
					}
					i++;
				}
			}
			i = 0;
			for(String key :params7.keySet()){
				if(StrUtils.isNotEmpty(params7.get(key))){
					if(i==0){sql7.append(" WHERE ");}
					if(i!=0){sql7.append(" AND ");}
					if(key.equals("a.FLBIZDATE")){
						sql7.append( "coalesce( "+key+" , '')  <> '' " );
					}else if(key.equals("a.newresult") ){
						if("P".equals(params7.get(key))){
							sql7.append( " a.newresult IN ('P')    AND  a.senderstatus != '1' ");
						}else{
							sql7.append( " a.newresult IN ('A' ,'P')    AND  a.senderstatus != '1' ");
						}
					}else if(key.equals("a.BIZDATE")){
						sql7.append( key+" = ? ");
						values.add(DateTimeUtils.convertDate(params7.get(key), "yyyymmdd", "yyyymmdd"));
					}else{
						sql7.append( key+" = ? ");
						values.add(params7.get(key));
					}
					i++;
				}
			}
			i = 0;
			for(String key :params8.keySet()){
				if(StrUtils.isNotEmpty(params8.get(key))){
					if(i==0){sql8.append(" WHERE ");}
					if(i!=0){sql8.append(" AND ");}
					if(key.equals("p.FLBIZDATE")){
						sql8.append( "coalesce( "+key+" , '')  <> '' " );
					}else if(key.equals("p.BIZDATE")){
						sql8.append( key+" = ? ");
						values.add(DateTimeUtils.convertDate(params8.get(key), "yyyymmdd", "yyyymmdd"));
					}else{
						sql8.append( key+" = ? ");
						values.add(params8.get(key));
					}
					i++;
				}
			}
//			最外層
			i = 0;
			for(String key :params9.keySet()){
				if(StrUtils.isNotEmpty(params9.get(key))){
					sql9.append(" AND ");
					sql9.append( key+" = ? ");
					values.add(params9.get(key));
					i++;
				}
			}
			
//			sql.append(" AND a.newresult IN ('A' ,'P')    AND  a.senderstatus != '1' ");
			sql2.append(" AND p.RESULTCODE = '01' ");
			sql4.append(" AND p.RESULTCODE = '01' ");
			sql6.append(" AND p.RESULTCODE = '01' ");
			sql8.append(" AND p.RESULTCODE = '01' ");
			retmap.put("sqlPath", sql.toString());
			retmap.put("sqlPath2", sql2.toString());
			retmap.put("sqlPath3", sql3.toString());
			retmap.put("sqlPath4", sql4.toString());
			retmap.put("sqlPath5", sql5.toString());
			retmap.put("sqlPath6", sql6.toString());
			retmap.put("sqlPath7", sql7.toString());
			retmap.put("sqlPath8", sql8.toString());
			retmap.put("sqlPath9", sql9.toString());
			retmap.put("values", values);
			log.debug("getConditionDataForBat.sqlPath>>"+sql);
			log.debug("getConditionDataForBat.sqlPath2>>"+sql2);
			log.debug("getConditionDataForBat.values>>"+values);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e.toString()) ; 
		}
		return retmap;
	}
	
	
	
	
	public String getSQL(Map<String,String> sqlmap, String orderbySql){
		StringBuffer sql = new StringBuffer();
		sql.append(" WITH TEMP AS  ");
		sql.append(" ( ");
//		發動行
		sql.append(" SELECT row_number() over (order by a.TXDT)  as SEQNO ");
		sql.append(" ,(CASE WHEN coalesce(VARCHAR(a.txDT),'') ='' THEN '' ELSE (CAST (YEAR(a.txDT)-1911 AS CHAR(3)) ||''|| SUBSTR( REPLACE(CAST( DATE(a.txdt) AS CHAR(10)),'-','/'),5,6)  )  END ) TXDT ");
		sql.append(" ,(CASE WHEN coalesce(VARCHAR(a.txDT),'') ='' THEN '' ELSE (SUBSTR(REPLACE(CAST( TIME(a.TXDT) AS CHAR(8)) ,'.',':') ,1,8))  END ) TXT ");
		sql.append(" ,a.pcode || coalesce(b.each_txn_name,'')  pcode   ,a.STAN ,a.txid, coalesce((select  a.txid ||'-'|| t.txn_name from TXN_CODE t where a.txid=t.txn_id),'') txn_name ,a.CLEARINGPHASE ");
		sql.append(" ,coalesce(a.senderId,'') ||'-'|| coalesce(a.company_abbr,'') senderId  ,Decimal(a.txAmt) txAmt  ");
		sql.append(" ,(case  when a.newresult='R' then '失敗' when a.newresult='P' and a.senderstatus != '1' then '未完成' when a.newresult='P' and a.senderstatus = '1' then '失敗' else '成功' end) Resp   ");
//		20201007 edit by hugo 改抓最新欄位
		sql.append(" , coalesce(a.senderfee_nw,0)  FEE  ");
//		sql.append(" ,( CASE WHEN coalesce(a.FEE_TYPE,'') = '' THEN  coalesce(a.senderfee,0)  ELSE  coalesce(a.senderfee_nw,0)  END   ) FEE  ");
		sql.append("  , a.FEE_TYPE  ");
		sql.append(" ,(CAST( coalesce(a.SND_BANK_FEE_DISC_NW , '0.00' ) AS DECIMAL(7,2))) AS UNIT_PRICE , a.FEE_LVL_TYPE  ");
		sql.append(" ,a.SENDERACQUIRE ||'-'||coalesce((select brbk_name from bank_branch where brbk_id=a.SENDERACQUIRE),'') SENDERACQUIRE_NAME  ");
		sql.append(" ,GETBKHEADNAME(a.SENDERHEAD) BANK_NAME  ");
		sql.append(" ,'A' as TXTYPE ,'1' as ROLETYPE ,a.SENDERACQUIRE AS ACQUIRE ,a.SENDERHEAD AS BANK_ID ");
		sql.append(" FROM RPONBLOCKTAB a");
		sql.append(" left join EACH_TXN_CODE b on b.each_txn_id = a.pcode ");
		sql.append(sqlmap.get("sqlPath"));
//		發動行 END
		sql.append(" UNION ALL ");
//		發動行沖正
		sql.append(" SELECT row_number() over (order by p.TXDT)  as SEQNO ");
		sql.append(" ,(CASE WHEN coalesce(VARCHAR(p.TXDT),'') ='' THEN '' ELSE (CAST (YEAR( TRANSLATE('abcd-ef-gh ij:kl:mn', p.TXDT, 'abcdefghijklmn') )-1911 AS CHAR(3)) ||''|| SUBSTR( REPLACE(CAST( DATE( TRANSLATE('abcd-ef-gh ij:kl:mn', p.TXDT, 'abcdefghijklmn') ) AS CHAR(10)),'-','/'),5,6)  )  END ) TXDT ");
		sql.append(" ,(CASE WHEN coalesce(VARCHAR(p.TXDT),'') ='' THEN '' ELSE (SUBSTR(REPLACE(CAST( TIME( TRANSLATE('abcd-ef-gh ij:kl:mn', p.TXDT, 'abcdefghijklmn')  ) AS CHAR(8)) ,'.',':') ,1,8))  END ) TXT  ");
		sql.append(" ,p.pcode || coalesce(etc.each_txn_name,'')  pcode ,p.OSTAN as STAN,p.txid, coalesce((select  p.txid ||'-'|| t.txn_name from TXN_CODE t where p.txid=t.txn_id),'') txn_name ,p.CLEARINGPHASE ");
		sql.append(" ,coalesce(p.senderId,'') ||'-'|| coalesce( GETCOMPANY_ABBR(p.senderId)  ,'') senderId  ,Decimal(p.txAmt) txAmt ");
		sql.append(" ,'成功' as Resp ");
		sql.append(" ,( coalesce(p.senderfee_nw,0) * (-1) )  FEE ");
//		sql.append(" ,( CASE WHEN coalesce(p.FEE_TYPE,'') = '' THEN  coalesce(p.senderfee,0)  ELSE  coalesce(p.senderfee_nw,0)  END   ) FEE ");
		sql.append(" , p.FEE_TYPE ");
		sql.append(" ,(CAST(coalesce(p.SND_BANK_FEE_DISC_NW , '0.00' ) AS DECIMAL(7,2)))  AS UNIT_PRICE , p.FEE_LVL_TYPE ");
		sql.append(" , p.SENDERACQUIRE ||'-'||coalesce((select brbk_name from bank_branch where brbk_id=p.SENDERACQUIRE),'') SENDERACQUIRE_NAME  ");
		sql.append(" , GETBKHEADNAME(p.SENDERHEAD) BANK_NAME   ");
		sql.append(" ,'B' as TXTYPE ,'1' as ROLETYPE,p.SENDERACQUIRE AS ACQUIRE , p.SENDERHEAD AS BANK_ID");
		sql.append(" FROM RPONPENDINGTAB p   ");
		sql.append(" left join EACH_TXN_CODE etc    ");
		sql.append(" on etc.each_txn_id = p.pcode    ");
		sql.append(sqlmap.get("sqlPath2"));
//		sql.append(sqlPath2);
//		發動行沖正 END
		sql.append(" UNION ALL ");
//		扣款行
		sql.append(" SELECT row_number() over (order by a.TXDT)  as SEQNO ");
		sql.append(" ,(CASE WHEN coalesce(VARCHAR(a.txDT),'') ='' THEN '' ELSE (CAST (YEAR(a.txDT)-1911 AS CHAR(3)) ||''|| SUBSTR( REPLACE(CAST( DATE(a.txdt) AS CHAR(10)),'-','/'),5,6)  )  END ) TXDT ");
		sql.append(" ,(CASE WHEN coalesce(VARCHAR(a.txDT),'') ='' THEN '' ELSE (SUBSTR(REPLACE(CAST( TIME(a.TXDT) AS CHAR(8)) ,'.',':') ,1,8))  END ) TXT ");
		sql.append(" ,a.pcode || coalesce(b.each_txn_name,'')  pcode   ,a.STAN ,a.txid, coalesce((select  a.txid ||'-'|| t.txn_name from TXN_CODE t where a.txid=t.txn_id),'') txn_name ,a.CLEARINGPHASE ");
		sql.append(" ,coalesce(a.senderId,'') ||'-'|| coalesce(a.company_abbr,'') senderId  ,Decimal(a.txAmt) txAmt  ");
		sql.append(" ,(case  when a.newresult='R' then '失敗' when a.newresult='P' and a.senderstatus != '1' then '未完成' when a.newresult='P' and a.senderstatus = '1' then '失敗' else '成功' end) Resp   ");
//		20201007 edit by hugo 改抓最新欄位
		sql.append(" ,coalesce(a.outfee_nw,0)  FEE  ");
//		sql.append(" ,( CASE WHEN coalesce(a.FEE_TYPE,'') = '' THEN  coalesce(a.outfee,0)  ELSE  coalesce(a.outfee_nw,0)  END   ) FEE  ");
		sql.append(" , a.FEE_TYPE  ");
		sql.append(" , (CAST( coalesce(a.OUT_BANK_FEE_DISC_NW , '0.00' ) AS DECIMAL(7,2)))  AS UNIT_PRICE , a.FEE_LVL_TYPE  ");
		sql.append(" ,a.OUTACQUIRE ||'-'||coalesce((select brbk_name from bank_branch where brbk_id=a.OUTACQUIRE),'') SENDERACQUIRE_NAME  ");
		sql.append(" ,GETBKHEADNAME(a.OUTHEAD) BANK_NAME  ");
		sql.append(" ,'A' as TXTYPE ,'2' as ROLETYPE ,a.OUTACQUIRE AS ACQUIRE ,a.OUTHEAD AS BANK_ID ");
		sql.append(" FROM RPONBLOCKTAB a");
		sql.append(" left join EACH_TXN_CODE b on b.each_txn_id = a.pcode ");
		sql.append(sqlmap.get("sqlPath3"));
//		扣款行 END
		sql.append(" UNION ALL ");
		
//		扣款行沖正
		sql.append(" SELECT row_number() over (order by p.TXDT)  as SEQNO ");
		sql.append(" ,(CASE WHEN coalesce(VARCHAR(p.TXDT),'') ='' THEN '' ELSE (CAST (YEAR( TRANSLATE('abcd-ef-gh ij:kl:mn', p.TXDT, 'abcdefghijklmn') )-1911 AS CHAR(3)) ||''|| SUBSTR( REPLACE(CAST( DATE( TRANSLATE('abcd-ef-gh ij:kl:mn', p.TXDT, 'abcdefghijklmn') ) AS CHAR(10)),'-','/'),5,6)  )  END ) TXDT ");
		sql.append(" ,(CASE WHEN coalesce(VARCHAR(p.TXDT),'') ='' THEN '' ELSE (SUBSTR(REPLACE(CAST( TIME( TRANSLATE('abcd-ef-gh ij:kl:mn', p.TXDT, 'abcdefghijklmn')  ) AS CHAR(8)) ,'.',':') ,1,8))  END ) TXT  ");
		sql.append(" ,p.pcode || coalesce(etc.each_txn_name,'')  pcode ,p.OSTAN as STAN,p.txid, coalesce((select  p.txid ||'-'|| t.txn_name from TXN_CODE t where p.txid=t.txn_id),'') txn_name ,p.CLEARINGPHASE ");
		sql.append(" ,coalesce(p.senderId,'') ||'-'|| coalesce( GETCOMPANY_ABBR(p.senderId)  ,'') senderId  ,Decimal(p.txAmt) txAmt ");
		sql.append(" ,'成功' as Resp ");
		sql.append(" ,( coalesce(p.outfee_nw,0) * (-1) )  FEE ");
//		sql.append(" ,( CASE WHEN coalesce(p.FEE_TYPE,'') = '' THEN  coalesce(p.outfee,0)  ELSE  coalesce(p.outfee_nw,0)  END   ) FEE ");
		sql.append("  , p.FEE_TYPE ");
		sql.append(" ,(CAST( coalesce(p.OUT_BANK_FEE_DISC_NW , '0.00' ) AS DECIMAL(7,2))) AS UNIT_PRICE , p.FEE_LVL_TYPE ");
		sql.append(" , p.OUTACQUIRE ||'-'||coalesce((select brbk_name from bank_branch where brbk_id=p.OUTACQUIRE),'') SENDERACQUIRE_NAME  ");
		sql.append(" , GETBKHEADNAME(p.OUTHEAD) BANK_NAME  ");
		sql.append(" ,'B' as TXTYPE ,'2' as ROLETYPE,p.OUTACQUIRE AS ACQUIRE ,p.OUTHEAD AS BANK_ID ");
		sql.append(" FROM RPONPENDINGTAB p   ");
		sql.append(" left join EACH_TXN_CODE etc    ");
		sql.append(" on etc.each_txn_id = p.pcode    ");
		sql.append(sqlmap.get("sqlPath4"));
//		扣款行沖正 END
		sql.append(" UNION ALL ");
//		入賬行
		sql.append(" SELECT row_number() over (order by a.TXDT)  as SEQNO ");
		sql.append(" ,(CASE WHEN coalesce(VARCHAR(a.txDT),'') ='' THEN '' ELSE (CAST (YEAR(a.txDT)-1911 AS CHAR(3)) ||''|| SUBSTR( REPLACE(CAST( DATE(a.txdt) AS CHAR(10)),'-','/'),5,6)  )  END ) TXDT ");
		sql.append(" ,(CASE WHEN coalesce(VARCHAR(a.txDT),'') ='' THEN '' ELSE (SUBSTR(REPLACE(CAST( TIME(a.TXDT) AS CHAR(8)) ,'.',':') ,1,8))  END ) TXT ");
		sql.append(" ,a.pcode || coalesce(b.each_txn_name,'')  pcode   ,a.STAN ,a.txid, coalesce((select  a.txid ||'-'|| t.txn_name from TXN_CODE t where a.txid=t.txn_id),'') txn_name ,a.CLEARINGPHASE ");
		sql.append(" ,coalesce(a.senderId,'') ||'-'|| coalesce(a.company_abbr,'') senderId  ,Decimal(a.txAmt) txAmt  ");
		sql.append(" ,(case  when a.newresult='R' then '失敗' when a.newresult='P' and a.senderstatus != '1' then '未完成' when a.newresult='P' and a.senderstatus = '1' then '失敗' else '成功' end) Resp   ");
//		20201007 edit by hugo 改抓最新欄位
		sql.append(" ,coalesce(a.infee_nw,0) FEE  ");
//		sql.append(" ,( CASE WHEN coalesce(a.FEE_TYPE,'') = '' THEN  coalesce(a.infee,0)  ELSE  coalesce(a.infee_nw,0)  END   ) FEE  ");
		sql.append(" , a.FEE_TYPE  ");
		sql.append(" , (CAST(coalesce(a.IN_BANK_FEE_DISC_NW , '0.00' ) AS DECIMAL(7,2)))  AS UNIT_PRICE , a.FEE_LVL_TYPE  ");
		sql.append(" ,a.INACQUIRE ||'-'||coalesce((select brbk_name from bank_branch where brbk_id=a.INACQUIRE),'') SENDERACQUIRE_NAME  ");
		sql.append(" , GETBKHEADNAME(a.INHEAD) BANK_NAME  ");
		sql.append(" ,'A' as TXTYPE ,'3' as ROLETYPE ,a.INACQUIRE AS ACQUIRE ,a.INHEAD AS BANK_ID");
		sql.append(" FROM RPONBLOCKTAB a");
		sql.append(" left join EACH_TXN_CODE b on b.each_txn_id = a.pcode ");
		sql.append(sqlmap.get("sqlPath5"));
//		入賬行 END
		sql.append(" UNION ALL ");
//		入賬行 沖正
		sql.append(" SELECT row_number() over (order by p.TXDT)  as SEQNO ");
		sql.append(" ,(CASE WHEN coalesce(VARCHAR(p.TXDT),'') ='' THEN '' ELSE (CAST (YEAR( TRANSLATE('abcd-ef-gh ij:kl:mn', p.TXDT, 'abcdefghijklmn') )-1911 AS CHAR(3)) ||''|| SUBSTR( REPLACE(CAST( DATE( TRANSLATE('abcd-ef-gh ij:kl:mn', p.TXDT, 'abcdefghijklmn') ) AS CHAR(10)),'-','/'),5,6)  )  END ) TXDT ");
		sql.append(" ,(CASE WHEN coalesce(VARCHAR(p.TXDT),'') ='' THEN '' ELSE (SUBSTR(REPLACE(CAST( TIME( TRANSLATE('abcd-ef-gh ij:kl:mn', p.TXDT, 'abcdefghijklmn')  ) AS CHAR(8)) ,'.',':') ,1,8))  END ) TXT  ");
		sql.append(" ,p.pcode || coalesce(etc.each_txn_name,'')  pcode ,p.OSTAN as STAN,p.txid, coalesce((select  p.txid ||'-'|| t.txn_name from TXN_CODE t where p.txid=t.txn_id),'') txn_name ,p.CLEARINGPHASE ");
		sql.append(" ,coalesce(p.senderId,'') ||'-'|| coalesce( GETCOMPANY_ABBR(p.senderId)  ,'') senderId  ,Decimal(p.txAmt) txAmt ");
		sql.append(" ,'成功' as Resp ");
		sql.append(" ,( coalesce(p.infee_nw,0) * (-1) )  FEE ");
//		sql.append(" ,( CASE WHEN coalesce(p.FEE_TYPE,'') = '' THEN  coalesce(p.infee,0)  ELSE  coalesce(p.infee_nw,0)  END   ) FEE ");
		sql.append(" , p.FEE_TYPE ");
		sql.append(" ,(CAST(coalesce(p.IN_BANK_FEE_DISC_NW , '0.00' ) AS DECIMAL(7,2)))  AS UNIT_PRICE , p.FEE_LVL_TYPE ");
		sql.append(" , p.INACQUIRE ||'-'||coalesce((select brbk_name from bank_branch where brbk_id=p.INACQUIRE),'') SENDERACQUIRE_NAME  ");
		sql.append(" , GETBKHEADNAME(p.INHEAD) BANK_NAME  ");
		sql.append(" ,'B' as TXTYPE ,'3' as ROLETYPE,p.INACQUIRE AS ACQUIRE ,p.INHEAD AS BANK_ID ");
		sql.append(" FROM RPONPENDINGTAB p   ");
		sql.append(" left join EACH_TXN_CODE etc    ");
		sql.append(" on etc.each_txn_id = p.pcode    ");
		sql.append(sqlmap.get("sqlPath6"));
//		入賬行沖正 END
		sql.append(" UNION ALL ");
		
//		銷賬行
		sql.append(" SELECT row_number() over (order by a.TXDT)  as SEQNO ");
		sql.append(" ,(CASE WHEN coalesce(VARCHAR(a.txDT),'') ='' THEN '' ELSE (CAST (YEAR(a.txDT)-1911 AS CHAR(3)) ||''|| SUBSTR( REPLACE(CAST( DATE(a.txdt) AS CHAR(10)),'-','/'),5,6)  )  END ) TXDT ");
		sql.append(" ,(CASE WHEN coalesce(VARCHAR(a.txDT),'') ='' THEN '' ELSE (SUBSTR(REPLACE(CAST( TIME(a.TXDT) AS CHAR(8)) ,'.',':') ,1,8))  END ) TXT ");
		sql.append(" ,a.pcode || coalesce(b.each_txn_name,'')  pcode   ,a.STAN ,a.txid, coalesce((select  a.txid ||'-'|| t.txn_name from TXN_CODE t where a.txid=t.txn_id),'') txn_name ,a.CLEARINGPHASE ");
		sql.append(" ,coalesce(a.senderId,'') ||'-'|| coalesce(a.company_abbr,'') senderId  ,Decimal(a.txAmt) txAmt  ");
		sql.append(" ,(case  when a.newresult='R' then '失敗' when a.newresult='P' and a.senderstatus != '1' then '未完成' when a.newresult='P' and a.senderstatus = '1' then '失敗' else '成功' end) Resp   ");
//		20201007 edit by hugo 改抓最新欄位
		sql.append(" ,coalesce(a.wofee_nw,0)  FEE  ");
//		sql.append(" ,( CASE WHEN coalesce(a.FEE_TYPE,'') = '' THEN  coalesce(a.wofee,0)  ELSE  coalesce(a.wofee_nw,0)  END   ) FEE  ");
		sql.append(" , a.FEE_TYPE  ");
		sql.append(" , (CAST(coalesce(a.WO_BANK_FEE_DISC_NW , '0.00' ) AS DECIMAL(7,2)))  AS UNIT_PRICE , a.FEE_LVL_TYPE  ");
		sql.append(" ,a.WOACQUIRE ||'-'||coalesce((select brbk_name from bank_branch where brbk_id=a.WOACQUIRE),'') SENDERACQUIRE_NAME  ");
		sql.append(" , GETBKHEADNAME(a.WOHEAD) BANK_NAME  ");
		sql.append(" ,'A' as TXTYPE ,'4' as ROLETYPE ,a.WOACQUIRE AS ACQUIRE ,a.WOHEAD AS BANK_ID");
		sql.append(" FROM RPONBLOCKTAB a");
		sql.append(" left join EACH_TXN_CODE b on b.each_txn_id = a.pcode ");
		sql.append(sqlmap.get("sqlPath7"));
//		銷賬行 END
		sql.append(" UNION ALL "); 
//		銷賬行沖正
		sql.append(" SELECT row_number() over (order by p.TXDT)  as SEQNO ");
		sql.append(" ,(CASE WHEN coalesce(VARCHAR(p.TXDT),'') ='' THEN '' ELSE (CAST (YEAR( TRANSLATE('abcd-ef-gh ij:kl:mn', p.TXDT, 'abcdefghijklmn') )-1911 AS CHAR(3)) ||''|| SUBSTR( REPLACE(CAST( DATE( TRANSLATE('abcd-ef-gh ij:kl:mn', p.TXDT, 'abcdefghijklmn') ) AS CHAR(10)),'-','/'),5,6)  )  END ) TXDT ");
		sql.append(" ,(CASE WHEN coalesce(VARCHAR(p.TXDT),'') ='' THEN '' ELSE (SUBSTR(REPLACE(CAST( TIME( TRANSLATE('abcd-ef-gh ij:kl:mn', p.TXDT, 'abcdefghijklmn')  ) AS CHAR(8)) ,'.',':') ,1,8))  END ) TXT  ");
		sql.append(" ,p.pcode || coalesce(etc.each_txn_name,'')  pcode ,p.OSTAN as STAN,p.txid, coalesce((select  p.txid ||'-'|| t.txn_name from TXN_CODE t where p.txid=t.txn_id),'') txn_name ,p.CLEARINGPHASE ");
		sql.append(" ,coalesce(p.senderId,'') ||'-'|| coalesce( GETCOMPANY_ABBR(p.senderId)  ,'') senderId  ,Decimal(p.txAmt) txAmt ");
		sql.append(" ,'成功' as Resp ");
		sql.append(" , ( coalesce(p.wofee_nw,0) * (-1) )  FEE ");
//		sql.append(" ,( CASE WHEN coalesce(p.FEE_TYPE,'') = '' THEN  coalesce(p.wofee,0)  ELSE  coalesce(p.wofee_nw,0)  END   ) FEE ");
		sql.append(" , p.FEE_TYPE ");
		sql.append(" , (CAST(coalesce(p.WO_BANK_FEE_DISC_NW , '0.00' ) AS DECIMAL(7,2))) AS UNIT_PRICE , p.FEE_LVL_TYPE ");
		sql.append(" , p.WOACQUIRE ||'-'||coalesce((select brbk_name from bank_branch where brbk_id=p.WOACQUIRE),'') SENDERACQUIRE_NAME  ");
		sql.append(" , GETBKHEADNAME(p.WOHEAD) BANK_NAME  ");
		sql.append(" ,'B' as TXTYPE ,'4' as ROLETYPE,p.WOACQUIRE AS ACQUIRE , p.WOHEAD AS BANK_ID ");
		sql.append(" FROM RPONPENDINGTAB p   ");
		sql.append(" left join EACH_TXN_CODE etc    ");
		sql.append(" on etc.each_txn_id = p.pcode    ");
		sql.append(sqlmap.get("sqlPath8"));
//		銷賬行沖正 END
		
		sql.append(" ) ");
		sql.append(" SELECT SEQNO,TXDT,TXT,PCODE,STAN,TXID,TXN_NAME,CLEARINGPHASE,SENDERID,TXAMT,RESP,FEE ");
		sql.append("  ,FEE_TYPE ,UNIT_PRICE ,FEE_LVL_TYPE,SENDERACQUIRE_NAME,TXTYPE,ROLETYPE , ACQUIRE , ACQUIRE||'-'||ROLETYPE AS SENDERROLETYPE ,BANK_ID ,BANK_NAME, BANK_ID||'-'|| ROLETYPE AS BANKROLETYPE ");
		sql.append("  FROM EACHUSER.TEMP a  ");
		sql.append("  WHERE a.ACQUIRE IS NOT NULL AND coalesce(BANK_ID,'') <> '' ");
		sql.append(sqlmap.get("sqlPath9"));
		sql.append(orderbySql);
		log.debug("getSQL>>"+sql);
		return sql.toString();
	}
	
	
	
	
	public List<LabelValueBean> getBgbkIdList(){
		List<BANK_GROUP> list = bank_group_Dao.getBgbkIdList_2();
		List<LabelValueBean> beanList = new LinkedList<LabelValueBean>();
		LabelValueBean bean = null;
		for(BANK_GROUP po : list){
			bean = new LabelValueBean(po.getBGBK_ID() + " - " + po.getBGBK_NAME(), po.getBGBK_ID());
			beanList.add(bean);
		}
		log.debug("beanList>>"+beanList);
		return beanList;
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
