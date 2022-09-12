package tw.org.twntch.bo;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import tw.org.twntch.db.dao.hibernate.BULLETIN_Dao;
import tw.org.twntch.db.dao.hibernate.BULLETIN_REC_Dao;
import tw.org.twntch.po.BULLETIN;
import tw.org.twntch.po.BULLETIN_REC;
import tw.org.twntch.po.EACH_USERLOG;
import tw.org.twntch.util.BeanUtils;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.WebServletUtils;
import tw.org.twntch.util.zDateHandler;

public class BULLETIN_BO {
	private Logger log = Logger.getLogger(BULLETIN_BO.class.getName());
	private BULLETIN_Dao bulletin_Dao ;
	private BULLETIN_REC_Dao bulletin_rec_Dao ;
	private EACH_USERLOG_BO  userlog_bo ;
	
	
	
	public Map<String,String> save(BULLETIN bulletin){
		Map<String, String> retmap = new HashMap<String, String>();
		Map<String, String> pkmap = new HashMap<String, String>();
		try {
			pkmap.put("SNO", String.valueOf(bulletin.getSNO()));
			if(bulletin != null){
				bulletin.setSAVE_DATE(zDateHandler.getTimestamp());
			}
			if(!bulletin.getSEND_STATUS().equals("Y")){
				bulletin.setSEND_STATUS("N");
			}
			bulletin_Dao.save(bulletin);
			retmap.put("result", "TRUE");
			retmap.put("msg", "儲存成功");
			retmap.put("target", "search");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.debug("save.Exception:"+e);
			bulletin_Dao.saveFail(bulletin, pkmap, "儲存失敗，系統異常", 2);
		}
		return retmap;
	}
	
	
	/**
	 * 發佈及儲存
	 * @param bulletin
	 * @return
	 */
	public Map<String,String> saveNsend(BULLETIN bulletin){
		Map<String, String> retmap = new HashMap<String, String>();
		Map<String, String> pkmap = new HashMap<String, String>();
		BULLETIN_REC bulletin_rec = new BULLETIN_REC();
		boolean result = false;
		try {
			pkmap.put("SNO", String.valueOf(bulletin.getSNO()));
			bulletin.setSAVE_DATE(zDateHandler.getTimestamp());
			bulletin.setSEND_DATE(zDateHandler.getTimestamp());
//			還要另外寫入公告紀錄檔 即將其他非此資料的的SEND_STATUS 改為N
			bulletin_rec.setCHCON(bulletin.getCHCON());
			bulletin_rec.setUSERID((String) WebServletUtils.getRequest().getSession().getAttribute("S.USER.ID"));
			bulletin_rec.setUSERIP((String) WebServletUtils.getRequest().getSession().getAttribute("USERIP"));
			bulletin_rec.setSEND_DATE(bulletin.getSEND_DATE());
			result = bulletin_Dao.saveData(bulletin, bulletin_rec);
			retmap.put("result", "TRUE");
			retmap.put("msg", "儲存及發佈成功");
			retmap.put("target", "search");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.debug("save.Exception:"+e);
			bulletin_Dao.saveFail(bulletin, pkmap, "儲存及發佈失敗，系統異常", 2);
		}finally{
			wirte_SaveNsend_Log(bulletin, bulletin_rec, pkmap , null);
		}
		return retmap;
	}
	/**
	 * 由批次的btn發出的公告
	 * @param bulletin
	 * @param action
	 * @return
	 */
	public Map<String,String> saveNsend_BAT(BULLETIN bulletin , String action){
		Map<String, String> retmap = new HashMap<String, String>();
		Map<String, String> pkmap = new HashMap<String, String>();
		BULLETIN_REC bulletin_rec = new BULLETIN_REC();
		boolean result = false;
		try {
			pkmap.put("SNO", String.valueOf(bulletin.getSNO()));
			bulletin.setSAVE_DATE(zDateHandler.getTimestamp());
			bulletin.setSEND_DATE(zDateHandler.getTimestamp());
//			還要另外寫入公告紀錄檔 即將其他非此資料的的SEND_STATUS 改為N
			bulletin_rec.setCHCON(bulletin.getCHCON());
			bulletin_rec.setUSERID((String) WebServletUtils.getRequest().getSession().getAttribute("S.USER.ID"));
			bulletin_rec.setUSERIP((String) WebServletUtils.getRequest().getSession().getAttribute("USERIP"));
			bulletin_rec.setSEND_DATE(bulletin.getSEND_DATE());
			result = bulletin_Dao.saveData(bulletin, bulletin_rec);
			retmap.put("result", "TRUE");
			retmap.put("msg", "儲存及發佈成功");
			retmap.put("target", "search");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.debug("save.Exception:"+e);
			bulletin_Dao.saveFail(bulletin, pkmap, "儲存及發佈失敗，系統異常", 2);
		}finally{
			wirte_SaveNsend_Log(bulletin, bulletin_rec, pkmap , action);
		}
		return retmap;
	}
	
