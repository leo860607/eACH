package tw.org.twntch.bo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import tw.org.twntch.db.dao.hibernate.BANK_BRANCH_Dao;
import tw.org.twntch.db.dao.hibernate.BANK_GROUP_Dao;
import tw.org.twntch.db.dao.hibernate.Page;
import tw.org.twntch.po.BANK_BRANCH;
import tw.org.twntch.po.BANK_BRANCH_PK;
import tw.org.twntch.po.BANK_GROUP;
import tw.org.twntch.util.BeanUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.zDateHandler;

public class BANK_BRANCH_BO {
private BANK_BRANCH_Dao bank_branch_Dao;
private BANK_GROUP_Dao bank_group_Dao;

public String pageSearch(Map<String, String> param){
	String paramName;
	paramName = "BGBK_ID";
	String BGBK_ID = StrUtils.isNotEmpty(param.get(paramName))? param.get(paramName): "";
	paramName = "BRBK_ID";
	String BRBK_ID = StrUtils.isNotEmpty(param.get(paramName))? param.get(paramName): "";
	
	int pageNo = StrUtils.isEmpty(param.get("page"))? 0 : Integer.valueOf(param.get("page"));
	int pageSize = StrUtils.isEmpty(param.get("rows"))? Integer.valueOf(Arguments.getStringArg("PAGE.SIZE")) : Integer.valueOf(param.get("rows"));
	String countQuerySql = "" ,sql = "" , result = "";
	String[] cols = "".split(",");
	List list = null;
	Map rtnMap = null;
	Map conditionMap = null;
	Page page = null;
	try {
		String conditionColsKey = "BGBK_ID,BRBK_ID";
		conditionMap = getPathSQL(param, Arrays.asList(conditionColsKey.split(",")));
		String hql = getSQL(conditionMap.get("sqlPath").toString(), getOrderBySQL(param)) ;
//		String hql = " FROM tw.org.twntch.po.BANK_BRANCH WHERE BGBK_ID = ? AND BRBK_ID = ?" ;
		page = bank_branch_Dao.pagedQuery(hql, pageNo, pageSize,  ((List<String>) conditionMap.get("values")).toArray());
		list = (List) page.getResult();
		rtnMap = new HashMap();
		rtnMap.put("total", page.getTotalPageCount());
		rtnMap.put("page", String.valueOf(page.getCurrentPageNo()));
		rtnMap.put("records", page.getTotalCount());
		rtnMap.put("rows", list);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		rtnMap.put("total", "0");
		rtnMap.put("page", "0");
		rtnMap.put("records", "0");
		rtnMap.put("rows", new ArrayList());
	}
	result = JSONUtils.map2json(rtnMap) ;
	System.out.println("FAIL_TRANS_BO>>"+result);
	return result;
}

public String getSQL(String sqlPath ,String orderbySQL){
	StringBuffer sql = new StringBuffer();
	sql.append("  FROM tw.org.twntch.po.BANK_BRANCH ");
	sql.append(sqlPath);
	sql.append(orderbySQL);
	System.out.println("getSQL.sql>>"+sql);
	return sql.toString();
}



public Map getPathSQL(Map<String, String> param , List<String> list){
	StringBuffer sql = new StringBuffer();
	List values = new LinkedList();
	Map map = new HashMap();
	for(String key :list){
		param.get(key);
		if(StrUtils.isNotEmpty(param.get(key))){
			if(list.indexOf(key) == 0){ sql.append(" WHERE "); }
			if(list.indexOf(key) != 0){ sql.append(" AND "); }
			sql.append(key+" = ?");
			values.add(param.get(key));
		}
	}
	sql.append(" ");
	map.put("sqlPath", sql.toString());
	map.put("values", values);
	System.out.println("getPathSQL.map>>"+map);
	return map;
}

public String getOrderBySQL(Map<String, String> param ){
	String sortType = StrUtils.isNotEmpty(param.get("sord")) ?param.get("sord"):"";
	String sortCols = StrUtils.isNotEmpty(param.get("sidx")) ?param.get("sidx"):"";
	StringBuffer sql = new StringBuffer();
	sql = StrUtils.isNotEmpty(param.get("sidx"))?sql.append(" ORDER BY "+sortCols+" "+sortType):sql.append("  ORDER BY BGBK_ID,BRBK_ID ");
	System.out.println("OrderBySQL>>"+sql);
	return sql.toString();
}


public Map<String,String> delete(String bgid ,String brid ){
	Map<String, String> map = null;
	Map<String, String> pkmap = new HashMap<String, String>();
	BANK_BRANCH po =null;
	try {
		map = new HashMap<String, String>();
		BANK_BRANCH_PK id = new BANK_BRANCH_PK(bgid, brid);
		pkmap = BeanUtils.describe(id);
		po = bank_branch_Dao.get(id);
		if(po == null ){
//			map.put("result", "FALSE");
//			map.put("msg", "刪除失敗，查無資料");
//			map.put("target", "edit_p");
			map = bank_branch_Dao.removeFail(po, pkmap, "刪除失敗，查無資料", 1);
			return map;
		}
//		bank_branch_Dao.remove(po);
		bank_branch_Dao.removeII(po, pkmap);
		map.put("result", "TRUE");
		map.put("msg", "刪除成功");
		map.put("target", "search");
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
//		map.put("result", "ERROR");
//		map.put("msg", "刪除失敗，系統異常:"+e);
//		map.put("target", "edit_p");
		map = bank_branch_Dao.removeFail(po, pkmap, "刪除失敗，系統異常", 2);
		return map;
	}
	return map;
}

