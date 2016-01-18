package cn.bblink.common.ormybatis.bean.mapping;

import java.util.Map;

/**
 * insert单个对象 mapping参数bean
 * 
 * @author donghui
 */
public class InsertMappingParamBean extends BaseMappingParam{

	/**
	 * po 对象 map形式
	 */
	private Map poMap;
	
	/**
	 * 新增主键ID 值
	 */
	private Long insertId;

	public InsertMappingParamBean(){}
	
	public InsertMappingParamBean(Object po){
		poMap = super.toMap(po);
	}
	
	public Map getPoMap() {return poMap;}
	public void setPoMap(Map poMap) {this.poMap = poMap;}
	
	public Long getInsertId() {return insertId;}
	public void setInsertId(Long insertId) {this.insertId = insertId;}
}
