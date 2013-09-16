package cn.leyundong.entity;

import java.util.List;


public class Page<T>  implements java.io.Serializable {
	/**
	 * 
	 */
	public static final long serialVersionUID = 1L;
	public int pageSize = 10; // 每页默认10条数据
	public int currentPage = 1; // 当前页
	public int totalPages = 0; // 总页数
	public int totalRows = 0; // 总数据数
	public int pageStartRow = 0; // 每页的起始行数
	public int pageEndRow = 0; // 每页显示数据的终止行数
	public boolean pagination=false;   //是否分页
	public boolean hasNextPage = false; // 是否有下一页
	public boolean hasPreviousPage = false; // 是否有前一页
	public String pagedView; // 用于页面显示
	public List<T> list;//返回结果集
	public String pageStr; // 用于页面显示
	@Override
	public String toString() {
		return "Page [hasNextPage=" + hasNextPage + ", list=" + list + "]";
	}

}
