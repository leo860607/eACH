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
import tw.org.twntch.po.EACHSYSSTATUSTAB;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.RptUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.zDateHandler;

public class RPTCL_3_BO {

	private RPONBLOCKTAB_Dao rponblocktab_Dao;
	private BANK_GROUP_Dao bank_group_Dao;
	public Map<String, String> export(String txdt, String clearingPhase, String serchStrs){
		Map<String, String> rtnMap = null;
		String outputFilePath ="";
		try{
			rtnMap = new HashMap<String, String>();
			rtnMap.put("serchStrs", serchStrs);
			Map<String, Object> params = new HashMap<String, Object>();
//			顯示區塊
			params.put("V_TXDT",DateTimeUtils.convertDate(DateTimeUtils.convertDate(txdt,"yyyyMMdd","yyyyMMdd"),"yyyyMMdd","營業日期: yyy/MM/dd"));
			params.put("V_CLEARINGPHASE",clearingPhase);
			params.put("V_PRINT_DATE",zDateHandler.getODDate());
			params.put("V_PRINT_TIME",zDateHandler.getTheTime());
			System.out.println("params >> " + params);
			Map map = this.getConditionData(txdt, clearingPhase);			
//			List<Map> list = rponblocktab_Dao.getCl_3_Detail_Data_ForRpt(map.get("sqlPath").toString());
			
			//String sql = getSQL(map.get("sqlPath").toString());
			String sql = getSQL2(map.get("sqlPath").toString());
			List<Map> list = rponblocktab_Dao.getRptData(sql, (List<String>) map.get("values"));
//			for(Map map1:list){
//				System.out.println("CLEARINGPHASE>>"+map1.get("CLEARINGPHASE"));
//			}
			int pathType = Arguments.getStringArg("ISWINOPEN").equals("Y")?RptUtils.C_PATH :RptUtils.R_PATH;
			outputFilePath = RptUtils.export(RptUtils.COLLECTION,pathType, "cl_3", "cl_3", params, list);
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
	
	public String getSQL(String sqlPath){
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT coalesce( b.CTBK_ID ,'')  CTBK_ID , coalesce((SELECT BGBK_NAME  FROM BANK_GROUP  WHERE BGBK_ID = b.CTBK_ID )  ,'') CTBGBK_NAME ");
		sql.append(" , coalesce((SELECT CTBK_ACCT  FROM BANK_GROUP  WHERE BGBK_ID = b.CTBK_ID )  ,'') CTBK_ACCT ,VARCHAR(a.CLEARINGPHASE) CLEARINGPHASE   ");
		sql.append(" ,SUM(a.RECVCNT) RECVCNT , SUM(a.RECVAMT) RECVAMT ,SUM(a.PAYCNT) PAYCNT , SUM(a.PAYAMT ) PAYAMT ,SUM(a.RVSRECVCNT) RVSRECVCNT , SUM(a.RVSRECVAMT) RVSRECVAMT ,SUM(a.RVSPAYCNT) RVSPAYCNT ,SUM(a.RVSPAYAMT) RVSPAYAMT ");
//		sql.append(" ,SUM(a.RECVAMT+a.RVSRECVAMT) in_tol, SUM(a.PAYAMT+a.RVSPAYAMT) out_tol , SUM((a.RECVAMT+a.RVSRECVAMT)-(a.PAYAMT+a.RVSPAYAMT)) dif_tol  ");
		sql.append(" ,SUM(a.RECVAMT+a.RVSRECVAMT) in_tol, SUM(a.PAYAMT+a.RVSPAYAMT) out_tol , SUM((a.RECVAMT+a.RVSRECVAMT)+(a.PAYAMT+a.RVSPAYAMT)) dif_tol  ");
		sql.append(" FROM  RPONCLEARINGTAB a");
		sql.append(" LEFT JOIN BANK_GROUP b ON a.BGBK_ID = b.BGBK_ID ");
		sql.append(sqlPath);
		sql.append(" GROUP BY b.CTBK_ID  ,CLEARINGPHASE ");
		sql.append(" ORDER BY CLEARINGPHASE ,  b.CTBK_ID ");
		
		
		System.out.println("getSQL>>"+sql);
		return sql.toString();
	}
	
	public String getSQL2(String sqlPath){
		StringBuffer sql = new StringBuffer();
		sql.append("WITH TEMP AS ( ");
		sql.append("    SELECT A.*, ");
		sql.append("    (A.RECVAMT+A.RVSRECVAMT) AS IN_TOL, (A.PAYAMT+A.RVSPAYAMT) AS OUT_TOL, ");
		sql.append("    ((A.RECVAMT+A.RVSRECVAMT)+(A.PAYAMT+A.RVSPAYAMT)) AS DIF_TOL ");
		sql.append("    FROM ( ");
		sql.append("        SELECT T.*, GET_CUR_CTBKID(BGBK_ID, TRANS_DATE(BIZDATE,'T',''), 0) AS CTBK_ID ");
		sql.append("        FROM EACHUSER.RPONCLEARINGTAB AS T ");
		sql.append("        " + (StrUtils.isNotEmpty(sqlPath)?sqlPath:"") + " ");
		sql.append("    ) AS A ");
		sql.append(") ");
		
		StringBuffer selectStr = new StringBuffer();
		selectStr.append("SELECT "); 
		selectStr.append("COALESCE(CTBK_ID, '') CTBK_ID, ");
		selectStr.append("COALESCE((SELECT CTBK_ACCT FROM BANK_GROUP WHERE BGBK_ID = TEMP.CTBK_ID) , '') CTBK_ACCT, "); 
		selectStr.append("VARCHAR(CLEARINGPHASE) CLEARINGPHASE, ");
		selectStr.append("SUM(RECVCNT) RECVCNT, SUM(RECVAMT) RECVAMT, SUM(PAYCNT) PAYCNT, SUM(PAYAMT) PAYAMT, "); 
		selectStr.append("SUM(RVSRECVCNT) RVSRECVCNT, SUM(RVSRECVAMT) RVSRECVAMT, SUM(RVSPAYCNT) RVSPAYCNT, SUM(RVSPAYAMT) RVSPAYAMT, ");
		selectStr.append("SUM(RECVAMT+RVSRECVAMT) IN_TOL, SUM(PAYAMT+RVSPAYAMT) OUT_TOL, SUM((RECVAMT+RVSRECVAMT)+(PAYAMT+RVSPAYAMT)) DIF_TOL, ");
		
		sql.append(selectStr);
		sql.append("'代理清算('||COALESCE((SELECT BGBK_NAME FROM BANK_GROUP WHERE BGBK_ID = TEMP.CTBK_ID) , '')||')' CTBGBK_NAME ");
		sql.append("FROM TEMP WHERE CTBK_ID <> BGBK_ID GROUP BY CTBK_ID, CLEARINGPHASE ");
		sql.append("UNION ALL ");
		sql.append(selectStr);
		sql.append("COALESCE((SELECT BGBK_NAME FROM BANK_GROUP WHERE BGBK_ID = TEMP.CTBK_ID) , '') CTBGBK_NAME ");
		sql.append("FROM TEMP WHERE CTBK_ID = BGBK_ID GROUP BY CTBK_ID, CLEARINGPHASE ");
		sql.append("ORDER BY CLEARINGPHASE, CTBK_ID");
		System.out.println("getSQL2 >> "+sql);
		
		/* 20150923 HUANGPU edit
		sql.append("SELECT ");
		sql.append("COALESCE(A.CTBK_ID, '') CTBK_ID, ");
		sql.append("COALESCE((SELECT BGBK_NAME FROM BANK_GROUP WHERE BGBK_ID = A.CTBK_ID) , '') CTBGBK_NAME, ");
		sql.append("COALESCE((SELECT CTBK_ACCT FROM BANK_GROUP WHERE BGBK_ID = A.CTBK_ID) , '') CTBK_ACCT, ");
		sql.append("VARCHAR(A.CLEARINGPHASE) CLEARINGPHASE, ");
		sql.append("SUM(A.RECVCNT) RECVCNT, SUM(A.RECVAMT) RECVAMT, SUM(A.PAYCNT) PAYCNT, SUM(A.PAYAMT) PAYAMT, SUM(A.RVSRECVCNT) RVSRECVCNT, SUM(A.RVSRECVAMT) RVSRECVAMT, "); 
		sql.append("SUM(A.RVSPAYCNT) RVSPAYCNT, SUM(A.RVSPAYAMT) RVSPAYAMT,SUM(A.RECVAMT+A.RVSRECVAMT) IN_TOL, SUM(A.PAYAMT+A.RVSPAYAMT) OUT_TOL, ");
		sql.append("SUM((A.RECVAMT+A.RVSRECVAMT)+(A.PAYAMT+A.RVSPAYAMT)) DIF_TOL ");
		sql.append("FROM ( ");
		sql.append("		SELECT T.*, GET_CUR_CTBKID(BGBK_ID, BIZDATE, 0) AS CTBK_ID ");
		sql.append("		FROM EACHUSER.RPONCLEARINGTAB AS T ");
		sql.append("		" + (StrUtils.isNotEmpty(sqlPath)?sqlPath:""));
		sql.append(") A ");
		sql.append("GROUP BY A.CTBK_ID, CLEARINGPHASE ");
		sql.append("ORDER BY CLEARINGPHASE, A.CTBK_ID ");
		System.out.println("getSQL2 >> "+sql);
		*/
		return sql.toString();
	}
	
	public Map getConditionData(String txdt, String clearingPhase) throws Exception{
		StringBuffer sql = new StringBuffer();
		Map<String, String> params = new LinkedHashMap<String, String> ();
		Map retmap = new LinkedHashMap();
		List<String> values = new LinkedList<String>(); 
		try {
			params.put("T.CLEARINGPHASE", clearingPhase);
			params.put("T.BIZDATE", txdt);
			int i = 0;
			for(String key :params.keySet()){
				if(StrUtils.isNotEmpty(params.get(key))){
					if(i==0){sql.append(" WHERE ");}
					if(i!=0){sql.append(" AND ");}
					if(key.equals("T.BIZDATE")){
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
			System.out.println("sql>>"+sql.toString());
			System.out.println("getConditionDataSQL_Path>>"+retmap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e.toString()) ; 
		}
		return retmap;
	}
	
	/**
	 * 取得前一個營業日
	 * @return
	 */
	public String getBeforeDate(){
		List<EACHSYSSTATUSTAB> list = rponblocktab_Dao.get_before_Date();		
		String beDte =list.get(0).getPREVDATE();
		int yyy = Integer.parseInt(beDte.substring(0, 4))-1911;
		beDte =  "0"+Integer.toString(yyy)+ beDte.substring(4);
		System.out.println("beDte>>"+beDte);
		return beDte;
	}
	
	/**
	 * 取得清算行清單
	 * @return
	 */
	public List<LabelValueBean> getOptCtbkIdList(){
		List<BANK_GROUP> list = bank_group_Dao.getctbkIdList_all();
		List<LabelValueBean> beanList = new LinkedList<LabelValueBean>();
		LabelValueBean bean = null;
		for(BANK_GROUP po : list){
			bean = new LabelValueBean(po.getCTBK_ID() + " - " + po.getBGBK_NAME(), po.getCTBK_ID());
			beanList.add(bean);
		}
		System.out.println("beanList>>"+beanList);
		return beanList;
	}
	
	/**
	 * 此API目前無用，本來想法是組好後丟到ireport組SQL
	 * @param txdt
	 * @param senderacquire
	 * @param clearingPhase
	 * @return
	 */
	public String getConditionStr(String txdt, String senderacquire, String clearingPhase){
		StringBuffer sql = new StringBuffer();
		Map<String, String> params = new LinkedHashMap<String, String> ();
		params.put("a.SENDERACQUIRE", senderacquire);
		params.put("a.CLEARINGPHASE", clearingPhase);
		params.put("a.TXDT", txdt);
		int i = 0;
		for(String key :params.keySet()){
			if(i!=0){sql.append(" AND ");}
			if(StrUtils.isNotEmpty(params.get(key))){
				if(i==0){sql.append(" WHERE ");}
				if(key.equals("TXDT")){
					sql.append( key+" LIKE '%"+ DateTimeUtils.convertDate(params.get(key), "yyyymmdd", "yyyymmdd")+"%' " );
					System.out.println("TXDT.sql"+sql);
				}else{
					sql.append( key+" = '"+ params.get(key)+"' ");
				}
				i++;
			}
		}
		System.out.println("sql>>"+sql);
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
