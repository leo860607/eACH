package tw.org.twntch.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import tw.org.twntch.bo.Arguments;
import tw.org.twntch.bo.BUSINESS_TYPE_BO;
import tw.org.twntch.bo.EACHSYSSTATUSTAB_BO;
import tw.org.twntch.bo.RPTST_10_BO;
import tw.org.twntch.form.Rptst_10_Form;
import tw.org.twntch.util.BeanUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.zDateHandler;

public class RPTST_10_Action extends GenericAction {
	private EACHSYSSTATUSTAB_BO eachsysstatustab_bo;
	private BUSINESS_TYPE_BO business_type_bo;
	private RPTST_10_BO rptst_10_bo;
	
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Rptst_10_Form rptst_10_form = (Rptst_10_Form) form;
		String ac_key = StrUtils.isEmpty(rptst_10_form.getAc_key())?"":rptst_10_form.getAc_key();
		String target = StrUtils.isEmpty(rptst_10_form.getTarget())?"search":rptst_10_form.getTarget();
		rptst_10_form.setTarget(target);
		System.out.println("RPTST_10_Action is start >> " + ac_key);
		
		if(ac_key.startsWith("search")){
			String ext = ac_key.split("_")[1];
			Map map = null;
			if(ext.equals("xls")){
				map = rptst_10_bo.ex_export(ext,
						rptst_10_form.getTW_YEAR(),
						rptst_10_form.getTW_MONTH(),
						rptst_10_form.getBUSINESS_TYPE_ID(),
						rptst_10_form.getSerchStrs()
				);
			}else if(ext.equals("pdf")){
				map = rptst_10_bo.export(ext,
						rptst_10_form.getTW_YEAR(),
						rptst_10_form.getTW_MONTH(),
						rptst_10_form.getBUSINESS_TYPE_ID(),
						rptst_10_form.getSerchStrs()
				);
			}
			BeanUtils.populate(rptst_10_form, map);
			if(map.get("result").equals("TRUE") && !Arguments.getStringArg("ISWINOPEN").equals("Y")){
				exportFile2Client(response, map.get("msg").toString(), zDateHandler.getDateNum()+"_st_10." + ext, rptst_10_form.getDow_token() );
				return null;
			}
		}
		
		if(StrUtils.isEmpty(ac_key)){
			String bsDate = eachsysstatustab_bo.getRptBusinessDate();
			rptst_10_form.setTW_YEAR(bsDate.substring(0, 4));
			rptst_10_form.setTW_MONTH(bsDate.substring(4,  6));
		}
		rptst_10_form.setPcodeList(business_type_bo.getBsTypeIdList());
		
		target = StrUtils.isEmpty(rptst_10_form.getTarget())?"":rptst_10_form.getTarget();
		return mapping.findForward(target);
	}
	
	public EACHSYSSTATUSTAB_BO getEachsysstatustab_bo() {
		return eachsysstatustab_bo;
	}

	public void setEachsysstatustab_bo(EACHSYSSTATUSTAB_BO eachsysstatustab_bo) {
		this.eachsysstatustab_bo = eachsysstatustab_bo;
	}
	
	public BUSINESS_TYPE_BO getBusiness_type_bo() {
		return business_type_bo;
	}

	public void setBusiness_type_bo(BUSINESS_TYPE_BO business_type_bo) {
		this.business_type_bo = business_type_bo;
	}
	
	public RPTST_10_BO getRptst_10_bo() {
		return rptst_10_bo;
	}

	public void setRptst_10_bo(RPTST_10_BO rptst_10_bo) {
		this.rptst_10_bo = rptst_10_bo;
	}
}
