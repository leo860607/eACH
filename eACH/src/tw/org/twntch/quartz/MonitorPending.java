package tw.org.twntch.quartz;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
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
import tw.org.twntch.util.NumericUtil;
import tw.org.twntch.util.SpringAppCtxHelper;

public class MonitorPending implements Job {
	private Logger logger = Logger.getLogger(getClass());

	@Override
	public void execute(JobExecutionContext content) throws JobExecutionException {

		ApplicationContext context = SpringAppCtxHelper.getApplicationContext();
		SYS_PARA_BO sys_param_bo = context.getBean(SYS_PARA_BO.class);
		Date now = new Date();
		String endTimeStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now);
		logger.debug("★★★★★★★★★★★" + " 開始逾時監控  " + endTimeStr + "★★★★★★★★★★★");

		String pending = sys_param_bo.searchII().getMONITOR_PENDING();
		String period = sys_param_bo.searchII().getMONITOR_PENDING_PERIOD();
		String receivers = sys_param_bo.searchII().getMONITOR_MAILRECEIVER();

		String endTime = new SimpleDateFormat("yyyyMMddHHmmss").format(now);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -Integer.parseInt(period));
		Date d2 = cal.getTime();
		String startTime = new SimpleDateFormat("yyyyMMddHHmmss").format(d2);
		String startTimeStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(d2);

