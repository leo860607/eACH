package tw.org.twntch.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import tw.org.twntch.bo.Arguments;
import tw.org.twntch.bo.BANK_GROUP_BO;
import tw.org.twntch.bo.BUSINESS_TYPE_BO;
import tw.org.twntch.bo.EACHSYSSTATUSTAB_BO;
import tw.org.twntch.bo.RPTST_15_BO;
import tw.org.twntch.bo.RPTST_8_BO;
import tw.org.twntch.form.Rptst_15_Form;
import tw.org.twntch.form.Rptst_8_Form;
import tw.org.twntch.util.BeanUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.zDateHandler;

public class RPTST_15_Action extends GenericAction {
	private RPTST_15_BO rptst_15_bo ;
	private EACHSYSSTATUSTAB_BO eachsysstatustab_bo;
	private BANK_GROUP_BO bank_group_bo;
	private BUSINESS_TYPE_BO business_type_bo;
	
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Rptst_15_Form rptst_15_form = (Rptst_15_Form) form;
		String ac_key = StrUtils.isEmpty(rptst_15_form.getAc_key())?"":rptst_15_form.getAc_key();
		String target = StrUtils.isEmpty(rptst_15_form.getTarget())?"search":rptst_15_form.getTarget();
		rptst_15_form.setTarget(target);
		System.out.println("RPTST_15_Action is start >> " + ac_key);
		
		if(ac_key.startsWith("search")){
			String ext = ac_key.split("_")[1];
			Map map = null;
			if(ext.equals("xls")){
				map = rptst_15_bo.ex_export(ext,
						rptst_15_form.getTXDATE(),
						rptst_15_form.getBUSINESS_TYPE_ID(),
						rptst_15_form.getOPBK_ID(),
						rptst_15_form.getSerchStrs()
				);
			}else if(ext.equals("pdf")){
				map = rptst_15_bo.export(ext,
						rptst_15_form.getTXDATE(),
						rptst_15_form.getBUSINESS_TYPE_ID(),
						rptst_15_form.getOPBK_ID(),
						rptst_15_form.getSerchStrs()
				);
			}
			BeanUtils.populate(rptst_15_form, map);
			//System.out.println(BeanUtils.describe(rptst_15_form));
			if(map.get("result").equals("TRUE") && !Arguments.getStringArg("ISWINOPEN").equals("Y")){
				exportFile2Client(response, map.get("msg").toString(), zDateHandler.getDateNum()+"_st_15." + ext, rptst_15_form.getDow_token() );
				return null;
			}
		}
		if(StrUtils.isEmpty(ac_key)){
			String bsDate = eachsysstatustab_bo.getRptBusinessDate();
			rptst_15_form.setTXDATE(bsDate);
		}
		rptst_15_form.setPcodeList(business_type_bo.getBsTypeIdList());
		rptst_15_form.setOpbkIdList(rptst_15_bo.getOpbkIdList());
		
		target = StrUtils.isEmpty(rptst_15_form.getTarget())?"":rptst_15_form.getTarget();
		return mapping.findForward(target);
	}
	
	public RPTST_15_BO getRptst_15_bo() {
		return rptst_15_bo;
	}

	public void setRptst_15_bo(RPTST_15_BO rptst_15_bo) {
		this.rptst_15_bo = rptst_15_bo;
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

	public BUSINESS_TYPE_BO getBusiness_type_bo() {
		return business_type_bo;
	}

	public void setBusiness_type_bo(BUSINESS_TYPE_BO business_type_bo) {
		this.business_type_bo = business_type_bo;
	}
	
}
