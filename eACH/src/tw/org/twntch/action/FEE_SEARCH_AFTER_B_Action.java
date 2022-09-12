
package tw.org.twntch.action;


import java.util.Calendar;
import java.util.Date;
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
import tw.org.twntch.bo.FEE_SEARCH_BO;
import tw.org.twntch.bo.RPTFEE_1_BO;
import tw.org.twntch.form.Fee_Search_Form;
import tw.org.twntch.form.Login_Form;
import tw.org.twntch.po.BANK_BRANCH;
import tw.org.twntch.util.BeanUtils;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.zDateHandler;

public class FEE_SEARCH_AFTER_B_Action extends GenericAction {
	private FEE_SEARCH_BO fee_search_bo;
	private BANK_BRANCH_BO bank_branch_bo;
	private BANK_GROUP_BO bank_group_bo;
	private EACHSYSSTATUSTAB_BO eachsysstatustab_bo;
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Fee_Search_Form fee_search_form = (Fee_Search_Form) form;
		String ac_key = StrUtils.isEmpty(fee_search_form.getAc_key())?"":fee_search_form.getAc_key();
		String target = StrUtils.isEmpty(fee_search_form.getTarget())?"search":fee_search_form.getTarget();
		fee_search_form.setTarget(target);
		Login_Form login_form = (Login_Form) request.getSession().getAttribute("login_form");
		
		System.out.println("FEE_SEARCH_Action is start >> " + ac_key);
		
		if(ac_key.equalsIgnoreCase("edit")){
		}
		if(ac_key.equalsIgnoreCase("update")){
		}
		if(ac_key.equalsIgnoreCase("add")){
		}
		if(ac_key.equalsIgnoreCase("export")){
			Map map = fee_search_bo.qs_ex_export_aftb(
					fee_search_form.getSTART_DATE(), 
					fee_search_form.getEND_DATE(),
					fee_search_form.getPCODE(),
					fee_search_form.getCLEARINGPHASE(), 
					fee_search_form.getOPBK_ID(), 
					fee_search_form.getBGBK_ID(), 
					fee_search_form.getBRBK_ID(), 
					fee_search_form.getSerchStrs(),
					fee_search_form.getSortname(),
					fee_search_form.getSortorder()
			);
			BeanUtils.populate(fee_search_form, map);
			if(map.get("result").equals("TRUE") && !Arguments.getStringArg("ISWINOPEN").equals("Y")){
				exportFile2Client(response, map.get("msg").toString(), zDateHandler.getDateNum()+"_fee_search_aftb.xls" ,fee_search_form.getDow_token() );
				return null;
			}else{
				//交易代號清單
				fee_search_form.setPcodeList(fee_search_bo.getPcodeList());
				//操作行代號清單
				fee_search_form.setOpbkIdList(bank_group_bo.getOpbkList());
				//總行代號清單
				fee_search_form.setBgbkIdList(fee_search_bo.getBgbkIdList());
				Map userData = BeanUtils.describe(login_form.getUserData());
				//銀行端預設帶入操作行、總行代號
				if(((String)userData.get("USER_TYPE")).equals("B")){
					fee_search_form.setOPBK_ID((String)userData.get("USER_COMPANY"));
				}
			}
		}
		if(StrUtils.isEmpty(ac_key)){
			//交易代號清單
			fee_search_form.setPcodeList(fee_search_bo.getPcodeList());
			//操作行代號清單
//			fee_search_form.setOpbkIdList(fee_search_bo.getOpbkIdList());
			fee_search_form.setOpbkIdList(bank_group_bo.getOpbkList());
			//總行代號清單
			fee_search_form.setBgbkIdList(fee_search_bo.getBgbkIdList());
			//將營業日塞到頁面的日期控制項
			String busDate = eachsysstatustab_bo.getBusinessDate();
//			Calendar cal = Calendar.getInstance();
//			Date today = cal.getTime();
			System.out.println("busDate="+busDate);
			fee_search_form.setSTART_DATE(busDate);
			fee_search_form.setEND_DATE(busDate);
			
			Map userData = BeanUtils.describe(login_form.getUserData());
			//銀行端預設帶入操作行、總行代號
			if(((String)userData.get("USER_TYPE")).equals("B")){
				fee_search_form.setOPBK_ID((String)userData.get("USER_COMPANY"));
				//fee_search_form.setBGBK_ID(po.getId().getBGBK_ID());
				
				//分行代號清單
				//fee_search_form.setBrbkIdList(fee_search_bo.getBrbkIdList(po.getId().getBGBK_ID()));
			}
		}
		target = StrUtils.isEmpty(fee_search_form.getTarget())?"":fee_search_form.getTarget();
		return mapping.findForward(target);
	}
	public FEE_SEARCH_BO getFee_search_bo() {
		return fee_search_bo;
	}
	public void setFee_search_bo(FEE_SEARCH_BO fee_search_bo) {
		this.fee_search_bo = fee_search_bo;
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
