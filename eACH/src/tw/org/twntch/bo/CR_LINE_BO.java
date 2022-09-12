package tw.org.twntch.bo;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.util.LabelValueBean;












import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import tw.org.twntch.db.dao.hibernate.BANK_GROUP_Dao;
import tw.org.twntch.db.dao.hibernate.CR_LINE_Dao;
import tw.org.twntch.po.CR_LINE;
import tw.org.twntch.po.CR_LINE_LOG;
import tw.org.twntch.po.EACH_USERLOG;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.WebServletUtils;
import tw.org.twntch.util.zDateHandler;

public class CR_LINE_BO {

	private CR_LINE_Dao cr_line_Dao ;
	private BANK_GROUP_Dao bank_group_Dao ;
	private EACH_USERLOG_BO userlog_bo;
	/**
	 * 取得已有額度檔的總行清單
	 * @return
	 */
	public List<LabelValueBean> getExistingBgbkIdList(){
		List<Map<String, String>> list = cr_line_Dao.getExistingBgbkIdList();
		List<LabelValueBean> beanList = new LinkedList<LabelValueBean>();
		LabelValueBean bean = null;
		for(Map<String, String> m : list){
			bean = new LabelValueBean(m.get("BGBK_ID") + " - " + m.get("BGBK_NAME"), m.get("BGBK_ID"));
			beanList.add(bean);
		}
		System.out.println("beanList>>"+beanList);
		return beanList;
	}

