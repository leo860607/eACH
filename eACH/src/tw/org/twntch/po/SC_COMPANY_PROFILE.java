package tw.org.twntch.po;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
@Table(name="SC_COMPANY_PROFILE")
@Entity(name="tw.org.twntch.po.SC_COMPANY_PROFILE")
public class SC_COMPANY_PROFILE implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1626680474300711954L;
	
	private SC_COMPANY_PROFILE_PK id ;
	private String 	COMPANY_ID ;
	private String 	TXN_ID ;
	private String 	SND_BRBK_ID ;
	
	private	String	COMPANY_ABBR_NAME	;
	private	String	COMPANY_NAME	;
	private	String	IPO_COMPANY_ID	;
	private	String	PROFIT_ISSUE_DATE	;
	private	String	CDATE	;
	private	String	UDATE	;
	private String	BRBK_NAME;
	private String	SYS_CDATE;
	private String	IS_SHORT;
	private String	FEE_TYPE;
	private String  FEE_TYPE_ACTIVE_DATE;
	@Transient
	private String FEE_TYPE_CHT;
	
	@EmbeddedId
	public SC_COMPANY_PROFILE_PK getId() {
		return id;
	}
	public void setId(SC_COMPANY_PROFILE_PK id) {
		this.id = id;
	}
	public String getCOMPANY_ABBR_NAME() {
		return COMPANY_ABBR_NAME;
	}
	public void setCOMPANY_ABBR_NAME(String cOMPANY_ABBR_NAME) {
		COMPANY_ABBR_NAME = cOMPANY_ABBR_NAME;
	}
	public String getCOMPANY_NAME() {
		return COMPANY_NAME;
	}
	public void setCOMPANY_NAME(String cOMPANY_NAME) {
		COMPANY_NAME = cOMPANY_NAME;
	}
	public String getIPO_COMPANY_ID() {
		return IPO_COMPANY_ID;
	}
	public void setIPO_COMPANY_ID(String iPO_COMPANY_ID) {
		IPO_COMPANY_ID = iPO_COMPANY_ID;
	}
	public String getPROFIT_ISSUE_DATE() {
		return PROFIT_ISSUE_DATE;
	}
	public void setPROFIT_ISSUE_DATE(String pROFIT_ISSUE_DATE) {
		PROFIT_ISSUE_DATE = pROFIT_ISSUE_DATE;
	}
	public String getCDATE() {
		return CDATE;
	}
	public void setCDATE(String cDATE) {
		CDATE = cDATE;
	}
	public String getUDATE() {
		return UDATE;
	}
	public void setUDATE(String uDATE) {
		UDATE = uDATE;
	}
	public String getSYS_CDATE() {
		return SYS_CDATE;
	}
	public void setSYS_CDATE(String sYS_CDATE) {
		SYS_CDATE = sYS_CDATE;
	}
	public String getIS_SHORT() {
		return IS_SHORT;
	}
	public void setIS_SHORT(String iS_SHORT) {
		IS_SHORT = iS_SHORT;
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
	public String getFEE_TYPE_ACTIVE_DATE() {
		return FEE_TYPE_ACTIVE_DATE;
	}
	public void setFEE_TYPE_ACTIVE_DATE(String fEE_TYPE_ACTIVE_DATE) {
		FEE_TYPE_ACTIVE_DATE = fEE_TYPE_ACTIVE_DATE;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((BRBK_NAME == null) ? 0 : BRBK_NAME.hashCode());
		result = prime * result + ((CDATE == null) ? 0 : CDATE.hashCode());
		result = prime * result + ((COMPANY_ABBR_NAME == null) ? 0 : COMPANY_ABBR_NAME.hashCode());
		result = prime * result + ((COMPANY_ID == null) ? 0 : COMPANY_ID.hashCode());
		result = prime * result + ((COMPANY_NAME == null) ? 0 : COMPANY_NAME.hashCode());
		result = prime * result + ((FEE_TYPE == null) ? 0 : FEE_TYPE.hashCode());
		result = prime * result + ((FEE_TYPE_ACTIVE_DATE == null) ? 0 : FEE_TYPE_ACTIVE_DATE.hashCode());
		result = prime * result + ((FEE_TYPE_CHT == null) ? 0 : FEE_TYPE_CHT.hashCode());
		result = prime * result + ((IPO_COMPANY_ID == null) ? 0 : IPO_COMPANY_ID.hashCode());
		result = prime * result + ((IS_SHORT == null) ? 0 : IS_SHORT.hashCode());
		result = prime * result + ((PROFIT_ISSUE_DATE == null) ? 0 : PROFIT_ISSUE_DATE.hashCode());
		result = prime * result + ((SND_BRBK_ID == null) ? 0 : SND_BRBK_ID.hashCode());
		result = prime * result + ((SYS_CDATE == null) ? 0 : SYS_CDATE.hashCode());
		result = prime * result + ((TXN_ID == null) ? 0 : TXN_ID.hashCode());
		result = prime * result + ((UDATE == null) ? 0 : UDATE.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		SC_COMPANY_PROFILE other = (SC_COMPANY_PROFILE) obj;
		if (BRBK_NAME == null) {
			if (other.BRBK_NAME != null)
				return false;
		} else if (!BRBK_NAME.equals(other.BRBK_NAME))
			return false;
		if (CDATE == null) {
			if (other.CDATE != null)
				return false;
		} else if (!CDATE.equals(other.CDATE))
			return false;
		if (COMPANY_ABBR_NAME == null) {
			if (other.COMPANY_ABBR_NAME != null)
				return false;
		} else if (!COMPANY_ABBR_NAME.equals(other.COMPANY_ABBR_NAME))
			return false;
		if (COMPANY_ID == null) {
			if (other.COMPANY_ID != null)
				return false;
		} else if (!COMPANY_ID.equals(other.COMPANY_ID))
			return false;
		if (COMPANY_NAME == null) {
			if (other.COMPANY_NAME != null)
				return false;
		} else if (!COMPANY_NAME.equals(other.COMPANY_NAME))
			return false;
		if (FEE_TYPE == null) {
			if (other.FEE_TYPE != null)
				return false;
		} else if (!FEE_TYPE.equals(other.FEE_TYPE))
			return false;
		if (FEE_TYPE_ACTIVE_DATE == null) {
			if (other.FEE_TYPE_ACTIVE_DATE != null)
				return false;
		} else if (!FEE_TYPE_ACTIVE_DATE.equals(other.FEE_TYPE_ACTIVE_DATE))
			return false;
		if (FEE_TYPE_CHT == null) {
			if (other.FEE_TYPE_CHT != null)
				return false;
		} else if (!FEE_TYPE_CHT.equals(other.FEE_TYPE_CHT))
			return false;
		if (IPO_COMPANY_ID == null) {
			if (other.IPO_COMPANY_ID != null)
				return false;
		} else if (!IPO_COMPANY_ID.equals(other.IPO_COMPANY_ID))
			return false;
		if (IS_SHORT == null) {
			if (other.IS_SHORT != null)
				return false;
		} else if (!IS_SHORT.equals(other.IS_SHORT))
			return false;
		if (PROFIT_ISSUE_DATE == null) {
			if (other.PROFIT_ISSUE_DATE != null)
				return false;
		} else if (!PROFIT_ISSUE_DATE.equals(other.PROFIT_ISSUE_DATE))
			return false;
		if (SND_BRBK_ID == null) {
			if (other.SND_BRBK_ID != null)
				return false;
		} else if (!SND_BRBK_ID.equals(other.SND_BRBK_ID))
			return false;
		if (SYS_CDATE == null) {
			if (other.SYS_CDATE != null)
				return false;
		} else if (!SYS_CDATE.equals(other.SYS_CDATE))
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
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "SC_COMPANY_PROFILE [id=" + id + ", COMPANY_ID=" + COMPANY_ID + ", TXN_ID=" + TXN_ID + ", SND_BRBK_ID="
				+ SND_BRBK_ID + ", COMPANY_ABBR_NAME=" + COMPANY_ABBR_NAME + ", COMPANY_NAME=" + COMPANY_NAME
				+ ", IPO_COMPANY_ID=" + IPO_COMPANY_ID + ", PROFIT_ISSUE_DATE=" + PROFIT_ISSUE_DATE + ", CDATE=" + CDATE
				+ ", UDATE=" + UDATE + ", BRBK_NAME=" + BRBK_NAME + ", SYS_CDATE=" + SYS_CDATE + ", IS_SHORT="
				+ IS_SHORT + ", FEE_TYPE=" + FEE_TYPE + ", FEE_TYPE_ACTIVE_DATE="
				+ FEE_TYPE_ACTIVE_DATE + ", FEE_TYPE_CHT=" + FEE_TYPE_CHT + "]";
	}
	
}
