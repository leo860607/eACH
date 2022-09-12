package tw.org.twntch.form;

public class Tx_Err_Form extends CommonForm {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4383416770867964938L;
	private String BIZDATE;
	private String CLEARINGPHASE;
	private String sourcePage;	//來源網頁
	
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
	public String getSourcePage() {
		return sourcePage;
	}
	public void setSourcePage(String sourcePage) {
		this.sourcePage = sourcePage;
	}
	
}
