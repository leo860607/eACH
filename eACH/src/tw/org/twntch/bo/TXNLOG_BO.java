package tw.org.twntch.bo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.struts.util.LabelValueBean;

import tw.org.twntch.db.dao.hibernate.HibernateEntityDao;
import tw.org.twntch.db.dao.hibernate.ONBLOCKTAB_Dao;
import tw.org.twntch.db.dao.hibernate.Page;
import tw.org.twntch.form.Login_Form;
import tw.org.twntch.po.AGENT_PROFILE;
import tw.org.twntch.po.ONBLOCKTAB;
import tw.org.twntch.po.ONBLOCKTAB_PK;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.WebServletUtils;

public class TXNLOG_BO {

	private ONBLOCKTAB_Dao onblocktab_Dao ;
	private EACH_USERLOG_BO userlog_bo ;
	private EACHSYSSTATUSTAB_BO eachsysstatustab_bo ; 
	private AGENT_CR_LINE_BO agent_cr_line_bo;
	private AGENT_FEE_CODE_BO agent_fee_code_bo;
	private AGENT_PROFILE_BO agent_profile_bo;
	private AGENT_SEND_PROFILE_BO agent_send_profile_bo;
	private TXN_CODE_BO txn_code_bo;
	
	
	public List<Map> getDetail(String txdate , String issuerid ,String seq ,String user_type  ){
		String sqlPath = " WHERE TG.TXDATE = ? AND TG.ISSUERID =?  AND TG.SEQ = ? ";
		String spSql="";
		if(user_type.equals("A")){
//			spSql = " ,( CASE WHEN COALESCE(TG.TG_RESULT ,'') = 'W' THEN '處理中'  WHEN  COALESCE(TG.TG_RESULT ,'') = 'A' THEN '成功' WHEN  COALESCE(TG.TG_RESULT ,'') = 'R' THEN '失敗'  WHEN  COALESCE(TG.TG_RESULT ,'') = 'P' THEN '未完成' WHEN  COALESCE(TG.TG_RESULT ,'') = 'U' THEN '與中心不一致' ELSE  '狀態不明'END) TG_RESULT  ";
			spSql = " ,( CASE WHEN COALESCE(TG.TG_RESULT ,'') = 'W' OR COALESCE(TG.TG_RESULT ,'') = 'P' THEN '處理中'  WHEN  COALESCE(TG.TG_RESULT ,'') = 'A' THEN '成功' WHEN  COALESCE(TG.TG_RESULT ,'') = 'R' OR COALESCE(TG.TG_RESULT ,'') = 'U' THEN '失敗'  ELSE '狀態不明' END) TG_RESULT  ";
		}else{
			spSql = " ,( CASE WHEN COALESCE(TG.TG_RESULT ,'') = 'W' OR COALESCE(TG.TG_RESULT ,'') = 'P' THEN '處理中'  WHEN  COALESCE(TG.TG_RESULT ,'') = 'A' THEN '成功' WHEN  COALESCE(TG.TG_RESULT ,'') = 'R' OR COALESCE(TG.TG_RESULT ,'') = 'U' THEN '失敗'  ELSE '狀態不明' END) TG_RESULT  ";
		}
		List<String> values = new LinkedList<String>();	
		values.add(txdate);values.add(issuerid);values.add(seq);
		String sql = getDetailSQL(sqlPath, "", spSql,"");
		return onblocktab_Dao.getTXNLOG_Detail(sql , values );
	}
	
	
	public String getSnd_Com_List(Map<String, String> params){
		String json = "{}";
		String company_id = StrUtils.isNotEmpty(params.get("COMPANY_ID"))? params.get("COMPANY_ID") : "";
		String bizdate = "";
		List<Map> list = null ;
		Map<String , Object> retMap = new HashMap<String , Object> ();
		retMap.put("result", "FALSE");
		try {
			bizdate = eachsysstatustab_bo.getBusinessDate();
			System.out.println("bizdate>>"+bizdate);
			if(StrUtils.isNotEmpty(company_id)){
				list = agent_send_profile_bo.getSnd_Com_List(company_id);
				System.out.println("list>>"+list);
				if(list!=null && list.size() != 0){
					retMap.put("result", "TRUE");
					retMap.put("msg", list);
				}else{
					retMap.put("result", "FALSE");
					retMap.put("msg", "查無發動業者");
				}
				json = JSONUtils.map2json(retMap);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
//			retMap.put("msg", "系統異常");
		}
		System.out.println("json>>"+json);
		return json;
	}
	
	public List<LabelValueBean> getCompany_Id_List(){
		String bizdate = "";
		bizdate = eachsysstatustab_bo.getBusinessDate();
		System.out.println("bizdate>>"+bizdate);
//		List<AGENT_PROFILE> list = agent_profile_Dao.getData(sql.toString(), new ArrayList<String>());
		List<AGENT_PROFILE> list = agent_profile_bo.getAgent_profile_Dao().getCompany_Id_List(bizdate);
		List<LabelValueBean> beanList = new LinkedList<LabelValueBean>();
		LabelValueBean bean = null;
		if(list != null &&  list.size() != 0){
			for(AGENT_PROFILE po : list){
				bean = new LabelValueBean(po.getCOMPANY_ID() + " - " + po.getCOMPANY_ABBR_NAME(), po.getCOMPANY_ID());
				beanList.add(bean);
			}
		}
		System.out.println("beanList>>"+beanList);
		return beanList;
	}

public String pageSearch(Map<String, String> params){
	int pageNo = StrUtils.isEmpty(params.get("page"))? 0 : Integer.valueOf(params.get("page"));
	int pageSize = StrUtils.isEmpty(params.get("rows"))? Integer.valueOf(Arguments.getStringArg("PAGE.SIZE")) : Integer.valueOf(params.get("rows"));
	int startIndex = 0 , lastIndex = 0;
	String countQuerySql = "" ,sql = "" , result = "";
	List list = null;
	List<Map<String,Integer>> cntlist = null;
	Map rtnMap = null;
	Page page = null;
	try {
		Login_Form login_form = (Login_Form) WebServletUtils.getRequest().getSession().getAttribute("login_form");
		rtnMap = new HashMap();
		String sord =StrUtils.isNotEmpty(params.get("sord"))? params.get("sord"):"";
		String sidx =StrUtils.isNotEmpty(params.get("sidx"))? params.get("sidx"):"";
		String orderSQL = StrUtils.isNotEmpty(sidx)? " ORDER BY "+sidx+" "+sord:"";
		String conditionKey = "[\"STXDATE\" ,\"GARBAGEDATA\" ,\"ROWNUMBER\",\"SBIZDATE\",\"BIZDATE\",\"SND_COMPANY_ID\",\"SEQ\" , \"AGENT_COMPANY_ID\", \"STAN\", \"CLEARINGPHASE\", \"opAction1\", \"TXN_ID\", \"TRANSACTIONAMOUNT\" , \"RESULTSTATUS\", \"PFCLASS\", \"TOLLID\", \"CHARGETYPE\", \"BILLFLAG\"]";
		String garbageData =  StrUtils.isEmpty(params.get("GARBAGEDATA")) ?"N":"Y";
		System.out.println("garbageData"+garbageData);
		String spSql = "";
		startIndex = Page.getStartOfPage(pageNo, pageSize) + 1;
		lastIndex = pageNo * pageSize;
		System.out.println("startIndex>>"+startIndex+"lastIndex>>"+lastIndex);
		params.put("ROWNUMBER", String.valueOf(startIndex));
		params.put("LAST_ROWNUMBER", String.valueOf(lastIndex));
		params.put("GARBAGEDATA", garbageData);
		Map map = null;
		if(login_form.getUserData().getUSER_TYPE().equals("A")){
			spSql = " ,(CASE WHEN COALESCE(TG.TG_RESULT , '') = 'P'  THEN '未完成' WHEN COALESCE(TG_RESULT , '') = 'A' THEN '成功' WHEN COALESCE(TG_RESULT , '') = 'W' THEN '處理中' WHEN COALESCE(TG_RESULT , '') = 'U' THEN '與中心不一致' WHEN COALESCE(TG_RESULT , '') = 'R' THEN '失敗' ELSE '狀態不明' END   )AS  RESULTSTATUS ";
			map = getConditionData(params, conditionKey);
		}else{
			spSql = " ,(CASE WHEN COALESCE(TG.TG_RESULT , '') = 'P'  THEN '未完成' WHEN COALESCE(TG_RESULT , '') = 'A' THEN '成功' WHEN COALESCE(TG_RESULT , '') = 'W' THEN '處理中' WHEN COALESCE(TG_RESULT , '') = 'R' OR COALESCE(TG_RESULT , '') = 'U' THEN '失敗' ELSE '狀態不明' END   )AS  RESULTSTATUS ";
			map = getConditionData_TypeC(params, conditionKey);
		}
		
//		sql = getSQL(map.get("sqlPath").toString(), map.get("sqlPath2").toString() ,orderSQL ) ;
		sql = getSQL(map.get("sqlPath").toString(), map.get("sqlPath2").toString(),spSql ,orderSQL ) ;
		countQuerySql = getCntSQL(map.get("sqlPath").toString(), map.get("sqlPath2").toString());
//		page = onblocktab_Dao.getDataRetPage(startIndex, pageSize, countQuerySql, sql, (List<String>) map.get("values"));
		cntlist = onblocktab_Dao.DataSumList(countQuerySql , (Map<String,String>) map.get("values"));
		rtnMap.put("dataSumList", cntlist);
		page = onblocktab_Dao.getDataRetPage(startIndex, pageSize, cntlist.get(0).get("RECORDS") , sql, (Map<String,String>) map.get("values"));
		list = (List) page.getResult();
		rtnMap.put("total", page.getTotalPageCount());
		rtnMap.put("page", String.valueOf(page.getCurrentPageNo()));
		rtnMap.put("records", page.getTotalCount());
		rtnMap.put("rows", list);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		rtnMap.put("total", "0");
		rtnMap.put("page", "0");
		rtnMap.put("records", "0");
		rtnMap.put("rows", new ArrayList());
	}
	result = JSONUtils.map2json(rtnMap) ;
	System.out.println("pageSearch>>"+result);
	return result;
}
	

public String getSQL(String sqlPath ,String sqlPath2,String spSql,String orderbySQL ){
	StringBuffer sql = new StringBuffer();
	sql.append(" WITH TEMP AS ( ");
	sql.append(" SELECT   COALESCE(TG.SEQ , '') AS  SEQ ,  COALESCE(TG.TXDATE  , '') AS  TXDATE ,  COALESCE(TG.STAN , '') AS  STAN   ,  COALESCE(TG.ISSUERID,'') AS COMPANY_ID  ");
	sql.append(" , COALESCE(TG.TXID,'') AS TXID , COALESCE(TG.PCODE,'') AS PCODE ,COALESCE(TG.TRANSACTIONAMOUNT,'0') AS TRANSACTIONAMOUNT  ");
	sql.append(" , COALESCE(TG.TRANSACTIONDATETIME , '' ) AS TRANSACTIONDATETIME , TRANSLATE('abcd-ef-gh op:qr:st', TG.TRANSACTIONDATETIME, 'abcdefghopqrst') AS TMP_TXDATE , COALESCE(OB.TXFROM , '') AS  TXFROM ");
	sql.append(" , TG.TXN_NAME , TG.PCODE_NAME  ");
	sql.append(" , COALESCE(TG.INBANKID,'') AS INBANKID , COALESCE(TG.OUTBANKID,'') AS OUTBANKID  ");
	sql.append(" , COALESCE(TG.INACCOUNTNUM ,'') AS INACCOUNTNUM  , COALESCE(TG.OUTACCOUNTNUM ,'') AS OUTACCOUNTNUM   ");
	sql.append(" , COALESCE(TG.INBANKID,'') ||'-'||COALESCE((SELECT BRBK_NAME FROM BANK_BRANCH BR WHERE BR.BRBK_ID = TG.INBANKID ),'') AS INBANK_NAME  ");
	sql.append(" , COALESCE(TG.OUTBANKID,'') ||'-'||COALESCE((SELECT BRBK_NAME FROM BANK_BRANCH BR WHERE BR.BRBK_ID = TG.OUTBANKID ),'') AS OUTBANK_NAME  ");
	sql.append(" , TG.COMPANY_ABBR_NAME  ");
	sql.append(" , TG.SND_COMPANY_ID   ");
	sql.append(" , TG.SND_COMPANY_ABBR_NAME  "); 
	sql.append(" , TG.PFCLASS   ");
	sql.append(" , TG.TOLLID   ");
	sql.append(" , TG.CHARGETYPE   ");
	sql.append(" , TG.BILLFLAG   ");
	sql.append(" , TG.CHECKDATA   ");
//	sql.append(" ,(CASE WHEN COALESCE(TG.TG_RESULT , '') = 'P'  THEN '未完成' WHEN COALESCE(TG_RESULT , '') = 'A' THEN '成功' WHEN COALESCE(TG_RESULT , '') = 'W' THEN '處理中' WHEN COALESCE(TG_RESULT , '') = 'U' THEN '與中心不一致' WHEN COALESCE(TG_RESULT , '') = 'R' THEN '失敗' ELSE '狀態不明' END   )AS  RESULTSTATUS ");
	sql.append(spSql);
	sql.append(" FROM VW_TXNLOG TG   ");
	sql.append(" LEFT JOIN ONBLOCKTAB OB ON TG.BIZDATE = OB.BIZDATE AND TG.STAN = OB.STAN   ");
	sql.append(sqlPath);
	sql.append("  )   ");
	sql.append("   SELECT * FROM (  ");
	sql.append("   SELECT  ROWNUMBER() OVER("+orderbySQL+") AS ROWNUMBER , TEMP.*  FROM TEMP  ");
	sql.append("  ) AS A  ");
	sql.append(sqlPath2);
	sql.append(orderbySQL);
	System.out.println("getSQL.sql>>"+sql);
	return sql.toString();
}
public String getCntSQL(String sqlPath ,String sqlPath2  ){
	StringBuffer sql = new StringBuffer();
	sql.append(" WITH TEMP AS ( ");
	sql.append(" SELECT   COALESCE(TG.TRANSACTIONAMOUNT,'0') AS TRANSACTIONAMOUNT  ");
	sql.append(" FROM VW_TXNLOG TG   ");
	sql.append(" LEFT JOIN ONBLOCKTAB OB ON TG.BIZDATE = OB.BIZDATE AND TG.STAN = OB.STAN   ");
	sql.append(sqlPath);
	sql.append(" )   ");
	sql.append("  SELECT COUNT(*) AS RECORDS , SUM(INT(TRANSACTIONAMOUNT)) AS TXAMT FROM TEMP ");
	System.out.println("getCntSQL.sql>>"+sql);
	return sql.toString();
}

/**
 * 20151211 依最新訪談結果 先不用
 * @param sqlPath
 * @param sqlPath2
 * @param orderbySQL
 * @return
 */
public String getDetailSQL(String sqlPath ,String sqlPath2 , String spSql,String orderbySQL ){
	StringBuffer sql = new StringBuffer();
	sql.append("WITH TEMP AS ( ");
		sql.append("SELECT  COALESCE(OB.BIZDATE ,TG.BIZDATE  , '') AS  BIZDATE , COALESCE(OB.CLEARINGPHASE ,TG.CLEARINGPHASE  , '') AS  CLEARINGPHASE ");
	    sql.append(",TRANSLATE('abcd-ef-gh op:qr:st', TG.TRANSACTIONDATETIME, 'abcdefghopqrst') AS TRANSACTIONDATETIME ,TRANSLATE('abcd-ef-gh op:qr:st', TG.HANDLEDATETIME, 'abcdefghopqrst') AS HANDLEDATETIME ");
	    sql.append(", COALESCE(TG.SEQ , '') AS  SEQ    ,  COALESCE(TG.STAN , '') AS  STAN ");  
	    sql.append(", TG.AGENT_COMPANY_ID ,  TG.SND_COMPANY_ID ");
	    sql.append(",COALESCE(TG.PROCESSCODE,'') PROCESSCODE , COALESCE(TG.TXID,'') AS TXID ");
	    sql.append(",COALESCE(TG.TRANSACTIONAMOUNT,'0') AS TRANSACTIONAMOUNT ");
//	    sql.append(",( CASE WHEN COALESCE(TG.RESPONSECODE ,'') = '' THEN '處理中'  WHEN  COALESCE(TG.RESPONSECODE ,'') = '3001' THEN '成功' ELSE  '失敗'END) TG_RESULT "); 
	    sql.append(spSql); 
//	    sql.append(",( CASE WHEN COALESCE(OB.RESULTSTATUS ,'') = 'P' AND OB.SENDERSTATUS = '2' THEN '未完成'  WHEN  COALESCE(OB.RESULTSTATUS ,'') = 'A' THEN '成功' WHEN  COALESCE(OB.RESULTSTATUS ,'') = 'R' THEN '失敗' ELSE  '處理中' END) OB_RESULT ");
	    sql.append(",( CASE WHEN COALESCE(OB.RESULTSTATUS ,'') = 'P' AND OB.SENDERSTATUS = '2' THEN '未完成'  WHEN  COALESCE(OB.RESULTSTATUS ,'') = 'A' THEN '成功' WHEN  COALESCE(OB.RESULTSTATUS ,'') = 'R' THEN '失敗' WHEN COALESCE(OB.RESULTSTATUS ,'') = 'P' AND OB.SENDERSTATUS = '2' THEN '處理中' WHEN COALESCE(OB.RESULTSTATUS ,'') = ''  THEN ''  ELSE  '狀態不明' END) OB_RESULT ");
	    sql.append(",TG.CUST_FEE  , COALESCE(TG.ISSUERREMARK,'') ISSUERREMARK ");
//	    20160329 edit by hugo 補入帳 扣款統編 UAT待補
	    sql.append(", COALESCE( TG.OUTNATIONALID  ,'' ) OUTNATIONALID , COALESCE(TG.INNATIONALID,'') INNATIONALID ");
	    
	    sql.append(", COALESCE(TG.INBANKID,'') AS INBANKID , COALESCE(TG.OUTBANKID,'') AS OUTBANKID ");
	    sql.append(", COALESCE(TG.INBANKID,'') ||'-'||COALESCE((SELECT BRBK_NAME FROM BANK_BRANCH BR WHERE BR.BRBK_ID = TG.INBANKID ),'') AS INBANK_NAME  ");
	    sql.append(", COALESCE(TG.OUTBANKID,'') ||'-'||COALESCE((SELECT BRBK_NAME FROM BANK_BRANCH BR WHERE BR.BRBK_ID = TG.OUTBANKID ),'') AS OUTBANK_NAME  ");
	    sql.append(", COALESCE(TG.INACCOUNTNUM ,'') AS INACCOUNTNUM  , COALESCE(TG.OUTACCOUNTNUM ,'') AS OUTACCOUNTNUM ");
	    sql.append(", COALESCE(TG.CHECKTYPE,'') CHECKTYPE	, COALESCE(TG.BILLTYPE , '') BILLTYPE ");
	    sql.append(", COALESCE(TG.USERNUM , '') USERNUM	, COALESCE(TG.NOTE,'')	NOTE ");
	    sql.append(", COALESCE(TG.MERCHANTID , '') MERCHANTID	, COALESCE(TG.ORGMERCHANTID , '')	ORGMERCHANTID ");
	    sql.append(", COALESCE(TG.ORDERNUM , '') ORDERNUM	, COALESCE(TG.ORGORDERNUM,'') ORGORDERNUM ");
	    sql.append(", COALESCE(TG.TERMINERID,'') TERMINERID	, COALESCE(TG.ORGTERMINERID,'')	ORGTERMINERID ");
	    sql.append(", COALESCE(TG.BILLDATA,'') BILLDATA	");		
	    sql.append(", COALESCE(TG.ORGBILLDATA,'') ORGBILLDATA ");				
	    sql.append(", COALESCE(TG.VERIFYDATA,'') VERIFYDATA	");	
	    
	    sql.append(", COALESCE(TG.PFCLASS,'') PFCLASS	");	
	    sql.append(", COALESCE(TG.TOLLID,'') TOLLID	");	
	    sql.append(", COALESCE(TG.CHARGETYPE,'') CHARGETYPE	");	
	    sql.append(", COALESCE(TG.BILLFLAG,'') BILLFLAG	");	
	    sql.append(", COALESCE(TG.CHECKDATA,'') CHECKDATA	");
	    
	    sql.append(", COALESCE(TG.VERIFIEDDATA,'')	VERIFIEDDATA		, COALESCE(TG.ORGTRANSACTIONNO,'')	ORGTRANSACTIONNO ");
	    sql.append(", COALESCE(TG.ORGTRANSACTIONDATE,'') ORGTRANSACTIONDATE	, COALESCE(TG.ORGTRANSACTIONAMOUNT,'')	ORGTRANSACTIONAMOUNT ");
	   
	    sql.append(", COALESCE(OB.PCODE,'')	OB_PCODE , COALESCE(TG.PCODE,'') TG_PCODE    , COALESCE(TG.RESPONSECODE,'')	RESPONSECODE  ,TG.TXN_NAME,TG.RECEIVERID ");
	    sql.append(", COALESCE((SELECT TEC.ERR_DESC  FROM TXN_ERROR_CODE TEC WHERE TEC.ERROR_ID = TG. RESPONSECODE),'') AS RESPONSECODE_NAME ");
	    sql.append(", COALESCE((SELECT ETC.EACH_TXN_NAME FROM EACH_TXN_CODE ETC WHERE ETC.EACH_TXN_ID = OB.PCODE ),'') AS OB_PCODE_NAME ");
	    sql.append(", COALESCE((SELECT ETC.EACH_TXN_NAME FROM EACH_TXN_CODE ETC WHERE ETC.EACH_TXN_ID = TG.PCODE ),'') AS TG_PCODE_NAME ");
	    sql.append(", COALESCE((SELECT ATC.AGENT_TXN_NAME FROM AGENT_TXN_CODE ATC WHERE ATC.AGENT_TXN_ID = TG.PROCESSCODE ),'') AS PROCESSCODE_NAME ");
	    
	    sql.append("FROM VW_TXNLOG TG ");  
	    sql.append("LEFT JOIN ONBLOCKTAB OB ON TG.BIZDATE = OB.BIZDATE AND TG.STAN = OB.STAN ");  
    sql.append(sqlPath);
	sql.append(") ");
	sql.append("SELECT * FROM TEMP ");
	sql.append(sqlPath2);
	sql.append(orderbySQL);
	System.out.println("getSQL.sql>>"+sql);
	return sql.toString();
}


//TODO 之後可能會取消
/**
 * 
 * @param sqlPath
 * @param sqlPath2
 * @param orderbySQL
 * @return
// */
//public String getDetailSQL(String sqlPath ,String sqlPath2,String orderbySQL ){
//	StringBuffer sql = new StringBuffer();
//	sql.append(" WITH TEMP AS ( ");
//	sql.append(" SELECT   COALESCE(TG.SEQ , '') AS  SEQ  ,COALESCE(TG.BIZDATE  , '') AS  BIZDATE  , COALESCE(TG.CLEARINGPHASE  , '') AS  CLEARINGPHASE ,  COALESCE(TG.TXDATE  , '') AS  TXDATE ,  COALESCE(TG.STAN , '') AS  STAN   ,  COALESCE(TG.ISSUERID,'') AS COMPANY_ID  ");
//	sql.append(" , COALESCE(TG.TXID,'') AS TXID , COALESCE(TG.PCODE,'') AS PCODE ,COALESCE(TG.TRANSACTIONAMOUNT,'0') AS TRANSACTIONAMOUNT  ");
//	sql.append(" , COALESCE(TG.TRANSACTIONDATETIME , '' ) AS TRANSACTIONDATETIME , TRANSLATE('abcd-ef-gh xx:yy:zz', TG.TRANSACTIONDATETIME, 'abcdefghxxyyzz') AS TMP_TXDATE , COALESCE(OB.TXFROM , '') AS  TXFROM ");
//	sql.append(" , COALESCE(TG.TXID,'')  ||'-'|| COALESCE((SELECT TC.TXN_NAME FROM TXN_CODE TC WHERE TC.TXN_ID = TG.TXID ),'') AS TXN_NAME  ");
//	sql.append(" , COALESCE(TG.PCODE,'') ||'-'|| COALESCE((SELECT ETC.EACH_TXN_NAME FROM EACH_TXN_CODE ETC WHERE ETC.EACH_TXN_ID = TG.PCODE ),'') AS PCODE_NAME  ");
//	sql.append(" , COALESCE(TG.INBANKID,'') AS INBANKID , COALESCE(TG.OUTBANKID,'') AS OUTBANKID  ");
//	sql.append(" , COALESCE(TG.INACCOUNTNUM ,'') AS INACCOUNTNUM  , COALESCE(TG.OUTACCOUNTNUM ,'') AS OUTACCOUNTNUM   ");
//	sql.append(" , COALESCE(TG.INBANKID,'') ||'-'||COALESCE((SELECT BRBK_NAME FROM BANK_BRANCH BR WHERE BR.BRBK_ID = TG.INBANKID ),'') AS INBANK_NAME  ");
//	sql.append(" , COALESCE(TG.OUTBANKID,'') ||'-'||COALESCE((SELECT BRBK_NAME FROM BANK_BRANCH BR WHERE BR.BRBK_ID = TG.OUTBANKID ),'') AS OUTBANK_NAME  ");
//	sql.append(" , COALESCE( (SELECT COMPANY_ABBR_NAME FROM AGENT_PROFILE AP WHERE AP.COMPANY_ID = TG.ISSUERID),'') AS COMPANY_ABBR_NAME  ");
//	sql.append(" ,(CASE WHEN COALESCE(TG.PCODE ,'') IN ('2505', '2506', '2705', '2706') THEN    COALESCE( OUTNATIONALID  ,'' )ELSE COALESCE(INNATIONALID,'')  END ) AS SND_COMPANY_ID   ");
//	sql.append(" ,(CASE WHEN COALESCE(TG.PCODE ,'') IN ('2505', '2506', '2705', '2706') THEN    COALESCE( EACHUSER.GETCOMPANY_ABBR(COALESCE( OUTNATIONALID  ,'' )),'') ELSE COALESCE( EACHUSER.GETCOMPANY_ABBR(COALESCE(INNATIONALID,'') ),'') END ) AS SND_COMPANY_ABBR_NAME  "); 
//	sql.append(" ,(CASE WHEN COALESCE(OB.RESULTSTATUS , '') = 'P' AND  COALESCE(OB.SENDERSTATUS,'') ='1' THEN '處理中' WHEN COALESCE(OB.RESULTSTATUS , '') = 'P' AND COALESCE(OB.SENDERSTATUS,'') ='2' THEN '未完成'     WHEN COALESCE(OB.RESULTSTATUS , '') = 'A' THEN '成功' WHEN COALESCE(OB.RESULTSTATUS , '') = 'R' THEN '失敗' ELSE '狀態不明' END   )AS  RESULTSTATUS ");
//	sql.append(" ,TRANSLATE('abcd-ef-gh ij:kl:mn', OB.EACHDT, 'abcdefghijklmn') AS EACHDT, COALESCE( OB.RECEIVERID , '') RECEIVERID , COALESCE(OB.RECEIVERBANK ,'') RECEIVERBANK ");
//	sql.append(" , COALESCE(TG.INNATIONALID,'') INNATIONALID  , COALESCE(TG.OUTNATIONALID,'') OUTNATIONALID   ");
//	sql.append(" ,DECIMAL(EACHUSER.ISNUMERICII(OB.SENDERFEE,OB.txdate, OB.stan, OB.RESULTSTATUS),7,2) NewSENDERFEE ");
//	sql.append(" ,DECIMAL(EACHUSER.ISNUMERICII(OB.INFEE, OB.txdate, OB.stan, OB.RESULTSTATUS),7,2) NewINFEE ");
//	sql.append(" ,DECIMAL(EACHUSER.ISNUMERICII(OB.OUTFEE, OB.txdate, OB.stan, OB.RESULTSTATUS),7,2) NewOUTFEE  ");
//	sql.append(" ,DECIMAL(EACHUSER.ISNUMERICII(OB.EACHFEE, OB.txdate, OB.stan, OB.RESULTSTATUS),7,2) NewEACHFEE  ");
//	sql.append(" ,RC1 || '-' || (SELECT B.ERR_DESC FROM TXN_ERROR_CODE B WHERE OB.RC1=B.ERROR_ID) ERR_DESC1 ");
//	sql.append(" ,RC2 || '-' || (SELECT B.ERR_DESC FROM TXN_ERROR_CODE B WHERE OB.RC2=B.ERROR_ID) ERR_DESC2 ");
//	sql.append(" ,RC3 || '-' || (SELECT B.ERR_DESC FROM TXN_ERROR_CODE B WHERE OB.RC3=B.ERROR_ID) ERR_DESC3 ");
//	sql.append(" ,RC4 || '-' || (SELECT B.ERR_DESC FROM TXN_ERROR_CODE B WHERE OB.RC4=B.ERROR_ID) ERR_DESC4 ");
//	sql.append(" ,RC5 || '-' || (SELECT B.ERR_DESC FROM TXN_ERROR_CODE B WHERE OB.RC5=B.ERROR_ID) ERR_DESC5 ");
//	sql.append(" ,RC6 || '-' || (SELECT B.ERR_DESC FROM TXN_ERROR_CODE B WHERE OB.RC6=B.ERROR_ID) ERR_DESC6 ");
//	sql.append(" , TRANSLATE('abcd-ef-gh ij:kl:mn.opq',OB.UPDATEDT,'abcdefghijklmnopq') AS UPDATEDT ");
//	sql.append(" , COALESCE((SELECT B.ERR_DESC FROM TXN_ERROR_CODE B WHERE OB.CONRESULTCODE = B.ERROR_ID),'') AS CONRESULTCODE_DESC ");
//	sql.append(" , COALESCE(OB.SENDERBANKID,'') ||'-'||COALESCE((SELECT BRBK_NAME FROM BANK_BRANCH BR WHERE BR.BRBK_ID = OB.SENDERBANKID ),'') AS SENDERBANK_NAME  ");
//	sql.append(" , COALESCE(OB.ACCTCODE,'') AS ACCTCODE ");
//	sql.append(" ,DECIMAL(EACHUSER.ISNUMERIC(substr(TG.HANDLECHARGE,1,3) || '.' || substr(TG.HANDLECHARGE,4,2))  ,7,2)  AS HANDLECHARGE   ");
//	sql.append(" ,COALESCE(TG.USERNUM ,'') USERNUM ,COALESCE(TG.NOTE ,'') NOTE	 ,COALESCE(TG.MERCHANTID ,'') MERCHANTID   ");
//	sql.append(" ,COALESCE(TG.ORDERNUM ,'') ORDERNUM ,COALESCE(TG.TERMINERID ,'') TERMINERID ,COALESCE(TG.ORGTRANSACTIONAMOUNT ,'')	ORGTRANSACTIONAMOUNT ,COALESCE(TG.ORGTRANSACTIONDATE ,'') ORGTRANSACTIONDATE   ");
//	sql.append(" ,COALESCE(TG.ORGTERMINERID ,'') ORGTERMINERID	,COALESCE(TG.ORGMERCHANTID ,'') ORGMERCHANTID	,COALESCE(TG.ORGORDERNUM ,'') ORGORDERNUM ");
//	sql.append(" FROM TXNLOG TG   ");
//	sql.append(" LEFT JOIN ONBLOCKTAB OB ON TG.BIZDATE = OB.BIZDATE AND TG.STAN = OB.STAN   ");
//	sql.append(sqlPath);
//	sql.append("  )   ");
//	sql.append("   SELECT *  FROM TEMP  ");
//	sql.append(sqlPath2);
//	sql.append(orderbySQL);
//	System.out.println("getSQL.sql>>"+sql);
//	return sql.toString();
//}

public Map getConditionData(Map<String,String> params , String conditionKey){
	List<String> keyList = JSONUtils.toList(conditionKey);
//	List<String> values = new LinkedList<String>();
	Map<String, String> values = new HashMap<String,String>();
	String TG = "TG.";
	String OB = "OB.";
	Map retMap = new HashMap();
	StringBuffer sql = new StringBuffer();
	StringBuffer sql2 = new StringBuffer();
	int i = 0 , j=0;
	try {
		sql.append(" WHERE 1=1 ");
		i = 1 ;
		System.out.println("params>>"+params);
		for(String key :params.keySet()){
			if(keyList.contains(key)){
				
				if(StrUtils.isNotEmpty(params.get(key))){
					if(key.equals("ROWNUMBER")){
						if(j==0){sql2.append(" WHERE ");}
						if(j!=0){sql2.append(" AND ");}
						sql2.append(key+" >= :"+key+" AND "+key+" <= :LAST_ROWNUMBER");
						values.put(key, params.get(key));
						values.put("LAST_ROWNUMBER", params.get("LAST_ROWNUMBER"));
						j++;
						continue;
					}
					if(key.equals("GARBAGEDATA") && params.get(key).equals("N")){
						if(i==0){sql.append(" WHERE ");}
						if(i!=0){sql.append(" AND ");}
						sql.append( " COALESCE(TG.GARBAGEDATA,'') <> '*' ");
						i++;
						continue;
					}
					if(key.equals("GARBAGEDATA") && params.get(key).equals("Y")){
						continue;
					}
					if(key.equals("opAction1") && StrUtils.isEmpty(params.get("ACCOUNTNUM")) ) {
						continue;
					}
					
					if(i==0){sql.append(" WHERE ");}
					if(i!=0){sql.append(" AND ");}
					
					if(key.equals("STXDATE")  ) {
						sql.append(" TG.TXDATE >= :"+key+" AND TG.TXDATE <= :ETXDATE");
						values.put(key,DateTimeUtils.convertDate(params.get(key), "yyyymmdd", "yyyymmdd"));
						values.put("ETXDATE",DateTimeUtils.convertDate(params.get("ETXDATE"), "yyyymmdd", "yyyymmdd"));
						i++;
						continue;
					}
					
					if(key.equals("RESULTSTATUS")  ) {
						sql.append( " COALESCE(TG.TG_RESULT , '') = :"+key);
						values.put(key, params.get(key));
						i++;
						continue;
					}
					
					
					if(key.equals("opAction1") && StrUtils.isNotEmpty(params.get("ACCOUNTNUM")) ) {
						if(params.get(key).equals("IN") ){
							sql.append( " COALESCE(TG.INACCOUNTNUM , '')  = :INACCOUNTNUM");
							values.put("INACCOUNTNUM", params.get("ACCOUNTNUM"));
						}else{
							sql.append( " COALESCE(TG.OUTACCOUNTNUM , '')  = :OUTACCOUNTNUM");
							values.put("OUTACCOUNTNUM", params.get("ACCOUNTNUM"));
						}
						i++;
						continue;
					}
					
					
					if(key.equals("TXN_ID")  ) {
						sql.append( TG+"TXID = :TXID");
						values.put("TXID", params.get(key));
						i++;
						continue;
					}
					if(key.equals("SBIZDATE")  ) {
						sql.append(" TG.BIZDATE>= :"+key+" AND TG.BIZDATE <= :EBIZDATE");
						values.put(key,DateTimeUtils.convertDate(params.get(key), "yyyymmdd", "yyyymmdd"));
						values.put("EBIZDATE",DateTimeUtils.convertDate(params.get("EBIZDATE"), "yyyymmdd", "yyyymmdd"));
						i++;
					}else if( key.equals("BIZDATE") ) {
						sql.append( TG+key+" = :"+key);
						values.put(key,DateTimeUtils.convertDate(params.get(key), "yyyymmdd", "yyyymmdd"));
						i++;
					}else{
						sql.append( TG+key+" = :"+key);
						values.put(key,params.get(key));
						i++;
					}	
				}
			}
		}
		retMap.put("sqlPath", sql.toString());
		retMap.put("sqlPath2", sql2.toString());
		retMap.put("values", values);
		System.out.println("retMap>>"+retMap);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return retMap;
}
	

public Map getConditionData_TypeC(Map<String,String> params , String conditionKey){
	List<String> keyList = JSONUtils.toList(conditionKey);
//	List<String> values = new LinkedList<String>();
	Map<String, String> values = new HashMap<String,String>();
	String TG = "TG.";
	String OB = "OB.";
	Map retMap = new HashMap();
	StringBuffer sql = new StringBuffer();
	StringBuffer sql2 = new StringBuffer();
	int i = 0 , j=0;
	try {
		sql.append(" WHERE 1=1 ");
		i = 1 ;
		System.out.println("params>>"+params);
		for(String key :params.keySet()){
			if(keyList.contains(key)){
				
				if(StrUtils.isNotEmpty(params.get(key))){
					
					if(key.equals("ROWNUMBER")){
						if(j==0){sql2.append(" WHERE ");}
						if(j!=0){sql2.append(" AND ");}
						sql2.append(key+" >= :"+key+" AND "+key+" <= :LAST_ROWNUMBER");
						values.put(key, params.get(key));
						values.put("LAST_ROWNUMBER", params.get("LAST_ROWNUMBER"));
						j++;
						continue;
					}
					
					if(key.equals("GARBAGEDATA") && params.get(key).equals("N")){
						if(i==0){sql.append(" WHERE ");}
						if(i!=0){sql.append(" AND ");}
						sql.append( " COALESCE(TG.GARBAGEDATA,'') <> '*' ");
						i++;
						continue;
					}
					if(key.equals("GARBAGEDATA") && params.get(key).equals("Y")){
						continue;
					}
					
					if(key.equals("opAction1") && StrUtils.isEmpty(params.get("ACCOUNTNUM")) ) {
						continue;
					}
					sql.append(" AND ");
					
					if(key.equals("TXDATE")  ) {
						sql.append(" TG.TXDATE = :TXDATE");
						values.put(key,DateTimeUtils.convertDate(params.get(key), "yyyymmdd", "yyyymmdd"));
						i++;
						continue;
					}
					
					if(key.equals("RESULTSTATUS")  ) {
						if(params.get(key).equals("R")){
							sql.append( " COALESCE(TG.TG_RESULT , '') IN ('R','U')");
							i++;
						}else{
							sql.append( " COALESCE(TG.TG_RESULT , '') = :"+key);
							values.put(key, params.get(key));
							i++;
						}
						continue;
					}
					
					
					if(key.equals("opAction1") && StrUtils.isNotEmpty(params.get("ACCOUNTNUM")) ) {
						if(params.get(key).equals("IN") ){
							sql.append( " COALESCE(TG.INACCOUNTNUM , '')  = :INACCOUNTNUM");
							values.put("INACCOUNTNUM", params.get("ACCOUNTNUM"));
						}else{
							sql.append( " COALESCE(TG.OUTACCOUNTNUM , '')  = :OUTACCOUNTNUM");
							values.put("OUTACCOUNTNUM", params.get("ACCOUNTNUM"));
						}
						i++;
						continue;
					}
					
					
					if(key.equals("TXN_ID")  ) {
						sql.append( TG+"TXID = :TXID");
						values.put("TXID", params.get(key));
						i++;
						continue;
					}
					if(key.equals("SBIZDATE")  ) {
						sql.append(" TG.BIZDATE>= :"+key+" AND TG.BIZDATE <= :EBIZDATE");
						values.put(key,DateTimeUtils.convertDate(params.get(key), "yyyymmdd", "yyyymmdd"));
						values.put("EBIZDATE",DateTimeUtils.convertDate(params.get("EBIZDATE"), "yyyymmdd", "yyyymmdd"));
						i++;
					}else if( key.equals("BIZDATE") ) {
						sql.append( TG+key+" = :"+key);
						values.put(key,DateTimeUtils.convertDate(params.get(key), "yyyymmdd", "yyyymmdd"));
						i++;
					}else{
						sql.append( TG+key+" = :"+key);
						values.put(key,params.get(key));
						i++;
					}	
				}
			}
		}
		retMap.put("sqlPath", sql.toString());
		retMap.put("sqlPath2", sql2.toString());
		retMap.put("values", values);
		System.out.println("retMap>>"+retMap);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return retMap;
}

	public ONBLOCKTAB_Dao getOnblocktab_Dao() {
		return onblocktab_Dao;
	}

	public void setOnblocktab_Dao(ONBLOCKTAB_Dao onblocktab_Dao) {
		this.onblocktab_Dao = onblocktab_Dao;
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

	public AGENT_CR_LINE_BO getAgent_cr_line_bo() {
		return agent_cr_line_bo;
	}

	public void setAgent_cr_line_bo(AGENT_CR_LINE_BO agent_cr_line_bo) {
		this.agent_cr_line_bo = agent_cr_line_bo;
	}

	public AGENT_FEE_CODE_BO getAgent_fee_code_bo() {
		return agent_fee_code_bo;
	}

	public void setAgent_fee_code_bo(AGENT_FEE_CODE_BO agent_fee_code_bo) {
		this.agent_fee_code_bo = agent_fee_code_bo;
	}

	public AGENT_PROFILE_BO getAgent_profile_bo() {
		return agent_profile_bo;
	}

	public void setAgent_profile_bo(AGENT_PROFILE_BO agent_profile_bo) {
		this.agent_profile_bo = agent_profile_bo;
	}

	public TXN_CODE_BO getTxn_code_bo() {
		return txn_code_bo;
	}

	public void setTxn_code_bo(TXN_CODE_BO txn_code_bo) {
		this.txn_code_bo = txn_code_bo;
	}

	public AGENT_SEND_PROFILE_BO getAgent_send_profile_bo() {
		return agent_send_profile_bo;
	}

	public void setAgent_send_profile_bo(AGENT_SEND_PROFILE_BO agent_send_profile_bo) {
		this.agent_send_profile_bo = agent_send_profile_bo;
	}
	
	
	
	
}
