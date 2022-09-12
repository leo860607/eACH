package tw.org.twntch.form;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

public class Login_Form extends CommonForm{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2660281177423438063L;
	private String userCompany;
	private String userId;
	private List scaseary;
	private List breadcrumb;
	private Each_User_Form userData;
//	20150402 edit by hugo 給空字串 否則null時登入頁面會出錯
//	private String THIS_LOGIN_DATE;
	private String THIS_LOGIN_DATE = "";
	private String THIS_LOGIN_IP;
	private String IS_PROXY;//判斷登入者是否為代理清算行
	//IKey使用
	private String RAOName;//的帳號
	private String signvalue;//用頁面getSignedPKCS7StrByPara的function取得的簽章字串
	private String ikeyValidateDate;//IKey的有效日期
	private String ikeyExtendURL;//IKey展期的URL
	private String isFormal;//判斷是否為正式環境
	//
//	eToken使用
	private String cardType;
	private String method;
	private String CompanyID;
	private String EmployeeID;
	private String LoginType;
	private String browserType;
	
	public String getUserCompany() {
		return userCompany;
	}

	public void setUserCompany(String userCompany) {
		this.userCompany = userCompany;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public List getScaseary() {
		return scaseary;
	}

	public void setScaseary(List scaseary) {
		this.scaseary = scaseary;
	}
	
	public Each_User_Form getUserData() {
		return userData;
	}

	public void setUserData(Each_User_Form userData) {
		this.userData = userData;
	}

	public List getBreadcrumb() {
		return breadcrumb;
	}

	public void setBreadcrumb(List breadcrumb) {
		this.breadcrumb = breadcrumb;
	}
	public String getRAOName() {
		return RAOName;
	}
	public void setRAOName(String rAOName) {
		RAOName = rAOName;
	}
	public String getSignvalue() {
		return signvalue;
	}
	public void setSignvalue(String signvalue) {
		this.signvalue = signvalue;
	}
	public String getIkeyValidateDate() {
		return ikeyValidateDate;
	}
	public void setIkeyValidateDate(String ikeyValidateDate) {
		this.ikeyValidateDate = ikeyValidateDate;
	}
	public String getIkeyExtendURL() {
		return ikeyExtendURL;
	}
	public void setIkeyExtendURL(String ikeyExtendURL) {
		this.ikeyExtendURL = ikeyExtendURL;
	}

	public String getTHIS_LOGIN_DATE() {
		return THIS_LOGIN_DATE;
	}

	public void setTHIS_LOGIN_DATE(String tHIS_LOGIN_DATE) {
		THIS_LOGIN_DATE = tHIS_LOGIN_DATE;
	}

	public String getTHIS_LOGIN_IP() {
		return THIS_LOGIN_IP;
	}

	public void setTHIS_LOGIN_IP(String tHIS_LOGIN_IP) {
		THIS_LOGIN_IP = tHIS_LOGIN_IP;
	}
	

	public String getIS_PROXY() {
		return IS_PROXY;
	}

	public void setIS_PROXY(String iS_PROXY) {
		IS_PROXY = iS_PROXY;
	}

	
	
	public String getIsFormal() {
		return isFormal;
	}

	public void setIsFormal(String isFormal) {
		this.isFormal = isFormal;
	}
	
//	eToken使用	
	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getCompanyID() {
		return CompanyID;
	}

	public void setCompanyID(String companyID) {
		CompanyID = companyID;
	}

	public String getEmployeeID() {
		return EmployeeID;
	}

	public void setEmployeeID(String employeeID) {
		EmployeeID = employeeID;
	}

	public String getLoginType() {
		return LoginType;
	}

	public void setLoginType(String loginType) {
		LoginType = loginType;
	}

	public String getBrowserType() {
		return browserType;
	}

	public void setBrowserType(String browserType) {
		this.browserType = browserType;
	}

	@Override
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		super.reset(mapping, request);
		this.userId = "";
		userData=null;
	}
}
