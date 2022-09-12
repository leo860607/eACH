package tw.org.twntch.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.servlet.http.Cookie;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;

import com.google.gson.Gson;

import net.lingala.zip4j.io.ZipOutputStream;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import tw.org.twntch.bean.RADATA;
import tw.org.twntch.db.dao.hibernate.CommonSpringDao;
import tw.org.twntch.db.dao.hibernate.IKey_Dao;
import tw.org.twntch.socket.HSMSocketClient;
import tw.org.twntch.socket.HSMSuipRequestData;
import tw.org.twntch.socket.HSMSuipResponseData;

public class CodeUtils {
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private CommonSpringDao commonSpringDao;
	private IKey_Dao ikey_Dao;
	// private SD_COMPANY_PROFILE_Dao sd_com_Dao;
	// private SD_COMPANY_PROFILE_HIS_Dao sd_com_his_Dao;
	// private FEE_CODE_NW_Dao fee_code_nw_Dao;
	// private FEE_CODE_Dao fee_code_Dao;
	static Gson gs = new Gson();

	public static String toJson(Object obj) {
		return gs.toJson(obj);
	}

	public static <T> T fromJson(String json, Class<T> to) {
		return gs.fromJson(json, to);
	}

	// 將查出的資料轉成txt的byte[]
	public byte[] createTXT(List<Map<String, Object>> queryListMap, Map<String, Object> columnMap, String firstRow,
			String lastRow) {
		byte[] byteTXT = null;

		// 組字串
		StringBuilder sb = new StringBuilder();
		// 要抓的欄位名稱
		List<String> columnNameList = (List<String>) columnMap.get("columnName");
		// 欄位的值要補到多少的長度
		List<Integer> columnLengthList = (List<Integer>) columnMap.get("columnLength");
		// 欄位的值是什麼型態，影響到排版位置
		List<String> columnTypeList = (List<String>) columnMap.get("columnType");

		// 加入控制首錄
		if (firstRow != null) {
			sb.append(firstRow + "\r\n");
		}
		// 加入本文
		if (queryListMap.size() != 0) {
			for (int x = 0; x < queryListMap.size(); x++) {
				for (int y = 0; y < columnNameList.size(); y++) {
					// 抓出來的欄位值
					String columnValue = String.valueOf(queryListMap.get(x).get(columnNameList.get(y)));
					// logger.debug("columnValue="+columnValue);
					// 補位
					columnValue = padText(columnTypeList.get(y), columnLengthList.get(y), columnValue);
					// logger.debug("columnValue after padText="+columnValue);
					// 塞每一列的資料
					sb.append(columnValue);
					// 如果是每一列最後的值，就換行
					if (y == columnNameList.size() - 1) {
						sb.append("\r\n");
					}
				}
			}
		}
		// 加入控制尾錄
		if (lastRow != null) {
			sb.append(lastRow);
		}
		try {
			// 將SB轉成Byte[]
			// 20160310 edit by hugo req by UAT-2016310-02
			// byteTXT = sb.toString().getBytes("UTF-8");
			byteTXT = sb.toString().getBytes("BIG5");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			byteTXT = sb.toString().getBytes();
		}
		return byteTXT;
	}

	public byte[] createTXT_UTF8(List<Map<String, Object>> queryListMap, Map<String, Object> columnMap, String firstRow,
			String lastRow) {
		byte[] byteTXT = null;

		// 組字串
		StringBuilder sb = new StringBuilder();
		// 要抓的欄位名稱
		List<String> columnNameList = (List<String>) columnMap.get("columnName");
		// 欄位的值要補到多少的長度
		List<Integer> columnLengthList = (List<Integer>) columnMap.get("columnLength");
		// 欄位的值是什麼型態，影響到排版位置
		List<String> columnTypeList = (List<String>) columnMap.get("columnType");

		// 加入控制首錄
		if (firstRow != null) {
			sb.append(firstRow + "\r\n");
		}
		// 加入本文
		if (queryListMap.size() != 0) {
			for (int x = 0; x < queryListMap.size(); x++) {
				for (int y = 0; y < columnNameList.size(); y++) {
					// 抓出來的欄位值
					String columnValue = String.valueOf(queryListMap.get(x).get(columnNameList.get(y)));
					// logger.debug("columnValue="+columnValue);
					// 補位
					columnValue = padText_UTF8(columnTypeList.get(y), columnLengthList.get(y), columnValue);
					// logger.debug("columnValue after padText="+columnValue);
					// 塞每一列的資料
					sb.append(columnValue);
					// 如果是每一列最後的值，就換行
					if (y == columnNameList.size() - 1) {
						sb.append("\r\n");
					}
				}
			}
		}
		// 加入控制尾錄
		if (lastRow != null) {
			sb.append(lastRow);
		}
		try {
			// 將SB轉成Byte[]
			// 20160310 edit by hugo req by UAT-2016310-02
			byteTXT = sb.toString().getBytes("UTF-8");
			// byteTXT = sb.toString().getBytes("BIG5");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			byteTXT = sb.toString().getBytes();
		}
		return byteTXT;
	}

	// 將首尾錄轉成txt的byte[]
	public byte[] createTXTWithoutData(String firstRow, String lastRow) {
		byte[] byteTXT = null;

		// 組字串
		StringBuilder sb = new StringBuilder();
		// 加入控制首錄
		if (firstRow != null) {
			sb.append(firstRow + "\r\n");
		}
		// 加入控制尾錄
		if (lastRow != null) {
			sb.append(lastRow);
		}
		try {
			// 將SB轉成Byte[]
			byteTXT = sb.toString().getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			byteTXT = sb.toString().getBytes();
		}
		return byteTXT;
	}

