package tw.org.twntch.bo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.struts.util.LabelValueBean;

import com.google.gson.Gson;

import tw.org.twntch.db.dao.hibernate.EACH_USERLOG_Dao;
import tw.org.twntch.db.dao.hibernate.PI_COMPANY_PROFILE_Dao;
import tw.org.twntch.db.dao.hibernate.PI_SND_PROFILE_Dao;
import tw.org.twntch.db.dao.hibernate.SC_COMPANY_PROFILE_Dao;
import tw.org.twntch.po.PI_COMPANY_PROFILE;
import tw.org.twntch.po.PI_COMPANY_PROFILE_PK;
import tw.org.twntch.po.PI_SND_PROFILE;
import tw.org.twntch.po.PI_SND_PROFILE_PK;
import tw.org.twntch.util.BeanUtils;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.RptUtils;
import tw.org.twntch.util.StrUtils;

public class PI_SND_PROFILE_BO {
	private PI_COMPANY_PROFILE_Dao pi_company_profile_Dao;
	private PI_SND_PROFILE_Dao pi_snd_profile_Dao;
	private SC_COMPANY_PROFILE_Dao sc_com_Dao;
	private EACH_USERLOG_Dao userLog_Dao ;
	private BANK_GROUP_BO  bank_group_bo ;
	private EACH_USERLOG_BO userlog_bo;
	
	

	
	public List<PI_COMPANY_PROFILE> getPI_CompanyData(){
		PI_COMPANY_PROFILE po = null ;
		po = pi_company_profile_Dao.getPI_CompanyDataByCompanyId("");
		return null;
		
	}
	public List<LabelValueBean> getPI_IdList(){
		List<LabelValueBean> beanList = new LinkedList<LabelValueBean>();
		String sql = " SELECT DISTINCT PI_COMPANY_ID , PI_COMPANY_NAME FROM PI_COMPANY_PROFILE ";
		List<Map> list = pi_company_profile_Dao.getDataRTMap(sql, null);
		LabelValueBean bean = null;
		for(Map<String,String> map : list){
				bean = new LabelValueBean(map.get("PI_COMPANY_ID") + " - " + map.get("PI_COMPANY_NAME"), map.get("PI_COMPANY_ID"));
				beanList.add(bean);
		}
		System.out.println("getPI_IdList>>"+beanList);
		return beanList;
		
	}
	
	
	public String getCompanyDataByCompanyId(Map<String, String> params){
		String companyId = params.get("SND_COMPANY_ID")==null?"":params.get("SND_COMPANY_ID");
//		PI_COMPANY_PROFILE po = null ;
		PI_SND_PROFILE po = null ;
		String companyData = "";
		Map map = new HashMap();
		try{
			po = (PI_SND_PROFILE) sc_com_Dao.getCompanyDataByCompanyIdOnly930(companyId, new PI_SND_PROFILE() );
			
			if(po != null){
				map = tw.org.twntch.util.BeanUtils.describe(po);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		companyData = JSONUtils.map2json(map);
		return companyData;
	}
	
	public List<LabelValueBean> getIdListByBillType(){
		List<LabelValueBean> beanList = new LinkedList<LabelValueBean>();
		String sql = " SELECT * FROM BILL_TYPE ";
		List<Map> list = pi_company_profile_Dao.getDataRTMap(sql, null);
		LabelValueBean bean = null;
		for(Map<String,String> map : list){
				bean = new LabelValueBean(map.get("BILL_TYPE_ID") + " - " + map.get("BILL_TYPE_NAME"), map.get("BILL_TYPE_ID"));
				beanList.add(bean);
		}
		System.out.println("BillTypeList>>"+beanList);
		return beanList;
	}
	public String getBillIdListByPI(Map<String, String> params){
		List<LabelValueBean> beanList = new LinkedList<LabelValueBean>();
		String json = "";
		String companyId = params.get("PI_COMPANY_ID")==null?"":params.get("PI_COMPANY_ID");
		String sql = " SELECT A.*,(SELECT BILL_TYPE_NAME FROM BILL_TYPE WHERE A.BILL_TYPE_ID = BILL_TYPE_ID) BILL_TYPE_NAME FROM PI_COMPANY_PROFILE A WHERE A.PI_COMPANY_ID = ?";
		List<String> values = new ArrayList<String>();
		Gson gs = new Gson();
		Map rtnMap = new HashMap();
		values.add(companyId);
		List<Map> list = pi_company_profile_Dao.getDataRTMap(sql, values);
		LabelValueBean bean = null;
		for(Map<String,String> map : list){
			bean = new LabelValueBean(map.get("BILL_TYPE_ID") + " - " + map.get("BILL_TYPE_NAME"), map.get("BILL_TYPE_ID"));
			beanList.add(bean);
		}
		
		System.out.println("BillTypeList>>"+beanList);
		if(beanList != null){
			rtnMap.put("result", "TRUE");
			rtnMap.put("msg", beanList);
		}else{
			rtnMap.put("result", "FALSE");
			rtnMap.put("msg", "無繳費類別資料");
		}
		
		json = gs.toJson(rtnMap);
		System.out.println("json>>"+json);
		return json;
	}
	/**
	 * 取得收費業者資料檔的id及名稱
	 * @return
	 */
	public List<LabelValueBean> getIdListFromPI_Company(){
		List<LabelValueBean> beanList = new LinkedList<LabelValueBean>();
		String sql = " SELECT * FROM PI_COMPANY_PROFILE ";
		List<Map> list = pi_company_profile_Dao.getDataRTMap(sql, null);
		LabelValueBean bean = null;
		for(Map<String,String> map : list){
			bean = new LabelValueBean(map.get("PI_COMPANY_ID") + " - " + map.get("PI_COMPANY_NAME"), map.get("PI_COMPANY_ID"));
			beanList.add(bean);
		}
		System.out.println("BillTypeList>>"+beanList);
		return beanList;
	}
	
	public Map<String,String> save(Map formMap){
		Map<String, String> map = null;
		try {
			System.out.println("formMap>>"+formMap);
			map = new HashMap<String, String>();
			String company_id = (String)formMap.get("PI_COMPANY_ID");
			String snd_company_id = (String)formMap.get("SND_COMPANY_ID");
			String bill_type_id = (String)formMap.get("BILL_TYPE_ID");
			PI_SND_PROFILE_PK id = new PI_SND_PROFILE_PK(company_id,snd_company_id,bill_type_id);
			PI_SND_PROFILE po = pi_snd_profile_Dao.get(id);
//			啟用日期，要檢核是否小於營業日
			String twDate = (String) formMap.get("START_DATE");
			String twDateCheck = "0"+String.valueOf(Integer.parseInt((String)formMap.get("START_DATE"))+1);
			String tmpDate = DateTimeUtils.convertDate(DateTimeUtils.INTERCONVERSION,twDateCheck, "yyyyMMdd", "yyyyMMdd");
			if(bank_group_bo.getEachsysstatustab_bo().checkBizDate(tmpDate)){
				map.put("result", "FALSE");
				map.put("msg", "啟用日期不可小於營業日:"+bank_group_bo.getEachsysstatustab_bo().getBusinessDate());
				return map;
			}
			
			
			if(po != null ){
				map.put("result", "FALSE");
				map.put("msg", "儲存失敗，id重複");
				map.put("target", "add_p");
				return map;
			}
			po = new PI_SND_PROFILE();
			BeanUtils.populate(po, formMap);
			po.setId(id);
			po.setSTART_DATE(StrUtils.isEmpty(po.getSTART_DATE())?null:DateTimeUtils.convertDate(po.getSTART_DATE(), "yyyyMMdd", "yyyyMMdd"));
			po.setSTOP_DATE(StrUtils.isEmpty(po.getSTOP_DATE())?null:DateTimeUtils.convertDate(po.getSTOP_DATE(), "yyyyMMdd", "yyyyMMdd"));
			System.out.println("### READY TO INSERT = " + po.toString());
			System.out.println("### READY TO INSERT = " + BeanUtils.describe(po));
			pi_snd_profile_Dao.save(po);
			map.put("result", "TRUE");
			map.put("msg", "儲存成功");
			map.put("target", "search");
		}catch (Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();
			map.put("result", "ERROR");
			map.put("msg", "儲存失敗，系統異常:"+e);
			map.put("target", "add_p");
			return map;
		}
		return map;
	}
	
	public String search_toJson(Map<String, String> params){
		String json = "{}";
		List<PI_SND_PROFILE> list = null;
		try {
			System.out.println("params>>"+params);
			String conditionKey = "[\"TXN_ID\",\"PI_COMPANY_ID\",\"BILL_TYPE_ID\",\"SND_COMPANY_ID\",\"PI_COMPANY_NAME\"]";
			Map map = getConditionData(params, conditionKey);
			String sord =StrUtils.isNotEmpty(params.get("sord"))? params.get("sord"):"";
			String sidx =StrUtils.isNotEmpty(params.get("sidx"))? params.get("sidx"):"";
			String orderSQL = StrUtils.isNotEmpty(sidx)? " ORDER BY "+sidx+" "+sord:"";
			list = search(map.get("sqlPath").toString(), orderSQL , (List<String>) map.get("values"));
			
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
	
	public List<PI_SND_PROFILE> search(String sqlPath,String orderSQL  , List<String> values){
		
		List<PI_SND_PROFILE> list = null;
		StringBuffer sql = new StringBuffer();
		try{
//			sql.append(" SELECT A.*,(SELECT BILL_NAME FROM BILL_TYPE WHERE A.BILL_TYPE_ID = BILL_TYPE_ID) BILL_NAME FROM PI_COMPANY_PROFILE A ");
			sql.append(" WITH PI_SND_PROFILE_TEMP AS ( ");
			sql.append(" SELECT A.*,(SELECT BILL_TYPE_NAME FROM BILL_TYPE WHERE A.BILL_TYPE_ID = BILL_TYPE_ID) BILL_TYPE_NAME ");
			sql.append(" FROM PI_SND_PROFILE A ");
			sql.append(" ) ");
			sql.append(" SELECT * FROM PI_SND_PROFILE_TEMP ");
			sql.append(sqlPath);
			sql.append(orderSQL);
			System.out.println("sql>>"+sql);
			list = pi_snd_profile_Dao.getData(sql.toString(), values);
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("list>>"+list);
		return list;
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
					System.out.println("key>>"+key+",val>>"+params.get(key));
					if(StrUtils.isNotEmpty(params.get(key))){
						
						
						if(key.equals("SND_COMPANY_NAME") ){
							if(j==0){sql2.append(" WHERE ");}
							if(j!=0){sql2.append(" AND ");}
							sql2.append(key+" LIKE ?");
							values.add("%" + params.get(key) + "%");
							j++;
						}else{
							if(i==0){sql.append(" WHERE ");}
							if(i!=0){sql.append(" AND ");}
							if(key.equals("SND_COMPANY_NAME") || key.equals("PI_COMPANY_NAME")){
								sql.append(key+" LIKE ?");
								values.add("%" + params.get(key) + "%");
							}else{
								sql.append( key+" = ? ");
								values.add(params.get(key));
							}
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
	
	
	public Map<String,String> update(Map formMap){
		Map<String, String> map = null;
		Map<String, String> pkmap = new HashMap<String, String>();//e
		Map<String, String> oldmap = new HashMap<String, String>();//e
		PI_SND_PROFILE po = null;
		try {
			map = new HashMap<String, String>();
			String company_id = (String)formMap.get("PI_COMPANY_ID");
			String snd_company_id = (String)formMap.get("SND_COMPANY_ID");
			String bill_type_id = (String)formMap.get("BILL_TYPE_ID");
			PI_SND_PROFILE_PK id = new PI_SND_PROFILE_PK(company_id,snd_company_id,bill_type_id);
			po = pi_snd_profile_Dao.get(id);
//			如果有修改啟用日期，要檢核是否小於營業日
			String twDate = (String) formMap.get("START_DATE");
			String tmpDate = DateTimeUtils.convertDate(DateTimeUtils.INTERCONVERSION,twDate, "yyyyMMdd", "yyyyMMdd");
			String twDateCheck = "0"+String.valueOf(Integer.parseInt((String)formMap.get("START_DATE"))+1);
			String tmpDateCheck = DateTimeUtils.convertDate(DateTimeUtils.INTERCONVERSION,twDateCheck, "yyyyMMdd", "yyyyMMdd");
			if(!po.getSTART_DATE().equals(tmpDate)){
				if(bank_group_bo.getEachsysstatustab_bo().checkBizDate(tmpDateCheck)){
					map.put("result", "FALSE");
					map.put("msg", "啟用日期不可小於營業日:"+bank_group_bo.getEachsysstatustab_bo().getBusinessDate());
					return map;
				}
			}
			pkmap.put("PI_COMPANY_ID", company_id);
			pkmap.put("SND_COMPANY_ID", snd_company_id);
			pkmap.put("BILL_TYPE_ID", bill_type_id);
			if(po == null){
				BeanUtils.populate(po, formMap);
				map = pi_snd_profile_Dao.updateFail(po, oldmap, pkmap, "儲存失敗，查無資料", 1);
				return map;
			}
			oldmap = BeanUtils.describe(po);
			BeanUtils.populate(po, formMap);
			po.setSTART_DATE(StrUtils.isEmpty(po.getSTART_DATE())?null:DateTimeUtils.convertDate(po.getSTART_DATE(), "yyyyMMdd", "yyyyMMdd"));
			po.setSTOP_DATE(StrUtils.isEmpty(po.getSTOP_DATE())?null:DateTimeUtils.convertDate(po.getSTOP_DATE(), "yyyyMMdd", "yyyyMMdd"));
			pi_snd_profile_Dao.saveII(po, oldmap, pkmap);
			map.put("result", "TRUE");
			map.put("msg", "儲存成功");
			map.put("target", "search");
		}catch (Exception e){
			e.printStackTrace();
			pi_company_profile_Dao.updateFail(po, oldmap, pkmap, "儲存失敗，查無資料", 1);
			return map;
		}
		return map;
	}
	
	public Map<String,String> delete(Map formMap){
		String company_id = (String)formMap.get("PI_COMPANY_ID");
		String snd_company_id = (String)formMap.get("SND_COMPANY_ID");
		String bill_type_id = (String)formMap.get("BILL_TYPE_ID");
		Map<String, String> map = null;
		Map<String, String> pkmap = new HashMap<String,String>();//e
		PI_SND_PROFILE po = null;
		try {
			map = new HashMap<String, String>();
			
//			TODO 要問建利如果已有交易資料是否可以刪除?
			pkmap.put("PI_COMPANY_ID", company_id);
			pkmap.put("SND_COMPANY_ID", snd_company_id);
			pkmap.put("BILL_TYPE_ID", bill_type_id);
			PI_SND_PROFILE_PK id = new PI_SND_PROFILE_PK(company_id,snd_company_id,bill_type_id);
			po = pi_snd_profile_Dao.get(id);
			if(po == null ){
				map = pi_snd_profile_Dao.removeFail(po, pkmap, "刪除失敗，查無資料", 1);
				return map;
			}
			pi_snd_profile_Dao.removeII(po, pkmap);
			map.put("result", "TRUE");
			map.put("msg", "刪除成功");
			map.put("target", "search");
		} catch (Exception e) {
			map = pi_snd_profile_Dao.removeFail(po, pkmap, "刪除失敗，系統異常", 2);
			return map;
		}
		return map;
	}
	
	public PI_SND_PROFILE getByPk(String company_id,String snd_company_id  , String bill_type_id){
		PI_SND_PROFILE po = null;
		PI_SND_PROFILE_PK id = null;
		try{
			id = new PI_SND_PROFILE_PK(company_id, snd_company_id, bill_type_id);
			po = pi_snd_profile_Dao.get(id);
		}catch(Exception e){
			e.printStackTrace();
		}
		return po;
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
	
	

	public PI_COMPANY_PROFILE_Dao getPi_company_profile_Dao() {
		return pi_company_profile_Dao;
	}

	public void setPi_company_profile_Dao(
			PI_COMPANY_PROFILE_Dao pi_company_profile_Dao) {
		this.pi_company_profile_Dao = pi_company_profile_Dao;
	}

	public EACH_USERLOG_Dao getUserLog_Dao() {
		return userLog_Dao;
	}

	public void setUserLog_Dao(EACH_USERLOG_Dao userLog_Dao) {
		this.userLog_Dao = userLog_Dao;
	}

	public BANK_GROUP_BO getBank_group_bo() {
		return bank_group_bo;
	}

	public void setBank_group_bo(BANK_GROUP_BO bank_group_bo) {
		this.bank_group_bo = bank_group_bo;
	}

	public EACH_USERLOG_BO getUserlog_bo() {
		return userlog_bo;
	}

	public void setUserlog_bo(EACH_USERLOG_BO userlog_bo) {
		this.userlog_bo = userlog_bo;
	}

	public SC_COMPANY_PROFILE_Dao getSc_com_Dao() {
		return sc_com_Dao;
	}

	public void setSc_com_Dao(SC_COMPANY_PROFILE_Dao sc_com_Dao) {
		this.sc_com_Dao = sc_com_Dao;
	}

	public PI_SND_PROFILE_Dao getPi_snd_profile_Dao() {
		return pi_snd_profile_Dao;
	}

	public void setPi_snd_profile_Dao(PI_SND_PROFILE_Dao pi_snd_profile_Dao) {
		this.pi_snd_profile_Dao = pi_snd_profile_Dao;
	}
	
	
}
