package tw.org.twntch.util;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;


public class StrUtils {
	public static boolean isEmpty(String s) {
		return s == null || s.length() == 0;
	}
	
	public static boolean isNotEmpty(String s) {
		return !(s == null || s.length() == 0);
	}
	
	public static String trim(String s) {
		if(isEmpty(s))
			return "";
		return s.trim();
	}
	
	public static String right(String str, int len) {
		if(str.length() <= len)
			return str;
		return str.substring(str.length() - len);
	}
	
	public static String left(String str, int len) {
		if(str.length() <= len)
			return str;
		return str.substring(0, len);
	}
	
	public static String repeat(String str, int len) {
		StringBuffer sb = new StringBuffer();
		for(int i =0; i < len ; i++) {
			sb.append(str);
		}
		return sb.toString();
	}
	
	/**
	 * �Y�ǤJ�� v ���Ŧr��Ϊ̬�null, �h�^�� default value
	 * @param v
	 * @param defaultValue
	 * @return
	 */
	public static String nvl(String v, String defaultValue) {
		return (isEmpty(v) ? defaultValue : v);
	}

	
	public static Set<String> splitToSet(String delim, String value) {
		return new HashSet(Arrays.asList(value.split(delim)));
	}
	
	/**
	 * �N arys �[�W delim �ܦ� String
	 * @param delim
	 * @param arys
	 * @return
	 */
	public static String implode(String delim, String[] arys) {
		if(arys == null || arys.length == 0)
			return "";
		
		StringBuffer result = new StringBuffer();
		
		for(String x : arys) {
			if(result.length() != 0)
				result.append(delim);
			result.append(x);
		}
		
		return result.toString();
	}
	
	public static String implode(String delim, Set<String> arys) {
		if(arys == null || arys.size() == 0)
			return "";
		
		StringBuffer result = new StringBuffer();
		
		for(String x : arys) {
			if(result.length() != 0)
				result.append(delim);
			result.append(x);
		}
		
		return result.toString();
	}
	
	public static String implode(String delim, List<String> arys) {
		if(arys == null || arys.size() == 0)
			return "";
		
		StringBuffer result = new StringBuffer();
		
		for(String x : arys) {
			if(result.length() != 0)
				result.append(delim);
			result.append(x);
		}
		
		return result.toString();
	}
	
    /**
     * �r��ƻs, �̪� 4096 bytes
     */
    public static void copyStream( InputStream in, OutputStream out ) throws IOException {
        byte[] buf = new byte[4096];
        int len;
        while ( ( len = in.read( buf ) ) != -1 )
            out.write( buf, 0, len );
    }

    /**
     * �r��ƻs, �̪� 4096 bytes
     */
    public static void copyStream( Reader in, Writer out ) throws IOException {
        char[] buf = new char[4096];
        int len;
        while ( ( len = in.read( buf ) ) != -1 )
            out.write( buf, 0, len );
    }

    /**
     * �r��ƻs, �̪� 4096 bytes
     */
    public static void copyStream( InputStream in, Writer out ) throws IOException {

        byte[] buf1 = new byte[4096];
        char[] buf2 = new char[4096];
        int len, i;
        while ( ( len = in.read( buf1 ) ) != -1 ) {
            for ( i = 0; i < len; ++i )
                buf2[i] = (char) buf1[i];
            out.write( buf2, 0, len );
        }
    }

    /**
     * �r��ƻs, �̪� 4096 bytes
     */
    public static void copyStream( Reader in, OutputStream out ) throws IOException
    {
        char[] buf1 = new char[4096];
        byte[] buf2 = new byte[4096];
        int len, i;
        while ( ( len = in.read( buf1 ) ) != -1 ) {
            for ( i = 0; i < len; ++i )
                buf2[i] = (byte) buf1[i];
            out.write( buf2, 0, len );
        }
    }
    

