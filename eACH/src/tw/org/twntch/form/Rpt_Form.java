package tw.org.twntch.form;

import java.util.List;
/**
 * 相關報表共用Form 目前只有ST20 ST21在使用
 * @author Hugo
 *
 */
public class Rpt_Form extends CommonForm {
	
	private List txnIdList;
	private List opbkIdList;
	private String BIZDATE ; //營業日期
	private String SBIZDATE ; //營業日期(起)
	private String EBIZDATE ; //營業日期(迄)
	private String CLEARINGPHASE ; //清算階段
	private String TXID ; //交易代號
	private String PCODE ; //交易類別
	private String TW_YEAR ; //年
	private String TW_MONTH ; //月
	private String START_YEAR ; //年(起)
	private String START_MONTH ; //月(起)
	private String END_YEAR ; //年(迄)
	private String END_MONTH ; //月(迄)
	private String YYYYMM ; //月(迄)
	private String RESULT_TYPE ; //回應代碼類別
	private String OPBK_ID ; //操作行代號
	
	private String dow_token ; //檔案下載判別用
	
	public List getTxnIdList() {
		return txnIdList;
	}
	public void setTxnIdList(List txnIdList) {
		this.txnIdList = txnIdList;
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
	public String getRESULT_TYPE() {
		return RESULT_TYPE;
	}
	public void setRESULT_TYPE(String rESULT_TYPE) {
		RESULT_TYPE = rESULT_TYPE;
	}
	public String getOPBK_ID() {
		return OPBK_ID;
	}
	public void setOPBK_ID(String oPBK_ID) {
		OPBK_ID = oPBK_ID;
	}
	public List getOpbkIdList() {
		return opbkIdList;
	}
	public void setOpbkIdList(List opbkIdList) {
		this.opbkIdList = opbkIdList;
	}
	
	
	
}
