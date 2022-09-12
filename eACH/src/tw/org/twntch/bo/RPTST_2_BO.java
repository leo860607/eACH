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
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.RptUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.zDateHandler;

public class RPTST_2_BO {
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
	
	public Map<String, String> ex_export(String ext, String startYear, String startMonth, String endYear, String endMonth, String opbkId, String bgbkId, String resultStatus, String senderId, String serchStrs){
		return export(ext, startYear, startMonth, endYear, endMonth, opbkId, bgbkId, resultStatus, senderId, serchStrs);
	}

	public Map<String, String> export(String ext, String startYear, String startMonth, String endYear, String endMonth, String opbkId, String bgbkId, String resultStatus, String senderId, String serchStrs){
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
			params.put("V_SENDERID", StrUtils.isEmpty(senderId)?"全部":senderId);
			params.put("V_PRINT_DATE", DateTimeUtils.convertDate(DateTimeUtils.INTERCONVERSION, zDateHandler.getTheDate(), "yyyy/MM/dd", "yyy/MM/dd"));
			if(StrUtils.isEmpty(opbkId)){
				params.put("V_OPT_BANK", "全部");
			}else{
				List<BANK_OPBK> bank_opbk = bank_opbk_Dao.getDataByBgbkId(opbkId, "");
				String opbk_name = "";
				for (int i = 0; i < bank_opbk.size(); i++) {
					opbk_name = bank_opbk.get(i).getOPBK_NAME();
				}
				params.put("V_OPT_BANK", opbk_name);
			}
			
			//System.out.println("params >> " + params);
			
			Map queryParam = this.getConditionData(startYear, startMonth, endYear, endMonth, opbkId, bgbkId, resultStatus, senderId);
			//System.out.println("queryParam >> " + queryParam);
			
			//List list = rponblocktab_Dao.getSt_1_Detail_Data_ForRpt(queryParam.get("sqlPath").toString(), (List<String>) queryParam.get("values"));
			List list = rponblocktab_Dao.getRptData(getSQL((String)queryParam.get("sqlPath")), (List<String>)queryParam.get("values"));
			
			int pathType = Arguments.getStringArg("ISWINOPEN").equals("Y")?RptUtils.C_PATH :RptUtils.R_PATH;
			String outputFilePath = "";
			if(ext.equals("xls")){
				outputFilePath = RptUtils.export(RptUtils.COLLECTION,pathType, "ex_st_2", "st_2", params, list, 2);
			}else if(ext.equals("pdf")){
				outputFilePath = RptUtils.export(RptUtils.COLLECTION,pathType, "st_2", "st_2", params, list);
			}
			System.out.println("SQL>>>"+getSQL((String)queryParam.get("sqlPath")));
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
	
	public Map getConditionData(String startYear, String startMonth, String endYear, String endMonth, String opbkId, String bgbkId, String resultStatus, String senderId) throws Exception{
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
			params.put("RESULTSTATUS", resultStatus);
			params.put("SENDERID", senderId);
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
						sql.append( "BIZDATE >= ? " );
						values.add(DateTimeUtils.convertDate(start_year + params.get(key), "yyyyMM", "yyyyMM") + "01");
					}
					else if(key.equals("END_YEAR")){
						end_year = params.get(key);
					}
					else if(key.equals("END_MONTH")){
						sql.append(" AND ");
						sql.append( "BIZDATE <= ? " );
						values.add(DateTimeUtils.convertDate(end_year + params.get(key), "yyyyMM", "yyyyMM") + "31");
					}
					else{
						sql.append(" AND ");
						sql.append( key+" = ? ");
						values.add(params.get(key));
					}
					i++;
				}
			}
			List<String> values2 = new LinkedList<String>();
			
			//RPDAILYSUMTAB,RPPENDINGDAILYSUMTAB 搜尋條件相同
			values2.addAll(values);
			values2.addAll(values);
			
			retmap.put("sqlPath", sql.toString());
			retmap.put("values", values2);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e.toString()) ; 
		}
		return retmap;
	}
	
	public String getSQL(String condition){
		StringBuffer sql = new StringBuffer();
		sql.append("WITH temp1 AS ( ");
		sql.append("SELECT * FROM ");
		sql.append("EACHUSER.RPDAILYSUMTAB ");
		sql.append("Where RESULTSTATUS !='P' "+(StrUtils.isEmpty(condition)?"":condition));
		
		sql.append(" UNION All ");
		sql.append("SELECT * FROM ");
		sql.append("EACHUSER.RPPENDINGDAILYSUMTAB ");
		sql.append("Where RESULTSTATUS !='P'"+(StrUtils.isEmpty(condition)?"":condition));
		
		sql.append(")");//AS
		sql.append("Select ");
		sql.append("A.BIZDATE, ");
		sql.append("A.RESULTSTATUS, ");//A成功 ,  R失敗
		sql.append("COALESCE(B.SendCNT,0) AS SendCNT, ");
		sql.append("COALESCE(B.SendAMT,0) AS SendAMT, ");
		sql.append("COALESCE(C.OutCNT, 0) AS OutCNT, ");
		sql.append("COALESCE(C.OutAMT,0) AS OutAMT, ");
		sql.append("COALESCE(D.InCNT,0) AS InCNT, ");
		sql.append("COALESCE(D.InAMT,0) AS InAMT ");
		sql.append("From (");//所有分行
		sql.append("SELECT VARCHAR(SUBSTR(BIZDATE,1,4)-1911) || '/' ||  VARCHAR(SUBSTR(BIZDATE,5,2)) || '/' || VARCHAR(SUBSTR(BIZDATE,7,2)) AS  BIZDATE, ");
		sql.append("(CASE VARCHAR(RESULTSTATUS) WHEN 'A' THEN '成功' WHEN 'R' THEN '失敗' WHEN 'P' THEN '未完成' ELSE '未定義' END) AS RESULTSTATUS ");
		sql.append("FROM temp1 ");
		sql.append("Group by BIZDATE, RESULTSTATUS ) A ");
		sql.append("Left join ");
		sql.append("(");//發動行
		sql.append("SELECT ");
		sql.append("VARCHAR(SUBSTR(BIZDATE,1,4)-1911) || '/' ||  VARCHAR(SUBSTR(BIZDATE,5,2)) || '/' || VARCHAR(SUBSTR(BIZDATE,7,2)) AS  BIZDATE, ");
		sql.append("(CASE VARCHAR(RESULTSTATUS) WHEN 'A' THEN '成功' WHEN 'R' THEN '失敗' WHEN 'P' THEN '未完成' ELSE '未定義' END) AS RESULTSTATUS, ");
		sql.append("sum(CNT) SendCNT, ");
		sql.append("sum(AMT) SendAMT ");
		sql.append("FROM temp1 ");
		sql.append("Where op_type='S' ");
		sql.append("Group by         BIZDATE, RESULTSTATUS) B ");
		sql.append("on B.BIZDATE=A.BIZDATE  and B.RESULTSTATUS=A.RESULTSTATUS ");
		sql.append("Left join ");
		sql.append("(");//扣款行
		sql.append("SELECT ");
		sql.append("VARCHAR(SUBSTR(BIZDATE,1,4)-1911) || '/' ||  VARCHAR(SUBSTR(BIZDATE,5,2)) || '/' || VARCHAR(SUBSTR(BIZDATE,7,2)) AS  BIZDATE, ");
		sql.append("(CASE VARCHAR(RESULTSTATUS) WHEN 'A' THEN '成功' WHEN 'R' THEN '失敗' WHEN 'P' THEN '未完成' ELSE '未定義' END) AS RESULTSTATUS, ");
		sql.append("sum(CNT) OutCNT, ");
		sql.append("sum(AMT) OutAMT ");
		sql.append("FROM temp1 ");
		sql.append("Where op_type='O' ");
		sql.append("Group by         BIZDATE,  RESULTSTATUS) C ");
		sql.append("on C.BIZDATE=A.BIZDATE and C.RESULTSTATUS=A.RESULTSTATUS ");
		sql.append("Left join ");
		sql.append("(");//入帳行
		sql.append("SELECT VARCHAR(SUBSTR(BIZDATE,1,4)-1911) || '/' ||  VARCHAR(SUBSTR(BIZDATE,5,2)) || '/' || VARCHAR(SUBSTR(BIZDATE,7,2)) AS  BIZDATE, ");
		sql.append("(CASE VARCHAR(RESULTSTATUS) WHEN 'A' THEN '成功' WHEN 'R' THEN '失敗' WHEN 'P' THEN '未完成' ELSE '未定義' END) AS  RESULTSTATUS, sum(CNT) InCNT, sum(AMT)  InAMT ");
		sql.append("FROM temp1 ");
		sql.append("Where op_type='I' ");
		sql.append("Group by         BIZDATE, RESULTSTATUS ) D ");
		sql.append("on D.BIZDATE=A.BIZDATE  and D.RESULTSTATUS=A.RESULTSTATUS ");
		sql.append("Order by BIZDATE, RESULTSTATUS");
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
