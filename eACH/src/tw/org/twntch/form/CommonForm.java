/**
 * 20141120 HUANGPU 共用屬性
 */
package tw.org.twntch.form;


import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import tw.org.twntch.bo.Arguments;

public class CommonForm extends ActionForm{
	private	String	ac_key;
	private	String	target;
	private	String	result;
	private	String	msg;
	private	String	pagesize = Arguments.getStringArg("PAGE.SIZE");
	private String serchStrs = "{}";
	private String colForSort = "";
	private String ordForSort = "";
	private String s_auth_type = "R"; //該使用者對應功能的權限 預設為R(查詢)
	private String s_up_func_name;
	private String s_func_name;
	private String s_fcid;
	private String s_is_record = "N";
	private String p_is_record = "N";
	private String file_path;
	private String error_list;
	private String edit_params = "{}";
	private String sortname = "";
	private String sortorder = "";
	private String pageForSort = "1"; 
	public String getAc_key() {
		return ac_key;
	}

	public void setAc_key(String ac_key) {
		this.ac_key = ac_key;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getPagesize() {
		return pagesize;
	}

	public void setPagesize(String pagesize) {
		this.pagesize = pagesize;
	}

	public String getSerchStrs() {
		return serchStrs;
	}

	public void setSerchStrs(String serchStrs) {
		this.serchStrs = serchStrs;
	}

	public String getColForSort() {
		return colForSort;
	}

	public void setColForSort(String colForSort) {
		this.colForSort = colForSort;
	}

	public String getOrdForSort() {
		return ordForSort;
	}

	public void setOrdForSort(String ordForSort) {
		this.ordForSort = ordForSort;
	}

	
	
	public String getS_auth_type() {
		return s_auth_type;
	}

	public void setS_auth_type(String s_auth_type) {
		this.s_auth_type = s_auth_type;
	}
	
	public String getS_up_func_name() {
		return s_up_func_name;
	}

	public void setS_up_func_name(String s_up_func_name) {
		this.s_up_func_name = s_up_func_name;
	}

	public String getS_func_name() {
		return s_func_name;
	}

	public void setS_func_name(String s_func_name) {
		this.s_func_name = s_func_name;
	}

	public String getFile_path() {
		return file_path;
	}

	public void setFile_path(String file_path) {
		this.file_path = file_path;
	}
	
	public String getError_list() {
		return error_list;
	}

	public void setError_list(String error_list) {
		this.error_list = error_list;
	}
	
	
	public String getS_fcid() {
		return s_fcid;
	}

	public void setS_fcid(String s_fcid) {
		this.s_fcid = s_fcid;
	}

	
	
	
	public String getEdit_params() {
		return edit_params;
	}

	public void setEdit_params(String edit_params) {
		this.edit_params = edit_params;
	}

	
	
	public String getSortname() {
		return sortname;
	}

	public void setSortname(String sortname) {
		this.sortname = sortname;
	}

	
	
	
	public String getSortorder() {
		return sortorder;
	}

	public void setSortorder(String sortorder) {
		this.sortorder = sortorder;
	}

	
	
	
	public String getS_is_record() {
		return s_is_record;
	}

	public void setS_is_record(String s_is_record) {
		this.s_is_record = s_is_record;
	}
	
	

	public String getP_is_record() {
		return p_is_record;
	}

	public void setP_is_record(String p_is_record) {
		this.p_is_record = p_is_record;
	}
	
	

	public String getPageForSort() {
		return pageForSort;
	}

	public void setPageForSort(String pageForSort) {
		this.pageForSort = pageForSort;
	}

	@Override
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		// TODO Auto-generated method stub
		super.reset(mapping, request);
		ac_key="";
		result="";
		msg="";
	}
}
