package tw.org.twntch.po;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity (name = "tw.org.twntch.po.ONCLEARINGTAB")
@Table(name = "ONCLEARINGTAB")
public class ONCLEARINGTAB implements Serializable {
	/**
	 * private	CHAR    	BANKID	;
private	CHAR    	BIZDATE	;
private	CHAR    	CLEARINGPHASE	;
private	BIGINT  	PAYAMT	;
private	INTEGER 	PAYCNT	;
private	DECIMAL 	PAYEACHFEEAMT	;
private	INTEGER 	PAYEACHFEECNT	;
private	DECIMAL 	PAYMBFEEAMT	;
private	INTEGER 	PAYMBFEECNT	;
private	CHAR    	PCODE	;
private	BIGINT  	RECVAMT	;
private	INTEGER 	RECVCNT	;
private	DECIMAL 	RECVMBFEEAMT	;
private	INTEGER 	RECVMBFEECNT	;
private	BIGINT  	RVSPAYAMT	;
private	INTEGER 	RVSPAYCNT	;
private	DECIMAL 	RVSPAYMBFEEAMT	;
private	INTEGER 	RVSPAYMBFEECNT	;
private	BIGINT  	RVSRECVAMT	;
private	INTEGER 	RVSRECVCNT	;
private	DECIMAL 	RVSRECVEACHFEEAMT	;
private	INTEGER 	RVSRECVEACHFEECNT	;
private	DECIMAL 	RVSRECVMBFEEAMT	;
private	INTEGER 	RVSRECVMBFEECNT	;
private	CHAR    	TYPHBIZDATE	;
private	CHAR    	TYPHCLEARINGPHASE	;
	 */
	private static final long serialVersionUID = 7820369674264143170L;
	@EmbeddedId
	private ONCLEARINGTAB_PK id;
	@Transient
	private String BIZDATE;
	@Transient
	private String CLEARINGPHASE;
	@Transient
	private String BANKID;
	@Transient
	private String PCODE;
	
	private Integer RECVCNT;
	private BigInteger RECVAMT;
	private Integer PAYCNT;
	private BigInteger PAYAMT;
	private Integer RECVMBFEECNT;
	private BigDecimal RECVMBFEEAMT;
	private Integer PAYMBFEECNT;
	private BigDecimal PAYMBFEEAMT;
	private Integer PAYEACHFEECNT;
	private BigDecimal PAYEACHFEEAMT;
	private Integer RVSRECVCNT;
	private BigInteger RVSRECVAMT;
	private Integer RVSPAYCNT;
	private BigInteger RVSPAYAMT;
	private Integer RVSRECVMBFEECNT;
	private BigDecimal RVSRECVMBFEEAMT;
	private Integer RVSRECVEACHFEECNT;
	private BigDecimal RVSRECVEACHFEEAMT;
	private Integer RVSPAYMBFEECNT;
	private BigDecimal RVSPAYMBFEEAMT;
	private	String    	TYPHBIZDATE	;
	private	String    	TYPHCLEARINGPHASE	;
	
	//結算統計查詢從BANK_GROUP表抓進來的BGBK_NAME和ONCLEARINGTAB表的BANKID組合而成
	@Transient
	private String BANKIDANDNAME;
	//結算統計查詢從EACH_TXN_CODE表抓進來的EACH_TXN_NAME和ONCLEARINGTAB表的PCODE組合而成
	@Transient
	private String PCODEANDNAME;
	@Transient
	private String format_BIZDATE;
	@Transient
	private String format_CLEARINGPHASE;
	//For 結算資料查詢
	@Transient
	private String TOTAL_RECV_PAY_CNT;
	@Transient
	private String TOTAL_RECV_PAY_AMT;
	@Transient
	private String TOTAL_RECV_PAY_FEE_CNT;
	@Transient
	private String TOTAL_RECV_PAY_FEE_AMT;
	@Transient
	private String ALL_TOTAL_RECV_PAY_CNT;
	@Transient
	private String ALL_TOTAL_RECV_PAY_AMT;
	@Transient
	private String ALL_TOTAL_RECV_PAY_FEE_CNT;
	@Transient
	private String ALL_TOTAL_RECV_PAY_FEE_AMT;
	
