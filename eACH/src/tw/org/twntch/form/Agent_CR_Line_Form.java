package tw.org.twntch.form;

import java.math.BigInteger;

public class Agent_CR_Line_Form extends CommonForm {

	
	private	String 	SND_COMPANY_ID	;
//	private	String 	USER_ID	;
//	private	BigInteger BASIC_CR_LINE;
//	private	BigInteger REST_CR_LINE	;
	private	String BASIC_CR_LINE;
	private	String REST_CR_LINE	;
	private	String  CDATE	;
	private	String  UDATE	;
	private	String  COMPANY_NAME	;
	private String OLD_BASIC_CR_LINE;
	private String OLD_REST_CR_LINE;
	
	
	public String getSND_COMPANY_ID() {
		return SND_COMPANY_ID;
	}
	public void setSND_COMPANY_ID(String sND_COMPANY_ID) {
		SND_COMPANY_ID = sND_COMPANY_ID;
	}
//	public String getUSER_ID() {
//		return USER_ID;
//	}
//	public void setUSER_ID(String uSER_ID) {
//		USER_ID = uSER_ID;
//	}
	
//	public BigInteger getBASIC_CR_LINE() {
//		return BASIC_CR_LINE;
//	}
//	public void setBASIC_CR_LINE(BigInteger bASIC_CR_LINE) {
//		BASIC_CR_LINE = bASIC_CR_LINE;
//	}
//	public BigInteger getREST_CR_LINE() {
//		return REST_CR_LINE;
//	}
//	public void setREST_CR_LINE(BigInteger rEST_CR_LINE) {
//		REST_CR_LINE = rEST_CR_LINE;
//	}
	
	public String getCDATE() {
		return CDATE;
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
	public void setCDATE(String cDATE) {
		CDATE = cDATE;
	}
	public String getUDATE() {
		return UDATE;
	}
	public void setUDATE(String uDATE) {
		UDATE = uDATE;
	}
	public String getCOMPANY_NAME() {
		return COMPANY_NAME;
	}
	public void setCOMPANY_NAME(String cOMPANY_NAME) {
		COMPANY_NAME = cOMPANY_NAME;
	}
	public String getOLD_BASIC_CR_LINE() {
		return OLD_BASIC_CR_LINE;
	}
	public void setOLD_BASIC_CR_LINE(String oLD_BASIC_CR_LINE) {
		OLD_BASIC_CR_LINE = oLD_BASIC_CR_LINE;
	}
	public String getOLD_REST_CR_LINE() {
		return OLD_REST_CR_LINE;
	}
	public void setOLD_REST_CR_LINE(String oLD_REST_CR_LINE) {
		OLD_REST_CR_LINE = oLD_REST_CR_LINE;
	}
	
	
	
}
