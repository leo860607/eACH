package tw.org.twntch.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;

import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

import tw.org.twntch.db.dao.hibernate.EACH_BATCH_NOTIFY_Dao;
import tw.org.twntch.db.dao.hibernate.EACH_FUNC_LIST_Dao;
import tw.org.twntch.form.Login_Form;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.SpringAppCtxHelper;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.WebServletUtils;
import tw.org.twntch.util.zDateHandler;

public class GenericAction extends Action {
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private EACH_FUNC_LIST_Dao func_list_Dao ;
	
	
	public Map<String, String> getRPT_PRE_YandM_M1(String date , String type ,String key_start_year ,String key_start_month ,String key_end_year ,String key_end_month){
		Map<String, String> map = new HashMap<String, String>();
		String bizdate = "";
//		bizdate = zDateHandler.getEACH_PrveMonth(date, type);
		bizdate = zDateHandler.getEACH_PrveMonth(date, type, "E", "yyyyMM");
		map.put(key_start_year, bizdate.substring(0, 4));
		map.put(key_start_month, bizdate.substring(4, 6));
		map.put(key_end_year, bizdate.substring(0, 4));
		map.put(key_end_month, bizdate.substring(4, 6));
		return map;
		
	}
	
	
	
	/**
	 * 20151015 add by hugo req by UAT-2015904-02
	 * 用來處理使用struts 內部forward至另外一個action時，不會經過濾器的暫時處理方式
	 * @param login_form
	 * @param request
	 */
	public void resetGenericAttribute(Login_Form login_form , HttpServletRequest request){
		List<Map> list = null;
		String func_name = "";
		String up_func_name = "";
		String fcid = "";
		String userType = login_form.getUserData().getUSER_TYPE();
		func_list_Dao = (EACH_FUNC_LIST_Dao) (func_list_Dao == null ? SpringAppCtxHelper.getBean("func_list_Dao"):func_list_Dao);
		String funcUrl= request.getRequestURI().replace(request.getContextPath()+"/", "");
		list = func_list_Dao.getFuncAuth(funcUrl ,login_form.getUserData().getROLE_ID());
		for(Map map :list){
			System.out.println("AUTH_TYPE>>"+map.get("AUTH_TYPE"));
//			login_form.setS_auth_type(String.valueOf(map.get("AUTH_TYPE")) );
			if( userType.equals("A")){
				func_name = String.valueOf(map.get("FUNC_NAME")) ;
				up_func_name = String.valueOf(map.get("UP_FUCNAME")) ;
			}else{
				func_name = StrUtils.isEmpty(String.valueOf(map.get("FUNC_NAME_BK"))) ?String.valueOf(map.get("FUNC_NAME")) :String.valueOf(map.get("FUNC_NAME_BK"));
				up_func_name = StrUtils.isEmpty(String.valueOf(map.get("UP_FUCNAME_BK"))) ?String.valueOf(map.get("UP_FUCNAME")) :String.valueOf(map.get("UP_FUCNAME_BK"));
			}
			login_form.getUserData().setS_auth_type(String.valueOf(map.get("AUTH_TYPE")));
			login_form.getUserData().setS_up_func_name(up_func_name);
//			login_form.getUserData().setS_func_name(String.valueOf(map.get("FUNC_NAME")));
			login_form.getUserData().setS_func_name(func_name);
			login_form.getUserData().setS_fcid(fcid);
			login_form.getUserData().setS_is_record(String.valueOf(map.get("IS_RECORD")));
		}
		request.getSession().setAttribute("login_form", login_form);
	}
	
