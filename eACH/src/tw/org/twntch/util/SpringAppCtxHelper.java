package tw.org.twntch.util;


import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.support.XmlWebApplicationContext;

import tw.org.twntch.bo.Arguments;

/**
 * 與 SpringBeanFactory 不同，與 web context 無關，取得 spring context 的方式</br>
 * <pre>
 *  最近我我開發的一個系統裡邊有老的代碼， 這些老代碼沒有使用Spring，
 *  我們準備用Spring改寫老代碼， 但是寫到一半， 發現問題很多，
 *  然後我們又不想丟掉我們寫好的新代碼， 所以我們就需要找到一個方法，
 *  使我們能夠在老的代碼裡訪問Spring的bean. 
 *  我們都知道要訪問Spring bean 我們就必須得到一個ApplicationContext 或者BeanFactory 對象，
 *  而ApplicationContext的BeanFactory 的子類， 
 *  擁有更強大的功能，ApplicationContext可以在服務器啟動的時候自動實例化所有的bean,
 *  而BeanFactory只有在調用getBean()的時候才去實例化那個bean, 
 *  這也是我們為什麼要得到一個ApplicationContext對象， 
 *  事實上Spring2相關的web應用默認使用的是ApplicationContext對象去實例化bean，
 *  換一句話說， 在服務器啟動的時候，Spring容器就已經實例化好了一個ApplicationContext對象，
 *  所以我們要在老的代碼裡嘗試去獲取這個對象。但是如何才能得到一個ApplicationContext對象呢？
 *  方法很多，最常用的辦法就是用ClassPathXmlApplicationContext， 
 *  FileSystemClassPathXmlApplicationContext， 
 *  FileSystemXmlApplicationContext 
 *  等對象去加載Spring配置文件，這樣做也是可以， 但是在加載Spring配置文件的時候，
 *  就會生成一個新的ApplicaitonContext對象而不是Spring容器幫我們生成的哪一個，
 *  這樣就產生了冗餘， 所以我們在這裡不採用這種加載文件的方式，
 *  我們使用ApplicationContextAware讓Spring容器傳遞自己生成的ApplicationContext給我們， 
 *  然後我們把這個ApplicationContext設置成一個類的靜態變量， 這樣我們就隨時都可以在老的代碼裡得到Application的對象了。
 *  </pre>
 * 
 * @author andy
 *
 */
public class SpringAppCtxHelper implements ApplicationContextAware
{
	private static ApplicationContext applicationContext;  
	static Logger log = Logger.getLogger(SpringAppCtxHelper.class);
	@Override
	@Autowired
	public void setApplicationContext(ApplicationContext ctx)
			throws BeansException {
		
		System.out.println("================ ApplicationContext Injected ================");
		log.debug("================ ApplicationContext Injected ================");
		applicationContext = ctx;
	}
	
	public static ApplicationContext getApplicationContext()
	{
		return applicationContext;
	}
	
	/**
	* Refresh the context that manages this bean.
	*/
	public static void refresh() 
	{
		if (applicationContext instanceof XmlWebApplicationContext) 
		{
			XmlWebApplicationContext xmlWebApplicationContext = (XmlWebApplicationContext) applicationContext;
			
			System.out.println("xmlWebApplicationContext>>"+xmlWebApplicationContext);
			log.debug("xmlWebApplicationContext>>"+xmlWebApplicationContext);
			xmlWebApplicationContext.refresh();
		}
	}
	
	/**
	 * 自動轉型為所賦值對象的類型
	 * @param beanName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean( String beanName ) {  
		System.out.println("beanName>>"+beanName);
		log.debug("beanName>>"+beanName);
		if (applicationContext == null) 
			System.out.println("SpringAppCtxHelper context=null!");
			log.debug("applicationContext>>"+applicationContext);
		//當載入  bean 失敗時，先 refresh 再試一次
		try
		{
			return (T) applicationContext.getBean( beanName );
		}
		catch(Exception e)
		{
			refresh();
			return (T) applicationContext.getBean( beanName );
		}
    } 	

}

