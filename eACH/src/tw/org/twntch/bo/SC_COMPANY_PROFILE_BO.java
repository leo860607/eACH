package tw.org.twntch.bo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.struts.upload.FormFile;
import org.apache.struts.util.LabelValueBean;

import tw.org.twntch.db.dao.hibernate.EACH_USERLOG_Dao;
import tw.org.twntch.db.dao.hibernate.SC_COMPANY_PROFILE_Dao;
import tw.org.twntch.db.dao.hibernate.SC_COMPANY_PROFILE_HIS_Dao;
import tw.org.twntch.form.SC_Company_Profile_Form;
import tw.org.twntch.po.EACH_USERLOG;
import tw.org.twntch.po.FEE_CODE_NW;
import tw.org.twntch.po.SC_COMPANY_PROFILE;
import tw.org.twntch.po.SC_COMPANY_PROFILE_HIS;
import tw.org.twntch.po.SC_COMPANY_PROFILE_PK;
import tw.org.twntch.po.SYS_PARA;
import tw.org.twntch.po.TXN_CODE;
import tw.org.twntch.util.CodeUtils;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.SpringAppCtxHelper;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.zDateHandler;

public class SC_COMPANY_PROFILE_BO {
	private SC_COMPANY_PROFILE_Dao sc_com_Dao;
	private EACH_USERLOG_Dao userLog_Dao;
	private BANK_GROUP_BO bank_group_bo;
	private EACH_USERLOG_BO userlog_bo;
	private FEE_CODE_NW_BO fee_code_nw_bo;
	private FEE_CODE_BO fee_code_bo;
	private SC_COMPANY_PROFILE_HIS_Dao sc_com_his_Dao;
	private SYS_PARA_BO sys_para_bo;
	private EACHSYSSTATUSTAB_BO eachsysstatustab_bo;
	private TXN_CODE_BO txn_code_bo;
	private Logger logger = Logger.getLogger(this.getClass().getName());

	/**
	 * 自SC_COMPANY_PROFILE檔中找出發動行所屬總行清單
	 * 
	 * @return
	 */
	public List<LabelValueBean> getScBgbkIdList() {
		// 20150629 edit by hugo req by 李建利
		// List<LabelValueBean> beanList = new LinkedList<LabelValueBean>();
		// List<Map<String, String>> result = sc_com_Dao.getScBgbkIdList();
		// if(result != null){
		// LabelValueBean bean = null;
		// for(Map<String, String> map : result){
		// bean = new LabelValueBean(map.get("BGBK_ID") + " - " + map.get("BGBK_NAME"),
		// map.get("BGBK_ID"));
		// beanList.add(bean);
		// }
		// }
		// System.out.println("beanList>>"+beanList);
		// return beanList;
		return bank_group_bo.getBgbkIdList();
	}

	public Map<String, String> delete(SC_Company_Profile_Form sc_com_form) {
		String com_id = sc_com_form.getCOMPANY_ID();
		String txn_id = sc_com_form.getTXN_ID();
		String brbkid = sc_com_form.getSND_BRBK_ID();
		Map<String, String> map = null;
		Map<String, String> pkmap = new HashMap<String, String>();// e
		SC_COMPANY_PROFILE po = null;
		try {
			map = new HashMap<String, String>();
			pkmap.put("COMPANY_ID", com_id);
			pkmap.put("TXN_ID", txn_id);
			pkmap.put("SND_BRBK_ID", brbkid);
			
			
			//刪除 SC_HIS START
			SC_COMPANY_PROFILE_HIS hispo = new SC_COMPANY_PROFILE_HIS();
			List<Map<String, String>> delList = sc_com_his_Dao.findByCondition(com_id,txn_id,brbkid);
			for(Map<String, String> deldata:delList) {
				hispo = CodeUtils.objectCovert(SC_COMPANY_PROFILE_HIS.class, deldata);
				
				sc_com_his_Dao.remove(hispo);
			}
			
			//刪除 SC_HIS END
			
			
			SC_COMPANY_PROFILE_PK id = new SC_COMPANY_PROFILE_PK(com_id, txn_id, brbkid);
			po = sc_com_Dao.get(id);
			if (po == null) {
				map = sc_com_Dao.removeFail(po, pkmap, "刪除失敗，查無資料", 1);
				return map;
			}
			po.setCOMPANY_ID(com_id);
			po.setTXN_ID(txn_id);
			po.setSND_BRBK_ID(brbkid);
			sc_com_Dao.removeII(po, pkmap);
			map.put("result", "TRUE");
			map.put("msg", "刪除成功");
			map.put("target", "search");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			map = sc_com_Dao.removeFail(po, pkmap, "刪除失敗，系統異常", 2);
			return map;
		}
		return map;
	}

	/**
	 * 
	 * @param bgid
	 * @param brid
	 * @param formMap
	 * @return
	 */
	public Map<String, String> save(String com_id, String txn_id, String brbkid, Map formMap) {
		Map<String, String> map = null;
		try {
			map = new HashMap<String, String>();
			SC_COMPANY_PROFILE_PK id = new SC_COMPANY_PROFILE_PK(com_id, txn_id, brbkid);
			SC_COMPANY_PROFILE po = sc_com_Dao.get(id);
			if (po != null) {
				map.put("result", "FALSE");
				map.put("msg", "儲存失敗，id重複");
				map.put("target", "add_p");
				return map;
			}
			po = new SC_COMPANY_PROFILE();
			BeanUtils.populate(po, formMap);
			po.setBRBK_NAME(formMap.get("SND_BRBK_NAME").toString());
			po.setId(id);
			po.setCDATE(zDateHandler.getTheDateII());
			sc_com_Dao.save(po);
			
			
			//新增歷程檔區 START
			logger.debug("SC_HIS_Save Start");
			SC_COMPANY_PROFILE_HIS hispo = new SC_COMPANY_PROFILE_HIS();
			hispo.setSND_BRBK_ID(brbkid);
			hispo.setCOMPANY_ID(com_id);
			hispo.setTXN_ID(txn_id);
			hispo.setACTIVE_DATE(po.getFEE_TYPE_ACTIVE_DATE());
			hispo.setFEE_TYPE(po.getFEE_TYPE());
			hispo.setUDATE(po.getCDATE());
			
			sc_com_his_Dao.save(hispo);
			
			map.put("result", "TRUE");
			map.put("msg", "儲存成功");
			map.put("target", "search");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			map.put("result", "ERROR");
			map.put("msg", "儲存失敗，系統異常:" + e);
			map.put("target", "add_p");
			return map;
		}
		return map;
	}

