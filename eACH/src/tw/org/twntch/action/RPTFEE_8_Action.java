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
import tw.org.twntch.bo.RPTFEE_8_BO;
import tw.org.twntch.bo.RPTTX_1_BO;
import tw.org.twntch.bo.RPTTX_5_BO;
import tw.org.twntch.db.dao.hibernate.BANK_GROUP_Dao;
import tw.org.twntch.form.Login_Form;
import tw.org.twntch.form.Rpt_Agent_Form;
import tw.org.twntch.form.Rpttx_1_Form;
import tw.org.twntch.po.BANK_GROUP;
import tw.org.twntch.util.BeanUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.WebServletUtils;
import tw.org.twntch.util.zDateHandler;

public class RPTFEE_8_Action extends GenericAction {

	private RPTFEE_8_BO rptfee_8_bo ;
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Rpt_Agent_Form rpt_agent_form = (Rpt_Agent_Form) form;
		String ac_key = StrUtils.isEmpty(rpt_agent_form.getAc_key())?"":rpt_agent_form.getAc_key();
		String target = StrUtils.isEmpty(rpt_agent_form.getTarget())?"search":rpt_agent_form.getTarget();
		rpt_agent_form.setTarget(target);
		String   outFilename = "_fee_8.pdf";
		Map map = null ;
		int export_type = 1;
		System.out.println("RPTFEE_8_Action is start >> " + ac_key);
		Login_Form login_form = (Login_Form) WebServletUtils.getRequest().getSession().getAttribute("login_form");
		//非票交端不帶入下拉選單
		if(!login_form.getUserData().getUSER_TYPE().equals("A")){
			rpt_agent_form.setAGENT_COMPANY_ID(login_form.getUserData().getUSER_COMPANY());
		}
		if(ac_key.equalsIgnoreCase("search") || ac_key.equalsIgnoreCase("xls")){
//			銀行端要多加一道檢核
			if(!login_form.getUserData().getUSER_TYPE().equals("A")){
				Map retmap = super.checkRPT_BizDate(rpt_agent_form.getSBIZDATE(), rpt_agent_form.getCLEARINGPHASE());
				if(retmap.get("result").equals("FALSE") ){
					BeanUtils.populate(rpt_agent_form, retmap);
					return mapping.findForward(target);
				}
			}
			if(ac_key.equalsIgnoreCase("xls")){
				export_type = 2;
				outFilename = "_fee_8.xls";
				map = rptfee_8_bo.ex_export(rpt_agent_form.getAGENT_COMPANY_ID(), rpt_agent_form.getSND_COMPANY_ID(), "" , rpt_agent_form.getCLEARINGPHASE(), rpt_agent_form.getSerchStrs() , export_type , login_form.getUserData().getUSER_TYPE());
			}else{
				map = rptfee_8_bo.export(rpt_agent_form.getAGENT_COMPANY_ID(), rpt_agent_form.getSND_COMPANY_ID(), "" , rpt_agent_form.getCLEARINGPHASE(), rpt_agent_form.getSerchStrs(), export_type ,login_form.getUserData().getUSER_TYPE());
			}
			
			BeanUtils.populate(rpt_agent_form, map);
			if(map.get("result").equals("TRUE") && !Arguments.getStringArg("ISWINOPEN").equals("Y")){
				exportFile2Client(response, map.get("msg").toString(), zDateHandler.getDateNum()+outFilename ,rpt_agent_form.getDow_token() );
				return null;
			}
			
			
		}
		if(StrUtils.isEmpty(ac_key)){
		}
		rpt_agent_form.setBIZDATE(rptfee_8_bo.getAgent_send_profile_bo().getEachsysstatustab_bo().getRptBusinessDate()); 
		rpt_agent_form.setSBIZDATE(rpt_agent_form.getBIZDATE()); 
		rpt_agent_form.setEBIZDATE(rpt_agent_form.getBIZDATE()); 
		rpt_agent_form.setTxnIdList(rptfee_8_bo.getAgent_send_profile_bo().getAgent_fee_code_bo().getIdList());
//		rpt_agent_form.setCompany_IdList(rptfee_8_bo.getAgent_send_profile_bo().getAgent_profile_bo().getCompany_Id_List());
		rpt_agent_form.setCompany_IdList(rptfee_8_bo.getAgent_send_profile_bo().getAgent_profile_bo().getCompany_Id_ABBR_List());
		target = StrUtils.isEmpty(rpt_agent_form.getTarget())?"":rpt_agent_form.getTarget();
		return mapping.findForward(target);
	}
	public RPTFEE_8_BO getRptfee_8_bo() {
		return rptfee_8_bo;
	}
	public void setRptfee_8_bo(RPTFEE_8_BO rptfee_8_bo) {
		this.rptfee_8_bo = rptfee_8_bo;
	}
	
	
	
	
	
}
