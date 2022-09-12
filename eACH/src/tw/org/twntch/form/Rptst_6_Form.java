package tw.org.twntch.form;

import java.util.List;

public class Rptst_6_Form extends CommonForm {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6223418012176166642L;
	private String START_MONTH;
	private String START_YEAR;
	private String END_MONTH;
	private String END_YEAR;
    private String OP_TYPE; //操作行角色
	private String dow_token ; //檔案下載判別用
	public String getSTART_MONTH() {
		return START_MONTH;
	}
	public void setSTART_MONTH(String sTART_MONTH) {
		START_MONTH = sTART_MONTH;
	}
	public String getEND_MONTH() {
		return END_MONTH;
	}
	public void setEND_MONTH(String eND_MONTH) {
		END_MONTH = eND_MONTH;
	}
	public String getSTART_YEAR() {
		return START_YEAR;
	}
	public void setSTART_YEAR(String sTART_YEAR) {
		START_YEAR = sTART_YEAR;
	}
	public String getEND_YEAR() {
		return END_YEAR;
	}
	public void setEND_YEAR(String eND_YEAR) {
		END_YEAR = eND_YEAR;
	}
	public String getOP_TYPE() {
		return OP_TYPE;
	}
	public void setOP_TYPE(String oP_TYPE) {
		OP_TYPE = oP_TYPE;
	}
	public String getDow_token() {
		return dow_token;
	}
	public void setDow_token(String dow_token) {
		this.dow_token = dow_token;
	}
}
