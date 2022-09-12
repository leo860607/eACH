package tw.org.twntch.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import tw.org.twntch.bo.GL_ERROR_CODE_BO;
import tw.org.twntch.form.Gl_Error_Code_Form;
import tw.org.twntch.po.GL_ERROR_CODE;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.StrUtils;

public class GL_ERROR_CODE_Action extends Action {

	private GL_ERROR_CODE_BO gl_err_code_bo ;

	
	
	
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		System.out.println("GL_ERROR_CODE_Action is start");
		String target = "";
		Gl_Error_Code_Form GL_ERROR_CODE_Form = (Gl_Error_Code_Form) form;
		String ac_key = StrUtils.isEmpty(GL_ERROR_CODE_Form.getAc_key())?"":GL_ERROR_CODE_Form.getAc_key();
		target = StrUtils.isEmpty(GL_ERROR_CODE_Form.getTarget())?"search":GL_ERROR_CODE_Form.getTarget();
		GL_ERROR_CODE_Form.setTarget(target);
		List<GL_ERROR_CODE> list = null;
		if(ac_key.equalsIgnoreCase("search")|| ac_key.equalsIgnoreCase("back") ){
			list = gl_err_code_bo.search(GL_ERROR_CODE_Form.getERROR_ID());
			GL_ERROR_CODE_Form.setJsonList(JSONUtils.toJson(list));
			GL_ERROR_CODE_Form.setScaseary(list);
		}
		if(ac_key.equalsIgnoreCase("save")){
			 Map map = gl_err_code_bo.save(GL_ERROR_CODE_Form.getERROR_ID(),BeanUtils.describe(GL_ERROR_CODE_Form));
			 BeanUtils.populate(GL_ERROR_CODE_Form, map);
			 list = gl_err_code_bo.search(GL_ERROR_CODE_Form.getERROR_ID());
			 GL_ERROR_CODE_Form.setJsonList(JSONUtils.toJson(list));
			 GL_ERROR_CODE_Form.setScaseary(list);
		}
		if(ac_key.equalsIgnoreCase("add")){
		}
		if(ac_key.equalsIgnoreCase("edit")){
			list = gl_err_code_bo.search(GL_ERROR_CODE_Form.getERROR_ID());
			if(list != null){
				for(GL_ERROR_CODE po :list){
					BeanUtils.copyProperties(GL_ERROR_CODE_Form, po);
				}
			}
		}
		if(ac_key.equalsIgnoreCase("update")){
			Map map =gl_err_code_bo.update(GL_ERROR_CODE_Form.getERROR_ID(),BeanUtils.describe(GL_ERROR_CODE_Form));
			BeanUtils.populate(GL_ERROR_CODE_Form, map);
			list = gl_err_code_bo.search(GL_ERROR_CODE_Form.getERROR_ID());
			GL_ERROR_CODE_Form.setJsonList(JSONUtils.toJson(list));
			GL_ERROR_CODE_Form.setScaseary(list);
		}
		if(ac_key.equalsIgnoreCase("delete")){
			Map map =gl_err_code_bo.delete(GL_ERROR_CODE_Form.getERROR_ID());
			BeanUtils.populate(GL_ERROR_CODE_Form, map);
			GL_ERROR_CODE_Form.setAc_key("");
		}
		target = StrUtils.isEmpty(GL_ERROR_CODE_Form.getTarget())?"":GL_ERROR_CODE_Form.getTarget();
//		下拉清單
		GL_ERROR_CODE_Form.setIdList(gl_err_code_bo.getIdList());
		return mapping.findForward(target);
	}

	public GL_ERROR_CODE_BO getGl_err_code_bo() {
		return gl_err_code_bo;
	}

	public void setGl_err_code_bo(GL_ERROR_CODE_BO gl_err_code_bo) {
		this.gl_err_code_bo = gl_err_code_bo;
	}
	
	
	
}
