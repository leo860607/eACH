package tw.org.twntch.socket;

import java.io.ByteArrayOutputStream;
import tw.org.twntch.util.HSMMACUtils;

public class HSMSuipRequestData {
	/**
	 * Hex String Ex:0xCA
	 */
	String function = "D1";
	/**
	 * 依 ACH 規範之工作基碼 ID  Ex：022，A03
	 */
	String keyId = "013";
	/**
	 * Hex String 
	 * 0x00：eACH系統
	 * 0x01：eDDA系統
	 */
	String keyFlag =  "03" ;
	
	/**
	 * ‘N’: New Key ‘O’: Old Key
	 */
	String keyVersion = "O";
	
	/**
	 * Hex String
	 * Initial Chaining Vector
	 */
	String icv = "0000000000000000";
	/**
	 * MAX 32 bytes
	 */
	String data;
	
	private static final int TYPE_A = 0;
	private static final int TYPE_X = 1;
	private static final int TYPE_N = 2;
	
	public byte[] toByteArray() throws Exception{
		ByteArrayOutputStream bso = new ByteArrayOutputStream();
		bso.write(getBytes(function,TYPE_X,1));
		bso.write(getBytes(keyId,TYPE_A,3));
		bso.write(getBytes(keyFlag,TYPE_X,1));
		bso.write(getBytes(keyVersion,TYPE_A,1));
		bso.write(getBytes(icv,TYPE_X,8));
		byte[] databs = (data == null || data.length() == 0) ? "".getBytes():data.getBytes();
		
		int p = 8 - (databs.length % 8);
		
		//補0
		if( p < 8) {
			byte[] zeroByte = getBytes("0",TYPE_X,p);
			byte[] pByte = new byte[databs.length + zeroByte.length];
			
			System.arraycopy(databs, 0, pByte, 0, databs.length);
			System.arraycopy(zeroByte, 0, pByte, databs.length, zeroByte.length);
			
			databs = pByte;
		}
		
		bso.write(getBytes(String.valueOf(databs.length),TYPE_N,2));//長度
		bso.write(databs);
		
		return bso.toByteArray();
		
	}
	
	private byte[] getBytes(String data,int type,int len){
		String tmpStr = null;
		data = (data == null || data.length() == 0) ? "" : data;

		switch(type){
		case TYPE_A:
			tmpStr = HSMMACUtils.pendingString(data, len, ' ', -1);
			return data.getBytes();
		case TYPE_X:
			tmpStr = HSMMACUtils.pendingString(data, len * 2, '0', -1);
			return HSMMACUtils.hexToByte(tmpStr);
		case TYPE_N:
			tmpStr = HSMMACUtils.pendingString(data, len , '0',1);
			return tmpStr.getBytes();
		default:
			return data.getBytes();
		}
	}
	
	public String getFunction() {
		return function;
	}
	public void setFunction(String function) {
		this.function = function;
	}
	public String getKeyId() {
		return keyId;
	}
	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}
	public String getKeyFlag() {
		return keyFlag;
	}
	public void setKeyFlag(String keyFlag) {
		this.keyFlag = keyFlag;
	}
	public String getKeyVersion() {
		return keyVersion;
	}
	public void setKeyVersion(String keyVersion) {
		this.keyVersion = keyVersion;
	}
	public String getIcv() {
		return icv;
	}
	public void setIcv(String icv) {
		this.icv = icv;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("HSMSuipRequestData==>\r\n")
		  .append("function:").append(function).append("\r\n")
		  .append("keyId:").append(keyId).append("\r\n")
		  .append("keyFlag:").append(keyFlag).append("\r\n")
		  .append("keyVersion:").append(keyVersion).append("\r\n")
		  .append("icv:").append(icv).append("\r\n")
		  .append("data:").append(data).append("\r\n");
		return sb.toString();
	}
}
