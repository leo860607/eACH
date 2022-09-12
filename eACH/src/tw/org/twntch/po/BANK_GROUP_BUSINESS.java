package tw.org.twntch.po;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
@Entity(name = "tw.org.twntch.po.BANK_GROUP_BUSINESS")
@Table(name = "BANK_GROUP_BUSINESS")
public class BANK_GROUP_BUSINESS implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3032369843542344140L;
	
	private BANK_GROUP_BUSINESS_PK id ;
	private	String	CDATE	;
	private	String	UDATE	;
	@EmbeddedId
	public BANK_GROUP_BUSINESS_PK getId() {
		return id;
	}
	public void setId(BANK_GROUP_BUSINESS_PK id) {
		this.id = id;
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
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		BANK_GROUP_BUSINESS other = (BANK_GROUP_BUSINESS) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
}
