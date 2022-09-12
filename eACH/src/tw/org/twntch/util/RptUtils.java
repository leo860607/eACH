package tw.org.twntch.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import tw.org.twntch.bo.Arguments;
import tw.org.twntch.bo.BAT_RPT_BO;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRTextExporter;
import net.sf.jasperreports.engine.export.JRTextExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRCsvExporterParameter;

public class RptUtils {
	private Logger logger = Logger.getLogger(RptUtils.class.getName());
	public static int COLLECTION = 0;
	public static int SQL = 1;
	public final static int R_PATH = 0;
	public final static int C_PATH = 1;
	private CodeUtils codeUtils;
	private WebServerPath webserverpath ;
	public static String export(int sourceType, String sourceFileName, String outputFileName, Map<String, Object> params, Object dataSource) throws FileNotFoundException{
		String sessionId =  WebServletUtils.getRequest().getSession().getId();
		String sourceFilePath = "", tmpFileDir = "", outputFilePath = "";
		ServletContext context = WebServletUtils.getServletContext();
		if(context != null){
			sourceFilePath = context.getRealPath(Arguments.getStringArg("RPT.XML.PATH") + "\\" + sourceFileName + ".jrxml");
			tmpFileDir = context.getRealPath(Arguments.getStringArg("RPT.PDF.PATH"));
		}
		File file = new File(sourceFilePath);
		if(!file.exists()){
			throw new FileNotFoundException("檔案不存在:" + file.getAbsolutePath());
		}
		file = new File(tmpFileDir);
		if(!file.exists()){
			if(!file.mkdir()){
				throw new SecurityException("PDF暫存資料夾產生失敗:" + file.getAbsolutePath());
			}
		}
		System.out.println("dataSource>>"+dataSource);
		if(dataSource == null){return null;}
		//TODO 如果用暫存資料夾檔名必須另外用時間或uuid來命名  或另外用com_id+user_id命名資料夾 ，檔案加上日期來命名
//		outputFilePath = tmpFileDir + "\\" + outputFileName + ".pdf";
		outputFilePath = tmpFileDir + "\\" + sessionId+"_"+outputFileName + ".pdf";
		
		try{
			//編譯JRXML
			JasperReport report = JasperCompileManager.compileReport(sourceFilePath);
			if(report != null){
				if(sourceType == COLLECTION){
					JRMapCollectionDataSource jrds = new JRMapCollectionDataSource((List)dataSource);
					JasperPrint print = JasperFillManager.fillReport(report, params, jrds);
					JRExporter exporter = new JRPdfExporter();
					exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, outputFilePath);
					exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
					exporter.exportReport();
				}else if(sourceType == SQL){
					Connection con = (Connection)dataSource;
					JasperPrint print = JasperFillManager.fillReport(report, params, con);
					JRExporter exporter = new JRPdfExporter();
					exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, outputFilePath);
					exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
					exporter.exportReport();
				}
				
				System.out.println("outputFilePath>>"+outputFilePath);
				return outputFilePath;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 
	 * @param sourceType 0:List 1:SQL
	 * @param path_type 0:realPath 1:contextPath
	 * @param sourceFileName
	 * @param outputFileName
	 * @param params
	 * @param dataSource
	 * @return
	 * @throws FileNotFoundException
	 */
	public static String export(int sourceType, int path_type, String sourceFileName, String outputFileName, Map<String, Object> params, Object dataSource ) throws FileNotFoundException{
		String sessionId =  WebServletUtils.getRequest().getSession().getId();
		String sourceFilePath = "", tmpFileDir = "", outputFilePath = "" , contextPath="";
		ServletContext context = WebServletUtils.getServletContext();
		if(context != null){
//			sourceFilePath = context.getRealPath(Arguments.getStringArg("RPT.XML.PATH") + "\\" + sourceFileName + ".jrxml");
			sourceFilePath = context.getRealPath(Arguments.getStringArg("RPT.XML.PATH") + "/" + sourceFileName + ".jrxml");
			tmpFileDir = context.getRealPath(Arguments.getStringArg("RPT.PDF.PATH"));
			contextPath = context.getContextPath()+Arguments.getStringArg("RPT.PDF.PATH");
		}
		File file = new File(sourceFilePath);
		if(!file.exists()){
			throw new FileNotFoundException("檔案不存在:" + file.getAbsolutePath());
		}
		file = new File(tmpFileDir);
		if(!file.exists()){
			if(!file.mkdir()){
				throw new SecurityException("PDF暫存資料夾產生失敗:" + file.getAbsolutePath());
			}
		}
		System.out.println("dataSource>>"+dataSource);
		if(dataSource == null){return null;}
		//TODO 如果用暫存資料夾檔名必須另外用時間或uuid來命名  或另外用com_id+user_id命名資料夾 ，檔案加上日期來命名
//		outputFilePath = tmpFileDir + "\\" + outputFileName + ".pdf";
//		outputFilePath = tmpFileDir + "\\" + sessionId+"_"+outputFileName + ".pdf";
		outputFilePath = tmpFileDir + "/" + sessionId+"_"+outputFileName + ".pdf";
		contextPath = contextPath+"/"+sessionId+"_"+outputFileName + ".pdf";
		try{
			//編譯JRXML
			JasperReport report = JasperCompileManager.compileReport(sourceFilePath);
			if(report != null){
				if(sourceType == COLLECTION){
					JRMapCollectionDataSource jrds = new JRMapCollectionDataSource((List)dataSource);
					JasperPrint print = JasperFillManager.fillReport(report, params, jrds);
					JRExporter exporter = new JRPdfExporter();
					exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, outputFilePath);
					exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
					exporter.exportReport();
				}else if(sourceType == SQL){
					Connection con = (Connection)dataSource;
					JasperPrint print = JasperFillManager.fillReport(report, params, con);
					JRExporter exporter = new JRPdfExporter();
					exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, outputFilePath);
					exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
					exporter.exportReport();
				}
				System.out.println("outputFilePath>>"+outputFilePath);
				switch (path_type) {
				case R_PATH:break;
				case C_PATH:outputFilePath = contextPath;break;
				default:break;
				}
				System.out.println("dataSource>>"+dataSource);
				System.out.println("outputFilePath2>>"+outputFilePath);
				return outputFilePath;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 20150313 add by hugo 尚未測試 依據輸入的參數分別產生pdf、xls、txt
	 * 20200408 add by vincenthuang 新增csv
	 * @param sourceType
	 * @param path_type
	 * @param sourceFileName
	 * @param outputFileName
	 * @param params
	 * @param dataSource
	 * @param output_type  1.pdf 2.xls 3.txt 4.csv
	 * @return
	 * @throws FileNotFoundException
	 */
	public static String export(int sourceType, int path_type, String sourceFileName, String outputFileName, Map<String, Object> params, Object dataSource ,Integer output_type) throws FileNotFoundException{
		String sessionId =  WebServletUtils.getRequest().getSession().getId();
		String sourceFilePath = "", tmpFileDir = "", outputFilePath = "" , contextPath="";
		ServletContext context = WebServletUtils.getServletContext();
		if(context != null){
			sourceFilePath = context.getRealPath(Arguments.getStringArg("RPT.XML.PATH") + "/" + sourceFileName + ".jrxml");
			tmpFileDir = context.getRealPath(Arguments.getStringArg("RPT.PDF.PATH"));
			contextPath = context.getContextPath()+Arguments.getStringArg("RPT.PDF.PATH");
		}
		File file = new File(sourceFilePath);
		if(!file.exists()){
			throw new FileNotFoundException("檔案不存在:" + file.getAbsolutePath());
		}
		file = new File(tmpFileDir);
		if(!file.exists()){
			if(!file.mkdir()){
				throw new SecurityException("暫存資料夾產生失敗:" + file.getAbsolutePath());
			}
		}
		System.out.println("dataSource>>"+dataSource);
		if(dataSource == null){return null;}
		//TODO 如果用暫存資料夾檔名必須另外用時間或uuid來命名  或另外用com_id+user_id命名資料夾 ，檔案加上日期來命名
		switch (output_type) {
		case 1:
			outputFilePath = tmpFileDir + "/" + sessionId+"_"+outputFileName + ".pdf";
			contextPath = contextPath+"/"+sessionId+"_"+outputFileName + ".pdf";
			break;
		case 2:
			outputFilePath = tmpFileDir + "/" + sessionId+"_"+outputFileName + ".xls";
			contextPath = contextPath+"/"+sessionId+"_"+outputFileName + ".xls";
			break;
		case 3:
			outputFilePath = tmpFileDir + "/" + sessionId+"_"+outputFileName + ".txt";
			contextPath = contextPath+"/"+sessionId+"_"+outputFileName + ".txt";
			break;
		case 4:
			outputFilePath = tmpFileDir + "/" + sessionId+"_"+outputFileName + ".csv";
			contextPath = contextPath+"/"+sessionId+"_"+outputFileName + ".csv";
			break;
		default:
			outputFilePath = tmpFileDir + "/" + sessionId+"_"+outputFileName + ".pdf";
			contextPath = contextPath+"/"+sessionId+"_"+outputFileName + ".pdf";
			break;
		}
		try{
			//編譯JRXML
			JasperReport report = JasperCompileManager.compileReport(sourceFilePath);
			if(report != null){
				if(sourceType == COLLECTION){
					JRMapCollectionDataSource jrds = new JRMapCollectionDataSource((List)dataSource);
					JasperPrint print = JasperFillManager.fillReport(report, params, jrds);
//					JRExporter exporter = new JRPdfExporter();
					JRExporter exporter = null;
					switch (output_type) {
					case 1:
						 exporter = new JRPdfExporter();
						break;
					case 2:
						 exporter =new JRXlsExporter();
//						 20160407 add by hugo 設定匯出excel時 數字欄位不會轉成文字 以便後續user 可以使用sum()進行運算
						 exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
						break;
					case 3:
						 exporter =new JRTextExporter();
						 Float f = Float.valueOf("8") ;
						 exporter.setParameter(JRTextExporterParameter.CHARACTER_WIDTH, f);
						 exporter.setParameter(JRTextExporterParameter.CHARACTER_HEIGHT, f);
//						 exporter.setParameter(JRTextExporterParameter.PAGE_HEIGHT,80);
//						 exporter.setParameter(JRTextExporterParameter.PAGE_WIDTH,40);
						break;
					case 4:
						 exporter =new JRCsvExporter();
						 exporter.setParameter(JRCsvExporterParameter.CHARACTER_ENCODING,"MS950");			
			      
						break;
					default:
						exporter = new JRPdfExporter();
						break;
					}
					exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, outputFilePath);
					exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
					exporter.exportReport();
				}else if(sourceType == SQL){
					Connection con = (Connection)dataSource;
					JasperPrint print = JasperFillManager.fillReport(report, params, con);
					JRExporter exporter = null;
					switch (output_type) {
					case 1:
						 exporter = new JRPdfExporter();
						break;
					case 2:
						 exporter =new JRXlsExporter();
						break;
					case 3:
						 exporter =new JRTextExporter();
						break;
					case 4:
						exporter =new JRCsvExporter();
						break;
					default:
						exporter = new JRPdfExporter();
						break;
					}
					exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, outputFilePath);
					exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
					exporter.exportReport();
				}
				System.out.println("outputFilePath>>"+outputFilePath);
				switch (path_type) {
				case R_PATH:break;
				case C_PATH:outputFilePath = contextPath;break;
				default:break;
				}
				System.out.println("dataSource>>"+dataSource);
				System.out.println("outputFilePath2>>"+outputFilePath);
			}
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		return outputFilePath;
	}
	/**
	 * 批次專用
	 * @param sourceType
	 * @param path_type
	 * @param sourceFileName
	 * @param outputFileName
	 * @param params
	 * @param dataSource
	 * @param output_type
	 * @return
	 * @throws FileNotFoundException
	 */
	public Map<String,String> bat_export(int sourceType, String sourceFileName, String outputFileName ,String outputPath, Map<String, Object> params, Object dataSource ,List data,Integer output_type) throws FileNotFoundException{
		String sourceFilePath = "", tmpFileDir = "", outputFilePath = "" , contextPath="";
//		ServletContext context = WebServletUtils.getServletContext();
		Map<String,String> map = new HashMap<String,String>();
		map.put("result", "FALSE");
		try{
			System.out.println("bat_export.webserverpath.."+webserverpath);
			logger.debug("bat_export.webserverpath.."+webserverpath);
			webserverpath = (WebServerPath) (webserverpath==null? SpringAppCtxHelper.getBean("webserverpath"):webserverpath);
			sourceFilePath = webserverpath.getServerRootUrl()+"/"+Arguments.getStringArg("RPT.XML.PATH") + "/" + sourceFileName + ".jrxml";
			if(OSValidator.isWindows()){
				tmpFileDir = webserverpath.getServerRootUrl()+"/"+Arguments.getStringArg("RPT.PDF.PATH")+ "/" +outputPath;
				System.out.println("bat_export..is isWindows tmpFileDir="+tmpFileDir);
				logger.debug("bat_export..is isWindows tmpFileDir="+tmpFileDir);
			}else{
				Map<String,String> valueMap = codeUtils.getPropertyValue("Configuration.properties","basedataDirPath","basedataFilePrefix");
				tmpFileDir = valueMap.get("basedataDirPath")+ "/" +outputPath;
				System.out.println("bat_export..is aix tmpFileDir="+tmpFileDir);
				logger.debug("bat_export..is aix tmpFileDir="+tmpFileDir);
			}
			
			
			File file = new File(sourceFilePath);
			if(!file.exists()){
				System.out.println("bat_export..FileNotFound:" + file.getAbsolutePath());
				logger.debug("bat_export..FileNotFound:" + file.getAbsolutePath());
				throw new FileNotFoundException("FileNotFound:" + file.getAbsolutePath());
			}
			file = new File(tmpFileDir);
//			20150817 edit by hugo 主要觀察產生資料夾失敗的情況 ，如產生失敗，重新判斷資料夾是否存在，連續失敗3次就拋出Exception，資料夾存在直接跳出loop料
			int cnt = 1;
			while( cnt <= 3 ){
				if(!file.exists()){
					if(!file.mkdirs()){
						System.out.println("bat_export..creatFile fail:" + file.getAbsolutePath());
						logger.debug("bat_export..creatFile fail:" + file.getAbsolutePath());
						System.out.println("bat_export..creatFile fail cnt:" + cnt);
						logger.debug("bat_export..creatFile fail cnt:" + cnt);
						if(cnt >= 3){
							throw new SecurityException("creatFile fail:" + file.getAbsolutePath());
						}
						cnt++;
					}
				}else{
					System.out.println("File exists break>>" + file.getAbsolutePath());
					logger.debug("File exists break>>" + file.getAbsolutePath());
					break;
				}
			}//while end
			
			
			
			System.out.println("tmpFileDir"+tmpFileDir);
			switch (output_type) {
			case 1:
				outputFilePath = tmpFileDir + "/" +outputFileName + ".pdf";
				break;
			case 2:
				outputFilePath = tmpFileDir + "/" +outputFileName + ".xls";
				break;
			case 3:
				outputFilePath = tmpFileDir + "/" +outputFileName + ".txt";
				break;
			case 4:
				outputFilePath = tmpFileDir + "/" +outputFileName + ".csv";
				break;
				
			default:
				outputFilePath = tmpFileDir + "/" +outputFileName + ".pdf";
				break;
			}
			System.out.println("bat_export..outputFilePath>>"+outputFilePath);
			logger.debug("bat_export..outputFilePath>>"+outputFilePath);
			file = new File(outputFilePath);
			if(file.exists()){
				file.delete();
			}
		
			//編譯JRXML
			JasperReport report = JasperCompileManager.compileReport(sourceFilePath);
			if(report != null){
				if(sourceType == COLLECTION){
					JRMapCollectionDataSource jrds = new JRMapCollectionDataSource(data);
					JasperPrint print = JasperFillManager.fillReport(report, params, jrds);
					JRExporter exporter = null;
					switch (output_type) {
					case 1:
						exporter = new JRPdfExporter();
						break;
					case 2:
						exporter =new JRXlsExporter();
						break;
					case 3:
						exporter =new JRTextExporter();
						Float f = Float.valueOf("8") ;
						exporter.setParameter(JRTextExporterParameter.CHARACTER_WIDTH, f);
						exporter.setParameter(JRTextExporterParameter.CHARACTER_HEIGHT, f);
						break;
					case 4:
						 exporter =new JRCsvExporter();
						 exporter.setParameter(JRCsvExporterParameter.CHARACTER_ENCODING,"MS950");
						break;
						
					default:
						exporter = new JRPdfExporter();
						break;
					}
					exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, outputFilePath);
					exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
					exporter.exportReport();
					map.put("result", "TRUE");
					map.put("outputFilePath", outputFilePath);
					map.put("tmpFileDir", tmpFileDir);
					
				}else if(sourceType == SQL){
					System.out.println("dataSource>>"+dataSource);
					if(dataSource == null){return map;}
					Connection con = (Connection)dataSource;
					JasperPrint print = JasperFillManager.fillReport(report, params, con);
					JRExporter exporter = null;
					switch (output_type) {
					case 1:
						exporter = new JRPdfExporter();
						break;
					case 2:
						exporter =new JRXlsExporter();
						break;
					case 3:
						exporter =new JRTextExporter();
						break;
					case 4:
						exporter =new JRCsvExporter();
						break;
					default:
						exporter = new JRPdfExporter();
						break;
					}
					exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, outputFilePath);
					exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
					exporter.exportReport();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			map.put("result", "FALSE");
			map.put("outputFilePath", outputFilePath);
			logger.debug("bat_export..Exception>>"+e);
		}
		System.out.println("bat_export..map>>"+map);
		logger.debug("bat_export..map>>"+map);
		return map;
	}
	
	
	public CodeUtils getCodeUtils() {
		return codeUtils;
	}
	public void setCodeUtils(CodeUtils codeUtils) {
		this.codeUtils = codeUtils;
	}
	public WebServerPath getWebserverpath() {
		return webserverpath;
	}
	public void setWebserverpath(WebServerPath webserverpath) {
		this.webserverpath = webserverpath;
	}
	
	
	
}
