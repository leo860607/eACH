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
import tw.org.twntch.bo.RPTCL_1_BO;
import tw.org.twntch.db.dao.hibernate.BANK_GROUP_Dao;
import tw.org.twntch.form.Login_Form;
import tw.org.twntch.form.Rptcl_1_Form;
import tw.org.twntch.po.BANK_GROUP;
import tw.org.twntch.util.BeanUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.WebServletUtils;
import tw.org.twntch.util.zDateHandler;

public class RPTCL_1_Action extends GenericAction {
	private RPTCL_1_BO rptcl_1_bo ;
	private BANK_GROUP_BO bank_group_bo ;
	private BANK_GROUP_Dao bank_group_Dao ;
	private EACHSYSSTATUSTAB_BO eachsysstatustab_bo ;
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Rptcl_1_Form Rptcl_1_Form = (Rptcl_1_Form) form;
		String ac_key = StrUtils.isEmpty(Rptcl_1_Form.getAc_key())?"":Rptcl_1_Form.getAc_key();
		String target = StrUtils.isEmpty(Rptcl_1_Form.getTarget())?"search":Rptcl_1_Form.getTarget();
		Login_Form login_form = (Login_Form) WebServletUtils.getRequest().getSession().getAttribute("login_form");
		Rptcl_1_Form.setTarget(target);
		System.out.println("RPTCL_1_Action is start >> " + ac_key);
		
		if(ac_key.equalsIgnoreCase("search")){
//			銀行端要多加一道檢核
			if(login_form.getUserData().getUSER_TYPE().equals("B")){
//				Map retmap = super.checkRPTCL_BizDate(Rptcl_1_Form.getBIZDATE(), Rptcl_1_Form.getCLEARINGPHASE());
				Map retmap = super.checkRPT_BizDate(Rptcl_1_Form.getBIZDATE(), Rptcl_1_Form.getCLEARINGPHASE());
				if(retmap.get("result").equals("FALSE") ){
					BeanUtils.populate(Rptcl_1_Form, retmap);
					return mapping.findForward(target);
				}
			}
//			Map map = rptcl_1_bo.export(Rptcl_1_Form.getTXDT(), Rptcl_1_Form.getSENDERACQUIRE(), Rptcl_1_Form.getCLEARINGPHASE() ,Rptcl_1_Form.getOpt_bank());
			Map map = rptcl_1_bo.export(Rptcl_1_Form.getBIZDATE(), Rptcl_1_Form.getBGBK_ID(), Rptcl_1_Form.getCLEARINGPHASE() ,Rptcl_1_Form.getOpt_id(), Rptcl_1_Form.getSerchStrs());
			BeanUtils.populate(Rptcl_1_Form, map);
			if(map.get("result").equals("TRUE") && !Arguments.getStringArg("ISWINOPEN").equals("Y")){
				exportFile2Client(response, map.get("msg").toString(), zDateHandler.getDateNum()+"_cl_1.pdf" ,Rptcl_1_Form.getDow_token() );
				return null;
			}
		}
		if(StrUtils.isEmpty(ac_key)){
		}
		if(login_form.getUserData().getUSER_TYPE().equals("B")){
			BANK_GROUP po =  bank_group_Dao.get(login_form.getUserData().getUSER_COMPANY());
			Rptcl_1_Form.setOpt_bank(login_form.getUserData().getUSER_COMPANY()+"-"+po.getBGBK_NAME());
//			rpttx_1_form.setSENDERACQUIRE(login_form.getUserData().getUSER_COMPANY());
			Rptcl_1_Form.setOpt_id(login_form.getUserData().getUSER_COMPANY());
			Rptcl_1_Form.setBg_bankList(bank_group_bo.getCurBgbkListByOP(login_form.getUserData().getUSER_COMPANY() , eachsysstatustab_bo.getRptBusinessDate()));
		}
		Rptcl_1_Form.setBIZDATE(eachsysstatustab_bo.getRptBusinessDate());
//		Rptcl_1_Form.setBg_bankList(rptcl_1_bo.getBgbkIdList());
//		Rptcl_1_Form.setOpt_bankList(rptcl_1_bo.getOptBgbkIdList());
		Rptcl_1_Form.setOpt_bankList(bank_group_bo.getOpbkList());
		System.out.println("txdt>>"+Rptcl_1_Form.getTXDT());
		target = StrUtils.isEmpty(Rptcl_1_Form.getTarget())?"":Rptcl_1_Form.getTarget();
		return mapping.findForward(target);
	}

	public RPTCL_1_BO getRptcl_1_bo() {
		return rptcl_1_bo;
	}

	public void setRptcl_1_bo(RPTCL_1_BO rptcl_1_bo) {
		this.rptcl_1_bo = rptcl_1_bo;
	}

	public EACHSYSSTATUSTAB_BO getEachsysstatustab_bo() {
		return eachsysstatustab_bo;
	}

	public void setEachsysstatustab_bo(EACHSYSSTATUSTAB_BO eachsysstatustab_bo) {
		this.eachsysstatustab_bo = eachsysstatustab_bo;
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

	



	
	
}
