package tw.org.twntch.action;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import tw.org.twntch.form.Tch_Ap_Restart_Form;
import tw.org.twntch.util.StrUtils;

public class TCH_AP_RESTART_Action extends Action {
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Tch_Ap_Restart_Form tch_ap_restart_form = (Tch_Ap_Restart_Form) form;
		String ac_key = StrUtils.isEmpty(tch_ap_restart_form.getAc_key())?"":tch_ap_restart_form.getAc_key();
		String target = StrUtils.isEmpty(tch_ap_restart_form.getTarget())?"search":tch_ap_restart_form.getTarget();
		return mapping.findForward(target);
	}
}
