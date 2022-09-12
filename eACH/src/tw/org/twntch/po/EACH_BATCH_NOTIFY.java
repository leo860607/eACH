package tw.org.twntch.po;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
@Entity(name="tw.org.twntch.po.EACH_BATCH_NOTIFY")
@Table(name="EACH_BATCH_NOTIFY")
public class EACH_BATCH_NOTIFY implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 465772381287896979L;
	private EACH_BATCH_NOTIFY_PK id ;
	private	String CLEAR_NOTIFY	;
	private	String RPT_NOTIFY	;
	
	
	@EmbeddedId
	public EACH_BATCH_NOTIFY_PK getId() {
		return id;
	}
	public void setId(EACH_BATCH_NOTIFY_PK id) {
		this.id = id;
	}
	public String getCLEAR_NOTIFY() {
		return CLEAR_NOTIFY;
	}
	public void setCLEAR_NOTIFY(String cLEAR_NOTIFY) {
		CLEAR_NOTIFY = cLEAR_NOTIFY;
	}
	public String getRPT_NOTIFY() {
		return RPT_NOTIFY;
	}
	public void setRPT_NOTIFY(String rPT_NOTIFY) {
		RPT_NOTIFY = rPT_NOTIFY;
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
		EACH_BATCH_NOTIFY other = (EACH_BATCH_NOTIFY) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	
	
}
