package tw.org.twntch.po;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
@Entity(name = "tw.org.twntch.po.BANK_GROUP")
@Table(name = "MASTER_BANK_GROUP")
public class BANK_GROUP implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4098412301586046065L;
	
	@Transient
	private	String	OPBK_NAME	;
	@Transient
	private	String	CTBK_NAME	;	
	@Transient
	private	String	OP_START_DATE	;
	@Transient
	private	String	CT_START_DATE	;	
	@Transient
	private	String	OP_NOTE	;
	@Transient
	private	String	CT_NOTE	;	
	@Id
	private	String	BGBK_ID	;
	private	String	BGBK_NAME	;
	private	String	BGBK_ATTR	;
	private	String	CTBK_ACCT	;
	private	String	TCH_ID	;
	private	String	OPBK_ID	;
	private	String	CTBK_ID	;
	private	String	ACTIVE_DATE	;
	private	String	STOP_DATE	;
	private	String	SND_FEE_BRBK_ID	;
	private	String	OUT_FEE_BRBK_ID	;
	private	String	WO_FEE_BRBK_ID	;
	private	String	IN_FEE_BRBK_ID	;
	private	String	EDDA_FLAG	;
	private	String	CDATE	;
	private	String	UDATE	;
	private	String	HR_UPLOAD_MAX_FILE	;
	private	String	FILE_MAX_CNT	;
	private	String	IS_EACH	;
	
	
	
	public String getOPBK_NAME() {
		return OPBK_NAME;
	}
	public void setOPBK_NAME(String oPBK_NAME) {
		OPBK_NAME = oPBK_NAME;
	}
	public String getCTBK_NAME() {
		return CTBK_NAME;
	}
	public void setCTBK_NAME(String cTBK_NAME) {
		CTBK_NAME = cTBK_NAME;
	}
	public String getBGBK_ID() {
		return BGBK_ID;
	}
	public void setBGBK_ID(String bGBK_ID) {
		BGBK_ID = bGBK_ID;
	}
	public String getBGBK_NAME() {
		return BGBK_NAME;
	}
	public void setBGBK_NAME(String bGBK_NAME) {
		BGBK_NAME = bGBK_NAME;
	}
	public String getBGBK_ATTR() {
		return BGBK_ATTR;
	}
	public void setBGBK_ATTR(String bGBK_ATTR) {
		BGBK_ATTR = bGBK_ATTR;
	}
	public String getCTBK_ACCT() {
		return CTBK_ACCT;
	}
	public void setCTBK_ACCT(String cTBK_ACCT) {
		CTBK_ACCT = cTBK_ACCT;
	}
	public String getTCH_ID() {
		return TCH_ID;
	}
	public void setTCH_ID(String tCH_ID) {
		TCH_ID = tCH_ID;
	}
	public String getOPBK_ID() {
		return OPBK_ID;
	}
	public void setOPBK_ID(String oPBK_ID) {
		OPBK_ID = oPBK_ID;
	}
	public String getCTBK_ID() {
		return CTBK_ID;
	}
	public void setCTBK_ID(String cTBK_ID) {
		CTBK_ID = cTBK_ID;
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
	public String getSND_FEE_BRBK_ID() {
		return SND_FEE_BRBK_ID;
	}
	public void setSND_FEE_BRBK_ID(String sND_FEE_BRBK_ID) {
		SND_FEE_BRBK_ID = sND_FEE_BRBK_ID;
	}
	public String getOUT_FEE_BRBK_ID() {
		return OUT_FEE_BRBK_ID;
	}
	public void setOUT_FEE_BRBK_ID(String oUT_FEE_BRBK_ID) {
		OUT_FEE_BRBK_ID = oUT_FEE_BRBK_ID;
	}
	public String getIN_FEE_BRBK_ID() {
		return IN_FEE_BRBK_ID;
	}
	public void setIN_FEE_BRBK_ID(String iN_FEE_BRBK_ID) {
		IN_FEE_BRBK_ID = iN_FEE_BRBK_ID;
	}
	public String getEDDA_FLAG() {
		return EDDA_FLAG;
	}
	public void setEDDA_FLAG(String eDDA_FLAG) {
		EDDA_FLAG = eDDA_FLAG;
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
	
	public String getHR_UPLOAD_MAX_FILE() {
		return HR_UPLOAD_MAX_FILE;
	}
	public void setHR_UPLOAD_MAX_FILE(String hR_UPLOAD_MAX_FILE) {
		HR_UPLOAD_MAX_FILE = hR_UPLOAD_MAX_FILE;
	}
	
	public String getIS_EACH() {
		return IS_EACH;
	}
	public void setIS_EACH(String iS_EACH) {
		IS_EACH = iS_EACH;
	}
	
	
	
	public String getWO_FEE_BRBK_ID() {
		return WO_FEE_BRBK_ID;
	}
	public void setWO_FEE_BRBK_ID(String wO_FEE_BRBK_ID) {
		WO_FEE_BRBK_ID = wO_FEE_BRBK_ID;
	}
	public String getFILE_MAX_CNT() {
		return FILE_MAX_CNT;
	}
	public void setFILE_MAX_CNT(String fILE_MAX_CNT) {
		FILE_MAX_CNT = fILE_MAX_CNT;
	}
	@Transient
	public String getOP_START_DATE() {
		return OP_START_DATE;
	}
	public void setOP_START_DATE(String oP_START_DATE) {
		OP_START_DATE = oP_START_DATE;
	}
	@Transient
	public String getCT_START_DATE() {
		return CT_START_DATE;
	}
	public void setCT_START_DATE(String cT_START_DATE) {
		CT_START_DATE = cT_START_DATE;
	}
	
	
	@Transient
	public String getOP_NOTE() {
		return OP_NOTE;
	}
	public void setOP_NOTE(String oP_NOTE) {
		OP_NOTE = oP_NOTE;
	}
	@Transient
	public String getCT_NOTE() {
		return CT_NOTE;
	}
	public void setCT_NOTE(String cT_NOTE) {
		CT_NOTE = cT_NOTE;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((BGBK_ID == null) ? 0 : BGBK_ID.hashCode());
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
		BANK_GROUP other = (BANK_GROUP) obj;
		if (BGBK_ID == null) {
			if (other.BGBK_ID != null)
				return false;
		} else if (!BGBK_ID.equals(other.BGBK_ID))
			return false;
		return true;
	}
	
	
	
}
