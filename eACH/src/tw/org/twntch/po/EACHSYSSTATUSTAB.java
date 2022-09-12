package tw.org.twntch.po;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
@Entity(name="tw.org.twntch.po.EACHSYSSTATUSTAB")
@Table(name="EACHSYSSTATUSTAB")
public class EACHSYSSTATUSTAB implements Serializable {

	//eACH系統狀態檔
	private static final long serialVersionUID = -1529163750111026803L;

	@Id
	private	String 	SYSSTATUS;
	private	String 	PREVDATE;
	private String  THISDATE;
	private String  NEXTDATE;
	private String  DATEMODE;
	private String  CLEARINGPHASE;
	
	@Transient
	private String BUSINESS_DATE;
		
	public String getSYSSTATUS() {
		return SYSSTATUS;
	}
	public void setSYSSTATUS(String sYSSTATUS) {
		SYSSTATUS = sYSSTATUS;
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
	public String getNEXTDATE() {
		return NEXTDATE;
	}
	public void setNEXTDATE(String nEXTDATE) {
		NEXTDATE = nEXTDATE;
	}
	public String getDATEMODE() {
		return DATEMODE;
	}
	public void setDATEMODE(String dATEMODE) {
		DATEMODE = dATEMODE;
	}
	public String getCLEARINGPHASE() {
		return CLEARINGPHASE;
	}
	public void setCLEARINGPHASE(String cLEARINGPHASE) {
		CLEARINGPHASE = cLEARINGPHASE;
	}
	
	@Transient
	public String getBUSINESS_DATE() {
		return BUSINESS_DATE;
	}
	public void setBUSINESS_DATE(String bUSINESS_DATE) {
		BUSINESS_DATE = bUSINESS_DATE;
	}
	
}
