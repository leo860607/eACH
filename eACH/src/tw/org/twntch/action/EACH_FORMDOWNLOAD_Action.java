package tw.org.twntch.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
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
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import tw.org.twntch.bo.EACH_USERLOG_BO;
import tw.org.twntch.form.Each_Formdownload_Form;
import tw.org.twntch.util.CodeUtils;
import tw.org.twntch.util.StrUtils;

public class EACH_FORMDOWNLOAD_Action extends Action{
	private CodeUtils codeUtils;
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private EACH_USERLOG_BO userlog_bo;//寫操作軌跡記錄
	
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
		Each_Formdownload_Form each_formdownload_form = (Each_Formdownload_Form) form;
		String ac_key = StrUtils.isEmpty(each_formdownload_form.getAc_key())?"":each_formdownload_form.getAc_key();
		String target = StrUtils.isEmpty(each_formdownload_form.getTarget())?"search":each_formdownload_form.getTarget();
		each_formdownload_form.setTarget(target);
		logger.debug("ac_key=" + ac_key);
		logger.debug("target=" + target);
		
		//第一次進來先查詢
		if("".equals(ac_key)){
			try {
				//查詢XML，並趴到頁面
				queryList(each_formdownload_form);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		//下載檔案
		if(ac_key.equalsIgnoreCase("download")){
			//將下載表單的名稱記進去
			Map<String,Object> pkMap = new HashMap<String,Object>();
			pkMap.put("serchStrs",each_formdownload_form.getSerchStrs());
			//如果有錯誤要將訊息放進去
			Map<String,Object> msgMap = new HashMap<String,Object>();
			
			try {
				//取得在classpath下的properties檔案
				Map<String,String> valueMap = codeUtils.getPropertyValue("Configuration.properties","formDirPath");
				//無法取得properties
				if(valueMap == null){
					//回頁面alert訊息
					each_formdownload_form.setMsg("無法取得properties檔");
					
					//寫操作軌跡記錄(失敗)
					msgMap.put("msg",each_formdownload_form.getMsg());
					userlog_bo.writeFailLog("F",msgMap,null,null,pkMap);
					
					//重新查詢XML，並趴到頁面
					queryList(each_formdownload_form);
					return mapping.findForward(target);
				}
				//取得放置檔案的資料夾絕對位置
				String formDirPath = valueMap.get("formDirPath");
				logger.debug("formDirPath="+formDirPath);
				//頁面塞時間的token
				String downloadToken = each_formdownload_form.getDownloadToken();
				//取得放置檔案的資料夾
				File dir = new File(formDirPath);
				//取得使用者要下載的檔案名稱，因為頁面傳來的中文檔名已加密，所以要先decode回來
				String downloadFilename = URLDecoder.decode(each_formdownload_form.getFilename(),"UTF-8");
				logger.debug("downloadFilename="+downloadFilename);
				
				String downloadFilenameHexString = codeUtils.bytesToHex(downloadFilename.getBytes("UTF-8"));
				logger.debug("downloadFilenameHexString="+downloadFilenameHexString);
				//有資料夾
				if(dir.listFiles() != null){
					//瀏覽資料夾下的所有檔案
					for(int i=0;i<dir.listFiles().length;i++){
						File file = dir.listFiles()[i];
						String filename = file.getName();
						logger.debug("filename="+filename);
						String filenameHexString = codeUtils.bytesToHex(filename.getBytes());
						logger.debug("filenameHexString="+filenameHexString);
						//如果是使用者要下載的檔案(這裡會用Hex比對是因為資料夾下的檔案的中文檔名無法正確顯示與比較)
						if(filenameHexString.equals(downloadFilenameHexString)){
							InputStream inputStream = new FileInputStream(file);
							//將檔案吐到前端頁面
							codeUtils.forDownload(inputStream,downloadFilename,"fileDownloadToken",downloadToken);
							
							//寫操作軌跡記錄(成功)
							userlog_bo.writeLog("F",null,null,pkMap);
							
							return null;
						}
						//如果找不到使用者要下載的檔案
						if(i == dir.listFiles().length-1){
							each_formdownload_form.setMsg("找不到該檔案");
						}
					}
					//資料夾下沒有檔案
					if(dir.listFiles().length == 0){
						each_formdownload_form.setMsg("資料夾無檔案");
					}
				}
				//資料夾不存在
				else{
					each_formdownload_form.setMsg("資料夾不存在");
				}
				//重新查詢XML，並趴到頁面
				queryList(each_formdownload_form);
			}
			catch(Exception e){
				e.printStackTrace();
				each_formdownload_form.setMsg(e.getMessage());
				try {
					queryList(each_formdownload_form);
				}
				catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			//寫操作軌跡記錄(失敗)
			msgMap.put("msg",each_formdownload_form.getMsg());
			userlog_bo.writeFailLog("F",msgMap,null,null,pkMap);
		}
		
		target = StrUtils.isEmpty(each_formdownload_form.getTarget())?"":each_formdownload_form.getTarget();
		
		return mapping.findForward(target);
	}
	//讀取XML
	public void queryList(Each_Formdownload_Form each_formdownload_form) throws Exception{
		//取得在classpath下的xml檔案
		SAXReader reader = new SAXReader();
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("FormData.xml");
		Document document = reader.read(inputStream);
		Element rootElement = document.getRootElement();
		logger.debug("rootElement.asXML()="+rootElement.asXML());
		
		List<Map<String,Object>> dataListMap = new ArrayList<Map<String,Object>>();
		//如果categorys下有category的話 
		for(int x=0;x<rootElement.elements().size();x++){
			Map map = new HashMap<String,Object>();
			Element categoryElement = (Element)rootElement.elements().get(x);
			logger.debug("categoryElement.attributeValue('id')="+categoryElement.attributeValue("id"));
			map.put("Header",((Element)rootElement.elements().get(x)).attributeValue("id"));
			List fileList = new ArrayList();
			//如果category下有document的話
			for(int y=0;y<categoryElement.elements().size();y++){
				List list = new ArrayList();
				Element documentElement = (Element)categoryElement.elements().get(y);
				//如果document下有title和filename的話
				for(int z=0;z<documentElement.elements().size();z++){
					//將title和filename tag的text加進list
					logger.debug("((Element)documentElement.elements().get("+z+")).getText()="+((Element)documentElement.elements().get(z)).getText());
					list.add(((Element)documentElement.elements().get(z)).getText());
				}
				fileList.add(list);
			}
			map.put("FileList",fileList);
			dataListMap.add(map);
		}
		each_formdownload_form.setDataListMap(dataListMap);
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
