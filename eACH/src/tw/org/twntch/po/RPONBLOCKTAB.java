package tw.org.twntch.po;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity(name="tw.org.twntch.po.RPONBLOCKTAB")
@Table(name="RPONBLOCKTAB")
public class RPONBLOCKTAB implements Serializable{
	private RPONBLOCKTAB_PK id;
	private String ACCTBAL;
	private String ACCTCODE;
	private String AVAILBAL;
	private String BANKRSPMSG;
	private String BIZDATE;
	private String CHECKTYPE;
	private String CLEARINGCODE;
	private String CLEARINGPHASE;
	private String CONRESULTCODE;
	private String EACHDT;
	private BigDecimal EACHFEE;
	private String FEE;
	private String INACCTNO;
	private String INACQUIRE;
	private String INBANKID;
	private String INCLEARING;
	private BigDecimal INFEE;
	private String INHEAD;
	private String INID;
	private String MERCHANTID;
	private String ORDERNO;
	private String OUTACCTNO;
	private String OUTACQUIRE;
	private String OUTBANKID;
	private String OUTCLEARING;
	private BigDecimal OUTFEE;
	private String OUTHEAD;
	private String OUTID;
	private String PCODE;
	private String PENDINGCODE;
	private String RC1;
	private String RC2;
	private String RC3;
	private String RC4;
	private String RC5;
	private String RC6;
	private String RECEIVERBANK;
	private String RECEIVERID;
	private String RECEIVERSTATUS;
	private String REFUNDDEADLINE;
	private String RESULTSTATUS;
	private String RRN;
	private String SENDERACQUIRE;
	private String SENDERBANK;
	private String SENDERBANKID;
	private String SENDERCLEARING;
	private BigDecimal SENDERFEE;
	private String SENDERHEAD;
	private String SENDERID;
	private String SENDERSTATUS;
	private String TIMEOUTCODE;
	private String TRMLCHECK;
	private String TRMLID;
	private String TRMLMCC;
	private String TXAMT;
	private String TXDT;
	private String TXID;
	
	
	// 新版手續費新增
	private String SENDERFEE_NW;
	private String INFEE_NW;
	private String OUTFEE_NW;
	private String EACHFEE_NW;
	private String WOFEE_NW;
	private String HANDLECHARGE_NW;
	private String FEE_TYPE;
	private String FEE_LVL_TYPE;
	private String SND_BANK_FEE_DISC_NW;
	private String IN_BANK_FEE_DISC_NW;
	private String OUT_BANK_FEE_DISC_NW;
	private String WO_BANK_FEE_DISC_NW;
	private String TCH_FEE_DISC_NW;
	
