package tw.org.twntch.form;

import java.util.List;
import java.util.Map;

public class Onclearing_Adj_Form extends CommonForm {
	String BIZDATE = "";
	String CLEARINGPHASE = "";
	String BANKID = "";
	String PCDE = "";
	Map map_BH = null;
	Map map_CL = null;
	public String getBIZDATE() {
		return BIZDATE;
	}
	public void setBIZDATE(String bIZDATE) {
		BIZDATE = bIZDATE;
	}
	public String getCLEARINGPHASE() {
		return CLEARINGPHASE;
	}
	public void setCLEARINGPHASE(String cLEARINGPHASE) {
		CLEARINGPHASE = cLEARINGPHASE;
	}
	public String getBANKID() {
		return BANKID;
	}
	public void setBANKID(String bANKID) {
		BANKID = bANKID;
	}
	public String getPCDE() {
		return PCDE;
	}
	public void setPCDE(String pCDE) {
		PCDE = pCDE;
	}
	public Map getMap_BH() {
		return map_BH;
	}
	public void setMap_BH(Map map_BH) {
		this.map_BH = map_BH;
	}
	public Map getMap_CL() {
		return map_CL;
	}
	public void setMap_CL(Map map_CL) {
		this.map_CL = map_CL;
	}

	
	
}
