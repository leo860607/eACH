package tw.org.twntch.bo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

import com.ibm.db2.jcc.am.re;

import tw.org.twntch.aop.GenerieAop;
import tw.org.twntch.db.dao.hibernate.EACH_USERLOG_Dao;
import tw.org.twntch.db.dao.hibernate.SD_COMPANY_PROFILE_Dao;
import tw.org.twntch.db.dao.hibernate.SD_COMPANY_PROFILE_HIS_Dao;
import tw.org.twntch.form.SD_Company_Profile_Form;
import tw.org.twntch.po.EACH_USERLOG;
import tw.org.twntch.po.FEE_CODE_NW;
import tw.org.twntch.po.SD_COMPANY_PROFILE;
import tw.org.twntch.po.SD_COMPANY_PROFILE_HIS;
import tw.org.twntch.po.SD_COMPANY_PROFILE_PK;
import tw.org.twntch.po.SYS_PARA;
import tw.org.twntch.util.CodeUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.SpringAppCtxHelper;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.zDateHandler;

public class SD_COMPANY_PROFILE_BO extends GenerieAop {
	private BANK_GROUP_BO bank_group_bo;
	private SD_COMPANY_PROFILE_Dao sd_com_Dao;
	private SD_COMPANY_PROFILE_HIS_Dao sd_com_his_Dao;
	private EACH_USERLOG_Dao userLog_Dao;
	private FEE_CODE_NW_BO fee_code_nw_bo;
	private FEE_CODE_BO fee_code_bo;
	private SYS_PARA_BO sys_para_bo;
	private Logger logger = Logger.getLogger(this.getClass().getName());

