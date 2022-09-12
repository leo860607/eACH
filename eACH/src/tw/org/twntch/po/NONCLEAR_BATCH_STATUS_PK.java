package tw.org.twntch.po;

import java.io.Serializable;

public class NONCLEAR_BATCH_STATUS_PK implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5256731505740543515L;

	public NONCLEAR_BATCH_STATUS_PK() {
	}
	public NONCLEAR_BATCH_STATUS_PK(String bIZDATE, String cLEARINGPHASE,
			Integer bATCH_PROC_SEQ) {
		BIZDATE = bIZDATE;
		CLEARINGPHASE = cLEARINGPHASE;
		BATCH_PROC_SEQ = bATCH_PROC_SEQ;
	}
	
	private	String  BIZDATE	;
	private	String  CLEARINGPHASE	;
	private	Integer	BATCH_PROC_SEQ	;
	
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
	public Integer getBATCH_PROC_SEQ() {
		return BATCH_PROC_SEQ;
	}
	public void setBATCH_PROC_SEQ(Integer bATCH_PROC_SEQ) {
		BATCH_PROC_SEQ = bATCH_PROC_SEQ;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((BATCH_PROC_SEQ == null) ? 0 : BATCH_PROC_SEQ.hashCode());
		result = prime * result + ((BIZDATE == null) ? 0 : BIZDATE.hashCode());
		result = prime * result
				+ ((CLEARINGPHASE == null) ? 0 : CLEARINGPHASE.hashCode());
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
		NONCLEAR_BATCH_STATUS_PK other = (NONCLEAR_BATCH_STATUS_PK) obj;
		if (BATCH_PROC_SEQ == null) {
			if (other.BATCH_PROC_SEQ != null)
				return false;
		} else if (!BATCH_PROC_SEQ.equals(other.BATCH_PROC_SEQ))
			return false;
		if (BIZDATE == null) {
			if (other.BIZDATE != null)
				return false;
		} else if (!BIZDATE.equals(other.BIZDATE))
			return false;
		if (CLEARINGPHASE == null) {
			if (other.CLEARINGPHASE != null)
				return false;
		} else if (!CLEARINGPHASE.equals(other.CLEARINGPHASE))
			return false;
		return true;
	}
	
	
	
}
