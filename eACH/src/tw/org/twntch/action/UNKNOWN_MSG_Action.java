package tw.org.twntch.action;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import tw.org.twntch.bo.EACH_USERLOG_BO;
import tw.org.twntch.bo.UNKNOWN_MSG_BO;
import tw.org.twntch.form.Login_Form;
import tw.org.twntch.form.Unknown_Msg_Form;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.WebServletUtils;

public class UNKNOWN_MSG_Action extends Action {
	private UNKNOWN_MSG_BO unknown_msg_bo;
	
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Unknown_Msg_Form unknown_msg_form = (Unknown_Msg_Form) form;
		String ac_key = StrUtils.isEmpty(unknown_msg_form.getAc_key())?"":unknown_msg_form.getAc_key();
		String target = StrUtils.isEmpty(unknown_msg_form.getTarget())?"search":unknown_msg_form.getTarget();
		unknown_msg_form.setTarget(target);
		System.out.println("UNKNOWN_MSG_Action is start >> " + ac_key);
		Login_Form login_form = (Login_Form) WebServletUtils.getRequest().getSession().getAttribute("login_form");
		if(ac_key.equalsIgnoreCase("edit")){
		}
		if(ac_key.equalsIgnoreCase("update")){
		}
		if(ac_key.equalsIgnoreCase("add")){
		}
		if(StrUtils.isEmpty(ac_key)){
			//銀行端
			if(login_form.getUserData().getUSER_TYPE().equalsIgnoreCase("B")){
				unknown_msg_form.setSENDERBANK(login_form.getUserData().getUSER_COMPANY());
				unknown_msg_form.setRECEIVERBANK(login_form.getUserData().getUSER_COMPANY());
			}
			
			//總行代號清單
			//unknown_msg_form.setOpbkIdList(unknown_msg_bo.getBgbkIdList());
		}
		target = StrUtils.isEmpty(unknown_msg_form.getTarget())?"":unknown_msg_form.getTarget();
		return mapping.findForward(target);
	}
	public UNKNOWN_MSG_BO getUnknown_msg_bo() {
		return unknown_msg_bo;
	}
	public void setUnknown_msg_bo(UNKNOWN_MSG_BO unknown_msg_bo) {
		this.unknown_msg_bo = unknown_msg_bo;
	}
}
