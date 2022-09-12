package tw.org.twntch.bo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.struts.util.LabelValueBean;

import tw.org.twntch.aop.GenerieAop;
import tw.org.twntch.db.dao.hibernate.EACH_USERLOG_Dao;
import tw.org.twntch.db.dao.hibernate.TXN_RETURNDAY_Dao;
import tw.org.twntch.po.EACH_USERLOG;
import tw.org.twntch.po.RETURN_DAY;
import tw.org.twntch.po.RETURN_DAY_PK;
import tw.org.twntch.util.BeanUtils;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.SpringAppCtxHelper;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.WebServletUtils;
import tw.org.twntch.util.zDateHandler;

public class TXN_RETURNDAY_BO extends GenerieAop{
	
	private TXN_RETURNDAY_Dao txn_returnday_Dao;
	private EACH_USERLOG_BO userlog_bo;
	private EACH_USERLOG_Dao userLog_Dao ;
	
	/**
	 * 取得退回天數檔中，已存在的交易代號清單
	 * @return
	 */
	public List<LabelValueBean> getTxnIdList(){
		List<Map<String, String>> list = txn_returnday_Dao.getTxnIdList();
		List<LabelValueBean> beanList = new LinkedList<LabelValueBean>();
		LabelValueBean bean = null;
		if(list != null){
			for(Map<String, String> m : list){
				bean = new LabelValueBean(m.get("TXN_ID") + " - " + m.get("TXN_NAME"), m.get("TXN_ID"));
				beanList.add(bean);
			}
		}
		System.out.println("beanList>>"+beanList);
		return beanList;
	}
	
