package tw.org.twntch.bo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tw.org.twntch.db.dao.hibernate.EACH_BATCH_DEF_Dao;
import tw.org.twntch.po.EACH_BATCH_DEF;
import tw.org.twntch.util.BeanUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.StrUtils;

public class EACH_BATCH_DEF_BO {
private EACH_BATCH_DEF_Dao batch_def_Dao  ;


public Map<String,String> save(String batch_proc_seq ,Map formMap){
	Map<String, String> map = null;
	Map<String, String> pkmap = new HashMap<String, String>();//e
	EACH_BATCH_DEF po = null;//e
	try {
		map = new HashMap<String, String>();
		pkmap.put("BATCH_PROC_SEQ", batch_proc_seq) ;//e
		po = batch_def_Dao.get(Integer.valueOf(batch_proc_seq));
		if(po != null ){
			BeanUtils.populate(po, formMap);//e
			map = batch_def_Dao.saveFail(po,pkmap, "儲存失敗，資料重複" ,1);//e
			return map;
		}
		po = new EACH_BATCH_DEF();
		BeanUtils.populate(po, formMap);
//		po = null; //故障測試用
		batch_def_Dao.save(po);
		map.put("result", "TRUE");
		map.put("msg", "儲存成功");
		map.put("target", "search");
	}catch (Exception e){
		// TODO Auto-generated catch block
		e.printStackTrace();
		System.out.println("EACH_BATCH_DEF_BO.save.Exception>>"+e);//e
		map = batch_def_Dao.saveFail(po,pkmap, "儲存失敗，系統異常" ,2);//e
		return map;
	}
	return map;
}


public Map<String,String> update(String batch_proc_seq ,Map formMap){
	Map<String, String> map = null;
	Map<String, String> oldMap = null; //e
	Map<String, String> pkmap = new HashMap<String, String>(); //e
	EACH_BATCH_DEF po = null ;
	try {
		map = new HashMap<String, String>();
		po = batch_def_Dao.get(Integer.valueOf (batch_proc_seq));
		pkmap.put("BATCH_PROC_SEQ", batch_proc_seq);//e
		if(po == null ){
			po = new EACH_BATCH_DEF();//e
			BeanUtils.populate(po, formMap);//e
			map = batch_def_Dao.updateFail(po, null, pkmap, "修改-儲存失敗，查無資料", 1);
			return map;
		}
		oldMap = BeanUtils.describe(po);
		BeanUtils.populate(po, formMap);
//		Map testmap = null ;testmap.put("a", "c"); // 故障測試 用
		map.put("BATCH_PROC_SEQ", batch_proc_seq);;//e
		batch_def_Dao.saveII(po, oldMap , map);//e
		map.clear();//e
		map.put("result", "TRUE");
		map.put("msg", "儲存成功");
		map.put("target", "search");
	}catch (Exception e){
		// TODO Auto-generated catch block
		e.printStackTrace();
		map = batch_def_Dao.updateFail(po, oldMap, pkmap, "修改-儲存失敗，系統異常", 2);
		return map;
	}
	return map;
}



public Map<String,String> delete(String batch_proc_seq ){
	Map<String, String> map = null;
	Map<String, String> pkmap = new HashMap<String,String>();//e
	EACH_BATCH_DEF po =null ; //e
	try {
		map = new HashMap<String, String>();
		pkmap.put("BATCH_PROC_SEQ", batch_proc_seq);
		po = batch_def_Dao.get(Integer.valueOf (batch_proc_seq));
//		po = null ; //故障測試用
		if(po == null ){
			map = batch_def_Dao.removeFail(po, pkmap, "刪除失敗，查無資料", 1);
			return map;
		}
//		batch_def_Dao.remove(po);
//		map = null ;map.get(""); //故障測試用
		batch_def_Dao.removeII(po, pkmap);
		map.put("result", "TRUE");
		map.put("msg", "刪除成功");
		map.put("target", "search");
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		map = batch_def_Dao.removeFail(po, pkmap, "刪除失敗，系統異常", 2);
		return map;
	}
	return map;
}


public String search_toJson(Map<String, String> param){
	String id =StrUtils.isNotEmpty(param.get("BANK_ID"))? param.get("BANK_ID"):"";
	String sord =StrUtils.isNotEmpty(param.get("sord"))? param.get("sord"):"";
	String sidx =StrUtils.isNotEmpty(param.get("sidx"))? param.get("sidx"):"";
	String orderSQL = StrUtils.isNotEmpty(sidx)? " ORDER BY "+sidx+" "+sord:"";
	System.out.println("orderSQL>>"+orderSQL);
//	return JSONUtils.toJson(search(id));
	return JSONUtils.toJson(search(id , orderSQL));
}

/**
 * 主要查詢用
 * @param id
 * @param orderSQL
 * @return
 */
public List<EACH_BATCH_DEF> search(String id , String orderSQL){
	
	List<EACH_BATCH_DEF> list = null ;
	try {
		if(StrUtils.isNotEmpty(id)){
//			list = batch_def_Dao.find(" FROM tw.org.twntch.po.EACH_BATCH_DEF WHERE BATCH_PROC_SEQ = ? ORDER BY BATCH_PROC_SEQ ",Integer.valueOf(id));
			list = batch_def_Dao.find(" FROM tw.org.twntch.po.EACH_BATCH_DEF WHERE BATCH_PROC_SEQ = ? "+orderSQL,Integer.valueOf(id));
		}else{
//			20150611 edit by hugo 此API order by 會亂跳
//			list = batch_def_Dao.getAll();
//			list = batch_def_Dao.find(" FROM tw.org.twntch.po.EACH_BATCH_DEF  ORDER BY BATCH_PROC_SEQ ");
			list = batch_def_Dao.find(" FROM tw.org.twntch.po.EACH_BATCH_DEF  "+orderSQL);
		}
		
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	System.out.println("list>>"+list);
//	list = list.size() == 0 ? null : list;
	list = list == null ? new ArrayList<EACH_BATCH_DEF>() : list;
	return list;
}


/**
 * 一般查詢用
 * @param id
 * @return
 */
public List<EACH_BATCH_DEF> search(String id ){
	
	List<EACH_BATCH_DEF> list = null ;
	try {
		if(StrUtils.isNotEmpty(id)){
			list = batch_def_Dao.find(" FROM tw.org.twntch.po.EACH_BATCH_DEF WHERE BATCH_PROC_SEQ = ? ORDER BY BATCH_PROC_SEQ ",Integer.valueOf(id));
		}else{
//			20150611 edit by hugo 此API order by 會亂跳
//			list = batch_def_Dao.getAll();
			list = batch_def_Dao.find(" FROM tw.org.twntch.po.EACH_BATCH_DEF  ORDER BY BATCH_PROC_SEQ ");
		}
		
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	System.out.println("list>>"+list);
//	list = list.size() == 0 ? null : list;
	list = list == null ? new ArrayList<EACH_BATCH_DEF>() : list;
	return list;
}


public EACH_BATCH_DEF_Dao getBatch_def_Dao() {
	return batch_def_Dao;
}

public void setBatch_def_Dao(EACH_BATCH_DEF_Dao batch_def_Dao) {
	this.batch_def_Dao = batch_def_Dao;
}







}
