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
import tw.org.twntch.bo.RPTFEE_1_BO;
import tw.org.twntch.form.Login_Form;
import tw.org.twntch.form.Rptfee_1_Form;
import tw.org.twntch.util.BeanUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.WebServletUtils;
import tw.org.twntch.util.zDateHandler;

public class RPTFEE_1_Action extends GenericAction {
	private RPTFEE_1_BO rptfee_1_bo;
	private BANK_GROUP_BO bank_group_bo;
	private EACHSYSSTATUSTAB_BO eachsysstatustab_bo;
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Rptfee_1_Form rptfee_1_form = (Rptfee_1_Form) form;
		String ac_key = StrUtils.isEmpty(rptfee_1_form.getAc_key())?"":rptfee_1_form.getAc_key();
		String target = StrUtils.isEmpty(rptfee_1_form.getTarget())?"search":rptfee_1_form.getTarget();
		rptfee_1_form.setTarget(target);
		System.out.println("RPTFEE_1_Action is start >> " + ac_key);
		
		Login_Form login_form = (Login_Form) WebServletUtils.getRequest().getSession().getAttribute("login_form");
		
		if(ac_key.startsWith("search")){
//			銀行端要多加一道檢核
			if(login_form.getUserData().getUSER_TYPE().equals("B")){
				Map retmap = super.checkRPT_BizDate(rptfee_1_form.getSTART_DATE(), rptfee_1_form.getCLEARINGPHASE());
				if(retmap.get("result").equals("FALSE") ){
					BeanUtils.populate(rptfee_1_form, retmap);
					return mapping.findForward(target);
				}
			}
			String ext = ac_key.split("_")[1];
			Map map = null;
			if(ext.equals("xls")){
				map = rptfee_1_bo.ex_export(ext, rptfee_1_form.getSTART_DATE(), rptfee_1_form.getEND_DATE(), rptfee_1_form.getCLEARINGPHASE(), rptfee_1_form.getOPBK_ID(), rptfee_1_form.getBGBK_ID(), rptfee_1_form.getBRBK_ID(), rptfee_1_form.getSerchStrs());
			}else if(ext.equals("pdf")){
				map = rptfee_1_bo.export(ext, rptfee_1_form.getSTART_DATE(), rptfee_1_form.getEND_DATE(), rptfee_1_form.getCLEARINGPHASE(), rptfee_1_form.getOPBK_ID(), rptfee_1_form.getBGBK_ID(), rptfee_1_form.getBRBK_ID(), rptfee_1_form.getSerchStrs());
			}
			BeanUtils.populate(rptfee_1_form, map);
			if(map.get("result").equals("TRUE") && !Arguments.getStringArg("ISWINOPEN").equals("Y")){
				exportFile2Client(response, map.get("msg").toString(), zDateHandler.getDateNum()+"_fee_1." + ext, rptfee_1_form.getDow_token() );
				return null;
			}
		}
		if(StrUtils.isEmpty(ac_key)){
			rptfee_1_form.setSTART_DATE(eachsysstatustab_bo.getRptBusinessDate());
			rptfee_1_form.setOpbkIdList(bank_group_bo.getOpbkList());
//			rptfee_1_form.setOpbkIdList(bank_group_bo.getOpbkList());
			
			if(login_form.getUserData().getUSER_TYPE().equalsIgnoreCase("B")){
				rptfee_1_form.setOPBK_ID(login_form.getUserData().getUSER_COMPANY());
			}
		}
		target = StrUtils.isEmpty(rptfee_1_form.getTarget())?"":rptfee_1_form.getTarget();
		return mapping.findForward(target);
	}
	public RPTFEE_1_BO getRptfee_1_bo() {
		return rptfee_1_bo;
	}
	public void setRptfee_1_bo(RPTFEE_1_BO rptfee_1_bo) {
		this.rptfee_1_bo = rptfee_1_bo;
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
