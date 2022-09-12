package tw.org.twntch.form;

import java.util.List;

public class Rptfee_4_Form extends CommonForm {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5825038820325168674L;
	private String TW_YEAR;
	private String TW_MONTH;
	private String OPBK_ID;
	private String BGBK_ID;
	private String BRBK_ID;
	private List bgbkIdList;
	private List opbkIdList;
	private String dow_token ; //檔案下載判別用
	
	public String getTW_YEAR() {
		return TW_YEAR;
	}
	public void setTW_YEAR(String tW_YEAR) {
		TW_YEAR = tW_YEAR;
	}
	public String getTW_MONTH() {
		return TW_MONTH;
	}
	public void setTW_MONTH(String tW_MONTH) {
		TW_MONTH = tW_MONTH;
	}
	public String getOPBK_ID() {
		return OPBK_ID;
	}
	public void setOPBK_ID(String oPBK_ID) {
		OPBK_ID = oPBK_ID;
	}
	public String getBGBK_ID() {
		return BGBK_ID;
	}
	public void setBGBK_ID(String bGBK_ID) {
		BGBK_ID = bGBK_ID;
	}
	public String getBRBK_ID() {
		return BRBK_ID;
	}
	public void setBRBK_ID(String bRBK_ID) {
		BRBK_ID = bRBK_ID;
	}
	public List getBgbkIdList() {
		return bgbkIdList;
	}
	public void setBgbkIdList(List bgbkIdList) {
		this.bgbkIdList = bgbkIdList;
	}
	public List getOpbkIdList() {
		return opbkIdList;
	}
	public void setOpbkIdList(List opbkIdList) {
		this.opbkIdList = opbkIdList;
	}
	public String getDow_token() {
		return dow_token;
	}
	public void setDow_token(String dow_token) {
		this.dow_token = dow_token;
	}
	
}
