package tw.org.twntch.po;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity(name="tw.org.twntch.po.BULLETIN_REC")
@Table(name="BULLETIN_REC")
public class BULLETIN_REC implements Serializable{

	private static final long serialVersionUID = 6792875633747472641L;
	/**
	 * 
	 */
	private	Integer 	SNO	;
	private	String 	    SEND_DATE	;
	private	String 	    CHCON	;
	private	String 	    USERIP	;
	private	String 	    USERID	;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getSNO() {
		return SNO;
	}
	public void setSNO(Integer sNO) {
		SNO = sNO;
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
	
	
	
	
	public String getUSERIP() {
		return USERIP;
	}
	public void setUSERIP(String uSERIP) {
		USERIP = uSERIP;
	}
	public String getUSERID() {
		return USERID;
	}
	public void setUSERID(String uSERID) {
		USERID = uSERID;
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
		BULLETIN_REC other = (BULLETIN_REC) obj;
		if (SNO == null) {
			if (other.SNO != null)
				return false;
		} else if (!SNO.equals(other.SNO))
			return false;
		return true;
	}
	
	
	
	
	
	
}
