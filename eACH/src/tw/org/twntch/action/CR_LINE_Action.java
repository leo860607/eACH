
package tw.org.twntch.action;

import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;





import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import tw.org.twntch.bo.BANK_GROUP_BO;
import tw.org.twntch.bo.CR_LINE_BO;
import tw.org.twntch.form.Cr_Line_Form;
import tw.org.twntch.po.CR_LINE;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.StrUtils;

public class CR_LINE_Action extends Action {

	private CR_LINE_BO cr_line_bo;
	private BANK_GROUP_BO bank_group_bo;
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		System.out.println("CR_LINE_Action is start");
		String target = "";
		Cr_Line_Form cr_line_form = (Cr_Line_Form) form;
		String ac_key = StrUtils.isEmpty(cr_line_form.getAc_key())?"":cr_line_form.getAc_key();
		target = StrUtils.isEmpty(cr_line_form.getTarget())?"search":cr_line_form.getTarget();
		cr_line_form.setTarget(target);
		List<CR_LINE> list = null;
		System.out.println("ac_key>>"+ac_key);
		if(ac_key.equalsIgnoreCase("search")|| ac_key.equalsIgnoreCase("back") ){
			System.out.println("SerchStrs>>"+cr_line_form.getSerchStrs());
			BeanUtils.populate(cr_line_form, JSONUtils.json2map(cr_line_form.getSerchStrs()));
		}
		if(ac_key.equalsIgnoreCase("save")){
			 Map map = cr_line_bo.save(cr_line_form.getBANK_ID(),BeanUtils.describe(cr_line_form));
			 BeanUtils.populate(cr_line_form, map);
			 if(map.get("result").equals("TRUE")){
					BeanUtils.populate(cr_line_form, JSONUtils.json2map(cr_line_form.getSerchStrs()));
			 }
//			 list = cr_line_bo.search(cr_line_form.getBANK_ID());
//			 cr_line_form.setScaseary(list);
		}
		if(ac_key.equalsIgnoreCase("add")){
		}
		if(ac_key.equalsIgnoreCase("edit")){
			BeanUtils.populate(cr_line_form, JSONUtils.json2map(cr_line_form.getEdit_params()));
			list = cr_line_bo.search(cr_line_form.getBANK_ID());
			
			for(CR_LINE po :list){
				BeanUtils.copyProperties(cr_line_form, po);
				cr_line_form.setOLD_BASIC_CR_LINE(cr_line_form.getBASIC_CR_LINE());
				cr_line_form.setOLD_REST_CR_LINE(cr_line_form.getREST_CR_LINE());
			}
			System.out.println("edit.cr_line_form.getSerchStrs()>>"+cr_line_form.getSerchStrs());
			if(StrUtils.isNotEmpty(cr_line_form.getSerchStrs())){
				System.out.println("edit.json2map>>"+ JSONUtils.json2map(cr_line_form.getSerchStrs()) );
			}
		}
		if(ac_key.equalsIgnoreCase("update_rest_cr") ){
		}
		if(ac_key.equalsIgnoreCase("update")){
			Map map =cr_line_bo.update(cr_line_form.getBANK_ID(),BeanUtils.describe(cr_line_form));
			BeanUtils.populate(cr_line_form, map);
			if(map.get("result").equals("TRUE")){
				BeanUtils.populate(cr_line_form, JSONUtils.json2map(cr_line_form.getSerchStrs()));
			}
//			list = cr_line_bo.search(cr_line_form.getBANK_ID());
//			cr_line_form.setScaseary(list);
//			System.out.println("update.cr_line_form.getSerchStrs()>>"+cr_line_form.getSerchStrs());
//			if(StrUtils.isNotEmpty(cr_line_form.getSerchStrs())){
//				System.out.println("update.json2map>>"+ JSONUtils.json2map(cr_line_form.getSerchStrs()) );
//			}
		}
		if(ac_key.equalsIgnoreCase("delete")){
			Map map =cr_line_bo.delete(cr_line_form.getBANK_ID());
			BeanUtils.populate(cr_line_form, map);
			if(map.get("result").equals("TRUE")){
				BeanUtils.populate(cr_line_form, JSONUtils.json2map(cr_line_form.getSerchStrs()));
			}
			//cr_line_form.setAc_key("");
		}
		
		//下拉清單
		if(ac_key.equalsIgnoreCase("add")){
			cr_line_form.setBgbkIdList(bank_group_bo.getBgbkIdList());
		}else{
			cr_line_form.setBgbkIdList(cr_line_bo.getExistingBgbkIdList());
		}
		target = StrUtils.isEmpty(cr_line_form.getTarget())?"":cr_line_form.getTarget();
		return mapping.findForward(target);
	}

	public CR_LINE_BO getCr_line_bo() {
		return cr_line_bo;
	}

	public void setCr_line_bo(CR_LINE_BO cr_line_bo) {
		this.cr_line_bo = cr_line_bo;
	}

	public BANK_GROUP_BO getBank_group_bo() {
		return bank_group_bo;
	}

	public void setBank_group_bo(BANK_GROUP_BO bank_group_bo) {
		this.bank_group_bo = bank_group_bo;
	}
	
	
	
	
}
