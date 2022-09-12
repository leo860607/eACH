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
import tw.org.twntch.db.dao.hibernate.RPONBLOCKTAB_Dao;
import tw.org.twntch.po.BANK_GROUP;
import tw.org.twntch.util.CodeUtils;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.RptUtils;
import tw.org.twntch.util.zDateHandler;

/**
 * 批次產生報表
 * @author Hugo
 *
 */
public class BAT_RPT_BO {
	private Logger logger = Logger.getLogger(BAT_RPT_BO.class.getName());
	private RPTTX_1_BO rpttx_1_bo ;
	private RPTTX_2_BO rpttx_2_bo ;
	private RPTTX_3_BO rpttx_3_bo ;
	private RPTCL_1_BO rptcl_1_bo ;
	private RPTFEE_1_BO rptfee_1_bo ;
	private BANK_GROUP_Dao bank_group_Dao ;
	private RPONBLOCKTAB_Dao rponblocktab_Dao ;
	private RptUtils rptutils ;
	private CodeUtils codeUtils;
	
	public Map do_Bat_Rpt(String bizDate , String clearingphase ,int batch_proc_seq){
//		TODO 執行相關批次報表，不管錯誤還是失敗都要回傳 ，msg="明細1成功，明細2失敗" 有一個失敗就算失敗result="FALSE"
		List<BANK_GROUP> optBank_List = bank_group_Dao.getBgbkIdList_2();
		//測試用，要記的註解
//		List<BANK_GROUP> optBank_List = bank_group_Dao.find(" FROM tw.org.twntch.po.BANK_GROUP WHERE BGBK_ID = '4510000' ");//測試用，要記的註解
		optBank_List = optBank_List == null ? new LinkedList():optBank_List;
		String opt_type = "0,1,2";
		int errorcount = 0;
		StringBuffer msg = new StringBuffer();
		List<String> opt_type_List = Arrays.asList(opt_type.split(",")); 
		Map<String,String> params = new HashMap<String,String>();
		Map<String,String> retMap = new HashMap<String,String>();
		params.put("BIZDATE", bizDate);
		params.put("CLEARINGPHASE", clearingphase);
		String tmp1 = "";
//		TODO 先註解以利測試 之後要打開
		if(clearingphase.equals("02")){
			tmp1= zDateHandler.getTheTime_MS();
			if( !doRpt_CL1(params, optBank_List, opt_type_List)){
				errorcount++;
				msg.append(" 結算日統計報表檔產生失敗  ");
				msg.append(" start "+tmp1);
				msg.append(" end "+zDateHandler.getTheTime_MS());
			}else{
				msg.append(" 結算日統計報表檔產生成功  ");
				msg.append(" start "+tmp1);
				msg.append(" end "+zDateHandler.getTheTime_MS());
			}
			tmp1= zDateHandler.getTheTime_MS();
			if( !doRpt_FEE1(params, optBank_List, opt_type_List)){
				errorcount++;
				msg.append(" , 收費(手續費)日統計報表檔產生失敗  ");
				msg.append(" start "+tmp1);
				msg.append(" end "+zDateHandler.getTheTime_MS());
			}else{
				msg.append(" , 收費(手續費)日統計報表檔產生成功  ");
				msg.append(" start "+tmp1);
				msg.append(" end "+zDateHandler.getTheTime_MS());
			}
		}
		tmp1= zDateHandler.getTheTime_MS();
		if( !doRpt_TX(params, optBank_List, opt_type_List)){
			errorcount++;
			msg.append(" ; 交易明細資料報表檔產生失敗  ");
			msg.append(" start "+tmp1);
			msg.append(" end "+zDateHandler.getTheTime_MS());
		}else{
			msg.append(" ; 交易明細資料報表檔產生成功  ");
			msg.append(" start "+tmp1);
			msg.append(" end "+zDateHandler.getTheTime_MS());
		}
		
		
		if(errorcount>0){
			retMap.put("result", "FALSE");
			retMap.put("msg", msg.toString());
		}else{
			retMap.put("result", "TRUE");
			retMap.put("msg", msg.toString());
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
				zipName = "eACH_txlist_"+params.get("BIZDATE")+"_"+params.get("CLEARINGPHASE")+"_"+opt_po.getBGBK_ID()+"_pdf.zip";
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
					return false;
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
					return false;
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
					return false;
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
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = false;
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
//		eACH_txlist_20150408_01_0040000.zip
		String zipName= "";
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
				zipName = "eACH_txlist_"+params.get("BIZDATE")+"_"+params.get("CLEARINGPHASE")+"_"+opt_id+"_pdf.zip";
				rpt_param.put("V_OPT_BANK", opt_name);
				for(String opt_type :opt_type_List){
					outputFileName= Arguments.getStringArg("BAT.FILE.NAME.TX1");
					rpt_param.put("V_OP_TYPE",rpttx_1_bo.getOpType_Name(Integer.valueOf(opt_type)));
					outputFileName = outputFileName.replace("#1", rpttx_1_bo.getOpType(Integer.valueOf(opt_type)));
					outputFileName = outputFileName.replace("#2", params.get("BIZDATE"));
					outputFileName = outputFileName.replace("#3", params.get("CLEARINGPHASE"));
					outputPath = params.get("BIZDATE")+"/"+opt_id+"/";
//					Map map = rpttx_1_bo.getConditionData( tmpdate, opt_id, "", params.get("CLEARINGPHASE"), Integer.valueOf(opt_type));
//					sql = rpttx_1_bo.getSQL(map.get("sqlPath").toString(), rpttx_1_bo.getSPSql(Integer.valueOf(opt_type)), rpttx_1_bo.getOrderBySql(Integer.valueOf(opt_type)));
					Map map = rpttx_1_bo.getConditionDataForBat( tmpdate, opt_id, "", params.get("CLEARINGPHASE"), Integer.valueOf(opt_type),"","","");
					sql = rpttx_1_bo.getSQLForBat(map.get("sqlPath").toString(),map.get("sqlPath2").toString(), rpttx_1_bo.getSPSql(Integer.valueOf(opt_type)),rpttx_1_bo.getspSqlII(Integer.valueOf(opt_type)) ,rpttx_1_bo.getOrderBySql(Integer.valueOf(opt_type)));
					tmps1 = zDateHandler.getTheTime_MS();
					List list = rponblocktab_Dao.getRptData(sql, (List<String>) map.get("values"));
					tmps2 = zDateHandler.getTheTime_MS();
					retmap  = rptutils.bat_export(RptUtils.COLLECTION, "tx_1", outputFileName,outputPath, rpt_param, null,list, 1);
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
				outputFileName= Arguments.getStringArg("BAT.FILE.NAME.TX2");
				rpt_param.put("V_OP_TYPE",rpttx_1_bo.getOpType_Name(Integer.valueOf(opt_type)));
				outputFileName = outputFileName.replace("#1", rpttx_1_bo.getOpType(Integer.valueOf(opt_type)));
				outputFileName = outputFileName.replace("#2", params.get("BIZDATE"));
				outputFileName = outputFileName.replace("#3", params.get("CLEARINGPHASE"));
				outputPath = params.get("BIZDATE")+"/"+opt_id+"/";
//				Map map = rpttx_2_bo.getConditionData( tmpdate, opt_id, "", params.get("CLEARINGPHASE"), Integer.valueOf(opt_type));
//				sql = rpttx_2_bo.getSQL(map.get("sqlPath").toString(), rpttx_1_bo.getSPSql(Integer.valueOf(opt_type)), rpttx_1_bo.getOrderBySql(Integer.valueOf(opt_type)));
				Map map = rpttx_2_bo.getConditionDataForBat( tmpdate, opt_id, "", params.get("CLEARINGPHASE"), Integer.valueOf(opt_type));
				sql = rpttx_2_bo.getSQLForBat(map.get("sqlPath").toString(),map.get("sqlPath2").toString(), rpttx_1_bo.getSPSql(Integer.valueOf(opt_type)), rpttx_1_bo.getOrderBySql(Integer.valueOf(opt_type)));
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
				outputFileName= Arguments.getStringArg("BAT.FILE.NAME.TX3");
				rpt_param.put("V_OP_TYPE",rpttx_1_bo.getOpType_Name(Integer.valueOf(opt_type)));
				outputFileName = outputFileName.replace("#1", rpttx_1_bo.getOpType(Integer.valueOf(opt_type)));
				outputFileName = outputFileName.replace("#2", params.get("BIZDATE"));
				outputFileName = outputFileName.replace("#3", params.get("CLEARINGPHASE"));
				outputPath = params.get("BIZDATE")+"/"+opt_id+"/";
//				Map map = rpttx_3_bo.getConditionData( tmpdate, opt_id, "", params.get("CLEARINGPHASE"), Integer.valueOf(opt_type));
//				sql = rpttx_3_bo.getSQL(map.get("sqlPath").toString(), rpttx_1_bo.getSPSql(Integer.valueOf(opt_type)), rpttx_1_bo.getOrderBySql(Integer.valueOf(opt_type)));
				Map map = rpttx_3_bo.getConditionDataForBat( tmpdate, opt_id, "", params.get("CLEARINGPHASE"), Integer.valueOf(opt_type));
				sql = rpttx_3_bo.getSQLForBat(map.get("sqlPath").toString(), map.get("sqlPath2").toString(), rpttx_1_bo.getSPSql(Integer.valueOf(opt_type)), rpttx_1_bo.getOrderBySql(Integer.valueOf(opt_type)));
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
//		eACH_ ClsDaySum_YYYYMMDD_opBkId.pdf
		String outputFileName= "";
		String outputPath= "";
		StringBuffer errorMsg = new StringBuffer();
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
				outputFileName = outputFileName.replace("#1", params.get("BIZDATE"));
				outputFileName = outputFileName.replace("#2", opt_po.getBGBK_ID());
				outputPath = params.get("BIZDATE")+"/"+opt_po.getBGBK_ID()+"/";
				Map map = rptcl_1_bo.getConditionData( tmpdate, "", "",opt_po.getBGBK_ID());
				sql = rptcl_1_bo.getSQL(map.get("sqlPath").toString());
				tmps1 = zDateHandler.getTheTime_MS();
				List list = rponblocktab_Dao.getTx_Detail_Data_ForRpt(sql, (List<String>) map.get("values"));
				tmps2 = zDateHandler.getTheTime_MS();
				retmap  = rptutils.bat_export(RptUtils.COLLECTION, "cl_1", outputFileName, outputPath,rpt_param, null,list, 1);
				tmps3 = zDateHandler.getTheTime_MS();
				System.out.println("query start time>>"+tmps1+",query end time>>"+tmps2+"cost>>"+zDateHandler.compareDiffTime(tmps1, tmps2));
				System.out.println("rpt start time>>"+tmps2+",rpt end time>>"+tmps3+"cost>>"+zDateHandler.compareDiffTime(tmps2, tmps3));
				if(retmap.get("result").equals("TRUE")){
					result = true;
				}else{
					result = false;
					System.out.println("doRpt_CL1 fail , OPT_BKID="+opt_po.getBGBK_ID());
					return result;
				}
//				}// TODO 測試用，要記的註解
				i++;
			}//optBank_List for end
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = false;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = false;
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
				for(String opt_type :opt_type_List){
					outputFileName= Arguments.getStringArg("BAT.FILE.NAME.FEE1");
					rpt_param.put("V_OP_TYPE",rpttx_1_bo.getOpType_Name(Integer.valueOf(opt_type)));
					outputFileName = outputFileName.replace("#1", params.get("BIZDATE"));
					outputFileName = outputFileName.replace("#2", opt_po.getBGBK_ID());
					outputPath = params.get("BIZDATE")+"/"+opt_po.getBGBK_ID()+"/";
					sql = rptfee_1_bo.getSQL(tmpdate,tmpdate,"",opt_po.getBGBK_ID(),"","");
					tmps1 = zDateHandler.getTheTime_MS();
					List list = rponblocktab_Dao.getRptData(sql, new ArrayList());
					tmps2 = zDateHandler.getTheTime_MS();
					retmap  = rptutils.bat_export(RptUtils.COLLECTION, "fee_1", outputFileName,outputPath, rpt_param, null,list, 1);
					tmps3 = zDateHandler.getTheTime_MS();
				}//opt_type_List for end
				i++;
				System.out.println("query start time>>"+tmps1+",query end time>>"+tmps2+"cost>>"+zDateHandler.compareDiffTime(tmps1, tmps2));
				System.out.println("rpt start time>>"+tmps2+",rpt end time>>"+tmps3+"cost>>"+zDateHandler.compareDiffTime(tmps2, tmps3));
				if(retmap.get("result").equals("TRUE")){
					result = true;
				}else{
					result = false;
					System.out.println("doRpt_FEE1 fail , OPT_BKID="+opt_po.getBGBK_ID());
					return result;
				}
				}// TODO 測試用，要記的註解
//			}//optBank_List for end
			
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = false;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = false;
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
	
	
	
	
}
