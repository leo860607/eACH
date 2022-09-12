package tw.org.twntch.socket;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="msg")
@XmlType(name="message",propOrder = {"header","body"})
public class Message {
	private Header header;
	private Body body;
//	private NotifyBody notifybody;
	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}

	public Body getBody() {
		return body;
	}

	public void setBody(Body body) {
		this.body = body;
	}
	



	@XmlType(name="header",propOrder={"SystemHeader","MsgType","PrsCode","Stan","InBank","OutBank","DateTime","RspCode"})
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class Header {
		private String SystemHeader;
		private String MsgType;			//訊息類別
		private String PrsCode;			//交易代碼
		private String Stan;			//交易追蹤序號
		private String InBank;			//接收單位代號
		private String OutBank;			//發動單位代號
		private String DateTime;		//交易日期時間
		private String RspCode;			//回應代碼
		public String getSystemHeader() {
			return SystemHeader;
		}
		public void setSystemHeader(String systemHeader) {
			SystemHeader = systemHeader;
		}
		public String getMsgType() {
			return MsgType;
		}
		public void setMsgType(String msgType) {
			MsgType = msgType;
		}
		public String getPrsCode() {
			return PrsCode;
		}
		public void setPrsCode(String prsCode) {
			PrsCode = prsCode;
		}
		public String getStan() {
			return Stan;
		}
		public void setStan(String stan) {
			Stan = stan;
		}
		public String getInBank() {
			return InBank;
		}
		public void setInBank(String inBank) {
			InBank = inBank;
		}
		public String getOutBank() {
			return OutBank;
		}
		public void setOutBank(String outBank) {
			OutBank = outBank;
		}
		public String getDateTime() {
			return DateTime;
		}
		public void setDateTime(String dateTime) {
			DateTime = dateTime;
		}
		public String getRspCode() {
			return RspCode;
		}
		public void setRspCode(String rspCode) {
			RspCode = rspCode;
		}
	}
