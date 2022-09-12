package tw.org.twntch.bo;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.struts.util.LabelValueBean;

import tw.org.twntch.db.dao.hibernate.BANKAPSTATUSTAB_Dao;
import tw.org.twntch.db.dao.hibernate.BANKSYSSTATUSTAB_Dao;
import tw.org.twntch.db.dao.hibernate.BANK_BRANCH_Dao;
import tw.org.twntch.db.dao.hibernate.BANK_GROUP_BUSINESS_Dao;
import tw.org.twntch.db.dao.hibernate.BANK_GROUP_Dao;
import tw.org.twntch.db.dao.hibernate.CR_LINE_Dao;
import tw.org.twntch.po.BANKAPSTATUSTAB;
import tw.org.twntch.po.BANKAPSTATUSTAB_PK;
import tw.org.twntch.po.BANKSYSSTATUSTAB;
import tw.org.twntch.po.BANK_BRANCH;
import tw.org.twntch.po.BANK_BRANCH_PK;
import tw.org.twntch.po.BANK_CTBK;
import tw.org.twntch.po.BANK_CTBK_PK;
import tw.org.twntch.po.BANK_GROUP;
import tw.org.twntch.po.BANK_GROUP_BUSINESS;
import tw.org.twntch.po.BANK_GROUP_BUSINESS_PK;
import tw.org.twntch.po.BANK_OPBK;
import tw.org.twntch.po.BANK_OPBK_PK;
import tw.org.twntch.po.BUSINESS_TYPE;
import tw.org.twntch.po.CR_LINE;
import tw.org.twntch.po.EACH_USERLOG;
import tw.org.twntch.po.WK_DATE_CALENDAR;
import tw.org.twntch.util.BeanUtils;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.SpringAppCtxHelper;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.zDateHandler;

public class BANK_GROUP_BO {

	private BANK_GROUP_Dao bank_group_Dao ;
	private BANK_GROUP_BUSINESS_Dao bank_group_business_Dao ;
	private BUSINESS_TYPE_BO business_type_bo ;
	private BANK_BRANCH_Dao bank_branch_Dao;
	private CR_LINE_Dao cr_line_Dao;
	private BANKSYSSTATUSTAB_Dao banksysstatus_Dao;
	private BANKAPSTATUSTAB_Dao bankapstatus_Dao ;
	private EACH_USERLOG_BO userlog_bo;
	private BANK_OPBK_BO bank_opbk_bo;
	private BANK_CTBK_BO bank_ctbk_bo;
	private EACHSYSSTATUSTAB_BO eachsysstatustab_bo;
	
	public BANK_GROUP getOne(String id){
//		20150811 edit by hugo 因應操作行及清算行變更機制
//		BANK_GROUP po = bank_group_Dao.get(id);
		BANK_GROUP po = null;
		List<BANK_GROUP> list = bank_group_Dao.getDataByBgbkId(id);
		if(list!=null && list.size()!=0){
			for(BANK_GROUP tmp :list){
				po = tmp;
			}
		}
		return po;
	}
	
	/**
	 * 依據bgbkid 查詢MASTER_BANK_GROUP
	 * @param id
	 * @return
	 */
	public BANK_GROUP getOneFromMaster(String id){
		BANK_GROUP po = null;
		List<BANK_GROUP> list = bank_group_Dao.getDataFromMasterByBgbkId(id);
		if(list!=null && list.size()!=0){
			for(BANK_GROUP tmp :list){
				po = tmp;
			}
		}
		return po;
	}
	
