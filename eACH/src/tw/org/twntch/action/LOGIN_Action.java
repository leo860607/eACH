package tw.org.twntch.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.hitrust.isecurity2_0.SignerInfo;
import com.hitrust.isecurity2_0.client.CertInfo;
import com.hitrust.isecurity2_0.client.PKCS7SignatureProc;
import com.hitrust.isecurity2_0.util.Base64;

import tw.org.twntch.bo.LOGIN_BO;
import tw.org.twntch.form.Each_User_Form;
import tw.org.twntch.form.Login_Form;
import tw.org.twntch.po.EACH_USER;
import tw.org.twntch.util.BeanUtils;
import tw.org.twntch.util.CodeUtils;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.SpringAppCtxHelper;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.zDateHandler;

public class LOGIN_Action extends Action {
	private LOGIN_BO login_bo;
	private CodeUtils codeUtils;
	private Logger logger = Logger.getLogger(this.getClass().getName());

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Login_Form login_form = (Login_Form) form;
		String ac_key = StrUtils.isEmpty(login_form.getAc_key())?"":login_form.getAc_key();
		String target = StrUtils.isEmpty(login_form.getTarget())?"search":login_form.getTarget();
		logger.debug("LOGIN_Action is start >> " + ac_key);
		login_form.setResult("");
		login_form.setMsg("");
		
