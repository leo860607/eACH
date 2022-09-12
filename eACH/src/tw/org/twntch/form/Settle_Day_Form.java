package tw.org.twntch.form;

import java.util.List;

public class Settle_Day_Form extends CommonForm {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1485363372371963925L;
	private String DATE;//日期
	private String PCODE;//交易類別
	private String OPBK_ID;//操作行
	private String BGBK_ID;//總行代號
	private String CLEARINGPHASE;//清算階段
	private List opbkIdList;//交易類別下拉選單
	private List pcodeList;//操作行下拉選單
	private String dow_token ; //檔案下載判別用
	
	public String getDATE() {
		return DATE;
	}
	public void setDATE(String dATE) {
		DATE = dATE;
	}
	public String getOPBK_ID() {
		return OPBK_ID;
	}
	public void setOPBK_ID(String oPBK_ID) {
		OPBK_ID = oPBK_ID;
	}
	public List getOpbkIdList() {
		return opbkIdList;
	}
	public void setOpbkIdList(List opbkIdList) {
		this.opbkIdList = opbkIdList;
	}
	public String getPCODE() {
		return PCODE;
	}
	public void setPCODE(String pCODE) {
		PCODE = pCODE;
	}
	public String getCLEARINGPHASE() {
		return CLEARINGPHASE;
	}
	public void setCLEARINGPHASE(String cLEARINGPHASE) {
		CLEARINGPHASE = cLEARINGPHASE;
	}
	public List getPcodeList() {
		return pcodeList;
	}
	public void setPcodeList(List pcodeList) {
		this.pcodeList = pcodeList;
	}
	public String getBGBK_ID() {
		return BGBK_ID;
	}
	public void setBGBK_ID(String bGBK_ID) {
		BGBK_ID = bGBK_ID;
	}
	public String getDow_token() {
		return dow_token;
	}
	public void setDow_token(String dow_token) {
		this.dow_token = dow_token;
	}
}
