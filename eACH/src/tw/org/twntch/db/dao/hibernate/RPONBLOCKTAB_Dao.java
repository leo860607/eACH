package tw.org.twntch.db.dao.hibernate;

import java.lang.reflect.InvocationTargetException;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;

import tw.org.twntch.bean.TX_1_DETAIL;
import tw.org.twntch.bean.TX_2_DETAIL;
import tw.org.twntch.db.dataaccess.DataAccessException;
import tw.org.twntch.db.dataaccess.ExecuteSQL;
import tw.org.twntch.po.EACHSYSSTATUSTAB;
import tw.org.twntch.po.EACH_USER;
import tw.org.twntch.po.RPONBLOCKTAB;
import tw.org.twntch.po.RPONBLOCKTAB_PK;
import tw.org.twntch.util.AutoAddScalar;
import tw.org.twntch.util.BeanUtils;
import tw.org.twntch.util.StrUtils;

public class RPONBLOCKTAB_Dao extends HibernateEntityDao<RPONBLOCKTAB, RPONBLOCKTAB_PK> {

	
	public List<Map> getRptData(String sql , List<String> values){
		List<Map> list = null;
		try {
			SQLQuery query =  getCurrentSession().createSQLQuery(sql.toString());
			int i=0;
			System.out.println("values>>"+values);
			for( String val :values ){
				query.setParameter(i ,val);
				i++;
			}
			query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			list = query.list();
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		return list;
	}
	
	public List<Map> getRptData(String sql , Map<String,String> values){
		List<Map> list = null;
		try {
			SQLQuery query =  getCurrentSession().createSQLQuery(sql.toString());
			System.out.println("values>>"+values);
			for( String key :values.keySet() ){
				query.setParameter(key ,values.get(key));
			}
			query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			list = query.list();
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		return list;
	}
	
	
	/**
	 * 報表:交易明細
	 * @param sqlPath
	 * @param values
	 * @return
	 */
	public List<Map> getTx_1_Detail_Data_ForRpt(String sqlPath , List<String> values){
		StringBuffer sql = new StringBuffer();
		List<Map> list = null;
		try {
			sql.append(" SELECT row_number() over (order by TXDT)  as SEQNO");
			sql.append(" ,(CASE WHEN coalesce(VARCHAR(a.txDT),'') ='' THEN '' ELSE (CAST (YEAR(a.txDT)-1911 AS CHAR(3)) ||''|| SUBSTR( REPLACE(CAST( DATE(a.txdt) AS CHAR(10)),'-','/'),5,6)  ||'\n'|| SUBSTR(REPLACE(CAST( TIME(a.TXDT) AS CHAR(8)) ,'.',':') ,1,8))  END ) TXDT");
			sql.append(" ,a.pcode || coalesce(b.each_txn_name,'')  pcode  ,a.STAN ,a.txid, coalesce((select  txid ||'\n'|| txn_name from TXN_CODE where a.txid=txn_id),'') txn_name ,a.CLEARINGPHASE ");
			sql.append(" ,coalesce(a.SENDERBANK,'') || '\n' || coalesce((select bgbk_name from bank_group where bgbk_id=a.SENDERBANK),'') SENDERBANK ");
			sql.append(" ,coalesce(a.outBankId,'') || '\n' || coalesce((select brbk_name from bank_branch where brbk_id=a.outBankId),'') outBankId ");
			sql.append(" ,coalesce(a.inBankId ,'') || '\n' || coalesce((select brbk_name from bank_branch where brbk_id=a.inBankId),'') inBankId ");
			sql.append(" ,a.outAcctNo ,a.inAcctNo ,coalesce(a.senderId,'') ||'\n'|| coalesce(a.company_abbr,'') senderId ,Decimal(a.txAmt) txAmt ");
			sql.append(" ,(case when a.newresult='R' then '失敗' when a.newresult='P' then '處理中' else '成功' end) Resp ");
			sql.append(" ,RTRIM(coalesce(RC1 || '/','        /')) || coalesce(RC2 || '/','        /') || coalesce(RC3 || '\n','        \n') || RTRIM(coalesce(RC4 || '/','        /')) || RTRIM(coalesce(RC5 || '/','        /')) || coalesce(RC6 ,'         ') RC ");
//			手續費改抓發動行手續費
			sql.append(" ,(case when a.newresult !='R' then  coalesce(a.senderfee,0) else 0 end) fee ");
			sql.append(" FROM RPONBLOCKTAB a");
			sql.append(" left join EACH_TXN_CODE b on b.each_txn_id = a.pcode ");
			sql.append(sqlPath);
			sql.append(" order by a.CLEARINGPHASE ,a.SENDERBANK, a.PCODE  ");
			SQLQuery query =  getCurrentSession().createSQLQuery(sql.toString());
			int i=0;
			for( String val :values ){
				query.setParameter(i ,val);
				i++;
			}
//			String[] cols = " SEQNO,PCODE,TXDT,STAN,SENDERBANK,OUTBANKID,INBANKID,OUTACCTNO,INACCTNO,SENDERID,TXAMT,RESP,RC,FEE,BACKFEE,EACHFEE,BACKEACHFEE".split(",");
//			AutoAddScalar addscalar = new AutoAddScalar();
//			addscalar.addScalar(query, TX_1_DETAIL.class, cols);
//			query.setResultTransformer(Transformers.aliasToBean(TX_DETAIL.class));
			query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			list = query.list();
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		return list;
	}
	
	/**
	 * 報表:未完成明細表
	 * @param sqlPath
	 * @param values
	 * @return
	 */
	public List<Map> getTx_2_Detail_Data_ForRpt(String sqlPath , List<String> values){
		StringBuffer sql = new StringBuffer();
		List<Map> list = null;
		try {
			sql.append(" SELECT  c.business_type_id || coalesce((select (business_type_name) from BUSINESS_TYPE where business_type_id=c.business_type_id),'') biztype");
			sql.append(" ,(CASE WHEN coalesce(VARCHAR(a.TXDT),'') ='' THEN '' ELSE ('0'||CAST (YEAR(a.TXDT)-1911 AS CHAR(3)) ||''|| SUBSTR( REPLACE(CAST( DATE(a.TXDT) AS CHAR(10)),'-','/'),5,6)  ||'\n'|| SUBSTR(REPLACE(CAST( TIME(a.TXDT) AS CHAR(8)) ,'.',':') ,1,8)) END ) TXDT  ");
			sql.append(" ,a.pcode ||'\n'|| coalesce(c.each_txn_name,'')  pcode , a.STAN  ");
			sql.append(" ,a.senderBankId ||'\n'||coalesce((select brbk_name from bank_Branch where brbk_id=a.senderBankId),'')  senderBankId");
			sql.append(" ,a.OUTBANKID ||'\n'|| coalesce((select brbk_name from bank_Branch where brbk_id=a.OUTBANKID),'')  OUTBANKID");
			sql.append(" ,a.INBANKID ||'\n'|| coalesce((select brbk_name from bank_Branch where brbk_id=a.INBANKID),'')  INBANKID");
//			sql.append(" ,OUTACCTNO ,INACCTNO, STRIP(a.txAmt ,L ,'0') as txamt,(case when b.RESULTCODE='00' then 'A' else 'R' end) RESULTCODE");
			sql.append(" ,OUTACCTNO ,INACCTNO, a.txAmt as txamt,(case when b.RESULTCODE='00' then 'A' else 'R' end) RESULTCODE");
			sql.append(" FROM RPONBLOCKTAB a");
			sql.append("  left join ONPENDINGTAB b on b.OTXDate=a.txDATE and b.OSTAN=a.STAN ");
			sql.append("  left join EACH_TXN_CODE c on c.each_txn_id = a.pcode ");
			sql.append(sqlPath);
			SQLQuery query =  getCurrentSession().createSQLQuery(sql.toString());
			int i = 0;
			for( String val :values ){
				query.setParameter(i, val);
				i++;
			}
//			query.addScalar("TXDT",Hibernate.STRING);
//			String[] cols = "BIZTYPE,PCODE,TXDT,STAN,SENDERBANKID,OUTBANKID,INBANKID,OUTACCTNO,INACCTNO,TXAMT,RESULTCODE".split(",");
//			AutoAddScalar addscalar = new AutoAddScalar();
//			addscalar.addScalar(query, TX_2_DETAIL.class, cols);
//			query.setResultTransformer(Transformers.aliasToBean(TX_2_DETAIL.class));
			query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			list = query.list();
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		return list;
	}
	
	/**
	 * 報表:
	 * @param sqlPath
	 * @param values
	 * @return
	 */
	public List<Map> getTx_Detail_Data_ForRpt(String sql ,  List<String> values){
		List<Map> list = null;
		try {
			SQLQuery query =  getCurrentSession().createSQLQuery(sql.toString());
			int i = 0;
			for( String val :values ){
				query.setParameter(i, val);
				i++;
			}
//			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			list = query.list();
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		return list;
	}
	
	
	
	/**
	 * 報表:交易資料日統計-銀行別
	 * @param sqlPath
	 * @param values
	 * @return
	 */
	public List<Map> getSt_1_Detail_Data_ForRpt(String sqlPath, List<String> values){
		StringBuffer sql = new StringBuffer();
		List<Map> list = new ArrayList<Map>();;
		try {
			sql.append("WITH TEMP AS ( ");
			sql.append("    SELECT a.txDate,a.PCODE,coalesce(a.senderAcquire, a.senderBankId) senderAcquire,a.inAcquire, ");
			sql.append("    (case when a.resultstatus='P' then (case when b.RESULTCODE='00' then 'A' when b.RESULTCODE='01' then 'R' else 'P' end) else  a.resultstatus end ) resultstatus, ");
			sql.append("    a.TXAMT,RC1,RC2,RC3,RC4,RC5,RC6 ");
			sql.append("    FROM ONBLOCKTAB A left join ONPENDINGTAB b on b.OTXDate=a.txDATE and b.OSTAN=a.STAN ");
			sql.append("	" + sqlPath);
			sql.append(") ");
			sql.append("SELECT ");
			sql.append("'G' FAKE_GROUP, ");
			sql.append("a.PCODE || '-' || ( SELECT coalesce(EACH_TXN_NAME,'') From EACH_TXN_CODE Where EACH_TXN_ID=a.PCODE) PCODE, ");
			sql.append("COALESCE(a.senderAcquire || '-' || (select coalesce(bgbk_name,'') from bank_group where bgbk_id=a.senderAcquire),'') senderAcquire, ");
			sql.append("(case when a.resultstatus='A' then '成功' when a.resultstatus='R' then '失敗' when a.resultstatus='P' then '處理中' else 'NA' end) resultstatus, ");
			sql.append("count(*) FireCount ,CAST(COALESCE(CHAR(sum(Decimal(a.TXAMT))),'0') AS DECIMAL(16,2)) FireAmt, ");
			sql.append("(SELECT COUNT(*) FROM TEMP B WHERE B.PCODE=A.PCODE and B.senderAcquire = A.senderAcquire and B.resultstatus=a.resultstatus  and length(RC2)>0)  DebitCount, ");
			sql.append("(SELECT CAST(COALESCE(CHAR(sum(Decimal(b.TXAMT))), '0') AS DECIMAL(16,2)) FROM TEMP B WHERE B.PCODE=A.PCODE and B.senderAcquire = A.senderAcquire and B.resultstatus=a.resultstatus  and length(RC2)>0)  DebitAmt, "); 
			sql.append("(SELECT COUNT(*) FROM TEMP B WHERE B.PCODE=A.PCODE and B.senderAcquire = A.senderAcquire and B.resultstatus=a.resultstatus  and length(RC3)>0) SaveCount, ");
			sql.append("(SELECT CAST(COALESCE(CHAR(sum(Decimal(b.TXAMT))), '0') AS DECIMAL(16,2)) FROM TEMP B WHERE B.PCODE=A.PCODE and B.senderAcquire = A.senderAcquire and B.resultstatus=a.resultstatus and length(RC3)>0) SaveAmt ");
			sql.append("FROM TEMP a group by a.senderAcquire, a.PCODE, a.resultstatus ");
			System.out.println(sql.toString());
			SQLQuery query =  getCurrentSession().createSQLQuery(sql.toString());
			for(int i = 0; i < values.size(); i++){
				query.setParameter(i, values.get(i));
			}
			String[] cols = {"PCODE", "SENDERACQUIRE", "RESULTSTATUS", "FIRECOUNT", "FIREAMT", "DEBITCOUNT", "DEBITAMT", "SAVECOUNT", "SAVEAMT"};
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			list = query.list();
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		return list;
	}
	
	/**
	 * 報表:收費日報表
	 * @param sqlPath
	 * @param values
	 * @return
	 */
	public List<Map> getFee_1_Detail_Data_ForRpt(String conStr_1, String conStr_2){
		StringBuffer sql = new StringBuffer();
		//List<Map> list = null;
		List list = null;
		ExecuteSQL es = null;
		try {
			es = new ExecuteSQL("jdbc/ACH");
			
			sql.append("WITH TEMP AS ( ");
			sql.append("	SELECT GetBkHeadId(a.SENDERBANKID) SENDERBANKID,(case when a.ACCTCODE='0' then '0' else '1' end) ACCTCODE, ");
			sql.append("    a.CLEARINGPHASE,a.SENDERFEE,GetBkHeadId(a.inBankId) inBankId,a.INFEE,GetBkHeadId(a.outBankId) outBankId, ");
			sql.append("    a.OUTFEE,a.TXAMT,a.RESULTSTATUS,a.RC1,a.RC2,a.RC3 ");
			sql.append("    FROM RPONBLOCKTAB A ");
			sql.append("	" + (StrUtils.isNotEmpty(conStr_1)?"WHERE " + conStr_1 : ""));
			sql.append(") ");

			sql.append("SELECT ");
			sql.append("'G' AS FAKE_GROUP, ");
			sql.append("GetBkHead(a.bgbk_Id) AS bgbk_Id, ");       //銀行代號名稱
			sql.append("(case when a.CLEARINGPHASE='01' then '第一階段' else '第二階段' end) CLEARINGPHASE, ");     //清算階段
			sql.append("(case when a.ACCTCODE='1' then '一般' else '沖正' end) ACCTCODE, ");  //類別型態
			sql.append("(SELECT count(SENDERBANKID) from TEMP where  SENDERBANKID=a.bgbk_Id and CLEARINGPHASE=a.CLEARINGPHASE and ACCTCODE=a.ACCTCODE ) FireCount, "); //發動行筆數
			sql.append("DECIMAL(COALESCE(CHAR((SELECT Sum(SENDERFEE) from TEMP where  SENDERBANKID=a.bgbk_Id and CLEARINGPHASE=a.CLEARINGPHASE and ACCTCODE=a.ACCTCODE)),'0')) FireFeeAmt, ");      //發動行金額(A)
			sql.append("(SELECT count(inBankId) from TEMP where inBankId=a.bgbk_Id and CLEARINGPHASE=a.CLEARINGPHASE and ACCTCODE=a.ACCTCODE ) InBankCount, ");      //扣款行筆數
			sql.append("DECIMAL(COALESCE(CHAR((SELECT Sum(INFEE) from TEMP where  inBankId=a.bgbk_Id and CLEARINGPHASE=a.CLEARINGPHASE and ACCTCODE=a.ACCTCODE)),'0')) INFEEAmt, ");        //扣款行金額(B)
			sql.append("(SELECT count(outBankId) from TEMP where outBankId=a.bgbk_Id and CLEARINGPHASE=a.CLEARINGPHASE and ACCTCODE=a.ACCTCODE ) OutBankCount, ");   //入帳行筆數
			sql.append("DECIMAL(COALESCE(CHAR((SELECT Sum(OUTFEE) from TEMP where outBankId=a.bgbk_Id and CLEARINGPHASE=a.CLEARINGPHASE and ACCTCODE=a.ACCTCODE)),'0')) OutFeeAmt ");     //入帳行金額(C) //應收應付差額(A +B+C)
			sql.append("FROM (select distinct SENDERBANKID bgbk_Id, CLEARINGPHASE,  ACCTCODE from Temp union select distinct inBankId bgbk_Id, CLEARINGPHASE,  ACCTCODE from Temp union select distinct outBankId bgbk_Id, CLEARINGPHASE,  ACCTCODE from Temp) a ");
			sql.append(StrUtils.isEmpty(conStr_2)? "" : "WHERE " + conStr_2 + " ");
			sql.append("order by a.ACCTCODE DESC, a.bgbk_Id ASC, a.CLEARINGPHASE ASC ");
			System.out.println(sql.toString());
			//Hibernate 太笨，不認得自定義的FUNCTION，只好改用JDBC...
			/*
			SQLQuery query =  getCurrentSession().createSQLQuery(sql.toString());
			int i=0;
			for( String val :values ){
				query.setParameter(i ,val);
				i++;
			}
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			list = query.list();
			*/
			list = es.doQuery(sql.toString());
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 報表:收費月報表-日期別
	 * @param sqlPath
	 * @param values
	 * @return
	 */
	public List<Map> getFee_4_Detail_Data_ForRpt(String conStr_1, String conStr_2){
		StringBuffer sql = new StringBuffer();
		//List<Map> list = null;
		List list = null;
		ExecuteSQL es = null;
		try {
			es = new ExecuteSQL("jdbc/ACH");
			
			sql.append("WITH TEMP AS ( SELECT GetBkHeadId(a.SENDERBANKID) SENDERBANKID,(case when a.ACCTCODE='0' then '0' else '1' end) ACCTCODE, to_twdate(a.TXDATE) TXDATE,a.SENDERFEE,GetBkHeadId(a.inBankId) inBankId,a.INFEE,GetBkHeadId(a.outBankId) outBankId, a.OUTFEE,a.TXAMT,a.RESULTSTATUS,a.RC1,a.RC2,a.RC3 FROM RPONBLOCKTAB A "); 
			sql.append("WHERE " + conStr_1 + " ) "); 
			sql.append("SELECT "); 
			sql.append("a.TXN_DATE, ");
			sql.append("(SELECT count(SENDERBANKID) from TEMP where TXDATE=a.TXN_DATE) FireCount, ");
			sql.append("COALESCE((SELECT Sum(SENDERFEE) from TEMP where TXDATE=a.TXN_DATE), 0) FireFeeAmt, ");
			sql.append("(SELECT count(inBankId) from TEMP where TXDATE=a.TXN_DATE) InBankCount, ");
			sql.append("COALESCE((SELECT Sum(INFEE) from TEMP where TXDATE=a.TXN_DATE ), 0) INFEEAmt, ");
			sql.append("(SELECT count(outBankId) from TEMP where TXDATE=a.TXN_DATE) OutBankCount, ");
			sql.append("COALESCE((SELECT Sum(OUTFEE) from TEMP where  TXDATE=a.TXN_DATE), 0) OutFeeAmt "); 
			sql.append("From ( "); 		
			sql.append("SELECT substr(TXN_DATE,1,4) ||  '/' || substr(TXN_DATE,5,2) || '/' || substr(TXN_DATE,7,2) TXN_DATE FROM WK_DATE_CALENDAR "); 		
			sql.append("Where  IS_TXN_DATE='Y' AND " + conStr_2 + " "); 
			sql.append(") a "); 
			sql.append("Order by a. TXN_DATE ");
			System.out.println(sql.toString());
			//Hibernate 太笨，不認得自定義的FUNCTION，只好改用JDBC...
			/*
			SQLQuery query =  getCurrentSession().createSQLQuery(sql.toString());
			int i=0;
			for( String val :values ){
				query.setParameter(i ,val);
				i++;
			}
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			list = query.list();
			*/
			list = es.doQuery(sql.toString());
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 報表:銀行收費日報表
	 * @param sqlPath
	 * @param values
	 * @return
	 */
	public List<Map> getFee_5_Detail_Data_ForRpt(String conStr_1, String conStr_2){
		StringBuffer sql = new StringBuffer();
		//List<Map> list = null;
		List list = null;
		ExecuteSQL es = null;
		try {
			es = new ExecuteSQL("jdbc/ACH");
			sql.append("WITH TEMP AS ( ");
			sql.append("    SELECT ");
			sql.append("    ETC.BUSINESS_TYPE_ID,(ETC.BUSINESS_TYPE_ID || '-' || ETC.BUSINESS_TYPE_NAME) AS BUSINESS_TYPE_NAME, ");
			sql.append("    RP.PCODE,(RP.PCODE || '-' || ETC.EACH_TXN_NAME) AS PCODE_DESC, ");
			sql.append("    (CASE RP.ACCTCODE WHEN '1' THEN '一般' ELSE '沖正' END) AS ACCTCODE, ");    
			sql.append("    RP.TXID,COALESCE(RP.TXID || '-' || TC.TXN_NAME, RP.TXID) AS TXID_DESC, ");
			sql.append("    (CASE RP.CLEARINGPHASE WHEN '01' THEN '第一階段' WHEN '02' THEN '第二階段' ELSE '未定義' END) AS CLEARINGPHASE, ");
			sql.append("    RP.SENDERID, RP.SENDERBANKID, RP.OUTBANKID, RP.INBANKID, ");
			sql.append("    COALESCE(TFM.SND_BANK_FEE_DISC,0) AS SND_BANK_FEE_DISC, COALESCE(TFM.OUT_BANK_FEE_DISC,0) AS OUT_BANK_FEE_DISC, "); 
			sql.append("    COALESCE(TFM.IN_BANK_FEE_DISC,0) AS IN_BANK_FEE_DISC, RP.SENDERHEAD, RP.OUTHEAD, RP.INHEAD, "); 
			sql.append("    COALESCE(RP.SENDERFEE,0) AS SENDERFEE, COALESCE(RP.OUTFEE,0) AS OUTFEE, COALESCE(RP.INFEE,0) AS INFEE ");
			sql.append("    FROM RPONBLOCKTAB RP LEFT JOIN ( ");
			sql.append("        SELECT A.EACH_TXN_ID, A.EACH_TXN_NAME, B.BUSINESS_TYPE_ID, B.BUSINESS_TYPE_NAME ");
			sql.append("        FROM EACH_TXN_CODE A JOIN BUSINESS_TYPE B ON A.BUSINESS_TYPE_ID = B.BUSINESS_TYPE_ID ");
			sql.append("    ) ETC ON RP.PCODE = ETC.EACH_TXN_ID ");
			sql.append("    LEFT JOIN TXN_CODE TC ON RP.TXID = TC.TXN_ID ");
			sql.append("    LEFT JOIN ( ");
			sql.append("        SELECT ");
			sql.append("        iTFM.TXN_ID, iTFM.FEE_ID, iTFM.START_DATE, "); 
			sql.append("        FC.OUT_BANK_FEE_DISC, FC.IN_BANK_FEE_DISC, FC.TCH_FEE_DISC, FC.SND_BANK_FEE_DISC ");
			sql.append("        FROM ( ");
			sql.append("            SELECT TXN_ID, FEE_ID, MAX(START_DATE) AS START_DATE ");
			sql.append("            FROM TXN_FEE_MAPPING GROUP BY TXN_ID, FEE_ID ");
			sql.append("        ) iTFM LEFT JOIN FEE_CODE FC ON iTFM.FEE_ID = FC.FEE_ID AND iTFM.START_DATE = FC.START_DATE ");
			sql.append("    )TFM ON RP.TXID = TFM.TXN_ID ");
			sql.append("    " + (StrUtils.isEmpty(conStr_1)?"":"WHERE " + conStr_1));
			sql.append("    ORDER BY ETC.BUSINESS_TYPE_ID ASC, RP.PCODE ASC, RP.ACCTCODE DESC ");
			sql.append(") ");
			sql.append("SELECT "); 
			sql.append("DISTINCT 'G' AS FAKE_GROUP, BUSINESS_TYPE_NAME, PCODE_DESC, CLEARINGPHASE, SENDERID ");
			sql.append(",(SELECT COUNT(*) FROM TEMP WHERE " + (StrUtils.isEmpty(conStr_2)?"":"SENDERHEAD =" + conStr_2 + "AND ") + "BUSINESS_TYPE_ID = A.BUSINESS_TYPE_ID AND CLEARINGPHASE = A.CLEARINGPHASE AND PCODE_DESC = A.PCODE_DESC AND SENDERID = A.SENDERID) AS FIRECOUNT ");
			sql.append(",COALESCE((SELECT SUM(SENDERFEE) FROM TEMP WHERE " + (StrUtils.isEmpty(conStr_2)?"":"SENDERHEAD =" + conStr_2 + "AND ") + " BUSINESS_TYPE_ID = A.BUSINESS_TYPE_ID AND CLEARINGPHASE = A.CLEARINGPHASE AND PCODE_DESC = A.PCODE_DESC AND SENDERID = A.SENDERID " + (StrUtils.isEmpty(conStr_2)?"":" GROUP BY SENDERHEAD ") + "),0) AS FIREAMT ");
			sql.append(",(SELECT COUNT(*) FROM TEMP WHERE " + (StrUtils.isEmpty(conStr_2)?"":"OUTHEAD =" + conStr_2 + "AND ") + " BUSINESS_TYPE_ID = A.BUSINESS_TYPE_ID AND CLEARINGPHASE = A.CLEARINGPHASE AND PCODE_DESC = A.PCODE_DESC AND SENDERID = A.SENDERID) AS DEBITCOUNT ");
			sql.append(",COALESCE((SELECT SUM(OUTFEE) FROM TEMP WHERE " + (StrUtils.isEmpty(conStr_2)?"":"OUTHEAD =" + conStr_2 + "AND ") + " BUSINESS_TYPE_ID = A.BUSINESS_TYPE_ID AND CLEARINGPHASE = A.CLEARINGPHASE AND PCODE_DESC = A.PCODE_DESC AND SENDERID = A.SENDERID " + (StrUtils.isEmpty(conStr_2)?"":" GROUP BY OUTHEAD ") + ") ,0) AS DEBITAMT ");
			sql.append(",(SELECT COUNT(*) FROM TEMP WHERE " + (StrUtils.isEmpty(conStr_2)?"":"INHEAD =" + conStr_2 + "AND ") + " BUSINESS_TYPE_ID = A.BUSINESS_TYPE_ID AND CLEARINGPHASE = A.CLEARINGPHASE AND PCODE_DESC = A.PCODE_DESC AND SENDERID = A.SENDERID) AS SAVECOUNT ");
			sql.append(",COALESCE((SELECT SUM(INFEE) FROM TEMP WHERE " + (StrUtils.isEmpty(conStr_2)?"":"INHEAD =" + conStr_2 + "AND ") + " BUSINESS_TYPE_ID = A.BUSINESS_TYPE_ID AND CLEARINGPHASE = A.CLEARINGPHASE AND PCODE_DESC = A.PCODE_DESC AND SENDERID = A.SENDERID " + (StrUtils.isEmpty(conStr_2)?"":" GROUP BY INHEAD ") + ") ,0) AS SAVEAMT ");
			sql.append("FROM TEMP A ");
			sql.append("ORDER BY BUSINESS_TYPE_NAME ASC, PCODE_DESC ASC, CLEARINGPHASE ASC, SENDERID ASC ");
			/*
			sql.append("WITH TEMP AS ( ");
			sql.append("	SELECT ");
			sql.append("    ETC.BUSINESS_TYPE_ID,(ETC.BUSINESS_TYPE_ID || '-' || ETC.BUSINESS_TYPE_NAME) AS BUSINESS_TYPE_NAME, ");
			sql.append("    RP.PCODE,(RP.PCODE || '-' || ETC.EACH_TXN_NAME) AS PCODE_DESC, ");
			sql.append("    (CASE RP.ACCTCODE WHEN '1' THEN '一般' ELSE '沖正' END) AS ACCTCODE, ");
			sql.append("    RP.TXID,COALESCE(RP.TXID || '-' || TC.TXN_NAME, RP.TXID) AS TXID_DESC, ");
			sql.append("    (CASE RP.CLEARINGPHASE WHEN '01' THEN '第一階段' WHEN '02' THEN '第二階段' ELSE '未定義' END) AS CLEARINGPHASE, ");
			sql.append("    RP.SENDERID, RP.SENDERBANKID, RP.OUTBANKID, RP.INBANKID, ");
			sql.append("    COALESCE(TFM.SND_BANK_FEE_DISC,0) AS SND_BANK_FEE_DISC, COALESCE(TFM.OUT_BANK_FEE_DISC,0) AS OUT_BANK_FEE_DISC, "); 
			sql.append("    COALESCE(TFM.IN_BANK_FEE_DISC,0) AS IN_BANK_FEE_DISC, ");
			sql.append("	(SELECT BGBK_ID || '-' || BGBK_NAME FROM BANK_GROUP WHERE BGBK_ID = RP.SENDERHEAD) AS SENDERHEAD, ");
		    sql.append("	(SELECT BGBK_ID || '-' || BGBK_NAME FROM BANK_GROUP WHERE BGBK_ID = RP.OUTHEAD) AS OUTHEAD, ");
		    sql.append("	(SELECT BGBK_ID || '-' || BGBK_NAME FROM BANK_GROUP WHERE BGBK_ID = RP.INHEAD) AS INHEAD, ");
			sql.append("    COALESCE(RP.SENDERFEE,0) AS SENDERFEE, COALESCE(RP.OUTFEE,0) AS OUTFEE, COALESCE(RP.INFEE,0) AS INFEE ");
			sql.append("    FROM RPONBLOCKTAB RP LEFT JOIN ( ");
			sql.append("        SELECT A.EACH_TXN_ID, A.EACH_TXN_NAME, B.BUSINESS_TYPE_ID, B.BUSINESS_TYPE_NAME ");
			sql.append("        FROM EACH_TXN_CODE A JOIN BUSINESS_TYPE B ON A.BUSINESS_TYPE_ID = B.BUSINESS_TYPE_ID ");
			sql.append("    ) ETC ON RP.PCODE = ETC.EACH_TXN_ID ");
			sql.append("    LEFT JOIN TXN_CODE TC ON RP.TXID = TC.TXN_ID ");
			sql.append("    LEFT JOIN ( ");
			sql.append("        SELECT ");
			sql.append("        iTFM.TXN_ID, iTFM.FEE_ID, iTFM.START_DATE, "); 
			sql.append("        FC.OUT_BANK_FEE_DISC, FC.IN_BANK_FEE_DISC, FC.TCH_FEE_DISC, FC.SND_BANK_FEE_DISC ");
			sql.append("        FROM ( ");
			sql.append("            SELECT TXN_ID, FEE_ID, MAX(START_DATE) AS START_DATE ");
			sql.append("            FROM TXN_FEE_MAPPING GROUP BY TXN_ID, FEE_ID ");
			sql.append("        ) iTFM LEFT JOIN FEE_CODE FC ON iTFM.FEE_ID = FC.FEE_ID AND iTFM.START_DATE = FC.START_DATE ");
			sql.append("    )TFM ON RP.TXID = TFM.TXN_ID ");
			sql.append("    " + (StrUtils.isEmpty(conStr_1)? "" : "WHERE " + conStr_1));
			sql.append("    ORDER BY ETC.BUSINESS_TYPE_ID ASC, RP.PCODE ASC, RP.ACCTCODE DESC ");
			sql.append(") ");
			sql.append("SELECT 'G' AS FAKE_GROUP, ");
			sql.append("COALESCE(BANK_ID,'') AS BANK_ID, BUSINESS_TYPE_NAME, CLEARINGPHASE, PCODE_DESC, SENDERID, ");
			sql.append("(SELECT COUNT(SENDERHEAD) FROM TEMP WHERE SENDERHEAD = A.BANK_ID AND BUSINESS_TYPE_NAME = A.BUSINESS_TYPE_NAME AND CLEARINGPHASE = A.CLEARINGPHASE AND PCODE_DESC = A.PCODE_DESC AND SENDERID = A.SENDERID) AS FIRECOUNT, ");
			sql.append("(SELECT COUNT(OUTHEAD) FROM TEMP WHERE OUTHEAD = A.BANK_ID AND BUSINESS_TYPE_NAME = A.BUSINESS_TYPE_NAME AND CLEARINGPHASE = A.CLEARINGPHASE AND PCODE_DESC = A.PCODE_DESC AND SENDERID = A.SENDERID) AS DEBITCOUNT, ");
			sql.append("(SELECT COUNT(INHEAD) FROM TEMP WHERE INHEAD = A.BANK_ID AND BUSINESS_TYPE_NAME = A.BUSINESS_TYPE_NAME AND CLEARINGPHASE = A.CLEARINGPHASE AND PCODE_DESC = A.PCODE_DESC AND SENDERID = A.SENDERID) AS SAVECOUNT, ");
			sql.append("COALESCE((SELECT SUM(SENDERFEE) FROM TEMP WHERE SENDERHEAD = A.BANK_ID AND BUSINESS_TYPE_NAME = A.BUSINESS_TYPE_NAME AND CLEARINGPHASE = A.CLEARINGPHASE AND PCODE_DESC = A.PCODE_DESC AND SENDERID = A.SENDERID GROUP BY SENDERHEAD),0) AS FIREAMT, ");
			sql.append("COALESCE((SELECT SUM(OUTFEE) FROM TEMP WHERE OUTHEAD = A.BANK_ID AND BUSINESS_TYPE_NAME = A.BUSINESS_TYPE_NAME AND CLEARINGPHASE = A.CLEARINGPHASE AND PCODE_DESC = A.PCODE_DESC AND SENDERID = A.SENDERID GROUP BY OUTHEAD),0) AS DEBITAMT, ");
			sql.append("COALESCE((SELECT SUM(INFEE) FROM TEMP WHERE INHEAD = A.BANK_ID AND BUSINESS_TYPE_NAME = A.BUSINESS_TYPE_NAME AND CLEARINGPHASE = A.CLEARINGPHASE AND PCODE_DESC = A.PCODE_DESC AND SENDERID = A.SENDERID GROUP BY INHEAD),0) AS SAVEAMT ");
			sql.append("FROM (SELECT DISTINCT SENDERHEAD AS BANK_ID, BUSINESS_TYPE_NAME, CLEARINGPHASE, PCODE_DESC, SENDERID FROM TEMP ");
			sql.append("UNION SELECT DISTINCT OUTHEAD AS BANK_ID, BUSINESS_TYPE_NAME, CLEARINGPHASE, PCODE_DESC, SENDERID FROM TEMP ");
			sql.append("UNION SELECT DISTINCT INHEAD AS BANK_ID, BUSINESS_TYPE_NAME, CLEARINGPHASE, PCODE_DESC, SENDERID FROM TEMP ");
			sql.append(") AS A ");
			sql.append((StrUtils.isEmpty(conStr_2)? "" : "WHERE " + conStr_2) + " ");
			sql.append("ORDER BY BANK_ID, BUSINESS_TYPE_NAME, CLEARINGPHASE, PCODE_DESC, SENDERID ");
			*/
			System.out.println(sql.toString());
			//Hibernate 太笨，不認得自定義的FUNCTION，只好改用JDBC...
			/*
			SQLQuery query =  getCurrentSession().createSQLQuery(sql.toString());
			int i=0;
			for( String val :values ){
				query.setParameter(i ,val);
				i++;
			}
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			list = query.list();
			*/
			list = es.doQuery(sql.toString());
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 報表:代理清算交換差額表
	 * @param sqlPath
	 * @param values
	 * @return
	 */
	public List<Map> getCl_1_Detail_Data_ForRpt(String conStr_1, String val){
		StringBuffer sql = new StringBuffer();
		//List<Map> list = null;
		List list = null;
		ExecuteSQL es = null;
		try {
			es = new ExecuteSQL("jdbc/ACH");
			
			sql.append("WITH TEMP AS ( ");
			sql.append("	SELECT      (case when a.ACCTCODE='0' then '0' else '1' end) ACCTCODE, ");
			sql.append("    a.CLEARINGPHASE,  ");
			sql.append("    GetBkHeadId(a.INCLEARING) INCLEARING,  ");
			sql.append("    GetBkHeadId(a.OUTCLEARING) OUTCLEARING, ");
			sql.append("    a.TXAMT, ");
			sql.append("    a.RESULTSTATUS, ");
			sql.append("    a.RC1, ");
			sql.append("    a.RC2, ");
			sql.append("    a.RC3  ");
			sql.append("    FROM RPONBLOCKTAB  A ");
			sql.append(conStr_1);
			sql.append(") ");

			sql.append(" SELECT  GetBkHead(a.bgbk_Id)     bgbk_Id,      ");//銀行代號名稱
			sql.append(" (case when a.CLEARINGPHASE='01' then '第一階段' else '第二階段' end)  CLEARINGPHASE, ");//清算階段
			sql.append(" (SELECT count(INCLEARING)  from TEMP where  INCLEARING=a.bgbk_Id and CLEARINGPHASE=a.CLEARINGPHASE and ACCTCODE='1' ) InCount, ");//入帳筆數
			sql.append(" (SELECT Sum(TXAMT)  from TEMP where  INCLEARING=a.bgbk_Id and CLEARINGPHASE=a.CLEARINGPHASE and ACCTCODE='1') INAmt, ");//入帳金額(B)
			sql.append(" (SELECT count(INCLEARING)  from TEMP where  INCLEARING=a.bgbk_Id and CLEARINGPHASE=a.CLEARINGPHASE and ACCTCODE='0' ) InBackCount, ");//扣款沖正筆數
			sql.append(" (SELECT Sum(TXAMT)  from TEMP where  INCLEARING=a.bgbk_Id and CLEARINGPHASE=a.CLEARINGPHASE and ACCTCODE='0') InBackAmt, ");//扣款沖正金額(B)
			sql.append(" (SELECT count(OUTCLEARING)  from TEMP where  OUTCLEARING=a.bgbk_Id and CLEARINGPHASE=a.CLEARINGPHASE and ACCTCODE='1' ) OutCount, ");//扣款筆數
			sql.append(" (SELECT Sum(TXAMT)  from TEMP where  OUTCLEARING=a.bgbk_Id and CLEARINGPHASE=a.CLEARINGPHASE and ACCTCODE='1') OutAmt, ");//扣款金額(C)
			sql.append(" (SELECT count(OUTCLEARING)  from TEMP where  OUTCLEARING=a.bgbk_Id and CLEARINGPHASE=a.CLEARINGPHASE and ACCTCODE='0' ) OutBackCount, ");//入帳沖正筆數
			sql.append(" (SELECT Sum(TXAMT)  from TEMP where  OUTCLEARING=a.bgbk_Id and CLEARINGPHASE=a.CLEARINGPHASE and ACCTCODE='0') OutBackAmt ");//入帳沖正金額(C)
			sql.append(" FROM ");
			sql.append(" (select distinct INCLEARING bgbk_Id , CLEARINGPHASE from Temp ");
			sql.append(" union select distinct OUTCLEARING bgbk_Id , CLEARINGPHASE from Temp ");
			sql.append(" )  a ");
			sql.append(" where a.bgbk_Id = '"+val+  "'");
			sql.append(" order by a.bgbk_Id , a.CLEARINGPHASE desc ");
			System.out.println(sql.toString());
			//Hibernate 太笨，不認得自定義的FUNCTION，只好改用JDBC...
			/*
			SQLQuery query =  getCurrentSession().createSQLQuery(sql.toString());
			int i=0;
			for( String val :values ){
				query.setParameter(i ,val);
				i++;
			}
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			list = query.list();
			*/
			list = es.doQuery(sql.toString());
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	public boolean bat_RPONBLOCKTAB(String twYear) throws SQLException{
		boolean result = false;
		Session session = getSessionFactory().openSession();
		session.beginTransaction();
		String sql = "CALL EACHUSER.BAT_RPONBLOCKTABII()";
		try {
			CallableStatement query = session.connection().prepareCall(sql);
			query.execute();
			session.beginTransaction().commit();
			result = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			session.beginTransaction().rollback();
			result = false;
		}
		return result;
	}
	
	/**
	 * 報表:應收應付差額明細表
	 * @param sqlPath
	 * @param values
	 * @return
	 */
	public List<Map> getCl_3_Detail_Data_ForRpt(String conStr_1){
		StringBuffer sql = new StringBuffer();
		//List<Map> list = null;
		List list = null;
		ExecuteSQL es = null;
		try {
			es = new ExecuteSQL("jdbc/ACH");
			
			sql.append("WITH TEMP AS ( ");
			sql.append("	SELECT      (case when a.ACCTCODE='0' then '0' else '1' end) ACCTCODE ");
			sql.append("    ,a.CLEARINGPHASE  ");			
			sql.append("    , GetBkHeadId(a.INCLEARING) INCLEARING ");			
			sql.append("    , GetBkHeadId(a.OUTCLEARING) OUTCLEARING ");			
			sql.append("    ,a.TXAMT ");
			sql.append("    ,a.RESULTSTATUS  ");
			sql.append("    ,b.TXN_TYPE  ");			
			sql.append("    FROM RPONBLOCKTAB  A ");
			sql.append("    left join txn_code b on b.TXN_ID=a.TXID  ");
			sql.append(conStr_1);
			sql.append(") ");

			sql.append(" SELECT  GetBkHead(a.bgbk_Id)     bgbk_Id ,CLEARINGPHASE  ");			
			sql.append("  ,(SELECT Sum(TXAMT)  from TEMP where  OUTCLEARING=a.bgbk_Id  and ACCTCODE='1'  and TXN_TYPE='SD') OutAmt ");//代收金額(A)
			sql.append("  ,(SELECT Sum(TXAMT)  from TEMP where  OUTCLEARING=a.bgbk_Id  and ACCTCODE='0'  and TXN_TYPE='SD') OutBackAmt  ");//代收沖正金額(B)  代收案件(應收)差額(A-B)
			sql.append("  ,(SELECT Sum(TXAMT)  from TEMP where  INCLEARING=a.bgbk_Id  and ACCTCODE='1' and TXN_TYPE='SC') INAmt ");//代付金額(C)
			sql.append("  ,(SELECT Sum(TXAMT)  from TEMP where  INCLEARING=a.bgbk_Id  and ACCTCODE='0' and TXN_TYPE='SC' ) InBackAmt ");//代付金額沖正(D) 代付案件(應付)差額(C-D) 淨差額 = (A-B) - (C-D)
			sql.append(" FROM (select distinct INCLEARING bgbk_Id ,CLEARINGPHASE from Temp ");//
			sql.append(" union select distinct OUTCLEARING bgbk_Id ,CLEARINGPHASE from Temp)  a ");//
			sql.append(" order by a.bgbk_Id  desc ");//			
			System.out.println(sql.toString());
			
			list = es.doQuery(sql.toString());
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 報表:應收應付差額明細表
	 * @param sqlPath
	 * @param values
	 * @return
	 */
	public List<EACHSYSSTATUSTAB> get_before_Date(){
		List<Map> list = null;
		EACHSYSSTATUSTAB po =null;
		List<EACHSYSSTATUSTAB> list2 = new ArrayList<EACHSYSSTATUSTAB>();
		try {
			
			SQLQuery query =  getCurrentSession().createSQLQuery(" SELECT PREVDATE FROM  EACHSYSSTATUSTAB ");										
			query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);			
			list = query.list();	
			for(Map map : list){
				 po = new EACHSYSSTATUSTAB();
				 BeanUtils.copyProperties(po, map);
				 list2.add(po);
			}
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list2;
	}
}
