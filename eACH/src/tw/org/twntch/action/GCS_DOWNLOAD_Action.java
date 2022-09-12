package tw.org.twntch.action;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import tw.org.twntch.bo.EACHSYSSTATUSTAB_BO;
import tw.org.twntch.bo.EACH_USERLOG_BO;
import tw.org.twntch.form.GCS_Download_Form;
import tw.org.twntch.util.CodeUtils;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.StrUtils;

public class GCS_DOWNLOAD_Action extends GenericAction{
	private CodeUtils codeUtils;
	private EACHSYSSTATUSTAB_BO eachsysstatustab_bo;
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private EACH_USERLOG_BO userlog_bo;//寫操作軌跡記錄
	
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
		GCS_Download_Form gcs_download_form = (GCS_Download_Form) form;
		String ac_key = StrUtils.isEmpty(gcs_download_form.getAc_key())?"":gcs_download_form.getAc_key();
		String target = StrUtils.isEmpty(gcs_download_form.getTarget())?"search":gcs_download_form.getTarget();
		gcs_download_form.setTarget(target);
		logger.debug("ac_key=" + ac_key);
		logger.debug("target=" + target);
		
		try{
			if(StrUtils.isEmpty(ac_key)){
				gcs_download_form.setBIZDATE(eachsysstatustab_bo.getRptBusinessDate());
			}
			//取得在classpath下的properties檔案
			Map<String,String> valueMap = codeUtils.getPropertyValue("Configuration.properties","GCSDirPath","GCSFilePrefix","GCSFileSurfix");
			//無法取得properties
			if(valueMap == null){
				//回頁面alert訊息
				gcs_download_form.setMsg("無法取得properties檔");
				return mapping.findForward(target);
			}
			//取得放置檔案的資料夾根目錄
			String GCSDirPath = valueMap.get("GCSDirPath");
			logger.debug("GCSDirPath="+GCSDirPath);
			
			//取得檔名的前置碼
			String GCSFilePrefix = valueMap.get("GCSFilePrefix");
			logger.debug("GCSFilePrefix="+GCSFilePrefix);
			
			//取得檔名的尾碼
			String GCSFileSurfix = valueMap.get("GCSFileSurfix");
			logger.debug("GCSFileSurfix="+GCSFileSurfix);
			
			String date = StrUtils.isEmpty(gcs_download_form.getBIZDATE())?"":DateTimeUtils.convertDate(gcs_download_form.getBIZDATE(), "yyyyMMdd", "yyyyMMdd");
			
			//清算階段代號
			String clearingphase = StrUtils.isEmpty(gcs_download_form.getCLEARINGPHASE())?"":gcs_download_form.getCLEARINGPHASE();
			
			List<String> conditions = new ArrayList<String>();
			String condition = "";
			/* 20150210 HUANGPU 改以營業日(BIZDATE)查詢資料，非交易日期時間(TXDT) */
			if(StrUtils.isNotEmpty(date)){
				conditions.add(" BIZDATE = '" + date + "' ");
			}
			if(StrUtils.isNotEmpty(clearingphase)){
				conditions.add(" CLEARINGPHASE = '" + clearingphase + "' ");
			}
			for(int i = 0; i < conditions.size(); i++){
				condition += conditions.get(i);
				if(i < conditions.size() - 1){
					condition += " AND ";
				}
			}
					
			logger.debug("condition==>"+condition);
			
			//產生檔案和下載檔案的路徑
			String filePath = GCSDirPath+"/"+GCSFileSurfix+"/"+DateTimeUtils.getDateShort(new Date())+"/";
			logger.debug("filePath==>"+filePath);
			//產生檔案和下載檔案的檔名
			String fileName = GCSFilePrefix+"_"+date+(StrUtils.isEmpty(clearingphase)?"":"_"+clearingphase)+"."+GCSFileSurfix;
			logger.debug("fileName==>"+fileName);
			
			//產生檔案和下載檔案的路徑加檔名
			String fullPath = filePath+fileName;
			logger.debug("fullPath==>"+fullPath);
			
			//下載檔案
			if(ac_key.equalsIgnoreCase("download")){
				//將頁面上的查詢條件放進pkMap
				Map<String,Object> pkMap = new HashMap<String,Object>();
				pkMap.put("serchStrs",gcs_download_form.getSerchStrs());
				//如果有錯誤要將訊息放進去
				Map<String,Object> msgMap = new HashMap<String,Object>();
				
				String SQL = "WITH TEMP AS(SELECT BGBK_ID,(SUM(RECVAMT+RVSRECVAMT)+SUM(PAYAMT+RVSPAYAMT)) NETAMT FROM RPONCLEARINGTAB "+(StrUtils.isEmpty(condition)? "" : " WHERE " + condition)+" GROUP BY BGBK_ID ORDER BY BGBK_ID)";
				SQL += "SELECT BGBK_ID,(CASE WHEN NETAMT > 0 THEN '+' ELSE '-' END) AS PLUSMINUS,ABS(NETAMT) AS NETAMT FROM TEMP WHERE NETAMT != 0";
				Map<String,Object> columnMap = new HashMap<String,Object>();
				List<String> columnNameList = new ArrayList<String>();
				List<Integer> columnLengthList = new ArrayList<Integer>();
				List<String> columnTypeList = new ArrayList<String>();
				//先查資料，因為要算總筆數
				List<Map<String,Object>> queryListMap = codeUtils.queryListMap(SQL,null);
				//TXT的控制首錄
				String firstRow = "BOF"+("01".equals(clearingphase)?"EA01":"EA02")+gcs_download_form.getBIZDATE();
				logger.debug("firstRow==>"+firstRow);
				//TXT的控制尾錄
				String lastRow = "EOF"+("01".equals(clearingphase)?"EA01":"EA02")+codeUtils.padText("number",8,String.valueOf(queryListMap.size()));
				logger.debug("lastRow==>"+lastRow);
				columnNameList.add("BGBK_ID");
				columnLengthList.add(7);
				columnTypeList.add("string");
				columnNameList.add("PLUSMINUS");
				columnLengthList.add(1);
				columnTypeList.add("string");
				columnNameList.add("NETAMT");
				columnLengthList.add(15);
				columnTypeList.add("number");
				columnMap.put("columnName",columnNameList);
				columnMap.put("columnLength",columnLengthList);
				columnMap.put("columnType",columnTypeList);
				logger.debug("SQL==>"+SQL);
				
				//TXT的Byte[]
//				if(queryListMap.size() == 0){
//					gcs_download_form.setMsg("查無資料");
//				}
//				else{
					byte[] byteTXT = codeUtils.createTXT(queryListMap,columnMap,firstRow,lastRow);
					//正常
					if(byteTXT != null){
						//檔案放到資料夾下
						codeUtils.putFileToPath(fullPath,byteTXT);
						//頁面塞時間的token
						String downloadToken = gcs_download_form.getDownloadToken();
						//將檔案吐到前端頁面
						codeUtils.forDownload(new ByteArrayInputStream(byteTXT),fileName,"fileDownloadToken",downloadToken);
						
						//寫操作軌跡記錄(成功)
						userlog_bo.writeLog("F",null,null,pkMap);
						
						return null;
					}
					//有問題
					else{
						gcs_download_form.setMsg("查詢過程出現問題");
					}
//				}
				//寫操作軌跡記錄(失敗)
				msgMap.put("msg",gcs_download_form.getMsg());
				userlog_bo.writeFailLog("F",msgMap,null,null,pkMap);
			}
		}
		catch(Exception e){
			e.printStackTrace();
			gcs_download_form.setMsg(e.getMessage());
		}
		target = StrUtils.isEmpty(gcs_download_form.getTarget())?"":gcs_download_form.getTarget();
		return mapping.findForward(target);
	}

	public CodeUtils getCodeUtils() {
		return codeUtils;
	}
	public void setCodeUtils(CodeUtils codeUtils) {
		this.codeUtils = codeUtils;
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