	/**
	 * 自SD_COMPANY_PROFILE檔中找出發動行所屬總行清單
	 * 
	 * @return
	 */
	public List<LabelValueBean> getSdBgbkIdList() {
		// 20150629 edit by hugo req by 李建利
		// List<LabelValueBean> beanList = new LinkedList<LabelValueBean>();
		// List<Map<String, String>> result = sd_com_Dao.getSdBgbkIdList();
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

	public Map<String, String> delete(SD_Company_Profile_Form sd_com_form) {
		String com_id = sd_com_form.getCOMPANY_ID();
		String txn_id = sd_com_form.getTXN_ID();
		String brbkid = sd_com_form.getSND_BRBK_ID();
		Map<String, String> map = null;
		Map<String, String> pkmap = new HashMap<String, String>();// e
		SD_COMPANY_PROFILE po = null;
		EACH_USERLOG userlog_po = null;
		try {
			map = new HashMap<String, String>();
			pkmap.put("COMPANY_ID", com_id);
			pkmap.put("TXN_ID", txn_id);
			pkmap.put("SND_BRBK_ID", brbkid);
			
			
			//刪除 SD_HIS START
			SD_COMPANY_PROFILE_HIS hispo = new SD_COMPANY_PROFILE_HIS();
			List<Map<String, String>> delList = sd_com_his_Dao.findByCondition(com_id,txn_id,brbkid);
			for(Map<String, String> deldata:delList) {
				hispo = CodeUtils.objectCovert(SD_COMPANY_PROFILE_HIS.class, deldata);
				
				sd_com_his_Dao.remove(hispo);
			}
			
			//刪除 SD_HIS END
			
			
			SD_COMPANY_PROFILE_PK id = new SD_COMPANY_PROFILE_PK(com_id, txn_id, brbkid);
			po = sd_com_Dao.get(id);
			if (po == null) {
				map = sd_com_Dao.removeFail(po, pkmap, "刪除失敗，查無資料", 1);
				return map;
			}
			po.setCOMPANY_ID(com_id);
			po.setTXN_ID(txn_id);
			po.setSND_BRBK_ID(brbkid);
			po.setBRBK_NAME(sd_com_form.getSND_BRBK_NAME());

			// sd_com_Dao.removeII(po, pkmap);
			sd_com_Dao.remove(po);

			String result = "{文號=" + po.getCASE_NO() + ", 啟用日期=" + po.getACTIVE_DATE() + ", 停用日期=" + po.getSTOP_DATE()
					+ ", 發動分行代號=" + po.getSND_BRBK_ID() + ", 發動者簡稱=" + po.getCOMPANY_ABBR_NAME() + ", 發動者統一編號="
					+ po.getCOMPANY_ID() + ", 分行名稱=" + po.getBRBK_NAME() + ", 建立日期=" + po.getCDATE() + ", 聯絡人資訊="
					+ po.getCONTACT_INFO() + ", 異動日期=" + po.getUDATE() + ", 交易代號=" + po.getTXN_ID() + ", 發文日期="
					+ po.getDISPATCH_DATE() + ", 用戶號碼=" + po.getUSER_NO() + ", 發動者名稱=" + po.getCOMPANY_NAME()
					+ ", 開辦日期=" + po.getSTART_DATE() + "}";
			userlog_po = getUSERLOG("D", "");
			userlog_po.setADEXCODE("刪除成功，PK={發動分行代號=" + po.getSND_BRBK_ID() + ", 交易代號=" + po.getTXN_ID() + ", 發動者統一編號="
					+ po.getCOMPANY_ID() + "} ");
			userlog_po.setBFCHCON(result);
			userLog_Dao = SpringAppCtxHelper.getBean("userLog_Dao");

			userLog_Dao.aop_save(userlog_po);

			map.put("result", "TRUE");
			map.put("msg", "刪除成功");
			map.put("target", "search");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			map = sd_com_Dao.removeFail(po, pkmap, "刪除失敗，系統異常", 2);
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
		EACH_USERLOG userlog_po = null;
		EACH_USERLOG userlog_po2 = null;
		
		logger.debug("SD_Save Start");
		try {
			
			userLog_Dao = SpringAppCtxHelper.getBean("userLog_Dao");
			//新增主檔區 START
			
			map = new HashMap<String, String>();
			SD_COMPANY_PROFILE_PK id = new SD_COMPANY_PROFILE_PK(com_id, txn_id, brbkid);
			SD_COMPANY_PROFILE po = sd_com_Dao.get(id);
			if (po != null) {
				map.put("result", "FALSE");
				map.put("msg", "儲存失敗，id重複");
				map.put("target", "add_p");
				return map;
			}
			po = new SD_COMPANY_PROFILE();
			BeanUtils.populate(po, formMap);
			po.setBRBK_NAME(formMap.get("SND_BRBK_NAME").toString());
			po.setId(id);
			String cdate = zDateHandler.getTheDateII();
			po.setCDATE(cdate);
			sd_com_Dao.aop_save(po);

			String result = "{文號=" + po.getCASE_NO() + ", 啟用日期=" + po.getACTIVE_DATE() + ", 停用日期=" + po.getSTOP_DATE()
					+ ", 手續費啟用日=" + po.getFEE_TYPE_ACTIVE_DATE() +", 收費類型=" + po.getFEE_TYPE()
					+ ", 發動分行代號=" + po.getSND_BRBK_ID() + ", 發動者簡稱=" + po.getCOMPANY_ABBR_NAME() + ", 發動者統一編號="
					+ po.getCOMPANY_ID() + ", 分行名稱=" + po.getBRBK_NAME() + ", 建立日期=" + po.getCDATE() + ", 聯絡人資訊="
					+ po.getCONTACT_INFO() + ", 異動日期=" + "" + ", 交易代號=" + po.getTXN_ID() + ", 發文日期="
					+ po.getDISPATCH_DATE() + ", 用戶號碼=" + po.getUSER_NO() + ", 發動者名稱=" + po.getCOMPANY_NAME()
					+ ", 開辦日期=" + po.getSTART_DATE() +"}";
			userlog_po = getUSERLOG("A", "");
			userlog_po.setADEXCODE("新增成功");
			userlog_po.setAFCHCON(result);

			userLog_Dao.aop_save(userlog_po);
			
			logger.debug("SD_Save END");
			
		//新增主檔區 END
			
		//新增歷程檔區 START
			logger.debug("SD_HIS_Save Start");
			SD_COMPANY_PROFILE_HIS hispo = new SD_COMPANY_PROFILE_HIS();
			hispo.setSND_BRBK_ID(brbkid);
			hispo.setCOMPANY_ID(com_id);
			hispo.setTXN_ID(txn_id);
			hispo.setACTIVE_DATE(po.getFEE_TYPE_ACTIVE_DATE());
			hispo.setFEE_TYPE(po.getFEE_TYPE());
			hispo.setUDATE(cdate);

			String result2 =  "{ 交易代號=" + hispo.getTXN_ID() 
							+ ", 發動者統一編號=" + hispo.getCOMPANY_ID()
							+", 發動分行代號=" + hispo.getSND_BRBK_ID() 
							+", 收費類型=" + hispo.getFEE_TYPE()
							+", 手續費啟用日=" + hispo.getACTIVE_DATE() 
							+", 建立日期=" + hispo.getUDATE() + "}";
			
			sd_com_his_Dao.save(hispo);
			userlog_po2 = getUSERLOG("A", "");
			userlog_po2.setAFCHCON(result2);
			userlog_po2.setADEXCODE("新增代收歷程成功");
			
			userLog_Dao.aop_save(userlog_po2);
			
			logger.debug("SD_HIS_Save END");
		
		//新增歷程檔區 END	
			
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
		EACH_USERLOG userlog_po = null;
		EACH_USERLOG userlog_po2 = null;
		SD_COMPANY_PROFILE po = null;
		
		logger.debug("SD_Update Start");
		try {
			userLog_Dao = SpringAppCtxHelper.getBean("userLog_Dao");
			
			SD_COMPANY_PROFILE_PK id = new SD_COMPANY_PROFILE_PK(com_id, txn_id, brbkid);
			System.out.println("com_id=" + com_id + " ,txn_id=" + txn_id + " ,brbkid=" + brbkid);
			po = sd_com_Dao.get(id);
			
			
			map = new HashMap<String, String>();
			
			System.out.println("OLD PO >> :" + po.toString());
			pkmap = BeanUtils.describe(id);
			if (po == null) {
				BeanUtils.populate(po, formMap);
				map = sd_com_Dao.updateFail(po, oldmap, pkmap, "儲存失敗，查無資料", 1);
				return map;
			}
			
			// 新增歷程檔區 START
			// 修改先新增HIS表 
			// 如果 formMap 的手續費類型 或 手續費啟用日 跟主表比有變  , 代表需要更新歷程檔  , 將"新的記錄"到HIS檔裡 !
			if(!po.getFEE_TYPE().equalsIgnoreCase((String) formMap.get("FEE_TYPE"))||
			   !po.getFEE_TYPE_ACTIVE_DATE().equalsIgnoreCase((String) formMap.get("FEE_TYPE_ACTIVE_DATE"))) {
				logger.debug("SD_HIS_Update Start");
				try {
					SD_COMPANY_PROFILE_HIS hispo = new SD_COMPANY_PROFILE_HIS();
					hispo.setCOMPANY_ID(po.getId().getCOMPANY_ID());
					hispo.setTXN_ID(po.getId().getTXN_ID());
					hispo.setSND_BRBK_ID(po.getId().getSND_BRBK_ID());
					hispo.setACTIVE_DATE((String) formMap.get("FEE_TYPE_ACTIVE_DATE"));
					hispo.setFEE_TYPE((String) formMap.get("FEE_TYPE"));
					hispo.setUDATE(zDateHandler.getTheDateII());

					sd_com_his_Dao.save(hispo);
					
					String result2 =  "{ 交易代號=" + hispo.getTXN_ID() 
					+ ", 發動者統一編號=" + hispo.getCOMPANY_ID()
					+", 發動分行代號=" + hispo.getSND_BRBK_ID() 
					+", 收費類型=" + hispo.getFEE_TYPE()
					+", 手續費啟用日=" + hispo.getACTIVE_DATE() 
					+", 建立日期=" + hispo.getUDATE() + "}";
					
					userlog_po2 = getUSERLOG("A", "");
					userlog_po2.setAFCHCON(result2);
					userlog_po2.setADEXCODE("新增代收歷程成功");
					
					userLog_Dao.aop_save(userlog_po2);
					logger.debug("HIS UPDATE SUCCESS");
				} catch (Exception e) {
					logger.error("HIS UPDATE ERROR :" + e.getMessage());
				}
				logger.debug("SD_HIS_Update END");
			}else {
				logger.debug("NO NEED TO UPDATE SD_HIS");
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
			po = new SD_COMPANY_PROFILE();
			BeanUtils.populate(po, formMap);
			po.setId(id);
			po.setUDATE(zDateHandler.getTheDateII());
			po.setCDATE((String) oldmap.get("CDATE"));
			//從批量修改來 忽略ACTIVE DATE
			if(ignoreActiveDate) {
				po.setACTIVE_DATE(oldmap.get("ACTIVE_DATE"));
			}
			
			sd_com_Dao.aop_save(po);
			
			String BFCHCON = "";
			String AFCHCON = "";

			String udate = oldmap.get("UDATE") == null ? "" : oldmap.get("UDATE");
			String bfc = "文號=" + oldmap.get("CASE_NO") + ",啟用日期=" + oldmap.get("ACTIVE_DATE") + ",停用日期="
					+ oldmap.get("STOP_DATE") + ",發動分行代號=" + oldmap.get("SND_BRBK_ID") + ",發動者簡稱="
					+ oldmap.get("COMPANY_ABBR_NAME") + ",發動者統一編號=" + oldmap.get("COMPANY_ID") + ",分行名稱="
					+ oldmap.get("BRBK_NAME") + ",建立日期=" + oldmap.get("CDATE") + ",聯絡人資訊=" + oldmap.get("CONTACT_INFO")
					+ ",異動日期=" + udate + ",交易代號=" + oldmap.get("TXN_ID") + ",發文日期=" + oldmap.get("DISPATCH_DATE")
					+ ",用戶號碼=" + oldmap.get("USER_NO") + ",發動者名稱=" + oldmap.get("COMPANY_NAME") + ",開辦日期="
					+ oldmap.get("START_DATE");
			String afc = "文號=" + po.getCASE_NO() + ",啟用日期=" + po.getACTIVE_DATE() + ",停用日期=" + po.getSTOP_DATE()
					+ ",發動分行代號=" + po.getSND_BRBK_ID() + ",發動者簡稱=" + po.getCOMPANY_ABBR_NAME() + ",發動者統一編號="
					+ po.getCOMPANY_ID() + ",分行名稱=" + po.getBRBK_NAME() + ",建立日期=" + po.getCDATE() + ",聯絡人資訊="
					+ po.getCONTACT_INFO() + ",異動日期=" + po.getUDATE() + ",交易代號=" + po.getTXN_ID() + ",發文日期="
					+ po.getDISPATCH_DATE() + ",用戶號碼=" + po.getUSER_NO() + ",發動者名稱=" + po.getCOMPANY_NAME() + ",開辦日期="
					+ po.getSTART_DATE();

			String[] b = bfc.split(",");
			String[] a = afc.split(",");

			for (int i = 0; i <= b.length - 1; i++) {
				if (!b[i].equals(a[i])) {
					if (i != b.length - 1) {
						BFCHCON += b[i] + ", ";
						AFCHCON += a[i] + ", ";
					} else {
						BFCHCON += b[i];
						AFCHCON += a[i];
					}
				}
			}

			userlog_po = getUSERLOG("B", "");
			userlog_po.setADEXCODE("修改-儲存成功，PK={發動分行代號=" + po.getSND_BRBK_ID() + ", 交易代號=" + po.getTXN_ID()
					+ ", 發動者統一編號=" + po.getCOMPANY_ID() + "} ");
			userlog_po.setBFCHCON("{" + BFCHCON + "}");
			userlog_po.setAFCHCON("{" + AFCHCON + "}");

			userLog_Dao.aop_save(userlog_po);
			
			//修改主檔區 END
			
			
			map.put("result", "TRUE");
			map.put("msg", "儲存成功");
			map.put("target", "search");
		
		} catch (Exception e) {
			System.out.println(e);
			map = sd_com_Dao.updateFail(po, oldmap, pkmap, "儲存失敗，查無資料", 2);
			return map;
		}
		return map;
	}

	public List<SD_COMPANY_PROFILE> searchBySenderHead(String com_id, String txn_id, String fee_type, String senderHead,
			String comName, String orderSQL) {
		List<SD_COMPANY_PROFILE> list = new ArrayList<SD_COMPANY_PROFILE>();
		if (StrUtils.isEmpty(com_id) && StrUtils.isEmpty(txn_id) && StrUtils.isEmpty(fee_type)
				&& StrUtils.isEmpty(senderHead) && StrUtils.isEmpty(comName)) {
			// list = sd_com_Dao.getAll();
			list = sd_com_Dao.getData("", new ArrayList<String>());
		} else {
			StringBuffer sql = new StringBuffer();
			List<String> strList = new LinkedList<String>();
			Map<String, String> map = new HashMap<String, String>();
			map.put("COMPANY_ID", com_id.trim());
			map.put("TXN_ID", txn_id.trim());
			map.put("B.BGBK_ID", senderHead.trim());
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
			System.out.println("SD_COMPANY_PROFILE.condition>>" + sql);
			list = sd_com_Dao.getData(sql.toString(), strList, orderSQL);
		}
		// System.out.println("SD_COMPANY_PROFILE.list>>"+JSONUtils.toJson(list));
		List rslist = new ArrayList<>();
		if (list.size() != 0) {
			for (SD_COMPANY_PROFILE po : list) {
				// 20201012修改 FEE_TYPE補植 改邏輯
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

				// 舊的
				// //... 將FEE_TYPE=""的資料去舊表找出TYPE
				// if(" ".equals(po.getFEE_TYPE())) {
				// po.setFEE_TYPE(fee_code_bo.checkFeeCodeType(po.getTXN_ID())+"o");
				// }
				//
				// if ("A".equals(po.getFEE_TYPE())||"Ao".equals(po.getFEE_TYPE())) {
				// po.setFEE_TYPE_CHT("固定");
				// } else if ("B".equals(po.getFEE_TYPE())||"Bo".equals(po.getFEE_TYPE())) {
				// po.setFEE_TYPE_CHT("外加");
				// } else if ("C".equals(po.getFEE_TYPE())) {
				// po.setFEE_TYPE_CHT("百分比");
				// } else if ("D".equals(po.getFEE_TYPE())) {
				// po.setFEE_TYPE_CHT("級距");
				// }
				//
				// if(!StrUtils.isEmpty(fee_type)) {
				// if(po.getFEE_TYPE().equals(fee_type+"o")||po.getFEE_TYPE().equals(fee_type))
				// {
				// rslist.add(po);
				// }
				// }else {
				// rslist.add(po);
				// }
			}
		}
		return rslist;
	}

	public List<SD_COMPANY_PROFILE> searchBySndBrbkId(String com_id, String txn_id, String sndBrbkId, String fee_type,
			String comName) {
		List<SD_COMPANY_PROFILE> list = new ArrayList<SD_COMPANY_PROFILE>();
		if (StrUtils.isEmpty(com_id) && StrUtils.isEmpty(txn_id) && StrUtils.isEmpty(sndBrbkId)
				&& StrUtils.isEmpty(fee_type) && StrUtils.isEmpty(comName)) {
			// list = sd_com_Dao.getAll();
			list = sd_com_Dao.getData("", new ArrayList<String>());
		} else {
			StringBuffer sql = new StringBuffer();
			List<String> strList = new LinkedList<String>();
			Map<String, String> map = new HashMap<String, String>();
			map.put("COMPANY_ID", StrUtils.isEmpty(com_id) ? "" : com_id.trim());
			map.put("TXN_ID", StrUtils.isEmpty(txn_id) ? "" : txn_id.trim());
			map.put("SND_BRBK_ID", StrUtils.isEmpty(sndBrbkId) ? "" : sndBrbkId.trim());
			try {
				System.out.println(java.net.URLDecoder.decode(comName.trim(), "UTF-8"));
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
					} else {
						sql.append(key + " = ?");
						strList.add(map.get(key));
					}

					i++;
				}
			}
			System.out.println("SD_COMPANY_PROFILE.condition>>" + sql);
			list = sd_com_Dao.getData(sql.toString(), strList);
		}
		// System.out.println("SD_COMPANY_PROFILE.list>>"+JSONUtils.toJson(list));
		list = list == null ? null : list.size() == 0 ? null : list;
		List rslist = new ArrayList<>();
		if (list.size() != 0) {
			for (SD_COMPANY_PROFILE po : list) {
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
				// // ... 將FEE_TYPE=""的資料去舊表找出TYPE
				// if (" ".equals(po.getFEE_TYPE())) {
				// po.setFEE_TYPE(fee_code_bo.checkFeeCodeType(po.getTXN_ID()) + "o");
				// }
				//
				// if ("A".equals(po.getFEE_TYPE()) || "Ao".equals(po.getFEE_TYPE())) {
				// po.setFEE_TYPE_CHT("固定");
				// } else if ("B".equals(po.getFEE_TYPE()) || "Bo".equals(po.getFEE_TYPE())) {
				// po.setFEE_TYPE_CHT("外加");
				// } else if ("C".equals(po.getFEE_TYPE())) {
				// po.setFEE_TYPE_CHT("百分比");
				// } else if ("D".equals(po.getFEE_TYPE())) {
				// po.setFEE_TYPE_CHT("級距");
				// }
				//
				// if (!StrUtils.isEmpty(fee_type)) {
				// if (po.getFEE_TYPE().equals(fee_type + "o") ||
				// po.getFEE_TYPE().equals(fee_type)) {
				// rslist.add(po);
				// }
				// } else {
				// rslist.add(po);
				// }
			}
		}
		return rslist.size() == 0 ? null : rslist;
	}

	public String search_toJson(Map<String, String> param) {
		String com_id = StrUtils.isNotEmpty(param.get("COMPANY_ID")) ? param.get("COMPANY_ID") : "";
		String fee_type = StrUtils.isNotEmpty(param.get("FEE_TYPE")) ? param.get("FEE_TYPE") : "";
		String txn_id = StrUtils.isNotEmpty(param.get("TXN_ID")) ? param.get("TXN_ID") : "";
		String senderHead = StrUtils.isNotEmpty(param.get("SENDERHEAD")) ? param.get("SENDERHEAD") : "";
		String comName = StrUtils.isNotEmpty(param.get("COMPANY_NAME")) ? param.get("COMPANY_NAME") : "";
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
		if (fee_type.equals("all")) {
			fee_type = "";
		}
		if (senderHead.equals("all")) {
			senderHead = "";
		}
		if (comName.equals("all")) {
			comName = "";
		}
		// 20150128 HUANGPU 改以總行查詢
		// String brbkId =StrUtils.isNotEmpty(param.get("SND_BRBK_ID"))?
		// param.get("SND_BRBK_ID"):"";
		// String jsonStr = JSONUtils.toJson(search(com_id, txn_id, brbkId, comName));
		String jsonStr = "";
		List<SD_COMPANY_PROFILE> list = searchBySenderHead(com_id, txn_id, fee_type, senderHead, comName, orderSQL);
		if (list.toString().equals("[]")) {
			jsonStr = "{}";
		} else {
			jsonStr = JSONUtils.toJson(list);
		}
		// jsonStr = StrUtils.isEmpty(jsonStr) ?"{}":jsonStr;
		System.out.println("jsonStr" + jsonStr);
		return jsonStr;
	}

	public SD_COMPANY_PROFILE getByPk(String com_id, String txn_id, String brbkid) {
		SD_COMPANY_PROFILE po = null;
		try {
			if (StrUtils.isNotEmpty(com_id) && StrUtils.isNotEmpty(txn_id) && StrUtils.isNotEmpty(brbkid)) {
				po = sd_com_Dao.get(new SD_COMPANY_PROFILE_PK(com_id, txn_id, brbkid));
				po.setTXN_ID(txn_id);
				po.setCOMPANY_ID(com_id);
				po.setSND_BRBK_ID(brbkid);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return po;
	}
	
	// ReadXL 匯入excel文件

	// 檢核文件內容
	public List<String> validate(String filePath) throws Exception {
		List<String> errorList = new ArrayList<String>();
		String bizdate = bank_group_bo.getEachsysstatustab_bo().getThisBusinessDate();
//		String bizdate = (Integer.parseInt(bizdate_w.substring(0,4))-1911)+bizdate_w.substring(4);
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

			if (cellValue.getBytes("UTF-8").length > 400) {
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
				if (cellValue.getBytes("UTF-8").length > 60) {
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
				if (cellValue.getBytes("UTF-8").length > 7) {
					errorList.add("第" + i + "筆   發動行代號 錯誤 ");
					System.out.println("第" + i + "筆   發動行代號 錯誤 ");
				}
			}
			lnchrBkId = cellValue;
			if (lnchrBkId.equals("") || lnchrBkId == null) {
				errorList.add("第" + i + "筆   發動行代號 錯誤 ");
			}

			// 用戶號碼
			cell = row.getCell(7);
			if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
				cellValue = cell.getStringCellValue();
			} else {
				errorList.add("第" + i + "筆   用戶號碼 錯誤 ");
			}
			if (StringUtils.isNotEmpty(cellValue)) {
				if (cellValue.getBytes("UTF-8").length > 200) {
					errorList.add("第" + i + "筆   用戶號碼 錯誤 ");
					System.out.println("第" + i + "筆   用戶號碼 錯誤 ");
				}
			}

			// 聯絡人姓名及電話
			cell = row.getCell(8);
			if (cell != null) {
				cellValue = cell.getStringCellValue();
				if (StringUtils.isNotEmpty(cellValue)) {
					if (cellValue.getBytes("UTF-8").length > 200) {
						errorList.add("第" + i + "筆   聯絡人姓名及電話 錯誤 ");
						System.out.println("第" + i + "筆   聯絡人姓名及電話 錯誤 ");
					}
				}
			} else {
				errorList.add("檔案格式錯誤" + "'" + cellValue + "'");
				return errorList;
			}

			// 開辦日期
			cell = row.getCell(9);
			if (cell != null) {
				cellValue = cell.getStringCellValue();
				if (cellValue.getBytes("UTF-8").length > 60) {
					errorList.add("第" + i + "筆   開辦日期 錯誤  " + cellValue.getBytes("UTF-8").length);
					System.out.println("第" + i + "筆   開辦日期 錯誤 ");
				}
			} else {
				errorList.add("檔案格式錯誤" + "'" + cellValue + "'");
				return errorList;
			}

			// 交換所發文字號
			cell = row.getCell(10);
			if (cell != null) {
				cellValue = cell.getStringCellValue();
				if (StringUtils.isNotEmpty(cellValue)) {
					if (cellValue.length() < 9) {
						errorList.add("第" + i + "筆   交換所發文字號 錯誤 ");
						System.out.println("第" + i + "筆   交換所發文字號 錯誤 ");
					} else {
						// String issueDate = cellValue.substring(0, 9);
						// try {
						// issueDate = StringUtils.replace(issueDate, ".", "");
						//// System.out.println(issueDate);
						//
						// String year = StringUtils.substring(issueDate, 0, 3);
						// int intYear = Integer.parseInt(year) + 1911;
						//
						// String mmdd = issueDate.substring(3, issueDate.length());
						// issueDate = intYear + mmdd;
						// SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
						// format.parse(issueDate);
						// } catch (Exception e) {
						// errorList.add("第"+i+"筆 交換所發文字號 錯誤 ");
						// System.out.println("第"+i+"筆 交換所發文字號 錯誤 ");
						// }

						String issueNo = cellValue.substring(9, cellValue.length());
						if (issueNo.getBytes("UTF-8").length > 200) {
							errorList.add("第" + i + "筆   交換所發文字號 錯誤 ");
							System.out.println("第" + i + "筆   交換所發文字號 錯誤 ");
						}
					}
				}
			} else {
				errorList.add("檔案格式錯誤" + "'" + cellValue + "'");
				return errorList;
			}
			
			String fee_type_active_date = null;
			String fee_type = null;
			String active_date = null;
			
			// 手續費啟用日
			cell = row.getCell(11);
			if (cell != null) {
				
				if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
					cellValue = String.valueOf((int) cell.getNumericCellValue());
				} else {
					cellValue = cell.getStringCellValue();
				}
				
				if (StringUtils.isNotEmpty(cellValue)) {
					//長度必須為8 YYYYMMDD
					if (cellValue.length() != 8) {
						errorList.add("第" + i + "筆   手續費啟用日 錯誤 需為8碼 ");
						System.out.println("第" + i + "筆   手續費啟用日 錯誤 需為8碼 ");
					}else{
						
						DateFormat formatter = new SimpleDateFormat("yyyyMMdd");
						try{
							Date date = formatter.parse(cellValue);
							if(Integer.valueOf(cellValue.substring(0, 4))-1911>0) {
								errorList.add("第" + i + "筆   手續費啟用日 格式錯誤 需為民國年 ");
								System.out.println("第" + i + "筆   手續費啟用日 格式錯誤 需為民國年 ");
				            }else if(Integer.valueOf(cellValue.substring(4, 6)) > 12 ||
				            		 Integer.valueOf(cellValue.substring(4, 6)) < 0){
				            	errorList.add("第" + i + "筆   手續費啟用日 月格式錯誤 ");
								System.out.println("第" + i + "筆   手續費啟用日 月格式錯誤 ");
				            }else if(Integer.valueOf(cellValue.substring(6, 8)) > 31 || 
				            		 Integer.valueOf(cellValue.substring(6, 8)) < 0){
				            	errorList.add("第" + i + "筆   手續費啟用日 日格式錯誤 ");
								System.out.println("第" + i + "筆   手續費啟用日 日格式錯誤 ");
				            }
						}catch (Exception e){
							errorList.add("第" + i + "筆   手續費啟用日 日期格式錯誤 ");
							System.out.println("第" + i + "筆   手續費啟用日 日期格式錯誤 ");
						}
					}
				} else {
					errorList.add("第" + i + "筆   手續費啟用日 錯誤 ");
					System.out.println("第" + i + "筆   手續費啟用日 錯誤 ");
				}
				
				fee_type_active_date = cellValue;
			} else {
				errorList.add("第" + i + "筆  手續費啟用日 檔案格式錯誤");
			}
			
			// 業者啟用日
			cell = row.getCell(12);
			if (cell != null) {
				
				if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
					cellValue = String.valueOf((int) cell.getNumericCellValue());
				} else {
					cellValue = cell.getStringCellValue();
				}
				
				if (StringUtils.isNotEmpty(cellValue)) {
					//長度必須為8 YYYYMMDD
					if (cellValue.length() != 8) {
						errorList.add("第" + i + "筆    業者啟用日 錯誤 需為8碼 ");
						System.out.println("第" + i + "筆    業者啟用日 錯誤 需為8碼 ");
					}else{
						
						DateFormat formatter = new SimpleDateFormat("yyyyMMdd");
						try{
							Date date = formatter.parse(cellValue);
							if(Integer.valueOf(cellValue.substring(0, 4))-1911>0) {
								errorList.add("第" + i + "筆    業者啟用日 格式錯誤 需為民國年 ");
								System.out.println("第" + i + "筆    業者啟用日 格式錯誤 需為民國年 ");
				            }else if(Integer.valueOf(cellValue.substring(4, 6)) > 12 ||
				            		 Integer.valueOf(cellValue.substring(4, 6)) < 0){
				            	errorList.add("第" + i + "筆    業者啟用日 月格式錯誤 ");
								System.out.println("第" + i + "筆    業者啟用日 月格式錯誤 ");
				            }else if(Integer.valueOf(cellValue.substring(6, 8)) > 31 || 
				            		 Integer.valueOf(cellValue.substring(6, 8)) < 0){
				            	errorList.add("第" + i + "筆    業者啟用日 日格式錯誤 ");
								System.out.println("第" + i + "筆    業者啟用日 日格式錯誤 ");
				            }
						}catch (Exception e){
							errorList.add("第" + i + "筆    業者啟用日 日期格式錯誤 ");
							System.out.println("第" + i + "筆    業者啟用日 日期格式錯誤 ");
						}
					}
				} else {
					errorList.add("第" + i + "筆    業者啟用日 錯誤 ");
					System.out.println("第" + i + "筆    業者啟用日 錯誤 ");
				}
				
				active_date = cellValue;

			} else {
				errorList.add("第" + i + "筆  業者啟用日 檔案格式錯誤");
			}

			if (StringUtils.isNotEmpty(fee_type_active_date) && StringUtils.isNotEmpty(active_date)) {
				if (Integer.valueOf(active_date) < Integer.valueOf(bizdate)
						|| Integer.valueOf(fee_type_active_date) < Integer.valueOf(bizdate)) {
					errorList.add("第" + i + "筆   日期檢核失敗：業者啟用日及手續費啟用日 不可小於營業日");
				} else {
					// 2.兩日期 須相同
					if (!active_date.equals(fee_type_active_date)) {
						errorList.add("第" + i + "筆   日期檢核失敗：業者啟用日與手續費啟用日不相同");
					}
				}
			}

			// 收費類型
			cell = row.getCell(13);
			if (cell != null) {
				if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
					cellValue = cell.getStringCellValue();
				} else {
					errorList.add("第" + i + "筆   收費類型 錯誤 ");
				}

				if (StringUtils.isNotEmpty(cellValue)) {
					if (cellValue.length() > 1) {
						errorList.add("第" + i + "筆   收費類型 錯誤 ");
						System.out.println("第" + i + "筆   收費類型 錯誤 ");
					}
					List<FEE_CODE_NW> typeList = fee_code_nw_bo.checkActive(txId, cellValue);
					boolean flag = false;
					for (FEE_CODE_NW data : typeList) {
						if (Integer.parseInt(fee_type_active_date) >= Integer.parseInt(data.getSTART_DATE())) {
							flag = true;
						}
					}
					if (!flag) {
						errorList.add("第" + i + "筆   此交易代號無此收費類型 或 尚未啟用 ");
						System.out.println("第" + i + "筆   此交易代號無此收費類型  或 尚未啟用 ");
					}
				}
			} else {
				errorList.add("第" + i + "筆  收費類型 檔案格式錯誤");
			}
		}

		return errorList;

	}

