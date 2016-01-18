package cn.bblink.common.ormybatis.bean.mapping;

import cn.bblink.common.ormybatis.builder.WhereBuilder;

/**
 * 删除mapping参数bean
 * 
 * @author donghui
 */
public class DeleteMappingParamBean extends BaseMappingParam{

	/**
	 * where查询条件
	 */
	private WhereBuilder whereBuilder;
	
	public DeleteMappingParamBean(){}
	
	public DeleteMappingParamBean(WhereBuilder whereBuilder){
		this.whereBuilder = whereBuilder;
	}
	
	public WhereBuilder getWhereBuilder() {return whereBuilder;}
	public void setWhereBuilder(WhereBuilder whereBuilder) {this.whereBuilder = whereBuilder;}
}
