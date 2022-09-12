package tw.org.twntch.form;

import java.util.List;

import org.apache.struts.action.ActionForm;

public class Business_Type_Form extends CommonForm{
	
	
	private	List	scaseary	;
	private	List	bsIdKist	;
	private	String	BUSINESS_TYPE_ID	;
	private	String	BUSINESS_TYPE_NAME	;
	
	private String	json_unselectedBank;
	private String	json_selectedBank;
	private List	unselectedBankList;
	private List 	selectedBankList;
	private String[]	unselectedBankArray;
	private String[] 	selectedBankArray;
	
	public List getScaseary() {
		return scaseary;
	}
	public void setScaseary(List scaseary) {
		this.scaseary = scaseary;
	}
	
	public List getBsIdKist() {
		return bsIdKist;
	}
	public void setBsIdKist(List bsIdKist) {
		this.bsIdKist = bsIdKist;
	}
	public String getBUSINESS_TYPE_ID() {
		return BUSINESS_TYPE_ID;
	}
	public void setBUSINESS_TYPE_ID(String bUSINESS_TYPE_ID) {
		BUSINESS_TYPE_ID = bUSINESS_TYPE_ID;
	}
	public String getBUSINESS_TYPE_NAME() {
		return BUSINESS_TYPE_NAME;
	}
	public void setBUSINESS_TYPE_NAME(String bUSINESS_TYPE_NAME) {
		BUSINESS_TYPE_NAME = bUSINESS_TYPE_NAME;
	}
	public List getUnselectedBankList() {
		return unselectedBankList;
	}
	public void setUnselectedBankList(List unselectedBankList) {
		this.unselectedBankList = unselectedBankList;
	}
	public List getSelectedBankList() {
		return selectedBankList;
	}
	public void setSelectedBankList(List selectedBankList) {
		this.selectedBankList = selectedBankList;
	}
	public String[] getUnselectedBankArray() {
		return unselectedBankArray;
	}
	public void setUnselectedBankArray(String[] unselectedBankArray) {
		this.unselectedBankArray = unselectedBankArray;
	}
	public String[] getSelectedBankArray() {
		return selectedBankArray;
	}
	public void setSelectedBankArray(String[] selectedBankArray) {
		this.selectedBankArray = selectedBankArray;
	}
	public String getJson_unselectedBank() {
		return json_unselectedBank;
	}
	public void setJson_unselectedBank(String json_unselectedBank) {
		this.json_unselectedBank = json_unselectedBank;
	}
	public String getJson_selectedBank() {
		return json_selectedBank;
	}
	public void setJson_selectedBank(String json_selectedBank) {
		this.json_selectedBank = json_selectedBank;
	}
	
}
