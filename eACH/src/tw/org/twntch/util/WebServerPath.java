package tw.org.twntch.util;

import javax.servlet.ServletContext;

import org.springframework.web.context.ServletContextAware;
/**
 * 再一些情況下(ex:排程)， 要getRealPath時，使用
 * WebServletUtils.getServletContext()在此情況會是null居多
 * @author Hugo
 *
 */
public class WebServerPath implements ServletContextAware{
	private String serverRootUrl;

	@Override
	public void setServletContext(ServletContext servletContext) {
		// TODO Auto-generated method stub
		this.serverRootUrl=servletContext.getRealPath("/");
		
	}
	
    public String getServerRootUrl(){ return serverRootUrl; }

    
}
