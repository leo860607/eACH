package tw.org.twntch.action;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;





import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import tw.org.twntch.bo.EACH_ROLE_LIST_BO;
import tw.org.twntch.form.Each_Role_List_Form;
import tw.org.twntch.po.EACH_ROLE_LIST;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.StrUtils;

public class EACH_ROLE_LIST_Action extends Action {

	private EACH_ROLE_LIST_BO role_list_bo ;

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		System.out.println("EACH_ROLE_LIST_Action is start");
		String target = "";
		Each_Role_List_Form role_list_form = (Each_Role_List_Form) form;
		String ac_key = StrUtils.isEmpty(role_list_form.getAc_key())?"":role_list_form.getAc_key();
		target = StrUtils.isEmpty(role_list_form.getTarget())?"search":role_list_form.getTarget();
		role_list_form.setTarget(target);
		List<EACH_ROLE_LIST> list = null;
		System.out.println("ac_key>>"+ac_key);
		if(ac_key.equalsIgnoreCase("search")|| ac_key.equalsIgnoreCase("back") ){
//			list = role_list_bo.search(role_list_form.getROLE_ID() ,role_list_form.getROLE_TYPE());
//			role_list_form.setScaseary(list);
			System.out.println("SerchStrs>>"+role_list_form.getSerchStrs());
			BeanUtils.populate(role_list_form, JSONUtils.json2map(role_list_form.getSerchStrs()));
			Map<String ,String> tmpMap = JSONUtils.json2map(role_list_form.getSerchStrs());
			role_list_form.setROLE_TYPE((tmpMap.get("action").split("=")[1].replace("&", "")));
			role_list_form.setROLE_ID("");
		}
		if(ac_key.equalsIgnoreCase("save")){
			 List<String> selected_MList = Arrays.asList(role_list_form.getSelected_MFuncs()); 
			 List<String> selected_SList = Arrays.asList(role_list_form.getSelected_SFuncs()); 
			 Map map = role_list_bo.save(role_list_form.getROLE_ID()  , selected_MList, selected_SList ,BeanUtils.describe(role_list_form));
			 BeanUtils.populate(role_list_form, map);
			 if(map.get("result").equals("TRUE")){
				BeanUtils.populate(role_list_form, JSONUtils.json2map(role_list_form.getSerchStrs()));
				Map<String ,String> tmpMap = JSONUtils.json2map(role_list_form.getSerchStrs());
				role_list_form.setROLE_TYPE((tmpMap.get("action").split("=")[1].replace("&", "")));
//				role_list_form.reset(mapping, request); 
				role_list_form.setROLE_ID("");
			 }
		}
		if(ac_key.equalsIgnoreCase("add")){
		}
		if(ac_key.equalsIgnoreCase("edit")){
			BeanUtils.populate(role_list_form, JSONUtils.json2map(role_list_form.getEdit_params()));
//			list = role_list_bo.search(role_list_form.getROLE_ID() ,role_list_form.getROLE_TYPE());
			list = role_list_bo.search(role_list_form.getROLE_ID() ,"");
			for(EACH_ROLE_LIST po :list){
				BeanUtils.copyProperties(role_list_form, po);
			}
			System.out.println("arytostring>>"+Arrays.toString(role_list_bo.getSelectedItem(role_list_form.getROLE_ID(), "2")));
			role_list_form.setSelected_MFuncs(role_list_bo.getSelectedItem(role_list_form.getROLE_ID(), "1"));
			role_list_form.setSelected_SFuncs(role_list_bo.getSelectedItem(role_list_form.getROLE_ID(), "2"));
			role_list_form.setSelected_SAuth(Arrays.toString(role_list_bo.getSelectedSAuth(role_list_form.getROLE_ID(), "2")));
			role_list_form.setOld_selected_SAuth(role_list_bo.getSelectedSAuth2(role_list_form.getROLE_ID(), "2"));
			role_list_form.setOld_selected_MFuncs( Arrays.toString(role_list_bo.getSelectedItem(role_list_form.getROLE_ID(), "1")) );
			role_list_form.setOld_selected_SFuncs( Arrays.toString(role_list_bo.getSelectedItem(role_list_form.getROLE_ID(), "2")) );
		}
		if(ac_key.equalsIgnoreCase("editII")){
			role_list_form.setSelected_MFuncs(role_list_bo.getSelectedItem(role_list_form.getROLE_ID(), "1"));
			role_list_form.setSelected_SFuncs(role_list_bo.getSelectedItem(role_list_form.getROLE_ID(), "2"));
			role_list_form.setSelected_SAuth(Arrays.toString(role_list_bo.getSelectedSAuth(role_list_form.getROLE_ID(), "2")));
			role_list_form.setOld_selected_SAuth(role_list_bo.getSelectedSAuth2(role_list_form.getROLE_ID(), "2"));
			role_list_form.setOld_selected_MFuncs( Arrays.toString(role_list_bo.getSelectedItem(role_list_form.getROLE_ID(), "1")) );
			role_list_form.setOld_selected_SFuncs( Arrays.toString(role_list_bo.getSelectedItem(role_list_form.getROLE_ID(), "2")) );
		}
		
