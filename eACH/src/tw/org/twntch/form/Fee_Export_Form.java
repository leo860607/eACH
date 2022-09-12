package tw.org.twntch.form;

public class Fee_Export_Form extends CommonForm{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6017473455401849921L;
	private String FEEYM;
	private String downloadToken;	//檔案下載判別用

	public String getFEEYM() {
		return FEEYM;
	}
	public void setFEEYM(String fEEYM) {
		FEEYM = fEEYM;
	}
	public String getDownloadToken() {
		return downloadToken;
	}
	public void setDownloadToken(String downloadToken) {
		this.downloadToken = downloadToken;
	}
}
