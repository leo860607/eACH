package tw.org.twntch.action;

import java.math.BigDecimal;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import tw.org.twntch.bo.Arguments;
import tw.org.twntch.bo.RPTST_17_BO;
import tw.org.twntch.bo.RPTST_7_BO;
import tw.org.twntch.form.Login_Form;
import tw.org.twntch.form.Rpt_Agent_Form;
import tw.org.twntch.form.Rpt_Form;
import tw.org.twntch.util.BeanUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.WebServletUtils;
import tw.org.twntch.util.zDateHandler;

public class RPTST_7_Action extends GenericAction {

		private RPTST_7_BO rptst_7_bo ;
		@Override
		public ActionForward execute(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {
			Rpt_Form rpt_form = (Rpt_Form) form;
			String ac_key = StrUtils.isEmpty(rpt_form.getAc_key())?"":rpt_form.getAc_key();
			String target = StrUtils.isEmpty(rpt_form.getTarget())?"search":rpt_form.getTarget();
			rpt_form.setTarget(target);
			String  yyyymm = "" , outFilename = "_st_7.pdf";
			Map map = null ;
			int export_type = 1;
			System.out.println("RPTST_7_Action is start >> " + ac_key);
			Login_Form login_form = (Login_Form) WebServletUtils.getRequest().getSession().getAttribute("login_form");
			//非票交端不帶入下拉選單
			if(!login_form.getUserData().getUSER_TYPE().equals("A")){
				rpt_form.setOPBK_ID(login_form.getUserData().getUSER_COMPANY());
			}
			if(ac_key.equalsIgnoreCase("search") || ac_key.equalsIgnoreCase("xls") ){
				yyyymm = Integer.valueOf(rpt_form.getSTART_YEAR())+"/"+rpt_form.getSTART_MONTH()+"~"+Integer.valueOf(rpt_form.getEND_YEAR())+"/"+rpt_form.getEND_MONTH() ;
				if(ac_key.equalsIgnoreCase("xls")){
					export_type = 2;
					outFilename = "_st_7.xls";
					map = rptst_7_bo.ex_export(rpt_form.getOPBK_ID(), yyyymm , rpt_form.getCLEARINGPHASE(), rpt_form.getSerchStrs() , export_type );
				}else{
					map = rptst_7_bo.export(rpt_form.getOPBK_ID(), yyyymm , rpt_form.getCLEARINGPHASE(), rpt_form.getSerchStrs() , export_type );
				}
				BeanUtils.populate(rpt_form, map);
				if(map.get("result").equals("TRUE") && !Arguments.getStringArg("ISWINOPEN").equals("Y")){
					exportFile2Client(response, map.get("msg").toString(), zDateHandler.getDateNum()+outFilename ,rpt_form.getDow_token() );
					return null;
				}
				
				
			}
			if(StrUtils.isEmpty(ac_key)){
				String bsDate = rptst_7_bo.getBank_group_bo().getEachsysstatustab_bo().getRptBusinessDate();
//				20160118 edit by hugo req by UAT-20160112-01..~05
				Map<String,String> tmpmap = getRPT_PRE_YandM_M1(bsDate, "yyyyMMdd", "START_YEAR", "START_MONTH", "END_YEAR", "END_MONTH");
				BeanUtils.populate(rpt_form, tmpmap);
				rpt_form.setOpbkIdList(rptst_7_bo.getBank_group_bo().getOpbkList());
			}
			target = StrUtils.isEmpty(rpt_form.getTarget())?"":rpt_form.getTarget();
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


		public RPTST_7_BO getRptst_7_bo() {
			return rptst_7_bo;
		}


		public void setRptst_7_bo(RPTST_7_BO rptst_7_bo) {
			this.rptst_7_bo = rptst_7_bo;
		}


		
		
		
	
	
}
