package tw.org.twntch.form;

import java.util.List;

public class Rptst_13_Form extends CommonForm {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6866154317082944535L;
	private List opbkIdList ;//操作行清單
	private String START_DATE ; //營業日期(起)
	private String END_DATE;	//營業日期(迄)
	private String CLEARINGPHASE ; //清算階段
	private String OPBK ; //操作行名稱
	private String OPBK_ID; //操作行代號
	private String dow_token ; //檔案下載判別用
	
	public List getOpbkIdList() {
		return opbkIdList;
	}
	public void setOpbkIdList(List opbkIdList) {
		this.opbkIdList = opbkIdList;
	}
	public String getSTART_DATE() {
		return START_DATE;
	}
	public void setSTART_DATE(String sTART_DATE) {
		START_DATE = sTART_DATE;
	}
	public String getEND_DATE() {
		return END_DATE;
	}
	public void setEND_DATE(String eND_DATE) {
		END_DATE = eND_DATE;
	}
	public String getCLEARINGPHASE() {
		return CLEARINGPHASE;
	}
	public void setCLEARINGPHASE(String cLEARINGPHASE) {
		CLEARINGPHASE = cLEARINGPHASE;
	}
	public String getOPBK() {
		return OPBK;
	}
	public void setOPBK(String oPBK) {
		OPBK = oPBK;
	}
	public String getOPBK_ID() {
		return OPBK_ID;
	}
	public void setOPBK_ID(String oPBK_ID) {
		OPBK_ID = oPBK_ID;
	}
	public String getDow_token() {
		return dow_token;
	}
	public void setDow_token(String dow_token) {
		this.dow_token = dow_token;
	}
}
