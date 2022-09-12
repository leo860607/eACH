package tw.org.twntch.action;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import tw.org.twntch.bo.BANK_GROUP_BO;
import tw.org.twntch.bo.CHG_MAC_BO;
import tw.org.twntch.form.Chg_Mac_Form;
import tw.org.twntch.util.StrUtils;

public class CHG_MAC_Action extends Action {
	private CHG_MAC_BO chg_mac_bo;
	private BANK_GROUP_BO bank_group_bo;
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Chg_Mac_Form chg_mac_form = (Chg_Mac_Form) form;
		String ac_key = StrUtils.isEmpty(chg_mac_form.getAc_key())?"":chg_mac_form.getAc_key();
		String target = StrUtils.isEmpty(chg_mac_form.getTarget())?"search":chg_mac_form.getTarget();
		chg_mac_form.setTarget(target);
		System.out.println("TURNON_Action is start >> " + ac_key);
		
		if(ac_key.equalsIgnoreCase("edit")){
		}
		if(ac_key.equalsIgnoreCase("update")){
		}
		if(ac_key.equalsIgnoreCase("add")){
		}
		if(StrUtils.isEmpty(ac_key)){
			//操作行代號清單
//			chg_mac_form.setBgbkIdList(chg_mac_bo.getBgbkIdList());
			chg_mac_form.setBgbkIdList(bank_group_bo.getOpbkList());
		}
		target = StrUtils.isEmpty(chg_mac_form.getTarget())?"":chg_mac_form.getTarget();
		return mapping.findForward(target);
	}
	public CHG_MAC_BO getChg_mac_bo() {
		return chg_mac_bo;
	}
	public void setChg_mac_bo(CHG_MAC_BO chg_mac_bo) {
		this.chg_mac_bo = chg_mac_bo;
	}
	public BANK_GROUP_BO getBank_group_bo() {
		return bank_group_bo;
	}
	public void setBank_group_bo(BANK_GROUP_BO bank_group_bo) {
		this.bank_group_bo = bank_group_bo;
	}
	
}
