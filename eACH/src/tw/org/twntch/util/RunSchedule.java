package tw.org.twntch.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import tw.org.twntch.bo.Arguments;
import tw.org.twntch.bo.BASEDATA_DOWNLOAD_BO;
import tw.org.twntch.bo.CLEARDAY_DOWNLOAD_BO;
import tw.org.twntch.bo.FEEDAY_DOWNLOAD_BO;
import tw.org.twntch.bo.FEEMONTH_DOWNLOAD_BO;
import tw.org.twntch.bo.PROXY_CL_BO;
import tw.org.twntch.bo.TRANSACTIONDAY_DOWNLOAD_BO;
import tw.org.twntch.bo.TRANSACTIONDETAIL_DOWNLOAD_BO;
import tw.org.twntch.bo.TRANSACTIONMONTH_DOWNLOAD_BO;
import tw.org.twntch.db.dao.hibernate.BANK_GROUP_Dao;
import tw.org.twntch.db.dao.hibernate.EACH_BATCH_DEF_Dao;
import tw.org.twntch.db.dao.hibernate.EACH_BATCH_STATUS_Dao;
import tw.org.twntch.po.BANK_GROUP;
import tw.org.twntch.po.EACH_BATCH_DEF;
import tw.org.twntch.po.EACH_BATCH_STATUS;
import tw.org.twntch.po.EACH_BATCH_STATUS_PK;

public class RunSchedule implements Runnable{
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private TRANSACTIONDETAIL_DOWNLOAD_BO transactiondetail_download_bo;
	private BASEDATA_DOWNLOAD_BO basedata_download_bo;
	private TRANSACTIONDAY_DOWNLOAD_BO transactionday_download_bo;
	private TRANSACTIONMONTH_DOWNLOAD_BO transactionmonth_download_bo;
	private FEEDAY_DOWNLOAD_BO feeday_download_bo;
	private FEEMONTH_DOWNLOAD_BO feemonth_download_bo;
	private CLEARDAY_DOWNLOAD_BO clearday_download_bo;
	private PROXY_CL_BO proxy_cl_bo;
	private BANK_GROUP_Dao bank_group_Dao;
	private CodeUtils codeUtils;
	
