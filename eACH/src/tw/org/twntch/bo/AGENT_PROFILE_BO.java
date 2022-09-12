package tw.org.twntch.bo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.util.LabelValueBean;

import tw.org.twntch.db.dao.hibernate.AGENT_PROFILE_Dao;
import tw.org.twntch.db.dao.hibernate.AGENT_SEND_PROFILE_Dao;
import tw.org.twntch.po.AGENT_PROFILE;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.zDateHandler;

public class AGENT_PROFILE_BO {
	private AGENT_PROFILE_Dao agent_profile_Dao;
	private EACHSYSSTATUSTAB_BO eachsysstatustab_bo;
	private AGENT_SEND_PROFILE_Dao agent_send_profile_Dao ;
	
	
	public boolean isActive(String startDate , String company_id){
		boolean result = false;
		String sql = " SELECT * FROM AGENT_PROFILE WHERE COMPANY_ID = ? ";
		String active_date ="";
		int tmp = -1;
		List<String> values = new ArrayList<String>();
		values.add(company_id);
		List<AGENT_PROFILE> data=agent_profile_Dao.getData(sql, values);
		if(data!= null && data.size() !=0){
			active_date = data.get(0).getACTIVE_DATE();
//			民國轉西元
			active_date = DateTimeUtils.convertDate(2, active_date, "yyyyMMdd", "yyyyMMdd");
			tmp = zDateHandler.compareDiffDate(startDate, active_date, "yyyyMMdd");
			System.out.println("startDate>>"+startDate);
			System.out.println("active_date>>"+active_date);
			System.out.println("tmp>>"+tmp);
			result = tmp>=0 ? true :result;
		}
		
		return result;
	}
	
	
	public List<LabelValueBean> getCompany_Id_List(){
		String bizdate = "";
		bizdate = eachsysstatustab_bo.getBusinessDate();
		System.out.println("bizdate>>"+bizdate);
//		List<AGENT_PROFILE> list = agent_profile_Dao.getData(sql.toString(), new ArrayList<String>());
//		List<AGENT_PROFILE> list = agent_profile_Dao.getCompany_Id_List(bizdate);
//		不過濾啟用日停用日 一律顯示
		List<AGENT_PROFILE> list = agent_profile_Dao.getCompany_Id_List();
		List<LabelValueBean> beanList = new LinkedList<LabelValueBean>();
		LabelValueBean bean = null;
		if(!( list == null || list.size() == 0 )){
			for(AGENT_PROFILE po : list){
				bean = new LabelValueBean(po.getCOMPANY_ID() + " - " + po.getCOMPANY_NAME(), po.getCOMPANY_ID());
				beanList.add(bean);
			}
		}else{
			bean = new LabelValueBean("===查無代理業者資料===", "");
			beanList.add(bean);
		}
		System.out.println("beanList>>"+beanList);
		return beanList;
	}
	/**
	 * 取得代理業者清單(ajax)
	 * @param params
	 * @return
	 */
	public String getCompany_Id_List(Map<String, String> params){
		List<LabelValueBean> beanList = getCompany_Id_List();
		Map rtnMap = new HashMap();
		if(beanList != null){
			rtnMap.put("result", "TRUE");
			rtnMap.put("msg", beanList);
		}else{
			rtnMap.put("result", "FALSE");
			rtnMap.put("msg", "無操作行資料");
		}
		return JSONUtils.map2json(rtnMap);
	}
	
	/**
	 * 共用下拉選單，代理業者ID及簡稱
	 * @return
	 */
	public List<LabelValueBean> getCompany_Id_ABBR_List(){
		String bizdate = "";
		bizdate = eachsysstatustab_bo.getBusinessDate();
		System.out.println("bizdate>>"+bizdate);
//		List<AGENT_PROFILE> list = agent_profile_Dao.getData(sql.toString(), new ArrayList<String>());
		List<AGENT_PROFILE> list = agent_profile_Dao.getCompany_Id_List(bizdate);
		List<LabelValueBean> beanList = new LinkedList<LabelValueBean>();
		LabelValueBean bean = null;
		for(AGENT_PROFILE po : list){
			bean = new LabelValueBean(po.getCOMPANY_ID() + " - " + po.getCOMPANY_ABBR_NAME(), po.getCOMPANY_ID());
			beanList.add(bean);
		}
		System.out.println("beanList>>"+beanList);
		return beanList;
	}
	