	/**
	 *  �N�r���ରURL�W��
	 * 	�̷�JavaScript��escape��k���W��
	 * 	 1. ���s�X���Ÿ��]�A: @*+/
	 *   2. scape ���s�X + (�[��), �N�Ϫ������Ƥ����ťժ��B�z(�� + �s���r��)�y�����D�C
     *   	�B escape �b�B�z�D ASCII �y�t���r���|�����D�C
	 * @param str �ݭn��X���r��
	 * @return �^����X�᪺�r��
	 */
	public static String escapeJSURL(String str){
		StringBuffer buf = new StringBuffer();
		char[] ch = str.toCharArray();
		for(int i=0; i < ch.length;i++){
			switch(ch[i]){
			case '@':   
			case '*':
			case '/':
			case '+':
				buf.append(ch[i]);
				break;							
			default:
				int Fd = (int) ch[i];	
			    if(Fd <= 128){
			    	if((Fd >= 48 && Fd <= 57) || (Fd >= 65 && Fd <= 90) || (Fd >= 97 && Fd <= 122)){
			    		buf.append(ch[i]);
			    	}else{
			    		buf.append("%" + Integer.toString(Fd, 16).toUpperCase());
			    	}			    	
			    }else{
			    	buf.append("%u" + Integer.toString(Fd, 16).toUpperCase());
			    }
			    break;
			}							
		}
		//System.out.println(buf.toString());
		return buf.toString();
	}
	
	/**
	 *  �NURL�W���ন�r��
	 * 	�̷�JavaScript��escape��k���W��
	 * 	 1. ���s�X���Ÿ��]�A: @*+/
	 *   2. scape ���s�X + (�[��), �N�Ϫ������Ƥ����ťժ��B�z(�� + �s���r��)�y�����D�C
     *   	�B escape �b�B�z�D ASCII �y�t���r���|�����D�C
	 * @param str �ݭn��X���r��
	 * @return �^����X�᪺�r��
	 */
	public static String unescapeJSURL(String str){
		StringBuffer buf = new StringBuffer();
		char[] ch = str.toCharArray();
		for(int i=0; i < ch.length;i++){
			switch(ch[i]){
			case '%':
				if(ch[i+1] == 'u'){
					i=i+2;
					StringBuffer tmp = new StringBuffer();
					int j = i+3;
					for(;i<=j;i++){
						tmp.append(ch[i]);
					}
					buf.append((char)Integer.parseInt(tmp.toString(),16));
					i--;
				}else{
					i++;
					StringBuffer tmp = new StringBuffer();
					int j = i+1;
					for(;i<=j;i++){
						tmp.append(ch[i]);
					}
					buf.append((char)Integer.parseInt(tmp.toString(),16));
					i--;
				}				
				break;
			default:
				buf.append(ch[i]);				
			    break;
			}							
		}
		//System.out.println(buf.toString());
		return buf.toString();
	}
	
