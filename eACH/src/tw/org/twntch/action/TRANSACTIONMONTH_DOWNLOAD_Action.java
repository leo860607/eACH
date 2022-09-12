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

import tw.org.twntch.bo.EACH_USERLOG_BO;
import tw.org.twntch.bo.TRANSACTIONMONTH_DOWNLOAD_BO;
import tw.org.twntch.bo.TXS_DAY_BO;
import tw.org.twntch.form.Login_Form;
import tw.org.twntch.form.Transactionmonth_Download_Form;
import tw.org.twntch.util.CodeUtils;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.zDateHandler;

public class TRANSACTIONMONTH_DOWNLOAD_Action extends GenericAction{
	private CodeUtils codeUtils;
	private TRANSACTIONMONTH_DOWNLOAD_BO transactionmonth_download_bo;
	private TXS_DAY_BO txs_day_bo;
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private EACH_USERLOG_BO userlog_bo;//寫操作軌跡記錄
	
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
		Transactionmonth_Download_Form transactionmonth_download_form = (Transactionmonth_Download_Form) form;
		String ac_key = StrUtils.isEmpty(transactionmonth_download_form.getAc_key())?"":transactionmonth_download_form.getAc_key();
		String target = StrUtils.isEmpty(transactionmonth_download_form.getTarget())?"search":transactionmonth_download_form.getTarget();
		transactionmonth_download_form.setTarget(target);
		Login_Form login_form = (Login_Form)request.getSession().getAttribute("login_form");
		logger.debug("ac_key=" + ac_key);
		logger.debug("target=" + target);
		
		logger.debug("transactionmonth_download_form.getOpbkIdList()=" + transactionmonth_download_form.getOpbkIdList());
		logger.debug("transactionmonth_download_form.getPcodeList()=" + transactionmonth_download_form.getPcodeList());
		
		//交易代號清單
		transactionmonth_download_form.setPcodeList(txs_day_bo.getPcodeList());
		//票交端
		if(login_form.getUserData().getUSER_TYPE().equals("A")){
			//操作行代號清單
			transactionmonth_download_form.setOpbkIdList(txs_day_bo.getOpbkIdList());
		}
		//銀行端
		else{
			transactionmonth_download_form.setOPBK_ID(login_form.getUserData().getUSER_COMPANY());
		}
		//初次進入此功能
		if(ac_key.equalsIgnoreCase("")){
			//將營業年月塞到頁面的日期控制項，預設帶入上個月
			String now = zDateHandler.getTWDate();
			int year = Integer.valueOf(now.substring(0,4));
			int month = Integer.valueOf(now.substring(4,6)) - 1;
			if(month <= 0){
				month = 12;
				year--;
			}
			String newDate = "0" + year + (month<10?"0"+month:month);
			String YYYYMM = DateTimeUtils.convertDate(DateTimeUtils.NOT_INTERCONVERSION, newDate, "yyyyMM", "yyyy/MM");
			transactionmonth_download_form.setBIZYM(YYYYMM);
			logger.debug("YYYYMM="+YYYYMM);
		}
		
