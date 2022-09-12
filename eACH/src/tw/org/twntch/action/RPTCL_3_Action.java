package tw.org.twntch.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import tw.org.twntch.bo.Arguments;
import tw.org.twntch.bo.EACHSYSSTATUSTAB_BO;
import tw.org.twntch.bo.RPTCL_3_BO;
import tw.org.twntch.form.Login_Form;
import tw.org.twntch.form.Rptcl_3_Form;
import tw.org.twntch.util.BeanUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.WebServletUtils;
import tw.org.twntch.util.zDateHandler;

public class RPTCL_3_Action extends GenericAction {
	private RPTCL_3_BO rptcl_3_bo ;
	private EACHSYSSTATUSTAB_BO eachsysstatustab_bo ;
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Rptcl_3_Form Rptcl_3_Form = (Rptcl_3_Form) form;
		String ac_key = StrUtils.isEmpty(Rptcl_3_Form.getAc_key())?"":Rptcl_3_Form.getAc_key();
		String target = StrUtils.isEmpty(Rptcl_3_Form.getTarget())?"search":Rptcl_3_Form.getTarget();
		Rptcl_3_Form.setTarget(target);
		System.out.println("RPTCL_3_Action is start >> " + ac_key);
		Login_Form login_form = (Login_Form) WebServletUtils.getRequest().getSession().getAttribute("login_form");
		if(ac_key.equalsIgnoreCase("search")){
			System.out.println("Rptcl_3_Form.getTXDT()>>"+Rptcl_3_Form.getTXDT());
//			銀行端要多加一道檢核
			if(login_form.getUserData().getUSER_TYPE().equals("B")){
//				Map retmap = super.checkRPTCL_BizDate(Rptcl_3_Form.getTXDT(), Rptcl_3_Form.getCLEARINGPHASE());
				Map retmap = super.checkRPT_BizDate(Rptcl_3_Form.getTXDT(), Rptcl_3_Form.getCLEARINGPHASE());
				if(retmap.get("result").equals("FALSE") ){
					BeanUtils.populate(Rptcl_3_Form, retmap);
					return mapping.findForward(target);
				}
			}
			Map map = rptcl_3_bo.export(Rptcl_3_Form.getTXDT(), Rptcl_3_Form.getCLEARINGPHASE(), Rptcl_3_Form.getSerchStrs());
			BeanUtils.populate(Rptcl_3_Form, map);
			if(map.get("result").equals("TRUE") && !Arguments.getStringArg("ISWINOPEN").equals("Y")){
				exportFile2Client(response, map.get("msg").toString(), zDateHandler.getDateNum()+"_cl_3.pdf" ,Rptcl_3_Form.getDow_token() );
				return null;
			}
		}
		if(StrUtils.isEmpty(ac_key)){
		}
//		Rptcl_4_Form.setBg_bankList(rptcl_4_bo.getBgbkIdList());
//		Rptcl_4_Form.setOpt_bankList(rptcl_4_bo.getOptBgbkIdList());
//		Rptcl_3_Form.setCt_bankList(rptcl_3_bo.getOptCtbkIdList());
//		Rptcl_3_Form.setbeforeDate(rptcl_3_bo.getBeforeDate());
		Rptcl_3_Form.setbeforeDate(eachsysstatustab_bo.getRptBusinessDate());
		System.out.println("txdt>>"+Rptcl_3_Form.getTXDT());
		target = StrUtils.isEmpty(Rptcl_3_Form.getTarget())?"":Rptcl_3_Form.getTarget();
		return mapping.findForward(target);
	}

	public RPTCL_3_BO getRptcl_3_bo() {
		return rptcl_3_bo;
	}

	public void setRptcl_3_bo(RPTCL_3_BO rptcl_3_bo) {
		this.rptcl_3_bo = rptcl_3_bo;
	}

	public EACHSYSSTATUSTAB_BO getEachsysstatustab_bo() {
		return eachsysstatustab_bo;
	}

	public void setEachsysstatustab_bo(EACHSYSSTATUSTAB_BO eachsysstatustab_bo) {
		this.eachsysstatustab_bo = eachsysstatustab_bo;
	}

	
}
