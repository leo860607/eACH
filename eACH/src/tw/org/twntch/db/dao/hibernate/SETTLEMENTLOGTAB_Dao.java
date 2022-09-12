package tw.org.twntch.db.dao.hibernate;

import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;

import tw.org.twntch.po.SETTLEMENTLOGTAB;
import tw.org.twntch.po.SETTLEMENTLOGTAB_PK;

public class SETTLEMENTLOGTAB_Dao extends HibernateGenericDao<SETTLEMENTLOGTAB, SETTLEMENTLOGTAB_PK>{

	public List<Map> getData(String bizdate, String clearingphase){
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT  VARCHAR(PCODE) PCODE ,CLEARINGPHASE,BIZDATE, RECEIVERBANK ||'-'||(SELECT BGBK_NAME FROM BANK_GROUP WHERE BGBK_ID = RECEIVERBANK ) AS RECEIVERBANK ");
		sql.append(" ,TIMESTAMP_FORMAT(TXDT,'YYYY-MM-DD HH24:MI:SS') AS TX_TEST, TRANSLATE('abcd-ef-gh ij:kl:mn', TXDT, 'abcdefghijklmn') AS TXTIME  ");
		sql.append(" FROM SETTLEMENTLOGTAB WHERE PCODE IN( '5200','5201') ");
		sql.append(" AND   BIZDATE = :bizdate AND CLEARINGPHASE = :clearingphase  ");
		sql.append(" ORDER BY   PCODE  ,RECEIVERBANK  ASC ,TXTIME DESC   ");
		SQLQuery  sqlquery = getCurrentSession().createSQLQuery(sql.toString());
		sqlquery.setParameter("bizdate", bizdate);
		sqlquery.setParameter("clearingphase", clearingphase);
		sqlquery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List<Map> list = sqlquery.list();
		return list;
		
	}


}
