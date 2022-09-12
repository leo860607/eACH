package tw.org.twntch.po;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity(name = "tw.org.twntch.po.FEE_CODE_NW")
@Table(name = "FEE_CODE_NW")
public class FEE_CODE_NW implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4645731359874628929L;
	
//	private String FEE_UID;
	//維持舊有架構
	private FEE_CODE_NW_PK id;
	@Transient
	private String FEE_UID;
	
	private String FEE_ID;
	private String FEE_TYPE;
	private String START_DATE;
	private String OUT_BANK_FEE;
	private String OUT_BANK_FEE_DISC;
	private String IN_BANK_FEE;
	private String IN_BANK_FEE_DISC;
	private String TCH_FEE;
	private String TCH_FEE_DISC;
	private String SND_BANK_FEE;
	private String SND_BANK_FEE_DISC;
	private String HANDLECHARGE;
	private String WO_BANK_FEE;
	private String WO_BANK_FEE_DISC;
	private String CDATE;
	private String UDATE;
	private String FEE_DESC;
	private String ACTIVE_DESC;
	
	@Transient
	private String TXN_NAME;
	
	@Transient
	private String FEE_TYPE_CHT;
	
	//附表欄位 為了頁面顯示用
	@Transient
	private String FEE_LVL_TYPE;
	@Transient
	private String FEE_LVL_TYPE_CHT;
	@Transient
	private String FEE_DTNO;
	@Transient
	private String FEE_LVL_BEG_AMT;
	@Transient
	private String FEE_LVL_END_AMT;
	
	@EmbeddedId
	public FEE_CODE_NW_PK getId() {
		return id;
	}

	public void setId(FEE_CODE_NW_PK id) {
		this.id = id;
	}
	@Transient
	public String getFEE_UID() {
		return FEE_UID;
	}

	public void setFEE_UID(String fEE_UID) {
		FEE_UID = fEE_UID;
	}

	public String getFEE_ID() {
		return FEE_ID;
	}

	public void setFEE_ID(String fEE_ID) {
		FEE_ID = fEE_ID;
	}
	
	public String getFEE_TYPE() {
		return FEE_TYPE;
	}

	public void setFEE_TYPE(String fEE_TYPE) {
		FEE_TYPE = fEE_TYPE;
	}

	public String getSTART_DATE() {
		return START_DATE;
	}

	public void setSTART_DATE(String sTART_DATE) {
		START_DATE = sTART_DATE;
	}

	public String getOUT_BANK_FEE() {
		return OUT_BANK_FEE;
	}

	public void setOUT_BANK_FEE(String oUT_BANK_FEE) {
		OUT_BANK_FEE = oUT_BANK_FEE;
	}

	public String getOUT_BANK_FEE_DISC() {
		return OUT_BANK_FEE_DISC;
	}

	public void setOUT_BANK_FEE_DISC(String oUT_BANK_FEE_DISC) {
		OUT_BANK_FEE_DISC = oUT_BANK_FEE_DISC;
	}

	public String getIN_BANK_FEE() {
		return IN_BANK_FEE;
	}

	public void setIN_BANK_FEE(String iN_BANK_FEE) {
		IN_BANK_FEE = iN_BANK_FEE;
	}

	public String getIN_BANK_FEE_DISC() {
		return IN_BANK_FEE_DISC;
	}

	public void setIN_BANK_FEE_DISC(String iN_BANK_FEE_DISC) {
		IN_BANK_FEE_DISC = iN_BANK_FEE_DISC;
	}

	public String getTCH_FEE() {
		return TCH_FEE;
	}

	public void setTCH_FEE(String tCH_FEE) {
		TCH_FEE = tCH_FEE;
	}

	public String getTCH_FEE_DISC() {
		return TCH_FEE_DISC;
	}

	public void setTCH_FEE_DISC(String tCH_FEE_DISC) {
		TCH_FEE_DISC = tCH_FEE_DISC;
	}

	public String getSND_BANK_FEE() {
		return SND_BANK_FEE;
	}

	public void setSND_BANK_FEE(String sND_BANK_FEE) {
		SND_BANK_FEE = sND_BANK_FEE;
	}

	public String getSND_BANK_FEE_DISC() {
		return SND_BANK_FEE_DISC;
	}

	public void setSND_BANK_FEE_DISC(String sND_BANK_FEE_DISC) {
		SND_BANK_FEE_DISC = sND_BANK_FEE_DISC;
	}

	public String getHANDLECHARGE() {
		return HANDLECHARGE;
	}

	public void setHANDLECHARGE(String hANDLECHARGE) {
		HANDLECHARGE = hANDLECHARGE;
	}

	public String getWO_BANK_FEE() {
		return WO_BANK_FEE;
	}

	public void setWO_BANK_FEE(String wO_BANK_FEE) {
		WO_BANK_FEE = wO_BANK_FEE;
	}

	public String getWO_BANK_FEE_DISC() {
		return WO_BANK_FEE_DISC;
	}

	public void setWO_BANK_FEE_DISC(String wO_BANK_FEE_DISC) {
		WO_BANK_FEE_DISC = wO_BANK_FEE_DISC;
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

	@Transient
	public String getTXN_NAME() {
		return TXN_NAME;
	}

	public void setTXN_NAME(String tXN_NAME) {
		TXN_NAME = tXN_NAME;
	}
	
	@Transient
	public String getFEE_LVL_TYPE() {
		return FEE_LVL_TYPE;
	}

	public void setFEE_LVL_TYPE(String fEE_LVL_TYPE) {
		FEE_LVL_TYPE = fEE_LVL_TYPE;
	}
	@Transient
	public String getFEE_DTNO() {
		return FEE_DTNO;
	}

	public void setFEE_DTNO(String fEE_DTNO) {
		FEE_DTNO = fEE_DTNO;
	}
	@Transient
	public String getFEE_LVL_BEG_AMT() {
		return FEE_LVL_BEG_AMT;
	}

	public void setFEE_LVL_BEG_AMT(String fEE_LVL_BEG_AMT) {
		FEE_LVL_BEG_AMT = fEE_LVL_BEG_AMT;
	}
	@Transient
	public String getFEE_LVL_END_AMT() {
		return FEE_LVL_END_AMT;
	}

	public void setFEE_LVL_END_AMT(String fEE_LVL_END_AMT) {
		FEE_LVL_END_AMT = fEE_LVL_END_AMT;
	}
	@Transient
	public String getFEE_TYPE_CHT() {
		return FEE_TYPE_CHT;
	}

	public void setFEE_TYPE_CHT(String fEE_TYPE_CHT) {
		FEE_TYPE_CHT = fEE_TYPE_CHT;
	}
	@Transient
	public String getFEE_LVL_TYPE_CHT() {
		return FEE_LVL_TYPE_CHT;
	}

	public void setFEE_LVL_TYPE_CHT(String fEE_LVL_TYPE_CHT) {
		FEE_LVL_TYPE_CHT = fEE_LVL_TYPE_CHT;
	}
	
	public String getFEE_DESC() {
		return FEE_DESC;
	}

	public void setFEE_DESC(String fEE_DESC) {
		FEE_DESC = fEE_DESC;
	}

	public String getACTIVE_DESC() {
		return ACTIVE_DESC;
	}

	public void setACTIVE_DESC(String aCTIVE_DESC) {
		ACTIVE_DESC = aCTIVE_DESC;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ACTIVE_DESC == null) ? 0 : ACTIVE_DESC.hashCode());
		result = prime * result + ((CDATE == null) ? 0 : CDATE.hashCode());
		result = prime * result + ((FEE_DESC == null) ? 0 : FEE_DESC.hashCode());
		result = prime * result + ((FEE_DTNO == null) ? 0 : FEE_DTNO.hashCode());
		result = prime * result + ((FEE_ID == null) ? 0 : FEE_ID.hashCode());
		result = prime * result + ((FEE_LVL_BEG_AMT == null) ? 0 : FEE_LVL_BEG_AMT.hashCode());
		result = prime * result + ((FEE_LVL_END_AMT == null) ? 0 : FEE_LVL_END_AMT.hashCode());
		result = prime * result + ((FEE_LVL_TYPE == null) ? 0 : FEE_LVL_TYPE.hashCode());
		result = prime * result + ((FEE_LVL_TYPE_CHT == null) ? 0 : FEE_LVL_TYPE_CHT.hashCode());
		result = prime * result + ((FEE_TYPE == null) ? 0 : FEE_TYPE.hashCode());
		result = prime * result + ((FEE_TYPE_CHT == null) ? 0 : FEE_TYPE_CHT.hashCode());
		result = prime * result + ((FEE_UID == null) ? 0 : FEE_UID.hashCode());
		result = prime * result + ((HANDLECHARGE == null) ? 0 : HANDLECHARGE.hashCode());
		result = prime * result + ((IN_BANK_FEE == null) ? 0 : IN_BANK_FEE.hashCode());
		result = prime * result + ((IN_BANK_FEE_DISC == null) ? 0 : IN_BANK_FEE_DISC.hashCode());
		result = prime * result + ((OUT_BANK_FEE == null) ? 0 : OUT_BANK_FEE.hashCode());
		result = prime * result + ((OUT_BANK_FEE_DISC == null) ? 0 : OUT_BANK_FEE_DISC.hashCode());
		result = prime * result + ((SND_BANK_FEE == null) ? 0 : SND_BANK_FEE.hashCode());
		result = prime * result + ((SND_BANK_FEE_DISC == null) ? 0 : SND_BANK_FEE_DISC.hashCode());
		result = prime * result + ((START_DATE == null) ? 0 : START_DATE.hashCode());
		result = prime * result + ((TCH_FEE == null) ? 0 : TCH_FEE.hashCode());
		result = prime * result + ((TCH_FEE_DISC == null) ? 0 : TCH_FEE_DISC.hashCode());
		result = prime * result + ((TXN_NAME == null) ? 0 : TXN_NAME.hashCode());
		result = prime * result + ((UDATE == null) ? 0 : UDATE.hashCode());
		result = prime * result + ((WO_BANK_FEE == null) ? 0 : WO_BANK_FEE.hashCode());
		result = prime * result + ((WO_BANK_FEE_DISC == null) ? 0 : WO_BANK_FEE_DISC.hashCode());
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
		FEE_CODE_NW other = (FEE_CODE_NW) obj;
		if (ACTIVE_DESC == null) {
			if (other.ACTIVE_DESC != null)
				return false;
		} else if (!ACTIVE_DESC.equals(other.ACTIVE_DESC))
			return false;
		if (CDATE == null) {
			if (other.CDATE != null)
				return false;
		} else if (!CDATE.equals(other.CDATE))
			return false;
		if (FEE_DESC == null) {
			if (other.FEE_DESC != null)
				return false;
		} else if (!FEE_DESC.equals(other.FEE_DESC))
			return false;
		if (FEE_DTNO == null) {
			if (other.FEE_DTNO != null)
				return false;
		} else if (!FEE_DTNO.equals(other.FEE_DTNO))
			return false;
		if (FEE_ID == null) {
			if (other.FEE_ID != null)
				return false;
		} else if (!FEE_ID.equals(other.FEE_ID))
			return false;
		if (FEE_LVL_BEG_AMT == null) {
			if (other.FEE_LVL_BEG_AMT != null)
				return false;
		} else if (!FEE_LVL_BEG_AMT.equals(other.FEE_LVL_BEG_AMT))
			return false;
		if (FEE_LVL_END_AMT == null) {
			if (other.FEE_LVL_END_AMT != null)
				return false;
		} else if (!FEE_LVL_END_AMT.equals(other.FEE_LVL_END_AMT))
			return false;
		if (FEE_LVL_TYPE == null) {
			if (other.FEE_LVL_TYPE != null)
				return false;
		} else if (!FEE_LVL_TYPE.equals(other.FEE_LVL_TYPE))
			return false;
		if (FEE_LVL_TYPE_CHT == null) {
			if (other.FEE_LVL_TYPE_CHT != null)
				return false;
		} else if (!FEE_LVL_TYPE_CHT.equals(other.FEE_LVL_TYPE_CHT))
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
		if (FEE_UID == null) {
			if (other.FEE_UID != null)
				return false;
		} else if (!FEE_UID.equals(other.FEE_UID))
			return false;
		if (HANDLECHARGE == null) {
			if (other.HANDLECHARGE != null)
				return false;
		} else if (!HANDLECHARGE.equals(other.HANDLECHARGE))
			return false;
		if (IN_BANK_FEE == null) {
			if (other.IN_BANK_FEE != null)
				return false;
		} else if (!IN_BANK_FEE.equals(other.IN_BANK_FEE))
			return false;
		if (IN_BANK_FEE_DISC == null) {
			if (other.IN_BANK_FEE_DISC != null)
				return false;
		} else if (!IN_BANK_FEE_DISC.equals(other.IN_BANK_FEE_DISC))
			return false;
		if (OUT_BANK_FEE == null) {
			if (other.OUT_BANK_FEE != null)
				return false;
		} else if (!OUT_BANK_FEE.equals(other.OUT_BANK_FEE))
			return false;
		if (OUT_BANK_FEE_DISC == null) {
			if (other.OUT_BANK_FEE_DISC != null)
				return false;
		} else if (!OUT_BANK_FEE_DISC.equals(other.OUT_BANK_FEE_DISC))
			return false;
		if (SND_BANK_FEE == null) {
			if (other.SND_BANK_FEE != null)
				return false;
		} else if (!SND_BANK_FEE.equals(other.SND_BANK_FEE))
			return false;
		if (SND_BANK_FEE_DISC == null) {
			if (other.SND_BANK_FEE_DISC != null)
				return false;
		} else if (!SND_BANK_FEE_DISC.equals(other.SND_BANK_FEE_DISC))
			return false;
		if (START_DATE == null) {
			if (other.START_DATE != null)
				return false;
		} else if (!START_DATE.equals(other.START_DATE))
			return false;
		if (TCH_FEE == null) {
			if (other.TCH_FEE != null)
				return false;
		} else if (!TCH_FEE.equals(other.TCH_FEE))
			return false;
		if (TCH_FEE_DISC == null) {
			if (other.TCH_FEE_DISC != null)
				return false;
		} else if (!TCH_FEE_DISC.equals(other.TCH_FEE_DISC))
			return false;
		if (TXN_NAME == null) {
			if (other.TXN_NAME != null)
				return false;
		} else if (!TXN_NAME.equals(other.TXN_NAME))
			return false;
		if (UDATE == null) {
			if (other.UDATE != null)
				return false;
		} else if (!UDATE.equals(other.UDATE))
			return false;
		if (WO_BANK_FEE == null) {
			if (other.WO_BANK_FEE != null)
				return false;
		} else if (!WO_BANK_FEE.equals(other.WO_BANK_FEE))
			return false;
		if (WO_BANK_FEE_DISC == null) {
			if (other.WO_BANK_FEE_DISC != null)
				return false;
		} else if (!WO_BANK_FEE_DISC.equals(other.WO_BANK_FEE_DISC))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
