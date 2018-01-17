package com.qq1833111108.sys.mongo;

import java.util.Date;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.Point;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qq1833111108.FirmwareServerApplication;
import com.qq1833111108.core.util.DateUtil;
import com.qq1833111108.sys.mongo.UserApiLogMongoDao;
import com.qq1833111108.sys.mongo.UserApiLogMongoModel;


@RunWith(SpringJUnit4ClassRunner.class)  
@SpringBootTest(classes=FirmwareServerApplication.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@ActiveProfiles("dev")
public class UserApiLogMongoDaoTest {

	@Autowired
    UserApiLogMongoDao userApiLogMongoDao;
 
//	@Test
//	public void testInsert() {
//		UserApiLogMongoModel dto = new UserApiLogMongoModel();
//		dto.setApiName("测试Dao");
//		dto.setApiUriPath("/1111");
//		dto.setReqBody("1");
//		dto.setMobile("13590382516");
//		dto.setCreateTime(DateUtils.parseDate("2017-07-17 15:29:40"));
//		dto.setStatus(1);
//		dto.setDevId("abcd");
//		dto.setPosGps(22.548857,113.94501);
//		userApiLogMongoDao.insert(dto);
//	}    
   
//	@Test
//	public void testFindOne() {
//		Serializable dto = userApiLogMongoDao.findOne("c2e60658-dd21-4fe1-8984-b84aa7ad64c8");
//		System.out.println("testFindOne:"+JSON.toJSONString(dto));
//	}
//	
//	@Test
//	public void testFindAll() {
//		List<UserApiLogModel> list = userApiLogMongoDao.findAll();
//		for(Serializable obj : list){
//			System.out.println("testFindAll:"+JSON.toJSONString(obj));
//		}
//	}	

	@Test
	public void testfindByCreatimeAndMobileAndStatus() {
		Date beginTime = DateUtil.parseDate("2017-06-17 00:29:40");
		Date endTime =   DateUtil.parseDate("2017-12-17 00:29:40");
		System.out.println("beginTime="+beginTime.getTime());
		System.out.println("endTime  ="+endTime.getTime());
		
		String mobileRegex = "135";
		Integer status = 1;
		UserApiLogMongoModel userApiLogMongoModel = new UserApiLogMongoModel();
		userApiLogMongoModel.setMobile(mobileRegex);
		//userApiLogMongoModel.setStatus(status);
		//userApiLogMongoModel.setApiUriPath("/111");
		userApiLogMongoModel.setApiName("获取申请额度信息");
		
		Point[] points = new Point[1];
		points[0] = new Point(22.54903021918403,113.9443204752604);
		Double maxDistance = 0.1; //1公里内
		
		List<UserApiLogMongoModel> list = userApiLogMongoDao.selectNoPageFromMongo(beginTime, endTime, userApiLogMongoModel, points, maxDistance);
		if(list!=null && list.size()>0){
			for(UserApiLogMongoModel obj : list){
		        ObjectMapper mapper = new ObjectMapper();  
		        try {
					String Json =  mapper.writeValueAsString(obj);
					System.out.println("testfindByCreatimeAndMobileAndStatus:Json:"+Json);
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  
			}
		}else{
			System.out.println("data in mongo is none:");
		}
	}

}
