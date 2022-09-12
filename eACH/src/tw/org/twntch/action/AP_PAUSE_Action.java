package tw.org.twntch.action;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import tw.org.twntch.bo.AP_PAUSE_BO;
import tw.org.twntch.bo.BANK_GROUP_BO;
import tw.org.twntch.form.Ap_Pause_Form;
import tw.org.twntch.util.StrUtils;

public class AP_PAUSE_Action extends Action {
	private AP_PAUSE_BO ap_pause_bo;
	private BANK_GROUP_BO bank_group_bo;
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Ap_Pause_Form ap_pause_form = (Ap_Pause_Form) form;
		String ac_key = StrUtils.isEmpty(ap_pause_form.getAc_key())?"":ap_pause_form.getAc_key();
		String target = StrUtils.isEmpty(ap_pause_form.getTarget())?"search":ap_pause_form.getTarget();
		ap_pause_form.setTarget(target);
		System.out.println("AP_PAUSE_Action is start >> " + ac_key);
		
		if(ac_key.equalsIgnoreCase("edit")){
		}
		if(ac_key.equalsIgnoreCase("update")){
		}
		if(ac_key.equalsIgnoreCase("add")){
		}
		if(StrUtils.isEmpty(ac_key)){
			//操作行代號清單
//			ap_pause_form.setBgbkIdList(ap_pause_bo.getBgbkIdList());
			ap_pause_form.setBgbkIdList(bank_group_bo.getOpbkList());
		}
		target = StrUtils.isEmpty(ap_pause_form.getTarget())?"":ap_pause_form.getTarget();
		return mapping.findForward(target);
	}
	public AP_PAUSE_BO getAp_pause_bo() {
		return ap_pause_bo;
	}
	public void setAp_pause_bo(AP_PAUSE_BO ap_pause_bo) {
		this.ap_pause_bo = ap_pause_bo;
	}
	public BANK_GROUP_BO getBank_group_bo() {
		return bank_group_bo;
	}
	public void setBank_group_bo(BANK_GROUP_BO bank_group_bo) {
		this.bank_group_bo = bank_group_bo;
	}
	
}
