package com.qq1833111108.sys.mongo;

import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.qq1833111108.common.mongo.AbstractMongoDao;

@Component
public class UserApiLogMongoDao extends AbstractMongoDao{

    /**
     * 根据createTime的起止日期，手机号，接口状态查询接口日志，结果按createTime日期降序排序
     * @param beginTime
     * @param endTime
     * @param mobileRegex
     * @param status
     * @param devIdRegex
     * @param apiName
     * @param apiUriPath
     * @return
     */
	public List<UserApiLogMongoModel> selectNoPageFromMongo(Date beginTime, Date endTime, UserApiLogMongoModel userApiLogMongoModel,Point[] points, Double maxDistance) {  
        Query query = new Query(); 
        query.with(new Sort(new Order(Direction.DESC,"createTime")));  //默认按createTime日期降序
        query.limit(AbstractMongoDao.RESULT_NORMAL_COUNT);
        if( beginTime==null 
        		&& endTime==null
        		&& userApiLogMongoModel.getStatus()==null 
                && StringUtils.isBlank(userApiLogMongoModel.getMobile())
        		&& StringUtils.isBlank(userApiLogMongoModel.getDevId())
        		&& StringUtils.isBlank(userApiLogMongoModel.getApiName())
        		&& StringUtils.isBlank(userApiLogMongoModel.getApiUriPath())
        		&& (points == null || maxDistance ==null)){
        	query.limit(AbstractMongoDao.RESULT_MIN_COUNT);
        	return getMongoTemplate().find(query, UserApiLogMongoModel.class);    
        }
		Criteria criteria = Criteria.where("");		
		if(beginTime!=null || endTime!=null){
			criteria=criteria.and("createTime");
        	if(beginTime!=null){
        		criteria.gt(beginTime);
        	}
        	if(endTime!=null){
        		criteria.lt(endTime);
        	}
        }
        if(userApiLogMongoModel.getStatus()!=null){
        	criteria=criteria.and("status").is(userApiLogMongoModel.getStatus().intValue());  
        }		
        //手机号正则
		if(StringUtils.isNotBlank(userApiLogMongoModel.getMobile())){
			Pattern mobilePattern = Pattern.compile(userApiLogMongoModel.getMobile(),Pattern.CASE_INSENSITIVE);   
			criteria=criteria.and("mobile").regex(mobilePattern.toString());  
		}
		//设备ID正则
		if(StringUtils.isNotBlank(userApiLogMongoModel.getDevId())){
			Pattern devIdPattern = Pattern.compile(userApiLogMongoModel.getDevId(),Pattern.CASE_INSENSITIVE);   
			criteria=criteria.and("devId").regex(devIdPattern.toString());  
		}		
		//接口名称
		if(StringUtils.isNotBlank(userApiLogMongoModel.getApiName())){
			Pattern apiNamePattern = Pattern.compile(userApiLogMongoModel.getApiName(),Pattern.CASE_INSENSITIVE);   
			criteria=criteria.and("apiName").regex(apiNamePattern.toString());  
		}
		//接口路径
		if(StringUtils.isNotBlank(userApiLogMongoModel.getApiUriPath())){
			Pattern apiUriPathPattern = Pattern.compile(userApiLogMongoModel.getApiUriPath(),Pattern.CASE_INSENSITIVE);   
			criteria=criteria.and("apiUriPath").regex(apiUriPathPattern.toString());  
		}
		//地理位置查询
		if(points!=null && points.length>0){
			query.limit(AbstractMongoDao.RESULT_MAX_COUNT);
			//经纬度，距离范围（单位：千米）
			if(points.length==1 && maxDistance!=null){
				criteria=criteria.and("posGps").near(points[0]).maxDistance(maxDistance/111.12);  
			}
			
			//多边形查询
		}		
		
        query.addCriteria(criteria);
        return getMongoTemplate().find(query, UserApiLogMongoModel.class);  
	}	
	
    /**
     * 根据ID查找对象 
     * @param <T>
     * @param idValue
     * @return
     */
	public UserApiLogMongoModel findOne(String idValue) {
		return findOne(idValue, UserApiLogMongoModel.class);   
	}
	
    /**
     * 查询所有，结果集最多1万条记录
     * @param <T>
     * @param idValue
     * @return
     */
	public List<UserApiLogMongoModel> findAll() {
		return findAll(UserApiLogMongoModel.class);   
	}	
}