	public Map<String,String> update(String id  ,Map formMap){
		Map<String, String> map = null;
		CR_LINE po = null ;
		CR_LINE_LOG logpo = null ;
		Map<String, String> pkmap = new HashMap<String, String>();//e
		Map<String, String> oldmap = new HashMap<String, String>();//e
		try {
			map = new HashMap<String, String>();
			po = cr_line_Dao.get(id);
			pkmap.put("BANK_ID", id);
			if(po == null ){
//				map.put("result", "FALSE");
//				map.put("msg", "更新失敗，查無資料");
//				map.put("target", "edit_p");
				BeanUtils.populate(po, formMap);
				map = cr_line_Dao.updateFail(po, oldmap, pkmap, "儲存失敗，查無資料", 1);
				return map;
			}
//			logpo = new CR_LINE_LOG();
			oldmap = BeanUtils.describe(po);
			BeanUtils.populate(po, formMap);
//			BeanUtils.populate(logpo, formMap);
			po.setUDATE(zDateHandler.getTheDateII());
//			logpo.setCDATE(zDateHandler.getTheDateII());
//			logpo.setCR_LINE_UPDATE_DATE(zDateHandler.getTWDate()+zDateHandler.getTheTime());
//			logpo.setNEW_BASIC_CR_LINE(po.getBASIC_CR_LINE());
//			logpo.setNEW_REST_CR_LINE(po.getREST_CR_LINE());
//			logpo.setSEQ_ID(null);
//			cr_line_Dao.save(po);
			cr_line_Dao.saveII(po, oldmap, pkmap);
			map.put("result", "TRUE");
			map.put("msg", "儲存成功");
			map.put("target", "search");
//			if(cr_line_Dao.saveData(po, logpo)){
//				map.put("result", "TRUE");
//				map.put("msg", "更新成功");
//				map.put("target", "search");
//			}else{
//				map.put("result", "FALSE");
//				map.put("msg", "更新失敗");
//				map.put("target", "edit_p");
//			}
		}catch (Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();
//			map.put("result", "ERROR");
//			map.put("msg", "更新失敗，系統異常:"+e);
//			map.put("target", "edit_p");
			map = cr_line_Dao.updateFail(po, oldmap, pkmap, "儲存失敗，系統異常", 2);
			return map;
		}
		return map;
	}

public Map<String,String> delete(String id ){
	Map<String, String> map = null;
	Map<String, String> pkmap = new HashMap<String, String>();//e
	CR_LINE po = null ;
	try {
		map = new HashMap<String, String>();
		List list = bank_group_Dao.find(" FROM tw.org.twntch.po.BANK_GROUP WHERE CTBK_ID = ? ", id);
		if(list != null && list.size() != 0){
			map.put("result", "FALSE");
			map.put("msg", "該銀行是清算行，不可刪除");
			map.put("target", "edit_p");
			return map;
		}
		po = cr_line_Dao.get(id);
		pkmap.put("BANK_ID", id);
		if(po == null ){
//			map.put("result", "FALSE");
//			map.put("msg", "刪除失敗，查無資料");
//			map.put("target", "edit_p");
			map = cr_line_Dao.removeFail(po, pkmap, "刪除失敗，查無資料", 1);
			return map;
		}
//		cr_line_Dao.remove(po);
		cr_line_Dao.removeII(po, pkmap);;
		map.put("result", "TRUE");
		map.put("msg", "刪除成功");
		map.put("target", "search");
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
//		map.put("result", "ERROR");
//		map.put("msg", "刪除失敗，系統異常:"+e);
//		map.put("target", "edit_p");
		map = cr_line_Dao.removeFail(po, pkmap, "刪除失敗，系統異常", 2);
		return map;
	}
	return map;
}
	
	
	public Map<String,String> save(String id ,Map formMap){
		Map<String, String> map = null;
		CR_LINE po = null;
		Map<String, String> pkmap = new HashMap<String, String>();//e
		try {
			map = new HashMap<String, String>();
			po  = cr_line_Dao.get(id);
			pkmap.put("BANK_ID", id);
			if(po != null){
//				map.put("result", "FALSE");
//				map.put("msg", "儲存失敗，已有資料");
//				map.put("target", "add_p");
				map = cr_line_Dao.saveFail(po,pkmap, "儲存失敗，資料重複" ,1);//e
				return map;
			}
			po = new CR_LINE();
			BeanUtils.populate(po, formMap);
			po.setCDATE(zDateHandler.getTheDateII());
			cr_line_Dao.save(po);
			map.put("result", "TRUE");
			map.put("msg", "儲存成功");
			map.put("target", "search");
//			if(cr_line_Dao.saveData(po, logpo)){
//				map.put("result", "TRUE");
//				map.put("msg", "儲存成功");
//				map.put("target", "search");
//			}else{
//				map.put("result", "TRUE");
//				map.put("msg", "儲存失敗");
//				map.put("target", "add_p");
//			}
//			
		}catch (Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();
//			map.put("result", "ERROR");
//			map.put("msg", "儲存失敗，系統異常:"+e);
//			map.put("target", "add_p");
			map = cr_line_Dao.saveFail(po,pkmap, "儲存失敗，系統異常" ,2);
			return map;
		}
		System.out.println("save.map"+map);
		return map;
	}
	
	
	public String search_toJson(Map<String, String> param){
		String json = "{}";
		String id =StrUtils.isNotEmpty(param.get("BANK_ID"))? param.get("BANK_ID"):"";
		String sord =StrUtils.isNotEmpty(param.get("sord"))? param.get("sord"):"";
		String sidx =StrUtils.isNotEmpty(param.get("sidx"))? param.get("sidx"):"";
		String orderSQL = StrUtils.isNotEmpty(sidx)? " ORDER BY "+sidx+" "+sord:"";
		List<CR_LINE> list = search(id , orderSQL);
		if(list != null && list.size() !=0){
			json = JSONUtils.toJson(list);
		}
		return json;
	}
//	UI重新計算剩餘額度時使用
	public String search_toJson4Over_CR(Map<String, String> param){
		String json = "{}";
		String id =StrUtils.isNotEmpty(param.get("BANK_ID"))? param.get("BANK_ID"):"";
		String sord =StrUtils.isNotEmpty(param.get("sord"))? param.get("sord"):"";
		String sidx =StrUtils.isNotEmpty(param.get("sidx"))? param.get("sidx"):"";
		String orderSQL = StrUtils.isNotEmpty(sidx)? " ORDER BY "+sidx+" "+sord:"";
		Gson gs = new Gson();
		List<Map> list = searchRetMap (id , orderSQL);
		if(list != null && list.size() !=0){
			json =  gs.toJson(searchRetMap (id , orderSQL));
		}
		return json;
	}
	public String update_Over_CR(Map<String, String> param){
		String result = "{}";
		boolean ret = false;
		String opt_type = "5";
		String afStr = "資料格式:{銀行代號:剩餘額度}\n";
		String bfStr = "資料格式:{銀行代號:剩餘額度}\n";
				
		Map<String,String> retMap = new HashMap<String,String>();
		List<String> bankList = new LinkedList<String>();
		Map<String,BigInteger> map = null;
		TreeMap<String,String> oldmap = null;
		TreeMap<String,String> newmap = null;
		Gson gs = null;
		try {
			String updata =StrUtils.isNotEmpty(param.get("updata"))? param.get("updata"):"";
			gs = new Gson();
			map = gs.fromJson(updata, Map.class);
			System.out.println("map>>>"+map);
			if(map !=null && map.size() !=0){
				for(String key :map.keySet()){
					bankList.add(key);
				}
				List<CR_LINE> data =  cr_line_Dao.findDataByIdList(bankList);
				System.out.println("data>>"+data);
				oldmap = new TreeMap<String,String>();
				newmap = new TreeMap<String,String>();
				for(CR_LINE cr_line : data){
					oldmap.put(cr_line.getBANK_ID(), cr_line.getREST_CR_LINE());
					System.out.println("zz>>"+map.get(cr_line.getBANK_ID()) );
					String a = String.valueOf(map.get(cr_line.getBANK_ID())) ;
					System.out.println("a>>>>"+a);
					cr_line.setREST_CR_LINE(a);
					cr_line_Dao.aop_save(cr_line);
					newmap.put(cr_line.getBANK_ID(), cr_line.getREST_CR_LINE());
				}
				retMap.put("result", "TRUE");
				retMap.put("msg", "更新成功");
			}else{
				retMap.put("result", "FALSE");
				retMap.put("msg", "剩餘額度無異常，不須更新");
			}
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			retMap.put("result", "FALSE");
			retMap.put("msg", "更新失敗，系統異常");
		}finally{
			result = gs.toJson(retMap);
//			TODO 寫LOG
			String uri = "cr_line.do";
			EACH_USERLOG userlog  = userlog_bo.getUSERLOG(opt_type, uri);
			userlog.setADEXCODE("更新成功");
			bfStr+= gs.toJson(oldmap);
			afStr+= gs.toJson(newmap);
			userlog.setBFCHCON(bfStr);
			userlog.setAFCHCON(afStr);
			userlog_bo.getUserLog_Dao().aop_save(userlog);
		}
		System.out.println(result);
		return result;
	}
	
