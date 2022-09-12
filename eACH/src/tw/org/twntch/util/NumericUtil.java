package tw.org.twntch.util;

public class NumericUtil {
	/**
	 * Added by Richard Yang on 2004-01-09
	 * @param a_str_expr 表示待格式化的数值字符串
	 * @param a_i_decimal 表示格式化后的数值字符串中小数点后面的位数
	 * @return
	 * @author Modified by Richard Yang on 2004-01-11
	 */
	public static String formatNumberString(String a_str_expr, int a_i_decimal) {
		String str_nocomma = "";
		String str_intpart = "";
		String str_decpart = "";
		String str_result = "";
		StringBuffer str_buffer = new StringBuffer();
		double d_amt = 0;
		double d_temp = 0;
		boolean b_negative = false;
		try {
			if (a_str_expr.equals("")) {
				return "";
			} else {
				try {
					str_nocomma = deleteComma(a_str_expr);
					// 判斷正負號
					if (str_nocomma.indexOf("+") != -1) {
						int sign = str_nocomma.indexOf("+");
						if (sign == 0) {
							str_nocomma = str_nocomma.substring(1);
						} else {
							str_nocomma = str_nocomma.substring(0, str_nocomma.length() - 1);
						}
					} else if (str_nocomma.indexOf("-") != -1) {
						int sign = str_nocomma.indexOf("-");
						if (sign == 0) {
							str_nocomma = str_nocomma.substring(1);
						} else {
							str_nocomma = str_nocomma.substring(0, str_nocomma.length() - 1);
						}
						b_negative = true;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			d_amt = Double.parseDouble(str_nocomma);
			if (a_i_decimal != 0 && str_nocomma.indexOf(".") == -1) {
				d_temp = Math.pow(10, a_i_decimal);
				// d_amt = Math.round( d_amt * d_temp ) / d_temp;
				d_amt /= d_temp;
			} else if (a_i_decimal != 0 && str_nocomma.indexOf(".") != -1) {
				// do nothing
			} else {
				d_amt = Math.round(d_amt);
			}
			java.math.BigDecimal big = new java.math.BigDecimal(d_amt);
			java.math.BigDecimal one = new java.math.BigDecimal(1.0);
			str_nocomma = (big.divide(one, a_i_decimal, java.math.BigDecimal.ROUND_HALF_UP)).toString();
			if (str_nocomma.indexOf(".") != -1) {
				str_intpart = str_nocomma.substring(0, str_nocomma.indexOf("."));
				str_decpart = str_nocomma.substring(str_nocomma.indexOf(".") + 1);
			} else {
				str_intpart = str_nocomma;
				if (a_i_decimal != 0) {
					str_decpart = new Double(Math.pow(10, (-1) * a_i_decimal)).toString();
				} else {
					str_decpart = "";
				}
			}
			if (str_intpart.length() % 3 == 0) {
				for (int i = 0; i < (str_intpart.length() / 3); i++) {
					str_buffer.append(str_intpart.substring(i * 3, i * 3 + 3));
					str_buffer.append(",");
				}
				str_result = (str_buffer.toString()).substring(0, str_buffer.toString().length() - 1);
			} else if (str_intpart.length() % 3 == 1) {
				str_buffer.append(str_intpart.substring(0, 1));
				str_buffer.append(",");
				for (int i = 0; i < Math.floor((str_intpart.length() / 3)); i++) {
					str_buffer.append(str_intpart.substring(i * 3 + 1, i * 3 + 4));
					str_buffer.append(",");
				}
				str_result = (str_buffer.toString()).substring(0, str_buffer.toString().length() - 1);
			} else if (str_intpart.length() % 3 == 2) {
				str_buffer.append(str_intpart.substring(0, 2));
				str_buffer.append(",");
				for (int i = 0; i < Math.floor((str_intpart.length() / 3)); i++) {
					str_buffer.append(str_intpart.substring(i * 3 + 2, i * 3 + 5));
					str_buffer.append(",");
				}
				str_result = (str_buffer.toString()).substring(0, str_buffer.toString().length() - 1);
			}
			if (a_i_decimal != 0) {
				str_result = str_result + "." + str_decpart;
			} else {
				str_result = str_result + str_decpart;
			}
			if (b_negative) {
				str_result = "-" + str_result;
			}
		} catch (Exception exc) {
			str_result = "數值格式錯誤:" + a_str_expr;
		}
		return str_result;
	}
	
	/**
	 * 去掉撇節符號。例如：123,456.00 -> 123456.00
	 * 
	 * @param a_str_String
	 * @return
	 * @throws Exception
	 */
	public static String deleteComma(String a_str_String) throws Exception {
		int i;
		StringBuffer sb = new StringBuffer();
		String s = a_str_String;
		try {
			if (s == null) {
				return null;
			}
			// 去掉撇節符號。例如：123,456.00 -> 123456.00
			for (i = s.indexOf(","); i > 0;) {
				sb.append(s.substring(0, i));
				s = s.substring(i + 1);
				i = s.indexOf(",");
			}
			sb.append(s);
			return sb.toString();
		} catch (Exception exc) {
			throw new Exception("00001");
		}
	}
	public static void main(String[] args) {
		String test = formatNumberString("123456",0);
		System.out.println(test);
	}
}
