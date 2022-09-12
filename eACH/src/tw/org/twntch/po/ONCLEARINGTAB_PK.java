package tw.org.twntch.po;

import java.io.Serializable;

public class ONCLEARINGTAB_PK implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1584679321343892813L;
	private String BIZDATE;
	private String CLEARINGPHASE;
	private String BANKID;
	private String PCODE;
	
	public ONCLEARINGTAB_PK() {
	}
	
	public ONCLEARINGTAB_PK(String bIZDATE, String cLEARINGPHASE,String bANKID, String pCODE) {
		super();
		BIZDATE = bIZDATE;
		CLEARINGPHASE = cLEARINGPHASE;
		BANKID = bANKID;
		PCODE = pCODE;
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

	public String getBANKID() {
		return BANKID;
	}

	public void setBANKID(String bANKID) {
		BANKID = bANKID;
	}

	public String getPCODE() {
		return PCODE;
	}

	public void setPCODE(String pCODE) {
		PCODE = pCODE;
	}
	
}
