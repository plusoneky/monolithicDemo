package com.qq183311108.state;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.qq183311108.state.Consts.OrderEvents;

@Component
@Order(value=1)
/*在SpringBoot中，有两种接口方式实现启动执行，分别是ApplicationRunner和CommandLineRunner，除了可接受参数不同，其他的大同小异
 *实现ApplicationListener<ContextRefreshedEvent>接口，与上面的区别是，这是WEB项目，是在加载后，WEB流量还没有进来前执行*/
public class DemoCommandLineRunner implements CommandLineRunner{

	private static final Logger logger = LoggerFactory.getLogger(DemoCommandLineRunner.class);
	
	//@Autowired
	//private StateMachine<States, OrderEvents> stateMachine;
	
	@Autowired
	StateMachineFactory<States, OrderEvents> stateMachineFactory;
	
	@Autowired
    private StateMachinePersister<States, OrderEvents, InvestOrder> persister;

	@Override
	public void run(String... args) throws Exception {
		logger.info(">>>>>>>>>>>>>>>服务启动执行，执行加载数据等操作<<<<<<<<<<<<<");
		
		InvestOrder order = new InvestOrder();
		order.setOrderId(1234);
		order.setOrderName("testOrder");
		order.setOrderStatus(States.STATES_0);
		
		StateMachine<States, OrderEvents> stateMachine = getStateMachine(order);
		
		logger.info(">>>>>>>>>>>>>>>>>>>>start 当前状态："+stateMachine.getState());
		
	    stateMachine.sendEvent(MessageBuilder.withPayload(OrderEvents.E1).setHeader("orderJsonStr", JSON.toJSON(order)).build());
	    logger.info(">>>>>>>>>>>>>>>>>>>>E1 当前状态："+stateMachine.getState());
	    stateMachine.sendEvent(MessageBuilder.withPayload(OrderEvents.E2).setHeader("orderJsonStr", JSON.toJSON(order)).build());
	    logger.info(">>>>>>>>>>>>>>>>>>>>E2 当前状态："+stateMachine.getState());
	    stateMachine.sendEvent(OrderEvents.E3);
	    logger.info(">>>>>>>>>>>>>>>>>>>>E3 当前状态："+stateMachine.getState());
	    stateMachine.sendEvent(OrderEvents.E4);
	    logger.info(">>>>>>>>>>>>>>>>>>>>E4 当前状态："+stateMachine.getState());
	}
	
	private StateMachine<States, OrderEvents> getStateMachine(InvestOrder order){ 
		StateMachine<States, OrderEvents> machine = stateMachineFactory.getStateMachine("InvestOrderStateMachineId"); 
		try { 
			//machine.start(); 
			persister.restore(machine, order); 
		} catch (Exception e) { 
			e.printStackTrace(); 
			return machine; 
		}
		return machine;
	}


}