		//下載檔案
		if(ac_key.equalsIgnoreCase("download")){
			//將頁面上的查詢條件放進pkMap
			Map<String,Object> pkMap = new HashMap<String,Object>();
			pkMap.put("serchStrs",transactionmonth_download_form.getSerchStrs());
			//如果有錯誤要將訊息放進去
			Map<String,Object> msgMap = new HashMap<String,Object>();
			
			try{
				//取得在classpath下的properties檔案
				Map<String,String> valueMap = codeUtils.getPropertyValue("Configuration.properties","transactionmonthDirPath","transactionmonthFilePrefix");
				//無法取得properties
				if(valueMap == null){
					//回頁面alert訊息
					transactionmonth_download_form.setMsg("無法取得properties檔");
					
					//寫操作軌跡記錄(失敗)
					msgMap.put("msg",transactionmonth_download_form.getMsg());
					userlog_bo.writeFailLog("F",msgMap,null,null,pkMap);
					
					return mapping.findForward(target);
				}
				
				//取得放置檔案的資料夾根目錄
				String transactionmonthDirPath = valueMap.get("transactionmonthDirPath");
				logger.debug("transactionmonthDirPath="+transactionmonthDirPath);
				//取得檔名的前置碼
				String transactionmonthFilePrefix = valueMap.get("transactionmonthFilePrefix");
				logger.debug("transactionmonthFilePrefix="+transactionmonthFilePrefix);
			
				//年月
				String YYYYMM = StrUtils.isEmpty(transactionmonth_download_form.getBIZYM())?"":DateTimeUtils.convertDate(DateTimeUtils.INTERCONVERSION, transactionmonth_download_form.getBIZYM(), "yyyy/MM", "yyyyMM");
				//交易代號
				String pcode = StrUtils.isEmpty(transactionmonth_download_form.getPCODE())?"":transactionmonth_download_form.getPCODE();
				//操作行
				String opbkId = StrUtils.isEmpty(transactionmonth_download_form.getOPBK_ID())?"":transactionmonth_download_form.getOPBK_ID();
					
				//產生檔案和下載檔案的路徑
				String filePath = transactionmonthDirPath+"/"+YYYYMM+"/"+(StrUtils.isEmpty(opbkId)?"":opbkId+"/");
				logger.debug("filePath==>"+filePath);
				//產生檔案和下載檔案的檔名
				String fileName = transactionmonthFilePrefix+(StrUtils.isEmpty(opbkId)?"":"_"+opbkId)+(StrUtils.isEmpty(pcode)?"":"_"+pcode)+"_"+YYYYMM+".txt";
				logger.debug("fileName==>"+fileName);
				//產生檔案和下載檔案的路徑加檔名
				String fullPath = filePath+fileName;
				logger.debug("fullPath==>"+fullPath);
					
//				//先去完整路徑抓檔案
				InputStream inputStream = codeUtils.getFileFromPath(fullPath);
//				//有檔案
//				if(inputStream != null){
//					//頁面塞時間的token
//					String downloadToken = transactionmonth_download_form.getDownloadToken();
//					//將檔案吐到前端頁面
//					codeUtils.forDownload(inputStream,fileName,"fileDownloadToken",downloadToken);
//					return null;
//				}
//				//找不到檔案或檔案轉成InputStream出現問題
//				else{
					Map<String,Object> dataMap = transactionmonth_download_bo.getTXT(YYYYMM,pcode,opbkId);
					//正常
					if(dataMap.get("data") != null){
						//檔案放到資料夾下
						codeUtils.putFileToPath(fullPath,(byte[])dataMap.get("data"));
						//再一次去完整路徑抓檔案
						inputStream = codeUtils.getFileFromPath(fullPath);
						//有檔案
						if(inputStream != null){
							//頁面塞時間的token
							String downloadToken = transactionmonth_download_form.getDownloadToken();
							//將檔案吐到前端頁面
							codeUtils.forDownload(inputStream,fileName,"fileDownloadToken",downloadToken);
							
							//寫操作軌跡記錄(成功)
							userlog_bo.writeLog("F",null,null,pkMap);
							
							return null;
						}
						//alert message到頁面
						else{
							transactionmonth_download_form.setMsg("下載過程出現問題");
						}
					}
					//有問題
					else{
						transactionmonth_download_form.setMsg((String)dataMap.get("message"));
					}
//				}
			}
			catch(Exception e){
				e.printStackTrace();
				transactionmonth_download_form.setMsg(e.getMessage());
				transactionmonth_download_form.setTarget("search");
			}
			//寫操作軌跡記錄(失敗)
			msgMap.put("msg",transactionmonth_download_form.getMsg());
			userlog_bo.writeFailLog("F",msgMap,null,null,pkMap);
		}	
		target = StrUtils.isEmpty(transactionmonth_download_form.getTarget())?"":transactionmonth_download_form.getTarget();
		return mapping.findForward(target);
	}
	public CodeUtils getCodeUtils() {
		return codeUtils;
	}
	public void setCodeUtils(CodeUtils codeUtils) {
		this.codeUtils = codeUtils;
	}
	public TRANSACTIONMONTH_DOWNLOAD_BO getTransactionmonth_download_bo() {
		return transactionmonth_download_bo;
	}
	public void setTransactionmonth_download_bo(
			TRANSACTIONMONTH_DOWNLOAD_BO transactionmonth_download_bo) {
		this.transactionmonth_download_bo = transactionmonth_download_bo;
	}
	public TXS_DAY_BO getTxs_day_bo() {
		return txs_day_bo;
	}
	public void setTxs_day_bo(TXS_DAY_BO txs_day_bo) {
		this.txs_day_bo = txs_day_bo;
	}
	public EACH_USERLOG_BO getUserlog_bo() {
		return userlog_bo;
	}
	public void setUserlog_bo(EACH_USERLOG_BO userlog_bo) {
		this.userlog_bo = userlog_bo;
	}
}
