package tw.org.twntch.action;

import java.lang.reflect.InvocationTargetException;
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

import tw.org.twntch.bo.Arguments;
import tw.org.twntch.bo.BANK_BRANCH_BO;
import tw.org.twntch.bo.BANK_GROUP_BO;
import tw.org.twntch.bo.BUSINESS_TYPE_BO;
import tw.org.twntch.bo.EACHSYSSTATUSTAB_BO;
import tw.org.twntch.bo.ONBLOCKTAB_BO;
import tw.org.twntch.bo.PI_COMPANY_PROFILE_BO;
import tw.org.twntch.bo.TXN_CODE_BO;
import tw.org.twntch.form.Login_Form;
import tw.org.twntch.form.Onblocktab_Form;
import tw.org.twntch.po.BANK_BRANCH;
import tw.org.twntch.po.ONBLOCKTAB;
import tw.org.twntch.util.BeanUtils;
import tw.org.twntch.util.CodeUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.WebServletUtils;
import tw.org.twntch.util.zDateHandler;

public class ONBLOCKTAB_Action extends GenericAction {
	private ONBLOCKTAB_BO onblocktab_bo;
	private BANK_GROUP_BO bank_group_bo ;	   //取得總行代號
	private BANK_BRANCH_BO bank_branch_bo;    //取得分行代號 
	private BUSINESS_TYPE_BO business_type_bo;//取得業務代號
	private TXN_CODE_BO txn_code_bo;          //
	private EACHSYSSTATUSTAB_BO eachsysstatustab_bo;
	private PI_COMPANY_PROFILE_BO pi_company_profile_bo;
	
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// TODO Auto-generated method stub
		Onblocktab_Form onblocktab_form = (Onblocktab_Form) form ;		
		String target = "";
		String ac_key = StrUtils.isEmpty(onblocktab_form.getAc_key())?"":onblocktab_form.getAc_key();
		target = StrUtils.isEmpty(onblocktab_form.getTarget())?"search":onblocktab_form.getTarget();
		onblocktab_form.setTarget(target);
		List<BANK_BRANCH> list = null;
		List<ONBLOCKTAB> onblist = null;
		//取得繳費類別清單
		onblocktab_form.setPfclassList(pi_company_profile_bo.getIdListByBillType());
		//String onblist ="";
		System.out.println("ONBLOCKTAB_Action is start target>> " + target);
		System.out.println("ONBLOCKTAB_Action is start ac_key>> " + ac_key);
		Login_Form login_form = (Login_Form) WebServletUtils.getRequest().getSession().getAttribute("login_form");
		Map<String,String> m = new HashMap<String,String>();
		
