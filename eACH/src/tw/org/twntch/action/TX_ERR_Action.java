package tw.org.twntch.action;

import java.math.BigDecimal;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import tw.org.twntch.bo.EACHSYSSTATUSTAB_BO;
import tw.org.twntch.bo.ONBLOCKTAB_BO;
import tw.org.twntch.bo.TX_ERR_BO;
import tw.org.twntch.form.Login_Form;
import tw.org.twntch.form.Onblocktab_Form;
import tw.org.twntch.util.BeanUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.StrUtils;

public class TX_ERR_Action extends GenericAction {
	private TX_ERR_BO tx_err_bo;
	private EACHSYSSTATUSTAB_BO eachsysstatustab_bo;
	private ONBLOCKTAB_BO onblocktab_bo;
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Onblocktab_Form tx_err_form = (Onblocktab_Form) form;
		String ac_key = StrUtils.isEmpty(tx_err_form.getAc_key())?"":tx_err_form.getAc_key();
		String target = StrUtils.isEmpty(tx_err_form.getTarget())?"search":tx_err_form.getTarget();
		tx_err_form.setTarget(target);
		Login_Form login_form = (Login_Form) request.getSession().getAttribute("login_form");
		
		System.out.println("TX_ERR_Action is start >> " + ac_key);
		
		if(ac_key.equalsIgnoreCase("edit")){
			Map editParam = JSONUtils.json2map(tx_err_form.getEdit_params());
			Map detailDataMap =tx_err_bo.searchByPk((String)editParam.get("TXDATE"), (String)editParam.get("STAN"));
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
			tx_err_form.setDetailData(detailDataMap);
		}
		if(ac_key.equalsIgnoreCase("update")){
		}
		if(ac_key.equalsIgnoreCase("add")){
		}
		if(StrUtils.isEmpty(ac_key) || ac_key.equalsIgnoreCase("back")){
			if(StrUtils.isEmpty(tx_err_form.getBIZDATE())){
				tx_err_form.setBIZDATE(eachsysstatustab_bo.getBusinessDate());
			}
			
			if(ac_key.equals("back")){
				resetGenericAttribute(login_form, request);
				BeanUtils.populate(tx_err_form, JSONUtils.json2map(tx_err_form.getSerchStrs()));
			}
		}
		target = StrUtils.isEmpty(tx_err_form.getTarget())?"":tx_err_form.getTarget();
		return mapping.findForward(target);
	}
	public TX_ERR_BO getTx_err_bo() {
		return tx_err_bo;
	}
	public void setTx_err_bo(TX_ERR_BO tx_err_bo) {
		this.tx_err_bo = tx_err_bo;
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
