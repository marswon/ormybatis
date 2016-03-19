package cn.bblink.common.ormybatis.bean.mapping;

import cn.bblink.common.ormybatis.builder.UpdateFieldBuilder;
import cn.bblink.common.ormybatis.builder.UpdateBuilder;
import cn.bblink.common.ormybatis.builder.WhereBuilder;

/**
 * 修改update mapping参数bean
 * 
 * @author donghui
 */
public class UpdateMappingParamBean extends BaseMappingParam{

	/**
	 * 修改update字段构造器
	 */
	private UpdateFieldBuilder updateBuilder;

	/**
	 * where查询条件
	 */
	private WhereBuilder whereBuilder;
	
	public UpdateMappingParamBean(){}
	
	public UpdateMappingParamBean(UpdateBuilder updateParamBuilder){
		this.updateBuilder = updateParamBuilder.getUpdateBuilder();
		this.whereBuilder = updateParamBuilder.getWhereBuilder();
	}

	public UpdateFieldBuilder getUpdateBuilder() {return updateBuilder;}
	public void setUpdateBuilder(UpdateFieldBuilder updateBuilder) {this.updateBuilder = updateBuilder;}

	public WhereBuilder getWhereBuilder() {return whereBuilder;}
	public void setWhereBuilder(WhereBuilder whereBuilder) {this.whereBuilder = whereBuilder;}
}
