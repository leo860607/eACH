package tw.org.twntch.po;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name="tw.org.twntch.po.EACH_ROLE_LIST")
@Table(name="EACH_ROLE_LIST")
public class EACH_ROLE_LIST implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8244211103991680209L;
	private String ROLE_ID;
	private String ROLE_NAME;
	private String DESC;
	private String USE_IKEY;
	private String ROLE_TYPE;
	private String CDATE;
	private String UDATE;
	
	@Id
	public String getROLE_ID() {
		return ROLE_ID;
	}
	public void setROLE_ID(String rOLE_ID) {
		ROLE_ID = rOLE_ID;
	}
	public String getROLE_NAME() {
		return ROLE_NAME;
	}
	public void setROLE_NAME(String rOLE_NAME) {
		ROLE_NAME = rOLE_NAME;
	}
	public String getDESC() {
		return DESC;
	}
	public void setDESC(String dESC) {
		DESC = dESC;
	}
	public String getUSE_IKEY() {
		return USE_IKEY;
	}
	public void setUSE_IKEY(String uSE_IKEY) {
		USE_IKEY = uSE_IKEY;
	}
	public String getROLE_TYPE() {
		return ROLE_TYPE;
	}
	public void setROLE_TYPE(String rOLE_TYPE) {
		ROLE_TYPE = rOLE_TYPE;
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
		result = prime * result + ((ROLE_ID == null) ? 0 : ROLE_ID.hashCode());
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
		EACH_ROLE_LIST other = (EACH_ROLE_LIST) obj;
		if (ROLE_ID == null) {
			if (other.ROLE_ID != null)
				return false;
		} else if (!ROLE_ID.equals(other.ROLE_ID))
			return false;
		return true;
	}

	
	
}