	public void setBreadcrumb(Login_Form form, String breadcrumb){
		if(StrUtils.isNotEmpty(breadcrumb)){
			List<HashMap<String, String>> list = new LinkedList<HashMap<String, String>>();
			try {
				String chi = java.net.URLDecoder.decode(breadcrumb, "UTF-8");
				String itemAry[] = chi.split(",");
				HashMap<String, String> item = null;
				for(int i = 0; i < itemAry.length; i++){
					item = new HashMap<String, String>();
					item.put("className", "");
					item.put("funcName", itemAry[i]);
					if(i < itemAry.length - 1){
						item.put("className", "has-next");
					}
					list.add(item);
				}
				if(form != null){
					form.setBreadcrumb(list);
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void addBreadcrumb(Login_Form form, String funcName){
		if(form != null){
			HashMap<String, String> item = new HashMap<String, String>();
			item.put("funcName", funcName);
			item.put("className", "");
			
			LinkedList<HashMap<String, String>> breadcrumb = null;
			if(form.getBreadcrumb() == null){
				breadcrumb = new LinkedList<HashMap<String, String>>();
			}else{
				breadcrumb = (LinkedList<HashMap<String, String>>)form.getBreadcrumb();
				((HashMap<String, String>)breadcrumb.getLast()).put("className", "has-next");
			}
			breadcrumb.addLast(item);
			form.setBreadcrumb(breadcrumb);
		}
	}

	public void exportFile2Client(HttpServletResponse response , String fullFilePath,String filename ,String dow_token ){
		filename = StrUtils.isEmpty(filename)?zDateHandler.getTheDate()+"_tx_1.pdf":filename;
		//return an application file instead of html page
	     response.setContentType("application/octet-stream");
//	     response.setContentType("application/pdf");
	     response.setHeader("Content-Disposition","attachment;filename="+filename);
	     response.addCookie(new Cookie("fileDownloadToken", dow_token));
//	     response.addCookie(new HttpCookie("fileDownloadToken", dow_token));
	     File file = null ;
	     try 
	     {
	    	file = new File(fullFilePath);
	       	//Get it from file system
//	       	FileInputStream in =new FileInputStream(new File(fullFilePath));
	    	FileInputStream in =new FileInputStream(file);
	        //Get it from web path
	       	//http://localhost:8080/eACH/
	        ServletOutputStream out = response.getOutputStream();
	        out.write(IOUtils.toByteArray(in));
	        in.close();
	        out.flush();
	        out.close();
	        //	 要把檔案刪除 
	        if(file!= null && file.exists()){
	        	file.delete();
	        }
	     }catch(Exception e){
	    	e.printStackTrace();
	    	logger.debug("exportFile2Client.Exception>>"+e);
	   }
	}
	
	public void exportFile2Client(HttpServletResponse response , String fullFilePath,String filename ,String dow_token ,boolean isDeleteFile){
		filename = StrUtils.isEmpty(filename)?zDateHandler.getTheDate()+"_tx_1.pdf":filename;
		//return an application file instead of html page
		response.setContentType("application/octet-stream");
//	     response.setContentType("application/pdf");
		response.setHeader("Content-Disposition","attachment;filename="+filename);
		response.addCookie(new Cookie("fileDownloadToken", dow_token));
//	     response.addCookie(new HttpCookie("fileDownloadToken", dow_token));
		File file = null ;
		try 
		{
			System.out.println("fullFilePath>>"+fullFilePath);
			System.out.println("filename>>"+filename);
			file = new File(fullFilePath);
			//Get it from file system
//	       	FileInputStream in =new FileInputStream(new File(fullFilePath));
			FileInputStream in =new FileInputStream(file);
			//Get it from web path
			//http://localhost:8080/eACH/
			ServletOutputStream out = response.getOutputStream();
			out.write(IOUtils.toByteArray(in));
			in.close();
			out.flush();
			out.close();
			//	 要把檔案刪除 
			if(isDeleteFile){
				if(file!= null && file.exists()){
					file.delete();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.debug("exportFile2Client.Exception>>"+e);
		}
	}
	/**
	 * 一開始寫的版本 ，但部分pdf下載時會出現文件已損壞的訊息
	 * @param response
	 * @param fullFilePath
	 * @param filename
	 * @param dow_token
	 */
	public void exportFile2ClientII(HttpServletResponse response , String fullFilePath,String filename ,String dow_token ){
		filename = StrUtils.isEmpty(filename)?zDateHandler.getTheDate()+"_tx_1.pdf":filename;
		//return an application file instead of html page
	     response.setContentType("application/octet-stream");
//		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition","attachment;filename="+filename);
		response.addCookie(new Cookie("fileDownloadToken", dow_token));
//	     response.addCookie(new HttpCookie("fileDownloadToken", dow_token));
		try 
		{
			//Get it from file system
			FileInputStream in = 
					new FileInputStream(new File(fullFilePath));
			
			//Get it from web path
			//http://localhost:8080/eACH/
			ServletOutputStream out = response.getOutputStream();
	        byte[] outputByte = new byte[4096];
//			copy binary content to output stream
	        int bytesRead = -1;
	        while((bytesRead = in.read(outputByte, 0, 4096)) != -1){
	        	out.write(outputByte, 0, 4096);
	        }
			in.close();
			out.flush();
			out.close();
			
//	 TODO 要把檔案刪除 未完成
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 測試用 失敗
	 * @param fullFilePath
	 * @param filename
	 * @param dow_token
	 */
	public void exportFile2ClientIII( String fullFilePath,String filename ,String dow_token ){
		filename = StrUtils.isEmpty(filename)?zDateHandler.getTheDate()+"_tx_1.pdf":filename;
		//return an application file instead of html page
		HttpServletResponse response = WebServletUtils.getResponse();
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition","attachment;filename="+filename);
		response.addCookie(new Cookie("fileDownloadToken", dow_token));
		Document doc = new Document();
//	     response.addCookie(new HttpCookie("fileDownloadToken", dow_token));
		try 
		{
			//Get it from file system
			FileInputStream in = 
					new FileInputStream(new File(fullFilePath));
			//Get it from web path
			//http://localhost:8080/eACH/
			ServletOutputStream out = response.getOutputStream();
//			PdfWriter.getInstance(doc, out);
//			doc.open();
//			doc.add(new Paragraph("Hello Pdf"));
//			doc.close();
			byte[] outputByte = new byte[4096];
			//copy binary content to output stream
			while(in.read(outputByte, 0, 4096) != -1){
				out.write(outputByte, 0, outputByte.length);
			}
			in.close();
			out.flush();
			out.close();
			
//	 TODO 要把檔案刪除 未完成
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	public Map<String,String> checkRPT_BizDate(String bizdate ,String clPhase ){
//		String bizdate = StrUtils.isEmpty(param.get("bizdate")) ?"":param.get("bizdate");
		String clearingPhase = StrUtils.isEmpty(clPhase) || clPhase.equals("all")  ?"01,02":clPhase;
	   EACH_BATCH_NOTIFY_Dao batch_notify_Dao = SpringAppCtxHelper.getBean("batch_notify_Dao");
		Map<String,String> retmap = new HashMap<String,String>();
		List<String> list = Arrays.asList(clearingPhase.split(",")); 
		boolean res = false ;
		retmap.put("result", "TRUE");
		if(StrUtils.isEmpty(bizdate)){
			retmap.put("result", "FALSE");
			retmap.put("msg", "請選擇營業日");
			return retmap;
		}
		bizdate = DateTimeUtils.convertDate(1, bizdate, "yyyyMMdd", "yyyyMMdd");
		for(String str : list){
			StringBuffer sql = new StringBuffer();
			System.out.println("bizdate>>"+bizdate);
			sql.append(" BIZDATE = '"+bizdate+"'");
			sql.append(" AND  CLEARINGPHASE = '"+str.trim()+"'");
			System.out.println("sql.path>>"+sql);
			res = batch_notify_Dao.checkRPT_BizDate(sql.toString());
			if(!res){
				retmap.put("result", "FALSE");
//					retmap.put("msg", "無法列印報表，目前營業日="+bizdate+"清算階段="+str+"尚未發佈結算通知");
				retmap.put("msg", "目前營業日="+bizdate+"，清算階段="+str+"，尚無法列印報表或檔案下載。");
				return retmap;
			}
		}//for end
		System.out.println("checkRPT_BizDate>>"+retmap);
		return retmap;
	}
	
	public Map<String,String> checkRPTCL_BizDate(String bizdate ,String clPhase ){
//		String bizdate = StrUtils.isEmpty(param.get("bizdate")) ?"":param.get("bizdate");
		String clearingPhase = StrUtils.isEmpty(clPhase) || clPhase.equals("all")  ?"01,02":clPhase;
		EACH_BATCH_NOTIFY_Dao batch_notify_Dao = SpringAppCtxHelper.getBean("batch_notify_Dao");
		Map<String,String> retmap = new HashMap<String,String>();
		List<String> list = Arrays.asList(clearingPhase.split(",")); 
		boolean res = false ;
		retmap.put("result", "TRUE");
		if(StrUtils.isEmpty(bizdate)){
			retmap.put("result", "FALSE");
			retmap.put("msg", "請選擇營業日");
			return retmap;
		}
		bizdate = DateTimeUtils.convertDate(1, bizdate, "yyyyMMdd", "yyyyMMdd");
		for(String str : list){
			StringBuffer sql = new StringBuffer();
			System.out.println("bizdate>>"+bizdate);
			sql.append(" BIZDATE = '"+bizdate+"'");
			sql.append(" AND  CLEARINGPHASE = '"+str.trim()+"'");
			System.out.println("sql.path>>"+sql);
			res = batch_notify_Dao.checkRPTCL_BizDate(sql.toString());
			if(!res){
				retmap.put("result", "FALSE");
//					retmap.put("msg", "無法列印報表，目前營業日="+bizdate+"清算階段="+str+"尚未發佈結算通知");
				retmap.put("msg", "目前營業日="+bizdate+"，清算階段="+str+"，尚無法列印報表。");
				return retmap;
			}
		}//for end
		System.out.println("checkRPT_BizDate>>"+retmap);
		return retmap;
	}
	
//	TODO 未完成 輸出.TXT檔
	public void exportTxt(){
//		TODO 要將DB查出來的資料   [PO,PO,PO] 輸出成TXT
//		PrintWriter writer;
//		try {
//			writer = new PrintWriter("the-file-name.txt", "UTF-8");
//			writer.println("The first line");
//			writer.println("The second line");
//			writer.close();
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
	}

	public EACH_FUNC_LIST_Dao getFunc_list_Dao() {
		return func_list_Dao;
	}

	public void setFunc_list_Dao(EACH_FUNC_LIST_Dao func_list_Dao) {
		this.func_list_Dao = func_list_Dao;
	}
	
	
}
