package tw.org.twntch.form;

import java.util.List;

public class Each_Batch_Def_Form extends CommonForm{
	
	private	List	scaseary	;
	
	private	String	BATCH_PROC_SEQ	;
	private	String 	BATCH_PROC_DESC	;
	private	String 	BATCH_PROC_NAME	;
	private	String  ERR_BREAK = "Y"	;
	private	String  PROC_TYPE ;
	
	
	public List getScaseary() {
		return scaseary;
	}
	public void setScaseary(List scaseary) {
		this.scaseary = scaseary;
	}
	public String getBATCH_PROC_SEQ() {
		return BATCH_PROC_SEQ;
	}
	public void setBATCH_PROC_SEQ(String bATCH_PROC_SEQ) {
		BATCH_PROC_SEQ = bATCH_PROC_SEQ;
	}
	public String getBATCH_PROC_DESC() {
		return BATCH_PROC_DESC;
	}
	public void setBATCH_PROC_DESC(String bATCH_PROC_DESC) {
		BATCH_PROC_DESC = bATCH_PROC_DESC;
	}
	public String getBATCH_PROC_NAME() {
		return BATCH_PROC_NAME;
	}
	public void setBATCH_PROC_NAME(String bATCH_PROC_NAME) {
		BATCH_PROC_NAME = bATCH_PROC_NAME;
	}
	public String getERR_BREAK() {
		return ERR_BREAK;
	}
	public void setERR_BREAK(String eRR_BREAK) {
		ERR_BREAK = eRR_BREAK;
	}
	public String getPROC_TYPE() {
		return PROC_TYPE;
	}
	public void setPROC_TYPE(String pROC_TYPE) {
		PROC_TYPE = pROC_TYPE;
	}

	
	
}