		if(StrUtils.isNotEmpty(ac_key)){			
			if(ac_key.equals("search")){				
				//取得總行檔清單
//				list = bank_branch_bo.search(onblocktab_form.getBGBK_ID(), onblocktab_form.getBRBK_ID());
//				m.put("TXTIME1", onblocktab_form.getTXTIME1());
//				m.put("TXTIME2", onblocktab_form.getTXTIME2());				
//				onblist = onblocktab_bo.getResList(m);
//				System.out.println("onblist==>"+onblist);
			}else if(ac_key.equals("back")){
				if(onblocktab_form.getSourcePage().equals("onblocktab")){
					setDropdownList(onblocktab_form , login_form ,ac_key);
					setSearchCondition(onblocktab_form);
				}else{
					target = onblocktab_form.getSourcePage();
					//System.out.println(onblocktab_form.getSerchStrs());
				}
			}else if(ac_key.equals("edit")){
				BeanUtils.populate(onblocktab_form, JSONUtils.json2map(onblocktab_form.getEdit_params()));
				String txDate = onblocktab_form.getTXDATE();
				String stan = onblocktab_form.getSTAN();
				System.out.println("TXDATE>>"+txDate);
				System.out.println("STAN>>"+stan);
				String bizdate = eachsysstatustab_bo.getBusinessDateII();
				
				Map detailDataMap = onblocktab_bo.showDetail(txDate,stan);
				
				//20220210新增FOR EXTENDFEE 位數轉換
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
						if("1".equals((String)detailDataMap.get("FEE_LVL_TYPE"))) {
							detailDataMap.put("TXN_TYPE","級距-固定");
						}else if("2".equals((String)detailDataMap.get("FEE_LVL_TYPE"))){
							detailDataMap.put("TXN_TYPE","級距-百分比");
						}else {
							detailDataMap.put("TXN_TYPE","級距");
						}
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
						if("1".equals((String)detailDataMap.get("FEE_LVL_TYPE"))) {
							detailDataMap.put("TXN_TYPE","級距-固定");
						}else if("2".equals((String)detailDataMap.get("FEE_LVL_TYPE"))){
							detailDataMap.put("TXN_TYPE","級距-百分比");
						}else {
							detailDataMap.put("TXN_TYPE","級距");
						}
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
					Map newFeeDtailMap = onblocktab_bo.getNewFeeDetail(((String) detailDataMap.get("BIZDATE")).replaceAll("-", ""),(String) detailDataMap.get("TXN_NAME"),(String) detailDataMap.get("SENDERID")
							,(String) detailDataMap.get("SENDERBANKID_NAME"),(String)detailDataMap.get("NEWTXAMT"));
					detailDataMap.put("TXN_TYPE", newFeeDtailMap.get("TXN_TYPE"));
					detailDataMap.put("NEWSENDERFEE_NW", newFeeDtailMap.get("NEWSENDERFEE_NW"));
					detailDataMap.put("NEWINFEE_NW", newFeeDtailMap.get("NEWINFEE_NW"));
					detailDataMap.put("NEWOUTFEE_NW", newFeeDtailMap.get("NEWOUTFEE_NW"));
					detailDataMap.put("NEWWOFEE_NW", newFeeDtailMap.get("NEWWOFEE_NW"));
					detailDataMap.put("NEWEACHFEE_NW", newFeeDtailMap.get("NEWEACHFEE_NW"));
					detailDataMap.put("NEWFEE_NW", newFeeDtailMap.get("NEWFEE_NW"));
					
				//如果FEE_TYPE為空 且結果為失敗或處理中 新版手續跟舊的一樣 但還是要call sp 拿FEE_TYPE
				}else if (StrUtils.isEmpty((String)detailDataMap.get("FEE_TYPE")) && 
						   ("失敗".equals((String)detailDataMap.get("RESP"))||"處理中".equals((String)detailDataMap.get("RESP")))) {
					
					Map newFeeDtailMap = onblocktab_bo.getNewFeeDetail(((String) detailDataMap.get("BIZDATE")).replaceAll("-", ""),(String) detailDataMap.get("TXN_NAME"),(String) detailDataMap.get("SENDERID")
							,(String) detailDataMap.get("SENDERBANKID_NAME"),(String)detailDataMap.get("NEWTXAMT"));
					detailDataMap.put("TXN_TYPE", newFeeDtailMap.get("TXN_TYPE"));
					detailDataMap.put("NEWSENDERFEE_NW", detailDataMap.get("NEWSENDERFEE")!=null?detailDataMap.get("NEWSENDERFEE"):"0");
					detailDataMap.put("NEWINFEE_NW", detailDataMap.get("NEWINFEE")!=null?detailDataMap.get("NEWINFEE"):"0");
					detailDataMap.put("NEWOUTFEE_NW", detailDataMap.get("NEWOUTFEE")!=null?detailDataMap.get("NEWOUTFEE"):"0");
					detailDataMap.put("NEWWOFEE_NW", detailDataMap.get("NEWWOFEE")!=null?detailDataMap.get("NEWWOFEE"):"0");
					detailDataMap.put("NEWEACHFEE_NW", detailDataMap.get("NEWEACHFEE")!=null?detailDataMap.get("NEWEACHFEE"):"0");
					detailDataMap.put("NEWFEE_NW",detailDataMap.get("NEWFEE")!=null?detailDataMap.get("NEWFEE"):"0");
				
				}
				//如果結果為成功 但FEE_TYPE為空  CALL SP 拿資料
				onblocktab_form.setDetailData(detailDataMap);

				
				onblocktab_form.setTXDATE(txDate);
				onblocktab_form.setSTAN(stan);
				
				onblocktab_form.setPFCLASS(onblocktab_form.getPFCLASS());
				onblocktab_form.setTOLLID(onblocktab_form.getTOLLID());
				onblocktab_form.setCHARGETYPE(onblocktab_form.getCHARGETYPE());
				onblocktab_form.setBILLFLAG(onblocktab_form.getBILLFLAG());
//				onblist = onblocktab_bo.showDetail(onblocktab_form.getTXDT());
			}else if(ac_key.equals("search_csv")){
				Map<String,String> searchMap = CodeUtils.objectCovert(Map.class, onblocktab_form);
				Map map = null;
				map = onblocktab_bo.export_csv(searchMap);
				BeanUtils.populate(onblocktab_form, map);
				if(map.get("result").equals("TRUE") && !Arguments.getStringArg("ISWINOPEN").equals("Y")){
					exportFile2Client(response, map.get("msg").toString(), zDateHandler.getDateNum()+"_txdata.csv", onblocktab_form.getDow_token() );
					return null;
				}
			}
		}else{
			String busDate = eachsysstatustab_bo.getBusinessDate();
			onblocktab_form.setTXTIME1(busDate);
			onblocktab_form.setTXTIME2(busDate);
			
			//onblocktab_form.setUSER_COMPANY(login_form.getUserData().getUSER_COMPANY());
			setDropdownList(onblocktab_form , login_form , ac_key);	
//			onblocktab_form.setResList(onblocktab_bo.getResstatusList());
		}
			
