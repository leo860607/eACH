package tw.org.twntch.bo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;

import tw.org.twntch.db.dao.hibernate.ISEC_VERSION_DAO;
import tw.org.twntch.po.SYS_PARA;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.zDateHandler;

public class ISEC_VERSION_BO {
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	private ISEC_VERSION_DAO isec_version_dao;
	
	
	public Map<String, String> update(String id ,Map formMap){
		logger.debug("Start ISEC_BO");
//		System.out.println("ID="+id+"MAP="+formMap);
		Map<String,String> map =new HashMap<String, String>();
		SYS_PARA po = null;
		try {
			map = new HashMap<String, String>();
			if(StrUtils.isNotEmpty(id)){
				po = isec_version_dao.get(Integer.valueOf(id));
				System.out.println("PO = "+po);
			}else {
				po = isec_version_dao.getISEC();
				System.out.println("PO1 = "+po);
			}
			
			if(po == null ){
				po = new SYS_PARA();
				formMap.remove("SEQ_ID");
			}
			BeanUtils.populate(po, formMap);
			po.setUDATE(zDateHandler.getTheDateII());
			isec_version_dao.aop_save(po);
			
			map.put("result", "TRUE");
			map.put("msg", "儲存成功");
			map.put("target", "edit_p");
		}catch (Exception e){
			e.printStackTrace();
			map.put("result", "ERROR");
			map.put("msg", "更新失敗，系統異常:"+e);
			map.put("target", "edit_p");
			return map;
		}		
		return map;
	}

	public List<SYS_PARA> search(String id){
		System.out.println("Start ISEC_SEARCH");
		List<SYS_PARA> list = null ;
		try {
			if(StrUtils.isNotEmpty(id)){
				list = isec_version_dao.find(" FROM tw.org.twntch.po.SYS_PARA WHERE SEQ_ID = ?",Integer.valueOf(id));
			}else{
				list = isec_version_dao.getAll();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("list>>"+list);
		list = list.size() == 0 ? null : list;
		return list;
	}	
	
//	2018/07/30修改過
	public String search_toJson(Map<String, String> param){
		logger.debug("search_toJson");
//		String seq_id =StrUtils.isNotEmpty(param.get("SEQ_ID"))? param.get("SEQ_ID"):"";
		List<SYS_PARA> seq_id = isec_version_dao.getversion();
		logger.debug("seq_id"+JSONUtils.toJson(seq_id));
		return JSONUtils.toJson(seq_id);
	}
	
//	public String search_toJson_test(Map<String, String> param){					
//		String seq_id =StrUtils.isNotEmpty(param.get("SEQ_ID"))? param.get("SEQ_ID"):"";
//		SYS_PARA sys_para = new SYS_PARA();
//		List<SYS_PARA> seq_id = new ArrayList<SYS_PARA>();
//		sys_para.setISEC_VERSION("1.1805.8.2");		
//		seq_id.add(sys_para);
//		return JSONUtils.toJson(seq_id);
//	}
	

	public ISEC_VERSION_DAO getIsec_version_dao() {
		return isec_version_dao;
	}

	public void setIsec_version_dao(ISEC_VERSION_DAO isec_version_dao) {
		this.isec_version_dao = isec_version_dao;
	}
	
	
	
}

