package cn.bblink.common.ormybatis.builder;

import java.util.Map;

/**
 * update sql参数构造器
 * @author donghui
 */
public class UpdateBuilder {
	/**
	 * 查询条件构造器
	 */
	private WhereBuilder whereBuilder = new WhereBuilder();

	/**
	 * 修改字段Map
	 */
	private UpdateFieldBuilder updateFieldBuilder = new UpdateFieldBuilder();

	/**
	 * 构造修改的Map字段(updateMap key)和值(updateMap value)
	 * @param updateFieldMap
	 * @return
	 */
	public UpdateBuilder updateEq(Map<String, Object> updateFieldMap){
		updateFieldBuilder.eq(updateFieldMap);
		return this;
	}
	
	/**
	 * 构造修改的字段和值
	 * @param updateFieldName 
	 * @param updateValue
	 * @return
	 */
	public UpdateBuilder updateEq(String updateFieldName, Object updateValue){
		updateFieldBuilder.eq(updateFieldName, updateValue);
		return this;
	}
	
	/**
	 * 自增 field_name = field_name + 1
	 */
	public UpdateBuilder upateIncr(String updateFieldName){
		updateFieldBuilder.incr(updateFieldName);
		return this;
	}
	
	/**
	 * 自增 field_name = field_name + num
	 */
	public UpdateBuilder upateIncr(String updateFieldName, Number num){
		updateFieldBuilder.incr(updateFieldName, num);
		return this;
	}
	
	/**
	 * 设置为null
	 */
	public UpdateBuilder upateEqNull(String updateFieldName){
		updateFieldBuilder.eqNull(updateFieldName);
		return this;
	}
	
	/**
	 * 查询条件 等于 (=)
	 */
	public UpdateBuilder whereEq(String field, Object value) {
		whereBuilder.eq(field, value);
		return this;
	}
	/**
	 * 大于(>)
	 */
	public UpdateBuilder whereQt(String field, Object value) {
		whereBuilder.gt(field, value);
		return this;
	}
	
	/**
	 * 大于等于(>=) 
	 */
	public UpdateBuilder whereGte(String field, Object value) {
		whereBuilder.gte(field, value);
		return this;
	}
	
	/**
	 * 小于(<) 
	 */
	public UpdateBuilder whereLt(String field, Object value) {
		whereBuilder.lt(field, value);
		return this;
	}
	
	/**
	 * 小于等于(<=) 
	 */
	public UpdateBuilder whereLte(String field, Object value) {
		whereBuilder.lte(field, value);
		return this;
	}
	
	/**
	 * like查询(% + value + %)
	 */
	public UpdateBuilder whereLike(String field, Object value) {
		whereBuilder.like(field, value);
		return this;
	}
	
	/**
	 * like查询(value + %)
	 */
	public UpdateBuilder whereLikeLeft(String field, Object value) {
		whereBuilder.likeLeft(field, value);
		return this;
	}
	
	/**
	 * like查询(% + value)
	 */
	public UpdateBuilder whereLikeRight(String field, Object value) {
		whereBuilder.likeRight(field, value);
		return this;
	}
	
	public WhereBuilder getWhereBuilder() {return whereBuilder;}
	public void setWhereBuilder(WhereBuilder whereBuilder) {this.whereBuilder = whereBuilder;}
	
	public UpdateFieldBuilder getUpdateBuilder() {return updateFieldBuilder;}
	public void setUpdateBuilder(UpdateFieldBuilder updateBuilder) {this.updateFieldBuilder = updateBuilder;}
}