package tw.org.twntch.action;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import tw.org.twntch.bo.EACH_ROLE_LIST_BO;
import tw.org.twntch.form.Each_Role_List_Form;
import tw.org.twntch.po.EACH_ROLE_LIST;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.StrUtils;

public class EACH_ROLE_LIST_BANK_Action extends Action {

	private EACH_ROLE_LIST_BO role_list_bo ;

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		System.out.println("EACH_ROLE_LIST_Action is start");
		String target = "";
		Each_Role_List_Form role_list_form = (Each_Role_List_Form) form;
		String ac_key = StrUtils.isEmpty(role_list_form.getAc_key())?"":role_list_form.getAc_key();
		target = StrUtils.isEmpty(role_list_form.getTarget())?"search":role_list_form.getTarget();
		role_list_form.setTarget(target);
		List<EACH_ROLE_LIST> list = null;
		if(ac_key.equalsIgnoreCase("search")|| ac_key.equalsIgnoreCase("back") ){
			role_list_form.reset(mapping, request); 
		}
		if(ac_key.equalsIgnoreCase("edit")){
			list = role_list_bo.search(role_list_form.getROLE_ID() ,role_list_form.getROLE_TYPE());
			for(EACH_ROLE_LIST po :list){
				BeanUtils.copyProperties(role_list_form, po);
			}
			System.out.println("arytostring>>"+Arrays.toString(role_list_bo.getSelectedItem(role_list_form.getROLE_ID(), "2")));
			role_list_form.setSelected_MFuncs(role_list_bo.getSelectedItem(role_list_form.getROLE_ID(), "1"));
			role_list_form.setSelected_SFuncs(role_list_bo.getSelectedItem(role_list_form.getROLE_ID(), "2"));
			role_list_form.setSelected_SAuth(Arrays.toString(role_list_bo.getSelectedSAuth(role_list_form.getROLE_ID(), "2")));
		}
		target = StrUtils.isEmpty(role_list_form.getTarget())?"":role_list_form.getTarget();
//		下拉清單
		role_list_form.setMenuFuncs(role_list_bo.getMenuFuncList(role_list_form.getROLE_TYPE()));
//		role_list_form.setSubItemFuncsII(role_list_bo.getSubItemFuncListII(role_list_form.getROLE_TYPE()));
		role_list_form.setSubItemFuncsII(role_list_bo.getSubItemFuncListIII(role_list_form.getROLE_TYPE()));
		return mapping.findForward(target);
	}

	public EACH_ROLE_LIST_BO getRole_list_bo() {
		return role_list_bo;
	}

	public void setRole_list_bo(EACH_ROLE_LIST_BO role_list_bo) {
		this.role_list_bo = role_list_bo;
	}
	
	
	
}
