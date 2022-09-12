package tw.org.twntch.bean;

import java.math.BigDecimal;

public class TXS_DAY {
	private String PCODE;	//交易代碼
	private String SENDERACQUIRE;	//操作行
	private String SENDERHEAD;		//總行代號
	private String RESULTSTATUS;	//處理結果
	private Integer FIRECOUNT;	//發動單位筆數
	private BigDecimal FIREAMT;	//發動單位金額
	private Integer DEBITCOUNT;	//扣款單位筆數
	private BigDecimal DEBITAMT;	//扣款單位金額
	private Integer SAVECOUNT;	//入帳單位筆數
	private BigDecimal SAVEAMT;	//入帳單位金額
	private String BANKHEAD;
	public String getPCODE() {
		return PCODE;
	}
	public void setPCODE(String pCODE) {
		PCODE = pCODE;
	}
	public String getSENDERACQUIRE() {
		return SENDERACQUIRE;
	}
	public void setSENDERACQUIRE(String sENDERACQUIRE) {
		SENDERACQUIRE = sENDERACQUIRE;
	}
	public String getRESULTSTATUS() {
		return RESULTSTATUS;
	}
	public void setRESULTSTATUS(String rESULTSTATUS) {
		RESULTSTATUS = rESULTSTATUS;
	}
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
	public String getSENDERHEAD() {
		return SENDERHEAD;
	}
	public void setSENDERHEAD(String sENDERHEAD) {
		SENDERHEAD = sENDERHEAD;
	}
	public String getBANKHEAD() {
		return BANKHEAD;
	}
	public void setBANKHEAD(String bANKHEAD) {
		BANKHEAD = bANKHEAD;
	}
	
}
