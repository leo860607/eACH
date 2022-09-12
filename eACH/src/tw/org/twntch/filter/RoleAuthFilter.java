package tw.org.twntch.filter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import tw.org.twntch.db.dao.hibernate.EACH_FUNC_LIST_Dao;
import tw.org.twntch.db.dao.hibernate.EACH_ROLE_FUNC_Dao;
import tw.org.twntch.form.Each_User_Form;
import tw.org.twntch.form.Login_Form;
import tw.org.twntch.util.SpringAppCtxHelper;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.WebServletUtils;

public class RoleAuthFilter implements Filter{
private Logger logger = Logger.getLogger(this.getClass().getName());	
private EACH_ROLE_FUNC_Dao role_func_Dao ;
private EACH_FUNC_LIST_Dao func_list_Dao ;
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		WebServletUtils.removeThreadLocal();
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		HttpServletRequest req  = (HttpServletRequest) request ;
//		HttpSession session = req.getSession(false);
		HttpSession session = req.getSession();
		
		Login_Form login_form = (Login_Form) session.getAttribute("login_form");
		System.out.println("session.login_form>>"+login_form);
		logger.debug("session.login_form>>"+login_form);
		if(login_form != null &&login_form.getUserData() !=null){
			System.out.println("userData>>"+login_form.getUserData());
			System.out.println("userData.getUSER_ID()>>"+login_form.getUserData().getUSER_ID());
			System.out.println("userData.getROLE_ID()>>"+login_form.getUserData().getROLE_ID());
			logger.debug("userData>>"+login_form.getUserData());
			logger.debug("userData.getUSER_ID()>>"+login_form.getUserData().getUSER_ID());
			logger.debug("userData.getROLE_ID()>>"+login_form.getUserData().getROLE_ID());
		}else{
			chain.doFilter(request, response);
			return;
		}
		
		String fcid = req.getParameter("fcid");
		String funcUrl= req.getRequestURI().replace(req.getContextPath()+"/", "");
		String userType = login_form.getUserData().getUSER_TYPE();
		String func_name = "";
		String up_func_name = "";
		System.out.println("fcid>>"+fcid);
		System.out.println("funcUrl>>"+funcUrl);
		logger.debug("fcid>>"+fcid+",funcUrl>>"+funcUrl);
		func_list_Dao = SpringAppCtxHelper.getBean("func_list_Dao");
		System.out.println("func_list_Dao22>>"+func_list_Dao);
		List<Map> list = null;
		if(StrUtils.isEmpty(fcid)){
			list = func_list_Dao.getFuncAuth(funcUrl ,login_form.getUserData().getROLE_ID());
		}else{
			list = func_list_Dao.getFuncAuthByFuncId(fcid ,login_form.getUserData().getROLE_ID());
		}
		
		for(Map map :list){
			System.out.println("AUTH_TYPE>>"+map.get("AUTH_TYPE"));
			logger.debug("AUTH_TYPE>>"+map.get("AUTH_TYPE"));
//			login_form.setS_auth_type(String.valueOf(map.get("AUTH_TYPE")) );
			if( userType.equals("A")){
				func_name = String.valueOf(map.get("FUNC_NAME")) ;
				up_func_name = String.valueOf(map.get("UP_FUCNAME")) ;
			}else{
				func_name = StrUtils.isEmpty(String.valueOf(map.get("FUNC_NAME_BK"))) ?String.valueOf(map.get("FUNC_NAME")) :String.valueOf(map.get("FUNC_NAME_BK"));
				up_func_name = StrUtils.isEmpty(String.valueOf(map.get("UP_FUCNAME_BK"))) ?String.valueOf(map.get("UP_FUCNAME")) :String.valueOf(map.get("UP_FUCNAME_BK"));
			}
			login_form.getUserData().setS_auth_type(String.valueOf(map.get("AUTH_TYPE")));
			login_form.getUserData().setS_up_func_name(up_func_name);
//			login_form.getUserData().setS_func_name(String.valueOf(map.get("FUNC_NAME")));
			login_form.getUserData().setS_func_name(func_name);
			login_form.getUserData().setS_fcid(fcid);
			login_form.getUserData().setS_is_record(String.valueOf(map.get("IS_RECORD")));
		}
		session.setAttribute("login_form", login_form);
//		利用funcUrl去查詢目前是屬於哪個功能funcId 在用funcId 去查該 session role_FuncMap 的funcid 的權限
//		role_FuncMap 應該在 loginAction 是存在session理(UserData)
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig cfg) throws ServletException {
		// TODO Auto-generated method stub
		
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


	

}
