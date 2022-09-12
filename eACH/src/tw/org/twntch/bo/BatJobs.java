package tw.org.twntch.bo;


import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.Query;
import javax.management.ReflectionException;

import org.apache.catalina.Server;
import org.apache.commons.beanutils.DynaBean;
import org.apache.log4j.Logger;
import org.h2.constant.SysProperties;

import com.ibm.db2.jcc.am.zd;
import com.ibm.db2.jcc.am.zp;

import tw.org.twntch.db.dao.hibernate.SYS_PARA_Dao;
import tw.org.twntch.db.dao.hibernate.WK_DATE_Dao;
import tw.org.twntch.po.EACH_BATCH_DEF;
import tw.org.twntch.po.EACH_BATCH_STATUS;
import tw.org.twntch.po.EACH_BATCH_STATUS_PK;
import tw.org.twntch.po.SYS_PARA;
import tw.org.twntch.util.SpringAppCtxHelper;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.WebServerPath;
import tw.org.twntch.util.WebServletUtils;
import tw.org.twntch.util.zDateHandler;


/**
 * 土炮自製 cron service</br>
 * 本物件主要功能為定時服務排程之執行及檢查</br>
 * 所有排程均在此加入及設定</br>
 * 當 app server 啟動後，需要等待 spring 將所有 bean 先準備好才可以開始執行</br>
 * 使用 SpringAppCtxHelper.getApplicationContext() == null 來判斷 spring 是否已準備好</br>
 * @author andy
 *
 */
