package tw.org.twntch.po;

import java.io.Serializable;

public class WO_COMPANY_PROFILE_PK implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6546503514813751664L;

	public WO_COMPANY_PROFILE_PK() {
	}
	
	
	
	public WO_COMPANY_PROFILE_PK(String wo_company_id, String bill_type_id) {
		super();
		WO_COMPANY_ID = wo_company_id;
		BILL_TYPE_ID = bill_type_id;
	}



	private	String	WO_COMPANY_ID	;
	private	String	BILL_TYPE_ID ;

	
	public String getWO_COMPANY_ID() {
		return WO_COMPANY_ID;
	}

	public void setWO_COMPANY_ID(String wO_COMPANY_ID) {
		WO_COMPANY_ID = wO_COMPANY_ID;
	}

	public String getBILL_TYPE_ID() {
		return BILL_TYPE_ID;
	}
	public void setBILL_TYPE_ID(String bILL_TYPE_ID) {
		BILL_TYPE_ID = bILL_TYPE_ID;
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((BILL_TYPE_ID == null) ? 0 : BILL_TYPE_ID.hashCode());
		result = prime * result
				+ ((WO_COMPANY_ID == null) ? 0 : WO_COMPANY_ID.hashCode());
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
		WO_COMPANY_PROFILE_PK other = (WO_COMPANY_PROFILE_PK) obj;
		if (BILL_TYPE_ID == null) {
			if (other.BILL_TYPE_ID != null)
				return false;
		} else if (!BILL_TYPE_ID.equals(other.BILL_TYPE_ID))
			return false;
		if (WO_COMPANY_ID == null) {
			if (other.WO_COMPANY_ID != null)
				return false;
		} else if (!WO_COMPANY_ID.equals(other.WO_COMPANY_ID))
			return false;
		return true;
	}
	
	
	
	
}
