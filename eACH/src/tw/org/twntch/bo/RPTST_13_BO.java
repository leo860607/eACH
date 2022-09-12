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

public class RPTST_13_BO {
	private RPONBLOCKTAB_Dao rponblocktab_Dao;
	private BANK_GROUP_Dao bank_group_Dao;
	
	public Map<String, String> export(String startDate, String endDate, String clearingPhase, String opbk, String opbkId, String serchStrs){
		Map<String, String> rtnMap = null;
		String outputFilePath = "";
		try{
			rtnMap = new HashMap<String, String>();
			rtnMap.put("serchStrs", serchStrs);
			Map<String, Object> params = new HashMap<String, Object>();
			//顯示區塊
			if(startDate.equals(endDate)){
				params.put("V_TXDT", "上傳營業日：" + DateTimeUtils.convertDate(DateTimeUtils.NOT_INTERCONVERSION, startDate, "yyyymmdd", "yyy/mm/dd"));
			}else{
				params.put("V_TXDT", "上傳營業日區間：" + DateTimeUtils.convertDate(DateTimeUtils.NOT_INTERCONVERSION, startDate, "yyyymmdd", "yyy/mm/dd") + "~" + DateTimeUtils.convertDate(DateTimeUtils.NOT_INTERCONVERSION, endDate, "yyyymmdd", "yyy/mm/dd"));
			}
			params.put("V_CLEARINGPHASE", StrUtils.isEmpty(clearingPhase)?"全部":clearingPhase);
			params.put("V_OPBK_NAME", StrUtils.isEmpty(opbk)?"全部":opbk);
			params.put("V_PRINT_DATE",DateTimeUtils.convertDate(DateTimeUtils.INTERCONVERSION, zDateHandler.getTheDate(), "yyyy/MM/dd", "yyy/MM/dd"));
			params.put("V_PRINT_TIME",zDateHandler.getTheTime());
			//System.out.println("params >> " + params);
			
			Map map = this.getConditionData(startDate, endDate, clearingPhase, opbkId);
			String sql = getSQL(map.get("sqlPath").toString(), "", getOrderBySql(Integer.valueOf(0)));
			List list = rponblocktab_Dao.getRptData(sql, (List<String>) map.get("values"));
			int pathType = Arguments.getStringArg("ISWINOPEN").equals("Y")?RptUtils.C_PATH :RptUtils.R_PATH;
			outputFilePath = RptUtils.export(RptUtils.COLLECTION,pathType, "st_13", "st_13", params, list);
			
			System.out.println("RPTST_13_BO.sql >> " + sql.replaceAll("\\n", "<br/>"));
			System.out.println("RPTST_13_BO.val >> " + map.get("values"));
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
		sql.append(" ORDER BY A.ACQUIREID, A.LASTMODIFYDT ");
		return sql.toString();
	}
	
	public Map getConditionData(String startDate, String endDate, String clearingPhase, String opbkId) throws Exception{
		StringBuffer sql = new StringBuffer();
		Map<String, String> params = new LinkedHashMap<String, String> ();
		Map retmap = new LinkedHashMap();
		List<String> values = new LinkedList<String>();
		
		try {
			if(startDate.equals(endDate)){
				params.put("BIZDATE", DateTimeUtils.convertDate(startDate, "yyyymmdd", "yyyymmdd"));
			}else{
				params.put("START_DATE", startDate);
				params.put("END_DATE", endDate);
			}
			params.put("CLEARINGPHASE", clearingPhase);
			params.put("ACQUIREID", opbkId);
			
			int i = 0;
			for(String key :params.keySet()){
				if(StrUtils.isNotEmpty(params.get(key))){
					if(i==0){sql.append(" WHERE ");}
					if(i!=0){sql.append(" AND ");}
					if(key.equals("START_DATE")){
						sql.append("BIZDATE >= ?");
						values.add(DateTimeUtils.convertDate(params.get(key), "yyyymmdd", "yyyymmdd"));
					}else if(key.equals("END_DATE")){
						sql.append("BIZDATE <= ?");
						values.add(DateTimeUtils.convertDate(params.get(key), "yyyymmdd", "yyyymmdd"));
					}else{
						sql.append(key + " = ?");
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
	
	public String getSQL(String sqlPath , String spSql , String orderbySql){
		StringBuffer sql = new StringBuffer();
		sql.append("WITH SRC AS ( ");
		sql.append("    SELECT ");
		sql.append("    BIZDATE, CLEARINGPHASE, PROCSEQ, BATCHSEQ, TOTALCOUNT, TOTALAMT, REJECTCOUNT, REJECTAMT, ACCEPTCOUNT, ACCEPTAMT ");
		sql.append("    , PROCCOUNT, PROCAMT, LASTMODIFYDT, ACHFLAG, FILEREJECT, ACQUIREID, FILELAYOUT ");
		sql.append("    FROM FLCONTROLTAB ");
		sql.append("	" + sqlPath);
		sql.append("), TEMP AS ( ");
		sql.append("    SELECT ");
		sql.append("    (CASE A.RESULTSTATUS WHEN 'A' THEN 1 ELSE 0 END) AS AC, ");
		sql.append("    (CASE A.RESULTSTATUS WHEN 'P' THEN 1 ELSE 0 END) AS PC, ");
		sql.append("    (CASE A.RESULTSTATUS WHEN 'R' THEN 1 ELSE 0 END) AS RC, ");
		sql.append("    A.FLBATCHSEQ, A.CLEARINGPHASE, A.RESULTSTATUS, DECIMAL(EACHUSER.ISNUMERIC(A.TXAMT)) AS TXAMT ");
		sql.append("    FROM RPONBLOCKTAB AS A RIGHT JOIN SRC AS B ON A.FLBIZDATE = B.BIZDATE AND A.FLPROCSEQ = B.PROCSEQ AND A.FLBATCHSEQ = B.BATCHSEQ ");
		sql.append("    WHERE A.FLBATCHSEQ IS NOT NULL AND A.FLBATCHSEQ <> '' ");
		sql.append(") ");

		sql.append("SELECT A.CLEARINGPHASE ");
		sql.append(", COALESCE((ACQUIREID || '-' || (SELECT BGBK_NAME FROM BANK_GROUP WHERE BGBK_ID = A.ACQUIREID)), '') AS ACQUIREID ");
		sql.append(", COALESCE((SUBSTR(TRANS_DATE(TRANSLATE('ABCDEFGH',A.LASTMODIFYDT,'ABCDEFGHIJKLMNOPQ'),'T','/'), 2, 9) || ' ' || TRANSLATE('IJ:KL:MN',A.LASTMODIFYDT,'ABCDEFGHIJKLMNOPQ')), '') AS LASTMODIFYDT ");
		sql.append(", (A.BATCHSEQ || '.P' || A.FILELAYOUT) AS FILENAME ");
		sql.append(", SUBSTR(TRANS_DATE(A.BIZDATE, 'T', '/'), 2) AS BIZDATE ");
		sql.append(", A.CLEARINGPHASE ");
		sql.append(", (CASE WHEN COALESCE(A.ACHFLAG, '') = '*' THEN '是' ELSE '否' END) AS ACHFLAG ");
		sql.append(", (CASE WHEN COALESCE(A.FILEREJECT, '') = 'R' THEN '是' ELSE '否' END) AS FILEREJECT ");
		sql.append(", COALESCE(A.TOTALCOUNT, 0) AS TOTALCOUNT, COALESCE(A.TOTALAMT, 0) AS TOTALAMT ");
		sql.append(", COALESCE(A.REJECTCOUNT, 0) AS REJECTCOUNT, COALESCE(A.REJECTAMT, 0) AS REJECTAMT ");
		sql.append(", COALESCE(A.ACCEPTCOUNT, 0) AS ACCEPTCOUNT, COALESCE(A.ACCEPTAMT, 0) AS ACCEPTAMT ");
		sql.append(", COALESCE((SELECT SUM(AC) FROM TEMP WHERE FLBATCHSEQ = A.BATCHSEQ AND BIZDATE = A.BIZDATE AND CLEARINGPHASE = A.CLEARINGPHASE), 0) AS COUNT_A ");
		sql.append(", COALESCE((SELECT SUM(TXAMT) FROM TEMP WHERE FLBATCHSEQ = A.BATCHSEQ AND BIZDATE = A.BIZDATE AND CLEARINGPHASE = A.CLEARINGPHASE AND AC = 1), 0) AS AMT_A ");
		sql.append(", COALESCE((SELECT SUM(RC) FROM TEMP WHERE FLBATCHSEQ = A.BATCHSEQ AND BIZDATE = A.BIZDATE AND CLEARINGPHASE = A.CLEARINGPHASE), 0) AS COUNT_R ");
		sql.append(", COALESCE((SELECT SUM(TXAMT) FROM TEMP WHERE FLBATCHSEQ = A.BATCHSEQ AND BIZDATE = A.BIZDATE AND CLEARINGPHASE = A.CLEARINGPHASE AND RC = 1), 0) AS AMT_R ");
		sql.append(", COALESCE(A.PROCCOUNT, 0) AS PROCCOUNT, COALESCE(A.PROCAMT, 0) AS PROCAMT ");
		sql.append("FROM SRC AS A ");
		sql.append(orderbySql);
		
		//System.out.println("getSQL >> " + sql);
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
