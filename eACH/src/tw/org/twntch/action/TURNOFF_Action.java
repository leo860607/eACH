package tw.org.twntch.action;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import tw.org.twntch.bo.BANK_GROUP_BO;
import tw.org.twntch.bo.TURNOFF_BO;
import tw.org.twntch.form.Turnoff_Form;
import tw.org.twntch.util.StrUtils;

public class TURNOFF_Action extends Action {
	private TURNOFF_BO turnoff_bo;
	private BANK_GROUP_BO bank_group_bo;
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Turnoff_Form turnoff_form = (Turnoff_Form) form;
		String ac_key = StrUtils.isEmpty(turnoff_form.getAc_key())?"":turnoff_form.getAc_key();
		String target = StrUtils.isEmpty(turnoff_form.getTarget())?"search":turnoff_form.getTarget();
		turnoff_form.setTarget(target);
		System.out.println("TURNOFF_Action is start >> " + ac_key);
		
		if(ac_key.equalsIgnoreCase("edit")){
		}
		if(ac_key.equalsIgnoreCase("update")){
		}
		if(ac_key.equalsIgnoreCase("add")){
		}
		if(StrUtils.isEmpty(ac_key)){
			//操作行代號清單
			turnoff_form.setBgbkIdList(turnoff_bo.getBgbkIdList());
//			turnoff_form.setBgbkIdList(bank_group_bo.getOpbkList());
		}
		target = StrUtils.isEmpty(turnoff_form.getTarget())?"":turnoff_form.getTarget();
		return mapping.findForward(target);
	}
	public TURNOFF_BO getTurnoff_bo() {
		return turnoff_bo;
	}
	public void setTurnoff_bo(TURNOFF_BO turnoff_bo) {
		this.turnoff_bo = turnoff_bo;
	}
	public BANK_GROUP_BO getBank_group_bo() {
		return bank_group_bo;
	}
	public void setBank_group_bo(BANK_GROUP_BO bank_group_bo) {
		this.bank_group_bo = bank_group_bo;
	}
	
}
