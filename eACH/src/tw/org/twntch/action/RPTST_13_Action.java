package tw.org.twntch.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import tw.org.twntch.bo.Arguments;
import tw.org.twntch.bo.BANK_GROUP_BO;
import tw.org.twntch.bo.EACHSYSSTATUSTAB_BO;
import tw.org.twntch.bo.RPTST_13_BO;
import tw.org.twntch.db.dao.hibernate.BANK_GROUP_Dao;
import tw.org.twntch.form.Login_Form;
import tw.org.twntch.form.Rptst_13_Form;
import tw.org.twntch.po.BANK_GROUP;
import tw.org.twntch.util.BeanUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.WebServletUtils;
import tw.org.twntch.util.zDateHandler;

public class RPTST_13_Action extends GenericAction {
	private RPTST_13_BO rptst_13_bo ;
	private BANK_GROUP_BO bank_group_bo ; 
	private BANK_GROUP_Dao bank_group_Dao ;
	private EACHSYSSTATUSTAB_BO eachsysstatustab_bo ;
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Rptst_13_Form rptst_13_form = (Rptst_13_Form) form;
		String ac_key = StrUtils.isEmpty(rptst_13_form.getAc_key())?"":rptst_13_form.getAc_key();
		String target = StrUtils.isEmpty(rptst_13_form.getTarget())?"search":rptst_13_form.getTarget();
		rptst_13_form.setTarget(target);
		Map map = null ;
		System.out.println("RPTST_13_Action is start >> " + ac_key);
		Login_Form login_form = (Login_Form) WebServletUtils.getRequest().getSession().getAttribute("login_form");
		
		if(login_form.getUserData().getUSER_TYPE().equals("B")){
			BANK_GROUP po = bank_group_Dao.get(login_form.getUserData().getUSER_COMPANY());
			rptst_13_form.setOPBK(login_form.getUserData().getUSER_COMPANY()+"-"+po.getBGBK_NAME());
			rptst_13_form.setOPBK_ID(login_form.getUserData().getUSER_COMPANY());
		}else{
			rptst_13_form.setOpbkIdList(bank_group_bo.getOpbkList());
		}
		
		if(ac_key.equalsIgnoreCase("search")){
			//銀行端要多加一道檢核
			if(login_form.getUserData().getUSER_TYPE().equals("B")){
				Map retmap = super.checkRPT_BizDate(rptst_13_form.getEND_DATE(), rptst_13_form.getCLEARINGPHASE());
				if(retmap.get("result").equals("FALSE") ){
					BeanUtils.populate(rptst_13_form, retmap);
					return mapping.findForward(target);
				}
			}
			
			map = rptst_13_bo.export(
				rptst_13_form.getSTART_DATE()
				, rptst_13_form.getEND_DATE()
				, rptst_13_form.getCLEARINGPHASE()
				, rptst_13_form.getOPBK()
				, rptst_13_form.getOPBK_ID()
				, rptst_13_form.getSerchStrs()
			);
			
			BeanUtils.populate(rptst_13_form, map);
			if(map.get("result").equals("TRUE") && !Arguments.getStringArg("ISWINOPEN").equals("Y")){
				exportFile2Client(response, map.get("msg").toString(), zDateHandler.getDateNum()+"_st_13.pdf", rptst_13_form.getDow_token() );
				return null;
			}
		}
		
		//預設為系統日
		String sysDate = zDateHandler.getTWDate();
		rptst_13_form.setSTART_DATE(sysDate);
		rptst_13_form.setEND_DATE(sysDate);
		
		target = StrUtils.isEmpty(rptst_13_form.getTarget())?"":rptst_13_form.getTarget();
		return mapping.findForward(target);
	}
	
	public BANK_GROUP_Dao getBank_group_Dao() {
		return bank_group_Dao;
	}
	
	public RPTST_13_BO getRptst_13_bo() {
		return rptst_13_bo;
	}
	public void setRptst_13_bo(RPTST_13_BO rptst_13_bo) {
		this.rptst_13_bo = rptst_13_bo;
	}
	public void setBank_group_Dao(BANK_GROUP_Dao bank_group_Dao) {
		this.bank_group_Dao = bank_group_Dao;
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
