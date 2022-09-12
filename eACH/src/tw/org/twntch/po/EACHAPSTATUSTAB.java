package tw.org.twntch.po;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
@Entity(name="tw.org.twntch.po.EACHAPSTATUSTAB")
@Table(name="EACHAPSTATUSTAB")
public class EACHAPSTATUSTAB implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	private String APID;
	private String APSTATUS;
	@Id
	public String getAPID() {
		return APID;
	}
	public void setAPID(String aPID) {
		APID = aPID;
	}
	public String getAPSTATUS() {
		return APSTATUS;
	}
	public void setAPSTATUS(String aPSTATUS) {
		APSTATUS = aPSTATUS;
	}
	
}
