package tw.org.twntch.form;

import java.util.List;

public class Rptcl_3_Form extends CommonForm {
	
	private List bg_bankList ;
	private List opt_bankList ;
	private String dow_token ; //檔案下載判別用
	private String TXDT ; //日期時間
	private String CLEARINGPHASE ; //清算階段
//	private String CTBK_ID ; //清算行代號
//	private String opt_bank ; //操作行名稱
//	private List ct_bankList ;//清算行清單
	private String beforeDate ;//前一營業日
	
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
	public String getbeforeDate() {
		return this.beforeDate;
	}
	public void setbeforeDate(String beforeDate) {
		this.beforeDate = beforeDate;
	}
//	public String getOpt_bank() {
//		return opt_bank;
//	}
//	public void setOpt_bank(String opt_bank) {
//		this.opt_bank = opt_bank;
//	}
//	public List getCt_bankList() {
//		return ct_bankList;
//	}
//	public void setCt_bankList(List ct_bankList) {
//		this.ct_bankList = ct_bankList;
//	}

}
