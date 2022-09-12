package tw.org.twntch.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import tw.org.twntch.bo.EACH_BATCH_DEF_BO;
import tw.org.twntch.form.Each_Batch_Def_Form;
import tw.org.twntch.po.CR_LINE;
import tw.org.twntch.po.EACH_BATCH_DEF;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.StrUtils;

public class EACH_BATCH_DEF_Action extends GenericAction{
private EACH_BATCH_DEF_BO each_batch_def_bo ;
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// TODO Auto-generated method stub
		String target = "";
		Each_Batch_Def_Form each_batch_def_form = 	(Each_Batch_Def_Form) form ;
		String ac_key = StrUtils.isEmpty(each_batch_def_form.getAc_key())?"":each_batch_def_form.getAc_key();
		target = StrUtils.isEmpty(each_batch_def_form.getTarget())?"search":each_batch_def_form.getTarget();
		each_batch_def_form.setTarget(target);
		List<EACH_BATCH_DEF> list = null;
		System.out.println("ac_key>>"+ac_key);
		if(ac_key.equalsIgnoreCase("search")|| ac_key.equalsIgnoreCase("back") ){
		}
		if(ac_key.equalsIgnoreCase("save")){
			 Map map = each_batch_def_bo.save(each_batch_def_form.getBATCH_PROC_SEQ(),BeanUtils.describe(each_batch_def_form));
			 BeanUtils.populate(each_batch_def_form, map);
			 list = each_batch_def_bo.search(each_batch_def_form.getBATCH_PROC_SEQ());
			 each_batch_def_form.setScaseary(list);
		}
		if(ac_key.equalsIgnoreCase("add")){
			System.out.println("each_batch_def_form.getSerchStrs()>>"+each_batch_def_form.getSerchStrs());
			if(StrUtils.isNotEmpty(each_batch_def_form.getSerchStrs())){
				System.out.println("json2map>>"+ JSONUtils.json2map(each_batch_def_form.getSerchStrs()) );
			}
		}
		if(ac_key.equalsIgnoreCase("edit")){
			list = each_batch_def_bo.search(each_batch_def_form.getBATCH_PROC_SEQ());
			
			for(EACH_BATCH_DEF po :list){
				BeanUtils.copyProperties(each_batch_def_form, po);
			}
//			System.out.println("edit.each_batch_def_form.getSerchStrs()>>"+each_batch_def_form.getSerchStrs());
//			if(StrUtils.isNotEmpty(each_batch_def_form.getSerchStrs())){
//				System.out.println("edit.json2map>>"+ JSONUtils.json2map(each_batch_def_form.getSerchStrs()) );
//			}
		}
		if(ac_key.equalsIgnoreCase("update")){
			Map map =each_batch_def_bo.update(each_batch_def_form.getBATCH_PROC_SEQ(),BeanUtils.describe(each_batch_def_form));
			BeanUtils.populate(each_batch_def_form, map);
			list = each_batch_def_bo.search(each_batch_def_form.getBATCH_PROC_SEQ());
			each_batch_def_form.setScaseary(list);
//			System.out.println("update.each_batch_def_form.getSerchStrs()>>"+each_batch_def_form.getSerchStrs());
//			if(StrUtils.isNotEmpty(each_batch_def_form.getSerchStrs())){
//				System.out.println("update.json2map>>"+ JSONUtils.json2map(each_batch_def_form.getSerchStrs()) );
//			}
		}
		if(ac_key.equalsIgnoreCase("delete")){
			Map map =each_batch_def_bo.delete(each_batch_def_form.getBATCH_PROC_SEQ());
			BeanUtils.populate(each_batch_def_form, map);
//			each_batch_def_form.setAc_key("");
		}
		target = StrUtils.isEmpty(each_batch_def_form.getTarget())?"":each_batch_def_form.getTarget();
		return mapping.findForward(target);
	}
	public EACH_BATCH_DEF_BO getEach_batch_def_bo() {
		return each_batch_def_bo;
	}
	public void setEach_batch_def_bo(EACH_BATCH_DEF_BO each_batch_def_bo) {
		this.each_batch_def_bo = each_batch_def_bo;
	}
	
	

}
