package tw.org.twntch.po;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name="tw.org.twntch.po.CR_LINE_LOG")
@Table(name="CR_LINE_LOG")
public class CR_LINE_LOG implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6437356915187811018L;
	private Integer SEQ_ID;
	private String BANK_ID;
	private String NEW_BASIC_CR_LINE;
	private String NEW_REST_CR_LINE;
	private String OLD_BASIC_CR_LINE;
	private String OLD_REST_CR_LINE;
	private String USER_ID;
	private String CR_LINE_UPDATE_DATE;
	private String CDATE;
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer getSEQ_ID() {
		return SEQ_ID;
	}
	public void setSEQ_ID(Integer sEQ_ID) {
		SEQ_ID = sEQ_ID;
	}
	public String getBANK_ID() {
		return BANK_ID;
	}
	public void setBANK_ID(String bANK_ID) {
		BANK_ID = bANK_ID;
	}
	public String getNEW_BASIC_CR_LINE() {
		return NEW_BASIC_CR_LINE;
	}
	public void setNEW_BASIC_CR_LINE(String nEW_BASIC_CR_LINE) {
		NEW_BASIC_CR_LINE = nEW_BASIC_CR_LINE;
	}
	public String getNEW_REST_CR_LINE() {
		return NEW_REST_CR_LINE;
	}
	public void setNEW_REST_CR_LINE(String nEW_REST_CR_LINE) {
		NEW_REST_CR_LINE = nEW_REST_CR_LINE;
	}
	public String getOLD_BASIC_CR_LINE() {
		return OLD_BASIC_CR_LINE;
	}
	public void setOLD_BASIC_CR_LINE(String oLD_BASIC_CR_LINE) {
		OLD_BASIC_CR_LINE = oLD_BASIC_CR_LINE;
	}
	public String getOLD_REST_CR_LINE() {
		return OLD_REST_CR_LINE;
	}
	public void setOLD_REST_CR_LINE(String oLD_REST_CR_LINE) {
		OLD_REST_CR_LINE = oLD_REST_CR_LINE;
	}
	public String getUSER_ID() {
		return USER_ID;
	}
	public void setUSER_ID(String uSER_ID) {
		USER_ID = uSER_ID;
	}
	public String getCR_LINE_UPDATE_DATE() {
		return CR_LINE_UPDATE_DATE;
	}
	public void setCR_LINE_UPDATE_DATE(String cR_LINE_UPDATE_DATE) {
		CR_LINE_UPDATE_DATE = cR_LINE_UPDATE_DATE;
	}
	public String getCDATE() {
		return CDATE;
	}
	public void setCDATE(String cDATE) {
		CDATE = cDATE;
	}
	
	

}
