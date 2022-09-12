package tw.org.twntch.quartz;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;

import tw.org.twntch.bo.SYS_PARA_BO;
import tw.org.twntch.db.dao.hibernate.VW_ONBLOCKTAB_Dao;
import tw.org.twntch.po.VW_ONBLOCKTAB;
import tw.org.twntch.util.NumericUtil;
import tw.org.twntch.util.SpringAppCtxHelper;

public class MonitorAmount implements Job {
	private Logger logger = Logger.getLogger(getClass());
	@Override
	public void execute(JobExecutionContext content) throws JobExecutionException {
		ApplicationContext context = SpringAppCtxHelper.getApplicationContext();
		SYS_PARA_BO sys_param_bo = context.getBean(SYS_PARA_BO.class);
		Date now = new Date();
		String endTimeStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now);
		logger.debug("★★★★★★★★★★★"+ " 開始大額監控  "+ endTimeStr + "★★★★★★★★★★★");
		String amount = sys_param_bo.searchII().getMONITOR_AMOUNT();
		String period = sys_param_bo.searchII().getMONITOR_AMOUNT_PERIOD();
		String receivers = sys_param_bo.searchII().getMONITOR_MAILRECEIVER();
//		String receivers = "eeff1234@gmail.com";
		String endTime = new SimpleDateFormat("yyyyMMddHHmmss").format(now);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -Integer.parseInt(period));
		Date d2 = cal.getTime();
		String startTime = new SimpleDateFormat("yyyyMMddHHmmss").format(d2);
		String startTimeStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(d2);
		
		//test
