package tw.org.twntch.util;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoaderListener;

public class SpringBeanFactoryBruce {
	private static ApplicationContext ctx;
	public static void initApplicationContext() {
		ctx = ContextLoaderListener.getCurrentWebApplicationContext();
		System.out.println("initApplicationContext start and ctx="+ctx);
	}

	public static <T> T getBean(String name) {
		if (ctx == null) {
			initApplicationContext();
		}
		return (T) ctx.getBean(name);
	}
}

