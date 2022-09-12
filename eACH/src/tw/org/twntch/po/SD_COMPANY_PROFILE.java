package tw.org.twntch.po;

import java.io.Serializable;






import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
@Entity(name = "tw.org.twntch.po.SD_COMPANY_PROFILE")
@Table(name = "SD_COMPANY_PROFILE")
public class SD_COMPANY_PROFILE implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3287643084091209886L;
	private SD_COMPANY_PROFILE_PK id ;
	private String 	COMPANY_ID ;
	private String 	TXN_ID ;
	private String 	SND_BRBK_ID ;
	private	String	COMPANY_ABBR_NAME	;
	private	String	COMPANY_NAME	;
	private	String	CONTACT_INFO	;
	private	String	START_DATE	;
	private	String	DISPATCH_DATE	;
	private	String	USER_NO	;
	private	String	CASE_NO	;
	private	String	STOP_DATE	;
	private	String	CDATE	;
	private	String	UDATE	;
	private String	BRBK_NAME;
	private String	ACTIVE_DATE;
	private String	FEE_TYPE;
	private String	FEE_TYPE_ACTIVE_DATE;
	@Transient
	private String FEE_TYPE_CHT;
	
	public String getFEE_TYPE() {
		return FEE_TYPE;
	}
	public void setFEE_TYPE(String fEE_TYPE) {
		FEE_TYPE = fEE_TYPE;
	}
	@EmbeddedId
	public SD_COMPANY_PROFILE_PK getId() {
		return id;
	}
	public void setId(SD_COMPANY_PROFILE_PK id) {
		this.id = id;
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
	public String getCONTACT_INFO() {
		return CONTACT_INFO;
	}
	public void setCONTACT_INFO(String cONTACT_INFO) {
		CONTACT_INFO = cONTACT_INFO;
	}
	public String getSTART_DATE() {
		return START_DATE;
	}
	public void setSTART_DATE(String sTART_DATE) {
		START_DATE = sTART_DATE;
	}
	public String getDISPATCH_DATE() {
		return DISPATCH_DATE;
	}
	public void setDISPATCH_DATE(String dISPATCH_DATE) {
		DISPATCH_DATE = dISPATCH_DATE;
	}
	public String getUSER_NO() {
		return USER_NO;
	}
	public void setUSER_NO(String uSER_NO) {
		USER_NO = uSER_NO;
	}
	public String getCASE_NO() {
		return CASE_NO;
	}
	public void setCASE_NO(String cASE_NO) {
		CASE_NO = cASE_NO;
	}
	public String getSTOP_DATE() {
		return STOP_DATE;
	}
	public void setSTOP_DATE(String sTOP_DATE) {
		STOP_DATE = sTOP_DATE;
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
	
	public String getACTIVE_DATE() {
		return ACTIVE_DATE;
	}
	public void setACTIVE_DATE(String aCTIVE_DATE) {
		ACTIVE_DATE = aCTIVE_DATE;
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
		result = prime * result + ((ACTIVE_DATE == null) ? 0 : ACTIVE_DATE.hashCode());
		result = prime * result + ((BRBK_NAME == null) ? 0 : BRBK_NAME.hashCode());
		result = prime * result + ((CASE_NO == null) ? 0 : CASE_NO.hashCode());
		result = prime * result + ((CDATE == null) ? 0 : CDATE.hashCode());
		result = prime * result + ((COMPANY_ABBR_NAME == null) ? 0 : COMPANY_ABBR_NAME.hashCode());
		result = prime * result + ((COMPANY_ID == null) ? 0 : COMPANY_ID.hashCode());
		result = prime * result + ((COMPANY_NAME == null) ? 0 : COMPANY_NAME.hashCode());
		result = prime * result + ((CONTACT_INFO == null) ? 0 : CONTACT_INFO.hashCode());
		result = prime * result + ((DISPATCH_DATE == null) ? 0 : DISPATCH_DATE.hashCode());
		result = prime * result + ((FEE_TYPE == null) ? 0 : FEE_TYPE.hashCode());
		result = prime * result + ((FEE_TYPE_ACTIVE_DATE == null) ? 0 : FEE_TYPE_ACTIVE_DATE.hashCode());
		result = prime * result + ((FEE_TYPE_CHT == null) ? 0 : FEE_TYPE_CHT.hashCode());
		result = prime * result + ((SND_BRBK_ID == null) ? 0 : SND_BRBK_ID.hashCode());
		result = prime * result + ((START_DATE == null) ? 0 : START_DATE.hashCode());
		result = prime * result + ((STOP_DATE == null) ? 0 : STOP_DATE.hashCode());
		result = prime * result + ((TXN_ID == null) ? 0 : TXN_ID.hashCode());
		result = prime * result + ((UDATE == null) ? 0 : UDATE.hashCode());
		result = prime * result + ((USER_NO == null) ? 0 : USER_NO.hashCode());
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
		SD_COMPANY_PROFILE other = (SD_COMPANY_PROFILE) obj;
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
		if (CASE_NO == null) {
			if (other.CASE_NO != null)
				return false;
		} else if (!CASE_NO.equals(other.CASE_NO))
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
		if (CONTACT_INFO == null) {
			if (other.CONTACT_INFO != null)
				return false;
		} else if (!CONTACT_INFO.equals(other.CONTACT_INFO))
			return false;
		if (DISPATCH_DATE == null) {
			if (other.DISPATCH_DATE != null)
				return false;
		} else if (!DISPATCH_DATE.equals(other.DISPATCH_DATE))
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
		if (SND_BRBK_ID == null) {
			if (other.SND_BRBK_ID != null)
				return false;
		} else if (!SND_BRBK_ID.equals(other.SND_BRBK_ID))
			return false;
		if (START_DATE == null) {
			if (other.START_DATE != null)
				return false;
		} else if (!START_DATE.equals(other.START_DATE))
			return false;
		if (STOP_DATE == null) {
			if (other.STOP_DATE != null)
				return false;
		} else if (!STOP_DATE.equals(other.STOP_DATE))
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
		if (USER_NO == null) {
			if (other.USER_NO != null)
				return false;
		} else if (!USER_NO.equals(other.USER_NO))
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
		return "SD_COMPANY_PROFILE [id=" + id + ", COMPANY_ID=" + COMPANY_ID + ", TXN_ID=" + TXN_ID + ", SND_BRBK_ID="
				+ SND_BRBK_ID + ", COMPANY_ABBR_NAME=" + COMPANY_ABBR_NAME + ", COMPANY_NAME=" + COMPANY_NAME
				+ ", CONTACT_INFO=" + CONTACT_INFO + ", START_DATE=" + START_DATE + ", DISPATCH_DATE=" + DISPATCH_DATE
				+ ", USER_NO=" + USER_NO + ", CASE_NO=" + CASE_NO + ", STOP_DATE=" + STOP_DATE + ", CDATE=" + CDATE
				+ ", UDATE=" + UDATE + ", BRBK_NAME=" + BRBK_NAME + ", ACTIVE_DATE=" + ACTIVE_DATE + ", FEE_TYPE="
				+ FEE_TYPE + ", FEE_TYPE_ACTIVE_DATE=" + FEE_TYPE_ACTIVE_DATE + ", FEE_TYPE_CHT=" + FEE_TYPE_CHT + "]";
	}
	
	
}
