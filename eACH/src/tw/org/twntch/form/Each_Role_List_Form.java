package tw.org.twntch.form;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

public class Each_Role_List_Form extends CommonForm {
	private	List	scaseary	;
	private	List	menuFuncs	;
	private	List	subItemFuncs	;
	private	List	role_func_List	;
	private	List	subItemFuncsII	;
	private	String[]	selected_MFuncs	= new String[0];
	private	String[]	selected_SFuncs	=new String[0];
	private	String[]	selected_SFuncs_name = new String[0];
	private String OLD_ROLE_TYPE="";
	private	String	selected_SAuth	;
	private String  old_selected_SAuth	;
	private	String	old_selected_MFuncs;
	private	String	old_selected_SFuncs;
	private String ROLE_ID ;
	private String ROLE_NAME;
	private String DESC;
	private String USE_IKEY="N";
	private String ROLE_TYPE;
	private String CDATE;
	private String UDATE;
	public List getScaseary() {
		return scaseary;
	}
	public void setScaseary(List scaseary) {
		this.scaseary = scaseary;
	}
	public List getMenuFuncs() {
		return menuFuncs;
	}
	public void setMenuFuncs(List menuFuncs) {
		this.menuFuncs = menuFuncs;
	}
	public List getSubItemFuncs() {
		return subItemFuncs;
	}
	public void setSubItemFuncs(List subItemFuncs) {
		this.subItemFuncs = subItemFuncs;
	}
	public List getRole_func_List() {
		return role_func_List;
	}
	public void setRole_func_List(List role_func_List) {
		this.role_func_List = role_func_List;
	}
	public List getSubItemFuncsII() {
		return subItemFuncsII;
	}
	public void setSubItemFuncsII(List subItemFuncsII) {
		this.subItemFuncsII = subItemFuncsII;
	}
	public String[] getSelected_MFuncs() {
		return selected_MFuncs;
	}
	public void setSelected_MFuncs(String[] selected_MFuncs) {
		this.selected_MFuncs = selected_MFuncs;
	}
	public String[] getSelected_SFuncs() {
		return selected_SFuncs;
	}
	public void setSelected_SFuncs(String[] selected_SFuncs) {
		this.selected_SFuncs = selected_SFuncs;
	}
	public String[] getSelected_SFuncs_name() {
		return selected_SFuncs_name;
	}
	public void setSelected_SFuncs_name(String[] selected_SFuncs_name) {
		this.selected_SFuncs_name = selected_SFuncs_name;
	}
	public String getOLD_ROLE_TYPE() {
		return OLD_ROLE_TYPE;
	}
	public void setOLD_ROLE_TYPE(String oLD_ROLE_TYPE) {
		OLD_ROLE_TYPE = oLD_ROLE_TYPE;
	}
	public String getSelected_SAuth() {
		return selected_SAuth;
	}
	public void setSelected_SAuth(String selected_SAuth) {
		this.selected_SAuth = selected_SAuth;
	}
	public String getOld_selected_SAuth() {
		return old_selected_SAuth;
	}
	public void setOld_selected_SAuth(String old_selected_SAuth) {
		this.old_selected_SAuth = old_selected_SAuth;
	}
	public String getOld_selected_MFuncs() {
		return old_selected_MFuncs;
	}
	public void setOld_selected_MFuncs(String old_selected_MFuncs) {
		this.old_selected_MFuncs = old_selected_MFuncs;
	}
	public String getOld_selected_SFuncs() {
		return old_selected_SFuncs;
	}
	public void setOld_selected_SFuncs(String old_selected_SFuncs) {
		this.old_selected_SFuncs = old_selected_SFuncs;
	}
	public String getROLE_ID() {
		return ROLE_ID;
	}
	public void setROLE_ID(String rOLE_ID) {
		ROLE_ID = rOLE_ID;
	}
	public String getROLE_NAME() {
		return ROLE_NAME;
	}
	public void setROLE_NAME(String rOLE_NAME) {
		ROLE_NAME = rOLE_NAME;
	}
	public String getDESC() {
		return DESC;
	}
	public void setDESC(String dESC) {
		DESC = dESC;
	}
	public String getUSE_IKEY() {
		return USE_IKEY;
	}
	public void setUSE_IKEY(String uSE_IKEY) {
		USE_IKEY = uSE_IKEY;
	}
	public String getROLE_TYPE() {
		return ROLE_TYPE;
	}
	public void setROLE_TYPE(String rOLE_TYPE) {
		ROLE_TYPE = rOLE_TYPE;
	}
	public String getCDATE() {
		return CDATE;
	}
	public void setCDATE(String cDATE) {
		CDATE = cDATE;
	}
	public String getUDATE() {
		return UDATE;
	}
	public void setUDATE(String uDATE) {
		UDATE = uDATE;
	}
	@Override
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		// TODO Auto-generated method stub
		super.reset(mapping, request);
		ROLE_ID="";
	}
	
	
	
	
	
	
}