		if(ac_key.equalsIgnoreCase("editIII")){
			boolean check = false;
			String roltypeTmp = role_list_form.getROLE_TYPE();
			list = role_list_bo.search(role_list_form.getROLE_ID() ,"");
			for(EACH_ROLE_LIST po :list){
				BeanUtils.copyProperties(role_list_form, po);
			}
			check = role_list_bo.checkRoleUser(role_list_form.getROLE_ID());
			if(!check){
				role_list_form.setROLE_TYPE(roltypeTmp);
			}else{
				role_list_form.setMsg("該群組已有使用者，不可變更群組類型");
			}
//			System.out.println("arytostring>>"+Arrays.toString(role_list_bo.getSelectedItem(role_list_form.getROLE_ID(), "2")));
			role_list_form.setSelected_MFuncs(role_list_bo.getSelectedItem(role_list_form.getROLE_ID(), "1"));
			role_list_form.setSelected_SFuncs(role_list_bo.getSelectedItem(role_list_form.getROLE_ID(), "2"));
			role_list_form.setSelected_SAuth(Arrays.toString(role_list_bo.getSelectedSAuth(role_list_form.getROLE_ID(), "2")));
			role_list_form.setOld_selected_SAuth(role_list_bo.getSelectedSAuth2(role_list_form.getROLE_ID(), "2"));
			role_list_form.setOld_selected_MFuncs( Arrays.toString(role_list_bo.getSelectedItem(role_list_form.getROLE_ID(), "1")) );
			role_list_form.setOld_selected_SFuncs( Arrays.toString(role_list_bo.getSelectedItem(role_list_form.getROLE_ID(), "2")) );
		}
		
