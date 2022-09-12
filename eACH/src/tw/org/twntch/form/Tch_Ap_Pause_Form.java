package tw.org.twntch.form;

public class Tch_Ap_Pause_Form extends CommonForm{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3913697536263555382L;
	private String BGBK_ID;
	//IKey使用
	private String RAOName;//的帳號
	private String signvalue;//用頁面getSignedPKCS7StrByPara的function取得的簽章字串	
//	eToken使用
	private String cardType;
	private String method;
	private String CompanyID;
	private String EmployeeID;
	private String LoginType;
	private String browserType;
	
	public String getBGBK_ID() {
		return BGBK_ID;
	}

	public void setBGBK_ID(String bGBK_ID) {
		BGBK_ID = bGBK_ID;
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
	
}
