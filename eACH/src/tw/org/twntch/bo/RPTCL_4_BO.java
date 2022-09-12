package tw.org.twntch.bo;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.struts.util.LabelValueBean;

import tw.org.twntch.db.dao.hibernate.BANK_GROUP_Dao;
import tw.org.twntch.db.dao.hibernate.EACH_BATCH_NOTIFY_Dao;
import tw.org.twntch.db.dao.hibernate.RPONBLOCKTAB_Dao;
import tw.org.twntch.po.BANK_GROUP;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.RptUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.zDateHandler;

public class RPTCL_4_BO {

	private RPONBLOCKTAB_Dao rponblocktab_Dao;
	private BANK_GROUP_Dao bank_group_Dao;
	private EACH_BATCH_NOTIFY_Dao batch_notify_Dao ;
	public Map<String, String> export(String txdt, String ctbk_id, String clearingPhase, String serchStrs){
		Map<String, String> rtnMap = null;
		String outputFilePath ="";
		try{
			rtnMap = new HashMap<String, String>();
			rtnMap.put("serchStrs", serchStrs);
			Map<String, Object> params = new HashMap<String, Object>();
//			顯示區塊
			params.put("V_TXDT",DateTimeUtils.convertDate(DateTimeUtils.convertDate(txdt,"yyyyMMdd","yyyyMMdd"),"yyyyMMdd","營業日期: yyy/MM/dd"));
			System.out.println("ctbk_id>>"+ctbk_id);
//			System.out.println("opt_bank>>"+opt_bank);
			if(StrUtils.isEmpty(ctbk_id)){
				params.put("V_OPT_BANK", "全部");
			}else{
//				params.put("V_OPT_BANK", opt_bank);
			}
			params.put("V_CLEARINGPHASE",clearingPhase);
			params.put("V_PRINT_DATE",zDateHandler.getODDate());
			params.put("V_PRINT_TIME",zDateHandler.getTheTime());
			System.out.println("params >> " + params);
			Map map = this.getConditionData(txdt, ctbk_id, clearingPhase);
			String sql = getSQL(map.get("sqlPath").toString());
			//System.out.println("sql=====>"+sql);
//			List<Map> list = rponblocktab_Dao.getCl_1_Detail_Data_ForRpt(map.get("sqlPath").toString(), ctbk_id);
			List<Map> list = rponblocktab_Dao.getRptData(sql, (List<String>) map.get("values"));
//			for(Map map1:list){
//				System.out.println("CLEARINGPHASE>>"+map1.get("CLEARINGPHASE"));
//			}
			int pathType = Arguments.getStringArg("ISWINOPEN").equals("Y")?RptUtils.C_PATH :RptUtils.R_PATH;
			outputFilePath = RptUtils.export(RptUtils.COLLECTION,pathType, "cl_4", "cl_4", params, list);
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
	
	//20150902 HUANGPU 此報表因已和「代理清算行報表列印」功能合併，故不因應更換清算行而異動語法
	public String getSQL(String sqlPath){
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT coalesce( b.CTBK_ID ,'')  CTBK_ID , coalesce((SELECT BGBK_NAME  FROM BANK_GROUP  WHERE BGBK_ID = b.CTBK_ID )  ,'') CTBGBK_NAME "); 
	    sql.append(" , coalesce((SELECT CTBK_ACCT  FROM BANK_GROUP  WHERE BGBK_ID = b.CTBK_ID )  ,'') CTBK_ACCT ,VARCHAR(a.CLEARINGPHASE) CLEARINGPHASE ");
	    sql.append(" , VARCHAR(a.BGBK_ID) BGBK_ID , (select coalesce(bgbk_name,'') from bank_group where bgbk_id=a.BGBK_ID) BGBK_NAME ");
//	    20150430 edit by hugo req by 李建利 此張報表所有應付金額都要正向表示 差額才有負號
//	    sql.append(" ,SUM(a.RECVCNT) RECVCNT , SUM(a.RECVAMT) RECVAMT ,SUM(a.PAYCNT) PAYCNT , SUM(a.PAYAMT ) PAYAMT ,SUM(a.RVSRECVCNT) RVSRECVCNT , SUM(a.RVSRECVAMT) RVSRECVAMT ,SUM(a.RVSPAYCNT) RVSPAYCNT ,SUM(a.RVSPAYAMT) RVSPAYAMT ");
//	    sql.append(" ,SUM(a.RECVAMT+a.RVSRECVAMT) in_tol, SUM(a.PAYAMT+a.RVSPAYAMT) out_tol,SUM((a.RECVAMT+a.RVSRECVAMT)-(a.PAYAMT+a.RVSPAYAMT)) dif_tol ");
	    sql.append(" ,SUM(a.RECVCNT) RECVCNT , SUM(a.RECVAMT) RECVAMT , SUM(a.PAYCNT) PAYCNT , abs(SUM(a.PAYAMT )) PAYAMT ,SUM(a.RVSRECVCNT) RVSRECVCNT , SUM(a.RVSRECVAMT) RVSRECVAMT ,SUM(a.RVSPAYCNT) RVSPAYCNT , abs(SUM(a.RVSPAYAMT)) RVSPAYAMT ");
	    sql.append(" ,SUM(a.RECVAMT+a.RVSRECVAMT) in_tol, abs(SUM(a.PAYAMT+a.RVSPAYAMT)) out_tol,SUM((a.RECVAMT+a.RVSRECVAMT)+(a.PAYAMT+a.RVSPAYAMT)) dif_tol ");
	    sql.append(" FROM EACHUSER.RPONCLEARINGTAB a ");
	    sql.append(" RIGHT JOIN BANK_GROUP b ON a.BGBK_ID = b.BGBK_ID and b.BGBK_ID != b.CTBK_ID ");
	    sql.append(sqlPath);
	    sql.append(" GROUP BY b.CTBK_ID , a.BGBK_ID  ,CLEARINGPHASE ");
	    sql.append(" ORDER BY CLEARINGPHASE ,  b.CTBK_ID , a.BGBK_ID ");
		System.out.println("getSQL>>"+sql);
		return sql.toString();
	}
	
	public Map getConditionData(String txdt,String ctbk_id ,String clearingPhase) throws Exception{
		StringBuffer sql = new StringBuffer();
		Map<String, String> params = new LinkedHashMap<String, String> ();
		Map retmap = new LinkedHashMap();
		List<String> values = new LinkedList<String>(); 
		try {
			params.put("a.CLEARINGPHASE", clearingPhase);
			params.put("a.BIZDATE", txdt);
			params.put("b.CTBK_ID", ctbk_id);
			int i = 0;
			for(String key :params.keySet()){
				if(StrUtils.isNotEmpty(params.get(key))){
					if(i==0){sql.append(" WHERE ");}
					if(i!=0){sql.append(" AND ");}
					if(key.equals("a.BIZDATE")){
						sql.append( key+" = ? ");
						values.add(DateTimeUtils.convertDate(params.get(key), "yyyymmdd", "yyyymmdd"));
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
			System.out.println("sql>>"+sql.toString());
			System.out.println("getConditionDataSQL_Path>>"+retmap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e.toString()) ; 
		}
		return retmap;
	}
	
//	/**
//	 * 取得總行清單
//	 * @return
//	 */
//	public List<LabelValueBean> getBgbkIdList(){
//		List<BANK_GROUP> list = bank_group_Dao.getBgbkIdList();
//		List<LabelValueBean> beanList = new LinkedList<LabelValueBean>();
//		LabelValueBean bean = null;
//		for(BANK_GROUP po : list){
//			bean = new LabelValueBean(po.getBGBK_ID() + " - " + po.getBGBK_NAME(), po.getBGBK_ID());
//			beanList.add(bean);
//		}
//		System.out.println("beanList>>"+beanList);
//		return beanList;
//	}
	/**
	 * 取得清算行清單
	 * @return
	 */
	public List<LabelValueBean> getOptCtbkIdList(){
		List<BANK_GROUP> list = bank_group_Dao.getctbkIdList_all();
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
	
//	TODO 未完成
	public String checkRPT_BizDate(Map<String,String> param){
		String bizdate = StrUtils.isEmpty(param.get("bizdate")) ?"":param.get("bizdate");
		String clearingPhase = StrUtils.isEmpty(param.get("clearingPhase")) ?"01,02":param.get("clearingPhase");
		Map<String,String> retmap = new HashMap<String,String>();
		List<String> list = Arrays.asList(clearingPhase.split(",")); 
		boolean res = false ;
		for(String str : list){
			StringBuffer sql = new StringBuffer();
			if(StrUtils.isNotEmpty(param.get("bizdate"))){
				sql.append(" BIZDATE = '"+bizdate+"'");
				sql.append(" AND  CLEARINGPHASE = '"+str+"'");
				res = batch_notify_Dao.checkRPT_BizDate(sql.toString());
				if(!res){
					retmap.put("result", "FALSE");
					retmap.put("msg", "無法列印報表，目前營業日="+bizdate+"清算階段="+str+"尚未發佈結算通知");
					return JSONUtils.map2json(retmap);
				}
			}
			
		}
		
		return null;
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

	public EACH_BATCH_NOTIFY_Dao getBatch_notify_Dao() {
		return batch_notify_Dao;
	}

	public void setBatch_notify_Dao(EACH_BATCH_NOTIFY_Dao batch_notify_Dao) {
		this.batch_notify_Dao = batch_notify_Dao;
	}

	
	
	
}
