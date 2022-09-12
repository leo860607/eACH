package tw.org.twntch.po;

import java.io.Serializable;
public class BRBK_FEE_ADJ_PK implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3002110062837650251L;
	private String YYYYMM;
	private String BRBK_ID;
	public BRBK_FEE_ADJ_PK() {
		super();
	}
	public BRBK_FEE_ADJ_PK(String yYYYMM, String bRBK_ID) {
		super();
		YYYYMM = yYYYMM;
		BRBK_ID = bRBK_ID;
	}
	public String getYYYYMM() {
		return YYYYMM;
	}
	public void setYYYYMM(String yYYYMM) {
		YYYYMM = yYYYMM;
	}
	public String getBRBK_ID() {
		return BRBK_ID;
	}
	public void setBRBK_ID(String bRBK_ID) {
		BRBK_ID = bRBK_ID;
	}
}
