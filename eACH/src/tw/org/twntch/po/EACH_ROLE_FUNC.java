package tw.org.twntch.po;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
@Entity(name="tw.org.twntch.po.EACH_ROLE_FUNC")
@Table(name="EACH_ROLE_FUNC")
public class EACH_ROLE_FUNC implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4872180336200155184L;
	private EACH_ROLE_FUNC_PK id ;
	private String AUTH_TYPE = "N";
	private String CDATE;
	private String UDATE;
	
	
	@EmbeddedId
	public EACH_ROLE_FUNC_PK getId() {
		return id;
	}
	public void setId(EACH_ROLE_FUNC_PK id) {
		this.id = id;
	}
	
	public String getAUTH_TYPE() {
		return AUTH_TYPE;
	}
	public void setAUTH_TYPE(String aUTH_TYPE) {
		AUTH_TYPE = aUTH_TYPE;
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
		EACH_ROLE_FUNC other = (EACH_ROLE_FUNC) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	
	
}
