package tw.org.twntch.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.context.ApplicationContext;

import com.hitrust.isecurity2_0.SignerInfo;
import com.hitrust.isecurity2_0.client.CertInfo;
import com.hitrust.isecurity2_0.client.PKCS7SignatureProc;
import com.hitrust.isecurity2_0.util.Base64;

import tw.org.twntch.bo.LOGIN_BO;
import tw.org.twntch.bo.ONBLOCKTAB_BO;
import tw.org.twntch.bo.SYS_PARA_BO;
import tw.org.twntch.form.Each_User_Form;
import tw.org.twntch.form.Login_Form;
import tw.org.twntch.po.EACH_USER;
import tw.org.twntch.po.VW_ONBLOCKTAB;
import tw.org.twntch.util.BeanUtils;
import tw.org.twntch.util.CodeUtils;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.NumericUtil;
import tw.org.twntch.util.SpringAppCtxHelper;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.zDateHandler;

public class ONBLOCKTAB_COUNT_Action extends Action {
	private ONBLOCKTAB_BO onblocktab_bo;
	private CodeUtils codeUtils;

	private Logger logger = Logger.getLogger(this.getClass().getName());

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Login_Form login_form = (Login_Form) form;
		String ac_key = StrUtils.isEmpty(login_form.getAc_key()) ? "" : login_form.getAc_key();
		String target = StrUtils.isEmpty(login_form.getTarget()) ? "search" : login_form.getTarget();
		String endTimeStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		logger.debug("★★★★★★★★★★★" + " ONBLOCKTAB監控  " + endTimeStr + "★★★★★★★★★★★");
		// 取得在classpath下的properties檔案
//		Map<String, String> valueMap = SpringAppCtxHelper.getBean("onblocktab_count_param");
		Map<String, String> valueMap = codeUtils.getPropertyValue("Configuration.properties", "onblocktab_email",
				"onblocktab_qryPeriod", "onblocktab_warnLimit");

		if (valueMap != null) {
			logger.debug("email: " + valueMap.get("onblocktab_email"));
			logger.debug("查詢區間: " + valueMap.get("onblocktab_qryPeriod"));
			logger.debug("告警筆數: " + valueMap.get("onblocktab_warnLimit"));
			int dataNum = -1;
			try {
				dataNum = onblocktab_bo.getFiveMinData(valueMap.get("onblocktab_qryPeriod"));
			} catch (Exception e) {
				logger.debug("開始寫Exception的信");
				String sub = "[測試]ONBLOCKTAB資料表查詢異常";
				StringBuffer MailTextBuf = new StringBuffer();
				MailTextBuf = createExceptionMailContext(e.toString());
				createMail(valueMap.get("onblocktab_email"), sub, MailTextBuf);
			}
			logger.debug("資料筆數: " + dataNum);
			if (dataNum != -1 && dataNum <= Integer.parseInt(valueMap.get("onblocktab_warnLimit"))) {
				logger.debug("開始寫信");
				String sub = "[測試]ONBLOCKTAB資料異常告警";
				StringBuffer MailTextBuf = new StringBuffer();
				MailTextBuf = createMailContext(valueMap.get("onblocktab_qryPeriod"),
						valueMap.get("onblocktab_warnLimit"));
				createMail(valueMap.get("onblocktab_email"), sub, MailTextBuf);
			}
		}
		return mapping.findForward("index.jsp");
	}

	public void createMail(String receivers, String sub, StringBuffer MailTextBuf) {
		ApplicationContext context = SpringAppCtxHelper.getApplicationContext();
		SYS_PARA_BO sys_param_bo = context.getBean(SYS_PARA_BO.class);
		// ===== Create Header =====
		Properties properties = System.getProperties();
		String from = "tch_pgm@mail2.twnch.org.tw";
		// String host = "10.57.4.11";
		String host = "10.57.7.11";
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", "25");
		// properties.put("mail.smtp.ssl.enable", "true"); if need SSL
		properties.put("mail.smtp.auth", "false");
//		String receivers = sys_param_bo.searchII().getMONITOR_MAILRECEIVER();
//		String receivers = "eeff1234@gmail.com";

		// ===== Mail Content =====
		Session session = Session.getDefaultInstance(properties);
		// 串mail內容
//      MailTextBuf.append("監控間隔：").append(period).append(" 分鐘").append("<br>");
		logger.debug(MailTextBuf.toString());
		String text = MailTextBuf.toString();
		// Used to debug SMTP issues
		session.setDebug(true);

		// ===== Go Send =====
		try {
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from));

			// Set To: header field of the header.

			message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(receivers));

			// Set Subject: header field
			message.setSubject(sub, "UTF-8");

			// set message body
			Multipart mp = new MimeMultipart();
			MimeBodyPart mbp = new MimeBodyPart();

			// 設定郵件內容的型態為 text/html
			mbp.setContent(text, "text/html;charset=UTF-8");
			mp.addBodyPart(mbp);
			message.setContent(mp);

			System.out.println("sending...");
			// set send time
			message.setSentDate(new Date());
			// Send message
			Transport.send(message);
			System.out.println("Sent message successfully....");
		} catch (MessagingException mex) {
			mex.printStackTrace();
		}
	}

	public StringBuffer createMailContext(String qryPeriod, String warnLimit) {
		StringBuffer MailTextBuf = new StringBuffer();
		MailTextBuf.append("<fonze size=5><b>ONBLOCKTAB資料監控</b></font>").append("<br>");

		Calendar now = Calendar.getInstance();
		now.add(Calendar.MINUTE, -(Integer.parseInt(qryPeriod)));
		String endTimeStr = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
		Date tmp = now.getTime();
		String startTimeStr = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(tmp);

		MailTextBuf.append("資料日期：").append(endTimeStr).append("<br>");
		MailTextBuf.append("查詢區間：").append(startTimeStr).append(" ~ ").append(endTimeStr).append("<br>");
		MailTextBuf.append("監控條件：").append(qryPeriod + "分鐘內筆數小於" + warnLimit).append("<br>");
		return MailTextBuf;
	}

	public StringBuffer createExceptionMailContext(String e) {
		StringBuffer MailTextBuf = new StringBuffer();
		MailTextBuf.append("<fonze size=5><b>ONBLOCKTAB資料監控</b></font>").append("<br>");
		String endTimeStr = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
		MailTextBuf.append("資料日期：").append(endTimeStr).append("<br>");
		MailTextBuf.append("錯誤訊息：").append(e).append("<br>");
		return MailTextBuf;
	}

	public ONBLOCKTAB_BO getOnblocktab_bo() {
		return onblocktab_bo;
	}

	public void setOnblocktab_bo(ONBLOCKTAB_BO onblocktab_bo) {
		this.onblocktab_bo = onblocktab_bo;
	}

	public CodeUtils getCodeUtils() {
		return codeUtils;
	}

	public void setCodeUtils(CodeUtils codeUtils) {
		this.codeUtils = codeUtils;
	}
}
