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
import tw.org.twntch.bo.RPTST_14_BO;
import tw.org.twntch.bo.RPTST_15_BO;
import tw.org.twntch.bo.RPTST_8_BO;
import tw.org.twntch.db.dao.hibernate.BANK_GROUP_Dao;
import tw.org.twntch.form.Login_Form;
import tw.org.twntch.form.Rptst_14_Form;
import tw.org.twntch.form.Rptst_15_Form;
import tw.org.twntch.form.Rptst_8_Form;
import tw.org.twntch.po.BANK_GROUP;
import tw.org.twntch.util.BeanUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.WebServletUtils;
import tw.org.twntch.util.zDateHandler;

public class RPTST_14_Action extends GenericAction {
	private RPTST_14_BO rptst_14_bo ;
	private EACHSYSSTATUSTAB_BO eachsysstatustab_bo;
	private BANK_GROUP_Dao bank_group_Dao;
	private BUSINESS_TYPE_BO business_type_bo;
	
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Rptst_14_Form rptst_14_form = (Rptst_14_Form) form;
		String ac_key = StrUtils.isEmpty(rptst_14_form.getAc_key())?"":rptst_14_form.getAc_key();
		String target = StrUtils.isEmpty(rptst_14_form.getTarget())?"search":rptst_14_form.getTarget();
		rptst_14_form.setTarget(target);
		Login_Form login_form = (Login_Form) WebServletUtils.getRequest().getSession().getAttribute("login_form");
		System.out.println("RPTST_14_Action is start >> " + ac_key);
		
		if(login_form.getUserData().getUSER_TYPE().equals("B")){
			BANK_GROUP po = bank_group_Dao.get(login_form.getUserData().getUSER_COMPANY());
			rptst_14_form.setOPBK(login_form.getUserData().getUSER_COMPANY()+"-"+po.getBGBK_NAME());
			rptst_14_form.setOPBK_ID(login_form.getUserData().getUSER_COMPANY());
		}else{
			rptst_14_form.setOpbkIdList(rptst_14_bo.getOpbkIdList());
		}
		
		if(ac_key.startsWith("search")){
			String ext = ac_key.split("_")[1];
			Map map = null;
			if(ext.equals("pdf")){
				map = rptst_14_bo.export(ext,
						rptst_14_form.getTW_YEAR()
						, rptst_14_form.getSTART_TW_MONTH()
						, rptst_14_form.getEND_TW_MONTH()
						, rptst_14_form.getOPBK_ID()
						, rptst_14_form.getSerchStrs()
				);
			}
			BeanUtils.populate(rptst_14_form, map);
			//System.out.println(BeanUtils.describe(rptst_15_form));
			if(map.get("result").equals("TRUE") && !Arguments.getStringArg("ISWINOPEN").equals("Y")){
				exportFile2Client(response, map.get("msg").toString(), zDateHandler.getDateNum()+"_st_14." + ext, rptst_14_form.getDow_token() );
				return null;
			}
		}
		
		if(StrUtils.isEmpty(ac_key)){
			String bsDate = eachsysstatustab_bo.getRptBusinessDate();
//			20160118 edit by hugo req by UAT-20160112-01..~05
			Map<String,String> map = getRPT_PRE_YandM_M1(bsDate, "yyyyMMdd", "TW_YEAR", "START_TW_MONTH", "", "END_TW_MONTH");
			BeanUtils.populate(rptst_14_form, map);
//			rptst_14_form.setTW_YEAR(bsDate.substring(0, 4));
//			rptst_14_form.setSTART_TW_MONTH(bsDate.substring(4, 6));
//			rptst_14_form.setEND_TW_MONTH(bsDate.substring(4, 6));
		}
		
		target = StrUtils.isEmpty(rptst_14_form.getTarget())?"":rptst_14_form.getTarget();
		return mapping.findForward(target);
	}
	
	public RPTST_14_BO getRptst_14_bo() {
		return rptst_14_bo;
	}

	public void setRptst_14_bo(RPTST_14_BO rptst_14_bo) {
		this.rptst_14_bo = rptst_14_bo;
	}

	public EACHSYSSTATUSTAB_BO getEachsysstatustab_bo() {
		return eachsysstatustab_bo;
	}

	public void setEachsysstatustab_bo(EACHSYSSTATUSTAB_BO eachsysstatustab_bo) {
		this.eachsysstatustab_bo = eachsysstatustab_bo;
	}

	public BANK_GROUP_Dao getBank_group_Dao() {
		return bank_group_Dao;
	}

	public void setBank_group_Dao(BANK_GROUP_Dao bank_group_Dao) {
		this.bank_group_Dao = bank_group_Dao;
	}

	public BUSINESS_TYPE_BO getBusiness_type_bo() {
		return business_type_bo;
	}

	public void setBusiness_type_bo(BUSINESS_TYPE_BO business_type_bo) {
		this.business_type_bo = business_type_bo;
	}
	
}
