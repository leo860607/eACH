package tw.org.twntch.form;

import java.math.BigDecimal;
import java.util.List;

public class Txn_Code_Form  extends CommonForm{
	//TXN_CODE
	private	String	TXN_ID	;
	private	String	TXN_NAME	;
	private	String	TXN_TYPE	;
	private	String	TXN_CHECK_TYPE	;
	private	String	TXN_DESC	;
	private	String	ACTIVE_DATE	;
	private	String	MAX_TXN_AMT	= "0";
	private	String	BUSINESS_TYPE_ID	;
	private	String	AGENT_TXN_ID	;
	
	//FEE_CODE
	private String		FEE_ID;
	private String		START_DATE;
	private	String 		OUT_BANK_FEE;
	private	String 		OUT_BANK_FEE_DISC;
	private	String 		IN_BANK_FEE;
	private	String 		IN_BANK_FEE_DISC;
	private	String 		TCH_FEE;
	private	String 		TCH_FEE_DISC;
	private	String 		SND_BANK_FEE;
	private	String 		HANDLECHARGE;
	private	String 		SND_BANK_FEE_DISC;
	private	String		FEE_DESC;
	private String		ACTIVE_DESC;
	private String		WO_BANK_FEE;
	private	String 		WO_BANK_FEE_DISC;
	
	private	List	scaseary	;
	private	List	idList	;
	private	List	agentTxnIdList	;
	private String  jsonList ;
	
	private List	bsTypeIdList;
	private List	distinctFeeIdList;
	private String	feeId;
	private String	MAPPING_START_DATE;
	
	private List	feeList;
	private String[] feeAry;
	private List	selectedFeeList;
	private String[] selectedFeeAry;
	
	public String getTXN_ID() {
		return TXN_ID;
	}
	public void setTXN_ID(String tXN_ID) {
		TXN_ID = tXN_ID;
	}
	public String getTXN_NAME() {
		return TXN_NAME;
	}
	public void setTXN_NAME(String tXN_NAME) {
		TXN_NAME = tXN_NAME;
	}
	public String getTXN_TYPE() {
		return TXN_TYPE;
	}
	public void setTXN_TYPE(String tXN_TYPE) {
		TXN_TYPE = tXN_TYPE;
	}
	public String getTXN_CHECK_TYPE() {
		return TXN_CHECK_TYPE;
	}
	public void setTXN_CHECK_TYPE(String tXN_CHECK_TYPE) {
		TXN_CHECK_TYPE = tXN_CHECK_TYPE;
	}
	public String getTXN_DESC() {
		return TXN_DESC;
	}
	public void setTXN_DESC(String tXN_DESC) {
		TXN_DESC = tXN_DESC;
	}
	public String getACTIVE_DATE() {
		return ACTIVE_DATE;
	}
	public void setACTIVE_DATE(String aCTIVE_DATE) {
		ACTIVE_DATE = aCTIVE_DATE;
	}
	public String getMAX_TXN_AMT() {
		return MAX_TXN_AMT;
	}
	public void setMAX_TXN_AMT(String mAX_TXN_AMT) {
		MAX_TXN_AMT = mAX_TXN_AMT;
	}
	public String getBUSINESS_TYPE_ID() {
		return BUSINESS_TYPE_ID;
	}
	public void setBUSINESS_TYPE_ID(String bUSINESS_TYPE_ID) {
		BUSINESS_TYPE_ID = bUSINESS_TYPE_ID;
	}
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
	public List getScaseary() {
		return scaseary;
	}
	public void setScaseary(List scaseary) {
		this.scaseary = scaseary;
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
	public List getBsTypeIdList() {
		return bsTypeIdList;
	}
	public void setBsTypeIdList(List bsTypeIdList) {
		this.bsTypeIdList = bsTypeIdList;
	}
	public List getDistinctFeeIdList() {
		return distinctFeeIdList;
	}
	public void setDistinctFeeIdList(List distinctFeeIdList) {
		this.distinctFeeIdList = distinctFeeIdList;
	}
	public String getFeeId() {
		return feeId;
	}
	public void setFeeId(String feeId) {
		this.feeId = feeId;
	}
	public String getMAPPING_START_DATE() {
		return MAPPING_START_DATE;
	}
	public void setMAPPING_START_DATE(String mAPPING_START_DATE) {
		MAPPING_START_DATE = mAPPING_START_DATE;
	}
	public List getFeeList() {
		return feeList;
	}
	public void setFeeList(List feeList) {
		this.feeList = feeList;
	}
	public String[] getFeeAry() {
		return feeAry;
	}
	public void setFeeAry(String[] feeAry) {
		this.feeAry = feeAry;
	}
	public List getSelectedFeeList() {
		return selectedFeeList;
	}
	public void setSelectedFeeList(List selectedFeeList) {
		this.selectedFeeList = selectedFeeList;
	}
	public String[] getSelectedFeeAry() {
		return selectedFeeAry;
	}
	public void setSelectedFeeAry(String[] selectedFeeAry) {
		this.selectedFeeAry = selectedFeeAry;
	}
	public String getAGENT_TXN_ID() {
		return AGENT_TXN_ID;
	}
	public void setAGENT_TXN_ID(String aGENT_TXN_ID) {
		AGENT_TXN_ID = aGENT_TXN_ID;
	}
	public List getAgentTxnIdList() {
		return agentTxnIdList;
	}
	public void setAgentTxnIdList(List agentTxnIdList) {
		this.agentTxnIdList = agentTxnIdList;
	}
	public String getHANDLECHARGE() {
		return HANDLECHARGE;
	}
	public void setHANDLECHARGE(String hANDLECHARGE) {
		HANDLECHARGE = hANDLECHARGE;
	}
	
	
}
