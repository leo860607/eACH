package tw.org.twntch.bo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.struts.util.LabelValueBean;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;

import tw.org.twntch.db.dao.hibernate.AGENT_SEND_PROFILE_Dao;
import tw.org.twntch.form.Agent_Send_Profile_Form;
import tw.org.twntch.po.AGENT_SEND_PROFILE;
import tw.org.twntch.po.AGENT_SEND_PROFILE_PK;
import tw.org.twntch.util.BeanUtils;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.RptUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.zDateHandler;

public class AGENT_SEND_PROFILE_BO {

	private AGENT_SEND_PROFILE_Dao agent_send_profile_Dao ;
	private EACHSYSSTATUSTAB_BO eachsysstatustab_bo;
	private EACH_USERLOG_BO userlog_bo;
	private AGENT_FEE_CODE_BO agent_fee_code_bo;
	private AGENT_PROFILE_BO agent_profile_bo;
	private AGENT_CR_LINE_BO agent_cr_line_bo;
	
	//查詢 AGENT_SEND_PROFILE 的代理業者清單
	public List<LabelValueBean> getAll_Snd_Com_List(){
		StringBuffer sql = new StringBuffer();
		List<AGENT_SEND_PROFILE> list = null;
		List<LabelValueBean> beanList = new LinkedList<LabelValueBean>();
		try {
			sql.append(" SELECT SND_COMPANY_ID , COALESCE (GETCOMPANY_NAME(SND_COMPANY_ID),'') AS SND_COMPANY_NAME ");
			sql.append(" FROM AGENT_SEND_PROFILE ");
			sql.append(" GROUP BY SND_COMPANY_ID ");
			try{
				SQLQuery query = agent_send_profile_Dao.getCurrentSession().createSQLQuery(sql.toString());
				query.setResultTransformer(Transformers.aliasToBean(AGENT_SEND_PROFILE.class));
				list = query.list();
				LabelValueBean bean = null;
				if(!( list == null || list.size() == 0 )){
					for(AGENT_SEND_PROFILE po : list){
						bean = new LabelValueBean(po.getSND_COMPANY_ID() + " - " + po.getSND_COMPANY_NAME(), po.getSND_COMPANY_ID());
						beanList.add(bean);
					}
				}else{
					bean = new LabelValueBean("===查無代理業者資料===", "");
					beanList.add(bean);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return beanList;
	}
	
	public List<Map> getSnd_Com_List(String company_id){
		StringBuffer sql = new StringBuffer();
		List list = null;
		try {
			sql.append(" SELECT DISTINCT  COALESCE (SND_COMPANY_ID,'') AS SND_COMPANY_ID , COALESCE (GETCOMPANY_ABBR(SND_COMPANY_ID),'') AS SND_COMPANY_ABBR_NAME ");
			sql.append(" FROM AGENT_SEND_PROFILE ");
			sql.append(" WHERE COMPANY_ID = ?");
			List<String> values = new LinkedList<String>();
			values.add(company_id);
			list = agent_send_profile_Dao.getDataRTMap(sql.toString(), values);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return list;
	}
	
	public Map<String, String> agent_ex_export( String serchStrs, String sortname, String sortorder){
		Map<String, String> rtnMap = null;
		String conditionKey = "[\"TXN_ID\",\"COMPANY_ID\",\"SND_COMPANY_ID\",\"SND_COMPANY_NAME\"]";
		try {
			rtnMap = new HashMap<String,String>();
			List<Map> data = new ArrayList<Map>();
			Map<String, Object> param = new HashMap<String, Object>();
			Map<String, String> params = JSONUtils.json2map(serchStrs) ;
			Map map = getConditionData(params, conditionKey);
			param.putAll(params);
			String sname =StrUtils.isNotEmpty(sortname)? sortname :"";
			String sorder =StrUtils.isNotEmpty(sortorder)? sortorder:"";
			String orderSQL = StrUtils.isNotEmpty(sname)? " ORDER BY "+sname+" "+ sorder:"";
			data = searchRTMap(map.get("sqlPath").toString(),map.get("sqlPath2").toString(), orderSQL , (List<String>) map.get("values"));
			int pathType = Arguments.getStringArg("ISWINOPEN").equals("Y")?RptUtils.C_PATH :RptUtils.R_PATH;
			String outputFilePath = RptUtils.export(RptUtils.COLLECTION,pathType, "agent_send_profile", "agent_send_profile", param, data, 2);
			System.out.println("data>>"+data);
			System.out.println("outputFilePath>>"+outputFilePath);
			if(StrUtils.isNotEmpty(outputFilePath)){
				rtnMap.put("result", "TRUE");
				rtnMap.put("msg", outputFilePath);
			}else{
				rtnMap.put("result", "FALSE");
				rtnMap.put("msg", "產生失敗!");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rtnMap.put("result", "FALSE");
			rtnMap.put("msg", "產生錯誤!");
		}
		
		return rtnMap;
	}
	
	
	public Map<String,String> delete(String company_id , String snd_company_id , String txn_id, Agent_Send_Profile_Form form){
		Map<String, String> map = null;
		Map<String, String> pkmap = new HashMap<String, String>();
		Map<String, String> oldmap = new HashMap<String, String>();
		String activeDate = "";
		try {
			map = new HashMap<String, String>();
			AGENT_SEND_PROFILE po = agent_send_profile_Dao.get(new AGENT_SEND_PROFILE_PK(txn_id , company_id , snd_company_id ));
			pkmap = BeanUtils.describe(new AGENT_SEND_PROFILE_PK(txn_id , company_id , snd_company_id ));
			if(po == null ){
				map.put("result", "FALSE");
				map.put("msg", "刪除失敗，查無資料");
				map.put("target", "edit_p");
//				map = agent_send_profile_Dao.removeFail(null, pkmap, "刪除失敗，查無資料", 1);
				return map;
			}
			activeDate =  DateTimeUtils.convertDate(DateTimeUtils.INTERCONVERSION, po.getACTIVE_DATE(), "yyyyMMdd", "yyyyMMdd") ;
			
//			已到啟用日期，若未發生交易允許刪除，已跟票交李建利確定過 TXNLOG 有紀錄(無論成功或失敗)就不可刪除
			if(eachsysstatustab_bo.checkBizDate(activeDate) && isTxnLog(txn_id, company_id, snd_company_id)){
				map.put("result", "FALSE");
				map.put("msg", "刪除失敗，目前資料已啟用，且已有交易資料");
				map.put("target", "edit_p");
				return map;
			}
			po.setCOMPANY_ID(form.getSND_COMPANY_ID());
			po.setCOMPANY_NAME(form.getSND_COMPANY_NAME());
			po.setTXN_ID(form.getTXN_ID());
			
			agent_send_profile_Dao.removeII(po, pkmap);
			
			map.put("result", "TRUE");
			map.put("msg", "刪除成功");
			map.put("target", "search");
//			oldmap = BeanUtils.describe(po);
//			oldmap.putAll(BeanUtils.describe(po.getId()));
//			userlog_bo.writeLog("D", oldmap, null, pkmap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			map.put("result", "ERROR");
			map.put("msg", "刪除失敗，系統異常");
			map.put("target", "edit_p");
//			map = agent_send_profile_Dao.removeFail(null, pkmap, "刪除失敗，系統異常", 2);
			return map;
		}
		return map;
	}

	/**
	 * 
	 *判斷代理業者關連檔資料是否已做過交易 是:true 否:false
	 * @param txn_id
	 * @param company_id
	 * @param snd_company_id
	 * @return
	 */
	public  boolean isTxnLog(String txn_id , String company_id , String snd_company_id){
		boolean result = false;
		try {
			Map<String , Integer> map = agent_send_profile_Dao.getTxnLogDataById(txn_id, company_id, snd_company_id);
			if(map.get("NUM") > 0){
				result = true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	
	public Map<String,String> update(AGENT_SEND_PROFILE po){
		Map<String, String> map = null;
		Map<String, String> pkmap = new HashMap<String, String>();
		Map<String, String> oldmap = new HashMap<String, String>();
		String activedate = "";
		String stopdate = "";
		String tmpactivedate = "";
		boolean chk3 = false;
		try {
			map = new HashMap<String, String>();
			AGENT_SEND_PROFILE tmp = agent_send_profile_Dao.get(po.getId());
			pkmap = BeanUtils.describe(po.getId());
//			if(po == null ){
			if(tmp == null ){
				map.put("result", "FALSE");
				map.put("msg", "儲存失敗，查無資料");
				map.put("target", "edit_p");
//				map = fee_code_Dao.updateFail(po, oldmap, pkmap, "儲存失敗，查無資料", 1);
				return map;
			}

			tmpactivedate =  tmp.getACTIVE_DATE();
			activedate = po.getACTIVE_DATE();
//			如果有填停用日期
			if(StrUtils.isNotEmpty(po.getSTOP_DATE())){
				stopdate = DateTimeUtils.convertDate(DateTimeUtils.INTERCONVERSION, po.getSTOP_DATE(), "yyyyMMdd", "yyyyMMdd") ;
				if(eachsysstatustab_bo.checkBizDate(stopdate)){
					map.put("result", "FALSE");
					map.put("msg", "儲存失敗，停用日期不可小於營業日");
					map.put("target", "edit_p");
					return map;
				}
			}
			if(! tmpactivedate.equals(activedate) && isTxnLog(tmp.getId().getTXN_ID(), tmp.getId().getCOMPANY_ID(), tmp.getId().getSND_COMPANY_ID())){
				map.put("result", "FALSE");
				map.put("msg", "儲存失敗，已有交易資料，啟用日期不可修改");
				map.put("target", "edit_p");
				return map;
			}
			
			chk3 = agent_profile_bo.isActive(po.getACTIVE_DATE(), po.getId().getCOMPANY_ID());
			if(!chk3){
				map.put("result", "FALSE");
				map.put("msg", "目前輸入的啟用日期不可小於代理業者基本資料檔啟用日期..");
				map.put("target", "edit_p");
				return map;
			}
			
			oldmap = BeanUtils.describe(tmp);
			po.setCDATE(tmp.getCDATE());
			po.setUDATE(zDateHandler.getTheDateII());
			agent_send_profile_Dao.saveII(po, oldmap, pkmap);
			map.put("result", "TRUE");
			map.put("msg", "儲存成功");
			map.put("target", "search");
		}catch (Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();
			map.put("result", "ERROR");
			map.put("msg", "儲存失敗，系統異常");
			map.put("target", "edit_p");
//			map = fee_code_Dao.updateFail(po, oldmap, pkmap, "儲存失敗，系統異常", 2);
			return map;
		}
		return map;
	}

	public Map<String, String> save(AGENT_SEND_PROFILE po){
		Map<String, String> map = null;
		Map<String, String> pkmap = new HashMap<String, String>();
		boolean chk1 = false , chk2 = false , chk3 = false;
		try {
			map = new HashMap<String, String>();
			AGENT_SEND_PROFILE tmp = agent_send_profile_Dao.get(po.getId());
			pkmap = BeanUtils.describe(po.getId());
			if(tmp != null){
				map = agent_send_profile_Dao.saveFail(null, pkmap, "儲存失敗，資料重複", 1);
				return map;
			}
			// 啟用日期控管為操作時間所屬營業日的下一個營業日(含)以後
//			String nextDate = eachsysstatustab_bo.getNextBusinessDate();
//			String west_startDate = po.getId().getSTART_DATE();
//			if(Integer.valueOf(west_startDate) < Integer.valueOf(nextDate)){
//				map = fee_code_Dao.saveFail(null, pkmap, "儲存失敗，啟用日期不可小於 " + nextDate, 2);
//				return map;
//			}
			
//			eachsysstatustab_bo.getBusinessDate();
//			String west_startDate = po.getACTIVE_DATE();
//			west_startDate = DateTimeUtils.convertDate(DateTimeUtils.INTERCONVERSION,west_startDate, "yyyyMMdd", "yyyyMMdd");
//			if(eachsysstatustab_bo.checkBizDate(west_startDate)){
//				map.put("result", "FALSE");
//				map.put("msg", "啟用日期不可小於或等於營業日:"+eachsysstatustab_bo.getBusinessDate());
//				map.put("target", "add_p");
//				return map;
//			}
			
//			1.	以代理業者統一編及交易代號為Key，檢核該統一編號需存在於代理業者手續費檔，且為啟用狀態
//			20160314 edit by hugo req by UAT-20160205-02 改為不檢核是否啟用
//			2.	以發動者統一編號及交易代號為Key，檢核該統一編號需存在於代收/付發動者基本資料檔，及被代理發動者額度檔(AGENT_CR_LINE)
//			chk1 = agent_fee_code_bo.checkIsEx(po.getId().getCOMPANY_ID(), po.getId().getTXN_ID());
			chk1 = agent_fee_code_bo.checkIsExII(po.getId().getCOMPANY_ID(), po.getId().getTXN_ID());
			if(!chk1){
				map.put("result", "FALSE");
				map.put("msg", "目前輸入的交易代號尚未建立相對應的代理業者手續費代號..");
				map.put("target", "add_p");
				return map;
			}
			chk2 =agent_cr_line_bo.chkIsEx(po.getId().getSND_COMPANY_ID());
			if(!chk2){
				map.put("result", "FALSE");
				map.put("msg", "目前輸入的發動業者統編尚未建立額度檔..");
				map.put("target", "add_p");
				return map;
			}
			chk3 = agent_profile_bo.isActive(po.getACTIVE_DATE(), po.getId().getCOMPANY_ID());
			if(!chk3){
				map.put("result", "FALSE");
				map.put("msg", "目前輸入的啟用日期不可小於代理業者基本資料檔啟用日期..");
				map.put("target", "add_p");
				return map;
			}
			po.setCDATE(zDateHandler.getTheDateII());
//			fee_code_Dao.save(po);
			agent_send_profile_Dao.save(po , pkmap);
			map.put("result", "TRUE");
			map.put("msg", "儲存成功");
			map.put("target", "search");
		}catch (Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();
			map.put("result", "ERROR");
			map.put("msg", "儲存失敗，系統異常");
			map.put("target", "add_p");
//			map = agent_send_profile_Dao.saveFail(null, pkmap, "儲存失敗，系統異常" , 2);
			return map;
		}
		return map;
	}
	

	public List<AGENT_SEND_PROFILE> search(String sqlPath, String sqlPath2,String orderSQL  , List<String> values){
		List<AGENT_SEND_PROFILE> list = null;
		StringBuffer sql = new StringBuffer();
		try{
			sql.append(" WITH TEMP AS ( ");
			sql.append("  SELECT COALESCE (SND_COMPANY_ID,'') AS SND_COMPANY_ID , COALESCE (GETCOMPANY_ABBR(SND_COMPANY_ID),'') AS SND_COMPANY_NAME ");
			sql.append(" , COALESCE (COMPANY_ID,'') AS COMPANY_ID ,COALESCE (TXN_ID,'') AS TXN_ID ");
			sql.append(" , COALESCE ((SELECT COMPANY_NAME FROM AGENT_PROFILE A WHERE A.COMPANY_ID = AGENT_SEND_PROFILE.COMPANY_ID FETCH FIRST ROW ONLY),'') AS COMPANY_NAME ");
			sql.append(" , COALESCE (ACTIVE_DATE,'') AS ACTIVE_DATE,COALESCE (STOP_DATE,'') AS STOP_DATE ");
			sql.append("  FROM AGENT_SEND_PROFILE  ");
			sql.append(sqlPath);
			sql.append(" ) ");
			sql.append(" SELECT * FROM TEMP ");
			sql.append(sqlPath2);
			sql.append(orderSQL);
			System.out.println("sql>>"+sql);
			list = agent_send_profile_Dao.getData(sql.toString(), values);
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("list>>"+list);
		return list;
	}
	
	public List<Map> searchRTMap(String sqlPath, String sqlPath2,String orderSQL  , List<String> values){
		List<Map> list = null;
		StringBuffer sql = new StringBuffer();
		try{
			sql.append(" WITH TEMP AS ( ");
			sql.append("  SELECT COALESCE (SND_COMPANY_ID,'') AS SND_COMPANY_ID , COALESCE (GETCOMPANY_NAME(SND_COMPANY_ID),'') AS SND_COMPANY_NAME ");
			sql.append(" , COALESCE (COMPANY_ID,'') AS COMPANY_ID ,COALESCE (TXN_ID,'') AS TXN_ID ");
			sql.append(" , COALESCE ((SELECT COMPANY_NAME FROM AGENT_PROFILE A WHERE A.COMPANY_ID = AGENT_SEND_PROFILE.COMPANY_ID FETCH FIRST ROW ONLY),'') AS COMPANY_NAME ");
			sql.append(" , COALESCE (ACTIVE_DATE,'') AS ACTIVE_DATE,COALESCE (STOP_DATE,'') AS STOP_DATE ");
			sql.append(" , COALESCE ( (SELECT TXN_NAME FROM TXN_CODE TX WHERE  TX.TXN_ID = TXN_ID FETCH FIRST ROW ONLY ) , '') AS TXN_NAME ");
			sql.append("  FROM AGENT_SEND_PROFILE  ");
			sql.append(sqlPath);
			sql.append(" ) ");
			sql.append(" SELECT * FROM TEMP ");
			sql.append(sqlPath2);
			sql.append(orderSQL);
			System.out.println("sql>>"+sql);
			list = agent_send_profile_Dao.getDataRTMap(sql.toString(), values);
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("list>>"+list);
		return list;
	}
	
	public List<AGENT_SEND_PROFILE> search(String company_id, String snd_company_id,String txn_id ){
		List<AGENT_SEND_PROFILE> list = null;
		List<String> values = new LinkedList<String>();
		StringBuffer sql = new StringBuffer();
		try{
			sql.append(" WITH TEMP AS ( ");
			sql.append("  SELECT COALESCE (SND_COMPANY_ID,'') AS SND_COMPANY_ID , COALESCE (GETCOMPANY_ABBR(SND_COMPANY_ID),'') AS SND_COMPANY_NAME ");
			sql.append(" , COALESCE (COMPANY_ID,'') AS COMPANY_ID ,COALESCE (TXN_ID,'') AS TXN_ID ");
			sql.append(" , COALESCE ((SELECT COMPANY_NAME FROM AGENT_PROFILE A WHERE A.COMPANY_ID = AGENT_SEND_PROFILE.COMPANY_ID FETCH FIRST ROW ONLY),'') AS COMPANY_NAME ");
			sql.append(" , COALESCE (ACTIVE_DATE,'') AS ACTIVE_DATE,COALESCE (STOP_DATE,'') AS STOP_DATE ");
			sql.append("  FROM AGENT_SEND_PROFILE  ");
			sql.append(" WHERE COMPANY_ID = ? AND SND_COMPANY_ID = ? AND TXN_ID = ?");
			sql.append(" ) ");
			sql.append(" SELECT * FROM TEMP ");
			System.out.println("sql>>"+sql);
			values.add(company_id);
			values.add(snd_company_id);
			values.add(txn_id);
			list = agent_send_profile_Dao.getData(sql.toString(), values);
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("list>>"+list);
		return list;
	}
	
	public String search_toJson(Map<String, String> params){
		String json = "{}";
		List<AGENT_SEND_PROFILE> list = null;
		try {
			String conditionKey = "[\"TXN_ID\",\"COMPANY_ID\",\"SND_COMPANY_ID\",\"SND_COMPANY_NAME\"]";
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
						
						
						if(key.equals("SND_COMPANY_NAME")){
							if(j==0){sql2.append(" WHERE ");}
							if(j!=0){sql2.append(" AND ");}
							sql2.append(key+" LIKE ?");
							values.add("%" + params.get(key) + "%");
							j++;
						}else{
							if(i==0){sql.append(" WHERE ");}
							if(i!=0){sql.append(" AND ");}
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

	public AGENT_SEND_PROFILE_Dao getAgent_send_profile_Dao() {
		return agent_send_profile_Dao;
	}

	public void setAgent_send_profile_Dao(
			AGENT_SEND_PROFILE_Dao agent_send_profile_Dao) {
		this.agent_send_profile_Dao = agent_send_profile_Dao;
	}


	public EACHSYSSTATUSTAB_BO getEachsysstatustab_bo() {
		return eachsysstatustab_bo;
	}


	public void setEachsysstatustab_bo(EACHSYSSTATUSTAB_BO eachsysstatustab_bo) {
		this.eachsysstatustab_bo = eachsysstatustab_bo;
	}


	public EACH_USERLOG_BO getUserlog_bo() {
		return userlog_bo;
	}


	public void setUserlog_bo(EACH_USERLOG_BO userlog_bo) {
		this.userlog_bo = userlog_bo;
	}

	public AGENT_FEE_CODE_BO getAgent_fee_code_bo() {
		return agent_fee_code_bo;
	}

	public void setAgent_fee_code_bo(AGENT_FEE_CODE_BO agent_fee_code_bo) {
		this.agent_fee_code_bo = agent_fee_code_bo;
	}

	public AGENT_PROFILE_BO getAgent_profile_bo() {
		return agent_profile_bo;
	}

	public void setAgent_profile_bo(AGENT_PROFILE_BO agent_profile_bo) {
		this.agent_profile_bo = agent_profile_bo;
	}

	public AGENT_CR_LINE_BO getAgent_cr_line_bo() {
		return agent_cr_line_bo;
	}

	public void setAgent_cr_line_bo(AGENT_CR_LINE_BO agent_cr_line_bo) {
		this.agent_cr_line_bo = agent_cr_line_bo;
	} 
	
	
	
	
}
