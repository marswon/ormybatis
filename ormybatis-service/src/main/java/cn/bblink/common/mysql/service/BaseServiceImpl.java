package cn.bblink.common.mysql.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.bblink.common.mysql.dao.BaseDao;
import cn.bblink.common.mysql.paging.PagingBean;

public class BaseServiceImpl<T> implements IBaseOperationService<T> {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    protected BaseDao<T> dao;

    public BaseServiceImpl(BaseDao<T> baseDao) {
        this.dao = baseDao;
    }

	@Override
	public PagingBean<T> paging(int page, int size, Map<String, Object> paramMap) {
		return dao.paging(page, size, paramMap);
	}
    
    @Override
    public T selectById(Object id) {
        long lngId = NumberUtils.toLong(String.valueOf(id));
        return dao.selectOne(lngId);
    }

    @Override
    public List<T> selectById(List ids) {
        return dao.selectList(ids);
    }

    @Override
    public List<T> selectList(Map<String, Object> paramMap) {
        return dao.selectList(paramMap);
    }

    @Override
    public Long count(Map<String, Object> paramMap) {
        return dao.count(paramMap);
    }

    @Override
    public int insert(T po) {
        return dao.insert(po);
    }

    @Override
    public int insertBatch(List<T> list) {
        return dao.insertBatch(list);
    }

    @Override
    public int insertOrUpdate(T po) {
        return dao.insertOrUpdate(po);
    }

    @Override
    public int update(T po) {
        return dao.update(po);
    }

    @Override
    public int updateSelective(T po) {
        return dao.updateSelective(po);
    }

    @Override
    public int delete(List<Number> idList) {
        return dao.delete(idList);
    }

}