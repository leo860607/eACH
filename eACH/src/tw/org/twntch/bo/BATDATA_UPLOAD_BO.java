package tw.org.twntch.bo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.struts.upload.FormFile;

import tw.org.twntch.db.dao.hibernate.FLCONTROLTAB_Dao;
import tw.org.twntch.db.dao.hibernate.Page;
import tw.org.twntch.db.dao.hibernate.VW_ONBLOCKTAB_Dao;
import tw.org.twntch.form.Each_User_Form;
import tw.org.twntch.form.Login_Form;
import tw.org.twntch.po.FLCONTROLTAB;
import tw.org.twntch.po.FLCONTROLTAB_PK;
import tw.org.twntch.util.CodeUtils;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.FileUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.OSValidator;
import tw.org.twntch.util.SpringAppCtxHelper;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.WebServerPath;
import tw.org.twntch.util.WebServletUtils;
import tw.org.twntch.util.zDateHandler;

public class BATDATA_UPLOAD_BO {
	private EACH_USERLOG_BO userlog_bo;
	private VW_ONBLOCKTAB_Dao vw_onblocktab_Dao;
	private CodeUtils codeUtils;
	private FLCONTROLTAB_Dao flcontroltab_Dao;
	private SYS_PARA_BO sys_para_bo;
	private EACHSYSSTATUSTAB_BO eachsysstatustab_bo;
	private BANK_GROUP_BO bank_group_bo;
	private WK_DATE_BO wk_date_bo;
	private Logger logger = Logger.getLogger(BATDATA_UPLOAD_BO.class);
	
	public String search_toJson(Map<String, String> param){
		return JSONUtils.map2json(search(param));
	}
	
