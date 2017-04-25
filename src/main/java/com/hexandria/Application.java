package com.hexandria;

import com.hexandria.websocket.GameSocketHandler;
import com.hexandria.websocket.MyWebSocketHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.handler.PerConnectionWebSocketHandler;

/**
 * Created by root on 20.02.17.
 */
@SpringBootApplication
public class Application {


    @Bean
    public WebSocketHandler myWebSocketHandler() {
        return new PerConnectionWebSocketHandler(MyWebSocketHandler.class);
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(new Object[]{WebSocketConfig.class, Application.class}, args);
    }
}
