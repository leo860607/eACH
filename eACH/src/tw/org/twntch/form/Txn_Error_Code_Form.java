package tw.org.twntch.form;

import java.util.List;

public class Txn_Error_Code_Form extends CommonForm{
	private	List	scaseary	;
	private	List	idList	;
	private	String	jsonList	;
	private String ERROR_ID ;
	private String ERR_DESC ;
	private String BATCH_ERR_ID ;
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
	public String getERROR_ID() {
		return ERROR_ID;
	}
	public void setERROR_ID(String eRROR_ID) {
		ERROR_ID = eRROR_ID;
	}
	public String getERR_DESC() {
		return ERR_DESC;
	}
	public void setERR_DESC(String eRR_DESC) {
		ERR_DESC = eRR_DESC;
	}
	public String getBATCH_ERR_ID() {
		return BATCH_ERR_ID;
	}
	public void setBATCH_ERR_ID(String bATCH_ERR_ID) {
		BATCH_ERR_ID = bATCH_ERR_ID;
	}

	
	
}
