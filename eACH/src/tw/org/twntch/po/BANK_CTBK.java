package tw.org.twntch.po;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity (name = "tw.org.twntch.po.BANK_CTBK")
@Table(name = "BANK_CTBK")
public class BANK_CTBK implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9122816418378095677L;
	@EmbeddedId
	private BANK_CTBK_PK id ;
	private String CTBK_ID;
	private String CT_NOTE;
	
	
	@Transient
	private String CTBK_NAME;
	@Transient
	private String BGBK_NAME;
	@Transient
	private String BGBK_ID;
	@Transient
	private String START_DATE;
	
	public BANK_CTBK_PK getId() {
		return id;
	}
	public void setId(BANK_CTBK_PK id) {
		this.id = id;
	}
	public String getCTBK_ID() {
		return CTBK_ID;
	}
	public void setCTBK_ID(String cTBK_ID) {
		CTBK_ID = cTBK_ID;
	}
	@Transient
	public String getCTBK_NAME() {
		return CTBK_NAME;
	}
	public void setCTBK_NAME(String cTBK_NAME) {
		CTBK_NAME = cTBK_NAME;
	}
	@Transient
	public String getBGBK_ID() {
		return BGBK_ID;
	}
	public void setBGBK_ID(String bGBK_ID) {
		BGBK_ID = bGBK_ID;
	}
	@Transient
	public String getSTART_DATE() {
		return START_DATE;
	}
	public void setSTART_DATE(String sTART_DATE) {
		START_DATE = sTART_DATE;
	}
	@Transient
	public String getBGBK_NAME() {
		return BGBK_NAME;
	}
	public void setBGBK_NAME(String bGBK_NAME) {
		BGBK_NAME = bGBK_NAME;
	}
	public String getCT_NOTE() {
		return CT_NOTE;
	}
	public void setCT_NOTE(String cT_NOTE) {
		CT_NOTE = cT_NOTE;
	}
	
	
}
