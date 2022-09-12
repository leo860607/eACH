package tw.org.twntch.po;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Table(name = "SETTLEMENTLOGTAB")
@Entity(name= "tw.org.twntch.po.SETTLEMENTLOGTAB")
public class SETTLEMENTLOGTAB implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4420492215138504355L;
	private SETTLEMENTLOGTAB_PK id ;
	private String ACHPAYAMT;
	private String ACHPAYCNT;
	private String ACHRECVAMT;
	private String ACHRECVCNT;
	private String ACHRVSPAYAMT;
	private String ACHRVSPAYCNT;
	private String ACHRVSRECVAMT;
	private String ACHRVSRECVCNT;
	private String BANKID;
	private String BIZDATE;
	private String BIZFLAG;
	private String BIZGROUP;
	private String CCACHPAYAMT;
	private String CCACHPAYCNT;
	private String CCACHRECVAMT;
	private String CCACHRECVCNT;
	private String CCACHRVSPAYAMT;
	private String CCACHRVSPAYCNT;
	private String CCACHRVSRECVAMT;
	private String CCACHRVSRECVCNT;
	private String CLEARINGPHASE;
	private String DTREQ;
	private String DTRSP;
	private String NETAMT;
	private String PAYAMT;
	private String PAYCNT;
	private String PAYFEEAMT;
	private String PCODE;
	private String PENDING;
	private String RECEIVERBANK;
	private String RECVAMT;
	private String RECVCNT;
	private String RECVFEEAMT;
	private String RSPCODE;
	private String RSPRESULTCODE;
	private String SENDERBANK;
	private String STATUS;
	private String TIMEOUTCODE;
	private String TOTALCNT;
	private String TXDT;
	@EmbeddedId
	public SETTLEMENTLOGTAB_PK getId() {
		return id;
	}
	public void setId(SETTLEMENTLOGTAB_PK id) {
		this.id = id;
	}
	public String getACHPAYAMT() {
		return ACHPAYAMT;
	}
	public void setACHPAYAMT(String aCHPAYAMT) {
		ACHPAYAMT = aCHPAYAMT;
	}
	public String getACHPAYCNT() {
		return ACHPAYCNT;
	}
	public void setACHPAYCNT(String aCHPAYCNT) {
		ACHPAYCNT = aCHPAYCNT;
	}
	public String getACHRECVAMT() {
		return ACHRECVAMT;
	}
	public void setACHRECVAMT(String aCHRECVAMT) {
		ACHRECVAMT = aCHRECVAMT;
	}
	public String getACHRECVCNT() {
		return ACHRECVCNT;
	}
	public void setACHRECVCNT(String aCHRECVCNT) {
		ACHRECVCNT = aCHRECVCNT;
	}
	public String getACHRVSPAYAMT() {
		return ACHRVSPAYAMT;
	}
	public void setACHRVSPAYAMT(String aCHRVSPAYAMT) {
		ACHRVSPAYAMT = aCHRVSPAYAMT;
	}
	public String getACHRVSPAYCNT() {
		return ACHRVSPAYCNT;
	}
	public void setACHRVSPAYCNT(String aCHRVSPAYCNT) {
		ACHRVSPAYCNT = aCHRVSPAYCNT;
	}
	public String getACHRVSRECVAMT() {
		return ACHRVSRECVAMT;
	}
	public void setACHRVSRECVAMT(String aCHRVSRECVAMT) {
		ACHRVSRECVAMT = aCHRVSRECVAMT;
	}
	public String getACHRVSRECVCNT() {
		return ACHRVSRECVCNT;
	}
	public void setACHRVSRECVCNT(String aCHRVSRECVCNT) {
		ACHRVSRECVCNT = aCHRVSRECVCNT;
	}
	public String getBANKID() {
		return BANKID;
	}
	public void setBANKID(String bANKID) {
		BANKID = bANKID;
	}
	public String getBIZDATE() {
		return BIZDATE;
	}
	public void setBIZDATE(String bIZDATE) {
		BIZDATE = bIZDATE;
	}
	public String getBIZFLAG() {
		return BIZFLAG;
	}
	public void setBIZFLAG(String bIZFLAG) {
		BIZFLAG = bIZFLAG;
	}
	public String getBIZGROUP() {
		return BIZGROUP;
	}
	public void setBIZGROUP(String bIZGROUP) {
		BIZGROUP = bIZGROUP;
	}
	public String getCCACHPAYAMT() {
		return CCACHPAYAMT;
	}
	public void setCCACHPAYAMT(String cCACHPAYAMT) {
		CCACHPAYAMT = cCACHPAYAMT;
	}
	public String getCCACHPAYCNT() {
		return CCACHPAYCNT;
	}
	public void setCCACHPAYCNT(String cCACHPAYCNT) {
		CCACHPAYCNT = cCACHPAYCNT;
	}
	public String getCCACHRECVAMT() {
		return CCACHRECVAMT;
	}
	public void setCCACHRECVAMT(String cCACHRECVAMT) {
		CCACHRECVAMT = cCACHRECVAMT;
	}
	public String getCCACHRECVCNT() {
		return CCACHRECVCNT;
	}
	public void setCCACHRECVCNT(String cCACHRECVCNT) {
		CCACHRECVCNT = cCACHRECVCNT;
	}
	public String getCCACHRVSPAYAMT() {
		return CCACHRVSPAYAMT;
	}
	public void setCCACHRVSPAYAMT(String cCACHRVSPAYAMT) {
		CCACHRVSPAYAMT = cCACHRVSPAYAMT;
	}
	public String getCCACHRVSPAYCNT() {
		return CCACHRVSPAYCNT;
	}
	public void setCCACHRVSPAYCNT(String cCACHRVSPAYCNT) {
		CCACHRVSPAYCNT = cCACHRVSPAYCNT;
	}
	public String getCCACHRVSRECVAMT() {
		return CCACHRVSRECVAMT;
	}
	public void setCCACHRVSRECVAMT(String cCACHRVSRECVAMT) {
		CCACHRVSRECVAMT = cCACHRVSRECVAMT;
	}
	public String getCCACHRVSRECVCNT() {
		return CCACHRVSRECVCNT;
	}
	public void setCCACHRVSRECVCNT(String cCACHRVSRECVCNT) {
		CCACHRVSRECVCNT = cCACHRVSRECVCNT;
	}
	public String getCLEARINGPHASE() {
		return CLEARINGPHASE;
	}
	public void setCLEARINGPHASE(String cLEARINGPHASE) {
		CLEARINGPHASE = cLEARINGPHASE;
	}
	public String getDTREQ() {
		return DTREQ;
	}
	public void setDTREQ(String dTREQ) {
		DTREQ = dTREQ;
	}
	public String getDTRSP() {
		return DTRSP;
	}
	public void setDTRSP(String dTRSP) {
		DTRSP = dTRSP;
	}
	public String getNETAMT() {
		return NETAMT;
	}
	public void setNETAMT(String nETAMT) {
		NETAMT = nETAMT;
	}
	public String getPAYAMT() {
		return PAYAMT;
	}
	public void setPAYAMT(String pAYAMT) {
		PAYAMT = pAYAMT;
	}
	public String getPAYCNT() {
		return PAYCNT;
	}
	public void setPAYCNT(String pAYCNT) {
		PAYCNT = pAYCNT;
	}
	public String getPAYFEEAMT() {
		return PAYFEEAMT;
	}
	public void setPAYFEEAMT(String pAYFEEAMT) {
		PAYFEEAMT = pAYFEEAMT;
	}
	public String getPCODE() {
		return PCODE;
	}
	public void setPCODE(String pCODE) {
		PCODE = pCODE;
	}
	public String getPENDING() {
		return PENDING;
	}
	public void setPENDING(String pENDING) {
		PENDING = pENDING;
	}
	public String getRECEIVERBANK() {
		return RECEIVERBANK;
	}
	public void setRECEIVERBANK(String rECEIVERBANK) {
		RECEIVERBANK = rECEIVERBANK;
	}
	public String getRECVAMT() {
		return RECVAMT;
	}
	public void setRECVAMT(String rECVAMT) {
		RECVAMT = rECVAMT;
	}
	public String getRECVCNT() {
		return RECVCNT;
	}
	public void setRECVCNT(String rECVCNT) {
		RECVCNT = rECVCNT;
	}
	public String getRECVFEEAMT() {
		return RECVFEEAMT;
	}
	public void setRECVFEEAMT(String rECVFEEAMT) {
		RECVFEEAMT = rECVFEEAMT;
	}
	public String getRSPCODE() {
		return RSPCODE;
	}
	public void setRSPCODE(String rSPCODE) {
		RSPCODE = rSPCODE;
	}
	public String getRSPRESULTCODE() {
		return RSPRESULTCODE;
	}
	public void setRSPRESULTCODE(String rSPRESULTCODE) {
		RSPRESULTCODE = rSPRESULTCODE;
	}
	public String getSENDERBANK() {
		return SENDERBANK;
	}
	public void setSENDERBANK(String sENDERBANK) {
		SENDERBANK = sENDERBANK;
	}
	public String getSTATUS() {
		return STATUS;
	}
	public void setSTATUS(String sTATUS) {
		STATUS = sTATUS;
	}
	public String getTIMEOUTCODE() {
		return TIMEOUTCODE;
	}
	public void setTIMEOUTCODE(String tIMEOUTCODE) {
		TIMEOUTCODE = tIMEOUTCODE;
	}
	public String getTOTALCNT() {
		return TOTALCNT;
	}
	public void setTOTALCNT(String tOTALCNT) {
		TOTALCNT = tOTALCNT;
	}
	public String getTXDT() {
		return TXDT;
	}
	public void setTXDT(String tXDT) {
		TXDT = tXDT;
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
		SETTLEMENTLOGTAB other = (SETTLEMENTLOGTAB) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
	
	
}
