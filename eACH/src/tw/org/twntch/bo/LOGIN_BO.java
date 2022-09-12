package tw.org.twntch.bo;

import java.io.IOException;
import java.net.InetAddress;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.hitrust.isecurity2_0.ISecurityException;
import com.hitrust.isecurity2_0.SignerInfo;
import com.hitrust.isecurity2_0.client.PKCS7SignatureProc;

import tw.org.twntch.db.dao.hibernate.BANK_GROUP_Dao;
import tw.org.twntch.db.dao.hibernate.EACH_FUNC_LIST_Dao;
import tw.org.twntch.db.dao.hibernate.EACH_ROLE_FUNC_Dao;
import tw.org.twntch.db.dao.hibernate.EACH_USER_Dao;
import tw.org.twntch.db.dao.hibernate.SYS_PARA_Dao;
import tw.org.twntch.db.dataaccess.ExecuteSQL;
import tw.org.twntch.po.AGENT_PROFILE;
import tw.org.twntch.po.BANK_GROUP;
import tw.org.twntch.po.EACH_FUNC_LIST;
import tw.org.twntch.po.EACH_USER;
import tw.org.twntch.po.EACH_USERLOG;
import tw.org.twntch.po.EACH_USERLOG_PK;
import tw.org.twntch.po.EACH_USER_PK;
import tw.org.twntch.po.SYS_PARA;
import tw.org.twntch.util.BeanUtils;
import tw.org.twntch.util.CodeUtils;
import tw.org.twntch.util.IPUtil;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.PKCS7Util;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.WebServletUtils;
import tw.org.twntch.util.zDateHandler;

