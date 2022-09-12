package tw.org.twntch.form;

import java.util.List;
import java.util.Map;

public class TxnLog_Form extends CommonForm {

	private List txn_IdList ;
	private List company_IdList ;
	private List snd_company_IdList ;
	private Map detailData ;
	private String  AGENT_COMPANY_NAME ;
	private String  opAction1 ;
	private String  SBIZDATE ;
	private String  EBIZDATE ;
	private String  AGENT_COMPANY_ID ;
	private String  SND_COMPANY_ID ;
	private String  TXN_ID ;
	private String  ACCOUNTNUM ;
	private String  TXAMT ;
	private String  RESULTSTATUS ;
	private String  GARBAGEDATA ;
	
	private	String 	BILLDATA	;
	private	String 	BILLTYPE	;
	private	String 	BIZDATE	;
	private	String 	CHECKTYPE	;
	private	String 	CLEARINGPHASE	;
	private	String 	HANDLECHARGE	;
	private	String 	HANDLEDATETIME	;
	private	String 	INACCOUNTNUM	;
	private	String 	INBANKID	;
	private	String 	INNATIONALID	;
	private	String 	ISSUERID	;
	private	String 	ISSUERREMARK	;
	private	String 	MERCHANTID	;
	private	String 	MESSAGE	;
	private	String 	MSGCODE	;
	private	String 	MSGTYPE	;
	private	String 	NOTE	;
	private	String 	ORDERNUM	;
	private	String 	ORGBILLDATA	;
	private	String 	ORGMERCHANTID	;
	private	String 	ORGORDERNUM	;
	private	String 	ORGTERMINERID	;
	private	String 	ORGTRANSACTIONAMOUNT	;
	private	String 	ORGTRANSACTIONDATE	;
	private	String 	OUTACCOUNTNUM	;
	private	String 	OUTBANKID	;
	private	String 	OUTNATIONALID	;
	private	String 	PCODE	;
	private	String 	PROCESSCODE	;
	private	String 	QRYSEQUENCENUM	;
	private	String 	QRYTRANSACTIONDATE	;
	private	String 	RECEIVERBANK	;
	private	String 	RECEIVERID	;
	private	String 	RESPONSECODE	;
	private	String 	SENDERBANK	;
	private	String 	SEQ	;
	private	String 	STAN	;
	private	String 	STATUS	;
	private	String 	TERMINERID	;
	private	String 	TRANSACTIONAMOUNT	;
	private	String 	TRANSACTIONDATETIME	;
	private	String 	TXDATE	;
	private	String 	TXID	;
	private	String 	TXNCODE	;
	private	String 	USERNUM	;
	private	String 	VERIFIEDDATA	;
	private	String 	VERIFYDATA	;
	private	String 	TOLLID	;
	private	String 	BILLFLAG	;
	private	String 	PFCLASS	;
	private	String 	CHARGETYPE	;
	private	String 	CHECKDATA	;
	private String  STXDATE ;
	private String  ETXDATE ;
	//繳費類別清單
	private List pfclassList;
	
