package tw.org.twntch.po;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OrderBy;
import javax.persistence.Table;

@Entity(name= "tw.org.twntch.po.WK_DATE_CALENDAR")
@Table(name="WK_DATE_CALENDAR")
public class WK_DATE_CALENDAR implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2790938525294469301L;
	private String TXN_DATE;
	private Integer WEEKDAY;
	private String IS_TXN_DATE;
	private String CDATE;
	private String UDATE;
	
	@Id
	@OrderBy("TXN_DATE ASC")
	public String getTXN_DATE() {
		return TXN_DATE;
	}
	public void setTXN_DATE(String tXN_DATE) {
		TXN_DATE = tXN_DATE;
	}
	public Integer getWEEKDAY() {
		return WEEKDAY;
	}
	public void setWEEKDAY(Integer wEEKDAY) {
		WEEKDAY = wEEKDAY;
	}
	public String getIS_TXN_DATE() {
		return IS_TXN_DATE;
	}
	public void setIS_TXN_DATE(String iS_TXN_DATE) {
		IS_TXN_DATE = iS_TXN_DATE;
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
