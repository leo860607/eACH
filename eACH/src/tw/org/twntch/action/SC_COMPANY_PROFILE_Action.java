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
import tw.org.twntch.bo.SC_COMPANY_PROFILE_BO;
import tw.org.twntch.bo.TXN_CODE_BO;
import tw.org.twntch.form.SC_Company_Profile_Form;
import tw.org.twntch.po.SC_COMPANY_PROFILE;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.SpringAppCtxHelper;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.WebServerPath;

public class SC_COMPANY_PROFILE_Action extends Action {
	private SC_COMPANY_PROFILE_BO sc_com_bo;
	private TXN_CODE_BO txn_code_bo;
	private EACHSYSSTATUSTAB_BO eachsysstatustab_bo;

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String target = "";
		SC_Company_Profile_Form sc_com_form = (SC_Company_Profile_Form) form;
		String ac_key = StrUtils.isEmpty(sc_com_form.getAc_key()) ? "" : sc_com_form.getAc_key();
		target = StrUtils.isEmpty(sc_com_form.getTarget()) ? "search" : sc_com_form.getTarget();
		sc_com_form.setTarget(target);
		List<SC_COMPANY_PROFILE> list = null;
		System.out.println("SC_COMPANY_PROFILE_Action is start >> " + ac_key);

		String busDate = eachsysstatustab_bo.getRptBusinessDate();
		sc_com_form.setBUSSINESS_DATE(busDate);
		if (ac_key.equalsIgnoreCase("")) {

		}

		if (ac_key.equalsIgnoreCase("search") || ac_key.equalsIgnoreCase("back")) {
			System.out.println("SerchStrs>>" + sc_com_form.getSerchStrs());
			BeanUtils.populate(sc_com_form, JSONUtils.json2map(sc_com_form.getSerchStrs()));
			// 因SerchStrs中已將中文先encode
			sc_com_form.setCOMPANY_NAME(URLDecoder.decode(sc_com_form.getCOMPANY_NAME(), "UTF-8"));
		}
		if (ac_key.equalsIgnoreCase("save")) {
			//20201012修改 不轉空值
			//若FEE_TYPE為Ao or Bo 代表是舊的手續費  FEE_TYPE轉成空""
//			if("Ao".equalsIgnoreCase(sc_com_form.getFEE_TYPE())||"Bo".equalsIgnoreCase(sc_com_form.getFEE_TYPE())) {
//				sc_com_form.setFEE_TYPE("");
//			}
			Map map = sc_com_bo.save(sc_com_form.getCOMPANY_ID(), sc_com_form.getTXN_ID(), sc_com_form.getSND_BRBK_ID(),
					BeanUtils.describe(sc_com_form));
			String company_id = sc_com_form.getCOMPANY_ID();
			String txn_id = sc_com_form.getTXN_ID();
			String snd_brbk_id = sc_com_form.getSND_BRBK_ID();
			String fee_type = sc_com_form.getFEE_TYPE();
			BeanUtils.populate(sc_com_form, map);
			if (map.get("result").equals("TRUE")) {
				BeanUtils.populate(sc_com_form, JSONUtils.json2map(sc_com_form.getSerchStrs()));
				// 因SerchStrs中已將中文先encode
				System.out.println("SerchStrs>>" + sc_com_form.getSerchStrs());
				BeanUtils.populate(sc_com_form, JSONUtils.json2map(sc_com_form.getSerchStrs()));
				sc_com_form.setCOMPANY_NAME(URLDecoder.decode(sc_com_form.getCOMPANY_NAME(), "UTF-8"));
				// 讓新增成功後返回只顯示新增那筆資料
				sc_com_form.setCOMPANY_ID(company_id);
				sc_com_form.setTXN_ID(txn_id);
				sc_com_form.setSND_BRBK_ID(snd_brbk_id);
				sc_com_form.setFEE_TYPE(fee_type);
			}
		}
		if (ac_key.equalsIgnoreCase("add")) {
		}
		if (ac_key.equalsIgnoreCase("edit")) {
			BeanUtils.populate(sc_com_form, JSONUtils.json2map(sc_com_form.getEdit_params()));
			list = sc_com_bo.searchBySndBrbkId(sc_com_form.getCOMPANY_ID(), sc_com_form.getTXN_ID(),
					sc_com_form.getSND_BRBK_ID(),sc_com_form.getFEE_TYPE(), "");
			if (list != null && list.size() > 0) {
				for (SC_COMPANY_PROFILE po : list) {
					BeanUtils.copyProperties(sc_com_form, po);
				}
			} else {
				sc_com_form.setAc_key("back");
				sc_com_form.setTarget("search");
				sc_com_form.setMsg("無法查詢資料，請確認以下資料是否存在：發動者統一編號、交易代號、發動分行代號");
				sc_com_form.setResult("FALSE");
			}
		}
		if (ac_key.equalsIgnoreCase("update")) {
			//這段移到後面update function做
			//若FEE_TYPE為Ao or Bo 代表是舊的手續費  FEE_TYPE轉成空""
//			if("Ao".equalsIgnoreCase(sc_com_form.getFEE_TYPE())||"Bo".equalsIgnoreCase(sc_com_form.getFEE_TYPE())) {
//				sc_com_form.setFEE_TYPE("");
//			}
			System.out.println("formMap >> "+BeanUtils.describe(sc_com_form));
			Map map = sc_com_bo.update(sc_com_form.getCOMPANY_ID(), sc_com_form.getTXN_ID(),
					sc_com_form.getSND_BRBK_ID(), BeanUtils.describe(sc_com_form),false);
			String company_id = sc_com_form.getCOMPANY_ID();
			String txn_id = sc_com_form.getTXN_ID();
			String snd_brbk_id = sc_com_form.getSND_BRBK_ID();
			String fee_type = sc_com_form.getFEE_TYPE();

			BeanUtils.populate(sc_com_form, map);
			if (map.get("result").equals("TRUE")) {
				BeanUtils.populate(sc_com_form, JSONUtils.json2map(sc_com_form.getSerchStrs()));
				// 因SerchStrs中已將中文先encode
				sc_com_form.setCOMPANY_NAME(URLDecoder.decode(sc_com_form.getCOMPANY_NAME(), "UTF-8"));
				BeanUtils.populate(sc_com_form, JSONUtils.json2map(sc_com_form.getSerchStrs()));
				// 讓更新成功後返回只顯示更新那筆資料
				sc_com_form.setCOMPANY_ID(company_id);
				sc_com_form.setTXN_ID(txn_id);
				sc_com_form.setSND_BRBK_ID(snd_brbk_id);
				sc_com_form.setFEE_TYPE(fee_type);
			}
		}
		if (ac_key.equalsIgnoreCase("delete")) {
			Map map = sc_com_bo.delete(sc_com_form);
			BeanUtils.populate(sc_com_form, map);
			if (map.get("result").equals("TRUE")) {
				BeanUtils.populate(sc_com_form, JSONUtils.json2map(sc_com_form.getSerchStrs()));
				// 因SerchStrs中已將中文先encode
				sc_com_form.setCOMPANY_NAME(URLDecoder.decode(sc_com_form.getCOMPANY_NAME(), "UTF-8"));
				BeanUtils.populate(sc_com_form, JSONUtils.json2map(sc_com_form.getSerchStrs()));
				//防止刪除完搜尋全部資料
				sc_com_form.setAc_key("");
			}
		}

