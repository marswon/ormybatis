package cn.bblink.common.ormybaits.example;

import static cn.bblink.common.ormybaits.example.HospitalInfo.F_CREATE_TIME;
import static cn.bblink.common.ormybaits.example.HospitalInfo.F_HOSPITAL_ID;
import static cn.bblink.common.ormybaits.example.HospitalInfo.F_HOSPITAL_NAME;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.bblink.common.ormybatis.builder.UpdateBuilder;
import cn.bblink.common.ormybatis.builder.WhereBuilder;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/spring/spring-core.xml"})
public class HospitalInfoDaoTest {
	protected final Logger log = LoggerFactory.getLogger(getClass());
	  
    @Resource
    private HospitalInfoDao dao;

    @Test
    public void paging() {
       WhereBuilder where = new WhereBuilder()
        	.likeRight(F_HOSPITAL_NAME, "保健院").orderDesc(F_HOSPITAL_ID);
        dao.paging(1, 20, where);
    }

    @Test
    public void findById() {
        HospitalInfo hos = dao.selectOne(80);
        log.info("findById:{}",hos);
    }

    @Test
    public void whereIn() {
        WhereBuilder where = new WhereBuilder().in(F_HOSPITAL_ID, 54, 55);
        List<HospitalInfo> list = dao.selectList(where);
        log.info("whereIn:{}", list);
    }

    @Test
    public void findOneByMap() {
        Map paramMap = new HashMap();
        paramMap.put(F_HOSPITAL_NAME, "贝联公司（上海）Aruba");
        HospitalInfo hos = dao.selectOne(paramMap);
        log.info("findOneByMap:{}", hos);
    }

    @Test
    public void findBetweenDate() {
        WhereBuilder qb = new WhereBuilder()
                .betweenDate(F_CREATE_TIME, "2015-10-22", "2015-10-24")
                .orderDesc(F_HOSPITAL_ID)
                .limit(50);
        List<HospitalInfo> list = dao.selectList(qb);
        log.info("findBetweenDate:{}", list);
    }

    @Test
    public void countByMap() {
        Map paramMap = new HashMap();
        paramMap.put(F_HOSPITAL_NAME, "贝联公司（上海）Aruba");
        Long count = dao.count(paramMap);
        log.info("countByMap:{}", count);
    }
    

    @Test
    public void insert() {
        HospitalInfo hos = new HospitalInfo();
        hos.setHospitalName("xx医院name");
        hos.setCreateUserId(100L);
        hos.setCreateTime(new Date());
        int num = dao.insert(hos);
        log.debug("insert num:[{}]", num);
    }
    
    @Test
    public void insertBatch() {
        HospitalInfo hos = new HospitalInfo();
        hos.setHospitalName("xx医院name1");
        hos.setCreateUserId(100L);
        hos.setCreateTime(new Date());

        HospitalInfo hos1 = new HospitalInfo();
        hos1.setHospitalName("医院name2");
        hos1.setCreateUserId(10L);
        hos1.setCreateTime(new Date());

        List<HospitalInfo> list = Arrays.asList(hos, hos1);
        int num = dao.insertBatch(list);
        log.debug("批量insert num:[{}]", num);
    }
  
    @Test
    public void update() {
    	Long id = 100L;
        HospitalInfo hos = new HospitalInfo();
        hos.setId(id);
        hos.setHospitalName("update hos nam");
        int num = dao.update(hos);
        log.debug("修改记录数 :{}", num);
        log.debug("修改后的记录:{}", dao.selectOne(id));
    }
    
    @Test
    public void updateBuilder() {
    	Long id = 100L;
    	UpdateBuilder updateBuilder = new UpdateBuilder()
    	.whereEq(F_HOSPITAL_ID, id)
    	.upateEqNull(F_HOSPITAL_NAME);
    	int num = dao.update(updateBuilder);
    	log.debug("修改记录数 :{}", num);
    	log.debug("修改后的记录:{}", dao.selectOne(id));
    }
    
    @Test
    public void insertOrUpdate() {
    	Long id = 1000010L;
    	HospitalInfo hos = new HospitalInfo();
    	hos.setId(id);
    	hos.setHospitalAbbre("abbre");
        int num = dao.insertOrUpdate(hos);
        log.debug("insertOrUpdate数 :{}", num);
        log.debug(hos.toString());
    }
    
    @Test
    public void deleteById() {
    	Long id = 1000010L;
    	int num = dao.delete(id);
    	log.debug("delete num :{}", num);
    }
    
    @Test
    public void deleteByBuilder() {
    	WhereBuilder whereBuilder = new WhereBuilder()
    		.like(F_HOSPITAL_NAME, "医院name");
        int num = dao.delete(whereBuilder);
        log.debug("delete num :{}", num);
    }
}