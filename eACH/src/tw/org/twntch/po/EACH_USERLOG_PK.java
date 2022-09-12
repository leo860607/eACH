package tw.org.twntch.po;

import java.io.Serializable;

public class EACH_USERLOG_PK implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 504340270667525638L;
	private String SERNO;
	private String USER_COMPANY;
	private String USERID;
	
	
	public EACH_USERLOG_PK() {
	}
	public EACH_USERLOG_PK(String sERNO, String uSER_COMPANY, String uSERID) {
		SERNO = sERNO;
		USER_COMPANY = uSER_COMPANY;
		USERID = uSERID;
	}
	public String getSERNO() {
		return SERNO;
	}
	public void setSERNO(String sERNO) {
		SERNO = sERNO;
	}
	public String getUSER_COMPANY() {
		return USER_COMPANY;
	}
	public void setUSER_COMPANY(String uSER_COMPANY) {
		USER_COMPANY = uSER_COMPANY;
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
		result = prime * result + ((SERNO == null) ? 0 : SERNO.hashCode());
		result = prime * result + ((USERID == null) ? 0 : USERID.hashCode());
		result = prime * result
				+ ((USER_COMPANY == null) ? 0 : USER_COMPANY.hashCode());
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
		EACH_USERLOG_PK other = (EACH_USERLOG_PK) obj;
		if (SERNO == null) {
			if (other.SERNO != null)
				return false;
		} else if (!SERNO.equals(other.SERNO))
			return false;
		if (USERID == null) {
			if (other.USERID != null)
				return false;
		} else if (!USERID.equals(other.USERID))
			return false;
		if (USER_COMPANY == null) {
			if (other.USER_COMPANY != null)
				return false;
		} else if (!USER_COMPANY.equals(other.USER_COMPANY))
			return false;
		return true;
	}
	
	
}
