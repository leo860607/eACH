package tw.org.twntch.action;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.BigDecimalConverter;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import tw.org.twntch.bo.AGENT_FEE_CODE_BO;
import tw.org.twntch.bo.AGENT_PROFILE_BO;
import tw.org.twntch.bo.FEE_CODE_BO;
import tw.org.twntch.bo.TXN_CODE_BO;
import tw.org.twntch.form.Agent_Fee_Code_Form;
import tw.org.twntch.form.Fee_Code_Form;
import tw.org.twntch.po.AGENT_FEE_CODE;
import tw.org.twntch.po.AGENT_FEE_CODE_PK;
import tw.org.twntch.po.AGENT_PROFILE;
import tw.org.twntch.po.FEE_CODE;
import tw.org.twntch.po.FEE_CODE_PK;
import tw.org.twntch.util.BeanUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.zDateHandler;

public class AGENT_FEE_CODE_Action extends Action{
	private AGENT_FEE_CODE_BO agent_fee_code_bo;
	private AGENT_PROFILE_BO agent_profile_bo;
	
	private FEE_CODE_BO fee_code_bo;
	private TXN_CODE_BO txn_code_bo;
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Agent_Fee_Code_Form agent_fee_code_form = (Agent_Fee_Code_Form) form;
		String ac_key = StrUtils.isEmpty(agent_fee_code_form.getAc_key())?"":agent_fee_code_form.getAc_key();
		String target = StrUtils.isEmpty(agent_fee_code_form.getTarget())?"search":agent_fee_code_form.getTarget();
		agent_fee_code_form.setTarget(target);
		System.out.println("AGENT_FEE_CODE_Action is start >> " + ac_key);
		
//		agent_fee_code_form.setTxnIdList(agent_fee_code_bo.getFeeIdList());
		agent_fee_code_form.setTxnIdList(txn_code_bo.getIdList());
		agent_fee_code_form.setCompanyIdList(agent_profile_bo.getCompany_Id_List());
		
		if(ac_key.equalsIgnoreCase("search")|| ac_key.equalsIgnoreCase("back") ){
			System.out.println("SerchStrs>>"+agent_fee_code_form.getSerchStrs());
			BeanUtils.populate(agent_fee_code_form, JSONUtils.json2map(agent_fee_code_form.getSerchStrs()));
			agent_fee_code_form.setResult("TRUE");
		}
		
		if(ac_key.equalsIgnoreCase("add")){
//			agent_fee_code_form.setTxnIdList(txn_code_bo.getIdList());
		}
		if(ac_key.equalsIgnoreCase("save")){
			AGENT_FEE_CODE_PK id = new AGENT_FEE_CODE_PK();
			AGENT_FEE_CODE po = new AGENT_FEE_CODE();
			BeanUtils.copyProperties(id , agent_fee_code_form);
			BeanUtils.copyProperties(po , agent_fee_code_form);
			po.setId(id);
			Map map = agent_fee_code_bo.save(po);
			BeanUtils.populate(agent_fee_code_form, map);
			if(map.get("result").equals("TRUE")){
				BeanUtils.populate(agent_fee_code_form, JSONUtils.json2map(agent_fee_code_form.getSerchStrs()));
			}
			BeanUtils.populate(agent_fee_code_form, map);
			System.out.println("map>>"+map);
		}
		
		if(ac_key.equalsIgnoreCase("edit")){
			BeanUtils.populate(agent_fee_code_form, JSONUtils.json2map(agent_fee_code_form.getEdit_params()));
			List<AGENT_FEE_CODE> list = agent_fee_code_bo.search(agent_fee_code_form.getFEE_ID(), agent_fee_code_form.getSTART_DATE(),agent_fee_code_form.getCOMPANY_ID());
			if(list!=null){
				for(AGENT_FEE_CODE  po :list){
					BeanUtils.copyProperties(agent_fee_code_form, po);
					BeanUtils.copyProperties(agent_fee_code_form, po.getId());
				}
			}
		}
		
		if(ac_key.equalsIgnoreCase("update")){
			AGENT_FEE_CODE po = new AGENT_FEE_CODE();
			AGENT_FEE_CODE_PK pk = new AGENT_FEE_CODE_PK(agent_fee_code_form.getFEE_ID() ,agent_fee_code_form.getCOMPANY_ID() , agent_fee_code_form.getSTART_DATE());
			po.setId(pk);
			po.setFEE( new BigDecimal( StrUtils.isEmpty(agent_fee_code_form.getFEE())?"0":agent_fee_code_form.getFEE() ).setScale(2) );
			po.setFEE_DESC(agent_fee_code_form.getFEE_DESC());
			po.setACTIVE_DESC(agent_fee_code_form.getACTIVE_DESC());
			Map map = agent_fee_code_bo.update(po);
			if(map.get("result").equals("TRUE")){
				BeanUtils.populate(agent_fee_code_form, JSONUtils.json2map(agent_fee_code_form.getSerchStrs()));
			}
			BeanUtils.populate(agent_fee_code_form, map);
			
		}
		if(ac_key.equalsIgnoreCase("delete")){
			Map map = agent_fee_code_bo.delete(agent_fee_code_form.getFEE_ID(), agent_fee_code_form.getCOMPANY_ID(), agent_fee_code_form.getSTART_DATE() );
			if(map.get("result").equals("TRUE")){
				BeanUtils.populate(agent_fee_code_form, JSONUtils.json2map(agent_fee_code_form.getSerchStrs()));
			}
			BeanUtils.populate(agent_fee_code_form, map);
		}
//		agent_fee_code_form.setIdList(fee_code_bo.getIdListJoinTxnName());
		target = StrUtils.isEmpty(agent_fee_code_form.getTarget())?"":agent_fee_code_form.getTarget();
		System.out.println("target>>"+target);
		return mapping.findForward(target);
	}
	public AGENT_FEE_CODE_BO getAgent_fee_code_bo() {
		return agent_fee_code_bo;
	}
	public void setAgent_fee_code_bo(AGENT_FEE_CODE_BO agent_fee_code_bo) {
		this.agent_fee_code_bo = agent_fee_code_bo;
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
	public AGENT_PROFILE_BO getAgent_profile_bo() {
		return agent_profile_bo;
	}
	public void setAgent_profile_bo(AGENT_PROFILE_BO agent_profile_bo) {
		this.agent_profile_bo = agent_profile_bo;
	}
	
}
