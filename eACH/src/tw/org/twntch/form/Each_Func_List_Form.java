package tw.org.twntch.form;

import java.util.List;

public class Each_Func_List_Form extends CommonForm {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8359338583837649313L;
	private String FUNC_ID;
	private String FUNC_NAME;
	private String FUNC_TYPE;
	private String FUNC_SEQ;
	private String UP_FUNC_ID;
	private String FUNC_DESC;
	private String FUNC_URL;
	private String USE_IKEY = "N";
	private String TCH_FUNC;
	private String BANK_FUNC;
	private String COMPANY_FUNC;
	private String PROXY_FUNC;
	private String IS_USED = "N";
	private String START_DATE;
	private String IS_RECORD;
	private String FUNC_NAME_BK;
	private List   funcIdList;
	
	public String getFUNC_ID() {
		return FUNC_ID;
	}
	public void setFUNC_ID(String fUNC_ID) {
		FUNC_ID = fUNC_ID;
	}
	public String getFUNC_NAME() {
		return FUNC_NAME;
	}
	public void setFUNC_NAME(String fUNC_NAME) {
		FUNC_NAME = fUNC_NAME;
	}
	public String getFUNC_TYPE() {
		return FUNC_TYPE;
	}
	public void setFUNC_TYPE(String fUNC_TYPE) {
		FUNC_TYPE = fUNC_TYPE;
	}
	public String getFUNC_SEQ() {
		return FUNC_SEQ;
	}
	public void setFUNC_SEQ(String fUNC_SEQ) {
		FUNC_SEQ = fUNC_SEQ;
	}
	public String getUP_FUNC_ID() {
		return UP_FUNC_ID;
	}
	public void setUP_FUNC_ID(String uP_FUNC_ID) {
		UP_FUNC_ID = uP_FUNC_ID;
	}
	public String getFUNC_DESC() {
		return FUNC_DESC;
	}
	public void setFUNC_DESC(String fUNC_DESC) {
		FUNC_DESC = fUNC_DESC;
	}
	public String getFUNC_URL() {
		return FUNC_URL;
	}
	public void setFUNC_URL(String fUNC_URL) {
		FUNC_URL = fUNC_URL;
	}
	public String getUSE_IKEY() {
		return USE_IKEY;
	}
	public void setUSE_IKEY(String uSE_IKEY) {
		USE_IKEY = uSE_IKEY;
	}
	public String getTCH_FUNC() {
		return TCH_FUNC;
	}
	public void setTCH_FUNC(String tCH_FUNC) {
		TCH_FUNC = tCH_FUNC;
	}
	public String getBANK_FUNC() {
		return BANK_FUNC;
	}
	public void setBANK_FUNC(String bANK_FUNC) {
		BANK_FUNC = bANK_FUNC;
	}
	public String getCOMPANY_FUNC() {
		return COMPANY_FUNC;
	}
	public void setCOMPANY_FUNC(String cOMPANY_FUNC) {
		COMPANY_FUNC = cOMPANY_FUNC;
	}
	public String getPROXY_FUNC() {
		return PROXY_FUNC;
	}
	public void setPROXY_FUNC(String pROXY_FUNC) {
		PROXY_FUNC = pROXY_FUNC;
	}
	public String getIS_USED() {
		return IS_USED;
	}
	public void setIS_USED(String iS_USED) {
		IS_USED = iS_USED;
	}
	public String getSTART_DATE() {
		return START_DATE;
	}
	public void setSTART_DATE(String sTART_DATE) {
		START_DATE = sTART_DATE;
	}
	public List getFuncIdList() {
		return funcIdList;
	}
	public void setFuncIdList(List funcIdList) {
		this.funcIdList = funcIdList;
	}
	public String getIS_RECORD() {
		return IS_RECORD;
	}
	public void setIS_RECORD(String iS_RECORD) {
		IS_RECORD = iS_RECORD;
	}
	public String getFUNC_NAME_BK() {
		return FUNC_NAME_BK;
	}
	public void setFUNC_NAME_BK(String fUNC_NAME_BK) {
		FUNC_NAME_BK = fUNC_NAME_BK;
	}
	
	
	
}
