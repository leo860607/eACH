package tw.org.twntch.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import tw.org.twntch.bo.Arguments;
import tw.org.twntch.bo.EACHSYSSTATUSTAB_BO;
import tw.org.twntch.bo.TXS_DAY_BO;
import tw.org.twntch.form.Login_Form;
import tw.org.twntch.form.Txs_Day_Form;
import tw.org.twntch.util.BeanUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.WebServletUtils;
import tw.org.twntch.util.zDateHandler;

public class TXS_DAY_Action extends GenericAction {
	private TXS_DAY_BO txs_day_bo;
	private EACHSYSSTATUSTAB_BO eachsysstatustab_bo;
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Txs_Day_Form txs_day_form = (Txs_Day_Form) form;
		String ac_key = StrUtils.isEmpty(txs_day_form.getAc_key())?"":txs_day_form.getAc_key();
		String target = StrUtils.isEmpty(txs_day_form.getTarget())?"search":txs_day_form.getTarget();
		Login_Form login_form = (Login_Form) WebServletUtils.getRequest().getSession().getAttribute("login_form");
		txs_day_form.setTarget(target);
		
		System.out.println("TXS_DAY_Action is start >> " + ac_key);
		
		if(ac_key.equalsIgnoreCase("edit")){
		}
		if(ac_key.equalsIgnoreCase("update")){
		}
		if(ac_key.equalsIgnoreCase("add")){
		}
		if(ac_key.equalsIgnoreCase("export")){
			Map map = txs_day_bo.qs_ex_export(
					txs_day_form.getTXDATE(), 
					txs_day_form.getEND_DATE(),
					txs_day_form.getPCODE(),
					txs_day_form.getSENDERACQUIRE(),
					txs_day_form.getBGBK_ID(), 
					txs_day_form.getCLEARINGPHASE(),
					txs_day_form.getRESULTSTATUS(),
					txs_day_form.getSerchStrs(),
					txs_day_form.getSortname(),
					txs_day_form.getSortorder()
			);
			BeanUtils.populate(txs_day_bo, map);
			if(map.get("result").equals("TRUE") && !Arguments.getStringArg("ISWINOPEN").equals("Y")){
				exportFile2Client(response, map.get("msg").toString(), zDateHandler.getDateNum()+"_txs_day.xls" ,txs_day_form.getDow_token() );
				return null;
			}
		}
		if(StrUtils.isEmpty(ac_key)){
			//將營業日塞到頁面的日期控制項
			String busDate = eachsysstatustab_bo.getBusinessDate();
			System.out.println("busDate="+busDate);
			txs_day_form.setTXDATE(busDate);
			
			//銀行端預設帶入操作行
			if(login_form.getUserData().getUSER_TYPE().equals("B")){
				txs_day_form.setSENDERACQUIRE(login_form.getUserData().getUSER_COMPANY());
			}
		}
		//交易代號清單
		txs_day_form.setPcodeList(txs_day_bo.getPcodeList());
		//操作行代號清單
		txs_day_form.setOpbkIdList(txs_day_bo.getOpbkIdList());
		
		target = StrUtils.isEmpty(txs_day_form.getTarget())?"":txs_day_form.getTarget();
		return mapping.findForward(target);
	}
	public TXS_DAY_BO getTxs_day_bo() {
		return txs_day_bo;
	}
	public void setTxs_day_bo(TXS_DAY_BO txs_day_bo) {
		this.txs_day_bo = txs_day_bo;
	}
	public EACHSYSSTATUSTAB_BO getEachsysstatustab_bo() {
		return eachsysstatustab_bo;
	}
	public void setEachsysstatustab_bo(EACHSYSSTATUSTAB_BO eachsysstatustab_bo) {
		this.eachsysstatustab_bo = eachsysstatustab_bo;
	}
}
