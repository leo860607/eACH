package tw.org.twntch.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import tw.org.twntch.bo.EACH_BATCH_BO;
import tw.org.twntch.bo.NONCLEAR_BATCH_BO;
import tw.org.twntch.form.Each_Batch_Form;
import tw.org.twntch.po.EACH_BATCH_DEF;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.StrUtils;

public class NONCLEAR_BATCH_Action extends GenericAction{
private EACH_BATCH_BO each_batch_bo ;
private NONCLEAR_BATCH_BO nonclear_batch_bo ;
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String target = "";
		Each_Batch_Form each_batch_form = 	(Each_Batch_Form) form ;
		String ac_key = StrUtils.isEmpty(each_batch_form.getAc_key())?"":each_batch_form.getAc_key();
		target = StrUtils.isEmpty(each_batch_form.getTarget())?"search":each_batch_form.getTarget();
		each_batch_form.setTarget(target);
		List<EACH_BATCH_DEF> list = null;
		System.out.println("ac_key>>"+ac_key);
		if(ac_key.equalsIgnoreCase("search")|| ac_key.equalsIgnoreCase("back") ){
		}
		if(ac_key.equalsIgnoreCase("save")){
		}
		if(ac_key.equalsIgnoreCase("add")){
		}
		if(ac_key.equalsIgnoreCase("edit")){
		}
		if(ac_key.equalsIgnoreCase("update")){
		}
		if(ac_key.equalsIgnoreCase("delete")){
		}
		if(ac_key.equalsIgnoreCase("notify")){
		}
//		target = StrUtils.isEmpty(each_batch_form.getTarget())?"":each_batch_form.getTarget();
		return mapping.findForward(target);
	}
	
	
	
	public EACH_BATCH_BO getEach_batch_bo() {
		return each_batch_bo;
	}
	public void setEach_batch_bo(EACH_BATCH_BO each_batch_bo) {
		this.each_batch_bo = each_batch_bo;
	}



	public NONCLEAR_BATCH_BO getNonclear_batch_bo() {
		return nonclear_batch_bo;
	}



	public void setNonclear_batch_bo(NONCLEAR_BATCH_BO nonclear_batch_bo) {
		this.nonclear_batch_bo = nonclear_batch_bo;
	}
	
	

}
