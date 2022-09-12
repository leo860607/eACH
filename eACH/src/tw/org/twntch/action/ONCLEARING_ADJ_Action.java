package tw.org.twntch.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import tw.org.twntch.bo.ONCLEARING_ADJ_BO;
import tw.org.twntch.form.Onclearing_Adj_Form;
import tw.org.twntch.form.Opc_Trans_Form;
import tw.org.twntch.util.BeanUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.StrUtils;

public class ONCLEARING_ADJ_Action extends GenericAction {
 private Logger logger = Logger.getLogger(ONCLEARING_ADJ_Action.class.getName());
 private ONCLEARING_ADJ_BO onclearing_adj_bo ;
 
 
 
@Override
public ActionForward execute(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
	// TODO Auto-generated method stub
	Onclearing_Adj_Form onclearing_adj_form = (Onclearing_Adj_Form) form;
	String ac_key = StrUtils.isEmpty(onclearing_adj_form.getAc_key())?"":onclearing_adj_form.getAc_key();
	String target = StrUtils.isEmpty(onclearing_adj_form.getTarget())?"search":onclearing_adj_form.getTarget();
	if(ac_key.equalsIgnoreCase("edit")){
		System.out.println("Edit_params>>"+onclearing_adj_form.getEdit_params());
		System.out.println("Edit_params2>>"+JSONUtils.json2map(onclearing_adj_form.getEdit_params()));
		BeanUtils.populate(onclearing_adj_form, JSONUtils.json2map(onclearing_adj_form.getEdit_params()));
		Map<String, String> map = onclearing_adj_bo.getOne(onclearing_adj_form.getBIZDATE(), onclearing_adj_form.getCLEARINGPHASE(), onclearing_adj_form.getBANKID());
		onclearing_adj_form.setMap_BH(map);
		System.out.println("bh>>"+onclearing_adj_form.getMap_BH());
	}
	if(ac_key.equalsIgnoreCase("update")){
		
	}
	if(ac_key.equalsIgnoreCase("back")){
	  BeanUtils.populate(onclearing_adj_form, JSONUtils.json2map(onclearing_adj_form.getSerchStrs()) ); ;
	}
	if(StrUtils.isEmpty(ac_key)){
		onclearing_adj_form.setBIZDATE(onclearing_adj_bo.getPreBizdate());
	}
	return mapping.findForward(target);
}
public ONCLEARING_ADJ_BO getOnclearing_adj_bo() {
	return onclearing_adj_bo;
}
public void setOnclearing_adj_bo(ONCLEARING_ADJ_BO onclearing_adj_bo) {
	this.onclearing_adj_bo = onclearing_adj_bo;
}
 
 
 
 
 
}
