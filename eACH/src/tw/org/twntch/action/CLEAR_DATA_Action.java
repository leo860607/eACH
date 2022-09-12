package tw.org.twntch.action;



import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import tw.org.twntch.bo.Arguments;
import tw.org.twntch.bo.BANK_GROUP_BO;
import tw.org.twntch.bo.CLEAR_DATA_BO;
import tw.org.twntch.bo.EACHSYSSTATUSTAB_BO;
import tw.org.twntch.form.Clear_Data_Form;
import tw.org.twntch.form.Login_Form;
import tw.org.twntch.util.BeanUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.WebServletUtils;
import tw.org.twntch.util.zDateHandler;

public class CLEAR_DATA_Action extends GenericAction {
	private CLEAR_DATA_BO clear_data_bo;
	private BANK_GROUP_BO bank_group_bo;
	private EACHSYSSTATUSTAB_BO eachsysstatustab_bo;
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Clear_Data_Form clear_data_form = (Clear_Data_Form) form;
		String ac_key = StrUtils.isEmpty(clear_data_form.getAc_key())?"":clear_data_form.getAc_key();
		String target = StrUtils.isEmpty(clear_data_form.getTarget())?"search":clear_data_form.getTarget();
		Login_Form login_form = (Login_Form) WebServletUtils.getRequest().getSession().getAttribute("login_form");
		clear_data_form.setTarget(target);
		System.out.println("CLEAR_DATA_Action is start >> " + ac_key);
		
		if(ac_key.equalsIgnoreCase("edit")){
			clear_data_form.setDetailData(clear_data_bo.getDetail(clear_data_form.getSrch_BIZDATE(), clear_data_form.getSrch_CLEARINGPHASE(), clear_data_form.getSrch_BANKID(), clear_data_form.getSrch_PCODE()));
		}
		if(ac_key.equalsIgnoreCase("update")){
		}
		if(ac_key.equalsIgnoreCase("add")){
		}
		if(ac_key.equalsIgnoreCase("export")){
			Map map = clear_data_bo.qs_ex_export(
					clear_data_form.getSTART_DATE(), 
					clear_data_form.getEND_DATE(),
					clear_data_form.getPCODE(),
					clear_data_form.getOPBK_ID(),
					clear_data_form.getCTBK_ID(),
					clear_data_form.getBGBK_ID(),
					clear_data_form.getCLEARINGPHASE(),
					clear_data_form.getSerchStrs(),
					clear_data_form.getSortname(),
					clear_data_form.getSortorder()
			);
			BeanUtils.populate(clear_data_form, map);
			if(map.get("result").equals("TRUE") && !Arguments.getStringArg("ISWINOPEN").equals("Y")){
				exportFile2Client(response, map.get("msg").toString(), zDateHandler.getDateNum()+"_clear_data.xls" ,clear_data_form.getDow_token() );
				return null;
			}else{
				//交易代號清單
				clear_data_form.setPcodeList(clear_data_bo.getPcodeList());
			}
		}
		if(StrUtils.isEmpty(ac_key) || ac_key.equalsIgnoreCase("back")){
			//交易代號清單
			clear_data_form.setPcodeList(clear_data_bo.getPcodeList());
			//清單由網頁用AJAX取得
			//clear_data_form.setOpbkIdList(clear_data_bo.getOpbkIdList());
			//clear_data_form.setCtbkIdList(bank_group_bo.getCtbkIdList());
			//將營業日塞到頁面的日期控制項
			String bizDate = eachsysstatustab_bo.getBusinessDate();
			clear_data_form.setBIZDATE(bizDate);
			clear_data_form.setSTART_DATE(bizDate);
			clear_data_form.setEND_DATE(bizDate);
			
			//銀行端預設帶入操作行
			if(login_form.getUserData().getUSER_TYPE().equals("B")){
			}
		}
		target = StrUtils.isEmpty(clear_data_form.getTarget())?"":clear_data_form.getTarget();
		return mapping.findForward(target);
	}
	
	public CLEAR_DATA_BO getClear_data_bo() {
		return clear_data_bo;
	}
	public void setClear_data_bo(CLEAR_DATA_BO clear_data_bo) {
		this.clear_data_bo = clear_data_bo;
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
