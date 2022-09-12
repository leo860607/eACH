package tw.org.twntch.util;



import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.type.NullableType;
import org.hibernate.type.Type;
/**
 * @author Hugo
 *
 *
 *
 */
public class AutoAddScalar {

	Logger logger = Logger.getLogger(AutoAddScalar.class.getName());
	
	/**
	 * 根據傳入的欄位名稱及PO來自動使用org.hibernate.SQLQuery.addScalar(col , Type)
	 * @param query
	 * @param obj
	 * @param objs
	 */
	public void addScalar(SQLQuery query , Object obj , Object... objs ){
		Object reobj = null ;
		Type type = null ;
		String col = "";
		try {
			for (int i = 0; i < objs.length; i++) {
				col=objs[i].toString().trim();
				reobj = this.getType((Class)obj ,"get"+col);
				type = this.decideHibernateType(reobj);
				query.addScalar(col , type);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("AutoAddScalar.addScalar.Exception==>"+e);
		}
		
	}
	
	/**
	 * 根據傳入屬性及其對應的類別來取得該屬性的型態
	 * @param obj
	 * @param col
	 * @return
	 */
	public Object getType(Class clas , String col){
		Object rtobj = null ;
		try {
			rtobj = clas.getMethod(col).getReturnType();
		} catch (SecurityException e) {
			logger.error("AutoAddScalar.getType.SecurityException="+e);
		} catch (NoSuchMethodException e) {
			logger.error("AutoAddScalar.getType.NoSuchMethodException="+e);
		}
		return rtobj;
	}
	
	/**
	 * 根據資料型態來取得相對應的Hibernate Type
	 * @param o
	 * @return
	 */
	public Type decideHibernateType(Object o){
		try {
			String type = o.toString().toUpperCase();
			int a = type.lastIndexOf(".");
			type = type.substring(a+1 , type.length());
			if(type.equals("BIGDECIMAL")){
				type = "BIG_DECIMAL";
			}
			if(type.equals("BIGINTEGER")){
				type = "BIG_INTEGER";
			}
			return (Type) Hibernate.class.getField(type).get(NullableType.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.debug("AutoAddScalar.decideHibernateType.Exception=>"+e);
			return null ;
		}
		
		
//		if(o.equals(String.class)){
//			return Hibernate.STRING;
//		}
//		if(o.equals(Integer.class)){
//			return Hibernate.INTEGER;
//		}
//		if(o.equals(Float.class)){
//			return Hibernate.FLOAT;
//		}
//		if(o.equals(Double.class)){
//			return Hibernate.DOUBLE;
//		}
//		if(o.equals(Long.class)){
//			return Hibernate.LONG;
//		}
//		if(o.equals(BigDecimal.class)){
//			return Hibernate.BIG_DECIMAL;
//		}
	}
}

