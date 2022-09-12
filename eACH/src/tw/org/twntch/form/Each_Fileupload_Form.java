package tw.org.twntch.form;

import java.util.List;

import org.apache.struts.upload.FormFile;

public class Each_Fileupload_Form extends CommonForm {

	private static final long serialVersionUID = 6727661119944967626L;
	//頁面的File欄位
	private FormFile FILE1;
	private FormFile FILE2;
	private FormFile FILE3;
	//檔案描述(新增)
	private String FILE_DESC1;
	private String FILE_DESC2;
	private String FILE_DESC3;
	private String USER_COMPANY;//判斷使用者的身份
	//銀行下拉清單
	private	String[] banksArray;
	private	List banksList;
	//銀行下拉清單(已選擇)
	private	String[] selectedBanksArray;
	private	List selectedBanksList;
	//多筆刪除用
	private String ID_NOS;
	private String FILE_NOS;
	private String ACTION_TYPE;
	
	public FormFile getFILE1() {
		return FILE1;
	}
	public void setFILE1(FormFile fILE1) {
		FILE1 = fILE1;
	}
	public FormFile getFILE2() {
		return FILE2;
	}
	public void setFILE2(FormFile fILE2) {
		FILE2 = fILE2;
	}
	public FormFile getFILE3() {
		return FILE3;
	}
	public void setFILE3(FormFile fILE3) {
		FILE3 = fILE3;
	}
	public String getFILE_DESC1() {
		return FILE_DESC1;
	}
	public void setFILE_DESC1(String fILE_DESC1) {
		FILE_DESC1 = fILE_DESC1;
	}
	public String getFILE_DESC2() {
		return FILE_DESC2;
	}
	public void setFILE_DESC2(String fILE_DESC2) {
		FILE_DESC2 = fILE_DESC2;
	}
	public String getFILE_DESC3() {
		return FILE_DESC3;
	}
	public void setFILE_DESC3(String fILE_DESC3) {
		FILE_DESC3 = fILE_DESC3;
	}
	public String getUSER_COMPANY() {
		return USER_COMPANY;
	}
	public void setUSER_COMPANY(String uSER_COMPANY) {
		USER_COMPANY = uSER_COMPANY;
	}
	public String[] getBanksArray() {
		return banksArray;
	}
	public void setBanksArray(String[] banksArray) {
		this.banksArray = banksArray;
	}
	public List getBanksList() {
		return banksList;
	}
	public void setBanksList(List banksList) {
		this.banksList = banksList;
	}
	public String[] getSelectedBanksArray() {
		return selectedBanksArray;
	}
	public void setSelectedBanksArray(String[] selectedBanksArray) {
		this.selectedBanksArray = selectedBanksArray;
	}
	public List getSelectedBanksList() {
		return selectedBanksList;
	}
	public void setSelectedBanksList(List selectedBanksList) {
		this.selectedBanksList = selectedBanksList;
	}
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
	public String getACTION_TYPE() {
		return ACTION_TYPE;
	}
	public void setACTION_TYPE(String aCTION_TYPE) {
		ACTION_TYPE = aCTION_TYPE;
	}
	
}
