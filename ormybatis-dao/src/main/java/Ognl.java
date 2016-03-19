import java.util.Collection;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Ognl工具类，主要是为了在ognl表达式访问静态方法时可以减少长长的类名称编写
 * Ognl访问静态方法的表达式为: @class@method(args)
 * 
 * 示例使用: 
 * <pre>
 * 	< if test="@Ognl@isNotBank(userId)">
 *		and user_id = #{userId}
 *	< /if>
 * </pre>
 * @author badqiu
 * @author 
 */
public class Ognl extends Object {
	
	public static boolean isBlank(Object o) {
		if (o == null) {
			return true;
		}
		if (o instanceof String) {
			return StringUtils.isBlank((String)o);
		}
		else if (o instanceof Collection) {
			return CollectionUtils.isEmpty((Collection)o);
		}
		else if (o instanceof Map) {
			return MapUtils.isEmpty((Map)o);
		}
		else if (o instanceof Object[]) {
			return ArrayUtils.isEmpty((Object[])o);
		}
		return false;
	}
	
	public static boolean isNotBlank(Object o) {
		return !isBlank(o);
	}
	
	public static boolean isNumber(Object o) {
		if (o == null){ 
			return false;
		}
		if (o instanceof Number) {
			return true;
		}
		else if (o instanceof String) {
			return StringUtils.isNumeric((String)o);
		}
		return false;
	}
	
}
