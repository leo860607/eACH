package tw.org.twntch.action;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import tw.org.twntch.bo.Arguments;
import tw.org.twntch.bo.BANK_GROUP_BO;
import tw.org.twntch.bo.EACHSYSSTATUSTAB_BO;
import tw.org.twntch.bo.RPTST_5_BO;
import tw.org.twntch.form.Rptst_5_Form;
import tw.org.twntch.util.BeanUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.zDateHandler;

public class RPTST_5_Action extends GenericAction {
	private RPTST_5_BO rptst_5_bo ;
	private EACHSYSSTATUSTAB_BO eachsysstatustab_bo;
	private BANK_GROUP_BO bank_group_bo;
	
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Rptst_5_Form rptst_5_form = (Rptst_5_Form) form;
		String ac_key = StrUtils.isEmpty(rptst_5_form.getAc_key())?"":rptst_5_form.getAc_key();
		String target = StrUtils.isEmpty(rptst_5_form.getTarget())?"search":rptst_5_form.getTarget();
		rptst_5_form.setTarget(target);
		System.out.println("RPTST_5_Action is start >> " + ac_key);
		
		if(ac_key.startsWith("search")){
			String ext = ac_key.split("_")[1];
			Map map = null;
			if(ext.equals("xls")){
				map = rptst_5_bo.ex_export(ext,
						rptst_5_form.getSTART_YEAR(),
						rptst_5_form.getSTART_MONTH(),
						rptst_5_form.getEND_YEAR(),
						rptst_5_form.getEND_MONTH(),
						rptst_5_form.getOPBK_ID(),
						rptst_5_form.getBGBK_ID(),
						rptst_5_form.getSerchStrs()
				);
			}else if(ext.equals("pdf")){
				map = rptst_5_bo.export(ext,
						rptst_5_form.getSTART_YEAR(),
						rptst_5_form.getSTART_MONTH(),
						rptst_5_form.getEND_YEAR(),
						rptst_5_form.getEND_MONTH(),
						rptst_5_form.getOPBK_ID(),
						rptst_5_form.getBGBK_ID(),
						rptst_5_form.getSerchStrs()
				);
			}
			BeanUtils.populate(rptst_5_form, map);
			System.out.println(BeanUtils.describe(rptst_5_form));
			if(map.get("result").equals("TRUE") && !Arguments.getStringArg("ISWINOPEN").equals("Y")){
				exportFile2Client(response, map.get("msg").toString(), zDateHandler.getDateNum()+"_st_5." + ext, rptst_5_form.getDow_token() );
				return null;
			}
		}
		if(StrUtils.isEmpty(ac_key)){
			String bsDate = eachsysstatustab_bo.getRptBusinessDate();
//			20160118 edit by hugo req by UAT-20160112-01..~05
			Map<String,String> map = getRPT_PRE_YandM_M1(bsDate, "yyyyMMdd", "START_YEAR", "START_MONTH", "END_YEAR", "END_MONTH");
			BeanUtils.populate(rptst_5_form, map);
//			rptst_5_form.setSTART_YEAR(bsDate.substring(0, 4));
//			rptst_5_form.setSTART_MONTH(getLastMonth(bsDate.substring(4, 6)));
//			rptst_5_form.setEND_YEAR(bsDate.substring(0, 4));
//			rptst_5_form.setEND_MONTH(getLastMonth(bsDate.substring(4, 6)));
		}
//		rptst_5_form.setOpbkIdList(rptst_5_bo.getOpbkIdList());
		rptst_5_form.setOpbkIdList(bank_group_bo.getOpbkList());
		//rptst_5_form.setBgbkIdList(rptst_5_bo.getBgbkIdList());
		target = StrUtils.isEmpty(rptst_5_form.getTarget())?"":rptst_5_form.getTarget();
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

	public RPTST_5_BO getRptst_5_bo() {
		return rptst_5_bo;
	}

	public void setRptst_5_bo(RPTST_5_BO rptst_5_bo) {
		this.rptst_5_bo = rptst_5_bo;
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
