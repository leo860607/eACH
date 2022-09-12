package tw.org.twntch.action;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import tw.org.twntch.bo.BANK_GROUP_BO;
import tw.org.twntch.bo.BUSINESS_TYPE_BO;
import tw.org.twntch.bo.EACHSYSSTATUSTAB_BO;
import tw.org.twntch.bo.EACH_USERLOG_BO;
import tw.org.twntch.bo.TRANSACTIONDETAIL_DOWNLOAD_BO;
import tw.org.twntch.form.Login_Form;
import tw.org.twntch.form.Transactiondetail_Download_Form;
import tw.org.twntch.util.BeanUtils;
import tw.org.twntch.util.CodeUtils;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.StrUtils;

public class TRANSACTIONDETAIL_DOWNLOAD_Action extends GenericAction{
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private CodeUtils codeUtils;
	private TRANSACTIONDETAIL_DOWNLOAD_BO transactiondetail_download_bo;
	private BANK_GROUP_BO bank_group_bo ;//取得操作行
	private BUSINESS_TYPE_BO business_type_bo;//取得業務類別
	private EACHSYSSTATUSTAB_BO eachsysstatustab_bo;
	private EACH_USERLOG_BO userlog_bo;//寫操作軌跡記錄
	
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
		Transactiondetail_Download_Form transactiondetail_download_form = (Transactiondetail_Download_Form) form;
		String ac_key = StrUtils.isEmpty(transactiondetail_download_form.getAc_key())?"":transactiondetail_download_form.getAc_key();
		String target = StrUtils.isEmpty(transactiondetail_download_form.getTarget())?"search":transactiondetail_download_form.getTarget();
		transactiondetail_download_form.setTarget(target);
		Login_Form login_form = (Login_Form)request.getSession().getAttribute("login_form");
		logger.debug("ac_key=" + ac_key);
		logger.debug("target=" + target);
		
		//票交端
		if(login_form.getUserData().getUSER_TYPE().equals("A")){
			//操作行清單
			//transactiondetail_download_form.setOpbkIdList(bank_group_bo.getBgbkIdListII());
			transactiondetail_download_form.setOpbkIdList(bank_group_bo.getOpbkList());
		}
		//銀行端
		else{
			transactiondetail_download_form.setOPBK_ID(login_form.getUserData().getUSER_COMPANY());
		}
		//初次進入此功能
		if(ac_key.equalsIgnoreCase("")){
			//將營業日塞到頁面的日期控制項
//			20150529 edit by hugo req by UAT-20150525-02
//			String busDate = eachsysstatustab_bo.getBusinessDate();
			String busDate = eachsysstatustab_bo.getRptBusinessDate();
			logger.debug("busDate="+busDate);
			transactiondetail_download_form.setBIZDATE(busDate);
		}
		
