package tw.org.twntch.po;

import java.io.Serializable;

public class EACH_BATCH_NOTIFY_PK implements Serializable{
	
	
	public EACH_BATCH_NOTIFY_PK() {
	}
	public EACH_BATCH_NOTIFY_PK(String bIZDATE, String cLEARINGPHASE) {
		BIZDATE = bIZDATE;
		CLEARINGPHASE = cLEARINGPHASE;
	}
	private	String    	BIZDATE	;
	private	String    	CLEARINGPHASE	;
	
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
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		EACH_BATCH_NOTIFY_PK other = (EACH_BATCH_NOTIFY_PK) obj;
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