//		下拉清單
		if(!ac_key.equals("edit")){
			onblocktab_form.setBgIdList(bank_group_bo.getBgbkIdList());      //儲存總行代號清單
			onblocktab_form.setBsIdKist(business_type_bo.getBsTypeIdList()); //儲存業務代號清單
			onblocktab_form.setTxnIdList(txn_code_bo.getIdList());           //儲存交易類別清單
		}
		System.out.println("forward to >> " + target);
		return (mapping.findForward(target));
	}
	
	public void setDropdownList(Onblocktab_Form form ,Login_Form  login_form ,String ac_key){
				
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
			//操作行清單
			form.setOpbkIdList(bank_group_bo.getOpbkList());
//			form.setOpbkIdList(bank_group_bo.getOpbkList());
		}else{
			//使用者代號清單(以登入時預設的USER_COMPANY查詢)
			form.setUserIdList(onblocktab_bo.getUserIdListByComId(login_form.getUserData().getUSER_COMPANY()));
			//功能清單
			form.setFuncList(onblocktab_bo.getFuncListByRole_Type(login_form.getUserData().getUSER_TYPE()));
//			form.setUSER_COMPANY(login_form.getUserData().getUSER_COMPANY());
			form.setOPBK_ID(login_form.getUserData().getUSER_COMPANY());
		}
		
	}
	
	public void setSearchCondition(Onblocktab_Form onblocktab_form){
		Map<String, String> params = new HashMap<String, String>(); 
//		String searchCondition = onblocktab_form.getSearchCondition();
		String searchCondition = onblocktab_form.getSerchStrs();
		System.out.println("getSerchStrs>>"+onblocktab_form.getSerchStrs()); 
		System.out.println("searchCondition>>"+searchCondition); 
		if(StrUtils.isNotEmpty(searchCondition)){
//			String[] qs = searchCondition.split("&");  
//		    Map<String, String> params = new HashMap<String, String>();  
//		    for (String param : qs) {  
//		    	String name = param.split("=")[0];
//		        String value = param.indexOf("=") == param.length() - 1?"":param.split("=")[1];  
//		        params.put(name, value);  
//		    }  
			params = JSONUtils.json2map(searchCondition);
			onblocktab_form.setSerchStrs(searchCondition);
			onblocktab_form.setTXTIME1(params.get("TXTIME1"));
			onblocktab_form.setTXTIME2(params.get("TXTIME2"));
			onblocktab_form.setSTAN(params.get("STAN"));
			onblocktab_form.setCDNUMRAO(params.get("CDNUMRAO"));
			onblocktab_form.setCARDNUM_ID(params.get("CARDNUM_ID"));
			onblocktab_form.setOpAction1(params.get("opAction1"));
			onblocktab_form.setUSERID(params.get("USERID"));
			onblocktab_form.setTXAMT(params.get("TXAMT"));
			onblocktab_form.setSearchAspect(params.get("searchAspect"));
//			onblocktab_form.setUSER_COMPANY(params.get("USER_COMPANY"));
			onblocktab_form.setOPBK_ID(params.get("OPBK_ID"));
			onblocktab_form.setBGBK_ID(params.get("BGBK_ID"));
			onblocktab_form.setBRBK_ID(params.get("BRBK_ID"));
			onblocktab_form.setCLEARINGPHASE(params.get("CLEARINGPHASE"));
			onblocktab_form.setBUSINESS_TYPE_ID(params.get("BUSINESS_TYPE_ID"));
			onblocktab_form.setRESULTSTATUS(params.get("RESULTSTATUS"));
			onblocktab_form.setGARBAGEDATA(params.get("GARBAGEDATA"));
			onblocktab_form.setFILTER_BAT(params.get("FILTER_BAT"));
			onblocktab_form.setPFCLASS(params.get("PFCLASS"));
			onblocktab_form.setTOLLID(params.get("TOLLID"));
			onblocktab_form.setCHARGETYPE(params.get("CHARGETYPE"));
			onblocktab_form.setBILLFLAG(params.get("BILLFLAG"));
			onblocktab_form.setTXTIME3(params.get("TXTIME3"));
			onblocktab_form.setTXTIME4(params.get("TXTIME4"));
			onblocktab_form.setHOUR1(params.get("HOUR1"));
			onblocktab_form.setHOUR2(params.get("HOUR2"));
			onblocktab_form.setMON1(params.get("MON1"));
			onblocktab_form.setMON2(params.get("MON2"));
			onblocktab_form.setTXID(params.get("TXID"));
			onblocktab_form.setFEE_TYPE(params.get("FEE_TYPE"));
			onblocktab_form.setRESSTATUS(params.get("RESSTATUS"));
			onblocktab_form.setRCSERCTEXT(params.get("RCSERCTEXT"));
			onblocktab_form.setAc_key("back");
			try {
				System.out.println("#### Search Condition:\n" + BeanUtils.describe(onblocktab_form));
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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

	public PI_COMPANY_PROFILE_BO getPi_company_profile_bo() {
		return pi_company_profile_bo;
	}

	public void setPi_company_profile_bo(PI_COMPANY_PROFILE_BO pi_company_profile_bo) {
		this.pi_company_profile_bo = pi_company_profile_bo;
	}
	
}
