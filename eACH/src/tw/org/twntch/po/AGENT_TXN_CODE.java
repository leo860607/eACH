package tw.org.twntch.po;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity(name="tw.org.twntch.po.AGENT_TXN_CODE")
@Table(name="AGENT_TXN_CODE")
public class AGENT_TXN_CODE implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4399854260258508281L;
	@Id
	private	String	AGENT_TXN_ID	;
	private	String	AGENT_TXN_NAME	;
	private	String	BUSINESS_TYPE_ID	;
	private	String	CDATE	;
	private	String	UDATE	;
	
	@OrderBy("AGENT_TXN_ID ASC")
	public String getAGENT_TXN_ID() {
		return AGENT_TXN_ID;
	}
	public void setAGENT_TXN_ID(String aGENT_TXN_ID) {
		AGENT_TXN_ID = aGENT_TXN_ID;
	}
	public String getAGENT_TXN_NAME() {
		return AGENT_TXN_NAME;
	}
	public void setAGENT_TXN_NAME(String aGENT_TXN_NAME) {
		AGENT_TXN_NAME = aGENT_TXN_NAME;
	}
	public String getBUSINESS_TYPE_ID() {
		return BUSINESS_TYPE_ID;
	}
	public void setBUSINESS_TYPE_ID(String bUSINESS_TYPE_ID) {
		BUSINESS_TYPE_ID = bUSINESS_TYPE_ID;
	}
	public String getCDATE() {
		return CDATE;
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
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((AGENT_TXN_ID == null) ? 0 : AGENT_TXN_ID.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AGENT_TXN_CODE other = (AGENT_TXN_CODE) obj;
		if (AGENT_TXN_ID == null) {
			if (other.AGENT_TXN_ID != null)
				return false;
		} else if (!AGENT_TXN_ID.equals(other.AGENT_TXN_ID))
			return false;
		return true;
	}
	
	
	
	
	
	
	
}
