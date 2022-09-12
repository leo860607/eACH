package tw.org.twntch.bo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tw.org.twntch.db.dao.hibernate.BANK_OPBK_Dao;
import tw.org.twntch.po.BANK_OPBK;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.zDateHandler;

public class BANK_OPBK_BO {
	private BANK_OPBK_Dao bank_opbk_Dao;
	
	
	public List<BANK_OPBK> getByBgbkId(String bgbkId){
		List<BANK_OPBK> list = null;
		if(StrUtils.isNotEmpty(bgbkId)){
			try{
				list = bank_opbk_Dao.find("FROM tw.org.twntch.po.BANK_OPBK WHERE BGBK_ID = ? ORDER BY START_DATE DESC", bgbkId);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return list;
	}
	
	public List<BANK_OPBK> getByBgbkIdAndOpbkId(String bgbkId, String opbkId){
		List<BANK_OPBK> list = null;
		if(StrUtils.isNotEmpty(opbkId)){
			try{
				list = bank_opbk_Dao.find("FROM tw.org.twntch.po.BANK_OPBK WHERE BGBK_ID = ? AND OPBK_ID = ? ORDER BY START_DATE DESC", bgbkId, opbkId);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return list;
	}
	/**
	 * 查詢對映BANK_GROUP(VIEW)的所有操作行清單
	 * @return
	 */
	public List<BANK_OPBK> getAllOpbkList(){
		List<BANK_OPBK> list = null;
		try{
			list = bank_opbk_Dao.getAllOpbkList();
		}catch(Exception e){
			e.printStackTrace();
		}
		return list==null?null:(list.size()<=0?null:list);
	}
	
	/**
	 * 查詢對映MASTER_BANK_GROUP的所有操作行清單
	 * @return
	 */
	public List<BANK_OPBK> getAllOpbkListFromMaster(){
		List<BANK_OPBK> list = null;
		try{
			list = bank_opbk_Dao.getAllOpbkListFromMaster();
		}catch(Exception e){
			e.printStackTrace();
		}
		return list==null?null:(list.size()<=0?null:list);
	}
	
	public List<BANK_OPBK> getBgbkList(String opbk_id ,String s_bizdate , String e_bizdate ){
		List<BANK_OPBK> list = null;
		String sqlPath = ""; 
		Map<String,String> param = new HashMap<String, String>();
		if(StrUtils.isNotEmpty(opbk_id)){
			sqlPath = " WHERE OPBK_ID = :opbk_id ";
			param.put("opbk_id", opbk_id);
		}
		try{
			list = bank_opbk_Dao.getBgbkList(sqlPath , param , s_bizdate, e_bizdate);
		}catch(Exception e){
			e.printStackTrace();
		}
		return list==null?null:(list.size()<=0?null:list);
	}
	public List<BANK_OPBK> getCurBgbkList(String opbk_id ,String s_bizdate  ){
		List<BANK_OPBK> list = null;
		String sqlPath = ""; 
		Map<String,String> param = new HashMap<String, String>();
		if(StrUtils.isNotEmpty(opbk_id)){
			sqlPath = " WHERE OPBK_ID = :opbk_id ";
			param.put("opbk_id", opbk_id);
		}
		try{
			list = bank_opbk_Dao.getCurBgbkList(sqlPath , param , s_bizdate);
		}catch(Exception e){
			e.printStackTrace();
		}
		return list==null?null:(list.size()<=0?null:list);
	}
	
	
	
	public List<BANK_OPBK> getLastByBgbkId(String bgbkId){
		List<BANK_OPBK> list = null;
		String sql = "FROM tw.org.twntch.po.BANK_OPBK WHERE BGBK_ID = ? AND COALESCE(STOP_DATE,'') = '' ORDER BY START_DATE DESC";
		try{
			list = bank_opbk_Dao.find(sql, bgbkId);
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 取得大於營業日的操作行清單
	 * @param bgbkId
	 * @param opbk_id
	 * @param bizdate
	 * @return
	 */
	public List<BANK_OPBK> getFutureByBgbkId(String bgbkId , String bizdate){
		List<BANK_OPBK> list = null;
		String sql = "FROM tw.org.twntch.po.BANK_OPBK WHERE BGBK_ID = ?  AND START_DATE > ? ORDER BY START_DATE DESC";
		try{
			list = bank_opbk_Dao.find(sql, bgbkId  , bizdate );
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	/**
	 * 取得總行所屬操作行修改紀錄
	 * @param params
	 * @return
	 */
	public String getOpbkRec(Map<String,String> params){
		String json = "{}";
		String bgbkId = StrUtils.isNotEmpty( params.get("BGBK_ID")) ?  params.get("BGBK_ID") :"";
		String sord = StrUtils.isNotEmpty( params.get("sord")) ?  params.get("sord") :"";
		String sidx = StrUtils.isNotEmpty( params.get("sidx")) ?  params.get("sidx") :"";
		List<BANK_OPBK> list = null;
		String sqlPath = "";
//		String sql = "FROM tw.org.twntch.po.BANK_OPBK WHERE BGBK_ID = ?  ORDER BY START_DATE DESC";
		try{
			sqlPath = " ORDER BY "+sidx+" "+sord;
//			list = bank_opbk_Dao.find(sql, bgbkId);
			list = bank_opbk_Dao.getDataByBgbkId(bgbkId , sqlPath);
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
		String opbk_Id = StrUtils.isNotEmpty( params.get("OPBK_ID")) ?  params.get("OPBK_ID") :"";
		String start_Date = StrUtils.isNotEmpty( params.get("START_DATE")) ?  params.get("START_DATE") :"";
		List<BANK_OPBK> list = null;
		String sql = "FROM tw.org.twntch.po.BANK_OPBK WHERE BGBK_ID = ? AND OPBK_ID = ? AND START_DATE =? ";
		try{
			list = bank_opbk_Dao.find(sql, bgbk_Id , opbk_Id , start_Date);
			if(list !=null && list.size() !=0){
				json = JSONUtils.toJson(list.get(0));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("getOne.json>>"+json);
		return json;
	}
	
	public BANK_OPBK_Dao getBank_opbk_Dao() {
		return bank_opbk_Dao;
	}

	public void setBank_opbk_Dao(BANK_OPBK_Dao bank_opbk_Dao) {
		this.bank_opbk_Dao = bank_opbk_Dao;
	}
}
