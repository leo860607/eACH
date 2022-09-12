package tw.org.twntch.action;


import java.math.BigDecimal;
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
import tw.org.twntch.bo.ONBLOCKTAB_BO;
import tw.org.twntch.bo.UNDONE_TXDATA_BO;
import tw.org.twntch.db.dao.hibernate.EACH_FUNC_LIST_Dao;
import tw.org.twntch.form.Login_Form;
import tw.org.twntch.form.Onblocktab_Form;
import tw.org.twntch.form.Undone_Txdata_Form;
import tw.org.twntch.po.BANK_BRANCH;
import tw.org.twntch.util.BeanUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.SpringAppCtxHelper;
import tw.org.twntch.util.StrUtils;

public class UNDONE_TXDATA_Action extends GenericAction {
	private UNDONE_TXDATA_BO undone_txdata_bo;
	private BANK_BRANCH_BO bank_branch_bo;
	private BANK_GROUP_BO bank_group_bo;
	private BUSINESS_TYPE_BO business_type_bo;
	private EACHSYSSTATUSTAB_BO eachsysstatustab_bo;
	private ONBLOCKTAB_BO onblocktab_bo;
	
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Undone_Txdata_Form undone_txdata_form = (Undone_Txdata_Form) form;
		String ac_key = StrUtils.isEmpty(undone_txdata_form.getAc_key())?"":undone_txdata_form.getAc_key();
		String target = StrUtils.isEmpty(undone_txdata_form.getTarget())?"search":undone_txdata_form.getTarget();
		undone_txdata_form.setTarget(target);
		Login_Form login_form = (Login_Form) request.getSession().getAttribute("login_form");
		
		System.out.println("FEE_SEARCH_Action is start >> " + ac_key);
		
		if(ac_key.equalsIgnoreCase("edit")){
			String txDate = undone_txdata_form.getTXDATE();
			String stan = undone_txdata_form.getSTAN();
			System.out.println("TXDATE>>"+txDate);
			System.out.println("STAN>>"+stan);
			Onblocktab_Form onblocktab_form = new Onblocktab_Form();
			Map detailDataMap  = onblocktab_bo.showDetail(txDate,stan);
			String bizdate = eachsysstatustab_bo.getBusinessDateII();
			
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
			
			//如果FEE_TYPE有值 且結果為成功或未完成 新版手續直接取後面欄位
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
				
			//如果FEE_TYPE有值 且結果為失敗或處理中 新版手續跟舊的一樣
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
				detailDataMap.put("NEWSENDERFEE_NW", detailDataMap.get("NEWSENDERFEE")!=null?detailDataMap.get("NEWSENDERFEE"):"0");
				detailDataMap.put("NEWINFEE_NW", detailDataMap.get("NEWINFEE")!=null?detailDataMap.get("NEWINFEE"):"0");
				detailDataMap.put("NEWOUTFEE_NW", detailDataMap.get("NEWOUTFEE")!=null?detailDataMap.get("NEWOUTFEE"):"0");
				detailDataMap.put("NEWWOFEE_NW", detailDataMap.get("NEWWOFEE")!=null?detailDataMap.get("NEWWOFEE"):"0");
				detailDataMap.put("NEWEACHFEE_NW", detailDataMap.get("NEWEACHFEE")!=null?detailDataMap.get("NEWEACHFEE"):"0");
				detailDataMap.put("NEWFEE_NW",detailDataMap.get("NEWFEE")!=null?detailDataMap.get("NEWFEE"):"0");
				
			//如果FEE_TYPE為空 且結果為成功或未完成 新版手續call sp	
			}else if (StrUtils.isEmpty((String)detailDataMap.get("FEE_TYPE")) && 
					   ("成功".equals((String)detailDataMap.get("RESP"))||"未完成".equals((String)detailDataMap.get("RESP")))) {
				Map newFeeDtailMap = onblocktab_bo.getNewFeeDetail(bizdate,(String) detailDataMap.get("TXN_NAME"),(String) detailDataMap.get("SENDERID")
						,(String) detailDataMap.get("SENDERBANKID_NAME"),(String)detailDataMap.get("NEWTXAMT"));
				detailDataMap.put("TXN_TYPE", newFeeDtailMap.get("TXN_TYPE"));
				detailDataMap.put("NEWSENDERFEE_NW", newFeeDtailMap.get("NEWSENDERFEE_NW"));
				detailDataMap.put("NEWINFEE_NW", newFeeDtailMap.get("NEWINFEE_NW"));
				detailDataMap.put("NEWOUTFEE_NW", newFeeDtailMap.get("NEWOUTFEE_NW"));
				detailDataMap.put("NEWWOFEE_NW", newFeeDtailMap.get("NEWWOFEE_NW"));
				detailDataMap.put("NEWEACHFEE_NW", newFeeDtailMap.get("NEWEACHFEE_NW"));
				detailDataMap.put("NEWFEE_NW", newFeeDtailMap.get("NEWFEE_NW"));
				
			//如果FEE_TYPE為空 且結果為失敗或處理中 新版手續跟舊的一樣
			}else if (StrUtils.isEmpty((String)detailDataMap.get("FEE_TYPE")) && 
					   ("失敗".equals((String)detailDataMap.get("RESP"))||"處理中".equals((String)detailDataMap.get("RESP")))) {
				Map newFeeDtailMap = onblocktab_bo.getNewFeeDetail(bizdate,(String) detailDataMap.get("TXN_NAME"),(String) detailDataMap.get("SENDERID")
						,(String) detailDataMap.get("SENDERBANKID_NAME"),(String)detailDataMap.get("NEWTXAMT"));
				detailDataMap.put("TXN_TYPE", newFeeDtailMap.get("TXN_TYPE"));
				detailDataMap.put("NEWSENDERFEE_NW", detailDataMap.get("NEWSENDERFEE")!=null?detailDataMap.get("NEWSENDERFEE"):"0");
				detailDataMap.put("NEWINFEE_NW", detailDataMap.get("NEWINFEE")!=null?detailDataMap.get("NEWINFEE"):"0");
				detailDataMap.put("NEWOUTFEE_NW", detailDataMap.get("NEWOUTFEE")!=null?detailDataMap.get("NEWOUTFEE"):"0");
				detailDataMap.put("NEWWOFEE_NW", detailDataMap.get("NEWWOFEE")!=null?detailDataMap.get("NEWWOFEE"):"0");
				detailDataMap.put("NEWEACHFEE_NW", detailDataMap.get("NEWEACHFEE")!=null?detailDataMap.get("NEWEACHFEE"):"0");
				detailDataMap.put("NEWFEE_NW",detailDataMap.get("NEWFEE")!=null?detailDataMap.get("NEWFEE"):"0");
			
			}
			
