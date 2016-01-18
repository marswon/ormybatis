package cn.bblink.common.ormybaits.example;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.bblink.common.ormybatis.dao.BaseDao;

@Component
public class HospitalInfoDao extends BaseDao<HospitalInfo>{
	
	@Autowired
	public HospitalInfoDao(SqlSessionFactory sqlSessionFactory) {
		super(sqlSessionFactory);
		super.setPrimaryKeyName(HospitalInfo.F_HOSPITAL_ID);
		super.setTableName(HospitalInfo.DB_TABLE_NAME);
	}
	
}