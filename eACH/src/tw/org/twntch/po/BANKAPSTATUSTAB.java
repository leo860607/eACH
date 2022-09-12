package tw.org.twntch.po;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity(name = "tw.org.twntch.po.BANKAPSTATUSTAB")
@Table(name = "BANKAPSTATUSTAB")
public class BANKAPSTATUSTAB implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7521516122770873553L;
	private BANKAPSTATUSTAB_PK id;
	private String MBAPSTATUS;
	@EmbeddedId
	public BANKAPSTATUSTAB_PK getId() {
		return id;
	}
	public void setId(BANKAPSTATUSTAB_PK id) {
		this.id = id;
	}
	public String getMBAPSTATUS() {
		return MBAPSTATUS;
	}
	public void setMBAPSTATUS(String mBAPSTATUS) {
		MBAPSTATUS = mBAPSTATUS;
	}
}
