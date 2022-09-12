package tw.org.twntch.form;

import java.util.List;

public class Async_Mac_Form extends CommonForm{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2496974856330316671L;
	private String OPBK_ID = "";
	private String TXDATE = "";
	private	List opbkIdList;
	public String getOPBK_ID() {
		return OPBK_ID;
	}
	public void setOPBK_ID(String oPBK_ID) {
		OPBK_ID = oPBK_ID;
	}
	public String getTXDATE() {
		return TXDATE;
	}
	public void setTXDATE(String tXDATE) {
		TXDATE = tXDATE;
	}
	public List getOpbkIdList() {
		return opbkIdList;
	}
	public void setOpbkIdList(List opbkIdList) {
		this.opbkIdList = opbkIdList;
	}
	
}
