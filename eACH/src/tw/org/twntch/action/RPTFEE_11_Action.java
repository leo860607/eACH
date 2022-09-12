package tw.org.twntch.action;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import tw.org.twntch.bo.Arguments;
import tw.org.twntch.bo.BANK_GROUP_BO;
import tw.org.twntch.bo.EACHSYSSTATUSTAB_BO;
import tw.org.twntch.bo.EACH_USERLOG_BO;
import tw.org.twntch.bo.RPTFEE_11_BO;
import tw.org.twntch.bo.RPTTX_1_BO;
import tw.org.twntch.bo.TXN_CODE_BO;
import tw.org.twntch.db.dao.hibernate.BANK_GROUP_Dao;
import tw.org.twntch.form.Login_Form;
import tw.org.twntch.form.Rptfee_11_Form;
import tw.org.twntch.form.Rpttx_1_Form;
import tw.org.twntch.po.BANK_GROUP;
import tw.org.twntch.util.BeanUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.WebServletUtils;
import tw.org.twntch.util.zDateHandler;

public class RPTFEE_11_Action extends GenericAction {
	private RPTFEE_11_BO rptfee_11_bo ;
	private BANK_GROUP_BO bank_group_bo ; 
	private BANK_GROUP_Dao bank_group_Dao ;
	private EACHSYSSTATUSTAB_BO eachsysstatustab_bo ;
	private TXN_CODE_BO txn_code_bo ;
	private Logger log = Logger.getLogger(this.getClass().getName());
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Rptfee_11_Form rptfee_11_form = (Rptfee_11_Form) form;
		String ac_key = StrUtils.isEmpty(rptfee_11_form.getAc_key())?"":rptfee_11_form.getAc_key();
		String target = StrUtils.isEmpty(rptfee_11_form.getTarget())?"search":rptfee_11_form.getTarget();
		rptfee_11_form.setTarget(target);
		Map map = null ;
		log.debug("RPTFEE_11_Action is start >> " + ac_key);
		Login_Form login_form = (Login_Form) WebServletUtils.getRequest().getSession().getAttribute("login_form");
		if(login_form.getUserData().getUSER_TYPE().equals("B")){
			BANK_GROUP po =  bank_group_Dao.get(login_form.getUserData().getUSER_COMPANY());
			rptfee_11_form.setOpt_bank(login_form.getUserData().getUSER_COMPANY()+"-"+po.getBGBK_NAME());
			rptfee_11_form.setOpt_id(login_form.getUserData().getUSER_COMPANY());
			rptfee_11_form.setBgbkIdList(bank_group_bo.getCurBgbkListByOP(login_form.getUserData().getUSER_COMPANY() , eachsysstatustab_bo.getRptBusinessDate()) );
		}
		
		if(ac_key.equalsIgnoreCase("search")){
//			銀行端要多加一道檢核
			if(login_form.getUserData().getUSER_TYPE().equals("B")){
				Map retmap = super.checkRPT_BizDate(rptfee_11_form.getBIZDATE(), rptfee_11_form.getCLEARINGPHASE());
				if(retmap.get("result").equals("FALSE") ){
					BeanUtils.populate(rptfee_11_form, retmap);
					return mapping.findForward(target);
				}
			}
			map = rptfee_11_bo.export(rptfee_11_form.getBIZDATE(),rptfee_11_form.getOpt_id() , rptfee_11_form.getBGBK_ID(), rptfee_11_form.getCLEARINGPHASE() ,rptfee_11_form.getOpt_bank() ,rptfee_11_form.getSENDERID(),rptfee_11_form.getTXN_ID() ,rptfee_11_form.getTXTYPE() , rptfee_11_form.getFEE_TYPE() ,  rptfee_11_form.getSerchStrs() );
			BeanUtils.populate(rptfee_11_form, map);
			if(map.get("result").equals("TRUE") && !Arguments.getStringArg("ISWINOPEN").equals("Y")){
				exportFile2Client(response, map.get("msg").toString(), zDateHandler.getDateNum()+"_fee_11.pdf" ,rptfee_11_form.getDow_token() );
				return null;
			}

			
		}
		if(StrUtils.isEmpty(ac_key)){
		}
		rptfee_11_form.setIdList(txn_code_bo.getIdList());
		rptfee_11_form.setBIZDATE(eachsysstatustab_bo.getRptBusinessDate()); 
		rptfee_11_form.setOpt_bankList(bank_group_bo.getOpbkList());
		log.debug("txdt>>"+rptfee_11_form.getTXDT());
		target = StrUtils.isEmpty(rptfee_11_form.getTarget())?"":rptfee_11_form.getTarget();
		return mapping.findForward(target);
	}
	

	public RPTFEE_11_BO getRptfee_11_bo() {
		return rptfee_11_bo;
	}


	public void setRptfee_11_bo(RPTFEE_11_BO rptfee_11_bo) {
		this.rptfee_11_bo = rptfee_11_bo;
	}


	public BANK_GROUP_Dao getBank_group_Dao() {
		return bank_group_Dao;
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


	public TXN_CODE_BO getTxn_code_bo() {
		return txn_code_bo;
	}


	public void setTxn_code_bo(TXN_CODE_BO txn_code_bo) {
		this.txn_code_bo = txn_code_bo;
	}

	
	
}
