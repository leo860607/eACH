package tw.org.twntch.action;


import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import tw.org.twntch.bo.BANK_GROUP_BO;
import tw.org.twntch.bo.EACH_USERLOG_BO;
import tw.org.twntch.bo.FEE_ADJ_BO;
import tw.org.twntch.form.Fee_Adj_Form;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.zDateHandler;


public class FEE_ADJ_Action extends GenericAction {
	private FEE_ADJ_BO fee_adj_bo;
	private BANK_GROUP_BO bank_group_bo;
	private EACH_USERLOG_BO userlog_bo;//寫操作軌跡記錄
	
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		Fee_Adj_Form fee_adj_form = (Fee_Adj_Form) form ;
		String target = StrUtils.isEmpty(fee_adj_form.getTarget())?"search":fee_adj_form.getTarget();
		String ac_key = StrUtils.isEmpty(fee_adj_form.getAc_key())?"":fee_adj_form.getAc_key();
		System.out.println("FEE_ADJ_Action is start >> " + ac_key);
		if(StrUtils.isNotEmpty(ac_key)){
			if("add".equals(ac_key)){
				//fee_adj_form.setBgbkIdList(bank_group_bo.getBgbkIdList());
				String date = DateTimeUtils.convertDate(DateTimeUtils.NOT_INTERCONVERSION, zDateHandler.getTheDate(), "yyyy/MM/dd", "yyyyMMdd");
				date = DateTimeUtils.addDateCount(date, Calendar.MONTH, "1", "-");
				date = DateTimeUtils.convertDate(date, "yyyyMMdd", "yyyyMM");
				fee_adj_form.setYYYYMM(date);
			}else if("save".equals(ac_key)){
				Map rtnMap = fee_adj_bo.save(BeanUtils.describe(fee_adj_form));
				BeanUtils.copyProperties(fee_adj_form, rtnMap);
			}else if("edit".equals(ac_key)){
				BeanUtils.copyProperties(fee_adj_form, fee_adj_bo.searchByPK(fee_adj_form.getYYYYMM(), fee_adj_form.getBRBK_ID()));
			}else if("delete".equals(ac_key)){
				Map rtnMap = fee_adj_bo.delete(fee_adj_form.getYYYYMM(), fee_adj_form.getBRBK_ID());
				BeanUtils.copyProperties(fee_adj_form, rtnMap);
			}else if("update".equals(ac_key)){
				Map rtnMap = fee_adj_bo.update(BeanUtils.describe(fee_adj_form));
				BeanUtils.copyProperties(fee_adj_form, rtnMap);
			}else if("publish".equals(ac_key)){
				Map rtnMap = fee_adj_bo.publish(fee_adj_form.getYYYYMM());
				BeanUtils.copyProperties(fee_adj_form, rtnMap);
				
				//將頁面上的發布條件放進pkMap
				Map<String,Object> pkMap = new HashMap<String,Object>();
				pkMap.put("serchStrs",fee_adj_form.getSerchStrs());
				//如果有錯誤要將訊息放進去
				Map<String,Object> msgMap = new HashMap<String,Object>();
				//發布成功
				if("TRUE".equals(rtnMap.get("result"))){
					//寫操作軌跡記錄(成功)
					userlog_bo.writeLog("U",null,null,pkMap);
				}
				//發布失敗
				else{
					//寫操作軌跡記錄(失敗)
					msgMap.put("msg",rtnMap.get("msg"));
					userlog_bo.writeFailLog("U",msgMap,null,null,pkMap);
				}
			}
		}else{
			//預設帶入上個月
			String now = zDateHandler.getTWDate();
			int year = Integer.valueOf(now.substring(0,4));
			int month = Integer.valueOf(now.substring(4,6)) - 1;
			if(month <= 0){month = 12;year--;}
			String newDate = "0" + year + (month<10?"0"+month:month);
			fee_adj_form.setYYYYMM(DateTimeUtils.convertDate(DateTimeUtils.NOT_INTERCONVERSION, newDate, "yyyyMM", "yyyyMM"));
		}
		target = StrUtils.isEmpty(fee_adj_form.getTarget())?"search":fee_adj_form.getTarget();
		System.out.println("forward to>>" + target);
		return (mapping.findForward(target));
	}
	public FEE_ADJ_BO getFee_adj_bo() {
		return fee_adj_bo;
	}
	public void setFee_adj_bo(FEE_ADJ_BO fee_adj_bo) {
		this.fee_adj_bo = fee_adj_bo;
	}
	
	public BANK_GROUP_BO getBank_group_bo() {
		return bank_group_bo;
	}
	public void setBank_group_bo(BANK_GROUP_BO bank_group_bo) {
		this.bank_group_bo = bank_group_bo;
	}
	public EACH_USERLOG_BO getUserlog_bo() {
		return userlog_bo;
	}
	public void setUserlog_bo(EACH_USERLOG_BO userlog_bo) {
		this.userlog_bo = userlog_bo;
	}
	
}
