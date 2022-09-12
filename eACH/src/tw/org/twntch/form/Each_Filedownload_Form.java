package tw.org.twntch.form;

public class Each_Filedownload_Form extends CommonForm {

	private static final long serialVersionUID = -6858155249205720858L;
	//多筆下載用
	private String ID_NOS;
	private String FILE_NOS;
	private String ROWIDS;
	//
	private String dow_token;//檔案下載判別用
	
	public String getID_NOS() {
		return ID_NOS;
	}
	public void setID_NOS(String iD_NOS) {
		ID_NOS = iD_NOS;
	}
	public String getFILE_NOS() {
		return FILE_NOS;
	}
	public void setFILE_NOS(String fILE_NOS) {
		FILE_NOS = fILE_NOS;
	}
	public String getDow_token() {
		return dow_token;
	}
	public void setDow_token(String dow_token) {
		this.dow_token = dow_token;
	}
	public String getROWIDS() {
		return ROWIDS;
	}
	public void setROWIDS(String rOWIDS) {
		ROWIDS = rOWIDS;
	}
}
