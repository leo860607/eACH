package tw.org.twntch.bo;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanNameAware;

import tw.org.twntch.util.SpringAppCtxHelper;


/**
 * 提供共用常數設定值
 * @author andy
 *
 */
public class Arguments implements BeanNameAware
{
	public static String beanName = "";
	
	static Logger log = Logger.getLogger(Arguments.class);
	
	public Map<String, Object> args;
	
	public static Object getArg(String name)
	{
		Arguments b = SpringAppCtxHelper.getBean(beanName);
		return b.getArgs().get(name);
	}

	public static String getStringArg(String name)
	{
		System.out.println("beanName>>"+beanName);
		log.debug("beanName>>"+beanName);
		Arguments b = SpringAppCtxHelper.getBean(beanName);
		if (b == null)
		{
			System.out.println("Arguments is null");
		}
		return (String) b.getArgs().get(name);
	}
	
	public Map<String, Object> getArgs() {
		return args;
	}

	public void setArgs(Map<String, Object> args) {
		this.args = args;
	}

	@Override
	public void setBeanName(String arg0) 
	{
		log.debug("set bean name=" + arg0);
		System.out.println("set bean name=" + arg0);
		beanName = arg0;
	}
	
	
}

