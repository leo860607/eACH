package tw.org.twntch.form;

import java.util.List;

public class Turnon_Form extends CommonForm{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2108227712688362728L;
	private String BGBK_ID = "";
	private String TXDATE = "";
	private	List bgbkIdList;
	
	public String getBGBK_ID() {
		return BGBK_ID;
	}
	public void setBGBK_ID(String bGBK_ID) {
		BGBK_ID = bGBK_ID;
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
}
