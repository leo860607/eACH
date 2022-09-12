package tw.org.twntch.action;


import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import tw.org.twntch.bo.Arguments;
import tw.org.twntch.bo.BANK_GROUP_BO;
import tw.org.twntch.bo.EACHSYSSTATUSTAB_BO;
import tw.org.twntch.bo.RPTFEE_5_BO;
import tw.org.twntch.form.Login_Form;
import tw.org.twntch.form.Rptfee_5_Form;
import tw.org.twntch.po.BANK_GROUP;
import tw.org.twntch.util.BeanUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.zDateHandler;

public class RPTFEE_5_Action extends GenericAction {
	private BANK_GROUP_BO bank_group_bo;
	private EACHSYSSTATUSTAB_BO eachsysstatustab_bo;
	private RPTFEE_5_BO rptfee_5_bo;
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Rptfee_5_Form rptfee_5_form = (Rptfee_5_Form) form;
		String ac_key = StrUtils.isEmpty(rptfee_5_form.getAc_key())?"":rptfee_5_form.getAc_key();
		String target = StrUtils.isEmpty(rptfee_5_form.getTarget())?"search":rptfee_5_form.getTarget();
		rptfee_5_form.setTarget(target);
		System.out.println("RPTFEE_5_Action is start >> " + ac_key);
		
		Login_Form login_form = (Login_Form) request.getSession().getAttribute("login_form");
		Map userData = BeanUtils.describe(login_form.getUserData());
		//銀行端預設帶入所屬操作行、總行代號
		if(((String)userData.get("USER_TYPE")).equals("B")){
			List<BANK_GROUP> tmp = bank_group_bo.search((String)userData.get("USER_COMPANY"));
			if(tmp != null && tmp.size() > 0){
				rptfee_5_form.setOPBK_ID(tmp.get(0).getOPBK_ID());
			}
			//rptfee_5_form.setBGBK_ID(login_form.getUserCompany());
		}
		if(ac_key.startsWith("search")){
//			銀行端要多加一道檢核
			if(login_form.getUserData().getUSER_TYPE().equals("B")){
				Map retmap = super.checkRPT_BizDate(rptfee_5_form.getBIZDATE(), rptfee_5_form.getCLEARINGPHASE());
				if(retmap.get("result").equals("FALSE") ){
					BeanUtils.populate(rptfee_5_form, retmap);
					return mapping.findForward(target);
				}
			}
			String ext = ac_key.split("_")[1];
			Map map = null;
			if(ext.equals("xls")){
				map = rptfee_5_bo.ex_export(ext, rptfee_5_form.getBIZDATE(), rptfee_5_form.getCLEARINGPHASE(), rptfee_5_form.getOPBK_ID(), rptfee_5_form.getBGBK_ID(), rptfee_5_form.getBRBK_ID(), rptfee_5_form.getSerchStrs());
			}else if(ext.equals("pdf")){
				map = rptfee_5_bo.export(ext, rptfee_5_form.getBIZDATE(), rptfee_5_form.getCLEARINGPHASE(), rptfee_5_form.getOPBK_ID(), rptfee_5_form.getBGBK_ID(), rptfee_5_form.getBRBK_ID(), rptfee_5_form.getSerchStrs());
			}else if(ext.equals("csv")){
				map = rptfee_5_bo.csv_export(ext, rptfee_5_form.getBIZDATE(), rptfee_5_form.getCLEARINGPHASE(), rptfee_5_form.getOPBK_ID(), rptfee_5_form.getBGBK_ID(), rptfee_5_form.getBRBK_ID(), rptfee_5_form.getSerchStrs());
			}
			BeanUtils.populate(rptfee_5_form, map);
			if(map.get("result").equals("TRUE") && !Arguments.getStringArg("ISWINOPEN").equals("Y")){
				exportFile2Client(response, map.get("msg").toString(), zDateHandler.getDateNum()+"_fee_5." + ext, rptfee_5_form.getDow_token() );
				return null;
			}
		}
		if(StrUtils.isEmpty(ac_key)){
			rptfee_5_form.setBIZDATE(eachsysstatustab_bo.getRptBusinessDate());
		}
		
		
		
		//rptfee_5_form.setBgbkIdList(bank_group_bo.getBgbkIdList());
		rptfee_5_form.setOpbkIdList(bank_group_bo.getOpbkList());
//		rptfee_5_form.setOpbkIdList(bank_group_bo.getOpbkList());
		target = StrUtils.isEmpty(rptfee_5_form.getTarget())?"":rptfee_5_form.getTarget();
		return mapping.findForward(target);
	}
	public BANK_GROUP_BO getBank_group_bo() {
		return bank_group_bo;
	}
	public void setBank_group_bo(BANK_GROUP_BO bank_group_bo) {
		this.bank_group_bo = bank_group_bo;
	}
	public RPTFEE_5_BO getRptfee_5_bo() {
		return rptfee_5_bo;
	}
	public void setRptfee_5_bo(RPTFEE_5_BO rptfee_5_bo) {
		this.rptfee_5_bo = rptfee_5_bo;
	}
	public EACHSYSSTATUSTAB_BO getEachsysstatustab_bo() {
		return eachsysstatustab_bo;
	}
	public void setEachsysstatustab_bo(EACHSYSSTATUSTAB_BO eachsysstatustab_bo) {
		this.eachsysstatustab_bo = eachsysstatustab_bo;
	}
	
}
