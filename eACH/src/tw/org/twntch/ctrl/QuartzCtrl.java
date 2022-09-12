package tw.org.twntch.ctrl;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import tw.org.twntch.bo.SYS_PARA_BO;
import tw.org.twntch.po.SYS_PARA;
import tw.org.twntch.quartz.MonitorAmount;
import tw.org.twntch.quartz.MonitorPending;
import tw.org.twntch.util.QuartzUtil;
import tw.org.twntch.util.SpringAppCtxHelper;

public class QuartzCtrl  extends HttpServlet {
	
	private Logger logger = Logger.getLogger(getClass());
	
	@Override
	public void init() throws ServletException {
		logger.info("######################## QuartzCtrl #########################");
		ApplicationContext context = SpringAppCtxHelper.getApplicationContext();
		SYS_PARA_BO sys_param_bo = context.getBean(SYS_PARA_BO.class);
//		SYS_PARA_BO sys_param_bo = (SYS_PARA_BO)applicationContext.getBean(SYS_PARA_BO.class);
		SYS_PARA setting = sys_param_bo.searchII();
		String monitor_amount_time_period = setting.getMONITOR_AMOUNT_PERIOD();
		String monitor_pending_time_period = setting.getMONITOR_PENDING_PERIOD();
		String job_name1 = "大額監控排程";
		String job_name2 = "逾時監控排程";
		
//		String min = new SimpleDateFormat("mm").format(new Date());
//		//0 */10 * ? * *
//		String time1 = "0 "+min+"/"+ monitor_amount_time_period+" * ? * * ";
//		String time2 = "0 "+min+"/"+ monitor_pending_time_period+" * ? * * ";
		
		logger.info("############## addJob ##################");
		QuartzUtil.addJob(job_name1, MonitorAmount.class, monitor_amount_time_period );
		QuartzUtil.addJob(job_name2, MonitorPending.class, monitor_pending_time_period );
		
		super.init();
		
	}

	@Override
	public void destroy() {
		System.out.println("############## shutdownJobs ##################");
		QuartzUtil.shutdownJobs();
		super.destroy();
	}

	
}
