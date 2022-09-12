package tw.org.twntch.form;

import java.util.List;

import org.apache.struts.action.ActionForm;

public class Bank_Group_Form extends CommonForm {

	private	String	BGBK_ID	;
	private	String	BGBK_NAME	;
	private	String	BGBK_ATTR	;
	private	String	CTBK_ACCT	;
	private	String	TCH_ID	;
	private	String	OPBK_ID	;
	private	String	OPBK_NAME	;
	private	String	CTBK_ID	;
	private	String	CTBK_NAME	;
	private	String	ACTIVE_DATE	;
	private	String	STOP_DATE	;
	private	String	SND_FEE_BRBK_ID	;
	private	String	OUT_FEE_BRBK_ID	;
	private	String	IN_FEE_BRBK_ID	;
	private	String	EDDA_FLAG	;
	private	List	bgbkIdList	;
	private	List	scaseary	;
	private String	scaseJson	;
	private	String	bsTypeAry[]	;
	private	List	bsTypeList	;
	private	String	selectedBsTypeAry[]	;
	private	List	selectedBsTypeList	;
	private List	opbkIdList;
	private List 	ctbkIdList;
	private String  BASIC_CR_LINE;
	private String  REST_CR_LINE;
	private String  isEditCR;
	private String  OP_START_DATE;
	private String  CT_START_DATE;
	private	String	HR_UPLOAD_MAX_FILE	;
	private	String	FILE_MAX_CNT	;
	private	String	OP_NOTE	;
	private	String	CT_NOTE	;
	private	String	IS_EACH	="";
	private	String	WO_FEE_BRBK_ID	;
	
	
	
