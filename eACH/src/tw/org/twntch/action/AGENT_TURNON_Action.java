package tw.org.twntch.action;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import tw.org.twntch.bo.AGENT_TURNON_BO;
import tw.org.twntch.form.Agent_Turnon_Form;
import tw.org.twntch.util.StrUtils;

public class AGENT_TURNON_Action extends Action {
	private AGENT_TURNON_BO agent_turnon_bo;
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Agent_Turnon_Form agent_turnon_form = (Agent_Turnon_Form) form;
		String ac_key = StrUtils.isEmpty(agent_turnon_form.getAc_key())?"":agent_turnon_form.getAc_key();
		String target = StrUtils.isEmpty(agent_turnon_form.getTarget())?"search":agent_turnon_form.getTarget();
		agent_turnon_form.setTarget(target);
		System.out.println("AGENT_TURNON_Action is start >> " + ac_key);
		
		if(ac_key.equalsIgnoreCase("edit")){
		}
		if(ac_key.equalsIgnoreCase("update")){
		}
		if(ac_key.equalsIgnoreCase("add")){
		}
		if(StrUtils.isEmpty(ac_key)){
			//代理業者清單
			agent_turnon_form.setAbbrIdList(agent_turnon_bo.getAgent_send_profile_bo().getAll_Snd_Com_List());
		}
		target = StrUtils.isEmpty(agent_turnon_form.getTarget())?"":agent_turnon_form.getTarget();
		return mapping.findForward(target);
	}
	public AGENT_TURNON_BO getAgent_turnon_bo() {
		return agent_turnon_bo;
	}
	public void setAgent_turnon_bo(AGENT_TURNON_BO agent_turnon_bo) {
		this.agent_turnon_bo = agent_turnon_bo;
	}
	
}
