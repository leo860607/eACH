package tw.org.twntch.bo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.apache.struts.util.LabelValueBean;

import tw.org.twntch.bean.UNDONE_TXDATA;
import tw.org.twntch.db.dao.hibernate.BANK_GROUP_Dao;
import tw.org.twntch.db.dao.hibernate.CommonSpringDao;
import tw.org.twntch.db.dao.hibernate.ONBLOCKTAB_Dao;
import tw.org.twntch.db.dao.hibernate.ONPENDINGTAB_Dao;
import tw.org.twntch.db.dao.hibernate.Page;
import tw.org.twntch.db.dao.hibernate.VW_ONBLOCKTAB_Dao;
import tw.org.twntch.po.BANK_GROUP;
import tw.org.twntch.po.ONPENDINGTAB;
import tw.org.twntch.po.ONPENDINGTAB_PK;
import tw.org.twntch.socket.Message;
import tw.org.twntch.socket.MessageConverter;
import tw.org.twntch.socket.SocketClient;
import tw.org.twntch.socket.Message.Body;
import tw.org.twntch.socket.Message.Header;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.zDateHandler;

public class UNDONE_TXDATA_BO {
	private ONBLOCKTAB_Dao onblocktab_Dao;
	private BANK_GROUP_Dao bank_group_Dao;
	private VW_ONBLOCKTAB_Dao vw_onblocktab_Dao;
	private CommonSpringDao commonSpringDao;
	private SocketClient socketClient;
	private ONPENDINGTAB_Dao onpendingtab_Dao ;
	
	public String send(Map<String, String> param){
		/* 未完成處理結果 沖正作業電文格式
		<?xml version="1.0" encoding="UTF-8" standalone="yes"?> 
			<msg> 
			    <header> 
			        <SystemHeader>eACH01</SystemHeader> 
			        <MsgType>0100</MsgType> 
			        <PrsCode>1403</PrsCode> 
			        <Stan>XXXXXXX</Stan> 
			        <InBank>0000000</InBank> 
			        <OutBank>9990000</OutBank> 
			        <DateTime>YYYYMMDDHHMMSS</DateTime> 
			        <RspCode>0000</RspCode> 
			    </header> 
			    <body> 
			         <OTxDate></OTxDate> 
			         <OSTAN></OSTAN> 
			         <ResultCode></ResultCode> 
			    </body> 
			</msg> 
			
			ResultCode -  00 成功 
			           -  01 失敗(沖正) 

		*/
		String json = "{}";
		String stan = StrUtils.isNotEmpty(param.get("STAN"))?param.get("STAN"):"" ;
		String txdate = StrUtils.isNotEmpty(param.get("TXDATE"))?param.get("TXDATE"):"" ;
		String type = StrUtils.isNotEmpty(param.get("TYPE"))?param.get("TYPE"):"" ;
		txdate = DateTimeUtils.convertDate(2, txdate, "yyyy-MM-dd", "yyyyMMdd");
		String resultCode = "";
		Map rtnMap = new HashMap();
		try {
			//先檢查onpendingtab中是否有該筆資料存在
			ONPENDINGTAB_PK id = new ONPENDINGTAB_PK(txdate, stan);
			ONPENDINGTAB po = onpendingtab_Dao.get(id);
			if(po == null){
				rtnMap.put("result", "FALSE");
				rtnMap.put("msg", "失敗，資料尚未轉移，PK={STAN:"+stan+",TXDATE:"+txdate+"}");
			}else{
//				20150529 add by hugo req by UAT-20150526-06
				if(po.getBIZDATE() !=null){//表示已有處理結果
					rtnMap.put("result", "FALSE");
					rtnMap.put("msg", "已有未完成交易處理結果，營業日="+po.getBIZDATE());
					json = JSONUtils.map2json(rtnMap);
					return json;
				}
				
//				判斷type來選擇電文種類 普鴻尚未完成
				if(type.equals("S")){
					resultCode = "00";
				}
				if(type.equals("F")){
					resultCode = "01";
				}
				Header msgHeader = new Header();
				msgHeader.setSystemHeader("eACH01");
				msgHeader.setMsgType("0100");
				msgHeader.setPrsCode("1403");
				msgHeader.setStan("");//此案例未使用
				msgHeader.setInBank("");//此案例未使用
				msgHeader.setOutBank("9990000");	//20150109 FANNY說 票交發動的電文，「OUTBANK」必須固定為「9990000」
				msgHeader.setDateTime(zDateHandler.getDateNum()+zDateHandler.getTheTime().replaceAll(":", ""));
				msgHeader.setRspCode("0000");
				Message msg = new Message();
				msg.setHeader(msgHeader);
				Body body = new Body();
				body.setOSTAN(stan);
				body.setOTxDate(txdate);
				body.setResultCode(resultCode);
				msg.setBody(body);
				String telegram = MessageConverter.marshalling(msg);
				rtnMap = socketClient.send(telegram);
				
				//過10秒後再查詢，檢查是否已沖正完畢
				Thread.sleep(5*1000);
				po = onpendingtab_Dao.get(id);
				if(po != null){
					if(po.getACHFLAG().equals("*")){
						rtnMap.put("result", "TRUE");
						rtnMap.put("msg", "成功，已完成作業");
					}else{
						rtnMap.put("result", "FALSE");
						rtnMap.put("msg", "失敗，未完成作業");
					}
				}else{
					rtnMap.put("result", "FALSE");
					rtnMap.put("msg", "失敗，無此資料，PK={STAN:"+stan+",TXDATE:"+txdate+"}");
				}
			}
		} catch ( JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rtnMap.put("result", "FALSE");
			rtnMap.put("msg", "失敗，電文發送異常");
		}catch(Exception ee){
			ee.printStackTrace();
			rtnMap.put("result", "FALSE");
			rtnMap.put("msg", "失敗，系統異常");
		}
		
		json = JSONUtils.map2json(rtnMap);
		return json;
	}
	
	
	
