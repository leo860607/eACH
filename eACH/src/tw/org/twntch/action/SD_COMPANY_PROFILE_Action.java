package tw.org.twntch.action;

import java.io.File;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import tw.org.twntch.bo.Arguments;
import tw.org.twntch.bo.EACHSYSSTATUSTAB_BO;
import tw.org.twntch.bo.SD_COMPANY_PROFILE_BO;
import tw.org.twntch.bo.TXN_CODE_BO;
import tw.org.twntch.form.SD_Company_Profile_Form;
import tw.org.twntch.po.SD_COMPANY_PROFILE;
import tw.org.twntch.util.CodeUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.SpringAppCtxHelper;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.WebServerPath;

public class SD_COMPANY_PROFILE_Action extends Action {
	private SD_COMPANY_PROFILE_BO sd_com_bo;
	private TXN_CODE_BO txn_code_bo;
	private EACHSYSSTATUSTAB_BO eachsysstatustab_bo;
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String target = "";
		SD_Company_Profile_Form sd_com_form = (SD_Company_Profile_Form) form;
		String ac_key = StrUtils.isEmpty(sd_com_form.getAc_key())?"":sd_com_form.getAc_key();
		target = StrUtils.isEmpty(sd_com_form.getTarget())?"search":sd_com_form.getTarget();
		sd_com_form.setTarget(target);
		List<SD_COMPANY_PROFILE> list = null;
		System.out.println("SD_Company_Profile_Action is start >> " + ac_key);
		String bizdate = eachsysstatustab_bo.getBusinessDate();
		
