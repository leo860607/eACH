package tw.org.twntch.form;

import java.util.List;

public class Agent_Fee_Search_Form extends CommonForm {

	private String SBIZDATE;
	private String EBIZDATE;
	private String TXN_ID;
	private String TXID;
	private String AGENT_COMPANY_ID;
	private String SND_COMPANY_ID;
	private String TG_RESULT;
	private String CLEARINGPHASE;
	private List txnIdList;
	private List company_IdList ;
	private List snd_company_IdList ;
	private String dow_token ; //檔案下載判別用
	
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
	
	public String getTG_RESULT() {
		return TG_RESULT;
	}
	public void setTG_RESULT(String tG_RESULT) {
		TG_RESULT = tG_RESULT;
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
	public List getTxnIdList() {
		return txnIdList;
	}
	public void setTxnIdList(List txnIdList) {
		this.txnIdList = txnIdList;
	}
	public String getTXN_ID() {
		return TXN_ID;
	}
	public void setTXN_ID(String tXN_ID) {
		TXN_ID = tXN_ID;
	}
	public String getTXID() {
		return TXID;
	}
	public void setTXID(String tXID) {
		TXID = tXID;
	}
	
	
	
}
