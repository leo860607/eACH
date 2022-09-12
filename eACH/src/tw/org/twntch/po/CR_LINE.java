package tw.org.twntch.po;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity(name= "tw.org.twntch.po.CR_LINE")
@Table(name="CR_LINE")
public class CR_LINE implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7042121575474526593L;
	private	String 	BANK_ID	;
//	private	String 	USER_ID	;
	private	String BASIC_CR_LINE	;
	private	String REST_CR_LINE	;
	private	String  CDATE	;
	private	String  UDATE	;
	
	@Transient
	private String BANK_NAME ;
	
	@Id
	@OrderBy("BANK_ID ASC" )
	public String getBANK_ID() {
		return BANK_ID;
	}
	public void setBANK_ID(String bANK_ID) {
		BANK_ID = bANK_ID;
	}
//	public String getUSER_ID() {
//		return USER_ID;
//	}
//	public void setUSER_ID(String uSER_ID) {
//		USER_ID = uSER_ID;
//	}
	public String getBASIC_CR_LINE() {
		return BASIC_CR_LINE;
	}
	public void setBASIC_CR_LINE(String bASIC_CR_LINE) {
		BASIC_CR_LINE = bASIC_CR_LINE;
	}
	public String getREST_CR_LINE() {
		return REST_CR_LINE;
	}
	public void setREST_CR_LINE(String rEST_CR_LINE) {
		REST_CR_LINE = rEST_CR_LINE;
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
	
	
	@Transient
	public String getBANK_NAME() {
		return BANK_NAME;
	}
	public void setBANK_NAME(String bANK_NAME) {
		BANK_NAME = bANK_NAME;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((BANK_ID == null) ? 0 : BANK_ID.hashCode());
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
		CR_LINE other = (CR_LINE) obj;
		if (BANK_ID == null) {
			if (other.BANK_ID != null)
				return false;
		} else if (!BANK_ID.equals(other.BANK_ID))
			return false;
		return true;
	}

	

}
