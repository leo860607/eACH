package tw.org.twntch.form;

import java.util.List;

import tw.org.twntch.form.CommonForm;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.StrUtils;

public class Proxy_Cl_Form extends CommonForm{
private List proxy_cl_bankList;
	
	private String CTBK_ID;
	private String BIZDATE;
	private String CLEARINGPHASE;
	private String dow_token ; //檔案下載判別用
	private String BGBK_ATTR;
	
	private String TXDATE;
	private String STAN;
	private String SENDERACQUIRE;
	private String SENDERBANKID;
	private String INACQUIRE;
	private String TRANSTYPE;
	private String RESULTSTATUS;
	private String RESULTCODE;
	private String CONRESULTCODE;
	private String ERR_DESC;
	private String TXN_ERR_DESC;
	private String GL_ERR_DESC;
	private String OTXDATE;
	private String OSTAN;
	private String ERROR_ID;
	private String TOTALCOUNT;
	private String SYSERRSELF;
	private String SYSERROUT;
	private String TXNERRSELF;
	private String TXNERROUT;
	private String OTHERRSELF;
	private String OTHERROUT;
	private String FAILCOUNT;
	private String CTBK_NAME;
	public List getProxy_cl_bankList() {
		return proxy_cl_bankList;
	}
	public void setProxy_cl_bankList(List proxy_cl_bankList) {
		this.proxy_cl_bankList = proxy_cl_bankList;
	}
	public String getCTBK_ID() {
		return CTBK_ID;
	}
	public void setCTBK_ID(String cTBK_ID) {
		CTBK_ID = cTBK_ID;
	}
	public String getBIZDATE() {
		return BIZDATE;
	}
	public void setBIZDATE(String bIZDATE) {
		BIZDATE = bIZDATE;
	}
	public String getCLEARINGPHASE() {
		return CLEARINGPHASE;
	}
	public void setCLEARINGPHASE(String cLEARINGPHASE) {
		CLEARINGPHASE = cLEARINGPHASE;
	}
	public String getDow_token() {
		return dow_token;
	}
	public void setDow_token(String dow_token) {
		this.dow_token = dow_token;
	}
	public String getBGBK_ATTR() {
		return BGBK_ATTR;
	}
	public void setBGBK_ATTR(String bGBK_ATTR) {
		BGBK_ATTR = bGBK_ATTR;
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
	public String getSENDERACQUIRE() {
		return SENDERACQUIRE;
	}
	public void setSENDERACQUIRE(String sENDERACQUIRE) {
		SENDERACQUIRE = sENDERACQUIRE;
	}
	public String getSENDERBANKID() {
		return SENDERBANKID;
	}
	public void setSENDERBANKID(String sENDERBANKID) {
		SENDERBANKID = sENDERBANKID;
	}
	public String getINACQUIRE() {
		return INACQUIRE;
	}
	public void setINACQUIRE(String iNACQUIRE) {
		INACQUIRE = iNACQUIRE;
	}
	public String getTRANSTYPE() {
		return TRANSTYPE;
	}
	public void setTRANSTYPE(String tRANSTYPE) {
		TRANSTYPE = tRANSTYPE;
	}
	public String getRESULTSTATUS() {
		return RESULTSTATUS;
	}
	public void setRESULTSTATUS(String rESULTSTATUS) {
		RESULTSTATUS = rESULTSTATUS;
	}
	public String getRESULTCODE() {
		return RESULTCODE;
	}
	public void setRESULTCODE(String rESULTCODE) {
		RESULTCODE = rESULTCODE;
	}
	public String getCONRESULTCODE() {
		return CONRESULTCODE;
	}
	public void setCONRESULTCODE(String cONRESULTCODE) {
		CONRESULTCODE = cONRESULTCODE;
	}
	public String getERR_DESC() {
		return ERR_DESC;
	}
	public void setERR_DESC(String eRR_DESC) {
		ERR_DESC = eRR_DESC;
	}
	public String getTXN_ERR_DESC() {
		return TXN_ERR_DESC;
	}
	public void setTXN_ERR_DESC(String tXN_ERR_DESC) {
		TXN_ERR_DESC = tXN_ERR_DESC;
	}
	public String getGL_ERR_DESC() {
		return GL_ERR_DESC;
	}
	public void setGL_ERR_DESC(String gL_ERR_DESC) {
		GL_ERR_DESC = gL_ERR_DESC;
	}
	public String getOTXDATE() {
		return OTXDATE;
	}
	public void setOTXDATE(String oTXDATE) {
		OTXDATE = oTXDATE;
	}
	public String getOSTAN() {
		return OSTAN;
	}
	public void setOSTAN(String oSTAN) {
		OSTAN = oSTAN;
	}
	public String getERROR_ID() {
		return ERROR_ID;
	}
	public void setERROR_ID(String eRROR_ID) {
		ERROR_ID = eRROR_ID;
	}
	public String getTOTALCOUNT() {
		return TOTALCOUNT;
	}
	public void setTOTALCOUNT(String tOTALCOUNT) {
		TOTALCOUNT = tOTALCOUNT;
	}
	public String getSYSERRSELF() {
		return SYSERRSELF;
	}
	public void setSYSERRSELF(String sYSERRSELF) {
		SYSERRSELF = sYSERRSELF;
	}
	public String getSYSERROUT() {
		return SYSERROUT;
	}
	public void setSYSERROUT(String sYSERROUT) {
		SYSERROUT = sYSERROUT;
	}
	public String getTXNERRSELF() {
		return TXNERRSELF;
	}
	public void setTXNERRSELF(String tXNERRSELF) {
		TXNERRSELF = tXNERRSELF;
	}
	public String getTXNERROUT() {
		return TXNERROUT;
	}
	public void setTXNERROUT(String tXNERROUT) {
		TXNERROUT = tXNERROUT;
	}
	public String getOTHERRSELF() {
		return OTHERRSELF;
	}
	public void setOTHERRSELF(String oTHERRSELF) {
		OTHERRSELF = oTHERRSELF;
	}
	public String getOTHERROUT() {
		return OTHERROUT;
	}
	public void setOTHERROUT(String oTHERROUT) {
		OTHERROUT = oTHERROUT;
	}
	public String getFAILCOUNT() {
		return FAILCOUNT;
	}
	public void setFAILCOUNT(String fAILCOUNT) {
		FAILCOUNT = fAILCOUNT;
	}
	public String getCTBK_NAME() {
		return CTBK_NAME;
	}
	public void setCTBK_NAME(String cTBK_NAME) {
		CTBK_NAME = cTBK_NAME;
	}
	
	
}
