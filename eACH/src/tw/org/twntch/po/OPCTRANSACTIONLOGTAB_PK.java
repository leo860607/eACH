package tw.org.twntch.po;

import java.io.Serializable;

public class OPCTRANSACTIONLOGTAB_PK implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1992228716151746583L;
	private String STAN;
	private String TXDATE;
	
	public OPCTRANSACTIONLOGTAB_PK() {
		// TODO Auto-generated constructor stub
	}
	
	public OPCTRANSACTIONLOGTAB_PK(String sTAN, String tXDATE) {
		super();
		STAN = sTAN;
		TXDATE = tXDATE;
	}
	public String getSTAN() {
		return STAN;
	}
	public void setSTAN(String sTAN) {
		STAN = sTAN;
	}
	public String getTXDATE() {
		return TXDATE;
	}
	public void setTXDATE(String tXDATE) {
		TXDATE = tXDATE;
	}
}
