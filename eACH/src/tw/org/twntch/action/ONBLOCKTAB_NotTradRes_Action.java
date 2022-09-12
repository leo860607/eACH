package tw.org.twntch.action;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import tw.org.twntch.bo.BANK_BRANCH_BO;
import tw.org.twntch.bo.BANK_GROUP_BO;
import tw.org.twntch.bo.BUSINESS_TYPE_BO;
import tw.org.twntch.bo.EACHSYSSTATUSTAB_BO;
import tw.org.twntch.bo.EACH_USERLOG_BO;
import tw.org.twntch.bo.ONBLOCKTAB_BO;
import tw.org.twntch.bo.TXN_CODE_BO;
import tw.org.twntch.form.Each_Userlog_Form;
import tw.org.twntch.form.Login_Form;
import tw.org.twntch.form.Onblocktab_Form;
import tw.org.twntch.form.Onblocktab_NotTradRes_Form;
import tw.org.twntch.po.BANK_BRANCH;
import tw.org.twntch.po.BANK_GROUP;
import tw.org.twntch.po.ONBLOCKTAB;
import tw.org.twntch.util.BeanUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.WebServletUtils;

public class ONBLOCKTAB_NotTradRes_Action extends GenericAction {

	private ONBLOCKTAB_BO onblocktab_bo;
	private BANK_GROUP_BO bank_group_bo ;	   //取得總行代號
	private BANK_BRANCH_BO bank_branch_bo;    //取得分行代號 
	private BUSINESS_TYPE_BO business_type_bo;//取得業務代號
	private TXN_CODE_BO txn_code_bo;          //
	private EACHSYSSTATUSTAB_BO eachsysstatustab_bo;
	
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
				
		// TODO Auto-generated method stub
		Onblocktab_NotTradRes_Form obktNtR_form = (Onblocktab_NotTradRes_Form) form ;		
		String target = "";
		String ac_key = StrUtils.isEmpty(obktNtR_form.getAc_key())?"":obktNtR_form.getAc_key();
		target = StrUtils.isEmpty(obktNtR_form.getTarget())?"search":obktNtR_form.getTarget();
		obktNtR_form.setTarget(target);
		List<BANK_BRANCH> list = null;
		List<ONBLOCKTAB> onblist = null;
		//String onblist ="";
		System.out.println("ONBLOCKTAB_NotTradRes_Action is start target>> " + target);
		System.out.println("ONBLOCKTAB_NotTradRes_Action is start ac_key>> " + ac_key);
		Login_Form login_form = (Login_Form) WebServletUtils.getRequest().getSession().getAttribute("login_form");
		Map<String,String> m = new HashMap<String,String>();
				
