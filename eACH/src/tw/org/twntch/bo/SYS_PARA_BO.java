package tw.org.twntch.bo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import tw.org.twntch.db.dao.hibernate.SYS_PARA_Dao;
import tw.org.twntch.po.SYS_PARA;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.zDateHandler;

public class SYS_PARA_BO {

	private SYS_PARA_Dao sys_para_Dao ;
	private EACH_USERLOG_BO userlog_bo ;

/**
 * 系統參數的update變更為無資料就新增 有資料就修改
 * @param id
 * @param formMap
 * @return
 */
	public Map<String,String> update(String id  ,Map formMap){
		System.out.println("update>>");
		Map<String, String> map = null;
		Map<String, String> pkmap = new HashMap<String, String>();//e
		Map<String, String> oldmap = new HashMap<String, String>();//e
		Map<String, String> newmap = new HashMap<String, String>();//e
		SYS_PARA po = null ;
//		id=StrUtils.isEmpty(id)?"0":id;
		try {
			map = new HashMap<String, String>();
			pkmap = BeanUtils.describe(id);
			if(StrUtils.isNotEmpty(id)){
				po = sys_para_Dao.get(Integer.valueOf(id));
			}
			if(po == null ){
				po = new SYS_PARA();
				formMap.remove("SEQ_ID");
			}
//			此2個屬性不可再變更時異動 ，只可再批次排程作業異動
			formMap.remove("RP_CLEARPHASE1_TIME");
			formMap.remove("RP_CLEARPHASE2_TIME");
			oldmap = BeanUtils.describe(po);
			BeanUtils.populate(po, formMap);
			po.setUDATE(zDateHandler.getTheDateII());
//			sys_para_Dao.save(po);
			sys_para_Dao.aop_save(po);
			map.put("result", "TRUE");
			map.put("msg", "儲存成功");
			map.put("target", "edit_p");
			newmap = BeanUtils.describe(po);
			userlog_bo.writeLog("B", oldmap, newmap, pkmap);
		}catch (Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();
			map.put("result", "ERROR");
			map.put("msg", "更新失敗，系統異常:"+e);
			map.put("target", "edit_p");
//			sys_para_Dao.updateFail(po, oldmap, pkmap, "儲存失敗，系統異常", 2);
			return map;
		}
		return map;
	}

public Map<String,String> delete(String id ){
	Map<String, String> map = null;
	try {
		map = new HashMap<String, String>();
		SYS_PARA po = sys_para_Dao.get(Integer.valueOf(id));
		if(po == null ){
			map.put("result", "FALSE");
			map.put("msg", "刪除失敗，查無資料");
			map.put("target", "edit_p");
			return map;
		}
		sys_para_Dao.remove(po);
		map.put("result", "TRUE");
		map.put("msg", "刪除成功");
		map.put("target", "edit_p");
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		map.put("result", "ERROR");
		map.put("msg", "刪除失敗，系統異常:"+e);
		map.put("target", "edit_p");
		return map;
	}
	return map;
}
	
	
	public Map<String,String> save(Map formMap){
		Map<String, String> map = null;
		SYS_PARA po = null;
		try {
			map = new HashMap<String, String>();
			List list  = sys_para_Dao.getTopOne();
			if(list.size() != 0){
				map.put("result", "FALSE");
				map.put("msg", "儲存失敗，已有資料");
				map.put("target", "add_p");
				return map;
			}
			po = new SYS_PARA();
			System.out.println("idbefoer>>"+po.getSEQ_ID());
			System.out.println("idbefoer2>>"+formMap.get("SEQ_ID"));
			BeanUtils.populate(po, formMap);
			System.out.println("id>>"+po.getSEQ_ID());
			po.setSEQ_ID(null);//因為BeanUtils會自動塞0
			po.setCDATE(zDateHandler.getTheDateII());
			sys_para_Dao.save(po);
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
		System.out.println("save.map"+map);
		return map;
	}
	
	
	public String search_toJson(Map<String, String> param){
		String seq_id =StrUtils.isNotEmpty(param.get("SEQ_ID"))? param.get("SEQ_ID"):"";
		return JSONUtils.toJson(search(seq_id));
	}
	
	public List<SYS_PARA> search(String id){
		
		List<SYS_PARA> list = null ;
		try {
			if(StrUtils.isNotEmpty(id)){
				list = sys_para_Dao.find(" FROM tw.org.twntch.po.SYS_PARA WHERE SEQ_ID = ?",Integer.valueOf(id));
			}else{
				list = sys_para_Dao.getAll();
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("list>>"+list);
		list = list.size() == 0 ? null : list;
		return list;
	}
	public SYS_PARA searchII(){
		List<SYS_PARA> list = null ;
		try {
			list = sys_para_Dao.getAll();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list.size() == 0 ? null : list.get(0);
	}
	
	public String checkAptTime(Map<String, String> params){
		Map rtnMap = new HashMap();
		rtnMap.put("result", "FALSE");
		rtnMap.put("msg", "無法取得上傳時間設定");
		
		SYS_PARA po = sys_para_Dao.getAPT_PEND_TIME();
		if(po != null){
			String nowDateTime = zDateHandler.getTheDateII() + " " + zDateHandler.getTheTime();
			try {
				//取得上傳時間起訖
				rtnMap.putAll(BeanUtils.describe(po));
				String startTime, endTime, acceptTimeStr = "";
				boolean isInTimeRange = false;
				for(int i = 1; i <= 5; i++){
					startTime = (String) rtnMap.get("APT_PEND_START_TIME" + i);
					endTime = (String) rtnMap.get("APT_PEND_END_TIME" + i);
					if(StrUtils.isEmpty(startTime) || StrUtils.isEmpty(endTime)){
						continue;
					}
					
					acceptTimeStr += startTime + " ~ " + endTime + "\n";
					
					if(zDateHandler.isInDateTimeRange(nowDateTime, startTime, endTime)){
						isInTimeRange = true;
					}
				}
				
				if(isInTimeRange){
					rtnMap.put("msg", "目前為約定非上傳時間，以下時間不接受上傳檔案\n" + acceptTimeStr);
				}else{
					rtnMap.put("result", "TRUE");
					rtnMap.put("msg", "目前時間可接受上傳");
				}
			} catch (Exception e){
				e.printStackTrace();
				rtnMap.put("msg", "取得上傳時間設定時發生錯誤");
			}
		}
		return JSONUtils.map2json(rtnMap);
	}
	
	public SYS_PARA_Dao getSys_para_Dao() {
		return sys_para_Dao;
	}

	public void setSys_para_Dao(SYS_PARA_Dao sys_para_Dao) {
		this.sys_para_Dao = sys_para_Dao;
	}

	public EACH_USERLOG_BO getUserlog_bo() {
		return userlog_bo;
	}

	public void setUserlog_bo(EACH_USERLOG_BO userlog_bo) {
		this.userlog_bo = userlog_bo;
	}
	
	
	
}