	@EmbeddedId
	public RPONBLOCKTAB_PK getId() {
		return id;
	}
	public void setId(RPONBLOCKTAB_PK id) {
		this.id = id;
	}
	public String getACCTBAL() {
		return ACCTBAL;
	}
	public void setACCTBAL(String aCCTBAL) {
		ACCTBAL = aCCTBAL;
	}
	public String getACCTCODE() {
		return ACCTCODE;
	}
	public void setACCTCODE(String aCCTCODE) {
		ACCTCODE = aCCTCODE;
	}
	public String getAVAILBAL() {
		return AVAILBAL;
	}
	public void setAVAILBAL(String aVAILBAL) {
		AVAILBAL = aVAILBAL;
	}
	public String getBANKRSPMSG() {
		return BANKRSPMSG;
	}
	public void setBANKRSPMSG(String bANKRSPMSG) {
		BANKRSPMSG = bANKRSPMSG;
	}
	public String getBIZDATE() {
		return BIZDATE;
	}
	public void setBIZDATE(String bIZDATE) {
		BIZDATE = bIZDATE;
	}
	public String getCHECKTYPE() {
		return CHECKTYPE;
	}
	public void setCHECKTYPE(String cHECKTYPE) {
		CHECKTYPE = cHECKTYPE;
	}
	public String getCLEARINGCODE() {
		return CLEARINGCODE;
	}
	public void setCLEARINGCODE(String cLEARINGCODE) {
		CLEARINGCODE = cLEARINGCODE;
	}
	public String getCLEARINGPHASE() {
		return CLEARINGPHASE;
	}
	public void setCLEARINGPHASE(String cLEARINGPHASE) {
		CLEARINGPHASE = cLEARINGPHASE;
	}
	public String getCONRESULTCODE() {
		return CONRESULTCODE;
	}
	public void setCONRESULTCODE(String cONRESULTCODE) {
		CONRESULTCODE = cONRESULTCODE;
	}
	public String getEACHDT() {
		return EACHDT;
	}
	public void setEACHDT(String eACHDT) {
		EACHDT = eACHDT;
	}
	public BigDecimal getEACHFEE() {
		return EACHFEE;
	}
	public void setEACHFEE(BigDecimal eACHFEE) {
		EACHFEE = eACHFEE;
	}
	public String getFEE() {
		return FEE;
	}
	public void setFEE(String fEE) {
		FEE = fEE;
	}
	public String getINACCTNO() {
		return INACCTNO;
	}
	public void setINACCTNO(String iNACCTNO) {
		INACCTNO = iNACCTNO;
	}
	public String getINACQUIRE() {
		return INACQUIRE;
	}
	public void setINACQUIRE(String iNACQUIRE) {
		INACQUIRE = iNACQUIRE;
	}
	public String getINBANKID() {
		return INBANKID;
	}
	public void setINBANKID(String iNBANKID) {
		INBANKID = iNBANKID;
	}
	public String getINCLEARING() {
		return INCLEARING;
	}
	public void setINCLEARING(String iNCLEARING) {
		INCLEARING = iNCLEARING;
	}
	public BigDecimal getINFEE() {
		return INFEE;
	}
	public void setINFEE(BigDecimal iNFEE) {
		INFEE = iNFEE;
	}
	public String getINHEAD() {
		return INHEAD;
	}
	public void setINHEAD(String iNHEAD) {
		INHEAD = iNHEAD;
	}
	public String getINID() {
		return INID;
	}
	public void setINID(String iNID) {
		INID = iNID;
	}
	public String getMERCHANTID() {
		return MERCHANTID;
	}
	public void setMERCHANTID(String mERCHANTID) {
		MERCHANTID = mERCHANTID;
	}
	public String getORDERNO() {
		return ORDERNO;
	}
	public void setORDERNO(String oRDERNO) {
		ORDERNO = oRDERNO;
	}
	public String getOUTACCTNO() {
		return OUTACCTNO;
	}
	public void setOUTACCTNO(String oUTACCTNO) {
		OUTACCTNO = oUTACCTNO;
	}
	public String getOUTACQUIRE() {
		return OUTACQUIRE;
	}
	public void setOUTACQUIRE(String oUTACQUIRE) {
		OUTACQUIRE = oUTACQUIRE;
	}
	public String getOUTBANKID() {
		return OUTBANKID;
	}
	public void setOUTBANKID(String oUTBANKID) {
		OUTBANKID = oUTBANKID;
	}
	public String getOUTCLEARING() {
		return OUTCLEARING;
	}
	public void setOUTCLEARING(String oUTCLEARING) {
		OUTCLEARING = oUTCLEARING;
	}
	public BigDecimal getOUTFEE() {
		return OUTFEE;
	}
	public void setOUTFEE(BigDecimal oUTFEE) {
		OUTFEE = oUTFEE;
	}
	public String getOUTHEAD() {
		return OUTHEAD;
	}
	public void setOUTHEAD(String oUTHEAD) {
		OUTHEAD = oUTHEAD;
	}
	public String getOUTID() {
		return OUTID;
	}
	public void setOUTID(String oUTID) {
		OUTID = oUTID;
	}
	public String getPCODE() {
		return PCODE;
	}
	public void setPCODE(String pCODE) {
		PCODE = pCODE;
	}
	public String getPENDINGCODE() {
		return PENDINGCODE;
	}
	public void setPENDINGCODE(String pENDINGCODE) {
		PENDINGCODE = pENDINGCODE;
	}
	public String getRC1() {
		return RC1;
	}
	public void setRC1(String rC1) {
		RC1 = rC1;
	}
	public String getRC2() {
		return RC2;
	}
	public void setRC2(String rC2) {
		RC2 = rC2;
	}
	public String getRC3() {
		return RC3;
	}
	public void setRC3(String rC3) {
		RC3 = rC3;
	}
	public String getRC4() {
		return RC4;
	}
	public void setRC4(String rC4) {
		RC4 = rC4;
	}
	public String getRC5() {
		return RC5;
	}
	public void setRC5(String rC5) {
		RC5 = rC5;
	}
	public String getRC6() {
		return RC6;
	}
	public void setRC6(String rC6) {
		RC6 = rC6;
	}
	public String getRECEIVERBANK() {
		return RECEIVERBANK;
	}
	public void setRECEIVERBANK(String rECEIVERBANK) {
		RECEIVERBANK = rECEIVERBANK;
	}
	public String getRECEIVERID() {
		return RECEIVERID;
	}
	public void setRECEIVERID(String rECEIVERID) {
		RECEIVERID = rECEIVERID;
	}
	public String getRECEIVERSTATUS() {
		return RECEIVERSTATUS;
	}
	public void setRECEIVERSTATUS(String rECEIVERSTATUS) {
		RECEIVERSTATUS = rECEIVERSTATUS;
	}
	public String getREFUNDDEADLINE() {
		return REFUNDDEADLINE;
	}
	public void setREFUNDDEADLINE(String rEFUNDDEADLINE) {
		REFUNDDEADLINE = rEFUNDDEADLINE;
	}
	public String getRESULTSTATUS() {
		return RESULTSTATUS;
	}
	public void setRESULTSTATUS(String rESULTSTATUS) {
		RESULTSTATUS = rESULTSTATUS;
	}
	public String getRRN() {
		return RRN;
	}
	public void setRRN(String rRN) {
		RRN = rRN;
	}
	public String getSENDERACQUIRE() {
		return SENDERACQUIRE;
	}
	public void setSENDERACQUIRE(String sENDERACQUIRE) {
		SENDERACQUIRE = sENDERACQUIRE;
	}
	public String getSENDERBANK() {
		return SENDERBANK;
	}
	public void setSENDERBANK(String sENDERBANK) {
		SENDERBANK = sENDERBANK;
	}
	public String getSENDERBANKID() {
		return SENDERBANKID;
	}
	public void setSENDERBANKID(String sENDERBANKID) {
		SENDERBANKID = sENDERBANKID;
	}
	public String getSENDERCLEARING() {
		return SENDERCLEARING;
	}
	public void setSENDERCLEARING(String sENDERCLEARING) {
		SENDERCLEARING = sENDERCLEARING;
	}
	public BigDecimal getSENDERFEE() {
		return SENDERFEE;
	}
	public void setSENDERFEE(BigDecimal sENDERFEE) {
		SENDERFEE = sENDERFEE;
	}
	public String getSENDERHEAD() {
		return SENDERHEAD;
	}
	public void setSENDERHEAD(String sENDERHEAD) {
		SENDERHEAD = sENDERHEAD;
	}
	public String getSENDERID() {
		return SENDERID;
	}
	public void setSENDERID(String sENDERID) {
		SENDERID = sENDERID;
	}
	public String getSENDERSTATUS() {
		return SENDERSTATUS;
	}
	public void setSENDERSTATUS(String sENDERSTATUS) {
		SENDERSTATUS = sENDERSTATUS;
	}
	public String getTIMEOUTCODE() {
		return TIMEOUTCODE;
	}
	public void setTIMEOUTCODE(String tIMEOUTCODE) {
		TIMEOUTCODE = tIMEOUTCODE;
	}
	public String getTRMLCHECK() {
		return TRMLCHECK;
	}
	public void setTRMLCHECK(String tRMLCHECK) {
		TRMLCHECK = tRMLCHECK;
	}
	public String getTRMLID() {
		return TRMLID;
	}
	public void setTRMLID(String tRMLID) {
		TRMLID = tRMLID;
	}
	public String getTRMLMCC() {
		return TRMLMCC;
	}
	public void setTRMLMCC(String tRMLMCC) {
		TRMLMCC = tRMLMCC;
	}
	public String getTXAMT() {
		return TXAMT;
	}
	public void setTXAMT(String tXAMT) {
		TXAMT = tXAMT;
	}
	public String getTXDT() {
		return TXDT;
	}
	public void setTXDT(String tXDT) {
		TXDT = tXDT;
	}
	public String getTXID() {
		return TXID;
	}
	public void setTXID(String tXID) {
		TXID = tXID;
	}
	public String getSENDERFEE_NW() {
		return SENDERFEE_NW;
	}
	public void setSENDERFEE_NW(String sENDERFEE_NW) {
		SENDERFEE_NW = sENDERFEE_NW;
	}
	public String getINFEE_NW() {
		return INFEE_NW;
	}
	public void setINFEE_NW(String iNFEE_NW) {
		INFEE_NW = iNFEE_NW;
	}
	public String getOUTFEE_NW() {
		return OUTFEE_NW;
	}
	public void setOUTFEE_NW(String oUTFEE_NW) {
		OUTFEE_NW = oUTFEE_NW;
	}
	public String getEACHFEE_NW() {
		return EACHFEE_NW;
	}
	public void setEACHFEE_NW(String eACHFEE_NW) {
		EACHFEE_NW = eACHFEE_NW;
	}
	public String getWOFEE_NW() {
		return WOFEE_NW;
	}
	public void setWOFEE_NW(String wOFEE_NW) {
		WOFEE_NW = wOFEE_NW;
	}
	public String getHANDLECHARGE_NW() {
		return HANDLECHARGE_NW;
	}
	public void setHANDLECHARGE_NW(String hANDLECHARGE_NW) {
		HANDLECHARGE_NW = hANDLECHARGE_NW;
	}
	public String getFEE_TYPE() {
		return FEE_TYPE;
	}
	public void setFEE_TYPE(String fEE_TYPE) {
		FEE_TYPE = fEE_TYPE;
	}
	public String getFEE_LVL_TYPE() {
		return FEE_LVL_TYPE;
	}
	public void setFEE_LVL_TYPE(String fEE_LVL_TYPE) {
		FEE_LVL_TYPE = fEE_LVL_TYPE;
	}
	public String getSND_BANK_FEE_DISC_NW() {
		return SND_BANK_FEE_DISC_NW;
	}
	public void setSND_BANK_FEE_DISC_NW(String sND_BANK_FEE_DISC_NW) {
		SND_BANK_FEE_DISC_NW = sND_BANK_FEE_DISC_NW;
	}
	public String getIN_BANK_FEE_DISC_NW() {
		return IN_BANK_FEE_DISC_NW;
	}
	public void setIN_BANK_FEE_DISC_NW(String iN_BANK_FEE_DISC_NW) {
		IN_BANK_FEE_DISC_NW = iN_BANK_FEE_DISC_NW;
	}
	public String getOUT_BANK_FEE_DISC_NW() {
		return OUT_BANK_FEE_DISC_NW;
	}
	public void setOUT_BANK_FEE_DISC_NW(String oUT_BANK_FEE_DISC_NW) {
		OUT_BANK_FEE_DISC_NW = oUT_BANK_FEE_DISC_NW;
	}
	public String getWO_BANK_FEE_DISC_NW() {
		return WO_BANK_FEE_DISC_NW;
	}
	public void setWO_BANK_FEE_DISC_NW(String wO_BANK_FEE_DISC_NW) {
		WO_BANK_FEE_DISC_NW = wO_BANK_FEE_DISC_NW;
	}
	public String getTCH_FEE_DISC_NW() {
		return TCH_FEE_DISC_NW;
	}
	public void setTCH_FEE_DISC_NW(String tCH_FEE_DISC_NW) {
		TCH_FEE_DISC_NW = tCH_FEE_DISC_NW;
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
		RPONBLOCKTAB other = (RPONBLOCKTAB) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
	
}
