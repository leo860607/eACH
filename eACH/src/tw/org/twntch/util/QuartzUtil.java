package tw.org.twntch.util;

import java.util.Calendar;
import java.util.Date;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.quartz.impl.StdSchedulerFactory;

public class QuartzUtil {
	
	 private static SchedulerFactory gSchedulerFactory = new StdSchedulerFactory();  
	    private static String JOB_GROUP_NAME = "EXTJWEB_JOBGROUP_NAME";  
	    private static String TRIGGER_GROUP_NAME = "EXTJWEB_TRIGGERGROUP_NAME";  
	  
	    /** 
	     * 新增一個定時任務，使用預設的任務組名，觸發器名，觸發器組名 
	     * @param jobName 任務名 
	     * @param cls 任務 
	     * @param time 時間設定  單位:分
	     * @param delayTime 重設之後多久跑起來   單位 :秒
	     */  
	    @SuppressWarnings("rawtypes")  
	    public static void addJob(String jobName, Class cls, String time) {  
	        try {  
	            Scheduler sched = gSchedulerFactory.getScheduler();  
	            // 任務名，任務組，任務執行類  
	            JobDetail jobDetail = new JobDetail(jobName, JOB_GROUP_NAME, cls); 
	            Calendar cal = Calendar.getInstance();
	            cal.add(Calendar.SECOND,10);
	            //改用SimpleTrigger
	            SimpleTrigger trigger = new SimpleTrigger();
	        	trigger.setName(jobName);
	        	trigger.setGroup(TRIGGER_GROUP_NAME);
	        	trigger.setStartTime(cal.getTime());
	        	trigger.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
	        	trigger.setRepeatInterval(Integer.parseInt(time)*60*1000);
	        	sched.scheduleJob(jobDetail, trigger);
	            //可以傳遞引數  
//	            jobDetail.getJobDataMap().put("param", "?" );  
	            // 觸發器  
//	            CronTrigger trigger = new CronTrigger(jobName, TRIGGER_GROUP_NAME);  
//	            // 觸發器名,觸發器組  
//	            trigger.setCronExpression(time);  
	            // 觸發器時間設定  
//	            sched.scheduleJob(jobDetail, trigger);  
	            // 啟動  
	            if (!sched.isShutdown()) {  
	                sched.start();  
	            }  
	        } catch (Exception e) {  
	            throw new RuntimeException(e);  
	        }  
	    }  
	    /** 
	     * 修改一個任務的觸發時間(使用預設的任務組名，觸發器名，觸發器組名) 
	     * @param jobName 
	     * @param time 
	     */  
	    @SuppressWarnings("rawtypes")  
	    public static void modifyJobTime(String jobName, String time) {  
	        try {  
	            Scheduler sched = gSchedulerFactory.getScheduler();  
	            SimpleTrigger trigger = (SimpleTrigger) sched.getTrigger(jobName,TRIGGER_GROUP_NAME);  
	            if (trigger == null) {  
	                return;  
	            }  
//	            String oldTime = trigger.getCronExpression();  
//	            if (!oldTime.equalsIgnoreCase(time)) {  
	            JobDetail jobDetail = sched.getJobDetail(jobName,JOB_GROUP_NAME);  
	            Class objJobClass = jobDetail.getJobClass();  
	            removeJob(jobName);  
	            addJob(jobName, objJobClass, time);  
//	            }  
	        } catch (Exception e) {  
	            throw new RuntimeException(e);  
	        }  
	    }  
	  
	  
	    /** 
	     * 移除一個任務(使用預設的任務組名，觸發器名，觸發器組名) 
	     * @param jobName 
	     */  
	    public static void removeJob(String jobName) {  
	        try {  
	            Scheduler sched = gSchedulerFactory.getScheduler();  
	            sched.pauseTrigger(jobName, TRIGGER_GROUP_NAME);// 停止觸發器  
	            sched.unscheduleJob(jobName, TRIGGER_GROUP_NAME);// 移除觸發器  
	            sched.deleteJob(jobName, JOB_GROUP_NAME);// 刪除任務  
	        } catch (Exception e) {  
	            throw new RuntimeException(e);  
	        }  
	    }  
	  
	    /** 
	     * 啟動所有定時任務 
	     */  
	    public static void startJobs() {  
	        try {  
	            Scheduler sched = gSchedulerFactory.getScheduler();  
	            sched.start();  
	        } catch (Exception e) {  
	            throw new RuntimeException(e);  
	        }  
	    }  
	  
	    /** 
	     * 關閉所有定時任務 
	     */  
	    public static void shutdownJobs() {  
	        try {  
	            Scheduler sched = gSchedulerFactory.getScheduler();  
	            if (!sched.isShutdown()) {  
	                sched.shutdown();  
	            }  
	        } catch (Exception e) {  
	            throw new RuntimeException(e);  
	        }  
	    }  
}
