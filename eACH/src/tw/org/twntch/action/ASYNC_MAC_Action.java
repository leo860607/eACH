package tw.org.twntch.action;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import tw.org.twntch.bo.ASYNC_MAC_BO;
import tw.org.twntch.bo.BANK_GROUP_BO;
import tw.org.twntch.form.Async_Mac_Form;
import tw.org.twntch.util.StrUtils;

public class ASYNC_MAC_Action extends Action {
	private ASYNC_MAC_BO async_mac_bo;
	private BANK_GROUP_BO bank_group_bo ;
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Async_Mac_Form async_mac_form = (Async_Mac_Form) form;
		String ac_key = StrUtils.isEmpty(async_mac_form.getAc_key())?"":async_mac_form.getAc_key();
		String target = StrUtils.isEmpty(async_mac_form.getTarget())?"search":async_mac_form.getTarget();
		async_mac_form.setTarget(target);
		System.out.println("ASYNC_MAC_Action is start >> " + ac_key);
		
		if(ac_key.equalsIgnoreCase("edit")){
		}
		if(ac_key.equalsIgnoreCase("update")){
		}
		if(ac_key.equalsIgnoreCase("add")){
		}
		if(StrUtils.isEmpty(ac_key)){
			//總行代號清單
//			async_mac_form.setOpbkIdList(async_mac_bo.getOpbkIdList());
			async_mac_form.setOpbkIdList(bank_group_bo.getOpbkList());
		}
		target = StrUtils.isEmpty(async_mac_form.getTarget())?"":async_mac_form.getTarget();
		return mapping.findForward(target);
	}
	public ASYNC_MAC_BO getAsync_mac_bo() {
		return async_mac_bo;
	}
	public void setAsync_mac_bo(ASYNC_MAC_BO async_mac_bo) {
		this.async_mac_bo = async_mac_bo;
	}
	public BANK_GROUP_BO getBank_group_bo() {
		return bank_group_bo;
	}
	public void setBank_group_bo(BANK_GROUP_BO bank_group_bo) {
		this.bank_group_bo = bank_group_bo;
	}
	
	
}
