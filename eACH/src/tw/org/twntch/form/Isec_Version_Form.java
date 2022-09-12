package tw.org.twntch.form;

import java.util.List;

public class Isec_Version_Form extends CommonForm {
	private	List	scaseary;
	private String	SEQ_ID;
	private String	ISEC_VERSION;
	
	public List getScaseary() {
		return scaseary;
	}
	public void setScaseary(List scaseary) {
		this.scaseary = scaseary;
	}	
	public String getSEQ_ID() {
		return SEQ_ID;
	}
	public void setSEQ_ID(String sEQ_ID) {
		SEQ_ID = sEQ_ID;
	}
	public String getISEC_VERSION() {
		return ISEC_VERSION;
	}
	public void setISEC_VERSION(String iSEC_VERSION) {
		ISEC_VERSION = iSEC_VERSION;
	}
	
	public void resetFields(){
		scaseary = null;
		SEQ_ID = null;
	}
}
