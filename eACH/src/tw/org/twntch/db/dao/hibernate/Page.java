package tw.org.twntch.db.dao.hibernate;


import java.io.Serializable;
import java.util.ArrayList;

/**
 * 分頁對像.
 * 包含當前頁數據及分頁信息.
 *
 * @author ajax
 * @author calvin
 */
@SuppressWarnings("serial")
public class Page implements Serializable {

	static private int DEFAULT_PAGE_SIZE = 20;

	/**
	 * 每頁的記錄數
	 */
	private int pageSize = DEFAULT_PAGE_SIZE;

	/**
	 * 當前頁第一條數據在List中的位置,從0開始
	 */
	private long start;

	/**
	 * 當前頁中存放的記錄,類型一般為List
	 */
	private Object data;

	/**
	 * 總記錄數
	 */
	private long totalCount;

	/**
	 * 構造方法，只構造空頁
	 */
	public Page() {
		this(0, 0, DEFAULT_PAGE_SIZE, new ArrayList());
	}

	/**
	 * 默認構造方法
	 *
	 * @param start	 本頁數據在數據庫中的起始位置
	 * @param totalSize 數據庫中總記錄條數
	 * @param pageSize  本頁容量
	 * @param data	  本頁包含的數據
	 */
	public Page(long start, long totalSize, int pageSize, Object data) {
		this.pageSize = pageSize;
		this.start = start;
		this.totalCount = totalSize;
		this.data = data;
	}

	/**
	 * 取數據庫中包含的總記錄數
	 */
	public long getTotalCount() {
		return this.totalCount;
	}
	
	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}

	/**
	 * 取總頁數
	 */
	public long getTotalPageCount() {
//		20150427 edit by hugo req by eACH 無資料時總頁數就是1
		if(totalCount == 0){
			return 1;
		}
		else if (totalCount % pageSize == 0)
			return totalCount / pageSize;
		else
			return totalCount / pageSize + 1;
//		if (totalCount % pageSize == 0)
//			return totalCount / pageSize;
//		else
//			return totalCount / pageSize + 1;
	}

	/**
	 * 取每頁數據容量
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * 當前頁中的記錄
	 */
	public Object getResult() {
		return data;
	}

	/**
	 * 取當前頁碼,頁碼從1開始
	 */
	public long getCurrentPageNo() {
		return start / pageSize + 1;
	}

	/**
	 * 是否有下一頁
	 */
	public boolean hasNextPage() {
		return this.getCurrentPageNo() < this.getTotalPageCount() - 1;
	}

	/**
	 * 是否有上一頁
	 */
	public boolean hasPreviousPage() {
		return this.getCurrentPageNo() > 1;
	}

	/**
	 * 獲取任一頁第一條數據的位置，每頁條數使用默認值
	 */
	protected static int getStartOfPage(int pageNo) {
		return getStartOfPage(pageNo, DEFAULT_PAGE_SIZE);
	}

	/**
	 * 獲取任一頁第一條數據的位置,startIndex從0開始
	 */
	public static int getStartOfPage(int pageNo, int pageSize) {
		return (pageNo - 1) * pageSize;
	}

}