	public Map search(Map<String, String> params){
		Map rtnMap = new HashMap();
		List<FLCONTROLTAB> list = null;
		
		int pageNo = StrUtils.isEmpty(params.get("page"))?0:Integer.valueOf(params.get("page"));
		int pageSize = StrUtils.isEmpty(params.get("pagesize"))?Integer.valueOf(Arguments.getStringArg("PAGE.SIZE")):Integer.valueOf(params.get("pagesize"));
		String isSearch = StrUtils.isEmpty(params.get("isSearch")) ?"Y":params.get("isSearch");
		
		try{
			String condition = getConditionList(params).get(0);
			
			StringBuffer withTemp = new StringBuffer();
			withTemp.append("WITH TEMP2 AS ( ");
			withTemp.append("    SELECT ");
			withTemp.append("    (CASE DATEMODE WHEN '0' THEN PREVDATE ELSE THISDATE END) AS STARTDATE, ");
			withTemp.append("    (CASE DATEMODE WHEN '0' THEN THISDATE ELSE NEXTDATE END) AS ENDDATE ");
			withTemp.append("    FROM EACHSYSSTATUSTAB ");
			withTemp.append("), TEMP AS ( ");
			withTemp.append("    SELECT ");
			withTemp.append("    (CASE RESULTSTATUS WHEN 'A' THEN 1 ELSE 0 END) AS AC, ");
//			20151120 add edit by hugo req by UAT-20151120-04
//			withTemp.append("    (CASE RESULTSTATUS WHEN 'P' THEN 1 ELSE 0 END) AS PC, ");
			withTemp.append("    (CASE WHEN RESULTSTATUS = 'P' AND SENDERSTATUS = '1' THEN 1 ELSE 0 END) AS PC, ");
			withTemp.append("    (CASE RESULTSTATUS WHEN 'R' THEN 1 ELSE 0 END) AS RC, ");
			withTemp.append("    BIZDATE, FLBATCHSEQ, CLEARINGPHASE, RESULTSTATUS, DECIMAL(EACHUSER.ISNUMERIC(TXAMT)) AS TXAMT ");
			withTemp.append("    ,FLPROCSEQ ");
			withTemp.append("    FROM ONBLOCKTAB ");
			withTemp.append("    WHERE BIZDATE >= (SELECT STARTDATE FROM TEMP2 FETCH FIRST 1 ROWS ONLY) AND BIZDATE <= (SELECT ENDDATE FROM TEMP2 FETCH FIRST 1 ROWS ONLY) ");
			withTemp.append("    AND  FLBATCHSEQ IS NOT NULL AND FLBATCHSEQ <> '' ");
			
			withTemp.append(") ");
			
			StringBuffer fromAndWhere_core = new StringBuffer();
			fromAndWhere_core.append("SELECT ");
			fromAndWhere_core.append("ROWNUMBER() OVER (" + getOrderStr(params.get("sidx"), params.get("sord")) + ") AS ROWNUMBER, ");
			fromAndWhere_core.append("COALESCE((SUBSTR(TRANS_DATE(TRANSLATE('ABCDEFGH',A.LASTMODIFYDT,'ABCDEFGHIJKLMNOPQ'),'T','/'), 2, 9) || ' ' || TRANSLATE('IJ:KL:MN',A.LASTMODIFYDT,'ABCDEFGHIJKLMNOPQ')), '') AS LASTMODIFYDT, ");
			fromAndWhere_core.append("('P' || A.FILELAYOUT) AS FILELAYOUT, SUBSTR(TRANS_DATE(A.BIZDATE, 'T', '/'), 2) AS BIZDATE, A.CLEARINGPHASE, ");
			fromAndWhere_core.append("(A.BATCHSEQ || '.P' || A.FILELAYOUT) AS FILENAME, ");
			fromAndWhere_core.append("(CASE WHEN COALESCE(A.ACHFLAG, '') = '*' THEN '是' ELSE '否' END) AS ACHFLAG, ");
			fromAndWhere_core.append("(CASE WHEN COALESCE(A.FILEREJECT, '') = 'R' THEN '是' ELSE '否' END) AS FILEREJECT, ");
			fromAndWhere_core.append("COALESCE(A.TOTALCOUNT, 0) AS TOTALCOUNT, COALESCE(A.TOTALAMT, 0) AS TOTALAMT, ");
			fromAndWhere_core.append("COALESCE(A.REJECTCOUNT, 0) AS REJECTCOUNT, COALESCE(A.REJECTAMT, 0) AS REJECTAMT, ");
			fromAndWhere_core.append("COALESCE(A.ACCEPTCOUNT, 0) AS ACCEPTCOUNT, COALESCE(A.ACCEPTAMT, 0) AS ACCEPTAMT, ");
			fromAndWhere_core.append("('{\"EFILESTATUS\":\"' || COALESCE(A.EFILESTATUS, 'N') || '\",' || ");
			fromAndWhere_core.append(" '\"BIZDATE\":\"' || COALESCE(TRANS_DATE(A.BIZDATE,'T',''), '') || '\",' || ");
			fromAndWhere_core.append(" '\"PROCSEQ\":\"' || COALESCE(A.PROCSEQ, '') || '\",' || ");
			fromAndWhere_core.append(" '\"BATCHSEQ\":\"' || COALESCE(A.BATCHSEQ, '') || '\"}') AS REJECTFILEDOWNLOAD, ");
//			20160201 edit by hugo req by SRS20160122
//			fromAndWhere_core.append("(COALESCE(A.ACCEPTCOUNT, 0) - COALESCE(A.PROCCOUNT, 0)) AS SUCCESSCOUNT, ");
//			fromAndWhere_core.append("COALESCE(A.PROCCOUNT, 0) AS PROCCOUNT, ");
			fromAndWhere_core.append("COALESCE(A.PROCCOUNT, 0) AS TMP_PROCCOUNT, ");
			fromAndWhere_core.append("COALESCE((SELECT SUM(AC) FROM TEMP WHERE FLBATCHSEQ = A.BATCHSEQ AND BIZDATE = A.BIZDATE AND CLEARINGPHASE = A.CLEARINGPHASE  AND FLPROCSEQ = A.PROCSEQ ), 0) AS COUNT_A, ");
			fromAndWhere_core.append("COALESCE((SELECT SUM(TXAMT) FROM TEMP WHERE FLBATCHSEQ = A.BATCHSEQ AND BIZDATE = A.BIZDATE AND CLEARINGPHASE = A.CLEARINGPHASE AND AC = 1  AND FLPROCSEQ = A.PROCSEQ ), 0) AS AMT_A, ");
			fromAndWhere_core.append("COALESCE((SELECT SUM(PC) FROM TEMP WHERE FLBATCHSEQ = A.BATCHSEQ AND BIZDATE = A.BIZDATE AND CLEARINGPHASE = A.CLEARINGPHASE  AND FLPROCSEQ = A.PROCSEQ ), 0) AS COUNT_P, ");
			fromAndWhere_core.append("COALESCE((SELECT SUM(TXAMT) FROM TEMP WHERE FLBATCHSEQ = A.BATCHSEQ AND BIZDATE = A.BIZDATE AND CLEARINGPHASE = A.CLEARINGPHASE AND PC = 1  AND FLPROCSEQ = A.PROCSEQ ), 0) AS AMT_P, ");
			fromAndWhere_core.append("COALESCE((SELECT SUM(RC) FROM TEMP WHERE FLBATCHSEQ = A.BATCHSEQ AND BIZDATE = A.BIZDATE AND CLEARINGPHASE = A.CLEARINGPHASE  AND FLPROCSEQ = A.PROCSEQ ), 0) AS COUNT_R, ");
			fromAndWhere_core.append("COALESCE((SELECT SUM(TXAMT) FROM TEMP WHERE FLBATCHSEQ = A.BATCHSEQ AND BIZDATE = A.BIZDATE AND CLEARINGPHASE = A.CLEARINGPHASE AND RC = 1  AND FLPROCSEQ = A.PROCSEQ ), 0) AS AMT_R, ");
			fromAndWhere_core.append("COALESCE(A.RFILESTATUS, 'N') AS RESULTFILEDOWNLOAD, ");
			fromAndWhere_core.append("COALESCE((ACQUIREID || '-' || (SELECT BGBK_NAME FROM BANK_GROUP WHERE BGBK_ID = A.ACQUIREID)), '') AS ACQUIREID, ");
			fromAndWhere_core.append("BATCHSEQ, ");
			fromAndWhere_core.append("TRANSLATE('ABCD/EF/GH IJ:KL:MN',STARTDT,'ABCDEFGHIJKLMNOPQ') AS STARTDT, ");
			fromAndWhere_core.append("TRANSLATE('ABCD/EF/GH IJ:KL:MN',ENDDT,'ABCDEFGHIJKLMNOPQ') AS ENDDT ");
			fromAndWhere_core.append("FROM FLCONTROLTAB AS A ");
			fromAndWhere_core.append("WHERE BIZDATE >= (SELECT STARTDATE FROM TEMP2 FETCH FIRST 1 ROWS ONLY) ");
			fromAndWhere_core.append("AND BIZDATE <= (SELECT ENDDATE FROM TEMP2 FETCH FIRST 1 ROWS ONLY) ");
//			TODO 測試用勿上至開發、測試、正式環境
//			fromAndWhere_core.append("WHERE BIZDATE >= '20160101' ");
//			fromAndWhere_core.append("AND BIZDATE <= '20160202' ");
			fromAndWhere_core.append((StrUtils.isEmpty(condition))?"" : "AND " + condition);
			
			StringBuffer fromAndWhere = new StringBuffer();
			fromAndWhere.append("FROM (" + fromAndWhere_core + ") AS C WHERE ROWNUMBER >= " + (Page.getStartOfPage(pageNo, pageSize) + 1) + " AND ROWNUMBER <= " + pageNo * pageSize + " ");
			StringBuffer fromAndWhere_all = new StringBuffer();
			fromAndWhere_all.append("FROM (" + fromAndWhere_core + ") AS C ");
			
			StringBuffer sql = new StringBuffer();
			sql.append(withTemp.toString());
//			20160201 edit by hugo req by SRS20160122
//			sql.append("SELECT  C.* " + fromAndWhere.toString());
//			20160325 edit by hugo req by UAT-2016311-05
//			sql.append(" SELECT COALESCE((COUNT_A+COUNT_P+COUNT_R) , 0) SUCCESSCOUNT  ,  COALESCE ( (ACCEPTCOUNT- TMP_PROCCOUNT- (COUNT_A+COUNT_P+COUNT_R)) , 0)   PROCCOUNT , C.* " + fromAndWhere.toString());
			sql.append(" SELECT COALESCE((COUNT_A+COUNT_P+COUNT_R) , 0) SUCCESSCOUNT  ,  COALESCE ( (ACCEPTCOUNT- (COUNT_A+COUNT_P+COUNT_R)) , 0)   PROCCOUNT , C.* " + fromAndWhere.toString());
			System.out.println("### SQL >> " + sql);
			
			//總筆數
			StringBuffer countQuery = new StringBuffer();
			countQuery.append(withTemp.toString());
			countQuery.append("SELECT COUNT(*) AS NUM ");
			countQuery.append("FROM FLCONTROLTAB AS A ");
			countQuery.append("WHERE BIZDATE >= (SELECT STARTDATE FROM TEMP2 FETCH FIRST 1 ROWS ONLY) ");
			countQuery.append("AND BIZDATE <= (SELECT ENDDATE FROM TEMP2 FETCH FIRST 1 ROWS ONLY) ");
//			TODO 測試用勿上至開發、測試、正式環境
//			countQuery.append("WHERE BIZDATE >= '20160101' ");
//			countQuery.append("AND BIZDATE <= '20160202' ");
			countQuery.append((StrUtils.isEmpty(condition))?"" : "AND " + condition);
			//System.out.println("### COUNTSQL >> " + countQuery.toString());
			
			String cols[] = {"LASTMODIFYDT", "FILELAYOUT", "BIZDATE", "CLEARINGPHASE", "FILENAME", "ACHFLAG", "FILEREJECT", "TOTALCOUNT", "TOTALAMT", "REJECTCOUNT", "REJECTAMT", "ACCEPTCOUNT", "ACCEPTAMT", "REJECTFILEDOWNLOAD", "PROCCOUNT", "SUCCESSCOUNT", "COUNT_A", "AMT_A", "COUNT_P", "AMT_P", "COUNT_R", "AMT_R", "RESULTFILEDOWNLOAD", "ACQUIREID", "BATCHSEQ", "STARTDT", "ENDDT"};
			Page page = vw_onblocktab_Dao.getDataIII(Integer.valueOf(pageNo), Integer.valueOf(pageSize), countQuery.toString(), sql.toString(), cols, FLCONTROLTAB.class);
			list = (List<FLCONTROLTAB>) page.getResult();
			list = (list == null || list.size() == 0)? null : list;
			if(page == null){
				rtnMap.put("total", "0");
				rtnMap.put("page", "0");
				rtnMap.put("records", "0");
				rtnMap.put("rows", new ArrayList());
			}else{
				rtnMap.put("total", page.getTotalPageCount());
				rtnMap.put("page", String.valueOf(page.getCurrentPageNo()));
				rtnMap.put("records", page.getTotalCount());
				rtnMap.put("rows", list);
			}
			
			//必須是按下UI的查詢才紀錄
			if(isSearch.equals("Y")){
				userlog_bo.writeLog("C", null, null, params);
			}
		}catch(Exception e){
			e.printStackTrace();
			rtnMap.put("total", 1);
			rtnMap.put("page", 1);
			rtnMap.put("records", 0);
			rtnMap.put("rows", new ArrayList<>());
			rtnMap.put("msg", "查詢失敗");
			userlog_bo.writeFailLog("C", rtnMap, null, null, params);
		}
		
		return rtnMap;
	}
	
