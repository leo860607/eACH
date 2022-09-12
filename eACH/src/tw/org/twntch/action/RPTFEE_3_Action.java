package tw.org.twntch.action;


import java.util.Calendar;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import tw.org.twntch.bo.Arguments;
import tw.org.twntch.bo.BANK_GROUP_BO;
import tw.org.twntch.bo.RPTFEE_3_BO;
import tw.org.twntch.form.Rptfee_3_Form;
import tw.org.twntch.util.BeanUtils;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.zDateHandler;

public class RPTFEE_3_Action extends GenericAction {
	private BANK_GROUP_BO bank_group_bo;
	private RPTFEE_3_BO rptfee_3_bo;
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Rptfee_3_Form rptfee_3_form = (Rptfee_3_Form) form;
		String ac_key = StrUtils.isEmpty(rptfee_3_form.getAc_key())?"":rptfee_3_form.getAc_key();
		String target = StrUtils.isEmpty(rptfee_3_form.getTarget())?"search":rptfee_3_form.getTarget();
		rptfee_3_form.setTarget(target);
		System.out.println("RPTFEE_3_Action is start >> " + ac_key);
		
		if(ac_key.startsWith("search")){
			String ext = ac_key.split("_")[1];
			Map map = null;
			if(ext.equals("xls")){
				map = rptfee_3_bo.ex_export(ext, rptfee_3_form.getTW_YEAR(), rptfee_3_form.getTW_MONTH(), rptfee_3_form.getOPBK_ID(), rptfee_3_form.getBGBK_ID(), rptfee_3_form.getSerchStrs());
			}else if(ext.equals("pdf")){
				map = rptfee_3_bo.export(ext, rptfee_3_form.getTW_YEAR(), rptfee_3_form.getTW_MONTH(), rptfee_3_form.getOPBK_ID(), rptfee_3_form.getBGBK_ID(), rptfee_3_form.getSerchStrs());
			}
			BeanUtils.populate(rptfee_3_form, map);
			if(map.get("result").equals("TRUE") && !Arguments.getStringArg("ISWINOPEN").equals("Y")){
				exportFile2Client(response, map.get("msg").toString(), zDateHandler.getDateNum()+"_fee_3." + ext, rptfee_3_form.getDow_token() );
				return null;
			}
		}
		if(StrUtils.isEmpty(ac_key)){
//			20160118 edit by hugo req by UAT-20160112-01..~05
			String bsDate = bank_group_bo.getEachsysstatustab_bo().getRptBusinessDate();
			Map<String,String> map = getRPT_PRE_YandM_M1(bsDate, "yyyyMMdd", "TW_YEAR", "TW_MONTH", "", "");
			BeanUtils.populate(rptfee_3_form, map);
//			Calendar nowDate = Calendar.getInstance();
//			rptfee_3_form.setTW_YEAR(DateTimeUtils.convertDate(String.valueOf(nowDate.get(Calendar.YEAR)), "yyyy", "yyyy"));
//			rptfee_3_form.setTW_MONTH(DateTimeUtils.convertDate(String.valueOf(nowDate.get(Calendar.MONTH)), "M", "MM"));
		}
		rptfee_3_form.setOpbkIdList(bank_group_bo.getOpbkList());
//		rptfee_3_form.setOpbkIdList(bank_group_bo.getOpbkList());
		target = StrUtils.isEmpty(rptfee_3_form.getTarget())?"":rptfee_3_form.getTarget();
		return mapping.findForward(target);
	}
	public BANK_GROUP_BO getBank_group_bo() {
		return bank_group_bo;
	}
	public void setBank_group_bo(BANK_GROUP_BO bank_group_bo) {
		this.bank_group_bo = bank_group_bo;
	}
	public RPTFEE_3_BO getRptfee_3_bo() {
		return rptfee_3_bo;
	}
	public void setRptfee_3_bo(RPTFEE_3_BO rptfee_3_bo) {
		this.rptfee_3_bo = rptfee_3_bo;
	}
	
}
