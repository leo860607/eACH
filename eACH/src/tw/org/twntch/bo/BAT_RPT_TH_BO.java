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

import javax.servlet.ServletOutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import tw.org.twntch.db.dao.hibernate.BANK_GROUP_Dao;
import tw.org.twntch.db.dao.hibernate.EACH_BATCH_DEF_Dao;
import tw.org.twntch.db.dao.hibernate.EACH_BATCH_NOTIFY_Dao;
import tw.org.twntch.db.dao.hibernate.EACH_BATCH_STATUS_Dao;
import tw.org.twntch.db.dao.hibernate.RPONBLOCKTAB_Dao;
import tw.org.twntch.po.BANK_GROUP;
import tw.org.twntch.po.EACH_BATCH_DEF;
import tw.org.twntch.po.EACH_BATCH_STATUS;
import tw.org.twntch.po.EACH_BATCH_STATUS_PK;
import tw.org.twntch.util.CodeUtils;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.RptUtils;
import tw.org.twntch.util.zDateHandler;

/**
 * 批次產生報表
 * @author Hugo
 *
 */
public class BAT_RPT_TH_BO  implements Runnable {
	private Logger logger = Logger.getLogger(BAT_RPT_TH_BO.class.getName());
	private RPTTX_1_BO rpttx_1_bo ;
	private RPTTX_2_BO rpttx_2_bo ;
	private RPTTX_3_BO rpttx_3_bo ;
	private RPTCL_1_BO rptcl_1_bo ;
	private RPTFEE_1_BO rptfee_1_bo ;
	private BANK_GROUP_Dao bank_group_Dao ;
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
	
