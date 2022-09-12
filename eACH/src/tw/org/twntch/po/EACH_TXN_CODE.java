package tw.org.twntch.po;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name= "tw.org.twntch.po.EACH_TXN_CODE")
@Table(name="EACH_TXN_CODE")
public class EACH_TXN_CODE implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4951358394339688787L;
	private String EACH_TXN_ID;
	private String EACH_TXN_NAME;
	private String BUSINESS_TYPE_ID;
	private String CDATE;
	private String UDATE;
	@Id
	public String getEACH_TXN_ID() {
		return EACH_TXN_ID;
	}
	public void setEACH_TXN_ID(String eACH_TXN_ID) {
		EACH_TXN_ID = eACH_TXN_ID;
	}
	public String getEACH_TXN_NAME() {
		return EACH_TXN_NAME;
	}
	public void setEACH_TXN_NAME(String eACH_TXN_NAME) {
		EACH_TXN_NAME = eACH_TXN_NAME;
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
		result = prime
				* result
				+ ((BUSINESS_TYPE_ID == null) ? 0 : BUSINESS_TYPE_ID.hashCode());
		result = prime * result + ((CDATE == null) ? 0 : CDATE.hashCode());
		result = prime * result
				+ ((EACH_TXN_ID == null) ? 0 : EACH_TXN_ID.hashCode());
		result = prime * result
				+ ((EACH_TXN_NAME == null) ? 0 : EACH_TXN_NAME.hashCode());
		result = prime * result + ((UDATE == null) ? 0 : UDATE.hashCode());
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
		EACH_TXN_CODE other = (EACH_TXN_CODE) obj;
		if (BUSINESS_TYPE_ID == null) {
			if (other.BUSINESS_TYPE_ID != null)
				return false;
		} else if (!BUSINESS_TYPE_ID.equals(other.BUSINESS_TYPE_ID))
			return false;
		if (CDATE == null) {
			if (other.CDATE != null)
				return false;
		} else if (!CDATE.equals(other.CDATE))
			return false;
		if (EACH_TXN_ID == null) {
			if (other.EACH_TXN_ID != null)
				return false;
		} else if (!EACH_TXN_ID.equals(other.EACH_TXN_ID))
			return false;
		if (EACH_TXN_NAME == null) {
			if (other.EACH_TXN_NAME != null)
				return false;
		} else if (!EACH_TXN_NAME.equals(other.EACH_TXN_NAME))
			return false;
		if (UDATE == null) {
			if (other.UDATE != null)
				return false;
		} else if (!UDATE.equals(other.UDATE))
			return false;
		return true;
	}
}
