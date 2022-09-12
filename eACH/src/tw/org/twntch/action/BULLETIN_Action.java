package tw.org.twntch.action;


import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import tw.org.twntch.bo.BULLETIN_BO;
import tw.org.twntch.form.Bulletin_Form;
import tw.org.twntch.po.BULLETIN;
import tw.org.twntch.util.BeanUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.zDateHandler;

public class BULLETIN_Action extends GenericAction {
	private Logger log = Logger.getLogger(BULLETIN_Action.class.getName());
	private BULLETIN_BO bulletin_bo ;
	
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// TODO Auto-generated method stub
		
		Bulletin_Form  bulletin_form = (Bulletin_Form) form ; 
		String ac_key = StrUtils.isEmpty(bulletin_form.getAc_key())?"search":bulletin_form.getAc_key();
		String target = StrUtils.isEmpty(bulletin_form.getTarget())?"search":bulletin_form.getTarget();
		if(ac_key.equalsIgnoreCase("search")){
		}
		if(ac_key.equalsIgnoreCase("back") ){
			BeanUtils.populate(bulletin_form, JSONUtils.json2map(bulletin_form.getSerchStrs()));
			bulletin_form.setSNO(null);
		}
		if(ac_key.equalsIgnoreCase("add")){
			target = "add_p";
		}
		if(ac_key.equalsIgnoreCase("save")){
			BULLETIN po = new BULLETIN();
			BeanUtils.copyProperties(po, bulletin_form); 
			po.setSNO(null);
			Map<String,String> map =null;
			if(StrUtils.isNotEmpty(po.getSEND_STATUS()) && po.getSEND_STATUS().equals("Y")){
				System.out.println("send>>"+po.getSEND_STATUS());
				map = bulletin_bo.saveNsend(po);
			}else{
				map = bulletin_bo.save(po);
			}
			
			if(map.get("result") != null && map.get("result").equals("TRUE")){
				BeanUtils.populate(bulletin_form, JSONUtils.json2map(bulletin_form.getSerchStrs()));
				bulletin_form.setSNO(null);
			}
			BeanUtils.populate(bulletin_form, map);
		}
		if(ac_key.equalsIgnoreCase("edit")){
			BeanUtils.populate(bulletin_form, JSONUtils.json2map(bulletin_form.getEdit_params()));
			List <BULLETIN> list = bulletin_bo.search(bulletin_form.getSNO(), null, null , "");
			if(list != null){
				for(BULLETIN  po :list ){
					BeanUtils.copyProperties(bulletin_form, po);
				}
			}
			target = "edit_p";
		}
		if(ac_key.equalsIgnoreCase("update")){
			BULLETIN po = new BULLETIN();
			BeanUtils.copyProperties(po, bulletin_form); 
			Map<String,String> map =null;
			if(StrUtils.isNotEmpty(po.getSEND_STATUS()) && po.getSEND_STATUS().equals("Y")){
				System.out.println("send>>"+po.getSEND_STATUS());
				map = bulletin_bo.updateNsend(po);
			}else{
				map = bulletin_bo.update(po);
			}
			if(map.get("result") != null && map.get("result").equals("TRUE")){
				BeanUtils.populate(bulletin_form, JSONUtils.json2map(bulletin_form.getSerchStrs()));
				bulletin_form.setSNO(null);
			}
			BeanUtils.populate(bulletin_form, map);
		}
		if(ac_key.equalsIgnoreCase("delete")){
			Map<String,String> map = bulletin_bo.delete(bulletin_form.getSNO());
			if(map.get("result") != null && map.get("result").equals("TRUE")){
				BeanUtils.populate(bulletin_form, JSONUtils.json2map(bulletin_form.getSerchStrs()));
				bulletin_form.setSNO(null);
			}
			BeanUtils.populate(bulletin_form, map);
		}
		
		if( StrUtils.isEmpty(bulletin_form.getSDATE()) ){
			bulletin_form.setSDATE(zDateHandler.getTWDate());
		}
		if( StrUtils.isEmpty(bulletin_form.getEDATE()) ){
			bulletin_form.setEDATE(zDateHandler.getTWDate());
		}
		bulletin_form.setTarget(target);
		return mapping.findForward(target);
	}

	public BULLETIN_BO getBulletin_bo() {
		return bulletin_bo;
	}

	public void setBulletin_bo(BULLETIN_BO bulletin_bo) {
		this.bulletin_bo = bulletin_bo;
	}

	
	
	
}
