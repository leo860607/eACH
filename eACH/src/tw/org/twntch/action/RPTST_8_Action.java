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
import tw.org.twntch.bo.RPTST_8_BO;
import tw.org.twntch.form.Rptst_8_Form;
import tw.org.twntch.util.BeanUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.zDateHandler;

public class RPTST_8_Action extends GenericAction {
	private RPTST_8_BO rptst_8_bo ;
	private EACHSYSSTATUSTAB_BO eachsysstatustab_bo;
	private BANK_GROUP_BO bank_group_bo;
	
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Rptst_8_Form rptst_8_form = (Rptst_8_Form) form;
		String ac_key = StrUtils.isEmpty(rptst_8_form.getAc_key())?"":rptst_8_form.getAc_key();
		String target = StrUtils.isEmpty(rptst_8_form.getTarget())?"search":rptst_8_form.getTarget();
		rptst_8_form.setTarget(target);
		System.out.println("RPTST_8_Action is start >> " + ac_key);
		
		if(ac_key.startsWith("search")){
			String ext = ac_key.split("_")[1];
			Map map = null;
			if(ext.equals("xls")){
				map = rptst_8_bo.ex_export(ext,
						rptst_8_form.getTXDATE(),
						rptst_8_form.getPCODE(),
						rptst_8_form.getOPBK_ID(),
						rptst_8_form.getBGBK_ID(),
						rptst_8_form.getCLEARINGPHASE(),
						rptst_8_form.getSerchStrs()
				);
			}else if(ext.equals("pdf")){
				map = rptst_8_bo.export(ext,
						rptst_8_form.getTXDATE(),
						rptst_8_form.getPCODE(),
						rptst_8_form.getOPBK_ID(),
						rptst_8_form.getBGBK_ID(),
						rptst_8_form.getCLEARINGPHASE(),
						rptst_8_form.getSerchStrs()
				);
			}
			BeanUtils.populate(rptst_8_form, map);
			//System.out.println(BeanUtils.describe(rptst_8_form));
			if(map.get("result").equals("TRUE") && !Arguments.getStringArg("ISWINOPEN").equals("Y")){
				exportFile2Client(response, map.get("msg").toString(), zDateHandler.getDateNum()+"_st_8." + ext, rptst_8_form.getDow_token() );
				return null;
			}
		}
		if(StrUtils.isEmpty(ac_key)){
			String bsDate = eachsysstatustab_bo.getRptBusinessDate();
			rptst_8_form.setTXDATE(bsDate);
			rptst_8_form.setPcodeList(rptst_8_bo.getPcodeList());
			rptst_8_form.setOpbkIdList(rptst_8_bo.getOpbkIdList());
		}
		target = StrUtils.isEmpty(rptst_8_form.getTarget())?"":rptst_8_form.getTarget();
		return mapping.findForward(target);
	}
	
	public String getLastMonth(String bsDate_month){
		int int_month = Integer.parseInt(bsDate_month)-1;
		String month = Integer.toString(int_month);
		if(month.length() < 2){
			month = "0"+month;
		}
		return month;
	}


	public RPTST_8_BO getRptst_8_bo() {
		return rptst_8_bo;
	}

	public void setRptst_8_bo(RPTST_8_BO rptst_8_bo) {
		this.rptst_8_bo = rptst_8_bo;
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
