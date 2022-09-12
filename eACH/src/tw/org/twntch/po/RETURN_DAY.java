package tw.org.twntch.po;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity(name="tw.org.twntch.po.RETURN_DAY")
@Table(name="RETURN_DAY")
public class RETURN_DAY implements Serializable{
	private	RETURN_DAY_PK	id	;
	private	Integer	RETURN_DAY	;
	private	String	CDATE	;
	private	String	UDATE	;
	private String 	TXN_NAME	;
	private String 	IS_ACTIVE	;
	@Transient
	public String getTXN_NAME() {
		return TXN_NAME;
	}
	public void setTXN_NAME(String tXN_NAME) {
		TXN_NAME = tXN_NAME;
	}
	@Transient
	public String getIS_ACTIVE() {
		return IS_ACTIVE;
	}
	public void setIS_ACTIVE(String iS_ACTIVE) {
		IS_ACTIVE = iS_ACTIVE;
	}
	@EmbeddedId
	public RETURN_DAY_PK getId() {
		return id;
	}
	public void setId(RETURN_DAY_PK id) {
		this.id = id;
	}
	public Integer getRETURN_DAY() {
		return RETURN_DAY;
	}
	public void setRETURN_DAY(Integer rETURN_DAY) {
		RETURN_DAY = rETURN_DAY;
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
		result = prime * result + ((CDATE == null) ? 0 : CDATE.hashCode());
		result = prime * result
				+ ((IS_ACTIVE == null) ? 0 : IS_ACTIVE.hashCode());
		result = prime * result
				+ ((RETURN_DAY == null) ? 0 : RETURN_DAY.hashCode());
		result = prime * result
				+ ((TXN_NAME == null) ? 0 : TXN_NAME.hashCode());
		result = prime * result + ((UDATE == null) ? 0 : UDATE.hashCode());
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
		RETURN_DAY other = (RETURN_DAY) obj;
		if (CDATE == null) {
			if (other.CDATE != null)
				return false;
		} else if (!CDATE.equals(other.CDATE))
			return false;
		if (IS_ACTIVE == null) {
			if (other.IS_ACTIVE != null)
				return false;
		} else if (!IS_ACTIVE.equals(other.IS_ACTIVE))
			return false;
		if (RETURN_DAY == null) {
			if (other.RETURN_DAY != null)
				return false;
		} else if (!RETURN_DAY.equals(other.RETURN_DAY))
			return false;
		if (TXN_NAME == null) {
			if (other.TXN_NAME != null)
				return false;
		} else if (!TXN_NAME.equals(other.TXN_NAME))
			return false;
		if (UDATE == null) {
			if (other.UDATE != null)
				return false;
		} else if (!UDATE.equals(other.UDATE))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
