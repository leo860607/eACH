package tw.org.twntch.po;

import java.io.Serializable;
import java.math.BigInteger;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity(name= "tw.org.twntch.po.AGENT_CR_LINE")
@Table(name="AGENT_CR_LINE")
public class AGENT_CR_LINE implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8243554178308095159L;
	private	String 	SND_COMPANY_ID	;
//	private	String 	USER_ID	;
	private	BigInteger BASIC_CR_LINE	;
	private	BigInteger REST_CR_LINE	;
	private	String  CDATE	;
	private	String  UDATE	;
	@Transient
	private String COMPANY_NAME ;
	@Transient
	private String TMP_COMPANY_NAME ;
	@Transient
	private String COMPANY_ID ;
	
	@Id
	@OrderBy("SND_COMPANY_ID ASC" )
	public String getSND_COMPANY_ID() {
		return SND_COMPANY_ID;
	}
	public void setSND_COMPANY_ID(String sND_COMPANY_ID) {
		SND_COMPANY_ID = sND_COMPANY_ID;
	}
//	public String getUSER_ID() {
//		return USER_ID;
//	}
//	public void setUSER_ID(String uSER_ID) {
//		USER_ID = uSER_ID;
//	}
	
	public BigInteger getBASIC_CR_LINE() {
		return BASIC_CR_LINE;
	}
	public void setBASIC_CR_LINE(BigInteger bASIC_CR_LINE) {
		BASIC_CR_LINE = bASIC_CR_LINE;
	}
	public BigInteger getREST_CR_LINE() {
		return REST_CR_LINE;
	}
	public void setREST_CR_LINE(BigInteger rEST_CR_LINE) {
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
	public String getCOMPANY_NAME() {
		return COMPANY_NAME;
	}
	public void setCOMPANY_NAME(String cOMPANY_NAME) {
		COMPANY_NAME = cOMPANY_NAME;
	}
	
	
	
	@Transient
	public String getTMP_COMPANY_NAME() {
		return TMP_COMPANY_NAME;
	}
	public void setTMP_COMPANY_NAME(String tMP_COMPANY_NAME) {
		TMP_COMPANY_NAME = tMP_COMPANY_NAME;
	}
	@Transient
	public String getCOMPANY_ID() {
		return COMPANY_ID;
	}
	public void setCOMPANY_ID(String cOMPANY_ID) {
		COMPANY_ID = cOMPANY_ID;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((SND_COMPANY_ID == null) ? 0 : SND_COMPANY_ID.hashCode());
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
		AGENT_CR_LINE other = (AGENT_CR_LINE) obj;
		if (SND_COMPANY_ID == null) {
			if (other.SND_COMPANY_ID != null)
				return false;
		} else if (!SND_COMPANY_ID.equals(other.SND_COMPANY_ID))
			return false;
		return true;
	}
	
	

}
