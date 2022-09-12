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
import tw.org.twntch.bo.RPTST_1_BO;
import tw.org.twntch.bo.RPTTX_1_BO;
import tw.org.twntch.form.Login_Form;
import tw.org.twntch.form.Rptst_1_Form;
import tw.org.twntch.form.Rpttx_1_Form;
import tw.org.twntch.util.BeanUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.zDateHandler;

public class RPTST_1_Action extends GenericAction {
	private RPTST_1_BO rptst_1_bo ;
	private EACHSYSSTATUSTAB_BO eachsysstatustab_bo;
	private BANK_GROUP_BO bank_group_bo;
	
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Rptst_1_Form rptst_1_form = (Rptst_1_Form) form;
		String ac_key = StrUtils.isEmpty(rptst_1_form.getAc_key())?"":rptst_1_form.getAc_key();
		String target = StrUtils.isEmpty(rptst_1_form.getTarget())?"search":rptst_1_form.getTarget();
		rptst_1_form.setTarget(target);
		System.out.println("RPTST_1_Action is start >> " + ac_key);
		Login_Form login_form = (Login_Form) request.getSession().getAttribute("login_form");
		
		if(ac_key.startsWith("search")){
			String ext = ac_key.split("_")[1];
			Map map = null;
			if(ext.equals("xls")){
				map = rptst_1_bo.ex_export(ext,
						rptst_1_form.getSTART_DATE(),
						rptst_1_form.getEND_DATE(),
						rptst_1_form.getOPBK_ID(),
						rptst_1_form.getBGBK_ID(),
						rptst_1_form.getRESULTSTATUS(),
						rptst_1_form.getCLEARINGPHASE(),
						rptst_1_form.getPCODE(),
						rptst_1_form.getSerchStrs()
				);
			}else if(ext.equals("pdf")){
				map = rptst_1_bo.export(ext,
						rptst_1_form.getSTART_DATE(),
						rptst_1_form.getEND_DATE(),
						rptst_1_form.getOPBK_ID(),
						rptst_1_form.getBGBK_ID(),
						rptst_1_form.getRESULTSTATUS(),
						rptst_1_form.getCLEARINGPHASE(),
						rptst_1_form.getPCODE(),
						rptst_1_form.getSerchStrs()
				);
			}
			BeanUtils.populate(rptst_1_form, map);
			System.out.println(BeanUtils.describe(rptst_1_form));
			if(map.get("result").equals("TRUE") && !Arguments.getStringArg("ISWINOPEN").equals("Y")){
				exportFile2Client(response, map.get("msg").toString(), zDateHandler.getDateNum()+"_st_1." + ext, rptst_1_form.getDow_token() );
				return null;
			}
		}
		if(StrUtils.isEmpty(ac_key)){
			String bsDate = eachsysstatustab_bo.getRptBusinessDate();
			rptst_1_form.setSTART_DATE(bsDate);
			rptst_1_form.setEND_DATE(bsDate);
			
			String userType = login_form.getUserData().getUSER_TYPE();
			if(userType.equalsIgnoreCase("A")){
//				rptst_1_form.setOpbkIdList(rptst_1_bo.getOpbkIdList());
				rptst_1_form.setOpbkIdList(bank_group_bo.getOpbkList());
			}else if(userType.equalsIgnoreCase("B")){
				rptst_1_form.setOPBK_ID(login_form.getUserData().getUSER_COMPANY());
			}
		}
		rptst_1_form.setPcodeList(rptst_1_bo.getPcodeList());
		//rptst_1_form.setBgbkIdList(rptst_1_bo.getBgbkIdList());
		target = StrUtils.isEmpty(rptst_1_form.getTarget())?"":rptst_1_form.getTarget();
		return mapping.findForward(target);
	}

	public RPTST_1_BO getRptst_1_bo() {
		return rptst_1_bo;
	}

	public void setRptst_1_bo(RPTST_1_BO rptst_1_bo) {
		this.rptst_1_bo = rptst_1_bo;
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
	
}
