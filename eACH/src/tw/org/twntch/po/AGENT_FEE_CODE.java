package tw.org.twntch.po;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
@Table(name="AGENT_FEE_CODE")
@Entity(name="tw.org.twntch.po.AGENT_FEE_CODE")
public class AGENT_FEE_CODE implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4031453568356286188L;
	@EmbeddedId
	private AGENT_FEE_CODE_PK id;
	@Transient
	private String FEE_ID;
	@Transient
	private String COMPANY_ID;
	@Transient
	private String START_DATE;
	private BigDecimal FEE;
	private String FEE_DESC;
	private String ACTIVE_DESC;
	private String CDATE;
	private String UDATE;
	@Transient
	private String FEE_NAME;
	@Transient
	private String COMPANY_NAME;
	
	public AGENT_FEE_CODE_PK getId() {
		return id;
	}
	public void setId(AGENT_FEE_CODE_PK id) {
		this.id = id;
	}
	
	public String getFEE_ID() {
		return FEE_ID;
	}
	public void setFEE_ID(String fEE_ID) {
		FEE_ID = fEE_ID;
	}
	public String getCOMPANY_ID() {
		return COMPANY_ID;
	}
	public void setCOMPANY_ID(String cOMPANY_ID) {
		COMPANY_ID = cOMPANY_ID;
	}
	public String getSTART_DATE() {
		return START_DATE;
	}
	public void setSTART_DATE(String sTART_DATE) {
		START_DATE = sTART_DATE;
	}
	public BigDecimal getFEE() {
		return FEE;
	}
	public void setFEE(BigDecimal fEE) {
		FEE = fEE;
	}
	public String getFEE_DESC() {
		return FEE_DESC;
	}
	public void setFEE_DESC(String fEE_DESC) {
		FEE_DESC = fEE_DESC;
	}
	public String getACTIVE_DESC() {
		return ACTIVE_DESC;
	}
	public void setACTIVE_DESC(String aCTIVE_DESC) {
		ACTIVE_DESC = aCTIVE_DESC;
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
	public String getFEE_NAME() {
		return FEE_NAME;
	}
	public void setFEE_NAME(String fEE_NAME) {
		FEE_NAME = fEE_NAME;
	}
	public String getCOMPANY_NAME() {
		return COMPANY_NAME;
	}
	public void setCOMPANY_NAME(String cOMPANY_NAME) {
		COMPANY_NAME = cOMPANY_NAME;
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
		AGENT_FEE_CODE other = (AGENT_FEE_CODE) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
}
