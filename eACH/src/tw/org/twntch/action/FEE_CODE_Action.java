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

import tw.org.twntch.bo.FEE_CODE_BO;
import tw.org.twntch.form.Fee_Code_Form;
import tw.org.twntch.po.FEE_CODE;
import tw.org.twntch.po.FEE_CODE_PK;
import tw.org.twntch.util.BeanUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.zDateHandler;

public class FEE_CODE_Action extends Action{
	private FEE_CODE_BO fee_code_bo;
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// TODO Auto-generated method stub
		System.out.println("FEE_CODE_Action is start");
		
		Fee_Code_Form fee_code_form = (Fee_Code_Form) form;
		String ac_key = StrUtils.isEmpty(fee_code_form.getAc_key())?"":fee_code_form.getAc_key();
		String target = StrUtils.isEmpty(fee_code_form.getTarget())?"search":fee_code_form.getTarget();
		fee_code_form.setTarget(target);
		System.out.println("ac_key>>"+ac_key);
		List<FEE_CODE> list = null;
		if(ac_key.equalsIgnoreCase("search")|| ac_key.equalsIgnoreCase("back") ){
			System.out.println("SerchStrs>>"+fee_code_form.getSerchStrs());
			BeanUtils.populate(fee_code_form, JSONUtils.json2map(fee_code_form.getSerchStrs()));
			fee_code_form.setResult("TRUE");
		}
		if(ac_key.equalsIgnoreCase("add")){
			FEE_CODE po = new FEE_CODE();
			FEE_CODE_PK pk = new FEE_CODE_PK(fee_code_form.getFEE_ID(), fee_code_form.getSTART_DATE());
			po.setId(pk);
			po.setOUT_BANK_FEE( new BigDecimal( StrUtils.isEmpty(fee_code_form.getOUT_BANK_FEE())?"0":fee_code_form.getOUT_BANK_FEE() ).setScale(2) );
			po.setOUT_BANK_FEE_DISC( new BigDecimal( StrUtils.isEmpty(fee_code_form.getOUT_BANK_FEE_DISC())?"0":fee_code_form.getOUT_BANK_FEE_DISC() ).setScale(2) );
			po.setIN_BANK_FEE( new BigDecimal( StrUtils.isEmpty(fee_code_form.getIN_BANK_FEE())?"0":fee_code_form.getIN_BANK_FEE() ).setScale(2) );
			po.setIN_BANK_FEE_DISC( new BigDecimal( StrUtils.isEmpty(fee_code_form.getIN_BANK_FEE_DISC())?"0":fee_code_form.getIN_BANK_FEE_DISC() ).setScale(2) );
			po.setTCH_FEE( new BigDecimal( StrUtils.isEmpty(fee_code_form.getTCH_FEE())?"0":fee_code_form.getTCH_FEE() ).setScale(2) );
			po.setTCH_FEE_DISC( new BigDecimal( StrUtils.isEmpty(fee_code_form.getTCH_FEE_DISC())?"0":fee_code_form.getTCH_FEE_DISC() ).setScale(2) );
			po.setSND_BANK_FEE( new BigDecimal( StrUtils.isEmpty(fee_code_form.getSND_BANK_FEE())?"0":fee_code_form.getSND_BANK_FEE() ).setScale(2) );
			po.setSND_BANK_FEE_DISC( new BigDecimal( StrUtils.isEmpty(fee_code_form.getSND_BANK_FEE_DISC())?"0":fee_code_form.getSND_BANK_FEE_DISC() ).setScale(2) );
			po.setWO_BANK_FEE( new BigDecimal( StrUtils.isEmpty(fee_code_form.getWO_BANK_FEE())?"0":fee_code_form.getWO_BANK_FEE() ).setScale(2) );
			po.setWO_BANK_FEE_DISC( new BigDecimal( StrUtils.isEmpty(fee_code_form.getWO_BANK_FEE_DISC())?"0":fee_code_form.getWO_BANK_FEE_DISC() ).setScale(2) );
			po.setHANDLECHARGE( new BigDecimal( StrUtils.isEmpty(fee_code_form.getHANDLECHARGE())?"0":fee_code_form.getHANDLECHARGE() ).setScale(2) );
			po.setFEE_DESC(fee_code_form.getFEE_DESC());
			po.setACTIVE_DESC(fee_code_form.getACTIVE_DESC());
			po.setCDATE(zDateHandler.getTheDateII());
			Map map = fee_code_bo.save(po);
			BeanUtils.populate(fee_code_form, map);
			if(map.get("result").equals("TRUE")){
				BeanUtils.populate(fee_code_form, JSONUtils.json2map(fee_code_form.getSerchStrs()));
			}
			list = fee_code_bo.search(fee_code_form.getFEE_ID(),fee_code_form.getSTART_DATE());
			fee_code_form.setJsonList(JSONUtils.toJson(list));
			fee_code_form.setScaseary(list);
		}
		if(ac_key.equalsIgnoreCase("edit")){
			BeanUtils.populate(fee_code_form, JSONUtils.json2map(fee_code_form.getEdit_params()));
			list = fee_code_bo.search(fee_code_form.getFEE_ID(), fee_code_form.getSTART_DATE());
			for(FEE_CODE po : list){
				BeanUtils.copyProperties(fee_code_form, po);
			}
		}
		if(ac_key.equalsIgnoreCase("update")){
			FEE_CODE po = new FEE_CODE();
			FEE_CODE_PK pk = new FEE_CODE_PK(fee_code_form.getFEE_ID(), fee_code_form.getSTART_DATE());
			po.setId(pk);
			po.setOUT_BANK_FEE( new BigDecimal( StrUtils.isEmpty(fee_code_form.getOUT_BANK_FEE())?"0":fee_code_form.getOUT_BANK_FEE() ).setScale(2) );
			po.setOUT_BANK_FEE_DISC( new BigDecimal( StrUtils.isEmpty(fee_code_form.getOUT_BANK_FEE_DISC())?"0":fee_code_form.getOUT_BANK_FEE_DISC() ).setScale(2) );
			po.setIN_BANK_FEE( new BigDecimal( StrUtils.isEmpty(fee_code_form.getIN_BANK_FEE())?"0":fee_code_form.getIN_BANK_FEE() ).setScale(2) );
			po.setIN_BANK_FEE_DISC( new BigDecimal( StrUtils.isEmpty(fee_code_form.getIN_BANK_FEE_DISC())?"0":fee_code_form.getIN_BANK_FEE_DISC() ).setScale(2) );
			po.setTCH_FEE( new BigDecimal( StrUtils.isEmpty(fee_code_form.getTCH_FEE())?"0":fee_code_form.getTCH_FEE() ).setScale(2) );
			po.setTCH_FEE_DISC( new BigDecimal( StrUtils.isEmpty(fee_code_form.getTCH_FEE_DISC())?"0":fee_code_form.getTCH_FEE_DISC() ).setScale(2) );
			po.setSND_BANK_FEE( new BigDecimal( StrUtils.isEmpty(fee_code_form.getSND_BANK_FEE())?"0":fee_code_form.getSND_BANK_FEE() ).setScale(2) );
			po.setSND_BANK_FEE_DISC( new BigDecimal( StrUtils.isEmpty(fee_code_form.getSND_BANK_FEE_DISC())?"0":fee_code_form.getSND_BANK_FEE_DISC() ).setScale(2) );
			po.setWO_BANK_FEE( new BigDecimal( StrUtils.isEmpty(fee_code_form.getWO_BANK_FEE())?"0":fee_code_form.getWO_BANK_FEE() ).setScale(2) );
			po.setWO_BANK_FEE_DISC( new BigDecimal( StrUtils.isEmpty(fee_code_form.getWO_BANK_FEE_DISC())?"0":fee_code_form.getWO_BANK_FEE_DISC() ).setScale(2) );
			po.setHANDLECHARGE( new BigDecimal( StrUtils.isEmpty(fee_code_form.getHANDLECHARGE())?"0":fee_code_form.getHANDLECHARGE() ).setScale(2) );
			po.setFEE_DESC(fee_code_form.getFEE_DESC());
			po.setACTIVE_DESC(fee_code_form.getACTIVE_DESC());
			Map map = fee_code_bo.update(po);
			if(map.get("result").equals("TRUE")){
				BeanUtils.populate(fee_code_form, JSONUtils.json2map(fee_code_form.getSerchStrs()));
			}
			BeanUtils.populate(fee_code_form, map);
			
		}
		if(ac_key.equalsIgnoreCase("delete")){
			Map map = fee_code_bo.delete(fee_code_form.getFEE_ID(), fee_code_form.getSTART_DATE());
			if(map.get("result").equals("TRUE")){
				BeanUtils.populate(fee_code_form, JSONUtils.json2map(fee_code_form.getSerchStrs()));
			}
			BeanUtils.populate(fee_code_form, map);
		}
		target = StrUtils.isEmpty(fee_code_form.getTarget())?"":fee_code_form.getTarget();
		//FEE_ID下拉清單
		//fee_code_form.setIdList(fee_code_bo.getIdList());
		fee_code_form.setIdList(fee_code_bo.getIdListJoinTxnName());
		return mapping.findForward(target);
	}
	public FEE_CODE_BO getFee_code_bo() {
		return fee_code_bo;
	}
	public void setFee_code_bo(FEE_CODE_BO fee_code_bo) {
		this.fee_code_bo = fee_code_bo;
	}
}
