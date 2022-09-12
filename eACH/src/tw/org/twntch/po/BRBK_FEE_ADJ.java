package tw.org.twntch.po;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity (name = "tw.org.twntch.po.BRBK_FEE_ADJ")
@Table(name = "BRBK_FEE_ADJ")
public class BRBK_FEE_ADJ implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6292819702871980781L;
	@EmbeddedId
	private BRBK_FEE_ADJ_PK id;
	private Integer SNO;
	private String BRBK_NAME;
	private String FEE;
	private String ACTIVE_DATE;
	private String CDATE;
	private String UDATE;
	@EmbeddedId
	public BRBK_FEE_ADJ_PK getId() {
		return id;
	}
	public void setId(BRBK_FEE_ADJ_PK id) {
		this.id = id;
	}
	public Integer getSNO() {
		return SNO;
	}
	public void setSNO(Integer sNO) {
		SNO = sNO;
	}
	public String getBRBK_NAME() {
		return BRBK_NAME;
	}
	public void setBRBK_NAME(String bRBK_NAME) {
		BRBK_NAME = bRBK_NAME;
	}
	public String getFEE() {
		return FEE;
	}
	public void setFEE(String fEE) {
		FEE = fEE;
	}
	public String getACTIVE_DATE() {
		return ACTIVE_DATE;
	}
	public void setACTIVE_DATE(String aCTIVE_DATE) {
		ACTIVE_DATE = aCTIVE_DATE;
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
	
}