	/**
	 * 
	 * @param bgid
	 * @param brid
	 * @param formMap
	 * @return
	 */
	public Map<String, String> update(String com_id, String txn_id, String brbkid, Map formMap , boolean ignoreActiveDate) {
		Map<String, String> map = null;
		Map<String, String> pkmap = new HashMap<String, String>();// e
		Map<String, String> oldmap = new HashMap<String, String>();// e
		SC_COMPANY_PROFILE po = null;
		
		logger.debug("SC_Update Start");
		
		
		try {
			map = new HashMap<String, String>();
			SC_COMPANY_PROFILE_PK id = new SC_COMPANY_PROFILE_PK(com_id, txn_id, brbkid);
			System.out.println("com_id="+com_id+" ,txn_id="+txn_id+" ,brbkid="+brbkid);
			po = sc_com_Dao.get(id);
			
			pkmap.put("COMPANY_ID", com_id);
			pkmap.put("TXN_ID", txn_id);
			pkmap.put("SND_BRBK_ID", brbkid);
			if (po == null) {
				// map.put("result", "FALSE");
				// map.put("msg", "儲存失敗，查無資料");
				// map.put("target", "edit_p");
				BeanUtils.populate(po, formMap);
				map = sc_com_Dao.updateFail(po, oldmap, pkmap, "儲存失敗，查無資料", 1);
				return map;
			}
			
			// 新增歷程檔區 START
			// 修改先新增HIS表 // 如果 formMap 的手續費類型 或 手續費啟用日 跟主表比有變  , 代表需要更新歷程檔  , 將"新的記錄"到HIS檔裡 !
			if(!po.getFEE_TYPE().equalsIgnoreCase((String) formMap.get("FEE_TYPE"))||
				!po.getFEE_TYPE_ACTIVE_DATE().equalsIgnoreCase((String) formMap.get("FEE_TYPE_ACTIVE_DATE"))) {
				logger.debug("SC_HIS_Update Start");
				try {
					SC_COMPANY_PROFILE_HIS hispo = new SC_COMPANY_PROFILE_HIS();
					hispo.setCOMPANY_ID(po.getId().getCOMPANY_ID());
					hispo.setTXN_ID(po.getId().getTXN_ID());
					hispo.setSND_BRBK_ID(po.getId().getSND_BRBK_ID());
					hispo.setACTIVE_DATE((String) formMap.get("FEE_TYPE_ACTIVE_DATE"));
					hispo.setFEE_TYPE((String) formMap.get("FEE_TYPE"));
					hispo.setUDATE(zDateHandler.getTheDateII());

					sc_com_his_Dao.save(hispo);
					logger.debug("HIS UPDATE SUCCESS");
				} catch (Exception e) {
					logger.error("HIS UPDATE ERROR :" + e.getMessage());
				}
				logger.debug("SC_HIS_Update END");
			}else {
				logger.debug("NO NEED TO UPDATE SC_HIS");
			}
			
			//新增歷程檔區 END
			
			//修改主檔區 START
			String tCOMPANY_ID = (String) formMap.get("COMPANY_ID");
			String tTXN_ID = (String) formMap.get("TXN_ID");
			String tSND_BRBK_ID = (String) formMap.get("SND_BRBK_ID");
			po.setCOMPANY_ID(tCOMPANY_ID);
			po.setTXN_ID(tTXN_ID);
			po.setSND_BRBK_ID(tSND_BRBK_ID);

			oldmap = BeanUtils.describe(po);
			po = new SC_COMPANY_PROFILE();
			BeanUtils.populate(po, formMap);
			po.setId(id);
			po.setUDATE(zDateHandler.getTheDateII());
			po.setCDATE(oldmap.get("CDATE"));
			po.setIS_SHORT("");
			// sc_com_Dao.save(po);
			//20201012修改  不轉空值
			//如果 更新為適用"舊手續費"  將FEE_TYPE變成"" ( 保持舊資料樣式  FEE_TYPE 沒值 (因為這是新增的欄位  ??????
//			if("Ao".equalsIgnoreCase(po.getFEE_TYPE())||"Bo".equalsIgnoreCase(po.getFEE_TYPE())) {
//				po.setFEE_TYPE("");
//			}
			sc_com_Dao.saveII(po, oldmap, pkmap);
			map.put("result", "TRUE");
			map.put("msg", "儲存成功");
			map.put("target", "search");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// map.put("result", "ERROR");
			// map.put("msg", "儲存失敗，系統異常:"+e);
			// map.put("target", "edit_p");
			sc_com_Dao.updateFail(po, oldmap, pkmap, "儲存失敗，查無資料", 1);
			return map;
		}
		return map;
	}

