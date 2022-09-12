package tw.org.twntch.util;


import java.io.Serializable;
import java.util.Map;


//測試 JSON 資料：
//{"taskId":"0","status":"0","formInfo":[{"id":["trancode"],"value":[""],"text":[],"name":"trancode"},{"id":["component"],"value":["prjApplyBo"],"text":[],"name":"component"},{"id":["method"],"value":["apply"],"text":[],"name":"method"},{"id":["pureJson"],"value":["true"],"text":[],"name":"pureJson"},{"id":["applierAccount"],"value":["sharon"],"text":[],"name":"applierAccount"},{"id":["applierUnitNo"],"value":["3100"],"text":[],"name":"applierUnitNo"},{"id":["applierUnitName"],"value":["\u8cc7\u8a0a\u6574\u5408\u696d\u52d9\u8655\u696d\u52d9\u90e8"],"text":[],"name":"applierUnitName"},{"id":["applierName"],"value":["\u8521\u7394\u7489"],"text":[],"name":"applierName"},{"id":["applierTitle"],"value":[""],"text":[],"name":"applierTitle"},{"id":["applierEmail"],"value":["andy@fstop.com.tw"],"text":[],"name":"applierEmail"},{"id":["userAccount"],"value":["cherry"],"text":[],"name":"userAccount"},{"id":["userUnitNo"],"value":["2000"],"text":[],"name":"userUnitNo"},{"id":["userUnitName"],"value":["\u6280\u8853\u670d\u52d9\u8655"],"text":[],"name":"userUnitName"},{"id":["userName"],"value":["\u9673\u9326\u6167"],"text":[],"name":"userName"},{"id":["userTitle"],"value":[""],"text":[],"name":"userTitle"},{"id":["userEmail"],"value":["andy@fstop.com.tw"],"text":[],"name":"userEmail"},{"id":["sysDate"],"value":["2012/02/01"],"text":[],"name":"sysDate"},{"id":["attachList"],"value":[""],"text":[],"name":"attachList"},{"id":["signList"],"value":[""],"text":[],"name":"signList"},{"id":["flowId"],"value":["prjapply"],"text":[],"name":"flowId"},{"id":["flowName"],"value":["\u5c08\u6848\u7533\u8acb\u55ae"],"text":[],"name":"flowName"},{"id":["flowState"],"value":["2"],"text":[],"name":"flowState"},{"id":["caseSN"],"value":["225"],"text":[],"name":"caseSN"},{"id":["processData"],"value":[""],"text":[],"name":"processData"},{"id":["nextUserAccount"],"value":["andy"],"text":[],"name":"nextUserAccount"},{"id":["pmId"],"value":[""],"text":[],"name":"pmId"},{"id":["prj_state_value"],"value":["00"],"text":[],"name":"prj_state_value"},{"id":["prj_key_id"],"value":["351"],"text":[],"name":"prj_key_id"},{"id":["newcase"],"value":["n"],"text":[],"name":"prjcase"},{"id":["apply_dep"],"value":["\u8cc7\u8a0a\u6574\u5408\u696d\u52d9\u8655\u696d\u52d9\u90e8"],"text":[],"name":"apply_dep"},{"id":["applier"],"value":["\u8521\u7394\u7489"],"text":[],"name":"applier"},{"id":["salse_dep"],"value":["105"],"text":["\u8cc7\u8a0a\u6574\u5408\u696d\u52d9\u8655\u696d\u52d9\u90e8"],"name":"salse_dep"},{"id":["salse"],"value":["29"],"text":["\u8521\u7394\u7489-andy@fstop.com.tw"],"name":"salse"},{"id":["cust_type"],"value":["T"],"text":["\u9f0e\u76db"],"name":"cust_type"},{"id":["cust_name"],"value":["1"],"text":["TOP"],"name":"cust_name"},{"id":["prj_name"],"value":["\u9280\u884c\u7db2\u9280\u7cfb\u7d71"],"text":[],"name":"prj_name"},{"id":["prj_dep"],"value":["126"],"text":["\u6280\u8853\u670d\u52d9\u8655\u7522\u54c1\u7814\u767c\u90e8"],"name":"prj_dep"},{"id":["prj_sdate"],"value":["2012/02/06"],"text":[],"name":"prj_sdate"},{"id":["prj_edate"],"value":["2012/02/28"],"text":[],"name":"prj_edate"},{"id":["prj_state"],"value":["\u7533\u8acb"],"text":[],"name":"prj_state"},{"id":["prj_property"],"value":["2"],"text":["\u958b\u767c/\u6574\u5408"],"name":"prj_property"},{"id":["contract_date"],"value":["2012/02/22"],"text":[],"name":"contract_date"},{"id":["prj_ccy"],"value":["TWD"],"text":["\u53f0\u5e63"],"name":"prj_ccy"},{"id":["contract_amt"],"value":["12,345"],"text":[],"name":"contract_amt"},{"id":["prj_scale"],"value":["1"],"text":[],"name":"prj_scale"},{"id":["prj_tech"],"value":["Java"],"text":["Java"],"name":"prj_tech"},{"id":["prj_platform"],"value":["Windows"],"text":["Windows"],"name":"prj_platform"},{"id":["prj_desc"],"value":["xxxxxxx"],"text":[],"name":"prj_desc"},{"id":["prj_type"],"value":["3"],"text":["\u7522\u54c1\u578b\u5c08\u6848"],"name":"prj_type"},{"id":["ctrl"],"value":["1"],"text":[],"name":"isctrl"},{"id":["pm"],"value":[""],"text":[],"name":"pm"},{"id":["kickoff_date1"],"value":[""],"text":[],"name":"kickoff_date1"},{"id":["kickoff_date2"],"value":[""],"text":[],"name":"kickoff_date2"},{"id":["check_period1"],"value":["2"],"text":[],"name":"check_period1"},{"id":["check_period2"],"value":[""],"text":[],"name":"check_period2"},{"id":["upload-name0"],"value":["b3df0f1b54d34ab4b3f23a46532b8bb6-memo.txt"],"text":[],"name":"upload-name0"},{"id":["upload-size0"],"value":["17252"],"text":[],"name":"upload-size0"},{"id":["upload-user0"],"value":["sharon"],"text":[],"name":"upload-user0"},{"id":["comment_type"],"value":["2"],"text":["\u540c\u610f"],"name":"comment_type"},{"id":["comment_desc"],"value":["\u8acb\u6307\u6d3e\u5c08\u6848\u7d93\u7406"],"text":[],"name":"comment_desc"},{"id":["take_case"],"value":["take_case"],"text":[],"name":"take_case[]"}],"uploadInfo":[],"signInfo":[{"userAccount":"william_lee","userName":"\u674e\u5049\u96c4","userTitle":"","userUnitNo":"3000","userUnitName":"\u8cc7\u8a0a\u6574\u5408\u696d\u52d9\u8655","email":"andy@fstop.com.tw","signDate":"2012/02/01","result":"2","comment":"ok"},{"userAccount":"cherry","userName":"\u9673\u9326\u6167","userTitle":"","userUnitNo":"2000","userUnitName":"\u6280\u8853\u670d\u52d9\u8655","email":"andy@fstop.com.tw","signDate":"2012/02/01","result":"2","comment":"\u8acb\u6307\u6d3e\u5c08\u6848\u7d93\u7406"}]}


