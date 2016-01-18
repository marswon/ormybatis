package cn.bblink.common.ormybatis.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import cn.bblink.common.ormybatis.bean.OperationExpressionBean;

/**
 * 修改update要修改的字段构造器
 * @author donghui
 */
public class UpdateFieldBuilder {
	private List<OperationExpressionBean> operExpressList = new ArrayList<OperationExpressionBean>();
	
	public UpdateFieldBuilder() {}
	public UpdateFieldBuilder(String field, Object value) {
		this.eq(field, value);
	}
	
	/**
	 * 等于 (=)
	 */
	public UpdateFieldBuilder eq(String field, Object value) {
		return this.setOperationExpression(field, "=", value);
	}
	
	/**
	 * 等于 (=)
	 */
	public UpdateFieldBuilder eq(Map<String, Object> paramMap) {
		if (MapUtils.isNotEmpty(paramMap)) {
			for (String key : paramMap.keySet()) {
				this.eq(key, paramMap.get(key));
			}
		}
		return this;
	}
	
	/**
	 * update时 field_name = field_name + 1
	 */
	public UpdateFieldBuilder incr(String field) {
		return this.incr(field, 1);
	}
	
	/**
	 * update时 field_name = field_name + num
	 */
	public UpdateFieldBuilder incr(String field, Number num) {
		return this.setOperationExpression(field, "incr", num);
	}
	
	/**
	 * 设置为null
	 */
	public UpdateFieldBuilder eqNull(String field) {
		OperationExpressionBean operationExpressionBean = new OperationExpressionBean(field, "=", null);
		this.operExpressList.add(operationExpressionBean);
		return this;
	}
	
	/**
	 * 判断查询条件lis不为空
	 * @return
	 */
	public boolean isEmptyOperExpressList() {
		return CollectionUtils.isEmpty(this.operExpressList);
	}
	
    private UpdateFieldBuilder setOperationExpression(String field, String oper, Object value){
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