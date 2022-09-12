package tw.org.twntch.action;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import tw.org.twntch.bo.AGENT_FEE_CODE_BO;
import tw.org.twntch.bo.AGENT_PROFILE_BO;
import tw.org.twntch.bo.AGENT_SEND_PROFILE_BO;
import tw.org.twntch.bo.Arguments;
import tw.org.twntch.bo.FEE_CODE_BO;
import tw.org.twntch.bo.TXN_CODE_BO;
import tw.org.twntch.form.Agent_Fee_Code_Form;
import tw.org.twntch.form.Agent_Send_Profile_Form;
import tw.org.twntch.po.AGENT_FEE_CODE;
import tw.org.twntch.po.AGENT_FEE_CODE_PK;
import tw.org.twntch.po.AGENT_SEND_PROFILE;
import tw.org.twntch.po.AGENT_SEND_PROFILE_PK;
import tw.org.twntch.util.BeanUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.zDateHandler;

public class AGENT_SEND_PROFILE_Action extends GenericAction{
private AGENT_SEND_PROFILE_BO agent_send_profile_bo ;
private AGENT_FEE_CODE_BO agent_fee_code_bo;
private AGENT_PROFILE_BO agent_profile_bo;
private FEE_CODE_BO fee_code_bo;
private TXN_CODE_BO txn_code_bo;
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// TODO Auto-generated method stub
		Agent_Send_Profile_Form agent_send_profile_form = (Agent_Send_Profile_Form) form;
		String ac_key = StrUtils.isEmpty(agent_send_profile_form.getAc_key())?"":agent_send_profile_form.getAc_key();
		String target = StrUtils.isEmpty(agent_send_profile_form.getTarget())?"search":agent_send_profile_form.getTarget();
		System.out.println("AGENT_SEND_PROFILE_Action is start >> " + ac_key);
		agent_send_profile_form.setTxnIdList(txn_code_bo.getIdList());
		agent_send_profile_form.setCompanyIdList(agent_profile_bo.getCompany_Id_List());
		agent_send_profile_form.setTarget(target);
		if(ac_key.equalsIgnoreCase("search")|| ac_key.equalsIgnoreCase("back") ){
			System.out.println("SerchStrs>>"+agent_send_profile_form.getSerchStrs());
			BeanUtils.populate(agent_send_profile_form, JSONUtils.json2map(agent_send_profile_form.getSerchStrs()));
			agent_send_profile_form.setResult("TRUE");
		}
		
		if(ac_key.equalsIgnoreCase("export")){
			Map map = agent_send_profile_bo.agent_ex_export(
					agent_send_profile_form.getSerchStrs(),
					agent_send_profile_form.getSortname(),
					agent_send_profile_form.getSortorder()
			);
			BeanUtils.populate(agent_send_profile_form, map);
			if(map.get("result").equals("TRUE") && !Arguments.getStringArg("ISWINOPEN").equals("Y")){
				exportFile2Client(response, map.get("msg").toString(), zDateHandler.getDateNum()+"_chg_sc_profile.xls" ,agent_send_profile_form.getDow_token() );
				return null;
			}
		}
		if(ac_key.equalsIgnoreCase("add")){
		}
		if(ac_key.equalsIgnoreCase("save")){
			AGENT_SEND_PROFILE_PK id = new AGENT_SEND_PROFILE_PK();
			AGENT_SEND_PROFILE po = new AGENT_SEND_PROFILE();
			BeanUtils.copyProperties(id , agent_send_profile_form);
			BeanUtils.copyProperties(po , agent_send_profile_form);
			po.setId(id);
			Map map = agent_send_profile_bo.save(po);
			BeanUtils.populate(agent_send_profile_form, map);
			if(map.get("result").equals("TRUE")){
				BeanUtils.populate(agent_send_profile_form, JSONUtils.json2map(agent_send_profile_form.getSerchStrs()));
			}
			BeanUtils.populate(agent_send_profile_form, map);
			System.out.println("map>>"+map);
		}
		

		if(ac_key.equalsIgnoreCase("edit")){
			BeanUtils.populate(agent_send_profile_form, JSONUtils.json2map(agent_send_profile_form.getEdit_params()));
			List<AGENT_SEND_PROFILE> list = agent_send_profile_bo.search(agent_send_profile_form.getCOMPANY_ID(), agent_send_profile_form.getSND_COMPANY_ID(),agent_send_profile_form.getTXN_ID());
			if(list!=null){
				for(AGENT_SEND_PROFILE  po :list){
					BeanUtils.copyProperties(agent_send_profile_form, po);
//					BeanUtils.copyProperties(agent_send_profile_form, po.getId());
				}
			}
		}
		
		if(ac_key.equalsIgnoreCase("update")){
			AGENT_SEND_PROFILE po = new AGENT_SEND_PROFILE();
			AGENT_SEND_PROFILE_PK pk = new AGENT_SEND_PROFILE_PK( agent_send_profile_form.getTXN_ID(),agent_send_profile_form.getCOMPANY_ID() , agent_send_profile_form.getSND_COMPANY_ID());
			BeanUtils.copyProperties(po, agent_send_profile_form);
			po.setId(pk);
			
			Map map = agent_send_profile_bo.update(po);
			if(map.get("result").equals("TRUE")){
				BeanUtils.populate(agent_send_profile_form, JSONUtils.json2map(agent_send_profile_form.getSerchStrs()));
			}
			BeanUtils.populate(agent_send_profile_form, map);
			
		}
		if(ac_key.equalsIgnoreCase("delete")){
			Map map = agent_send_profile_bo.delete(agent_send_profile_form.getCOMPANY_ID() , agent_send_profile_form.getSND_COMPANY_ID(), agent_send_profile_form.getTXN_ID(), agent_send_profile_form);
			if(map.get("result").equals("TRUE")){
				BeanUtils.populate(agent_send_profile_form, JSONUtils.json2map(agent_send_profile_form.getSerchStrs()));
			}
			BeanUtils.populate(agent_send_profile_form, map);
		}
		target = StrUtils.isEmpty(agent_send_profile_form.getTarget())?"":agent_send_profile_form.getTarget();
		return mapping.findForward(target);
	}

	public AGENT_SEND_PROFILE_BO getAgent_send_profile_bo() {
		return agent_send_profile_bo;
	}

	public void setAgent_send_profile_bo(AGENT_SEND_PROFILE_BO agent_send_profile_bo) {
		this.agent_send_profile_bo = agent_send_profile_bo;
	}

	public FEE_CODE_BO getFee_code_bo() {
		return fee_code_bo;
	}

	public void setFee_code_bo(FEE_CODE_BO fee_code_bo) {
		this.fee_code_bo = fee_code_bo;
	}

	public TXN_CODE_BO getTxn_code_bo() {
		return txn_code_bo;
	}

	public void setTxn_code_bo(TXN_CODE_BO txn_code_bo) {
		this.txn_code_bo = txn_code_bo;
	}

	public AGENT_FEE_CODE_BO getAgent_fee_code_bo() {
		return agent_fee_code_bo;
	}

	public void setAgent_fee_code_bo(AGENT_FEE_CODE_BO agent_fee_code_bo) {
		this.agent_fee_code_bo = agent_fee_code_bo;
	}

	public AGENT_PROFILE_BO getAgent_profile_bo() {
		return agent_profile_bo;
	}

	public void setAgent_profile_bo(AGENT_PROFILE_BO agent_profile_bo) {
		this.agent_profile_bo = agent_profile_bo;
	}
	
	
	

}
