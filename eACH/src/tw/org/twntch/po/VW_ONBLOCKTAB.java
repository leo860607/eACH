package tw.org.twntch.po;

import groovy.beans.Vetoable;

import java.io.Serializable;
import java.math.BigInteger;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
@Entity(name="tw.org.twntch.po.VW_ONBLOCKTAB")
@Table(name = "VW_ONBLOCKTAB")
public class VW_ONBLOCKTAB implements Serializable{
	
	private String ACCTBAL;
	private String ACCTCODE;
	private String AVAILBAL;
	private String BANKRSPMSG;
	@Id
	private String BIZDATE;
	private String CHECKTYPE;
	private String CLEARINGCODE;
	private String CLEARINGPHASE;
	private String CONMEMO;
	private String CONRESULTCODE;
	private String DT_CON_1;
	private String DT_CON_2;
	private String DT_CON_3;
	private String DT_REQ_1;
	private String DT_REQ_2;
	private String DT_REQ_3;
	private String DT_RSP_1;
	private String DT_RSP_2;
	private String DT_RSP_3;
	private String EACHDT;
	private String EACHFEE;
	private String FEE;
	private String INACCTNO;
	private String INACQUIRE;
	private String INBANKID;
	private String INCLEARING;
	private String INFEE;
	private String INHEAD;
	private String INID;
	private String MERCHANTID;
	private String NEWEACHFEE;
	private String NEWFEE;
	private String NEWINFEE;
	private String NEWOUTFEE;
	private String NEWRESULT;
	private String NEWSENDERFEE;
	private String NEWTXAMT;
	private String NEWTXDT;
	private String ORDERNO;
	private String OUTACCTNO;
	private String OUTACQUIRE;
	private String OUTBANKID;
	private String OUTCLEARING;
	private String OUTFEE;
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
	private String SENDERFEE;
	private String SENDERHEAD;
	private String SENDERID;
	private String SENDERSTATUS;
	private String STAN;
	private String TIMEOUTCODE;
	private String TRMLCHECK;
	private String TRMLID;
	private String TRMLMCC;
	private String TXAMT;
	private String TXDATE;
	private String TXDT;
	private String TXID;
	private String UPDATEDT;
	private String FLBATCHSEQ;
	//查詢角度銷帳行20170704
	private String WOACQUIRE;
	private String WOHEAD;
	private String WOBANKID;
	private String WOFEE;
	private String NEWWOFEE;
	//查詢條件
	private String PFCLASS;
	private String TOLLID;
	private String CHARGETYPE;
	private String BILLFLAG;
	
	//2022/01/20新增欄位給頁面顯示
	private String EXTENDFEE;
//	//新增欄位
//	private String FEE_TYPE;
//	private String NEWEACHFEE_NW;
//	private String NEWFEE_NW;
//	private String NEWINFEE_NW;
//	private String NEWOUTFEE_NW;
//	private String NEWSENDERFEE_NW;
//	private String HANDLECHARGE_NW;
//	private String FEE_LVL_TYPE;
//	private String SND_BANK_FEE_DISC;
//	private String IN_BANK_FEE_DISC;
//	private String OUT_BANK_FEE_DISC;
//	private String WO_BANK_FEE_DISC;
	
	
	@Transient
	private String RESP;
	@Transient 
	private String NUM;	//for Count Query
	@Transient 
	private String ROWNUMBER;	//
	@Transient 
	private String FLBIZDATE; 
	

