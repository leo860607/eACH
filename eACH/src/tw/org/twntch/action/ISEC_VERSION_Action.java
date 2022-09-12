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

import tw.org.twntch.bo.ISEC_VERSION_BO;
import tw.org.twntch.form.Isec_Version_Form;
import tw.org.twntch.po.SYS_PARA;
import tw.org.twntch.util.StrUtils;

public class ISEC_VERSION_Action extends Action {

	private ISEC_VERSION_BO isec_version_bo;
	
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
			System.out.println("ISEC_Action start");
			String target = "";
			Isec_Version_Form isec_version_form = (Isec_Version_Form) form;
			String ac_key = StrUtils.isEmpty(isec_version_form.getAc_key())?"edit":isec_version_form.getAc_key();
			target = StrUtils.isEmpty(isec_version_form.getTarget())?"edit":isec_version_form.getTarget();
			
			List<SYS_PARA> list = null;
			
			if(ac_key.equalsIgnoreCase("edit")){
				list = isec_version_bo.search(isec_version_form.getSEQ_ID());
				if(list!=null){
					SYS_PARA po  = list.get(0);
					BeanUtils.copyProperties(isec_version_form, po);
				}
			}
			
			if(ac_key.equalsIgnoreCase("update")){
				Map map =isec_version_bo.update(isec_version_form.getSEQ_ID(),BeanUtils.describe(isec_version_form));
				System.out.println("list1234"+map);
				list = isec_version_bo.search("");
				if(list!=null){
					SYS_PARA po  = list.get(0);
					BeanUtils.copyProperties(isec_version_form, po);
					}
			System.out.println("list1234"+list);
			}
			
		return mapping.findForward(target);
	}

	public ISEC_VERSION_BO getIsec_version_bo() {
		return isec_version_bo;
	}

	public void setIsec_version_bo(ISEC_VERSION_BO isec_version_bo) {
		this.isec_version_bo = isec_version_bo;
	}
		
	
}
