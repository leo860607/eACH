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
import tw.org.twntch.db.dao.hibernate.NONCLEAR_BATCH_STATUS_Dao;
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
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.RunSchedule;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.zDateHandler;

public class NONCLEAR_BATCH_BO {
private EACH_BATCH_DEF_Dao batch_def_Dao  ;
private EACH_BATCH_NOTIFY_Dao batch_notify_Dao  ;
private EACH_BATCH_STATUS_Dao batch_status_Dao  ;
private NONCLEAR_BATCH_STATUS_Dao nonclear_batch_status_Dao  ;
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
private RunSchedule runschedule ;
private EACH_USERLOG_BO userlog_bo;
private BULLETIN_BO bulletin_bo;
private SC_COMPANY_PROFILE_BO sc_com_bo;
private Thread rpt_thread ;
private Logger logger = Logger.getLogger(getClass());


//單獨執行批次
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
//			retmap = doBatByBeforeThreeSeq(bizdate, clearingphase , action, seq);
		retmap = bat_send(bizdate, clearingphase , seq);
		userlog_bo.genericLog("M", "成功，重新執行中...", action, null, new HashMap<String,String>());
	
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
//			發送電文通知給GATEWAY，。
			retmap =  bat_send(bizdate, clearingphase ,batch_proc_seq);
			if(!retmap.get("result").equals("TRUE")){
				return retmap;
			}
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

//<?xml version="1.0" encoding="UTF-8" standalone="yes"?> 
//<msg> 
//    <header> 
//        <SystemHeader>eACH01</SystemHeader> 
//        <MsgType>0100</MsgType> 
//        <PrsCode>9103</PrsCode> 
//        <Stan>XXXXXXX</Stan> 
//        <InBank>0000000</InBank> 
//        <OutBank>9990000</OutBank> 
//        <DateTime>YYYYMMDDHHMMSS</DateTime> 
//        <RspCode>0000</RspCode> 
//    </header> 
//    <body> 
//         <BizDate>20151113</BizDate> 
//         <ClearingPhase>01</ClearingPhase> 
//        <BatchProcSeq></BatchProcSeq> 
//    </body> 
//</msg> 
//
//BatchProcSeq 
//   1-代理銀行發動con1 
//   2-未完成檔操作行/清算行修改 
//   3-圈存扣款交易失敗原因分類 

	
	try {
		Header msgHeader = new Header();
		msgHeader.setSystemHeader("eACH01");
		msgHeader.setMsgType("0100");
		msgHeader.setPrsCode("9103");
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
		msgBody.setBatchProcSeq(String.valueOf(batch_proc_seq));
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
 *原則上 16:20:00~00:00:00後取THISDATE 00:00:00~16:20:00  取PRVE DATE
 *更正req by 李建利 同清算階段的換日時間
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
		if(zDateHandler.isInTimeRange(zDateHandler.getTheTime(), "00:00:00", "14:54:59")){
//		假日測試用
//		if(zDateHandler.isInTimeRange(zDateHandler.getTheTime(), "00:00:00", "15:10:00")){
			businessDate = (String) dateData.get("PREVDATE");
			pre_businessDate = (String) dateData.get("PREVDATE");
			System.out.println("businessDate>>"+businessDate);
			System.out.println("pre_businessDate>>"+pre_businessDate);
			businessDate = DateTimeUtils.convertDate(1, businessDate, "yyyyMMdd", "yyyyMMdd"); 
			pre_businessDate = DateTimeUtils.convertDate(1, pre_businessDate, "yyyyMMdd", "yyyyMMdd"); 
			pre_clrphase = (clrphase.equals("01")? "02" : "01");
		}
		else if(zDateHandler.isInTimeRange(zDateHandler.getTheTime(), "14:55:00:00", "15:30:00")){
			businessDate = (String) dateData.get("THISDATE");
			pre_businessDate = (String) dateData.get("PREVDATE");
			System.out.println("time in 12:00:00~15:30:00 >>");
			System.out.println("businessDate>>"+businessDate);
			System.out.println("pre_businessDate>>"+pre_businessDate);
			businessDate = DateTimeUtils.convertDate(1, businessDate, "yyyyMMdd", "yyyyMMdd"); 
			pre_businessDate = DateTimeUtils.convertDate(1, pre_businessDate, "yyyyMMdd", "yyyyMMdd"); 
			pre_clrphase = (clrphase.equals("01")? "02" : "01");
		}
		else{
//			如果是營業日
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
		}
		list = nonclear_batch_status_Dao.find(" FROM tw.org.twntch.po.NONCLEAR_BATCH_STATUS WHERE BIZDATE = ? AND CLEARINGPHASE = ? ORDER BY PROC_TYPE , BATCH_PROC_SEQ " , bizdate ,clearingphase );
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	System.out.println("list>>"+list);
	list = list.size() == 0 ? null : list;
	return list;
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

	public NONCLEAR_BATCH_STATUS_Dao getNonclear_batch_status_Dao() {
		return nonclear_batch_status_Dao;
	}

	public void setNonclear_batch_status_Dao(
			NONCLEAR_BATCH_STATUS_Dao nonclear_batch_status_Dao) {
		this.nonclear_batch_status_Dao = nonclear_batch_status_Dao;
	}
	



}
