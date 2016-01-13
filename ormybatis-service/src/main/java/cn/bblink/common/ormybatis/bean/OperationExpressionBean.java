package cn.bblink.common.ormybatis.bean;

/**
 * 运算表达式bean
 * @author donghui
 */
public class OperationExpressionBean {

	private String field;//字段名
	private String oper;//操作符
	private Object value;//值
	
	public OperationExpressionBean(){}
	
	public OperationExpressionBean(String field, String oper, Object value){
		this.field = field;
		this.oper = oper;
		this.value = value;
	}

	public String getField() {return field;}
	public void setField(String field) {this.field = field;}

	public String getOper() {return oper;}

	public void setOper(String oper) {this.oper = oper;}
	public Object getValue() {return value;}

	public void setValue(Object value) {this.value = value;}
}
