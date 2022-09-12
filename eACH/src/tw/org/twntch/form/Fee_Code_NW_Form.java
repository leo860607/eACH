package tw.org.twntch.form;

import java.util.List;

public class Fee_Code_NW_Form extends CommonForm{
	private String		FEE_UID;
	private String		FEE_TYPE;
	private String		FEE_ID;
	private String		FEE_DTNO;
	private String		FEE_LVL_TYPE;
	private String		FEE_LVL_BEG_AMT;
	private String		FEE_LVL_END_AMT;
	private String		START_DATE;
	private	String 		OUT_BANK_FEE;
	private	String 		OUT_BANK_FEE_DISC;
	private	String 		IN_BANK_FEE;
	private	String 		IN_BANK_FEE_DISC;
	private	String 		TCH_FEE;
	private	String 		TCH_FEE_DISC;
	private	String 		SND_BANK_FEE;
	private	String 		SND_BANK_FEE_DISC;
	private	String 		WO_BANK_FEE;
	private	String 		WO_BANK_FEE_DISC;
	private	String		FEE_DESC;
	private String		ACTIVE_DESC;
	private String		HANDLECHARGE;
	private	String    	CDATE;
	private	String    	UDATE;
	private List		idList;
	private String		jsonList;	
	private List		scaseary;
	
