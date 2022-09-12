package tw.org.twntch.form;

import java.util.List;

public class Txn_Returnday_Form extends CommonForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1645991647397136256L;
	private	String	TXN_ID	;
	private	String	STXN_ID	;
	private	String	RETURN_DAY	;
	private	String	ACTIVE_DATE	;
	private List	txnIdList	;
	private	List	scaseary	;
	private boolean	IS_EDITABLE	;
	
	public String getTXN_ID() {
		return TXN_ID;
	}
	public void setTXN_ID(String tXN_ID) {
		TXN_ID = tXN_ID;
	}
	
	
	
	public String getSTXN_ID() {
		return STXN_ID;
	}
	public void setSTXN_ID(String sTXN_ID) {
		STXN_ID = sTXN_ID;
	}
	public String getRETURN_DAY() {
		return RETURN_DAY;
	}
	public void setRETURN_DAY(String rETURN_DAY) {
		RETURN_DAY = rETURN_DAY;
	}
	public String getACTIVE_DATE() {
		return ACTIVE_DATE;
	}
	public void setACTIVE_DATE(String aCTIVE_DATE) {
		ACTIVE_DATE = aCTIVE_DATE;
	}
	public List getTxnIdList() {
		return txnIdList;
	}
	public void setTxnIdList(List txnIdList) {
		this.txnIdList = txnIdList;
	}
	public List getScaseary() {
		return scaseary;
	}
	public void setScaseary(List scaseary) {
		this.scaseary = scaseary;
	}
	public boolean isIS_EDITABLE() {
		return IS_EDITABLE;
	}
	public void setIS_EDITABLE(boolean iS_EDITABLE) {
		IS_EDITABLE = iS_EDITABLE;
	}
	
}
