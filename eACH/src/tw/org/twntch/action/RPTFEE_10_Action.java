package tw.org.twntch.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import tw.org.twntch.bo.AGENT_SEND_PROFILE_BO;
import tw.org.twntch.bo.Arguments;
import tw.org.twntch.bo.BANK_GROUP_BO;
import tw.org.twntch.bo.EACHSYSSTATUSTAB_BO;
import tw.org.twntch.bo.RPTFEE_10_BO;
import tw.org.twntch.bo.RPTFEE_9_BO;
import tw.org.twntch.bo.RPTST_16_BO;
import tw.org.twntch.bo.RPTST_17_BO;
import tw.org.twntch.bo.RPTTX_1_BO;
import tw.org.twntch.bo.RPTTX_5_BO;
import tw.org.twntch.db.dao.hibernate.BANK_GROUP_Dao;
import tw.org.twntch.form.Login_Form;
import tw.org.twntch.form.Rpt_Agent_Form;
import tw.org.twntch.form.Rpttx_1_Form;
import tw.org.twntch.po.BANK_GROUP;
import tw.org.twntch.util.BeanUtils;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.WebServletUtils;
import tw.org.twntch.util.zDateHandler;

public class RPTFEE_10_Action extends GenericAction {

	private RPTFEE_10_BO rptfee_10_bo ;
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Rpt_Agent_Form rpt_agent_form = (Rpt_Agent_Form) form;
		String ac_key = StrUtils.isEmpty(rpt_agent_form.getAc_key())?"":rpt_agent_form.getAc_key();
		String target = StrUtils.isEmpty(rpt_agent_form.getTarget())?"search":rpt_agent_form.getTarget();
		rpt_agent_form.setTarget(target);
		String  yyyymm = "" , outFilename = "_fee_10.pdf";
		Map map = null ;
		int export_type = 1;
		System.out.println("RPTFEE_10_Action is start >> " + ac_key);
		Login_Form login_form = (Login_Form) WebServletUtils.getRequest().getSession().getAttribute("login_form");
		//非票交端不帶入下拉選單
		if(!login_form.getUserData().getUSER_TYPE().equals("A")){
			rpt_agent_form.setAGENT_COMPANY_ID(login_form.getUserData().getUSER_COMPANY());
		}
		if(ac_key.equalsIgnoreCase("search") || ac_key.equalsIgnoreCase("xls") ){
			yyyymm = Integer.valueOf(rpt_agent_form.getSTART_YEAR())+"/"+rpt_agent_form.getSTART_MONTH()+"~"+Integer.valueOf(rpt_agent_form.getEND_YEAR())+"/"+rpt_agent_form.getEND_MONTH() ;
//			參加單位端要多加一道檢核
			if(!login_form.getUserData().getUSER_TYPE().equals("A")){
				Map retmap = super.checkRPT_BizDate(rpt_agent_form.getBIZDATE(), rpt_agent_form.getCLEARINGPHASE());
				if(retmap.get("result").equals("FALSE") ){
					BeanUtils.populate(rpt_agent_form, retmap);
					return mapping.findForward(target);
				}
			}
			if(ac_key.equalsIgnoreCase("xls")){
				export_type = 2;
				outFilename = "_fee_10.xls";
				map = rptfee_10_bo.ex_export(rpt_agent_form.getAGENT_COMPANY_ID(), rpt_agent_form.getSND_COMPANY_ID(), yyyymm , rpt_agent_form.getCLEARINGPHASE(), rpt_agent_form.getSerchStrs() , export_type);
			}else{
				map = rptfee_10_bo.export(rpt_agent_form.getAGENT_COMPANY_ID(), rpt_agent_form.getSND_COMPANY_ID(), yyyymm , rpt_agent_form.getCLEARINGPHASE(), rpt_agent_form.getSerchStrs() , export_type);
			}
			BeanUtils.populate(rpt_agent_form, map);
			if(map.get("result").equals("TRUE") && !Arguments.getStringArg("ISWINOPEN").equals("Y")){
				exportFile2Client(response, map.get("msg").toString(), zDateHandler.getDateNum()+outFilename ,rpt_agent_form.getDow_token() );
				return null;
			}
			
			
		}
		if(StrUtils.isEmpty(ac_key)){
			String bsDate = rptfee_10_bo.getAgent_send_profile_bo().getEachsysstatustab_bo() .getRptBusinessDate();
//			20160118 edit by hugo req by UAT-20160112-01..~05
			Map<String,String> tmpmap = getRPT_PRE_YandM_M1(bsDate, "yyyyMMdd", "START_YEAR", "START_MONTH", "END_YEAR", "END_MONTH");
			BeanUtils.populate(rpt_agent_form, tmpmap);
			
//			rpt_agent_form.setSTART_YEAR(bsDate.substring(0, 4));
//			rpt_agent_form.setSTART_MONTH(getLastMonth(bsDate.substring(4, 6)));
//			rpt_agent_form.setEND_YEAR(bsDate.substring(0, 4));
//			rpt_agent_form.setEND_MONTH(getLastMonth(bsDate.substring(4, 6)));
		}
		rpt_agent_form.setTxnIdList(rptfee_10_bo.getAgent_send_profile_bo().getAgent_fee_code_bo().getIdList());
		rpt_agent_form.setCompany_IdList(rptfee_10_bo.getAgent_send_profile_bo().getAgent_profile_bo().getCompany_Id_ABBR_List());
		target = StrUtils.isEmpty(rpt_agent_form.getTarget())?"":rpt_agent_form.getTarget();
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


	public RPTFEE_10_BO getRptfee_10_bo() {
		return rptfee_10_bo;
	}


	public void setRptfee_10_bo(RPTFEE_10_BO rptfee_10_bo) {
		this.rptfee_10_bo = rptfee_10_bo;
	}



	
	
	
	
}
