package tw.org.twntch.po;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;


@Entity(name="tw.org.twntch.po.NONCLEAR_BATCH_STATUS")
@Table(name="NONCLEAR_BATCH_STATUS")
public class NONCLEAR_BATCH_STATUS implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6269507578036627945L;
	private NONCLEAR_BATCH_STATUS_PK id ;
	private	String 	BATCH_PROC_DESC	;
	private	String 	BATCH_PROC_NAME	;
	private	String	BEGIN_TIME	;
	private	String	END_TIME	;
	private	String 	NOTE	;
	private	String  PROC_STATUS	;
	private	String  PROC_TYPE	;
	@EmbeddedId
	public NONCLEAR_BATCH_STATUS_PK getId() {
		return id;
	}
	public void setId(NONCLEAR_BATCH_STATUS_PK id) {
		this.id = id;
	}
	
	public String getBATCH_PROC_DESC() {
		return BATCH_PROC_DESC;
	}
	public void setBATCH_PROC_DESC(String bATCH_PROC_DESC) {
		BATCH_PROC_DESC = bATCH_PROC_DESC;
	}
	public String getBATCH_PROC_NAME() {
		return BATCH_PROC_NAME;
	}
	public void setBATCH_PROC_NAME(String bATCH_PROC_NAME) {
		BATCH_PROC_NAME = bATCH_PROC_NAME;
	}
	public String getBEGIN_TIME() {
		return BEGIN_TIME;
	}
	public void setBEGIN_TIME(String bEGIN_TIME) {
		BEGIN_TIME = bEGIN_TIME;
	}
	public String getEND_TIME() {
		return END_TIME;
	}
	public void setEND_TIME(String eND_TIME) {
		END_TIME = eND_TIME;
	}
	public String getNOTE() {
		return NOTE;
	}
	public void setNOTE(String nOTE) {
		NOTE = nOTE;
	}
	public String getPROC_STATUS() {
		return PROC_STATUS;
	}
	public void setPROC_STATUS(String pROC_STATUS) {
		PROC_STATUS = pROC_STATUS;
	}
	public String getPROC_TYPE() {
		return PROC_TYPE;
	}
	public void setPROC_TYPE(String pROC_TYPE) {
		PROC_TYPE = pROC_TYPE;
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
		NONCLEAR_BATCH_STATUS other = (NONCLEAR_BATCH_STATUS) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
	
}
