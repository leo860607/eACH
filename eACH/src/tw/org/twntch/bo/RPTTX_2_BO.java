package tw.org.twntch.bo;

import java.math.BigDecimal;
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
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.RptUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.zDateHandler;

public class RPTTX_2_BO {

	private RPONBLOCKTAB_Dao rponblocktab_Dao;
	private BANK_GROUP_Dao bank_group_Dao;
	public Map<String, String> export(String txdt, String opt_id,String bgbk_id ,  String clearingPhase ,  String opt_bank ,String opt_type, String serchStrs){
		Map<String, String> rtnMap = null;
		String outputFilePath ="";
		try{
			rtnMap = new HashMap<String, String>();
			rtnMap.put("serchStrs", serchStrs);
			Map<String, Object> params = new HashMap<String, Object>();
//			SQL查詢條件 區塊
			params.put("TXDT","'%"+ DateTimeUtils.convertDate(txdt, "yyyymmdd", "yyyymmdd")+"%' ");
			params.put("opt_id", opt_id);
			params.put("CLEARINGPHASE", clearingPhase);
//			顯示區塊
//			params.put("V_TXDT",DateTimeUtils.convertDate(DateTimeUtils.convertDate(txdt,"yyyyMMdd","yyyyMMdd"),"yyyyMMdd","民國 yyy 年 MM 月 dd 日"));
			params.put("V_TXDT",DateTimeUtils.convertDate(DateTimeUtils.convertDate(txdt,"yyyyMMdd","yyyyMMdd"),"yyyyMMdd","營業日期: yyy/MM/dd"));
			System.out.println("opt_id>>"+opt_id);
			System.out.println("opt_bank>>"+opt_bank);
			if(StrUtils.isEmpty(opt_id)){
				params.put("V_OPT_BANK", "全部");
			}else{
				params.put("V_OPT_BANK", opt_bank);
			}
			params.put("V_CLEARINGPHASE",clearingPhase);
			params.put("V_PRINT_DATE",zDateHandler.getODDate());
			params.put("V_PRINT_TIME",zDateHandler.getTheTime());
			params.put("V_OP_TYPE",getOpType_Name(Integer.valueOf(opt_type)));
			System.out.println("params >> " + params);
//			Map map = this.getConditionData(txdt, opt_id, bgbk_id, clearingPhase, Integer.valueOf(opt_type));
//			String sql = getSQL(map.get("sqlPath").toString(), getSPSql(Integer.valueOf(opt_type)), getOrderBySql(Integer.valueOf(opt_type)));
			Map map = this.getConditionDataForBat(txdt, opt_id, bgbk_id, clearingPhase, Integer.valueOf(opt_type));
			String sql = getSQLForBat(map.get("sqlPath").toString(), map.get("sqlPath2").toString() ,getSPSql(Integer.valueOf(opt_type)), getOrderBySql(Integer.valueOf(opt_type)));
			List list = rponblocktab_Dao.getRptData(sql, (List<String>) map.get("values"));
			int pathType = Arguments.getStringArg("ISWINOPEN").equals("Y")?RptUtils.C_PATH :RptUtils.R_PATH;
			outputFilePath = RptUtils.export(RptUtils.COLLECTION,pathType, "tx_2", "tx_2", params, list);

			
			
//			String outputFilePath = RptUtils.export(RptUtils.SQL, "tx_1", "tx_1", params, rponblocktab_Dao.getCurrentSession().connection());
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
	
	
	public String getOrderBySql(int opt_type){
		StringBuffer sql = new StringBuffer();
		sql.append(" order by  a.CLEARINGPHASE ");
		sql.append(" , BANKID ");
//		switch (opt_type) {
//		case 0:
//			sql.append(" , a.SENDERHEAD ");
//			break;
//		case 1:
//			sql.append(" , a.OUTHEAD ");
//			break;
//		case 2:
//			sql.append(" , a.INHEAD ");
//			break;
//		default:
//			sql.append(" , a.SENDERHEAD ");
//			break;
//		}
		sql.append(" , a.PCODE ,a.TXDT ,a.STAN ");
		System.out.println("OrderBySql>>"+sql);
		return sql.toString();
	}
	public String getSPSql(int opt_type){
		StringBuffer sql = new StringBuffer();
		switch (opt_type) {
		case 0:
			sql.append(" ,coalesce(a.SENDERHEAD,'') BANKID , coalesce((select brbk_name from bank_branch where brbk_id=a.SENDERHEAD),'') BANK_NAME ");
			break;
		case 1:
			sql.append(" ,coalesce(a.OUTHEAD,'') BANKID , coalesce((select brbk_name from bank_branch where brbk_id=a.OUTHEAD),'') BANK_NAME ");
			break;
		case 2:
			sql.append(" ,coalesce(a.INHEAD,'') BANKID , coalesce((select brbk_name from bank_branch where brbk_id=a.INHEAD),'') BANK_NAME ");
			break;
		default:
			sql.append(" ,coalesce(a.SENDERHEAD,'') BANKID , coalesce((select brbk_name from bank_branch where brbk_id=a.SENDERHEAD),'') BANK_NAME ");
			break;
		}
		System.out.println("SPSql>>"+sql);
		return sql.toString();
	}
	
	public Map getConditionData(String txdt,String opt_id,String bgbk_id ,  String clearingPhase ,int opt_type) throws Exception{
		StringBuffer sql = new StringBuffer();
		Map<String, String> params = new LinkedHashMap<String, String> ();
		Map retmap = new LinkedHashMap();
		List<String> values = new LinkedList<String>(); 
		try {
//			params.put("a.RESULTSTATUS", "P");
			params.put("a.newresult", "P");
			params.put("a.SENDERSTATUS", "1");
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
//						sql.append( key+" LIKE ? " );
						sql.append( key+" = ? ");
//						values.add("%"+ DateTimeUtils.convertDate(params.get(key), "yyyymmdd", "yyyymmdd")+"%");
						values.add(DateTimeUtils.convertDate(params.get(key), "yyyymmdd", "yyyymmdd"));
					}else if(key.equals("a.SENDERSTATUS")){
						sql.append( key+" != ? ");
						values.add(params.get(key));
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
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e.toString()) ; 
		}
		return retmap;
	}
	
	public Map getConditionDataForBat(String txdt,String opt_id,String bgbk_id ,  String clearingPhase ,int opt_type) throws Exception{
		StringBuffer sql = new StringBuffer();
		StringBuffer sql2 = new StringBuffer();
		boolean hasOpt_Id = StrUtils.isNotEmpty(opt_id) ? true : false;
		Map<String, String> params = new LinkedHashMap<String, String> ();
		Map<String, String> params2 = new LinkedHashMap<String, String> ();
		Map retmap = new LinkedHashMap();
		List<String> values = new LinkedList<String>(); 
		try {
//			params.put("a.RESULTSTATUS", "P");
			params.put("a.newresult", "P");
			params.put("a.SENDERSTATUS", "1");
			params.put("a.CLEARINGPHASE", clearingPhase);
			params.put("a.BIZDATE", txdt);
			params2.putAll(params);
			switch (opt_type) {
			case 0:
				params.put("a.FLBIZDATE", "''");
				params.put("a.SENDERACQUIRE", opt_id);
				params.put("a.SENDERHEAD", bgbk_id);
				
				params2.put("a.FLBIZDATE", "''");
				params2.put("a.SENDERACQUIRE", opt_id);
				params2.put("a.SENDERHEAD", bgbk_id);
				break;
			case 1:
				params.put("a.FLBIZDATE", "''");
				params.put("a.OUTACQUIRE", opt_id);
				params.put("a.OUTHEAD", bgbk_id);
				if(hasOpt_Id){
					params2.putAll(params);
				}
				params2.put("a.FLBIZDATE", "''");
				params2.put(" substr(COALESCE(PCODE,''),4)  ", "1");
				break;
			case 2:
				params.put("a.FLBIZDATE", "''");
				params.put("a.INACQUIRE", opt_id);
				params.put("a.INHEAD", bgbk_id);
				if(hasOpt_Id){
					params2.putAll(params);
				}
				params2.put("a.FLBIZDATE", "''");
				params2.put(" substr(COALESCE(PCODE,''),4)  ", "2");
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
					if(key.equals("a.FLBIZDATE")){
						sql.append( "coalesce( "+key+" , '')  = '' " );
					}else if(key.equals("a.BIZDATE")){
						sql.append( key+" = ? ");
						values.add(DateTimeUtils.convertDate(params.get(key), "yyyymmdd", "yyyymmdd"));
					}else if(key.equals("a.SENDERSTATUS")){
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
					}else if(key.equals("a.SENDERSTATUS")){
						sql2.append( key+" != ? ");
						values.add(params2.get(key));
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
			}
			
			
			retmap.put("sqlPath", sql.toString());
			retmap.put("sqlPath2", sql2.toString());
			retmap.put("values", values);
			System.out.println("getConditionDataForBat_sqlPath>>"+sql);
			System.out.println("getConditionDataForBat_sqlPath2>>"+sql2);
			System.out.println("getConditionDataForBat_values>>"+values);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e.toString()) ; 
		}
		return retmap;
	}
	
	public String getSQLForBat(String sqlPath ,String sqlPath2 , String spSql , String orderbySql){
		StringBuffer sql = new StringBuffer();
		sql.append(" WITH TEMP AS  ");
		sql.append(" ( ");
		sql.append(" SELECT  c.business_type_id || coalesce((select (business_type_name) from BUSINESS_TYPE where business_type_id=c.business_type_id),'') biztype");
		sql.append(" ,(CASE WHEN coalesce(VARCHAR(a.TXDT),'') ='' THEN '' ELSE (CAST (YEAR(a.TXDT)-1911 AS CHAR(3)) ||''|| SUBSTR( REPLACE(CAST( DATE(a.TXDT) AS CHAR(10)),'-','/'),5,6)  ||'\n'|| SUBSTR(REPLACE(CAST( TIME(a.TXDT) AS CHAR(8)) ,'.',':') ,1,8)) END ) TXDT  ");
		sql.append(" ,a.pcode ||'\n'|| coalesce(c.each_txn_name,'')  pcode , a.STAN  ,a.txid, coalesce((select  txid ||'\n'|| txn_name from TXN_CODE where a.txid=txn_id),'') txn_name ,a.CLEARINGPHASE ");
		sql.append(" ,coalesce(a.SENDERBANKID ,'') SENDERBANKID , coalesce((select brbk_name from bank_branch where brbk_id=a.SENDERBANKID),'') SENDERBANKID_NAME ");
		sql.append(" ,coalesce(a.outBankId,'')  outBankId , coalesce((select brbk_name from bank_branch where brbk_id=a.outBankId),'') outBank_NAME ");
		sql.append(" ,coalesce(a.inBankId ,'')  inBankId , coalesce((select brbk_name from bank_branch where brbk_id=a.inBankId),'') inBank_NAME ");
		sql.append(" ,OUTACCTNO ,INACCTNO, a.txAmt as txamt,a.newresult ,coalesce(a.senderId,'') ||'\n'|| coalesce(a.company_abbr,'') senderId ");
		sql.append(spSql);
		sql.append(" FROM RPONBLOCKTAB a");
		sql.append(" left join EACH_TXN_CODE c on c.each_txn_id = a.pcode ");
		sql.append(sqlPath);
		sql.append(" UNION ALL ");
		sql.append(" SELECT  c.business_type_id || coalesce((select (business_type_name) from BUSINESS_TYPE where business_type_id=c.business_type_id),'') biztype");
		sql.append(" ,(CASE WHEN coalesce(VARCHAR(a.TXDT),'') ='' THEN '' ELSE (CAST (YEAR(a.TXDT)-1911 AS CHAR(3)) ||''|| SUBSTR( REPLACE(CAST( DATE(a.TXDT) AS CHAR(10)),'-','/'),5,6)  ||'\n'|| SUBSTR(REPLACE(CAST( TIME(a.TXDT) AS CHAR(8)) ,'.',':') ,1,8)) END ) TXDT  ");
		sql.append(" ,a.pcode ||'\n'|| coalesce(c.each_txn_name,'')  pcode , a.STAN  ,a.txid, coalesce((select  txid ||'\n'|| txn_name from TXN_CODE where a.txid=txn_id),'') txn_name ,a.CLEARINGPHASE ");
		sql.append(" ,coalesce(a.SENDERBANKID ,'') SENDERBANKID , coalesce((select brbk_name from bank_branch where brbk_id=a.SENDERBANKID),'') SENDERBANKID_NAME ");
		sql.append(" ,coalesce(a.outBankId,'')  outBankId , coalesce((select brbk_name from bank_branch where brbk_id=a.outBankId),'') outBank_NAME ");
		sql.append(" ,coalesce(a.inBankId ,'')  inBankId , coalesce((select brbk_name from bank_branch where brbk_id=a.inBankId),'') inBank_NAME ");
		sql.append(" ,OUTACCTNO ,INACCTNO, a.txAmt as txamt,a.newresult ,coalesce(a.senderId,'') ||'\n'|| coalesce(a.company_abbr,'') senderId ");
		sql.append(spSql);
		sql.append(" FROM RPONBLOCKTAB a");
		sql.append(" left join EACH_TXN_CODE c on c.each_txn_id = a.pcode ");
		sql.append(sqlPath2);
		sql.append(" ) ");
		sql.append(" SELECT * FROM TEMP a ");
		sql.append(orderbySql);
		System.out.println("getSQLForBat.sql>>"+sql);
		return sql.toString();
	}
	public String getSQL(String sqlPath , String spSql , String orderbySql){
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT  c.business_type_id || coalesce((select (business_type_name) from BUSINESS_TYPE where business_type_id=c.business_type_id),'') biztype");
		sql.append(" ,(CASE WHEN coalesce(VARCHAR(a.TXDT),'') ='' THEN '' ELSE (CAST (YEAR(a.TXDT)-1911 AS CHAR(3)) ||''|| SUBSTR( REPLACE(CAST( DATE(a.TXDT) AS CHAR(10)),'-','/'),5,6)  ||'\n'|| SUBSTR(REPLACE(CAST( TIME(a.TXDT) AS CHAR(8)) ,'.',':') ,1,8)) END ) TXDT  ");
		sql.append(" ,a.pcode ||'\n'|| coalesce(c.each_txn_name,'')  pcode , a.STAN  ,a.txid, coalesce((select  txid ||'\n'|| txn_name from TXN_CODE where a.txid=txn_id),'') txn_name ,a.CLEARINGPHASE ");
		sql.append(" ,coalesce(a.SENDERBANKID ,'') SENDERBANKID , coalesce((select brbk_name from bank_branch where brbk_id=a.SENDERBANKID),'') SENDERBANKID_NAME ");
		sql.append(" ,coalesce(a.outBankId,'')  outBankId , coalesce((select brbk_name from bank_branch where brbk_id=a.outBankId),'') outBank_NAME ");
		sql.append(" ,coalesce(a.inBankId ,'')  inBankId , coalesce((select brbk_name from bank_branch where brbk_id=a.inBankId),'') inBank_NAME ");
		sql.append(" ,OUTACCTNO ,INACCTNO, a.txAmt as txamt,a.newresult ,coalesce(a.senderId,'') ||'\n'|| coalesce(a.company_abbr,'') senderId ");
		sql.append(spSql);
		sql.append(" FROM RPONBLOCKTAB a");
		sql.append(" left join EACH_TXN_CODE c on c.each_txn_id = a.pcode ");
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
	
	
	
}