	public List<SC_COMPANY_PROFILE> searchBySndBrbkId(String com_id, String txn_id, String brbkid, String fee_type,String comName) {
		List<SC_COMPANY_PROFILE> list = new ArrayList<SC_COMPANY_PROFILE>();
		if (StrUtils.isEmpty(com_id) && StrUtils.isEmpty(txn_id) && StrUtils.isEmpty(brbkid)
				&& StrUtils.isEmpty(fee_type) && StrUtils.isEmpty(comName)) {
			list = sc_com_Dao.getData("", new ArrayList<String>());
		} else {
			StringBuffer sql = new StringBuffer();
			List<String> strList = new LinkedList<String>();
			Map<String, String> map = new HashMap<String, String>();
			map.put("COMPANY_ID", StrUtils.isEmpty(com_id) ? "" : com_id.trim());
			map.put("TXN_ID", StrUtils.isEmpty(txn_id) ? "" : txn_id.trim());
			map.put("SND_BRBK_ID", StrUtils.isEmpty(brbkid) ? "" : brbkid.trim());
			try {
				String comname = comName.trim();
				comname = comname.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
				comname = comname.replaceAll("\\+", "%2B");
				map.put("COMPANY_NAME", java.net.URLDecoder.decode(comname, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int i = 0;
			for (String key : map.keySet()) {
				if (StrUtils.isNotEmpty(map.get(key))) {
					if (i != 0) {
						sql.append(" AND ");
					}
					if (key.equals("COMPANY_NAME")) {
						sql.append(key + " LIKE ?");
						strList.add("%" + map.get(key) + "%");
					} else {
						sql.append(key + " = ?");
						strList.add(map.get(key));
					}
					i++;
				}
			}
			System.out.println("SC_COMPANY_PROFILE.sql>>" + sql);
			System.out.println("SC_COMPANY_PROFILE.param>>" + strList);
			list = sc_com_Dao.getData(sql.toString(), strList);
			// list=sc_com_Dao.find(sql.toString(), strList.toArray() );
			// list=sc_com_Dao.getDataByPK(sql.toString(), strList.toArray());
		}
		System.out.println("SC_COMPANY_PROFILE.list>>" + list);
		list = list == null ? null : list.size() == 0 ? null : list;
		List rslist = new ArrayList<>();
		if (list.size() != 0) {
			for (SC_COMPANY_PROFILE po : list) {
				
				if ("A".equals(po.getFEE_TYPE())) {
					po.setFEE_TYPE_CHT("固定");
				} else if ("B".equals(po.getFEE_TYPE())) {
					po.setFEE_TYPE_CHT("外加");
				} else if ("C".equals(po.getFEE_TYPE())) {
					po.setFEE_TYPE_CHT("百分比");
				} else if ("D".equals(po.getFEE_TYPE())) {
					po.setFEE_TYPE_CHT("級距");
				}

				if (!StrUtils.isEmpty(fee_type)) {
					if (po.getFEE_TYPE().equals(fee_type)) {
						rslist.add(po);
					}
				} else {
					rslist.add(po);
				}
				
				
//				//... 將FEE_TYPE=""的資料去舊表找出TYPE 
//				if(" ".equals(po.getFEE_TYPE())) {
//					po.setFEE_TYPE(fee_code_bo.checkFeeCodeType(po.getTXN_ID())+"o");
//				}
//				
//				if ("A".equals(po.getFEE_TYPE())||"Ao".equals(po.getFEE_TYPE())) {
//					po.setFEE_TYPE_CHT("固定");
//				} else if ("B".equals(po.getFEE_TYPE())||"Bo".equals(po.getFEE_TYPE())) {
//					po.setFEE_TYPE_CHT("外加");
//				} else if ("C".equals(po.getFEE_TYPE())) {
//					po.setFEE_TYPE_CHT("百分比");
//				} else if ("D".equals(po.getFEE_TYPE())) {
//					po.setFEE_TYPE_CHT("級距");
//				}
//				
//				if(!StrUtils.isEmpty(fee_type)) {
//					if(po.getFEE_TYPE().equals(fee_type+"o")||po.getFEE_TYPE().equals(fee_type)) {
//						rslist.add(po);
//					}
//				}else {
//					rslist.add(po);
//				}
			}
		}
		return rslist.size()==0?null:rslist;
	}

	public List<SC_COMPANY_PROFILE> searchBySenderHead(String com_id, String txn_id, String senderHead, String comName,
			String fee_type, String startDate, String endDate, String isShort, String orderSQL) {
		List<SC_COMPANY_PROFILE> list = new ArrayList<SC_COMPANY_PROFILE>();
		if (StrUtils.isEmpty(com_id) && StrUtils.isEmpty(txn_id) && StrUtils.isEmpty(senderHead)
				&& StrUtils.isEmpty(comName) && StrUtils.isEmpty(startDate) && StrUtils.isEmpty(endDate)
				&& StrUtils.isEmpty(fee_type) && isShort.equals("false")) {
		} else {
			StringBuffer sql = new StringBuffer();
			List<String> strList = new LinkedList<String>();
			Map<String, String> map = new HashMap<String, String>();
			map.put("COMPANY_ID", com_id.trim());
			map.put("TXN_ID", txn_id.trim());
			map.put("B.BGBK_ID", senderHead.trim());
			map.put("START_DATE", startDate.trim());
			map.put("END_DATE", endDate.trim());
			if (isShort.equals("on")) {
				map.put("IS_SHORT", isShort.trim());
			}
			try {
				map.put("COMPANY_NAME", java.net.URLDecoder.decode(comName.trim(), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			int i = 0;
			for (String key : map.keySet()) {
				if (StrUtils.isNotEmpty(map.get(key))) {
					if (i != 0) {
						sql.append(" AND ");
					}
					if (key.equals("COMPANY_NAME")) {
						sql.append("( COMPANY_NAME  LIKE ? OR COMPANY_ABBR_NAME LIKE ? ) ");
						strList.add("%" + map.get(key) + "%");
						strList.add("%" + map.get(key) + "%");
//						// 嘗試"完整"模糊查詢
//						String[] array = map.get(key).split("");
//						for (int j = 0; j < array.length; j++) {
//							if (j != array.length - 1) {
//								sql.append("(" + key + " LIKE ? or COMPANY_ABBR_NAME like ? ) AND");
//								strList.add("%" + array[j] + "%");
//								strList.add("%" + array[j] + "%");
//							} else {
//								sql.append("(" + key + " LIKE ? or COMPANY_ABBR_NAME like ? )");
//								strList.add("%" + array[j] + "%");
//								strList.add("%" + array[j] + "%");
//							}
//						}
					} else if (key.equals("START_DATE")) {
						sql.append("SYS_CDATE" + " >= ?");
						strList.add(map.get(key));
					} else if (key.equals("END_DATE")) {
						sql.append("SYS_CDATE" + " <= ?");
						strList.add(map.get(key));
					} else if (key.equals("IS_SHORT")) {
						sql.append("IS_SHORT = 'Y'");
					} else {
						sql.append(key + " = ?");
						strList.add(map.get(key));
					}
					i++;
				}
			}
			System.out.println("SC_COMPANY_PROFILE.sql>>" + sql);
			System.out.println("SC_COMPANY_PROFILE.param>>" + strList);
			list = sc_com_Dao.getData(sql.toString(), strList, orderSQL);
			// list=sc_com_Dao.find(sql.toString(), strList.toArray() );
			// list=sc_com_Dao.getDataByPK(sql.toString(), strList.toArray());
		}
		System.out.println("SC_COMPANY_PROFILE.list>>" + list);
		List rslist = new ArrayList<>();
		if (list.size() != 0) {
			for (SC_COMPANY_PROFILE po : list) {
				
				if ("A".equals(po.getFEE_TYPE())) {
					po.setFEE_TYPE_CHT("固定");
				} else if ("B".equals(po.getFEE_TYPE())) {
					po.setFEE_TYPE_CHT("外加");
				} else if ("C".equals(po.getFEE_TYPE())) {
					po.setFEE_TYPE_CHT("百分比");
				} else if ("D".equals(po.getFEE_TYPE())) {
					po.setFEE_TYPE_CHT("級距");
				}

				if (!StrUtils.isEmpty(fee_type)) {
					if (po.getFEE_TYPE().equals(fee_type)) {
						rslist.add(po);
					}
				} else {
					rslist.add(po);
				}
				
//				//... 將FEE_TYPE=""的資料去舊表找出TYPE 
//				if(" ".equals(po.getFEE_TYPE())) {
//					po.setFEE_TYPE(fee_code_bo.checkFeeCodeType(po.getTXN_ID())+"o");
//				}
//				
//				if ("A".equals(po.getFEE_TYPE())||"Ao".equals(po.getFEE_TYPE())) {
//					po.setFEE_TYPE_CHT("固定");
//				} else if ("B".equals(po.getFEE_TYPE())||"Bo".equals(po.getFEE_TYPE())) {
//					po.setFEE_TYPE_CHT("外加");
//				} else if ("C".equals(po.getFEE_TYPE())) {
//					po.setFEE_TYPE_CHT("百分比");
//				} else if ("D".equals(po.getFEE_TYPE())) {
//					po.setFEE_TYPE_CHT("級距");
//				}
//				
//				if(!StrUtils.isEmpty(fee_type)) {
//					if(po.getFEE_TYPE().equals(fee_type+"o")||po.getFEE_TYPE().equals(fee_type)) {
//						rslist.add(po);
//					}
//				}else {
//					rslist.add(po);
//				}
			}
		}
		return rslist;
	}

	public String search_toJson(Map<String, String> param) {
		String com_id = StrUtils.isNotEmpty(param.get("COMPANY_ID")) ? param.get("COMPANY_ID") : "";
		String fee_type = StrUtils.isNotEmpty(param.get("FEE_TYPE")) ? param.get("FEE_TYPE") : "";
		String txn_id = StrUtils.isNotEmpty(param.get("TXN_ID")) ? param.get("TXN_ID") : "";
		String senderHead = StrUtils.isNotEmpty(param.get("SENDERHEAD")) ? param.get("SENDERHEAD") : "";
		String comName = StrUtils.isNotEmpty(param.get("COMPANY_NAME")) ? param.get("COMPANY_NAME") : "";
		String startDate = StrUtils.isNotEmpty(param.get("START_DATE")) ? param.get("START_DATE") : "";
		String endDate = StrUtils.isNotEmpty(param.get("END_DATE")) ? param.get("END_DATE") : "";
		String isShort = StrUtils.isNotEmpty(param.get("IS_SHORT")) ? param.get("IS_SHORT") : "";
		String sord = StrUtils.isNotEmpty(param.get("sord")) ? param.get("sord") : "";
		String sidx = StrUtils.isNotEmpty(param.get("sidx")) ? param.get("sidx") : "";
		String orderSQL = StrUtils.isNotEmpty(sidx) ? " ORDER BY " + sidx + " " + sord : "";
		if (com_id.equals("all")) {
			com_id = "";
		}
		if (txn_id.equals("all")) {
			txn_id = "";
		}
		if (senderHead.equals("all")) {
			senderHead = "";
		}
		if (comName.equals("all")) {
			comName = "";
		}
		if (fee_type.equals("all")) {
			fee_type = "";
		}
		if (startDate != null && !startDate.equals("")) {
			startDate = DateTimeUtils.convertDate(startDate, "yyyyMMdd", "MM/dd/yyyy");
		}
		if (!endDate.equals("") && endDate != null) {
			endDate = DateTimeUtils.convertDate(endDate, "yyyyMMdd", "MM/dd/yyyy");
		}
		// 20150128 HUANGPU 改以總行查詢
		// String brbkId =StrUtils.isNotEmpty(param.get("SND_BRBK_ID"))?
		// param.get("SND_BRBK_ID"):"";
		// String jsonStr = JSONUtils.toJson(search(com_id, txn_id, brbkId, comName));
		String jsonStr = "";
		List<SC_COMPANY_PROFILE> list = searchBySenderHead(com_id, txn_id, senderHead, comName, fee_type, startDate,
				endDate, isShort, orderSQL);
		if (list.toString().equals("[]")) {
			jsonStr = "{}";
		} else {
			jsonStr = JSONUtils.toJson(list);
		}
		// jsonStr = StrUtils.isEmpty(jsonStr) ?"{}":jsonStr;
		System.out.println("jsonStr" + jsonStr);
		return jsonStr;
	}

	public SC_COMPANY_PROFILE getByPk(String COMPANY_ID, String TXN_ID, String SND_BRBK_ID) {
		SC_COMPANY_PROFILE po = null;
		try {
			po = sc_com_Dao.get(new SC_COMPANY_PROFILE_PK(COMPANY_ID, TXN_ID, SND_BRBK_ID));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return po;
	}

	// ReadXL 匯入excel文件

	// 檢核文件內容
	public List<String> validate(String filePath) throws Exception {
		List<String> errorList = new ArrayList<String>();
		HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(filePath));
		HSSFSheet sheet = wb.getSheetAt(0);
		if (sheet.getLastRowNum() == 0) {
			errorList.add("檔案錯誤");
		}

		for (int i = 0; i < (sheet.getLastRowNum() + 1); i++) {
			if (i < 3)
				continue;

			Row row = sheet.getRow(i);
			if (row == null)
				continue;

			// 發動者中文簡稱
			Cell cell = row.getCell(0);
			if (cell == null)
				continue;

			String cellValue = "";
			if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
				cellValue = cell.getStringCellValue();
				if (cellValue.equals("")) {
					errorList.add("第" + i + "筆   發動者中文簡稱 錯誤 ");
					System.out.println("第" + i + "筆   發動者中文簡稱 錯誤 ");
				}

			} else {
				errorList.add("第" + i + "筆   發動者中文簡稱 錯誤 ");
				System.out.println("第" + i + "筆   發動者中文簡稱 錯誤 ");
			}

			if (cellValue.getBytes("UTF-8").length > 30) {
				errorList.add("第" + i + "筆   發動者中文簡稱 錯誤 長度過長 ");
				System.out.println("第" + i + "筆   發動者中文簡稱 錯誤 長度過長 ");
			}
			

			// 發動者名稱
			cell = row.getCell(1);
			if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
				cellValue = cell.getStringCellValue();
				if (cellValue.equals("")) {
					errorList.add("第" + i + "筆   發動者名稱 錯誤 ");
					System.out.println("第" + i + "筆   發動者名稱 錯誤 ");
				}
			} else {
				errorList.add("第" + i + "筆   發動者名稱 錯誤 ");
				System.out.println("第" + i + "筆   發動者名稱 錯誤 ");
			}
			
			if (cellValue.getBytes("UTF-8").length > 400) {
				errorList.add("第" + i + "筆   發動者名稱 錯誤 長度過長 ");
				System.out.println("第" + i + "筆   發動者名稱 錯誤  長度過長 ");
			}

			// 發動者統編
			cell = row.getCell(2);
			String lnchrId = null;
			if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
				cellValue = String.valueOf((int) cell.getNumericCellValue());
			} else {
				cellValue = cell.getStringCellValue();
			}
			if (StringUtils.isEmpty(cellValue)) {
				errorList.add("第" + i + "筆   發動者統編 錯誤 ");
				System.out.println("第" + i + "筆   發動者統編 錯誤 ");
			} else if (cellValue.length() > 10) {
				errorList.add("第" + i + "筆   發動者統編 錯誤 ");
				System.out.println("第" + i + "筆   發動者統編 錯誤 ");
			}
			lnchrId = cellValue;

			// 交易代號
			cell = row.getCell(4);
			String txId = null;
			if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
				cellValue = String.valueOf((int) cell.getNumericCellValue());
			} else {
				cellValue = cell.getStringCellValue();
			}
			if (StringUtils.isNotEmpty(cellValue) && cellValue.length() > 10) {
				errorList.add("第" + i + "筆   交易代號 錯誤 ");
				System.out.println("第" + i + "筆   交易代號 錯誤 ");
			}
			txId = cellValue;
			if (txId.equals("") || txId == null) {
				errorList.add("第" + i + "筆   交易代號 錯誤 ");
			}

			// 發動行
			cell = row.getCell(5);
			if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
				cellValue = cell.getStringCellValue();
			} else {
				errorList.add("第" + i + "筆   發動行 錯誤 ");
			}
			if (StringUtils.isNotEmpty(cellValue)) {
				if (cellValue.getBytes("UTF-8").length > 60) {
					errorList.add("第" + i + "筆   發動行 錯誤 長度過長 ");
					System.out.println("第" + i + "筆   發動行 錯誤 長度過長 ");
				}
			}

			// 發動行代號
			cell = row.getCell(6);
			String lnchrBkId = null;
			if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
				cellValue = String.valueOf((int) cell.getNumericCellValue());
			} else {
				cellValue = cell.getStringCellValue();
			}
			if (StringUtils.isNotEmpty(cellValue)) {
				if (cellValue.getBytes("UTF-8").length > 7) {
					errorList.add("第" + i + "筆   發動行代號 錯誤  長度過長");
					System.out.println("第" + i + "筆   發動行代號 錯誤  長度過長");
				}
			}
			lnchrBkId = cellValue;
			if (lnchrBkId.equals("") || lnchrBkId == null) {
				errorList.add("第" + i + "筆   發動行代號 錯誤 ");
			}

			// 上市公司代號
			cell = row.getCell(7);
			if (cell != null) {
				cellValue = cell.getStringCellValue();
				if (cellValue.getBytes("UTF-8").length > 6) {
					errorList.add("第" + i + "筆   上市公司代號 錯誤 長度過長");
					System.out.println("第" + i + "筆   上市公司代號 錯誤 長度過長");
				}
			} else {
				errorList.add("檔案格式錯誤 ");
				return errorList;
			}

			// 現金股息發放日期
			cell = row.getCell(8);
			if (cell != null) {
				cellValue = cell.getStringCellValue();
				if (cellValue.getBytes("UTF-8").length > 10) {
					errorList.add("第" + i + "筆   現金股息發放日期 錯誤 長度過長");
					System.out.println("第" + i + "筆   現金股息發放日期 錯誤 長度過長");
				}
				if (!cellValue.equals("")) {
					String patternStr = "[0-9]{2,3}.[0-9]{2,2}.[0-9]{2,2}";
					if (cellValue.trim().matches(patternStr) == false) {
						errorList.add("第" + i + "筆   現金股息發放日期 錯誤 ");
						System.out.println("第" + i + "筆   現金股息發放日期 錯誤 ");
					}
				}

			} else {
				errorList.add("檔案格式錯誤 ");
				return errorList;
			}
			
//			cell = row.getCell(9);
//			String feeType = null;
//			if (cell != null) {
//				cellValue = cell.getStringCellValue();
//				if (cellValue.length() > 1) {
//					errorList.add("第" + i + "筆   收費類型 錯誤 ");
//					System.out.println("第" + i + "筆   收費類型 錯誤 ");
//				}
//				if (StringUtils.isNotEmpty(cellValue)) {
//					
//					List<FEE_CODE_NW> typeList = fee_code_nw_bo.checkActive(txId, feeType);
//					String bizDate = eachsysstatustab_bo.getBusinessDate();
//					boolean flag = false ;
//					for(FEE_CODE_NW data:typeList) {
//						if(Integer.parseInt(bizDate)>Integer.parseInt(data.getSTART_DATE())) {
//							flag =true;
//						}
//					}
//					if(!flag) {
//						errorList.add("第" + i + "筆   此交易代號無此收費類型 或 尚未啟用 ");
//						System.out.println("第" + i + "筆   此交易代號無此收費類型  或 尚未啟用 ");
//					}
//					
//				}
//			} else {
//				//errorList.add("第" + i + "筆   收費類型 錯誤 ");
//			}
			
			// SC_COMPANY_PROFILE_PK id = new SC_COMPANY_PROFILE_PK(lnchrId,
			// txId, lnchrBkId);
			// SC_COMPANY_PROFILE po = sc_com_Dao.get(id);
			//
			// if(po != null){
			// errorList.add("第"+i+"筆 資料重複");
			// }

		}

		return errorList;

	}

