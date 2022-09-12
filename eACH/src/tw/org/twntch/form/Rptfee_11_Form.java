package tw.org.twntch.form;

import java.util.List;

public class Rptfee_11_Form extends CommonForm {
	
	private List opt_bankList ;//操作行清單
	private List bgbkIdList ;//操作行所代理的總行清單
	private	List idList	; ;//交易代號清單
	private String BIZDATE ; //營業日期
	private String opt_bank ; //操作行名稱
	private String opt_id ; //操作行代號
	private String opt_type ; //操作行角色 0=發動行 ,1=扣款行,2=入帳行
	private String dow_token ; //檔案下載判別用
	private String CLEARINGPHASE ; //清算階段
	private String TXDT ; //交易日期
	private String SENDERACQUIRE ;
	private String BGBK_ID ;
	private	String	TXN_ID	;
	private	String	SENDERID	; // 發動者統編
	private	String	RESULTSTATUS	; // 交易結果
	private	String	FEE_TYPE	; // 手續費類型     A:內含 B:外加 C:百分比 D:級距
	private	String	TXTYPE	; // 交易結果類型 A:一般 B:沖正
	
	
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
	public List getIdList() {
		return idList;
	}
	public void setIdList(List idList) {
		this.idList = idList;
	}
	public String getTXN_ID() {
		return TXN_ID;
	}
	public void setTXN_ID(String tXN_ID) {
		TXN_ID = tXN_ID;
	}
	public String getSENDERID() {
		return SENDERID;
	}
	public void setSENDERID(String sENDERID) {
		SENDERID = sENDERID;
	}
	public String getRESULTSTATUS() {
		return RESULTSTATUS;
	}
	public void setRESULTSTATUS(String rESULTSTATUS) {
		RESULTSTATUS = rESULTSTATUS;
	}
	public String getFEE_TYPE() {
		return FEE_TYPE;
	}
	public void setFEE_TYPE(String fEE_TYPE) {
		FEE_TYPE = fEE_TYPE;
	}
	public String getTXTYPE() {
		return TXTYPE;
	}
	public void setTXTYPE(String tXTYPE) {
		TXTYPE = tXTYPE;
	}
	
	
}
