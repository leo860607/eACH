package tw.org.twntch.form;

import java.util.List;

public class Agent_Notice_Form extends CommonForm{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2660281177423438063L;
	private String ABBR_ID = "";
	private String MESSAGE = "";
	private String NOTICEID = "";
	private String TXDATE = "";
	private	List abbrIdList;
	public String getABBR_ID() {
		return ABBR_ID;
	}
	public void setABBR_ID(String ABBR_ID) {
		this.ABBR_ID = ABBR_ID;
	}
	public String getMESSAGE() {
		return MESSAGE;
	}
	public void setMESSAGE(String MESSAGE) {
		this.MESSAGE = MESSAGE;
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
	public String getNOTICEID() {
		return NOTICEID;
	}
	public void setNOTICEID(String nOTICEID) {
		NOTICEID = nOTICEID;
	}
	
	
	
	
}
