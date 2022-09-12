package tw.org.twntch.bo;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.util.LabelValueBean;

import tw.org.twntch.db.dao.hibernate.GL_ERROR_CODE_Dao;
import tw.org.twntch.po.GL_ERROR_CODE;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.zDateHandler;

public class GL_ERROR_CODE_BO {

	private GL_ERROR_CODE_Dao gl_err_code_Dao ;

	
	public Map<String,String> update(String id  ,Map formMap){
		Map<String, String> map = null;
		try {
			map = new HashMap<String, String>();
			GL_ERROR_CODE po = gl_err_code_Dao.get(id);
			if(po == null ){
				map.put("result", "FALSE");
				map.put("msg", "更新失敗，查無資料");
				map.put("target", "edit_p");
				return map;
			}
			po = new GL_ERROR_CODE();
			BeanUtils.populate(po, formMap);
			po.setUDATE(zDateHandler.getTheDateII());
			gl_err_code_Dao.save(po);
			map.put("result", "TRUE");
			map.put("msg", "更新成功");
			map.put("target", "search");
		}catch (Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();
			map.put("result", "ERROR");
			map.put("msg", "更新失敗，系統異常");
			map.put("target", "edit_p");
			return map;
		}
		return map;
	}

public Map<String,String> delete(String id ){
	Map<String, String> map = null;
	try {
		map = new HashMap<String, String>();
		GL_ERROR_CODE po = gl_err_code_Dao.get(id);
		if(po == null ){
			map.put("result", "FALSE");
			map.put("msg", "刪除失敗，查無資料");
			map.put("target", "edit_p");
			return map;
		}
		gl_err_code_Dao.remove(po);
		map.put("result", "TRUE");
		map.put("msg", "刪除成功");
		map.put("target", "search");
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		map.put("result", "ERROR");
		map.put("msg", "刪除失敗，系統異常");
		map.put("target", "edit_p");
		return map;
	}
	return map;
}
	
	/**
	 * 新增交易代碼
	 * @param bgid
	 * @param brid
	 * @param formMap
	 * @return
	 */
	public Map<String,String> save(String id ,Map formMap){
		Map<String, String> map = null;
		try {
			map = new HashMap<String, String>();
			GL_ERROR_CODE po = gl_err_code_Dao.get(id);
			if(po != null ){
				map.put("result", "FALSE");
				map.put("msg", "儲存失敗，一般錯誤代號重複");
				map.put("target", "add_p");
				return map;
			}
			po = new GL_ERROR_CODE();
			BeanUtils.populate(po, formMap);
			po.setCDATE(zDateHandler.getTheDateII());
			gl_err_code_Dao.save(po);
			map.put("result", "TRUE");
			map.put("msg", "儲存成功");
			map.put("target", "search");
		}catch (Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();
			map.put("result", "ERROR");
			map.put("msg", "儲存失敗，系統異常");
			map.put("target", "add_p");
			return map;
		}
		return map;
	}
	
	public List<GL_ERROR_CODE> search(String id){
		
		List<GL_ERROR_CODE> list = null ;
		try {
			if(StrUtils.isEmpty(id)){
				list = gl_err_code_Dao.getAll();
			}else{
				list= gl_err_code_Dao.getByPK(id.trim());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("list>>"+list);
		list = list == null? null : list.size() == 0 ? null : list;
		return list;
	}
	
	
	public List<LabelValueBean> getIdList(){
		List<GL_ERROR_CODE> list = gl_err_code_Dao.getAll_OrderByTxnId();
		List<LabelValueBean> beanList = new LinkedList<LabelValueBean>();
		LabelValueBean bean = null;
		for(GL_ERROR_CODE po : list){
			bean = new LabelValueBean(po.getERROR_ID() + " - " + po.getERR_DESC(), po.getERROR_ID());
			beanList.add(bean);
		}
		System.out.println("beanList>>"+beanList);
		return beanList;
	}
	
	public String search_toJson(Map<String, String> param){
		String error_id =StrUtils.isNotEmpty(param.get("ERROR_ID"))? param.get("ERROR_ID"):"";
		error_id = error_id.equals("all")?"":error_id;
		return JSONUtils.toJson(search(error_id));
	}
	
	public GL_ERROR_CODE_Dao getGl_err_code_Dao() {
		return gl_err_code_Dao;
	}

	public void setGl_err_code_Dao(GL_ERROR_CODE_Dao gl_err_code_Dao) {
		this.gl_err_code_Dao = gl_err_code_Dao;
	}

	
	
	
	
	
}
