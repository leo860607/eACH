package tw.org.twntch.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import tw.org.twntch.bo.Arguments;
import tw.org.twntch.bo.BANK_GROUP_BO;
import tw.org.twntch.bo.EACHSYSSTATUSTAB_BO;
import tw.org.twntch.bo.PROXY_CL_BO;
import tw.org.twntch.bo.RPTCL_4_BO;
import tw.org.twntch.db.dao.hibernate.BANK_GROUP_Dao;
import tw.org.twntch.form.Login_Form;
import tw.org.twntch.form.Rptcl_4_Form;
import tw.org.twntch.po.BANK_GROUP;
import tw.org.twntch.util.BeanUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.WebServletUtils;
import tw.org.twntch.util.zDateHandler;

public class RPTCL_4_Action extends GenericAction {
	private RPTCL_4_BO rptcl_4_bo ;
	private BANK_GROUP_BO bank_group_bo ;
	private BANK_GROUP_Dao bank_group_Dao ;
	private EACHSYSSTATUSTAB_BO eachsysstatustab_bo ;
	private PROXY_CL_BO proxy_cl_bo;
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Rptcl_4_Form Rptcl_4_Form = (Rptcl_4_Form) form;
		String ac_key = StrUtils.isEmpty(Rptcl_4_Form.getAc_key())?"":Rptcl_4_Form.getAc_key();
		String target = StrUtils.isEmpty(Rptcl_4_Form.getTarget())?"search":Rptcl_4_Form.getTarget();
		Rptcl_4_Form.setTarget(target);
		System.out.println("RPTCL_4_Action is start >> " + ac_key);
		Login_Form login_form = (Login_Form) WebServletUtils.getRequest().getSession().getAttribute("login_form");
		if(ac_key.equalsIgnoreCase("search")){
//			銀行端要多加一道檢核
			if(login_form.getUserData().getUSER_TYPE().equals("B")){
//				Map retmap = super.checkRPTCL_BizDate(Rptcl_4_Form.getBIZDATE(), Rptcl_4_Form.getCLEARINGPHASE());
				Map retmap = super.checkRPT_BizDate(Rptcl_4_Form.getBIZDATE(), Rptcl_4_Form.getCLEARINGPHASE());
				if(retmap.get("result").equals("FALSE") ){
					BeanUtils.populate(Rptcl_4_Form, retmap);
					return mapping.findForward(target);
				}
			}
			Map map = rptcl_4_bo.export(Rptcl_4_Form.getBIZDATE(), Rptcl_4_Form.getCTBK_ID(), Rptcl_4_Form.getCLEARINGPHASE(), Rptcl_4_Form.getSerchStrs());
			BeanUtils.populate(Rptcl_4_Form, map);
			if(map.get("result").equals("TRUE") && !Arguments.getStringArg("ISWINOPEN").equals("Y")){
				exportFile2Client(response, map.get("msg").toString(), zDateHandler.getDateNum()+"_cl_4.pdf" ,Rptcl_4_Form.getDow_token() );
				return null;
			}
		}
		if(StrUtils.isEmpty(ac_key)){
		}
		if(login_form.getUserData().getUSER_TYPE().equals("B")){
			BANK_GROUP po =  bank_group_Dao.get(login_form.getUserData().getUSER_COMPANY());
			Rptcl_4_Form.setCTBK_NAME (login_form.getUserData().getUSER_COMPANY()+"-"+po.getBGBK_NAME());
//			rpttx_1_form.setSENDERACQUIRE(login_form.getUserData().getUSER_COMPANY());
			Rptcl_4_Form.setCTBK_ID(login_form.getUserData().getUSER_COMPANY());
			Rptcl_4_Form.setBg_bankList(bank_group_bo.getCurBgbkListByOP(login_form.getUserData().getUSER_COMPANY() , eachsysstatustab_bo.getRptBusinessDate()));
		}
		Rptcl_4_Form.setBIZDATE(eachsysstatustab_bo.getRptBusinessDate());
		Rptcl_4_Form.setCt_bankList(bank_group_bo.getCtbkIdList());
//		Rptcl_4_Form.setCt_bankList(rptcl_4_bo.getOptCtbkIdList());
//		Rptcl_4_Form.setCt_bankList(proxy_cl_bo.getProxyClean_BankList());
		System.out.println("txdt>>"+Rptcl_4_Form.getTXDT());
		target = StrUtils.isEmpty(Rptcl_4_Form.getTarget())?"":Rptcl_4_Form.getTarget();
		return mapping.findForward(target);
	}

	public RPTCL_4_BO getRptcl_4_bo() {
		return rptcl_4_bo;
	}

	public void setRptcl_4_bo(RPTCL_4_BO rptcl_4_bo) {
		this.rptcl_4_bo = rptcl_4_bo;
	}

	public BANK_GROUP_BO getBank_group_bo() {
		return bank_group_bo;
	}

	public void setBank_group_bo(BANK_GROUP_BO bank_group_bo) {
		this.bank_group_bo = bank_group_bo;
	}

	public BANK_GROUP_Dao getBank_group_Dao() {
		return bank_group_Dao;
	}

	public void setBank_group_Dao(BANK_GROUP_Dao bank_group_Dao) {
		this.bank_group_Dao = bank_group_Dao;
	}

	public EACHSYSSTATUSTAB_BO getEachsysstatustab_bo() {
		return eachsysstatustab_bo;
	}

	public void setEachsysstatustab_bo(EACHSYSSTATUSTAB_BO eachsysstatustab_bo) {
		this.eachsysstatustab_bo = eachsysstatustab_bo;
	}

	public PROXY_CL_BO getProxy_cl_bo() {
		return proxy_cl_bo;
	}

	public void setProxy_cl_bo(PROXY_CL_BO proxy_cl_bo) {
		this.proxy_cl_bo = proxy_cl_bo;
	}
}
