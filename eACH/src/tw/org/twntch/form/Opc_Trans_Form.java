package tw.org.twntch.form;

import java.util.List;
import java.util.Map;

public class Opc_Trans_Form extends CommonForm{

	private static final long serialVersionUID = 6013207111136183658L;
	private String TXDATE = "";	//交易日期
	private String STAN = "";	//交易序號
	private String TXTIME_START = "";	//交易起時間
	private String TXTIME_END = "";	//交易訖時間
	
	//private String STAN_TYPE = "";	//發動端(交換所/銀行)
	private String SENDER_TYPE;
	private String RECEIVER_TYPE;
	private String SENDERBANK;
	private String RECEIVERBANK;
	
	private String OPBK_ID = "";	//銀行(全部/單一)
	private String PCODE = "";	//交易代號(全部/單一)
	private String pageType = "";	//決定明細顯示的欄位
	private	List opbkIdList;
	private List pcodeList;
	private List detailData;
	private List txnErrorCodeList;	//回應代碼清單
	private String pageForSort;
//	SETTLEMENT明細要用的欄位
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
	private String NETFEEAMT;
	private String PAYAMT;
	private String PAYCNT;
	private String PAYFEEAMT;
	private String PENDING;
	private String RECVAMT;
	private String RECVCNT;
	private String RECVFEEAMT;
	private String RSPCODE;
	private String RSPRESULTCODE;
	private String RVSPAYFEEAMT;
	private String RVSRECVFEEAMT;
	private String STATUS;
	private String TIMEOUTCODE;
	private String TOTALCNT;
	private String TXDT;
	
	
	private String OPCTXDATE;
	private String OPCCUR_TXDATE;
	private String OPCTXDATE_PRE;
	
	public String getTXDATE() {
		return TXDATE;
	}
	public void setTXDATE(String tXDATE) {
		TXDATE = tXDATE;
	}
	public String getSTAN() {
		return STAN;
	}
	public void setSTAN(String sTAN) {
		STAN = sTAN;
	}
	public String getTXTIME_START() {
		return TXTIME_START;
	}
	public void setTXTIME_START(String tXTIME_START) {
		TXTIME_START = tXTIME_START;
	}
	public String getTXTIME_END() {
		return TXTIME_END;
	}
	public void setTXTIME_END(String tXTIME_END) {
		TXTIME_END = tXTIME_END;
	}
	public String getSENDER_TYPE() {
		return SENDER_TYPE;
	}
	public void setSENDER_TYPE(String sENDER_TYPE) {
		SENDER_TYPE = sENDER_TYPE;
	}
	public String getRECEIVER_TYPE() {
		return RECEIVER_TYPE;
	}
	public void setRECEIVER_TYPE(String rECEIVER_TYPE) {
		RECEIVER_TYPE = rECEIVER_TYPE;
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
	public String getOPBK_ID() {
		return OPBK_ID;
	}
	public void setOPBK_ID(String oPBK_ID) {
		OPBK_ID = oPBK_ID;
	}
	public String getPCODE() {
		return PCODE;
	}
	public void setPCODE(String pCODE) {
		PCODE = pCODE;
	}
	public String getPageType() {
		return pageType;
	}
	public void setPageType(String pageType) {
		this.pageType = pageType;
	}
	public List getOpbkIdList() {
		return opbkIdList;
	}
	public void setOpbkIdList(List opbkIdList) {
		this.opbkIdList = opbkIdList;
	}
	public List getPcodeList() {
		return pcodeList;
	}
	public void setPcodeList(List pcodeList) {
		this.pcodeList = pcodeList;
	}
	public List getDetailData() {
		return detailData;
	}
	public void setDetailData(List detailData) {
		this.detailData = detailData;
	}
	public List getTxnErrorCodeList() {
		return txnErrorCodeList;
	}
	public void setTxnErrorCodeList(List txnErrorCodeList) {
		this.txnErrorCodeList = txnErrorCodeList;
	}
	public String getPageForSort() {
		return pageForSort;
	}
	public void setPageForSort(String pageForSort) {
		this.pageForSort = pageForSort;
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
	public String getNETFEEAMT() {
		return NETFEEAMT;
	}
	public void setNETFEEAMT(String nETFEEAMT) {
		NETFEEAMT = nETFEEAMT;
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
	public String getPENDING() {
		return PENDING;
	}
	public void setPENDING(String pENDING) {
		PENDING = pENDING;
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
	public String getRVSPAYFEEAMT() {
		return RVSPAYFEEAMT;
	}
	public void setRVSPAYFEEAMT(String rVSPAYFEEAMT) {
		RVSPAYFEEAMT = rVSPAYFEEAMT;
	}
	public String getRVSRECVFEEAMT() {
		return RVSRECVFEEAMT;
	}
	public void setRVSRECVFEEAMT(String rVSRECVFEEAMT) {
		RVSRECVFEEAMT = rVSRECVFEEAMT;
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
	public String getOPCTXDATE() {
		return OPCTXDATE;
	}
	public void setOPCTXDATE(String oPCTXDATE) {
		OPCTXDATE = oPCTXDATE;
	}
	public String getOPCCUR_TXDATE() {
		return OPCCUR_TXDATE;
	}
	public void setOPCCUR_TXDATE(String oPCCUR_TXDATE) {
		OPCCUR_TXDATE = oPCCUR_TXDATE;
	}
	public String getOPCTXDATE_PRE() {
		return OPCTXDATE_PRE;
	}
	public void setOPCTXDATE_PRE(String oPCTXDATE_PRE) {
		OPCTXDATE_PRE = oPCTXDATE_PRE;
	}
	
}
