package tw.org.twntch.po;

import java.io.Serializable;

public class SC_COMPANY_PROFILE_PK implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6199538045217835829L;
	private String COMPANY_ID ;
	private String TXN_ID ;
	private String SND_BRBK_ID ;
	public SC_COMPANY_PROFILE_PK() {
	}
	public SC_COMPANY_PROFILE_PK(String cOMPANY_ID, String tXN_ID,
			String sND_BRBK_ID) {
		super();
		COMPANY_ID = cOMPANY_ID;
		TXN_ID = tXN_ID;
		SND_BRBK_ID = sND_BRBK_ID;
	}
	public String getCOMPANY_ID() {
		return COMPANY_ID;
	}
	public void setCOMPANY_ID(String cOMPANY_ID) {
		COMPANY_ID = cOMPANY_ID;
	}
	public String getTXN_ID() {
		return TXN_ID;
	}
	public void setTXN_ID(String tXN_ID) {
		TXN_ID = tXN_ID;
	}
	public String getSND_BRBK_ID() {
		return SND_BRBK_ID;
	}
	public void setSND_BRBK_ID(String sND_BRBK_ID) {
		SND_BRBK_ID = sND_BRBK_ID;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((COMPANY_ID == null) ? 0 : COMPANY_ID.hashCode());
		result = prime * result
				+ ((SND_BRBK_ID == null) ? 0 : SND_BRBK_ID.hashCode());
		result = prime * result + ((TXN_ID == null) ? 0 : TXN_ID.hashCode());
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
		SC_COMPANY_PROFILE_PK other = (SC_COMPANY_PROFILE_PK) obj;
		if (COMPANY_ID == null) {
			if (other.COMPANY_ID != null)
				return false;
		} else if (!COMPANY_ID.equals(other.COMPANY_ID))
			return false;
		if (SND_BRBK_ID == null) {
			if (other.SND_BRBK_ID != null)
				return false;
		} else if (!SND_BRBK_ID.equals(other.SND_BRBK_ID))
			return false;
		if (TXN_ID == null) {
			if (other.TXN_ID != null)
				return false;
		} else if (!TXN_ID.equals(other.TXN_ID))
			return false;
		return true;
	}

	
	
}
