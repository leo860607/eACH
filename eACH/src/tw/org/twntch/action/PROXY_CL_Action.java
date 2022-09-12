package tw.org.twntch.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import tw.org.twntch.bo.BANK_GROUP_BO;
import tw.org.twntch.bo.EACHSYSSTATUSTAB_BO;
import tw.org.twntch.bo.PROXY_CL_BO;
import tw.org.twntch.form.Login_Form;
import tw.org.twntch.form.Proxy_Cl_Form;
import tw.org.twntch.po.BANK_GROUP;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.WebServletUtils;
import tw.org.twntch.util.zDateHandler;

public class PROXY_CL_Action extends GenericAction{
	private PROXY_CL_BO  proxy_cl_bo;
	private BANK_GROUP_BO bank_group_bo;
	private EACHSYSSTATUSTAB_BO eachsysstatustab_bo;
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		// TODO Auto-generated method stub
		Proxy_Cl_Form proxy_cl_form = (Proxy_Cl_Form) form ;
		String target = StrUtils.isEmpty(proxy_cl_form.getTarget())?"search":proxy_cl_form.getTarget();
		String ac_key = StrUtils.isEmpty(proxy_cl_form.getAc_key())?"":proxy_cl_form.getAc_key();
		System.out.println("FAIL_TRANS_Action demo is start >> " + ac_key);
		Login_Form login_form = (Login_Form) WebServletUtils.getRequest().getSession().getAttribute("login_form");
		if(StrUtils.isNotEmpty(ac_key)){
			if(ac_key.equals("search")){
				
			}else if(ac_key.equals("update")){
			}else if(ac_key.equals("new")){
			}else if(ac_key.equals("add")){
			}else if(ac_key.equals("save")){
			}else if(ac_key.equals("delete")){
			}else if(ac_key.equals("don")){
//				來自下載頁面 考慮直接指定target xx.do?target=don
				target= "don";
			}
		}
		
//		if(StrUtils.isEmpty(target)){
//			target = "search";
//		}
		if(login_form.getUserData().getUSER_TYPE().equals("A")){
			//target="";
		}else{
			//target="";
			String userCompany = login_form.getUserData().getUSER_COMPANY();
			proxy_cl_form.setSENDERACQUIRE(userCompany);
			BANK_GROUP po =  bank_group_bo.getOne(userCompany);
			proxy_cl_form.setCTBK_NAME(userCompany + " - " + po.getBGBK_NAME());
		}
//		20150529 edit by hugo req by UAT-20150525-02
//		proxy_cl_form.setTXDATE(zDateHandler.getTWDate());
		proxy_cl_form.setBIZDATE(eachsysstatustab_bo.getRptBusinessDate());
		proxy_cl_form.setProxy_cl_bankList(proxy_cl_bo.getProxyClean_BankList());
		System.out.println("forward to >> " + target);
		return (mapping.findForward(target));
	}

	public PROXY_CL_BO getProxy_cl_bo() {
		return proxy_cl_bo;
	}

	public void setProxy_cl_bo(PROXY_CL_BO proxy_cl_bo) {
		this.proxy_cl_bo = proxy_cl_bo;
	}

	public BANK_GROUP_BO getBank_group_bo() {
		return bank_group_bo;
	}

	public void setBank_group_bo(BANK_GROUP_BO bank_group_bo) {
		this.bank_group_bo = bank_group_bo;
	}

	public EACHSYSSTATUSTAB_BO getEachsysstatustab_bo() {
		return eachsysstatustab_bo;
	}

	public void setEachsysstatustab_bo(EACHSYSSTATUSTAB_BO eachsysstatustab_bo) {
		this.eachsysstatustab_bo = eachsysstatustab_bo;
	}
	
	

}
