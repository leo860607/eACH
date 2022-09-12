package tw.org.twntch.po;

import java.io.Serializable;

public class BANK_CTBK_PK  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -202774167000794267L;
	private String BGBK_ID;
	private String START_DATE;
	
	public BANK_CTBK_PK() {
	}
	
	public BANK_CTBK_PK(String bGBK_ID, String sTART_DATE) {
		super();
		BGBK_ID = bGBK_ID;
		START_DATE = sTART_DATE;
	}
	
	public String getBGBK_ID() {
		return BGBK_ID;
	}
	public void setBGBK_ID(String bGBK_ID) {
		BGBK_ID = bGBK_ID;
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
		result = prime * result + ((BGBK_ID == null) ? 0 : BGBK_ID.hashCode());
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
		BANK_CTBK_PK other = (BANK_CTBK_PK) obj;
		if (BGBK_ID == null) {
			if (other.BGBK_ID != null)
				return false;
		} else if (!BGBK_ID.equals(other.BGBK_ID))
			return false;
		if (START_DATE == null) {
			if (other.START_DATE != null)
				return false;
		} else if (!START_DATE.equals(other.START_DATE))
			return false;
		return true;
	}
	
	
	
}
