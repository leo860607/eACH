package tw.org.twntch.action;

import java.io.File;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.util.JsonUtil;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import tw.org.twntch.bo.Arguments;
import tw.org.twntch.bo.BANK_GROUP_BO;
import tw.org.twntch.bo.BATDATA_UPLOAD_BO;
import tw.org.twntch.bo.EACHSYSSTATUSTAB_BO;
import tw.org.twntch.bo.EACH_USERLOG_BO;
import tw.org.twntch.bo.SC_COMPANY_PROFILE_BO;
import tw.org.twntch.bo.TXN_CODE_BO;
import tw.org.twntch.form.Batdata_Upload_Form;
import tw.org.twntch.form.Login_Form;
import tw.org.twntch.form.SC_Company_Profile_Form;
import tw.org.twntch.po.SC_COMPANY_PROFILE;
import tw.org.twntch.util.CodeUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.SpringAppCtxHelper;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.WebServerPath;
import tw.org.twntch.util.WebServletUtils;

public class BATDATA_UPLOAD_Action extends GenericAction{
	private BATDATA_UPLOAD_BO batdata_upload_bo;
	private BANK_GROUP_BO bank_group_bo;
	private CodeUtils codeUtils;
	private EACH_USERLOG_BO userlog_bo;

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Batdata_Upload_Form batdata_upload_form = (Batdata_Upload_Form) form;
		Login_Form login_form = (Login_Form) WebServletUtils.getRequest().getSession().getAttribute("login_form");
		String ac_key = StrUtils.isEmpty(batdata_upload_form.getAc_key())?"":batdata_upload_form.getAc_key();
		String target = StrUtils.isEmpty(batdata_upload_form.getTarget())?"search":batdata_upload_form.getTarget();
		batdata_upload_form.setTarget(target);
		System.out.println("BATDATA_UPLOAD_Action is start >> " + ac_key);
		
		if(ac_key.contains("FileDownload")){
			Map<String,Object> pkMap = new HashMap<String,Object>();
			pkMap.put("serchStrs", batdata_upload_form.getSerchStrs());
			Map<String,Object> msgMap = new HashMap<String,Object>();
			
			Map editParams = JSONUtils.json2map(batdata_upload_form.getEdit_params());
			Map<String, String> fileMetadata = batdata_upload_bo.getFileMetadata(ac_key, editParams);
			if(fileMetadata == null){
				batdata_upload_form.setMsg("找不到檔案目錄");
				//寫操作軌跡記錄(失敗)
				msgMap.put("msg", batdata_upload_form.getMsg());
				userlog_bo.writeFailLog("F", msgMap, null, null, pkMap);
			}else{
				//取得放置的檔案
				System.out.println("fileMetadata.get(PATH)>>"+fileMetadata.get("PATH"));
				InputStream inputStream = codeUtils.getFileFromPath(fileMetadata.get("PATH"));
				//沒問題
				if(inputStream != null){
					//頁面塞時間的token
					String downloadToken = batdata_upload_form.getDow_token();
					//將檔案吐到前端頁面
//					20160310 edit by hugo 偶發性下載壓縮檔時會發生錯誤 先改用此方式試試看
//					codeUtils.forDownload(inputStream, fileMetadata.get("NAME"), "fileDownloadToken", downloadToken);
					exportFile2Client(response, fileMetadata.get("PATH"), fileMetadata.get("NAME"), downloadToken, false);
					
					//寫操作軌跡記錄(成功)
					userlog_bo.writeLog("F", null, null, pkMap);
					return null;
				}
				//找不到檔案或檔案轉成InputStream出現問題
				else{
					batdata_upload_form.setMsg("找不到檔案或檔案轉成InputStream出現問題");
					//寫操作軌跡記錄(失敗)
					msgMap.put("msg", batdata_upload_form.getMsg());
					userlog_bo.writeFailLog("F", msgMap, null, null, pkMap);
				}
			}
		}
		
		if(ac_key.equalsIgnoreCase("upload")){
			Map params = BeanUtils.describe(batdata_upload_form);
			params = setFiles(batdata_upload_form, params);
			BeanUtils.populate(batdata_upload_form, batdata_upload_bo.uploadFile(params));
		}
		