	/**
	 * 
	 * @param params
	 * @return
	 */
	public String getOpt_Items(Map<String, String> params){
		String json = "{}";
		Map<String,String> rtnMap = new HashMap();
		try {
			rtnMap  =  SpringAppCtxHelper.getBean("opt_type");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		json =  JSONUtils.map2json(rtnMap);
		return json;
	}
	
	
	/**
	 * 取得總行代號清單
	 * @return
	 */
	public List<LabelValueBean> getBgbkIdList(){
		List<BANK_GROUP> list = bank_group_Dao.getBgbkIdList_Not_5_And_6();
		List<LabelValueBean> beanList = new LinkedList<LabelValueBean>();
		LabelValueBean bean = null;
		for(BANK_GROUP po : list){
			bean = new LabelValueBean(po.getBGBK_ID() + " - " + po.getBGBK_NAME(), po.getBGBK_ID());
			beanList.add(bean);
		}
		System.out.println("beanList>>"+beanList);
		return beanList;
	}
	
	/**
	 * 取得EACH總行代號清單
	 * @return
	 */
	public List<LabelValueBean> getBgbkIdList_EACH(){
		List<BANK_GROUP> list = bank_group_Dao.getBgbkIdList_Not_5_And_6_And_IS_EACH();
		List<LabelValueBean> beanList = new LinkedList<LabelValueBean>();
		LabelValueBean bean = null;
		for(BANK_GROUP po : list){
			bean = new LabelValueBean(po.getBGBK_ID() + " - " + po.getBGBK_NAME(), po.getBGBK_ID());
			beanList.add(bean);
		}
		System.out.println("beanList>>"+beanList);
		return beanList;
	}
	/**
	 * 
	 * @return
	 */
	public List<LabelValueBean> getActiveBgbkIdList(){
//		List<BANK_GROUP> list = bank_group_Dao.getBgbkIdList();
		List<BANK_GROUP> list = bank_group_Dao.getBgbkIdList_Not_5_And_6();
		List<LabelValueBean> beanList = new LinkedList<LabelValueBean>();
		LabelValueBean bean = null;
		String stopDate = "";
		for(BANK_GROUP po : list){
			if(StrUtils.isNotEmpty(po.getSTOP_DATE())){
				stopDate = po.getSTOP_DATE().trim();
			}
			if(StrUtils.isEmpty(stopDate) || 
			   Integer.valueOf(stopDate) > Integer.valueOf(zDateHandler.getTWDate()) ){
				bean = new LabelValueBean(po.getBGBK_ID() + " - " + po.getBGBK_NAME(), po.getBGBK_ID());
				beanList.add(bean);
			}
		}
		System.out.println("beanList>>"+beanList);
		return beanList;
	}
	/**
	 * 從BANK_OPBK取得所有操作行清單
	 * @return
	 */
//	public List<LabelValueBean> getOpbkList(){
//		List<BANK_GROUP> list = bank_group_Dao.getBgbkIdList_2();
//		List<LabelValueBean> beanList = new LinkedList<LabelValueBean>();
//		LabelValueBean bean = null;
//		for(BANK_GROUP po : list){
//			bean = new LabelValueBean(po.getBGBK_ID() + " - " + po.getBGBK_NAME(), po.getBGBK_ID());
//			beanList.add(bean);
//		}
//		System.out.println("beanList>>"+beanList);
//		return beanList;
//		20150817 edit by hugo  變更操作行已定案 改用此API
//		List<BANK_OPBK> list = bank_opbk_bo.getAllOpbkList();
//		List<LabelValueBean> beanList = new LinkedList<LabelValueBean>();
//		LabelValueBean bean = null;
//		for(BANK_OPBK po : list){
//			bean = new LabelValueBean(po.getOPBK_ID() + " - " + po.getOPBK_NAME(), po.getOPBK_ID());
//			beanList.add(bean);
//		}
//		return beanList;
//	}
	
	/**
	 * 查詢對映BANK_GROUP(VIEW)的所有操作行清單
	 * @return
	 */
	public List<LabelValueBean> getOpbkList(){
		List<BANK_OPBK> list = bank_opbk_bo.getAllOpbkList();
		List<LabelValueBean> beanList = new LinkedList<LabelValueBean>();
		LabelValueBean bean = null;
		for(BANK_OPBK po : list){
			bean = new LabelValueBean(po.getOPBK_ID() + " - " + po.getOPBK_NAME(), po.getOPBK_ID());
			beanList.add(bean);
		}
		return beanList;
	}
	/**
	 * 查詢對映MASTER_BANK_GROUP的所有操作行清單
	 * @return
	 */
	public List<LabelValueBean> getOpbkListFromMaster(){
		List<BANK_OPBK> list = bank_opbk_bo.getAllOpbkListFromMaster();
		List<LabelValueBean> beanList = new LinkedList<LabelValueBean>();
		LabelValueBean bean = null;
		for(BANK_OPBK po : list){
			bean = new LabelValueBean(po.getOPBK_ID() + " - " + po.getOPBK_NAME(), po.getOPBK_ID());
			beanList.add(bean);
		}
		return beanList;
	}
	
	/**
	 * 取得操作行代號清單(AJAX)
	 * @return
	 */
	public String getOpbkList(Map<String, String> params){
		//List<LabelValueBean> beanList = getBgbkIdListII();
		List<LabelValueBean> beanList = getOpbkList();
		Map rtnMap = new HashMap();
		if(beanList != null){
			rtnMap.put("result", "TRUE");
			rtnMap.put("msg", beanList);
		}else{
			rtnMap.put("result", "FALSE");
			rtnMap.put("msg", "無操作行資料");
		}
		return JSONUtils.map2json(rtnMap);
	}
	
	/**
	 * 查詢對映BANK_GROUP(VIEW)的所有清算行清單
	 * @return
	 */
	public List<LabelValueBean> getCtbkIdList(){
//		List<BANK_GROUP> list = bank_group_Dao.getctbkIdList();
		List<BANK_CTBK> list = bank_ctbk_bo.getAllCtbkList();
		List<LabelValueBean> beanList = new LinkedList<LabelValueBean>();
		LabelValueBean bean = null;
		for(BANK_CTBK po : list){
//			bean = new LabelValueBean(po.getBGBK_ID() + " - " + po.getBGBK_NAME(), po.getBGBK_ID());
			bean = new LabelValueBean(po.getCTBK_ID() + " - " + po.getCTBK_NAME(), po.getCTBK_ID());
			beanList.add(bean);
		}
		System.out.println("beanList>>"+beanList);
		return beanList;
	}
	/**
	 * 查詢對映MASTER_BANK_GROUP的所有清算行清單
	 * @return
	 */
	public List<LabelValueBean> getCtbkIdListFromMaster(){
//		List<BANK_GROUP> list = bank_group_Dao.getctbkIdList();
		List<BANK_CTBK> list = bank_ctbk_bo.getAllCtbkListFromMaster();
		List<LabelValueBean> beanList = new LinkedList<LabelValueBean>();
		LabelValueBean bean = null;
		for(BANK_CTBK po : list){
//			bean = new LabelValueBean(po.getBGBK_ID() + " - " + po.getBGBK_NAME(), po.getBGBK_ID());
			bean = new LabelValueBean(po.getCTBK_ID() + " - " + po.getCTBK_NAME(), po.getCTBK_ID());
			beanList.add(bean);
		}
		System.out.println("beanList>>"+beanList);
		return beanList;
	}
	
	
	/**
	 * 取得清算行代號清單(Ajax)
	 * @return
	 */
	public String getCtbkList(Map<String, String> params){
		List<LabelValueBean> beanList = getCtbkIdList();
		Map rtnMap = new HashMap();
		if(beanList != null){
			rtnMap.put("result", "TRUE");
			rtnMap.put("msg", beanList);
		}else{
			rtnMap.put("result", "FALSE");
			rtnMap.put("msg", "無清算行資料");
		}
		return JSONUtils.map2json(rtnMap);
	}
	
//	取得代理清算行清單
	public List<LabelValueBean> getAll_Proxy_CtbkList(){
		List<BANK_CTBK> list = bank_ctbk_bo.getAll_Proxy_CtbkList();
		List<LabelValueBean> beanList = new LinkedList<LabelValueBean>();
		LabelValueBean bean = null;
		if(list ==null){
			bean = new LabelValueBean("目前尚無代理清算銀行", "");
			beanList.add(bean);
		}else{
			for(BANK_CTBK po : list){
				bean = new LabelValueBean(po.getCTBK_ID() + " - " + po.getCTBK_NAME(), po.getCTBK_ID());
				beanList.add(bean);
			}
		}
		System.out.println("beanList>>"+beanList);
		return beanList;
	}
	
	
	public List<LabelValueBean> getBgbkIdListByCT(String ctbk_id , String s_bizdate , String e_bizdate){
//		List<BANK_GROUP> list = bank_group_Dao.getctbkIdList();
		List<BANK_CTBK> list = bank_ctbk_bo.getBgbkList(ctbk_id, s_bizdate, e_bizdate);
		List<LabelValueBean> beanList = new LinkedList<LabelValueBean>();
		LabelValueBean bean = null;
		for(BANK_CTBK po : list){
			bean = new LabelValueBean(po.getBGBK_ID() + " - " + po.getBGBK_NAME(), po.getBGBK_ID());
			beanList.add(bean);
		}
		System.out.println("beanList>>"+beanList);
		return beanList;
	}
	
	
	/**
	 * 用BGBK_ID取得業務清單
	 * @param bgbkId
	 * @param bsTypeId
	 * @return
	 */
	public List<LabelValueBean> getbgbList(String bgbkId, String bsTypeId){
		List<LabelValueBean> list = null ;
		List<BANK_GROUP_BUSINESS> dataList = null ;
		List<BUSINESS_TYPE> bsList = null;
		String bsName = "";
		if(StrUtils.isNotEmpty(bgbkId)){
			if(StrUtils.isNotEmpty(bsTypeId)){
				BANK_GROUP_BUSINESS bgb = bank_group_business_Dao.get(new BANK_GROUP_BUSINESS_PK(bgbkId, bsTypeId));
				list = new ArrayList<LabelValueBean>();
				list.add(new LabelValueBean(
						bgb.getId().getBUSINESS_TYPE_ID() + " - " + business_type_bo.search(bgb.getId().getBUSINESS_TYPE_ID(), "").get(0).getBUSINESS_TYPE_NAME(), 
						bgb.getId().getBUSINESS_TYPE_ID()
				));
			}else{
				dataList = bank_group_business_Dao.getByBgbkId(bgbkId);
				BANK_GROUP_BUSINESS bgb = null;
				list = new ArrayList<LabelValueBean>();
				for(int i = 0; i < dataList.size(); i++){
					bgb = dataList.get(i);
					bsList = business_type_bo.search(bgb.getId().getBUSINESS_TYPE_ID(), "");
					if(bsList != null && bsList.size() > 0){
						bsName = bsList.get(0).getBUSINESS_TYPE_NAME();
					}
					list.add(new LabelValueBean(
							bgb.getId().getBUSINESS_TYPE_ID() + " - " + bsName, 
							bgb.getId().getBUSINESS_TYPE_ID()
					));
				}
			}
		}
		return list;
	}
	
	/**
	 * 依照BGBK_ID或是加上BUSINESS_TYPE_ID查詢業務清單
	 * @param bgbkId
	 * @param bsTypeId
	 * @return
	 */
	public List<BANK_GROUP_BUSINESS> searchBankGroupBusiness(String bgbkId, String bsTypeId){
		List<BANK_GROUP_BUSINESS> list = null ;
		if(StrUtils.isNotEmpty(bgbkId)){
			if(StrUtils.isNotEmpty(bsTypeId)){
				BANK_GROUP_BUSINESS bgb = bank_group_business_Dao.get(new BANK_GROUP_BUSINESS_PK(bgbkId, bsTypeId));
				list = new ArrayList<BANK_GROUP_BUSINESS>();
				list.add(bgb);
			}else{
				list = bank_group_business_Dao.getByBgbkId(bgbkId);
			}
		}
		System.out.println("list>>"+list);
		return list;
	}
	
	public Map<String, String> removeBgbByBgbkId(String bgbkId){
		Map rtnMap = new HashMap();
		List<BANK_GROUP_BUSINESS> bgbList = null;
		try{
			bgbList = searchBankGroupBusiness(bgbkId, "");
			if(bgbList == null){
				rtnMap.put("result", "FALSE");
				rtnMap.put("msg", "刪除失敗，查無該總行之承辦業務清單");
				return rtnMap;
			}
			for(int i = 0; i < bgbList.size(); i++){
				bank_group_business_Dao.remove(bgbList.get(i));
			}
			rtnMap.put("result", "TRUE");
			rtnMap.put("msg", "刪除成功");
		}catch(Exception e){
			e.printStackTrace();
			rtnMap.put("result", "FALSE");
			rtnMap.put("msg", "刪除發生錯誤:" + e.toString());
		}
		return rtnMap;
	}
	
	/**
	 * 取得該總行未選取的業務清單
	 * @return
	 */
	public List<LabelValueBean> getUnelectedBsTypeList(String bgbkId){
		List<BUSINESS_TYPE> list = bank_group_business_Dao.getUnselectedBsTypeList(bgbkId);
		List<LabelValueBean> beanList = new LinkedList<LabelValueBean>();
		LabelValueBean bean = null;
		for(BUSINESS_TYPE po : list){
			bean = new LabelValueBean(po.getBUSINESS_TYPE_ID() + " - " + po.getBUSINESS_TYPE_NAME(), po.getBUSINESS_TYPE_ID());
			beanList.add(bean);
		}
		System.out.println("beanList>>"+beanList);
		return beanList;
	}
	
	public List<BANK_GROUP> search(String bgbkId){
		List<BANK_GROUP> list = null ;
		if(StrUtils.isEmpty(bgbkId)){
//			list = bank_group_Dao.getAll();
			list = bank_group_Dao.getAllData();
		}else{
//			list = new ArrayList<BANK_GROUP>();
//			BANK_GROUP po = bank_group_Dao.get(bgbkId);
//			if(po != null){
//				list.add(po);
//			}
			list = bank_group_Dao.getDataByBgbkId(bgbkId);
		}
		System.out.println("list>>"+list);
		list = list == null? null : list.size() == 0? null:list;
		
		//測試
		//bank_group_Dao.creatWK();
		
		return list;
	}
	
	public List<BANK_GROUP> searchFromMaster(String bgbkId ){
		List<BANK_GROUP> list = null ;
		if(StrUtils.isEmpty(bgbkId)){
//			list = bank_group_Dao.getAll();
			list = bank_group_Dao.getAllDataFromMaster();
		}else{
//			list = new ArrayList<BANK_GROUP>();
//			BANK_GROUP po = bank_group_Dao.get(bgbkId);
//			if(po != null){
//				list.add(po);
//			}
			list = bank_group_Dao.getDataFromMasterByBgbkId(bgbkId);
		}
		System.out.println("list>>"+list);
		list = list == null? null : list.size() == 0? null:list;
		
		//測試
		//bank_group_Dao.creatWK();
		
		return list;
	}
	
	/**
	 * 此API是查詢MASTER_BANK_GROUP
	 * @param param
	 * @return
	 */
	public String search_toJson(Map<String, String> param){
		String opbkId = param.get("OPBK_ID");
		String ctbkId = param.get("CTBK_ID");
		String sord =StrUtils.isNotEmpty(param.get("sord"))? param.get("sord"):"";
		String sidx =StrUtils.isNotEmpty(param.get("sidx"))? param.get("sidx"):"";
		String orderSQL = StrUtils.isNotEmpty(sidx)? " ORDER BY TEMP."+sidx+" "+sord:"";
		return JSONUtils.toJson(searchFromMaster(opbkId, ctbkId , orderSQL));
	}
	
	public Map add(BANK_GROUP bg, List<BANK_GROUP_BUSINESS> bgbList){
		Map map = null;
		try {
			map = new HashMap();
			BANK_GROUP po = bank_group_Dao.get(bg.getBGBK_ID());
			if(po != null ){
				map.put("result", false);
				map.put("msg", "總行代號重覆");
				return map;
			}
			bg.setCDATE(zDateHandler.getTheDateII());
			bank_group_Dao.save(bg);
			
			//Insert to BANK_GROUP_BUSINESS
			for(int i = 0; i < bgbList.size(); i++){
				bgbList.get(i).setCDATE(zDateHandler.getTheDateII());
				bank_group_business_Dao.save(bgbList.get(i));
			}
			
			map.put("result", true);
			map.put("msg", bg.getBGBK_ID());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "新增失敗");
			return map;
		}
		return map;
	}
	public Map addII(BANK_GROUP bg, List<BANK_GROUP_BUSINESS> bgbList, String basic_crline ,String reset_crline){
		Map map = null;
		try {
			map = new HashMap();
			BANK_GROUP po = bank_group_Dao.get(bg.getBGBK_ID());
			if(po != null ){
				map.put("result", false);
				map.put("msg", "總行代號重覆");
				return map;
			}
			bg.setCDATE(zDateHandler.getTheDateII());
			bank_group_Dao.save(bg);
//			新增額度檔
			CR_LINE cr_line = new CR_LINE();
			cr_line.setBANK_ID(bg.getCTBK_ID());
			cr_line.setBASIC_CR_LINE(basic_crline);
			cr_line.setREST_CR_LINE(reset_crline);
			cr_line.setCDATE(zDateHandler.getTheDateII());
			cr_line_Dao.save(cr_line);
			
			//Insert to BANK_GROUP_BUSINESS
			for(int i = 0; i < bgbList.size(); i++){
				bgbList.get(i).setCDATE(zDateHandler.getTheDateII());
				bank_group_business_Dao.save(bgbList.get(i));
			}
			
			map.put("result", true);
			map.put("msg", bg.getBGBK_ID());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "新增失敗");
			return map;
		}
		return map;
	}
	
	/**
	 * 20150429 edit by hugo 加入rollback機制
	 * @param bg
	 * @param bgbList
	 * @param basic_crline
	 * @param reset_crline
	 * @param isaddCR
	 * @param isaddStstusTab
	 * @return
	 */
	public Map addIII(
			BANK_GROUP bg, 
			List<BANK_GROUP_BUSINESS> bgbList, 
			String basic_crline,
			String reset_crline,
			boolean isaddCR, 
			boolean isaddStstusTab){
		Map map = null;
		CR_LINE cr_line = null;
		BANKSYSSTATUSTAB sysPO= null;
		BANKAPSTATUSTAB apPO = null;
		BANKAPSTATUSTAB_PK id = null;
		BANK_CTBK bankCtbk = null;
		BANK_CTBK_PK bank_ctbk_pk = null;
		boolean result = false;
		boolean isDiff = true ;
		try {
			map = new HashMap();
			BANK_GROUP po = bank_group_Dao.get(bg.getBGBK_ID());
			if(po != null ){
				map.put("result", false);
				map.put("msg", "資料重複，PK={總行代號="+po.getBGBK_ID()+"}");
//				userlog_bo.writeFailLog("A", map, null, null, null);
				return map;
			}
			bg.setCDATE(zDateHandler.getTheDateII());
			
			//總行-操作行對應檔(須更新舊的設定及增加新的設定)
			//新增時啟用日期=操作行啟用日期=清算行啟用日期 ，檢核邏輯相同，不可小於等於營業日			
			String twDate = bg.getACTIVE_DATE();
			String tmpDate = DateTimeUtils.convertDate(DateTimeUtils.INTERCONVERSION,twDate, "yyyyMMdd", "yyyyMMdd");
			if(eachsysstatustab_bo.checkBizDate(tmpDate)){
				map.put("result", "FALSE");
				map.put("msg", "啟用日期不可小於或等於營業日:"+eachsysstatustab_bo.getBusinessDate());
//				map.put("target", "add_p");
				return map;
			}
			BANK_OPBK_PK bank_opbk_pk = new BANK_OPBK_PK(bg.getBGBK_ID(), twDate);
			BANK_OPBK bankOpbk = null;
			bankOpbk = bank_opbk_bo.getBank_opbk_Dao().get(bank_opbk_pk);
			if(bankOpbk != null){
				map.put("result", false);
				map.put("msg", "總行所屬操作行啟用日期重複，PK={總行代號="+po.getBGBK_ID()+", 啟用日期="+twDate +"}");
				return map;
			}
			bankOpbk = new BANK_OPBK();
			bankOpbk.setId(bank_opbk_pk);
			bankOpbk.setOPBK_ID(bg.getOPBK_ID());
			bankOpbk.setOP_NOTE(bg.getOP_NOTE());
			
			bank_ctbk_pk = new BANK_CTBK_PK(bg.getBGBK_ID(), twDate);
			bankCtbk = bank_ctbk_bo.getBank_ctbk_Dao().get(bank_ctbk_pk);
			if(bankCtbk !=null){
				map.put("result", false);
				map.put("msg", "總行所屬清算行啟用日期重複，PK={總行代號="+po.getBGBK_ID()+", 啟用日期="+twDate +"}");
				return map;
			}
			bankCtbk = new BANK_CTBK();
			bankCtbk.setId(bank_ctbk_pk);
			bankCtbk.setCTBK_ID(bg.getCTBK_ID());
			bankCtbk.setCT_NOTE(bg.getCT_NOTE());
//			新增額度檔
			if(isaddCR){
				cr_line = new CR_LINE();
				cr_line.setBANK_ID(bg.getCTBK_ID());
				cr_line.setBASIC_CR_LINE(basic_crline);
				cr_line.setREST_CR_LINE(reset_crline);
				cr_line.setCDATE(zDateHandler.getTheDateII());
			}
//			20150618 edit by hugo req by 李建利   改成操作行不存在於 banksysstatus or bankapstatus 就個別新增
//			如果新增的總行與操作行相同 才進行新增
//			if(isaddStstusTab){
//				sysPO= banksysstatus_Dao.get(bg.getBGBK_ID());
				sysPO= banksysstatus_Dao.get(bg.getOPBK_ID());
				if(sysPO == null){
					sysPO = new BANKSYSSTATUSTAB();
//					sysPO.setBGBK_ID(bg.getBGBK_ID());
					sysPO.setBGBK_ID(bg.getOPBK_ID());
//					20150401 add by hugo req by 票交李建利 固定帶9
					sysPO.setMBSYSSTATUS("9");
					sysPO.setUNSYNCCNT01("0");
					sysPO.setUNSYNCCNT02("0");
					sysPO.setUNSYNCCNT03("0");
					sysPO.setUNSYNCCNT04("0");
				}else{
//					有資料就不異動強制為null
					sysPO = null;
				}
				
//				id = new BANKAPSTATUSTAB_PK(bg.getBGBK_ID(), "2000");
				id = new BANKAPSTATUSTAB_PK(bg.getOPBK_ID(), "2000");
				apPO = bankapstatus_Dao.get(id);
				if(apPO == null){
					apPO = new BANKAPSTATUSTAB();
					apPO.setId(id);
//					20150401 add by hugo req by 票交李建利 固定帶9
					apPO.setMBAPSTATUS("9");
				}else{
//					有資料就不異動強制為null
					apPO = null;
				}
//			}
			//Insert to BANK_GROUP_BUSINESS
			for(int i = 0; i < bgbList.size(); i++){
				bgbList.get(i).setCDATE(zDateHandler.getTheDateII());
			}
			result = bank_group_Dao.add_saveData(bg, cr_line, sysPO, apPO, bgbList,  bankOpbk,  bankCtbk);
			if(result){
				map.put("result", result);
				map.put("msg", bg.getBGBK_ID());
				writeLog("A", bg, cr_line, sysPO, apPO, bgbList, bankOpbk, bankCtbk ,isDiff);
			}else{
				map.put("result", result);
				map.put("msg", "系統異常，新增失敗");
//				userlog_bo.writeFailLog("A", map, null, null, null);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = false;
			map.put("result", result);
			map.put("msg", "系統異常，新增失敗");
//			userlog_bo.writeFailLog("A", map, null, null, null);
			return map;
		}
		return map;
	}
	
	
	public void writeLog(String opt_type , BANK_GROUP bg, CR_LINE cr_line , BANKSYSSTATUSTAB sysPO , BANKAPSTATUSTAB apPO, List<BANK_GROUP_BUSINESS> bgbList, BANK_OPBK bankOpbk, BANK_CTBK bankCtbk ,boolean isDiff){
		Map newmap = null; 
		StringBuffer str = new StringBuffer();
		try {
			if(bg !=null){
				newmap = BeanUtils.describe(bg);
				if(bg.getIS_EACH().equals("Y")){
					newmap.put("APTAG" , "EACH");
				}else{
					newmap.put("APTAG" , "");
				}
				newmap = userlog_bo.mapremoveII(newmap, "OP_START_DATE" , "CT_START_DATE");
				newmap = userlog_bo.restMapKey2CH(newmap);
				str.append("新增總行檔:"+ JSONUtils.map2json(newmap));
			}
			
			if(cr_line !=null){
				newmap = BeanUtils.describe(cr_line);
				newmap = userlog_bo.restMapKey2CH(newmap);
				str.append("\n");
				str.append("新增額度檔:"+ JSONUtils.map2json(newmap));
			}
			if(sysPO !=null){
				newmap = BeanUtils.describe(sysPO);
				newmap = userlog_bo.restMapKey2CH(newmap);
				str.append("\n");
				str.append("新增參加單位系統狀態檔:"+ JSONUtils.map2json(newmap));
			}
			if(apPO !=null){
				newmap = BeanUtils.describe(apPO);
				newmap.putAll(BeanUtils.describe(apPO.getId()));
				newmap = userlog_bo.restMapKey2CH(newmap);
				str.append("\n");
				str.append("新增參加單位應用系統狀態檔:"+ JSONUtils.map2json(newmap));
			}
			
			
			
			if(bankOpbk != null){
				newmap = BeanUtils.describe(bankOpbk);
				newmap.putAll(BeanUtils.describe(bankOpbk.getId()));
				newmap.put("OP_START_DATE", bankOpbk.getId().getSTART_DATE());
				newmap = userlog_bo.mapremoveII(newmap, "BGBK_NAME" , "START_DATE");
				newmap = userlog_bo.restMapKey2CH(newmap);
				str.append("\n");
				str.append("新增總行所屬操作行檔:"+ JSONUtils.map2json(newmap));
			}
			if(bankCtbk != null){
				newmap = BeanUtils.describe(bankCtbk);
				newmap.putAll(BeanUtils.describe(bankCtbk.getId()));
				newmap.put("CT_START_DATE", bankCtbk.getId().getSTART_DATE());
				newmap = userlog_bo.mapremoveII(newmap, "BGBK_NAME" , "START_DATE");
				newmap = userlog_bo.restMapKey2CH(newmap);
				str.append("\n");
				str.append("新增總行所屬清算行檔:"+ JSONUtils.map2json(newmap));
			}
			EACH_USERLOG userlog  = userlog_bo.getUSERLOG(opt_type, null);
			userlog.setADEXCODE("新增成功");
			userlog.setAFCHCON(str.toString());
			userlog_bo.getUserLog_Dao().aop_save(userlog);
			if(bgbList != null && isDiff){
				for(BANK_GROUP_BUSINESS po :bgbList){
					newmap = BeanUtils.describe(po);
					newmap.putAll(BeanUtils.describe(po.getId()));
					userlog_bo.writeLog(opt_type, null, newmap, null);
				}
				
			}
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void writeLog_bak(String opt_type , BANK_GROUP bg, CR_LINE cr_line , BANKSYSSTATUSTAB sysPO , BANKAPSTATUSTAB apPO, List<BANK_GROUP_BUSINESS> bgbList, BANK_OPBK bankOpbk, BANK_CTBK bankCtbk){
		Map newmap = null; 
		try {
			if(bg !=null){
				newmap = BeanUtils.describe(bg);
				userlog_bo.writeLog(opt_type, null, newmap, null);
			}
			if(cr_line !=null){
				newmap = BeanUtils.describe(cr_line);
				userlog_bo.writeLog(opt_type, null, newmap, null);
			}
			if(sysPO !=null){
				newmap = BeanUtils.describe(sysPO);
				userlog_bo.writeLog(opt_type, null, newmap, null);
			}
			if(apPO !=null){
				newmap = BeanUtils.describe(apPO);
				newmap.putAll(BeanUtils.describe(apPO.getId()));
				userlog_bo.writeLog(opt_type, null, newmap, null);
			}
			if(bankOpbk != null){
				newmap = BeanUtils.describe(bankOpbk);
				userlog_bo.writeLog(opt_type, null, newmap, null);
			}
			if(bankCtbk != null){
				newmap = BeanUtils.describe(bankCtbk);
				userlog_bo.writeLog(opt_type, null, newmap, null);
			}
			if(bgbList != null){
				for(BANK_GROUP_BUSINESS po :bgbList){
					newmap = BeanUtils.describe(po);
					newmap.putAll(BeanUtils.describe(po.getId()));
					userlog_bo.writeLog(opt_type, null, newmap, null);
				}
				
			}
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	public void writedDeleteLog(String opt_type , BANK_GROUP bg, List<BANK_GROUP_BUSINESS> bgbList, List<BANK_OPBK> opbkList, List<BANK_CTBK> ctbkList){
		Map oldmap = null; 
		Map pkmap = new HashMap(); 
		try {
			if(bg !=null){
				oldmap = BeanUtils.describe(bg);
				pkmap.put("BGBK_ID", bg.getBGBK_ID());
				if(bg.getIS_EACH().equals("Y")){
					oldmap.put("APTAG" , "EACH");
				}else{
					oldmap.put("APTAG" , "");
				}
				oldmap = userlog_bo.mapremoveII(oldmap, "OP_START_DATE" , "CT_START_DATE");
				userlog_bo.writeLog(opt_type, oldmap, null, pkmap);
			}
			pkmap.clear();
			if(bgbList != null){
				for(BANK_GROUP_BUSINESS po :bgbList){
					oldmap = BeanUtils.describe(po);
					oldmap.putAll(BeanUtils.describe(po.getId()));
					pkmap.putAll(BeanUtils.describe(po.getId()));
					
					userlog_bo.writeLog(opt_type, oldmap, null,pkmap );
				}
				
			}
			
			
			if(opbkList != null){
				for(BANK_OPBK op :opbkList){
					oldmap = BeanUtils.describe(op);
					oldmap.putAll(BeanUtils.describe(op.getId()));
					oldmap.put("CT_START_DATE", op.getId().getSTART_DATE());
					oldmap = userlog_bo.mapremoveII(oldmap ,"BGBK_NAME", "START_DATE");
					pkmap.put("BGBK_ID", op.getId().getBGBK_ID());
					pkmap.put("OP_START_DATE", op.getId().getSTART_DATE());
					userlog_bo.writeLog(opt_type, oldmap, null, pkmap);
				}
			}
			if(ctbkList != null){
				for(BANK_CTBK ct :ctbkList){
					oldmap = BeanUtils.describe(ct);
					oldmap.putAll(BeanUtils.describe(ct.getId()));
					oldmap.put("CT_START_DATE", ct.getId().getSTART_DATE());
					oldmap = userlog_bo.mapremoveII(oldmap ,"BGBK_NAME", "START_DATE");
					pkmap.put("BGBK_ID", ct.getId().getBGBK_ID());
					pkmap.put("CT_START_DATE", ct.getId().getSTART_DATE());
					userlog_bo.writeLog(opt_type, oldmap, null, pkmap);
				}
			}
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void writeSaveLog(String opt_type, BANK_GROUP oBg, BANK_GROUP nBg, CR_LINE cr_line  ,BANKSYSSTATUSTAB sysPO , BANKAPSTATUSTAB apPO ,  List<BANK_GROUP_BUSINESS> oBgbList, List<BANK_GROUP_BUSINESS> nBgbList ,BANK_OPBK bankOpbk ,BANK_OPBK obankOpbk ,List<BANK_OPBK> delbankOpbk , BANK_CTBK bankCtbk ,BANK_CTBK obankCtbk, List<BANK_CTBK> delbankCtbk ){
		Map newmap = null; 
		Map oldmap = null ;
		StringBuffer oldstr = new StringBuffer();
		StringBuffer str = new StringBuffer();
		Map pkmap = new HashMap();
		try {
			pkmap.put("BGBK_ID", oBg.getBGBK_ID());
			pkmap= userlog_bo.restMapKey2CH(pkmap);
			if(oBg !=null){
				oldmap = BeanUtils.describe(oBg);
				newmap = BeanUtils.describe(nBg);
				if(oBg.getIS_EACH().equals("Y")){
					oldmap.put("APTAG" , "EACH");
				}else{
					oldmap.put("APTAG" , "");
				}
				if(nBg.getIS_EACH().equals("Y")){
					newmap.put("APTAG" , "EACH");
				}else{
					newmap.put("APTAG" , "");
				}
				
				Map<String,Map> tmp = userlog_bo.com_Dif_Val(newmap, oldmap);
				oldmap = tmp.get("oldmap");
				oldmap = userlog_bo.restMapKey2CH(oldmap);
				oldstr.append("總行檔:"+ JSONUtils.map2json(oldmap));
				oldstr.append("\n");
				oldstr.append("\n");
			}
			if(nBg !=null){
				oldmap = BeanUtils.describe(oBg);
				newmap = BeanUtils.describe(nBg);
				if(oBg.getIS_EACH().equals("Y")){
					oldmap.put("APTAG" , "EACH");
				}else{
					oldmap.put("APTAG" , "");
				}
				if(nBg.getIS_EACH().equals("Y")){
					newmap.put("APTAG" , "EACH");
				}else{
					newmap.put("APTAG" , "");
				}
				Map<String,Map> tmp = userlog_bo.com_Dif_Val(newmap, oldmap);
				newmap = tmp.get("newmap");
				newmap = userlog_bo.restMapKey2CH(newmap);
				str.append("總行檔:"+ JSONUtils.map2json(newmap));
				str.append("\n");
				str.append("\n");
			}
			if(obankOpbk !=null){
				oldmap = BeanUtils.describe(obankOpbk);
				newmap = BeanUtils.describe(bankOpbk);
				oldmap.put("OP_START_DATE", oldmap.get("START_DATE"));
				newmap.put("OP_START_DATE", newmap.get("START_DATE"));
				oldmap = userlog_bo.mapremoveII(oldmap, "BGBK_NAME" , "START_DATE");
				newmap = userlog_bo.mapremoveII(newmap, "BGBK_NAME" , "START_DATE");
				Map<String,Map> tmp = userlog_bo.com_Dif_Val(newmap, oldmap);
				oldmap = tmp.get("oldmap");
				oldmap = userlog_bo.restMapKey2CH(oldmap);
				
				oldstr.append("總行所屬操作行檔:"+ JSONUtils.map2json(oldmap));
				oldstr.append("\n");
				newmap = tmp.get("newmap");
				newmap = userlog_bo.restMapKey2CH(newmap);
				str.append("總行所屬操作行檔:"+ JSONUtils.map2json(newmap));
				str.append("\n");
			}
			if(obankCtbk !=null){
				oldmap = BeanUtils.describe(obankCtbk);
				newmap = BeanUtils.describe(bankCtbk);
				oldmap.put("CT_START_DATE", oldmap.get("START_DATE"));
				newmap.put("CT_START_DATE", newmap.get("START_DATE"));
				oldmap = userlog_bo.mapremoveII(oldmap, "BGBK_NAME" , "START_DATE");
				newmap = userlog_bo.mapremoveII(newmap, "BGBK_NAME" , "START_DATE");
				Map<String,Map> tmp = userlog_bo.com_Dif_Val(newmap, oldmap);
				oldmap = tmp.get("oldmap");
				oldmap = userlog_bo.restMapKey2CH(oldmap);
				oldstr.append("總行所屬清算行檔:"+ JSONUtils.map2json(oldmap));
				newmap = tmp.get("newmap");
				newmap = userlog_bo.restMapKey2CH(newmap);
				str.append("總行所屬清算行檔:"+ JSONUtils.map2json(newmap));
				str.append("\n");
			}
			
			
			
			if(bankOpbk != null && obankOpbk == null){
				newmap = BeanUtils.describe(bankOpbk);
				newmap.putAll(BeanUtils.describe(bankOpbk.getId()));
				newmap.put("OP_START_DATE", newmap.get("START_DATE"));
				newmap = userlog_bo.mapremoveII(newmap, "BGBK_NAME" , "START_DATE");
				newmap = userlog_bo.restMapKey2CH(newmap);
				str.append("新增總行所屬操作行檔:"+ JSONUtils.map2json(newmap));
				str.append("\n");
			}
			if(bankCtbk != null && obankCtbk == null){
				newmap = BeanUtils.describe(bankCtbk);
				newmap.putAll(BeanUtils.describe(bankCtbk.getId()));
				newmap.put("CT_START_DATE", newmap.get("START_DATE"));
				newmap = userlog_bo.mapremoveII(newmap, "BGBK_NAME" , "START_DATE");
				newmap = userlog_bo.restMapKey2CH(newmap);
				str.append("新增總行所屬清算行檔:"+ JSONUtils.map2json(newmap));
				str.append("\n");
			}
			
			if(delbankOpbk != null && delbankOpbk.size()!=0){
				for(BANK_OPBK po : delbankOpbk){
					newmap = BeanUtils.describe(po);
					newmap.putAll(BeanUtils.describe(po.getId()));
					newmap.put("OP_START_DATE", newmap.get("START_DATE"));
					newmap = userlog_bo.mapremoveII(newmap, "BGBK_NAME" , "START_DATE");
					newmap = userlog_bo.restMapKey2CH(newmap);
					str.append("刪除總行所屬操作行檔:"+ JSONUtils.map2json(newmap));
					str.append("\n");
					str.append("\n");
				}
			}
			if(delbankCtbk != null && delbankCtbk.size()!=0){
				for(BANK_CTBK po : delbankCtbk){
					newmap = BeanUtils.describe(po);
					newmap.putAll(BeanUtils.describe(po.getId()));
					newmap.put("CT_START_DATE", newmap.get("START_DATE"));
					newmap = userlog_bo.mapremoveII(newmap, "BGBK_NAME" , "START_DATE");
					newmap = userlog_bo.restMapKey2CH(newmap);
					str.append("刪除總行所屬清算行檔:"+ JSONUtils.map2json(newmap));
					str.append("\n");
					str.append("\n");
				}
			}
			
			
			if(cr_line !=null){
				newmap = BeanUtils.describe(cr_line);
				newmap = userlog_bo.restMapKey2CH(newmap);
				str.append("新增額度檔:"+ JSONUtils.map2json(newmap));
				str.append("\n");
			}
			if(sysPO !=null){
				newmap = BeanUtils.describe(sysPO);
				newmap = userlog_bo.restMapKey2CH(newmap);
				str.append("新增參加單位系統狀態檔:"+ JSONUtils.map2json(newmap));
				str.append("\n");
			}
			if(apPO !=null){
				newmap = BeanUtils.describe(apPO);
				newmap.putAll(BeanUtils.describe(apPO.getId()));
				newmap = userlog_bo.restMapKey2CH(newmap);
				str.append("新增參加單位應用系統狀態檔:"+ JSONUtils.map2json(newmap));
				str.append("\n");
			}
			EACH_USERLOG userlog  = userlog_bo.getUSERLOG(opt_type, null);
			userlog.setADEXCODE("修改成功，PK="+JSONUtils.map2json(pkmap));
			userlog.setBFCHCON(oldstr.toString());
			userlog.setAFCHCON(str.toString());
			userlog_bo.getUserLog_Dao().aop_save(userlog);
			
			
			//業務類別清單需先刪再新增
			if(oBgbList != null){
				writedDeleteLog("D", null, oBgbList, null, null);
			}
			if(nBgbList != null){
				for(BANK_GROUP_BUSINESS po : nBgbList){
					newmap.clear();
					pkmap.clear();
					
					newmap = BeanUtils.describe(po);
					newmap.putAll(BeanUtils.describe(po.getId()));
					pkmap.putAll(BeanUtils.describe(po.getId()));
					
					userlog_bo.writeLog("A", null, newmap, pkmap);
				}
			}
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void writeSaveLog_bak(String opt_type, BANK_GROUP oBg, BANK_GROUP nBg, List<BANK_GROUP_BUSINESS> oBgbList, List<BANK_GROUP_BUSINESS> nBgbList){
		Map oldmap = null, newmap = null; 
		Map pkmap = new HashMap();
		int readyToWrite = 2;
		try {
			if(oBg != null){
				oldmap = BeanUtils.describe(oBg);
				pkmap.put("BGBK_ID", oBg.getBGBK_ID());
				readyToWrite--;
			}
			if(nBg != null){
				newmap = BeanUtils.describe(nBg);
				readyToWrite--;
			}
			if(readyToWrite == 0){
				userlog_bo.writeLog(opt_type, oldmap, newmap, pkmap);
			}
			
			//業務類別清單需先刪再新增
			if(oBgbList != null){
				writedDeleteLog("D", null, oBgbList, null, null);
			}
			if(nBgbList != null){
				for(BANK_GROUP_BUSINESS po : nBgbList){
					newmap.clear();
					pkmap.clear();
					
					newmap = BeanUtils.describe(po);
					newmap.putAll(BeanUtils.describe(po.getId()));
					pkmap.putAll(BeanUtils.describe(po.getId()));
					
					userlog_bo.writeLog("A", null, newmap, pkmap);
				}
			}
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
//	public Map addIII(BANK_GROUP bg, List<BANK_GROUP_BUSINESS> bgbList, String basic_crline ,String reset_crline ,boolean isaddCR , boolean isaddStstusTab){
//		Map map = null;
//		try {
//			map = new HashMap();
//			BANK_GROUP po = bank_group_Dao.get(bg.getBGBK_ID());
//			if(po != null ){
//				map.put("result", false);
//				map.put("msg", "總行代號重覆");
//				return map;
//			}
//			bg.setCDATE(zDateHandler.getTheDateII());
//			bank_group_Dao.save(bg);
////			新增額度檔
//			if(isaddCR){
//				CR_LINE cr_line = new CR_LINE();
//				cr_line.setBANK_ID(bg.getCTBK_ID());
//				cr_line.setBASIC_CR_LINE(basic_crline);
//				cr_line.setREST_CR_LINE(reset_crline);
//				cr_line.setCDATE(zDateHandler.getTheDateII());
//				cr_line_Dao.save(cr_line);
//			}
////			如果新增的總行與操作行相同 才進行新增
//			if(isaddStstusTab){
//				BANKSYSSTATUSTAB sysPO= banksysstatus_Dao.get(bg.getBGBK_ID());
//				if(sysPO == null){
//					sysPO = new BANKSYSSTATUSTAB();
//					sysPO.setBGBK_ID(bg.getBGBK_ID());
////					20150401 add by hugo req by 票交李建利 固定帶9
//					sysPO.setMBSYSSTATUS("9");
//					sysPO.setUNSYNCCNT01("0");
//					sysPO.setUNSYNCCNT02("0");
//					sysPO.setUNSYNCCNT03("0");
//					sysPO.setUNSYNCCNT04("0");
//					banksysstatus_Dao.save(sysPO);
//				}
//				BANKAPSTATUSTAB_PK id = new BANKAPSTATUSTAB_PK(bg.getBGBK_ID(), "2000");
//				BANKAPSTATUSTAB apPO = bankapstatus_Dao.get(id);
//				if(apPO == null){
//					apPO = new BANKAPSTATUSTAB();
//					apPO.setId(id);
////					20150401 add by hugo req by 票交李建利 固定帶9
//					apPO.setMBAPSTATUS("9");
//					bankapstatus_Dao.save(apPO);
//				}
//			}
//			//Insert to BANK_GROUP_BUSINESS
//			for(int i = 0; i < bgbList.size(); i++){
//				bgbList.get(i).setCDATE(zDateHandler.getTheDateII());
//				bank_group_business_Dao.save(bgbList.get(i));
//			}
//			
//			map.put("result", true);
//			map.put("msg", bg.getBGBK_ID());
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			map.put("result", false);
//			map.put("msg", "新增失敗");
//			return map;
//		}
//		return map;
//	}
	
	
//	
//	public boolean save(BANK_GROUP bg, String[] selectedBsType){
//		boolean result = false;
//		try {
//			//找出要刪除的清單
//			List<BANK_GROUP_BUSINESS> bgbList_toDelete = null;
//			bgbList_toDelete = searchBankGroupBusiness(bg.getBGBK_ID(), "");
//			//建立要新增的清單
//			List<BANK_GROUP_BUSINESS> bgbList_toInsert = new ArrayList<BANK_GROUP_BUSINESS>();
//			BANK_GROUP_BUSINESS bgb = null;
//			if(selectedBsType != null){
//				for(int i = 0; i < selectedBsType.length; i++){
//					bgb = new BANK_GROUP_BUSINESS();
//					bgb.setId(new BANK_GROUP_BUSINESS_PK(bg.getBGBK_ID(), selectedBsType[i]));
//					bgbList_toInsert.add(bgb);
//				}
//			}
//			bg.setUDATE(zDateHandler.getTheDateII());
//			//存檔
//			//測試壞掉情況
//			//bg.setBGBK_ID("000000000000000000000000000000000000000000000");
//			result = bank_group_Dao.saveData(bg, bgbList_toDelete, bgbList_toInsert, null, null, null, null);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return result;
//	}
	
	/**
	 * 修改資料-操作紀錄
	 * @param bg
	 * @param selectedBsType
	 * @return
	 */
	public Map<String, Object> saveII(BANK_GROUP bg, String[] selectedBsType  , boolean isaddCR ,String basic_crline ,String reset_crline  ){
		boolean result = false;
		boolean isDiff = true;
		Map<String, Object> rtnMap = new HashMap<String, Object>();
		Map<String, String> pkmap = new HashMap<String, String>();
		BANKSYSSTATUSTAB sysPO= null;
		BANKAPSTATUSTAB apPO = null;
		BANKAPSTATUSTAB_PK id = null;
		CR_LINE cr_line = null;
		BANK_OPBK_PK bank_opbk_pk = null;
		BANK_OPBK bankOpbk= null;
		BANK_CTBK bankCtbk = null;
		BANK_OPBK obankOpbk= null;
		BANK_CTBK obankCtbk = null;
		BANK_CTBK_PK bank_ctbk_pk = null;
		List<BANK_OPBK> bankOpbkList = null;
		List<BANK_CTBK> bankCtbkList = null;
		String bizdate = "";
		try {
			
			//檢查總行是否存在
//			BANK_GROUP oBg = getOne(bg.getBGBK_ID());
			BANK_GROUP oBg = getOneFromMaster(bg.getBGBK_ID());
			if(oBg == null){
				rtnMap.put("result", false);
				rtnMap.put("msg", "資料不存在 ，PK={總行代號="+bg.getBGBK_ID()+"}");
//				userlog_bo.writeFailLog("B", rtnMap, null, null, null);
				return rtnMap;
			}
			pkmap.put("BGBK_ID", bg.getBGBK_ID());
			
			//檢查是否有修改操作行
			System.out.println("操作行是否相同:"+bg.getOPBK_ID().equals(oBg.getOPBK_ID()));
//			 啟用日期不同也算 另外要討論 未來啟用日期只保持一筆 (已跟建利確認)
			if(!bg.getOPBK_ID().equals(oBg.getOPBK_ID()) || !bg.getOP_START_DATE().equals(oBg.getOP_START_DATE())){
//				 要抓UI帶過來的日期
//				String twDate = zDateHandler.getTWDate();
				String twDate = bg.getOP_START_DATE();
				String tmpDate = DateTimeUtils.convertDate(DateTimeUtils.INTERCONVERSION,twDate, "yyyyMMdd", "yyyyMMdd");
				if(eachsysstatustab_bo.checkBizDate(tmpDate)){
					rtnMap.put("result", "FALSE");
					rtnMap.put("msg", "總行所屬操作行啟用日期不可小於或等於營業日:"+eachsysstatustab_bo.getBusinessDate());
//					rtnMap.put("target", "add_p");
					return rtnMap;
				}
				bank_opbk_pk = new BANK_OPBK_PK(bg.getBGBK_ID(), twDate);
				bankOpbk = new BANK_OPBK();
				bankOpbk = bank_opbk_bo.getBank_opbk_Dao().get(bank_opbk_pk);
				if(bankOpbk != null){
					rtnMap.put("result", false);
					rtnMap.put("msg", "總行所屬操作行啟用日期重複，PK={總行代號="+bg.getBGBK_ID()+", 啟用日期="+twDate +"}");
					return rtnMap;
				}
				bankOpbk = new BANK_OPBK();
				bankOpbk.setId(bank_opbk_pk);
				bankOpbk.setOPBK_ID(bg.getOPBK_ID());
				bankOpbk.setOP_NOTE(bg.getOP_NOTE());
//				如果已有大於營業日的操作行資料就先都刪除
				bizdate = eachsysstatustab_bo.getBusinessDate();
				bankOpbkList = bank_opbk_bo.getFutureByBgbkId(bg.getBGBK_ID(), bizdate);
			}
			
//			只改操作行異動備註的情況
			if(bg.getOPBK_ID().equals(oBg.getOPBK_ID()) && bg.getOP_START_DATE().equals(oBg.getOP_START_DATE()) && !bg.getOP_NOTE().equals(oBg.getOP_NOTE()) ){
				String twDate = bg.getOP_START_DATE();
				bank_opbk_pk = new BANK_OPBK_PK(bg.getBGBK_ID(), twDate);
				bankOpbk = new BANK_OPBK();
				obankOpbk = bank_opbk_bo.getBank_opbk_Dao().get(bank_opbk_pk);
				if(obankOpbk ==null){
					rtnMap.put("result", false);
					rtnMap.put("msg", "總行所屬操作行，查無資料，PK={總行代號="+bg.getBGBK_ID()+", 操作行代號="+bg.getCTBK_ID() +", 啟用日期="+twDate +"}");
					return rtnMap;
				}
				bankOpbk.setId(bank_opbk_pk);
				bankOpbk.setOP_NOTE(bg.getOP_NOTE());
				bankOpbk.setOPBK_ID(obankOpbk.getOPBK_ID());
			}
			
			
			//檢查是否有修改清算行
			System.out.println("清算行是否相同:"+bg.getCTBK_ID().equals(oBg.getCTBK_ID()));
			if(!bg.getCTBK_ID().equals(oBg.getCTBK_ID()) || !bg.getCT_START_DATE().equals(oBg.getCT_START_DATE())){
//				String twDate = zDateHandler.getTWDate();
				String twDate = bg.getCT_START_DATE();
				String tmpDate = DateTimeUtils.convertDate(DateTimeUtils.INTERCONVERSION,twDate, "yyyyMMdd", "yyyyMMdd");
				if(eachsysstatustab_bo.checkBizDate(tmpDate)){
					rtnMap.put("result", "FALSE");
					rtnMap.put("msg", "總行所屬清算行啟用日期不可小於或等於營業日:"+eachsysstatustab_bo.getBusinessDate());
//					rtnMap.put("target", "add_p");
					return rtnMap;
				}
				bank_ctbk_pk = new BANK_CTBK_PK(bg.getBGBK_ID(), twDate);
				bankCtbk = bank_ctbk_bo.getBank_ctbk_Dao().get(bank_ctbk_pk);
				if(bankCtbk !=null){
					rtnMap.put("result", false);
					rtnMap.put("msg", "總行所屬清算行啟用日期重複，PK={總行代號="+bg.getBGBK_ID()+", 清算行代號="+bg.getCTBK_ID()+", 啟用日期="+twDate +"}");
					return rtnMap;
				}
				bankCtbk = new BANK_CTBK();
				bankCtbk.setId(bank_ctbk_pk);
				bankCtbk.setCTBK_ID(bg.getCTBK_ID());
				bankCtbk.setCT_NOTE(bg.getCT_NOTE());
//				如果已有大於營業日的清算行資料就先都刪除
				bizdate = eachsysstatustab_bo.getBusinessDate();
				bankCtbkList = bank_ctbk_bo.getFutureByBgbkId(bg.getBGBK_ID(), bizdate);
			}
//			只改清算行異動備註的情況
			if(bg.getCTBK_ID().equals(oBg.getCTBK_ID()) && bg.getCT_START_DATE().equals(oBg.getCT_START_DATE()) && !bg.getCT_NOTE().equals(oBg.getCT_NOTE()) ){
				String twDate = bg.getCT_START_DATE();
				bank_ctbk_pk = new BANK_CTBK_PK(bg.getBGBK_ID(), twDate);
				bankCtbk = new BANK_CTBK();
				obankCtbk = bank_ctbk_bo.getBank_ctbk_Dao().get(bank_ctbk_pk);
				if(bankCtbk ==null){
					rtnMap.put("result", false);
					rtnMap.put("msg", "總行所屬清算行，查無資料，PK={總行代號="+bg.getBGBK_ID()+", 清算行代號="+bg.getCTBK_ID() +", 啟用日期="+twDate +"}");
					return rtnMap;
				}
				bankCtbk.setId(bank_ctbk_pk);
				bankCtbk.setCT_NOTE(bg.getCT_NOTE());
				bankCtbk.setCTBK_ID(obankCtbk.getCTBK_ID());
			}
			
			//找出要刪除的清單
			List<BANK_GROUP_BUSINESS> bgbList_toDelete = null;
			bgbList_toDelete = searchBankGroupBusiness(bg.getBGBK_ID(), "");
			//建立要新增的清單
			List<BANK_GROUP_BUSINESS> bgbList_toInsert = new ArrayList<BANK_GROUP_BUSINESS>();
			BANK_GROUP_BUSINESS bgb = null;
			if(selectedBsType != null){
				for(int i = 0; i < selectedBsType.length; i++){
					bgb = new BANK_GROUP_BUSINESS();
					bgb.setId(new BANK_GROUP_BUSINESS_PK(bg.getBGBK_ID(), selectedBsType[i]));
					bgbList_toInsert.add(bgb);
				}
			}
			
//			新增額度檔
			if(isaddCR){
				cr_line = new CR_LINE();
				cr_line.setBANK_ID(bg.getCTBK_ID());
				cr_line.setBASIC_CR_LINE(basic_crline);
				cr_line.setREST_CR_LINE(reset_crline);
				cr_line.setCDATE(zDateHandler.getTheDateII());
			}
			
			bg.setUDATE(zDateHandler.getTheDateII());
			bg.setCDATE(oBg.getCDATE());
			bg.setEDDA_FLAG(oBg.getEDDA_FLAG());
			sysPO= banksysstatus_Dao.get(bg.getOPBK_ID());
			if(sysPO == null){
				sysPO = new BANKSYSSTATUSTAB();
//				sysPO.setBGBK_ID(bg.getBGBK_ID());
				sysPO.setBGBK_ID(bg.getOPBK_ID());
//				20150401 add by hugo req by 票交李建利 固定帶9
				sysPO.setMBSYSSTATUS("9");
				sysPO.setUNSYNCCNT01("0");
				sysPO.setUNSYNCCNT02("0");
				sysPO.setUNSYNCCNT03("0");
				sysPO.setUNSYNCCNT04("0");
			}else{
//				有資料就不異動強制為null
				sysPO = null;
			}
			
//			id = new BANKAPSTATUSTAB_PK(bg.getBGBK_ID(), "2000");
			id = new BANKAPSTATUSTAB_PK(bg.getOPBK_ID(), "2000");
			apPO = bankapstatus_Dao.get(id);
			if(apPO == null){
				apPO = new BANKAPSTATUSTAB();
				apPO.setId(id);
//				20150401 add by hugo req by 票交李建利 固定帶9
				apPO.setMBAPSTATUS("9");
			}else{
//				有資料就不異動強制為null
				apPO = null;
			}
			
			result = bank_group_Dao.saveData(bg, bgbList_toDelete, bgbList_toInsert,bankOpbkList , bankOpbk,bankCtbkList , bankCtbk ,sysPO ,apPO ,cr_line );
			List<BANK_GROUP_BUSINESS> removeObj = new ArrayList<BANK_GROUP_BUSINESS>();
			
			for(BANK_GROUP_BUSINESS tmp :bgbList_toInsert){
				
				if(bgbList_toDelete.contains(tmp)){
					System.out.println("重覆的ID>>"+bgbList_toDelete.get(bgbList_toDelete.indexOf(tmp)).getId().getBUSINESS_TYPE_ID());
					removeObj.add(tmp);
				}
			}
			
			for(BANK_GROUP_BUSINESS tmp : removeObj){
				bgbList_toDelete.remove(tmp);
				bgbList_toInsert.remove(tmp);
			}
			if(result){
				writeSaveLog("B", oBg, bg, cr_line , sysPO, apPO, bgbList_toDelete, bgbList_toInsert, bankOpbk, obankOpbk, bankOpbkList, bankCtbk, obankCtbk, bankCtbkList);
//				writeSaveLog("B", oBg, bg, cr_line , sysPO, apPO, bgbList_toDelete, bgbList_toInsert);
				rtnMap.put("result", true);
				rtnMap.put("msg", "儲存成功");
			}else{
				rtnMap.put("result", false);
				rtnMap.put("msg", "儲存失敗");
//				userlog_bo.writeFailLog("B", rtnMap, BeanUtils.describe(oBg), BeanUtils.describe(bg), pkmap);
			}
		} catch (Exception e) {
			e.printStackTrace();
			rtnMap.put("result", "FALSE");
			rtnMap.put("msg", "系統異常，儲存失敗");
//			userlog_bo.writeFailLog("B", rtnMap, null, null, pkmap);
		}
		return rtnMap;
	}
	
	public Map<String, String> delete(BANK_GROUP bg){
		Map rtnMap = new HashMap();
		boolean result = false ;
		try {
			BANK_GROUP po = bank_group_Dao.get(bg.getBGBK_ID());
			if(po == null ){
				rtnMap.put("result", "FALSE");
				rtnMap.put("msg", "資料不存在 ，PK={"+bg.getBGBK_ID()+"}");
//				userlog_bo.writeFailLog("D", rtnMap, null, null, null);
				return rtnMap;
			}
			//檢查該總行下是否有分行，若有則不允許刪除
			List<BANK_BRANCH> branchList = bank_branch_Dao.getDataByBgBkId(bg.getBGBK_ID());
			if(branchList != null){
				if(branchList.size() > 0){
					rtnMap.put("result", "FALSE");
					rtnMap.put("msg", "刪除失敗，該總行尚有分行資料，PK={BGBK_ID="+bg.getBGBK_ID()+"}");
//					userlog_bo.writeFailLog("D", rtnMap, null, null, null);
					return rtnMap;
				}
			}
			//找出要刪除的承作業務清單
			List<BANK_GROUP_BUSINESS> bgbList = searchBankGroupBusiness(bg.getBGBK_ID(), "");
			//找出要刪除的BANK_OPBK
			List<BANK_OPBK> opbkList = bank_opbk_bo.getByBgbkId(bg.getBGBK_ID());
			//找出要刪除的BANK_CTBK
			List<BANK_CTBK> ctbkList = bank_ctbk_bo.getByBgbkId(bg.getBGBK_ID());
			result = bank_group_Dao.delData(bg, bgbList, opbkList, ctbkList);
			if(result){
				rtnMap.put("result", "TRUE");
				rtnMap.put("msg", "刪除成功");
				writedDeleteLog("D",bg, bgbList, opbkList, ctbkList);
			}else{
				rtnMap.put("result", "FALSE");
				rtnMap.put("msg", "系統異常，刪除失敗");
//				userlog_bo.writeFailLog("D", rtnMap, null, null, null);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rtnMap.put("result", "FALSE");
			rtnMap.put("msg", "系統異常，刪除失敗");
//			userlog_bo.writeFailLog("D", rtnMap, null, null, null);
		}
		return rtnMap;
	}
	
	public List<BANK_GROUP> search(String opbkId, String ctbkId){
		List<BANK_GROUP> list = null;
		List<String> conditions = new ArrayList<String>();
 		if(StrUtils.isNotEmpty(opbkId)){
 			conditions.add(" OPBK_ID = '" + opbkId + "' ");
		}
 		if(StrUtils.isNotEmpty(ctbkId)){
 			conditions.add(" CTBK_ID = '" + ctbkId + "' ");
 		}
		list = bank_group_Dao.getByOpbkidAndCtbkid(conditions);
		System.out.println("list>>"+list);
		list = list == null? null : list.size() == 0? null:list;
		
		return list;
	}
	public List<BANK_GROUP> searchFromMaster(String opbkId, String ctbkId , String orderSQL){
		List<BANK_GROUP> list = null;
		List<String> conditions = new ArrayList<String>();
		if(StrUtils.isNotEmpty(opbkId)){
			conditions.add(" OPBK_ID = '" + opbkId + "' ");
		}
		if(StrUtils.isNotEmpty(ctbkId)){
			conditions.add(" CTBK_ID = '" + ctbkId + "' ");
		}
		list = bank_group_Dao.getByOpbkidAndCtbkidFromMaster(conditions  , orderSQL);
		System.out.println("list>>"+list);
		list = list == null? null : list.size() == 0? null:list;
		
		return list;
	}
	
	
	/**
	 * 頁面查詢用(適用UI有月區間，以操作行找出總行清單的情況)
	 * @param params
	 * @return
	 */
	public String getByOpbkId_MON(Map<String, String> params){
		String result;
		String paramName;
		String paramValue;
		String s_bizdate;
		String ss_bizdate;
		String e_bizdate;
		List<BANK_OPBK> list  = null;
		Map rtnMap = new HashMap();
		
		String OPBK_ID = "";
		paramName = "OPBK_ID";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			OPBK_ID = paramValue;
		}
		String s_mon = "";
		paramName = "s_mon";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			s_mon = paramValue;
		}else{
			rtnMap.put("result", "FALSE");
			rtnMap.put("msg", "輸入年或月不可空白");
			result = JSONUtils.map2json(rtnMap);
			return result;
		}
		String e_mon = "";
		paramName = "e_mon";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			e_mon = paramValue;
		}else{
			e_mon = s_mon;
		}
		s_bizdate = s_mon+"01";
		ss_bizdate = e_mon+"01";
		rtnMap = eachsysstatustab_bo.getWk_date_bo().validateYear_Month(s_mon.substring(0,4), s_mon.substring(4,s_mon.length()));
		if(rtnMap.get("result").equals("FALSE")){
			result = JSONUtils.map2json(rtnMap);
			return result;
		}
		rtnMap = eachsysstatustab_bo.getWk_date_bo().validateYear_Month(e_mon.substring(0,4), e_mon.substring(4,e_mon.length()));
		if(rtnMap.get("result").equals("FALSE")){
			result = JSONUtils.map2json(rtnMap);
			return result;
		}
		s_bizdate = DateTimeUtils.convertDate(1, s_bizdate, "yyyyMMdd", "yyyy/MM/dd");
		ss_bizdate = DateTimeUtils.convertDate(1, ss_bizdate,"yyyyMMdd", "yyyy/MM/dd");
		s_bizdate = eachsysstatustab_bo.getWk_date_bo().getFirst_Bizdate_of_Month(s_bizdate);
		e_bizdate = eachsysstatustab_bo.getWk_date_bo().getLast_Bizdate_of_Month(ss_bizdate);
		s_bizdate = DateTimeUtils.convertDate(1, s_bizdate, "yyyyMMdd", "yyyyMMdd");
		e_bizdate = DateTimeUtils.convertDate(1, e_bizdate,"yyyyMMdd", "yyyyMMdd");
		System.out.println("s_bizdate>>"+s_bizdate+",e_bizdate>>"+e_bizdate);
		params.put("s_bizdate", s_bizdate);
		params.put("e_bizdate", e_bizdate);
		
		result  = getByOpbkId(params);
		return result;
	}
	/**
	 * 頁面查詢用(適用UI有日期區間，以操作行找出總行清單的情況)
	 * @param params
	 * @return
	 */
	public String getByOpbkId(Map<String, String> params){
		String result;
		String paramName;
		String paramValue;
		List<BANK_OPBK> list  = null;
		String OPBK_ID = "";
		paramName = "OPBK_ID";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			OPBK_ID = paramValue;
		}
//		20150818 add by hugo 因應操作行變更機制
		
		
		String s_bizdate = "";
		paramName = "s_bizdate";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			s_bizdate = paramValue;
		}
		String e_bizdate = "";
		paramName = "e_bizdate";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			e_bizdate = paramValue;
		}else{
//			hugo note 加入此判斷的原因:因頁面銀行與票交共用，查詢日期銀行只有單日，票交有區間，故如UI為銀行端e_bizdate再UI端要設定為""
//			server端直接指定e_bizdate = s_bizdate;
			e_bizdate = s_bizdate;
		}
		Map rtnMap = new HashMap();
//		20150818 edit by hugo 因應操作行變更機制
//		List<BANK_GROUP> list = search(OPBK_ID, "");
		rtnMap = zDateHandler.verify_BizDate(DateTimeUtils.convertDate(1, s_bizdate, "yyyyMMdd", "yyyyMMdd") , DateTimeUtils.convertDate(1, e_bizdate, "yyyyMMdd", "yyyyMMdd"));
		if(rtnMap.get("result").equals("FALSE")){
			result = JSONUtils.map2json(rtnMap);
			return result;
		}
		list  = bank_opbk_bo.getBgbkList(OPBK_ID, s_bizdate, e_bizdate);
		if(list != null && list.size() > 0){
			rtnMap.put("result", "TRUE");
			rtnMap.put("msg", list);
		}else{
			rtnMap.put("result", "FALSE");
			rtnMap.put("msg", "無總行資料");
		}
		result = JSONUtils.map2json(rtnMap);
		System.out.println(result);
		return result;
	}
	
	
	
	/**
	 * 頁面查詢用(適用UI為單一日期，以操作行找出總行清單的情況)
	 * @param params
	 * @return
	 */
	public String getByOpbkId_Single_Date(Map<String, String> params){
		String result;
		String paramName;
		String paramValue;
		List<BANK_OPBK> list  = null;
		Map rtnMap = new HashMap();
		String OPBK_ID = "";
		paramName = "OPBK_ID";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			OPBK_ID = paramValue;
		}
		String s_bizdate = "";
		paramName = "s_bizdate";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			s_bizdate = paramValue;
		}else{
			rtnMap.put("result", "FALSE");
			rtnMap.put("msg", "營業日期不可空白");
			result = JSONUtils.map2json(rtnMap);
			return result;
		}
		rtnMap = zDateHandler.verify_BizDate(DateTimeUtils.convertDate(1, s_bizdate, "yyyyMMdd", "yyyyMMdd"));
		if(rtnMap.get("result").equals("FALSE")){
			result = JSONUtils.map2json(rtnMap);
			return result;
		}
		list  = bank_opbk_bo.getCurBgbkList(OPBK_ID, s_bizdate);
			
		if(list != null && list.size() > 0){
			rtnMap.put("result", "TRUE");
			rtnMap.put("msg", list);
		}else{
			rtnMap.put("result", "FALSE");
			rtnMap.put("msg", "無總行資料");
		}
		result = JSONUtils.map2json(rtnMap);
		System.out.println(result);
		return result;
	}
	
	
	/**
	 * 頁面查詢用(適用UI有日期區間，且同時有因操作行及清算行找出總行清單的情況)
	 * @param params
	 * @return
	 */
	public String getByOpbk_Ctbk(Map<String, String> params){
		String result;
		String paramName;
		String paramValue;
		String OPBK_ID = "";
		List list = null;
		paramName = "OPBK_ID";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			OPBK_ID = paramValue;
		}
		String CTBK_ID = "";
		paramName = "CTBK_ID";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			CTBK_ID = paramValue;
		}
		String s_bizdate = "";
		paramName = "s_bizdate";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			s_bizdate = paramValue;
		}
		String e_bizdate = "";
		paramName = "e_bizdate";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			e_bizdate = paramValue;
		}
		String type = "";
		paramName = "type";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			type = paramValue;
		}
		
		Map rtnMap = new HashMap();
		if(type.equals("CT")){
			list  = bank_ctbk_bo.getBgbkList(CTBK_ID, s_bizdate, e_bizdate);
		}else{
			list  = bank_opbk_bo.getBgbkList(OPBK_ID, s_bizdate, e_bizdate);
		}
		
		if(list != null && list.size() > 0){
			rtnMap.put("result", "TRUE");
			rtnMap.put("msg", list);
		}else{
			rtnMap.put("result", "FALSE");
			rtnMap.put("msg", "無總行資料");
		}
		result = JSONUtils.map2json(rtnMap);
		System.out.println("getByOpbk_Ctbk.result>>"+result);
		return result;
	}
	
	/**
	 * 依據操作行及營業日來查詢相對應的總行清單
	 * @param opbk_id
	 * @param s_bizdate
	 * @return
	 */
	public List<LabelValueBean> getCurBgbkListByOP(String opbk_id , String s_bizdate){
		String OPBK_ID = opbk_id;
		Map rtnMap = new HashMap();
		List<LabelValueBean> beanList = new LinkedList<LabelValueBean>();
		LabelValueBean bean = null;
//		List<BANK_GROUP> list = search(OPBK_ID, "");
		List<BANK_OPBK> list = bank_opbk_bo.getCurBgbkList(opbk_id, s_bizdate);
		if(list != null && list.size() > 0){
			for(BANK_OPBK po : list){
				bean = new LabelValueBean(po.getBGBK_ID() + " - " + po.getBGBK_NAME(), po.getBGBK_ID());
				beanList.add(bean);
			}
			System.out.println("beanList>>"+beanList);
		}
		return beanList;
	}
	
	public String getByBgbkId(Map<String, String> params){
		String result;
		String paramName;
		String paramValue;
		
		String BGBK_ID = "";
		paramName = "BGBK_ID";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			BGBK_ID = paramValue;
		}
		
		Map rtnMap = new HashMap();
		BANK_GROUP po = bank_group_Dao.get(BGBK_ID);
		try {
			rtnMap = BeanUtils.describe(po);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		result = JSONUtils.map2json(rtnMap);
		System.out.println(result);
		return result;
	}
	
	//20140107 HUANGPU 業務邏輯檢核
	//總行資料新增-欄位驗證
	public String validate(Map<String, String> params){
		Map<String, String> rtnMap = new HashMap<String, String>();
		String paramName = "";
		String paramValue = "";
		
		String bgbkId = "";
		paramName = "BGBK_ID";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			bgbkId = paramValue.trim();
		}
		
		String ctbkId = "";
		paramName = "CTBK_ID";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			ctbkId = paramValue.trim();
		}
		
		String ctbkAcct = "";
		paramName = "CTBK_ACCT";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			ctbkAcct = paramValue.trim();
		}
		
		String opbkId = "";
		paramName = "OPBK_ID";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			opbkId = paramValue.trim();
		}
		
		String tchId = "";
		paramName = "TCH_ID";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			tchId = paramValue.trim();
		}
		
		String sndFeeBrbkId = "";
		paramName = "SND_FEE_BRBK_ID";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			sndFeeBrbkId = paramValue.trim();
		}
		
		String outFeeBrbkId = "";
		paramName = "OUT_FEE_BRBK_ID";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			outFeeBrbkId = paramValue.trim();
		}
		
		String inFeeBrbkId = "";
		paramName = "IN_FEE_BRBK_ID";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			inFeeBrbkId = paramValue.trim();
		}
		String woFeeBrbkId = "";
		paramName = "WO_FEE_BRBK_ID";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			inFeeBrbkId = paramValue.trim();
		}
		String type = "";
		paramName = "type";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			type = paramValue.trim();
		}
		
		rtnMap.put("result", "FALSE");
		
		//若清算行代號不等於本筆資料的總行代號時，央行帳號須為清算行的央行帳號
		if(!ctbkId.equals(bgbkId)){
			List<BANK_GROUP> ctbkIdList = search(ctbkId);
			if(ctbkIdList == null || (ctbkIdList != null && ctbkIdList.size() > 1)){
				rtnMap.put("msg", "請輸入正確的清算行代號");
				return JSONUtils.map2json(rtnMap);
			}else{
				BANK_GROUP ctbk = ctbkIdList.get(0);
				if(!ctbk.getCTBK_ACCT().equals(ctbkAcct)){
					rtnMap.put("msg", "央行帳號須為清算行的央行帳號(" + ctbk.getCTBK_ACCT() + ")");
					return JSONUtils.map2json(rtnMap);
				}
			}
			
			//清算行代號須存在或是為本筆資料的總行代號
			ctbkIdList = bank_group_Dao.getctbkIdList();
			boolean isExists = false;
			for(int i = 0; i < ctbkIdList.size(); i++){
				if(ctbkIdList.get(i).getBGBK_ID().equals(ctbkId)){ isExists = true; }
			}
			if(!isExists){
				rtnMap.put("msg", "不存在的清算行代號!");
				return JSONUtils.map2json(rtnMap);
			}
		}
		
		//操作行代號須存在或是為本筆資料的總行代號
		if(StrUtils.isNotEmpty(opbkId)){
			if(!opbkId.equals(bgbkId)){
				List<BANK_GROUP> opbkIdList = bank_group_Dao.getBgbkIdList_2();
				boolean isExists = false;
				for(int i = 0; i < opbkIdList.size(); i++){
					if(opbkIdList.get(i).getBGBK_ID().equals(opbkId)){ isExists = true; }
				}
				if(!isExists){
					rtnMap.put("msg", "不存在的操作行代號!");
					return JSONUtils.map2json(rtnMap);
				}
			}
		}
		
		//問題反映單-UAT-20150120-08 交換所代號必須存在的交換所代號
		boolean isExist = false;
		List<BANK_GROUP> tchIdList = bank_group_Dao.getTchIdList();
		for(BANK_GROUP po : tchIdList){
			if(tchId.equals(po.getTCH_ID())){
				isExist = true;
			}
		}
		if(!isExist){
			rtnMap.put("msg", "不存在的交換所代號!");
			return JSONUtils.map2json(rtnMap);
		}
		
		//問題反映單-UAT-20150202-01 發動付費分行及收費分行僅可設為該總行底下之分行
		BANK_BRANCH branchPo = null;
