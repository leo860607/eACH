package tw.org.twntch.form;

import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

public class SC_Company_Profile_Form extends CommonForm{

	private	List	scaseary	;
	private	List    txnIdList	;
	private	List    feeTypeList	;
	public List getFeeTypeList() {
		return feeTypeList;
	}
	public void setFeeTypeList(List feeTypeList) {
		this.feeTypeList = feeTypeList;
	}
	private String COMPANY_ID ;
	private String TXN_ID ;
	private String SND_BRBK_ID ;
	private String SND_BRBK_NAME;
	private	String	COMPANY_ABBR_NAME	;
	private	String	COMPANY_NAME	;
	private	String	IPO_COMPANY_ID	;
	private	String	PROFIT_ISSUE_DATE	;
	private	String	CDATE	;
	private	String	UDATE	;
	private String	SENDERHEAD;
	private String  START_DATE;
	private String  END_DATE;
	private String  SYS_CDATE;
	private List	bgbkIdList;
	private String	IS_SHORT;
	private String  BUSSINESS_DATE;
	private String	FEE_TYPE;
	//為了記錄更改前的FEE_TYPE FOR SC歷程而加
	private String FEE_TYPE_ORG;
	private String FEE_TYPE_ACTIVE_DATE_ORG;
	private String FEE_TYPE_CHT;
	private String	FEE_TYPE_ACTIVE_DATE;
	private String	BIZDATE;
	
	private FormFile FILE;   //檔案匯入
	public String getFEE_TYPE() {
		return FEE_TYPE;
	}
	public void setFEE_TYPE(String fEE_TYPE) {
		FEE_TYPE = fEE_TYPE;
	}
	public List getScaseary() {
		return scaseary;
	}
	public void setScaseary(List scaseary) {
		this.scaseary = scaseary;
	}
	public List getTxnIdList() {
		return txnIdList;
	}
	public void setTxnIdList(List txnIdList) {
		this.txnIdList = txnIdList;
	}
	public String getCOMPANY_ID() {
		return COMPANY_ID;
	}
	public void setCOMPANY_ID(String cOMPANY_ID) {
		COMPANY_ID = cOMPANY_ID;
	}
	public String getTXN_ID() {
		return TXN_ID;
	}
	public void setTXN_ID(String tXN_ID) {
		TXN_ID = tXN_ID;
	}
	public String getSND_BRBK_ID() {
		return SND_BRBK_ID;
	}
	public void setSND_BRBK_ID(String sND_BRBK_ID) {
		SND_BRBK_ID = sND_BRBK_ID;
	}
	public String getSND_BRBK_NAME() {
		return SND_BRBK_NAME;
	}
	public void setSND_BRBK_NAME(String sND_BRBK_NAME) {
		SND_BRBK_NAME = sND_BRBK_NAME;
	}
	public String getCOMPANY_ABBR_NAME() {
		return COMPANY_ABBR_NAME;
	}
	public void setCOMPANY_ABBR_NAME(String cOMPANY_ABBR_NAME) {
		COMPANY_ABBR_NAME = cOMPANY_ABBR_NAME;
	}
	public String getCOMPANY_NAME() {
		return COMPANY_NAME;
	}
	public void setCOMPANY_NAME(String cOMPANY_NAME) {
		COMPANY_NAME = cOMPANY_NAME;
	}
	public String getIPO_COMPANY_ID() {
		return IPO_COMPANY_ID;
	}
	public void setIPO_COMPANY_ID(String iPO_COMPANY_ID) {
		IPO_COMPANY_ID = iPO_COMPANY_ID;
	}
	public String getPROFIT_ISSUE_DATE() {
		return PROFIT_ISSUE_DATE;
	}
	public void setPROFIT_ISSUE_DATE(String pROFIT_ISSUE_DATE) {
		PROFIT_ISSUE_DATE = pROFIT_ISSUE_DATE;
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
	public String getSENDERHEAD() {
		return SENDERHEAD;
	}
	public void setSENDERHEAD(String sENDERHEAD) {
		SENDERHEAD = sENDERHEAD;
	}
	public List getBgbkIdList() {
		return bgbkIdList;
	}
	public void setBgbkIdList(List bgbkIdList) {
		this.bgbkIdList = bgbkIdList;
	}
	public FormFile getFILE() {
		return FILE;
	}
	public void setFILE(FormFile fILE) {
		FILE = fILE;
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
	public String getSYS_CDATE() {
		return SYS_CDATE;
	}
	public void setSYS_CDATE(String sYS_CDATE) {
		SYS_CDATE = sYS_CDATE;
	}
	public String getIS_SHORT() {
		return IS_SHORT;
	}
	public void setIS_SHORT(String iS_SHORT) {
		IS_SHORT = iS_SHORT;
	}
	public String getBUSSINESS_DATE() {
		return BUSSINESS_DATE;
	}
	public void setBUSSINESS_DATE(String bUSSINESS_DATE) {
		BUSSINESS_DATE = bUSSINESS_DATE;
	}
	public String getFEE_TYPE_ORG() {
		return FEE_TYPE_ORG;
	}
	public void setFEE_TYPE_ORG(String fEE_TYPE_ORG) {
		FEE_TYPE_ORG = fEE_TYPE_ORG;
	}
	public String getFEE_TYPE_CHT() {
		return FEE_TYPE_CHT;
	}
	public void setFEE_TYPE_CHT(String fEE_TYPE_CHT) {
		FEE_TYPE_CHT = fEE_TYPE_CHT;
	}
	public String getFEE_TYPE_ACTIVE_DATE() {
		return FEE_TYPE_ACTIVE_DATE;
	}
	public void setFEE_TYPE_ACTIVE_DATE(String fEE_TYPE_ACTIVE_DATE) {
		FEE_TYPE_ACTIVE_DATE = fEE_TYPE_ACTIVE_DATE;
	}
	public String getBIZDATE() {
		return BIZDATE;
	}
	public void setBIZDATE(String bIZDATE) {
		BIZDATE = bIZDATE;
	}
	public String getFEE_TYPE_ACTIVE_DATE_ORG() {
		return FEE_TYPE_ACTIVE_DATE_ORG;
	}
	public void setFEE_TYPE_ACTIVE_DATE_ORG(String fEE_TYPE_ACTIVE_DATE_ORG) {
		FEE_TYPE_ACTIVE_DATE_ORG = fEE_TYPE_ACTIVE_DATE_ORG;
	}
	
}
