package tw.org.twntch.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.apache.log4j.Logger;
/**
 * 
 * @author Hugo
 *
 */
public class DateTimeUtils {
	static SimpleDateFormat dtF = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	static SimpleDateFormat dtD = new SimpleDateFormat("yyyyMMdd");
	static SimpleDateFormat dtT = new SimpleDateFormat("HHmmss");
	static SimpleDateFormat dtS = new SimpleDateFormat("yyyy-MM-dd");
	static Calendar calendar = GregorianCalendar.getInstance(java.util.Locale.TAIWAN);
	private static Logger logger = Logger.getLogger(DateTimeUtils.class.getName());
	
	static Map<String, SimpleDateFormat> pool = new HashMap();
	
	public static int INTERCONVERSION = 1;
	public static int NOT_INTERCONVERSION = 2;
	
	/**
	 * 將指定的 names 把 params 裡的值的日期, 如 2008/02/01 格式的日期, 變成 20080201
	 * @param params
	 * @param names
	 */
	public static void removeSlash(Hashtable params, String[] names) {
		if(names != null && params != null) {
			for(String o : names) {
				String s = ((String)params.get(o)).replaceAll("/", "");
				params.put(o, s);
			}
		}
	}
	
	/**
	 * 將 2008/02/01 格式的日期, 變成 20080201
	 * @param params
	 * @param names
	 */
	public static String addSlash(String dateShort) {
		return dateShort.substring(0, 4) + "/" + dateShort.substring(4, 6) + "/" + dateShort.substring(6, 8);
	}
	
	public static String format(String pattern, Date d) {
		SimpleDateFormat sdf = getDateFormatFromPool(pattern);
		return sdf.format(d);		
	}

	public static Date parse(String pattern, String value) {
		try {
			SimpleDateFormat sdf = getDateFormatFromPool(pattern);
			return sdf.parse(value);
		}
		catch(ParseException e) {
//			throw new ToRuntimeException("無法 Parse [" + value+ "] 成 Date Object.", e);
			logger.debug("無法 Parse [" + value+ "] 成 Date Object."+ e);
			return null;
		}
		
	}

	/**
	 * 傳入 年，月，日 的整數值，回傳 日期物件
	 * @param year
	 * @param month
	 * @param day
	 * @return 日期物件
	 */
	public static Date parse(int year, int month, int day)
	{
		try 
		{
			//因為 GregorianCalendar 的月份是由 0 開始，所以需要減一
			GregorianCalendar calendar = new GregorianCalendar(year, month - 1, day);			
			return calendar.getTime();
		}
		catch(Exception e) 
		{
//			throw new ToRuntimeException("無法 Parse "+ year + " " + month + " " + day + " 成 Date Object.", e);
			logger.debug("無法 Parse "+ year + " " + month + " " + day + " 成 Date Object."+e);
			return null;
		}
				
	}
	
	/**
	 * 將 20120401 的整數轉成日期物件
	 * @param date
	 * @return
	 */
	public static Date parse(int date)
	{
		try
		{
			String dt = String.valueOf(date);
			if (dt.length() < 8)
			{
//				throw new ToRuntimeException("無法 Parse "+ date + " 成 Date Object."); 
				logger.debug("無法 Parse "+ date + " 成 Date Object.");
			}
			
			Integer year, month, day;
			year = Integer.parseInt(dt.substring(0, 4));
			month = Integer.parseInt(dt.substring(4, 6));
			day = Integer.parseInt(dt.substring(6, 8));
			
			return parse(year.intValue(), month.intValue(), day.intValue());
			
		}
		catch(Exception e)
		{
//			throw new ToRuntimeException("無法 Parse "+ date + " 成 Date Object.", e);
			logger.debug("無法 Parse "+ date + " 成 Date Object."+ e);
			return null;
		}
		
	}
	
