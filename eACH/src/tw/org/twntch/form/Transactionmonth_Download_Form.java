package tw.org.twntch.form;

import java.util.List;

public class Transactionmonth_Download_Form extends CommonForm{
	private static final long serialVersionUID = 686880146457955685L;

	private String BIZYM;//營業年月
	private String PCODE;//交易代號
	private String OPBK_ID;//操作行
	private List pcodeList;//交易代號下拉選單
	private List opbkIdList;//操作行下拉選單
	private String downloadToken;//檔案下載判別用
	
	
	public String getBIZYM() {
		return BIZYM;
	}
	public void setBIZYM(String bIZYM) {
		BIZYM = bIZYM;
	}
	public String getPCODE() {
		return PCODE;
	}
	public void setPCODE(String pCODE) {
		PCODE = pCODE;
	}
	public String getOPBK_ID() {
		return OPBK_ID;
	}
	public void setOPBK_ID(String oPBK_ID) {
		OPBK_ID = oPBK_ID;
	}
	public List getPcodeList() {
		return pcodeList;
	}
	public void setPcodeList(List pcodeList) {
		this.pcodeList = pcodeList;
	}
	public List getOpbkIdList() {
		return opbkIdList;
	}
	public void setOpbkIdList(List opbkIdList) {
		this.opbkIdList = opbkIdList;
	}
	public String getDownloadToken() {
		return downloadToken;
	}
	public void setDownloadToken(String downloadToken) {
		this.downloadToken = downloadToken;
	}
}