public class FormDataBean implements Serializable
{
	private String name;
	private String [] id;
	private String [] value;
	private String [] text;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String[] getId() {
		return id;
	}
	
	
	public void setId(String[] id) {
		this.id = id;
	}
	
	public void setId(String id)
	{
		String temp [];
		if (this.id == null)
		{
			temp = new String[1];
			this.id = new String[1];
		}
		else
		{
			temp = new String[this.id.length + 1];
		}
		
		for(int i=0; i < this.id.length; i++)
		{
			temp[i] = this.id[i];
		}
		temp[temp.length - 1] = id;	
		this.id = temp;
	}
	
	public String[] getValue() {
		return value;
	}
	public void setValue(String[] value) {
		this.value = value;
	}
	
	public void setValue(String value)
	{
		String temp [];
		if (this.value == null)
		{
			temp = new String[1];
			this.value = new String[1];
		}
		else
		{
			temp = new String[this.value.length + 1];
		}
		
		for(int i=0; i < this.value.length; i++)
		{
			temp[i] = this.value[i];
		}
		temp[temp.length - 1] = value;
		this.value = temp;
	}
	
	public String[] getText() {
		return text;
	}
	public void setText(String[] text) {
		this.text = text;
	}
	
	public void setValueById(String id, String value)
	{
		if (this.id == null) 
		{
			System.out.println("setValueById id=null");
			return;
		}
		
		for(int i=0; i < this.id.length; i++)
		{
			if (this.id[i].equals(id))
			{
				if (this.value != null && this.value.length > i)
				{
					this.value[i] = value;
					return;
				}
			}
		}
	}
	public void setTextById(String id, String text)
	{
		if (this.id == null) 
		{
			System.out.println("setTextById id=null");
			return;
		}
		
		for(int i=0; i < this.id.length; i++)
		{
			if (this.id[i].equals(id))
			{
				if (this.text != null && this.text.length > i)
				{
					this.text[i] = text;
					return;
				}
			}
		}
	}
	
	
	public static enum Option
	{
		BY_NAME,
		BY_ID,
		BY_VALUE,
		BY_TEXT
	}
	
