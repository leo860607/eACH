package tw.org.twntch.action;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import tw.org.twntch.bo.AGENTDL_ST001_BO;
import tw.org.twntch.bo.AGENTDL_TX001_BO;
import tw.org.twntch.bo.AGENT_PROFILE_BO;
import tw.org.twntch.bo.Arguments;
import tw.org.twntch.bo.BANK_GROUP_BO;
import tw.org.twntch.bo.EACHSYSSTATUSTAB_BO;
import tw.org.twntch.bo.EACH_USERLOG_BO;
import tw.org.twntch.bo.TRANSACTIONDAY_DOWNLOAD_BO;
import tw.org.twntch.bo.TXS_DAY_BO;
import tw.org.twntch.form.Login_Form;
import tw.org.twntch.form.Rpt_Agent_Form;
import tw.org.twntch.form.Transactionday_Download_Form;
import tw.org.twntch.util.BeanUtils;
import tw.org.twntch.util.CodeUtils;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.OSValidator;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.WebServletUtils;

public class AGENTDL_ST001_Action extends GenericAction{
	private CodeUtils codeUtils;
	private AGENTDL_ST001_BO agentdl_st001_bo;
	private EACHSYSSTATUSTAB_BO eachsysstatustab_bo;
	private EACH_USERLOG_BO userlog_bo;//寫操作軌跡記錄
	private AGENT_PROFILE_BO agent_profile_bo ;
	private Logger logger = Logger.getLogger(this.getClass().getName());
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
		Rpt_Agent_Form rpt_agent_form = (Rpt_Agent_Form) form;
		
		String ac_key = StrUtils.isEmpty(rpt_agent_form.getAc_key())?"":rpt_agent_form.getAc_key();
		String target = StrUtils.isEmpty(rpt_agent_form.getTarget())?"search":rpt_agent_form.getTarget();
		rpt_agent_form.setTarget(target);
		Login_Form login_form = (Login_Form)request.getSession().getAttribute("login_form");
		logger.debug("ac_key=" + ac_key+",target=" + target);
		logger.debug("rpt_agent_form.getCompany_IdList()=" + rpt_agent_form.getCompany_IdList());
		String tmpFileDir ="";
		ServletContext context = WebServletUtils.getServletContext();
		if(context != null){
			tmpFileDir = context.getRealPath(Arguments.getStringArg("RPT.PDF.PATH"));
		}
		//交易代號清單
		//票交端
		if(login_form.getUserData().getUSER_TYPE().equals("A")){
			//TODO 改代理業者清單
			rpt_agent_form.setCompany_IdList(agent_profile_bo.getCompany_Id_ABBR_List());
		}
		//參加單位端
		else{
//			參加單位端代入登入時的單位ID
			rpt_agent_form.setAGENT_COMPANY_ID(login_form.getUserData().getUSER_COMPANY());
		}
		//初次進入此功能
		if(ac_key.equalsIgnoreCase("")){
			//將營業日塞到頁面的日期控制項
			String busDate = eachsysstatustab_bo.getRptBusinessDate();
			rpt_agent_form.setBIZDATE(busDate);
		}
		
