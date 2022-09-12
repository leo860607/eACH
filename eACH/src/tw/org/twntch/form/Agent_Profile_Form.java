package tw.org.twntch.form;

public class Agent_Profile_Form extends CommonForm{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2680633780042439664L;
	private String COMPANY_ID;
	private String COMPANY_ABBR_NAME;
	private String COMPANY_NAME;
	private String ACTIVE_DATE;
	private String STOP_DATE;
	private String WS_URL;
	private String WS_NAME_SPACE;
	private String KEY_ID;
	private String COMPANY_NO;
	private String KEY_FLAG;
	
	public String getCOMPANY_ID() {
		return COMPANY_ID;
	}
	public void setCOMPANY_ID(String cOMPANY_ID) {
		COMPANY_ID = cOMPANY_ID;
	}
	public String getCOMPANY_ABBR_NAME() {
		return COMPANY_ABBR_NAME;
	}
	public void setCOMPANY_ABBR_NAME(String cOMPANY_ABBR_NAME) {
		COMPANY_ABBR_NAME = cOMPANY_ABBR_NAME;
	}
	public String getCOMPANY_NAME() {
		return COMPANY_NAME;
	}
	public void setCOMPANY_NAME(String cOMPANY_NAME) {
		COMPANY_NAME = cOMPANY_NAME;
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
	public String getWS_URL() {
		return WS_URL;
	}
	public void setWS_URL(String wS_URL) {
		WS_URL = wS_URL;
	}
	public String getWS_NAME_SPACE() {
		return WS_NAME_SPACE;
	}
	public void setWS_NAME_SPACE(String wS_NAME_SPACE) {
		WS_NAME_SPACE = wS_NAME_SPACE;
	}
	public String getKEY_ID() {
		return KEY_ID;
	}
	public void setKEY_ID(String kEY_ID) {
		KEY_ID = kEY_ID;
	}
	public String getCOMPANY_NO() {
		return COMPANY_NO;
	}
	public void setCOMPANY_NO(String cOMPANY_NO) {
		COMPANY_NO = cOMPANY_NO;
	}
	
	public String getKEY_FLAG() {
		return KEY_FLAG;
	}
	public void setKEY_FLAG(String kEY_FLAG) {
		KEY_FLAG = kEY_FLAG;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Agent_Profile_Form [COMPANY_ID=");
		builder.append(COMPANY_ID);
		builder.append(", COMPANY_ABBR_NAME=");
		builder.append(COMPANY_ABBR_NAME);
		builder.append(", COMPANY_NAME=");
		builder.append(COMPANY_NAME);
		builder.append(", ACTIVE_DATE=");
		builder.append(ACTIVE_DATE);
		builder.append(", STOP_DATE=");
		builder.append(STOP_DATE);
		builder.append(", WS_URL=");
		builder.append(WS_URL);
		builder.append(", WS_NAME_SPACE=");
		builder.append(WS_NAME_SPACE);
		builder.append(", KEY_ID=");
		builder.append(KEY_ID);
		builder.append(", COMPANY_NO=");
		builder.append(COMPANY_NO);
		builder.append("]");
		return builder.toString();
	}

}
