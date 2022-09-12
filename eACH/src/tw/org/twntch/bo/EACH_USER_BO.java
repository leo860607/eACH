package tw.org.twntch.bo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts.util.LabelValueBean;

import tw.org.twntch.db.dao.hibernate.BANK_GROUP_Dao;
import tw.org.twntch.db.dao.hibernate.EACH_ROLE_LIST_Dao;
import tw.org.twntch.db.dao.hibernate.EACH_USER_Dao;
import tw.org.twntch.db.dao.hibernate.SC_COMPANY_PROFILE_Dao;
import tw.org.twntch.db.dao.hibernate.SD_COMPANY_PROFILE_Dao;
import tw.org.twntch.db.dao.hibernate.SYS_PARA_Dao;
import tw.org.twntch.po.AGENT_PROFILE;
import tw.org.twntch.po.BANK_GROUP;
import tw.org.twntch.po.EACH_ROLE_LIST;
import tw.org.twntch.po.EACH_USER;
import tw.org.twntch.po.EACH_USER_PK;
import tw.org.twntch.po.SYS_PARA;
import tw.org.twntch.util.BeanUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.SpringAppCtxHelper;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.zDateHandler;

public class EACH_USER_BO {
	private Logger logger = Logger.getLogger(getClass());

	private EACH_USER_Dao each_user_Dao;
	private EACH_ROLE_LIST_Dao role_list_Dao;
	private BANK_GROUP_Dao bank_group_Dao ;
	private SYS_PARA_Dao sys_para_Dao;
	private SC_COMPANY_PROFILE_Dao sc_com_Dao ;
	private SD_COMPANY_PROFILE_Dao sd_com_Dao ;
	private AGENT_PROFILE_BO agent_profile_bo ;
	
//TODO 未完成 預計用來檢核發動者統編是否合法	
public String checkIsSCSD(Map<String,String> param){
	String userCompany = StrUtils.isNotEmpty(param.get("userCompany"))?param.get("userCompany"):"";
	return null;
}
	
public int getSysParaTimeOut(){
	int timeOut = 0;
	List<SYS_PARA> list = sys_para_Dao.getTopOne();
	if(list != null && list.size() !=0 ){
		for( SYS_PARA po :list){
			timeOut = po.getTIMEOUT_TIME();
		}
	}
	return timeOut;
}	
	
public String reset_Login_Date(Map<String, String> param){
	String json ="";
	Map map = new HashMap<>();
	try {
		String dateStr = zDateHandler.getTheDateII()+" "+zDateHandler.getTheTime();
		map.put("result", "TRUE");
		map.put("data", dateStr);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		map.put("result", "ERROR");
		map.put("msg", "ERROR");
	}
	json = JSONUtils.map2json(map);
	
	System.out.println("json>>"+json);
	return json;
}
	
public Map<String,String> delete(String user_id,String user_comId ){
	Map<String, String> map = null;
	Map<String, String> pkmap = new HashMap<String,String>();//e
	EACH_USER po =null ; //e
	try {
		map = new HashMap<String, String>();
		pkmap.put("USER_ID", user_id);
		po = each_user_Dao.get(user_id);
//		po = null ; //故障測試用
		if(po == null ){
			map = each_user_Dao.removeFail(po, pkmap, "刪除失敗，查無資料", 1);
			return map;
		}
//		each_user_Dao.remove(po);
//		map = null ;map.get(""); //故障測試用
		each_user_Dao.removeII(po, pkmap);
		map.put("result", "TRUE");
		map.put("msg", "刪除成功");
		map.put("target", "search");
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		map = each_user_Dao.removeFail(po, pkmap, "刪除失敗，系統異常", 2);
		return map;
	}
	return map;
}

