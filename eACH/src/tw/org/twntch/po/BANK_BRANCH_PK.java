package tw.org.twntch.po;

import java.io.Serializable;

public class BANK_BRANCH_PK implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8605216767974289486L;
	
	public BANK_BRANCH_PK() {
	}
	public BANK_BRANCH_PK(String bGBK_ID, String bRBK_ID) {
		BGBK_ID = bGBK_ID;
		BRBK_ID = bRBK_ID;
	}
	private	String	BGBK_ID	;
	private	String	BRBK_ID ;
	public String getBGBK_ID() {
		return BGBK_ID;
	}
	public void setBGBK_ID(String bGBK_ID) {
		BGBK_ID = bGBK_ID;
	}
	public String getBRBK_ID() {
		return BRBK_ID;
	}
	public void setBRBK_ID(String bRBK_ID) {
		BRBK_ID = bRBK_ID;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((BGBK_ID == null) ? 0 : BGBK_ID.hashCode());
		result = prime * result + ((BRBK_ID == null) ? 0 : BRBK_ID.hashCode());
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
		BANK_BRANCH_PK other = (BANK_BRANCH_PK) obj;
		if (BGBK_ID == null) {
			if (other.BGBK_ID != null)
				return false;
		} else if (!BGBK_ID.equals(other.BGBK_ID))
			return false;
		if (BRBK_ID == null) {
			if (other.BRBK_ID != null)
				return false;
		} else if (!BRBK_ID.equals(other.BRBK_ID))
			return false;
		return true;
	}
	
	
	
}
