package tw.org.twntch.po;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "tw.org.twntch.po.BANKSYSSTATUSTAB")
@Table(name = "BANKSYSSTATUSTAB")
public class BANKSYSSTATUSTAB implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8235099370514183736L;
	private String BGBK_ID;
	private String MBSYSSTATUS;
	private String XCHGNCD01;
	private String XCHGNCD02;
	private String XCHGNCD03;
	private String XCHGNCD04;
	private String UNSYNCCNT01;
	private String UNSYNCCNT02;
	private String UNSYNCCNT03;
	private String UNSYNCCNT04;

	@Id
	public String getBGBK_ID() {
		return BGBK_ID;
	}
	public void setBGBK_ID(String bGBK_ID) {
		BGBK_ID = bGBK_ID;
	}
	public String getMBSYSSTATUS() {
		return MBSYSSTATUS;
	}
	public void setMBSYSSTATUS(String mBSYSSTATUS) {
		MBSYSSTATUS = mBSYSSTATUS;
	}
	public String getXCHGNCD01() {
		return XCHGNCD01;
	}
	public void setXCHGNCD01(String xCHGNCD01) {
		XCHGNCD01 = xCHGNCD01;
	}
	public String getXCHGNCD02() {
		return XCHGNCD02;
	}
	public void setXCHGNCD02(String xCHGNCD02) {
		XCHGNCD02 = xCHGNCD02;
	}
	public String getXCHGNCD03() {
		return XCHGNCD03;
	}
	public void setXCHGNCD03(String xCHGNCD03) {
		XCHGNCD03 = xCHGNCD03;
	}
	public String getXCHGNCD04() {
		return XCHGNCD04;
	}
	public void setXCHGNCD04(String xCHGNCD04) {
		XCHGNCD04 = xCHGNCD04;
	}
	public String getUNSYNCCNT01() {
		return UNSYNCCNT01;
	}
	public void setUNSYNCCNT01(String uNSYNCCNT01) {
		UNSYNCCNT01 = uNSYNCCNT01;
	}
	public String getUNSYNCCNT02() {
		return UNSYNCCNT02;
	}
	public void setUNSYNCCNT02(String uNSYNCCNT02) {
		UNSYNCCNT02 = uNSYNCCNT02;
	}
	public String getUNSYNCCNT03() {
		return UNSYNCCNT03;
	}
	public void setUNSYNCCNT03(String uNSYNCCNT03) {
		UNSYNCCNT03 = uNSYNCCNT03;
	}
	public String getUNSYNCCNT04() {
		return UNSYNCCNT04;
	}
	public void setUNSYNCCNT04(String uNSYNCCNT04) {
		UNSYNCCNT04 = uNSYNCCNT04;
	}
	
}
