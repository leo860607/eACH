package tw.org.twntch.po;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity (name = "tw.org.twntch.po.BANK_BRANCH")
@Table(name = "BANK_BRANCH")
public class BANK_BRANCH implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4585597083104668065L;
	private BANK_BRANCH_PK id ;
	private	String	BRBK_NAME	;
	private	String	TCH_ID	;
	private	String	ACTIVE_DATE	;
	private	String	STOP_DATE	;
	private	String	SYNCSPDATE	;//分行的停用日期是否與總行的停用日期同步
	private	String	CDATE	;
	private	String	UDATE	;
	@EmbeddedId
	public BANK_BRANCH_PK getId() {
		return id;
	}
	public void setId(BANK_BRANCH_PK id) {
		this.id = id;
	}
	public String getBRBK_NAME() {
		return BRBK_NAME;
	}
	public void setBRBK_NAME(String bRBK_NAME) {
		BRBK_NAME = bRBK_NAME;
	}
	public String getTCH_ID() {
		return TCH_ID;
	}
	public void setTCH_ID(String tCH_ID) {
		TCH_ID = tCH_ID;
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
	
	
	public String getSYNCSPDATE() {
		return SYNCSPDATE;
	}
	public void setSYNCSPDATE(String sYNCSPDATE) {
		SYNCSPDATE = sYNCSPDATE;
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
		BANK_BRANCH other = (BANK_BRANCH) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
}
