package tw.org.twntch.bo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.struts.util.LabelValueBean;

import tw.org.twntch.po.VW_ONBLOCKTAB;
import tw.org.twntch.db.dao.hibernate.BANK_GROUP_Dao;
import tw.org.twntch.db.dao.hibernate.EACH_FUNC_LIST_Dao;
import tw.org.twntch.db.dao.hibernate.EACH_USERLOG_Dao;
import tw.org.twntch.db.dao.hibernate.EACH_USER_Dao;
import tw.org.twntch.db.dao.hibernate.ONBLOCKTAB_Dao;
import tw.org.twntch.db.dao.hibernate.CommonSpringDao;
import tw.org.twntch.db.dao.hibernate.Page;
import tw.org.twntch.db.dao.hibernate.RPONBLOCKTAB_Dao;
import tw.org.twntch.db.dao.hibernate.VW_ONBLOCKTAB_Dao;
import tw.org.twntch.po.BANK_GROUP;
import tw.org.twntch.po.EACH_FUNC_LIST;
import tw.org.twntch.po.EACH_USER;
import tw.org.twntch.po.EACH_USER_PK;
import tw.org.twntch.po.ONBLOCKTABbean;
import tw.org.twntch.po.ONBLOCKTAB;
import tw.org.twntch.util.CodeUtils;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.RptUtils;
import tw.org.twntch.util.StrUtils;

public class ONBLOCKTAB_BO {

	private EACH_USER_Dao each_user_Dao;
	private EACH_FUNC_LIST_Dao func_list_Dao;
	private EACH_USERLOG_Dao userLog_Dao;
	private ONBLOCKTAB_Dao onblocktab_Dao;
	private BANK_GROUP_Dao bank_group_Dao;
	private VW_ONBLOCKTAB_Dao vw_onblocktab_Dao;
	private CommonSpringDao commonSpringDao;
	private EACH_USERLOG_BO userlog_bo;
	private RPONBLOCKTAB_Dao rponblocktab_Dao;
	
	
	