	public List<String> getConditionList(Map<String, String> params){
		List<String> conditionList = new ArrayList<String>();
		List<String> conditions_1 = new ArrayList<String>();
		
		if(StrUtils.isNotEmpty(params.get("OPBK_ID")) && !params.get("OPBK_ID").equalsIgnoreCase("all")){
			conditions_1.add("ACQUIREID = '" + params.get("OPBK_ID") + "'");
		}
		
		conditionList.add( combine(conditions_1) );
		return conditionList;
	}
	
	public String getOrderStr(String sidx, String sord){
		StringBuffer ordStr = new StringBuffer();
		if(StrUtils.isNotEmpty(sidx)){
			if("FILENAME".equalsIgnoreCase(sidx)){
				ordStr.append("ORDER BY BATCHSEQ " + sord);
			}
			else if("SUCCESSCOUNT".equalsIgnoreCase(sidx)){
				ordStr.append("ORDER BY (COALESCE(A.ACCEPTCOUNT, 0) - COALESCE(A.PROCCOUNT, 0)) " + sord);
			}
			else if("COUNT_A".equalsIgnoreCase(sidx)){
				ordStr.append("ORDER BY COALESCE((SELECT SUM(AC) FROM TEMP WHERE FLBATCHSEQ = A.BATCHSEQ AND CLEARINGPHASE = A.CLEARINGPHASE), 0) " + sord);
			}
			else if("AMT_A".equalsIgnoreCase(sidx)){
				ordStr.append("ORDER BY COALESCE((SELECT SUM(TXAMT) FROM TEMP WHERE FLBATCHSEQ = A.BATCHSEQ AND CLEARINGPHASE = A.CLEARINGPHASE AND AC = 1), 0) " + sord);
			}
			else if("COUNT_P".equalsIgnoreCase(sidx)){
				ordStr.append("ORDER BY COALESCE((SELECT SUM(PC) FROM TEMP WHERE FLBATCHSEQ = A.BATCHSEQ AND CLEARINGPHASE = A.CLEARINGPHASE), 0) " + sord);
			}
			else if("AMT_P".equalsIgnoreCase(sidx)){
				ordStr.append("ORDER BY COALESCE((SELECT SUM(TXAMT) FROM TEMP WHERE FLBATCHSEQ = A.BATCHSEQ AND CLEARINGPHASE = A.CLEARINGPHASE AND PC = 1), 0) " + sord);
			}
			else if("COUNT_R".equalsIgnoreCase(sidx)){
				ordStr.append("ORDER BY COALESCE((SELECT SUM(RC) FROM TEMP WHERE FLBATCHSEQ = A.BATCHSEQ AND CLEARINGPHASE = A.CLEARINGPHASE), 0) " + sord);
			}
			else if("AMT_R".equalsIgnoreCase(sidx)){
				ordStr.append("ORDER BY COALESCE((SELECT SUM(TXAMT) FROM TEMP WHERE FLBATCHSEQ = A.BATCHSEQ AND CLEARINGPHASE = A.CLEARINGPHASE AND RC = 1), 0) " + sord);
			}
			else{
				ordStr.append("ORDER BY A." + sidx + " "+sord);
			}
		}
		return ordStr.toString();
	}
	
