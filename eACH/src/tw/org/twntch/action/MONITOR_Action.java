package tw.org.twntch.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import tw.org.twntch.bo.MONITOR_BO;
import tw.org.twntch.form.Monitor_Form;
import tw.org.twntch.util.StrUtils;

public class MONITOR_Action extends Action{

	private MONITOR_BO monitor_bo;
	
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Monitor_Form monitor_form = (Monitor_Form) form;
		String ac_key = StrUtils.isEmpty(monitor_form.getAc_key())?"":monitor_form.getAc_key();
		String target = StrUtils.isEmpty(monitor_form.getTarget())?"search":monitor_form.getTarget();
		monitor_form.setTarget(target);
		System.out.println("MONITOR_Action is start >> " + ac_key);
		
		if(ac_key.equalsIgnoreCase("edit")){
		}
		if(ac_key.equalsIgnoreCase("update")){
		}
		if(ac_key.equalsIgnoreCase("add")){
		}
		if(ac_key.equalsIgnoreCase("search_pending")){
			monitor_form.setTarget(ac_key);
		}
		if(StrUtils.isEmpty(ac_key)){
		}
		target = StrUtils.isEmpty(monitor_form.getTarget())?"":monitor_form.getTarget();
		return mapping.findForward(target);
	}

	public MONITOR_BO getMonitor_bo() {
		return monitor_bo;
	}

	public void setMonitor_bo(MONITOR_BO monitor_bo) {
		this.monitor_bo = monitor_bo;
	}

}
