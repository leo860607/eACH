package tw.org.twntch.form;

import java.util.List;

public class Ap_Status_Form extends CommonForm{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8724024546608700570L;
	private String BGBK_ID;
	private String APID;
	private String MBAPSTATUS;
	private String MBSYSSTATUS;
	private List scaseary;

	public String getBGBK_ID() {
		return BGBK_ID;
	}

	public void setBGBK_ID(String bGBK_ID) {
		BGBK_ID = bGBK_ID;
	}

	public String getAPID() {
		return APID;
	}

	public void setAPID(String aPID) {
		APID = aPID;
	}

	public String getMBAPSTATUS() {
		return MBAPSTATUS;
	}

	public void setMBAPSTATUS(String mBAPSTATUS) {
		MBAPSTATUS = mBAPSTATUS;
	}

	public String getMBSYSSTATUS() {
		return MBSYSSTATUS;
	}

	public void setMBSYSSTATUS(String mBSYSSTATUS) {
		MBSYSSTATUS = mBSYSSTATUS;
	}

	public List getScaseary() {
		return scaseary;
	}

	public void setScaseary(List scaseary) {
		this.scaseary = scaseary;
	}
	
}
