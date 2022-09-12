package tw.org.twntch.util;


import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 將 Request 及 Response 放至到 ThreadLocal 需配合 SysFilter 作用
 * 
 * @author Owner
 * 
 */
public class WebServletUtils {
	private static ThreadLocal req = new ThreadLocal();
	private static ThreadLocal res = new ThreadLocal();
	private static ServletConfig servletConfig = null;
	private static ServletContext servletContext = null;
	private static ThreadLocal uploadFiles = new ThreadLocal();


	public static void initThreadLocal(HttpServletRequest request,
			HttpServletResponse response) {
		req.set(request);
		res.set(response);
	}

	public static void removeThreadLocal() {
		req.set(null);
		res.set(null);
		uploadFiles.set(null);
	}

	public static HttpServletRequest getRequest() {
		return (HttpServletRequest) req.get();
	}

	public static HttpServletResponse getResponse() {
		return (HttpServletResponse) res.get();
	}

	public static ServletConfig getServletConfig() {
		return servletConfig;
	}

	public static void setServletConfig(ServletConfig config) {
		servletConfig = config;
	}

	public static ServletContext getServletContext() {
		return servletContext;
	}

	public static void setServletContext(ServletContext context) {
		servletContext = context;
	}


}