	// 文件匯入db
	public List<String> import_db(String path) throws Exception {

		HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(path));
		HSSFSheet sheet = wb.getSheetAt(0);
		SC_Company_Profile_Form form = new SC_Company_Profile_Form();
		List<SC_COMPANY_PROFILE> list = null;
		EACH_USERLOG userlog_po = null;
		int count = 0;
		List<String> alertList = new ArrayList<String>();
		List<String> txncodeList = new ArrayList<String>();
		String dbResult="";
		
		List<TXN_CODE> alltxncode=txn_code_bo.search("");
		for(TXN_CODE each:alltxncode) {
			txncodeList.add(each.getTXN_ID());
		}
		
		for (int i = 0; i < (sheet.getLastRowNum() + 1); i++) {
			if (i < 3)
				continue;

			Row row = sheet.getRow(i);
			if (row == null)
				continue;

			// 發動者中文簡稱
			Cell cell = row.getCell(0);
			if (cell == null)
				continue;
			String cellValue = cell.getStringCellValue();
			cellValue = removeEnterKey(cellValue);
			form.setCOMPANY_ABBR_NAME(cellValue);

			// 發動者名稱
			cell = row.getCell(1);
			cellValue = cell.getStringCellValue();
			cellValue = removeEnterKey(cellValue);
			form.setCOMPANY_NAME(cellValue);

			// 發動者統編
			cell = row.getCell(2);
			String lnchrId = null;
			if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
				cellValue = String.valueOf((int) cell.getNumericCellValue());
			} else {
				cellValue = cell.getStringCellValue();
			}
			cellValue = removeEnterKey(cellValue);
			lnchrId = cellValue;
			form.setCOMPANY_ID(cellValue);

