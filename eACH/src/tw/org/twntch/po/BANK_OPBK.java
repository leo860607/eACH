package tw.org.twntch.po;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity (name = "tw.org.twntch.po.BANK_OPBK")
@Table(name = "BANK_OPBK")
public class BANK_OPBK implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1946783387317874612L;
	@EmbeddedId
	private BANK_OPBK_PK id ;
	private String OPBK_ID;
	private String OP_NOTE;
	@Transient
	private String OPBK_NAME;
	@Transient
	private String BGBK_NAME;
	@Transient
	private String BGBK_ID;
	@Transient
	private String START_DATE;
	

	public BANK_OPBK_PK getId() {
		return id;
	}
	public void setId(BANK_OPBK_PK id) {
		this.id = id;
	}
	public String getOPBK_ID() {
		return OPBK_ID;
	}
	public void setOPBK_ID(String oPBK_ID) {
		OPBK_ID = oPBK_ID;
	}
	
	
	@Transient
	public String getOPBK_NAME() {
		return OPBK_NAME;
	}
	public void setOPBK_NAME(String oPBK_NAME) {
		OPBK_NAME = oPBK_NAME;
	}
	public String getBGBK_NAME() {
		return BGBK_NAME;
	}
	public void setBGBK_NAME(String bGBK_NAME) {
		BGBK_NAME = bGBK_NAME;
	}
	public String getBGBK_ID() {
		return BGBK_ID;
	}
	public void setBGBK_ID(String bGBK_ID) {
		BGBK_ID = bGBK_ID;
	}
	public String getSTART_DATE() {
		return START_DATE;
	}
	public void setSTART_DATE(String sTART_DATE) {
		START_DATE = sTART_DATE;
	}
	public String getOP_NOTE() {
		return OP_NOTE;
	}
	public void setOP_NOTE(String oP_NOTE) {
		OP_NOTE = oP_NOTE;
	}
	
	
	
}
