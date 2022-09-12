package tw.org.twntch.form;

public class GCS_Download_Form extends CommonForm{
	private static final long serialVersionUID = -569686906292428197L;

	private String BIZDATE;//營業日期
	private String CLEARINGPHASE;//清算階段
	private String downloadToken;//檔案下載判別用

	public String getBIZDATE() {
		return BIZDATE;
	}
	public void setBIZDATE(String bIZDATE) {
		BIZDATE = bIZDATE;
	}
	public String getCLEARINGPHASE() {
		return CLEARINGPHASE;
	}
	public void setCLEARINGPHASE(String cLEARINGPHASE) {
		CLEARINGPHASE = cLEARINGPHASE;
	}
	public String getDownloadToken() {
		return downloadToken;
	}
	public void setDownloadToken(String downloadToken) {
		this.downloadToken = downloadToken;
	}
}
