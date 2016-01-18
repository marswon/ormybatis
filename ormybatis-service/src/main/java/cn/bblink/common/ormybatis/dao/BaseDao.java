package cn.bblink.common.ormybatis.dao;

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

import cn.bblink.common.ormybaits.paging.PagingBean;
import cn.bblink.common.ormybatis.bean.mapping.BaseMappingParam;
import cn.bblink.common.ormybatis.bean.mapping.DeleteMappingParamBean;
import cn.bblink.common.ormybatis.bean.mapping.InsertBatchMappingParamBean;
import cn.bblink.common.ormybatis.bean.mapping.InsertMappingParamBean;
import cn.bblink.common.ormybatis.bean.mapping.SelectMappingParamBean;
import cn.bblink.common.ormybatis.bean.mapping.UpdateMappingParamBean;
import cn.bblink.common.ormybatis.builder.UpdateBuilder;
import cn.bblink.common.ormybatis.builder.WhereBuilder;
import cn.bblink.common.ormybatis.util.ReflectUtil;

import com.google.common.base.CaseFormat;

public class BaseDao<T> extends SqlSessionTemplate {
	protected final Logger log = LoggerFactory.getLogger(getClass());

	/**
	 * ormybaits_sql.xml的namespace
	 */
	private String BASE_SQL_NAMESPACE = "ormybaits_sql";

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
		return this.paging(page, size, new WhereBuilder().eq(paramMap));
	}
	
	/**
	 *  分页查询
	 * @param page 第几页
	 * @param size 每页记录数
	 * @param qb 查询构造器
	 */
	public PagingBean<T> paging(int page, int size, WhereBuilder qb) {
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
        WhereBuilder qb = new WhereBuilder().eq(this.primaryKeyName, id);
        return this.selectOne(qb);
    }

    /**
     * 按id主键查询
     * @param ids 主键
     * @return
     */
    public List<T> selectList(List<Number> ids) {
        WhereBuilder qb = new WhereBuilder().in(this.primaryKeyName, ids);
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
        WhereBuilder qb = new WhereBuilder().eq(paramMap).orderDesc(this.primaryKeyName);
        return this.selectOne(qb);
    }

    /**
     * 查询单条记录按map拼查询条件
     * @param qb 查询构建builder对象
     * @return
     */
    public T selectOne(WhereBuilder qb) {
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
        WhereBuilder qb = new WhereBuilder().eq(paramMap);
        return this.selectList(qb);
    }

    /**
     * 查询多条记录
     *
     * @param qb 查询构建builder对象
     * @return
     */
    public List<T> selectList(WhereBuilder whereBuilder) {
    	String statement = this.getBaseSqlStatement("selecListByWhereBuilder");
    	SelectMappingParamBean selectMappingParamBean = new SelectMappingParamBean(whereBuilder);
    	this.getMappingParam(selectMappingParamBean);
        List<Map<String, Object>> mapList = super.selectList(statement, selectMappingParamBean);
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
        WhereBuilder qb = new WhereBuilder().eq(paramMap);
        return this.count(qb);
    }

    /**
     * 查询记录数
     *
     * @param qb
     * @return
     */
    public Long count(WhereBuilder whereBuilder) {
    	String statement = this.getBaseSqlStatement("countByWhereBuilder");
    	SelectMappingParamBean selectMappingParamBean = new SelectMappingParamBean(whereBuilder);
    	this.getMappingParam(selectMappingParamBean);
        return super.selectOne(statement, selectMappingParamBean);
    }

    /**
     * 单条插入
     *
     * @param po
     * @return 插入记录数
     */
   public int insert(T po) {
	   String statement = this.getBaseSqlStatement("insert");
	   InsertMappingParamBean insertMappingParamBean = new InsertMappingParamBean(po);
	   this.getMappingParam(insertMappingParamBean);
	   int num = super.insert(statement, insertMappingParamBean);
	   ReflectUtil.setBeanPropertyValue(po, "id", insertMappingParamBean.getInsertId());
	   return num;
   }

    /**
     * 批量插入
     *
     * @param list PO记录集
     * @return 插入记录数
     */
    public int insertBatch(List<T> poList) {
    	String statement = this.getBaseSqlStatement("insertBatch");
    	InsertBatchMappingParamBean insertBatchMappingParamBean = new InsertBatchMappingParamBean(poList);
    	this.getMappingParam(insertBatchMappingParamBean);
        return super.insert(statement, insertBatchMappingParamBean);
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
    public int insertOrUpdate(T po, WhereBuilder whereBuilder) {
        T selectPo = this.selectOne(whereBuilder);
        if (selectPo == null) {
        	return this.insert(po);
        } else {
        	Object id = ReflectUtil.getBeanPropertyValue(selectPo, "id");
            ReflectUtil.setBeanPropertyValue(po, "id", id);
            return this.update(po);
        }
    }

    /**
     * 按po的id主键查找修改po,为空的属性不修改
     *
     * @param po
     * @return 修改记录数
     */
    public int update(T po) {
        Map<String, Object> updateFieldMap = this.toMap(po);
        UpdateBuilder updateBuilder = new UpdateBuilder()
        	.updateEq(updateFieldMap)
        	.whereEq(this.primaryKeyName, ReflectUtil.getBeanPropertyValue(po, "id"));
        return this.update(updateBuilder);
    }

    /**
     * 通过参数构造器来update记录
     *
     * @param updateBuilder 参数构造器
     * @return 修改记录数量
     */
    public int update(UpdateBuilder updateBuilder) {
        //如果where为空,不修改
        if (updateBuilder.getWhereBuilder().isEmptyOperExpressList()) {
            return 0;
        }
        String statement = this.getBaseSqlStatement("updateByBuilder");
        UpdateMappingParamBean updateMappingParamBean =  new UpdateMappingParamBean(updateBuilder);
        this.getMappingParam(updateMappingParamBean);
        return super.update(statement, updateMappingParamBean);
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return 删除记录数
     */
    public int delete(Number... ids) {
        WhereBuilder whereBuilder = new WhereBuilder()
        	.in(this.primaryKeyName, Arrays.asList(ids));
        return this.delete(whereBuilder);
    }
    
    /**
     * 批量删除
     *
     * @param whereBuilder where条件
     * @return 删除记录数
     */
    public int delete(WhereBuilder whereBuilder) {
        //如果where为空,不修改
        if (whereBuilder.isEmptyOperExpressList()) {
            return 0;
        }
        String statement = this.getBaseSqlStatement("delete");
        DeleteMappingParamBean deleteMappingParamBean = new DeleteMappingParamBean(whereBuilder);
        this.getMappingParam(deleteMappingParamBean);
        return super.delete(statement, deleteMappingParamBean);
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
     * 得到ormybaits_sql.xml namespace
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
     * 拼装基础Mapping参数
     * @return
     */
    private void getMappingParam(BaseMappingParam baseMappingParam) {
    	baseMappingParam.setPrimaryKeyName(this.primaryKeyName);
    	baseMappingParam.setTableName(this.tableName);
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