	// 文件匯入db
	public List<String> import_db(String path) throws Exception {

		HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(path));
		HSSFSheet sheet = wb.getSheetAt(0);

		SD_Company_Profile_Form form = new SD_Company_Profile_Form();
		List<SD_COMPANY_PROFILE> list = null;
		EACH_USERLOG userlog_po = null;
		int count = 0;
		List<String> alertList = new ArrayList<String>();
		String dbResult="";
		String bizdate = "";
		// 20151112 add by hugo req by UAT-20151027-02 抓營業日即可 且不管是否換日 以當下匯入的營業日期為準
		bizdate = bank_group_bo.getEachsysstatustab_bo().getBusinessDate();
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

			// 用戶號碼
			cell = row.getCell(7);
			cellValue = cell.getStringCellValue();
			cellValue = removeEnterKey(cellValue);
			form.setUSER_NO(cellValue);

			// 聯絡人姓名及電話
			cell = row.getCell(8);
			cellValue = cell.getStringCellValue();
			cellValue = removeEnterKey(cellValue);
			form.setCONTACT_INFO(cellValue);

			// 開辦日期
			cell = row.getCell(9);
			cellValue = cell.getStringCellValue();
			cellValue = removeEnterKey(cellValue);
			form.setSTART_DATE(cellValue);

