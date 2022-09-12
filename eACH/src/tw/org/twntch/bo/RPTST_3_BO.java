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

public class RPTST_3_BO {
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
	
	public Map<String, String> ex_export(String ext, String startYear, String startMonth, String endYear, String endMonth, String opbkId, String bgbkId, String serchStrs){
		return export(ext, startYear, startMonth, endYear, endMonth, opbkId, bgbkId, serchStrs);
	}

	public Map<String, String> export(String ext, String startYear, String startMonth, String endYear, String endMonth, String opbkId, String bgbkId, String serchStrs){
		Map<String, String> rtnMap = null;
		
		try{
			rtnMap = new HashMap<String, String>();
			rtnMap.put("serchStrs", serchStrs);
			Map<String, Object> params = new HashMap<String, Object>();
			//「營業月份區間」為必填
			params.put("V_TXDT",
					"營業月份：" +
					DateTimeUtils.convertDate(DateTimeUtils.convertDate(startYear+startMonth,"yyyyMM","yyyyMM"),"yyyyMM","yyy/MM") + " ~ " + 
					DateTimeUtils.convertDate(DateTimeUtils.convertDate(endYear+endMonth,"yyyyMM","yyyyMM"),"yyyyMM","yyy/MM")
			);
			params.put("V_PRINT_DATE", DateTimeUtils.convertDate(DateTimeUtils.INTERCONVERSION, zDateHandler.getTheDate(), "yyyy/MM/dd", "yyy/MM/dd"));
			//System.out.println("params >> " + params);
			
			Map queryParam = this.getConditionData(startYear, startMonth, endYear, endMonth, opbkId, bgbkId);
			//System.out.println("queryParam >> " + queryParam);
			
			//List list = rponblocktab_Dao.getSt_1_Detail_Data_ForRpt(queryParam.get("sqlPath").toString(), (List<String>) queryParam.get("values"));
			List list = rponblocktab_Dao.getRptData(getSQL((String)queryParam.get("sqlPath")), (List<String>)queryParam.get("values"));
			
			int pathType = Arguments.getStringArg("ISWINOPEN").equals("Y")?RptUtils.C_PATH :RptUtils.R_PATH;
			String outputFilePath = "";
			if(ext.equals("xls")){
				outputFilePath = RptUtils.export(RptUtils.COLLECTION,pathType, "ex_st_3", "st_3", params, list, 2);
			}else if(ext.equals("pdf")){
				outputFilePath = RptUtils.export(RptUtils.COLLECTION,pathType, "st_3", "st_3", params, list);
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
	
	public Map getConditionData(String startYear, String startMonth, String endYear, String endMonth, String opbkId, String bgbkId) throws Exception{
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
			int i = 0;
			String start_year = "";
			String end_year = "";
			
			for(String key : params.keySet()){
				if(StrUtils.isNotEmpty(params.get(key))){
					
					if(key.equals("START_YEAR")){
						start_year = params.get(key);
					}
					else if(key.equals("START_MONTH")){
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
		sql.append("with temp1 as ( ");
		sql.append("SELECT * FROM EACHUSER.RPMONTHSUMTAB WHERE ");
		sql.append((StrUtils.isEmpty(condition)?"":condition));
		sql.append(" ) ");
		sql.append("SELECT ");
		sql.append("A.BGBK_ID, ");
		sql.append("A.RESULTSTATUS, ");//A成功 ,  R失敗
		sql.append("B.SendCNT, ");
		sql.append("B.SendAMT, ");
		sql.append("C.OutCNT, ");
		sql.append("C.OutAMT, ");
		sql.append("D.InCNT, ");
		sql.append("D.InAMT ");
		sql.append("From ( ");
		sql.append("SELECT ");//所有總行
		sql.append("VARCHAR(BGBK_ID) BGBK_ID, ");
		sql.append("(CASE VARCHAR(RESULTSTATUS) WHEN 'A' THEN '成功' WHEN 'R' THEN '失敗' WHEN 'P' THEN '未完成' ELSE '未定義' END) AS RESULTSTATUS FROM temp1 ");
		sql.append("Group by BGBK_ID, RESULTSTATUS ) A ");
		sql.append("Left join ( ");
		sql.append("SELECT ");//發動行
		sql.append("VARCHAR(BGBK_ID) BGBK_ID, ");
		sql.append("(CASE VARCHAR(RESULTSTATUS) WHEN 'A' THEN '成功' WHEN 'R' THEN '失敗' WHEN 'P' THEN '未完成' ELSE '未定義' END) AS RESULTSTATUS, ");
		sql.append("sum(CNT) SendCNT, ");
		sql.append("sum(AMT) SendAMT ");
		sql.append("FROM temp1 Where op_type='S' ");
		sql.append("Group by    BGBK_ID, RESULTSTATUS) B ");
		sql.append("on B.BGBK_ID=A.BGBK_ID  and B.RESULTSTATUS=A.RESULTSTATUS ");
		sql.append("Left join ( ");
		sql.append("SELECT ");//扣款行
		sql.append("VARCHAR(BGBK_ID) BGBK_ID, ");
		sql.append("(CASE VARCHAR(RESULTSTATUS) WHEN 'A' THEN '成功' WHEN 'R' THEN '失敗' WHEN 'P' THEN '未完成' ELSE '未定義' END) AS RESULTSTATUS, ");
		sql.append("sum(CNT) OutCNT, ");
		sql.append("sum(AMT) OutAMT ");
		sql.append("FROM temp1 Where op_type='O' ");
		sql.append("Group by   BGBK_ID,  RESULTSTATUS) C ");
		sql.append("on C.BGBK_ID=A.BGBK_ID and C.RESULTSTATUS=A.RESULTSTATUS ");
		sql.append("Left join ( ");
		sql.append("SELECT ");//入帳行
		sql.append("VARCHAR(BGBK_ID) BGBK_ID, ");
		sql.append("(CASE VARCHAR(RESULTSTATUS) WHEN 'A' THEN '成功' WHEN 'R' THEN '失敗' WHEN 'P' THEN '未完成' ELSE '未定義' END) AS RESULTSTATUS, ");
		sql.append("sum(CNT) InCNT, ");
		sql.append("sum(AMT)  InAMT ");
		sql.append("FROM temp1 Where op_type='I' ");
		sql.append("Group by    BGBK_ID, RESULTSTATUS ) D ");
		sql.append("on D.BGBK_ID=A.BGBK_ID  and D.RESULTSTATUS=A.RESULTSTATUS ");
		sql.append("Order by BGBK_ID, RESULTSTATUS ");
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
