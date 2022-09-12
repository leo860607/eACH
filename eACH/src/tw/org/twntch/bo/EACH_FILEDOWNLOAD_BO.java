package tw.org.twntch.bo;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.google.gson.Gson;

import tw.org.twntch.db.dao.hibernate.CommonSpringDao;
import tw.org.twntch.db.dao.hibernate.EACH_FILEDOWNLOAD_Dao;
import tw.org.twntch.db.dao.hibernate.Page;
import tw.org.twntch.form.Each_Filedownload_Form;
import tw.org.twntch.form.Login_Form;
import tw.org.twntch.po.EACH_FILEDOWNLOAD;
import tw.org.twntch.util.BeanUtils;
import tw.org.twntch.util.CodeUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.WebServletUtils;

public class EACH_FILEDOWNLOAD_BO {
	private EACH_FILEDOWNLOAD_Dao each_filedownload_Dao;
	private CommonSpringDao commonSpringDao;
	private CodeUtils codeUtils;
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	//JQGRID分頁查詢
	public String pageSearch(Map<String, String> param) throws Exception{
		//目前頁數
		String pageNo = StrUtils.isEmpty(param.get("page")) ?"0":param.get("page");
		//每頁筆數
		String pageSize = StrUtils.isEmpty(param.get("rows")) ?Arguments.getStringArg("PAGE.SIZE"):param.get("rows");
		logger.debug("pageNo="+pageNo);
		logger.debug("pageSize="+pageSize);
		Map rtnMap = new HashMap();
		
		List<EACH_FILEDOWNLOAD> list = null;
		Page page = null;
		try {
			String countQuery = "";
			String sql = "";
				
			//只能看到TOBANKS有自己單位代號的檔案
			countQuery = "SELECT COUNT(*) FROM EACH_FILEUPLOAD WHERE TOBANKS LIKE'%"+param.get("USER_COMPANY")+"%'";
			sql = "SELECT ID_NO, FILE_NO, FILENAME,FILEDATA,FILETYPE,DOCNAME,FILESIZE,DOWNLOADTYPE,FILEPATH,DOWNLOADNUM,LASTDOWNLOADER,LASTDOWNLOADTIME,UPLOADER,CDATE,USER_COMPANY FROM EACH_FILEUPLOAD WHERE TOBANKS LIKE'%"+param.get("USER_COMPANY")+"%' ORDER BY CDATE DESC,FILE_NO ASC";
			logger.debug("countQuery="+countQuery);
			logger.debug("sql="+sql);
			
			String cols[] = {"ID_NO", "FILE_NO", "FILENAME", "FILEDATA", "FILETYPE", "DOCNAME", "FILESIZE", "DOWNLOADTYPE", "FILEPATH", "DOWNLOADNUM", "LASTDOWNLOADER", "LASTDOWNLOADTIME", "UPLOADER", "CDATE", "USER_COMPANY"};
			page = each_filedownload_Dao.getData(Integer.valueOf(pageNo), Integer.valueOf(pageSize), countQuery, sql, cols, EACH_FILEDOWNLOAD.class);
			list = (List<EACH_FILEDOWNLOAD>) page.getResult();
			logger.debug("list="+list);
			list = list!=null&& list.size() ==0 ?null:list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(page == null){
			rtnMap.put("total", "0");
			rtnMap.put("page", "0");
			rtnMap.put("records", "0");
			rtnMap.put("rows", new ArrayList());
		}else{
			rtnMap.put("total", page.getTotalPageCount());
			rtnMap.put("page", String.valueOf(page.getCurrentPageNo()));
			rtnMap.put("records", page.getTotalCount());
			//因為用Gson轉json，所以要格式日期
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if(list != null && list.size() > 0){
				for(EACH_FILEDOWNLOAD po : list){
					po.setCDATEString(sdf.format(po.getCDATE()));
					if(po.getUPDDATE() != null && !StrUtils.isEmpty(po.getUPDDATE().toString())){
						po.setUPDDATEString(sdf.format(po.getUPDDATE()));
					}
				}
			}
			rtnMap.put("rows",list);
		}
		String json = new Gson().toJson(rtnMap);
		return json;
	}
	
	//檔案下載
	public String download(Each_Filedownload_Form each_filedownload_form,Login_Form login_form){
		//從頁面取得ID_NOS和FILE_NOS
		String[] ID_NOS = each_filedownload_form.getID_NOS().split(",");
		String[] FILE_NOS = each_filedownload_form.getFILE_NOS().split(",");
		String dow_token = each_filedownload_form.getDow_token();
		//如果有錯誤的話，回傳到頁面的錯誤訊息
		String message = "";
		String sql = "";
		Object[] paramObject;
		List<Map<String,Object>> queryListMap;
		//選取的檔案數目
		int dataNum = ID_NOS.length;
		//Zip裡面各個檔案的檔名
		List<String> filenameList = new ArrayList<String>();
		//放置Data Byte[]的List
		List<byte[]> dataList = new ArrayList<byte[]>();
		
		for(int x=0;x<dataNum;x++){
			logger.debug("ID_NOS="+ID_NOS[x]+",FILE_NOS="+FILE_NOS[x]);
			sql = "SELECT FILENAME,FILEDATA,DOWNLOADNUM FROM EACH_FILEUPLOAD WHERE ID_NO=? AND FILE_NO=?";
			paramObject = new Object[2];
			paramObject[0] = ID_NOS[x];
			paramObject[1] = FILE_NOS[x];
			queryListMap = commonSpringDao.list(sql,paramObject);
			try{
				filenameList.add(new String(((String)queryListMap.get(0).get("FILENAME")).getBytes(),"UTF-8"));
			}
			catch(UnsupportedEncodingException e){
				filenameList.add((String)queryListMap.get(0).get("FILENAME"));
			}
			//將資料庫撈出來的檔案加到dataList
			dataList.add((byte[])queryListMap.get(0).get("FILEDATA"));
			
			//更新下載次數和最後下載人員
			sql = "UPDATE EACH_FILEUPLOAD SET DOWNLOADNUM=?,LASTDOWNLOADER=? WHERE ID_NO=? AND FILE_NO=?";
		    paramObject = new Object[4];
		    //選到的檔案就加一
		    paramObject[0] = (Integer)queryListMap.get(0).get("DOWNLOADNUM") + 1;
		    Map userData = null;
			try{
				userData = BeanUtils.describe(login_form.getUserData());
			}
			catch(Exception e){
				e.printStackTrace();
				message = codeUtils.appendMessage(message,e.getMessage());
			}
		    paramObject[1] = (String)userData.get("USER_ID");
		    paramObject[2] = ID_NOS[x];
		    paramObject[3] = FILE_NOS[x];
		    each_filedownload_Dao.saveOrUpdate(sql,paramObject);
		}
		//Zip的Byte[]
		byte[] byteZIP = null;
		//List有Data的Byte[]才做
		if(dataList.size() > 0){
			//Zip的Byte[]
			byteZIP = codeUtils.createZIP(dataList,filenameList,null);
			//正常，檔案吐到前端
			if(byteZIP != null){
				codeUtils.forDownload(new ByteArrayInputStream(byteZIP),"檔案.zip","fileDownloadToken",dow_token);
			}
			//有問題
			else{
				message = codeUtils.appendMessage(message,"壓縮過程出現問題");
			}
		}
	    return message;
	}
	//更新JQGRID的AJAX查詢
	public String queryDownLoadNumAndLastDownloader(Map<String,String> param){
		//從頁面傳來的ID_NOS和FILE_NOS
		logger.debug("ID_NOS="+WebServletUtils.getRequest().getParameter("ID_NOS"));
		logger.debug("FILE_NOS="+WebServletUtils.getRequest().getParameter("FILE_NOS"));
		String[] ID_NOS = WebServletUtils.getRequest().getParameter("ID_NOS").split(",");
		String[] FILE_NOS = WebServletUtils.getRequest().getParameter("FILE_NOS").split(",");
		
		String sql = "SELECT DOWNLOADNUM,LASTDOWNLOADER FROM EACH_FILEUPLOAD WHERE ID_NO=? AND FILE_NO=?";
		String cols[] = {"DOWNLOADNUM","LASTDOWNLOADER"};
		List<EACH_FILEDOWNLOAD> list = new ArrayList<EACH_FILEDOWNLOAD>();
		//將查出來的資料裝到list中傳到頁面更新Grid
		for(int x=0;x<ID_NOS.length;x++){
			logger.debug("ID_NO="+ID_NOS[x]);
			logger.debug("FILE_NO="+FILE_NOS[x]);
			list.add(each_filedownload_Dao.query(ID_NOS[x],Integer.valueOf(FILE_NOS[x]),sql,cols,EACH_FILEDOWNLOAD.class).get(0));
		}
		//將資料轉成json字串
		String json = new Gson().toJson(list);
		logger.debug("json="+json);
		return json;
	}
	
	public EACH_FILEDOWNLOAD_Dao getEach_filedownload_Dao() {
		return each_filedownload_Dao;
	}
	public void setEach_filedownload_Dao(EACH_FILEDOWNLOAD_Dao each_filedownload_Dao) {
		this.each_filedownload_Dao = each_filedownload_Dao;
	}
	public CommonSpringDao getCommonSpringDao() {
		return commonSpringDao;
	}
	public void setCommonSpringDao(CommonSpringDao commonSpringDao) {
		this.commonSpringDao = commonSpringDao;
	}
	public CodeUtils getCodeUtils() {
		return codeUtils;
	}
	public void setCodeUtils(CodeUtils codeUtils) {
		this.codeUtils = codeUtils;
	}
}
