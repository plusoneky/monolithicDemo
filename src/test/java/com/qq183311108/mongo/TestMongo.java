package com.qq183311108.mongo;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.qq183311108.FirmwareServerApplication;

@RunWith(SpringJUnit4ClassRunner.class)  
@SpringBootTest(classes=FirmwareServerApplication.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@ActiveProfiles("dev")
public class TestMongo {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void test101_DbName() throws Exception {
    	String dbname = mongoTemplate.getDb().getName();
    	System.out.println("mongodb:"+dbname);
    }
}