	/**
	 * 新增分行檔案
	 * @param bgid
	 * @param brid
	 * @param formMap
	 * @return
	 */
	public Map<String,String> save(String bgid ,String brid ,Map formMap){
		Map<String, String> map = null;
		Map<String, String> pkmap = new HashMap<String, String>();//e
		BANK_BRANCH po = null;
		try {
			map = new HashMap<String, String>();
			BANK_BRANCH_PK id = new BANK_BRANCH_PK(bgid, brid);
			pkmap = BeanUtils.describe(id);
			po = bank_branch_Dao.get(id);
			if(po != null ){
//				map.put("result", "FALSE");
//				map.put("msg", "儲存失敗，id重複");
//				map.put("target", "add_p");
				map = bank_branch_Dao.saveFail(po,pkmap, "儲存失敗，資料重複" ,1);//e
				return map;
			}
			po = new BANK_BRANCH();
			BeanUtils.populate(po, formMap);
			po.setId(id);
			po.setCDATE(zDateHandler.getTheDateII());
			
			//bank_branch_Dao.save(po);
			bank_branch_Dao.save(po, pkmap);
			
			map.put("result", "TRUE");
			map.put("msg", "儲存成功");
			map.put("target", "search");
		}catch (Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();
			map.put("result", "FALSE");
			map.put("msg", "儲存失敗，系統異常");
			map.put("target", "add_p");
//			map = bank_branch_Dao.saveFail(po,pkmap, "儲存失敗，系統異常" ,2);
			return map;
		}
		return map;
	}
	
	
	/**
	 * 修改分行檔案
	 * @param bgid
	 * @param brid
	 * @param formMap
	 * @return
	 */
	public Map<String,String> update(String bgid ,String brid ,Map formMap){
		Map<String, String> map = null;
		Map<String, String> pkmap = new HashMap<String, String>();//e
		Map<String, String> oldmap = new HashMap<String, String>();//e
		BANK_BRANCH po= null ;
		try {
			map = new HashMap<String, String>();
			BANK_BRANCH_PK id = new BANK_BRANCH_PK(bgid, brid);
			po = bank_branch_Dao.get(id);
			pkmap = BeanUtils.describe(id);
			if(po == null ){
//				map.put("result", "FALSE");
//				map.put("msg", "儲存失敗，查無資料");
//				map.put("target", "edit_p");
				BeanUtils.populate(po, formMap);
				map = bank_branch_Dao.updateFail(po, oldmap, pkmap, "儲存失敗，查無資料", 1);
				return map;
			}
			oldmap = BeanUtils.describe(po);
			po = new BANK_BRANCH();
			BeanUtils.populate(po, formMap);
			po.setId(id);
			po.setUDATE(zDateHandler.getTheDateII());
//			bank_branch_Dao.save(po);
			bank_branch_Dao.saveII(po, oldmap, pkmap);
			map.put("result", "TRUE");
			map.put("msg", "儲存成功");
			map.put("target", "search");
		}catch (Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();
//			map.put("result", "ERROR");
//			map.put("msg", "儲存失敗，系統異常:"+e);
//			map.put("target", "edit_p");
			map = bank_branch_Dao.updateFail(po, oldmap, pkmap, "儲存失敗，系統異常", 2);
			return map;
		}
		return map;
	}

	public List<BANK_BRANCH> search(String bgid , String brid  ){
		List<BANK_BRANCH> list = new ArrayList<BANK_BRANCH>();
		BANK_BRANCH_PK id = null;
		if(StrUtils.isEmpty(bgid) && brid.equalsIgnoreCase("all")){
			list = bank_branch_Dao.getAll();
		}else if(StrUtils.isNotEmpty(bgid) && brid.equalsIgnoreCase("all")){
			list = bank_branch_Dao.getDataByBgBkId(bgid);
		}else if(StrUtils.isNotEmpty(bgid) && StrUtils.isNotEmpty(brid)){
			String arg[] = {bgid, brid};
			list = bank_branch_Dao.find("FROM tw.org.twntch.po.BANK_BRANCH WHERE BGBK_ID = ? AND BRBK_ID = ? ", arg);
		}
		//System.out.println("list>>"+list);
		list = (list == null)? null : (list.size() == 0)? null : list;
		return list;
	}
	
