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
import tw.org.twntch.db.dao.hibernate.BANK_OPBK_Dao;
import tw.org.twntch.db.dao.hibernate.EACH_TXN_CODE_Dao;
import tw.org.twntch.db.dao.hibernate.RPONBLOCKTAB_Dao;
import tw.org.twntch.po.BANK_GROUP;
import tw.org.twntch.po.BANK_OPBK;
import tw.org.twntch.po.EACH_TXN_CODE;
import tw.org.twntch.util.BeanUtils;
import tw.org.twntch.util.CodeUtils;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.RptUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.zDateHandler;

public class RPTST_4_BO {
	private RPONBLOCKTAB_Dao rponblocktab_Dao;
	private BANK_GROUP_Dao bank_group_Dao;
	private EACH_TXN_CODE_Dao each_txn_code_Dao;
	private BANK_OPBK_Dao bank_opbk_Dao;
	
	public List<LabelValueBean> getOpbkIdList(){
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
	
	public List<LabelValueBean> getBgbkIdList(){
		List<BANK_GROUP> list = bank_group_Dao.getBgbkIdList();
		List<LabelValueBean> beanList = new LinkedList<LabelValueBean>();
		LabelValueBean bean = null;
		for(BANK_GROUP po : list){
			bean = new LabelValueBean(po.getBGBK_ID() + " - " + po.getBGBK_NAME(), po.getBGBK_ID());
			beanList.add(bean);
		}
		System.out.println("beanList>>"+beanList);
		return beanList;
	}
	
	public Map<String, String> ex_export(String ext, String startYear, String startMonth, String endYear, String endMonth, String opbkId, String bgbkId, String senderId,String feeType,String companyName,String txnId,String serchStrs){
		return export(ext, startYear, startMonth, endYear, endMonth, opbkId, bgbkId, senderId, feeType, companyName, txnId, serchStrs);
	}

	public Map<String, String> export(String ext, String startYear, String startMonth, String endYear, String endMonth, String opbkId, String bgbkId, String senderId,String feeType,String companyName,String txnId,String serchStrs){
		Map<String, String> rtnMap = null;
		
		try{
			rtnMap = new HashMap<String, String>();
			rtnMap.put("serchStrs", serchStrs);
			Map<String, Object> params = new HashMap<String, Object>();
			//?????????????????????????????????
			params.put("V_TXDT",
					"???????????????" + 
					DateTimeUtils.convertDate(DateTimeUtils.convertDate(startYear+startMonth,"yyyyMM","yyyyMM"),"yyyyMM","yyy/MM") + " ~ " + 
					DateTimeUtils.convertDate(DateTimeUtils.convertDate(endYear+endMonth,"yyyyMM","yyyyMM"),"yyyyMM","yyy/MM")
			);
			params.put("V_PRINT_DATE", DateTimeUtils.convertDate(DateTimeUtils.INTERCONVERSION, zDateHandler.getTheDate(), "yyyy/MM/dd", "yyy/MM/dd"));
			if(StrUtils.isEmpty(opbkId)){
				params.put("V_OPT_BANK", "??????");
			}else{
				List<BANK_OPBK> bank_opbk = bank_opbk_Dao.getDataByBgbkId(opbkId, "");
				String opbk_name = "";
				for (int i = 0; i < bank_opbk.size(); i++) {
					opbk_name = bank_opbk.get(i).getOPBK_NAME();
				}
				params.put("V_OPT_BANK", opbk_name);
			}
			//System.out.println("params >> " + params);
			
			Map queryParam = this.getConditionData(startYear, startMonth, endYear, endMonth, opbkId, bgbkId, senderId, feeType, companyName, txnId);
			//System.out.println("queryParam >> " + queryParam);
			
			//List list = rponblocktab_Dao.getSt_1_Detail_Data_ForRpt(queryParam.get("sqlPath").toString(), (List<String>) queryParam.get("values"));
			List datalist = rponblocktab_Dao.getRptData(getSQL((String)queryParam.get("sqlPath")), (List<String>)queryParam.get("values"));
			List list = new ArrayList();
			for(Object each:datalist) {
				Map eachMap = CodeUtils.objectCovert(Map.class, each);
				System.out.println("eachMap >> "+ eachMap.toString());
				switch (null==(String)eachMap.get("FEE_TYPE")?"":(String)eachMap.get("FEE_TYPE")){
				case "":
					eachMap.put("FEE_TYPE", "");
					break;
				case "A":
					eachMap.put("FEE_TYPE", "??????");
					break;
				case "B":
					eachMap.put("FEE_TYPE", "??????");
					break;
				case "C":
					eachMap.put("FEE_TYPE", "?????????");
					break;
				case "D":
					eachMap.put("FEE_TYPE", "??????");
					break;
				}
				list.add(eachMap);
			}
			
			int pathType = Arguments.getStringArg("ISWINOPEN").equals("Y")?RptUtils.C_PATH :RptUtils.R_PATH;
			String outputFilePath = "";
			if(ext.equals("xls")){
				outputFilePath = RptUtils.export(RptUtils.COLLECTION,pathType, "ex_st_4", "st_4", params, list, 2);
			}else if(ext.equals("pdf")){
				outputFilePath = RptUtils.export(RptUtils.COLLECTION,pathType, "st_4", "st_4", params, list);
			}else if(ext.equals("csv")){
				outputFilePath = RptUtils.export(RptUtils.COLLECTION,pathType, "csv_st_4", "st_4", params, list, 4);
			}
			if(StrUtils.isNotEmpty(outputFilePath)){
				rtnMap.put("result", "TRUE");
				rtnMap.put("msg", outputFilePath);
			}else{
				rtnMap.put("result", "FALSE");
				rtnMap.put("msg", "????????????!");
			}
		}catch(Exception e){
			e.printStackTrace();
			rtnMap.put("result", "FALSE");
			rtnMap.put("msg", "????????????!");
		}
		
		return rtnMap;
	}
	
	public Map getConditionData(String startYear, String startMonth, String endYear, String endMonth, String opbkId, String bgbkId, String senderId, String feeType, String companyName, String txnId) throws Exception{
		StringBuffer sql = new StringBuffer();
		Map<String, String> params = new LinkedHashMap<String, String> ();
		Map retmap = new LinkedHashMap();
		List<String> values = new LinkedList<String>(); 
		try {
			params.put("START_YEAR", startYear);
			params.put("START_MONTH", startMonth);
			params.put("END_YEAR", endYear);
			params.put("END_MONTH", endMonth);
			params.put("OPBK_ID", opbkId);
			params.put("BGBK_ID", bgbkId);
			params.put("SENDERID", senderId);
			params.put("FEE_TYPE", feeType);
			params.put("COMPANY_ABBR_NAME", companyName);
			params.put("TXN_ID", txnId);
			int i = 0;
			String start_year = "";
			String end_year = "";
			
			for(String key : params.keySet()){
				if(StrUtils.isNotEmpty(params.get(key))){
					
					if(key.equals("START_YEAR")){
						start_year = params.get(key);
					}
					else if(key.equals("START_MONTH")){
						sql.append(" AND ");
						sql.append( "YYYYMM >= ? " );
						values.add(DateTimeUtils.convertDate(start_year + params.get(key), "yyyyMM", "yyyyMM"));
					}
					else if(key.equals("END_YEAR")){
						end_year = params.get(key);
					}
					else if(key.equals("END_MONTH")){
						sql.append(" AND ");
						sql.append( "YYYYMM <= ? " );
						values.add(DateTimeUtils.convertDate(end_year + params.get(key), "yyyyMM", "yyyyMM"));
					}else if(key.equals("COMPANY_ABBR_NAME")) {
						sql.append(" AND ");
						sql.append(" COMPANY_ABBR_NAME LIKE ? ");
						values.add("%" + params.get(key) + "%");
					}
					else{
						sql.append(" AND ");
						sql.append( key+" = ? ");
						values.add(params.get(key));
					}
					i++;
				}
			}
			
			retmap.put("sqlPath", sql.toString());
			retmap.put("values", values);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e.toString()) ; 
		}
		return retmap;
	}
	