	public Map<String,String> save(String user_id,String user_comId ,Map formMap){
		Map<String, String> map = null;
		Map<String, String> pkmap = new HashMap<String, String>();//e
		EACH_USER po = null;//e
		String user_type = "";
		try {
			map = new HashMap<String, String>();
			pkmap.put("USER_ID", user_id) ;//e
			po = each_user_Dao.get(user_id);
			if(po != null ){
				BeanUtils.populate(po, formMap);//e
				map = each_user_Dao.saveFail(po,pkmap, "儲存失敗，資料重複" ,1);//e
				return map;
			}
			po = new EACH_USER();
			BeanUtils.populate(po, formMap);
			System.out.println(">>>>>"+formMap.get("TMPUSER_TYPE"));
			user_type = formMap.get("TMPUSER_TYPE") != null && StrUtils.isNotEmpty(formMap.get("TMPUSER_TYPE").toString())?formMap.get("TMPUSER_TYPE").toString():user_type;
			if(StrUtils.isNotEmpty(user_type)){
				po.setUSER_TYPE(user_type);
			}
			//			po = null; //故障測試用
			po.setLAST_LOGIN_DATE(null);
			po.setCDATE(zDateHandler.getTheDateII());
			each_user_Dao.save(po);
			map.put("result", "TRUE");
			map.put("msg", "儲存成功");
			map.put("target", "search");
		}catch (Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("EACH_USER_BO.save.Exception>>"+e);//e
			map = each_user_Dao.saveFail(po,pkmap, "儲存失敗，系統異常" ,2);//e
			return map;
		}
		return map;
	}
	
	
	public Map<String,String> update(String user_id,String user_comId ,Map formMap){
		Map<String, String> map = null;
		Map<String, String> oldMap = null; //e
		Map<String, String> pkmap = new HashMap<String, String>(); //e
		EACH_USER po = null ;
		String user_type = "";
		try {
			map = new HashMap<String, String>();
			po = each_user_Dao.get(user_id);
			pkmap.put("USER_ID", user_id);//e
			if(po == null ){
				po = new EACH_USER();//e
				BeanUtils.populate(po, formMap);//e
				map = each_user_Dao.updateFail(po, null, pkmap, "修改-儲存失敗，查無資料", 1);
				return map;
			}
			oldMap = BeanUtils.describe(po);
//			此欄位原則上是由系統異動，特述狀況油使用者編輯功能更新
			if(StrUtils.isEmpty(String.valueOf(formMap.get("LAST_LOGIN_DATE")))){
				formMap.put("LAST_LOGIN_DATE" , po.getLAST_LOGIN_DATE());
			}
//			此欄位目前是由系統異動
			formMap.put("LAST_LOGIN_IP" , po.getLAST_LOGIN_IP());
//			20150622 edit by hugo 避免使用者按下修改時 ，將CDATE清空 導致後續使用者無法登入的情況
			formMap.put("CDATE" , po.getCDATE());
			BeanUtils.populate(po, formMap);
			
			user_type = formMap.get("TMPUSER_TYPE") != null && StrUtils.isNotEmpty(formMap.get("TMPUSER_TYPE").toString())?formMap.get("TMPUSER_TYPE").toString():user_type;
			if(StrUtils.isNotEmpty(user_type)){
				po.setUSER_TYPE(user_type);
			}
			po.setUDATE(zDateHandler.getTheDateII());
//			each_user_Dao.save(po);
//			Map testmap = null ;testmap.put("a", "c"); // 故障測試 用
			map.put("USER_ID", po.getUSER_ID());//e
			each_user_Dao.saveII(po, oldMap , map);//e
			map.clear();//e
			map.put("result", "TRUE");
			map.put("msg", "儲存成功");
			map.put("target", "search");
		}catch (Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();
//			map.put("result", "ERROR");
//			map.put("msg", "儲存失敗，系統異常:"+e);
//			map.put("target", "edit_p");
			map = each_user_Dao.updateFail(po, oldMap, pkmap, "修改-儲存失敗，系統異常", 2);
			return map;
		}
		return map;
	}
	
	
	
