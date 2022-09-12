package tw.org.twntch.form;

import java.util.List;

import javax.persistence.Transient;

import org.apache.struts.upload.FormFile;

public class SD_Company_Profile_Form extends CommonForm{
	
	private	List	scaseary	;
	private	List    txnIdList	;
	private String COMPANY_ID ;
	private String TXN_ID ;
	private String SND_BRBK_ID ;
	private String SND_BRBK_NAME;
	private	String	COMPANY_ABBR_NAME	;
	private	String	COMPANY_NAME	;
	private	String	CONTACT_INFO	;
	private	String	START_DATE	;	//開辦日期(ex:104年3月1日起)
	private	String	DISPATCH_DATE	;
	private	String	USER_NO	;
	private	String	CASE_NO	;
	private	String	STOP_DATE	;
	private	String	CDATE	;
	private	String	UDATE	;
	private String	ACTIVE_DATE;	//啟用日期(ex:01040301)
	private FormFile FILE;   //檔案匯入
	private String	FEE_TYPE;
	private String FEE_ID;
	private String	FEE_TYPE_CHT;
	private String	FEE_TYPE_ACTIVE_DATE;
	private String	BIZDATE;
	private String	LOCK_ACTIVE_DATE;
	//為了記錄更改前的SD歷程而加
	private String FEE_TYPE_ORG;
	private String FEE_TYPE_ACTIVE_DATE_ORG;
	
	public String getFEE_TYPE() {
		return FEE_TYPE;
	}
	public void setFEE_TYPE(String fEE_TYPE) {
		FEE_TYPE = fEE_TYPE;
	}
	private String	SENDERHEAD;
	private List	bgbkIdList;
	
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
	public String getCONTACT_INFO() {
		return CONTACT_INFO;
	}
	public void setCONTACT_INFO(String cONTACT_INFO) {
		CONTACT_INFO = cONTACT_INFO;
	}
	public String getSTART_DATE() {
		return START_DATE;
	}
	public void setSTART_DATE(String sTART_DATE) {
		START_DATE = sTART_DATE;
	}
	public String getDISPATCH_DATE() {
		return DISPATCH_DATE;
	}
	public void setDISPATCH_DATE(String dISPATCH_DATE) {
		DISPATCH_DATE = dISPATCH_DATE;
	}
	public String getUSER_NO() {
		return USER_NO;
	}
	public void setUSER_NO(String uSER_NO) {
		USER_NO = uSER_NO;
	}
	public String getCASE_NO() {
		return CASE_NO;
	}
	public void setCASE_NO(String cASE_NO) {
		CASE_NO = cASE_NO;
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
	public String getACTIVE_DATE() {
		return ACTIVE_DATE;
	}
	public void setACTIVE_DATE(String aCTIVE_DATE) {
		ACTIVE_DATE = aCTIVE_DATE;
	}
	public FormFile getFILE() {
		return FILE;
	}
	public void setFILE(FormFile fILE) {
		FILE = fILE;
	}
	
	
	public String getFEE_ID() {
		return FEE_ID;
	}
	public void setFEE_ID(String fEE_ID) {
		FEE_ID = fEE_ID;
	}
	public String getFEE_TYPE_CHT() {
		return FEE_TYPE_CHT;
	}
	public void setFEE_TYPE_CHT(String fEE_TYPE_CHT) {
		FEE_TYPE_CHT = fEE_TYPE_CHT;
	}
	public String getFEE_TYPE_ORG() {
		return FEE_TYPE_ORG;
	}
	public void setFEE_TYPE_ORG(String fEE_TYPE_ORG) {
		FEE_TYPE_ORG = fEE_TYPE_ORG;
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
	public String getLOCK_ACTIVE_DATE() {
		return LOCK_ACTIVE_DATE;
	}
	public void setLOCK_ACTIVE_DATE(String lOCK_ACTIVE_DATE) {
		LOCK_ACTIVE_DATE = lOCK_ACTIVE_DATE;
	}
}
