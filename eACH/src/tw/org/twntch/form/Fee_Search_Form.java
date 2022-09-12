package tw.org.twntch.form;

import java.util.List;

public class Fee_Search_Form extends CommonForm {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4383416770867964938L;
	private String START_DATE;
	private String END_DATE;
	private String PCODE;
	private String OPBK_ID;
	private String CLEARINGPHASE;
	private String BGBK_ID;
	private String BRBK_ID;
	private List scaseary;
	private List opbkIdList;
	private List bgbkIdList;
	private List brbkIdList;
	private List pcodeList;
	private String CLEARINGPHASE_PRE;
	private String dow_token ; //檔案下載判別用
	
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
	public String getPCODE() {
		return PCODE;
	}
	public void setPCODE(String pCODE) {
		PCODE = pCODE;
	}
	public String getOPBK_ID() {
		return OPBK_ID;
	}
	public void setOPBK_ID(String oPBK_ID) {
		OPBK_ID = oPBK_ID;
	}
	public String getCLEARINGPHASE() {
		return CLEARINGPHASE;
	}
	public void setCLEARINGPHASE(String cLEARINGPHASE) {
		CLEARINGPHASE = cLEARINGPHASE;
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
	public List getScaseary() {
		return scaseary;
	}
	public void setScaseary(List scaseary) {
		this.scaseary = scaseary;
	}
	public List getOpbkIdList() {
		return opbkIdList;
	}
	public void setOpbkIdList(List opbkIdList) {
		this.opbkIdList = opbkIdList;
	}
	public List getBgbkIdList() {
		return bgbkIdList;
	}
	public void setBgbkIdList(List bgbkIdList) {
		this.bgbkIdList = bgbkIdList;
	}
	public List getBrbkIdList() {
		return brbkIdList;
	}
	public void setBrbkIdList(List brbkIdList) {
		this.brbkIdList = brbkIdList;
	}
	public List getPcodeList() {
		return pcodeList;
	}
	public void setPcodeList(List pcodeList) {
		this.pcodeList = pcodeList;
	}
	public String getDow_token() {
		return dow_token;
	}
	public void setDow_token(String dow_token) {
		this.dow_token = dow_token;
	}
	public String getCLEARINGPHASE_PRE() {
		return CLEARINGPHASE_PRE;
	}
	public void setCLEARINGPHASE_PRE(String cLEARINGPHASE_NOW) {
		CLEARINGPHASE_PRE = cLEARINGPHASE_NOW;
	}
}
