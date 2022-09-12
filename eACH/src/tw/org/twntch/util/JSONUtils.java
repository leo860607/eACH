package tw.org.twntch.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

//import org.apache.jackrabbit.commons.json.JsonParser;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;

public class JSONUtils {
	public static String map2json(Map m) {
		return JSONObject.fromObject(m).toString();
	}
	
	public static Map json2map(String json) {
		return JSONObject.fromObject(json);
	}
	
	public static String toJson(Object o) {
		if(o instanceof List)
			return JSONArray.fromObject(o).toString(); 
		else if(o instanceof Map)
			return JSONObject.fromObject(o).toString();
		else {
			try {
				Map m = BeanUtils.describe(o);
				return JSONObject.fromObject(m).toString();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		return "";
	}
	
	/**
	 * 產生JSON字串(以Html替代符號替換JSON字串內的單引號，以防網頁上以單引號括住的JSON字串被誤判)
	 */
	public static String toHtmlJson(Object o) {
		if(o instanceof List)
			return JSONArray.fromObject(o).toString().replaceAll("'", "&#039"); 
		else if(o instanceof Map)
			return JSONObject.fromObject(o).toString().replaceAll("'", "&#039");
		else {
			try {
				Map m = BeanUtils.describe(o);
				return JSONObject.fromObject(m).toString().replaceAll("'", "&#039");
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		return "";
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List toList(String json) {
		return new ArrayList(Arrays.asList(JSONArray.fromObject(json).toArray()));
		
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List toList(JSONArray json) {
		return new ArrayList(Arrays.asList(json.toArray()));
		
	}
	
	public static JSONObject getJsonObject(String json)
	{
		JSONObject obj = (JSONObject) JSONSerializer.toJSON( json );
		return obj;
	}
	
	public static JSONArray getContainKeys(JSONObject obj)
	{
		JSONArray jsonArrayNames  = obj.names();		
		return jsonArrayNames;
	}
	
	public static List getContainKeyList(String json)
	{
		return toList(getContainKeys(getJsonObject(json)));
	}
	
	public static JSONArray getContainArray(JSONObject jsonObj, String containKey)
	{
		JSONArray ar = null;
		if (jsonObj.isArray())
		{
			ar = jsonObj.getJSONArray(containKey);			
		}
		else
		{
			ar = new JSONArray();
			ar.add(jsonObj.get(containKey));
		}
		return ar;
	}
	
	public static List getContainList(String json, String containKey)
	{
		JSONObject obj = getJsonObject(json);
		JSONArray ar = getContainArray(obj, containKey);
		return toList(ar);
	}
	
	public static boolean isArray(JSONObject obj)
	{
		return obj.isArray();
	}
	
	public static boolean isArray(String json)
	{
		return json.startsWith("[");
	}
	
	/**
	 * 物件化陣列形式的 json 字串，只處理簡單型別
	 * @param json			json 字串
	 * @param rootClass    	物件類別
	 * @return				物件實例陣列
	 */
	public static Object[] toObject(String json, Class rootClass)
	{
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setArrayMode( JsonConfig.MODE_OBJECT_ARRAY );
		jsonConfig.setRootClass(rootClass);
		JSONArray jsonArray = JSONArray.fromObject(json);
		Object [] obj = (Object[]) JSONSerializer.toJava( jsonArray, jsonConfig );				
		return obj;
	}
	
	/**
	 * 物件化 json 字串，只處理簡單型別
	 * 當物件中有巢狀物件時無法正常處理，巢狀屬性請以字串型別設定，
	 * 再由物件本身另外處理
	 * @param json			json 字串
	 * @param rootClass		物件類別
	 * @return				物件實例 List
	 */
	public static List toObjectEx(String json, Class rootClass) 
	{
		Map jsonData = null;
		List beans = new ArrayList();
		
		if (isArray(json))
		{
			List list = JSONUtils.toList(json);
			// process every element in list
			for(int i=0; i < list.size(); i++)
			{
				JSONObject jo = (JSONObject) list.get(i);
				String s = (String) jo.toString();
				System.out.println("s=" + s);
				List beanList = toObjectEx(s, rootClass);
				
				//add beanList to parent list
				if (beanList != null)
				{
					for(int j=0; j < beanList.size(); j++)
					{
						beans.add(beanList.get(j));
					}
				}
			}
		}
		else
		{
			jsonData = JSONUtils.json2map(json);			
		}
		
		if (jsonData != null)
		{
			Set ks = jsonData.keySet();
			Iterator ir = ks.iterator();
			
			Object beanInstance = null;
			
	        try 
	        {
				beanInstance = rootClass.newInstance();
			} 
	        catch (Exception e) 
	        {
	        	//例外要如何處理 ?
				e.printStackTrace();
				return null;
			} 
			
	        Map map = new HashMap();
	        
			while(ir.hasNext())
			{
				String key = (String) ir.next();
				String value = null;
				JSONArray ja = null;
				Object obj = (Object) jsonData.get(key);
				List beanList = null;
				if (obj instanceof JSONArray)
				{
					ja = (JSONArray) obj;
					value = ja.toString();
					//
//					Object [] arr = toObject(value, rootClass);					
//					if (arr != null)
//					{
//						if (arr.length == 1)
//						{
//						}
//						else if (arr.length > 1)
//						{
//							for(int i=0; i < arr.length; i++)
//							{
//								//key = key + "[" + i + "]";  //再交由 BeanUtils 處理
//							}
//						}
//					}
					
					map.put(key, value);							
										
					// Collection 元素暫不處理
					beanList = toObjectEx(value, rootClass);					
					//map.put(key, beanList);
					
				}
				else if (obj instanceof String)
				{
					value = (String) obj;
					map.put(key, value);					
				}
				else
				{
					System.out.println("I don't know how to do with " + obj.getClass().toString());
				}
				System.out.println(key + "=" + value);
			}  //while
			
			try 
			{
				BeanUtils.populate(beanInstance, map);
								
				beans.add(beanInstance);
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
				return null;
			}
		}  //if jsonData
		
		
		return beans;
	}
	
	
	//add object to json string 
	public static String addObject(String json, String key, Object obj, Class rootClass)
	{
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setRootClass(FormDataBean.class);

		JSONObject o = JSONObject.fromObject(json, jsonConfig);
		o.accumulate(key, obj, jsonConfig);
		return  o.toString();
	}
	
	//remove json content
	public static String remove(String json, String key)
	{
		JSONObject o = JSONObject.fromObject(json);
		o.remove(key);
		
		return o.toString();
	}
	
	public static boolean isJsonList(String json)
	{
		try 
		{
			JSONUtils.toList(json);
			return true;
		} 
		catch (Exception e) 
		{
		    // Invalid.
			return false;
		}
	}
	
	

}



