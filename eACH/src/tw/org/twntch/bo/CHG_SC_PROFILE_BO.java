package tw.org.twntch.bo;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tw.org.twntch.db.dao.hibernate.CHG_SC_PROFILE_Dao;
import tw.org.twntch.db.dao.hibernate.EACH_USERLOG_Dao;
import tw.org.twntch.po.CHG_SC_PROFILE;
import tw.org.twntch.po.SC_COMPANY_PROFILE;
import tw.org.twntch.util.BeanUtils;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.RptUtils;
import tw.org.twntch.util.StrUtils;

public class CHG_SC_PROFILE_BO {
	private CHG_SC_PROFILE_Dao chg_sc_profile_Dao;
	private EACH_USERLOG_Dao userLog_Dao ;
	private BANK_GROUP_BO  bank_group_bo ;
	private EACH_USERLOG_BO userlog_bo;
	
	public String getCompanyDataByCompanyId(Map<String, String> params){
		String companyId = params.get("COMPANY_ID")==null?"":params.get("COMPANY_ID");
		
		String companyData = "";
		Map map = new HashMap();
		try{
			SC_COMPANY_PROFILE po = chg_sc_profile_Dao.getCompanyDataByCompanyId(companyId);
			if(po != null){
				map = tw.org.twntch.util.BeanUtils.describe(po);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		companyData = JSONUtils.map2json(map);
		return companyData;
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
			List<CHG_SC_PROFILE> list = search(sdItemNo, bgbkId, companyId, txnId, companyName, orderSQL);
			List<Map> data = new ArrayList();
			for(CHG_SC_PROFILE po : list){
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
			map = new HashMap<String, String>();
			String sd_item_no = (String)formMap.get("SD_ITEM_NO");
			CHG_SC_PROFILE po = chg_sc_profile_Dao.get(sd_item_no);
			if(po != null ){
				map.put("result", "FALSE");
				map.put("msg", "儲存失敗，id重複");
				map.put("target", "add_p");
				return map;
			}
			po = new CHG_SC_PROFILE();
			BeanUtils.populate(po, formMap);
			po.setSTART_DATE(StrUtils.isEmpty(po.getSTART_DATE())?null:DateTimeUtils.convertDate(po.getSTART_DATE(), "yyyyMMdd", "yyyy-MM-dd"));
			po.setSTOP_DATE(StrUtils.isEmpty(po.getSTOP_DATE())?null:DateTimeUtils.convertDate(po.getSTOP_DATE(), "yyyyMMdd", "yyyy-MM-dd"));
			//System.out.println("### READY TO INSERT = " + po.toString());
			chg_sc_profile_Dao.save(po);
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
	
	public String search_toJson(Map<String, String> param){
		String sdItemNo =StrUtils.isNotEmpty(param.get("SD_ITEM_NO"))? param.get("SD_ITEM_NO"):"";
		String bgbkId =StrUtils.isNotEmpty(param.get("BGBK_ID"))?(param.get("BGBK_ID").equalsIgnoreCase("all")?"":param.get("BGBK_ID")):"";
		String companyId = StrUtils.isNotEmpty(param.get("COMPANY_ID"))? param.get("COMPANY_ID"):"";
		String txnId =StrUtils.isNotEmpty(param.get("TXN_ID"))?(param.get("TXN_ID").equalsIgnoreCase("all")?"":param.get("TXN_ID")):"";
		String company = "";
		try {
			company = StrUtils.isNotEmpty(param.get("COMPANY_NAME"))?java.net.URLDecoder.decode(param.get("COMPANY_NAME"), "UTF-8"):"";
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String sord =StrUtils.isNotEmpty(param.get("sord"))? param.get("sord"):"";
		String sidx =StrUtils.isNotEmpty(param.get("sidx"))? param.get("sidx"):"";
		String orderSQL = StrUtils.isNotEmpty(sidx)? " ORDER BY "+sidx+" "+sord:"";
		
		//20150128 HUANGPU 改以總行查詢
		//String brbkId =StrUtils.isNotEmpty(param.get("SND_BRBK_ID"))? param.get("SND_BRBK_ID"):"";
		//String jsonStr = JSONUtils.toJson(search(com_id, txn_id, brbkId, comName));
		String jsonStr = JSONUtils.toJson(search(sdItemNo, bgbkId, companyId, txnId, company, orderSQL));
		System.out.println("json"+jsonStr);
		return jsonStr;
	}
	
	public List<CHG_SC_PROFILE> search(String sdItemNo, String bgbkId, String companyId, String txnId, String company, String orderSQL){
		List<CHG_SC_PROFILE> list = new ArrayList<CHG_SC_PROFILE>();
		
		List<String> cons1 = new ArrayList<String>();
		List val1 = new ArrayList();
		
		if(StrUtils.isEmpty(sdItemNo) && StrUtils.isEmpty(bgbkId) && StrUtils.isEmpty(companyId) && StrUtils.isEmpty(txnId) && StrUtils.isEmpty(company)){
			//No condition
		}else{
			if(StrUtils.isNotEmpty(sdItemNo)){
				cons1.add(" A.SD_ITEM_NO = ? ");
				val1.add(sdItemNo);
			}
			if(StrUtils.isNotEmpty(bgbkId)){
				cons1.add(" EACHUSER.GETBKHEADID(A.INBANKID)= ? ");
				val1.add(bgbkId);
			}
			if(StrUtils.isNotEmpty(companyId)){
				cons1.add(" A.COMPANY_ID = ? ");
				val1.add(companyId);
			}
			if(StrUtils.isNotEmpty(txnId)){
				cons1.add(" A.TXN_ID = ? ");
				val1.add(txnId);
			}
			if(StrUtils.isNotEmpty(company)){
				cons1.add(" A.COMPANY_NAME LIKE ? ");
				val1.add("%" + company + "%");
			}
		}
		
		String con1 = combine(cons1);
		list = chg_sc_profile_Dao.search(con1, val1, orderSQL);
		
		//System.out.println("CHG_SC_PROFILE.list>>"+list);
		list = list == null? null : list.size() == 0? null : list;
		return list;
	}
	
	public Map<String,String> update(Map formMap){
		Map<String, String> map = null;
		Map<String, String> pkmap = new HashMap<String, String>();//e
		Map<String, String> oldmap = new HashMap<String, String>();//e
		CHG_SC_PROFILE po = null;
		try {
			map = new HashMap<String, String>();
			String key = (String) formMap.get("SD_ITEM_NO");
			po = chg_sc_profile_Dao.get(key);
			pkmap.put("SD_ITEM_NO", key);
			if(po == null){
				BeanUtils.populate(po, formMap);
				map = chg_sc_profile_Dao.updateFail(po, oldmap, pkmap, "儲存失敗，查無資料", 1);
				return map;
			}
			oldmap = BeanUtils.describe(po);
			po = new CHG_SC_PROFILE();
			BeanUtils.populate(po, formMap);
			po.setSTART_DATE(StrUtils.isEmpty(po.getSTART_DATE())?null:DateTimeUtils.convertDate(po.getSTART_DATE(), "yyyyMMdd", "yyyy-MM-dd"));
			po.setSTOP_DATE(StrUtils.isEmpty(po.getSTOP_DATE())?null:DateTimeUtils.convertDate(po.getSTOP_DATE(), "yyyyMMdd", "yyyy-MM-dd"));
			chg_sc_profile_Dao.saveII(po, oldmap, pkmap);
			map.put("result", "TRUE");
			map.put("msg", "儲存成功");
			map.put("target", "search");
		}catch (Exception e){
			e.printStackTrace();
			chg_sc_profile_Dao.updateFail(po, oldmap, pkmap, "儲存失敗，查無資料", 1);
			return map;
		}
		return map;
	}
	
	public Map<String,String> delete(Map formMap){
		String key = (String) formMap.get("SD_ITEM_NO");
		Map<String, String> map = null;
		Map<String, String> pkmap = new HashMap<String,String>();//e
		CHG_SC_PROFILE po = null;
		try {
			map = new HashMap<String, String>();
			pkmap.put("SD_ITEM_NO", key);
			po = chg_sc_profile_Dao.get(key);
			if(po == null ){
				map = chg_sc_profile_Dao.removeFail(po, pkmap, "刪除失敗，查無資料", 1);
				return map;
			}
			chg_sc_profile_Dao.removeII(po, pkmap);
			map.put("result", "TRUE");
			map.put("msg", "刪除成功");
			map.put("target", "search");
		} catch (Exception e) {
			map = chg_sc_profile_Dao.removeFail(po, pkmap, "刪除失敗，系統異常", 2);
			return map;
		}
		return map;
	}
	
	public CHG_SC_PROFILE getByPk(String sdItemNo){
		CHG_SC_PROFILE po = null;
		try{
			po = chg_sc_profile_Dao.get(sdItemNo);
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
	
	public CHG_SC_PROFILE_Dao getChg_sc_profile_Dao() {
		return chg_sc_profile_Dao;
	}

	public void setChg_sc_profile_Dao(CHG_SC_PROFILE_Dao chg_sc_profile_Dao) {
		this.chg_sc_profile_Dao = chg_sc_profile_Dao;
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
}
