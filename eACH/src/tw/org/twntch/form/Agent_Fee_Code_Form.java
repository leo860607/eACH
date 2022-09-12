package tw.org.twntch.form;

import java.math.BigDecimal;
import java.util.List;

public class Agent_Fee_Code_Form extends CommonForm{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7643058703442889575L;
	private String FEE_ID;
	private String COMPANY_ID;
	private String COMPANY_NAME;
	private String START_DATE;
	private String FEE;
	private String FEE_DESC;
	private String ACTIVE_DESC;
	private String CDATE;
	private String UDATE;
	private List txnIdList;
	private List companyIdList;
	
	public String getFEE_ID() {
		return FEE_ID;
	}
	public void setFEE_ID(String fEE_ID) {
		FEE_ID = fEE_ID;
	}
	public String getCOMPANY_ID() {
		return COMPANY_ID;
	}
	public void setCOMPANY_ID(String cOMPANY_ID) {
		COMPANY_ID = cOMPANY_ID;
	}
	public String getCOMPANY_NAME() {
		return COMPANY_NAME;
	}
	public void setCOMPANY_NAME(String cOMPANY_NAME) {
		COMPANY_NAME = cOMPANY_NAME;
	}
	public String getSTART_DATE() {
		return START_DATE;
	}
	public void setSTART_DATE(String sTART_DATE) {
		START_DATE = sTART_DATE;
	}
	public String getFEE() {
		return FEE;
	}
	public void setFEE(String fEE) {
		FEE = fEE;
	}
	public String getFEE_DESC() {
		return FEE_DESC;
	}
	public void setFEE_DESC(String fEE_DESC) {
		FEE_DESC = fEE_DESC;
	}
	public String getACTIVE_DESC() {
		return ACTIVE_DESC;
	}
	public void setACTIVE_DESC(String aCTIVE_DESC) {
		ACTIVE_DESC = aCTIVE_DESC;
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
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Agent_Fee_Code_Form [FEE_ID=");
		builder.append(FEE_ID);
		builder.append(", COMPANY_ID=");
		builder.append(COMPANY_ID);
		builder.append(", START_DATE=");
		builder.append(START_DATE);
		builder.append(", FEE=");
		builder.append(FEE);
		builder.append(", FEE_DESC=");
		builder.append(FEE_DESC);
		builder.append(", ACTIVE_DESC=");
		builder.append(ACTIVE_DESC);
		builder.append(", CDATE=");
		builder.append(CDATE);
		builder.append(", UDATE=");
		builder.append(UDATE);
		builder.append("]");
		return builder.toString();
	}
}
