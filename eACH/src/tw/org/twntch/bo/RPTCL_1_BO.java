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

public class RPTCL_1_BO {

	private RPONBLOCKTAB_Dao rponblocktab_Dao;
	private BANK_GROUP_Dao bank_group_Dao;
	public Map<String, String> export(String txdt, String bgbk_id, String clearingPhase, String opt_bank, String serchStrs ){
		Map<String, String> rtnMap = null;
		String outputFilePath ="";
		try{
			rtnMap = new HashMap<String, String>();
			rtnMap.put("serchStrs", serchStrs);
			Map<String, Object> params = new HashMap<String, Object>();
//			顯示區塊
			params.put("V_TXDT",DateTimeUtils.convertDate(DateTimeUtils.convertDate(txdt,"yyyyMMdd","yyyyMMdd"),"yyyyMMdd","營業日期: yyy/MM/dd"));
			System.out.println("bgbk_id>>"+bgbk_id);
			params.put("V_CLEARINGPHASE",clearingPhase);
			params.put("V_PRINT_DATE",zDateHandler.getODDate());
			params.put("V_PRINT_TIME",zDateHandler.getTheTime());
			System.out.println("params >> " + params);
			Map map = this.getConditionData(txdt, bgbk_id, clearingPhase, opt_bank);
			String sql = getSQL(map.get("sqlPath").toString());
			List<Map> list = rponblocktab_Dao.getTx_Detail_Data_ForRpt(sql, (List<String>) map.get("values"));
			int pathType = Arguments.getStringArg("ISWINOPEN").equals("Y")?RptUtils.C_PATH :RptUtils.R_PATH;
			outputFilePath = RptUtils.export(RptUtils.COLLECTION,pathType, "cl_1", "cl_1", params, list);
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
		
		sql.append("SELECT ");
		sql.append("VARCHAR(A.BIZDATE) AS BIZDATE, VARCHAR(A.CLEARINGPHASE) AS CLEARINGPHASE, VARCHAR(A.BGBK_ID) AS BGBK_ID, ");
		sql.append("COALESCE((SELECT BGBK_NAME FROM BANK_GROUP WHERE BGBK_ID = A.BGBK_ID),'') AS BGBK_NAME, ");
		sql.append("SUM(A.RECVCNT) AS RECVCNT, SUM(A.RECVAMT) AS RECVAMT, SUM(A.PAYCNT) AS PAYCNT, SUM(A.PAYAMT) AS PAYAMT, ");
		sql.append("SUM(A.RVSRECVCNT) AS RVSRECVCNT, SUM(A.RVSRECVAMT) AS RVSRECVAMT, SUM(A.RVSPAYCNT) AS RVSPAYCNT, SUM(A.RVSPAYAMT) AS RVSPAYAMT, ");
		sql.append("SUM(A.RECVAMT + A.RVSRECVAMT) IN_TOL, SUM(A.PAYAMT + A.RVSPAYAMT) OUT_TOL, SUM((A.RECVAMT + A.RVSRECVAMT) + (A.PAYAMT + A.RVSPAYAMT)) DIF_TOL ");
		sql.append("FROM RPONCLEARINGTAB AS A ");
		sql.append((StrUtils.isNotEmpty(sqlPath)?sqlPath:""));
		sql.append("GROUP BY A.BIZDATE, A.CLEARINGPHASE, A.BGBK_ID ");
		sql.append("ORDER BY A.BIZDATE, A.CLEARINGPHASE, A.BGBK_ID ");
		
		/*
		sql.append(" SELECT VARCHAR(a.BGBK_ID) BGBK_ID , coalesce((select bgbk_name from bank_group where bgbk_id=a.BGBK_ID),'') BGBK_NAME ");
//		sql.append(" ,a.CLEARINGPHASE || '' CLEARINGPHASE ,a.PCODE ,b.BUSINESS_TYPE_ID , b.BUSINESS_TYPE_NAME ");
		sql.append(" ,VARCHAR(a.CLEARINGPHASE) CLEARINGPHASE  ");
		sql.append(" ,SUM(a.RECVCNT) RECVCNT , SUM(a.RECVAMT) RECVAMT ,SUM(a.PAYCNT) PAYCNT , SUM(a.PAYAMT ) PAYAMT ,SUM(a.RVSRECVCNT) RVSRECVCNT , SUM(a.RVSRECVAMT) RVSRECVAMT ,SUM(a.RVSPAYCNT) RVSPAYCNT ,SUM(a.RVSPAYAMT) RVSPAYAMT ");
//		sql.append(" ,SUM(a.RECVAMT+a.RVSRECVAMT) in_tol, SUM(a.PAYAMT+a.RVSPAYAMT) out_tol,SUM((a.RECVAMT+a.RVSRECVAMT)-(a.PAYAMT+a.RVSPAYAMT)) dif_tol  ");
		sql.append(" ,SUM(a.RECVAMT+a.RVSRECVAMT) in_tol, SUM(a.PAYAMT+a.RVSPAYAMT) out_tol,SUM((a.RECVAMT+a.RVSRECVAMT)+(a.PAYAMT+a.RVSPAYAMT)) dif_tol  ");
		sql.append(" FROM  RPONCLEARINGTAB a");
		sql.append(sqlPath);
//		sql.append(" GROUP BY a.CLEARINGPHASE  ,a.BGBK_ID , a.BRBK_ID");
//		sql.append(" ORDER BY a.BGBK_ID  , a.BRBK_ID ,a.CLEARINGPHASE  ");
		sql.append(" GROUP BY a.CLEARINGPHASE  ,a.BGBK_ID ");
		sql.append(" ORDER BY a.CLEARINGPHASE  ,a.BGBK_ID  ");
		*/
		
		System.out.println("getSQL>>"+sql);
		return sql.toString();
	}
	
	public Map getConditionData(String txdt, String bgbk_id, String clearingPhase, String opt_bank) throws Exception{
		StringBuffer sql = new StringBuffer();
		Map<String, String> params = new LinkedHashMap<String, String> ();
		Map retmap = new LinkedHashMap();
		List<String> values = new LinkedList<String>(); 
		try {
			params.put("A.BGBK_ID", bgbk_id);
			params.put("A.OPBK_ID", opt_bank);
			params.put("A.CLEARINGPHASE", clearingPhase);
			params.put("A.BIZDATE", txdt);
			
			int i = 0;
			for(String key :params.keySet()){
				if(StrUtils.isNotEmpty(params.get(key))){
					if(i==0){sql.append(" WHERE ");}
					if(i!=0){sql.append(" AND ");}
					if(key.equals("A.BIZDATE")){
						sql.append( key+" = ? ");
						values.add(DateTimeUtils.convertDate(params.get(key), "yyyymmdd", "yyyymmdd"));
//						sql.append( key+" LIKE ? " );
//						values.add("%"+ DateTimeUtils.convertDate(params.get(key), "yyyymmdd", "yyyymmdd")+"%");
					}else{
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
	
	/**
	 * 取得總行清單
	 * @return
	 */
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
	 * 取得發動行清單
	 * @return
	 */
	public List<LabelValueBean> getOptBgbkIdList(){
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
