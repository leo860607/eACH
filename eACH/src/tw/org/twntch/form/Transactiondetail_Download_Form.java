package tw.org.twntch.form;

import java.util.List;

public class Transactiondetail_Download_Form extends CommonForm{
	private static final long serialVersionUID = 3499075873887025607L;

	private String BIZDATE;//營業日期
    private String OPBK_ID;//操作行代號
    private String CLEARINGPHASE;//清算階段
    private List opbkIdList;//操作行代號下拉選單
    private String downloadToken;//檔案下載判別用
    
	public String getBIZDATE() {
		return BIZDATE;
	}
	public void setBIZDATE(String bIZDATE) {
		BIZDATE = bIZDATE;
	}
	public String getOPBK_ID() {
		return OPBK_ID;
	}
	public void setOPBK_ID(String oPBK_ID) {
		OPBK_ID = oPBK_ID;
	}
	public String getCLEARINGPHASE() {
		return CLEARINGPHASE;
	}
	public void setCLEARINGPHASE(String cLEARINGPHASE) {
		CLEARINGPHASE = cLEARINGPHASE;
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
