package tw.org.twntch.bo;

import java.sql.SQLException;

import tw.org.twntch.db.dataaccess.DataAccessException;
import tw.org.twntch.db.dataaccess.ExecuteSQL;
import tw.org.twntch.util.zDateHandler;

public class RUN_SP_RPONBLOCKTAB implements Runnable{
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true){
			System.out.println("zDateHandler.getTheTime()>>"+zDateHandler.getTheTime()); 
			try {
				if(zDateHandler.isInTimeRange(zDateHandler.getTheTime(), Arguments.getStringArg("RPONBLOCKTAB_CL1_A"), Arguments.getStringArg("RPONBLOCKTAB_CL1_B"))){
					System.out.println("RUN_SP_RPONBLOCKTAB>>時間區間內開始..");
					ExecuteSQL es = new ExecuteSQL("jdbc/ACH");
					String sql = "{call EACHUSER.BAT_RPONBLOCKTAB()}";
					es.doSP(sql);
				}
//				Thread.sleep(60*1000);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				try {
					Thread.sleep(1200*1000);//間隔20分
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

}
