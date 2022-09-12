package tw.org.twntch.action;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.apache.struts.util.LabelValueBean;

import tw.org.twntch.bo.EACH_FILEUPLOAD_BO;
import tw.org.twntch.bo.EACH_USERLOG_BO;
import tw.org.twntch.form.Each_Fileupload_Form;
import tw.org.twntch.form.Login_Form;
import tw.org.twntch.po.AGENT_PROFILE;
import tw.org.twntch.util.BeanUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.StrUtils;

public class EACH_FILEUPLOAD_Action extends Action {
	private EACH_FILEUPLOAD_BO each_fileupload_bo;
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private EACH_USERLOG_BO userlog_bo;//寫操作軌跡記錄

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
		
		Each_Fileupload_Form each_fileupload_form = (Each_Fileupload_Form) form;
		String ac_key = StrUtils.isEmpty(each_fileupload_form.getAc_key())?"":each_fileupload_form.getAc_key();
		String target = StrUtils.isEmpty(each_fileupload_form.getTarget())?"search":each_fileupload_form.getTarget();
		each_fileupload_form.setTarget(target);
		Login_Form login_form = (Login_Form) request.getSession().getAttribute("login_form");
		logger.debug("ac_key=" + ac_key);
		logger.debug("target=" + target);
		//前往檔案新增頁面
		if(target.equalsIgnoreCase("add_p")){
			
			logger.debug("ACTION_TYPE=" + each_fileupload_form.getACTION_TYPE());
			if(login_form.getUserData().getUSER_TYPE().equals("A")){
//				TODO 還需要一個屬性ACTOIN_TYPE
				if(each_fileupload_form.getACTION_TYPE().equals("B")){
					//抓銀行清單的值
					each_fileupload_form.setBanksList(each_fileupload_bo.getBanksList());
				}else if(each_fileupload_form.getACTION_TYPE().equals("C")){
					//抓代理業者的值
					each_fileupload_form.setBanksList(each_fileupload_bo.getAgentList());
				}
			}
			if(login_form.getUserData().getUSER_TYPE().equals("B")){
				//抓銀行清單的值
//				each_fileupload_form.setBanksList(each_fileupload_bo.getBanksList());
				each_fileupload_form.setBanksList(each_fileupload_bo.getBanksListWithoutThisBank(login_form.getUserData().getUSER_COMPANY()));
			}
			if(login_form.getUserData().getUSER_TYPE().equals("C")){
				//抓代理業者的值
//				each_fileupload_form.setBanksList(each_fileupload_bo.getAgentList());
				List<LabelValueBean> beanList = new LinkedList<LabelValueBean>();
				LabelValueBean bean = new LabelValueBean("0188888- 臺灣票據交換所" , "0188888");
				beanList.add(bean);
				each_fileupload_form.setBanksList(beanList);
			}
		}
		//檔案新增
		if(ac_key.equalsIgnoreCase("add")){
			//將頁面上的查詢條件放進pkMap
			Map<String,Object> pkMap = new HashMap<String,Object>();
			pkMap.put("serchStrs",each_fileupload_form.getSerchStrs());
			//如果有錯誤要將訊息放進去
			Map<String,Object> msgMap = new HashMap<String,Object>();
			//新增成功會用到
			String serchStrs = (String) (StrUtils.isEmpty(pkMap.get("serchStrs")+"" ) ? "{}" :pkMap.get("serchStrs")) ;
			serchStrs = serchStrs.replace("[", "").replace("]", "");
			Map<String,Object> newMap = JSONUtils.json2map(serchStrs);
			
			try {
				//檢查檔案是否合乎規範
				String result = checkData(each_fileupload_form);
				//不合乎規範
				if(!"".equals(result)){
					//抓銀行清單的值
					each_fileupload_form.setBanksList(each_fileupload_bo.getBanksList());
					//alert訊息
					each_fileupload_form.setMsg(result);
					//停在新增頁面
					each_fileupload_form.setTarget("add_p");
					
					//寫操作軌跡記錄(失敗)
					msgMap.put("msg",each_fileupload_form.getMsg());
					userlog_bo.writeFailLog("K",msgMap,null,null,pkMap);
				}
				//合乎規範
				else{
					//新增檔案
					addData(each_fileupload_form,login_form);
					each_fileupload_form.setMsg("儲存成功");
					each_fileupload_form.setTarget("search");
					
					//寫操作軌跡記錄(成功)
					userlog_bo.writeLog("K",null,newMap,null);
				}
			}
			catch (Exception e) {
				each_fileupload_form.setMsg(e.getMessage());
				each_fileupload_form.setTarget("add_p");
				
				//寫操作軌跡記錄(失敗)
				msgMap.put("msg",each_fileupload_form.getMsg());
				userlog_bo.writeFailLog("K",msgMap,null,null,pkMap);
			}
		}
		//刪除檔案
		if(ac_key.equalsIgnoreCase("delete")){
			String serchStrs = StrUtils.isEmpty(each_fileupload_form.getSerchStrs()) ? "{}" :each_fileupload_form.getSerchStrs();
			serchStrs = serchStrs.replace("[", "").replace("]", "");
			Map<String,Object> map = JSONUtils.json2map(serchStrs);
			//將刪除檔案的KEY放進pkMap
			Map<String,Object> pkMap = new HashMap<String,Object>();
			pkMap.put("ID_NOS",map.get("ID_NOS"));
			pkMap.put("FILE_NOS",map.get("FILE_NOS"));
			//刪除成功會用到
			Map<String,Object> oldMap = new HashMap<String,Object>();
			oldMap.put("FILENAMES",map.get("FILENAMES"));
			oldMap.put("FILESIZES",map.get("FILESIZES"));
			
			deleteData(each_fileupload_form);
			each_fileupload_form.setMsg("刪除成功");
			each_fileupload_form.setTarget("search");
			
			//寫操作軌跡記錄(成功)
			userlog_bo.writeLog("D",oldMap,null,pkMap);
		}
		
