package tw.org.twntch.po;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity(name="tw.org.twntch.po.GL_ERROR_CODE")
@Table(name="GL_ERROR_CODE")
public class GL_ERROR_CODE implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5546706445104114787L;
	private String ERROR_ID ;
	private String ERR_DESC  ;
	private String CDATE ;
	private String UDATE ;
	@Id
	public String getERROR_ID() {
		return ERROR_ID;
	}
	public void setERROR_ID(String eRROR_ID) {
		ERROR_ID = eRROR_ID;
	}
	public String getERR_DESC() {
		return ERR_DESC;
	}
	public void setERR_DESC(String eRR_DESC) {
		ERR_DESC = eRR_DESC;
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
				+ ((ERROR_ID == null) ? 0 : ERROR_ID.hashCode());
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
		GL_ERROR_CODE other = (GL_ERROR_CODE) obj;
		if (ERROR_ID == null) {
			if (other.ERROR_ID != null)
				return false;
		} else if (!ERROR_ID.equals(other.ERROR_ID))
			return false;
		return true;
	}
	
	
}
