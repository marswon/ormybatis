package cn.bblink.common.ormybatis.bean.mapping;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.google.common.base.CaseFormat;

/**
 * 基础Mapping参数bean
 * 
 * @author donghui
 */
public class BaseMappingParam {
	
	/**
	 * 表名
	 */
	private String tableName;
	
	/**
	 * 主键字段名
	 */
	private String primaryKeyName;	
	
	public String getTableName() {return tableName;}
	public void setTableName(String tableName) {this.tableName = tableName;}
	
	public String getPrimaryKeyName() {return primaryKeyName;}
	public void setPrimaryKeyName(String primaryKeyName) {this.primaryKeyName = primaryKeyName;}

    protected Map<String, Object> toMap(Object po) {
        Class type = po.getClass();
        Map returnMap = new HashMap();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(type);
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (int i = 0; i < propertyDescriptors.length; i++) {
                PropertyDescriptor descriptor = propertyDescriptors[i];
                String propertyName = descriptor.getName();
                propertyName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, propertyName);
                if (!propertyName.equals("class")) {
                    Method readMethod = descriptor.getReadMethod();
                    Object result = readMethod.invoke(po, new Object[0]);
                    if (result != null) {
                        returnMap.put(propertyName, result);
                    } else {
                        returnMap.put(propertyName, null);
                    }
                }
            }
        } catch (Exception e) {}
        return returnMap;
    }
    
}