	// 將資料轉成zip的byte[]
	public byte[] createZIP(List<byte[]> dataList, List<String> filenameList, String zipPassword) {
		byte[] byteZIP = null;
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream);
		try {
			for (int i = 0; i < dataList.size(); i++) {
				// zip的參數
				ZipParameters zipParameters = new ZipParameters();
				zipParameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
				zipParameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
				zipParameters.setSourceExternalStream(true);
				// zip內該TXT的檔名
				logger.debug("filenameList.get(i)=" + filenameList.get(i));
				zipParameters.setFileNameInZip(filenameList.get(i));
				// 如果沒有zipPassword，表示不要加密
				if (zipPassword == null) {
					zipParameters.setEncryptFiles(false);
				}
				// 如果有zipPassword，表示要加密
				else {
					zipParameters.setEncryptFiles(true);
					zipParameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_STANDARD);
					zipParameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_128);
					// 加密的密碼
					zipParameters.setPassword(zipPassword);
				}
				// 將該檔案放入zip
				zipOutputStream.putNextEntry(null, zipParameters);
				zipOutputStream.write(dataList.get(i));
				zipOutputStream.closeEntry();
			}
			// zip完成
			zipOutputStream.finish();
			zipOutputStream.close();
			byteZIP = byteArrayOutputStream.toByteArray();
			byteArrayOutputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		return byteZIP;
	}

	// 將檔案的串流倒回前端頁面
	public void forDownload(InputStream inputStream, String filename, String cookieName, String cookie) {
		try {
			// URLEncoder.encode避免中文顯示錯誤
			WebServletUtils.getResponse().setHeader("Content-Disposition",
					"attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
			if (cookieName != null && cookie != null) {
				// 更新頁面的cookie
				WebServletUtils.getResponse().addCookie(new Cookie(cookieName, cookie));
			}
			// 設定輸出型態為串流
			WebServletUtils.getResponse().setContentType("application/octet-stream");
			OutputStream outStream = WebServletUtils.getResponse().getOutputStream();
			// 將串流輸出到頁面上，使用者才能下載
			byte[] outputByte = new byte[4096];
			int bytesRead = -1;
			while ((bytesRead = inputStream.read(outputByte)) != -1) {
				outStream.write(outputByte, 0, bytesRead);
			}
			inputStream.close();
			WebServletUtils.getResponse().getOutputStream().flush();
			WebServletUtils.getResponse().getOutputStream().close();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}

	// 用Spring查詢資料
	public List<Map<String, Object>> queryListMap(String SQL, Object[] parameter) {
		// 撈出資料
		return commonSpringDao.list(SQL, parameter);
	}

	// 取得Zip檔裡面的檔案
	public InputStream getFileInZIP(String zipPath, String fileName) {
		InputStream inputStream = null;
		try {
			ZipFile zipFile = new ZipFile(zipPath);
			ZipEntry entry = zipFile.getEntry(fileName);
			inputStream = zipFile.getInputStream(entry);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		return inputStream;
	}

	// 將Byte[]檔案放到實體路徑
	public void putFileToPath(String targetPath, byte[] byteFile) throws Exception {
		try {
			// 取得該檔案的絕對路徑
			File file = new File(targetPath);
			// 取得放該檔案的路徑
			File folder = new File(file.getParent());
			// 如果該檔案的路徑不是資料夾，表示資料夾不存在
			if (!folder.isDirectory()) {
				// 創建多階層的資料夾
				folder.mkdirs();
			}
			FileOutputStream fileOutputStream = new FileOutputStream(targetPath);
			fileOutputStream.write(byteFile);
			fileOutputStream.flush();
			fileOutputStream.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.debug("putFileToPath.Exception>>" + ex);
			throw ex;
		}
	}

	// 將實體路徑檔案轉成InputStream
	public InputStream getFileFromPath(String targetPath) {
		InputStream inputStream = null;
		try {
			File fileData = new File(targetPath);
			inputStream = new FileInputStream(fileData);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		return inputStream;
	}

	// 將該路徑下的資料夾和檔案全部刪除(不包含資料夾自己)
	public void delete(File file) {
		// 如果是自己是檔案，就刪除
		if (file.isFile()) {
			file.delete();
		}
		// 如果是自己是資料夾，再往下鑽
		else {
			for (File f : file.listFiles()) {
				delete(f);
			}
		}
	}

	// 算出字串的Byte長度
	// 先將字串轉BIG5的Byte(難字會變問號)，再用BIG5編碼轉回字串，然後比較問號的數量，這樣就可以知道有多少個難字
	// 再加回BIG5的Byte的長度，輸出到txt，全形會跟肉眼看到寬度一樣
	public int findByteLength(String text) {
		int byteLength = 0;
		String newString = "";
		try {
			newString = new String(text.getBytes("BIG5"), "BIG5");
			int hardWordNum = findHardWordNum(text, newString);
			// logger.debug("hardWordNum="+hardWordNum);
			byteLength = text.getBytes("BIG5").length + hardWordNum;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		return byteLength;
	}

	public int findByteLength_UTF8(String text) {
		int byteLength = 0;
		String newString = "";
		try {
			newString = new String(text.getBytes("UTF8"), "UTF8");
			int hardWordNum = findHardWordNum(text, newString);
			// logger.debug("hardWordNum="+hardWordNum);
			byteLength = text.getBytes("UTF8").length + hardWordNum;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		return byteLength;
	}

	public int findHardWordNum(String text, String newString) {
		int hardWordNum = 0;
		int textQNum = 0;
		int newStringQNum = 0;

		for (int x = 0; x < text.length(); x++) {
			if ("?".equals(text.substring(x, x + 1))) {
				textQNum += 1;
			}
		}
		for (int x = 0; x < newString.length(); x++) {
			if ("?".equals(newString.substring(x, x + 1))) {
				newStringQNum += 1;
			}
		}
		hardWordNum = newStringQNum - textQNum;

		return hardWordNum;
	}

	// 補位
	public String padText(String textType, Integer expectLength, String textValue) {
		// 如果textValue先被String.valueOf轉過，null會變成'null'，所以也要考慮進去
		if (textValue == null || textValue.equalsIgnoreCase("null")) {
			if (textType.contains("string")) {
				textValue = "";
			} else if (textType.contains("number")) {
				textValue = "0";
			} else if (textType.contains("decimal")) {
				textValue = "0";
			} else {
				textValue = "";
			}
		}

		// 左靠右補空白
		if (textType.contains("string")) {
			// 如果傳進來的字串比需要的長度還要長，就把右邊的字串截斷
			while (findByteLength(textValue) > expectLength) {
				textValue = textValue.substring(0, textValue.length() - 1);
			}
			while (findByteLength(textValue) < expectLength) {
				textValue = textValue + " ";
			}
			// logger.debug("textValue after padding="+textValue);
		}
		// 右靠左補零
		else if (textType.contains("number")) {
			// 如果傳進來的數字比需要的長度還要長，就把左邊的數字截斷
			while (findByteLength(textValue) > expectLength) {
				// 某些金額的值前面會有負號
				if ("-".equals(textValue.substring(0, 1))) {
					textValue = "-" + textValue.substring(2);
				} else {
					textValue = textValue.substring(1);
				}
			}
			while (findByteLength(textValue) < expectLength) {
				// 某些金額的值前面會有負號
				if ("-".equals(textValue.substring(0, 1))) {
					textValue = "-0" + textValue.substring(1);
				} else {
					textValue = "0" + textValue;
				}
			}
			// logger.debug("textValue after padding="+textValue);
		}
		// 有小數點，小數點右邊數字靠左右補0；小數點左邊數字靠右左補0。例如textValue='55.04'，textType='decimal(3)'，3為小數位數
		// 假如6為總長度，補位完之後為'055040'
		else if (textType.contains("decimal")) {
			String decimalString = "";
			// 有小數點
			if (textValue.indexOf(".") != -1) {
				decimalString = textValue.substring(textValue.indexOf(".") + 1);
				textValue = textValue.substring(0, textValue.indexOf("."));
			}
			// logger.debug("decimalString before padding="+decimalString);
			// logger.debug("textValue before padding="+textValue);
			// 總長度
			int totalLength = Integer.valueOf(expectLength);
			// 小數位數
			int decimalLength = Integer.valueOf(textType.substring(textType.indexOf("(") + 1, textType.indexOf(")")));

			// logger.debug("totalLength="+totalLength);
			// logger.debug("decimalLength="+decimalLength);

			// 如果需要的小數位數長度比實際小數位數右邊的位數還要少的話，把右邊多的位數截掉
			while (findByteLength(decimalString) > decimalLength) {
				decimalString = decimalString.substring(0, decimalString.length() - 1);
			}
			// 小數位數右邊的右邊補零
			while (findByteLength(decimalString) < decimalLength) {
				decimalString += "0";
			}
			// logger.debug("decimalString after padding="+decimalString);
			// 如果需要的數字長度比實際數字長度還要少的話，把左邊多的位數截掉
			while (findByteLength(textValue) > (totalLength - decimalLength)) {
				// 某些金額的值前面會有負號
				if ("-".equals(textValue.substring(0, 1))) {
					textValue = "-" + textValue.substring(2);
				} else {
					textValue = textValue.substring(1);
				}
			}
			// 小數位數左邊的左邊補零
			while (findByteLength(textValue) < (totalLength - decimalLength)) {
				// 某些金額的值前面會有負號
				if (!"".equals(textValue) && "-".equals(textValue.substring(0, 1))) {
					textValue = "-0" + textValue.substring(1);
				} else {
					textValue = "0" + textValue;
				}
			}

			// logger.debug("textValue after padding="+textValue);
			// 組合起來
			textValue += decimalString;
		}
		// 左靠右補空白
		else {
			while (findByteLength(textValue) < expectLength) {
				textValue = textValue + " ";
			}
		}
		return textValue;
	}

	// 補位
	public String padText_UTF8(String textType, Integer expectLength, String textValue) {
		// 如果textValue先被String.valueOf轉過，null會變成'null'，所以也要考慮進去
		if (textValue == null || textValue.equalsIgnoreCase("null")) {
			if (textType.contains("string")) {
				textValue = "";
			} else if (textType.contains("number")) {
				textValue = "0";
			} else if (textType.contains("decimal")) {
				textValue = "0";
			} else {
				textValue = "";
			}
		}

		// 左靠右補空白
		if (textType.contains("string")) {
			// 如果傳進來的字串比需要的長度還要長，就把右邊的字串截斷
			while (findByteLength_UTF8(textValue) > expectLength) {
				textValue = textValue.substring(0, textValue.length() - 1);
			}
			while (findByteLength_UTF8(textValue) < expectLength) {
				textValue = textValue + " ";
			}
			// logger.debug("textValue after padding="+textValue);
		}
		// 右靠左補零
		else if (textType.contains("number")) {
			// 如果傳進來的數字比需要的長度還要長，就把左邊的數字截斷
			while (findByteLength_UTF8(textValue) > expectLength) {
				// 某些金額的值前面會有負號
				if ("-".equals(textValue.substring(0, 1))) {
					textValue = "-" + textValue.substring(2);
				} else {
					textValue = textValue.substring(1);
				}
			}
			while (findByteLength_UTF8(textValue) < expectLength) {
				// 某些金額的值前面會有負號
				if ("-".equals(textValue.substring(0, 1))) {
					textValue = "-0" + textValue.substring(1);
				} else {
					textValue = "0" + textValue;
				}
			}
			// logger.debug("textValue after padding="+textValue);
		}
		// 有小數點，小數點右邊數字靠左右補0；小數點左邊數字靠右左補0。例如textValue='55.04'，textType='decimal(3)'，3為小數位數
		// 假如6為總長度，補位完之後為'055040'
		else if (textType.contains("decimal")) {
			String decimalString = "";
			// 有小數點
			if (textValue.indexOf(".") != -1) {
				decimalString = textValue.substring(textValue.indexOf(".") + 1);
				textValue = textValue.substring(0, textValue.indexOf("."));
			}
			// logger.debug("decimalString before padding="+decimalString);
			// logger.debug("textValue before padding="+textValue);
			// 總長度
			int totalLength = Integer.valueOf(expectLength);
			// 小數位數
			int decimalLength = Integer.valueOf(textType.substring(textType.indexOf("(") + 1, textType.indexOf(")")));

			// logger.debug("totalLength="+totalLength);
			// logger.debug("decimalLength="+decimalLength);

			// 如果需要的小數位數長度比實際小數位數右邊的位數還要少的話，把右邊多的位數截掉
			while (findByteLength_UTF8(decimalString) > decimalLength) {
				decimalString = decimalString.substring(0, decimalString.length() - 1);
			}
			// 小數位數右邊的右邊補零
			while (findByteLength_UTF8(decimalString) < decimalLength) {
				decimalString += "0";
			}
			// logger.debug("decimalString after padding="+decimalString);
			// 如果需要的數字長度比實際數字長度還要少的話，把左邊多的位數截掉
			while (findByteLength_UTF8(textValue) > (totalLength - decimalLength)) {
				// 某些金額的值前面會有負號
				if ("-".equals(textValue.substring(0, 1))) {
					textValue = "-" + textValue.substring(2);
				} else {
					textValue = textValue.substring(1);
				}
			}
			// 小數位數左邊的左邊補零
			while (findByteLength_UTF8(textValue) < (totalLength - decimalLength)) {
				// 某些金額的值前面會有負號
				if (!"".equals(textValue) && "-".equals(textValue.substring(0, 1))) {
					textValue = "-0" + textValue.substring(1);
				} else {
					textValue = "0" + textValue;
				}
			}

			// logger.debug("textValue after padding="+textValue);
			// 組合起來
			textValue += decimalString;
		}
		// 左靠右補空白
		else {
			while (findByteLength_UTF8(textValue) < expectLength) {
				textValue = textValue + " ";
			}
		}
		return textValue;
	}

	// 將message相加並換行
	public String appendMessage(String message, String text) {
		if ("".equals(message)) {
			message = text;
		} else {
			message = message + "，" + text;
		}
		return message;
	}

	// 用JDBC連線取得不同Schema的資料表資料
	public ResultSet useJDBCGetResultSet(String className, String url, String username, String password, String SQL,
			Map<String, Object> parameterMap) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			// 註冊class
			Class.forName(className);
			// 取得連線
			connection = DriverManager.getConnection(url, username, password);
			// 直接用SQL查
			if (parameterMap == null) {
				statement = connection.createStatement();
				resultSet = statement.executeQuery(SQL);
			}
			// 用PreparedStatement SQL查
			else {
				preparedStatement = connection.prepareStatement(SQL);
				// ?的值
				List<Object> questionList = (List<Object>) parameterMap.get("question");
				// 欄位的型態
				List<String> columnTypeList = (List<String>) parameterMap.get("columnType");
				// 塞參數
				for (int x = 0; x < questionList.size(); x++) {
					switch (columnTypeList.get(x)) {
					case "string":
						preparedStatement.setString(x + 1, (String) questionList.get(x));
						break;
					case "int":
						preparedStatement.setInt(x + 1, (Integer) questionList.get(x));
						break;
					default:
						preparedStatement.setObject(x + 1, questionList.get(x));
					}
				}
				resultSet = preparedStatement.executeQuery();
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		return resultSet;
	}

	// 取得properties檔案的值
	public Map<String, String> getPropertyValue(String propertyName, String... keys) {
		Map<String, String> valueMap = new HashMap<String, String>();
		try {
			System.out.println("propertyName>>" + propertyName);
			System.out.println("getClassLoader>>" + this.getClass().getClassLoader());
			System.out.println(
					"getResourceAsStream>>" + this.getClass().getClassLoader().getResourceAsStream(propertyName));
			logger.debug("propertyName>>" + propertyName);
			logger.debug("getClassLoader>>" + this.getClass().getClassLoader());
			// 取得在classpath下的properties檔案
			Properties properties = new Properties();
			// 20160107 edit by hugo 解決properties檔中 中文亂碼問題
			// properties.load(this.getClass().getClassLoader().getResourceAsStream(propertyName));
			properties.load(
					new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream(propertyName), "UTF-8"));
			// 塞值
			for (String key : keys) {
				valueMap.put(key, properties.getProperty(key));
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return null;
		}
		return valueMap;
	}

	// IKey驗證
	public Map<String, String> iKeyVerification(String subCn) {
		logger.debug("subCn = " + subCn);
		Map<String, String> rtnMap = new HashMap<String, String>();
		rtnMap.put("result", "FALSE");
		String sno = null;
		RADATA raData = null;
		// 憑證狀態
		String[] statusCode = { "已註冊，未申請", "有效", "暫禁中", "已過期", "已註銷", "登記中", "已登記", "不明" };

		// 取得憑證資料
		try {
			String SQL = "select * from DB2ACHNRA.CertInfo where ci_SubCn=?";
			Object[] paramObject = new Object[1];
			paramObject[0] = subCn;
			System.out.println("ikey_Dao1>>" + ikey_Dao);
			ikey_Dao = (IKey_Dao) (ikey_Dao == null ? SpringAppCtxHelper.getBean("ikey_Dao") : ikey_Dao);
			System.out.println("ikey_Dao2>>" + ikey_Dao);
			List<Map<String, Object>> dataList = ikey_Dao.list(SQL, paramObject);
			if (dataList != null && dataList.size() > 0) {
				for (Map map : dataList) {
					raData = new RADATA();
					System.out.println("map>>>>" + map);
					BeanUtils.populate(raData, map);
				}
			} else {
				rtnMap.put("msg", "驗證失敗：查無相關資料");
				return rtnMap;
			}
			// 憑證狀態
			if (raData != null) {
				logger.debug("組織名稱: " + raData.getCI_COMPANYID() + " - " + raData.getCI_COMPANYNAME());
				logger.debug("員工編號: " + raData.getCI_EMPLOYEEID());
				logger.debug("憑證CN: " + raData.getCI_SUBCN());
				logger.debug("中文姓名: " + raData.getCI_USERCHNNAME());
				logger.debug("電子郵件: " + raData.getCI_USEREMAIL());
				logger.debug("憑證狀態: " + raData.getCI_SIGNCERTSTATUS() + " - "
						+ statusCode[Integer.parseInt(raData.getCI_SIGNCERTSTATUS())]);
				logger.debug("憑證效期 : " + raData.getCI_SIGNCERTNOTBEFORE() + "~" + raData.getCI_SIGNCERTNOTAFTER());
			} else {
				logger.debug("raData>>" + raData);
				rtnMap.put("msg", "驗證失敗：查無相關資料，raData>>" + raData);
				return rtnMap;
			}

			// //非有效憑證
			if (!"1".equals(raData.getCI_SIGNCERTSTATUS())) {
				logger.debug("登入失敗：憑證狀態非有效，狀態為「 " + statusCode[Integer.parseInt(raData.getCI_SIGNCERTSTATUS())] + "」!");
				rtnMap.put("msg",
						"驗證失敗：憑證狀態非有效，狀態為「 " + statusCode[Integer.parseInt(raData.getCI_SIGNCERTSTATUS())] + "」!");
			}
			// 有效憑證
			else {
				// 接下來判斷有效日期
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
				SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				// 有效日期
				Date certVailDate = sdf.parse(raData.getCI_SIGNCERTNOTAFTER());

				// 有效日期小於今天
				if (certVailDate.before(new Date())) {
					logger.debug("登入失敗：核驗憑證到期日為「" + sdf2.format(certVailDate) + "」，已過期!");
					rtnMap.put("msg", "驗證失敗：核驗憑證到期日為「" + sdf2.format(certVailDate) + "」，已過期!");
				}
				// 有效日期大於今天
				else {
					// 成功
					logger.debug("SUCCESS!");
					// 將IKey的有效日期傳回頁面，登入後還要用
					rtnMap.put("ikeyValidateDate", sdf2.format(certVailDate));
					sno = raData.getCI_SIGNCERTSERIALNO();
					rtnMap.put("result", "TRUE");
					// 將IKey的序號傳回頁面，頁面送交後要拿來作PKCS7驗證
					rtnMap.put("msg", sno);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			rtnMap.put("msg", "驗證失敗：" + e.toString());
		}

		return rtnMap;
	}

	// Hard-Coded Password (CWE 259) 先註解不使用
	//
	// public Map<String,String> iKeyVerification_bak(String subCn){
	// logger.debug("subCn = " + subCn);
	// Map<String, String> rtnMap = new HashMap<String,String>();
	// rtnMap.put("result", "FALSE");
	// String sno = null;
	// RADATA raData = null;
	// //憑證狀態
	// String[] statusCode = {"已註冊，未申請","有效","暫禁中","已過期","已註銷","登記中","已登記","不明"};
	//
	// //取得憑證資料
	// try{
	//// 20150422 edit by hugo 先統一把設定檔改成同一份，避免換版要改很多設定檔，之後有時間再改成JNDI
	//// Map<String,String> valueMap =
	// getPropertyValue("IKey.properties","className","url","username","password");
	// Map<String,String> valueMap = SpringAppCtxHelper.getBean("ikey");
	// //可以取得IKey.properties檔
	// if(valueMap != null){
	// logger.debug(String.format("className=%s,url=%s,username=%s,password=%s",valueMap.get("className"),valueMap.get("url"),valueMap.get("username"),valueMap.get("password")));
	// String SQL = "select * from DB2ACHNRA.CertInfo where ci_SubCn=?";
	// Map<String,Object> parameterMap = new HashMap<String,Object>();
	// List<Object> questionList = new ArrayList<Object>();
	// List<String> columnTypeList = new ArrayList<String>();
	// questionList.add(subCn);
	// columnTypeList.add("string");
	// parameterMap.put("question",questionList);
	// parameterMap.put("columnType",columnTypeList);
	// //用JDBC連線取得不同Schema的資料表資料
	// ResultSet resultSet =
	// useJDBCGetResultSet(valueMap.get("className"),valueMap.get("url"),valueMap.get("username"),valueMap.get("password"),SQL,parameterMap);
	// //取得結果集
	// if(resultSet != null){
	// raData = new RADATA();
	// //如果有資料的話，塞進Bean class
	// while(resultSet.next()){
	// raData.setCiIDENTIFIER(resultSet.getInt("CI_IDENTIFIER"));
	// raData.setCiUSERIDNO(resultSet.getString("CI_USERIDNO"));
	// raData.setCiUSERSEQNO(resultSet.getString("CI_USERSEQNO"));
	// raData.setCiUSEROPTNO(resultSet.getString("CI_USEROPTNO"));
	// raData.setCiCERTKEYUSAGE(resultSet.getString("CI_CERTKEYUSAGE"));
	// raData.setCiCSR_P12(resultSet.getString("CI_CSR_P12"));
	// raData.setCiCATYPE(resultSet.getString("CI_CATYPE"));
	// raData.setCiSUBCN(resultSet.getString("CI_SUBCN"));
	// raData.setCiSUBCOUNTRY(resultSet.getString("CI_SUBCOUNTRY"));
	// raData.setCiUSEREMAIL(resultSet.getString("CI_USEREMAIL"));
	// raData.setCiRA(resultSet.getString("CI_RA"));
	// raData.setCiSUBDN(resultSet.getString("CI_SUBDN"));
	// raData.setCiSIGNCERTSERIALNO(resultSet.getString("CI_SIGNCERTSERIALNO"));
	// raData.setCiCERTSERIALNO(resultSet.getString("CI_CERTSERIALNO"));
	// raData.setCiSIGNCERTSTATUS(resultSet.getString("CI_SIGNCERTSTATUS"));
	// raData.setCiCERTSTATUS(resultSet.getString("CI_CERTSTATUS"));
	// raData.setCiSIGNCERTNOTAFTER(resultSet.getString("CI_SIGNCERTNOTAFTER"));
	// raData.setCiCERTNOTAFTER(resultSet.getString("CI_CERTNOTAFTER"));
	// raData.setCiSIGNCERTNOTBEFORE(resultSet.getString("CI_SIGNCERTNOTBEFORE"));
	// raData.setCiCERTNOTBEFORE(resultSet.getString("CI_CERTNOTBEFORE"));
	// raData.setCiCHALLENGEPHRASE(resultSet.getString("CI_CHALLENGEPHRASE"));
	// raData.setCiSIGNAPPLYID(resultSet.getString("CI_SIGNAPPLYID"));
	// raData.setCiAPPLYID(resultSet.getString("CI_APPLYID"));
	// raData.setCiSIGNCERT(resultSet.getString("CI_SIGNCERT"));
	// raData.setCiCERT(resultSet.getString("CI_CERT"));
	// raData.setCiREVOKEREASON(resultSet.getString("CI_REVOKEREASON"));
	// raData.setCiREVOKEMEMO(resultSet.getString("CI_REVOKEMEMO"));
	// raData.setCiENABLE(resultSet.getString("CI_ENABLE"));
	// raData.setCiLASTUPDATETIME(resultSet.getString("CI_LASTUPDATETIME"));
	// raData.setCiLASTUPDATESOURCE(resultSet.getString("CI_LASTUPDATESOURCE"));
	// raData.setCiOPSTATUS(resultSet.getString("CI_OPSTATUS"));
	// raData.setCiOPRRAO(resultSet.getString("CI_OPRRAO"));
	// raData.setCiCNFRAO(resultSet.getString("CI_CNFRAO"));
	// raData.setCiGROUPID(resultSet.getString("CI_GROUPID"));
	// raData.setCiUSERCHNNAME(resultSet.getString("CI_USERCHNNAME"));
	// raData.setCiCOMPANYNAME(resultSet.getString("CI_COMPANYNAME"));
	// raData.setCiCOMPANYID(resultSet.getString("CI_COMPANYID"));
	// raData.setCiEMPLOYEEID(resultSet.getString("CI_EMPLOYEEID"));
	// raData.setCiUSERPHONE(resultSet.getString("CI_USERPHONE"));
	// raData.setCiIKEYSNO(resultSet.getString("CI_IKEYSNO"));
	// raData.setCiUSERPHONEEXT(resultSet.getString("CI_USERPHONEEXT"));
	// raData.setCiLOGINPWD(resultSet.getString("CI_LOGINPWD"));
	// raData.setCiLOGINERRORTIMES(resultSet.getString("CI_LOGINERRORTIMES"));
	// raData.setCiSIGNCERTCHAIN(resultSet.getString("CI_SIGNCERTCHAIN"));
	// }
	// }
	// //無法取得結果集
	// else{
	// logger.error("Cannot get resultset : \nclassName = " +
	// valueMap.get("className") + "\nurl = " + valueMap.get("url") + "\nusername =
	// " + valueMap.get("username") + "\npassword = " + valueMap.get("password"));
	// rtnMap.put("msg", "驗證失敗：連線異常!");
	// return rtnMap;
	// }
	// }
	// //無法取得IKey.properties檔
	// else{
	// logger.error("Cannot get IKey.properties!");
	// rtnMap.put("msg", "驗證失敗：無法取得連線設定檔!");
	// return rtnMap;
	// }
	//
	// //憑證狀態
	// logger.debug("組織名稱: " + raData.getCiCOMPANYID() + " - " +
	// raData.getCiCOMPANYNAME() );
	// logger.debug("員工編號: " + raData.getCiEMPLOYEEID() );
	// logger.debug("憑證CN: " + raData.getCiSUBCN() );
	// logger.debug("中文姓名: " + raData.getCiUSERCHNNAME() );
	// logger.debug("電子郵件: " + raData.getCiUSEREMAIL() );
	// logger.debug("憑證狀態: " + raData.getCiSIGNCERTSTATUS() + " - " +
	// statusCode[Integer.parseInt(raData.getCiSIGNCERTSTATUS())] );
	// logger.debug("憑證效期 : " + raData.getCiSIGNCERTNOTBEFORE() + "~" +
	// raData.getCiSIGNCERTNOTAFTER() );
	// //非有效憑證
	// if(!"1".equals(raData.getCiSIGNCERTSTATUS())){
	// logger.debug("登入失敗：憑證狀態非有效，狀態為「
	// "+statusCode[Integer.parseInt(raData.getCiSIGNCERTSTATUS())]+"」!");
	// rtnMap.put("msg", "驗證失敗：憑證狀態非有效，狀態為「
	// "+statusCode[Integer.parseInt(raData.getCiSIGNCERTSTATUS())]+"」!");
	// }
	// //有效憑證
	// else{
	// //接下來判斷有效日期
	// SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
	// SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	// //有效日期
	// Date certVailDate = sdf.parse(raData.getCiSIGNCERTNOTAFTER());
	//
	// //有效日期小於今天
	// if(certVailDate.before(new Date())){
	// logger.debug("登入失敗：核驗憑證到期日為「"+ sdf2.format(certVailDate) +"」，已過期!");
	// rtnMap.put("msg", "驗證失敗：核驗憑證到期日為「"+ sdf2.format(certVailDate) +"」，已過期!");
	// }
	// //有效日期大於今天
	// else{
	// //成功
	// logger.debug("SUCCESS!");
	// //將IKey的有效日期傳回頁面，登入後還要用
	// rtnMap.put("ikeyValidateDate",sdf2.format(certVailDate));
	// sno = raData.getCiSIGNCERTSERIALNO();
	// rtnMap.put("result", "TRUE");
	// //將IKey的序號傳回頁面，頁面送交後要拿來作PKCS7驗證
	// rtnMap.put("msg", sno);
	// }
	// }
	// }
	// catch(Exception e){
	// e.printStackTrace();
	// logger.error(e.getMessage());
	// rtnMap.put("msg", "驗證失敗：" + e.toString());
	// }
	//
	// return rtnMap;
	// }
	// 產出HSM的Response
	public HSMSuipResponseData getHSMResponse(String keyId, String macData, String hsmIP, int hsmPort, String keyFlag) {
		HSMSocketClient hsmSocketClient = new HSMSocketClient();
		HSMSuipRequestData hsmSuipRequestData = new HSMSuipRequestData();
		HSMSuipResponseData hsmSuipResponseData;
		// 20150129txmonsum
		String data = "";
		// "2015012900000000"; //System Date+補8個0共16位
		// String icv = zDateHandler.getDateNum()+"00000000";
		String icv = zDateHandler.getDateNum();
		try {
			// data = date+funcId;
			data = macData;
			hsmSuipRequestData.setIcv(icv);
			hsmSuipRequestData.setKeyId(keyId);
			// 20150129txmonsum
			hsmSuipRequestData.setData(data);
			hsmSuipRequestData.setKeyFlag(keyFlag);
			hsmSuipResponseData = hsmSocketClient.send2HSM(hsmIP, hsmPort, hsmSuipRequestData);
			logger.debug("hsmSuipRequestData>>" + hsmSuipRequestData.toString());
			if (hsmSuipResponseData != null) {
				logger.debug("hsmSuipResponseData>>" + hsmSuipResponseData.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return null;
		}
		return hsmSuipResponseData;
	}

	// 將byte[]轉成Hexstring
	public String bytesToHex(byte[] bytes) {
		char[] hexArray = "0123456789ABCDEF".toCharArray();
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}

	public CommonSpringDao l() {
		return commonSpringDao;
	}

	public void setCommonSpringDao(CommonSpringDao commonSpringDao) {
		this.commonSpringDao = commonSpringDao;
	}

	public IKey_Dao getIkey_Dao() {
		return ikey_Dao;
	}

	public void setIkey_Dao(IKey_Dao ikey_Dao) {
		this.ikey_Dao = ikey_Dao;
	}

	/**
	 * 測試用的
	 * 
	 * @throws UnsupportedEncodingException
	 */
	public void StrTest() throws UnsupportedEncodingException {
		Map<String, String> map = this.getPropertyValue("Configuration.properties", "agent_bat_str");
		String json = map.get("agent_bat_str");
		Map<String, String> strmap = new Gson().fromJson(json, Map.class);
		Map<String, String> accmap = null;
		for (String key : strmap.keySet()) {
			System.out.println(strmap.get(key));
		}
		// accmap.put("str1", strmap.get("str1").replace("#1", "執行中")) ;
		// System.out.println(accmap.get("str1"));
		accmap = process(strmap, "str1", "#1", "執行中");
		System.out.println("str1>>" + accmap.get("str1"));
		accmap = process(accmap, "str1", "#2", "20:00:00");
		System.out.println("str1>>" + accmap.get("str1"));
		for (String key : accmap.keySet()) {
			System.out.println(strmap.get(key));
		}
		accmap = process(accmap, "str1", "#3", "21:00:00");
		accmap = process(accmap, "str1", "@執行中", "成功");
		System.out.println("str1>>" + accmap.get("str1"));
		accmap = process_all(accmap);
		for (String key : accmap.keySet()) {
			System.out.println(strmap.get(key));
		}

	}

	public TreeMap<String, String> process(Map<String, String> strmap, String key, String oldChar, String newChar) {
		TreeMap<String, String> map = new TreeMap<String, String>();
		if (StrUtils.isNotEmpty(key)) {
			strmap.put(key, strmap.get(key).replace(oldChar, "@" + newChar));
		}
		map.putAll(strmap);
		return map;

	}

	public TreeMap<String, String> process_all(Map<String, String> strmap) {
		TreeMap<String, String> map = new TreeMap<String, String>();
		for (String tmpkey : strmap.keySet()) {
			map.put(tmpkey,
					strmap.get(tmpkey).replace("@", "").replace("#1", "未執行").replace("#2", "").replace("#3", ""));
		}
		return map;

	}

	public static <T> T objectCovert(Class<T> to, Object from) {
		String json = null;
		if (from == null) {
			return null;
		}
		json = gs.toJson(from);
		T t = gs.fromJson(json, to);

		return t;
	}

	public static String escape(String s) {

		try {
			s = StringEscapeUtils.escapeSql(s);
			// s = StringEscapeUtils.escapeHtml(s);
			// s = StringEscapeUtils.escapeXml(s);
		} catch (Exception e) {
			e.printStackTrace();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return s;
	}

	public static <T> List<T> escapeList(List<T> s) {
		List<T> l = new LinkedList();
		Class<Object> T;
		String json = "";
		String vStr = "";
		try {
			for (T each : s) {
				json = new Gson().toJson(each);
				vStr = StringEscapeUtils.escapeSql(json);
//				vStr = StringEscapeUtils.escapeHtml(json);
				// System.out.println("JSON :"+json);
				System.out.println("VSTR :" + vStr);
				if (json.equals(vStr)) {
					l.add(each);
				} else {
					System.out.println("validate failed");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return l;
	}

	public static Map<String, String> escapeStringMap(Map<String, String> map) {
		Map<String, String> returnMap = new HashMap<String, String>();
		String vStr = "";
		for (String key : map.keySet()) {
			vStr = StringEscapeUtils.escapeSql(map.get(key));
			returnMap.put(key, vStr);
		}
		return returnMap;
	}

	public static File escapeFile(File file) {
		FileInputStream is = null;
		FileOutputStream os = null;
		byte[] content = null;
		String dataStr = "";
		String vStr = "";

		try {
			is = new FileInputStream(file);

			byte[] data = new byte[is.available()];
			int pos = 0;
			while (true) {
				int amt = is.read(data, pos, data.length - pos);
				if (amt <= 0) {
					break;
				}
				pos += amt;
				int avail = is.available();
				if (avail > data.length - pos) {
					byte[] newData = new byte[(pos + avail)];
					System.arraycopy(data, 0, newData, 0, pos);
					data = newData;
				}
			}

			is.close();
			dataStr = new String(data, StandardCharsets.UTF_8);
			vStr = StringEscapeUtils.escapeSql(dataStr);
//			vStr = StringEscapeUtils.escapeHtml(dataStr);
			content = vStr.getBytes();
			os = new FileOutputStream(file, false);
			os.write(content);
			os.flush();
			os.close();
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		} finally {
			close(is);
			close(os);
		}
		return file;
	}

	public static File[] escapeFile2(File[] fileArrary) {
		List<File> listOfFile = new ArrayList<File>();
		for (File each : fileArrary) {
			FileInputStream is = null;
			FileOutputStream os = null;
			byte[] content = null;
			String dataStr = "";
			String vStr = "";

			try {
				is = new FileInputStream(each);

				byte[] data = new byte[is.available()];
				int pos = 0;
				while (true) {
					int amt = is.read(data, pos, data.length - pos);
					if (amt <= 0) {
						break;
					}
					pos += amt;
					int avail = is.available();
					if (avail > data.length - pos) {
						byte[] newData = new byte[(pos + avail)];
						System.arraycopy(data, 0, newData, 0, pos);
						data = newData;
					}
				}

				is.close();
				dataStr = new String(data, StandardCharsets.UTF_8);
				vStr = StringEscapeUtils.escapeSql(dataStr);
//				vStr = StringEscapeUtils.escapeHtml(dataStr);
				content = vStr.getBytes();
				os = new FileOutputStream(each, false);
				os.write(content);
				os.flush();
				os.close();
			} catch (FileNotFoundException e) {
			} catch (IOException e) {
			} finally {
				close(is);
				close(os);
			}
			listOfFile.add(each);
		}
		fileArrary = listOfFile.toArray(fileArrary);
		return fileArrary;
	}

	public static InputStream escapeInpS(InputStream in) {
		StringWriter writer = new StringWriter();
		try {
			IOUtils.copy(in, writer, "UTF-8");

			String vStr = StringEscapeUtils.escapeSql(writer.toString());
//			vStr = StringEscapeUtils.escapeHtml(writer.toString());
			in = new ByteArrayInputStream(vStr.getBytes());
		} catch (IOException e) {

		}

		return in;
	}

	private static void close(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (IOException e) {

			}
		}
	}
	
	//	DB2 SQL Error: SQLCODE=-438, SQLSTATE=88888, SQLERRMC=null, DRIVER=4.14.111
	public static String parseDB2Error(String errorStr ) {
		Map<String,String> returnMap = new HashMap<String,String>();
		String sqlstate="SQLSTATE=";
		String retStr="";
		Integer start = 0;
		Integer end = 0;
		start = errorStr.indexOf(sqlstate) ;
		end = errorStr.indexOf(",", start);
		if(start != -1){
			retStr = errorStr.substring(start+sqlstate.length() , end).trim();
		}
		
		return retStr;
	}

	public static void main(String[] args) throws UnsupportedEncodingException {
		CodeUtils c = new CodeUtils();
		// System.out.println(c.iKeyVerification("0040000-01"));
		// String gg ="代收發測試資料代收發測試公司";
		// System.out.println(new CodeUtils().findHardWordNum(gg,new
		// String(gg.getBytes("BIG5"),"BIG5")));
		c.StrTest();
	}
}