			// 交易代號
			cell = row.getCell(4);
			String txId = null;
			if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
				cellValue = String.valueOf((int) cell.getNumericCellValue());
			} else {
				cellValue = cell.getStringCellValue();
			}
			cellValue = removeEnterKey(cellValue);
			txId = cellValue;
			form.setTXN_ID(cellValue);

			// 發動行
			cell = row.getCell(5);
			cellValue = cell.getStringCellValue();
			cellValue = removeEnterKey(cellValue);
			form.setSND_BRBK_NAME(cellValue);

			// 發動行代號
			cell = row.getCell(6);
			String lnchrBkId = null;
			if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
				cellValue = String.valueOf((int) cell.getNumericCellValue());
			} else {
				cellValue = cell.getStringCellValue();
			}
			cellValue = removeEnterKey(cellValue);
			lnchrBkId = cellValue;
			form.setSND_BRBK_ID(cellValue);

			// 上市公司代號
			cell = row.getCell(7);
			cellValue = cell.getStringCellValue();
			cellValue = removeEnterKey(cellValue);
			form.setIPO_COMPANY_ID(cellValue);

			// 現金股息發放日期
			cell = row.getCell(8);
			cellValue = cell.getStringCellValue().trim();
			cellValue = removeEnterKey(cellValue);

			if (!cellValue.equals("")) {
				if (cellValue.length() == 8) {
					cellValue = "00" + cellValue.replace(".", "");
				} else if (cellValue.length() == 9) {
					cellValue = "0" + cellValue.replace(".", "");
				}
			}
			
			form.setPROFIT_ISSUE_DATE(cellValue);
			
