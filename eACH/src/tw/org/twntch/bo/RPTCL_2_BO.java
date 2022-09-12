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
import tw.org.twntch.po.BANK_OPBK;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.RptUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.zDateHandler;

public class RPTCL_2_BO {

	private RPONBLOCKTAB_Dao rponblocktab_Dao;
	private BANK_GROUP_Dao bank_group_Dao;
	private BANK_OPBK_BO bank_opbk_bo;
	
	public Map<String, String> export(String txdt, String bgbk_id, String clearingPhase ,  String optbk_id , String serchStrs ){
		Map<String, String> rtnMap = null;
		String outputFilePath ="";
		try{
			rtnMap = new HashMap<String, String>();
			rtnMap.put("serchStrs", serchStrs);//e
			Map<String, Object> params = new HashMap<String, Object>();
			System.out.println("optbk_id>>"+optbk_id);
			if(StrUtils.isNotEmpty(optbk_id)){
				StringBuffer strtmp = new StringBuffer();
				/*
				List<BANK_GROUP> list = bank_group_Dao.find(" FROM tw.org.twntch.po.BANK_GROUP WHERE OPBK_ID  = ? " , optbk_id);
				int i = 0;
				for( BANK_GROUP po :list ){
					if(i!=0){ strtmp.append(",") ;}
					strtmp.append(po.getBGBK_ID()) ;
					i++;
				}
				*/
				List<BANK_OPBK> list = bank_opbk_bo.getBgbkList(optbk_id, txdt, txdt);
				int i = 0;
				for( BANK_OPBK po : list ){
					if(i!=0){ strtmp.append(",") ;}
					strtmp.append(po.getBGBK_ID());
					i++;
				}
				bgbk_id = strtmp.toString();
			}
//			顯示區塊
			params.put("V_TXDT",DateTimeUtils.convertDate(DateTimeUtils.convertDate(txdt,"yyyyMMdd","yyyyMMdd"),"yyyyMMdd","營業日期: yyy/MM/dd"));
			params.put("V_CLEARINGPHASE",clearingPhase);
			params.put("V_PRINT_DATE",zDateHandler.getODDate());
			params.put("V_PRINT_TIME",zDateHandler.getTheTime());
			System.out.println("params >> " + params);
			Map map = this.getConditionData(txdt, bgbk_id, clearingPhase);
			String sql = getSQL(map.get("sqlPath").toString());
			List<Map> list = rponblocktab_Dao.getTx_Detail_Data_ForRpt(sql, (List<String>) map.get("values"));
			int pathType = Arguments.getStringArg("ISWINOPEN").equals("Y")?RptUtils.C_PATH :RptUtils.R_PATH;
			outputFilePath = RptUtils.export(RptUtils.COLLECTION,pathType, "cl_2", "cl_2", params, list);
			System.out.println("bgbk_id>>"+bgbk_id);
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
		sql.append(" SELECT VARCHAR(a.BGBK_ID) BGBK_ID , coalesce((select bgbk_name from bank_group where bgbk_id=a.BGBK_ID),'') BGBK_NAME ");
//		sql.append(" ,VARCHAR(a.BRBK_ID) BRBK_ID , coalesce((select brbk_name from bank_branch where brbk_id=a.BRBK_ID),'') BRBK_NAME ");
		sql.append(" ,VARCHAR(a.CLEARINGPHASE) CLEARINGPHASE ,T.EACH_TXN_ID , T.EACH_TXN_NAME ");
		sql.append(" ,SUM(a.RECVCNT) RECVCNT , SUM(a.RECVAMT) RECVAMT ,SUM(a.PAYCNT) PAYCNT , SUM(a.PAYAMT ) PAYAMT ,SUM(a.RVSRECVCNT) RVSRECVCNT , SUM(a.RVSRECVAMT) RVSRECVAMT ,SUM(a.RVSPAYCNT) RVSPAYCNT ,SUM(a.RVSPAYAMT) RVSPAYAMT ");
//		sql.append(" ,SUM(a.RECVAMT+a.RVSRECVAMT) in_tol, SUM(a.PAYAMT+a.RVSPAYAMT) out_tol , SUM((a.RECVAMT+a.RVSRECVAMT)-(a.PAYAMT+a.RVSPAYAMT)) dif_tol  ");
		sql.append(" ,SUM(a.RECVAMT+a.RVSRECVAMT) in_tol, SUM(a.PAYAMT+a.RVSPAYAMT) out_tol , SUM((a.RECVAMT+a.RVSRECVAMT)+(a.PAYAMT+a.RVSPAYAMT)) dif_tol  ");
		sql.append(" FROM  RPONCLEARINGTAB a");
		sql.append(" LEFT JOIN EACH_TXN_CODE T ON a.PCODE = T.EACH_TXN_ID ");
		sql.append(" LEFT JOIN BUSINESS_TYPE B ON T.BUSINESS_TYPE_ID=B.BUSINESS_TYPE_ID ");
		sql.append(sqlPath);
		sql.append(" GROUP BY a.CLEARINGPHASE  ,a.BGBK_ID  ,T.EACH_TXN_ID , T.EACH_TXN_NAME ");
		sql.append(" ORDER BY a.BGBK_ID , a.CLEARINGPHASE , T.EACH_TXN_ID , T.EACH_TXN_NAME ");
//		sql.append(" GROUP BY a.CLEARINGPHASE  ,a.BGBK_ID , a.BRBK_ID ,b.BUSINESS_TYPE_ID , b.BUSINESS_TYPE_NAME ");
//		sql.append(" ORDER BY   a.BGBK_ID ,a.BRBK_ID  ,  a.CLEARINGPHASE , b.BUSINESS_TYPE_ID , b.BUSINESS_TYPE_NAME ");
		
		
		System.out.println("getSQL>>"+sql);
		return sql.toString();
	}
	
	public Map getConditionData(String txdt, String bgbk_id, String clearingPhase) throws Exception{
		StringBuffer sql = new StringBuffer();
		Map<String, String> params = new LinkedHashMap<String, String> ();
		Map retmap = new LinkedHashMap();
		List<String> values = new LinkedList<String>(); 
		try {
			params.put("a.BGBK_ID", bgbk_id);
//			params.put("a.BANKID", senderacquire);
			params.put("a.CLEARINGPHASE", clearingPhase);
			params.put("a.BIZDATE", txdt);
			int i = 0;
			for(String key :params.keySet()){
				if(StrUtils.isNotEmpty(params.get(key))){
					if(i==0){sql.append(" WHERE ");}
					if(i!=0){sql.append(" AND ");}
					if(key.equals("a.BIZDATE")){
						sql.append( key+" = ? ");
						values.add(DateTimeUtils.convertDate(params.get(key), "yyyymmdd", "yyyymmdd"));
//						sql.append( key+" LIKE ? " );
//						values.add("%"+ DateTimeUtils.convertDate(params.get(key), "yyyymmdd", "yyyymmdd")+"%");
					}else if(key.equals("a.BGBK_ID") && params.get(key).indexOf(",") >0){
						sql.append( key+" IN ( "+params.get(key)+") ");
//						values.add(params.get(key));
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

	public BANK_OPBK_BO getBank_opbk_bo() {
		return bank_opbk_bo;
	}

	public void setBank_opbk_bo(BANK_OPBK_BO bank_opbk_bo) {
		this.bank_opbk_bo = bank_opbk_bo;
	}
	
}
