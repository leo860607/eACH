package tw.org.twntch.bo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.struts.util.LabelValueBean;

import tw.org.twntch.db.dao.hibernate.BANK_GROUP_Dao;
import tw.org.twntch.db.dao.hibernate.RPONBLOCKTAB_Dao;
import tw.org.twntch.po.BANK_GROUP;
import tw.org.twntch.util.CodeUtils;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.RptUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.zDateHandler;

public class RPTTX_1_BO {

	private RPONBLOCKTAB_Dao rponblocktab_Dao;
	private BANK_GROUP_Dao bank_group_Dao;
	public Map<String, String> export(String txdt, String opt_id,String bgbk_id ,  String clearingPhase ,  String opt_bank ,String opt_type,String senderid,String txnid,String resp,String serchStrs,Integer downloadtype){
		Map<String, String> rtnMap = null;
		String outputFilePath = "";
		String filename = "tx_1";
		try{
			rtnMap = new HashMap<String, String>();
			rtnMap.put("serchStrs", serchStrs);
			Map<String, Object> params = new HashMap<String, Object>();
//			SQL查詢條件 區塊
			params.put("TXDT","'%"+ DateTimeUtils.convertDate(txdt, "yyyymmdd", "yyyymmdd")+"%' ");
			params.put("opt_id", opt_id);
			params.put("CLEARINGPHASE", clearingPhase);
			params.put("SENDERID", senderid);
			params.put("TXN_ID", txnid);
			params.put("RESP", resp);
//			顯示區塊
			params.put("V_TXDT",DateTimeUtils.convertDate(DateTimeUtils.convertDate(txdt,"yyyyMMdd","yyyyMMdd"),"yyyyMMdd","營業日期: yyy/MM/dd"));
			System.out.println("opt_id>>"+opt_id);
			System.out.println("opt_bank>>"+opt_bank);
			if(StrUtils.isEmpty(opt_id)){
				params.put("V_OPT_BANK", "全部");
			}else{
				params.put("V_OPT_BANK", opt_bank);
			}
			params.put("V_CLEARINGPHASE",clearingPhase);
			params.put("V_PRINT_DATE",DateTimeUtils.convertDate(DateTimeUtils.INTERCONVERSION, zDateHandler.getTheDate(), "yyyy/MM/dd", "yyy/MM/dd"));
			params.put("V_PRINT_TIME",zDateHandler.getTheTime());
			params.put("V_OP_TYPE",getOpType_Name(Integer.valueOf(opt_type)));
			System.out.println("params >> " + params);
			Map map = null;
//			Map map = this.getConditionDataForBat(txdt, opt_id , bgbk_id, clearingPhase , Integer.valueOf(opt_type));
//			String sql = getSQLForBat(map.get("sqlPath").toString(),map.get("sqlPath2").toString() ,  getSPSql(Integer.valueOf(opt_type))  , getspSqlII(Integer.valueOf(opt_type)) , getOrderBySql(Integer.valueOf(opt_type)));
			String sql = "";
			if(3== Integer.valueOf(opt_type)){
				map = this.getConditionDataForWO(txdt, opt_id , bgbk_id, clearingPhase , Integer.valueOf(opt_type) , senderid , txnid , resp );
				sql = getSQLForWO(map.get("sqlPath").toString(),map.get("sqlPath2").toString() ,  getSPSql(Integer.valueOf(opt_type))  , getspSqlII(Integer.valueOf(opt_type)) , getOrderBySql(Integer.valueOf(opt_type)));
				filename = "tx_6";
			}else{
				map = this.getConditionDataForBat(txdt, opt_id , bgbk_id, clearingPhase , Integer.valueOf(opt_type) , senderid , txnid , resp );
				sql = getSQLForBat(map.get("sqlPath").toString(),map.get("sqlPath2").toString() ,  getSPSql(Integer.valueOf(opt_type))  , getspSqlII(Integer.valueOf(opt_type)) , getOrderBySql(Integer.valueOf(opt_type)));
			}
			List datalist = rponblocktab_Dao.getRptData(sql, (List<String>) map.get("values"));
			List list = new ArrayList();
			for(Object each:datalist) {
				Map eachMap = CodeUtils.objectCovert(Map.class, each);
				switch (null==(String)eachMap.get("FEE_TYPE")?"":(String)eachMap.get("FEE_TYPE")){
				case "":
					eachMap.put("FEE_TYPE", "");
					break;
				case "A":
					eachMap.put("FEE_TYPE", "固定");
					break;
				case "B":
					eachMap.put("FEE_TYPE", "外加");
					break;
				case "C":
					eachMap.put("FEE_TYPE", "百分比");
					break;
				case "D":
					eachMap.put("FEE_TYPE", "級距");
					break;
				}
				list.add(eachMap);
			}
			for(Object eachdata:list) {
				System.out.println("EACH DATA >> " + eachdata);
			}
			int pathType = Arguments.getStringArg("ISWINOPEN").equals("Y")?RptUtils.C_PATH :RptUtils.R_PATH;
			System.out.println("@@@@@ downloadtype :"+downloadtype);
			switch (downloadtype) {
			case 1:
				outputFilePath = RptUtils.export(RptUtils.COLLECTION,pathType, filename, filename, params, list);
				break;
			case 2:
				outputFilePath = RptUtils.export(RptUtils.COLLECTION ,pathType,"ex_"+filename , filename, params, list,2);
				break;
			}
			
			
			System.out.println("RPTTX_1_BO.sql>>"+sql);
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
	
	
	public String getOrderBySql(int opt_type){
		StringBuffer sql = new StringBuffer();
		sql.append(" order by  a.CLEARINGPHASE ");
		sql.append(" , BANKID ");
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
//		20150528 edit by hugo req by UAT-20150505-01
//		sql.append(" , a.PCODE ");
		sql.append(" , a.PCODE ,a.TXDT,a.STAN ");
		System.out.println("OrderBySql>>"+sql);
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
			sql.append(" , (case when a.newresult ='A' OR (a.newresult='P' and a.senderstatus != '1')  then  coalesce(a.senderfee_nw,0) else 0 end) fee");
			break;
		case 1:
			sql.append(" ,coalesce(a.OUTHEAD,'') BANKID , coalesce((select brbk_name from bank_branch where brbk_id=a.OUTHEAD),'') BANK_NAME ");
//			sql.append(" ,(case when a.newresult !='R' then  coalesce(a.outfee,0) else 0 end) fee ");
//			20150527 edit by hugo 因SP使用的ISNUMERICII 所回傳的 senderfee、outfee、infee 只有判斷A(成功)或P但ONPENDING.RESULTCODE !='01' 才會有手續費
//			但a.newresult='P' and a.senderstatus = '1' 的交易不會出現在ONPENDING中 ，會被視為"00"，會出現手續費 故自行加入判斷
//			sql.append(" ,coalesce(a.outfee,0)  fee ");
			sql.append(" , (case when a.newresult ='A' OR (a.newresult='P' and a.senderstatus != '1')  then  coalesce(a.outfee_nw,0) else 0 end) fee");
			break;
		case 2:
			sql.append(" ,coalesce(a.INHEAD,'') BANKID , coalesce((select brbk_name from bank_branch where brbk_id=a.INHEAD),'') BANK_NAME ");
//			sql.append(" ,(case when a.newresult !='R' then  coalesce(a.infee,0) else 0 end) fee ");
//			20150527 edit by hugo 因SP使用的ISNUMERICII 所回傳的 senderfee、outfee、infee 只有判斷A(成功)或P但ONPENDING.RESULTCODE !='01' 才會有手續費
//			但a.newresult='P' and a.senderstatus = '1' 的交易不會出現在ONPENDING中 ，會被視為"00"，會出現手續費 故自行加入判斷
//			sql.append(" ,coalesce(a.infee,0)  fee ");
			sql.append(" , (case when a.newresult ='A' OR (a.newresult='P' and a.senderstatus != '1')  then  coalesce(a.infee_nw,0) else 0 end) fee");
			break;
		case 3:
			sql.append(" ,coalesce(a.WOHEAD,'') BANKID , coalesce((select brbk_name from bank_branch where brbk_id=a.WOHEAD),'') BANK_NAME ");
//			sql.append(" ,(case when a.newresult !='R' then  coalesce(a.infee,0) else 0 end) fee ");
//			20150527 edit by hugo 因SP使用的ISNUMERICII 所回傳的 senderfee、outfee、infee 只有判斷A(成功)或P但ONPENDING.RESULTCODE !='01' 才會有手續費
//			但a.newresult='P' and a.senderstatus = '1' 的交易不會出現在ONPENDING中 ，會被視為"00"，會出現手續費 故自行加入判斷
//			sql.append(" ,coalesce(a.infee,0)  fee ");
			sql.append(" , (case when a.newresult ='A' OR (a.newresult='P' and a.senderstatus != '1')  then  coalesce(a.wofee_nw,0) else 0 end) fee");
			break;
		default:
			sql.append(" ,coalesce(a.SENDERHEAD,'') BANKID , coalesce((select brbk_name from bank_branch where brbk_id=a.SENDERHEAD),'') BANK_NAME ");
//			sql.append(" ,(case when a.newresult !='R' then  coalesce(a.senderfee,0) else 0 end) fee ");
			sql.append(" ,coalesce(a.senderfee_nw,0)  fee ");
			break;
		}
		System.out.println("SPSql>>"+sql);
		return sql.toString();
	}
	
	public Map getConditionDataForBat(String txdt, String opt_id,String bgbk_id , String clearingPhase , int opt_type , String senderid ,String txnid , String resp ) throws Exception{
		StringBuffer sql = new StringBuffer();
		StringBuffer sql2 = new StringBuffer();
		boolean hasOpt_Id = StrUtils.isNotEmpty(opt_id) ? true : false;
		Map<String, String> params = new LinkedHashMap<String, String> ();
		Map<String, String> params2 = new LinkedHashMap<String, String> ();
		Map retmap = new LinkedHashMap();
		List<String> values = new LinkedList<String>(); 
		try {
			params.put("a.CLEARINGPHASE", clearingPhase);
			params.put("a.BIZDATE", txdt);
			params2.putAll(params);
			switch (opt_type) {
			case 0:
				params.put("a.FLBIZDATE", "''");
				params.put("a.SENDERACQUIRE", opt_id);
				params.put("a.SENDERHEAD", bgbk_id);
				params.put("a.senderId", senderid);
				params.put("a.txid", txnid);
				params.put("a.newresult", resp);
				
				params2.put("a.FLBIZDATE", "''");
				params2.put("a.SENDERACQUIRE", opt_id);
				params2.put("a.SENDERHEAD", bgbk_id);
				params2.put("a.senderId", senderid);
				params2.put("a.txid", txnid);
				params2.put("a.newresult", resp);
				break;
			case 1:
				params.put("a.FLBIZDATE", "''");
				params.put("a.OUTACQUIRE", opt_id);
				params.put("a.OUTHEAD", bgbk_id);
				params.put("a.senderId", senderid);
				params.put("a.txid", txnid);
				params.put("a.newresult", resp);
				if(hasOpt_Id){
					params2.putAll(params);
				}
				params2.put("a.FLBIZDATE", "''");
				params2.put(" substr(COALESCE(PCODE,''),4)  ", "1");
				params2.put("a.senderId", senderid);
				params2.put("a.txid", txnid);
				params2.put("a.newresult", resp);
				break;
			case 2:
				params.put("a.FLBIZDATE", "''");
				params.put("a.INACQUIRE", opt_id);
				params.put("a.INHEAD", bgbk_id);
				params.put("a.senderId", senderid);
				params.put("a.txid", txnid);
				params.put("a.newresult", resp);
				if(hasOpt_Id){
					params2.putAll(params);
				}
				params2.put("a.FLBIZDATE", "''");
				params2.put(" substr(COALESCE(PCODE,''),4)  ", "2");
				params2.put("a.senderId", senderid);
				params2.put("a.txid", txnid);
				params2.put("a.newresult", resp);
				break;
			case 3:
				params.put("a.FLBIZDATE", "''");
				params.put("a.WOACQUIRE", opt_id);
				params.put("a.WOHEAD", bgbk_id);
				params.put("a.senderId", senderid);
				params.put("a.txid", txnid);
				params.put("a.newresult", resp);
				if(hasOpt_Id){
					params2.putAll(params);
				}
//				銷帳行目前無此邏輯
//				params2.put("a.FLBIZDATE", "''");
//				params2.put(" substr(COALESCE(PCODE,''),4)  ", "2");
				params2.put("a.senderId", senderid);
				params2.put("a.txid", txnid);
				params2.put("a.newresult", resp);
				break;
			default:
				params.put("a.SENDERACQUIRE", opt_id);
				params.put("a.SENDERHEAD", bgbk_id);
				params.put("a.senderId", senderid);
				params.put("a.txid", txnid);
				params.put("a.newresult", resp);
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
					}else if(key.equals("a.newresult")) {
						if("R".equals(params.get(key))) {
							sql.append(" ( a.newresult='R' or a.newresult='P' and a.senderstatus = '1') ");
						}else {
							sql.append(" (a.newresult = ? AND a.senderstatus <> '1') ");
							values.add(params.get(key));
						}
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
						values.add(DateTimeUtils.convertDate(params.get(key), "yyyymmdd", "yyyymmdd"));
					}else if(key.equals("a.SENDERACQUIRE")){
						sql2.append("("+ key+" = ? ");
						sql2.append( " AND substr(COALESCE(PCODE,''),4) NOT IN ( '1' ,'2' ,'3' , '4')) " );
						values.add(params2.get(key));
					}else if(key.equals("a.newresult")) {
						if("R".equals(params2.get(key))) {
							sql2.append(" ( a.newresult='R' or a.newresult='P' and a.senderstatus = '1') ");
						}else {
							sql2.append(" (a.newresult = ? AND a.senderstatus <> '1') ");
							values.add(params2.get(key));
						}
					}else{
						sql2.append( key+" = ? ");
						values.add(params2.get(key));
					}
					i++;
				}
			}
			retmap.put("sqlPath", sql.toString());
			retmap.put("sqlPath2", sql2.toString());
			retmap.put("values", values);
			System.out.println("getConditionDataForBat.sqlPath>>"+sql);
			System.out.println("getConditionDataForBat.sqlPath2>>"+sql2);
			System.out.println("getConditionDataForBat.values>>"+values);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e.toString()) ; 
		}
		return retmap;
	}
	
	
	public Map getConditionDataForWO(String txdt, String opt_id,String bgbk_id , String clearingPhase ,int opt_type , String senderid ,String txnid , String resp ) throws Exception{
		StringBuffer sql = new StringBuffer();
		StringBuffer sql2 = new StringBuffer();
		boolean hasOpt_Id = StrUtils.isNotEmpty(opt_id) ? true : false;
		Map<String, String> params = new LinkedHashMap<String, String> ();
		Map<String, String> params2 = new LinkedHashMap<String, String> ();
		Map retmap = new LinkedHashMap();
		List<String> values = new LinkedList<String>(); 
		try {
			params.put("a.CLEARINGPHASE", clearingPhase);
			params.put("a.BIZDATE", txdt);
			params2.putAll(params);
			switch (opt_type) {
			case 0:
				params.put("a.FLBIZDATE", "''");
				params.put("a.SENDERACQUIRE", opt_id);
				params.put("a.SENDERHEAD", bgbk_id);
				params.put("a.senderId", senderid);
				params.put("a.txid", txnid);
				params.put("a.newresult", resp);
				
				params2.put("a.FLBIZDATE", "''");
				params2.put("a.SENDERACQUIRE", opt_id);
				params2.put("a.SENDERHEAD", bgbk_id);
				params2.put("a.senderId", senderid);
				params2.put("a.txid", txnid);
				params2.put("a.newresult", resp);
				
				break;
			case 1:
				params.put("a.FLBIZDATE", "''");
				params.put("a.OUTACQUIRE", opt_id);
				params.put("a.OUTHEAD", bgbk_id);
				params.put("a.senderId", senderid);
				params.put("a.txid", txnid);
				params.put("a.newresult", resp);
				if(hasOpt_Id){
					params2.putAll(params);
				}
				params2.put("a.FLBIZDATE", "''");
				params2.put(" substr(COALESCE(PCODE,''),4)  ", "1");
				params2.put("a.senderId", senderid);
				params2.put("a.txid", txnid);
				params2.put("a.newresult", resp);
				break;
			case 2:
				params.put("a.FLBIZDATE", "''");
				params.put("a.INACQUIRE", opt_id);
				params.put("a.INHEAD", bgbk_id);
				params.put("a.senderId", senderid);
				params.put("a.txid", txnid);
				params.put("a.newresult", resp);
				if(hasOpt_Id){
					params2.putAll(params);
				}
				params2.put("a.FLBIZDATE", "''");
				params2.put(" substr(COALESCE(PCODE,''),4)  ", "2");
				params2.put("a.senderId", senderid);
				params2.put("a.txid", txnid);
				params2.put("a.newresult", resp);
				break;
			case 3:
				params.put("a.FLBIZDATE", "''");
				params.put("a.WOACQUIRE", opt_id);
				params.put("a.WOHEAD", bgbk_id);
				params.put("a.TOLLID", senderid);
				params.put("a.txid", txnid);
				params.put("a.newresult", resp);
				if(hasOpt_Id){
					params2.putAll(params);
				}
//				銷帳行目前無此邏輯
//				params2.put("a.FLBIZDATE", "''");
//				params2.put(" substr(COALESCE(PCODE,''),4)  ", "2");
				params2.put("a.senderId", senderid);
				params2.put("a.txid", txnid);
				params2.put("a.newresult", resp);
				break;
			default:
				params.put("a.SENDERACQUIRE", opt_id);
				params.put("a.SENDERHEAD", bgbk_id);
				break;
			}
			int i = 0;
			for(String key :params.keySet()){
				
				if(StrUtils.isNotEmpty(params.get(key))){
					if(i==0){sql.append(" WHERE a.PCODE LIKE '27%' ");}
//					if(i!=0){sql.append(" AND ");}
					sql.append(" AND ");
					if(key.equals("a.FLBIZDATE")){
						sql.append( "coalesce( "+key+" , '')  = '' " );
					}else if(key.equals("a.BIZDATE")){
						sql.append( key+" = ? ");
						values.add(DateTimeUtils.convertDate(params.get(key), "yyyymmdd", "yyyymmdd"));
					}else if(key.equals("a.newresult")) {
						if("R".equals(params.get(key))) {
							sql.append(" ( a.newresult='R' or a.newresult='P' and a.senderstatus = '1') ");
						}else {
							sql.append(" (a.newresult = ? AND a.senderstatus <> '1') ");
							values.add(params.get(key));
						}
					}
					else{
						sql.append( key+" = ? ");
						values.add(params.get(key));
					}
					i++;
				}
				
			}
			
			if(3!= opt_type){
				
				i = 0;
				for(String key :params2.keySet()){
					if(StrUtils.isNotEmpty(params2.get(key))){
						if(i==0){sql2.append(" WHERE ");}
						if(i!=0){sql2.append(" AND ");}
						if(key.equals("a.FLBIZDATE")){
							sql2.append( "coalesce( "+key+" , '')  <> '' " );
						}else if(key.equals("a.BIZDATE")){
							sql2.append( key+" = ? ");
							values.add(DateTimeUtils.convertDate(params.get(key), "yyyymmdd", "yyyymmdd"));
						}else if(key.equals("a.SENDERACQUIRE")){
							sql2.append("("+ key+" = ? ");
							sql2.append( " AND substr(COALESCE(PCODE,''),4) NOT IN ( '1' ,'2' ,'3' , '4')) " );
							values.add(params2.get(key));
						}else{
							sql2.append( key+" = ? ");
							values.add(params2.get(key));
						}
						i++;
					}
				}// for end
			}//if end
			retmap.put("sqlPath", sql.toString());
			retmap.put("sqlPath2", sql2.toString());
			retmap.put("values", values);
			System.out.println("getConditionDataForBat.sqlPath>>"+sql);
			System.out.println("getConditionDataForBat.sqlPath2>>"+sql2);
			System.out.println("getConditionDataForBat.values>>"+values);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e.toString()) ; 
		}
		return retmap;
	}
	
	public Map getConditionData(String txdt, String opt_id,String bgbk_id , String clearingPhase ,int opt_type) throws Exception{
		StringBuffer sql = new StringBuffer();
		Map<String, String> params = new LinkedHashMap<String, String> ();
		Map retmap = new LinkedHashMap();
		List<String> values = new LinkedList<String>(); 
		try {
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
						sql.append( key+" LIKE ? " );
						values.add("%"+ DateTimeUtils.convertDate(params.get(key), "yyyymmdd", "yyyymmdd")+"%");
						System.out.println("getConditionSQL_Path.TXDT.sql"+sql);
					}else{
						sql.append( key+" = ? ");
						values.add(params.get(key));
					}
					i++;
				}
			}
			retmap.put("sqlPath", sql.toString());
			retmap.put("values", values);
			System.out.println("getConditionDataSQL_Path>>"+sql);
			System.out.println("getConditionSQL_Path>>"+sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e.toString()) ; 
		}
		return retmap;
	}
	
	/**
	 * 交易明細報表濾掉整批的語法
	 * 
	 * @param sqlPath
	 * @param spSql
	 * @param orderbySql
	 * @return
	 */
	public String getSQLForBat(String sqlPath ,String sqlPath2 , String spSql ,String spSql2 , String orderbySql){
		StringBuffer sql = new StringBuffer();
		sql.append(" WITH TEMP AS  ");
		sql.append(" ( ");
		sql.append(" SELECT row_number() over (order by TXDT)  as SEQNO");
		sql.append(" ,(CASE WHEN coalesce(VARCHAR(a.txDT),'') ='' THEN '' ELSE (CAST (YEAR(a.txDT)-1911 AS CHAR(3)) ||''|| SUBSTR( REPLACE(CAST( DATE(a.txdt) AS CHAR(10)),'-','/'),5,6)  )  END ) TXDT");
		sql.append(" ,(CASE WHEN coalesce(VARCHAR(a.txDT),'') ='' THEN '' ELSE (SUBSTR(REPLACE(CAST( TIME(a.TXDT) AS CHAR(8)) ,'.',':') ,1,8))  END ) TXT");
		sql.append(" ,a.pcode || coalesce(b.each_txn_name,'')  pcode  ,a.STAN ,a.txid, coalesce((select  txid ||'\n'|| txn_name from TXN_CODE where a.txid=txn_id),'') txn_name ,a.CLEARINGPHASE ");
		sql.append(" ,a.outAcctNo ,a.inAcctNo ,coalesce(a.senderId,'') ||'\n'|| coalesce(a.company_abbr,'') senderId ,Decimal(a.txAmt) txAmt ");
//		sql.append(" ,(case when a.newresult='R' then '失敗' when a.newresult='P' and a.senderstatus != '1' then '未完成' when a.newresult='P' and a.senderstatus = '1' then '失敗' else '成功' end) Resp ");
		sql.append(spSql2);
		sql.append(" ,RTRIM(coalesce(RC1 || '/','        /')) || coalesce(RC2 || '/','        /') || coalesce(RC3 || '\n','        \n') || RTRIM(coalesce(RC4 || '/','        /')) || RTRIM(coalesce(RC5 || '/','        /')) || coalesce(RC6 ,'         ') RC ");
//		手續費改抓發動行手續費
		sql.append(" ,coalesce(a.SENDERBANKID ,'') SENDERBANKID , coalesce((select brbk_name from bank_branch where brbk_id=a.SENDERBANKID),'') SENDERBANKID_NAME ");
		sql.append(" ,coalesce(a.outBankId,'')  outBankId , coalesce((select brbk_name from bank_branch where brbk_id=a.outBankId),'') outBankId_NAME  ");
		sql.append(" ,coalesce(a.inBankId ,'')  inBankId , coalesce((select brbk_name from bank_branch where brbk_id=a.inBankId),'') inBankId_NAME ");
		sql.append(spSql);
		sql.append(" , FEE_TYPE ");
		sql.append(" FROM RPONBLOCKTAB a");
		sql.append(" left join EACH_TXN_CODE b on b.each_txn_id = a.pcode ");
		sql.append(sqlPath);
		sql.append(" UNION ALL ");
		sql.append(" SELECT row_number() over (order by TXDT)  as SEQNO");
		sql.append(" ,(CASE WHEN coalesce(VARCHAR(a.txDT),'') ='' THEN '' ELSE (CAST (YEAR(a.txDT)-1911 AS CHAR(3)) ||''|| SUBSTR( REPLACE(CAST( DATE(a.txdt) AS CHAR(10)),'-','/'),5,6)  )  END ) TXDT");
		sql.append(" ,(CASE WHEN coalesce(VARCHAR(a.txDT),'') ='' THEN '' ELSE (SUBSTR(REPLACE(CAST( TIME(a.TXDT) AS CHAR(8)) ,'.',':') ,1,8))  END ) TXT");
		sql.append(" ,a.pcode || coalesce(b.each_txn_name,'')  pcode  ,a.STAN ,a.txid, coalesce((select  txid ||'\n'|| txn_name from TXN_CODE where a.txid=txn_id),'') txn_name ,a.CLEARINGPHASE ");
		sql.append(" ,a.outAcctNo ,a.inAcctNo ,coalesce(a.senderId,'') ||'\n'|| coalesce(a.company_abbr,'') senderId ,Decimal(a.txAmt) txAmt ");
//		sql.append(" ,(case when a.newresult='R' then '失敗' when a.newresult='P' and a.senderstatus != '1' then '未完成' when a.newresult='P' and a.senderstatus = '1' then '失敗' else '成功' end) Resp ");
		sql.append(spSql2);		
		sql.append(" ,RTRIM(coalesce(RC1 || '/','        /')) || coalesce(RC2 || '/','        /') || coalesce(RC3 || '\n','        \n') || RTRIM(coalesce(RC4 || '/','        /')) || RTRIM(coalesce(RC5 || '/','        /')) || coalesce(RC6 ,'         ') RC ");
		sql.append(" ,coalesce(a.SENDERBANKID ,'') SENDERBANKID , coalesce((select brbk_name from bank_branch where brbk_id=a.SENDERBANKID),'') SENDERBANKID_NAME ");
		sql.append(" ,coalesce(a.outBankId,'')  outBankId , coalesce((select brbk_name from bank_branch where brbk_id=a.outBankId),'') outBankId_NAME  ");
		sql.append(" ,coalesce(a.inBankId ,'')  inBankId , coalesce((select brbk_name from bank_branch where brbk_id=a.inBankId),'') inBankId_NAME ");
		sql.append(spSql);
		sql.append(" , FEE_TYPE ");
		sql.append(" FROM RPONBLOCKTAB a");
		sql.append(" left join EACH_TXN_CODE b on b.each_txn_id = a.pcode ");
		sql.append(sqlPath2);
		sql.append(" ) ");
		sql.append(" SELECT SEQNO,TXDT,TXT,PCODE,STAN,TXID,TXN_NAME,CLEARINGPHASE,OUTACCTNO,INACCTNO,SENDERID,TXAMT,RESP,RC,SENDERBANKID,SENDERBANKID_NAME,OUTBANKID,OUTBANKID_NAME,INBANKID,INBANKID_NAME,BANKID,BANK_NAME,FEE,FEE_TYPE FROM EACHUSER.TEMP a  ");
		sql.append(orderbySql);
		System.out.println("getSQLForBat>>"+sql);
		return sql.toString();
	}
	/**
	 * 銷帳交易明細報表
	 * @param sqlPath
	 * @param sqlPath2
	 * @param spSql
	 * @param spSql2
	 * @param orderbySql
	 * @return
	 */
	public String getSQLForWO(String sqlPath ,String sqlPath2 , String spSql ,String spSql2 , String orderbySql){
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT  ");
		sql.append(" (CASE WHEN COALESCE(VARCHAR(A.TXDT),'') ='' THEN '' ELSE (CAST (YEAR(A.TXDT)-1911 AS CHAR(3)) ||''|| SUBSTR( REPLACE(CAST( DATE(A.TXDT) AS CHAR(10)),'-','/'),5,6)  )  END ) TXDT ");
		sql.append(" ,(CASE WHEN COALESCE(VARCHAR(A.TXDT),'') ='' THEN '' ELSE (SUBSTR(REPLACE(CAST( TIME(A.TXDT) AS CHAR(8)) ,'.',':') ,1,8))  END ) TXT ");
		sql.append(" ,A.PCODE || COALESCE((SELECT EACH_TXN_NAME  FROM EACH_TXN_CODE WHERE EACH_TXN_ID = A.PCODE),'')  PCODE ,A.STAN ,A.TXID, COALESCE((SELECT  TXID ||'\n'|| TXN_NAME FROM TXN_CODE WHERE A.TXID=TXN_ID),'') TXN_NAME  ,A.CLEARINGPHASE ");
//		sql.append(" ,A.INACCTNO , COALESCE(A.TOLLID,'') TOLLID  , COALESCE( (SELECT WO_COMPANY_ABBR_NAME FROM WO_COMPANY_PROFILE  WHERE WO_COMPANY_ID = A.TOLLID  AND TXN_ID = A.TXID AND  BILL_TYPE_ID =  A.PFCLASS  AND LENGTH( WO_COMPANY_ABBR_NAME) >0 FETCH FIRST  1 ROWS ONLY  )     ,'') WO_ABBR ,DECIMAL(A.TXAMT) TXAMT ");
		sql.append(" ,A.INACCTNO , COALESCE(A.TOLLID,'') TOLLID  , DECIMAL(A.TXAMT) TXAMT ");
		sql.append(" ,CASE  WHEN  A.BILLFLAG ='1'  THEN ");
		sql.append("	  COALESCE( (SELECT WO_COMPANY_ABBR_NAME FROM WO_COMPANY_PROFILE  WHERE WO_COMPANY_ID = A.TOLLID  AND TXN_ID = A.TXID AND  BILL_TYPE_ID =  A.PFCLASS  AND LENGTH( WO_COMPANY_ABBR_NAME) >0 FETCH FIRST  1 ROWS ONLY  )  ,'') ");
		sql.append("		ELSE ");
		sql.append("      COALESCE( (SELECT PI_COMPANY_ABBR_NAME FROM PI_COMPANY_PROFILE  WHERE PI_COMPANY_ID = A.TOLLID  AND TXN_ID = A.TXID AND  BILL_TYPE_ID =  A.PFCLASS  AND LENGTH( PI_COMPANY_ABBR_NAME) >0 FETCH FIRST  1 ROWS ONLY  )  ,'') ");
		sql.append("  END  WO_ABBR ");
		
		sql.append(" ,COALESCE(A.PFCLASS ,'') PFCLASS ,COALESCE(A.BILLTYPE,'') BILLTYPE ");
//		sql.append(" ,(case when a.newresult='R' then '失敗' when a.newresult='P' and a.senderstatus != '1' then '未完成' when a.newresult='P' and a.senderstatus = '1' then '失敗' else '成功' end) Resp ");
		sql.append(spSql2);
		sql.append("  ,RTRIM(coalesce(RC1 || '/','        /')) || coalesce(RC2 || '/','        /') || coalesce(RC3 || '','        ') || RTRIM(coalesce(RC4 || '/','        /')) || RTRIM(coalesce(RC5 || '/','        /')) || coalesce(RC6 ,'         ') RC ");
//		手續費改抓發動行手續費
		sql.append(" ,COALESCE(A.INBANKID ,'')  INBANKID ,COALESCE(A.BILLTYPE,'')");
		sql.append(" ,COALESCE(  (SELECT BILLDATA FROM ONBLOCKAPPENDTAB WHERE STAN = A.STAN AND TXDATE = A.TXDATE), '' ) BILLDATA  ");
		sql.append(spSql);
		sql.append(" , FEE_TYPE ");
		sql.append(" FROM RPONBLOCKTAB A");
		sql.append(sqlPath);
		sql.append(orderbySql);
		System.out.println("getSQLForWO>>"+sql);
		return sql.toString();
	}
	
	
	public String getSQL(String sqlPath , String spSql , String orderbySql){
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT row_number() over (order by TXDT)  as SEQNO");
//		sql.append(" ,(CASE WHEN coalesce(VARCHAR(a.txDT),'') ='' THEN '' ELSE (CAST (YEAR(a.txDT)-1911 AS CHAR(3)) ||''|| SUBSTR( REPLACE(CAST( DATE(a.txdt) AS CHAR(10)),'-','/'),5,6)  ||'\n'|| SUBSTR(REPLACE(CAST( TIME(a.TXDT) AS CHAR(8)) ,'.',':') ,1,8))  END ) TXDT");
		sql.append(" ,(CASE WHEN coalesce(VARCHAR(a.txDT),'') ='' THEN '' ELSE (CAST (YEAR(a.txDT)-1911 AS CHAR(3)) ||''|| SUBSTR( REPLACE(CAST( DATE(a.txdt) AS CHAR(10)),'-','/'),5,6)  )  END ) TXDT");
		sql.append(" ,(CASE WHEN coalesce(VARCHAR(a.txDT),'') ='' THEN '' ELSE (SUBSTR(REPLACE(CAST( TIME(a.TXDT) AS CHAR(8)) ,'.',':') ,1,8))  END ) TXT");
		sql.append(" ,a.pcode || coalesce(b.each_txn_name,'')  pcode  ,a.STAN ,a.txid, coalesce((select  txid ||'\n'|| txn_name from TXN_CODE where a.txid=txn_id),'') txn_name ,a.CLEARINGPHASE ");
		sql.append(" ,a.outAcctNo ,a.inAcctNo ,coalesce(a.senderId,'') ||'\n'|| coalesce(a.company_abbr,'') senderId ,Decimal(a.txAmt) txAmt ");
//		20150520 edit by hugo req by UAT-20150519-02
//		sql.append(" ,(case when a.newresult='R' then '失敗' when a.newresult='P' then '未完成' else '成功' end) Resp ");
//		sql.append(" ,(case when a.newresult='R' then '失敗' when a.newresult='P' and a.senderstatus != '1' then '未完成' when a.newresult='P' and a.senderstatus = '1' then '處理中' else '成功' end) Resp ");
//		20150527 edit by hugo req by 李建利 '處理中'直接視為失敗
		sql.append(" ,(case when a.newresult='R' then '失敗' when a.newresult='P' and a.senderstatus != '1' then '未完成' when a.newresult='P' and a.senderstatus = '1' then '失敗' else '成功' end) Resp ");
		sql.append(" ,RTRIM(coalesce(RC1 || '/','        /')) || coalesce(RC2 || '/','        /') || coalesce(RC3 || '\n','        \n') || RTRIM(coalesce(RC4 || '/','        /')) || RTRIM(coalesce(RC5 || '/','        /')) || coalesce(RC6 ,'         ') RC ");
//		手續費改抓發動行手續費
		sql.append(" ,coalesce(a.SENDERBANKID ,'') SENDERBANKID , coalesce((select brbk_name from bank_branch where brbk_id=a.SENDERBANKID),'') SENDERBANKID_NAME ");
		sql.append(" ,coalesce(a.outBankId,'')  outBankId , coalesce((select brbk_name from bank_branch where brbk_id=a.outBankId),'') outBankId_NAME  ");
		sql.append(" ,coalesce(a.inBankId ,'')  inBankId , coalesce((select brbk_name from bank_branch where brbk_id=a.inBankId),'') inBankId_NAME ");
		
//		sql.append(" ,coalesce(a.SENDERHEAD,'') SENDERHEAD , coalesce((select brbk_name from bank_branch where brbk_id=a.SENDERHEAD),'') SENDERHEAD_NAME ");
//		sql.append(" ,(case when a.newresult !='R' then  coalesce(a.senderfee,0) else 0 end) fee ");
		sql.append(spSql);
		sql.append(" FROM RPONBLOCKTAB a");
		sql.append(" left join EACH_TXN_CODE b on b.each_txn_id = a.pcode ");
		sql.append(sqlPath);
		sql.append(orderbySql);
		System.out.println("getSQL>>"+sql);
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
		System.out.println("beanList>>"+beanList);
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
	
	public static void main(String[] args) {
		System.out.println(new BigDecimal(String.valueOf("-18.27")).setScale(2).abs());
	}
	
}
