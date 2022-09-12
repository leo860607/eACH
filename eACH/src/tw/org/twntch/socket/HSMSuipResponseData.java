package tw.org.twntch.socket;

import tw.org.twntch.util.HSMMACUtils;

public class HSMSuipResponseData {
	/**
	 * Hex String
	 * 0x00: Success
	 * 0x01: Fail
	 */
	String response;
	/**
	 * 0000
	 */
	String returnCode;
	/**
	 * 0000
	 */
	String reasonCode;
	/**
	 * The leading 4 bytes of latest block
	 */
	String mac;
	
	public HSMSuipResponseData(byte[] bs){
		int pos = 0;
		if(bs.length > pos +1 ) this.response = HSMMACUtils.byteToHex(bs[pos]);
		pos = pos + 1;
		byte[] intBytes = new byte[4];
		if(bs.length >= pos + 4){
			System.arraycopy(bs, pos, intBytes, 0, 4);
			this.returnCode = new String(intBytes);
		}
		pos = pos + 4;
		if(bs.length >= pos + 4){
			System.arraycopy(bs, pos, intBytes, 0, 4);
			this.reasonCode = new String(intBytes);
		}
		pos = pos + 4;
		if(bs.length >= pos + 4){
			System.arraycopy(bs, pos, intBytes, 0, 4);
			this.mac = HSMMACUtils.toHex(intBytes);
		}		
	}

	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	public String getReturnCode() {
		return returnCode;
	}
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}
	public String getReasonCode() {
		return reasonCode;
	}
	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("HSMSuipResponseData==>\r\n")
		  .append("response:").append(response).append("\r\n")
		  .append("returnCode:").append(returnCode).append("\r\n")
		  .append("reasonCode:").append(reasonCode).append("\r\n")
		  .append("mac:").append(mac).append("\r\n");
		return sb.toString();
	}
}
