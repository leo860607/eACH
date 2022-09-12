package tw.org.twntch.bo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;



import org.apache.log4j.Logger;
import org.apache.struts.util.LabelValueBean;

import com.google.gson.Gson;

import tw.org.twntch.db.dao.hibernate.EACH_FILEUPLOAD_Dao;
import tw.org.twntch.db.dao.hibernate.Page;
import tw.org.twntch.po.BANK_GROUP;
import tw.org.twntch.po.EACH_FILEUPLOAD;
import tw.org.twntch.util.StrUtils;

public class EACH_FILEUPLOAD_BO{
	private EACH_FILEUPLOAD_Dao each_fileupload_Dao;
	private AGENT_PROFILE_BO agent_profile_bo ;
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	//取得代理業者清單
		public List<LabelValueBean> getAgentList(){
			return agent_profile_bo.getCompany_Id_ABBR_List();
		}
	
	//取得銀行清單
	public List<LabelValueBean> getBanksList(){
		List<BANK_GROUP> list = each_fileupload_Dao.getBanksList();
		List<LabelValueBean> beanList = new LinkedList<LabelValueBean>();
		LabelValueBean bean = null;
		for(BANK_GROUP po : list){
			bean = new LabelValueBean(po.getBGBK_ID() + " - " + po.getBGBK_NAME(), po.getBGBK_ID());
			logger.debug("po.getBGBK_ID()="+po.getBGBK_ID()+",po.getBGBK_NAME()="+po.getBGBK_NAME());
			beanList.add(bean);
		}
		return beanList;
	}
//	20160130依據20160122SRS 修改 濾掉登入銀行
	//取得銀行清單
	public List<LabelValueBean> getBanksListWithoutThisBank(String bank_id){
//		List<BANK_GROUP> list = each_fileupload_Dao.getBanksList();
		List<BANK_GROUP> list = each_fileupload_Dao.getBanksList(bank_id);
		List<LabelValueBean> beanList = new LinkedList<LabelValueBean>();
		LabelValueBean bean = null;
		for(BANK_GROUP po : list){
			bean = new LabelValueBean(po.getBGBK_ID() + " - " + po.getBGBK_NAME(), po.getBGBK_ID());
			logger.debug("po.getBGBK_ID()="+po.getBGBK_ID()+",po.getBGBK_NAME()="+po.getBGBK_NAME());
			beanList.add(bean);
		}
		return beanList;
	}
	
