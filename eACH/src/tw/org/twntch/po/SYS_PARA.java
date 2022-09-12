package tw.org.twntch.po;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;



@Entity(name="tw.org.twntch.po.SYS_PARA")
@Table(name="SYS_PARA")
public class SYS_PARA implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3163902085131295047L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)	
	private	Integer 	SEQ_ID	;
	private	Integer 	TIMEOUT_TIME	;
	private	Integer 	MAX_FILE_SIZE	;
//	private	Integer 	PARTY_STD_ECHO_TIME	;
//	private	Integer 	TCH_STD_ECHO_TIME	;
//	private	Integer 	TXN_STD_PROC_TIME	;
	
	
	private	String    	ACCT_LIMIT_SAMT	;
	private	String    	ATM_LIMIT_SAMT	;
	private	String    	CERT_LIMIT_SAMT	;
	
	private	String    	CDATE	;
	private	String   		UDATE	;
//	查詢統計用
	private	BigDecimal 	TCH_STD_ECHO_TIME	;
	private	BigDecimal 	TXN_STD_PROC_TIME	;
	private	BigDecimal  BANK_SC_STD_PROC_TIME;
	private	BigDecimal  BANK_SD_STD_PROC_TIME;
//	此2個屬性是批次排程使用 
	private	String    	RP_CLEARPHASE1_TIME 	;
	private	String    	RP_CLEARPHASE2_TIME 	;
	private	String    	AP1 	;
	private	String    	AP2 	;
	private	String    	AP1_ISRUN 	;
	private	String    	AP2_ISRUN 	;
//	整批作業使用
	private	Integer     PEND_TIME_BUF ;
	private	String    	APT_PEND_START_TIME1 	;
	private	String    	APT_PEND_END_TIME1 	;
	private	String    	APT_PEND_START_TIME2 	;
	private	String    	APT_PEND_END_TIME2 	;
	private	String    	APT_PEND_START_TIME3 	;
	private	String    	APT_PEND_END_TIME3 	;
	private	String    	APT_PEND_START_TIME4 	;
	private	String    	APT_PEND_END_TIME4 	;
	private	String    	APT_PEND_START_TIME5 	;
	private	String    	APT_PEND_END_TIME5 	;
	private	String    	HR_UP_MAX_FILE_DFT 	;
	private	String    	FILE_MAX_CNT 	;
	private	String    	TRAN_DIFF_TIME 	;
//	安控版本
//	private String		ISEC_VERSION	;
	
//	手續費相關新增
	private String		FEE_NW_D_DATE;
//  
	private String		SD_SC_TYPE_CHK;
	
