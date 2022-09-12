package tw.org.twntch.util;

import javax.servlet.ServletContext;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;


public class SpringBeanFactory {
	private static Logger getLogger() {
		return Logger.getLogger(SpringBeanFactory.class);
	}
	
	private ApplicationContext appContext = null;
	private static SpringBeanFactory instance = null;
	public static SpringBeanFactory getInstance() {
		if(instance == null) {
			synchronized (SpringBeanFactory.class) {
				if(instance == null) {
					instance = new SpringBeanFactory();
				}
			}
		}
		return instance;
	}
	
	public SpringBeanFactory()
	{
		ServletContext servletContext = WebServletUtils.getServletContext();
		appContext = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
		if(null == appContext) System.out.println("appContext==="+appContext);
	}
	
	
	public static Object getBean(String beanid) {
		return SpringBeanFactory.getInstance().appContext.getBean(beanid);
	}
	
}

