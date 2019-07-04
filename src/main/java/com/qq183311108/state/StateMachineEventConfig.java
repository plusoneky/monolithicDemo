package com.qq183311108.state;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.statemachine.annotation.OnTransition;
import org.springframework.statemachine.annotation.WithStateMachine;

@WithStateMachine(name="InvestOrderStateMachineId")
public class StateMachineEventConfig {

	private final Logger logger = LoggerFactory.getLogger(getClass());

    
    @OnTransition(target = "STATES_0")
    public void toState0(Message message) {
    	logger.info("s0 ttttttttttttttttttttttttttt"+"---message:"+message);
    }
    
    @OnTransition(source="STATES_0",target = "STATES_1")
    public void toState1(Message message) {
    	logger.info("s1 ddddddddddddddddddddddddddd"+"---header:"+message.getHeaders()+",---payload:"+message.getPayload());
    }    

    @OnTransition(source="STATES_1",target = "STATES_2")
    public void toState2(Message message) {
    	logger.info("s2 cccccccccccccccccccccccccccc"+"---header:"+message.getHeaders()+",---payload:"+message.getPayload());
    }
    
    @OnTransition(source="STATES_2",target = "STATES_3")
    public void toState3(Message message) {
    	logger.info("s3 hhhhhhhhhhhhhhhhhhhhhhhhhhhh"+"---header:"+message.getHeaders()+",---payload:"+message.getPayload());
    }
    
    @OnTransition(source="STATES_3",target = "STATES_4")
    public void toState4(Message message) {
    	logger.info("s4 ggggggggggggggggggggggggggggg"+"---header:"+message.getHeaders()+",---payload:"+message.getPayload());
    }    

}
