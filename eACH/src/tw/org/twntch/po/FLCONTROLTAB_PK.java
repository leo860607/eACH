package tw.org.twntch.po;

import java.io.Serializable;
import java.math.BigDecimal;

public class FLCONTROLTAB_PK implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7317179264229746556L;
	private String BIZDATE;
	private BigDecimal PROCSEQ;
	private String BATCHSEQ;
	
	public FLCONTROLTAB_PK() {}
	
	public FLCONTROLTAB_PK(String bIZDATE, BigDecimal pROCSEQ, String bATCHSEQ) {
		super();
		BIZDATE = bIZDATE;
		PROCSEQ = pROCSEQ;
		BATCHSEQ = bATCHSEQ;
	}

	public String getBIZDATE() {
		return BIZDATE;
	}
	public void setBIZDATE(String bIZDATE) {
		BIZDATE = bIZDATE;
	}
	public BigDecimal getPROCSEQ() {
		return PROCSEQ;
	}
	public void setPROCSEQ(BigDecimal pROCSEQ) {
		PROCSEQ = pROCSEQ;
	}
	public String getBATCHSEQ() {
		return BATCHSEQ;
	}
	public void setBATCHSEQ(String bATCHSEQ) {
		BATCHSEQ = bATCHSEQ;
	}
}
