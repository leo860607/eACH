package tw.org.twntch.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.util.Assert;

/**
 * 擴展Apache Commons BeanUtils, 提供一些反射方面缺失的封裝.
 */
public class BeanUtils extends org.apache.commons.beanutils.BeanUtils {
	private static Logger log = Logger.getLogger(BeanUtils.class);

	private BeanUtils() {
	}

	/**
	 * 暴力獲取當前類聲明的private/protected變量
	 */
	static public Object getDeclaredProperty(Object object, String propertyName) throws IllegalAccessException, NoSuchFieldException {
		Assert.notNull(object);
		Assert.hasText(propertyName);
		Field field = object.getClass().getDeclaredField(propertyName);
		return getDeclaredProperty(object, field);
	}

	/**
	 * 暴力獲取當前類聲明的private/protected變量
	 */
	static public Object getDeclaredProperty(Object object, Field field) throws IllegalAccessException {
		Assert.notNull(object);
		Assert.notNull(field);
		boolean accessible = field.isAccessible();
		field.setAccessible(true);
		Object result = field.get(object);
		field.setAccessible(accessible);
		return result;
	}

	/**
	 * 暴力設置當前類聲明的private/protected變量
	 */
	static public void setDeclaredProperty(Object object, String propertyName, Object newValue) throws IllegalAccessException, NoSuchFieldException {
		Assert.notNull(object);
		Assert.hasText(propertyName);

		Field field = object.getClass().getDeclaredField(propertyName);
		setDeclaredProperty(object, field, newValue);
	}
	/**
	 * 暴力設置當前類聲明的private/protected變量
	 * add by hugo
	 * 如果該類無該屬性就跳過
	 */
	static public void setDeclaredPropertyII(Object object, Map<String , String> map) throws IllegalAccessException, NoSuchFieldException {
		Assert.notNull(object);
		Assert.notNull(map);
//		Assert.hasText(propertyName);
		
//		Field field = object.getClass().getDeclaredField(propertyName);
		Field[] field = object.getClass().getDeclaredFields();
		for( int i = 0 ; i <field.length ; i++ ){
			System.out.println(i+",field>>"+field[i].getName());
//			arguments 要跳過的原因是 在SYS中是一個類別
			if(map.containsKey(field[i].getName()) && !field[i].getName().equals("arguments")){
				setDeclaredProperty(object, field[i].getName(), map.get(field[i].getName()));
			}
		}
//		if(field !=null){
//			setDeclaredProperty(object, field, newValue);
//		}
	}

	/**
	 * 暴力設置當前類聲明的private/protected變量
	 */
	static public void setDeclaredProperty(Object object, Field field, Object newValue) throws IllegalAccessException {
		boolean accessible = field.isAccessible();
		field.setAccessible(true);
		field.set(object, newValue);
		field.setAccessible(accessible);
	}

	/**
	 * 暴力調用當前類聲明的private/protected函數
	 */
	static public Object invokePrivateMethod(Object object, String methodName, Object... params) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		Assert.notNull(object);
		Assert.hasText(methodName);
		Class[] types = new Class[params.length];
		for (int i = 0; i < params.length; i++) {
			types[i] = params[i].getClass();
		}
		Method method = object.getClass().getDeclaredMethod(methodName, types);

		boolean accessible = method.isAccessible();
		method.setAccessible(true);
		Object result = method.invoke(object, params);
		method.setAccessible(accessible);
		return result;
	}

	/**
	 * 按Filed的類型取得Field列表
	 */
	static public List<Field> getFieldsByType(Object object, Class type) {
		ArrayList<Field> list = new ArrayList<Field>();
		Field[] fields = object.getClass().getDeclaredFields();
		for (Field field : fields) {
			if (field.getType().isAssignableFrom(type)) {
				list.add(field);
			}
		}
		return list;
	}

	/**
	 * 獲得field的getter名稱
	 */
	public static String getAccessorName(Class type, String fieldName) {
		Assert.hasText(fieldName, "FieldName required");
		Assert.notNull(type, "Type required");

		if (type.getName().equals("boolean")) {
			return "is" + StringUtils.capitalize(fieldName);
		} else {
			return "get" + StringUtils.capitalize(fieldName);
		}
	}

	/**
	 * 獲得field的getter名稱
	 */
	public static Method getAccessor(Class type, String fieldName) {
		try {
			return type.getMethod(getAccessorName(type, fieldName));
		} catch (NoSuchMethodException e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}
	
	public static void dumpBean(Object bean, String sp)
	{
		try 
		{
			Map m = describe(bean);
			Set ks = m.keySet();
			Iterator ir = ks.iterator();
			while(ir.hasNext())
			{
				String key = (String) ir.next();
				String value = PropertyUtils.getProperty(bean, key).toString();
				log.debug(sp + key + " = " + value);
				System.out.println(sp + key + " = " + value);
			}
		} 
		catch (Exception e) 
		{
		}		
	}
	
}
