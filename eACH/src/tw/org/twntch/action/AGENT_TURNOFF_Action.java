package tw.org.twntch.action;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import tw.org.twntch.bo.AGENT_SEND_PROFILE_BO;
import tw.org.twntch.bo.AGENT_TURNOFF_BO;
import tw.org.twntch.form.Agent_Turnoff_Form;
import tw.org.twntch.util.StrUtils;

public class AGENT_TURNOFF_Action extends Action {
	private AGENT_TURNOFF_BO agent_turnoff_bo;
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Agent_Turnoff_Form agent_turnoff_form = (Agent_Turnoff_Form) form;
		String ac_key = StrUtils.isEmpty(agent_turnoff_form.getAc_key())?"":agent_turnoff_form.getAc_key();
		String target = StrUtils.isEmpty(agent_turnoff_form.getTarget())?"search":agent_turnoff_form.getTarget();
		agent_turnoff_form.setTarget(target);
		System.out.println("AGENT_TURNOFF_Action is start >> " + ac_key);
		
		if(ac_key.equalsIgnoreCase("edit")){
		}
		if(ac_key.equalsIgnoreCase("update")){
		}
		if(ac_key.equalsIgnoreCase("add")){
		}
		if(StrUtils.isEmpty(ac_key)){
			//代理業者清單
			agent_turnoff_form.setAbbrIdList(agent_turnoff_bo.getAgent_send_profile_bo().getAll_Snd_Com_List());
		}
		target = StrUtils.isEmpty(agent_turnoff_form.getTarget())?"":agent_turnoff_form.getTarget();
		return mapping.findForward(target);
	}
	public AGENT_TURNOFF_BO getAgent_turnoff_bo() {
		return agent_turnoff_bo;
	}
	public void setAgent_turnoff_bo(AGENT_TURNOFF_BO agent_turnoff_bo) {
		this.agent_turnoff_bo = agent_turnoff_bo;
	}

}
