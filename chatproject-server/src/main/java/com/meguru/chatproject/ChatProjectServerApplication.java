package com.meguru.chatproject;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication(scanBasePackages = {"com.meguru.chatproject"})
@MapperScan({"com.meguru.chatproject.**.mapper"})
@ServletComponentScan
public class ChatProjectServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatProjectServerApplication.class, args);
    }

}
