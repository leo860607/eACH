package tw.org.twntch.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import tw.org.twntch.bo.TYPH_OP_BO;
import tw.org.twntch.form.Typh_Op_Form;
import tw.org.twntch.util.StrUtils;

public class TYPH_OP_Action extends Action {
	private TYPH_OP_BO typh_op_bo;
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Typh_Op_Form typh_op_form = (Typh_Op_Form) form;
		String ac_key = StrUtils.isEmpty(typh_op_form.getAc_key())?"":typh_op_form.getAc_key();
		String target = StrUtils.isEmpty(typh_op_form.getTarget())?"search":typh_op_form.getTarget();
		typh_op_form.setTarget(target);
		System.out.println("TYPH_OP_Action is start >> " + ac_key);
		
		if(ac_key.equalsIgnoreCase("search")){
			BeanUtils.populate(typh_op_form, typh_op_bo.preExecute(typh_op_form.getSTART_DATE(), typh_op_form.getEND_DATE()));
		}else if(ac_key.equalsIgnoreCase("edit")){
		}else if(ac_key.equalsIgnoreCase("update")){
			BeanUtils.populate(typh_op_form, typh_op_bo.execute(typh_op_form.getSTART_DATE(), typh_op_form.getEND_DATE()));
		}else if(ac_key.equalsIgnoreCase("add")){
		}else if(StrUtils.isEmpty(ac_key)){
		}
		target = StrUtils.isEmpty(typh_op_form.getTarget())?"":typh_op_form.getTarget();
		return mapping.findForward(target);
	}
	public TYPH_OP_BO getTyph_op_bo() {
		return typh_op_bo;
	}
	public void setTyph_op_bo(TYPH_OP_BO typh_op_bo) {
		this.typh_op_bo = typh_op_bo;
	}
}