//  監控新增
	private String		MONITOR_AMOUNT;
	private String		MONITOR_PENDING;
	private String		MONITOR_AMOUNT_PERIOD;
	private String		MONITOR_PENDING_PERIOD;
	private String		MONITOR_MAILRECEIVER;
	
	
	public Integer getSEQ_ID() {
		return SEQ_ID;
	}
	public void setSEQ_ID(Integer sEQ_ID) {
		SEQ_ID = sEQ_ID;
	}	
	public Integer getTIMEOUT_TIME() {
		return TIMEOUT_TIME;
	}
	public void setTIMEOUT_TIME(Integer tIMEOUT_TIME) {
		TIMEOUT_TIME = tIMEOUT_TIME;
	}
	public Integer getMAX_FILE_SIZE() {
		return MAX_FILE_SIZE;
	}
	public void setMAX_FILE_SIZE(Integer mAX_FILE_SIZE) {
		MAX_FILE_SIZE = mAX_FILE_SIZE;
	}
	
	public BigDecimal getTCH_STD_ECHO_TIME() {
		return TCH_STD_ECHO_TIME;
	}
	public void setTCH_STD_ECHO_TIME(BigDecimal tCH_STD_ECHO_TIME) {
		TCH_STD_ECHO_TIME = tCH_STD_ECHO_TIME;
	}
	public BigDecimal getTXN_STD_PROC_TIME() {
		return TXN_STD_PROC_TIME;
	}
	public void setTXN_STD_PROC_TIME(BigDecimal tXN_STD_PROC_TIME) {
		TXN_STD_PROC_TIME = tXN_STD_PROC_TIME;
	}
	

	public String getCDATE() {
		return CDATE;
	}
	public void setCDATE(String cDATE) {
		CDATE = cDATE;
	}
	public String getUDATE() {
		return UDATE;
	}
	public void setUDATE(String uDATE) {
		UDATE = uDATE;
	}
	public String getRP_CLEARPHASE1_TIME() {
		return RP_CLEARPHASE1_TIME;
	}
	public void setRP_CLEARPHASE1_TIME(String rP_CLEARPHASE1_TIME) {
		RP_CLEARPHASE1_TIME = rP_CLEARPHASE1_TIME;
	}
	public String getRP_CLEARPHASE2_TIME() {
		return RP_CLEARPHASE2_TIME;
	}
	public void setRP_CLEARPHASE2_TIME(String rP_CLEARPHASE2_TIME) {
		RP_CLEARPHASE2_TIME = rP_CLEARPHASE2_TIME;
	}
	
	public String getAP1() {
		return AP1;
	}
	public void setAP1(String aP1) {
		AP1 = aP1;
	}
	public String getAP2() {
		return AP2;
	}
	public void setAP2(String aP2) {
		AP2 = aP2;
	}
	public String getAP1_ISRUN() {
		return AP1_ISRUN;
	}
	public void setAP1_ISRUN(String aP1_ISRUN) {
		AP1_ISRUN = aP1_ISRUN;
	}
	public String getAP2_ISRUN() {
		return AP2_ISRUN;
	}
	public void setAP2_ISRUN(String aP2_ISRUN) {
		AP2_ISRUN = aP2_ISRUN;
	}
	
	
	
	
	public String getAPT_PEND_START_TIME1() {
		return APT_PEND_START_TIME1;
	}
	public void setAPT_PEND_START_TIME1(String aPT_PEND_START_TIME1) {
		APT_PEND_START_TIME1 = aPT_PEND_START_TIME1;
	}
	public String getAPT_PEND_END_TIME1() {
		return APT_PEND_END_TIME1;
	}
	public void setAPT_PEND_END_TIME1(String aPT_PEND_END_TIME1) {
		APT_PEND_END_TIME1 = aPT_PEND_END_TIME1;
	}
	public String getAPT_PEND_START_TIME2() {
		return APT_PEND_START_TIME2;
	}
	public void setAPT_PEND_START_TIME2(String aPT_PEND_START_TIME2) {
		APT_PEND_START_TIME2 = aPT_PEND_START_TIME2;
	}
	public String getAPT_PEND_END_TIME2() {
		return APT_PEND_END_TIME2;
	}
	public void setAPT_PEND_END_TIME2(String aPT_PEND_END_TIME2) {
		APT_PEND_END_TIME2 = aPT_PEND_END_TIME2;
	}
	public String getAPT_PEND_START_TIME3() {
		return APT_PEND_START_TIME3;
	}
	public void setAPT_PEND_START_TIME3(String aPT_PEND_START_TIME3) {
		APT_PEND_START_TIME3 = aPT_PEND_START_TIME3;
	}
	public String getAPT_PEND_END_TIME3() {
		return APT_PEND_END_TIME3;
	}
	public void setAPT_PEND_END_TIME3(String aPT_PEND_END_TIME3) {
		APT_PEND_END_TIME3 = aPT_PEND_END_TIME3;
	}
	public String getAPT_PEND_START_TIME4() {
		return APT_PEND_START_TIME4;
	}
	public void setAPT_PEND_START_TIME4(String aPT_PEND_START_TIME4) {
		APT_PEND_START_TIME4 = aPT_PEND_START_TIME4;
	}
	public String getAPT_PEND_END_TIME4() {
		return APT_PEND_END_TIME4;
	}
	public void setAPT_PEND_END_TIME4(String aPT_PEND_END_TIME4) {
		APT_PEND_END_TIME4 = aPT_PEND_END_TIME4;
	}
	public String getAPT_PEND_START_TIME5() {
		return APT_PEND_START_TIME5;
	}
	public void setAPT_PEND_START_TIME5(String aPT_PEND_START_TIME5) {
		APT_PEND_START_TIME5 = aPT_PEND_START_TIME5;
	}
	public String getAPT_PEND_END_TIME5() {
		return APT_PEND_END_TIME5;
	}
	public void setAPT_PEND_END_TIME5(String aPT_PEND_END_TIME5) {
		APT_PEND_END_TIME5 = aPT_PEND_END_TIME5;
	}
	public String getHR_UP_MAX_FILE_DFT() {
		return HR_UP_MAX_FILE_DFT;
	}
	public void setHR_UP_MAX_FILE_DFT(String hR_UP_MAX_FILE_DFT) {
		HR_UP_MAX_FILE_DFT = hR_UP_MAX_FILE_DFT;
	}
	
	
	
	public BigDecimal getBANK_SC_STD_PROC_TIME() {
		return BANK_SC_STD_PROC_TIME;
	}
	public void setBANK_SC_STD_PROC_TIME(BigDecimal bANK_SC_STD_PROC_TIME) {
		BANK_SC_STD_PROC_TIME = bANK_SC_STD_PROC_TIME;
	}
	public BigDecimal getBANK_SD_STD_PROC_TIME() {
		return BANK_SD_STD_PROC_TIME;
	}
	public void setBANK_SD_STD_PROC_TIME(BigDecimal bANK_SD_STD_PROC_TIME) {
		BANK_SD_STD_PROC_TIME = bANK_SD_STD_PROC_TIME;
	}
	public Integer getPEND_TIME_BUF() {
		return PEND_TIME_BUF;
	}
	public void setPEND_TIME_BUF(Integer pEND_TIME_BUF) {
		PEND_TIME_BUF = pEND_TIME_BUF;
	}
	
	public String getFILE_MAX_CNT() {
		return FILE_MAX_CNT;
	}
	public void setFILE_MAX_CNT(String fILE_MAX_CNT) {
		FILE_MAX_CNT = fILE_MAX_CNT;
	}
	
	
	
	
	
	public String getACCT_LIMIT_SAMT() {
		return ACCT_LIMIT_SAMT;
	}
	public void setACCT_LIMIT_SAMT(String aCCT_LIMIT_SAMT) {
		ACCT_LIMIT_SAMT = aCCT_LIMIT_SAMT;
	}
	public String getATM_LIMIT_SAMT() {
		return ATM_LIMIT_SAMT;
	}
	public void setATM_LIMIT_SAMT(String aTM_LIMIT_SAMT) {
		ATM_LIMIT_SAMT = aTM_LIMIT_SAMT;
	}
	public String getCERT_LIMIT_SAMT() {
		return CERT_LIMIT_SAMT;
	}
	public void setCERT_LIMIT_SAMT(String cERT_LIMIT_SAMT) {
		CERT_LIMIT_SAMT = cERT_LIMIT_SAMT;
	}	
	
	public String getTRAN_DIFF_TIME() {
		return TRAN_DIFF_TIME;
	}
	public void setTRAN_DIFF_TIME(String tRAN_DIFF_TIME) {
		TRAN_DIFF_TIME = tRAN_DIFF_TIME;
	}
