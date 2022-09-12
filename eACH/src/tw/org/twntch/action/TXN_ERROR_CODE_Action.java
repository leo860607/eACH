package tw.org.twntch.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import tw.org.twntch.bo.TXN_ERROR_CODE_BO;
import tw.org.twntch.form.Login_Form;
import tw.org.twntch.form.Txn_Error_Code_Form;
import tw.org.twntch.po.TXN_ERROR_CODE;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.WebServletUtils;

public class TXN_ERROR_CODE_Action extends Action {
private TXN_ERROR_CODE_BO txn_err_code_bo;



@Override
public ActionForward execute(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
	System.out.println("TXN_ERROR_CODE_Action is start");
	String target = "";
	Login_Form login_form = (Login_Form) WebServletUtils.getRequest().getSession().getAttribute("login_form");
	Txn_Error_Code_Form txn_Error_Code_Form = (Txn_Error_Code_Form) form;
	String ac_key = StrUtils.isEmpty(txn_Error_Code_Form.getAc_key())?"":txn_Error_Code_Form.getAc_key();
	target = StrUtils.isEmpty(txn_Error_Code_Form.getTarget())?"search":txn_Error_Code_Form.getTarget();
	txn_Error_Code_Form.setTarget(target);
	List<TXN_ERROR_CODE> list = null;
	if(ac_key.equalsIgnoreCase("search")|| ac_key.equalsIgnoreCase("back") ){
		System.out.println("SerchStrs>>"+txn_Error_Code_Form.getSerchStrs());
		BeanUtils.populate(txn_Error_Code_Form, JSONUtils.json2map(txn_Error_Code_Form.getSerchStrs()));
//		list = txn_err_code_bo.search(txn_Error_Code_Form.getERROR_ID());
//		txn_Error_Code_Form.setJsonList(JSONUtils.toJson(list));
//		txn_Error_Code_Form.setScaseary(list);
	}
	if(ac_key.equalsIgnoreCase("save")){
		 Map map = txn_err_code_bo.save(txn_Error_Code_Form.getERROR_ID(),BeanUtils.describe(txn_Error_Code_Form));
		 BeanUtils.populate(txn_Error_Code_Form, map);
		 if(map.get("result").equals("TRUE")){
			System.out.println("SerchStrs>>"+txn_Error_Code_Form.getSerchStrs());
			BeanUtils.populate(txn_Error_Code_Form, JSONUtils.json2map(txn_Error_Code_Form.getSerchStrs()));
		 }
//		 list = txn_err_code_bo.search(txn_Error_Code_Form.getERROR_ID());
//		 txn_Error_Code_Form.setJsonList(JSONUtils.toJson(list));
//		 txn_Error_Code_Form.setScaseary(list);
	}
	if(ac_key.equalsIgnoreCase("add")){
	}
	if(ac_key.equalsIgnoreCase("edit")){
		list = txn_err_code_bo.search(txn_Error_Code_Form.getERROR_ID() , "");
		for(TXN_ERROR_CODE po :list){
			BeanUtils.copyProperties(txn_Error_Code_Form, po);
		}
	}
	if(ac_key.equalsIgnoreCase("update")){
		Map map =txn_err_code_bo.update(txn_Error_Code_Form.getERROR_ID(),BeanUtils.describe(txn_Error_Code_Form));
		BeanUtils.populate(txn_Error_Code_Form, map);
		if(map.get("result").equals("TRUE")){
			System.out.println("SerchStrs>>"+txn_Error_Code_Form.getSerchStrs());
			BeanUtils.populate(txn_Error_Code_Form, JSONUtils.json2map(txn_Error_Code_Form.getSerchStrs()));
		 }
//		list = txn_err_code_bo.search(txn_Error_Code_Form.getERROR_ID());
//		txn_Error_Code_Form.setJsonList(JSONUtils.toJson(list));
//		txn_Error_Code_Form.setScaseary(list);
	}
	if(ac_key.equalsIgnoreCase("delete")){
		Map map =txn_err_code_bo.delete(txn_Error_Code_Form.getERROR_ID());
		BeanUtils.populate(txn_Error_Code_Form, map);
		if(map.get("result").equals("TRUE")){
			System.out.println("SerchStrs>>"+txn_Error_Code_Form.getSerchStrs());
			BeanUtils.populate(txn_Error_Code_Form, JSONUtils.json2map(txn_Error_Code_Form.getSerchStrs()));
		 }
//		txn_Error_Code_Form.setAc_key("");
	}
	target = StrUtils.isEmpty(txn_Error_Code_Form.getTarget())?"":txn_Error_Code_Form.getTarget();
//	下拉清單 銀行端需額外過濾42XX
	if(login_form.getUserData().getUSER_TYPE().equals("B")){
		txn_Error_Code_Form.setIdList(txn_err_code_bo.getIdList4Bank());
	}else{
		txn_Error_Code_Form.setIdList(txn_err_code_bo.getIdList());
	}
	return mapping.findForward(target);
}

public TXN_ERROR_CODE_BO getTxn_err_code_bo() {
	return txn_err_code_bo;
}

public void setTxn_err_code_bo(TXN_ERROR_CODE_BO txn_err_code_bo) {
	this.txn_err_code_bo = txn_err_code_bo;
}



	
	
}