		if (ac_key.equalsIgnoreCase("import_data")) {
			List<String> result = new ArrayList<String>();
			boolean fileUpload = false;

			// 暫存檔路徑
			WebServerPath webserverpath = (WebServerPath) SpringAppCtxHelper.getBean("webserverpath");
			// windowdir=eACH/tmp/
			String filePath = webserverpath.getServerRootUrl() + "/" + Arguments.getStringArg("RPT.PDF.PATH") + "/";
			String file = filePath + sc_com_form.getFILE().getFileName();

			File folder = new File(filePath);
			if (!folder.exists()) {
				folder.mkdir();
			}

			System.out.println("import in");

			String fileName = sc_com_form.getFILE().getFileName();

			if (fileName.contains(".xls") && !fileName.contains(".xlsx")) {
				// 將檔案暫存至 eACH/tmp
				fileUpload = sc_com_bo.addTmpFile(sc_com_form, filePath);

				
				if (fileUpload == true) {
					// 檢核文件內容是否符合格式
					result = sc_com_bo.validate(file);
					if (result.size() == 0) {
						// 匯入資料庫
						result = sc_com_bo.import_db(file);
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
							sc_com_form.setMsg("匯入資料成功：\\n" + msg +"\\n"+"請注意彈出視窗");
							// 返回文件檢查錯誤
							sc_com_form.setError_list(msg2);
						}else{
							sc_com_form.setMsg("匯入資料成功：\\n" + msg);
						}

						// 刪除暫存檔案
						sc_com_bo.deleteTmpFile(file);
					} else {
						String msg = "";
						for (int i = 0; i < result.size(); i++) {
							msg += result.get(i) + ",";
						}
						System.out.println(msg);

						// 返回文件檢查錯誤
						sc_com_form.setError_list(msg);

						// 刪除暫存檔案
						sc_com_bo.deleteTmpFile(file);
					}

				} else {
					sc_com_form.setMsg("檔案匯入錯誤");
				}

			} else {
				sc_com_form.setMsg("檔案格式錯誤");
			}
			System.out.println("import_data.SerchStrs>>" + sc_com_form.getSerchStrs());
			BeanUtils.populate(sc_com_form, JSONUtils.json2map(sc_com_form.getSerchStrs()));
			// 因SerchStrs中已將中文先encode
			sc_com_form.setCOMPANY_NAME(URLDecoder.decode(sc_com_form.getCOMPANY_NAME(), "UTF-8"));
		}
		target = StrUtils.isEmpty(sc_com_form.getTarget()) ? "" : sc_com_form.getTarget();
		sc_com_form.setBIZDATE(eachsysstatustab_bo.getBusinessDate());
		// 下拉清單
		sc_com_form.setTxnIdList(txn_code_bo.getIdListByTxnType("SC"));
		sc_com_form.setBgbkIdList(sc_com_bo.getScBgbkIdList());
		
		return mapping.findForward(target);
	}

	public SC_COMPANY_PROFILE_BO getSc_com_bo() {
		return sc_com_bo;
	}

	public void setSc_com_bo(SC_COMPANY_PROFILE_BO sc_com_bo) {
		this.sc_com_bo = sc_com_bo;
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
