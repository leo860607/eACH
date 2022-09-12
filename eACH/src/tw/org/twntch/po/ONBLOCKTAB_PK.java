package tw.org.twntch.po;

import java.io.Serializable;

public class ONBLOCKTAB_PK implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7923386334434878672L;
	private  String     TXDATE          ;    
	private  String     STAN            ;
	public ONBLOCKTAB_PK() {
		super();
	}
	public ONBLOCKTAB_PK(String tXDATE, String sTAN) {
		super();
		TXDATE = tXDATE;
		STAN = sTAN;
	}
	public String getTXDATE() {
		return TXDATE;
	}
	public void setTXDATE(String tXDATE) {
		TXDATE = tXDATE;
	}
	public String getSTAN() {
		return STAN;
	}
	public void setSTAN(String sTAN) {
		STAN = sTAN;
	}    
}
