package com.qq183311108.state;

import java.util.EnumSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.persist.DefaultStateMachinePersister;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.DefaultStateMachineContext;

import com.qq183311108.state.Consts.OrderEvents;



@Configuration
//@EnableStateMachine
@EnableStateMachineFactory
public class StateMachineConfig extends EnumStateMachineConfigurerAdapter<States, OrderEvents> {
	
	private static final Logger logger = LoggerFactory.getLogger(StateMachineConfig.class);
	
	@Override
    public void configure(StateMachineConfigurationConfigurer<States, OrderEvents> config)
            throws Exception {
        config
            .withConfiguration()
                .autoStartup(true)
                .listener(listener());
    }

    
    /**
     * 初始化状态机状态
     */
    @Override
    public void configure(StateMachineStateConfigurer<States, OrderEvents> states) throws Exception {
        states.withStates()
        // 定义初始状态
        .initial(States.STATES_0)
        // 定义状态机状态
        .states(EnumSet.allOf(States.class));
    }
    /**
     * 初始化状态迁移事件
     */
    @Override
    public void configure(StateMachineTransitionConfigurer<States, OrderEvents> transitions)throws Exception {
        transitions
            // 1.连接事件
            // 未连接 -> 已连接
            .withExternal()
                .source(States.STATES_0)
                .target(States.STATES_1)
                .event(OrderEvents.E1)
            .and()                     
            
            // 2.注册事件   
            // 已连接 -> 注册中
            .withExternal()
            .source(States.STATES_1)
            .target(States.STATES_2)
            .event(OrderEvents.E2)
            .and()
            
            // 3.注册成功事件   
            // 注册中 -> 已注册
            .withExternal()
            .source(States.STATES_2)
            .target(States.STATES_3)
            .event(OrderEvents.E3)
            .and()
            
            // 5.注销事件
            // 已连接 -> 未连接
            .withExternal()
            .source(States.STATES_3)
            .target(States.STATES_4)
            .event(OrderEvents.E4)
            ;
    }   
    
    @Bean
    public StateMachineListener<States, OrderEvents> listener() {
        return new StateMachineListenerAdapter<States, OrderEvents>() {
            @Override
            public void stateChanged(State<States, OrderEvents> from, State<States, OrderEvents> to) {
            	logger.info("stateChanged:"+(from!=null?from.getId():"no from id")+" State change to " + (to!=null?to.getId():"no to id"));
            }
        };
    }
    
    
	@Bean 
	public StateMachinePersister<States, OrderEvents, InvestOrder> persister1111(){
		return new DefaultStateMachinePersister<States, OrderEvents, InvestOrder>(new StateMachinePersist<States, OrderEvents, InvestOrder>(){ 
			@Override 
			public void write(StateMachineContext<States, OrderEvents> context, InvestOrder order) throws Exception { 
				System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
				//此处并没有进行持久化操作 
				//order.setStatus(context.getState()); 
		    } 
	        
			@Override public StateMachineContext<States, OrderEvents> read(InvestOrder order) throws Exception { 
				System.out.println("bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb");
				//此处直接获取order中的状态，其实并没有进行持久化读取操作 
			    StateMachineContext<States, OrderEvents> result = new DefaultStateMachineContext<States, OrderEvents>(order.getOrderStatus(), null, null, null, null, "InvestOrderStateMachineId"); 
			    return result; 
			} 
		});
	}
}