	private static SimpleDateFormat getDateFormatFromPool(String pattern) {
		SimpleDateFormat sdf = null;
		if(!pool.containsKey(pattern)) {
			sdf = new SimpleDateFormat(pattern);
			pool.put(pattern, sdf);
		}
		else {
			sdf = pool.get(pattern);
		}
		return sdf;
	}
	
	/**
	 * 回傳 2008/10/22 20:12:00
	 * @param d
	 * @return
	 */
	public static String getDatetime(Date d) {
		return dtF.format(d);
	}
	
	/**
	 * 回傳 20081022
	 * @param d
	 * @return
	 */
	public static String getDateShort(Date d) {
		return dtD.format(d);
	}
	
	/**
	 * 回傳 2008-10-22
	 * @param d
	 * @return
	 */
	public static String getDateShort2(Date d) {
		return dtS.format(d);
	}
	/**
	 * 回傳 0971011
	 * @param d
	 * @return
	 */
	public static String getCDateShort(Date d) {
		String dt = dtD.format(d);
		String result = dt.substring(4);
		result = new Integer(dt.substring(0, 4)) - 1911 + result;
		if(result.length() == 6)
			result = "0" + result;
		return result;
	}
	
	public static String getTimeShort(Date d) {
		return dtT.format(d);
	}
	
	
	/**
	 * 傳入民國日期及日期, 如 "0970501", "103020", 回傳 097/05/01 10:30:20
	 * @param cDateSort
	 * @param timeSort
	 * @return
	 */
	public static String getCDateTime(String cDateSort, String timeSort) {
		String result = cDateSort + " " + timeSort;
		return result.replaceAll("(\\d{3})(\\d{2})(\\d{2})(\\s)(\\d{2})(\\d{2})(\\d{2})", "$1/$2/$3 $5:$6:$7");
	}
	
	/**
	 * 傳入民國年月日, 如 "00970501"  回傳 Date 物件
	 * @param cDateSort
	 * @return
	 */
	public static Date getCDate2Date(String cDateSort) {
		String s = (new Integer(cDateSort.substring(0,4)) + 1911) + "";
		return DateTimeUtils.parse("yyyyMMdd", s + cDateSort.substring(4));
	}
	
	/**
	 * 加入時間符號 ":"
	 * @param dateShort
	 * @return
	 */
	public static String addColon(String dateShort) {
		return dateShort.substring(0, 2) + ":" + dateShort.substring(2, 4) + ":" + dateShort.substring(4, 6);
	}
	
	public static String addDateCount(String date, int addType,
			String dateCount, String flag) {
		try {
			Date inDate = dtD.parse(date);
			calendar.setTime(inDate);
			int count = Integer.parseInt(dateCount);
			if (flag.equals("-")) {
				count = 0 - count;
			}
			calendar.add(addType, count);
			return dtD.format(calendar.getTime());
		} catch (Exception ex) {
			return "E408";
		}
	}
	
