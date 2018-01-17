package com.qq1833111108.redis;

import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.qq1833111108.FirmwareServerApplication;
import com.qq1833111108.sys.entity.User;

@RunWith(SpringJUnit4ClassRunner.class)  
@SpringBootTest(classes=FirmwareServerApplication.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@ActiveProfiles("dev")
public class TestRedis {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    
    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void test101_String() throws Exception {
        stringRedisTemplate.opsForValue().set("aaa", "111");
        Assert.assertEquals("111", stringRedisTemplate.opsForValue().get("aaa"));
    }
    
    @Test
    public void test102_Object() throws Exception {
        User user = new User();
        user.setEmail("testdev@163.com");
        user.setNickname("wangwu");
        
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        operations.set("com.qq183311108", user);
        operations.set("com.qq183311108.f", user,1,TimeUnit.SECONDS);
        Thread.sleep(2000);
        
        //redisTemplate.delete("com.qq183311108.f");
        boolean exists1 = redisTemplate.hasKey("com.qq183311108");
        if(exists1){
            System.out.println("com.qq183311108 exists is true");
        }else{
            System.out.println("com.qq183311108 exists is false");
        }        
        
        boolean exists2 = redisTemplate.hasKey("com.qq183311108.f");
        if(exists2){
            System.out.println("com.qq183311108.f exists is true");
        }else{
            System.out.println("com.qq183311108.f exists is false");
        }
       // Assert.assertEquals("aa", operations.get("com.qq183311108.f").getUserName());
    }
}
