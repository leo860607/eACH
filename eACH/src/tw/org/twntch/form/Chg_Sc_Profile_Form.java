package tw.org.twntch.form;

import java.util.List;

public class Chg_Sc_Profile_Form extends CommonForm{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3380683573393472301L;
	private	List	scaseary;
	private	List    txnIdList;
	private List	bgbkIdList;
	private String BGBK_ID;
	
	private String SD_ITEM_NO;
	private String COMPANY_ID;
	private String COMPANY_ABBR_NAME;
	private String COMPANY_NAME;
	private String TXN_ID;
	private String INBANKID;
	private String INBANKNAME;
	private String INBANKACCTNO;
	private String LAYOUTID;
	private String DEALY_CHARGE_DAY;
	private String START_DATE;
	private String STOP_DATE;
	private String NOTE;
	
	private String dow_token ; //檔案下載判別用
	
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
	public List getBgbkIdList() {
		return bgbkIdList;
	}
	public void setBgbkIdList(List bgbkIdList) {
		this.bgbkIdList = bgbkIdList;
	}
	public String getBGBK_ID() {
		return BGBK_ID;
	}
	public void setBGBK_ID(String bGBK_ID) {
		BGBK_ID = bGBK_ID;
	}
	public String getSD_ITEM_NO() {
		return SD_ITEM_NO;
	}
	public void setSD_ITEM_NO(String sD_ITEM_NO) {
		SD_ITEM_NO = sD_ITEM_NO;
	}
	public String getCOMPANY_ID() {
		return COMPANY_ID;
	}
	public void setCOMPANY_ID(String cOMPANY_ID) {
		COMPANY_ID = cOMPANY_ID;
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
	public String getTXN_ID() {
		return TXN_ID;
	}
	public void setTXN_ID(String tXN_ID) {
		TXN_ID = tXN_ID;
	}
	public String getINBANKID() {
		return INBANKID;
	}
	public void setINBANKID(String iNBANKID) {
		INBANKID = iNBANKID;
	}
	public String getINBANKNAME() {
		return INBANKNAME;
	}
	public void setINBANKNAME(String iNBANKNAME) {
		INBANKNAME = iNBANKNAME;
	}
	public String getINBANKACCTNO() {
		return INBANKACCTNO;
	}
	public void setINBANKACCTNO(String iNBANKACCTNO) {
		INBANKACCTNO = iNBANKACCTNO;
	}
	public String getLAYOUTID() {
		return LAYOUTID;
	}
	public void setLAYOUTID(String lAYOUTID) {
		LAYOUTID = lAYOUTID;
	}
	public String getDEALY_CHARGE_DAY() {
		return DEALY_CHARGE_DAY;
	}
	public void setDEALY_CHARGE_DAY(String dEALY_CHARGE_DAY) {
		DEALY_CHARGE_DAY = dEALY_CHARGE_DAY;
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
	public String getNOTE() {
		return NOTE;
	}
	public void setNOTE(String nOTE) {
		NOTE = nOTE;
	}
	public String getDow_token() {
		return dow_token;
	}
	public void setDow_token(String dow_token) {
		this.dow_token = dow_token;
	}
	
}
