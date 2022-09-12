package tw.org.twntch.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import tw.org.twntch.bo.PAYMENT_CATEGORY_BO;
import tw.org.twntch.form.Payment_Category_Form;
import tw.org.twntch.po.BILL_TYPE;
import tw.org.twntch.po.EACH_TXN_CODE;
import tw.org.twntch.util.BeanUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.StrUtils;

public class PAYMENT_CATEGORY_Action extends Action{
	
	private PAYMENT_CATEGORY_BO payment_category_bo;
	
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// TODO Auto-generated method stub
		System.out.println("PAYMENT_CATEGORY_Action is start");
		
		Payment_Category_Form payment_category_form = (Payment_Category_Form) form;
		String ac_key = StrUtils.isEmpty(payment_category_form.getAc_key())?"":payment_category_form.getAc_key();
		String target = StrUtils.isEmpty(payment_category_form.getTarget())?"search":payment_category_form.getTarget();
		payment_category_form.setTarget(target);
		System.out.println("ac_key>>"+ac_key);
		List<BILL_TYPE> list = null;
		if(ac_key.equalsIgnoreCase("search")|| ac_key.equalsIgnoreCase("back") ){
			System.out.println("SerchStrs>>"+payment_category_form.getSerchStrs());
			BeanUtils.populate(payment_category_form, JSONUtils.json2map(payment_category_form.getSerchStrs()));

		}
		if(ac_key.equalsIgnoreCase("save")){
			 Map map = payment_category_bo.save(payment_category_form.getBILL_TYPE_ID(),BeanUtils.describe(payment_category_form));
			 BeanUtils.populate(payment_category_form, map);
			 if(map.get("result").equals("TRUE")){
				BeanUtils.populate(payment_category_form, JSONUtils.json2map(payment_category_form.getSerchStrs()));
			 }
		}
		if(ac_key.equalsIgnoreCase("add")){
		}
		if(ac_key.equalsIgnoreCase("edit")){
			BeanUtils.populate(payment_category_form, JSONUtils.json2map(payment_category_form.getEdit_params()));
			list = payment_category_bo.search(payment_category_form.getBILL_TYPE_ID());
			for(BILL_TYPE po :list){
				BeanUtils.copyProperties(payment_category_form, po);
			}
			
		}
		if(ac_key.equalsIgnoreCase("update")){
			Map map =payment_category_bo.update(payment_category_form.getBILL_TYPE_ID(),BeanUtils.describe(payment_category_form));
			BeanUtils.populate(payment_category_form, map);
			if(map.get("result").equals("TRUE")){
				BeanUtils.populate(payment_category_form, JSONUtils.json2map(payment_category_form.getSerchStrs()));
			}
		}
		if(ac_key.equalsIgnoreCase("delete")){
			Map map =payment_category_bo.delete(payment_category_form.getBILL_TYPE_ID());
			BeanUtils.populate(payment_category_form, map);
			if(map.get("result").equals("TRUE")){
				BeanUtils.populate(payment_category_form, JSONUtils.json2map(payment_category_form.getSerchStrs()));
			}
		}
		target = StrUtils.isEmpty(payment_category_form.getTarget())?"":payment_category_form.getTarget();
//		下拉清單
		payment_category_form.setIdList(payment_category_bo.getIdList());
		return mapping.findForward(target);
	}
	
	public PAYMENT_CATEGORY_BO getPayment_category_bo() {
		return payment_category_bo;
	}
	public void setPayment_category_bo(PAYMENT_CATEGORY_BO payment_category_bo) {
		this.payment_category_bo = payment_category_bo;
	}
	
}
