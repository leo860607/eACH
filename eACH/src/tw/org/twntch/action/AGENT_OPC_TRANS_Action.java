package tw.org.twntch.action;


import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import tw.org.twntch.aop.RPT_Aop;
import tw.org.twntch.bo.AGENT_OPC_TRANS_BO;
import tw.org.twntch.bo.OPC_TRANS_BO;
import tw.org.twntch.bo.TXN_ERROR_CODE_BO;
import tw.org.twntch.form.Login_Form;
import tw.org.twntch.form.Opc_Trans_Form;
import tw.org.twntch.form.Rpt_Agent_Form;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.WebServletUtils;

public class AGENT_OPC_TRANS_Action extends Action {
	private AGENT_OPC_TRANS_BO agent_opc_trans_bo;
	private TXN_ERROR_CODE_BO txn_err_code_bo;
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Rpt_Agent_Form rpt_agent_form = (Rpt_Agent_Form) form;
		String ac_key = StrUtils.isEmpty(rpt_agent_form.getAc_key())?"":rpt_agent_form.getAc_key();
		String target = StrUtils.isEmpty(rpt_agent_form.getTarget())?"search":rpt_agent_form.getTarget();
		rpt_agent_form.setTarget(target);
		System.out.println("AGENT_OPC_TRANS_Action is start >> " + ac_key);
		Login_Form login_form = (Login_Form) WebServletUtils.getRequest().getSession().getAttribute("login_form");
		
		System.out.println("PageForSort -> " + rpt_agent_form.getPageForSort());
		
		if(ac_key.equalsIgnoreCase("edit")){
			Map<String,String> edit_params =  JSONUtils.json2map(rpt_agent_form.getEdit_params());
			rpt_agent_form.setDetailData(agent_opc_trans_bo.getDetail(edit_params));
		}
		if(StrUtils.isEmpty(ac_key) || ac_key.equalsIgnoreCase("back")){
			System.out.println("ac_key>>"+ac_key);
			System.out.println(""+rpt_agent_form.getSerchStrs());
			if(StrUtils.isNotEmpty(ac_key)){
				BeanUtils.populate(rpt_agent_form, JSONUtils.json2map(rpt_agent_form.getSerchStrs()) ); ;
			}
			//非票交端
			if(!login_form.getUserData().getUSER_TYPE().equalsIgnoreCase("A")){
//				rpt_agent_form.setSENDERBANK(login_form.getUserData().getUSER_COMPANY());
//				rpt_agent_form.setRECEIVERBANK(login_form.getUserData().getUSER_COMPANY());
			}
			//TODO 回覆結果代號清單 之後要過濾出屬代理業者的回應代碼
			rpt_agent_form.setTxnErrorCodeList(txn_err_code_bo.getIdList());
		}
		target = StrUtils.isEmpty(rpt_agent_form.getTarget())?"":rpt_agent_form.getTarget();
		return mapping.findForward(target);
	}
	public TXN_ERROR_CODE_BO getTxn_err_code_bo() {
		return txn_err_code_bo;
	}
	public void setTxn_err_code_bo(TXN_ERROR_CODE_BO txn_err_code_bo) {
		this.txn_err_code_bo = txn_err_code_bo;
	}
	public AGENT_OPC_TRANS_BO getAgent_opc_trans_bo() {
		return agent_opc_trans_bo;
	}
	public void setAgent_opc_trans_bo(AGENT_OPC_TRANS_BO agent_opc_trans_bo) {
		this.agent_opc_trans_bo = agent_opc_trans_bo;
	}
	
	
	
}
