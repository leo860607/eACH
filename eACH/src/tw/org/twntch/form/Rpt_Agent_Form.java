package tw.org.twntch.form;

import java.util.List;
/**
 * 代理業者相關報表共用Form
 * @author Hugo
 *
 */
public class Rpt_Agent_Form extends CommonForm {
	
	private List txnIdList;
	private List company_IdList ;
	private List snd_company_IdList ;
	private String BIZDATE ; //營業日期
	private String SBIZDATE ; //營業日期(起)
	private String EBIZDATE ; //營業日期(迄)
	private String CLEARINGPHASE ; //清算階段
	private String AGENT_COMPANY_ID ; //代理業者代號
	private String AGENT_COMPANY_NAME ; //代理業者名稱
	private String AGENT_COMPANY_ABBR_NAME ; //代理業者簡稱
	private String SND_COMPANY_ID ; //發動業者代號
	private String SND_COMPANY_NAME ; //發動業者名稱
	private String SND_COMPANY_ABBR_NAME ; //發動業者簡稱
	private String TG_RESULT ; //交易結果
	private String TXID ; //交易代號
	private String PCODE ; //交易類別
	private String TW_YEAR ; //年
	private String TW_MONTH ; //月
	private String START_YEAR ; //年(起)
	private String START_MONTH ; //月(起)
	private String END_YEAR ; //年(迄)
	private String END_MONTH ; //月(迄)
	private String YYYYMM ; //月(迄)
	
//	OPC用
	private String TXDATE ; //交易日期
	private String TXTIME_START ; //交易時間(起)
	private String TXTIME_END ; //交易時間(訖)
	private String SENDER_TYPE ; //發動端
	private String RECEIVER_TYPE ; //接收端
	private String ISSUERID ; //接收端
	private String RECEIVERID ; //接收端
	private String RESPONSECODE ; //回應代碼
	private String PROCESSCODE ; //交易類別(代理業者用4碼)
	private List pcodeList ;//交易代號清單(4碼)
	private List txnErrorCodeList ;//回應代碼清單
	private List detailData; //明細資料
	
	private String dow_token ; //檔案下載判別用
	public List getTxnIdList() {
		return txnIdList;
	}
	public void setTxnIdList(List txnIdList) {
		this.txnIdList = txnIdList;
	}
	public List getCompany_IdList() {
		return company_IdList;
	}
	public void setCompany_IdList(List company_IdList) {
		this.company_IdList = company_IdList;
	}
	public List getSnd_company_IdList() {
		return snd_company_IdList;
	}
	public void setSnd_company_IdList(List snd_company_IdList) {
		this.snd_company_IdList = snd_company_IdList;
	}
	public String getBIZDATE() {
		return BIZDATE;
	}
	public void setBIZDATE(String bIZDATE) {
		BIZDATE = bIZDATE;
	}
	
	
	
