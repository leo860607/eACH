package tw.org.twntch.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import tw.org.twntch.bo.Arguments;
import tw.org.twntch.bo.BANK_BRANCH_BO;
import tw.org.twntch.bo.BANK_GROUP_BO;
import tw.org.twntch.bo.BUSINESS_TYPE_BO;
import tw.org.twntch.bo.EACHSYSSTATUSTAB_BO;
import tw.org.twntch.bo.ONBLOCKTAB_BO;
import tw.org.twntch.bo.TXN_CODE_BO;
import tw.org.twntch.form.Login_Form;
import tw.org.twntch.form.Onblocktab_evdayTraTimeTol_Form;
import tw.org.twntch.po.BANK_BRANCH;
import tw.org.twntch.po.ONBLOCKTAB;
import tw.org.twntch.util.BeanUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.WebServletUtils;
import tw.org.twntch.util.zDateHandler;

public class ONBLOCKTAB_evdayTraTimeTol_Action extends GenericAction {

	private ONBLOCKTAB_BO onblocktab_bo;
	private BANK_GROUP_BO bank_group_bo ;	   //取得總行代號
	private BANK_BRANCH_BO bank_branch_bo;    //取得分行代號 
	private BUSINESS_TYPE_BO business_type_bo;//取得業務代號
	private TXN_CODE_BO txn_code_bo;          //
	private EACHSYSSTATUSTAB_BO eachsysstatustab_bo;
	
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
				
		// TODO Auto-generated method stub
		Onblocktab_evdayTraTimeTol_Form obketttol_form = (Onblocktab_evdayTraTimeTol_Form) form ;		
		String target = "";
		String ac_key = StrUtils.isEmpty(obketttol_form.getAc_key())?"":obketttol_form.getAc_key();
		target = StrUtils.isEmpty(obketttol_form.getTarget())?"search":obketttol_form.getTarget();
		obketttol_form.setTarget(target);
		List<BANK_BRANCH> list = null;
		List<ONBLOCKTAB> onblist = null;
		//String onblist ="";
		System.out.println("ONBLOCKTAB_evdayTraTimeTol_Action is start target>> " + target);
		System.out.println("ONBLOCKTAB_evdayTraTimeTol_Action is start ac_key>> " + ac_key);
		Login_Form login_form = (Login_Form) WebServletUtils.getRequest().getSession().getAttribute("login_form");
		Map<String,String> m = new HashMap<String,String>();
		if(StrUtils.isNotEmpty(ac_key)){			
			if(ac_key.equals("search")){				
				
			}else if(ac_key.equals("back")){
//				setDropdownList(obketttol_form , login_form ,ac_key);							
			}else if(ac_key.equals("edit")){
//				System.out.println("TXDATE>>"+obktNtR_form.getTXDATE());
//				System.out.println("STAN>>"+obktNtR_form.getSTAN());
//				BeanUtils.copyProperties(obktNtR_form, onblocktab_bo.showDetail(obktNtR_form.getTXDATE(),onblocktab_form.getSTAN()));
//				onblist = onblocktab_bo.showDetail(onblocktab_form.getTXDT());
			}else if(ac_key.equalsIgnoreCase("export")){
				Map map = onblocktab_bo.qs_ex_export(
						obketttol_form.getTXTIME(),
						obketttol_form.getBUSINESS_TYPE_ID(),
						obketttol_form.getOPBK_ID(),
						obketttol_form.getBGBK_ID(),
						obketttol_form.getSerchStrs(),
						obketttol_form.getSortname(),
						obketttol_form.getSortorder()
				);
				BeanUtils.populate(obketttol_form, map);
				if(map.get("result").equals("TRUE") && !Arguments.getStringArg("ISWINOPEN").equals("Y")){
					exportFile2Client(response, map.get("msg").toString(), zDateHandler.getDateNum()+"_evdayTraTimeTol.xls" ,obketttol_form.getDow_token() );
					return null;
				}
			}
		}else{			
//			obketttol_form.setUSER_COMPANY(login_form.getUserData().getUSER_COMPANY());
//			setDropdownList(obketttol_form , login_form , ac_key);	
//			onblocktab_form.setResList(onblocktab_bo.getResstatusList());
		}
			
//		下拉清單
		if(!ac_key.equals("edit")){
			obketttol_form.setOpbkIdList(onblocktab_bo.getOpbkIdList());//操作行
//			obketttol_form.setOpbkIdList(bank_group_bo.getOpbkList());//操作行
			
			obketttol_form.setBsIdKist(business_type_bo.getBsTypeIdList()); //儲存業務代號清單
//			obktNtR_form.setTxnIdList(txn_code_bo.getIdList());           //儲存交易類別清單
		}
		//將營業日塞到頁面的日期控制項
		String busDate = eachsysstatustab_bo.getBusinessDate();
		System.out.println("busDate="+busDate);
		obketttol_form.setTXTIME(busDate);
		
		System.out.println("forward to >> " + target);
		return (mapping.findForward(target));
	}
	
//	public void setDropdownList(Onblocktab_evdayTraTimeTol_Form form ,Login_Form  login_form ,String ac_key){
//				
//		if(login_form.getUserData().getUSER_TYPE().equals("A")){
//			//使用者代號清單(以登入時預設的USER_COMPANY查詢)
//			if( StrUtils.isNotEmpty(ac_key) && ac_key.equals("back")){
//				form.setUserIdList(onblocktab_bo.getUserIdListByComId(form.getUSER_COMPANY()));
//			}else{
//				form.setUserIdList(onblocktab_bo.getUserIdListByComId(login_form.getUserData().getUSER_COMPANY()));
//			}
//			form.setFuncList(onblocktab_bo.getFuncListByRole_Type(login_form.getUserData().getUSER_TYPE()));
//			//USER_COMPANY清單
//			form.setUserCompanyList(onblocktab_bo.getUserCompanyList());
//		}else{
//			//使用者代號清單(以登入時預設的USER_COMPANY查詢)
//			form.setUserIdList(onblocktab_bo.getUserIdListByComId(login_form.getUserData().getUSER_COMPANY()));
//			//功能清單
//			form.setFuncList(onblocktab_bo.getFuncListByRole_Type(login_form.getUserData().getUSER_TYPE()));
//			form.setUSER_COMPANY(login_form.getUserData().getUSER_COMPANY());
//		}
//		
//	}
	
	public ONBLOCKTAB_BO getOnblocktab_bo() {
		return onblocktab_bo;
	}
	public void setOnblocktab_bo(ONBLOCKTAB_BO onblocktab_bo) {
		this.onblocktab_bo = onblocktab_bo;
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
	public TXN_CODE_BO getTxn_code_bo() {
		return txn_code_bo;
	}
	public void setTxn_code_bo(TXN_CODE_BO txn_code_bo) {
		this.txn_code_bo = txn_code_bo;
	}
	public EACHSYSSTATUSTAB_BO getEachsysstatustab_bo() {
		return eachsysstatustab_bo;
	}
	public void setEachsysstatustab_bo(EACHSYSSTATUSTAB_BO eachsysstatustab_bo) {
		this.eachsysstatustab_bo = eachsysstatustab_bo;
	}
}
