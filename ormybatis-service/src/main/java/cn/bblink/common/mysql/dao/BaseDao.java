package cn.bblink.common.mysql.dao;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.joda.time.DateTime;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.bblink.common.mysql.bean.BaseSqlParamBean;
import cn.bblink.common.mysql.builder.QueryBuilder;
import cn.bblink.common.mysql.builder.UpdateParamBuilder;
import cn.bblink.common.mysql.paging.PagingBean;
import cn.bblink.common.mysql.util.ReflectUtil;

import com.google.common.base.CaseFormat;

public class BaseDao<T> extends SqlSessionTemplate {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * base_sql.xml的namespace
     */
    private String BASE_SQL_NAMESPACE = "baseSql";

    /**
     * mysql 主键字段名
     */
    protected String primaryKeyName = "id";

    /**
     * mysql 表名
     */
    protected String tableName = this.getTableName();

    private int pageSize = 20;

    public BaseDao(SqlSessionFactory sqlSessionFactory) {
        super(sqlSessionFactory);
    }

    /**
	 *  分页查询
	 * @param page 第几页	
	 * @param size 每页记录数
	 * @param paramMap 查询Map
	 */
	public PagingBean<T> paging(int page, int size, Map<String, Object> paramMap) {
		return this.paging(page, size, new QueryBuilder().eq(paramMap));
	}
	
	/**
	 *  分页查询
	 * @param page 第几页
	 * @param size 每页记录数
	 * @param qb 查询构造器
	 */
	public PagingBean<T> paging(int page, int size, QueryBuilder qb) {
		PagingBean<T> pagingResult = new PagingBean<T>();
		pagingResult.setPage(page);
		pagingResult.setSize(size);
		int limitIndex = (page - 1) * size;
		qb.limit(limitIndex, size);
		Long count = this.count(qb);
		if (count > 0) {
			List<T> record = this.selectList(qb);
			pagingResult.setRecord(record);
			pagingResult.setCount(count);
		}
		return pagingResult;
	}
    
    /**
     * sql语句的分页
     * @param statement SQL语句id标识
     * @param parameter 查询条件参数
     * @return
     */
	public PagingBean paging(String statement, Object parameter) {
		Number totalCount = (Number)this.selectOne(statement + "Count", parameter);
		if (totalCount.longValue() == 0) {
			return new PagingBean();
		}
		if (parameter == null) {
			parameter = new Object();
		}
		Object pageObj = ReflectUtil.getBeanPropertyValue(parameter, "page");
		Object pageSizeObj = ReflectUtil.getBeanPropertyValue(parameter, "pageSize");
		if (pageObj == null || StringUtils.isBlank(pageObj.toString())) {
			pageObj = 1;
			ReflectUtil.setBeanPropertyValue(parameter, "page", pageObj);
		}
		if (pageSizeObj == null || StringUtils.isBlank(pageSizeObj.toString())) {
			pageSizeObj = this.pageSize;
			ReflectUtil.setBeanPropertyValue(parameter, "pageSize", pageSizeObj);
		}
		Integer page = NumberUtils.toInt(pageObj.toString());
		Integer pageSize = NumberUtils.toInt(pageSizeObj.toString());
		List record = this.selectList(statement, parameter);
		return new PagingBean(page, pageSize, totalCount.longValue(), record);
	}
	
    /**
     * 按id主键查询
     * @param id 主键
     * @return
     */
    public T selectOne(Number id) {
        QueryBuilder qb = new QueryBuilder().eq(this.primaryKeyName, id);
        return this.selectOne(qb);
    }

    /**
     * 按id主键查询
     * @param ids 主键
     * @return
     */
    public List<T> selectList(List<Number> ids) {
        QueryBuilder qb = new QueryBuilder().in(this.primaryKeyName, ids);
        return this.selectList(qb);
    }

    /**
     * 查询单条记录按map拼查询条件
     * <pre>
     * Map map = (K,V),(k1,v1)
     * "WHERE K = V AND k1 = v1"
     * </pre>
     * @param paramMap 查询条件K是bean属性名或DB字段名,V为值
     * @return
     */
    public T selectOne(Map<String, Object> paramMap) {
        QueryBuilder qb = new QueryBuilder().eq(paramMap).orderDesc(this.primaryKeyName);
        return this.selectOne(qb);
    }

    /**
     * 查询单条记录按map拼查询条件
     * @param qb 查询构建builder对象
     * @return
     */
    public T selectOne(QueryBuilder qb) {
    	qb.limit(1);
        List<T> result = this.selectList(qb);
        if (CollectionUtils.isEmpty(result)) {
            return null;
        }
        return result.get(0);
    }

