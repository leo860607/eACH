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
import tw.org.twntch.bo.RPTTX_4_BO;
import tw.org.twntch.db.dao.hibernate.BANK_GROUP_Dao;
import tw.org.twntch.form.Login_Form;
import tw.org.twntch.form.Rpttx_4_Form;
import tw.org.twntch.po.BANK_GROUP;
import tw.org.twntch.util.BeanUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.WebServletUtils;
import tw.org.twntch.util.zDateHandler;

public class RPTTX_4_Action extends GenericAction {
	private RPTTX_4_BO rpttx_4_bo ;
	private BANK_GROUP_BO bank_group_bo ; 
	private BANK_GROUP_Dao bank_group_Dao ;
	private EACHSYSSTATUSTAB_BO eachsysstatustab_bo ;
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Rpttx_4_Form rpttx_4_form = (Rpttx_4_Form) form;
		String ac_key = StrUtils.isEmpty(rpttx_4_form.getAc_key())?"":rpttx_4_form.getAc_key();
		String target = StrUtils.isEmpty(rpttx_4_form.getTarget())?"search":rpttx_4_form.getTarget();
		rpttx_4_form.setTarget(target);
		Map map = null ;
		System.out.println("RPTTX_4_Action is start >> " + ac_key);
		Login_Form login_form = (Login_Form) WebServletUtils.getRequest().getSession().getAttribute("login_form");
		
		if(login_form.getUserData().getUSER_TYPE().equals("B")){
			BANK_GROUP po =  bank_group_Dao.get(login_form.getUserData().getUSER_COMPANY());
			rpttx_4_form.setOpt_bank(login_form.getUserData().getUSER_COMPANY()+"-"+po.getBGBK_NAME());
			rpttx_4_form.setOpt_id(login_form.getUserData().getUSER_COMPANY());
			rpttx_4_form.setBgbkIdList(bank_group_bo.getCurBgbkListByOP(login_form.getUserData().getUSER_COMPANY() , eachsysstatustab_bo.getRptBusinessDate()) );
		}else{
			rpttx_4_form.setOpt_bankList(bank_group_bo.getOpbkList());
		}
		
		if(ac_key.equalsIgnoreCase("search")){
			//銀行端要多加一道檢核
			if(login_form.getUserData().getUSER_TYPE().equals("B")){
				Map retmap = super.checkRPT_BizDate(rpttx_4_form.getEND_DATE(), rpttx_4_form.getCLEARINGPHASE());
				if(retmap.get("result").equals("FALSE") ){
					BeanUtils.populate(rpttx_4_form, retmap);
					return mapping.findForward(target);
				}
			}
			
			map = rpttx_4_bo.export(
				rpttx_4_form.getSTART_DATE()
				, rpttx_4_form.getEND_DATE()
				, rpttx_4_form.getFLBATCHSEQ()
				, rpttx_4_form.getOpt_id()
				, rpttx_4_form.getBGBK_ID()
				, rpttx_4_form.getCLEARINGPHASE()
				, rpttx_4_form.getOpt_bank()
				, rpttx_4_form.getSerchStrs()
			);
			
			BeanUtils.populate(rpttx_4_form, map);
			if(map.get("result").equals("TRUE") && !Arguments.getStringArg("ISWINOPEN").equals("Y")){
				exportFile2Client(response, map.get("msg").toString(), zDateHandler.getDateNum()+"_tx_4.pdf" ,rpttx_4_form.getDow_token() );
				return null;
			}
		}
		
		String bizdate = eachsysstatustab_bo.getRptBusinessDate();
		rpttx_4_form.setSTART_DATE(bizdate);
		rpttx_4_form.setEND_DATE(bizdate);
		
		target = StrUtils.isEmpty(rpttx_4_form.getTarget())?"":rpttx_4_form.getTarget();
		return mapping.findForward(target);
	}
	
	public BANK_GROUP_Dao getBank_group_Dao() {
		return bank_group_Dao;
	}
	public RPTTX_4_BO getRpttx_4_bo() {
		return rpttx_4_bo;
	}
	public void setRpttx_4_bo(RPTTX_4_BO rpttx_4_bo) {
		this.rpttx_4_bo = rpttx_4_bo;
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
	public BANK_GROUP_BO getBank_group_bo() {
		return bank_group_bo;
	}
	public void setBank_group_bo(BANK_GROUP_BO bank_group_bo) {
		this.bank_group_bo = bank_group_bo;
	}
}