	public List<Map> groupOnpending(String bizdate , String  clearingphase){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM EACHUSER.ONBLOCKTAB");
		Map<String,String> params = new HashMap<String , String>();
		Map retMap = new HashMap();
		List<Map> list = null;
		
		try {
			params.put("flbizdate", bizdate);
			params.put("oclearingphase", clearingphase);
			list = vw_onblocktab_Dao.getData(sql.toString()  ,params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return list;
		
	}
	public Map<String,Integer> cntOnpending(String bizdate , String  clearingphase){
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT COUNT(*) as NUM  FROM ONPENDINGTAB WHERE FLBIZDATE = :flbizdate AND OCLEARINGPHASE = :oclearingphase ");
		Map<String,String> params = new HashMap<String , String>();
		Map<String,Integer> retMap = new HashMap<String,Integer>();
		
		try {
			params.put("flbizdate", bizdate);
			params.put("oclearingphase", clearingphase);
			retMap = vw_onblocktab_Dao.getDetail(sql.toString()  ,params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return retMap;
		
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
	
	//按照USER_COMPANY找出所有USER
	public List<LabelValueBean> getUserIdListByComId(String comId){
		List<EACH_USER> userIdList = null;
		List<LabelValueBean> list = null;
		try{
			if(StrUtils.isNotEmpty(comId)){
				userIdList = each_user_Dao.getDataByComId(comId);
			}else{
				userIdList = each_user_Dao.getAll();
			}
			System.out.println("userIdList"+userIdList);
			if(userIdList != null && userIdList.size() > 0){
				list = new ArrayList<LabelValueBean>();
				for(int i = 0; i < userIdList.size(); i++){
					list.add(new LabelValueBean(userIdList.get(i).getUSER_ID(), userIdList.get(i).getUSER_ID()));
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		list = list == null? null : list.size() == 0? null : list;
		System.out.println("getUserIdListByComId list>>" + list);
		return list;
	}
	
	
	//找出所有USER_COMPANY清單
	public List<LabelValueBean> getUserCompanyList(){
		List<EACH_USER_PK> usercompanyList = null;
		List<LabelValueBean> list = null;
		try{
			usercompanyList = each_user_Dao.getUserCompanyList();
			if(usercompanyList != null && usercompanyList.size() > 0){
				list = new ArrayList<LabelValueBean>();
				for(int i = 0; i < usercompanyList.size(); i++){
					list.add(new LabelValueBean(usercompanyList.get(i).getUSER_COMPANY(), usercompanyList.get(i).getUSER_COMPANY()));
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		list = list == null? null : list.size() == 0? null : list;
		System.out.println("getUserCompanyList list>>" + list);
		return list;
	}	
	
	//找出所有功能清單
	public List getFuncList(){
		List<EACH_FUNC_LIST> menuList = null;
		List<EACH_FUNC_LIST> subList = null;
		Map menuItem = null;
		Map subItem = null;
		List menuList_N = new ArrayList();
		List subList_N = new ArrayList();
		try {
			//找作業模組
			menuList = func_list_Dao.getFuncItemByType("1");
			if(menuList != null){
				menuList_N = new ArrayList();
				for(int i = 0; i < menuList.size(); i++){
					menuItem = new HashMap();
					menuItem.put("FUNC_NAME", menuList.get(i).getFUNC_NAME());
					menuItem.put("FUNC_URL", menuList.get(i).getFUNC_URL());
					menuItem.put("FUNC_TYPE", menuList.get(i).getFUNC_TYPE());
					menuItem.put("FUNC_ID", menuList.get(i).getFUNC_ID());
					subList = func_list_Dao.getNextSubItem(menuList.get(i).getFUNC_ID());
					if(subList != null){
						subList_N = new ArrayList();
						for(int j = 0; j < subList.size(); j++){
							subItem = new HashMap();
							subItem.put("FUNC_NAME", subList.get(j).getFUNC_NAME());
							subItem.put("FUNC_URL", subList.get(j).getFUNC_URL());
							subItem.put("FUNC_TYPE", subList.get(j).getFUNC_TYPE());
							subItem.put("FUNC_ID", subList.get(j).getFUNC_ID());
							subList_N.add(subItem);
						}
						menuItem.put("SUB_LIST", subList_N);
					}
					menuList_N.add(menuItem);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("getFuncList menu>>" + menuList_N);
		return menuList_N;
	}
	
	
	//根據群組類別找出所屬功能清單
	public List getFuncListByRole_Type(String role_type){
		List<Map> beanList = new LinkedList<Map>();
		List<Map> subList = new LinkedList<Map>();
		StringBuffer pathSql = new StringBuffer();
		try {
			role_type= StrUtils.isEmpty(role_type) ? "" :role_type;
			//票交所
			if(role_type.equals("A")){
				pathSql.append(" WHERE (S.BANK_FUNC='Y' AND S.TCH_FUNC='Y') OR S.TCH_FUNC='Y' ");
			//銀行端
			}else if(role_type.equals("B")){
				pathSql.append(" WHERE (S.BANK_FUNC='Y' AND S.TCH_FUNC='Y') OR S.BANK_FUNC='Y' ");
			}
			List<EACH_FUNC_LIST> list = func_list_Dao.getAllSubItem(pathSql.toString());
			String tmp = "";
			String tmpName = "";
			Map map = null;
			Map keymap = null;
			int i =0;
			for(EACH_FUNC_LIST po :list){
				map = new HashMap<>();
				System.out.println("tmp"+tmp+",UP_FUNC_ID>>"+po.getUP_FUNC_ID());
				if(i==0){
					tmp = po.getUP_FUNC_ID();
					tmpName = po.getUP_FUNC_NAME();
				}
				
				if(!tmp.equals(po.getUP_FUNC_ID())){
					keymap = new HashMap<>();
					keymap.put(tmpName, beanList);
					subList.add(keymap);
					tmp = po.getUP_FUNC_ID();
					tmpName = po.getUP_FUNC_NAME();
					beanList = new LinkedList<Map>();
				}
				map.put("label", po.getFUNC_NAME());
				map.put("value", po.getFUNC_ID());
				beanList.add(map);
				if(i==list.size()-1){
					keymap = new HashMap<>();
					keymap.put(tmpName, beanList);
					subList.add(keymap);
				}
				i++;	
				
			}
			System.out.println("getFuncListByRole_Type subList>>"+subList);
			return subList;
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("getFuncListByRole_Type subList>>" + subList);
		return subList;
	}
	
	public List<ONBLOCKTAB> search(String opt_date ,String user_company ,String sUser_id , String func_id,int pageNo , int pageSize){
		List<ONBLOCKTAB> list = null;
		try {
			
			list = new ArrayList<ONBLOCKTAB>();
			StringBuffer sql = new StringBuffer();
			sql.append(" FROM tw.org.twntch.po.ONBLOCKTAB WHERE ") ;
			List<String> strList = new LinkedList<String>();
			Map<String,String> map = new HashMap<String,String>();
			map.put("TXTIME1", DateTimeUtils.convertDate(opt_date.trim(), "yyyyMMdd", "yyyy-MM-dd"));
			map.put("USER_COMPANY", user_company.trim());
			map.put("USERID", sUser_id.trim());
			map.put("FUNC_ID", func_id.trim());
			int i =0;
			for(String key:map.keySet()){
				if(StrUtils.isNotEmpty(map.get(key))){
					if(i!=0){
						sql.append(" AND ");
					}
					if(key.equals("TXTIME1")){
						sql.append(key + " >=  ?  AND "+key+" <= ?");
						strList.add(map.get(key)+" 00:00:00 ");
						strList.add(map.get(key)+" 23:59:59 ");
//						sql.append(key + " LIKE ?");
//						strList.add("%" + map.get(key) + "%");
					}else{
						sql.append(key+" = ?");
						strList.add(map.get(key));
					}
					i++;
				}
			}
			System.out.println("strList>>"+strList);
//			getQuery
//			list = userLog_Dao.find(sql.toString(), strList.toArray());
			list = (List<ONBLOCKTAB>) onblocktab_Dao.pagedQuery(sql.toString(),pageNo,pageSize, strList.toArray()).getResult();
//			list = (List<EACH_USERLOG>) userLog_Dao.getAllDataByParm(pageNo,pageSize);
			System.out.println("ONBLOCKTAB.list>>"+list);
			list = list!=null&& list.size() ==0 ?null:list;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	
	
	public String search_toJson(Map<String, String> param){		
		String opt_date = StrUtils.isNotEmpty(param.get("TXTIME1"))?param.get("TXTIME1"):"";
		String user_company = StrUtils.isNotEmpty(param.get("USER_COMPANY")) && param.get("USER_COMPANY").trim().equals("all") ?"":param.get("USER_COMPANY");
		String sUser_id = StrUtils.isNotEmpty(param.get("USERID")) && param.get("USERID").trim().equals("all") ?"":param.get("USERID");
		String func_id = StrUtils.isNotEmpty(param.get("FUNC_ID")) && param.get("FUNC_ID").trim().equals("all") ?"":param.get("FUNC_ID");
//		String pageNo = StrUtils.isEmpty(param.get("pageNo")) ?"0":param.get("pageNo");
		String pageNo = StrUtils.isEmpty(param.get("page")) ?"0":param.get("page");
		System.out.println("pageNo"+pageNo);
//	 改從參數拿取
		String pageSize = StrUtils.isEmpty(param.get("pagesize")) ?Arguments.getStringArg("PAGE.SIZE"):param.get("pagesize");
		String json = JSONUtils.toJson(search(opt_date, user_company, sUser_id, func_id ,Integer.valueOf(pageNo)  ,Integer.valueOf(pageSize) )) ;
		return json;
	}
	
	//20150317 交易資料查詢
	public String getResList(Map<String, String> params){
		//日期區間(票交端為區間，銀行端僅可選單一日期 TXTIME1)
		String stadate = StrUtils.isEmpty(params.get("TXTIME1"))?"":DateTimeUtils.convertDate(params.get("TXTIME1"), "yyyyMMdd", "yyyyMMdd");
		String enddate = StrUtils.isEmpty(params.get("TXTIME2"))?"":DateTimeUtils.convertDate(params.get("TXTIME2"), "yyyyMMdd", "yyyyMMdd");
		//日期區間(TXDT)
		String sdate = StrUtils.isEmpty(params.get("TXTIME3"))?"":DateTimeUtils.convertDate(params.get("TXTIME3"), "yyyyMMdd", "yyyyMMdd");
		String edate = StrUtils.isEmpty(params.get("TXTIME4"))?"":DateTimeUtils.convertDate(params.get("TXTIME4"), "yyyyMMdd", "yyyyMMdd");
		String hour1 = StrUtils.isEmpty(params.get("HOUR1"))?"":params.get("HOUR1").trim();
		String hour2 = StrUtils.isEmpty(params.get("HOUR2"))?"":params.get("HOUR2").trim();
		String min1 = StrUtils.isEmpty(params.get("MON1"))?"":params.get("MON1").trim();
		String min2 = StrUtils.isEmpty(params.get("MON2"))?"":params.get("MON2").trim();
		
		sdate = sdate + hour1 + min1;
		edate = edate + hour2 + min2;
		//交易追蹤序號(起
		String stan = StrUtils.isEmpty(params.get("STAN"))?"":params.get("STAN").trim();
		//交易追蹤序號(迄
		String stan2 = StrUtils.isEmpty(params.get("STAN2"))?"":params.get("STAN2").trim();
		//收費類型
		String fee_type = StrUtils.isEmpty(params.get("FEE_TYPE"))?"":params.get("FEE_TYPE").trim();
		//發動/收受選項
		String cdNumRao = StrUtils.isEmpty(params.get("CDNUMRAO"))?"":params.get("CDNUMRAO");
		//統編
		String cdNumId = StrUtils.isEmpty(params.get("CARDNUM_ID"))?"":params.get("CARDNUM_ID").trim();
		//入帳/扣款選項
		String opAction1 = StrUtils.isEmpty(params.get("opAction1"))?"":params.get("opAction1");
		//帳號
		String acctNo = StrUtils.isEmpty(params.get("USERID"))?"":params.get("USERID").trim();
		//金額
		String txamt = StrUtils.isEmpty(params.get("TXAMT"))?"":params.get("TXAMT").trim();
		//查詢角度
		String searchAspect = StrUtils.isEmpty(params.get("searchAspect"))?"":params.get("searchAspect").trim();
		//操作行
		String opbkId = StrUtils.isEmpty(params.get("OPBK_ID"))?"":params.get("OPBK_ID").trim();
		//總行
		String bgbkId = StrUtils.isEmpty(params.get("BGBK_ID"))?"":params.get("BGBK_ID").trim();
		//分行
		String brbkId = StrUtils.isEmpty(params.get("BRBK_ID"))?"":params.get("BRBK_ID").trim();
		//清算階段代號
		String clearingphase = StrUtils.isEmpty(params.get("CLEARINGPHASE"))?"":params.get("CLEARINGPHASE");
		//業務類別代號
		String bus_Type_Id = StrUtils.isEmpty(params.get("BUSINESS_TYPE_ID"))?"":params.get("BUSINESS_TYPE_ID");
		//交易結果
		String resultstatus = StrUtils.isEmpty(params.get("RESULTSTATUS"))?"":params.get("RESULTSTATUS");
		//交易結果
		String pfclass = StrUtils.isEmpty(params.get("PFCLASS"))?"":params.get("PFCLASS");
		//交易結果
		String tollid = StrUtils.isEmpty(params.get("TOLLID"))?"":params.get("TOLLID");
		//交易結果
		String chargetype = StrUtils.isEmpty(params.get("CHARGETYPE"))?"":params.get("CHARGETYPE");
		//交易結果
		String billflag = StrUtils.isEmpty(params.get("BILLFLAG"))?"":params.get("BILLFLAG");
		//交易結果
		String txid = StrUtils.isEmpty(params.get("TXID"))?"":params.get("TXID");
		//回應狀態RC
		String resstatus = StrUtils.isEmpty(params.get("RESSTATUS"))?"all":params.get("RESSTATUS");
		//回應狀態RC
		String rcserctext = StrUtils.isEmpty(params.get("RCSERCTEXT"))?"":params.get("RCSERCTEXT");
		System.out.println("RCSERCTEXT >> "+ rcserctext);
		
		//是否包含異常資料(N表示不包含)
		String garbageData = params.get("GARBAGEDATA")==null?"N":"Y";
		//是否包含整批資料("N"表示不包含)
		String filter_bat = params.get("FILTER_BAT")==null?"N":"Y";
		int pageNo = StrUtils.isEmpty(params.get("page")) ?0:Integer.valueOf(params.get("page"));
		int pageSize = StrUtils.isEmpty(params.get("rows"))?Integer.valueOf(Arguments.getStringArg("PAGE.SIZE")):Integer.valueOf(params.get("rows"));
		String isSearch = StrUtils.isEmpty(params.get("isSearch")) ?"Y":params.get("isSearch");
		List<VW_ONBLOCKTAB> list = null;
		Map rtnMap = new HashMap();
		Page page = null;
		List<String> conditions_1 = new ArrayList<String>();
		List<String> conditions_2 = new ArrayList<String>();
		String condition_1 = "" , condition_2 = "";
		try{
			/* 20150210 HUANGPU 改以營業日(BIZDATE)查詢資料，非交易日期時間(TXDT) */
			if(StrUtils.isNotEmpty(stadate) && StrUtils.isNotEmpty(enddate)){
				conditions_1.add(" (BIZDATE >= '" + stadate + "' AND BIZDATE <= '"+enddate+"') ");
			}else if(StrUtils.isNotEmpty(stadate)){
				conditions_1.add(" BIZDATE >= '" + stadate + "' ");
			}else if(StrUtils.isNotEmpty(enddate)){
				conditions_1.add(" BIZDATE <= '" + enddate + "' ");
			}
			
			/* 20200413 vincenthuang 增加非交易日期時間(TXDT) */
			if(StrUtils.isNotEmpty(sdate) && StrUtils.isNotEmpty(edate)){
				conditions_1.add(" ( TXDT BETWEEN '"+sdate+"00' AND '"+edate+"59' )");
			}else if(StrUtils.isNotEmpty(sdate)){
				conditions_1.add(" TXDT >= '" + sdate + "00' ");
			}else if(StrUtils.isNotEmpty(edate)){
				conditions_1.add(" TXDT <= '" + edate + "59' ");
			}
			
			if(StrUtils.isNotEmpty(stan)&&StrUtils.isNotEmpty(stan2)) {
				conditions_1.add(" ( STAN BETWEEN '"+stan+ "' AND '"+stan2+"' )");
			}else if(StrUtils.isNotEmpty(stan)&&!StrUtils.isNotEmpty(stan2)){
				conditions_1.add(" STAN = '" + stan + "' ");
			}
			
			if(StrUtils.isNotEmpty(fee_type)){
				conditions_1.add(" FEE_TYPE = '" + fee_type + "' ");
			}
	
			if(StrUtils.isNotEmpty(cdNumId)){
				if(cdNumRao.equals("SENDID")){
					conditions_1.add(" SENDERID = '" + cdNumId + "' ");
				}else if(cdNumRao.equals("RECVID")){
					conditions_1.add(" RECEIVERID = '" + cdNumId + "' ");
				}
			}
			if(StrUtils.isNotEmpty(acctNo)){
				if(opAction1.equals("IN")){
					conditions_1.add(" INACCTNO = '" + acctNo + "' ");
				}else if(opAction1.equals("OUT")){
					conditions_1.add(" OUTACCTNO = '" + acctNo + "' ");
				}
			}
			if(StrUtils.isNotEmpty(txamt)){
				if("N".equals(garbageData)){
					conditions_1.add(" NEWTXAMT = ISNUMERIC('" + txamt + "') ");
				}else{
					conditions_1.add(" NEWTXAMT = '" + txamt + "' ");
				}
			}
			if(StrUtils.isNotEmpty(clearingphase)){
				conditions_1.add(" CLEARINGPHASE = '" + clearingphase + "' ");
			}
			if(StrUtils.isNotEmpty(bus_Type_Id)){
				conditions_1.add(" SUBSTR(PCODE,1,2) LIKE SUBSTR('" + bus_Type_Id + "',1,2) ");
			}
			//(U=處理中需另外轉換條件)
			if(StrUtils.isNotEmpty(resultstatus)){
				if(resultstatus.equals("U")){
					conditions_1.add(" (RESULTSTATUS = 'P' AND SENDERSTATUS = '1') ");
				}else if(resultstatus.equals("AP")){
					conditions_1.add(" ( (RESULTSTATUS IN ('A', 'P')) AND SENDERSTATUS <> '1') ");
				}else{
					conditions_1.add(" (RESULTSTATUS = '" + resultstatus + "' AND SENDERSTATUS <> '1') ");
				}
			}
			//20170719新增的查詢欄位
			if(StrUtils.isNotEmpty(pfclass)){
				conditions_1.add(" PFCLASS = '" + pfclass + "' ");
			}
			if(StrUtils.isNotEmpty(tollid)){
				conditions_1.add(" TOLLID = '" + tollid + "' ");
			}
			if(StrUtils.isNotEmpty(chargetype)){
				conditions_1.add(" CHARGETYPE = '" + chargetype + "' ");
			}
			if(StrUtils.isNotEmpty(billflag)){
				conditions_1.add(" BILLFLAG = '" + billflag + "' ");
			}
			//20200708新增的查詢欄位
			if(StrUtils.isNotEmpty(txid)){
				conditions_1.add(" TXID = '" + txid + "' ");
			}
			//如果回應狀態的下拉式選單的值不為全部
			if(!"all".equals(resstatus)){
				//先切分後面輸入的值,以,分隔
				String[] rcarry = rcserctext.split(",");
				//如果有一個數字以上,串條件
				if(rcarry.length>1) {
					String inCondition="'";
					for(int i = 0 ; i< rcarry.length ; i++) {
						if(i==rcarry.length-1) {
							inCondition=inCondition+rcarry[i]+"'";
						}else {
							inCondition=inCondition+rcarry[i]+"','";
						}
					}
					System.out.println("inCondition >> "+inCondition );
					conditions_1.add( resstatus +" in("+ inCondition +")");
				//只有一個數字
				}else {
					conditions_1.add( resstatus +" = '" + rcarry[0] + "' ");
				}
				
			}
			
			//不包含異常資料才需下條件過濾
			if(garbageData.equals("N")){
				conditions_1.add(" COALESCE(GARBAGEDATA,'') <> '*' ");
			}
			
			if(StrUtils.isNotEmpty(searchAspect)){
				
				if(filter_bat.equals("Y")){
					conditions_2.addAll(conditions_1);
					conditions_1.add(" COALESCE(FLBIZDATE,'') = '' ");
					conditions_2.add(" COALESCE(FLBIZDATE,'') <> '' ");
				}else{// 20160310 add by hugo req by UAT-2016309-01
					conditions_1.add(" COALESCE(FLBIZDATE,'') = '' ");
				}
				if(StrUtils.isNotEmpty(opbkId) && !opbkId.equals("all")){
//					20151012 add by hugo 如過濾整批資料，發動行要過濾所掉所有整批資料，
					if(searchAspect.equals("SENDER")){
						conditions_1.add(" SENDERACQUIRE = '" + opbkId + "' ");
						conditions_2.add(" (SENDERACQUIRE = '" + opbkId + "' AND substr(COALESCE(PCODE,''),4) NOT IN ( '1' ,'2' ,'3' , '4') )");
					}else if(searchAspect.equals("IN")){
						conditions_1.add(" INACQUIRE = '" + opbkId + "' ");
						conditions_2.add(" (INACQUIRE = '" + opbkId + "' AND substr(COALESCE(PCODE,''),4) = '2' )");
					}else if(searchAspect.equals("OUT")){
						conditions_1.add(" OUTACQUIRE = '" + opbkId + "' ");
						conditions_2.add(" (OUTACQUIRE = '" + opbkId + "' AND substr(COALESCE(PCODE,''),4) = '1' )");
					}else if(searchAspect.equals("WO")){
						conditions_1.add(" WOACQUIRE = '" + opbkId + "' ");
						conditions_2.add(" (WOACQUIRE = '" + opbkId + "' AND substr(COALESCE(PCODE,''),4) = '3' )");
					}
				}else{
					if(searchAspect.equals("SENDER")){
//						conditions_2.add("  substr(COALESCE(PCODE,''),4) NOT IN ( '1' ,'2' ,'3' , '4') ");
					}else if(searchAspect.equals("IN")){
						conditions_2.add(" substr(COALESCE(PCODE,''),4) = '2' ");
					}else if(searchAspect.equals("OUT")){
						conditions_2.add(" substr(COALESCE(PCODE,''),4) = '1' ");
					}
					else if(searchAspect.equals("WO")){
						conditions_1.add(" COALESCE(WOACQUIRE,'') <> ''  ");
						conditions_2.add(" substr(COALESCE(PCODE,''),4) = '3' ");
					}
				}
				
				
				if(StrUtils.isNotEmpty(bgbkId)){
					if(searchAspect.equals("SENDER")){
						conditions_1.add(" SENDERHEAD = '" + bgbkId + "' ");
						conditions_2.add(" SENDERHEAD = '" + bgbkId + "' ");
					}else if(searchAspect.equals("IN")){
						conditions_1.add(" INHEAD = '" + bgbkId + "' ");
						conditions_2.add(" INHEAD = '" + bgbkId + "' ");
					}else if(searchAspect.equals("OUT")){
						conditions_1.add(" OUTHEAD = '" + bgbkId + "' ");
						conditions_2.add(" OUTHEAD = '" + bgbkId + "' ");
					}else if(searchAspect.equals("WO")){
						conditions_1.add(" WOHEAD = '" + bgbkId + "' ");
						conditions_2.add(" WOHEAD = '" + bgbkId + "' ");
					}
				}
				if(StrUtils.isNotEmpty(brbkId) && !brbkId.equals("all")){
					if(searchAspect.equals("SENDER")){
						conditions_1.add(" SENDERBANKID = '" + brbkId + "' ");
						conditions_2.add(" SENDERBANKID = '" + brbkId + "' ");
					}else if(searchAspect.equals("IN")){
						conditions_1.add(" INBANKID = '" + brbkId + "' ");
						conditions_2.add(" INBANKID = '" + brbkId + "' ");
					}else if(searchAspect.equals("OUT")){
						conditions_1.add(" OUTBANKID = '" + brbkId + "' ");
						conditions_2.add(" OUTBANKID = '" + brbkId + "' ");
					}else if(searchAspect.equals("WO")){
						conditions_1.add(" WOBANKID = '" + brbkId + "' ");
						conditions_2.add(" WOBANKID = '" + brbkId + "' ");
					}
				}
			}else{
//				未選擇查詢角度
				if(StrUtils.isNotEmpty(opbkId) && !opbkId.equals("all")){
					if(filter_bat.equals("Y")){
						conditions_2.addAll(conditions_1);
						conditions_2.add(" COALESCE(FLBIZDATE,'') <> ''  ");
						conditions_2.add(" ((INACQUIRE = '" + opbkId + "' AND substr(COALESCE(PCODE,''),4) = '2' ) OR (OUTACQUIRE = '" + opbkId + "' AND substr(COALESCE(PCODE,''),4) = '1' ) OR (WOACQUIRE = '" + opbkId + "' AND substr(COALESCE(PCODE,''),4) = '3')) ");
						conditions_1.add(" COALESCE(FLBIZDATE,'') = ''  ");
						conditions_1.add(" (SENDERACQUIRE = '" + opbkId + "' OR OUTACQUIRE = '" + opbkId + "' OR INACQUIRE = '" + opbkId + "' OR WOACQUIRE = '" + opbkId + "') ");
					}else{
						conditions_1.add(" COALESCE(FLBIZDATE,'') = ''  ");
						conditions_1.add(" (SENDERACQUIRE = '" + opbkId + "' OR OUTACQUIRE = '" + opbkId + "' OR INACQUIRE = '" + opbkId + "' OR WOACQUIRE = '" + opbkId + "') ");
					}
				}else{
					if(filter_bat.equals("Y")){
						conditions_2.addAll(conditions_1);
						conditions_2.add(" COALESCE(FLBIZDATE,'') <> ''  ");
						conditions_1.add(" COALESCE(FLBIZDATE,'') = ''  ");
					}else{
						conditions_1.add(" COALESCE(FLBIZDATE,'') = ''  ");
					}
				}
				if(StrUtils.isNotEmpty(bgbkId)){
					conditions_1.add(" (SENDERHEAD = '" + bgbkId + "' OR INHEAD = '" + bgbkId + "' OR OUTHEAD = '" + bgbkId + "' OR WOHEAD = '" + bgbkId + "') ");
					conditions_2.add(" (SENDERHEAD = '" + bgbkId + "' OR INHEAD = '" + bgbkId + "' OR OUTHEAD = '" + bgbkId + "' OR WOHEAD = '" + bgbkId + "') ");
				}
				if(StrUtils.isNotEmpty(brbkId) && !brbkId.equals("all")){
					conditions_1.add(" (SENDERBANKID = '" + brbkId + "' OR INBANKID = '" + brbkId + "' OR OUTBANKID = '" + brbkId + "' OR WOBANKID = '" + brbkId + "') ");
					conditions_2.add(" (SENDERBANKID = '" + brbkId + "' OR INBANKID = '" + brbkId + "' OR OUTBANKID = '" + brbkId + "' OR WOBANKID = '" + brbkId + "') ");
				}
			}
			condition_1 = combine(conditions_1);
			condition_2 = combine(conditions_2);
			
			StringBuffer sql = new StringBuffer();
			StringBuffer tmpSQL = new StringBuffer();
			StringBuffer sumSQL = new StringBuffer();
			StringBuffer cntSQL = new StringBuffer();
			String sord =StrUtils.isNotEmpty(params.get("sord"))? params.get("sord"):"";
			String sidx =StrUtils.isNotEmpty(params.get("sidx"))? params.get("sidx"):"";
			String orderSQL = StrUtils.isNotEmpty(sidx)? " ORDER BY "+sidx+" "+sord:"";
			if("TXAMT".equals(params.get("sidx"))){
				orderSQL = " ORDER BY BIGINT(TXAMT) "+sord;
			}
			if(StrUtils.isNotEmpty(params.get("sidx"))){
//				if("TXDT".equalsIgnoreCase(params.get("sidx"))){
//					orderSQL = "ORDER BY NEWTXDT " + params.get("sord");
//				}
//				if("TXAMT".equalsIgnoreCase(params.get("sidx"))){
//					orderSQL = "ORDER BY NEWTXAMT " + params.get("sord");
//				}
//				if("RESP".equalsIgnoreCase(params.get("sidx"))){
//					orderSQL = "ORDER BY RESULTSTATUS " + params.get("sord");
//				}
//				if(!"ACCTCODE".equalsIgnoreCase(params.get("sidx"))){
//					if("SENDERID".equalsIgnoreCase(params.get("sidx"))){
//						sql.append("ORDER BY C." + params.get("sidx"));
//					}else{
//						sql.append("ORDER BY " + params.get("sidx"));
//					}
//					if(params.get("sidx").contains("ASC") || params.get("sidx").contains("DESC") ||
//					   params.get("sidx").contains("asc") || params.get("sidx").contains("desc")){
//					}else{
//						sql.append(" " + params.get("sord"));
//					}
//				}
			}
			tmpSQL.append(" WITH TEMP AS  ");
			tmpSQL.append(" ( ");
			tmpSQL.append("  SELECT TRANSLATE('abcd-ef-gh op:qr:st', A.TXDT, 'abcdefghopqrst') AS TXDT ");
//			tmpSQL.append("  SELECT VARCHAR_FORMAT(NEWTXDT, 'YYYY-MM-DD HH24:MI:SS') AS TXDT ");
			tmpSQL.append("  , NEWTXAMT AS TXAMT, OUTACCTNO , INACCTNO, STAN, TXDATE, NEWRESULT, UPDATEDT, BIZDATE , CLEARINGPHASE  ");
			tmpSQL.append("  , RECEIVERID, SENDERACQUIRE, OUTACQUIRE, INACQUIRE, WOACQUIRE, SENDERHEAD, OUTHEAD, INHEAD, WOHEAD ,FLBIZDATE ,RESULTSTATUS");
			tmpSQL.append("  , PCODE || '-' || COALESCE((SELECT EACH_TXN_NAME FROM EACH_TXN_CODE WHERE EACH_TXN_ID = PCODE),'') AS PCODE ");
			tmpSQL.append("  , SENDERBANKID || '-' || (SELECT COALESCE(BRBK_NAME,'') FROM BANK_BRANCH WHERE BRBK_ID = SENDERBANKID) AS SENDERBANKID ");
			tmpSQL.append("  , OUTBANKID || '-' || (SELECT COALESCE(BRBK_NAME,'') FROM BANK_BRANCH WHERE BRBK_ID = OUTBANKID) AS OUTBANKID ");
			tmpSQL.append("  , INBANKID || '-' || (SELECT COALESCE(BRBK_NAME,'') FROM BANK_BRANCH WHERE BRBK_ID = INBANKID) AS INBANKID ");
			tmpSQL.append("  , WOBANKID || '-' || (SELECT COALESCE(BRBK_NAME,'') FROM BANK_BRANCH WHERE BRBK_ID = WOBANKID) AS WOBANKID ");
			tmpSQL.append("  , (CASE WHEN RESULTSTATUS = 'P' AND NEWRESULT = 'R' THEN '0' ELSE '1' END) AS ACCTCODE ");
			tmpSQL.append("  , TXID || '-' || COALESCE((SELECT t.TXN_NAME FROM TXN_CODE T WHERE T.TXN_ID = TXID),'') AS TXID ");
			tmpSQL.append("  , SENDERID || '-' || GETCOMPANY_ABBR(SENDERID) AS SENDERID");
			tmpSQL.append("  ,(CASE RESULTSTATUS WHEN 'A' THEN '成功' WHEN 'R' THEN '失敗' ELSE (CASE SENDERSTATUS WHEN '1' THEN '處理中' ELSE '未完成' END) END) AS RESP  ");
			tmpSQL.append("  , PFCLASS || '-' || COALESCE((SELECT B.BILL_TYPE_NAME FROM BILL_TYPE B WHERE B.BILL_TYPE_ID = PFCLASS),'') AS PFCLASS, TOLLID, CHARGETYPE, BILLFLAG, RC1, RC2, RC3, RC4, RC5, RC6 ");
			tmpSQL.append("  FROM VW_ONBLOCKTAB A ");
			tmpSQL.append((StrUtils.isEmpty(condition_1))?"" : "WHERE " + condition_1);
			if(filter_bat.equals("Y")){
				tmpSQL.append(" UNION ALL ");
				tmpSQL.append("  SELECT TRANSLATE('abcd-ef-gh op:qr:st', A.TXDT, 'abcdefghopqrst') AS TXDT ");
//				tmpSQL.append("  SELECT VARCHAR_FORMAT(NEWTXDT, 'YYYY-MM-DD HH24:MI:SS') AS TXDT ");
				tmpSQL.append("  ,NEWTXAMT AS TXAMT, OUTACCTNO , INACCTNO, STAN, TXDATE, NEWRESULT, UPDATEDT, BIZDATE , CLEARINGPHASE  ");
				tmpSQL.append("  , RECEIVERID, SENDERACQUIRE, OUTACQUIRE, INACQUIRE, WOACQUIRE, SENDERHEAD, OUTHEAD, INHEAD, WOHEAD ,FLBIZDATE ,RESULTSTATUS");
				tmpSQL.append("  , PCODE || '-' || COALESCE((SELECT EACH_TXN_NAME FROM EACH_TXN_CODE WHERE EACH_TXN_ID = PCODE),'') AS PCODE ");
				tmpSQL.append("  , SENDERBANKID || '-' || (SELECT COALESCE(BRBK_NAME,'') FROM BANK_BRANCH WHERE BRBK_ID = SENDERBANKID) AS SENDERBANKID ");
				tmpSQL.append("  , OUTBANKID || '-' || (SELECT COALESCE(BRBK_NAME,'') FROM BANK_BRANCH WHERE BRBK_ID = OUTBANKID) AS OUTBANKID ");
				tmpSQL.append("  , INBANKID || '-' || (SELECT COALESCE(BRBK_NAME,'') FROM BANK_BRANCH WHERE BRBK_ID = INBANKID) AS INBANKID ");
				tmpSQL.append("  , WOBANKID || '-' || (SELECT COALESCE(BRBK_NAME,'') FROM BANK_BRANCH WHERE BRBK_ID = WOBANKID) AS WOBANKID ");
				tmpSQL.append("  , (CASE WHEN RESULTSTATUS = 'P' AND NEWRESULT = 'R' THEN '0' ELSE '1' END) AS ACCTCODE ");
				tmpSQL.append("  , TXID || '-' || COALESCE((SELECT t.TXN_NAME FROM TXN_CODE T WHERE T.TXN_ID = TXID),'') AS TXID ");
				tmpSQL.append("  , SENDERID || '-' || GETCOMPANY_ABBR(SENDERID) AS SENDERID");
				tmpSQL.append("  ,(CASE RESULTSTATUS WHEN 'A' THEN '成功' WHEN 'R' THEN '失敗' ELSE (CASE SENDERSTATUS WHEN '1' THEN '處理中' ELSE '未完成' END) END) AS RESP  ");
				tmpSQL.append("  , PFCLASS || '-' || COALESCE((SELECT B.BILL_TYPE_NAME FROM BILL_TYPE B WHERE B.BILL_TYPE_ID = PFCLASS),'') AS PFCLASS, TOLLID, CHARGETYPE, BILLFLAG, RC1, RC2, RC3, RC4, RC5, RC6   ");
				tmpSQL.append("  FROM VW_ONBLOCKTAB A ");
				tmpSQL.append((StrUtils.isEmpty(condition_2))?"" : "WHERE " + condition_2);
			}
			tmpSQL.append(" ) ");
			sql.append(" SELECT * FROM ( ");
			sql.append("  	SELECT  ROWNUMBER() OVER( "+orderSQL+") AS ROWNUMBER , TEMP.*  ");
			sql.append(" FROM TEMP ");
			sql.append(" ) AS A    ");
			sql.append("  WHERE ROWNUMBER >= " + (Page.getStartOfPage(pageNo, pageSize) + 1) + " AND ROWNUMBER <= " + (pageNo * pageSize) + " ");
			sql.insert(0, tmpSQL.toString());
			sql.append(orderSQL);
			//先算出總計的欄位值，放到rtnMap裡面，到頁面再放到下面的總計Grid裡面
			cntSQL.append(" SELECT COALESCE(COUNT(*),0) AS NUM FROM TEMP ");
			cntSQL.insert(0, tmpSQL.toString());
//			20150504 edit by hugo req by 李建利 ，交易金額 無論成功或失敗都要計算 不可為0
//			dataSumSQL.append("		CASE RESULTSTATUS WHEN 'R' THEN 0 ELSE NEWTXAMT END ");
			sumSQL.append(" SELECT COALESCE( SUM(EACHUSER.ISNUMERIC(TXAMT)) ,0) AS TXAMT FROM TEMP ");
			sumSQL.insert(0, tmpSQL.toString());
			System.out.println("sumSQL="+sumSQL);
			String dataSumCols[] = {"TXAMT"};
			list = vw_onblocktab_Dao.dataSum(sumSQL.toString(),dataSumCols,VW_ONBLOCKTAB.class);
			for(VW_ONBLOCKTAB po:list){
				System.out.println(String.format("SUM(X.TXAMT)=%s",
					po.getTXAMT()));
			}
			rtnMap.put("dataSumList", list);
			
			System.out.println("cntSQL===>"+cntSQL.toString());
			System.out.println("sql===>"+sql.toString());
			
			String cols[] = {"TXDT", "PCODE", "SENDERBANKID", "OUTBANKID", "INBANKID", "WOBANKID", "OUTACCTNO", "INACCTNO","TXDATE","STAN", "TXAMT", "RESP", "SENDERID", "TXID" ,"PFCLASS", "TOLLID", "CHARGETYPE", "BILLFLAG","RC1", "RC2", "RC3","RC4","RC5","RC6"};
			page= vw_onblocktab_Dao.getDataIII(Integer.valueOf(pageNo), Integer.valueOf(pageSize), cntSQL.toString(), sql.toString(), cols, VW_ONBLOCKTAB.class);
			list = (List<VW_ONBLOCKTAB>) page.getResult();
			System.out.println("VW_ONBLOCKTAB.list>>"+list);
			list = list!=null&& list.size() ==0 ?null:list;
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
//			必須是按下UI的查詢才紀錄
			if(isSearch.equals("Y")){
				if(cdNumRao.equals("SENDID")){
					params.put("serchStrs", params.get("serchStrs").replace(":\"SENDID\"", ":\"發動\"") );
				}else if(cdNumRao.equals("RECVID")){
					params.put("serchStrs", params.get("serchStrs").replace(":\"RECVID\"", ":\"收受\"") );
				}
				
				if(opAction1.equals("IN")){
					params.put("serchStrs", params.get("serchStrs").replace(":\"IN\"", ":\"入帳\"") );
				}else if(opAction1.equals("OUT")){
					params.put("serchStrs", params.get("serchStrs").replace(":\"OUT\"", ":\"扣款\"") );
				}
				
				params.put("serchStrs", params.get("serchStrs").replace(":\"\"", ":\"all\"") );
				
				userlog_bo.writeLog("C", null, null, params);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rtnMap.put("total", 1);
			rtnMap.put("page", 1);
			rtnMap.put("records", 0);
			rtnMap.put("rows", new ArrayList<>());
			rtnMap.put("msg", "查詢失敗");
			userlog_bo.writeFailLog("C", rtnMap, null, null, params);
		}
		String json = JSONUtils.map2json(rtnMap) ;
		//System.out.println("json===>"+json+"<===");
		return json;
	}
	
	/**
	 * 原getResList暫時不使用
	 * @param params
	 * @return
	 */
	//20150317 交易資料查詢
	public String getResList_BAKII(Map<String, String> params){
		//日期區間(票交端為區間，銀行端僅可選單一日期 TXTIME1)
		String stadate = StrUtils.isEmpty(params.get("TXTIME1"))?"":DateTimeUtils.convertDate(params.get("TXTIME1"), "yyyyMMdd", "yyyyMMdd");
		String enddate = StrUtils.isEmpty(params.get("TXTIME2"))?"":DateTimeUtils.convertDate(params.get("TXTIME2"), "yyyyMMdd", "yyyyMMdd");
		//交易追蹤序號
		String stan = StrUtils.isEmpty(params.get("STAN"))?"":params.get("STAN").trim();
		//發動/收受選項
		String cdNumRao = StrUtils.isEmpty(params.get("CDNUMRAO"))?"":params.get("CDNUMRAO");
		//統編
		String cdNumId = StrUtils.isEmpty(params.get("CARDNUM_ID"))?"":params.get("CARDNUM_ID").trim();
		//入帳/扣款選項
		String opAction1 = StrUtils.isEmpty(params.get("opAction1"))?"":params.get("opAction1");
		//帳號
		String acctNo = StrUtils.isEmpty(params.get("USERID"))?"":params.get("USERID").trim();
		//金額
		String txamt = StrUtils.isEmpty(params.get("TXAMT"))?"":params.get("TXAMT").trim();
		//查詢角度
		String searchAspect = StrUtils.isEmpty(params.get("searchAspect"))?"":params.get("searchAspect").trim();
		//操作行
		String opbkId = StrUtils.isEmpty(params.get("OPBK_ID"))?"":params.get("OPBK_ID").trim();
		//總行
		String bgbkId = StrUtils.isEmpty(params.get("BGBK_ID"))?"":params.get("BGBK_ID").trim();
		//分行
		String brbkId = StrUtils.isEmpty(params.get("BRBK_ID"))?"":params.get("BRBK_ID").trim();
		//清算階段代號
		String clearingphase = StrUtils.isEmpty(params.get("CLEARINGPHASE"))?"":params.get("CLEARINGPHASE");
		//業務類別代號
		String bus_Type_Id = StrUtils.isEmpty(params.get("BUSINESS_TYPE_ID"))?"":params.get("BUSINESS_TYPE_ID");
		//交易結果
		String resultstatus = StrUtils.isEmpty(params.get("RESULTSTATUS"))?"":params.get("RESULTSTATUS");
		//是否包含異常資料(N表示不包含)
		String garbageData = params.get("GARBAGEDATA")==null?"N":"Y";
		int pageNo = StrUtils.isEmpty(params.get("page")) ?0:Integer.valueOf(params.get("page"));
		int pageSize = StrUtils.isEmpty(params.get("rows"))?Integer.valueOf(Arguments.getStringArg("PAGE.SIZE")):Integer.valueOf(params.get("rows"));
		String isSearch = StrUtils.isEmpty(params.get("isSearch")) ?"Y":params.get("isSearch");
		List<VW_ONBLOCKTAB> list = null;
		Map rtnMap = new HashMap();
		Page page = null;
		List<String> conditions_1 = new ArrayList<String>();
		String condition_1 = "";
		try{
			/* 20150210 HUANGPU 改以營業日(BIZDATE)查詢資料，非交易日期時間(TXDT) */
			if(StrUtils.isNotEmpty(stadate) && StrUtils.isNotEmpty(enddate)){
				conditions_1.add(" (BIZDATE >= '" + stadate + "' AND BIZDATE <= '"+enddate+"') ");
			}else if(StrUtils.isNotEmpty(stadate)){
				conditions_1.add(" BIZDATE >= '" + stadate + "' ");
			}else if(StrUtils.isNotEmpty(enddate)){
				conditions_1.add(" BIZDATE <= '" + enddate + "' ");
			}
			
			if(StrUtils.isNotEmpty(stan)){
				conditions_1.add(" STAN = '" + stan + "' ");
			}
			
			if(StrUtils.isNotEmpty(cdNumId)){
				if(cdNumRao.equals("SENDID")){
					conditions_1.add(" SENDERID = '" + cdNumId + "' ");
				}else if(cdNumRao.equals("RECVID")){
					conditions_1.add(" RECEIVERID = '" + cdNumId + "' ");
				}
			}
			if(StrUtils.isNotEmpty(acctNo)){
				if(opAction1.equals("IN")){
					conditions_1.add(" INACCTNO = '" + acctNo + "' ");
				}else if(opAction1.equals("OUT")){
					conditions_1.add(" OUTACCTNO = '" + acctNo + "' ");
				}
			}
			if(StrUtils.isNotEmpty(txamt)){
				conditions_1.add(" NEWTXAMT = ISNUMERIC('" + txamt + "') ");
			}
			if(StrUtils.isNotEmpty(searchAspect)){
				
				if(StrUtils.isNotEmpty(opbkId) && !opbkId.equals("all")){
					if(searchAspect.equals("SENDER")){
						conditions_1.add(" SENDERACQUIRE = '" + opbkId + "' ");
					}else if(searchAspect.equals("IN")){
						conditions_1.add(" INACQUIRE = '" + opbkId + "' ");
					}else if(searchAspect.equals("OUT")){
						conditions_1.add(" OUTACQUIRE = '" + opbkId + "' ");
					}else if(searchAspect.equals("WO")){
						conditions_1.add(" WOACQUIRE = '" + opbkId + "' ");
					}
				}
				if(StrUtils.isNotEmpty(bgbkId)){
					if(searchAspect.equals("SENDER")){
						conditions_1.add(" SENDERHEAD = '" + bgbkId + "' ");
					}else if(searchAspect.equals("IN")){
						conditions_1.add(" INHEAD = '" + bgbkId + "' ");
					}else if(searchAspect.equals("OUT")){
						conditions_1.add(" OUTHEAD = '" + bgbkId + "' ");
					}else if(searchAspect.equals("WO")){
						conditions_1.add(" WOHEAD = '" + bgbkId + "' ");
					}
				}
				if(StrUtils.isNotEmpty(brbkId) && !brbkId.equals("all")){
					if(searchAspect.equals("SENDER")){
						conditions_1.add(" SENDERBANKID = '" + brbkId + "' ");
					}else if(searchAspect.equals("IN")){
						conditions_1.add(" INBANKID = '" + brbkId + "' ");
					}else if(searchAspect.equals("OUT")){
						conditions_1.add(" OUTBANKID = '" + brbkId + "' ");
					}else if(searchAspect.equals("WO")){
						conditions_1.add(" WOBANKID = '" + brbkId + "' ");
					}
				}
			}else{
				if(StrUtils.isNotEmpty(opbkId) && !opbkId.equals("all")){
						conditions_1.add(" (SENDERACQUIRE = '" + opbkId + "' OR INACQUIRE = '" + opbkId + "' OR OUTACQUIRE = '" + opbkId + "' OR WOACQUIRE = '" + opbkId + "') ");
				}
				if(StrUtils.isNotEmpty(bgbkId)){
					conditions_1.add(" (SENDERHEAD = '" + bgbkId + "' OR INHEAD = '" + bgbkId + "' OR OUTHEAD = '" + bgbkId + "' OR WOHEAD = '" + bgbkId + "') ");
				}
				if(StrUtils.isNotEmpty(brbkId) && !brbkId.equals("all")){
					conditions_1.add(" (SENDERBANKID = '" + brbkId + "' OR INBANKID = '" + brbkId + "' OR OUTBANKID = '" + "' OR WOBANKID = '" + brbkId + "') ");
				}
			}
			if(StrUtils.isNotEmpty(clearingphase)){
				conditions_1.add(" CLEARINGPHASE = '" + clearingphase + "' ");
			}
			if(StrUtils.isNotEmpty(bus_Type_Id)){
				conditions_1.add(" SUBSTR(PCODE,1,2) LIKE SUBSTR('" + bus_Type_Id + "',1,2) ");
			}
			//(U=處理中需另外轉換條件)
			if(StrUtils.isNotEmpty(resultstatus)){
				if(resultstatus.equals("U")){
					conditions_1.add(" (RESULTSTATUS = 'P' AND SENDERSTATUS = '1') ");
				}else if(resultstatus.equals("AP")){
					conditions_1.add(" ( (RESULTSTATUS IN ('A', 'P')) AND SENDERSTATUS <> '1') ");
				}else{
					conditions_1.add(" (RESULTSTATUS = '" + resultstatus + "' AND SENDERSTATUS <> '1') ");
				}
			}
			//不包含異常資料才需下條件過濾
			if(garbageData.equals("N")){
				conditions_1.add(" COALESCE(GARBAGEDATA,'') <> '*' ");
			}
			//不包含整批資料
//			if(filter_bat.equals("N")){
//				conditions_1.add(" COALESCE(FLBIZDATE,'') = '' ");
//			}
			
			condition_1 = combine(conditions_1);
			
			StringBuffer fromAndWhere_core = new StringBuffer();
			fromAndWhere_core.append("SELECT ROWNUMBER() OVER( ");
			if(StrUtils.isNotEmpty(params.get("sidx"))){
				if("TXDT".equalsIgnoreCase(params.get("sidx"))){
					fromAndWhere_core.append("ORDER BY NEWTXDT " + params.get("sord"));
				}
				else if("TXAMT".equalsIgnoreCase(params.get("sidx"))){
					fromAndWhere_core.append("ORDER BY NEWTXAMT " + params.get("sord"));
				}
				else if("RESP".equalsIgnoreCase(params.get("sidx"))){
					fromAndWhere_core.append("ORDER BY RESULTSTATUS " + params.get("sord"));
				}
				else{
					fromAndWhere_core.append("ORDER BY " + params.get("sidx") + " "+params.get("sord"));
				}
			}
			fromAndWhere_core.append(") AS ROWNUMBER, NEWTXDT, PCODE, SENDERBANKID, OUTBANKID, INBANKID, WOBANKID, NEWTXAMT, OUTACCTNO, INACCTNO, STAN, TXDATE, NEWRESULT, UPDATEDT, ");
			fromAndWhere_core.append("(CASE WHEN RESULTSTATUS = 'P' AND NEWRESULT = 'R' THEN '0' ELSE '1' END) AS ACCTCODE, BIZDATE, CLEARINGPHASE, TXID, ");
			fromAndWhere_core.append("SENDERID, RECEIVERID, SENDERACQUIRE, OUTACQUIRE, INACQUIRE, SENDERHEAD, WOACQUIRE, OUTHEAD, INHEAD, WOHEAD, RESULTSTATUS, SENDERSTATUS ");
			fromAndWhere_core.append("FROM VW_ONBLOCKTAB A ");
			fromAndWhere_core.append((StrUtils.isEmpty(condition_1))?"" : "WHERE " + condition_1);
			
			StringBuffer fromAndWhere = new StringBuffer();
			fromAndWhere.append("FROM (" + fromAndWhere_core + ") AS C WHERE ROWNUMBER >= " + (Page.getStartOfPage(pageNo, pageSize) + 1) + " AND ROWNUMBER <= " + pageNo * pageSize + " ");
			StringBuffer fromAndWhere_all = new StringBuffer();
			fromAndWhere_all.append("FROM (" + fromAndWhere_core + ") AS C ");
			
			//先算出總計的欄位值，放到rtnMap裡面，到頁面再放到下面的總計Grid裡面
			StringBuffer dataSumSQL = new StringBuffer();
			dataSumSQL.append("SELECT SUM( ");
//			20150504 edit by hugo req by 李建利 ，交易金額 無論成功或失敗都要計算 不可為0
//			dataSumSQL.append("		CASE RESULTSTATUS WHEN 'R' THEN 0 ELSE NEWTXAMT END ");
			dataSumSQL.append("		CASE RESULTSTATUS WHEN 'R' THEN NEWTXAMT ELSE NEWTXAMT END ");
			dataSumSQL.append(") AS TXAMT ");
			dataSumSQL.append(fromAndWhere_all);
			System.out.println("dataSumSQL="+dataSumSQL);
			
			String dataSumCols[] = {"TXAMT"};
			list = vw_onblocktab_Dao.dataSum(dataSumSQL.toString(),dataSumCols,VW_ONBLOCKTAB.class);
			for(VW_ONBLOCKTAB po:list){
				System.out.println(String.format("SUM(X.TXAMT)=%s",
						po.getTXAMT()));
			}
			rtnMap.put("dataSumList", list);
			
			StringBuffer countQuery = new StringBuffer();
			countQuery.append("SELECT COUNT(*) AS NUM ");
			countQuery.append(fromAndWhere_all);
			System.out.println("countQuery===>"+countQuery.toString());
			
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT VARCHAR_FORMAT(C.NEWTXDT, 'YYYY-MM-DD HH24:MI:SS') AS TXDT, "); 
			sql.append("C.PCODE || '-' || COALESCE((SELECT EACH_TXN_NAME FROM EACH_TXN_CODE WHERE EACH_TXN_ID = C.PCODE),'') AS PCODE, "); 
			sql.append("C.SENDERBANKID || '-' || (SELECT COALESCE(BRBK_NAME,'') FROM BANK_BRANCH WHERE BRBK_ID = C.SENDERBANKID) AS SENDERBANKID, "); 
			sql.append("C.OUTBANKID || '-' || (SELECT COALESCE(BRBK_NAME,'') FROM BANK_BRANCH WHERE BRBK_ID = C.OUTBANKID) AS OUTBANKID, "); 
			sql.append("C.INBANKID || '-' || (SELECT COALESCE(BRBK_NAME,'') FROM BANK_BRANCH WHERE BRBK_ID = C.INBANKID) AS INBANKID, ");
			sql.append("C.WOBANKID || '-' || (SELECT COALESCE(BRBK_NAME,'') FROM BANK_BRANCH WHERE BRBK_ID = C.WOBANKID) AS WOBANKID, "); 
			sql.append("C.NEWTXAMT AS TXAMT, C.OUTACCTNO, C.INACCTNO, C.STAN, C.TXDATE, UPDATEDT, BIZDATE, CLEARINGPHASE, ");
			
			//sql.append("C.SENDERID || '-' || COALESCE((SELECT COMPANY_ABBR_NAME FROM (SELECT COMPANY_ID, TXN_ID, SND_BRBK_ID, COMPANY_ABBR_NAME FROM SD_COMPANY_PROFILE UNION SELECT COMPANY_ID, TXN_ID, SND_BRBK_ID, COMPANY_ABBR_NAME FROM SC_COMPANY_PROFILE) WHERE COMPANY_ID = C.SENDERID AND TXN_ID = C.TXID AND SND_BRBK_ID = C.SENDERBANKID),'') AS SENDERID, ");
			sql.append("C.SENDERID || '-' || GETCOMPANY_ABBR(C.SENDERID) AS SENDERID, ");
			
			sql.append("C.TXID || '-' || COALESCE((SELECT TXN_NAME FROM TXN_CODE WHERE TXN_ID = C.TXID),'') AS TXID, ");
			sql.append("(CASE C.RESULTSTATUS WHEN 'A' THEN '成功' WHEN 'R' THEN '失敗' ELSE (CASE SENDERSTATUS WHEN '1' THEN '處理中' ELSE '未完成' END) END) AS RESP ");
			sql.append(fromAndWhere);
			if(StrUtils.isNotEmpty(params.get("sidx"))){
				if(!"ACCTCODE".equalsIgnoreCase(params.get("sidx"))){
					if("SENDERID".equalsIgnoreCase(params.get("sidx"))){
						sql.append("ORDER BY C." + params.get("sidx"));
					}else{
						sql.append("ORDER BY " + params.get("sidx"));
					}
					if(params.get("sidx").contains("ASC") || params.get("sidx").contains("DESC") ||
							params.get("sidx").contains("asc") || params.get("sidx").contains("desc")){
					}else{
						sql.append(" " + params.get("sord"));
					}
				}
			}
			System.out.println("sql===>"+sql.toString());
			
			String cols[] = {"TXDT", "PCODE", "SENDERBANKID", "WOBANKID", "OUTBANKID", "INBANKID", "OUTACCTNO", "INACCTNO","TXDATE","STAN", "TXAMT", "RESP", "SENDERID", "TXID"};
			page= vw_onblocktab_Dao.getDataIII(Integer.valueOf(pageNo), Integer.valueOf(pageSize), countQuery.toString(), sql.toString(), cols, VW_ONBLOCKTAB.class);
			list = (List<VW_ONBLOCKTAB>) page.getResult();
			System.out.println("VW_ONBLOCKTAB.list>>"+list);
			list = list!=null&& list.size() ==0 ?null:list;
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
//			必須是按下UI的查詢才紀錄
			if(isSearch.equals("Y")){
				userlog_bo.writeLog("C", null, null, params);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rtnMap.put("total", 1);
			rtnMap.put("page", 1);
			rtnMap.put("records", 0);
			rtnMap.put("rows", new ArrayList<>());
			rtnMap.put("msg", "查詢失敗");
			userlog_bo.writeFailLog("C", rtnMap, null, null, params);
		}
		String json = JSONUtils.map2json(rtnMap) ;
		//System.out.println("json===>"+json+"<===");
		return json;
	}
	
	
	
	//交易資料查詢-查詢功能 --20150317 備份(已不使用)
	public String getResList_BAK(Map<String, String> params){
		//日期區間(票交端為區間，銀行端僅可選單一日期 TXTIME1)
		String stadate = StrUtils.isEmpty(params.get("TXTIME1"))?"":DateTimeUtils.convertDate(params.get("TXTIME1"), "yyyyMMdd", "yyyyMMdd");
		String enddate = StrUtils.isEmpty(params.get("TXTIME2"))?"":DateTimeUtils.convertDate(params.get("TXTIME2"), "yyyyMMdd", "yyyyMMdd");
		//交易追蹤序號
		String stan = StrUtils.isEmpty(params.get("STAN"))?"":params.get("STAN").trim();
		//發動/收受選項
		String cdNumRao = StrUtils.isEmpty(params.get("CDNUMRAO"))?"":params.get("CDNUMRAO");
		//統編
		String cdNumId = StrUtils.isEmpty(params.get("CARDNUM_ID"))?"":params.get("CARDNUM_ID").trim();
		//入帳/扣款選項
		String opAction1 = StrUtils.isEmpty(params.get("opAction1"))?"":params.get("opAction1");
		//帳號
		String acctNo = StrUtils.isEmpty(params.get("USERID"))?"":params.get("USERID").trim();
		//金額
		String txamt = StrUtils.isEmpty(params.get("TXAMT"))?"":params.get("TXAMT").trim();
		//查詢角度
		String searchAspect = StrUtils.isEmpty(params.get("searchAspect"))?"":params.get("searchAspect").trim();
		//操作行
		String opbkId = StrUtils.isEmpty(params.get("USER_COMPANY"))?"":params.get("USER_COMPANY").trim();
		//總行
		String bgbkId = StrUtils.isEmpty(params.get("BGBK_ID"))?"":params.get("BGBK_ID").trim();
		//分行
		String brbkId = StrUtils.isEmpty(params.get("BRBK_ID"))?"":params.get("BRBK_ID").trim();
		//清算階段代號
		String clearingphase = StrUtils.isEmpty(params.get("CLEARINGPHASE"))?"":params.get("CLEARINGPHASE");
		//業務類別代號
		String bus_Type_Id = StrUtils.isEmpty(params.get("BUSINESS_TYPE_ID"))?"":params.get("BUSINESS_TYPE_ID");
		//交易結果
		String resultstatus = StrUtils.isEmpty(params.get("RESULTSTATUS"))?"":params.get("RESULTSTATUS");
		int pageNo = StrUtils.isEmpty(params.get("page")) ?0:Integer.valueOf(params.get("page"));
		int pageSize = StrUtils.isEmpty(params.get("rows"))?Integer.valueOf(Arguments.getStringArg("PAGE.SIZE")):Integer.valueOf(params.get("rows"));
		
		List<VW_ONBLOCKTAB> list = null;
		Map rtnMap = new HashMap();
		Page page = null;
		List<String> conditions_1 = new ArrayList<String>();
		List<String> conditions_2 = new ArrayList<String>();
		List<String> conditions_3 = new ArrayList<String>();
		String condition_1 = "", condition_2 = "", condition_3 = "";
		try {
			list = new ArrayList<VW_ONBLOCKTAB>();
			/* 20150210 HUANGPU 改以營業日(BIZDATE)查詢資料，非交易日期時間(TXDT) */
			if(StrUtils.isNotEmpty(stadate) && StrUtils.isNotEmpty(enddate)){
				conditions_1.add(" (BIZDATE >= '" + stadate + "' AND BIZDATE <= '"+enddate+"') ");
				conditions_2.add(" (NEWBIZDATE >= '" + stadate + "' AND NEWBIZDATE <= '"+enddate+"') ");
			}else if(StrUtils.isNotEmpty(stadate)){
				conditions_1.add(" BIZDATE >= '" + stadate + "' ");
				conditions_2.add(" NEWBIZDATE >= '" + stadate + "' ");
			}else if(StrUtils.isNotEmpty(enddate)){
				conditions_1.add(" BIZDATE <= '" + enddate + "' ");
				conditions_2.add(" NEWBIZDATE <= '" + enddate + "' ");
			}
			
			if(StrUtils.isNotEmpty(stan)){
				conditions_1.add(" STAN = '" + stan + "' ");
				conditions_2.add(" STAN = '" + stan + "' ");
			}
			if(StrUtils.isNotEmpty(cdNumId)){
				if(cdNumRao.equals("SENDID")){
					conditions_1.add(" SENDERID = '" + cdNumId + "' ");
					conditions_2.add(" SENDERID = '" + cdNumId + "' ");
				}else if(cdNumRao.equals("RECVID")){
					conditions_1.add(" RECEIVERID = '" + cdNumId + "' ");
					conditions_2.add(" RECEIVERID = '" + cdNumId + "' ");
				}
			}
			if(StrUtils.isNotEmpty(acctNo)){
				if(opAction1.equals("IN")){
					conditions_1.add(" INACCTNO = '" + acctNo + "' ");
					conditions_2.add(" INACCTNO = '" + acctNo + "' ");
				}else if(opAction1.equals("OUT")){
					conditions_1.add(" OUTACCTNO = '" + acctNo + "' ");
					conditions_2.add(" OUTACCTNO = '" + acctNo + "' ");
				}
			}
			if(StrUtils.isNotEmpty(txamt)){
				conditions_1.add(" NEWTXAMT = ISNUMERIC('" + txamt + "') ");
				conditions_2.add(" NEWTXAMT = ISNUMERIC('" + txamt + "') ");
			}
			if(StrUtils.isNotEmpty(searchAspect)){
				if(StrUtils.isNotEmpty(opbkId) && !opbkId.equals("all")){
					if(searchAspect.equals("SENDER")){
						conditions_1.add(" SENDERACQUIRE = '" + opbkId + "' ");
						conditions_2.add(" SENDERACQUIRE = '" + opbkId + "' ");
					}else if(searchAspect.equals("IN")){
						conditions_1.add(" INACQUIRE = '" + opbkId + "' ");
						conditions_2.add(" INACQUIRE = '" + opbkId + "' ");
					}else if(searchAspect.equals("OUT")){
						conditions_1.add(" OUTACQUIRE = '" + opbkId + "' ");
						conditions_2.add(" OUTACQUIRE = '" + opbkId + "' ");
					}else if(searchAspect.equals("WO")){
						conditions_1.add(" WOACQUIRE = '" + opbkId + "' ");
						conditions_2.add(" WOACQUIRE = '" + opbkId + "' ");
					}
				}
				if(StrUtils.isNotEmpty(bgbkId)){
					if(searchAspect.equals("SENDER")){
						conditions_1.add(" SENDERHEAD = '" + bgbkId + "' ");
						conditions_2.add(" SENDERHEAD = '" + bgbkId + "' ");
					}else if(searchAspect.equals("IN")){
						conditions_1.add(" INHEAD = '" + bgbkId + "' ");
						conditions_2.add(" INHEAD = '" + bgbkId + "' ");
					}else if(searchAspect.equals("OUT")){
						conditions_1.add(" OUTHEAD = '" + bgbkId + "' ");
						conditions_2.add(" OUTHEAD = '" + bgbkId + "' ");
					}else if(searchAspect.equals("WO")){
						conditions_1.add(" WOHEAD = '" + bgbkId + "' ");
						conditions_2.add(" WOHEAD = '" + bgbkId + "' ");
					}
				}
				if(StrUtils.isNotEmpty(brbkId) && !brbkId.equals("all")){
					if(searchAspect.equals("SENDER")){
						conditions_1.add(" SENDERBANKID = '" + brbkId + "' ");
						conditions_2.add(" SENDERBANKID = '" + brbkId + "' ");
					}else if(searchAspect.equals("IN")){
						conditions_1.add(" INBANKID = '" + brbkId + "' ");
						conditions_2.add(" INBANKID = '" + brbkId + "' ");
					}else if(searchAspect.equals("OUT")){
						conditions_1.add(" OUTBANKID = '" + brbkId + "' ");
						conditions_2.add(" OUTBANKID = '" + brbkId + "' ");
					}else if(searchAspect.equals("WO")){
						conditions_1.add(" WOBANKID = '" + brbkId + "' ");
						conditions_2.add(" WOBANKID = '" + brbkId + "' ");
					}
				}
			}else{
				if(StrUtils.isNotEmpty(opbkId) && !opbkId.equals("all")){
					conditions_1.add(" (SENDERACQUIRE = '" + opbkId + "' OR INACQUIRE = '" + opbkId + "' OR OUTACQUIRE = '" + opbkId + "' OR WOACQUIRE = '" + opbkId + "') ");
					conditions_2.add(" (SENDERACQUIRE = '" + opbkId + "' OR INACQUIRE = '" + opbkId + "' OR OUTACQUIRE = '" + opbkId + "' OR WOACQUIRE = '" + opbkId + "') ");
				}
				if(StrUtils.isNotEmpty(bgbkId)){
					conditions_1.add(" (SENDERHEAD = '" + bgbkId + "' OR INHEAD = '" + bgbkId + "' OR OUTHEAD = '" + bgbkId + "' OR WOHEAD = '" + bgbkId + "') ");
					conditions_2.add(" (SENDERHEAD = '" + bgbkId + "' OR INHEAD = '" + bgbkId + "' OR OUTHEAD = '" + bgbkId + "' OR WOHEAD = '" + bgbkId + "') ");
				}
				if(StrUtils.isNotEmpty(brbkId) && !brbkId.equals("all")){
					conditions_1.add(" (SENDERBANKID = '" + brbkId + "' OR INBANKID = '" + brbkId + "' OR OUTBANKID = '" + brbkId + "' OR WOBANKID = '" + brbkId + "') ");
					conditions_2.add(" (SENDERBANKID = '" + brbkId + "' OR INBANKID = '" + brbkId + "' OR OUTBANKID = '" + brbkId + "' OR WOBANKID = '" + brbkId + "') ");
				}
			}
			if(StrUtils.isNotEmpty(clearingphase)){
				conditions_1.add(" CLEARINGPHASE = '" + clearingphase + "' ");
				conditions_2.add(" NEWCLRPHASE = '" + clearingphase + "' ");
			}
			if(StrUtils.isNotEmpty(bus_Type_Id)){
				conditions_3.add(" B.BUSINESS_TYPE_ID = '" + bus_Type_Id + "' ");
			}
			//(U=處理中需另外轉換條件)
			if(StrUtils.isNotEmpty(resultstatus)){
				if(resultstatus.equals("U")){
					conditions_1.add(" (NEWRESULT = 'P' AND SENDERSTATUS = '1') ");
					conditions_2.add(" (NEWRESULT = 'P' AND SENDERSTATUS = '1') ");
				}else{
					conditions_1.add(" (NEWRESULT = '" + resultstatus + "' AND SENDERSTATUS <> '1') ");
					conditions_2.add(" (NEWRESULT = '" + resultstatus + "' AND SENDERSTATUS <> '1') ");
				}
			}
			condition_1 = combine(conditions_1);
			condition_2 = combine(conditions_2);
			condition_3 = combine(conditions_3);
			
			StringBuffer fromAndWhere_core = new StringBuffer();
			fromAndWhere_core.append("FROM ( ");
			fromAndWhere_core.append("    SELECT NEWTXDT, PCODE, SENDERBANKID, WOBANKID, OUTBANKID, INBANKID, NEWTXAMT, OUTACCTNO, INACCTNO, STAN, TXDATE, NEWRESULT, UPDATEDT, "); 	 
			fromAndWhere_core.append("    (CASE WHEN RESULTSTATUS = 'P' AND NEWRESULT = 'R' THEN '0' ELSE '1' END) AS ACCTCODE, BIZDATE, CLEARINGPHASE, "); 
			fromAndWhere_core.append("    SENDERID, RECEIVERID, SENDERACQUIRE, WOACQUIRE, OUTACQUIRE, INACQUIRE, SENDERHEAD, WOHEAD, OUTHEAD, INHEAD, RESULTSTATUS, SENDERSTATUS "); 
			fromAndWhere_core.append("    FROM VW_ONBLOCKTAB ");
			fromAndWhere_core.append("    " + (StrUtils.isNotEmpty(condition_1)? "WHERE " + condition_1 : ""));
			fromAndWhere_core.append("    UNION ALL ");
			fromAndWhere_core.append("    SELECT NEWTXDT, PCODE, SENDERBANKID, WOBANKID, OUTBANKID, INBANKID, NEWTXAMT, OUTACCTNO, INACCTNO, STAN, TXDATE, NEWRESULT, UPDATEDT, "); 	 
			fromAndWhere_core.append("    (CASE WHEN RESULTSTATUS = 'P' AND NEWRESULT = 'R' THEN '0' ELSE '1' END) AS ACCTCODE, NEWBIZDATE, NEWCLRPHASE, "); 
			fromAndWhere_core.append("    SENDERID, RECEIVERID, SENDERACQUIRE, WOACQUIRE, OUTACQUIRE, INACQUIRE, SENDERHEAD, WOHEAD, OUTHEAD, INHEAD, RESULTSTATUS, SENDERSTATUS "); 
			fromAndWhere_core.append("    FROM VW_ONPENDING_EC ");
			fromAndWhere_core.append("    " + (StrUtils.isNotEmpty(condition_2)? "WHERE " + condition_2 : ""));
			fromAndWhere_core.append(") AS A LEFT JOIN EACH_TXN_CODE B ON A.PCODE = B.EACH_TXN_ID ");
			fromAndWhere_core.append("" + (StrUtils.isNotEmpty(condition_3)? "WHERE " + condition_3 : ""));
			
			StringBuffer fromAndWhere = new StringBuffer();
			fromAndWhere.append("FROM ( ");
			fromAndWhere.append("SELECT * FROM ( ");
		    fromAndWhere.append("    SELECT ROWNUMBER() OVER( ");
		    if(StrUtils.isNotEmpty(params.get("sidx"))){
				if("TXDT".equalsIgnoreCase(params.get("sidx"))){
					fromAndWhere.append("ORDER BY NEWTXDT " + params.get("sord"));
				}
				else if("TXAMT".equalsIgnoreCase(params.get("sidx"))){
					fromAndWhere.append("ORDER BY NEWTXAMT " + params.get("sord"));
				}
				else if("RESP".equalsIgnoreCase(params.get("sidx"))){
					fromAndWhere.append("ORDER BY NEWRESULT " + params.get("sord"));
				}
				else{
					fromAndWhere.append("ORDER BY " + params.get("sidx") + " "+params.get("sord"));
				}
			}
		    fromAndWhere.append(") AS ROWNUMBER, A.*, B.EACH_TXN_NAME ");
		    fromAndWhere.append(fromAndWhere_core);
		    fromAndWhere.append(") WHERE ROWNUMBER >= " + (Page.getStartOfPage(pageNo, pageSize) + 1) + " AND ROWNUMBER <= " + pageNo * pageSize + " ");
		    fromAndWhere.append(") AS C ");
			
			//先算出總計的欄位值，放到rtnMap裡面，到頁面再放到下面的總計Grid裡面
			StringBuffer dataSumSQL = new StringBuffer();
			dataSumSQL.append("SELECT SUM( ");
			dataSumSQL.append("		CASE NEWRESULT WHEN 'R' THEN ( ");
			dataSumSQL.append("				CASE ACCTCODE WHEN '0' THEN ( ");
			dataSumSQL.append("						CASE RESULTSTATUS WHEN 'P' THEN NEWTXAMT ELSE 0 END ");
			dataSumSQL.append("				) ELSE 0 END ");
			dataSumSQL.append("		) ELSE NEWTXAMT END ");
			dataSumSQL.append(") AS TXAMT ");
			dataSumSQL.append(fromAndWhere_core);
			System.out.println("dataSumSQL="+dataSumSQL);
			
			String dataSumCols[] = {"TXAMT"};
			list = vw_onblocktab_Dao.dataSum(dataSumSQL.toString(),dataSumCols,VW_ONBLOCKTAB.class);
			for(VW_ONBLOCKTAB po:list){
				System.out.println(String.format("SUM(X.TXAMT)=%s",
					po.getTXAMT()));
			}
			rtnMap.put("dataSumList", list);
			
			StringBuffer countQuery = new StringBuffer();
			countQuery.append("SELECT COUNT(*) AS NUM ");
			countQuery.append(fromAndWhere_core);
			System.out.println("countQuery===>"+countQuery.toString());
			
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT VARCHAR_FORMAT(C.NEWTXDT, 'YYYY-MM-DD HH24:MI:SS') AS TXDT, ");
			sql.append("C.PCODE || '-' || COALESCE(C.EACH_TXN_NAME,'') AS PCODE, ");
			sql.append("C.SENDERBANKID || '-' || (SELECT COALESCE(BRBK_NAME,'') FROM BANK_BRANCH WHERE BRBK_ID = C.SENDERBANKID) AS SENDERBANKID, "); 
			sql.append("C.OUTBANKID || '-' || (SELECT COALESCE(BRBK_NAME,'') FROM BANK_BRANCH WHERE BRBK_ID = C.OUTBANKID) AS OUTBANKID, "); 
			sql.append("C.INBANKID || '-' || (SELECT COALESCE(BRBK_NAME,'') FROM BANK_BRANCH WHERE BRBK_ID = C.INBANKID) AS INBANKID, ");
			sql.append("C.WOBANKID || '-' || (SELECT COALESCE(BRBK_NAME,'') FROM BANK_BRANCH WHERE BRBK_ID = C.WOBANKID) AS WOBANKID, ");
			sql.append("C.NEWTXAMT AS TXAMT, C.OUTACCTNO, C.INACCTNO, C.STAN, C.TXDATE, UPDATEDT, BIZDATE, CLEARINGPHASE, ");
			sql.append("(CASE C.NEWRESULT WHEN 'A' THEN '成功' WHEN 'R' THEN '失敗' ELSE (CASE SENDERSTATUS WHEN '1' THEN '處理中' ELSE '未完成' END) END) AS RESP, ");
			sql.append("(CASE ACCTCODE WHEN '0' THEN '沖正' ELSE '一般' END) AS ACCTCODE ");
			sql.append(fromAndWhere);
			if(StrUtils.isNotEmpty(params.get("sidx"))){
				if(!"ACCTCODE".equalsIgnoreCase(params.get("sidx"))){
					sql.append("ORDER BY " + params.get("sidx"));
					if(params.get("sidx").contains("ASC") || params.get("sidx").contains("DESC") ||
					   params.get("sidx").contains("asc") || params.get("sidx").contains("desc")){
					}else{
						sql.append(" " + params.get("sord"));
					}
				}
			}
			System.out.println("sql===>"+sql.toString());
			
			String cols[] = {"TXDT", "PCODE", "SENDERBANKID", "WOBANKID", "OUTBANKID", "INBANKID", "OUTACCTNO", "INACCTNO","TXDATE","STAN", "TXAMT", "RESP", "ACCTCODE"};
			page= vw_onblocktab_Dao.getDataIII(Integer.valueOf(pageNo), Integer.valueOf(pageSize), countQuery.toString(), sql.toString(), cols, VW_ONBLOCKTAB.class);
			list = (List<VW_ONBLOCKTAB>) page.getResult();
			System.out.println("VW_ONBLOCKTAB.list>>"+list);
			list = list!=null&& list.size() ==0 ?null:list;
		} catch (Exception e) {
			// TODO Auto-generated catch block
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
		//System.out.println("json===>"+json+"<===");
		return json;
	}	
	//檢視明細
	public Map showDetail(String txdate,String stan){
		
		Map po = null;
		Map rtnMap = new HashMap();
		String condition ="";
		try {
			txdate = StrUtils.isEmpty(txdate)?"":txdate;
			stan = StrUtils.isEmpty(stan)?"":stan;
			
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT TRANSLATE('abcd-ef-gh', VARCHAR(A.TXDATE), 'abcdefgh') AS TXDATE,VARCHAR(A.STAN) AS STAN,VARCHAR_FORMAT(NEWTXDT, 'YYYY-MM-DD HH24:MI:SS') AS TXDT,A.PCODE || '-' || ETC.EACH_TXN_NAME AS PCODE_DESC, ");
//20151023 edit by hugo req by UAT-20151022-02
//			sql.append("A.SENDERBANK || '-' || (SELECT B.BGBK_NAME FROM BANK_GROUP B WHERE A.SENDERBANK = B.BGBK_ID) AS SENDERBANK_NAME, ");
//			sql.append("A.RECEIVERBANK || '-' || (SELECT B.BGBK_NAME FROM BANK_GROUP B WHERE A.RECEIVERBANK = B.BGBK_ID) AS RECEIVERBANK_NAME, ");
			sql.append("A.SENDERBANK || '-' || COALESCE((SELECT B.BRBK_NAME FROM BANK_BRANCH B WHERE A.SENDERBANK = B.BRBK_ID),'') AS SENDERBANK_NAME, ");
			sql.append("A.RECEIVERBANK || '-' || COALESCE((SELECT B.BRBK_NAME FROM BANK_BRANCH B WHERE A.RECEIVERBANK = B.BRBK_ID),'') AS RECEIVERBANK_NAME, ");
			sql.append("(SELECT B.ERR_DESC FROM TXN_ERROR_CODE B WHERE A.CONRESULTCODE = B.ERROR_ID) AS CONRESULTCODE_DESC,A.ACCTCODE, ");
			sql.append("A.SENDERCLEARING || '-' || (SELECT B.BGBK_NAME FROM BANK_GROUP B WHERE A.SENDERCLEARING = B.BGBK_ID) AS SENDERCLEARING_NAME, ");
			sql.append("A.INCLEARING || '-' || (SELECT B.BGBK_NAME FROM BANK_GROUP B WHERE A.INCLEARING = B.BGBK_ID) AS INCLEARING_NAME, ");
			sql.append("A.OUTCLEARING || '-' || (SELECT B.BGBK_NAME FROM BANK_GROUP B WHERE A.OUTCLEARING = B.BGBK_ID) AS OUTCLEARING_NAME, ");
			sql.append("A.SENDERACQUIRE || '-' || (SELECT B.BGBK_NAME FROM BANK_GROUP B WHERE A.SENDERACQUIRE = B.BGBK_ID) AS SENDERACQUIRE_NAME, ");
			sql.append("A.INACQUIRE || '-' || (SELECT B.BGBK_NAME FROM BANK_GROUP B WHERE A.INACQUIRE = B.BGBK_ID) AS INACQUIRE_NAME, ");
			sql.append("A.OUTACQUIRE || '-' || (SELECT B.BGBK_NAME FROM BANK_GROUP B WHERE A.OUTACQUIRE = B.BGBK_ID) AS OUTACQUIRE_NAME, ");
			sql.append("A.WOACQUIRE || '-' || (SELECT B.BGBK_NAME FROM BANK_GROUP B WHERE A.WOACQUIRE = B.BGBK_ID) AS WOACQUIRE_NAME, ");
			sql.append("A.SENDERHEAD || '-' || (SELECT B.BGBK_NAME FROM BANK_GROUP B WHERE A.SENDERHEAD = B.BGBK_ID) AS SENDERHEAD_NAME, ");
			sql.append("A.INHEAD || '-' || (SELECT B.BGBK_NAME FROM BANK_GROUP B WHERE A.INHEAD = B.BGBK_ID) AS INHEAD_NAME, ");
			sql.append("A.OUTHEAD || '-' || (SELECT B.BGBK_NAME FROM BANK_GROUP B WHERE A.OUTHEAD = B.BGBK_ID) AS OUTHEAD_NAME, ");
			sql.append("A.WOHEAD || '-' || (SELECT B.BGBK_NAME FROM BANK_GROUP B WHERE A.WOHEAD = B.BGBK_ID) AS WOHEAD_NAME, ");
			sql.append("(CASE A.RESULTSTATUS WHEN 'R' THEN 0 WHEN 'A' THEN A.NEWSENDERFEE ELSE (CASE COALESCE(OP.RESULTCODE,'00') WHEN '00' THEN A.NEWSENDERFEE ELSE (CASE A.ACCTCODE WHEN '0' THEN A.NEWSENDERFEE ELSE 0 END) END) END) AS NEWSENDERFEE, ");
			sql.append("(CASE A.RESULTSTATUS WHEN 'R' THEN 0 WHEN 'A' THEN A.NEWINFEE ELSE (CASE COALESCE(OP.RESULTCODE,'00') WHEN '00' THEN A.NEWINFEE ELSE (CASE A.ACCTCODE WHEN '0' THEN A.NEWINFEE ELSE 0 END) END) END) AS NEWINFEE, ");
			sql.append("(CASE A.RESULTSTATUS WHEN 'R' THEN 0 WHEN 'A' THEN A.NEWOUTFEE ELSE (CASE COALESCE(OP.RESULTCODE,'00') WHEN '00' THEN A.NEWOUTFEE ELSE (CASE A.ACCTCODE WHEN '0' THEN A.NEWOUTFEE ELSE 0 END) END) END) AS NEWOUTFEE, ");
			sql.append("(CASE A.RESULTSTATUS WHEN 'R' THEN 0 WHEN 'A' THEN A.NEWWOFEE ELSE (CASE COALESCE(OP.RESULTCODE,'00') WHEN '00' THEN A.NEWWOFEE ELSE (CASE A.ACCTCODE WHEN '0' THEN A.NEWWOFEE ELSE 0 END) END) END) AS NEWWOFEE, ");
			sql.append("(CASE A.RESULTSTATUS WHEN 'R' THEN 0 WHEN 'A' THEN A.NEWEACHFEE ELSE (CASE COALESCE(OP.RESULTCODE,'00') WHEN '00' THEN A.NEWEACHFEE ELSE (CASE A.ACCTCODE WHEN '0' THEN A.NEWEACHFEE ELSE 0 END) END) END) AS NEWEACHFEE, ");
			//20220210新增
			sql.append("(CASE A.RESULTSTATUS WHEN 'R' THEN 0 WHEN 'A' THEN A.NEWEXTENDFEE ELSE (CASE COALESCE(OP.RESULTCODE,'00') WHEN '00' THEN A.NEWEXTENDFEE ELSE (CASE A.ACCTCODE WHEN '0' THEN A.NEWEXTENDFEE ELSE 0 END) END) END) AS NEWEXTENDFEE, ");
			sql.append(" A.EXTENDFEE AS EXTENDFEE , ");
			//20220210新增end
			//20200824新增start
			sql.append("(CASE A.RESULTSTATUS WHEN 'R' THEN 0 WHEN 'A' THEN A.SENDERFEE_NW ELSE (CASE COALESCE(OP.RESULTCODE,'00') WHEN '00' THEN A.SENDERFEE_NW ELSE (CASE A.ACCTCODE WHEN '0' THEN A.SENDERFEE_NW ELSE 0 END) END) END) AS NEWSENDERFEE_NW, ");
			sql.append("(CASE A.RESULTSTATUS WHEN 'R' THEN 0 WHEN 'A' THEN A.INFEE_NW ELSE (CASE COALESCE(OP.RESULTCODE,'00') WHEN '00' THEN A.INFEE_NW ELSE (CASE A.ACCTCODE WHEN '0' THEN A.INFEE_NW ELSE 0 END) END) END) AS NEWINFEE_NW, ");
			sql.append("(CASE A.RESULTSTATUS WHEN 'R' THEN 0 WHEN 'A' THEN A.OUTFEE_NW ELSE (CASE COALESCE(OP.RESULTCODE,'00') WHEN '00' THEN A.OUTFEE_NW ELSE (CASE A.ACCTCODE WHEN '0' THEN A.OUTFEE_NW ELSE 0 END) END) END) AS NEWOUTFEE_NW, ");
			sql.append("(CASE A.RESULTSTATUS WHEN 'R' THEN 0 WHEN 'A' THEN A.WOFEE_NW ELSE (CASE COALESCE(OP.RESULTCODE,'00') WHEN '00' THEN A.WOFEE_NW ELSE (CASE A.ACCTCODE WHEN '0' THEN A.WOFEE_NW ELSE 0 END) END) END) AS NEWWOFEE_NW, ");
			sql.append("(CASE A.RESULTSTATUS WHEN 'R' THEN 0 WHEN 'A' THEN A.EACHFEE_NW ELSE (CASE COALESCE(OP.RESULTCODE,'00') WHEN '00' THEN A.EACHFEE_NW ELSE (CASE A.ACCTCODE WHEN '0' THEN A.EACHFEE_NW ELSE 0 END) END) END) AS NEWEACHFEE_NW, ");
			sql.append(" (CASE CAST(A.FEE_TYPE AS VARCHAR(1)) WHEN ' ' THEN '' ELSE CAST(A.FEE_TYPE AS VARCHAR(1)) END) AS FEE_TYPE ,");
			sql.append(" (CASE CAST(A.FEE_LVL_TYPE AS VARCHAR(1)) WHEN ' ' THEN '' ELSE CAST(A.FEE_LVL_TYPE AS VARCHAR(1)) END) AS FEE_LVL_TYPE ,");
			//20200824新增end
			
			
			sql.append("A.SENDERID,A.RECEIVERID,TRANSLATE('abcd-ef-gh',A.REFUNDDEADLINE,'abcdefgh') AS REFUNDDEADLINE, ");
//			sql.append("A.NEWSENDERFEE AS SENDERFEE, ");
//			sql.append("A.NEWINFEE AS INFEE, ");
//			sql.append("A.NEWOUTFEE AS OUTFEE, ");
//			sql.append("A.NEWEACHFEE AS EACHFEE,A.SENDERID,A.RECEIVERID,A.REFUNDDEADLINE, ");
			sql.append("A.TXID || '-' || TC.TXN_NAME AS TXN_NAME, A.NEWTXAMT AS NEWTXAMT, A.SENDERSTATUS, ");
			
			/*
			sql.append("(CASE WHEN (A.RESULTSTATUS = 'R' OR (A.RESULTSTATUS = 'P' AND ONP.RESULTCODE = '01')) THEN '0' ELSE (INTEGER(SUBSTR((CASE LENGTH(TRIM(COALESCE(A.FEE, '00000'))) WHEN 0 THEN '00000' ELSE COALESCE(A.FEE, '00000') END),1,3)) || '.' || ");
			sql.append("SUBSTR((CASE LENGTH(TRIM(COALESCE(A.FEE, '00000'))) WHEN 0 THEN '00000' ELSE COALESCE(A.FEE, '00000') END),4,2)) END) AS FEE, ");
			*/
			//sql.append("INTEGER(SUBSTR(RIGHT((REPEAT('0',5) || A.NEWFEE), 5),1,3)) || '.' || SUBSTR(RIGHT((REPEAT('0',5) || A.NEWFEE), 5),4,2) AS FEE, ");
			sql.append("(CASE A.RESULTSTATUS WHEN 'R' THEN 0 WHEN 'A' THEN A.NEWFEE ELSE (CASE COALESCE(OP.RESULTCODE,'00') WHEN '00' THEN A.NEWFEE ELSE (CASE A.ACCTCODE WHEN '0' THEN A.NEWFEE ELSE 0 END) END) END) AS NEWFEE, ");
			sql.append("(CASE A.RESULTSTATUS WHEN 'R' THEN 0 WHEN 'A' THEN A.HANDLECHARGE_NW ELSE (CASE COALESCE(OP.RESULTCODE,'00') WHEN '00' THEN A.HANDLECHARGE_NW ELSE (CASE A.ACCTCODE WHEN '0' THEN A.HANDLECHARGE_NW ELSE 0 END) END) END) AS NEWFEE_NW, ");
			
			sql.append("A.SENDERBANKID || '-' || (SELECT B.BRBK_NAME FROM BANK_BRANCH B WHERE A.SENDERBANKID = B.BRBK_ID) AS SENDERBANKID_NAME, ");
			sql.append("A.INBANKID || '-' || (SELECT B.BRBK_NAME FROM BANK_BRANCH B WHERE A.INBANKID = B.BRBK_ID) AS INBANKID_NAME, ");
			sql.append("A.OUTBANKID || '-' || (SELECT B.BRBK_NAME FROM BANK_BRANCH B WHERE A.OUTBANKID = B.BRBK_ID) AS OUTBANKID_NAME, ");
			sql.append("A.WOBANKID || '-' || (SELECT B.BRBK_NAME FROM BANK_BRANCH B WHERE A.WOBANKID = B.BRBK_ID) AS WOBANKID_NAME, ");
			sql.append("TRANSLATE('abcd-ef-gh', A.BIZDATE, 'abcdefgh') AS BIZDATE,TRANSLATE('abcd-ef-gh ij:kl:mn', A.EACHDT, 'abcdefghijklmn') AS EACHDT,A.CLEARINGPHASE,A.INACCTNO,A.OUTACCTNO,A.INID,A.OUTID,A.ACCTBAL,A.AVAILBAL,A.CHECKTYPE,A.MERCHANTID,A.ORDERNO,A.TRMLID,A.TRMLCHECK,A.TRMLMCC,A.BANKRSPMSG,A.RRN, ");
			
			//20150319 by 李建利  「交易資料查詢」、「未完成交易資料查詢」的檢視明細的「交易結果」顯示最初交易結果即可
			//sql.append("(CASE A.NEWRESULT WHEN 'A' THEN '成功' WHEN 'R' THEN '失敗' ELSE COALESCE((CASE OP.RESULTCODE WHEN '01' THEN '失敗' ELSE '成功' END),(CASE A.SENDERSTATUS WHEN '1' THEN '處理中' ELSE '未完成' END)) END) AS RESP, ");
			sql.append("(CASE A.RESULTSTATUS WHEN 'A' THEN '成功' WHEN 'R' THEN '失敗' ELSE (CASE A.SENDERSTATUS WHEN '1' THEN '處理中' ELSE '未完成' END) END) AS RESP, ");
			
			sql.append("RC1 || '-' || (SELECT B.ERR_DESC FROM TXN_ERROR_CODE B WHERE A.RC1=B.ERROR_ID) ERR_DESC1, ");
			sql.append("RC2 || '-' || (SELECT B.ERR_DESC FROM TXN_ERROR_CODE B WHERE A.RC2=B.ERROR_ID) ERR_DESC2, ");
			sql.append("RC3 || '-' || (SELECT B.ERR_DESC FROM TXN_ERROR_CODE B WHERE A.RC3=B.ERROR_ID) ERR_DESC3, ");
			sql.append("RC4 || '-' || (SELECT B.ERR_DESC FROM TXN_ERROR_CODE B WHERE A.RC4=B.ERROR_ID) ERR_DESC4, ");
			sql.append("RC5 || '-' || (SELECT B.ERR_DESC FROM TXN_ERROR_CODE B WHERE A.RC5=B.ERROR_ID) ERR_DESC5, ");
			sql.append("RC6 || '-' || (SELECT B.ERR_DESC FROM TXN_ERROR_CODE B WHERE A.RC6=B.ERROR_ID) ERR_DESC6, TRANSLATE('abcd-ef-gh ij:kl:mn.opq',UPDATEDT,'abcdefghijklmnopq') AS UPDATEDT, ");
			sql.append("A.BILLTYPE, OBA.BILLDATA, A.CHARGETYPE, A.TOLLID, A.BILLFLAG, OBA.CHECKDATA,");
			sql.append("A.PFCLASS || '-' || (SELECT BT.BILL_TYPE_NAME FROM BILL_TYPE BT WHERE A.PFCLASS = BT.BILL_TYPE_ID) AS PFCLASS, ");
			sql.append("OBA.USERNO, A.CONMEMO, OBA.SENDERDATA, OBA.COMPANYID, A.MERCHANTID, A.ORDERNO, A.TRMLID, OBA.OTXAMT, COALESCE(TRANSLATE('abcd-ef-gh',OBA.OTXDATE,'abcdefgh'),'') AS OTXDATE, OBA.OTRMLID, OBA.OMERCHANTID, OBA.OORDERNO, OBA.PAN, OBA.OPAN, OBA.PSN, OBA.OPSN, ");
			sql.append("COALESCE(TRANSLATE('abcd-ef-gh',VARCHAR(OP.BIZDATE),'abcdefgh'),'') AS NEWBIZDATE ");
			sql.append(" , COALESCE(OP.CLEARINGPHASE,'') AS NEWCLRPHASE, COALESCE(OP.RESULTCODE,'eACH') AS RESULTCODE ");
			sql.append(" ,TRANSLATE('abcd-ef-gh', COALESCE(A.FLBIZDATE,''), 'abcdefgh') AS FLBIZDATE ,COALESCE(VARCHAR(A.FLPROCSEQ),'') AS FLPROCSEQ ");
			sql.append(" ,COALESCE(A.FLBATCHSEQ,'') AS FLBATCHSEQ, COALESCE(A.DATASEQ,'') AS DATASEQ ");
			
			sql.append("FROM VW_ONBLOCKTAB A LEFT JOIN EACH_TXN_CODE ETC ON A.PCODE = ETC.EACH_TXN_ID "); 
			sql.append("LEFT JOIN TXN_CODE TC ON A.TXID = TC.TXN_ID ");
			sql.append("LEFT JOIN ONBLOCKAPPENDTAB OBA ON A.TXDATE = OBA.TXDATE AND A.STAN = OBA.STAN ");
			sql.append("LEFT JOIN ONPENDINGTAB OP ON OP.OTXDATE = A.TXDATE AND OP.OSTAN = A.STAN ");
			condition += "WHERE ";
			if(!txdate.equals("")){
				condition += " A.TXDATE='"+txdate+"'";
				condition += " AND ";
			}
			if(!stan.equals("")){
				condition += " A.STAN='"+stan+"'";
			}
			sql.append(condition);
			System.out.println("sql===>"+sql.toString());
			po = vw_onblocktab_Dao.getDetail(sql.toString(),txdate,stan);
		}catch(Exception e){
			e.printStackTrace();
		}
					
		return po;
	}
	
	
	public Map getNewFeeDetail(String bizdate,String txid,String senderid , String senderbankid , String txamt){
		if(txid.indexOf("-")!=-1) {
			txid = txid.substring(0,txid.indexOf("-"));
		}
		if(senderid.indexOf("-")!=-1) {
			senderid = senderid.substring(0,senderid.indexOf("-"));
		}
		if(senderbankid.indexOf("-")!=-1) {
			senderbankid = senderbankid.substring(0,senderbankid.indexOf("-"));
		}
		return onblocktab_Dao.getNewFeeDetail(bizdate,txid,senderid , senderbankid , txamt);
	}
	
	//未完成交易結果查詢
	public String getNotTradResList(Map<String, String> params){
		
		String startDate = StrUtils.isEmpty(params.get("START_DATE"))?"":params.get("START_DATE");//交易日期
		String endDate = StrUtils.isEmpty(params.get("END_DATE"))?"":params.get("END_DATE");//交易日期
		
		String opbkId = StrUtils.isEmpty(params.get("OPBK_ID"))?"all":params.get("OPBK_ID");//操作行代號
		String bgbkId = StrUtils.isEmpty(params.get("BGBK_ID"))?"all":params.get("BGBK_ID");//總行代號
		String clearingphase = StrUtils.isEmpty(params.get("CLEARINGPHASE"))?"all":params.get("CLEARINGPHASE");//清算階段代號
		String bus_Type_Id = StrUtils.isEmpty(params.get("BUSINESS_TYPE_ID"))?"all":params.get("BUSINESS_TYPE_ID");//業務類別代號
		String resultStatus = StrUtils.isEmpty(params.get("RESULTSTATUS"))?"all":params.get("RESULTSTATUS");//業務類別代號
		String ostan = StrUtils.isEmpty(params.get("OSTAN"))?"all":params.get("OSTAN");//系統追蹤序號
		
		int pageNo = StrUtils.isEmpty(params.get("page")) ?0:Integer.valueOf(params.get("page"));
		int pageSize = StrUtils.isEmpty(params.get("rows")) ?Integer.valueOf(Arguments.getStringArg("PAGE.SIZE")):Integer.valueOf(params.get("rows"));
		String isSearch = StrUtils.isEmpty(params.get("isSearch")) ?"Y":params.get("isSearch");
		//是否包含整批資料("N"表示不過濾)
		String filter_bat = params.get("FILTER_BAT")==null?"N":"Y";
		List<ONBLOCKTAB> list = null;
		Map rtnMap = new HashMap();
		Page page = null;
		String condition_1 = "";
		String condition_2 = "";
		try {
			list = new ArrayList<ONBLOCKTAB>();
			List<String> conditions_1 = new ArrayList<String>();
			List<String> conditions_2 = new ArrayList<String>();
			/* 20150210 HUANGPU 改以清算階段後的營業日(BIZDATE)查詢資料，非原交易日期(OTXDATE) */
			if(StrUtils.isNotEmpty(startDate)){//交易日期
				conditions_1.add(" BIZDATE >= '"+DateTimeUtils.convertDate(startDate, "yyyyMMdd", "yyyyMMdd")+"' ");
			}
			if(StrUtils.isNotEmpty(endDate)){//交易日期
				conditions_1.add(" BIZDATE <= '"+DateTimeUtils.convertDate(endDate, "yyyyMMdd", "yyyyMMdd")+"' ");
			}
					
			if(!bgbkId.equals("all")){//發動行所屬總行、入賬行所屬總行、扣款行所屬總行
				conditions_1.add(" (SENDERHEAD = '"+bgbkId+"' OR INHEAD = '"+bgbkId+"' OR OUTHEAD = '"+bgbkId+"') ");
			}
			if(!clearingphase.equals("all")){//清算階段代號
				conditions_1.add(" CLEARINGPHASE= '"+clearingphase+"' ");
			}
			if(!resultStatus.equals("all")){
				conditions_1.add(" COALESCE(RESULTCODE,'00') = '" + resultStatus + "' ");
			}
			if(!bus_Type_Id.equals("all")){
				conditions_1.add(" ETC.BUSINESS_TYPE_ID IN ('"+bus_Type_Id+"') ");
			}
			if(!ostan.equals("all")){
				conditions_1.add(" A.OSTAN = '"+ostan+"' ");
			}
			if(!opbkId.equals("all")){//發動行所屬操作行、入賬行所屬操作行、扣款行所屬操作行
				if(filter_bat.equals("Y")){
					conditions_2.addAll(conditions_1);
					conditions_2.add(" COALESCE(FLBIZDATE,'') <> ''  ");
					conditions_2.add(" ((INACQUIRE = '" + opbkId + "' AND substr(COALESCE(PCODE,''),4) = '2' ) OR (OUTACQUIRE = '" + opbkId + "' AND substr(COALESCE(PCODE,''),4) = '1')) ");
					conditions_1.add(" COALESCE(FLBIZDATE,'') = ''  ");
					conditions_1.add(" (SENDERACQUIRE = '" + opbkId + "' OR OUTACQUIRE = '" + opbkId + "' OR INACQUIRE = '" + opbkId + "') ");
				}else{
					conditions_1.add(" (SENDERACQUIRE = '" + opbkId + "' OR OUTACQUIRE = '" + opbkId + "' OR INACQUIRE = '" + opbkId + "') ");
				}
//					conditions_1.add(" (SENDERACQUIRE = '"+opbkId+"' OR INACQUIRE = '"+opbkId+"' OR OUTACQUIRE = '"+opbkId+"') ");
			}else{
				if(filter_bat.equals("Y")){
					conditions_2.addAll(conditions_1);
					conditions_2.add(" COALESCE(FLBIZDATE,'') <> ''  ");
					conditions_1.add(" COALESCE(FLBIZDATE,'') = ''  ");
				}
			}	
			condition_1 = combine(conditions_1);
			condition_2 = combine(conditions_2);
			StringBuffer sql = new StringBuffer();
			StringBuffer tmpSQL = new StringBuffer();
			StringBuffer sumSQL = new StringBuffer();
			StringBuffer cntSQL = new StringBuffer();
			String sord =StrUtils.isNotEmpty(params.get("sord"))? params.get("sord"):"";
			String sidx =StrUtils.isNotEmpty(params.get("sidx"))? params.get("sidx"):"";
			String orderSQL = "";
			if(StrUtils.isNotEmpty(params.get("sidx"))){
//					20160329 edit by hugo 修正未完成交易結果 grid 按部分欄位排序時(ex:交易日期、金額)，查無資料問題
//					if("STAN".equalsIgnoreCase(params.get("sidx"))){
//						orderSQL = " ORDER BY A.OSTAN "+params.get("sord");
//					}
//					if("TXDT".equalsIgnoreCase(params.get("sidx"))){
//						if("AAA".equalsIgnoreCase(params.get("sidx"))){
//						orderSQL = " ORDER BY (SELECT NEWTXDT FROM VW_ONBLOCKTAB B WHERE A.OTXDATE = B.TXDATE AND A.OSTAN = B.STAN) "+params.get("sord");
//					}
//					else if("TXAMT".equalsIgnoreCase(params.get("sidx"))){
//						else if("BBB".equalsIgnoreCase(params.get("sidx"))){
//						orderSQL = " ORDER BY (SELECT NEWTXAMT FROM VW_ONBLOCKTAB B WHERE A.OTXDATE = B.TXDATE AND A.OSTAN = B.STAN) "+params.get("sord");
//					}
//					else if("RESULTCODE".equalsIgnoreCase(params.get("sidx"))){
//						else if("CCC".equalsIgnoreCase(params.get("sidx"))){
//						orderSQL = " ORDER BY (CASE (SELECT NEWRESULT FROM VW_ONBLOCKTAB B WHERE A.OTXDATE = B.TXDATE AND A.OSTAN = B.STAN) WHEN 'R' THEN '失敗' WHEN 'A' THEN '成功' ELSE '未完成' END) "+params.get("sord");
//					}
//					else if("ACCTCODE".equalsIgnoreCase(params.get("sidx"))){
//						orderSQL = " ORDER BY (SELECT ACCTCODE FROM VW_ONBLOCKTAB B WHERE A.OTXDATE = B.TXDATE AND A.OSTAN = B.STAN) "+params.get("sord");
//					}
//					else if("CONRESULTCODE".equalsIgnoreCase(params.get("sidx"))){
//						orderSQL = " ORDER BY (GETRESPDESC(A.OTXDATE, A.OSTAN)) "+params.get("sord");
//					}
//					else{
//						orderSQL = StrUtils.isNotEmpty(sidx)? " ORDER BY "+sidx+" "+sord:"";
//					}
					orderSQL = StrUtils.isNotEmpty(sidx)? " ORDER BY "+sidx+" "+sord:"";
			}
			tmpSQL.append("WITH TEMP AS ( ");
			tmpSQL.append(" SELECT OUTACCT, INACCT, OBIZDATE, OCLEARINGPHASE, BIZDATE, CLEARINGPHASE, SENDERACQUIRE, SENDERHEAD , ETC.EACH_TXN_NAME, '' AS ACCTCODE  , COALESCE(A.OSTAN,'') AS OSTAN");
			tmpSQL.append(" , COALESCE(A.OTXDATE, '') AS TXDATE , COALESCE(A.OSTAN,'') AS STAN , (COALESCE(RESULTCODE, 'eACH')) AS CONRESULTCODE ");
			tmpSQL.append(" , A.SENDERBANKID || '-' || COALESCE((SELECT BRBK_NAME from BANK_BRANCH WHERE BRBK_ID = A.SENDERBANKID),'') AS SENDERBANKID ");
			tmpSQL.append(" , A.OUTBANKID || '-' || COALESCE((SELECT BRBK_NAME from BANK_BRANCH WHERE BRBK_ID = A.OUTBANKID),'') AS OUTBANKID ");
			tmpSQL.append(" , A.INBANKID || '-' || COALESCE((SELECT BRBK_NAME from BANK_BRANCH WHERE BRBK_ID = A.INBANKID),'') AS INBANKID ");
			tmpSQL.append(" , (CASE A.RESULTCODE WHEN '01' THEN '失敗' ELSE '成功' END) AS RESULTCODE,COALESCE(A.ACHFLAG,'') AS ACHFLAG  ");
			tmpSQL.append(" , (SELECT NEWTXAMT FROM VW_ONBLOCKTAB B WHERE A.OTXDATE = B.TXDATE AND A.OSTAN = B.STAN) AS TXAMT ");
			tmpSQL.append(" , A.PCODE || '-' || COALESCE(ETC.EACH_TXN_NAME,'') AS PCODE ");
			tmpSQL.append(" , VARCHAR_FORMAT((SELECT NEWTXDT FROM VW_ONBLOCKTAB B WHERE A.OTXDATE = B.TXDATE AND A.OSTAN = B.STAN),'YYYY-MM-DD HH24:MI:SS') AS TXDT ");
			tmpSQL.append(" FROM ONPENDINGTAB A ");
			tmpSQL.append(" LEFT JOIN EACH_TXN_CODE ETC ON A.PCODE = ETC.EACH_TXN_ID  ");
			tmpSQL.append(" WHERE COALESCE(BIZDATE,'') <> '' AND COALESCE(CLEARINGPHASE,'') <> ''  ");
			tmpSQL.append("    " + (StrUtils.isEmpty(condition_1)? "" : "AND " + condition_1));
			if(filter_bat.equals("Y")){
				tmpSQL.append("  UNION ALL  ");
				tmpSQL.append(" SELECT OUTACCT, INACCT, OBIZDATE, OCLEARINGPHASE, BIZDATE, CLEARINGPHASE, SENDERACQUIRE, SENDERHEAD , ETC.EACH_TXN_NAME, '' AS ACCTCODE  , COALESCE(A.OSTAN,'') AS OSTAN");
				tmpSQL.append(" , COALESCE(A.OTXDATE, '') AS TXDATE , COALESCE(A.OSTAN,'') AS STAN , (COALESCE(RESULTCODE, 'eACH')) AS CONRESULTCODE ");
				tmpSQL.append(" , A.SENDERBANKID || '-' || COALESCE((SELECT BRBK_NAME from BANK_BRANCH WHERE BRBK_ID = A.SENDERBANKID),'') AS SENDERBANKID ");
				tmpSQL.append(" , A.OUTBANKID || '-' || COALESCE((SELECT BRBK_NAME from BANK_BRANCH WHERE BRBK_ID = A.OUTBANKID),'') AS OUTBANKID ");
				tmpSQL.append(" , A.INBANKID || '-' || COALESCE((SELECT BRBK_NAME from BANK_BRANCH WHERE BRBK_ID = A.INBANKID),'') AS INBANKID ");
				tmpSQL.append(" , (CASE A.RESULTCODE WHEN '01' THEN '失敗' ELSE '成功' END) AS RESULTCODE,COALESCE(A.ACHFLAG,'') AS ACHFLAG  ");
				tmpSQL.append(" , (SELECT NEWTXAMT FROM VW_ONBLOCKTAB B WHERE A.OTXDATE = B.TXDATE AND A.OSTAN = B.STAN) AS TXAMT ");
				tmpSQL.append(" , A.PCODE || '-' || COALESCE(ETC.EACH_TXN_NAME,'') AS PCODE ");
				tmpSQL.append(" , VARCHAR_FORMAT((SELECT NEWTXDT FROM VW_ONBLOCKTAB B WHERE A.OTXDATE = B.TXDATE AND A.OSTAN = B.STAN),'YYYY-MM-DD HH24:MI:SS') AS TXDT ");
				tmpSQL.append(" FROM ONPENDINGTAB A ");
				tmpSQL.append(" LEFT JOIN EACH_TXN_CODE ETC ON A.PCODE = ETC.EACH_TXN_ID  ");
				tmpSQL.append(" WHERE COALESCE(BIZDATE,'') <> '' AND COALESCE(CLEARINGPHASE,'') <> ''  ");
				tmpSQL.append("    " + (StrUtils.isEmpty(condition_2)? "" : "AND " + condition_2));
			}
			tmpSQL.append(" ) ");
			sql.append(" SELECT * FROM ( ");
			sql.append("  	SELECT  ROWNUMBER() OVER( "+orderSQL+") AS ROWNUMBER , TEMP.*  ");
			sql.append(" FROM TEMP ");
			sql.append(" ) AS A    ");
			sql.append("  WHERE ROWNUMBER >= " + (Page.getStartOfPage(pageNo, pageSize) + 1) + " AND ROWNUMBER <= " + (pageNo * pageSize) + " ");
			sql.insert(0, tmpSQL.toString());
			sql.append(orderSQL);
			//先算出總計的欄位值，放到rtnMap裡面，到頁面再放到下面的總計Grid裡面
			sumSQL.append("SELECT SUM(TXAMT) AS TXAMT  FROM TEMP ");
			sumSQL.insert(0, tmpSQL.toString());
			System.out.println("sumSQL="+sumSQL);
			String dataSumCols[] = {"TXAMT"};
			list = onblocktab_Dao.dataSum(sumSQL.toString(),dataSumCols,ONBLOCKTAB.class);
			
			/*
			for(ONBLOCKTAB po:list){
				System.out.println(String.format("SUM(X.TXAMT)=%s", po.getTXAMT()));
			}
			*/
			rtnMap.put("dataSumList", list);
			cntSQL.append("SELECT COUNT(*) AS NUM FROM TEMP");
			cntSQL.insert(0, tmpSQL.toString());
			String cols[] = {"PCODE","TXDT","STAN","TXDATE","SENDERBANKID", "OUTBANKID", "INBANKID","OUTACCT","INACCT", 
							 "TXAMT", "CONRESULTCODE","OBIZDATE","OCLEARINGPHASE","RESULTCODE", "ACCTCODE" ,"ACHFLAG"  };
			System.out.println("cntSQL===>"+cntSQL.toString());
			System.out.println("sql===>"+sql.toString());
			page= onblocktab_Dao.getDataIIII(Integer.valueOf(pageNo), Integer.valueOf(pageSize), cntSQL.toString(), sql.toString(), cols, ONBLOCKTABbean.class);
			list = (List<ONBLOCKTAB>) page.getResult();
			System.out.println("ONBLOCKTAB.list>>"+list);
			list = list!=null&& list.size() ==0 ?null:list;
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
			
			String newParams = params.get("serchStrs");
			System.out.println("serchStrs ="+newParams);
			if(resultStatus.equals("00")){
				newParams = newParams.replace("\"RESULTSTATUS\":\"00\"", "\"RESULTSTATUS\":\"成功\"");
			    params.put("serchStrs", newParams);
			}else if(resultStatus.equals("01")){
				newParams = newParams.replace("\"RESULTSTATUS\":\"01\"", "\"RESULTSTATUS\":\"失敗\"");
			    params.put("serchStrs", newParams);
			}
//				必須是按下UI的查詢才紀錄
			if(isSearch.equals("Y")){
				userlog_bo.writeLog("C", null, null, params);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rtnMap.put("total", 1);
			rtnMap.put("page", 1);
			rtnMap.put("records", 0);
			rtnMap.put("rows", new ArrayList<>());
			rtnMap.put("msg", "查詢失敗");
			userlog_bo.writeFailLog("C", rtnMap, null, null, params);
		}
		String json = JSONUtils.map2json(rtnMap) ;
		System.out.println("json===>"+json+"<===");
		return json;
}
	
	/**
	 * 原未完成交易結果查詢，暫不使用
	 * @param params
	 * @return
	 */
	//未完成交易結果查詢
	public String getNotTradResList_BAK(Map<String, String> params){
		
		String startDate = StrUtils.isEmpty(params.get("START_DATE"))?"":params.get("START_DATE");//交易日期
		String endDate = StrUtils.isEmpty(params.get("END_DATE"))?"":params.get("END_DATE");//交易日期
		
		String opbkId = StrUtils.isEmpty(params.get("OPBK_ID"))?"all":params.get("OPBK_ID");//操作行代號
		String bgbkId = StrUtils.isEmpty(params.get("BGBK_ID"))?"all":params.get("BGBK_ID");//總行代號
		String clearingphase = StrUtils.isEmpty(params.get("CLEARINGPHASE"))?"all":params.get("CLEARINGPHASE");//清算階段代號
		String bus_Type_Id = StrUtils.isEmpty(params.get("BUSINESS_TYPE_ID"))?"all":params.get("BUSINESS_TYPE_ID");//業務類別代號
		String resultStatus = StrUtils.isEmpty(params.get("RESULTSTATUS"))?"all":params.get("RESULTSTATUS");//業務類別代號
		int pageNo = StrUtils.isEmpty(params.get("page")) ?0:Integer.valueOf(params.get("page"));
		int pageSize = StrUtils.isEmpty(params.get("rows")) ?Integer.valueOf(Arguments.getStringArg("PAGE.SIZE")):Integer.valueOf(params.get("rows"));
		String isSearch = StrUtils.isEmpty(params.get("isSearch")) ?"Y":params.get("isSearch");
		List<ONBLOCKTAB> list = null;
		Map rtnMap = new HashMap();
		Page page = null;
		String condition_1 = "";
		try {
			list = new ArrayList<ONBLOCKTAB>();
			List<String> conditions_1 = new ArrayList<String>();
			/* 20150210 HUANGPU 改以清算階段後的營業日(BIZDATE)查詢資料，非原交易日期(OTXDATE) */
			if(StrUtils.isNotEmpty(startDate)){//交易日期
				conditions_1.add(" BIZDATE >= '"+DateTimeUtils.convertDate(startDate, "yyyyMMdd", "yyyyMMdd")+"' ");
			}
			if(StrUtils.isNotEmpty(endDate)){//交易日期
				conditions_1.add(" BIZDATE <= '"+DateTimeUtils.convertDate(endDate, "yyyyMMdd", "yyyyMMdd")+"' ");
			}
			if(!opbkId.equals("all")){//發動行所屬操作行、入賬行所屬操作行、扣款行所屬操作行
				conditions_1.add(" (SENDERACQUIRE = '"+opbkId+"' OR INACQUIRE = '"+opbkId+"' OR OUTACQUIRE = '"+opbkId+"' OR WOACQUIRE = '"+opbkId+"') ");
			}			
			if(!bgbkId.equals("all")){//發動行所屬總行、入賬行所屬總行、扣款行所屬總行
				conditions_1.add(" (SENDERHEAD = '"+bgbkId+"' OR INHEAD = '"+bgbkId+"' OR OUTHEAD = '"+bgbkId+"' OR WOHEAD = '"+bgbkId+"') ");
			}
			if(!clearingphase.equals("all")){//清算階段代號
				conditions_1.add(" CLEARINGPHASE= '"+clearingphase+"' ");
			}
			if(!resultStatus.equals("all")){
				conditions_1.add(" COALESCE(RESULTCODE,'00') = '" + resultStatus + "' ");
			}
			if(!bus_Type_Id.equals("all")){
				conditions_1.add(" ETC.BUSINESS_TYPE_ID IN ('"+bus_Type_Id+"') ");
			}
			
			condition_1 = combine(conditions_1);
			
			StringBuffer fromAndWhere_core = new StringBuffer();
			fromAndWhere_core.append("FROM ( ");
			fromAndWhere_core.append("    SELECT ROWNUMBER() OVER( ");
			if(StrUtils.isNotEmpty(params.get("sidx"))){
				if("STAN".equalsIgnoreCase(params.get("sidx"))){
					fromAndWhere_core.append(" ORDER BY A.OSTAN "+params.get("sord"));
				}
				else if("TXDT".equalsIgnoreCase(params.get("sidx"))){
					fromAndWhere_core.append(" ORDER BY (SELECT NEWTXDT FROM VW_ONBLOCKTAB B WHERE A.OTXDATE = B.TXDATE AND A.OSTAN = B.STAN) "+params.get("sord"));
				}
				else if("TXAMT".equalsIgnoreCase(params.get("sidx"))){
					fromAndWhere_core.append(" ORDER BY (SELECT NEWTXAMT FROM VW_ONBLOCKTAB B WHERE A.OTXDATE = B.TXDATE AND A.OSTAN = B.STAN) "+params.get("sord"));
				}
				else if("RESULTCODE".equalsIgnoreCase(params.get("sidx"))){
					fromAndWhere_core.append(" ORDER BY (CASE (SELECT NEWRESULT FROM VW_ONBLOCKTAB B WHERE A.OTXDATE = B.TXDATE AND A.OSTAN = B.STAN) WHEN 'R' THEN '失敗' WHEN 'A' THEN '成功' ELSE '未完成' END) "+params.get("sord"));
				}
				else if("ACCTCODE".equalsIgnoreCase(params.get("sidx"))){
					fromAndWhere_core.append(" ORDER BY (SELECT ACCTCODE FROM VW_ONBLOCKTAB B WHERE A.OTXDATE = B.TXDATE AND A.OSTAN = B.STAN) "+params.get("sord"));
				}
				else if("CONRESULTCODE".equalsIgnoreCase(params.get("sidx"))){
					fromAndWhere_core.append(" ORDER BY (GETRESPDESC(A.OTXDATE, A.OSTAN)) "+params.get("sord"));
				}
				else{
					fromAndWhere_core.append(" ORDER BY "+params.get("sidx")+" "+params.get("sord"));
				}
			}
			fromAndWhere_core.append("    ) AS ROWNUMBER, PCODE, OTXDATE, OSTAN, SENDERBANKID, OUTBANKID, INBANKID, WOBANKID, OUTACCT, INACCT, OBIZDATE, OCLEARINGPHASE, ");
			fromAndWhere_core.append("    RESULTCODE, BIZDATE, CLEARINGPHASE, SENDERACQUIRE, SENDERHEAD ,ACHFLAG   ");
			if(StrUtils.isNotEmpty(bus_Type_Id)){
				fromAndWhere_core.append(", ETC.EACH_TXN_NAME ");
			}
			fromAndWhere_core.append("    FROM ONPENDINGTAB A ");
			if(StrUtils.isNotEmpty(bus_Type_Id)){
				fromAndWhere_core.append("LEFT JOIN EACH_TXN_CODE ETC ON A.PCODE = ETC.EACH_TXN_ID ");
			}
			fromAndWhere_core.append("    WHERE COALESCE(BIZDATE,'') <> '' AND COALESCE(CLEARINGPHASE,'') <> '' ");
			fromAndWhere_core.append("    " + (StrUtils.isEmpty(condition_1)? "" : "AND " + condition_1));
			fromAndWhere_core.append(") ");
			
			StringBuffer fromAndWhere_all = new StringBuffer();
			fromAndWhere_all.append("FROM ( ");
			fromAndWhere_all.append("    SELECT * ");
			fromAndWhere_all.append(fromAndWhere_core);
			fromAndWhere_all.append(") AS A ");
			if(StrUtils.isEmpty(bus_Type_Id)){
				fromAndWhere_all.append("LEFT JOIN EACH_TXN_CODE ETC ON A.PCODE = ETC.EACH_TXN_ID ");
			}
			
			StringBuffer fromAndWhere =  new StringBuffer();
			fromAndWhere.append("FROM ( ");
			fromAndWhere.append("    SELECT * ");
			fromAndWhere.append(fromAndWhere_core);
			fromAndWhere.append("    WHERE ROWNUMBER >= " + (Page.getStartOfPage(pageNo, pageSize) + 1) + " AND ROWNUMBER <= " + (pageNo * pageSize) + " ");
			fromAndWhere.append(") AS A ");
			if(StrUtils.isEmpty(bus_Type_Id)){
				fromAndWhere.append("LEFT JOIN EACH_TXN_CODE ETC ON A.PCODE = ETC.EACH_TXN_ID ");
			}
			
			//先算出總計的欄位值，放到rtnMap裡面，到頁面再放到下面的總計Grid裡面
			StringBuffer dataSumSQL = new StringBuffer();
			/* 20150505 HUANGPU 交易金額不須區分沖正或一般
			dataSumSQL.append("SELECT SUM((CASE (SELECT ACCTCODE FROM VW_ONBLOCKTAB B WHERE A.OTXDATE = B.TXDATE AND A.OSTAN = B.STAN) WHEN '0' "); 
			dataSumSQL.append("THEN -(SELECT NEWTXAMT FROM VW_ONBLOCKTAB B WHERE A.OTXDATE = B.TXDATE AND A.OSTAN = B.STAN) ELSE "); 
			dataSumSQL.append("(SELECT NEWTXAMT FROM VW_ONBLOCKTAB B WHERE A.OTXDATE = B.TXDATE AND A.OSTAN = B.STAN) END)) AS TXAMT ");
			 */
			dataSumSQL.append("SELECT SUM((SELECT NEWTXAMT FROM VW_ONBLOCKTAB B WHERE A.OTXDATE = B.TXDATE AND A.OSTAN = B.STAN)) AS TXAMT ");
			dataSumSQL.append(fromAndWhere_all);
			System.out.println("dataSumSQL="+dataSumSQL);
			
			String dataSumCols[] = {"TXAMT"};
			list = onblocktab_Dao.dataSum(dataSumSQL.toString(),dataSumCols,ONBLOCKTAB.class);
			/*
			for(ONBLOCKTAB po:list){
				System.out.println(String.format("SUM(X.TXAMT)=%s", po.getTXAMT()));
			}
			 */
			rtnMap.put("dataSumList", list);
			
			StringBuffer countQuery = new StringBuffer();
			StringBuffer sql = new StringBuffer();
			
			countQuery.append("SELECT COUNT(*) AS NUM ");
			countQuery.append(fromAndWhere_all);
			
			sql.append("SELECT ");
			if(StrUtils.isEmpty(bus_Type_Id)){
				sql.append("A.PCODE || '-' || COALESCE(ETC.EACH_TXN_NAME,'') AS PCODE, "); 
			}else{
				sql.append("A.PCODE || '-' || COALESCE(A.EACH_TXN_NAME,'') AS PCODE, "); 
			}
			sql.append("REPLACE('12-3','-','A' ), "); 
			sql.append("VARCHAR_FORMAT((SELECT NEWTXDT FROM VW_ONBLOCKTAB B WHERE A.OTXDATE = B.TXDATE AND A.OSTAN = B.STAN),'YYYY-MM-DD HH24:MI:SS') AS TXDT, "); 
			sql.append("COALESCE(A.OSTAN,'') AS STAN, COALESCE(A.OTXDATE, '') AS TXDATE, ");
			sql.append("A.SENDERBANKID || '-' || COALESCE((SELECT BRBK_NAME from BANK_BRANCH WHERE BRBK_ID = A.SENDERBANKID),'') AS SENDERBANKID, "); 
			sql.append("A.OUTBANKID || '-' || COALESCE((SELECT BRBK_NAME from BANK_BRANCH WHERE BRBK_ID = A.OUTBANKID),'') AS OUTBANKID, "); 
			sql.append("A.INBANKID || '-' || COALESCE((SELECT BRBK_NAME from BANK_BRANCH WHERE BRBK_ID = A.INBANKID),'') AS INBANKID, "); 
			sql.append("A.WOBANKID || '-' || COALESCE((SELECT BRBK_NAME from BANK_BRANCH WHERE BRBK_ID = A.WOBANKID),'') AS WOBANKID, "); 
			sql.append("A.OUTACCT, A.INACCT, "); 
			sql.append(" COALESCE(A.ACHFLAG,'') AS ACHFLAG, "); 
			sql.append("(SELECT NEWTXAMT FROM VW_ONBLOCKTAB B WHERE A.OTXDATE = B.TXDATE AND A.OSTAN = B.STAN) AS TXAMT, "); 
			
			//20150316 BY 李建利 「回覆代號」改為顯示RESULTCODE的原始值
			//sql.append("(GETRESPDESC(A.OTXDATE, A.OSTAN)) AS CONRESULTCODE, ");
			sql.append("(COALESCE(RESULTCODE, 'eACH')) AS CONRESULTCODE, ");
			
			sql.append("A.OBIZDATE, A.OCLEARINGPHASE, (CASE A.RESULTCODE WHEN '01' THEN '失敗' ELSE '成功' END) AS RESULTCODE, "); 
			
			//20150305 BY 李建利 不要這個欄位
			//sql.append(",(CASE (SELECT ACCTCODE FROM VW_ONBLOCKTAB B WHERE A.OTXDATE = B.TXDATE AND A.OSTAN = B.STAN) WHEN '0' THEN '調整' ELSE '一般' END) AS ACCTCODE ");
			sql.append("'' AS ACCTCODE ");
			
			sql.append(fromAndWhere);
			if(StrUtils.isNotEmpty(params.get("sidx"))){
				if(params.get("sidx").contains("ASC") || params.get("sidx").contains("DESC") ||
						params.get("sidx").contains("asc") || params.get("sidx").contains("desc")){
					sql.append(" ORDER BY "+params.get("sidx"));
				}else{
					sql.append(" ORDER BY "+params.get("sidx")+" "+params.get("sord"));
				}
			}
			
			String cols[] = {"PCODE","TXDT","STAN","TXDATE","SENDERBANKID", "OUTBANKID", "INBANKID", "WOBANKID","OUTACCT","INACCT", 
					"TXAMT", "CONRESULTCODE","OBIZDATE","OCLEARINGPHASE","RESULTCODE", "ACCTCODE" ,"ACHFLAG"  };
			System.out.println("countQuery===>"+countQuery.toString());
			System.out.println("sql===>"+sql.toString());
			page= onblocktab_Dao.getDataIIII(Integer.valueOf(pageNo), Integer.valueOf(pageSize), countQuery.toString(), sql.toString(), cols, ONBLOCKTABbean.class);
			list = (List<ONBLOCKTAB>) page.getResult();
			System.out.println("ONBLOCKTAB.list>>"+list);
			list = list!=null&& list.size() ==0 ?null:list;
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
			
			String newParams = params.get("serchStrs");
			System.out.println("serchStrs ="+newParams);
			if(resultStatus.equals("00")){
				newParams = newParams.replace("\"RESULTSTATUS\":\"00\"", "\"RESULTSTATUS\":\"成功\"");
				params.put("serchStrs", newParams);
			}else if(resultStatus.equals("01")){
				newParams = newParams.replace("\"RESULTSTATUS\":\"01\"", "\"RESULTSTATUS\":\"失敗\"");
				params.put("serchStrs", newParams);
			}
//			必須是按下UI的查詢才紀錄
			if(isSearch.equals("Y")){
				userlog_bo.writeLog("C", null, null, params);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rtnMap.put("total", 1);
			rtnMap.put("page", 1);
			rtnMap.put("records", 0);
			rtnMap.put("rows", new ArrayList<>());
			rtnMap.put("msg", "查詢失敗");
			userlog_bo.writeFailLog("C", rtnMap, null, null, params);
		}
		String json = JSONUtils.map2json(rtnMap) ;
		System.out.println("json===>"+json+"<===");
		return json;
	}
	
	//檢視未完成交易結果明細
	public Map showNotTradResDetail(String txdate,String stan){
		Map po = null;
		Map rtnMap = new HashMap();
		String condition ="";
		try {
			txdate = StrUtils.isEmpty(txdate)?"":txdate;
			stan = StrUtils.isEmpty(stan)?"":stan;
			
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT TRANSLATE('abcd-ef-gh', VARCHAR(A.TXDATE), 'abcdefgh') AS TXDATE,VARCHAR(A.STAN) AS STAN,VARCHAR_FORMAT(NEWTXDT, 'YYYY-MM-DD HH24:MI:SS') AS TXDT,A.PCODE || '-' || ETC.EACH_TXN_NAME AS PCODE_DESC, ");
			//20151023 edit by hugo req by UAT-20151022-02
//			sql.append("A.SENDERBANK || '-' || (SELECT B.BGBK_NAME FROM BANK_GROUP B WHERE A.SENDERBANK = B.BGBK_ID) AS SENDERBANK_NAME, ");
//			sql.append("A.RECEIVERBANK || '-' || (SELECT B.BGBK_NAME FROM BANK_GROUP B WHERE A.RECEIVERBANK = B.BGBK_ID) AS RECEIVERBANK_NAME, ");
			sql.append("A.SENDERBANK || '-' || COALESCE((SELECT B.BRBK_NAME FROM BANK_BRANCH B WHERE A.SENDERBANK = B.BRBK_ID),'') AS SENDERBANK_NAME, ");
			sql.append("A.RECEIVERBANK || '-' || COALESCE((SELECT B.BRBK_NAME FROM BANK_BRANCH B WHERE A.RECEIVERBANK = B.BRBK_ID),'') AS RECEIVERBANK_NAME, ");
			sql.append("(SELECT B.ERR_DESC FROM TXN_ERROR_CODE B WHERE A.CONRESULTCODE = B.ERROR_ID) AS CONRESULTCODE_DESC,A.ACCTCODE, ");
			sql.append("A.SENDERCLEARING || '-' || (SELECT B.BGBK_NAME FROM BANK_GROUP B WHERE A.SENDERCLEARING = B.BGBK_ID) AS SENDERCLEARING_NAME, ");
			sql.append("A.INCLEARING || '-' || (SELECT B.BGBK_NAME FROM BANK_GROUP B WHERE A.INCLEARING = B.BGBK_ID) AS INCLEARING_NAME, ");
			sql.append("A.OUTCLEARING || '-' || (SELECT B.BGBK_NAME FROM BANK_GROUP B WHERE A.OUTCLEARING = B.BGBK_ID) AS OUTCLEARING_NAME, ");
			sql.append("A.SENDERACQUIRE || '-' || (SELECT B.BGBK_NAME FROM BANK_GROUP B WHERE A.SENDERACQUIRE = B.BGBK_ID) AS SENDERACQUIRE_NAME, ");
			sql.append("A.INACQUIRE || '-' || (SELECT B.BGBK_NAME FROM BANK_GROUP B WHERE A.INACQUIRE = B.BGBK_ID) AS INACQUIRE_NAME, ");
			sql.append("A.OUTACQUIRE || '-' || (SELECT B.BGBK_NAME FROM BANK_GROUP B WHERE A.OUTACQUIRE = B.BGBK_ID) AS OUTACQUIRE_NAME, ");
			sql.append("A.WOACQUIRE || '-' || (SELECT B.BGBK_NAME FROM BANK_GROUP B WHERE A.WOACQUIRE = B.BGBK_ID) AS WOACQUIRE_NAME, ");
			sql.append("A.SENDERHEAD || '-' || (SELECT B.BGBK_NAME FROM BANK_GROUP B WHERE A.SENDERHEAD = B.BGBK_ID) AS SENDERHEAD_NAME, ");
			sql.append("A.INHEAD || '-' || (SELECT B.BGBK_NAME FROM BANK_GROUP B WHERE A.INHEAD = B.BGBK_ID) AS INHEAD_NAME, ");
			sql.append("A.OUTHEAD || '-' || (SELECT B.BGBK_NAME FROM BANK_GROUP B WHERE A.OUTHEAD = B.BGBK_ID) AS OUTHEAD_NAME, ");
			sql.append("A.WOHEAD || '-' || (SELECT B.BGBK_NAME FROM BANK_GROUP B WHERE A.WOHEAD = B.BGBK_ID) AS WOHEAD_NAME, ");
			sql.append("(CASE A.RESULTSTATUS WHEN 'R' THEN 0 WHEN 'A' THEN A.NEWSENDERFEE ELSE (CASE COALESCE(OP.RESULTCODE,'00') WHEN '00' THEN A.NEWSENDERFEE ELSE (CASE A.ACCTCODE WHEN '0' THEN A.NEWSENDERFEE ELSE 0 END) END) END) AS NEWSENDERFEE, ");
			sql.append("(CASE A.RESULTSTATUS WHEN 'R' THEN 0 WHEN 'A' THEN A.NEWINFEE ELSE (CASE COALESCE(OP.RESULTCODE,'00') WHEN '00' THEN A.NEWINFEE ELSE (CASE A.ACCTCODE WHEN '0' THEN A.NEWINFEE ELSE 0 END) END) END) AS NEWINFEE, ");
			sql.append("(CASE A.RESULTSTATUS WHEN 'R' THEN 0 WHEN 'A' THEN A.NEWOUTFEE ELSE (CASE COALESCE(OP.RESULTCODE,'00') WHEN '00' THEN A.NEWOUTFEE ELSE (CASE A.ACCTCODE WHEN '0' THEN A.NEWOUTFEE ELSE 0 END) END) END) AS NEWOUTFEE, ");
			sql.append("(CASE A.RESULTSTATUS WHEN 'R' THEN 0 WHEN 'A' THEN A.NEWWOFEE ELSE (CASE COALESCE(OP.RESULTCODE,'00') WHEN '00' THEN A.NEWWOFEE ELSE (CASE A.ACCTCODE WHEN '0' THEN A.NEWWOFEE ELSE 0 END) END) END) AS NEWWOFEE, ");
			sql.append("(CASE A.RESULTSTATUS WHEN 'R' THEN 0 WHEN 'A' THEN A.NEWEACHFEE ELSE (CASE COALESCE(OP.RESULTCODE,'00') WHEN '00' THEN A.NEWEACHFEE ELSE (CASE A.ACCTCODE WHEN '0' THEN A.NEWEACHFEE ELSE 0 END) END) END) AS NEWEACHFEE, ");
			
			//20220321新增
			sql.append("(CASE A.RESULTSTATUS WHEN 'R' THEN 0 WHEN 'A' THEN A.NEWEXTENDFEE ELSE (CASE COALESCE(OP.RESULTCODE,'00') WHEN '00' THEN A.NEWEXTENDFEE ELSE (CASE A.ACCTCODE WHEN '0' THEN A.NEWEXTENDFEE ELSE 0 END) END) END) AS NEWEXTENDFEE, ");
			sql.append(" A.EXTENDFEE AS EXTENDFEE , ");
			//20220321新增end
			
			//20200824新增start
			sql.append("(CASE A.RESULTSTATUS WHEN 'R' THEN 0 WHEN 'A' THEN A.SENDERFEE_NW ELSE (CASE COALESCE(OP.RESULTCODE,'00') WHEN '00' THEN A.SENDERFEE_NW ELSE (CASE A.ACCTCODE WHEN '0' THEN A.SENDERFEE_NW ELSE 0 END) END) END) AS NEWSENDERFEE_NW, ");
			sql.append("(CASE A.RESULTSTATUS WHEN 'R' THEN 0 WHEN 'A' THEN A.INFEE_NW ELSE (CASE COALESCE(OP.RESULTCODE,'00') WHEN '00' THEN A.INFEE_NW ELSE (CASE A.ACCTCODE WHEN '0' THEN A.INFEE_NW ELSE 0 END) END) END) AS NEWINFEE_NW, ");
			sql.append("(CASE A.RESULTSTATUS WHEN 'R' THEN 0 WHEN 'A' THEN A.OUTFEE_NW ELSE (CASE COALESCE(OP.RESULTCODE,'00') WHEN '00' THEN A.OUTFEE_NW ELSE (CASE A.ACCTCODE WHEN '0' THEN A.OUTFEE_NW ELSE 0 END) END) END) AS NEWOUTFEE_NW, ");
			sql.append("(CASE A.RESULTSTATUS WHEN 'R' THEN 0 WHEN 'A' THEN A.WOFEE_NW ELSE (CASE COALESCE(OP.RESULTCODE,'00') WHEN '00' THEN A.WOFEE_NW ELSE (CASE A.ACCTCODE WHEN '0' THEN A.WOFEE_NW ELSE 0 END) END) END) AS NEWWOFEE_NW, ");
			sql.append("(CASE A.RESULTSTATUS WHEN 'R' THEN 0 WHEN 'A' THEN A.EACHFEE_NW ELSE (CASE COALESCE(OP.RESULTCODE,'00') WHEN '00' THEN A.EACHFEE_NW ELSE (CASE A.ACCTCODE WHEN '0' THEN A.EACHFEE_NW ELSE 0 END) END) END) AS NEWEACHFEE_NW, ");
			sql.append("(CASE CAST(A.FEE_TYPE AS VARCHAR(1)) WHEN ' ' THEN '' ELSE CAST(A.FEE_TYPE AS VARCHAR(1)) END) AS FEE_TYPE ,");
			//20200824新增end
			sql.append("A.SENDERID,A.RECEIVERID,TRANSLATE('abcd-ef-gh',A.REFUNDDEADLINE,'abcdefgh') AS REFUNDDEADLINE, ");
			sql.append("A.TXID || '-' || TC.TXN_NAME AS TXN_NAME, A.NEWTXAMT AS NEWTXAMT, A.SENDERSTATUS, ");
			sql.append("(CASE A.RESULTSTATUS WHEN 'R' THEN 0 WHEN 'A' THEN A.NEWFEE ELSE (CASE COALESCE(OP.RESULTCODE,'00') WHEN '00' THEN A.NEWFEE ELSE (CASE A.ACCTCODE WHEN '0' THEN A.NEWFEE ELSE 0 END) END) END) AS NEWFEE, ");
			sql.append("(CASE A.RESULTSTATUS WHEN 'R' THEN 0 WHEN 'A' THEN A.HANDLECHARGE_NW ELSE (CASE COALESCE(OP.RESULTCODE,'00') WHEN '00' THEN A.HANDLECHARGE_NW ELSE (CASE A.ACCTCODE WHEN '0' THEN A.HANDLECHARGE_NW ELSE 0 END) END) END) AS NEWFEE_NW, ");
			sql.append("A.SENDERBANKID || '-' || (SELECT B.BRBK_NAME FROM BANK_BRANCH B WHERE A.SENDERBANKID = B.BRBK_ID) AS SENDERBANKID_NAME, ");
			sql.append("A.INBANKID || '-' || (SELECT B.BRBK_NAME FROM BANK_BRANCH B WHERE A.INBANKID = B.BRBK_ID) AS INBANKID_NAME, ");
			sql.append("A.OUTBANKID || '-' || (SELECT B.BRBK_NAME FROM BANK_BRANCH B WHERE A.OUTBANKID = B.BRBK_ID) AS OUTBANKID_NAME, ");
			sql.append("A.WOBANKID || '-' || (SELECT B.BRBK_NAME FROM BANK_BRANCH B WHERE A.WOBANKID = B.BRBK_ID) AS WOBANKID_NAME, ");
			sql.append("TRANSLATE('abcd-ef-gh', A.BIZDATE, 'abcdefgh') AS BIZDATE,TRANSLATE('abcd-ef-gh ij:kl:mn', A.EACHDT, 'abcdefghijklmn') AS EACHDT,A.CLEARINGPHASE,A.INACCTNO,A.OUTACCTNO,A.INID,A.OUTID,A.ACCTBAL,A.AVAILBAL,A.CHECKTYPE,A.MERCHANTID,A.ORDERNO,A.TRMLID,A.TRMLCHECK,A.TRMLMCC,A.BANKRSPMSG,A.RRN, ");
			sql.append("(CASE A.NEWRESULT WHEN 'A' THEN '成功' WHEN 'R' THEN '失敗' ELSE COALESCE((CASE OP.RESULTCODE WHEN '01' THEN '失敗' ELSE '成功' END),(CASE A.SENDERSTATUS WHEN '1' THEN '處理中' ELSE '未完成' END)) END) AS RESP, ");
			sql.append("RC1 || '-' || (SELECT B.ERR_DESC FROM TXN_ERROR_CODE B WHERE A.RC1=B.ERROR_ID) ERR_DESC1, ");
			sql.append("RC2 || '-' || (SELECT B.ERR_DESC FROM TXN_ERROR_CODE B WHERE A.RC2=B.ERROR_ID) ERR_DESC2, ");
			sql.append("RC3 || '-' || (SELECT B.ERR_DESC FROM TXN_ERROR_CODE B WHERE A.RC3=B.ERROR_ID) ERR_DESC3, ");
			sql.append("RC4 || '-' || (SELECT B.ERR_DESC FROM TXN_ERROR_CODE B WHERE A.RC4=B.ERROR_ID) ERR_DESC4, ");
			sql.append("RC5 || '-' || (SELECT B.ERR_DESC FROM TXN_ERROR_CODE B WHERE A.RC5=B.ERROR_ID) ERR_DESC5, ");
			sql.append("RC6 || '-' || (SELECT B.ERR_DESC FROM TXN_ERROR_CODE B WHERE A.RC6=B.ERROR_ID) ERR_DESC6, TRANSLATE('abcd-ef-gh ij:kl:mn.opq',UPDATEDT,'abcdefghijklmnopq') AS UPDATEDT, ");
			sql.append("OBA.USERNO, A.CONMEMO, OBA.SENDERDATA, OBA.COMPANYID, A.MERCHANTID, A.ORDERNO, A.TRMLID, OBA.OTXAMT, COALESCE(TRANSLATE('abcd-ef-gh',OBA.OTXDATE,'abcdefgh'),'') AS OTXDATE, OBA.OTRMLID, OBA.OMERCHANTID, OBA.OORDERNO, OBA.PAN, OBA.OPAN, OBA.PSN, OBA.OPSN, ");
			sql.append("COALESCE(TRANSLATE('abcd-ef-gh',VARCHAR(OP.BIZDATE),'abcdefgh'),'') AS NEWBIZDATE, ");
			sql.append("COALESCE(OP.CLEARINGPHASE,'') AS NEWCLRPHASE, COALESCE(OP.RESULTCODE,'eACH') AS RESULTCODE, ");
			sql.append("COALESCE(OP.ACHFLAG,'') AS ACHFLAG ");
			sql.append(" ,TRANSLATE('abcd-ef-gh', COALESCE(A.FLBIZDATE,''), 'abcdefgh') AS FLBIZDATE ,COALESCE(VARCHAR(A.FLPROCSEQ),'') AS FLPROCSEQ ");
			sql.append(" ,COALESCE(A.FLBATCHSEQ,'') AS FLBATCHSEQ, COALESCE(A.DATASEQ,'') AS DATASEQ ");
			sql.append("FROM VW_ONBLOCKTAB A LEFT JOIN EACH_TXN_CODE ETC ON A.PCODE = ETC.EACH_TXN_ID "); 
			sql.append("LEFT JOIN TXN_CODE TC ON A.TXID = TC.TXN_ID ");
			sql.append("LEFT JOIN ONBLOCKAPPENDTAB OBA ON A.TXDATE = OBA.TXDATE AND A.STAN = OBA.STAN ");
			sql.append("LEFT JOIN ONPENDINGTAB OP ON OP.OTXDATE = A.TXDATE AND OP.OSTAN = A.STAN ");
			condition += "WHERE ";
			if(!txdate.equals("")){
				condition += " A.TXDATE='"+txdate+"'";
				condition += " AND ";
			}
			if(!stan.equals("")){
				condition += " A.STAN='"+stan+"'";
			}
			sql.append(condition);
			System.out.println("sql===>"+sql.toString());
			po = vw_onblocktab_Dao.getDetail(sql.toString(),txdate,stan);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return po;
	}
	
	/**
	 * 目前只有每日交易處理時間統計在用
	 * @param txtime
	 * @param businessTypeId
	 * @param opbkId
	 * @param bgbkId
	 * @param serchStrs
	 * @param sortname
	 * @param sortorder
	 * @return
	 */
	public Map<String, String> qs_ex_export(String txtime, String businessTypeId, String opbkId, String bgbkId, String serchStrs, String sortname, String sortorder){
		Map<String, String> rtnMap = null;
		
		try{
			rtnMap = new HashMap<String, String>();
			rtnMap.put("serchStrs", serchStrs);
			Map<String, Object> params = new HashMap<String, Object>();
			
			txtime = StrUtils.isEmpty(txtime)?"":DateTimeUtils.convertDate(txtime, "yyyyMMdd", "yyyyMMdd");//交易日期
			opbkId = StrUtils.isEmpty(opbkId)?"":opbkId.equalsIgnoreCase("all")?"":opbkId;//操作行代號		
			businessTypeId = StrUtils.isEmpty(businessTypeId)?"":businessTypeId.equalsIgnoreCase("all")?"":businessTypeId;//業務類別代號
			bgbkId = StrUtils.isEmpty(bgbkId)?"":bgbkId.equalsIgnoreCase("all")?"":bgbkId;//總行代號
			
			StringBuffer sql = new StringBuffer();
			sql.append(getEvdayTimeTolSQL(txtime, businessTypeId, opbkId, bgbkId));
			sql.append("SELECT RSS.* FROM ( ");
			sql.append(" 	SELECT ROWNUMBER() OVER (" + parseSidx(sortname, sortorder, 1) + ") AS ROWNUMBER, "); 
			sql.append("    BANKID, BANKIDANDNAME, TOTALCOUNT, PENDCOUNT, ");
			sql.append("    PRCCOUNT, (CASE WHEN PRCCOUNT > 0 THEN DECIMAL((PRCTIME / PRCCOUNT), 18, 2) ELSE 0 END) AS PRCTIME, ");
			sql.append("    SAVECOUNT, (CASE WHEN SAVECOUNT > 0 THEN DECIMAL((SAVETIME / SAVECOUNT), 18, 2) ELSE 0 END) AS SAVETIME, ");
			sql.append("    DEBITCOUNT, (CASE WHEN DEBITCOUNT > 0 THEN DECIMAL((DEBITTIME / DEBITCOUNT), 18, 2) ELSE 0 END) AS DEBITTIME, ");
			sql.append("    ACHPRCCOUNT, (CASE WHEN ACHPRCCOUNT > 0 THEN DECIMAL((ACHPRCTIME / ACHPRCCOUNT), 18, 2) ELSE 0 END) AS ACHPRCTIME ");
			sql.append("    FROM TEMP___ AS RS "); 
			sql.append(") AS RSS ");
			List list = rponblocktab_Dao.getRptData(sql.toString(), new ArrayList());
			
			int pathType = Arguments.getStringArg("ISWINOPEN").equals("Y")?RptUtils.C_PATH :RptUtils.R_PATH;
			String outputFilePath = RptUtils.export(RptUtils.COLLECTION,pathType, "evdayTraTimeTol", "evdayTraTimeTol", params, list, 2);
			//String outputFilePath = "";
			if(StrUtils.isNotEmpty(outputFilePath)){
				rtnMap.put("result", "TRUE");
				rtnMap.put("msg", outputFilePath);
			}else{
				rtnMap.put("result", "FALSE");
				rtnMap.put("msg", "產生失敗!");
			}
			System.out.println("EXPORT SQL >> " + sql);
		}catch(Exception e){
			e.printStackTrace();
			rtnMap.put("result", "FALSE");
			rtnMap.put("msg", "產生錯誤!");
		}
		
		return rtnMap;
	}
	
	public String getEvdayTimeTolSQL(String txtime, String businessTypeId, String opbkId, String bgbkId){
		List<String> conditions = getEvdayTimeTolCondition(txtime, businessTypeId, opbkId, bgbkId);
		String condition_1 = conditions.get(0);
		String condition_2 = conditions.get(1);
		
		StringBuffer withTemp = new StringBuffer();
		withTemp.append("WITH TEMP AS ( ");
		withTemp.append("    SELECT ");
		withTemp.append("    DOUBLE(DATE_DIFF(ENDTIME, DT_REQ_1)) / CAST (1000000 AS BIGINT) AS TIME_1, ");
		withTemp.append("    DOUBLE(DATE_DIFF(DT_RSP_1, DT_REQ_2)) / CAST (1000000 AS BIGINT) AS TIME_2, ");
		withTemp.append("    (DOUBLE(DATE_DIFF(DT_REQ_2, DT_REQ_1)) + DOUBLE(DATE_DIFF(DT_RSP_2, DT_RSP_1))) / CAST (1000000 AS BIGINT) AS TIME_3, ");
		//20160130 edit by hugo req by SRS_20160122
//		withTemp.append("    (CASE C.TXN_TYPE WHEN '4' THEN (DOUBLE(DATE_DIFF(DT_RSP_3, DT_REQ_3)) / CAST(1000000 AS BIGINT)) ELSE 0 END) AS TIME_4, ");
//		withTemp.append("    (CASE C.TXN_TYPE WHEN '4' THEN ((DOUBLE(DATE_DIFF(DT_REQ_2,DT_REQ_1)) + DOUBLE(DATE_DIFF(DT_REQ_3,DT_RSP_1)) + DOUBLE(DATE_DIFF(DT_RSP_2,DT_RSP_3))) / CAST(1000000 AS BIGINT)) ELSE 0 END) AS TIME_5, ");
		withTemp.append("    (CASE WHEN C.TXN_TYPE IN('4','6') THEN (DOUBLE(DATE_DIFF(DT_RSP_3, DT_REQ_3)) / CAST(1000000 AS BIGINT)) ELSE 0 END) AS TIME_4, ");
		withTemp.append("    (CASE WHEN C.TXN_TYPE IN('4','6') THEN ((DOUBLE(DATE_DIFF(DT_REQ_2,DT_REQ_1)) + DOUBLE(DATE_DIFF(DT_REQ_3,DT_RSP_1)) + DOUBLE(DATE_DIFF(DT_RSP_2,DT_RSP_3))) / CAST(1000000 AS BIGINT)) ELSE 0 END) AS TIME_5, ");
		withTemp.append("    C.* ");
		withTemp.append("    FROM ( ");
		withTemp.append("        SELECT ");
		withTemp.append("        (CASE WHEN RC2 = '3001' THEN 1 ELSE 0 END) OKCOUNT, ");
		withTemp.append("        (CASE WHEN RC2 NOT IN ('3001','0601') THEN 1 ELSE 0 END) FAILCOUNT, ");
		withTemp.append("        (CASE WHEN RC2 = '0601' THEN 1 ELSE 0 END) PENDCOUNT, ");
		withTemp.append("        INTEGER(SUBSTR(A.TXDT,9,2)) AS HOURLAP, A.RESULTSTATUS, ");
		withTemp.append("        A.PCODE, SUBSTR(COALESCE(A.PCODE,'0000'),4,1) AS TXN_TYPE, A.RC2, A.SENDERSTATUS, ");
		withTemp.append("        COALESCE(A.DT_RSP_2, COALESCE(A.DT_RSP_1, COALESCE(A.DT_REQ_2, COALESCE(A.DT_REQ_1, 0)))) AS ENDTIME, ");
		withTemp.append("        (CASE WHEN LENGTH(COALESCE(A.DT_REQ_1,''))=0 THEN '0' ELSE A.DT_REQ_1 END ) DT_REQ_1, ");
		withTemp.append("        (CASE WHEN LENGTH(COALESCE(A.DT_REQ_2,''))=0 THEN '0' ELSE A.DT_REQ_2 END ) DT_REQ_2, ");
		withTemp.append("        (CASE WHEN LENGTH(COALESCE(A.DT_REQ_3,''))=0 THEN '0' ELSE A.DT_REQ_3 END ) DT_REQ_3, ");
		withTemp.append("        (CASE WHEN LENGTH(COALESCE(A.DT_RSP_1,''))=0 THEN '0' ELSE A.DT_RSP_1 END ) DT_RSP_1, ");
		withTemp.append("        (CASE WHEN LENGTH(COALESCE(A.DT_RSP_2,''))=0 THEN '0' ELSE A.DT_RSP_2 END ) DT_RSP_2, ");
		withTemp.append("        (CASE WHEN LENGTH(COALESCE(A.DT_RSP_3,''))=0 THEN '0' ELSE A.DT_RSP_3 END ) DT_RSP_3, ");
		withTemp.append("        A.CLEARINGPHASE, A.SENDERACQUIRE, A.OUTACQUIRE, A.INACQUIRE, A.WOACQUIRE, A.SENDERHEAD, A.OUTHEAD, A.INHEAD, A.WOHEAD ");
		withTemp.append("        FROM ONBLOCKTAB AS A ");
		withTemp.append("        WHERE ( A.RESULTSTATUS IN ('A', 'R') OR (A.RESULTSTATUS = 'P' AND A.SENDERSTATUS <> '1') ) AND COALESCE(DT_REQ_2,'') <> '' ");
		withTemp.append("		 " + (StrUtils.isEmpty(condition_1)? "" : "AND " + condition_1));
		withTemp.append("    ) AS C ");
		withTemp.append("), TEMP_ AS ( ");
		withTemp.append("    SELECT SENDERACQUIRE AS BANKID FROM TEMP GROUP BY SENDERACQUIRE ");
		withTemp.append("    UNION ");
		withTemp.append("    SELECT OUTACQUIRE FROM TEMP GROUP BY OUTACQUIRE ");
		withTemp.append("    UNION ");
		withTemp.append("    SELECT INACQUIRE FROM TEMP GROUP BY INACQUIRE ");
		withTemp.append("    UNION ");
		withTemp.append("    SELECT WOACQUIRE FROM TEMP GROUP BY WOACQUIRE ");
		withTemp.append("), TEMP__ AS ( ");
		withTemp.append("    SELECT ");
		withTemp.append("    (CASE WHEN RC2 <> '0601' AND TIME_1 > TXN_STD_PROC_TIME THEN TIME_1 ELSE 0 END) PRC_FLAG, ");
//20160130 edit by hugo req by SRS_20160122
//		withTemp.append("    (CASE TXN_TYPE ");
//		withTemp.append("    WHEN '2' THEN (CASE WHEN RC2 <> '0601' AND TIME_2 > BANK_SC_STD_PROC_TIME THEN TIME_2 ELSE 0 END) ");
//		withTemp.append("    WHEN '4' THEN (CASE WHEN RC2 <> '0601' AND DT_REQ_3 <> 0 AND TIME_4 > BANK_SC_STD_PROC_TIME THEN TIME_4 ELSE 0 END) ");
		withTemp.append("    (CASE  ");
		withTemp.append("    WHEN TXN_TYPE = '2' THEN (CASE WHEN RC2 <> '0601' AND TIME_2 > BANK_SC_STD_PROC_TIME THEN TIME_2 ELSE 0 END) ");
		withTemp.append("    WHEN TXN_TYPE IN('4','6') THEN (CASE WHEN RC2 <> '0601' AND DT_REQ_3 <> 0 AND TIME_4 > BANK_SC_STD_PROC_TIME THEN TIME_4 ELSE 0 END) ");
		withTemp.append("    ELSE 0 END) SAVE_FLAG, ");
		//20160130 edit by hugo req by SRS_20160122
//		withTemp.append("    (CASE WHEN TXN_TYPE IN ('1','3','4') THEN ( ");
		withTemp.append("    (CASE WHEN TXN_TYPE IN ('1','3','4','5','6') THEN ( ");
		withTemp.append("        CASE WHEN RC2 <> '0601' AND TIME_2 > BANK_SD_STD_PROC_TIME THEN TIME_2 ELSE 0 END ");
		withTemp.append("    ) ELSE 0 END) AS DEBIT_FLAG, ");
		//20160130 edit by hugo req by SRS_20160122
//		withTemp.append("    (CASE TXN_TYPE ");
//		withTemp.append("    WHEN '4' THEN (CASE WHEN RC2 <> '0601' AND TIME_5 > TCH_STD_ECHO_TIME THEN TIME_5 ELSE 0 END) ");
		withTemp.append("    (CASE  ");
		withTemp.append("    WHEN TXN_TYPE IN ('4','6') THEN (CASE WHEN RC2 <> '0601' AND TIME_5 > TCH_STD_ECHO_TIME THEN TIME_5 ELSE 0 END) ");
		withTemp.append("    ELSE (CASE WHEN RC2 <> '0601' AND TIME_3 > TCH_STD_ECHO_TIME THEN TIME_3 ELSE 0 END) ");
		withTemp.append("    END) AS ACHPRC_FLAG, TEMP.* ");
		withTemp.append("    FROM TEMP, SYS_PARA ");
		withTemp.append("), TEMP___ AS ( ");
		withTemp.append("    SELECT ");
		withTemp.append("    B.BANKID, COALESCE(EACHUSER.GETBKNAME(B.BANKID), '') AS BANKIDANDNAME, ");
		withTemp.append("    (SELECT SUM(OKCOUNT + FAILCOUNT + PENDCOUNT) FROM TEMP AS A WHERE (A.SENDERACQUIRE = B.BANKID OR A.OUTACQUIRE = B.BANKID OR A.INACQUIRE = B.BANKID OR A.WOACQUIRE = B.BANKID)) AS TOTALCOUNT, ");
		withTemp.append("    (SELECT SUM(PENDCOUNT) FROM TEMP AS A WHERE (A.SENDERACQUIRE = B.BANKID OR A.OUTACQUIRE = B.BANKID OR A.INACQUIRE = B.BANKID  OR A.WOACQUIRE = B.BANKID )) AS PENDCOUNT, ");
		withTemp.append("    DECIMAL(COALESCE((SELECT SUM(PRC_FLAG) FROM TEMP__ AS A WHERE (A.SENDERACQUIRE = B.BANKID OR A.OUTACQUIRE = B.BANKID OR A.INACQUIRE = B.BANKID OR A.WOACQUIRE = B.BANKID) AND PRC_FLAG <> 0),0),18,2) PRCTIME, ");
		withTemp.append("    DECIMAL(COALESCE((SELECT SUM(SAVE_FLAG) FROM TEMP__ AS A WHERE (A.SENDERACQUIRE = B.BANKID OR A.OUTACQUIRE = B.BANKID OR A.INACQUIRE = B.BANKID OR A.WOACQUIRE = B.BANKID) AND SAVE_FLAG <> 0),0),18,2) SAVETIME, ");
		withTemp.append("    DECIMAL(COALESCE((SELECT SUM(DEBIT_FLAG) FROM TEMP__ AS A WHERE (A.SENDERACQUIRE = B.BANKID OR A.OUTACQUIRE = B.BANKID OR A.INACQUIRE = B.BANKID OR A.WOACQUIRE = B.BANKID) AND DEBIT_FLAG <> 0),0),18,2) DEBITTIME, ");
		withTemp.append("    DECIMAL(COALESCE((SELECT SUM(ACHPRC_FLAG) FROM TEMP__ AS A WHERE (A.SENDERACQUIRE = B.BANKID OR A.OUTACQUIRE = B.BANKID OR A.INACQUIRE = B.BANKID OR A.WOACQUIRE = B.BANKID) AND ACHPRC_FLAG <> 0),0),18,2) ACHPRCTIME, ");
		withTemp.append("    (SELECT SUM(CASE WHEN PRC_FLAG <> 0 THEN 1 ELSE 0 END) FROM TEMP__ AS A WHERE (A.SENDERACQUIRE = B.BANKID OR A.OUTACQUIRE = B.BANKID OR A.INACQUIRE = B.BANKID OR A.WOACQUIRE = B.BANKID)) AS PRCCOUNT, ");
		withTemp.append("    (SELECT SUM(CASE WHEN SAVE_FLAG <> 0 THEN 1 ELSE 0 END) FROM TEMP__ AS A WHERE (A.SENDERACQUIRE = B.BANKID OR A.OUTACQUIRE = B.BANKID OR A.INACQUIRE = B.BANKID OR A.WOACQUIRE = B.BANKID)) AS SAVECOUNT, ");
		withTemp.append("    (SELECT SUM(CASE WHEN DEBIT_FLAG <> 0 THEN 1 ELSE 0 END) FROM TEMP__ AS A WHERE (A.SENDERACQUIRE = B.BANKID OR A.OUTACQUIRE = B.BANKID OR A.INACQUIRE = B.BANKID OR A.WOACQUIRE = B.BANKID)) AS DEBITCOUNT, ");
		withTemp.append("    (SELECT SUM(CASE WHEN ACHPRC_FLAG <> 0 THEN 1 ELSE 0 END) FROM TEMP__ AS A WHERE (A.SENDERACQUIRE = B.BANKID OR A.OUTACQUIRE = B.BANKID OR A.INACQUIRE = B.BANKID OR A.WOACQUIRE = B.BANKID)) AS ACHPRCCOUNT ");
		withTemp.append("    FROM TEMP_ AS B ");
		withTemp.append("     " + (StrUtils.isEmpty(condition_2)? "" : "WHERE " + condition_2));
		withTemp.append("), TEMP____ AS ( ");
		withTemp.append("    SELECT ");
		withTemp.append("    SUM(TOTALCOUNT) AS TOTALCOUNT, SUM(PENDCOUNT) AS PENDCOUNT, ");
		withTemp.append("    SUM(PRCCOUNT) AS PRCCOUNT, SUM(SAVECOUNT) AS SAVECOUNT, ");
		withTemp.append("    SUM(DEBITCOUNT) AS DEBITCOUNT, SUM(ACHPRCCOUNT) AS ACHPRCCOUNT, ");
		withTemp.append("    SUM(PRCTIME) AS PRCTIME, SUM(SAVETIME) AS SAVETIME, ");
		withTemp.append("    SUM(DEBITTIME) AS DEBITTIME, SUM(ACHPRCTIME) AS ACHPRCTIME ");
		withTemp.append("    FROM TEMP___ ");
		withTemp.append(")");
		
		return withTemp.toString();
	}
	
	public List<String> getEvdayTimeTolCondition(String txtime, String businessTypeId, String opbkId, String bgbkId){
		List<String> conditions = new ArrayList<String>();
		
		List<String> conditions_1 = new ArrayList<String>();
		List<String> conditions_2 = new ArrayList<String>();
		
		if(StrUtils.isNotEmpty(txtime)){
			//20151022 HUANGPU 改用交易日期查較合邏輯
			//conditions_1.add(" A.BIZDATE= '"+txtime+"' ");
			conditions_1.add(" A.TXDATE= '"+txtime+"' ");
		}
		if(StrUtils.isNotEmpty(opbkId)){//XX行所屬操作行
			conditions_1.add(" (A.SENDERACQUIRE= '"+opbkId+"' OR A.OUTACQUIRE= '"+opbkId+"' OR A.INACQUIRE= '"+opbkId+"' OR A.WOACQUIRE= '"+opbkId+"') ");
			conditions_2.add(" BANKID = '" + opbkId + "' ");
		}
		if(StrUtils.isNotEmpty(bgbkId)){//XX行所屬總行
			conditions_1.add(" (A.SENDERHEAD= '"+bgbkId+"' OR A.OUTHEAD= '"+bgbkId+"' OR A.INHEAD= '"+bgbkId+"' OR A.WOHEAD= '"+bgbkId+"') ");
		}
		if(StrUtils.isNotEmpty(businessTypeId)){//業務類別
			conditions_1.add(" A.PCODE LIKE '" + businessTypeId.substring(0,2) + "%' ");
		}
		
		conditions.add( combine(conditions_1) );
		conditions.add( combine(conditions_2) );
		
		return conditions;
	}
	
	//每日交易處理時間統計
	public String getEvdayTimeTolList(Map<String, String> params){
		String txtime = StrUtils.isEmpty(params.get("TXTIME"))?"":params.get("TXTIME");//交易日期
		String opbk_id = StrUtils.isEmpty(params.get("OPBK_ID"))?"none":params.get("OPBK_ID").equalsIgnoreCase("all")?"":params.get("OPBK_ID");//操作行代號		
		String bus_Type_Id = StrUtils.isEmpty(params.get("BUSINESS_TYPE_ID"))?"none":params.get("BUSINESS_TYPE_ID").equalsIgnoreCase("all")?"":params.get("BUSINESS_TYPE_ID");//業務類別代號
		String bgbkId = StrUtils.isEmpty(params.get("BGBK_ID"))?"none":params.get("BGBK_ID").equalsIgnoreCase("all")?"":params.get("BGBK_ID");//總行代號
		int pageNo = StrUtils.isEmpty(params.get("page")) ?0:Integer.valueOf(params.get("page"));
		int pageSize = StrUtils.isEmpty(params.get("rows")) ?Integer.valueOf(Arguments.getStringArg("PAGE.SIZE")):Integer.valueOf(params.get("rows"));
		String isSearch = StrUtils.isEmpty(params.get("isSearch")) ?"Y":params.get("isSearch");
		List<VW_ONBLOCKTAB> list = null;
		Map rtnMap = new HashMap();
		Page page = null;
		String condition_1 = "", condition_2 = "";
		int time = 0;
		try {
			list = new ArrayList<VW_ONBLOCKTAB>();
			txtime = DateTimeUtils.convertDate(txtime, "yyyyMMdd", "yyyyMMdd");
			
			List<String> conditions = getEvdayTimeTolCondition(txtime, bus_Type_Id, opbk_id, bgbkId);
			condition_1 = conditions.get(0);
			condition_2 = conditions.get(1);
			
			StringBuffer withTemp = new StringBuffer();
			/*
			withTemp.append("WITH TEMP AS ( ");
			withTemp.append("    SELECT * FROM ( ");
			withTemp.append("        SELECT SENDERACQUIRE, SENDERHEAD, OUTHEAD, INHEAD, PCODE, NEWRESULT AS RESULTSTATUS, ");  
			withTemp.append("        (case when length(COALESCE(DT_Req_1,''))=0  then '0' else DT_Req_1 end ) DT_Req_1, ");  
			withTemp.append("        (case when length(COALESCE(DT_Req_2,''))=0  then '0' else DT_Req_2 end ) DT_Req_2, ");
			withTemp.append("        (case when length(COALESCE(DT_Req_3,''))=0  then '0' else DT_Req_3 end ) DT_Req_3, ");
			withTemp.append("        (case when length(COALESCE(DT_Rsp_1,''))=0  then '0' else DT_Rsp_1 end ) DT_Rsp_1, ");  
			withTemp.append("        (case when length(COALESCE(DT_Rsp_2,''))=0  then '0' else DT_Rsp_2 end ) DT_Rsp_2, ");
			withTemp.append("        (case when length(COALESCE(DT_Rsp_3,''))=0  then '0' else DT_Rsp_3 end ) DT_Rsp_3, ");
			withTemp.append("        (case when length(COALESCE(DT_Con_1,''))=0  then '0' else DT_Con_1 end ) DT_Con_1, ");  
			withTemp.append("        (case when length(COALESCE(DT_Con_2,''))=0  then '0' else DT_Con_2 end ) DT_Con_2, "); 
			withTemp.append("        (case when length(COALESCE(DT_Con_3,''))=0  then '0' else DT_Con_3 end ) DT_Con_3, ");
			withTemp.append("        (case when length(COALESCE(DT_Con_2,'')) = 0 then ");
			withTemp.append("            (case when length(COALESCE(DT_Con_1,'')) = 0 then ");
			withTemp.append("                (case when length(COALESCE(DT_Rsp_2,'')) = 0 then ");
			withTemp.append("                    (case when length(COALESCE(DT_Rsp_1,'')) = 0 then ");
			withTemp.append("                        (case when length(COALESCE(DT_Req_2,'')) = 0 then COALESCE(DT_Req_1,'') else COALESCE(DT_Req_2,'') end) ");
			withTemp.append("                        else COALESCE(DT_Rsp_1,'') end) ");
			withTemp.append("                else COALESCE(DT_Rsp_2,'') end) ");
			withTemp.append("            else COALESCE(DT_Con_1,'') end) ");
			withTemp.append("        else COALESCE(DT_Con_2,'') end) EndTime ");
			withTemp.append("        FROM VW_ONBLOCKTAB ");
			withTemp.append("        WHERE LENGTH(COALESCE(TIMEOUTCODE, '')) = 0 ");
			withTemp.append("        " + (StrUtils.isEmpty(condition_1)? "" : "AND " + condition_1));
			withTemp.append("    ) AS A LEFT JOIN EACH_TXN_CODE C ON C.EACH_TXN_ID = A.PCODE ");
			withTemp.append("    " + (StrUtils.isEmpty(condition_2)? "" : "WHERE " + condition_2));
			withTemp.append(") ");
			*/
			withTemp.append(getEvdayTimeTolSQL(txtime, bus_Type_Id, opbk_id, bgbkId));
			
			/*
			StringBuffer fromAndWhere = new StringBuffer();
			fromAndWhere.append("FROM ( ");
			fromAndWhere.append("    SELECT * FROM ( ");
			fromAndWhere.append("        SELECT ROWNUMBER() OVER( ");
			fromAndWhere.append(parseSidx(params.get("sidx"), params.get("sord"), 1));
			fromAndWhere.append("        ) AS ROWNUMBER, T.* FROM (");
			fromAndWhere.append("            SELECT SENDERHEAD AS BANKID FROM TEMP GROUP BY SENDERHEAD ");
			fromAndWhere.append("            UNION ");
			fromAndWhere.append("            SELECT OUTHEAD FROM TEMP GROUP BY OUTHEAD ");
			fromAndWhere.append("            UNION ");
			fromAndWhere.append("            SELECT INHEAD FROM TEMP GROUP BY INHEAD ");
			fromAndWhere.append("        ) AS T ");
			fromAndWhere.append("        " + (StrUtils.isEmpty(condition_3)? "" : "WHERE " + condition_3));
			fromAndWhere_all.append(fromAndWhere);
			fromAndWhere.append("    ) WHERE ROWNUMBER >= " + (Page.getStartOfPage(pageNo, pageSize) + 1) + " AND ROWNUMBER <= " + (pageNo * pageSize) + " ");
			fromAndWhere_all.append(") ");
			fromAndWhere.append(") AS A ");
			fromAndWhere_all.append(") AS A ");
			*/
			
			//先算出總計的欄位值，放到rtnMap裡面，到頁面再放到下面的總計Grid裡面
			StringBuffer dataSumSQL = new StringBuffer();
			dataSumSQL.append(withTemp);
			dataSumSQL.append("SELECT ");
			dataSumSQL.append("TOTALCOUNT, PENDCOUNT, ");
			dataSumSQL.append("PRCCOUNT, (CASE WHEN PRCCOUNT > 0 THEN DECIMAL((PRCTIME / PRCCOUNT), 18, 2) ELSE 0 END) AS PRCTIME, ");
			dataSumSQL.append("SAVECOUNT, (CASE WHEN SAVECOUNT > 0 THEN DECIMAL((SAVETIME / SAVECOUNT), 18, 2) ELSE 0 END) AS SAVETIME, ");
			dataSumSQL.append("DEBITCOUNT, (CASE WHEN DEBITCOUNT > 0 THEN DECIMAL((DEBITTIME / DEBITCOUNT), 18, 2) ELSE 0 END) AS DEBITTIME, ");
			dataSumSQL.append("ACHPRCCOUNT, (CASE WHEN ACHPRCCOUNT > 0 THEN DECIMAL((ACHPRCTIME / ACHPRCCOUNT), 18, 2) ELSE 0 END) AS ACHPRCTIME ");
			dataSumSQL.append("FROM TEMP____ ");
			/*
			dataSumSQL.append("SELECT SUM(FIRECOUNT) AS FIRECOUNT FROM ( ");
			dataSumSQL.append("    SELECT (SELECT COUNT(*) FROM TEMP WHERE (SENDERHEAD = A.BANKID OR OUTHEAD = A.BANKID OR INHEAD = A.BANKID)) AS FIRECOUNT ");
			dataSumSQL.append("    " + fromAndWhere_all);
			dataSumSQL.append(")");
			*/
			System.out.println("dataSumSQL="+dataSumSQL.toString());
			List dataSumList = commonSpringDao.list(dataSumSQL.toString(),null);
			rtnMap.put("dataSumList",dataSumList);
			
			System.out.println("pageNo==>"+pageNo);
			System.out.println("pageSize==>"+pageSize);
			StringBuffer countQuery = new StringBuffer();
			StringBuffer sql = new StringBuffer();
			
			countQuery.append(withTemp);
			countQuery.append("SELECT COUNT(*) AS NUM FROM TEMP___");
			/*
			countQuery.append(withTemp);
			countQuery.append("SELECT COUNT(*) AS NUM ");
			countQuery.append(fromAndWhere_all);
			*/
			
			sql.append(withTemp);
			/*
			sql.append("SELECT ");
			sql.append("A.BANKID || '-' || (SELECT coalesce(BGBK_NAME,'') from bank_group where bgbk_id=A.BANKID) SENDERACQUIRE, ");
			sql.append("(SELECT COUNT(*) FROM TEMP WHERE (SENDERHEAD = A.BANKID OR OUTHEAD = A.BANKID OR INHEAD = A.BANKID)) AS FIRECOUNT, ");
			sql.append("(SELECT (SUM(DOUBLE(DATE_DIFF(ENDTIME, DT_REQ_1))) / 1000000 / COUNT(*)) FROM TEMP WHERE (SENDERHEAD = A.BANKID OR OUTHEAD = A.BANKID OR INHEAD = A.BANKID)) AS AVGTIME, ");
			sql.append("(SELECT SUM( ");
			sql.append("    (CASE WHEN PCODE LIKE '2%4' ");
			sql.append("    THEN (DOUBLE(DATE_DIFF(DT_REQ_2,DT_REQ_1)) + DOUBLE(DATE_DIFF(DT_REQ_3,DT_RSP_1))) / 2 ");	
			sql.append("    ELSE DOUBLE(DATE_DIFF(DT_REQ_2,DT_REQ_1)) ");
			sql.append("    END) ");
			sql.append(") / 1000000 / COUNT(*) FROM TEMP WHERE (SENDERHEAD = A.BANKID OR OUTHEAD = A.BANKID OR INHEAD = A.BANKID) GROUP BY 'G') AS ACHAVGTIME, ");
			sql.append("(SELECT SUM( ");
			sql.append("    (CASE WHEN PCODE LIKE '2%4' ");
			sql.append("    THEN (DOUBLE(DATE_DIFF(DT_RSP_2,DT_RSP_3))) ");
			sql.append("    ELSE (DOUBLE(DATE_DIFF(DT_RSP_2,DT_RSP_1))) ");
			sql.append("    END) ");
			sql.append(") / 1000000 / COUNT(*) FROM TEMP WHERE (SENDERHEAD = A.BANKID OR OUTHEAD = A.BANKID OR INHEAD = A.BANKID) GROUP BY 'G') AS ACHSAVETIME, ");
			sql.append("(SELECT (SUM(DOUBLE(DATE_DIFF(DT_CON_2,DT_CON_1))) / 1000000 / COUNT(*)) FROM TEMP WHERE (SENDERHEAD = A.BANKID OR OUTHEAD = A.BANKID OR INHEAD = A.BANKID)) AS ACHDEBITTIME, ");
			sql.append("COALESCE((SELECT (SUM(DOUBLE(DATE_DIFF(DT_RSP_1,DT_REQ_2))) / 1000000 / COUNT(*)) FROM TEMP WHERE INHEAD = A.BANKID),0) AS INSAVETIME, ");
			sql.append("COALESCE((SELECT (SUM(DOUBLE(DATE_DIFF(DT_RSP_1,DT_REQ_2))) / 1000000 / COUNT(*)) FROM TEMP WHERE OUTHEAD = A.BANKID),0) AS OUTDEBITTIME, ");
			sql.append("(SELECT TCH_STD_ECHO_TIME FROM SYS_PARA) TCH_STD_ECHO_TIME, ");
			sql.append("(SELECT PARTY_STD_ECHO_TIME FROM SYS_PARA) PARTY_STD_ECHO_TIME, ");
			sql.append("(SELECT TXN_STD_PROC_TIME FROM SYS_PARA) TXN_STD_PROC_TIME ");
			sql.append(fromAndWhere);
			if(StrUtils.isNotEmpty(params.get("sidx"))){
				if(params.get("sidx").contains("ASC") || params.get("sidx").contains("DESC") ||
				   params.get("sidx").contains("asc") || params.get("sidx").contains("desc")){
					sql.append(" ORDER BY "+params.get("sidx"));
				}else{
					sql.append(" ORDER BY "+params.get("sidx")+" "+params.get("sord"));
				}
			}
			*/
			
			sql.append("SELECT RSS.* FROM ( ");
			sql.append(" 	SELECT ROWNUMBER() OVER (" + parseSidx(params.get("sidx"), params.get("sord"), 1) + ") AS ROWNUMBER, "); 
			sql.append("    BANKID, BANKIDANDNAME, TOTALCOUNT, PENDCOUNT, ");
			sql.append("    PRCCOUNT, (CASE WHEN PRCCOUNT > 0 THEN DECIMAL((PRCTIME / PRCCOUNT), 18, 2) ELSE 0 END) AS PRCTIME, ");
			sql.append("    SAVECOUNT, (CASE WHEN SAVECOUNT > 0 THEN DECIMAL((SAVETIME / SAVECOUNT), 18, 2) ELSE 0 END) AS SAVETIME, ");
			sql.append("    DEBITCOUNT, (CASE WHEN DEBITCOUNT > 0 THEN DECIMAL((DEBITTIME / DEBITCOUNT), 18, 2) ELSE 0 END) AS DEBITTIME, ");
			sql.append("    ACHPRCCOUNT, (CASE WHEN ACHPRCCOUNT > 0 THEN DECIMAL((ACHPRCTIME / ACHPRCCOUNT), 18, 2) ELSE 0 END) AS ACHPRCTIME ");
			sql.append("    FROM TEMP___ AS RS "); 
			sql.append(") AS RSS "); 
			sql.append("WHERE ROWNUMBER >= " + (Page.getStartOfPage(pageNo, pageSize) + 1) + " AND ROWNUMBER <= " + (pageNo * pageSize));
			/*
			String cols[] = {"SENDERACQUIRE","FIRECOUNT","AVGTIME","ACHAVGTIME","ACHSAVETIME","ACHDEBITTIME",
							 "INSAVETIME","OUTDEBITTIME","TCH_STD_ECHO_TIME","PARTY_STD_ECHO_TIME","TXN_STD_PROC_TIME"};
			*/
			String cols[] = {
					"BANKID", "BANKIDANDNAME", "TOTALCOUNT", "PENDCOUNT",
					"PRCCOUNT", "PRCTIME", "SAVECOUNT", "SAVETIME",
					"DEBITCOUNT", "DEBITTIME", "ACHPRCCOUNT", "ACHPRCTIME"
			};
			System.out.println("countQuery===>"+countQuery.toString());
			System.out.println("sql===>"+sql.toString());
			page= onblocktab_Dao.getDataIIII(pageNo, pageSize, countQuery.toString(), sql.toString(), cols, ONBLOCKTABbean.class);
//			list = (List<ONBLOCKTAB>) page.getResult();
			list = (List<VW_ONBLOCKTAB>) page.getResult();
			System.out.println("ONBLOCKTAB.list>>"+list);
			
			list = list!=null&& list.size() ==0 ?null:list;
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
//			必須是按下UI的查詢才紀錄
			if(isSearch.equals("Y")){
				userlog_bo.writeLog("C", null, null, params);
			}
		} catch (Exception e) {
			e.printStackTrace();
			rtnMap.put("total", 1);
			rtnMap.put("page", 1);
			rtnMap.put("records", 0);
			rtnMap.put("rows", new ArrayList<>());
			rtnMap.put("msg", "查詢失敗");
//			userlog_bo.writeFailLog("C", rtnMap, null, null, params);
		}
		
		String json = JSONUtils.map2json(rtnMap) ;
		System.out.println("json===>"+json+"<===");
		return json;
	}
	
	public String getEvdayTimeTolOrderCondition(String sidx){
		/*
		if(sidx.startsWith("SENDERACQUIRE")){
			return "BANKID";
		}else if(sidx.startsWith("FIRECOUNT")){
			return "(SELECT COUNT(*) FROM TEMP WHERE (SENDERHEAD = T.BANKID OR OUTHEAD = T.BANKID OR INHEAD = T.BANKID))";
		}else if(sidx.startsWith("AVGTIME")){
			return "(SELECT (SUM(DOUBLE(DATE_DIFF(ENDTIME, DT_REQ_1))) / 1000000 / COUNT(*)) FROM TEMP WHERE (SENDERHEAD = T.BANKID OR OUTHEAD = T.BANKID OR INHEAD = T.BANKID))";
		}else if(sidx.startsWith("ACHAVGTIME")){
			return "(SELECT SUM(     (CASE WHEN PCODE LIKE '2%4'     THEN (DOUBLE(DATE_DIFF(DT_REQ_2,DT_REQ_1)) + DOUBLE(DATE_DIFF(DT_REQ_3,DT_RSP_1))) / 2     ELSE DOUBLE(DATE_DIFF(DT_REQ_2,DT_REQ_1))     END) ) / 1000000 / COUNT(*) FROM TEMP WHERE (SENDERHEAD = T.BANKID OR OUTHEAD = T.BANKID OR INHEAD = T.BANKID) GROUP BY 'G')";
		}else if(sidx.startsWith("ACHSAVETIME")){
			return "(SELECT SUM(     (CASE WHEN PCODE LIKE '2%4'     THEN (DOUBLE(DATE_DIFF(DT_RSP_2,DT_RSP_3)))     ELSE (DOUBLE(DATE_DIFF(DT_RSP_2,DT_RSP_1)))     END) ) / 1000000 / COUNT(*) FROM TEMP WHERE (SENDERHEAD = T.BANKID OR OUTHEAD = T.BANKID OR INHEAD = T.BANKID) GROUP BY 'G')";
		}else if(sidx.startsWith("ACHDEBITTIME")){
			return "(SELECT (SUM(DOUBLE(DATE_DIFF(DT_CON_2,DT_CON_1))) / 1000000 / COUNT(*)) FROM TEMP WHERE (SENDERHEAD = T.BANKID OR OUTHEAD = T.BANKID OR INHEAD = T.BANKID))";
		}else if(sidx.startsWith("INSAVETIME")){
			return "COALESCE((SELECT (SUM(DOUBLE(DATE_DIFF(DT_RSP_1,DT_REQ_2))) / 1000000 / COUNT(*)) FROM TEMP WHERE INHEAD = T.BANKID),0)";
		}else if(sidx.startsWith("OUTDEBITTIME")){
			return "COALESCE((SELECT (SUM(DOUBLE(DATE_DIFF(DT_RSP_1,DT_REQ_2))) / 1000000 / COUNT(*)) FROM TEMP WHERE OUTHEAD = T.BANKID),0)";
		}else{
			return sidx;
		}
		*/
		return sidx;
	}
	
	/**
	 * type = 1 = 每日交易處理時間統計;
	 * @param sidx
	 * @param sord
	 * @param type
	 * @return
	 */
	public String parseSidx(String sidx, String sord, int type){
		StringBuffer orderbyStr = new StringBuffer("");
		if(StrUtils.isNotEmpty(sidx)){
			orderbyStr.append("ORDER BY ");
			String strAry[] = sidx.split(",");
			if(strAry.length == 1){
				if(type == 1){
					orderbyStr.append(getEvdayTimeTolOrderCondition(strAry[0].trim()) + " ");
				}
				if(StrUtils.isNotEmpty(sord)){
					orderbyStr.append(sord);
				}
			}else{
				for(int i = 0; i < strAry.length; i++){
					strAry[i] = strAry[i].trim();
					if(type == 1){
						orderbyStr.append(getEvdayTimeTolOrderCondition(strAry[i]));
					}
					if(strAry[i].contains(" ")){
						orderbyStr.append(" " + strAry[i].split(" ")[1]);
					}
					if(i < strAry.length - 1){
						orderbyStr.append(", ");
					}
				}
			}
		}
		return orderbyStr.toString();
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
	
	
	public  Map<String, String> export_csv(Map<String, String> params){
		System.out.println("export_csv params : " + params.toString());
		Map<String, Object> paramsV = new HashMap<String, Object>();
		//日期區間(票交端為區間，銀行端僅可選單一日期 TXTIME1)
		String stadate = StrUtils.isEmpty(params.get("TXTIME1"))?"":DateTimeUtils.convertDate(params.get("TXTIME1"), "yyyyMMdd", "yyyyMMdd");
		System.out.println();
		String enddate = StrUtils.isEmpty(params.get("TXTIME2"))?"":DateTimeUtils.convertDate(params.get("TXTIME2"), "yyyyMMdd", "yyyyMMdd");
		//日期區間(TXDT)
		String sdate = StrUtils.isEmpty(params.get("TXTIME3"))?"":DateTimeUtils.convertDate(params.get("TXTIME3"), "yyyyMMdd", "yyyyMMdd");
		String edate = StrUtils.isEmpty(params.get("TXTIME4"))?"":DateTimeUtils.convertDate(params.get("TXTIME4"), "yyyyMMdd", "yyyyMMdd");
		String hour1 = StrUtils.isEmpty(params.get("HOUR1"))?"":params.get("HOUR1").trim();
		String hour2 = StrUtils.isEmpty(params.get("HOUR2"))?"":params.get("HOUR2").trim();
		String mon1 = StrUtils.isEmpty(params.get("MON1"))?"":params.get("MON1").trim();
		String mon2 = StrUtils.isEmpty(params.get("MON2"))?"":params.get("MON2").trim();
		
		sdate = sdate + hour1 + mon1;
		edate = edate + hour2 + mon2;
		//交易追蹤序號
		String stan = StrUtils.isEmpty(params.get("STAN"))?"":params.get("STAN").trim();
		//交易追蹤序號
		String stan2 = StrUtils.isEmpty(params.get("STAN2"))?"":params.get("STAN2").trim();
		//收費類型
		String fee_type = StrUtils.isEmpty(params.get("FEE_TYPE"))?"":params.get("FEE_TYPE").trim();
		//發動/收受選項
		String cdNumRao = StrUtils.isEmpty(params.get("CDNUMRAO"))?"":params.get("CDNUMRAO");
		//統編
		String cdNumId = StrUtils.isEmpty(params.get("CARDNUM_ID"))?"":params.get("CARDNUM_ID").trim();
		//入帳/扣款選項
		String opAction1 = StrUtils.isEmpty(params.get("opAction1"))?"":params.get("opAction1");
		//帳號
		String acctNo = StrUtils.isEmpty(params.get("USERID"))?"":params.get("USERID").trim();
		//金額
		String txamt = StrUtils.isEmpty(params.get("TXAMT"))?"":params.get("TXAMT").trim();
		//查詢角度
		String searchAspect = StrUtils.isEmpty(params.get("searchAspect"))?"":params.get("searchAspect").trim();
		//操作行
		String opbkId = StrUtils.isEmpty(params.get("OPBK_ID"))?"":params.get("OPBK_ID").trim();
		//總行
		String bgbkId = StrUtils.isEmpty(params.get("BGBK_ID"))?"":params.get("BGBK_ID").trim();
		//分行
		String brbkId = StrUtils.isEmpty(params.get("BRBK_ID"))?"":params.get("BRBK_ID").trim();
		//清算階段代號
		String clearingphase = StrUtils.isEmpty(params.get("CLEARINGPHASE"))?"":params.get("CLEARINGPHASE");
		//業務類別代號
		String bus_Type_Id = StrUtils.isEmpty(params.get("BUSINESS_TYPE_ID"))?"":params.get("BUSINESS_TYPE_ID");
		//交易結果
		String resultstatus = StrUtils.isEmpty(params.get("RESULTSTATUS"))?"":params.get("RESULTSTATUS");
		//交易結果
		String pfclass = StrUtils.isEmpty(params.get("PFCLASS"))?"":params.get("PFCLASS");
		//交易結果
		String tollid = StrUtils.isEmpty(params.get("TOLLID"))?"":params.get("TOLLID");
		//交易結果
		String chargetype = StrUtils.isEmpty(params.get("CHARGETYPE"))?"":params.get("CHARGETYPE");
		//交易結果
		String billflag = StrUtils.isEmpty(params.get("BILLFLAG"))?"":params.get("BILLFLAG");
		//交易結果
		String txid = StrUtils.isEmpty(params.get("TXID"))?"":params.get("TXID");
		//回應狀態RC
		String resstatus = StrUtils.isEmpty(params.get("RESSTATUS"))?"all":params.get("RESSTATUS");
		//回應狀態RC
		String rcserctext = StrUtils.isEmpty(params.get("RCSERCTEXT"))?"":params.get("RCSERCTEXT");
		System.out.println("RCSERCTEXT >> "+ rcserctext);
		
		//是否包含異常資料(N表示不包含)
		String garbageData = params.get("GARBAGEDATA")==null?"N":"Y";
		//是否包含整批資料("N"表示不包含)
		String filter_bat = params.get("FILTER_BAT")==null?"N":"Y";
		String isSearch = StrUtils.isEmpty(params.get("isSearch")) ?"Y":params.get("isSearch");
		List<VW_ONBLOCKTAB> list = null;
		Map rtnMap = new HashMap();
		List<String> conditions_1 = new ArrayList<String>();
		List<String> conditions_2 = new ArrayList<String>();
		String condition_1 = "" , condition_2 = "";
		try{
			/* 20150210 HUANGPU 改以營業日(BIZDATE)查詢資料，非交易日期時間(TXDT) */
			if(StrUtils.isNotEmpty(stadate) && StrUtils.isNotEmpty(enddate)){
				conditions_1.add(" (BIZDATE >= '" + stadate + "' AND BIZDATE <= '"+enddate+"') ");
			}else if(StrUtils.isNotEmpty(stadate)){
				conditions_1.add(" BIZDATE >= '" + stadate + "' ");
			}else if(StrUtils.isNotEmpty(enddate)){
				conditions_1.add(" BIZDATE <= '" + enddate + "' ");
			}
			
			/* 20200413 vincenthuang 增加非交易日期時間(TXDT) */
			if(StrUtils.isNotEmpty(sdate) && StrUtils.isNotEmpty(edate)){
				conditions_1.add(" ( TXDT BETWEEN '"+sdate+"00' AND '"+edate+"59' )");
			}else if(StrUtils.isNotEmpty(sdate)){
				conditions_1.add(" TXDT >= '" + sdate + "00' ");
			}else if(StrUtils.isNotEmpty(edate)){
				conditions_1.add(" TXDT <= '" + edate + "59' ");
			}
			
			if(StrUtils.isNotEmpty(stan)&&StrUtils.isNotEmpty(stan2)) {
				conditions_1.add(" ( STAN BETWEEN '"+stan+ "' AND '"+stan2+"' )");
			}else if(StrUtils.isNotEmpty(stan)&&!StrUtils.isNotEmpty(stan2)){
				conditions_1.add(" STAN = '" + stan + "' ");
			}
			
			if(StrUtils.isNotEmpty(fee_type)){
				conditions_1.add(" FEE_TYPE = '" + fee_type + "' ");
			}
	
			if(StrUtils.isNotEmpty(cdNumId)){
				if(cdNumRao.equals("SENDID")){
					conditions_1.add(" SENDERID = '" + cdNumId + "' ");
				}else if(cdNumRao.equals("RECVID")){
					conditions_1.add(" RECEIVERID = '" + cdNumId + "' ");
				}
			}
			if(StrUtils.isNotEmpty(acctNo)){
				if(opAction1.equals("IN")){
					conditions_1.add(" INACCTNO = '" + acctNo + "' ");
				}else if(opAction1.equals("OUT")){
					conditions_1.add(" OUTACCTNO = '" + acctNo + "' ");
				}
			}
			if(StrUtils.isNotEmpty(txamt)){
				if("N".equals(garbageData)){
					conditions_1.add(" NEWTXAMT = ISNUMERIC('" + txamt + "') ");
				}else{
					conditions_1.add(" NEWTXAMT = '" + txamt + "' ");
				}
			}
			if(StrUtils.isNotEmpty(clearingphase)){
				conditions_1.add(" CLEARINGPHASE = '" + clearingphase + "' ");
			}
			if(StrUtils.isNotEmpty(bus_Type_Id)){
				conditions_1.add(" SUBSTR(PCODE,1,2) LIKE SUBSTR('" + bus_Type_Id + "',1,2) ");
			}
			//(U=處理中需另外轉換條件)
			if(StrUtils.isNotEmpty(resultstatus)){
				if(resultstatus.equals("U")){
					conditions_1.add(" (RESULTSTATUS = 'P' AND SENDERSTATUS = '1') ");
				}else if(resultstatus.equals("AP")){
					conditions_1.add(" ( (RESULTSTATUS IN ('A', 'P')) AND SENDERSTATUS <> '1') ");
				}else{
					conditions_1.add(" (RESULTSTATUS = '" + resultstatus + "' AND SENDERSTATUS <> '1') ");
				}
			}
			//20170719新增的查詢欄位
			if(StrUtils.isNotEmpty(pfclass)){
				conditions_1.add(" PFCLASS = '" + pfclass + "' ");
			}
			if(StrUtils.isNotEmpty(tollid)){
				conditions_1.add(" TOLLID = '" + tollid + "' ");
			}
			if(StrUtils.isNotEmpty(chargetype)){
				conditions_1.add(" CHARGETYPE = '" + chargetype + "' ");
			}
			if(StrUtils.isNotEmpty(billflag)){
				conditions_1.add(" BILLFLAG = '" + billflag + "' ");
			}
			//20200708新增的查詢欄位
			if(StrUtils.isNotEmpty(txid)){
				conditions_1.add(" TXID = '" + txid + "' ");
			}
			//如果回應狀態的下拉式選單的值不為全部
			if(!"all".equals(resstatus)){
				//先切分後面輸入的值,以,分隔
				String[] rcarry = rcserctext.split(",");
				//如果有一個數字以上,串條件
				if(rcarry.length>1) {
					String inCondition="'";
					for(int i = 0 ; i< rcarry.length ; i++) {
						if(i==rcarry.length-1) {
							inCondition=inCondition+rcarry[i]+"'";
						}else {
							inCondition=inCondition+rcarry[i]+"','";
						}
					}
					System.out.println("inCondition >> "+inCondition );
					conditions_1.add( resstatus +" in("+ inCondition +")");
				//只有一個數字
				}else {
					conditions_1.add( resstatus +" = '" + rcarry[0] + "' ");
				}
				
			}
			
			//不包含異常資料才需下條件過濾
			if(garbageData.equals("N")){
				conditions_1.add(" COALESCE(GARBAGEDATA,'') <> '*' ");
			}
			
			if(StrUtils.isNotEmpty(searchAspect)){
				
				if(filter_bat.equals("Y")){
					conditions_2.addAll(conditions_1);
					conditions_1.add(" COALESCE(FLBIZDATE,'') = '' ");
					conditions_2.add(" COALESCE(FLBIZDATE,'') <> '' ");
				}else{// 20160310 add by hugo req by UAT-2016309-01
					conditions_1.add(" COALESCE(FLBIZDATE,'') = '' ");
				}
				if(StrUtils.isNotEmpty(opbkId) && !opbkId.equals("all")){
//					20151012 add by hugo 如過濾整批資料，發動行要過濾所掉所有整批資料，
					if(searchAspect.equals("SENDER")){
						conditions_1.add(" SENDERACQUIRE = '" + opbkId + "' ");
						conditions_2.add(" (SENDERACQUIRE = '" + opbkId + "' AND substr(COALESCE(PCODE,''),4) NOT IN ( '1' ,'2' ,'3' , '4') )");
					}else if(searchAspect.equals("IN")){
						conditions_1.add(" INACQUIRE = '" + opbkId + "' ");
						conditions_2.add(" (INACQUIRE = '" + opbkId + "' AND substr(COALESCE(PCODE,''),4) = '2' )");
					}else if(searchAspect.equals("OUT")){
						conditions_1.add(" OUTACQUIRE = '" + opbkId + "' ");
						conditions_2.add(" (OUTACQUIRE = '" + opbkId + "' AND substr(COALESCE(PCODE,''),4) = '1' )");
					}else if(searchAspect.equals("WO")){
						conditions_1.add(" WOACQUIRE = '" + opbkId + "' ");
						conditions_2.add(" (WOACQUIRE = '" + opbkId + "' AND substr(COALESCE(PCODE,''),4) = '3' )");
					}
				}else{
					if(searchAspect.equals("SENDER")){
//						conditions_2.add("  substr(COALESCE(PCODE,''),4) NOT IN ( '1' ,'2' ,'3' , '4') ");
					}else if(searchAspect.equals("IN")){
						conditions_2.add(" substr(COALESCE(PCODE,''),4) = '2' ");
					}else if(searchAspect.equals("OUT")){
						conditions_2.add(" substr(COALESCE(PCODE,''),4) = '1' ");
					}
					else if(searchAspect.equals("WO")){
						conditions_1.add(" COALESCE(WOACQUIRE,'') <> ''  ");
						conditions_2.add(" substr(COALESCE(PCODE,''),4) = '3' ");
					}
				}
				
				
				if(StrUtils.isNotEmpty(bgbkId)){
					if(searchAspect.equals("SENDER")){
						conditions_1.add(" SENDERHEAD = '" + bgbkId + "' ");
						conditions_2.add(" SENDERHEAD = '" + bgbkId + "' ");
					}else if(searchAspect.equals("IN")){
						conditions_1.add(" INHEAD = '" + bgbkId + "' ");
						conditions_2.add(" INHEAD = '" + bgbkId + "' ");
					}else if(searchAspect.equals("OUT")){
						conditions_1.add(" OUTHEAD = '" + bgbkId + "' ");
						conditions_2.add(" OUTHEAD = '" + bgbkId + "' ");
					}else if(searchAspect.equals("WO")){
						conditions_1.add(" WOHEAD = '" + bgbkId + "' ");
						conditions_2.add(" WOHEAD = '" + bgbkId + "' ");
					}
				}
				if(StrUtils.isNotEmpty(brbkId) && !brbkId.equals("all")){
					if(searchAspect.equals("SENDER")){
						conditions_1.add(" SENDERBANKID = '" + brbkId + "' ");
						conditions_2.add(" SENDERBANKID = '" + brbkId + "' ");
					}else if(searchAspect.equals("IN")){
						conditions_1.add(" INBANKID = '" + brbkId + "' ");
						conditions_2.add(" INBANKID = '" + brbkId + "' ");
					}else if(searchAspect.equals("OUT")){
						conditions_1.add(" OUTBANKID = '" + brbkId + "' ");
						conditions_2.add(" OUTBANKID = '" + brbkId + "' ");
					}else if(searchAspect.equals("WO")){
						conditions_1.add(" WOBANKID = '" + brbkId + "' ");
						conditions_2.add(" WOBANKID = '" + brbkId + "' ");
					}
				}
			}else{
//				未選擇查詢角度
				if(StrUtils.isNotEmpty(opbkId) && !opbkId.equals("all")){
					if(filter_bat.equals("Y")){
						conditions_2.addAll(conditions_1);
						conditions_2.add(" COALESCE(FLBIZDATE,'') <> ''  ");
						conditions_2.add(" ((INACQUIRE = '" + opbkId + "' AND substr(COALESCE(PCODE,''),4) = '2' ) OR (OUTACQUIRE = '" + opbkId + "' AND substr(COALESCE(PCODE,''),4) = '1' ) OR (WOACQUIRE = '" + opbkId + "' AND substr(COALESCE(PCODE,''),4) = '3')) ");
						conditions_1.add(" COALESCE(FLBIZDATE,'') = ''  ");
						conditions_1.add(" (SENDERACQUIRE = '" + opbkId + "' OR OUTACQUIRE = '" + opbkId + "' OR INACQUIRE = '" + opbkId + "' OR WOACQUIRE = '" + opbkId + "') ");
					}else{
						conditions_1.add(" COALESCE(FLBIZDATE,'') = ''  ");
						conditions_1.add(" (SENDERACQUIRE = '" + opbkId + "' OR OUTACQUIRE = '" + opbkId + "' OR INACQUIRE = '" + opbkId + "' OR WOACQUIRE = '" + opbkId + "') ");
					}
				}else{
					if(filter_bat.equals("Y")){
						conditions_2.addAll(conditions_1);
						conditions_2.add(" COALESCE(FLBIZDATE,'') <> ''  ");
						conditions_1.add(" COALESCE(FLBIZDATE,'') = ''  ");
					}else{
						conditions_1.add(" COALESCE(FLBIZDATE,'') = ''  ");
					}
				}
				if(StrUtils.isNotEmpty(bgbkId)){
					conditions_1.add(" (SENDERHEAD = '" + bgbkId + "' OR INHEAD = '" + bgbkId + "' OR OUTHEAD = '" + bgbkId + "' OR WOHEAD = '" + bgbkId + "') ");
					conditions_2.add(" (SENDERHEAD = '" + bgbkId + "' OR INHEAD = '" + bgbkId + "' OR OUTHEAD = '" + bgbkId + "' OR WOHEAD = '" + bgbkId + "') ");
				}
				if(StrUtils.isNotEmpty(brbkId) && !brbkId.equals("all")){
					conditions_1.add(" (SENDERBANKID = '" + brbkId + "' OR INBANKID = '" + brbkId + "' OR OUTBANKID = '" + brbkId + "' OR WOBANKID = '" + brbkId + "') ");
					conditions_2.add(" (SENDERBANKID = '" + brbkId + "' OR INBANKID = '" + brbkId + "' OR OUTBANKID = '" + brbkId + "' OR WOBANKID = '" + brbkId + "') ");
				}
			}
			condition_1 = combine(conditions_1);
			condition_2 = combine(conditions_2);
			
			StringBuffer sql = new StringBuffer();
			StringBuffer tmpSQL = new StringBuffer();
			StringBuffer sumSQL = new StringBuffer();
			StringBuffer cntSQL = new StringBuffer();
			String orderSQL =  "ORDER BY TXDT ASC , STAN ASC";
			if("TXAMT".equals(params.get("sidx"))){
				orderSQL = " ORDER BY BIGINT(TXAMT) "+" DESC ";
			}
			if(StrUtils.isNotEmpty(params.get("sidx"))){
//				if("TXDT".equalsIgnoreCase(params.get("sidx"))){
//					orderSQL = "ORDER BY NEWTXDT " + params.get("sord");
//				}
//				if("TXAMT".equalsIgnoreCase(params.get("sidx"))){
//					orderSQL = "ORDER BY NEWTXAMT " + params.get("sord");
//				}
//				if("RESP".equalsIgnoreCase(params.get("sidx"))){
//					orderSQL = "ORDER BY RESULTSTATUS " + params.get("sord");
//				}
//				if(!"ACCTCODE".equalsIgnoreCase(params.get("sidx"))){
//					if("SENDERID".equalsIgnoreCase(params.get("sidx"))){
//						sql.append("ORDER BY C." + params.get("sidx"));
//					}else{
//						sql.append("ORDER BY " + params.get("sidx"));
//					}
//					if(params.get("sidx").contains("ASC") || params.get("sidx").contains("DESC") ||
//					   params.get("sidx").contains("asc") || params.get("sidx").contains("desc")){
//					}else{
//						sql.append(" " + params.get("sord"));
//					}
//				}
			}
			tmpSQL.append(" WITH TEMP AS  ");
			tmpSQL.append(" ( ");
			tmpSQL.append("  SELECT TRANSLATE('abcd-ef-gh op:qr:st', A.TXDT, 'abcdefghopqrst') AS TXDT ");
//			tmpSQL.append("  SELECT VARCHAR_FORMAT(NEWTXDT, 'YYYY-MM-DD HH24:MI:SS') AS TXDT ");
			tmpSQL.append("  , NEWTXAMT AS TXAMT, OUTACCTNO , INACCTNO, STAN, TXDATE, NEWRESULT, UPDATEDT, BIZDATE , CLEARINGPHASE  ");
			tmpSQL.append("  , RECEIVERID, SENDERACQUIRE, OUTACQUIRE, INACQUIRE, WOACQUIRE, SENDERHEAD, OUTHEAD, INHEAD, WOHEAD ,FLBIZDATE ,RESULTSTATUS");
			tmpSQL.append("  , PCODE || '-' || COALESCE((SELECT EACH_TXN_NAME FROM EACH_TXN_CODE WHERE EACH_TXN_ID = PCODE),'') AS PCODE ");
			tmpSQL.append("  , SENDERBANKID || '-' || (SELECT COALESCE(BRBK_NAME,'') FROM BANK_BRANCH WHERE BRBK_ID = SENDERBANKID) AS SENDERBANKID ");
			tmpSQL.append("  , OUTBANKID || '-' || (SELECT COALESCE(BRBK_NAME,'') FROM BANK_BRANCH WHERE BRBK_ID = OUTBANKID) AS OUTBANKID ");
			tmpSQL.append("  , INBANKID || '-' || (SELECT COALESCE(BRBK_NAME,'') FROM BANK_BRANCH WHERE BRBK_ID = INBANKID) AS INBANKID ");
			tmpSQL.append("  , WOBANKID || '-' || (SELECT COALESCE(BRBK_NAME,'') FROM BANK_BRANCH WHERE BRBK_ID = WOBANKID) AS WOBANKID ");
			tmpSQL.append("  , (CASE WHEN RESULTSTATUS = 'P' AND NEWRESULT = 'R' THEN '0' ELSE '1' END) AS ACCTCODE ");
			tmpSQL.append("  , TXID || '-' || COALESCE((SELECT t.TXN_NAME FROM TXN_CODE T WHERE T.TXN_ID = TXID),'') AS TXID ");
			tmpSQL.append("  , SENDERID || '-' || GETCOMPANY_ABBR(SENDERID) AS SENDERID");
			tmpSQL.append("  ,(CASE RESULTSTATUS WHEN 'A' THEN '成功' WHEN 'R' THEN '失敗' ELSE (CASE SENDERSTATUS WHEN '1' THEN '處理中' ELSE '未完成' END) END) AS RESP  ");
			tmpSQL.append("  , PFCLASS || '-' || COALESCE((SELECT B.BILL_TYPE_NAME FROM BILL_TYPE B WHERE B.BILL_TYPE_ID = PFCLASS),'') AS PFCLASS, TOLLID, CHARGETYPE, BILLFLAG, COALESCE(RC1,' ') AS RC1 ,COALESCE(RC2,' ') AS RC2 ,COALESCE(RC3,' ') AS RC3 ,COALESCE(RC4,' ') AS RC4 ,COALESCE(RC5,' ') AS RC5 ,COALESCE(RC6,' ')  AS RC6");
			tmpSQL.append("  FROM VW_ONBLOCKTAB A ");
			tmpSQL.append((StrUtils.isEmpty(condition_1))?"" : "WHERE " + condition_1);
			if(filter_bat.equals("Y")){
				tmpSQL.append(" UNION ALL ");
				tmpSQL.append("  SELECT TRANSLATE('abcd-ef-gh op:qr:st', A.TXDT, 'abcdefghopqrst') AS TXDT ");
//				tmpSQL.append("  SELECT VARCHAR_FORMAT(NEWTXDT, 'YYYY-MM-DD HH24:MI:SS') AS TXDT ");
				tmpSQL.append("  ,NEWTXAMT AS TXAMT, OUTACCTNO , INACCTNO, STAN, TXDATE, NEWRESULT, UPDATEDT, BIZDATE , CLEARINGPHASE  ");
				tmpSQL.append("  , RECEIVERID, SENDERACQUIRE, OUTACQUIRE, INACQUIRE, WOACQUIRE, SENDERHEAD, OUTHEAD, INHEAD, WOHEAD ,FLBIZDATE ,RESULTSTATUS");
				tmpSQL.append("  , PCODE || '-' || COALESCE((SELECT EACH_TXN_NAME FROM EACH_TXN_CODE WHERE EACH_TXN_ID = PCODE),'') AS PCODE ");
				tmpSQL.append("  , SENDERBANKID || '-' || (SELECT COALESCE(BRBK_NAME,'') FROM BANK_BRANCH WHERE BRBK_ID = SENDERBANKID) AS SENDERBANKID ");
				tmpSQL.append("  , OUTBANKID || '-' || (SELECT COALESCE(BRBK_NAME,'') FROM BANK_BRANCH WHERE BRBK_ID = OUTBANKID) AS OUTBANKID ");
				tmpSQL.append("  , INBANKID || '-' || (SELECT COALESCE(BRBK_NAME,'') FROM BANK_BRANCH WHERE BRBK_ID = INBANKID) AS INBANKID ");
				tmpSQL.append("  , WOBANKID || '-' || (SELECT COALESCE(BRBK_NAME,'') FROM BANK_BRANCH WHERE BRBK_ID = WOBANKID) AS WOBANKID ");
				tmpSQL.append("  , (CASE WHEN RESULTSTATUS = 'P' AND NEWRESULT = 'R' THEN '0' ELSE '1' END) AS ACCTCODE ");
				tmpSQL.append("  , TXID || '-' || COALESCE((SELECT t.TXN_NAME FROM TXN_CODE T WHERE T.TXN_ID = TXID),'') AS TXID ");
				tmpSQL.append("  , SENDERID || '-' || GETCOMPANY_ABBR(SENDERID) AS SENDERID");
				tmpSQL.append("  ,(CASE RESULTSTATUS WHEN 'A' THEN '成功' WHEN 'R' THEN '失敗' ELSE (CASE SENDERSTATUS WHEN '1' THEN '處理中' ELSE '未完成' END) END) AS RESP  ");
				tmpSQL.append("  , PFCLASS || '-' || COALESCE((SELECT B.BILL_TYPE_NAME FROM BILL_TYPE B WHERE B.BILL_TYPE_ID = PFCLASS),'') AS PFCLASS, TOLLID, CHARGETYPE, BILLFLAG , COALESCE(RC1,' ') AS RC1 ,COALESCE(RC2,' ') AS RC2 ,COALESCE(RC3,' ') AS RC3 ,COALESCE(RC4,' ') AS RC4 ,COALESCE(RC5,' ') AS RC5 ,COALESCE(RC6,' ')  AS RC6  ");
				tmpSQL.append("  FROM VW_ONBLOCKTAB A ");
				tmpSQL.append((StrUtils.isEmpty(condition_2))?"" : "WHERE " + condition_2);
			}
			tmpSQL.append(" ) ");
			sql.append(" SELECT * FROM ( ");
			sql.append("  	SELECT  ROWNUMBER() OVER( "+orderSQL+") AS ROWNUMBER , TEMP.*  ");
			sql.append(" FROM TEMP ");
			sql.append(" ) AS A    ");
			sql.insert(0, tmpSQL.toString());
			sql.append(orderSQL);
			//先算出總計的欄位值，放到rtnMap裡面，到頁面再放到下面的總計Grid裡面
			cntSQL.append(" SELECT COALESCE(COUNT(*),0) AS NUM FROM TEMP ");
			cntSQL.insert(0, tmpSQL.toString());
//			20150504 edit by hugo req by 李建利 ，交易金額 無論成功或失敗都要計算 不可為0
//			dataSumSQL.append("		CASE RESULTSTATUS WHEN 'R' THEN 0 ELSE NEWTXAMT END ");
			sumSQL.append(" SELECT COALESCE( SUM(EACHUSER.ISNUMERIC(TXAMT)) ,0) AS TXAMT FROM TEMP ");
			sumSQL.insert(0, tmpSQL.toString());
			System.out.println("sumSQL="+sumSQL);
			String dataSumCols[] = {"TXAMT"};
			list = vw_onblocktab_Dao.dataSum(sumSQL.toString(),dataSumCols,VW_ONBLOCKTAB.class);
			for(VW_ONBLOCKTAB po:list){
				System.out.println(String.format("SUM(X.TXAMT)=%s",
					po.getTXAMT()));
			}
			rtnMap.put("dataSumList", list);
			
			System.out.println("cntSQL===>"+cntSQL.toString());
			System.out.println("sql===>"+sql.toString());
			String cols[] = {"TXDT", "PCODE", "SENDERBANKID", "OUTBANKID", "INBANKID", "WOBANKID", "OUTACCTNO", "INACCTNO","TXDATE","STAN", "TXAMT", "RESP", "SENDERID", "TXID" ,"PFCLASS", "TOLLID", "CHARGETYPE", "BILLFLAG","RC1", "RC2", "RC3","RC4","RC5","RC6"};
			
			list = vw_onblocktab_Dao.getCSVData(sql.toString(),cols);
			List<Map> listdata = new ArrayList<Map>();
			for( VW_ONBLOCKTAB eachdata:list) {
				listdata.add(CodeUtils.objectCovert(Map.class, eachdata));
			}
			int pathType = Arguments.getStringArg("ISWINOPEN").equals("Y")?RptUtils.C_PATH :RptUtils.R_PATH;
			String outputFilePath = "";
			outputFilePath = RptUtils.export(RptUtils.COLLECTION, pathType, "csv_txdata", "csv_txdata", paramsV, listdata ,4);
			if(StrUtils.isNotEmpty(outputFilePath)){
				rtnMap.put("result", "TRUE");
				rtnMap.put("msg", outputFilePath);
			}else{
				rtnMap.put("result", "FALSE");
				rtnMap.put("msg", "產生失敗!");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rtnMap.put("result", "FALSE");
			rtnMap.put("msg", "產生錯誤!");
		}
		String json = JSONUtils.map2json(rtnMap) ;
		//System.out.println("json===>"+json+"<===");
		return rtnMap;
	}
	
	//取得五分鐘內交易數量
	public int getFiveMinData(String qryPeriod) throws Exception{
		SimpleDateFormat currenttime = new SimpleDateFormat("yyyyMMddHHmmss");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT COUNT(*) AS NUM FROM EACHUSER.ONBLOCKTAB ");
//		System.out.println( currenttime.toString());
		sql.append(" WHERE (CAST(TXDT as BIGINT)-CAST('"+ currenttime.format(Calendar.getInstance().getTime()) +"' as BIGINT)) > "+qryPeriod+"00 WITH UR");
		
		int totalCount=0;
//		throw new Exception();
		try {
			totalCount = onblocktab_Dao.countDataIII(sql.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e);
		}
		return totalCount;
	}
	
	public EACH_USER_Dao getEach_user_Dao() {
		return each_user_Dao;
	}

	public void setEach_user_Dao(EACH_USER_Dao each_user_Dao) {
		this.each_user_Dao = each_user_Dao;
	}

	public EACH_FUNC_LIST_Dao getFunc_list_Dao() {
		return func_list_Dao;
	}

	public void setFunc_list_Dao(EACH_FUNC_LIST_Dao func_list_Dao) {
		this.func_list_Dao = func_list_Dao;
	}

	public EACH_USERLOG_Dao getUserLog_Dao() {
		return userLog_Dao;
	}

	public void setUserLog_Dao(EACH_USERLOG_Dao userLog_Dao) {
		this.userLog_Dao = userLog_Dao;
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


	public EACH_USERLOG_BO getUserlog_bo() {
		return userlog_bo;
	}

	public void setUserlog_bo(EACH_USERLOG_BO userlog_bo) {
		this.userlog_bo = userlog_bo;
	}

	public RPONBLOCKTAB_Dao getRponblocktab_Dao() {
		return rponblocktab_Dao;
	}

	public void setRponblocktab_Dao(RPONBLOCKTAB_Dao rponblocktab_Dao) {
		this.rponblocktab_Dao = rponblocktab_Dao;
	}
}