	/*
	 * ��֤ߥD���^�Ǫ��q��i����X
	 * 1. ���Y���26
	 * 2. ���Y��o�q���`���-���Y���=>�ݭn�P�_�ѪR���s�X
	 */
	public static String Unescape(byte[] by, int length, String charset){
		StringBuffer buf = new StringBuffer();
		boolean startFlag = false;
		int count = 0;
		int startIndex = -1;
		int endIndex=-1;
		int offset = 0; //26;
		
		byte[] bb = null;
		//�Y�O?���Ȥj�� 0 ��ܭn�Ѫ��Y
		if (offset > 0)
		{
			//��Ѫ��Y
			bb = new byte[offset];
			for(int i=0; i <bb.length;i++ )
			{
				bb[i] = by[i];			
			}
			try 
			{
				buf.append(new String(bb, charset));
			} 
			catch (UnsupportedEncodingException e1) 
			{
				e1.printStackTrace();
			}			
		}
		
		//�b�ѥ���óB�z�ഫ����
		bb = new byte[length - offset];
		
		for(int i=offset; i<length;i++)
		{
			//if(i == 660){
			//	System.out.println("aaa");
			//}
					
			if(by[i] == (byte)4)
			{
				startFlag = true;
				startIndex = i;
			}
			else if(by[i]== (byte)7)
			{
				startFlag = false;
				endIndex = i;
			}
			
			if(!startFlag && startIndex >= 0)
			{
				//�אּ���l���
				//byte[] tmp = new byte[endIndex-startIndex-1]; 
				byte[] tmp = new byte[endIndex-startIndex + 1]; 
				int index = 0;
				
				for(int k=startIndex + 1; k<endIndex; k++)
				{
					tmp[index]= by[k];
					index++;
				}
				
				//�w�O�Ҧ� 0x04 0x07 �����ɨ�Ӫťզb�᭱
				tmp[index] = 0x20;
				index++;
				tmp[index] = 0x20;
				index++;
				
				//����
				buf.append(new String(bb,0,count));
				
				//�ѤU�� buffer ��� �A-2 = ��Ӫť� 
				//bb = new byte[length - offset - count];
				bb = new byte[length - offset - count - 2];
				
				count = 0;
				startIndex = -1;
				endIndex = -1;
				try 
				{
					buf.append(new String(tmp, charset));
				} 
				catch (UnsupportedEncodingException e) 
				{
					e.printStackTrace();
				}
			}
			
			if(!startFlag && by[i] != 04 && by[i] != 07)
			{
				bb[count] = by[i];
				count++;
			}			
		}
		buf.append(new String(bb,0,count));
		return buf.toString();
	}
	
