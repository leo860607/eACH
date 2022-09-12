package tw.org.twntch.po;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

public class ONBLOCKTABbean implements Serializable{
	
	@EmbeddedId
	private String TXDATE;
	private String STAN;
	private String PCODE;
	private String SENDERBANK;
	private String RECEIVERBANK;
	private String TXDT;
	private String SENDERSTATUS;
	private String RECEIVERSTATUS;
	private String TIMEOUTCODE;
	private String CONRESULTCODE;
	private String ACCTCODE;
	private String CLEARINGCODE;
	private String PENDINGCODE;
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
	private String REFUNDDEADLINE;
	private String SENDERID;
	private String RECEIVERID;
	private String TXID;
	private String TXAMT;
	private String FEE;
	private String SENDERBANKID;
	private String INBANKID;
	private String OUTBANKID;
	private String BIZDATE;
	private String EACHDT;
	private String CLEARINGPHASE;
	private String INACCTNO;
	private String OUTACCTNO;
	private String INID;
	private String OUTID;
	private String ACCTBAL;
	private String AVAILBAL;
	private String CHECKTYPE;
	private String MERCHANTID;
	private String ORDERNO;
	private String TRMLID;
	private String TRMLCHECK;
	private String TRMLMCC;
	private String BANKRSPMSG;
	private String RRN;
	private String RESULTSTATUS;
	private String RC1;
	private String RC2;
	private String RC3;
	private String RC4;
	private String RC5;
	private String RC6;
	private String DT_Req_1;
	private String DT_Req_2;
	private String DT_Req_3;
	private String DT_Rsp_1;
	private String DT_Rsp_2;
	private String DT_Rsp_3;
	private String DT_Con_1;
	private String DT_Con_2;
	private String DT_Con_3;
	private String Resp;
	private String OBIZDATE;
	private String OCLEARINGPHASE;
	private String RESULTCODE;
	private String EndTime;
	private String FIRECOUNT;
	private String AVGTIME;
	private String ACHAVGTIME;
	private String ACHSAVETIME;
	private String ACHDEBITTIME;
	private String INSAVETIME;
	private String OUTDEBITTIME;
	private String TCH_STD_ECHO_TIME;
	private String PARTY_STD_ECHO_TIME;
	private String TXN_STD_PROC_TIME;
	private String ACHFLAG;
	@Transient
	private String OUTACCT;
	@Transient
	private String INACCT;
	@Transient
	private String BANKID;
	@Transient
	private String BANKIDANDNAME;
	@Transient
	private Integer TOTALCOUNT;
	@Transient
	private Integer PENDCOUNT;
	@Transient
	private Integer PRCCOUNT;
	@Transient
	private BigDecimal PRCTIME;
	@Transient
	private Integer SAVECOUNT;
	@Transient
	private BigDecimal SAVETIME;
	@Transient
	private Integer DEBITCOUNT;
	@Transient
	private BigDecimal DEBITTIME;
	@Transient
	private Integer ACHPRCCOUNT;
	@Transient
	private BigDecimal ACHPRCTIME;
	