	//新增檔案
	public void add(String uuid, String[] filename, String[] filedesc, int[] filesize, byte[][] filedata, String userID,String userCompany, String userType,String toBanks) throws Exception {
		String sql = "INSERT INTO EACH_FILEUPLOAD(ID_NO,FILE_NO,FILENAME,FILEDATA,FILETYPE,DOCNAME,FILESIZE,DOWNLOADTYPE,DOWNLOADNUM,UPLOADER,CDATE,USER_COMPANY,TOBANKS) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
		Object[] paramObject = new Object[13];
		int count = 0;
		int DOWNLOADTYPE = 0;
		//票交
		if ("A".equals(userType)) {
			DOWNLOADTYPE = 1;
		}
		//銀行
		if ("B".equals(userType)) {
			DOWNLOADTYPE = 0;
		}
		for (int x = 0; x < 3; x++) {
			Date CDate = new Date();
			//欄位有值才新增
			if (!"".equals(filename[x])) {
				count += 1;
				paramObject[0] = uuid;
				paramObject[1] = count;
				paramObject[2] = filename[x];
				paramObject[3] = filedata[x];
				//如果沒有'.'
				if (filename[x].lastIndexOf(".") == -1) {
					paramObject[4] = "undefined";
				}
				//副檔名
				else {
					paramObject[4] = filename[x].substring(filename[x].lastIndexOf(".") + 1);
				}
				paramObject[5] = filedesc[x];
				//如果檔案不滿1KB，以1KB計算
				paramObject[6] = filesize[x]/1024 == 0 ? 1 : filesize[x]/1024;
				paramObject[7] = DOWNLOADTYPE;
				paramObject[8] = 0;
				paramObject[9] = userID;
				paramObject[10] = CDate;
				paramObject[11] = userCompany;
				paramObject[12] = toBanks;
				
				each_fileupload_Dao.saveOrUpdate(sql,paramObject);
			}
		}
	}
	//JQGRID分頁查詢
	public String pageSearch(Map<String, String> param) throws Exception{
		//目前頁數
		String pageNo = StrUtils.isEmpty(param.get("page")) ?"0":param.get("page");
		//每頁筆數
		String pageSize = StrUtils.isEmpty(param.get("rows")) ?Arguments.getStringArg("PAGE.SIZE"):param.get("rows");
		logger.debug("pageNo="+pageNo);
		logger.debug("pageSize="+pageSize);
		Map rtnMap = new HashMap();
		
		List<EACH_FILEUPLOAD> list = null;
		Page page = null;
		try {
			String countQuery = "";
			String sql = "";
			//只能看到自己單位上傳的檔案
			countQuery = "SELECT COUNT(*) FROM EACH_FILEUPLOAD WHERE USER_COMPANY = '"+param.get("USER_COMPANY")+"'";
			sql = "SELECT ID_NO, FILE_NO, FILENAME,FILEDATA,FILETYPE,DOCNAME,FILESIZE,DOWNLOADTYPE,FILEPATH,DOWNLOADNUM,LASTDOWNLOADER,LASTDOWNLOADTIME,UPLOADER,CDATE,USER_COMPANY,TOBANKS FROM EACH_FILEUPLOAD WHERE USER_COMPANY = '"+param.get("USER_COMPANY")+"' ORDER BY CDATE DESC,FILE_NO ASC";

			logger.debug("countQuery="+countQuery);
			logger.debug("sql="+sql);
			
			String cols[] = {"ID_NO", "FILE_NO", "FILENAME", "FILEDATA", "FILETYPE", "DOCNAME", "FILESIZE", "DOWNLOADTYPE", "FILEPATH", "DOWNLOADNUM", "LASTDOWNLOADER", "LASTDOWNLOADTIME", "UPLOADER", "CDATE", "USER_COMPANY", "TOBANKS"};
			page = each_fileupload_Dao.getData(Integer.valueOf(pageNo), Integer.valueOf(pageSize), countQuery, sql, cols, EACH_FILEUPLOAD.class);
			list = (List<EACH_FILEUPLOAD>) page.getResult();
			logger.debug("list="+list);
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
			//因為用Gson轉json，所以要格式日期
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if(list != null && list.size() > 0){
				for(EACH_FILEUPLOAD po : list){
					po.setCDATEString(sdf.format(po.getCDATE()));
					if(po.getUPDDATE() != null && !StrUtils.isEmpty(po.getUPDDATE().toString())){
						po.setUPDDATEString(sdf.format(po.getUPDDATE()));
					}
				}
			}
			
			rtnMap.put("rows",list);
		}
		
		String json = new Gson().toJson(rtnMap);
		return json;
	}
	//刪除檔案
	public void delete(String idNo,int fileNo){
		Object[] paramObject = new Object[2];
		String sql = "DELETE FROM EACH_FILEUPLOAD WHERE ID_NO=? AND FILE_NO=?";
		paramObject[0] = idNo;
		paramObject[1] = fileNo;
			
		each_fileupload_Dao.saveOrUpdate(sql,paramObject);
	}
	public EACH_FILEUPLOAD_Dao getEach_fileupload_Dao() {
		return each_fileupload_Dao;
	}

	public void setEach_fileupload_Dao(EACH_FILEUPLOAD_Dao each_fileupload_Dao) {
		this.each_fileupload_Dao = each_fileupload_Dao;
	}

	public AGENT_PROFILE_BO getAgent_profile_bo() {
		return agent_profile_bo;
	}

	public void setAgent_profile_bo(AGENT_PROFILE_BO agent_profile_bo) {
		this.agent_profile_bo = agent_profile_bo;
	}
	
	
	
	
}