		if(StrUtils.isNotEmpty(ac_key) && !ac_key.equals("back")){			
			if(ac_key.equals("search")){				
				
			}else if(ac_key.equals("edit")){
				BeanUtils.populate(obktNtR_form, JSONUtils.json2map(obktNtR_form.getEdit_params()));
				String txDate = obktNtR_form.getTXDATE();
				String stan = obktNtR_form.getSTAN();
				System.out.println("TXDATE>>"+txDate);
				System.out.println("STAN>>"+stan);
				Onblocktab_Form onblocktab_form = new Onblocktab_Form();
				Map detailDataMap=onblocktab_bo.showNotTradResDetail(txDate,stan);
				//要用舊的營業日去查手續費
				String obizdate = obktNtR_form.getOLDBIZDATE();
				
				//20220321新增FOR EXTENDFEE 位數轉換
				if(detailDataMap.get("EXTENDFEE")!=null) {
				  BigDecimal orgNewExtendFee = (BigDecimal) detailDataMap.get("EXTENDFEE");
				   //去逗號除100 1,000 > 1000/100 = 10
				  String strOrgNewExtendFee = orgNewExtendFee.toString();
				   double realNewExtendFee = Double.parseDouble(strOrgNewExtendFee.replace(",", ""))/100;
				   detailDataMap.put("NEWEXTENDFEE", String.valueOf(realNewExtendFee));
				}else {
					//如果是null 顯示空字串
					detailDataMap.put("NEWEXTENDFEE", "");
				}
				
				//如果FEE_TYPE有值 且結果為成功或未完成  此功能都拿新的值
				if(StrUtils.isNotEmpty((String)detailDataMap.get("FEE_TYPE")) && 
				   ("成功".equals((String)detailDataMap.get("RESP"))||"未完成".equals((String)detailDataMap.get("RESP")))) {
					switch ((String)detailDataMap.get("FEE_TYPE")){
					case "A":
						detailDataMap.put("TXN_TYPE","固定");
						break;
					case "B":
						detailDataMap.put("TXN_TYPE","外加");
						break;
					case "C":
						detailDataMap.put("TXN_TYPE","百分比");
						break;
					case "D":
						detailDataMap.put("TXN_TYPE","級距");
						break;
					}
					
				//如果FEE_TYPE有值 且結果為失敗或處理中 此功能都拿新的值
				}else if (StrUtils.isNotEmpty((String)detailDataMap.get("FEE_TYPE")) && 
						   ("失敗".equals((String)detailDataMap.get("RESP"))||"處理中".equals((String)detailDataMap.get("RESP")))) {
					switch ((String)detailDataMap.get("FEE_TYPE")){
					case "A":
						detailDataMap.put("TXN_TYPE","固定");
						break;
					case "B":
						detailDataMap.put("TXN_TYPE","外加");
						break;
					case "C":
						detailDataMap.put("TXN_TYPE","百分比");
						break;
					case "D":
						detailDataMap.put("TXN_TYPE","級距");
						break;
					}
					
//					detailDataMap.put("NEWSENDERFEE_NW", detailDataMap.get("NEWSENDERFEE")!=null?detailDataMap.get("NEWSENDERFEE"):"0");
//					detailDataMap.put("NEWINFEE_NW", detailDataMap.get("NEWINFEE")!=null?detailDataMap.get("NEWINFEE"):"0");
//					detailDataMap.put("NEWOUTFEE_NW", detailDataMap.get("NEWOUTFEE")!=null?detailDataMap.get("NEWOUTFEE"):"0");
//					detailDataMap.put("NEWWOFEE_NW", detailDataMap.get("NEWWOFEE")!=null?detailDataMap.get("NEWWOFEE"):"0");
//					detailDataMap.put("NEWEACHFEE_NW", detailDataMap.get("NEWEACHFEE")!=null?detailDataMap.get("NEWEACHFEE"):"0");
//					detailDataMap.put("NEWFEE_NW",detailDataMap.get("NEWFEE")!=null?detailDataMap.get("NEWFEE"):"0");
					
				//如果FEE_TYPE為空 且結果為成功或未完成 新版手續call sp  此功能都拿新的值
				}else if (StrUtils.isEmpty((String)detailDataMap.get("FEE_TYPE")) && 
						   ("成功".equals((String)detailDataMap.get("RESP"))||"未完成".equals((String)detailDataMap.get("RESP")))) {
					Map newFeeDtailMap = onblocktab_bo.getNewFeeDetail(obizdate,(String) detailDataMap.get("TXN_NAME"),(String) detailDataMap.get("SENDERID")
							,(String) detailDataMap.get("SENDERBANKID_NAME"),(String)detailDataMap.get("NEWTXAMT"));
					detailDataMap.put("TXN_TYPE", newFeeDtailMap.get("TXN_TYPE"));
					detailDataMap.put("NEWSENDERFEE_NW", newFeeDtailMap.get("NEWSENDERFEE_NW"));
					detailDataMap.put("NEWINFEE_NW", newFeeDtailMap.get("NEWINFEE_NW"));
					detailDataMap.put("NEWOUTFEE_NW", newFeeDtailMap.get("NEWOUTFEE_NW"));
					detailDataMap.put("NEWWOFEE_NW", newFeeDtailMap.get("NEWWOFEE_NW"));
					detailDataMap.put("NEWEACHFEE_NW", newFeeDtailMap.get("NEWEACHFEE_NW"));
					detailDataMap.put("NEWFEE_NW", newFeeDtailMap.get("NEWFEE_NW"));
					
				//如果FEE_TYPE為空 且結果為失敗或處理中 call sp 拿FEE_TYPE	  此功能都拿新的值
				}else if (StrUtils.isEmpty((String)detailDataMap.get("FEE_TYPE")) && 
						   ("失敗".equals((String)detailDataMap.get("RESP"))||"處理中".equals((String)detailDataMap.get("RESP")))) {
					Map newFeeDtailMap = onblocktab_bo.getNewFeeDetail(obizdate,(String) detailDataMap.get("TXN_NAME"),(String) detailDataMap.get("SENDERID")
							,(String) detailDataMap.get("SENDERBANKID_NAME"),(String)detailDataMap.get("NEWTXAMT"));
					
					detailDataMap.put("TXN_TYPE", newFeeDtailMap.get("TXN_TYPE"));
					detailDataMap.put("NEWSENDERFEE_NW", newFeeDtailMap.get("NEWSENDERFEE")!=null?newFeeDtailMap.get("NEWSENDERFEE"):"0");
					detailDataMap.put("NEWINFEE_NW", newFeeDtailMap.get("NEWINFEE")!=null?newFeeDtailMap.get("NEWINFEE"):"0");
					detailDataMap.put("NEWOUTFEE_NW", newFeeDtailMap.get("NEWOUTFEE")!=null?newFeeDtailMap.get("NEWOUTFEE"):"0");
					detailDataMap.put("NEWWOFEE_NW", newFeeDtailMap.get("NEWWOFEE")!=null?newFeeDtailMap.get("NEWWOFEE"):"0");
					detailDataMap.put("NEWEACHFEE_NW", newFeeDtailMap.get("NEWEACHFEE")!=null?newFeeDtailMap.get("NEWEACHFEE"):"0");
					detailDataMap.put("NEWFEE_NW",newFeeDtailMap.get("NEWFEE")!=null?newFeeDtailMap.get("NEWFEE"):"0");
				
				}
				onblocktab_form.setDetailData(detailDataMap);
				onblocktab_form.setIsUndoneRes("Y");
				BeanUtils.copyProperties(onblocktab_form, obktNtR_form);
				request.setAttribute("onblocktab_form", onblocktab_form);
			}
		}else{			
//			obktNtR_form.setUSER_COMPANY(login_form.getUserData().getUSER_COMPANY());
			obktNtR_form.setOPBK_ID(login_form.getUserData().getUSER_COMPANY());
			
			obktNtR_form.setTXTIME(eachsysstatustab_bo.getBusinessDate());
			
			setDropdownList(obktNtR_form , login_form , ac_key);	
//			onblocktab_form.setResList(onblocktab_bo.getResstatusList());
		}
			
//		下拉清單
		if(!ac_key.equals("edit")){
//			obktNtR_form.setBgIdList(bank_group_bo.getBgbkIdList());      //儲存總行代號清單
			obktNtR_form.setBsIdKist(business_type_bo.getBsTypeIdList()); //儲存業務代號清單
//			obktNtR_form.setTxnIdList(txn_code_bo.getIdList());           //儲存交易類別清單
		}
		if(ac_key.equals("back")){
			resetGenericAttribute(login_form, request);
			System.out.println("SerchStrs>>"+obktNtR_form.getSerchStrs());
			BeanUtils.populate(obktNtR_form, JSONUtils.json2map(obktNtR_form.getSerchStrs()) ); ;
		}	
		System.out.println("forward to >> " + target);
		return (mapping.findForward(target));
	}
	
	public void setDropdownList(Onblocktab_NotTradRes_Form form ,Login_Form  login_form ,String ac_key){
				
		if(login_form.getUserData().getUSER_TYPE().equals("A")){
			//使用者代號清單(以登入時預設的USER_COMPANY查詢)
			if( StrUtils.isNotEmpty(ac_key) && ac_key.equals("back")){
//				form.setUserIdList(onblocktab_bo.getUserIdListByComId(form.getUSER_COMPANY()));
				form.setUserIdList(onblocktab_bo.getUserIdListByComId(form.getOPBK_ID()));
			}else{
				form.setUserIdList(onblocktab_bo.getUserIdListByComId(login_form.getUserData().getUSER_COMPANY()));
			}
			form.setFuncList(onblocktab_bo.getFuncListByRole_Type(login_form.getUserData().getUSER_TYPE()));
			//USER_COMPANY清單
			form.setUserCompanyList(onblocktab_bo.getUserCompanyList());
			//操作行代號清單 20151014 edit by hugo req by UAT-2015112-02
//			form.setOpbkIdList(onblocktab_bo.getOpbkIdList());
			form.setOpbkIdList(bank_group_bo.getOpbkList());
		}else{
			//使用者代號清單(以登入時預設的USER_COMPANY查詢)
			form.setUserIdList(onblocktab_bo.getUserIdListByComId(login_form.getUserData().getUSER_COMPANY()));
			//功能清單
			form.setFuncList(onblocktab_bo.getFuncListByRole_Type(login_form.getUserData().getUSER_TYPE()));
//			form.setUSER_COMPANY(login_form.getUserData().getUSER_COMPANY());
			form.setOPBK_ID(login_form.getUserData().getUSER_COMPANY());
		}
		
	}
	
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
