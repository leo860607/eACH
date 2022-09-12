package tw.org.twntch.po;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity(name="tw.org.twntch.po.TXN_CODE")
@Table(name="TXN_CODE")
public class TXN_CODE implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1850590745698058824L;
	@Id
	private	String	TXN_ID	;
	private	String	TXN_NAME	;
	private	String	TXN_TYPE	;
	private String 	TXN_CHECK_TYPE;
	private	String	TXN_DESC	;
	private	String	ACTIVE_DATE	;
	private	BigDecimal	MAX_TXN_AMT	;
	private	String	BUSINESS_TYPE_ID	;
	private	String	CDATE	;
	private	String	UDATE	;
	private	String	AGENT_TXN_ID	;
	@Transient
	private String	FEE_ID	;
	@Transient
	private String	START_DATE	;
	
	@OrderBy("TXN_ID ASC")
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
	public BigDecimal getMAX_TXN_AMT() {
		return MAX_TXN_AMT;
	}
	public void setMAX_TXN_AMT(BigDecimal mAX_TXN_AMT) {
		MAX_TXN_AMT = mAX_TXN_AMT;
	}
	public String getBUSINESS_TYPE_ID() {
		return BUSINESS_TYPE_ID;
	}
	public void setBUSINESS_TYPE_ID(String bUSINESS_TYPE_ID) {
		BUSINESS_TYPE_ID = bUSINESS_TYPE_ID;
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
	
	public String getAGENT_TXN_ID() {
		return AGENT_TXN_ID;
	}
	public void setAGENT_TXN_ID(String aGENT_TXN_ID) {
		AGENT_TXN_ID = aGENT_TXN_ID;
	}
	@Transient
	public String getFEE_ID() {
		return FEE_ID;
	}
	public void setFEE_ID(String fEE_ID) {
		FEE_ID = fEE_ID;
	}
	@Transient
	public String getSTART_DATE() {
		return START_DATE;
	}
	public void setSTART_DATE(String sTART_DATE) {
		START_DATE = sTART_DATE;
	}
	
	
}
