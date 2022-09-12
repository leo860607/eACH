package tw.org.twntch.form;

import java.util.List;

public class Each_Txn_Code_Form extends CommonForm{
	private	List	scaseary	;
	private	List	idList	;
	private	String	jsonList	;
	private String  EACH_TXN_ID;
	private String  EACH_TXN_NAME;
	private String  BUSINESS_TYPE_ID;
	public List getScaseary() {
		return scaseary;
	}
	public void setScaseary(List scaseary) {
		this.scaseary = scaseary;
	}
	public List getIdList() {
		return idList;
	}
	public void setIdList(List idList) {
		this.idList = idList;
	}
	public String getJsonList() {
		return jsonList;
	}
	public void setJsonList(String jsonList) {
		this.jsonList = jsonList;
	}
	public String getEACH_TXN_ID() {
		return EACH_TXN_ID;
	}
	public void setEACH_TXN_ID(String eACH_TXN_ID) {
		EACH_TXN_ID = eACH_TXN_ID;
	}
	public String getEACH_TXN_NAME() {
		return EACH_TXN_NAME;
	}
	public void setEACH_TXN_NAME(String eACH_TXN_NAME) {
		EACH_TXN_NAME = eACH_TXN_NAME;
	}
	public String getBUSINESS_TYPE_ID() {
		return BUSINESS_TYPE_ID;
	}
	public void setBUSINESS_TYPE_ID(String bUSINESS_TYPE_ID) {
		BUSINESS_TYPE_ID = bUSINESS_TYPE_ID;
	}	

	
	
}
