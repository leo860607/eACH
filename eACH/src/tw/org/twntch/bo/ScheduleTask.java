package tw.org.twntch.bo;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;
import org.apache.log4j.Logger;

import tw.org.twntch.util.SpringAppCtxHelper;


/**
 * 土炮自製 cron service</br>
 * 本物件主要功能為定時服務排程之執行及檢查</br>
 * 所有排程均在此加入及設定</br>
 * 當 app server 啟動後，需要等待 spring 將所有 bean 先準備好才可以開始執行</br>
 * 使用 SpringAppCtxHelper.getApplicationContext() == null 來判斷 spring 是否已準備好</br>
 * @author andy
 *
 */
public class ScheduleTask implements Runnable
{
	private static boolean isRunning = false;
	private static boolean is_RUN_SP_RPONBLOCKTAB = false;
	private static boolean is_RUN_SP_RPONCLEARINGTAB = false;
	private static boolean is_RUN_SP_RPONCLEARFEETAB = false;
	RUN_SP_RPONBLOCKTAB run_rp;
	RUN_SP_RPONCLEARINGTAB run_rp_oncl;
	RUN_SP_RPONCLEARFEETAB run_rp_onfee;
	private Logger logger = Logger.getLogger(getClass());
	public ScheduleTask()
	{
		if (isRunning == false)
		{
			Thread thread = new Thread(this);
			thread.start();
			isRunning = true;
		}		
	}
	
	@Override
	public void run() 
	{
		try
		{
			while(true)
			{
				logger.debug("ScheduleTask running...");
				//當 app server 啟動後，需要等待 spring 將所有 bean 先準備好才可以開始執行
				logger.debug("SpringAppCtxHelper.getApplicationContext()>>"+SpringAppCtxHelper.getApplicationContext());
				if (SpringAppCtxHelper.getApplicationContext() == null)
				{
					Thread.sleep(10 * 1000); //每  n 秒 check 一次
					continue;
				}
				
				if (is_RUN_SP_RPONBLOCKTAB == false && Arguments.getStringArg("RUN_SP_RPONBLOCKTAB").equalsIgnoreCase("Y"))
				{
					System.out.println("run_rp>>"+run_rp);
//					這邊有點奇怪 在pl可以不用判斷是否null 這邊沒判斷啟動時會是null 造成thread 沒有運作 因為is_RUN_SP_RPONBLOCKTAB已改為true
					if(run_rp!=null){
						Thread thread = new Thread(run_rp);
						thread.start();
						is_RUN_SP_RPONBLOCKTAB = true;
						logger.debug("RUN_SP_RPONBLOCKTAB IsRunning");
					}
				}
				
				
				if (is_RUN_SP_RPONCLEARINGTAB == false && Arguments.getStringArg("RUN_SP_RPONCLEARINGTAB").equalsIgnoreCase("Y"))
				{
					System.out.println("run_rp_oncl>>"+run_rp_oncl);
					if(run_rp!=null){
						Thread thread = new Thread(run_rp_oncl);
						thread.start();
						is_RUN_SP_RPONCLEARINGTAB = true;
						logger.debug("is_RUN_SP_RPONCLEARINGTAB IsRunning");
					}
				}
				if (is_RUN_SP_RPONCLEARFEETAB == false && Arguments.getStringArg("RUN_SP_RPONCLEARFEETAB").equalsIgnoreCase("Y"))
				{
					System.out.println("run_rp_onfee>>"+run_rp_onfee);
					if(run_rp!=null){
						Thread thread = new Thread(run_rp_onfee);
						thread.start();
						is_RUN_SP_RPONCLEARINGTAB = true;
						logger.debug("is_RUN_SP_RPONCLEARFEETAB IsRunning");
					}
				}
				
				//logger.debug("ScheduleTask tick!");
				//logger.debug(tw.gov.nsc.util.FileUtils.getStringFromFile("/getTodoListResponse.xml"));
//				Thread.sleep(600 * 1000); //每  n 秒 check 一次
				Thread.sleep(1200 * 1000); //每  n 秒 check 一次
			}
		}
		catch(Exception e)
		{
			e.printStackTrace(System.out);
			logger.error("ScheduleTask.Exception>>"+e.toString());
			
		}
	}
	
	public RUN_SP_RPONBLOCKTAB getRun_rp() {
		return run_rp;
	}

	public void setRun_rp(RUN_SP_RPONBLOCKTAB run_rp) {
		this.run_rp = run_rp;
	}

	public RUN_SP_RPONCLEARINGTAB getRun_rp_oncl() {
		return run_rp_oncl;
	}

	public void setRun_rp_oncl(RUN_SP_RPONCLEARINGTAB run_rp_oncl) {
		this.run_rp_oncl = run_rp_oncl;
	}

	public RUN_SP_RPONCLEARFEETAB getRun_rp_onfee() {
		return run_rp_onfee;
	}

	public void setRun_rp_onfee(RUN_SP_RPONCLEARFEETAB run_rp_onfee) {
		this.run_rp_onfee = run_rp_onfee;
	}

	
	
	
	
}
