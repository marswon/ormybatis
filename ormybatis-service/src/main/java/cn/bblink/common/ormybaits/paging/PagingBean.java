package cn.bblink.common.ormybaits.paging;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * bblink_mysql分页查询结果对象
 * @author donghui
 */
public class PagingBean<T> implements Serializable {

	/**
	 * 默认查第1页
	 */
	private Integer page = 1;
	
	/**
	 * 每页数查询数量，默认20条
	 */
	private Integer size = 20;
	
	/**
	 * 总记录数
	 */
	private Long count = 0L;

	/**
	 * 分页查询结果的记录集合
	 */
	private List<T> record = new ArrayList<T>();
	
	public PagingBean(){}
	
	public PagingBean(int page, int size, Long count, List<T> record){
		this.page = page;
		this.size = size;
		this.count = count;
		this.record = record;
	}
	
	public Integer getPage() {return page;}
	public void setPage(Integer page) {this.page = page;}

	public Integer getSize() {return size;}
	public void setSize(Integer size) {this.size = size;}

	public Long getCount() {return count;}
	public void setCount(Long count) {this.count = count;}

	public List<T> getRecord() {return record;}
	public void setRecord(List<T> record) {this.record = record;}
	
}
