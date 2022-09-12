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

import tw.org.twntch.bo.BANK_BRANCH_BO;
import tw.org.twntch.bo.BANK_GROUP_BO;
import tw.org.twntch.form.Bank_Branch_Form;
import tw.org.twntch.po.BANK_BRANCH;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.StrUtils;

public class BANK_BRANCH_Action extends Action {
private BANK_BRANCH_BO bank_branch_bo ;
private BANK_GROUP_BO bank_group_bo ;
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// TODO Auto-generated method stub
		String target = "";
		Bank_Branch_Form bank_branch_form = (Bank_Branch_Form) form;
		String ac_key = StrUtils.isEmpty(bank_branch_form.getAc_key())?"":bank_branch_form.getAc_key();
		target = StrUtils.isEmpty(bank_branch_form.getTarget())?"search":bank_branch_form.getTarget();
		bank_branch_form.setTarget(target);
		System.out.println("BANK_BRANCH_Action is start >> " + ac_key);
		
		List<BANK_BRANCH> list = null;
		if(ac_key.equalsIgnoreCase("search") || ac_key.equalsIgnoreCase("back")){
			BeanUtils.populate(bank_branch_form, JSONUtils.json2map(bank_branch_form.getSerchStrs()));
			list = bank_branch_bo.search(bank_branch_form.getBGBK_ID(), bank_branch_form.getBRBK_ID());
			bank_branch_form.setScaseary(list);
		}
		if(ac_key.equalsIgnoreCase("save")){
			 Map map = bank_branch_bo.save(bank_branch_form.getBGBK_ID(), bank_branch_form.getBRBK_ID(),BeanUtils.describe(bank_branch_form));
			 BeanUtils.populate(bank_branch_form, map);
			 if(map.get("result") != null && map.get("result").equals("TRUE")){
				BeanUtils.populate(bank_branch_form, JSONUtils.json2map(bank_branch_form.getSerchStrs()));
			 }
			 list = bank_branch_bo.search(bank_branch_form.getBGBK_ID(), bank_branch_form.getBRBK_ID());
			 bank_branch_form.setScaseary(list);
		}
		if(ac_key.equalsIgnoreCase("add")){
//			bank_branch_form.setBgIdList(bank_group_bo.getActiveBgbkIdList());
			bank_branch_form.setBgIdList(bank_group_bo.getBgbkIdList());
		}
		if(ac_key.equalsIgnoreCase("edit")){
			BeanUtils.populate(bank_branch_form, JSONUtils.json2map(bank_branch_form.getEdit_params()));
			list = bank_branch_bo.search(bank_branch_form.getBGBK_ID(), bank_branch_form.getBRBK_ID());
			for(BANK_BRANCH po :list){
				System.out.println("SYNCSPDATE>>"+po.getSYNCSPDATE());
				BeanUtils.copyProperties(bank_branch_form, po);
				System.out.println("bank_branch_form.SYNCSPDATE>>"+bank_branch_form.getSYNCSPDATE());
				
			}
		}
		if(ac_key.equalsIgnoreCase("update")){
			Map map =bank_branch_bo.update(bank_branch_form.getBGBK_ID(), bank_branch_form.getBRBK_ID(),BeanUtils.describe(bank_branch_form));
			BeanUtils.populate(bank_branch_form, map);
			if(map.get("result") != null && map.get("result").equals("TRUE")){
				BeanUtils.populate(bank_branch_form, JSONUtils.json2map(bank_branch_form.getSerchStrs()));
			}
			list = bank_branch_bo.search(bank_branch_form.getBGBK_ID(), bank_branch_form.getBRBK_ID());
			bank_branch_form.setScaseary(list);
		}
		if(ac_key.equalsIgnoreCase("delete")){
			Map map =bank_branch_bo.delete(bank_branch_form.getBGBK_ID(), bank_branch_form.getBRBK_ID());
			BeanUtils.populate(bank_branch_form, map);
//			list = bank_branch_bo.search(bank_branch_form.getBGBK_ID(), "all");
//			bank_branch_form.setScaseary(list);
			bank_branch_form.setAc_key("");
		}
		target = StrUtils.isEmpty(bank_branch_form.getTarget())?"":bank_branch_form.getTarget();
		//下拉清單
		if(bank_branch_form.getBgIdList() == null){
			bank_branch_form.setBgIdList(bank_group_bo.getBgbkIdList());
		}
		
		return mapping.findForward(target);
	}
	public BANK_BRANCH_BO getBank_branch_bo() {
		return bank_branch_bo;
	}
	public void setBank_branch_bo(BANK_BRANCH_BO bank_branch_bo) {
		this.bank_branch_bo = bank_branch_bo;
	}
	public BANK_GROUP_BO getBank_group_bo() {
		return bank_group_bo;
	}
	public void setBank_group_bo(BANK_GROUP_BO bank_group_bo) {
		this.bank_group_bo = bank_group_bo;
	}

	
	
}
