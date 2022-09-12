package tw.org.twntch.action;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import tw.org.twntch.bo.Arguments;
import tw.org.twntch.bo.BANK_GROUP_BO;
import tw.org.twntch.bo.EACHSYSSTATUSTAB_BO;
import tw.org.twntch.bo.RPTTX_2_BO;
import tw.org.twntch.db.dao.hibernate.BANK_GROUP_Dao;
import tw.org.twntch.form.Login_Form;
import tw.org.twntch.form.Rpttx_2_Form;
import tw.org.twntch.po.BANK_GROUP;
import tw.org.twntch.util.BeanUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.WebServletUtils;
import tw.org.twntch.util.zDateHandler;

public class RPTTX_2_Action extends GenericAction {
	private RPTTX_2_BO rpttx_2_bo ;
	private BANK_GROUP_BO bank_group_bo ;
	private BANK_GROUP_Dao bank_group_Dao ;
	private EACHSYSSTATUSTAB_BO eachsysstatustab_bo ;
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Rpttx_2_Form rpttx_2_form = (Rpttx_2_Form) form;
		String ac_key = StrUtils.isEmpty(rpttx_2_form.getAc_key())?"":rpttx_2_form.getAc_key();
		String target = StrUtils.isEmpty(rpttx_2_form.getTarget())?"search":rpttx_2_form.getTarget();
		rpttx_2_form.setTarget(target);
		System.out.println("RPTTX_2_Action is start >> " + ac_key);
		Map map = null;
		Login_Form login_form = (Login_Form) WebServletUtils.getRequest().getSession().getAttribute("login_form");
		if(login_form.getUserData().getUSER_TYPE().equals("B")){
			BANK_GROUP po =  bank_group_Dao.get(login_form.getUserData().getUSER_COMPANY());
			rpttx_2_form.setOpt_bank(login_form.getUserData().getUSER_COMPANY()+"-"+po.getBGBK_NAME());
			rpttx_2_form.setOpt_id(login_form.getUserData().getUSER_COMPANY());
			rpttx_2_form.setBgbkIdList(bank_group_bo.getCurBgbkListByOP(login_form.getUserData().getUSER_COMPANY() ,eachsysstatustab_bo.getRptBusinessDate()));
		}
		if(ac_key.equalsIgnoreCase("search")){
//			銀行端要多加一道檢核
			if(login_form.getUserData().getUSER_TYPE().equals("B")){
				Map retmap = super.checkRPT_BizDate(rpttx_2_form.getBIZDATE(), rpttx_2_form.getCLEARINGPHASE());
				if(retmap.get("result").equals("FALSE") ){
					BeanUtils.populate(rpttx_2_form, retmap);
					return mapping.findForward(target);
				}
			}
			map = rpttx_2_bo.export(rpttx_2_form.getBIZDATE(),rpttx_2_form.getOpt_id() , rpttx_2_form.getBGBK_ID(), rpttx_2_form.getCLEARINGPHASE() ,rpttx_2_form.getOpt_bank(),rpttx_2_form.getOpt_type(), rpttx_2_form.getSerchStrs());
			BeanUtils.populate(rpttx_2_form, map);
			if(map.get("result").equals("TRUE") && !Arguments.getStringArg("ISWINOPEN").equals("Y")){
				exportFile2Client(response, map.get("msg").toString(), zDateHandler.getDateNum()+"_tx_2.pdf" ,rpttx_2_form.getDow_token() );
				return null;
			}
		}
		if(StrUtils.isEmpty(ac_key)){
		}
		
		rpttx_2_form.setBIZDATE(eachsysstatustab_bo.getRptBusinessDate()); 
//		rpttx_2_form.setOpt_bankList(rpttx_2_bo.getBgbkIdList());
		rpttx_2_form.setOpt_bankList(bank_group_bo.getOpbkList());
		System.out.println("txdt>>"+rpttx_2_form.getTXDT());
		target = StrUtils.isEmpty(rpttx_2_form.getTarget())?"":rpttx_2_form.getTarget();
		return mapping.findForward(target);
	}
	


	public RPTTX_2_BO getRpttx_2_bo() {
		return rpttx_2_bo;
	}


	public void setRpttx_2_bo(RPTTX_2_BO rpttx_2_bo) {
		this.rpttx_2_bo = rpttx_2_bo;
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
