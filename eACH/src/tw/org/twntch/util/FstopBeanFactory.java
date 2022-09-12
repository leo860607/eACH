package tw.org.twntch.util;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

//import tw.gov.nsc.exception.ServiceBindingException;
//import tw.gov.nsc.exception.UncheckedException;

public class FstopBeanFactory {


	private static Logger getLogger() {
		return Logger.getLogger(FstopBeanFactory.class);
	}
	
	public static Object getBean(String beanName) {
		return SpringBeanFactory.getBean(beanName);
	}
	
	public static Object beanMethodInvoke(String beanName,  String methodName, Class[] parameters, Object[] arguments)
			throws Exception { 
		
		Object result = null;
		try {
			String bo = StrUtils.trim(beanName);
			//bo = bo.toLowerCase();
			//System.out.println("bo==="+bo);
			Object bean = null;
			try {
				bean = SpringBeanFactory.getBean(bo);
			}
			catch(NoSuchBeanDefinitionException e) {
				System.out.println("找不到對應的 BO Service. [" + bo + "], "+e);
//				throw new ServiceBindingException("找不到對應的 BO Service. [" + bo + "]", e);
			}

			Method myMethod = null;
			try {
				myMethod = bean.getClass().getMethod(methodName, parameters);
			}
			catch(Exception e) {
				System.out.println("BO Service 無對應的 Method (" + methodName + "). [" + bo + "] ,+" +e);
//				throw new ServiceBindingException("BO Service 無對應的 Method (" + methodName + "). [" + bo + "]", e);
			}
			if(myMethod == null)
				System.out.println("BO Service 無對應的 Method (" + methodName + "). [" + bo + "]");
//				throw new ServiceBindingException("BO Service 無對應的 Method (" + methodName + "). [" + bo + "]");

			result = (Object)myMethod.invoke(bean, arguments);
			return result;
		}
		catch(java.lang.reflect.InvocationTargetException e) {
			System.out.println("呼叫 Service 時有誤 !! \r\nbeanMethodInvoke Error [beanName: " + beanName + ", methodName: " + methodName + ", arguments: " + arguments + "]!! , " + e.getTargetException());
//			if(e.getTargetException() instanceof UncheckedException) {
//				throw (UncheckedException)e.getTargetException();
//			}
//			else {
//				throw new ServiceBindingException("呼叫 Service 時有誤 !! \r\nbeanMethodInvoke Error [beanName: " + beanName + ", methodName: " + methodName + ", arguments: " + arguments + "]!!", e.getTargetException());
//			}
			return null;
		}
		catch (Exception e) {
			// TODO: handle exception
			System.out.println("beanMethodInvoke.Exception>>"+e);
			return null;
		}
	}
	
}