		//下載檔案
		if(ac_key.equalsIgnoreCase("download")){
			//將頁面上的查詢條件放進pkMap
			Map<String,Object> pkMap = new HashMap<String,Object>();
			pkMap.put("serchStrs",rpt_agent_form.getSerchStrs());
			//如果有錯誤要將訊息放進去
			Map<String,Object> msgMap = new HashMap<String,Object>();
			try{
				//取得在classpath下的properties檔案
				Map<String,String> valueMap = codeUtils.getPropertyValue("Configuration.properties","agentDirPath","agentst001FilePrefix");
				//無法取得properties
				if(valueMap == null){
					//回頁面alert訊息
					rpt_agent_form.setMsg("無法取得properties檔");
					
					//寫操作軌跡記錄(失敗)
					msgMap.put("msg",rpt_agent_form.getMsg());
					userlog_bo.writeFailLog("F",msgMap,null,null,pkMap);
					
					return mapping.findForward(target);
				}
				
				if(login_form.getUserData().getUSER_TYPE().equals("B")){
					Map retmap = super.checkRPT_BizDate(rpt_agent_form.getBIZDATE(), rpt_agent_form.getCLEARINGPHASE());
					if(retmap.get("result").equals("FALSE") ){
						BeanUtils.populate(rpt_agent_form, retmap);
						
						//寫操作軌跡記錄(失敗)
						msgMap.put("msg",retmap.get("msg"));
						userlog_bo.writeFailLog("F",msgMap,null,null,pkMap);
						
						return mapping.findForward(target);
					}
				}
				
				//取得放置檔案的資料夾根目錄
				String dirPath = OSValidator.isWindows()?tmpFileDir:valueMap.get("agentDirPath");
				logger.debug("dirPath="+dirPath);
				//取得檔名的前置碼
				String filePrefix = valueMap.get("agentst001FilePrefix");
				logger.debug("filePrefix="+filePrefix);
			
				//日期
				String date = StrUtils.isEmpty(rpt_agent_form.getBIZDATE())?"":DateTimeUtils.convertDate(rpt_agent_form.getBIZDATE(), "yyyyMMdd", "yyyyMMdd");
				//代理業者代號
				String agent_company_id = StrUtils.isEmpty(rpt_agent_form.getAGENT_COMPANY_ID())?"":rpt_agent_form.getAGENT_COMPANY_ID();
				//發動者代號
				String snd_company_id = StrUtils.isEmpty(rpt_agent_form.getSND_COMPANY_ID())?"":rpt_agent_form.getSND_COMPANY_ID();
				//清算階段代號
				String clearingphase = StrUtils.isEmpty(rpt_agent_form.getCLEARINGPHASE())?"":rpt_agent_form.getCLEARINGPHASE();
				
				//產生檔案和下載檔案的路徑
				String filePath = dirPath+"/"+date+"/"+agent_company_id+"/";
				logger.debug("filePath==>"+filePath);
				//產生檔案和下載檔案的檔名
				String fileName = filePrefix+agent_company_id+"_"+date+"_"+clearingphase+".txt";
				logger.debug("fileName==>"+fileName);
				//產生檔案和下載檔案的路徑加檔名
				String fullPath = filePath+fileName;
				logger.debug("fullPath==>"+fullPath);
				
//				//先去完整路徑抓檔案
				InputStream inputStream = codeUtils.getFileFromPath(fullPath);
				Map<String,Object> dataMap = agentdl_st001_bo.getTXT(date,agent_company_id,snd_company_id,clearingphase,true);
				//正常
				if(dataMap.get("data") != null){
					//檔案放到資料夾下
					codeUtils.putFileToPath(fullPath,(byte[])dataMap.get("data"));
					//再一次去完整路徑抓檔案
					inputStream = codeUtils.getFileFromPath(fullPath);
					//有檔案
					if(inputStream != null){
						//頁面塞時間的token
						String downloadToken = rpt_agent_form.getDow_token();
						//將檔案吐到前端頁面
						codeUtils.forDownload(inputStream,fileName,"fileDownloadToken",downloadToken);
						
						//寫操作軌跡記錄(成功)
						userlog_bo.writeLog("F",null,null,pkMap);
						
						return null;
					}
					//alert message到頁面
					else{
						rpt_agent_form.setMsg("下載過程出現問題");
					}
				}
				//有問題
				else{
					rpt_agent_form.setMsg((String)dataMap.get("message"));
				}
			}
			catch(Exception e){
				e.printStackTrace();
				rpt_agent_form.setMsg(e.getMessage());
				rpt_agent_form.setTarget("search");
			}
			//寫操作軌跡記錄(失敗)
			msgMap.put("msg",rpt_agent_form.getMsg());
			userlog_bo.writeFailLog("F",msgMap,null,null,pkMap);
		}
		target = StrUtils.isEmpty(rpt_agent_form.getTarget())?"":rpt_agent_form.getTarget();
		return mapping.findForward(target);
	}
	
	
	public CodeUtils getCodeUtils() {
		return codeUtils;
	}
	public void setCodeUtils(CodeUtils codeUtils) {
		this.codeUtils = codeUtils;
	}
	
	
	

	public AGENTDL_ST001_BO getAgentdl_st001_bo() {
		return agentdl_st001_bo;
	}


	public void setAgentdl_st001_bo(AGENTDL_ST001_BO agentdl_st001_bo) {
		this.agentdl_st001_bo = agentdl_st001_bo;
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


	public AGENT_PROFILE_BO getAgent_profile_bo() {
		return agent_profile_bo;
	}


	public void setAgent_profile_bo(AGENT_PROFILE_BO agent_profile_bo) {
		this.agent_profile_bo = agent_profile_bo;
	}


	
	
}