//	20150413 加入2個屬性"BizDate","ClearingPhase" 為批次排程作業的結算通知使用
//	20150521 加入3個屬性"OTxDate","OSTAN","ResultCode" 未完成交易查詢 沖正作業使用
//	20150521 加入1個屬性"BatchSeqNo" 排程-重新執行getWay批次使用
//	201500701 加入1個屬性"Type" 排程-重新執行getWay批次使用(00表示起始點 01表是從中斷點)
//	@XmlType(name="body",propOrder={"ApId","KeyID","NoticeId","NoticeData","OrigStan","OrigTxAmt","Result","WebStep"})
	@XmlType(name="body",propOrder={"ApId","KeyID","NoticeId","NoticeData","OrigStan","OrigTxAmt","Result","WebStep" ,"BizDate","ClearingPhase","BatchSeqNo","OTxDate","OSTAN","ResultCode","IsBizDate" , "Type" , "BatchProcSeq" , "CompanyId"  , "Msg" })
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class Body {
		private String ApId;			//應用系統代號
		private String KeyID;			//基碼代號
		private String NoticeId;		//訊息代碼
		private String NoticeData;		//訊息
		private String OrigStan;		//原交易追蹤序號
		private String OrigTxAmt;		//原交易金額
		private String Result;			//處理結果
		private String WebStep;			//交換所開/關機作業(01)/通知(02)
//		排程-結算通知使用
		private String BizDate;//營業日
		private String ClearingPhase;//清算階段
//		排程-重新執行getWay批次使用
		private String BatchSeqNo;//排程序號
//		未完成交易查詢 沖正作業使用
		private String OTxDate; //交易日
		private String OSTAN;//系統追蹤序號
		private String ResultCode;//回覆訊息代碼 00:成功、01:失敗
//		營業日設定 使用
		private String IsBizDate;
		private String Type; //00表示起始點 01表是從中斷點
		private String BatchProcSeq; //20161122 非清算跑批-錯誤報表分析-重送
//		代理業者開關機通知使用
		private String CompanyId;
		private String Msg; 
		
		public String getApId() {	
			return ApId;
		}
		public void setApId(String apId) {
			ApId = apId;
		}
		public String getKeyID() {
			return KeyID;
		}
		public void setKeyID(String keyID) {
			KeyID = keyID;
		}
		public String getNoticeId() {
			return NoticeId;
		}
		public void setNoticeId(String noticeId) {
			NoticeId = noticeId;
		}
		public String getNoticeData() {
			return NoticeData;
		}
		public void setNoticeData(String noticeData) {
			NoticeData = noticeData;
		}
		public String getOrigStan() {
			return OrigStan;
		}
		public void setOrigStan(String origStan) {
			OrigStan = origStan;
		}
		public String getOrigTxAmt() {
			return OrigTxAmt;
		}
		public void setOrigTxAmt(String origTxAmt) {
			OrigTxAmt = origTxAmt;
		}
		public String getResult() {
			return Result;
		}
		public void setResult(String result) {
			Result = result;
		}
		public String getWebStep() {
			return WebStep;
		}
		public void setWebStep(String webStep) {
			WebStep = webStep;
		}
		public String getBizDate() {
			return BizDate;
		}
		public void setBizDate(String bizDate) {
			BizDate = bizDate;
		}
		public String getClearingPhase() {
			return ClearingPhase;
		}
		public void setClearingPhase(String clearingPhase) {
			ClearingPhase = clearingPhase;
		}
		public String getOTxDate() {
			return OTxDate;
		}
		public void setOTxDate(String oTxDate) {
			OTxDate = oTxDate;
		}
		public String getOSTAN() {
			return OSTAN;
		}
		public void setOSTAN(String oSTAN) {
			OSTAN = oSTAN;
		}
		public String getIsBizDate() {
			return IsBizDate;
		}
		public void setIsBizDate(String isBizDate) {
			IsBizDate = isBizDate;
		}
		public String getBatchSeqNo() {
			return BatchSeqNo;
		}
		public void setBatchSeqNo(String batchSeqNo) {
			BatchSeqNo = batchSeqNo;
		}
		public String getResultCode() {
			return ResultCode;
		}
		public void setResultCode(String resultCode) {
			ResultCode = resultCode;
		}
		public String getType() {
			return Type;
		}
		public void setType(String type) {
			Type = type;
		}
		public String getBatchProcSeq() {
			return BatchProcSeq;
		}
		public void setBatchProcSeq(String batchProcSeq) {
			BatchProcSeq = batchProcSeq;
		}
		public String getCompanyId() {
			return CompanyId;
		}
		public void setCompanyId(String companyId) {
			CompanyId = companyId;
		}
		public String getMsg() {
			return Msg;
		}
		public void setMsg(String msg) {
			Msg = msg;
		}
		
		
	}
	
	
	/**
	 * 單元測試
	 * @param args
	 */
	public static void main(String[] args) {
		try {
	        Header header = new Header();
	        header.setSystemHeader("eACH01");
	        header.setMsgType("0100");
	        header.setPrsCode("1300");
	        header.setStan("7385830");
	        header.setInBank("0000000");
	        header.setOutBank("9500000");
	        header.setDateTime("20141028173858");
	        header.setRspCode("0000");
	        
	        Body body = new Body();
	        body.setNoticeId("1000");
	        body.setNoticeData("input notice data here");
	        
	        Message object = new Message();
	        object.setHeader(header);
	        object.setBody(body);
	        
	        String xml = MessageConverter.marshalling(object);
	        System.out.println("==== 物件轉XML ====\n" + xml);
	        Message newMsg = MessageConverter.unmarshalling(xml);
	        System.out.println("==== XML轉物件 ====\n" + newMsg.getHeader().getSystemHeader());
		}catch(JAXBException e){
			e.printStackTrace();
		}
	}
}
