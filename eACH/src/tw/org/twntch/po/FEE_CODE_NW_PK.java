package tw.org.twntch.po;

import java.io.Serializable;

public class FEE_CODE_NW_PK implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 894984785926854356L;
	private	String 	FEE_UID	;
	
	public FEE_CODE_NW_PK() {
	}
	
	public FEE_CODE_NW_PK(String fEE_UID) {
		FEE_UID = fEE_UID;
	}
	
	public String getFEE_UID() {
		return FEE_UID;
	}
	public void setFEE_UID(String fEE_UID) {
		FEE_UID = fEE_UID;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((FEE_UID == null) ? 0 : FEE_UID.hashCode());
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
		FEE_CODE_NW_PK other = (FEE_CODE_NW_PK) obj;
		if (FEE_UID == null) {
			if (other.FEE_UID != null)
				return false;
		} else if (!FEE_UID.equals(other.FEE_UID))
			return false;
		return true;
	}
	
	
}
