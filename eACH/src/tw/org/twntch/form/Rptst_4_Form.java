package tw.org.twntch.form;

import java.util.List;

public class Rptst_4_Form extends CommonForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2567642978484597356L;
	private String START_MONTH;
	private String START_YEAR;
	private String END_MONTH;
	private String END_YEAR;
	private String SENDERID ; //發動者統編
	private String OPBK_ID;	//操作行代號
	private String BGBK_ID;	//總行代號
	private List opbkIdList;
	private List bgbkIdList;
	private String dow_token ; //檔案下載判別用
	private String COMPANY_NAME;
	private String TXN_ID;
	private String FEE_TYPE;
	private	List	idList	;
	
	public String getSTART_MONTH() {
		return START_MONTH;
	}
	public void setSTART_MONTH(String sTART_MONTH) {
		START_MONTH = sTART_MONTH;
	}
	public String getEND_MONTH() {
		return END_MONTH;
	}
	public void setEND_MONTH(String eND_MONTH) {
		END_MONTH = eND_MONTH;
	}
	public String getSTART_YEAR() {
		return START_YEAR;
	}
	public void setSTART_YEAR(String sTART_YEAR) {
		START_YEAR = sTART_YEAR;
	}
	public String getEND_YEAR() {
		return END_YEAR;
	}
	public void setEND_YEAR(String eND_YEAR) {
		END_YEAR = eND_YEAR;
	}
	public String getSENDERID() {
		return SENDERID;
	}
	public void setSENDERID(String sENDERID) {
		SENDERID = sENDERID;
	}
	public String getOPBK_ID() {
		return OPBK_ID;
	}
	public void setOPBK_ID(String oPBK_ID) {
		OPBK_ID = oPBK_ID;
	}
	public String getBGBK_ID() {
		return BGBK_ID;
	}
	public void setBGBK_ID(String bGBK_ID) {
		BGBK_ID = bGBK_ID;
	}
	public List getOpbkIdList() {
		return opbkIdList;
	}
	public void setOpbkIdList(List opbkIdList) {
		this.opbkIdList = opbkIdList;
	}
	public List getBgbkIdList() {
		return bgbkIdList;
	}
	public void setBgbkIdList(List bgbkIdList) {
		this.bgbkIdList = bgbkIdList;
	}
	public String getDow_token() {
		return dow_token;
	}
	public void setDow_token(String dow_token) {
		this.dow_token = dow_token;
	}
	public String getCOMPANY_NAME() {
		return COMPANY_NAME;
	}
	public void setCOMPANY_NAME(String cOMPANY_NAME) {
		COMPANY_NAME = cOMPANY_NAME;
	}
	public String getTXN_ID() {
		return TXN_ID;
	}
	public void setTXN_ID(String tXN_ID) {
		TXN_ID = tXN_ID;
	}
	public String getFEE_TYPE() {
		return FEE_TYPE;
	}
	public void setFEE_TYPE(String fEE_TYPE) {
		FEE_TYPE = fEE_TYPE;
	}
	public List getIdList() {
		return idList;
	}
	public void setIdList(List idList) {
		this.idList = idList;
	}
}
