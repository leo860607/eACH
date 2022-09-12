package tw.org.twntch.po;

import java.io.Serializable;

public class TXN_FEE_MAPPING_PK implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6662058596083006902L;
	private String TXN_ID;
	private String FEE_ID;
	private String START_DATE;
	
	public TXN_FEE_MAPPING_PK() {
		// TODO Auto-generated constructor stub
	}
	
	public TXN_FEE_MAPPING_PK(String TXN_ID, String FEE_ID, String START_DATE) {
		this.TXN_ID = TXN_ID;
		this.FEE_ID = FEE_ID;
		this.START_DATE= START_DATE;
	}
	
	public String getTXN_ID() {
		return TXN_ID;
	}
	public void setTXN_ID(String tXN_ID) {
		TXN_ID = tXN_ID;
	}
	public String getFEE_ID() {
		return FEE_ID;
	}
	public void setFEE_ID(String fEE_ID) {
		FEE_ID = fEE_ID;
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
		TXN_FEE_MAPPING_PK other = (TXN_FEE_MAPPING_PK) obj;
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
	
	
}