	public Map<String, String> getFileMetadata(String ac_key, Map editParams){
		Map<String, String> metaData = new HashMap<String, String>();
		
		//營業日期(民國年)
		String bizdate = editParams.get("BIZDATE") == null ? zDateHandler.getTWDate() : (StrUtils.isEmpty((String)editParams.get("BIZDATE"))? zDateHandler.getTWDate() : (String)editParams.get("BIZDATE"));
		
		//操作行
		FLCONTROLTAB flcontroltab = null;
		String acquireId = "";
		String debugStr = "";
		try{
			String westBizdate = DateTimeUtils.convertDate(bizdate, "yyyyMMdd", "yyyyMMdd");
			
			String str_procseq = editParams.get("PROCSEQ") == null ? "0" : (StrUtils.isEmpty((String)editParams.get("PROCSEQ"))? "0" : (String)editParams.get("PROCSEQ"));
			BigDecimal bd_procseq = new BigDecimal(str_procseq);
			
			String batchSeq = editParams.get("BATCHSEQ") == null ? ""  : (StrUtils.isEmpty((String)editParams.get("BATCHSEQ"))? "" : (String)editParams.get("BATCHSEQ"));
			
			debugStr = String.format("{BIZDATE = '%s'; PROCSEQ = %d; BATCHSEQ = '%s'}", westBizdate, bd_procseq.intValue(), batchSeq);
			logger.debug("### GET FLCONTROLTAB BY PK >> " + debugStr);
			
			FLCONTROLTAB_PK pk = new FLCONTROLTAB_PK(westBizdate, bd_procseq, batchSeq);
			flcontroltab = flcontroltab_Dao.get(pk);
			if(flcontroltab != null){
				acquireId = flcontroltab.getACQUIREID();
			}else{
				logger.debug("### CANNOT GET DATA WITH >> " + debugStr);
				return null;
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error(e);
			logger.debug("### GET DATA EXCEPTION >> " + debugStr);
		}
		
		
		//即使是票交人員，下載時還是要到各家操作行的資料夾搜尋檔案，因此 getCompletePath() 要帶入 false
		String completePath = getCompletePath(false, bizdate, acquireId, "download");
		if(StrUtils.isEmpty(completePath)){
			return null;
		}
		
		if(ac_key.equalsIgnoreCase("rejectFileDownload")){
			//壓縮檔的檔名
			String zipFileName = getZipFileName(flcontroltab.getId().getBATCHSEQ(), flcontroltab.getFILELAYOUT());
			
			//取得「檢核錯誤檔檔名」、「錯誤原始檔檔名」，並將兩個檔案壓成一個ZIP，再把ZIP檔案路徑回傳
			String eFilePath = completePath + flcontroltab.getEFILENAME();
			String oFilePath = completePath + flcontroltab.getOFILENAME();
			//System.out.println("E >> " + eFilePath);
			//System.out.println("O >> " + oFilePath);
			
			//封裝檔案
			Map<String, Object> filenameAndDataMap = new HashMap<String, Object>();
			List<String> filenameList = new ArrayList<String>();
			List<byte[]> dataList = new ArrayList<byte[]>();
			
			//取得「檢核錯誤檔檔」
			try{
				File eFile = new File(eFilePath);
				if(eFile.exists()){
					filenameList.add(flcontroltab.getEFILENAME());
					dataList.add(IOUtils.toByteArray(new FileInputStream(eFile)));
				}else{
					logger.error("###1 Cannot find file >> " + eFilePath);
				}
			}catch(Exception e){
				e.printStackTrace();
				logger.error("###2 Cannot find file >> " + eFilePath);
			}
			
			//取得「錯誤原始檔」
			try{
				File oFile = new File(oFilePath);
				if(oFile.exists()){
					filenameList.add(flcontroltab.getOFILENAME());
					dataList.add(IOUtils.toByteArray(new FileInputStream(oFile)));
				}else{
					logger.error("###1 Cannot find file >> " + oFilePath);
				}
			}catch(Exception e){
				e.printStackTrace();
				logger.error("###2 Cannot find file >> " + oFilePath);
			}
			
			filenameAndDataMap.put("filenameList", filenameList);
			filenameAndDataMap.put("dataList", dataList);
			
			byte[] byteZIP = codeUtils.createZIP(
					(List<byte[]>)filenameAndDataMap.get("dataList"),
					(List<String>)filenameAndDataMap.get("filenameList"), null);
			//正常，把ZIP檔案放到路徑下
			if(byteZIP != null){
				try {
					codeUtils.putFileToPath(completePath + zipFileName, byteZIP);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				logger.debug("### ZIP >> " + completePath + zipFileName);
				metaData.put("PATH", completePath + zipFileName);
				metaData.put("NAME", zipFileName);
			}else{
				logger.error("### Error when create ZIP!");
			}
		}else if(ac_key.equalsIgnoreCase("resultFileDownload")){
			String rFilePath = completePath + flcontroltab.getRFILENAME();
			logger.debug("### RFILE PATH >> " + rFilePath);
			
			metaData.put("PATH", rFilePath);
			metaData.put("NAME", flcontroltab.getRFILENAME());
		}
		
		return metaData;
	}
	
	/**
	 * 取得檔案放置的共用根目錄路徑
	 * @return
	 */
	public String getRootPath(String action, String uploadPath, String downloadPath){
		String message = "";
		
		try{
			if(StrUtils.isEmpty(action)){
				message = codeUtils.appendMessage(message, "ERROR_系統異常：未指定欲取得的根目錄路徑類型");
				return message;
			}else if(action.equalsIgnoreCase("download")){
				if(StrUtils.isEmpty(downloadPath)){
					message = codeUtils.appendMessage(message, "ERROR_系統異常：無法取得下載路徑");
					return message;
				}
				message = downloadPath;
			}else if(action.equalsIgnoreCase("upload")){
				if(StrUtils.isEmpty(uploadPath)){
					message = codeUtils.appendMessage(message, "ERROR_系統異常：無法取得上傳路徑");
					return message;
				}
				message = uploadPath;
			}
			
			WebServerPath webserverpath = (WebServerPath) SpringAppCtxHelper.getBean("webserverpath");

			message = OSValidator.isWindows()?
					webserverpath.getServerRootUrl() + Arguments.getStringArg("RPT.PDF.PATH").substring(1) + "/" :
					message;
		}catch(Exception e){
			e.printStackTrace();
			message = "ERROR";
		}
		
		return message;
	}
	
	/**
	 * 來源檔案上傳目錄：/eachrpt/eachbatchdata/source/營業日期/操作行/
	 * 結果檔下載目錄：/eachrpt/eachbatchdata/result/營業日期/操作行/
	 * 營業日期： 民國年
	 * 操作行： 三碼，如 BGBK_ID = 0040000，則傳入參數為 004
	 * 如果 isACH = true，則「操作行」永遠指向 Configuration.properties 檔設定的 batdataUploadDir_ACH 值
	 * action: 'upload'=取得上傳路徑; 'download'=取得下載路徑
	 */
	public String getCompletePath(boolean isACH, String bizdate, String acquireId, String action){
		Map<String,String> valueMap = null;
		String completePath = "";
		
		//先從設定檔一次取得之後會用到的值
		valueMap = codeUtils.getPropertyValue("Configuration.properties", "batdataUploadDir_ACH", "batdataUploadPath", "batdataDownloadPath");
		if(valueMap == null){
			logger.error("Cannot acquire 'Configuration.properties'!");
			return null;
		}
		
		//根目錄路徑
		completePath = getRootPath(action, valueMap.get("batdataUploadPath"), valueMap.get("batdataDownloadPath"));
		if(completePath.startsWith("ERROR")){
			return null;
		}
		
		//營業日期
		if(StrUtils.isEmpty(bizdate)){
			return null;
		}else{
			completePath += bizdate + "/";
		}
		
		//操作行
		if(isACH){
			if(StrUtils.isNotEmpty(valueMap.get("batdataUploadDir_ACH"))){
				completePath += valueMap.get("batdataUploadDir_ACH") + "/";
			}else{
				logger.error("Configuration.properties 中沒有設定 batdataUploadDir_ACH 的值");
				return null;
			}
		}else{
			if(StrUtils.isEmpty(acquireId)){
				return null;
			}else{
				//判斷傳入的是不是三碼，否則截斷
				if(acquireId.length() > 3){
					acquireId = acquireId.substring(0, 3);
				}
				completePath += acquireId + "/";
			}
		}
		
		System.out.println("### COMPLETE PATH >> " + completePath);
		return completePath;
	}
	
	/**
	 * 產生ZIP的檔名
	 * @param batchSeq
	 * @param fileLayout
	 * @return
	 */
	public String getZipFileName(String batchSeq, String fileLayout){
		if(StrUtils.isEmpty(batchSeq) || StrUtils.isEmpty(fileLayout)){
			return "REJECTFILE_" + zDateHandler.getTimestamp().replaceAll(" |:|-", "") + ".zip";
		}else{
			return batchSeq + "_P" + fileLayout + ".zip";
		}
	}
	
	public String uploadValidation(Map<String, String> params){
		String resultStr = "";
		
		//1. 檢核是否為「約定非上傳時間」
		resultStr = sys_para_bo.checkAptTime(params);
		Map resultMap = JSONUtils.json2map(resultStr);
		if(((String)resultMap.get("result")).equalsIgnoreCase("FALSE")){
			return resultStr;
		}
		
		//2. 檢核每小時上傳檔案個數是否超過限制
		resultStr = this.checkAvailableUploadNumber(params);
		resultMap = JSONUtils.json2map(resultStr);
		if(((String)resultMap.get("result")).equalsIgnoreCase("FALSE")){
			return resultStr;
		}
		
		return resultStr;
	}
	public String uploadValidationFile(Map<String, String> params){
		String resultStr = "";
		
		//1. 檢核是否為「約定非上傳時間」
		
		
		return resultStr;
	}
	
	public String checkUploadFile(Map<String, String> params){
		Map rtnMap = new HashMap();
		rtnMap.put("result", "FALSE");
		rtnMap.put("msg", "檔名未符合格式");
		int num = 2 ;//檔案最大上傳數 
		String filekey = "DATAFILE";
		String mackey = "MACFILE";
		String macFileName = "" ,dataFileName = "" , bizdate = "" , nextBizdate = "" , fileBizdate= "";
		logger.debug("checkUploadFile.params>>>"+params);
		Map<String,String> serchStrs = JSONUtils.json2map((String)params.get("serchStrs"));
		logger.debug("serchStrs>>>"+serchStrs);
		try {
			bizdate = eachsysstatustab_bo.getBusinessDate();
			nextBizdate = eachsysstatustab_bo.getNextBusinessDate();
//			表示為下午15:30的換日階段 此時bizdate=nextBizdate 故另外抓
			if(bizdate.equals(nextBizdate) ){
				nextBizdate = DateTimeUtils.convertDate(1, nextBizdate, "yyyyMMdd", "yyyyMMdd");
				nextBizdate = wk_date_bo.getNextBizdateByThisDate(nextBizdate, 1);
			}
			logger.debug("bizdate>>>"+bizdate+",nextBizdate>>>"+nextBizdate);
			
			for(int i = 1 ;i <=num ; i++){
				filekey = filekey+i;mackey = mackey+i;
				if(serchStrs.containsKey(filekey) && StrUtils.isNotEmpty(serchStrs.get(filekey))){
					macFileName = serchStrs.get(mackey).split("\\.")[0];
					dataFileName = serchStrs.get(filekey).split("\\.")[0];
					logger.debug("dataFileName>>>"+dataFileName+",macFileName>>>"+macFileName);
					if(!dataFileName.equals(macFileName)){
						System.out.println("第"+i+"組檔名前後不符"+serchStrs.get(filekey)+","+serchStrs.get(mackey));
						rtnMap.put("msg", "檔名前後不符"+serchStrs.get(filekey)+","+serchStrs.get(mackey));
						logger.debug("第"+i+"組檔名前後不符"+serchStrs.get(filekey)+","+serchStrs.get(mackey));
						break;
					}
					System.out.println(serchStrs.get(filekey).length());
					String[] a = serchStrs.get(filekey).split("-");
					if(!a[0].equals(serchStrs.get("OPBK_ID").substring(0,3))){
						rtnMap.put("msg", "檔名前3碼:"+a[0]+"與操作行不符");
						logger.debug("檔名前3碼:"+a[0]+"與操作行不符");
						break;
					}
					System.out.println("a1"+a[1]);
					fileBizdate = a[1].substring(0,8);
					logger.debug("fileBizdate>>"+fileBizdate);
					if(fileBizdate.equals(bizdate) || fileBizdate.equals(nextBizdate)){
						logger.debug("日期符合:"+bizdate+"或次營業日:"+nextBizdate);
					}else{
						logger.debug("檔名中日期的部分需等於營業日:"+bizdate+"或次營業日:"+nextBizdate);
						rtnMap.put("msg", "檔名中日期的部分需等於營業日:"+bizdate+"或次營業日:"+nextBizdate);
						break;
					}
					rtnMap.put("result", "TRUE");
					rtnMap.remove("msg");
				}
				
			}//for end
			
		} catch (Exception e){
			e.printStackTrace();
			rtnMap.put("msg", "系統錯誤，檔名驗證異常"+e);
		}
		return JSONUtils.map2json(rtnMap);
	}
	
	/**
	 * 目前做法：當前尚可上傳的檔案數 = 總行檔的「每小時最多上傳個數」- 該小時實際已上傳檔案數
	 * TODO: 當前尚可上傳的檔案數 = 總行檔的「每小時最多上傳個數」- 該小時實際已上傳檔案數 + 整檔失敗之檔案個數
	 * @param params
	 * @return
	 */
	public String checkAvailableUploadNumber(Map<String, String> params){
		Map rtnMap = new HashMap();
		rtnMap.put("result", "FALSE");
		rtnMap.put("msg", "無法取得上傳個數設定");
		
		params.put("BGBK_ID", params.get("ACQUIREID"));
		System.out.println("params>>"+params);
		try {
			//每小時最多上傳個數
			int hrUploadMaxFile = Integer.valueOf((String)JSONUtils.json2map(bank_group_bo.getHR_UPLOAD_MAX_FILE(params)).get("HR_UPLOAD_MAX_FILE"));
			rtnMap.put("HR_UPLOAD_MAX_FILE", hrUploadMaxFile);
			
			//某操作行該小時實際上傳個數
			int hrUploadCount = 0;
			String bizdate = eachsysstatustab_bo.getBusinessDate();
			String acquireId = params.get("ACQUIREID")==null?"":params.get("ACQUIREID").trim();
			File dir = new File(getCompletePath(isACH(), bizdate, acquireId, "upload"));
			if(dir.exists()){
				for(File f : dir.listFiles()){
					if(f.exists() && isInThisHour(FileUtils.getCTime(f.getAbsolutePath(), "", 0))){
						hrUploadCount++;
					}
				}
			}
			rtnMap.put("HR_UPLOAD_COUNT", hrUploadCount);
			
			int hrUploadAvailable = (hrUploadMaxFile - hrUploadCount);
			rtnMap.put("HR_UPLOAD_AVAILABLE", hrUploadAvailable);
			if(hrUploadAvailable > 0){
				rtnMap.put("result", "TRUE");
				rtnMap.put("msg", "尚可上傳 " + hrUploadAvailable + " 筆");
			}else{
				rtnMap.put("result", "FALSE"); //20160819  655 UAT-20160427-02
				rtnMap.put("msg", "已達上傳個數上限");
			}
		} catch (Exception e){
			e.printStackTrace();
			rtnMap.put("msg", "取得上傳個數設定時發生錯誤");
		}
		return JSONUtils.map2json(rtnMap);
	}
	
	/**
	 * 判斷傳入的日期時間是否在當前日期小時範圍內
	 * @param dateTime : yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public boolean isInThisHour(String dateTime){
		boolean result = false;
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Calendar input = Calendar.getInstance();
			input.setTime(sdf.parse(dateTime));
			
			Calendar now = Calendar.getInstance();
			//先判斷日期
			if(df.format(input.getTime()).equals(df.format(now.getTime()))){
				now.set(Calendar.MINUTE, 0);
				now.set(Calendar.SECOND, 0);
				Date start = now.getTime();
				now.add(Calendar.HOUR, 1);
				Date end = now.getTime();
				
				result = zDateHandler.isInTimeRange(
						DateTimeUtils.format("HH:mm:ss", input.getTime()), 
						DateTimeUtils.format("HH:mm:ss", start), 
						DateTimeUtils.format("HH:mm:ss", end));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	
	public synchronized Map uploadFile(Map params){
		Map rtnMap = new HashMap();
		rtnMap.put("result", "FALSE");
		
		Map serchStrs = JSONUtils.json2map((String)params.get("serchStrs"));
		try{
			String bizdate = eachsysstatustab_bo.getBusinessDate();
			String acquireId = params.get("OPBK_ID")==null?"":(String)params.get("OPBK_ID");
			File dir = new File(getCompletePath(isACH(), bizdate, acquireId, "upload"));
			logger.debug("### Ready to upload => " + dir.getAbsolutePath());
//			uploadValidation(params);
			String retStr = checkUploadFile(params);
			rtnMap =  JSONUtils.json2map(retStr);
			if(rtnMap.get("result").equals("FALSE")){
				return rtnMap;
			}
			if(!dir.exists()){
				dir.mkdirs();
			}
			
			if(dir.exists()){
				//設定目錄權限
				setFilePermission(dir);
				
				//The size of 'DATAFILE' can referenced to 'Batdata_Upload_Form'
				FormFile datafile = null;
				FormFile macFile = null;
				String tmpName = "";
				for(int i = 1; i <= 20; i++){
					if(params.containsKey("DATAFILE" + i) && params.containsKey("MACFILE" + i)){
						datafile = (FormFile) params.get("DATAFILE" + i);
						macFile = (FormFile) params.get("MACFILE" + i);
						
						if(datafile != null && macFile != null){
							//檢查該目錄下是否已有相同檔名
							File file = new File(dir.getAbsolutePath(), datafile.getFileName());
							if(file.exists()){
								rtnMap.put("msg", datafile.getFileName() + " 檔案已存在，不可重複上傳");
								//寫操作軌跡記錄(失敗)
								userlog_bo.writeFailLog("K",rtnMap,null,params,params);
								return rtnMap;
							}
							file = new File(dir.getAbsolutePath(), macFile.getFileName());
							if(file.exists()){
								rtnMap.put("msg", macFile.getFileName() + " 檔案已存在，不可重複上傳");
								userlog_bo.writeFailLog("K",rtnMap,null,params,params);
								return rtnMap;
							}
							
							//上傳成功之筆數
							int uploadSuccessCount = 0;
							
							//上傳時檔名後綴加上.tmp，待mac上傳完畢更名(刪除檔名後綴)
							tmpName = datafile.getFileName() + ".tmp";
							file = new File(dir.getAbsolutePath(), tmpName);
							FileOutputStream fos = new FileOutputStream(file);
							fos.write(datafile.getFileData());
							fos.flush();
							fos.close();
							if(file.exists()){
								file.renameTo(new File(dir.getAbsolutePath(), datafile.getFileName()));
								//設定檔案權限
								setFilePermission(file);
								uploadSuccessCount++;
							}
							
							tmpName = macFile.getFileName() + ".tmp";
							file = new File(dir.getAbsolutePath(), tmpName);
							fos = new FileOutputStream(file);
							fos.write(macFile.getFileData());
							fos.flush();
							fos.close();
							if(file.exists()){
								file.renameTo(new File(dir.getAbsolutePath(), macFile.getFileName()));
								//設定檔案權限
								setFilePermission(file);
								uploadSuccessCount++;
							}
							
							if(uploadSuccessCount == 2){
								rtnMap.put("result", "TRUE");
								rtnMap.put("msg", "上傳成功");
								//寫操作軌跡記錄(成功)
								userlog_bo.writeLog("K",null,serchStrs,null);
							}else{
								rtnMap.put("msg", "上傳錯誤：有些檔案未完整上傳");
								userlog_bo.writeFailLog("K",rtnMap,null,params,params);
							}
						}
					}
				}
			}else{
				rtnMap.put("msg", "上傳錯誤：目錄建立失敗");
				userlog_bo.writeFailLog("K",rtnMap,null,params,params);
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("### UPLOAD FILE ERROR >> " + e);
			rtnMap.put("msg", "系統錯誤：檔案上傳失敗");
			userlog_bo.writeFailLog("K",rtnMap,null,params,params);
		}
		return rtnMap;
	}
	
	public boolean isACH(){
		Login_Form loginForm = (Login_Form) WebServletUtils.getRequest().getSession().getAttribute("login_form");
		if(loginForm != null && loginForm.getUserData() != null){
			Each_User_Form eachUserForm = loginForm.getUserData();
			if(StrUtils.isNotEmpty(eachUserForm.getUSER_TYPE()) && eachUserForm.getUSER_TYPE().equals("A")){
				return true;
			}
		}
		return false;
	}
	
	//將傳入的檔案(目錄)設定存取權限及擁有者、群組
	public void setFilePermission(File target){
		String filePath = target.getAbsolutePath();
		
		Map<String, String> valueMap = codeUtils.getPropertyValue("Configuration.properties", "batdataUploadPermission", "batdataUploadOwner", "batdataUploadOwnerGroup");
		if(valueMap == null){
			logger.error("### SET FILE PERMISSION FAIL >> " + filePath);
		}else if(OSValidator.isWindows()){
			logger.debug("### WINDOWS CANNOT EXEC THIS CMD!");
		}else{
			String cmd_1 = "chmod " + valueMap.get("batdataUploadPermission") + " " + filePath;
			String cmd_2 = "chown " + valueMap.get("batdataUploadOwner") + ":" + valueMap.get("batdataUploadOwnerGroup") + " " + filePath;
			List<String> cmds = new ArrayList<String>();
			cmds.add(cmd_1);
			cmds.add(cmd_2);
			
			try{
				for(String cmd : cmds){
					logger.debug("### EXEC >> " + cmd);
					Runtime.getRuntime().exec(cmd);
				}
			}catch(Exception e){
				e.printStackTrace();
				logger.error("### ERROR EXEC CMD >> " + e);
			}
		}
	}
	
	public String combine(List<String> conditions){
		String conStr = "";
		for(int i = 0 ; i < conditions.size(); i++){
			conStr += conditions.get(i);
			if(i < conditions.size() - 1){
				conStr += " AND ";
			}
		}
		return conStr;
	}

	public VW_ONBLOCKTAB_Dao getVw_onblocktab_Dao() {
		return vw_onblocktab_Dao;
	}

	public void setVw_onblocktab_Dao(VW_ONBLOCKTAB_Dao vw_onblocktab_Dao) {
		this.vw_onblocktab_Dao = vw_onblocktab_Dao;
	}

	public EACH_USERLOG_BO getUserlog_bo() {
		return userlog_bo;
	}

	public void setUserlog_bo(EACH_USERLOG_BO userlog_bo) {
		this.userlog_bo = userlog_bo;
	}

	public CodeUtils getCodeUtils() {
		return codeUtils;
	}

	public void setCodeUtils(CodeUtils codeUtils) {
		this.codeUtils = codeUtils;
	}

	public FLCONTROLTAB_Dao getFlcontroltab_Dao() {
		return flcontroltab_Dao;
	}

	public void setFlcontroltab_Dao(FLCONTROLTAB_Dao flcontroltab_Dao) {
		this.flcontroltab_Dao = flcontroltab_Dao;
	}

	public SYS_PARA_BO getSys_para_bo() {
		return sys_para_bo;
	}

	public void setSys_para_bo(SYS_PARA_BO sys_para_bo) {
		this.sys_para_bo = sys_para_bo;
	}

	public EACHSYSSTATUSTAB_BO getEachsysstatustab_bo() {
		return eachsysstatustab_bo;
	}

	public void setEachsysstatustab_bo(EACHSYSSTATUSTAB_BO eachsysstatustab_bo) {
		this.eachsysstatustab_bo = eachsysstatustab_bo;
	}

	public BANK_GROUP_BO getBank_group_bo() {
		return bank_group_bo;
	}

	public void setBank_group_bo(BANK_GROUP_BO bank_group_bo) {
		this.bank_group_bo = bank_group_bo;
	}

	public WK_DATE_BO getWk_date_bo() {
		return wk_date_bo;
	}

	public void setWk_date_bo(WK_DATE_BO wk_date_bo) {
		this.wk_date_bo = wk_date_bo;
	}
	
	
	
}
