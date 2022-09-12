package tw.org.twntch.form;

import java.util.List;

public class PI_SND_Profile_Form extends CommonForm{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4046914916846897212L;
	
	private String PI_COMPANY_ID;
	private String BILL_TYPE_ID;
	private String BILL_NAME;
	private String BILL_TYPE_NAME;
	private String PI_COMPANY_ABBR_NAME;
	private String PI_COMPANY_NAME;
	private String SND_COMPANY_ID;
	private String COMPANY_NAME;
	private String COMPANY_ABBR_NAME;
	private String START_DATE;
	private String STOP_DATE;
	
	
	private	List    txnIdList;
	private List	bgbkIdList;
	private List	bill_type_IdList;
	private List	pi_com_IdList;
	private String dow_token ; //檔案下載判別用
	public String getPI_COMPANY_ID() {
		return PI_COMPANY_ID;
	}
	public void setPI_COMPANY_ID(String pI_COMPANY_ID) {
		PI_COMPANY_ID = pI_COMPANY_ID;
	}
	public String getBILL_TYPE_ID() {
		return BILL_TYPE_ID;
	}
	public void setBILL_TYPE_ID(String bILL_TYPE_ID) {
		BILL_TYPE_ID = bILL_TYPE_ID;
	}
	public String getBILL_NAME() {
		return BILL_NAME;
	}
	public void setBILL_NAME(String bILL_NAME) {
		BILL_NAME = bILL_NAME;
	}
	public String getBILL_TYPE_NAME() {
		return BILL_TYPE_NAME;
	}
	public void setBILL_TYPE_NAME(String bILL_TYPE_NAME) {
		BILL_TYPE_NAME = bILL_TYPE_NAME;
	}
	public String getPI_COMPANY_ABBR_NAME() {
		return PI_COMPANY_ABBR_NAME;
	}
	public void setPI_COMPANY_ABBR_NAME(String pI_COMPANY_ABBR_NAME) {
		PI_COMPANY_ABBR_NAME = pI_COMPANY_ABBR_NAME;
	}
	public String getPI_COMPANY_NAME() {
		return PI_COMPANY_NAME;
	}
	public void setPI_COMPANY_NAME(String pI_COMPANY_NAME) {
		PI_COMPANY_NAME = pI_COMPANY_NAME;
	}
	public String getSND_COMPANY_ID() {
		return SND_COMPANY_ID;
	}
	public void setSND_COMPANY_ID(String sND_COMPANY_ID) {
		SND_COMPANY_ID = sND_COMPANY_ID;
	}
	public List getTxnIdList() {
		return txnIdList;
	}
	public void setTxnIdList(List txnIdList) {
		this.txnIdList = txnIdList;
	}
	public List getBgbkIdList() {
		return bgbkIdList;
	}
	public void setBgbkIdList(List bgbkIdList) {
		this.bgbkIdList = bgbkIdList;
	}
	public List getBill_type_IdList() {
		return bill_type_IdList;
	}
	public void setBill_type_IdList(List bill_type_IdList) {
		this.bill_type_IdList = bill_type_IdList;
	}
	public String getDow_token() {
		return dow_token;
	}
	public void setDow_token(String dow_token) {
		this.dow_token = dow_token;
	}
	public String getSTART_DATE() {
		return START_DATE;
	}
	public void setSTART_DATE(String sTART_DATE) {
		START_DATE = sTART_DATE;
	}
	public String getSTOP_DATE() {
		return STOP_DATE;
	}
	public void setSTOP_DATE(String sTOP_DATE) {
		STOP_DATE = sTOP_DATE;
	}
	public List getPi_com_IdList() {
		return pi_com_IdList;
	}
	public void setPi_com_IdList(List pi_com_IdList) {
		this.pi_com_IdList = pi_com_IdList;
	}
	public String getCOMPANY_NAME() {
		return COMPANY_NAME;
	}
	public void setCOMPANY_NAME(String cOMPANY_NAME) {
		COMPANY_NAME = cOMPANY_NAME;
	}
	public String getCOMPANY_ABBR_NAME() {
		return COMPANY_ABBR_NAME;
	}
	public void setCOMPANY_ABBR_NAME(String cOMPANY_ABBR_NAME) {
		COMPANY_ABBR_NAME = cOMPANY_ABBR_NAME;
	}
	
	
	
	
}