		if(StrUtils.isNotEmpty(ac_key)){
			Each_User_Form userform = null;
			EACH_USER user = null;
			//使用帳號登入
			if(ac_key.equals("login")){
				Map<String,String> retmap = login_bo.loginValidate(login_form.getUserId(), ac_key);
				if(retmap.get("result").equals("FALSE")){
					login_form.setResult(retmap.get("result"));
					login_form.setMsg(retmap.get("msg")+","+retmap.get("errmsg"));
					return mapping.getInputForward();
				}else{
					//通過驗證，可以登入了
					userform = new Each_User_Form();
					user = login_bo.getUserData( login_form.getUserId());
					login(login_form,userform,user,request);
				}
			}
			
			//使用IKey登入(原版)
			if(ac_key.equals("ikeyLogin")){
				logger.debug("login_form.getRAOName()="+login_form.getRAOName());
				logger.debug("login_form.getSignvalue()="+login_form.getSignvalue());
				logger.debug("login_form.getIkeyValidateDate()="+login_form.getIkeyValidateDate());
				
				
				String subCn = login_form.getRAOName();
				String pkcs_information = login_form.getSignvalue();
				String ikeyValidateDate = login_form.getIkeyValidateDate();
				Map<String,String> retmap  = null;
				retmap = login_bo.loginValidate(login_form.getUserId(), ac_key);
//				System.out.println("retmap4321"+retmap);
				if(retmap.get("result").equals("FALSE")){
					login_form.setResult(retmap.get("result"));
					login_form.setMsg(retmap.get("msg")+","+retmap.get("errmsg"));
					return mapping.getInputForward();
				}
				//安控
//				retmap = login_bo.validateIKey(subCn,pkcs_information);
				retmap = login_bo.loginVerified(login_form.getSignvalue());
//				System.out.println("retmap1234"+retmap);
				if(retmap.get("result").equals("FALSE")){
					login_form.setResult(retmap.get("result"));
					login_form.setMsg(retmap.get("msg")+","+retmap.get("errmsg"));
					return mapping.getInputForward();
				}
				else{
					//安控
					PKCS7SignatureProc pkcs7 = new PKCS7SignatureProc();
					pkcs7.parseSignedEnvelopString(pkcs_information);
					Date certVailDate = null;
					//判斷是否要提醒使用者展期
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
					SignerInfo[] signers = pkcs7.getSigners();
					if(signers!=null && signers.length>0){
						SignerInfo signer = signers[0];
						CertInfo cert = new CertInfo(Base64.decode(signer.getSignerCert()));
						try {
							certVailDate = sdf.parse(cert.getEndDate());
							logger.debug("certVailDate="+certVailDate);
							if (certVailDate.before(new Date())) { // 有效日期小於今天	，提醒使用者展期							
								login_form.setResult("FALSE");
								//塞IKey展期的URL
								Map<String,String> parameterMap = SpringAppCtxHelper.getBean("parameters");
								login_form.setIkeyExtendURL(parameterMap.get("IKEYEXTENDURL"));
								logger.debug("parameterMap.get('IKEYEXTENDURL')="+parameterMap.get("IKEYEXTENDURL"));
							} else {
								Date today = new Date();
								long day = (certVailDate.getTime() - today.getTime()) / (24 * 60 * 60 * 1000) > 0
										? (certVailDate.getTime() - today.getTime()) / (24 * 60 * 60 * 1000)
										: (today.getTime() - certVailDate.getTime()) / (24 * 60 * 60 * 1000);
								if (day > 30) {//大於30天，不用提醒使用者展期
									login_form.setResult("TRUE");
								}
							}	

						} catch (ParseException e) {
							login_form.setResult("登入失敗：核驗憑證到期日「" + cert.getEndDate() + "」，發生 錯誤!  ");
						}
					}
					logger.debug("login_form.getResult()="+login_form.getResult());
					userform = new Each_User_Form();
					user = login_bo.getUserData( login_form.getUserId());
					//通過驗證，可以登入了
					login(login_form,userform,user,request);
				}
			}				
		//原本Ikey else
//			else{				
//				login_form.setIkeyValidateDate(ikeyValidateDate);
//				//判斷是否要提醒使用者展期
//				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");					
//				//取得憑證有效日期和目前日期的差距天數
//				long differentDays = DateTimeUtils.getDifferentDays(new Date(),sdf.parse(ikeyValidateDate));
//				logger.debug("differentDays="+differentDays);
//				//小於等於30天，提醒使用者展期
//				if(differentDays <= 30){
//					login_form.setResult("FALSE");
//					//塞IKey展期的URL
//					Map<String,String> parameterMap = SpringAppCtxHelper.getBean("parameters");
//					login_form.setIkeyExtendURL(parameterMap.get("IKEYEXTENDURL"));
//					logger.debug("parameterMap.get('IKEYEXTENDURL')="+parameterMap.get("IKEYEXTENDURL"));
//				}
//				//大於30天，不用提醒使用者展期
//				else{
//					login_form.setResult("TRUE");
//				}
//				logger.debug("login_form.getResult()="+login_form.getResult());
//				userform = new Each_User_Form();
//				user = login_bo.getUserData( login_form.getUserId());
//				//通過驗證，可以登入了
//				login(login_form,userform,user,request);
//			}
			
//			20150504 note by hugo 順序不可顛倒，否則寫log會失敗
			//登出
			if(ac_key.equals("logout")){
				login_bo.logOutSuc();
				login_form.reset(mapping, request);
			}		
		}
		return mapping.findForward(target);
	}

	// 通過驗證，可以登入了
	public void login(Login_Form login_form, Each_User_Form userform, EACH_USER user, HttpServletRequest request)
			throws Exception {
		BeanUtils.copyProperties(userform, user);
		userform.setUSER_COMPANY(user.getUSER_COMPANY());
		userform.setUSER_ID(login_form.getUserId());
		// 調整上次登入時間的格式
		userform.setLAST_LOGIN_DATE((StrUtils.isEmpty(userform.getLAST_LOGIN_DATE()) ? ""
				: userform.getLAST_LOGIN_DATE().substring(0, 19)));
		login_form.setUserData(userform);
		login_form.setIS_PROXY(login_bo.getIS_PROXY(user.getUSER_COMPANY()));
		login_form.setScaseary(login_bo.getMenuWithIsProxy(login_form.getUserId(), login_form.getIS_PROXY()));
		// 寫入最後登入日期及IP到使用者檔
		user.setLAST_LOGIN_DATE(zDateHandler.getTheDateII() + " " + zDateHandler.getTheTime());
		// 20150514 edit by hugo 因應load balance 取不到用戶端IP
		// req.getHeader("referer");
		String userIP = request.getHeader("HTTP_X_FORWARDED_FOR");
		System.out.println("HTTP_X_FORWARDED_FOR.userIP>>" + userIP);
		userIP = StrUtils.isEmpty(userIP) ? request.getHeader("X-Forwarded-For") : userIP;
		System.out.println("X-Forwarded-For.userIP>>" + userIP);
		userIP = StrUtils.isEmpty(userIP) ? request.getHeader("Remote_Addr") : userIP;
		System.out.println("getHeader(Remote_Addr).userIP>>" + userIP);
		userIP = StrUtils.isEmpty(userIP) ? request.getRemoteAddr() : userIP;
		System.out.println("getRequest().getRemoteAddr().userIP>>" + userIP);
		request.getSession().setAttribute("USERIP", userIP);
		user.setLAST_LOGIN_IP(userIP);
		// 把目前登入的時間、IP帶到網頁中
		login_form.setTHIS_LOGIN_DATE(user.getLAST_LOGIN_DATE());
		login_form.setTHIS_LOGIN_IP(user.getLAST_LOGIN_IP());
		Map<String, String> map = SpringAppCtxHelper.getBean("parameters");
		if (map != null) {
			login_form.setIsFormal(map.get("isFormal"));
		}
		login_bo.loginSuc(user);
		logger.debug("login success : (" + login_form.getUserData().getUSER_COMPANY() + " / "
				+ login_form.getUserData().getUSER_ID() + ")");
	}

	public LOGIN_BO getLogin_bo() {
		return login_bo;
	}

	public void setLogin_bo(LOGIN_BO login_bo) {
		this.login_bo = login_bo;
	}

	public CodeUtils getCodeUtils() {
		return codeUtils;
	}

	public void setCodeUtils(CodeUtils codeUtils) {
		this.codeUtils = codeUtils;
	}
}
