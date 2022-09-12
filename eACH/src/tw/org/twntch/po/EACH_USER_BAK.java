package tw.org.twntch.po;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
@Entity(name="tw.org.twntch.po.EACH_USER")
@Table(name="EACH_USER")
public class EACH_USER_BAK implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2075266464276340838L;
//	Transient
	
	private	String 	USER_COMPANY	;
	private	String 	USER_ID	;
	private	String 	COM_NAME	;
//	Transient end
	private EACH_USER_PK id ;
	private String USER_TYPE;
	private String USER_STATUS;
	private String USER_DESC;
	private String USE_IKEY;
	private String ROLE_ID;
	private Integer NOLOGIN_EXPIRE_DAY;
	private String IP;
	private Integer IDLE_TIMEOUT;
	private String LAST_LOGIN_DATE;
	private String LAST_LOGIN_IP;
	private String CDATE;
	private String UDATE;
	
	
	@Transient
	public String getUSER_COMPANY() {
		return USER_COMPANY;
	}
	public void setUSER_COMPANY(String uSER_COMPANY) {
		USER_COMPANY = uSER_COMPANY;
	}
	@Transient
	public String getUSER_ID() {
		return USER_ID;
	}
	public void setUSER_ID(String uSER_ID) {
		USER_ID = uSER_ID;
	}
	@Transient
	public String getCOM_NAME() {
		return COM_NAME;
	}
	public void setCOM_NAME(String cOM_NAME) {
		COM_NAME = cOM_NAME;
	}
	@EmbeddedId
	public EACH_USER_PK getId() {
		return id;
	}
	public void setId(EACH_USER_PK id) {
		this.id = id;
	}
	public String getUSER_TYPE() {
		return USER_TYPE;
	}
	public void setUSER_TYPE(String uSER_TYPE) {
		USER_TYPE = uSER_TYPE;
	}
	public String getUSER_STATUS() {
		return USER_STATUS;
	}
	public void setUSER_STATUS(String uSER_STATUS) {
		USER_STATUS = uSER_STATUS;
	}
	public String getUSER_DESC() {
		return USER_DESC;
	}
	public void setUSER_DESC(String uSER_DESC) {
		USER_DESC = uSER_DESC;
	}
	public String getUSE_IKEY() {
		return USE_IKEY;
	}
	public void setUSE_IKEY(String uSE_IKEY) {
		USE_IKEY = uSE_IKEY;
	}
	public String getROLE_ID() {
		return ROLE_ID;
	}
	public void setROLE_ID(String rOLE_ID) {
		ROLE_ID = rOLE_ID;
	}
	public Integer getNOLOGIN_EXPIRE_DAY() {
		return NOLOGIN_EXPIRE_DAY;
	}
	public void setNOLOGIN_EXPIRE_DAY(Integer nOLOGIN_EXPIRE_DAY) {
		NOLOGIN_EXPIRE_DAY = nOLOGIN_EXPIRE_DAY;
	}
	public String getIP() {
		return IP;
	}
	public void setIP(String iP) {
		IP = iP;
	}
	public Integer getIDLE_TIMEOUT() {
		return IDLE_TIMEOUT;
	}
	public void setIDLE_TIMEOUT(Integer iDLE_TIMEOUT) {
		IDLE_TIMEOUT = iDLE_TIMEOUT;
	}
	public String getLAST_LOGIN_DATE() {
		return LAST_LOGIN_DATE;
	}
	public void setLAST_LOGIN_DATE(String lAST_LOGIN_DATE) {
		LAST_LOGIN_DATE = lAST_LOGIN_DATE;
	}
	public String getLAST_LOGIN_IP() {
		return LAST_LOGIN_IP;
	}
	public void setLAST_LOGIN_IP(String lAST_LOGIN_IP) {
		LAST_LOGIN_IP = lAST_LOGIN_IP;
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
		EACH_USER_BAK other = (EACH_USER_BAK) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	
	
}