	public String getFEE_ID() {
		return FEE_ID;
	}
	public void setFEE_ID(String fEE_ID) {
		FEE_ID = fEE_ID;
	}
	public String getSTART_DATE() {
		return START_DATE;
	}
	public void setSTART_DATE(String sTART_DATE) {
		START_DATE = sTART_DATE;
	}
	public String getOUT_BANK_FEE() {
		return OUT_BANK_FEE;
	}
	public void setOUT_BANK_FEE(String oUT_BANK_FEE) {
		OUT_BANK_FEE = oUT_BANK_FEE;
	}
	public String getOUT_BANK_FEE_DISC() {
		return OUT_BANK_FEE_DISC;
	}
	public void setOUT_BANK_FEE_DISC(String oUT_BANK_FEE_DISC) {
		OUT_BANK_FEE_DISC = oUT_BANK_FEE_DISC;
	}
	public String getIN_BANK_FEE() {
		return IN_BANK_FEE;
	}
	public void setIN_BANK_FEE(String iN_BANK_FEE) {
		IN_BANK_FEE = iN_BANK_FEE;
	}
	public String getIN_BANK_FEE_DISC() {
		return IN_BANK_FEE_DISC;
	}
	public void setIN_BANK_FEE_DISC(String iN_BANK_FEE_DISC) {
		IN_BANK_FEE_DISC = iN_BANK_FEE_DISC;
	}
	public String getTCH_FEE() {
		return TCH_FEE;
	}
	public void setTCH_FEE(String tCH_FEE) {
		TCH_FEE = tCH_FEE;
	}
	public String getTCH_FEE_DISC() {
		return TCH_FEE_DISC;
	}
	public void setTCH_FEE_DISC(String tCH_FEE_DISC) {
		TCH_FEE_DISC = tCH_FEE_DISC;
	}
	public String getSND_BANK_FEE() {
		return SND_BANK_FEE;
	}
	public void setSND_BANK_FEE(String sND_BANK_FEE) {
		SND_BANK_FEE = sND_BANK_FEE;
	}
	public String getSND_BANK_FEE_DISC() {
		return SND_BANK_FEE_DISC;
	}
	public void setSND_BANK_FEE_DISC(String sND_BANK_FEE_DISC) {
		SND_BANK_FEE_DISC = sND_BANK_FEE_DISC;
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
	public List getIdList() {
		return idList;
	}
	public void setIdList(List idList) {
		this.idList = idList;
	}
	public String getJsonList() {
		return jsonList;
	}
	public void setJsonList(String jsonList) {
		this.jsonList = jsonList;
	}
	public List getScaseary() {
		return scaseary;
	}
	public void setScaseary(List scaseary) {
		this.scaseary = scaseary;
	}
	public String getHANDLECHARGE() {
		return HANDLECHARGE;
	}
	public void setHANDLECHARGE(String hANDLECHARGE) {
		HANDLECHARGE = hANDLECHARGE;
	}
	public String getWO_BANK_FEE() {
		return WO_BANK_FEE;
	}
	public void setWO_BANK_FEE(String wO_BANK_FEE) {
		WO_BANK_FEE = wO_BANK_FEE;
	}
	public String getWO_BANK_FEE_DISC() {
		return WO_BANK_FEE_DISC;
	}
	public void setWO_BANK_FEE_DISC(String wO_BANK_FEE_DISC) {
		WO_BANK_FEE_DISC = wO_BANK_FEE_DISC;
	}
	public String getFEE_UID() {
		return FEE_UID;
	}
	public void setFEE_UID(String fEE_UID) {
		FEE_UID = fEE_UID;
	}
	public String getFEE_TYPE() {
		return FEE_TYPE;
	}
	public void setFEE_TYPE(String fEE_TYPE) {
		FEE_TYPE = fEE_TYPE;
	}
	public String getFEE_DTNO() {
		return FEE_DTNO;
	}
	public void setFEE_DTNO(String fEE_DTNO) {
		FEE_DTNO = fEE_DTNO;
	}
	public String getFEE_LVL_TYPE() {
		return FEE_LVL_TYPE;
	}
	public void setFEE_LVL_TYPE(String fEE_LVL_TYPE) {
		FEE_LVL_TYPE = fEE_LVL_TYPE;
	}
	public String getFEE_LVL_BEG_AMT() {
		return FEE_LVL_BEG_AMT;
	}
	public void setFEE_LVL_BEG_AMT(String fEE_LVL_BEG_AMT) {
		FEE_LVL_BEG_AMT = fEE_LVL_BEG_AMT;
	}
	public String getFEE_LVL_END_AMT() {
		return FEE_LVL_END_AMT;
	}
	public void setFEE_LVL_END_AMT(String fEE_LVL_END_AMT) {
		FEE_LVL_END_AMT = fEE_LVL_END_AMT;
	}
	@Override
	public String toString() {
		return "Fee_Code_NW_Form [FEE_UID=" + FEE_UID + ", FEE_TYPE=" + FEE_TYPE + ", FEE_ID=" + FEE_ID + ", FEE_DTNO="
				+ FEE_DTNO + ", FEE_LVL_TYPE=" + FEE_LVL_TYPE + ", FEE_LVL_BEG_AMT=" + FEE_LVL_BEG_AMT
				+ ", FEE_LVL_END_AMT=" + FEE_LVL_END_AMT + ", START_DATE=" + START_DATE + ", OUT_BANK_FEE="
				+ OUT_BANK_FEE + ", OUT_BANK_FEE_DISC=" + OUT_BANK_FEE_DISC + ", IN_BANK_FEE=" + IN_BANK_FEE
				+ ", IN_BANK_FEE_DISC=" + IN_BANK_FEE_DISC + ", TCH_FEE=" + TCH_FEE + ", TCH_FEE_DISC=" + TCH_FEE_DISC
				+ ", SND_BANK_FEE=" + SND_BANK_FEE + ", SND_BANK_FEE_DISC=" + SND_BANK_FEE_DISC + ", WO_BANK_FEE="
				+ WO_BANK_FEE + ", WO_BANK_FEE_DISC=" + WO_BANK_FEE_DISC + ", FEE_DESC=" + FEE_DESC + ", ACTIVE_DESC="
				+ ACTIVE_DESC + ", HANDLECHARGE=" + HANDLECHARGE + ", CDATE=" + CDATE + ", UDATE=" + UDATE + ", idList="
				+ idList + ", jsonList=" + jsonList + ", scaseary=" + scaseary + "]";
	}
	
}
