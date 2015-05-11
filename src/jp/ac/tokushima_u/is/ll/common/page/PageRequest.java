package jp.ac.tokushima_u.is.ll.common.page;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Page request
 */
public class PageRequest<T> implements Serializable {

	private static final long serialVersionUID = -118671930702913528L;
	/**
	 * Page number, start from 1
	 */
	private int pageNumber;
	/**
	 * Page size
	 */
	private int pageSize;
	/**
	 * Sort,e.g.: username desc
	 */
	private String sortColumns;

	private Map<String, Object> attribute = new HashMap<String, Object>();

	public PageRequest() {
		this(0, 0);
	}

	public PageRequest(int pageNumber, int pageSize) {
		this(pageNumber, pageSize, null);
	}

	public PageRequest(int pageNumber, int pageSize, String sortColumns) {
		this.pageNumber = pageNumber;
		this.pageSize = pageSize;
		setSortColumns(sortColumns);
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getSortColumns() {
		return sortColumns;
	}

	/**
	 * Set sort columns,e.g.: username desc,age asc
	 * 
	 * @return
	 */
	public void setSortColumns(String sortColumns) {
		checkSortColumnsSqlInjection(sortColumns);
		if (sortColumns != null && sortColumns.length() > 50) {
			throw new IllegalArgumentException(
					"sortColumns.length() <= 50 must be true");
		}
		this.sortColumns = sortColumns;
	}

	/**
	 * Analyze sortColumns, get SortInfo
	 * 
	 * @return
	 */
	public List<SortInfo> getSortInfos() {
		return Collections.unmodifiableList(SortInfo
				.parseSortColumns(sortColumns));
	}

	private void checkSortColumnsSqlInjection(String sortColumns) {
		if (sortColumns == null)
			return;
		if (sortColumns.indexOf("'") >= 0 || sortColumns.indexOf("\\") >= 0) {
			throw new IllegalArgumentException("sortColumns:" + sortColumns
					+ " has SQL Injection risk");
		}
	}

	public int getFirstResult() {
		return PageUtils.getFirstResult(pageNumber, pageSize);
	}

	public Map<String, Object> getAttribute() {
		if (attribute == null)
			attribute = new HashMap<String, Object>();
		return attribute;
	}

	public void setAttribute(Map<String, Object> attribute) {
		this.attribute = attribute;
	}

	public void addAttribute(String key, Object value) {
		this.getAttribute().put(key, value);
	}
}
