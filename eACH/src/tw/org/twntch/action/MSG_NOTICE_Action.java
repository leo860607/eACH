package tw.org.twntch.action;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import tw.org.twntch.bo.BANK_GROUP_BO;
import tw.org.twntch.bo.MSG_NOTICE_BO;
import tw.org.twntch.form.Msg_Notice_Form;
import tw.org.twntch.util.StrUtils;

public class MSG_NOTICE_Action extends Action {
	private MSG_NOTICE_BO msg_notice_bo;
	private BANK_GROUP_BO bank_group_bo;
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Msg_Notice_Form msg_notice_form = (Msg_Notice_Form) form;
		String ac_key = StrUtils.isEmpty(msg_notice_form.getAc_key())?"":msg_notice_form.getAc_key();
		String target = StrUtils.isEmpty(msg_notice_form.getTarget())?"search":msg_notice_form.getTarget();
		msg_notice_form.setTarget(target);
		System.out.println("MSG_NOTICE_Action is start >> " + ac_key);
		
		if(ac_key.equalsIgnoreCase("edit")){
		}
		if(ac_key.equalsIgnoreCase("update")){
		}
		if(ac_key.equalsIgnoreCase("add")){
		}
		if(StrUtils.isEmpty(ac_key)){
			//操作行代號清單
			msg_notice_form.setBgbkIdList(msg_notice_bo.getBgbkIdList());
//			msg_notice_form.setBgbkIdList(bank_group_bo.getOpbkList());
		}
		target = StrUtils.isEmpty(msg_notice_form.getTarget())?"":msg_notice_form.getTarget();
		return mapping.findForward(target);
	}
	public MSG_NOTICE_BO getMsg_notice_bo() {
		return msg_notice_bo;
	}
	public void setMsg_notice_bo(MSG_NOTICE_BO msg_notice_bo) {
		this.msg_notice_bo = msg_notice_bo;
	}
	public BANK_GROUP_BO getBank_group_bo() {
		return bank_group_bo;
	}
	public void setBank_group_bo(BANK_GROUP_BO bank_group_bo) {
		this.bank_group_bo = bank_group_bo;
	}
	
}
