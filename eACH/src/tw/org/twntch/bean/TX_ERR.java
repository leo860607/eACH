package tw.org.twntch.bean;

import java.math.BigDecimal;

public class TX_ERR {
	private String ERR_TYPE;
	private String TXDATE;
	private String TXDT;
	private String STAN;
	private String SENDERBANKID;
	private String OUTBANKID;
	private String INBANKID;
	private String SENDERID;
	private String PCODE;
	private BigDecimal TXAMT;
	public String getERR_TYPE() {
		return ERR_TYPE;
	}
	public void setERR_TYPE(String eRR_TYPE) {
		ERR_TYPE = eRR_TYPE;
	}
	public String getTXDATE() {
		return TXDATE;
	}
	public void setTXDATE(String tXDATE) {
		TXDATE = tXDATE;
	}
	public String getTXDT() {
		return TXDT;
	}
	public void setTXDT(String tXDT) {
		TXDT = tXDT;
	}
	public String getSTAN() {
		return STAN;
	}
	public void setSTAN(String sTAN) {
		STAN = sTAN;
	}
	public String getSENDERBANKID() {
		return SENDERBANKID;
	}
	public void setSENDERBANKID(String sENDERBANKID) {
		SENDERBANKID = sENDERBANKID;
	}
	public String getOUTBANKID() {
		return OUTBANKID;
	}
	public void setOUTBANKID(String oUTBANKID) {
		OUTBANKID = oUTBANKID;
	}
	public String getINBANKID() {
		return INBANKID;
	}
	public void setINBANKID(String iNBANKID) {
		INBANKID = iNBANKID;
	}
	public String getSENDERID() {
		return SENDERID;
	}
	public void setSENDERID(String sENDERID) {
		SENDERID = sENDERID;
	}
	public String getPCODE() {
		return PCODE;
	}
	public void setPCODE(String pCODE) {
		PCODE = pCODE;
	}
	public BigDecimal getTXAMT() {
		return TXAMT;
	}
	public void setTXAMT(BigDecimal tXAMT) {
		TXAMT = tXAMT;
	}
}
