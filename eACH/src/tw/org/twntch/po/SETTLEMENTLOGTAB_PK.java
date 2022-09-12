package tw.org.twntch.po;

import java.io.Serializable;

public class SETTLEMENTLOGTAB_PK implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2782370722036024030L;
	private String TXDATE;
	private String STAN;
	public SETTLEMENTLOGTAB_PK() {
	}
	public SETTLEMENTLOGTAB_PK(String tXDATE, String sTAN) {
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
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((STAN == null) ? 0 : STAN.hashCode());
		result = prime * result + ((TXDATE == null) ? 0 : TXDATE.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SETTLEMENTLOGTAB_PK other = (SETTLEMENTLOGTAB_PK) obj;
		if (STAN == null) {
			if (other.STAN != null)
				return false;
		} else if (!STAN.equals(other.STAN))
			return false;
		if (TXDATE == null) {
			if (other.TXDATE != null)
				return false;
		} else if (!TXDATE.equals(other.TXDATE))
			return false;
		return true;
	}
	
	
}
