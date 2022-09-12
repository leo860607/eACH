package tw.org.twntch.bo;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanNameAware;

import tw.org.twntch.util.SpringAppCtxHelper;


/**
 * aop使用對應欄位的中文名稱
 * @author hugo
 *
 */
public class FieldsMap implements BeanNameAware
{
	public static String beanName = "";
	
	static Logger log = Logger.getLogger(FieldsMap.class);
	
	public Map<String, Object> args;
	
	public static Object getArg(String name)
	{
		FieldsMap b = SpringAppCtxHelper.getBean(beanName);
		return b.getArgs().get(name);
	}

	public static String getStringArg(String name)
	{
		FieldsMap b = SpringAppCtxHelper.getBean(beanName);
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
		beanName = arg0;
	}
	
	
}