		//下載檔案
		if(ac_key.equalsIgnoreCase("downloadold")){
			//將頁面上的查詢條件放進pkMap
			Map<String,Object> pkMap = new HashMap<String,Object>();
			pkMap.put("serchStrs",transactiondetail_download_form.getSerchStrs());
			//如果有錯誤要將訊息放進去
			Map<String,Object> msgMap = new HashMap<String,Object>();
			
			try{
				//取得在classpath下的properties檔案
				Map<String,String> valueMap = codeUtils.getPropertyValue("Configuration.properties","transactiondetailDirPath","transactiondetailFilePrefix");
				//無法取得properties
				if(valueMap == null){
					//回頁面alert訊息
					transactiondetail_download_form.setMsg("無法取得properties檔");
					//寫操作軌跡記錄(失敗)
					msgMap.put("msg",transactiondetail_download_form.getMsg());
					userlog_bo.writeFailLog("F",msgMap,null,null,pkMap);
					
					return mapping.findForward(target);
				}
				
				if(login_form.getUserData().getUSER_TYPE().equals("B")){
					Map retmap = super.checkRPT_BizDate(transactiondetail_download_form.getBIZDATE(), transactiondetail_download_form.getCLEARINGPHASE());
					if(retmap.get("result").equals("FALSE") ){
						BeanUtils.populate(transactiondetail_download_form, retmap);
						//寫操作軌跡記錄(失敗)
						msgMap.put("msg",retmap.get("msg"));
						userlog_bo.writeFailLog("F",msgMap,null,null,pkMap);
						
						return mapping.findForward(target);
					}
				}
				
				//取得放置ZIP檔案的資料夾根目錄
				String transactiondetailDirPath = valueMap.get("transactiondetailDirPath");
				logger.debug("transactiondetailDirPath="+transactiondetailDirPath);
				//取得ZIP檔名的前置碼
				String transactiondetailFilePrefix = valueMap.get("transactiondetailFilePrefix");
				logger.debug("transactiondetailFilePrefix="+transactiondetailFilePrefix);
				
				//日期
				String date = StrUtils.isEmpty(transactiondetail_download_form.getBIZDATE())?"":DateTimeUtils.convertDate(transactiondetail_download_form.getBIZDATE(), "yyyyMMdd", "yyyyMMdd");
				//操作行
				String opbkId = StrUtils.isEmpty(transactiondetail_download_form.getOPBK_ID())?"":transactiondetail_download_form.getOPBK_ID().trim();
				//清算階段代號
				String clearingphase = StrUtils.isEmpty(transactiondetail_download_form.getCLEARINGPHASE())?"":transactiondetail_download_form.getCLEARINGPHASE();
				
				//產生檔案和下載檔案的路徑
				String filePath = transactiondetailDirPath+"/"+date+"/"+(StrUtils.isEmpty(opbkId)?"":opbkId+"/");
				logger.debug("filePath==>"+filePath);
				//產生檔案和下載檔案的檔名
				String fileName = transactiondetailFilePrefix+(StrUtils.isEmpty(opbkId)?"":"_"+opbkId)+"_"+date+(StrUtils.isEmpty(clearingphase)?"":"_"+clearingphase)+"_OLD.zip";
				logger.debug("fileName==>"+fileName);
				//產生檔案和下載檔案的路徑加檔名
				String fullPath = filePath+fileName;
				logger.debug("fullPath==>"+fullPath);
				
//				//先去完整路徑抓檔案
				InputStream inputStream = codeUtils.getFileFromPath(fullPath);
//				//有檔案
//				if(inputStream != null){
//					//頁面塞時間的token
//					String downloadToken = transactiondetail_download_form.getDownloadToken();
//					//將檔案吐到前端頁面
//					codeUtils.forDownload(inputStream,fileName,"fileDownloadToken",downloadToken);
//					return null;
//				}
//				//找不到檔案或檔案轉成InputStream出現問題
//				else{
					Map<String,Object> filenameAndDataMap = transactiondetail_download_bo.getFilenameListAndDataList(date,opbkId,clearingphase);
					//Zip的Byte[]
					byte[] byteZIP = null;
					//List有TXT的Byte[]才做
					if(((List<byte[]>)filenameAndDataMap.get("dataList")).size() > 0){
						//Zip的Byte[]
						byteZIP = codeUtils.createZIP((List<byte[]>)filenameAndDataMap.get("dataList"),(List<String>)filenameAndDataMap.get("filenameList"),null);
						
						//正常，檔案放到資料夾下
						if(byteZIP != null){
							codeUtils.putFileToPath(fullPath,byteZIP);
							//再一次去完整路徑抓檔案
							inputStream = codeUtils.getFileFromPath(fullPath);
							//有檔案
							if(inputStream != null){
								//頁面塞時間的token
								String downloadToken = transactiondetail_download_form.getDownloadToken();
								//將檔案吐到前端頁面
								codeUtils.forDownload(inputStream,fileName,"fileDownloadToken",downloadToken);
								//寫操作軌跡記錄(成功)
								userlog_bo.writeLog("F",null,null,pkMap);
								return null;
							}
							//alert message到頁面
							else{
								transactiondetail_download_form.setMsg("下載過程出現問題");
							}
						}
						//有問題
						else{
							transactiondetail_download_form.setMsg("壓縮過程出現問題");
						}
					}
					else{
						transactiondetail_download_form.setMsg("查詢過程出現問題");
					}
//				}
			}
			catch(Exception e){
				e.printStackTrace();
				transactiondetail_download_form.setMsg(e.getMessage());
				transactiondetail_download_form.setTarget("search");
			}
			//寫操作軌跡記錄(失敗)
			msgMap.put("msg",transactiondetail_download_form.getMsg());
			userlog_bo.writeFailLog("F",msgMap,null,null,pkMap);
		}
		
