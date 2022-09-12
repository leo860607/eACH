package tw.org.twntch.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import tw.org.twntch.bo.EACH_FILEDOWNLOAD_BO;
import tw.org.twntch.bo.EACH_USERLOG_BO;
import tw.org.twntch.form.Each_Filedownload_Form;
import tw.org.twntch.form.Login_Form;
import tw.org.twntch.util.StrUtils;

public class EACH_FILEDOWNLOAD_Action extends Action{
	private EACH_FILEDOWNLOAD_BO each_filedownload_bo;
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private EACH_USERLOG_BO userlog_bo;//寫操作軌跡記錄
	
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
		Each_Filedownload_Form each_filedownload_form = (Each_Filedownload_Form) form;
		String ac_key = StrUtils.isEmpty(each_filedownload_form.getAc_key())?"":each_filedownload_form.getAc_key();
		String target = StrUtils.isEmpty(each_filedownload_form.getTarget())?"search":each_filedownload_form.getTarget();
		each_filedownload_form.setTarget(target);
		Login_Form login_form = (Login_Form) request.getSession().getAttribute("login_form");
		logger.debug("ac_key=" + ac_key);
		logger.debug("target=" + target);
		
		//下載檔案
		if(ac_key.equalsIgnoreCase("download")){
			//將下載檔案的名稱，檔案大小記進去
			Map<String,Object> pkMap = new HashMap<String,Object>();
			pkMap.put("serchStrs",each_filedownload_form.getSerchStrs());
			//如果有錯誤要將訊息放進去
			Map<String,Object> msgMap = new HashMap<String,Object>();
			
			String message = each_filedownload_bo.download(each_filedownload_form,login_form);
			//如果沒有訊息，表示成功
			if("".equals(message)){
				//寫操作軌跡記錄(成功)
				userlog_bo.writeLog("F",null,null,pkMap);
				
				return null;
			}
			//有問題
			else{
				each_filedownload_form.setMsg(message);
				
				//寫操作軌跡記錄(失敗)
				msgMap.put("msg",each_filedownload_form.getMsg());
				userlog_bo.writeFailLog("F",msgMap,null,null,pkMap);
			}
		}
		
		target = StrUtils.isEmpty(each_filedownload_form.getTarget())?"":each_filedownload_form.getTarget();
		
		return mapping.findForward(target);
	}

	public EACH_FILEDOWNLOAD_BO getEach_filedownload_bo() {
		return each_filedownload_bo;
	}
	public void setEach_filedownload_bo(EACH_FILEDOWNLOAD_BO each_filedownload_bo) {
		this.each_filedownload_bo = each_filedownload_bo;
	}
	public EACH_USERLOG_BO getUserlog_bo() {
		return userlog_bo;
	}
	public void setUserlog_bo(EACH_USERLOG_BO userlog_bo) {
		this.userlog_bo = userlog_bo;
	}
}
