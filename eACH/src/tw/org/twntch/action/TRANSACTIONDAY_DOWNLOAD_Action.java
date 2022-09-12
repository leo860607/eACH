package tw.org.twntch.action;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import tw.org.twntch.bo.BANK_GROUP_BO;
import tw.org.twntch.bo.EACHSYSSTATUSTAB_BO;
import tw.org.twntch.bo.EACH_USERLOG_BO;
import tw.org.twntch.bo.TRANSACTIONDAY_DOWNLOAD_BO;
import tw.org.twntch.bo.TXS_DAY_BO;
import tw.org.twntch.form.Login_Form;
import tw.org.twntch.form.Transactionday_Download_Form;
import tw.org.twntch.util.BeanUtils;
import tw.org.twntch.util.CodeUtils;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.StrUtils;

public class TRANSACTIONDAY_DOWNLOAD_Action extends GenericAction{
	private CodeUtils codeUtils;
	private TRANSACTIONDAY_DOWNLOAD_BO transactionday_download_bo;
	private TXS_DAY_BO txs_day_bo;
	private EACHSYSSTATUSTAB_BO eachsysstatustab_bo;
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private EACH_USERLOG_BO userlog_bo;//寫操作軌跡記錄
	private BANK_GROUP_BO bank_group_bo;
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
		Transactionday_Download_Form transactionday_download_form = (Transactionday_Download_Form) form;
		String ac_key = StrUtils.isEmpty(transactionday_download_form.getAc_key())?"":transactionday_download_form.getAc_key();
		String target = StrUtils.isEmpty(transactionday_download_form.getTarget())?"search":transactionday_download_form.getTarget();
		transactionday_download_form.setTarget(target);
		Login_Form login_form = (Login_Form)request.getSession().getAttribute("login_form");
		logger.debug("ac_key=" + ac_key);
		logger.debug("target=" + target);
		
		logger.debug("transactionday_download_form.getOpbkIdList()=" + transactionday_download_form.getOpbkIdList());
		logger.debug("transactionday_download_form.getPcodeList()=" + transactionday_download_form.getPcodeList());
		
		//交易代號清單
		transactionday_download_form.setPcodeList(txs_day_bo.getPcodeList());
		//票交端
		if(login_form.getUserData().getUSER_TYPE().equals("A")){
			//操作行代號清單
//			transactionday_download_form.setOpbkIdList(txs_day_bo.getOpbkIdList());
			transactionday_download_form.setOpbkIdList(bank_group_bo.getOpbkList());
		}
		//銀行端
		else{
			transactionday_download_form.setOPBK_ID(login_form.getUserData().getUSER_COMPANY());
		}
		//初次進入此功能
		if(ac_key.equalsIgnoreCase("")){
			//將營業日塞到頁面的日期控制項
//			20150529 edit by hugo req by UAT-20150525-02
//			String busDate = eachsysstatustab_bo.getBusinessDate();
			String busDate = eachsysstatustab_bo.getRptBusinessDate();
			transactionday_download_form.setBIZDATE(busDate);
		}
		
