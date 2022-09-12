package tw.org.twntch.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import tw.org.twntch.bo.AGENT_PROFILE_BO;
import tw.org.twntch.bo.BANK_GROUP_BO;
import tw.org.twntch.bo.EACH_USERLOG_BO;
import tw.org.twntch.form.Each_Userlog_Form;
import tw.org.twntch.form.Login_Form;
import tw.org.twntch.util.BeanUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.SpringAppCtxHelper;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.WebServletUtils;

public class EACH_USERLOG_Action extends Action {

	private EACH_USERLOG_BO userlog_bo;
	private BANK_GROUP_BO bank_group_bo ;
	private AGENT_PROFILE_BO agent_profile_bo;
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// TODO Auto-generated method stub
		Each_Userlog_Form userlog_form = (Each_Userlog_Form) form ;
		String target = StrUtils.isEmpty(userlog_form.getTarget())?"":userlog_form.getTarget();
		String ac_key = StrUtils.isEmpty(userlog_form.getAc_key())?"":userlog_form.getAc_key();
		System.out.println("ADMUSERLOG_Action is start >> " + ac_key);
		Login_Form login_form = (Login_Form) WebServletUtils.getRequest().getSession().getAttribute("login_form");
		String fc_type = WebServletUtils.getRequest().getParameter("USER_TYPE");
		if(!ac_key.equals("back")){
			userlog_form.setFc_type(fc_type);
		}
		System.out.println("fc_type>>>"+fc_type);
		if(StrUtils.isNotEmpty(ac_key)){
			if(ac_key.equals("search")){
			}else if(ac_key.equals("update")){
			}else if(ac_key.equals("back")){
				BeanUtils.populate(userlog_form, JSONUtils.json2map(userlog_form.getSerchStrs()));
//				Map<String ,String> tmpMap = JSONUtils.json2map(userlog_form.getSerchStrs());
//				System.out.println("USER_TYPE_1>>"+(tmpMap.get("action").split("=")[1].replace("&", "")));
//				System.out.println("USER_TYPE_2>>"+login_form.getUserData().getUSER_TYPE());
//				System.out.println("target>>"+target);
//				userlog_form.setUSER_TYPE((tmpMap.get("action").split("=")[1].replace("&", "")));
				System.out.println("userlog_form.getFc_type>>"+userlog_form.getFc_type());
				if(userlog_form.getFc_type().equals("A")){
					userlog_form.setUSER_TYPE((login_form.getUserData().getUSER_TYPE()));
					userlog_form.setUserIdList(userlog_bo.getUserIdListByComId(""));
					userlog_form.setUserCompanyList(userlog_bo.getUserCompanyList());
					userlog_form.setFuncList(userlog_bo.getFuncList());
				}else if(userlog_form.getFc_type().equals("B")) {
					System.out.println("Fc_type ="+userlog_form.getFc_type()+" ,ROLE_TYPE ="+userlog_form.getROLE_TYPE()+" ,USER_COMPANY ="+userlog_form.getUSER_COMPANY());
					userlog_form.setUSER_TYPE("B");
					
					if(userlog_form.getROLE_TYPE().equals("B")){
						setDropdownList4back(userlog_form , login_form);
					}else{
						setDropdownList4back2(userlog_form , login_form);
					}
					
				}
			}else if(ac_key.equals("add")){
			}else if(ac_key.equals("edit")){
				BeanUtils.populate(userlog_form, JSONUtils.json2map(userlog_form.getEdit_params()));
				System.out.println("pk>>"+userlog_form.getSERNO()+","+ userlog_form.getUSERID()+" , "+ userlog_form.getUSER_COMPANY());
				BeanUtils.copyProperties(userlog_form, userlog_bo.getDetail(userlog_form.getSERNO(), userlog_form.getUSERID(), userlog_form.getUSER_COMPANY()));
			}else if(ac_key.equals("save")){
			}else if(ac_key.equals("delete")){
			}
		}else{
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
		//TODO 使用者代號清單(應以登入時預設的USER_COMPANY查詢)
		if(form.getUSER_TYPE().equals("A")){
			form.setUserIdList(userlog_bo.getUserIdListByComId(login_form.getUserData().getUSER_COMPANY()));
		}
		//USER_COMPANY清單
		form.setUserCompanyList(userlog_bo.getUserCompanyList(form.getUSER_TYPE()));
		form.setOpt_bankList(bank_group_bo.getOpbkList());
		//功能清單
		form.setFuncList(userlog_bo.getFuncList(form.getUSER_TYPE()));
//		form.setFuncList(userlog_bo.getFuncListByRole_Type(form.getUSER_TYPE()));
	}
	public void setDropdownList4back(Each_Userlog_Form form ,Login_Form  login_form){
		//TODO 使用者代號清單(應以登入時預設的USER_COMPANY查詢)
		form.setUserIdList(userlog_bo.getUserIdListByComId(form.getUSER_COMPANY()));
		//USER_COMPANY清單
		form.setUserCompanyList(userlog_bo.getUserCompanyList(form.getUSER_TYPE()));
		form.setOpt_bankList(bank_group_bo.getOpbkList());
		//功能清單
		form.setFuncList(userlog_bo.getFuncList(form.getUSER_TYPE()));
	}
	
	//當群組類型為代理業者 ROLE_TYPE=C
	public void setDropdownList4back2(Each_Userlog_Form form ,Login_Form  login_form){
		form.setUserIdList(userlog_bo.getUserIdListByComId(form.getUSER_COMPANY()));
		//USER_COMPANY清單
		form.setUserCompanyList(userlog_bo.getUserCompanyList(form.getUSER_TYPE()));
		agent_profile_bo = (AGENT_PROFILE_BO) (agent_profile_bo==null ? SpringAppCtxHelper.getBean("agent_profile_bo"):agent_profile_bo);
		form.setOpt_bankList(agent_profile_bo.getCompany_Id_List());
		//功能清單
		form.setFuncList(userlog_bo.getFuncList(form.getROLE_TYPE()));
	}
	
	public EACH_USERLOG_BO getUserlog_bo() {
		return userlog_bo;
	}
	public void setUserlog_bo(EACH_USERLOG_BO userlog_bo) {
		this.userlog_bo = userlog_bo;
	}

	public BANK_GROUP_BO getBank_group_bo() {
		return bank_group_bo;
	}

	public void setBank_group_bo(BANK_GROUP_BO bank_group_bo) {
		this.bank_group_bo = bank_group_bo;
	}
	
	public AGENT_PROFILE_BO getAgent_profile_bo() {
		return agent_profile_bo;
	}

	public void setAgent_profile_bo(AGENT_PROFILE_BO agent_profile_bo) {
		this.agent_profile_bo = agent_profile_bo;
	}
}