		if(ac_key.equalsIgnoreCase("download")){
			//將頁面上的查詢條件放進pkMap
			Map<String,Object> pkMap = new HashMap<String,Object>();
			pkMap.put("serchStrs",transactiondetail_download_form.getSerchStrs());
			//如果有錯誤要將訊息放進去
			Map<String,Object> msgMap = new HashMap<String,Object>();
			
			try{
				//取得在classpath下的properties檔案
				Map<String,String> valueMap = codeUtils.getPropertyValue("Configuration.properties","transactiondetailDirPath","transactiondetailFilePrefix");
				//無法取得properties
				if(valueMap == null){
					//回頁面alert訊息
					transactiondetail_download_form.setMsg("無法取得properties檔");
					//寫操作軌跡記錄(失敗)
					msgMap.put("msg",transactiondetail_download_form.getMsg());
					userlog_bo.writeFailLog("F",msgMap,null,null,pkMap);
					
					return mapping.findForward(target);
				}
				
				if(login_form.getUserData().getUSER_TYPE().equals("B")){
					Map retmap = super.checkRPT_BizDate(transactiondetail_download_form.getBIZDATE(), transactiondetail_download_form.getCLEARINGPHASE());
					if(retmap.get("result").equals("FALSE") ){
						BeanUtils.populate(transactiondetail_download_form, retmap);
						//寫操作軌跡記錄(失敗)
						msgMap.put("msg",retmap.get("msg"));
						userlog_bo.writeFailLog("F",msgMap,null,null,pkMap);
						
						return mapping.findForward(target);
					}
				}
				
				//取得放置ZIP檔案的資料夾根目錄
				String transactiondetailDirPath = valueMap.get("transactiondetailDirPath");
				logger.debug("transactiondetailDirPath="+transactiondetailDirPath);
				//取得ZIP檔名的前置碼
				String transactiondetailFilePrefix = valueMap.get("transactiondetailFilePrefix");
				logger.debug("transactiondetailFilePrefix="+transactiondetailFilePrefix);
				
				//日期
				String date = StrUtils.isEmpty(transactiondetail_download_form.getBIZDATE())?"":DateTimeUtils.convertDate(transactiondetail_download_form.getBIZDATE(), "yyyyMMdd", "yyyyMMdd");
				//操作行
				String opbkId = StrUtils.isEmpty(transactiondetail_download_form.getOPBK_ID())?"":transactiondetail_download_form.getOPBK_ID().trim();
				//清算階段代號
				String clearingphase = StrUtils.isEmpty(transactiondetail_download_form.getCLEARINGPHASE())?"":transactiondetail_download_form.getCLEARINGPHASE();
				
				//產生檔案和下載檔案的路徑
				String filePath = transactiondetailDirPath+"/"+date+"/"+(StrUtils.isEmpty(opbkId)?"":opbkId+"/");
				logger.debug("filePath==>"+filePath);
				//產生檔案和下載檔案的檔名
				String fileName = transactiondetailFilePrefix+(StrUtils.isEmpty(opbkId)?"":"_"+opbkId)+"_"+date+(StrUtils.isEmpty(clearingphase)?"":"_"+clearingphase)+".zip";
				logger.debug("fileName==>"+fileName);
				//產生檔案和下載檔案的路徑加檔名
				String fullPath = filePath+fileName;
				logger.debug("fullPath==>"+fullPath);
				
//				//先去完整路徑抓檔案
				InputStream inputStream = codeUtils.getFileFromPath(fullPath);
//				//有檔案
//				if(inputStream != null){
//					//頁面塞時間的token
//					String downloadToken = transactiondetail_download_form.getDownloadToken();
//					//將檔案吐到前端頁面
//					codeUtils.forDownload(inputStream,fileName,"fileDownloadToken",downloadToken);
//					return null;
//				}
//				//找不到檔案或檔案轉成InputStream出現問題
//				else{
					Map<String,Object> filenameAndDataMap = transactiondetail_download_bo.getFilenameListAndDataList_NW(date,opbkId,clearingphase);
					//Zip的Byte[]
					byte[] byteZIP = null;
					//List有TXT的Byte[]才做
					if(((List<byte[]>)filenameAndDataMap.get("dataList")).size() > 0){
						//Zip的Byte[]
						byteZIP = codeUtils.createZIP((List<byte[]>)filenameAndDataMap.get("dataList"),(List<String>)filenameAndDataMap.get("filenameList"),null);
						
						//正常，檔案放到資料夾下
						if(byteZIP != null){
							codeUtils.putFileToPath(fullPath,byteZIP);
							//再一次去完整路徑抓檔案
							inputStream = codeUtils.getFileFromPath(fullPath);
							//有檔案
							if(inputStream != null){
								//頁面塞時間的token
								String downloadToken = transactiondetail_download_form.getDownloadToken();
								//將檔案吐到前端頁面
								codeUtils.forDownload(inputStream,fileName,"fileDownloadToken",downloadToken);
								//寫操作軌跡記錄(成功)
								userlog_bo.writeLog("F",null,null,pkMap);
								return null;
							}
							//alert message到頁面
							else{
								transactiondetail_download_form.setMsg("下載過程出現問題");
							}
						}
						//有問題
						else{
							transactiondetail_download_form.setMsg("壓縮過程出現問題");
						}
					}
					else{
						transactiondetail_download_form.setMsg("查詢過程出現問題");
					}
//				}
			}
			catch(Exception e){
				e.printStackTrace();
				transactiondetail_download_form.setMsg(e.getMessage());
				transactiondetail_download_form.setTarget("search");
			}
			//寫操作軌跡記錄(失敗)
			msgMap.put("msg",transactiondetail_download_form.getMsg());
			userlog_bo.writeFailLog("F",msgMap,null,null,pkMap);
		}
		
		
		
