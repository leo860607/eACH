package tw.org.twntch.po;

import java.io.Serializable;
import java.sql.Blob;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity (name = "tw.org.twntch.po.EACH_FILEUPLOAD")
@Table(name = "EACH_FILEUPLOAD")
public class EACH_FILEUPLOAD implements Serializable{
	/**
	 * 檔案上傳PO
	 */
	private static final long serialVersionUID = -161380309878430277L;
	//ID_NO和FILE_NO複合主鍵
	private String ID_NO;
	private Integer FILE_NO;
	//檔名
	private String FILENAME;
	//檔案
	private Blob FILEDATA;
	//副檔名
	private String FILETYPE;
	//檔案描述
	private String DOCNAME;
	//檔案大小
	private Integer FILESIZE;
	//票交上傳是1，銀行上傳是0
	private Integer DOWNLOADTYPE;
	//檔案路徑
	private String FILEPATH;
	//下載次數
	private Integer DOWNLOADNUM;
	//最後下載者
	private String LASTDOWNLOADER;
	//最後下載日期
	private Date LASTDOWNLOADTIME;
	//初次上傳者
	private String UPLOADER;
	//初次上傳日期
	private Date CDATE;
	//parsing到頁面上的grid所顯示的String
	private String CDATEString;
	//最後更新者
	private String UPDER;
	//最後更新日期
	private Date UPDDATE;
	//parsing到頁面上的grid所顯示的String
	private String UPDDATEString;
	//上傳者所屬單位
	private String USER_COMPANY;
	//可下載單位
	private String TOBANKS;
	
	@Id
	public String getID_NO() {
		return ID_NO;
	}
	public void setID_NO(String iD_NO) {
		ID_NO = iD_NO;
	}
	@Id
	public Integer getFILE_NO() {
		return FILE_NO;
	}
	public void setFILE_NO(Integer fILE_NO) {
		FILE_NO = fILE_NO;
	}
	public String getFILENAME() {
		return FILENAME;
	}
	public void setFILENAME(String fILENAME) {
		FILENAME = fILENAME;
	}
	public Blob getFILEDATA() {
		return FILEDATA;
	}
	public void setFILEDATA(Blob fILEDATA) {
		FILEDATA = fILEDATA;
	}
	public String getFILETYPE() {
		return FILETYPE;
	}
	public void setFILETYPE(String fILETYPE) {
		FILETYPE = fILETYPE;
	}
	public String getDOCNAME() {
		return DOCNAME;
	}
	public void setDOCNAME(String dOCNAME) {
		DOCNAME = dOCNAME;
	}
	public Integer getFILESIZE() {
		return FILESIZE;
	}
	public void setFILESIZE(Integer fILESIZE) {
		FILESIZE = fILESIZE;
	}
	public Integer getDOWNLOADTYPE() {
		return DOWNLOADTYPE;
	}
	public void setDOWNLOADTYPE(Integer dOWNLOADTYPE) {
		DOWNLOADTYPE = dOWNLOADTYPE;
	}
	public String getFILEPATH() {
		return FILEPATH;
	}
	public void setFILEPATH(String fILEPATH) {
		FILEPATH = fILEPATH;
	}
	public Integer getDOWNLOADNUM() {
		return DOWNLOADNUM;
	}
	public void setDOWNLOADNUM(Integer dOWNLOADNUM) {
		DOWNLOADNUM = dOWNLOADNUM;
	}
	public String getLASTDOWNLOADER() {
		return LASTDOWNLOADER;
	}
	public void setLASTDOWNLOADER(String lASTDOWNLOADER) {
		LASTDOWNLOADER = lASTDOWNLOADER;
	}
	public Date getLASTDOWNLOADTIME() {
		return LASTDOWNLOADTIME;
	}
	public void setLASTDOWNLOADTIME(Date lASTDOWNLOADTIME) {
		LASTDOWNLOADTIME = lASTDOWNLOADTIME;
	}
	public String getUPLOADER() {
		return UPLOADER;
	}
	public void setUPLOADER(String uPLOADER) {
		UPLOADER = uPLOADER;
	}
	public Date getCDATE() {
		return CDATE;
	}
	public void setCDATE(Date cDATE) {
		CDATE = cDATE;
	}
	public String getCDATEString() {
		return CDATEString;
	}
	public void setCDATEString(String cDATEString) {
		CDATEString = cDATEString;
	}
	public String getUPDER() {
		return UPDER;
	}
	public void setUPDER(String uPDER) {
		UPDER = uPDER;
	}
	public Date getUPDDATE() {
		return UPDDATE;
	}
	public void setUPDDATE(Date uPDDATE) {
		UPDDATE = uPDDATE;
	}
	public String getUPDDATEString() {
		return UPDDATEString;
	}
	public void setUPDDATEString(String uPDDATEString) {
		UPDDATEString = uPDDATEString;
	}
	public String getUSER_COMPANY() {
		return USER_COMPANY;
	}
	public void setUSER_COMPANY(String uSER_COMPANY) {
		USER_COMPANY = uSER_COMPANY;
	}
	public String getTOBANKS() {
		return TOBANKS;
	}
	public void setTOBANKS(String tOBANKS) {
		TOBANKS = tOBANKS;
	}
}
