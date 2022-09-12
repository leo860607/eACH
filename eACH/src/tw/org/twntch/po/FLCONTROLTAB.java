package tw.org.twntch.po;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity (name="tw.org.twntch.po.FLCONTROLTAB")
@Table(name="FLCONTROLTAB")
public class FLCONTROLTAB implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3777315493501246982L;
	@EmbeddedId
	private FLCONTROLTAB_PK id;
	private String CLEARINGPHASE;
	private String CREATEDT;
	private String STARTDT;
	private String ENDDT;
	private BigDecimal TOTALCOUNT;
	private BigDecimal TOTALAMT;
	private BigDecimal REJECTCOUNT;
	private BigDecimal REJECTAMT;
	private BigDecimal ACCEPTCOUNT;
	private BigDecimal ACCEPTAMT;
	private BigDecimal PROCCOUNT;
	private BigDecimal PROCAMT;
	private String LASTMODIFYDT;
	private String ACHFLAG;
	private String STATUS;
	private String RFILESTATUS;
	private String EFILESTATUS;
	private String OFILESTATUS;
	private String FILEREJECT;
	private String PFILENAME;
	private String RFILENAME;
	private String EFILENAME;
	private String OFILENAME;
	private String ACQUIREID;
	private String FILELAYOUT;
	private String SENDERBANK;
	@Transient
	private String BIZDATE;
	@Transient
	private BigDecimal PROCSEQ;
	@Transient
	private String BATCHSEQ;
	@Transient
	private String FILENAME;
	@Transient
	private String REJECTFILEDOWNLOAD;
	@Transient
	private BigDecimal SUCCESSCOUNT;
	@Transient
	private BigDecimal COUNT_A;
	@Transient
	private BigDecimal AMT_A;
	@Transient
	private BigDecimal COUNT_P;
	@Transient
	private BigDecimal AMT_P;
	@Transient
	private BigDecimal COUNT_R;
	@Transient
	private BigDecimal AMT_R;
	@Transient
	private String RESULTFILEDOWNLOAD;
	public FLCONTROLTAB_PK getId() {
		return id;
	}
	public void setId(FLCONTROLTAB_PK id) {
		this.id = id;
	}
	public String getCLEARINGPHASE() {
		return CLEARINGPHASE;
	}
	public void setCLEARINGPHASE(String cLEARINGPHASE) {
		CLEARINGPHASE = cLEARINGPHASE;
	}
	public String getCREATEDT() {
		return CREATEDT;
	}
	public void setCREATEDT(String cREATEDT) {
		CREATEDT = cREATEDT;
	}
	public String getSTARTDT() {
		return STARTDT;
	}
	public void setSTARTDT(String sTARTDT) {
		STARTDT = sTARTDT;
	}
	public String getENDDT() {
		return ENDDT;
	}
	public void setENDDT(String eNDDT) {
		ENDDT = eNDDT;
	}
	public BigDecimal getTOTALCOUNT() {
		return TOTALCOUNT;
	}
	public void setTOTALCOUNT(BigDecimal tOTALCOUNT) {
		TOTALCOUNT = tOTALCOUNT;
	}
	public BigDecimal getTOTALAMT() {
		return TOTALAMT;
	}
	public void setTOTALAMT(BigDecimal tOTALAMT) {
		TOTALAMT = tOTALAMT;
	}
	public BigDecimal getREJECTCOUNT() {
		return REJECTCOUNT;
	}
	public void setREJECTCOUNT(BigDecimal rEJECTCOUNT) {
		REJECTCOUNT = rEJECTCOUNT;
	}
	public BigDecimal getREJECTAMT() {
		return REJECTAMT;
	}
	public void setREJECTAMT(BigDecimal rEJECTAMT) {
		REJECTAMT = rEJECTAMT;
	}
	public BigDecimal getACCEPTCOUNT() {
		return ACCEPTCOUNT;
	}
	public void setACCEPTCOUNT(BigDecimal aCCEPTCOUNT) {
		ACCEPTCOUNT = aCCEPTCOUNT;
	}
	public BigDecimal getACCEPTAMT() {
		return ACCEPTAMT;
	}
	public void setACCEPTAMT(BigDecimal aCCEPTAMT) {
		ACCEPTAMT = aCCEPTAMT;
	}
	public BigDecimal getPROCCOUNT() {
		return PROCCOUNT;
	}
	public void setPROCCOUNT(BigDecimal pROCCOUNT) {
		PROCCOUNT = pROCCOUNT;
	}
	public BigDecimal getPROCAMT() {
		return PROCAMT;
	}
	public void setPROCAMT(BigDecimal pROCAMT) {
		PROCAMT = pROCAMT;
	}
	public String getLASTMODIFYDT() {
		return LASTMODIFYDT;
	}
	public void setLASTMODIFYDT(String lASTMODIFYDT) {
		LASTMODIFYDT = lASTMODIFYDT;
	}
	public String getACHFLAG() {
		return ACHFLAG;
	}
	public void setACHFLAG(String aCHFLAG) {
		ACHFLAG = aCHFLAG;
	}
	public String getSTATUS() {
		return STATUS;
	}
	public void setSTATUS(String sTATUS) {
		STATUS = sTATUS;
	}
	public String getRFILESTATUS() {
		return RFILESTATUS;
	}
	public void setRFILESTATUS(String rFILESTATUS) {
		RFILESTATUS = rFILESTATUS;
	}
	public String getEFILESTATUS() {
		return EFILESTATUS;
	}
	public void setEFILESTATUS(String eFILESTATUS) {
		EFILESTATUS = eFILESTATUS;
	}
	public String getOFILESTATUS() {
		return OFILESTATUS;
	}
	public void setOFILESTATUS(String oFILESTATUS) {
		OFILESTATUS = oFILESTATUS;
	}
	public String getFILEREJECT() {
		return FILEREJECT;
	}
	public void setFILEREJECT(String fILEREJECT) {
		FILEREJECT = fILEREJECT;
	}
	public String getPFILENAME() {
		return PFILENAME;
	}
	public void setPFILENAME(String pFILENAME) {
		PFILENAME = pFILENAME;
	}
	public String getRFILENAME() {
		return RFILENAME;
	}
	public void setRFILENAME(String rFILENAME) {
		RFILENAME = rFILENAME;
	}
	public String getEFILENAME() {
		return EFILENAME;
	}
	public void setEFILENAME(String eFILENAME) {
		EFILENAME = eFILENAME;
	}
	public String getOFILENAME() {
		return OFILENAME;
	}
	public void setOFILENAME(String oFILENAME) {
		OFILENAME = oFILENAME;
	}
	public String getACQUIREID() {
		return ACQUIREID;
	}
	public void setACQUIREID(String aCQUIREID) {
		ACQUIREID = aCQUIREID;
	}
	public String getFILELAYOUT() {
		return FILELAYOUT;
	}
	public void setFILELAYOUT(String fILELAYOUT) {
		FILELAYOUT = fILELAYOUT;
	}
	public String getSENDERBANK() {
		return SENDERBANK;
	}
	public void setSENDERBANK(String sENDERBANK) {
		SENDERBANK = sENDERBANK;
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
	public String getFILENAME() {
		return FILENAME;
	}
	public void setFILENAME(String fILENAME) {
		FILENAME = fILENAME;
	}
	public String getREJECTFILEDOWNLOAD() {
		return REJECTFILEDOWNLOAD;
	}
	public void setREJECTFILEDOWNLOAD(String rEJECTFILEDOWNLOAD) {
		REJECTFILEDOWNLOAD = rEJECTFILEDOWNLOAD;
	}
	public BigDecimal getSUCCESSCOUNT() {
		return SUCCESSCOUNT;
	}
	public void setSUCCESSCOUNT(BigDecimal sUCCESSCOUNT) {
		SUCCESSCOUNT = sUCCESSCOUNT;
	}
	public BigDecimal getCOUNT_A() {
		return COUNT_A;
	}
	public void setCOUNT_A(BigDecimal cOUNT_A) {
		COUNT_A = cOUNT_A;
	}
	public BigDecimal getAMT_A() {
		return AMT_A;
	}
	public void setAMT_A(BigDecimal aMT_A) {
		AMT_A = aMT_A;
	}
	public BigDecimal getCOUNT_P() {
		return COUNT_P;
	}
	public void setCOUNT_P(BigDecimal cOUNT_P) {
		COUNT_P = cOUNT_P;
	}
	public BigDecimal getAMT_P() {
		return AMT_P;
	}
	public void setAMT_P(BigDecimal aMT_P) {
		AMT_P = aMT_P;
	}
	public BigDecimal getCOUNT_R() {
		return COUNT_R;
	}
	public void setCOUNT_R(BigDecimal cOUNT_R) {
		COUNT_R = cOUNT_R;
	}
	public BigDecimal getAMT_R() {
		return AMT_R;
	}
	public void setAMT_R(BigDecimal aMT_R) {
		AMT_R = aMT_R;
	}
	public String getRESULTFILEDOWNLOAD() {
		return RESULTFILEDOWNLOAD;
	}
	public void setRESULTFILEDOWNLOAD(String rESULTFILEDOWNLOAD) {
		RESULTFILEDOWNLOAD = rESULTFILEDOWNLOAD;
	}
}