public class BatJobs implements Runnable
{
	private static boolean isRunning = false;
	private static boolean is_RUN_CL1 = true;
	private static boolean is_RUN_CL2 = true;
	public static boolean is_FS_CL1 = false; //第一階段是否完全執行完畢
	public static boolean is_FS_CL2 = false;
	public static int CL1_FAIL_CNT = 0;
	public static int CL2_FAIL_CNT = 0;
	private static String SERVER_IP = "";
	private static String THIS_AP = "";
	private static String RP_CLEARPHASE1_TIME = "12:40:00";
	private static String RP_CLEARPHASE2_TIME = "15:40:00";
	private static List<String> IPLIST = new LinkedList<String>();
	private  String tmpsysdate = "";
	EACH_BATCH_BO each_batch_bo ;
	SYS_PARA_Dao sys_para_Dao ;
	WK_DATE_BO wk_date_bo ;
	WebServerPath webserverpath ;
	private Logger logger = Logger.getLogger(getClass());
	public BatJobs()
	{
		if (isRunning == false)
		{
			Thread thread = new Thread(this);
			thread.start();
			isRunning = true;
			tmpsysdate = zDateHandler.getTheDateII();
			try {
				IPLIST = getServerIP();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
	}
	
	@Override
	public void run() 
	{
		try
		{
			while(true)
			{
				logger.debug("BatJobs running...");
//				System.out.println("BatJobs.path>>"+BatJobs.class.getClassLoader().getResource(""));
//				logger.debug("BatJobs.path>>"+BatJobs.class.getClassLoader().getResource(""));
				//當 app server 啟動後，需要等待 spring 將所有 bean 先準備好才可以開始執行
				logger.debug("SpringAppCtxHelper.getApplicationContext()>>"+SpringAppCtxHelper.getApplicationContext());
				if (SpringAppCtxHelper.getApplicationContext() == null)
				{
					Thread.sleep(10 * 1000); //每  n 秒 check 一次
					continue;
				}
				webserverpath = (WebServerPath) (webserverpath==null? SpringAppCtxHelper.getBean("webserverpath"):webserverpath);
//				logger.debug("BatJobs.path4>>"+webserverpath.getServerRootUrl());
//				System.out.println("BatJobs.path4>>"+webserverpath.getServerRootUrl());
				System.out.println("THIS SERVER_IP>>"+SERVER_IP);
				if(!checkAP()){
					logger.debug("不執行排程>>...wait..5min");
//					System.out.println("不執行排程>>...wait..5min");
//					Thread.sleep(180*1000);
					Thread.sleep(300*1000);
					continue;
				}
				if(!isBizdate()){
//					System.out.println("not Bizdate 不執行排程>>...wait..5min");
					logger.debug("not Bizdate 不執行排程>>...wait..5min");
//					Thread.sleep(180*1000);
					Thread.sleep(300*1000);
					continue;
				}
//				TODO 如可以執行 還要判斷上一階段的執行是否已結束 並變更相關變數避免重複執行或跳關執行
//				先取得目前應該執行的營業日及清算階段，拿來查詢notify最後一筆資料 rpt_notify是否為Y
				
				
//				 表示換日 某些參數要初始化
				if(! tmpsysdate.equals(zDateHandler.getTheDateII()) ){
					logger.debug(" 日期已變化" );
					tmpsysdate = zDateHandler.getTheDateII();
					is_RUN_CL1 = true ;
					is_RUN_CL2 = true ;
					CL1_FAIL_CNT = 0;
					CL2_FAIL_CNT = 0;

				}
//				TODO 這日期還是有點問題  如執行時間未超過12:00:00 會抓不到資料
//				String exedate = zDateHandler.getDateNum();
				Map<String,String> map = each_batch_bo.getExeBatBizDateAndCl();
				String exedate = map.get("pre_bizdate");
				String exe_clearingphase = map.get("pre_clearingphase");
				System.out.println("exedate>>"+exedate);
				System.out.println("exe_clearingphase>>"+exe_clearingphase);
				System.out.println("is_RUN_CL1>>"+is_RUN_CL1);
				System.out.println("is_RUN_CL2>>"+is_RUN_CL2);
//				檢查現在要跑的排程是否已執行過
//				checkNotify(exedate, exe_clearingphase);
				checkBatIsRun(exedate, exe_clearingphase);
				System.out.println("2.is_RUN_CL1>>"+is_RUN_CL1);
				System.out.println("2.is_RUN_CL2>>"+is_RUN_CL2);
//				判斷是否為排程的時間 及是否為營業日
				if (is_RUN_CL1 == false &&  zDateHandler.compareDiffTime(zDateHandler.getTheTime(), RP_CLEARPHASE1_TIME) >0   )
				{
					System.out.println("each_batch_bo>>"+each_batch_bo);
//					
					try {
						if(each_batch_bo!=null){
//							先檢查上一階段的結算通知是否已發佈  20150421 edit by hugo req by 李建利  先暫時不判斷
//							20150520 edit by hugo 不檢核的話2個行程太接近會出問題
							if(!each_batch_bo.checkPreNotify(exedate, "01")){
								logger.debug("bizdate="+exedate+",階段=01,的上一階段rpt_notify != Y bat is not run");
								System.out.println("bizdate="+exedate+",階段=01,的上一階段rpt_notify != Y bat is not run");
								Thread.sleep(180*1000);
								continue;
							}
//							先判斷 onblock onpending 是否已清算
							if( !check(exedate , "01")){
								logger.debug("01 階段  onblock or onpending 尚未完成");
								Thread.sleep(180*1000);
//								Thread.sleep(10*1000);
								continue;
							}
							if(CL1_FAIL_CNT < 3){
								if(doBat(exedate, "01")){
									is_FS_CL1 = true ;// TODO 第一階段已完全執行完 畢，之後要改判斷上一個階段的通知是否已發佈 
									is_RUN_CL1 = true ;
								}else{
									CL1_FAIL_CNT++;
									is_FS_CL1 = false ;
									is_RUN_CL1 = false ;
									System.out.println("CL1_FAIL_CNT>>"+CL1_FAIL_CNT);
									Thread.sleep(10*1000);
								}
							}else{
								is_RUN_CL1 = true ;
							}
							
							logger.debug("排程執行完畢 is_RUN_CL1>>"+is_RUN_CL1);
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (is_RUN_CL2 == false &&  zDateHandler.compareDiffTime(zDateHandler.getTheTime(), RP_CLEARPHASE2_TIME) >0   )
				{
					System.out.println("each_batch_bo>>"+each_batch_bo);
					try {
						if(each_batch_bo!=null){
//							先檢查上一階段的結算通知是否已發佈  20150421 edit by hugo req by 李建利  先暫時不判斷
							if(!each_batch_bo.checkPreNotify(exedate, "02")){
								logger.debug("bizdate="+exedate+",階段=02,的上一階段rpt_notify != Y bat is not run");
								System.out.println("bizdate="+exedate+",階段=02,的上一階段rpt_notify != Y bat is not run");
								Thread.sleep(180*1000);
								continue;
							}
//							先判斷 onblock onpending 是否已清算
							if( !check(exedate , "02")){
								logger.debug(" 第二階段  onblock or onpending 尚未完成");
								Thread.sleep(180*1000);
//								Thread.sleep(10*1000);
								continue;
							}
							if(CL2_FAIL_CNT<3){
								
								if(doBat(exedate, "02")){
									is_FS_CL2 = true;
									is_RUN_CL2 = true ;
								}else{
									CL2_FAIL_CNT++;
									is_FS_CL2 = false ;
									is_RUN_CL2 = false ;
									System.out.println("CL2_FAIL_CNT>>"+CL2_FAIL_CNT);
									Thread.sleep(10*1000);
								}
							}else{
								is_RUN_CL2 = true ;
							}
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
//				Thread.sleep(1800*1000); //20分
//				Thread.sleep(180*1000); //2分
				Thread.sleep(30*1000); //2分
			}	//while end
		}
		catch(Exception e)
		{
			e.printStackTrace(System.out);
			logger.error("BatJobs.Exception>>"+e.toString());
			
		}
	}
	public void getExeTime(){
		
	}
	
	/**
	 * 判斷是否為營業日
	 * @return
	 */
	public boolean isBizdate(){
		boolean ret = false ;
		wk_date_bo = (WK_DATE_BO) (wk_date_bo == null ? SpringAppCtxHelper.getBean("wk_date_bo"):wk_date_bo);
		ret = wk_date_bo.isTxnDate();
		return ret;
	}
	
	/**
	 * 
	 * 取得資料主機IP
	 * @return
	 */
	public boolean checkAP(){
		boolean ret = false ;
		System.out.println("IPLIST>>"+IPLIST);
//		TODO AP1 要改抓DB資料 抓變數無法即時換台;
		sys_para_Dao = (SYS_PARA_Dao) (sys_para_Dao == null ? SpringAppCtxHelper.getBean("sys_para_Dao"):sys_para_Dao);
		SYS_PARA  po = null ;
		try {
			List <SYS_PARA> list =  sys_para_Dao.getTopOne();
			po = list != null && list.size() != 0 ? list.get(0): new SYS_PARA();
			if(StrUtils.isEmpty(po.getAP1()) 
				|| StrUtils.isEmpty(po.getAP2())
				|| StrUtils.isEmpty(po.getAP1_ISRUN())
				|| StrUtils.isEmpty(po.getAP2_ISRUN())
				|| StrUtils.isEmpty(po.getRP_CLEARPHASE1_TIME())
				|| StrUtils.isEmpty(po.getRP_CLEARPHASE2_TIME())
			  ){
				logger.debug(" Short of param 缺少參數 ");
				return ret =false;
			}
			RP_CLEARPHASE1_TIME = po.getRP_CLEARPHASE1_TIME();
			RP_CLEARPHASE2_TIME = po.getRP_CLEARPHASE2_TIME();
			if( IPLIST.contains(po.getAP1())){
				THIS_AP = "AP1";
				SERVER_IP = po.getAP1();
				SYS.AP1_ISRUN = po.getAP1_ISRUN();
			}
			if(IPLIST.contains(po.getAP2())){
				THIS_AP = "AP2";
				SERVER_IP = po.getAP2();
				SYS.AP2_ISRUN = po.getAP2_ISRUN();
			}
			System.out.println("THIS_AP>>"+THIS_AP);
			if(THIS_AP.equals("AP1") ){
				System.out.println("AP1.AP1_ISRUN00>>"+SYS.AP1_ISRUN);
				if(! SYS.AP1_ISRUN.equals("Y")){
					System.out.println("AP1.AP1_ISRUN>>"+SYS.AP1_ISRUN);
					System.out.println("此AP(AP1)未符合設定，不執行排程");
					System.out.println("此AP資訊，ip>>"+SERVER_IP+",是否執行>>"+SYS.AP1_ISRUN);
					return ret =false;
				}else{
					return ret =true;
				}
			}
			if(THIS_AP.equals("AP2") ){
				System.out.println("AP1.AP2_ISRUN00>>"+SYS.AP2_ISRUN);
				if(!SYS.AP2_ISRUN.equals("Y")){
					System.out.println("此AP(AP2)未符合設定，不執行排程");
					System.out.println("此AP資訊，ip>>"+SERVER_IP+",是否執行>>"+SYS.AP2_ISRUN);
					return ret =false;
				}else{
					return ret =true;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("checkAP.Exception>>"+e);
			ret =false;
		}
		return ret;
	}
	/**
	 * 取得本機的IP清單
	 * @return
	 * @throws AttributeNotFoundException
	 * @throws InstanceNotFoundException
	 * @throws MBeanException
	 * @throws ReflectionException
	 * @throws UnknownHostException
	 * @throws MalformedObjectNameException
	 */
	public static List<String> getServerIP() throws AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException, UnknownHostException, MalformedObjectNameException{
	    String hostname = InetAddress.getLocalHost().getHostName();
	    System.out.println("hostname>>"+hostname);
	    InetAddress[] addresses = InetAddress.getAllByName(hostname);
	    System.out.println("addresses>>"+addresses);
	    ArrayList<String> ipList = new ArrayList<String>();
	    for(InetAddress addr : addresses) {
            String host = addr.getHostAddress();
            ipList.add(host);
	    }
	    System.out.println("ipList>>"+ipList);
		return ipList;
	}
	/**
	 * 此版本在AIX不能RUN
	 * @return
	 * @throws AttributeNotFoundException
	 * @throws InstanceNotFoundException
	 * @throws MBeanException
	 * @throws ReflectionException
	 * @throws UnknownHostException
	 * @throws MalformedObjectNameException
	 */
	public static List<String> getServerIPII() throws AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException, UnknownHostException, MalformedObjectNameException{
		MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
		Set<ObjectName> objs = mbs.queryNames(new ObjectName("*:type=Connector,*"),
				Query.match(Query.attr("protocol"), Query.value("HTTP/1.1")));
		String hostname = InetAddress.getLocalHost().getHostName();
		System.out.println("hostname>>"+hostname);
		InetAddress[] addresses = InetAddress.getAllByName(hostname);
		System.out.println("addresses>>"+addresses);
		ArrayList<String> endPoints = new ArrayList<String>();
		
//	    for (Iterator<ObjectName> i = objs.iterator(); i.hasNext();) {
//	        ObjectName obj = i.next();
//	        String scheme = mbs.getAttribute(obj, "scheme").toString();
//	        String port = obj.getKeyProperty("port");
		for (InetAddress addr : addresses) {
			String host = addr.getHostAddress();
//	            String ep = scheme + "://" + host + ":" + port;
//	            endPoints.add(ep);
			endPoints.add(host);
		}
//	    }
		System.out.println("endPoints>>"+endPoints);
		return endPoints;
	}
	
	public boolean check(String bizdate  , String clearingphase){
		return each_batch_bo.isRunBat(bizdate, clearingphase);
	}
	
	public boolean doBat(String bizdate , String clearingphase){
		boolean res = false ;
		boolean res1 = false ;
		boolean res2 = false ;
		res1 = each_batch_bo.initEach_Batch_Status(bizdate, clearingphase);
		res2 = each_batch_bo.initEach_Batch_Notify(bizdate, clearingphase);
		res = res1 && res2 ? true :res;
		res = each_batch_bo.doAllBatch(bizdate , clearingphase) ? true :false;
		logger.debug("doBat IsRunning  , bizdate>>"+bizdate+" , clearingphase>>"+clearingphase+" res>>"+res);
		System.out.println("doBat IsRunning  , bizdate>>"+bizdate+" , clearingphase>>"+clearingphase+" res>>"+res);
		return res;
	}
	
//	public void checkNotify(String exedate , String exe_clearingphase){
//		//true 表示現階段還沒跑 要改為is_RUN_CL1 要改為false;
//		if(each_batch_bo.checkNotify(exedate, exe_clearingphase)){
//			if(exe_clearingphase.equals("01")){
//				System.out.println("exedate="+exedate+"exe_clearingphase="+exe_clearingphase+".pre_clphase.rptnotify=N");
//				is_RUN_CL1=false;
//			}
//			if(exe_clearingphase.equals("02")){
//				System.out.println("exedate="+exedate+"exe_clearingphase="+exe_clearingphase+".pre_clphase.rptnotify=N");
//				is_RUN_CL2=false;
//			}
//		}else{
//			if(exe_clearingphase.equals("01")){
//				is_RUN_CL1=true;
//			}
//			if(exe_clearingphase.equals("02")){
//				is_RUN_CL2=true;
//			}
//		}
//	}
	public void checkBatIsRun(String exedate , String exe_clearingphase){
		//false 表示現階段還沒跑 ，is_RUN_CL1 要改為false;
		if(!each_batch_bo.checkBatIsRun(exedate, exe_clearingphase)){
			if(exe_clearingphase.equals("01")){
				System.out.println("exedate="+exedate+"exe_clearingphase="+exe_clearingphase+".pre_clphase.rptnotify=N");
				is_RUN_CL1=false;
			}
			if(exe_clearingphase.equals("02")){
				System.out.println("exedate="+exedate+"exe_clearingphase="+exe_clearingphase+".pre_clphase.rptnotify=N");
				is_RUN_CL2=false;
			}
		}else{
			if(exe_clearingphase.equals("01")){
				is_RUN_CL1=true;
			}
			if(exe_clearingphase.equals("02")){
				is_RUN_CL2=true;
			}
		}
	}
	
	
//	/**
//	 * 排程剛起時，要將相關排程狀態先寫入以利後續作業
//	 * @param bizdate
//	 * @param clearingphase
//	 * @return
//	 */
//	public boolean initEach_Batch_Status(String bizdate , String clearingphase){
//		boolean res = false ;
//		List<EACH_BATCH_DEF> list = each_batch_bo.getBatDef();
//		EACH_BATCH_STATUS_PK id= null;
//		EACH_BATCH_STATUS statusPo = null ; 
//		try {
//			if(list !=null && list.size() !=0){
//				for(int i = 2 ; i < list.size() ; i++ ){
//					statusPo = new EACH_BATCH_STATUS();
//					EACH_BATCH_DEF po = list.get(i);
//					id = new EACH_BATCH_STATUS_PK(bizdate, clearingphase, po.getBATCH_PROC_SEQ());
//					statusPo.setId(id);
//					statusPo.setBATCH_PROC_NAME(po.getBATCH_PROC_NAME());
//					statusPo.setBATCH_PROC_DESC(po.getBATCH_PROC_DESC());
//					statusPo.setPROC_TYPE(po.getPROC_TYPE());
//					statusPo.setPROC_STATUS("");//避免後續作業 NULL壞掉
//					each_batch_bo.saveStatus(statusPo);
//				}
//				res=true;
//			}
//			
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return res;
//	}
	

	public EACH_BATCH_BO getEach_batch_bo() {
		return each_batch_bo;
	}

	public void setEach_batch_bo(EACH_BATCH_BO each_batch_bo) {
		this.each_batch_bo = each_batch_bo;
	}

	public SYS_PARA_Dao getSys_para_Dao() {
		return sys_para_Dao;
	}

	public void setSys_para_Dao(SYS_PARA_Dao sys_para_Dao) {
		this.sys_para_Dao = sys_para_Dao;
	}

	public WK_DATE_BO getWk_date_bo() {
		return wk_date_bo;
	}

	public void setWk_date_bo(WK_DATE_BO wk_date_bo) {
		this.wk_date_bo = wk_date_bo;
	}

	public WebServerPath getWebserverpath() {
		return webserverpath;
	}

	public void setWebserverpath(WebServerPath webserverpath) {
		this.webserverpath = webserverpath;
	}

	
	
	
}
