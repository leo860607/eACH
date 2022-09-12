package tw.org.twntch.action;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import tw.org.twntch.bo.BASEDATA_DOWNLOAD_BO;
import tw.org.twntch.bo.EACH_USERLOG_BO;
import tw.org.twntch.form.Basedata_Download_Form;
import tw.org.twntch.form.Login_Form;
import tw.org.twntch.util.CodeUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.WebServletUtils;

public class BASEDATA_DOWNLOAD_Action extends Action {
	private BASEDATA_DOWNLOAD_BO basedata_download_bo;
	private CodeUtils codeUtils;
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private EACH_USERLOG_BO userlog_bo;// 寫操作軌跡記錄

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		Basedata_Download_Form basedata_download_form = (Basedata_Download_Form) form;
		String ac_key = StrUtils.isEmpty(basedata_download_form.getAc_key()) ? "" : basedata_download_form.getAc_key();
		String enc_type = StrUtils.isEmpty(basedata_download_form.getEncType()) ? "" : basedata_download_form.getEncType();
		String target = StrUtils.isEmpty(basedata_download_form.getTarget()) ? "search"
				: basedata_download_form.getTarget();
		basedata_download_form.setTarget(target);
		logger.debug("ac_key=" + ac_key);
		logger.debug("enc_type=" + enc_type);
		logger.debug("target=" + target);
		Login_Form login_form = (Login_Form) WebServletUtils.getRequest().getSession().getAttribute("login_form");
		try {
			String user_type = login_form.getUserData().getUSER_TYPE();
			// 取得在classpath下的properties檔案
			Map<String, String> valueMap = codeUtils.getPropertyValue("Configuration.properties", "basedataDirPath","basedataFilePrefix","basedataFilePrefix_C","basedataFilePrefix_UTF8","basedataFilePrefix_C_UTF8");
			// 無法取得properties
			if (valueMap == null) {
				// 回頁面alert訊息
				basedata_download_form.setMsg("無法取得properties檔");
				return mapping.findForward(target);
			}

			// 產生檔案
			if (ac_key.equalsIgnoreCase("create")) {

				// TODO 原方法拿來當做銀行端用 (但回應代碼要過濾掉42xx)，另外做一個給業者端的檔案， 票交端分actionType B or C
				if (user_type.equals("A")) {
					if (this.create4Bank(basedata_download_form, valueMap) ) {
						this.create4Agent(basedata_download_form, valueMap);
					}
				}
				if (user_type.equals("B")) {
					this.create4Bank(basedata_download_form, valueMap);
				}
				if (user_type.equals("C")) {
					this.create4Agent(basedata_download_form, valueMap);
				}
				//
				// Map<String,Object> filenameAndDataMap =
				// basedata_download_bo.getFilenameListAndDataList();
				// //Zip的Byte[]
				// byte[] byteZIP = null;
				// //List有TXT的Byte[]才做
				// if(((List<byte[]>)filenameAndDataMap.get("dataList")).size() > 0){
				// //Zip的Byte[]
				// byteZIP =
				// codeUtils.createZIP((List<byte[]>)filenameAndDataMap.get("dataList"),(List<String>)filenameAndDataMap.get("filenameList"),null);
				// //正常，檔案放到資料夾下
				// if(byteZIP != null){
				// codeUtils.putFileToPath(fullPath,byteZIP);
				// basedata_download_form.setMsg("產生資料完成");
				// //寫操作軌跡記錄(成功)
				// userlog_bo.writeLog("L",null,null,pkMap);
				// }
				// //有問題
				// else{
				// basedata_download_form.setMsg("壓縮過程出現問題");
				// //寫操作軌跡記錄(失敗)
				// msgMap.put("msg",basedata_download_form.getMsg());
				// userlog_bo.writeFailLog("L",msgMap,null,null,pkMap);
				// }
				// }
				// else{
				// basedata_download_form.setMsg("查詢過程出現問題");
				// //寫操作軌跡記錄(失敗)
				// msgMap.put("msg",basedata_download_form.getMsg());
				// userlog_bo.writeFailLog("L",msgMap,null,null,pkMap);
				// }
			}

			// 下載檔案
			if (ac_key.equalsIgnoreCase("download")) {
				String fileName = "";
				//1為BIG5
				if("1".equals(enc_type)) {
					// 取得放置檔案的資料夾根目錄
					String basedataDirPath = valueMap.get("basedataDirPath");
					logger.debug("basedataDirPath=" + basedataDirPath);
					// 取得檔名的前置碼
					String basedataFilePrefix = valueMap.get("basedataFilePrefix");
					logger.debug("basedataFilePrefix=" + basedataFilePrefix);
					String basedataFilePrefix_c = valueMap.get("basedataFilePrefix_C");
					logger.debug("basedataFilePrefix_c=" + basedataFilePrefix_c);

					// 產生檔案和下載檔案的路徑 ex:eachrpt/eACH_CommonData
					String filePath = basedataDirPath + "/" + basedataFilePrefix + "/";
					logger.debug("filePath==>" + filePath);
					// 產生檔案和下載檔案的檔名

					if (user_type.equals("A")) {
						if (basedata_download_form.getActionType().equals("B")) {
							// ex:eachrpt/eACH_CommonData/eACH_CommonData.zip
							fileName = basedataFilePrefix + ".zip";
						}
						if (basedata_download_form.getActionType().equals("C")) {
							// ex:eachrpt/eACH_CommonData/eACH_AgentCommonData.zip
							fileName = basedataFilePrefix_c + ".zip";
						}
					}
					if (user_type.equals("B")) {
						fileName = basedataFilePrefix + ".zip";

					}
					if (user_type.equals("C")) {
						fileName = basedataFilePrefix_c + ".zip";
					}
					logger.debug("fileName==>" + fileName);

					// 產生檔案和下載檔案的路徑加檔名
					String fullPath = filePath + fileName;
					logger.debug("fullPath==>" + fullPath);

					// 將頁面上的查詢條件放進pkMap
					Map<String, Object> pkMap = new HashMap<String, Object>();
					pkMap.put("serchStrs", basedata_download_form.getSerchStrs());
					// 如果有錯誤要將訊息放進去
					Map<String, Object> msgMap = new HashMap<String, Object>();

					// 取得放置的檔案
					InputStream inputStream = codeUtils.getFileFromPath(fullPath);
					// 沒問題
					if (inputStream != null) {
						// 頁面塞時間的token
						String downloadToken = basedata_download_form.getDownloadToken();
						// 將檔案吐到前端頁面
						codeUtils.forDownload(inputStream, fileName, "fileDownloadToken", downloadToken);
						// 寫操作軌跡記錄(成功)
						userlog_bo.writeLog("F", null, null, pkMap);
						return null;
					}
					// 找不到檔案或檔案轉成InputStream出現問題
					else {
						basedata_download_form.setMsg("找不到檔案或檔案轉成InputStream出現問題");
						// 寫操作軌跡記錄(失敗)
						msgMap.put("msg", basedata_download_form.getMsg());
						userlog_bo.writeFailLog("F", msgMap, null, null, pkMap);
					}
				//UTF-8
				}else {
					// 取得放置檔案的資料夾根目錄
					String basedataDirPath = valueMap.get("basedataDirPath");
					logger.debug("basedataDirPath=" + basedataDirPath);
					// 取得檔名的前置碼
					String basedataFilePrefix = valueMap.get("basedataFilePrefix_UTF8");
					logger.debug("basedataFilePrefix=" + basedataFilePrefix);
					String basedataFilePrefix_c = valueMap.get("basedataFilePrefix_C_UTF8");
					logger.debug("basedataFilePrefix_c=" + basedataFilePrefix_c);

					// 產生檔案和下載檔案的路徑 ex:eachrpt/eACH_CommonData
					String filePath = basedataDirPath + "/" + basedataFilePrefix + "/";
					logger.debug("filePath==>" + filePath);
					// 產生檔案和下載檔案的檔名

					if (user_type.equals("A")) {
						if (basedata_download_form.getActionType().equals("B")) {
							// ex:eachrpt/eACH_CommonData/eACH_CommonData.zip
							fileName = basedataFilePrefix + ".zip";
						}
						if (basedata_download_form.getActionType().equals("C")) {
							// ex:eachrpt/eACH_CommonData/eACH_AgentCommonData.zip
							fileName = basedataFilePrefix_c + ".zip";
						}
					}
					if (user_type.equals("B")) {
						fileName = basedataFilePrefix + ".zip";

					}
					if (user_type.equals("C")) {
						fileName = basedataFilePrefix_c + ".zip";
					}
					logger.debug("fileName==>" + fileName);

					// 產生檔案和下載檔案的路徑加檔名
					String fullPath = filePath + fileName;
					logger.debug("fullPath==>" + fullPath);

					// 將頁面上的查詢條件放進pkMap
					Map<String, Object> pkMap = new HashMap<String, Object>();
					pkMap.put("serchStrs", basedata_download_form.getSerchStrs());
					// 如果有錯誤要將訊息放進去
					Map<String, Object> msgMap = new HashMap<String, Object>();

					// 取得放置的檔案
					InputStream inputStream = codeUtils.getFileFromPath(fullPath);
					// 沒問題
					if (inputStream != null) {
						// 頁面塞時間的token
						String downloadToken = basedata_download_form.getDownloadToken();
						// 將檔案吐到前端頁面
						codeUtils.forDownload(inputStream, fileName, "fileDownloadToken", downloadToken);
						// 寫操作軌跡記錄(成功)
						userlog_bo.writeLog("F", null, null, pkMap);
						return null;
					}
					// 找不到檔案或檔案轉成InputStream出現問題
					else {
						basedata_download_form.setMsg("找不到檔案或檔案轉成InputStream出現問題");
						// 寫操作軌跡記錄(失敗)
						msgMap.put("msg", basedata_download_form.getMsg());
						userlog_bo.writeFailLog("F", msgMap, null, null, pkMap);
					}
				}
			}
		} catch (Exception e) {
			basedata_download_form.setMsg(e.getMessage());
			e.printStackTrace();
		}
		target = StrUtils.isEmpty(basedata_download_form.getTarget()) ? "" : basedata_download_form.getTarget();
		return mapping.findForward(target);
	}

	public boolean create4Bank(Basedata_Download_Form basedata_download_form, Map<String, String> valueMap) {
		boolean result = false;
		// 取得放置檔案的資料夾根目錄
		String basedataDirPath = valueMap.get("basedataDirPath");
		logger.debug("basedataDirPath=" + basedataDirPath);
		// 取得前置路徑
		String basedataFilePrefix = valueMap.get("basedataFilePrefix");
		logger.debug("basedataFilePrefix=" + basedataFilePrefix);

		// 產生檔案和下載檔案的路徑
		String filePath = basedataDirPath + "/" + basedataFilePrefix + "/";
		logger.debug("filePath==>" + filePath);
		// 產生檔案和下載檔案的檔名
		String fileName = basedataFilePrefix + ".zip";
		logger.debug("fileName==>" + fileName);
		// 產生檔案和下載檔案的路徑加檔名
		String fullPath = filePath + fileName;
		logger.debug("fullPath==>" + fullPath);

		// 將頁面上的查詢條件放進pkMap
		Map<String, Object> pkMap = new HashMap<String, Object>();
		pkMap.put("serchStrs", basedata_download_form.getSerchStrs());
		// 如果有錯誤要將訊息放進去
		Map<String, Object> msgMap = new HashMap<String, Object>();
		
		boolean big5Result = false;
		boolean utf8Result = false;
		
		try {
			Map<String, Object> filenameAndDataMap = basedata_download_bo.getFilenameListAndDataList();
			// Zip的Byte[]
			byte[] byteZIP = null;
			// List有TXT的Byte[]才做
			if (((List<byte[]>) filenameAndDataMap.get("dataList")).size() > 0) {
				// Zip的Byte[]
				byteZIP = codeUtils.createZIP((List<byte[]>) filenameAndDataMap.get("dataList"),
						(List<String>) filenameAndDataMap.get("filenameList"), null);
				// 正常，檔案放到資料夾下
				if (byteZIP != null) {
					codeUtils.putFileToPath(fullPath, byteZIP);
					basedata_download_form.setMsg("產生資料完成(BIG5)");
					big5Result = true;
					// 寫操作軌跡記錄(成功)
					userlog_bo.writeLog("L", null, null, pkMap);
					result = true;
				}
				// 有問題
				else {
					basedata_download_form.setMsg("壓縮過程出現問題(銀行端)(BIG5)");
					big5Result = false;
					// 寫操作軌跡記錄(失敗)
					msgMap.put("msg", basedata_download_form.getMsg());
					userlog_bo.writeFailLog("L", msgMap, null, null, pkMap);
				}
			} else {
				basedata_download_form.setMsg("查詢過程出現問題(銀行端)(BIG5)");
				big5Result = false;
				// 寫操作軌跡記錄(失敗)
				msgMap.put("msg", basedata_download_form.getMsg());
				userlog_bo.writeFailLog("L", msgMap, null, null, pkMap);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 取得前置路徑
		basedataFilePrefix = valueMap.get("basedataFilePrefix_UTF8");
		logger.debug("basedataFilePrefix=" + basedataFilePrefix);

		// 產生檔案和下載檔案的路徑
		filePath = basedataDirPath + "/" + basedataFilePrefix + "/";
		logger.debug("filePath==>" + filePath);
		// 產生檔案和下載檔案的檔名
		fileName = basedataFilePrefix + ".zip";
		logger.debug("fileName==>" + fileName);
		// 產生檔案和下載檔案的路徑加檔名
		fullPath = filePath + fileName;
		logger.debug("fullPath==>" + fullPath);

		try {
			Map<String, Object> filenameAndDataMap = basedata_download_bo.getFilenameListAndDataList_UTF8();
			// Zip的Byte[]
			byte[] byteZIP = null;
			// List有TXT的Byte[]才做
			if (((List<byte[]>) filenameAndDataMap.get("dataList")).size() > 0) {
				// Zip的Byte[]
				byteZIP = codeUtils.createZIP((List<byte[]>) filenameAndDataMap.get("dataList"),
						(List<String>) filenameAndDataMap.get("filenameList"), null);
				// 正常，檔案放到資料夾下
				if (byteZIP != null) {
					codeUtils.putFileToPath(fullPath, byteZIP);
					basedata_download_form.setMsg("產生資料完成(UTF8)");
					utf8Result = true;
					// 寫操作軌跡記錄(成功)
					userlog_bo.writeLog("L", null, null, pkMap);
					result = true;
				}
				// 有問題
				else {
					basedata_download_form.setMsg("壓縮過程出現問題(銀行端)(UTF8)");
					utf8Result = false;
					// 寫操作軌跡記錄(失敗)
					msgMap.put("msg", basedata_download_form.getMsg());
					userlog_bo.writeFailLog("L", msgMap, null, null, pkMap);
				}
			} else {
				basedata_download_form.setMsg("查詢過程出現問題(銀行端)(UTF8)");
				utf8Result = false;
				// 寫操作軌跡記錄(失敗)
				msgMap.put("msg", basedata_download_form.getMsg());
				userlog_bo.writeFailLog("L", msgMap, null, null, pkMap);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(big5Result && utf8Result) {
			basedata_download_form.setMsg("產生資料完成");
		}

		return result;
	}

	public void create4Agent(Basedata_Download_Form basedata_download_form, Map<String, String> valueMap) {

		// 取得放置檔案的資料夾根目錄
		String basedataDirPath = valueMap.get("basedataDirPath");
		logger.debug("basedataDirPath=" + basedataDirPath);
		// 取得前置路徑
		String basedataFilePrefix = valueMap.get("basedataFilePrefix");
		logger.debug("basedataFilePrefix=" + basedataFilePrefix);
		String basedataFilePrefix_c = valueMap.get("basedataFilePrefix_C");
		logger.debug("basedataFilePrefix_c=" + basedataFilePrefix_c);

		// 產生檔案和下載檔案的路徑
		String filePath = basedataDirPath + "/" + basedataFilePrefix + "/";
		logger.debug("filePath==>" + filePath);
		// 產生檔案和下載檔案的檔名
		String fileName = basedataFilePrefix_c + ".zip";
		logger.debug("fileName==>" + fileName);
		// 產生檔案和下載檔案的路徑加檔名
		String fullPath = filePath + fileName;
		logger.debug("fullPath==>" + fullPath);

		// 將頁面上的查詢條件放進pkMap
		Map<String, Object> pkMap = new HashMap<String, Object>();
		pkMap.put("serchStrs", basedata_download_form.getSerchStrs());
		// 如果有錯誤要將訊息放進去
		Map<String, Object> msgMap = new HashMap<String, Object>();
		
		boolean big5Result = false;
		boolean utf8Result = false;

		try {
			Map<String, Object> filenameAndDataMap = basedata_download_bo.getFilenameListAndDataList4Agent();
			// Zip的Byte[]
			byte[] byteZIP = null;
			// List有TXT的Byte[]才做
			if (((List<byte[]>) filenameAndDataMap.get("dataList")).size() > 0) {
				// Zip的Byte[]
				byteZIP = codeUtils.createZIP((List<byte[]>) filenameAndDataMap.get("dataList"),
						(List<String>) filenameAndDataMap.get("filenameList"), null);
				// 正常，檔案放到資料夾下
				if (byteZIP != null) {
					codeUtils.putFileToPath(fullPath, byteZIP);
					basedata_download_form.setMsg("產生資料完成(BIG5)");
					big5Result = true;
					// 寫操作軌跡記錄(成功)
					userlog_bo.writeLog("L", null, null, pkMap);
				}
				// 有問題
				else {
					basedata_download_form.setMsg("壓縮過程出現問題(業者端)(BIG5)");
					// 寫操作軌跡記錄(失敗)
					big5Result = false;
					msgMap.put("msg", basedata_download_form.getMsg());
					userlog_bo.writeFailLog("L", msgMap, null, null, pkMap);
				}
			} else {
				basedata_download_form.setMsg("查詢過程出現問題(業者端)(BIG5)");
				big5Result = false;
				// 寫操作軌跡記錄(失敗)
				msgMap.put("msg", basedata_download_form.getMsg());
				userlog_bo.writeFailLog("L", msgMap, null, null, pkMap);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 取得前置路徑
		basedataFilePrefix = valueMap.get("basedataFilePrefix_UTF8");
		logger.debug("basedataFilePrefix=" + basedataFilePrefix);
		basedataFilePrefix_c = valueMap.get("basedataFilePrefix_C_UTF8");
		logger.debug("basedataFilePrefix_c=" + basedataFilePrefix_c);

		// 產生檔案和下載檔案的路徑
		filePath = basedataDirPath + "/" + basedataFilePrefix + "/";
		logger.debug("filePath==>" + filePath);
		// 產生檔案和下載檔案的檔名
		fileName = basedataFilePrefix_c + ".zip";
		logger.debug("fileName==>" + fileName);
		// 產生檔案和下載檔案的路徑加檔名
		fullPath = filePath + fileName;
		logger.debug("fullPath==>" + fullPath);

		try {
			Map<String, Object> filenameAndDataMap = basedata_download_bo.getFilenameListAndDataList4Agent_UTF8();
			// Zip的Byte[]
			byte[] byteZIP = null;
			// List有TXT的Byte[]才做
			if (((List<byte[]>) filenameAndDataMap.get("dataList")).size() > 0) {
				// Zip的Byte[]
				byteZIP = codeUtils.createZIP((List<byte[]>) filenameAndDataMap.get("dataList"),
						(List<String>) filenameAndDataMap.get("filenameList"), null);
				// 正常，檔案放到資料夾下
				if (byteZIP != null) {
					codeUtils.putFileToPath(fullPath, byteZIP);
					basedata_download_form.setMsg("產生資料完成(UTF8)");
					utf8Result = true;
					// 寫操作軌跡記錄(成功)
					userlog_bo.writeLog("L", null, null, pkMap);
				}
				// 有問題
				else {
					basedata_download_form.setMsg("壓縮過程出現問題(業者端)(UTF8)");
					utf8Result = false;
					// 寫操作軌跡記錄(失敗)
					msgMap.put("msg", basedata_download_form.getMsg());
					userlog_bo.writeFailLog("L", msgMap, null, null, pkMap);
				}
			} else {
				basedata_download_form.setMsg("查詢過程出現問題(業者端)(UTF8)");
				utf8Result = false;
				// 寫操作軌跡記錄(失敗)
				msgMap.put("msg", basedata_download_form.getMsg());
				userlog_bo.writeFailLog("L", msgMap, null, null, pkMap);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(big5Result && utf8Result) {
			basedata_download_form.setMsg("產生資料完成");
		}
	}

	public BASEDATA_DOWNLOAD_BO getBasedata_download_bo() {
		return basedata_download_bo;
	}

	public void setBasedata_download_bo(BASEDATA_DOWNLOAD_BO basedata_download_bo) {
		this.basedata_download_bo = basedata_download_bo;
	}

	public CodeUtils getCodeUtils() {
		return codeUtils;
	}

	public void setCodeUtils(CodeUtils codeUtils) {
		this.codeUtils = codeUtils;
	}

	public EACH_USERLOG_BO getUserlog_bo() {
		return userlog_bo;
	}

	public void setUserlog_bo(EACH_USERLOG_BO userlog_bo) {
		this.userlog_bo = userlog_bo;
	}
}
