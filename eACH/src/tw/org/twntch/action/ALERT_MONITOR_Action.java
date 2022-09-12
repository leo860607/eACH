package tw.org.twntch.action;


import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import tw.org.twntch.bo.SYS_PARA_BO;
import tw.org.twntch.form.Alert_Monitor_Form;
import tw.org.twntch.form.Login_Form;
import tw.org.twntch.po.SYS_PARA;
import tw.org.twntch.util.BeanUtils;
import tw.org.twntch.util.QuartzUtil;
import tw.org.twntch.util.StrUtils;

public class ALERT_MONITOR_Action extends Action {
	private SYS_PARA_BO sys_para_bo;
	
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Alert_Monitor_Form alert_monitor_form = (Alert_Monitor_Form) form;
		String ac_key = StrUtils.isEmpty(alert_monitor_form.getAc_key())?"":alert_monitor_form.getAc_key();
		String target = StrUtils.isEmpty(alert_monitor_form.getTarget())?"search":alert_monitor_form.getTarget();
		alert_monitor_form.setTarget(target);
		Login_Form login_form = (Login_Form) request.getSession().getAttribute("login_form");
		Map map = new HashMap<>();
		System.out.println("ALERT_MONITOR_Action is start >> " + ac_key);
		
		if(ac_key.equalsIgnoreCase("edit")){
		}
		if(ac_key.equalsIgnoreCase("update")){
		}
		if(ac_key.equalsIgnoreCase("add")){
		}
		if(ac_key.equalsIgnoreCase("monitor_set")){
			SYS_PARA sys_para = sys_para_bo.searchII();
			try {
				if(!alert_monitor_form.getMONITOR_AMOUNT().equals(sys_para.getMONITOR_AMOUNT())
						|| !alert_monitor_form.getMONITOR_AMOUNT_PERIOD().equals(sys_para.getMONITOR_AMOUNT_PERIOD())){
					QuartzUtil.modifyJobTime("大額監控排程", alert_monitor_form.getMONITOR_AMOUNT_PERIOD());
					sys_para.setMONITOR_AMOUNT(alert_monitor_form.getMONITOR_AMOUNT());
					sys_para.setMONITOR_AMOUNT_PERIOD(alert_monitor_form.getMONITOR_AMOUNT_PERIOD());
					System.out.println("大額監控排程 更新");
				}
				if(!alert_monitor_form.getMONITOR_PENDING().equals(sys_para.getMONITOR_PENDING())
						|| !alert_monitor_form.getMONITOR_PENDING_PERIOD().equals(sys_para.getMONITOR_PENDING_PERIOD())){
					QuartzUtil.modifyJobTime("逾時監控排程", alert_monitor_form.getMONITOR_PENDING_PERIOD());
					sys_para.setMONITOR_PENDING(alert_monitor_form.getMONITOR_PENDING());
					sys_para.setMONITOR_PENDING_PERIOD(alert_monitor_form.getMONITOR_PENDING_PERIOD());
					System.out.println("逾時監控排程 更新");
				}
				sys_para.setMONITOR_MAILRECEIVER(alert_monitor_form.getMONITOR_MAILRECEIVER());
			}catch (Exception e) {
				System.out.println("Update Quartz Error");
			}
			
			
			try {
				sys_para_bo.getSys_para_Dao().update(sys_para);
				map.put("result", "TRUE");
				map.put("msg", "儲存成功");
			}catch (Exception e) {
				System.out.println("Update sys_para Error");
			}
			BeanUtils.populate(alert_monitor_form, map);
		}
		if(StrUtils.isEmpty(ac_key)){
			
			SYS_PARA sys_para = sys_para_bo.searchII();
			alert_monitor_form.setMONITOR_AMOUNT(sys_para.getMONITOR_AMOUNT());
			alert_monitor_form.setMONITOR_AMOUNT_PERIOD(sys_para.getMONITOR_AMOUNT_PERIOD());
			alert_monitor_form.setMONITOR_PENDING(sys_para.getMONITOR_PENDING());
			alert_monitor_form.setMONITOR_PENDING_PERIOD(sys_para.getMONITOR_PENDING_PERIOD());
			alert_monitor_form.setMONITOR_MAILRECEIVER(sys_para.getMONITOR_MAILRECEIVER());
		}
		target = StrUtils.isEmpty(alert_monitor_form.getTarget())?"":alert_monitor_form.getTarget();
		return mapping.findForward(target);
	}

	public SYS_PARA_BO getSys_para_bo() {
		return sys_para_bo;
	}

	public void setSys_para_bo(SYS_PARA_BO sys_para_bo) {
		this.sys_para_bo = sys_para_bo;
	}
}
