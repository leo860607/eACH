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
import org.apache.struts.util.LabelValueBean;

import tw.org.twntch.bo.BUSINESS_TYPE_BO;
import tw.org.twntch.bo.FEE_CODE_BO;
import tw.org.twntch.bo.TXN_CODE_BO;
import tw.org.twntch.form.Txn_Code_Form;
import tw.org.twntch.po.TXN_CODE;
import tw.org.twntch.util.CodeUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.StrUtils;

public class TXN_CODE_Action extends Action{
	private TXN_CODE_BO txn_code_bo ;
	private BUSINESS_TYPE_BO business_type_bo;
	private FEE_CODE_BO fee_code_bo;
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// TODO Auto-generated method stub
		System.out.println("TXN_CODE_Action is start");
		String target = "";
		Txn_Code_Form txn_code_form = (Txn_Code_Form) form;
		String ac_key = StrUtils.isEmpty(CodeUtils.escape(txn_code_form.getAc_key()))?"":CodeUtils.escape(txn_code_form.getAc_key());
		target = StrUtils.isEmpty(CodeUtils.escape(txn_code_form.getTarget()))?"search":CodeUtils.escape(txn_code_form.getTarget());
		txn_code_form.setTarget(target);
		List<TXN_CODE> list = null;
		if(ac_key.equalsIgnoreCase("search")|| ac_key.equalsIgnoreCase("back") ){
			
			System.out.println("SerchStrs>>"+txn_code_form.getSerchStrs());
			BeanUtils.populate(txn_code_form, JSONUtils.json2map(CodeUtils.escape(txn_code_form.getSerchStrs())));
			if(StrUtils.isNotEmpty(CodeUtils.escape(txn_code_form.getACTIVE_DATE()))){
				System.out.println("WITH DATE");
//				list = txn_code_bo.searchByIdAndActiveDate(txn_code_form.getTXN_ID(), txn_code_form.getACTIVE_DATE());
			}else{
				System.out.println("NO DATE");
//				list = txn_code_bo.search(txn_code_form.getTXN_ID());
			}
			//txn_code_form.setJsonList(JSONUtils.toJson(list));
//			txn_code_form.setScaseary(list);
		}
		if(ac_key.equalsIgnoreCase("save")){
			Map map = txn_code_bo.save(BeanUtils.describe(txn_code_form));
			BeanUtils.populate(txn_code_form, map);
			if(map.get("result").equals("TRUE")){
				BeanUtils.populate(txn_code_form, JSONUtils.json2map(txn_code_form.getSerchStrs()));
			}
			list = txn_code_bo.search(txn_code_form.getTXN_ID());
			//txn_code_form.setJsonList(JSONUtils.toJson(list));
			txn_code_form.setScaseary(list);
		}
		if(ac_key.equalsIgnoreCase("add")){
			//手續費下拉清單(20141201 - 改用Grid挑選手續費)
			//txn_code_form.setFeeList(fee_code_bo.getIdList_toJson());
			txn_code_form.setScaseary(fee_code_bo.search("all", ""));
			txn_code_form.setDistinctFeeIdList(fee_code_bo.getIdList());
		}
		if(ac_key.equalsIgnoreCase("edit")){
			BeanUtils.populate(txn_code_form, JSONUtils.json2map(txn_code_form.getEdit_params()));
			list = txn_code_bo.search(txn_code_form.getTXN_ID());
			for(TXN_CODE po :list){
				BeanUtils.copyProperties(txn_code_form, po);
			}
			//txn_code_form.setFeeList(fee_code_bo.getUnselectedFeeList(txn_code_form.getTXN_ID()));
			txn_code_form.setScaseary(fee_code_bo.search("all", ""));
			
			txn_code_form.setSelectedFeeList(fee_code_bo.getSelectedFeeList(txn_code_form.getTXN_ID()));
			String selectedFeeAry[] = new String[txn_code_form.getSelectedFeeList().size()];
			for(int i = 0; i < txn_code_form.getSelectedFeeList().size(); i++){
				selectedFeeAry[i] = ((LabelValueBean)txn_code_form.getSelectedFeeList().get(i)).getValue();
			}
			txn_code_form.setSelectedFeeAry(selectedFeeAry);
			txn_code_form.setDistinctFeeIdList(fee_code_bo.getIdList());
		}
		if(ac_key.equalsIgnoreCase("update")){
			
			Map map =txn_code_bo.update(BeanUtils.describe(txn_code_form), txn_code_form.getSelectedFeeAry());
			BeanUtils.populate(txn_code_form, map);
			if(map.get("result").equals("TRUE")){
				BeanUtils.populate(txn_code_form, JSONUtils.json2map(txn_code_form.getSerchStrs()));
				list = txn_code_bo.search(txn_code_form.getTXN_ID());
				//txn_code_form.setJsonList(JSONUtils.toJson(list));
				txn_code_form.setScaseary(list);
				txn_code_form.setDistinctFeeIdList(fee_code_bo.getIdList());
			}else {
				BeanUtils.populate(txn_code_form, JSONUtils.json2map(txn_code_form.getEdit_params()));
				list = txn_code_bo.search(txn_code_form.getTXN_ID());
				for(TXN_CODE po :list){
					BeanUtils.copyProperties(txn_code_form, po);
				}
				//txn_code_form.setFeeList(fee_code_bo.getUnselectedFeeList(txn_code_form.getTXN_ID()));
				txn_code_form.setScaseary(fee_code_bo.search("all", ""));
				
				txn_code_form.setSelectedFeeList(fee_code_bo.getSelectedFeeList(txn_code_form.getTXN_ID()));
				String selectedFeeAry[] = new String[txn_code_form.getSelectedFeeList().size()];
				for(int i = 0; i < txn_code_form.getSelectedFeeList().size(); i++){
					selectedFeeAry[i] = ((LabelValueBean)txn_code_form.getSelectedFeeList().get(i)).getValue();
				}
				txn_code_form.setSelectedFeeAry(selectedFeeAry);
				txn_code_form.setDistinctFeeIdList(fee_code_bo.getIdList());
				txn_code_form.setAc_key("back");
			}
		}
		if(ac_key.equalsIgnoreCase("delete")){
			Map map =txn_code_bo.delete(CodeUtils.escape(txn_code_form.getTXN_ID()));
			BeanUtils.populate(txn_code_form, map);
			if(map.get("result").equals("TRUE")){
				BeanUtils.populate(txn_code_form, JSONUtils.json2map(txn_code_form.getSerchStrs()));
			}
		}
		target = StrUtils.isEmpty(txn_code_form.getTarget())?"":txn_code_form.getTarget();
//		下拉清單
//		txn_code_form.setAgentTxnIdList(txn_code_bo.getAgentTxnIdList());
		txn_code_form.setIdList(txn_code_bo.getIdList());
		txn_code_form.setBsTypeIdList(business_type_bo.getBsTypeIdList());
		return mapping.findForward(target);
	}
	public TXN_CODE_BO getTxn_code_bo() {
		return txn_code_bo;
	}
	public void setTxn_code_bo(TXN_CODE_BO txn_code_bo) {
		this.txn_code_bo = txn_code_bo;
	}
	public BUSINESS_TYPE_BO getBusiness_type_bo() {
		return business_type_bo;
	}
	public void setBusiness_type_bo(BUSINESS_TYPE_BO business_type_bo) {
		this.business_type_bo = business_type_bo;
	}
	public FEE_CODE_BO getFee_code_bo() {
		return fee_code_bo;
	}
	public void setFee_code_bo(FEE_CODE_BO fee_code_bo) {
		this.fee_code_bo = fee_code_bo;
	}
}
