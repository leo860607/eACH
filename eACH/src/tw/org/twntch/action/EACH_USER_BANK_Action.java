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

import tw.org.twntch.bo.EACH_USER_BO;
import tw.org.twntch.form.Each_User_Form;
import tw.org.twntch.form.Login_Form;
import tw.org.twntch.po.EACH_ROLE_LIST;
import tw.org.twntch.po.EACH_USER;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.StrUtils;

public class EACH_USER_BANK_Action extends Action{

	private EACH_USER_BO each_user_bo;

	
	
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// TODO Auto-generated method stub
		System.out.println("EACH_USER_BANK_Action is start");
		String target = "";
		Each_User_Form each_user_form = (Each_User_Form) form;
		Login_Form login_form = (Login_Form) request.getSession().getAttribute("login_form");
		System.out.println("loginid>>"+login_form.getUserId());
//		TODO 這邊還要加入R/W search all 的判斷
		
		String ac_key = StrUtils.isEmpty(each_user_form.getAc_key())?"":each_user_form.getAc_key();
		System.out.println("ac_key>>"+ac_key);
		target = StrUtils.isEmpty(each_user_form.getTarget())?"search":each_user_form.getTarget();
		each_user_form.setTarget(target);
		String user_company = login_form.getUserData().getUSER_COMPANY();
		each_user_form.setUSER_COMPANY(user_company);
		List<EACH_USER> list = null;
		if(ac_key.equalsIgnoreCase("search")|| ac_key.equalsIgnoreCase("back") ){
			BeanUtils.populate(each_user_form, JSONUtils.json2map(each_user_form.getSerchStrs()));
			Map<String ,String> tmpMap = JSONUtils.json2map(each_user_form.getSerchStrs());
		}
		if(ac_key.equalsIgnoreCase("save")){
//			 Map map = each_user_bo.save(each_user_form.getUSER_ID(), user_company ,BeanUtils.describe(each_user_form));
//			 BeanUtils.populate(each_user_form, map);
		}
		if(ac_key.equalsIgnoreCase("add")){
				System.out.println("po.getUSER_TYPE()>>"+login_form.getUserData().getUSER_TYPE());
				each_user_form.setRole_list(each_user_bo.getRole_ListByUser_Type(login_form.getUserData().getUSER_TYPE()));
		}
		if(ac_key.equalsIgnoreCase("edit")){
//			list = each_user_bo.searchByPK(each_user_form.getUSER_ID(), each_user_form.getUSER_COMPANY());
			BeanUtils.populate(each_user_form, JSONUtils.json2map(each_user_form.getEdit_params()));
			EACH_USER po = each_user_bo.searchByPK(each_user_form.getUSER_ID());
			System.out.println("list.edit>>"+list);
			BeanUtils.copyProperties(each_user_form, po);
			each_user_form.setRole_list(each_user_bo.getRole_ListByUser_Type(po.getUSER_TYPE()));
		}
		if(ac_key.equalsIgnoreCase("update")){
//			Map map =each_user_bo.update(each_user_form.getUSER_ID(), user_company,BeanUtils.describe(each_user_form));
//			BeanUtils.populate(each_user_form, map);
		}
		if(ac_key.equalsIgnoreCase("delete")){
//			Map map =each_user_bo.delete(each_user_form.getUSER_ID(), user_company);
//			BeanUtils.populate(each_user_form, map);
		}
		target = StrUtils.isEmpty(each_user_form.getTarget())?"":each_user_form.getTarget();
		return mapping.findForward(target);
	}

	public EACH_USER_BO getEach_user_bo() {
		return each_user_bo;
	}

	public void setEach_user_bo(EACH_USER_BO each_user_bo) {
		this.each_user_bo = each_user_bo;
	}
	
	
	
	
}
