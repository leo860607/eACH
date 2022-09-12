package tw.org.twntch.po;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
@Table(name="CHANGE_SC_PROFILE")
@Entity(name="tw.org.twntch.po.CHG_SC_PROFILE")
public class CHG_SC_PROFILE implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 561905123606805612L;
	private String SD_ITEM_NO;
	private String COMPANY_ID;
	private String COMPANY_ABBR_NAME;
	private String COMPANY_NAME;
	private String TXN_ID;
	private String INBANKID;
	private String INBANKACCTNO;
	private String LAYOUTID;
	private String DEALY_CHARGE_DAY;
	private String START_DATE;
	private String STOP_DATE;
	private String NOTE;
	@Transient
	private String INBANKNAME; 
	
	@Id
	public String getSD_ITEM_NO() {
		return SD_ITEM_NO;
	}
	public void setSD_ITEM_NO(String sD_ITEM_NO) {
		SD_ITEM_NO = sD_ITEM_NO;
	}
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
	public String getTXN_ID() {
		return TXN_ID;
	}
	public void setTXN_ID(String tXN_ID) {
		TXN_ID = tXN_ID;
	}
	public String getINBANKID() {
		return INBANKID;
	}
	public void setINBANKID(String iNBANKID) {
		INBANKID = iNBANKID;
	}
	public String getINBANKACCTNO() {
		return INBANKACCTNO;
	}
	public void setINBANKACCTNO(String iNBANKACCTNO) {
		INBANKACCTNO = iNBANKACCTNO;
	}
	public String getLAYOUTID() {
		return LAYOUTID;
	}
	public void setLAYOUTID(String lAYOUTID) {
		LAYOUTID = lAYOUTID;
	}
	public String getDEALY_CHARGE_DAY() {
		return DEALY_CHARGE_DAY;
	}
	public void setDEALY_CHARGE_DAY(String dEALY_CHARGE_DAY) {
		DEALY_CHARGE_DAY = dEALY_CHARGE_DAY;
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
	public String getNOTE() {
		return NOTE;
	}
	public void setNOTE(String nOTE) {
		NOTE = nOTE;
	}
	@Transient
	public String getINBANKNAME() {
		return INBANKNAME;
	}
	public void setINBANKNAME(String iNBANKNAME) {
		INBANKNAME = iNBANKNAME;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((COMPANY_ABBR_NAME == null) ? 0 : COMPANY_ABBR_NAME
						.hashCode());
		result = prime * result
				+ ((COMPANY_ID == null) ? 0 : COMPANY_ID.hashCode());
		result = prime * result
				+ ((COMPANY_NAME == null) ? 0 : COMPANY_NAME.hashCode());
		result = prime
				* result
				+ ((DEALY_CHARGE_DAY == null) ? 0 : DEALY_CHARGE_DAY.hashCode());
		result = prime * result
				+ ((INBANKACCTNO == null) ? 0 : INBANKACCTNO.hashCode());
		result = prime * result
				+ ((INBANKID == null) ? 0 : INBANKID.hashCode());
		result = prime * result
				+ ((INBANKNAME == null) ? 0 : INBANKNAME.hashCode());
		result = prime * result
				+ ((LAYOUTID == null) ? 0 : LAYOUTID.hashCode());
		result = prime * result + ((NOTE == null) ? 0 : NOTE.hashCode());
		result = prime * result
				+ ((SD_ITEM_NO == null) ? 0 : SD_ITEM_NO.hashCode());
		result = prime * result
				+ ((START_DATE == null) ? 0 : START_DATE.hashCode());
		result = prime * result
				+ ((STOP_DATE == null) ? 0 : STOP_DATE.hashCode());
		result = prime * result + ((TXN_ID == null) ? 0 : TXN_ID.hashCode());
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
		CHG_SC_PROFILE other = (CHG_SC_PROFILE) obj;
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
		if (DEALY_CHARGE_DAY == null) {
			if (other.DEALY_CHARGE_DAY != null)
				return false;
		} else if (!DEALY_CHARGE_DAY.equals(other.DEALY_CHARGE_DAY))
			return false;
		if (INBANKACCTNO == null) {
			if (other.INBANKACCTNO != null)
				return false;
		} else if (!INBANKACCTNO.equals(other.INBANKACCTNO))
			return false;
		if (INBANKID == null) {
			if (other.INBANKID != null)
				return false;
		} else if (!INBANKID.equals(other.INBANKID))
			return false;
		if (INBANKNAME == null) {
			if (other.INBANKNAME != null)
				return false;
		} else if (!INBANKNAME.equals(other.INBANKNAME))
			return false;
		if (LAYOUTID == null) {
			if (other.LAYOUTID != null)
				return false;
		} else if (!LAYOUTID.equals(other.LAYOUTID))
			return false;
		if (NOTE == null) {
			if (other.NOTE != null)
				return false;
		} else if (!NOTE.equals(other.NOTE))
			return false;
		if (SD_ITEM_NO == null) {
			if (other.SD_ITEM_NO != null)
				return false;
		} else if (!SD_ITEM_NO.equals(other.SD_ITEM_NO))
			return false;
		if (START_DATE == null) {
			if (other.START_DATE != null)
				return false;
		} else if (!START_DATE.equals(other.START_DATE))
			return false;
		if (STOP_DATE == null) {
			if (other.STOP_DATE != null)
				return false;
		} else if (!STOP_DATE.equals(other.STOP_DATE))
			return false;
		if (TXN_ID == null) {
			if (other.TXN_ID != null)
				return false;
		} else if (!TXN_ID.equals(other.TXN_ID))
			return false;
		return true;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CHG_SC_PROFILE [SD_ITEM_NO=");
		builder.append(SD_ITEM_NO);
		builder.append(", COMPANY_ID=");
		builder.append(COMPANY_ID);
		builder.append(", COMPANY_ABBR_NAME=");
		builder.append(COMPANY_ABBR_NAME);
		builder.append(", COMPANY_NAME=");
		builder.append(COMPANY_NAME);
		builder.append(", TXN_ID=");
		builder.append(TXN_ID);
		builder.append(", INBANKID=");
		builder.append(INBANKID);
		builder.append(", INBANKACCTNO=");
		builder.append(INBANKACCTNO);
		builder.append(", LAYOUTID=");
		builder.append(LAYOUTID);
		builder.append(", DEALY_CHARGE_DAY=");
		builder.append(DEALY_CHARGE_DAY);
		builder.append(", START_DATE=");
		builder.append(START_DATE);
		builder.append(", STOP_DATE=");
		builder.append(STOP_DATE);
		builder.append(", NOTE=");
		builder.append(NOTE);
		builder.append(", INBANKNAME=");
		builder.append(INBANKNAME);
		builder.append("]");
		return builder.toString();
	}
	
}
