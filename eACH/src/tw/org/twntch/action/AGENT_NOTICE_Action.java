package tw.org.twntch.action;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import tw.org.twntch.bo.AGENT_NOTICE_BO;
import tw.org.twntch.bo.BANK_GROUP_BO;
import tw.org.twntch.form.Agent_Notice_Form;
import tw.org.twntch.form.Msg_Notice_Form;
import tw.org.twntch.util.StrUtils;

public class AGENT_NOTICE_Action extends Action {
	private AGENT_NOTICE_BO agent_notice_bo;
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Agent_Notice_Form agent_notice_form = (Agent_Notice_Form) form;
		String ac_key = StrUtils.isEmpty(agent_notice_form.getAc_key())?"":agent_notice_form.getAc_key();
		String target = StrUtils.isEmpty(agent_notice_form.getTarget())?"search":agent_notice_form.getTarget();
		agent_notice_form.setTarget(target);
		System.out.println("AGENT_NOTICE_Action is start >> " + ac_key);
		
		if(ac_key.equalsIgnoreCase("edit")){
		}
		if(ac_key.equalsIgnoreCase("update")){
		}
		if(ac_key.equalsIgnoreCase("add")){
		}
		if(StrUtils.isEmpty(ac_key)){
			//代理業者清單
			agent_notice_form.setAbbrIdList(agent_notice_bo.getAgent_send_profile_bo().getAll_Snd_Com_List());
		}
		target = StrUtils.isEmpty(agent_notice_form.getTarget())?"":agent_notice_form.getTarget();
		return mapping.findForward(target);
	}
	public AGENT_NOTICE_BO getAgent_notice_bo() {
		return agent_notice_bo;
	}
	public void setAgent_notice_bo(AGENT_NOTICE_BO agent_notice_bo) {
		this.agent_notice_bo = agent_notice_bo;
	}

	
}
