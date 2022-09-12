package tw.org.twntch.form;

import java.util.List;

public class Rptst_10_Form extends CommonForm {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9204236369586353855L;
	private String TW_YEAR;
	private String TW_MONTH;
	private String BUSINESS_TYPE_ID;
	private List scaseary;
	private List pcodeList;
	private String dow_token ; //檔案下載判別用
	
	public String getTW_YEAR() {
		return TW_YEAR;
	}
	public void setTW_YEAR(String tW_YEAR) {
		TW_YEAR = tW_YEAR;
	}
	public String getTW_MONTH() {
		return TW_MONTH;
	}
	public void setTW_MONTH(String tW_MONTH) {
		TW_MONTH = tW_MONTH;
	}
	public String getBUSINESS_TYPE_ID() {
		return BUSINESS_TYPE_ID;
	}
	public void setBUSINESS_TYPE_ID(String bUSINESS_TYPE_ID) {
		BUSINESS_TYPE_ID = bUSINESS_TYPE_ID;
	}
	public List getScaseary() {
		return scaseary;
	}
	public void setScaseary(List scaseary) {
		this.scaseary = scaseary;
	}
	public List getPcodeList() {
		return pcodeList;
	}
	public void setPcodeList(List pcodeList) {
		this.pcodeList = pcodeList;
	}
	public String getDow_token() {
		return dow_token;
	}
	public void setDow_token(String dow_token) {
		this.dow_token = dow_token;
	}
}
