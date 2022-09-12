package tw.org.twntch.bo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import tw.org.twntch.db.dao.hibernate.AGENT_CR_LINE_Dao;
import tw.org.twntch.po.AGENT_CR_LINE;
import tw.org.twntch.po.AGENT_FEE_CODE;
import tw.org.twntch.po.CR_LINE;
import tw.org.twntch.po.CR_LINE_LOG;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.zDateHandler;

public class AGENT_CR_LINE_BO {

	private AGENT_CR_LINE_Dao agent_cr_line_Dao ;
	

public Map<String,String> delete(String id ){
	Map<String, String> map = null;
	Map<String, String> pkmap = new HashMap<String, String>();//e
	AGENT_CR_LINE po = null ;
	try {
		map = new HashMap<String, String>();
//		List list = agent_cr_line_Dao.find(" FROM tw.org.twntch.po.BANK_GROUP WHERE CTBK_ID = ? ", id);
//		if(list != null && list.size() != 0){
//			map.put("result", "FALSE");
//			map.put("msg", "該銀行是清算行，不可刪除");
//			map.put("target", "edit_p");
//			return map;
//		}
		po = agent_cr_line_Dao.get(id);
		pkmap.put("SND_COMPANY_ID", id);
		if(po == null ){
			map.put("result", "FALSE");
			map.put("msg", "刪除失敗，查無資料");
			map.put("target", "edit_p");
			return map;
		}
		agent_cr_line_Dao.removeII(po, pkmap);;
		map.put("result", "TRUE");
		map.put("msg", "刪除成功");
		map.put("target", "search");
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		map.put("result", "ERROR");
		map.put("msg", "刪除失敗，系統異常:"+e);
		map.put("target", "edit_p");
		return map;
	}
	return map;
}

	public Map<String,String> update(String id  ,Map formMap){
		Map<String, String> map = null;
		AGENT_CR_LINE po = null ;
		Map<String, String> pkmap = new HashMap<String, String>();//e
		Map<String, String> oldmap = new HashMap<String, String>();//e
		try {
			map = new HashMap<String, String>();
			po = agent_cr_line_Dao.get(id);
			pkmap.put("BANK_ID", id);
			if(po == null ){
				map.put("result", "FALSE");
				map.put("msg", "更新失敗，查無資料");
				map.put("target", "edit_p");
				BeanUtils.populate(po, formMap);
//				map = agent_cr_line_Dao.updateFail(po, oldmap, pkmap, "儲存失敗，查無資料", 1);
				return map;
			}
			oldmap = BeanUtils.describe(po);
			String tmpdate = po.getCDATE();
			BeanUtils.populate(po, formMap);
			po.setUDATE(zDateHandler.getTheDateII());
			po.setCDATE(tmpdate);
			agent_cr_line_Dao.saveII(po, oldmap, pkmap);
			map.put("result", "TRUE");
			map.put("msg", "儲存成功");
			map.put("target", "search");
		}catch (Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();
			map.put("result", "ERROR");
			map.put("msg", "更新失敗，系統異常:"+e);
			map.put("target", "edit_p");
//			map = agent_cr_line_Dao.updateFail(po, oldmap, pkmap, "儲存失敗，系統異常", 2);
			return map;
		}
		return map;
	}

	
	public Map<String,String> save(String id ,Map formMap){
		Map<String, String> map = null;
		AGENT_CR_LINE po = null;
		Map<String, String> pkmap = new HashMap<String, String>();//e
		try {
			map = new HashMap<String, String>();
			po  = agent_cr_line_Dao.get(id);
			pkmap.put("SND_COMPANY_ID", id);
			if(po != null){
				map.put("result", "FALSE");
				map.put("msg", "儲存失敗，已有資料");
				map.put("target", "add_p");
				return map;
			}
			po = new AGENT_CR_LINE();
			BeanUtils.populate(po, formMap);
			po.setCDATE(zDateHandler.getTheDateII());
			agent_cr_line_Dao.save(po);
			map.put("result", "TRUE");
			map.put("msg", "儲存成功");
			map.put("target", "search");
		}catch (Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();
			map.put("result", "ERROR");
			map.put("msg", "儲存失敗，系統異常:"+e);
			map.put("target", "add_p");
//			map = agent_cr_line_Dao.saveFail(po,pkmap, "儲存失敗，系統異常" ,2);
			return map;
		}
		System.out.println("save.map"+map);
		return map;
	}
	