	public void wirte_SaveNsend_Log(BULLETIN bulletin ,BULLETIN_REC bulletin_rec , Map<String,String> pkmap ,String action){
		Map<String,String> newmap = new HashMap<String,String>();
		StringBuffer str = new StringBuffer();
		EACH_USERLOG userlog  = null;
		try {
//			userlog  = userlog_bo.getUSERLOG("0", null);
			userlog  = userlog_bo.getUSERLOG("0", action);
			if(userlog == null){
				log.debug("userlog is null dosent write log");
				return;
			}
			if(bulletin != null){
					newmap = BeanUtils.describe(bulletin);
					newmap = userlog_bo.restMapKey2CH(newmap);
					str.append("新增公告檔:"+newmap);
			}
			if(bulletin_rec != null){
				newmap.clear();
				newmap = BeanUtils.describe(bulletin_rec);
				newmap = userlog_bo.restMapKey2CH(newmap);
				str.append("\n\n");
				str.append("新增公告紀錄檔:"+newmap);
			}
			userlog.setADEXCODE("儲存及發佈公告成功");
			userlog.setAFCHCON(str.toString());
			userlog_bo.getUserLog_Dao().aop_save(userlog);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void wirte_UpdateNsend_Log(BULLETIN bulletin , BULLETIN obulletin  ,BULLETIN_REC bulletin_rec , Map<String,String> pkmap , String op_type){
		Map<String,String> newmap = new HashMap<String,String>();
		Map<String,String> oldmap = new HashMap<String,String>();
		StringBuffer str = new StringBuffer();
		EACH_USERLOG userlog  = null;
		try {
			userlog  = userlog_bo.getUSERLOG(op_type, null);
			if(userlog == null){
				log.debug("userlog is null dosent write log");
				return;
			}
			
			
			if(bulletin != null && obulletin !=null){
				newmap = BeanUtils.describe(bulletin);
				oldmap = BeanUtils.describe(obulletin);
				newmap = userlog_bo.restMapKey2CH(newmap);
				oldmap = userlog_bo.restMapKey2CH(oldmap);
				Map<String,Map> tmp = userlog_bo.com_Dif_Val(newmap, oldmap);
				newmap = tmp.get("newmap");
				oldmap = tmp.get("oldmap");
				userlog.setBFCHCON("公告檔:"+oldmap);
				str.append("公告檔:"+newmap);
			}
			if(bulletin_rec != null){
				newmap.clear();
				newmap = BeanUtils.describe(bulletin_rec);
				newmap = userlog_bo.restMapKey2CH(newmap);
				str.append("\n\n");
				str.append("新增公告紀錄檔:"+newmap);
			}
			userlog.setADEXCODE("成功");
			userlog.setAFCHCON(str.toString());
			userlog_bo.getUserLog_Dao().aop_save(userlog);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void wirte_UpdateNsend_Log(BULLETIN bulletin , BULLETIN obulletin  ,BULLETIN_REC bulletin_rec , Map<String,String> pkmap , String op_type , String uri  ){
		Map<String,String> newmap = new HashMap<String,String>();
		Map<String,String> oldmap = new HashMap<String,String>();
		StringBuffer str = new StringBuffer();
		EACH_USERLOG userlog  = null;
		
		try {
			userlog  = userlog_bo.getUSERLOG(op_type, uri);
			if(userlog == null){
				log.debug("userlog is null dosent write log");
				return;
			}
			
			if(bulletin != null && obulletin !=null){
				newmap = BeanUtils.describe(bulletin);
				oldmap = BeanUtils.describe(obulletin);
				newmap = userlog_bo.restMapKey2CH(newmap);
				oldmap = userlog_bo.restMapKey2CH(oldmap);
				Map<String,Map> tmp = userlog_bo.com_Dif_Val(newmap, oldmap);
				newmap = tmp.get("newmap");
				oldmap = tmp.get("oldmap");
				userlog.setBFCHCON("公告檔:"+oldmap);
				str.append("公告檔:"+newmap);
			}
			if(bulletin_rec != null){
				newmap.clear();
				newmap = BeanUtils.describe(bulletin_rec);
				newmap = userlog_bo.restMapKey2CH(newmap);
				str.append("\n\n");
				str.append("新增公告紀錄檔:"+newmap);
			}
			userlog.setADEXCODE("成功");
			userlog.setAFCHCON(str.toString());
			userlog_bo.getUserLog_Dao().aop_save(userlog);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	public Map<String,String> update(BULLETIN bulletin){
		Map<String, String> retmap = new HashMap<String, String>();
		Map<String, String> oldmap = new HashMap<String, String>();
		Map<String, String> pkmap = new HashMap<String, String>();
		BULLETIN oBulletin = null ; 
		try {
			oBulletin = bulletin_Dao.get(bulletin.getSNO());
			pkmap.put("SNO", String.valueOf(bulletin.getSNO()));
			if(oBulletin == null ){
				retmap = bulletin_Dao.updateFail(bulletin, null, pkmap, "儲存失敗，查無資料", 1);
				return retmap;
			}
			oldmap = BeanUtils.describe(oBulletin);
			bulletin.setSAVE_DATE(zDateHandler.getTimestamp());
			bulletin.setSEND_DATE(oBulletin.getSEND_DATE());
			bulletin_Dao.saveII(bulletin ,oldmap , pkmap);
			retmap.put("result", "TRUE");
			retmap.put("msg", "儲存成功");
			retmap.put("target", "search");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.debug("save.Exception:"+e);
			retmap = bulletin_Dao.updateFail(bulletin, oldmap, pkmap, "儲存失敗，系統異常", 2);
		}
		return retmap;
	}
	
	
	/**
	 * 編輯頁面中的儲存及發佈公告
	 * @param bulletin
	 * @return
	 */
	public Map<String,String> updateNsend(BULLETIN bulletin){
		Map<String, String> retmap = new HashMap<String, String>();
		Map<String, String> oldmap = new HashMap<String, String>();
		Map<String, String> pkmap = new HashMap<String, String>();
		BULLETIN oBulletin = null ; 
		BULLETIN_REC bulletin_rec = new BULLETIN_REC() ; 
		try {
			
			oBulletin = bulletin_Dao.get(bulletin.getSNO());
			pkmap.put("SNO", String.valueOf(bulletin.getSNO()));
			if(oBulletin == null ){
				retmap = bulletin_Dao.updateFail(bulletin, null, pkmap, "儲存失敗，查無資料", 1);
				return retmap;
			}
			oldmap = BeanUtils.describe(oBulletin);
			bulletin.setSAVE_DATE(zDateHandler.getTimestamp());
			bulletin.setSEND_DATE(zDateHandler.getTimestamp());
//				還要另外寫入公告紀錄檔 即將其他非此資料的的SEND_STATUS 改為N
			bulletin_rec.setCHCON(bulletin.getCHCON());
			bulletin_rec.setUSERID((String) WebServletUtils.getRequest().getSession().getAttribute("S.USER.ID"));
			bulletin_rec.setUSERIP((String) WebServletUtils.getRequest().getSession().getAttribute("USERIP"));
			bulletin_rec.setSEND_DATE(bulletin.getSEND_DATE());
			
			bulletin_Dao.updateData(bulletin, bulletin_rec);
			retmap.put("result", "TRUE");
			retmap.put("msg", "儲存成功");
			retmap.put("target", "search");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.debug("save.Exception:"+e);
			retmap = bulletin_Dao.updateFail(bulletin, oldmap, pkmap, "儲存失敗，系統異常", 2);
		}finally{
			wirte_UpdateNsend_Log(bulletin, oBulletin, bulletin_rec, pkmap , "0");
		}
		return retmap;
	}
	
	public Map<String,String> delete(Integer id){
		Map<String, String> retmap = new HashMap<String, String>();
		Map<String, String> pkmap = new HashMap<String, String>();
		BULLETIN oBulletin = null ; 
		oBulletin = bulletin_Dao.get(id);
		try {
			pkmap.put("SNO", String.valueOf(id));
			if(oBulletin == null){
				bulletin_Dao.removeFail(oBulletin, pkmap, "刪除失敗，查無資料", 1);
			}
			bulletin_Dao.removeII(oBulletin, pkmap);
			retmap.put("result", "TRUE");
			retmap.put("msg", "刪除成功");
			retmap.put("target", "search");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.debug("delete.Exception:"+e);
			bulletin_Dao.removeFail(oBulletin, pkmap, "刪除失敗，系統異常", 2);
		}
		
		return retmap;
	}
	
	
	public String search_toJson(Map<String, String> param){
		String json ="{}";
		String s_date ="";
		String e_date ="";
		Integer id = null;
		List<BULLETIN> list = null;
		try {
			id = StrUtils.isEmpty(param.get("SNO"))?null: Integer.valueOf(param.get("SNO")) ;
			s_date = StrUtils.isEmpty(param.get("SDATE"))? zDateHandler.getTimestamp() : DateTimeUtils.convertDate(1, param.get("SDATE") , "yyyyMMdd", "yyyy-MM-dd")+" "+"00:00:00" ;
			e_date = StrUtils.isEmpty(param.get("EDATE"))? zDateHandler.getTimestamp() : DateTimeUtils.convertDate(1, param.get("EDATE") , "yyyyMMdd", "yyyy-MM-dd")+" "+"23:59:59" ;
			String sord =StrUtils.isNotEmpty(param.get("sord"))? param.get("sord"):"";
			String sidx =StrUtils.isNotEmpty(param.get("sidx"))? param.get("sidx"):"";
			String orderSQL = StrUtils.isNotEmpty(sidx)? " ORDER BY "+sidx+" "+sord:"";
			System.out.println("orderSQL>>"+orderSQL);
			
			
			list = search(id , s_date , e_date , orderSQL);
			if(list != null){
				json = JSONUtils.toJson(list);  
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}
	
	public List<BULLETIN> search(Integer id , String s_date ,String e_date ,String orderSQL){
		List<BULLETIN> list = null;
		
		try {
			if(id != null ){
				list = bulletin_Dao.getDataById(id);
			}else{
//				list = bulletin_Dao.getAll();
				list = bulletin_Dao.getDataByDate(s_date, e_date ,orderSQL );
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return list;
	}
	/**
	 * 依據日期查詢公告紀錄(UI查詢用)
	 * @param s_date
	 * @param e_date
	 * @return
	 */
	public String search_Rec( Map<String, String> param){
		List<BULLETIN_REC> list = null;
		String json ="{}";
		String s_date ="";
		String e_date ="";
		try {
			s_date = StrUtils.isEmpty(param.get("SDATE"))? zDateHandler.getTimestamp() : DateTimeUtils.convertDate(1, param.get("SDATE") , "yyyyMMdd", "yyyy-MM-dd")+" "+"00:00:00" ;
			e_date = StrUtils.isEmpty(param.get("EDATE"))? zDateHandler.getTimestamp() : DateTimeUtils.convertDate(1, param.get("EDATE") , "yyyyMMdd", "yyyy-MM-dd")+" "+"23:59:59" ;
			String sord =StrUtils.isNotEmpty(param.get("sord"))? param.get("sord"):"";
			String sidx =StrUtils.isNotEmpty(param.get("sidx"))? param.get("sidx"):"";
			String orderSQL = StrUtils.isNotEmpty(sidx)? " ORDER BY "+sidx+" "+sord:"";
			System.out.println("orderSQL>>"+orderSQL);
			
			list = bulletin_rec_Dao.getDataByDate(s_date, e_date , orderSQL);
			if(list != null){
				json = JSONUtils.toJson(list);  
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
		
	}
	
	/**
	 * 
	 * @param param
	 * @return
	 */
	public String send_status(Map<String, String> param){
		Map<String, String> retmap = new HashMap<String, String>();
		Map<String, String> pkmap = new HashMap<String, String>();
		String json ="{}";
		String action ="";
		Integer id =null;
		BULLETIN_REC bulletin_rec = null;
		BULLETIN oBulletin = null; 
		BULLETIN bulletin = null;
		boolean result = false;
		try {
			id = StrUtils.isEmpty(param.get("SNO"))?null: Integer.valueOf(param.get("SNO")) ;
			action = StrUtils.isEmpty(param.get("action"))?"bulletin.do": param.get("action") ;
			bulletin =  new BULLETIN();
			oBulletin =  bulletin_Dao.get(id);
			if(bulletin == null ){
				retmap = bulletin_Dao.updateFail(bulletin, null, pkmap, "發佈失敗，查無資料", 1);
				json = JSONUtils.map2json(retmap);
				return json;
			}
			bulletin.setSNO(oBulletin.getSNO());
			bulletin.setSEND_DATE(zDateHandler.getTimestamp());
			bulletin.setSEND_STATUS("Y");
			bulletin.setCHCON(oBulletin.getCHCON());
			bulletin.setSAVE_DATE(oBulletin.getSAVE_DATE());
			
			bulletin_rec = new BULLETIN_REC();
			bulletin_rec.setCHCON(bulletin.getCHCON());
			bulletin_rec.setSEND_DATE(bulletin.getSEND_DATE());
			bulletin_rec.setUSERID((String) WebServletUtils.getRequest().getSession().getAttribute("S.USER.ID"));
			bulletin_rec.setUSERIP((String) WebServletUtils.getRequest().getSession().getAttribute("USERIP"));
			result = bulletin_Dao.updateData(bulletin, bulletin_rec);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.debug("send_status.Exception>>"+e);
			result = false;
		}finally{
			if(result){
				retmap.put("result", "TRUE");
				retmap.put("msg", "發佈成功");
				wirte_UpdateNsend_Log(bulletin, oBulletin, bulletin_rec, pkmap , "Y" , action);
			}else{
				retmap.put("result", "FALSE");
				retmap.put("msg", "發佈失敗");
			}
			json = JSONUtils.map2json(retmap);
		}
		
		return json;
	}
	
//	TODO 要另外寫LOG
	public String cancel_status(Map<String, String> param){
		Map<String, String> retmap = new HashMap<String, String>();
		Map<String, String> pkmap = new HashMap<String, String>();
		String json ="{}";
		String action ="{}";
		BULLETIN bulletin = null;
		BULLETIN oBulletin = null;
		boolean result = false;
		try {
			action = StrUtils.isEmpty(param.get("action"))?"bulletin.do": param.get("action") ;
			List<BULLETIN> list =  bulletin_Dao.find(" FROM tw.org.twntch.po.BULLETIN WHERE SEND_STATUS = 'Y' ");
			if(list !=null && list.size() !=0){
				for(BULLETIN po :list){
					oBulletin = new BULLETIN();
					bulletin = new BULLETIN();
					BeanUtils.copyProperties(oBulletin, po);
					po.setSEND_STATUS("N");
					BeanUtils.copyProperties(bulletin, po);
					bulletin_Dao.aop_save(po);
				}
			}
			result = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.debug("cancel_status.Exception>>"+e);
			result = false;
		}finally{
			if(result){
				retmap.put("result", "TRUE");
				retmap.put("msg", "取消公告已執行");
				wirte_UpdateNsend_Log(bulletin, oBulletin, null, pkmap, "Z" , action);
			}else{
				retmap.put("result", "FALSE");
				retmap.put("msg", "取消公告失敗");
			}
			json = JSONUtils.map2json(retmap);
		}
		
		return json;
	}
	
	
	/**
	 * 取得要發佈的公告訊息
	 * @param param
	 * @return
	 */
	public String getPublicData(Map<String, String> param){
		String json ="{}";
		try {
			List<BULLETIN> list =  bulletin_Dao.find(" FROM tw.org.twntch.po.BULLETIN WHERE SEND_STATUS = 'Y' ");
			if(list != null && list.size() !=0 ){
				json = JSONUtils.toJson(list);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.debug("getPublicData.Exception>>"+e);
		}
		return json;
	}
	
	
	public BULLETIN_Dao getBulletin_Dao() {
		return bulletin_Dao;
	}
	public void setBulletin_Dao(BULLETIN_Dao bulletin_Dao) {
		this.bulletin_Dao = bulletin_Dao;
	}
	public EACH_USERLOG_BO getUserlog_bo() {
		return userlog_bo;
	}
	public void setUserlog_bo(EACH_USERLOG_BO userlog_bo) {
		this.userlog_bo = userlog_bo;
	}
	public BULLETIN_REC_Dao getBulletin_rec_Dao() {
		return bulletin_rec_Dao;
	}
	public void setBulletin_rec_Dao(BULLETIN_REC_Dao bulletin_rec_Dao) {
		this.bulletin_rec_Dao = bulletin_rec_Dao;
	} 
	
	
	
}
