package tw.org.twntch.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import tw.org.twntch.bo.Arguments;
import tw.org.twntch.bo.EACHSYSSTATUSTAB_BO;
import tw.org.twntch.bo.SETTLE_DAY_BO;
import tw.org.twntch.form.Login_Form;
import tw.org.twntch.form.Settle_Day_Form;
import tw.org.twntch.util.BeanUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.zDateHandler;
//結算統計查詢(日)
public class SETTLE_DAY_Action extends GenericAction {
	private SETTLE_DAY_BO settle_day_bo;
	private EACHSYSSTATUSTAB_BO eachsysstatustab_bo;
	
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Settle_Day_Form settle_day_form = (Settle_Day_Form) form;
		String ac_key = StrUtils.isEmpty(settle_day_form.getAc_key())?"":settle_day_form.getAc_key();
		String target = StrUtils.isEmpty(settle_day_form.getTarget())?"search":settle_day_form.getTarget();
		settle_day_form.setTarget(target);
		Login_Form login_form = (Login_Form)request.getSession().getAttribute("login_form");
		
		System.out.println("ac_key=" + ac_key);
		System.out.println("target=" + target);
		
		//交易類別清單
		settle_day_form.setPcodeList(settle_day_bo.getPcodeList());
		//操作行代號清單
		settle_day_form.setOpbkIdList(settle_day_bo.getOpbkIdList());
		
		if(ac_key.equalsIgnoreCase("export")){
			Map map = settle_day_bo.qs_ex_export(
					settle_day_form.getDATE(),
					settle_day_form.getPCODE(),
					settle_day_form.getOPBK_ID(),
					settle_day_form.getBGBK_ID(),
					settle_day_form.getCLEARINGPHASE(),
					settle_day_form.getSerchStrs(),
					settle_day_form.getSortname(),
					settle_day_form.getSortorder()
			);
			BeanUtils.populate(settle_day_form, map);
			if(map.get("result").equals("TRUE") && !Arguments.getStringArg("ISWINOPEN").equals("Y")){
				exportFile2Client(response, map.get("msg").toString(), zDateHandler.getDateNum()+"_settle_day.xls" ,settle_day_form.getDow_token() );
				return null;
			}
		}else if(StrUtils.isEmpty(ac_key)){
			//將營業日塞到頁面的日期控制項
			String busDate = eachsysstatustab_bo.getBusinessDate();
			System.out.println("busDate="+busDate);
			settle_day_form.setDATE(busDate);
			//銀行端
			if(login_form.getUserData().getUSER_TYPE().equals("B")){
				settle_day_form.setOPBK_ID(login_form.getUserData().getUSER_COMPANY());
			}
		}
		
		target = StrUtils.isEmpty(settle_day_form.getTarget())?"":settle_day_form.getTarget();
		return mapping.findForward(target);
	}

	public SETTLE_DAY_BO getSettle_day_bo() {
		return settle_day_bo;
	}
	public void setSettle_day_bo(SETTLE_DAY_BO settle_day_bo) {
		this.settle_day_bo = settle_day_bo;
	}
	public EACHSYSSTATUSTAB_BO getEachsysstatustab_bo() {
		return eachsysstatustab_bo;
	}
	public void setEachsysstatustab_bo(EACHSYSSTATUSTAB_BO eachsysstatustab_bo) {
		this.eachsysstatustab_bo = eachsysstatustab_bo;
	}
}
