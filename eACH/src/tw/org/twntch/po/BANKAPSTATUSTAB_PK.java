package tw.org.twntch.po;

import java.io.Serializable;

public class BANKAPSTATUSTAB_PK implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2844552890056462681L;
	private String BGBK_ID;
	private String APID;
	
	public BANKAPSTATUSTAB_PK() {
	}
	public BANKAPSTATUSTAB_PK(String bGBK_ID, String aPID) {
		BGBK_ID = bGBK_ID;
		APID = aPID;
	}
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
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((APID == null) ? 0 : APID.hashCode());
		result = prime * result + ((BGBK_ID == null) ? 0 : BGBK_ID.hashCode());
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
		BANKAPSTATUSTAB_PK other = (BANKAPSTATUSTAB_PK) obj;
		if (APID == null) {
			if (other.APID != null)
				return false;
		} else if (!APID.equals(other.APID))
			return false;
		if (BGBK_ID == null) {
			if (other.BGBK_ID != null)
				return false;
		} else if (!BGBK_ID.equals(other.BGBK_ID))
			return false;
		return true;
	}
}