		target = StrUtils.isEmpty(each_fileupload_form.getTarget())?"":each_fileupload_form.getTarget();
			
		return mapping.findForward(target);
	}
	//檢查檔案是否合乎規範
	public String checkData(Each_Fileupload_Form each_fileupload_form){
		String[] filename = null;
		String[] filedesc = null;
		int[] filesize = null;
		
		//檢查是否有選銀行
		String[] selectedBanksArray = each_fileupload_form.getSelectedBanksArray();
		if(selectedBanksArray == null){
			return "未選擇銀行";
		}
		filename = new String[3];
		//檔名
		filename[0] = each_fileupload_form.getFILE1().getFileName();
		filename[1] = each_fileupload_form.getFILE2().getFileName();
		filename[2] = each_fileupload_form.getFILE3().getFileName();
		filedesc = new String[3];
		//檔案描述
		filedesc[0] = each_fileupload_form.getFILE_DESC1();
		filedesc[1] = each_fileupload_form.getFILE_DESC2();
		filedesc[2] = each_fileupload_form.getFILE_DESC3();
		filesize = new int[3];
		//檔案大小
		filesize[0] = each_fileupload_form.getFILE1().getFileSize();
		filesize[1] = each_fileupload_form.getFILE2().getFileSize();
		filesize[2] = each_fileupload_form.getFILE3().getFileSize();
		
		for(int x = 0; x < 3; x++){
			//如果該欄位有值的話(新增不一定每個欄位都有值)
			if (!"".equals(filename[x])) {
				//檔名過長
				if(filename[x].length() > 50){
					return "上傳檔案"+Integer.valueOf(x+1)+"檔案名稱過長";
				}
				//檔案描述過長
				if(filedesc[x].length() > 50){
					return "上傳檔案"+Integer.valueOf(x+1)+"檔案描述過長";
				}
				//檔案太大(5MB)
				if(filesize[x]/1024 > 5000){
					return "上傳檔案"+Integer.valueOf(x+1)+"過大";
				}
			}
		}
		return "";
	}
	public void addData(Each_Fileupload_Form each_fileupload_form,Login_Form login_form) throws Exception{
		String[] filename = new String[3];
		String[] filedesc = new String[3];
		int[] filesize = new int[3];
		byte[][] filedata = new byte[3][];
		String toBanks = "";
		//struts的File物件
		FormFile file1 = each_fileupload_form.getFILE1();
		FormFile file2 = each_fileupload_form.getFILE2();
		FormFile file3 = each_fileupload_form.getFILE3();
		
		String file1_desc = each_fileupload_form.getFILE_DESC1();
		String file2_desc = each_fileupload_form.getFILE_DESC2();
		String file3_desc = each_fileupload_form.getFILE_DESC3();
		//檔名
		filename[0] = file1.getFileName();
		//檔案描述
		filedesc[0] = file1_desc;
		//檔案大小
		filesize[0] = file1.getFileSize();
		//檔案
		filedata[0] = file1.getFileData();
		filename[1] = file2.getFileName();
		filedesc[1] = file2_desc;
		filesize[1] = file2.getFileSize();
		filedata[1] = file2.getFileData();
		filename[2] = file3.getFileName();
		filedesc[2] = file3_desc;
		filesize[2] = file3.getFileSize();
		filedata[2] = file3.getFileData();
		//取出使用者的資料
		Map userData = BeanUtils.describe(login_form.getUserData());
		String userID = (String)userData.get("USER_ID");
		String userCompany = (String)userData.get("USER_COMPANY");
		String userType = (String)userData.get("USER_TYPE");
		//產生UUID
		String uuid = UUID.randomUUID().toString(); 
		//給哪些銀行下載
		String[] selectedBanksArray = each_fileupload_form.getSelectedBanksArray();
		for(int i = 0; i < selectedBanksArray.length; i++){
			logger.debug("i="+i+",selectedBank:"+selectedBanksArray[i]);
			if(i == 0){
				toBanks = selectedBanksArray[i];
			}
			else{
				toBanks = toBanks+","+selectedBanksArray[i];
			}
		}
		logger.debug("toBanks="+toBanks);
		
		each_fileupload_bo.add(uuid,filename,filedesc,filesize,filedata,userID,userCompany,userType,toBanks);
	}
	public void deleteData(Each_Fileupload_Form each_fileupload_form){
		//從頁面取得ID_NOS和FILE_NOS
		String[] idNos = each_fileupload_form.getID_NOS().split(",");
		String[] fileNos = each_fileupload_form.getFILE_NOS().split(","); 
		for(int x=0;x<idNos.length;x++){
			logger.debug("idNos="+idNos[x]+",fileNos="+fileNos[x]);
			each_fileupload_bo.delete(idNos[x],Integer.valueOf(fileNos[x]));
		}
	}
	public EACH_FILEUPLOAD_BO getEach_fileupload_bo() {
		return each_fileupload_bo;
	}
	public void setEach_fileupload_bo(EACH_FILEUPLOAD_BO each_fileupload_bo) {
		this.each_fileupload_bo = each_fileupload_bo;
	}
	public EACH_USERLOG_BO getUserlog_bo() {
		return userlog_bo;
	}
	public void setUserlog_bo(EACH_USERLOG_BO userlog_bo) {
		this.userlog_bo = userlog_bo;
	}
}
