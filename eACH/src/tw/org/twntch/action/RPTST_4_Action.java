package tw.org.twntch.action;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import tw.org.twntch.bo.Arguments;
import tw.org.twntch.bo.BANK_GROUP_BO;
import tw.org.twntch.bo.EACHSYSSTATUSTAB_BO;
import tw.org.twntch.bo.RPTST_4_BO;
import tw.org.twntch.bo.TXN_CODE_BO;
import tw.org.twntch.form.Rptst_4_Form;
import tw.org.twntch.util.BeanUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.zDateHandler;

public class RPTST_4_Action extends GenericAction {
	private RPTST_4_BO rptst_4_bo ;
	private EACHSYSSTATUSTAB_BO eachsysstatustab_bo;
	private BANK_GROUP_BO bank_group_bo;
	private TXN_CODE_BO txn_code_bo ;
	
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Rptst_4_Form rptst_4_form = (Rptst_4_Form) form;
		String ac_key = StrUtils.isEmpty(rptst_4_form.getAc_key())?"":rptst_4_form.getAc_key();
		String target = StrUtils.isEmpty(rptst_4_form.getTarget())?"search":rptst_4_form.getTarget();
		rptst_4_form.setTarget(target);
		System.out.println("RPTST_4_Action is start >> " + ac_key);
		
		if(ac_key.startsWith("search")){
			String ext = ac_key.split("_")[1];
			Map map = null;
			if(ext.equals("xls")){
				map = rptst_4_bo.ex_export(ext,
						rptst_4_form.getSTART_YEAR(),
						rptst_4_form.getSTART_MONTH(),
						rptst_4_form.getEND_YEAR(),
						rptst_4_form.getEND_MONTH(),
						rptst_4_form.getOPBK_ID(),
						rptst_4_form.getBGBK_ID(),
						rptst_4_form.getSENDERID(),
						rptst_4_form.getFEE_TYPE(),
						rptst_4_form.getCOMPANY_NAME(),
						rptst_4_form.getTXN_ID(),
						rptst_4_form.getSerchStrs()
				);
			}else if(ext.equals("pdf")){
				map = rptst_4_bo.export(ext,
						rptst_4_form.getSTART_YEAR(),
						rptst_4_form.getSTART_MONTH(),
						rptst_4_form.getEND_YEAR(),
						rptst_4_form.getEND_MONTH(),
						rptst_4_form.getOPBK_ID(),
						rptst_4_form.getBGBK_ID(),
						rptst_4_form.getSENDERID(),
						rptst_4_form.getFEE_TYPE(),
						rptst_4_form.getCOMPANY_NAME(),
						rptst_4_form.getTXN_ID(),
						rptst_4_form.getSerchStrs()
				);
			}else if(ext.equals("csv")){
				map = rptst_4_bo.export(ext,
						rptst_4_form.getSTART_YEAR(),
						rptst_4_form.getSTART_MONTH(),
						rptst_4_form.getEND_YEAR(),
						rptst_4_form.getEND_MONTH(),
						rptst_4_form.getOPBK_ID(),
						rptst_4_form.getBGBK_ID(),
						rptst_4_form.getSENDERID(),
						rptst_4_form.getFEE_TYPE(),
						rptst_4_form.getCOMPANY_NAME(),
						rptst_4_form.getTXN_ID(),
						rptst_4_form.getSerchStrs()
				);
			}
			
			BeanUtils.populate(rptst_4_form, map);
			System.out.println(BeanUtils.describe(rptst_4_form));
			if(map.get("result").equals("TRUE") && !Arguments.getStringArg("ISWINOPEN").equals("Y")){
				exportFile2Client(response, map.get("msg").toString(), zDateHandler.getDateNum()+"_st_4." + ext, rptst_4_form.getDow_token() );
				return null;
			}
		}
		if(StrUtils.isEmpty(ac_key)){
			String bsDate = eachsysstatustab_bo.getRptBusinessDate();
//			20160118 edit by hugo req by UAT-20160112-01..~05
			Map<String,String> map = getRPT_PRE_YandM_M1(bsDate, "yyyyMMdd", "START_YEAR", "START_MONTH", "END_YEAR", "END_MONTH");
			BeanUtils.populate(rptst_4_form, map);
//			rptst_4_form.setSTART_YEAR(bsDate.substring(0, 4));
//			rptst_4_form.setSTART_MONTH(getLastMonth(bsDate.substring(4, 6)));
//			rptst_4_form.setEND_YEAR(bsDate.substring(0, 4));
//			rptst_4_form.setEND_MONTH(getLastMonth(bsDate.substring(4, 6)));
		}
//		rptst_4_form.setOpbkIdList(rptst_4_bo.getOpbkIdList());
		rptst_4_form.setOpbkIdList(bank_group_bo.getOpbkList());
		//rptst_4_form.setBgbkIdList(rptst_4_bo.getBgbkIdList());
		rptst_4_form.setIdList(txn_code_bo.getIdList());
		target = StrUtils.isEmpty(rptst_4_form.getTarget())?"":rptst_4_form.getTarget();
		return mapping.findForward(target);
	}
	
	public String getLastMonth(String bsDate_month){
		int int_month = Integer.parseInt(bsDate_month)-1;
		String month = Integer.toString(int_month);
		if(month.length() < 2){
			month = "0"+month;
		}
		return month;
	}

	public RPTST_4_BO getRptst_4_bo() {
		return rptst_4_bo;
	}

	public void setRptst_4_bo(RPTST_4_BO rptst_4_bo) {
		this.rptst_4_bo = rptst_4_bo;
	}

	public EACHSYSSTATUSTAB_BO getEachsysstatustab_bo() {
		return eachsysstatustab_bo;
	}

	public void setEachsysstatustab_bo(EACHSYSSTATUSTAB_BO eachsysstatustab_bo) {
		this.eachsysstatustab_bo = eachsysstatustab_bo;
	}

	public BANK_GROUP_BO getBank_group_bo() {
		return bank_group_bo;
	}

	public void setBank_group_bo(BANK_GROUP_BO bank_group_bo) {
		this.bank_group_bo = bank_group_bo;
	}

	public TXN_CODE_BO getTxn_code_bo() {
		return txn_code_bo;
	}

	public void setTxn_code_bo(TXN_CODE_BO txn_code_bo) {
		this.txn_code_bo = txn_code_bo;
	}
	
}
