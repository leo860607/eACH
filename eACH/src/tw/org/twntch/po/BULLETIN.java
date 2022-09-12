package tw.org.twntch.po;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity(name="tw.org.twntch.po.BULLETIN")
@Table(name="BULLETIN")
public class BULLETIN implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6042433155940262748L;
	private	Integer 	SNO	;
	private	String  	SEND_STATUS	;
	private	String  	SAVE_DATE	;
	private	String 	    SEND_DATE	;
	private	String 	    CHCON	;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getSNO() {
		return SNO;
	}
	public void setSNO(Integer sNO) {
		SNO = sNO;
	}
	public String getSEND_STATUS() {
		return SEND_STATUS;
	}
	public void setSEND_STATUS(String sEND_STATUS) {
		SEND_STATUS = sEND_STATUS;
	}
	public String getSAVE_DATE() {
		return SAVE_DATE;
	}
	public void setSAVE_DATE(String sAVE_DATE) {
		SAVE_DATE = sAVE_DATE;
	}
	public String getSEND_DATE() {
		return SEND_DATE;
	}
	public void setSEND_DATE(String sEND_DATE) {
		SEND_DATE = sEND_DATE;
	}
	public String getCHCON() {
		return CHCON;
	}
	public void setCHCON(String cHCON) {
		CHCON = cHCON;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((SNO == null) ? 0 : SNO.hashCode());
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
		BULLETIN other = (BULLETIN) obj;
		if (SNO == null) {
			if (other.SNO != null)
				return false;
		} else if (!SNO.equals(other.SNO))
			return false;
		return true;
	}
	
	
	
	
	
	
}
