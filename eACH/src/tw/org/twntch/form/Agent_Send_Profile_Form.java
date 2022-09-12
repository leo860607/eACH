package tw.org.twntch.form;

import java.util.List;

public class Agent_Send_Profile_Form extends CommonForm {
	
	
	private String TXN_ID  ;    	
	private String COMPANY_ID ;	
	private String SND_COMPANY_ID ;
	private String ACTIVE_DATE  ;    	
	private String STOP_DATE ;	
	private String CDATE ;
	private String UDATE ;
	private String COMPANY_NAME ;	
	private String SND_COMPANY_NAME ;
	
	private List txnIdList;
	private List companyIdList;
	private String dow_token ;
	
	public String getTXN_ID() {
		return TXN_ID;
	}
	public void setTXN_ID(String tXN_ID) {
		TXN_ID = tXN_ID;
	}
	public String getCOMPANY_ID() {
		return COMPANY_ID;
	}
	public void setCOMPANY_ID(String cOMPANY_ID) {
		COMPANY_ID = cOMPANY_ID;
	}
	public String getSND_COMPANY_ID() {
		return SND_COMPANY_ID;
	}
	public void setSND_COMPANY_ID(String sND_COMPANY_ID) {
		SND_COMPANY_ID = sND_COMPANY_ID;
	}
	public String getACTIVE_DATE() {
		return ACTIVE_DATE;
	}
	public void setACTIVE_DATE(String aCTIVE_DATE) {
		ACTIVE_DATE = aCTIVE_DATE;
	}
	public String getSTOP_DATE() {
		return STOP_DATE;
	}
	public void setSTOP_DATE(String sTOP_DATE) {
		STOP_DATE = sTOP_DATE;
	}
	public String getCDATE() {
		return CDATE;
	}
	public void setCDATE(String cDATE) {
		CDATE = cDATE;
	}
	public String getUDATE() {
		return UDATE;
	}
	public void setUDATE(String uDATE) {
		UDATE = uDATE;
	}
	public String getCOMPANY_NAME() {
		return COMPANY_NAME;
	}
	public void setCOMPANY_NAME(String cOMPANY_NAME) {
		COMPANY_NAME = cOMPANY_NAME;
	}
	public String getSND_COMPANY_NAME() {
		return SND_COMPANY_NAME;
	}
	public void setSND_COMPANY_NAME(String sND_COMPANY_NAME) {
		SND_COMPANY_NAME = sND_COMPANY_NAME;
	}
	public List getTxnIdList() {
		return txnIdList;
	}
	public void setTxnIdList(List txnIdList) {
		this.txnIdList = txnIdList;
	}
	public List getCompanyIdList() {
		return companyIdList;
	}
	public void setCompanyIdList(List companyIdList) {
		this.companyIdList = companyIdList;
	}
	public String getDow_token() {
		return dow_token;
	}
	public void setDow_token(String dow_token) {
		this.dow_token = dow_token;
	}

	
	
	
}