//		startTime="20201102115400";
//		endTime="20201102120400";
		
		logger.debug("monitor startTime ( now - period ) :" + startTime);
		logger.debug("monitor endTime ( now ) :" + endTime);
		StringBuffer sql = new StringBuffer();
		sql.append("  SELECT TRANSLATE('abcd-ef-gh op:qr:st', A.TXDT, 'abcdefghopqrst') AS TXDT ");
		sql.append("  , NEWTXAMT AS TXAMT, OUTACCTNO , INACCTNO, STAN, TXDATE, NEWRESULT, UPDATEDT, BIZDATE , CLEARINGPHASE  ");
		sql.append("  , RECEIVERID, SENDERACQUIRE, OUTACQUIRE, INACQUIRE, WOACQUIRE, SENDERHEAD, OUTHEAD, INHEAD, WOHEAD ,FLBIZDATE ,RESULTSTATUS");
		sql.append("  , PCODE || '-' || COALESCE((SELECT EACH_TXN_NAME FROM EACH_TXN_CODE WHERE EACH_TXN_ID = PCODE),'') AS PCODE ");
		sql.append("  , SENDERBANKID || '-' || (SELECT COALESCE(BRBK_NAME,'') FROM BANK_BRANCH WHERE BRBK_ID = SENDERBANKID) AS SENDERBANKID ");
		sql.append("  , OUTBANKID || '-' || (SELECT COALESCE(BRBK_NAME,'') FROM BANK_BRANCH WHERE BRBK_ID = OUTBANKID) AS OUTBANKID ");
		sql.append("  , INBANKID || '-' || (SELECT COALESCE(BRBK_NAME,'') FROM BANK_BRANCH WHERE BRBK_ID = INBANKID) AS INBANKID ");
		sql.append("  , WOBANKID || '-' || (SELECT COALESCE(BRBK_NAME,'') FROM BANK_BRANCH WHERE BRBK_ID = WOBANKID) AS WOBANKID ");
		sql.append("  , (CASE WHEN RESULTSTATUS = 'P' AND NEWRESULT = 'R' THEN '0' ELSE '1' END) AS ACCTCODE ");
		sql.append("  , TXID || '-' || COALESCE((SELECT t.TXN_NAME FROM TXN_CODE T WHERE T.TXN_ID = TXID),'') AS TXID ");
		sql.append("  , SENDERID || '-' || GETCOMPANY_ABBR(SENDERID) AS SENDERID");
		sql.append("  ,(CASE RESULTSTATUS WHEN 'A' THEN '成功' WHEN 'R' THEN '失敗' ELSE (CASE SENDERSTATUS WHEN '1' THEN '處理中' ELSE '未完成' END) END) AS RESP  ");
		sql.append("  , PFCLASS || '-' || COALESCE((SELECT B.BILL_TYPE_NAME FROM BILL_TYPE B WHERE B.BILL_TYPE_ID = PFCLASS),'') AS PFCLASS, TOLLID, CHARGETYPE, BILLFLAG, COALESCE(RC1,' ') AS RC1 ,COALESCE(RC2,' ') AS RC2 ,COALESCE(RC3,' ') AS RC3 ,COALESCE(RC4,' ') AS RC4 ,COALESCE(RC5,' ') AS RC5 ,COALESCE(RC6,' ')  AS RC6");
		sql.append("  , SENDERBANK,RECEIVERBANK ");
		sql.append("  FROM VW_ONBLOCKTAB A ");
		sql.append("  WHERE NEWRESULT='A' and NEWTXAMT>=").append("'"+amount+"'").append(" and TXDT BETWEEN ").append(startTime).append(" AND ").append(endTime);
		String cols[] = {"TXDT", "PCODE", "SENDERBANKID", "OUTBANKID", "INBANKID", "WOBANKID", "OUTACCTNO", "INACCTNO","TXDATE","STAN", "TXAMT", "RESP", "SENDERID", "TXID" ,"PFCLASS", "TOLLID", "CHARGETYPE", "BILLFLAG","RC1", "RC2", "RC3","RC4","RC5","RC6", "SENDERBANK","RECEIVERBANK"};
		Map<String,String> params = new HashMap<String , String>();
		VW_ONBLOCKTAB_Dao vw_onblocktab_dao = context.getBean(VW_ONBLOCKTAB_Dao.class);
		List<VW_ONBLOCKTAB> resultList = vw_onblocktab_dao.getCSVData(sql.toString()  ,cols);
		
		if(resultList != null && resultList.size()>0) {
			if(receivers != null && receivers.isEmpty() == false){
				logger.debug("Strat Send Mail");
				 // Get system properties
				Properties properties = System.getProperties();
				// EACH
		        String from = "tch_pgm@mail2.twnch.org.tw";
//		        String host = "10.57.4.11";
		        String host = "10.57.7.11";
		        properties.put("mail.smtp.host", host);
		        properties.put("mail.smtp.port", "25");
		        //properties.put("mail.smtp.ssl.enable", "true"); if need SSL
		        properties.put("mail.smtp.auth", "false");
				
//				//gmail test 
//				String from = "*******@gmail.com";
//		        String host = "smtp.gmail.com";
//		        properties.put("mail.smtp.host", host);
//		        properties.put("mail.smtp.port", "465");
//		        properties.put("mail.smtp.ssl.enable", "true");
//		        properties.put("mail.smtp.auth", "true");
		        
////		        測試gmail用
//		        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
	//
//		            protected PasswordAuthentication getPasswordAuthentication() {
	//
//		                return new PasswordAuthentication("*******@gmail.com", "*******");
	//
//		            }
	//
//		        });
//		        EACH用
		        Session session = Session.getDefaultInstance(properties);
		        String sub = "大額監控異常列表";
		        //串mail內容
		        StringBuffer MailTextBuf=new StringBuffer();
		        MailTextBuf.append("<fonze size=5><b>大額交易監控</b></font>").append("<br>");
		        MailTextBuf.append("資料日期：").append(startTimeStr).append(" ~ ").append(endTimeStr).append("<br>");
		        MailTextBuf.append("監控條件：").append("金額大於等於").append(NumericUtil.formatNumberString(amount, 0)).append("<br>");
		        MailTextBuf.append("監控間隔：").append(period).append(" 分鐘").append("<br>");
		        MailTextBuf.append("異常列表：").append("\n");
		        MailTextBuf.append("<div align='left'><table border=1 cellSpacing=0 cellPadding=0 width=670 frame=border >").append("\n");
				MailTextBuf.append("<tr><td width='200'><center>交易日期時間</center></td><td width='200'><center>系統追蹤序號</center></td><td width='70'><center>發動行</center></td>");
				MailTextBuf.append("<td width='70'><center>接收行</center></td><td width='70'><center>交易金額</center></td></tr>");
				MailTextBuf.append("<td width='120'><center>發動者統編/簡稱</center></td>").append("\n");
				for(VW_ONBLOCKTAB eachdata:resultList) {
		        	MailTextBuf.append("<tr><td width='200'><center>").append(eachdata.getTXDT()).append("</center></td>");
		        	MailTextBuf.append("<td width='200'><center>").append(eachdata.getSTAN()).append("</center></td>");
		        	MailTextBuf.append("<td width='100'><center>").append(eachdata.getSENDERBANK()).append("</center></td>");
		        	MailTextBuf.append("<td width='100'><center>").append(eachdata.getRECEIVERBANK()).append("</center></td>");
		        	MailTextBuf.append("<td width='70'><center>").append(NumericUtil.formatNumberString(eachdata.getTXAMT(),0)).append("</center></td><tr>");
		        	MailTextBuf.append("<td width='120'><center>").append(eachdata.getSENDERID().replace("-", "<br>")).append("</center></td><tr>");
		        	MailTextBuf.append("\n");
		        }
		        MailTextBuf.append("</table></div>").append("\n");
				logger.debug(MailTextBuf.toString());
				String text = MailTextBuf.toString();
		        // Used to debug SMTP issues
		        session.setDebug(true);
				
		        try {
		            // Create a default MimeMessage object.
		            MimeMessage message = new MimeMessage(session);

		            // Set From: header field of the header.
		            message.setFrom(new InternetAddress(from));
		            
		            // Set To: header field of the header.
		            
		            message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(receivers));

		            // Set Subject: header field
		            message.setSubject(sub, "UTF-8");
		            
		            //set message body
		            Multipart mp = new MimeMultipart();
		            MimeBodyPart mbp = new MimeBodyPart();
		            
		            //設定郵件內容的型態為 text/html
		            mbp.setContent(text,"text/html;charset=UTF-8");
		            mp.addBodyPart(mbp);
		            message.setContent(mp);

		            System.out.println("sending...");
		            //set send time
		            message.setSentDate(new Date());
		            // Send message            
		            Transport.send(message);
		            System.out.println("Sent message successfully....");
		        } catch (MessagingException mex) {
		            mex.printStackTrace();
		        }
			}else{
				logger.debug("Since there are no recipients, it is not necessary to send mail. ");
			}			
		}else {
			logger.debug("DO NOT NEED TO SEND MAIL ");
		}
		
	}
}
