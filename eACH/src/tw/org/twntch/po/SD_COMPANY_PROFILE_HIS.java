package tw.org.twntch.po;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
@Entity(name = "tw.org.twntch.po.SD_COMPANY_PROFILE_HIS")
@Table(name = "SD_COMPANY_PROFILE_HIS")
public class SD_COMPANY_PROFILE_HIS implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3287643084091209886L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)	
	private Integer SEQ_ID;
	private String 	COMPANY_ID ;
	private String 	TXN_ID ;
	private String 	SND_BRBK_ID ;
	private	String	UDATE	;
	private String	ACTIVE_DATE;
	private String	FEE_TYPE;
	
	
	
	@Transient
	private String	BRBK_NAME;
	@Transient
	private String FEE_TYPE_CHT;
	
	public Integer getSEQ_ID() {
		return SEQ_ID;
	}
	public void setSEQ_ID(Integer sEQ_ID) {
		SEQ_ID = sEQ_ID;
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
	public String getUDATE() {
		return UDATE;
	}
	public void setUDATE(String uDATE) {
		UDATE = uDATE;
	}
	public String getACTIVE_DATE() {
		return ACTIVE_DATE;
	}
	public void setACTIVE_DATE(String aCTIVE_DATE) {
		ACTIVE_DATE = aCTIVE_DATE;
	}
	public String getFEE_TYPE() {
		return FEE_TYPE;
	}
	public void setFEE_TYPE(String fEE_TYPE) {
		FEE_TYPE = fEE_TYPE;
	}
	@Transient
	public String getBRBK_NAME() {
		return BRBK_NAME;
	}
	public void setBRBK_NAME(String bRBK_NAME) {
		BRBK_NAME = bRBK_NAME;
	}
	@Transient
	public String getFEE_TYPE_CHT() {
		return FEE_TYPE_CHT;
	}
	public void setFEE_TYPE_CHT(String fEE_TYPE_CHT) {
		FEE_TYPE_CHT = fEE_TYPE_CHT;
	}
	
}
