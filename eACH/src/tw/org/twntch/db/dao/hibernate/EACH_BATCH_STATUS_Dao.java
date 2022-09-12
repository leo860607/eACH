package tw.org.twntch.db.dao.hibernate;

import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;

import tw.org.twntch.po.EACH_BATCH_STATUS;
import tw.org.twntch.po.EACH_BATCH_STATUS_PK;

public class EACH_BATCH_STATUS_Dao extends HibernateEntityDao<EACH_BATCH_STATUS, EACH_BATCH_STATUS_PK>{

	public List <EACH_BATCH_STATUS> checkStatus(String bizdate , String clearingphase){
//		20150424 edit by hugo 改抓每日排程的成筆數來跟def檔比對
//		List<EACH_BATCH_STATUS> list = this.find(" FROM tw.org.twntch.po.EACH_BATCH_STATUS WHERE PROC_TYPE = 'D' AND PROC_STATUS !='S' AND BIZDATE = ? AND CLEARINGPHASE = ? " , bizdate ,clearingphase );
		List<EACH_BATCH_STATUS> list = this.find(" FROM tw.org.twntch.po.EACH_BATCH_STATUS WHERE PROC_TYPE = 'D' AND PROC_STATUS ='S' AND BIZDATE = ? AND CLEARINGPHASE = ? " , bizdate ,clearingphase );
		return list;
	}
	public List <EACH_BATCH_STATUS> checkStatus_By_Seq(String bizdate , String clearingphase , int batch_proc_seq){
		List<EACH_BATCH_STATUS> list = this.find(" FROM tw.org.twntch.po.EACH_BATCH_STATUS WHERE PROC_TYPE = 'D' AND PROC_STATUS ='S' AND BIZDATE = ? AND CLEARINGPHASE = ? AND BATCH_PROC_SEQ = ? " , bizdate ,clearingphase , batch_proc_seq );
		return list;
	}
	/**
	 * 前3項是普鴻的批次
	 * @param bizdate
	 * @param clearingphase
	 * @param batch_proc_seq
	 * @return
	 */
	public List <EACH_BATCH_STATUS> checkStatus_BeforeThree(String bizdate , String clearingphase ){
//		List<EACH_BATCH_STATUS> list = this.find(" FROM tw.org.twntch.po.EACH_BATCH_STATUS WHERE PROC_TYPE = 'D' AND (PROC_STATUS ='P' OR PROC_STATUS IS NULL ) AND BIZDATE = ? AND CLEARINGPHASE = ? AND BATCH_PROC_SEQ IN (0,1,2)" , bizdate ,clearingphase );
		List<EACH_BATCH_STATUS> list = this.find(" FROM tw.org.twntch.po.EACH_BATCH_STATUS WHERE PROC_TYPE = 'D' AND PROC_STATUS ='P'  AND BIZDATE = ? AND CLEARINGPHASE = ? AND BATCH_PROC_SEQ IN (0,1,2)" , bizdate ,clearingphase );
		return list;
	}
	/**
	 * 前20項是普鴻的批次
	 * @param bizdate
	 * @param clearingphase
	 * @param batch_proc_seq
	 * @return
	 */
	public List <EACH_BATCH_STATUS> checkStatus_BeforeTwentey(String bizdate , String clearingphase ){
//		List<EACH_BATCH_STATUS> list = this.find(" FROM tw.org.twntch.po.EACH_BATCH_STATUS WHERE PROC_TYPE = 'D' AND (PROC_STATUS ='P' OR PROC_STATUS IS NULL ) AND BIZDATE = ? AND CLEARINGPHASE = ? AND BATCH_PROC_SEQ IN (0,1,2)" , bizdate ,clearingphase );
		List<EACH_BATCH_STATUS> list = this.find(" FROM tw.org.twntch.po.EACH_BATCH_STATUS WHERE PROC_TYPE = 'D' AND PROC_STATUS ='P'  AND BIZDATE = ? AND CLEARINGPHASE = ? AND BATCH_PROC_SEQ <= 20" , bizdate ,clearingphase );
		return list;
	}
	/**
	 * 查詢編號21~70的中斷點，排除S,N
	 * @param bizdate
	 * @param clearingphase
	 * @return
	 */
	public List <Map> getBrekPonitList(String bizdate , String clearingphase){
		StringBuffer  sql =  new StringBuffer(); 
		sql.append(" SELECT varchar( BIZDATE ) BIZDATE ,varchar( CLEARINGPHASE ) CLEARINGPHASE , varchar(BATCH_PROC_SEQ) BATCH_PROC_SEQ , BATCH_PROC_NAME ,varchar(PROC_STATUS) PROC_STATUS ");
		sql.append(" ,BATCH_PROC_DESC,NOTE , VARCHAR_FORMAT(BEGIN_TIME,'YYYY-MM-DD HH:MM:SS.FF3') AS BEGIN_TIME  ");
		sql.append(" ,VARCHAR_FORMAT(END_TIME,'YYYY-MM-DD HH:MM:SS.FF3') AS END_TIME  ");
		sql.append(" FROM EACH_BATCH_STATUS WHERE BATCH_PROC_SEQ >= ");
		sql.append(" (SELECT MIN(BATCH_PROC_SEQ)  FROM EACH_BATCH_STATUS  ");
		sql.append(" WHERE BATCH_PROC_SEQ >=21 AND PROC_STATUS NOT IN('S','N') ");
		sql.append(" AND BIZDATE = :bizdate AND CLEARINGPHASE = :clearingphase  AND PROC_TYPE = 'D' ) ");
		sql.append(" AND BIZDATE = :bizdate AND CLEARINGPHASE = :clearingphase  ");
		sql.append(" ORDER BY BATCH_PROC_SEQ  ");
		
		SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
		query.setParameter("bizdate", bizdate);
		query.setParameter("clearingphase", clearingphase);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List<Map> list = query.list();
		return list;
	}
	/**
	 * 查詢編號0~20的中斷點，排除S,N
	 * @param bizdate
	 * @param clearingphase
	 * @return
	 */
	public List <Map> getBrekPonitListII(String bizdate , String clearingphase){
		StringBuffer  sql =  new StringBuffer(); 
		sql.append(" SELECT varchar( BIZDATE ) BIZDATE ,varchar( CLEARINGPHASE ) CLEARINGPHASE , varchar(BATCH_PROC_SEQ) BATCH_PROC_SEQ , BATCH_PROC_NAME ,varchar(PROC_STATUS) PROC_STATUS ");
		sql.append(" ,BATCH_PROC_DESC,NOTE , VARCHAR_FORMAT(BEGIN_TIME,'YYYY-MM-DD HH:MM:SS.FF3') AS BEGIN_TIME  ");
		sql.append(" ,VARCHAR_FORMAT(END_TIME,'YYYY-MM-DD HH:MM:SS.FF3') AS END_TIME  ");
		sql.append(" FROM EACH_BATCH_STATUS WHERE BATCH_PROC_SEQ >= ");
		sql.append(" (SELECT MIN(BATCH_PROC_SEQ)  FROM EACH_BATCH_STATUS  ");
		sql.append(" WHERE BATCH_PROC_SEQ <=20 AND PROC_STATUS NOT IN('S','N') ");
		sql.append(" AND BIZDATE = :bizdate AND CLEARINGPHASE = :clearingphase  AND PROC_TYPE = 'D' ) ");
		sql.append(" AND BIZDATE = :bizdate AND CLEARINGPHASE = :clearingphase  ");
		sql.append(" ORDER BY BATCH_PROC_SEQ  ");
		
		SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
		query.setParameter("bizdate", bizdate);
		query.setParameter("clearingphase", clearingphase);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List<Map> list = query.list();
		return list;
	}
	
	
	public List <Map> getOnblockData(String bizdate , String clearingphase){
		StringBuffer  sql =  new StringBuffer(); 
		sql.append(" select distinct SENDERID,  TXID, senderbankid ,senderhead  ");
		sql.append(" from ONBLOCKTAB  ");
		sql.append(" where BIZDATE= :bizdate and CLEARINGPHASE= :clearingphase  and RESULTSTATUS <> 'R' ");
		sql.append(" and ( txid, senderid, senderhead ) not in ( select TXN_ID,  COMPANY_ID,  SND_BGBK_ID from TXN_COMPANY_FEE_PROFILE ) ");
		SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
		query.setParameter("bizdate", bizdate);
		query.setParameter("clearingphase", clearingphase);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List<Map> list = query.list();
		return list;
	}
	
}
