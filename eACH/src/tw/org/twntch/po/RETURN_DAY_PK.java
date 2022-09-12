package tw.org.twntch.po;

import java.io.Serializable;
public class RETURN_DAY_PK implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8730361223381221676L;
	private	String	TXN_ID	;
	private	String	ACTIVE_DATE	;
	
	public RETURN_DAY_PK(){}
	
	public RETURN_DAY_PK(String TXN_ID, String ACTIVE_DATE){
		this.TXN_ID = TXN_ID;
		this.ACTIVE_DATE = ACTIVE_DATE;
	}
	
	public String getTXN_ID() {
		return TXN_ID;
	}
	public void setTXN_ID(String tXN_ID) {
		TXN_ID = tXN_ID;
	}
	public String getACTIVE_DATE() {
		return ACTIVE_DATE;
	}
	public void setACTIVE_DATE(String aCTIVE_DATE) {
		ACTIVE_DATE = aCTIVE_DATE;
	}
}
