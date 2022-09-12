package tw.org.twntch.form;

import java.util.List;

public class Agent_Turnoff_Form extends CommonForm{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2108227712688362728L;
	private String ABBR_ID = "";
	private String TXDATE = "";
	private	List abbrIdList;
	
	public String getABBR_ID() {
		return ABBR_ID;
	}
	public void setABBR_ID(String ABBR_ID) {
		this.ABBR_ID = ABBR_ID;
	}
	public String getTXDATE() {
		return TXDATE;
	}
	public void setTXDATE(String TXDATE) {
		this.TXDATE = TXDATE;
	}
	public List getAbbrIdList() {
		return abbrIdList;
	}
	public void setAbbrIdList(List abbrIdList) {
		this.abbrIdList = abbrIdList;
	}
}
