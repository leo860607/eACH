package tw.org.twntch.po;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
@Entity(name="tw.org.twntch.po.WO_COMPANY_PROFILE")
@Table(name="WO_COMPANY_PROFILE")
public class WO_COMPANY_PROFILE implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6332855105572908557L;
	@EmbeddedId
	private WO_COMPANY_PROFILE_PK id ;
	private String WO_COMPANY_ABBR_NAME;
	private String WO_COMPANY_NAME;
	private String FMT_ID;
	private String INBANK_ACCT_NO;
	private String INBANK_ID;
	private String NOTE;
	private String SD_ITEM_NO;
	private String START_DATE;
	private String STOP_DATE;
	private String TXN_ID;
	private String TYPE_ACCT;
	private String TYPE_BARCODE;
	private String TYPE_WRITE_OFF_NO;
	private String C_DATE;
	private String U_DATE;
	
	private String IS_INTEGRATED;
	private String SD_ITEM;
	private String ITEM_START;
	private String ITEM_END;
	private String SD_ITEM_ID;
	private String VIRTUAL_ACC_NOTE;
	private String WO_NO_NOTE;
	
	
	
	
	@Transient
	private	String	WO_COMPANY_ID	;
	@Transient
	private	String	BILL_TYPE_ID ;
	@Transient
	private String BILL_TYPE_NAME;
	@Transient
	private String INBANK_NAME;
	
	@Transient
	private	String	COMPANY_ID	;
	@Transient
	private String COMPANY_ABBR_NAME;
	@Transient
	private String COMPANY_NAME;
	
	
	public WO_COMPANY_PROFILE_PK getId() {
		return id;
	}
	public void setId(WO_COMPANY_PROFILE_PK id) {
		this.id = id;
	}
	
	public String getFMT_ID() {
		return FMT_ID;
	}
	public void setFMT_ID(String fMT_ID) {
		FMT_ID = fMT_ID;
	}
	public String getINBANK_ACCT_NO() {
		return INBANK_ACCT_NO;
	}
	public void setINBANK_ACCT_NO(String iNBANK_ACCT_NO) {
		INBANK_ACCT_NO = iNBANK_ACCT_NO;
	}
	public String getINBANK_ID() {
		return INBANK_ID;
	}
	public void setINBANK_ID(String iNBANK_ID) {
		INBANK_ID = iNBANK_ID;
	}
	public String getNOTE() {
		return NOTE;
	}
	public void setNOTE(String nOTE) {
		NOTE = nOTE;
	}
	public String getSD_ITEM_NO() {
		return SD_ITEM_NO;
	}
	public void setSD_ITEM_NO(String sD_ITEM_NO) {
		SD_ITEM_NO = sD_ITEM_NO;
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
	public String getTYPE_ACCT() {
		return TYPE_ACCT;
	}
	public void setTYPE_ACCT(String tYPE_ACCT) {
		TYPE_ACCT = tYPE_ACCT;
	}
	public String getTYPE_BARCODE() {
		return TYPE_BARCODE;
	}
	public void setTYPE_BARCODE(String tYPE_BARCODE) {
		TYPE_BARCODE = tYPE_BARCODE;
	}
	public String getTYPE_WRITE_OFF_NO() {
		return TYPE_WRITE_OFF_NO;
	}
	public void setTYPE_WRITE_OFF_NO(String tYPE_WRITE_OFF_NO) {
		TYPE_WRITE_OFF_NO = tYPE_WRITE_OFF_NO;
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
	public String getINBANK_NAME() {
		return INBANK_NAME;
	}
	public void setINBANK_NAME(String iNBANK_NAME) {
		INBANK_NAME = iNBANK_NAME;
	}
	
	
	
	
	public String getIS_INTEGRATED() {
		return IS_INTEGRATED;
	}
	public void setIS_INTEGRATED(String iS_INTEGRATED) {
		IS_INTEGRATED = iS_INTEGRATED;
	}
	public String getSD_ITEM() {
		return SD_ITEM;
	}
	public void setSD_ITEM(String sD_ITEM) {
		SD_ITEM = sD_ITEM;
	}
	public String getITEM_START() {
		return ITEM_START;
	}
	public void setITEM_START(String iTEM_START) {
		ITEM_START = iTEM_START;
	}
	public String getITEM_END() {
		return ITEM_END;
	}
	public void setITEM_END(String iTEM_END) {
		ITEM_END = iTEM_END;
	}
	public String getSD_ITEM_ID() {
		return SD_ITEM_ID;
	}
	public void setSD_ITEM_ID(String sD_ITEM_ID) {
		SD_ITEM_ID = sD_ITEM_ID;
	}
	public String getVIRTUAL_ACC_NOTE() {
		return VIRTUAL_ACC_NOTE;
	}
	public void setVIRTUAL_ACC_NOTE(String vIRTUAL_ACC_NOTE) {
		VIRTUAL_ACC_NOTE = vIRTUAL_ACC_NOTE;
	}
	public String getWO_NO_NOTE() {
		return WO_NO_NOTE;
	}
	public void setWO_NO_NOTE(String wO_NO_NOTE) {
		WO_NO_NOTE = wO_NO_NOTE;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getWO_COMPANY_ABBR_NAME() {
		return WO_COMPANY_ABBR_NAME;
	}
	public void setWO_COMPANY_ABBR_NAME(String wO_COMPANY_ABBR_NAME) {
		WO_COMPANY_ABBR_NAME = wO_COMPANY_ABBR_NAME;
	}
	public String getWO_COMPANY_NAME() {
		return WO_COMPANY_NAME;
	}
	public void setWO_COMPANY_NAME(String wO_COMPANY_NAME) {
		WO_COMPANY_NAME = wO_COMPANY_NAME;
	}
	public String getWO_COMPANY_ID() {
		return WO_COMPANY_ID;
	}
	public void setWO_COMPANY_ID(String wO_COMPANY_ID) {
		WO_COMPANY_ID = wO_COMPANY_ID;
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
		WO_COMPANY_PROFILE other = (WO_COMPANY_PROFILE) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
	

	
	
}
