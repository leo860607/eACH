package tw.org.twntch.form;

import java.util.List;

public class Fee_Adj_Form extends CommonForm{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4352131397487668852L;
	private String YYYYMM;
	private String BRBK_ID;
	private String BRBK_NAME;
	private String FEE;
	private String BGBK_ID;
	private List brbkIdList;
	private List bgbkIdList;
	private String pageForSort;
	public String getYYYYMM() {
		return YYYYMM;
	}
	public void setYYYYMM(String yYYYMM) {
		YYYYMM = yYYYMM;
	}
	public String getBRBK_ID() {
		return BRBK_ID;
	}
	public void setBRBK_ID(String bRBK_ID) {
		BRBK_ID = bRBK_ID;
	}
	
	public String getBRBK_NAME() {
		return BRBK_NAME;
	}
	public void setBRBK_NAME(String bRBK_NAME) {
		BRBK_NAME = bRBK_NAME;
	}
	public String getFEE() {
		return FEE;
	}
	public void setFEE(String fEE) {
		FEE = fEE;
	}
	public String getBGBK_ID() {
		return BGBK_ID;
	}
	public void setBGBK_ID(String bGBK_ID) {
		BGBK_ID = bGBK_ID;
	}
	public List getBrbkIdList() {
		return brbkIdList;
	}
	public void setBrbkIdList(List brbkIdList) {
		this.brbkIdList = brbkIdList;
	}
	public List getBgbkIdList() {
		return bgbkIdList;
	}
	public void setBgbkIdList(List bgbkIdList) {
		this.bgbkIdList = bgbkIdList;
	}
	public String getPageForSort() {
		return pageForSort;
	}
	public void setPageForSort(String pageForSort) {
		this.pageForSort = pageForSort;
	}
}