//			//收費類型
//			cell = row.getCell(9);
//			cellValue = cell.getStringCellValue();
//			cellValue = removeEnterKey(cellValue);
//			String feeType = cellValue;
//			if(cellValue== null) {
//				form.setFEE_TYPE("");
//			}
//			if(feeType== null) {
//				feeType="";
//			}
//			form.setFEE_TYPE(cellValue);

			
			SC_COMPANY_PROFILE_PK id = new SC_COMPANY_PROFILE_PK(lnchrId, txId, lnchrBkId);
			SC_COMPANY_PROFILE po = sc_com_Dao.get(id);
			
			//覆蓋或待處理:  
			//form.getFEE_TYPE為空  把原來的FEE_TYPE 跟 FEE_TYPE_ACTIVE_DATE 帶入 
			//form.getFEE_TYPE不為空 若與原來相同 >> 沒事 / 若與原來不同 >> 提示   把原來的FEE_TYPE 跟 FEE_TYPE_ACTIVE_DATE 帶入  提示
			String way = "";
			if (po != null) {
				way="update";
				//form.getFEE_TYPE為空
					form.setFEE_TYPE(po.getFEE_TYPE());
					form.setFEE_TYPE_ACTIVE_DATE(po.getFEE_TYPE_ACTIVE_DATE());
					count += 1;
			//新增:
			//如果是新資料 將手續費啟用日設為當前營業日 
			//FEE_TYPE 依TXN_ID找舊FEE_CODE找最新生效的那筆
			}else{
				way = "insert";
				if(!txncodeList.contains(txId)) {
					alertList.add("第" + i + "筆  無此交易代號("+txId+")");
					continue ; 
				}else {
					String feeType = fee_code_bo.checkFeeCodeType2(txId);
					if(StrUtils.isNotEmpty(feeType)) {
						form.setFEE_TYPE(feeType);
						form.setFEE_TYPE_ACTIVE_DATE(eachsysstatustab_bo.getBusinessDate());
					}else {
						alertList.add("第" + i + "筆  此交易代號("+txId+") 尚無啟用之手續費");
						continue ; 
					}
				}
			}
			
			Map map = insertDB(form.getCOMPANY_ID(), form.getTXN_ID(), form.getSND_BRBK_ID(), BeanUtils.describe(form),way);
		}
		
		
		dbResult = " 新增" + ((sheet.getLastRowNum() - 2) - count - alertList.size()) + "筆資料，覆蓋" + count + "筆資料 , 須修正"+alertList.size()+"筆資料";// 1040625
		
		alertList.add(0, dbResult);
		
		userlog_po = userlog_bo.getUSERLOG("W", "");
		userlog_po.setADEXCODE("匯入成功");
		userlog_po.setAFCHCON(dbResult);
		userLog_Dao = SpringAppCtxHelper.getBean("userLog_Dao");

		userLog_Dao.aop_save(userlog_po);

		return alertList;
	}

	// 匯入資料內容去掉換行符號
	public String removeEnterKey(String cellValue) {
		if (cellValue.indexOf("\n") > -1) {
			cellValue = cellValue.replaceAll("\n", "");
		}
		return cellValue;
	}

	// 將xls檔暫存至/eACH/tmp
	public boolean addTmpFile(SC_Company_Profile_Form sc_com_form, String filePath) throws Exception {
		// struts的File物件
		FormFile file = sc_com_form.getFILE();

		// 檔名
		String fileName = file.getFileName();
		// 檔案大小
		int fileSize = file.getFileSize();

		if (!("").equals(fileName)) {

			File newFile = new File(filePath, fileName);

			FileOutputStream fos = new FileOutputStream(newFile);
			fos.write(file.getFileData());
			fos.flush();
			fos.close();
			System.out.println("upload file:" + fileName + " ,fileSize:" + fileSize);
		} else {
			return false;
		}
		return true;
	}

	// 刪除暫存檔案
	public void deleteTmpFile(String filePath) {
		File file = new File(filePath);
		file.delete();
	}
	
	
	public Map<String, String> insertDB(String com_id, String txn_id, String brbkid, Map formMap , String way) {
		Map<String, String> map = null;
		String cdate = "";
		String cdateposition="";
		try {
			map = new HashMap<String, String>();
			SC_COMPANY_PROFILE_PK id = new SC_COMPANY_PROFILE_PK(com_id, txn_id, brbkid);
			SC_COMPANY_PROFILE po = sc_com_Dao.get(id);
			if(po != null ){
				cdate= StrUtils.isNotEmpty(po.getCDATE())?po.getCDATE():StrUtils.isNotEmpty(po.getSYS_CDATE())?po.getSYS_CDATE():"";
				cdateposition = StrUtils.isNotEmpty(po.getCDATE())?"C":StrUtils.isNotEmpty(po.getSYS_CDATE())?"S":"X";
			}
			// if(po != null ){
			// map.put("result", "FALSE");
			// map.put("msg", "儲存失敗，id重複");
			// map.put("target", "add_p");
			// return map;
			// }
			po = new SC_COMPANY_PROFILE();
			SC_COMPANY_PROFILE_HIS hispo = new SC_COMPANY_PROFILE_HIS();
			BeanUtils.populate(po, formMap);
			
			po.setId(id);
			if("insert".equals(way)) {
				//主表
				po.setCDATE(zDateHandler.getTheDateII());
				
				//歷程
				hispo.setFEE_TYPE((String)formMap.get("FEE_TYPE"));
				hispo.setCOMPANY_ID((String)formMap.get("COMPANY_ID"));
				hispo.setSND_BRBK_ID((String)formMap.get("SND_BRBK_ID"));
				hispo.setTXN_ID((String)formMap.get("TXN_ID"));
				hispo.setACTIVE_DATE((String)formMap.get("FEE_TYPE_ACTIVE_DATE"));
				hispo.setUDATE(zDateHandler.getTheDateII());
				
				sc_com_his_Dao.save(hispo);
			}else if("update".equals(way)){
				//更新不會更新到  HIS
				if("S".equals(cdateposition)){
					po.setSYS_CDATE(cdate);
				}else {
					po.setCDATE(cdate);
				}
				po.setUDATE(zDateHandler.getTheDateII());
			}else {
				
			}
			sc_com_Dao.aop_save(po);
			
			map.put("result", "TRUE");
			map.put("msg", "儲存成功");
			map.put("target", "search");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			map.put("result", "ERROR");
			map.put("msg", "儲存失敗，系統異常:" + e);
			map.put("target", "add_p");
			return map;
		}
		return map;
	}

	public String checkCOMPANY_ID(Map<String, String> params) {
		String result = "";
		List list = null;
		String companyId = params.get("COMPANY_ID");
		try {
			list = sc_com_Dao.checkCOMPANY_ID(companyId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		list = list == null ? null : list.size() == 0 ? null : list;
		if (list != null) {
			result = JSONUtils.toJson(list);
		}

		return result;
	}

	public String checkCOMPANY_NAME(Map<String, String> params) {
		String result = "";
		String companyId = params.get("COMPANY_ID");
		try {
			result = sc_com_Dao.checkCOMPANY_NAME(companyId);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public String checkAmount(Map<String, String> params) {
		String result = "";
		String companyId = params.get("COMPANY_ID");
		try {
			result = sc_com_Dao.checkAmount(companyId);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}
	
	
	public String checkSD_SC_TYPE(Map<String, String> params) {
		SYS_PARA sys = sys_para_bo.searchII();
		String result = "";
		// 0檢核 1不檢核
		if ("1".equals(sys.getSD_SC_TYPE_CHK())) {
			// ajax回傳0 代表不用檢核or沒東西要改
			result = "0";
		} else {
			String companyId = params.get("COMPANY_ID");
			String txn_id = params.get("TXN_ID");
			String fee_type = params.get("FEE_TYPE");
			String snd_brbk_id = params.get("SND_BRBK_ID");
			// 如果進來的是舊FEE_CODE,轉成空
			// if (fee_type.indexOf("o") != -1) {
			// fee_type = "";
			// }
			result = sc_com_Dao.checkSCAmount(companyId, txn_id, fee_type,snd_brbk_id);
		}

		return result;
	}

	public void updateNameById(Map<String, String> params) throws Exception {
		boolean result;
		String companyId = URLDecoder.decode(params.get("COMPANY_ID"), "UTF-8");
		String abbr_name = URLDecoder.decode(params.get("COMPANY_ABBR_NAME"), "UTF-8");
		String name = URLDecoder.decode(params.get("COMPANY_NAME"), "UTF-8");
		try {
			result = sc_com_Dao.updateNameById(companyId, name, abbr_name);
			System.out.println("發動者統一編號:" + companyId + "，更新資料：" + result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// public void updateNameByIdOnly930(Map<String, String> params) throws
	// Exception {
	// boolean result;
	// String companyId = URLDecoder.decode(params.get("SND_COMPANY_ID"), "UTF-8");
	// String abbr_name = URLDecoder.decode(params.get("COMPANY_ABBR_NAME"),
	// "UTF-8");
	// String name = URLDecoder.decode(params.get("COMPANY_NAME"), "UTF-8");
	// try {
	// result = sc_com_Dao.updateNameById(companyId, name, abbr_name);
	// System.out.println("發動者統一編號:" + companyId + "，更新資料：" + result);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	public String searchONBLOCKTAB(Map<String, String> params) {
		String TXTIME1 = params.get("START_DATE");
		String TXTIME2 = params.get("END_DATE");
		String action = params.get("action");

		String time1 = DateTimeUtils.convertDate(TXTIME1, "yyyyMMdd", "yyyyMMdd");
		String time2 = DateTimeUtils.convertDate(TXTIME2, "yyyyMMdd", "yyyyMMdd");

		String result = "";
		List<SC_COMPANY_PROFILE> list = null;
		List<SC_COMPANY_PROFILE> resultList = null;

		try {
			list = sc_com_Dao.searchONBLOCKTAB(time1, time2);

			resultList = updateSCbyONBLOCKTAB(list, action, TXTIME1, TXTIME2);

		} catch (Exception e) {
			e.printStackTrace();
		}

		resultList = resultList == null ? null : resultList.size() == 0 ? null : resultList;
		if (resultList != null) {
			result = JSONUtils.toJson(resultList);
		}

		return result;
	}

	public List<SC_COMPANY_PROFILE> updateSCbyONBLOCKTAB(List<SC_COMPANY_PROFILE> list, String action, String TXTIME1,
			String TXTIME2) throws Exception {
		List<SC_COMPANY_PROFILE> resultList = new ArrayList<SC_COMPANY_PROFILE>();
		Map<String, String> logmap = new HashMap<String, String>();
		int len = 0; // 總更新筆數
		int newcount = 0; // 異動(名稱、簡稱沒有缺)

		for (int i = 0; i < list.size(); i++) {
			String COMPANY_ID = list.get(i).getCOMPANY_ID();
			String SND_BRBK_ID = list.get(i).getSND_BRBK_ID();
			String TXN_ID = list.get(i).getTXN_ID();
			boolean isnew = false;
			SC_Company_Profile_Form form = new SC_Company_Profile_Form();

			List<SC_COMPANY_PROFILE> nameList = sc_com_Dao.checkCOMPANY_ID_SD(COMPANY_ID);
			String COMPANY_NAME = "";
			String COMPANY_ABBR_NAME = "";
			if (nameList.size() == 1) {
				COMPANY_NAME = nameList.get(0).getCOMPANY_NAME();
				COMPANY_ABBR_NAME = nameList.get(0).getCOMPANY_ABBR_NAME();
			}

			if (StrUtils.isEmpty(COMPANY_NAME) || StrUtils.isEmpty(COMPANY_ABBR_NAME)) {
				form.setIS_SHORT("Y");
				list.get(i).setIS_SHORT("Y");
			} else {
				isnew = true;
				form.setIS_SHORT("");
			}
			if (!StrUtils.isEmpty(COMPANY_NAME)) {
				form.setCOMPANY_NAME(COMPANY_NAME);
			}
			if (!StrUtils.isEmpty(COMPANY_ABBR_NAME)) {
				form.setCOMPANY_ABBR_NAME(COMPANY_ABBR_NAME);
			}
			form.setSYS_CDATE(zDateHandler.getTheDateII());

			Map map = insertDB(COMPANY_ID, TXN_ID, SND_BRBK_ID, BeanUtils.describe(form),"updateByOn");

			String addCount = map.get("result").toString();
			if (addCount.equals("TRUE")) {
				len += 1;
				resultList.add(i, list.get(i));
				if (isnew == true) {
					newcount += 1;
				}
			}
		}

		if (len != 0) {
			logmap.put("UPDATED", (newcount) + "筆");
			logmap.put("IS_SHORT", (len - newcount) + "筆");
			logmap.put("action", action);

			userlog_bo.genericLog("3", "成功，營業日期 {" + TXTIME1 + "~" + TXTIME2 + "}", action, logmap, null);
		} else {
			logmap.put("action", action);

			userlog_bo.genericLog("3", "查無資料，營業日期 {" + TXTIME1 + "~" + TXTIME2 + "}", action, logmap, null);
		}

		return resultList;
	}
	
	
	/**
	 * 交易代號下拉選單連結收費類型用
	 * @param params
	 * @return
	 */
	public String getFeeTypeList(Map<String, String> params){
		String result;
		String paramName;
		String paramValue;
		List<FEE_CODE_NW> list  = null;
		String TXN_ID = "";
		paramName = "TXN_ID";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			TXN_ID = paramValue;
		}
		
		Map rtnMap = new HashMap();
//		
		list  = fee_code_nw_bo.getFeeTypeList(TXN_ID);
		System.out.println("FEE_TYPE list >> " + list.toString() );
		
		if(list != null && list.size() > 0){
			rtnMap.put("result", "TRUE");
			rtnMap.put("msg", list);
		}else {
			rtnMap.put("result", "FALSE");
		}
		result = JSONUtils.map2json(rtnMap);
		System.out.println(result);
		return result;
	}
	
	
	
	public String hisSearch(Map<String, String> param) {
		System.out.println("hisSearch START");
		List<SC_COMPANY_PROFILE_HIS> list = null;
		List<SC_COMPANY_PROFILE> list2 = null;
		List<String> strList = new LinkedList<String>();
		List<String> strList2 = new LinkedList<String>();
		try {
			Map<String,String> map = new HashMap<String,String>();
			System.out.println("COMPANY_ID : " +param.get("com_id"));
			System.out.println("TXN_ID : " +param.get("txn_id"));
			System.out.println("SND_BRBK_ID : " +param.get("sndBrbkId"));
			map.put("COMPANY_ID", StrUtils.isEmpty(param.get("com_id"))?"":param.get("com_id").trim());
			map.put("TXN_ID", StrUtils.isEmpty(param.get("txn_id"))?"":param.get("txn_id").trim());
			map.put("SND_BRBK_ID", StrUtils.isEmpty(param.get("sndBrbkId"))?"":param.get("sndBrbkId").trim());
			
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT A.*, B.BRBK_NAME ");
			sql.append("FROM SC_COMPANY_PROFILE_HIS A LEFT JOIN BANK_BRANCH B ON A.SND_BRBK_ID = B.BRBK_ID ");
			sql.append("WHERE " );
			int i =0;
			for(String key:map.keySet()){
				if(StrUtils.isNotEmpty(map.get(key))){
					if(i!=0){
						sql.append(" AND ");
					}
					sql.append(key+" = ?");
					strList.add(map.get(key));
					i++;
				}
			}
			sql.append(" ORDER BY ACTIVE_DATE DESC ");
			list = new ArrayList<SC_COMPANY_PROFILE_HIS>();
			String cols[] = { "SEQ_ID","COMPANY_ID", "TXN_ID", "SND_BRBK_ID", "UDATE" ,"ACTIVE_DATE","FEE_TYPE"};
			
			list = sc_com_his_Dao.getData(sql.toString(),strList, cols ,SC_COMPANY_PROFILE_HIS.class);
			
			//20201016 更新  全都寫進HIS 第一筆不需要了
//			//加上主表的那筆
//			StringBuffer sql2 = new StringBuffer();
//			sql2.append("SELECT A.*, B.BRBK_NAME ");
//			sql2.append("FROM SC_COMPANY_PROFILE A LEFT JOIN BANK_BRANCH B ON A.SND_BRBK_ID = B.BRBK_ID ");
//			sql2.append("WHERE " );
//			int j =0;
//			for(String key:map.keySet()){
//				if(StrUtils.isNotEmpty(map.get(key))){
//					if(j!=0){
//						sql2.append(" AND ");
//					}
//					sql2.append(key+" = ?");
//					strList2.add(map.get(key));
//					j++;
//				}
//			}
//			
//			list2 = new ArrayList<SC_COMPANY_PROFILE>();
//			String cols2[] = {"COMPANY_ID", "TXN_ID", "SND_BRBK_ID", "UDATE" ,"FEE_TYPE_ACTIVE_DATE","FEE_TYPE"};
//			
//			list2 = sc_com_Dao.getData(sql2.toString(),strList2, cols2 ,SC_COMPANY_PROFILE.class);
//			
//			Map mapPo = CodeUtils.objectCovert(Map.class, list2.get(0));
//			mapPo.put("ACTIVE_DATE", mapPo.get("FEE_TYPE_ACTIVE_DATE"));
//			mapPo.remove("FEE_TYPE_ACTIVE_DATE");
//			System.out.println("@@@@@ mapPo > "+ mapPo);
//			list.add(0,CodeUtils.objectCovert(SC_COMPANY_PROFILE_HIS.class, (Object)mapPo));
			
			System.out.println("SC_COMPANY_PROFILE_HIS.list>>" + list);
			for(SC_COMPANY_PROFILE_HIS eachMap:list) {
				switch (eachMap.getFEE_TYPE()) {
				case " ":
					String oldFeeType=fee_code_bo.checkFeeCodeType(map.get("TXN_ID"));
					if("A".equals(oldFeeType)) {
						eachMap.setFEE_TYPE_CHT("固定");
					}else if("B".equals(oldFeeType)) {
						eachMap.setFEE_TYPE_CHT("外加");
					}
					break;
				case "A":
					eachMap.setFEE_TYPE_CHT("固定");
					break;
				case "B":
					eachMap.setFEE_TYPE_CHT("外加");
					break;
				case "C":
					eachMap.setFEE_TYPE_CHT("百分比");
					break;
				case "D":
					eachMap.setFEE_TYPE_CHT("級距");
					break;
				}
			}
			list = list != null && list.size() == 0 ? new ArrayList() : list;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String json = JSONUtils.toJson(list);
		// System.out.println("json->" +json);
		return json;
	}
	
	
	public String del_History(Map<String, String> param) {
		System.out.println("del_History START");
		Map rtnMap = new HashMap();
		SC_COMPANY_PROFILE_HIS po = new SC_COMPANY_PROFILE_HIS();
		try {
			po = sc_com_his_Dao.get(Integer.valueOf(param.get("seq_id")));
		}catch (Exception e) {
			System.out.println("GET DEL DATA ERROR");
			rtnMap.put("result", "FALSE");
		}
		Map<String,String> map = new HashMap<String,String>();
			map.put("COMPANY_ID", po.getCOMPANY_ID());
			map.put("TXN_ID", po.getTXN_ID());
			map.put("SND_BRBK_ID", po.getSND_BRBK_ID());
			List<Map<String, String>> hisList = sc_com_his_Dao.findByCondition(po.getCOMPANY_ID(),po.getTXN_ID(),po.getSND_BRBK_ID());
			Map<String, String> lastpo = hisList.get(hisList.size()-1);
			Map<String, String> reupdatepo = hisList.get(hisList.size()-2);
			Integer delSEQ_ID = (Integer)po.getSEQ_ID().intValue();
			String last_SEQ_ID = String.valueOf(lastpo.get("SEQ_ID"));
		try {
			if(last_SEQ_ID.equals(String.valueOf(delSEQ_ID))) {
				SC_COMPANY_PROFILE_PK popk= new SC_COMPANY_PROFILE_PK();
				popk.setCOMPANY_ID(po.getCOMPANY_ID());
				popk.setTXN_ID(po.getTXN_ID());
				popk.setSND_BRBK_ID(po.getSND_BRBK_ID());
				SC_COMPANY_PROFILE singlePo = sc_com_Dao.get(popk);
				singlePo.setId(popk);
				singlePo.setFEE_TYPE_ACTIVE_DATE(reupdatepo.get("ACTIVE_DATE"));
				singlePo.setFEE_TYPE(String.valueOf(reupdatepo.get("FEE_TYPE")));
				singlePo.setUDATE(String.valueOf(reupdatepo.get("UDATE")));
				sc_com_Dao.aop_save(singlePo);
			}
			sc_com_his_Dao.remove(po);
			
			rtnMap.put("result", "TRUE");
		}catch (Exception e) {
			System.out.println("DEL DATA OR RESAVE HIS ERROR");
			rtnMap.put("result", "FALSE");
		}
		
		rtnMap.put("msg",  JSONUtils.map2json(map));
		String result = JSONUtils.map2json(rtnMap);
		return result;
	}
	
	
	/**
	 * 交易代號+交易類型找出收續費與起用日比較，看是否生效
	 * @param params
	 * @return
	 */
	public String checkActive(Map<String, String> params){
		String result;
		String paramName;
		String paramValue;
		List<FEE_CODE_NW> list  = null;
		String TXN_ID = "";
		String FEE_TYPE = "";
		String START_DATE = "";
		paramName = "TXN_ID";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			TXN_ID = paramValue;
		}
		paramName = "FEE_TYPE";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			FEE_TYPE = paramValue;
		}
		paramName = "START_DATE";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)){
			START_DATE = paramValue;
		}
		
		Map rtnMap = new HashMap();
//		
		list  = fee_code_nw_bo.checkActive(TXN_ID,FEE_TYPE);
		System.out.println("FEE_TYPE list >> " + list.toString() );
		List rtlist = new ArrayList();
		
		if (list != null && list.size() > 0) {
			if (Integer.parseInt(START_DATE) < Integer.parseInt(list.get(0).getSTART_DATE())) {
				rtnMap.put("result", "FALSE");
				rtnMap.put("msg", list);
			} else {
				rtnMap.put("result", "TRUE");
			}
		}
		else{
			rtnMap.put("result", "FALSE2");
		}
		result = JSONUtils.map2json(rtnMap);
		System.out.println(result);
		return result;
	}
	
	
	public void updateFee_typeByCIDnTXID(Map<String, String> params) throws Exception {
		String com_id = URLDecoder.decode(params.get("COMPANY_ID"), "UTF-8");
		String txn_id = URLDecoder.decode(params.get("TXN_ID"), "UTF-8");
		String fee_type = URLDecoder.decode(params.get("FEE_TYPE"), "UTF-8");
		String snd_brbk_id = URLDecoder.decode(params.get("SND_BRBK_ID"), "UTF-8");
		List<SC_COMPANY_PROFILE> updateList = sc_com_Dao.updateSCList(com_id, txn_id, fee_type,snd_brbk_id);
		try {
			for (SC_COMPANY_PROFILE data : updateList) {
				System.out.println("GET ONE DATA");
				// 濾掉自己
				if (com_id.equals(data.getCOMPANY_ID()) && txn_id.equals(data.getTXN_ID())
						&& snd_brbk_id.equals(data.getSND_BRBK_ID())) {
					continue;
				}
				if (!" ".equals(data.getFEE_TYPE())) {
					params.put("FEE_TYPE_ORG", data.getFEE_TYPE());
				}
				params.put("COMPANY_NAME", URLDecoder.decode(params.get("COMPANY_NAME"), "UTF-8"));
				params.put("COMPANY_ABBR_NAME", URLDecoder.decode(params.get("COMPANY_ABBR_NAME"), "UTF-8"));
				params.put("FEE_TYPE_ORG", data.getFEE_TYPE());
				update(data.getCOMPANY_ID(), data.getTXN_ID(), data.getSND_BRBK_ID(), params , true);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public String checkDoubleDate(Map<String, String> params) {
		String result;
		String paramName;
		String paramValue;
		String COMPANY_ID="";
		String TXN_ID="";
		String SND_BRBK_ID="";
		String FEE_TYPE_ACTIVE_DATE="";
		List<Map<String, String>> list = null;
		paramName = "COMPANY_ID";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)) {
			COMPANY_ID = paramValue;
		}
		paramName = "TXN_ID";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)) {
			TXN_ID = paramValue;
		}
		paramName = "SND_BRBK_ID";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)) {
			SND_BRBK_ID = paramValue;
		}
		paramName = "FEE_TYPE_ACTIVE_DATE";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)) {
			FEE_TYPE_ACTIVE_DATE = paramValue;
		}

		Map rtnMap = new HashMap();
		//
		list = sc_com_his_Dao.checkDoubleDate(COMPANY_ID,TXN_ID,SND_BRBK_ID,FEE_TYPE_ACTIVE_DATE);
		System.out.println("HIS list >> " + list.size());

		if (list != null && list.size() > 0) {
			rtnMap.put("result", "FLASE");
		} else {
			rtnMap.put("result", "TRUE");
		}
		result = JSONUtils.map2json(rtnMap);
		System.out.println(result);
		return result;
	}

	public SC_COMPANY_PROFILE_Dao getSc_com_Dao() {
		return sc_com_Dao;
	}

	public void setSc_com_Dao(SC_COMPANY_PROFILE_Dao sc_com_Dao) {
		this.sc_com_Dao = sc_com_Dao;
	}

	public EACH_USERLOG_Dao getUserLog_Dao() {
		return userLog_Dao;
	}

	public void setUserLog_Dao(EACH_USERLOG_Dao userLog_Dao) {
		this.userLog_Dao = userLog_Dao;
	}

	public BANK_GROUP_BO getBank_group_bo() {
		return bank_group_bo;
	}

	public void setBank_group_bo(BANK_GROUP_BO bank_group_bo) {
		this.bank_group_bo = bank_group_bo;
	}

	public EACH_USERLOG_BO getUserlog_bo() {
		return userlog_bo;
	}

	public void setUserlog_bo(EACH_USERLOG_BO userlog_bo) {
		this.userlog_bo = userlog_bo;
	}

	public FEE_CODE_NW_BO getFee_code_nw_bo() {
		return fee_code_nw_bo;
	}

	public void setFee_code_nw_bo(FEE_CODE_NW_BO fee_code_nw_bo) {
		this.fee_code_nw_bo = fee_code_nw_bo;
	}

	public FEE_CODE_BO getFee_code_bo() {
		return fee_code_bo;
	}

	public void setFee_code_bo(FEE_CODE_BO fee_code_bo) {
		this.fee_code_bo = fee_code_bo;
	}

	public SC_COMPANY_PROFILE_HIS_Dao getSc_com_his_Dao() {
		return sc_com_his_Dao;
	}

	public void setSc_com_his_Dao(SC_COMPANY_PROFILE_HIS_Dao sc_com_his_Dao) {
		this.sc_com_his_Dao = sc_com_his_Dao;
	}

	public SYS_PARA_BO getSys_para_bo() {
		return sys_para_bo;
	}

	public void setSys_para_bo(SYS_PARA_BO sys_para_bo) {
		this.sys_para_bo = sys_para_bo;
	}

	public EACHSYSSTATUSTAB_BO getEachsysstatustab_bo() {
		return eachsysstatustab_bo;
	}

	public void setEachsysstatustab_bo(EACHSYSSTATUSTAB_BO eachsysstatustab_bo) {
		this.eachsysstatustab_bo = eachsysstatustab_bo;
	}

	public TXN_CODE_BO getTxn_code_bo() {
		return txn_code_bo;
	}

	public void setTxn_code_bo(TXN_CODE_BO txn_code_bo) {
		this.txn_code_bo = txn_code_bo;
	}
}
