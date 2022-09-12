package tw.org.twntch.action;


import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import tw.org.twntch.bo.Arguments;
import tw.org.twntch.bo.BANK_BRANCH_BO;
import tw.org.twntch.bo.BANK_GROUP_BO;
import tw.org.twntch.bo.EACHSYSSTATUSTAB_BO;
import tw.org.twntch.bo.HR_TXP_TIME_BO;
import tw.org.twntch.form.Hr_Txp_Time_Form;
import tw.org.twntch.form.Login_Form;
import tw.org.twntch.util.BeanUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.zDateHandler;

public class HR_TXP_TIME_Action extends GenericAction {
	private HR_TXP_TIME_BO hr_txp_time_bo;
	private BANK_BRANCH_BO bank_branch_bo;
	private BANK_GROUP_BO bank_group_bo;
	private EACHSYSSTATUSTAB_BO eachsysstatustab_bo;
	
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Hr_Txp_Time_Form hr_txp_time_form = (Hr_Txp_Time_Form) form;
		String ac_key = StrUtils.isEmpty(hr_txp_time_form.getAc_key())?"":hr_txp_time_form.getAc_key();
		String target = StrUtils.isEmpty(hr_txp_time_form.getTarget())?"search":hr_txp_time_form.getTarget();
		hr_txp_time_form.setTarget(target);
		Login_Form login_form = (Login_Form) request.getSession().getAttribute("login_form");
		
		System.out.println("FEE_SEARCH_Action is start >> " + ac_key);
		
		if(ac_key.equalsIgnoreCase("edit")){
		}
		if(ac_key.equalsIgnoreCase("update")){
		}
		if(ac_key.equalsIgnoreCase("add")){
		}
		if(ac_key.equalsIgnoreCase("export")){
			Map map = hr_txp_time_bo.qs_ex_export(
					hr_txp_time_form.getTXDATE(),
					hr_txp_time_form.getPCODE(),
					hr_txp_time_form.getOPBK_ID(),
					hr_txp_time_form.getBGBK_ID(),
					hr_txp_time_form.getCLEARINGPHASE(),
					hr_txp_time_form.getSerchStrs(),
					hr_txp_time_form.getSortname(),
					hr_txp_time_form.getSortorder()
			);
			BeanUtils.populate(hr_txp_time_form, map);
			if(map.get("result").equals("TRUE") && !Arguments.getStringArg("ISWINOPEN").equals("Y")){
				exportFile2Client(response, map.get("msg").toString(), zDateHandler.getDateNum()+"_hr_txp_time.xls" ,hr_txp_time_form.getDow_token() );
				return null;
			}
		}
		if(StrUtils.isEmpty(ac_key)){
			//將營業日塞到頁面的日期控制項
			String busDate = eachsysstatustab_bo.getBusinessDate();
			System.out.println("busDate="+busDate);
			hr_txp_time_form.setTXDATE(busDate);
		}
		//交易代號清單
		hr_txp_time_form.setPcodeList(hr_txp_time_bo.getPcodeList());
		//操作行代號清單
		hr_txp_time_form.setOpbkIdList(hr_txp_time_bo.getOpbkIdList());
		target = StrUtils.isEmpty(hr_txp_time_form.getTarget())?"":hr_txp_time_form.getTarget();
		return mapping.findForward(target);
	}
	public HR_TXP_TIME_BO getHr_txp_time_bo() {
		return hr_txp_time_bo;
	}
	public void setHr_txp_time_bo(HR_TXP_TIME_BO hr_txp_time_bo) {
		this.hr_txp_time_bo = hr_txp_time_bo;
	}
	public BANK_BRANCH_BO getBank_branch_bo() {
		return bank_branch_bo;
	}
	public void setBank_branch_bo(BANK_BRANCH_BO bank_branch_bo) {
		this.bank_branch_bo = bank_branch_bo;
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
