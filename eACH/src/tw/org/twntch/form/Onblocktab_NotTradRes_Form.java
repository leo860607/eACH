package tw.org.twntch.form;

import java.util.List;

public class Onblocktab_NotTradRes_Form extends CommonForm {
    /**
	 * 
	 */
//	private static final long serialVersionUID = -2026944867513970378L;
	private String SERNO;	
//	private String USER_COMPANY;//操作行代號
	private String OPBK_ID;//操作行代號
	private	String  BGBK_ID	;//總行代號
//	private	String	BRBK_ID ;//分行代號	
	private	String	BUSINESS_TYPE_ID;//業務類別代號
	private String CDNUMRAO ;
	private String CARDNUM_ID ;	
    private String USERID;    
    private String TXTIME;    
    private String FUNC_ID;
	private String TXDATE;
    private String STAN;//交易追踨序號
    private String OSTAN;//交易追踨序號
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
	private String SENDERFEE;
	private String INFEE;
	private String OUTFEE;
	private String EACHFEE;
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
	private String DT_REQ_1;
	private String DT_REQ_2;
	private String DT_REQ_3;
	private String DT_RSP_1;
	private String DT_RSP_2;
	private String DT_RSP_3;
	private String DT_CON_1;
	private String DT_CON_2;
	private String DT_CON_3;
	private List userIdList;
    private List userCompanyList;
    private List funcList;
    private List opbkIdList;
    private String FILTER_BAT ;
//    private List bgIdList	;//總行檔清單
//    private	 List txnIdList	;//交易代號檔取得交易類別(入帳、扣帳)
//    private	 List resList	;//交易結果
//    private	 List scaseary	;
//    private String	scaseJson	;
    private	List	bsIdKist	;
    private List searchResList;
    private String sourcePage;
    private String pageForSort;
    private String START_DATE;
    private String END_DATE;
    
    
    private String OLDBIZDATE;
    
    
    public String getSERNO() {
		return SERNO;
	}
	public void setSERNO(String sERNO) {
		SERNO = sERNO;
	}
//	public String getUSER_COMPANY() {
//		return USER_COMPANY;
//	}
//	public void setUSER_COMPANY(String uSER_COMPANY) {
//		USER_COMPANY = uSER_COMPANY;
//	}
	
	
	
	public String getBGBK_ID() {
		return BGBK_ID;
	}
	public String getOPBK_ID() {
		return OPBK_ID;
	}
	public void setOPBK_ID(String oPBK_ID) {
		OPBK_ID = oPBK_ID;
	}
	public void setBGBK_ID(String bGBK_ID) {
		BGBK_ID = bGBK_ID;
	}
//	public String getBRBK_ID() {
//		return BRBK_ID;
//	}
//	public void setBRBK_ID(String bRBK_ID) {
//		BRBK_ID = bRBK_ID;
//	}
	public String getBUSINESS_TYPE_ID() {
		return BUSINESS_TYPE_ID;
	}
	public void setBUSINESS_TYPE_ID(String bUSINESS_TYPE_ID) {
		BUSINESS_TYPE_ID = bUSINESS_TYPE_ID;
	}
	
	public String getCDNUMRAO() {
		return CDNUMRAO;
	}
	public void setCDNUMRAO(String cDNUMRAO) {
		CDNUMRAO = cDNUMRAO;
	}
	public String getCARDNUM_ID() {
		return CARDNUM_ID;
	}
	public void setCARDNUM_ID(String cARDNUM_ID) {
		CARDNUM_ID = cARDNUM_ID;
	}
	public String getUSERID() {
		return USERID;
	}
	public void setUSERID(String uSERID) {
		USERID = uSERID;
	}
	public String getTXTIME() {
		return TXTIME;
	}
	public void setTXTIME(String tXTIME) {
		TXTIME = tXTIME;
	}	
	public String getFUNC_ID() {
		return FUNC_ID;
	}
	public void setFUNC_ID(String fUNC_ID) {
		FUNC_ID = fUNC_ID;
	}
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
	public String getSENDERFEE() {
		return SENDERFEE;
	}
	public void setSENDERFEE(String sENDERFEE) {
		SENDERFEE = sENDERFEE;
	}
	public String getINFEE() {
		return INFEE;
	}
	public void setINFEE(String iNFEE) {
		INFEE = iNFEE;
	}
	public String getOUTFEE() {
		return OUTFEE;
	}
	public void setOUTFEE(String oUTFEE) {
		OUTFEE = oUTFEE;
	}
	public String getEACHFEE() {
		return EACHFEE;
	}
	public void setEACHFEE(String eACHFEE) {
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
	public List getUserIdList() {
		return userIdList;
	}
	public void setUserIdList(List userIdList) {
		this.userIdList = userIdList;
	}
	public List getUserCompanyList() {
		return userCompanyList;
	}
	public void setUserCompanyList(List userCompanyList) {
		this.userCompanyList = userCompanyList;
	}
	public List getFuncList() {
		return funcList;
	}
	public void setFuncList(List funcList) {
		this.funcList = funcList;
	}	
//	public List getBgIdList() {
//		return bgIdList;
//	}
//	public void setBgIdList(List bgIdList) {
//		this.bgIdList = bgIdList;
//	}
//	public List getScaseary() {
//		return scaseary;
//	}
//	public void setScaseary(List scaseary) {
//		this.scaseary = scaseary;
//	}
//	public String getScaseJson() {
//		return scaseJson;
//	}
//	public void setScaseJson(String scaseJson) {
//		this.scaseJson = scaseJson;
//	}
//	public List getTxnIdList() {
//		return txnIdList;
//	}
//	public void setTxnIdList(List txnIdList) {
//		this.txnIdList = txnIdList;
//	}
	public List getBsIdKist() {
		return bsIdKist;
	}
	public void setBsIdKist(List bsIdKist) {
		this.bsIdKist = bsIdKist;
	}
//	public List getResList() {
//		return resList;
//	}
//	public void setResList(List resList) {
//		this.resList = resList;
//	}
	public List getSearchResList() {
		return searchResList;
	}
	public void setSearchResList(List searchResList) {
		this.searchResList = searchResList;
	}
	public List getOpbkIdList() {
		return opbkIdList;
	}
	public void setOpbkIdList(List opbkIdList) {
		this.opbkIdList = opbkIdList;
	}
	public String getSourcePage() {
		return sourcePage;
	}
	public void setSourcePage(String sourcePage) {
		this.sourcePage = sourcePage;
	}
	public String getPageForSort() {
		return pageForSort;
	}
	public void setPageForSort(String pageForSort) {
		this.pageForSort = pageForSort;
	}
	public String getSTART_DATE() {
		return START_DATE;
	}
	public void setSTART_DATE(String sTART_DATE) {
		START_DATE = sTART_DATE;
	}
	public String getEND_DATE() {
		return END_DATE;
	}
	public void setEND_DATE(String eND_DATE) {
		END_DATE = eND_DATE;
	}
	public String getFILTER_BAT() {
		return FILTER_BAT;
	}
	public void setFILTER_BAT(String fILTER_BAT) {
		FILTER_BAT = fILTER_BAT;
	}
	public String getOSTAN() {
		return OSTAN;
	}
	public void setOSTAN(String oSTAN) {
		OSTAN = oSTAN;
	}
	public String getOLDBIZDATE() {
		return OLDBIZDATE;
	}
	public void setOLDBIZDATE(String oLDBIZDATE) {
		OLDBIZDATE = oLDBIZDATE;
	}
}
