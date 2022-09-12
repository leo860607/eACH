package tw.org.twntch.po;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity(name = "tw.org.twntch.po.ONPENDINGTAB")
@Table(name = "ONPENDINGTAB")
public class ONPENDINGTAB implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7521516122770873553L;
	private ONPENDINGTAB_PK id;
	private String PCODE;
	private String CLEARINGCODE;
	private String OBIZDATE;
	private String OCLEARINGPHASE;
	private String BIZDATE;
	private String CLEARINGPHASE;
	private String RESULTCODE;
	private String SENDERBANK;
	private String SENDERCLEARING;
	private String INCLEARING;
	private String OUTCLEARING;
	private String SENDERACQUIRE;
	private String INACQUIRE;
	private String OUTACQUIRE;
	private String SENDERHEAD;
	private String INHEAD;
	private String OUTHEAD;
	private BigDecimal SENDERFEE;
	private BigDecimal INFEE;
	private BigDecimal OUTFEE;
	private BigDecimal EACHFEE;
	private String TXAMT;
	private String SENDERBANKID;
	private String OUTBANKID;
	private String INBANKID;
	private String OUTACCT;
	private String INACCT;
	private String ACHFLAG;

	@EmbeddedId
	public ONPENDINGTAB_PK getId() {
		return id;
	}

	public void setId(ONPENDINGTAB_PK id) {
		this.id = id;
	}

	public String getPCODE() {
		return PCODE;
	}

	public String getCLEARINGCODE() {
		return CLEARINGCODE;
	}

	public String getOBIZDATE() {
		return OBIZDATE;
	}

	public String getOCLEARINGPHASE() {
		return OCLEARINGPHASE;
	}

	public String getBIZDATE() {
		return BIZDATE;
	}

	public String getCLEARINGPHASE() {
		return CLEARINGPHASE;
	}

	public String getRESULTCODE() {
		return RESULTCODE;
	}

	public String getSENDERBANK() {
		return SENDERBANK;
	}

	public String getSENDERCLEARING() {
		return SENDERCLEARING;
	}

	public String getINCLEARING() {
		return INCLEARING;
	}

	public String getOUTCLEARING() {
		return OUTCLEARING;
	}

	public String getSENDERACQUIRE() {
		return SENDERACQUIRE;
	}

	public String getINACQUIRE() {
		return INACQUIRE;
	}

	public String getOUTACQUIRE() {
		return OUTACQUIRE;
	}

	public String getSENDERHEAD() {
		return SENDERHEAD;
	}

	public String getINHEAD() {
		return INHEAD;
	}

	public String getOUTHEAD() {
		return OUTHEAD;
	}

	public BigDecimal getSENDERFEE() {
		return SENDERFEE;
	}

	public BigDecimal getINFEE() {
		return INFEE;
	}

	public BigDecimal getOUTFEE() {
		return OUTFEE;
	}

	public BigDecimal getEACHFEE() {
		return EACHFEE;
	}

	public String getTXAMT() {
		return TXAMT;
	}

	public String getSENDERBANKID() {
		return SENDERBANKID;
	}

	public String getOUTBANKID() {
		return OUTBANKID;
	}

	public String getINBANKID() {
		return INBANKID;
	}

	public String getOUTACCT() {
		return OUTACCT;
	}

	public String getINACCT() {
		return INACCT;
	}

	public void setPCODE(String pCODE) {
		PCODE = pCODE;
	}

	public void setCLEARINGCODE(String cLEARINGCODE) {
		CLEARINGCODE = cLEARINGCODE;
	}

	public void setOBIZDATE(String oBIZDATE) {
		OBIZDATE = oBIZDATE;
	}

	public void setOCLEARINGPHASE(String oCLEARINGPHASE) {
		OCLEARINGPHASE = oCLEARINGPHASE;
	}

	public void setBIZDATE(String bIZDATE) {
		BIZDATE = bIZDATE;
	}

	public void setCLEARINGPHASE(String cLEARINGPHASE) {
		CLEARINGPHASE = cLEARINGPHASE;
	}

	public void setRESULTCODE(String rESULTCODE) {
		RESULTCODE = rESULTCODE;
	}

	public void setSENDERBANK(String sENDERBANK) {
		SENDERBANK = sENDERBANK;
	}

	public void setSENDERCLEARING(String sENDERCLEARING) {
		SENDERCLEARING = sENDERCLEARING;
	}

	public void setINCLEARING(String iNCLEARING) {
		INCLEARING = iNCLEARING;
	}

	public void setOUTCLEARING(String oUTCLEARING) {
		OUTCLEARING = oUTCLEARING;
	}

	public void setSENDERACQUIRE(String sENDERACQUIRE) {
		SENDERACQUIRE = sENDERACQUIRE;
	}

	public void setINACQUIRE(String iNACQUIRE) {
		INACQUIRE = iNACQUIRE;
	}

	public void setOUTACQUIRE(String oUTACQUIRE) {
		OUTACQUIRE = oUTACQUIRE;
	}

	public void setSENDERHEAD(String sENDERHEAD) {
		SENDERHEAD = sENDERHEAD;
	}

	public void setINHEAD(String iNHEAD) {
		INHEAD = iNHEAD;
	}

	public void setOUTHEAD(String oUTHEAD) {
		OUTHEAD = oUTHEAD;
	}

	public void setSENDERFEE(BigDecimal sENDERFEE) {
		SENDERFEE = sENDERFEE;
	}

	public void setINFEE(BigDecimal iNFEE) {
		INFEE = iNFEE;
	}

	public void setOUTFEE(BigDecimal oUTFEE) {
		OUTFEE = oUTFEE;
	}

	public void setEACHFEE(BigDecimal eACHFEE) {
		EACHFEE = eACHFEE;
	}

	public void setTXAMT(String tXAMT) {
		TXAMT = tXAMT;
	}

	public void setSENDERBANKID(String sENDERBANKID) {
		SENDERBANKID = sENDERBANKID;
	}

	public void setOUTBANKID(String oUTBANKID) {
		OUTBANKID = oUTBANKID;
	}

	public void setINBANKID(String iNBANKID) {
		INBANKID = iNBANKID;
	}

	public void setOUTACCT(String oUTACCT) {
		OUTACCT = oUTACCT;
	}

	public void setINACCT(String iNACCT) {
		INACCT = iNACCT;
	}
	
	public String getACHFLAG() {
		return ACHFLAG;
	}

	public void setACHFLAG(String aCHFLAG) {
		ACHFLAG = aCHFLAG;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		ONPENDINGTAB other = (ONPENDINGTAB) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
