package tw.org.twntch.form;

import java.util.List;

public class Chg_Mac_Form extends CommonForm{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2108227712688362728L;
	private String BGBK_ID = "";
	private String OPBK_ID = "";
	private String TXDATE = "";
	private String IDFIELD = "";
	private String CHG_PCODE = "1200";
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
	public String getIDFIELD() {
		return IDFIELD;
	}
	public void setIDFIELD(String iDFIELD) {
		IDFIELD = iDFIELD;
	}
	
	public String getCHG_PCODE() {
		return CHG_PCODE;
	}
	public void setCHG_PCODE(String cHG_PCODE) {
		CHG_PCODE = cHG_PCODE;
	}
	public List getBgbkIdList() {
		return bgbkIdList;
	}
	public void setBgbkIdList(List bgbkIdList) {
		this.bgbkIdList = bgbkIdList;
	}
	public String getOPBK_ID() {
		return OPBK_ID;
	}
	public void setOPBK_ID(String oPBK_ID) {
		OPBK_ID = oPBK_ID;
	}
	
	
}