//		20150626 add by hugo
		if(!type.equals("add")){
			
		}
		System.out.println("sndFeeBrbkId>>"+sndFeeBrbkId);
		if(StrUtils.isNotEmpty(sndFeeBrbkId)){
			branchPo = bank_branch_Dao.get(new BANK_BRANCH_PK(bgbkId, sndFeeBrbkId));
			if(branchPo == null){
				rtnMap.put("msg", "發動行手續費分行代號必須為 "+bgbkId+" 之分行");
				return JSONUtils.map2json(rtnMap);
			}
			branchPo = null;
		}
		if(StrUtils.isNotEmpty(outFeeBrbkId)){
			branchPo = bank_branch_Dao.get(new BANK_BRANCH_PK(bgbkId, outFeeBrbkId));
			if(branchPo == null){
				rtnMap.put("msg", "扣款行手續費分行代號必須為 "+bgbkId+" 之分行");
				return JSONUtils.map2json(rtnMap);
			}
			branchPo = null;
		}
		if(StrUtils.isNotEmpty(inFeeBrbkId)){
			branchPo = bank_branch_Dao.get(new BANK_BRANCH_PK(bgbkId, inFeeBrbkId));
			if(branchPo == null){
				rtnMap.put("msg", "入帳行手續費分行代號必須為 "+bgbkId+" 之分行");
				return JSONUtils.map2json(rtnMap);
			}
			branchPo = null;
		}
		if(StrUtils.isNotEmpty(woFeeBrbkId)){
			branchPo = bank_branch_Dao.get(new BANK_BRANCH_PK(bgbkId, inFeeBrbkId));
			if(branchPo == null){
				rtnMap.put("msg", "銷帳行手續費分行代號必須為 "+bgbkId+" 之分行");
				return JSONUtils.map2json(rtnMap);
			}
			branchPo = null;
		}
		
		rtnMap.put("result", "TRUE");
		rtnMap.put("msg", "");
		return JSONUtils.map2json(rtnMap);
	}
	
	public String getHR_UPLOAD_MAX_FILE(Map<String, String> params){
		String hrUploadMaxFile = "0";
		String bgbkId = StrUtils.isEmpty(params.get("BGBK_ID"))?"":params.get("BGBK_ID");
		if(StrUtils.isNotEmpty(bgbkId)){
			BANK_GROUP bg = getOneFromMaster(bgbkId);
			if(bg != null){
				hrUploadMaxFile = bg.getHR_UPLOAD_MAX_FILE();
			}
		}
		Map<String, String> rtnMap = new HashMap<String, String>();
		rtnMap.put("HR_UPLOAD_MAX_FILE", hrUploadMaxFile);
		return JSONUtils.map2json(rtnMap);
	}
	
	public BANK_GROUP_Dao getBank_group_Dao() {
		return bank_group_Dao;
	}

	public void setBank_group_Dao(BANK_GROUP_Dao bank_group_Dao) {
		this.bank_group_Dao = bank_group_Dao;
	}

	public BANK_GROUP_BUSINESS_Dao getBank_group_business_Dao() {
		return bank_group_business_Dao;
	}

	public void setBank_group_business_Dao(
			BANK_GROUP_BUSINESS_Dao bank_group_business_Dao) {
		this.bank_group_business_Dao = bank_group_business_Dao;
	}

	public BUSINESS_TYPE_BO getBusiness_type_bo() {
		return business_type_bo;
	}

	public void setBusiness_type_bo(BUSINESS_TYPE_BO business_type_bo) {
		this.business_type_bo = business_type_bo;
	}

	public BANK_BRANCH_Dao getBank_branch_Dao() {
		return bank_branch_Dao;
	}

	public void setBank_branch_Dao(BANK_BRANCH_Dao bank_branch_Dao) {
		this.bank_branch_Dao = bank_branch_Dao;
	}
	public CR_LINE_Dao getCr_line_Dao() {
		return cr_line_Dao;
	}
	public void setCr_line_Dao(CR_LINE_Dao cr_line_Dao) {
		this.cr_line_Dao = cr_line_Dao;
	}
	public BANKSYSSTATUSTAB_Dao getBanksysstatus_Dao() {
		return banksysstatus_Dao;
	}
	public void setBanksysstatus_Dao(BANKSYSSTATUSTAB_Dao banksysstatus_Dao) {
		this.banksysstatus_Dao = banksysstatus_Dao;
	}
	public BANKAPSTATUSTAB_Dao getBankapstatus_Dao() {
		return bankapstatus_Dao;
	}
	public void setBankapstatus_Dao(BANKAPSTATUSTAB_Dao bankapstatus_Dao) {
		this.bankapstatus_Dao = bankapstatus_Dao;
	}

	public EACH_USERLOG_BO getUserlog_bo() {
		return userlog_bo;
	}

	public void setUserlog_bo(EACH_USERLOG_BO userlog_bo) {
		this.userlog_bo = userlog_bo;
	}

	public BANK_OPBK_BO getBank_opbk_bo() {
		return bank_opbk_bo;
	}

	public void setBank_opbk_bo(BANK_OPBK_BO bank_opbk_bo) {
		this.bank_opbk_bo = bank_opbk_bo;
	}

	public BANK_CTBK_BO getBank_ctbk_bo() {
		return bank_ctbk_bo;
	}

	public void setBank_ctbk_bo(BANK_CTBK_BO bank_ctbk_bo) {
		this.bank_ctbk_bo = bank_ctbk_bo;
	}

	public EACHSYSSTATUSTAB_BO getEachsysstatustab_bo() {
		return eachsysstatustab_bo;
	}

	public void setEachsysstatustab_bo(EACHSYSSTATUSTAB_BO eachsysstatustab_bo) {
		this.eachsysstatustab_bo = eachsysstatustab_bo;
	}

	
	
}
