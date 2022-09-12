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
import tw.org.twntch.bo.RPTTX_1_BO;
import tw.org.twntch.bo.TXN_CODE_BO;
import tw.org.twntch.db.dao.hibernate.BANK_GROUP_Dao;
import tw.org.twntch.form.Login_Form;
import tw.org.twntch.form.Rpttx_1_Form;
import tw.org.twntch.po.BANK_GROUP;
import tw.org.twntch.util.BeanUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.WebServletUtils;
import tw.org.twntch.util.zDateHandler;

public class RPTTX_1_Action extends GenericAction {
	private RPTTX_1_BO rpttx_1_bo ;
	private BANK_GROUP_BO bank_group_bo ; 
	private BANK_GROUP_Dao bank_group_Dao ;
	private TXN_CODE_BO txn_code_bo ;
	private EACHSYSSTATUSTAB_BO eachsysstatustab_bo ;
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Rpttx_1_Form rpttx_1_form = (Rpttx_1_Form) form;
		String ac_key = StrUtils.isEmpty(rpttx_1_form.getAc_key())?"":rpttx_1_form.getAc_key();
		String target = StrUtils.isEmpty(rpttx_1_form.getTarget())?"search":rpttx_1_form.getTarget();
		rpttx_1_form.setTarget(target);
		Map map = null ;
		System.out.println("RPTTX_1_Action is start >> " + ac_key);
		Login_Form login_form = (Login_Form) WebServletUtils.getRequest().getSession().getAttribute("login_form");
		if(login_form.getUserData().getUSER_TYPE().equals("B")){
			BANK_GROUP po =  bank_group_Dao.get(login_form.getUserData().getUSER_COMPANY());
			rpttx_1_form.setOpt_bank(login_form.getUserData().getUSER_COMPANY()+"-"+po.getBGBK_NAME());
			rpttx_1_form.setOpt_id(login_form.getUserData().getUSER_COMPANY());
			rpttx_1_form.setBgbkIdList(bank_group_bo.getCurBgbkListByOP(login_form.getUserData().getUSER_COMPANY() , eachsysstatustab_bo.getRptBusinessDate()) );
		}
		
		if(ac_key.startsWith("search")){
			String ext = ac_key.split("_")[1];
//			銀行端要多加一道檢核
			if(login_form.getUserData().getUSER_TYPE().equals("B")){
				Map retmap = super.checkRPT_BizDate(rpttx_1_form.getBIZDATE(), rpttx_1_form.getCLEARINGPHASE());
				if(retmap.get("result").equals("FALSE") ){
					BeanUtils.populate(rpttx_1_form, retmap);
					return mapping.findForward(target);
				}
			}
			if(ext.equals("pdf")) {
				map = rpttx_1_bo.export(rpttx_1_form.getBIZDATE(),rpttx_1_form.getOpt_id() , rpttx_1_form.getBGBK_ID(), rpttx_1_form.getCLEARINGPHASE() ,rpttx_1_form.getOpt_bank(),rpttx_1_form.getOpt_type(),rpttx_1_form.getSENDERID(),rpttx_1_form.getTXN_ID() ,rpttx_1_form.getRESP(), rpttx_1_form.getSerchStrs(),1);
			}else if(ext.equals("xls")){
				map = rpttx_1_bo.export(rpttx_1_form.getBIZDATE(),rpttx_1_form.getOpt_id() , rpttx_1_form.getBGBK_ID(), rpttx_1_form.getCLEARINGPHASE() ,rpttx_1_form.getOpt_bank(),rpttx_1_form.getOpt_type(),rpttx_1_form.getSENDERID(),rpttx_1_form.getTXN_ID() ,rpttx_1_form.getRESP(), rpttx_1_form.getSerchStrs(),2);
			}
			
			BeanUtils.populate(rpttx_1_form, map);
			if(map.get("result").equals("TRUE") && !Arguments.getStringArg("ISWINOPEN").equals("Y")){
				exportFile2Client(response, map.get("msg").toString(), zDateHandler.getDateNum()+"_tx_1."+ext ,rpttx_1_form.getDow_token() );
				return null;
			}

			
		}
		if(StrUtils.isEmpty(ac_key)){
		}
		rpttx_1_form.setBIZDATE(eachsysstatustab_bo.getRptBusinessDate()); 
//		rpttx_1_form.setOpt_bankList(rpttx_1_bo.getBgbkIdList());
		rpttx_1_form.setOpt_bankList(bank_group_bo.getOpbkList());
		rpttx_1_form.setIdList(txn_code_bo.getIdList());
		System.out.println("txdt>>"+rpttx_1_form.getTXDT());
		target = StrUtils.isEmpty(rpttx_1_form.getTarget())?"":rpttx_1_form.getTarget();
		return mapping.findForward(target);
	}
	
//	
//	public void exportFile2Client(HttpServletResponse response , String fullFilePath,String filename ,String dow_token ){
//		filename = StrUtils.isEmpty(filename)?zDateHandler.getDateNum()+"_tx_1.pdf":filename;
//		//return an application file instead of html page
//	     response.setContentType("application/octet-stream");
//	     response.setHeader("Content-Disposition","attachment;filename="+filename);
//	     response.addCookie(new Cookie("fileDownloadToken", dow_token));
////	     response.addCookie(new HttpCookie("fileDownloadToken", dow_token));
//	     try 
//	     {
//	       	//Get it from file system
//	       	FileInputStream in = 
//	      		new FileInputStream(new File(fullFilePath));
//	 
//	        //Get it from web path
//	       	//http://localhost:8080/eACH/
//	        ServletOutputStream out = response.getOutputStream();
//	        byte[] outputByte = new byte[4096];
//	        //copy binary content to output stream
//	        while(in.read(outputByte, 0, 4096) != -1){
//	        	out.write(outputByte, 0, 4096);
//	        }
//	        in.close();
//	        out.flush();
//	        out.close();
////	 TODO 要把檔案刪除 未完成
//	     }catch(Exception e){
//	    	e.printStackTrace();
//	   }
//	}
	public RPTTX_1_BO getRpttx_1_bo() {
		return rpttx_1_bo;
	}
	public void setRpttx_1_bo(RPTTX_1_BO rpttx_1_bo) {
		this.rpttx_1_bo = rpttx_1_bo;
	}

	public BANK_GROUP_Dao getBank_group_Dao() {
		return bank_group_Dao;
	}

	public void setBank_group_Dao(BANK_GROUP_Dao bank_group_Dao) {
		this.bank_group_Dao = bank_group_Dao;
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
