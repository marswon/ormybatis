package cn.bblink.common.ormybatis.generate;

import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;

import cn.bblink.common.ormybatis.generate.util.DBUtils;

import com.google.common.base.CaseFormat;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * jdbc连接tool数据库
 * @author donghui
 */
public class GenerateMapper {
	static String package_name = "cn.bblink.eagleEye.model.po";
	
	static String table_name = "log_wechat_auth";
	
	public static void main(String[] args) throws Exception {
		List<Map> list = DBUtils.selectList("desc "+ table_name);
	    String className = GenerateMapper.getClassName(table_name);
	    Map root = new HashMap();  
	    root.put("tableName", table_name);
	    root.put("className", className);
	    GenerateMapper.data(list);
	    root.put("fieldList", list);
	    root.put("packageName", package_name);
	    
		Configuration  cfg = new Configuration();
	    cfg.setDirectoryForTemplateLoading(new ClassPathResource("").getFile());  
        Template template = cfg.getTemplate("bean.ftl");
        template.process(root, new FileWriter("/"+ className +".java"));
	}
	
	private static void data(List<Map> list){
		for (Map map : list) {
			String Field =  (String)map.get("Field");
			String type =  (String)map.get("Type");
			Boolean isPk = map.get("Key").equals("PRI");//是否主键PRI
			
			String javaType = "";
			if (StringUtils.containsIgnoreCase(type, "int")) {
				javaType = "Long";
			} else if(StringUtils.containsIgnoreCase(type, "double")){
				javaType = "BigDecimal";
			} else if (type.startsWith("varchar") || type.startsWith("longtext") || type.startsWith("text") ) {
				javaType = "String";
			} else if (type.startsWith("date") || type.startsWith("timestamp")) {
				javaType = "Date";
			}
			if (isPk) {
				map.put("properName", "id");
			} else {
				map.put("properName", CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, Field));
			}
			map.put("PROPER_NAME", Field.toUpperCase());
			map.put("type", javaType);
		}
	}
	
	private static String getClassName(String tableName){
		tableName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, tableName);
		return StringUtils.removeStartIgnoreCase(tableName, "bblink");
	}
	
}