		if(ac_key.equalsIgnoreCase("search")|| ac_key.equalsIgnoreCase("back") ){
			System.out.println("SerchStrs>>"+sd_com_form.getSerchStrs());
			BeanUtils.populate(sd_com_form, JSONUtils.json2map(sd_com_form.getSerchStrs()));
//			因SerchStrs中已將中文先encode
			sd_com_form.setCOMPANY_NAME(URLDecoder.decode(sd_com_form.getCOMPANY_NAME(),"UTF-8"));
		}
		if(ac_key.equalsIgnoreCase("save")){
			//	20201012修改  不轉空值
//			//若FEE_TYPE為Ao or Bo 代表是舊的手續費  FEE_TYPE轉成空""
//			if("Ao".equalsIgnoreCase(sd_com_form.getFEE_TYPE())||"Bo".equalsIgnoreCase(sd_com_form.getFEE_TYPE())) {
//				//保留舊資料為空
//				sd_com_form.setFEE_TYPE("");
//			}
			
			 Map map = sd_com_bo.save(sd_com_form.getCOMPANY_ID(),sd_com_form.getTXN_ID() ,sd_com_form.getSND_BRBK_ID(),BeanUtils.describe(sd_com_form));
			 String company_id = sd_com_form.getCOMPANY_ID();
			 String txn_id = sd_com_form.getTXN_ID();
			 String snd_brbk_id = sd_com_form.getSND_BRBK_ID();
			 String fee_type = sd_com_form.getFEE_TYPE();
			 BeanUtils.populate(sd_com_form, map);
			 if(map.get("result").equals("TRUE")){
				BeanUtils.populate(sd_com_form, JSONUtils.json2map(sd_com_form.getSerchStrs()));
//					因SerchStrs中已將中文先encode
				sd_com_form.setCOMPANY_NAME(URLDecoder.decode(sd_com_form.getCOMPANY_NAME(),"UTF-8"));
				// 讓新增成功後返回只顯示新增那筆資料
				sd_com_form.setCOMPANY_ID(company_id);
				sd_com_form.setTXN_ID(txn_id);
				sd_com_form.setSND_BRBK_ID(snd_brbk_id);
				sd_com_form.setFEE_TYPE(fee_type);
			 }
		}
		if(ac_key.equalsIgnoreCase("add")){
			//如果  進新增頁前  下列四個有值的話會被直接帶入到新增的欄位裡 , 故都清空
			sd_com_form.setCOMPANY_ID("");
			sd_com_form.setTXN_ID("");
			sd_com_form.setSND_BRBK_ID("");
			sd_com_form.setCOMPANY_NAME("");
		}
		if(ac_key.equalsIgnoreCase("edit")){
			BeanUtils.populate(sd_com_form, JSONUtils.json2map(sd_com_form.getEdit_params()));
			list = sd_com_bo.searchBySndBrbkId( CodeUtils.escape(sd_com_form.getCOMPANY_ID()) ,CodeUtils.escape(sd_com_form.getTXN_ID()) ,CodeUtils.escape(sd_com_form.getSND_BRBK_ID()),CodeUtils.escape(sd_com_form.getFEE_TYPE()), "");
//			list = sd_com_bo.searchBySndBrbkId(sd_com_form.getCOMPANY_ID(),sd_com_form.getTXN_ID() ,sd_com_form.getSND_BRBK_ID(),sd_com_form.getFEE_TYPE(), "");
			if(list != null && list.size() > 0){
				for(SD_COMPANY_PROFILE po :list){
					BeanUtils.copyProperties(sd_com_form, po);
					if(Integer.parseInt(sd_com_form.getACTIVE_DATE())<Integer.parseInt(bizdate)) {
						sd_com_form.setLOCK_ACTIVE_DATE("Y");
					}else {
						sd_com_form.setLOCK_ACTIVE_DATE("N");
					}
				}
			}
			else{
				sd_com_form.setTarget("search");
				sd_com_form.setAc_key("back");
				sd_com_form.setMsg("無法查詢資料，請確認以下資料是否存在：發動者統一編號、交易代號、發動分行代號");
				sd_com_form.setResult("FALSE");
			}
		}
		if(ac_key.equalsIgnoreCase("update")){
			//這段移到後面update function做
			//若FEE_TYPE為Ao or Bo 代表是舊的手續費  FEE_TYPE轉成空""
//			if("Ao".equalsIgnoreCase(sd_com_form.getFEE_TYPE())||"Bo".equalsIgnoreCase(sd_com_form.getFEE_TYPE())) {
//				sd_com_form.setFEE_TYPE("");
//			}
			
			System.out.println("formMap >> "+BeanUtils.describe(sd_com_form));
			Map map =sd_com_bo.update(sd_com_form.getCOMPANY_ID(),sd_com_form.getTXN_ID() ,sd_com_form.getSND_BRBK_ID(),BeanUtils.describe(sd_com_form),false);
			String company_id=sd_com_form.getCOMPANY_ID();
			String txn_id=sd_com_form.getTXN_ID();
			String snd_brbk_id=sd_com_form.getSND_BRBK_ID();
			String fee_type = sd_com_form.getFEE_TYPE();
			BeanUtils.populate(sd_com_form, map);
			if(map.get("result").equals("TRUE")){
				BeanUtils.populate(sd_com_form, JSONUtils.json2map(sd_com_form.getSerchStrs()));
//					因SerchStrs中已將中文先encode
				sd_com_form.setCOMPANY_NAME(URLDecoder.decode(sd_com_form.getCOMPANY_NAME(),"UTF-8"));
				//讓更新成功後返回只顯示更新那筆資料
				sd_com_form.setCOMPANY_ID(company_id);
				sd_com_form.setTXN_ID(txn_id);
				sd_com_form.setSND_BRBK_ID(snd_brbk_id);
			}
		}
		if(ac_key.equalsIgnoreCase("delete")){
			Map map =sd_com_bo.delete(sd_com_form);
			BeanUtils.populate(sd_com_form, map);
			if(map.get("result").equals("TRUE")){
				BeanUtils.populate(sd_com_form, JSONUtils.json2map(sd_com_form.getSerchStrs()));
	//					因SerchStrs中已將中文先encode
				sd_com_form.setCOMPANY_NAME(URLDecoder.decode(sd_com_form.getCOMPANY_NAME(),"UTF-8"));
				//防止刪除完搜尋全部資料
				sd_com_form.setAc_key("");
			}
		}
		if(ac_key.equalsIgnoreCase("import_data")){
			List<String> result = new ArrayList<String>();
			boolean fileUpload = false;
			
			//暫存檔路徑
			WebServerPath webserverpath = (WebServerPath)  SpringAppCtxHelper.getBean("webserverpath");
//			windowdir=eACH/tmp/
			String filePath = webserverpath.getServerRootUrl()+"/"+Arguments.getStringArg("RPT.PDF.PATH")+ "/";
			String file = filePath+sd_com_form.getFILE().getFileName();
			System.out.println("file:" + file);
			
			File folder = new File(filePath);
		    if(!folder.exists()){
		    	folder.mkdir();
		    }
			
			System.out.println("import in");

			String fileName = sd_com_form.getFILE().getFileName();
			
			System.out.println("import filename:" + fileName);
			System.out.println(fileName.contains(".xls"));
			if(fileName.contains(".xls") && !fileName.contains(".xlsx")){
				//將檔案暫存至 eACH/tmp
				fileUpload = sd_com_bo.addTmpFile(sd_com_form,filePath);
				
				if(fileUpload == true){
					//檢核文件內容是否符合格式
					result = sd_com_bo.validate(file); 
				
				    if(result.size() == 0){
				    	//匯入資料庫
					    result = sd_com_bo.import_db(file);
					    String msg = result.get(0);
						String msg2 = "";
					    if(result.size()>1) {
					    	for (int i = 1; i < result.size(); i++) {
								if(i==(result.size()-1)) {
									msg2 += result.get(i);
								}else {
									msg2 += result.get(i) + ",\\n";
								}
							}
					    	sd_com_form.setMsg("匯入資料成功：\\n" + msg +"\\n"+"請注意彈出視窗");
					    	// 返回文件檢查錯誤
							sd_com_form.setError_list(msg2);
					    }else{
					    	sd_com_form.setMsg("匯入資料成功：\\n"+msg);
					    }
				    
					    //刪除暫存檔案
					    sd_com_bo.deleteTmpFile(file);
				    }else{
					    String msg = "";
					    for(int i=0; i<result.size(); i++){
						    msg +=result.get(i)+",";
					    }
					    System.out.println(msg);
					    
					    //返回文件檢查錯誤
					    sd_com_form.setError_list(msg);
					    
					    //刪除暫存檔案
					    sd_com_bo.deleteTmpFile(file);
				    }
				    
				}else{
					sd_com_form.setMsg("檔案匯入錯誤");
				}
				
			}else{
				sd_com_form.setMsg("檔案格式錯誤");
			}
			System.out.println("import_data.SerchStrs>>"+sd_com_form.getSerchStrs());
			BeanUtils.populate(sd_com_form, JSONUtils.json2map(sd_com_form.getSerchStrs()));
//			因SerchStrs中已將中文先encode
			sd_com_form.setCOMPANY_NAME(URLDecoder.decode(sd_com_form.getCOMPANY_NAME(),"UTF-8"));
		}
		target = StrUtils.isEmpty(sd_com_form.getTarget())?"":sd_com_form.getTarget();
		sd_com_form.setBIZDATE(bizdate);
//		下拉清單
//		sd_com_form.setTxnIdList(txn_code_bo.getIdListByTxnTypeII("SD"));
		//20201006修改 不過濾930-990
		sd_com_form.setTxnIdList(txn_code_bo.getIdListByTxnType("SD"));
		sd_com_form.setBgbkIdList(sd_com_bo.getSdBgbkIdList());
		
		return mapping.findForward(target);
	}
	public SD_COMPANY_PROFILE_BO getSd_com_bo() {
		return sd_com_bo;
	}
	public void setSd_com_bo(SD_COMPANY_PROFILE_BO sd_com_bo) {
		this.sd_com_bo = sd_com_bo;
	}
	public TXN_CODE_BO getTxn_code_bo() {
		return txn_code_bo;
	}
	public void setTxn_code_bo(TXN_CODE_BO txn_code_bo) {
		this.txn_code_bo = txn_code_bo;
	}
	public EACHSYSSTATUSTAB_BO getEachsysstatustab_bo() {
		return eachsysstatustab_bo;
	}
	public void setEachsysstatustab_bo(EACHSYSSTATUSTAB_BO eachsysstatustab_bo) {
		this.eachsysstatustab_bo = eachsysstatustab_bo;
	}
}
