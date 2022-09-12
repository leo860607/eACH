package tw.org.twntch.bo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;
import org.hibernate.Query;

import com.ibm.db2.jcc.am.po;

import tw.org.twntch.db.dao.hibernate.AP_PAUSE_Dao;
import tw.org.twntch.db.dao.hibernate.AP_STATUS_Dao;
import tw.org.twntch.db.dao.hibernate.EACHSYSSTATUSTAB_Dao;
import tw.org.twntch.db.dao.hibernate.EACH_BATCH_DEF_Dao;
import tw.org.twntch.db.dao.hibernate.EACH_BATCH_Dao;
import tw.org.twntch.db.dao.hibernate.EACH_BATCH_NOTIFY_Dao;
import tw.org.twntch.db.dao.hibernate.EACH_BATCH_STATUS_Dao;
import tw.org.twntch.db.dao.hibernate.SETTLEMENTLOGTAB_Dao;
import tw.org.twntch.db.dao.hibernate.SYS_PARA_Dao;
import tw.org.twntch.db.dataaccess.DataAccessException;
import tw.org.twntch.db.dataaccess.ExecuteSQL;
import tw.org.twntch.po.BULLETIN;
import tw.org.twntch.po.EACHSYSSTATUSTAB;
import tw.org.twntch.po.EACH_BATCH_DEF;
import tw.org.twntch.po.EACH_BATCH_NOTIFY;
import tw.org.twntch.po.EACH_BATCH_NOTIFY_PK;
import tw.org.twntch.po.EACH_BATCH_STATUS;
import tw.org.twntch.po.EACH_BATCH_STATUS_PK;
import tw.org.twntch.po.SC_COMPANY_PROFILE;
import tw.org.twntch.po.SC_COMPANY_PROFILE_PK;
import tw.org.twntch.po.SETTLEMENTLOGTAB;
import tw.org.twntch.po.SYS_PARA;
import tw.org.twntch.socket.Message;
import tw.org.twntch.socket.MessageConverter;
import tw.org.twntch.socket.Message.Body;
import tw.org.twntch.socket.Message.Header;
import tw.org.twntch.socket.SocketClient;
import tw.org.twntch.util.BeanUtils;
import tw.org.twntch.util.CodeUtils;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.RunSchedule;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.zDateHandler;

public class EACH_BATCH_BO {
private EACH_BATCH_DEF_Dao batch_def_Dao  ;
private EACH_BATCH_NOTIFY_Dao batch_notify_Dao  ;
private EACH_BATCH_STATUS_Dao batch_status_Dao  ;
private EACH_BATCH_Dao each_batch_Dao  ;
private SYS_PARA_Dao  sys_para_Dao ;
private EACHSYSSTATUSTAB_Dao eachsysstatustab_Dao ;
private EACHSYSSTATUSTAB_BO eachsysstatustab_bo ; 
private AP_PAUSE_Dao ap_pause_Dao ; 
private SocketClient socketClient ; 
private SETTLEMENTLOGTAB_Dao settlementlogtab_Dao ; 
private WK_DATE_BO wk_date_bo ;
private BAT_RPT_BO bat_rpt_bo ;
private BAT_RPT_TH_BO bat_rpt_th_bo ;
private BAT_AGENT_DATA_BO bat_agent_data_bo;
private RunSchedule runschedule ;
private EACH_USERLOG_BO userlog_bo;
private BULLETIN_BO bulletin_bo;
private SC_COMPANY_PROFILE_BO sc_com_bo;
private Thread rpt_thread ;
private Logger logger = Logger.getLogger(getClass());


public String getNotifyData(Map<String, String> param){
	String bizdate =StrUtils.isNotEmpty(param.get("BIZDATE"))? param.get("BIZDATE"):"";
	String clearingphase =StrUtils.isNotEmpty(param.get("CLEARINGPHASE"))? param.get("CLEARINGPHASE"):"";
	String json = "";
//	StringBuffer sql = new StringBuffer();
//	sql.append(" FROM tw.org.twntch.po.SETTLEMENTLOGTAB WHERE PCODE IN( '5200','5201') AND  AND BIZDATE = ? AND CLEARINGPHASE = ? ");
//	List<SETTLEMENTLOGTAB> list = settlementlogtab_Dao.find(sql.toString(), bizdate ,clearingphase );
	bizdate = DateTimeUtils.convertDate(1, bizdate, "yyyy-MM-dd", "yyyyMMdd");
	System.out.println("bizdate>>"+bizdate);
	List<Map> list = settlementlogtab_Dao.getData(bizdate, clearingphase);
	System.out.println("list>>"+list);
	if(list != null && list.size() !=0){
		json = JSONUtils.toJson(list);
	}
	System.out.println("json>>"+json);
	return json;
}


public String getNotifyResult(Map<String, String> param){
	String bizdate =StrUtils.isNotEmpty(param.get("BIZDATE"))? param.get("BIZDATE"):"";
	String clearingphase =StrUtils.isNotEmpty(param.get("CLEARINGPHASE"))? param.get("CLEARINGPHASE"):"";
	String json = "{}";
//	StringBuffer sql = new StringBuffer();
//	sql.append(" FROM tw.org.twntch.po.SETTLEMENTLOGTAB WHERE PCODE IN( '5200','5201') AND  AND BIZDATE = ? AND CLEARINGPHASE = ? ");
//	List<SETTLEMENTLOGTAB> list = settlementlogtab_Dao.find(sql.toString(), bizdate ,clearingphase );
	try {
		bizdate = DateTimeUtils.convertDate(1, bizdate, "yyyy-MM-dd", "yyyyMMdd");
		System.out.println("bizdate>>"+bizdate);
		List<EACH_BATCH_NOTIFY> list = batch_notify_Dao.find(" FROM tw.org.twntch.po.EACH_BATCH_NOTIFY WHERE BIZDATE = ? ", bizdate);
		if(list != null){
			json = JSONUtils.toJson(list);
		}
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		logger.debug("getNotifyResult.Exception>>"+e);
	}
	System.out.println("json>>"+json);
	logger.debug("getNotifyResult.json>>"+json);
	logger.debug("getNotifyResult.bizdate>>"+bizdate);
	return json;
}

public String getNotifyResultII(Map<String, String> param){
	String bizdate =StrUtils.isNotEmpty(param.get("BIZDATE"))? param.get("BIZDATE"):"";
	String clearingphase =StrUtils.isNotEmpty(param.get("CLEARINGPHASE"))? param.get("CLEARINGPHASE"):"";
	String json = "{}";
//	StringBuffer sql = new StringBuffer();
//	sql.append(" FROM tw.org.twntch.po.SETTLEMENTLOGTAB WHERE PCODE IN( '5200','5201') AND  AND BIZDATE = ? AND CLEARINGPHASE = ? ");
//	List<SETTLEMENTLOGTAB> list = settlementlogtab_Dao.find(sql.toString(), bizdate ,clearingphase );
	bizdate = DateTimeUtils.convertDate(1, bizdate, "yyyy-MM-dd", "yyyyMMdd");
	System.out.println("bizdate>>"+bizdate);
	EACH_BATCH_NOTIFY_PK id = new EACH_BATCH_NOTIFY_PK(bizdate, clearingphase);
	EACH_BATCH_NOTIFY po = batch_notify_Dao.get(id);
	if(po != null){
		json = JSONUtils.toJson(po);
	}
	System.out.println("json>>"+json);
	return json;
}



public void saveStatus(EACH_BATCH_STATUS po ){
	try {
		batch_status_Dao.aop_save(po);
	}catch (Exception e){
		// TODO Auto-generated catch block
		e.printStackTrace();
		throw e ;
	}
}

	public String search_toJson2(Map<String, String> param){
		String bizdate =StrUtils.isNotEmpty(param.get("BIZDATE"))? param.get("BIZDATE"):"";
		String clearingphase =StrUtils.isNotEmpty(param.get("CLEARINGPHASE"))? param.get("CLEARINGPHASE"):"";
		String batch_proc_seq =StrUtils.isNotEmpty(param.get("BATCH_PROC_SEQ"))? param.get("BATCH_PROC_SEQ"):"";
		return JSONUtils.toJson(search(bizdate , clearingphase ));
	}
	
	//為了不被SearchAop.java攔截記錄在票交端紀錄，另外用 search_toJson2()處理
	public String search_toJson(Map<String, String> param){
		String bizdate =StrUtils.isNotEmpty(param.get("BIZDATE"))? param.get("BIZDATE"):"";
		String clearingphase =StrUtils.isNotEmpty(param.get("CLEARINGPHASE"))? param.get("CLEARINGPHASE"):"";
		String batch_proc_seq =StrUtils.isNotEmpty(param.get("BATCH_PROC_SEQ"))? param.get("BATCH_PROC_SEQ"):"";
		return JSONUtils.toJson(search(bizdate , clearingphase ));
	}
	
