package cn.bblink.common.mysql.bean;

import com.google.common.base.CaseFormat;

/**
 * 运算表达式bean
 * @author donghui
 */
public class OrderByBean {
	/**
	 * 字段名
	 */
	private String field;
	
	/**
	 * 排序方式desc or asc
	 */
	private String oper;
	
	public OrderByBean(){}
	
	public OrderByBean(String field, String oper){
		field = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, field);
		setField(field);
		setOper(oper);
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getOper() {
		return oper;
	}

	public void setOper(String oper) {
		this.oper = oper;
	}
}