    /**
     * 查询多条记录按map拼查询条件
     *
     * @param paramMap
     * @return
     */
    public List<T> selectList(Map<String, Object> paramMap) {
        QueryBuilder qb = new QueryBuilder().eq(paramMap);
        return this.selectList(qb);
    }

    /**
     * 查询多条记录
     *
     * @param qb 查询构建builder对象
     * @return
     */
    public List<T> selectList(QueryBuilder qb) {
//		if (qb.isEmptyOperExpressList()) {
//			return null;
//		}
        BaseSqlParamBean param = this.getBaseSqlParamBean(qb);
        List<Map<String, Object>> mapList = super.selectList(this.getBaseSqlStatement("selecListByQueryBuilder"), param);
        List<T> list = this.toPoList(mapList);
        return list;
    }

    /**
     * 查询记录数
     *
     * @param paramMap
     * @return
     */
    public Long count(Map<String, Object> paramMap) {
        QueryBuilder qb = new QueryBuilder().eq(paramMap);
        return this.count(qb);
    }

    /**
     * 查询记录数
     *
     * @param qb
     * @return
     */
    public Long count(QueryBuilder qb) {
        BaseSqlParamBean param = this.getBaseSqlParamBean(qb);
        return super.selectOne(this.getBaseSqlStatement("countByQueryBuilder"), param);
    }

    /**
     * 单条插入
     *
     * @param po
     * @return 插入记录数
     */
    public int insert(T po) {
        Map<String, Object> map = this.toMap(po);
        BaseSqlParamBean param = this.getBaseSqlParamBean(map);
        int num = super.insert(this.getBaseSqlStatement("insert"), param);
        ReflectUtil.setBeanPropertyValue(po, "id", param.getInsertId());
        return num;
    }

    /**
     * 批量插入
     *
     * @param list PO记录集
     * @return 插入记录数
     */
    public int insertBatch(List<T> list) {
        List<Map<String, Object>> mapList = new ArrayList();
        for (T po : list) {
            Map<String, Object> map = this.toMap(po);
            mapList.add(map);
        }
        BaseSqlParamBean param = this.getBaseSqlParamBean(mapList);
        return super.insert(this.getBaseSqlStatement("insertBatch"), param);
    }

    /**
     * 按po id主键查询有则update,无则insert
     *
     * @param po
     * @return
     */
    public int insertOrUpdate(T po) {
        int num = this.update(po);
        if (num == 0) {
            num = this.insert(po);
        }
        return num;
    }

    /**
     * 按qb查询条件查询有则update,无则insert
     *
     * @param po
     * @param qb
     * @return
     */
    public int insertOrUpdate(T po, QueryBuilder qb) {
        T selectPo = this.selectOne(qb);
        if (selectPo != null) {
            Object id = ReflectUtil.getBeanPropertyValue(selectPo, "id");
            ReflectUtil.setBeanPropertyValue(po, "id", id);
            return this.updateSelective(po);
        } else {
            return this.insert(po);
        }
    }

    /**
     * 按po的id主键查找修改po
     *
     * @param po
     * @return 修改记录数
     */
    public int update(T po) {
        Map<String, Object> map = this.toMap(po);
        BaseSqlParamBean param = this.getBaseSqlParamBean(map);
        return super.update(this.getBaseSqlStatement("updateById"), param);
    }

    /**
     * 按po的id主键查找修改po,po属性为空则忽略更新
     *
     * @param po
     * @return 修改记录数
     */
    public int updateSelective(T po) {
        Map<String, Object> map = this.toMap(po);
        BaseSqlParamBean param = this.getBaseSqlParamBean(map);
        return super.update(this.getBaseSqlStatement("updateByIdSelective"), param);
    }

    /**
     * 按指定的查询条件和修改字段来update记录
     *
     * @param updateFieldName 修改
     * @param updateValue     修改的值
     * @param whereFieldName  查询字段名
     * @param whereValue      查询值
     * @return 修改记录数
     */
    public int update(String updateFieldName, Object updateValue, String whereFieldName, Object whereValue) {
        UpdateParamBuilder updateParamBuilder = new UpdateParamBuilder()
                .set(updateFieldName, updateValue)
                .whereEq(whereFieldName, whereValue);
        return this.update(updateParamBuilder);
    }

