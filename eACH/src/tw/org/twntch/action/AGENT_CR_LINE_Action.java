package tw.org.twntch.action;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import tw.org.twntch.bo.AGENT_CR_LINE_BO;
import tw.org.twntch.form.Agent_CR_Line_Form;
import tw.org.twntch.po.AGENT_CR_LINE;
import tw.org.twntch.po.CR_LINE;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.StrUtils;

public class AGENT_CR_LINE_Action extends GenericAction{

	private AGENT_CR_LINE_BO agent_cr_line_bo;

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// TODO Auto-generated method stub
		Agent_CR_Line_Form   agent_cr_line_form =  (Agent_CR_Line_Form) form ;
		
		String ac_key = StrUtils.isEmpty(agent_cr_line_form.getAc_key())?"":agent_cr_line_form.getAc_key();
		String target = StrUtils.isEmpty(agent_cr_line_form.getTarget())?"search":agent_cr_line_form.getTarget();
		agent_cr_line_form.setTarget(target);
		List<AGENT_CR_LINE> list = null;
		System.out.println("ac_key>>"+ac_key);
		if(ac_key.equalsIgnoreCase("search")|| ac_key.equalsIgnoreCase("back") ){
			System.out.println("SerchStrs>>"+agent_cr_line_form.getSerchStrs());
			BeanUtils.populate(agent_cr_line_form, JSONUtils.json2map(agent_cr_line_form.getSerchStrs()));
		}
		if(ac_key.equalsIgnoreCase("save")){
			 Map map = agent_cr_line_bo.save(agent_cr_line_form.getSND_COMPANY_ID(),BeanUtils.describe(agent_cr_line_form));
			 BeanUtils.populate(agent_cr_line_form, map);
			 if(map.get("result").equals("TRUE")){
					BeanUtils.populate(agent_cr_line_form, JSONUtils.json2map(agent_cr_line_form.getSerchStrs()));
			 }
		}
		if(ac_key.equalsIgnoreCase("add")){
		}
		if(ac_key.equalsIgnoreCase("edit")){
			BeanUtils.populate(agent_cr_line_form, JSONUtils.json2map(agent_cr_line_form.getEdit_params()));
			list = agent_cr_line_bo.search(agent_cr_line_form.getSND_COMPANY_ID());
			for(AGENT_CR_LINE po :list){
				BeanUtils.copyProperties(agent_cr_line_form, po);
				agent_cr_line_form.setOLD_BASIC_CR_LINE(agent_cr_line_form.getBASIC_CR_LINE());
				agent_cr_line_form.setOLD_REST_CR_LINE(agent_cr_line_form.getREST_CR_LINE());
			}
			System.out.println("edit.agent_cr_line_form.getSerchStrs()>>"+agent_cr_line_form.getSerchStrs());
			if(StrUtils.isNotEmpty(agent_cr_line_form.getSerchStrs())){
				System.out.println("edit.json2map>>"+ JSONUtils.json2map(agent_cr_line_form.getSerchStrs()) );
			}
		}
		if(ac_key.equalsIgnoreCase("update")){
			Map map =agent_cr_line_bo.update(agent_cr_line_form.getSND_COMPANY_ID(),BeanUtils.describe(agent_cr_line_form));
			BeanUtils.populate(agent_cr_line_form, map);
			if(map.get("result").equals("TRUE")){
				BeanUtils.populate(agent_cr_line_form, JSONUtils.json2map(agent_cr_line_form.getSerchStrs()));
			}
		}
		if(ac_key.equalsIgnoreCase("delete")){
			Map map =agent_cr_line_bo.delete(agent_cr_line_form.getSND_COMPANY_ID());
			BeanUtils.populate(agent_cr_line_form, map);
			if(map.get("result").equals("TRUE")){
				BeanUtils.populate(agent_cr_line_form, JSONUtils.json2map(agent_cr_line_form.getSerchStrs()));
			}
			//agent_cr_line_form.setAc_key("");
		}
		return mapping.findForward(target);
	}

	public AGENT_CR_LINE_BO getAgent_cr_line_bo() {
		return agent_cr_line_bo;
	}

	public void setAgent_cr_line_bo(AGENT_CR_LINE_BO agent_cr_line_bo) {
		this.agent_cr_line_bo = agent_cr_line_bo;
	}

	
	
	
}
