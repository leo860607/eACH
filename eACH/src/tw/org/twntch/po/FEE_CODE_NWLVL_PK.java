package tw.org.twntch.po;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class FEE_CODE_NWLVL_PK implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6422475074502629835L;
	
	public FEE_CODE_NWLVL_PK() {
	}
	
	private String FEESEQ;
	
	public String getFEESEQ() {
		return FEESEQ;
	}
	public void setFEESEQ(String fEESEQ) {
		FEESEQ = fEESEQ;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((FEESEQ == null) ? 0 : FEESEQ.hashCode());
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
		FEE_CODE_NWLVL_PK other = (FEE_CODE_NWLVL_PK) obj;
		if (FEESEQ == null) {
			if (other.FEESEQ != null)
				return false;
		} else if (!FEESEQ.equals(other.FEESEQ))
			return false;
		return true;
	}

}
