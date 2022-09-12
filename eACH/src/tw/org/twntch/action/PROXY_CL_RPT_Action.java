package tw.org.twntch.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import tw.org.twntch.bo.Arguments;
import tw.org.twntch.bo.BANK_GROUP_BO;
import tw.org.twntch.bo.EACHSYSSTATUSTAB_BO;
import tw.org.twntch.bo.PROXY_CL_BO;
import tw.org.twntch.form.Login_Form;
import tw.org.twntch.form.Proxy_Cl_Form;
import tw.org.twntch.po.BANK_GROUP;
import tw.org.twntch.util.BeanUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.WebServletUtils;
import tw.org.twntch.util.zDateHandler;

public class PROXY_CL_RPT_Action extends GenericAction {
	private PROXY_CL_BO  proxy_cl_bo;
	private BANK_GROUP_BO bank_group_bo;
	private EACHSYSSTATUSTAB_BO eachsysstatustab_bo;
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Proxy_Cl_Form proxy_cl_form = (Proxy_Cl_Form) form;
		String ac_key = StrUtils.isEmpty(proxy_cl_form.getAc_key())?"":proxy_cl_form.getAc_key();
		String target = StrUtils.isEmpty(proxy_cl_form.getTarget())?"search":proxy_cl_form.getTarget();
		proxy_cl_form.setTarget(target);
		logger.debug("RPTCL_3_Action is start >> " + ac_key);
		Login_Form login_form = (Login_Form) WebServletUtils.getRequest().getSession().getAttribute("login_form");
		if(ac_key.equalsIgnoreCase("search")){
			logger.debug("proxy_cl_form.getTXDT()>>"+proxy_cl_form.getBIZDATE());
			Map map = proxy_cl_bo.export(proxy_cl_form.getBIZDATE(), proxy_cl_form.getCLEARINGPHASE() ,proxy_cl_form.getCTBK_ID(), proxy_cl_form.getSerchStrs() );
			BeanUtils.populate(proxy_cl_form, map);
//			if(map.get("result").equals("TRUE") && !Arguments.getStringArg("ISWINOPEN").equals("Y")){
//				exportFile2Client(response, map.get("msg").toString(), zDateHandler.getDateNum()+"_cl_4.pdf" ,proxy_cl_form.getDow_token() );
//				return null;
//			}
			
			if(login_form.getUserData().getUSER_TYPE().equals("B")){
				Map retmap = super.checkRPT_BizDate(proxy_cl_form.getBIZDATE(), proxy_cl_form.getCLEARINGPHASE());
				if(retmap.get("result").equals("FALSE") ){
					BeanUtils.populate(proxy_cl_form, retmap);
					return mapping.findForward(target);
				}
			}
			
			if(map.get("result").equals("TRUE") && !Arguments.getStringArg("ISWINOPEN").equals("Y")){
				exportFile2Client(response, map.get("msg").toString(), zDateHandler.getDateNum()+"_cl_4.pdf" ,proxy_cl_form.getDow_token() );
				return null;
			}
		}
		//初次進入此功能
		if(StrUtils.isEmpty(ac_key)){
			//將營業日塞到頁面的日期控制項
//			20150529 edit by hugo req by UAT-20150525-02
//			String busDate = eachsysstatustab_bo.getBusinessDate();
			String busDate = eachsysstatustab_bo.getRptBusinessDate();
			logger.debug("busDate="+busDate);
			proxy_cl_form.setBIZDATE(busDate);
		}
		if(login_form.getUserData().getUSER_TYPE().equals("B")){
			BANK_GROUP po =  bank_group_bo.getOne(login_form.getUserData().getUSER_COMPANY());
			proxy_cl_form.setCTBK_NAME (login_form.getUserData().getUSER_COMPANY()+"-"+po.getBGBK_NAME());
//			rpttx_1_form.setSENDERACQUIRE(login_form.getUserData().getUSER_COMPANY());
			proxy_cl_form.setCTBK_ID(login_form.getUserData().getUSER_COMPANY());
		}
		proxy_cl_form.setProxy_cl_bankList(proxy_cl_bo.getProxyClean_BankList());
		target = StrUtils.isEmpty(proxy_cl_form.getTarget())?"":proxy_cl_form.getTarget();
		return mapping.findForward(target);
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