public class LOGIN_BO {
	private BANK_GROUP_Dao bank_group_Dao;
	private EACH_USER_Dao each_user_Dao;
	private EACH_ROLE_FUNC_Dao role_func_Dao;
	private EACH_FUNC_LIST_Dao func_list_Dao;
	private CodeUtils codeUtils;
	private SYS_PARA_Dao sys_para_Dao ;
	private EACH_USERLOG_BO userlog_bo;
	private AGENT_PROFILE_BO agent_profile_bo ; 
	private Logger logger = Logger.getLogger(this.getClass().getName());
	/**
	 * 
	 * @return
	 */
	public Map loginSuc(EACH_USER po){
		Map<String,String> retmap = new HashMap<String,String>() ;
		try {
			HttpServletRequest request = WebServletUtils.getRequest();
			int timeout = po.getIDLE_TIMEOUT()==null ? 0 :po.getIDLE_TIMEOUT() ;
			int systimeout = 30;
			List<SYS_PARA> list = sys_para_Dao.getTopOne();
			if(list != null && list.size()!=0 ){
				for(SYS_PARA syspo :list){
					systimeout = syspo.getTIMEOUT_TIME() == 0 ?systimeout :syspo.getTIMEOUT_TIME();
				}
			}
			timeout = timeout ==0 ? systimeout :timeout;
			retmap = BeanUtils.describe(po);
//			根據資料庫設定設定timeout
			setSessionTimeoout(timeout);
			request.getSession().setAttribute("S.USER.ID", po.getUSER_ID());
			String serverIP = request.getLocalAddr();
			each_user_Dao.aop_save(po);
			writeLog("I" , serverIP);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retmap;
	}
	public Map logOutSuc(){
		Map<String,String> retmap = new HashMap<String,String>() ;
		String userID = "";
		try {
			HttpServletRequest request = WebServletUtils.getRequest();
			String serverIP = request.getLocalAddr();
			userID = (String) request.getSession().getAttribute("S.USER.ID");
			request.getSession().setAttribute("S.USER.ID", null);
//			each_user_Dao.aop_save(po);
//			writeLog("J" ,serverIP );
			writeLogOutLog("J", serverIP, userID);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retmap;
	}
	
	public void writeLog(String str , String serverIP){
		String tmpStr = "";
		System.out.println("str>>"+str);
		try {
			EACH_USERLOG po = userlog_bo.getEACH_USERLOG(str);
			if(StrUtils.isNotEmpty(str) && str.equals("I")){
				tmpStr = "登入成功";
				po.setFUNC_ID("eACH9999");
			}else if(StrUtils.isNotEmpty(str) && str.equals("J")){
				tmpStr = "登出成功";
				po.setFUNC_ID("eACH9998");
			}
			
			po.setFUNC_TYPE("2");
			po.setADEXCODE(tmpStr+"，ServerIP="+serverIP);
			userlog_bo.save(po);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	/**
	 * 因logout時 loginfrom 已被初始化拿不到userData所以另外處理
	 * @param str
	 * @param serverIP
	 * @param userID
	 */
	public void writeLogOutLog(String str , String serverIP ,String userID){
		String tmpStr = "";
		System.out.println("str>>"+str);
		try {
//			EACH_USERLOG po = userlog_bo.getEACH_USERLOG(str);
			UUID uuid = UUID.randomUUID();
			String[] tmp = userID.split("-");
			EACH_USERLOG_PK id = new EACH_USERLOG_PK(uuid.toString(), tmp[0] ,  userID);
			EACH_USERLOG po = new EACH_USERLOG();
//			20150514 edit by hugo 因應load balance 取不到用戶端IP
//			req.getHeader("referer");
			String userIP = WebServletUtils.getRequest().getHeader("HTTP_X_FORWARDED_FOR");
			System.out.println("HTTP_X_FORWARDED_FOR.userIP>>"+userIP);
			userIP = StrUtils.isEmpty(userIP)?WebServletUtils.getRequest().getHeader("X-Forwarded-For"):userIP;
			System.out.println("X-Forwarded-For.userIP>>"+userIP);
			userIP = StrUtils.isEmpty(userIP)?WebServletUtils.getRequest().getHeader("Remote_Addr"):userIP;
			System.out.println("getHeader(Remote_Addr).userIP>>"+userIP);
			userIP = StrUtils.isEmpty(userIP)?WebServletUtils.getRequest().getRemoteAddr():userIP;
			System.out.println("getRequest().getRemoteAddr().userIP>>"+userIP);
			po.setUSERIP(userIP);
			po.setTXTIME(zDateHandler.getTheDateII()+" "+zDateHandler.getTheTime_MS());
			po.setOPITEM(str);
			po.setId(id);
			tmpStr = "登出成功";
			po.setFUNC_ID("eACH9998");
			po.setFUNC_TYPE("2");
			po.setADEXCODE(tmpStr+"，ServerIP="+serverIP);
			userlog_bo.save(po);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	/**
	 * aop 攔截用
	 * @return
	 */
	public Map loginFail(){
		Map<String,String> retmap = new HashMap<String,String>() ;
		return retmap;
	}
	/**
	 * 根據資料庫設定設定timeout
	 */
	public void setSessionTimeoout(int time){
		WebServletUtils.getRequest().getSession().setMaxInactiveInterval(time*60);
	}
	
	public Map loginValidate(String user_id , String ac_key){
		System.out.println("user_id+ac_key+1234"+user_id+"ac"+ac_key);
		Map<String,String> retmap = new HashMap<String,String>() ;
		HttpServletRequest req =  WebServletUtils.getRequest();
		retmap.put("result","FALSE");
		EACH_USER po = getUserData(user_id);
		if(po == null){
			retmap.put("msg","登入失敗");
			retmap.put("errmsg","查無使用者資料，或已遭停用。");
			return retmap;
		}
//		驗證用戶所屬單位
		retmap = validatecomId(po.getUSER_COMPANY() , po.getUSER_TYPE());
		if(retmap.get("result").equals("FALSE")){return retmap;}
//		先註解 以後不會有一般登入
//		if(StrUtils.isNotEmpty(po.getUSE_IKEY()) && po.getUSE_IKEY().equals("Y") && !ac_key.equals("ikeyLogin")){
//			retmap.put("msg","登入失敗");
//			retmap.put("errmsg","必須使用Ikey登入");
//			return retmap;
//		}
//		IP驗證   add by hugo  req by 李建利說改'網段或ip'
		String ipstr = po.getIP();
//		20150514 edit by hugo 因應load balance 取不到用戶端IP
//		req.getHeader("referer");
		String userIP = WebServletUtils.getRequest().getHeader("HTTP_X_FORWARDED_FOR");
		System.out.println("HTTP_X_FORWARDED_FOR.userIP>>"+userIP);
		userIP = StrUtils.isEmpty(userIP)?WebServletUtils.getRequest().getHeader("X-Forwarded-For"):userIP;
		System.out.println("X-Forwarded-For.userIP>>"+userIP);
		userIP = StrUtils.isEmpty(userIP)?WebServletUtils.getRequest().getHeader("Remote_Addr"):userIP;
		System.out.println("getHeader(Remote_Addr).userIP>>"+userIP);
		userIP = StrUtils.isEmpty(userIP)?WebServletUtils.getRequest().getRemoteAddr():userIP;
		System.out.println("getRequest().getRemoteAddr().userIP>>"+userIP);
		retmap = validateIp( ipstr, userIP);
		if(retmap.get("result").equals("FALSE")){return retmap;}
		
//		有效日期驗證
		String loginDate = StrUtils.isEmpty(po.getLAST_LOGIN_DATE()) ?po.getCDATE():po.getLAST_LOGIN_DATE() ;
		int idleday  = po.getNOLOGIN_EXPIRE_DAY() == null ?0:po.getNOLOGIN_EXPIRE_DAY() ;
		retmap = validateIdleDay(loginDate , idleday);
		if(retmap.get("result").equals("FALSE")){return retmap;}
		
		
		
		
//		最後才驗證Ikey  ac_key.equals("ikeyLogin") 的判斷以後可能會拿掉 ，或判斷ac_key != ikeyLogin 但USE_IKEY()=Y 
		if(StrUtils.isNotEmpty(po.getUSE_IKEY()) && po.getUSE_IKEY().equals("Y") && ac_key.equals("ikeyLogin")){
	//		retmap = validateIKey();
	//		if(retmap.get("result").equals("FALSE")){return retmap;}
		}
		return retmap;
	}
	
	/**
	 * 
	 * @param tableip
	 * @param ip
	 * @return
	 */
	public Map validateIp(String tableip , String ip){
		Map<String,String> retmap = new HashMap<String,String>() ;
		logger.debug("tableip>>"+tableip+" , ip>>"+ip);
		if(StrUtils.isEmpty(tableip)){retmap.put("result","TRUE");return retmap;}
		String[] ips =  tableip.split(";");
		logger.debug("ips>>"+ips);
		List<String> iplist = Arrays.asList(ips);
		retmap.put("result","FALSE");
		boolean isIp = false;
		try {
		if(iplist.size()==0){retmap.put("result","TRUE");return retmap;}
		
		for(String str1 :iplist){
			if(tableip.contains(ip)){
				logger.debug(" ip 合法");
				retmap.put("result","TRUE");
				return retmap;
			}
			if(str1.indexOf("-") != -1){
				String[] ipary =  str1.split("-");
				long ipLo  = IPUtil.ipToLong(InetAddress.getByName(ipary[0]));
				long ipHi  = IPUtil.ipToLong(InetAddress.getByName(ipary[1]));
				long userip  = IPUtil.ipToLong(InetAddress.getByName(ip));
				if(userip >= ipLo && userip <= ipHi){
					logger.debug(" ip 在網段內");
					retmap.put("result","TRUE");
					return retmap;
				}
			}
		}
		
		
		if(!isIp){
			retmap.put("msg","登入失敗");
			retmap.put("errmsg","ip驗證未通過");
			return retmap;
		}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			retmap.put("msg","登入失敗");
			retmap.put("errmsg","ip驗證異常");
		}
		return retmap;
	}
	/**
	 * 檢查用戶所屬單位是否存在或啟用及停用
	 * @param company
	 * @return
	 */
	public Map validatecomId(String comId , String type){
		Map<String,String> retmap = new HashMap<String,String>();
		retmap.put("result", "FALSE");
		List<BANK_GROUP> list = null ; 
		List<AGENT_PROFILE> agent_list = null ; 
		System.out.println("type>>"+type);
		if(type.equals("C")){
			agent_list = agent_profile_bo.getAgent_profile_Dao().find("  FROM tw.org.twntch.po.AGENT_PROFILE WHERE COMPANY_ID = ? ", comId);
		}else{
			list= bank_group_Dao.getBgbkByComId(comId);
		}
		System.out.println("list>"+list);
		System.out.println("agent_list>"+agent_list);
		if((list !=null && list.size() !=0 ) || (agent_list !=null && agent_list.size() !=0 )){
			retmap.put("result", "TRUE");
		}else{
			retmap.put("msg", "登入失敗");
			retmap.put("errmsg", "所屬單位不存在或未啟用或已停用");
		}
		System.out.println("retmap>>"+retmap);
		return retmap;
	}
	
	/**
	 * 未完成
	 * @return
	 */
	public Map validateIdleDay(String tabledate , int idleday){
		Map<String,String> retmap = new HashMap<String,String>() ;
		if(idleday==0){
			retmap.put("result","TRUE");
			return retmap;
		}
		if(StrUtils.isEmpty(tabledate)){
//			因前面已有做判斷，到此關卡還是空的就判定失敗
			retmap.put("result","FALSE");
			retmap.put("msg","登入失敗");
			retmap.put("errmsg","超過有效天數");
			return retmap;
		}
		int day = zDateHandler.getDiffTimeStamp(tabledate, zDateHandler.getTheDateII()+" "+zDateHandler.getTheTime());
		logger.debug("day>>"+day);
		retmap.put("result","FALSE");
		
//		if(StrUtils.isEmpty(tabledate)){
//			retmap.put("result","TRUE");
//		}
		if(day > idleday){
			retmap.put("msg","登入失敗");
			retmap.put("errmsg","超過有效天數");
			return retmap;
		}else{
			retmap.put("result","TRUE");
			return retmap;
		}
	}
	/**
	 * @param RAOName
	 * @param signvalue
	 * @return Map
	 */
//	public Map<String,String> validateIKey(String RAOName ,String signvalue){
//		Map<String,String> retmap = new HashMap<String,String>();
//		retmap.put("result","FALSE");
//		try{
//			PKCS7Util pkcs7Util = new PKCS7Util();
//			//簽章驗證失敗
//			if(!pkcs7Util.pkcs7Verify(signvalue)){
//				retmap.put("msg","登入失敗");
//				retmap.put("errmsg","簽章驗證失敗");
//				return retmap;
//			}
//			//通過
//			else{
//				retmap.put("result","TRUE");
//			}
//		}
//		catch(Exception e){
//			e.printStackTrace();
//			retmap.put("msg","登入失敗");
//			retmap.put("errmsg","簽章驗證過程出現問題");
//		}
//		return retmap;
//	}
	/**
	 * @param RAOName
	 * @param signvalue
	 * @return Map
	 */
	public Map<String,String> validateIKey(String RAOName ,String signvalue){
		Map<String,String> retmap = new HashMap<String,String>();
		retmap.put("result","FALSE");
		try{
			PKCS7Util pkcs7Util = new PKCS7Util();
			//簽章驗證失敗
			if(!pkcs7Util.pkcs7Verify(signvalue)){
				retmap.put("msg","登入失敗");
				retmap.put("errmsg","簽章驗證失敗");
				return retmap;
			}
			//通過
			else{
				retmap.put("result","TRUE");
			}
		}
		catch(Exception e){
			e.printStackTrace();
			retmap.put("msg","登入失敗");
			retmap.put("errmsg","簽章驗證過程出現問題");
		}
		return retmap;
	}
	
	
	
	
	
	public String getIS_PROXY(String userCompany){
		String isProxy = "N";
		List<BANK_GROUP> proxyList = bank_group_Dao.getProxyClean_BankList();
		if(proxyList != null){
			for(int i = 0; i < proxyList.size(); i++){
				if(userCompany.trim().equals(proxyList.get(i).getCTBK_ID())){
					isProxy = "Y";
					break;
				}
			}
		}
		return isProxy;
	}
	
	public EACH_USER getUserData(String userCompany, String userId){
		EACH_USER_PK id = new EACH_USER_PK(userId, userCompany);
		EACH_USER po = each_user_Dao.get(id);
		return po;
	}
	public EACH_USER getUserData( String userId){
//		EACH_USER po = each_user_Dao.get(userId);
		EACH_USER po = each_user_Dao.getUserData(userId);
		return po;
	}
	
	public List getMenu(String userCompany, String userId){
		List<EACH_FUNC_LIST> menuList = null;
		List<EACH_FUNC_LIST> subList = null;
		Map menuItem = null;
		Map subItem = null;
		List menuList_N = new ArrayList();
		List subList_N = new ArrayList();
		try {
			if(StrUtils.isNotEmpty(userCompany) && StrUtils.isNotEmpty(userId)){
				//找出該使用者的所屬群組、用戶類型
				EACH_USER user_po = each_user_Dao.get(new EACH_USER_PK(userId, userCompany));
				String roleId = "", userType = "";
				if(user_po != null){
					roleId = user_po.getROLE_ID();
					userType = user_po.getUSER_TYPE();
					if(StrUtils.isNotEmpty(roleId) && StrUtils.isNotEmpty(userType)){
						//依使用者之群組、類型找出可用的FUNC_ID清單(作業模組)
						menuList = role_func_Dao.getFuncListByRoleId(roleId, userType);
					}
				}
				
				if(menuList != null){
					menuList_N = new ArrayList();
					for(int i = 0; i < menuList.size(); i++){
						menuItem = new HashMap();
						menuItem.put("FUNC_NAME", menuList.get(i).getFUNC_NAME());
						menuItem.put("FUNC_URL", menuList.get(i).getFUNC_URL());
						menuItem.put("FUNC_TYPE", menuList.get(i).getFUNC_TYPE());
						menuItem.put("FUNC_ID", menuList.get(i).getFUNC_ID());
						//依使用者之群組、類型找出某個FUNC_ID可用的子清單(功能項目)
						subList = func_list_Dao.getNextActiveSubItemByType(menuList.get(i).getFUNC_ID(), userType, roleId);
						if(subList != null){
							subList_N = new ArrayList();
							for(int j = 0; j < subList.size(); j++){
								subItem = new HashMap();
								subItem.put("FUNC_NAME", subList.get(j).getFUNC_NAME());
								subItem.put("FUNC_URL", subList.get(j).getFUNC_URL());
								subItem.put("FUNC_TYPE", subList.get(j).getFUNC_TYPE());
								subItem.put("FUNC_ID", subList.get(j).getFUNC_ID());
								subList_N.add(subItem);
							}
							menuItem.put("SUB_LIST", subList_N);
						}
						menuList_N.add(menuItem);
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		logger.debug("menu>>" + menuList_N);
		return menuList_N;
	}
	
	
	/**
	 * 變更pk後用的API
	 * @param userId
	 * @return
	 */
	public List getMenu( String userId){
		List<EACH_FUNC_LIST> menuList = null;
		List<EACH_FUNC_LIST> subList = null;
		Map menuItem = null;
		Map subItem = null;
		List menuList_N = new ArrayList();
		List subList_N = new ArrayList();
		try {
			if( StrUtils.isNotEmpty(userId)){
				//找出該使用者的所屬群組、用戶類型
				EACH_USER user_po = each_user_Dao.get(userId);
				String roleId = "", userType = "";
				if(user_po != null){
					roleId = user_po.getROLE_ID();
					userType = user_po.getUSER_TYPE();
					if(StrUtils.isNotEmpty(roleId) && StrUtils.isNotEmpty(userType)){
						//依使用者之群組、類型找出可用的FUNC_ID清單(作業模組)
						menuList = role_func_Dao.getFuncListByRoleId(roleId, userType);
					}
					
				}
				
				if(menuList != null){
					menuList_N = new ArrayList();
					for(int i = 0; i < menuList.size(); i++){
						menuItem = new HashMap();
						menuItem.put("FUNC_NAME", menuList.get(i).getFUNC_NAME());
						menuItem.put("FUNC_URL", menuList.get(i).getFUNC_URL());
						menuItem.put("FUNC_TYPE", menuList.get(i).getFUNC_TYPE());
						menuItem.put("FUNC_ID", menuList.get(i).getFUNC_ID());
						//依使用者之群組、類型找出某個FUNC_ID可用的子清單(功能項目)
						subList = func_list_Dao.getNextActiveSubItemByType(menuList.get(i).getFUNC_ID(), userType, roleId);
						if(subList != null){
							subList_N = new ArrayList();
							for(int j = 0; j < subList.size(); j++){
								subItem = new HashMap();
								subItem.put("FUNC_NAME", subList.get(j).getFUNC_NAME());
								subItem.put("FUNC_URL", subList.get(j).getFUNC_URL());
								subItem.put("FUNC_TYPE", subList.get(j).getFUNC_TYPE());
								subItem.put("FUNC_ID", subList.get(j).getFUNC_ID());
								subList_N.add(subItem);
							}
							menuItem.put("SUB_LIST", subList_N);
						}
						menuList_N.add(menuItem);
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		logger.debug("menu>>" + menuList_N);
		return menuList_N;
	}
	
	/**
	 * 增加判斷是否為代理清算行
	 * @param userId
	 * @return
	 */
	public List getMenuWithIsProxy(String userId, String isProxy  ){
		List<EACH_FUNC_LIST> menuList = null;
		List<EACH_FUNC_LIST> subList = null;
		Map menuItem = null;
		Map subItem = null;
		List menuList_N = new ArrayList();
		List subList_N = new ArrayList();
		boolean isBaseData = false;
		String tmpfuncname1 = "";
		String tmpfuncname2 = "";
		try {
			if( StrUtils.isNotEmpty(userId)){
				//找出該使用者的所屬群組、用戶類型
				EACH_USER user_po = each_user_Dao.get(userId);
				String roleId = "", userType = "";
				System.out.println("user_po>>"+user_po);
				if(user_po != null){
					roleId = user_po.getROLE_ID();
					userType = user_po.getUSER_TYPE();
					System.out.println("roleId>>"+roleId);
					System.out.println("userType>>"+userType);
					if(StrUtils.isNotEmpty(roleId) && StrUtils.isNotEmpty(userType)){
						//依使用者之群組、類型找出可用的FUNC_ID清單(作業模組)
						menuList = role_func_Dao.getFuncListByRoleId(roleId, userType);
					}
					System.out.println("menuList>>"+menuList);
				}
				
				if(menuList != null){
					menuList_N = new ArrayList();
					for(int i = 0; i < menuList.size(); i++){
//						20150401 eidt by hugo 要先判斷user_type 是銀行端或發動者端 才過濾 票交無此限制
						//2015030 HUANGPU 只有在非代理清算行角色登入時才需過濾掉代理清算行專屬功能
//						if(!(isProxy.equalsIgnoreCase("N") && menuList.get(i).getPROXY_FUNC().equalsIgnoreCase("Y"))){
						if(!((userType.equals("B") || userType.equals("C")  ) && isProxy.equalsIgnoreCase("N") && menuList.get(i).getPROXY_FUNC().equalsIgnoreCase("Y"))){
							menuItem = new HashMap();
							if(((userType.equals("B") || userType.equals("C")   )) ){
								tmpfuncname1 = StrUtils.isEmpty(menuList.get(i).getFUNC_NAME_BK()) ?menuList.get(i).getFUNC_NAME() : menuList.get(i).getFUNC_NAME_BK();
							}else{
								tmpfuncname1 = menuList.get(i).getFUNC_NAME() ;
							}
							menuItem.put("FUNC_NAME", tmpfuncname1);
//							menuItem.put("FUNC_NAME", menuList.get(i).getFUNC_NAME());
							menuItem.put("FUNC_URL", menuList.get(i).getFUNC_URL());
							menuItem.put("FUNC_TYPE", menuList.get(i).getFUNC_TYPE());
							menuItem.put("FUNC_ID", menuList.get(i).getFUNC_ID());
							//依使用者之群組、類型找出某個FUNC_ID可用的子清單(功能項目)
							subList = func_list_Dao.getNextActiveSubItemByType(menuList.get(i).getFUNC_ID(), userType, roleId);
							if(subList != null){
								subList_N = new ArrayList();
								for(int j = 0; j < subList.size(); j++){
//						20150401 eidt by hugo 要先判斷user_type 是銀行端或發動者端 才過濾 票交無此限制
									//2015030 HUANGPU 只有在非代理清算行角色登入時才需過濾掉代理清算行專屬功能
//									if(!(isProxy.equalsIgnoreCase("N") && subList.get(j).getPROXY_FUNC().equalsIgnoreCase("Y"))){
									if(!((userType.equals("B") || userType.equals("C") ) && isProxy.equalsIgnoreCase("N") && subList.get(j).getPROXY_FUNC().equalsIgnoreCase("Y"))){
										subItem = new HashMap();
										if(((userType.equals("B") || userType.equals("C")   )) ){
											tmpfuncname2 = StrUtils.isEmpty(subList.get(j).getFUNC_NAME_BK()) ?subList.get(j).getFUNC_NAME() : subList.get(j).getFUNC_NAME_BK();
											subItem.put("FUNC_NAME", tmpfuncname2);
//											subItem.put("FUNC_NAME", subList.get(j).getFUNC_NAME()+"-查詢");
//											tmpfuncname = subList.get(j).getFUNC_NAME().replace("管理", "查詢").replace("設定", "查詢").replace("維護", "查詢") ;
//											subItem.put("FUNC_NAME", tmpfuncname);
										}else{
											tmpfuncname2 = subList.get(j).getFUNC_NAME() ;
											subItem.put("FUNC_NAME", tmpfuncname2);
										}
										subItem.put("FUNC_URL", subList.get(j).getFUNC_URL());
										subItem.put("FUNC_TYPE", subList.get(j).getFUNC_TYPE());
										subItem.put("FUNC_ID", subList.get(j).getFUNC_ID());
										subList_N.add(subItem);
									}
								}
								menuItem.put("SUB_LIST", subList_N);
							}
							menuList_N.add(menuItem);
						}
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		logger.debug("getMenuWithIsProxy.menu>>" + menuList_N);
		return menuList_N;
	}
	
	/**
	 * TODO 測試用 手動執行報表批次作業
	 * @param param
	 * @return
	 */
	public String executeBatch(Map<String, String> param){
		String result = "";
		Map map = new HashMap();
		map.put("msg", "無法執行!");
		ExecuteSQL es = new ExecuteSQL("jdbc/ACH");
		String sql = "{call EACHUSER.BAT_RPONBLOCKTAB_M()}";
		String sql2 = "{call EACHUSER.BAT_RPONCLEARINGTAB_M()}";
		String sql3 = "{call EACHUSER.BAT_RPCLEARFEETAB_M()}";
//		String sql = "{call EACHUSER.BAT_RPONBLOCKTAB()}";
//		String sql2 = "{call EACHUSER.BAT_RPONCLEARINGTAB()}";
//		String sql3 = "{call EACHUSER.BAT_RPCLEARFEETAB()}";
		try {
			es.doSP(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			map.put("msg", "RPONBLOCKTAB 執行失敗!");
			return JSONUtils.map2json(map);
		}
		try {
			es.doSP(sql2);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			map.put("msg", "BAT_RPONCLEARINGTAB 執行失敗!");
			return JSONUtils.map2json(map);
		}
		map.put("msg", "執行成功!");
		
		try {
			es.doSP(sql3);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			map.put("msg", "BAT_RPCLEARFEETAB 執行失敗!");
			return JSONUtils.map2json(map);
		}
		map.put("msg", "執行成功!");
		return JSONUtils.map2json(map); 
	}
//	public String executeBatch(Map<String, String> param){
//		String result = "";
//		Map map = new HashMap();
//		map.put("msg", "無法執行!");
//		ExecuteSQL es = new ExecuteSQL("jdbc/ACH");
//		String sql = "{call EACHUSER.BAT_RPONBLOCKTABII()}";
//		try {
//			es.doSP(sql);
//			map.put("msg", "執行成功!");
//		} catch (DataAccessException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return JSONUtils.map2json(map);
//	}
	
	//用IKey登入要先取得SNO
	public String getSNO(Map<String,String> param){
		//自憑證讀取用戶代號
		String subCn = param.get("RAOName");
		logger.debug("subCn="+subCn);
		//驗證IKey
		Map<String,String> resultMap = new CodeUtils().iKeyVerification(subCn);
		//回傳到頁面的json字串
		String gson = new Gson().toJson(resultMap);
		System.out.println("gson:"+gson);
		logger.debug("gson:"+gson);
		return gson;
	}
	
	//20150408 HUANGPU for user session reset
	public void awakeSession(Map<String,String> param){
		String sessionId = WebServletUtils.getRequest().getSession().getId();
		logger.debug("Session : " + sessionId + " is awake!");
	}
	
    public Map<String,String> loginVerified(String signValue) throws IOException, ISecurityException {
//    	System.out.println("<loginVerified>1234"+signValue);
    	Map<String,String> retmap = new HashMap<String,String>();
		retmap.put("result","FALSE");
    	try {
			PKCS7SignatureProc pkcs7 = new PKCS7SignatureProc();
			pkcs7.parseSignedEnvelopString(signValue);
			
			//解簽 得到明文
//			String msg = pkcs7.getDataContentString();
//			System.out.println("Message = " + msg);
			
			// Get signers
//			log.debug("Get Signers()...");
			SignerInfo[] signers = pkcs7.getSigners();
			
			// Verify each signer
//			System.out.println("Verify Signers...");
			for(int i=0; i < signers.length; i++) {
				SignerInfo signer = signers[i];
				// show signer message
//				System.out.println("Signer #" + (i+1));
//				System.out.println("**** info ****");
//				System.out.println("1. DigestMethod:" + signer.getDigestMethod());
//				System.out.println("2. SignatureMethod:" + signer.getSignatureMethod());
//				CertInfo cert = new CertInfo(Base64.decode(signer.getSignerCert()));
//				System.out.println("3. Issuer:" + cert.getIssuerName().getX500NameString());
//				System.out.println("4. SerialNo:" + cert.getSerialNumber());
//				System.out.println("5. Subject:" + cert.getSubjectName().getX500NameString());
//				System.out.println("6. CN:" + cert.getSubjectName().commonName);
//				System.out.println("**************");
				
				//verify
				pkcs7.verify(signer);
				System.out.println("PKCS7 Verify OK");
				retmap.put("result","TRUE");
			}
	    } catch (ISecurityException e) {
	    	System.out.println("ISecurityException: " + e);
	    	retmap.put("msg","登入失敗");
	    	retmap.put("errmsg","loginError");
	    	return retmap;//先出現驗證失敗 導回"/"
	    }
		// Ending.
		System.out.println("Verification Finished.");
		
    	return retmap;
    }
	
	public EACH_USER_Dao getEach_user_Dao() {
		return each_user_Dao;
	}
	public void setEach_user_Dao(EACH_USER_Dao each_user_Dao) {
		this.each_user_Dao = each_user_Dao;
	}
	public EACH_ROLE_FUNC_Dao getRole_func_Dao() {
		return role_func_Dao;
	}
	public void setRole_func_Dao(EACH_ROLE_FUNC_Dao role_func_Dao) {
		this.role_func_Dao = role_func_Dao;
	}
	public EACH_FUNC_LIST_Dao getFunc_list_Dao() {
		return func_list_Dao;
	}
	public void setFunc_list_Dao(EACH_FUNC_LIST_Dao func_list_Dao) {
		this.func_list_Dao = func_list_Dao;
	}
	public CodeUtils getCodeUtils() {
		return codeUtils;
	}
	public void setCodeUtils(CodeUtils codeUtils) {
		this.codeUtils = codeUtils;
	}
	public SYS_PARA_Dao getSys_para_Dao() {
		return sys_para_Dao;
	}
	public void setSys_para_Dao(SYS_PARA_Dao sys_para_Dao) {
		this.sys_para_Dao = sys_para_Dao;
	}
	public BANK_GROUP_Dao getBank_group_Dao() {
		return bank_group_Dao;
	}
	public void setBank_group_Dao(BANK_GROUP_Dao bank_group_Dao) {
		this.bank_group_Dao = bank_group_Dao;
	}
	public EACH_USERLOG_BO getUserlog_bo() {
		return userlog_bo;
	}
	public void setUserlog_bo(EACH_USERLOG_BO userlog_bo) {
		this.userlog_bo = userlog_bo;
	}
	public AGENT_PROFILE_BO getAgent_profile_bo() {
		return agent_profile_bo;
	}
	public void setAgent_profile_bo(AGENT_PROFILE_BO agent_profile_bo) {
		this.agent_profile_bo = agent_profile_bo;
	}
	
	
	
}
