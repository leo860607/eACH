package tw.org.twntch.bo;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.struts.util.LabelValueBean;

import tw.org.twntch.db.dao.hibernate.BANK_GROUP_Dao;
import tw.org.twntch.db.dao.hibernate.RPONBLOCKTAB_Dao;
import tw.org.twntch.po.BANK_GROUP;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.RptUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.zDateHandler;

public class RPTTX_4_BO {
	private RPONBLOCKTAB_Dao rponblocktab_Dao;
	private BANK_GROUP_Dao bank_group_Dao;
	
	public Map<String, String> export(String startDate, String endDate, String flBatchSeq, String optId, String bgbkId, String clearingPhase, String optBank, String serchStrs){
		Map<String, String> rtnMap = null;
		String outputFilePath = "";
		try{
			rtnMap = new HashMap<String, String>();
			rtnMap.put("serchStrs", serchStrs);
			Map<String, Object> params = new HashMap<String, Object>();
			//SQL查詢條件 區塊
			/*
			params.put("TXDT","'%"+ DateTimeUtils.convertDate(txdt, "yyyymmdd", "yyyymmdd")+"%' ");
			params.put("opt_id", opt_id);
			params.put("CLEARINGPHASE", clearingPhase);
			*/
			//顯示區塊
			if(startDate.equals(endDate)){
				params.put("V_TXDT", "營業日期：" + DateTimeUtils.convertDate(DateTimeUtils.NOT_INTERCONVERSION, startDate, "yyyymmdd", "yyy/mm/dd"));
			}else{
				params.put("V_TXDT", "營業日期區間：" + DateTimeUtils.convertDate(DateTimeUtils.NOT_INTERCONVERSION, startDate, "yyyymmdd", "yyy/mm/dd") + "~" + DateTimeUtils.convertDate(DateTimeUtils.NOT_INTERCONVERSION, endDate, "yyyymmdd", "yyy/mm/dd"));
			}
			params.put("V_CLEARINGPHASE", clearingPhase);
			params.put("V_PRINT_DATE",DateTimeUtils.convertDate(DateTimeUtils.INTERCONVERSION, zDateHandler.getTheDate(), "yyyy/MM/dd", "yyy/MM/dd"));
			params.put("V_PRINT_TIME",zDateHandler.getTheTime());
			params.put("V_OP_TYPE", getOpType_Name(Integer.valueOf(3)));
			params.put("V_FLBATCHSEQ", StrUtils.isEmpty(flBatchSeq)?"檔名：全部":"檔名：" + flBatchSeq.trim());
			//System.out.println("params >> " + params);
			
			Map map = this.getConditionData(startDate, endDate, flBatchSeq, optId, bgbkId, clearingPhase);
			String sql = getSQL(map.get("sqlPath").toString(), "", getOrderBySql(Integer.valueOf(0)));
			List list = rponblocktab_Dao.getRptData(sql, (List<String>) map.get("values"));
			int pathType = Arguments.getStringArg("ISWINOPEN").equals("Y")?RptUtils.C_PATH :RptUtils.R_PATH;
			outputFilePath = RptUtils.export(RptUtils.COLLECTION,pathType, "tx_4", "tx_4", params, list);
			
			System.out.println("RPTTX_4_BO.sql >> " + sql.replaceAll("\\n", "<br/>"));
			System.out.println("RPTTX_4_BO.val >> " + map.get("values"));
			if(StrUtils.isNotEmpty(outputFilePath)){
				rtnMap.put("result", "TRUE");
				rtnMap.put("msg", outputFilePath);
			}else{
				rtnMap.put("result", "FALSE");
				rtnMap.put("msg", "產生失敗!");
			}
		}catch(Exception e){
			e.printStackTrace();
			rtnMap.put("result", "FALSE");
			rtnMap.put("msg", "產生錯誤!");
		}
		
		return rtnMap;
	}
	
