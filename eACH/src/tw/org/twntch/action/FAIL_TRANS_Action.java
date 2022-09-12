package tw.org.twntch.action;
//FAIL_TRANS_ACTION
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import tw.org.twntch.bo.BANK_GROUP_BO;
import tw.org.twntch.bo.EACHSYSSTATUSTAB_BO;
import tw.org.twntch.bo.FAIL_TRANS_BO;
import tw.org.twntch.form.FAIL_TRANS_Form;
import tw.org.twntch.form.Login_Form;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.WebServletUtils;

public class FAIL_TRANS_Action extends Action {

	private FAIL_TRANS_BO fail_trans_bo;
	private BANK_GROUP_BO bank_group_bo;
	private EACHSYSSTATUSTAB_BO eachsysstatustab_bo;
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// TODO Auto-generated method stub
		FAIL_TRANS_Form fail_trans_form = (FAIL_TRANS_Form) form ;
		String target = StrUtils.isEmpty(fail_trans_form.getTarget())?"search":fail_trans_form.getTarget();
		String ac_key = StrUtils.isEmpty(fail_trans_form.getAc_key())?"":fail_trans_form.getAc_key();
		System.out.println("FAIL_TRANS_Action demo is start >> " + ac_key);
		Login_Form login_form = (Login_Form) WebServletUtils.getRequest().getSession().getAttribute("login_form");
		if(StrUtils.isNotEmpty(ac_key)){
			if(ac_key.equals("search")){
				
			}else if(ac_key.equals("update")){
			}else if(ac_key.equals("new")){
			}else if(ac_key.equals("add")){
			}else if(ac_key.equals("save")){
			}else if(ac_key.equals("delete")){
			}
		}
		String userType = login_form.getUserData().getUSER_TYPE();
		if(userType.equalsIgnoreCase("A")){
			fail_trans_form.setOpt_bankList(bank_group_bo.getOpbkList());
		}else if(userType.equalsIgnoreCase("B")){
			fail_trans_form.setOPBK_ID(login_form.getUserData().getUSER_COMPANY());
		}
		
		//將營業日塞到頁面的日期控制項
		String busDate = eachsysstatustab_bo.getBusinessDate();
		System.out.println("busDate="+busDate);
		fail_trans_form.setTXDT(busDate);
		fail_trans_form.setETXDT(busDate);
		System.out.println("forward to >> " + target);
		return (mapping.findForward(target));
	}
	public FAIL_TRANS_BO getFail_trans_bo() {
		return fail_trans_bo;
	}
	public void setFail_trans_bo(FAIL_TRANS_BO fail_trans_bo) {
		this.fail_trans_bo = fail_trans_bo;
	}
	public BANK_GROUP_BO getBank_group_bo() {
		return bank_group_bo;
	}
	public void setBank_group_bo(BANK_GROUP_BO bank_group_bo) {
		this.bank_group_bo = bank_group_bo;
	}
	public EACHSYSSTATUSTAB_BO getEachsysstatustab_bo() {
		return eachsysstatustab_bo;
	}
	public void setEachsysstatustab_bo(EACHSYSSTATUSTAB_BO eachsysstatustab_bo) {
		this.eachsysstatustab_bo = eachsysstatustab_bo;
	}
}
