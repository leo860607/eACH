package tw.org.twntch.db.dao.hibernate;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;

import tw.org.twntch.bean.BANK_STATUS;
import tw.org.twntch.po.WK_DATE_CALENDAR;
import tw.org.twntch.util.AutoAddScalar;
import tw.org.twntch.util.zDateHandler;

public class MONITOR_Dao extends HibernateEntityDao<BANK_STATUS, Serializable> {
	public List<BANK_STATUS> getData(){
		List<BANK_STATUS> list = null;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT AP.BGBK_ID, AP.APID, AP.MBAPSTATUS, SYS.MBSYSSTATUS, ");
		sql.append("CASE B.IS_ACTIVE WHEN 'N' THEN '停用' ELSE (");
		sql.append("	CASE AP.MBAPSTATUS ");
		sql.append("	WHEN '1' THEN '簽到' ");
		sql.append("	WHEN '2' THEN '暫時簽退' ");
		sql.append("	WHEN '9' THEN '簽退' ELSE AP.MBAPSTATUS END ");
		sql.append(") END AS MBAPSTATUSNAME, ");
		sql.append("CASE B.IS_ACTIVE WHEN 'N' THEN '停用' ELSE ( ");
		sql.append("	CASE SYS.MBSYSSTATUS ");
		sql.append("	WHEN '1' THEN '開機' ");
		sql.append("	WHEN '2' THEN '押碼基碼同步' ");
		sql.append("	WHEN '3' THEN '訊息通知' ");
		sql.append("	WHEN '9' THEN '關機' ELSE SYS.MBSYSSTATUS END ");
		sql.append(") END AS MBSYSSTATUSNAME ");
		sql.append("FROM BANKAPSTATUSTAB AP JOIN BANKSYSSTATUSTAB SYS ON AP.BGBK_ID = SYS.BGBK_ID ");
		sql.append("LEFT JOIN ( ");
		sql.append("	SELECT BGBK_ID, ");
//		20150420 edit by hugo 不可寫死，要改系統日 
//		sql.append("	(CASE WHEN '01040224' BETWEEN ");
		sql.append("	(CASE WHEN '"+zDateHandler.getTWDate()+"' BETWEEN ");
		sql.append("	(CASE LENGTH(RTRIM(COALESCE(ACTIVE_DATE,''))) WHEN 0 THEN '99999999' ELSE ACTIVE_DATE END) AND "); 
		sql.append("	(CASE LENGTH(RTRIM(COALESCE(STOP_DATE,''))) WHEN 0 THEN '99999999' ELSE STOP_DATE END) "); 
		sql.append("	THEN 'Y' ELSE 'N' END) AS IS_ACTIVE ");
		sql.append("	FROM EACHUSER.BANK_GROUP ");
		sql.append(") AS B ON AP.BGBK_ID = B.BGBK_ID ORDER BY AP.BGBK_ID ");
		
		try{
			SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
			String cols = "BGBK_ID,APID,MBAPSTATUS,MBSYSSTATUS,MBAPSTATUSNAME,MBSYSSTATUSNAME";
			AutoAddScalar addscalar = new AutoAddScalar();
			addscalar.addScalar(query, BANK_STATUS.class, cols.split(","));
			query.setResultTransformer(Transformers.aliasToBean(BANK_STATUS.class));
			list = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return list;
	}
	
	public List<BANK_STATUS> getData(String bgbkId, String apId){
		List<BANK_STATUS> list = null;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT AP.BGBK_ID, AP.APID, AP.MBAPSTATUS, SYS.MBSYSSTATUS, ");
		sql.append("CASE B.IS_ACTIVE WHEN 'N' THEN '停用' ELSE (");
		sql.append("	CASE AP.MBAPSTATUS ");
		sql.append("	WHEN '1' THEN '簽到' ");
		sql.append("	WHEN '2' THEN '暫時簽退' ");
		sql.append("	WHEN '9' THEN '簽退' ELSE AP.MBAPSTATUS END ");
		sql.append(") END AS MBAPSTATUSNAME, ");
		sql.append("CASE B.IS_ACTIVE WHEN 'N' THEN '停用' ELSE ( ");
		sql.append("	CASE SYS.MBSYSSTATUS ");
		sql.append("	WHEN '1' THEN '開機' ");
		sql.append("	WHEN '2' THEN '押碼基碼同步' ");
		sql.append("	WHEN '3' THEN '訊息通知' ");
		sql.append("	WHEN '9' THEN '關機' ELSE SYS.MBSYSSTATUS END ");
		sql.append(") END AS MBSYSSTATUSNAME ");
		sql.append("FROM BANKAPSTATUSTAB AP JOIN BANKSYSSTATUSTAB SYS ON AP.BGBK_ID = SYS.BGBK_ID ");
		sql.append("LEFT JOIN ( ");
		sql.append("	SELECT BGBK_ID, ");
//		20150420 edit by hugo 不可寫死，改帶系統日
//		sql.append("	(CASE WHEN '01040224' BETWEEN ");
		sql.append("	(CASE WHEN '"+zDateHandler.getTWDate()+"' BETWEEN ");
		sql.append("	(CASE LENGTH(RTRIM(COALESCE(ACTIVE_DATE,''))) WHEN 0 THEN '99999999' ELSE ACTIVE_DATE END) AND "); 
		sql.append("	(CASE LENGTH(RTRIM(COALESCE(STOP_DATE,''))) WHEN 0 THEN '99999999' ELSE STOP_DATE END) "); 
		sql.append("	THEN 'Y' ELSE 'N' END) AS IS_ACTIVE ");
		sql.append("	FROM EACHUSER.BANK_GROUP ");
		sql.append(") AS B ON AP.BGBK_ID = B.BGBK_ID ");
		sql.append("WHERE AP.BGBK_ID = :bgbkId AND AP.APID = :apId");
		
		try{
			SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
			String cols = "BGBK_ID,APID,MBAPSTATUS,MBSYSSTATUS,MBAPSTATUSNAME,MBSYSSTATUSNAME";
			AutoAddScalar addscalar = new AutoAddScalar();
			addscalar.addScalar(query, BANK_STATUS.class, cols.split(","));
			query.setResultTransformer(Transformers.aliasToBean(BANK_STATUS.class));
			query.setParameter("bgbkId", bgbkId);
			query.setParameter("apId", apId);
			list = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return list;
	}
	
	public List<BANK_STATUS> getStatusPanel(){
		List<BANK_STATUS> list = null;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM ( ");
		sql.append("SELECT DISTINCT  A.BGBK_ID, A.MBSYSSTATUS, B.MBAPSTATUS, ");
		sql.append("(SELECT case SEND_STATUS when '1' then '0' when '2' then '0' else SEND_STATUS end FROM BANKCONNSTATUSTAB WHERE  A.BGBK_ID  LIKE  VARCHAR(BGBK_ID||'%')  AND CHANNEL_ID='01') AS S01, ");
		sql.append("(SELECT case RECV_STATUS when '1' then '0' when '2' then '0' else RECV_STATUS end FROM BANKCONNSTATUSTAB WHERE  A.BGBK_ID  LIKE  VARCHAR(BGBK_ID||'%')  AND CHANNEL_ID='01') AS R01, ");
		sql.append("(SELECT case SEND_STATUS when '1' then '0' when '2' then '0' else SEND_STATUS end FROM BANKCONNSTATUSTAB WHERE  A.BGBK_ID  LIKE  VARCHAR(BGBK_ID||'%')  AND CHANNEL_ID='02') AS S02, ");
		sql.append("(SELECT case RECV_STATUS when '1' then '0' when '2' then '0' else RECV_STATUS end FROM BANKCONNSTATUSTAB WHERE  A.BGBK_ID  LIKE  VARCHAR(BGBK_ID||'%')  AND CHANNEL_ID='02') AS R02, ");
		sql.append("(SELECT case SEND_STATUS when '1' then '0' when '2' then '0' else SEND_STATUS end FROM BANKCONNSTATUSTAB WHERE  A.BGBK_ID  LIKE  VARCHAR(BGBK_ID||'%')  AND CHANNEL_ID='03') AS S03, ");
		sql.append("(SELECT case RECV_STATUS when '1' then '0' when '2' then '0' else RECV_STATUS end FROM BANKCONNSTATUSTAB WHERE  A.BGBK_ID  LIKE  VARCHAR(BGBK_ID||'%')  AND CHANNEL_ID='03') AS R03, ");
		sql.append("(SELECT case SEND_STATUS when '1' then '0' when '2' then '0' else SEND_STATUS end FROM BANKCONNSTATUSTAB WHERE  A.BGBK_ID  LIKE  VARCHAR(BGBK_ID||'%')  AND CHANNEL_ID='04') AS S04, ");
		sql.append("(SELECT case RECV_STATUS when '1' then '0' when '2' then '0' else RECV_STATUS end FROM BANKCONNSTATUSTAB WHERE  A.BGBK_ID  LIKE  VARCHAR(BGBK_ID||'%')  AND CHANNEL_ID='04') AS R04, ");
		sql.append("(SELECT case SEND_STATUS when '1' then '0' when '2' then '0' else SEND_STATUS end FROM BANKCONNSTATUSTAB WHERE  A.BGBK_ID  LIKE  VARCHAR(BGBK_ID||'%')  AND CHANNEL_ID='05') AS S05, ");
		sql.append("(SELECT case RECV_STATUS when '1' then '0' when '2' then '0' else RECV_STATUS end FROM BANKCONNSTATUSTAB WHERE  A.BGBK_ID  LIKE  VARCHAR(BGBK_ID||'%')  AND CHANNEL_ID='05') AS R05, ");
		sql.append("(SELECT case SEND_STATUS when '1' then '0' when '2' then '0' else SEND_STATUS end FROM BANKCONNSTATUSTAB WHERE  A.BGBK_ID  LIKE  VARCHAR(BGBK_ID||'%')  AND CHANNEL_ID='06') AS S06, ");
		sql.append("(SELECT case RECV_STATUS when '1' then '0' when '2' then '0' else RECV_STATUS end FROM BANKCONNSTATUSTAB WHERE  A.BGBK_ID  LIKE  VARCHAR(BGBK_ID||'%')  AND CHANNEL_ID='06') AS R06, ");
		sql.append("(SELECT case SEND_STATUS when '1' then '0' when '2' then '0' else SEND_STATUS end FROM BANKCONNSTATUSTAB WHERE  A.BGBK_ID  LIKE  VARCHAR(BGBK_ID||'%')  AND CHANNEL_ID='07') AS S07, ");
		sql.append("(SELECT case RECV_STATUS when '1' then '0' when '2' then '0' else RECV_STATUS end FROM BANKCONNSTATUSTAB WHERE  A.BGBK_ID  LIKE  VARCHAR(BGBK_ID||'%')  AND CHANNEL_ID='07') AS R07, ");
		sql.append("(SELECT case SEND_STATUS when '1' then '0' when '2' then '0' else SEND_STATUS end FROM BANKCONNSTATUSTAB WHERE  A.BGBK_ID  LIKE  VARCHAR(BGBK_ID||'%')  AND CHANNEL_ID='08') AS S08, ");
		sql.append("(SELECT case RECV_STATUS when '1' then '0' when '2' then '0' else RECV_STATUS end FROM BANKCONNSTATUSTAB WHERE  A.BGBK_ID  LIKE  VARCHAR(BGBK_ID||'%')  AND CHANNEL_ID='08') AS R08, ");
		sql.append("(SELECT case SEND_STATUS when '1' then '0' when '2' then '0' else SEND_STATUS end FROM BANKCONNSTATUSTAB WHERE  A.BGBK_ID  LIKE  VARCHAR(BGBK_ID||'%')  AND CHANNEL_ID='09') AS S09, ");
		sql.append("(SELECT case RECV_STATUS when '1' then '0' when '2' then '0' else RECV_STATUS end FROM BANKCONNSTATUSTAB WHERE  A.BGBK_ID  LIKE  VARCHAR(BGBK_ID||'%')  AND CHANNEL_ID='09') AS R09, ");
		sql.append("(SELECT case SEND_STATUS when '1' then '0' when '2' then '0' else SEND_STATUS end FROM BANKCONNSTATUSTAB WHERE  A.BGBK_ID  LIKE  VARCHAR(BGBK_ID||'%')  AND CHANNEL_ID='10') AS S10, ");
		sql.append("(SELECT case RECV_STATUS when '1' then '0' when '2' then '0' else RECV_STATUS end FROM BANKCONNSTATUSTAB WHERE  A.BGBK_ID  LIKE  VARCHAR(BGBK_ID||'%')  AND CHANNEL_ID='10') AS R10 ");
		sql.append("FROM BANKSYSSTATUSTAB A, BANKAPSTATUSTAB B, BANKCONNSTATUSTAB D WHERE A.BGBK_ID = B.BGBK_ID AND A.BGBK_ID  LIKE  VARCHAR(D.BGBK_ID||'%') ");
//		sql.append("ORDER BY A.MBSYSSTATUS DESC, B.MBAPSTATUS DESC");
		//20160816 排序改為紅燈的排上面
		sql.append("ORDER BY S01, R01, S02, R02, S03, R03, S04, R04, S05, R05, S06, R06, S07, R07, S08, R08, S09, R09, S10, R10, A.MBSYSSTATUS DESC, B.MBAPSTATUS DESC");
		sql.append(" )AS F LEFT JOIN ");
//		20170209 edit by hugo PCODE='1310' 是換日通知 修改為PCODE='1210' 押碼基碼通知
//		sql.append("(select BANKID as APID from OPCTRANSACTIONLOGTAB where TXDATE=(SELECT THISDATE FROM EACHSYSSTATUSTAB) and PCODE='1310' and RSPCODE<>'0001')AS T ");
		sql.append("(select DISTINCT BANKID as APID from OPCTRANSACTIONLOGTAB where TXDATE=(SELECT THISDATE FROM EACHSYSSTATUSTAB) and PCODE='1210' and RSPCODE<>'0001')AS T ");
		sql.append("ON F.BGBK_ID = T.APID");
		System.out.println("SQL>>"+sql);
		try{
			SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
			String cols = "BGBK_ID,MBSYSSTATUS,MBAPSTATUS,S01,R01,S02,R02,S03,R03,S04,R04,S05,R05,S06,R06,S07,R07,S08,R08,S09,R09,S10,R10,APID";
			AutoAddScalar addscalar = new AutoAddScalar();
			addscalar.addScalar(query, BANK_STATUS.class, cols.split(","));
			query.setResultTransformer(Transformers.aliasToBean(BANK_STATUS.class));
			list = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return list;
	}
	
	public List<BANK_STATUS> getPanelDetail(String BGBK_ID, String CHANNEL_ID){
		List<BANK_STATUS> list = null;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT SEND_CONNECT_TIME, RECV_CONNECT_TIME, SEND_LASTTX_TIME, RECV_LASTTX_TIME, SEND_DISCONNECT_TIME, RECV_DISCONNECT_TIME ");
		sql.append("FROM BANKCONNSTATUSTAB ");
		sql.append("WHERE BGBK_ID='"+BGBK_ID+"' ");
		sql.append("AND CHANNEL_ID='"+CHANNEL_ID+"'");
		
		try{
		SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
		String cols = "SEND_CONNECT_TIME,RECV_CONNECT_TIME,SEND_LASTTX_TIME,RECV_LASTTX_TIME,SEND_DISCONNECT_TIME,RECV_DISCONNECT_TIME";
		AutoAddScalar addscalar = new AutoAddScalar();
		addscalar.addScalar(query, BANK_STATUS.class, cols.split(","));
		query.setResultTransformer(Transformers.aliasToBean(BANK_STATUS.class));
		list = query.list();
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return list;
	}
	
	public String getNextTxnDate(String date){
		String nextdate = null;
		StringBuffer sql = new StringBuffer();
		
		sql.append("SELECT TXN_DATE FROM WK_DATE_CALENDAR WHERE TXN_DATE > "+date);
		sql.append(" AND IS_TXN_DATE='Y' ORDER BY TXN_DATE ASC FETCH FIRST 1 ROW ONLY");
		try{
			SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
			query.addScalar("TXN_DATE", Hibernate.STRING);
			query.setResultTransformer(Transformers.aliasToBean(WK_DATE_CALENDAR.class));
			List<WK_DATE_CALENDAR> list = query.list();
			if(list != null && list.size() > 0){
				nextdate = list.get(0).getTXN_DATE();
			}
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return nextdate;
	}
	
	public List<WK_DATE_CALENDAR> getNextTxnDateList(String date){
		List<WK_DATE_CALENDAR> list = null;
		StringBuffer sql = new StringBuffer();
		
		sql.append("SELECT TXN_DATE FROM WK_DATE_CALENDAR WHERE TXN_DATE > "+date);
		sql.append(" AND IS_TXN_DATE='Y' ORDER BY TXN_DATE ASC");
		try{
			SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
			query.addScalar("TXN_DATE", Hibernate.STRING);
			query.setResultTransformer(Transformers.aliasToBean(WK_DATE_CALENDAR.class));
			list = query.list();
			
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return list;
	}
}
