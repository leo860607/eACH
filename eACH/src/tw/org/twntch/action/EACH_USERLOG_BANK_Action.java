package tw.org.twntch.action;

import java.util.Map;

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
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.WebServletUtils;

public class EACH_USERLOG_BANK_Action extends Action{

	private EACH_USERLOG_BO userlog_bo;
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// TODO Auto-generated method stub
		Each_Userlog_Form userlog_form = (Each_Userlog_Form) form ;
		String target = StrUtils.isEmpty(userlog_form.getTarget())?"":userlog_form.getTarget();
		String ac_key = StrUtils.isEmpty(userlog_form.getAc_key())?"":userlog_form.getAc_key();
		System.out.println("EACH_USERLOG_BANK_Action is start >> " + ac_key);
		Login_Form login_form = (Login_Form) WebServletUtils.getRequest().getSession().getAttribute("login_form");
		if(StrUtils.isNotEmpty(ac_key)){
			if(ac_key.equals("search")){
				
			}else if(ac_key.equals("update")){
			}else if(ac_key.equals("back")){
				
//				userlog_form.setUSER_COMPANY(login_form.getUserData().getUSER_COMPANY());
				System.out.println("SerchStrs>>"+userlog_form.getSerchStrs());
				BeanUtils.populate(userlog_form, JSONUtils.json2map(userlog_form.getSerchStrs()));
				Map<String ,String> tmpMap = JSONUtils.json2map(userlog_form.getSerchStrs());
//				userlog_form.setUSER_TYPE((tmpMap.get("action").split("=")[1].replace("&", "")));
//				System.out.println("USER_TYPE_1>>"+(tmpMap.get("action").split("=")[1].replace("&", "")));
				System.out.println("USER_TYPE_2>>"+login_form.getUserData().getUSER_TYPE());
				System.out.println("target>>"+target);
				userlog_form.setUSER_TYPE(login_form.getUserData().getUSER_TYPE());
				setDropdownList(userlog_form , login_form);
			}else if(ac_key.equals("add")){
			}else if(ac_key.equals("edit")){
				System.out.println();
				BeanUtils.populate(userlog_form, JSONUtils.json2map(userlog_form.getEdit_params()));
				System.out.println("pk>>"+userlog_form.getSERNO()+","+ userlog_form.getUSERID()+" , "+ userlog_form.getUSER_COMPANY());
				BeanUtils.copyProperties(userlog_form, userlog_bo.getDetail(userlog_form.getSERNO(), userlog_form.getUSERID(), userlog_form.getUSER_COMPANY()));
			}else if(ac_key.equals("save")){
			}else if(ac_key.equals("delete")){
			}
		}else{
			//測試時預設 fstop
			userlog_form.setUSER_COMPANY(login_form.getUserData().getUSER_COMPANY());
			setDropdownList(userlog_form , login_form);
		}
		
		if(StrUtils.isEmpty(target)){
			target = "search";
		}
		
		System.out.println("forward to >> " + target);
		return (mapping.findForward(target));
	}
	
	public void setDropdownList(Each_Userlog_Form form ,Login_Form  login_form){
		form.setUserIdList(userlog_bo.getUserIdListByComId(login_form.getUserData().getUSER_COMPANY()));
		form.setFuncList(userlog_bo.getFuncListByRole_Type(login_form.getUserData().getUSER_TYPE()));
	}
	
	public EACH_USERLOG_BO getUserlog_bo() {
		return userlog_bo;
	}
	public void setUserlog_bo(EACH_USERLOG_BO userlog_bo) {
		this.userlog_bo = userlog_bo;
	}
	
}
