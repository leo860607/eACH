package tw.org.twntch.form;

import java.util.List;

public class Monitor_Form extends CommonForm{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3161364619557040276L;
	private String BGBK_ID;
	private String APID;
	private String MBAPSTATUS;
	private String MBSYSSTATUS;
	private String PATH;
	private String BIZDATE;
	private String CLEARINGPHASE;
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
	
	public String getPATH() {
		return PATH;
	}

	public void setPATH(String pATH) {
		PATH = pATH;
	}

	public String getBIZDATE() {
		return BIZDATE;
	}

	public void setBIZDATE(String bIZDATE) {
		BIZDATE = bIZDATE;
	}

	public String getCLEARINGPHASE() {
		return CLEARINGPHASE;
	}

	public void setCLEARINGPHASE(String cLEARINGPHASE) {
		CLEARINGPHASE = cLEARINGPHASE;
	}
	
	
}
