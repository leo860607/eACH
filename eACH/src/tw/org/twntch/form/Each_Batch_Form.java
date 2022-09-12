package tw.org.twntch.form;

import java.util.List;

public class Each_Batch_Form extends CommonForm{

	private	List	scaseary	;
	private String RP_CLEARPHASE1_TIME ;
	private String RP_CLEARPHASE2_TIME ;
	private	String    	AP1 	;
	private	String    	AP2 	;
	private	String    	AP1_ISRUN 	;
	private	String    	AP2_ISRUN 	;
	private String BIZDATE ;
	private String CLEARINGPHASE ;
	private String PREVDATE;//前一個營業日
	private String THISDATE;//當前營業日
	
	private String CUR_BIZDATE;//當前營業日
	private String CUR_CLEARINGPHASE;//當前營業日
	private String PRE_BIZDATE;//當前營業日
	private String PRE_CLEARINGPHASE;//當前營業日
	private String SYSDATE;//系統日
	public List getScaseary() {
		return scaseary;
	}
	public void setScaseary(List scaseary) {
		this.scaseary = scaseary;
	}
	public String getRP_CLEARPHASE1_TIME() {
		return RP_CLEARPHASE1_TIME;
	}
	public void setRP_CLEARPHASE1_TIME(String rP_CLEARPHASE1_TIME) {
		RP_CLEARPHASE1_TIME = rP_CLEARPHASE1_TIME;
	}
	public String getRP_CLEARPHASE2_TIME() {
		return RP_CLEARPHASE2_TIME;
	}
	public void setRP_CLEARPHASE2_TIME(String rP_CLEARPHASE2_TIME) {
		RP_CLEARPHASE2_TIME = rP_CLEARPHASE2_TIME;
	}
	public String getAP1() {
		return AP1;
	}
	public void setAP1(String aP1) {
		AP1 = aP1;
	}
	public String getAP2() {
		return AP2;
	}
	public void setAP2(String aP2) {
		AP2 = aP2;
	}
	public String getAP1_ISRUN() {
		return AP1_ISRUN;
	}
	public void setAP1_ISRUN(String aP1_ISRUN) {
		AP1_ISRUN = aP1_ISRUN;
	}
	public String getAP2_ISRUN() {
		return AP2_ISRUN;
	}
	public void setAP2_ISRUN(String aP2_ISRUN) {
		AP2_ISRUN = aP2_ISRUN;
	}
	public String getBIZDATE() {
		return BIZDATE;
	}
	public void setBIZDATE(String bIZDATE) {
		BIZDATE = bIZDATE;
	}
	public String getCLEARINGPHASE() {
		return CLEARINGPHASE;
	}
	public void setCLEARINGPHASE(String cLEARINGPHASE) {
		CLEARINGPHASE = cLEARINGPHASE;
	}
	public String getPREVDATE() {
		return PREVDATE;
	}
	public void setPREVDATE(String pREVDATE) {
		PREVDATE = pREVDATE;
	}
	public String getTHISDATE() {
		return THISDATE;
	}
	public void setTHISDATE(String tHISDATE) {
		THISDATE = tHISDATE;
	}
	public String getCUR_BIZDATE() {
		return CUR_BIZDATE;
	}
	public void setCUR_BIZDATE(String cUR_BIZDATE) {
		CUR_BIZDATE = cUR_BIZDATE;
	}
	public String getCUR_CLEARINGPHASE() {
		return CUR_CLEARINGPHASE;
	}
	public void setCUR_CLEARINGPHASE(String cUR_CLEARINGPHASE) {
		CUR_CLEARINGPHASE = cUR_CLEARINGPHASE;
	}
	public String getPRE_BIZDATE() {
		return PRE_BIZDATE;
	}
	public void setPRE_BIZDATE(String pRE_BIZDATE) {
		PRE_BIZDATE = pRE_BIZDATE;
	}
	public String getPRE_CLEARINGPHASE() {
		return PRE_CLEARINGPHASE;
	}
	public void setPRE_CLEARINGPHASE(String pRE_CLEARINGPHASE) {
		PRE_CLEARINGPHASE = pRE_CLEARINGPHASE;
	}
	public String getSYSDATE() {
		return SYSDATE;
	}
	public void setSYSDATE(String sYSDATE) {
		SYSDATE = sYSDATE;
	}
	
	
	
	
}