	/**
	 * 西元年 民國年互轉
	 * @param AD
	 * @param beforeFormat(轉換前的格式)
	 * @param afterFormat(轉換後的格式)
	 * 範例
	 * convertDate("2013-05-08 21:10:10 ","yyyy-MM-dd HH:mm:ss","yyyyMMdd HH:mm:ss")
       convertDate("0102-05-08 21:20:10","yyyy-MM-dd HH:mm:ss","yyyyMMdd HH:mm:ss"
	 * @return
	 */
	public static String convertDate (String AD,String beforeFormat,String afterFormat){//轉年月格式
//        if (AD == null) return "";
        if (StrUtils.isEmpty(AD)) return "";
        SimpleDateFormat df4 = new SimpleDateFormat(beforeFormat);
        SimpleDateFormat df2 = new SimpleDateFormat(afterFormat);
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(df4.parse(AD));
            if (cal.get(Calendar.YEAR) > 1492){
            	cal.add(Calendar.YEAR, -1911);
            }else {
            	cal.add(Calendar.YEAR, +1911);
            }
            return df2.format(cal.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
	
	/**
	 * 西元年 民國年互轉
	 * @param type(1(INTERCONVERSION)=西元民國互轉;2(NOT_INTERCONVERSION)=不轉)
	 * @param AD
	 * @param beforeFormat(轉換前的格式)
	 * @param afterFormat(轉換後的格式)
	 * 範例
	 * convertDate("2013-05-08 21:10:10 ","yyyy-MM-dd HH:mm:ss","yyyyMMdd HH:mm:ss")
       convertDate("0102-05-08 21:20:10","yyyy-MM-dd HH:mm:ss","yyyyMMdd HH:mm:ss"
	 * @return
	 */
	public static String convertDate (int type, String AD,String beforeFormat,String afterFormat){//轉年月格式
        if (StrUtils.isEmpty(AD)) return "";
        SimpleDateFormat df4 = new SimpleDateFormat(beforeFormat);
        SimpleDateFormat df2 = new SimpleDateFormat(afterFormat);
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(df4.parse(AD));
            if(type == INTERCONVERSION){
            	if (cal.get(Calendar.YEAR) > 1492){
                	cal.add(Calendar.YEAR, -1911);
                }else {
                	cal.add(Calendar.YEAR, +1911);
                }
            }
            return df2.format(cal.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
	
	public static void main(String[] s) {
		logger.debug(DateTimeUtils.parse(20120402));
		logger.debug(DateTimeUtils.getCDateShort(new Date()));
		logger.debug(DateTimeUtils.getCDateTime("0970501", "103020"));
		logger.debug(DateTimeUtils.getCDate2Date("0970501"));
		logger.debug(DateTimeUtils.addSlash("20080501"));
		
		logger.debug(convertDate("2013-05-08 ","yyyy-MM-dd","yyyyMMdd"));
		logger.debug(convertDate("0102-05-08","yyyy-MM-dd","yyyyMMdd"));
		logger.debug(convertDate("2013/05/08","yyyy/MM/dd","yyy/MM/dd"));
		logger.debug(convertDate("2013/05/08","yyyy/MM/dd","yyy-MM-dd"));
		logger.debug(convertDate("2013/05/08","yyyy/MM/dd","yy-MM-dd"));
		logger.debug(convertDate("民國 103 年 01 月 15 日","民國 yyy 年 MM 月 dd 日","西元 yyyy 年 MM 月 dd 日"));
		logger.debug(convertDate("西元 2014 年 01 月 15 日","西元 yyyy 年 MM 月 dd 日","民國 yyy 年 MM 月 dd 日"));
	
		logger.debug(convertDate("2013-05-08 21:10:10 ","yyyy-MM-dd HH:mm:ss","yyyyMMdd HH:mm:ss"));
		logger.debug(convertDate("01031211 21:20:10.123","yyyyMMdd HH:mm:ss.SSS","yyyy-MM-dd HH:mm:ss.SSS"));
		logger.debug(convertDate(convertDate("01030105","yyyyMMdd","yyyyMMdd"),"yyyyMMdd","民國 yyy 年 MM 月 dd 日"));
	}
	
	/**
	 * 輸入年份及月份(以Calendar常數)
	 * @param year
	 * @param month
	 * @return
	 */
	public static String getMonthDays(int year, int month){
		int iDay = 1;

		// Create a calendar object and set year and month
		Calendar mycal = new GregorianCalendar(year, month, iDay);

		// Get the number of days in that month
		int daysInMonth = mycal.getActualMaximum(Calendar.DAY_OF_MONTH); // 28
		
		return String.valueOf(daysInMonth);
	}
	/**
	 * 回傳兩個日期的相差天數
	 * @param beforeDate
	 * @param afterDate
	 * @return long
	 */
	public static long getDifferentDays(Date beforeDate,Date afterDate){
		return (afterDate.getTime()-beforeDate.getTime())/(1000*60*60*24);
	}
}