	@SuppressWarnings("rawtypes")
	public static FormDataBean[] getFormDataBeans(String json, String key)
	{
		Map jsonData = JSONUtils.json2map(json);	
		//System.out.println("getFormDataBeans json=" + json);
		String j = jsonData.get(key).toString();
		//System.out.println("getFormDataBeans=" + j);
		FormDataBean [] formInfo = (FormDataBean[]) JSONUtils.toObject(jsonData.get(key).toString(), FormDataBean.class);		
		return formInfo;
	}
	
	//取得指定的 form 資料
	//未來考慮效能，可以改為用 hash 索引
	public static FormDataBean find(String data, FormDataBean [] bean, Option opt)
	{
		for(FormDataBean f : bean)
		{
			if (opt == Option.BY_NAME) //以 form name 來找
			{
				if (data.equals(f.getName()))
				{
					return f;
				}
				else
				{
					continue;
				}
			}
			else if (opt == Option.BY_ID) //以 id 來找
			{
				for(String s : f.getId())
				{
					if (data.equals(s))
					{
						return f;
					}
				}						
			}
			else if (opt == Option.BY_VALUE) //以 value 來找
			{
				for(String s : f.getValue())
				{
					if (data.equals(s))
					{
						return f;
					}
				}										
			}
			else if (opt == Option.BY_TEXT) //以 text 來找
			{
				for(String s : f.getText())
				{
					if (data.equals(s))
					{
						return f;
					}
				}														
			}
			else
			{
				
			}
		}
		return null;
	}
	
	public static int getIndex(String data, FormDataBean [] bean, Option opt)
	{
		int ret = -1;
		
		for(FormDataBean f : bean)
		{
			ret++;
			if (opt == Option.BY_NAME) //以 form name 來找
			{
				if (data.equals(f.getName()))
				{
					return ret;
				}
				else
				{
					continue;
				}
			}
			else if (opt == Option.BY_ID) //以 id 來找
			{
				for(String s : f.getId())
				{
					if (data.equals(s))
					{
						return ret;
					}
				}						
			}
			else if (opt == Option.BY_VALUE) //以 value 來找
			{
				for(String s : f.getValue())
				{
					if (data.equals(s))
					{
						return ret;
					}
				}										
			}
			else if (opt == Option.BY_TEXT) //以 text 來找
			{
				for(String s : f.getText())
				{
					if (data.equals(s))
					{
						return ret;
					}
				}														
			}
			else
			{
				
			}
		}
		return ret;		
	}
	
	
	public static String getFormDataById(String json, String key, String id)
	{
		//Map jsonData = JSONUtils.json2map(json);			
		//FormDataBean [] formInfo = (FormDataBean[]) JSONUtils.toObject(jsonData.get(key).toString(), FormDataBean.class);
		FormDataBean [] formInfo = getFormDataBeans(json, key);
		FormDataBean data = null;
		
		data = FormDataBean.find(id, formInfo, FormDataBean.Option.BY_ID);
		if (data == null)
		{
			return null;
		}
		return data.getValue()[0];
	}

	
	public static String setFormDataById(String json, String key, String id, String value)
	{
		String ret = json;
		
		FormDataBean [] beans = getFormDataBeans(json, key);
		int index = FormDataBean.getIndex(id, beans, FormDataBean.Option.BY_ID);
		
		if (index == -1)
		{
			return ret;
		}
		
		FormDataBean target =  beans[index];

		target.setValueById(id, value);
		
		beans[index] = target;
		
		ret = JSONUtils.remove(json, key);
		
		for(int i=0; i < beans.length; i++)
		{
			ret = JSONUtils.addObject(ret, key, beans[i], FormDataBean.class);
		}
		
		System.out.println("setFormDataById=" + ret);
		return ret;
	}
	
}

