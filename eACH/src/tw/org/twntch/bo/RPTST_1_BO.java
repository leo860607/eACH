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
import tw.org.twntch.db.dao.hibernate.EACH_TXN_CODE_Dao;
import tw.org.twntch.db.dao.hibernate.RPONBLOCKTAB_Dao;
import tw.org.twntch.po.BANK_GROUP;
import tw.org.twntch.po.EACH_TXN_CODE;
import tw.org.twntch.util.BeanUtils;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.RptUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.zDateHandler;

public class RPTST_1_BO {
	private RPONBLOCKTAB_Dao rponblocktab_Dao;
	private BANK_GROUP_Dao bank_group_Dao;
	private EACH_TXN_CODE_Dao each_txn_code_Dao;
	
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
	
	/**
	 * 取得交易代號(PCODE-4碼)清單
	 * @return
	 */
	public List<LabelValueBean> getPcodeList(){
		List<EACH_TXN_CODE> list = each_txn_code_Dao.getTranCode();
		List<LabelValueBean> beanList = new LinkedList<LabelValueBean>();
		LabelValueBean bean = null;
		for(EACH_TXN_CODE po : list){
			bean = new LabelValueBean(po.getEACH_TXN_ID() + " - " + po.getEACH_TXN_NAME(), po.getEACH_TXN_ID());
			beanList.add(bean);
		}
		System.out.println("beanList>>"+beanList);
		return beanList;
	}
	
	public Map<String, String> ex_export(String ext, String startDate, String endDate, String opbkId, String bgbkId, String resultStatus, String clearingPhase, String pcode, String serchStrs){
		return export(ext, startDate, endDate, opbkId, bgbkId, resultStatus, clearingPhase, pcode, serchStrs);
	}
	
