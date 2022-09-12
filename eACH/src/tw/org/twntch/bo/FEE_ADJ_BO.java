package tw.org.twntch.bo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tw.org.twntch.db.dao.hibernate.BANK_BRANCH_Dao;
import tw.org.twntch.db.dao.hibernate.BANK_GROUP_Dao;
import tw.org.twntch.db.dao.hibernate.FEE_ADJ_Dao;
import tw.org.twntch.db.dao.hibernate.Page;
import tw.org.twntch.po.BANK_BRANCH;
import tw.org.twntch.po.BANK_BRANCH_PK;
import tw.org.twntch.po.BANK_GROUP;
import tw.org.twntch.po.BRBK_FEE_ADJ;
import tw.org.twntch.po.BRBK_FEE_ADJ_PK;
import tw.org.twntch.util.BeanUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.zDateHandler;

public class FEE_ADJ_BO {
	private FEE_ADJ_Dao fee_adj_Dao ;
	private BANK_GROUP_Dao bank_group_Dao;
	private BANK_BRANCH_Dao bank_branch_Dao;
	
	/**
	 * 依照調帳角色挑選總行清單
	 * @return
	 */
	public String getBgbkIdList(Map<String, String> param){
		Map rtnMap = new HashMap();
		List<BANK_GROUP> list = null;
		List<Map> listRet = new ArrayList<Map>();
		String role = (StrUtils.isEmpty(param.get("ROLE"))?"":param.get("ROLE").trim());
		if(role.equals("TCH")){
			list = bank_group_Dao.getBgbkIdList_ACH();
		}else if(role.equals("BANK")){
			list = bank_group_Dao.getBgbkIdList_Not_5_And_6();
		}
		if(list != null){
			Map bean = null;
			for(BANK_GROUP po : list){
				bean = new HashMap();
				bean.put("label", po.getBGBK_ID() + " - " + po.getBGBK_NAME());
				bean.put("value", po.getBGBK_ID());
				listRet.add(bean);
			}
			rtnMap.put("result", "TRUE");
			rtnMap.put("msg", listRet);
		}else{
			rtnMap.put("result", "FALSE");
			rtnMap.put("msg", "");
		}
		
		return JSONUtils.map2json(rtnMap);
	}
	
