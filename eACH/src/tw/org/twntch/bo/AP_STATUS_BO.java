package tw.org.twntch.bo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import tw.org.twntch.bean.BANK_STATUS;
import tw.org.twntch.db.dao.hibernate.AP_STATUS_Dao;
import tw.org.twntch.db.dao.hibernate.BANKAPSTATUSTAB_Dao;
import tw.org.twntch.db.dao.hibernate.BANKSYSSTATUSTAB_Dao;
import tw.org.twntch.po.BANKAPSTATUSTAB;
import tw.org.twntch.po.BANKAPSTATUSTAB_PK;
import tw.org.twntch.po.BANKSYSSTATUSTAB;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.StrUtils;

public class AP_STATUS_BO {
	private AP_STATUS_Dao ap_status_Dao;
	private BANKAPSTATUSTAB_Dao bankapstatus_Dao;
	private BANKSYSSTATUSTAB_Dao banksysstatus_Dao;
	private EACH_USERLOG_BO userlog_bo;//寫操作軌跡記錄
	
	public String getData(Map<String, String> params){
		//操作軌跡記錄用
		String SERCHSTRS = "";
		String paramName = "serchStrs";
		String paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue) ){
			SERCHSTRS = paramValue;
		}
		String sord =StrUtils.isNotEmpty(params.get("sord"))? params.get("sord"):"";
		String sidx =StrUtils.isNotEmpty(params.get("sidx"))? params.get("sidx"):"";
		String orderSQL = StrUtils.isNotEmpty(sidx)? " ORDER BY AP."+sidx+" "+sord:"";
		//將頁面上的查詢條件放進pkMap
		Map<String,Object> pkMap = new HashMap<String,Object>();
		pkMap.put("serchStrs",SERCHSTRS);
		//如果有錯誤要將訊息放進去
		Map<String,Object> msgMap = new HashMap<String,Object>();
		
		String result = "";
		List list = null;
		try{
			list = ap_status_Dao.getData(orderSQL);
		}catch(Exception e){
			e.printStackTrace();
		}
		list = list == null? null : list.size() == 0? null : list;
		//查詢成功
		if(list != null){
			result = JSONUtils.toJson(list);
			//寫操作軌跡記錄(成功)
			userlog_bo.writeLog("C",null,null,pkMap);
		}
		//失敗
		else{
			//寫操作軌跡記錄(失敗)
			msgMap.put("msg","查詢失敗");
			userlog_bo.writeFailLog("C",msgMap,null,null,pkMap);
		}
		return result;
	}
	
	public List<BANK_STATUS> getData(String bgbkId, String apId){
		List<BANK_STATUS> list = null;
		try{
			list = ap_status_Dao.getData(bgbkId, apId);
		}catch(Exception e){
			e.printStackTrace();
		}
		list = list == null? null : list.size() == 0? null : list;
		return list;
	}
	
	public Map save(Map formMap){
		Map<String,Object> oldmap = new HashMap<String,Object>();
		Map<String,Object> newmap = new HashMap<String,Object>();
		//將頁面上變更後的值放進newMap
		newmap.put("MBSYSSTATUS",formMap.get("MBSYSSTATUS"));
		newmap.put("MBAPSTATUS",formMap.get("MBAPSTATUS"));
		//將PK的值放進pkMap
		Map<String,Object> pkMap = new HashMap<String,Object>();
		pkMap.put("BGBK_ID",formMap.get("BGBK_ID"));
		pkMap.put("APID",formMap.get("APID"));
		//如果有錯誤要將訊息放進去
		Map<String,Object> msgMap = new HashMap<String,Object>();
		
		Map rtnMap = null;
		try{
			rtnMap = new HashMap();
			BANK_STATUS po = new BANK_STATUS();
			BeanUtils.populate(po, formMap);
			BANKSYSSTATUSTAB sys = banksysstatus_Dao.get(po.getBGBK_ID());
			if(sys == null){
				rtnMap.put("result", "FASLE");
				rtnMap.put("msg", "更新失敗，查無系統狀態!");
				
				//寫操作軌跡記錄(失敗)
				msgMap.put("msg",rtnMap.get("msg"));
				userlog_bo.writeFailLog("B",msgMap,null,null,pkMap);
				
				return rtnMap;
			}
			BANKAPSTATUSTAB_PK apPk = new BANKAPSTATUSTAB_PK(po.getBGBK_ID(), po.getAPID());
			System.out.println(apPk.getBGBK_ID() + "/" + apPk.getAPID());
			BANKAPSTATUSTAB ap = bankapstatus_Dao.get(apPk);
			if(ap == null){
				rtnMap.put("result", "FASLE");
				rtnMap.put("msg", "更新失敗，查無應用系統狀態!");

				//寫操作軌跡記錄(失敗)
				msgMap.put("msg",rtnMap.get("msg"));
				userlog_bo.writeFailLog("B",msgMap,null,null,pkMap);
				
				return rtnMap;
			}
			//將變更前的值放進oldMap
			oldmap.put("MBSYSSTATUS",sys.getMBSYSSTATUS());
			oldmap.put("MBAPSTATUS",ap.getMBAPSTATUS());
			
			sys.setMBSYSSTATUS(po.getMBSYSSTATUS());
			ap.setMBAPSTATUS(po.getMBAPSTATUS());
			
			if(ap_status_Dao.saveData(ap, sys)){
				rtnMap.put("result", "TRUE");
				rtnMap.put("msg", "更新成功!");
				
				//寫操作軌跡記錄(成功)
				userlog_bo.writeLog("B",oldmap,newmap,pkMap);
			}else{
				rtnMap.put("result", "FASLE");
				rtnMap.put("msg", "更新失敗!");
				
				//寫操作軌跡記錄(失敗)
				msgMap.put("msg",rtnMap.get("msg"));
				userlog_bo.writeFailLog("B",msgMap,null,null,pkMap);
			}
		}catch(Exception e){
			e.printStackTrace();
			rtnMap.put("result", "FASLE");
			rtnMap.put("msg", "更新錯誤!");

			//寫操作軌跡記錄(失敗)
			msgMap.put("msg",rtnMap.get("msg"));
			userlog_bo.writeFailLog("B",msgMap,null,null,pkMap);
			
		}
		return rtnMap;
	}

	public AP_STATUS_Dao getAp_status_Dao() {
		return ap_status_Dao;
	}

	public void setAp_status_Dao(AP_STATUS_Dao ap_status_Dao) {
		this.ap_status_Dao = ap_status_Dao;
	}

	public BANKAPSTATUSTAB_Dao getBankapstatus_Dao() {
		return bankapstatus_Dao;
	}

	public void setBankapstatus_Dao(BANKAPSTATUSTAB_Dao bankapstatus_Dao) {
		this.bankapstatus_Dao = bankapstatus_Dao;
	}

	public BANKSYSSTATUSTAB_Dao getBanksysstatus_Dao() {
		return banksysstatus_Dao;
	}

	public void setBanksysstatus_Dao(BANKSYSSTATUSTAB_Dao banksysstatus_Dao) {
		this.banksysstatus_Dao = banksysstatus_Dao;
	}

	public EACH_USERLOG_BO getUserlog_bo() {
		return userlog_bo;
	}

	public void setUserlog_bo(EACH_USERLOG_BO userlog_bo) {
		this.userlog_bo = userlog_bo;
	}
}
