package tw.org.twntch.po;

import java.io.Serializable;

public class AGENT_FEE_CODE_PK implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2999489148213394537L;
	private String FEE_ID;
	private String COMPANY_ID;
	private String START_DATE;
	
	
	
	
	public AGENT_FEE_CODE_PK(String fEE_ID, String cOMPANY_ID, String sTART_DATE) {
		super();
		FEE_ID = fEE_ID;
		COMPANY_ID = cOMPANY_ID;
		START_DATE = sTART_DATE;
	}
	public AGENT_FEE_CODE_PK() {
		
	}
	public String getFEE_ID() {
		return FEE_ID;
	}
	public void setFEE_ID(String fEE_ID) {
		FEE_ID = fEE_ID;
	}
	public String getCOMPANY_ID() {
		return COMPANY_ID;
	}
	public void setCOMPANY_ID(String cOMPANY_ID) {
		COMPANY_ID = cOMPANY_ID;
	}
	public String getSTART_DATE() {
		return START_DATE;
	}
	public void setSTART_DATE(String sTART_DATE) {
		START_DATE = sTART_DATE;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((COMPANY_ID == null) ? 0 : COMPANY_ID.hashCode());
		result = prime * result + ((FEE_ID == null) ? 0 : FEE_ID.hashCode());
		result = prime * result
				+ ((START_DATE == null) ? 0 : START_DATE.hashCode());
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
		AGENT_FEE_CODE_PK other = (AGENT_FEE_CODE_PK) obj;
		if (COMPANY_ID == null) {
			if (other.COMPANY_ID != null)
				return false;
		} else if (!COMPANY_ID.equals(other.COMPANY_ID))
			return false;
		if (FEE_ID == null) {
			if (other.FEE_ID != null)
				return false;
		} else if (!FEE_ID.equals(other.FEE_ID))
			return false;
		if (START_DATE == null) {
			if (other.START_DATE != null)
				return false;
		} else if (!START_DATE.equals(other.START_DATE))
			return false;
		return true;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AGENT_FEE_CODE_PK [FEE_ID=");
		builder.append(FEE_ID);
		builder.append(", COMPANY_ID=");
		builder.append(COMPANY_ID);
		builder.append(", START_DATE=");
		builder.append(START_DATE);
		builder.append("]");
		return builder.toString();
	}
	
}
