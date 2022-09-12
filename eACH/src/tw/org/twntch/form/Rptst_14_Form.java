package tw.org.twntch.form;

import java.util.List;

public class Rptst_14_Form extends CommonForm {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1513582436396879120L;
	private List opbkIdList ;//操作行清單
	private String TW_YEAR;
	private String START_TW_MONTH;
	private String END_TW_MONTH;
	private String OPBK_ID; //操作行代號
	private String OPBK;
	private String dow_token ; //檔案下載判別用
	
	public List getOpbkIdList() {
		return opbkIdList;
	}
	public void setOpbkIdList(List opbkIdList) {
		this.opbkIdList = opbkIdList;
	}
	public String getTW_YEAR() {
		return TW_YEAR;
	}
	public void setTW_YEAR(String tW_YEAR) {
		TW_YEAR = tW_YEAR;
	}
	public String getSTART_TW_MONTH() {
		return START_TW_MONTH;
	}
	public void setSTART_TW_MONTH(String sTART_TW_MONTH) {
		START_TW_MONTH = sTART_TW_MONTH;
	}
	public String getEND_TW_MONTH() {
		return END_TW_MONTH;
	}
	public void setEND_TW_MONTH(String eND_TW_MONTH) {
		END_TW_MONTH = eND_TW_MONTH;
	}
	public String getOPBK_ID() {
		return OPBK_ID;
	}
	public void setOPBK_ID(String oPBK_ID) {
		OPBK_ID = oPBK_ID;
	}
	public String getOPBK() {
		return OPBK;
	}
	public void setOPBK(String oPBK) {
		OPBK = oPBK;
	}
	public String getDow_token() {
		return dow_token;
	}
	public void setDow_token(String dow_token) {
		this.dow_token = dow_token;
	}
}