	private String  date_type_checked ;
	
	
	public String getDate_type_checked() {
		return date_type_checked;
	}
	public void setDate_type_checked(String date_type_checked) {
		this.date_type_checked = date_type_checked;
	}
	public String getSBIZDATE() {
		return SBIZDATE;
	}
	public void setSBIZDATE(String sBIZDATE) {
		SBIZDATE = sBIZDATE;
	}
	public String getEBIZDATE() {
		return EBIZDATE;
	}
	public void setEBIZDATE(String eBIZDATE) {
		EBIZDATE = eBIZDATE;
	}
	public String getBILLDATA() {
		return BILLDATA;
	}
	public void setBILLDATA(String bILLDATA) {
		BILLDATA = bILLDATA;
	}
	public String getBILLTYPE() {
		return BILLTYPE;
	}
	public void setBILLTYPE(String bILLTYPE) {
		BILLTYPE = bILLTYPE;
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
	public String getCLEARINGPHASE() {
		return CLEARINGPHASE;
	}
	public void setCLEARINGPHASE(String cLEARINGPHASE) {
		CLEARINGPHASE = cLEARINGPHASE;
	}
	public String getHANDLECHARGE() {
		return HANDLECHARGE;
	}
	public void setHANDLECHARGE(String hANDLECHARGE) {
		HANDLECHARGE = hANDLECHARGE;
	}
	public String getHANDLEDATETIME() {
		return HANDLEDATETIME;
	}
	public void setHANDLEDATETIME(String hANDLEDATETIME) {
		HANDLEDATETIME = hANDLEDATETIME;
	}
	public String getINACCOUNTNUM() {
		return INACCOUNTNUM;
	}
	public void setINACCOUNTNUM(String iNACCOUNTNUM) {
		INACCOUNTNUM = iNACCOUNTNUM;
	}
	public String getINBANKID() {
		return INBANKID;
	}
	public void setINBANKID(String iNBANKID) {
		INBANKID = iNBANKID;
	}
	public String getINNATIONALID() {
		return INNATIONALID;
	}
	public void setINNATIONALID(String iNNATIONALID) {
		INNATIONALID = iNNATIONALID;
	}
	public String getISSUERID() {
		return ISSUERID;
	}
	public void setISSUERID(String iSSUERID) {
		ISSUERID = iSSUERID;
	}
	public String getISSUERREMARK() {
		return ISSUERREMARK;
	}
	public void setISSUERREMARK(String iSSUERREMARK) {
		ISSUERREMARK = iSSUERREMARK;
	}
	public String getMERCHANTID() {
		return MERCHANTID;
	}
	public void setMERCHANTID(String mERCHANTID) {
		MERCHANTID = mERCHANTID;
	}
	public String getMESSAGE() {
		return MESSAGE;
	}
	public void setMESSAGE(String mESSAGE) {
		MESSAGE = mESSAGE;
	}
	public String getMSGCODE() {
		return MSGCODE;
	}
	public void setMSGCODE(String mSGCODE) {
		MSGCODE = mSGCODE;
	}
	public String getMSGTYPE() {
		return MSGTYPE;
	}
	public void setMSGTYPE(String mSGTYPE) {
		MSGTYPE = mSGTYPE;
	}
	public String getNOTE() {
		return NOTE;
	}
	public void setNOTE(String nOTE) {
		NOTE = nOTE;
	}
	public String getORDERNUM() {
		return ORDERNUM;
	}
	public void setORDERNUM(String oRDERNUM) {
		ORDERNUM = oRDERNUM;
	}
	public String getORGBILLDATA() {
		return ORGBILLDATA;
	}
	public void setORGBILLDATA(String oRGBILLDATA) {
		ORGBILLDATA = oRGBILLDATA;
	}
	public String getORGMERCHANTID() {
		return ORGMERCHANTID;
	}
	public void setORGMERCHANTID(String oRGMERCHANTID) {
		ORGMERCHANTID = oRGMERCHANTID;
	}
	public String getORGORDERNUM() {
		return ORGORDERNUM;
	}
	public void setORGORDERNUM(String oRGORDERNUM) {
		ORGORDERNUM = oRGORDERNUM;
	}
	public String getORGTERMINERID() {
		return ORGTERMINERID;
	}
	public void setORGTERMINERID(String oRGTERMINERID) {
		ORGTERMINERID = oRGTERMINERID;
	}
	public String getORGTRANSACTIONAMOUNT() {
		return ORGTRANSACTIONAMOUNT;
	}
	public void setORGTRANSACTIONAMOUNT(String oRGTRANSACTIONAMOUNT) {
		ORGTRANSACTIONAMOUNT = oRGTRANSACTIONAMOUNT;
	}
	public String getORGTRANSACTIONDATE() {
		return ORGTRANSACTIONDATE;
	}
	public void setORGTRANSACTIONDATE(String oRGTRANSACTIONDATE) {
		ORGTRANSACTIONDATE = oRGTRANSACTIONDATE;
	}
	public String getOUTACCOUNTNUM() {
		return OUTACCOUNTNUM;
	}
	public void setOUTACCOUNTNUM(String oUTACCOUNTNUM) {
		OUTACCOUNTNUM = oUTACCOUNTNUM;
	}
	public String getOUTBANKID() {
		return OUTBANKID;
	}
	public void setOUTBANKID(String oUTBANKID) {
		OUTBANKID = oUTBANKID;
	}
	public String getOUTNATIONALID() {
		return OUTNATIONALID;
	}
	public void setOUTNATIONALID(String oUTNATIONALID) {
		OUTNATIONALID = oUTNATIONALID;
	}
	public String getPCODE() {
		return PCODE;
	}
	public void setPCODE(String pCODE) {
		PCODE = pCODE;
	}
	public String getPROCESSCODE() {
		return PROCESSCODE;
	}
	public void setPROCESSCODE(String pROCESSCODE) {
		PROCESSCODE = pROCESSCODE;
	}
	public String getQRYSEQUENCENUM() {
		return QRYSEQUENCENUM;
	}
	public void setQRYSEQUENCENUM(String qRYSEQUENCENUM) {
		QRYSEQUENCENUM = qRYSEQUENCENUM;
	}
	public String getQRYTRANSACTIONDATE() {
		return QRYTRANSACTIONDATE;
	}
	public void setQRYTRANSACTIONDATE(String qRYTRANSACTIONDATE) {
		QRYTRANSACTIONDATE = qRYTRANSACTIONDATE;
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
	public String getRESPONSECODE() {
		return RESPONSECODE;
	}
	public void setRESPONSECODE(String rESPONSECODE) {
		RESPONSECODE = rESPONSECODE;
	}
	public String getSENDERBANK() {
		return SENDERBANK;
	}
	public void setSENDERBANK(String sENDERBANK) {
		SENDERBANK = sENDERBANK;
	}
	public String getSEQ() {
		return SEQ;
	}
	public void setSEQ(String sEQ) {
		SEQ = sEQ;
	}
	public String getSTAN() {
		return STAN;
	}
	public void setSTAN(String sTAN) {
		STAN = sTAN;
	}
	public String getSTATUS() {
		return STATUS;
	}
	public void setSTATUS(String sTATUS) {
		STATUS = sTATUS;
	}
	public String getTERMINERID() {
		return TERMINERID;
	}
	public void setTERMINERID(String tERMINERID) {
		TERMINERID = tERMINERID;
	}
	public String getTRANSACTIONAMOUNT() {
		return TRANSACTIONAMOUNT;
	}
	public void setTRANSACTIONAMOUNT(String tRANSACTIONAMOUNT) {
		TRANSACTIONAMOUNT = tRANSACTIONAMOUNT;
	}
	public String getTRANSACTIONDATETIME() {
		return TRANSACTIONDATETIME;
	}
	public void setTRANSACTIONDATETIME(String tRANSACTIONDATETIME) {
		TRANSACTIONDATETIME = tRANSACTIONDATETIME;
	}
	public String getTXDATE() {
		return TXDATE;
	}
	public void setTXDATE(String tXDATE) {
		TXDATE = tXDATE;
	}
	public String getTXID() {
		return TXID;
	}
	public void setTXID(String tXID) {
		TXID = tXID;
	}
	public String getTXNCODE() {
		return TXNCODE;
	}
	public void setTXNCODE(String tXNCODE) {
		TXNCODE = tXNCODE;
	}
	public String getUSERNUM() {
		return USERNUM;
	}
	public void setUSERNUM(String uSERNUM) {
		USERNUM = uSERNUM;
	}
	public String getVERIFIEDDATA() {
		return VERIFIEDDATA;
	}
	public void setVERIFIEDDATA(String vERIFIEDDATA) {
		VERIFIEDDATA = vERIFIEDDATA;
	}
	public String getVERIFYDATA() {
		return VERIFYDATA;
	}
	public void setVERIFYDATA(String vERIFYDATA) {
		VERIFYDATA = vERIFYDATA;
	}
	public List getTxn_IdList() {
		return txn_IdList;
	}
	public void setTxn_IdList(List txn_IdList) {
		this.txn_IdList = txn_IdList;
	}
	public String getAGENT_COMPANY_ID() {
		return AGENT_COMPANY_ID;
	}
	public void setAGENT_COMPANY_ID(String aGENT_COMPANY_ID) {
		AGENT_COMPANY_ID = aGENT_COMPANY_ID;
	}
	public String getSND_COMPANY_ID() {
		return SND_COMPANY_ID;
	}
	public void setSND_COMPANY_ID(String sND_COMPANY_ID) {
		SND_COMPANY_ID = sND_COMPANY_ID;
	}
	public String getOpAction1() {
		return opAction1;
	}
	public void setOpAction1(String opAction1) {
		this.opAction1 = opAction1;
	}
	public String getTXN_ID() {
		return TXN_ID;
	}
	public void setTXN_ID(String tXN_ID) {
		TXN_ID = tXN_ID;
	}
	public String getACCOUNTNUM() {
		return ACCOUNTNUM;
	}
	public void setACCOUNTNUM(String aCCOUNTNUM) {
		ACCOUNTNUM = aCCOUNTNUM;
	}
	public String getTXAMT() {
		return TXAMT;
	}
	public void setTXAMT(String tXAMT) {
		TXAMT = tXAMT;
	}
	public String getRESULTSTATUS() {
		return RESULTSTATUS;
	}
	public void setRESULTSTATUS(String rESULTSTATUS) {
		RESULTSTATUS = rESULTSTATUS;
	}
	public String getGARBAGEDATA() {
		return GARBAGEDATA;
	}
	public void setGARBAGEDATA(String gARBAGEDATA) {
		GARBAGEDATA = gARBAGEDATA;
	}
	public List getCompany_IdList() {
		return company_IdList;
	}
	public void setCompany_IdList(List company_IdList) {
		this.company_IdList = company_IdList;
	}
	public List getSnd_company_IdList() {
		return snd_company_IdList;
	}
	public void setSnd_company_IdList(List snd_company_IdList) {
		this.snd_company_IdList = snd_company_IdList;
	}
	public Map getDetailData() {
		return detailData;
	}
	public void setDetailData(Map detailData) {
		this.detailData = detailData;
	}
	public String getAGENT_COMPANY_NAME() {
		return AGENT_COMPANY_NAME;
	}
	public void setAGENT_COMPANY_NAME(String aGENT_COMPANY_NAME) {
		AGENT_COMPANY_NAME = aGENT_COMPANY_NAME;
	}
	public String getTOLLID() {
		return TOLLID;
	}
	public void setTOLLID(String tOLLID) {
		TOLLID = tOLLID;
	}
	public String getBILLFLAG() {
		return BILLFLAG;
	}
	public void setBILLFLAG(String bILLFLAG) {
		BILLFLAG = bILLFLAG;
	}
	public String getPFCLASS() {
		return PFCLASS;
	}
	public void setPFCLASS(String pFCLASS) {
		PFCLASS = pFCLASS;
	}
	public String getCHARGETYPE() {
		return CHARGETYPE;
	}
	public void setCHARGETYPE(String cHARGETYPE) {
		CHARGETYPE = cHARGETYPE;
	}
	public List getPfclassList() {
		return pfclassList;
	}
	public void setPfclassList(List pfclassList) {
		this.pfclassList = pfclassList;
	}
	public String getCHECKDATA() {
		return CHECKDATA;
	}
	public void setCHECKDATA(String cHECKDATA) {
		CHECKDATA = cHECKDATA;
	}
	public String getSTXDATE() {
		return STXDATE;
	}
	public void setSTXDATE(String sTXDATE) {
		STXDATE = sTXDATE;
	}
	public String getETXDATE() {
		return ETXDATE;
	}
	public void setETXDATE(String eTXDATE) {
		ETXDATE = eTXDATE;
	}
	
	
	
	
}
