package tw.org.twntch.po;

import java.io.Serializable;
import java.math.BigInteger;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity(name= "tw.org.twntch.po.BILL_TYPE")
@Table(name="BILL_TYPE")
public class BILL_TYPE implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private	String 	BILL_TYPE_ID	;
	private	String 	BILL_TYPE_NAME	;
	
	@Id
	public String getBILL_TYPE_ID() {
		return BILL_TYPE_ID;
	}
	public void setBILL_TYPE_ID(String bILL_TYPE_ID) {
		BILL_TYPE_ID = bILL_TYPE_ID;
	}
	public String getBILL_TYPE_NAME() {
		return BILL_TYPE_NAME;
	}
	public void setBILL_TYPE_NAME(String bILL_TYPE_NAME) {
		BILL_TYPE_NAME = bILL_TYPE_NAME;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((BILL_TYPE_ID == null) ? 0 : BILL_TYPE_ID.hashCode());
		result = prime * result + ((BILL_TYPE_NAME == null) ? 0 : BILL_TYPE_NAME.hashCode());
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
		BILL_TYPE other = (BILL_TYPE) obj;
		if (BILL_TYPE_ID == null) {
			if (other.BILL_TYPE_ID != null)
				return false;
		} else if (!BILL_TYPE_ID.equals(other.BILL_TYPE_ID))
			return false;
		if (BILL_TYPE_NAME == null) {
			if (other.BILL_TYPE_NAME != null)
				return false;
		} else if (!BILL_TYPE_NAME.equals(other.BILL_TYPE_NAME))
			return false;
		return true;
	}

	

}