			onblocktab_form.setDetailData(detailDataMap);
			
			BeanUtils.copyProperties(onblocktab_form, undone_txdata_form);
			onblocktab_form.setIsUndone("Y");
			System.out.println("FILTER_BAT>>"+onblocktab_form.getFILTER_BAT());
			request.setAttribute("onblocktab_form", onblocktab_form);
		}
		if(ac_key.equalsIgnoreCase("update")){
		}
		if(ac_key.equalsIgnoreCase("add")){
		}
		if(StrUtils.isEmpty(ac_key) || ac_key.equalsIgnoreCase("back")){
			System.out.println("FILTER_BAT>>"+undone_txdata_form.getFILTER_BAT());
			//操作行代號清單
//			undone_txdata_form.setOpbkIdList(undone_txdata_bo.getOpbkIdList());
			undone_txdata_form.setOpbkIdList(bank_group_bo.getOpbkList());
			//業務類別清單
			undone_txdata_form.setBsTypeList(business_type_bo.getBsTypeIdList());
			
			String businessDate = eachsysstatustab_bo.getBusinessDate();
			undone_txdata_form.setSTART_DATE(businessDate);
			undone_txdata_form.setEND_DATE(businessDate);
			
			Map userData = BeanUtils.describe(login_form.getUserData());
			//銀行端預設帶入操作行
			if(((String)userData.get("USER_TYPE")).equals("B")){
//				20150407 edit by hugo 只會有操作行故只能抓總行檔 抓分行檔 997會查無資料
//				BANK_BRANCH po = bank_branch_bo.searchByBrbkId((String)userData.get("USER_COMPANY")).get(0);
//				undone_txdata_form.setOPBK_ID(bank_group_bo.search(po.getId().getBGBK_ID()).get(0).getOPBK_ID());
				undone_txdata_form.setOPBK_ID(bank_group_bo.search((String)userData.get("USER_COMPANY")).get(0).getOPBK_ID());
			}
		}
		if(ac_key.equalsIgnoreCase("back")){
			resetGenericAttribute(login_form, request);
			System.out.println("SerchStrs>>"+undone_txdata_form.getSerchStrs());
			BeanUtils.populate(undone_txdata_form, JSONUtils.json2map(undone_txdata_form.getSerchStrs()) ); ;
		}
		target = StrUtils.isEmpty(undone_txdata_form.getTarget())?"":undone_txdata_form.getTarget();
		return mapping.findForward(target);
	}
	public UNDONE_TXDATA_BO getUndone_txdata_bo() {
		return undone_txdata_bo;
	}
	public void setUndone_txdata_bo(UNDONE_TXDATA_BO undone_txdata_bo) {
		this.undone_txdata_bo = undone_txdata_bo;
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
	public BUSINESS_TYPE_BO getBusiness_type_bo() {
		return business_type_bo;
	}
	public void setBusiness_type_bo(BUSINESS_TYPE_BO business_type_bo) {
		this.business_type_bo = business_type_bo;
	}
	public EACHSYSSTATUSTAB_BO getEachsysstatustab_bo() {
		return eachsysstatustab_bo;
	}
	public void setEachsysstatustab_bo(EACHSYSSTATUSTAB_BO eachsysstatustab_bo) {
		this.eachsysstatustab_bo = eachsysstatustab_bo;
	}
	public ONBLOCKTAB_BO getOnblocktab_bo() {
		return onblocktab_bo;
	}
	public void setOnblocktab_bo(ONBLOCKTAB_BO onblocktab_bo) {
		this.onblocktab_bo = onblocktab_bo;
	}
	
	
}
