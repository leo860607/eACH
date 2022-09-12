package tw.org.twntch.po;

import java.io.Serializable;

public class EACH_ROLE_FUNC_PK implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2718470974812994864L;
	private String FUNC_ID;
	private String ROLE_ID;
	
	
	public EACH_ROLE_FUNC_PK() {
	}
	public EACH_ROLE_FUNC_PK(String fUNC_ID, String rOLE_ID) {
		FUNC_ID = fUNC_ID;
		ROLE_ID = rOLE_ID;
	}
	public String getFUNC_ID() {
		return FUNC_ID;
	}
	public void setFUNC_ID(String fUNC_ID) {
		FUNC_ID = fUNC_ID;
	}
	public String getROLE_ID() {
		return ROLE_ID;
	}
	public void setROLE_ID(String rOLE_ID) {
		ROLE_ID = rOLE_ID;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((FUNC_ID == null) ? 0 : FUNC_ID.hashCode());
		result = prime * result + ((ROLE_ID == null) ? 0 : ROLE_ID.hashCode());
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
		EACH_ROLE_FUNC_PK other = (EACH_ROLE_FUNC_PK) obj;
		if (FUNC_ID == null) {
			if (other.FUNC_ID != null)
				return false;
		} else if (!FUNC_ID.equals(other.FUNC_ID))
			return false;
		if (ROLE_ID == null) {
			if (other.ROLE_ID != null)
				return false;
		} else if (!ROLE_ID.equals(other.ROLE_ID))
			return false;
		return true;
	}
	
	
}
