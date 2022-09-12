package tw.org.twntch.ctrl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import tw.org.twntch.util.FstopBeanFactory;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.WebServletUtils;



public class SecondInfoCtrl extends HttpServlet {
	Logger logger = Logger.getLogger(getClass().getName());
	public static final String REQ_PAY_LOAD_NAME = "__BODY__";
	private static final String REQ_ENCODING_KEY = "controller.req.encoding";
	private static final String REP_ENCODING_KEY = "controller.rep.encoding";
	private static String REQ_ENCODING = "UTF-8";  //預設 UTF-8
	private static String REP_ENCODING = "UTF-8";  //預設 UTF-8
			
	protected void processRequest(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, java.io.IOException 
	{
		
		request.setCharacterEncoding(REQ_ENCODING);
		
		//改為在 filter 做，因為不是所有的 request 都會經過這個 servlet
//		WebServletUtils.initThreadLocal(request, response);
//		WebServletUtils.setServletContext(getServletContext());
		
		String serverName = request.getServerName();
		logger.debug("serverName=" + serverName);
		
		if (serverName.indexOf(".") >= 0)
			serverName = serverName.substring(0, serverName.indexOf("."));

		logger.info("-->Controller.processRequest().");
		String beanName = request.getParameter("component");
		String methodName = request.getParameter("method");
		String trancode = request.getParameter("trancode");
		String pureJson = request.getParameter("pureJson");

		logger.info("beanName=" + beanName);
		logger.info("methodName=" + methodName);
		logger.info("trancode=" + trancode);
		logger.info("pureJson=" + pureJson);
		System.out.println("beanName=" + beanName);
		System.out.println("methodName=" + methodName);
		System.out.println("trancode=" + trancode);
		System.out.println("pureJson=" + pureJson);

/*		
		if (StrUtils.isEmpty(beanName) || StrUtils.isEmpty(methodName))
		{
			response.sendRedirect("./");
			return;			
		}
		
		HttpSession session = request.getSession(true);
		if ((session == null || session.isNew()) && (StrUtils.isNotEmpty(beanName) && !beanName.equals("loginservice")) )
		{
			System.out.println("Servlet User not login!!");
			response.sendRedirect("./");
			return;
		}
		else
		{
			System.out.println("Session is not null." + session.getId());
		}
*/		
		
		// 將Request的參數全部放到userInput裡。
		//Hashtable userInput = new Hashtable();
		HashMap userInput = new HashMap();
		
		Enumeration paramNames = request.getParameterNames();
		while (paramNames.hasMoreElements()) 
		{
			String paramName = (String) paramNames.nextElement();			
			//對於多選的網頁元件，例如 checkbox 需再進一步檢查多選狀態
			//String paramValue = request.getParameter(paramName);			
			String[] paramValues =request.getParameterValues(paramName);
			
			if (paramValues.length == 1)
			{
				userInput.put(paramName, paramValues[0]);
				logger.debug("  " + paramName + "=<" + paramValues[0] + ">");
				System.out.println("  " + paramName + "=<" + paramValues[0] + ">");				
			}
			else
			{
				//多選元件的值以 arraylist 轉成 json 字串後放入 hashmap 中
				List list = new ArrayList();
				for(int i=0; i < paramValues.length; i++) 
				{
					list.add(paramValues[i]);
					logger.debug("  " + paramName + "=<" + paramValues[i] + ">");
					System.out.println("  " + paramName + "=<" + paramValues[i] + ">");				
	            }
				String values = JSONUtils.toJson(list);
				userInput.put(paramName, values);
			}
			
		}	//while
		
		//取得 BODY 資料 (HTTP Request Payload, if any)
		String values = getRequestBody(request);		
		System.out.println("BODY=" + values);
		userInput.put(REQ_PAY_LOAD_NAME, values);
		
		//呼叫相關CLASS和METHOD
		//Class[] parameterTypes = new Class[] { Hashtable.class };
		Class[] parameterTypes = new Class[] { Map.class };
		Object[] arguments = new Object[] {userInput};
		String buf = null;
		try 
		{
			if(trancode == null || trancode.equals(""))
			{
				/*  當網頁以 get 方式發出請求又未帶 trancode時 */
				response.setCharacterEncoding(REP_ENCODING);					
				buf = (String) FstopBeanFactory.beanMethodInvoke(beanName, methodName,	parameterTypes, arguments);
				if (StrUtils.isNotEmpty(pureJson) && pureJson.equals("true"))
				{
					response.setContentType("application/json");
				}

				//logger.debug(buf);
				
				PrintWriter out = response.getWriter();
				out.print(buf);  //請勿使用 println ，以防 client 端異常
				out.close();
				
			}
			else
			{
				HashMap map = (HashMap) FstopBeanFactory.beanMethodInvoke(beanName, methodName,	parameterTypes, arguments);
				request.setAttribute("ResultHashMap", map);  //為了下一個 JSP/Servlet 功能
				if (map.get("trancode") != null)
				{
					trancode = (String)map.get("trancode");
					//request.getRequestDispatcher("./jsp/" + trancode + ".jsp").forward(request, response);	
					response.sendRedirect("./" + trancode + ".html");
				}
				else
				{
					
				}
			}
			
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		logger.info("<--Controller.processRequest().");
	}

	
	protected String getRequestBody(HttpServletRequest request) throws IOException
	{
		String ret = null;
		StringBuilder stringBuilder = new StringBuilder();
		BufferedReader bufferedReader = null;
		try 
		{
			InputStream inputStream = request.getInputStream();
			if (inputStream != null) 
			{
				bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
				char[] charBuffer = new char[128];
				int bytesRead = -1;
				while ((bytesRead = bufferedReader.read(charBuffer)) > 0) 
				{
					stringBuilder.append(charBuffer, 0, bytesRead);
				}
			} 
			else 
			{
				stringBuilder.append("");
			}
		} 
		catch (IOException ex) 
		{
		  throw ex;
		} 
		finally 
		{
		  if (bufferedReader != null) 
		  {
			  try 
			  {
				  bufferedReader.close();
			  } 
			  catch (IOException ex) 
			  {
				  //Ignore
			  }
		  }
		}
		ret = stringBuilder.toString();
		//ret = StrUtils.unescapeJSURL(new String(ret.getBytes("iso-8859-1"), "UTF-8"));
		//ret = StrUtils.unescapeJSURL(ret);
		//System.out.println(new String(ret.getBytes("iso-8859-1"), "UTF-8"));
		//logger.debug(StrUtils.unescapeJSURL(new String(ret.getBytes("iso-8859-1"), "UTF-8")));
		logger.debug(ret);
		return ret;
	}
	
	public void init(ServletConfig config) throws ServletException 
	{
	    super.init(config);
	    //REQ_ENCODING = config.getInitParameter(REQ_ENCODING_KEY);
	    String param = null;
	    param = getServletContext().getInitParameter(REQ_ENCODING_KEY); 
	    if (StrUtils.isNotEmpty(param))
	    {
		    REQ_ENCODING = param;	    	
	    }
	    param = getServletContext().getInitParameter(REP_ENCODING_KEY); 
	    if (StrUtils.isNotEmpty(param))
	    {
		    REP_ENCODING = param;	    	
	    }
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, java.io.IOException 
	{
		logger.debug("============doGet====================");
		processRequest(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, java.io.IOException 
	{
		logger.debug("============doPost====================");
		processRequest(request, response);
	}
	
}