		target = StrUtils.isEmpty(batdata_upload_form.getTarget())?"search":batdata_upload_form.getTarget();
		//操作行清單
		batdata_upload_form.setOpbkIdList(bank_group_bo.getOpbkList());
		if(login_form.getUserData().getUSER_TYPE().equals("B")){
			batdata_upload_form.setOPBK_ID(login_form.getUserData().getUSER_COMPANY());
		}
		
		return mapping.findForward(target);
	}
	
	//The size of 'DATAFILE' can referenced to 'Batdata_Upload_Form'
	public Map setFiles(Batdata_Upload_Form form, Map params){
		params.put("DATAFILE1", form.getDATAFILE1());
		params.put("MACFILE1", form.getMACFILE1());
		params.put("DATAFILE2", form.getDATAFILE2());
		params.put("MACFILE2", form.getMACFILE2());
		params.put("DATAFILE3", form.getDATAFILE3());
		params.put("MACFILE3", form.getMACFILE3());
		params.put("DATAFILE4", form.getDATAFILE4());
		params.put("MACFILE4", form.getMACFILE4());
		params.put("DATAFILE5", form.getDATAFILE5());
		params.put("MACFILE5", form.getMACFILE5());
		params.put("DATAFILE6", form.getDATAFILE6());
		params.put("MACFILE6", form.getMACFILE6());
		params.put("DATAFILE7", form.getDATAFILE7());
		params.put("MACFILE7", form.getMACFILE7());
		params.put("DATAFILE8", form.getDATAFILE8());
		params.put("MACFILE8", form.getMACFILE8());
		params.put("DATAFILE9", form.getDATAFILE9());
		params.put("MACFILE9", form.getMACFILE9());
		params.put("DATAFILE10", form.getDATAFILE10());
		params.put("MACFILE10", form.getMACFILE10());
		params.put("DATAFILE11", form.getDATAFILE11());
		params.put("MACFILE11", form.getMACFILE11());
		params.put("DATAFILE12", form.getDATAFILE12());
		params.put("MACFILE12", form.getMACFILE12());
		params.put("DATAFILE13", form.getDATAFILE13());
		params.put("MACFILE13", form.getMACFILE13());
		params.put("DATAFILE14", form.getDATAFILE14());
		params.put("MACFILE14", form.getMACFILE14());
		params.put("DATAFILE15", form.getDATAFILE15());
		params.put("MACFILE15", form.getMACFILE15());
		params.put("DATAFILE16", form.getDATAFILE16());
		params.put("MACFILE16", form.getMACFILE16());
		params.put("DATAFILE17", form.getDATAFILE17());
		params.put("MACFILE17", form.getMACFILE17());
		params.put("DATAFILE18", form.getDATAFILE18());
		params.put("MACFILE18", form.getMACFILE18());
		params.put("DATAFILE19", form.getDATAFILE19());
		params.put("MACFILE19", form.getMACFILE19());
		params.put("DATAFILE20", form.getDATAFILE20());
		params.put("MACFILE20", form.getMACFILE20());
		return params;
	}
	
	public BATDATA_UPLOAD_BO getBatdata_upload_bo() {
		return batdata_upload_bo;
	}
	public void setBatdata_upload_bo(BATDATA_UPLOAD_BO batdata_upload_bo) {
		this.batdata_upload_bo = batdata_upload_bo;
	}
	public BANK_GROUP_BO getBank_group_bo() {
		return bank_group_bo;
	}
	public void setBank_group_bo(BANK_GROUP_BO bank_group_bo) {
		this.bank_group_bo = bank_group_bo;
	}
	public CodeUtils getCodeUtils() {
		return codeUtils;
	}
	public void setCodeUtils(CodeUtils codeUtils) {
		this.codeUtils = codeUtils;
	}
	public EACH_USERLOG_BO getUserlog_bo() {
		return userlog_bo;
	}
	public void setUserlog_bo(EACH_USERLOG_BO userlog_bo) {
		this.userlog_bo = userlog_bo;
	}
}