	@EmbeddedId
	public ONCLEARINGTAB_PK getId() {
		return id;
	}
	public void setId(ONCLEARINGTAB_PK id) {
		this.id = id;
	}
	@Transient
	public String getBIZDATE() {
		return BIZDATE;
	}
	public void setBIZDATE(String bIZDATE) {
		BIZDATE = bIZDATE;
	}
	@Transient
	public String getCLEARINGPHASE() {
		return CLEARINGPHASE;
	}
	public void setCLEARINGPHASE(String cLEARINGPHASE) {
		CLEARINGPHASE = cLEARINGPHASE;
	}
	@Transient
	public String getBANKID() {
		return BANKID;
	}
	public void setBANKID(String bANKID) {
		BANKID = bANKID;
	}
	@Transient
	public String getPCODE() {
		return PCODE;
	}
	public void setPCODE(String pCODE) {
		PCODE = pCODE;
	}
	public Integer getRECVCNT() {
		return RECVCNT;
	}
	public void setRECVCNT(Integer rECVCNT) {
		RECVCNT = rECVCNT;
	}
	public BigInteger getRECVAMT() {
		return RECVAMT;
	}
	public void setRECVAMT(BigInteger rECVAMT) {
		RECVAMT = rECVAMT;
	}
	public Integer getPAYCNT() {
		return PAYCNT;
	}
	public void setPAYCNT(Integer pAYCNT) {
		PAYCNT = pAYCNT;
	}
	public BigInteger getPAYAMT() {
		return PAYAMT;
	}
	public void setPAYAMT(BigInteger pAYAMT) {
		PAYAMT = pAYAMT;
	}
	public Integer getRECVMBFEECNT() {
		return RECVMBFEECNT;
	}
	public void setRECVMBFEECNT(Integer rECVMBFEECNT) {
		RECVMBFEECNT = rECVMBFEECNT;
	}
	public BigDecimal getRECVMBFEEAMT() {
		return RECVMBFEEAMT;
	}
	public void setRECVMBFEEAMT(BigDecimal rECVMBFEEAMT) {
		RECVMBFEEAMT = rECVMBFEEAMT;
	}
	public Integer getPAYMBFEECNT() {
		return PAYMBFEECNT;
	}
	public void setPAYMBFEECNT(Integer pAYMBFEECNT) {
		PAYMBFEECNT = pAYMBFEECNT;
	}
	public BigDecimal getPAYMBFEEAMT() {
		return PAYMBFEEAMT;
	}
	public void setPAYMBFEEAMT(BigDecimal pAYMBFEEAMT) {
		PAYMBFEEAMT = pAYMBFEEAMT;
	}
	public Integer getPAYEACHFEECNT() {
		return PAYEACHFEECNT;
	}
	public void setPAYEACHFEECNT(Integer pAYEACHFEECNT) {
		PAYEACHFEECNT = pAYEACHFEECNT;
	}
	public BigDecimal getPAYEACHFEEAMT() {
		return PAYEACHFEEAMT;
	}
	public void setPAYEACHFEEAMT(BigDecimal pAYEACHFEEAMT) {
		PAYEACHFEEAMT = pAYEACHFEEAMT;
	}
	public Integer getRVSRECVCNT() {
		return RVSRECVCNT;
	}
	public void setRVSRECVCNT(Integer rVSRECVCNT) {
		RVSRECVCNT = rVSRECVCNT;
	}
	public BigInteger getRVSRECVAMT() {
		return RVSRECVAMT;
	}
	public void setRVSRECVAMT(BigInteger rVSRECVAMT) {
		RVSRECVAMT = rVSRECVAMT;
	}
	public Integer getRVSPAYCNT() {
		return RVSPAYCNT;
	}
	public void setRVSPAYCNT(Integer rVSPAYCNT) {
		RVSPAYCNT = rVSPAYCNT;
	}
	public BigInteger getRVSPAYAMT() {
		return RVSPAYAMT;
	}
	public void setRVSPAYAMT(BigInteger rVSPAYAMT) {
		RVSPAYAMT = rVSPAYAMT;
	}
	public Integer getRVSRECVMBFEECNT() {
		return RVSRECVMBFEECNT;
	}
	public void setRVSRECVMBFEECNT(Integer rVSRECVMBFEECNT) {
		RVSRECVMBFEECNT = rVSRECVMBFEECNT;
	}
	public BigDecimal getRVSRECVMBFEEAMT() {
		return RVSRECVMBFEEAMT;
	}
	public void setRVSRECVMBFEEAMT(BigDecimal rVSRECVMBFEEAMT) {
		RVSRECVMBFEEAMT = rVSRECVMBFEEAMT;
	}
	public Integer getRVSRECVEACHFEECNT() {
		return RVSRECVEACHFEECNT;
	}
	public void setRVSRECVEACHFEECNT(Integer rVSRECVEACHFEECNT) {
		RVSRECVEACHFEECNT = rVSRECVEACHFEECNT;
	}
	public BigDecimal getRVSRECVEACHFEEAMT() {
		return RVSRECVEACHFEEAMT;
	}
	public void setRVSRECVEACHFEEAMT(BigDecimal rVSRECVEACHFEEAMT) {
		RVSRECVEACHFEEAMT = rVSRECVEACHFEEAMT;
	}
	public Integer getRVSPAYMBFEECNT() {
		return RVSPAYMBFEECNT;
	}
	public void setRVSPAYMBFEECNT(Integer rVSPAYMBFEECNT) {
		RVSPAYMBFEECNT = rVSPAYMBFEECNT;
	}
	public BigDecimal getRVSPAYMBFEEAMT() {
		return RVSPAYMBFEEAMT;
	}
	public void setRVSPAYMBFEEAMT(BigDecimal rVSPAYMBFEEAMT) {
		RVSPAYMBFEEAMT = rVSPAYMBFEEAMT;
	}
	public String getTYPHBIZDATE() {
		return TYPHBIZDATE;
	}
	public void setTYPHBIZDATE(String tYPHBIZDATE) {
		TYPHBIZDATE = tYPHBIZDATE;
	}
	public String getTYPHCLEARINGPHASE() {
		return TYPHCLEARINGPHASE;
	}
	public void setTYPHCLEARINGPHASE(String tYPHCLEARINGPHASE) {
		TYPHCLEARINGPHASE = tYPHCLEARINGPHASE;
	}
	@Transient
	public String getBANKIDANDNAME() {
		return BANKIDANDNAME;
	}
	public void setBANKIDANDNAME(String bANKIDANDNAME) {
		BANKIDANDNAME = bANKIDANDNAME;
	}
	@Transient
	public String getPCODEANDNAME() {
		return PCODEANDNAME;
	}
	public void setPCODEANDNAME(String pCODEANDNAME) {
		PCODEANDNAME = pCODEANDNAME;
	}
	@Transient
	public String getFormat_BIZDATE() {
		return format_BIZDATE;
	}
	public void setFormat_BIZDATE(String format_BIZDATE) {
		this.format_BIZDATE = format_BIZDATE;
	}
	@Transient
	public String getTOTAL_RECV_PAY_CNT() {
		return TOTAL_RECV_PAY_CNT;
	}
	public void setTOTAL_RECV_PAY_CNT(String tOTAL_RECV_PAY_CNT) {
		TOTAL_RECV_PAY_CNT = tOTAL_RECV_PAY_CNT;
	}
	@Transient
	public String getTOTAL_RECV_PAY_AMT() {
		return TOTAL_RECV_PAY_AMT;
	}
	public void setTOTAL_RECV_PAY_AMT(String tOTAL_RECV_PAY_AMT) {
		TOTAL_RECV_PAY_AMT = tOTAL_RECV_PAY_AMT;
	}
	@Transient
	public String getTOTAL_RECV_PAY_FEE_CNT() {
		return TOTAL_RECV_PAY_FEE_CNT;
	}
	public void setTOTAL_RECV_PAY_FEE_CNT(String tOTAL_RECV_PAY_FEE_CNT) {
		TOTAL_RECV_PAY_FEE_CNT = tOTAL_RECV_PAY_FEE_CNT;
	}
	@Transient
	public String getTOTAL_RECV_PAY_FEE_AMT() {
		return TOTAL_RECV_PAY_FEE_AMT;
	}
	public void setTOTAL_RECV_PAY_FEE_AMT(String tOTAL_RECV_PAY_FEE_AMT) {
		TOTAL_RECV_PAY_FEE_AMT = tOTAL_RECV_PAY_FEE_AMT;
	}
	@Transient
	public String getALL_TOTAL_RECV_PAY_CNT() {
		return ALL_TOTAL_RECV_PAY_CNT;
	}
	public void setALL_TOTAL_RECV_PAY_CNT(String aLL_TOTAL_RECV_PAY_CNT) {
		ALL_TOTAL_RECV_PAY_CNT = aLL_TOTAL_RECV_PAY_CNT;
	}
	@Transient
	public String getALL_TOTAL_RECV_PAY_AMT() {
		return ALL_TOTAL_RECV_PAY_AMT;
	}
	public void setALL_TOTAL_RECV_PAY_AMT(String aLL_TOTAL_RECV_PAY_AMT) {
		ALL_TOTAL_RECV_PAY_AMT = aLL_TOTAL_RECV_PAY_AMT;
	}
	@Transient
	public String getALL_TOTAL_RECV_PAY_FEE_CNT() {
		return ALL_TOTAL_RECV_PAY_FEE_CNT;
	}
	public void setALL_TOTAL_RECV_PAY_FEE_CNT(String aLL_TOTAL_RECV_PAY_FEE_CNT) {
		ALL_TOTAL_RECV_PAY_FEE_CNT = aLL_TOTAL_RECV_PAY_FEE_CNT;
	}
	@Transient
	public String getALL_TOTAL_RECV_PAY_FEE_AMT() {
		return ALL_TOTAL_RECV_PAY_FEE_AMT;
	}
	public void setALL_TOTAL_RECV_PAY_FEE_AMT(String aLL_TOTAL_RECV_PAY_FEE_AMT) {
		ALL_TOTAL_RECV_PAY_FEE_AMT = aLL_TOTAL_RECV_PAY_FEE_AMT;
	}
	@Transient
	public String getFormat_CLEARINGPHASE() {
		return format_CLEARINGPHASE;
	}
	public void setFormat_CLEARINGPHASE(String format_CLEARINGPHASE) {
		this.format_CLEARINGPHASE = format_CLEARINGPHASE;
	}
	@Override
	public String toString() {
		return "ONCLEARINGTAB [id=" + id + ", BIZDATE=" + BIZDATE + ", CLEARINGPHASE=" + CLEARINGPHASE + ", BANKID="
				+ BANKID + ", PCODE=" + PCODE + ", RECVCNT=" + RECVCNT + ", RECVAMT=" + RECVAMT + ", PAYCNT=" + PAYCNT
				+ ", PAYAMT=" + PAYAMT + ", RECVMBFEECNT=" + RECVMBFEECNT + ", RECVMBFEEAMT=" + RECVMBFEEAMT
				+ ", PAYMBFEECNT=" + PAYMBFEECNT + ", PAYMBFEEAMT=" + PAYMBFEEAMT + ", PAYEACHFEECNT=" + PAYEACHFEECNT
				+ ", PAYEACHFEEAMT=" + PAYEACHFEEAMT + ", RVSRECVCNT=" + RVSRECVCNT + ", RVSRECVAMT=" + RVSRECVAMT
				+ ", RVSPAYCNT=" + RVSPAYCNT + ", RVSPAYAMT=" + RVSPAYAMT + ", RVSRECVMBFEECNT=" + RVSRECVMBFEECNT
				+ ", RVSRECVMBFEEAMT=" + RVSRECVMBFEEAMT + ", RVSRECVEACHFEECNT=" + RVSRECVEACHFEECNT
				+ ", RVSRECVEACHFEEAMT=" + RVSRECVEACHFEEAMT + ", RVSPAYMBFEECNT=" + RVSPAYMBFEECNT
				+ ", RVSPAYMBFEEAMT=" + RVSPAYMBFEEAMT + ", TYPHBIZDATE=" + TYPHBIZDATE + ", TYPHCLEARINGPHASE="
				+ TYPHCLEARINGPHASE + ", BANKIDANDNAME=" + BANKIDANDNAME + ", PCODEANDNAME=" + PCODEANDNAME
				+ ", format_BIZDATE=" + format_BIZDATE + ", format_CLEARINGPHASE=" + format_CLEARINGPHASE
				+ ", TOTAL_RECV_PAY_CNT=" + TOTAL_RECV_PAY_CNT + ", TOTAL_RECV_PAY_AMT=" + TOTAL_RECV_PAY_AMT
				+ ", TOTAL_RECV_PAY_FEE_CNT=" + TOTAL_RECV_PAY_FEE_CNT + ", TOTAL_RECV_PAY_FEE_AMT="
				+ TOTAL_RECV_PAY_FEE_AMT + ", ALL_TOTAL_RECV_PAY_CNT=" + ALL_TOTAL_RECV_PAY_CNT
				+ ", ALL_TOTAL_RECV_PAY_AMT=" + ALL_TOTAL_RECV_PAY_AMT + ", ALL_TOTAL_RECV_PAY_FEE_CNT="
				+ ALL_TOTAL_RECV_PAY_FEE_CNT + ", ALL_TOTAL_RECV_PAY_FEE_AMT=" + ALL_TOTAL_RECV_PAY_FEE_AMT + "]";
	}
	
}
