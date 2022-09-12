package tw.org.twntch.db.dao.hibernate;

import java.io.Serializable;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;

import tw.org.twntch.bean.FAIL_TRANS;
import tw.org.twntch.db.dao.hibernate.HibernateEntityDao;
import tw.org.twntch.util.AutoAddScalar;

public class FAIL_TRANS_Dao extends
		HibernateEntityDao<FAIL_TRANS, Serializable> {
	public List<FAIL_TRANS> getData() {
		List<FAIL_TRANS> list = null;
		StringBuffer sql = new StringBuffer();
		sql.append("WITH TEMP AS (SELECT a.txDate, a.Stan ,coalesce(a.senderAcquire, a.senderBankId) senderAcquire, ");
		sql.append("a.inAcquire, (case when a.senderAcquire=a.inAcquire then 'S' else 'O' end) TransType, ");
		sql.append(" (case when a.resultstatus='P' then (case when b.RESULTCODE='00' then 'A' when b.RESULTCODE='01' then 'R' else 'P' end) else  a.resultstatus end )  resultstatus, ");
		sql.append("a.CONRESULTCODE, ");
		sql.append("c.ERR_DESC TXN_ERR_DESC, ");
		sql.append("d.ERR_DESC GL_ERR_DESC ");
		sql.append("FROM ONBLOCKTAB  A  left join ONPENDINGTAB b on b.OTXDate=a.txDATE and b.OSTAN=a.STAN ");
		sql.append("left join TXN_ERROR_CODE c on c.ERROR_ID=a.CONRESULTCODE  left join GL_ERROR_CODE d on d.ERROR_ID=a.CONRESULTCODE ");
//		sql.append("WHERE  a.txDT >= '20140801000000' AND a.txDT <= '20150831235959') ");
		sql.append("SELECT a.senderAcquire || '-' || (select coalesce(bgbk_name,'') from bank_group where bgbk_id=a.senderAcquire) senderAcquire ");
		sql.append(",count(*)  TotalCount ");
		sql.append(",(SELECT COUNT(*) FROM TEMP B WHERE B.senderAcquire = A.senderAcquire and resultstatus='A' ) SuccessCount ");
		sql.append(",(SELECT COUNT(*) FROM TEMP B WHERE B.senderAcquire = A.senderAcquire and resultstatus !='A' and TransType='S' and  (GL_ERR_DESC is not null  and  TXN_ERR_DESC is null) ) SysErrSelf ");
		sql.append(",(SELECT COUNT(*) FROM TEMP B WHERE B.senderAcquire = A.senderAcquire and resultstatus !='A' and TransType='O' and  (GL_ERR_DESC is not null  and  TXN_ERR_DESC is null) ) SysErrOut ");
		sql.append(",(SELECT COUNT(*) FROM TEMP B WHERE B.senderAcquire = A.senderAcquire and resultstatus !='A' and TransType='S' and  (GL_ERR_DESC is  null  and  TXN_ERR_DESC is not null) ) TXNErrSelf ");
		sql.append(",(SELECT COUNT(*) FROM TEMP B WHERE B.senderAcquire = A.senderAcquire and resultstatus !='A' and TransType='O' and  (GL_ERR_DESC is  null  and  TXN_ERR_DESC is not null) ) TXNErrOut ");
		sql.append(",(SELECT COUNT(*) FROM TEMP B WHERE B.senderAcquire = A.senderAcquire and resultstatus !='A' and TransType='S' and  (GL_ERR_DESC is  null  and  TXN_ERR_DESC is null) ) OthErrSelf ");
		sql.append(",(SELECT COUNT(*) FROM TEMP B WHERE B.senderAcquire = A.senderAcquire and resultstatus !='A' and TransType='O' and  (GL_ERR_DESC is  null  and  TXN_ERR_DESC is null) ) OthErrOut ");
		sql.append(",(SELECT COUNT(*) FROM TEMP B WHERE B.senderAcquire = A.senderAcquire and resultstatus !='A' ) FailCount ");
		sql.append("FROM TEMP a ");
		sql.append("group by a.senderAcquire");
//		sql.append("");
		

		try {
			SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
			String cols = "TXDATE,STAN,SENDERACQUIRE,SENDERBANKID,INACQUIRE,TRANSTYPE,RESULTSTATUS,RESULTCODE,CONRESULTCODE,ERR_DESC,TXN_ERR_DESC,GL_ERR_DESC,OTXDATE,OSTAN,ERROR_ID,TXDT,TOTALCOUNT,SYSERRSELF,SYSERROUT,TXNERRSELF,TXNERROUT,OTHERRSELF,OTHERROUT,FAILCOUNT";
			AutoAddScalar addscalar = new AutoAddScalar();
			addscalar.addScalar(query, FAIL_TRANS.class, cols.split(","));
			query.setResultTransformer(Transformers.aliasToBean(FAIL_TRANS.class));
			list = query.list();
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public List<FAIL_TRANS> getData(String txdt1,String txdt2) {
		List<FAIL_TRANS> list = null;
		StringBuffer sql = new StringBuffer();
		sql.append("WITH TEMP AS (SELECT a.txDate, a.Stan ,coalesce(a.senderAcquire, a.senderBankId) senderAcquire, ");
		sql.append("a.inAcquire, (case when a.senderAcquire=a.inAcquire then 'S' else 'O' end) TransType, ");
		sql.append(" (case when a.resultstatus='P' then (case when b.RESULTCODE='00' then 'A' when b.RESULTCODE='01' then 'R' else 'P' end) else  a.resultstatus end )  resultstatus, ");
		sql.append("a.CONRESULTCODE, ");
		sql.append("c.ERR_DESC TXN_ERR_DESC, ");
		sql.append("d.ERR_DESC GL_ERR_DESC ");
		sql.append("FROM ONBLOCKTAB  A  left join ONPENDINGTAB b on b.OTXDate=a.txDATE and b.OSTAN=a.STAN ");
		sql.append("left join TXN_ERROR_CODE c on c.ERROR_ID=a.CONRESULTCODE  left join GL_ERROR_CODE d on d.ERROR_ID=a.CONRESULTCODE ");
//		sql.append("WHERE  a.txDT >= '20140801000000' AND a.txDT <= '20150831235959') ");
		sql.append("WHERE  a.txDT >= :txdt1 AND a.txDT <= :txdt2) ");
		sql.append("SELECT a.senderAcquire || '-' || (select coalesce(bgbk_name,'') from bank_group where bgbk_id=a.senderAcquire) senderAcquire ");
		sql.append(",count(*)  TotalCount ");
		sql.append(",(SELECT COUNT(*) FROM TEMP B WHERE B.senderAcquire = A.senderAcquire and resultstatus='A' ) SuccessCount ");
		sql.append(",(SELECT COUNT(*) FROM TEMP B WHERE B.senderAcquire = A.senderAcquire and resultstatus !='A' and TransType='S' and  (GL_ERR_DESC is not null  and  TXN_ERR_DESC is null) ) SysErrSelf ");
		sql.append(",(SELECT COUNT(*) FROM TEMP B WHERE B.senderAcquire = A.senderAcquire and resultstatus !='A' and TransType='O' and  (GL_ERR_DESC is not null  and  TXN_ERR_DESC is null) ) SysErrOut ");
		sql.append(",(SELECT COUNT(*) FROM TEMP B WHERE B.senderAcquire = A.senderAcquire and resultstatus !='A' and TransType='S' and  (GL_ERR_DESC is  null  and  TXN_ERR_DESC is not null) ) TXNErrSelf ");
		sql.append(",(SELECT COUNT(*) FROM TEMP B WHERE B.senderAcquire = A.senderAcquire and resultstatus !='A' and TransType='O' and  (GL_ERR_DESC is  null  and  TXN_ERR_DESC is not null) ) TXNErrOut ");
		sql.append(",(SELECT COUNT(*) FROM TEMP B WHERE B.senderAcquire = A.senderAcquire and resultstatus !='A' and TransType='S' and  (GL_ERR_DESC is  null  and  TXN_ERR_DESC is null) ) OthErrSelf ");
		sql.append(",(SELECT COUNT(*) FROM TEMP B WHERE B.senderAcquire = A.senderAcquire and resultstatus !='A' and TransType='O' and  (GL_ERR_DESC is  null  and  TXN_ERR_DESC is null) ) OthErrOut ");
		sql.append(",(SELECT COUNT(*) FROM TEMP B WHERE B.senderAcquire = A.senderAcquire and resultstatus !='A' ) FailCount ");
		sql.append("FROM TEMP a ");
		sql.append("group by a.senderAcquire");
//		sql.append("");
		

		try {
			SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
			String cols = "TXDATE,STAN,SENDERACQUIRE,SENDERBANKID,INACQUIRE,TRANSTYPE,RESULTSTATUS,RESULTCODE,CONRESULTCODE,ERR_DESC,TXN_ERR_DESC,GL_ERR_DESC,OTXDATE,OSTAN,ERROR_ID,TXDT,TOTALCOUNT,SYSERRSELF,SYSERROUT,TXNERRSELF,TXNERROUT,OTHERRSELF,OTHERROUT,FAILCOUNT";
			AutoAddScalar addscalar = new AutoAddScalar();
			addscalar.addScalar(query, FAIL_TRANS.class, cols.split(","));
			query.setResultTransformer(Transformers.aliasToBean(FAIL_TRANS.class));
			query.setParameter("txdt1", txdt1);
			query.setParameter("txdt2", txdt2);
			list = query.list();
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return list;
	}
}

