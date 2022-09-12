package tw.org.twntch.form;

import java.util.List;

public class Msg_Notice_Form extends CommonForm{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2660281177423438063L;
	private String BGBK_ID = "";
	private String MESSAGE = "";
	private String NOTICEID = "";
	private String TXDATE = "";
	private	List bgbkIdList;
	public String getBGBK_ID() {
		return BGBK_ID;
	}
	public void setBGBK_ID(String bGBK_ID) {
		BGBK_ID = bGBK_ID;
	}
	public String getMESSAGE() {
		return MESSAGE;
	}
	public void setMESSAGE(String mESSAGE) {
		MESSAGE = mESSAGE;
	}
	public String getTXDATE() {
		return TXDATE;
	}
	public void setTXDATE(String tXDATE) {
		TXDATE = tXDATE;
	}
	public List getBgbkIdList() {
		return bgbkIdList;
	}
	public void setBgbkIdList(List bgbkIdList) {
		this.bgbkIdList = bgbkIdList;
	}
	public String getNOTICEID() {
		return NOTICEID;
	}
	public void setNOTICEID(String nOTICEID) {
		NOTICEID = nOTICEID;
	}
	
	
	
	
}
