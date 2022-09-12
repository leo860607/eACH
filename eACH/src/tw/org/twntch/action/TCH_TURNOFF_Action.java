package tw.org.twntch.action;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import tw.org.twntch.bo.AP_PAUSE_BO;
import tw.org.twntch.form.Ap_Pause_Form;
import tw.org.twntch.form.Tch_Turnoff_Form;
import tw.org.twntch.util.StrUtils;

public class TCH_TURNOFF_Action extends Action {
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Tch_Turnoff_Form tch_turnoff_form = (Tch_Turnoff_Form) form;
		String ac_key = StrUtils.isEmpty(tch_turnoff_form.getAc_key())?"":tch_turnoff_form.getAc_key();
		String target = StrUtils.isEmpty(tch_turnoff_form.getTarget())?"search":tch_turnoff_form.getTarget();
		return mapping.findForward(target);
	}
}
