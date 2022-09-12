package tw.org.twntch.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import tw.org.twntch.bo.WK_DATE_BO;
import tw.org.twntch.form.Wk_Date_Form;
import tw.org.twntch.po.WK_DATE_CALENDAR;
import tw.org.twntch.util.BeanUtils;
import tw.org.twntch.util.StrUtils;

public class WK_DATE_Action extends Action {
	private WK_DATE_BO wk_date_bo;
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Wk_Date_Form wk_date_form = (Wk_Date_Form) form;
		String ac_key = StrUtils.isEmpty(wk_date_form.getAc_key())?"":wk_date_form.getAc_key();
		String target = StrUtils.isEmpty(wk_date_form.getTarget())?"search":wk_date_form.getTarget();
		wk_date_form.setTarget(target);
		System.out.println("WK_DATE_Action is start >> " + ac_key);
		List<WK_DATE_CALENDAR> list = null;
		if(ac_key.equalsIgnoreCase("edit")){
			WK_DATE_CALENDAR po = null;
			po = wk_date_bo.search(wk_date_form.getTXN_DATE());
			BeanUtils.copyProperties(wk_date_form, po);
		}
		if(ac_key.equalsIgnoreCase("update")){
			WK_DATE_CALENDAR po = new WK_DATE_CALENDAR();
			BeanUtils.copyProperties(po, wk_date_form);
			po.setIS_TXN_DATE(wk_date_form.getIS_TXN_DATE_VAL());
			Map map = wk_date_bo.update(po);
			BeanUtils.populate(wk_date_form, map);
			wk_date_form.setTW_YEAR(po.getTXN_DATE().substring(0,4));
			wk_date_form.setTW_MONTH(po.getTXN_DATE().substring(4,6));
		}
		if(ac_key.equalsIgnoreCase("add")){
			//產生全年營業日
			Map map = wk_date_bo.createWholeYearData(wk_date_form.getTW_YEAR());
			BeanUtils.populate(wk_date_form, map);
			//產生完後直接查詢全年資料
		}
		target = StrUtils.isEmpty(wk_date_form.getTarget())?"":wk_date_form.getTarget();
		return mapping.findForward(target);
	}
	public WK_DATE_BO getWk_date_bo() {
		return wk_date_bo;
	}
	public void setWk_date_bo(WK_DATE_BO wk_date_bo) {
		this.wk_date_bo = wk_date_bo;
	}
}