	public String getSBIZDATE() {
		return SBIZDATE;
	}
	public void setSBIZDATE(String sBIZDATE) {
		SBIZDATE = sBIZDATE;
	}
	public String getEBIZDATE() {
		return EBIZDATE;
	}
	public void setEBIZDATE(String eBIZDATE) {
		EBIZDATE = eBIZDATE;
	}
	public String getCLEARINGPHASE() {
		return CLEARINGPHASE;
	}
	public void setCLEARINGPHASE(String cLEARINGPHASE) {
		CLEARINGPHASE = cLEARINGPHASE;
	}
	public String getTXID() {
		return TXID;
	}
	public void setTXID(String tXID) {
		TXID = tXID;
	}
	public String getPCODE() {
		return PCODE;
	}
	public void setPCODE(String pCODE) {
		PCODE = pCODE;
	}
	public String getDow_token() {
		return dow_token;
	}
	public void setDow_token(String dow_token) {
		this.dow_token = dow_token;
	}
	public String getAGENT_COMPANY_ID() {
		return AGENT_COMPANY_ID;
	}
	public void setAGENT_COMPANY_ID(String aGENT_COMPANY_ID) {
		AGENT_COMPANY_ID = aGENT_COMPANY_ID;
	}
	public String getSND_COMPANY_ID() {
		return SND_COMPANY_ID;
	}
	public void setSND_COMPANY_ID(String sND_COMPANY_ID) {
		SND_COMPANY_ID = sND_COMPANY_ID;
	}
	public String getAGENT_COMPANY_NAME() {
		return AGENT_COMPANY_NAME;
	}
	public void setAGENT_COMPANY_NAME(String aGENT_COMPANY_NAME) {
		AGENT_COMPANY_NAME = aGENT_COMPANY_NAME;
	}
	public String getAGENT_COMPANY_ABBR_NAME() {
		return AGENT_COMPANY_ABBR_NAME;
	}
	public void setAGENT_COMPANY_ABBR_NAME(String aGENT_COMPANY_ABBR_NAME) {
		AGENT_COMPANY_ABBR_NAME = aGENT_COMPANY_ABBR_NAME;
	}
	public String getSND_COMPANY_NAME() {
		return SND_COMPANY_NAME;
	}
	public void setSND_COMPANY_NAME(String sND_COMPANY_NAME) {
		SND_COMPANY_NAME = sND_COMPANY_NAME;
	}
	public String getSND_COMPANY_ABBR_NAME() {
		return SND_COMPANY_ABBR_NAME;
	}
	public void setSND_COMPANY_ABBR_NAME(String sND_COMPANY_ABBR_NAME) {
		SND_COMPANY_ABBR_NAME = sND_COMPANY_ABBR_NAME;
	}
	public String getTG_RESULT() {
		return TG_RESULT;
	}
	public void setTG_RESULT(String tG_RESULT) {
		TG_RESULT = tG_RESULT;
	}
	public String getTW_YEAR() {
		return TW_YEAR;
	}
	public void setTW_YEAR(String tW_YEAR) {
		TW_YEAR = tW_YEAR;
	}
	public String getTW_MONTH() {
		return TW_MONTH;
	}
	public void setTW_MONTH(String tW_MONTH) {
		TW_MONTH = tW_MONTH;
	}
	public String getSTART_YEAR() {
		return START_YEAR;
	}
	public void setSTART_YEAR(String sTART_YEAR) {
		START_YEAR = sTART_YEAR;
	}
	public String getSTART_MONTH() {
		return START_MONTH;
	}
	public void setSTART_MONTH(String sTART_MONTH) {
		START_MONTH = sTART_MONTH;
	}
	public String getEND_YEAR() {
		return END_YEAR;
	}
	public void setEND_YEAR(String eND_YEAR) {
		END_YEAR = eND_YEAR;
	}
	public String getEND_MONTH() {
		return END_MONTH;
	}
	public void setEND_MONTH(String eND_MONTH) {
		END_MONTH = eND_MONTH;
	}
	public String getYYYYMM() {
		return YYYYMM;
	}
	public void setYYYYMM(String yYYYMM) {
		YYYYMM = yYYYMM;
	}
	public String getTXDATE() {
		return TXDATE;
	}
	public void setTXDATE(String tXDATE) {
		TXDATE = tXDATE;
	}
	public String getTXTIME_START() {
		return TXTIME_START;
	}
	public void setTXTIME_START(String tXTIME_START) {
		TXTIME_START = tXTIME_START;
	}
	public String getTXTIME_END() {
		return TXTIME_END;
	}
	public void setTXTIME_END(String tXTIME_END) {
		TXTIME_END = tXTIME_END;
	}
	public String getSENDER_TYPE() {
		return SENDER_TYPE;
	}
	public void setSENDER_TYPE(String sENDER_TYPE) {
		SENDER_TYPE = sENDER_TYPE;
	}
	public String getRECEIVER_TYPE() {
		return RECEIVER_TYPE;
	}
	public void setRECEIVER_TYPE(String rECEIVER_TYPE) {
		RECEIVER_TYPE = rECEIVER_TYPE;
	}
	
	public String getISSUERID() {
		return ISSUERID;
	}
	public void setISSUERID(String iSSUERID) {
		ISSUERID = iSSUERID;
	}
	public String getRECEIVERID() {
		return RECEIVERID;
	}
	public void setRECEIVERID(String rECEIVERID) {
		RECEIVERID = rECEIVERID;
	}
	public List getPcodeList() {
		return pcodeList;
	}
	public void setPcodeList(List pcodeList) {
		this.pcodeList = pcodeList;
	}
	public List getTxnErrorCodeList() {
		return txnErrorCodeList;
	}
	public void setTxnErrorCodeList(List txnErrorCodeList) {
		this.txnErrorCodeList = txnErrorCodeList;
	}
	public String getRESPONSECODE() {
		return RESPONSECODE;
	}
	public void setRESPONSECODE(String rESPONSECODE) {
		RESPONSECODE = rESPONSECODE;
	}
	public List getDetailData() {
		return detailData;
	}
	public void setDetailData(List detailData) {
		this.detailData = detailData;
	}
	public String getPROCESSCODE() {
		return PROCESSCODE;
	}
	public void setPROCESSCODE(String pROCESSCODE) {
		PROCESSCODE = pROCESSCODE;
	}
	
	
	
}