		//下載檔案
		if(ac_key.equalsIgnoreCase("download")){
			//將頁面上的查詢條件放進pkMap
			Map<String,Object> pkMap = new HashMap<String,Object>();
			pkMap.put("serchStrs",transactionday_download_form.getSerchStrs());
			//如果有錯誤要將訊息放進去
			Map<String,Object> msgMap = new HashMap<String,Object>();
			
			try{
				//取得在classpath下的properties檔案
				Map<String,String> valueMap = codeUtils.getPropertyValue("Configuration.properties","transactiondayDirPath","transactiondayFilePrefix");
				//無法取得properties
				if(valueMap == null){
					//回頁面alert訊息
					transactionday_download_form.setMsg("無法取得properties檔");
					
					//寫操作軌跡記錄(失敗)
					msgMap.put("msg",transactionday_download_form.getMsg());
					userlog_bo.writeFailLog("F",msgMap,null,null,pkMap);
					
					return mapping.findForward(target);
				}
				
				if(login_form.getUserData().getUSER_TYPE().equals("B")){
					Map retmap = super.checkRPT_BizDate(transactionday_download_form.getBIZDATE(), transactionday_download_form.getCLEARINGPHASE());
					if(retmap.get("result").equals("FALSE") ){
						BeanUtils.populate(transactionday_download_form, retmap);
						
						//寫操作軌跡記錄(失敗)
						msgMap.put("msg",retmap.get("msg"));
						userlog_bo.writeFailLog("F",msgMap,null,null,pkMap);
						
						return mapping.findForward(target);
					}
				}
				
				//取得放置檔案的資料夾根目錄
				String transactiondayDirPath = valueMap.get("transactiondayDirPath");
				logger.debug("transactiondayDirPath="+transactiondayDirPath);
				//取得檔名的前置碼
				String transactiondayFilePrefix = valueMap.get("transactiondayFilePrefix");
				logger.debug("transactiondayFilePrefix="+transactiondayFilePrefix);
			
				//日期
				String date = StrUtils.isEmpty(transactionday_download_form.getBIZDATE())?"":DateTimeUtils.convertDate(transactionday_download_form.getBIZDATE(), "yyyyMMdd", "yyyyMMdd");
				//交易代號
				String pcode = StrUtils.isEmpty(transactionday_download_form.getPCODE())?"":transactionday_download_form.getPCODE();
				//操作行
				String opbkId = StrUtils.isEmpty(transactionday_download_form.getOPBK_ID())?"":transactionday_download_form.getOPBK_ID();
				//清算階段代號
				String clearingphase = StrUtils.isEmpty(transactionday_download_form.getCLEARINGPHASE())?"":transactionday_download_form.getCLEARINGPHASE();
				
				//產生檔案和下載檔案的路徑
				String filePath = transactiondayDirPath+"/"+date+"/"+(StrUtils.isEmpty(opbkId)?"":opbkId+"/");
				logger.debug("filePath==>"+filePath);
				//產生檔案和下載檔案的檔名
				String fileName = transactiondayFilePrefix+(StrUtils.isEmpty(opbkId)?"":"_"+opbkId)+"_"+date+(StrUtils.isEmpty(clearingphase)?"":"_"+clearingphase)+".txt";
				logger.debug("fileName==>"+fileName);
				//產生檔案和下載檔案的路徑加檔名
				String fullPath = filePath+fileName;
				logger.debug("fullPath==>"+fullPath);
				
//				//先去完整路徑抓檔案
				InputStream inputStream = codeUtils.getFileFromPath(fullPath);
//				//有檔案
//				if(inputStream != null){
//					//頁面塞時間的token
//					String downloadToken = transactionday_download_form.getDownloadToken();
//					//將檔案吐到前端頁面
//					codeUtils.forDownload(inputStream,fileName,"fileDownloadToken",downloadToken);
//					return null;
//				}
//				//找不到檔案或檔案轉成InputStream出現問題
//				else{
					Map<String,Object> dataMap = transactionday_download_bo.getTXT(date,pcode,opbkId,clearingphase);
					//正常
					if(dataMap.get("data") != null){
						//檔案放到資料夾下
						codeUtils.putFileToPath(fullPath,(byte[])dataMap.get("data"));
						//再一次去完整路徑抓檔案
						inputStream = codeUtils.getFileFromPath(fullPath);
						//有檔案
						if(inputStream != null){
							//頁面塞時間的token
							String downloadToken = transactionday_download_form.getDownloadToken();
							//將檔案吐到前端頁面
							codeUtils.forDownload(inputStream,fileName,"fileDownloadToken",downloadToken);
							
							//寫操作軌跡記錄(成功)
							userlog_bo.writeLog("F",null,null,pkMap);
							
							return null;
						}
						//alert message到頁面
						else{
							transactionday_download_form.setMsg("下載過程出現問題");
						}
					}
					//有問題
					else{
						transactionday_download_form.setMsg((String)dataMap.get("message"));
					}
//				}
			}
			catch(Exception e){
				e.printStackTrace();
				transactionday_download_form.setMsg(e.getMessage());
				transactionday_download_form.setTarget("search");
			}
			//寫操作軌跡記錄(失敗)
			msgMap.put("msg",transactionday_download_form.getMsg());
			userlog_bo.writeFailLog("F",msgMap,null,null,pkMap);
		}
		target = StrUtils.isEmpty(transactionday_download_form.getTarget())?"":transactionday_download_form.getTarget();
		return mapping.findForward(target);
	}
	
	
	public CodeUtils getCodeUtils() {
		return codeUtils;
	}
	public void setCodeUtils(CodeUtils codeUtils) {
		this.codeUtils = codeUtils;
	}
	public TRANSACTIONDAY_DOWNLOAD_BO getTransactionday_download_bo() {
		return transactionday_download_bo;
	}
	public void setTransactionday_download_bo(TRANSACTIONDAY_DOWNLOAD_BO transactionday_download_bo) {
		this.transactionday_download_bo = transactionday_download_bo;
	}
	public TXS_DAY_BO getTxs_day_bo() {
		return txs_day_bo;
	}
	public void setTxs_day_bo(TXS_DAY_BO txs_day_bo) {
		this.txs_day_bo = txs_day_bo;
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


	public BANK_GROUP_BO getBank_group_bo() {
		return bank_group_bo;
	}


	public void setBank_group_bo(BANK_GROUP_BO bank_group_bo) {
		this.bank_group_bo = bank_group_bo;
	}
	
	
}
