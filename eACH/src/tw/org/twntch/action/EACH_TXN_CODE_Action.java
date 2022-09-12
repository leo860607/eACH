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

import tw.org.twntch.bo.EACH_TXN_CODE_BO;
import tw.org.twntch.form.Each_Txn_Code_Form;
import tw.org.twntch.po.EACH_TXN_CODE;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.StrUtils;

public class EACH_TXN_CODE_Action extends Action {
private EACH_TXN_CODE_BO each_txn_code_bo;



@Override
public ActionForward execute(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
	System.out.println("EACH_TXN_CODE_Action is start");
	String target = "";
	Each_Txn_Code_Form each_Txn_Code_Form = (Each_Txn_Code_Form) form;
	String ac_key = StrUtils.isEmpty(each_Txn_Code_Form.getAc_key())?"":each_Txn_Code_Form.getAc_key();
	target = StrUtils.isEmpty(each_Txn_Code_Form.getTarget())?"search":each_Txn_Code_Form.getTarget();
	each_Txn_Code_Form.setTarget(target);
	List<EACH_TXN_CODE> list = null;
	if(ac_key.equalsIgnoreCase("search")|| ac_key.equalsIgnoreCase("back") ){
		System.out.println("SerchStrs>>"+each_Txn_Code_Form.getSerchStrs());
		BeanUtils.populate(each_Txn_Code_Form, JSONUtils.json2map(each_Txn_Code_Form.getSerchStrs()));
//		list = each_txn_code_bo.search(each_Txn_Code_Form.getEACH_TXN_ID());
//		each_Txn_Code_Form.setJsonList(JSONUtils.toJson(list));
//		each_Txn_Code_Form.setScaseary(list);
	}
	if(ac_key.equalsIgnoreCase("save")){
		 Map map = each_txn_code_bo.save(each_Txn_Code_Form.getEACH_TXN_ID(),BeanUtils.describe(each_Txn_Code_Form));
		 BeanUtils.populate(each_Txn_Code_Form, map);
		 if(map.get("result").equals("TRUE")){
			BeanUtils.populate(each_Txn_Code_Form, JSONUtils.json2map(each_Txn_Code_Form.getSerchStrs()));
		 }
//		 list = each_txn_code_bo.search(each_Txn_Code_Form.getEACH_TXN_ID());
//		 each_Txn_Code_Form.setJsonList(JSONUtils.toJson(list));
//		 each_Txn_Code_Form.setScaseary(list);
	}
	if(ac_key.equalsIgnoreCase("add")){
	}
	if(ac_key.equalsIgnoreCase("edit")){
		BeanUtils.populate(each_Txn_Code_Form, JSONUtils.json2map(each_Txn_Code_Form.getEdit_params()));
		list = each_txn_code_bo.search(each_Txn_Code_Form.getEACH_TXN_ID());
		for(EACH_TXN_CODE po :list){
			BeanUtils.copyProperties(each_Txn_Code_Form, po);
		}
		
	}
	if(ac_key.equalsIgnoreCase("update")){
		Map map =each_txn_code_bo.update(each_Txn_Code_Form.getEACH_TXN_ID(),BeanUtils.describe(each_Txn_Code_Form));
		BeanUtils.populate(each_Txn_Code_Form, map);
		if(map.get("result").equals("TRUE")){
			BeanUtils.populate(each_Txn_Code_Form, JSONUtils.json2map(each_Txn_Code_Form.getSerchStrs()));
		}
//		list = each_txn_code_bo.search(each_Txn_Code_Form.getEACH_TXN_ID());
//		each_Txn_Code_Form.setJsonList(JSONUtils.toJson(list));
//		each_Txn_Code_Form.setScaseary(list);
	}
	if(ac_key.equalsIgnoreCase("delete")){
		Map map =each_txn_code_bo.delete(each_Txn_Code_Form.getEACH_TXN_ID());
		BeanUtils.populate(each_Txn_Code_Form, map);
		if(map.get("result").equals("TRUE")){
			BeanUtils.populate(each_Txn_Code_Form, JSONUtils.json2map(each_Txn_Code_Form.getSerchStrs()));
		}
//		each_Txn_Code_Form.setAc_key("");
	}
	target = StrUtils.isEmpty(each_Txn_Code_Form.getTarget())?"":each_Txn_Code_Form.getTarget();
//	下拉清單
	each_Txn_Code_Form.setIdList(each_txn_code_bo.getIdList());
	return mapping.findForward(target);
}

public EACH_TXN_CODE_BO getEach_txn_code_bo() {
	return each_txn_code_bo;
}

public void setEach_txn_code_bo(EACH_TXN_CODE_BO each_txn_code_bo) {
	this.each_txn_code_bo = each_txn_code_bo;
}



	
	
}