	public Map do_Bat_Rpt(String bizDate , String clearingphase ,int batch_proc_seq){
//		執行相關批次報表，不管錯誤還是失敗都要回傳 ，msg="明細1成功，明細2失敗" 有一個失敗就算失敗result="FALSE"
		List<BANK_GROUP> optBank_List = bank_group_Dao.getBgbkIdList_2();
		//測試用，要記的註解
//		List<BANK_GROUP> optBank_List = bank_group_Dao.find(" FROM tw.org.twntch.po.BANK_GROUP WHERE BGBK_ID = '4510000' ");//測試用，要記的註解
		optBank_List = optBank_List == null ? new LinkedList():optBank_List;
//		20170802 edit by hugo 新增銷帳行(3)
		String opt_type = "0,1,2,3";
		String semicolon = "";
		int errorcount = 0;
		StringBuffer msg = new StringBuffer();
		StringBuffer str = new StringBuffer();
		List<String> opt_type_List = Arrays.asList(opt_type.split(",")); 
		Map<String,String> params = new HashMap<String,String>();
		Map<String,String> retMap = new HashMap<String,String>();
		params.put("BIZDATE", bizDate);
		params.put("CLEARINGPHASE", clearingphase);
		String tmp1 = "";
		EACH_BATCH_STATUS_PK id = new EACH_BATCH_STATUS_PK(bizDate, clearingphase, batch_proc_seq);
		id = new EACH_BATCH_STATUS_PK(bizDate, clearingphase, batch_proc_seq);
		String str1 = "1.結算日統計報表檔 #1-1 #1-2 #1-3";
		String str2 = ";2.收費(手續費)日統計報表檔   #2-1 #2-2 #2-3";
		String str3 = ";3.交易明細資料報表檔 #3-1 #3-2 #3-3";
		String suc = "產生成功";
		String fail = "產生失敗";
		String notyet = "未執行";
		String run = "執行中";
		try {
			EACH_BATCH_STATUS po = batch_status_Dao.get(id);
			tmp1= zDateHandler.getTheTime_MS();
			msg.append(str1.replace("#1-1", run).replace("#1-2" , " start "+tmp1).replace("#1-3" , ""));
			msg.append(str2.replace("#2-1", "未執行").replace("#2-2" , "").replace("#2-3" , ""));
			msg.append(str3.replace("#3-1", "未執行").replace("#3-2" , "").replace("#3-3" , ""));
			po = tempSave(id, po, msg.toString());
			if( !doRpt_CL1(params, optBank_List, opt_type_List)){
				errorcount++;
				str1 = str1.replace("#1-1", fail).replace("#1-2" , " start "+tmp1).replace("#1-3" , " end "+zDateHandler.getTheTime_MS());
//				msg.append(" 結算日統計報表檔產生失敗  ");
//				msg.append(" start "+tmp1);
//				msg.append(" end "+zDateHandler.getTheTime_MS());
			}else{
				str1 = str1.replace("#1-1", suc).replace("#1-2" , " start "+tmp1).replace("#1-3" , " end "+zDateHandler.getTheTime_MS());
//				msg.append(" 結算日統計報表檔產生成功  ");
//				msg.append(" start "+tmp1);
//				msg.append(" end "+zDateHandler.getTheTime_MS());
			}
			msg.delete(0, msg.length());
			msg.append(str1);
			msg.append(str2.replace("#2-1", notyet).replace("#2-2" , "").replace("#2-3" , ""));
			msg.append(str3.replace("#3-1", notyet).replace("#3-2" , "").replace("#3-3" , ""));
			po = tempSave(id, po, msg.toString());
			
			tmp1= zDateHandler.getTheTime_MS();
			msg.delete(0, msg.length());
			msg.append(str1);
			msg.append(str2.replace("#2-1", run).replace("#2-2" , " start "+tmp1).replace("#2-3" , ""));
			msg.append(str3.replace("#3-1", notyet).replace("#3-2" , "").replace("#3-3" , ""));
			po = tempSave(id, po, msg.toString());
			
			if( !doRpt_FEE1(params, optBank_List, opt_type_List)){
				errorcount++;
				str2 = str2.replace("#2-1", fail).replace("#2-2" , " start "+tmp1).replace("#2-3" , " end "+zDateHandler.getTheTime_MS());
//				msg.append(" ; 收費(手續費)日統計報表檔產生失敗  ");
//				msg.append(" start "+tmp1);
//				msg.append(" end "+zDateHandler.getTheTime_MS());
			}else{
				str2 = str2.replace("#2-1", suc).replace("#2-2" , " start "+tmp1).replace("#2-3" , " end "+zDateHandler.getTheTime_MS());
//				msg.append(" ; 收費(手續費)日統計報表檔產生成功  ");
//				msg.append(" start "+tmp1);
//				msg.append(" end "+zDateHandler.getTheTime_MS());
			}
			msg.delete(0, msg.length());
			msg.append(str1);
			msg.append(str2);
			msg.append(str3.replace("#3-1", notyet).replace("#3-2" , "").replace("#3-3" , ""));
			po = tempSave(id, po, msg.toString());
			tmp1= zDateHandler.getTheTime_MS();
			semicolon = msg.length()==0?"":" ;";
			msg.delete(0, msg.length());
			msg.append(str1);
			msg.append(str2);
			msg.append(str3.replace("#3-1", run).replace("#3-2" , " start "+tmp1).replace("#3-3" , ""));
			po = tempSave(id, po, msg.toString());
			if( !doRpt_TX(params, optBank_List, opt_type_List)){
				errorcount++;
				str3 = str3.replace("#3-1", fail).replace("#3-2" , " start "+tmp1).replace("#3-3" , " end "+zDateHandler.getTheTime_MS());
//				msg.append(semicolon);
//				msg.append(" ; 交易明細資料報表檔產生失敗  ");
//				msg.append(" start "+tmp1);
//				msg.append(" end "+zDateHandler.getTheTime_MS());
			}else{
				str3 = str3.replace("#3-1", suc).replace("#3-2" , " start "+tmp1).replace("#3-3" , " end "+zDateHandler.getTheTime_MS());
//				msg.append(semicolon);
//				msg.append(" ; 交易明細資料報表檔產生成功  ");
//				msg.append(" start "+tmp1);
//				msg.append(" end "+zDateHandler.getTheTime_MS());
			}
			msg.delete(0, msg.length());
			msg.append(str1);
			msg.append(str2);
			msg.append(str3);
			po = tempSave(id, po, msg.toString());
			
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
	
	public boolean doRpt_TX(Map<String,String> params ,List<BANK_GROUP> optBank_List ,List<String> opt_type_List ){
//		TODO 使用rpttx_1_bo 的相關api來組出sql ，並呼叫IREPORT相關api(另外寫)
//		1.取得所有操作行   2.取得操作行所屬總行清單 依序執行且要分發動行、入帳行、扣款行角度
		boolean result = false;
//		eACH_txlist_S_20150408_01.pdf
		String zipName= "";
		Map<String, Object> rpt_param = new HashMap<String, Object>();
		rpt_param.put("V_PRINT_DATE",DateTimeUtils.convertDate(DateTimeUtils.INTERCONVERSION, zDateHandler.getTheDate(), "yyyy/MM/dd", "yyy/MM/dd"));
		rpt_param.put("V_PRINT_TIME",zDateHandler.getTheTime());
		rpt_param.put("V_TXDT",DateTimeUtils.convertDate(DateTimeUtils.convertDate(params.get("BIZDATE"),"yyyyMMdd","yyyyMMdd"),"yyyyMMdd","營業日期: yyy/MM/dd"));
		List<byte[]> dataList = new LinkedList<byte[]>();
		List<String> filenameList = new LinkedList<String>();
		byte[] byteZIP = null;
		String tmps1="";String tmps2="";String tmps3="";String tmps4="";
		try {
			for(BANK_GROUP opt_po :optBank_List){
				filenameList = new LinkedList<String>();
				dataList = new LinkedList<byte[]>();
//				eACH_txlistrp_{OPBKID}_{YYYYMMDD}_{清算階段}.zip
//				zipName = "eACH_txlist_"+params.get("BIZDATE")+"_"+params.get("CLEARINGPHASE")+"_"+opt_po.getBGBK_ID()+"_pdf.zip";
				zipName = "eACH_txlistrp_"+opt_po.getBGBK_ID()+"_"+params.get("BIZDATE")+"_"+params.get("CLEARINGPHASE")+".zip";
				rpt_param.put("V_OPT_BANK", opt_po.getBGBK_NAME());
//				交易明細報表
				Map map = doRpt_TX1(params, opt_po.getBGBK_ID(), opt_po.getBGBK_NAME(), opt_type_List);
				if(map.get("result").equals("TRUE")){
//					dataList = (List<byte[]>) map.get("dataList");
//					filenameList = (List<String>) map.get("filenameList");
					dataList.addAll((List<byte[]>) map.get("dataList"));
					filenameList.addAll((List<String>) map.get("filenameList")) ;
				}else{
					System.out.println("doRpt_TX1 fail...");
					logger.debug("doRpt_TX1 fail...");
					result =  false;
					break;
				}
//				未完成明細報表
				map.clear();
				map = doRpt_TX2(params, opt_po.getBGBK_ID(), opt_po.getBGBK_NAME(), opt_type_List);
				if(map.get("result").equals("TRUE")){
//					dataList = (List<byte[]>) map.get("dataList");
//					filenameList = (List<String>) map.get("filenameList");
					dataList.addAll((List<byte[]>) map.get("dataList"));
					filenameList.addAll((List<String>) map.get("filenameList")) ;
				}else{
					System.out.println("doRpt_TX2 fail...");
					logger.debug("doRpt_TX2 fail...");
					result =  false;
					break;
				}
//				未完成結果明細報表
				map.clear();
				map = doRpt_TX3(params, opt_po.getBGBK_ID(), opt_po.getBGBK_NAME(), opt_type_List);
				if(map.get("result").equals("TRUE")){
//					dataList = (List<byte[]>) map.get("dataList");
//					filenameList = (List<String>) map.get("filenameList");
					dataList.addAll((List<byte[]>) map.get("dataList"));
					filenameList.addAll((List<String>) map.get("filenameList")) ;
				}else{
					System.out.println("doRpt_TX3 fail...");
					logger.debug("doRpt_TX3 fail...");
					result =  false;
					break;
				}
				byteZIP = codeUtils.createZIP(dataList,filenameList,null);
				logger.debug("byteZIP>>"+byteZIP);
				codeUtils.putFileToPath(map.get("tmpFileDir")+"/"+zipName,byteZIP);
				for(String pathname :filenameList){
					System.out.println("delete pathname>>"+map.get("tmpFileDir")+pathname);
					logger.debug("delete pathname>>"+map.get("tmpFileDir")+pathname);
					File file = new File(map.get("tmpFileDir")+pathname);
//					java.nio.file.Files.deleteIfExists(file.toPath());
					if(!file.delete()){
						file.delete();
					}
//					codeUtils.delete(file);
				}
			}//optBank_List for end
			result = true ;
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = false;
			logger.debug("doRpt_TX.NumberFormatException>>"+e);
			return result;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = false;
			logger.debug("doRpt_TX.Exception>>"+e);
			return result;
		}
		System.out.println("doRpt_TX finsh...result="+result);
		logger.debug("doRpt_TX finsh...result="+result);
		
		return result;
	}
	
	/**
	 * 交易明細報表
	 * @param params
	 * @param opt_id
	 * @param opt_name
	 * @param opt_type_List
	 * @return
	 */
	public Map doRpt_TX1(Map<String,String> params ,String opt_id ,String opt_name,List<String> opt_type_List ){
		String sql= "";
//		eACH_txlist_S_20150408_01.pdf
		String outputFileName= "";
		String outputPath= "";
		List<BANK_GROUP> bgbkList = new LinkedList<BANK_GROUP>();
		Map<String, Object> rpt_param = new HashMap<String, Object>();
		rpt_param.put("V_PRINT_DATE",DateTimeUtils.convertDate(DateTimeUtils.INTERCONVERSION, zDateHandler.getTheDate(), "yyyy/MM/dd", "yyy/MM/dd"));
		rpt_param.put("V_PRINT_TIME",zDateHandler.getTheTime());
		rpt_param.put("V_TXDT",DateTimeUtils.convertDate(DateTimeUtils.convertDate(params.get("BIZDATE"),"yyyyMMdd","yyyyMMdd"),"yyyyMMdd","營業日期: yyy/MM/dd"));
		List<byte[]> dataList = new LinkedList<byte[]>();
		List<String> filenameList = new LinkedList<String>();
		Map filemaps = new HashMap();
		Map<String,String> retmap =null;
		String tmpdate = DateTimeUtils.convertDate(DateTimeUtils.INTERCONVERSION, params.get("BIZDATE"), "yyyyMMdd", "yyyyMMdd");
		String tmps1="";String tmps2="";String tmps3="";String tmps4="";
		String fileName = "tx_1";
		try {
				filenameList = new LinkedList<String>();
				dataList = new LinkedList<byte[]>();
				rpt_param.put("V_OPT_BANK", opt_name);
				for(String opt_type :opt_type_List){
					outputFileName= Arguments.getStringArg("BAT.FILE.NAME.TX1");
					rpt_param.put("V_OP_TYPE",rpttx_1_bo.getOpType_Name(Integer.valueOf(opt_type)));
					outputFileName = outputFileName.replace("#1", rpttx_1_bo.getOpType(Integer.valueOf(opt_type)));
					outputFileName = outputFileName.replace("#2", params.get("BIZDATE"));
					outputFileName = outputFileName.replace("#3", params.get("CLEARINGPHASE"));
//					outputPath = params.get("BIZDATE")+"/"+opt_id+"/";
					outputPath = params.get("BIZDATE")+"/"+opt_id+"/"+Arguments.getStringArg("BAT.LAST.PATH")+"/";
					Map map = null;
					if(3 == Integer.valueOf(opt_type)){
						fileName = "tx_6";
						map =	rpttx_1_bo.getConditionDataForWO(tmpdate, opt_id, "", params.get("CLEARINGPHASE"), Integer.valueOf(opt_type),"","","");
						sql = rpttx_1_bo.getSQLForWO(map.get("sqlPath").toString(),map.get("sqlPath2").toString() ,  rpttx_1_bo.getSPSql(Integer.valueOf(opt_type))  , rpttx_1_bo.getspSqlII(Integer.valueOf(opt_type)) , rpttx_1_bo.getOrderBySql(Integer.valueOf(opt_type)));
					}else{
						map =	rpttx_1_bo.getConditionData( tmpdate, opt_id, "", params.get("CLEARINGPHASE"), Integer.valueOf(opt_type));
						sql = rpttx_1_bo.getSQL(map.get("sqlPath").toString(), rpttx_1_bo.getSPSql(Integer.valueOf(opt_type)), rpttx_1_bo.getOrderBySql(Integer.valueOf(opt_type)));
						
					}
					
					
					tmps1 = zDateHandler.getTheTime_MS();
					List list = rponblocktab_Dao.getRptData(sql, (List<String>) map.get("values"));
					tmps2 = zDateHandler.getTheTime_MS();
					retmap  = rptutils.bat_export(RptUtils.COLLECTION, fileName, outputFileName,outputPath, rpt_param, null,list, 1);
					tmps3 = zDateHandler.getTheTime_MS();
					if(retmap.get("result").equals("TRUE")){
						filenameList.add(outputFileName+".pdf");
						dataList.add(getPdf2ByteArray(retmap.get("outputFilePath")));
						filemaps.put("result", "TRUE");
						filemaps.put("filenameList", filenameList);
						filemaps.put("dataList", dataList);
						filemaps.put("tmpFileDir", retmap.get("tmpFileDir"));
					}else{
						filemaps.put("result", "FALSE");
					}
				}//opt_type_List for end
				tmps4 = zDateHandler.getTheTime_MS();
//				System.out.println("query start time>>"+tmps1+",query end time>>"+tmps2+"cost>>"+zDateHandler.compareDiffTime(tmps1, tmps2));
//				System.out.println("rpt start time>>"+tmps2+",rpt end time>>"+tmps3+"cost>>"+zDateHandler.compareDiffTime(tmps2, tmps3));
//				System.out.println("zip start time>>"+tmps3+",zip end time>>"+tmps4+"cost>>"+zDateHandler.compareDiffTime(tmps3, tmps4));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			filemaps.put("result", "FALSE");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			filemaps.put("result", "FALSE");
		}
		
		return filemaps;
	}
	/**
	 * 未完成明細報表
	 * @param params
	 * @param opt_id
	 * @param opt_name
	 * @param opt_type_List
	 * @return
	 */
	public Map doRpt_TX2(Map<String,String> params ,String opt_id ,String opt_name,List<String> opt_type_List ){
		String sql= "";
//		eACH_pending_B_20150408_01.pdf
		String outputFileName= "";
		String outputPath= "";
		List<BANK_GROUP> bgbkList = new LinkedList<BANK_GROUP>();
		Map<String, Object> rpt_param = new HashMap<String, Object>();
		rpt_param.put("V_PRINT_DATE",DateTimeUtils.convertDate(DateTimeUtils.INTERCONVERSION, zDateHandler.getTheDate(), "yyyy/MM/dd", "yyy/MM/dd"));
		rpt_param.put("V_PRINT_TIME",zDateHandler.getTheTime());
		rpt_param.put("V_TXDT",DateTimeUtils.convertDate(DateTimeUtils.convertDate(params.get("BIZDATE"),"yyyyMMdd","yyyyMMdd"),"yyyyMMdd","營業日期: yyy/MM/dd"));
		List<byte[]> dataList = new LinkedList<byte[]>();
		List<String> filenameList = new LinkedList<String>();
		Map filemaps = new HashMap();
		Map<String,String> retmap =null;
		String tmpdate = DateTimeUtils.convertDate(DateTimeUtils.INTERCONVERSION, params.get("BIZDATE"), "yyyyMMdd", "yyyyMMdd");
		String tmps1="";String tmps2="";String tmps3="";String tmps4="";
		try {
			filenameList = new LinkedList<String>();
			dataList = new LinkedList<byte[]>();
			rpt_param.put("V_OPT_BANK", opt_name);
			for(String opt_type :opt_type_List){
				if("3".equals(opt_type)){
					continue;
				}
				outputFileName= Arguments.getStringArg("BAT.FILE.NAME.TX2");
				rpt_param.put("V_OP_TYPE",rpttx_1_bo.getOpType_Name(Integer.valueOf(opt_type)));
				outputFileName = outputFileName.replace("#1", rpttx_1_bo.getOpType(Integer.valueOf(opt_type)));
				outputFileName = outputFileName.replace("#2", params.get("BIZDATE"));
				outputFileName = outputFileName.replace("#3", params.get("CLEARINGPHASE"));
//				outputPath = params.get("BIZDATE")+"/"+opt_id+"/";
				outputPath = params.get("BIZDATE")+"/"+opt_id+"/"+Arguments.getStringArg("BAT.LAST.PATH")+"/";
				Map map = rpttx_2_bo.getConditionData( tmpdate, opt_id, "", params.get("CLEARINGPHASE"), Integer.valueOf(opt_type));
				sql = rpttx_2_bo.getSQL(map.get("sqlPath").toString(), rpttx_1_bo.getSPSql(Integer.valueOf(opt_type)), rpttx_1_bo.getOrderBySql(Integer.valueOf(opt_type)));
				tmps1 = zDateHandler.getTheTime_MS();
				List list = rponblocktab_Dao.getRptData(sql, (List<String>) map.get("values"));
				tmps2 = zDateHandler.getTheTime_MS();
				retmap  = rptutils.bat_export(RptUtils.COLLECTION, "tx_2", outputFileName,outputPath, rpt_param, null,list, 1);
				tmps3 = zDateHandler.getTheTime_MS();
				if(retmap.get("result").equals("TRUE")){
					filenameList.add(outputFileName+".pdf");
					dataList.add(getPdf2ByteArray(retmap.get("outputFilePath")));
					filemaps.put("result", "TRUE");
					filemaps.put("filenameList", filenameList);
					filemaps.put("dataList", dataList);
					filemaps.put("tmpFileDir", retmap.get("tmpFileDir"));
				}else{
					filemaps.put("result", "FALSE");
				}
			}//opt_type_List for end
			tmps4 = zDateHandler.getTheTime_MS();
//				System.out.println("query start time>>"+tmps1+",query end time>>"+tmps2+"cost>>"+zDateHandler.compareDiffTime(tmps1, tmps2));
//				System.out.println("rpt start time>>"+tmps2+",rpt end time>>"+tmps3+"cost>>"+zDateHandler.compareDiffTime(tmps2, tmps3));
//				System.out.println("zip start time>>"+tmps3+",zip end time>>"+tmps4+"cost>>"+zDateHandler.compareDiffTime(tmps3, tmps4));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			filemaps.put("result", "FALSE");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			filemaps.put("result", "FALSE");
		}
		
		return filemaps;
	}
	public Map doRpt_TX3(Map<String,String> params ,String opt_id ,String opt_name,List<String> opt_type_List ){
		String sql= "";
//		eACH_ pendresult_B_20150408_01.pdf
		String outputFileName= "";
		String outputPath= "";
		Map<String, Object> rpt_param = new HashMap<String, Object>();
		rpt_param.put("V_PRINT_DATE",DateTimeUtils.convertDate(DateTimeUtils.INTERCONVERSION, zDateHandler.getTheDate(), "yyyy/MM/dd", "yyy/MM/dd"));
		rpt_param.put("V_PRINT_TIME",zDateHandler.getTheTime());
		rpt_param.put("V_TXDT",DateTimeUtils.convertDate(DateTimeUtils.convertDate(params.get("BIZDATE"),"yyyyMMdd","yyyyMMdd"),"yyyyMMdd","營業日期: yyy/MM/dd"));
		List<byte[]> dataList = new LinkedList<byte[]>();
		List<String> filenameList = new LinkedList<String>();
		Map filemaps = new HashMap();
		Map<String,String> retmap =null;
		String tmpdate = DateTimeUtils.convertDate(DateTimeUtils.INTERCONVERSION, params.get("BIZDATE"), "yyyyMMdd", "yyyyMMdd");
		String tmps1="";String tmps2="";String tmps3="";String tmps4="";
		try {
			filenameList = new LinkedList<String>();
			dataList = new LinkedList<byte[]>();
			rpt_param.put("V_OPT_BANK", opt_name);
			for(String opt_type :opt_type_List){
				if("3".equals(opt_type)){
					continue;
				}
				outputFileName= Arguments.getStringArg("BAT.FILE.NAME.TX3");
				rpt_param.put("V_OP_TYPE",rpttx_1_bo.getOpType_Name(Integer.valueOf(opt_type)));
				outputFileName = outputFileName.replace("#1", rpttx_1_bo.getOpType(Integer.valueOf(opt_type)));
				outputFileName = outputFileName.replace("#2", params.get("BIZDATE"));
				outputFileName = outputFileName.replace("#3", params.get("CLEARINGPHASE"));
//				outputPath = params.get("BIZDATE")+"/"+opt_id+"/";
				outputPath = params.get("BIZDATE")+"/"+opt_id+"/"+Arguments.getStringArg("BAT.LAST.PATH")+"/";
				Map map = rpttx_3_bo.getConditionData( tmpdate, opt_id, "", params.get("CLEARINGPHASE"), Integer.valueOf(opt_type));
				sql = rpttx_3_bo.getSQL(map.get("sqlPath").toString(), rpttx_3_bo.getSPSql(Integer.valueOf(opt_type)), rpttx_3_bo.getOrderBySql(Integer.valueOf(opt_type)));
				tmps1 = zDateHandler.getTheTime_MS();
				List list = rponblocktab_Dao.getRptData(sql, (List<String>) map.get("values"));
				tmps2 = zDateHandler.getTheTime_MS();
				retmap  = rptutils.bat_export(RptUtils.COLLECTION, "tx_3", outputFileName,outputPath, rpt_param, null,list, 1);
				tmps3 = zDateHandler.getTheTime_MS();
				if(retmap.get("result").equals("TRUE")){
					filenameList.add(outputFileName+".pdf");
					dataList.add(getPdf2ByteArray(retmap.get("outputFilePath")));
					filemaps.put("result", "TRUE");
					filemaps.put("filenameList", filenameList);
					filemaps.put("dataList", dataList);
					filemaps.put("tmpFileDir", retmap.get("tmpFileDir"));
				}else{
					filemaps.put("result", "FALSE");
				}
			}//opt_type_List for end
			tmps4 = zDateHandler.getTheTime_MS();
//				System.out.println("query start time>>"+tmps1+",query end time>>"+tmps2+"cost>>"+zDateHandler.compareDiffTime(tmps1, tmps2));
//				System.out.println("rpt start time>>"+tmps2+",rpt end time>>"+tmps3+"cost>>"+zDateHandler.compareDiffTime(tmps2, tmps3));
//				System.out.println("zip start time>>"+tmps3+",zip end time>>"+tmps4+"cost>>"+zDateHandler.compareDiffTime(tmps3, tmps4));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			filemaps.put("result", "FALSE");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			filemaps.put("result", "FALSE");
		}
		
		return filemaps;
	}
	
	
	
	public boolean doRpt_CL1(Map<String,String> params ,List<BANK_GROUP> optBank_List ,List<String> opt_type_List ){
//		TODO 使用rpttx_1_bo 的相關api來組出sql ，並呼叫IREPORT相關api(另外寫)
//		1.取得所有操作行   2.取得操作行所屬總行清單 依序執行且要分發動行、入帳行、扣款行角度
		String sql= "";
		boolean result = false;
//		eACH_ ClsDaySum_YYYYMMDD_opBkId_clph.pdf
		String outputFileName= "";
		String outputPath= "";
		Map<String, Object> rpt_param = new HashMap<String, Object>();
		rpt_param.put("V_PRINT_DATE",DateTimeUtils.convertDate(DateTimeUtils.INTERCONVERSION, zDateHandler.getTheDate(), "yyyy/MM/dd", "yyy/MM/dd"));
		rpt_param.put("V_PRINT_TIME",zDateHandler.getTheTime());
		rpt_param.put("V_TXDT",DateTimeUtils.convertDate(DateTimeUtils.convertDate(params.get("BIZDATE"),"yyyyMMdd","yyyyMMdd"),"yyyyMMdd","營業日期: yyy/MM/dd"));
		Map<String,String> retmap =null;
		String tmps1="";String tmps2="";String tmps3="";
		int i = 0 ;
		String tmpdate = DateTimeUtils.convertDate(DateTimeUtils.INTERCONVERSION, params.get("BIZDATE"), "yyyyMMdd", "yyyyMMdd");
		try {
			for(BANK_GROUP opt_po :optBank_List){
//				if(i==3){break;}// TODO 測試用，要記的註解
//				if(opt_po.getBGBK_ID().equals("4510000")){
				rpt_param.put("V_OPT_BANK", opt_po.getBGBK_NAME());
				outputFileName= Arguments.getStringArg("BAT.FILE.NAME.CL1");
				outputFileName = outputFileName.replace("#1", opt_po.getBGBK_ID() );
				outputFileName = outputFileName.replace("#2", params.get("BIZDATE"));
				outputFileName = outputFileName.replace("#3", params.get("CLEARINGPHASE"));
				outputPath = params.get("BIZDATE")+"/"+opt_po.getBGBK_ID()+"/"+Arguments.getStringArg("BAT.LAST.PATH")+"/";
				Map map = rptcl_1_bo.getConditionData( tmpdate, "", params.get("CLEARINGPHASE"),opt_po.getBGBK_ID());
				sql = rptcl_1_bo.getSQL(map.get("sqlPath").toString());
				tmps1 = zDateHandler.getTheTime_MS();
				List list = rponblocktab_Dao.getTx_Detail_Data_ForRpt(sql, (List<String>) map.get("values"));
				tmps2 = zDateHandler.getTheTime_MS();
				retmap  = rptutils.bat_export(RptUtils.COLLECTION, "cl_1", outputFileName, outputPath,rpt_param, null,list, 1);
				tmps3 = zDateHandler.getTheTime_MS();
//				System.out.println("query start time>>"+tmps1+",query end time>>"+tmps2+"cost>>"+zDateHandler.compareDiffTime(tmps1, tmps2));
//				System.out.println("rpt start time>>"+tmps2+",rpt end time>>"+tmps3+"cost>>"+zDateHandler.compareDiffTime(tmps2, tmps3));
				if(retmap.get("result").equals("TRUE")){
					result = true;
				}else{
					result = false;
					logger.debug("doRpt_CL1 fail , OPT_BKID="+opt_po.getBGBK_ID());
					return result;
				}
//				}// TODO 測試用，要記的註解
				i++;
			}//optBank_List for end
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = false;
			logger.debug("doRpt_CL1.NumberFormatException>>"+e);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = false;
			logger.debug("doRpt_CL1.Exception>>"+e);
		}
		
		return result;
	}
	public boolean doRpt_FEE1(Map<String,String> params ,List<BANK_GROUP> optBank_List ,List<String> opt_type_List ){
//		TODO 使用rpttx_1_bo 的相關api來組出sql ，並呼叫IREPORT相關api(另外寫)
//		1.取得所有操作行   2.取得操作行所屬總行清單 依序執行且要分發動行、入帳行、扣款行角度
		String sql= "";
		boolean result = false;
//		 eACH_FeeDaySum_20150201_0040000.pdf 
		String outputFileName= "";
		String outputPath= "";
		Map<String, Object> rpt_param = new HashMap<String, Object>();
		rpt_param.put("V_PRINT_DATE",DateTimeUtils.convertDate(DateTimeUtils.INTERCONVERSION, zDateHandler.getTheDate(), "yyyy/MM/dd", "yyy/MM/dd"));
		rpt_param.put("V_PRINT_TIME",zDateHandler.getTheTime());
		rpt_param.put("V_TXDT",
				"營業日期：" + 
				DateTimeUtils.convertDate(DateTimeUtils.INTERCONVERSION,params.get("BIZDATE"),"yyyyMMdd","yyy/MM/dd") + " ~ " + 
				DateTimeUtils.convertDate(DateTimeUtils.INTERCONVERSION,params.get("BIZDATE"),"yyyyMMdd","yyy/MM/dd")
		);
//		因為呼叫的API是民國轉西元 故這邊要再多一次功
		String tmpdate = DateTimeUtils.convertDate(DateTimeUtils.INTERCONVERSION,params.get("BIZDATE"),"yyyyMMdd","yyyyMMdd");
		Map<String,String> retmap =null;
		String tmps1="";String tmps2="";String tmps3="";
		int i = 0 ;
		rpt_param.put("V_CLEARINGPHASE", "");
		try {
			for(BANK_GROUP opt_po :optBank_List){
//				if(i==3){break;}// TODO 測試用，要記的註解
//				if(opt_po.getBGBK_ID().equals("4510000")){
				rpt_param.put("V_OPT_BANK", opt_po.getBGBK_NAME());
//				20170802 edit by hugo opt_type_List V_OP_TYPE 在手續費裡邊應該用不到
//				for(String opt_type :opt_type_List){
//					rpt_param.put("V_OP_TYPE",rpttx_1_bo.getOpType_Name(Integer.valueOf(opt_type)));
					outputFileName= Arguments.getStringArg("BAT.FILE.NAME.FEE1");
					outputFileName = outputFileName.replace("#1", opt_po.getBGBK_ID() );
					outputFileName = outputFileName.replace("#2", params.get("BIZDATE"));
//					outputPath = params.get("BIZDATE")+"/"+opt_po.getBGBK_ID()+"/";
					outputFileName = outputFileName.replace("#3", params.get("CLEARINGPHASE"));
					outputPath = params.get("BIZDATE")+"/"+opt_po.getBGBK_ID()+"/"+Arguments.getStringArg("BAT.LAST.PATH")+"/";
					sql = rptfee_1_bo.getSQL(tmpdate,tmpdate,"",opt_po.getBGBK_ID(),"","");
					tmps1 = zDateHandler.getTheTime_MS();
					List list = rponblocktab_Dao.getRptData(sql, new ArrayList());
					tmps2 = zDateHandler.getTheTime_MS();
					retmap  = rptutils.bat_export(RptUtils.COLLECTION, "fee_1", outputFileName,outputPath, rpt_param, null,list, 1);
					tmps3 = zDateHandler.getTheTime_MS();
//				}//opt_type_List for end
				i++;
//				System.out.println("query start time>>"+tmps1+",query end time>>"+tmps2+"cost>>"+zDateHandler.compareDiffTime(tmps1, tmps2));
//				System.out.println("rpt start time>>"+tmps2+",rpt end time>>"+tmps3+"cost>>"+zDateHandler.compareDiffTime(tmps2, tmps3));
				if(retmap.get("result").equals("TRUE")){
					result = true;
				}else{
					result = false;
					logger.debug("doRpt_FEE1 fail , OPT_BKID="+opt_po.getBGBK_ID());
					return result;
				}
				}// TODO 測試用，要記的註解
//			}//optBank_List for end
			
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = false;
			logger.debug("doRpt_FEE1.NumberFormatException>>"+e);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = false;
			logger.debug("doRpt_FEE1.Exception>>"+e);
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
		}finally{
			if(in!=null){
				in.close();
			}
		}
		return res;
	}
	
	public RPTTX_1_BO getRpttx_1_bo() {
		return rpttx_1_bo;
	}

	public void setRpttx_1_bo(RPTTX_1_BO rpttx_1_bo) {
		this.rpttx_1_bo = rpttx_1_bo;
	}

	public BANK_GROUP_Dao getBank_group_Dao() {
		return bank_group_Dao;
	}

	public void setBank_group_Dao(BANK_GROUP_Dao bank_group_Dao) {
		this.bank_group_Dao = bank_group_Dao;
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

	public RPTCL_1_BO getRptcl_1_bo() {
		return rptcl_1_bo;
	}

	public void setRptcl_1_bo(RPTCL_1_BO rptcl_1_bo) {
		this.rptcl_1_bo = rptcl_1_bo;
	}

	public RPTFEE_1_BO getRptfee_1_bo() {
		return rptfee_1_bo;
	}

	public void setRptfee_1_bo(RPTFEE_1_BO rptfee_1_bo) {
		this.rptfee_1_bo = rptfee_1_bo;
	}

	public RPTTX_2_BO getRpttx_2_bo() {
		return rpttx_2_bo;
	}

	public void setRpttx_2_bo(RPTTX_2_BO rpttx_2_bo) {
		this.rpttx_2_bo = rpttx_2_bo;
	}

	public RPTTX_3_BO getRpttx_3_bo() {
		return rpttx_3_bo;
	}

	public void setRpttx_3_bo(RPTTX_3_BO rpttx_3_bo) {
		this.rpttx_3_bo = rpttx_3_bo;
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

	
	
	
	
}