    /**
     * 通过参数构造器来update记录
     *
     * @param updateParamBuilder 参数构造器
     * @return 修改记录数量
     */
    public int update(UpdateParamBuilder updateParamBuilder) {
        //如果where为空,不修改
        if (updateParamBuilder.getQb().isEmptyOperExpressList()) {
            return 0;
        }
        BaseSqlParamBean param = this.getBaseSqlParamBean(updateParamBuilder);
        return super.update(this.getBaseSqlStatement("updateByBuilder"), param);
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return 删除记录数
     */
    public int delete(Number... ids) {
        List<Number> idList = Arrays.asList(ids);
        return this.delete(idList);
    }

    /**
     * 批量删除
     *
     * @param idList id主键集合
     * @return 删除记录数
     */
    public int delete(List<Number> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return 0;
        }
        BaseSqlParamBean param = this.getBaseSqlParamBean(idList);
        return super.delete(this.getBaseSqlStatement("deleteBatch"), param);
    }

    /**
     * 重写父类方法statement string参数不用传入namespace
     *
     * @param statement SQL语句的id标识
     * @return
     */
    @Override
    public <E> E selectOne(String statement) {
        return super.selectOne(this.getStatement(statement));
    }

    @Override
    public <E> E selectOne(String statement, Object parameter) {
        return super.selectOne(this.getStatement(statement), parameter);
    }

    @Override
    public <E> List<E> selectList(String statement) {
        return super.selectList(this.getStatement(statement));
    }

    @Override
    public <E> List<E> selectList(String statement, Object parameter) {
        return super.selectList(this.getStatement(statement), parameter);
    }

    @Override
    public int insert(String statement) {
        return super.insert(this.getStatement(statement));
    }

    @Override
    public int insert(String statement, Object parameter) {
        return super.insert(this.getStatement(statement), parameter);
    }

    @Override
    public int update(String statement) {
        return super.update(this.getStatement(statement));
    }

    @Override
    public int update(String statement, Object parameter) {
        return super.update(this.getStatement(statement), parameter);
    }

    @Override
    public int delete(String statement) {
        return super.delete(this.getStatement(statement));
    }

    @Override
    public int delete(String statement, Object parameter) {
        return super.delete(this.getStatement(statement), parameter);
    }

    /**
     * 设置主键字段名
     */
    protected void setPrimaryKeyName(String primaryKeyName) {
        this.primaryKeyName = primaryKeyName;
    }

    /**
     * 设置mysql库表名
     */
    protected void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * 按当前实现类的名组成statement
     *
     * @param statement SQL语句的id标识
     * @return 全路径名的statement
     */
    private String getStatement(String statement) {
        return getNamespace() + "." + statement;
    }

    /**
     * 得到base_sql.xml namespace
     *
     * @param statement SQL语句的id标识
     * @return 全路径名的statement
     */
    private String getBaseSqlStatement(String statement) {
        return this.BASE_SQL_NAMESPACE + "." + statement;
    }

    /**
     * 得到当前操作表的mybatis的namespace
     *
     * @return
     */
    private String getNamespace() {
        String name = this.getClass().getSimpleName();
        name = StringUtils.removeEndIgnoreCase(name, "Dao");
        name = StringUtils.uncapitalize(name);
        return name;
    }

    /**
     * @return
     */
    private BaseSqlParamBean getBaseSqlParamBean(Object data) {
        BaseSqlParamBean param = new BaseSqlParamBean();
        param.setData(data);
        param.setPrimaryKeyName(this.primaryKeyName);
        param.setTableName(this.tableName);
        return param;
    }

    /**
     * 得到当前操作表的mybatis的namespace
     *
     * @return
     */
    private String getTableName() {
        String namespace = this.getNamespace();
        String tableName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, namespace);
        return tableName;
    }

    /**
     * map 转为 泛型类T po
     */
    private T toPo(Map<String, Object> map) {
        //map key变成驼峰命名法
        Map camelKeyMap = new HashMap();
        for (String key : map.keySet()) {
            Object value = map.get(key);
            String camelKey = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, key);
            if (value == null) {
                continue;
            }
            if (value instanceof Date) {
                DateTime dt = new DateTime(value);
                value = dt.toDate();
            }
            if (this.primaryKeyName.equals(key)) {
                camelKey = "id";
            }
            camelKeyMap.put(camelKey, value);
        }
        Type type = this.getClass().getGenericSuperclass();
        Type[] p = ((ParameterizedType) type).getActualTypeArguments();
        T po = null;
        try {
            po = ((Class<T>) p[0]).newInstance();
            BeanUtils.populate(po, camelKeyMap);
        } catch (Exception e) {
            log.error(null, e);
        }
        return po;
    }

    private Map<String, Object> toMap(T po) {
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
        } catch (Exception e) {
            log.error("转换Map出错.", e);
        }
        return returnMap;
    }

    /**
     * map 转为 泛型类T po
     */
    private List<T> toPoList(List<Map<String, Object>> mapList) {
        List<T> result = new ArrayList<T>();
        for (Map<String, Object> map : mapList) {
            T po = this.toPo(map);
            result.add(po);
        }
        return result;
    }

}