	public String getSQL(String condition){
		StringBuffer sql = new StringBuffer();
		sql.append("WITH TEMP1 AS ( ");
		sql.append("SELECT * FROM ");
		sql.append("EACHUSER.RPMONTHSUMTAB ");
		sql.append("WHERE OP_TYPE='S' " + (StrUtils.isEmpty(condition)?"":condition));
		sql.append(" )");//AS
		sql.append("SELECT ");
		sql.append("VARCHAR(A.SENDERID) SENDERID , ");
		//sql.append("VARCHAR((SELECT COMPANY_ABBR_NAME FROM TEMP1  WHERE SENDERID=A.SENDERID AND TXN_ID=A.TXN_ID AND BRBK_ID=A.BRBK_ID AND FEE_TYPE=A.FEE_TYPE FETCH FIRST 1 ROWS ONLY  ))  COMPANY_ABBR_NAME, ");
		sql.append("VARCHAR( EACHUSER.GETCOMPANY_NAME(coalesce(A.SENDERID,'')) ) COMPANY_ABBR_NAME , " );
		sql.append("VARCHAR(A.TXN_ID) TXN_ID , ");
		sql.append("VARCHAR(A.BRBK_ID) BRBK_ID , ");
		sql.append("VARCHAR(A.FEE_TYPE) FEE_TYPE , ");
		sql.append("SUM(A.CNT) CNT, ");
		sql.append("SUM(A.AMT) AMT, ");
		sql.append("COALESCE( (SELECT SUM(CNT) FROM TEMP1  WHERE SENDERID= A.SENDERID AND TXN_ID= A.TXN_ID AND BRBK_ID= A.BRBK_ID AND FEE_TYPE= A.FEE_TYPE AND RESULTSTATUS IN ('A','P') ) ,0)  S_CNT, ");
		sql.append("COALESCE( (SELECT SUM(AMT) FROM  TEMP1  WHERE SENDERID= A.SENDERID AND TXN_ID= A.TXN_ID AND BRBK_ID= A.BRBK_ID AND FEE_TYPE= A.FEE_TYPE AND RESULTSTATUS IN ('A','P') ) ,0)  S_AMT, ");
		sql.append("COALESCE( (SELECT SUM(CNT) FROM TEMP1  WHERE SENDERID= A.SENDERID AND TXN_ID= A.TXN_ID AND BRBK_ID= A.BRBK_ID AND FEE_TYPE= A.FEE_TYPE AND RESULTSTATUS='R' ) ,0)  F_CNT, ");
		sql.append("COALESCE( (SELECT SUM(AMT) FROM  TEMP1  WHERE SENDERID= A.SENDERID AND TXN_ID= A.TXN_ID AND BRBK_ID= A.BRBK_ID AND FEE_TYPE= A.FEE_TYPE AND RESULTSTATUS='R' ) ,0)  F_AMT ");
		sql.append("FROM  TEMP1 A ");
		sql.append("GROUP BY A.SENDERID ,A.TXN_ID , A.FEE_TYPE , A.BRBK_ID ");
		sql.append("ORDER BY A.SENDERID ,A.TXN_ID , A.FEE_TYPE , A.BRBK_ID ");
		
		System.out.println("SQL >> " + sql.toString());
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

	public EACH_TXN_CODE_Dao getEach_txn_code_Dao() {
		return each_txn_code_Dao;
	}

	public void setEach_txn_code_Dao(EACH_TXN_CODE_Dao each_txn_code_Dao) {
		this.each_txn_code_Dao = each_txn_code_Dao;
	}

	public BANK_OPBK_Dao getBank_opbk_Dao() {
		return bank_opbk_Dao;
	}

	public void setBank_opbk_Dao(BANK_OPBK_Dao bank_opbk_Dao) {
		this.bank_opbk_Dao = bank_opbk_Dao;
	}
}
