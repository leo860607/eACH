package tw.org.twntch.form;

import java.util.List;

public class Rpttx_2_Form extends CommonForm {
	
	private List opt_bankList ;
	private List bgbkIdList ;//操作行所代理的總行清單
	private String BIZDATE ; //營業日期
	private String opt_bank ; //操作行名稱
	private String opt_id ; //操作行代號
	private String opt_type ; //操作行角色 0=發動行 ,1=扣款行,2=入帳行
	private String dow_token ; //檔案下載判別用
	private String TXDT ; //日期時間
	private String CLEARINGPHASE ; //清算階段
	private String SENDERACQUIRE ; //操作行代號
	private String BGBK_ID ;
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
	public String getBIZDATE() {
		return BIZDATE;
	}
	public void setBIZDATE(String bIZDATE) {
		BIZDATE = bIZDATE;
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
	public String getOpt_type() {
		return opt_type;
	}
	public void setOpt_type(String opt_type) {
		this.opt_type = opt_type;
	}
	public String getDow_token() {
		return dow_token;
	}
	public void setDow_token(String dow_token) {
		this.dow_token = dow_token;
	}
	public String getTXDT() {
		return TXDT;
	}
	public void setTXDT(String tXDT) {
		TXDT = tXDT;
	}
	public String getCLEARINGPHASE() {
		return CLEARINGPHASE;
	}
	public void setCLEARINGPHASE(String cLEARINGPHASE) {
		CLEARINGPHASE = cLEARINGPHASE;
	}
	public String getSENDERACQUIRE() {
		return SENDERACQUIRE;
	}
	public void setSENDERACQUIRE(String sENDERACQUIRE) {
		SENDERACQUIRE = sENDERACQUIRE;
	}
	public String getBGBK_ID() {
		return BGBK_ID;
	}
	public void setBGBK_ID(String bGBK_ID) {
		BGBK_ID = bGBK_ID;
	}
	
	
	
	
}
