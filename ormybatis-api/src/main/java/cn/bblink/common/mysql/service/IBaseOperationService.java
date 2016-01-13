package cn.bblink.common.mysql.service;

import java.util.List;
import java.util.Map;

import cn.bblink.common.mysql.paging.PagingBean;

public interface IBaseOperationService<T> {

	/**
	 *  分页查询
	 * @param page 第几页	
	 * @param size 每页记录数
	 * @param paramMap 查询Map
	 */
	PagingBean<T> paging(int page, int size, Map<String, Object> paramMap);
    
    /**
     * 按id主键查询
     *
     * @param id 主键
     * @return
     */
    T selectById(Object id);

    /**
     * 按多个id主键查询
     *
     * @param ids ID主键集合
     * @return
     */
    List<T> selectById(List ids);

    /**
     * 查询多条记录按map拼查询条件
     *
     * @param paramMap
     * @return
     */
    List<T> selectList(Map<String, Object> paramMap);

    /**
     * 查询记录数
     *
     * @param paramMap
     * @return
     */
    Long count(Map<String, Object> paramMap);

    /**
     * 单条插入
     *
     * @param po
     * @return 插入记录数
     */
    int insert(T po);

    /**
     * 批量插入
     *
     * @param list PO记录集
     * @return 插入记录数
     */
    int insertBatch(List<T> list);

    /**
     * 按po id主键查询有则update,无则insert
     *
     * @param po
     * @return
     */
    int insertOrUpdate(T po);

    /**
     * 按po的id主键查找修改po
     *
     * @param po
     * @return 修改记录数
     */
    int update(T po);

    /**
     * 按po的id主键查找修改po,po属性为空则忽略更新
     *
     * @param po
     * @return 修改记录数
     */
    int updateSelective(T po);

    /**
     * 批量删除
     *
     * @param idList id主键集合
     * @return 删除记录数
     */
    int delete(List<Number> idList);
}