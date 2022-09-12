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

import tw.org.twntch.bo.BANK_GROUP_BO;
import tw.org.twntch.bo.BUSINESS_TYPE_BO;
import tw.org.twntch.form.Business_Type_Form;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.StrUtils;

public class BUSINESS_TYPE_Action extends Action {
	private BANK_GROUP_BO bank_group_bo ; 
	private BUSINESS_TYPE_BO business_type_bo ;
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// TODO Auto-generated method stub
		System.out.println("BUSINESS_TYPE_Action is start ");
		Business_Type_Form business_type_form=(Business_Type_Form) form ;
		String target ="";
		String ac_key = StrUtils.isEmpty(business_type_form.getAc_key())?"":business_type_form.getAc_key();
		target = StrUtils.isEmpty(business_type_form.getTarget())?"business_type":business_type_form.getTarget();
		business_type_form.setTarget(target);
		System.out.println("ac_key>>"+ac_key);
		List list = null;
		if(ac_key.equalsIgnoreCase("search") || ac_key.equalsIgnoreCase("back")){
			System.out.println("SerchStrs>>"+business_type_form.getSerchStrs());
			BeanUtils.populate(business_type_form, JSONUtils.json2map(business_type_form.getSerchStrs()));
		}
		
		if(ac_key.equalsIgnoreCase("save")){
			Map map = business_type_bo.save(business_type_form.getBUSINESS_TYPE_ID(), business_type_form.getBUSINESS_TYPE_NAME(), business_type_form.getSelectedBankArray());
			BeanUtils.populate(business_type_form, map);
			if(map.get("result").equals("FALSE")){
				business_type_form.setUnselectedBankList(business_type_bo.getDropdownFromJson(business_type_form.getJson_unselectedBank()));
				business_type_form.setSelectedBankList(business_type_bo.getDropdownFromJson(business_type_form.getJson_selectedBank()));
			}else{
				BeanUtils.populate(business_type_form, JSONUtils.json2map(business_type_form.getSerchStrs()));
			}
//			list = business_type_bo.search(business_type_form.getBUSINESS_TYPE_ID(), business_type_form.getBUSINESS_TYPE_NAME());
//			business_type_form.setScaseary(list);
		}
		if(ac_key.equalsIgnoreCase("add")){
			//帶入未選業務類別的總行清單
//			business_type_form.setUnselectedBankList(bank_group_bo.getBgbkIdList());
//			20150827 req by 李建利 只顯示屬於EACH總行的清單
			business_type_form.setUnselectedBankList(bank_group_bo.getBgbkIdList_EACH());
		}
		if(ac_key.equalsIgnoreCase("edit")){
			//帶入未選、已選業務類別的總行清單
			BeanUtils.populate(business_type_form, JSONUtils.json2map(business_type_form.getEdit_params()));
			System.out.println("business_type_form.getBUSINESS_TYPE_ID()>>"+business_type_form.getBUSINESS_TYPE_ID());
			business_type_form.setUnselectedBankList(business_type_bo.getUnselectedBgbkIdList(business_type_form.getBUSINESS_TYPE_ID()));
			business_type_form.setSelectedBankList(business_type_bo.getSelectedBgbkIdList(business_type_form.getBUSINESS_TYPE_ID()));
		}
		if(ac_key.equalsIgnoreCase("update")){
			Map map =business_type_bo.update(business_type_form.getBUSINESS_TYPE_ID(), business_type_form.getBUSINESS_TYPE_NAME(),business_type_form.getSelectedBankArray());
			BeanUtils.populate(business_type_form, map);
			if(map.get("result").equals("FALSE")){
				business_type_form.setUnselectedBankList(business_type_bo.getDropdownFromJson(business_type_form.getJson_unselectedBank()));
				business_type_form.setSelectedBankList(business_type_bo.getDropdownFromJson(business_type_form.getJson_selectedBank()));
			}else{
				BeanUtils.populate(business_type_form, JSONUtils.json2map(business_type_form.getSerchStrs()));
			}
			
//			list = business_type_bo.search(business_type_form.getBUSINESS_TYPE_ID(), business_type_form.getBUSINESS_TYPE_NAME());
//			business_type_form.setScaseary(list);
		}
		if(ac_key.equalsIgnoreCase("delete")){
			Map map =business_type_bo.delete(business_type_form.getBUSINESS_TYPE_ID());
			BeanUtils.populate(business_type_form, map);
			if(map.get("result").equals("FALSE")){
				business_type_form.setUnselectedBankList(business_type_bo.getDropdownFromJson(business_type_form.getJson_unselectedBank()));
				business_type_form.setSelectedBankList(business_type_bo.getDropdownFromJson(business_type_form.getJson_selectedBank()));
			}else{
				BeanUtils.populate(business_type_form, JSONUtils.json2map(business_type_form.getSerchStrs()));
			}
//			business_type_form.setAc_key("");
		}
		target = StrUtils.isEmpty(business_type_form.getTarget())?"":business_type_form.getTarget();
//		下拉清單
		business_type_form.setBsIdKist(business_type_bo.getBsTypeIdList());
		return mapping.findForward(target);
	}
	public BUSINESS_TYPE_BO getBusiness_type_bo() {
		return business_type_bo;
	}
	public void setBusiness_type_bo(BUSINESS_TYPE_BO business_type_bo) {
		this.business_type_bo = business_type_bo;
	}
	public BANK_GROUP_BO getBank_group_bo() {
		return bank_group_bo;
	}
	public void setBank_group_bo(BANK_GROUP_BO bank_group_bo) {
		this.bank_group_bo = bank_group_bo;
	}
}