	public String send_1406(Map<String, String> param){
		/* 查詢未完成交易處理結果 
			<?xml version="1.0" encoding="UTF-8" standalone="yes"?> 
			<msg> 
			    <header> 
			        <SystemHeader>eACH01</SystemHeader> 
			        <MsgType>0100</MsgType> 
			        <PrsCode>1406</PrsCode> 
			        <Stan>XXXXXXX</Stan> 
			        <InBank>0000000</InBank> 
			        <OutBank>9990000</OutBank> 
			        <DateTime>YYYYMMDDHHMMSS</DateTime> 
			        <RspCode>0000</RspCode> 
			    </header> 
			    <body> 
			         <OTxDate></OTXDate> 
			         <OSTAN></OSTAN> 
			    </body> 
			</msg> 

		 */
		String json = "{}";
		String stan = StrUtils.isNotEmpty(param.get("STAN"))?param.get("STAN"):"" ;
		String txdate = StrUtils.isNotEmpty(param.get("TXDATE"))?param.get("TXDATE"):"" ;
		txdate = DateTimeUtils.convertDate(2, txdate, "yyyy-MM-dd", "yyyyMMdd");
		String resultCode = "";
		Map rtnMap = new HashMap();
		try {
			//先檢查onpendingtab中是否有該筆資料存在
			ONPENDINGTAB_PK id = new ONPENDINGTAB_PK(txdate, stan);
			ONPENDINGTAB po = onpendingtab_Dao.get(id);
			if(po == null){
				rtnMap.put("result", "FALSE");
				rtnMap.put("msg", "失敗，資料尚未轉移，PK={STAN:"+stan+",TXDATE:"+txdate+"}");
			}else{
//				20150529 add by hugo req by UAT-20150526-06
				if(po.getBIZDATE() !=null){//表示已有處理結果
					rtnMap.put("result", "FALSE");
					rtnMap.put("msg", "已有未完成交易處理結果，營業日="+po.getBIZDATE());
					json = JSONUtils.map2json(rtnMap);
					return json;
				}
				Header msgHeader = new Header();
				msgHeader.setSystemHeader("eACH01");
				msgHeader.setMsgType("0100");
				msgHeader.setPrsCode("1406");
				msgHeader.setStan("");//此案例未使用
				msgHeader.setInBank("0000000");
				msgHeader.setOutBank("9990000");	//20150109 FANNY說 票交發動的電文，「OUTBANK」必須固定為「9990000」
				msgHeader.setDateTime(zDateHandler.getDateNum()+zDateHandler.getTheTime().replaceAll(":", ""));
				msgHeader.setRspCode("0000");
				Message msg = new Message();
				msg.setHeader(msgHeader);
				Body body = new Body();
				body.setOSTAN(stan);
				body.setOTxDate(txdate);
//				body.setResultCode(resultCode);
				msg.setBody(body);
				String telegram = MessageConverter.marshalling(msg);
				rtnMap = socketClient.send(telegram);
			}
		} catch ( JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rtnMap.put("result", "FALSE");
			rtnMap.put("msg", "失敗，電文發送異常");
		}catch(Exception ee){
			ee.printStackTrace();
			rtnMap.put("result", "FALSE");
			rtnMap.put("msg", "失敗，系統異常");
		}
		
		json = JSONUtils.map2json(rtnMap);
		return json;
	}
	
	
	
	
	
