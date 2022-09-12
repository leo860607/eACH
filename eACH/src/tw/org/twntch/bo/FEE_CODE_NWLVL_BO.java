package tw.org.twntch.bo;

import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import tw.org.twntch.db.dao.hibernate.EACH_USERLOG_Dao;
import tw.org.twntch.db.dao.hibernate.FEE_CODE_NWLVL_Dao;
import tw.org.twntch.db.dao.hibernate.FEE_CODE_NW_Dao;
import tw.org.twntch.db.dao.hibernate.SC_COMPANY_PROFILE_Dao;
import tw.org.twntch.db.dao.hibernate.TXN_CODE_Dao;
import tw.org.twntch.db.dao.hibernate.TXN_FEE_MAPPING_Dao;
import tw.org.twntch.form.SC_Company_Profile_Form;
import tw.org.twntch.po.FEE_CODE_NW;
import tw.org.twntch.po.FEE_CODE_NWLVL;
import tw.org.twntch.po.SC_COMPANY_PROFILE;
import tw.org.twntch.po.SC_COMPANY_PROFILE_PK;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.zDateHandler;

public class FEE_CODE_NWLVL_BO {
	private SC_COMPANY_PROFILE_Dao sc_com_Dao;
	private EACH_USERLOG_Dao userLog_Dao;
	private BANK_GROUP_BO bank_group_bo;
	private EACH_USERLOG_BO userlog_bo;
	private FEE_CODE_NWLVL_Dao fee_code_nwlvl_Dao;
	private FEE_CODE_NW_Dao fee_code_nw_Dao;
	private FEE_CODE_NW_BO fee_code_nw_bo;
	private EACHSYSSTATUSTAB_BO eachsysstatustab_bo;
	private TXN_CODE_Dao txn_code_Dao;
	private TXN_FEE_MAPPING_Dao txn_fee_mapping_Dao;

