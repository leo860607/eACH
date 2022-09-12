package tw.org.twntch.form;

import java.util.List;

public class Rpttx_4_Form extends CommonForm {
	
	private List opt_bankList ;//操作行清單
	private List bgbkIdList ;//操作行所代理的總行清單
	private String START_DATE ; //營業日期(起)
	private String END_DATE;	//營業日期(迄)
	private String FLBATCHSEQ;
	private String opt_bank ; //操作行名稱
	private String opt_id ; //操作行代號
	private String dow_token ; //檔案下載判別用
	private String CLEARINGPHASE ; //清算階段
	private String TXDT ; //交易日期
	private String SENDERACQUIRE ;
	private String BGBK_ID ;
	
	
	public String getBGBK_ID() {
		return BGBK_ID;
	}
	public void setBGBK_ID(String bGBK_ID) {
		BGBK_ID = bGBK_ID;
	}
	public List getOpt_bankList() {
		return opt_bankList;
	}
	public void setOpt_bankList(List opt_bankList) {
		this.opt_bankList = opt_bankList;
	}
	public List getBgbkIdList() {
		return bgbkIdList;
	}
	public void setBgbkIdList(List bgbkIdList) {
		this.bgbkIdList = bgbkIdList;
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
	public String getFLBATCHSEQ() {
		return FLBATCHSEQ;
	}
	public void setFLBATCHSEQ(String fLBATCHSEQ) {
		FLBATCHSEQ = fLBATCHSEQ;
	}
	public String getOpt_bank() {
		return opt_bank;
	}
	public void setOpt_bank(String opt_bank) {
		this.opt_bank = opt_bank;
	}
	public String getOpt_id() {
		return opt_id;
	}
	public void setOpt_id(String opt_id) {
		this.opt_id = opt_id;
	}
	public String getDow_token() {
		return dow_token;
	}
	public void setDow_token(String dow_token) {
		this.dow_token = dow_token;
	}
	public String getCLEARINGPHASE() {
		return CLEARINGPHASE;
	}
	public void setCLEARINGPHASE(String cLEARINGPHASE) {
		CLEARINGPHASE = cLEARINGPHASE;
	}
	public String getTXDT() {
		return TXDT;
	}
	public void setTXDT(String tXDT) {
		TXDT = tXDT;
	}
	public String getSENDERACQUIRE() {
		return SENDERACQUIRE;
	}
	public void setSENDERACQUIRE(String sENDERACQUIRE) {
		SENDERACQUIRE = sENDERACQUIRE;
	}
	
	
}
