package tw.org.twntch.po;

import java.io.Serializable;

public class PI_COMPANY_PROFILE_PK implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7440172946664063369L;
	
	public PI_COMPANY_PROFILE_PK() {
	}
	
	
	
	public PI_COMPANY_PROFILE_PK(String pi_company_id, String bill_type_id) {
		super();
		PI_COMPANY_ID = pi_company_id;
		BILL_TYPE_ID = bill_type_id;
	}



	private	String	PI_COMPANY_ID	;
	private	String	BILL_TYPE_ID ;

	
	public String getBILL_TYPE_ID() {
		return BILL_TYPE_ID;
	}
	public void setBILL_TYPE_ID(String bILL_TYPE_ID) {
		BILL_TYPE_ID = bILL_TYPE_ID;
	}

	public String getPI_COMPANY_ID() {
		return PI_COMPANY_ID;
	}

	public void setPI_COMPANY_ID(String pI_COMPANY_ID) {
		PI_COMPANY_ID = pI_COMPANY_ID;
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((BILL_TYPE_ID == null) ? 0 : BILL_TYPE_ID.hashCode());
		result = prime * result
				+ ((PI_COMPANY_ID == null) ? 0 : PI_COMPANY_ID.hashCode());
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
		PI_COMPANY_PROFILE_PK other = (PI_COMPANY_PROFILE_PK) obj;
		if (BILL_TYPE_ID == null) {
			if (other.BILL_TYPE_ID != null)
				return false;
		} else if (!BILL_TYPE_ID.equals(other.BILL_TYPE_ID))
			return false;
		if (PI_COMPANY_ID == null) {
			if (other.PI_COMPANY_ID != null)
				return false;
		} else if (!PI_COMPANY_ID.equals(other.PI_COMPANY_ID))
			return false;
		return true;
	}
	
	
	
	
}
