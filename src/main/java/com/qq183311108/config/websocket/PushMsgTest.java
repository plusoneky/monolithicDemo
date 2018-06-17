package com.qq183311108.config.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.qq183311108.config.websocket.ServerMessage;

/**
 * @author
 * https://blog.csdn.net/qq_28988969/article/details/78113463
 */
public class PushMsgTest {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    
    //客户端只要订阅了/topic/subscribeTest主题，调用这个方法即可
    public void templateTest() {
        messagingTemplate.convertAndSend("/topic/subscribeTest", new ServerMessage("服务器主动推的数据"));
    }

}
