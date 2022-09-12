package tw.org.twntch.util;

/**
 * StringUtils
 * 
 * @author 1300454
 * @date 2014/12/19-下午4:22:48
 */
public class HSMMACUtils {

	private static final String[] HEX = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F" };

	/**
	 * 在字串前或後補上指定的字元
	 * 
	 * @param source
	 *            要補指定字元的字串
	 * @param length
	 *            補完指定字元的字串
	 * @param pendingValue
	 *            指定字元,長度要為 1
	 * @param pendingDirection
	 *            補上字元的位置(前方或後方)
	 * @return 已補上字元的字串
	 */
	public static String pendingString(String source, int length,
			char pendingValue, int pendingDirection) {
		if (source == null)
			throw new IllegalArgumentException("source is null");

		if (length < 0)
			throw new IllegalArgumentException(
					"the value of the length less than and equal to 0");

		if (!(pendingDirection == 1 || pendingDirection == -1))
			throw new IllegalArgumentException(
					"the value of the direction is error");

		int sourceLength = source.length();

		if (length < sourceLength)
			return source;

		for (int i = 0; i < length - sourceLength; i++) {
			if (pendingDirection == 1)
				source = pendingValue + source;
			else
				source += pendingValue;
		}

		return source;
	}


	/**
	 * 16進位字串轉成byte[]
	 * 
	 * @param hexStr
	 * @return byte[]
	 */
	public static byte[] hexToByte(String hexStr) {
		byte[] bts = new byte[hexStr.length() / 2];
		for (int i = 0; i < bts.length; i++) {
			bts[i] = (byte) Integer.parseInt(
					hexStr.substring(2 * i, 2 * i + 2), 16);
		}
		return bts;
	}

	/**
	 * 將byte值轉為16進位，以字串傳回
	 * 
	 * @param b
	 *            singe byte
	 * @return Hex String
	 */
	public static String byteToHex(byte b) {

		int n = b;
		if (n < 0) {
			n &= 0xff;
		}
		return (HEX[n / 16]) + (HEX[n % 16]);
	}

	/**
	 * 將byte值轉為16進位,放回StringBuffer
	 * 
	 * @param b
	 *            singe byte
	 * @param sb
	 *            StringBuffer
	 */
	public static void byteToHex(byte b, StringBuffer sb) {

		int n = b;
		if (n < 0) {
			n &= 0xff;
		}
		sb.append(HEX[n / 16]).append(HEX[n % 16]);
	}

	/**
	 * 將byte[] 轉為16進位字串
	 * 
	 * @param byte[]
	 * @return hex string
	 */
	public static String toHex(byte[] b) {
		StringBuffer hex = new StringBuffer();

		for (int i = 0; i < b.length; i++) {
			byteToHex(b[i], hex);
		}

		return hex.toString();
	}
}
