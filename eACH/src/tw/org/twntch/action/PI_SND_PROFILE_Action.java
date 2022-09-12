package tw.org.twntch.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import tw.org.twntch.bo.PI_COMPANY_PROFILE_BO;
import tw.org.twntch.bo.PI_SND_PROFILE_BO;
import tw.org.twntch.form.PI_SND_Profile_Form;
import tw.org.twntch.po.PI_SND_PROFILE;
import tw.org.twntch.po.SC_COMPANY_PROFILE;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.StrUtils;

public class PI_SND_PROFILE_Action extends GenericAction{
	private PI_COMPANY_PROFILE_BO pi_company_profile_bo;
	private PI_SND_PROFILE_BO pi_snd_profile_bo;
	
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		PI_SND_Profile_Form pi_snd_profile_form = (PI_SND_Profile_Form) form;
		String ac_key = StrUtils.isEmpty(pi_snd_profile_form.getAc_key())?"":pi_snd_profile_form.getAc_key();
		String target = StrUtils.isEmpty(pi_snd_profile_form.getTarget())?"search":pi_snd_profile_form.getTarget();
		List<SC_COMPANY_PROFILE> list = null;
		System.out.println("PI_COMPANY_PROFILE_Action is start >> " + ac_key);
		
		pi_snd_profile_form.setBill_type_IdList(pi_company_profile_bo.getIdListByBillType());
		pi_snd_profile_form.setPi_com_IdList(pi_snd_profile_bo.getPI_IdList());
		if(ac_key.equalsIgnoreCase("")){}
		if(ac_key.equalsIgnoreCase("add")){
//			pi_snd_profile_form.setDEALY_CHARGE_DAY("0");
		}
		if(ac_key.equalsIgnoreCase("save")){
			Map map = pi_snd_profile_bo.save(BeanUtils.describe(pi_snd_profile_form));
			BeanUtils.populate(pi_snd_profile_form, map);
			if(map.get("result").equals("TRUE")){
				BeanUtils.populate(pi_snd_profile_form, JSONUtils.json2map(pi_snd_profile_form.getSerchStrs()));
//				pi_snd_profile_form.setPI_COMPANY_NAME(java.net.URLDecoder.decode(pi_snd_profile_form.getPI_COMPANY_NAME(), "UTF-8"));
			}
			target = StrUtils.isEmpty((String)map.get("target"))?"add_p":(String)map.get("target");
		}
		if(ac_key.equalsIgnoreCase("search") || ac_key.equalsIgnoreCase("back") ){
			BeanUtils.populate(pi_snd_profile_form, JSONUtils.json2map(pi_snd_profile_form.getSerchStrs()));
//			pi_snd_profile_form.setPI_COMPANY_NAME(java.net.URLDecoder.decode(pi_snd_profile_form.getPI_COMPANY_NAME(), "UTF-8"));
		}
		if(ac_key.equalsIgnoreCase("edit")){
			System.out.println("Edit_params()>>"+pi_snd_profile_form.getEdit_params());
			BeanUtils.populate(pi_snd_profile_form, JSONUtils.json2map(pi_snd_profile_form.getEdit_params()));
			PI_SND_PROFILE po = pi_snd_profile_bo.getByPk(pi_snd_profile_form.getPI_COMPANY_ID(),pi_snd_profile_form.getSND_COMPANY_ID(),pi_snd_profile_form.getBILL_TYPE_ID());
			if(po != null){
				BeanUtils.copyProperties(pi_snd_profile_form, po);
				BeanUtils.copyProperties(pi_snd_profile_form, po.getId());
				//轉換日期格式
				pi_snd_profile_form.setSTART_DATE(StrUtils.isEmpty(pi_snd_profile_form.getSTART_DATE())?"":DateTimeUtils.convertDate(pi_snd_profile_form.getSTART_DATE(), "yyyyMMdd", "yyyyMMdd"));
				pi_snd_profile_form.setSTOP_DATE(StrUtils.isEmpty(pi_snd_profile_form.getSTOP_DATE())?"":DateTimeUtils.convertDate(pi_snd_profile_form.getSTOP_DATE(), "yyyyMMdd", "yyyyMMdd"));
			}else{
				pi_snd_profile_form.setAc_key("back");
				pi_snd_profile_form.setTarget("search");
				pi_snd_profile_form.setMsg("無法查詢資料，請確認以下資料是否存在：代收項目代號");
				pi_snd_profile_form.setResult("FALSE");
			}
		}
		if(ac_key.equalsIgnoreCase("update")){
			Map map = pi_snd_profile_bo.update(BeanUtils.describe(pi_snd_profile_form));
			BeanUtils.populate(pi_snd_profile_form, map);
			if(map.get("result").equals("TRUE")){
				BeanUtils.populate(pi_snd_profile_form, JSONUtils.json2map(pi_snd_profile_form.getSerchStrs()));
//				pi_snd_profile_form.setPI_COMPANY_NAME(java.net.URLDecoder.decode(pi_snd_profile_form.getPI_COMPANY_NAME(), "UTF-8"));
		    }else{
		    	target = "edit_p";
		    }
		}
		if(ac_key.equalsIgnoreCase("delete")){
			Map map = pi_snd_profile_bo.delete(BeanUtils.describe(pi_snd_profile_form));
			BeanUtils.populate(pi_snd_profile_form, map);
			if(map.get("result").equals("TRUE")){
				BeanUtils.populate(pi_snd_profile_form, JSONUtils.json2map(pi_snd_profile_form.getSerchStrs()));
//				pi_snd_profile_form.setPI_COMPANY_NAME(java.net.URLDecoder.decode(pi_snd_profile_form.getPI_COMPANY_NAME(), "UTF-8"));
		    }
		}
		
		//target = StrUtils.isEmpty(pi_snd_profile_form.getTarget())?"":pi_snd_profile_form.getTarget();
		target = StrUtils.isEmpty(target)?( (StrUtils.isEmpty(pi_snd_profile_form.getTarget()))?"search":pi_snd_profile_form.getTarget() ):target;
		
		return mapping.findForward(target);
	}
	
	
	public PI_COMPANY_PROFILE_BO getPi_company_profile_bo() {
		return pi_company_profile_bo;
	}


	public void setPi_company_profile_bo(PI_COMPANY_PROFILE_BO pi_company_profile_bo) {
		this.pi_company_profile_bo = pi_company_profile_bo;
	}


	public PI_SND_PROFILE_BO getPi_snd_profile_bo() {
		return pi_snd_profile_bo;
	}


	public void setPi_snd_profile_bo(PI_SND_PROFILE_BO pi_snd_profile_bo) {
		this.pi_snd_profile_bo = pi_snd_profile_bo;
	}

	

}
