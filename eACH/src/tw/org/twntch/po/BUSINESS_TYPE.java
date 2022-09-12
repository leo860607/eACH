package tw.org.twntch.po;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "tw.org.twntch.po.BUSINESS_TYPE")
@Table(name = "BUSINESS_TYPE")
public class BUSINESS_TYPE implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1788986625072345048L;
	/**
	 * 
	 */
	
	/**
	 *
	 *  21XX-帳單扣款類
		22XX-入帳類
		23XX-數位錢包類
		24XX-中華QR扣類
		25XX-網路購物類
		26XX-晶片卡轉帳類
		27XX-繳費類交易
		28XX-自動充值
		41XX-整批交易
		51XX-結帳類
		61XX-授權類
	 */
	@Id
	private	String	BUSINESS_TYPE_ID	;
	private	String	BUSINESS_TYPE_NAME	;
	private	String	CDATE	;
	private	String	UDATE	;
	
	public String getBUSINESS_TYPE_ID() {
		return BUSINESS_TYPE_ID;
	}
	public void setBUSINESS_TYPE_ID(String bUSINESS_TYPE_ID) {
		BUSINESS_TYPE_ID = bUSINESS_TYPE_ID;
	}
	public String getBUSINESS_TYPE_NAME() {
		return BUSINESS_TYPE_NAME;
	}
	public void setBUSINESS_TYPE_NAME(String bUSINESS_TYPE_NAME) {
		BUSINESS_TYPE_NAME = bUSINESS_TYPE_NAME;
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
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((BUSINESS_TYPE_ID == null) ? 0 : BUSINESS_TYPE_ID.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BUSINESS_TYPE other = (BUSINESS_TYPE) obj;
		if (BUSINESS_TYPE_ID == null) {
			if (other.BUSINESS_TYPE_ID != null)
				return false;
		} else if (!BUSINESS_TYPE_ID.equals(other.BUSINESS_TYPE_ID))
			return false;
		return true;
	}
	
	
	
}
