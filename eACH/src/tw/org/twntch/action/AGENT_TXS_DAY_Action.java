package tw.org.twntch.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import tw.org.twntch.bo.AGENT_TXS_DAY_BO;
import tw.org.twntch.bo.Arguments;
import tw.org.twntch.bo.EACHSYSSTATUSTAB_BO;
import tw.org.twntch.bo.TXS_DAY_BO;
import tw.org.twntch.form.Agent_Txs_Day_Form;
import tw.org.twntch.form.Login_Form;
import tw.org.twntch.form.Txs_Day_Form;
import tw.org.twntch.util.BeanUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.WebServletUtils;
import tw.org.twntch.util.zDateHandler;

public class AGENT_TXS_DAY_Action extends GenericAction {

	private AGENT_TXS_DAY_BO agent_txs_day_bo;
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Agent_Txs_Day_Form agent_txs_day_form = (Agent_Txs_Day_Form) form;
		String ac_key = StrUtils.isEmpty(agent_txs_day_form.getAc_key())?"":agent_txs_day_form.getAc_key();
		String target = StrUtils.isEmpty(agent_txs_day_form.getTarget())?"search":agent_txs_day_form.getTarget();
		Login_Form login_form = (Login_Form) WebServletUtils.getRequest().getSession().getAttribute("login_form");
		agent_txs_day_form.setTarget(target);
		
		System.out.println("TXS_DAY_Action is start >> " + ac_key);
		
		if(ac_key.equalsIgnoreCase("edit")){
		}
		if(ac_key.equalsIgnoreCase("update")){
		}
		if(ac_key.equalsIgnoreCase("add")){
		}
		if(ac_key.equalsIgnoreCase("export")){
			Map<String,String> params = BeanUtils.describe(agent_txs_day_form);
			Map map = agent_txs_day_bo.qs_ex_export(params);
			BeanUtils.populate(agent_txs_day_bo, map);
			if(map.get("result").equals("TRUE") && !Arguments.getStringArg("ISWINOPEN").equals("Y")){
				exportFile2Client(response, map.get("msg").toString(), zDateHandler.getDateNum()+"_agent_txs_day.xls" ,agent_txs_day_form.getDow_token() );
				return null;
			}
		}
		if(StrUtils.isEmpty(ac_key)){
			//將營業日塞到頁面的日期控制項
			String busDate = agent_txs_day_bo.getTxnlog_bo().getEachsysstatustab_bo().getBusinessDate();
			System.out.println("busDate="+busDate);
			agent_txs_day_form.setSBIZDATE(busDate);
			agent_txs_day_form.setEBIZDATE(busDate);
			
			//非票交端不帶入下拉選單
			if(!login_form.getUserData().getUSER_TYPE().equals("A")){
				agent_txs_day_form.setAGENT_COMPANY_ID(login_form.getUserData().getUSER_COMPANY());
			}
		}
		//交易代號清單
		agent_txs_day_form.setPcodeList(agent_txs_day_bo.getPcodeList());
		//代理業者清單
		agent_txs_day_form.setCompany_IdList(agent_txs_day_bo.getTxnlog_bo().getAgent_profile_bo().getCompany_Id_List());
		
		target = StrUtils.isEmpty(agent_txs_day_form.getTarget())?"":agent_txs_day_form.getTarget();
		return mapping.findForward(target);
	}
	
	
	public AGENT_TXS_DAY_BO getAgent_txs_day_bo() {
		return agent_txs_day_bo;
	}


	public void setAgent_txs_day_bo(AGENT_TXS_DAY_BO agent_txs_day_bo) {
		this.agent_txs_day_bo = agent_txs_day_bo;
	}


	
}