	public String getOpType_Name(int opt_type){
		String ret = "";
		switch (opt_type) {
		case 1:
			ret = "依扣款行";
			break;
		case 2:
			ret = "依入帳行";
			break;
		default:
			ret = "依發動行";
			break;
		}
		return ret ;
	}
	public String getOpType(int opt_type){
		String ret = "";
		switch (opt_type) {
		case 1:
//			ret = "O";
			ret = "B";
			break;
		case 2:
			ret = "I";
			break;
		default:
			ret = "S";
			break;
		}
		return ret ;
	}
	
	public String getOrderBySql(int opt_type){
		StringBuffer sql = new StringBuffer();
		sql.append(" ORDER BY A.CLEARINGPHASE, BANKID, A.PCODE, A.FLBATCHSEQ, A.TXDT, TXT, A.STAN ");
		return sql.toString();
	}
	
	public Map getConditionData(String startDate, String endDate, String flBatchSeq, String optId, String bgbkId, String clearingPhase) throws Exception{
		StringBuffer sql = new StringBuffer();
		Map<String, String> params = new LinkedHashMap<String, String> ();
		Map retmap = new LinkedHashMap();
		List<String> values = new LinkedList<String>(); 
		try {
			if(startDate.equals(endDate)){
				params.put("A.BIZDATE", DateTimeUtils.convertDate(startDate, "yyyymmdd", "yyyymmdd"));
			}else{
				params.put("START_DATE", startDate);
				params.put("END_DATE", endDate);
			}
			params.put("A.CLEARINGPHASE", clearingPhase);
			params.put("A.FLBATCHSEQ", flBatchSeq);
			params.put("A.SENDERACQUIRE", optId);
			params.put("A.SENDERHEAD", bgbkId);
			
			int i = 0;
			for(String key :params.keySet()){
				if(StrUtils.isNotEmpty(params.get(key))){
					if(i==0){
						sql.append(" WHERE ");
						sql.append("COALESCE(A.FLBATCHSEQ,'') <> '' AND ");
					}
					if(i!=0){sql.append(" AND ");}
					if(key.equals("START_DATE")){
						sql.append("A.BIZDATE >= ?");
						values.add(DateTimeUtils.convertDate(params.get(key), "yyyymmdd", "yyyymmdd"));
					}else if(key.equals("END_DATE")){
						sql.append("A.BIZDATE <= ?");
						values.add(DateTimeUtils.convertDate(params.get(key), "yyyymmdd", "yyyymmdd"));
					}else{
						sql.append(key + " = ?");
						values.add(params.get(key));
					}
					i++;
				}
			}
			retmap.put("sqlPath", sql.toString());
			retmap.put("values", values);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e.toString()) ; 
		}
		return retmap;
	}
	
	public String getSQL(String sqlPath , String spSql , String orderbySql){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ");
		sql.append("COALESCE(A.SENDERHEAD,'') BANKID ");
		sql.append(", COALESCE((SELECT BRBK_NAME FROM BANK_BRANCH WHERE BRBK_ID = A.SENDERHEAD),'') BANK_NAME ");
		sql.append(", A.BIZDATE ");
		sql.append(", A.CLEARINGPHASE ");
		sql.append(", A.PCODE ");
		sql.append(", A.PCODE || '-' || COALESCE(B.EACH_TXN_NAME,'') PCODE_NAME ");
		sql.append(", A.FLBATCHSEQ, VARCHAR(A.STAN) AS STAN ");
		sql.append(", (CASE WHEN COALESCE(VARCHAR(A.TXDT),'') = '' THEN '' ELSE (CAST (YEAR(A.TXDT)-1911 AS CHAR(3)) || '' || SUBSTR( REPLACE(CAST( DATE(A.TXDT) AS CHAR(10)),'-','/'),5,6)  )  END ) TXDT ");
		sql.append(", (CASE WHEN COALESCE(VARCHAR(A.TXDT),'') = '' THEN '' ELSE (SUBSTR(REPLACE(CAST( TIME(A.TXDT) AS CHAR(8)) ,'.',':') ,1,8)) END ) TXT ");
		sql.append(", COALESCE(A.SENDERBANKID,'') SENDERBANKID ");
		sql.append(", COALESCE((SELECT BRBK_NAME FROM BANK_BRANCH WHERE BRBK_ID = A.SENDERBANKID),'') SENDERBANKID_NAME ");
		sql.append(", COALESCE(A.OUTBANKID,'') OUTBANKID ");
		sql.append(", COALESCE((SELECT BRBK_NAME FROM BANK_BRANCH WHERE BRBK_ID = A.OUTBANKID),'') OUTBANKID_NAME ");
		sql.append(", A.OUTACCTNO ");
		sql.append(", COALESCE(A.INBANKID,'') INBANKID ");
		sql.append(", COALESCE((SELECT BRBK_NAME FROM BANK_BRANCH WHERE BRBK_ID = A.INBANKID),'') INBANKID_NAME ");
		sql.append(", A.INACCTNO ");
		sql.append(", COALESCE(A.SENDERID,'') || '\n' || COALESCE(A.COMPANY_ABBR,'') SENDERID ");
		sql.append(", A.TXID ");
		sql.append(", COALESCE((SELECT TXID || '\n' || TXN_NAME FROM TXN_CODE WHERE A.TXID=TXN_ID),'') TXN_NAME ");
		sql.append(", DECIMAL(A.TXAMT, 18, 0) TXAMT ");
		sql.append(", (CASE WHEN A.NEWRESULT = 'R' THEN '失敗' WHEN A.NEWRESULT = 'P' AND A.SENDERSTATUS != '1' THEN '未完成' WHEN A.NEWRESULT = 'P' AND A.SENDERSTATUS = '1' THEN '失敗' ELSE '成功' END) RESP ");
		sql.append(", RTRIM(COALESCE(RC1 || '/','        /')) || COALESCE(RC2 || '/','        /') || COALESCE(RC3 || '\n','        \n') || RTRIM(COALESCE(RC4 || '/','        /')) || RTRIM(COALESCE(RC5 || '/','        /')) || COALESCE(RC6 ,'         ') RC ");
		sql.append(", (CASE WHEN A.NEWRESULT = 'A' OR (A.NEWRESULT = 'P' AND A.SENDERSTATUS != '1') THEN COALESCE(A.SENDERFEE,0) ELSE 0 END) FEE ");
		sql.append("FROM RPONBLOCKTAB A LEFT JOIN EACH_TXN_CODE B ON B.EACH_TXN_ID = A.PCODE ");
		sql.append(sqlPath);
		sql.append(orderbySql);
		
		//System.out.println("getSQL >> " + sql);
		return sql.toString();
	}
	
	public List<LabelValueBean> getBgbkIdList(){
		List<BANK_GROUP> list = bank_group_Dao.getBgbkIdList_2();
		List<LabelValueBean> beanList = new LinkedList<LabelValueBean>();
		LabelValueBean bean = null;
		for(BANK_GROUP po : list){
			bean = new LabelValueBean(po.getBGBK_ID() + " - " + po.getBGBK_NAME(), po.getBGBK_ID());
			beanList.add(bean);
		}
		System.out.println("beanList>>"+beanList);
		return beanList;
	}
	public RPONBLOCKTAB_Dao getRponblocktab_Dao() {
		return rponblocktab_Dao;
	}

	public void setRponblocktab_Dao(RPONBLOCKTAB_Dao rponblocktab_Dao) {
		this.rponblocktab_Dao = rponblocktab_Dao;
	}

	public BANK_GROUP_Dao getBank_group_Dao() {
		return bank_group_Dao;
	}

	public void setBank_group_Dao(BANK_GROUP_Dao bank_group_Dao) {
		this.bank_group_Dao = bank_group_Dao;
	}
}
