package tw.org.twntch.bo;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import tw.org.twntch.db.dao.hibernate.TYPH_OP_Dao;
import tw.org.twntch.db.dao.hibernate.WK_DATE_Dao;
import tw.org.twntch.po.WK_DATE_CALENDAR;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.zDateHandler;

public class TYPH_OP_BO {
	private TYPH_OP_Dao typh_op_Dao;
	private WK_DATE_Dao wk_date_Dao;
	private EACH_USERLOG_BO userlog_bo;
	private WK_DATE_BO wk_date_bo;
	
	public Map<String, String> execute(String startDate, String endDate){
		Map<String, String> rtnMap = new HashMap<String, String>();
		Map<String, String> pkmap = new HashMap<String, String>();
		String west_startDate = DateTimeUtils.convertDate(startDate, "yyyyMMdd", "yyyyMMdd");
		String west_endDate = DateTimeUtils.convertDate(endDate, "yyyyMMdd", "yyyyMMdd");
		
		//取得endDate的下一個營業日(因endDate本身為非營業日，所以不須刻意往後取營業日)
		String nextBizdate = wk_date_Dao.gotoBusinessDate(west_endDate, 0);
		if(StrUtils.isEmpty(nextBizdate)){
			rtnMap.put("result", "2-FALSE");
			rtnMap.put("msg", "執行失敗，無法取得次營業日");
			return rtnMap;
		}
		//System.out.println("### nextBizdate = " + nextBizdate);
		
		//從startDate到endDate迴圈
		Map<String, Map<String, Map<String, String>>> updateResult = new LinkedHashMap<String, Map<String, Map<String, String>>>();
		Map<String, String> dayResult = null;	//單日處理後筆數
		Map<String, String> oDayResult = null;	//單日處理前筆數
		Map<String, Map<String, String>> rtnResult = null;//單日處理前後-排序後的結果
		String updateResultStr = "";
		
		List<WK_DATE_CALENDAR> dateList = wk_date_Dao.getByStartAndEndDate(startDate, endDate);
		if(dateList != null && dateList.size() > 0){
			String idxDate = "";
			int status = 0;	//預存程序執行結果回傳的第一碼，0-正常;其他-異常
			for(WK_DATE_CALENDAR wk : dateList){
				idxDate = DateTimeUtils.convertDate(wk.getTXN_DATE(), "yyyyMMdd", "yyyyMMdd");
				
				//System.out.println("### idxDate = " + idxDate);
				//先查出需處理的筆數
				List<Map<String, String>> list = getCountBySingleDate(idxDate);
				if(list != null && list.size() > 0){
					oDayResult = list.get(0);
				}
				//再查出處理後異動的筆數
				updateResultStr = wk_date_Dao.updateTyphData(idxDate, nextBizdate);
				dayResult = formatUpdateResult(updateResultStr);
				//發生異常即返回
				if(dayResult == null){
					status = -1;
					break;
				}
				
				rtnResult = formatRtnResult(oDayResult, dayResult);
				updateResult.put(DateTimeUtils.convertDate(idxDate, "yyyyMMdd", "yyyyMMdd"), rtnResult);
			}
			
			if(status == 0){
				rtnMap.put("result", "2-TRUE");
				rtnMap.put("msg", JSONUtils.toJson(updateResult));
				pkmap.put("TXTIME1", startDate);
				pkmap.put("TXTIME2", endDate);
				userlog_bo.addLog("T", "成功", pkmap, pkmap);
				return rtnMap;
			}else{
				rtnMap.put("result", "2-FALSE");
				rtnMap.put("msg", "執行錯誤，預存程序發生異常");
				return rtnMap;
			}
		}else{
			rtnMap.put("result", "2-FALSE");
			rtnMap.put("msg", "執行失敗，區間內無日期資料");
			return rtnMap;
		}
	}
	
	public Map<String, String> preExecute(String startDate, String endDate){
		Map<String, String> rtnMap = new HashMap<String, String>();
		Map<String, String> pkmap = new HashMap<String, String>();
		Map<String, String> oldmap = new HashMap<String, String>();
		List<String> illegalList = getIllegalList(startDate, endDate);
		
		if(wk_date_bo.isTxnDate() != true){
			String today = zDateHandler.getTWDate();
			rtnMap.put("result", "1-FALSE");
			rtnMap.put("msg","今日為非營業日" + "(" + DateTimeUtils.addSlash(today) + ")，" + "不可執行颱風天處理作業");
			oldmap.put("LOG_MSG","今日為非營業日" + "(" + DateTimeUtils.addSlash(today) + ")，" + "不可執行颱風天處理作業");
			userlog_bo.geFailLog("T", "失敗", oldmap, null, pkmap);
			return rtnMap;
		}
		
		if(illegalList != null){
			rtnMap.put("result", "1-FALSE");
			rtnMap.put("msg", "下列日期為營業日，不可設定為颱風天：\\n" + formatIllegalList(illegalList));
			oldmap.put("LOG_MSG", "下列日期為營業日，不可設定為颱風天：" + formatIllegalList(illegalList));
			userlog_bo.geFailLog("T", "失敗", oldmap, null, pkmap);
			return rtnMap;
		}
		List<Map<String, String>> precheckResult = getPrecheckResult(startDate, endDate);
		rtnMap.put("result", "1-TRUE");
		rtnMap.put("msg", JSONUtils.toJson(precheckResult));
		pkmap.put("TXTIME1", startDate);
		pkmap.put("TXTIME2", endDate);
//		userlog_bo.addLog("T", "成功", pkmap, pkmap);
		return rtnMap;
	}
	
