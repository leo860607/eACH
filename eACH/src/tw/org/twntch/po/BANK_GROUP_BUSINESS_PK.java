package tw.org.twntch.po;

import java.io.Serializable;

public class BANK_GROUP_BUSINESS_PK implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7440172946664063369L;
	
	public BANK_GROUP_BUSINESS_PK() {
	}
	public BANK_GROUP_BUSINESS_PK(String bGBK_ID, String bUSINESS_TYPE_ID) {
		BGBK_ID = bGBK_ID;
		BUSINESS_TYPE_ID = bUSINESS_TYPE_ID;
	}
	private	String	BGBK_ID	;
	private	String	BUSINESS_TYPE_ID ;
	public String getBGBK_ID() {
		return BGBK_ID;
	}
	public void setBGBK_ID(String bGBK_ID) {
		BGBK_ID = bGBK_ID;
	}
	public String getBUSINESS_TYPE_ID() {
		return BUSINESS_TYPE_ID;
	}
	public void setBUSINESS_TYPE_ID(String bUSINESS_TYPE_ID) {
		BUSINESS_TYPE_ID = bUSINESS_TYPE_ID;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((BGBK_ID == null) ? 0 : BGBK_ID.hashCode());
		result = prime
				* result
				+ ((BUSINESS_TYPE_ID == null) ? 0 : BUSINESS_TYPE_ID.hashCode());
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
		BANK_GROUP_BUSINESS_PK other = (BANK_GROUP_BUSINESS_PK) obj;
		if (BGBK_ID == null) {
			if (other.BGBK_ID != null)
				return false;
		} else if (!BGBK_ID.equals(other.BGBK_ID))
			return false;
		if (BUSINESS_TYPE_ID == null) {
			if (other.BUSINESS_TYPE_ID != null)
				return false;
		} else if (!BUSINESS_TYPE_ID.equals(other.BUSINESS_TYPE_ID))
			return false;
		return true;
	}
	
	
}