	public List<RETURN_DAY> search(String txnId  ,String orderSQL){
		List<RETURN_DAY> list = null;
		try {
			if(StrUtils.isNotEmpty(txnId)){
				list = txn_returnday_Dao.getByTxnId(txnId , orderSQL);
			}else{
				list = txn_returnday_Dao.getAllData(orderSQL);
			}
			System.out.println("list>>"+list);
			list = list == null? new ArrayList<RETURN_DAY>() : list.size() == 0 ? new ArrayList<RETURN_DAY>() : list;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	public List<RETURN_DAY> search(String txnId, String activeDate ,String serchStrs){
		List<RETURN_DAY> list = null;
		try {
			if(StrUtils.isEmpty(txnId) && StrUtils.isEmpty(activeDate)){
				list = txn_returnday_Dao.getAllData();
			}else{
				//list = txn_returnday_Dao.getByTxnId(txnId);
				String arg[] = {txnId, activeDate};
				list = txn_returnday_Dao.find("FROM tw.org.twntch.po.RETURN_DAY WHERE TXN_ID = ? AND ACTIVE_DATE = ? ORDER BY TXN_ID, ACTIVE_DATE", arg);
			}
			System.out.println("list>>"+list);
			list = list == null? null : list.size() == 0 ? null : list;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return list;
	}
	
	public String search_toJson(Map<String, String> param){
		String txn_id =StrUtils.isNotEmpty(param.get("TXN_ID"))? param.get("TXN_ID"):"";
		txn_id = txn_id.equals("all")?"":txn_id;
		String sord =StrUtils.isNotEmpty(param.get("sord"))? param.get("sord"):"";
		String sidx =StrUtils.isNotEmpty(param.get("sidx"))? param.get("sidx"):"";
		String orderSQL = StrUtils.isNotEmpty(sidx)? " ORDER BY "+sidx+" "+sord:"";
		return JSONUtils.toJson(search(txn_id , orderSQL));
	}
	
	
	public Map save(RETURN_DAY po){
		Map msg = new HashMap();
		Map pkmap = new HashMap();
		EACH_USERLOG userlog_po = null;
		try{
			RETURN_DAY tmp = txn_returnday_Dao.get(po.getId());
			pkmap = BeanUtils.describe(po.getId());
			if(tmp != null){
//				msg.put("result", false);
//				msg.put("msg", "重複的資料!");
				msg = txn_returnday_Dao.saveFail(po, pkmap, "儲存失敗，資料重覆" , 1);
			}else{
				po.setCDATE(zDateHandler.getTheDateII());
				txn_returnday_Dao.aop_save(po);
//				txn_returnday_Dao.save(po, pkmap);
				msg.put("result", true);
				msg.put("msg", po.getId().getTXN_ID());
				
				String result = "{啟用日期="+po.getId().getACTIVE_DATE()+",交易代號="+po.getId().getTXN_ID()+",退回天數="+po.getRETURN_DAY()+",建立日期="+po.getCDATE()+"}";
				userlog_po = getUSERLOG("A","");
		    	userlog_po.setADEXCODE("新增成功，PK={啟用日期="+po.getId().getACTIVE_DATE()+",交易代號="+po.getId().getTXN_ID()+"}");
		    	userlog_po.setAFCHCON(result);
		    	userLog_Dao = SpringAppCtxHelper.getBean("userLog_Dao");
		    	
		    	userLog_Dao.aop_save(userlog_po);
			}
		}catch(Exception e){
			e.printStackTrace();
//			msg.put("result", false);
//			msg.put("msg", "儲存失敗：" + e.toString());
			msg = txn_returnday_Dao.saveFail(po, pkmap, "儲存失敗，系統異常" , 2);
		}
		return msg;
	}
	
	public Map update(String txnId, String activeDate, Map formMap){
		Map msg = new HashMap();
		Map pkmap = new HashMap();
		Map oldmap = new HashMap();
		try{
			RETURN_DAY_PK pk = new RETURN_DAY_PK(txnId, activeDate);
			RETURN_DAY tmp = txn_returnday_Dao.get(pk);
			pkmap = BeanUtils.describe(pk);
			//如果有資料則UPDATE，否則新增
			if(tmp == null){
				tmp = new RETURN_DAY();
				tmp.setId(pk);
				tmp.setRETURN_DAY(Integer.valueOf((String)formMap.get("RETURN_DAY")));
				tmp.setCDATE(zDateHandler.getTheDateII());
//				msg = txn_returnday_Dao.updateFail(null, null, pkmap, "儲存失敗，查無資料", 1);
//				20150615 note by hugo 特例 無資料時改新增一筆
				txn_returnday_Dao.save(tmp, pkmap);
				msg.put("result", true);
				msg.put("msg", "儲存成功");
				msg.put("target", "search");
				return msg;
			}else{
				oldmap = BeanUtils.describe(tmp);
				oldmap.putAll(BeanUtils.describe(tmp.getId()));
				tmp.setRETURN_DAY(Integer.valueOf((String)formMap.get("RETURN_DAY")));
				tmp.setUDATE(zDateHandler.getTheDateII());
			}
//			txn_returnday_Dao.save(tmp);
			txn_returnday_Dao.saveII(tmp, oldmap, pkmap);
			msg.put("result", true);
			msg.put("msg", "儲存成功");
			msg.put("target", "search");
		}catch(Exception e){
			e.printStackTrace();
//			msg.put("result", false);
//			msg.put("msg", "儲存失敗：" + e.toString());
//			msg.put("target", "edit_p");
			msg = txn_returnday_Dao.updateFail(null, null, pkmap, "儲存失敗，系統異常", 2);
		}
		return msg;
	}
	
	public Map delete(String txnId, String activeDate){
		Map msg = new HashMap();
		Map pkmap = new HashMap();
		EACH_USERLOG userlog_po = null;
		try{
			RETURN_DAY_PK pk = new RETURN_DAY_PK(txnId, activeDate);
			RETURN_DAY po = txn_returnday_Dao.get(pk);
			pkmap = BeanUtils.describe(pk);
			if(po == null){
//				msg.put("result", false);
//				msg.put("msg", "無此資料!");
//				msg.put("target", "edit_p");
				msg = txn_returnday_Dao.removeFail(null, pkmap, "刪除失敗，查無資料", 1);
			}else{
				txn_returnday_Dao.remove(po);
//				txn_returnday_Dao.remove(po, pkmap);
				msg.put("result", true);
				msg.put("msg", "刪除成功");
				msg.put("target", "search");
				
				userlog_po = getUSERLOG("D","");
				userlog_po.setADEXCODE("刪除成功，PK={啟用日期="+activeDate+",交易代號="+txnId+"}");
				
				String deleteText = "{啟用日期="+activeDate+",交易代號="+txnId+",退回天數="+po.getRETURN_DAY()+",建立日期="+po.getCDATE()+",異動日期="+po.getUDATE()+"}";
				userlog_po.setBFCHCON(deleteText);
				
				userLog_Dao = SpringAppCtxHelper.getBean("userLog_Dao");
				userLog_Dao.aop_save(userlog_po);
			}
		}catch(Exception e){
			e.printStackTrace();
//			msg.put("result", false);
//			msg.put("msg", "儲存失敗：" + e.toString());
//			msg.put("target", "edit_p");
			msg = txn_returnday_Dao.removeFail(null, pkmap, "刪除失敗，系統異常", 2);
		}
		return msg;
	}
	
	public List getHistory(){
		List list = null;
		list = txn_returnday_Dao.getHistory();
		if(list == null){
			list = new ArrayList();
		}
		return list;
	}
	
	public TXN_RETURNDAY_Dao getTxn_returnday_Dao() {
		return txn_returnday_Dao;
	}

	public void setTxn_returnday_Dao(TXN_RETURNDAY_Dao txn_returnday_Dao) {
		this.txn_returnday_Dao = txn_returnday_Dao;
	}

	public EACH_USERLOG_BO getUserlog_bo() {
		return userlog_bo;
	}

	public void setUserlog_bo(EACH_USERLOG_BO userlog_bo) {
		this.userlog_bo = userlog_bo;
	}

	
}