	public List<EACH_USER> search(String user_id,String user_company , String user_type ,String orderSQL){
		List<EACH_USER> list = new ArrayList<EACH_USER>();
		try {
			if(StrUtils.isEmpty(user_id) && StrUtils.isEmpty(user_company)&& StrUtils.isEmpty(user_type) ){
//			list = each_user_Dao.getAll();
				list = each_user_Dao.getAllData(orderSQL);
			}else {
				StringBuffer sql = new StringBuffer();
//				20160311 edit by hugo req by UAT-2016310-03
//				sql.append( " SELECT  CASE  A.USER_TYPE WHEN 'C' THEN  CASE  WHEN SC.COMPANY_ID IS NULL THEN RTRIM(SD.COMPANY_ID) ||'-'|| SD.COMPANY_NAME ELSE RTRIM(SC.COMPANY_ID) ||'-'|| SC.COMPANY_NAME END  ELSE A.USER_COMPANY ||'-'|| B.BGBK_NAME   END  AS COM_NAME  " );
				sql.append( " SELECT CASE  A.USER_TYPE WHEN 'C' THEN  A.USER_COMPANY ||'-'|| COALESCE ((SELECT COMPANY_ABBR_NAME FROM AGENT_PROFILE WHERE COMPANY_ID = A.USER_COMPANY ),'')  ELSE A.USER_COMPANY ||'-'|| B.BGBK_NAME   END  AS COM_NAME    " );
				sql.append( " , R.ROLE_ID||'-'||R.ROLE_NAME ROLE_ID ,A.* " );
				sql.append( " FROM EACH_USER A " );
				sql.append( " LEFT JOIN EACH_ROLE_LIST R ON A.ROLE_ID = R.ROLE_ID " );
				sql.append( " LEFT JOIN BANK_GROUP B  ON A.USER_COMPANY = B.BGBK_ID " );
				sql.append( " WHERE " );
				Map<String , String> keyMap = new HashMap<String,String>();
				List<String> strList = new LinkedList<String>();
				keyMap.put("USER_ID", user_id);
				keyMap.put("USER_COMPANY", user_company);
				keyMap.put("USER_TYPE", user_type);
				System.out.println("keyMap>>"+keyMap);
				int i =0;
				for(String key : keyMap.keySet()){
					if(StrUtils.isNotEmpty(keyMap.get(key))){
						if(i!=0){
							sql.append(" AND ");
						}
//						20150623 edit by hugo req by UAT-2015018-26
//						if(keyMap.get("USER_TYPE").equals("B") ){
						if(  key.equals("USER_TYPE") && keyMap.get(key).equals("B") ){
							sql.append(" USER_TYPE != 'A' ");
						}else{
							sql.append(key+" = ?");
							strList.add(keyMap.get(key));
						}
						i++;
					}
				}
//			list = each_user_Dao.find(sql.toString(), strList.toArray());
				sql.append(orderSQL);
//				sql.append("ORDER BY A.USER_ID ");
				System.out.println("SQL>>"+sql);
				list = each_user_Dao.getData(sql.toString(),  strList);
			}
				
			System.out.println("EACH_USER.list>>"+list);
			list = list!=null && list.size()  ==0 ?null:list;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list ; 
	}
	public List<EACH_USER> search4ACH(String user_id,String user_company , String user_type , String orderSQL){
		List<EACH_USER> list = new ArrayList<EACH_USER>();
		try {
			StringBuffer sql = new StringBuffer();
			sql.append( " SELECT '0188888-臺灣票據交換所' COM_NAME, value( R.ROLE_ID||'-'||R.ROLE_NAME ,'尚未指定群組') ROLE_ID ,A.* FROM EACH_USER A  " );
			sql.append( " LEFT JOIN EACH_ROLE_LIST R ON A.ROLE_ID = R.ROLE_ID  " );
			Map<String , String> keyMap = new HashMap<String,String>();
			List<String> strList = new LinkedList<String>();
			keyMap.put("USER_ID", user_id);
			keyMap.put("USER_COMPANY", user_company);
			keyMap.put("USER_TYPE", user_type);
			int i =0;
			for(String key : keyMap.keySet()){
				if(StrUtils.isNotEmpty(keyMap.get(key))){
					if(i!=0){
						sql.append(" AND ");
					}else{
						sql.append(" WHERE ");
					}
					sql.append(key+" = ? ");
					strList.add(keyMap.get(key));
					i++;
				}
			}
			sql.append(orderSQL);
			list = each_user_Dao.getData(sql.toString(),  strList);
			
			System.out.println("EACH_USER.list>>"+list);
			list = list ==null  ?new ArrayList<EACH_USER>():list;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list ; 
	}
	
	/**
	 * 
	 * @param user_id
	 * @param com_id
	 * @return
	 */
	public List<EACH_USER> searchByPK(String user_id,String com_id ){
		List<EACH_USER> list = new ArrayList<EACH_USER>();
//		原則上user_comId為必填
		if(StrUtils.isNotEmpty(user_id) && StrUtils.isNotEmpty(com_id)){
			StringBuffer sql = new StringBuffer();
			sql.append( " FROM tw.org.twntch.po.EACH_USER WHERE USER_ID =? AND USER_COMPANY=? " );
			
			list = each_user_Dao.find(sql.toString(), user_id , com_id);
		}
		System.out.println("searchByPK.list>>"+list);
		list = list!=null && list.size()  ==0 ?null:list;
		return list ; 
	}
	
	public EACH_USER searchByPK(String user_id ){
		EACH_USER po =null ;
		System.out.println("user_id>>"+user_id);
		if(StrUtils.isNotEmpty(user_id) ){
			po = each_user_Dao.get(user_id.trim());
		}
		System.out.println("po>>"+po);
		return po ; 
	}
	
	public List<LabelValueBean> getRole_ListByUser_Type(String role_type){
		List<EACH_ROLE_LIST> list = role_list_Dao.getDataByType(role_type);
		List<LabelValueBean> beanList = new LinkedList<LabelValueBean>();
		LabelValueBean bean = null;
		for(EACH_ROLE_LIST po :list){
			bean = new LabelValueBean(po.getROLE_NAME(), po.getROLE_ID());
			beanList.add(bean);
		}
		return beanList;
	}
	
	public String search_toJson(Map<String, String> param){
		String jsonStr = "{}";
		String user_id =StrUtils.isNotEmpty(param.get("USER_ID"))? param.get("USER_ID"):"";
		String user_company =StrUtils.isNotEmpty(param.get("USER_COMPANY"))? param.get("USER_COMPANY"):"";
		String user_type =StrUtils.isNotEmpty(param.get("USER_TYPE"))? param.get("USER_TYPE"):"";
		logger.debug("user_id>>"+user_id);
		String sord =StrUtils.isNotEmpty(param.get("sord"))? param.get("sord"):"";
		String sidx =StrUtils.isNotEmpty(param.get("sidx"))? param.get("sidx"):"";
		String orderSQL = StrUtils.isNotEmpty(sidx)? " ORDER BY "+sidx+" "+sord:"";
		
		List<EACH_USER> list = new LinkedList<EACH_USER>();
		if(user_type.equals("A")){
			list = search4ACH(user_id, user_company, user_type , orderSQL);
		}else{
			list = search(user_id, user_company , user_type , orderSQL);
		}
		if(list!=null){
			System.out.println("search_toJson.list.size>>"+list.size());
			jsonStr = JSONUtils.toJson(list);
		}
		System.out.println("json"+jsonStr);
		return jsonStr;
	}
	public String searchBank_toJson(Map<String, String> param){
		String user_id =StrUtils.isNotEmpty(param.get("USER_ID"))? param.get("USER_ID"):"";
		String com_id =StrUtils.isNotEmpty(param.get("USER_COMPANY"))? param.get("USER_COMPANY"):"";
		String user_type =StrUtils.isNotEmpty(param.get("USER_TYPE"))? param.get("USER_TYPE"):"";
//		String jsonStr = JSONUtils.toJson(findByPK(user_id, com_id));
		String sord =StrUtils.isNotEmpty(param.get("sord"))? param.get("sord"):"";
		String sidx =StrUtils.isNotEmpty(param.get("sidx"))? param.get("sidx"):"";
		String orderSQL = StrUtils.isNotEmpty(sidx)? " ORDER BY "+sidx+" "+sord:"";
		String jsonStr = JSONUtils.toJson(search(user_id,com_id ,  user_type , orderSQL));
		System.out.println("json"+jsonStr);
		return jsonStr;
	}
	
	public String getRole_ListByRole_Type(Map<String, String> param){
		String role_type = StrUtils.isNotEmpty(param.get("role_type")) ?param.get("role_type"):"";
		System.out.println("role_type>>"+role_type);
		List<EACH_ROLE_LIST> list = role_list_Dao.getDataByType(role_type);
		List<LabelValueBean> beanList = new LinkedList<LabelValueBean>();
		LabelValueBean bean = null;
		for(EACH_ROLE_LIST po :list){
			bean = new LabelValueBean(po.getROLE_NAME(), po.getROLE_ID());
			beanList.add(bean);
		}
		System.out.println("beanList>>"+beanList);
		return JSONUtils.toJson(beanList);
	}
	public String getBgbkListByUser_Type(Map<String, String> param){
		String user_type = StrUtils.isNotEmpty(param.get("user_type")) ?param.get("user_type"):"";
		List<LabelValueBean> beanList = new LinkedList<LabelValueBean>();
		List<BANK_GROUP> list = null ;
		LabelValueBean bean = null;
		System.out.println("user_type>>>"+user_type);
		if(user_type.equals("A")){
			list = bank_group_Dao.getBgbkIdList_ACH();
			for(BANK_GROUP po : list){
				bean = new LabelValueBean(po.getBGBK_ID() + " - " + po.getBGBK_NAME(), po.getBGBK_ID());
				beanList.add(bean);
			}
		}
		if(user_type.equals("B")){
			list = bank_group_Dao.getBgbkIdListwithoutACH();
			for(BANK_GROUP po : list){
				bean = new LabelValueBean(po.getBGBK_ID() + " - " + po.getBGBK_NAME(), po.getBGBK_ID());
				beanList.add(bean);
			}
		}
		agent_profile_bo = (AGENT_PROFILE_BO) (agent_profile_bo==null ? SpringAppCtxHelper.getBean("agent_profile_bo"):agent_profile_bo);
		if(user_type.equals("C")){
			return JSONUtils.toJson(agent_profile_bo.getCompany_Id_List());
		}
		return JSONUtils.toJson(beanList);
	}
	
	public EACH_USER_Dao getEach_user_Dao() {
		return each_user_Dao;
	}

	public void setEach_user_Dao(EACH_USER_Dao each_user_Dao) {
		this.each_user_Dao = each_user_Dao;
	}

	public EACH_ROLE_LIST_Dao getRole_list_Dao() {
		return role_list_Dao;
	}

	public void setRole_list_Dao(EACH_ROLE_LIST_Dao role_list_Dao) {
		this.role_list_Dao = role_list_Dao;
	}

	public BANK_GROUP_Dao getBank_group_Dao() {
		return bank_group_Dao;
	}

	public void setBank_group_Dao(BANK_GROUP_Dao bank_group_Dao) {
		this.bank_group_Dao = bank_group_Dao;
	}

	public SYS_PARA_Dao getSys_para_Dao() {
		return sys_para_Dao;
	}

	public void setSys_para_Dao(SYS_PARA_Dao sys_para_Dao) {
		this.sys_para_Dao = sys_para_Dao;
	}

	public SC_COMPANY_PROFILE_Dao getSc_com_Dao() {
		return sc_com_Dao;
	}

	public void setSc_com_Dao(SC_COMPANY_PROFILE_Dao sc_com_Dao) {
		this.sc_com_Dao = sc_com_Dao;
	}

	public SD_COMPANY_PROFILE_Dao getSd_com_Dao() {
		return sd_com_Dao;
	}

	public void setSd_com_Dao(SD_COMPANY_PROFILE_Dao sd_com_Dao) {
		this.sd_com_Dao = sd_com_Dao;
	}

	public AGENT_PROFILE_BO getAgent_profile_bo() {
		return agent_profile_bo;
	}

	public void setAgent_profile_bo(AGENT_PROFILE_BO agent_profile_bo) {
		this.agent_profile_bo = agent_profile_bo;
	}

	
	
	
	
}
