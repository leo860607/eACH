package tw.org.twntch.bo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.google.gson.Gson;

import tw.org.twntch.db.dao.hibernate.BANK_GROUP_Dao;
import tw.org.twntch.db.dao.hibernate.EACH_BATCH_DEF_Dao;
import tw.org.twntch.db.dao.hibernate.EACH_BATCH_NOTIFY_Dao;
import tw.org.twntch.db.dao.hibernate.EACH_BATCH_STATUS_Dao;
import tw.org.twntch.db.dao.hibernate.RPONBLOCKTAB_Dao;
import tw.org.twntch.po.AGENT_PROFILE;
import tw.org.twntch.po.BANK_GROUP;
import tw.org.twntch.po.EACH_BATCH_DEF;
import tw.org.twntch.po.EACH_BATCH_STATUS;
import tw.org.twntch.po.EACH_BATCH_STATUS_PK;
import tw.org.twntch.util.CodeUtils;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.OSValidator;
import tw.org.twntch.util.RptUtils;
import tw.org.twntch.util.WebServletUtils;
import tw.org.twntch.util.zDateHandler;

/**
 * 批次產生代理業者相關報表及資料下載檔
 * @author Hugo
 *
 */
public class BAT_AGENT_DATA_BO  implements Runnable {
	private Logger logger = Logger.getLogger(BAT_AGENT_DATA_BO.class.getName());
	private RPTTX_5_BO rpttx_5_bo ;
	private RPTST_16_BO rptst_16_bo;
	private RPTST_17_BO rptst_17_bo;
	private RPTFEE_8_BO rptfee_8_bo;
	private RPTFEE_9_BO rptfee_9_bo;
	private AGENTDL_TX001_BO agentdl_tx001_bo;
	private AGENTDL_ST001_BO agentdl_st001_bo;
	private AGENTDL_FEE001_BO agentdl_fee001_bo;
	private AGENTDL_FEE002_BO agentdl_fee002_bo;
	private BASEDATA_DOWNLOAD_BO basedata_download_bo;
	private AGENT_PROFILE_BO agent_profile_bo ;
	private RPONBLOCKTAB_Dao rponblocktab_Dao ;
	private RptUtils rptutils ;
	private CodeUtils codeUtils;
	private String bizDate;
	private String clearingphase;
	private Integer batch_proc_seq;
	private boolean isRunning = false;
	private EACH_BATCH_NOTIFY_Dao batch_notify_Dao  ;
	private EACH_BATCH_STATUS_Dao batch_status_Dao  ;
	private EACH_BATCH_DEF_Dao batch_def_Dao  ;
	@Override
	public void run() {
		boolean result = false;
		try {
			isRunning = true;
			initData();
			Map<String,String> map = do_Bat_Rpt(bizDate, clearingphase, batch_proc_seq);
			result = map.get("result").equals("TRUE")?true:false;
			finishData(result, map.get("msg"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.debug("BAT_RPT_TH_BO.run.Exception>>"+e);
		}finally{
			isRunning = false;
		}
	}
	
	public boolean initData(){
		boolean reault = false;
		EACH_BATCH_STATUS po = null;
		EACH_BATCH_DEF defpo = null;
		EACH_BATCH_STATUS_PK id = new EACH_BATCH_STATUS_PK(bizDate, clearingphase, Integer.valueOf(batch_proc_seq));
		try {
			po = batch_status_Dao.get(id);
			defpo =  batch_def_Dao.get(Integer.valueOf(batch_proc_seq));
			if(po==null){
				po = new EACH_BATCH_STATUS();
				po.setId(id);
			}
			po.setBATCH_PROC_DESC(defpo.getBATCH_PROC_DESC());
			po.setBATCH_PROC_NAME(defpo.getBATCH_PROC_NAME());
			po.setBEGIN_TIME(zDateHandler.getTheDateII()+" "+zDateHandler.getTheTime_MS());
			po.setNOTE("");
			po.setPROC_STATUS("P");
			batch_status_Dao.aop_save(po);
			reault = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.debug("initData.Exception>>"+e);
		}
		return reault;
	}
	public boolean finishData(boolean arg , String msg){
		boolean reault = false;
		EACH_BATCH_STATUS po = null;
		EACH_BATCH_DEF defpo = null;
		EACH_BATCH_STATUS_PK id = new EACH_BATCH_STATUS_PK(bizDate, clearingphase, Integer.valueOf(batch_proc_seq));
		try {
			po = batch_status_Dao.get(id);
			defpo =  batch_def_Dao.get(Integer.valueOf(batch_proc_seq));
			if(po==null){
				po = new EACH_BATCH_STATUS();
				po.setId(id);
				po.setBATCH_PROC_DESC(defpo.getBATCH_PROC_DESC());
				po.setBATCH_PROC_NAME(defpo.getBATCH_PROC_NAME());
			}
			po = batch_status_Dao.get(id);
			po.setEND_TIME(zDateHandler.getTheDateII()+" "+zDateHandler.getTheTime_MS());
			po.setNOTE(msg);
			if(arg){
				po.setPROC_STATUS("S");
			}else{
				po.setPROC_STATUS("F");
			}
			batch_status_Dao.aop_save(po);
			reault = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.debug("finishData.Exception>>"+e);
		}
		return reault;
	}
	
	public EACH_BATCH_STATUS tempSave(EACH_BATCH_STATUS_PK id , EACH_BATCH_STATUS po ,String msg){
		if(po != null){
			po.setNOTE(msg.toString());
			batch_status_Dao.aop_save(po);
		}
		return po = batch_status_Dao.get(id);
	}
	public EACH_BATCH_STATUS tempSave(EACH_BATCH_STATUS_PK id , EACH_BATCH_STATUS po ,Map<String,String> msgMap){
		StringBuffer str = new StringBuffer();
		for(String key: msgMap.keySet()){
			str.append(msgMap.get(key));
			str.append(";");
		}
		System.out.println("str>>"+str);
		if(po != null){
			po.setNOTE(str.toString());
			batch_status_Dao.aop_save(po);
		}
		return po = batch_status_Dao.get(id);
	}
	
	public Map do_Bat_Rpt(String bizDate , String clearingphase ,int batch_proc_seq){
//		執行相關批次報表，不管錯誤還是失敗都要回傳 ，msg="明細1成功，明細2失敗" 有一個失敗就算失敗result="FALSE"
//		List<BANK_GROUP> optBank_List = bank_group_Dao.getBgbkIdList_2();
		List<AGENT_PROFILE> company_Id_List = agent_profile_bo.getAgent_profile_Dao().getCompany_Id_List();
		company_Id_List = company_Id_List == null ? new LinkedList():company_Id_List;
		String prveYearMounth="";
		int errorcount = 0;
		StringBuffer msg = new StringBuffer();
		Map<String,String> params = new HashMap<String,String>();
		Map<String,String> retMap = new HashMap<String,String>();
		params.put("BIZDATE", bizDate);
		params.put("CLEARINGPHASE", clearingphase);
		prveYearMounth = zDateHandler.getEACH_PrveMonth(bizDate, "yyyyMMdd");
		
//		測試用
//		prveYearMounth = "201603";
		
		params.put("TW_YEAR", prveYearMounth.substring(0,4));
		params.put("TW_MONTH", prveYearMounth.substring(4,6));
		EACH_BATCH_STATUS_PK id = new EACH_BATCH_STATUS_PK(bizDate, clearingphase, batch_proc_seq);
		id = new EACH_BATCH_STATUS_PK(bizDate, clearingphase, batch_proc_seq);
		String suc = "產生成功" , strart = " start " , end = " end ";
		String fail = "產生失敗", run = "執行中";
		try {
			EACH_BATCH_STATUS po = batch_status_Dao.get(id);
			Map<String,String> map= codeUtils.getPropertyValue("Configuration.properties", "agent_bat_str");
			String json = map.get("agent_bat_str");
			TreeMap<String,String> strmap = new Gson().fromJson(json, TreeMap.class);
			TreeMap<String,String> accmap = null;
//			準備產生代理業者交易明細報表
			accmap = codeUtils.process(strmap, "str1", "#1", run);
			accmap = codeUtils.process(strmap, "str1", "#2", strart+zDateHandler.getTheTime_MS());
			accmap = codeUtils.process_all(accmap);
			po = tempSave(id, po, accmap);
			if( !doRpt_TX5(params, company_Id_List)){
				errorcount++;
				accmap = codeUtils.process(strmap, "str1", "@"+run, fail);
			}else{
				accmap = codeUtils.process(strmap, "str1", "@"+run, suc);
			}
			accmap = codeUtils.process(strmap, "str1", "#3", end+zDateHandler.getTheTime_MS());
			accmap = codeUtils.process_all(accmap);
			po = tempSave(id, po, accmap);
			if(clearingphase.equals("02")){
	//			準備產生代理業者交易日統計報表
				accmap = codeUtils.process(strmap, "str2", "#1", run);
				accmap = codeUtils.process(strmap, "str2", "#2", strart+zDateHandler.getTheTime_MS());
				accmap = codeUtils.process_all(accmap);
				po = tempSave(id, po, accmap);
			
				if( !doRpt_ST16(params, company_Id_List)){
					errorcount++;
					accmap = codeUtils.process(strmap, "str2", "@"+run, fail);
				}else{
					accmap = codeUtils.process(strmap, "str2", "@"+run, suc);
				}
				accmap = codeUtils.process(strmap, "str2", "#3", end+zDateHandler.getTheTime_MS());
				accmap = codeUtils.process_all(accmap);
				po = tempSave(id, po, accmap);
	//			準備產生代理業者手續費日報表
				accmap = codeUtils.process(strmap, "str3", "#1", run);
				accmap = codeUtils.process(strmap, "str3", "#2", strart+zDateHandler.getTheTime_MS());
				accmap = codeUtils.process_all(accmap);
				po = tempSave(id, po, accmap);
				if( !doRpt_FEE8(params, company_Id_List)){
					errorcount++;
					accmap = codeUtils.process(strmap, "str3", "@"+run, fail);
				}else{
					accmap = codeUtils.process(strmap, "str3", "@"+run, suc);
				}
				accmap = codeUtils.process(strmap, "str3", "#3", end+zDateHandler.getTheTime_MS());
				accmap = codeUtils.process_all(accmap);
				po = tempSave(id, po, accmap);
			}
//			準備產生代理業者交易明細資料檔
			accmap = codeUtils.process(strmap, "str4", "#1", run);
			accmap = codeUtils.process(strmap, "str4", "#2", strart+zDateHandler.getTheTime_MS());
			accmap = codeUtils.process_all(accmap);
			po = tempSave(id, po, accmap);
			if( !doAGENTDL_TX001(params, company_Id_List)){
				errorcount++;
				accmap = codeUtils.process(strmap, "str4", "@"+run, fail);
			}else{
				accmap = codeUtils.process(strmap, "str4", "@"+run, suc);
			}
			accmap = codeUtils.process(strmap, "str4", "#3", end+zDateHandler.getTheTime_MS());
			accmap = codeUtils.process_all(accmap);
			po = tempSave(id, po, accmap);
			
			if(clearingphase.equals("02")){
//			準備產生代理業者交易日統計資料檔
				accmap = codeUtils.process(strmap, "str5", "#1", run);
				accmap = codeUtils.process(strmap, "str5", "#2", strart+zDateHandler.getTheTime_MS());
				accmap = codeUtils.process_all(accmap);
				po = tempSave(id, po, accmap);
				if( !doAGENTDL_ST001(params, company_Id_List)){
					errorcount++;
					accmap = codeUtils.process(strmap, "str5", "@"+run, fail);
				}else{
					accmap = codeUtils.process(strmap, "str5", "@"+run, suc);
				}
				accmap = codeUtils.process(strmap, "str5", "#3", end+zDateHandler.getTheTime_MS());
				accmap = codeUtils.process_all(accmap);
				po = tempSave(id, po, accmap);
	//			準備產生代理業者手續費日統計資料檔
				accmap = codeUtils.process(strmap, "str6", "#1", run);
				accmap = codeUtils.process(strmap, "str6", "#2", strart+zDateHandler.getTheTime_MS());
				accmap = codeUtils.process_all(accmap);
				po = tempSave(id, po, accmap);
				if( !doAGENTDL_FEE001(params, company_Id_List)){
					errorcount++;
					accmap = codeUtils.process(strmap, "str6", "@"+run, fail);
				}else{
					accmap = codeUtils.process(strmap, "str6", "@"+run, suc);
				}
				accmap = codeUtils.process(strmap, "str5", "#3", end+zDateHandler.getTheTime_MS());
				accmap = codeUtils.process_all(accmap);
				po = tempSave(id, po, accmap);
				
	//			準備產生代理業者交易月統計報表
				accmap = codeUtils.process(strmap, "str7", "#1", run);
				accmap = codeUtils.process(strmap, "str7", "#2", strart+zDateHandler.getTheTime_MS());
				accmap = codeUtils.process_all(accmap);
				po = tempSave(id, po, accmap);
				if( !doRpt_ST17(params, company_Id_List)){
					errorcount++;
					accmap = codeUtils.process(strmap, "str7", "@"+run, fail);
				}else{
					accmap = codeUtils.process(strmap, "str7", "@"+run, suc);
				}
				accmap = codeUtils.process(strmap, "str7", "#3", end+zDateHandler.getTheTime_MS());
				accmap = codeUtils.process_all(accmap);
				po = tempSave(id, po, accmap);
	//			準備產生代理業者手續費月統計報表
				accmap = codeUtils.process(strmap, "str8", "#1", run);
				accmap = codeUtils.process(strmap, "str8", "#2", strart+zDateHandler.getTheTime_MS());
				accmap = codeUtils.process_all(accmap);
				po = tempSave(id, po, accmap);
				if( !doRpt_FEE9(params, company_Id_List)){
					errorcount++;
					accmap = codeUtils.process(strmap, "str8", "@"+run, fail);
				}else{
					accmap = codeUtils.process(strmap, "str8", "@"+run, suc);
				}
				
				accmap = codeUtils.process(strmap, "str8", "#3", end+zDateHandler.getTheTime_MS());
				accmap = codeUtils.process_all(accmap);
				po = tempSave(id, po, accmap);
			}
//			準備產生代理業者共用資料檔
//			accmap = codeUtils.process(strmap, "str9", "#1", run);
//			accmap = codeUtils.process(strmap, "str9", "#2", strart+zDateHandler.getTheTime_MS());
//			accmap = codeUtils.process_all(accmap);
//			po = tempSave(id, po, accmap);
//			Map<String,Object> datamap = doAGENTDL_COMM_DATA(params, company_Id_List);
//			//List有TXT的Byte[]才做
//			if(!(((List<byte[]>)datamap.get("dataList")).size() > 0)){
//				errorcount++;
//				accmap = codeUtils.process(strmap, "str9", "@"+run, fail);
//			}else{
//				accmap = codeUtils.process(strmap, "str9", "@"+run, suc);
//			}
//			accmap = codeUtils.process(strmap, "str9", "#3", end+zDateHandler.getTheTime_MS());
//			accmap = codeUtils.process_all(accmap);
//			po = tempSave(id, po, accmap);
			
//			準備產生壓縮檔(日)			
			accmap = codeUtils.process(strmap, "str10", "#1", run);
			accmap = codeUtils.process(strmap, "str10", "#2", strart+zDateHandler.getTheTime_MS());
			accmap = codeUtils.process_all(accmap);
			po = tempSave(id, po, accmap);
			if( !this.doAgent_DL_ZIP(params, company_Id_List )){
//				if( !this.doAgent_DL_ZIP(params, company_Id_List ,(List<byte[]>)datamap.get("dataList"),(List<String>)datamap.get("filenameList"))){
				errorcount++;
				accmap = codeUtils.process(strmap, "str10", "@"+run, fail);
			}else{
				accmap = codeUtils.process(strmap, "str10", "@"+run, suc);
			}
			accmap = codeUtils.process(strmap, "str10", "#3", zDateHandler.getTheTime_MS());
			accmap = codeUtils.process_all(accmap);
			po = tempSave(id, po, accmap);
//			msg.append(po.getNOTE());
//			if(errorcount>0){
//				retMap.put("result", "FALSE");
//				retMap.put("msg", msg.toString());
//			}else{
//				retMap.put("result", "TRUE");
//				retMap.put("msg", msg.toString());
//			}
			
			
		
//			準備產生壓縮檔(月)	
			if(clearingphase.equals("02")){
				accmap = codeUtils.process(strmap, "str11", "#1", run);
				accmap = codeUtils.process(strmap, "str11", "#2", strart+zDateHandler.getTheTime_MS());
				accmap = codeUtils.process_all(accmap);
				po = tempSave(id, po, accmap);
				if( !this.doAgent_DL_ZIP_Mon(params, company_Id_List)){
					errorcount++;
					accmap = codeUtils.process(strmap, "str11", "@"+run, fail);
				}else{
					accmap = codeUtils.process(strmap, "str11", "@"+run, suc);
				}
				accmap = codeUtils.process(strmap, "str11", "#3", zDateHandler.getTheTime_MS());
				accmap = codeUtils.process_all(accmap);
				po = tempSave(id, po, accmap);
			}
			msg.append(po.getNOTE());
			if(errorcount>0){
				retMap.put("result", "FALSE");
				retMap.put("msg", msg.toString());
			}else{
				retMap.put("result", "TRUE");
				retMap.put("msg", msg.toString());
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		
		return retMap;
	}
	/**
	 * 代理業者交易明細報表
	 * @param params
	 * @param opt_id
	 * @param opt_name
	 * @param opt_type_List
	 * @return
	 */
	public boolean doRpt_TX5(Map<String,String> params ,List<AGENT_PROFILE> company_Id_List){
		String sql= "";
//		Agent_txlistRP_{代理業者統編}_{YYYYMMDD}_{清算階段}.pdf Ex：Agent_txlistRP_16606102_20150201_01.pdf
		boolean result = false;
		String outputFileName= "";
		String outputPath= "";
		Map<String, Object> rpt_param = new HashMap<String, Object>();
		Map<String, String> sql_param = new HashMap<String, String>();
		rpt_param.put("V_PRINT_DATE",DateTimeUtils.convertDate(DateTimeUtils.INTERCONVERSION, zDateHandler.getTheDate(), "yyyy/MM/dd", "yyy/MM/dd"));
		rpt_param.put("V_PRINT_TIME",zDateHandler.getTheTime());
		rpt_param.put("V_TXDT",DateTimeUtils.convertDate(DateTimeUtils.convertDate(params.get("BIZDATE"),"yyyyMMdd","yyyyMMdd"),"yyyyMMdd","營業日期: yyy/MM/dd"));
//		rpt_param.put("V_TXDT",DateTimeUtils.convertDate(DateTimeUtils.convertDate(params.get("BIZDATE"),"yyyyMMdd","yyyyMMdd"),"yyyyMMdd","yyy/MM/dd"));
		rpt_param.put("V_SND_COMPANY_ID","全部");
		rpt_param.put("V_TXID","全部");
		rpt_param.put("V_TG_RESULT","全部");
		rpt_param.put("V_CLEARINGPHASE",params.get("CLEARINGPHASE"));
		
		Map<String,String> retmap =null;
		String conditionKey  ="" , agent_company_id = "";
		try {
			conditionKey = "[\"BIZDATE\",\"AGENT_COMPANY_ID\",\"SND_COMPANY_ID\",\"CLEARINGPHASE\"]";
			for( AGENT_PROFILE po :company_Id_List){
				agent_company_id = po.getCOMPANY_ID();
				outputFileName= Arguments.getStringArg("BAT.FILE.NAME.TX5");
				outputFileName = outputFileName.replace("#1", agent_company_id);
				outputFileName = outputFileName.replace("#2", params.get("BIZDATE"));
				outputFileName = outputFileName.replace("#3", params.get("CLEARINGPHASE"));
				outputPath = params.get("BIZDATE")+"/"+agent_company_id+"/"+Arguments.getStringArg("BAT.LAST.PATH")+"/";
				rpt_param.put("V_AGENT_COMPANY_ID",agent_company_id);
				sql_param.putAll(params);
				sql_param.put("BIZDATE", DateTimeUtils.convertDate(params.get("BIZDATE"), "yyyyMMdd", "yyyyMMdd") );
				Map map = rpttx_5_bo.getConditionData( sql_param, conditionKey);
				sql = rpttx_5_bo.getSQL(map.get("sqlPath").toString(), "", "");
				List list = rponblocktab_Dao.getRptData(sql, (Map<String, String>) map.get("values"));
				retmap  = rptutils.bat_export(RptUtils.COLLECTION, "tx_5", outputFileName,outputPath, rpt_param, null,list, 1);
				if(retmap.get("result").equals("TRUE")){
					result =  true;
				}else{
					result =  false;
					logger.debug("doRpt_TX5.bat_export fail...");
					logger.debug("doRpt_TX5.agent_company_id>>"+agent_company_id);
					break;
				}
			}
			return result;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.debug("doRpt_TX5.Exception>>");
			logger.debug("doRpt_TX5.agent_company_id>>"+agent_company_id);
			result =  false;
		}
		
		return result;
	}
	
	public boolean doRpt_ST16(Map<String,String> params ,List<AGENT_PROFILE> company_Id_List){
		String sql= "";
//		Agent_TxDaySumRP_{代理業者統編}_{YYYYMMDD}_{清算階段}.pdf Ex：Agent_TxDaySumRP_16606102_20150201_01.pdf
		boolean result = false;
		String outputFileName= "";
		String outputPath= "";
		Map<String, Object> rpt_param = new HashMap<String, Object>();
		Map<String, String> sql_param = new HashMap<String, String>();
		rpt_param.put("V_PRINT_DATE",DateTimeUtils.convertDate(DateTimeUtils.INTERCONVERSION, zDateHandler.getTheDate(), "yyyy/MM/dd", "yyy/MM/dd"));
		rpt_param.put("V_PRINT_TIME",zDateHandler.getTheTime());
//		rpt_param.put("V_TXDT",DateTimeUtils.convertDate(DateTimeUtils.convertDate(params.get("BIZDATE"),"yyyyMMdd","yyyyMMdd"),"yyyyMMdd","營業日期: yyy/MM/dd"));
		rpt_param.put("V_TXDT",DateTimeUtils.convertDate(DateTimeUtils.convertDate(params.get("BIZDATE"),"yyyyMMdd","yyyyMMdd"),"yyyyMMdd","yyy/MM/dd"));
//		rpt_param.put("V_AGENT_COMPANY_ID",params.get("AGENT_COMPANY_ID"));
		rpt_param.put("V_SND_COMPANY_ID","全部");
		rpt_param.put("V_TXID","全部");
		rpt_param.put("V_TG_RESULT","");
		rpt_param.put("V_CLEARINGPHASE","全部");
		Map<String,String> retmap =null;
		String conditionKey  ="" , agent_company_id = "";
		try {
//			conditionKey = "[\"BIZDATE\",\"AGENT_COMPANY_ID\",\"SND_COMPANY_ID\",\"CLEARINGPHASE\"]";
			conditionKey = "[\"BIZDATE\",\"AGENT_COMPANY_ID\",\"SND_COMPANY_ID\"]";
			for( AGENT_PROFILE po :company_Id_List){
				agent_company_id = po.getCOMPANY_ID();
				outputFileName= Arguments.getStringArg("BAT.FILE.NAME.ST16");
				outputFileName = outputFileName.replace("#1", agent_company_id);
				outputFileName = outputFileName.replace("#2", params.get("BIZDATE"));
				outputFileName = outputFileName.replace("#3", params.get("CLEARINGPHASE"));
				outputPath = params.get("BIZDATE")+"/"+agent_company_id+"/"+Arguments.getStringArg("BAT.LAST.PATH")+"/";
				rpt_param.put("V_AGENT_COMPANY_ID",params.get(agent_company_id));
				sql_param.putAll(params);
				sql_param.put("BIZDATE", DateTimeUtils.convertDate(params.get("BIZDATE"), "yyyyMMdd", "yyyyMMdd") );
				Map map = rptst_16_bo.getConditionData( sql_param, conditionKey);
				sql = rptst_16_bo.getSQL(map.get("sqlPath").toString(), "", "");
				List list = rponblocktab_Dao.getRptData(sql, (Map<String, String>) map.get("values"));
				retmap  = rptutils.bat_export(RptUtils.COLLECTION, "st_16", outputFileName,outputPath, rpt_param, null,list, 1);
				if(retmap.get("result").equals("TRUE")){
					result =  true;
				}else{
					result =  false;
					logger.debug("doRpt_ST16.bat_export fail...");
					logger.debug("doRpt_ST16.agent_company_id>>"+agent_company_id);
					break;
				}
			}
			return result;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.debug("doRpt_ST16.Exception>>");
			logger.debug("doRpt_ST16.agent_company_id>>"+agent_company_id);
			result =  false;
		}
		
		return result;
	}
	
	public boolean doRpt_ST17(Map<String,String> params ,List<AGENT_PROFILE> company_Id_List){
		String sql= "";
//		Agent_TxMonSumRP_{代理業者統編}_{YYYYMMDD}_{清算階段}.pdf Ex：Agent_TxMonSumRP_16606102_201501_01.pdf
		boolean result = false;
		String outputFileName= "";
		String outputPath= "";
		Map<String, Object> rpt_param = new HashMap<String, Object>();
		Map<String, String> sql_param = new HashMap<String, String>();
		rpt_param.put("V_PRINT_DATE",DateTimeUtils.convertDate(DateTimeUtils.INTERCONVERSION, zDateHandler.getTheDate(), "yyyy/MM/dd", "yyy/MM/dd"));
		rpt_param.put("V_PRINT_TIME",zDateHandler.getTheTime());
//		rpt_param.put("V_TXDT",DateTimeUtils.convertDate(DateTimeUtils.convertDate(params.get("BIZDATE"),"yyyyMMdd","yyyyMMdd"),"yyyyMMdd","營業日期: yyy/MM/dd"));
		rpt_param.put("V_TXDT",DateTimeUtils.convertDate (1,params.get("TW_YEAR")+params.get("TW_MONTH"),"yyyyMM","yyy/MM"));
		rpt_param.put("V_AGENT_COMPANY_ID",params.get("AGENT_COMPANY_ID"));
		rpt_param.put("V_SND_COMPANY_ID","全部");
		rpt_param.put("V_TXID","全部");
		rpt_param.put("V_TG_RESULT","");
		Map<String,String> retmap =null;
		String conditionKey  ="" , agent_company_id = "",yyyymm="";
		try {
//			conditionKey = "[\"TW_YEAR\",\"AGENT_COMPANY_ID\",\"SND_COMPANY_ID\",\"CLEARINGPHASE\"]";
			conditionKey = "[\"TW_YEAR\",\"AGENT_COMPANY_ID\",\"SND_COMPANY_ID\"]";
//			yyyymm =  params.get("TW_YEAR");
			yyyymm =  params.get("TW_YEAR")+params.get("TW_MONTH");
			logger.debug("yyyymm>>"+yyyymm);
			for( AGENT_PROFILE po :company_Id_List){
				agent_company_id = po.getCOMPANY_ID();
				outputFileName= Arguments.getStringArg("BAT.FILE.NAME.ST17");
				outputFileName = outputFileName.replace("#1", agent_company_id);
				outputFileName = outputFileName.replace("#2", yyyymm);
//				outputFileName = outputFileName.replace("#3", params.get("CLEARINGPHASE"));
				outputPath = yyyymm+"/"+agent_company_id+"/"+Arguments.getStringArg("BAT.LAST.PATH")+"/";
				sql_param.putAll(params);
				sql_param.put("TW_YEAR", DateTimeUtils.convertDate(params.get("TW_YEAR"),"yyyy","yyyy"));
				logger.debug("st17.TW_YEAR>>"+sql_param.get("TW_YEAR")+sql_param.get("TW_MONTH"));
				
				Map map = rptst_17_bo.getConditionData( sql_param, conditionKey);
				sql = rptst_17_bo.getSQL(map.get("sqlPath").toString(), "", "");
				logger.debug("st17.sql>>"+sql);
				logger.debug("st17.map.get(values)>>"+map.get("values"));
				
				List list = rponblocktab_Dao.getRptData(sql, (Map<String, String>) map.get("values"));
				retmap  = rptutils.bat_export(RptUtils.COLLECTION, "st_17", outputFileName,outputPath, rpt_param, null,list, 1);
				if(retmap.get("result").equals("TRUE")){
					result =  true;
				}else{
					result =  false;
					logger.debug("doRpt_ST17.bat_export fail...");
					logger.debug("doRpt_ST17.agent_company_id>>"+agent_company_id);
					break;
				}
			}
			return result;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.debug("doRpt_ST17.Exception>>");
			logger.debug("doRpt_ST17.agent_company_id>>"+agent_company_id);
			result =  false;
		}
		
		return result;
	}
	
	

	public boolean doRpt_FEE8(Map<String,String> params ,List<AGENT_PROFILE> company_Id_List){
		String sql= "";
//		Agent_FeeDaySumRP_16606102_20150201_01.pdf ;Agent_FeeDaySumRP_{代理業者統編}_{YYYYMMDD}_{清算階段}.pdf -->
		boolean result = false;
		String outputFileName= "";
		String outputPath= "";
		Map<String, Object> rpt_param = new HashMap<String, Object>();
		rpt_param.put("V_PRINT_DATE",DateTimeUtils.convertDate(DateTimeUtils.INTERCONVERSION, zDateHandler.getTheDate(), "yyyy/MM/dd", "yyy/MM/dd"));
		rpt_param.put("V_PRINT_TIME",zDateHandler.getTheTime());
//		rpt_param.put("V_TXDT",DateTimeUtils.convertDate(DateTimeUtils.convertDate(params.get("BIZDATE"),"yyyyMMdd","yyyyMMdd"),"yyyyMMdd","營業日期: yyy/MM/dd"));
		rpt_param.put("V_TXDT",DateTimeUtils.convertDate(DateTimeUtils.convertDate(params.get("BIZDATE"),"yyyyMMdd","yyyyMMdd"),"yyyyMMdd","yyy/MM/dd"));
		rpt_param.put("V_SND_COMPANY_ID","全部");
		rpt_param.put("V_TXID","全部");
		rpt_param.put("V_TG_RESULT","全部");
		rpt_param.put("V_CLEARINGPHASE","全部");
		Map<String,String> retmap =null;
		String conditionKey  ="" , agent_company_id = "";
		try {
			conditionKey = "[\"BIZDATE\",\"AGENT_COMPANY_ID\",\"SND_COMPANY_ID\"]";
			for( AGENT_PROFILE po :company_Id_List){
				agent_company_id = po.getCOMPANY_ID();
				outputFileName= Arguments.getStringArg("BAT.FILE.NAME.FEE8");
				outputFileName = outputFileName.replace("#1", agent_company_id);
				outputFileName = outputFileName.replace("#2", params.get("BIZDATE"));
				outputFileName = outputFileName.replace("#3", params.get("CLEARINGPHASE"));
				outputPath = params.get("BIZDATE")+"/"+agent_company_id+"/"+Arguments.getStringArg("BAT.LAST.PATH")+"/";
				rpt_param.put("V_AGENT_COMPANY_ID",agent_company_id);
				Map map = rptfee_8_bo.getConditionData( params, conditionKey);
				sql = rptfee_8_bo.getSQL(map.get("sqlPath").toString(), "", "");
				List list = rponblocktab_Dao.getRptData(sql, (Map<String, String>) map.get("values"));
				retmap  = rptutils.bat_export(RptUtils.COLLECTION, "fee_8", outputFileName,outputPath, rpt_param, null,list, 1);
				if(retmap.get("result").equals("TRUE")){
					result =  true;
				}else{
					result =  false;
					logger.debug("doRpt_FEE8.bat_export fail...");
					logger.debug("doRpt_FEE8.agent_company_id>>"+agent_company_id);
					break;
				}
			}
			return result;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.debug("doRpt_FEE8.Exception>>"+e);
			logger.debug("doRpt_FEE8.agent_company_id>>"+agent_company_id);
			result =  false;
		}
		
		return result;
	}
	
	
	
	/**
	 * 批次代理業者手續費月報表
	 * @param params
	 * @param company_Id_List
	 * @return
	 */
	public boolean doRpt_FEE9(Map<String,String> params ,List<AGENT_PROFILE> company_Id_List){
		String sql= "";
//		Agent_FeeMonSumRP_16606102_20150201.pdf ;Agent_FeeMonSumRP_{代理業者統編}_{YYYYMMDD}.pdf
		boolean result = false;
		String outputFileName= "";
		String outputPath= "";
		Map<String, Object> rpt_param = new HashMap<String, Object>();
		Map<String, String> sql_param = new HashMap<String, String>();
		rpt_param.put("V_PRINT_DATE",DateTimeUtils.convertDate(DateTimeUtils.INTERCONVERSION, zDateHandler.getTheDate(), "yyyy/MM/dd", "yyy/MM/dd"));
		rpt_param.put("V_PRINT_TIME",zDateHandler.getTheTime());
//		rpt_param.put("V_TXDT",DateTimeUtils.convertDate(DateTimeUtils.convertDate(params.get("BIZDATE"),"yyyyMMdd","yyyyMMdd"),"yyyyMMdd","營業日期: yyy/MM/dd"));
		rpt_param.put("V_TXDT",DateTimeUtils.convertDate (1,params.get("TW_YEAR")+params.get("TW_MONTH"),"yyyyMM","yyy/MM"));
		rpt_param.put("V_AGENT_COMPANY_ID",params.get("AGENT_COMPANY_ID"));
		rpt_param.put("V_SND_COMPANY_ID","全部");
		rpt_param.put("V_TXID","全部");
		Map<String,String> retmap =null;
		String conditionKey  ="" , agent_company_id = "",yyyymm="";
		try {
//			conditionKey = "[\"TW_YEAR\",\"AGENT_COMPANY_ID\",\"SND_COMPANY_ID\",\"CLEARINGPHASE\"]";
			conditionKey = "[\"TW_YEAR\",\"AGENT_COMPANY_ID\",\"SND_COMPANY_ID\"]";
//			yyyymm =  params.get("TW_YEAR");
			yyyymm =  params.get("TW_YEAR")+params.get("TW_MONTH");
			logger.debug("yyyymm>>"+yyyymm);
			for( AGENT_PROFILE po :company_Id_List){
				agent_company_id = po.getCOMPANY_ID();
				outputFileName= Arguments.getStringArg("BAT.FILE.NAME.FEE9");
				outputFileName = outputFileName.replace("#1", agent_company_id);
				outputFileName = outputFileName.replace("#2", yyyymm);
				outputPath = yyyymm+"/"+agent_company_id+"/"+Arguments.getStringArg("BAT.LAST.PATH")+"/";
				sql_param.putAll(params);
				sql_param.put("TW_YEAR", DateTimeUtils.convertDate(params.get("TW_YEAR"),"yyyy","yyyy"));
				Map map = rptfee_9_bo.getConditionData( sql_param, conditionKey);
				sql = rptfee_9_bo.getSQL(map.get("sqlPath").toString(), "", "");
				List list = rponblocktab_Dao.getRptData(sql, (Map<String, String>) map.get("values"));
				retmap  = rptutils.bat_export(RptUtils.COLLECTION, "fee_9", outputFileName,outputPath, rpt_param, null,list, 1);
				if(retmap.get("result").equals("TRUE")){
					result =  true;
				}else{
					result =  false;
					logger.debug("doRpt_FEE9.bat_export fail...");
					logger.debug("doRpt_FEE9.agent_company_id>>"+agent_company_id);
					break;
				}
			}
			return result;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.debug("doRpt_FEE9.Exception>>"+e);
			logger.debug("doRpt_FEE9.agent_company_id>>"+agent_company_id);
			result =  false;
		}
		
		return result;
	}
	
	

	public boolean doAGENTDL_TX001(Map<String,String> params ,List<AGENT_PROFILE> company_Id_List){
//		Agent_txlist_16606102_20150201_01.txt ;Agent_txlist_{代理業者統編}_{YYYYMMDD}_{清算階段}.txt
		boolean result = false;
		String outputFileName= "";
		String outputPath= "";
		String agent_company_id = "" ,bizdate="",clearingphase="" ,dirpath="";
		String proKey = "agenttx001FilePrefix";
		try {
			Map<String,String> valueMap = codeUtils.getPropertyValue("Configuration.properties","agentDirPath",proKey);
			//無法取得properties
			if(valueMap == null){
				logger.debug("無法取得Configuration.properties...");
				return false;
			}
			String tmpFileDir ="";
			ServletContext context = WebServletUtils.getServletContext();
			if(context != null){
				tmpFileDir = context.getRealPath(Arguments.getStringArg("RPT.PDF.PATH"));
			}
			dirpath = OSValidator.isWindows()?tmpFileDir:valueMap.get("agentDirPath");
			bizdate = params.get("BIZDATE");
			clearingphase = params.get("CLEARINGPHASE");
			for( AGENT_PROFILE po :company_Id_List){
				agent_company_id = po.getCOMPANY_ID();
				outputFileName ="";
				outputPath = "";
				outputFileName = valueMap.get(proKey)+agent_company_id+"_"+bizdate+"_"+clearingphase+".txt";
				outputPath = dirpath+"/"+params.get("BIZDATE")+"/"+agent_company_id+"/"+Arguments.getStringArg("BAT.LAST.PATH")+"/";
				outputPath = outputPath+outputFileName;
				logger.debug("doAFENTDL_TX001.outputPath>>"+outputPath);
				Map<String,Object> dataMap  = agentdl_tx001_bo.getTXT(bizdate, agent_company_id, "", clearingphase, false);
				//正常
				if(dataMap.get("data") != null){
					//檔案放到資料夾下
					codeUtils.putFileToPath(outputPath,(byte[])dataMap.get("data"));
					result =  true;
				}
				//有問題
				else{
					result =  false;
					logger.debug("doAFENTDL_TX001.bat_export fail...");
					logger.debug("doAFENTDL_TX001.agent_company_id>>"+agent_company_id);
					logger.debug("doAFENTDL_TX001.dataMap.get(message)>>"+dataMap.get("message"));
				}
				
			}
			return result;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.debug("doAFENTDL_TX001.Exception>>"+e);
			logger.debug("doAFENTDL_TX001.agent_company_id>>"+agent_company_id);
			result =  false;
		}
		
		return result;
	}
	
	public boolean doAGENTDL_ST001(Map<String,String> params ,List<AGENT_PROFILE> company_Id_List){
//		Agent_TxDaySum_16606102_20150201_01.txt ;Agent_TxDaySum_{代理業者統編}_{YYYYMMDD}_{清算階段}.txt
		boolean result = false;
		String outputFileName= "";
		String outputPath= "";
		String agent_company_id = "" ,bizdate="",clearingphase="" ,dirpath="";
		String proKey = "agentst001FilePrefix";
		try {
			Map<String,String> valueMap = codeUtils.getPropertyValue("Configuration.properties","agentDirPath",proKey);
			//無法取得properties
			if(valueMap == null){
				logger.debug("無法取得Configuration.properties...");
				return false;
			}
			String tmpFileDir ="";
			ServletContext context = WebServletUtils.getServletContext();
			if(context != null){
				tmpFileDir = context.getRealPath(Arguments.getStringArg("RPT.PDF.PATH"));
			}
			dirpath = OSValidator.isWindows()?tmpFileDir:valueMap.get("agentDirPath");
			bizdate = params.get("BIZDATE");
//			clearingphase = params.get("CLEARINGPHASE");
			for( AGENT_PROFILE po :company_Id_List){
				agent_company_id = po.getCOMPANY_ID();
				outputFileName ="";
				outputPath = "";
//				outputFileName = valueMap.get(proKey)+agent_company_id+"_"+bizdate+"_"+clearingphase+".txt";
				outputFileName = valueMap.get(proKey)+agent_company_id+"_"+bizdate+".txt";
				outputPath = dirpath+"/"+params.get("BIZDATE")+"/"+agent_company_id+"/"+Arguments.getStringArg("BAT.LAST.PATH")+"/";
				outputPath = outputPath+outputFileName;
				logger.debug("doAFENTDL_ST001.outputPath>>"+outputPath);
				Map<String,Object> dataMap  = agentdl_st001_bo.getTXT(bizdate, agent_company_id, "", clearingphase, false);
				//正常
				if(dataMap.get("data") != null){
					//檔案放到資料夾下
					codeUtils.putFileToPath(outputPath,(byte[])dataMap.get("data"));
					result =  true;
				}
				//有問題
				else{
					result =  false;
					logger.debug("doAFENTDL_ST001.bat_export fail...");
					logger.debug("doAFENTDL_ST001.agent_company_id>>"+agent_company_id);
					logger.debug("doAFENTDL_ST001.dataMap.get(message)>>"+dataMap.get("message"));
				}
				
			}
			return result;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.debug("doAFENTDL_ST001.Exception>>"+e);
			logger.debug("doAFENTDL_ST001.agent_company_id>>"+agent_company_id);
			result =  false;
		}
		
		return result;
	}
	
	public boolean doAGENTDL_FEE001(Map<String,String> params ,List<AGENT_PROFILE> company_Id_List){
//		AGENT_FeeDaySum_16606102_20150201_01.txt ;AGENT_FeeDaySum_{代理業者統編}_{YYYYMMDD}_{清算階段}.txt
		boolean result = false;
		String outputFileName= "";
		String outputPath= "";
		String agent_company_id = "" ,bizdate="",clearingphase="" ,dirpath="";
		String proKey = "agentfee001FilePrefix";
		try {
			Map<String,String> valueMap = codeUtils.getPropertyValue("Configuration.properties","agentDirPath",proKey);
			//無法取得properties
			if(valueMap == null){
				logger.debug("無法取得Configuration.properties...");
				return false;
			}
			String tmpFileDir ="";
			ServletContext context = WebServletUtils.getServletContext();
			if(context != null){
				tmpFileDir = context.getRealPath(Arguments.getStringArg("RPT.PDF.PATH"));
			}
			dirpath = OSValidator.isWindows()?tmpFileDir:valueMap.get("agentDirPath");
			bizdate = params.get("BIZDATE");
			clearingphase = params.get("CLEARINGPHASE");
			for( AGENT_PROFILE po :company_Id_List){
				agent_company_id = po.getCOMPANY_ID();
				outputFileName ="";
				outputPath = "";
//				outputFileName = valueMap.get(proKey)+agent_company_id+"_"+bizdate+"_"+clearingphase+".txt";
				clearingphase = "";
				outputFileName = valueMap.get(proKey)+agent_company_id+"_"+bizdate+".txt";
				outputPath = dirpath+"/"+params.get("BIZDATE")+"/"+agent_company_id+"/"+Arguments.getStringArg("BAT.LAST.PATH")+"/";
				outputPath = outputPath+outputFileName;
				logger.debug("doAFENTDL_FEE001.outputPath>>"+outputPath);
				Map<String,Object> dataMap  = agentdl_fee001_bo.getTXT(bizdate, agent_company_id, "", clearingphase, false);
				//正常
				if(dataMap.get("data") != null){
					//檔案放到資料夾下
					codeUtils.putFileToPath(outputPath,(byte[])dataMap.get("data"));
					result =  true;
				}
				//有問題
				else{
					result =  false;
					logger.debug("doAFENTDL_FEE001.bat_export fail...");
					logger.debug("doAFENTDL_FEE001.agent_company_id>>"+agent_company_id);
					logger.debug("doAFENTDL_FEE001.dataMap.get(message)>>"+dataMap.get("message"));
				}
				
			}
			return result;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.debug("doAFENTDL_FEE001.Exception>>"+e);
			logger.debug("doAFENTDL_FEE001.agent_company_id>>"+agent_company_id);
			result =  false;
		}
		
		return result;
	}
	
	public Map<String,Object> doAGENTDL_COMM_DATA(Map<String,String> params ,List<AGENT_PROFILE> company_Id_List){
			return basedata_download_bo.getFilenameListAndDataList4Agent();
	}
	
	
/**
 * 代理業者Http下載壓縮檔
 * @param params
 * @param company_Id_List
 * @return
 */
	public boolean doAgent_DL_ZIP(Map<String,String> params ,List<AGENT_PROFILE> company_Id_List ){
//		public boolean doAgent_DL_ZIP(Map<String,String> params ,List<AGENT_PROFILE> company_Id_List ,List<byte[]> commdataList,List<String> commFileNameList){
		boolean result = false;
		String zipName = "" , bizdate ="",yyyymm="", clearingphase="",fileName="",agentDirPath="",filePath="",filePath_Mon="",putPath="",agent_company_id="", lastpath="";
		String tmpFileDir = "",tmpFilePath = "";
		List<String> fileList = null;
		List<String> fileNameList = null;
		List<byte[]> dataList = null;
		byte[] byteZIP = null;
		String spilt = File.separator;
		try {
			
			bizdate =  params.get("BIZDATE");
			yyyymm=  zDateHandler.getEACH_PrveMonth(bizdate,"yyyyMMdd");
			clearingphase = params.get("CLEARINGPHASE");
//			Map<String,String> property = codeUtils.getPropertyValue("Configuration.properties", "agent_Zip_List_pdf", "agent_Zip_List_txt","agent_Zip_name","agentDirPath");
			Map<String,String> property = codeUtils.getPropertyValue("Configuration.properties", "agent_Zip_List_pdf_"+clearingphase, "agent_Zip_List_txt_"+clearingphase,"agent_Zip_name","agentDirPath");
//		找出要的前置檔名來與其他變數[營業日、清算階段..等]來組合出檔名，來將其製作成壓縮檔
//			Map<String,String> agent_Zip_List_pdf = JSONUtils.json2map(property.get("agent_Zip_List_pdf"));
//			Map<String,String> agent_Zip_List_txt = JSONUtils.json2map(property.get("agent_Zip_List_txt"));
			Map<String,String> agent_Zip_List_pdf = JSONUtils.json2map(property.get("agent_Zip_List_pdf_"+clearingphase));
			Map<String,String> agent_Zip_List_txt = JSONUtils.json2map(property.get("agent_Zip_List_txt_"+clearingphase));
			ServletContext context = WebServletUtils.getServletContext();
			if(context != null){
				tmpFileDir = context.getRealPath(Arguments.getStringArg("RPT.PDF.PATH"));
			}
//			檔案前置路徑
			agentDirPath = OSValidator.isWindows() ? tmpFileDir :property.get("agentDirPath");
			lastpath=Arguments.getStringArg("BAT.LAST.PATH");
			for(AGENT_PROFILE po :company_Id_List){
				fileName = "";filePath="";
				fileList = new LinkedList<>();
				fileNameList = new LinkedList<>();
				dataList = new LinkedList<byte[]>();
				agent_company_id = po.getCOMPANY_ID();
				//壓縮檔檔名
				zipName = property.get("agent_Zip_name")+"_"+agent_company_id+"_"+bizdate+"_"+clearingphase+".zip";
				filePath = agentDirPath+spilt+bizdate+spilt+agent_company_id+spilt+lastpath;
				putPath=filePath;
				filePath_Mon = agentDirPath+spilt+yyyymm+spilt+agent_company_id+spilt+lastpath;
//				取得pdf檔案
				for(String key : agent_Zip_List_pdf.keySet()){
					tmpFilePath = "";
					if(agent_Zip_List_pdf.get(key).indexOf("Mon")>0){
						fileName = agent_Zip_List_pdf.get(key)+agent_company_id+"_"+yyyymm+".pdf";
						tmpFilePath = filePath_Mon+spilt+fileName;
					}else{
//						Agent_txlistRP_04226091_20160106_02.pdf
						if(key.equals("tx5")){
							fileName = agent_Zip_List_pdf.get(key)+agent_company_id+"_"+bizdate+"_"+clearingphase+".pdf";
						}else{
							fileName = agent_Zip_List_pdf.get(key)+agent_company_id+"_"+bizdate+".pdf";
						}
						System.out.println("fileName>>"+fileName);
						tmpFilePath = filePath+spilt+fileName;
					}
					fileNameList.add(fileName);
					fileList.add(tmpFilePath);
					dataList.add(getPdf2ByteArray(tmpFilePath));
				}
//				取得txt檔案
				for(String key : agent_Zip_List_txt.keySet()){
					tmpFilePath = "";
					if(agent_Zip_List_txt.get(key).indexOf("Mon")>0){
						fileName = agent_Zip_List_txt.get(key)+agent_company_id+"_"+yyyymm+".txt";
						tmpFilePath = filePath_Mon+spilt+fileName;
					}else{
						if(key.equals("agenttx001")){
							fileName = agent_Zip_List_txt.get(key)+agent_company_id+"_"+bizdate+"_"+clearingphase+".txt";
						}else{
							fileName = agent_Zip_List_txt.get(key)+agent_company_id+"_"+bizdate+".txt";
							
						}
						tmpFilePath = filePath+spilt+fileName;
					}
					fileNameList.add(fileName);
					fileList.add(tmpFilePath);
					dataList.add(getPdf2ByteArray(tmpFilePath));
				}
				
//				if(commdataList == null || commFileNameList ==null){
//					logger.debug("commdataList is null..");
//					return false;
//				}
//				dataList.addAll(commdataList);
//				fileNameList.addAll(commFileNameList);
//				dataList.addAll(dataList.size(), commdataList);
//				fileNameList.addAll(fileNameList.size(),commFileNameList);
				
//				產生壓縮檔資料串流
				byteZIP = codeUtils.createZIP(dataList,fileNameList,null);
//				產生壓縮檔
				logger.debug("ZIP_PATH>>"+putPath+spilt+zipName);
				codeUtils.putFileToPath(putPath+spilt+zipName,byteZIP);
//				刪除原始資料
//				for(String pathname :fileList){
//					File file = new File(pathname);
//					if(!file.delete()){
//						file.delete();
//					}
//				}
			}//for end
			result= true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * 代理業者Http下載壓縮檔(月)
	 * @param params
	 * @param company_Id_List
	 * @return
	 */
	public boolean doAgent_DL_ZIP_Mon(Map<String,String> params ,List<AGENT_PROFILE> company_Id_List ){
//		public boolean doAgent_DL_ZIP_Mon(Map<String,String> params ,List<AGENT_PROFILE> company_Id_List ,List<byte[]> commdataList,List<String> commFileNameList){
		boolean result = false;
		String zipName = "" , bizdate ="",yyyymm="", clearingphase="",fileName="",agentDirPath="",filePath="",filePath_Mon="",putPath="",agent_company_id="", lastpath="";
		String tmpFileDir = "",tmpFilePath = "";
		List<String> fileList = null;
		List<String> fileNameList = null;
		List<byte[]> dataList = null;
		byte[] byteZIP = null;
		String spilt = File.separator;
		try {
			
			bizdate =  params.get("BIZDATE");
//			bizdate =  "20160408";
			yyyymm=  zDateHandler.getEACH_PrveMonth(bizdate,"yyyyMMdd");
			clearingphase = params.get("CLEARINGPHASE");
			Map<String,String> property = codeUtils.getPropertyValue("Configuration.properties", "agent_Zip_List_pdf_M", "agent_Zip_List_txt_M","agent_Zip_name_M","agentDirPath");
//		找出要的前置檔名來與其他變數[營業日、清算階段..等]來組合出檔名，來將其製作成壓縮檔
			Map<String,String> agent_Zip_List_pdf = JSONUtils.json2map(property.get("agent_Zip_List_pdf_M"));
			Map<String,String> agent_Zip_List_txt = JSONUtils.json2map(property.get("agent_Zip_List_txt_M"));
			ServletContext context = WebServletUtils.getServletContext();
			if(context != null){
				tmpFileDir = context.getRealPath(Arguments.getStringArg("RPT.PDF.PATH"));
			}
//			檔案前置路徑
			agentDirPath = OSValidator.isWindows() ? tmpFileDir :property.get("agentDirPath");
			lastpath=Arguments.getStringArg("BAT.LAST.PATH");
			for(AGENT_PROFILE po :company_Id_List){
				fileName = "";filePath="";
				fileList = new LinkedList<>();
				fileNameList = new LinkedList<>();
				dataList = new LinkedList<byte[]>();
				agent_company_id = po.getCOMPANY_ID();
				//壓縮檔檔名
				zipName = property.get("agent_Zip_name_M")+"_"+agent_company_id+"_"+yyyymm+".zip";
				filePath = agentDirPath+spilt+yyyymm+spilt+agent_company_id+spilt+lastpath;
				putPath=filePath;
				filePath_Mon = agentDirPath+spilt+yyyymm+spilt+agent_company_id+spilt+lastpath;
//				取得pdf檔案
				for(String key : agent_Zip_List_pdf.keySet()){
					tmpFilePath = "";
					if(agent_Zip_List_pdf.get(key).indexOf("Mon")>0){
						fileName = agent_Zip_List_pdf.get(key)+agent_company_id+"_"+yyyymm+".pdf";
						tmpFilePath = filePath_Mon+spilt+fileName;
					}else{
//						Agent_txlistRP_04226091_20160106_02.pdf
						fileName = agent_Zip_List_pdf.get(key)+agent_company_id+"_"+bizdate+"_"+clearingphase+".pdf";
						System.out.println("fileName>>"+fileName);
						tmpFilePath = filePath+spilt+fileName;
					}
					fileNameList.add(fileName);
					fileList.add(tmpFilePath);
					dataList.add(getPdf2ByteArray(tmpFilePath));
				}
//				20160205 note by hugo 目前代理業者資料下載檔無月統計
//				取得txt檔案
//				for(String key : agent_Zip_List_txt.keySet()){
//					tmpFilePath = "";
//					if(agent_Zip_List_txt.get(key).indexOf("Mon")>0){
//						fileName = agent_Zip_List_txt.get(key)+agent_company_id+"_"+yyyymm+".txt";
//						tmpFilePath = filePath_Mon+spilt+fileName;
//					}else{
//						fileName = agent_Zip_List_txt.get(key)+agent_company_id+"_"+bizdate+"_"+clearingphase+".txt";
//						tmpFilePath = filePath+spilt+fileName;
//					}
//					fileNameList.add(fileName);
//					fileList.add(tmpFilePath);
//					dataList.add(getPdf2ByteArray(tmpFilePath));
//				}
				
//				if(commdataList == null || commFileNameList ==null){
//					logger.debug("commdataList is null..");
//					return false;
//				}
//				dataList.addAll(dataList.size(), commdataList);
//				fileNameList.addAll(fileNameList.size(),commFileNameList);
				
				
//				產生壓縮檔資料串流
				byteZIP = codeUtils.createZIP(dataList,fileNameList,null);
//				產生壓縮檔
				logger.debug("Mon..ZIP_PATH>>"+putPath+spilt+zipName);
				codeUtils.putFileToPath(putPath+spilt+zipName,byteZIP);
//				刪除原始資料
//				for(String pathname :fileList){
//					File file = new File(pathname);
//					if(!file.delete()){
//						file.delete();
//					}
//				}
			}//for end
			result= true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	public byte[] getPdf2ByteArray(String fullFilePath) throws IOException{
		byte[] res = null;
		FileInputStream in =null;
        try {
        	in =new FileInputStream(new File(fullFilePath));
        	res = IOUtils.toByteArray(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.debug("getPdf2ByteArray.fullFilePath>>"+fullFilePath);
			logger.debug("getPdf2ByteArray.IOException>>"+e);
			throw e;
		}finally{
			if(in!=null){
				in.close();
			}
		}
		return res;
	}
	

	public RPONBLOCKTAB_Dao getRponblocktab_Dao() {
		return rponblocktab_Dao;
	}

	public void setRponblocktab_Dao(RPONBLOCKTAB_Dao rponblocktab_Dao) {
		this.rponblocktab_Dao = rponblocktab_Dao;
	}

	public RptUtils getRptutils() {
		return rptutils;
	}

	public void setRptutils(RptUtils rptutils) {
		this.rptutils = rptutils;
	}

	public CodeUtils getCodeUtils() {
		return codeUtils;
	}

	public void setCodeUtils(CodeUtils codeUtils) {
		this.codeUtils = codeUtils;
	}

	public String getBizDate() {
		return bizDate;
	}

	public void setBizDate(String bizDate) {
		this.bizDate = bizDate;
	}


	public String getClearingphase() {
		return clearingphase;
	}




	public void setClearingphase(String clearingphase) {
		this.clearingphase = clearingphase;
	}




	public Integer getBatch_proc_seq() {
		return batch_proc_seq;
	}




	public void setBatch_proc_seq(Integer batch_proc_seq) {
		this.batch_proc_seq = batch_proc_seq;
	}




	public boolean isRunning() {
		return isRunning;
	}




	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}

	public EACH_BATCH_NOTIFY_Dao getBatch_notify_Dao() {
		return batch_notify_Dao;
	}

	public void setBatch_notify_Dao(EACH_BATCH_NOTIFY_Dao batch_notify_Dao) {
		this.batch_notify_Dao = batch_notify_Dao;
	}

	public EACH_BATCH_STATUS_Dao getBatch_status_Dao() {
		return batch_status_Dao;
	}

	public void setBatch_status_Dao(EACH_BATCH_STATUS_Dao batch_status_Dao) {
		this.batch_status_Dao = batch_status_Dao;
	}

	public EACH_BATCH_DEF_Dao getBatch_def_Dao() {
		return batch_def_Dao;
	}

	public void setBatch_def_Dao(EACH_BATCH_DEF_Dao batch_def_Dao) {
		this.batch_def_Dao = batch_def_Dao;
	}

	public RPTTX_5_BO getRpttx_5_bo() {
		return rpttx_5_bo;
	}

	public void setRpttx_5_bo(RPTTX_5_BO rpttx_5_bo) {
		this.rpttx_5_bo = rpttx_5_bo;
	}

	public RPTST_16_BO getRptst_16_bo() {
		return rptst_16_bo;
	}

	public void setRptst_16_bo(RPTST_16_BO rptst_16_bo) {
		this.rptst_16_bo = rptst_16_bo;
	}

	public RPTST_17_BO getRptst_17_bo() {
		return rptst_17_bo;
	}

	public void setRptst_17_bo(RPTST_17_BO rptst_17_bo) {
		this.rptst_17_bo = rptst_17_bo;
	}

	public AGENT_PROFILE_BO getAgent_profile_bo() {
		return agent_profile_bo;
	}

	public void setAgent_profile_bo(AGENT_PROFILE_BO agent_profile_bo) {
		this.agent_profile_bo = agent_profile_bo;
	}

	public RPTFEE_8_BO getRptfee_8_bo() {
		return rptfee_8_bo;
	}

	public void setRptfee_8_bo(RPTFEE_8_BO rptfee_8_bo) {
		this.rptfee_8_bo = rptfee_8_bo;
	}

	public RPTFEE_9_BO getRptfee_9_bo() {
		return rptfee_9_bo;
	}

	public void setRptfee_9_bo(RPTFEE_9_BO rptfee_9_bo) {
		this.rptfee_9_bo = rptfee_9_bo;
	}

	public AGENTDL_TX001_BO getAgentdl_tx001_bo() {
		return agentdl_tx001_bo;
	}

	public void setAgentdl_tx001_bo(AGENTDL_TX001_BO agentdl_tx001_bo) {
		this.agentdl_tx001_bo = agentdl_tx001_bo;
	}

	public AGENTDL_ST001_BO getAgentdl_st001_bo() {
		return agentdl_st001_bo;
	}

	public void setAgentdl_st001_bo(AGENTDL_ST001_BO agentdl_st001_bo) {
		this.agentdl_st001_bo = agentdl_st001_bo;
	}

	public AGENTDL_FEE001_BO getAgentdl_fee001_bo() {
		return agentdl_fee001_bo;
	}

	public void setAgentdl_fee001_bo(AGENTDL_FEE001_BO agentdl_fee001_bo) {
		this.agentdl_fee001_bo = agentdl_fee001_bo;
	}

	public AGENTDL_FEE002_BO getAgentdl_fee002_bo() {
		return agentdl_fee002_bo;
	}

	public void setAgentdl_fee002_bo(AGENTDL_FEE002_BO agentdl_fee002_bo) {
		this.agentdl_fee002_bo = agentdl_fee002_bo;
	}

	public BASEDATA_DOWNLOAD_BO getBasedata_download_bo() {
		return basedata_download_bo;
	}

	public void setBasedata_download_bo(BASEDATA_DOWNLOAD_BO basedata_download_bo) {
		this.basedata_download_bo = basedata_download_bo;
	}

	
	
	
	
}
