package cn.bblink.common.ormybatis.bean.mapping;

import cn.bblink.common.ormybatis.builder.WhereBuilder;

/**
 * 查询select mapping参数bean
 * 
 * @author donghui
 */
public class SelectMappingParamBean extends BaseMappingParam{

	/**
	 * where查询条件
	 */
	private WhereBuilder whereBuilder;
	
	public SelectMappingParamBean(){}
	
	public SelectMappingParamBean(WhereBuilder whereBuilder){
		this.whereBuilder = whereBuilder;
	}
	
	public WhereBuilder getWhereBuilder() {return whereBuilder;}
	public void setWhereBuilder(WhereBuilder whereBuilder) {this.whereBuilder = whereBuilder;}
}
