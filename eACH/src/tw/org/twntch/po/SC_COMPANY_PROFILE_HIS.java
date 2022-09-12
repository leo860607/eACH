package tw.org.twntch.po;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
@Entity(name = "tw.org.twntch.po.SC_COMPANY_PROFILE_HIS")
@Table(name = "SC_COMPANY_PROFILE_HIS")
public class SC_COMPANY_PROFILE_HIS implements Serializable {
	
	
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
	public String getFEE_TYPE() {
		return FEE_TYPE;
	}
	public void setFEE_TYPE(String fEE_TYPE) {
		FEE_TYPE = fEE_TYPE;
	}
	@Transient
	public String getCOMPANY_ID() {
		return COMPANY_ID;
	}
	public void setCOMPANY_ID(String cOMPANY_ID) {
		COMPANY_ID = cOMPANY_ID;
	}
	@Transient
	public String getTXN_ID() {
		return TXN_ID;
	}
	public void setTXN_ID(String tXN_ID) {
		TXN_ID = tXN_ID;
	}
	@Transient
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
	@Transient
	public String getFEE_TYPE_CHT() {
		return FEE_TYPE_CHT;
	}
	public void setFEE_TYPE_CHT(String fEE_TYPE_CHT) {
		FEE_TYPE_CHT = fEE_TYPE_CHT;
	}
	@Transient
	public String getBRBK_NAME() {
		return BRBK_NAME;
	}
	public void setBRBK_NAME(String bRBK_NAME) {
		BRBK_NAME = bRBK_NAME;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ACTIVE_DATE == null) ? 0 : ACTIVE_DATE.hashCode());
		result = prime * result + ((BRBK_NAME == null) ? 0 : BRBK_NAME.hashCode());
		result = prime * result + ((COMPANY_ID == null) ? 0 : COMPANY_ID.hashCode());
		result = prime * result + ((FEE_TYPE == null) ? 0 : FEE_TYPE.hashCode());
		result = prime * result + ((FEE_TYPE_CHT == null) ? 0 : FEE_TYPE_CHT.hashCode());
		result = prime * result + ((SEQ_ID == null) ? 0 : SEQ_ID.hashCode());
		result = prime * result + ((SND_BRBK_ID == null) ? 0 : SND_BRBK_ID.hashCode());
		result = prime * result + ((TXN_ID == null) ? 0 : TXN_ID.hashCode());
		result = prime * result + ((UDATE == null) ? 0 : UDATE.hashCode());
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
		SC_COMPANY_PROFILE_HIS other = (SC_COMPANY_PROFILE_HIS) obj;
		if (ACTIVE_DATE == null) {
			if (other.ACTIVE_DATE != null)
				return false;
		} else if (!ACTIVE_DATE.equals(other.ACTIVE_DATE))
			return false;
		if (BRBK_NAME == null) {
			if (other.BRBK_NAME != null)
				return false;
		} else if (!BRBK_NAME.equals(other.BRBK_NAME))
			return false;
		if (COMPANY_ID == null) {
			if (other.COMPANY_ID != null)
				return false;
		} else if (!COMPANY_ID.equals(other.COMPANY_ID))
			return false;
		if (FEE_TYPE == null) {
			if (other.FEE_TYPE != null)
				return false;
		} else if (!FEE_TYPE.equals(other.FEE_TYPE))
			return false;
		if (FEE_TYPE_CHT == null) {
			if (other.FEE_TYPE_CHT != null)
				return false;
		} else if (!FEE_TYPE_CHT.equals(other.FEE_TYPE_CHT))
			return false;
		if (SEQ_ID == null) {
			if (other.SEQ_ID != null)
				return false;
		} else if (!SEQ_ID.equals(other.SEQ_ID))
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
		if (UDATE == null) {
			if (other.UDATE != null)
				return false;
		} else if (!UDATE.equals(other.UDATE))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "SC_COMPANY_PROFILE_HIS [SEQ_ID=" + SEQ_ID + ", COMPANY_ID=" + COMPANY_ID + ", TXN_ID=" + TXN_ID
				+ ", SND_BRBK_ID=" + SND_BRBK_ID + ", UDATE=" + UDATE + ", ACTIVE_DATE=" + ACTIVE_DATE + ", FEE_TYPE="
				+ FEE_TYPE + ", BRBK_NAME=" + BRBK_NAME + ", FEE_TYPE_CHT=" + FEE_TYPE_CHT + "]";
	}
}
