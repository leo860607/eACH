package tw.org.twntch.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import tw.org.twntch.bo.Arguments;
import tw.org.twntch.bo.BANK_BRANCH_BO;
import tw.org.twntch.bo.BANK_GROUP_BO;
import tw.org.twntch.bo.CHG_SC_PROFILE_BO;
import tw.org.twntch.bo.EACHSYSSTATUSTAB_BO;
import tw.org.twntch.bo.TXN_CODE_BO;
import tw.org.twntch.form.Chg_Sc_Profile_Form;
import tw.org.twntch.po.BANK_BRANCH;
import tw.org.twntch.po.CHG_SC_PROFILE;
import tw.org.twntch.po.SC_COMPANY_PROFILE;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.zDateHandler;

public class CHG_SC_PROFILE_Action extends GenericAction{
	private CHG_SC_PROFILE_BO chg_sc_profile_bo;
	private BANK_GROUP_BO bank_group_bo;
	private TXN_CODE_BO txn_code_bo ;
	private EACHSYSSTATUSTAB_BO eachsysstatustab_bo;
	private BANK_BRANCH_BO bank_branch_bo;
	
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Chg_Sc_Profile_Form chg_sc_profile_form = (Chg_Sc_Profile_Form) form;
		String ac_key = StrUtils.isEmpty(chg_sc_profile_form.getAc_key())?"":chg_sc_profile_form.getAc_key();
		String target = StrUtils.isEmpty(chg_sc_profile_form.getTarget())?"search":chg_sc_profile_form.getTarget();
		List<SC_COMPANY_PROFILE> list = null;
		System.out.println("CHG_SC_PROFILE_Action is start >> " + ac_key);
		
