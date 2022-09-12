package tw.org.twntch.form;

import java.util.List;

public class Rptcl_4_Form extends CommonForm {
	
	private List bg_bankList ;
	private List opt_bankList ;
	private String dow_token ; //檔案下載判別用
	private String TXDT ; //日期時間
	private String CLEARINGPHASE ; //清算階段
	private String CTBK_ID ; //清算行代號
	private String CTBK_NAME ; //清算行代號+名稱
	private String BIZDATE ; //營業日期
//	private String opt_bank ; //操作行名稱
	private List ct_bankList ;//清算行清單
	
	public List getBg_bankList() {
		return bg_bankList;
	}
	public void setBg_bankList(List bg_bankList) {
		this.bg_bankList = bg_bankList;
	}
	public List getOpt_bankList() {
		return opt_bankList;
	}
	public void setOpt_bankList(List opt_bankList) {
		this.opt_bankList = opt_bankList;
	}
	public String getDow_token() {
		return dow_token;
	}
	public void setDow_token(String dow_token) {
		this.dow_token = dow_token;
	}
	public String getTXDT() {
		return TXDT;
	}
	public void setTXDT(String tXDT) {
		TXDT = tXDT;
	}
	public String getCLEARINGPHASE() {
		return CLEARINGPHASE;
	}
	public void setCLEARINGPHASE(String cLEARINGPHASE) {
		CLEARINGPHASE = cLEARINGPHASE;
	}
	public String getCTBK_ID() {
		return this.CTBK_ID;
	}
	public void setCTBK_ID(String CTBK_ID) {
		this.CTBK_ID = CTBK_ID;
	}
//	public String getOpt_bank() {
//		return opt_bank;
//	}
//	public void setOpt_bank(String opt_bank) {
//		this.opt_bank = opt_bank;
//	}
	public List getCt_bankList() {
		return ct_bankList;
	}
	public void setCt_bankList(List ct_bankList) {
		this.ct_bankList = ct_bankList;
	}
	public String getBIZDATE() {
		return BIZDATE;
	}
	public void setBIZDATE(String bIZDATE) {
		BIZDATE = bIZDATE;
	}
	public String getCTBK_NAME() {
		return CTBK_NAME;
	}
	public void setCTBK_NAME(String cTBK_NAME) {
		CTBK_NAME = cTBK_NAME;
	}

	
	
}
