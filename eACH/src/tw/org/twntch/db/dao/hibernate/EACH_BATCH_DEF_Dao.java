package tw.org.twntch.db.dao.hibernate;

import java.io.Serializable;
import java.util.List;

import tw.org.twntch.po.EACH_BATCH_DEF;

public class EACH_BATCH_DEF_Dao extends HibernateEntityDao<EACH_BATCH_DEF, Serializable>{
	
	
	public List<EACH_BATCH_DEF> getDataBySeqRange(String bizdate , String clearingphase ,  int seq1 , int seq2 ){
		List<EACH_BATCH_DEF> list = this.find("FROM  tw.org.twntch.po.EACH_BATCH_DEF  WHERE BATCH_PROC_SEQ >=? AND BATCH_PROC_SEQ <= ?", seq1,seq2);
		
		return list;
	}

}