	public List<EACH_BATCH_STATUS> search(String bizdate ,String clearingphase ){
		
		List<EACH_BATCH_STATUS> list = null ;
		try {
			System.out.println("bizdate>>"+bizdate+",clearingphase>>"+clearingphase);
			if(StrUtils.isNotEmpty(bizdate)){
				bizdate = DateTimeUtils.convertDate(1, bizdate, "yyyy-MM-dd", "yyyyMMdd");
//				bizdate = bizdate.replace("-", "");
			}
//			20150608  edit by hugo  req by 李建利    月批次轉檔 要擺最後
//			list = batch_status_Dao.find(" FROM tw.org.twntch.po.EACH_BATCH_STATUS WHERE BIZDATE = ? AND CLEARINGPHASE = ? " , bizdate ,clearingphase );
			list = batch_status_Dao.find(" FROM tw.org.twntch.po.EACH_BATCH_STATUS WHERE BIZDATE = ? AND CLEARINGPHASE = ? ORDER BY PROC_TYPE , BATCH_PROC_SEQ " , bizdate ,clearingphase );
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("list>>"+list);
		list = list.size() == 0 ? null : list;
		return list;
	}
	
	
	
	public String getServerTime(Map<String, String> param){
		Map<String , String> retMap = new HashMap<String , String>();
		String json = "";
		String sysTime = "";
		try {
			sysTime = zDateHandler.getTheTime();
			retMap.put("sysTime", sysTime);
		} catch (Exception e) {
			e.printStackTrace();
		}
		json = JSONUtils.map2json(retMap);
		System.out.println("json>>"+json);
		return json;
	}
	public String chgSerarchCondition(Map<String, String> param){
		String tmpsysTime = StrUtils.isNotEmpty(param.get("tmpsysTime"))?param.get("tmpsysTime"):"";
		Map<String , String> retMap = new HashMap<String , String>();
		String json = "";
		String sysTime = "";
		retMap.put("result", "FALSE");
		retMap.put("msg", "chgSerarchCondition 位執行任何動作");
		try {
			if(StrUtils.isEmpty(tmpsysTime)){
				retMap.put("msg", "tmpsysTime>>"+tmpsysTime+"不換日");
				json = JSONUtils.map2json(retMap);
				return json;
			}
			if(zDateHandler.compareDiffTime(tmpsysTime, "12:00:00")>0){
				retMap.put("msg", "tmpsysTime大於12:00>>"+tmpsysTime+"，不換日");
				json = JSONUtils.map2json(retMap);
				return json;
			}
			if(zDateHandler.compareDiffTime(tmpsysTime, "12:00:00") < 0  && zDateHandler.isInTimeRange(zDateHandler.getTheTime(), "12:00:00", "12:01:00")){
//			if(zDateHandler.compareDiffTime(tmpsysTime, "12:00:00") > 0  && zDateHandler.isInTimeRange(zDateHandler.getTheTime(), "16:14:00", "16:15:00")){
				retMap.put("result", "TRUE");
				retMap.put("msg", "更新查詢條件");
				json = JSONUtils.map2json(retMap);
				return json;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			retMap.put("result", "ERROR");
			retMap.put("errmsg", "系統異常:"+e);
		}
		json = JSONUtils.map2json(retMap);
		System.out.println("json>>"+json);
		return json;
	}
	
	
	/**
	 * 取得SYS_PARA 的1 2 階段清算時間
	 * @return
	 */
	public String searchSYS(Map<String, String> param){
		List<SYS_PARA> list = null ;
		String json = "";
		try {
			list = sys_para_Dao.getTopOne();
		} catch (Exception e) {
			e.printStackTrace();
		}
		json = JSONUtils.toJson(list);
		System.out.println("json>>"+json);
		return json;
	}
	
	/**
	 * 更新SYS_PARA 的1 2 階段清算時間
	 * @param param
	 * @return
	 */
	public String saveSYS(Map<String, String> param){
		String RP_CLEARPHASE1_TIME = StrUtils.isEmpty(param.get("RP_CLEARPHASE1_TIME"))?"":param.get("RP_CLEARPHASE1_TIME");
		String RP_CLEARPHASE2_TIME = StrUtils.isEmpty(param.get("RP_CLEARPHASE2_TIME"))?"":param.get("RP_CLEARPHASE2_TIME");
		String action = StrUtils.isEmpty(param.get("action"))?"":param.get("action");
		String AP1 = StrUtils.isEmpty(param.get("AP1"))?"":param.get("AP1");
		String AP2 = StrUtils.isEmpty(param.get("AP2"))?"":param.get("AP2");
		String AP1_ISRUN = StrUtils.isEmpty(param.get("AP1_ISRUN"))?"":param.get("AP1_ISRUN");
		String AP2_ISRUN = StrUtils.isEmpty(param.get("AP2_ISRUN"))?"":param.get("AP2_ISRUN");
		List<SYS_PARA> list = null ;
		Map<String , String> retMap = new HashMap<String , String>();
		Map<String, String> oldMap = null; //e
		Map<String, String> newMap = null; //e
		retMap.put("result", "FALSE");
		SYS_PARA po  = null;
		try {
			list = sys_para_Dao.getTopOne();
			if(list != null && list.size()!=0){
				po = list.get(0);
				oldMap = BeanUtils.describe(po);
				po.setRP_CLEARPHASE1_TIME(RP_CLEARPHASE1_TIME);
				po.setRP_CLEARPHASE2_TIME(RP_CLEARPHASE2_TIME);
				po.setAP1(AP1);
				po.setAP2(AP2);
				System.out.println("AP1_ISRUN>>"+AP1_ISRUN);
				System.out.println("AP2_ISRUN>>"+AP2_ISRUN);
				po.setAP1_ISRUN(AP1_ISRUN);
				po.setAP2_ISRUN(AP2_ISRUN);
				
				SYS.RP_CLEARPHASE1_TIME = RP_CLEARPHASE1_TIME;
				SYS.AP1 = AP1;
				SYS.AP2 = AP2;
				SYS.AP1_ISRUN = AP1_ISRUN;
				SYS.AP2_ISRUN = AP2_ISRUN;
	//			sys_para_Dao.save(po);
//				sys_para_Dao.saveII(po, oldMap , new HashMap<String , String>());
				sys_para_Dao.aop_save(po);
				retMap.put("result", "TRUE");
				retMap.put("msg", "儲存成功");
				retMap.put("target", "search");
				newMap = BeanUtils.describe(po);
				userlog_bo.com_Dif_Val(newMap, oldMap);
				Map<String,Map> map  = userlog_bo.com_Dif_Val(newMap, oldMap);
//				newMap = map.get("newmap");
//				oldMap = map.get("oldmap");
				System.out.println("newmap>>"+map.get("newmap"));
				userlog_bo.genericLog("R", "儲存成功", action,map.get("newmap") , map.get("oldmap"));
			}else{
				retMap.put("msg", "儲存失敗 ，查無資料，請先至參數檔新增一筆資料");
				userlog_bo.genericLog("R", "修改-儲存失敗，查無資料，請先至參數檔新增一筆資料", action,null , null);
//				sys_para_Dao.updateFail(po, null, new HashMap<String , String>(), "修改-儲存失敗，查無資料", 1);
				return JSONUtils.map2json(retMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
			userlog_bo.genericLog("R", "修改-儲存失敗，系統異常", action,null , null);
//			sys_para_Dao.updateFail(po, null, new HashMap<String , String>(), "修改-儲存失敗，系統異常", 2);
		}
		return JSONUtils.map2json(retMap);
	}
	
	
	/**
	 * 取得定義檔中定義哪些SP
	 * @return
	 */
	public List<EACH_BATCH_DEF>  getBatDef(){
//		batch_def_Dao.getAll(); //不使用此API的原因是 如果新增資料是用其他工具新增時有可能照成取得資料時排序異常 造成後續無法作業
		List<EACH_BATCH_DEF> list = batch_def_Dao.find(" FROM tw.org.twntch.po.EACH_BATCH_DEF ORDER BY BATCH_PROC_SEQ " );
		return  list;
	}
	/**
	 * 取得定義檔中屬日排程
	 * @return
	 */
	public List<EACH_BATCH_DEF>  getBatDef_Daily(){
		List<EACH_BATCH_DEF> list = batch_def_Dao.find(" FROM tw.org.twntch.po.EACH_BATCH_DEF WHERE PROC_TYPE = 'D' ORDER BY BATCH_PROC_SEQ " );
		return  list;
	}
	/**
	 * 判短是否可以跑批，依據普鴻的onblock,onpending 是否完成
	 * @param bizdate
	 * @param clearingphase
	 * @return
	 */
	public boolean isRunBat(String bizdate , String  clearingphase ){
		boolean res = false ;
		EACH_BATCH_STATUS po = null ;
		EACH_BATCH_STATUS_PK id = null;
		try {
//			0~20屬於普鴻的排程
			List<EACH_BATCH_DEF> list = batch_def_Dao.getDataBySeqRange(bizdate, clearingphase, 0, 20);
			if(list != null && list.size()!=0){
				for(EACH_BATCH_DEF each_batch_def :list){
					id = new EACH_BATCH_STATUS_PK(bizdate, clearingphase, each_batch_def.getBATCH_PROC_SEQ());
					po = batch_status_Dao.get(id);
					if(po!=null && ( StrUtils.isNotEmpty(po.getPROC_STATUS() ) &&  po.getPROC_STATUS().equals("S"))){
						res = true ;
					}else{
						res = false ;
						break;
					}
					po =null;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			res = false ;
			logger.debug("Exception>>"+e);
		}
		return res;
	}
	/**舊的 先註解  之後可以砍掉
	 * 判短是否可以跑批，依據普鴻的onblock,onpending 是否完成
	 * @param bizdate
	 * @param clearingphase
	 * @return
	 */
//	public boolean isRunBat(String bizdate , String  clearingphase ){
//		boolean res = false ;
//		boolean seq0 = false ;
//		boolean seq1 = false ;
//		boolean seq2 = false ;
//		EACH_BATCH_STATUS po = null ;
//		EACH_BATCH_STATUS_PK id = null;
//		try {
//			id = new EACH_BATCH_STATUS_PK(bizdate, clearingphase, 0);
//			po = batch_status_Dao.get(id);
//			if(po!=null && ( StrUtils.isNotEmpty(po.getPROC_STATUS() ) &&  po.getPROC_STATUS().equals("S"))){
//				seq0 = true;
//			}
//			po= null ;
//			id = new EACH_BATCH_STATUS_PK(bizdate, clearingphase, 1);
//			po = batch_status_Dao.get(id);
//			if(po!=null && ( StrUtils.isNotEmpty(po.getPROC_STATUS() ) &&  po.getPROC_STATUS().equals("S"))){
//				seq1 = true;
//			}
//			po= null ;
//			id = new EACH_BATCH_STATUS_PK(bizdate, clearingphase, 2);
//			po = batch_status_Dao.get(id);
//			if(po!=null && ( StrUtils.isNotEmpty(po.getPROC_STATUS() ) &&  po.getPROC_STATUS().equals("S"))){
//				seq2 = true;
//			}
//			res = seq0 == true && seq1 == true && seq2 == true  ? true :false;
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return res;
//	}
//	
	

	
	
//	單獨執行批次
	public String doBatBySeq(Map<String, String> param){
		String json = "";
		String bizdate =StrUtils.isNotEmpty(param.get("BIZDATE"))? param.get("BIZDATE"):"";
		String clearingphase =StrUtils.isNotEmpty(param.get("CLEARINGPHASE"))? param.get("CLEARINGPHASE"):"";
		String batch_proc_seq =StrUtils.isNotEmpty(param.get("BATCH_PROC_SEQ"))? param.get("BATCH_PROC_SEQ"):"";
		String action =StrUtils.isNotEmpty(param.get("action"))? param.get("action"):"";
		Map<String,String> retmap = new HashMap<String,String>();
		retmap.put("result", "FALSE");
		int seq = 99;
		List<EACH_BATCH_STATUS> list = null ;
		try {
			seq = Integer.valueOf(batch_proc_seq);
			bizdate = DateTimeUtils.convertDate(1, bizdate, "yyyy-MM-dd", "yyyyMMdd");
			list = batch_status_Dao.checkStatus_BeforeThree(bizdate, clearingphase);
			List<EACH_BATCH_DEF> defList = getBatDef_Daily();
			list = list ==null? new LinkedList<EACH_BATCH_STATUS>():list;
			if(list.size() !=0){
//				for(EACH_BATCH_STATUS po :list){
//				}
				retmap.put("msg", "無法執行，尚有"+list.size()+"項，圈存相關批次尚未執行完畢");
			}else{
				EACH_BATCH_NOTIFY_PK id = new EACH_BATCH_NOTIFY_PK(bizdate, clearingphase);
				EACH_BATCH_NOTIFY po = batch_notify_Dao.get(id);
				if(po == null){
					po = new EACH_BATCH_NOTIFY();
					po.setId(id);
				}
				po.setRPT_NOTIFY("");
				batch_notify_Dao.aop_save(po);
				if(seq<=20){
					retmap = doBatByBeforeThreeSeq(bizdate, clearingphase , action, seq);
				}else{
					retmap.put("msg", "失敗，無相關api ,SEQ>>"+seq);
				}
//				switch (seq) {
//				case 0:
//					retmap = doBatByBeforeThreeSeq(bizdate, clearingphase, seq);
//					break;
//				case 1:
//					retmap = doBatByBeforeThreeSeq(bizdate, clearingphase, seq);
//					break;
//				case 2:
//					retmap = doBatByBeforeThreeSeq(bizdate, clearingphase, seq);
//					break;
////TODO 剩下的客戶無此需求 先不寫
//				default:
//					retmap.put("msg", "失敗，無相關api ,SEQ>>"+seq);
//					break;
//				}
			}
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			retmap.put("msg", "發出通知失敗，系統異常");
		}
		json = JSONUtils.map2json(retmap);
		return json;
	}
	/**
	 * 單獨執行普鴻的批次
	 * @param bizdate 格式:20150101
	 * @param clearingphase
	 * @param batch_proc_seq
	 * @return
	 */
	public Map<String,String> doBatByBeforeThreeSeq(String bizdate , String clearingphase ,String action , int batch_proc_seq){
		Map<String,String> retmap = new HashMap<String,String>();
		Map<String,String> oldmap = new HashMap<String,String>();
		retmap.put("result", "FALSE");
		try {
//				查詢要異動資料的原始狀態
				EACH_BATCH_STATUS_PK pk  = new EACH_BATCH_STATUS_PK(bizdate, clearingphase, batch_proc_seq) ;
				EACH_BATCH_STATUS each_batch_status =	batch_status_Dao.get(pk);
				oldmap = BeanUtils.describe(each_batch_status);
				oldmap.putAll(BeanUtils.describe(pk));
//				相關表格初始化 配合普鴻先初始化在call電文
				initEACH_BATCH_STATUS_By_Seq(bizdate, clearingphase, batch_proc_seq);
//				發送電文通知給GATEWAY，。
				retmap =  bat_send(bizdate, clearingphase ,batch_proc_seq);
				if(!retmap.get("result").equals("TRUE")){
					return retmap;
				}
//				初始化3以後的 
				bizdate = DateTimeUtils.convertDate(1, bizdate, "yyyyMMdd" , "yyyy-MM-dd");
				initEach_Batch_Status_MAN(bizdate, clearingphase);
				retmap.put("result", "TRUE");
				retmap.put("msg", "成功，重新執行中...");
				
				userlog_bo.genericLog("M", "成功，重新執行中...", action, null, oldmap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			retmap.put("msg", "發出通知失敗，系統異常");
			userlog_bo.genericLog("M", "發出通知失敗，系統異常", action, null, oldmap);
		}
		return retmap;
	}
	
	public boolean initEACH_BATCH_STATUS_By_Seq(String bizdate , String clearingphase , int batch_proc_seq){
		boolean result=false;
		EACH_BATCH_STATUS_PK id2= new EACH_BATCH_STATUS_PK(bizdate, clearingphase, batch_proc_seq);
		EACH_BATCH_DEF defpo = null;
		EACH_BATCH_STATUS po2 = null;
		po2 = batch_status_Dao.get(id2);
		if(po2 == null){
			po2 = new EACH_BATCH_STATUS();
			po2.setId(id2);
			defpo = batch_def_Dao.get(Integer.valueOf(batch_proc_seq));
			if(defpo != null ){
				po2.setBATCH_PROC_NAME(defpo.getBATCH_PROC_DESC());
				po2.setBATCH_PROC_DESC(defpo.getBATCH_PROC_DESC());
			}
		}
//		po2.setBEGIN_TIME(zDateHandler.getTheDateII()+" "+zDateHandler.getTheTime_MS());
		po2.setBEGIN_TIME(null);
		po2.setEND_TIME(null);
		po2.setNOTE("");
		po2.setPROC_TYPE("D");
		po2.setPROC_STATUS("");
		batch_status_Dao.aop_save(po2);
		return result;
	}
	
//	發佈結算通知
	public String doCLNotify(Map<String, String> param){
		String json = "";
		String bizdate =StrUtils.isNotEmpty(param.get("BIZDATE"))? param.get("BIZDATE"):"";
		String clearingphase =StrUtils.isNotEmpty(param.get("CLEARINGPHASE"))? param.get("CLEARINGPHASE"):"";
		String batch_proc_seq =StrUtils.isNotEmpty(param.get("BATCH_PROC_SEQ"))? param.get("BATCH_PROC_SEQ"):"";
		String action =StrUtils.isNotEmpty(param.get("action"))? param.get("action"):"";
		Map<String,String> retmap = new HashMap<String,String>();
		Map<String,String> logmap = new HashMap<String,String>();
		retmap.put("result", "FALSE");
		List<EACH_BATCH_STATUS> list = null ;
		try {
			bizdate = DateTimeUtils.convertDate(1, bizdate, "yyyy-MM-dd", "yyyyMMdd");
			list = batch_status_Dao.checkStatus_By_Seq(bizdate, clearingphase , Integer.valueOf(batch_proc_seq) );
			list = list ==null? new LinkedList<EACH_BATCH_STATUS>():list;
			if(list.size() ==0){
				retmap.put("msg", "無法執行，該項批次作業未完成");
			}else{
				EACH_BATCH_NOTIFY_PK id = new EACH_BATCH_NOTIFY_PK(bizdate, clearingphase);
				EACH_BATCH_NOTIFY po = batch_notify_Dao.get(id);
				if(po == null){
					po = new EACH_BATCH_NOTIFY();
					po.setId(id);
				}
//				最後由普鴻回寫Y或N
				po.setCLEAR_NOTIFY("P");
//				batch_notify_Dao.save(po);
				batch_notify_Dao.aop_save(po);
//				發送電文通知給GATEWAY，再轉發給各家銀行。
				retmap =  send(bizdate, clearingphase);
				if(!retmap.get("result").equals("TRUE")){
					json = JSONUtils.map2json(retmap);
					return json;
				}
				retmap.put("result", "TRUE");
				retmap.put("msg", "結算通知執行中");
				logmap.put("BIZDATE", bizdate);
				logmap.put("CLEARINGPHASE", clearingphase);
				logmap.put("action", action);
//				userlog_bo.writeLog("P", null, logmap, null);
				userlog_bo.genericLog("P", "成功" ,action , logmap, null);
//				只要送出通知給getway無論結果均發佈通告，格式:yyy-mm-dd 第x清算階段結算通知已發佈
				BULLETIN bulletin = new BULLETIN();
				bizdate = DateTimeUtils.convertDate(1, bizdate, "yyyyMMdd", "yyy-MM-dd");
				bulletin.setCHCON(bizdate+ " ， 第"+clearingphase+"清算階段結算通知已發佈 ");
				bulletin.setSAVE_DATE(zDateHandler.getTimestamp());
				bulletin.setSEND_DATE(zDateHandler.getTimestamp());
				bulletin.setSEND_STATUS("Y");
				bulletin_bo.saveNsend_BAT(bulletin,action);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.debug("doCLNotify.Exception>>"+e);
			retmap.put("msg", "發出通知失敗，系統異常");
			logmap.clear();
			logmap.put("BIZDATE", bizdate);
			logmap.put("CLEARINGPHASE", clearingphase);
			logmap.put("action", action);
			userlog_bo.genericLog("P", "失敗，發出通知失敗，系統異常:"+e, action, logmap, null);
		}
		
		json = JSONUtils.map2json(retmap);
		
		return json;
	}
//	發佈報表及檔案作業通知
	public String doNotify(Map<String, String> param){
		String json = "";
		String bizdate =StrUtils.isNotEmpty(param.get("BIZDATE"))? param.get("BIZDATE"):"";
		String clearingphase =StrUtils.isNotEmpty(param.get("CLEARINGPHASE"))? param.get("CLEARINGPHASE"):"";
		String batch_proc_seq =StrUtils.isNotEmpty(param.get("BATCH_PROC_SEQ"))? param.get("BATCH_PROC_SEQ"):"";
		String action =StrUtils.isNotEmpty(param.get("action"))? param.get("action"):"";
		Map<String,String> retmap = new HashMap<String,String>();
		Map<String,String> logmap = new HashMap<String,String>();
		retmap.put("result", "FALSE");
		List<EACH_BATCH_STATUS> list = null ;
		try {
			bizdate = DateTimeUtils.convertDate(1, bizdate, "yyyy-MM-dd", "yyyyMMdd");
			List<EACH_BATCH_DEF> defList = getBatDef_Daily();
//			bizdate = bizdate.replace("-", "");
			list = batch_status_Dao.checkStatus(bizdate, clearingphase);
			list = list ==null? new LinkedList<EACH_BATCH_STATUS>():list;
//			if(list != null && list.size() !=0){
			if( list.size() !=defList.size()){
				retmap.put("msg", "無法執行，尚有"+(defList.size()-list.size())+"項，日批次作業未完成");
			}else{
				EACH_BATCH_NOTIFY_PK id = new EACH_BATCH_NOTIFY_PK(bizdate, clearingphase);
				EACH_BATCH_NOTIFY po = batch_notify_Dao.get(id);
				if(po == null){
					po = new EACH_BATCH_NOTIFY();
					po.setId(id);
				}
				po.setRPT_NOTIFY("Y");
				batch_notify_Dao.aop_save(po);
				retmap.put("result", "TRUE");
				retmap.put("msg", "報表及檔案作業通知已執行");
				logmap.put("BIZDATE", bizdate);
				logmap.put("CLEARINGPHASE", clearingphase);
//				yyy-mm-dd 第x清算階段報表及檔案開放下載
				BULLETIN bulletin = new BULLETIN();
				bizdate = DateTimeUtils.convertDate(1, bizdate, "yyyyMMdd", "yyy-MM-dd");
				bulletin.setCHCON(bizdate+ " ， 第"+clearingphase+"清算階段報表及檔案開放下載 ");
				bulletin.setSAVE_DATE(zDateHandler.getTimestamp());
				bulletin.setSEND_DATE(zDateHandler.getTimestamp());
				bulletin.setSEND_STATUS("Y");
				bulletin_bo.saveNsend_BAT(bulletin , action);
				userlog_bo.genericLog("Q", "成功", action,logmap, null);
			}
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			retmap.put("msg", "發出通知失敗，系統異常");
			retmap.put("msg", "報表及檔案作業通知已執行");
			logmap.put("BIZDATE", bizdate);
			logmap.put("CLEARINGPHASE", clearingphase);
			userlog_bo.genericLog("Q", "失敗，發出通知失敗，系統異常:"+e,action, logmap, null);
		}
		
		json = JSONUtils.map2json(retmap);
		
		return json;
	}
	
	
	
	public Map<String,String> send(String bizdate ,String clearingphase){
		boolean res = false ;
		String telegram = "";
		Map<String ,String> retmap = new HashMap<String ,String>();
//		<?xml version="1.0" encoding="UTF-8" standalone="yes"?> 
//		<msg> 
//		    <header> 
//		        <SystemHeader>eACH01</SystemHeader> 
//		        <MsgType>0100</MsgType> 
//		        <PrsCode>52XX</PrsCode> 
//		        <Stan>XXXXXXX</Stan> 
//		        <InBank>0000000</InBank> 
//		        <OutBank>9990000</OutBank> 
//		        <DateTime>YYYYMMDDHHMMSS</DateTime> 
//		        <RspCode>0000</RspCode> 
//		    </header> 
//		</msg>
		try {
		Header msgHeader = new Header();
		msgHeader.setSystemHeader("eACH01");
		msgHeader.setMsgType("0100");
		msgHeader.setPrsCode("52XX");//52XX 
		msgHeader.setStan(ap_pause_Dao.getStan());
		msgHeader.setInBank("0000000");
		msgHeader.setOutBank("9990000");	//20150109 FANNY說 票交發動的電文，「OUTBANK」必須固定為「9990000」
		msgHeader.setDateTime(zDateHandler.getDateNum()+zDateHandler.getTheTime().replaceAll(":", ""));
		msgHeader.setRspCode("0000");
		System.out.println("msgHeader.getStan>>"+msgHeader.getStan());
		Body msgBody = new Body();
		System.out.println("bizdate>>"+bizdate+",clearingphase>>"+clearingphase);
		msgBody.setBizDate(bizdate);
		msgBody.setClearingPhase(clearingphase);
		Message msg = new Message();
		msg.setHeader(msgHeader);
		msg.setBody(msgBody);
		telegram = MessageConverter.marshalling(msg);
		retmap = socketClient.send(telegram);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			retmap.put("result", "FALSE");
			retmap.put("msg", "發送通知失敗，系統異常");
		}
		
		return retmap;
	}
	
	
	/**
	 * 
	 * @param bizdate
	 * @param clearingphase
	 * @return
	 */
	public Map<String,String> bat_send(String bizdate ,String clearingphase , int batch_proc_seq){
		boolean res = false ;
		String telegram = "";
		Map<String ,String> retmap = new HashMap<String ,String>();

//		<?xml version="1.0" encoding="UTF-8" standalone="yes"?> 
//		<msg> 
//		    <header> 
//		        <SystemHeader>eACH01</SystemHeader> 
//		        <MsgType>0100</MsgType> 
//		        <PrsCode>9102</PrsCode> 
//		        <Stan>XXXXXXX</Stan> 
//		        <InBank>0000000</InBank> 
//		        <OutBank>9990000</OutBank> 
//		        <DateTime>YYYYMMDDHHMMSS</DateTime> 
//		        <RspCode>0000</RspCode> 
//		    </header> 
//		    <body> 
//		         <BizDate></BizDate> 
//		         <ClearingPhase></ClearingPhase> 
//		        <BatchSeqNo></BatchSeqNo> 
//		    </body> 
//		</msg> 
	//
//		BatchSeqNo  0 => 重新產生BHCLEARINGTAB、EACH_BATCH_DATA_VALIDATION 
//		            1 => 寫入未完成資料 (圈群未完成檔處理)
//		            2=>  重新執行比對作業(圈存連線結帳檔處理)
		
		try {
			Header msgHeader = new Header();
			msgHeader.setSystemHeader("eACH01");
			msgHeader.setMsgType("0100");
			msgHeader.setPrsCode("9102");
			msgHeader.setStan("XXXXXXX");
			msgHeader.setInBank("0000000");
			msgHeader.setOutBank("9990000");	//20150109 FANNY說 票交發動的電文，「OUTBANK」必須固定為「9990000」
			msgHeader.setDateTime(zDateHandler.getDateNum()+zDateHandler.getTheTime().replaceAll(":", ""));
			msgHeader.setRspCode("0000");
			System.out.println("msgHeader.getStan>>"+msgHeader.getStan());
			Body msgBody = new Body();
			System.out.println("bizdate>>"+bizdate+",clearingphase>>"+clearingphase);
			msgBody.setBizDate(bizdate);
			msgBody.setClearingPhase(clearingphase);
			msgBody.setBatchSeqNo(String.valueOf(batch_proc_seq));
			Message msg = new Message();
			msg.setHeader(msgHeader);
			msg.setBody(msgBody);
			telegram = MessageConverter.marshalling(msg);
			retmap = socketClient.send(telegram);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			retmap.put("result", "FALSE");
			retmap.put("msg", "發送批次電文通知失敗，系統異常");
		}
		
		return retmap;
	}
	
	public Map<String,String> gw_Bat_Send(String bizdate ,String clearingphase ,String type){
		boolean res = false ;
		String telegram = "";
		Map<String ,String> retmap = new HashMap<String ,String>();
//		<?xml version="1.0" encoding="UTF-8" standalone="yes"?> 
//		<msg> 
//		    <header> 
//		        <SystemHeader>eACH01</SystemHeader> 
//		        <MsgType>0100</MsgType> 
//		        <PrsCode>9102</PrsCode> 
//		        <Stan>XXXXXXX</Stan> 
//		        <InBank>0000000</InBank> 
//		        <OutBank>9990000</OutBank> 
//		        <DateTime>YYYYMMDDHHMMSS</DateTime> 
//		        <RspCode>0000</RspCode> 
//		    </header> 
//		    <body> 
//		         <BizDate></BizDate> 
//		         <ClearingPhase></ClearingPhase> 
//		        <Type></Type> 
//		    </body> 
//		</msg> 
	//
//		Type    00 =>起始點開始執行 
//		             01 =>中斷點開始執行 
		
		try {
			Header msgHeader = new Header();
			msgHeader.setSystemHeader("eACH01");
			msgHeader.setMsgType("0100");
			msgHeader.setPrsCode("9102");
			msgHeader.setStan("XXXXXXX");
			msgHeader.setInBank("0000000");
			msgHeader.setOutBank("9990000");	//20150109 FANNY說 票交發動的電文，「OUTBANK」必須固定為「9990000」
			msgHeader.setDateTime(zDateHandler.getDateNum()+zDateHandler.getTheTime().replaceAll(":", ""));
			msgHeader.setRspCode("0000");
			System.out.println("msgHeader.getStan>>"+msgHeader.getStan());
			Body msgBody = new Body();
			System.out.println("bizdate>>"+bizdate+",clearingphase>>"+clearingphase);
			msgBody.setBizDate(bizdate);
			msgBody.setClearingPhase(clearingphase);
			msgBody.setType(type);
			Message msg = new Message();
			msg.setHeader(msgHeader);
			msg.setBody(msgBody);
			telegram = MessageConverter.marshalling(msg);
			retmap = socketClient.send(telegram);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			retmap.put("result", "FALSE");
			retmap.put("msg", "發送批次電文通知失敗，系統異常");
		}
		
		return retmap;
	}
	
	

	
	/**
	 * 所有GateWay負責的排程重新執行及中斷點後重新執行
	 * @param params
	 * @return
	 */
	public String exeGW_Bat(Map<String, String> params){
		String json = "";
		boolean ret = false ;
		String bizdate =StrUtils.isNotEmpty(params.get("BIZDATE"))? params.get("BIZDATE"):"";
		String clearingphase =StrUtils.isNotEmpty(params.get("CLEARINGPHASE"))? params.get("CLEARINGPHASE"):"";
		String type =StrUtils.isNotEmpty(params.get("type"))? params.get("type"):"";
		String action =StrUtils.isNotEmpty(params.get("action"))? params.get("action"):"";
		Map<String ,String> retmap = new HashMap<String ,String>();
		Map<String ,String> logmap = new HashMap<String ,String>();
		try {
//			民國轉西元
			bizdate = DateTimeUtils.convertDate(1, bizdate, "yyyy-MM-dd", "yyyyMMdd");
			if(type.equals("01")){
				List<Map> list =batch_status_Dao.getBrekPonitListII(bizdate, clearingphase);
				System.out.println("list>>"+list);
				if(list == null || list.size() == 0){
					retmap.put("result", "FALSE");
					retmap.put("msg", "編號0~20批次作業查無中斷點，未執行");
					return JSONUtils.map2json(retmap);
				}
			}

			retmap = gw_Bat_Send(bizdate, clearingphase, type);
			if(!retmap.get("result").equals("TRUE")){
				json = JSONUtils.map2json(retmap);
				return json;
			}
//		要先初始化  notify 失敗就直接回傳
			ret= initEach_Batch_Notify(bizdate, clearingphase);
			if(!ret){
				retmap.put("result", "FALSE");
				retmap.put("msg", "失敗，初始化Each_Batch_Notify時異常");
				return JSONUtils.map2json(retmap);
			}
			ret = false;
//			初始化21以後的 
			bizdate = DateTimeUtils.convertDate(1, bizdate, "yyyyMMdd" , "yyyy-MM-dd");
			ret = initEach_Batch_Status_MAN(bizdate, clearingphase);
			logmap.put("BIZDATE", bizdate);
			logmap.put("CLEARINGPHASE", clearingphase);
			if(ret){
				retmap.put("result", "TRUE");
				retmap.put("msg", "成功，編號0~20批次作業重新執行中...");
				if(type.equals("00")){
					userlog_bo.genericLog("O", "成功，編號0~20批次作業重新執行中...", action, null, logmap);
				}
				if(type.equals("01")){
					userlog_bo.genericLog("N", "成功，編號0~20批次作業重新執行中...", action, null, logmap);
				}
				
			}else{
				retmap.put("result", "FALSE");
				retmap.put("msg", "部分異常，編號0~20批次作業重新執行中，但初始化批次作業編號21之後的排程狀態失敗...");
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			retmap.put("msg", "系統異常");
		}finally{
			
		}
		json = JSONUtils.map2json(retmap);
		return json;
	}
//	所有sp重新執行
	public String exeStartPoint(Map<String, String> params){
		String json = "";
		boolean ret = false ;
		String bizdate =StrUtils.isNotEmpty(params.get("BIZDATE"))? params.get("BIZDATE"):"";
		String clearingphase =StrUtils.isNotEmpty(params.get("CLEARINGPHASE"))? params.get("CLEARINGPHASE"):"";
		String batch_proc_seq =StrUtils.isNotEmpty(params.get("BATCH_PROC_SEQ"))? params.get("BATCH_PROC_SEQ"):"";
		String action =StrUtils.isNotEmpty(params.get("action"))? params.get("action"):"";
		Map<String ,String> retmap = new HashMap<String ,String>();
		Map<String ,String> logmap = new HashMap<String ,String>();
		try {
//			先檢查onblocktab onpending 是否已清算
//			要先初始化 status notify 失敗就直接回傳
			Map map = initEach_Batch_MAN(bizdate, clearingphase);
			if(map.get("result").equals("FALSE")){
				return JSONUtils.map2json(map);
			}
//			bizdate = bizdate.replace("-", "");
			logmap.put("BIZDATE", bizdate);
			logmap.put("CLEARINGPHASE", clearingphase);
			bizdate = DateTimeUtils.convertDate(1, bizdate, "yyyy-MM-dd", "yyyyMMdd");
//			ret = doAllBatch(bizdate, clearingphase);
			ret = doALL_MAN_BAT(bizdate, clearingphase);
			if(ret){
				retmap.put("result", "TRUE");
				retmap.put("msg", "相關批次作業執行中...");
				userlog_bo.genericLog("O", "成功，編號21~70批次作業重新執行中...", action, null, logmap);
			}else{
				retmap.put("msg", "執行失敗");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			retmap.put("msg", "系統異常");
			userlog_bo.genericLog("O", "失敗，系統異常:"+e, action, null,logmap);
		}finally{
			
//			List<EACH_BATCH_STATUS> list = batch_status_Dao.find( " FROM tw.org.twntch.po.EACH_BATCH_STATUS WHERE BATCH_PROC_SEQ >= 21 AND PROC_TYPE = 'D' ORDER BY BATCH_PROC_SEQ DESC  FETCH FIRST 1 ROWS ONLY " ) ;
//			if(list !=null){
//				EACH_BATCH_STATUS tmppo =  list.get(0);
//				try {
//					logmap = BeanUtils.describe(tmppo.getId()) ;
//					logmap.putAll(BeanUtils.describe(tmppo));
//					userlog_bo.genericLog("O", "成功", action,null, logmap);
//				} catch (IllegalAccessException | InvocationTargetException
//						| NoSuchMethodException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
		}
		json = JSONUtils.map2json(retmap);
		return json;
	}
	
//	只執行中斷點以後的sp
	public String exeBreakPoint(Map<String, String> params){
		String json = "";
		String bizdate =StrUtils.isNotEmpty(params.get("BIZDATE"))? params.get("BIZDATE"):"";
		String clearingphase =StrUtils.isNotEmpty(params.get("CLEARINGPHASE"))? params.get("CLEARINGPHASE"):"";
		String batch_proc_seq =StrUtils.isNotEmpty(params.get("BATCH_PROC_SEQ"))? params.get("BATCH_PROC_SEQ"):"";
		String action =StrUtils.isNotEmpty(params.get("action"))? params.get("action"):"";
		String batch_name = "";
		String tmpbizdate = "";
		String tmpbatch_proc_seq = "21";//從21開始是避免有特殊狀況時影響到onblock排程
		String pre_proc_seq = "";//從21開始是避免有特殊狀況時影響到onblock排程
		String tmpproc_status = "S";//因為是從中斷點開始，所已前一個階段一定是成功
		boolean logresult = true;
		Map<String ,String> retmap = new HashMap<String ,String>();
		Map<String ,String> logmap = new HashMap<String ,String>();
		// 要查詢哪個是中斷點 之後不管成功失敗都要執行
		retmap.put("result", "FALSE");
		try {
			tmpbizdate = bizdate;
			logmap.put("BIZDATE", bizdate);
			logmap.put("CLEARINGPHASE", clearingphase);
			bizdate = DateTimeUtils.convertDate(1, bizdate, "yyyy-MM-dd", "yyyyMMdd");
			System.out.println("exeBreakPoint.bizdate>>"+bizdate+",clearingphase>>"+clearingphase);
			if(!isRunBat(bizdate, clearingphase)){
				retmap.put("result", "FALSE");
				retmap.put("msg", "尚不可執行，目前營業日:"+bizdate+",清算階段:"+clearingphase+"的前置作業批次，有項目未完成");
				json = JSONUtils.map2json(retmap);
				return json;
			}
//			取得中斷點之後的清單O
			List<Map> list =batch_status_Dao.getBrekPonitList(bizdate, clearingphase);
			System.out.println("tmpbizdate>>"+tmpbizdate);
			if(list != null && list.size() != 0){
				logmap = list.get(0);
//				結算通知初始話
				if(initEach_Batch_Notify_MAN(tmpbizdate, clearingphase)){
					tmpbatch_proc_seq = String.valueOf(list.get(0).get("BATCH_PROC_SEQ")) ;
//					tmpbatch_proc_seq = String.valueOf(Integer.valueOf(tmpbatch_proc_seq).intValue()+1);
					System.out.println("tmpbatch_proc_seq>>"+tmpbatch_proc_seq);
//					中斷點之後的所有批次狀態都要初始化
					
					if(initEach_Batch_Status_BySeq(tmpbizdate, clearingphase, tmpbatch_proc_seq)){
						int i = 0;
						for(Map<String,String> map :list){
							batch_proc_seq = map.get("BATCH_PROC_SEQ");
							bizdate = map.get("BIZDATE") ;
							clearingphase = map.get("CLEARINGPHASE");
							batch_name = map.get("BATCH_PROC_NAME");
							System.out.println("batch_proc_seq>>"+batch_proc_seq);
//							檢核上一階段如果失敗且不可繼續就中斷
//							if(isBreak(batch_proc_seq, tmpproc_status)){
							if(i!=0){
								pre_proc_seq = (String) list.get(i-1).get("BATCH_PROC_SEQ");
								if(isBreak(pre_proc_seq, tmpproc_status)){
									break;
								}
							}
		//					doBreakPoint_BAT(batch_name);
							doBreakPoint_BAT(batch_name ,bizdate ,clearingphase , batch_proc_seq);
	//						查詢執行後的結果
							EACH_BATCH_STATUS_PK id = new EACH_BATCH_STATUS_PK(bizdate, clearingphase, Integer.valueOf(batch_proc_seq));
							EACH_BATCH_STATUS po = batch_status_Dao.get(id);
							tmpproc_status = po.getPROC_STATUS();
							i++;
						}//for end
						retmap.put("result", "TRUE");
						retmap.put("msg", "相關批次作業執行中...");
//						userlog_bo.genericLog("N", "成功，編號21~70批次作業重新執行中...", action, null, logmap);
					}else{
						retmap.put("result", "FALSE");
						retmap.put("msg", "初始化Each_Batch_Status_BySeq失敗...");
//						userlog_bo.genericLog("N", "失敗，初始化Each_Batch_Status_BySeq失敗...", action,logmap, null);
					}
					
				}else{
					retmap.put("result", "FALSE");
					retmap.put("msg", "初始化Each_Batch_Notify失敗...");
//					userlog_bo.genericLog("N", "失敗，初始化Each_Batch_Notify失敗...", action, null ,logmap);
				}
			}else{
				retmap.put("result", "FALSE");
				retmap.put("msg", "編號21~70批次作業查無中斷點，未執行");
				logresult= false;
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			retmap.put("msg", "執行失敗");
			logresult= false;
			userlog_bo.genericLog("N", "失敗，錯誤訊息:"+e, action, null,logmap);
		}finally{
			if(logresult){
				userlog_bo.genericLog("N", "成功，編號21~70批次作業重新執行中...", action, null,logmap);
			}
		}
		json = JSONUtils.map2json(retmap);
		return json;
	}
	
	/**
	 * 判斷是否中斷
	 * @return
	 */
	public boolean isBreak(String batch_proc_seq , String proc_status ){
		EACH_BATCH_DEF defpo= null ;
//		defpo = batch_def_Dao.get(Integer.valueOf(batch_proc_seq)-1);
		defpo = batch_def_Dao.get(Integer.valueOf(batch_proc_seq));
		if(StrUtils.isEmpty(proc_status)){
			return true;
		}
		if( !proc_status.equals("S")  && !defpo.getERR_BREAK().equals("Y")){
			System.out.println("上個階段不是成功,proc_status>>"+proc_status+"且不可繼續執行，ERR_BREAK>>"+defpo.getERR_BREAK());
			return true;
		}
		return false;
		
	}
	
	public boolean doBreakPoint_BAT(String batName , String bizdate,String clearingphase , String batch_proc_seq) throws Exception{
		boolean res = false ;
		String name = batName ;
		Method myMethod = null;
//		Class[] parameterTypes = new Class[] { };
		Class[] parameterTypes = new Class[] {String.class , String.class ,String.class};
		Map<String,String> map = new HashMap<String,String>();
		try {
//			Object[] arguments = new Object[] {};
			Object[] arguments = new Object[] {bizdate,clearingphase , batch_proc_seq};
//			name = "do"+batName;
			name = "doMAN_"+name.replace("BAT_", "");
			System.out.println("doBreakPoint_BAT.name>>"+name);
			myMethod = getClass().getMethod(name, parameterTypes);
			myMethod.invoke(this, arguments);
			res = true;
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		return res;
	}
	
	public boolean doALL_MAN_BAT(String bizdate,String clearingphase  ) throws Exception{
		boolean res = false ;
		String name = "" ;
		Method myMethod = null;
//		Class[] parameterTypes = new Class[] {};
//		Object[] arguments = new Object[] {};
		Class[] parameterTypes = new Class[] {String.class , String.class ,String.class};
		Object[] arguments = new Object[] { };
		EACH_BATCH_STATUS_PK id = null ; 
		EACH_BATCH_STATUS statuspo = null;
		EACH_BATCH_DEF tmppo = null;
		String tmp_ERR_BREAK = "Y";
		try {
//			List<EACH_BATCH_DEF> list = batch_def_Dao.getAll();
			List<EACH_BATCH_DEF> list = this.getBatDef();
//			batch_def_Dao.find(" FROM tw.org.twntch.po.EACH_BATCH_DEF WHERE PROC_TYPE = 'D'", values);
			int i =0;
			if(list != null && list.size() !=0 ){
				logger.debug("doALL_MAN_BAT.list>>"+list);
				for(EACH_BATCH_DEF po:list){
//					if(po.getBATCH_PROC_SEQ() >= 3){
					if(po.getBATCH_PROC_SEQ() >= 21){
						tmppo = po ;
//						batch_status_Dao. 每次都要去檢查 上一個的 執行狀況 中斷點是否繼續執行， 及現在是否為成功
//						id = new EACH_BATCH_STATUS_PK(bizdate, clearingphase, po.getBATCH_PROC_SEQ()-1) ;
						id = new EACH_BATCH_STATUS_PK(bizdate, clearingphase, list.get(i-1).getBATCH_PROC_SEQ()) ;
						statuspo = batch_status_Dao.get(id);
						logger.debug("doALL_MAN_BAT.statuspo>>"+statuspo);
//						上一個執行是空的  break
						if(statuspo ==null ){break;}
						if(StrUtils.isEmpty(statuspo.getPROC_STATUS()) ){break;}
//						上一個執行未成功 且中斷點為N(不可繼續) break
//						if(!statuspo.getPROC_STATUS().equals("S") && !po.getERR_BREAK().equals("Y")){
						if(!statuspo.getPROC_STATUS().equals("S") && !tmp_ERR_BREAK.equals("Y")){
							logger.debug("doALL_MAN_BAT.上一個執行未成功 且中斷點為N(不可繼續) break");
							logger.debug("doALL_MAN_BAT.STATUS>>"+statuspo.getPROC_STATUS()+",tmp_ERR_BREAK>>"+tmp_ERR_BREAK);
							break ;
						}
						//成功或不成功但中斷點為Y(可繼續)
						if( statuspo.getPROC_STATUS().equals("S") 
//								||(!statuspo.getPROC_STATUS().equals("S") && po.getERR_BREAK().equals("Y")) 
								||(!statuspo.getPROC_STATUS().equals("S") && tmp_ERR_BREAK.equals("Y")) 
							){
							name = "doMAN_"+po.getBATCH_PROC_NAME().replace("BAT_", "");
							logger.debug("doALL_MAN_BAT.name>>"+name);
							arguments = new Object[] {bizdate, clearingphase ,String.valueOf(po.getBATCH_PROC_SEQ()) };
							myMethod = getClass().getMethod(name, parameterTypes);
							myMethod.invoke(this, arguments);
//							故障測試用(測試中斷點)
//							if(po.getBATCH_PROC_SEQ()==5){
//								id = new EACH_BATCH_STATUS_PK(bizdate, clearingphase, po.getBATCH_PROC_SEQ()) ;
//								statuspo = batch_status_Dao.get(id);
//								statuspo.setPROC_STATUS("F");
//								statuspo.setBEGIN_TIME(zDateHandler.getTheDateII()+" "+zDateHandler.getTheTime_MS());
//								statuspo.setEND_TIME(zDateHandler.getTheDateII()+" "+zDateHandler.getTheTime_MS());
//								batch_status_Dao.aop_save(statuspo);
//							}
						}
					}
//					紀錄上一個階段定義檔中 ，中斷是否可繼續執行
					tmp_ERR_BREAK = po.getERR_BREAK();
					i++;
				}//for end
				logger.debug("doAllBatch is over");
				res = true;
			}
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.debug("doALL_MAN_BAT."+e);
//			 AP錯誤 ，自己寫狀態
			tmp_ERR_BREAK = tmppo.getERR_BREAK();
			id = new EACH_BATCH_STATUS_PK(bizdate, clearingphase, tmppo.getBATCH_PROC_SEQ()) ;
			statuspo = batch_status_Dao.get(id);
			statuspo.setPROC_STATUS("F");
			statuspo.setBEGIN_TIME(zDateHandler.getTheDateII()+" "+zDateHandler.getTheTime_MS());
			statuspo.setNOTE("系統異常，錯誤訊息"+e);
			batch_status_Dao.aop_save(statuspo);
			
		}
		return res;
	}
	
	/**
	 * 屬於排程的執行所有批次，但要等普鴻的批次跑完才可執行
	 * @return
	 */
	public boolean doBatch(String batName){
		boolean res = false ;
		ExecuteSQL es = new ExecuteSQL("jdbc/ACH");
		String sql = "{call "+batName+"}";
		try {
			es.doSP(sql);
			res = true;
		} catch (DataAccessException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}
	
	/**
	 * 屬於排程的執行所有批次，但要等普鴻的批次跑完才可執行
	 * @return
	 */
	public boolean doAllBatch(String bizdate , String clearingphase){
		boolean res = false ;
		String name = "" ;
		Method myMethod = null;
//		Class[] parameterTypes = new Class[] {};
//		Object[] arguments = new Object[] {};
		Class[] parameterTypes = new Class[] {String.class , String.class ,String.class};
		Object[] arguments = new Object[] { };
		EACH_BATCH_STATUS_PK id = null ; 
		EACH_BATCH_STATUS statuspo = null;
		EACH_BATCH_DEF tmppo = null;
		String tmpBreak = "";
		try {
//			List<EACH_BATCH_DEF> list = batch_def_Dao.getAll();
			List<EACH_BATCH_DEF> list = this.getBatDef();
			logger.debug("doAllBatch.list>>"+list);
			if(list != null && list.size() !=0 ){
				int i = 0;
				for(EACH_BATCH_DEF po:list){
					logger.debug("doAllBatch.def.seq>>"+po.getBATCH_PROC_SEQ());
//					if(po.getBATCH_PROC_SEQ() >= 3){
					if(po.getBATCH_PROC_SEQ() >= 21){
						tmppo = po ;
//						batch_status_Dao. 每次都要去檢查 上一個的 執行狀況 中斷點是否繼續執行， 及現在是否為成功
//						id = new EACH_BATCH_STATUS_PK(bizdate, clearingphase, po.getBATCH_PROC_SEQ()-1) ;
						id = new EACH_BATCH_STATUS_PK(bizdate, clearingphase, list.get(i-1).getBATCH_PROC_SEQ()) ;
						statuspo = batch_status_Dao.get(id);
						logger.debug("statuspo>>"+statuspo);
//						上一個執行是空的  break
						if(statuspo ==null ){break;}
						if(StrUtils.isEmpty(statuspo.getPROC_STATUS()) ){break;}
//						上一個執行未成功 且中斷點為N(不可繼續) break
//						if(!statuspo.getPROC_STATUS().equals("S") && !po.getERR_BREAK().equals("Y")){
						if(!statuspo.getPROC_STATUS().equals("S") && !tmpBreak.equals("Y")){
							logger.debug("doAllBatch.上一個執行未成功 且中斷點為N(不可繼續) break");
							logger.debug("doAllBatch.STATUS>>"+statuspo.getPROC_STATUS()+",tmpBreak>>"+tmpBreak);
							break ;
						}
						System.out.println("STATUS>>"+statuspo.getPROC_STATUS());
						System.out.println("ERR_BREAK>>"+po.getERR_BREAK());
						System.out.println("BATCH_PROC_NAME>>"+po.getBATCH_PROC_NAME());
						//成功或不成功但中斷點為Y(可繼續)
						if(statuspo.getPROC_STATUS().equals("S") 
//								||(!statuspo.getPROC_STATUS().equals("S") && po.getERR_BREAK().equals("Y")) 
								||(!statuspo.getPROC_STATUS().equals("S") && tmpBreak.equals("Y")) 
							){
							
							name = "do"+po.getBATCH_PROC_NAME();
							logger.debug("name>>"+name);
							arguments = new Object[] {bizdate, clearingphase ,String.valueOf(po.getBATCH_PROC_SEQ()) };
							myMethod = getClass().getMethod(name, parameterTypes);
							myMethod.invoke(this, arguments);
//							故障測試用(測試中斷點)
//							if(po.getBATCH_PROC_SEQ()==5){
//								id = new EACH_BATCH_STATUS_PK(bizdate, clearingphase, po.getBATCH_PROC_SEQ()) ;
//								statuspo = batch_status_Dao.get(id);
//								statuspo.setPROC_STATUS("F");
//								statuspo.setBEGIN_TIME(zDateHandler.getTheDateII()+" "+zDateHandler.getTheTime_MS());
//								statuspo.setEND_TIME(zDateHandler.getTheDateII()+" "+zDateHandler.getTheTime_MS());
//								batch_status_Dao.aop_save(statuspo);
//							}
							
							
							System.out.println("po.getBATCH_PROC_SEQ()"+po.getBATCH_PROC_SEQ());
//							TODO SP 尚未完成 先自己寫狀態
//							if(po.getBATCH_PROC_SEQ()==9){
//								
//								id = new EACH_BATCH_STATUS_PK(bizdate, clearingphase, po.getBATCH_PROC_SEQ()) ;
//								statuspo = batch_status_Dao.get(id);
//								statuspo.setPROC_STATUS("S");
//								statuspo.setBEGIN_TIME(zDateHandler.getTheDateII()+" "+zDateHandler.getTheTime_MS());
//								statuspo.setEND_TIME(zDateHandler.getTheDateII()+" "+zDateHandler.getTheTime_MS());
//								batch_status_Dao.aop_save(statuspo);
//							}
							System.out.println("doAllBatch is over>>"+ po.getBATCH_PROC_SEQ());
						}
					}//if end
					tmpBreak = po.getERR_BREAK();
					i++;
				}//for end
				System.out.println("doAllBatch is over");
				res = true;
			}
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.debug("doAllBatch.Exception>>"+e);
//			 AP錯誤 ，自己寫狀態
			tmpBreak = tmppo.getERR_BREAK();
			id = new EACH_BATCH_STATUS_PK(bizdate, clearingphase, tmppo.getBATCH_PROC_SEQ()) ;
			statuspo = batch_status_Dao.get(id);
			statuspo.setPROC_STATUS("F");
			statuspo.setBEGIN_TIME(zDateHandler.getTheDateII()+" "+zDateHandler.getTheTime_MS());
			statuspo.setNOTE("系統異常，錯誤訊息"+e);
			batch_status_Dao.aop_save(statuspo);
			res = false;
		}
		return res;
	}
	
	public boolean doBAT_RPONBLOCKTAB(String bizdate,String clearingphase ,String batch_proc_seq){
		logger.debug("doBAT_RPONBLOCKTAB is running...");
		boolean res = false ;
//		20150422 note by hugo 因原本寫法在was運行時，會間歇性發生
//		com.ibm.ws.webcontainer.webapp.WebApp.getInitParameterNames(WebApp.java:2776)為null的情形
		try {
			each_batch_Dao.do_BAT_RPONBLOCKTAB();
			res=true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		ExecuteSQL es = new ExecuteSQL("jdbc/ACH");
//		String sql = "{call EACHUSER.BAT_RPONBLOCKTAB()}";
//		try {
//			es.doSP(sql);
//			res = true;
//		} catch (DataAccessException | SQLException  e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			logger.debug("doBAT_RPONBLOCKTAB>>."+e);
//		}
		return res;
	}
	
	public boolean doBAT_RPTXNLOG(String bizdate,String clearingphase ,String batch_proc_seq){
		logger.debug("doBAT_RPONBLOCKTAB is running...");
		boolean res = false ;
//		20150422 note by hugo 因原本寫法在was運行時，會間歇性發生
//		com.ibm.ws.webcontainer.webapp.WebApp.getInitParameterNames(WebApp.java:2776)為null的情形
		try {
			each_batch_Dao.do_BAT_RPTXNLOG();
			res=true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		ExecuteSQL es = new ExecuteSQL("jdbc/ACH");
//		String sql = "{call EACHUSER.BAT_RPONBLOCKTAB()}";
//		try {
//			es.doSP(sql);
//			res = true;
//		} catch (DataAccessException | SQLException  e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			logger.debug("doBAT_RPONBLOCKTAB>>."+e);
//		}
		return res;
	}
	
	
	public boolean doBAT_RPDAILYTXNLOG(String bizdate,String clearingphase ,String batch_proc_seq){
		logger.debug("doBAT_RPDAILYTXNLOG is running...");
		boolean res = false ;
		try {
			each_batch_Dao.do_BAT_RPDAILYTXNLOG();
			res=true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}
	
	
	public boolean doBAT_RPONPENDINGTAB(String bizdate,String clearingphase ,String batch_proc_seq){
		logger.debug("doBAT_RPONPENDINGTAB is running...");
		boolean res = false ;
		try {
//			TODO sp未完成先註解
//			each_batch_Dao.do_BAT_RPONPENDINGTAB();
			res=true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		ExecuteSQL es = new ExecuteSQL("jdbc/ACH");
//		String sql = "{call EACHUSER.BAT_RPONPENDINGTAB()}";
//		try {
////			TODO sp未完成先註解
////			es.doSP(sql);
//			res = true;
//		} catch (DataAccessException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			logger.debug("doBAT_RPONPENDINGTAB>>."+e);
//		}
		return res;
	}
	public boolean doBAT_RPONCLEARINGTAB(String bizdate,String clearingphase ,String batch_proc_seq){
		logger.debug("doBAT_RPONCLEARINGTAB is running...");
		boolean res = false ;
		try {
			each_batch_Dao.do_BAT_RPONCLEARINGTAB();
			res=true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		ExecuteSQL es = new ExecuteSQL("jdbc/ACH");
//		String sql = "{call EACHUSER.BAT_RPONCLEARINGTAB()}";
//		try {
//			es.doSP(sql);
//			res = true;
//		} catch (DataAccessException | SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			logger.debug("doBAT_RPONCLEARINGTAB>>."+e);
//		}
		return res;
	}
	public boolean doBAT_RPCLEARFEETAB(String bizdate,String clearingphase ,String batch_proc_seq){
		logger.debug("doBAT_RPCLEARFEETAB is running...");
		boolean res = false ;
		try {
			each_batch_Dao.do_BAT_RPCLEARFEETAB();
			res=true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		ExecuteSQL es = new ExecuteSQL("jdbc/ACH");
//		String sql = "{call EACHUSER.BAT_RPCLEARFEETAB()}";
//		try {
//			es.doSP(sql);
//			res = true;
//		} catch (DataAccessException | SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			logger.debug("doBAT_RPCLEARFEETAB>>."+e);
//		}
		return res;
	}
	public boolean doBAT_RPDAILYSUM(String bizdate,String clearingphase ,String batch_proc_seq){
		logger.debug("doBAT_RPDAILYSUM is running...");
		boolean res = false ;
		try {
			each_batch_Dao.do_BAT_RPDAILYSUM();
			res=true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		ExecuteSQL es = new ExecuteSQL("jdbc/ACH");
//		String sql = "{call EACHUSER.BAT_RPDAILYSUM()}";
//		try {
//			es.doSP(sql);
//			res = true;
//		} catch (DataAccessException | SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			logger.debug("doBAT_RPDAILYSUM>>."+e);
//		}
		return res;
	}
	public boolean doBAT_RPMONTHSUM(String bizdate,String clearingphase ,String batch_proc_seq){
		logger.debug("doBAT_RPMONTHSUM is running...");
		boolean res = false ;
		try {
			each_batch_Dao.do_BAT_RPMONTHSUM();
			res=true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		ExecuteSQL es = new ExecuteSQL("jdbc/ACH");
//		String sql = "{call EACHUSER.BAT_RPMONTHSUM()}";
//		try {
//			es.doSP(sql);
//			res = true;
//		} catch (DataAccessException | SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			logger.debug("doBAT_RPMONTHSUM>>."+e);
//		}
		return res;
	}
	
	
	/**
	 * 
	 * @param bizdate
	 * @param clearingphase
	 * @param batch_proc_seq
	 * @return
	 */
	public boolean doBAT_ADDNEWSC(String bizdate,String clearingphase ,String batch_proc_seq){
		logger.debug("doBAT_ADDNEWSC is running...");
		boolean res = false ;
		try {
			res = doMAN_ADDNEWSC(bizdate, clearingphase, batch_proc_seq);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			 e.printStackTrace();
			res = false;
		}
		return res;
	}
	
	
	
	public boolean doMAN_ADDNEWSC(String bizdate,String clearingphase ,String batch_proc_seq){
		logger.debug("doMAN_ADDNEWSC is running...");
		boolean res = false ;
		String sbizdate = bizdate;
		SC_COMPANY_PROFILE sc_company = null;
		List<SC_COMPANY_PROFILE_PK> list = null;
		List<SC_COMPANY_PROFILE> addlist = null;
		List<SC_COMPANY_PROFILE> realAddlist = null;
		StringBuffer str = null;
		StringBuffer str2 = null;
		StringBuffer str3 = null;
		StringBuffer tiltle = null;
		EACH_BATCH_STATUS each_batch_status = null;
		EACH_BATCH_STATUS_PK pk = null;
		String errmsg = "";
		int shortcount = 0;
		try {
			pk = new EACH_BATCH_STATUS_PK(bizdate, clearingphase, Integer.valueOf(batch_proc_seq) );
			each_batch_status = batch_status_Dao.get(pk) ;
			if(each_batch_status == null){
				return false;
//				TODO 測試用 記得改回
//				each_batch_status = new EACH_BATCH_STATUS();
//				each_batch_status.setId(pk);
			}
			each_batch_status.setBEGIN_TIME(zDateHandler.getTimestamp());
			each_batch_status.setPROC_STATUS("P");
			batch_status_Dao.aop_save(each_batch_status);
			str = new StringBuffer();
			str2 = new StringBuffer();
			str3 = new StringBuffer();
			str.append(";無新的代付業者");
			tiltle = new StringBuffer();
			list = sc_com_bo.getSc_com_Dao().getNewSC_Data(sbizdate, sbizdate);
			if(list == null){
				logger.debug("doMAN_ADDNEWSC.list>>"+list);
			}else{
				logger.debug("doMAN_ADDNEWSC.list.size>>"+list.size());
			}
			if(list != null ){
				addlist = new LinkedList<SC_COMPANY_PROFILE>();
				for(SC_COMPANY_PROFILE_PK id : list){
					sc_company = new SC_COMPANY_PROFILE();
					if(StrUtils.isEmpty(id.getCOMPANY_ID()) || StrUtils.isEmpty(id.getTXN_ID()) || StrUtils.isEmpty(id.getSND_BRBK_ID())){
						continue;
					}
					sc_company.setId(id);
					addlist.add(sc_company);
				}
			}
			if(addlist == null){
				logger.debug("doMAN_ADDNEWSC.addlist>>"+addlist);
			}else{
				logger.debug("doMAN_ADDNEWSC.addlist.size>>"+addlist.size());
			}
//			到此處都無問題就視為true
			res = true ;
			if(addlist != null &&  addlist.size()!=0){
				sc_company = null;
				realAddlist = new LinkedList<SC_COMPANY_PROFILE>();
				for(SC_COMPANY_PROFILE po :addlist){
//					sc_com_bo.getSc_com_his_Dao()
//					 找FEE_CODE
					String fee_type = sc_com_bo.getFee_code_bo().getFee_code_Dao().checkFeeCodeType(po.getId().getTXN_ID());
//					找代收代付檔的簡稱及名稱
					sc_company = sc_com_bo.getSc_com_Dao().getDataByCompany_Id(po.getId().getCOMPANY_ID() , po.getId().getTXN_ID() , po.getId().getSND_BRBK_ID());
					logger.debug("sc_company>>"+sc_company);
					if(sc_company != null ){
						if(StrUtils.isEmpty(sc_company.getCOMPANY_ABBR_NAME()) || StrUtils.isEmpty(sc_company.getCOMPANY_NAME()) ){
							sc_company.setIS_SHORT("Y");
							shortcount++;
						}
//						sc_company.setSYS_CDATE(zDateHandler.getTheDateII());
//						sc_company.setCDATE(zDateHandler.getTheDateII());
//						realAddlist.add(sc_company);
//						str3.append(";統編:"+po.getId().getCOMPANY_ID()+" ， 分行代號:"+po.getId().getSND_BRBK_ID()+" , 交易代號:"+po.getId().getTXN_ID()+";");
					}
//					20201013 edit by hugo req by建利 如果找不到 就自動建立基本資料
					else
					{ 
						sc_company = new SC_COMPANY_PROFILE();
						sc_company.setId(new SC_COMPANY_PROFILE_PK(po.getId().getCOMPANY_ID(), po.getId().getTXN_ID(), po.getId().getSND_BRBK_ID()) );
						sc_company.setIS_SHORT("Y");
						shortcount++;
//						sc_company.setSYS_CDATE(zDateHandler.getTheDateII());
//						sc_company.setCDATE(zDateHandler.getTheDateII());
//						realAddlist.add(sc_company);
//						str3.append(";統編:"+po.getId().getCOMPANY_ID()+" ， 分行代號:"+po.getId().getSND_BRBK_ID()+" , 交易代號:"+po.getId().getTXN_ID()+";");
					}
					sc_company.setFEE_TYPE(fee_type);
					//民國年 >>  ex :01091015
					sc_company.setFEE_TYPE_ACTIVE_DATE(zDateHandler.getTWDate());
					sc_company.setSYS_CDATE(zDateHandler.getTheDateII());
					sc_company.setCDATE(zDateHandler.getTheDateII());
					realAddlist.add(sc_company);
					fee_type = StrUtils.isEmpty(fee_type)? "":"A".equalsIgnoreCase(fee_type) ?"固定":"外加";
					str3.append(";統編:"+po.getId().getCOMPANY_ID()+" ， 分行代號:"+po.getId().getSND_BRBK_ID()+" , 交易代號:"+po.getId().getTXN_ID()+" , 手續費類型:"+fee_type +" , 手續費類型啟用日:"+zDateHandler.getTheDateII()+";");
				}//for end
				
				logger.debug("realAddlist.size()>>"+realAddlist.size());
				if(realAddlist.size() != 0){
					res = sc_com_bo.getSc_com_Dao().batch_SaveData(realAddlist);
					str.delete(0, str.length());
					str.append(str3);
					str2.append(";共新增 "+realAddlist.size()+" 筆 ，異動 "+(realAddlist.size()-shortcount)+" 筆 ，資料短缺"+shortcount+"筆");
				}
			}
			str2.append(str);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.debug("doMAN_ADDNEWSC.Exception>>"+e);
			errmsg= e.toString();
			res = false;
		}finally{
			logger.debug("res>>"+res);
//			pk = new EACH_BATCH_STATUS_PK(bizdate, clearingphase, Integer.valueOf(batch_proc_seq) );
//			each_batch_status = new EACH_BATCH_STATUS();
//			each_batch_status.setId(pk);
			each_batch_status = batch_status_Dao.get(pk) ;
			if(each_batch_status == null){
				return false;
			}
			if(res){
				tiltle.append("新增代付業者檔清單如下:");
				tiltle.append(str2.toString());
				each_batch_status.setPROC_STATUS("S");
			}else{
				tiltle.append("錯誤訊息:"+errmsg);
				each_batch_status.setPROC_STATUS("F");
			}
			each_batch_status.setEND_TIME(zDateHandler.getTimestamp());
			each_batch_status.setNOTE(tiltle.toString());
			batch_status_Dao.aop_save(each_batch_status);
		}
		return res;
	}
	
	/**
	 * 中介檔處理
	 * @param bizdate
	 * @param clearingphase
	 * @param batch_proc_seq
	 * @return
	 */
	public boolean doBAT_MAKE_TXN_COMPANY_FEE(String bizdate,String clearingphase ,String batch_proc_seq){
		logger.debug("doBAT_MAKE_TXN_COMPANY_FEE is running...");
		boolean res = false ;
		try {
			res = doMAN_MAKE_TXN_COMPANY_FEE(bizdate, clearingphase, batch_proc_seq);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			res = false;
		}
		return res;
	}
	/**
	 * 中介檔處理
	 * @param bizdate
	 * @param clearingphase
	 * @param batch_proc_seq
	 * @return
	 */
	public boolean doMAN_MAKE_TXN_COMPANY_FEE(String bizdate,String clearingphase ,String batch_proc_seq){
		logger.debug("doMAN_MAKE_TXN_COMPANY_FEE is running...");
		boolean res = false ;
		ExecuteSQL es = new ExecuteSQL("jdbc/ACH");
		String sql = "{call EACHUSER.MAN_MAKE_TXN_COMPANY_FEE('"+bizdate+"','"+clearingphase+"')}";
		try {
//			尚未有sp
			write_Batch_Status_BySeq(bizdate, clearingphase, batch_proc_seq, "P", "");
			es.doSP(sql);
			write_Batch_Status_BySeq(bizdate, clearingphase, batch_proc_seq, "S", "成功");
		} catch (DataAccessException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			res = false;
			String error = CodeUtils.parseDB2Error(e.getMessage());
			try {
				if(StrUtils.isNotEmpty(error) && "88888".equalsIgnoreCase(error)){
					error = "RPONBLOCK 前置作業失敗，錯誤代碼:"+error;
					List<Map> data = batch_status_Dao.getOnblockData(bizdate, clearingphase);
					if(data !=null && !data.isEmpty()){
						error = error +"代收發動者基本資料未建立，無法產生手續費中介檔。" + 
								"需補建之代收發動者基本資料如下：";
						error += "[";
						for(Map eachdata:data) {
							error +="{發動行:"+eachdata.get("SENDERBANKID")+",交易代號:"+eachdata.get("TXID")+",發動者統編:"+eachdata.get("SENDERID")+"}";
						}
						error += "]";
				}
					write_Batch_Status_BySeq(bizdate, clearingphase, batch_proc_seq, "F", error);
				}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				logger.error("Exception>>"+e);
			}
		}
		return res;
	}
	
	
	
	public boolean doRPDATADOWNLOAD(String bizdate,String clearingphase ,String batch_proc_seq){
		logger.debug("doRPDATADOWNLOAD is running...");
		boolean res = false ;
		boolean isRunMonth = false;
		String prv_year_month = "";
		String tmpdate = "";
//		要另外判斷bizdate 是否為當月第一天 clearingphase是否為02 來決定isMonth 並擷取為幾月(數字)?當月-1?(上個月)
		EACH_BATCH_STATUS po = null ;
		EACH_BATCH_STATUS_PK id = new EACH_BATCH_STATUS_PK(bizdate, clearingphase, Integer.valueOf(batch_proc_seq));
		po = batch_status_Dao.get(id);
		EACH_BATCH_DEF defpo =  batch_def_Dao.get(Integer.valueOf(batch_proc_seq));
		if(po==null){
			po = new EACH_BATCH_STATUS();
			po.setId(id);
		}
		tmpdate = DateTimeUtils.convertDate(2,bizdate, "yyyyMMdd", "yyyy/MM/dd");
		prv_year_month = zDateHandler.getEACH_PrveMonth(tmpdate);
		
//		這裡應該要輸入西元年
		isRunMonth = wk_date_bo.isFirst_Bizdate_of_Month(tmpdate);// 手動這部分可以註解 
//		是該月的第一個營業日 且為第二階端才可執行'月'相關下載檔
		if(isRunMonth && clearingphase.equals("02")){
			isRunMonth = true ;
		}
		System.out.println("doRPDATADOWNLOAD ..prv_year_month>>"+prv_year_month+"bizdate"+bizdate);
		System.out.println("doRPDATADOWNLOAD ..isRunMonth>>"+isRunMonth+"clearingphase"+clearingphase);
		logger.debug("doRPDATADOWNLOAD ..prv_year_month>>"+prv_year_month+"bizdate"+bizdate);
		logger.debug("doRPDATADOWNLOAD ..isRunMonth>>"+isRunMonth+"clearingphase"+clearingphase);
		
		if(!runschedule.isRunning()){
			runschedule.setBizDate(bizdate);
			runschedule.setClearingphase(clearingphase);
			runschedule.setMonth(isRunMonth);
			runschedule.setYYYYMM(prv_year_month);
			runschedule.setBatch_proc_seq(Integer.valueOf(batch_proc_seq));
			
			Thread thread = new Thread(runschedule);
			thread.start();
		}
		
//		Map<String,String> map = runschedule.doBAT_RPDATADOWNLOAD(bizdate, clearingphase, isRunMonth, prv_year_month);
//		po = batch_status_Dao.get(id);
//		if(po==null){
//			po = new EACH_BATCH_STATUS();
//			po.setId(id);
//		}
//		po.setEND_TIME(zDateHandler.getTheDateII()+" "+zDateHandler.getTheTime_MS());
//		if(map.get("result").equals("TRUE")){
//			po.setPROC_STATUS("S");
//		}else{
//			po.setPROC_STATUS("F");
//		}
//		po.setNOTE(map.get("msg"));
//		batch_status_Dao.save(po);
		res = true ;
		return res;
		
	}
	
	public boolean doMAN_RPONBLOCKTAB(String bizdate,String clearingphase ,String batch_proc_seq){
		logger.debug("doMAN_RPONBLOCKTAB bizdate>>"+bizdate+" , clearingphase>"+clearingphase+" , batch_proc_seq>"+batch_proc_seq);
		boolean res = false ;
		ExecuteSQL es = new ExecuteSQL("jdbc/ACH");
//		String sql = "{call EACHUSER.MAN_RPONBLOCKTAB('"+bizdate+"','"+clearingphase+"')}";
		//20200717 更新
		String sql = "{call EACHUSER.UPD_ONBLOCKTAB_FEE_NW('"+bizdate+"','"+clearingphase+"')}";
		String sql2 = "{call EACHUSER.MAN_RPONBLOCKTAB_NW('"+bizdate+"','"+clearingphase+"')}";
		try {
			es.doSP(sql);
			//20200717 更新
			es.doSP(sql2);
//			es.doQuery("SELECT * FROM SYS_PARA");
			res = true;
		} catch (DataAccessException | SQLException e) {
			e.printStackTrace();
			logger.debug("doMAN_RPONBLOCKTAB.Exception>>"+e);
			res = false;
		}
		return res;
	}
	public boolean doMAN_RPONPENDINGTAB(String bizdate,String clearingphase ,String batch_proc_seq){
		logger.debug("doMAN_RPONPENDINGTAB bizdate>>"+bizdate+" , clearingphase>"+clearingphase+" , batch_proc_seq>"+batch_proc_seq);
		boolean res = false ;
		ExecuteSQL es = new ExecuteSQL("jdbc/ACH");
		String sql = "{call EACHUSER.MAN_RPONPENDINGTAB('"+bizdate+"','"+clearingphase+"')}";
		try {
//			TODO sp未完成先註解
//			es.doSP(sql);
			res = true;
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.debug("doMAN_RPONPENDINGTAB.Exception>>"+e);
			res = false;
		}
		return res;
	}
	public boolean doMAN_RPONCLEARINGTAB(String bizdate,String clearingphase ,String batch_proc_seq){
		logger.debug("doMAN_RPONCLEARINGTAB bizdate>>"+bizdate+" , clearingphase>"+clearingphase+" , batch_proc_seq>"+batch_proc_seq);
		boolean res = false ;
		ExecuteSQL es = new ExecuteSQL("jdbc/ACH");
		String sql = "{call EACHUSER.MAN_RPONCLEARINGTAB('"+bizdate+"','"+clearingphase+"')}";
		try {
			es.doSP(sql);
			System.out.println("doMAN_RPONCLEARINGTAB is run...");
			res = true;
		} catch (DataAccessException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.debug("doMAN_RPONCLEARINGTAB.Exception>>"+e);
			res = false;
		}
		return res;
	}
	public boolean doMAN_RPCLEARFEETAB(String bizdate,String clearingphase ,String batch_proc_seq){
		logger.debug("doMAN_RPCLEARFEETAB bizdate>>"+bizdate+" , clearingphase>"+clearingphase+" , batch_proc_seq>"+batch_proc_seq);
		boolean res = false ;
		ExecuteSQL es = new ExecuteSQL("jdbc/ACH");
		String sql = "{call EACHUSER.MAN_RPCLEARFEETAB('"+bizdate+"','"+clearingphase+"')}";
		//20200717 更新
		String sql2 = "{call EACHUSER.MAN_RPCLEARFEETAB_NW('"+bizdate+"','"+clearingphase+"')}";
		try {
			es.doSP(sql);
			//20200717 更新
			es.doSP(sql2);
			res = true;
		} catch (DataAccessException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.debug("doMAN_RPCLEARFEETAB.Exception>>"+e);
			res = false;
		}
		return res;
	}
	public boolean doMAN_RPDAILYSUM(String bizdate,String clearingphase ,String batch_proc_seq){
		logger.debug("doMAN_RPDAILYSUM bizdate>>"+bizdate+" , clearingphase>"+clearingphase+" , batch_proc_seq>"+batch_proc_seq);
		boolean res = false ;
		ExecuteSQL es = new ExecuteSQL("jdbc/ACH");
		String sql = "{call EACHUSER.MAN_RPDAILYSUM('"+bizdate+"','"+clearingphase+"')}";
		try {
			es.doSP(sql);
			res = true;
		} catch (DataAccessException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.debug("doMAN_RPDAILYSUM.Exception>>"+e);
			res = false;
		}
		return res;
	}
	public boolean doMAN_RPMONTHSUM(String bizdate,String clearingphase ,String batch_proc_seq){
		logger.debug("doMAN_RPMONTHSUM bizdate>>"+bizdate+" , clearingphase>"+clearingphase+" , batch_proc_seq>"+batch_proc_seq);
		boolean res = false ;
		ExecuteSQL es = new ExecuteSQL("jdbc/ACH");
		System.out.println("EACHUSER.MAN_RPMONTHSUM IS START");
		String sql = "{call EACHUSER.MAN_RPMONTHSUM('"+bizdate+"','"+clearingphase+"')}";
		try {
			es.doSP(sql);
			res = true;
		} catch (DataAccessException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.debug("doMAN_RPMONTHSUM.Exception>>"+e);
			res = false;
		}
		return res;
	}
	
	public boolean doMAN_RPTXNLOG(String bizdate,String clearingphase ,String batch_proc_seq){
		logger.debug("doMAN_RPTXNLOG bizdate>>"+bizdate+" , clearingphase>"+clearingphase+" , batch_proc_seq>"+batch_proc_seq);
		boolean res = false ;
		ExecuteSQL es = new ExecuteSQL("jdbc/ACH");
		String sql = "{call EACHUSER.MAN_RPTXNLOG('"+bizdate+"','"+clearingphase+"')}";
		try {
			es.doSP(sql);
			res = true;
		} catch (DataAccessException | SQLException e) {
			e.printStackTrace();
			logger.debug("doMAN_RPTXNLOG.Exception>>"+e);
			res = false;
		}
		return res;
	}
	public boolean doMAN_RPDAILYTXNLOG(String bizdate,String clearingphase ,String batch_proc_seq){
		logger.debug("doMAN_RPDAILYTXNLOG bizdate>>"+bizdate+" , clearingphase>"+clearingphase+" , batch_proc_seq>"+batch_proc_seq);
		boolean res = false ;
		ExecuteSQL es = new ExecuteSQL("jdbc/ACH");
		String sql = "{call EACHUSER.MAN_RPDAILYTXNLOG('"+bizdate+"','"+clearingphase+"')}";
		try {
			es.doSP(sql);
			res = true;
		} catch (DataAccessException | SQLException e) {
			e.printStackTrace();
			logger.debug("doMAN_RPDAILYTXNLOG.Exception>>"+e);
			res = false;
		}
		return res;
	}
	public boolean doMAN_RPDATADOWNLOAD(String bizdate,String clearingphase ,String batch_proc_seq){
		logger.debug("doMAN_RPDATADOWNLOAD bizdate>>"+bizdate+" , clearingphase>"+clearingphase+" , batch_proc_seq>"+batch_proc_seq);
		boolean res = false ;
		boolean isRunMonth = false;
		String prv_year_month = "";
		String tmpdate = "";
//		非sp 相關程式也未完成  先視為TRUE
//		TODO 要另外判斷bizdate 是否為當月第一天 clearingphase是否為02 來決定isMonth 並擷取為幾月(數字)?當月-1?(上個月)
//		排程那邊也要改
		System.out.println("doMAN_RPDATADOWNLOAD is begin...");
//		EACH_BATCH_STATUS po = null ;
//		EACH_BATCH_STATUS_PK id = new EACH_BATCH_STATUS_PK(bizdate, clearingphase, Integer.valueOf(batch_proc_seq));
//		po = batch_status_Dao.get(id);
//		EACH_BATCH_DEF defpo =  batch_def_Dao.get(Integer.valueOf(batch_proc_seq));
//		if(po==null){
//			po = new EACH_BATCH_STATUS();
//			po.setId(id);
//		}
		tmpdate = DateTimeUtils.convertDate(bizdate, "yyyyMMdd", "yyyy/MM/dd");
		prv_year_month = zDateHandler.getEACH_PrveMonth(tmpdate);
//		po.setBATCH_PROC_DESC(defpo.getBATCH_PROC_DESC());
//		po.setBATCH_PROC_NAME(defpo.getBATCH_PROC_NAME());
//		po.setBEGIN_TIME(zDateHandler.getTheDateII()+" "+zDateHandler.getTheTime_MS());
//		po.setNOTE("");
//		po.setPROC_STATUS("P");
//		batch_status_Dao.aop_save(po);
//		isRunMonth = wk_date_bo.isFirst_Bizdate_of_Month(tmpdate);// 手動這部分可以註解 
		
//		是該月的第一個營業日 且為第二階端才可執行'月'相關下載檔
//		if(isRunMonth && clearingphase.equals("02")){
		if(clearingphase.equals("02")){ //手動判斷是否為第二階段即可
			isRunMonth = true ;
		}
		System.out.println("doMAN_RPDATADOWNLOAD ..prv_year_month>>"+prv_year_month+"bizdate"+bizdate);
		System.out.println("doMAN_RPDATADOWNLOAD ..isRunMonth>>"+isRunMonth+"clearingphase"+clearingphase);
		logger.debug("doMAN_RPDATADOWNLOAD ..prv_year_month>>"+prv_year_month+"bizdate"+bizdate);
		logger.debug("doMAN_RPDATADOWNLOAD ..isRunMonth>>"+isRunMonth+"clearingphase"+clearingphase);
		
		
		if(!runschedule.isRunning()){
			runschedule.setBizDate(bizdate);
			runschedule.setClearingphase(clearingphase);
			runschedule.setMonth(isRunMonth);
			runschedule.setYYYYMM(prv_year_month);
			runschedule.setBatch_proc_seq(Integer.valueOf(batch_proc_seq));
			
			Thread thread = new Thread(runschedule);
			thread.start();
		}
		
		
//		Map<String,String> map = runschedule.doBAT_RPDATADOWNLOAD(bizdate, clearingphase, isRunMonth, prv_year_month);
//		po = batch_status_Dao.get(id);
//		if(po==null){
//			po = new EACH_BATCH_STATUS();
//			po.setId(id);
//		}
//		po.setEND_TIME(zDateHandler.getTheDateII()+" "+zDateHandler.getTheTime_MS());
//		if(map.get("result").equals("TRUE")){
//			po.setPROC_STATUS("S");
//		}else{
//			po.setPROC_STATUS("F");
//		}
//		po.setNOTE(map.get("msg"));
//		batch_status_Dao.aop_save(po);
		res = true ;
		return res;
	}
	

	
	
	
	/**
	 * 執行批次報表
	 * @param bizdate
	 * @param clearingphase
	 * @param batch_proc_seq
	 * @return
	 */
	public boolean doMAN_RPT(String bizdate,String clearingphase ,String batch_proc_seq){
		logger.debug("doMAN_BAT_RPT bizdate>>"+bizdate+" , clearingphase>"+clearingphase+" , batch_proc_seq>"+batch_proc_seq);
		boolean res = false ;
		System.out.println("doMAN_BAT_RPT is begin...");
		EACH_BATCH_STATUS po = null;
		EACH_BATCH_DEF defpo = null;
		EACH_BATCH_STATUS_PK id = new EACH_BATCH_STATUS_PK(bizdate, clearingphase, Integer.valueOf(batch_proc_seq));
		try {
//			po = batch_status_Dao.get(id);
//			defpo =  batch_def_Dao.get(Integer.valueOf(batch_proc_seq));
//			if(po==null){
//				po = new EACH_BATCH_STATUS();
//				po.setId(id);
//			}
//			po.setBATCH_PROC_DESC(defpo.getBATCH_PROC_DESC());
//			po.setBATCH_PROC_NAME(defpo.getBATCH_PROC_NAME());
//			po.setBEGIN_TIME(zDateHandler.getTheDateII()+" "+zDateHandler.getTheTime_MS());
//			po.setNOTE("");
//			po.setPROC_STATUS("P");
//			batch_status_Dao.save(po);
			
			
//			thread 版
			
			if(!bat_rpt_th_bo.isRunning()){
				bat_rpt_th_bo.setBizDate(bizdate);
				bat_rpt_th_bo.setClearingphase(clearingphase);
				bat_rpt_th_bo.setBatch_proc_seq(Integer.valueOf(batch_proc_seq));
				System.out.println("bat_rpt_th_bo.isRunning"+bat_rpt_th_bo.isRunning());
				logger.debug("bat_rpt_th_bo.isRunning"+bat_rpt_th_bo.isRunning());
				Thread thread = new Thread(bat_rpt_th_bo);
				thread.start();
			}
			
			/*
			bat_rpt_th_bo.setBizDate(bizdate);
			bat_rpt_th_bo.setClearingphase(clearingphase);
			bat_rpt_th_bo.setBatch_proc_seq(Integer.valueOf(batch_proc_seq));
			System.out.println("bat_rpt_th_bo.isRunning"+bat_rpt_th_bo.isRunning());
			logger.debug("bat_rpt_th_bo.isRunning"+bat_rpt_th_bo.isRunning());
			if(!bat_rpt_th_bo.isRunning()){
				rpt_thread = new Thread(bat_rpt_th_bo);
				rpt_thread.start();
			}else if(bat_rpt_th_bo.isRunning() && rpt_thread !=null){
				bat_rpt_th_bo.setRunning(false) ;
//				rpt_thread.interrupt();
				logger.debug("bat_rpt_th_bo.isRunning()>>"+bat_rpt_th_bo.isRunning() );
				logger.debug("rpt_thread.isInterrupted>>"+rpt_thread.isInterrupted() );
				logger.debug("rpt_thread.interrupted>>"+rpt_thread.interrupted() );
				bat_rpt_th_bo.interrupt();
//				Runtime.getRuntime().exec(command)
//				if(!bat_rpt_th_bo.isRunning() && rpt_thread.i ){
//					
//				}
//				bat_rpt_th_bo.start();
//				rpt_thread.start();
			}
			*/
//			Map<String,String> map = bat_rpt_bo.do_Bat_Rpt(bizdate, clearingphase,Integer.valueOf(batch_proc_seq));
//			
//			po = batch_status_Dao.get(id);
//			po.setEND_TIME(zDateHandler.getTheDateII()+" "+zDateHandler.getTheTime_MS());
//			po.setNOTE(map.get("msg"));
//			if(map.get("result").equals("TRUE")){
//				po.setPROC_STATUS("S");
//			}else{
//				po.setPROC_STATUS("F");
//			}
//			batch_status_Dao.save(po);
			res = true ;
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}
	/**
	 * 執行批次報表
	 * @param bizdate
	 * @param clearingphase
	 * @param batch_proc_seq
	 * @return
	 */
	public boolean doBAT_RPT(String bizdate,String clearingphase ,String batch_proc_seq){
		logger.debug("doBAT_RPT bizdate>>"+bizdate+" , clearingphase>"+clearingphase+" , batch_proc_seq>"+batch_proc_seq);
		boolean res = false ;
		System.out.println("doBAT_RPT is begin...");
		try {
			res = doMAN_RPT(bizdate, clearingphase, batch_proc_seq);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			res=false;
		}
		return res;
	}
	
	
	

	/**
	 * 執行代理業者相關批次報表及資料檔
	 * @param bizdate
	 * @param clearingphase
	 * @param batch_proc_seq
	 * @return
	 */
	public boolean doMAN_AGENT_RPT(String bizdate,String clearingphase ,String batch_proc_seq){
		logger.debug("doMAN_AGENT_RPT bizdate>>"+bizdate+" , clearingphase>"+clearingphase+" , batch_proc_seq>"+batch_proc_seq);
		boolean res = false ;
		System.out.println("doMAN_AGENT_RPT is begin...");
		try {
			if(!bat_agent_data_bo.isRunning()){
				bat_agent_data_bo.setBizDate(bizdate);
				bat_agent_data_bo.setClearingphase(clearingphase);
				bat_agent_data_bo.setBatch_proc_seq(Integer.valueOf(batch_proc_seq));
				System.out.println("bat_agent_data_bo.isRunning"+bat_agent_data_bo.isRunning());
				logger.debug("bat_agent_data_bo.isRunning"+bat_agent_data_bo.isRunning());
				Thread thread = new Thread(bat_agent_data_bo);
				thread.start();
			}
			
			res = true ;
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}
	
	/**
	 * 執行代理業者相關批次報表及資料檔
	 * @param bizdate
	 * @param clearingphase
	 * @param batch_proc_seq
	 * @return
	 */
	public boolean doBAT_AGENT_RPT(String bizdate,String clearingphase ,String batch_proc_seq){
		logger.debug("doBAT_AGENT_RPT bizdate>>"+bizdate+" , clearingphase>"+clearingphase+" , batch_proc_seq>"+batch_proc_seq);
		boolean res = false ;
		System.out.println("doBAT_AGENT_RPT is begin...");
		try {
			res = doMAN_AGENT_RPT(bizdate, clearingphase, batch_proc_seq);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			res=false;
		}
		return res;
	}
	
	/**
	 * 排程剛起時，要將相關排程狀態先寫入以利後續作業
	 * @param bizdate
	 * @param clearingphase
	 * @return
	 */
	public boolean initEach_Batch_Status(String bizdate , String clearingphase){
		boolean res = false ;
		List<EACH_BATCH_DEF> list = getBatDef();
		EACH_BATCH_STATUS_PK id= null;
		EACH_BATCH_STATUS statusPo = null ; 
		try {
			if(list !=null && list.size() !=0){
				for(int i = 0 ; i < list.size() ; i++ ){
					statusPo = new EACH_BATCH_STATUS();
					EACH_BATCH_DEF po = list.get(i);
//					if(po.getBATCH_PROC_SEQ()>2){
					if(po.getBATCH_PROC_SEQ()>=21){
						id = new EACH_BATCH_STATUS_PK(bizdate, clearingphase, po.getBATCH_PROC_SEQ());
						statusPo.setId(id);
						statusPo.setBATCH_PROC_NAME(po.getBATCH_PROC_NAME());
						statusPo.setBATCH_PROC_DESC(po.getBATCH_PROC_DESC());
						statusPo.setPROC_TYPE(po.getPROC_TYPE());
						statusPo.setPROC_STATUS("");//避免後續作業 NULL壞掉
						saveStatus(statusPo);
					}
				}
				res=true;
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}
	
	public Map<String,String> initEach_Batch_MAN(String bizdate , String clearingphase){
		boolean res = false ;
		Map<String , String> retmap = new HashMap<String , String>();
		Map<String , String> pkmap = new HashMap<String , String>();
		pkmap.put("BIZDATE", bizdate);
		pkmap.put("CLEARINGPHASE", clearingphase);
		String tmpbizdate = "";
		try {
			tmpbizdate = DateTimeUtils.convertDate(1, bizdate, "yyyy-MM-dd", "yyyyMMdd");
			if(!isRunBat(tmpbizdate, clearingphase)){
				retmap.put("result", "FALSE");
				retmap.put("msg", "尚不可執行，目前營業日:"+bizdate+",清算階段:"+clearingphase+"批次ONBLOCK or ONPENDING 尚未完成");
				return retmap;
			}
			
			if(! initEach_Batch_Status_MAN(bizdate, clearingphase)){
				retmap.put("result", "FALSE");
				retmap.put("msg", "初始化 Each_Batch_Status 失敗...");
				return retmap;
			}
			if(! initEach_Batch_Notify_MAN(bizdate, clearingphase)){
				retmap.put("result", "FALSE");
				retmap.put("msg", "初始化 Each_Batch_Notify 失敗...");
				return retmap;
			}
			retmap.put("result", "TRUE");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			retmap.put("result", " FALSE");
			retmap.put("msg", "初始化 Each_Batch相關檔案失敗..錯誤訊息:"+e);
			return retmap;
		}
		return retmap;
	}
	
	/**
	 * 
	 * 手動起始點執行 需要將相關表格初始化
	 * @param bizdate
	 * @param clearingphase
	 * @return
	 */
	public boolean initEach_Batch_Status_MAN(String bizdate , String clearingphase){
		boolean res = false ;
		List<EACH_BATCH_DEF> list = getBatDef();
		EACH_BATCH_STATUS_PK id= null;
		EACH_BATCH_STATUS statusPo = null ; 
		bizdate = DateTimeUtils.convertDate(1, bizdate, "yyyy-MM-dd", "yyyyMMdd");
		try {
			if(list !=null && list.size() !=0){
				for(int i = 0 ; i < list.size() ; i++ ){
					EACH_BATCH_DEF po = list.get(i);
					System.out.println(bizdate +"," +clearingphase+ ","+ po.getBATCH_PROC_SEQ());
					System.out.println("statusPo>>"+statusPo);
//					if(po.getBATCH_PROC_SEQ()>2){
					if(po.getBATCH_PROC_SEQ()>=21){
						id = new EACH_BATCH_STATUS_PK(bizdate, clearingphase, po.getBATCH_PROC_SEQ());
						statusPo = batch_status_Dao.get(id);
						if(statusPo == null){
							statusPo = new EACH_BATCH_STATUS();
							statusPo.setId(id);
						}
						statusPo.setBATCH_PROC_NAME(po.getBATCH_PROC_NAME());
						statusPo.setBATCH_PROC_DESC(po.getBATCH_PROC_DESC());
						statusPo.setPROC_TYPE(po.getPROC_TYPE());
						statusPo.setPROC_STATUS("");//避免後續作業 NULL壞掉
						statusPo.setBEGIN_TIME(null);
						statusPo.setEND_TIME(null);
						statusPo.setNOTE("");
						batch_status_Dao.aop_save(statusPo);
					}
				}
				res=true;
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			res=false;
		}
		return res;
	}
	
	public boolean initEach_Batch_Notify_MAN(String bizdate , String clearingphase){
		boolean res = false ;
		EACH_BATCH_NOTIFY_PK id = null ;
		EACH_BATCH_NOTIFY po = null ;
		try {
			bizdate = DateTimeUtils.convertDate(1, bizdate, "yyyy-MM-dd", "yyyyMMdd");
			id = new EACH_BATCH_NOTIFY_PK(bizdate, clearingphase);
			po = batch_notify_Dao.get(id);
			if(po == null ){
				po = new EACH_BATCH_NOTIFY();
				po.setId(id);
			}
//			po.setCLEAR_NOTIFY("N");
			po.setRPT_NOTIFY("");
			batch_notify_Dao.aop_save(po);
			res = true ;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.debug("initEach_Batch_Notify_MAN.Exception>>"+e);
			System.out.println("initEach_Batch_Notify_MAN.Exception>>"+e);
			res = false;
		}
		System.out.println("res>>"+res);
		return res;
	}
	/**
	 * 排程用
	 * @param bizdate
	 * @param clearingphase
	 * @return
	 */
	public boolean initEach_Batch_Notify(String bizdate , String clearingphase){
		boolean res = false ;
		EACH_BATCH_NOTIFY_PK id = null ;
		EACH_BATCH_NOTIFY po = null ;
		try {
			id = new EACH_BATCH_NOTIFY_PK(bizdate, clearingphase);
			po = batch_notify_Dao.get(id);
			if(po == null ){
				po = new EACH_BATCH_NOTIFY();
				po.setId(id);
			}
//			po.setCLEAR_NOTIFY("N");
//			po.setRPT_NOTIFY("N");
//			po.setCLEAR_NOTIFY(null);
			po.setRPT_NOTIFY(null);
			batch_notify_Dao.save(po);
			res = true ;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("initEach_Batch_Notify.Exception>>"+e);
			System.out.println("initEach_Batch_Notify.Exception>>"+e);
			res = false;
		}
		return res;
	}
	
	
	
	public boolean initEach_Batch_Status_BySeq(String bizdate , String clearingphase , String proc_seq){
		boolean res = false ;
		List<EACH_BATCH_DEF> list = batch_def_Dao.find(" FROM tw.org.twntch.po.EACH_BATCH_DEF WHERE BATCH_PROC_SEQ >= ?", Integer.valueOf(proc_seq)) ;
		EACH_BATCH_STATUS_PK id= null;
		EACH_BATCH_STATUS statusPo = null ; 
		bizdate = DateTimeUtils.convertDate(1, bizdate, "yyyy-MM-dd", "yyyyMMdd");
		try {
			if(list !=null && list.size() !=0){
				for(int i = 0 ; i < list.size() ; i++ ){
					EACH_BATCH_DEF po = list.get(i);
					System.out.println(bizdate +"," +clearingphase+ ","+ po.getBATCH_PROC_SEQ());
					id = new EACH_BATCH_STATUS_PK(bizdate, clearingphase, po.getBATCH_PROC_SEQ());
					statusPo = batch_status_Dao.get(id);
					System.out.println("statusPo>>"+statusPo);
					if(statusPo == null){
						statusPo = new EACH_BATCH_STATUS();
						statusPo.setId(id);
					}
					statusPo.setBATCH_PROC_NAME(po.getBATCH_PROC_NAME());
					statusPo.setBATCH_PROC_DESC(po.getBATCH_PROC_DESC());
					statusPo.setPROC_TYPE(po.getPROC_TYPE());
					statusPo.setPROC_STATUS("");//避免後續作業 NULL壞掉
//					statusPo.setBEGIN_TIME("");
//					statusPo.setEND_TIME("");
					statusPo.setBEGIN_TIME(null);
					statusPo.setEND_TIME(null);
					statusPo.setNOTE("");
					batch_status_Dao.aop_save(statusPo);
				}
				res=true;
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			res=false;
		}
		return res;
	}

	/**
	 * 依照DATEMODE取得目前營業日、清算階段及前一清算階段、營業日(for AJAX)
	 * @return
	 */
	public String getBusinessDateAndClrphase(Map<String, String> params){
		Map<String, String> data = new HashMap<String, String>();
		List<EACHSYSSTATUSTAB> list = null;
		String businessDate = "", clrphase = "";
		String pre_businessDate = "", pre_clrphase = "";
		
		try{
			list = eachsysstatustab_Dao.getBusinessDate();
			if(list != null && list.size() > 0){
				businessDate = list.get(0).getBUSINESS_DATE();
				clrphase = list.get(0).getCLEARINGPHASE();
			}
			Map dateData = eachsysstatustab_bo.getRowData();
			//前一階段的營業日
	//		pre_businessDate = (String) dateData.get("THISDATE");
	//		時間為中午12前抓PREVDATE 12:00 後抓THISDATE
			if(zDateHandler.isInTimeRange(zDateHandler.getTheTime(), "00:00:00", "12:00:00")){
//			假日測試用
//			if(zDateHandler.isInTimeRange(zDateHandler.getTheTime(), "00:00:00", "15:10:00")){
				businessDate = (String) dateData.get("PREVDATE");
				pre_businessDate = (String) dateData.get("PREVDATE");
				System.out.println("businessDate>>"+businessDate);
				System.out.println("pre_businessDate>>"+pre_businessDate);
				businessDate = DateTimeUtils.convertDate(1, businessDate, "yyyyMMdd", "yyyyMMdd"); 
				pre_businessDate = DateTimeUtils.convertDate(1, pre_businessDate, "yyyyMMdd", "yyyyMMdd"); 
				pre_clrphase = (clrphase.equals("01")? "02" : "01");
			}
//			else if(zDateHandler.isInTimeRange(zDateHandler.getTheTime(), "12:00:00", "15:30:00")){
//				businessDate = (String) dateData.get("THISDATE");
//				pre_businessDate = (String) dateData.get("PREVDATE");
//				System.out.println("time in 12:00:00~15:30:00 >>");
//				System.out.println("businessDate>>"+businessDate);
//				System.out.println("pre_businessDate>>"+pre_businessDate);
//				businessDate = DateTimeUtils.convertDate(1, businessDate, "yyyyMMdd", "yyyyMMdd"); 
//				pre_businessDate = DateTimeUtils.convertDate(1, pre_businessDate, "yyyyMMdd", "yyyyMMdd"); 
//				pre_clrphase = (clrphase.equals("01")? "02" : "01");
//			}
			else{
//				businessDate = zDateHandler.getTWDate();
//				pre_businessDate = zDateHandler.getTWDate();
//				如果是營業日
				if(wk_date_bo.isTxnDate()){
					businessDate = (String) dateData.get("THISDATE");
					pre_businessDate = (String) dateData.get("THISDATE");
				}else{//非營業日
					businessDate = (String) dateData.get("PREVDATE");
					pre_businessDate = (String) dateData.get("PREVDATE");
				}
			
				businessDate = DateTimeUtils.convertDate(1, businessDate, "yyyyMMdd", "yyyyMMdd"); 
				pre_businessDate = DateTimeUtils.convertDate(1, pre_businessDate, "yyyyMMdd", "yyyyMMdd"); 
				//前一階段的清算階段
				pre_clrphase = (clrphase.equals("01")? "02" : "01");
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		data.put("bizdate", DateTimeUtils.convertDate(DateTimeUtils.NOT_INTERCONVERSION, businessDate, "yyyyMMdd", "yyyy-MM-dd"));
		data.put("clearingphase", clrphase);
		data.put("pre_bizdate", DateTimeUtils.convertDate(DateTimeUtils.NOT_INTERCONVERSION, pre_businessDate, "yyyyMMdd", "yyyy-MM-dd"));
		data.put("pre_clearingphase", pre_clrphase);
		data.put("sysdate", zDateHandler.getODDate());
		
		return JSONUtils.map2json(data);
	}
	
	/**
	 * 
	 * @param params
	 * @return
	 */
	public Map<String,String> getExeBatBizDateAndCl(){
		Map<String, String> data = new HashMap<String, String>();
		List<EACHSYSSTATUSTAB> list = null;
		String businessDate = "", clrphase = "";
		String pre_businessDate = "", pre_clrphase = "";
		
		try{
			list = eachsysstatustab_Dao.getBusinessDate();
			if(list != null && list.size() > 0){
				businessDate = list.get(0).getBUSINESS_DATE();
				clrphase = list.get(0).getCLEARINGPHASE();
			}
			Map dateData = eachsysstatustab_bo.getRowData();
			//前一階段的營業日
			//		pre_businessDate = (String) dateData.get("THISDATE");
			//		時間為中午12前抓PREVDATE 12:00 後抓THISDATE
			
			if(zDateHandler.isInTimeRange(zDateHandler.getTheTime(), "00:00:00", "12:00:00")){
//			假日測試用
//			if(zDateHandler.isInTimeRange(zDateHandler.getTheTime(), "00:00:00", "15:10:00")){
				businessDate = (String) dateData.get("PREVDATE");
				pre_businessDate = (String) dateData.get("PREVDATE");
				System.out.println("businessDate>>"+businessDate);
				System.out.println("pre_businessDate>>"+pre_businessDate);
				businessDate = DateTimeUtils.convertDate(1, businessDate, "yyyyMMdd", "yyyyMMdd"); 
				pre_businessDate = DateTimeUtils.convertDate(1, pre_businessDate, "yyyyMMdd", "yyyyMMdd"); 
			}else{
//				businessDate = zDateHandler.getTWDate();
//				pre_businessDate = zDateHandler.getTWDate();
				businessDate = (String) dateData.get("THISDATE");
				pre_businessDate = (String) dateData.get("THISDATE");
//				businessDate = DateTimeUtils.convertDate(1, businessDate, "yyyyMMdd", "yyyyMMdd"); 
//				pre_businessDate = DateTimeUtils.convertDate(1, pre_businessDate, "yyyyMMdd", "yyyyMMdd"); 
				//前一階段的清算階段
			}
			pre_clrphase = (clrphase.equals("01")? "02" : "01");
		}catch(Exception e){
			e.printStackTrace();
		}
		data.put("pre_bizdate",  pre_businessDate);
		data.put("pre_clearingphase", pre_clrphase);
		return data;
	}
	
	/**
	 * 查詢是否已發佈通知目前執行階段的上一個階段是否已發佈通知
	 * @param bizdate
	 * @param clearingphase
	 * @return
	 */
	public boolean checkPreNotify(String bizdate , String clearingphase){
		boolean res = false;
		String prevdate = "";
		EACH_BATCH_NOTIFY_PK id = null ;
		System.out.println("checkPreNotify.input>>bizdate="+bizdate+", clearingphase>>"+clearingphase);
		if(clearingphase.equals("01")){
//			TODO 要找上個營業日的"02"階段RPTNOTIFY = Y
			Map<String,String> dateData = eachsysstatustab_bo.getRowData();	
			prevdate = dateData.get("PREVDATE");
			System.out.println("checkPreNotify.prevdate>>"+prevdate);
			id = new EACH_BATCH_NOTIFY_PK(prevdate, "02");
			EACH_BATCH_NOTIFY po = batch_notify_Dao.get(id);
			System.out.println("checkPreNotify.po(01)>>"+po);
			if(po == null){
				res = false;
			}else{
				if(StrUtils.isNotEmpty(po.getRPT_NOTIFY()) &&  po.getRPT_NOTIFY().equals("Y")){
					res = true;
				}else{
					res = false;
				}
			}
		}else{
//			根據傳入的bizdate直接去查詢01階段
			id = new EACH_BATCH_NOTIFY_PK(bizdate, "01");
			EACH_BATCH_NOTIFY po = batch_notify_Dao.get(id);
			System.out.println("checkPreNotify.po(02)>>"+po);
			if(po == null){
				res = false;
			}else{
				if(StrUtils.isNotEmpty(po.getRPT_NOTIFY()) && po.getRPT_NOTIFY().equals("Y")){
					res = true;
				}else{
					res = false;
				}
			}
		}
		System.out.println("res>>"+res);
		
		return res;
		
	}
	/**
	 * 檢查目前這一階段的排程是否已執行
	 * @param bizdate
	 * @param clearingphase
	 * @return
	 */
	public boolean checkNotify(String bizdate , String clearingphase){
		boolean res = false;
		String prevdate = "";
		EACH_BATCH_NOTIFY_PK id = null ;
		System.out.println("checkNotify.input>>bizdate="+bizdate+", clearingphase>>"+clearingphase);
//			根據傳入的bizdate,clearingphase直接去查詢
		id = new EACH_BATCH_NOTIFY_PK(bizdate, clearingphase);
		EACH_BATCH_NOTIFY po = batch_notify_Dao.get(id);
		System.out.println("checkNotify.po(02)>>"+po);
		//po==null表示排程不管是手動還是自動都尚未執行  ，!=null 表示已執行過
		if(po == null){
			res = true;
		}else {
			res = false;
		}
		System.out.println("res>>"+res);
		
		return res;
		
	}
	/**
	 * 檢查目前這一階段的排程是否已執行
	 * @param bizdate
	 * @param clearingphase
	 * @return
	 */
	public boolean checkBatIsRun(String bizdate , String clearingphase){
		boolean res = false;
		logger.debug("checkBatIsRun.input>>bizdate="+bizdate+", clearingphase>>"+clearingphase);
//			根據傳入的bizdate,clearingphase直接去查詢
		List<EACH_BATCH_STATUS> list = batch_status_Dao.find(" FROM tw.org.twntch.po.EACH_BATCH_STATUS WHERE BATCH_PROC_SEQ > 20 AND BIZDATE = ? AND CLEARINGPHASE = ? ", bizdate ,clearingphase );
		logger.debug("checkBatIsRun.list="+list);
		//list==null表示排程不管是手動還是自動都尚未執行  ，!=null or list.size()>0 表示已執行過
		if(list != null && list.size()>0){
			res = true;
		} else{
			res = false;
		}
		logger.debug("res>>"+res);
		return res;
		
	}
	
	/**
	 * 
	 * @param bizdate
	 * @param clearingphase
	 * @param proc_seq
	 * @return
	 */
	public boolean write_Batch_Status_BySeq(String bizdate , String clearingphase , String proc_seq ,String status, String note){
		boolean res = false ;
		List<EACH_BATCH_DEF> list = batch_def_Dao.find(" FROM tw.org.twntch.po.EACH_BATCH_DEF WHERE BATCH_PROC_SEQ = ?", Integer.valueOf(proc_seq)) ;
		EACH_BATCH_STATUS_PK id= null;
		EACH_BATCH_STATUS statusPo = null ; 
//		bizdate = DateTimeUtils.convertDate(1, bizdate, "yyyy-MM-dd", "yyyyMMdd");
		try {
			if(list !=null && list.size() !=0){
				for(int i = 0 ; i < list.size() ; i++ ){
					EACH_BATCH_DEF po = list.get(i);
					System.out.println(bizdate +"," +clearingphase+ ","+ po.getBATCH_PROC_SEQ());
					id = new EACH_BATCH_STATUS_PK(bizdate, clearingphase, po.getBATCH_PROC_SEQ());
					statusPo = batch_status_Dao.get(id);
					System.out.println("statusPo>>"+statusPo);
					if(statusPo == null){
						statusPo = new EACH_BATCH_STATUS();
						statusPo.setId(id);
					}
					statusPo.setBATCH_PROC_NAME(po.getBATCH_PROC_NAME());
					statusPo.setBATCH_PROC_DESC(po.getBATCH_PROC_DESC());
					statusPo.setPROC_TYPE(po.getPROC_TYPE());
					statusPo.setPROC_STATUS(status);//避免後續作業 NULL壞掉
					if("P".equalsIgnoreCase(status)){
						statusPo.setBEGIN_TIME(zDateHandler.getTimestamp());
					}
					if("S".equalsIgnoreCase(status) || "F".equalsIgnoreCase(status)){
						statusPo.setEND_TIME(zDateHandler.getTimestamp());
					}
					statusPo.setNOTE(note);
					batch_status_Dao.aop_save(statusPo);
				}
				res=true;
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			res=false;
		}
		return res;
	}
	
	
	
	/**
	 * 單獨執行批次 測試用
	 * @param param
	 * @return
	 */
	public String doBatBySeqTest(Map<String, String> param){
		String json = "";
		String bizdate =StrUtils.isNotEmpty(param.get("BIZDATE"))? param.get("BIZDATE"):"";
		String clearingphase =StrUtils.isNotEmpty(param.get("CLEARINGPHASE"))? param.get("CLEARINGPHASE"):"";
		String batch_proc_seq =StrUtils.isNotEmpty(param.get("BATCH_PROC_SEQ"))? param.get("BATCH_PROC_SEQ"):"";
		String action =StrUtils.isNotEmpty(param.get("action"))? param.get("action"):"";
		Map<String,String> retmap = new HashMap<String,String>();
		Boolean result = Boolean.FALSE;
		retmap.put("result", "FALSE");
		int seq = 99;
		List<EACH_BATCH_STATUS> list = null ;
		try {
			seq = Integer.valueOf(batch_proc_seq);
			bizdate = DateTimeUtils.convertDate(1, bizdate, "yyyy-MM-dd", "yyyyMMdd");
				switch (seq) {
				case 27:
					result = doMAN_RPONBLOCKTAB(bizdate, clearingphase, batch_proc_seq);
					break;
				case 21:
					result = doMAN_ADDNEWSC(bizdate, clearingphase, batch_proc_seq);
					break;
				case 24:
					result = doMAN_MAKE_TXN_COMPANY_FEE(bizdate, clearingphase, batch_proc_seq);
					break;
//TODO 剩下的客戶無此需求 先不寫
				default:
					retmap.put("msg", "fail，無相關api ,SEQ>>"+seq);
					break;
				}
			if(result){
				retmap.put("result", result.toString());
			}	
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			retmap.put("msg", "doBatBySeqTest error...");
		}
		json = JSONUtils.map2json(retmap);
		return json;
	}
	
	
	
	public EACH_BATCH_DEF_Dao getBatch_def_Dao() {
		return batch_def_Dao;
	}
	public void setBatch_def_Dao(EACH_BATCH_DEF_Dao batch_def_Dao) {
		this.batch_def_Dao = batch_def_Dao;
	}
	public EACH_BATCH_NOTIFY_Dao getBatch_notify_Dao() {
		return batch_notify_Dao;
	}
	public void setBatch_notify_Dao(EACH_BATCH_NOTIFY_Dao batch_notify_Dao) {
		this.batch_notify_Dao = batch_notify_Dao;
	}
	public EACH_BATCH_STATUS_Dao getBatch_status_Dao() {
		return batch_status_Dao;
	}
	public void setBatch_status_Dao(EACH_BATCH_STATUS_Dao batch_status_Dao) {
		this.batch_status_Dao = batch_status_Dao;
	}
	public SYS_PARA_Dao getSys_para_Dao() {
		return sys_para_Dao;
	}
	public void setSys_para_Dao(SYS_PARA_Dao sys_para_Dao) {
		this.sys_para_Dao = sys_para_Dao;
	}
	public EACHSYSSTATUSTAB_Dao getEachsysstatustab_Dao() {
		return eachsysstatustab_Dao;
	}
	public void setEachsysstatustab_Dao(EACHSYSSTATUSTAB_Dao eachsysstatustab_Dao) {
		this.eachsysstatustab_Dao = eachsysstatustab_Dao;
	}
	
	public EACHSYSSTATUSTAB_BO getEachsysstatustab_bo() {
		return eachsysstatustab_bo;
	}
	
	public void setEachsysstatustab_bo(EACHSYSSTATUSTAB_BO eachsysstatustab_bo) {
		this.eachsysstatustab_bo = eachsysstatustab_bo;
	}

	public AP_PAUSE_Dao getAp_pause_Dao() {
		return ap_pause_Dao;
	}


	public void setAp_pause_Dao(AP_PAUSE_Dao ap_pause_Dao) {
		this.ap_pause_Dao = ap_pause_Dao;
	}




	public SocketClient getSocketClient() {
		return socketClient;
	}




	public void setSocketClient(SocketClient socketClient) {
		this.socketClient = socketClient;
	}



	public SETTLEMENTLOGTAB_Dao getSettlementlogtab_Dao() {
		return settlementlogtab_Dao;
	}

	public void setSettlementlogtab_Dao(SETTLEMENTLOGTAB_Dao settlementlogtab_Dao) {
		this.settlementlogtab_Dao = settlementlogtab_Dao;
	}

	public EACH_BATCH_Dao getEach_batch_Dao() {
		return each_batch_Dao;
	}

	public void setEach_batch_Dao(EACH_BATCH_Dao each_batch_Dao) {
		this.each_batch_Dao = each_batch_Dao;
	}

	public WK_DATE_BO getWk_date_bo() {
		return wk_date_bo;
	}

	public void setWk_date_bo(WK_DATE_BO wk_date_bo) {
		this.wk_date_bo = wk_date_bo;
	}


	public BAT_RPT_BO getBat_rpt_bo() {
		return bat_rpt_bo;
	}


	public void setBat_rpt_bo(BAT_RPT_BO bat_rpt_bo) {
		this.bat_rpt_bo = bat_rpt_bo;
	}


	public BAT_RPT_TH_BO getBat_rpt_th_bo() {
		return bat_rpt_th_bo;
	}


	public void setBat_rpt_th_bo(BAT_RPT_TH_BO bat_rpt_th_bo) {
		this.bat_rpt_th_bo = bat_rpt_th_bo;
	}


	public RunSchedule getRunschedule() {
		return runschedule;
	}


	public void setRunschedule(RunSchedule runschedule) {
		this.runschedule = runschedule;
	}


	public EACH_USERLOG_BO getUserlog_bo() {
		return userlog_bo;
	}


	public void setUserlog_bo(EACH_USERLOG_BO userlog_bo) {
		this.userlog_bo = userlog_bo;
	}


	public BULLETIN_BO getBulletin_bo() {
		return bulletin_bo;
	}


	public void setBulletin_bo(BULLETIN_BO bulletin_bo) {
		this.bulletin_bo = bulletin_bo;
	}


	public SC_COMPANY_PROFILE_BO getSc_com_bo() {
		return sc_com_bo;
	}


	public void setSc_com_bo(SC_COMPANY_PROFILE_BO sc_com_bo) {
		this.sc_com_bo = sc_com_bo;
	}


	public BAT_AGENT_DATA_BO getBat_agent_data_bo() {
		return bat_agent_data_bo;
	}


	public void setBat_agent_data_bo(BAT_AGENT_DATA_BO bat_agent_data_bo) {
		this.bat_agent_data_bo = bat_agent_data_bo;
	}
	



}
