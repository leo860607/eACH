package tw.org.twntch.action;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import tw.org.twntch.bo.EACHSYSSTATUSTAB_BO;
import tw.org.twntch.bo.PI_COMPANY_PROFILE_BO;
import tw.org.twntch.bo.TXNLOG_BO;
import tw.org.twntch.form.Login_Form;
import tw.org.twntch.form.TxnLog_Form;
import tw.org.twntch.po.AGENT_PROFILE;
import tw.org.twntch.util.BeanUtils;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.WebServletUtils;

public class TXNLOG_Action extends GenericAction {

	private TXNLOG_BO txnlog_bo ;
	private PI_COMPANY_PROFILE_BO pi_company_profile_bo;
	private EACHSYSSTATUSTAB_BO eachsysstatustab_bo;
	
	
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// TODO Auto-generated method stub
		TxnLog_Form   txnlog_form =  (TxnLog_Form) form ;
		Login_Form login_form = (Login_Form) WebServletUtils.getRequest().getSession().getAttribute("login_form");
		String ac_key = StrUtils.isEmpty(txnlog_form.getAc_key())?"":txnlog_form.getAc_key();
		String target = StrUtils.isEmpty(txnlog_form.getTarget())?"search":txnlog_form.getTarget();
		String date_type_checked = StrUtils.isEmpty(txnlog_form.getDate_type_checked())?"TXDATE":txnlog_form.getDate_type_checked();
		txnlog_form.setDate_type_checked(date_type_checked);
		txnlog_form.setTarget(target);
		List<Map> list = null;
		//取得繳費類別清單
		txnlog_form.setPfclassList(pi_company_profile_bo.getIdListByBillType());
		System.out.println("ac_key>>"+ac_key);
		txnlog_form.setTxn_IdList(txnlog_bo.getTxn_code_bo().getIdList()); 
//		將系統日日塞到頁面的日期控制項
		String busDate = eachsysstatustab_bo.getBusinessDate();
//		將系統日日塞到頁面的日期控制項
		String sysDate = DateTimeUtils.convertDate(DateTimeUtils.getDateShort(new Date()), "yyyyMMdd", "yyyyMMdd");
		txnlog_form.setSTXDATE(sysDate);
		txnlog_form.setETXDATE(sysDate);
		//txnlog_form.setTXDATE(busDate);
		
//		將營業日塞到頁面的日期控制項
		if(login_form.getUserData().getUSER_TYPE().equals("A")){
			txnlog_form.setSBIZDATE(txnlog_bo.getEachsysstatustab_bo().getBusinessDate());
			txnlog_form.setEBIZDATE(txnlog_bo.getEachsysstatustab_bo().getBusinessDate());
		}else{
			txnlog_form.setBIZDATE(txnlog_bo.getEachsysstatustab_bo().getBusinessDate());
		}
		
		if(login_form.getUserData().getUSER_TYPE().equals("A")){
			txnlog_form.setCompany_IdList(txnlog_bo.getCompany_Id_List()); 
		}else{
			txnlog_form.setAGENT_COMPANY_ID(login_form.getUserData().getUSER_COMPANY());
			AGENT_PROFILE po =  txnlog_bo.getAgent_profile_bo().getAgent_profile_Dao().get(login_form.getUserData().getUSER_COMPANY());
			txnlog_form.setAGENT_COMPANY_NAME(po.getCOMPANY_ID()+"-"+po.getCOMPANY_NAME() );
		}
		if(ac_key.equalsIgnoreCase("search")|| ac_key.equalsIgnoreCase("back") ){
			System.out.println("SerchStrs>>"+txnlog_form.getSerchStrs());
			BeanUtils.populate(txnlog_form, JSONUtils.json2map(txnlog_form.getSerchStrs()));
			if("".equals(txnlog_form.getSTXDATE()) && "".equals(txnlog_form.getETXDATE())){
				//將營業日塞到頁面的日期控制項
				busDate = eachsysstatustab_bo.getBusinessDate();
				txnlog_form.setSTXDATE(sysDate);
				txnlog_form.setETXDATE(sysDate);
			}
			if("".equals(txnlog_form.getSBIZDATE()) && "".equals(txnlog_form.getEBIZDATE())){
				//將交易日塞到頁面的日期控制項
				if(login_form.getUserData().getUSER_TYPE().equals("A")){
					txnlog_form.setSBIZDATE(txnlog_bo.getEachsysstatustab_bo().getBusinessDate());
					txnlog_form.setEBIZDATE(txnlog_bo.getEachsysstatustab_bo().getBusinessDate());
				}else{
					txnlog_form.setBIZDATE(txnlog_bo.getEachsysstatustab_bo().getBusinessDate());
				}
			}
		}
		
		if(ac_key.equalsIgnoreCase("edit")){
			BeanUtils.populate(txnlog_form, JSONUtils.json2map(txnlog_form.getEdit_params()));
//			list = txnlog_bo.getDetail(txnlog_form.getTXDATE(), txnlog_form.getSTAN(), txnlog_form.getSEQ());
//			list = txnlog_bo.getDetail(txnlog_form.getTXDATE(), txnlog_form.getISSUERID(), txnlog_form.getSEQ());
			list = txnlog_bo.getDetail(txnlog_form.getTXDATE(), txnlog_form.getISSUERID(), txnlog_form.getSEQ() , login_form.getUserData().getUSER_TYPE());
			System.out.println("edit.agent_cr_line_form.getSerchStrs()>>"+txnlog_form.getSerchStrs());
			if(list != null && list.size() > 0){
				txnlog_form.setDetailData(list.get(0));
			}
			if(StrUtils.isNotEmpty(txnlog_form.getSerchStrs())){
				System.out.println("edit.json2map>>"+ JSONUtils.json2map(txnlog_form.getSerchStrs()) );
			}
		}
		if(ac_key.equalsIgnoreCase("update")){
		}
		if(ac_key.equalsIgnoreCase("delete")){
		}
		target = StrUtils.isEmpty(txnlog_form.getTarget())?"search":txnlog_form.getTarget();
		return mapping.findForward(target);
	}

	public TXNLOG_BO getTxnlog_bo() {
		return txnlog_bo;
	}

	public void setTxnlog_bo(TXNLOG_BO txnlog_bo) {
		this.txnlog_bo = txnlog_bo;
	}

	public PI_COMPANY_PROFILE_BO getPi_company_profile_bo() {
		return pi_company_profile_bo;
	}

	public void setPi_company_profile_bo(PI_COMPANY_PROFILE_BO pi_company_profile_bo) {
		this.pi_company_profile_bo = pi_company_profile_bo;
	}

	public EACHSYSSTATUSTAB_BO getEachsysstatustab_bo() {
		return eachsysstatustab_bo;
	}

	public void setEachsysstatustab_bo(EACHSYSSTATUSTAB_BO eachsysstatustab_bo) {
		this.eachsysstatustab_bo = eachsysstatustab_bo;
	}
	
	
	
	
	
}
