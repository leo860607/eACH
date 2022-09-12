package tw.org.twntch.util;


import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.apache.log4j.Logger;


/**
 * Generics的util類,
 *
 * @author sshwsfc
 */
public class GenericsUtils {
	private static Logger log = Logger.getLogger(GenericsUtils.class);

	private GenericsUtils() {
	}

	/**
	 * 通過反射,獲得定義Class時聲明的父類的范型參數的類型.
	 * 如public BookManager extends GenricManager<Book>
	 *
	 * @param clazz The class to introspect
	 * @return the first generic declaration, or <code>Object.class</code> if cannot be determined
	 */
	public static Class getSuperClassGenricType(Class clazz) {
		return getSuperClassGenricType(clazz, 0);
	}

	/**
	 * 通過反射,獲得定義Class時聲明的父類的范型參數的類型.
	 * 如public BookManager extends GenricManager<Book>
	 *
	 * @param clazz clazz The class to introspect
	 * @param index the Index of the generic ddeclaration,start from 0.
	 */
	public static Class getSuperClassGenricType(Class clazz, int index) throws IndexOutOfBoundsException {

		Type genType = clazz.getGenericSuperclass();

		if (!(genType instanceof ParameterizedType)) {
			log.warn(clazz.getSimpleName() + "'s superclass not ParameterizedType");
			return Object.class;
		}

		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

		if (index >= params.length || index < 0) {
			log.warn("Index: " + index + ", Size of " + clazz.getSimpleName() + "'s Parameterized Type: " + params.length);
			return Object.class;
		}
		if (!(params[index] instanceof Class)) {
			log.warn(clazz.getSimpleName() + " not set the actual class on superclass generic parameter");
			return Object.class;
		}
		return (Class) params[index];
	}
}
