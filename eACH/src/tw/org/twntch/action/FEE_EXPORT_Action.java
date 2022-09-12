package tw.org.twntch.action;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import tw.org.twntch.bo.EACH_USERLOG_BO;
import tw.org.twntch.form.Fee_Export_Form;
import tw.org.twntch.util.CodeUtils;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.zDateHandler;

public class FEE_EXPORT_Action extends GenericAction{
	private CodeUtils codeUtils;
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private EACH_USERLOG_BO userlog_bo;//寫操作軌跡記錄
	
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
		Fee_Export_Form fee_export_form = (Fee_Export_Form) form;
		String ac_key = StrUtils.isEmpty(fee_export_form.getAc_key())?"":fee_export_form.getAc_key();
		String target = StrUtils.isEmpty(fee_export_form.getTarget())?"search":fee_export_form.getTarget();
		fee_export_form.setTarget(target);
		logger.debug("ac_key=" + ac_key);
		logger.debug("target=" + target);
		
		if("DOWNLOADOLD".equalsIgnoreCase(ac_key)){
			//將頁面上的查詢條件放進pkMap
			Map<String,Object> pkMap = new HashMap<String,Object>();
			pkMap.put("serchStrs",fee_export_form.getSerchStrs());
			//如果有錯誤要將訊息放進去
			Map<String,Object> msgMap = new HashMap<String,Object>();
			
			//取得在classpath下的properties檔案
			Map<String,String> valueMap = codeUtils.getPropertyValue("Configuration.properties","feeexportDirPath");
			//無法取得properties
			if(valueMap == null){
				//回頁面alert訊息
				fee_export_form.setMsg("無法取得properties檔");
				
				//寫操作軌跡記錄(失敗)
				msgMap.put("msg",fee_export_form.getMsg());
				userlog_bo.writeFailLog("F",msgMap,null,null,pkMap);
				
				return mapping.findForward(target);
			}
			
			//取得放置檔案的資料夾根目錄
			String feeexportDirPath = valueMap.get("feeexportDirPath");
			logger.debug("feeexportDirPath="+feeexportDirPath);
			
			StringBuffer sql = new StringBuffer();
			String YYMMTT = DateTimeUtils.convertDate(DateTimeUtils.INTERCONVERSION, fee_export_form.getFEEYM(), "yyyy/MM", "yyyyMM");
			String YYMMTT_TW = fee_export_form.getFEEYM().replace("/", "");
			
			sql.append("WITH TEMP AS ( ");
			sql.append("SELECT 	");
			sql.append("RPC.BRBK_ID, ");
			sql.append("SUM(AMT)+SUM(RVSAMT)+COALESCE(");
			sql.append("(SELECT FEE FROM BRBK_FEE_ADJ BFA WHERE VARCHAR(INT(BFA.YYYYMM)+191100) = RPC.YYYYMM AND BFA.BRBK_ID = RPC.BRBK_ID),0) ");
			sql.append("AS AMT ");
			sql.append("FROM EACHUSER.RPCLEARFEETAB RPC LEFT JOIN BANK_BRANCH BR ON RPC.BRBK_ID = BR.BRBK_ID ");
			sql.append("WHERE YYYYMM = '"+YYMMTT+"' ");
			sql.append("GROUP BY YYYYMM, BR.BGBK_ID, RPC.BRBK_ID ");
			sql.append(")"); //TEMP END
//			20160205 edit by hugo req by UAT-20160108-01
			sql.append(",TEMP2 AS ( ");
			
			sql.append("SELECT ");
			sql.append("SUBSTR(TRANS_DATE('"+YYMMTT+"' || '01','T',''), 1, 6) AS YYYYMM, ");
			sql.append("COALESCE((SELECT TCH_ID FROM BANK_BRANCH WHERE BRBK_ID = A.BRBK_ID),'  ') AS TCH_ID, ");
			sql.append("A.BRBK_ID, ");
			//20150410 by 李建利 代號(FEE_CODE)改為507
			sql.append("'507' AS FEE_CODE, ");
			sql.append("(CASE WHEN (SELECT AMT FROM TEMP WHERE BRBK_ID=A.BRBK_ID) < 0 THEN '-' WHEN (SELECT AMT FROM TEMP WHERE BRBK_ID=A.BRBK_ID) > 0 THEN '+' ");
			sql.append("WHEN(SELECT FEE FROM BRBK_FEE_ADJ WHERE YYYYMM='"+YYMMTT_TW+"' AND BRBK_ID=A.BRBK_ID) < 0 THEN '-' ELSE '+' END) AS SIGN, ");
			sql.append("COALESCE( ");
			sql.append("( SELECT ( RIGHT(REPEAT('0',14) || REPLACE(VARCHAR(DECIMAL((  ROUND(DECIMAL((CASE WHEN AMT < 0 THEN -AMT ELSE AMT END), 14, 2), 0)  ), 14, 0)),'.',''), 14) ) ");
			sql.append("AS AMT ");
			sql.append("FROM TEMP WHERE BRBK_ID=A.BRBK_ID ), ");    
			sql.append("( SELECT ( RIGHT(REPEAT('0',14) || REPLACE(VARCHAR(DECIMAL((  ROUND(DECIMAL((CASE WHEN FEE < 0 THEN -FEE ELSE FEE END), 14, 2), 0)  ), 14, 0)),'.',''), 14) ) ");
			sql.append("AS FEE ");
			sql.append("FROM BRBK_FEE_ADJ WHERE YYYYMM='"+YYMMTT_TW+"' AND BRBK_ID=A.BRBK_ID) ) AS AMT, "); 
			sql.append("'                ' AS BLANK "); 
			sql.append("FROM ");
			sql.append("(SELECT BRBK_ID FROM  RPCLEARFEETAB WHERE YYYYMM='"+YYMMTT+"'  AND BRBK_ID <>'0188888' UNION  SELECT BRBK_ID  FROM  BRBK_FEE_ADJ WHERE  YYYYMM='"+YYMMTT_TW+"'  AND BRBK_ID <>'0188888') AS A ");
			sql.append(" ) ");//TEMP2 END
//			20160205 edit by hugo req by UAT-20160108-01
			sql.append(" SELECT * FROM  TEMP2 WHERE  AMT <> '00000000000000' ");

			logger.debug("SQL >> " + sql.toString());
			
			String twYearMonth = DateTimeUtils.convertDate(DateTimeUtils.NOT_INTERCONVERSION, fee_export_form.getFEEYM(), "yyyy/MM", "yyyyMM");
			
			//產生檔案和下載檔案的路徑
			String filePath = feeexportDirPath+"/"+DateTimeUtils.getDateShort(new Date())+"/";
			logger.debug("filePath==>"+filePath);
			//20150410 by 李建利 檔名為：(執行當下的民國年月日)_(匯出資料的年月)_EACHFEE.txt
			String fileName = zDateHandler.getTWDate() + "_" + twYearMonth + "_EACHFEE_OLD.txt";
			//產生檔案和下載檔案的路徑加檔名
			String fullPath = filePath+fileName;
			logger.debug("fullPath==>"+fullPath);
			
			Map<String,Object> columnMap = new HashMap<String,Object>();
			List<String> columnNameList = new ArrayList<String>();
			List<Integer> columnLengthList = new ArrayList<Integer>();
			List<String> columnTypeList = new ArrayList<String>();
			
			//先查資料，因為要算總筆數
			List<Map<String,Object>> queryListMap = codeUtils.queryListMap(sql.toString(), null);
			//TXT的控制首錄
			//20150410 by 李建利 不要首錄
			//String firstRow = "BOFclearfee"+twYearMonth;
			String firstRow = null;
			logger.debug("firstRow==>"+firstRow);
			//TXT的控制尾錄
			String lastRow = "EOF"+codeUtils.padText("number",8,String.valueOf(queryListMap.size())) + codeUtils.padText("string", 38, " ");
			logger.debug("lastRow==>"+lastRow);
			
			columnNameList.add("YYYYMM");
			columnLengthList.add(6);
			columnTypeList.add("string");
			//20150507 HUANGPU 劍麗說要加入交換所代號
			columnNameList.add("TCH_ID");
			columnLengthList.add(2);
			columnTypeList.add("string");
			columnNameList.add("BRBK_ID");
			columnLengthList.add(7);
			columnTypeList.add("string");
			columnNameList.add("FEE_CODE");
			columnLengthList.add(3);
			columnTypeList.add("string");
			columnNameList.add("SIGN");
			columnLengthList.add(1);
			columnTypeList.add("string");
			columnNameList.add("AMT");
			columnLengthList.add(14);
			columnTypeList.add("string");
			columnNameList.add("BLANK");
			columnLengthList.add(16);
			columnTypeList.add("string");
			columnMap.put("columnName",columnNameList);
			columnMap.put("columnLength",columnLengthList);
			columnMap.put("columnType",columnTypeList);
			byte[] byteTXT = null;
			//TXT的Byte[]
//			if(queryListMap.size() == 0){
//				fee_export_form.setMsg("查無資料");
//			}
//			else{
				byteTXT = codeUtils.createTXT(queryListMap,columnMap,firstRow,lastRow);
				//正常
				if(byteTXT != null){
					//檔案放到資料夾下
					try {
						codeUtils.putFileToPath(fullPath,byteTXT);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//頁面塞時間的token
					String downloadToken = fee_export_form.getDownloadToken();
					//將檔案吐到前端頁面
					codeUtils.forDownload(new ByteArrayInputStream(byteTXT),fileName,"fileDownloadToken",downloadToken);
					
					//寫操作軌跡記錄(成功)
					userlog_bo.writeLog("F",null,null,pkMap);
					
					return null;
				}
				//有問題
				else{
					fee_export_form.setMsg("查詢過程出現問題");
				}
//			}
			//寫操作軌跡記錄(失敗)
			msgMap.put("msg",fee_export_form.getMsg());
			userlog_bo.writeFailLog("F",msgMap,null,null,pkMap);
		}
		
		if("DOWNLOAD".equalsIgnoreCase(ac_key)){
			//將頁面上的查詢條件放進pkMap
			Map<String,Object> pkMap = new HashMap<String,Object>();
			pkMap.put("serchStrs",fee_export_form.getSerchStrs());
			//如果有錯誤要將訊息放進去
			Map<String,Object> msgMap = new HashMap<String,Object>();
			
			//取得在classpath下的properties檔案
			Map<String,String> valueMap = codeUtils.getPropertyValue("Configuration.properties","feeexportDirPath");
			//無法取得properties
			if(valueMap == null){
				//回頁面alert訊息
				fee_export_form.setMsg("無法取得properties檔");
				
				//寫操作軌跡記錄(失敗)
				msgMap.put("msg",fee_export_form.getMsg());
				userlog_bo.writeFailLog("F",msgMap,null,null,pkMap);
				
				return mapping.findForward(target);
			}
			
			//取得放置檔案的資料夾根目錄
			String feeexportDirPath = valueMap.get("feeexportDirPath");
			logger.debug("feeexportDirPath="+feeexportDirPath);
			
			StringBuffer sql = new StringBuffer();
			String YYMMTT = DateTimeUtils.convertDate(DateTimeUtils.INTERCONVERSION, fee_export_form.getFEEYM(), "yyyy/MM", "yyyyMM");
			String YYMMTT_TW = fee_export_form.getFEEYM().replace("/", "");
			
			sql.append("WITH TEMP AS ( ");
			sql.append("SELECT 	");
			sql.append("RPC.BRBK_ID, ");
			sql.append("SUM(AMT)+SUM(RVSAMT)+COALESCE(");
			sql.append("(SELECT FEE FROM BRBK_FEE_ADJ BFA WHERE VARCHAR(INT(BFA.YYYYMM)+191100) = RPC.YYYYMM AND BFA.BRBK_ID = RPC.BRBK_ID),0) ");
			sql.append("AS AMT ");
			sql.append("FROM EACHUSER.RPCLEARFEETAB_NW RPC LEFT JOIN BANK_BRANCH BR ON RPC.BRBK_ID = BR.BRBK_ID ");
			sql.append("WHERE YYYYMM = '"+YYMMTT+"' ");
			sql.append("GROUP BY YYYYMM, BR.BGBK_ID, RPC.BRBK_ID ");
			sql.append(")"); //TEMP END
//			20160205 edit by hugo req by UAT-20160108-01
			sql.append(",TEMP2 AS ( ");
			
			sql.append("SELECT ");
			sql.append("SUBSTR(TRANS_DATE('"+YYMMTT+"' || '01','T',''), 1, 6) AS YYYYMM, ");
			sql.append("COALESCE((SELECT TCH_ID FROM BANK_BRANCH WHERE BRBK_ID = A.BRBK_ID),'  ') AS TCH_ID, ");
			sql.append("A.BRBK_ID, ");
			//20150410 by 李建利 代號(FEE_CODE)改為507
			sql.append("'507' AS FEE_CODE, ");
			sql.append("(CASE WHEN (SELECT AMT FROM TEMP WHERE BRBK_ID=A.BRBK_ID) < 0 THEN '-' WHEN (SELECT AMT FROM TEMP WHERE BRBK_ID=A.BRBK_ID) > 0 THEN '+' ");
			sql.append("WHEN(SELECT FEE FROM BRBK_FEE_ADJ WHERE YYYYMM='"+YYMMTT_TW+"' AND BRBK_ID=A.BRBK_ID) < 0 THEN '-' ELSE '+' END) AS SIGN, ");
			sql.append("COALESCE( ");
			sql.append("( SELECT ( RIGHT(REPEAT('0',14) || REPLACE(VARCHAR(DECIMAL((  ROUND(DECIMAL((CASE WHEN AMT < 0 THEN -AMT ELSE AMT END), 14, 2), 0)  ), 14, 0)),'.',''), 14) ) ");
			sql.append("AS AMT ");
			sql.append("FROM TEMP WHERE BRBK_ID=A.BRBK_ID ), ");    
			sql.append("( SELECT ( RIGHT(REPEAT('0',14) || REPLACE(VARCHAR(DECIMAL((  ROUND(DECIMAL((CASE WHEN FEE < 0 THEN -FEE ELSE FEE END), 14, 2), 0)  ), 14, 0)),'.',''), 14) ) ");
			sql.append("AS FEE ");
			sql.append("FROM BRBK_FEE_ADJ WHERE YYYYMM='"+YYMMTT_TW+"' AND BRBK_ID=A.BRBK_ID) ) AS AMT, "); 
			sql.append("'                ' AS BLANK "); 
			sql.append("FROM ");
			sql.append("(SELECT BRBK_ID FROM  RPCLEARFEETAB_NW WHERE YYYYMM='"+YYMMTT+"'  AND BRBK_ID <>'0188888' UNION  SELECT BRBK_ID  FROM  BRBK_FEE_ADJ WHERE  YYYYMM='"+YYMMTT_TW+"'  AND BRBK_ID <>'0188888') AS A ");
			sql.append(" ) ");//TEMP2 END
//			20160205 edit by hugo req by UAT-20160108-01
			sql.append(" SELECT * FROM  TEMP2 WHERE  AMT <> '00000000000000' ");

			logger.debug("SQL >> " + sql.toString());
			
			String twYearMonth = DateTimeUtils.convertDate(DateTimeUtils.NOT_INTERCONVERSION, fee_export_form.getFEEYM(), "yyyy/MM", "yyyyMM");
			
			//產生檔案和下載檔案的路徑
			String filePath = feeexportDirPath+"/"+DateTimeUtils.getDateShort(new Date())+"/";
			logger.debug("filePath==>"+filePath);
			//20150410 by 李建利 檔名為：(執行當下的民國年月日)_(匯出資料的年月)_EACHFEE.txt
			String fileName = zDateHandler.getTWDate() + "_" + twYearMonth + "_EACHFEE.txt";
			//產生檔案和下載檔案的路徑加檔名
			String fullPath = filePath+fileName;
			logger.debug("fullPath==>"+fullPath);
			
			Map<String,Object> columnMap = new HashMap<String,Object>();
			List<String> columnNameList = new ArrayList<String>();
			List<Integer> columnLengthList = new ArrayList<Integer>();
			List<String> columnTypeList = new ArrayList<String>();
			
			//先查資料，因為要算總筆數
			List<Map<String,Object>> queryListMap = codeUtils.queryListMap(sql.toString(), null);
			//TXT的控制首錄
			//20150410 by 李建利 不要首錄
			//String firstRow = "BOFclearfee"+twYearMonth;
			String firstRow = null;
			logger.debug("firstRow==>"+firstRow);
			//TXT的控制尾錄
			String lastRow = "EOF"+codeUtils.padText("number",8,String.valueOf(queryListMap.size())) + codeUtils.padText("string", 38, " ");
			logger.debug("lastRow==>"+lastRow);
			
			columnNameList.add("YYYYMM");
			columnLengthList.add(6);
			columnTypeList.add("string");
			//20150507 HUANGPU 劍麗說要加入交換所代號
			columnNameList.add("TCH_ID");
			columnLengthList.add(2);
			columnTypeList.add("string");
			columnNameList.add("BRBK_ID");
			columnLengthList.add(7);
			columnTypeList.add("string");
			columnNameList.add("FEE_CODE");
			columnLengthList.add(3);
			columnTypeList.add("string");
			columnNameList.add("SIGN");
			columnLengthList.add(1);
			columnTypeList.add("string");
			columnNameList.add("AMT");
			columnLengthList.add(14);
			columnTypeList.add("string");
			columnNameList.add("BLANK");
			columnLengthList.add(16);
			columnTypeList.add("string");
			columnMap.put("columnName",columnNameList);
			columnMap.put("columnLength",columnLengthList);
			columnMap.put("columnType",columnTypeList);
			byte[] byteTXT = null;
			//TXT的Byte[]
//			if(queryListMap.size() == 0){
//				fee_export_form.setMsg("查無資料");
//			}
//			else{
				byteTXT = codeUtils.createTXT(queryListMap,columnMap,firstRow,lastRow);
				//正常
				if(byteTXT != null){
					//檔案放到資料夾下
					try {
						codeUtils.putFileToPath(fullPath,byteTXT);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//頁面塞時間的token
					String downloadToken = fee_export_form.getDownloadToken();
					//將檔案吐到前端頁面
					codeUtils.forDownload(new ByteArrayInputStream(byteTXT),fileName,"fileDownloadToken",downloadToken);
					
					//寫操作軌跡記錄(成功)
					userlog_bo.writeLog("F",null,null,pkMap);
					
					return null;
				}
				//有問題
				else{
					fee_export_form.setMsg("查詢過程出現問題");
				}
//			}
			//寫操作軌跡記錄(失敗)
			msgMap.put("msg",fee_export_form.getMsg());
			userlog_bo.writeFailLog("F",msgMap,null,null,pkMap);
		}
		//預設帶入上個月
		String now = zDateHandler.getTWDate();
		int year = Integer.valueOf(now.substring(0,4));
		int month = Integer.valueOf(now.substring(4,6)) - 1;
		if(month <= 0){month = 12;year--;}
		String newDate = "0" + year + (month<10?"0"+month:month);
		fee_export_form.setFEEYM(DateTimeUtils.convertDate(DateTimeUtils.NOT_INTERCONVERSION, newDate, "yyyyMM", "yyyy/MM"));
		
		target = StrUtils.isEmpty(fee_export_form.getTarget())?"":fee_export_form.getTarget();
		return mapping.findForward(target);
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