	/**
	 * 自SC_COMPANY_PROFILE檔中找出發動行所屬總行清單
	 * 
	 * @return
	 */
	// public List<LabelValueBean> getScBgbkIdList() {
	// // 20150629 edit by hugo req by 李建利
	// // List<LabelValueBean> beanList = new LinkedList<LabelValueBean>();
	// // List<Map<String, String>> result = sc_com_Dao.getScBgbkIdList();
	// // if(result != null){
	// // LabelValueBean bean = null;
	// // for(Map<String, String> map : result){
	// // bean = new LabelValueBean(map.get("BGBK_ID") + " - " +
	// map.get("BGBK_NAME"),
	// // map.get("BGBK_ID"));
	// // beanList.add(bean);
	// // }
	// // }
	// // System.out.println("beanList>>"+beanList);
	// // return beanList;
	// return bank_group_bo.getBgbkIdList();
	// }

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
	public Map<String, String> update(String com_id, String txn_id, String brbkid, Map formMap) {
		Map<String, String> map = null;
		Map<String, String> pkmap = new HashMap<String, String>();// e
		Map<String, String> oldmap = new HashMap<String, String>();// e
		SC_COMPANY_PROFILE po = null;
		try {
			map = new HashMap<String, String>();
			SC_COMPANY_PROFILE_PK id = new SC_COMPANY_PROFILE_PK(com_id, txn_id, brbkid);
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

	public List<SC_COMPANY_PROFILE> searchBySndBrbkId(String com_id, String txn_id, String brbkid, String comName) {
		List<SC_COMPANY_PROFILE> list = new ArrayList<SC_COMPANY_PROFILE>();
		if (StrUtils.isEmpty(com_id) && StrUtils.isEmpty(txn_id) && StrUtils.isEmpty(brbkid)
				&& StrUtils.isEmpty(comName)) {
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
		return list;
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
			map.put("FEE_TYPE", fee_type.trim());
			if (isShort.equals("on")) {
				map.put("IS_SHORT", isShort.trim());
			}
			try {
				map.put("COMPANY_NAME", java.net.URLDecoder.decode(comName.trim(), "UTF-8"));
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
					} else if (key.equals("START_DATE")) {
						sql.append("SYS_CDATE" + " >= ?");
						strList.add(map.get(key));
					} else if (key.equals("END_DATE")) {
						sql.append("SYS_CDATE" + " <= ?");
						strList.add(map.get(key));
					} else if (key.equals("IS_SHORT")) {
						sql.append("IS_SHORT = 'Y'");
					} else if (key.equals("FEE_TYPE")) {
						sql.append("FEE_TYPE = ?");
						strList.add(map.get(key));
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
		list = list == null ? null : list.size() == 0 ? null : list;
		for (SC_COMPANY_PROFILE po : list) {
			String feetype = po.getFEE_TYPE();
			if ("A".equals(feetype)) {
				po.setFEE_TYPE("固定");
			} else if ("B".equals(feetype)) {
				po.setFEE_TYPE("外加");
			} else if ("C".equals(feetype)) {
				po.setFEE_TYPE("百分比");
			} else if ("D".equals(feetype)) {
				po.setFEE_TYPE("級距");
			}
		}
		return list;
	}

	public List<FEE_CODE_NW> searchBySenderHead(String fee_id, String fee_type) {
		List<FEE_CODE_NW> list = new ArrayList<FEE_CODE_NW>();
		if (StrUtils.isEmpty(fee_id) && StrUtils.isEmpty(fee_type)) {
		} else {
			StringBuffer sql = new StringBuffer();
			List<String> strList = new LinkedList<String>();
			Map<String, String> map = new HashMap<String, String>();
			map.put("FEE_ID", fee_id.trim());
			map.put("FEE_TYPE", fee_type.trim());

			int i = 0;
			for (String key : map.keySet()) {
				if (StrUtils.isNotEmpty(map.get(key))) {
					if (i != 0) {
						sql.append(" AND ");
					}
					if (key.equals("FEE_ID")) {
						sql.append("FEE_ID= ?");
						strList.add(map.get(key));
					} else if (key.equals("FEE_TYPE")) {
						sql.append("FEE_TYPE= ?");
						strList.add(map.get(key));
					} else {
						sql.append(key + " = ?");
						strList.add(map.get(key));
					}
					i++;
				}
			}
			// System.out.println("SC_COMPANY_PROFILE.sql>>" + sql);
			// System.out.println("SC_COMPANY_PROFILE.param>>" + strList);
			list = fee_code_nw_Dao.getdata(sql.toString(), strList);
			// list=sc_com_Dao.find(sql.toString(), strList.toArray() );
			// list=sc_com_Dao.getDataByPK(sql.toString(), strList.toArray());
		}
		System.out.println("fee_code_nw_Dao.list>>" + list);
		list = list == null ? null : list.size() == 0 ? null : list;

		return list;
	}

	public List<FEE_CODE_NWLVL> searchBySenderHead(String fee_uid) {
		List<FEE_CODE_NWLVL> list = new ArrayList<FEE_CODE_NWLVL>();
		if (StrUtils.isEmpty(fee_uid)) {
		} else {
			StringBuffer sql = new StringBuffer();
			List<String> strList = new LinkedList<String>();
			// Map<String, String> map = new HashMap<String, String>();
			// map.put("FEE_UID", fee_uid.trim());

			// int i = 0;
			// for (String key : map.keySet()) {
			// if (StrUtils.isNotEmpty(map.get(key))) {
			// if (i != 0) {
			// sql.append(" AND ");
			// }
			// if (key.equals("FEE_UID")) {
			// sql.append("FEE_UID= ?");
			// strList.add(map.get(key));
			// } else {
			// sql.append(key + " = ?");
			// strList.add(map.get(key));
			// }
			// i++;
			// }
			// }
			// System.out.println("SC_COMPANY_PROFILE.sql>>" + sql);
			// System.out.println("SC_COMPANY_PROFILE.param>>" + strList);
			sql.append("FEE_UID= ?");
			strList.add(fee_uid);
			list = fee_code_nwlvl_Dao.getdata(sql.toString(), strList);
			// list=sc_com_Dao.find(sql.toString(), strList.toArray() );
			// list=sc_com_Dao.getDataByPK(sql.toString(), strList.toArray());
		}
		System.out.println("fee_code_nw_Dao.list>>" + list);
		// list = list == null ? null : list.size() == 0 ? null : list;
		// String type =null;
		// for(FEE_CODE_NWLVL po : list) {
		// type = po.getFEE_LVL_TYPE();
		// if(type== "1")po.setFEE_LVL_TYPE("固定");
		// else if(type == "2")po.setFEE_LVL_TYPE("百分比");
		// }

		return list;
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
		;
		if (txn_id.equals("all")) {
			txn_id = "";
		}
		if (senderHead.equals("all")) {
			senderHead = "";
		}
		if (comName.equals("all")) {
			comName = "";
		}
		if (startDate != null && !startDate.equals("")) {
			startDate = DateTimeUtils.convertDate("01090305", "yyyyMMdd", "MM/dd/yyyy");
		}
		if (!endDate.equals("") && endDate != null) {
			endDate = DateTimeUtils.convertDate(endDate, "yyyyMMdd", "MM/dd/yyyy");
		}

		// 20150128 HUANGPU 改以總行查詢
		// String brbkId =StrUtils.isNotEmpty(param.get("SND_BRBK_ID"))?
		// param.get("SND_BRBK_ID"):"";
		// String jsonStr = JSONUtils.toJson(search(com_id, txn_id, brbkId, comName));
		String jsonStr = JSONUtils.toJson(searchBySenderHead(com_id, txn_id, senderHead, comName, fee_type, startDate,
				endDate, isShort, orderSQL));
		System.out.println("json" + jsonStr);
		return jsonStr;
	}

	public String search_fee_toJson(Map<String, String> param) {
		String fee_id = StrUtils.isNotEmpty(param.get("TXN_ID")) ? param.get("TXN_ID") : "";
		String fee_type = StrUtils.isNotEmpty(param.get("FEE_TYPE")) ? param.get("FEE_TYPE") : "";
		if (fee_id.equals("all")) {
			fee_id = "";
		}

		FEE_CODE_NW po = fee_code_nw_bo.searchBySenderHead(fee_id, fee_type).get(0);

		String UID = po.getFEE_UID();
		String jsonStr = JSONUtils.toJson(searchBySenderHead(UID));
		// 注意 這裡因為時間關係，所以用串的，建議之後封裝成一個類別或改在POJO
		jsonStr = jsonStr.replace("}", ",\"START_DATE\": " + "\"" + po.getSTART_DATE() + "\"" + " }");
		System.out.println("json" + jsonStr);
		return jsonStr;
	}

	public FEE_CODE_NW_Dao getFee_code_nw_Dao() {
		return fee_code_nw_Dao;
	}

	public void setFee_code_nw_Dao(FEE_CODE_NW_Dao fee_code_nw_Dao) {
		this.fee_code_nw_Dao = fee_code_nw_Dao;
	}

	public FEE_CODE_NW_BO getFee_code_nw_bo() {
		return fee_code_nw_bo;
	}

	public void setFee_code_nw_bo(FEE_CODE_NW_BO fee_code_nw_bo) {
		this.fee_code_nw_bo = fee_code_nw_bo;
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

			if (cellValue.getBytes().length > 30) {
				errorList.add("第" + i + "筆   發動者中文簡稱 錯誤 ");
				System.out.println("第" + i + "筆   發動者中文簡稱 錯誤 ");
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

			if (cellValue.getBytes().length > 200) {
				errorList.add("第" + i + "筆   發動者名稱 錯誤 ");
				System.out.println("第" + i + "筆   發動者名稱 錯誤 ");
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
				if (cellValue.getBytes().length > 60) {
					errorList.add("第" + i + "筆   發動行 錯誤 ");
					System.out.println("第" + i + "筆   發動行 錯誤 ");
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
				if (cellValue.getBytes().length > 7) {
					errorList.add("第" + i + "筆   發動行代號 錯誤 ");
					System.out.println("第" + i + "筆   發動行代號 錯誤 ");
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
				if (cellValue.getBytes().length > 6) {
					errorList.add("第" + i + "筆   上市公司代號 錯誤 ");
					System.out.println("第" + i + "筆   上市公司代號 錯誤 ");
				}
			} else {
				errorList.add("檔案格式錯誤 ");
				return errorList;
			}

			// 現金股息發放日期
			cell = row.getCell(8);
			if (cell != null) {
				cellValue = cell.getStringCellValue();
				if (cellValue.getBytes().length > 10) {
					errorList.add("第" + i + "筆   現金股息發放日期 錯誤 ");
					System.out.println("第" + i + "筆   現金股息發放日期 錯誤 ");
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

	// // 文件匯入db
	// public String import_db(String path) throws Exception {
	//
	// HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(path));
	// HSSFSheet sheet = wb.getSheetAt(0);
	//
	// SC_Company_Profile_Form form = new SC_Company_Profile_Form();
	// List<SC_COMPANY_PROFILE> list = null;
	// EACH_USERLOG userlog_po = null;
	// int count = 0;
	// String result = "";
	//
	// for (int i = 0; i < (sheet.getLastRowNum() + 1); i++) {
	// if (i < 3)
	// continue;
	//
	// Row row = sheet.getRow(i);
	// if (row == null)
	// continue;
	//
	// // 發動者中文簡稱
	// Cell cell = row.getCell(0);
	// if (cell == null)
	// continue;
	// String cellValue = cell.getStringCellValue();
	// cellValue = removeEnterKey(cellValue);
	// form.setCOMPANY_ABBR_NAME(cellValue);
	//
	// // 發動者名稱
	// cell = row.getCell(1);
	// cellValue = cell.getStringCellValue();
	// cellValue = removeEnterKey(cellValue);
	// form.setCOMPANY_NAME(cellValue);
	//
	// // 發動者統編
	// cell = row.getCell(2);
	// String lnchrId = null;
	// if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
	// cellValue = String.valueOf((int) cell.getNumericCellValue());
	// } else {
	// cellValue = cell.getStringCellValue();
	// }
	// cellValue = removeEnterKey(cellValue);
	// lnchrId = cellValue;
	// form.setCOMPANY_ID(cellValue);
	//
	// // 交易代號
	// cell = row.getCell(4);
	// String txId = null;
	// if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
	// cellValue = String.valueOf((int) cell.getNumericCellValue());
	// } else {
	// cellValue = cell.getStringCellValue();
	// }
	// cellValue = removeEnterKey(cellValue);
	// txId = cellValue;
	// form.setTXN_ID(cellValue);
	//
	// // 發動行
	// cell = row.getCell(5);
	// cellValue = cell.getStringCellValue();
	// cellValue = removeEnterKey(cellValue);
	// form.setSND_BRBK_NAME(cellValue);
	//
	// // 發動行代號
	// cell = row.getCell(6);
	// String lnchrBkId = null;
	// if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
	// cellValue = String.valueOf((int) cell.getNumericCellValue());
	// } else {
	// cellValue = cell.getStringCellValue();
	// }
	// cellValue = removeEnterKey(cellValue);
	// lnchrBkId = cellValue;
	// form.setSND_BRBK_ID(cellValue);
	//
	// // 上市公司代號
	// cell = row.getCell(7);
	// cellValue = cell.getStringCellValue();
	// cellValue = removeEnterKey(cellValue);
	// form.setIPO_COMPANY_ID(cellValue);
	//
	// // 現金股息發放日期
	// cell = row.getCell(8);
	// cellValue = cell.getStringCellValue().trim();
	// cellValue = removeEnterKey(cellValue);
	//
	// if (!cellValue.equals("")) {
	// if (cellValue.length() == 8) {
	// cellValue = "00" + cellValue.replace(".", "");
	// } else if (cellValue.length() == 9) {
	// cellValue = "0" + cellValue.replace(".", "");
	// }
	// }
	//
	// form.setPROFIT_ISSUE_DATE(cellValue);
	//
	// SC_COMPANY_PROFILE_PK id = new SC_COMPANY_PROFILE_PK(lnchrId, txId,
	// lnchrBkId);
	// SC_COMPANY_PROFILE po = sc_com_Dao.get(id);
	//
	// if (po != null) {
	// count += 1;
	// }
	//
	// Map map = insertDB(form.getCOMPANY_ID(), form.getTXN_ID(),
	// form.getSND_BRBK_ID(), BeanUtils.describe(form));
	// BeanUtils.populate(form, map);
	// list = searchBySndBrbkId(form.getCOMPANY_ID(), form.getTXN_ID(),
	// form.getSND_BRBK_ID(),
	// form.getCOMPANY_NAME());
	// form.setScaseary(list);
	//
	// }
	// result = "新增" + ((sheet.getLastRowNum() - 2) - count) + "筆資料，覆蓋" + count +
	// "筆資料";// 1040625
	// // 除了table本身以外要再扣頭兩行header
	//
	// userlog_po = userlog_bo.getUSERLOG("W", "");
	// userlog_po.setADEXCODE("匯入成功");
	// userlog_po.setAFCHCON(result);
	// userLog_Dao = SpringAppCtxHelper.getBean("userLog_Dao");
	//
	// userLog_Dao.aop_save(userlog_po);
	//
	// return result;
	// }
	//
	// // 匯入資料內容去掉換行符號
	// public String removeEnterKey(String cellValue) {
	// if (cellValue.indexOf("\n") > -1) {
	// cellValue = cellValue.replaceAll("\n", "");
	// }
	// return cellValue;
	// }
	//
	// // 將xls檔暫存至/eACH/tmp
	// public boolean addTmpFile(SC_Company_Profile_Form sc_com_form, String
	// filePath) throws Exception {
	// // struts的File物件
	// FormFile file = sc_com_form.getFILE();
	//
	// // 檔名
	// String fileName = file.getFileName();
	// // 檔案大小
	// int fileSize = file.getFileSize();
	//
	// if (!("").equals(fileName)) {
	//
	// System.out.println("Server path:" + filePath);
	// File newFile = new File(filePath, fileName);
	//
	// FileOutputStream fos = new FileOutputStream(newFile);
	// fos.write(file.getFileData());
	// fos.flush();
	// fos.close();
	// System.out.println("upload file:" + fileName + " ,fileSize:" + fileSize);
	// } else {
	// return false;
	// }
	// return true;
	// }
	//
	// // 刪除暫存檔案
	// public void deleteTmpFile(String filePath) {
	// File file = new File(filePath);
	// file.delete();
	// }
	//
	// public Map<String, String> insertDB(String com_id, String txn_id, String
	// brbkid, Map formMap) {
	// Map<String, String> map = null;
	// try {
	// map = new HashMap<String, String>();
	// SC_COMPANY_PROFILE_PK id = new SC_COMPANY_PROFILE_PK(com_id, txn_id, brbkid);
	// SC_COMPANY_PROFILE po = sc_com_Dao.get(id);
	// // if(po != null ){
	// // map.put("result", "FALSE");
	// // map.put("msg", "儲存失敗，id重複");
	// // map.put("target", "add_p");
	// // return map;
	// // }
	// po = new SC_COMPANY_PROFILE();
	// BeanUtils.populate(po, formMap);
	// po.setId(id);
	// po.setCDATE(zDateHandler.getTheDateII());
	// sc_com_Dao.aop_save(po);
	// map.put("result", "TRUE");
	// map.put("msg", "儲存成功");
	// map.put("target", "search");
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// map.put("result", "ERROR");
	// map.put("msg", "儲存失敗，系統異常:" + e);
	// map.put("target", "add_p");
	// return map;
	// }
	// return map;
	// }
	//
	// public String checkCOMPANY_ID(Map<String, String> params) {
	// String result = "";
	// List list = null;
	// String companyId = params.get("COMPANY_ID");
	// try {
	// list = sc_com_Dao.checkCOMPANY_ID(companyId);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// list = list == null ? null : list.size() == 0 ? null : list;
	// if (list != null) {
	// result = JSONUtils.toJson(list);
	// }
	//
	// return result;
	// }
	//
	// public String checkCOMPANY_NAME(Map<String, String> params) {
	// String result = "";
	// String companyId = params.get("COMPANY_ID");
	// try {
	// result = sc_com_Dao.checkCOMPANY_NAME(companyId);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// return result;
	// }
	//
	// public String checkAmount(Map<String, String> params) {
	// String result = "";
	// String companyId = params.get("COMPANY_ID");
	// try {
	// result = sc_com_Dao.checkAmount(companyId);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// return result;
	// }
	//
	// public void updateNameById(Map<String, String> params) throws Exception {
	// boolean result;
	// String companyId = URLDecoder.decode(params.get("COMPANY_ID"), "UTF-8");
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
	//
	// // public void updateNameByIdOnly930(Map<String, String> params) throws
	// // Exception {
	// // boolean result;
	// // String companyId = URLDecoder.decode(params.get("SND_COMPANY_ID"),
	// "UTF-8");
	// // String abbr_name = URLDecoder.decode(params.get("COMPANY_ABBR_NAME"),
	// // "UTF-8");
	// // String name = URLDecoder.decode(params.get("COMPANY_NAME"), "UTF-8");
	// // try {
	// // result = sc_com_Dao.updateNameById(companyId, name, abbr_name);
	// // System.out.println("發動者統一編號:" + companyId + "，更新資料：" + result);
	// // } catch (Exception e) {
	// // e.printStackTrace();
	// // }
	// // }
	//
	// public String searchONBLOCKTAB(Map<String, String> params) {
	// String TXTIME1 = params.get("START_DATE");
	// String TXTIME2 = params.get("END_DATE");
	// String action = params.get("action");
	//
	// String time1 = DateTimeUtils.convertDate(TXTIME1, "yyyyMMdd", "yyyyMMdd");
	// String time2 = DateTimeUtils.convertDate(TXTIME2, "yyyyMMdd", "yyyyMMdd");
	//
	// String result = "";
	// List<SC_COMPANY_PROFILE> list = null;
	// List<SC_COMPANY_PROFILE> resultList = null;
	//
	// try {
	// list = sc_com_Dao.searchONBLOCKTAB(time1, time2);
	//
	// resultList = updateSCbyONBLOCKTAB(list, action, TXTIME1, TXTIME2);
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// resultList = resultList == null ? null : resultList.size() == 0 ? null :
	// resultList;
	// if (resultList != null) {
	// result = JSONUtils.toJson(resultList);
	// }
	//
	// return result;
	// }
	//
	// public List<SC_COMPANY_PROFILE> updateSCbyONBLOCKTAB(List<SC_COMPANY_PROFILE>
	// list, String action, String TXTIME1,
	// String TXTIME2) throws Exception {
	// List<SC_COMPANY_PROFILE> resultList = new ArrayList<SC_COMPANY_PROFILE>();
	// Map<String, String> logmap = new HashMap<String, String>();
	// int len = 0; // 總更新筆數
	// int newcount = 0; // 異動(名稱、簡稱沒有缺)
	//
	// for (int i = 0; i < list.size(); i++) {
	// String COMPANY_ID = list.get(i).getCOMPANY_ID();
	// String SND_BRBK_ID = list.get(i).getSND_BRBK_ID();
	// String TXN_ID = list.get(i).getTXN_ID();
	// boolean isnew = false;
	// SC_Company_Profile_Form form = new SC_Company_Profile_Form();
	//
	// List<SC_COMPANY_PROFILE> nameList =
	// sc_com_Dao.checkCOMPANY_ID_SD(COMPANY_ID);
	// String COMPANY_NAME = "";
	// String COMPANY_ABBR_NAME = "";
	// if (nameList.size() == 1) {
	// COMPANY_NAME = nameList.get(0).getCOMPANY_NAME();
	// COMPANY_ABBR_NAME = nameList.get(0).getCOMPANY_ABBR_NAME();
	// }
	//
	// if (StrUtils.isEmpty(COMPANY_NAME) || StrUtils.isEmpty(COMPANY_ABBR_NAME)) {
	// form.setIS_SHORT("Y");
	// list.get(i).setIS_SHORT("Y");
	// } else {
	// isnew = true;
	// form.setIS_SHORT("");
	// }
	// if (!StrUtils.isEmpty(COMPANY_NAME)) {
	// form.setCOMPANY_NAME(COMPANY_NAME);
	// }
	// if (!StrUtils.isEmpty(COMPANY_ABBR_NAME)) {
	// form.setCOMPANY_ABBR_NAME(COMPANY_ABBR_NAME);
	// }
	// form.setSYS_CDATE(zDateHandler.getTheDateII());
	//
	// Map map = insertDB(COMPANY_ID, TXN_ID, SND_BRBK_ID,
	// BeanUtils.describe(form));
	//
	// String addCount = map.get("result").toString();
	// if (addCount.equals("TRUE")) {
	// len += 1;
	// resultList.add(i, list.get(i));
	// if (isnew == true) {
	// newcount += 1;
	// }
	// }
	// }
	//
	// if (len != 0) {
	// logmap.put("UPDATED", (newcount) + "筆");
	// logmap.put("IS_SHORT", (len - newcount) + "筆");
	// logmap.put("action", action);
	//
	// userlog_bo.genericLog("3", "成功，營業日期 {" + TXTIME1 + "~" + TXTIME2 + "}",
	// action, logmap, null);
	// } else {
	// logmap.put("action", action);
	//
	// userlog_bo.genericLog("3", "查無資料，營業日期 {" + TXTIME1 + "~" + TXTIME2 + "}",
	// action, logmap, null);
	// }
	//
	// return resultList;
	// }

	public Map<String, String> save(FEE_CODE_NWLVL po) {
		Map<String, String> map = null;
		Map<String, String> pkmap = new HashMap<String, String>();
		try {
			map = new HashMap<String, String>();

			pkmap.put("FEE_UID", po.getFEE_ID());
			pkmap.put("FEE_DTNO", po.getFEE_DTNO());
			System.out.println("po.getFEE_ID()>>" + po.getFEE_ID());
			System.out.println("po.getFEE_DTNO()>>" + po.getFEE_DTNO());

			po.setCDATE(zDateHandler.getTheDateII());

			fee_code_nwlvl_Dao.save(po, pkmap);
			map.put("result", "TRUE");
			map.put("msg", "儲存成功");
			map.put("target", "search");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// map.put("result", "ERROR");
			// map.put("msg", "儲存失敗，系統異常");
			// map.put("target", "add_p");
			map = fee_code_nwlvl_Dao.saveFail(null, pkmap, "儲存失敗，系統異常", 2);
			return map;
		}
		return map;
	}

	public Map<String, String> update(FEE_CODE_NWLVL polv) {
		Map<String, String> map = null;
		Map<String, String> pkmap = new HashMap<String, String>();
		Map<String, String> oldmap = new HashMap<String, String>();
		try {
			map = new HashMap<String, String>();
			FEE_CODE_NWLVL tmp = fee_code_nwlvl_Dao.findByUK(polv.getFEE_UID(), polv.getFEE_DTNO());
			pkmap.put("FEE_UID", polv.getFEE_UID());
			pkmap.put("FEE_DTNO", polv.getFEE_DTNO());
			// pkmap = BeanUtils.describe(po.getId());
			// if(po == null ){
			if (tmp == null) {
				map.put("result", "FALSE");
				map.put("msg", "儲存失敗，查無資料");
				map.put("target", "edit_p");
				// map = fee_code_nw_Dao.updateFail(po, oldmap, pkmap, "儲存失敗，查無資料", 1);
				return map;
			}
			oldmap = BeanUtils.describe(tmp);
			polv.setCDATE(tmp.getCDATE());
			polv.setUDATE(zDateHandler.getTheDateII());
			fee_code_nwlvl_Dao.saveII(polv, oldmap, pkmap);
			map.put("result", "TRUE");
			map.put("msg", "儲存成功");
			map.put("target", "search");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", "ERROR");
			map.put("msg", "儲存失敗，系統異常");
			map.put("target", "edit_p");
			return map;
		}
		return map;
	}

	public FEE_CODE_NWLVL findByUK(String fee_uid, String dtno) {
		return fee_code_nwlvl_Dao.findByUK(fee_uid, dtno);
	}

	public int lvlrowcnt(String fee_uid) {
		int result = fee_code_nwlvl_Dao.getLVLRows(fee_uid);
		return result;
	}

	public Map<String, String> delete(String feeUId, String dtno) {
		boolean result = false;
		Map<String, String> map = null;
		Map<String, String> pkmap = new HashMap<String, String>();
		Map<String, String> oldmap = new HashMap<String, String>();
		try {
			map = new HashMap<String, String>();
			pkmap.put("FEE_UID", feeUId);
			pkmap.put("FEE_DTNO", dtno);
			FEE_CODE_NWLVL po = fee_code_nwlvl_Dao.findByUK(feeUId, dtno);
			// result=fee_code_nw_Dao.deleteLVL(feeUId,dtno);
			fee_code_nwlvl_Dao.remove(po);

			map.put("result", "TRUE");
			map.put("msg", "刪除成功");
			map.put("target", "search");
			oldmap = BeanUtils.describe(po);
			oldmap.remove("TXN_NAME");
			oldmap.putAll(BeanUtils.describe(pkmap));
			userlog_bo.writeLog("D", oldmap, null, pkmap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			map.put("result", "ERROR");
			map.put("msg", "刪除失敗，系統異常");
			map.put("target", "edit_p");
			// map = fee_code_nw_Dao.removeFail(null, pkmap, "刪除失敗，系統異常", 2);
			return map;
		}
		return map;
	}
	
	public Map<String, String> deleteLVL(String feeUId) {
		boolean result = false;
		Map<String, String> map = null;
		Map<String, String> pkmap = new HashMap<String, String>();
		Map<String, String> oldmap = new HashMap<String, String>();
		try {
			map = new HashMap<String, String>();
			pkmap.put("FEE_UID", feeUId);
			List<FEE_CODE_NWLVL> polsit = fee_code_nwlvl_Dao.findByFeeUid(feeUId);
			// result=fee_code_nw_Dao.deleteLVL(feeUId,dtno);
			for(FEE_CODE_NWLVL po:polsit) {
				fee_code_nwlvl_Dao.remove(po);	
				oldmap = BeanUtils.describe(po);
				oldmap.remove("TXN_NAME");
				oldmap.putAll(BeanUtils.describe(pkmap));
				userlog_bo.writeLog("D", oldmap, null, pkmap);
			}
			map.put("result", "TRUE");
			map.put("msg", "刪除成功");
			map.put("target", "search");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			map.put("result", "ERROR");
			map.put("msg", "刪除失敗，系統異常");
			map.put("target", "edit_p");
			// map = fee_code_nw_Dao.removeFail(null, pkmap, "刪除失敗，系統異常", 2);
			return map;
		}
		return map;
	}

	public String search_toJson_LVL(Map<String, String> params) {
		String jsonStr = null;
		String paramName;
		String paramValue;

		String feeUid = "";
		paramName = "feeUId";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)) {
			feeUid = paramValue;
		}
		System.out.println("search_toJson_LVL >> " + feeUid);
		List<FEE_CODE_NWLVL> list = null;
		list = search_LVL(feeUid);
		for (FEE_CODE_NWLVL data : list) {
			if ("1".equals(data.getFEE_LVL_TYPE())) {
				data.setFEE_LVL_TYPE_CHT("固定");
			} else if ("2".equals(data.getFEE_LVL_TYPE())) {
				data.setFEE_LVL_TYPE_CHT("百分比");
			}
		}

		if (list != null && list.size() > 0) {
			jsonStr = JSONUtils.toJson(list);
		}
		System.out.println("json>>" + jsonStr);
		return jsonStr;
	}

	public List<FEE_CODE_NWLVL> search_LVL(String feeUid) {
		List<FEE_CODE_NWLVL> list = null;
		try {
			list = fee_code_nwlvl_Dao.findByFeeUid(feeUid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		list = list == null ? null : list.size() == 0 ? null : list;
		return list;
	}
	
	public String checkDataEnd(Map<String, String> params) {
		List<FEE_CODE_NWLVL> list = null;
		String json = "{}";
		Map<String,String> retmap = new HashMap<String,String>();
		retmap.put("result", "FALSE");
		try {
			System.out.println("checkDataEnd FEE_UID " + params.get("feeUid"));
			list = fee_code_nwlvl_Dao.findByFeeUid(params.get("feeUid"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		for(FEE_CODE_NWLVL eachData:list) {
			if("9999999999999".equals(eachData.getFEE_LVL_END_AMT())){
				retmap.put("result", "TRUE");
			}
		}
		json = JSONUtils.map2json(retmap);
		return json;
	}

	public SC_COMPANY_PROFILE_Dao getSc_com_Dao() {
		return sc_com_Dao;
	}

	public FEE_CODE_NWLVL_Dao getFee_code_nwlvl_Dao() {
		return fee_code_nwlvl_Dao;
	}

	public void setFee_code_nwlvl_Dao(FEE_CODE_NWLVL_Dao fee_code_nwlvl_Dao) {
		this.fee_code_nwlvl_Dao = fee_code_nwlvl_Dao;
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

	public EACHSYSSTATUSTAB_BO getEachsysstatustab_bo() {
		return eachsysstatustab_bo;
	}

	public void setEachsysstatustab_bo(EACHSYSSTATUSTAB_BO eachsysstatustab_bo) {
		this.eachsysstatustab_bo = eachsysstatustab_bo;
	}

	public TXN_CODE_Dao getTxn_code_Dao() {
		return txn_code_Dao;
	}

	public void setTxn_code_Dao(TXN_CODE_Dao txn_code_Dao) {
		this.txn_code_Dao = txn_code_Dao;
	}

	public TXN_FEE_MAPPING_Dao getTxn_fee_mapping_Dao() {
		return txn_fee_mapping_Dao;
	}

	public void setTxn_fee_mapping_Dao(TXN_FEE_MAPPING_Dao txn_fee_mapping_Dao) {
		this.txn_fee_mapping_Dao = txn_fee_mapping_Dao;
	}
	
	
}