	private EACH_BATCH_STATUS_Dao batch_status_Dao  ;
	private EACH_BATCH_DEF_Dao batch_def_Dao  ;
	private String bizDate;
	private String YYYYMM;
	private String clearingphase;
	private Integer batch_proc_seq;
	private boolean isRunning = false;
	private boolean isMonth = false;
	@Override
	public void run() {
		boolean result = false;
		try {
			isRunning = true;
			initData();
			Map<String,String> map = doBAT_RPDATADOWNLOAD(bizDate, clearingphase, isMonth, YYYYMM);
			result = map.get("result").equals("TRUE")?true:false;
			finishData(result, map.get("msg"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.debug("BAT_RPT_TH_BO.run.Exception>>"+e);
		}finally{
			isRunning = false;
		}
		
	}
	
	public boolean initData(){
		boolean reault = false;
		EACH_BATCH_STATUS po = null;
		EACH_BATCH_DEF defpo = null;
		EACH_BATCH_STATUS_PK id = new EACH_BATCH_STATUS_PK(bizDate, clearingphase, Integer.valueOf(batch_proc_seq));
		try {
			po = batch_status_Dao.get(id);
			defpo =  batch_def_Dao.get(Integer.valueOf(batch_proc_seq));
			if(po==null){
				po = new EACH_BATCH_STATUS();
				po.setId(id);
			}
			po.setBATCH_PROC_DESC(defpo.getBATCH_PROC_DESC());
			po.setBATCH_PROC_NAME(defpo.getBATCH_PROC_NAME());
			po.setBEGIN_TIME(zDateHandler.getTheDateII()+" "+zDateHandler.getTheTime_MS());
			po.setNOTE("");
			po.setPROC_STATUS("P");
			batch_status_Dao.aop_save(po);
			reault = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.debug("initData.Exception>>"+e);
		}
		return reault;
	}
	public boolean finishData(boolean arg , String msg){
		boolean reault = false;
		EACH_BATCH_STATUS po = null;
		EACH_BATCH_DEF defpo = null;
		EACH_BATCH_STATUS_PK id = new EACH_BATCH_STATUS_PK(bizDate, clearingphase, Integer.valueOf(batch_proc_seq));
		try {
			po = batch_status_Dao.get(id);
			defpo =  batch_def_Dao.get(Integer.valueOf(batch_proc_seq));
			if(po==null){
				po = new EACH_BATCH_STATUS();
				po.setId(id);
				po.setBATCH_PROC_DESC(defpo.getBATCH_PROC_DESC());
				po.setBATCH_PROC_NAME(defpo.getBATCH_PROC_NAME());
			}
			po = batch_status_Dao.get(id);
			po.setEND_TIME(zDateHandler.getTheDateII()+" "+zDateHandler.getTheTime_MS());
			po.setNOTE(msg);
			if(arg){
				po.setPROC_STATUS("S");
			}else{
				po.setPROC_STATUS("F");
			}
			batch_status_Dao.aop_save(po);
			reault = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.debug("finishData.Exception>>"+e);
		}
		return reault;
	}
	
	public EACH_BATCH_STATUS tempSave(EACH_BATCH_STATUS_PK id , EACH_BATCH_STATUS po ,String msg){
		if(po != null){
			po.setNOTE(msg.toString());
			batch_status_Dao.aop_save(po);
		}
		return po = batch_status_Dao.get(id);
	}
	
	
	/**
	 * @param bizdate//營業日期
	 * @param clearingphase//清算階段
	 * @param isMonth//判斷是否跑月統計 Y:是 ,N:否
	 * @param month//月統計的月份
	 * @return {result=true,msg=基本資料下載:成功 ,共用代碼資料下載:成功}
	 */
	public Map<String,String> doBAT_RPDATADOWNLOAD(String bizdate,String clearingphase,boolean isMonth,String YYYYMM){
		//回傳的Map
		Map<String,String> resultMap = new HashMap<String,String>();
		//最後回傳的訊息
		String resultMessage = "";
		//最後回傳的結果
		String result = "TRUE";
		//訊息
		String message = "";
		Map<String,String> valueMap = null ;
		//取得在classpath下的properties檔案
		try {
			valueMap = codeUtils.getPropertyValue("Configuration.properties","transactiondetailDirPath","transactiondetailFilePrefix","basedataDirPath","basedataFilePrefix","transactiondayDirPath","transactiondayFilePrefix","transactionmonthDirPath","transactionmonthFilePrefix","feedayDirPath","feedayFilePrefix","feemonthDirPath","feemonthFilePrefix","cleardayDirPath","cleardayFilePrefix","proxybankDirPath","basedataFilePrefix_C","basedataFilePrefix_UTF8","basedataFilePrefix_C_UTF8");
			//無法取得properties
			if(valueMap == null){
				resultMap.put("msg","無法取得properties檔");
				resultMap.put("result","FALSE");
				return resultMap;
			}
			//取得放置TXT或ZIP檔案的資料夾根目錄
			WebServerPath webserverpath = (WebServerPath)  SpringAppCtxHelper.getBean("webserverpath");
//		windowdir=eACH/tmp/
			String windowdir =  webserverpath.getServerRootUrl()+"/"+Arguments.getStringArg("RPT.PDF.PATH")+ "/";
			String transactiondetailDirPath = OSValidator.isWindows() ? windowdir :valueMap.get("transactiondetailDirPath");
			logger.debug("transactiondetailDirPath="+transactiondetailDirPath);
			String basedataDirPath = OSValidator.isWindows() ? windowdir :valueMap.get("basedataDirPath");
			logger.debug("basedataDirPath="+basedataDirPath);
			String transactiondayDirPath = OSValidator.isWindows() ? windowdir :valueMap.get("transactiondayDirPath");
			logger.debug("transactiondayDirPath="+transactiondayDirPath);
			String transactionmonthDirPath = OSValidator.isWindows() ? windowdir :valueMap.get("transactionmonthDirPath");
			logger.debug("transactionmonthDirPath="+transactionmonthDirPath);
			String feedayDirPath = OSValidator.isWindows() ? windowdir :valueMap.get("feedayDirPath");
			logger.debug("feedayDirPath="+feedayDirPath);
			String feemonthDirPath = OSValidator.isWindows() ? windowdir :valueMap.get("feemonthDirPath");
			logger.debug("feemonthDirPath="+feemonthDirPath);
			String cleardayDirPath = OSValidator.isWindows() ? windowdir :valueMap.get("cleardayDirPath");
			logger.debug("cleardayDirPath="+cleardayDirPath);
			String proxybankDirPath = OSValidator.isWindows() ? windowdir :valueMap.get("proxybankDirPath");
			logger.debug("proxybankDirPath="+proxybankDirPath);
			//取得TXT或ZIP檔名的前置碼
			String transactiondetailFilePrefix = valueMap.get("transactiondetailFilePrefix");
			logger.debug("transactiondetailFilePrefix="+transactiondetailFilePrefix);
			String basedataFilePrefix = valueMap.get("basedataFilePrefix");
			logger.debug("basedataFilePrefix="+basedataFilePrefix);
			String basedataFilePrefix_C = valueMap.get("basedataFilePrefix_C");
			logger.debug("basedataFilePrefix_C="+basedataFilePrefix_C);
			//UTF8
			String basedataFilePrefix_UTF8 = valueMap.get("basedataFilePrefix_UTF8");
			logger.debug("basedataFilePrefix_UTF8="+basedataFilePrefix_UTF8);
			String basedataFilePrefix_C_UTF8 = valueMap.get("basedataFilePrefix_C_UTF8");
			logger.debug("basedataFilePrefix_C_UTF8="+basedataFilePrefix_C_UTF8);
			
			String transactiondayFilePrefix = valueMap.get("transactiondayFilePrefix");
			logger.debug("transactiondayFilePrefix="+transactiondayFilePrefix);
			String transactionmonthFilePrefix = valueMap.get("transactionmonthFilePrefix");
			logger.debug("transactionmonthFilePrefix="+transactionmonthFilePrefix);
			String feedayFilePrefix = valueMap.get("feedayFilePrefix");
			logger.debug("feedayFilePrefix="+feedayFilePrefix);
			String feemonthFilePrefix = valueMap.get("feemonthFilePrefix");
			logger.debug("feemonthFilePrefix="+feemonthFilePrefix);
			String cleardayFilePrefix = valueMap.get("cleardayFilePrefix");
			logger.debug("cleardayFilePrefix="+cleardayFilePrefix);
			/////////////各功能會共用的變數////////////////////
			//Zip的Byte[]
			byte[] byteZIP;
			EACH_BATCH_STATUS_PK id = new EACH_BATCH_STATUS_PK(bizdate, clearingphase, batch_proc_seq);
			id = new EACH_BATCH_STATUS_PK(bizdate, clearingphase, batch_proc_seq);
			StringBuffer msg = new StringBuffer();
			String str1 = "1.交易明細資料檔下載排程：#1-1 #1-2 #1-3";
			String str2 = ";2.共用代碼資料下載排程：#2-1 #2-2 #2-3";
			String str2a = ";2a.代理業者共用代碼資料下載排程：#2a-1 #2a-2 #2a-3";
			String str3 = ";3交易日統計資料檔下載排程：#3-1 #3-2 #3-3";
			String str4 = ";3-1.交易月統計資料檔下載排程：#4-1 #4-2 #4-3";
			String str5 = ";4手續費日統計資料檔下載排程：#5-1 #5-2 #5-3";
			String str6 = ";4-1.手續費月統計資料檔下載排程：#6-1 #6-2 #6-3";
			String str7 = ";5.結算日統計資料檔下載排程：#7-1 #7-2 #7-3";
			String str8 = ";6.代理清算行資料檔下載排程：#8-1 #8-2 #8-3";
			String suc = "產生成功";
			String fail = "產生失敗";
			String notyet = "未執行";
			String run = "執行中";
			String tmp1 = "";
			
			////////////////////////////////////
			//撈出所有的操作行代號
			List<BANK_GROUP> bankGroupList = bank_group_Dao.getBgbkIdList_2();
			EACH_BATCH_STATUS po_batch = batch_status_Dao.get(id);
			/*******************以下為交易明細資料檔下載排程*************************************/
			//將該路徑下的交易明細資料的ZIP檔案全部刪除
			for(BANK_GROUP po:bankGroupList){
				//操作行代號
				String opbkId = po.getBGBK_ID();
				//路徑
//				String transactiondetailFilePath = transactiondetailDirPath+"/"+bizdate+"/"+opbkId;
				String transactiondetailFilePath = transactiondetailDirPath+"/"+bizdate+"/"+opbkId+"/"+Arguments.getStringArg("BAT.LAST.PATH")+"/";
				File transactiondetailFile = new File(transactiondetailFilePath);
//				eACH_txlist_0040000_20150513_02.zip
//				String deleteFileName = transactiondetailFilePrefix+"_"+bizdate+"_"+clearingphase+"_"+opbkId+".zip";
				String deleteFileName = transactiondetailFilePrefix+"_"+opbkId +"_"+bizdate+"_"+clearingphase+".zip";
//				20150511 edit by hugo 要先判斷path 是否存在 
				if(transactiondetailFile.exists()){
					for(File f:transactiondetailFile.listFiles()){
//				20150513 edit by hugo 要加副檔名且檔名相同 否則會連同其他類似的的資料都刪除	
//						if(f.getName().contains(transactiondetailFilePrefix)){
						if(f.getName().equals(deleteFileName)){
							f.delete();
						}
					}
				}
				
			}
			tmp1= zDateHandler.getTheTime_MS();
			msg.append(str1.replace("#1-1", run).replace("#1-2" , " start "+tmp1).replace("#1-3" , ""));
			msg.append(str2.replace("#2-1", notyet).replace("#2-2" , "").replace("#2-3" , ""));
			msg.append(str2a.replace("#2a-1", notyet).replace("#2a-2" , "").replace("#2a-3" , ""));
			msg.append(str3.replace("#3-1", notyet).replace("#3-2" , "").replace("#3-3" , ""));
			msg.append(str4.replace("#4-1", notyet).replace("#4-2" , "").replace("#4-3" , ""));
			msg.append(str5.replace("#5-1", notyet).replace("#5-2" , "").replace("#5-3" , ""));
			msg.append(str6.replace("#6-1", notyet).replace("#6-2" , "").replace("#6-3" , ""));
			msg.append(str7.replace("#7-1", notyet).replace("#7-2" , "").replace("#7-3" , ""));
			msg.append(str8.replace("#8-1", notyet).replace("#8-2" , "").replace("#8-3" , ""));
			po_batch = tempSave(id, po_batch, msg.toString());
			//每家操作行產一次
			for(BANK_GROUP po:bankGroupList){
				//操作行代號
				String opbkId = po.getBGBK_ID();
				//產生檔案和下載檔案的路徑
//				String transactiondetailFilePath = transactiondetailDirPath+"/"+bizdate+"/"+(StrUtils.isEmpty(opbkId)?"":opbkId+"/");
				String transactiondetailFilePath = transactiondetailDirPath+"/"+bizdate+"/"+(StrUtils.isEmpty(opbkId)?"":opbkId+"/")+"/"+Arguments.getStringArg("BAT.LAST.PATH")+"/";
				//產生檔案和下載檔案的檔名
//				String transactiondetailFileName = transactiondetailFilePrefix+"_"+bizdate+(StrUtils.isEmpty(clearingphase)?"":"_"+clearingphase)+(StrUtils.isEmpty(opbkId)?"":"_"+opbkId)+".zip";
				String transactiondetailFileName = transactiondetailFilePrefix+(StrUtils.isEmpty(opbkId)?"":"_"+opbkId)+"_"+bizdate+(StrUtils.isEmpty(clearingphase)?"":"_"+clearingphase)+".zip";
				//產生檔案和下載檔案的路徑加檔名
				String transactiondetailFullPath = transactiondetailFilePath+transactiondetailFileName;
				logger.debug("transactiondetailFullPath==>"+transactiondetailFullPath);
				
				Map<String,Object> filenameAndDataMap = transactiondetail_download_bo.getFilenameListAndDataList_NW(bizdate,opbkId,clearingphase);
				message = (String)filenameAndDataMap.get("message");
				
				byteZIP = null;
				//List有TXT的Byte[]才做
				if(((List<byte[]>)filenameAndDataMap.get("dataList")).size() > 0){
					//Zip的Byte[]
					byteZIP = codeUtils.createZIP((List<byte[]>)filenameAndDataMap.get("dataList"),(List<String>)filenameAndDataMap.get("filenameList"),null);
					
					//正常，檔案放到資料夾下
					if(byteZIP != null){
						codeUtils.putFileToPath(transactiondetailFullPath,byteZIP);
					}
					//有問題
					else{
						message = codeUtils.appendMessage(message,"壓縮操作行="+opbkId+"的檔案過程出現問題");
					}
				}
			}//for end
			//完全成功
			if("".equals(message)){
				str1 = str1.replace("#1-1", suc).replace("#1-2" , " start "+tmp1).replace("#1-3" , " end "+zDateHandler.getTheTime_MS());
//				resultMessage = codeUtils.appendMessage(resultMessage,"交易明細資料檔下載排程：完全成功");
			}
			//不完全成功
			else{
				result = "FALSE";
				str1 = str1.replace("#1-1", fail+","+message).replace("#1-2" , " start "+tmp1).replace("#1-3" , " end "+zDateHandler.getTheTime_MS());
//				resultMessage = codeUtils.appendMessage(resultMessage,"交易明細資料檔下載排程：失敗{"+message+"}");
				message = "";
			}
			msg.delete(0, msg.length());
			tmp1= zDateHandler.getTheTime_MS();
			msg.append(str1);
			msg.append(str2.replace("#2-1", notyet).replace("#2-2" , "").replace("#2-3" , ""));
			msg.append(str2a.replace("#2a-1", notyet).replace("#2a-2" , "").replace("#2a-3" , ""));
			msg.append(str3.replace("#3-1", notyet).replace("#3-2" , "").replace("#3-3" , ""));
			msg.append(str4.replace("#4-1", notyet).replace("#4-2" , "").replace("#4-3" , ""));
			msg.append(str5.replace("#5-1", notyet).replace("#5-2" , "").replace("#5-3" , ""));
			msg.append(str6.replace("#6-1", notyet).replace("#6-2" , "").replace("#6-3" , ""));
			msg.append(str7.replace("#7-1", notyet).replace("#7-2" , "").replace("#7-3" , ""));
			msg.append(str8.replace("#8-1", notyet).replace("#8-2" , "").replace("#8-3" , ""));
			po_batch = tempSave(id, po_batch, msg.toString());
			/*****************************以上為交易明細資料檔下載排程********************************************/
			/*****************************以下為共用代碼資料下載排程********************************************/
			//BIG5
			//將該路徑下的共用代碼資料的ZIP檔案刪除
			//路徑
//			20150602 edit by hugo req by 李建利 共用資料就直接抓最新的不以日期區分
//			String basedataFilePath = basedataDirPath+"/"+basedataFilePrefix+"/"+DateTimeUtils.getDateShort(new Date());
			String basedataFilePath = basedataDirPath+"/"+basedataFilePrefix+"/";
//			File basedataFile = new File(basedataFilePath);
//			eACH_CommonData.zip
			File basedataFile = new File(basedataFilePath+"/"+basedataFilePrefix+".zip");
			if(basedataFile.exists()){
				codeUtils.delete(basedataFile);
			}
			
			//產生檔案和下載檔案的檔名
			String basedataFileName = basedataFilePrefix+".zip";
			//產生檔案和下載檔案的路徑加檔名
			String basedataFullPath = basedataFilePath+"/"+basedataFileName;
			logger.debug("basedataFullPath==>"+basedataFullPath);
			
			Map<String,Object> filenameAndDataMap = basedata_download_bo.getFilenameListAndDataList();
			message = (String)filenameAndDataMap.get("message");
			
			byteZIP = null;
			//List有TXT的Byte[]才做
			if(((List<byte[]>)filenameAndDataMap.get("dataList")).size() > 0){
				msg.delete(0, msg.length());
				tmp1= zDateHandler.getTheTime_MS();
				msg.append(str1);
				msg.append(str2.replace("#2-1", run).replace("#2-2" , " start "+tmp1).replace("#2-3" , ""));
//				msg.append(str2a.replace("#2a-1", run).replace("#2a-2" , " start "+tmp1).replace("#2a-3" , ""));
				msg.append(str3.replace("#3-1", notyet).replace("#3-2" , "").replace("#3-3" , ""));
				msg.append(str4.replace("#4-1", notyet).replace("#4-2" , "").replace("#4-3" , ""));
				msg.append(str5.replace("#5-1", notyet).replace("#5-2" , "").replace("#5-3" , ""));
				msg.append(str6.replace("#6-1", notyet).replace("#6-2" , "").replace("#6-3" , ""));
				msg.append(str7.replace("#7-1", notyet).replace("#7-2" , "").replace("#7-3" , ""));
				msg.append(str8.replace("#8-1", notyet).replace("#8-2" , "").replace("#8-3" , ""));
				po_batch = tempSave(id, po_batch, msg.toString());
				//Zip的Byte[]
				byteZIP = codeUtils.createZIP((List<byte[]>)filenameAndDataMap.get("dataList"),(List<String>)filenameAndDataMap.get("filenameList"),null);
				
				//正常，檔案放到資料夾下
				if(byteZIP != null){
					codeUtils.putFileToPath(basedataFullPath,byteZIP);
				}
				//有問題
				else{
					message = codeUtils.appendMessage(message,"壓縮共用代碼資料的檔案_BIG5過程出現問題");
				}
			}
			
			//UTF8
			basedataFilePath = basedataDirPath+"/"+basedataFilePrefix_UTF8+"/";
//			File basedataFile = new File(basedataFilePath);
//			eACH_CommonData.zip
			basedataFile = new File(basedataFilePath+"/"+basedataFilePrefix_UTF8+".zip");
			if(basedataFile.exists()){
				codeUtils.delete(basedataFile);
			}
			
			//產生檔案和下載檔案的檔名
			basedataFileName = basedataFilePrefix_UTF8+".zip";
			//產生檔案和下載檔案的路徑加檔名
			basedataFullPath = basedataFilePath+"/"+basedataFileName;
			logger.debug("basedataFullPath==>"+basedataFullPath);
			
			Map<String,Object> filenameAndDataMapUTF8 = basedata_download_bo.getFilenameListAndDataList_UTF8();
			
			message = (String)filenameAndDataMapUTF8.get("message");
			
			byteZIP = null;
			//List有TXT的Byte[]才做
			if(((List<byte[]>)filenameAndDataMapUTF8.get("dataList")).size() > 0){
				msg.delete(0, msg.length());
				tmp1= zDateHandler.getTheTime_MS();
				msg.append(str1);
//				msg.append(str2.replace("#2-1", run).replace("#2-2" , " start "+tmp1).replace("#2-3" , ""));
				msg.append(str2a.replace("#2a-1", run).replace("#2a-2" , " start "+tmp1).replace("#2a-3" , ""));
				msg.append(str3.replace("#3-1", notyet).replace("#3-2" , "").replace("#3-3" , ""));
				msg.append(str4.replace("#4-1", notyet).replace("#4-2" , "").replace("#4-3" , ""));
				msg.append(str5.replace("#5-1", notyet).replace("#5-2" , "").replace("#5-3" , ""));
				msg.append(str6.replace("#6-1", notyet).replace("#6-2" , "").replace("#6-3" , ""));
				msg.append(str7.replace("#7-1", notyet).replace("#7-2" , "").replace("#7-3" , ""));
				msg.append(str8.replace("#8-1", notyet).replace("#8-2" , "").replace("#8-3" , ""));
				po_batch = tempSave(id, po_batch, msg.toString());
				//Zip的Byte[]
				byteZIP = codeUtils.createZIP((List<byte[]>)filenameAndDataMapUTF8.get("dataList"),(List<String>)filenameAndDataMapUTF8.get("filenameList"),null);
				
				//正常，檔案放到資料夾下
				if(byteZIP != null){
					codeUtils.putFileToPath(basedataFullPath,byteZIP);
				}
				//有問題
				else{
					message = codeUtils.appendMessage(message,"壓縮共用代碼資料的檔案_UTF8過程出現問題");
				}
			}
			
			
			//完全成功
			if("".equals(message)){
				str2 = str2.replace("#2-1", suc).replace("#2-2" , " start "+tmp1).replace("#2-3" , " end "+zDateHandler.getTheTime_MS());
//				resultMessage = codeUtils.appendMessage(resultMessage,";共用代碼資料下載排程：完全成功");
			}
			//不完全成功
			else{
				result = "FALSE";
				str2 = str2.replace("#2-1", fail+","+message).replace("#2-2" , " start "+tmp1).replace("#2-3" , " end "+zDateHandler.getTheTime_MS());
//				resultMessage = codeUtils.appendMessage(resultMessage,";共用代碼資料下載排程：失敗{"+message+"}");
				message = "";
			}
			
			msg.delete(0, msg.length());
			tmp1= zDateHandler.getTheTime_MS();
			msg.append(str1);
			msg.append(str2);
			msg.append(str2a.replace("#2a-1", notyet).replace("#2a-2" , "").replace("#2a-3" , ""));
			msg.append(str3.replace("#3-1", notyet).replace("#3-2" , "").replace("#3-3" , ""));
			msg.append(str4.replace("#4-1", notyet).replace("#4-2" , "").replace("#4-3" , ""));
			msg.append(str5.replace("#5-1", notyet).replace("#5-2" , "").replace("#5-3" , ""));
			msg.append(str6.replace("#6-1", notyet).replace("#6-2" , "").replace("#6-3" , ""));
			msg.append(str7.replace("#7-1", notyet).replace("#7-2" , "").replace("#7-3" , ""));
			msg.append(str8.replace("#8-1", notyet).replace("#8-2" , "").replace("#8-3" , ""));
			po_batch = tempSave(id, po_batch, msg.toString());
			
			/*****************************以上為共用代碼資料下載排程********************************************/
			/*****************************以下為代理業者共用代碼資料下載排程********************************************/
			
			//BIG5
			//將該路徑下的共用代碼資料的ZIP檔案刪除
			//路徑
			basedataFilePath = basedataDirPath+"/"+basedataFilePrefix+"/";
//			File basedataFile = new File(basedataFilePath);
//			eACH_AgentCommonData.zip
			basedataFile = new File(basedataFilePath+"/"+basedataFilePrefix_C+".zip");
			if(basedataFile.exists()){
				codeUtils.delete(basedataFile);
			}
			
			//產生檔案和下載檔案的檔名
			basedataFileName = basedataFilePrefix_C+".zip";
			//產生檔案和下載檔案的路徑加檔名
			basedataFullPath = basedataFilePath+"/"+basedataFileName;
			logger.debug("agent..basedataFullPath==>"+basedataFullPath);
			filenameAndDataMap = null;
			filenameAndDataMap = basedata_download_bo.getFilenameListAndDataList4Agent();
			message = (String)filenameAndDataMap.get("message");
			
			byteZIP = null;
			//List有TXT的Byte[]才做
			if(((List<byte[]>)filenameAndDataMap.get("dataList")).size() > 0){
				msg.delete(0, msg.length());
				tmp1= zDateHandler.getTheTime_MS();
				msg.append(str1);
				msg.append(str2);
				msg.append(str2a.replace("#2a-1", run).replace("#2a-2" , " start "+tmp1).replace("#2a-3" , ""));
				msg.append(str3.replace("#3-1", notyet).replace("#3-2" , "").replace("#3-3" , ""));
				msg.append(str4.replace("#4-1", notyet).replace("#4-2" , "").replace("#4-3" , ""));
				msg.append(str5.replace("#5-1", notyet).replace("#5-2" , "").replace("#5-3" , ""));
				msg.append(str6.replace("#6-1", notyet).replace("#6-2" , "").replace("#6-3" , ""));
				msg.append(str7.replace("#7-1", notyet).replace("#7-2" , "").replace("#7-3" , ""));
				msg.append(str8.replace("#8-1", notyet).replace("#8-2" , "").replace("#8-3" , ""));
				po_batch = tempSave(id, po_batch, msg.toString());
				//Zip的Byte[]
				byteZIP = codeUtils.createZIP((List<byte[]>)filenameAndDataMap.get("dataList"),(List<String>)filenameAndDataMap.get("filenameList"),null);
				
				//正常，檔案放到資料夾下
				if(byteZIP != null){
					codeUtils.putFileToPath(basedataFullPath,byteZIP);
				}
				//有問題
				else{
					message = codeUtils.appendMessage(message,"壓縮代理業者共用代碼資料的檔案_BIG5過程出現問題");
				}
			}
			
			//UTF8
			//將該路徑下的共用代碼資料的ZIP檔案刪除
			//路徑
			basedataFilePath = basedataDirPath+"/"+basedataFilePrefix_UTF8+"/";
//			File basedataFile = new File(basedataFilePath);
//			eACH_AgentCommonData.zip
			basedataFile = new File(basedataFilePath+"/"+basedataFilePrefix_C_UTF8+".zip");
			if(basedataFile.exists()){
				codeUtils.delete(basedataFile);
			}
			
			//產生檔案和下載檔案的檔名
			basedataFileName = basedataFilePrefix_C_UTF8+".zip";
			//產生檔案和下載檔案的路徑加檔名
			basedataFullPath = basedataFilePath+"/"+basedataFileName;
			logger.debug("agent..basedataFullPath==>"+basedataFullPath);
			filenameAndDataMap = null;
			filenameAndDataMap = basedata_download_bo.getFilenameListAndDataList4Agent_UTF8();
			message = (String)filenameAndDataMap.get("message");
			
			byteZIP = null;
			//List有TXT的Byte[]才做
			if(((List<byte[]>)filenameAndDataMap.get("dataList")).size() > 0){
				msg.delete(0, msg.length());
				tmp1= zDateHandler.getTheTime_MS();
				msg.append(str1);
				msg.append(str2);
//				msg.append(str2a.replace("#2a-1", run).replace("#2a-2" , " start "+tmp1).replace("#2a-3" , ""));
				msg.append(str3.replace("#3-1", notyet).replace("#3-2" , "").replace("#3-3" , ""));
				msg.append(str4.replace("#4-1", notyet).replace("#4-2" , "").replace("#4-3" , ""));
				msg.append(str5.replace("#5-1", notyet).replace("#5-2" , "").replace("#5-3" , ""));
				msg.append(str6.replace("#6-1", notyet).replace("#6-2" , "").replace("#6-3" , ""));
				msg.append(str7.replace("#7-1", notyet).replace("#7-2" , "").replace("#7-3" , ""));
				msg.append(str8.replace("#8-1", notyet).replace("#8-2" , "").replace("#8-3" , ""));
				po_batch = tempSave(id, po_batch, msg.toString());
				//Zip的Byte[]
				byteZIP = codeUtils.createZIP((List<byte[]>)filenameAndDataMap.get("dataList"),(List<String>)filenameAndDataMap.get("filenameList"),null);
				
				//正常，檔案放到資料夾下
				if(byteZIP != null){
					codeUtils.putFileToPath(basedataFullPath,byteZIP);
				}
				//有問題
				else{
					message = codeUtils.appendMessage(message,"壓縮代理業者共用代碼資料的檔案_UTF8過程出現問題");
				}
			}
			
			//完全成功
			if("".equals(message)){
				str2a = str2a.replace("#2a-1", suc).replace("#2a-2" , " start "+tmp1).replace("#2a-3" , " end "+zDateHandler.getTheTime_MS());
			}
			//不完全成功
			else{
				result = "FALSE";
				str2a = str2a.replace("#2a-1", fail+","+message).replace("#2a-2" , " start "+tmp1).replace("#2a-3" , " end "+zDateHandler.getTheTime_MS());
				message = "";
			}
			
			msg.delete(0, msg.length());
			tmp1= zDateHandler.getTheTime_MS();
			msg.append(str1);
			msg.append(str2);
			msg.append(str2a);
			msg.append(str3.replace("#3-1", notyet).replace("#3-2" , "").replace("#3-3" , ""));
			msg.append(str4.replace("#4-1", notyet).replace("#4-2" , "").replace("#4-3" , ""));
			msg.append(str5.replace("#5-1", notyet).replace("#5-2" , "").replace("#5-3" , ""));
			msg.append(str6.replace("#6-1", notyet).replace("#6-2" , "").replace("#6-3" , ""));
			msg.append(str7.replace("#7-1", notyet).replace("#7-2" , "").replace("#7-3" , ""));
			msg.append(str8.replace("#8-1", notyet).replace("#8-2" , "").replace("#8-3" , ""));
			po_batch = tempSave(id, po_batch, msg.toString());
			
			/*****************************以上為代理業者共用代碼資料下載排程********************************************/
			/*****************************以下為交易日統計資料檔下載排程********************************************/
			//將該路徑下的交易日統計資料檔的txt檔案全部刪除
			for(BANK_GROUP po:bankGroupList){
				//操作行代號
				String opbkId = po.getBGBK_ID();
				//路徑
//				String transactiondayFilePath = transactiondayDirPath+"/"+bizdate+"/"+opbkId;
				String transactiondayFilePath = transactiondayDirPath+"/"+bizdate+"/"+opbkId+"/"+Arguments.getStringArg("BAT.LAST.PATH")+"/";
//				eACH_TxDaySum_0040000_20150513_02.txt
//				String deleteFileName = transactiondayFilePrefix+"_"+bizdate+"_"+opbkId+"_"+clearingphase+".txt";
				String deleteFileName = transactiondayFilePrefix+"_"+opbkId+"_"+bizdate+"_"+clearingphase+".txt";
				File transactiondayFile = new File(transactiondayFilePath);
				if(transactiondayFile.exists()){
					for(File f:transactiondayFile.listFiles()){
//					if(f.getName().contains(transactiondayFilePrefix)){
						if(f.getName().equals(deleteFileName)){
							f.delete();
						}
					}
				}
			}
			msg.delete(0, msg.length());
			tmp1= zDateHandler.getTheTime_MS();
			msg.append(str1);
			msg.append(str2);
			msg.append(str2a);
			msg.append(str3.replace("#3-1", run).replace("#3-2" , " start "+tmp1).replace("#3-3" , ""));
			msg.append(str4.replace("#4-1", notyet).replace("#4-2" , "").replace("#4-3" , ""));
			msg.append(str5.replace("#5-1", notyet).replace("#5-2" , "").replace("#5-3" , ""));
			msg.append(str6.replace("#6-1", notyet).replace("#6-2" , "").replace("#6-3" , ""));
			msg.append(str7.replace("#7-1", notyet).replace("#7-2" , "").replace("#7-3" , ""));
			msg.append(str8.replace("#8-1", notyet).replace("#8-2" , "").replace("#8-3" , ""));
			po_batch = tempSave(id, po_batch, msg.toString());
			//每家操作行產一次
			for(BANK_GROUP po:bankGroupList){
				//操作行代號
				String opbkId = po.getBGBK_ID();
						
				//產生檔案和下載檔案的路徑
//				String transactiondayFilePath = transactiondayDirPath+"/"+bizdate+"/"+(StrUtils.isEmpty(opbkId)?"":opbkId+"/");
				String transactiondayFilePath = transactiondayDirPath+"/"+bizdate+"/"+(StrUtils.isEmpty(opbkId)?"":opbkId+"/")+"/"+Arguments.getStringArg("BAT.LAST.PATH")+"/";
				//產生檔案和下載檔案的檔名
//				String transactiondayFileName = transactiondayFilePrefix+"_"+bizdate+(StrUtils.isEmpty(opbkId)?"":"_"+opbkId)+"_"+clearingphase+".txt";
				String transactiondayFileName = transactiondayFilePrefix+(StrUtils.isEmpty(opbkId)?"":"_"+opbkId)+"_"+bizdate+"_"+clearingphase+".txt";
				//產生檔案和下載檔案的路徑加檔名
				String transactiondayFullPath = transactiondayFilePath+transactiondayFileName;
				logger.debug("transactiondayFullPath==>"+transactiondayFullPath);
						
				Map<String,Object> dataMap = transactionday_download_bo.getTXT(bizdate,null,opbkId,clearingphase);
				
				//正常，檔案放到資料夾下
				if(dataMap.get("data") != null){
					codeUtils.putFileToPath(transactiondayFullPath,(byte[])dataMap.get("data"));
				}
				//有問題
				else{
					message = codeUtils.appendMessage(message,(String)dataMap.get("message"));
				}
			}//for end
			//完全成功
			if("".equals(message)){
				str3 = str3.replace("#3-1", suc ).replace("#3-2" , " start "+tmp1).replace("#3-3" , " end "+zDateHandler.getTheTime_MS());
//				resultMessage = codeUtils.appendMessage(resultMessage,";交易日統計資料檔下載排程：完全成功");
			}
			//不完全成功
			else{
				result = "FALSE";
				str3 = str3.replace("#3-1", fail+","+message ).replace("#3-2" , " start "+tmp1).replace("#3-3" , " end "+zDateHandler.getTheTime_MS());
//				resultMessage = codeUtils.appendMessage(resultMessage,";交易日統計資料檔下載排程：失敗{"+message+"}");
				message = "";
			}
			
			msg.delete(0, msg.length());
			tmp1= zDateHandler.getTheTime_MS();
			msg.append(str1);
			msg.append(str2);
			msg.append(str2a);
			msg.append(str3);
			msg.append(str4.replace("#4-1", notyet).replace("#4-2" , "").replace("#4-3" , ""));
			msg.append(str5.replace("#5-1", notyet).replace("#5-2" , "").replace("#5-3" , ""));
			msg.append(str6.replace("#6-1", notyet).replace("#6-2" , "").replace("#6-3" , ""));
			msg.append(str7.replace("#7-1", notyet).replace("#7-2" , "").replace("#7-3" , ""));
			msg.append(str8.replace("#8-1", notyet).replace("#8-2" , "").replace("#8-3" , ""));
			po_batch = tempSave(id, po_batch, msg.toString());
			/*****************************以上為交易日統計資料檔下載排程********************************************/
			/*****************************以下為交易月統計資料檔下載排程********************************************/
			logger.debug("isMonth>>"+isMonth+",YYYYMM>>"+YYYYMM);
			if(isMonth){
				//將該路徑下的交易月統計資料檔的ZIP檔案全部刪除
				for(BANK_GROUP po:bankGroupList){
					//操作行代號
					String opbkId = po.getBGBK_ID();
					//路徑
//					String transactionmonthFilePath = transactionmonthDirPath+"/"+YYYYMM+"/"+opbkId;
					String transactionmonthFilePath = transactionmonthDirPath+"/"+YYYYMM+"/"+opbkId+"/"+Arguments.getStringArg("BAT.LAST.PATH")+"/";
								
					File transactionmonthFile = new File(transactionmonthFilePath);
//					eACH_TxMonSum_0040000_201504.txt
//					String deleteFileName = transactionmonthFilePrefix+"_"+YYYYMM+"_"+opbkId+".txt";
					String deleteFileName = transactionmonthFilePrefix+"_"+opbkId+"_"+YYYYMM+".txt";
//					logger.debug("transactionmonthFile.listFiles()>>"+transactionmonthFile.listFiles());
					if(transactionmonthFile.exists()){
						for(File f:transactionmonthFile.listFiles()){
//							if(f.getName().contains(transactionmonthFilePrefix)){
							if(f.getName().equals(deleteFileName)){
								f.delete();
							}
						}
					}
				}
				
				msg.delete(0, msg.length());
				tmp1= zDateHandler.getTheTime_MS();
				msg.append(str1);
				msg.append(str2);
				msg.append(str2a);
				msg.append(str3);
				msg.append(str4.replace("#4-1", run).replace("#4-2" , "start "+tmp1).replace("#4-3" , ""));
				msg.append(str5.replace("#5-1", notyet).replace("#5-2" , "").replace("#5-3" , ""));
				msg.append(str6.replace("#6-1", notyet).replace("#6-2" , "").replace("#6-3" , ""));
				msg.append(str7.replace("#7-1", notyet).replace("#7-2" , "").replace("#7-3" , ""));
				msg.append(str8.replace("#8-1", notyet).replace("#8-2" , "").replace("#8-3" , ""));
				po_batch = tempSave(id, po_batch, msg.toString());
				//每家操作行產一次
				for(BANK_GROUP po:bankGroupList){
					//操作行代號
					String opbkId = po.getBGBK_ID();
							
					//產生檔案和下載檔案的路徑
//					String transactionmonthFilePath = transactionmonthDirPath+"/"+YYYYMM+"/"+(StrUtils.isEmpty(opbkId)?"":opbkId+"/");
					String transactionmonthFilePath = transactionmonthDirPath+"/"+YYYYMM+"/"+(StrUtils.isEmpty(opbkId)?"":opbkId+"/")+"/"+Arguments.getStringArg("BAT.LAST.PATH")+"/";
					//產生檔案和下載檔案的檔名
//					String transactionmonthFileName = transactionmonthFilePrefix+"_"+YYYYMM+(StrUtils.isEmpty(opbkId)?"":"_"+opbkId)+".txt";
					String transactionmonthFileName = transactionmonthFilePrefix+(StrUtils.isEmpty(opbkId)?"":"_"+opbkId)+"_"+YYYYMM+".txt";
					//產生檔案和下載檔案的路徑加檔名
					String transactionmonthFullPath = transactionmonthFilePath+transactionmonthFileName;
					logger.debug("transactionmonthFullPath==>"+transactionmonthFullPath);
					
					Map<String,Object> dataMap = transactionmonth_download_bo.getTXT(YYYYMM,null,opbkId);
					
					//正常，檔案放到資料夾下
					if(dataMap.get("data") != null){
						codeUtils.putFileToPath(transactionmonthFullPath,(byte[])dataMap.get("data"));
					}
					//有問題
					else{
						message = codeUtils.appendMessage(message,(String)dataMap.get("message"));
					}
				}//for end
				//完全成功
				if("".equals(message)){
					str4 = str4.replace("#4-1", suc).replace("#4-2" , "start "+tmp1).replace("#4-3" , " end "+zDateHandler.getTheTime_MS());
//					resultMessage = codeUtils.appendMessage(resultMessage,";交易月統計資料檔下載排程：完全成功");
				}
				//不完全成功
				else{
					result = "FALSE";
					str4 = str4.replace("#4-1", fail+","+message).replace("#4-2" , "start "+tmp1).replace("#4-3" , " end "+zDateHandler.getTheTime_MS());
//					resultMessage = codeUtils.appendMessage(resultMessage,";交易月統計資料檔下載排程：失敗{"+message+"}");
					message = "";
				}
				
				msg.delete(0, msg.length());
				tmp1= zDateHandler.getTheTime_MS();
				msg.append(str1);
				msg.append(str2);
				msg.append(str2a);
				msg.append(str3);
				msg.append(str4);
				msg.append(str5.replace("#5-1", notyet).replace("#5-2" , "").replace("#5-3" , ""));
				msg.append(str6.replace("#6-1", notyet).replace("#6-2" , "").replace("#6-3" , ""));
				msg.append(str7.replace("#7-1", notyet).replace("#7-2" , "").replace("#7-3" , ""));
				msg.append(str8.replace("#8-1", notyet).replace("#8-2" , "").replace("#8-3" , ""));
				po_batch = tempSave(id, po_batch, msg.toString());
			}else{
				str4 = str4.replace("#4-1", notyet).replace("#4-2" , "").replace("#4-3" , "");
			}
			/*****************************以上為交易月統計資料檔下載排程********************************************/
			/*****************************以下為手續費日統計資料檔下載排程********************************************/
			//將該路徑下的手續費日統計資料檔的txt檔案全部刪除
			for(BANK_GROUP po:bankGroupList){
				//操作行代號
				String opbkId = po.getBGBK_ID();
				//路徑
//				String feedayFilePath = feedayDirPath+"/"+bizdate+"/"+opbkId;
				String feedayFilePath = feedayDirPath+"/"+bizdate+"/"+opbkId+"/"+Arguments.getStringArg("BAT.LAST.PATH")+"/";
//				String deleteFileName = feedayFilePrefix+"_"+bizdate+"_"+opbkId+"_"+clearingphase+".txt";
				String deleteFileName = feedayFilePrefix+"_"+opbkId+"_"+bizdate+"_"+clearingphase+".txt";
				File feedayFile = new File(feedayFilePath);
				if(feedayFile.exists()){
					for(File f:feedayFile.listFiles()){
						if(f.getName().equals(deleteFileName)){
							f.delete();
						}
					}
				}
			}
			msg.delete(0, msg.length());
			tmp1= zDateHandler.getTheTime_MS();
			msg.append(str1);
			msg.append(str2);
			msg.append(str2a);
			msg.append(str3);
			msg.append(str4);
			msg.append(str5.replace("#5-1", run).replace("#5-2" , " start "+tmp1).replace("#5-3" , ""));
			msg.append(str6.replace("#6-1", notyet).replace("#6-2" , "").replace("#6-3" , ""));
			msg.append(str7.replace("#7-1", notyet).replace("#7-2" , "").replace("#7-3" , ""));
			msg.append(str8.replace("#8-1", notyet).replace("#8-2" , "").replace("#8-3" , ""));
			po_batch = tempSave(id, po_batch, msg.toString());
			//每家操作行產一次
			for(BANK_GROUP po:bankGroupList){
				//操作行代號
				String opbkId = po.getBGBK_ID();
				//產生檔案和下載檔案的路徑
				String feedayFilePath = feedayDirPath+"/"+bizdate+"/"+(StrUtils.isEmpty(opbkId)?"":opbkId+"/")+"/"+Arguments.getStringArg("BAT.LAST.PATH")+"/";
				//產生檔案和下載檔案的檔名
//				String feedayFileName = feedayFilePrefix+"_"+bizdate+(StrUtils.isEmpty(opbkId)?"":"_"+opbkId)+"_"+clearingphase+".txt";
				String feedayFileName = feedayFilePrefix+(StrUtils.isEmpty(opbkId)?"":"_"+opbkId)+"_"+bizdate+"_"+clearingphase+".txt";
				//產生檔案和下載檔案的路徑加檔名
				String feedayFullPath = feedayFilePath+feedayFileName;
				logger.debug("feedayFullPath==>"+feedayFullPath);
						
				Map<String,Object> dataMap = feeday_download_bo.getTXT_NW(bizdate,opbkId,clearingphase);
				
				//正常，檔案放到資料夾下
				if(dataMap.get("data") != null){
					codeUtils.putFileToPath(feedayFullPath,(byte[])dataMap.get("data"));
				}
				//有問題
				else{
					message = codeUtils.appendMessage(message,(String)dataMap.get("message"));
				}
			}//for end
			//完全成功
			if("".equals(message)){
				str5 = str5.replace("#5-1", suc).replace("#5-2" , "start "+tmp1).replace("#5-3" , " end "+zDateHandler.getTheTime_MS());
				resultMessage = codeUtils.appendMessage(resultMessage,";手續費日統計資料檔下載排程：完全成功");
			}
			//不完全成功
			else{
				result = "FALSE";
				str5 = str5.replace("#5-1", fail+","+message).replace("#5-2" , "start "+tmp1).replace("#5-3" , " end "+zDateHandler.getTheTime_MS());
				resultMessage = codeUtils.appendMessage(resultMessage,";手續費日統計資料檔下載排程：失敗{"+message+"}");
				message = "";
			}
			
			msg.delete(0, msg.length());
			tmp1= zDateHandler.getTheTime_MS();
			msg.append(str1);
			msg.append(str2);
			msg.append(str2a);
			msg.append(str3);
			msg.append(str4);
			msg.append(str5);
			msg.append(str6.replace("#6-1", notyet).replace("#6-2" , "").replace("#6-3" , ""));
			msg.append(str7.replace("#7-1", notyet).replace("#7-2" , "").replace("#7-3" , ""));
			msg.append(str8.replace("#8-1", notyet).replace("#8-2" , "").replace("#8-3" , ""));
			po_batch = tempSave(id, po_batch, msg.toString());
			/*****************************以上為手續費日統計資料檔下載排程********************************************/
			/*****************************以下為手續費月統計資料檔下載排程********************************************/
			logger.debug("isMonth>>"+isMonth+",YYYYMM>>"+YYYYMM);
			if(isMonth){
				//將該路徑下的手續費月統計資料檔的ZIP檔案全部刪除
				for(BANK_GROUP po:bankGroupList){
					//操作行代號
					String opbkId = po.getBGBK_ID();
					//路徑
					String feemonthFilePath = feemonthDirPath+"/"+YYYYMM+"/"+opbkId+"/"+Arguments.getStringArg("BAT.LAST.PATH")+"/";
								
					File feemonthFile = new File(feemonthFilePath);
//					String deleteFileName = feemonthFilePrefix+"_"+YYYYMM+"_"+opbkId+".txt";
					String deleteFileName = feemonthFilePrefix+"_"+opbkId+"_"+YYYYMM+".txt";
					if(feemonthFile.exists()){
						for(File f:feemonthFile.listFiles()){
							if(f.getName().equals(deleteFileName)){
								f.delete();
							}
						}
					}
				}
				msg.delete(0, msg.length());
				tmp1= zDateHandler.getTheTime_MS();
				msg.append(str1);
				msg.append(str2);
				msg.append(str2a);
				msg.append(str3);
				msg.append(str4);
				msg.append(str5);
				msg.append(str6.replace("#6-1", run).replace("#6-2" , " start "+tmp1).replace("#6-3" , ""));
				msg.append(str7.replace("#7-1", notyet).replace("#7-2" , "").replace("#7-3" , ""));
				msg.append(str8.replace("#8-1", notyet).replace("#8-2" , "").replace("#8-3" , ""));
				po_batch = tempSave(id, po_batch, msg.toString());
				//每家操作行產一次
				for(BANK_GROUP po:bankGroupList){
					//操作行代號
					String opbkId = po.getBGBK_ID();
					logger.debug("================opbkId="+opbkId+"=================================");
							
					//產生檔案和下載檔案的路徑
					String feemonthFilePath = feemonthDirPath+"/"+YYYYMM+"/"+(StrUtils.isEmpty(opbkId)?"":opbkId+"/")+"/"+Arguments.getStringArg("BAT.LAST.PATH")+"/";
					//產生檔案和下載檔案的檔名
//					String feemonthFileName = feemonthFilePrefix+"_"+YYYYMM+(StrUtils.isEmpty(opbkId)?"":"_"+opbkId)+".txt";
					String feemonthFileName = feemonthFilePrefix+(StrUtils.isEmpty(opbkId)?"":"_"+opbkId)+"_"+YYYYMM+".txt";
					//產生檔案和下載檔案的路徑加檔名
					String feemonthFullPath = feemonthFilePath+feemonthFileName;
					logger.debug("feemonthFullPath==>"+feemonthFullPath);
					
					Map<String,Object> dataMap = feemonth_download_bo.getTXT_NW(YYYYMM,opbkId);
					
					//正常，檔案放到資料夾下
					if(dataMap.get("data") != null){
						codeUtils.putFileToPath(feemonthFullPath,(byte[])dataMap.get("data"));
					}
					//有問題
					else{
						message = codeUtils.appendMessage(message,(String)dataMap.get("message"));
					}
				}//for end
				//完全成功
				if("".equals(message)){
					str6 = str6.replace("#6-1", suc).replace("#6-2" , " start "+tmp1).replace("#6-3" , " end "+zDateHandler.getTheTime_MS());
//					resultMessage = codeUtils.appendMessage(resultMessage,";手續費月統計資料檔下載排程：完全成功");
				}
				//不完全成功
				else{
					result = "FALSE";
					str6 = str6.replace("#6-1", fail).replace("#6-2" , " start "+tmp1).replace("#6-3" , " end "+zDateHandler.getTheTime_MS());
//					resultMessage = codeUtils.appendMessage(resultMessage,";手續費月統計資料檔下載排程：失敗{"+message+"}");
					message = "";
				}
				msg.delete(0, msg.length());
				tmp1= zDateHandler.getTheTime_MS();
				msg.append(str1);
				msg.append(str2);
				msg.append(str2a);
				msg.append(str3);
				msg.append(str4);
				msg.append(str5);
				msg.append(str6);
				msg.append(str7.replace("#7-1", notyet).replace("#7-2" , "").replace("#7-3" , ""));
				msg.append(str8.replace("#8-1", notyet).replace("#8-2" , "").replace("#8-3" , ""));
				po_batch = tempSave(id, po_batch, msg.toString());
			}else{
				str6 = str6.replace("#6-1", notyet).replace("#6-2" , "").replace("#6-3" , "");
			}
			/*****************************以上為手續費月統計資料檔下載排程********************************************/
			/*****************************以下為結算日統計資料檔下載排程********************************************/
			//將該路徑下的結算日統計資料檔的txt檔案全部刪除
			for(BANK_GROUP po:bankGroupList){
				//操作行代號
				String opbkId = po.getBGBK_ID();
				//路徑
//				String cleardayFilePath = cleardayDirPath+"/"+bizdate+"/"+opbkId;
				String cleardayFilePath = cleardayDirPath+"/"+bizdate+"/"+opbkId+"/"+Arguments.getStringArg("BAT.LAST.PATH")+"/";
//				String deleteFileName = cleardayFilePrefix+"_"+bizdate+"_"+opbkId+"_"+clearingphase+".txt";
				String deleteFileName = cleardayFilePrefix+"_"+opbkId+"_"+bizdate+"_"+clearingphase+".txt";
				File cleardayFile = new File(cleardayFilePath);
				if(cleardayFile.exists()){
					for(File f:cleardayFile.listFiles()){
						if(f.getName().equals(deleteFileName)){
							f.delete();
						}
					}
				}
			}
			
			msg.delete(0, msg.length());
			tmp1= zDateHandler.getTheTime_MS();
			msg.append(str1);
			msg.append(str2);
			msg.append(str2a);
			msg.append(str3);
			msg.append(str4);
			msg.append(str5);
			msg.append(str6);
			msg.append(str7.replace("#7-1", run).replace("#7-2" , " start "+tmp1).replace("#7-3" , ""));
			msg.append(str8.replace("#8-1", notyet).replace("#8-2" , "").replace("#8-3" , ""));
			po_batch = tempSave(id, po_batch, msg.toString());
			//每家操作行產一次
			for(BANK_GROUP po:bankGroupList){
				//操作行代號
				String opbkId = po.getBGBK_ID();
						
				//產生檔案和下載檔案的路徑
//				String cleardayFilePath = cleardayDirPath+"/"+bizdate+"/"+(StrUtils.isEmpty(opbkId)?"":opbkId+"/");
				String cleardayFilePath = cleardayDirPath+"/"+bizdate+"/"+(StrUtils.isEmpty(opbkId)?"":opbkId+"/")+"/"+Arguments.getStringArg("BAT.LAST.PATH")+"/";
				//產生檔案和下載檔案的檔名
//				String cleardayFileName = cleardayFilePrefix+"_"+bizdate+(StrUtils.isEmpty(opbkId)?"":"_"+opbkId)+"_"+clearingphase+".txt";
				String cleardayFileName = cleardayFilePrefix+(StrUtils.isEmpty(opbkId)?"":"_"+opbkId)+"_"+bizdate+"_"+clearingphase+".txt";
				//產生檔案和下載檔案的路徑加檔名
				String cleardayFullPath = cleardayFilePath+cleardayFileName;
				logger.debug("cleardayFullPath==>"+cleardayFullPath);
						
				Map<String,Object> dataMap = clearday_download_bo.getTXT(bizdate,opbkId,clearingphase);
				
				//正常，檔案放到資料夾下
				if(dataMap.get("data") != null){
					codeUtils.putFileToPath(cleardayFullPath,(byte[])dataMap.get("data"));
				}
				//有問題
				else{
					message = codeUtils.appendMessage(message,(String)dataMap.get("message"));
				}
			}//for end
			//完全成功
			if("".equals(message)){
				str7 = str7.replace("#7-1", suc).replace("#7-2" , " start "+tmp1).replace("#7-3" , " end "+zDateHandler.getTheTime_MS());
//				resultMessage = codeUtils.appendMessage(resultMessage,";結算日統計資料檔下載排程：完全成功");
			}
			//不完全成功
			else{
				result = "FALSE";
				str7 = str7.replace("#7-1", fail).replace("#7-2" , " start "+tmp1).replace("#7-3" , " end "+zDateHandler.getTheTime_MS());
//				resultMessage = codeUtils.appendMessage(resultMessage,";結算日統計資料檔下載排程：失敗{"+message+"}");
				message = "";
			}
			msg.delete(0, msg.length());
			tmp1= zDateHandler.getTheTime_MS();
			msg.append(str1);
			msg.append(str2);
			msg.append(str2a);
			msg.append(str3);
			msg.append(str4);
			msg.append(str5);
			msg.append(str6);
			msg.append(str7);
			msg.append(str8.replace("#8-1", notyet).replace("#8-2" , "").replace("#8-3" , ""));
			po_batch = tempSave(id, po_batch, msg.toString());
			/*****************************以上為結算日統計資料檔下載排程********************************************/
			/*****************************以下為代理清算行資料檔下載排程********************************************/
			//此排程與其他的下載排程寫法較不一樣
			//撈出所有的代理清算行代號
			List<BANK_GROUP> list = bank_group_Dao.getProxyClean_BankList();
			//將該路徑下的代理清算行資料檔的檔案全部刪除
//			String proxybankFilePrefix = DateTimeUtils.getCDateShort(new SimpleDateFormat("yyyyMMdd").parse(bizdate));
			String proxybankFilePrefix = DateTimeUtils.convertDate(DateTimeUtils.INTERCONVERSION, bizdate, "yyyyMMdd", "yyyyMMdd");
			
			for(BANK_GROUP po:list){
				//代理清算行代號
				String ctbkId = po.getCTBK_ID();
				//路徑
//				String proxybankFilePath = proxybankDirPath+"/"+bizdate+"/"+ctbkId;
				String proxybankFilePath = proxybankDirPath+"/"+bizdate+"/"+ctbkId+"/"+Arguments.getStringArg("BAT.LAST.PATH")+"/";
				File proxybankFile = new File(proxybankFilePath);
				//這裡和其他檔案的處理不一樣，因為有分清算階段，不能刪到上一階段的，所以要判斷是01還是02
				if(proxybankFile.exists()){
					for(File f:proxybankFile.listFiles()){
						if(f.getName().contains(proxybankFilePrefix)){
							if(f.getName().substring(0,8).equals(proxybankFilePrefix) && f.getName().substring(17,19).equals(clearingphase)){
								f.delete();
							}
						}
					}
				}
			}
			msg.delete(0, msg.length());
			tmp1= zDateHandler.getTheTime_MS();
			msg.append(str1);
			msg.append(str2);
			msg.append(str2a);
			msg.append(str3);
			msg.append(str4);
			msg.append(str5);
			msg.append(str6);
			msg.append(str7);
			msg.append(str8.replace("#8-1", run).replace("#8-2" , " start "+tmp1).replace("#8-3" , ""));
			po_batch = tempSave(id, po_batch, msg.toString());
			//每家代理清算行產一次
			for(BANK_GROUP po:list){
				//代理清算行代號
				String ctbkId = po.getCTBK_ID();

				//產生檔案和下載檔案的路徑
//				String proxybankFilePath = proxybankDirPath+"/"+bizdate+"/"+(StrUtils.isEmpty(ctbkId)?"":ctbkId+"/");
				String proxybankFilePath = proxybankDirPath+"/"+bizdate+"/"+(StrUtils.isEmpty(ctbkId)?"":ctbkId+"/")+"/"+Arguments.getStringArg("BAT.LAST.PATH")+"/";
				//產生檔案和下載檔案的檔名
				String proxybankFileName = proxybankFilePrefix+"_"+DateTimeUtils.getTimeShort(Calendar.getInstance().getTime())+"F_"+clearingphase+"_"+ctbkId+"_eachftp.ach";
				//產生檔案和下載檔案的路徑加檔名
				String proxybankFullPath = proxybankFilePath+proxybankFileName;
				logger.debug("proxybankFullPath==>"+proxybankFullPath);
//				因proxybankFilePrefix是以民國年的營業日做為檔名，另外此API第一個參數營業日剛好也需要民國年
				Map<String,Object> dataMap = proxy_cl_bo.exportTXT(proxybankFilePrefix,clearingphase,ctbkId,"");
				
				//正常，檔案放到資料夾下
				if("TRUE".equals(dataMap.get("result"))){
					codeUtils.putFileToPath(proxybankFullPath,(byte[])dataMap.get("msg"));
				}
				//有問題
				else{
					message = codeUtils.appendMessage(message,(String)dataMap.get("msg"));
				}
			}//for end
			//完全成功
			if("".equals(message)){
				str8 = str8.replace("#8-1", suc).replace("#8-2" , " start "+tmp1).replace("#8-3" , " end "+zDateHandler.getTheTime_MS());
//				resultMessage = codeUtils.appendMessage(resultMessage,";代理清算行資料檔下載排程：完全成功");
			}
			//不完全成功
			else{
				result = "FALSE";
				str8 = str8.replace("#8-1", fail+","+message).replace("#8-2" , " start "+tmp1).replace("#8-3" , " end "+zDateHandler.getTheTime_MS());
//				resultMessage = codeUtils.appendMessage(resultMessage,";代理清算行資料檔下載排程：失敗{"+message+"}");
				message = "";
			}
			msg.delete(0, msg.length());
			tmp1= zDateHandler.getTheTime_MS();
			msg.append(str1);
			msg.append(str2);
			msg.append(str2a);
			msg.append(str3);
			msg.append(str4);
			msg.append(str5);
			msg.append(str6);
			msg.append(str7);
			msg.append(str8);
			po_batch = tempSave(id, po_batch, msg.toString());
			/*****************************以上為代理清算行資料檔下載排程********************************************/
			resultMap.put("result",result);
//			resultMap.put("msg",resultMessage);
			resultMap.put("msg",msg.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = "FALSE";
			resultMessage = codeUtils.appendMessage(resultMessage,"系統異常>>"+e);
			resultMap.put("result",result);
			resultMap.put("msg",resultMessage);
		}
		logger.debug("resultMap>>"+resultMap);
		logger.debug("finsh time>>"+zDateHandler.getTheDateII()+" "+zDateHandler.getTheTime_MS());
		return resultMap;
	}

	public TRANSACTIONDETAIL_DOWNLOAD_BO getTransactiondetail_download_bo() {
		return transactiondetail_download_bo;
	}
	public void setTransactiondetail_download_bo(TRANSACTIONDETAIL_DOWNLOAD_BO transactiondetail_download_bo) {
		this.transactiondetail_download_bo = transactiondetail_download_bo;
	}
	public BASEDATA_DOWNLOAD_BO getBasedata_download_bo() {
		return basedata_download_bo;
	}
	public void setBasedata_download_bo(BASEDATA_DOWNLOAD_BO basedata_download_bo) {
		this.basedata_download_bo = basedata_download_bo;
	}
	public TRANSACTIONDAY_DOWNLOAD_BO getTransactionday_download_bo() {
		return transactionday_download_bo;
	}
	public void setTransactionday_download_bo(TRANSACTIONDAY_DOWNLOAD_BO transactionday_download_bo) {
		this.transactionday_download_bo = transactionday_download_bo;
	}
	public TRANSACTIONMONTH_DOWNLOAD_BO getTransactionmonth_download_bo() {
		return transactionmonth_download_bo;
	}
	public void setTransactionmonth_download_bo(TRANSACTIONMONTH_DOWNLOAD_BO transactionmonth_download_bo) {
		this.transactionmonth_download_bo = transactionmonth_download_bo;
	}
	public FEEDAY_DOWNLOAD_BO getFeeday_download_bo() {
		return feeday_download_bo;
	}

	public void setFeeday_download_bo(FEEDAY_DOWNLOAD_BO feeday_download_bo) {
		this.feeday_download_bo = feeday_download_bo;
	}

	public FEEMONTH_DOWNLOAD_BO getFeemonth_download_bo() {
		return feemonth_download_bo;
	}

	public void setFeemonth_download_bo(FEEMONTH_DOWNLOAD_BO feemonth_download_bo) {
		this.feemonth_download_bo = feemonth_download_bo;
	}

	public CLEARDAY_DOWNLOAD_BO getClearday_download_bo() {
		return clearday_download_bo;
	}

	public void setClearday_download_bo(CLEARDAY_DOWNLOAD_BO clearday_download_bo) {
		this.clearday_download_bo = clearday_download_bo;
	}

	public PROXY_CL_BO getProxy_cl_bo() {
		return proxy_cl_bo;
	}

	public void setProxy_cl_bo(PROXY_CL_BO proxy_cl_bo) {
		this.proxy_cl_bo = proxy_cl_bo;
	}

	public BANK_GROUP_Dao getBank_group_Dao() {
		return bank_group_Dao;
	}
	public void setBank_group_Dao(BANK_GROUP_Dao bank_group_Dao) {
		this.bank_group_Dao = bank_group_Dao;
	}
	public CodeUtils getCodeUtils() {
		return codeUtils;
	}
	public void setCodeUtils(CodeUtils codeUtils) {
		this.codeUtils = codeUtils;
	}

	public EACH_BATCH_STATUS_Dao getBatch_status_Dao() {
		return batch_status_Dao;
	}

	public void setBatch_status_Dao(EACH_BATCH_STATUS_Dao batch_status_Dao) {
		this.batch_status_Dao = batch_status_Dao;
	}

	public EACH_BATCH_DEF_Dao getBatch_def_Dao() {
		return batch_def_Dao;
	}

	public void setBatch_def_Dao(EACH_BATCH_DEF_Dao batch_def_Dao) {
		this.batch_def_Dao = batch_def_Dao;
	}

	public String getBizDate() {
		return bizDate;
	}

	public void setBizDate(String bizDate) {
		this.bizDate = bizDate;
	}

	public String getClearingphase() {
		return clearingphase;
	}

	public void setClearingphase(String clearingphase) {
		this.clearingphase = clearingphase;
	}

	public Integer getBatch_proc_seq() {
		return batch_proc_seq;
	}

	public void setBatch_proc_seq(Integer batch_proc_seq) {
		this.batch_proc_seq = batch_proc_seq;
	}

	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}

	public String getYYYYMM() {
		return YYYYMM;
	}

	public void setYYYYMM(String yYYYMM) {
		YYYYMM = yYYYMM;
	}

	public boolean isMonth() {
		return isMonth;
	}

	public void setMonth(boolean isMonth) {
		this.isMonth = isMonth;
	}

	
}