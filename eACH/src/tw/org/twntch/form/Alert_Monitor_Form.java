package tw.org.twntch.form;

public class Alert_Monitor_Form extends CommonForm{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7229063449252475376L;
	
	//監控新增
	private String MONITOR_AMOUNT;
	private String MONITOR_AMOUNT_PERIOD;
	private String MONITOR_PENDING;
	private String MONITOR_PENDING_PERIOD;
	private String MONITOR_MAILRECEIVER;
	
	public String getMONITOR_AMOUNT() {
		return MONITOR_AMOUNT;
	}
	public void setMONITOR_AMOUNT(String mONITOR_AMOUNT) {
		MONITOR_AMOUNT = mONITOR_AMOUNT;
	}
	public String getMONITOR_AMOUNT_PERIOD() {
		return MONITOR_AMOUNT_PERIOD;
	}
	public void setMONITOR_AMOUNT_PERIOD(String mONITOR_AMOUNT_PERIOD) {
		MONITOR_AMOUNT_PERIOD = mONITOR_AMOUNT_PERIOD;
	}
	public String getMONITOR_PENDING() {
		return MONITOR_PENDING;
	}
	public void setMONITOR_PENDING(String mONITOR_PENDING) {
		MONITOR_PENDING = mONITOR_PENDING;
	}
	public String getMONITOR_PENDING_PERIOD() {
		return MONITOR_PENDING_PERIOD;
	}
	public void setMONITOR_PENDING_PERIOD(String mONITOR_PENDING_PERIOD) {
		MONITOR_PENDING_PERIOD = mONITOR_PENDING_PERIOD;
	}
	public String getMONITOR_MAILRECEIVER() {
		return MONITOR_MAILRECEIVER;
	}
	public void setMONITOR_MAILRECEIVER(String mONITOR_MAILRECEIVER) {
		MONITOR_MAILRECEIVER = mONITOR_MAILRECEIVER;
	}
}
