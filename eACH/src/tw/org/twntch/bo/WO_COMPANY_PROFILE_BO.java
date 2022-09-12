package tw.org.twntch.bo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.struts.util.LabelValueBean;

import tw.org.twntch.db.dao.hibernate.EACH_USERLOG_Dao;
import tw.org.twntch.db.dao.hibernate.SC_COMPANY_PROFILE_Dao;
import tw.org.twntch.db.dao.hibernate.WO_COMPANY_PROFILE_Dao;
import tw.org.twntch.po.WO_COMPANY_PROFILE;
import tw.org.twntch.po.WO_COMPANY_PROFILE_PK;
import tw.org.twntch.util.BeanUtils;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.RptUtils;
import tw.org.twntch.util.StrUtils;

public class WO_COMPANY_PROFILE_BO {
	private WO_COMPANY_PROFILE_Dao wo_company_profile_Dao;
	private SC_COMPANY_PROFILE_Dao sc_com_Dao;
	private EACH_USERLOG_Dao userLog_Dao ;
	private BANK_GROUP_BO  bank_group_bo ;
	private EACH_USERLOG_BO userlog_bo;
	
	public String getCompanyDataByCompanyId(Map<String, String> params){
		String companyId = params.get("WO_COMPANY_ID")==null?"":params.get("WO_COMPANY_ID");
		WO_COMPANY_PROFILE po = null ;
		String companyData = "";
		Map map = new HashMap();
		try{
//			先查詢銷帳委託單位業者
			po = wo_company_profile_Dao.getWO_CompanyDataByCompanyId(companyId);
//			如果查不到就查代收代付業者
			if(po ==null){
//				po = wo_company_profile_Dao.getCompanyDataByCompanyId(companyId);
				po = (WO_COMPANY_PROFILE) sc_com_Dao.getCompanyDataByCompanyId(companyId, new WO_COMPANY_PROFILE() );
			}
			
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
		String sql = " SELECT * FROM BILL_TYPE ORDER BY BILL_TYPE_ID ";
		List<Map> list = wo_company_profile_Dao.getDataRTMap(sql, null);
		LabelValueBean bean = null;
		for(Map<String,String> map : list){
				bean = new LabelValueBean(map.get("BILL_TYPE_ID") + " - " + map.get("BILL_TYPE_NAME"), map.get("BILL_TYPE_ID"));
				beanList.add(bean);
		}
		System.out.println("BillTypeList>>"+beanList);
		return beanList;
	}
	
	public Map<String, String> qs_ex_export(String sdItemNo, String bgbkId, String companyId, String txnId, String companyName, String serchStrs, String sortname, String sortorder){
		Map<String, String> rtnMap = null;
		
		bgbkId = StrUtils.isEmpty(bgbkId)?"":bgbkId.equalsIgnoreCase("all")?"":bgbkId;
		txnId = StrUtils.isEmpty(txnId)?"":txnId.equalsIgnoreCase("all")?"":txnId;
		
		try{
			rtnMap = new HashMap<String, String>();
			rtnMap.put("serchStrs", serchStrs);
			Map<String, Object> params = new HashMap<String, Object>();
			
			String orderSQL = "ORDER BY " + sortname + " " + sortorder;
			
			//String sql = getSQL(txdate, pcode, opbkId, bgbkId, clearingPhase, serchStrs, sortname, sortorder);
			//List list = rponblocktab_Dao.getRptData(sql, new ArrayList());
			List<WO_COMPANY_PROFILE> list = null;
//			list = search(sdItemNo, bgbkId, companyId, txnId, companyName, orderSQL);
			List<Map> data = new ArrayList();
			for(WO_COMPANY_PROFILE po : list){
				data.add(BeanUtils.describe(po));
			}
			
			int pathType = Arguments.getStringArg("ISWINOPEN").equals("Y")?RptUtils.C_PATH :RptUtils.R_PATH;
			String outputFilePath = RptUtils.export(RptUtils.COLLECTION,pathType, "chg_sc_profile", "chg_sc_profile", params, data, 2);
			//String outputFilePath = "";
			if(StrUtils.isNotEmpty(outputFilePath)){
				rtnMap.put("result", "TRUE");
				rtnMap.put("msg", outputFilePath);
			}else{
				rtnMap.put("result", "FALSE");
				rtnMap.put("msg", "產生失敗!");
			}
		}catch(Exception e){
			e.printStackTrace();
			rtnMap.put("result", "FALSE");
			rtnMap.put("msg", "產生錯誤!");
		}
		
		return rtnMap;
	}
	
	public Map<String,String> save(Map formMap){
		Map<String, String> map = null;
		try {
			System.out.println("formMap>>"+formMap);
			map = new HashMap<String, String>();
			String company_id = (String)formMap.get("WO_COMPANY_ID");
			String bill_type_id = (String)formMap.get("BILL_TYPE_ID");
//			啟用日期，要檢核是否小於等於營業日
			String twDate = (String) formMap.get("START_DATE");
			String twDateCheck = "0"+String.valueOf(Integer.parseInt((String)formMap.get("START_DATE"))+1);
			String tmpDate = DateTimeUtils.convertDate(DateTimeUtils.INTERCONVERSION,twDateCheck, "yyyyMMdd", "yyyyMMdd");
			if(bank_group_bo.getEachsysstatustab_bo().checkBizDate(tmpDate)){
				map.put("result", "FALSE");
				map.put("msg", "啟用日期不可小於或等於營業日:"+bank_group_bo.getEachsysstatustab_bo().getBusinessDate());
				return map;
			}
			WO_COMPANY_PROFILE_PK id = new WO_COMPANY_PROFILE_PK(company_id,bill_type_id);
			WO_COMPANY_PROFILE po = wo_company_profile_Dao.get(id);
			if(po != null ){
				map.put("result", "FALSE");
				map.put("msg", "儲存失敗，id重複");
				map.put("target", "add_p");
				return map;
			}
			po = new WO_COMPANY_PROFILE();
			BeanUtils.populate(po, formMap);
			po.setId(id);
			po.setSTART_DATE(StrUtils.isEmpty(po.getSTART_DATE())?null:DateTimeUtils.convertDate(po.getSTART_DATE(), "yyyyMMdd", "yyyyMMdd"));
			po.setSTOP_DATE(StrUtils.isEmpty(po.getSTOP_DATE())?null:DateTimeUtils.convertDate(po.getSTOP_DATE(), "yyyyMMdd", "yyyyMMdd"));
			System.out.println("### READY TO INSERT = " + po.toString());
			System.out.println("### READY TO INSERT = " + BeanUtils.describe(po));
			wo_company_profile_Dao.save(po);
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
		List<WO_COMPANY_PROFILE> list = null;
		try {
			System.out.println("params>>"+params);
			String conditionKey = "[\"TXN_ID\",\"WO_COMPANY_ID\",\"BILL_TYPE_ID\",\"SND_COMPANY_ID\",\"WO_COMPANY_NAME\" ,\"INBANK_ID\" ,\"INBANK_ACCT_NO\" ]";
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
	
	public List<WO_COMPANY_PROFILE> search(String sqlPath,String orderSQL  , List<String> values){
		
		List<WO_COMPANY_PROFILE> list = null;
		StringBuffer sql = new StringBuffer();
		try{
//			sql.append(" SELECT A.*,(SELECT BILL_NAME FROM BILL_TYPE WHERE A.BILL_TYPE_ID = BILL_TYPE_ID) BILL_NAME FROM WO_COMPANY_PROFILE A ");
			sql.append(" WITH WO_COMPANY_PROFILE_TEMP AS ( ");
			sql.append(" SELECT A.*,(SELECT  BGBK_NAME FROM BANK_GROUP WHERE BGBK_ID = A.INBANK_ID)  INBANK_NAME ");
			sql.append(" ,(SELECT BILL_TYPE_NAME FROM BILL_TYPE WHERE A.BILL_TYPE_ID = BILL_TYPE_ID) BILL_TYPE_NAME ");
			sql.append(" FROM WO_COMPANY_PROFILE A ");
			sql.append(" ) ");
			sql.append(" SELECT * FROM WO_COMPANY_PROFILE_TEMP ");
			sql.append(sqlPath);
			sql.append(orderSQL);
			System.out.println("sql>>"+sql);
			list = wo_company_profile_Dao.getData(sql.toString(), values);
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
							if(key.equals("SND_COMPANY_NAME") || key.equals("WO_COMPANY_NAME")){
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
		WO_COMPANY_PROFILE po = null;
		try {
			map = new HashMap<String, String>();
			String company_id = (String)formMap.get("WO_COMPANY_ID");
			String bill_type_id = (String)formMap.get("BILL_TYPE_ID");
			WO_COMPANY_PROFILE_PK id = new WO_COMPANY_PROFILE_PK(company_id,bill_type_id);
			po = wo_company_profile_Dao.get(id);
//			如果有修改啟用日期，要檢核是否小於等於營業日
			String twDate = (String) formMap.get("START_DATE");
			String tmpDate = DateTimeUtils.convertDate(DateTimeUtils.INTERCONVERSION,twDate, "yyyyMMdd", "yyyyMMdd");
			String twDateCheck = "0"+String.valueOf(Integer.parseInt((String)formMap.get("START_DATE"))+1);
			String tmpDateCheck = DateTimeUtils.convertDate(DateTimeUtils.INTERCONVERSION,twDateCheck, "yyyyMMdd", "yyyyMMdd");
			if(!po.getSTART_DATE().equals(tmpDate)){
				if(bank_group_bo.getEachsysstatustab_bo().checkBizDate(tmpDateCheck)){
					map.put("result", "FALSE");
					map.put("msg", "啟用日期不可小於或等於營業日:"+bank_group_bo.getEachsysstatustab_bo().getBusinessDate());
					return map;
				}
			}
			
			pkmap.put("WO_COMPANY_ID", company_id);
			pkmap.put("BILL_TYPE_ID", bill_type_id);
			if(po == null){
				BeanUtils.populate(po, formMap);
				map = wo_company_profile_Dao.updateFail(po, oldmap, pkmap, "儲存失敗，查無資料", 1);
				return map;
			}
			oldmap = BeanUtils.describe(po);
			BeanUtils.populate(po, formMap);
			po.setSTART_DATE(StrUtils.isEmpty(po.getSTART_DATE())?null:DateTimeUtils.convertDate(po.getSTART_DATE(), "yyyyMMdd", "yyyyMMdd"));
			po.setSTOP_DATE(StrUtils.isEmpty(po.getSTOP_DATE())?null:DateTimeUtils.convertDate(po.getSTOP_DATE(), "yyyyMMdd", "yyyyMMdd"));
			wo_company_profile_Dao.saveII(po, oldmap, pkmap);
			map.put("result", "TRUE");
			map.put("msg", "儲存成功");
			map.put("target", "search");
		}catch (Exception e){
			e.printStackTrace();
			wo_company_profile_Dao.updateFail(po, oldmap, pkmap, "儲存失敗，查無資料", 1);
			return map;
		}
		return map;
	}
	
	public Map<String,String> delete(Map formMap){
		String company_id = (String)formMap.get("WO_COMPANY_ID");
		String bill_type_id = (String)formMap.get("BILL_TYPE_ID");
		Map<String, String> map = null;
		Map<String, String> pkmap = new HashMap<String,String>();//e
		WO_COMPANY_PROFILE po = null;
		try {
			map = new HashMap<String, String>();
			pkmap.put("WO_COMPANY_ID", company_id);
			pkmap.put("BILL_TYPE_ID", bill_type_id);
			WO_COMPANY_PROFILE_PK id = new WO_COMPANY_PROFILE_PK(company_id,bill_type_id);
			po = wo_company_profile_Dao.get(id);
			if(po == null ){
				map = wo_company_profile_Dao.removeFail(po, pkmap, "刪除失敗，查無資料", 1);
				return map;
			}
			wo_company_profile_Dao.removeII(po, pkmap);
			map.put("result", "TRUE");
			map.put("msg", "刪除成功");
			map.put("target", "search");
		} catch (Exception e) {
			map = wo_company_profile_Dao.removeFail(po, pkmap, "刪除失敗，系統異常", 2);
			return map;
		}
		return map;
	}
	
	public WO_COMPANY_PROFILE getByPk(String company_id , String bill_type_id){
		WO_COMPANY_PROFILE po = null;
		WO_COMPANY_PROFILE_PK id = null;
		try{
			id = new WO_COMPANY_PROFILE_PK(company_id, bill_type_id);
			po = wo_company_profile_Dao.get(id);
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
	
	

	

	public WO_COMPANY_PROFILE_Dao getWo_company_profile_Dao() {
		return wo_company_profile_Dao;
	}

	public void setWo_company_profile_Dao(
			WO_COMPANY_PROFILE_Dao wo_company_profile_Dao) {
		this.wo_company_profile_Dao = wo_company_profile_Dao;
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


	
}
