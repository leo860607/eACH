package tw.org.twntch.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import tw.org.twntch.bo.Arguments;
import tw.org.twntch.bo.BANK_BRANCH_BO;
import tw.org.twntch.bo.BANK_GROUP_BO;
import tw.org.twntch.bo.EACHSYSSTATUSTAB_BO;
import tw.org.twntch.bo.WO_COMPANY_PROFILE_BO;
import tw.org.twntch.bo.TXN_CODE_BO;
import tw.org.twntch.form.WO_Company_Profile_Form;
import tw.org.twntch.po.BANK_BRANCH;
import tw.org.twntch.po.WO_COMPANY_PROFILE;
import tw.org.twntch.po.SC_COMPANY_PROFILE;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.zDateHandler;

public class WO_COMPANY_PROFILE_Action extends GenericAction{
	private WO_COMPANY_PROFILE_BO wo_company_profile_bo;
	private BANK_GROUP_BO bank_group_bo;
	private TXN_CODE_BO txn_code_bo ;
	private EACHSYSSTATUSTAB_BO eachsysstatustab_bo;
	private BANK_BRANCH_BO bank_branch_bo;
	
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		WO_Company_Profile_Form wo_com_profile_form = (WO_Company_Profile_Form) form;
		String ac_key = StrUtils.isEmpty(wo_com_profile_form.getAc_key())?"":wo_com_profile_form.getAc_key();
		String target = StrUtils.isEmpty(wo_com_profile_form.getTarget())?"search":wo_com_profile_form.getTarget();
		List<SC_COMPANY_PROFILE> list = null;
		System.out.println("PI_COMPANY_PROFILE_Action is start >> " + ac_key);
		
