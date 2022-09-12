package tw.org.twntch.po;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Table(name="AGENT_SEND_PROFILE")
@Entity(name="tw.org.twntch.po.AGENT_SEND_PROFILE")
public class AGENT_SEND_PROFILE  {

	@EmbeddedId
	private AGENT_SEND_PROFILE_PK id ;
	private String ACTIVE_DATE  ;    	
	private String STOP_DATE ;	
	private String CDATE ;
	private String UDATE ;
	@Transient
	private String COMPANY_NAME ;
	@Transient
	private String SND_COMPANY_NAME ;
	@Transient
	private String TXN_ID ;
	@Transient
	private String COMPANY_ID ;
	@Transient
	private String SND_COMPANY_ID ;
	
	
	
	public AGENT_SEND_PROFILE_PK getId() {
		return id;
	}
	public void setId(AGENT_SEND_PROFILE_PK id) {
		this.id = id;
	}
	public String getACTIVE_DATE() {
		return ACTIVE_DATE;
	}
	public void setACTIVE_DATE(String aCTIVE_DATE) {
		ACTIVE_DATE = aCTIVE_DATE;
	}
	public String getSTOP_DATE() {
		return STOP_DATE;
	}
	public void setSTOP_DATE(String sTOP_DATE) {
		STOP_DATE = sTOP_DATE;
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
	
	
	public String getCOMPANY_NAME() {
		return COMPANY_NAME;
	}
	public void setCOMPANY_NAME(String cOMPANY_NAME) {
		COMPANY_NAME = cOMPANY_NAME;
	}
	public String getSND_COMPANY_NAME() {
		return SND_COMPANY_NAME;
	}
	public void setSND_COMPANY_NAME(String sND_COMPANY_NAME) {
		SND_COMPANY_NAME = sND_COMPANY_NAME;
	}
	
	
	public String getTXN_ID() {
		return TXN_ID;
	}
	public void setTXN_ID(String tXN_ID) {
		TXN_ID = tXN_ID;
	}
	public String getCOMPANY_ID() {
		return COMPANY_ID;
	}
	public void setCOMPANY_ID(String cOMPANY_ID) {
		COMPANY_ID = cOMPANY_ID;
	}
	public String getSND_COMPANY_ID() {
		return SND_COMPANY_ID;
	}
	public void setSND_COMPANY_ID(String sND_COMPANY_ID) {
		SND_COMPANY_ID = sND_COMPANY_ID;
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
		AGENT_SEND_PROFILE other = (AGENT_SEND_PROFILE) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
	
}