			// 交換所發文字號
			cell = row.getCell(10);
			cellValue = cell.getStringCellValue();
			cellValue = removeEnterKey(cellValue);
			form.setCASE_NO(cellValue);

			// 手續費啟用日
			cell = row.getCell(11);
			String fee_type_active_date = null;
			if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
				cellValue = String.valueOf((int) cell.getNumericCellValue());
			} else {
				cellValue = cell.getStringCellValue();
			}
			cellValue = removeEnterKey(cellValue);
			fee_type_active_date = cellValue;
			form.setFEE_TYPE_ACTIVE_DATE(cellValue);
			
			
			// 業者啟用日
			cell = row.getCell(12);
			String active_date = null;
			if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
				cellValue = String.valueOf((int) cell.getNumericCellValue());
			} else {
				cellValue = cell.getStringCellValue();
			}
			cellValue = removeEnterKey(cellValue);
			active_date = cellValue;
			form.setACTIVE_DATE(cellValue);

			
			// 收費類型
			 cell = row.getCell(13);
			 cellValue = cell.getStringCellValue();
			 cellValue = removeEnterKey(cellValue);
			 String feeType = cellValue;
			 form.setFEE_TYPE(cellValue);
			 
			 
			// if (StringUtils.isNotEmpty(cellValue)) {
			// int loc = cellValue.indexOf("台");
			// //截去日期 ex:104.05.04
			// form.setCASE_NO(cellValue.substring(loc));
			//
			// }

			SD_COMPANY_PROFILE_PK id = new SD_COMPANY_PROFILE_PK(lnchrId, txId, lnchrBkId);
			SD_COMPANY_PROFILE po = sd_com_Dao.get(id);
			
			
			String way = "insert";
			
			if (po != null) {
				//update
				way="update";
				if(form.getFEE_TYPE().equals(po.getFEE_TYPE())&&form.getFEE_TYPE_ACTIVE_DATE().equals(po.getFEE_TYPE_ACTIVE_DATE())&&form.getACTIVE_DATE().equals(po.getACTIVE_DATE())) {
					//三者皆同  沒事兒就覆蓋
				}else {
					//只要一者不同  覆蓋並提示
					form.setFEE_TYPE(po.getFEE_TYPE());
					form.setFEE_TYPE_ACTIVE_DATE(po.getFEE_TYPE_ACTIVE_DATE());
					form.setACTIVE_DATE(po.getACTIVE_DATE());
					alertList.add("第"+i+"筆  發動者統編:"+form.getCOMPANY_ID()+", 交易代號:"+form.getTXN_ID()+", 發動行代號:"+form.getSND_BRBK_ID());
				}
				count += 1;
			}
			
			Map map = insertDB(form.getCOMPANY_ID(), form.getTXN_ID(), form.getSND_BRBK_ID(), BeanUtils.describe(form),way);
			
		}
		
		if(alertList.size()>0) {
			alertList.add(" 收費類型 或 手續費啟用日 或 業者啟用日 與管理網站設定不同 , 請至網站更新 ");
		}
		
		dbResult = "共新增" + ((sheet.getLastRowNum() - 2) - count) + "筆資料，覆蓋" + count + "筆資料，需手動修改"+alertList.size()+"筆資料"; 																				// 除了table本身以外要再扣頭兩行header
		alertList.add(0, dbResult);
		
		userlog_po = getUSERLOG("W", "");
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
	public boolean addTmpFile(SD_Company_Profile_Form sd_com_form, String filePath) throws Exception {
		// struts的File物件
		FormFile file = sd_com_form.getFILE();

		// 檔名
		String fileName = file.getFileName();
		// 檔案大小
		int fileSize = file.getFileSize();

		if (!("").equals(fileName)) {

			System.out.println("Server path:" + filePath);
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
			SD_COMPANY_PROFILE_PK id = new SD_COMPANY_PROFILE_PK(com_id, txn_id, brbkid);
			SD_COMPANY_PROFILE po = sd_com_Dao.get(id);
			if(po != null ){
				cdate=StrUtils.isNotEmpty(po.getCDATE())?po.getCDATE():"";
				cdateposition = StrUtils.isNotEmpty(po.getCDATE())?"C":"X";
			}
			// if(po != null ){
			// map.put("result", "FALSE");
			// map.put("msg", "儲存失敗，id重複");
			// map.put("target", "add_p");
			// return map;
			// }
			po = new SD_COMPANY_PROFILE();
			SD_COMPANY_PROFILE_HIS hispo = new SD_COMPANY_PROFILE_HIS();
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
				sd_com_his_Dao.save(hispo);
				
			}else if("update".equals(way)){
				po.setCDATE(cdate);
				po.setUDATE(zDateHandler.getTheDateII());
			}
			
			sd_com_Dao.aop_save(po);
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
			list = sd_com_Dao.checkCOMPANY_ID(companyId);
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
			result = sd_com_Dao.checkCOMPANY_NAME(companyId);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public String checkAmount(Map<String, String> params) {
		String result = "";
		String companyId = params.get("COMPANY_ID");
		try {
			result = sd_com_Dao.checkAmount(companyId);
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
			result = sd_com_Dao.checkSDAmount(companyId, txn_id, fee_type,snd_brbk_id);
		}

		return result;
	}
	
	public String check_min_fee_type_active_date(Map<String, String> params) {
		
		String companyId = params.get("COMPANY_ID");
		String txn_id = params.get("TXN_ID");
		String snd_brbk_id = params.get("SND_BRBK_ID");
		String active_date = params.get("ACTIVE_DATE");
		String mindate="";
		String result="";
		Map rtnMap = new HashMap();
		mindate =sd_com_his_Dao.getMiniDate_of_his(companyId,txn_id,snd_brbk_id);
		if(Integer.parseInt(active_date)>=Integer.parseInt(mindate)) {
			rtnMap.put("result", "TRUE");
		}else {
			rtnMap.put("result", "FLASE");
		}
		result = JSONUtils.map2json(rtnMap);
		return result;
	}

	public void updateNameById(Map<String, String> params) throws Exception {
		boolean result;
		String companyId = URLDecoder.decode(params.get("COMPANY_ID"), "UTF-8");
		String abbr_name = URLDecoder.decode(params.get("COMPANY_ABBR_NAME"), "UTF-8");
		String name = URLDecoder.decode(params.get("COMPANY_NAME"), "UTF-8");
		try {
			result = sd_com_Dao.updateNameById(companyId, name, abbr_name);
			System.out.println("發動者統一編號:" + companyId + "，更新資料：" + result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateFee_typeByCIDnTXID(Map<String, String> params) throws Exception {
		String com_id = URLDecoder.decode(params.get("COMPANY_ID"), "UTF-8");
		String txn_id = URLDecoder.decode(params.get("TXN_ID"), "UTF-8");
		String fee_type = URLDecoder.decode(params.get("FEE_TYPE"), "UTF-8");
		String snd_brbk_id = URLDecoder.decode(params.get("SND_BRBK_ID"), "UTF-8");
		// if (fee_type.contains("o")) {
		// fee_type = " ";
		// }
		List<SD_COMPANY_PROFILE> updateList = sd_com_Dao.updateSDList(com_id, txn_id, fee_type,snd_brbk_id);
		try {
			for (SD_COMPANY_PROFILE data : updateList) {
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
				params.put("CONTACT_INFO", URLDecoder.decode(data.getCONTACT_INFO(), "UTF-8"));
				params.put("START_DATE", URLDecoder.decode(data.getSTART_DATE(), "UTF-8"));
				params.put("USER_NO", URLDecoder.decode(data.getUSER_NO(), "UTF-8"));
				params.put("CASE_NO", URLDecoder.decode(data.getCASE_NO(), "UTF-8"));
				params.put("FEE_TYPE_ORG", data.getFEE_TYPE());
				update(data.getCOMPANY_ID(), data.getTXN_ID(), data.getSND_BRBK_ID(), params , true);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 交易代號下拉選單連結收費類型用
	 * 
	 * @param params
	 * @return
	 */
	public String getFeeTypeList(Map<String, String> params) {
		String result;
		String paramName;
		String paramValue;
		List<FEE_CODE_NW> list = null;
		String TXN_ID = "";
		paramName = "TXN_ID";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)) {
			TXN_ID = paramValue;
		}

		Map rtnMap = new HashMap();
		//
		list = fee_code_nw_bo.getFeeTypeList(TXN_ID);
		System.out.println("FEE_TYPE list >> " + list.toString());

		if (list != null && list.size() > 0) {
			rtnMap.put("result", "TRUE");
			rtnMap.put("msg", list);
		} else {
			rtnMap.put("result", "FALSE");
		}
		result = JSONUtils.map2json(rtnMap);
		System.out.println(result);
		return result;
	}

	/**
	 * 交易代號+交易類型找出收續費與起用日比較，看是否生效
	 * 
	 * @param params
	 * @return
	 */
	public String checkActive(Map<String, String> params) {
		String result;
		String paramName;
		String paramValue;
		List<FEE_CODE_NW> list = null;
		String TXN_ID = "";
		String FEE_TYPE = "";
		String START_DATE = "";
		paramName = "TXN_ID";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)) {
			TXN_ID = paramValue;
		}
		paramName = "FEE_TYPE";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)) {
			FEE_TYPE = paramValue;
		}
		paramName = "START_DATE";
		paramValue = params.get(paramName);
		if (StrUtils.isNotEmpty(paramValue)) {
			START_DATE = paramValue;
		}

		Map rtnMap = new HashMap();
		//
		list = fee_code_nw_bo.checkActive(TXN_ID, FEE_TYPE);
		System.out.println("FEE_TYPE list >> " + list.toString());
		List rtlist = new ArrayList();

		if (list != null && list.size() > 0) {
			if (Integer.parseInt(START_DATE) < Integer.parseInt(list.get(0).getSTART_DATE())) {
				rtnMap.put("result", "FALSE");
				rtnMap.put("msg", list);
			} else {
				rtnMap.put("result", "TRUE");
			}
		} else {
			rtnMap.put("result", "FALSE2");
		}
		result = JSONUtils.map2json(rtnMap);
		System.out.println(result);
		return result;
	}

	public String hisSearch(Map<String, String> param) {
		System.out.println("hisSearch START");
		List<SD_COMPANY_PROFILE> list2 = null;
		List<SD_COMPANY_PROFILE_HIS> list = null;
		List<String> strList = new LinkedList<String>();
		List<String> strList2 = new LinkedList<String>();
		try {
			Map<String, String> map = new HashMap<String, String>();
			System.out.println("COMPANY_ID : " + param.get("com_id"));
			System.out.println("TXN_ID : " + param.get("txn_id"));
			System.out.println("SND_BRBK_ID : " + param.get("sndBrbkId"));
			map.put("COMPANY_ID", StrUtils.isEmpty(param.get("com_id")) ? "" : param.get("com_id").trim());
			map.put("TXN_ID", StrUtils.isEmpty(param.get("txn_id")) ? "" : param.get("txn_id").trim());
			map.put("SND_BRBK_ID", StrUtils.isEmpty(param.get("sndBrbkId")) ? "" : param.get("sndBrbkId").trim());

			StringBuffer sql = new StringBuffer();
			sql.append("SELECT A.*, B.BRBK_NAME ");
			sql.append("FROM SD_COMPANY_PROFILE_HIS A LEFT JOIN BANK_BRANCH B ON A.SND_BRBK_ID = B.BRBK_ID ");
			sql.append("WHERE ");
			int i = 0;
			for (String key : map.keySet()) {
				if (StrUtils.isNotEmpty(map.get(key))) {
					if (i != 0) {
						sql.append(" AND ");
					}
					sql.append(key + " = ?");
					strList.add(map.get(key));
					i++;
				}
			}
			sql.append(" ORDER BY ACTIVE_DATE DESC ");
			list = new ArrayList<SD_COMPANY_PROFILE_HIS>();
			String cols[] = { "SEQ_ID", "COMPANY_ID", "TXN_ID", "SND_BRBK_ID", "UDATE", "ACTIVE_DATE", "FEE_TYPE" };

			list = sd_com_his_Dao.getData(sql.toString(), strList, cols, SD_COMPANY_PROFILE_HIS.class);
			
			
			//20201016 更新  全都寫進HIS 第一筆不需要了
//			// 加上主表的那筆
//			StringBuffer sql2 = new StringBuffer();
//			sql2.append("SELECT A.*, B.BRBK_NAME ");
//			sql2.append("FROM SD_COMPANY_PROFILE A LEFT JOIN BANK_BRANCH B ON A.SND_BRBK_ID = B.BRBK_ID ");
//			sql2.append("WHERE ");
//			int j = 0;
//			for (String key : map.keySet()) {
//				if (StrUtils.isNotEmpty(map.get(key))) {
//					if (j != 0) {
//						sql2.append(" AND ");
//					}
//					sql2.append(key + " = ?");
//					strList2.add(map.get(key));
//					j++;
//				}
//			}
//			list2 = new ArrayList<SD_COMPANY_PROFILE>();
//			String cols2[] = { "COMPANY_ID", "TXN_ID", "SND_BRBK_ID", "UDATE", "FEE_TYPE_ACTIVE_DATE", "FEE_TYPE" };
//
//			list2 = sd_com_Dao.getData(sql2.toString(), strList2, cols2, SD_COMPANY_PROFILE.class);
//			Map mapPo = CodeUtils.objectCovert(Map.class, list2.get(0));
//			mapPo.put("ACTIVE_DATE", mapPo.get("FEE_TYPE_ACTIVE_DATE"));
//			mapPo.remove("FEE_TYPE_ACTIVE_DATE");
//			System.out.println("@@@@@ mapPo > " + mapPo);
//			list.add(0, CodeUtils.objectCovert(SD_COMPANY_PROFILE_HIS.class, (Object) mapPo));

			System.out.println("SD_COMPANY_PROFILE_HIS.list>>" + list);
			for (SD_COMPANY_PROFILE_HIS eachMap : list) {
				switch (eachMap.getFEE_TYPE()) {
				case " ":
					String oldFeeType = fee_code_bo.checkFeeCodeType(map.get("TXN_ID"));
					if ("A".equals(oldFeeType)) {
						eachMap.setFEE_TYPE_CHT("固定");
					} else if ("B".equals(oldFeeType)) {
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
		SD_COMPANY_PROFILE_HIS po = new SD_COMPANY_PROFILE_HIS();
		try {
			po = sd_com_his_Dao.get(Integer.valueOf(param.get("seq_id")));
		} catch (Exception e) {
			System.out.println("GET DEL DATA ERROR");
			rtnMap.put("result", "FALSE");
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("COMPANY_ID", po.getCOMPANY_ID());
		map.put("TXN_ID", po.getTXN_ID());
		map.put("SND_BRBK_ID", po.getSND_BRBK_ID());
		List<Map<String, String>> hisList = sd_com_his_Dao.findByCondition(po.getCOMPANY_ID(),po.getTXN_ID(),po.getSND_BRBK_ID());
		Map<String, String> lastpo = hisList.get(hisList.size()-1);
		Map<String, String> reupdatepo = hisList.get(hisList.size()-2);
		Integer delSEQ_ID = (Integer)po.getSEQ_ID().intValue();
		String last_SEQ_ID = String.valueOf(lastpo.get("SEQ_ID"));
		try {
			if(last_SEQ_ID.equals(String.valueOf(delSEQ_ID))) {
				SD_COMPANY_PROFILE_PK popk= new SD_COMPANY_PROFILE_PK();
				popk.setCOMPANY_ID(po.getCOMPANY_ID());
				popk.setTXN_ID(po.getTXN_ID());
				popk.setSND_BRBK_ID(po.getSND_BRBK_ID());
				SD_COMPANY_PROFILE singlePo = sd_com_Dao.get(popk);
				singlePo.setId(popk);
				singlePo.setFEE_TYPE_ACTIVE_DATE(reupdatepo.get("ACTIVE_DATE"));
				singlePo.setFEE_TYPE(String.valueOf(reupdatepo.get("FEE_TYPE")));
				singlePo.setUDATE(String.valueOf(reupdatepo.get("UDATE")));
				sd_com_Dao.aop_save(singlePo);
			}
			sd_com_his_Dao.remove(po);
			
			rtnMap.put("result", "TRUE");	
		} catch (Exception e) {
			System.out.println("DEL DATA OR RESAVE HIS ERROR");
			rtnMap.put("result", "FALSE");
		}
		
		rtnMap.put("msg", JSONUtils.map2json(map));
		String result = JSONUtils.map2json(rtnMap);
		return result;
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
		list = sd_com_his_Dao.checkDoubleDate(COMPANY_ID,TXN_ID,SND_BRBK_ID,FEE_TYPE_ACTIVE_DATE);
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

	public SD_COMPANY_PROFILE_Dao getSd_com_Dao() {
		return sd_com_Dao;
	}

	public void setSd_com_Dao(SD_COMPANY_PROFILE_Dao sd_com_Dao) {
		this.sd_com_Dao = sd_com_Dao;
	}

	public BANK_GROUP_BO getBank_group_bo() {
		return bank_group_bo;
	}

	public void setBank_group_bo(BANK_GROUP_BO bank_group_bo) {
		this.bank_group_bo = bank_group_bo;
	}

	public EACH_USERLOG_Dao getUserLog_Dao() {
		return userLog_Dao;
	}

	public void setUserLog_Dao(EACH_USERLOG_Dao userLog_Dao) {
		this.userLog_Dao = userLog_Dao;
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

	public SD_COMPANY_PROFILE_HIS_Dao getSd_com_his_Dao() {
		return sd_com_his_Dao;
	}

	public void setSd_com_his_Dao(SD_COMPANY_PROFILE_HIS_Dao sd_com_his_Dao) {
		this.sd_com_his_Dao = sd_com_his_Dao;
	}

	public SYS_PARA_BO getSys_para_bo() {
		return sys_para_bo;
	}

	public void setSys_para_bo(SYS_PARA_BO sys_para_bo) {
		this.sys_para_bo = sys_para_bo;
	}
}
