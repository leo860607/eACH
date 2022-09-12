package tw.org.twntch.action;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import tw.org.twntch.bo.OPC_TRANS_BO;
import tw.org.twntch.bo.SETTLEMENT_MSG_BO;
import tw.org.twntch.form.Login_Form;
import tw.org.twntch.form.Opc_Trans_Form;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.WebServletUtils;

public class SETTLEMENT_MSG_Action extends Action {
	private SETTLEMENT_MSG_BO settlement_msg_bo;
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Opc_Trans_Form opc_trans_form = (Opc_Trans_Form) form;
		String ac_key = StrUtils.isEmpty(opc_trans_form.getAc_key())?"":opc_trans_form.getAc_key();
		String target = StrUtils.isEmpty(opc_trans_form.getTarget())?"search":opc_trans_form.getTarget();
		opc_trans_form.setTarget(target);
		System.out.println("OPC_TRANS_Action is start >> " + ac_key);
		Login_Form login_form = (Login_Form) WebServletUtils.getRequest().getSession().getAttribute("login_form");
		
		System.out.println("PageForSort -> " + opc_trans_form.getPageForSort());
		
		if(ac_key.equalsIgnoreCase("edit")){
			opc_trans_form.setDetailData(settlement_msg_bo.getDetail(BeanUtils.describe(opc_trans_form)));
			opc_trans_form.setPageType(settlement_msg_bo.getPageType(opc_trans_form.getPCODE()));
		}
		if(ac_key.equalsIgnoreCase("update")){
		}
		if(ac_key.equalsIgnoreCase("add")){
		}
		if(StrUtils.isEmpty(ac_key) || ac_key.equalsIgnoreCase("back")){
			//銀行端
			if(login_form.getUserData().getUSER_TYPE().equalsIgnoreCase("B")){
				opc_trans_form.setSENDERBANK(login_form.getUserData().getUSER_COMPANY());
				opc_trans_form.setRECEIVERBANK(login_form.getUserData().getUSER_COMPANY());
			}
			
			//總行代號清單
			//opc_trans_form.setOpbkIdList(opc_trans_bo.getBgbkIdList());
			opc_trans_form.setPcodeList(settlement_msg_bo.getOpcTxnCodeList());
		}
		target = StrUtils.isEmpty(opc_trans_form.getTarget())?"":opc_trans_form.getTarget();
		return mapping.findForward(target);
	}
	public SETTLEMENT_MSG_BO getSettlement_msg_bo() {
		return settlement_msg_bo;
	}
	public void setSettlement_msg_bo(SETTLEMENT_MSG_BO settlement_msg_bo) {
		this.settlement_msg_bo = settlement_msg_bo;
	}

	
	
}