		chg_sc_profile_form.setTxnIdList(txn_code_bo.getIdListByTxnType("SC"));
		chg_sc_profile_form.setBgbkIdList(bank_group_bo.getBgbkIdList());
		if(ac_key.equalsIgnoreCase("")){}
		if(ac_key.equalsIgnoreCase("add")){
			chg_sc_profile_form.setDEALY_CHARGE_DAY("0");
		}
		if(ac_key.equalsIgnoreCase("save")){
			Map map = chg_sc_profile_bo.save(BeanUtils.describe(chg_sc_profile_form));
			BeanUtils.populate(chg_sc_profile_form, map);
			if(map.get("result").equals("TRUE")){
				BeanUtils.populate(chg_sc_profile_form, JSONUtils.json2map(chg_sc_profile_form.getSerchStrs()));
				chg_sc_profile_form.setCOMPANY_NAME(java.net.URLDecoder.decode(chg_sc_profile_form.getCOMPANY_NAME(), "UTF-8"));
			}
			target = StrUtils.isEmpty((String)map.get("target"))?"add_p":(String)map.get("target");
		}
		if(ac_key.equalsIgnoreCase("search") || ac_key.equalsIgnoreCase("back") ){
			BeanUtils.populate(chg_sc_profile_form, JSONUtils.json2map(chg_sc_profile_form.getSerchStrs()));
			chg_sc_profile_form.setCOMPANY_NAME(java.net.URLDecoder.decode(chg_sc_profile_form.getCOMPANY_NAME(), "UTF-8"));
		}
		if(ac_key.equalsIgnoreCase("edit")){
			BeanUtils.populate(chg_sc_profile_form, JSONUtils.json2map(chg_sc_profile_form.getEdit_params()));
			CHG_SC_PROFILE po = chg_sc_profile_bo.getByPk(chg_sc_profile_form.getSD_ITEM_NO());
			if(po != null){
				BeanUtils.copyProperties(chg_sc_profile_form, po);
				//轉換日期格式
				chg_sc_profile_form.setSTART_DATE(StrUtils.isEmpty(chg_sc_profile_form.getSTART_DATE())?"":DateTimeUtils.convertDate(chg_sc_profile_form.getSTART_DATE(), "yyyy-MM-dd", "yyyyMMdd"));
				chg_sc_profile_form.setSTOP_DATE(StrUtils.isEmpty(chg_sc_profile_form.getSTOP_DATE())?"":DateTimeUtils.convertDate(chg_sc_profile_form.getSTOP_DATE(), "yyyy-MM-dd", "yyyyMMdd"));
				//取得總行代號
				List<BANK_BRANCH> brList = bank_branch_bo.searchByBrbkId(chg_sc_profile_form.getINBANKID());
				if(brList != null && brList.size() > 0){
					chg_sc_profile_form.setBGBK_ID(brList.get(0).getId().getBGBK_ID());
				}else{
					chg_sc_profile_form.setAc_key("back");
					chg_sc_profile_form.setTarget("search");
					chg_sc_profile_form.setMsg("無法查詢資料，請確認以下資料是否存在：入帳行所屬總行");
					chg_sc_profile_form.setResult("FALSE");
				}
			}else{
				chg_sc_profile_form.setAc_key("back");
				chg_sc_profile_form.setTarget("search");
				chg_sc_profile_form.setMsg("無法查詢資料，請確認以下資料是否存在：代收項目代號");
				chg_sc_profile_form.setResult("FALSE");
			}
		}
		if(ac_key.equalsIgnoreCase("update")){
			Map map = chg_sc_profile_bo.update(BeanUtils.describe(chg_sc_profile_form));
			BeanUtils.populate(chg_sc_profile_form, map);
			if(map.get("result").equals("TRUE")){
				BeanUtils.populate(chg_sc_profile_form, JSONUtils.json2map(chg_sc_profile_form.getSerchStrs()));
				chg_sc_profile_form.setCOMPANY_NAME(java.net.URLDecoder.decode(chg_sc_profile_form.getCOMPANY_NAME(), "UTF-8"));
		    }
		}
		if(ac_key.equalsIgnoreCase("delete")){
			Map map = chg_sc_profile_bo.delete(BeanUtils.describe(chg_sc_profile_form));
			BeanUtils.populate(chg_sc_profile_form, map);
			if(map.get("result").equals("TRUE")){
				BeanUtils.populate(chg_sc_profile_form, JSONUtils.json2map(chg_sc_profile_form.getSerchStrs()));
				chg_sc_profile_form.setCOMPANY_NAME(java.net.URLDecoder.decode(chg_sc_profile_form.getCOMPANY_NAME(), "UTF-8"));
		    }
		}
		if(ac_key.equalsIgnoreCase("export")){
			Map map = chg_sc_profile_bo.qs_ex_export(
					chg_sc_profile_form.getSD_ITEM_NO(),
					chg_sc_profile_form.getBGBK_ID(),
					chg_sc_profile_form.getCOMPANY_ID(),
					chg_sc_profile_form.getTXN_ID(),
					chg_sc_profile_form.getCOMPANY_NAME(),
					chg_sc_profile_form.getSerchStrs(),
					chg_sc_profile_form.getSortname(),
					chg_sc_profile_form.getSortorder()
			);
			BeanUtils.populate(chg_sc_profile_form, map);
			if(map.get("result").equals("TRUE") && !Arguments.getStringArg("ISWINOPEN").equals("Y")){
				exportFile2Client(response, map.get("msg").toString(), zDateHandler.getDateNum()+"_chg_sc_profile.xls" ,chg_sc_profile_form.getDow_token() );
				return null;
			}
		}
		
		//target = StrUtils.isEmpty(chg_sc_profile_form.getTarget())?"":chg_sc_profile_form.getTarget();
		target = StrUtils.isEmpty(target)?( (StrUtils.isEmpty(chg_sc_profile_form.getTarget()))?"search":chg_sc_profile_form.getTarget() ):target;
		
		return mapping.findForward(target);
	}
	
	public CHG_SC_PROFILE_BO getChg_sc_profile_bo() {
		return chg_sc_profile_bo;
	}

	public void setChg_sc_profile_bo(CHG_SC_PROFILE_BO chg_sc_profile_bo) {
		this.chg_sc_profile_bo = chg_sc_profile_bo;
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
	
	public EACHSYSSTATUSTAB_BO getEachsysstatustab_bo() {
		return eachsysstatustab_bo;
	}
	
	public void setEachsysstatustab_bo(EACHSYSSTATUSTAB_BO eachsysstatustab_bo) {
		this.eachsysstatustab_bo = eachsysstatustab_bo;
	}

	public BANK_BRANCH_BO getBank_branch_bo() {
		return bank_branch_bo;
	}

	public void setBank_branch_bo(BANK_BRANCH_BO bank_branch_bo) {
		this.bank_branch_bo = bank_branch_bo;
	}

}
