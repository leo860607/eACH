package tw.org.twntch.form;

public class Basedata_Download_Form extends CommonForm{
	private static final long serialVersionUID = -5792396278166039240L;

	private String downloadToken;//檔案下載判別用
	private String actionType; //判斷所方端的執行動作
	private String encType; //判斷下載何種編碼檔案
	public String getDownloadToken() {
		return downloadToken;
	}
	public void setDownloadToken(String downloadToken) {
		this.downloadToken = downloadToken;
	}
	public String getActionType() {
		return actionType;
	}
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}
	public String getEncType() {
		return encType;
	}
	public void setEncType(String encType) {
		this.encType = encType;
	}
	
}
