package tw.org.twntch.form;

import java.util.List;
import java.util.Map;

public class Each_Formdownload_Form extends CommonForm {
	private static final long serialVersionUID = 8125332046977001467L;

	private List<Map<String,Object>> dataListMap;
	private String filename;
	private String downloadToken;//檔案下載判別用
	
	public List<Map<String, Object>> getDataListMap() {
		return dataListMap;
	}
	public void setDataListMap(List<Map<String, Object>> dataListMap) {
		this.dataListMap = dataListMap;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getDownloadToken() {
		return downloadToken;
	}
	public void setDownloadToken(String downloadToken) {
		this.downloadToken = downloadToken;
	}
}
