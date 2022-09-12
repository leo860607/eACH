package tw.org.twntch.po;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity(name= "tw.org.twntch.po.ONBLOCKTAB")
@Table(name="ONBLOCKTAB")
public class ONBLOCKTAB implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4790279133399956018L;
	private ONBLOCKTAB_PK id;
	private  String     TXDATE          ;    
	private  String     STAN            ;    
	private  String     PCODE           ;    
	private  String     SENDERBANK      ;    
	private  String     RECEIVERBANK    ;    
	private  String     TXDT            ;    
	private  String        SENDERSTATUS    ;    
	private  String        RECEIVERSTATUS  ;    
	private  String     TIMEOUTCODE     ;    
	private  String     CONRESULTCODE   ;    
	private  String        ACCTCODE        ;    
	private  String        CLEARINGCODE    ;    
	private  String        PENDINGCODE     ;    
	private  String     SENDERCLEARING  ;    
	private  String     INCLEARING      ;    
	private  String     OUTCLEARING     ;    
	private  String     SENDERACQUIRE   ;    
	private  String     INACQUIRE       ;    
	private  String     OUTACQUIRE      ;    
	private  String     SENDERHEAD      ;    
	private  String     INHEAD          ;    
	private  String     OUTHEAD         ;    
	private  BigDecimal     SENDERFEE       ;    
	private  BigDecimal     INFEE           ;    
	private  BigDecimal     OUTFEE          ;    
	private  BigDecimal     EACHFEE         ;    
	private  String     REFUNDDEADLINE  ;    
	private  String     SENDERID        ;    
	private  String     RECEIVERID      ;    
	private  String     TXID            ;    
	private  String     TXAMT           ;    
	private  String     FEE             ;    
	private  String     SENDERBANKID    ;    
	private  String     INBANKID        ;    
	private  String     OUTBANKID       ;    
	private  String     BIZDATE         ;    
	private  String     EACHDT          ;    
	private  String     CLEARINGPHASE   ;    
	private  String     INACCTNO        ;    
	private  String     OUTACCTNO       ;    
	private  String     INID            ;    
	private  String     OUTID           ;    
	private  String     ACCTBAL         ;    
	private  String     AVAILBAL        ;    
	private  String        CHECKTYPE       ;    
	private  String     MERCHANTID      ;    
	private  String     ORDERNO         ;    
	private  String     TRMLID          ;    
	private  String     TRMLCHECK       ;    
	private  String     BANKRSPMSG      ;    
	private  String     RRN             ;    
	private  String        RESULTSTATUS    ;    
	private  String     RC1             ;    
	private  String     RC2             ;    
	private  String     RC3             ;    
	private  String     RC4             ;    
	private  String     RC5             ;    
	private  String     RC6             ;    
	private  String     DT_REQ_1        ;    
	private  String     DT_REQ_2        ;    
	private  String     DT_REQ_3        ;    
	private  String     DT_RSP_1        ;    
	private  String     DT_RSP_2        ;    
	private  String     DT_RSP_3        ;    
	private  String     DT_CON_1        ;    
	private  String     TRMLMCC         ;    
	private  String     DT_CON_2        ;    
	private  String     DT_CON_3        ;
	//查詢統計的交易資料查詢的onblocktab_trad_edit_q.jsp裡的RC1~RC6的說明要加上TXN_ERROR_CODE的ERROR_DESC而設
	private  String 	ERR_DESC1;
	private  String 	ERR_DESC2;
	private  String 	ERR_DESC3;
	private  String 	ERR_DESC4;
	private  String 	ERR_DESC5;
	private  String 	ERR_DESC6;
	@EmbeddedId
	public ONBLOCKTAB_PK getId() {
		return id;
	}
	public void setId(ONBLOCKTAB_PK id) {
		this.id = id;
	}
	@Transient
	public String getTXDATE() {
		return TXDATE;
	}
	public void setTXDATE(String tXDATE) {
		TXDATE = tXDATE;
	}
	@Transient
	public String getSTAN() {
		return STAN;
	}
	public void setSTAN(String sTAN) {
		STAN = sTAN;
	}
	public String getPCODE() {
		return PCODE;
	}
	public void setPCODE(String pCODE) {
		PCODE = pCODE;
	}
	public String getSENDERBANK() {
		return SENDERBANK;
	}
	public void setSENDERBANK(String sENDERBANK) {
		SENDERBANK = sENDERBANK;
	}
	public String getRECEIVERBANK() {
		return RECEIVERBANK;
	}
	public void setRECEIVERBANK(String rECEIVERBANK) {
		RECEIVERBANK = rECEIVERBANK;
	}
	public String getTXDT() {
		return TXDT;
	}
	public void setTXDT(String tXDT) {
		TXDT = tXDT;
	}
	public String getSENDERSTATUS() {
		return SENDERSTATUS;
	}
	public void setSENDERSTATUS(String sENDERSTATUS) {
		SENDERSTATUS = sENDERSTATUS;
	}
	public String getRECEIVERSTATUS() {
		return RECEIVERSTATUS;
	}
	public void setRECEIVERSTATUS(String rECEIVERSTATUS) {
		RECEIVERSTATUS = rECEIVERSTATUS;
	}
	public String getTIMEOUTCODE() {
		return TIMEOUTCODE;
	}
	public void setTIMEOUTCODE(String tIMEOUTCODE) {
		TIMEOUTCODE = tIMEOUTCODE;
	}
	public String getCONRESULTCODE() {
		return CONRESULTCODE;
	}
	public void setCONRESULTCODE(String cONRESULTCODE) {
		CONRESULTCODE = cONRESULTCODE;
	}
	public String getACCTCODE() {
		return ACCTCODE;
	}
	public void setACCTCODE(String aCCTCODE) {
		ACCTCODE = aCCTCODE;
	}
	public String getCLEARINGCODE() {
		return CLEARINGCODE;
	}
	public void setCLEARINGCODE(String cLEARINGCODE) {
		CLEARINGCODE = cLEARINGCODE;
	}
	public String getPENDINGCODE() {
		return PENDINGCODE;
	}
	public void setPENDINGCODE(String pENDINGCODE) {
		PENDINGCODE = pENDINGCODE;
	}
	public String getSENDERCLEARING() {
		return SENDERCLEARING;
	}
	public void setSENDERCLEARING(String sENDERCLEARING) {
		SENDERCLEARING = sENDERCLEARING;
	}
	public String getINCLEARING() {
		return INCLEARING;
	}
	public void setINCLEARING(String iNCLEARING) {
		INCLEARING = iNCLEARING;
	}
	public String getOUTCLEARING() {
		return OUTCLEARING;
	}
	public void setOUTCLEARING(String oUTCLEARING) {
		OUTCLEARING = oUTCLEARING;
	}
	public String getSENDERACQUIRE() {
		return SENDERACQUIRE;
	}
	public void setSENDERACQUIRE(String sENDERACQUIRE) {
		SENDERACQUIRE = sENDERACQUIRE;
	}
	public String getINACQUIRE() {
		return INACQUIRE;
	}
	public void setINACQUIRE(String iNACQUIRE) {
		INACQUIRE = iNACQUIRE;
	}
	public String getOUTACQUIRE() {
		return OUTACQUIRE;
	}
	public void setOUTACQUIRE(String oUTACQUIRE) {
		OUTACQUIRE = oUTACQUIRE;
	}
	public String getSENDERHEAD() {
		return SENDERHEAD;
	}
	public void setSENDERHEAD(String sENDERHEAD) {
		SENDERHEAD = sENDERHEAD;
	}
	public String getINHEAD() {
		return INHEAD;
	}
	public void setINHEAD(String iNHEAD) {
		INHEAD = iNHEAD;
	}
	public String getOUTHEAD() {
		return OUTHEAD;
	}
	public void setOUTHEAD(String oUTHEAD) {
		OUTHEAD = oUTHEAD;
	}
	public BigDecimal getSENDERFEE() {
		return SENDERFEE;
	}
	public void setSENDERFEE(BigDecimal sENDERFEE) {
		SENDERFEE = sENDERFEE;
	}
	public BigDecimal getINFEE() {
		return INFEE;
	}
	public void setINFEE(BigDecimal iNFEE) {
		INFEE = iNFEE;
	}
	public BigDecimal getOUTFEE() {
		return OUTFEE;
	}
	public void setOUTFEE(BigDecimal oUTFEE) {
		OUTFEE = oUTFEE;
	}
	public BigDecimal getEACHFEE() {
		return EACHFEE;
	}
	public void setEACHFEE(BigDecimal eACHFEE) {
		EACHFEE = eACHFEE;
	}
	public String getREFUNDDEADLINE() {
		return REFUNDDEADLINE;
	}
	public void setREFUNDDEADLINE(String rEFUNDDEADLINE) {
		REFUNDDEADLINE = rEFUNDDEADLINE;
	}
	public String getSENDERID() {
		return SENDERID;
	}
	public void setSENDERID(String sENDERID) {
		SENDERID = sENDERID;
	}
	public String getRECEIVERID() {
		return RECEIVERID;
	}
	public void setRECEIVERID(String rECEIVERID) {
		RECEIVERID = rECEIVERID;
	}
	public String getTXID() {
		return TXID;
	}
	public void setTXID(String tXID) {
		TXID = tXID;
	}
	public String getTXAMT() {
		return TXAMT;
	}
	public void setTXAMT(String tXAMT) {
		TXAMT = tXAMT;
	}
	public String getFEE() {
		return FEE;
	}
	public void setFEE(String fEE) {
		FEE = fEE;
	}
	public String getSENDERBANKID() {
		return SENDERBANKID;
	}
	public void setSENDERBANKID(String sENDERBANKID) {
		SENDERBANKID = sENDERBANKID;
	}
	public String getINBANKID() {
		return INBANKID;
	}
	public void setINBANKID(String iNBANKID) {
		INBANKID = iNBANKID;
	}
	public String getOUTBANKID() {
		return OUTBANKID;
	}
	public void setOUTBANKID(String oUTBANKID) {
		OUTBANKID = oUTBANKID;
	}
	public String getBIZDATE() {
		return BIZDATE;
	}
	public void setBIZDATE(String bIZDATE) {
		BIZDATE = bIZDATE;
	}
	public String getEACHDT() {
		return EACHDT;
	}
	public void setEACHDT(String eACHDT) {
		EACHDT = eACHDT;
	}
	public String getCLEARINGPHASE() {
		return CLEARINGPHASE;
	}
	public void setCLEARINGPHASE(String cLEARINGPHASE) {
		CLEARINGPHASE = cLEARINGPHASE;
	}
	public String getINACCTNO() {
		return INACCTNO;
	}
	public void setINACCTNO(String iNACCTNO) {
		INACCTNO = iNACCTNO;
	}
	public String getOUTACCTNO() {
		return OUTACCTNO;
	}
	public void setOUTACCTNO(String oUTACCTNO) {
		OUTACCTNO = oUTACCTNO;
	}
	public String getINID() {
		return INID;
	}
	public void setINID(String iNID) {
		INID = iNID;
	}
	public String getOUTID() {
		return OUTID;
	}
	public void setOUTID(String oUTID) {
		OUTID = oUTID;
	}
	public String getACCTBAL() {
		return ACCTBAL;
	}
	public void setACCTBAL(String aCCTBAL) {
		ACCTBAL = aCCTBAL;
	}
	public String getAVAILBAL() {
		return AVAILBAL;
	}
	public void setAVAILBAL(String aVAILBAL) {
		AVAILBAL = aVAILBAL;
	}
	public String getCHECKTYPE() {
		return CHECKTYPE;
	}
	public void setCHECKTYPE(String cHECKTYPE) {
		CHECKTYPE = cHECKTYPE;
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
	public String getTRMLID() {
		return TRMLID;
	}
	public void setTRMLID(String tRMLID) {
		TRMLID = tRMLID;
	}
	public String getTRMLCHECK() {
		return TRMLCHECK;
	}
	public void setTRMLCHECK(String tRMLCHECK) {
		TRMLCHECK = tRMLCHECK;
	}
	public String getBANKRSPMSG() {
		return BANKRSPMSG;
	}
	public void setBANKRSPMSG(String bANKRSPMSG) {
		BANKRSPMSG = bANKRSPMSG;
	}
	public String getRRN() {
		return RRN;
	}
	public void setRRN(String rRN) {
		RRN = rRN;
	}
	public String getRESULTSTATUS() {
		return RESULTSTATUS;
	}
	public void setRESULTSTATUS(String rESULTSTATUS) {
		RESULTSTATUS = rESULTSTATUS;
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
	public String getDT_REQ_1() {
		return DT_REQ_1;
	}
	public void setDT_REQ_1(String dT_REQ_1) {
		DT_REQ_1 = dT_REQ_1;
	}
	public String getDT_REQ_2() {
		return DT_REQ_2;
	}
	public void setDT_REQ_2(String dT_REQ_2) {
		DT_REQ_2 = dT_REQ_2;
	}
	public String getDT_REQ_3() {
		return DT_REQ_3;
	}
	public void setDT_REQ_3(String dT_REQ_3) {
		DT_REQ_3 = dT_REQ_3;
	}
	public String getDT_RSP_1() {
		return DT_RSP_1;
	}
	public void setDT_RSP_1(String dT_RSP_1) {
		DT_RSP_1 = dT_RSP_1;
	}
	public String getDT_RSP_2() {
		return DT_RSP_2;
	}
	public void setDT_RSP_2(String dT_RSP_2) {
		DT_RSP_2 = dT_RSP_2;
	}
	public String getDT_RSP_3() {
		return DT_RSP_3;
	}
	public void setDT_RSP_3(String dT_RSP_3) {
		DT_RSP_3 = dT_RSP_3;
	}
	public String getDT_CON_1() {
		return DT_CON_1;
	}
	public void setDT_CON_1(String dT_CON_1) {
		DT_CON_1 = dT_CON_1;
	}
	public String getTRMLMCC() {
		return TRMLMCC;
	}
	public void setTRMLMCC(String tRMLMCC) {
		TRMLMCC = tRMLMCC;
	}
	public String getDT_CON_2() {
		return DT_CON_2;
	}
	public void setDT_CON_2(String dT_CON_2) {
		DT_CON_2 = dT_CON_2;
	}
	public String getDT_CON_3() {
		return DT_CON_3;
	}
	public void setDT_CON_3(String dT_CON_3) {
		DT_CON_3 = dT_CON_3;
	}
	public String getERR_DESC1() {
		return ERR_DESC1;
	}
	public void setERR_DESC1(String eRR_DESC1) {
		ERR_DESC1 = eRR_DESC1;
	}
	public String getERR_DESC2() {
		return ERR_DESC2;
	}
	public void setERR_DESC2(String eRR_DESC2) {
		ERR_DESC2 = eRR_DESC2;
	}
	public String getERR_DESC3() {
		return ERR_DESC3;
	}
	public void setERR_DESC3(String eRR_DESC3) {
		ERR_DESC3 = eRR_DESC3;
	}
	public String getERR_DESC4() {
		return ERR_DESC4;
	}
	public void setERR_DESC4(String eRR_DESC4) {
		ERR_DESC4 = eRR_DESC4;
	}
	public String getERR_DESC5() {
		return ERR_DESC5;
	}
	public void setERR_DESC5(String eRR_DESC5) {
		ERR_DESC5 = eRR_DESC5;
	}
	public String getERR_DESC6() {
		return ERR_DESC6;
	}
	public void setERR_DESC6(String eRR_DESC6) {
		ERR_DESC6 = eRR_DESC6;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ACCTBAL == null) ? 0 : ACCTBAL.hashCode());
		result = prime * result
				+ ((ACCTCODE == null) ? 0 : ACCTCODE.hashCode());
		result = prime * result
				+ ((AVAILBAL == null) ? 0 : AVAILBAL.hashCode());
		result = prime * result
				+ ((BANKRSPMSG == null) ? 0 : BANKRSPMSG.hashCode());
		result = prime * result + ((BIZDATE == null) ? 0 : BIZDATE.hashCode());
		result = prime * result
				+ ((CHECKTYPE == null) ? 0 : CHECKTYPE.hashCode());
		result = prime * result
				+ ((CLEARINGCODE == null) ? 0 : CLEARINGCODE.hashCode());
		result = prime * result
				+ ((CLEARINGPHASE == null) ? 0 : CLEARINGPHASE.hashCode());
		result = prime * result
				+ ((CONRESULTCODE == null) ? 0 : CONRESULTCODE.hashCode());
		result = prime * result
				+ ((DT_CON_1 == null) ? 0 : DT_CON_1.hashCode());
		result = prime * result
				+ ((DT_CON_2 == null) ? 0 : DT_CON_2.hashCode());
		result = prime * result
				+ ((DT_CON_3 == null) ? 0 : DT_CON_3.hashCode());
		result = prime * result
				+ ((DT_REQ_1 == null) ? 0 : DT_REQ_1.hashCode());
		result = prime * result
				+ ((DT_REQ_2 == null) ? 0 : DT_REQ_2.hashCode());
		result = prime * result
				+ ((DT_REQ_3 == null) ? 0 : DT_REQ_3.hashCode());
		result = prime * result
				+ ((DT_RSP_1 == null) ? 0 : DT_RSP_1.hashCode());
		result = prime * result
				+ ((DT_RSP_2 == null) ? 0 : DT_RSP_2.hashCode());
		result = prime * result
				+ ((DT_RSP_3 == null) ? 0 : DT_RSP_3.hashCode());
		result = prime * result + ((EACHDT == null) ? 0 : EACHDT.hashCode());
		result = prime * result + ((EACHFEE == null) ? 0 : EACHFEE.hashCode());
		result = prime * result + ((FEE == null) ? 0 : FEE.hashCode());
		result = prime * result
				+ ((INACCTNO == null) ? 0 : INACCTNO.hashCode());
		result = prime * result
				+ ((INACQUIRE == null) ? 0 : INACQUIRE.hashCode());
		result = prime * result
				+ ((INBANKID == null) ? 0 : INBANKID.hashCode());
		result = prime * result
				+ ((INCLEARING == null) ? 0 : INCLEARING.hashCode());
		result = prime * result + ((INFEE == null) ? 0 : INFEE.hashCode());
		result = prime * result + ((INHEAD == null) ? 0 : INHEAD.hashCode());
		result = prime * result + ((INID == null) ? 0 : INID.hashCode());
		result = prime * result
				+ ((MERCHANTID == null) ? 0 : MERCHANTID.hashCode());
		result = prime * result + ((ORDERNO == null) ? 0 : ORDERNO.hashCode());
		result = prime * result
				+ ((OUTACCTNO == null) ? 0 : OUTACCTNO.hashCode());
		result = prime * result
				+ ((OUTACQUIRE == null) ? 0 : OUTACQUIRE.hashCode());
		result = prime * result
				+ ((OUTBANKID == null) ? 0 : OUTBANKID.hashCode());
		result = prime * result
				+ ((OUTCLEARING == null) ? 0 : OUTCLEARING.hashCode());
		result = prime * result + ((OUTFEE == null) ? 0 : OUTFEE.hashCode());
		result = prime * result + ((OUTHEAD == null) ? 0 : OUTHEAD.hashCode());
		result = prime * result + ((OUTID == null) ? 0 : OUTID.hashCode());
		result = prime * result + ((PCODE == null) ? 0 : PCODE.hashCode());
		result = prime * result
				+ ((PENDINGCODE == null) ? 0 : PENDINGCODE.hashCode());
		result = prime * result + ((RC1 == null) ? 0 : RC1.hashCode());
		result = prime * result + ((RC2 == null) ? 0 : RC2.hashCode());
		result = prime * result + ((RC3 == null) ? 0 : RC3.hashCode());
		result = prime * result + ((RC4 == null) ? 0 : RC4.hashCode());
		result = prime * result + ((RC5 == null) ? 0 : RC5.hashCode());
		result = prime * result + ((RC6 == null) ? 0 : RC6.hashCode());
		result = prime * result
				+ ((RECEIVERBANK == null) ? 0 : RECEIVERBANK.hashCode());
		result = prime * result
				+ ((RECEIVERID == null) ? 0 : RECEIVERID.hashCode());
		result = prime * result
				+ ((RECEIVERSTATUS == null) ? 0 : RECEIVERSTATUS.hashCode());
		result = prime * result
				+ ((REFUNDDEADLINE == null) ? 0 : REFUNDDEADLINE.hashCode());
		result = prime * result
				+ ((RESULTSTATUS == null) ? 0 : RESULTSTATUS.hashCode());
		result = prime * result + ((RRN == null) ? 0 : RRN.hashCode());
		result = prime * result
				+ ((SENDERACQUIRE == null) ? 0 : SENDERACQUIRE.hashCode());
		result = prime * result
				+ ((SENDERBANK == null) ? 0 : SENDERBANK.hashCode());
		result = prime * result
				+ ((SENDERBANKID == null) ? 0 : SENDERBANKID.hashCode());
		result = prime * result
				+ ((SENDERCLEARING == null) ? 0 : SENDERCLEARING.hashCode());
		result = prime * result
				+ ((SENDERFEE == null) ? 0 : SENDERFEE.hashCode());
		result = prime * result
				+ ((SENDERHEAD == null) ? 0 : SENDERHEAD.hashCode());
		result = prime * result
				+ ((SENDERID == null) ? 0 : SENDERID.hashCode());
		result = prime * result
				+ ((SENDERSTATUS == null) ? 0 : SENDERSTATUS.hashCode());
		result = prime * result + ((STAN == null) ? 0 : STAN.hashCode());
		result = prime * result
				+ ((TIMEOUTCODE == null) ? 0 : TIMEOUTCODE.hashCode());
		result = prime * result
				+ ((TRMLCHECK == null) ? 0 : TRMLCHECK.hashCode());
		result = prime * result + ((TRMLID == null) ? 0 : TRMLID.hashCode());
		result = prime * result + ((TRMLMCC == null) ? 0 : TRMLMCC.hashCode());
		result = prime * result + ((TXAMT == null) ? 0 : TXAMT.hashCode());
		result = prime * result + ((TXDATE == null) ? 0 : TXDATE.hashCode());
		result = prime * result + ((TXDT == null) ? 0 : TXDT.hashCode());
		result = prime * result + ((TXID == null) ? 0 : TXID.hashCode());
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
		ONBLOCKTAB other = (ONBLOCKTAB) obj;
		if (ACCTBAL == null) {
			if (other.ACCTBAL != null)
				return false;
		} else if (!ACCTBAL.equals(other.ACCTBAL))
			return false;
		if (ACCTCODE == null) {
			if (other.ACCTCODE != null)
				return false;
		} else if (!ACCTCODE.equals(other.ACCTCODE))
			return false;
		if (AVAILBAL == null) {
			if (other.AVAILBAL != null)
				return false;
		} else if (!AVAILBAL.equals(other.AVAILBAL))
			return false;
		if (BANKRSPMSG == null) {
			if (other.BANKRSPMSG != null)
				return false;
		} else if (!BANKRSPMSG.equals(other.BANKRSPMSG))
			return false;
		if (BIZDATE == null) {
			if (other.BIZDATE != null)
				return false;
		} else if (!BIZDATE.equals(other.BIZDATE))
			return false;
		if (CHECKTYPE == null) {
			if (other.CHECKTYPE != null)
				return false;
		} else if (!CHECKTYPE.equals(other.CHECKTYPE))
			return false;
		if (CLEARINGCODE == null) {
			if (other.CLEARINGCODE != null)
				return false;
		} else if (!CLEARINGCODE.equals(other.CLEARINGCODE))
			return false;
		if (CLEARINGPHASE == null) {
			if (other.CLEARINGPHASE != null)
				return false;
		} else if (!CLEARINGPHASE.equals(other.CLEARINGPHASE))
			return false;
		if (CONRESULTCODE == null) {
			if (other.CONRESULTCODE != null)
				return false;
		} else if (!CONRESULTCODE.equals(other.CONRESULTCODE))
			return false;
		if (DT_CON_1 == null) {
			if (other.DT_CON_1 != null)
				return false;
		} else if (!DT_CON_1.equals(other.DT_CON_1))
			return false;
		if (DT_CON_2 == null) {
			if (other.DT_CON_2 != null)
				return false;
		} else if (!DT_CON_2.equals(other.DT_CON_2))
			return false;
		if (DT_CON_3 == null) {
			if (other.DT_CON_3 != null)
				return false;
		} else if (!DT_CON_3.equals(other.DT_CON_3))
			return false;
		if (DT_REQ_1 == null) {
			if (other.DT_REQ_1 != null)
				return false;
		} else if (!DT_REQ_1.equals(other.DT_REQ_1))
			return false;
		if (DT_REQ_2 == null) {
			if (other.DT_REQ_2 != null)
				return false;
		} else if (!DT_REQ_2.equals(other.DT_REQ_2))
			return false;
		if (DT_REQ_3 == null) {
			if (other.DT_REQ_3 != null)
				return false;
		} else if (!DT_REQ_3.equals(other.DT_REQ_3))
			return false;
		if (DT_RSP_1 == null) {
			if (other.DT_RSP_1 != null)
				return false;
		} else if (!DT_RSP_1.equals(other.DT_RSP_1))
			return false;
		if (DT_RSP_2 == null) {
			if (other.DT_RSP_2 != null)
				return false;
		} else if (!DT_RSP_2.equals(other.DT_RSP_2))
			return false;
		if (DT_RSP_3 == null) {
			if (other.DT_RSP_3 != null)
				return false;
		} else if (!DT_RSP_3.equals(other.DT_RSP_3))
			return false;
		if (EACHDT == null) {
			if (other.EACHDT != null)
				return false;
		} else if (!EACHDT.equals(other.EACHDT))
			return false;
		if (EACHFEE == null) {
			if (other.EACHFEE != null)
				return false;
		} else if (!EACHFEE.equals(other.EACHFEE))
			return false;
		if (FEE == null) {
			if (other.FEE != null)
				return false;
		} else if (!FEE.equals(other.FEE))
			return false;
		if (INACCTNO == null) {
			if (other.INACCTNO != null)
				return false;
		} else if (!INACCTNO.equals(other.INACCTNO))
			return false;
		if (INACQUIRE == null) {
			if (other.INACQUIRE != null)
				return false;
		} else if (!INACQUIRE.equals(other.INACQUIRE))
			return false;
		if (INBANKID == null) {
			if (other.INBANKID != null)
				return false;
		} else if (!INBANKID.equals(other.INBANKID))
			return false;
		if (INCLEARING == null) {
			if (other.INCLEARING != null)
				return false;
		} else if (!INCLEARING.equals(other.INCLEARING))
			return false;
		if (INFEE == null) {
			if (other.INFEE != null)
				return false;
		} else if (!INFEE.equals(other.INFEE))
			return false;
		if (INHEAD == null) {
			if (other.INHEAD != null)
				return false;
		} else if (!INHEAD.equals(other.INHEAD))
			return false;
		if (INID == null) {
			if (other.INID != null)
				return false;
		} else if (!INID.equals(other.INID))
			return false;
		if (MERCHANTID == null) {
			if (other.MERCHANTID != null)
				return false;
		} else if (!MERCHANTID.equals(other.MERCHANTID))
			return false;
		if (ORDERNO == null) {
			if (other.ORDERNO != null)
				return false;
		} else if (!ORDERNO.equals(other.ORDERNO))
			return false;
		if (OUTACCTNO == null) {
			if (other.OUTACCTNO != null)
				return false;
		} else if (!OUTACCTNO.equals(other.OUTACCTNO))
			return false;
		if (OUTACQUIRE == null) {
			if (other.OUTACQUIRE != null)
				return false;
		} else if (!OUTACQUIRE.equals(other.OUTACQUIRE))
			return false;
		if (OUTBANKID == null) {
			if (other.OUTBANKID != null)
				return false;
		} else if (!OUTBANKID.equals(other.OUTBANKID))
			return false;
		if (OUTCLEARING == null) {
			if (other.OUTCLEARING != null)
				return false;
		} else if (!OUTCLEARING.equals(other.OUTCLEARING))
			return false;
		if (OUTFEE == null) {
			if (other.OUTFEE != null)
				return false;
		} else if (!OUTFEE.equals(other.OUTFEE))
			return false;
		if (OUTHEAD == null) {
			if (other.OUTHEAD != null)
				return false;
		} else if (!OUTHEAD.equals(other.OUTHEAD))
			return false;
		if (OUTID == null) {
			if (other.OUTID != null)
				return false;
		} else if (!OUTID.equals(other.OUTID))
			return false;
		if (PCODE == null) {
			if (other.PCODE != null)
				return false;
		} else if (!PCODE.equals(other.PCODE))
			return false;
		if (PENDINGCODE == null) {
			if (other.PENDINGCODE != null)
				return false;
		} else if (!PENDINGCODE.equals(other.PENDINGCODE))
			return false;
		if (RC1 == null) {
			if (other.RC1 != null)
				return false;
		} else if (!RC1.equals(other.RC1))
			return false;
		if (RC2 == null) {
			if (other.RC2 != null)
				return false;
		} else if (!RC2.equals(other.RC2))
			return false;
		if (RC3 == null) {
			if (other.RC3 != null)
				return false;
		} else if (!RC3.equals(other.RC3))
			return false;
		if (RC4 == null) {
			if (other.RC4 != null)
				return false;
		} else if (!RC4.equals(other.RC4))
			return false;
		if (RC5 == null) {
			if (other.RC5 != null)
				return false;
		} else if (!RC5.equals(other.RC5))
			return false;
		if (RC6 == null) {
			if (other.RC6 != null)
				return false;
		} else if (!RC6.equals(other.RC6))
			return false;
		if (RECEIVERBANK == null) {
			if (other.RECEIVERBANK != null)
				return false;
		} else if (!RECEIVERBANK.equals(other.RECEIVERBANK))
			return false;
		if (RECEIVERID == null) {
			if (other.RECEIVERID != null)
				return false;
		} else if (!RECEIVERID.equals(other.RECEIVERID))
			return false;
		if (RECEIVERSTATUS == null) {
			if (other.RECEIVERSTATUS != null)
				return false;
		} else if (!RECEIVERSTATUS.equals(other.RECEIVERSTATUS))
			return false;
		if (REFUNDDEADLINE == null) {
			if (other.REFUNDDEADLINE != null)
				return false;
		} else if (!REFUNDDEADLINE.equals(other.REFUNDDEADLINE))
			return false;
		if (RESULTSTATUS == null) {
			if (other.RESULTSTATUS != null)
				return false;
		} else if (!RESULTSTATUS.equals(other.RESULTSTATUS))
			return false;
		if (RRN == null) {
			if (other.RRN != null)
				return false;
		} else if (!RRN.equals(other.RRN))
			return false;
		if (SENDERACQUIRE == null) {
			if (other.SENDERACQUIRE != null)
				return false;
		} else if (!SENDERACQUIRE.equals(other.SENDERACQUIRE))
			return false;
		if (SENDERBANK == null) {
			if (other.SENDERBANK != null)
				return false;
		} else if (!SENDERBANK.equals(other.SENDERBANK))
			return false;
		if (SENDERBANKID == null) {
			if (other.SENDERBANKID != null)
				return false;
		} else if (!SENDERBANKID.equals(other.SENDERBANKID))
			return false;
		if (SENDERCLEARING == null) {
			if (other.SENDERCLEARING != null)
				return false;
		} else if (!SENDERCLEARING.equals(other.SENDERCLEARING))
			return false;
		if (SENDERFEE == null) {
			if (other.SENDERFEE != null)
				return false;
		} else if (!SENDERFEE.equals(other.SENDERFEE))
			return false;
		if (SENDERHEAD == null) {
			if (other.SENDERHEAD != null)
				return false;
		} else if (!SENDERHEAD.equals(other.SENDERHEAD))
			return false;
		if (SENDERID == null) {
			if (other.SENDERID != null)
				return false;
		} else if (!SENDERID.equals(other.SENDERID))
			return false;
		if (SENDERSTATUS == null) {
			if (other.SENDERSTATUS != null)
				return false;
		} else if (!SENDERSTATUS.equals(other.SENDERSTATUS))
			return false;
		if (STAN == null) {
			if (other.STAN != null)
				return false;
		} else if (!STAN.equals(other.STAN))
			return false;
		if (TIMEOUTCODE == null) {
			if (other.TIMEOUTCODE != null)
				return false;
		} else if (!TIMEOUTCODE.equals(other.TIMEOUTCODE))
			return false;
		if (TRMLCHECK == null) {
			if (other.TRMLCHECK != null)
				return false;
		} else if (!TRMLCHECK.equals(other.TRMLCHECK))
			return false;
		if (TRMLID == null) {
			if (other.TRMLID != null)
				return false;
		} else if (!TRMLID.equals(other.TRMLID))
			return false;
		if (TRMLMCC == null) {
			if (other.TRMLMCC != null)
				return false;
		} else if (!TRMLMCC.equals(other.TRMLMCC))
			return false;
		if (TXAMT == null) {
			if (other.TXAMT != null)
				return false;
		} else if (!TXAMT.equals(other.TXAMT))
			return false;
		if (TXDATE == null) {
			if (other.TXDATE != null)
				return false;
		} else if (!TXDATE.equals(other.TXDATE))
			return false;
		if (TXDT == null) {
			if (other.TXDT != null)
				return false;
		} else if (!TXDT.equals(other.TXDT))
			return false;
		if (TXID == null) {
			if (other.TXID != null)
				return false;
		} else if (!TXID.equals(other.TXID))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