	//依照輸入的起訖日期(含)逐一檢查是否為非營業日，若為營業日則回傳(不合法)
	public List<String> getIllegalList(String startDate, String endDate){
		List<String> rtnList = null;
		StringBuilder sql = new StringBuilder();
		sql.append("FROM tw.org.twntch.po.WK_DATE_CALENDAR ");
		sql.append("WHERE TXN_DATE BETWEEN ? AND ? ");
		sql.append("AND IS_TXN_DATE = 'Y' ");
		
		try{
			List<WK_DATE_CALENDAR> list = wk_date_Dao.find(sql.toString(), startDate, endDate);
			if(list != null && list.size() > 0){
				rtnList = new ArrayList<String>();
				for(WK_DATE_CALENDAR w : list){
					rtnList.add(w.getTXN_DATE());
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return rtnList;
	}
	
	public String formatIllegalList(List<String> illegalList){
		String rtnStr = "";
		if(illegalList != null && illegalList.size() > 0){
			for(int i = 0; i < illegalList.size(); i++){
				rtnStr += illegalList.get(i);
				if(i < illegalList.size() - 1){
					rtnStr += "\\n";
				}
			}
		}
		return rtnStr;
	}
	
	public List<Map<String, String>> getPrecheckResult(String startDate, String endDate){
		List<Map<String, String>> rtnList = null;
		
		String west_startDate = DateTimeUtils.convertDate(startDate, "yyyyMMdd", "yyyyMMdd");
		String west_endDate = DateTimeUtils.convertDate(endDate, "yyyyMMdd", "yyyyMMdd");
		StringBuilder sql = new StringBuilder();
//		sql.append("SELECT 'ONBLOCKTAB' AS NAME, COUNT(*) AS NUM FROM ONBLOCKTAB WHERE BIZDATE BETWEEN '" + west_startDate + "' AND '" + west_endDate + "' ");
//		sql.append("UNION ");
//		sql.append("SELECT 'ONPENDINGTAB' AS NAME, COUNT(*) AS NUM FROM ONPENDINGTAB WHERE (OBIZDATE BETWEEN '" + west_startDate + "' AND '" + west_endDate + "') OR (BIZDATE BETWEEN '" + west_startDate + "' AND '" + west_endDate + "') ");
//		sql.append("UNION ");
//		sql.append("SELECT 'ONCLEARINGTAB' AS NAME, COUNT(*) AS NUM FROM ONCLEARINGTAB WHERE BIZDATE BETWEEN '" + west_startDate + "' AND '" + west_endDate + "' ");
		
		//680 UAT-20161110-01 新增沖正筆數= (空值：筆)
		sql.append("WITH TEMP AS (SELECT COUNT(*) AS ZERO FROM ONCLEARINGTAB WHERE BIZDATE BETWEEN '" + west_startDate + "' AND '" + west_endDate + "' AND RECVCNT='0' AND RECVAMT='0' AND PAYCNT='0' AND PAYAMT='0' AND RECVMBFEECNT='0' AND RECVMBFEEAMT='0' AND PAYMBFEECNT='0' AND PAYMBFEEAMT='0' AND PAYEACHFEECNT='0' AND PAYEACHFEEAMT='0' AND RVSRECVCNT='0' AND RVSRECVAMT='0' AND RVSPAYCNT='0' AND RVSPAYAMT='0' AND RVSRECVMBFEECNT='0' AND RVSRECVMBFEEAMT='0' AND RVSRECVEACHFEECNT='0' AND RVSRECVEACHFEEAMT='0' AND RVSPAYMBFEECNT='0' AND RVSPAYMBFEEAMT='0') ");
		sql.append("SELECT 'ONBLOCKTAB' AS NAME, COUNT(*) AS NUM, 0 AS ZERO FROM ONBLOCKTAB WHERE BIZDATE BETWEEN '" + west_startDate + "' AND '" + west_endDate + "' ");
		sql.append("UNION ");
		sql.append("SELECT 'ONPENDINGTAB' AS NAME, COUNT(*) AS NUM, 0 AS ZERO FROM ONPENDINGTAB WHERE (OBIZDATE BETWEEN '" + west_startDate + "' AND '" + west_endDate + "') OR (BIZDATE BETWEEN '" + west_startDate + "' AND '" + west_endDate + "') ");
		sql.append("UNION ");
		sql.append("SELECT 'ONCLEARINGTAB' AS NAME, COUNT(*) AS NUM, (SELECT ZERO FROM TEMP) AS ZERO FROM ONCLEARINGTAB WHERE BIZDATE BETWEEN '" + west_startDate + "' AND '" + west_endDate + "' ");
		
		try{
			rtnList = typh_op_Dao.preCheck(sql.toString());
			DecimalFormat df = new DecimalFormat("#,###");
			for(Map<String, String> m : rtnList){
				m.put("NUM", df.format(m.get("NUM")));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return rtnList;
	}
	
	public List<Map<String, String>> getCountBySingleDate(String date){
		List<Map<String, String>> rtnList = null;
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ");
		sql.append("(SELECT COUNT(*) FROM ONBLOCKTAB WHERE BIZDATE = '"+date+"') AS ONBLOCKTAB, ");
		sql.append("(SELECT COUNT(*) FROM ONPENDINGTAB WHERE OBIZDATE = '"+date+"' OR BIZDATE = '"+date+"') AS ONPENDINGTAB, ");
		sql.append("(SELECT COUNT(*) FROM ONCLEARINGTAB WHERE BIZDATE = '"+date+"') AS ONCLEARINGTAB ");
		sql.append("FROM SYSIBM.SYSDUMMY1 ");
		
		try{
			rtnList = typh_op_Dao.preCheck(sql.toString());
			DecimalFormat df = new DecimalFormat("#,###");
			for(Map<String, String> m : rtnList){
				m.put("ONBLOCKTAB", df.format(m.get("ONBLOCKTAB")));
				m.put("ONPENDINGTAB", df.format(m.get("ONPENDINGTAB")));
				m.put("ONCLEARINGTAB", df.format(m.get("ONCLEARINGTAB")));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return rtnList;
	}
	
	//傳入格式為逗號分隔資料(第一位為狀態碼，0-正常;其他-異常)
	public Map<String, String> formatUpdateResult(String updateResult){
		Map<String, String> m = null;
		String data[] = updateResult.split(",");
		if(data.length == 4){
			if(Integer.valueOf(data[0]) != 0) return m;
			
			m = new HashMap<String, String>();
			for(int i = 1; i < data.length; i++){
				if(i == 1){
					m.put("ONBLOCKTAB", data[i]);
				}else if(i == 2){
					m.put("ONPENDINGTAB", data[i]);
				}else if(i == 3){
					m.put("ONCLEARINGTAB", data[i]);
				}
			}
		}
		return m;
	}
	
	//整理異動後的結果(單日異動前筆數, 單日異動後筆數)
	public Map<String, Map<String, String>> formatRtnResult(Map<String, String> oDayResult, Map<String, String> dayResult){
		Map<String, Map<String, String>> rtnResult = new LinkedHashMap<String, Map<String, String>>();
		String subject[] = {"ONBLOCKTAB", "ONPENDINGTAB", "ONCLEARINGTAB"};
		
		Map<String, String> m = null;
		for(String s : subject){
			m = new LinkedHashMap();
			if(oDayResult != null && !oDayResult.isEmpty()){
				m.put("B", oDayResult.get(s));
			}
			if(dayResult != null && !dayResult.isEmpty()){
				m.put("A", dayResult.get(s));
			}
			rtnResult.put(s, m);
		}
		return rtnResult;
	}
	public TYPH_OP_Dao getTyph_op_Dao() {
		return typh_op_Dao;
	}

	public void setTyph_op_Dao(TYPH_OP_Dao typh_op_Dao) {
		this.typh_op_Dao = typh_op_Dao;
	}

	public WK_DATE_Dao getWk_date_Dao() {
		return wk_date_Dao;
	}

	public void setWk_date_Dao(WK_DATE_Dao wk_date_Dao) {
		this.wk_date_Dao = wk_date_Dao;
	}

	public EACH_USERLOG_BO getUserlog_bo() {
		return userlog_bo;
	}

	public void setUserlog_bo(EACH_USERLOG_BO userlog_bo) {
		this.userlog_bo = userlog_bo;
	}

	public WK_DATE_BO getWk_date_bo() {
		return wk_date_bo;
	}

	public void setWk_date_bo(WK_DATE_BO wk_date_bo) {
		this.wk_date_bo = wk_date_bo;
	}
	
	
}