	public Map<String, String> export(String ext, String startDate, String endDate, String opbkId, String bgbkId, String resultStatus, String clearingPhase, String pcode, String serchStrs){
		Map<String, String> rtnMap = null;
		
		try{
			rtnMap = new HashMap<String, String>();
			rtnMap.put("serchStrs", serchStrs);
			Map<String, Object> params = new HashMap<String, Object>();
			//「營業日區間」為必填
			params.put("V_TXDT",
					"營業日期：" + 
					DateTimeUtils.convertDate(DateTimeUtils.convertDate(startDate,"yyyyMMdd","yyyyMMdd"),"yyyyMMdd","yyy/MM/dd") + " ~ " + 
					DateTimeUtils.convertDate(DateTimeUtils.convertDate(endDate,"yyyyMMdd","yyyyMMdd"),"yyyyMMdd","yyy/MM/dd")
			);
			//清算階段
			params.put("V_CLEARINGPHASE", StrUtils.isEmpty(clearingPhase)?"全部":clearingPhase);
			params.put("V_PRINT_DATE", DateTimeUtils.convertDate(DateTimeUtils.INTERCONVERSION, zDateHandler.getTheDate(), "yyyy/MM/dd", "yyy/MM/dd"));
			//System.out.println("params >> " + params);
			
			Map queryParam = this.getConditionData(startDate, endDate, opbkId, bgbkId, resultStatus, clearingPhase, pcode);
			//System.out.println("queryParam >> " + queryParam);
			
			//List list = rponblocktab_Dao.getSt_1_Detail_Data_ForRpt(queryParam.get("sqlPath").toString(), (List<String>) queryParam.get("values"));
			List list = rponblocktab_Dao.getRptData(getSQL((String)queryParam.get("sqlPath")), (List<String>)queryParam.get("values"));
			
			int pathType = Arguments.getStringArg("ISWINOPEN").equals("Y")?RptUtils.C_PATH :RptUtils.R_PATH;
			String outputFilePath = "";
			if(ext.equals("xls")){
				outputFilePath = RptUtils.export(RptUtils.COLLECTION,pathType, "ex_st_1", "st_1", params, list, 2);
			}else if(ext.equals("pdf")){
				outputFilePath = RptUtils.export(RptUtils.COLLECTION,pathType, "st_1", "st_1", params, list);
			}
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
	
	public Map getConditionData(String startDate, String endDate, String opbkId, String bgbkId, String resultStatus, String clearingPhase, String pcode) throws Exception{
		StringBuffer sql = new StringBuffer();
		Map<String, String> params = new LinkedHashMap<String, String> ();
		Map retmap = new LinkedHashMap();
		List<String> values = new LinkedList<String>(); 
		try {
			params.put("START_DATE", startDate);
			params.put("END_DATE", endDate);
			params.put("OPBK_ID", opbkId);
			params.put("BGBK_ID", bgbkId);
			params.put("RESULTSTATUS", resultStatus);
			params.put("CLEARINGPHASE", clearingPhase);
			params.put("PCODE", pcode);
			int i = 0;
			for(String key : params.keySet()){
				if(StrUtils.isNotEmpty(params.get(key))){
					if(i==0){sql.append(" WHERE ");}else{sql.append(" AND ");}
					if(key.equals("START_DATE")){
						sql.append( "BIZDATE >= ? " );
						values.add(DateTimeUtils.convertDate(params.get(key), "yyyyMMdd", "yyyyMMdd"));
					}else if(key.equals("END_DATE")){
						sql.append( "BIZDATE <= ? " );
						values.add(DateTimeUtils.convertDate(params.get(key), "yyyyMMdd", "yyyyMMdd"));
					}else{
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
		sql.append("WITH TEMP AS ( ");
		sql.append("    SELECT * FROM RPDAILYSUMTAB " + (StrUtils.isEmpty(condition)?"":condition));
		sql.append(") ");
		
		sql.append("SELECT ");
		sql.append("VARCHAR(A.BGBK_ID) || '-' || (SELECT BGBK_NAME FROM BANK_GROUP WHERE BGBK_ID = A.BGBK_ID) AS BGBK_ID, ");
//		sql.append("VARCHAR(A.PCODE) || '-' || (SELECT EACH_TXN_NAME FROM EACH_TXN_CODE WHERE EACH_TXN_ID = PCODE) AS PCODE, ");
		sql.append(" COALESCE ( VARCHAR(A.PCODE) || '-' || (SELECT EACH_TXN_NAME FROM EACH_TXN_CODE WHERE EACH_TXN_ID = PCODE) ,'') AS PCODE, ");
		sql.append("(CASE VARCHAR(A.RESULTSTATUS) WHEN 'A' THEN '成功' WHEN 'R' THEN '失敗' WHEN 'P' THEN '未完成' ELSE '未定義' END) AS RESULTSTATUS ");
		sql.append(",COALESCE((SELECT SUM(COALESCE(CNT,0)) FROM TEMP WHERE BGBK_ID = A.BGBK_ID AND PCODE = A.PCODE AND RESULTSTATUS = A.RESULTSTATUS AND OP_TYPE = 'S'),0) AS FIRECOUNT ");
		sql.append(",COALESCE((SELECT SUM(COALESCE(AMT,0)) FROM TEMP WHERE BGBK_ID = A.BGBK_ID AND PCODE = A.PCODE AND RESULTSTATUS = A.RESULTSTATUS AND OP_TYPE = 'S'),0) AS FIREAMT ");
		sql.append(",COALESCE((SELECT SUM(COALESCE(CNT,0)) FROM TEMP WHERE BGBK_ID = A.BGBK_ID AND PCODE = A.PCODE AND RESULTSTATUS = A.RESULTSTATUS AND OP_TYPE = 'O'),0) AS DEBITCOUNT ");
		sql.append(",COALESCE((SELECT SUM(COALESCE(AMT,0)) FROM TEMP WHERE BGBK_ID = A.BGBK_ID AND PCODE = A.PCODE AND RESULTSTATUS = A.RESULTSTATUS AND OP_TYPE = 'O'),0) AS DEBITAMT ");
		sql.append(",COALESCE((SELECT SUM(COALESCE(CNT,0)) FROM TEMP WHERE BGBK_ID = A.BGBK_ID AND PCODE = A.PCODE AND RESULTSTATUS = A.RESULTSTATUS AND OP_TYPE = 'I'),0) AS SAVECOUNT ");
		sql.append(",COALESCE((SELECT SUM(COALESCE(AMT,0)) FROM TEMP WHERE BGBK_ID = A.BGBK_ID AND PCODE = A.PCODE AND RESULTSTATUS = A.RESULTSTATUS AND OP_TYPE = 'I'),0) AS SAVEAMT ");
		sql.append("FROM TEMP AS A ");
		sql.append("GROUP BY A.BGBK_ID, A.PCODE, A.RESULTSTATUS ");
		sql.append("ORDER BY A.BGBK_ID, A.PCODE, A.RESULTSTATUS ");
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
	
}
