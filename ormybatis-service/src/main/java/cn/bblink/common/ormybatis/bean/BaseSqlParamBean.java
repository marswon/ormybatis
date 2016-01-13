package cn.bblink.common.ormybatis.bean;

public class BaseSqlParamBean {

	private Object Data;//参数对象
	private String tableName;//表名
	private String primaryKeyName;//主键字段名
	
	private Long insertId;
	
	public Object getData() {
		return Data;
	}
	public void setData(Object data) {
		Data = data;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getPrimaryKeyName() {
		return primaryKeyName;
	}
	public void setPrimaryKeyName(String primaryKeyName) {
		this.primaryKeyName = primaryKeyName;
	}
	public Long getInsertId() {
		return insertId;
	}
	public void setInsertId(Long insertId) {
		this.insertId = insertId;
	}
}
