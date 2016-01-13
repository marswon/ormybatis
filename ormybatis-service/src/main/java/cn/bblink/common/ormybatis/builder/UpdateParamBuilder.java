package cn.bblink.common.ormybatis.builder;

import java.util.Map;

/**
 * update sql参数构造器
 * @author donghui
 */
public class UpdateParamBuilder {
	/**
	 * 查询条件构造器
	 */
	private QueryBuilder qb = new QueryBuilder();
	
	/**
	 * 修改字段Map
	 */
	private UpdateBuilder updateQb = new UpdateBuilder();
	
	/**
	 * 构造修改的字段(updateMap key)和值(updateMap value)
	 * @param updateMap
	 * @return
	 */
	public UpdateParamBuilder upField(Map<String, Object> updateMap){
		updateMap.putAll(updateMap);
		return this;
	}
	
	/**
	 * 构造修改的字段和值
	 * @param updateFieldName 
	 * @param updateValue
	 * @return
	 */
	public UpdateParamBuilder set(String updateFieldName, Object updateValue){
		updateQb.set(updateFieldName, updateValue);
		return this;
	}
	
	/**
	 * 自增 field_name = field_name + 1
	 */
	public UpdateParamBuilder incr(String updateFieldName){
		updateQb.incr(updateFieldName);
		return this;
	}
	
	/**
	 * 自增 field_name = field_name + num
	 */
	public UpdateParamBuilder incr(String updateFieldName, Number num){
		updateQb.incr(updateFieldName, num);
		return this;
	}
	
	/**
	 * 查询条件 等于 (=)
	 */
	public UpdateParamBuilder whereEq(String field, Object value) {
		qb.eq(field, value);
		return this;
	}
	/**
	 * 大于(>)
	 */
	public UpdateParamBuilder whereQt(String field, Object value) {
		qb.gt(field, value);
		return this;
	}
	
	/**
	 * 大于等于(>=) 
	 */
	public UpdateParamBuilder whereGte(String field, Object value) {
		qb.gte(field, value);
		return this;
	}
	
	/**
	 * 小于(<) 
	 */
	public UpdateParamBuilder whereLt(String field, Object value) {
		qb.lt(field, value);
		return this;
	}
	
	/**
	 * 小于等于(<=) 
	 */
	public UpdateParamBuilder whereLte(String field, Object value) {
		qb.lte(field, value);
		return this;
	}
	
	/**
	 * like查询(% + value + %)
	 */
	public UpdateParamBuilder whereLike(String field, Object value) {
		qb.like(field, value);
		return this;
	}
	
	/**
	 * like查询(value + %)
	 */
	public UpdateParamBuilder whereLikeLeft(String field, Object value) {
		qb.likeLeft(field, value);
		return this;
	}
	
	/**
	 * like查询(% + value)
	 */
	public UpdateParamBuilder whereLikeRight(String field, Object value) {
		qb.likeRight(field, value);
		return this;
	}
	
	public QueryBuilder getQb() {return qb;}
	public void setQb(QueryBuilder qb) {this.qb = qb;}
	
	public UpdateBuilder getUpdateQb() {return updateQb;}
	public void setUpdateQb(UpdateBuilder updateQb) {this.updateQb = updateQb;}
}