package tw.org.twntch.form;

import java.util.List;

public class WO_Company_Profile_Form extends CommonForm{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8970858231877203035L;
	private String WO_COMPANY_ID;
	private String BILL_TYPE_ID;
	private String BILL_NAME;
	private String BILL_TYPE_NAME;
	private String WO_COMPANY_ABBR_NAME;
	private String WO_COMPANY_NAME;
	private String SND_COMPANY_ID;
	private String START_DATE;
	private String STOP_DATE;
	private String TXN_ID;
	private String TYPE_ACCT;
	private String TYPE_WRITE_OFF_NO;
	private String TYPE_BARCODE;
	private String NOTE;
	private String INBANK_ID;
	private String INBANK_NAME;
	private String INBANK_ACCT_NO;
	private String SD_ITEM_NO;
	private String FMT_ID;
	private String BGBK_ID;
	private String IS_INTEGRATED;
	private String SD_ITEM;
	private String ITEM_START;
	private String ITEM_END;
	private String SD_ITEM_ID;
	private String VIRTUAL_ACC_NOTE;
	private String WO_NO_NOTE;
	
	
	private	List    txnIdList;
	private List	bgbkIdList;
	private List	bill_type_IdList;
	private String dow_token ; //檔案下載判別用
	

	public String getWO_COMPANY_ID() {
		return WO_COMPANY_ID;
	}
	public void setWO_COMPANY_ID(String wO_COMPANY_ID) {
		WO_COMPANY_ID = wO_COMPANY_ID;
	}
	public String getWO_COMPANY_ABBR_NAME() {
		return WO_COMPANY_ABBR_NAME;
	}
	public void setWO_COMPANY_ABBR_NAME(String wO_COMPANY_ABBR_NAME) {
		WO_COMPANY_ABBR_NAME = wO_COMPANY_ABBR_NAME;
	}
	public String getWO_COMPANY_NAME() {
		return WO_COMPANY_NAME;
	}
	public void setWO_COMPANY_NAME(String wO_COMPANY_NAME) {
		WO_COMPANY_NAME = wO_COMPANY_NAME;
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
	
	public String getSND_COMPANY_ID() {
		return SND_COMPANY_ID;
	}
	public void setSND_COMPANY_ID(String sND_COMPANY_ID) {
		SND_COMPANY_ID = sND_COMPANY_ID;
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
	public String getTXN_ID() {
		return TXN_ID;
	}
	public void setTXN_ID(String tXN_ID) {
		TXN_ID = tXN_ID;
	}
	
	
	public String getTYPE_ACCT() {
		return TYPE_ACCT;
	}
	public void setTYPE_ACCT(String tYPE_ACCT) {
		TYPE_ACCT = tYPE_ACCT;
	}
	public String getTYPE_WRITE_OFF_NO() {
		return TYPE_WRITE_OFF_NO;
	}
	public void setTYPE_WRITE_OFF_NO(String tYPE_WRITE_OFF_NO) {
		TYPE_WRITE_OFF_NO = tYPE_WRITE_OFF_NO;
	}
	public String getTYPE_BARCODE() {
		return TYPE_BARCODE;
	}
	public void setTYPE_BARCODE(String tYPE_BARCODE) {
		TYPE_BARCODE = tYPE_BARCODE;
	}
	public String getNOTE() {
		return NOTE;
	}
	public void setNOTE(String nOTE) {
		NOTE = nOTE;
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
	public String getINBANK_ID() {
		return INBANK_ID;
	}
	public void setINBANK_ID(String iNBANK_ID) {
		INBANK_ID = iNBANK_ID;
	}
	public String getINBANK_NAME() {
		return INBANK_NAME;
	}
	public void setINBANK_NAME(String iNBANK_NAME) {
		INBANK_NAME = iNBANK_NAME;
	}
	public String getINBANK_ACCT_NO() {
		return INBANK_ACCT_NO;
	}
	public void setINBANK_ACCT_NO(String iNBANK_ACCT_NO) {
		INBANK_ACCT_NO = iNBANK_ACCT_NO;
	}
	public String getSD_ITEM_NO() {
		return SD_ITEM_NO;
	}
	public void setSD_ITEM_NO(String sD_ITEM_NO) {
		SD_ITEM_NO = sD_ITEM_NO;
	}
	public String getFMT_ID() {
		return FMT_ID;
	}
	public void setFMT_ID(String fMT_ID) {
		FMT_ID = fMT_ID;
	}
	public String getBGBK_ID() {
		return BGBK_ID;
	}
	public void setBGBK_ID(String bGBK_ID) {
		BGBK_ID = bGBK_ID;
	}
	public String getBILL_TYPE_NAME() {
		return BILL_TYPE_NAME;
	}
	public void setBILL_TYPE_NAME(String bILL_TYPE_NAME) {
		BILL_TYPE_NAME = bILL_TYPE_NAME;
	}
	public String getIS_INTEGRATED() {
		return IS_INTEGRATED;
	}
	public void setIS_INTEGRATED(String iS_INTEGRATED) {
		IS_INTEGRATED = iS_INTEGRATED;
	}
	public String getSD_ITEM() {
		return SD_ITEM;
	}
	public void setSD_ITEM(String sD_ITEM) {
		SD_ITEM = sD_ITEM;
	}
	public String getITEM_START() {
		return ITEM_START;
	}
	public void setITEM_START(String iTEM_START) {
		ITEM_START = iTEM_START;
	}
	public String getITEM_END() {
		return ITEM_END;
	}
	public void setITEM_END(String iTEM_END) {
		ITEM_END = iTEM_END;
	}
	public String getSD_ITEM_ID() {
		return SD_ITEM_ID;
	}
	public void setSD_ITEM_ID(String sD_ITEM_ID) {
		SD_ITEM_ID = sD_ITEM_ID;
	}
	public String getVIRTUAL_ACC_NOTE() {
		return VIRTUAL_ACC_NOTE;
	}
	public void setVIRTUAL_ACC_NOTE(String vIRTUAL_ACC_NOTE) {
		VIRTUAL_ACC_NOTE = vIRTUAL_ACC_NOTE;
	}
	public String getWO_NO_NOTE() {
		return WO_NO_NOTE;
	}
	public void setWO_NO_NOTE(String wO_NO_NOTE) {
		WO_NO_NOTE = wO_NO_NOTE;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
}
