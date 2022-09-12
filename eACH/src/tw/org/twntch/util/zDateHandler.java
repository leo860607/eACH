package tw.org.twntch.util;
/* ************************************************** 
**程式代號：zDateHandler
**程式名稱：
**程式說明：取得日期及時間
**程式說明之內容：
**版本說明：
**日期          作者          版本     說明
**----------    ----------    -----    --------------------
**01/25/2005    Grace         V1.0     Create
**MM/DD/YYYY
**
**相關程式：
**程式代號                     說明
**-------------------------    -------------------------
**
**Proprietary/Confidential to NSC 
** ***************************************************/ 


import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class zDateHandler{	
	
	//static Calendar cal = new GregorianCalendar();
	
	/**
	 * 驗證EACH的營業日
	 * @param bizDate(西元年)ex:20150801
	 * @return
	 */
	public static Map<String,String> verify_BizDate(String bizDate){
		String result ="FALSE";
		String msg = "";
		Map<String,String> map = new HashMap<String, String>();
		if(StrUtils.isEmpty(bizDate)){
			msg = "營業日不可為空白";
			map.put("result", result);
			map.put("msg", msg);
			return map;
		}
		if(bizDate.length() !=8){
			msg = "營業日長度不符";
			map.put("result", result);
			map.put("msg", msg);
			return map;
		}
		if(!bizDate.matches("^[0-9]*[1-9][0-9]*$")){
			msg = "營業日必須為數字";
			map.put("result", result);
			map.put("msg", msg);
			return map;
		}
		if(!bizDate.matches("((19|20)\\d\\d)(0?[1-9]|1[012])(0?[1-9]|[12][0-9]|3[01])")){
			result = "FALSE";
			msg = "營業日為不合法日期";
			map.put("result", result);
			map.put("msg", msg);
			return map;
		}
		result ="TRUE";
		map.put("result", result);
		map.put("msg", msg);
		return map;
	}
	/**
	 * 驗證EACH的營業日
	 * @param bizDate(西元年)ex:20150801
	 * @return
	 */
	public static Map<String,String> verify_BizDate(String s_bizDate , String e_bizDate){
		String result ="FALSE";
		String msg = "";
		Map<String,String> map = new HashMap<String, String>();
		if(StrUtils.isEmpty(s_bizDate) || StrUtils.isEmpty(e_bizDate)){
			msg = "營業日不可為空白";
			map.put("result", result);
			map.put("msg", msg);
			return map;
		}
		if(s_bizDate.length() !=8 || e_bizDate.length() !=8){
			msg = "營業日長度不符";
			map.put("result", result);
			map.put("msg", msg);
			return map;
		}
		if(!s_bizDate.matches("^[0-9]*[1-9][0-9]*$") || !e_bizDate.matches("^[0-9]*[1-9][0-9]*$")){
			msg = "營業日必須為數字";
			map.put("result", result);
			map.put("msg", msg);
			return map;
		}
		if(!s_bizDate.matches("((19|20)\\d\\d)(0?[1-9]|1[012])(0?[1-9]|[12][0-9]|3[01])") || !e_bizDate.matches("((19|20)\\d\\d)(0?[1-9]|1[012])(0?[1-9]|[12][0-9]|3[01])")){
			result = "FALSE";
			msg = "營業日為不合法日期";
			map.put("result", result);
			map.put("msg", msg);
			return map;
		}
		if( zDateHandler.compareDiffDate(s_bizDate, e_bizDate, "yyyyMMdd") > 0){
			result = "FALSE";
			msg = "起始日期必須早於結束日期";
			map.put("result", result);
			map.put("msg", msg);
			return map;
		}
		result ="TRUE";
		map.put("result", result);
		map.put("msg", msg);
		return map;
	}
	
//	=================20140926 add by hugo========================
	/**
	 * 比較2個日期的大小 前者大 返回1 返之-1 相等 =0
	 * @param firstdate
	 * @param seconddate
	 * @return
	 */
	public static int compareDiffDate(String firstdate ,String seconddate ){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		Calendar calendar =Calendar.getInstance();
		Calendar calendar2 = Calendar.getInstance();
		int result = 0;
		try {
			calendar.setTime(sdf.parse(firstdate));
			calendar2.setTime(sdf.parse(seconddate));
			result = calendar.compareTo(calendar2);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("firstdate"+ firstdate+"seconddate"+seconddate);
		System.out.println("result>>"+result);
		return result;
	}
	/**
	 * 比較2個日期的大小 前者大 返回1 返之-1 相等 =0
	 * @param firstdate
	 * @param seconddate
	 * @param formatStyle 輸入日期的格式
	 * @return
	 */
	public static int compareDiffDate(String firstdate ,String seconddate ,String formatStyle  ){
		SimpleDateFormat sdf = new SimpleDateFormat(formatStyle);
		Calendar calendar =Calendar.getInstance();
		Calendar calendar2 = Calendar.getInstance();
		int result = 0;
		try {
			calendar.setTime(sdf.parse(firstdate));
			calendar2.setTime(sdf.parse(seconddate));
			result = calendar.compareTo(calendar2);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("firstdate"+ firstdate+"seconddate"+seconddate);
		System.out.println("result>>"+result);
		return result;
	}
	
//	取得目前日期的上個月分
	public static int getPrveMonth(String date  ){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		Calendar calendar =Calendar.getInstance();
		int month = 0;
		try {
			calendar.setTime(sdf.parse(date));
			calendar.add(Calendar.MONTH, -1);
			month =  Integer.valueOf( String.valueOf((calendar.get(Calendar.MONTH)+1)/10) +""+ String.valueOf((calendar.get(Calendar.MONTH)+1)%10));	
//			month = calendar.get(Calendar.MONTH);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("result>>"+month);
		return month;
	}
	
	/**
	 * 根據日期取得上一個年月
	 * 格式201505
	 * @param date ex:2015/05/01
	 * @return ex:201504
	 */
	public static String getEACH_PrveMonth(String date  ){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		Calendar calendar =Calendar.getInstance();
		String month_str = "";
		try {
			calendar.setTime(sdf.parse(date));
			calendar.add(Calendar.MONTH, -1);
			if(String.valueOf(calendar.get(Calendar.YEAR)).startsWith("20") ){
				month_str = calendar.get(Calendar.YEAR) +String.valueOf((calendar.get(Calendar.MONTH)+1)/10) +""+ String.valueOf((calendar.get(Calendar.MONTH)+1)%10);	
			}
//			如果非西元年強制+1911
			else{
				month_str = (calendar.get(Calendar.YEAR)+1911) +String.valueOf((calendar.get(Calendar.MONTH)+1)/10) +""+ String.valueOf((calendar.get(Calendar.MONTH)+1)%10);	
			}
			System.out.println("month_str>>"+month_str);
			return month_str;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return month_str;
	}
	/**
	 * 根據日期取得上一個年月
	 * 格式201505
	 * @param date ex:2015/05/01
	 * @param type ex:yyyy/MM/dd
	 * 
	 * @return ex:201504
	 */
	public static String getEACH_PrveMonth(String date ,String type ){
		SimpleDateFormat sdf = new SimpleDateFormat(type);
		Calendar calendar =Calendar.getInstance();
		String month_str = "";
		try {
			calendar.setTime(sdf.parse(date));
			calendar.add(Calendar.MONTH, -1);
//		    month_str = calendar.get(Calendar.YEAR) +String.valueOf((calendar.get(Calendar.MONTH)+1)/10) +""+ String.valueOf((calendar.get(Calendar.MONTH)+1)%10);	
			
			if(String.valueOf(calendar.get(Calendar.YEAR)).startsWith("20") ){
				month_str = calendar.get(Calendar.YEAR) +String.valueOf((calendar.get(Calendar.MONTH)+1)/10) +""+ String.valueOf((calendar.get(Calendar.MONTH)+1)%10);	
			}
//			如果非西元年強制+1911
			else{
				month_str = (calendar.get(Calendar.YEAR)+1911) +String.valueOf((calendar.get(Calendar.MONTH)+1)/10) +""+ String.valueOf((calendar.get(Calendar.MONTH)+1)%10);	
			}
		    System.out.println("month_str>>"+month_str);
			return month_str;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return month_str;
	}
	
	/**
	 * 根據日期取得上一個年月
	 * 格式201505
	 * @param date ex:2015/05/01
	 * @param type ex:yyyy/MM/dd
	 * @param retMod E:為民國年
	 * @param retType 回傳的格式 ex: 010412
	 * @return
	 */
	public static String getEACH_PrveMonth(String date ,String type ,String retMod ,String retType){
		SimpleDateFormat sdf = new SimpleDateFormat(type);
		Calendar calendar =Calendar.getInstance();
		String month_str = "";
		try {
			calendar.setTime(sdf.parse(date));
			calendar.add(Calendar.MONTH, -1);
//		    month_str = calendar.get(Calendar.YEAR) +String.valueOf((calendar.get(Calendar.MONTH)+1)/10) +""+ String.valueOf((calendar.get(Calendar.MONTH)+1)%10);	
			
			if(String.valueOf(calendar.get(Calendar.YEAR)).startsWith("20") ){
				month_str = calendar.get(Calendar.YEAR) +String.valueOf((calendar.get(Calendar.MONTH)+1)/10) +""+ String.valueOf((calendar.get(Calendar.MONTH)+1)%10);	
			}
//			如果非西元年強制+1911
			else{
				month_str = (calendar.get(Calendar.YEAR)+1911) +String.valueOf((calendar.get(Calendar.MONTH)+1)/10) +""+ String.valueOf((calendar.get(Calendar.MONTH)+1)%10);	
			}
			
			if(StrUtils.isNotEmpty(retMod) && retMod.equals("E")){
				month_str = DateTimeUtils.convertDate(month_str, "yyyyMM", retType);
			}
			System.out.println("month_str>>"+month_str);
			return month_str;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return month_str;
	}
	
	
	
	/**
	 * eACH用
	 * 輸入日期取得該月的第一天
	 * @param date (西元年)("yyyy/MM/dd")
	 * @return (西元年)("yyyyMMdd")
	 */
	public static String getFirst_Date_of_Date(String date  ){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		Calendar calendar =Calendar.getInstance();
		String dateNum = "";
		try {
			calendar.setTime(sdf.parse(date));
			calendar.set(Calendar.DATE, 1);
			String month_str = String.valueOf((calendar.get(Calendar.MONTH)+1)/10) +""+ String.valueOf((calendar.get(Calendar.MONTH)+1)%10);	
			String day= String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)/10) +""+ String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)%10);
//			dateNum = getCurrentYear() +""+ month_str+""+day ;
//			dateNum = (calendar.get(Calendar.YEAR) +1911)  +""+ month_str+""+day ;
			dateNum = (calendar.get(Calendar.YEAR))  +""+ month_str+""+day ;
			System.out.println("dateNum>>"+dateNum);
			return dateNum;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dateNum;
	}
	/**
	 *eACH用
	 * 輸入日期取得該月的最後一天
	 * @param date
	 * @return
	 */
	public static String getLast_Date_of_Date(String date  ){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		Calendar calendar =Calendar.getInstance();
		String dateNum = "";
		try {
			calendar.setTime(sdf.parse(date));
			calendar.set(Calendar.DATE,calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
			String month_str = String.valueOf((calendar.get(Calendar.MONTH)+1)/10) +""+ String.valueOf((calendar.get(Calendar.MONTH)+1)%10);	
			String day= String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)/10) +""+ String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)%10);
//			dateNum = getCurrentYear() +""+ month_str+""+day ;
			dateNum = (calendar.get(Calendar.YEAR)) +""+ month_str+""+day ;
			System.out.println("dateNum>>"+dateNum);
			return dateNum;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dateNum;
	}
	
	
	/**
	 * 比較2個日期的大小 前者大 返回1 返之-1 相等 =0
	 * @param firstdate
	 * @param seconddate
	 * @return
	 */
	public static int compareDiffDateII(String firstdate ,String seconddate ){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar =Calendar.getInstance();
		Calendar calendar2 = Calendar.getInstance();
		int result = 0;
		try {
			calendar.setTime(sdf.parse(firstdate));
			calendar2.setTime(sdf.parse(seconddate));
			result = calendar.compareTo(calendar2);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("firstdate"+ firstdate+"seconddate"+seconddate);
		System.out.println("result>>"+result);
		return result;
	}
	/**
	 * 比較2個時間的大小 前者大 返回1 返之-1 相等 =0
	 * @param firsttime
	 * @param secondtime
	 * @return
	 */
	public static int compareDiffTime(String firsttime ,String secondtime ){
		System.out.println("firsttime>>"+firsttime);
		System.out.println("secondtime>>"+secondtime);
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		Calendar calendar =Calendar.getInstance();
		Calendar calendar2 = Calendar.getInstance();
		int result = 0;
		try {
			calendar.setTime(sdf.parse(firsttime));
			calendar2.setTime(sdf.parse(secondtime));
			result = calendar.compareTo(calendar2);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("firsttime"+ firsttime+"secondtime"+secondtime);
		System.out.println("result>>"+result);
		return result;
	}
	
	public static boolean isInTimeRange(String time ,String firstTime ,String secondTime ){
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		Calendar calendar =Calendar.getInstance();
		Calendar calendar1 = Calendar.getInstance();
		Calendar calendar2 = Calendar.getInstance();
		boolean result = false;
		try {
			calendar.setTime(sdf.parse(time));
			calendar1.setTime(sdf.parse(firstTime));
			calendar2.setTime(sdf.parse(secondTime));
//			result = calendar.compareTo(calendar2);
			if(calendar.compareTo(calendar1) !=-1 && calendar.compareTo(calendar2) ==-1){
				result = true;
			}else{
				result = false;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println("time>>"+time+"firstTime>>"+ firstTime+"secondTime>>"+secondTime);
		//System.out.println("result>>"+result);
		return result;
	}
	
	/**
	 * 此方法假設startTime早於endTime
	 * 以inputDateTime的日期為基準，判斷時間是否在startTime及endTime之間(或剛好)，並考慮跨日的情形
	 * @param inputDateTime : yyyy-MM-dd HH:mm:ss
	 * @param startTime : HH:mm:ss
	 * @param endTime : HH:mm:ss
	 * @return
	 */
	public static boolean isInDateTimeRange(String inputDateTime ,String startTime ,String endTime){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat tf = new SimpleDateFormat("HH:mm:ss");
		Calendar input_cal = Calendar.getInstance();
		Calendar start_cal = Calendar.getInstance();
		Calendar end_cal = Calendar.getInstance();
		
		boolean result = false;
		try {
			input_cal.setTime(df.parse(inputDateTime));
			
			start_cal.setTime(tf.parse(startTime));
			start_cal.set(input_cal.get(Calendar.YEAR), input_cal.get(Calendar.MONTH), input_cal.get(Calendar.DATE));
			
			end_cal.setTime(tf.parse(endTime));
			end_cal.set(input_cal.get(Calendar.YEAR), input_cal.get(Calendar.MONTH), input_cal.get(Calendar.DATE));
			
			//若起日比迄日晚，表示有跨日
			if(start_cal.after(end_cal)){
				end_cal.add(Calendar.DATE, 1);
			}
			
			//System.out.println("INPUT >> " + df.format(input_cal.getTime()));
			//System.out.println("START >> " + df.format(start_cal.getTime()));
			//System.out.println("END >> " + df.format(end_cal.getTime()));
			
			if(
					(input_cal.after(start_cal) && input_cal.before(end_cal)) ||
					input_cal.equals(start_cal) ||
					input_cal.equals(end_cal)
			){
				result = true;
			}else{
				result = false;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 取得2個日期間的天數
	 * @param firstdate
	 * @param seconddate
	 * @return
	 */
	public static int getDiffDate(String firstdate ,String seconddate){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		Calendar calendar =Calendar.getInstance();
		Calendar calendar2 = Calendar.getInstance();
		long d1 = 0;
		long d2 = 0;
		BigDecimal diffdays = new BigDecimal(0) ; 
		try {
//			System.out.println("firstdate>>"+firstdate+"seconddate>>"+seconddate);
			calendar.setTime(sdf.parse(firstdate));
			calendar2.setTime(sdf.parse(seconddate));
			d1 = calendar.getTimeInMillis();
			d2 = calendar2.getTimeInMillis();
			diffdays = new BigDecimal((d1-d2)/(1000*60*60*24)).abs();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return diffdays.intValue()+1;
		
	}
	/**
	 * 取得2個日期間的天數
	 * @param firstdate
	 * @param seconddate
	 * @return
	 */
	public static int getDiffTimeStamp(String firstdate ,String seconddate){
		System.out.println("firstdate>>"+firstdate);
		System.out.println("seconddate>>"+seconddate);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar =Calendar.getInstance();
		Calendar calendar2 = Calendar.getInstance();
		long d1 = 0;
		long d2 = 0;
		BigDecimal diffdays = new BigDecimal(0) ; 
		try {
//			System.out.println("firstdate>>"+firstdate+"seconddate>>"+seconddate);
			calendar.setTime(sdf.parse(firstdate));
			calendar2.setTime(sdf.parse(seconddate));
			d1 = calendar.getTimeInMillis();
			d2 = calendar2.getTimeInMillis();
			diffdays = new BigDecimal((d1-d2)/(1000*60*60*24)).abs();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return diffdays.intValue()+1;
		
	}
	
	/**
	 * 根據輸入的參數取得該日期 N天後的日期
	 * @param sdate
	 * @param day
	 * @return
	 */
	public static String getDateByafterDay(String sdate ,String day){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		Calendar calendar = sdf.getCalendar();
		BigDecimal sday =new BigDecimal(0);
		String rtDate = "";
		try {
			
			calendar.setTime(sdf.parse(sdate));
			sday = new BigDecimal(calendar.get(Calendar.DAY_OF_YEAR));
			calendar.set(Calendar.DAY_OF_YEAR, sday.add(new BigDecimal(day)).intValue());
			rtDate = sdf.format(calendar.getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rtDate;
	}
	
//	=================20140926 end        ========================
	/**
	 * 取得西元年
	 * 
	 * @return
	 */
	public static String getCurrentYear(){
		Calendar cal = Calendar.getInstance();
		String year = String.valueOf(cal.get(Calendar.YEAR));
		return year;
	}
	
	/**
	 * 取得民國年
	 * 
	 * @return
	 */
	public static String getCurrentTWYear(){
		Calendar cal = Calendar.getInstance();
		String year = String.valueOf(cal.get(Calendar.YEAR)-1911);
		return year;
	}
	
	/**
	 * 取得申請年度
	 * 
	 * @return
	 */
	public static String getAplYear(){
		Calendar cal = Calendar.getInstance();
		String year = String.valueOf(cal.get(Calendar.YEAR)-1910);
		return year;
	}
	
	/**
	 * 取得月份
	 * 
	 * @return
	 */
	public static String getTheMonth(){
		Calendar cal = Calendar.getInstance();
		String theMonth = String.valueOf((cal.get(Calendar.MONTH)+1)/10) +""+ String.valueOf((cal.get(Calendar.MONTH)+1)%10);
		return theMonth;
	}
	
	/**
	 * 取得日期日
	 * 
	 * @return
	 */
	public static String getTheDay(){
		Calendar cal = Calendar.getInstance();
		String theDate = String.valueOf(cal.get(Calendar.DAY_OF_MONTH)/10) +""+ String.valueOf(cal.get(Calendar.DAY_OF_MONTH)%10);
		return theDate;
	}
	
	/**
	 * 取得西元日期
	 * 格式為20050127
	 * 
	 * @return
	 */
	public static String getDateNum(){
		Calendar cal = Calendar.getInstance();
		String dateNum = "";
		String month = String.valueOf((cal.get(Calendar.MONTH)+1)/10) +""+ String.valueOf((cal.get(Calendar.MONTH)+1)%10);	
		String day= String.valueOf(cal.get(Calendar.DAY_OF_MONTH)/10) +""+ String.valueOf(cal.get(Calendar.DAY_OF_MONTH)%10);
		dateNum = getCurrentYear() +""+ month +""+ day;
		return dateNum;
	}
	
	/**
	 * 取得西元日期
	 * 其回傳值格式為 _ _ _ _/_ _/_ _  
	 * 例如 2004/01/25
	 * 
	 * @return
	 */
	public static String getTheDate(){		
		Calendar cal = Calendar.getInstance();
		String theDate = null;
		String month = String.valueOf((cal.get(Calendar.MONTH)+1)/10) +""+ String.valueOf((cal.get(Calendar.MONTH)+1)%10);	
		String day= String.valueOf(cal.get(Calendar.DAY_OF_MONTH)/10) +""+ String.valueOf(cal.get(Calendar.DAY_OF_MONTH)%10);
		theDate = getCurrentYear() +"/"+ month +"/"+ day;
		return theDate;
	}
	/**
	 * 取得西元日期
	 * 其回傳值格式為 _ _ _ _-_ _-_ _  
	 * 例如 2014-01-25
	 * 
	 * @return
	 */
	public static String getTheDateII(){		
		Calendar cal = Calendar.getInstance();
		String theDate = null;
		String month = String.valueOf((cal.get(Calendar.MONTH)+1)/10) +""+ String.valueOf((cal.get(Calendar.MONTH)+1)%10);	
		String day= String.valueOf(cal.get(Calendar.DAY_OF_MONTH)/10) +""+ String.valueOf(cal.get(Calendar.DAY_OF_MONTH)%10);
		theDate = getCurrentYear() +"-"+ month +"-"+ day;
		return theDate;
	}
	
	/**
	 * 取得時間
	 * 其回傳值格式為 _ _:_ _:_ _
	 * 例如  04:06:26
	 * 
	 * @return
	 */
	public static String getTheTime(){
		Calendar cal = Calendar.getInstance();
		String theTime = null;
		String theHours = String.valueOf(cal.get(Calendar.HOUR_OF_DAY)/10) +""+ String.valueOf(cal.get(Calendar.HOUR_OF_DAY)%10);
		String theMinutes = String.valueOf(cal.get(Calendar.MINUTE)/10) +""+ String.valueOf(cal.get(Calendar.MINUTE)%10);
		String theSeconds = String.valueOf(cal.get(Calendar.SECOND)/10) +""+ String.valueOf(cal.get(Calendar.SECOND)%10);
		theTime = theHours +":"+ theMinutes +":"+ theSeconds;
		return theTime;
	}
	/**
	 * 取得時間
	 * 其回傳值格式為 _ _:_ _:_ _._ _ _
	 * 例如  04:06:26.123
	 * 
	 * @return
	 */
	public static String getTheTime_MS(){
		Calendar cal = Calendar.getInstance();
		String theTime = null;
		String theHours = String.valueOf(cal.get(Calendar.HOUR_OF_DAY)/10) +""+ String.valueOf(cal.get(Calendar.HOUR_OF_DAY)%10);
		String theMinutes = String.valueOf(cal.get(Calendar.MINUTE)/10) +""+ String.valueOf(cal.get(Calendar.MINUTE)%10);
		String theSeconds = String.valueOf(cal.get(Calendar.SECOND)/10) +""+ String.valueOf(cal.get(Calendar.SECOND)%10);
		String theMSeconds = String.valueOf(cal.get(Calendar.MILLISECOND)/10) +""+ String.valueOf(cal.get(Calendar.SECOND)%10);
		theTime = theHours +":"+ theMinutes +":"+ theSeconds+"."+theMSeconds;
		return theTime;
	}
	
	/**
	 * 取得Timestamp格式的字串
	 * @return
	 */
	public static String getTimestamp(){
		String theTime = "";
		theTime = getTheDateII()+" "+getTheTime();
		return theTime;
	}
	
	
	
	/**
	 * 將千位以內的數字改成中文顯示
	 * 
	 * @param num
	 * @return
	 */
	public static String getNumInTC(int num){
		String tcNum = "";
		int theHundred = num/100;
		int theTens = (num%100)/10;
		int theOnes = num%10;
		if(theHundred>0){
			tcNum = getTC(theHundred)+"百";
		}
		if(theHundred>0 && theTens==0 && theOnes>0){
			tcNum = tcNum +"零";
		}
		if(theTens>0){
			tcNum = tcNum +""+ getTC(theTens) +"十";
		}
		if(theOnes>0){
			tcNum = tcNum +""+ getTC(theOnes);
		}
		return tcNum;
	}
	
	/**
	 * 將單一數字改成中文
	 * 
	 * @param num
	 * @return
	 */
	public static String getTC(int num){
		String tc = "";
		if(num==1){
			tc = "一";
		}
		if(num==2){
			tc = "二";
		}
		if(num==3){
			tc = "三";
		}
		if(num==4){
			tc = "四";
		}
		if(num==5){
			tc = "五";
		}
		if(num==6){
			tc = "六";
		}
		if(num==7){
			tc = "七";
		}
		if(num==8){
			tc = "八";
		}
		if(num==9){
			tc = "九";
		}
		return tc;
	}
	
	/**
		 * 取得日期(FOR 公文DB用)
		 * 其回傳值格式為 _ _ _/_ _/_ _  
		 * 例如 093/01/25
		 * 
		 * @return
		 */
		public static String getODDate(){	
			Calendar cal = Calendar.getInstance();	
			String theDate = null;
			String month = String.valueOf((cal.get(Calendar.MONTH)+1)/10) +""+ String.valueOf((cal.get(Calendar.MONTH)+1)%10);	
			String day= String.valueOf(cal.get(Calendar.DAY_OF_MONTH)/10) +""+ String.valueOf(cal.get(Calendar.DAY_OF_MONTH)%10);
			if (getCurrentTWYear().length()==3){
				theDate = getCurrentTWYear() +"/"+ month +"/"+ day;
			}else{
				theDate = "0"+getCurrentTWYear() +"/"+ month +"/"+ day;
			}
			return theDate;
		}
		/**
		 * 
		 * 
		 * 取得民國日期
		 * 其回傳值格式為 _ _ _ _ _ _ _ _  
		 * 例如 01030125
		 * @return
		 */
		public static String getTWDate(){	
			Calendar cal = Calendar.getInstance();	
			String theDate = null;
			String month = String.valueOf((cal.get(Calendar.MONTH)+1)/10) +""+ String.valueOf((cal.get(Calendar.MONTH)+1)%10);	
			String day= String.valueOf(cal.get(Calendar.DAY_OF_MONTH)/10) +""+ String.valueOf(cal.get(Calendar.DAY_OF_MONTH)%10);
			if (getCurrentTWYear().length()==3){
				theDate = "0"+getCurrentTWYear() + month + day;
			}else{
				theDate = "00"+getCurrentTWYear() + month + day;
			}
			return theDate;
		}

		
		public static void main(String[] s) {
			getPrveMonth("2016/01/01");
			getEACH_PrveMonth("2016/01/02");
			getFirst_Date_of_Date("2016/01/01");
			getLast_Date_of_Date("2016/01/01");
			
			System.out.println(isInDateTimeRange("2015-10-15 21:00:00", "20:30:00", "06:30:00"));
		}
		
}
