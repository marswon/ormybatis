package cn.bblink.common.mysql.util;

import java.lang.reflect.Field;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections4.MapUtils;

/**
 * 反射工具
 * 
 * @author donghui
 */
public class ReflectUtil {
    /**
     * 获取obj对象fieldName的Field
     * 
     * @param obj
     * @param fieldName
     * @return
     */
    public static Field getFieldByFieldName(Object obj, String fieldName) {
        for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                return superClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
            }
        }
        return null;
    }

    /**
     * 获取obj对象fieldName的属性值
     * 
     * @param obj
     * @param fieldName
     * @return
     * @throws SecurityException
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static Object getValueByFieldName(Object obj, String fieldName) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field field = getFieldByFieldName(obj, fieldName);
        Object value = null;
        if (field != null) {
            if (field.isAccessible()) {
                value = field.get(obj);
            } else {
                field.setAccessible(true);
                value = field.get(obj);
                field.setAccessible(false);
            }
        }
        return value;
    }

    /**
     * 设置obj对象fieldName的属性值
     * 
     * @param obj
     * @param fieldName
     * @param value
     * @throws SecurityException
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static void setValueByFieldName(Object obj, String fieldName, Object value) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field field = obj.getClass().getDeclaredField(fieldName);
        if (field.isAccessible()) {
            field.set(obj, value);
        } else {
            field.setAccessible(true);
            field.set(obj, value);
            field.setAccessible(false);
        }
    }
    
    
    public static Object getBeanPropertyValue(Object bean, String propertyName) {
        Object value = new Object();
        try {
            if (bean instanceof Map) {
                Map map = (Map) bean;
                value = MapUtils.getObject(map, propertyName, "");
            } else {
                value = PropertyUtils.getSimpleProperty(bean, propertyName);
            }
        } catch (Exception e) {
            return null;
        }
        return value;
    }

    public static void setBeanPropertyValue(Object bean, String propertyName, Object propertyValue) {
        try {
            if (bean instanceof Map) {
                Map map = (Map) bean;
                map.put(propertyName, propertyValue);
            } else {
                PropertyUtils.setSimpleProperty(bean, propertyName, propertyValue);
            }
        } catch (Exception e) {
        }
    }
}