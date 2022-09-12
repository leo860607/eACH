package tw.org.twntch.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import tw.org.twntch.bo.EACH_USERLOG_BO;
import tw.org.twntch.form.Each_Userlog_Form;
import tw.org.twntch.form.Login_Form;
import tw.org.twntch.util.BeanUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.WebServletUtils;

public class EACH_USERLOG_Demo_Action extends Action {

	private EACH_USERLOG_BO userlog_bo;
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// TODO Auto-generated method stub
		Each_Userlog_Form userlog_form = (Each_Userlog_Form) form ;
		String target = StrUtils.isEmpty(userlog_form.getTarget())?"":userlog_form.getTarget();
		String ac_key = StrUtils.isEmpty(userlog_form.getAc_key())?"":userlog_form.getAc_key();
		System.out.println("USERLOG_Action demo is start >> " + ac_key);
		Login_Form login_form = (Login_Form) WebServletUtils.getRequest().getSession().getAttribute("login_form");
		if(StrUtils.isNotEmpty(ac_key)){
			if(ac_key.equals("search")){
			}else if(ac_key.equals("update")){
			}else if(ac_key.equals("back")){
				setDropdownList(userlog_form , login_form ,ac_key);
				
			}else if(ac_key.equals("new")){
			}else if(ac_key.equals("add")){
			}else if(ac_key.equals("edit")){
				System.out.println("pk>>"+userlog_form.getSERNO()+","+ userlog_form.getUSERID()+" , "+ userlog_form.getUSER_COMPANY());
				BeanUtils.copyProperties(userlog_form, userlog_bo.getDetail(userlog_form.getSERNO(), userlog_form.getUSERID(), userlog_form.getUSER_COMPANY()));
			}else if(ac_key.equals("save")){
			}else if(ac_key.equals("delete")){
			}
		}else{
			userlog_form.setUSER_COMPANY(login_form.getUserData().getUSER_COMPANY());
			setDropdownList(userlog_form , login_form , ac_key);
		}
		
		if(StrUtils.isEmpty(target)){
			target = "search";
		}
		
		System.out.println("forward to >> " + target);
		return (mapping.findForward(target));
	}
	
	public void setDropdownList(Each_Userlog_Form form ,Login_Form  login_form ,String ac_key){
		
		if(login_form.getUserData().getUSER_TYPE().equals("A")){
			//使用者代號清單(以登入時預設的USER_COMPANY查詢)
			if( StrUtils.isNotEmpty(ac_key) && ac_key.equals("back")){
				form.setUserIdList(userlog_bo.getUserIdListByComId(form.getUSER_COMPANY()));
			}else{
				form.setUserIdList(userlog_bo.getUserIdListByComId(login_form.getUserData().getUSER_COMPANY()));
			}
			form.setFuncList(userlog_bo.getFuncListByRole_Type(login_form.getUserData().getUSER_TYPE()));
			//USER_COMPANY清單
			form.setUserCompanyList(userlog_bo.getUserCompanyList());
		}else{
			//使用者代號清單(以登入時預設的USER_COMPANY查詢)
			form.setUserIdList(userlog_bo.getUserIdListByComId(login_form.getUserData().getUSER_COMPANY()));
			//功能清單
			form.setFuncList(userlog_bo.getFuncListByRole_Type(login_form.getUserData().getUSER_TYPE()));
			form.setUSER_COMPANY(login_form.getUserData().getUSER_COMPANY());
		}
		
	}
	
	public EACH_USERLOG_BO getUserlog_bo() {
		return userlog_bo;
	}
	public void setUserlog_bo(EACH_USERLOG_BO userlog_bo) {
		this.userlog_bo = userlog_bo;
	}
}
