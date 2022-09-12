package tw.org.twntch.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import tw.org.twntch.bo.AGENT_PROFILE_BO;
import tw.org.twntch.bo.Arguments;
import tw.org.twntch.bo.BASEDATA_DOWNLOAD_BO;
import tw.org.twntch.bo.EACH_USERLOG_BO;
import tw.org.twntch.bo.TRANSACTIONDAY_DOWNLOAD_BO;
import tw.org.twntch.bo.TRANSACTIONDETAIL_DOWNLOAD_BO;
import tw.org.twntch.bo.TRANSACTIONMONTH_DOWNLOAD_BO;
import tw.org.twntch.db.dao.hibernate.BANK_GROUP_Dao;
import tw.org.twntch.po.EACH_USERLOG;
import tw.org.twntch.socket.HSMSuipResponseData;

public class HTTPDownload {
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private TRANSACTIONDETAIL_DOWNLOAD_BO transactiondetail_download_bo ;
	private BASEDATA_DOWNLOAD_BO basedata_download_bo ;
	private TRANSACTIONDAY_DOWNLOAD_BO transactionday_download_bo ;
	private TRANSACTIONMONTH_DOWNLOAD_BO transactionmonth_download_bo ;
	private AGENT_PROFILE_BO agent_profile_bo ;
	private CodeUtils codeUtils ;
	private BANK_GROUP_Dao bank_group_Dao ;
	private EACH_USERLOG_BO userlog_bo ;
	String underline ="_";String zip = ".zip";
	
	public void forDownload(String pathname ,String filename ,String msg ,boolean check){
		ServletOutputStream out = null;
		FileInputStream in = null;
		boolean result = false;
		try{
			//URLEncoder.encode避免中文顯示錯誤
			HttpServletResponse resp = WebServletUtils.getResponse();
			if(check){
				File file = new File(pathname+filename);
				if(file.exists()){
					resp.setHeader("Content-Disposition","attachment;filename="+URLEncoder.encode(filename,"UTF-8"));
					//設定輸出型態為串流
					resp.setContentType("application/octet-stream");
					in =new FileInputStream( file);
					out = resp.getOutputStream();
					out.write(IOUtils.toByteArray(in));
					in.close();
					out.flush();
					out.close();
					msg = "檔案已正常輸出，檔名:"+filename;
					result = true;
				}else{
					msg = "系統異常，檔案不存在";
					resp.setHeader("Content-Type", "text/plain");
					resp.setHeader("success", "yes");
//					resp.setStatus(500);
					resp.setStatus(200);
					resp.setContentType("text/html;charset=UTF-8");
					resp.getWriter().write(msg);
					resp.getWriter().close();;
				}
			}else{
				resp.setHeader("Content-Type", "text/plain");
				resp.setHeader("success", "yes");
				resp.setStatus(200);
//				resp.setContentType("text/xml");
				resp.setContentType("text/html;charset=UTF-8");
				resp.getWriter().write(msg);
				resp.getWriter().close();;
			}
		}
		catch(Exception e){
			e.printStackTrace();
			logger.error("forDownload.Exception>>"+e.getMessage());
		}finally{
			writeLog("4", msg, result);
		}
	}
	
