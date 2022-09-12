package tw.org.twntch.po;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
@Entity(name="tw.org.twntch.po.PI_COMPANY_PROFILE")
@Table(name="PI_COMPANY_PROFILE")
public class PI_COMPANY_PROFILE implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7970845930307249344L;
	@EmbeddedId
	private PI_COMPANY_PROFILE_PK id;
	private String PI_COMPANY_ABBR_NAME;
	private String PI_COMPANY_NAME;
	// private BigDecimal HANDLECHARGE;
	private String START_DATE;
	private String STOP_DATE;
	private String TXN_ID;
	private String TYPE_AUZ;
	private String TYPE_CARD;
	private String TYPE_USER_ACCT;
	private String C_DATE;
	private String U_DATE;
	private String NOTE;

	@Transient
	private String PI_COMPANY_ID;
	@Transient
	private String BILL_TYPE_ID;
	@Transient
	private String BILL_TYPE_NAME;
	
	@Transient
	private String COMPANY_ID;
	@Transient
	private String COMPANY_ABBR_NAME;
	@Transient
	private String COMPANY_NAME;
	
	
	
	
	public PI_COMPANY_PROFILE_PK getId() {
		return id;
	}
	public void setId(PI_COMPANY_PROFILE_PK id) {
		this.id = id;
	}
	
	
	public String getPI_COMPANY_ABBR_NAME() {
		return PI_COMPANY_ABBR_NAME;
	}
	public void setPI_COMPANY_ABBR_NAME(String pI_COMPANY_ABBR_NAME) {
		PI_COMPANY_ABBR_NAME = pI_COMPANY_ABBR_NAME;
	}
	public String getPI_COMPANY_NAME() {
		return PI_COMPANY_NAME;
	}
	public void setPI_COMPANY_NAME(String pI_COMPANY_NAME) {
		PI_COMPANY_NAME = pI_COMPANY_NAME;
	}
	public String getPI_COMPANY_ID() {
		return PI_COMPANY_ID;
	}
	public void setPI_COMPANY_ID(String pI_COMPANY_ID) {
		PI_COMPANY_ID = pI_COMPANY_ID;
	}
	public String getCOMPANY_ABBR_NAME() {
		return COMPANY_ABBR_NAME;
	}
	public void setCOMPANY_ABBR_NAME(String cOMPANY_ABBR_NAME) {
		COMPANY_ABBR_NAME = cOMPANY_ABBR_NAME;
	}
	public String getCOMPANY_NAME() {
		return COMPANY_NAME;
	}
	public void setCOMPANY_NAME(String cOMPANY_NAME) {
		COMPANY_NAME = cOMPANY_NAME;
	}
	public String getSTART_DATE() {
		return START_DATE;
	}
	public void setSTART_DATE(String sTART_DATE) {
		START_DATE = sTART_DATE;
	}
	public String getSTOP_DATE() {
		return STOP_DATE;
	}
	public void setSTOP_DATE(String sTOP_DATE) {
		STOP_DATE = sTOP_DATE;
	}
	public String getTXN_ID() {
		return TXN_ID;
	}
	public void setTXN_ID(String tXN_ID) {
		TXN_ID = tXN_ID;
	}
	public String getTYPE_AUZ() {
		return TYPE_AUZ;
	}
	public void setTYPE_AUZ(String tYPE_AUZ) {
		TYPE_AUZ = tYPE_AUZ;
	}
	public String getTYPE_CARD() {
		return TYPE_CARD;
	}
	public void setTYPE_CARD(String tYPE_CARD) {
		TYPE_CARD = tYPE_CARD;
	}
	public String getTYPE_USER_ACCT() {
		return TYPE_USER_ACCT;
	}
	public void setTYPE_USER_ACCT(String tYPE_USER_ACCT) {
		TYPE_USER_ACCT = tYPE_USER_ACCT;
	}
	public String getC_DATE() {
		return C_DATE;
	}
	public void setC_DATE(String c_DATE) {
		C_DATE = c_DATE;
	}
	public String getU_DATE() {
		return U_DATE;
	}
	public void setU_DATE(String u_DATE) {
		U_DATE = u_DATE;
	}
	public String getNOTE() {
		return NOTE;
	}
	public void setNOTE(String nOTE) {
		NOTE = nOTE;
	}
	public String getCOMPANY_ID() {
		return COMPANY_ID;
	}
	public void setCOMPANY_ID(String cOMPANY_ID) {
		COMPANY_ID = cOMPANY_ID;
	}
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
		PI_COMPANY_PROFILE other = (PI_COMPANY_PROFILE) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	

	
	
}
