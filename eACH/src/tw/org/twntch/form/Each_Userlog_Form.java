package tw.org.twntch.form;

import java.util.List;

public class Each_Userlog_Form extends CommonForm {
    /**
	 * 
	 */
//	private static final long serialVersionUID = -2026944867513970378L;
	private String SERNO;
	private String USER_TYPE;
    private String USER_COMPANY;
    private String USERID;
    private String USERIP;
	private String TXTIME;
	private String TXTIME_1;
	private String TXTIME_2;
	private String FUNC_TYPE;
	private String UP_FUNC_ID;
	private String FUNC_ID;
	private String OPITEM;
	private String BFCHCON;
	private String AFCHCON;
	private String ADEXCODE;
    private List userIdList;
    private List userCompanyList;
    private List opt_bankList;
    private List funcList;
    private String opt_id ;
    private String fc_type ;
    private String ROLE_TYPE="B" ;
	public String getSERNO() {
		return SERNO;
	}
	public void setSERNO(String sERNO) {
		SERNO = sERNO;
	}
	
	
	public String getUSER_TYPE() {
		return USER_TYPE;
	}
	public void setUSER_TYPE(String uSER_TYPE) {
		USER_TYPE = uSER_TYPE;
	}
	public String getUSER_COMPANY() {
		return USER_COMPANY;
	}
	public void setUSER_COMPANY(String uSER_COMPANY) {
		USER_COMPANY = uSER_COMPANY;
	}
	public String getUSERID() {
		return USERID;
	}
	public void setUSERID(String uSERID) {
		USERID = uSERID;
	}
	public String getUSERIP() {
		return USERIP;
	}
	public void setUSERIP(String uSERIP) {
		USERIP = uSERIP;
	}
	public String getTXTIME() {
		return TXTIME;
	}
	public void setTXTIME(String tXTIME) {
		TXTIME = tXTIME;
	}
	public String getTXTIME_1() {
		return TXTIME_1;
	}
	public void setTXTIME_1(String tXTIME_1) {
		TXTIME_1 = tXTIME_1;
	}
	public String getTXTIME_2() {
		return TXTIME_2;
	}
	public void setTXTIME_2(String tXTIME_2) {
		TXTIME_2 = tXTIME_2;
	}
	public String getFUNC_TYPE() {
		return FUNC_TYPE;
	}
	public void setFUNC_TYPE(String fUNC_TYPE) {
		FUNC_TYPE = fUNC_TYPE;
	}
	public String getUP_FUNC_ID() {
		return UP_FUNC_ID;
	}
	public void setUP_FUNC_ID(String uP_FUNC_ID) {
		UP_FUNC_ID = uP_FUNC_ID;
	}
	public String getFUNC_ID() {
		return FUNC_ID;
	}
	public void setFUNC_ID(String fUNC_ID) {
		FUNC_ID = fUNC_ID;
	}
	public String getOPITEM() {
		return OPITEM;
	}
	public void setOPITEM(String oPITEM) {
		OPITEM = oPITEM;
	}
	public String getBFCHCON() {
		return BFCHCON;
	}
	public void setBFCHCON(String bFCHCON) {
		BFCHCON = bFCHCON;
	}
	public String getAFCHCON() {
		return AFCHCON;
	}
	public void setAFCHCON(String aFCHCON) {
		AFCHCON = aFCHCON;
	}
	public String getADEXCODE() {
		return ADEXCODE;
	}
	public void setADEXCODE(String aDEXCODE) {
		ADEXCODE = aDEXCODE;
	}
	public List getUserIdList() {
		return userIdList;
	}
	public void setUserIdList(List userIdList) {
		this.userIdList = userIdList;
	}
	public List getUserCompanyList() {
		return userCompanyList;
	}
	public void setUserCompanyList(List userCompanyList) {
		this.userCompanyList = userCompanyList;
	}
	public List getFuncList() {
		return funcList;
	}
	public void setFuncList(List funcList) {
		this.funcList = funcList;
	}
	public List getOpt_bankList() {
		return opt_bankList;
	}
	public void setOpt_bankList(List opt_bankList) {
		this.opt_bankList = opt_bankList;
	}
	public String getOpt_id() {
		return opt_id;
	}
	public void setOpt_id(String opt_id) {
		this.opt_id = opt_id;
	}
	public String getFc_type() {
		return fc_type;
	}
	public void setFc_type(String fc_type) {
		this.fc_type = fc_type;
	}
	public String getROLE_TYPE() {
		return ROLE_TYPE;
	}
	public void setROLE_TYPE(String rOLE_TYPE) {
		ROLE_TYPE = rOLE_TYPE;
	}
    
    
}
