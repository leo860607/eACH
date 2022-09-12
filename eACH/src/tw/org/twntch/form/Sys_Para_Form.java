package tw.org.twntch.form;

import java.math.BigDecimal;
import java.util.List;

public class Sys_Para_Form extends CommonForm {
	
	private	List	scaseary	;
	private	String 	SEQ_ID	;
	private	String 	TIMEOUT_TIME	;
	private	String 	MAX_FILE_SIZE	;
//	private	String 	PARTY_STD_ECHO_TIME	;
	private	String 	TCH_STD_ECHO_TIME	;
	private	String 	TXN_STD_PROC_TIME	;
	private	String    	CDATE	;
	private	String    	UDATE	;
	private	String  BANK_SC_STD_PROC_TIME;//銀行每筆入帳標準處理時間(秒)
	private	String  BANK_SD_STD_PROC_TIME;//銀行每筆扣款標準處理時間(秒)
	private	String    	ACCT_LIMIT_SAMT	;
	private	String    	ATM_LIMIT_SAMT	;
	private	String    	CERT_LIMIT_SAMT	;
	
//	整批作業使用
	private	String    	PEND_TIME_BUF ; 	
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
	private String		SD_SC_TYPE_CHK	;
	private String		SD_SC_TYPE_CHK_Y	;
	private String		SD_SC_TYPE_CHK_N	;
	
	public List getScaseary() {
		return scaseary;
	}
	public void setScaseary(List scaseary) {
		this.scaseary = scaseary;
	}
	public String getSEQ_ID() {
		return SEQ_ID;
	}
	public void setSEQ_ID(String sEQ_ID) {
		SEQ_ID = sEQ_ID;
	}
	public String getTIMEOUT_TIME() {
		return TIMEOUT_TIME;
	}
	public void setTIMEOUT_TIME(String tIMEOUT_TIME) {
		TIMEOUT_TIME = tIMEOUT_TIME;
	}
	public String getMAX_FILE_SIZE() {
		return MAX_FILE_SIZE;
	}
	public void setMAX_FILE_SIZE(String mAX_FILE_SIZE) {
		MAX_FILE_SIZE = mAX_FILE_SIZE;
	}
	public String getTCH_STD_ECHO_TIME() {
		return TCH_STD_ECHO_TIME;
	}
	public void setTCH_STD_ECHO_TIME(String tCH_STD_ECHO_TIME) {
		TCH_STD_ECHO_TIME = tCH_STD_ECHO_TIME;
	}
	public String getTXN_STD_PROC_TIME() {
		return TXN_STD_PROC_TIME;
	}
	public void setTXN_STD_PROC_TIME(String tXN_STD_PROC_TIME) {
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
	
	
	
	
	public String getFILE_MAX_CNT() {
		return FILE_MAX_CNT;
	}
	public void setFILE_MAX_CNT(String fILE_MAX_CNT) {
		FILE_MAX_CNT = fILE_MAX_CNT;
	}
	public String getPEND_TIME_BUF() {
		return PEND_TIME_BUF;
	}
	public void setPEND_TIME_BUF(String pEND_TIME_BUF) {
		PEND_TIME_BUF = pEND_TIME_BUF;
	}
	public String getBANK_SC_STD_PROC_TIME() {
		return BANK_SC_STD_PROC_TIME;
	}
	public void setBANK_SC_STD_PROC_TIME(String bANK_SC_STD_PROC_TIME) {
		BANK_SC_STD_PROC_TIME = bANK_SC_STD_PROC_TIME;
	}
	public String getBANK_SD_STD_PROC_TIME() {
		return BANK_SD_STD_PROC_TIME;
	}
	public void setBANK_SD_STD_PROC_TIME(String bANK_SD_STD_PROC_TIME) {
		BANK_SD_STD_PROC_TIME = bANK_SD_STD_PROC_TIME;
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
	
	public String getSD_SC_TYPE_CHK() {
		return SD_SC_TYPE_CHK;
	}
	public void setSD_SC_TYPE_CHK(String sD_SC_TYPE_CHK) {
		SD_SC_TYPE_CHK = sD_SC_TYPE_CHK;
	}
	public String getSD_SC_TYPE_CHK_Y() {
		return SD_SC_TYPE_CHK_Y;
	}
	public void setSD_SC_TYPE_CHK_Y(String sD_SC_TYPE_CHK_Y) {
		SD_SC_TYPE_CHK_Y = sD_SC_TYPE_CHK_Y;
	}
	public String getSD_SC_TYPE_CHK_N() {
		return SD_SC_TYPE_CHK_N;
	}
	public void setSD_SC_TYPE_CHK_N(String sD_SC_TYPE_CHK_N) {
		SD_SC_TYPE_CHK_N = sD_SC_TYPE_CHK_N;
	}
	public void resetFields(){
		scaseary = null;
		SEQ_ID = null;
		TIMEOUT_TIME = null;
		MAX_FILE_SIZE = null;
		TCH_STD_ECHO_TIME = null;
		TXN_STD_PROC_TIME = null;
		CDATE = null;
		UDATE = null;
	}
	
	
	
	
}
