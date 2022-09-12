package tw.org.twntch.po;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity(name= "tw.org.twntch.po.EACH_FUNC_LIST")
@Table(name="EACH_FUNC_LIST")
public class EACH_FUNC_LIST implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8809598173033682601L;
	@Transient
	private String htmlId ;
	@Transient
	private String UP_FUNC_NAME ;
	@Transient
	private String AUTH_TYPE ;
	
	private String FUNC_ID;
	private String FUNC_NAME;
	private String FUNC_TYPE;
	private String FUNC_SEQ;
	private String UP_FUNC_ID;
	private String FUNC_DESC;
	private String FUNC_URL;
	private String USE_IKEY;
	private String TCH_FUNC;
	private String BANK_FUNC;
	private String COMPANY_FUNC;
	private String IS_USED = "N";
	private String START_DATE;
	private String CDATE;
	private String UDATE;
	private String PROXY_FUNC;
	private String IS_RECORD;
	private String FUNC_NAME_BK;
	
	@Transient
	public String getHtmlId() {
		return htmlId;
	}
	public void setHtmlId(String htmlId) {
		this.htmlId = htmlId;
	}
	@Transient
	public String getUP_FUNC_NAME() {
		return UP_FUNC_NAME;
	}
	public void setUP_FUNC_NAME(String uP_FUNC_NAME) {
		UP_FUNC_NAME = uP_FUNC_NAME;
	}
	@Transient
	public String getAUTH_TYPE() {
		return AUTH_TYPE;
	}
	public void setAUTH_TYPE(String aUTHTYPE) {
		AUTH_TYPE = aUTHTYPE;
	}
	@Id
	@OrderBy("FUNC_ID ASC")
	public String getFUNC_ID() {
		return FUNC_ID;
	}
	public void setFUNC_ID(String fUNC_ID) {
		FUNC_ID = fUNC_ID;
	}
	public String getFUNC_NAME() {
		return FUNC_NAME;
	}
	public void setFUNC_NAME(String fUNC_NAME) {
		FUNC_NAME = fUNC_NAME;
	}
	public String getFUNC_TYPE() {
		return FUNC_TYPE;
	}
	public void setFUNC_TYPE(String fUNC_TYPE) {
		FUNC_TYPE = fUNC_TYPE;
	}
	public String getFUNC_SEQ() {
		return FUNC_SEQ;
	}
	public void setFUNC_SEQ(String fUNC_SEQ) {
		FUNC_SEQ = fUNC_SEQ;
	}
	public String getUP_FUNC_ID() {
		return UP_FUNC_ID;
	}
	public void setUP_FUNC_ID(String uP_FUNC_ID) {
		UP_FUNC_ID = uP_FUNC_ID;
	}
	public String getFUNC_DESC() {
		return FUNC_DESC;
	}
	public void setFUNC_DESC(String fUNC_DESC) {
		FUNC_DESC = fUNC_DESC;
	}
	public String getFUNC_URL() {
		return FUNC_URL;
	}
	public void setFUNC_URL(String fUNC_URL) {
		FUNC_URL = fUNC_URL;
	}
	public String getUSE_IKEY() {
		return USE_IKEY;
	}
	public void setUSE_IKEY(String uSE_IKEY) {
		USE_IKEY = uSE_IKEY;
	}
	public String getTCH_FUNC() {
		return TCH_FUNC;
	}
	public void setTCH_FUNC(String tCH_FUNC) {
		TCH_FUNC = tCH_FUNC;
	}
	public String getBANK_FUNC() {
		return BANK_FUNC;
	}
	public void setBANK_FUNC(String bANK_FUNC) {
		BANK_FUNC = bANK_FUNC;
	}
	public String getCOMPANY_FUNC() {
		return COMPANY_FUNC;
	}
	public void setCOMPANY_FUNC(String cOMPANY_FUNC) {
		COMPANY_FUNC = cOMPANY_FUNC;
	}
	public String getIS_USED() {
		return IS_USED;
	}
	public void setIS_USED(String iS_USED) {
		IS_USED = iS_USED;
	}
	public String getSTART_DATE() {
		return START_DATE;
	}
	public void setSTART_DATE(String sTART_DATE) {
		START_DATE = sTART_DATE;
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
	public String getPROXY_FUNC() {
		return PROXY_FUNC;
	}
	public void setPROXY_FUNC(String pROXY_FUNC) {
		PROXY_FUNC = pROXY_FUNC;
	}
	
	
	
	public String getIS_RECORD() {
		return IS_RECORD;
	}
	public void setIS_RECORD(String iS_RECORD) {
		IS_RECORD = iS_RECORD;
	}
	public String getFUNC_NAME_BK() {
		return FUNC_NAME_BK;
	}
	public void setFUNC_NAME_BK(String fUNC_NAME_BK) {
		FUNC_NAME_BK = fUNC_NAME_BK;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((AUTH_TYPE == null) ? 0 : AUTH_TYPE.hashCode());
		result = prime * result
				+ ((BANK_FUNC == null) ? 0 : BANK_FUNC.hashCode());
		result = prime * result + ((CDATE == null) ? 0 : CDATE.hashCode());
		result = prime * result
				+ ((COMPANY_FUNC == null) ? 0 : COMPANY_FUNC.hashCode());
		result = prime * result
				+ ((FUNC_DESC == null) ? 0 : FUNC_DESC.hashCode());
		result = prime * result + ((FUNC_ID == null) ? 0 : FUNC_ID.hashCode());
		result = prime * result
				+ ((FUNC_NAME == null) ? 0 : FUNC_NAME.hashCode());
		result = prime * result
				+ ((FUNC_SEQ == null) ? 0 : FUNC_SEQ.hashCode());
		result = prime * result
				+ ((FUNC_TYPE == null) ? 0 : FUNC_TYPE.hashCode());
		result = prime * result
				+ ((FUNC_URL == null) ? 0 : FUNC_URL.hashCode());
		result = prime * result + ((IS_USED == null) ? 0 : IS_USED.hashCode());
		result = prime * result
				+ ((PROXY_FUNC == null) ? 0 : PROXY_FUNC.hashCode());
		result = prime * result
				+ ((START_DATE == null) ? 0 : START_DATE.hashCode());
		result = prime * result
				+ ((TCH_FUNC == null) ? 0 : TCH_FUNC.hashCode());
		result = prime * result + ((UDATE == null) ? 0 : UDATE.hashCode());
		result = prime * result
				+ ((UP_FUNC_ID == null) ? 0 : UP_FUNC_ID.hashCode());
		result = prime * result
				+ ((UP_FUNC_NAME == null) ? 0 : UP_FUNC_NAME.hashCode());
		result = prime * result
				+ ((USE_IKEY == null) ? 0 : USE_IKEY.hashCode());
		result = prime * result + ((htmlId == null) ? 0 : htmlId.hashCode());
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
		EACH_FUNC_LIST other = (EACH_FUNC_LIST) obj;
		if (AUTH_TYPE == null) {
			if (other.AUTH_TYPE != null)
				return false;
		} else if (!AUTH_TYPE.equals(other.AUTH_TYPE))
			return false;
		if (BANK_FUNC == null) {
			if (other.BANK_FUNC != null)
				return false;
		} else if (!BANK_FUNC.equals(other.BANK_FUNC))
			return false;
		if (CDATE == null) {
			if (other.CDATE != null)
				return false;
		} else if (!CDATE.equals(other.CDATE))
			return false;
		if (COMPANY_FUNC == null) {
			if (other.COMPANY_FUNC != null)
				return false;
		} else if (!COMPANY_FUNC.equals(other.COMPANY_FUNC))
			return false;
		if (FUNC_DESC == null) {
			if (other.FUNC_DESC != null)
				return false;
		} else if (!FUNC_DESC.equals(other.FUNC_DESC))
			return false;
		if (FUNC_ID == null) {
			if (other.FUNC_ID != null)
				return false;
		} else if (!FUNC_ID.equals(other.FUNC_ID))
			return false;
		if (FUNC_NAME == null) {
			if (other.FUNC_NAME != null)
				return false;
		} else if (!FUNC_NAME.equals(other.FUNC_NAME))
			return false;
		if (FUNC_SEQ == null) {
			if (other.FUNC_SEQ != null)
				return false;
		} else if (!FUNC_SEQ.equals(other.FUNC_SEQ))
			return false;
		if (FUNC_TYPE == null) {
			if (other.FUNC_TYPE != null)
				return false;
		} else if (!FUNC_TYPE.equals(other.FUNC_TYPE))
			return false;
		if (FUNC_URL == null) {
			if (other.FUNC_URL != null)
				return false;
		} else if (!FUNC_URL.equals(other.FUNC_URL))
			return false;
		if (IS_USED == null) {
			if (other.IS_USED != null)
				return false;
		} else if (!IS_USED.equals(other.IS_USED))
			return false;
		if (PROXY_FUNC == null) {
			if (other.PROXY_FUNC != null)
				return false;
		} else if (!PROXY_FUNC.equals(other.PROXY_FUNC))
			return false;
		if (START_DATE == null) {
			if (other.START_DATE != null)
				return false;
		} else if (!START_DATE.equals(other.START_DATE))
			return false;
		if (TCH_FUNC == null) {
			if (other.TCH_FUNC != null)
				return false;
		} else if (!TCH_FUNC.equals(other.TCH_FUNC))
			return false;
		if (UDATE == null) {
			if (other.UDATE != null)
				return false;
		} else if (!UDATE.equals(other.UDATE))
			return false;
		if (UP_FUNC_ID == null) {
			if (other.UP_FUNC_ID != null)
				return false;
		} else if (!UP_FUNC_ID.equals(other.UP_FUNC_ID))
			return false;
		if (UP_FUNC_NAME == null) {
			if (other.UP_FUNC_NAME != null)
				return false;
		} else if (!UP_FUNC_NAME.equals(other.UP_FUNC_NAME))
			return false;
		if (USE_IKEY == null) {
			if (other.USE_IKEY != null)
				return false;
		} else if (!USE_IKEY.equals(other.USE_IKEY))
			return false;
		if (htmlId == null) {
			if (other.htmlId != null)
				return false;
		} else if (!htmlId.equals(other.htmlId))
			return false;
		return true;
	}
	
}
