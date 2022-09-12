package tw.org.twntch.po;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OrderBy;
import javax.persistence.Table;

@Entity(name = "tw.org.twntch.po.EACH_BATCH_DEF")
@Table(name = "EACH_BATCH_DEF")
public class EACH_BATCH_DEF implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5476363767065323727L;
	@Id
	@OrderBy("BATCH_PROC_SEQ ASC" )
	private	Integer	BATCH_PROC_SEQ	;
	private	String 	BATCH_PROC_DESC	;
	private	String 	BATCH_PROC_NAME	;
	private	String    	ERR_BREAK	;
	private	String    	PROC_TYPE	;
	
	public Integer getBATCH_PROC_SEQ() {
		return BATCH_PROC_SEQ;
	}
	public void setBATCH_PROC_SEQ(Integer bATCH_PROC_SEQ) {
		BATCH_PROC_SEQ = bATCH_PROC_SEQ;
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
	public String getERR_BREAK() {
		return ERR_BREAK;
	}
	public void setERR_BREAK(String eRR_BREAK) {
		ERR_BREAK = eRR_BREAK;
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
		result = prime * result
				+ ((BATCH_PROC_SEQ == null) ? 0 : BATCH_PROC_SEQ.hashCode());
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
		EACH_BATCH_DEF other = (EACH_BATCH_DEF) obj;
		if (BATCH_PROC_SEQ == null) {
			if (other.BATCH_PROC_SEQ != null)
				return false;
		} else if (!BATCH_PROC_SEQ.equals(other.BATCH_PROC_SEQ))
			return false;
		return true;
	}
	
	
	

}