//	安控元件
//	public String getISEC_VERSION() {
//		return ISEC_VERSION;
//	}
//	public void setISEC_VERSION(String iSEC_VERSION) {
//		ISEC_VERSION = iSEC_VERSION;
//	}
	
	public String getFEE_NW_D_DATE() {
		return FEE_NW_D_DATE;
	}
	public void setFEE_NW_D_DATE(String fEE_NW_D_DATE) {
		FEE_NW_D_DATE = fEE_NW_D_DATE;
	}
	public String getSD_SC_TYPE_CHK() {
		return SD_SC_TYPE_CHK;
	}
	public void setSD_SC_TYPE_CHK(String sD_SC_TYPE_CHK) {
		SD_SC_TYPE_CHK = sD_SC_TYPE_CHK;
	}
	public String getMONITOR_AMOUNT() {
		return MONITOR_AMOUNT;
	}
	public void setMONITOR_AMOUNT(String mONITOR_AMOUNT) {
		MONITOR_AMOUNT = mONITOR_AMOUNT;
	}
	public String getMONITOR_PENDING() {
		return MONITOR_PENDING;
	}
	public void setMONITOR_PENDING(String mONITOR_PENDING) {
		MONITOR_PENDING = mONITOR_PENDING;
	}
	public String getMONITOR_AMOUNT_PERIOD() {
		return MONITOR_AMOUNT_PERIOD;
	}
	public void setMONITOR_AMOUNT_PERIOD(String mONITOR_AMOUNT_PERIOD) {
		MONITOR_AMOUNT_PERIOD = mONITOR_AMOUNT_PERIOD;
	}
	public String getMONITOR_PENDING_PERIOD() {
		return MONITOR_PENDING_PERIOD;
	}
	public void setMONITOR_PENDING_PERIOD(String mONITOR_PENDING_PERIOD) {
		MONITOR_PENDING_PERIOD = mONITOR_PENDING_PERIOD;
	}
	public String getMONITOR_MAILRECEIVER() {
		return MONITOR_MAILRECEIVER;
	}
	public void setMONITOR_MAILRECEIVER(String mONITOR_MAILRECEIVER) {
		MONITOR_MAILRECEIVER = mONITOR_MAILRECEIVER;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ACCT_LIMIT_SAMT == null) ? 0 : ACCT_LIMIT_SAMT.hashCode());
		result = prime * result + ((AP1 == null) ? 0 : AP1.hashCode());
		result = prime * result + ((AP1_ISRUN == null) ? 0 : AP1_ISRUN.hashCode());
		result = prime * result + ((AP2 == null) ? 0 : AP2.hashCode());
		result = prime * result + ((AP2_ISRUN == null) ? 0 : AP2_ISRUN.hashCode());
		result = prime * result + ((APT_PEND_END_TIME1 == null) ? 0 : APT_PEND_END_TIME1.hashCode());
		result = prime * result + ((APT_PEND_END_TIME2 == null) ? 0 : APT_PEND_END_TIME2.hashCode());
		result = prime * result + ((APT_PEND_END_TIME3 == null) ? 0 : APT_PEND_END_TIME3.hashCode());
		result = prime * result + ((APT_PEND_END_TIME4 == null) ? 0 : APT_PEND_END_TIME4.hashCode());
		result = prime * result + ((APT_PEND_END_TIME5 == null) ? 0 : APT_PEND_END_TIME5.hashCode());
		result = prime * result + ((APT_PEND_START_TIME1 == null) ? 0 : APT_PEND_START_TIME1.hashCode());
		result = prime * result + ((APT_PEND_START_TIME2 == null) ? 0 : APT_PEND_START_TIME2.hashCode());
		result = prime * result + ((APT_PEND_START_TIME3 == null) ? 0 : APT_PEND_START_TIME3.hashCode());
		result = prime * result + ((APT_PEND_START_TIME4 == null) ? 0 : APT_PEND_START_TIME4.hashCode());
		result = prime * result + ((APT_PEND_START_TIME5 == null) ? 0 : APT_PEND_START_TIME5.hashCode());
		result = prime * result + ((ATM_LIMIT_SAMT == null) ? 0 : ATM_LIMIT_SAMT.hashCode());
		result = prime * result + ((BANK_SC_STD_PROC_TIME == null) ? 0 : BANK_SC_STD_PROC_TIME.hashCode());
		result = prime * result + ((BANK_SD_STD_PROC_TIME == null) ? 0 : BANK_SD_STD_PROC_TIME.hashCode());
		result = prime * result + ((CDATE == null) ? 0 : CDATE.hashCode());
		result = prime * result + ((CERT_LIMIT_SAMT == null) ? 0 : CERT_LIMIT_SAMT.hashCode());
		result = prime * result + ((FEE_NW_D_DATE == null) ? 0 : FEE_NW_D_DATE.hashCode());
		result = prime * result + ((FILE_MAX_CNT == null) ? 0 : FILE_MAX_CNT.hashCode());
		result = prime * result + ((HR_UP_MAX_FILE_DFT == null) ? 0 : HR_UP_MAX_FILE_DFT.hashCode());
		result = prime * result + ((MAX_FILE_SIZE == null) ? 0 : MAX_FILE_SIZE.hashCode());
		result = prime * result + ((MONITOR_AMOUNT == null) ? 0 : MONITOR_AMOUNT.hashCode());
		result = prime * result + ((MONITOR_AMOUNT_PERIOD == null) ? 0 : MONITOR_AMOUNT_PERIOD.hashCode());
		result = prime * result + ((MONITOR_MAILRECEIVER == null) ? 0 : MONITOR_MAILRECEIVER.hashCode());
		result = prime * result + ((MONITOR_PENDING == null) ? 0 : MONITOR_PENDING.hashCode());
		result = prime * result + ((MONITOR_PENDING_PERIOD == null) ? 0 : MONITOR_PENDING_PERIOD.hashCode());
		result = prime * result + ((PEND_TIME_BUF == null) ? 0 : PEND_TIME_BUF.hashCode());
		result = prime * result + ((RP_CLEARPHASE1_TIME == null) ? 0 : RP_CLEARPHASE1_TIME.hashCode());
		result = prime * result + ((RP_CLEARPHASE2_TIME == null) ? 0 : RP_CLEARPHASE2_TIME.hashCode());
		result = prime * result + ((SD_SC_TYPE_CHK == null) ? 0 : SD_SC_TYPE_CHK.hashCode());
		result = prime * result + ((SEQ_ID == null) ? 0 : SEQ_ID.hashCode());
		result = prime * result + ((TCH_STD_ECHO_TIME == null) ? 0 : TCH_STD_ECHO_TIME.hashCode());
		result = prime * result + ((TIMEOUT_TIME == null) ? 0 : TIMEOUT_TIME.hashCode());
		result = prime * result + ((TRAN_DIFF_TIME == null) ? 0 : TRAN_DIFF_TIME.hashCode());
		result = prime * result + ((TXN_STD_PROC_TIME == null) ? 0 : TXN_STD_PROC_TIME.hashCode());
		result = prime * result + ((UDATE == null) ? 0 : UDATE.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SYS_PARA other = (SYS_PARA) obj;
		if (ACCT_LIMIT_SAMT == null) {
			if (other.ACCT_LIMIT_SAMT != null)
				return false;
		} else if (!ACCT_LIMIT_SAMT.equals(other.ACCT_LIMIT_SAMT))
			return false;
		if (AP1 == null) {
			if (other.AP1 != null)
				return false;
		} else if (!AP1.equals(other.AP1))
			return false;
		if (AP1_ISRUN == null) {
			if (other.AP1_ISRUN != null)
				return false;
		} else if (!AP1_ISRUN.equals(other.AP1_ISRUN))
			return false;
		if (AP2 == null) {
			if (other.AP2 != null)
				return false;
		} else if (!AP2.equals(other.AP2))
			return false;
		if (AP2_ISRUN == null) {
			if (other.AP2_ISRUN != null)
				return false;
		} else if (!AP2_ISRUN.equals(other.AP2_ISRUN))
			return false;
		if (APT_PEND_END_TIME1 == null) {
			if (other.APT_PEND_END_TIME1 != null)
				return false;
		} else if (!APT_PEND_END_TIME1.equals(other.APT_PEND_END_TIME1))
			return false;
		if (APT_PEND_END_TIME2 == null) {
			if (other.APT_PEND_END_TIME2 != null)
				return false;
		} else if (!APT_PEND_END_TIME2.equals(other.APT_PEND_END_TIME2))
			return false;
		if (APT_PEND_END_TIME3 == null) {
			if (other.APT_PEND_END_TIME3 != null)
				return false;
		} else if (!APT_PEND_END_TIME3.equals(other.APT_PEND_END_TIME3))
			return false;
		if (APT_PEND_END_TIME4 == null) {
			if (other.APT_PEND_END_TIME4 != null)
				return false;
		} else if (!APT_PEND_END_TIME4.equals(other.APT_PEND_END_TIME4))
			return false;
		if (APT_PEND_END_TIME5 == null) {
			if (other.APT_PEND_END_TIME5 != null)
				return false;
		} else if (!APT_PEND_END_TIME5.equals(other.APT_PEND_END_TIME5))
			return false;
		if (APT_PEND_START_TIME1 == null) {
			if (other.APT_PEND_START_TIME1 != null)
				return false;
		} else if (!APT_PEND_START_TIME1.equals(other.APT_PEND_START_TIME1))
			return false;
		if (APT_PEND_START_TIME2 == null) {
			if (other.APT_PEND_START_TIME2 != null)
				return false;
		} else if (!APT_PEND_START_TIME2.equals(other.APT_PEND_START_TIME2))
			return false;
		if (APT_PEND_START_TIME3 == null) {
			if (other.APT_PEND_START_TIME3 != null)
				return false;
		} else if (!APT_PEND_START_TIME3.equals(other.APT_PEND_START_TIME3))
			return false;
		if (APT_PEND_START_TIME4 == null) {
			if (other.APT_PEND_START_TIME4 != null)
				return false;
		} else if (!APT_PEND_START_TIME4.equals(other.APT_PEND_START_TIME4))
			return false;
		if (APT_PEND_START_TIME5 == null) {
			if (other.APT_PEND_START_TIME5 != null)
				return false;
		} else if (!APT_PEND_START_TIME5.equals(other.APT_PEND_START_TIME5))
			return false;
		if (ATM_LIMIT_SAMT == null) {
			if (other.ATM_LIMIT_SAMT != null)
				return false;
		} else if (!ATM_LIMIT_SAMT.equals(other.ATM_LIMIT_SAMT))
			return false;
		if (BANK_SC_STD_PROC_TIME == null) {
			if (other.BANK_SC_STD_PROC_TIME != null)
				return false;
		} else if (!BANK_SC_STD_PROC_TIME.equals(other.BANK_SC_STD_PROC_TIME))
			return false;
		if (BANK_SD_STD_PROC_TIME == null) {
			if (other.BANK_SD_STD_PROC_TIME != null)
				return false;
		} else if (!BANK_SD_STD_PROC_TIME.equals(other.BANK_SD_STD_PROC_TIME))
			return false;
		if (CDATE == null) {
			if (other.CDATE != null)
				return false;
		} else if (!CDATE.equals(other.CDATE))
			return false;
		if (CERT_LIMIT_SAMT == null) {
			if (other.CERT_LIMIT_SAMT != null)
				return false;
		} else if (!CERT_LIMIT_SAMT.equals(other.CERT_LIMIT_SAMT))
			return false;
		if (FEE_NW_D_DATE == null) {
			if (other.FEE_NW_D_DATE != null)
				return false;
		} else if (!FEE_NW_D_DATE.equals(other.FEE_NW_D_DATE))
			return false;
		if (FILE_MAX_CNT == null) {
			if (other.FILE_MAX_CNT != null)
				return false;
		} else if (!FILE_MAX_CNT.equals(other.FILE_MAX_CNT))
			return false;
		if (HR_UP_MAX_FILE_DFT == null) {
			if (other.HR_UP_MAX_FILE_DFT != null)
				return false;
		} else if (!HR_UP_MAX_FILE_DFT.equals(other.HR_UP_MAX_FILE_DFT))
			return false;
		if (MAX_FILE_SIZE == null) {
			if (other.MAX_FILE_SIZE != null)
				return false;
		} else if (!MAX_FILE_SIZE.equals(other.MAX_FILE_SIZE))
			return false;
		if (MONITOR_AMOUNT == null) {
			if (other.MONITOR_AMOUNT != null)
				return false;
		} else if (!MONITOR_AMOUNT.equals(other.MONITOR_AMOUNT))
			return false;
		if (MONITOR_AMOUNT_PERIOD == null) {
			if (other.MONITOR_AMOUNT_PERIOD != null)
				return false;
		} else if (!MONITOR_AMOUNT_PERIOD.equals(other.MONITOR_AMOUNT_PERIOD))
			return false;
		if (MONITOR_MAILRECEIVER == null) {
			if (other.MONITOR_MAILRECEIVER != null)
				return false;
		} else if (!MONITOR_MAILRECEIVER.equals(other.MONITOR_MAILRECEIVER))
			return false;
		if (MONITOR_PENDING == null) {
			if (other.MONITOR_PENDING != null)
				return false;
		} else if (!MONITOR_PENDING.equals(other.MONITOR_PENDING))
			return false;
		if (MONITOR_PENDING_PERIOD == null) {
			if (other.MONITOR_PENDING_PERIOD != null)
				return false;
		} else if (!MONITOR_PENDING_PERIOD.equals(other.MONITOR_PENDING_PERIOD))
			return false;
		if (PEND_TIME_BUF == null) {
			if (other.PEND_TIME_BUF != null)
				return false;
		} else if (!PEND_TIME_BUF.equals(other.PEND_TIME_BUF))
			return false;
		if (RP_CLEARPHASE1_TIME == null) {
			if (other.RP_CLEARPHASE1_TIME != null)
				return false;
		} else if (!RP_CLEARPHASE1_TIME.equals(other.RP_CLEARPHASE1_TIME))
			return false;
		if (RP_CLEARPHASE2_TIME == null) {
			if (other.RP_CLEARPHASE2_TIME != null)
				return false;
		} else if (!RP_CLEARPHASE2_TIME.equals(other.RP_CLEARPHASE2_TIME))
			return false;
		if (SD_SC_TYPE_CHK == null) {
			if (other.SD_SC_TYPE_CHK != null)
				return false;
		} else if (!SD_SC_TYPE_CHK.equals(other.SD_SC_TYPE_CHK))
			return false;
		if (SEQ_ID == null) {
			if (other.SEQ_ID != null)
				return false;
		} else if (!SEQ_ID.equals(other.SEQ_ID))
			return false;
		if (TCH_STD_ECHO_TIME == null) {
			if (other.TCH_STD_ECHO_TIME != null)
				return false;
		} else if (!TCH_STD_ECHO_TIME.equals(other.TCH_STD_ECHO_TIME))
			return false;
		if (TIMEOUT_TIME == null) {
			if (other.TIMEOUT_TIME != null)
				return false;
		} else if (!TIMEOUT_TIME.equals(other.TIMEOUT_TIME))
			return false;
		if (TRAN_DIFF_TIME == null) {
			if (other.TRAN_DIFF_TIME != null)
				return false;
		} else if (!TRAN_DIFF_TIME.equals(other.TRAN_DIFF_TIME))
			return false;
		if (TXN_STD_PROC_TIME == null) {
			if (other.TXN_STD_PROC_TIME != null)
				return false;
		} else if (!TXN_STD_PROC_TIME.equals(other.TXN_STD_PROC_TIME))
			return false;
		if (UDATE == null) {
			if (other.UDATE != null)
				return false;
		} else if (!UDATE.equals(other.UDATE))
			return false;
		return true;
	}
}
