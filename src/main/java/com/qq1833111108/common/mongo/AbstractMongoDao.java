package com.qq1833111108.common.mongo;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

/**
 * @author lijiabei
 *
 */
public abstract class AbstractMongoDao implements IMongoDBDao{

	public static final int RESULT_MAX_COUNT = 1000;  //结果集最大条数
	
	public static final int RESULT_NORMAL_COUNT = 500;  //结果集通用条数
	
	public static final int RESULT_MIN_COUNT = 100;  //结果集通用条数
	
	public static final String KEY_NAME = "id";        //默认的关键字名称
	
	@Resource
	private MongoTemplate mongoTemplate;   

	
	public MongoTemplate getMongoTemplate() {
		return mongoTemplate;
	}

	public void setMongoTemplate(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	public void insert(MongoDBBaseModel object) {
		object.setId(UUID.randomUUID().toString());   //默认添加主键
		getMongoTemplate().insert(object);   
	}
	
	@Override
	public void removeOne(String idValue) {
        Criteria criteria = Criteria.where(KEY_NAME).in(idValue);     
        if(criteria == null){     
             Query query = new Query(criteria);     
             if(query != null && getMongoTemplate().findOne(query, Serializable.class) != null)     
                 getMongoTemplate().remove(getMongoTemplate().findOne(query, Serializable.class));     
        }  
	}
	
    /**
     * 根据ID查找对象 
     * @param <T>
     * @param idValue
     * @return
     */
	protected <T> T findOne(String idValue, Class<T> entityClass) {
		return getMongoTemplate().findOne(new Query(Criteria.where(KEY_NAME).is(idValue)), entityClass);   
	}

    /**
     * 查询所有，结果集最多1万条记录
     * @return
     */
	protected <T> List<T> findAll(Class<T> entityClass) {
        Query query = new Query(); 
        query.limit(RESULT_MAX_COUNT);
		return getMongoTemplate().find(query, entityClass);    
	}

    /**
     * 根据正则查询
     * @param regex
     * @return
     */
	protected <T> List<T> findByKeyAndRegex(String keyName, String regex, Class<T> entityClass) {
		Pattern pattern = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);     
		Criteria criteria = new Criteria(keyName).regex(pattern.toString());    
		Query query = new Query(criteria); 
        query.limit(RESULT_MAX_COUNT);
        return getMongoTemplate().find(query, entityClass);  
	}
	
    /**
     * 修改指定的对象
     * @param idValue
     * @param update
     * @param entityClass
     */
	protected void findAndModify(String idValue,Update update, Class<?> entityClass) {
		getMongoTemplate().updateFirst(new Query(Criteria.where(KEY_NAME).is(idValue)), update, entityClass);  
	}	

}