	public String search_toJson(Map<String, String> params){
		String json = "{}";
		List<AGENT_CR_LINE> list = null;
		try {
			String conditionKey = "[\"BASIC_CR_LINE\",\"REST_CR_LINE\",\"SND_COMPANY_ID\",\"COMPANY_NAME\"]";
			Map map = getConditionData(params, conditionKey);
			String sord =StrUtils.isNotEmpty(params.get("sord"))? params.get("sord"):"";
			String sidx =StrUtils.isNotEmpty(params.get("sidx"))? params.get("sidx"):"";
			String orderSQL = StrUtils.isNotEmpty(sidx)? " ORDER BY "+sidx+" "+sord:"";
			list = search(map.get("sqlPath").toString(),map.get("sqlPath2").toString(), orderSQL , (List<String>) map.get("values"));
			
			if(list != null && list.size() > 0){
				json = JSONUtils.toJson(list);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("json>>" + json);
		return json;
	}
	

	public Map getConditionData(Map<String,String> params , String conditionKey){
		List<String> keyList = JSONUtils.toList(conditionKey);
		List<String> values = new LinkedList<String>();
		Map retMap = new HashMap();
		StringBuffer sql = new StringBuffer();
		StringBuffer sql2 = new StringBuffer();
		BigDecimal tmp = 	null;
		int i = 0 , j=0;
		try {
			for(String key :params.keySet()){
				if(keyList.contains(key)){
					if(StrUtils.isNotEmpty(params.get(key))){
						if(i==0){sql.append(" WHERE ");}
						if(i!=0){sql.append(" AND ");}
						
						if(key.equals("COMPANY_NAME")){
							if(j==0){sql2.append(" WHERE ");}
							if(j!=0){sql2.append(" AND ");}
							sql2.append(key+" LIKE ?");
							values.add("%" + params.get(key) + "%");
							j++;
						}else if(key.equals("BASIC_CR_LINE")) {
							tmp = 	new BigDecimal(params.get(key));
							tmp = tmp.divide(new BigDecimal(100));
							sql.append( " REST_CR_LINE <  ( "+key+" * "+tmp +" ) ");
//							values.add(key+" * "+tmp);
							i++;
						}else if(key.equals("REST_CR_LINE")) {
							sql.append( key+" < ? ");
							values.add(params.get(key));
							i++;
						}else{
							sql.append( key+" = ? ");
							values.add(params.get(key));
							i++;
						}
					}
				}
			}
			retMap.put("sqlPath", sql.toString());
			retMap.put("sqlPath2", sql2.toString());
			retMap.put("values", values);
			System.out.println("retMap>>"+retMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retMap;
	}
	

	public List<AGENT_CR_LINE> search(String sqlPath, String sqlPath2,String orderSQL  , List<String> values){
		List<AGENT_CR_LINE> list = null;
		StringBuffer sql = new StringBuffer();
		try{
			sql.append(" WITH TEMP AS ( ");
			sql.append(" SELECT COALESCE (SND_COMPANY_ID,'') AS SND_COMPANY_ID , COALESCE (BASIC_CR_LINE ,0) AS BASIC_CR_LINE, COALESCE (REST_CR_LINE ,0) AS REST_CR_LINE ");
			sql.append("  , COALESCE (GETCOMPANY_ABBR(SND_COMPANY_ID),'') AS COMPANY_NAME ");
			sql.append("  , COALESCE ((SELECT COMPANY_ID  FROM AGENT_SEND_PROFILE A WHERE A.SND_COMPANY_ID = AGENT_CR_LINE.SND_COMPANY_ID  FETCH FIRST 1 ROW ONLY ) , '' ) AS COMPANY_ID    ");
			sql.append("  , COALESCE (( SELECT (SELECT COMPANY_NAME FROM AGENT_PROFILE AP WHERE AP.COMPANY_ID = A.COMPANY_ID)   FROM AGENT_SEND_PROFILE A WHERE A.SND_COMPANY_ID = AGENT_CR_LINE.SND_COMPANY_ID  FETCH FIRST 1 ROW ONLY ), '' ) AS TMP_COMPANY_NAME   ");
			sql.append(" FROM AGENT_CR_LINE ");
			sql.append(sqlPath);
			sql.append(" ) ");
			sql.append(" SELECT * FROM TEMP ");
			sql.append(sqlPath2);
			sql.append(orderSQL);
			System.out.println("sql>>"+sql);
			list = agent_cr_line_Dao.getData(sql.toString(), values);
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("list>>"+list);
		return list;
	}
	
	public List<AGENT_CR_LINE> search(String snd_company_id){
		List<AGENT_CR_LINE> list = null;
		List<String> values = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		try{
			sql.append(" WITH TEMP AS ( ");
			sql.append(" SELECT COALESCE (SND_COMPANY_ID,'') AS SND_COMPANY_ID , COALESCE (BASIC_CR_LINE ,0) AS BASIC_CR_LINE, COALESCE (REST_CR_LINE ,0) AS REST_CR_LINE ");
			sql.append("  , COALESCE (GETCOMPANY_ABBR(SND_COMPANY_ID),'') AS COMPANY_NAME ");
			sql.append(" FROM AGENT_CR_LINE ");
			sql.append(" WHERE SND_COMPANY_ID = ? ");
			sql.append(" ) ");
			sql.append(" SELECT * FROM TEMP ");
			System.out.println("sql>>"+sql);
			values.add(snd_company_id);
			list = agent_cr_line_Dao.getData(sql.toString(), values);
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("list>>"+list);
		return list;
	}
	public boolean chkIsEx(String snd_company_id){
		boolean result = false;
		List<AGENT_CR_LINE> list = null ;
		try {
			list  = search(snd_company_id);
			if(list != null && list.size()!=0){
				result = true ;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public AGENT_CR_LINE_Dao getAgent_cr_line_Dao() {
		return agent_cr_line_Dao;
	}

	public void setAgent_cr_line_Dao(AGENT_CR_LINE_Dao agent_cr_line_Dao) {
		this.agent_cr_line_Dao = agent_cr_line_Dao;
	} 
	
	
	
}
