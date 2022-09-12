package tw.org.twntch.po;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity (name = "tw.org.twntch.po.MISSINGTRADERLOGTAB")
@Table(name = "MISSINGTRADERLOGTAB")
public class MISSINGTRADERLOGTAB implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -283940874726808424L;
	private MISSINGTRADERLOGTAB_PK id;
	private String TXTIME;
	private String PCODE;
	private String SENDERBANK;
	private String RECEIVERBANK;
	private String BASICDATA;
	//@Transient
	private	String PK_STAN;
	private	String PK_TXDATE;
	private String PNAME;
	
	@EmbeddedId
	public MISSINGTRADERLOGTAB_PK getId() {
		return id;
	}
	public void setId(MISSINGTRADERLOGTAB_PK id) {
		this.id = id;
	}
	public String getTXTIME() {
		return TXTIME;
	}
	public void setTXTIME(String tXTIME) {
		TXTIME = tXTIME;
	}
	public String getPCODE() {
		return PCODE;
	}
	public void setPCODE(String pCODE) {
		PCODE = pCODE;
	}
	public String getSENDERBANK() {
		return SENDERBANK;
	}
	public void setSENDERBANK(String sENDERBANK) {
		SENDERBANK = sENDERBANK;
	}
	public String getRECEIVERBANK() {
		return RECEIVERBANK;
	}
	public void setRECEIVERBANK(String rECEIVERBANK) {
		RECEIVERBANK = rECEIVERBANK;
	}
	public String getBASICDATA() {
		return BASICDATA;
	}
	public void setBASICDATA(String bASICDATA) {
		BASICDATA = bASICDATA;
	}
	@Transient
	public String getPK_STAN() {
		return PK_STAN;
	}
	public void setPK_STAN(String pK_STAN) {
		PK_STAN = pK_STAN;
	}
	@Transient
	public String getPK_TXDATE() {
		return PK_TXDATE;
	}
	public void setPK_TXDATE(String pK_TXDATE) {
		PK_TXDATE = pK_TXDATE;
	}
	public String getPNAME() {
		return PNAME;
	}
	public void setPNAME(String pNAME) {
		PNAME = pNAME;
	}
	
}
