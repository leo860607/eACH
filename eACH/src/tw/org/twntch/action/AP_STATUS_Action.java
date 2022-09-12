package tw.org.twntch.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import tw.org.twntch.bean.BANK_STATUS;
import tw.org.twntch.bo.AP_STATUS_BO;
import tw.org.twntch.form.Ap_Status_Form;
import tw.org.twntch.util.StrUtils;

public class AP_STATUS_Action extends Action{

	private AP_STATUS_BO ap_status_bo;
	
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Ap_Status_Form ap_status_form = (Ap_Status_Form) form;
		String ac_key = StrUtils.isEmpty(ap_status_form.getAc_key())?"":ap_status_form.getAc_key();
		String target = StrUtils.isEmpty(ap_status_form.getTarget())?"search":ap_status_form.getTarget();
		ap_status_form.setTarget(target);
		System.out.println("AP_STATUS_Action is start >> " + ac_key);
		
		if(ac_key.equalsIgnoreCase("edit")){
			List<BANK_STATUS>list = ap_status_bo.getData(ap_status_form.getBGBK_ID(), ap_status_form.getAPID());
			for(BANK_STATUS po : list){
				ap_status_form.setMBAPSTATUS(po.getMBAPSTATUS());
				ap_status_form.setMBSYSSTATUS(po.getMBSYSSTATUS());
			}
		}
		if(ac_key.equalsIgnoreCase("update")){
			BeanUtils.populate(ap_status_form, ap_status_bo.save(BeanUtils.describe(ap_status_form)));
		}
		if(ac_key.equalsIgnoreCase("add")){
		}
		if(StrUtils.isEmpty(ac_key)){
		}
		target = StrUtils.isEmpty(ap_status_form.getTarget())?"":ap_status_form.getTarget();
		return mapping.findForward(target);
	}

	public AP_STATUS_BO getAp_status_bo() {
		return ap_status_bo;
	}

	public void setAp_status_bo(AP_STATUS_BO ap_status_bo) {
		this.ap_status_bo = ap_status_bo;
	}
}