	/**
	 * 共用下拉選單，代理業者ID及簡稱(ajax)
	 * @param params
	 * @return
	 */
	public String getCompany_Id_ABBR_List(Map<String, String> params){
		List<LabelValueBean> beanList = getCompany_Id_ABBR_List();
		Map rtnMap = new HashMap();
		if(beanList != null){
			rtnMap.put("result", "TRUE");
			rtnMap.put("msg", beanList);
		}else{
			rtnMap.put("result", "FALSE");
			rtnMap.put("msg", "無操作行資料");
		}
		return JSONUtils.map2json(rtnMap);
	}
	
	public List<AGENT_PROFILE> search(Map<String, String> param){
		List<AGENT_PROFILE> list = null;
		
		try{
			List<String> conditions = new ArrayList<String>();
			List values = new ArrayList();
			String companyId = param.get("COMPANY_ID")==null?"":param.get("COMPANY_ID").trim();
			String companyName = param.get("COMPANY_NAME")==null?"":param.get("COMPANY_NAME").trim();
			
			if(StrUtils.isNotEmpty(companyId)){
				conditions.add(" COMPANY_ID = ? ");
				values.add(companyId);
			}
			if(StrUtils.isNotEmpty(companyName)){
				conditions.add(" COMPANY_NAME LIKE ? ");
				values.add("%" + java.net.URLDecoder.decode(companyName.trim(), "UTF-8") + "%");
			}
			String condition = combine(conditions);
			
			String sord = StrUtils.isNotEmpty(param.get("sord"))? param.get("sord"):"";
			String sidx = StrUtils.isNotEmpty(param.get("sidx"))? param.get("sidx"):"";
			String orderSQL = StrUtils.isNotEmpty(sidx)? " ORDER BY "+sidx+" "+sord:"";
			
			String sql = "SELECT * FROM AGENT_PROFILE " + (StrUtils.isEmpty(condition)?"":"WHERE " + condition) + orderSQL;
			
			list = agent_profile_Dao.getData(sql, values);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return list;
	}
	
	public String search_toJson(Map<String, String> param){
		return JSONUtils.toJson(search(param));
	}
	
	public Map<String,String> save(Map formMap){
		Map<String, String> map = null;
		try {
			map = new HashMap<String, String>();
			AGENT_PROFILE po = agent_profile_Dao.get((String) formMap.get("COMPANY_ID"));
			if(po != null ){
				map.put("result", "FALSE");
				map.put("msg", "儲存失敗，id重複");
				map.put("target", "add_p");
				return map;
			}
			po = new AGENT_PROFILE();
			BeanUtils.populate(po, formMap);
//			if(StrUtils.isEmpty(po.getACTIVE_DATE())){
//				po.setACTIVE_DATE(null);
//			}else{
//				po.setACTIVE_DATE(DateTimeUtils.convertDate(po.getACTIVE_DATE(), "yyyyMMdd", "yyyy-MM-dd"));
//			}
//			if(StrUtils.isEmpty(po.getSTOP_DATE())){
//				po.setSTOP_DATE(null);
//			}else{
//				po.setSTOP_DATE(DateTimeUtils.convertDate(po.getSTOP_DATE(), "yyyyMMdd", "yyyy-MM-dd"));
//			}
			
			agent_profile_Dao.save(po);
			map.put("result", "TRUE");
			map.put("msg", "儲存成功");
			map.put("target", "search");
		}catch (Exception e){
			e.printStackTrace();
			map.put("result", "ERROR");
			map.put("msg", "儲存失敗，系統異常:"+e);
			map.put("target", "add_p");
			return map;
		}
		return map;
	}
	
	public Map<String,String> delete(Map formMap){
		String com_id = (String) formMap.get("COMPANY_ID");
		Map<String, String> map = null;
		Map<String, String> pkmap = new HashMap<String,String>();//e
		AGENT_PROFILE po = null;
		try {
			map = new HashMap<String, String>();
			pkmap.put("COMPANY_ID", com_id);
			po = agent_profile_Dao.get(com_id);
			if(po == null){
				map = agent_profile_Dao.removeFail(po, pkmap, "刪除失敗，查無資料", 1);
				return map;
			}
			String sql = "SELECT * FROM AGENT_SEND_PROFILE WHERE COMPANY_ID = ? ";
			List<String> values = new LinkedList<String>();
			values.add(com_id);
			List<Map> dataList  = agent_send_profile_Dao.getDataRTMap(sql, values);
			if(dataList != null && dataList.size() != 0){
				map.put("result", "FALSE");
				map.put("msg","代理業者統一編號 : " + com_id + " 已建立關連檔，無法刪除");
			}else{
				po.setCOMPANY_ID(com_id);
				agent_profile_Dao.removeII(po, pkmap);
				map.put("result", "TRUE");
				map.put("msg", "刪除成功");
				map.put("target", "search");
			}
		} catch (Exception e) {
			e.printStackTrace();
			map = agent_profile_Dao.removeFail(po, pkmap, "刪除失敗，系統異常", 2);
			return map;
		}
		return map;
	}
	
	public Map<String,String> update(Map formMap){
		Map<String, String> map = null;
		Map<String, String> pkmap = new HashMap<String, String>();//e
		Map<String, String> oldmap = new HashMap<String, String>();//e
		AGENT_PROFILE po = null;
		try {
			map = new HashMap<String, String>();
			String companyId = (String) formMap.get("COMPANY_ID");
			po = agent_profile_Dao.get(companyId);
			pkmap.put("COMPANY_ID", companyId);
			if(po == null ){
				po = new AGENT_PROFILE();
				BeanUtils.populate(po, formMap);
				map = agent_profile_Dao.updateFail(po, oldmap, pkmap, "儲存失敗，查無資料", 1);
				return map;
			}
//			po.setCOMPANY_ID(companyId);
			
			oldmap = BeanUtils.describe(po);
			BeanUtils.populate(po, formMap);
//			if(StrUtils.isEmpty(po.getACTIVE_DATE())){
//				po.setACTIVE_DATE(null);
//			}else{
//				po.setACTIVE_DATE(DateTimeUtils.convertDate(po.getACTIVE_DATE(), "yyyyMMdd", "yyyy-MM-dd"));
//			}
//			if(StrUtils.isEmpty(po.getSTOP_DATE())){
//				po.setSTOP_DATE(null);
//			}else{
//				po.setSTOP_DATE(DateTimeUtils.convertDate(po.getSTOP_DATE(), "yyyyMMdd", "yyyy-MM-dd"));
//			}
			agent_profile_Dao.saveII(po, oldmap, pkmap);
			map.put("result", "TRUE");
			map.put("msg", "儲存成功");
			map.put("target", "search");
		}catch (Exception e){
			e.printStackTrace();
			agent_profile_Dao.updateFail(po, oldmap, pkmap, "儲存失敗，查無資料", 1);
			return map;
		}
		return map;
	}
	
	public String combine(List<String> conditions){
		String conStr = "";
		for(int i = 0 ; i < conditions.size(); i++){
			conStr += conditions.get(i);
			if(i < conditions.size() - 1){
				conStr += " AND ";
			}
		}
		return conStr;
	}
	
	public AGENT_PROFILE_Dao getAgent_profile_Dao() {
		return agent_profile_Dao;
	}

	public void setAgent_profile_Dao(AGENT_PROFILE_Dao agent_profile_Dao) {
		this.agent_profile_Dao = agent_profile_Dao;
	}

	public EACHSYSSTATUSTAB_BO getEachsysstatustab_bo() {
		return eachsysstatustab_bo;
	}

	public void setEachsysstatustab_bo(EACHSYSSTATUSTAB_BO eachsysstatustab_bo) {
		this.eachsysstatustab_bo = eachsysstatustab_bo;
	}

	public AGENT_SEND_PROFILE_Dao getAgent_send_profile_Dao() {
		return agent_send_profile_Dao;
	}

	public void setAgent_send_profile_Dao(
			AGENT_SEND_PROFILE_Dao agent_send_profile_Dao) {
		this.agent_send_profile_Dao = agent_send_profile_Dao;
	}
	
	
	
}