	public String send_1400(Map<String, String> param){
		/* 請求傳送確認訊息			
<?xml version="1.0" encoding="UTF-8" standalone="yes"?> 
<msg> 
    <header> 
        <SystemHeader>eACH01</SystemHeader> 
        <MsgType>0100</MsgType> 
        <PrsCode>1400</PrsCode> 
        <Stan>XXXXXXX</Stan> 
        <InBank>0000000</InBank> 
        <OutBank>9990000</OutBank> 
        <DateTime>YYYYMMDDHHMMSS</DateTime> 
        <RspCode>0000</RspCode> 
    </header> 
    <body> 
         <OTxDate></OTxDate> 
         <OSTAN></OSTAN> 
    </body> 
</msg> 



		 */
		String json = "{}";
		String stan = StrUtils.isNotEmpty(param.get("STAN"))?param.get("STAN"):"" ;
		String txdate = StrUtils.isNotEmpty(param.get("TXDATE"))?param.get("TXDATE"):"" ;
		txdate = DateTimeUtils.convertDate(2, txdate, "yyyy-MM-dd", "yyyyMMdd");
		String resultCode = "";
		Map rtnMap = new HashMap();
		try {
			//先檢查onpendingtab中是否有該筆資料存在
			ONPENDINGTAB_PK id = new ONPENDINGTAB_PK(txdate, stan);
			ONPENDINGTAB po = onpendingtab_Dao.get(id);
			if(po != null){
				rtnMap.put("result", "FALSE");
				rtnMap.put("msg", "失敗，資料已轉移，PK={STAN:"+stan+",TXDATE:"+txdate+"}");
			}else{
				Header msgHeader = new Header();
				msgHeader.setSystemHeader("eACH01");
				msgHeader.setMsgType("0100");
				msgHeader.setPrsCode("1400");
				msgHeader.setStan("");//此案例未使用
				msgHeader.setInBank("0000000");
				msgHeader.setOutBank("9990000");	//20150109 FANNY說 票交發動的電文，「OUTBANK」必須固定為「9990000」
				msgHeader.setDateTime(zDateHandler.getDateNum()+zDateHandler.getTheTime().replaceAll(":", ""));
				msgHeader.setRspCode("0000");
				Message msg = new Message();
				msg.setHeader(msgHeader);
				Body body = new Body();
				body.setOSTAN(stan);
				body.setOTxDate(txdate);
//				body.setResultCode(resultCode);
				msg.setBody(body);
				String telegram = MessageConverter.marshalling(msg);
				rtnMap = socketClient.send(telegram);
			}
		} catch ( JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rtnMap.put("result", "FALSE");
			rtnMap.put("msg", "失敗，電文發送異常");
		}catch(Exception ee){
			ee.printStackTrace();
			rtnMap.put("result", "FALSE");
			rtnMap.put("msg", "失敗，系統異常");
		}
		
		json = JSONUtils.map2json(rtnMap);
		return json;
	}
	
	/**
	 * 取得操作行代號清單
	 * select distinct OPBK_ID where Currentdate between 啟用日期 and 停用日期
	 * @return
	 */
	public List<LabelValueBean> getOpbkIdList(){
		List<BANK_GROUP> list = bank_group_Dao.getBgbkIdList_2();
		List<LabelValueBean> beanList = new LinkedList<LabelValueBean>();
		LabelValueBean bean = null;
		for(BANK_GROUP po : list){
			bean = new LabelValueBean(po.getBGBK_ID() + " - " + po.getBGBK_NAME(), po.getBGBK_ID());
			beanList.add(bean);
		}
		System.out.println("beanList>>"+beanList);
		return beanList;
	}
	
