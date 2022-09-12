package tw.org.twntch.db.dao.hibernate;

import java.io.Serializable;
import java.util.List;

import tw.org.twntch.po.EACH_BATCH_NOTIFY;
import tw.org.twntch.po.EACH_BATCH_NOTIFY_PK;

public class EACH_BATCH_NOTIFY_Dao extends HibernateEntityDao<EACH_BATCH_NOTIFY, EACH_BATCH_NOTIFY_PK>{

	
	public boolean checkRPT_BizDate(String pathSQL){
		boolean res = true;
		List<EACH_BATCH_NOTIFY> list = this.find(" FROM tw.org.twntch.po.EACH_BATCH_NOTIFY WHERE "+pathSQL);
		if(list != null && list.size() !=0 ){
			for(EACH_BATCH_NOTIFY po :list){
				if( po.getRPT_NOTIFY() == null  || ! po.getRPT_NOTIFY().equals("Y") ){
					res = false ;
					return res;
				}
			}
		}else{
			res =false;
		}
		
		return res;
	}
	public boolean checkRPTCL_BizDate(String pathSQL){
		boolean res = true;
		List<EACH_BATCH_NOTIFY> list = this.find(" FROM tw.org.twntch.po.EACH_BATCH_NOTIFY WHERE "+pathSQL);
		if(list != null && list.size() !=0 ){
			for(EACH_BATCH_NOTIFY po :list){
				if(! po.getCLEAR_NOTIFY().equals("Y")){
					res = false ;
					return res;
				}
			}
		}else{
			res =false;
		}
		
		return res;
	}
	
}
