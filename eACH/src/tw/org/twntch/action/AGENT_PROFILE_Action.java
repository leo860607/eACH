package tw.org.twntch.action;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import tw.org.twntch.bo.AGENT_PROFILE_BO;
import tw.org.twntch.form.Agent_Profile_Form;
import tw.org.twntch.po.AGENT_PROFILE;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.StrUtils;

public class AGENT_PROFILE_Action extends Action{
	private AGENT_PROFILE_BO agent_profile_bo;
	
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Agent_Profile_Form agent_profile_form = (Agent_Profile_Form) form;
		String ac_key = StrUtils.isEmpty(agent_profile_form.getAc_key())?"":agent_profile_form.getAc_key();
		String target = StrUtils.isEmpty(agent_profile_form.getTarget())?"search":agent_profile_form.getTarget();
		agent_profile_form.setTarget(target);
		System.out.println("AGENT_PROFILE_Action is start >> " + ac_key);
		
		List<AGENT_PROFILE> list = null;
		if(ac_key.equalsIgnoreCase("back")){
			BeanUtils.populate(agent_profile_form, JSONUtils.json2map(agent_profile_form.getSerchStrs()));
			agent_profile_form.setCOMPANY_NAME(URLDecoder.decode(agent_profile_form.getCOMPANY_NAME(),"UTF-8"));
		}
		
		if(ac_key.equalsIgnoreCase("save")){
			System.out.println("### SERCHSTR = " + agent_profile_form.getSerchStrs());
			Map map = agent_profile_bo.save(BeanUtils.describe(agent_profile_form));
			BeanUtils.populate(agent_profile_form, map);
			if(map.get("result").equals("TRUE")){
				BeanUtils.populate(agent_profile_form, JSONUtils.json2map(agent_profile_form.getSerchStrs()));
				agent_profile_form.setCOMPANY_NAME(URLDecoder.decode(agent_profile_form.getCOMPANY_NAME(),"UTF-8"));
			}
		}
		
		if(ac_key.equalsIgnoreCase("edit")){
			BeanUtils.populate(agent_profile_form, JSONUtils.json2map(agent_profile_form.getEdit_params()));
			Map<String, String> searchParam = new HashMap<String, String>();
			searchParam.put("COMPANY_ID", agent_profile_form.getCOMPANY_ID());
			list = agent_profile_bo.search(searchParam);
			if(list != null && list.size() > 0){
				for(AGENT_PROFILE po : list){
					BeanUtils.copyProperties(agent_profile_form, po);
					agent_profile_form.setACTIVE_DATE(agent_profile_form.getACTIVE_DATE());
					agent_profile_form.setSTOP_DATE(agent_profile_form.getSTOP_DATE());
//					agent_profile_form.setACTIVE_DATE(DateTimeUtils.convertDate(agent_profile_form.getACTIVE_DATE(), "yyyy-MM-dd", "yyyyMMdd"));
//					agent_profile_form.setSTOP_DATE(DateTimeUtils.convertDate(agent_profile_form.getSTOP_DATE(), "yyyy-MM-dd", "yyyyMMdd"));
				}
			}
			else{
				agent_profile_form.setAc_key("back");
				agent_profile_form.setTarget("search");
				agent_profile_form.setMsg("無法查詢資料，請確認以下資料是否存在：代理業者統一編號");
				agent_profile_form.setResult("FALSE");
			}
		}
		
		if(ac_key.equalsIgnoreCase("delete")){
			Map map = agent_profile_bo.delete(BeanUtils.describe(agent_profile_form));
			BeanUtils.populate(agent_profile_form, map);
			if(map.get("result").equals("TRUE")){
				BeanUtils.populate(agent_profile_form, JSONUtils.json2map(agent_profile_form.getSerchStrs()));
				agent_profile_form.setCOMPANY_NAME(URLDecoder.decode(agent_profile_form.getCOMPANY_NAME(),"UTF-8"));
		    }
		}
		
		if(ac_key.equalsIgnoreCase("update")){
			System.out.println("### SERCHSTR = " + agent_profile_form.getSerchStrs());
			Map map = agent_profile_bo.update(BeanUtils.describe(agent_profile_form));
			BeanUtils.populate(agent_profile_form, map);
			if(map.get("result").equals("TRUE")){
				BeanUtils.populate(agent_profile_form, JSONUtils.json2map(agent_profile_form.getSerchStrs()));
				agent_profile_form.setCOMPANY_NAME(URLDecoder.decode(agent_profile_form.getCOMPANY_NAME(),"UTF-8"));
		    }
		}
		
		target = StrUtils.isEmpty(agent_profile_form.getTarget())?"":agent_profile_form.getTarget();
		return mapping.findForward(target);
	}
	
	public AGENT_PROFILE_BO getAgent_profile_bo() {
		return agent_profile_bo;
	}

	public void setAgent_profile_bo(AGENT_PROFILE_BO agent_profile_bo) {
		this.agent_profile_bo = agent_profile_bo;
	}

}