		wo_com_profile_form.setTxnIdList(txn_code_bo.getIdListByBS_Id("2700"));
		wo_com_profile_form.setBill_type_IdList(wo_company_profile_bo.getIdListByBillType());
		wo_com_profile_form.setBgbkIdList(bank_group_bo.getBgbkIdList());
		if(ac_key.equalsIgnoreCase("")){}
		if(ac_key.equalsIgnoreCase("add")){
//			wo_com_profile_form.setDEALY_CHARGE_DAY("0");
		}
		if(ac_key.equalsIgnoreCase("save")){
			Map map = wo_company_profile_bo.save(BeanUtils.describe(wo_com_profile_form));
			BeanUtils.populate(wo_com_profile_form, map);
			if(map.get("result").equals("TRUE")){
				BeanUtils.populate(wo_com_profile_form, JSONUtils.json2map(wo_com_profile_form.getSerchStrs()));
				wo_com_profile_form.setWO_COMPANY_NAME(java.net.URLDecoder.decode(wo_com_profile_form.getWO_COMPANY_NAME(), "UTF-8"));
			}
			target = StrUtils.isEmpty((String)map.get("target"))?"add_p":(String)map.get("target");
		}
		if(ac_key.equalsIgnoreCase("search") || ac_key.equalsIgnoreCase("back") ){
			BeanUtils.populate(wo_com_profile_form, JSONUtils.json2map(wo_com_profile_form.getSerchStrs()));
			wo_com_profile_form.setWO_COMPANY_NAME(java.net.URLDecoder.decode(wo_com_profile_form.getWO_COMPANY_NAME(), "UTF-8"));
		}
		if(ac_key.equalsIgnoreCase("edit")){
			System.out.println("Edit_params()>>"+wo_com_profile_form.getEdit_params());
			BeanUtils.populate(wo_com_profile_form, JSONUtils.json2map(wo_com_profile_form.getEdit_params()));
			WO_COMPANY_PROFILE po = wo_company_profile_bo.getByPk(wo_com_profile_form.getWO_COMPANY_ID(),wo_com_profile_form.getBILL_TYPE_ID());
			if(po != null){
				BeanUtils.copyProperties(wo_com_profile_form, po);
				BeanUtils.copyProperties(wo_com_profile_form, po.getId());
				//轉換日期格式
				wo_com_profile_form.setSTART_DATE(StrUtils.isEmpty(wo_com_profile_form.getSTART_DATE())?"":DateTimeUtils.convertDate(wo_com_profile_form.getSTART_DATE(), "yyyyMMdd", "yyyyMMdd"));
				wo_com_profile_form.setSTOP_DATE(StrUtils.isEmpty(wo_com_profile_form.getSTOP_DATE())?"":DateTimeUtils.convertDate(wo_com_profile_form.getSTOP_DATE(), "yyyyMMdd", "yyyyMMdd"));
				//取得總行代號
				List<BANK_BRANCH> brList = bank_branch_bo.searchByBrbkId(wo_com_profile_form.getINBANK_ID());
				if(brList != null && brList.size() > 0){
					wo_com_profile_form.setBGBK_ID(brList.get(0).getId().getBGBK_ID());
				}else{
					wo_com_profile_form.setAc_key("back");
					wo_com_profile_form.setTarget("search");
					wo_com_profile_form.setMsg("無法查詢資料，請確認以下資料是否存在：入帳行所屬總行");
					wo_com_profile_form.setResult("FALSE");
				}
			}else{
				wo_com_profile_form.setAc_key("back");
				wo_com_profile_form.setTarget("search");
				wo_com_profile_form.setMsg("無法查詢資料，請確認以下資料是否存在：代收項目代號");
				wo_com_profile_form.setResult("FALSE");
			}
		}
		if(ac_key.equalsIgnoreCase("update")){
			Map map = wo_company_profile_bo.update(BeanUtils.describe(wo_com_profile_form));
			BeanUtils.populate(wo_com_profile_form, map);
			if(map.get("result").equals("TRUE")){
				BeanUtils.populate(wo_com_profile_form, JSONUtils.json2map(wo_com_profile_form.getSerchStrs()));
				wo_com_profile_form.setWO_COMPANY_NAME(java.net.URLDecoder.decode(wo_com_profile_form.getWO_COMPANY_NAME(), "UTF-8"));
		    }else{
		    	target = "edit_p";
		    }
		}
		if(ac_key.equalsIgnoreCase("delete")){
			Map map = wo_company_profile_bo.delete(BeanUtils.describe(wo_com_profile_form));
			BeanUtils.populate(wo_com_profile_form, map);
			if(map.get("result").equals("TRUE")){
				BeanUtils.populate(wo_com_profile_form, JSONUtils.json2map(wo_com_profile_form.getSerchStrs()));
				wo_com_profile_form.setWO_COMPANY_NAME(java.net.URLDecoder.decode(wo_com_profile_form.getWO_COMPANY_NAME(), "UTF-8"));
		    }else{
		    	target = "edit_p";
		    }
		}
		if(ac_key.equalsIgnoreCase("export")){
			Map map = wo_company_profile_bo.qs_ex_export(
					wo_com_profile_form.getWO_COMPANY_ID(),
					wo_com_profile_form.getWO_COMPANY_ID(),
					wo_com_profile_form.getWO_COMPANY_ID(),
					wo_com_profile_form.getTXN_ID(),
					wo_com_profile_form.getWO_COMPANY_NAME(),
					wo_com_profile_form.getSerchStrs(),
					wo_com_profile_form.getSortname(),
					wo_com_profile_form.getSortorder()
			);
			BeanUtils.populate(wo_com_profile_form, map);
			if(map.get("result").equals("TRUE") && !Arguments.getStringArg("ISWINOPEN").equals("Y")){
				exportFile2Client(response, map.get("msg").toString(), zDateHandler.getDateNum()+"_chg_sc_profile.xls" ,wo_com_profile_form.getDow_token() );
				return null;
			}
		}
		
		//target = StrUtils.isEmpty(wo_com_profile_form.getTarget())?"":wo_com_profile_form.getTarget();
		target = StrUtils.isEmpty(target)?( (StrUtils.isEmpty(wo_com_profile_form.getTarget()))?"search":wo_com_profile_form.getTarget() ):target;
		
		return mapping.findForward(target);
	}
	


	public WO_COMPANY_PROFILE_BO getWo_company_profile_bo() {
		return wo_company_profile_bo;
	}

	public void setWo_company_profile_bo(WO_COMPANY_PROFILE_BO wo_company_profile_bo) {
		this.wo_company_profile_bo = wo_company_profile_bo;
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
