package tw.org.twntch.form;

import java.util.List;

public class Unknown_Msg_Form extends CommonForm{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8344784735829403021L;
	private String SENDER_TYPE;
	private String RECEIVER_TYPE;
	private String OPBK_ID = "";
	private String TXDATE = "";
	private String SENDERBANK;
	private String RECEIVERBANK;
	private	List opbkIdList;
	
	public String getSENDER_TYPE() {
		return SENDER_TYPE;
	}
	public void setSENDER_TYPE(String sENDER_TYPE) {
		SENDER_TYPE = sENDER_TYPE;
	}
	public String getRECEIVER_TYPE() {
		return RECEIVER_TYPE;
	}
	public void setRECEIVER_TYPE(String rECEIVER_TYPE) {
		RECEIVER_TYPE = rECEIVER_TYPE;
	}
	public String getOPBK_ID() {
		return OPBK_ID;
	}
	public void setOPBK_ID(String oPBK_ID) {
		OPBK_ID = oPBK_ID;
	}
	public String getTXDATE() {
		return TXDATE;
	}
	public void setTXDATE(String tXDATE) {
		TXDATE = tXDATE;
	}
	public String getSENDERBANK() {
		return SENDERBANK;
	}
	public void setSENDERBANK(String sENDERBANK) {
		SENDERBANK = sENDERBANK;
	}
	public String getRECEIVERBANK() {
		return RECEIVERBANK;
	}
	public void setRECEIVERBANK(String rECEIVERBANK) {
		RECEIVERBANK = rECEIVERBANK;
	}
	public List getOpbkIdList() {
		return opbkIdList;
	}
	public void setOpbkIdList(List opbkIdList) {
		this.opbkIdList = opbkIdList;
	}
}