	public List<CR_LINE> search(String id ){
		
		List<CR_LINE> list = null ;
		try {
			if(StrUtils.isNotEmpty(id)){
//				list = cr_line_Dao.find(" FROM tw.org.twntch.po.CR_LINE WHERE BANK_ID = ?",id);
				list = cr_line_Dao.getDataById(id);
			}else{
//				list = cr_line_Dao.getAll();
				list = cr_line_Dao.getAllData("");
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("list>>"+list);
		list = list.size() == 0 ? null : list;
		return list;
	}
	public List<CR_LINE> search(String id ,String orderSQL){
		
		List<CR_LINE> list = null ;
		try {
			if(StrUtils.isNotEmpty(id)){
				list = cr_line_Dao.getDataById(id);
			}else{
				list = cr_line_Dao.getAllData(orderSQL);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("list>>"+list);
		list = list.size() == 0 ? null : list;
		return list;
	}
	public List<Map> searchRetMap(String id ,String orderSQL){
		
		List<Map> list = null ;
		try {
			if(StrUtils.isNotEmpty(id)){
				list = cr_line_Dao.getDataByIdRetMap(id);
			}else{
				list = cr_line_Dao.getAllDataRetMap(orderSQL);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("list>>"+list);
		list = list.size() == 0 ? null : list;
		return list;
	}
	
	
//	--額度檔補正建議金額語法
//	WITH TEMP AS ( 
//	SELECT BIZDATE , CLEARINGPHASE ,BANKID 
//	, (SELECT CTBK_ID FROM BANK_GROUP WHERE BGBK_ID = (SELECT BGBK_ID FROM BANK_BRANCH WHERE BRBK_ID = BANKID) )AS CTBK_ID
//	, (COALESCE (SUM(RECVAMT+RVSRECVAMT) , 0) - COALESCE( SUM(PAYAMT+RVSPAYAMT) , 0)) CLDIFAMT 
//	FROM ONCLEARINGTAB WHERE BIZDATE ='20151201' AND CLEARINGPHASE = '01'   GROUP BY BANKID,CLEARINGPHASE,BIZDATE
//	)
//	,TEMP2 AS (
//	SELECT * FROM CR_LINE
//	)
//	,TEMP3 AS (
//	SELECT T.CTBK_ID ,COALESCE( T.CLDIFAMT,0) AS CLDIFAMT , (BASIC_CR_LINE + COALESCE( T.CLDIFAMT,0) ) REC_CR_LINE  ,T2.* FROM TEMP2 T2
//	LEFT JOIN TEMP T ON T.BANKID = T2.BANK_ID
//	)
//	SELECT * FROM TEMP3
//	ORDER BY BANK_ID 
	
	
	
	public String getOne(Map<String, String> param){
		String id =param.get("cleanbgbkId");
		String json = "";
		Map ret = new HashMap<>();
		CR_LINE po = null;
		try {
			if(StrUtils.isEmpty(id)){
				return null;
			}
			System.out.println("id>>"+id);
			po =  cr_line_Dao.get(id);
			if(po!=null){
				System.out.println("po.id>>"+po.getBANK_ID());
				ret.put("result", "TRUE");
//				ret.put("data", po);
				ret.put("data", BeanUtils.describe(po));
			}else{
				ret.put("result", "FALSE");
			}
			System.out.println("ret>>"+ret);
			json = JSONUtils.map2json(ret);
//			使用 JSONUtils 轉json 字串會發生
//	WARN  2015-01-22 19:06:18 net.sf.json.JSONObject  - Property 'transactionTimeout' has no read method. SKIPPED
//	net.sf.json.JSONException: java.lang.NoSuchMethodException: Property 'delegate' has no getter method
//			用轉成BeanUtils map 會長這樣
//			ret>>{result=TRUE, data={hibernateLazyInitializer=org.hibernate.proxy.pojo.javassist.JavassistLazyInitializer@abdd50, USER_ID=null, UDATE=2015-01-20, BANK_ID=0060000, class=class tw.org.twntch.po.CR_LINE_$$_javassist_10, REST_CR_LINE=500000000, BASIC_CR_LINE=500000000, CDATE=null, BANK_NAME=null, handler=org.hibernate.proxy.pojo.javassist.JavassistLazyInitializer@abdd50}}
			
//			使用 Gson轉json 字串會發生
//java.lang.UnsupportedOperationException: Attempted to serialize 
//java.lang.Class: org.hibernate.proxy.HibernateProxy. Forgot to register a type adapter?
//			json = new Gson().toJson(ret);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}
	
	public CR_LINE_Dao getCr_line_Dao() {
		return cr_line_Dao;
	}

	public void setCr_line_Dao(CR_LINE_Dao cr_line_Dao) {
		this.cr_line_Dao = cr_line_Dao;
	}

	public BANK_GROUP_Dao getBank_group_Dao() {
		return bank_group_Dao;
	}

	public void setBank_group_Dao(BANK_GROUP_Dao bank_group_Dao) {
		this.bank_group_Dao = bank_group_Dao;
	}

	public EACH_USERLOG_BO getUserlog_bo() {
		return userlog_bo;
	}

	public void setUserlog_bo(EACH_USERLOG_BO userlog_bo) {
		this.userlog_bo = userlog_bo;
	}
	
	
	
	
}
