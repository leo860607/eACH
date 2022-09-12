package tw.org.twntch.action;


import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import tw.org.twntch.bo.OPC_TRANS_BO;
import tw.org.twntch.bo.TXN_ERROR_CODE_BO;
import tw.org.twntch.form.Login_Form;
import tw.org.twntch.form.Opc_Trans_Form;
import tw.org.twntch.util.CodeUtils;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.WebServletUtils;

public class OPC_TRANS_Action extends Action {
	private OPC_TRANS_BO opc_trans_bo;
	private TXN_ERROR_CODE_BO txn_err_code_bo;
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Opc_Trans_Form opc_trans_form = (Opc_Trans_Form) form;
		String ac_key = StrUtils.isEmpty(CodeUtils.escape(opc_trans_form.getAc_key()))?"":opc_trans_form.getAc_key();
		String target = StrUtils.isEmpty(CodeUtils.escape(opc_trans_form.getTarget()))?"search":opc_trans_form.getTarget();
		opc_trans_form.setTarget(target);
		System.out.println("OPC_TRANS_Action is start >> " + ac_key);
		Login_Form login_form = (Login_Form) WebServletUtils.getRequest().getSession().getAttribute("login_form");
		
		System.out.println("PageForSort -> " + CodeUtils.escape(opc_trans_form.getPageForSort()));
		
		if(ac_key.equalsIgnoreCase("edit")){
			
//			opc_trans_form.setDetailData(opc_trans_bo.getDetail(BeanUtils.describe(opc_trans_form)));
			Map<String,String> edit_params =  JSONUtils.json2map(CodeUtils.escape(opc_trans_form.getEdit_params()));
			opc_trans_form.setDetailData(opc_trans_bo.getDetail(edit_params));
			opc_trans_form.setPageType(opc_trans_bo.getPageType(edit_params.get("PCODE")));
		}
		if(ac_key.equalsIgnoreCase("update")){
		}
		if(ac_key.equalsIgnoreCase("add")){
		}
		if(StrUtils.isEmpty(ac_key) || ac_key.equalsIgnoreCase("back")){
			System.out.println("ac_key>>"+ac_key);
			System.out.println(""+opc_trans_form.getSerchStrs());
			if(StrUtils.isNotEmpty(ac_key)){
				BeanUtils.populate(opc_trans_form, JSONUtils.json2map(opc_trans_form.getSerchStrs()) ); ;
			}
			//銀行端
			if(login_form.getUserData().getUSER_TYPE().equalsIgnoreCase("B")){
				opc_trans_form.setSENDERBANK(login_form.getUserData().getUSER_COMPANY());
				opc_trans_form.setRECEIVERBANK(login_form.getUserData().getUSER_COMPANY());
			}
			System.out.println("getPCODE>>"+opc_trans_form.getPCODE());
			//總行代號清單
			//opc_trans_form.setOpbkIdList(opc_trans_bo.getBgbkIdList());
			opc_trans_form.setPcodeList(opc_trans_bo.getOpcTxnCodeList());
			//回覆結果代號清單
			opc_trans_form.setTxnErrorCodeList(txn_err_code_bo.getIdList());
			
			Calendar cal = Calendar.getInstance();
			Date today = cal.getTime();
			opc_trans_form.setOPCCUR_TXDATE(DateTimeUtils.getDateShort2(today));
			opc_trans_form.setOPCTXDATE(DateTimeUtils.getDateShort2(today));
			cal.add(Calendar.DAY_OF_YEAR, -1);
			Date yesterday = cal.getTime();
			opc_trans_form.setOPCTXDATE_PRE(DateTimeUtils.getDateShort2(yesterday));
			opc_trans_form.setTarget("search");
		}
		target = StrUtils.isEmpty(opc_trans_form.getTarget())?"":opc_trans_form.getTarget();
		System.out.println("OPC ACTION TARGET >> " + target);
		return mapping.findForward(target);
	}
	public OPC_TRANS_BO getOpc_trans_bo() {
		return opc_trans_bo;
	}
	public void setOpc_trans_bo(OPC_TRANS_BO opc_trans_bo) {
		this.opc_trans_bo = opc_trans_bo;
	}
	public TXN_ERROR_CODE_BO getTxn_err_code_bo() {
		return txn_err_code_bo;
	}
	public void setTxn_err_code_bo(TXN_ERROR_CODE_BO txn_err_code_bo) {
		this.txn_err_code_bo = txn_err_code_bo;
	}
	
}
