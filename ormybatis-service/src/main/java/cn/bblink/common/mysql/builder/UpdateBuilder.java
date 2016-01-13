package cn.bblink.common.mysql.builder;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import cn.bblink.common.mysql.bean.OperationExpressionBean;

/**
 * 查询构造器
 * @author donghui
 */
public class UpdateBuilder {
	private List<OperationExpressionBean> operExpressList = new ArrayList<OperationExpressionBean>();
	
	public UpdateBuilder() {}
	public UpdateBuilder(String field, Object value) {
		this.set(field, value);
	}
	
	/**
	 * 等于 (=)
	 */
	public UpdateBuilder set(String field, Object value) {
		return this.setOperationExpression(field, "=", value);
	}
	
	/**
	 * 等于 (=)
	 */
	public UpdateBuilder set(Map<String, Object> paramMap) {
		if (MapUtils.isNotEmpty(paramMap)) {
			for (String key : paramMap.keySet()) {
				this.set(key, paramMap.get(key));
			}
		}
		return this;
	}
	
	/**
	 * update时 field_name = field_name + 1
	 */
	public UpdateBuilder incr(String field) {
		return this.incr(field, 1);
	}
	
	/**
	 * update时 field_name = field_name + num
	 */
	public UpdateBuilder incr(String field, Number num) {
		return this.setOperationExpression(field, "incr", num);
	}
	
	/**
	 * 判断查询条件lis不为空
	 * @return
	 */
	public boolean isEmptyOperExpressList() {
		return CollectionUtils.isEmpty(this.operExpressList);
	}
	
    private UpdateBuilder setOperationExpression(String field, String oper, Object value){
    	if (value == null) {
			return this;
		}
		if (value instanceof String) {
			if (StringUtils.isBlank((String)value)) {
				return this;
			}
		}
    	OperationExpressionBean bean = new OperationExpressionBean(field, oper, value);
    	operExpressList.add(bean);
    	return this;
    }
    
	public List<OperationExpressionBean> getOperExpressList() {return operExpressList;}
	public void setOperExpressList(List<OperationExpressionBean> operExpressList) {this.operExpressList = operExpressList;}
	
}