	//刪除
	public Map<String, String> delete(String yyyymm, String brbkId){
		Map<String, String> map = null;
		Map<String, String> pkmap = new HashMap<String,String>();//e
		BRBK_FEE_ADJ po = null ; //e
		BRBK_FEE_ADJ_PK pk = null;
		try {
			map = new HashMap<String, String>();
			pkmap.put("YYYYMM", yyyymm);
			pkmap.put("BRBK_ID", brbkId);
			pk = new BRBK_FEE_ADJ_PK(yyyymm, brbkId);
			po = fee_adj_Dao.get(pk);
			if(po == null){
				map = fee_adj_Dao.removeFail(po, pkmap, "刪除失敗，查無資料", 1);
				return map;
			}
			if(fee_adj_Dao.deleteData(po)){
				fee_adj_Dao.writeLog("D", po, null, "刪除成功");
				map.put("result", "TRUE");
				map.put("msg", "刪除成功");
				map.put("target", "search");
			}else{
				map = fee_adj_Dao.removeFail(po, pkmap, "刪除失敗，更新生效日錯誤", 2);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			map = fee_adj_Dao.removeFail(po, pkmap, "刪除失敗，系統異常", 2);
			return map;
		}
		return map;
	}
	
	//修改
	public Map<String,String> update(Map data){
		Map<String, String> rtnMap = new HashMap<String, String>();
		Map<String, String> oldMap = new HashMap<String, String>();
		Map<String, String> pkmap = new HashMap<String, String>(); //e
		BRBK_FEE_ADJ newPo = null;
		BRBK_FEE_ADJ oldPo = null ;
		BRBK_FEE_ADJ_PK pk = null;
		try {
			pk = new BRBK_FEE_ADJ_PK((String)data.get("YYYYMM"), (String)data.get("BRBK_ID"));
			pkmap = BeanUtils.describe(pk);
			oldPo = fee_adj_Dao.get(pk);
			if(oldPo == null){
				newPo = new BRBK_FEE_ADJ();
				BeanUtils.populate(newPo, data);//e
				rtnMap = fee_adj_Dao.updateFail(newPo, null, pkmap, "修改-儲存失敗，查無資料", 1);
				return rtnMap;
			}
			oldMap = BeanUtils.describe(oldPo);
			newPo = oldPo;
			BeanUtils.populate(newPo, data);
			newPo.setACTIVE_DATE("");
			newPo.setUDATE(zDateHandler.getTheDateII());
			rtnMap.put("YYYYMM", pk.getYYYYMM());
			rtnMap.put("BRBK_ID", pk.getBRBK_ID());
			if(fee_adj_Dao.saveData(newPo)){
				fee_adj_Dao.writeLog("B", oldMap, BeanUtils.describe(newPo), pkmap, "修改成功");
				rtnMap.put("result", "TRUE");
				rtnMap.put("msg", "儲存成功");
				rtnMap.put("target", "search");
			}else{
				rtnMap = fee_adj_Dao.updateFail(newPo, oldMap, pkmap, "修改-儲存失敗，更新生效日錯誤", 2);
			}
		}catch (Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();
			rtnMap = fee_adj_Dao.updateFail(newPo, oldMap, pkmap, "修改-儲存失敗，系統異常", 2);
			return rtnMap;
		}
		return rtnMap;
	}	
	
	//新增
	public Map<String,String> save(Map data){
		Map<String, String> map = new HashMap<String, String>();
		Map<String, String> pkmap = new HashMap<String, String>();//e
		BRBK_FEE_ADJ po = null;
		BRBK_FEE_ADJ_PK pk = new BRBK_FEE_ADJ_PK();
		try {
			BeanUtils.populate(pk, data);
			pkmap.put("YYYYMM", pk.getYYYYMM()) ;//e
			pkmap.put("BRBK_ID", pk.getBRBK_ID()) ;//e
			po = fee_adj_Dao.get(pk);
			if(po != null){
				map = fee_adj_Dao.saveFail(po, pkmap, "儲存失敗，資料重複" ,1);//e
				return map;
			}
			po = new BRBK_FEE_ADJ();
			BeanUtils.populate(po, data);
			po.setSNO(getSNO(pk.getYYYYMM()));
			po.setId(pk);
			
			String brbkName = "";
			BANK_BRANCH branch = bank_branch_Dao.get(new BANK_BRANCH_PK((String)data.get("BGBK_ID"),pk.getBRBK_ID()));
			if(branch != null){
				brbkName = branch.getBRBK_NAME();
			}else{
				BANK_GROUP group = bank_group_Dao.get((String)data.get("BGBK_ID"));
				if(group != null){
					brbkName = group.getBGBK_NAME();
				}
			}
			po.setBRBK_NAME(brbkName);
			po.setACTIVE_DATE("");
			po.setCDATE(zDateHandler.getTheDateII());
			
			if(fee_adj_Dao.saveData(po)){
				fee_adj_Dao.writeLog("A", null, po, "新增成功");
				map.put("result", "TRUE");
				map.put("msg", "儲存成功");
				map.put("target", "search");
			}else{
				map = fee_adj_Dao.saveFail(po, pkmap, "儲存失敗，更新生效日錯誤" ,2);//e
			}
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("EACH_USER_BO.save.Exception>>"+e);//e
			map = fee_adj_Dao.saveFail(po, pkmap, "儲存失敗，系統異常" ,2);//e
			return map;
		}
		return map;
	}
	
	public Map<String,String> publish(String yyyymm){
		Map<String, String> rtnMap = new HashMap<String, String>();
		if(StrUtils.isNotEmpty(yyyymm)){
			BigDecimal total = fee_adj_Dao.getCheckSum(yyyymm);
			System.out.println("TOTAL >> " + total);
			if(total == null){
				rtnMap.put("result", "FALSE");
				rtnMap.put("msg", "發布失敗，無調帳資料!");
			}else if(total.compareTo(new BigDecimal("0")) == 0){
				if(fee_adj_Dao.publish(yyyymm)){
					rtnMap.put("result", "TRUE");
					rtnMap.put("msg", "發布成功!");
				}else{
					rtnMap.put("result", "FALSE");
					rtnMap.put("msg", "發布失敗，啟用錯誤!");
				}
			}else{
				rtnMap.put("result", "FALSE");
				rtnMap.put("msg", "發布失敗，調帳金額錯誤!調帳後金額加總須為 0.00 ，目前金額加總為:"+total);
			}
		}else{
			rtnMap.put("result", "FALSE");
			rtnMap.put("msg", "發布失敗，未指定調整年月!");
		}
		return rtnMap;
	}
	
	public BRBK_FEE_ADJ searchByPK(String yyyymm, String brbkId){
		return fee_adj_Dao.get(new BRBK_FEE_ADJ_PK(yyyymm, brbkId));
	}
//	
//	public String search_toJson(Map<String, String> param){
//		String json = null;
//		
//		int pageNo = StrUtils.isEmpty(param.get("page")) ?0:Integer.valueOf(param.get("page"));
//		int pageSize = StrUtils.isEmpty(param.get("rows")) ?Integer.valueOf(Arguments.getStringArg("PAGE.SIZE")):Integer.valueOf(param.get("rows"));
//		String sidx = StrUtils.isEmpty(param.get("sidx")) ?"":param.get("sidx").trim();
//		String sord = StrUtils.isEmpty(param.get("sord")) ?"":param.get("sord").trim();
//		
//		String yyyymm = "";
//		if(StrUtils.isNotEmpty(param.get("YYYYMM"))){
//			yyyymm = param.get("YYYYMM").trim();
//			json = JSONUtils.map2json(searchByYyyymm(yyyymm, pageNo, pageSize, sidx, sord));
//		}
//		
//		String sno = "";
//		if(StrUtils.isNotEmpty(param.get("SNO"))){
//			sno = param.get("SNO").trim();
//			json = JSONUtils.map2json(searchBySno(sno, pageNo, pageSize, sidx, sord));
//		}
//		
//		System.out.println("json >> " + json);
//		return json;
//	}
	public String pageSearch(Map<String, String> param){
		String json = null;
		
		int pageNo = StrUtils.isEmpty(param.get("page")) ?0:Integer.valueOf(param.get("page"));
		int pageSize = StrUtils.isEmpty(param.get("rows")) ?Integer.valueOf(Arguments.getStringArg("PAGE.SIZE")):Integer.valueOf(param.get("rows"));
		String sidx = StrUtils.isEmpty(param.get("sidx")) ?"":param.get("sidx").trim();
		String sord = StrUtils.isEmpty(param.get("sord")) ?"":param.get("sord").trim();
		
		String yyyymm = "";
		if(StrUtils.isNotEmpty(param.get("YYYYMM"))){
			yyyymm = param.get("YYYYMM").trim();
			json = JSONUtils.map2json(searchByYyyymm(yyyymm, pageNo, pageSize, sidx, sord));
		}
		
		String sno = "";
		if(StrUtils.isNotEmpty(param.get("SNO"))){
			sno = param.get("SNO").trim();
			json = JSONUtils.map2json(searchBySno(sno, pageNo, pageSize, sidx, sord));
		}
		
		System.out.println("json >> " + json);
		return json;
	}
	
	public Integer getSNO(String yyyymm){
		return fee_adj_Dao.getSNO(yyyymm).getSNO();
	}
	
	public Map searchByYyyymm(String yyyymm, int pageNo, int pageSize, String sidx, String sord){
		Map rtnMap = new HashMap();
		Page page = null;
		page = fee_adj_Dao.getByYyyymm(yyyymm, pageNo, pageSize, sidx, sord);
		if(page == null){
			rtnMap.put("total", "0");
			rtnMap.put("page", "0");
			rtnMap.put("records", "0");
			rtnMap.put("rows", new ArrayList());
		}else{
			rtnMap.put("total", page.getTotalPageCount());
			rtnMap.put("page", String.valueOf(page.getCurrentPageNo()));
			rtnMap.put("records", page.getTotalCount());
			rtnMap.put("rows", (List<BRBK_FEE_ADJ>) page.getResult());
		}
		return rtnMap;
	}
	
	public Map searchBySno(String sno, int pageNo, int pageSize, String sidx, String sord){
		Map rtnMap = new HashMap();
		Page page = null;
		page = fee_adj_Dao.getBySno(sno, pageNo, pageSize, sidx, sord);
		if(page == null){
			rtnMap.put("total", "0");
			rtnMap.put("page", "0");
			rtnMap.put("records", "0");
			rtnMap.put("rows", new ArrayList());
		}else{
			rtnMap.put("total", page.getTotalPageCount());
			rtnMap.put("page", String.valueOf(page.getCurrentPageNo()));
			rtnMap.put("records", page.getTotalCount());
			rtnMap.put("rows", (List<BRBK_FEE_ADJ>) page.getResult());
		}
		return rtnMap;
	}

	public FEE_ADJ_Dao getFee_adj_Dao() {
		return fee_adj_Dao;
	}

	public void setFee_adj_Dao(FEE_ADJ_Dao fee_adj_Dao) {
		this.fee_adj_Dao = fee_adj_Dao;
	}
	
	public BANK_GROUP_Dao getBank_group_Dao() {
		return bank_group_Dao;
	}

	public void setBank_group_Dao(BANK_GROUP_Dao bank_group_Dao) {
		this.bank_group_Dao = bank_group_Dao;
	}

	public BANK_BRANCH_Dao getBank_branch_Dao() {
		return bank_branch_Dao;
	}

	public void setBank_branch_Dao(BANK_BRANCH_Dao bank_branch_Dao) {
		this.bank_branch_Dao = bank_branch_Dao;
	}
	
}
