package tw.org.twntch.po;

import java.io.Serializable;

public class PI_SND_PROFILE_PK implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3968663778364097834L;

	public PI_SND_PROFILE_PK() {
	}
	
	

	



	public PI_SND_PROFILE_PK(String pI_COMPANY_ID, String sND_COMPANY_ID,
			String bILL_TYPE_ID) {
		super();
		PI_COMPANY_ID = pI_COMPANY_ID;
		SND_COMPANY_ID = sND_COMPANY_ID;
		BILL_TYPE_ID = bILL_TYPE_ID;
	}







	private	String	PI_COMPANY_ID	;
	private	String	SND_COMPANY_ID ;
	private	String	BILL_TYPE_ID ;

	public String getPI_COMPANY_ID() {
		return PI_COMPANY_ID;
	}
	public void setPI_COMPANY_ID(String pI_COMPANY_ID) {
		PI_COMPANY_ID = pI_COMPANY_ID;
	}
	public String getSND_COMPANY_ID() {
		return SND_COMPANY_ID;
	}
	public void setSND_COMPANY_ID(String sND_COMPANY_ID) {
		SND_COMPANY_ID = sND_COMPANY_ID;
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
				+ ((PI_COMPANY_ID == null) ? 0 : PI_COMPANY_ID.hashCode());
		result = prime * result
				+ ((SND_COMPANY_ID == null) ? 0 : SND_COMPANY_ID.hashCode());
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
		PI_SND_PROFILE_PK other = (PI_SND_PROFILE_PK) obj;
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
		if (SND_COMPANY_ID == null) {
			if (other.SND_COMPANY_ID != null)
				return false;
		} else if (!SND_COMPANY_ID.equals(other.SND_COMPANY_ID))
			return false;
		return true;
	}



	
	
	
}
