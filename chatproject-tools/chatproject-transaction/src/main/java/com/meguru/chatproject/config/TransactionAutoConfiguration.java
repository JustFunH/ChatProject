package com.meguru.chatproject.config;

import com.meguru.chatproject.aspect.SecureInvokeAspect;
import com.meguru.chatproject.mapper.SecureInvokeRecordMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@MapperScan(basePackageClasses = SecureInvokeRecordMapper.class)
@Import({SecureInvokeAspect.class})
public class TransactionAutoConfiguration {

}
