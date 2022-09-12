package tw.org.twntch.action;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import tw.org.twntch.bo.BANK_GROUP_BO;
import tw.org.twntch.bo.BANK_OPBK_BO;
import tw.org.twntch.bo.BUSINESS_TYPE_BO;
import tw.org.twntch.bo.SYS_PARA_BO;
import tw.org.twntch.form.Bank_Group_Form;
import tw.org.twntch.form.Login_Form;
import tw.org.twntch.po.BANK_GROUP;
import tw.org.twntch.po.BANK_GROUP_BUSINESS;
import tw.org.twntch.po.BANK_GROUP_BUSINESS_PK;
import tw.org.twntch.po.BANK_OPBK;
import tw.org.twntch.po.BUSINESS_TYPE;
import tw.org.twntch.po.SYS_PARA;
import tw.org.twntch.util.BeanUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.StrUtils;


public class BANK_GROUP_Action extends GenericAction {
	private BANK_GROUP_BO bank_group_bo ; 
	private BUSINESS_TYPE_BO business_type_bo ;
	private SYS_PARA_BO sys_para_bo ; 
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		Bank_Group_Form bank_group_form = (Bank_Group_Form) form ;
		String target = StrUtils.isEmpty(bank_group_form.getTarget())?"search":bank_group_form.getTarget();
		String ac_key = StrUtils.isEmpty(bank_group_form.getAc_key())?"":bank_group_form.getAc_key();
		setBreadcrumb((Login_Form) request.getSession().getAttribute("login_form"), request.getParameter("b"));
		System.out.println("BANK_GROUP_Action is start >> " + ac_key);
		if(StrUtils.isNotEmpty(ac_key)){
			if(ac_key.equals("OP")||ac_key.equals("CT")){
				return (mapping.findForward("OP"));
			}
			System.out.println("ac_key>>"+ac_key);
			if(ac_key.equals("search") || ac_key.equalsIgnoreCase("back")){
				System.out.println("SerchStrs>>"+bank_group_form.getSerchStrs());
				BeanUtils.populate(bank_group_form, JSONUtils.json2map(bank_group_form.getSerchStrs()));
//				String opbkId = "";
//				String ctbkId = "";
//				
//				List<BANK_GROUP> list = bank_group_bo.searchFromMaster(bank_group_form.getOPBK_ID(), bank_group_form.getCTBK_ID());
				/* 20141126 HUANGPU 改成回傳 JSON 字串於網頁端組成 GRID
				bank_group_form.setScaseary(list);
				*/
//				bank_group_form.setScaseJson(JSONUtils.toJson(list));
			}else if(ac_key.equals("new")){
//				從系統參數檔抓取預設的每小時最大上傳檔案數 (HR_UPLOAD_MAX_FILE)
				List<SYS_PARA> list = sys_para_bo.search("");
				if(list!=null){
					SYS_PARA po  = list.get(0);
					bank_group_form.setHR_UPLOAD_MAX_FILE(po.getHR_UP_MAX_FILE_DFT());
					bank_group_form.setFILE_MAX_CNT(po.getFILE_MAX_CNT());
				}
				
				bank_group_form.setBsTypeList(business_type_bo.getBsTypeIdList());
				//Return to add page
				target = "add_p";
			}else if(ac_key.equals("add")){
				BANK_GROUP bg = new BANK_GROUP();
				BeanUtils.copyProperties(bg, bank_group_form);
				
				List<BANK_GROUP_BUSINESS> bgbList = new ArrayList<BANK_GROUP_BUSINESS>();
				BANK_GROUP_BUSINESS bgb = null;
				String selectedBsType[] = bank_group_form.getSelectedBsTypeAry();
				if(selectedBsType != null){
					for(int i = 0; i < selectedBsType.length; i++){
						bgb = new BANK_GROUP_BUSINESS();
						bgb.setId(new BANK_GROUP_BUSINESS_PK(bg.getBGBK_ID(), selectedBsType[i]));
						bgbList.add(bgb);
					}
				}
				
				System.out.println("try to add " + bgbList.size() + " items");
				Map result = null;
				result = bank_group_bo.addIII(
						bg, 
						bgbList, 
						bank_group_form.getBASIC_CR_LINE(), 
						bank_group_form.getREST_CR_LINE(), 
						bank_group_form.getIsEditCR().equals("Y"), 
						bank_group_form.getOPBK_ID().equals(bank_group_form.getBGBK_ID())
				);
//				if(bank_group_form.getIsEditCR().equals("Y")){
//					
//					result = bank_group_bo.addII(bg, bgbList, bank_group_form.getBASIC_CR_LINE(), bank_group_form.getREST_CR_LINE());
//				System.out.println("result>>"+result);
//				}else{
//					result = bank_group_bo.add(bg, bgbList);
//				}
//				Map result = bank_group_bo.add(bg, bgbList);
				bank_group_form.setResult(ac_key + "_" + result.get("result"));
				bank_group_form.setMsg((String)result.get("msg"));
				bank_group_form.setBsTypeList(business_type_bo.getBsTypeIdList());
				//Return to add page
				target = "add_p";
			}else if(ac_key.equals("edit")){
				System.out.println("try to edit id>>" + bank_group_form.getBGBK_ID());
				//Get the first record
//				改抓取實體表格MASTER
//				BANK_GROUP bg = bank_group_bo.search(bank_group_form.getBGBK_ID()).get(0);
				BeanUtils.populate(bank_group_form, JSONUtils.json2map(bank_group_form.getEdit_params()));
				BANK_GROUP bg = bank_group_bo.searchFromMaster(bank_group_form.getBGBK_ID()).get(0);
				System.out.println("OP_START_DATE>>"+bg.getOP_START_DATE());
				System.out.println("FILE_MAX_CNT>>"+bg.getFILE_MAX_CNT());
				BeanUtils.copyProperties(bank_group_form, bg);
				bank_group_form.setSelectedBsTypeList(bank_group_bo.getbgbList(bg.getBGBK_ID(), ""));
				bank_group_form.setBsTypeList(bank_group_bo.getUnelectedBsTypeList(bg.getBGBK_ID()));
				target = "edit_p";
			}else if(ac_key.equals("save")){
				BANK_GROUP bg = new BANK_GROUP();
				System.out.println("IS_EACH>>"+bank_group_form.getIS_EACH());
				BeanUtils.copyProperties(bg, bank_group_form);
//				Map<String, Object> rtnMap = bank_group_bo.saveII(bg, bank_group_form.getSelectedBsTypeAry());
				Map<String, Object> rtnMap = bank_group_bo.saveII(bg, bank_group_form.getSelectedBsTypeAry(), bank_group_form.getIsEditCR().equals("Y"), bank_group_form.getBASIC_CR_LINE(), bank_group_form.getREST_CR_LINE());
				if(Boolean.valueOf(String.valueOf(rtnMap.get("result")))){
					bank_group_form.setMsg(bg.getBGBK_ID());
				}else{
					bank_group_form.setMsg((String)rtnMap.get("msg"));
				}
				bank_group_form.setResult(ac_key + "_" + rtnMap.get("result"));
				bank_group_form.setSelectedBsTypeList(bank_group_bo.getbgbList(bg.getBGBK_ID(), ""));
				bank_group_form.setBsTypeList(bank_group_bo.getUnelectedBsTypeList(bg.getBGBK_ID()));
				//Return to edit page
				target = "edit_p";
			}else if(ac_key.equals("delete")){
				BANK_GROUP bg = new BANK_GROUP();
				BeanUtils.copyProperties(bg, bank_group_form);
				System.out.println("try to delete bgbkId >>" + bg.getBGBK_ID());
				Map rtnMap = bank_group_bo.delete(bg);
				BeanUtils.populate(bank_group_form, rtnMap);
				bank_group_form.setResult(ac_key + "_" + Boolean.parseBoolean(bank_group_form.getResult()));
				bank_group_form.setSelectedBsTypeList(bank_group_bo.getbgbList(bg.getBGBK_ID(), ""));
				bank_group_form.setBsTypeList(bank_group_bo.getUnelectedBsTypeList(bg.getBGBK_ID()));
				//Return to edit page
				target = "edit_p";
			}else if(ac_key.equals("opbk_search")){
//				bank_group_form.setOpbkIdList(bank_group_bo.getOpbkList());
				bank_group_form.setOpbkIdList(bank_group_bo.getOpbkListFromMaster());
			}else if(ac_key.equals("ctbk_search")){
//				bank_group_form.setCtbkIdList(bank_group_bo.getCtbkIdList());
				bank_group_form.setCtbkIdList(bank_group_bo.getCtbkIdListFromMaster());
			}
		}
		
//		bank_group_form.setOpbkIdList(bank_group_bo.getOpbkList());
		bank_group_form.setOpbkIdList(bank_group_bo.getOpbkListFromMaster());
//		bank_group_form.setCtbkIdList(bank_group_bo.getCtbkIdList());
		bank_group_form.setCtbkIdList(bank_group_bo.getCtbkIdListFromMaster());
		
		System.out.println("forward to>>" + target);
		return (mapping.findForward(target));
	}
	public BANK_GROUP_BO getBank_group_bo() {
		return bank_group_bo;
	}
	public void setBank_group_bo(BANK_GROUP_BO bank_group_bo) {
		this.bank_group_bo = bank_group_bo;
	}
	public BUSINESS_TYPE_BO getBusiness_type_bo() {
		return business_type_bo;
	}
	public void setBusiness_type_bo(BUSINESS_TYPE_BO business_type_bo) {
		this.business_type_bo = business_type_bo;
	}
	public SYS_PARA_BO getSys_para_bo() {
		return sys_para_bo;
	}
	public void setSys_para_bo(SYS_PARA_BO sys_para_bo) {
		this.sys_para_bo = sys_para_bo;
	}
	
	
}