	/*
	 * �ǵ��֤ߥD�����q��i����X
	 * ����r��s�X�e�ݥ[(byte)04 ����ݥ[(byte)07
	 */
	public static byte[] escape(String str, String charset){
		byte[] by = new byte[4096];
		int length = 0;
		char[] tmp_ch = str.toCharArray();
		boolean startFlag = false;	
		for(int i=0;i<tmp_ch.length;i++){					
			int ch = (int)tmp_ch[i];
				
			if(ch <= 128){
				if(startFlag){
					startFlag = false;						
					by[length] = (byte)7;
					length++;
				}
				try {
					byte[] tmp = (String.valueOf(tmp_ch[i]).getBytes(charset));
					for(int j=0;j<tmp.length;j++){
						by[length] = tmp[j];
						length++;
					}
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}									
				//buf.append(tmp_ch[i]);
			}else{
				if(!startFlag){
					startFlag = true;
					by[length] = (byte)4;
					length++;
				}
				try {

					byte[] tmp = (String.valueOf(tmp_ch[i]).getBytes(charset));
					for(int j=0;j<tmp.length;j++){
						by[length] = tmp[j];
						length++;
					}
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}
		if(startFlag){
			startFlag = false;						
			by[length] = (byte)7;
			length++;
		}
		
		//��zbyte �}�C		
		byte[] aa = new byte[length];
		for(int i=0; i<length;i++){
			aa[i] = by[i];
		}
		return aa;
	}
		
	
	/**
	 * �k�a����
	 * @param format
	 * @param str
	 * @param length
	 * @return
	 */
	public static String formatString(char format, String str,
			int length) {
		StringBuffer formatBuf = new StringBuffer();
		int slen = str.getBytes().length;
		if (slen == length) {
			return str;
		}
		int len = length - slen;
		formatBuf.append(str);
		for (int i = 0; i < len; i++) {
			formatBuf.insert(0, format);
		}
		return formatBuf.toString();
	}
	
	/**
	 * ���a�k��
	 * @param format
	 * @param str
	 * @param length
	 * @return
	 */
	public static String formatString2(char format, String str,
			int length) {
		StringBuffer formatBuf = new StringBuffer();
		int slen = str.getBytes().length;
		if (slen == length) {
			return str;
		}
		int len = length - slen;
		formatBuf.append(str);
		for (int i = 0; i < len; i++) {
			formatBuf.append(format);
		}
		return formatBuf.toString();
	}

	/**
	 * �N unicode �r���ର big5 �r��
	 * @param s
	 * @return big5 �s�X�r��
	 */
	public static String unicodeToBig5(String s)
	{
	    try
	    {
	      return new String(s.getBytes("utf-8"), "Big5");
	    }
	    catch (UnsupportedEncodingException ex)
	    {
	      return null;
	    }
	}

	/**
	 * �N big5 �r���ର unicode �r��
	 * @param s
	 * @return unicode �s�X�r��
	 */
	public static String big5ToUnicode(String s)
	{
	    try
	    {
	      return new String(s.getBytes("Big5"), "utf-8");
	    }
	    catch (UnsupportedEncodingException ex)
	    {
	      return null;
	    }
	}
	
	/**
	 * ��o�ثe���
	 * @return ���
	 */
	public static int getMonth()
	{
	    Calendar  calendar = null;
	    calendar = Calendar.getInstance();
	    calendar.setTime(new Date());
	    return 1 + calendar.get(Calendar.MONTH);
	}
	
	public static int getYear()
	{
	    Calendar  calendar = null;
	    calendar = Calendar.getInstance();
	    calendar.setTime(new Date());
	    return calendar.get(Calendar.YEAR);		
	}
		
	/**
	 * ��o�ثe����~
	 * @return ����~
	 */
	public static int getROCYear()
	{
	    Calendar  calendar = null;
	    calendar = Calendar.getInstance();
	    calendar.setTime(new Date());
	    return calendar.get(Calendar.YEAR)-1911;
	}
	
	/**
	 * ��o�榡�ƪ��褸�~ yyyy/mm/dd
	 * @return �榡�ƪ��褸�~
	 */
	public static String getEngDate()
	{
	    Calendar  calendar = null;
	    calendar = Calendar.getInstance();
	    calendar.setTime(new Date());
	    String yyyy="0000", mm="00", dd="00",mm_1="00";
	    yyyy = yyyy + calendar.get(Calendar.YEAR);
	    mm = mm + (int)getMonth();
	    dd = dd + calendar.get(Calendar.DAY_OF_MONTH);
	    yyyy = yyyy.substring(yyyy.length()-4);
	    mm   = mm.substring(mm.length()-2,mm.length());
	    dd   = dd.substring(dd.length()-2);
	    return yyyy + "/" + mm + "/" +  dd;
	}
	
	/**
	 * ��o�榡�ƪ�����~ yyy/mm/dd
	 * @return �榡�ƪ�����~
	 */
	public static String GetROCDate()
    {
      Calendar  calendar = null;
      calendar = Calendar.getInstance();
      calendar.setTime(new Date());
      String yyyy="0000", yyy="000", mm="00", dd="00",mm_1="00";
      yyy = yyy + (int)getROCYear();
      mm = mm + (int)getMonth();
      dd = dd + calendar.get(Calendar.DAY_OF_MONTH);
      yyy = yyy.substring(yyyy.length()-3);
      mm   = mm.substring(mm.length()-2,mm.length());
      dd   = dd.substring(dd.length()-2);
      return yyy + "/" + mm + "/" +  dd;
    }
	
	/**
	 * ��o���Ѫ�����r��
	 * @return yyyMMdd
	 */
	public static String getToday()
	{
	    Calendar  calendar = null;
	    calendar = Calendar.getInstance();
	    calendar.setTime(new Date());
	    String yyyy="0000", yyy="000", mm="00", dd="00",mm_1="00";
	    yyy = yyy + (int)getROCYear();
	    mm = mm + (int)getMonth();
	    dd = dd + calendar.get(Calendar.DAY_OF_MONTH);
	    yyy = yyy.substring(yyyy.length()-3);
	    mm   = mm.substring(mm.length()-2,mm.length());
	    dd   = dd.substring(dd.length()-2);
	    return yyy + mm + dd;
	}
	
	/**
	 * �̿�J���榡�Ө�X��Ƥ����ƭȡA�H�զ��Ʀr�r���X
	 * ����J�榡�h�̭��ƿ�X
	 * ����ɶ��榡�� java �즳�榡�w�q���ǡA�Ҧp�G  yyyy/MM/dd hh:mm:ss
	 * @param data		���榡���r��
	 * @param pattern   �r��榡
	 * @return			�L�榡���Ʀr�r��
	 */
	public static String extractDateTimeDigit(String data, String pattern)
	{
		String ret = "";
		
		if (isEmpty(pattern) || isEmpty(data))
		{
			return data;
		}

		int y2 = -1, y4 = -1, mn2 = -1, d2 = -1, h2 = -1, mi2 = -1, s2 = -1;
		
		y4 = pattern.indexOf("yyyy");
		if (y4 == -1)
		{
			y2 = pattern.indexOf("yy");
		}
		
		mn2 = pattern.indexOf("MM");
		d2 = pattern.indexOf("dd");
		h2 = pattern.indexOf("hh");
		mi2 = pattern.indexOf("mm");
		s2 = pattern.indexOf("ss");
		
		if (y4 >= 0 && data.length() > y4)
		{
			ret += data.substring(y4, y4 + 4);
		}
		else
		{
			if (y2 >= 0 && data.length() > y2)
			{
				ret += data.substring(y2, y2 + 2);				
			}
		}
		
		if (mn2 >= 0 && data.length() > mn2)
		{
			ret += data.substring(mn2, mn2 + 2);
		}
		
		if (d2 >= 0 && data.length() > d2)
		{
			ret += data.substring(d2, d2 + 2);
		}
		
		if (h2 >= 0 && data.length() > h2)
		{
			ret += data.substring(h2, h2 + 2);
		}

		if (mi2 >= 0 && data.length() > mi2)
		{
			ret += data.substring(mi2, mi2 + 2);
		}

		if (s2 >= 0 && data.length() > s2)
		{
			ret += data.substring(s2, s2 + 2);
		}
		
		return ret;
	}
	
	/**
	 * �N��J���ƭȦr��榡�ơA�i��w�榡
	 * �Y����J�榡�A�w�]���G  12345 -> 12,345.00
	 * @param data  �ƭȦr��
	 * @param pattern �� Java �W�檺�榡�Ʀr��A�i�� null �� �Ŧr��
	 * @return �w�榡�Ƥ��ƭȦr��
	 */
	public static String toNumberFormat(String data, String pattern)
	{
		String defualPattern = ",##0.00";
		data=data.trim();
		if(data.length()==0) data="0";

		DecimalFormat f = null;
		if (pattern == null || pattern.isEmpty())
		{
			f = new DecimalFormat(defualPattern);	    	   
		}
		else
		{
			f = new DecimalFormat(pattern);
		}
		//f.setDecimalSeparatorAlwaysShown(true);

		return f.format(Double.parseDouble(data));
	}

	/**
	 * �N�榡�ƪ��ƭȦr��A�ഫ���L�榡�r��
	 * ���t���O�d
	 * @param data �榡�ƪ��ƭȦr��
	 * @return �L�榡�r��
	 */
	public static String numberUnFormat(String data)
	{
		String s = null;
		byte [] b = data.getBytes();
		byte [] c = new byte[b.length];
		
		int j = 0;
		for(int i=0; i<b.length; i++)
		{
			if (b[i] == ',')
			{
				continue;
			}
			
			c[j] = b[i];
			j++;
		}
		
		s = new String(c, 0, j);
		return s;
	}
	
	/**
	 * �N�榡�ƪ��ƭȦr��A�ഫ���L�榡�r��
	 * ���t���O�d
	 * @param data �榡�ƪ��ƭȦr��
	 * @return �L�榡�r��
	 */
	public static String numberUnFormat2(String data)
	{
		String s = null;
		byte [] b = data.getBytes();
		byte [] c = new byte[b.length];
		
		int j = 0;
		for(int i=0; i<b.length; i++)
		{
			if (b[i] == ',' || b[i] == '.')
			{
				continue;
			}
			
			c[j] = b[i];
			j++;
		}
		
		s = new String(c, 0, j);
		return s;
	}
	
	/**
	 * ²�檺����榡��
	 * @param data ����r��
	 * @param pattern �榡�Ʀr��
	 * @return �榡�Ƥ��
	 */
	public static String toDateFormat(String data, String pattern)
	{
		String rt = data;
		if (data.length() < 6 && data.length() > 8)
		{
			return rt;
		}
		String yyyy = "";
		String mm = "";
		String dd = "";						
		
		if (data.length() == 8)
		{
			yyyy = data.substring(0, 4);
			mm = data.substring(4, 6);
			dd = data.substring(6, 8);	
			rt = yyyy + pattern + mm + pattern + dd;
		}else if(data.length() == 6){
			yyyy = data.substring(0, 4);
			mm = data.substring(4, 6);
			rt = yyyy + pattern + mm;
		}
				
		return rt;
	}

	/**
	 * ²�檺�ɶ��榡��
	 * @param data �ɶ��r��
	 * @param pattern �榡�Ʀr��
	 * @return �榡�Ʈɶ�
	 */
	public static String toTimeFormat(String data, String pattern)
	{
		String rt = data;
		if (data == null)
		{
			return "";
		}
		if (data.length() < 6 && data.length() > 8)
		{
			return rt;
		}
		String hh = "";
		String mm = "";
		String ss = "";						
		
		if(data.length() == 6)
		{
			hh = data.substring(0, 2);
			mm = data.substring(2, 4);
			ss = data.substring(4, 6);
			
			rt = hh + pattern + mm + pattern + ss;
		}
				
		return rt;
	}
	
	/**
	 * ²�檺����榡��
	 * @param data ����r��
	 * @param pattern �榡�Ʀr��
	 * @return �榡�Ƥ��
	 */
	public static String toDateFormat_1(String data, String pattern)
	{
		String rt = data;
		if (data.length() < 6 && data.length() > 8)
		{
			return rt;
		}
		String yyyy = "";
		String mm = "";
		String dd = "";						
		
		if (data.length() == 8)
		{
			yyyy = data.substring(0, 4);
			mm = data.substring(4, 6);
			dd = data.substring(6, 8);	
			rt = yyyy.substring(1) + pattern + mm + pattern + dd;
		}else if(data.length() == 6){
			yyyy = data.substring(0, 4);
			mm = data.substring(4, 6);
			rt = yyyy.substring(1) + pattern + mm;
		}
				
		return rt;
	}
	
	public static Double toDouble(String data)
	{
		Double rt = null;
		
		try
		{
			String s = numberUnFormat(data);
			if (s != null && !s.isEmpty())
			{
				//�յ��� double
				Double d = new Double(s);
				rt = d;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return rt;		
	}
	
	
	/* 
	 * �k��ťզ�newLength(��ƾa��)
	 * @param org
	 * @param newLength
	 * @throws ArrayIndexOutOfBoundsException
	 */
	public static String addSpaceOnRight(String org, int newLength){
		
		byte[] newArr = new byte[newLength];
		
		Arrays.fill( newArr, (byte)32 );
				
		byte[] orgByteArr = org.getBytes();
		System.arraycopy(orgByteArr, 0, newArr, 0, orgByteArr.length);
		
		return new String(newArr);
	}
	
	
	public static String addSpaceOnRight2(String org, int newLength){
		byte[] newArr = new byte[newLength];
		
		Arrays.fill( newArr, (byte)32 );
		byte[] orgByteArr = null;
		try {
			orgByteArr = org.getBytes("BIG5");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		System.arraycopy(orgByteArr, 0, newArr, 0, (orgByteArr.length >= newLength?newLength:orgByteArr.length));
		return new String(newArr);
	}

	/**
	 * �k�ɺ�
	 * �� newLength ���Ȥp�� ��J�r���׮ɡA�^�ǭ즳�r��
	 * @param org  �즳���r��
	 * @param pad  �n�ɺ����r��(byte)
	 * @param newLength ���
	 * @return �ɺ����r��
	 */
	public static String padOnRight(String org, byte pad, int newLength)
	{	
		if (org.length() > newLength)
		{
			return org;
		}
		
		byte[] newArr = new byte[newLength];
		
		Arrays.fill( newArr, pad );
				
		byte[] orgByteArr = org.getBytes();
		System.arraycopy(orgByteArr, 0, newArr, 0, orgByteArr.length);
		
		return new String(newArr);
	}
	
	/* 
	 * ����ťզ�newLength(��ƾa�k)
	 * @param org
	 * @param newLength
	 * @throws ArrayIndexOutOfBoundsException
	 */
	public static String addSpaceOnLeft(String org, int newLength){
		
		byte[] newArr = new byte[newLength];
		
		Arrays.fill( newArr, (byte)32 );
		
		byte[] orgByteArr = org.getBytes();
		System.arraycopy(orgByteArr, 0, newArr, newArr.length-orgByteArr.length, orgByteArr.length);
		
		return new String(newArr);
	}

	/**
	 * ���ɹs
	 * @param org
	 * @param newLength
	 * @return
	 */
	public static String padZeroLeft(String org, int newLength)
	{
		return padOnLeft(org, (byte) 0x30, newLength);
	}
	
	/**
	 * ���ɺ�
	 * �� newLength ���Ȥp�� ��J�r���׮ɡA�^�ǭ즳�r��
	 * @param org  �즳���r��
	 * @param pad  �n�ɺ����r��(byte)
	 * @param newLength ���
	 * @return �ɺ����r��
	 */
	public static String padOnLeft(String org, byte pad, int newLength)
	{
		if (org.length() > newLength)
		{
			return org;
		}
		
		byte[] newArr = new byte[newLength];
		
		Arrays.fill( newArr, pad );
		
		byte[] orgByteArr = org.getBytes();
		System.arraycopy(orgByteArr, 0, newArr, newArr.length-orgByteArr.length, orgByteArr.length);
		
		return new String(newArr);
	}
	
	/**
	 * ���� UUID �r��
	 * @return
	 */
	public static String genUUID()
	{
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
	/**
	 * �^��r�Y��j�g (���Դ�)
	 * @param actualName
	 * @return
	 */
	public String conertToTitleCase(String actualName) 
	{
		StringBuilder name = new StringBuilder(actualName);
		name.setCharAt(0, (char)(name.charAt(0) -32));
		for(int i = 1 ; i < name.length() ; i++)
		{
			if(name.charAt(i -1) == ' ' && name.charAt(i) != ' ')
			{
				name.setCharAt(i, (char)(name.charAt(i) -32));
			}
		}	
		return name.toString();
	}
	
	/**
	 * 20131025 huangpu SQL����r��
	 */
	public static String escapeSQL(String sql){
		String result = sql;
		if(sql != null && sql.length() != 0 && sql.indexOf("'") != -1){
			result = sql.replaceAll("'", "''");
		}
		return result;
	}
	
	/**
	 * 20131025 huangpu SQL NULL�ഫ
	 */
	public static String escapeSQLNull(Object sql){
		String result = "";
		if(sql == null){
			result = "NULL";
		}else{
			result = "'" + sql.toString() + "'";
		}
		return result;
	}
	
	/**
	 * �̿�J����סB�r���A�ɺ��r���סC�i���ɤΥk��
	 * @param s            �n�ɺ����r��
	 * @param n            �`���
	 * @param c            ��Ŧr��
	 * @param paddingLeft  �O�_�V���ɺ�
	 * @return             �ɺ��᪺�r��
	 */
	public static String paddingString(String s, int n, char c,	boolean paddingLeft) 
	{
		if (s == null) 
		{
			return s;
		}
		
		// may overflow int size... should not be a problem in real life
		//�ݦҼ{ double bytes�r��
		int add = 0;
		if (s.getBytes().length > s.length())
		{
			add = n - s.getBytes().length;
		}
		else
		{
			add = n - s.length(); 			
		}
		
		if(add <= 0)
		{
			return s;
		}
		StringBuffer str = new StringBuffer(s);
		char[] ch = new char[add];
		Arrays.fill(ch, c);
		if(paddingLeft)
		{
			str.insert(0, ch);
		} else {
			str.append(ch);
		}
		
		return str.toString();
	}

	
}

