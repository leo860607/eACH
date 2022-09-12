package tw.org.twntch.bo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tw.org.twntch.db.dao.hibernate.BANK_CTBK_Dao;
import tw.org.twntch.po.BANK_CTBK;
import tw.org.twntch.po.BANK_OPBK;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.StrUtils;

public class BANK_CTBK_BO {
	private BANK_CTBK_Dao bank_ctbk_Dao;
	
	
	public List<BANK_CTBK> getBgbkList(String ctbk_id ,String s_bizdate , String e_bizdate ){
		List<BANK_CTBK> list = null;
		
		String sqlPath = ""; 
		Map<String,String> param = new HashMap<String, String>();
		if(StrUtils.isNotEmpty(ctbk_id)){
			sqlPath = " WHERE CTBK_ID = :ctbk_id ";
			param.put("ctbk_id", ctbk_id);
		}
		try{
			list = bank_ctbk_Dao.getBgbkList(sqlPath , param , s_bizdate, e_bizdate);
		}catch(Exception e){
			e.printStackTrace();
		}
		return list==null?null:(list.size()<=0?null:list);
	}
	
	public List<BANK_CTBK> getCurBgbkList(String ctbk_id ,String s_bizdate  ){
		List<BANK_CTBK> list = null;
		
		String sqlPath = ""; 
		Map<String,String> param = new HashMap<String, String>();
		if(StrUtils.isNotEmpty(ctbk_id)){
			sqlPath = " WHERE CTBK_ID = :ctbk_id ";
			param.put("ctbk_id", ctbk_id);
		}
		try{
			list = bank_ctbk_Dao.getCurBgbkList(sqlPath , param , s_bizdate);
		}catch(Exception e){
			e.printStackTrace();
		}
		return list==null?null:(list.size()<=0?null:list);
	}
	
	
	public List<BANK_CTBK> getByBgbkId(String bgbkId){
		List<BANK_CTBK> list = null;
		if(StrUtils.isNotEmpty(bgbkId)){
			try{
				list = bank_ctbk_Dao.find("FROM tw.org.twntch.po.BANK_CTBK WHERE BGBK_ID = ? ORDER BY START_DATE DESC", bgbkId);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return list;
	}
	
	public List<BANK_CTBK> getByBgbkIdAndCtbkId(String bgbkId, String ctbkId){
		List<BANK_CTBK> list = null;
		if(StrUtils.isNotEmpty(ctbkId)){
			try{
				list = bank_ctbk_Dao.find("FROM tw.org.twntch.po.BANK_CTBK WHERE BGBK_ID = ? AND CTBK_ID = ? ORDER BY START_DATE DESC", bgbkId, ctbkId);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return list;
	}
	
	/**
	 * 查詢對映BANK_GROUP(VIEW)的所有清算行清單
	 * @return
	 */
	public List<BANK_CTBK> getAllCtbkList(){
		List<BANK_CTBK> list = null;
		try{
			list = bank_ctbk_Dao.getAllCtbkList();
		}catch(Exception e){
			e.printStackTrace();
		}
		return list==null?null:(list.size()<=0?null:list);
	}
	
	/**
	 * 查詢對映MASTER_BANK_GROUP的所有清算行清單
	 * @return
	 */
	public List<BANK_CTBK> getAllCtbkListFromMaster(){
		List<BANK_CTBK> list = null;
		try{
			list = bank_ctbk_Dao.getAllCtbkListFromMaster();
		}catch(Exception e){
			e.printStackTrace();
		}
		return list==null?null:(list.size()<=0?null:list);
	}
	public List<BANK_CTBK> getAll_Proxy_CtbkList(){
		List<BANK_CTBK> list = null;
		try{
			list = bank_ctbk_Dao.getAll_Proxy_CtbkList();
		}catch(Exception e){
			e.printStackTrace();
		}
		return list==null?null:(list.size()<=0?null:list);
	}
	public List<BANK_CTBK> getAll_Proxy_CtbkList(String s_bizdate , String e_bizdate){
		List<BANK_CTBK> list = null;
		if(StrUtils.isEmpty(e_bizdate)){
			e_bizdate = s_bizdate;
		}
		try{
			list = bank_ctbk_Dao.getAll_Proxy_CtbkList(s_bizdate ,e_bizdate);
		}catch(Exception e){
			e.printStackTrace();
		}
		return list==null?null:(list.size()<=0?null:list);
	}
	
	
	
	public List<BANK_CTBK> getLastByBgbkId(String bgbkId){
		List<BANK_CTBK> list = null;
		String sql = "FROM tw.org.twntch.po.BANK_CTBK WHERE BGBK_ID = ? AND COALESCE(STOP_DATE,'') = '' ORDER BY START_DATE DESC";
		try{
			list = bank_ctbk_Dao.find(sql, bgbkId);
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	/**
	 * 取得大於營業日的清算行
	 * @param bgbkId
	 * @param bizdate
	 * @return
	 */
	public List<BANK_CTBK> getFutureByBgbkId(String bgbkId ,String bizdate){
		List<BANK_CTBK> list = null;
		String sql = "FROM tw.org.twntch.po.BANK_CTBK WHERE BGBK_ID = ? AND START_DATE > ? ORDER BY START_DATE DESC";
		try{
			list = bank_ctbk_Dao.find(sql, bgbkId,bizdate);
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 取得總行所屬清算行修改紀錄
	 * @param params
	 * @return
	 */
	public String getCtbkRec(Map<String,String> params){
		String json = "{}";
		String bgbkId = StrUtils.isNotEmpty( params.get("BGBK_ID")) ?  params.get("BGBK_ID") :"";
		String sord = StrUtils.isNotEmpty( params.get("sord")) ?  params.get("sord") :"";
		String sidx = StrUtils.isNotEmpty( params.get("sidx")) ?  params.get("sidx") :"";
		List<BANK_CTBK> list = null;
		String sqlPath = "";
//		String sql = "FROM tw.org.twntch.po.BANK_CTBK WHERE BGBK_ID = ?  ORDER BY START_DATE DESC";
		try{
//			list = bank_ctbk_Dao.find(sql, bgbkId);
			sqlPath = " ORDER BY "+sidx+" "+sord;
			list = bank_ctbk_Dao.getDataByBgbkId( bgbkId , sqlPath);
			if(list !=null ){
				json = JSONUtils.toJson(list);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return json;
	}
	
	
	public String getOne(Map<String,String> params){
		String json = "{}";
		String bgbk_Id = StrUtils.isNotEmpty( params.get("BGBK_ID")) ?  params.get("BGBK_ID") :"";
		String opbk_Id = StrUtils.isNotEmpty( params.get("CTBK_ID")) ?  params.get("CTBK_ID") :"";
		String start_Date = StrUtils.isNotEmpty( params.get("START_DATE")) ?  params.get("START_DATE") :"";
		List<BANK_OPBK> list = null;
		String sql = "FROM tw.org.twntch.po.BANK_CTBK WHERE BGBK_ID = ? AND CTBK_ID = ? AND START_DATE =? ";
		try{
			list = bank_ctbk_Dao.find(sql, bgbk_Id , opbk_Id , start_Date);
			if(list !=null && list.size() !=0){
				json = JSONUtils.toJson(list.get(0));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("getOne.json>>"+json);
		return json;
	}
	
	public BANK_CTBK_Dao getBank_ctbk_Dao() {
		return bank_ctbk_Dao;
	}

	public void setBank_ctbk_Dao(BANK_CTBK_Dao bank_ctbk_Dao) {
		this.bank_ctbk_Dao = bank_ctbk_Dao;
	}
	
}
