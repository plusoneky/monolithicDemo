package com.qq1833111108.common.mongo;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.mongodb.core.query.Update;

/**
 * @author lijiabei
 *
 */
public interface IMongoDBDao {
	
	
    /**
     * 添加对象 
     * @param person
     */
    void insert(MongoDBBaseModel object);   

    /**
     * 删除指定的ID对象 
     * @param idValue
     */
    void removeOne(String idValue);    
     
}
