package tw.org.twntch.filter;


import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import tw.org.twntch.bo.Arguments;
import tw.org.twntch.socket.HSMSuipResponseData;
import tw.org.twntch.util.CodeUtils;
import tw.org.twntch.util.HTTPDownload;
import tw.org.twntch.util.SpringAppCtxHelper;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.WebServletUtils;


public class SysFilter implements Filter
{
	Logger logger = Logger.getLogger(getClass());
	private HTTPDownload httpdownload ;
	FilterConfig config;

	public void setFilterConfig(FilterConfig config) {
		this.config = config;
	}

	public FilterConfig getFilterConfig() {
		return config;
	}
	  
	@Override
	public void destroy() 
	{
		WebServletUtils.removeThreadLocal();
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//		System.out.println("getSession>>"+((HttpServletRequest) request).getSession());
		logger.debug("getSession>>"+((HttpServletRequest) request).getSession());
		WebServletUtils.initThreadLocal((HttpServletRequest) request, (HttpServletResponse) response);
		ServletContext context = getFilterConfig().getServletContext();
		WebServletUtils.setServletContext(context);
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse resp = (HttpServletResponse)response;
		//如果URI結尾是httpMethod，表示透過HTTP連進來要資料
		System.out.println("URI>>"+req.getRequestURI().substring(req.getRequestURI().lastIndexOf("/")+1));
		if("httpMethod".equals(req.getRequestURI().substring(req.getRequestURI().lastIndexOf("/")+1))){
			httpdownload = (HTTPDownload) (httpdownload == null? SpringAppCtxHelper.getBean("httpdownload") : httpdownload);
			
			if(StrUtils.isNotEmpty(req.getParameter("agentId"))){
//				if(StrUtils.isNotEmpty(req.getParameter("funcId")) && req.getParameter("funcId").equals("agentdlrp") ){
				httpdownload.downloadForHTTP_4_AGENT(req.getParameter("funcId"),req.getParameter("bizDate"),req.getParameter("bizmon"),req.getParameter("agentId"), req.getParameter("clrbk"),req.getParameter("clsPhase"),req.getParameter("bizmon"),req.getParameter("mac"),req.getParameter("keyFlag"));
			}else{
				httpdownload.downloadForHTTP(req.getParameter("funcId"),req.getParameter("bizDate"),req.getParameter("bizmon"),req.getParameter("opbkId"), req.getParameter("clrbk"),req.getParameter("clsPhase"),req.getParameter("bizmon"),req.getParameter("mac"),req.getParameter("keyFlag"));
			}
			http://eachtest.twnch.org.tw/eACH/httpMethod?funcId=agentdlrp&opbkId=xxxxxxxx&bizDate=20150301&clsPhase=01&keyFlag=02&mac=xxxx
			
			return;
		}
		//檢核 session 是否存在
		String isCheck = Arguments.getStringArg("GLOBAL.CHECK.SESSION");
//		String isCheck = "N";
		if (isCheck.equalsIgnoreCase("Y"))
		{
			Map<String,String> parameters = SpringAppCtxHelper.getBean("parameters");
			String isFormal = parameters.get("isFormal");
			req.getSession().setAttribute("isFormal", isFormal);
			logger.debug("isFormal>>>"+isFormal);
			boolean sessionOk = checkSession(request, response);
//			boolean sessionOk = false;
			if (!sessionOk)
			{
//				String url = Arguments.getStringArg("GLOBAL.CHECK.SESSION.URL");
				String url = req.getContextPath()+"/index.jsp";
//				String url = "http://localhost:8080/eACH/index.jsp";
				logger.debug("url>>"+url);
				resp.sendRedirect(url);
				return;			
			}			
		}
		
		chain.doFilter(request, response);
	}
	
	@Override
	public void init(FilterConfig cfg) throws ServletException {
		config = cfg;
	}

	public boolean checkSession(ServletRequest request, ServletResponse response)
	{
//		boolean ret = false;
		boolean ret = true;
		
		//在例外清單中的 URL 不檢核
		if (checkUrlList(request, response))
		{
			return true;
		}

		HttpServletRequest req = (HttpServletRequest) request;
		HttpSession session = req.getSession(false);
		System.out.println("session>>"+session);
//		HttpSession session = req.getSession(true);
		if (session == null) 
		{
			System.out.println(">>> null session" + req.getRequestURI());
			return false;
		}
		logger.debug("session.login_form"+session.getAttribute("login_form"));
		logger.debug("session.S.USER.ID>>"+session.getAttribute("S.USER.ID"));
		if(session.getAttribute("S.USER.ID")==null)
		{
			logger.debug(">>> null USERID" + req.getRequestURI());
			return false;
		}
		else
		{
			ret = true;
		}
		return ret;
	}
	
	/**
	 * 採用例外清單方式來設定不檢核的 URL，其餘一律檢核 session
	 * @param request
	 * @param response
	 * @return
	 */
	public boolean checkUrlList(ServletRequest request, ServletResponse response)
	{
		boolean ret = false;
		HttpServletRequest req = (HttpServletRequest) request;
		String url = req.getRequestURI();
		
		/*
		   protocol:RegExp.$2,
		   host:RegExp.$3,
           path:RegExp.$4,
           file:RegExp.$6,
           query:RegExp.$7,
           hash:RegExp.$8
           
           0:http://www.google.com.tw/aaa/bbb.do?userId=1234&xxx=6789
		   1:http:/
           2:http
           3:www.google.com.tw
           4:/aaa/
           5:/aaa
           6:bbb.do
           7:?userId=1234&xxx=6789
           8:null
		*/
		logger.debug("checkUrlList.url>>"+url);
		List urlList = (List) SpringAppCtxHelper.getBean("spURL");

		//使用 regex 來拆解 URL
		String regex = "^((http[s]?|ftp):\\/)?\\/?([^:\\/\\s]+)((\\/\\w+)*\\/)([\\w\\-\\.]+[^#?\\s]+)(.*)?(#[\\w\\-]+)?$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(url);
		boolean matchFound = matcher.find();
		String str = "";
		if (matchFound)
		{
//			for(int i = 0; i <= matcher.groupCount(); i++) 
//			{
//		        String groupStr = matcher.group(i);		        
//		        System.out.println(i + ":" + groupStr);
//		    }
			//只比對 4, 6 
			str = matcher.group(4);
			if (urlList.contains(str))
			{
//				System.out.println("match url4=" + url);
				logger.debug("match url4=" + url);
				return true;
			}
			str = matcher.group(6);
			if (urlList.contains(str))
			{
//				System.out.println("match url6=" + url);
				logger.debug("match url6=" + url);
				return true;
			}			
		}
		
		return ret;
	}

	public HTTPDownload getHttpdownload() {
		return httpdownload;
	}

	public void setHttpdownload(HTTPDownload httpdownload) {
		this.httpdownload = httpdownload;
	}
	
	
	
}