	public List<BANK_BRANCH> search(String bgid , String brid  , String orderSQL){
		List<BANK_BRANCH> list = new ArrayList<BANK_BRANCH>();
		BANK_BRANCH_PK id = null;
		if(StrUtils.isEmpty(bgid) && brid.equalsIgnoreCase("all")){
			list = bank_branch_Dao.getAll();
		}else if(StrUtils.isNotEmpty(bgid) && brid.equalsIgnoreCase("all")){
			list = bank_branch_Dao.getDataByBgBkId(bgid);
		}else if(StrUtils.isNotEmpty(bgid) && StrUtils.isNotEmpty(brid)){
			String arg[] = {bgid, brid};
			list = bank_branch_Dao.find("FROM tw.org.twntch.po.BANK_BRANCH WHERE BGBK_ID = ? AND BRBK_ID = ? "+orderSQL, arg);
		}
		//System.out.println("list>>"+list);
		list = (list == null)? null : (list.size() == 0)? null : list;
		return list;
	}
	
	public String search_toJson(Map<String, String> params){
		String paramName;
		String paramValue;
		
		String BGBK_ID = "";
		paramName = "BGBK_ID";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			BGBK_ID = paramValue;
		}
		
		String BRBK_ID = "";
		paramName = "BRBK_ID";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			BRBK_ID = paramValue;
		}
		String sord =StrUtils.isNotEmpty(params.get("sord"))? params.get("sord"):"";
		String sidx =StrUtils.isNotEmpty(params.get("sidx"))? params.get("sidx"):"";
		String orderSQL = StrUtils.isNotEmpty(sidx)? " ORDER BY "+sidx+" "+sord:"";
		System.out.println("orderSQL>>"+orderSQL);
		
		return JSONUtils.toJson(search(BGBK_ID, BRBK_ID));
	}
	
	public List<BANK_BRANCH> searchByBrbkId(String brbkId){
		List<BANK_BRANCH> list = new ArrayList<BANK_BRANCH>();
		if(StrUtils.isNotEmpty(brbkId)){
			list = bank_branch_Dao.findBy("id.BRBK_ID", brbkId);
		}
		System.out.println("list>>"+list);
		list = (list == null)? null : (list.size() == 0)? null : list;
		return list;
	}
	
	public String getBank_branch_List(Map<String, String> params){
		String bgbkId = StrUtils.isEmpty(params.get("bgbkId"))?"":params.get("bgbkId");
		List<Map> listRet = new ArrayList<Map>();
		Map<String,String> m = new HashMap<String,String>();
		Map reultMap = new HashMap();
		List<BANK_BRANCH> list = null;
		List<BANK_GROUP> feeBranch = null;
		String json= "";
		try {
			if(StrUtils.isEmpty(bgbkId)){
				list = bank_branch_Dao.getAll();
			}else{
				list = bank_branch_Dao.getDataByBgBkId(bgbkId);
				feeBranch = bank_group_Dao.getfeeBranch(bgbkId);
			}
			if(list!=null){
				for(BANK_BRANCH po :list){
					m = new HashMap<String,String>();
					m.put("label", po.getId().getBRBK_ID()+" - "+po.getBRBK_NAME());
					m.put("value", po.getId().getBRBK_ID());
					listRet.add(m);
				}
				if(feeBranch!=null){
					for(BANK_GROUP po :feeBranch){
						if( !("".equals(po.getIN_FEE_BRBK_ID()) && "".equals(po.getSND_FEE_BRBK_ID()) && "".equals(po.getOUT_FEE_BRBK_ID()) ) ){
							reultMap.put("feeBranch", "TRUE");
						}
					}
				}
				reultMap.put("result", "TRUE");
				reultMap.put("msg", listRet);
			}else{
				reultMap.put("result", "FALSE");
				reultMap.put("msg", "無分行資料");
			}
			json = JSONUtils.map2json(reultMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("getBank_branch_List.Exception>>"+e);
		}
		return json;
	}
	
	public String getBranchName(Map<String, String> params){
		Map result = null;
		String paramName;
		String paramValue;
		
		String brbkId = "";
		paramName = "brbkId";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			brbkId = paramValue;
		}
		
		List<BANK_BRANCH> list = null;
		try{
			list = bank_branch_Dao.getBranchName(brbkId);
			if(list != null && list.size() == 1){
				result = tw.org.twntch.util.BeanUtils.describe(list.get(0));
			}
			System.out.println(result);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		if(result != null){
			return JSONUtils.map2json(result);
		}
		
		return null;
	}

	public BANK_BRANCH_Dao getBank_branch_Dao() {
		return bank_branch_Dao;
	}

	public void setBank_branch_Dao(BANK_BRANCH_Dao bank_branch_Dao) {
		this.bank_branch_Dao = bank_branch_Dao;
	}

	public BANK_GROUP_Dao getBank_group_Dao() {
		return bank_group_Dao;
	}

	public void setBank_group_Dao(BANK_GROUP_Dao bank_group_Dao) {
		this.bank_group_Dao = bank_group_Dao;
	}
	
	
}
