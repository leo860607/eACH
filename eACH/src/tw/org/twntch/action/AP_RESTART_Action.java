package tw.org.twntch.action;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import tw.org.twntch.bo.AP_RESTART_BO;
import tw.org.twntch.bo.BANK_GROUP_BO;
import tw.org.twntch.form.Ap_Restart_Form;
import tw.org.twntch.util.StrUtils;

public class AP_RESTART_Action extends Action {
	private AP_RESTART_BO ap_restart_bo;
	private BANK_GROUP_BO bank_group_bo;
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Ap_Restart_Form ap_restart_form = (Ap_Restart_Form) form;
		String ac_key = StrUtils.isEmpty(ap_restart_form.getAc_key())?"":ap_restart_form.getAc_key();
		String target = StrUtils.isEmpty(ap_restart_form.getTarget())?"search":ap_restart_form.getTarget();
		ap_restart_form.setTarget(target);
		System.out.println("AP_RESTART_Action is start >> " + ac_key);
		
		if(ac_key.equalsIgnoreCase("edit")){
		}
		if(ac_key.equalsIgnoreCase("update")){
		}
		if(ac_key.equalsIgnoreCase("add")){
		}
		if(StrUtils.isEmpty(ac_key)){
			//操作行代號清單
//			ap_restart_form.setBgbkIdList(ap_restart_bo.getBgbkIdList());
			ap_restart_form.setBgbkIdList(bank_group_bo.getOpbkList());
		}
		target = StrUtils.isEmpty(ap_restart_form.getTarget())?"":ap_restart_form.getTarget();
		return mapping.findForward(target);
	}
	public AP_RESTART_BO getAp_restart_bo() {
		return ap_restart_bo;
	}
	public void setAp_restart_bo(AP_RESTART_BO ap_restart_bo) {
		this.ap_restart_bo = ap_restart_bo;
	}
	public BANK_GROUP_BO getBank_group_bo() {
		return bank_group_bo;
	}
	public void setBank_group_bo(BANK_GROUP_BO bank_group_bo) {
		this.bank_group_bo = bank_group_bo;
	}
	
}