//		// test
//		startTime = "20200914111424";
//		endTime = "20200914144301";
		
		logger.debug("監控資料 起 ( now - period ) :" + startTime);
		logger.debug("監控資料 迄 ( now ) :" + endTime);
		
		StringBuffer sql = new StringBuffer();
		sql.append(" with tmp as ( ");
		sql.append(" SELECT SENDERACQUIRE,   EACHUSER.GETBKHEADNAME(SENDERACQUIRE) BKHEADNAME, COUNT(*) TIMEOUTCNT ");
		sql.append(" FROM VW_ONBLOCKTAB    ");
		sql.append(" WHERE  LENGTH( COALESCE(TIMEOUTCODE,'') )> 0 AND (  RC2='0601' OR RC4='0601') AND TXDT BETWEEN :startTime AND :endTime ");
		sql.append(" GROUP BY SENDERACQUIRE ) ");
		sql.append(" SELECT * FROM tmp WHERE tmp.TIMEOUTCNT >= :pendingcount  ORDER BY SENDERACQUIRE ");
		Map<String,String> params = new HashMap<String , String>();
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		params.put("pendingcount", pending);
		VW_ONBLOCKTAB_Dao vw_onblocktab_dao = context.getBean(VW_ONBLOCKTAB_Dao.class);
		List<Map> resultList1 = vw_onblocktab_dao.getData(sql.toString()  ,params);
		//get SENDERACQUIRE list
		List<String> banklist= new ArrayList<String>();
		for (Map eachdate:resultList1) {
			banklist.add((String) eachdate.get("SENDERACQUIRE"));
		}
		logger.debug("bankList >>"+banklist);
		
		List<Map> resultList2 =null;
		
		if(resultList1.size()>0 && null!=resultList1) {
			StringBuffer sql2 = new StringBuffer();
			sql2.append(" SELECT CAST(STAN AS VARCHAR(10)) AS STAN, TRANSLATE('abcd-ef-gh op:qr:st',TXDT, 'abcdefghopqrst') AS TXDT ,TXAMT, SENDERACQUIRE, OUTACQUIRE, INACQUIRE, SENDERHEAD, OUTHEAD, INHEAD, RESULTSTATUS, TIMEOUTCODE, SENDERSTATUS, "
					+ "COALESCE(RC1,'') AS RC1 ,COALESCE(RC2,'') AS RC2 ,COALESCE(RC3,'') AS RC3 ,COALESCE(RC4,'') AS RC4 ,CAST(SENDERBANK AS VARCHAR(7)) AS SENDERBANK, CAST(RECEIVERBANK AS VARCHAR(7)) AS RECEIVERBANK");
			sql2.append(" FROM VW_ONBLOCKTAB  ");
			sql2.append(" WHERE  LENGTH( COALESCE(TIMEOUTCODE,'') )> 0  AND (  RC2='0601' OR RC4='0601') ");
			sql2.append(" AND TXDT BETWEEN :startTime AND :endTime ");
			sql2.append(" AND SENDERACQUIRE IN (:banklist) ");
			sql2.append(" ORDER BY SENDERACQUIRE ");
			Map<String,String> params2 = new HashMap<String , String>();
			params2.put("startTime", startTime);
			params2.put("endTime", endTime);
			resultList2 = vw_onblocktab_dao.getDataWithIn(sql2.toString(),params2,"banklist",banklist);
			logger.debug("resultList2 >>"+ resultList2 );
		}
		
		if(resultList1 != null && resultList1.size()>0 && resultList2 != null && resultList2.size()>0 ) {
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
		        
//		       	 測試gmail用
//		        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
	//
//		            protected PasswordAuthentication getPasswordAuthentication() {
	//
//		                return new PasswordAuthentication("*******@gmail.com", "********");
	//
//		            }
	//
//		        });
//		        EACH用
		        Session session = Session.getDefaultInstance(properties);
		        String sub = "逾時監控異常列表";
		      //串mail內容
		        StringBuffer MailTextBuf=new StringBuffer();
		        MailTextBuf.append("<fonze size=5><b>逾時交易監控</b></font>").append("<br>");
		        MailTextBuf.append("資料日期：").append(startTimeStr).append(" ~ ").append(endTimeStr).append("<br>");
		        MailTextBuf.append("監控條件：").append("逾時筆數大於等於").append(pending).append("<br>");
		        MailTextBuf.append("監控間隔：").append(period).append(" 分鐘").append("<br>");
		        MailTextBuf.append("異常列表：").append("\n");
		        MailTextBuf.append("<div align='left'><table border=1 cellSpacing=0 cellPadding=0 width=870 frame=border >").append("\n");
				MailTextBuf.append("<tr>");
				MailTextBuf.append("<td width='70'><center>發動行</center></td>");
				MailTextBuf.append("<td width='80'><center>逾時筆數</center></td>");
				MailTextBuf.append("<td width='150'><center>交易日期時間</center></td>");
				MailTextBuf.append("<td width='150'><center>系統追蹤序號</center></td>");
				MailTextBuf.append("<td width='70'><center>發動行</center></td>");
				MailTextBuf.append("<td width='70'><center>接收行</center></td>");
				MailTextBuf.append("<td width='80'><center>交易金額</center></td>");
				MailTextBuf.append("<td width='50'><center>RC1</center></td>");
				MailTextBuf.append("<td width='50'><center>RC2</center></td>");
				MailTextBuf.append("<td width='50'><center>RC3</center></td>");
				MailTextBuf.append("<td width='50'><center>RC4</center></td>");
				MailTextBuf.append("</tr>");
				MailTextBuf.append("\n");
				int list1count=0;
				String senderbanktmp="";
				for(int i = 0; i<resultList2.size(); i ++) {
					MailTextBuf.append("<tr>");
					if("".equals(senderbanktmp)){
						MailTextBuf.append("<td width='70' rowspan='"+resultList1.get(list1count).get("TIMEOUTCNT")+"'><center>"+resultList1.get(list1count).get("SENDERACQUIRE")+"</center></td>");
						MailTextBuf.append("<td width='80' rowspan='"+resultList1.get(list1count).get("TIMEOUTCNT")+"'><center>"+resultList1.get(list1count).get("TIMEOUTCNT")+"筆</center></td>");
						senderbanktmp = (String) resultList2.get(i).get("SENDERBANK");
					}
					if(!senderbanktmp.equals((String) resultList2.get(i).get("SENDERBANK"))) {
						list1count++;
						senderbanktmp="";
						i--;
						continue;
					}
					MailTextBuf.append("<td width='150'><center>"+resultList2.get(i).get("TXDT")+"</center></td>");
					MailTextBuf.append("<td width='150'><center>"+resultList2.get(i).get("STAN")+"</center></td>");
					MailTextBuf.append("<td width='70'><center>"+resultList2.get(i).get("SENDERBANK")+"</center></td>");
					MailTextBuf.append("<td width='70'><center>"+resultList2.get(i).get("RECEIVERBANK")+"</center></td>");
					MailTextBuf.append("<td width='80'><center>"+NumericUtil.formatNumberString((String) resultList2.get(i).get("TXAMT"), 0)+"</center></td>");
					MailTextBuf.append("<td width='50'><center>"+resultList2.get(i).get("RC1")+"</center></td>");
					MailTextBuf.append("<td width='50'><center>"+resultList2.get(i).get("RC2")+"</center></td>");
					MailTextBuf.append("<td width='50'><center>"+resultList2.get(i).get("RC3")+"</center></td>");
					MailTextBuf.append("<td width='50'><center>"+resultList2.get(i).get("RC4")+"</center></td>");
					MailTextBuf.append("</tr>");
				}
				MailTextBuf.append("\n");
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
