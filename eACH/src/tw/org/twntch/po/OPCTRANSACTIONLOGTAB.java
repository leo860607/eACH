package tw.org.twntch.po;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity (name = "tw.org.twntch.po.OPCTRANSACTIONLOGTAB")
@Table(name = "OPCTRANSACTIONLOGTAB")
public class OPCTRANSACTIONLOGTAB implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1992228716151746583L;
	private OPCTRANSACTIONLOGTAB_PK id;
	private String PK_STAN;
	private String PK_TXDATE;
	private String TXTIME;
	private String PCODE;
	private String RSPCODE;
	private String FEPPROCESSRESULT;
	private String CONCODE;
	private String CONTIME;
	private String FEPTRACENO;
	private String IDFIELD;
	private String DATAFIELD;
	private String INQSTATUS;
	private String WEBTRACENO;
	private String BANKID;
	//@Transient
	private String N_TRACENO;
	private String BANKNAME;
	private String FEP_ERR_DESC;
	private String RSP_ERR_DESC;
	private String PNAME;
	private String TXDATE;
	private String TXDT;
	private String BANK;
	private String RSPRESULTCODE;
	private String CHG_PCODE;
	
	
	private String ERROR1200;
	private String ERROR1210;
	private String ERROR1310;
	
	@EmbeddedId
	public OPCTRANSACTIONLOGTAB_PK getId() {
		return id;
	}
	public void setId(OPCTRANSACTIONLOGTAB_PK id) {
		this.id = id;
	}
	@Transient
	public String getPK_STAN() {
		return PK_STAN;
	}
	public void setPK_STAN(String pK_STAN) {
		PK_STAN = pK_STAN;
	}
	@Transient
	public String getPK_TXDATE() {
		return PK_TXDATE;
	}
	public void setPK_TXDATE(String pK_TXDATE) {
		PK_TXDATE = pK_TXDATE;
	}
	public String getTXTIME() {
		return TXTIME;
	}
	public void setTXTIME(String tXTIME) {
		TXTIME = tXTIME;
	}
	public String getPCODE() {
		return PCODE;
	}
	public void setPCODE(String pCODE) {
		PCODE = pCODE;
	}
	public String getRSPCODE() {
		return RSPCODE;
	}
	public void setRSPCODE(String rSPCODE) {
		RSPCODE = rSPCODE;
	}
	public String getFEPPROCESSRESULT() {
		return FEPPROCESSRESULT;
	}
	public void setFEPPROCESSRESULT(String fEPPROCESSRESULT) {
		FEPPROCESSRESULT = fEPPROCESSRESULT;
	}
	public String getCONCODE() {
		return CONCODE;
	}
	public void setCONCODE(String cONCODE) {
		CONCODE = cONCODE;
	}
	public String getCONTIME() {
		return CONTIME;
	}
	public void setCONTIME(String cONTIME) {
		CONTIME = cONTIME;
	}
	public String getFEPTRACENO() {
		return FEPTRACENO;
	}
	public void setFEPTRACENO(String fEPTRACENO) {
		FEPTRACENO = fEPTRACENO;
	}
	public String getIDFIELD() {
		return IDFIELD;
	}
	public void setIDFIELD(String iDFIELD) {
		IDFIELD = iDFIELD;
	}
	public String getDATAFIELD() {
		return DATAFIELD;
	}
	public void setDATAFIELD(String dATAFIELD) {
		DATAFIELD = dATAFIELD;
	}
	public String getINQSTATUS() {
		return INQSTATUS;
	}
	public void setINQSTATUS(String iNQSTATUS) {
		INQSTATUS = iNQSTATUS;
	}
	public String getWEBTRACENO() {
		return WEBTRACENO;
	}
	public void setWEBTRACENO(String wEBTRACENO) {
		WEBTRACENO = wEBTRACENO;
	}
	public String getBANKID() {
		return BANKID;
	}
	public void setBANKID(String bANKID) {
		BANKID = bANKID;
	}
	@Transient
	public String getN_TRACENO() {
		return N_TRACENO;
	}
	public void setN_TRACENO(String n_TRACENO) {
		N_TRACENO = n_TRACENO;
	}
	@Transient
	public String getBANKNAME() {
		return BANKNAME;
	}
	public void setBANKNAME(String bANKNAME) {
		BANKNAME = bANKNAME;
	}
	@Transient
	public String getFEP_ERR_DESC() {
		return FEP_ERR_DESC;
	}
	public void setFEP_ERR_DESC(String fEP_ERR_DESC) {
		FEP_ERR_DESC = fEP_ERR_DESC;
	}
	@Transient
	public String getRSP_ERR_DESC() {
		return RSP_ERR_DESC;
	}
	public void setRSP_ERR_DESC(String rSP_ERR_DESC) {
		RSP_ERR_DESC = rSP_ERR_DESC;
	}
	@Transient
	public String getPNAME() {
		return PNAME;
	}
	public void setPNAME(String pNAME) {
		PNAME = pNAME;
	}
	@Transient
	public String getTXDATE() {
		return TXDATE;
	}
	public void setTXDATE(String tXDATE) {
		TXDATE = tXDATE;
	}
	@Transient
	public String getBANK() {
		return BANK;
	}
	public void setBANK(String bANK) {
		BANK = bANK;
	}
	@Transient
	public String getTXDT() {
		return TXDT;
	}
	public void setTXDT(String tXDT) {
		TXDT = tXDT;
	}
	@Transient
	public String getRSPRESULTCODE() {
		return RSPRESULTCODE;
	}
	public void setRSPRESULTCODE(String rSPRESULTCODE) {
		RSPRESULTCODE = rSPRESULTCODE;
	}
	@Transient
	public String getCHG_PCODE() {
		return CHG_PCODE;
	}
	public void setCHG_PCODE(String cHG_PCODE) {
		CHG_PCODE = cHG_PCODE;
	}
	@Transient
	public String getERROR1200() {
		return ERROR1200;
	}
	public void setERROR1200(String eRROR1200) {
		ERROR1200 = eRROR1200;
	}
	@Transient
	public String getERROR1210() {
		return ERROR1210;
	}
	public void setERROR1210(String eRROR1210) {
		ERROR1210 = eRROR1210;
	}
	@Transient
	public String getERROR1310() {
		return ERROR1310;
	}
	public void setERROR1310(String eRROR1310) {
		ERROR1310 = eRROR1310;
	}
	
	
}