	public String pageSearch(Map<String, String> param){
		List<String> conditions_1 = new ArrayList<String>();
		List<String> conditions_2 = new ArrayList<String>();
		//是否包含整批資料("N"表示不過濾)
		String filter_bat = param.get("FILTER_BAT")==null?"N":"Y";
		
		/* 20150210 HUANGPU 改以營業日(BIZDATE)查詢資料，非交易日期(TXDATE) */
		String startDate = "";
		if(StrUtils.isNotEmpty(param.get("START_DATE").trim())){
			startDate = param.get("START_DATE").trim();
			conditions_1.add(" BIZDATE >= '" + DateTimeUtils.convertDate(startDate, "yyyyMMdd", "yyyyMMdd") + "' ");
		}
				
		String endDate = "";
		if(StrUtils.isNotEmpty(param.get("END_DATE").trim())){
			endDate = param.get("END_DATE").trim();
			conditions_1.add(" BIZDATE <= '" + DateTimeUtils.convertDate(endDate, "yyyyMMdd", "yyyyMMdd") + "' ");
		}
		
		String clearingphase = "";
		if(StrUtils.isNotEmpty(param.get("CLEARINGPHASE").trim()) && !param.get("CLEARINGPHASE").trim().equals("all")){
			clearingphase = param.get("CLEARINGPHASE").trim();
			conditions_1.add(" CLEARINGPHASE = '" + clearingphase + "' ");
		}
		
		
		
		String bgbkId = "";
		if(StrUtils.isNotEmpty(param.get("BGBK_ID").trim()) && !param.get("BGBK_ID").trim().equals("all")){
			bgbkId = param.get("BGBK_ID").trim();
			conditions_1.add(" (SENDERHEAD = '" + bgbkId + "' OR OUTHEAD = '" + bgbkId + "' OR INHEAD = '" + bgbkId + "') ");
		}
		
		String businessTypeId = "";
		if(StrUtils.isNotEmpty(param.get("BUSINESS_TYPE_ID").trim()) && !param.get("BUSINESS_TYPE_ID").trim().equals("all")){
			businessTypeId = param.get("BUSINESS_TYPE_ID").trim();
			conditions_1.add(" BUSINESS_TYPE_ID = '" + businessTypeId + "' ");
		}
		
		String ostan = "";
		if(StrUtils.isNotEmpty(param.get("OSTAN")) && !param.get("OSTAN").equals("all")){
			ostan = param.get("OSTAN");
			conditions_1.add(" STAN = '" + ostan + "' ");
		}
		
		if(StrUtils.isNotEmpty(param.get("RESULTCODE")) && !param.get("RESULTCODE").equals("all")){
			if("A".equals(param.get("RESULTCODE"))){
				conditions_1.add(" RESULTCODE IS NOT NULL ");
			}else if("P".equals(param.get("RESULTCODE"))){
				conditions_1.add(" RESULTCODE IS NULL ");
			}
		}
		
		String opbkId = "";
		if(StrUtils.isNotEmpty(param.get("OPBK_ID").trim()) && !param.get("OPBK_ID").trim().equals("all")){
			opbkId = param.get("OPBK_ID").trim();
			if(filter_bat.equals("Y")){
				conditions_2.addAll(conditions_1);
				conditions_2.add(" COALESCE(FLBIZDATE,'') <> ''  ");
				conditions_2.add(" ((INACQUIRE = '" + opbkId + "' AND substr(COALESCE(PCODE,''),4) = '2' ) OR (OUTACQUIRE = '" + opbkId + "' AND substr(COALESCE(PCODE,''),4) = '1')) ");
				conditions_1.add(" COALESCE(FLBIZDATE,'') = ''  ");
				conditions_1.add(" (SENDERACQUIRE = '" + opbkId + "' OR OUTACQUIRE = '" + opbkId + "' OR INACQUIRE = '" + opbkId + "') ");
			}else{
				conditions_1.add(" (SENDERACQUIRE = '" + opbkId + "' OR OUTACQUIRE = '" + opbkId + "' OR INACQUIRE = '" + opbkId + "') ");
			}
		}else{
			if(filter_bat.equals("Y")){
				conditions_2.addAll(conditions_1);
				conditions_2.add(" COALESCE(FLBIZDATE,'') <> ''  ");
				conditions_1.add(" COALESCE(FLBIZDATE,'') = ''  ");
			}
		}
		
		
		
		int pageNo = StrUtils.isEmpty(param.get("page")) ?0:Integer.valueOf(param.get("page"));
		int pageSize = StrUtils.isEmpty(param.get("rows")) ?Integer.valueOf(Arguments.getStringArg("PAGE.SIZE")):Integer.valueOf(param.get("rows"));
		
		Map rtnMap = new HashMap();
		
		List<UNDONE_TXDATA> list = null;
		Page page = null;
		try {
			list = new ArrayList<UNDONE_TXDATA>();
			String condition_1 = "",condition_2 = "" ;
			condition_1 = combine(conditions_1);
			condition_2 = combine(conditions_2);
			String sord =StrUtils.isNotEmpty(param.get("sord"))? param.get("sord"):"";
			String sidx =StrUtils.isNotEmpty(param.get("sidx"))? param.get("sidx"):"";
			String orderSQL = StrUtils.isNotEmpty(sidx)? " ORDER BY "+sidx+" "+sord:"";
			StringBuffer tmpSQL = new StringBuffer();
			StringBuffer cntSQL = new StringBuffer();
			StringBuffer sumSQL = new StringBuffer();
			StringBuffer sql= new StringBuffer();
			if(StrUtils.isNotEmpty(param.get("sidx"))){
//				if("TXDT".equalsIgnoreCase(param.get("sidx"))){
//					orderSQL = " ORDER BY NEWTXDT "+param.get("sord");
//				}
//				if("TXAMT".equalsIgnoreCase(param.get("sidx"))){
//					orderSQL = " ORDER BY NEWTXAMT "+param.get("sord");
//				}
				if("RESULTCODE".equalsIgnoreCase(param.get("sidx"))){
					orderSQL = " ORDER BY NEWRESULT "+param.get("sord");
				}
			}
			tmpSQL.append(" WITH TEMP AS ");
			tmpSQL.append(" ( ");
			tmpSQL.append(" SELECT   COALESCE(NEWTXDT ,'') AS TXDT, TXDATE, BIZDATE, STAN, OUTACCTNO, INACCTNO, COALESCE(NEWTXAMT,0) AS TXAMT , NEWRESULT ,SENDERACQUIRE, SENDERHEAD ");
			
			tmpSQL.append("  , SENDERID, EACH_TXN_NAME ");
			tmpSQL.append(" , COALESCE(TXID,'') ||'-'|| COALESCE((SELECT TC.TXN_NAME FROM TXN_CODE TC WHERE TC.TXN_ID = TXID ),'') AS TXID ");
			tmpSQL.append(" , COALESCE(SENDERBANKID,'') ||'-'|| COALESCE((SELECT BRBK_NAME FROM BANK_BRANCH WHERE BRBK_ID=SENDERBANKID),'') SENDERBANKID "); 
			tmpSQL.append(" , COALESCE(OUTBANKID,'') ||'-'|| COALESCE((SELECT BRBK_NAME FROM BANK_BRANCH WHERE BRBK_ID=OUTBANKID),'') OUTBANKID "); 
			tmpSQL.append(" , COALESCE(INBANKID,'') ||'-'|| COALESCE((SELECT BRBK_NAME FROM BANK_BRANCH WHERE BRBK_ID=INBANKID),'') INBANKID ");
			tmpSQL.append(" , COALESCE(PCODE,'') || '-' || COALESCE(EACH_TXN_NAME,'') AS PCODE ");
//			20151127 add by hugo req by UAT-20151126-01
			tmpSQL.append(" , BUSINESS_TYPE_ID ");
			tmpSQL.append(" FROM VW_ONBLOCKTAB ");
			tmpSQL.append(" LEFT JOIN (  SELECT EACH_TXN_ID, EACH_TXN_NAME FROM EACH_TXN_CODE  ) ON PCODE = EACH_TXN_ID   ");
			tmpSQL.append(" LEFT JOIN (  SELECT OTXDate, OSTAN, RESULTCODE FROM ONPENDINGTAB  ) ON TXDATE = OTXDate AND STAN = OSTAN   ");
			//20150302 BY 李建利(與陳淑華開會討論)：只要是曾經是未完成交易(不包含處理中)，無論是否有回應結果，都可在此功能中查詢出來
			tmpSQL.append(" WHERE RESULTSTATUS = 'P' AND SENDERSTATUS <> '1' ");
			tmpSQL.append((StrUtils.isEmpty(condition_1)?"" : "AND " + condition_1));
			if(filter_bat.equals("Y")){
				tmpSQL.append(" UNION ALL ");
				tmpSQL.append(" SELECT   COALESCE(NEWTXDT ,'') AS TXDT, TXDATE, BIZDATE, STAN, OUTACCTNO, INACCTNO, COALESCE(NEWTXAMT,0) AS TXAMT, NEWRESULT,SENDERACQUIRE, SENDERHEAD ");
				
				tmpSQL.append("  , SENDERID, EACH_TXN_NAME ");
				tmpSQL.append(" , COALESCE(TXID,'') ||'-'|| COALESCE((SELECT TC.TXN_NAME FROM TXN_CODE TC WHERE TC.TXN_ID = TXID ),'') AS TXID ");
				tmpSQL.append(" , COALESCE(SENDERBANKID,'') ||'-'|| COALESCE((SELECT BRBK_NAME FROM BANK_BRANCH WHERE BRBK_ID=SENDERBANKID),'') SENDERBANKID "); 
				tmpSQL.append(" , COALESCE(OUTBANKID,'') ||'-'|| COALESCE((SELECT BRBK_NAME FROM BANK_BRANCH WHERE BRBK_ID=OUTBANKID),'') OUTBANKID "); 
				tmpSQL.append(" , COALESCE(INBANKID,'') ||'-'|| COALESCE((SELECT BRBK_NAME FROM BANK_BRANCH WHERE BRBK_ID=INBANKID),'') INBANKID ");
				tmpSQL.append(" , COALESCE(PCODE,'') || '-' || COALESCE(EACH_TXN_NAME,'') AS PCODE ");
//				20151127 add by hugo req by UAT-20151126-01
				tmpSQL.append(" , BUSINESS_TYPE_ID ");
				tmpSQL.append(" FROM VW_ONBLOCKTAB ");
				tmpSQL.append(" LEFT JOIN (  SELECT EACH_TXN_ID, EACH_TXN_NAME FROM EACH_TXN_CODE  ) ON PCODE = EACH_TXN_ID   ");
				tmpSQL.append(" WHERE RESULTSTATUS = 'P' AND SENDERSTATUS <> '1' ");
				tmpSQL.append((StrUtils.isEmpty(condition_2)?"" : "AND " + condition_2));
			}
			tmpSQL.append(" ) ");
			
			sql.append(" SELECT * FROM ( ");
			sql.append("  	SELECT  ROWNUMBER() OVER( "+orderSQL+") AS ROWNUMBER , TEMP.*  ");
			sql.append(" FROM TEMP ");
			sql.append(" ) AS A    ");
			sql.append("  WHERE ROWNUMBER >= " + (Page.getStartOfPage(pageNo, pageSize) + 1) + " AND ROWNUMBER <= " + (pageNo * pageSize) + " ");
			sql.insert(0, tmpSQL.toString());
			sql.append(orderSQL);
			
			cntSQL.append(" SELECT COALESCE(COUNT(*),0) AS NUM FROM TEMP ");
			cntSQL.insert(0, tmpSQL.toString());
			sumSQL.append(" SELECT COALESCE( SUM(TXAMT) ,0) AS TXAMT FROM TEMP ");
			sumSQL.insert(0, tmpSQL.toString());
			
			//先算出總計的欄位值，放到rtnMap裡面，到頁面再放到下面的總計Grid裡面
			System.out.println("sumSQL="+sumSQL);
			List dataSumList = commonSpringDao.list(sumSQL.toString(),null);
			rtnMap.put("dataSumList",dataSumList);
			
//			String cols[] = {"PCODE","NEWTXDT","TXDATE","STAN","SENDERBANKID","OUTBANKID","INBANKID","OUTACCTNO","INACCTNO","NEWTXAMT","TXID","SENDERID"};
			String cols[] = {"PCODE","TXDT","TXDATE","STAN","SENDERBANKID","OUTBANKID","INBANKID","OUTACCTNO","INACCTNO","TXAMT","TXID","SENDERID"};
		System.out.println("cntSQL==>"+cntSQL.toString().toUpperCase());
		System.out.println("sql==>"+sql.toString().toUpperCase());
			page = vw_onblocktab_Dao.getDataIII(pageNo, pageSize, cntSQL.toString(), sql.toString(), cols, UNDONE_TXDATA.class);
			list = (List<UNDONE_TXDATA>) page.getResult();
			System.out.println("UNDONE_TXDATA.list>>"+list);
			list = list!=null&& list.size() ==0 ?null:list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(page == null){
			rtnMap.put("total", "0");
			rtnMap.put("page", "0");
			rtnMap.put("records", "0");
			rtnMap.put("rows", new ArrayList());
		}else{
			rtnMap.put("total", page.getTotalPageCount());
			rtnMap.put("page", String.valueOf(page.getCurrentPageNo()));
			rtnMap.put("records", page.getTotalCount());
			rtnMap.put("rows", list);
		}
		
		String json = JSONUtils.map2json(rtnMap) ;
		return json;
	}
	
	/**
	 * 原pageSearch的備份，暫時不使用
	 * @param param
	 * @return
	 */
	public String pageSearch_bak(Map<String, String> param){
		List<String> conditions_1 = new ArrayList<String>();
		List<String> conditions_2 = new ArrayList<String>();
		
		/* 20150210 HUANGPU 改以營業日(BIZDATE)查詢資料，非交易日期(TXDATE) */
		String startDate = "";
		if(StrUtils.isNotEmpty(param.get("START_DATE").trim())){
			startDate = param.get("START_DATE").trim();
			conditions_1.add(" BIZDATE >= '" + DateTimeUtils.convertDate(startDate, "yyyyMMdd", "yyyyMMdd") + "' ");
		}
		
		String endDate = "";
		if(StrUtils.isNotEmpty(param.get("END_DATE").trim())){
			endDate = param.get("END_DATE").trim();
			conditions_1.add(" BIZDATE <= '" + DateTimeUtils.convertDate(endDate, "yyyyMMdd", "yyyyMMdd") + "' ");
		}
		
		String clearingphase = "";
		if(StrUtils.isNotEmpty(param.get("CLEARINGPHASE").trim()) && !param.get("CLEARINGPHASE").trim().equals("all")){
			clearingphase = param.get("CLEARINGPHASE").trim();
			conditions_1.add(" CLEARINGPHASE = '" + clearingphase + "' ");
		}
		
		String opbkId = "";
		if(StrUtils.isNotEmpty(param.get("OPBK_ID").trim()) && !param.get("OPBK_ID").trim().equals("all")){
			opbkId = param.get("OPBK_ID").trim();
				conditions_1.add(" (SENDERACQUIRE = '" + opbkId + "' OR OUTACQUIRE = '" + opbkId + "' OR INACQUIRE = '" + opbkId + "') ");
		}
		
		String bgbkId = "";
		if(StrUtils.isNotEmpty(param.get("BGBK_ID").trim()) && !param.get("BGBK_ID").trim().equals("all")){
			bgbkId = param.get("BGBK_ID").trim();
			conditions_1.add(" (SENDERHEAD = '" + bgbkId + "' OR OUTHEAD = '" + bgbkId + "' OR INHEAD = '" + bgbkId + "') ");
		}
		
		String businessTypeId = "";
		if(StrUtils.isNotEmpty(param.get("BUSINESS_TYPE_ID").trim()) && !param.get("BUSINESS_TYPE_ID").trim().equals("all")){
			businessTypeId = param.get("BUSINESS_TYPE_ID").trim();
			conditions_2.add(" BUSINESS_TYPE_ID = '" + businessTypeId + "' ");
		}
		
		
		
		int pageNo = StrUtils.isEmpty(param.get("page")) ?0:Integer.valueOf(param.get("page"));
		int pageSize = StrUtils.isEmpty(param.get("rows")) ?Integer.valueOf(Arguments.getStringArg("PAGE.SIZE")):Integer.valueOf(param.get("rows"));
		
		Map rtnMap = new HashMap();
		
		List<UNDONE_TXDATA> list = null;
		Page page = null;
		try {
			list = new ArrayList<UNDONE_TXDATA>();
			String condition_1 = "",condition_2 = "";
			condition_1 = combine(conditions_1);
			condition_2 = combine(conditions_2);
			
			StringBuffer fromAndWhere_core = new StringBuffer();
			fromAndWhere_core.append("FROM ( ");
			fromAndWhere_core.append("    SELECT ROWNUMBER() OVER(");
			if(StrUtils.isNotEmpty(param.get("sidx"))){
				if("PCODE".equalsIgnoreCase(param.get("sidx"))){
					fromAndWhere_core.append(" ORDER BY PCODE "+param.get("sord"));
				}
				else if("SENDERBANKID".equalsIgnoreCase(param.get("sidx"))){
					fromAndWhere_core.append(" ORDER BY SENDERBANKID "+param.get("sord"));
				}
				else if("OUTBANKID".equalsIgnoreCase(param.get("sidx"))){
					fromAndWhere_core.append(" ORDER BY OUTBANKID "+param.get("sord"));
				}
				else if("INBANKID".equalsIgnoreCase(param.get("sidx"))){
					fromAndWhere_core.append(" ORDER BY INBANKID "+param.get("sord"));
				}
				else if("TXAMT".equalsIgnoreCase(param.get("sidx"))){
					fromAndWhere_core.append(" ORDER BY NEWTXAMT "+param.get("sord"));
				}
				else if("RESULTCODE".equalsIgnoreCase(param.get("sidx"))){
					fromAndWhere_core.append(" ORDER BY NEWRESSULT "+param.get("sord"));
				}
				else{
					fromAndWhere_core.append(" ORDER BY "+param.get("sidx")+" "+param.get("sord"));
				}
			}
			fromAndWhere_core.append(") AS ROWNUMBER, PCODE, NEWTXDT, TXDATE, BIZDATE, STAN, OUTACCTNO, INACCTNO, NEWTXAMT, NEWRESULT, ");
			fromAndWhere_core.append("    SENDERACQUIRE, SENDERHEAD, SENDERBANKID, OUTBANKID, INBANKID, TXID, SENDERID, EACH_TXN_NAME ");
			fromAndWhere_core.append("    FROM VW_ONBLOCKTAB " + (StrUtils.isNotEmpty(businessTypeId)?"JOIN ( ":"LEFT JOIN ( ") );
			fromAndWhere_core.append("        SELECT EACH_TXN_ID, EACH_TXN_NAME ");
			fromAndWhere_core.append("        FROM EACH_TXN_CODE ");
			fromAndWhere_core.append("        " + (StrUtils.isNotEmpty(condition_2)?"WHERE " + condition_2:""));
			fromAndWhere_core.append("    ) ON PCODE = EACH_TXN_ID ");
			//20150302 BY 李建利(與陳淑華開會討論)：只要是曾經是未完成交易(不包含處理中)，無論是否有回應結果，都可在此功能中查詢出來
			//fromAndWhere_core.append("    WHERE A.NEWRESULT = 'P' AND COALESCE(B.BIZDATE,'') = '' AND COALESCE(B.CLEARINGPHASE,'') = '' ");
			fromAndWhere_core.append("    WHERE RESULTSTATUS = 'P' AND SENDERSTATUS <> '1' " + (StrUtils.isEmpty(condition_1)?"" : "AND " + condition_1));
			fromAndWhere_core.append(") ");
			
			StringBuffer fromAndWhere = new StringBuffer();
			fromAndWhere.append("FROM (");
			fromAndWhere.append("    SELECT * ");
			fromAndWhere.append(fromAndWhere_core);
			fromAndWhere.append("    WHERE ROWNUMBER >= " + (Page.getStartOfPage(pageNo, pageSize) + 1) + " AND ROWNUMBER <= " + (pageNo * pageSize) + " ");
			fromAndWhere.append(") AS A ");
			
			StringBuffer fromAndWhere_all = new StringBuffer();
			fromAndWhere_all.append("FROM (");
			fromAndWhere_all.append("    SELECT * ");
			fromAndWhere_all.append(fromAndWhere_core);
			fromAndWhere_all.append(") AS A ");
			
			//先算出總計的欄位值，放到rtnMap裡面，到頁面再放到下面的總計Grid裡面
			StringBuffer dataSumSQL = new StringBuffer();
			dataSumSQL.append("SELECT SUM(NEWTXAMT) TXAMT ");
			dataSumSQL.append(fromAndWhere_all);
			//System.out.println("dataSumSQL="+dataSumSQL);
			List dataSumList = commonSpringDao.list(dataSumSQL.toString(),null);
			rtnMap.put("dataSumList",dataSumList);
			
			StringBuffer countQuery = new StringBuffer();
			String cols[] = {"PCODE","TXDT","TXDATE","STAN","SENDERBANKID","OUTBANKID","INBANKID","OUTACCTNO","INACCTNO","TXAMT","TXID","SENDERID"};
			countQuery.append("SELECT COUNT(*) AS NUM ");
			countQuery.append(fromAndWhere_all);
			
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT ");
			sql.append("A.PCODE || '-' || COALESCE(A.EACH_TXN_NAME,'') PCODE, "); 
			sql.append("VARCHAR_FORMAT(A.NEWTXDT, 'YYYY-MM-DD HH24:MI:SS') AS TXDT, A.TXDATE, A.STAN, "); 
			sql.append("A.SENDERBANKID || '-' || COALESCE((SELECT BRBK_NAME FROM BANK_BRANCH WHERE BRBK_ID=A.SENDERBANKID),'') SENDERBANKID, "); 
			sql.append("A.OUTBANKID || '-' || COALESCE((SELECT BRBK_NAME FROM BANK_BRANCH WHERE BRBK_ID=A.OUTBANKID),'') OUTBANKID, "); 
			sql.append("A.INBANKID || '-' || COALESCE((SELECT BRBK_NAME FROM BANK_BRANCH WHERE BRBK_ID=A.INBANKID),'') INBANKID, "); 
			sql.append("OUTACCTNO,INACCTNO,A.NEWTXAMT AS TXAMT,A.TXID || '-' || (SELECT TXN_NAME FROM TXN_CODE WHERE TXN_ID = A.TXID) AS TXID,A.SENDERID ");
			sql.append(fromAndWhere);
			if(StrUtils.isNotEmpty(param.get("sidx"))){
				if(param.get("sidx").contains("ASC") || param.get("sidx").contains("DESC") ||
						param.get("sidx").contains("asc") || param.get("sidx").contains("desc")){
					sql.append(" ORDER BY "+param.get("sidx"));
				}else{
					sql.append(" ORDER BY "+param.get("sidx")+" "+param.get("sord"));
				}
			}
			
			//System.out.println("countQuery==>"+countQuery.toString().toUpperCase());
			System.out.println("sql==>"+sql.toString().toUpperCase());
			
			page = vw_onblocktab_Dao.getDataIII(pageNo, pageSize, countQuery.toString(), sql.toString(), cols, UNDONE_TXDATA.class);
			list = (List<UNDONE_TXDATA>) page.getResult();
			System.out.println("UNDONE_TXDATA.list>>"+list);
			list = list!=null&& list.size() ==0 ?null:list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(page == null){
			rtnMap.put("total", "0");
			rtnMap.put("page", "0");
			rtnMap.put("records", "0");
			rtnMap.put("rows", new ArrayList());
		}else{
			rtnMap.put("total", page.getTotalPageCount());
			rtnMap.put("page", String.valueOf(page.getCurrentPageNo()));
			rtnMap.put("records", page.getTotalCount());
			rtnMap.put("rows", list);
		}
		
		String json = JSONUtils.map2json(rtnMap) ;
		return json;
	}
	
	public String combine(List<String> conditions){
		String conStr = "";
		for(int i = 0 ; i < conditions.size(); i++){
			conStr += conditions.get(i);
			if(i < conditions.size() - 1){
				conStr += " AND ";
			}
		}
		return conStr;
	}
	
	public ONBLOCKTAB_Dao getOnblocktab_Dao() {
		return onblocktab_Dao;
	}
	public void setOnblocktab_Dao(ONBLOCKTAB_Dao onblocktab_Dao) {
		this.onblocktab_Dao = onblocktab_Dao;
	}
	public BANK_GROUP_Dao getBank_group_Dao() {
		return bank_group_Dao;
	}
	public void setBank_group_Dao(BANK_GROUP_Dao bank_group_Dao) {
		this.bank_group_Dao = bank_group_Dao;
	}
	public VW_ONBLOCKTAB_Dao getVw_onblocktab_Dao() {
		return vw_onblocktab_Dao;
	}
	public void setVw_onblocktab_Dao(VW_ONBLOCKTAB_Dao vw_onblocktab_Dao) {
		this.vw_onblocktab_Dao = vw_onblocktab_Dao;
	}

	public CommonSpringDao getCommonSpringDao() {
		return commonSpringDao;
	}

	public void setCommonSpringDao(CommonSpringDao commonSpringDao) {
		this.commonSpringDao = commonSpringDao;
	}

	public SocketClient getSocketClient() {
		return socketClient;
	}

	public void setSocketClient(SocketClient socketClient) {
		this.socketClient = socketClient;
	}

	public ONPENDINGTAB_Dao getOnpendingtab_Dao() {
		return onpendingtab_Dao;
	}

	public void setOnpendingtab_Dao(ONPENDINGTAB_Dao onpendingtab_Dao) {
		this.onpendingtab_Dao = onpendingtab_Dao;
	}
	
	
}
