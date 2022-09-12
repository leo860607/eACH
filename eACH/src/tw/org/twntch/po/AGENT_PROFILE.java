package tw.org.twntch.po;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
@Table(name="AGENT_PROFILE")
@Entity(name="tw.org.twntch.po.AGENT_PROFILE")
public class AGENT_PROFILE implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5228277355761735095L;
	@Id
	private String COMPANY_ID;
	private String COMPANY_ABBR_NAME;
	private String COMPANY_NAME;
	private String ACTIVE_DATE;
	private String STOP_DATE;
	private String WS_URL;
	private String WS_NAME_SPACE;
	private String KEY_ID;
	private String COMPANY_NO;
	private String KEY_FLAG;
	
	
	public String getCOMPANY_ID() {
		return COMPANY_ID;
	}
	public void setCOMPANY_ID(String cOMPANY_ID) {
		COMPANY_ID = cOMPANY_ID;
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
	public String getWS_URL() {
		return WS_URL;
	}
	public void setWS_URL(String wS_URL) {
		WS_URL = wS_URL;
	}
	public String getWS_NAME_SPACE() {
		return WS_NAME_SPACE;
	}
	public void setWS_NAME_SPACE(String wS_NAME_SPACE) {
		WS_NAME_SPACE = wS_NAME_SPACE;
	}
	public String getKEY_ID() {
		return KEY_ID;
	}
	public void setKEY_ID(String kEY_ID) {
		KEY_ID = kEY_ID;
	}
	public String getCOMPANY_NO() {
		return COMPANY_NO;
	}
	public void setCOMPANY_NO(String cOMPANY_NO) {
		COMPANY_NO = cOMPANY_NO;
	}
	public String getKEY_FLAG() {
		return KEY_FLAG;
	}
	public void setKEY_FLAG(String kEY_FLAG) {
		KEY_FLAG = kEY_FLAG;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((ACTIVE_DATE == null) ? 0 : ACTIVE_DATE.hashCode());
		result = prime
				* result
				+ ((COMPANY_ABBR_NAME == null) ? 0 : COMPANY_ABBR_NAME
						.hashCode());
		result = prime * result
				+ ((COMPANY_ID == null) ? 0 : COMPANY_ID.hashCode());
		result = prime * result
				+ ((COMPANY_NAME == null) ? 0 : COMPANY_NAME.hashCode());
		result = prime * result
				+ ((COMPANY_NO == null) ? 0 : COMPANY_NO.hashCode());
		result = prime * result + ((KEY_ID == null) ? 0 : KEY_ID.hashCode());
		result = prime * result
				+ ((STOP_DATE == null) ? 0 : STOP_DATE.hashCode());
		result = prime * result
				+ ((WS_NAME_SPACE == null) ? 0 : WS_NAME_SPACE.hashCode());
		result = prime * result + ((WS_URL == null) ? 0 : WS_URL.hashCode());
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
		AGENT_PROFILE other = (AGENT_PROFILE) obj;
		if (ACTIVE_DATE == null) {
			if (other.ACTIVE_DATE != null)
				return false;
		} else if (!ACTIVE_DATE.equals(other.ACTIVE_DATE))
			return false;
		if (COMPANY_ABBR_NAME == null) {
			if (other.COMPANY_ABBR_NAME != null)
				return false;
		} else if (!COMPANY_ABBR_NAME.equals(other.COMPANY_ABBR_NAME))
			return false;
		if (COMPANY_ID == null) {
			if (other.COMPANY_ID != null)
				return false;
		} else if (!COMPANY_ID.equals(other.COMPANY_ID))
			return false;
		if (COMPANY_NAME == null) {
			if (other.COMPANY_NAME != null)
				return false;
		} else if (!COMPANY_NAME.equals(other.COMPANY_NAME))
			return false;
		if (COMPANY_NO == null) {
			if (other.COMPANY_NO != null)
				return false;
		} else if (!COMPANY_NO.equals(other.COMPANY_NO))
			return false;
		if (KEY_ID == null) {
			if (other.KEY_ID != null)
				return false;
		} else if (!KEY_ID.equals(other.KEY_ID))
			return false;
		if (STOP_DATE == null) {
			if (other.STOP_DATE != null)
				return false;
		} else if (!STOP_DATE.equals(other.STOP_DATE))
			return false;
		if (WS_NAME_SPACE == null) {
			if (other.WS_NAME_SPACE != null)
				return false;
		} else if (!WS_NAME_SPACE.equals(other.WS_NAME_SPACE))
			return false;
		if (WS_URL == null) {
			if (other.WS_URL != null)
				return false;
		} else if (!WS_URL.equals(other.WS_URL))
			return false;
		return true;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AGENT_PROFILE [COMPANY_ID=");
		builder.append(COMPANY_ID);
		builder.append(", COMPANY_ABBR_NAME=");
		builder.append(COMPANY_ABBR_NAME);
		builder.append(", COMPANY_NAME=");
		builder.append(COMPANY_NAME);
		builder.append(", ACTIVE_DATE=");
		builder.append(ACTIVE_DATE);
		builder.append(", STOP_DATE=");
		builder.append(STOP_DATE);
		builder.append(", WS_URL=");
		builder.append(WS_URL);
		builder.append(", WS_NAME_SPACE=");
		builder.append(WS_NAME_SPACE);
		builder.append(", KEY_ID=");
		builder.append(KEY_ID);
		builder.append(", COMPANY_NO=");
		builder.append(COMPANY_NO);
		builder.append("]");
		return builder.toString();
	}
	
}
