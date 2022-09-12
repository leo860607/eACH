package tw.org.twntch.action;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import tw.org.twntch.form.Tch_Turnon_Form;
import tw.org.twntch.util.StrUtils;

public class TCH_TURNON_Action extends Action {
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Tch_Turnon_Form tch_turnon_form = (Tch_Turnon_Form) form;
		String ac_key = StrUtils.isEmpty(tch_turnon_form.getAc_key())?"":tch_turnon_form.getAc_key();
		String target = StrUtils.isEmpty(tch_turnon_form.getTarget())?"search":tch_turnon_form.getTarget();
//		ap_pause_form.setTarget(target);
//		System.out.println("AP_PAUSE_Action is start >> " + ac_key);
//		
//		if(ac_key.equalsIgnoreCase("edit")){
//		}
//		if(ac_key.equalsIgnoreCase("update")){
//		}
//		if(ac_key.equalsIgnoreCase("add")){
//		}
//		if(StrUtils.isEmpty(ac_key)){
//			//總行代號清單
//			ap_pause_form.setBgbkIdList(ap_pause_bo.getBgbkIdList());
//		}
//		target = StrUtils.isEmpty(ap_pause_form.getTarget())?"":ap_pause_form.getTarget();
		return mapping.findForward(target);
	}
}
