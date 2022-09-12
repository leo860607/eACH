package tw.org.twntch.form;

import java.util.List;

public class Bank_Branch_Form extends CommonForm {

	
	private	List	scaseary	;
	private	List	bgIdList	;
	private	String	BGBK_ID	;
	private	String	BRBK_ID ;
	private	String	BRBK_NAME	;
	private	String	TCH_ID	;
	private	String	SYNCSPDATE = "Y"	;//分行的停用日期是否與總行的停用日期同步，預設是同步
	private	String	ACTIVE_DATE	;
	private	String	STOP_DATE	;
	
	
	public List getScaseary() {
		return scaseary;
	}
	public void setScaseary(List scaseary) {
		this.scaseary = scaseary;
	}
	public List getBgIdList() {
		return bgIdList;
	}
	public void setBgIdList(List bgIdList) {
		this.bgIdList = bgIdList;
	}
	public String getBGBK_ID() {
		return BGBK_ID;
	}
	public void setBGBK_ID(String bGBK_ID) {
		BGBK_ID = bGBK_ID;
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
	public String getTCH_ID() {
		return TCH_ID;
	}
	public void setTCH_ID(String tCH_ID) {
		TCH_ID = tCH_ID;
	}
	public String getACTIVE_DATE() {
		return ACTIVE_DATE;
	}
	public void setACTIVE_DATE(String aCTIVE_DATE) {
		ACTIVE_DATE = aCTIVE_DATE;
	}
	public String getSTOP_DATE() {
		return STOP_DATE;
	}
	public void setSTOP_DATE(String sTOP_DATE) {
		STOP_DATE = sTOP_DATE;
	}
	public String getSYNCSPDATE() {
		return SYNCSPDATE;
	}
	public void setSYNCSPDATE(String sYNCSPDATE) {
		SYNCSPDATE = sYNCSPDATE;
	}
	
	
}
