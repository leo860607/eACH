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

import tw.org.twntch.bo.BANK_GROUP_BO;
import tw.org.twntch.bo.EACH_USER_BO;
import tw.org.twntch.db.dao.hibernate.SYS_PARA_Dao;
import tw.org.twntch.form.Each_User_Form;
import tw.org.twntch.form.Login_Form;
import tw.org.twntch.po.EACH_ROLE_LIST;
import tw.org.twntch.po.EACH_USER;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.StrUtils;

public class EACH_USER_Action extends Action{

	private EACH_USER_BO each_user_bo;
	
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// TODO Auto-generated method stub
		System.out.println("EACH_USER_Action is start..");
		String target = "";
		Each_User_Form each_user_form = (Each_User_Form) form;
		Login_Form login_form = (Login_Form) request.getSession().getAttribute("login_form");
		System.out.println("loginid>>"+login_form.getUserId());
		String ac_key = StrUtils.isEmpty(each_user_form.getAc_key())?"":each_user_form.getAc_key();
		System.out.println("ac_key>>"+ac_key);
		target = StrUtils.isEmpty(each_user_form.getTarget())?"search":each_user_form.getTarget();
		each_user_form.setTarget(target);
		System.out.println("user_type>>"+each_user_form.getUSER_TYPE());
		List<EACH_USER> list = null;
		if(ac_key.equalsIgnoreCase("search")|| ac_key.equalsIgnoreCase("back") ){
			System.out.println("SerchStrs>>"+each_user_form.getSerchStrs());
			BeanUtils.populate(each_user_form, JSONUtils.json2map(each_user_form.getSerchStrs()));
			Map<String ,String> tmpMap = JSONUtils.json2map(each_user_form.getSerchStrs());
//			each_user_form.setUSER_ID("");
			each_user_form.setUSER_COMPANY("");
			each_user_form.setUSER_TYPE(tmpMap.get("action").split("=")[1].replace("&", ""));
		}
		if(ac_key.equalsIgnoreCase("save")){
			 Map map = each_user_bo.save(each_user_form.getUSER_ID(), each_user_form.getUSER_COMPANY() ,BeanUtils.describe(each_user_form));
			 BeanUtils.populate(each_user_form, map);
			 each_user_form.setRole_list(each_user_bo.getRole_ListByUser_Type(each_user_form.getUSER_TYPE()));
			 if(map.get("result").equals("TRUE")){
//				這裡要額外課製 因查詢頁面的SerchStrs 預設為無查詢條件故不帶USER_ID，返回查詢頁面時會造成add 及edit頁面的USER_ID殘留值
				 each_user_form.setUSER_ID("");
				BeanUtils.populate(each_user_form, JSONUtils.json2map(each_user_form.getSerchStrs()));
				each_user_form.setUSER_COMPANY("");
				Map<String ,String> tmpMap = JSONUtils.json2map(each_user_form.getSerchStrs());
				each_user_form.setUSER_TYPE(tmpMap.get("action").split("=")[1].replace("&", ""));
			 }
		}
		if(ac_key.equalsIgnoreCase("add")){
				each_user_form.setRole_list(each_user_bo.getRole_ListByUser_Type(each_user_form.getUSER_TYPE()));
				each_user_form.setIDLE_TIMEOUT(each_user_bo.getSysParaTimeOut());
		}
		if(ac_key.equalsIgnoreCase("edit")){
			BeanUtils.populate(each_user_form, JSONUtils.json2map(each_user_form.getEdit_params()));
			EACH_USER po = each_user_bo.searchByPK(each_user_form.getUSER_ID());
			BeanUtils.copyProperties(each_user_form, po);
			each_user_form.setRole_list(each_user_bo.getRole_ListByUser_Type(each_user_form.getUSER_TYPE()));
		}
		if(ac_key.equalsIgnoreCase("update")){
			Map map =each_user_bo.update(each_user_form.getUSER_ID(), each_user_form.getUSER_COMPANY(),BeanUtils.describe(each_user_form));
			BeanUtils.populate(each_user_form, map);
			each_user_form.setRole_list(each_user_bo.getRole_ListByUser_Type(each_user_form.getUSER_TYPE()));
			if(map.get("result").equals("TRUE")){
//				note 因為頁面共用 故each_user_form.setUSER_ID("");與BeanUtils.populate(此2行順序不可對調
//				這裡要額外課製 因查詢頁面的SerchStrs 預設為無查詢條件故不帶USER_ID，返回查詢頁面時會造成add 及edit頁面的USER_ID殘留值
				each_user_form.setUSER_ID("");
				BeanUtils.populate(each_user_form, JSONUtils.json2map(each_user_form.getSerchStrs()));
				each_user_form.setUSER_COMPANY("");
				Map<String ,String> tmpMap = JSONUtils.json2map(each_user_form.getSerchStrs());
				each_user_form.setUSER_TYPE(tmpMap.get("action").split("=")[1].replace("&", ""));
			}
		}
		if(ac_key.equalsIgnoreCase("delete")){
			Map map =each_user_bo.delete(each_user_form.getUSER_ID(), each_user_form.getUSER_COMPANY());
			BeanUtils.populate(each_user_form, map);
			each_user_form.setRole_list(each_user_bo.getRole_ListByUser_Type(each_user_form.getUSER_TYPE()));
			if(map.get("result").equals("TRUE")){
				BeanUtils.populate(each_user_form, JSONUtils.json2map(each_user_form.getSerchStrs()));
				each_user_form.setUSER_ID("");
				Map<String ,String> tmpMap = JSONUtils.json2map(each_user_form.getSerchStrs());
				each_user_form.setUSER_TYPE(tmpMap.get("action").split("=")[1].replace("&", ""));
			}
			//刪除成功後清空PK欄位值，以免回查詢頁時殘留查詢條件
//			each_user_form.reset(mapping, request);
		}
		target = StrUtils.isEmpty(each_user_form.getTarget())?"":each_user_form.getTarget();
//		下拉清單
//		each_user_form.setTxnIdList(txn_code_bo.getIdList());
		
		return mapping.findForward(target);
	}

	public EACH_USER_BO getEach_user_bo() {
		return each_user_bo;
	}

	public void setEach_user_bo(EACH_USER_BO each_user_bo) {
		this.each_user_bo = each_user_bo;
	}

	
	
	
	
}