	public String getTXDATE() {
		return TXDATE;
	}
	public void setTXDATE(String txdate) {
		this.TXDATE = txdate;
	}
	public String getSTAN() {
		return STAN;
	}
	public void setSTAN(String stan) {
		this.STAN = stan;
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
	public String getTRMLMCC() {
		return TRMLMCC;
	}
	public void setTRMLMCC(String tRMLMCC) {
		TRMLMCC = tRMLMCC;
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
	public String getDT_Req_1() {
		return DT_Req_1;
	}
	public void setDT_Req_1(String dT_Req_1) {
		DT_Req_1 = dT_Req_1;
	}
	public String getDT_Req_2() {
		return DT_Req_2;
	}
	public void setDT_Req_2(String dT_Req_2) {
		DT_Req_2 = dT_Req_2;
	}
	public String getDT_Req_3() {
		return DT_Req_3;
	}
	public void setDT_Req_3(String dT_Req_3) {
		DT_Req_3 = dT_Req_3;
	}
	public String getDT_Rsp_1() {
		return DT_Rsp_1;
	}
	public void setDT_Rsp_1(String dT_Rsp_1) {
		DT_Rsp_1 = dT_Rsp_1;
	}
	public String getDT_Rsp_2() {
		return DT_Rsp_2;
	}
	public void setDT_Rsp_2(String dT_Rsp_2) {
		DT_Rsp_2 = dT_Rsp_2;
	}
	public String getDT_Rsp_3() {
		return DT_Rsp_3;
	}
	public void setDT_Rsp_3(String dT_Rsp_3) {
		DT_Rsp_3 = dT_Rsp_3;
	}
	public String getDT_Con_1() {
		return DT_Con_1;
	}
	public void setDT_Con_1(String dT_Con_1) {
		DT_Con_1 = dT_Con_1;
	}
	public String getDT_Con_2() {
		return DT_Con_2;
	}
	public void setDT_Con_2(String dT_Con_2) {
		DT_Con_2 = dT_Con_2;
	}
	public String getDT_Con_3() {
		return DT_Con_3;
	}
	public void setDT_Con_3(String dT_Con_3) {
		DT_Con_3 = dT_Con_3;
	}
	public String getRESP() {
		return Resp;
	}
	public void setRESP(String resp) {
		Resp = resp;
	}
	public String getOBIZDATE() {
		return OBIZDATE;
	}
	public void setOBIZDATE(String obizdate) {
		OBIZDATE = obizdate;
	}
	public String getOCLEARINGPHASE() {
		return OCLEARINGPHASE;
	}
	public void setOCLEARINGPHASE(String oclearingphase) {
		OCLEARINGPHASE = oclearingphase;
	}
	public String getRESULTCODE() {
		return RESULTCODE;
	}
	public void setRESULTCODE(String resultcode) {
		RESULTCODE = resultcode;
	}
	public String getEndTime() {
		return EndTime;
	}
	public void setEndTime(String endtime) {
		EndTime = endtime;
	}
	public String getFIRECOUNT() {
		return FIRECOUNT;
	}
	public void setFIRECOUNT(String fIRECOUNT) {
		FIRECOUNT = fIRECOUNT;
	}
	public String getAVGTIME() {
		return AVGTIME;
	}
	public void setAVGTIME(String aVGTIME) {
		AVGTIME = aVGTIME;
	}
	public String getACHAVGTIME() {
		return ACHAVGTIME;
	}
	public void setACHAVGTIME(String aCHAVGTIME) {
		ACHAVGTIME = aCHAVGTIME;
	}
	public String getACHSAVETIME() {
		return ACHSAVETIME;
	}
	public void setACHSAVETIME(String aCHSAVETIME) {
		ACHSAVETIME = aCHSAVETIME;
	}
	public String getACHDEBITTIME() {
		return ACHDEBITTIME;
	}
	public void setACHDEBITTIME(String aCHDEBITTIME) {
		ACHDEBITTIME = aCHDEBITTIME;
	}
	public String getINSAVETIME() {
		return INSAVETIME;
	}
	public void setINSAVETIME(String iNSAVETIME) {
		INSAVETIME = iNSAVETIME;
	}
	public String getOUTDEBITTIME() {
		return OUTDEBITTIME;
	}
	public void setOUTDEBITTIME(String oUTDEBITTIME) {
		OUTDEBITTIME = oUTDEBITTIME;
	}
	public String getTCH_STD_ECHO_TIME() {
		return this.TCH_STD_ECHO_TIME;
	}
	public void setTCH_STD_ECHO_TIME(String tch_std_echo_time) {
		this.TCH_STD_ECHO_TIME = tch_std_echo_time;
	}
	public String getPARTY_STD_ECHO_TIME() {
		return this.PARTY_STD_ECHO_TIME;
	}
	public void setPARTY_STD_ECHO_TIME(String party_std_echo_time) {
		this.PARTY_STD_ECHO_TIME = party_std_echo_time;
	}
	public String getTXN_STD_PROC_TIME() {
		return this.TXN_STD_PROC_TIME;
	}
	public void setTXN_STD_PROC_TIME(String txn_std_proc_time) {
		this.TXN_STD_PROC_TIME = txn_std_proc_time;
	}
	

	
	public String getACHFLAG() {
		return ACHFLAG;
	}
	public void setACHFLAG(String aCHFLAG) {
		ACHFLAG = aCHFLAG;
	}
	@Transient
	public String getOUTACCT() {
		return OUTACCT;
	}
	public void setOUTACCT(String oUTACCT) {
		OUTACCT = oUTACCT;
	}
	@Transient
	public String getINACCT() {
		return INACCT;
	}
	public void setINACCT(String iNACCT) {
		INACCT = iNACCT;
	}
	public String getBANKID() {
		return BANKID;
	}
	public void setBANKID(String bANKID) {
		BANKID = bANKID;
	}
	public String getBANKIDANDNAME() {
		return BANKIDANDNAME;
	}
	public void setBANKIDANDNAME(String bANKIDANDNAME) {
		BANKIDANDNAME = bANKIDANDNAME;
	}
	public Integer getTOTALCOUNT() {
		return TOTALCOUNT;
	}
	public void setTOTALCOUNT(Integer tOTALCOUNT) {
		TOTALCOUNT = tOTALCOUNT;
	}
	public Integer getPENDCOUNT() {
		return PENDCOUNT;
	}
	public void setPENDCOUNT(Integer pENDCOUNT) {
		PENDCOUNT = pENDCOUNT;
	}
	public Integer getPRCCOUNT() {
		return PRCCOUNT;
	}
	public void setPRCCOUNT(Integer pRCCOUNT) {
		PRCCOUNT = pRCCOUNT;
	}
	public BigDecimal getPRCTIME() {
		return PRCTIME;
	}
	public void setPRCTIME(BigDecimal pRCTIME) {
		PRCTIME = pRCTIME;
	}
	public Integer getSAVECOUNT() {
		return SAVECOUNT;
	}
	public void setSAVECOUNT(Integer sAVECOUNT) {
		SAVECOUNT = sAVECOUNT;
	}
	public BigDecimal getSAVETIME() {
		return SAVETIME;
	}
	public void setSAVETIME(BigDecimal sAVETIME) {
		SAVETIME = sAVETIME;
	}
	public Integer getDEBITCOUNT() {
		return DEBITCOUNT;
	}
	public void setDEBITCOUNT(Integer dEBITCOUNT) {
		DEBITCOUNT = dEBITCOUNT;
	}
	public BigDecimal getDEBITTIME() {
		return DEBITTIME;
	}
	public void setDEBITTIME(BigDecimal dEBITTIME) {
		DEBITTIME = dEBITTIME;
	}
	public Integer getACHPRCCOUNT() {
		return ACHPRCCOUNT;
	}
	public void setACHPRCCOUNT(Integer aCHPRCCOUNT) {
		ACHPRCCOUNT = aCHPRCCOUNT;
	}
	public BigDecimal getACHPRCTIME() {
		return ACHPRCTIME;
	}
	public void setACHPRCTIME(BigDecimal aCHPRCTIME) {
		ACHPRCTIME = aCHPRCTIME;
	}
	
}
