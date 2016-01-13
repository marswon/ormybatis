package ${packageName};

import java.util.Date;
import java.io.Serializable;

import lombok.Data;

@Data
public class ${className} implements Serializable {

	<#list fieldList as item>
	${item.type} ${item.properName};
	</#list>
	
	public final static String
	<#list fieldList as item>
		F_${item.PROPER_NAME} = "${item.Field}"<#if item_has_next>,<#else>;</#if>
	</#list>
	
	public final static String DB_TABLE_NAME = "${tableName}";
}