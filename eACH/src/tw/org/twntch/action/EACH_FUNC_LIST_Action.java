package tw.org.twntch.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import tw.org.twntch.bo.EACH_FUNC_LIST_BO;
import tw.org.twntch.form.Each_Func_List_Form;
import tw.org.twntch.po.EACH_FUNC_LIST;
import tw.org.twntch.util.BeanUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.StrUtils;

public class EACH_FUNC_LIST_Action extends Action {

	private EACH_FUNC_LIST_BO func_list_bo;
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Each_Func_List_Form func_list_form = (Each_Func_List_Form) form;
		String ac_key = StrUtils.isEmpty(func_list_form.getAc_key())?"":func_list_form.getAc_key();
		String target = StrUtils.isEmpty(func_list_form.getTarget())?"search":func_list_form.getTarget();
		func_list_form.setTarget(target);
		System.out.println("FUNC_LIST_Action is start >> " + ac_key);
		
		
		if(ac_key.equalsIgnoreCase("search")|| ac_key.equalsIgnoreCase("back") ){
			System.out.println("SerchStrs>>"+func_list_form.getSerchStrs());
			BeanUtils.populate(func_list_form, JSONUtils.json2map(func_list_form.getSerchStrs()));
		}
		if(ac_key.equalsIgnoreCase("add")){
			func_list_form.setPROXY_FUNC("N");
			//所有「作業模組」清單
			func_list_form.setFuncIdList(func_list_bo.getFuncIdList());
		}
		if(ac_key.equalsIgnoreCase("save")){
			 Map map = func_list_bo.save(BeanUtils.describe(func_list_form));
			 BeanUtils.populate(func_list_form, map);
			 if(!Boolean.valueOf(func_list_form.getResult())){
				//所有「作業模組」清單
				func_list_form.setFuncIdList(func_list_bo.getFuncIdList());
			 }else{
				 BeanUtils.populate(func_list_form, JSONUtils.json2map(func_list_form.getSerchStrs())); 
			 }
		}
		if(ac_key.equalsIgnoreCase("edit")){
			BeanUtils.populate(func_list_form, JSONUtils.json2map(func_list_form.getEdit_params()));
			List<EACH_FUNC_LIST>list = func_list_bo.search(func_list_form.getFUNC_ID());
			for(EACH_FUNC_LIST po : list){
				BeanUtils.copyProperties(func_list_form, po);
			}
			Map<String, String> funcOwner = new HashMap<String, String>();
			funcOwner.put("TCH_FUNC", func_list_form.getTCH_FUNC());
			funcOwner.put("BANK_FUNC", func_list_form.getBANK_FUNC());
			//funcOwner.put("COMPANY_FUNC", func_list_form.getCOMPANY_FUNC());
			//依照功能所屬單位找出「作業模組」清單
			func_list_form.setFuncIdList(func_list_bo.getFuncIdList(funcOwner));
		}
		if(ac_key.equalsIgnoreCase("update")){
			Map map = null ;
			System.out.println("TCH>>"+func_list_form.getTCH_FUNC());
			System.out.println(func_list_form.getBANK_FUNC());
			System.out.println(func_list_form.getCOMPANY_FUNC());
			System.out.println(func_list_form.getFUNC_ID());
			map = func_list_bo.validate(BeanUtils.describe(func_list_form));
			if(map.get("result").equals("TRUE")){
				map.clear();
				map = func_list_bo.update(BeanUtils.describe(func_list_form));
			}
			
			BeanUtils.populate(func_list_form, map);
			Map<String, String> funcOwner = new HashMap<String, String>();
			funcOwner.put("TCH_FUNC", func_list_form.getTCH_FUNC());
			funcOwner.put("BANK_FUNC", func_list_form.getBANK_FUNC());
			if(!Boolean.valueOf(func_list_form.getResult())){
				//依照功能所屬單位找出「作業模組」清單
				func_list_form.setFuncIdList(func_list_bo.getFuncIdList(funcOwner));
			}else{
				BeanUtils.populate(func_list_form, JSONUtils.json2map(func_list_form.getSerchStrs()));
			}
			
		}
		if(ac_key.equalsIgnoreCase("delete")){
			Map map = func_list_bo.delete(func_list_form.getFUNC_ID());
			BeanUtils.populate(func_list_form, map);
			func_list_form.setFUNC_ID("");
		}
		target = StrUtils.isEmpty(func_list_form.getTarget())?"":func_list_form.getTarget();
		System.out.println("target>>"+target);
		return mapping.findForward(target);
	}
	
	public EACH_FUNC_LIST_BO getFunc_list_bo() {
		return func_list_bo;
	}
	public void setFunc_list_bo(EACH_FUNC_LIST_BO func_list_bo) {
		this.func_list_bo = func_list_bo;
	}
	
}
