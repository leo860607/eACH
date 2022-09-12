package tw.org.twntch.form;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

public class Each_User_Form extends CommonForm {
	private	List	scaseary	;
	private	List	role_list	;
//	private	List	gbank_list	;
	private	String 	USER_COMPANY 	;
	private	String 	USER_ID	;
	private String USER_TYPE;
	private String USER_STATUS="N";
	private String USER_DESC;
	private String USE_IKEY = "N";
	private String ROLE_ID;
	private Integer NOLOGIN_EXPIRE_DAY;
	private String IP;
	private Integer IDLE_TIMEOUT;
	private String LAST_LOGIN_DATE = "";
	private String LAST_LOGIN_IP;
	private String CDATE;
	private String UDATE;
	private String TMPUSER_TYPE;
	
	
	public List getScaseary() {
		return scaseary;
	}


	public void setScaseary(List scaseary) {
		this.scaseary = scaseary;
	}


	public List getRole_list() {
		return role_list;
	}


	public void setRole_list(List role_list) {
		this.role_list = role_list;
	}


//	public List getGbank_list() {
//		return gbank_list;
//	}
//
//
//	public void setGbank_list(List gbank_list) {
//		this.gbank_list = gbank_list;
//	}


	public String getUSER_COMPANY() {
		return USER_COMPANY;
	}


	public void setUSER_COMPANY(String uSER_COMPANY) {
		USER_COMPANY = uSER_COMPANY;
	}


	public String getUSER_ID() {
		return USER_ID;
	}


	public void setUSER_ID(String uSER_ID) {
		USER_ID = uSER_ID;
	}


	public String getUSER_TYPE() {
		return USER_TYPE;
	}


	public void setUSER_TYPE(String uSER_TYPE) {
		USER_TYPE = uSER_TYPE;
	}


	public String getUSER_STATUS() {
		return USER_STATUS;
	}


	public void setUSER_STATUS(String uSER_STATUS) {
		USER_STATUS = uSER_STATUS;
	}


	public String getUSER_DESC() {
		return USER_DESC;
	}


	public void setUSER_DESC(String uSER_DESC) {
		USER_DESC = uSER_DESC;
	}


	public String getUSE_IKEY() {
		return USE_IKEY;
	}


	public void setUSE_IKEY(String uSE_IKEY) {
		USE_IKEY = uSE_IKEY;
	}


	public String getROLE_ID() {
		return ROLE_ID;
	}


	public void setROLE_ID(String rOLE_ID) {
		ROLE_ID = rOLE_ID;
	}


	public Integer getNOLOGIN_EXPIRE_DAY() {
		return NOLOGIN_EXPIRE_DAY;
	}


	public void setNOLOGIN_EXPIRE_DAY(Integer nOLOGIN_EXPIRE_DAY) {
		NOLOGIN_EXPIRE_DAY = nOLOGIN_EXPIRE_DAY;
	}


	public String getIP() {
		return IP;
	}


	public void setIP(String iP) {
		IP = iP;
	}


	public Integer getIDLE_TIMEOUT() {
		return IDLE_TIMEOUT;
	}


	public void setIDLE_TIMEOUT(Integer iDLE_TIMEOUT) {
		IDLE_TIMEOUT = iDLE_TIMEOUT;
	}


	public String getLAST_LOGIN_DATE() {
		return LAST_LOGIN_DATE;
	}


	public void setLAST_LOGIN_DATE(String lAST_LOGIN_DATE) {
		LAST_LOGIN_DATE = lAST_LOGIN_DATE;
	}


	public String getLAST_LOGIN_IP() {
		return LAST_LOGIN_IP;
	}


	public void setLAST_LOGIN_IP(String lAST_LOGIN_IP) {
		LAST_LOGIN_IP = lAST_LOGIN_IP;
	}


	public String getCDATE() {
		return CDATE;
	}


	public void setCDATE(String cDATE) {
		CDATE = cDATE;
	}


	public String getUDATE() {
		return UDATE;
	}


	public void setUDATE(String uDATE) {
		UDATE = uDATE;
	}


	public String getTMPUSER_TYPE() {
		return TMPUSER_TYPE;
	}


	public void setTMPUSER_TYPE(String tMPUSER_TYPE) {
		TMPUSER_TYPE = tMPUSER_TYPE;
	}


	@Override
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		// TODO Auto-generated method stub
		super.reset(mapping, request);
		USER_COMPANY =""	;
		USER_ID = ""	;
	}

	
	
	
}