	@Transient 
	private String BKHEADNAME; 
	@Transient 
	private String TIMEOUTCNT; 
	
	
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
	public String getCONMEMO() {
		return CONMEMO;
	}
	public void setCONMEMO(String cONMEMO) {
		CONMEMO = cONMEMO;
	}
	public String getCONRESULTCODE() {
		return CONRESULTCODE;
	}
	public void setCONRESULTCODE(String cONRESULTCODE) {
		CONRESULTCODE = cONRESULTCODE;
	}
	public String getDT_CON_1() {
		return DT_CON_1;
	}
	public void setDT_CON_1(String dT_CON_1) {
		DT_CON_1 = dT_CON_1;
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
	public String getEACHDT() {
		return EACHDT;
	}
	public void setEACHDT(String eACHDT) {
		EACHDT = eACHDT;
	}
	public String getEACHFEE() {
		return EACHFEE;
	}
	public void setEACHFEE(String eACHFEE) {
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
	public String getINFEE() {
		return INFEE;
	}
	public void setINFEE(String iNFEE) {
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
	public String getNEWEACHFEE() {
		return NEWEACHFEE;
	}
	public void setNEWEACHFEE(String nEWEACHFEE) {
		NEWEACHFEE = nEWEACHFEE;
	}
	public String getNEWFEE() {
		return NEWFEE;
	}
	public void setNEWFEE(String nEWFEE) {
		NEWFEE = nEWFEE;
	}
	public String getNEWINFEE() {
		return NEWINFEE;
	}
	public void setNEWINFEE(String nEWINFEE) {
		NEWINFEE = nEWINFEE;
	}
	public String getNEWOUTFEE() {
		return NEWOUTFEE;
	}
	public void setNEWOUTFEE(String nEWOUTFEE) {
		NEWOUTFEE = nEWOUTFEE;
	}
	public String getNEWRESULT() {
		return NEWRESULT;
	}
	public void setNEWRESULT(String nEWRESULT) {
		NEWRESULT = nEWRESULT;
	}
	public String getNEWSENDERFEE() {
		return NEWSENDERFEE;
	}
	public void setNEWSENDERFEE(String nEWSENDERFEE) {
		NEWSENDERFEE = nEWSENDERFEE;
	}
	public String getNEWTXAMT() {
		return NEWTXAMT;
	}
	public void setNEWTXAMT(String nEWTXAMT) {
		NEWTXAMT = nEWTXAMT;
	}
	public String getNEWTXDT() {
		return NEWTXDT;
	}
	public void setNEWTXDT(String nEWTXDT) {
		NEWTXDT = nEWTXDT;
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
	public String getOUTFEE() {
		return OUTFEE;
	}
	public void setOUTFEE(String oUTFEE) {
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
	public String getSENDERFEE() {
		return SENDERFEE;
	}
	public void setSENDERFEE(String sENDERFEE) {
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
	public String getSTAN() {
		return STAN;
	}
	public void setSTAN(String sTAN) {
		STAN = sTAN;
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
	public String getTXDATE() {
		return TXDATE;
	}
	public void setTXDATE(String tXDATE) {
		TXDATE = tXDATE;
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
	public String getUPDATEDT() {
		return UPDATEDT;
	}
	public void setUPDATEDT(String uPDATEDT) {
		UPDATEDT = uPDATEDT;
	}
	public String getFLBATCHSEQ() {
		return FLBATCHSEQ;
	}
	public void setFLBATCHSEQ(String fLBATCHSEQ) {
		FLBATCHSEQ = fLBATCHSEQ;
	}
	public String getWOACQUIRE() {
		return WOACQUIRE;
	}
	public void setWOACQUIRE(String wOACQUIRE) {
		WOACQUIRE = wOACQUIRE;
	}
	public String getWOHEAD() {
		return WOHEAD;
	}
	public void setWOHEAD(String wOHEAD) {
		WOHEAD = wOHEAD;
	}
	public String getWOBANKID() {
		return WOBANKID;
	}
	public void setWOBANKID(String wOBANKID) {
		WOBANKID = wOBANKID;
	}
	public String getWOFEE() {
		return WOFEE;
	}
	public void setWOFEE(String wOFEE) {
		WOFEE = wOFEE;
	}
	@Transient
	public String getRESP() {
		return RESP;
	}
	public void setRESP(String rESP) {
		RESP = rESP;
	}
	@Transient
	public String getNUM() {
		return NUM;
	}
	public void setNUM(String nUM) {
		NUM = nUM;
	}
	public String getPFCLASS() {
		return PFCLASS;
	}
	public void setPFCLASS(String pFCLASS) {
		PFCLASS = pFCLASS;
	}
	public String getTOLLID() {
		return TOLLID;
	}
	public void setTOLLID(String tOLLID) {
		TOLLID = tOLLID;
	}
	public String getCHARGETYPE() {
		return CHARGETYPE;
	}
	public void setCHARGETYPE(String cHARGETYPE) {
		CHARGETYPE = cHARGETYPE;
	}
	public String getBILLFLAG() {
		return BILLFLAG;
	}
	public void setBILLFLAG(String bILLFLAG) {
		BILLFLAG = bILLFLAG;
	}
	public String getNEWWOFEE() {
		return NEWWOFEE;
	}
	public void setNEWWOFEE(String nEWWOFEE) {
		NEWWOFEE = nEWWOFEE;
	}
	@Transient
	public String getFLBIZDATE() {
		return FLBIZDATE;
	}
	public void setFLBIZDATE(String fLBIZDATE) {
		FLBIZDATE = fLBIZDATE;
	}
	@Transient
	public String getROWNUMBER() {
		return ROWNUMBER;
	}
	public void setROWNUMBER(String rOWNUMBER) {
		ROWNUMBER = rOWNUMBER;
	}
	public String getBKHEADNAME() {
		return BKHEADNAME;
	}
	public void setBKHEADNAME(String bKHEADNAME) {
		BKHEADNAME = bKHEADNAME;
	}
	public String getTIMEOUTCNT() {
		return TIMEOUTCNT;
	}
	public void setTIMEOUTCNT(String tIMEOUTCNT) {
		TIMEOUTCNT = tIMEOUTCNT;
	}
	public String getEXTENDFEE() {
		return EXTENDFEE;
	}
	public void setEXTENDFEE(String eXTENDFEE) {
		EXTENDFEE = eXTENDFEE;
	}
}
