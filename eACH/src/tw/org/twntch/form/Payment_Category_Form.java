package tw.org.twntch.form;

import java.util.List;

public class Payment_Category_Form extends CommonForm {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2857383308505958283L;
	
	private	List	scaseary;
	private	List	idList	;
	private	String	jsonList	;
	private String	BILL_TYPE_ID;
	private String	BILL_TYPE_NAME;
	
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
	public String getBILL_TYPE_ID() {
		return BILL_TYPE_ID;
	}
	public void setBILL_TYPE_ID(String bILL_TYPE_ID) {
		BILL_TYPE_ID = bILL_TYPE_ID;
	}
	public String getBILL_TYPE_NAME() {
		return BILL_TYPE_NAME;
	}
	public void setBILL_TYPE_NAME(String bILL_TYPE_NAME) {
		BILL_TYPE_NAME = bILL_TYPE_NAME;
	}
	
	
	
}
