package cn.bblink.common.ormybatis.interceptor;

import java.sql.Connection;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.ibatis.executor.statement.BaseStatementHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;

import cn.bblink.common.ormybatis.util.ReflectUtil;

@Intercepts({ @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class }) })
public class PagingSQLInterceptor implements Interceptor {
	public Object intercept(Invocation invocation) throws Throwable {
		 if (invocation.getTarget() instanceof RoutingStatementHandler){
			RoutingStatementHandler statementHandler = (RoutingStatementHandler)invocation.getTarget();
            BaseStatementHandler delegate = (BaseStatementHandler) ReflectUtil.getValueByFieldName(statementHandler, "delegate");  
            MappedStatement mappedStatement = (MappedStatement) ReflectUtil.getValueByFieldName(delegate, "mappedStatement");  
            String statmentName = mappedStatement.getId();
            SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
			BoundSql boundSql = statementHandler.getBoundSql();
			Object parameterObject = boundSql.getParameterObject();
			String sql = boundSql.getSql();
			if(SqlCommandType.SELECT.equals(sqlCommandType)){
				Object pageObj = ReflectUtil.getBeanPropertyValue(parameterObject, "page");
				Object pageSizeObj = ReflectUtil.getBeanPropertyValue(parameterObject, "pageSize");
				//后页管理freemark列表页面分页
				if (pageObj != null && pageSizeObj != null && !StringUtils.endsWith(statmentName, "Count")) {
					int page = NumberUtils.toInt(pageObj.toString());
					int pageSize = NumberUtils.toInt(pageSizeObj.toString());
					if (page > 0) {
						page--;
					}
					int start = page * pageSize;
					sql = sql + " LIMIT " + start + "," + pageSize;
					ReflectUtil.setValueByFieldName(boundSql, "sql", sql);
				}
			}
		 }
		return invocation.proceed();
	}
	
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}
	
	public void setProperties(Properties properties) {}
	
}