		target = StrUtils.isEmpty(transactiondetail_download_form.getTarget())?"":transactiondetail_download_form.getTarget();
		
		return mapping.findForward(target);
	}

	public CodeUtils getCodeUtils() {
		return codeUtils;
	}
	public void setCodeUtils(CodeUtils codeUtils) {
		this.codeUtils = codeUtils;
	}
	public TRANSACTIONDETAIL_DOWNLOAD_BO getTransactiondetail_download_bo() {
		return transactiondetail_download_bo;
	}
	public void setTransactiondetail_download_bo(
			TRANSACTIONDETAIL_DOWNLOAD_BO transactiondetail_download_bo) {
		this.transactiondetail_download_bo = transactiondetail_download_bo;
	}
	public BANK_GROUP_BO getBank_group_bo() {
		return bank_group_bo;
	}
	public void setBank_group_bo(BANK_GROUP_BO bank_group_bo) {
		this.bank_group_bo = bank_group_bo;
	}
	public BUSINESS_TYPE_BO getBusiness_type_bo() {
		return business_type_bo;
	}
	public void setBusiness_type_bo(BUSINESS_TYPE_BO business_type_bo) {
		this.business_type_bo = business_type_bo;
	}
	public EACHSYSSTATUSTAB_BO getEachsysstatustab_bo() {
		return eachsysstatustab_bo;
	}
	public void setEachsysstatustab_bo(EACHSYSSTATUSTAB_BO eachsysstatustab_bo) {
		this.eachsysstatustab_bo = eachsysstatustab_bo;
	}
	public EACH_USERLOG_BO getUserlog_bo() {
		return userlog_bo;
	}
	public void setUserlog_bo(EACH_USERLOG_BO userlog_bo) {
		this.userlog_bo = userlog_bo;
	}
}
