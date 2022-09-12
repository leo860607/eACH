package tw.org.twntch.form;

import java.util.List;

public class Undone_Txdata_Form extends CommonForm {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3743571279535662345L;
	private String START_DATE;
	private String END_DATE;
	private String TXDATE;
	private String STAN;
	private String OSTAN;
	private String RESULTCODE;
	private String OPBK_ID;
	private String BGBK_ID;
	private String CLEARINGPHASE;
	private String BUSINESS_TYPE_ID;
	private List scaseary;
	private List opbkIdList;
	private List bsTypeList;
	private String sourcePage;	//來源網頁
	private String pageForSort;
	private String FLBIZDATE;//整批營業日
	private String FLBATCHSEQ;//整批處理序號(檔名)
	private String FLPROCSEQ;//整批處理序號
	private String DATASEQ;//資料序號
	private String FILTER_BAT;//是否過濾整批
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
	public String getTXDATE() {
		return TXDATE;
	}
	public void setTXDATE(String tXDATE) {
		TXDATE = tXDATE;
	}
	public String getSTAN() {
		return STAN;
	}
	public void setSTAN(String sTAN) {
		STAN = sTAN;
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
	public String getCLEARINGPHASE() {
		return CLEARINGPHASE;
	}
	public void setCLEARINGPHASE(String cLEARINGPHASE) {
		CLEARINGPHASE = cLEARINGPHASE;
	}
	public List getScaseary() {
		return scaseary;
	}
	public void setScaseary(List scaseary) {
		this.scaseary = scaseary;
	}
	public List getOpbkIdList() {
		return opbkIdList;
	}
	public void setOpbkIdList(List opbkIdList) {
		this.opbkIdList = opbkIdList;
	}
	public List getBsTypeList() {
		return bsTypeList;
	}
	public void setBsTypeList(List bsTypeList) {
		this.bsTypeList = bsTypeList;
	}
	public String getBUSINESS_TYPE_ID() {
		return BUSINESS_TYPE_ID;
	}
	public void setBUSINESS_TYPE_ID(String bUSINESS_TYPE_ID) {
		BUSINESS_TYPE_ID = bUSINESS_TYPE_ID;
	}
	public String getSourcePage() {
		return sourcePage;
	}
	public void setSourcePage(String sourcePage) {
		this.sourcePage = sourcePage;
	}
	public String getPageForSort() {
		return pageForSort;
	}
	public void setPageForSort(String pageForSort) {
		this.pageForSort = pageForSort;
	}
	public String getFLBIZDATE() {
		return FLBIZDATE;
	}
	public void setFLBIZDATE(String fLBIZDATE) {
		FLBIZDATE = fLBIZDATE;
	}
	public String getFLBATCHSEQ() {
		return FLBATCHSEQ;
	}
	public void setFLBATCHSEQ(String fLBATCHSEQ) {
		FLBATCHSEQ = fLBATCHSEQ;
	}
	public String getFLPROCSEQ() {
		return FLPROCSEQ;
	}
	public void setFLPROCSEQ(String fLPROCSEQ) {
		FLPROCSEQ = fLPROCSEQ;
	}
	public String getDATASEQ() {
		return DATASEQ;
	}
	public void setDATASEQ(String dATASEQ) {
		DATASEQ = dATASEQ;
	}
	public String getFILTER_BAT() {
		return FILTER_BAT;
	}
	public void setFILTER_BAT(String fILTER_BAT) {
		FILTER_BAT = fILTER_BAT;
	}
	public String getOSTAN() {
		return OSTAN;
	}
	public void setOSTAN(String oSTAN) {
		OSTAN = oSTAN;
	}
	public String getRESULTCODE() {
		return RESULTCODE;
	}
	public void setRESULTCODE(String rESULTCODE) {
		RESULTCODE = rESULTCODE;
	}

	
}
