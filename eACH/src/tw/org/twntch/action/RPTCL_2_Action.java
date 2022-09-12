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
import tw.org.twntch.bo.RPTCL_2_BO;
import tw.org.twntch.db.dao.hibernate.BANK_GROUP_Dao;
import tw.org.twntch.form.Login_Form;
import tw.org.twntch.form.Rptcl_2_Form;
import tw.org.twntch.po.BANK_GROUP;
import tw.org.twntch.util.BeanUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.WebServletUtils;
import tw.org.twntch.util.zDateHandler;

public class RPTCL_2_Action extends GenericAction {
	private RPTCL_2_BO rptcl_2_bo ;
	private BANK_GROUP_BO bank_group_bo ;
	private BANK_GROUP_Dao bank_group_Dao ;
	private EACHSYSSTATUSTAB_BO eachsysstatustab_bo ;
	
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Rptcl_2_Form Rptcl_2_Form = (Rptcl_2_Form) form;
		String ac_key = StrUtils.isEmpty(Rptcl_2_Form.getAc_key())?"":Rptcl_2_Form.getAc_key();
		String target = StrUtils.isEmpty(Rptcl_2_Form.getTarget())?"search":Rptcl_2_Form.getTarget();
		Rptcl_2_Form.setTarget(target);
		Map map = null;
		System.out.println("RPTCL_2_Action is start >> " + ac_key);
		Login_Form login_form = (Login_Form) WebServletUtils.getRequest().getSession().getAttribute("login_form");
		if(login_form.getUserData().getUSER_TYPE().equals("B")){
			BANK_GROUP po =  bank_group_Dao.get(login_form.getUserData().getUSER_COMPANY());
			Rptcl_2_Form.setOpt_bank(login_form.getUserData().getUSER_COMPANY()+"-"+po.getBGBK_NAME());
//			Rptcl_2_Form.setOpt_id(login_form.getUserData().getUSER_COMPANY());
			Rptcl_2_Form.setOPBK_ID(login_form.getUserData().getUSER_COMPANY());
			Rptcl_2_Form.setBg_bankList(bank_group_bo.getCurBgbkListByOP(login_form.getUserData().getUSER_COMPANY() , eachsysstatustab_bo.getRptBusinessDate()));
		}
		if(ac_key.equalsIgnoreCase("search")){
//			銀行端要多加一道檢核
			if(login_form.getUserData().getUSER_TYPE().equals("B")){
//				Map retmap = super.checkRPTCL_BizDate(Rptcl_2_Form.getBIZDATE(), Rptcl_2_Form.getCLEARINGPHASE());
				Map retmap = super.checkRPT_BizDate(Rptcl_2_Form.getBIZDATE(), Rptcl_2_Form.getCLEARINGPHASE());
				if(retmap.get("result").equals("FALSE") ){
					BeanUtils.populate(Rptcl_2_Form, retmap);
					return mapping.findForward(target);
				}
			}
			System.out.println("Rptcl_2_Form.getOPBK_ID()>>"+Rptcl_2_Form.getOPBK_ID());
			map = rptcl_2_bo.export(Rptcl_2_Form.getBIZDATE(), Rptcl_2_Form.getBGBK_ID(), Rptcl_2_Form.getCLEARINGPHASE() ,Rptcl_2_Form.getOPBK_ID(), Rptcl_2_Form.getSerchStrs());//e
			BeanUtils.populate(Rptcl_2_Form, map);
			if(map.get("result").equals("TRUE") && !Arguments.getStringArg("ISWINOPEN").equals("Y")){
				exportFile2Client(response, map.get("msg").toString(), zDateHandler.getDateNum()+"_cl_2.pdf" ,Rptcl_2_Form.getDow_token() );
				return null;
			}
		}
		if(StrUtils.isEmpty(ac_key)){
		}
		
		Rptcl_2_Form.setBIZDATE(eachsysstatustab_bo.getRptBusinessDate());
		//Rptcl_2_Form.setOpt_bankList(rptcl_2_bo.getOptBgbkIdList());
		Rptcl_2_Form.setOpt_bankList(bank_group_bo.getOpbkList());
//		分權限 票交端
//		if(login_form.getUserData().getUSER_TYPE().equals("A")){
//			Rptcl_2_Form.setBg_bankList(rptcl_2_bo.getBgbkIdList());
//			Rptcl_2_Form.setOpt_bankList(rptcl_2_bo.getOptBgbkIdList());
//		}
		
		System.out.println("txdt>>"+Rptcl_2_Form.getTXDT());
		target = StrUtils.isEmpty(Rptcl_2_Form.getTarget())?"":Rptcl_2_Form.getTarget();
		return mapping.findForward(target);
	}




	public RPTCL_2_BO getRptcl_2_bo() {
		return rptcl_2_bo;
	}

	public void setRptcl_2_bo(RPTCL_2_BO rptcl_2_bo) {
		this.rptcl_2_bo = rptcl_2_bo;
	}


	public BANK_GROUP_Dao getBank_group_Dao() {
		return bank_group_Dao;
	}

	public void setBank_group_Dao(BANK_GROUP_Dao bank_group_Dao) {
		this.bank_group_Dao = bank_group_Dao;
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
