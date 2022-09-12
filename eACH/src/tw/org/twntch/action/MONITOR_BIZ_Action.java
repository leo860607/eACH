package tw.org.twntch.action;


import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import tw.org.twntch.bo.EACHSYSSTATUSTAB_BO;
import tw.org.twntch.bo.MONITOR_BIZ_BO;
import tw.org.twntch.form.Login_Form;
import tw.org.twntch.form.Monitor_Biz_Form;
import tw.org.twntch.util.BeanUtils;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.StrUtils;

public class MONITOR_BIZ_Action extends Action {
	private MONITOR_BIZ_BO monitor_biz_bo;
	private EACHSYSSTATUSTAB_BO eachsysstatustab_bo;
	
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Monitor_Biz_Form monitor_biz_form = (Monitor_Biz_Form) form;
		String ac_key = StrUtils.isEmpty(monitor_biz_form.getAc_key())?"":monitor_biz_form.getAc_key();
		String target = StrUtils.isEmpty(monitor_biz_form.getTarget())?"search":monitor_biz_form.getTarget();
		monitor_biz_form.setTarget(target);
		Login_Form login_form = (Login_Form) request.getSession().getAttribute("login_form");
		
		System.out.println("MONITOR_BIZ_Action is start >> " + ac_key);
		
		if(ac_key.equalsIgnoreCase("edit")){
		}
		if(ac_key.equalsIgnoreCase("update")){
		}
		if(ac_key.equalsIgnoreCase("add")){
		}
		if(StrUtils.isEmpty(ac_key)){
			String data = monitor_biz_bo.getBusinessDateAndClrphase(null);
			Map<String, String> mapData = JSONUtils.json2map(data);
			
			monitor_biz_form.setCUR_BIZDATE(mapData.get("bizdate"));
			monitor_biz_form.setCUR_CLEARINGPHASE(mapData.get("clearingphase"));
			monitor_biz_form.setPRE_BIZDATE(mapData.get("pre_bizdate"));
			monitor_biz_form.setPRE_CLEARINGPHASE(mapData.get("pre_clearingphase"));
		}
		target = StrUtils.isEmpty(monitor_biz_form.getTarget())?"":monitor_biz_form.getTarget();
		return mapping.findForward(target);
	}

	public MONITOR_BIZ_BO getMonitor_biz_bo() {
		return monitor_biz_bo;
	}

	public void setMonitor_biz_bo(MONITOR_BIZ_BO monitor_biz_bo) {
		this.monitor_biz_bo = monitor_biz_bo;
	}

	public EACHSYSSTATUSTAB_BO getEachsysstatustab_bo() {
		return eachsysstatustab_bo;
	}

	public void setEachsysstatustab_bo(EACHSYSSTATUSTAB_BO eachsysstatustab_bo) {
		this.eachsysstatustab_bo = eachsysstatustab_bo;
	}
	
}
