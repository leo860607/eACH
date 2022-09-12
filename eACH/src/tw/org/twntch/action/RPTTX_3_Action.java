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
import tw.org.twntch.bo.RPTTX_3_BO;
import tw.org.twntch.db.dao.hibernate.BANK_GROUP_Dao;
import tw.org.twntch.form.Login_Form;
import tw.org.twntch.form.Rpttx_3_Form;
import tw.org.twntch.po.BANK_GROUP;
import tw.org.twntch.util.BeanUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.WebServletUtils;
import tw.org.twntch.util.zDateHandler;

public class RPTTX_3_Action extends GenericAction {
	private RPTTX_3_BO rpttx_3_bo ;
	private BANK_GROUP_BO bank_group_bo ; 
	private BANK_GROUP_Dao bank_group_Dao ;
	private EACHSYSSTATUSTAB_BO eachsysstatustab_bo ;
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Rpttx_3_Form Rpttx_3_Form = (Rpttx_3_Form) form;
		Map map = null;
		String ac_key = StrUtils.isEmpty(Rpttx_3_Form.getAc_key())?"":Rpttx_3_Form.getAc_key();
		String target = StrUtils.isEmpty(Rpttx_3_Form.getTarget())?"search":Rpttx_3_Form.getTarget();
		Rpttx_3_Form.setTarget(target);
		System.out.println("RPTTX_3_Action is start >> " + ac_key);
		Login_Form login_form = (Login_Form) WebServletUtils.getRequest().getSession().getAttribute("login_form");
		if(login_form.getUserData().getUSER_TYPE().equals("B")){
			BANK_GROUP po =  bank_group_Dao.get(login_form.getUserData().getUSER_COMPANY());
			Rpttx_3_Form.setOpt_bank(login_form.getUserData().getUSER_COMPANY()+"-"+po.getBGBK_NAME());
			Rpttx_3_Form.setOpt_id(login_form.getUserData().getUSER_COMPANY());
			Rpttx_3_Form.setBgbkIdList(bank_group_bo.getCurBgbkListByOP(login_form.getUserData().getUSER_COMPANY() , eachsysstatustab_bo.getRptBusinessDate()));
		}
		if(ac_key.equalsIgnoreCase("search")){
//			銀行端要多加一道檢核
			if(login_form.getUserData().getUSER_TYPE().equals("B")){
				Map retmap = super.checkRPT_BizDate(Rpttx_3_Form.getBIZDATE(), Rpttx_3_Form.getCLEARINGPHASE());
				if(retmap.get("result").equals("FALSE") ){
					BeanUtils.populate(Rpttx_3_Form, retmap);
					return mapping.findForward(target);
				}
			}
			map = rpttx_3_bo.export(Rpttx_3_Form.getBIZDATE(),Rpttx_3_Form.getOpt_id() , Rpttx_3_Form.getBGBK_ID(), Rpttx_3_Form.getCLEARINGPHASE() ,Rpttx_3_Form.getOpt_bank(), Rpttx_3_Form.getOpt_type(), Rpttx_3_Form.getSerchStrs());
			BeanUtils.populate(Rpttx_3_Form, map);
			if(map.get("result").equals("TRUE") && !Arguments.getStringArg("ISWINOPEN").equals("Y")){
				exportFile2Client(response, map.get("msg").toString(), zDateHandler.getDateNum()+"_tx_3.pdf" ,Rpttx_3_Form.getDow_token() );
				return null;
			}
		}
		if(StrUtils.isEmpty(ac_key)){
		}
		
		Rpttx_3_Form.setBIZDATE(eachsysstatustab_bo.getRptBusinessDate()); 
//		Rpttx_3_Form.setOpt_bankList(rpttx_3_bo.getBgbkIdList());
		Rpttx_3_Form.setOpt_bankList(bank_group_bo.getOpbkList());
		System.out.println("txdt>>"+Rpttx_3_Form.getTXDT());
		target = StrUtils.isEmpty(Rpttx_3_Form.getTarget())?"":Rpttx_3_Form.getTarget();
		return mapping.findForward(target);
	}
	public RPTTX_3_BO getRpttx_3_bo() {
		return rpttx_3_bo;
	}
	public void setRpttx_3_bo(RPTTX_3_BO rpttx_3_bo) {
		this.rpttx_3_bo = rpttx_3_bo;
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

	



	
	
}
