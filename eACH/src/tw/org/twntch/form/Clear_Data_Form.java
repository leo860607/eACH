package tw.org.twntch.form;

import java.util.List;
import java.util.Map;

public class Clear_Data_Form extends CommonForm {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3379939562243649755L;
	private String BIZDATE;
	//票交端
	private String START_DATE;
	private String END_DATE;
	
	private String CLEARINGPHASE;
	private String OPBK_ID;
	private String CTBK_ID;
	private String BGBK_ID;
	private String PCODE;
	private List pcodeList;
	private List opbkIdList;
	private List ctbkIdList;
	private Map detailData;
	private String pageForSort;
    private String serchStrs;
    private String ordForSort;
    private String colForSort;
    
    private String srch_BIZDATE;
	private String srch_CLEARINGPHASE;
	private String srch_PCODE;
	private String srch_BANKID;
	private String dow_token ; //檔案下載判別用
    
	public String getBIZDATE() {
		return BIZDATE;
	}
	public void setBIZDATE(String bIZDATE) {
		BIZDATE = bIZDATE;
	}
	public String getCLEARINGPHASE() {
		return CLEARINGPHASE;
	}
	public void setCLEARINGPHASE(String cLEARINGPHASE) {
		CLEARINGPHASE = cLEARINGPHASE;
	}
	public String getOPBK_ID() {
		return OPBK_ID;
	}
	public void setOPBK_ID(String oPBK_ID) {
		OPBK_ID = oPBK_ID;
	}
	public String getCTBK_ID() {
		return CTBK_ID;
	}
	public void setCTBK_ID(String cTBK_ID) {
		CTBK_ID = cTBK_ID;
	}
	public String getBGBK_ID() {
		return BGBK_ID;
	}
	public void setBGBK_ID(String bGBK_ID) {
		BGBK_ID = bGBK_ID;
	}
	public String getPCODE() {
		return PCODE;
	}
	public void setPCODE(String pCODE) {
		PCODE = pCODE;
	}
	public String getSTART_DATE() {
		return START_DATE;
	}
	public void setSTART_DATE(String sTART_DATE) {
		START_DATE = sTART_DATE;
	}
	public String getEND_DATE() {
		return END_DATE;
	}
	public void setEND_DATE(String eND_DATE) {
		END_DATE = eND_DATE;
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
	public List getCtbkIdList() {
		return ctbkIdList;
	}
	public void setCtbkIdList(List ctbkIdList) {
		this.ctbkIdList = ctbkIdList;
	}
	public Map getDetailData() {
		return detailData;
	}
	public void setDetailData(Map detailData) {
		this.detailData = detailData;
	}
	public String getPageForSort() {
		return pageForSort;
	}
	public void setPageForSort(String pageForSort) {
		this.pageForSort = pageForSort;
	}
	public String getSerchStrs() {
		return serchStrs;
	}
	public void setSerchStrs(String serchStrs) {
		this.serchStrs = serchStrs;
	}
	public String getOrdForSort() {
		return ordForSort;
	}
	public void setOrdForSort(String ordForSort) {
		this.ordForSort = ordForSort;
	}
	public String getColForSort() {
		return colForSort;
	}
	public void setColForSort(String colForSort) {
		this.colForSort = colForSort;
	}
	public String getSrch_BIZDATE() {
		return srch_BIZDATE;
	}
	public void setSrch_BIZDATE(String srch_BIZDATE) {
		this.srch_BIZDATE = srch_BIZDATE;
	}
	public String getSrch_CLEARINGPHASE() {
		return srch_CLEARINGPHASE;
	}
	public void setSrch_CLEARINGPHASE(String srch_CLEARINGPHASE) {
		this.srch_CLEARINGPHASE = srch_CLEARINGPHASE;
	}
	public String getSrch_PCODE() {
		return srch_PCODE;
	}
	public void setSrch_PCODE(String srch_PCODE) {
		this.srch_PCODE = srch_PCODE;
	}
	public String getSrch_BANKID() {
		return srch_BANKID;
	}
	public void setSrch_BANKID(String srch_BANKID) {
		this.srch_BANKID = srch_BANKID;
	}
	public String getDow_token() {
		return dow_token;
	}
	public void setDow_token(String dow_token) {
		this.dow_token = dow_token;
	}
	
}