	public String getBGBK_ID() {
		return BGBK_ID;
	}
	public void setBGBK_ID(String bGBK_ID) {
		BGBK_ID = bGBK_ID;
	}
	public String getBGBK_NAME() {
		return BGBK_NAME;
	}
	public void setBGBK_NAME(String bGBK_NAME) {
		BGBK_NAME = bGBK_NAME;
	}
	public String getBGBK_ATTR() {
		return BGBK_ATTR;
	}
	public void setBGBK_ATTR(String bGBK_ATTR) {
		BGBK_ATTR = bGBK_ATTR;
	}
	public String getCTBK_ACCT() {
		return CTBK_ACCT;
	}
	public void setCTBK_ACCT(String cTBK_ACCT) {
		CTBK_ACCT = cTBK_ACCT;
	}
	public String getTCH_ID() {
		return TCH_ID;
	}
	public void setTCH_ID(String tCH_ID) {
		TCH_ID = tCH_ID;
	}
	public String getOPBK_ID() {
		return OPBK_ID;
	}
	public void setOPBK_ID(String oPBK_ID) {
		OPBK_ID = oPBK_ID;
	}
	public String getOPBK_NAME() {
		return OPBK_NAME;
	}
	public void setOPBK_NAME(String oPBK_NAME) {
		OPBK_NAME = oPBK_NAME;
	}
	public String getCTBK_ID() {
		return CTBK_ID;
	}
	public void setCTBK_ID(String cTBK_ID) {
		CTBK_ID = cTBK_ID;
	}
	public String getCTBK_NAME() {
		return CTBK_NAME;
	}
	public void setCTBK_NAME(String cTBK_NAME) {
		CTBK_NAME = cTBK_NAME;
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
	public String getSND_FEE_BRBK_ID() {
		return SND_FEE_BRBK_ID;
	}
	public void setSND_FEE_BRBK_ID(String sND_FEE_BRBK_ID) {
		SND_FEE_BRBK_ID = sND_FEE_BRBK_ID;
	}
	public String getOUT_FEE_BRBK_ID() {
		return OUT_FEE_BRBK_ID;
	}
	public void setOUT_FEE_BRBK_ID(String oUT_FEE_BRBK_ID) {
		OUT_FEE_BRBK_ID = oUT_FEE_BRBK_ID;
	}
	public String getIN_FEE_BRBK_ID() {
		return IN_FEE_BRBK_ID;
	}
	public void setIN_FEE_BRBK_ID(String iN_FEE_BRBK_ID) {
		IN_FEE_BRBK_ID = iN_FEE_BRBK_ID;
	}
	public String getEDDA_FLAG() {
		return EDDA_FLAG;
	}
	public void setEDDA_FLAG(String eDDA_FLAG) {
		EDDA_FLAG = eDDA_FLAG;
	}
	public List getBgbkIdList() {
		return bgbkIdList;
	}
	public void setBgbkIdList(List bgbkIdList) {
		this.bgbkIdList = bgbkIdList;
	}
	public List getScaseary() {
		return scaseary;
	}
	public void setScaseary(List scaseary) {
		this.scaseary = scaseary;
	}
	public String getScaseJson() {
		return scaseJson;
	}
	public void setScaseJson(String scaseJson) {
		this.scaseJson = scaseJson;
	}
	public String[] getBsTypeAry() {
		return bsTypeAry;
	}
	public void setBsTypeAry(String[] bsTypeAry) {
		this.bsTypeAry = bsTypeAry;
	}
	public List getBsTypeList() {
		return bsTypeList;
	}
	public void setBsTypeList(List bsTypeList) {
		this.bsTypeList = bsTypeList;
	}
	public String[] getSelectedBsTypeAry() {
		return selectedBsTypeAry;
	}
	public void setSelectedBsTypeAry(String[] selectedBsTypeAry) {
		this.selectedBsTypeAry = selectedBsTypeAry;
	}
	public List getSelectedBsTypeList() {
		return selectedBsTypeList;
	}
	public void setSelectedBsTypeList(List selectedBsTypeList) {
		this.selectedBsTypeList = selectedBsTypeList;
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
	public String getBASIC_CR_LINE() {
		return BASIC_CR_LINE;
	}
	public void setBASIC_CR_LINE(String bASIC_CR_LINE) {
		BASIC_CR_LINE = bASIC_CR_LINE;
	}
	public String getREST_CR_LINE() {
		return REST_CR_LINE;
	}
	public void setREST_CR_LINE(String rEST_CR_LINE) {
		REST_CR_LINE = rEST_CR_LINE;
	}
	public String getIsEditCR() {
		return isEditCR;
	}
	public void setIsEditCR(String isEditCR) {
		this.isEditCR = isEditCR;
	}
	public String getOP_START_DATE() {
		return OP_START_DATE;
	}
	public void setOP_START_DATE(String oP_START_DATE) {
		OP_START_DATE = oP_START_DATE;
	}
	public String getCT_START_DATE() {
		return CT_START_DATE;
	}
	public void setCT_START_DATE(String cT_START_DATE) {
		CT_START_DATE = cT_START_DATE;
	}
	public String getHR_UPLOAD_MAX_FILE() {
		return HR_UPLOAD_MAX_FILE;
	}
	public void setHR_UPLOAD_MAX_FILE(String hR_UPLOAD_MAX_FILE) {
		HR_UPLOAD_MAX_FILE = hR_UPLOAD_MAX_FILE;
	}
	public String getOP_NOTE() {
		return OP_NOTE;
	}
	public void setOP_NOTE(String oP_NOTE) {
		OP_NOTE = oP_NOTE;
	}
	public String getCT_NOTE() {
		return CT_NOTE;
	}
	public void setCT_NOTE(String cT_NOTE) {
		CT_NOTE = cT_NOTE;
	}
	public String getIS_EACH() {
		return IS_EACH;
	}
	public void setIS_EACH(String iS_EACH) {
		IS_EACH = iS_EACH;
	}
	public String getFILE_MAX_CNT() {
		return FILE_MAX_CNT;
	}
	public void setFILE_MAX_CNT(String fILE_MAX_CNT) {
		FILE_MAX_CNT = fILE_MAX_CNT;
	}
	public String getWO_FEE_BRBK_ID() {
		return WO_FEE_BRBK_ID;
	}
	public void setWO_FEE_BRBK_ID(String wO_FEE_BRBK_ID) {
		WO_FEE_BRBK_ID = wO_FEE_BRBK_ID;
	}
	
	
	
	
}
