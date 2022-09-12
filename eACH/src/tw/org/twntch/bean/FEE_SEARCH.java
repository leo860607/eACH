package tw.org.twntch.bean;

import java.math.BigDecimal;

public class FEE_SEARCH {
	private String BGBK_ID_NAME;
	private String BANKID;	//分行代號
	private String PCODE;	//交易代碼
	//private String SENDERACQUIRE;	//操作行
	//private String SENDERBANKID;	//發動單位
	private Integer FIRECOUNT;	//發動單位筆數
	private BigDecimal FIREAMT;	//發動單位金額
	private Integer DEBITCOUNT;	//扣款單位筆數
	private BigDecimal DEBITAMT;	//扣款單位金額
	private Integer SAVECOUNT;	//入帳單位筆數
	private BigDecimal SAVEAMT;	//入帳單位金額
	private Integer CANCELCOUNT;	//入帳單位筆數
	private BigDecimal CANCELAMT;	//入帳單位金額
	private String BGBK_ID;
	private String BGBK_NAME;
	private String OPBK_ID;
	private String OPBK_NAME;
	private String ACCTCODE;	
	
	public String getBGBK_ID_NAME() {
		return BGBK_ID_NAME;
	}
	public void setBGBK_ID_NAME(String bGBK_ID_NAME) {
		BGBK_ID_NAME = bGBK_ID_NAME;
	}
	public String getBANKID() {
		return BANKID;
	}
	public void setBANKID(String bANKID) {
		BANKID = bANKID;
	}
	public String getPCODE() {
		return PCODE;
	}
	public void setPCODE(String pCODE) {
		PCODE = pCODE;
	}
//	public String getSENDERACQUIRE() {
//		return SENDERACQUIRE;
//	}
//	public void setSENDERACQUIRE(String sENDERACQUIRE) {
//		SENDERACQUIRE = sENDERACQUIRE;
//	}
//	public String getSENDERBANKID() {
//		return SENDERBANKID;
//	}
//	public void setSENDERBANKID(String sENDERBANKID) {
//		SENDERBANKID = sENDERBANKID;
//	}
	public Integer getFIRECOUNT() {
		return FIRECOUNT;
	}
	public void setFIRECOUNT(Integer fIRECOUNT) {
		FIRECOUNT = fIRECOUNT;
	}
	public BigDecimal getFIREAMT() {
		return FIREAMT;
	}
	public void setFIREAMT(BigDecimal fIREAMT) {
		FIREAMT = fIREAMT;
	}
	public Integer getDEBITCOUNT() {
		return DEBITCOUNT;
	}
	public void setDEBITCOUNT(Integer dEBITCOUNT) {
		DEBITCOUNT = dEBITCOUNT;
	}
	public BigDecimal getDEBITAMT() {
		return DEBITAMT;
	}
	public void setDEBITAMT(BigDecimal dEBITAMT) {
		DEBITAMT = dEBITAMT;
	}
	public Integer getSAVECOUNT() {
		return SAVECOUNT;
	}
	public void setSAVECOUNT(Integer sAVECOUNT) {
		SAVECOUNT = sAVECOUNT;
	}
	public BigDecimal getSAVEAMT() {
		return SAVEAMT;
	}
	public void setSAVEAMT(BigDecimal sAVEAMT) {
		SAVEAMT = sAVEAMT;
	}
	public String getBGBK_ID() {
		return BGBK_ID;
	}
	public void setBGBK_ID(String bGBK_ID) {
		BGBK_ID = bGBK_ID;
	}
	public String getBGBK_NAME() {
		return BGBK_NAME;
	}
	public void setBGBK_NAME(String bGBK_NAME) {
		BGBK_NAME = bGBK_NAME;
	}
	public String getOPBK_ID() {
		return OPBK_ID;
	}
	public void setOPBK_ID(String oPBK_ID) {
		OPBK_ID = oPBK_ID;
	}
	public String getOPBK_NAME() {
		return OPBK_NAME;
	}
	public void setOPBK_NAME(String oPBK_NAME) {
		OPBK_NAME = oPBK_NAME;
	}
	public String getACCTCODE() {
		return ACCTCODE;
	}
	public void setACCTCODE(String aCCTCODE) {
		ACCTCODE = aCCTCODE;
	}
	public Integer getCANCELCOUNT() {
		return CANCELCOUNT;
	}
	public void setCANCELCOUNT(Integer cANCELCOUNT) {
		CANCELCOUNT = cANCELCOUNT;
	}
	public BigDecimal getCANCELAMT() {
		return CANCELAMT;
	}
	public void setCANCELAMT(BigDecimal cANCELAMT) {
		CANCELAMT = cANCELAMT;
	}
	
}
