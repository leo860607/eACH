package tw.org.twntch.action;

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

import tw.org.twntch.bo.SYS_PARA_BO;
import tw.org.twntch.form.Sys_Para_Form;
import tw.org.twntch.po.SYS_PARA;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.StrUtils;

public class SYS_PARA_Action extends Action{

	private SYS_PARA_BO sys_para_bo ;

	
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
//		request.getSession().setMaxInactiveInterval(30*60); //session timeout
		System.out.println("SYS_PARA_Action is start");
		String target = "";
		Sys_Para_Form sys_para_form = (Sys_Para_Form) form;
		String ac_key = StrUtils.isEmpty(sys_para_form.getAc_key())?"edit":sys_para_form.getAc_key();
		target = StrUtils.isEmpty(sys_para_form.getTarget())?"edit_p":sys_para_form.getTarget();
		sys_para_form.setTarget(target);
		List<SYS_PARA> list = null;
		if(ac_key.equalsIgnoreCase("search")|| ac_key.equalsIgnoreCase("back") ){
			list = sys_para_bo.search("");
			sys_para_form.setScaseary(list);
		}
//		if(ac_key.equalsIgnoreCase("save")){
//			 list = sys_para_bo.search(sys_para_form.getSEQ_ID());
//			 
//			 Map map = sys_para_bo.save(BeanUtils.describe(sys_para_form));
//			 BeanUtils.populate(sys_para_form, map);
//			 list = sys_para_bo.search("");
//			 sys_para_form.setScaseary(list);
//			 
//		}
//		if(ac_key.equalsIgnoreCase("add")){
//		}
		if(ac_key.equalsIgnoreCase("edit")){
			list = sys_para_bo.search(sys_para_form.getSEQ_ID());
			if(list!=null){
				SYS_PARA po  = list.get(0);
				BeanUtils.copyProperties(sys_para_form, po);
			}
			
//			for(SYS_PARA po :list){
//			}
//			SYS_PARA po = sys_para_bo.searchII(sys_para_form.getSEQ_ID());
			
//			if(po!=null){
//				System.out.println("po.seqid"+po.getSEQ_ID());
//				BeanUtils.copyProperties(sys_para_form, po);
//			}
			System.out.println(sys_para_form.getSEQ_ID());
		}
		if(ac_key.equalsIgnoreCase("update")){
			Map map =sys_para_bo.update(sys_para_form.getSEQ_ID(),BeanUtils.describe(sys_para_form));
			BeanUtils.populate(sys_para_form, map);
			list = sys_para_bo.search("");
			if(list!=null){
				SYS_PARA po  = list.get(0);
				BeanUtils.copyProperties(sys_para_form, po);
			}
//			sys_para_form.setScaseary(list);
		}
		if(ac_key.equalsIgnoreCase("delete")){
			Map map =sys_para_bo.delete(sys_para_form.getSEQ_ID());
			BeanUtils.populate(sys_para_form, map);
			sys_para_form.setAc_key("");
			sys_para_form.resetFields();
		}
		target = StrUtils.isEmpty(sys_para_form.getTarget())?"":sys_para_form.getTarget();
//		下拉清單
		return mapping.findForward(target);
	}

	public SYS_PARA_BO getSys_para_bo() {
		return sys_para_bo;
	}

	public void setSys_para_bo(SYS_PARA_BO sys_para_bo) {
		this.sys_para_bo = sys_para_bo;
	}
	
	
	
}