		if(ac_key.equalsIgnoreCase("update")){
			List<String> selected_MList = Arrays.asList(role_list_form.getSelected_MFuncs()); 
			 List<String> selected_SList = Arrays.asList(role_list_form.getSelected_SFuncs()); 
			Map map =role_list_bo.update(role_list_form.getROLE_ID()  , selected_MList, selected_SList ,BeanUtils.describe(role_list_form));
			BeanUtils.populate(role_list_form, map);
			list = role_list_bo.search(role_list_form.getROLE_ID() ,role_list_form.getROLE_TYPE());
			role_list_form.setScaseary(list);
			if(map.get("result").equals("TRUE")){
				BeanUtils.populate(role_list_form, JSONUtils.json2map(role_list_form.getSerchStrs()));
				Map<String ,String> tmpMap = JSONUtils.json2map(role_list_form.getSerchStrs());
				role_list_form.setROLE_TYPE((tmpMap.get("action").split("=")[1].replace("&", "")));
				role_list_form.setROLE_ID("");
//				role_list_form.reset(mapping, request); 
			}
			
		}
		if(ac_key.equalsIgnoreCase("delete")){
			Map map =role_list_bo.delete(role_list_form.getROLE_ID());
			BeanUtils.populate(role_list_form, map);
			if(map.get("result").equals("TRUE")){
				BeanUtils.populate(role_list_form, JSONUtils.json2map(role_list_form.getSerchStrs()));
				Map<String ,String> tmpMap = JSONUtils.json2map(role_list_form.getSerchStrs());
				role_list_form.setROLE_TYPE((tmpMap.get("action").split("=")[1].replace("&", "")));
				role_list_form.setROLE_ID("");
//				role_list_form.reset(mapping, request); 
			}
		}
		if(ac_key.equalsIgnoreCase("update_ikey")){
			System.out.println("update_ikey");
			BeanUtils.populate(role_list_form, JSONUtils.json2map(role_list_form.getEdit_params()));
//			list = role_list_bo.search(role_list_form.getROLE_ID() ,role_list_form.getROLE_TYPE());
			list = role_list_bo.search(role_list_form.getROLE_ID() ,"");
			
			String ikey = role_list_form.getUSE_IKEY(); //紀錄原本ikey的值
			String name = role_list_form.getROLE_NAME();//紀錄原本群組名稱
			String desc = role_list_form.getDESC();     //紀錄原本群組說明
			
			for(EACH_ROLE_LIST po :list){
				BeanUtils.copyProperties(role_list_form, po);
			}
			role_list_form.setUSE_IKEY(ikey);
			role_list_form.setROLE_NAME(name);
			role_list_form.setDESC(desc);
			
			role_list_form.setSelected_MFuncs(role_list_bo.getSelectedItem(role_list_form.getROLE_ID(), "1"));
			role_list_form.setSelected_SFuncs(role_list_bo.getSelectedItem(role_list_form.getROLE_ID(), "2"));
			role_list_form.setSelected_SAuth(Arrays.toString(role_list_bo.getSelectedSAuth(role_list_form.getROLE_ID(), "2")));
			role_list_form.setOld_selected_SAuth(role_list_bo.getSelectedSAuth2(role_list_form.getROLE_ID(), "2"));
			role_list_form.setOld_selected_MFuncs( Arrays.toString(role_list_bo.getSelectedItem(role_list_form.getROLE_ID(), "1")) );
			role_list_form.setOld_selected_SFuncs( Arrays.toString(role_list_bo.getSelectedItem(role_list_form.getROLE_ID(), "2")) );
		}
		
		target = StrUtils.isEmpty(role_list_form.getTarget())?"":role_list_form.getTarget();
//		下拉清單
//		if(ac_key.equalsIgnoreCase("editII") &&!role_list_form.getROLE_TYPE().equals(role_list_form.getOLD_ROLE_TYPE())){
//			role_list_form.setROLE_TYPE(role_list_form.getOLD_ROLE_TYPE());
//		}
		role_list_form.setMenuFuncs(role_list_bo.getMenuFuncList(role_list_form.getROLE_TYPE()));
//		role_list_form.setSubItemFuncs(role_list_bo.getSubItemFuncList(role_list_form.getROLE_TYPE()));
//		role_list_form.setSubItemFuncsII(role_list_bo.getSubItemFuncListII(role_list_form.getROLE_TYPE()));
		
		//20160418 編輯功能選擇子項目時，增加判斷是否使用Ikey
		if(ac_key.equalsIgnoreCase("edit") || ac_key.equalsIgnoreCase("update_ikey") || ac_key.equalsIgnoreCase("update_ikey_add") || ac_key.equalsIgnoreCase("add")){
			role_list_form.setSubItemFuncsII(role_list_bo.getSubItemFuncListIV(role_list_form.getROLE_TYPE(), role_list_form.getUSE_IKEY()));
		}else{
			role_list_form.setSubItemFuncsII(role_list_bo.getSubItemFuncListIII(role_list_form.getROLE_TYPE()));
		}
		
		return mapping.findForward(target);
	}

	public EACH_ROLE_LIST_BO getRole_list_bo() {
		return role_list_bo;
	}

	public void setRole_list_bo(EACH_ROLE_LIST_BO role_list_bo) {
		this.role_list_bo = role_list_bo;
	}
	
	
	
}