	public void writeLog(String str  , String msg  ,boolean result ){
		String tmpStr = "";
		String queryString = "";
		String opbk_id = "";
 		System.out.println("str>>"+str);
		try {
			queryString = WebServletUtils.getRequest().getQueryString();
//			opbk_id = WebServletUtils.getRequest().getParameter("opbkId");
			opbk_id = StrUtils.isEmpty(WebServletUtils.getRequest().getParameter("opbkId")) ? WebServletUtils.getRequest().getParameter("agentId") : WebServletUtils.getRequest().getParameter("opbkId") ;
			EACH_USERLOG po = userlog_bo.getEACH_USERLOG(str , opbk_id);
			po.setFUNC_ID("eACH9997");
			if(result){
				tmpStr = "下載成功 ，";
			}else {
				tmpStr = "下載失敗 ，";
			}
			tmpStr += msg;
			po.setFUNC_TYPE("2");
			po.setADEXCODE(tmpStr);
			po.setBFCHCON("QueryString>>>"+queryString);
			userlog_bo.save(po);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 修改自downloadForHTTP_kyuso 但修改程曲批次產生的檔案 
	 * @param functionName
	 * @param date
	 * @param opbkId(代理清算行代號也用這個)
	 * @param clearingphase
	 * @param YYYYMM
	 * @return
	 */
	public void downloadForHTTP(String functionName,String date , String bizmon,String opbkId , String clrbk,String clearingphase,String YYYYMM ,String reqmac , String keyFlag ){	
		
		String filePath = "";String fileName = "";
		String transactiondetailDirPath =  "";
		String transactiondetailFilePrefix = "";
		String transactiondetailRPTPrefix = "";
		String basedataFilePrefix = "";
		String basedataFilePrefix_UTF8 = "";
		String transactiondayFilePrefix = "";
		String transactionmonthFilePrefix = "";
		String feedayFilePrefix = "";
		String feemonthFilePrefix = "";
		String cleardayFilePrefix = "";
		String keyname = "opbkId";
//		Mac 驗證所需參數
		String macData = "";
		int index = 0;
		//錯誤訊息(如果有的話)
		String message = "";
		String userIP = "";
		try{
			userIP = WebServletUtils.getRequest().getHeader("HTTP_X_FORWARDED_FOR");
			System.out.println("HTTP_X_FORWARDED_FOR.userIP>>"+userIP);
			userIP = StrUtils.isEmpty(userIP)?WebServletUtils.getRequest().getHeader("X-Forwarded-For"):userIP;
			System.out.println("X-Forwarded-For.userIP>>"+userIP);
			userIP = StrUtils.isEmpty(userIP)?WebServletUtils.getRequest().getHeader("Remote_Addr"):userIP;
			System.out.println("getHeader(Remote_Addr).userIP>>"+userIP);
			userIP = StrUtils.isEmpty(userIP)?WebServletUtils.getRequest().getRemoteAddr():userIP;
			System.out.println("getRequest().getRemoteAddr().userIP>>"+userIP);
			WebServerPath webserverpath = (WebServerPath)  SpringAppCtxHelper.getBean("webserverpath");
//			windowdir=eACH/tmp/
			String windowdir =  webserverpath.getServerRootUrl()+"/"+Arguments.getStringArg("RPT.PDF.PATH")+ "/";
			
			//取得在classpath下的properties檔案
			Map<String,String> valueMap = codeUtils.getPropertyValue("Configuration.properties","transactiondetailDirPath","transactiondetailFilePrefix","basedataFilePrefix","basedataFilePrefix_UTF8","transactiondayFilePrefix","transactionmonthFilePrefix","feedayFilePrefix","feemonthFilePrefix","cleardayFilePrefix","transactiondetailRPTPrefix");
			//無法取得properties
			if(valueMap == null){
				message = codeUtils.appendMessage(message,"系統異常：無法取得properties檔");
				forDownload("", "" , message ,false );
				return ;
			}
			index = validateFuncId(functionName);
			if(index ==0 ){
				message = codeUtils.appendMessage(message,"參數錯誤：funcId ="+functionName);
				forDownload("", "" , message ,false );
				return ;
			}
			if(!verify_keyFlag(keyFlag)){
				return ;
			}
			//因為代理清算匯出的是帶代理清算行代號 要另外處理
			opbkId = index == 11 ? clrbk :opbkId;
			keyname = index == 11 ? "clrbk" :keyname;
			if(!verify_opbkId(opbkId , keyname)){
				return ;
			}

			
//			if(index ==1 || index ==5 || index ==11){
			if(!(index ==2 || index ==4 || index ==9 || index ==12)){
				if(!verify_clsPhase(clearingphase)){
					return;
				}
			}

			if(index == 2 || index == 12){//common && commonutf8 不驗日期
				date = "";
				macData = functionName+opbkId;
			}else if( index == 4 || index == 9 ){//	txmonsum ，txmonsum 月  ，日期格式:yyyyMM
				if(!verify_bizmon(bizmon)){
					return;
				}
				date = bizmon; //後續MAC驗證
				macData = bizmon+functionName;
			}else{
				if(!verify_bizDate(date)){// yyyyMMdd
					return;
				}
				macData = date+functionName;
			}
//			TODO 正式及測試記得打開
			if(!verifyMAC(reqmac, opbkId , macData, keyFlag)){
				message = codeUtils.appendMessage(message,"mac驗證失敗：mac ="+reqmac);
				forDownload("", "" , message ,false );
				return ;
			}
			
//			transactiondetailDirPath =  valueMap.get("transactiondetailDirPath");
			transactiondetailDirPath =  OSValidator.isWindows() ? windowdir :valueMap.get("transactiondetailDirPath");
//			要先檢核各個參數是否齊全   不齊 RETURN
			if(StrUtils.isEmpty(transactiondetailDirPath)){
				message = codeUtils.appendMessage(message,"系統異常 :找不到路徑");
				forDownload("", "" , message ,false );
				return ;
			}
			transactiondetailFilePrefix = valueMap.get("transactiondetailFilePrefix");
			logger.debug("transactiondetailFilePrefix="+transactiondetailFilePrefix);
			transactiondetailRPTPrefix = valueMap.get("transactiondetailRPTPrefix");
			logger.debug("transactiondetailRPTPrefix="+transactiondetailRPTPrefix);
			basedataFilePrefix = valueMap.get("basedataFilePrefix");
			logger.debug("basedataFilePrefix="+basedataFilePrefix);
			basedataFilePrefix_UTF8 = valueMap.get("basedataFilePrefix_UTF8");
			logger.debug("basedataFilePrefix_UTF8="+basedataFilePrefix_UTF8);
			transactiondayFilePrefix = valueMap.get("transactiondayFilePrefix");
			logger.debug("transactiondayFilePrefix="+transactiondayFilePrefix);
			transactionmonthFilePrefix = valueMap.get("transactionmonthFilePrefix");
			logger.debug("transactionmonthFilePrefix="+transactionmonthFilePrefix);
			feedayFilePrefix = valueMap.get("feedayFilePrefix");
			logger.debug("feedayFilePrefix="+feedayFilePrefix);
			feemonthFilePrefix = valueMap.get("feemonthFilePrefix");
			logger.debug("feemonthFilePrefix="+feemonthFilePrefix);
			cleardayFilePrefix = valueMap.get("cleardayFilePrefix");
			logger.debug("cleardayFilePrefix="+cleardayFilePrefix);
			switch (index) {
			case 1:
				txlist_download(message, transactiondetailFilePrefix, transactiondetailDirPath, date, opbkId, clearingphase);
				break;
			case 2:
				common_download(message, basedataFilePrefix, transactiondetailDirPath, opbkId);
				break;
			case 3:
				txdaysum_download(message, transactiondayFilePrefix, transactiondetailDirPath, date, opbkId , clearingphase);
				break;
			case 4:
				txmonsum_download(message, transactionmonthFilePrefix, transactiondetailDirPath, YYYYMM, opbkId);
				break;
			case 5:
				txlistrp_download(message, transactiondetailRPTPrefix, transactiondetailDirPath, date, opbkId, clearingphase);
				break;
			case 6:
				cldaysumrp_download(message, transactiondetailDirPath, date, opbkId ,clearingphase);
				break;
			case 7:
				feedaysumrp_download(message, transactiondetailDirPath, date, opbkId , clearingphase);
				break;
			case 8:
				feedaysum_download(message, feedayFilePrefix, transactiondetailDirPath, date, opbkId,clearingphase);
				break;
			case 9:
				feemonsum_download(message, feemonthFilePrefix, transactiondetailDirPath, YYYYMM, opbkId);
				break;
			case 10:
				cldaysum_download(message, cleardayFilePrefix, transactiondetailDirPath, date, opbkId,clearingphase);
				break;
			case 11:
				//因為代理清算的檔名比較特別，所以和其他的做法不一樣
//				clbkbal_download(message, DateTimeUtils.getCDateShort(new SimpleDateFormat("yyyyMMdd").parse(date)),transactiondetailDirPath, date, opbkId, clearingphase);
				clbkbal_download(message, DateTimeUtils.convertDate(DateTimeUtils.INTERCONVERSION, date, "yyyyMMdd", "yyyyMMdd"),transactiondetailDirPath, date, opbkId, clearingphase);
				break;
			case 12:
				common_download(message, basedataFilePrefix_UTF8, transactiondetailDirPath, opbkId);
				break;
			default:
				message = codeUtils.appendMessage(message,"系統異常：無相關API，funcId ="+functionName);
				forDownload("", "" , message ,false );
				break;
			}
		}
		catch(Exception e){
			e.printStackTrace();
			logger.debug("downloadForHTTP.Exception>>"+e.getMessage());
			message = codeUtils.appendMessage(message,"系統異常：請洽相關資訊人員");
			forDownload(filePath, fileName , message ,false );
		}
	}
	
	/**
	 * 代理業者專用HTTP下載 
	 * @param functionName
	 * @param date
	 * @param opbkId(代理業者統編)
	 * @param clearingphase
	 * @param YYYYMM
	 * @return
	 */
	public void downloadForHTTP_4_AGENT(String functionName,String date , String bizmon,String agentId , String clrbk,String clearingphase,String YYYYMM ,String reqmac , String keyFlag ){	
		
		String filePath = "";String fileName = "";
		String transactiondetailDirPath =  "";
		String transactiondetailFilePrefix = "";
		String basedataFilePrefix = "";
		String keyname = "opbkId";
//		Mac 驗證所需參數
		String macData = "";
//		int index = 0;
		//錯誤訊息(如果有的話)
		String message = "";
		String userIP = "";
		int index = 0;
		try{
			userIP = WebServletUtils.getRequest().getHeader("HTTP_X_FORWARDED_FOR");
			System.out.println("HTTP_X_FORWARDED_FOR.userIP>>"+userIP);
			userIP = StrUtils.isEmpty(userIP)?WebServletUtils.getRequest().getHeader("X-Forwarded-For"):userIP;
			System.out.println("X-Forwarded-For.userIP>>"+userIP);
			userIP = StrUtils.isEmpty(userIP)?WebServletUtils.getRequest().getHeader("Remote_Addr"):userIP;
			System.out.println("getHeader(Remote_Addr).userIP>>"+userIP);
			userIP = StrUtils.isEmpty(userIP)?WebServletUtils.getRequest().getRemoteAddr():userIP;
			System.out.println("getRequest().getRemoteAddr().userIP>>"+userIP);
			WebServerPath webserverpath = (WebServerPath)  SpringAppCtxHelper.getBean("webserverpath");
//			windowdir=eACH/tmp/
			String windowdir =  webserverpath.getServerRootUrl()+"/"+Arguments.getStringArg("RPT.PDF.PATH")+ "/";
			
			//取得在classpath下的properties檔案
			Map<String,String> valueMap = codeUtils.getPropertyValue("Configuration.properties","transactiondetailDirPath","agent_Zip_name","agent_Zip_name_M","basedataFilePrefix_C","basedataFilePrefix","basedataFilePrefix_UTF8","basedataFilePrefix_C_UTF8");
			//無法取得properties
			if(valueMap == null){
				message = codeUtils.appendMessage(message,"系統異常：無法取得properties檔");
				forDownload("", "" , message ,false );
				return ;
			}
			
			index = validateFuncId4Agent(functionName);
			if(index ==0 ){
				message = codeUtils.appendMessage(message,"參數錯誤：funcId ="+functionName);
				forDownload("", "" , message ,false );
				return ;
			}
			if(!verify_keyFlag(keyFlag)){
				return ;
			}
			//驗證代理業者統編
			if(!verify_comm_id(agentId , keyname)){
				return ;
			}
			//驗證清算階段
			if(index == 1){
				if(!verify_clsPhase(clearingphase)){
					return;
				}
			}
			//驗證營業日
			if(index == 1){
				if(!verify_bizDate(date)){// yyyyMMdd
					return;
				}
			}
			if(index == 2){
				if(!verify_bizmon(bizmon) ){// yyyyMMdd
					return;
				}
				date = bizmon;
			}
			if(index == 3 || index == 4){
				date = "";
			}
			macData = date+functionName;
//			TODO 正式及測試記得打開
			if(!verifyMAC(reqmac, agentId , macData, keyFlag)){
				message = codeUtils.appendMessage(message,"mac驗證失敗：mac ="+reqmac);
				forDownload("", "" , message ,false );
				return ;
			}
			
			transactiondetailDirPath =  OSValidator.isWindows() ? windowdir :valueMap.get("transactiondetailDirPath");
//			要先檢核各個參數是否齊全   不齊 RETURN
			if(StrUtils.isEmpty(transactiondetailDirPath)){
				message = codeUtils.appendMessage(message,"系統異常 :找不到路徑");
				forDownload("", "" , message ,false );
				return ;
			}
			logger.debug("transactiondetailFilePrefix="+transactiondetailFilePrefix);
			switch (index) {
			case 1:
				transactiondetailFilePrefix = valueMap.get("agent_Zip_name");
				agentData_download(message, transactiondetailFilePrefix, transactiondetailDirPath, date, agentId, clearingphase);
				break;
			case 2:
				transactiondetailFilePrefix = valueMap.get("agent_Zip_name_M");
				agentDataMon_download(message, transactiondetailFilePrefix, transactiondetailDirPath, bizmon, agentId);
				break;
			case 3:
				transactiondetailFilePrefix = valueMap.get("basedataFilePrefix_C");
				basedataFilePrefix  = valueMap.get("basedataFilePrefix");
				agentDataComm_download(message, transactiondetailFilePrefix, transactiondetailDirPath, basedataFilePrefix);
				break;
			case 4:
				transactiondetailFilePrefix = valueMap.get("basedataFilePrefix_C_UTF8");
				basedataFilePrefix  = valueMap.get("basedataFilePrefix_UTF8");
				agentDataComm_download(message, transactiondetailFilePrefix, transactiondetailDirPath, basedataFilePrefix);
				break;
			default:
				break;
			}
		}
		catch(Exception e){
			e.printStackTrace();
			logger.debug("downloadForHTTP_4_AGENT.Exception>>"+e.getMessage());
			message = codeUtils.appendMessage(message,"系統異常：請洽相關資訊人員");
			forDownload(filePath, fileName , message ,false );
		}
	}
	
	
	
	public void agentData_download(String message, String transactiondetailFilePrefix, String transactiondetailDirPath, String date,String agentId,String clearingphase){
		boolean result =false ;
		
		if(StrUtils.isEmpty(transactiondetailFilePrefix)){
			message = codeUtils.appendMessage(message,"系統異常 :找不到檔名");
		}
		String filePath = transactiondetailDirPath+File.separator+date+File.separator+agentId+File.separator+Arguments.getStringArg("BAT.LAST.PATH")+File.separator;
		String fileName =  transactiondetailFilePrefix+underline+agentId+underline+date+underline+clearingphase+zip;
		logger.debug("filePath>>"+filePath);
		logger.debug("fileName>>"+fileName);
		result = message.length()!=0? false:true;
		forDownload(filePath, fileName , message ,result );
	}
	public void agentDataMon_download(String message, String transactiondetailFilePrefix, String transactiondetailDirPath, String bizmon,String agentId){
		boolean result =false ;
		
		if(StrUtils.isEmpty(transactiondetailFilePrefix)){
			message = codeUtils.appendMessage(message,"系統異常 :找不到檔名");
		}
		String filePath = transactiondetailDirPath+File.separator+bizmon+File.separator+agentId+File.separator+Arguments.getStringArg("BAT.LAST.PATH")+File.separator;
		String fileName =  transactiondetailFilePrefix+underline+agentId+underline+bizmon+zip;
		logger.debug("filePath>>"+filePath);
		logger.debug("fileName>>"+fileName);
		result = message.length()!=0? false:true;
		forDownload(filePath, fileName , message ,result );
	}
	public void agentDataComm_download(String message, String transactiondetailFilePrefix, String transactiondetailDirPath, String basedataFilePrefix){
		boolean result =false ;
		
		if(StrUtils.isEmpty(transactiondetailFilePrefix)){
			message = codeUtils.appendMessage(message,"系統異常 :找不到檔名");
		}
		String filePath  = transactiondetailDirPath+File.separator+basedataFilePrefix+File.separator;
		String fileName =  transactiondetailFilePrefix+zip;
		logger.debug("filePath>>"+filePath);
		logger.debug("fileName>>"+fileName);
		result = message.length()!=0? false:true;
		forDownload(filePath, fileName , message ,result );
	}
	
	
	
	public void txlist_download(String message, String transactiondetailFilePrefix, String transactiondetailDirPath, String date,String opbkId,String clearingphase){
		boolean result =false ;
		
		if(StrUtils.isEmpty(transactiondetailFilePrefix)){
			message = codeUtils.appendMessage(message,"系統異常 :找不到檔名");
		}
//		String filePath = transactiondetailDirPath+File.separator+date+File.separator+opbkId+File.separator;
		String filePath = transactiondetailDirPath+File.separator+date+File.separator+opbkId+File.separator+Arguments.getStringArg("BAT.LAST.PATH")+File.separator;
//		String fileName =  transactiondetailFilePrefix+underline+date+underline+clearingphase+underline+opbkId+zip;
		String fileName =  transactiondetailFilePrefix+underline+opbkId+underline+date+underline+clearingphase+zip;
		logger.debug("filePath>>"+filePath);
		logger.debug("fileName>>"+fileName);
		result = message.length()!=0? false:true;
		forDownload(filePath, fileName , message ,result );
	}
	
	
	public void common_download(String message, String basedataFilePrefix, String transactiondetailDirPath, String opbkId){
		boolean result =false ;
		if(StrUtils.isEmpty(basedataFilePrefix)){
			message = codeUtils.appendMessage(message,"系統異常 :找不到檔名");
		}
		String filePath  = transactiondetailDirPath+File.separator+basedataFilePrefix+File.separator;
		String fileName = basedataFilePrefix+zip;
		logger.debug("filePath>>"+filePath);
		logger.debug("fileName>>"+fileName);
		result = message.length()!=0? false:true;
		forDownload(filePath, fileName , message ,result );
	}
	
	
	public void txdaysum_download(String message, String transactiondayFilePrefix, String transactiondetailDirPath, String date,String opbkId , String clearingphase){
		boolean result =false ;
		if(StrUtils.isEmpty(transactiondayFilePrefix)){
			message = codeUtils.appendMessage(message,"系統異常 :找不到檔名");
		}
//		String filePath = transactiondetailDirPath+File.separator+date+File.separator+opbkId+File.separator;
//		String fileName = transactiondayFilePrefix+File.separator+date+File.separator+opbkId+".txt";
		String filePath = transactiondetailDirPath+File.separator+date+File.separator+opbkId+File.separator+Arguments.getStringArg("BAT.LAST.PATH")+File.separator;
//		String fileName = transactiondayFilePrefix+underline+date+underline+opbkId+underline+clearingphase+".txt";
		String fileName = transactiondayFilePrefix+underline+opbkId+underline+date+underline+clearingphase+".txt";
		logger.debug("filePath>>"+filePath);
		logger.debug("fileName>>"+fileName);
		result = message.length()!=0? false:true;
		forDownload(filePath, fileName , message ,result );
	}
	
	
	public void txmonsum_download(String message, String transactionmonthFilePrefix, String transactiondetailDirPath, String YYYYMM,String opbkId){
		boolean result =false ;
//		String filePath  = transactiondetailDirPath+File.separator+YYYYMM+File.separator+opbkId+File.separator;
		String filePath  = transactiondetailDirPath+File.separator+YYYYMM+File.separator+opbkId+File.separator+Arguments.getStringArg("BAT.LAST.PATH")+File.separator;
//		String fileName  = transactionmonthFilePrefix+File.separator+YYYYMM+File.separator+opbkId+".txt";
//		String fileName  = transactionmonthFilePrefix+underline+YYYYMM+underline+opbkId+".txt";
		String fileName  = transactionmonthFilePrefix+underline+opbkId+underline+YYYYMM+".txt";
		if(StrUtils.isEmpty(transactionmonthFilePrefix)){
			message = codeUtils.appendMessage(message,"系統異常 :找不到檔名");
		}
		logger.debug("filePath>>"+filePath);
		logger.debug("fileName>>"+fileName);
		result = message.length()!=0? false:true;
		forDownload(filePath, fileName , message ,result );
	}
	
	public int validateFuncId(String fincId){
		int index = 0;
		List<String> list = SpringAppCtxHelper.getBean("funcIds");
		if(list!=null){
			index = list.contains(fincId)? list.indexOf(fincId)+1:0 ;
		}
		return index;
	}
	/**
	 * 代理業者下載檔案
	 * @param fincId
	 * @return
	 */
	public int validateFuncId4Agent(String fincId){
		int index = 0;
		List<String> list = SpringAppCtxHelper.getBean("agent_funcIds");
		if(list!=null){
			index = list.contains(fincId)? list.indexOf(fincId)+1:0 ;
		}
		return index;
	}
	
	public boolean verifyMAC(String reqmac ,String opbkId ,String macData ,String keyFlag){
		boolean result = false;
		String hsmIP = "";
		String hsmPort = "";
		String keyId = "";
		String mac;
		HSMSuipResponseData hsmSuipResponseData = null;
		try {
//			0040000-->004
//			opbkId = "0040000";
			keyId = opbkId.substring(0,3);
			Map<String,String> parameterMap = SpringAppCtxHelper.getBean("parameters");
			if(parameterMap == null || StrUtils.isEmpty(parameterMap.get("hsmIP")) || StrUtils.isEmpty(parameterMap.get("hsmPort"))){
				logger.debug("short  parameter  parameterMap>>"+parameterMap+",hsmIP>>"+hsmIP+",hsmPort>>"+hsmPort);
				return false;
			}
			hsmIP = parameterMap.get("hsmIP");
			hsmPort = parameterMap.get("hsmPort");
			hsmSuipResponseData = codeUtils.getHSMResponse(keyId ,macData, hsmIP, Integer.valueOf(hsmPort) , keyFlag);
			if(hsmSuipResponseData == null || StrUtils.isEmpty(hsmSuipResponseData.getMac()) ){
				logger.debug("HSMSuipResponseData is null >>"+hsmSuipResponseData);
				return false;
			}
			if(hsmSuipResponseData.getResponse().equals("01")){
				logger.debug("hsmSuipResponseData fail"+hsmSuipResponseData.toString());
				logger.debug(" hsmSuipResponseData fail"+hsmSuipResponseData.toString() );
				logger.debug("hsmSuipResponseData fail"+hsmSuipResponseData.toString());
				logger.debug(" hsmSuipResponseData fail"+hsmSuipResponseData.toString() );
				return false;
			}
			mac = hsmSuipResponseData.getMac();
			if(StrUtils.isEmpty(mac)){
				logger.debug("hsmSuipResponseData.getMac is null >>"+mac);
				return false;
			}
			logger.debug("before URLdecode>>"+reqmac);
//			20150522 edit by hugo 因filter本身就有decode，故註解
//			reqmac = URLDecoder.decode(reqmac,"UTF-8");
//			logger.debug("after URLdecode>>"+reqmac);
			Base64 base64 = new Base64();
			reqmac = HSMMACUtils.toHex(base64.decode(reqmac.getBytes()))  ;
			logger.debug("after  Base64.decode>>"+reqmac);
			result = mac.equals(reqmac)? true:false;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = false;
		}
		
		return result;
	}
	
	public boolean verify_opbkId(String opbkId ,String keyname){
		boolean result =false;
		String errmsg = "";
		if(StrUtils.isEmpty(opbkId)){
			result = false;
			errmsg = codeUtils.appendMessage(errmsg, "參數錯誤 :"+keyname+"必填， opbkId="+opbkId);
			forDownload("", "", errmsg, false);
			return result;
		}
		if(opbkId.length() !=7){
			result = false;
			errmsg = codeUtils.appendMessage(errmsg, "參數錯誤 :長度不符， "+keyname+"="+opbkId);
			forDownload("", "", errmsg, false);
			return result;
		}
		
//		還要做該總行是否啟用停用的檢核
		List list = bank_group_Dao.getBgbkId_Data(opbkId);
		if(list == null || list.size()==0){
			result = false;
			errmsg = codeUtils.appendMessage(errmsg, "參數錯誤 :查無資料(已停用或未啟用) "+keyname+"="+opbkId);
			forDownload("", "", errmsg, false);
			return result;
		}
		result =true;
		return result;
	}
	/**
	 * 驗證統編
	 * @param opbkId
	 * @param keyname
	 * @return
	 */
	public boolean verify_comm_id(String opbkId ,String keyname){
		boolean result =false;
		String errmsg = "";
		if(StrUtils.isEmpty(opbkId)){
			result = false;
			errmsg = codeUtils.appendMessage(errmsg, "參數錯誤 :"+keyname+"必填， opbkId="+opbkId);
			forDownload("", "", errmsg, false);
			return result;
		}
		if(opbkId.length() !=8){
			result = false;
			errmsg = codeUtils.appendMessage(errmsg, "參數錯誤 :長度不符， "+keyname+"="+opbkId);
			forDownload("", "", errmsg, false);
			return result;
		}
		
//		還要做該總行是否啟用停用的檢核
		List list = agent_profile_bo.getAgent_profile_Dao().getAgentCom_Data(opbkId);
		if(list == null || list.size()==0){
			result = false;
			errmsg = codeUtils.appendMessage(errmsg, "參數錯誤 :查無資料(已停用或未啟用) "+keyname+"="+opbkId);
			forDownload("", "", errmsg, false);
			return result;
		}
		result =true;
		return result;
	}
	public boolean verify_keyFlag(String keyFlag){
		boolean result =false;
		String errmsg = "";
		String keys = "01,02,03,04";
		if(StrUtils.isEmpty(keyFlag)){
			result = false;
			errmsg = codeUtils.appendMessage(errmsg, "參數錯誤 :keyFlag必填， keyFlag="+keyFlag);
			forDownload("", "", errmsg, false);
			return result;
		}
		if(keyFlag.length() !=2){
			result = false;
			errmsg = codeUtils.appendMessage(errmsg, "參數錯誤 :長度不符， keyFlag="+keyFlag);
			forDownload("", "", errmsg, false);
			return result;
		}
		logger.debug("keys.indexOf>>"+keys.indexOf(keyFlag));
		if(keys.indexOf(keyFlag) < 0){
			result = false;
			errmsg = codeUtils.appendMessage(errmsg, "參數錯誤 :keyFlag不合法， keyFlag="+keyFlag);
			forDownload("", "", errmsg, false);
			return result;
		}
		result =true;
		return result;
	}
	public boolean verify_bizDate(String bizDate){
		boolean result =false;
		String errmsg = "";
		if(StrUtils.isEmpty(bizDate)){
			result = false;
			errmsg = codeUtils.appendMessage(errmsg, "參數錯誤 :bizDate必填， bizDate="+bizDate);
			forDownload("", "", errmsg, false);
			return result;
		}
		if(bizDate.length() !=8){
			result = false;
			errmsg = codeUtils.appendMessage(errmsg, "參數錯誤 :長度不符， bizDate="+bizDate);
			forDownload("", "", errmsg, false);
			return result;
		}
		if(!bizDate.matches("^[0-9]*[1-9][0-9]*$")){
			result = false;
			errmsg = codeUtils.appendMessage(errmsg, "參數錯誤 :必須是數字， bizDate="+bizDate);
			forDownload("", "", errmsg, false);
			return result;
		}
		String tmpdate = DateTimeUtils.convertDate(2,bizDate, "yyyyMMdd", "yyyy/MM/dd");
		logger.debug("tmpdate>>"+tmpdate);
		if(!bizDate.matches("((19|20)\\d\\d)(0?[1-9]|1[012])(0?[1-9]|[12][0-9]|3[01])")){
			result = false;
			errmsg = codeUtils.appendMessage(errmsg, "參數錯誤 :日期不合法， bizDate="+bizDate);
			forDownload("", "", errmsg, false);
			return result;
		}
		result =true;
		return result;
	}
	
	public boolean verify_bizmon(String bizmon){
		boolean result =false;
		String errmsg = "";
		if(StrUtils.isEmpty(bizmon)){
			result = false;
			errmsg = codeUtils.appendMessage(errmsg, "參數錯誤 :bizmon必填， bizmon="+bizmon);
			forDownload("", "", errmsg, false);
			return result;
		}
		if(bizmon.length() !=6){
			result = false;
			errmsg = codeUtils.appendMessage(errmsg, "參數錯誤 :長度不符， bizmon="+bizmon);
			forDownload("", "", errmsg, false);
			return result;
		}
//		if(!bizmon.matches("^[0-9]*[1-9][0-9]*$")){
		if(!bizmon.matches("^-?[0-9]+$")){
			result = false;
			errmsg = codeUtils.appendMessage(errmsg, "參數錯誤 :必須是數字， bizmon="+bizmon);
			forDownload("", "", errmsg, false);
			return result;
		}
		String tmpdate = bizmon+"01";
//		tmpdate = DateTimeUtils.convertDate(bizmon, "yyyyMMdd", "yyyy/MM/dd");
		if(!tmpdate.matches("((19|20)\\d\\d)(0?[1-9]|1[012])(0?[1-9]|[12][0-9]|3[01])")){
			result = false;
			errmsg = codeUtils.appendMessage(errmsg, "參數錯誤 :bizmon不合法， bizmon="+bizmon);
			forDownload("", "", errmsg, false);
			return result;
		}
		result =true;
		return result;
	}
	public boolean verify_clsPhase(String clsPhase){
		boolean result =false;
		String errmsg = "";
		String str = "01,02";
		logger.debug(clsPhase);
		if(StrUtils.isEmpty(clsPhase)){
			result = false;
			errmsg = codeUtils.appendMessage(errmsg, "參數錯誤 :clsPhase必填， clsPhase="+clsPhase);
			forDownload("", "", errmsg, false);
			return result;
		}
		if(clsPhase.length() !=2){
			result = false;
			errmsg = codeUtils.appendMessage(errmsg, "參數錯誤 :長度不符， clsPhase="+clsPhase);
			forDownload("", "", errmsg, false);
			return result;
		}
//		if(clsPhase.indexOf(str) < 0){
		logger.debug("indexOf>>"+str.indexOf(clsPhase));
		if(str.indexOf(clsPhase) < 0){
			result = false;
			errmsg = codeUtils.appendMessage(errmsg, "參數錯誤 :clsPhase不合法， clsPhase="+clsPhase);
			forDownload("", "", errmsg, false);
			return result;
		}
		result =true;
		return result;
	}
	
	
	public void txlistrp_download(String message, String transactiondetailFilePrefix, String transactiondetailDirPath, String date,String opbkId,String clearingphase){
		boolean result =false ;
		
		if(StrUtils.isEmpty(transactiondetailFilePrefix)){
			message = codeUtils.appendMessage(message,"系統異常 :找不到檔名");
		}
//		String filePath = transactiondetailDirPath+File.separator+date+File.separator+opbkId+File.separator;
		String filePath = transactiondetailDirPath+File.separator+date+File.separator+opbkId+File.separator+Arguments.getStringArg("BAT.LAST.PATH")+File.separator;
//		String fileName =  transactiondetailFilePrefix+underline+date+underline+clearingphase+underline+opbkId+"_pdf"+zip;
		String fileName =  transactiondetailFilePrefix+underline+opbkId+underline+date+underline+clearingphase+".zip";
		logger.debug("filePath>>"+filePath);
		logger.debug("fileName>>"+fileName);
		result = message.length()!=0? false:true;
		forDownload(filePath, fileName , message ,result );
	}
	public void cldaysumrp_download(String message,  String transactiondetailDirPath, String date,String opbkId , String clearingphase){
		boolean result =false ;
		String filePath ="";
//		eACH_ClsDaySum_0040000_20150201_01.pdf
		String fileName ="";
		String str = Arguments.getStringArg("BAT.FILE.NAME.CL1");
		if(StrUtils.isEmpty(str)){
			message = codeUtils.appendMessage(message,"系統異常 :找不到檔名");
		}else{
//			fileName =  str.replace("#1", date).replace("#2", opbkId);
			fileName =  str.replace("#1", opbkId ).replace("#2", date).replace("#3", clearingphase);
			fileName += ".pdf";
		}
//		filePath = transactiondetailDirPath+File.separator+date+File.separator+opbkId+File.separator;
		filePath = transactiondetailDirPath+File.separator+date+File.separator+opbkId+File.separator+Arguments.getStringArg("BAT.LAST.PATH")+File.separator;
		logger.debug("filePath>>"+filePath);
		logger.debug("fileName>>"+fileName);
		result = message.length()!=0? false:true;
		forDownload(filePath, fileName , message ,result );
	}
	public void feedaysumrp_download(String message,  String transactiondetailDirPath, String date,String opbkId , String clearingphase){
		boolean result =false ;
		String filePath ="";
//		eACH_FeeDaySum_0040000_20150201_01.pdf
		String fileName ="";
		String str = Arguments.getStringArg("BAT.FILE.NAME.FEE1");
		if(StrUtils.isEmpty(str)){
			message = codeUtils.appendMessage(message,"系統異常 :找不到檔名");
		}else{
//			fileName =  str.replace("#1", date).replace("#2", opbkId);
			fileName =  str.replace("#1", opbkId ).replace("#2", date).replace("#3", clearingphase);
			fileName += ".pdf";
		}
//		filePath = transactiondetailDirPath+File.separator+date+File.separator+opbkId+File.separator;
		filePath = transactiondetailDirPath+File.separator+date+File.separator+opbkId+File.separator+Arguments.getStringArg("BAT.LAST.PATH")+File.separator;
		logger.debug("filePath>>"+filePath);
		logger.debug("fileName>>"+fileName);
		result = message.length()!=0? false:true;
		forDownload(filePath, fileName , message ,result );
	}
	public void feedaysum_download(String message, String feedayFilePrefix, String transactiondetailDirPath, String date,String opbkId , String clearingphase ){
		boolean result =false ;
		if(StrUtils.isEmpty(feedayFilePrefix)){
			message = codeUtils.appendMessage(message,"系統異常 :找不到檔名");
		}
//		String filePath = transactiondetailDirPath+File.separator+date+File.separator+opbkId+File.separator;
		String filePath = transactiondetailDirPath+File.separator+date+File.separator+opbkId+File.separator+Arguments.getStringArg("BAT.LAST.PATH")+File.separator;
//		String fileName = feedayFilePrefix+File.separator+date+File.separator+opbkId+".txt";
		String fileName = feedayFilePrefix+underline+opbkId+underline+date+underline+clearingphase+".txt";
		logger.debug("filePath>>"+filePath);
		logger.debug("fileName>>"+fileName);
		result = message.length()!=0? false:true;
		forDownload(filePath, fileName , message ,result );
	}
	public void feemonsum_download(String message, String feemonthFilePrefix, String transactiondetailDirPath, String YYYYMM,String opbkId){
		boolean result =false ;
//		String filePath  = transactiondetailDirPath+File.separator+YYYYMM+File.separator+opbkId+File.separator;
		String filePath  = transactiondetailDirPath+File.separator+YYYYMM+File.separator+opbkId+File.separator+Arguments.getStringArg("BAT.LAST.PATH")+File.separator;
//		String fileName  = feemonthFilePrefix+File.separator+YYYYMM+File.separator+opbkId+".txt";
		String fileName  = feemonthFilePrefix+underline+opbkId+underline+YYYYMM+".txt";
		if(StrUtils.isEmpty(feemonthFilePrefix)){
			message = codeUtils.appendMessage(message,"系統異常 :找不到檔名");
		}
		logger.debug("filePath>>"+filePath);
		logger.debug("fileName>>"+fileName);
		result = message.length()!=0? false:true;
		forDownload(filePath, fileName , message ,result );
	}
	public void cldaysum_download(String message, String cleardayFilePrefix, String transactiondetailDirPath, String date,String opbkId , String clearingphase){
		boolean result =false ;
		if(StrUtils.isEmpty(cleardayFilePrefix)){
			message = codeUtils.appendMessage(message,"系統異常 :找不到檔名");
		}
//		String filePath = transactiondetailDirPath+File.separator+date+File.separator+opbkId+File.separator;
		String filePath = transactiondetailDirPath+File.separator+date+File.separator+opbkId+File.separator+Arguments.getStringArg("BAT.LAST.PATH")+File.separator;
//		String fileName = cleardayFilePrefix+File.separator+date+File.separator+opbkId+".txt";
		String fileName = cleardayFilePrefix+underline+opbkId+underline+date+underline+clearingphase+".txt";
		logger.debug("filePath>>"+filePath);
		logger.debug("fileName>>"+fileName);
		result = message.length()!=0? false:true;
		forDownload(filePath, fileName , message ,result );
	}
	public void clbkbal_download(String message, String proxybankFilePrefix, String transactiondetailDirPath, String date,String opbkId,String clearingphase){
		boolean result =false ;
		if(StrUtils.isEmpty(proxybankFilePrefix)){
			message = codeUtils.appendMessage(message,"系統異常 :找不到檔名");
		}
		String tmpDate =    "0"+DateTimeUtils.convertDate(DateTimeUtils.INTERCONVERSION, date, "yyyyMMdd", "yyyMMdd");
//		String filePath = transactiondetailDirPath+File.separator+date+File.separator+opbkId+File.separator;
		String filePath = transactiondetailDirPath+File.separator+date+File.separator+opbkId+File.separator+Arguments.getStringArg("BAT.LAST.PATH")+File.separator;
		//因為同一天雖然因為時分秒的因素而有許多檔案，但是所有一階和二階的檔案內容都是一樣的，因此只要判斷日期和清算階段即可
		File proxybankFile = new File(filePath);
		String fileName = "";
		logger.debug("proxybankFilePrefix>>"+proxybankFilePrefix);
		if(proxybankFile.exists()){
			for(File f:proxybankFile.listFiles()){
				logger.debug("f.getName()>>"+f.getName());
				logger.debug("isContains>>"+f.getName().contains(proxybankFilePrefix));
				if(f.getName().contains(proxybankFilePrefix)){
//					20150601 edit by hugo
//					if(f.getName().substring(0,8).equals(proxybankFilePrefix) && f.getName().substring(17,19).equals(clearingphase)){
//						logger.debug("a>>"+f.getName().substring(0,7));
//						logger.debug("b>>"+f.getName().substring(16,18));
						logger.debug("a>>"+f.getName().substring(0,8));
						logger.debug("b>>"+f.getName().substring(17,19));
						logger.debug("tmpDate>>"+tmpDate);
						logger.debug("clearingphase>>"+clearingphase);
//						logger.debug("e>>"+f.getName().substring(0,7).equals(tmpDate));
//						logger.debug("f>>"+f.getName().substring(16,18).equals(clearingphase));
						logger.debug("e>>"+f.getName().substring(0,8).equals(tmpDate));
						logger.debug("f>>"+f.getName().substring(17,19).equals(clearingphase));
//					if(f.getName().substring(0,7).equals(tmpDate) && f.getName().substring(16,18).equals(clearingphase)){
					if(f.getName().substring(0,8).equals(tmpDate) && f.getName().substring(17,19).equals(clearingphase)){
						fileName = f.getName();
						logger.debug("in.fileName>>"+fileName);
						break;
					}
				}
			}
		}
		logger.debug("filePath>>"+filePath);
		logger.debug("fileName>>"+fileName);
		result = message.length()!=0? false:true;
		forDownload(filePath, fileName , message ,result );
	}
	
	/**
	 * kyuso 原本寫的版本 暫時不使用
	 * @param functionName
	 * @param date
	 * @param opbkId
	 * @param clearingphase
	 * @param YYYYMM
	 * @return
	 */
	public Map<String,Object> downloadForHTTP_kyuso(String functionName,String date,String opbkId,String clearingphase,String YYYYMM){	
		//錯誤訊息(如果有的話)
		String message = "";
		//檔案的串流
		InputStream inputStream = null;
		//串流的檔名
		String dataName = "";
		//Zip的Byte[]
		byte[] byteZIP = null;
		//回傳的Map
		Map<String,Object> returnMap = new HashMap<String,Object>();
		try{
			//取得在classpath下的properties檔案
			Map<String,String> valueMap = codeUtils.getPropertyValue("Configuration.properties","transactiondetailFilePrefix","basedataFilePrefix","transactiondayFilePrefix","transactionmonthFilePrefix");
			//無法取得properties
			if(valueMap == null){
				message = codeUtils.appendMessage(message,"伺服器端錯誤：無法取得properties檔");
			}
			else{
				//取得TXT或ZIP檔名的前置碼
				String transactiondetailFilePrefix = valueMap.get("transactiondetailFilePrefix");
				logger.debug("transactiondetailFilePrefix="+transactiondetailFilePrefix);
				String basedataFilePrefix = valueMap.get("basedataFilePrefix");
				logger.debug("basedataFilePrefix="+basedataFilePrefix);
				String transactiondayFilePrefix = valueMap.get("transactiondayFilePrefix");
				logger.debug("transactiondayFilePrefix="+transactiondayFilePrefix);
				String transactionmonthFilePrefix = valueMap.get("transactionmonthFilePrefix");
				logger.debug("transactionmonthFilePrefix="+transactionmonthFilePrefix);
				//各功能的檔名
				String transactiondetailFileName = transactiondetailFilePrefix+"_"+date+(StrUtils.isEmpty(clearingphase)?"":"_"+clearingphase)+(StrUtils.isEmpty(opbkId)?"":"_"+opbkId)+".zip";
				logger.debug("transactiondetailFileName==>"+transactiondetailFileName);
				String basedataFileName = basedataFilePrefix+".zip";
				logger.debug("basedataFileName==>"+basedataFileName);
				String transactiondayFileName = transactiondayFilePrefix+"_"+date+(StrUtils.isEmpty(opbkId)?"":"_"+opbkId)+".txt";
				logger.debug("transactiondayFileName==>"+transactiondayFileName);
				String transactionmonthFileName = transactionmonthFilePrefix+"_"+YYYYMM+(StrUtils.isEmpty(opbkId)?"":"_"+opbkId)+".txt";
				logger.debug("transactionmonthFileName==>"+transactionmonthFileName);
				
				if("txlist".equalsIgnoreCase(functionName)){
					Map<String,Object> filenameAndDataMap = transactiondetail_download_bo.getFilenameListAndDataList(date,opbkId,clearingphase);
					
					//List有TXT的Byte[]才做
					if(((List<byte[]>)filenameAndDataMap.get("dataList")).size() > 0){
						//Zip的Byte[]
						byteZIP = codeUtils.createZIP((List<byte[]>)filenameAndDataMap.get("dataList"),(List<String>)filenameAndDataMap.get("filenameList"),null);
						//正常
						if(byteZIP != null){
							inputStream = new ByteArrayInputStream(byteZIP);
							returnMap.put("dataName",transactiondetailFileName);
						}
						//有問題
						else{
							message = codeUtils.appendMessage(message,"伺服器端錯誤：壓縮交易明細資料檔過程出現問題");
						}
					}
					else{
						message = codeUtils.appendMessage(message,"伺服器端錯誤：查詢交易明細資料檔過程出現問題");
					}
				}
				else if("common".equalsIgnoreCase(functionName)){
					Map<String,Object> filenameAndDataMap = basedata_download_bo.getFilenameListAndDataList();
					//List有TXT的Byte[]才做
					if(((List<byte[]>)filenameAndDataMap.get("dataList")).size() > 0){
						//Zip的Byte[]
						byteZIP = codeUtils.createZIP((List<byte[]>)filenameAndDataMap.get("dataList"),(List<String>)filenameAndDataMap.get("filenameList"),null);
						//正常
						if(byteZIP != null){
							inputStream = new ByteArrayInputStream(byteZIP);
							returnMap.put("dataName",basedataFileName);
						}
						//有問題
						else{
							message = codeUtils.appendMessage(message,"伺服器端錯誤：壓縮共用代碼資料的檔案過程出現問題");
						}
					}
					else{
						message = codeUtils.appendMessage(message,"伺服器端錯誤：查詢共用代碼資料的檔案過程出現問題");
					}
				}
				else if("txdaysum".equalsIgnoreCase(functionName)){
					Map<String,Object> dataMap = transactionday_download_bo.getTXT(date,null,opbkId,clearingphase);
					
					//正常
					if(dataMap.get("data") != null){
						inputStream = new ByteArrayInputStream((byte[])dataMap.get("data"));
						returnMap.put("dataName",transactiondayFileName);
					}
					//有問題
					else{
						message = codeUtils.appendMessage(message,"伺服器端錯誤：查詢交易日統計資料的檔案過程出現問題");
					}
				}
				else if("txmonsum".equalsIgnoreCase(functionName)){
					Map<String,Object> dataMap = transactionmonth_download_bo.getTXT(YYYYMM,null,opbkId);
					
					//正常
					if(dataMap.get("data") != null){
						inputStream = new ByteArrayInputStream((byte[])dataMap.get("data"));
						returnMap.put("dataName",transactionmonthFileName);
					}
					//有問題
					else{
						message = codeUtils.appendMessage(message,"伺服器端錯誤：查詢交易月統計資料的檔案過程出現問題");
					}
				}
				else{
					message = codeUtils.appendMessage(message,"用戶端錯誤：無此Function");
				}
				
			}
		}
		catch(Exception e){
			e.printStackTrace();
			logger.error(e.getMessage());
			message = codeUtils.appendMessage(message,"伺服器端錯誤："+e.getMessage());
		}
		
		returnMap.put("message",message);
		returnMap.put("data",inputStream);
		
		return returnMap;
	}
	public TRANSACTIONDETAIL_DOWNLOAD_BO getTransactiondetail_download_bo() {
		return transactiondetail_download_bo;
	}
	public void setTransactiondetail_download_bo(
			TRANSACTIONDETAIL_DOWNLOAD_BO transactiondetail_download_bo) {
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
	public void setTransactionday_download_bo(
			TRANSACTIONDAY_DOWNLOAD_BO transactionday_download_bo) {
		this.transactionday_download_bo = transactionday_download_bo;
	}
	public TRANSACTIONMONTH_DOWNLOAD_BO getTransactionmonth_download_bo() {
		return transactionmonth_download_bo;
	}
	public void setTransactionmonth_download_bo(
			TRANSACTIONMONTH_DOWNLOAD_BO transactionmonth_download_bo) {
		this.transactionmonth_download_bo = transactionmonth_download_bo;
	}
	public CodeUtils getCodeUtils() {
		return codeUtils;
	}
	public void setCodeUtils(CodeUtils codeUtils) {
		this.codeUtils = codeUtils;
	}
	
	public BANK_GROUP_Dao getBank_group_Dao() {
		return bank_group_Dao;
	}
	public void setBank_group_Dao(BANK_GROUP_Dao bank_group_Dao) {
		this.bank_group_Dao = bank_group_Dao;
	}
	
	
	public EACH_USERLOG_BO getUserlog_bo() {
		return userlog_bo;
	}

	public void setUserlog_bo(EACH_USERLOG_BO userlog_bo) {
		this.userlog_bo = userlog_bo;
	}

	
	
	public AGENT_PROFILE_BO getAgent_profile_bo() {
		return agent_profile_bo;
	}

	public void setAgent_profile_bo(AGENT_PROFILE_BO agent_profile_bo) {
		this.agent_profile_bo = agent_profile_bo;
	}

	public static void main(String[] args) throws UnsupportedEncodingException {
		HTTPDownload http = new HTTPDownload();
//		http.VerifyMAC("", "0040000");
	}
	
}
