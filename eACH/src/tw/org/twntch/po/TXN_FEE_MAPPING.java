package tw.org.twntch.po;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name="tw.org.twntch.po.TXN_FEE_MAPPING")
@Table(name="TXN_FEE_MAPPING")
public class TXN_FEE_MAPPING implements Serializable{

	private static final long serialVersionUID = 8933864693230753627L;
	private TXN_FEE_MAPPING_PK id;
	private	String MAPPING_START_DATE;
	private String CDATE;
	private String UDATE;
	
	public TXN_FEE_MAPPING() {
		super();
	}
	public TXN_FEE_MAPPING(TXN_FEE_MAPPING_PK id) {
		super();
		this.id = id;
	}
	@EmbeddedId
	public TXN_FEE_MAPPING_PK getId() {
		return id;
	}
	public void setId(TXN_FEE_MAPPING_PK id) {
		this.id = id;
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
	public String getMAPPING_START_DATE() {
		return MAPPING_START_DATE;
	}
	public void setMAPPING_START_DATE(String mAPPING_START_DATE) {
		MAPPING_START_DATE = mAPPING_START_DATE;
	}
}
